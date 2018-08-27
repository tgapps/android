package com.google.android.gms.measurement.internal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteFullException;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import com.google.android.gms.common.util.Clock;

public final class zzal extends zzf {
    private final zzam zzalq = new zzam(this, getContext(), "google_app_measurement_local.db");
    private boolean zzalr;

    zzal(zzbt com_google_android_gms_measurement_internal_zzbt) {
        super(com_google_android_gms_measurement_internal_zzbt);
    }

    public final java.util.List<com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable> zzr(int r14) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Unreachable block: B:150:0x013b
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.modifyBlocksTree(BlockProcessor.java:248)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.rerun(BlockProcessor.java:44)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.visit(BlockFinallyExtract.java:57)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
        /*
        r13 = this;
        r13.zzaf();
        r13.zzgb();
        r0 = r13.zzalr;
        if (r0 == 0) goto L_0x000c;
    L_0x000a:
        r0 = 0;
    L_0x000b:
        return r0;
    L_0x000c:
        r10 = new java.util.ArrayList;
        r10.<init>();
        r0 = r13.getContext();
        r1 = "google_app_measurement_local.db";
        r0 = r0.getDatabasePath(r1);
        r0 = r0.exists();
        if (r0 != 0) goto L_0x0024;
    L_0x0022:
        r0 = r10;
        goto L_0x000b;
    L_0x0024:
        r9 = 5;
        r0 = 0;
        r12 = r0;
    L_0x0027:
        r0 = 5;
        if (r12 >= r0) goto L_0x01e8;
    L_0x002a:
        r3 = 0;
        r11 = 0;
        r0 = r13.getWritableDatabase();	 Catch:{ SQLiteFullException -> 0x021a, SQLiteDatabaseLockedException -> 0x0211, SQLiteException -> 0x0207, all -> 0x01f9 }
        if (r0 != 0) goto L_0x003c;
    L_0x0032:
        r1 = 1;
        r13.zzalr = r1;	 Catch:{ SQLiteFullException -> 0x021f, SQLiteDatabaseLockedException -> 0x0215, SQLiteException -> 0x020c, all -> 0x01fe }
        if (r0 == 0) goto L_0x003a;
    L_0x0037:
        r0.close();
    L_0x003a:
        r0 = 0;
        goto L_0x000b;
    L_0x003c:
        r0.beginTransaction();	 Catch:{ SQLiteFullException -> 0x021f, SQLiteDatabaseLockedException -> 0x0215, SQLiteException -> 0x020c, all -> 0x01fe }
        r1 = "messages";	 Catch:{ SQLiteFullException -> 0x021f, SQLiteDatabaseLockedException -> 0x0215, SQLiteException -> 0x020c, all -> 0x01fe }
        r2 = 3;	 Catch:{ SQLiteFullException -> 0x021f, SQLiteDatabaseLockedException -> 0x0215, SQLiteException -> 0x020c, all -> 0x01fe }
        r2 = new java.lang.String[r2];	 Catch:{ SQLiteFullException -> 0x021f, SQLiteDatabaseLockedException -> 0x0215, SQLiteException -> 0x020c, all -> 0x01fe }
        r3 = 0;	 Catch:{ SQLiteFullException -> 0x021f, SQLiteDatabaseLockedException -> 0x0215, SQLiteException -> 0x020c, all -> 0x01fe }
        r4 = "rowid";	 Catch:{ SQLiteFullException -> 0x021f, SQLiteDatabaseLockedException -> 0x0215, SQLiteException -> 0x020c, all -> 0x01fe }
        r2[r3] = r4;	 Catch:{ SQLiteFullException -> 0x021f, SQLiteDatabaseLockedException -> 0x0215, SQLiteException -> 0x020c, all -> 0x01fe }
        r3 = 1;	 Catch:{ SQLiteFullException -> 0x021f, SQLiteDatabaseLockedException -> 0x0215, SQLiteException -> 0x020c, all -> 0x01fe }
        r4 = "type";	 Catch:{ SQLiteFullException -> 0x021f, SQLiteDatabaseLockedException -> 0x0215, SQLiteException -> 0x020c, all -> 0x01fe }
        r2[r3] = r4;	 Catch:{ SQLiteFullException -> 0x021f, SQLiteDatabaseLockedException -> 0x0215, SQLiteException -> 0x020c, all -> 0x01fe }
        r3 = 2;	 Catch:{ SQLiteFullException -> 0x021f, SQLiteDatabaseLockedException -> 0x0215, SQLiteException -> 0x020c, all -> 0x01fe }
        r4 = "entry";	 Catch:{ SQLiteFullException -> 0x021f, SQLiteDatabaseLockedException -> 0x0215, SQLiteException -> 0x020c, all -> 0x01fe }
        r2[r3] = r4;	 Catch:{ SQLiteFullException -> 0x021f, SQLiteDatabaseLockedException -> 0x0215, SQLiteException -> 0x020c, all -> 0x01fe }
        r3 = 0;	 Catch:{ SQLiteFullException -> 0x021f, SQLiteDatabaseLockedException -> 0x0215, SQLiteException -> 0x020c, all -> 0x01fe }
        r4 = 0;	 Catch:{ SQLiteFullException -> 0x021f, SQLiteDatabaseLockedException -> 0x0215, SQLiteException -> 0x020c, all -> 0x01fe }
        r5 = 0;	 Catch:{ SQLiteFullException -> 0x021f, SQLiteDatabaseLockedException -> 0x0215, SQLiteException -> 0x020c, all -> 0x01fe }
        r6 = 0;	 Catch:{ SQLiteFullException -> 0x021f, SQLiteDatabaseLockedException -> 0x0215, SQLiteException -> 0x020c, all -> 0x01fe }
        r7 = "rowid asc";	 Catch:{ SQLiteFullException -> 0x021f, SQLiteDatabaseLockedException -> 0x0215, SQLiteException -> 0x020c, all -> 0x01fe }
        r8 = 100;	 Catch:{ SQLiteFullException -> 0x021f, SQLiteDatabaseLockedException -> 0x0215, SQLiteException -> 0x020c, all -> 0x01fe }
        r8 = java.lang.Integer.toString(r8);	 Catch:{ SQLiteFullException -> 0x021f, SQLiteDatabaseLockedException -> 0x0215, SQLiteException -> 0x020c, all -> 0x01fe }
        r2 = r0.query(r1, r2, r3, r4, r5, r6, r7, r8);	 Catch:{ SQLiteFullException -> 0x021f, SQLiteDatabaseLockedException -> 0x0215, SQLiteException -> 0x020c, all -> 0x01fe }
        r4 = -1;
    L_0x006a:
        r1 = r2.moveToNext();	 Catch:{ SQLiteFullException -> 0x009f, SQLiteDatabaseLockedException -> 0x00d6, SQLiteException -> 0x00ee, all -> 0x013b }
        if (r1 == 0) goto L_0x01ad;	 Catch:{ SQLiteFullException -> 0x009f, SQLiteDatabaseLockedException -> 0x00d6, SQLiteException -> 0x00ee, all -> 0x013b }
    L_0x0070:
        r1 = 0;	 Catch:{ SQLiteFullException -> 0x009f, SQLiteDatabaseLockedException -> 0x00d6, SQLiteException -> 0x00ee, all -> 0x013b }
        r4 = r2.getLong(r1);	 Catch:{ SQLiteFullException -> 0x009f, SQLiteDatabaseLockedException -> 0x00d6, SQLiteException -> 0x00ee, all -> 0x013b }
        r1 = 1;	 Catch:{ SQLiteFullException -> 0x009f, SQLiteDatabaseLockedException -> 0x00d6, SQLiteException -> 0x00ee, all -> 0x013b }
        r1 = r2.getInt(r1);	 Catch:{ SQLiteFullException -> 0x009f, SQLiteDatabaseLockedException -> 0x00d6, SQLiteException -> 0x00ee, all -> 0x013b }
        r3 = 2;	 Catch:{ SQLiteFullException -> 0x009f, SQLiteDatabaseLockedException -> 0x00d6, SQLiteException -> 0x00ee, all -> 0x013b }
        r6 = r2.getBlob(r3);	 Catch:{ SQLiteFullException -> 0x009f, SQLiteDatabaseLockedException -> 0x00d6, SQLiteException -> 0x00ee, all -> 0x013b }
        if (r1 != 0) goto L_0x0118;	 Catch:{ SQLiteFullException -> 0x009f, SQLiteDatabaseLockedException -> 0x00d6, SQLiteException -> 0x00ee, all -> 0x013b }
    L_0x0081:
        r3 = android.os.Parcel.obtain();	 Catch:{ SQLiteFullException -> 0x009f, SQLiteDatabaseLockedException -> 0x00d6, SQLiteException -> 0x00ee, all -> 0x013b }
        r1 = 0;
        r7 = r6.length;	 Catch:{ ParseException -> 0x00c3, all -> 0x00e9 }
        r3.unmarshall(r6, r1, r7);	 Catch:{ ParseException -> 0x00c3, all -> 0x00e9 }
        r1 = 0;	 Catch:{ ParseException -> 0x00c3, all -> 0x00e9 }
        r3.setDataPosition(r1);	 Catch:{ ParseException -> 0x00c3, all -> 0x00e9 }
        r1 = com.google.android.gms.measurement.internal.zzad.CREATOR;	 Catch:{ ParseException -> 0x00c3, all -> 0x00e9 }
        r1 = r1.createFromParcel(r3);	 Catch:{ ParseException -> 0x00c3, all -> 0x00e9 }
        r1 = (com.google.android.gms.measurement.internal.zzad) r1;	 Catch:{ ParseException -> 0x00c3, all -> 0x00e9 }
        r3.recycle();
        if (r1 == 0) goto L_0x006a;	 Catch:{ SQLiteFullException -> 0x009f, SQLiteDatabaseLockedException -> 0x00d6, SQLiteException -> 0x00ee, all -> 0x013b }
    L_0x009b:
        r10.add(r1);	 Catch:{ SQLiteFullException -> 0x009f, SQLiteDatabaseLockedException -> 0x00d6, SQLiteException -> 0x00ee, all -> 0x013b }
        goto L_0x006a;
    L_0x009f:
        r1 = move-exception;
        r3 = r0;
    L_0x00a1:
        r0 = r13.zzgo();	 Catch:{ all -> 0x0203 }
        r0 = r0.zzjd();	 Catch:{ all -> 0x0203 }
        r4 = "Error reading entries from local database";	 Catch:{ all -> 0x0203 }
        r0.zzg(r4, r1);	 Catch:{ all -> 0x0203 }
        r0 = 1;	 Catch:{ all -> 0x0203 }
        r13.zzalr = r0;	 Catch:{ all -> 0x0203 }
        if (r2 == 0) goto L_0x00b7;
    L_0x00b4:
        r2.close();
    L_0x00b7:
        if (r3 == 0) goto L_0x0224;
    L_0x00b9:
        r3.close();
        r0 = r9;
    L_0x00bd:
        r1 = r12 + 1;
        r12 = r1;
        r9 = r0;
        goto L_0x0027;
    L_0x00c3:
        r1 = move-exception;
        r1 = r13.zzgo();	 Catch:{ ParseException -> 0x00c3, all -> 0x00e9 }
        r1 = r1.zzjd();	 Catch:{ ParseException -> 0x00c3, all -> 0x00e9 }
        r6 = "Failed to load event from local database";	 Catch:{ ParseException -> 0x00c3, all -> 0x00e9 }
        r1.zzbx(r6);	 Catch:{ ParseException -> 0x00c3, all -> 0x00e9 }
        r3.recycle();
        goto L_0x006a;
    L_0x00d6:
        r1 = move-exception;
        r3 = r0;
    L_0x00d8:
        r0 = (long) r9;
        android.os.SystemClock.sleep(r0);	 Catch:{ all -> 0x0203 }
        r0 = r9 + 20;
        if (r2 == 0) goto L_0x00e3;
    L_0x00e0:
        r2.close();
    L_0x00e3:
        if (r3 == 0) goto L_0x00bd;
    L_0x00e5:
        r3.close();
        goto L_0x00bd;
    L_0x00e9:
        r1 = move-exception;
        r3.recycle();	 Catch:{ SQLiteFullException -> 0x009f, SQLiteDatabaseLockedException -> 0x00d6, SQLiteException -> 0x00ee, all -> 0x013b }
        throw r1;	 Catch:{ SQLiteFullException -> 0x009f, SQLiteDatabaseLockedException -> 0x00d6, SQLiteException -> 0x00ee, all -> 0x013b }
    L_0x00ee:
        r1 = move-exception;
        r3 = r0;
    L_0x00f0:
        if (r3 == 0) goto L_0x00fb;
    L_0x00f2:
        r0 = r3.inTransaction();	 Catch:{ all -> 0x0203 }
        if (r0 == 0) goto L_0x00fb;	 Catch:{ all -> 0x0203 }
    L_0x00f8:
        r3.endTransaction();	 Catch:{ all -> 0x0203 }
    L_0x00fb:
        r0 = r13.zzgo();	 Catch:{ all -> 0x0203 }
        r0 = r0.zzjd();	 Catch:{ all -> 0x0203 }
        r4 = "Error reading entries from local database";	 Catch:{ all -> 0x0203 }
        r0.zzg(r4, r1);	 Catch:{ all -> 0x0203 }
        r0 = 1;	 Catch:{ all -> 0x0203 }
        r13.zzalr = r0;	 Catch:{ all -> 0x0203 }
        if (r2 == 0) goto L_0x0111;
    L_0x010e:
        r2.close();
    L_0x0111:
        if (r3 == 0) goto L_0x0224;
    L_0x0113:
        r3.close();
        r0 = r9;
        goto L_0x00bd;
    L_0x0118:
        r3 = 1;
        if (r1 != r3) goto L_0x0161;
    L_0x011b:
        r7 = android.os.Parcel.obtain();	 Catch:{ SQLiteFullException -> 0x009f, SQLiteDatabaseLockedException -> 0x00d6, SQLiteException -> 0x00ee, all -> 0x013b }
        r3 = 0;
        r1 = 0;
        r8 = r6.length;	 Catch:{ ParseException -> 0x0148, all -> 0x015c }
        r7.unmarshall(r6, r1, r8);	 Catch:{ ParseException -> 0x0148, all -> 0x015c }
        r1 = 0;	 Catch:{ ParseException -> 0x0148, all -> 0x015c }
        r7.setDataPosition(r1);	 Catch:{ ParseException -> 0x0148, all -> 0x015c }
        r1 = com.google.android.gms.measurement.internal.zzfh.CREATOR;	 Catch:{ ParseException -> 0x0148, all -> 0x015c }
        r1 = r1.createFromParcel(r7);	 Catch:{ ParseException -> 0x0148, all -> 0x015c }
        r1 = (com.google.android.gms.measurement.internal.zzfh) r1;	 Catch:{ ParseException -> 0x0148, all -> 0x015c }
        r7.recycle();
    L_0x0134:
        if (r1 == 0) goto L_0x006a;	 Catch:{ SQLiteFullException -> 0x009f, SQLiteDatabaseLockedException -> 0x00d6, SQLiteException -> 0x00ee, all -> 0x013b }
    L_0x0136:
        r10.add(r1);	 Catch:{ SQLiteFullException -> 0x009f, SQLiteDatabaseLockedException -> 0x00d6, SQLiteException -> 0x00ee, all -> 0x013b }
        goto L_0x006a;
    L_0x013b:
        r1 = move-exception;
        r3 = r0;
    L_0x013d:
        if (r2 == 0) goto L_0x0142;
    L_0x013f:
        r2.close();
    L_0x0142:
        if (r3 == 0) goto L_0x0147;
    L_0x0144:
        r3.close();
    L_0x0147:
        throw r1;
    L_0x0148:
        r1 = move-exception;
        r1 = r13.zzgo();	 Catch:{ ParseException -> 0x0148, all -> 0x015c }
        r1 = r1.zzjd();	 Catch:{ ParseException -> 0x0148, all -> 0x015c }
        r6 = "Failed to load user property from local database";	 Catch:{ ParseException -> 0x0148, all -> 0x015c }
        r1.zzbx(r6);	 Catch:{ ParseException -> 0x0148, all -> 0x015c }
        r7.recycle();
        r1 = r3;	 Catch:{ SQLiteFullException -> 0x009f, SQLiteDatabaseLockedException -> 0x00d6, SQLiteException -> 0x00ee, all -> 0x013b }
        goto L_0x0134;	 Catch:{ SQLiteFullException -> 0x009f, SQLiteDatabaseLockedException -> 0x00d6, SQLiteException -> 0x00ee, all -> 0x013b }
    L_0x015c:
        r1 = move-exception;	 Catch:{ SQLiteFullException -> 0x009f, SQLiteDatabaseLockedException -> 0x00d6, SQLiteException -> 0x00ee, all -> 0x013b }
        r7.recycle();	 Catch:{ SQLiteFullException -> 0x009f, SQLiteDatabaseLockedException -> 0x00d6, SQLiteException -> 0x00ee, all -> 0x013b }
        throw r1;	 Catch:{ SQLiteFullException -> 0x009f, SQLiteDatabaseLockedException -> 0x00d6, SQLiteException -> 0x00ee, all -> 0x013b }
    L_0x0161:
        r3 = 2;	 Catch:{ SQLiteFullException -> 0x009f, SQLiteDatabaseLockedException -> 0x00d6, SQLiteException -> 0x00ee, all -> 0x013b }
        if (r1 != r3) goto L_0x019d;	 Catch:{ SQLiteFullException -> 0x009f, SQLiteDatabaseLockedException -> 0x00d6, SQLiteException -> 0x00ee, all -> 0x013b }
    L_0x0164:
        r7 = android.os.Parcel.obtain();	 Catch:{ SQLiteFullException -> 0x009f, SQLiteDatabaseLockedException -> 0x00d6, SQLiteException -> 0x00ee, all -> 0x013b }
        r3 = 0;
        r1 = 0;
        r8 = r6.length;	 Catch:{ ParseException -> 0x0184, all -> 0x0198 }
        r7.unmarshall(r6, r1, r8);	 Catch:{ ParseException -> 0x0184, all -> 0x0198 }
        r1 = 0;	 Catch:{ ParseException -> 0x0184, all -> 0x0198 }
        r7.setDataPosition(r1);	 Catch:{ ParseException -> 0x0184, all -> 0x0198 }
        r1 = com.google.android.gms.measurement.internal.zzl.CREATOR;	 Catch:{ ParseException -> 0x0184, all -> 0x0198 }
        r1 = r1.createFromParcel(r7);	 Catch:{ ParseException -> 0x0184, all -> 0x0198 }
        r1 = (com.google.android.gms.measurement.internal.zzl) r1;	 Catch:{ ParseException -> 0x0184, all -> 0x0198 }
        r7.recycle();
    L_0x017d:
        if (r1 == 0) goto L_0x006a;	 Catch:{ SQLiteFullException -> 0x009f, SQLiteDatabaseLockedException -> 0x00d6, SQLiteException -> 0x00ee, all -> 0x013b }
    L_0x017f:
        r10.add(r1);	 Catch:{ SQLiteFullException -> 0x009f, SQLiteDatabaseLockedException -> 0x00d6, SQLiteException -> 0x00ee, all -> 0x013b }
        goto L_0x006a;
    L_0x0184:
        r1 = move-exception;
        r1 = r13.zzgo();	 Catch:{ ParseException -> 0x0184, all -> 0x0198 }
        r1 = r1.zzjd();	 Catch:{ ParseException -> 0x0184, all -> 0x0198 }
        r6 = "Failed to load user property from local database";	 Catch:{ ParseException -> 0x0184, all -> 0x0198 }
        r1.zzbx(r6);	 Catch:{ ParseException -> 0x0184, all -> 0x0198 }
        r7.recycle();
        r1 = r3;	 Catch:{ SQLiteFullException -> 0x009f, SQLiteDatabaseLockedException -> 0x00d6, SQLiteException -> 0x00ee, all -> 0x013b }
        goto L_0x017d;	 Catch:{ SQLiteFullException -> 0x009f, SQLiteDatabaseLockedException -> 0x00d6, SQLiteException -> 0x00ee, all -> 0x013b }
    L_0x0198:
        r1 = move-exception;	 Catch:{ SQLiteFullException -> 0x009f, SQLiteDatabaseLockedException -> 0x00d6, SQLiteException -> 0x00ee, all -> 0x013b }
        r7.recycle();	 Catch:{ SQLiteFullException -> 0x009f, SQLiteDatabaseLockedException -> 0x00d6, SQLiteException -> 0x00ee, all -> 0x013b }
        throw r1;	 Catch:{ SQLiteFullException -> 0x009f, SQLiteDatabaseLockedException -> 0x00d6, SQLiteException -> 0x00ee, all -> 0x013b }
    L_0x019d:
        r1 = r13.zzgo();	 Catch:{ SQLiteFullException -> 0x009f, SQLiteDatabaseLockedException -> 0x00d6, SQLiteException -> 0x00ee, all -> 0x013b }
        r1 = r1.zzjd();	 Catch:{ SQLiteFullException -> 0x009f, SQLiteDatabaseLockedException -> 0x00d6, SQLiteException -> 0x00ee, all -> 0x013b }
        r3 = "Unknown record type in local database";	 Catch:{ SQLiteFullException -> 0x009f, SQLiteDatabaseLockedException -> 0x00d6, SQLiteException -> 0x00ee, all -> 0x013b }
        r1.zzbx(r3);	 Catch:{ SQLiteFullException -> 0x009f, SQLiteDatabaseLockedException -> 0x00d6, SQLiteException -> 0x00ee, all -> 0x013b }
        goto L_0x006a;	 Catch:{ SQLiteFullException -> 0x009f, SQLiteDatabaseLockedException -> 0x00d6, SQLiteException -> 0x00ee, all -> 0x013b }
    L_0x01ad:
        r1 = "messages";	 Catch:{ SQLiteFullException -> 0x009f, SQLiteDatabaseLockedException -> 0x00d6, SQLiteException -> 0x00ee, all -> 0x013b }
        r3 = "rowid <= ?";	 Catch:{ SQLiteFullException -> 0x009f, SQLiteDatabaseLockedException -> 0x00d6, SQLiteException -> 0x00ee, all -> 0x013b }
        r6 = 1;	 Catch:{ SQLiteFullException -> 0x009f, SQLiteDatabaseLockedException -> 0x00d6, SQLiteException -> 0x00ee, all -> 0x013b }
        r6 = new java.lang.String[r6];	 Catch:{ SQLiteFullException -> 0x009f, SQLiteDatabaseLockedException -> 0x00d6, SQLiteException -> 0x00ee, all -> 0x013b }
        r7 = 0;	 Catch:{ SQLiteFullException -> 0x009f, SQLiteDatabaseLockedException -> 0x00d6, SQLiteException -> 0x00ee, all -> 0x013b }
        r4 = java.lang.Long.toString(r4);	 Catch:{ SQLiteFullException -> 0x009f, SQLiteDatabaseLockedException -> 0x00d6, SQLiteException -> 0x00ee, all -> 0x013b }
        r6[r7] = r4;	 Catch:{ SQLiteFullException -> 0x009f, SQLiteDatabaseLockedException -> 0x00d6, SQLiteException -> 0x00ee, all -> 0x013b }
        r1 = r0.delete(r1, r3, r6);	 Catch:{ SQLiteFullException -> 0x009f, SQLiteDatabaseLockedException -> 0x00d6, SQLiteException -> 0x00ee, all -> 0x013b }
        r3 = r10.size();	 Catch:{ SQLiteFullException -> 0x009f, SQLiteDatabaseLockedException -> 0x00d6, SQLiteException -> 0x00ee, all -> 0x013b }
        if (r1 >= r3) goto L_0x01d5;	 Catch:{ SQLiteFullException -> 0x009f, SQLiteDatabaseLockedException -> 0x00d6, SQLiteException -> 0x00ee, all -> 0x013b }
    L_0x01c7:
        r1 = r13.zzgo();	 Catch:{ SQLiteFullException -> 0x009f, SQLiteDatabaseLockedException -> 0x00d6, SQLiteException -> 0x00ee, all -> 0x013b }
        r1 = r1.zzjd();	 Catch:{ SQLiteFullException -> 0x009f, SQLiteDatabaseLockedException -> 0x00d6, SQLiteException -> 0x00ee, all -> 0x013b }
        r3 = "Fewer entries removed from local database than expected";	 Catch:{ SQLiteFullException -> 0x009f, SQLiteDatabaseLockedException -> 0x00d6, SQLiteException -> 0x00ee, all -> 0x013b }
        r1.zzbx(r3);	 Catch:{ SQLiteFullException -> 0x009f, SQLiteDatabaseLockedException -> 0x00d6, SQLiteException -> 0x00ee, all -> 0x013b }
    L_0x01d5:
        r0.setTransactionSuccessful();	 Catch:{ SQLiteFullException -> 0x009f, SQLiteDatabaseLockedException -> 0x00d6, SQLiteException -> 0x00ee, all -> 0x013b }
        r0.endTransaction();	 Catch:{ SQLiteFullException -> 0x009f, SQLiteDatabaseLockedException -> 0x00d6, SQLiteException -> 0x00ee, all -> 0x013b }
        if (r2 == 0) goto L_0x01e0;
    L_0x01dd:
        r2.close();
    L_0x01e0:
        if (r0 == 0) goto L_0x01e5;
    L_0x01e2:
        r0.close();
    L_0x01e5:
        r0 = r10;
        goto L_0x000b;
    L_0x01e8:
        r0 = r13.zzgo();
        r0 = r0.zzjg();
        r1 = "Failed to read events from database in reasonable time";
        r0.zzbx(r1);
        r0 = 0;
        goto L_0x000b;
    L_0x01f9:
        r0 = move-exception;
        r1 = r0;
        r2 = r11;
        goto L_0x013d;
    L_0x01fe:
        r1 = move-exception;
        r2 = r11;
        r3 = r0;
        goto L_0x013d;
    L_0x0203:
        r0 = move-exception;
        r1 = r0;
        goto L_0x013d;
    L_0x0207:
        r0 = move-exception;
        r1 = r0;
        r2 = r11;
        goto L_0x00f0;
    L_0x020c:
        r1 = move-exception;
        r2 = r11;
        r3 = r0;
        goto L_0x00f0;
    L_0x0211:
        r0 = move-exception;
        r2 = r11;
        goto L_0x00d8;
    L_0x0215:
        r1 = move-exception;
        r2 = r11;
        r3 = r0;
        goto L_0x00d8;
    L_0x021a:
        r0 = move-exception;
        r1 = r0;
        r2 = r11;
        goto L_0x00a1;
    L_0x021f:
        r1 = move-exception;
        r2 = r11;
        r3 = r0;
        goto L_0x00a1;
    L_0x0224:
        r0 = r9;
        goto L_0x00bd;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.zzal.zzr(int):java.util.List<com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable>");
    }

    protected final boolean zzgt() {
        return false;
    }

    public final void resetAnalyticsData() {
        zzgb();
        zzaf();
        try {
            int delete = getWritableDatabase().delete("messages", null, null) + 0;
            if (delete > 0) {
                zzgo().zzjl().zzg("Reset local analytics data. records", Integer.valueOf(delete));
            }
        } catch (SQLiteException e) {
            zzgo().zzjd().zzg("Error resetting local analytics data. error", e);
        }
    }

    private final boolean zza(int i, byte[] bArr) {
        zzgb();
        zzaf();
        if (this.zzalr) {
            return false;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("type", Integer.valueOf(i));
        contentValues.put("entry", bArr);
        int i2 = 5;
        int i3 = 0;
        while (i3 < 5) {
            SQLiteDatabase sQLiteDatabase = null;
            Cursor cursor = null;
            try {
                sQLiteDatabase = getWritableDatabase();
                if (sQLiteDatabase == null) {
                    this.zzalr = true;
                    if (sQLiteDatabase != null) {
                        sQLiteDatabase.close();
                    }
                    return false;
                }
                sQLiteDatabase.beginTransaction();
                long j = 0;
                cursor = sQLiteDatabase.rawQuery("select count(1) from messages", null);
                if (cursor != null && cursor.moveToFirst()) {
                    j = cursor.getLong(0);
                }
                if (j >= 100000) {
                    zzgo().zzjd().zzbx("Data loss, local db full");
                    j = (100000 - j) + 1;
                    long delete = (long) sQLiteDatabase.delete("messages", "rowid in (select rowid from messages order by rowid asc limit ?)", new String[]{Long.toString(j)});
                    if (delete != j) {
                        zzgo().zzjd().zzd("Different delete count than expected in local db. expected, received, difference", Long.valueOf(j), Long.valueOf(delete), Long.valueOf(j - delete));
                    }
                }
                sQLiteDatabase.insertOrThrow("messages", null, contentValues);
                sQLiteDatabase.setTransactionSuccessful();
                sQLiteDatabase.endTransaction();
                if (cursor != null) {
                    cursor.close();
                }
                if (sQLiteDatabase != null) {
                    sQLiteDatabase.close();
                }
                return true;
            } catch (SQLiteFullException e) {
                zzgo().zzjd().zzg("Error writing entry to local database", e);
                this.zzalr = true;
                if (cursor != null) {
                    cursor.close();
                }
                if (sQLiteDatabase != null) {
                    sQLiteDatabase.close();
                }
                i3++;
            } catch (SQLiteDatabaseLockedException e2) {
                SystemClock.sleep((long) i2);
                i2 += 20;
                if (cursor != null) {
                    cursor.close();
                }
                if (sQLiteDatabase != null) {
                    sQLiteDatabase.close();
                }
                i3++;
            } catch (SQLiteException e3) {
                if (sQLiteDatabase != null) {
                    if (sQLiteDatabase.inTransaction()) {
                        sQLiteDatabase.endTransaction();
                    }
                }
                zzgo().zzjd().zzg("Error writing entry to local database", e3);
                this.zzalr = true;
                if (cursor != null) {
                    cursor.close();
                }
                if (sQLiteDatabase != null) {
                    sQLiteDatabase.close();
                }
                i3++;
            } catch (Throwable th) {
                if (cursor != null) {
                    cursor.close();
                }
                if (sQLiteDatabase != null) {
                    sQLiteDatabase.close();
                }
            }
        }
        zzgo().zzjg().zzbx("Failed to write entry to local database");
        return false;
    }

    public final boolean zza(zzad com_google_android_gms_measurement_internal_zzad) {
        Parcel obtain = Parcel.obtain();
        com_google_android_gms_measurement_internal_zzad.writeToParcel(obtain, 0);
        byte[] marshall = obtain.marshall();
        obtain.recycle();
        if (marshall.length <= 131072) {
            return zza(0, marshall);
        }
        zzgo().zzjg().zzbx("Event is too long for local database. Sending event directly to service");
        return false;
    }

    public final boolean zza(zzfh com_google_android_gms_measurement_internal_zzfh) {
        Parcel obtain = Parcel.obtain();
        com_google_android_gms_measurement_internal_zzfh.writeToParcel(obtain, 0);
        byte[] marshall = obtain.marshall();
        obtain.recycle();
        if (marshall.length <= 131072) {
            return zza(1, marshall);
        }
        zzgo().zzjg().zzbx("User property too long for local database. Sending directly to service");
        return false;
    }

    public final boolean zzc(zzl com_google_android_gms_measurement_internal_zzl) {
        zzgm();
        byte[] zza = zzfk.zza((Parcelable) com_google_android_gms_measurement_internal_zzl);
        if (zza.length <= 131072) {
            return zza(2, zza);
        }
        zzgo().zzjg().zzbx("Conditional user property too long for local database. Sending directly to service");
        return false;
    }

    private final SQLiteDatabase getWritableDatabase() throws SQLiteException {
        if (this.zzalr) {
            return null;
        }
        SQLiteDatabase writableDatabase = this.zzalq.getWritableDatabase();
        if (writableDatabase != null) {
            return writableDatabase;
        }
        this.zzalr = true;
        return null;
    }

    public final /* bridge */ /* synthetic */ void zzga() {
        super.zzga();
    }

    public final /* bridge */ /* synthetic */ void zzgb() {
        super.zzgb();
    }

    public final /* bridge */ /* synthetic */ void zzgc() {
        super.zzgc();
    }

    public final /* bridge */ /* synthetic */ void zzaf() {
        super.zzaf();
    }

    public final /* bridge */ /* synthetic */ zza zzgd() {
        return super.zzgd();
    }

    public final /* bridge */ /* synthetic */ zzcs zzge() {
        return super.zzge();
    }

    public final /* bridge */ /* synthetic */ zzaj zzgf() {
        return super.zzgf();
    }

    public final /* bridge */ /* synthetic */ zzdr zzgg() {
        return super.zzgg();
    }

    public final /* bridge */ /* synthetic */ zzdo zzgh() {
        return super.zzgh();
    }

    public final /* bridge */ /* synthetic */ zzal zzgi() {
        return super.zzgi();
    }

    public final /* bridge */ /* synthetic */ zzeq zzgj() {
        return super.zzgj();
    }

    public final /* bridge */ /* synthetic */ zzx zzgk() {
        return super.zzgk();
    }

    public final /* bridge */ /* synthetic */ Clock zzbx() {
        return super.zzbx();
    }

    public final /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    public final /* bridge */ /* synthetic */ zzan zzgl() {
        return super.zzgl();
    }

    public final /* bridge */ /* synthetic */ zzfk zzgm() {
        return super.zzgm();
    }

    public final /* bridge */ /* synthetic */ zzbo zzgn() {
        return super.zzgn();
    }

    public final /* bridge */ /* synthetic */ zzap zzgo() {
        return super.zzgo();
    }

    public final /* bridge */ /* synthetic */ zzba zzgp() {
        return super.zzgp();
    }

    public final /* bridge */ /* synthetic */ zzn zzgq() {
        return super.zzgq();
    }

    public final /* bridge */ /* synthetic */ zzk zzgr() {
        return super.zzgr();
    }
}
