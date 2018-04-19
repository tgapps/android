package com.google.android.gms.internal.measurement;

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

public final class zzfc extends zzhk {
    private final zzfd zzaig = new zzfd(this, getContext(), "google_app_measurement_local.db");
    private boolean zzaih;

    zzfc(zzgl com_google_android_gms_internal_measurement_zzgl) {
        super(com_google_android_gms_internal_measurement_zzgl);
    }

    private final SQLiteDatabase getWritableDatabase() throws SQLiteException {
        if (this.zzaih) {
            return null;
        }
        SQLiteDatabase writableDatabase = this.zzaig.getWritableDatabase();
        if (writableDatabase != null) {
            return writableDatabase;
        }
        this.zzaih = true;
        return null;
    }

    private final boolean zza(int i, byte[] bArr) {
        zzab();
        if (this.zzaih) {
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
                    this.zzaih = true;
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
                    zzgg().zzil().log("Data loss, local db full");
                    j = (100000 - j) + 1;
                    long delete = (long) sQLiteDatabase.delete("messages", "rowid in (select rowid from messages order by rowid asc limit ?)", new String[]{Long.toString(j)});
                    if (delete != j) {
                        zzgg().zzil().zzd("Different delete count than expected in local db. expected, received, difference", Long.valueOf(j), Long.valueOf(delete), Long.valueOf(j - delete));
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
                zzgg().zzil().zzg("Error writing entry to local database", e);
                this.zzaih = true;
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
                zzgg().zzil().zzg("Error writing entry to local database", e3);
                this.zzaih = true;
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
        zzgg().zzin().log("Failed to write entry to local database");
        return false;
    }

    public final /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    public final void resetAnalyticsData() {
        zzab();
        try {
            int delete = getWritableDatabase().delete("messages", null, null) + 0;
            if (delete > 0) {
                zzgg().zzir().zzg("Reset local analytics data. records", Integer.valueOf(delete));
            }
        } catch (SQLiteException e) {
            zzgg().zzil().zzg("Error resetting local analytics data. error", e);
        }
    }

    public final boolean zza(zzeu com_google_android_gms_internal_measurement_zzeu) {
        Parcel obtain = Parcel.obtain();
        com_google_android_gms_internal_measurement_zzeu.writeToParcel(obtain, 0);
        byte[] marshall = obtain.marshall();
        obtain.recycle();
        if (marshall.length <= 131072) {
            return zza(0, marshall);
        }
        zzgg().zzin().log("Event is too long for local database. Sending event directly to service");
        return false;
    }

    public final boolean zza(zzjs com_google_android_gms_internal_measurement_zzjs) {
        Parcel obtain = Parcel.obtain();
        com_google_android_gms_internal_measurement_zzjs.writeToParcel(obtain, 0);
        byte[] marshall = obtain.marshall();
        obtain.recycle();
        if (marshall.length <= 131072) {
            return zza(1, marshall);
        }
        zzgg().zzin().log("User property too long for local database. Sending directly to service");
        return false;
    }

    public final /* bridge */ /* synthetic */ void zzab() {
        super.zzab();
    }

    public final /* bridge */ /* synthetic */ Clock zzbt() {
        return super.zzbt();
    }

    public final boolean zzc(zzef com_google_android_gms_internal_measurement_zzef) {
        zzgc();
        byte[] zza = zzjv.zza((Parcelable) com_google_android_gms_internal_measurement_zzef);
        if (zza.length <= 131072) {
            return zza(2, zza);
        }
        zzgg().zzin().log("Conditional user property too long for local database. Sending directly to service");
        return false;
    }

    public final /* bridge */ /* synthetic */ void zzfq() {
        super.zzfq();
    }

    public final /* bridge */ /* synthetic */ void zzfr() {
        super.zzfr();
    }

    public final /* bridge */ /* synthetic */ zzdx zzfs() {
        return super.zzfs();
    }

    public final /* bridge */ /* synthetic */ zzee zzft() {
        return super.zzft();
    }

    public final /* bridge */ /* synthetic */ zzhm zzfu() {
        return super.zzfu();
    }

    public final /* bridge */ /* synthetic */ zzfb zzfv() {
        return super.zzfv();
    }

    public final /* bridge */ /* synthetic */ zzeo zzfw() {
        return super.zzfw();
    }

    public final /* bridge */ /* synthetic */ zzil zzfx() {
        return super.zzfx();
    }

    public final /* bridge */ /* synthetic */ zzih zzfy() {
        return super.zzfy();
    }

    public final /* bridge */ /* synthetic */ zzfc zzfz() {
        return super.zzfz();
    }

    public final /* bridge */ /* synthetic */ zzei zzga() {
        return super.zzga();
    }

    public final /* bridge */ /* synthetic */ zzfe zzgb() {
        return super.zzgb();
    }

    public final /* bridge */ /* synthetic */ zzjv zzgc() {
        return super.zzgc();
    }

    public final /* bridge */ /* synthetic */ zzgf zzgd() {
        return super.zzgd();
    }

    public final /* bridge */ /* synthetic */ zzjk zzge() {
        return super.zzge();
    }

    public final /* bridge */ /* synthetic */ zzgg zzgf() {
        return super.zzgf();
    }

    public final /* bridge */ /* synthetic */ zzfg zzgg() {
        return super.zzgg();
    }

    public final /* bridge */ /* synthetic */ zzfr zzgh() {
        return super.zzgh();
    }

    public final /* bridge */ /* synthetic */ zzeh zzgi() {
        return super.zzgi();
    }

    protected final boolean zzhh() {
        return false;
    }

    public final java.util.List<com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable> zzp(int r14) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Unreachable block: B:150:0x0138
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
        r13.zzab();
        r0 = r13.zzaih;
        if (r0 == 0) goto L_0x0009;
    L_0x0007:
        r0 = 0;
    L_0x0008:
        return r0;
    L_0x0009:
        r10 = new java.util.ArrayList;
        r10.<init>();
        r0 = r13.getContext();
        r1 = "google_app_measurement_local.db";
        r0 = r0.getDatabasePath(r1);
        r0 = r0.exists();
        if (r0 != 0) goto L_0x0021;
    L_0x001f:
        r0 = r10;
        goto L_0x0008;
    L_0x0021:
        r9 = 5;
        r0 = 0;
        r12 = r0;
    L_0x0024:
        r0 = 5;
        if (r12 >= r0) goto L_0x01e5;
    L_0x0027:
        r3 = 0;
        r11 = 0;
        r0 = r13.getWritableDatabase();	 Catch:{ SQLiteFullException -> 0x0217, SQLiteDatabaseLockedException -> 0x020e, SQLiteException -> 0x0204, all -> 0x01f6 }
        if (r0 != 0) goto L_0x0039;
    L_0x002f:
        r1 = 1;
        r13.zzaih = r1;	 Catch:{ SQLiteFullException -> 0x021c, SQLiteDatabaseLockedException -> 0x0212, SQLiteException -> 0x0209, all -> 0x01fb }
        if (r0 == 0) goto L_0x0037;
    L_0x0034:
        r0.close();
    L_0x0037:
        r0 = 0;
        goto L_0x0008;
    L_0x0039:
        r0.beginTransaction();	 Catch:{ SQLiteFullException -> 0x021c, SQLiteDatabaseLockedException -> 0x0212, SQLiteException -> 0x0209, all -> 0x01fb }
        r1 = "messages";	 Catch:{ SQLiteFullException -> 0x021c, SQLiteDatabaseLockedException -> 0x0212, SQLiteException -> 0x0209, all -> 0x01fb }
        r2 = 3;	 Catch:{ SQLiteFullException -> 0x021c, SQLiteDatabaseLockedException -> 0x0212, SQLiteException -> 0x0209, all -> 0x01fb }
        r2 = new java.lang.String[r2];	 Catch:{ SQLiteFullException -> 0x021c, SQLiteDatabaseLockedException -> 0x0212, SQLiteException -> 0x0209, all -> 0x01fb }
        r3 = 0;	 Catch:{ SQLiteFullException -> 0x021c, SQLiteDatabaseLockedException -> 0x0212, SQLiteException -> 0x0209, all -> 0x01fb }
        r4 = "rowid";	 Catch:{ SQLiteFullException -> 0x021c, SQLiteDatabaseLockedException -> 0x0212, SQLiteException -> 0x0209, all -> 0x01fb }
        r2[r3] = r4;	 Catch:{ SQLiteFullException -> 0x021c, SQLiteDatabaseLockedException -> 0x0212, SQLiteException -> 0x0209, all -> 0x01fb }
        r3 = 1;	 Catch:{ SQLiteFullException -> 0x021c, SQLiteDatabaseLockedException -> 0x0212, SQLiteException -> 0x0209, all -> 0x01fb }
        r4 = "type";	 Catch:{ SQLiteFullException -> 0x021c, SQLiteDatabaseLockedException -> 0x0212, SQLiteException -> 0x0209, all -> 0x01fb }
        r2[r3] = r4;	 Catch:{ SQLiteFullException -> 0x021c, SQLiteDatabaseLockedException -> 0x0212, SQLiteException -> 0x0209, all -> 0x01fb }
        r3 = 2;	 Catch:{ SQLiteFullException -> 0x021c, SQLiteDatabaseLockedException -> 0x0212, SQLiteException -> 0x0209, all -> 0x01fb }
        r4 = "entry";	 Catch:{ SQLiteFullException -> 0x021c, SQLiteDatabaseLockedException -> 0x0212, SQLiteException -> 0x0209, all -> 0x01fb }
        r2[r3] = r4;	 Catch:{ SQLiteFullException -> 0x021c, SQLiteDatabaseLockedException -> 0x0212, SQLiteException -> 0x0209, all -> 0x01fb }
        r3 = 0;	 Catch:{ SQLiteFullException -> 0x021c, SQLiteDatabaseLockedException -> 0x0212, SQLiteException -> 0x0209, all -> 0x01fb }
        r4 = 0;	 Catch:{ SQLiteFullException -> 0x021c, SQLiteDatabaseLockedException -> 0x0212, SQLiteException -> 0x0209, all -> 0x01fb }
        r5 = 0;	 Catch:{ SQLiteFullException -> 0x021c, SQLiteDatabaseLockedException -> 0x0212, SQLiteException -> 0x0209, all -> 0x01fb }
        r6 = 0;	 Catch:{ SQLiteFullException -> 0x021c, SQLiteDatabaseLockedException -> 0x0212, SQLiteException -> 0x0209, all -> 0x01fb }
        r7 = "rowid asc";	 Catch:{ SQLiteFullException -> 0x021c, SQLiteDatabaseLockedException -> 0x0212, SQLiteException -> 0x0209, all -> 0x01fb }
        r8 = 100;	 Catch:{ SQLiteFullException -> 0x021c, SQLiteDatabaseLockedException -> 0x0212, SQLiteException -> 0x0209, all -> 0x01fb }
        r8 = java.lang.Integer.toString(r8);	 Catch:{ SQLiteFullException -> 0x021c, SQLiteDatabaseLockedException -> 0x0212, SQLiteException -> 0x0209, all -> 0x01fb }
        r2 = r0.query(r1, r2, r3, r4, r5, r6, r7, r8);	 Catch:{ SQLiteFullException -> 0x021c, SQLiteDatabaseLockedException -> 0x0212, SQLiteException -> 0x0209, all -> 0x01fb }
        r4 = -1;
    L_0x0067:
        r1 = r2.moveToNext();	 Catch:{ SQLiteFullException -> 0x009c, SQLiteDatabaseLockedException -> 0x00d3, SQLiteException -> 0x00eb, all -> 0x0138 }
        if (r1 == 0) goto L_0x01aa;	 Catch:{ SQLiteFullException -> 0x009c, SQLiteDatabaseLockedException -> 0x00d3, SQLiteException -> 0x00eb, all -> 0x0138 }
    L_0x006d:
        r1 = 0;	 Catch:{ SQLiteFullException -> 0x009c, SQLiteDatabaseLockedException -> 0x00d3, SQLiteException -> 0x00eb, all -> 0x0138 }
        r4 = r2.getLong(r1);	 Catch:{ SQLiteFullException -> 0x009c, SQLiteDatabaseLockedException -> 0x00d3, SQLiteException -> 0x00eb, all -> 0x0138 }
        r1 = 1;	 Catch:{ SQLiteFullException -> 0x009c, SQLiteDatabaseLockedException -> 0x00d3, SQLiteException -> 0x00eb, all -> 0x0138 }
        r1 = r2.getInt(r1);	 Catch:{ SQLiteFullException -> 0x009c, SQLiteDatabaseLockedException -> 0x00d3, SQLiteException -> 0x00eb, all -> 0x0138 }
        r3 = 2;	 Catch:{ SQLiteFullException -> 0x009c, SQLiteDatabaseLockedException -> 0x00d3, SQLiteException -> 0x00eb, all -> 0x0138 }
        r6 = r2.getBlob(r3);	 Catch:{ SQLiteFullException -> 0x009c, SQLiteDatabaseLockedException -> 0x00d3, SQLiteException -> 0x00eb, all -> 0x0138 }
        if (r1 != 0) goto L_0x0115;	 Catch:{ SQLiteFullException -> 0x009c, SQLiteDatabaseLockedException -> 0x00d3, SQLiteException -> 0x00eb, all -> 0x0138 }
    L_0x007e:
        r3 = android.os.Parcel.obtain();	 Catch:{ SQLiteFullException -> 0x009c, SQLiteDatabaseLockedException -> 0x00d3, SQLiteException -> 0x00eb, all -> 0x0138 }
        r1 = 0;
        r7 = r6.length;	 Catch:{ ParseException -> 0x00c0, all -> 0x00e6 }
        r3.unmarshall(r6, r1, r7);	 Catch:{ ParseException -> 0x00c0, all -> 0x00e6 }
        r1 = 0;	 Catch:{ ParseException -> 0x00c0, all -> 0x00e6 }
        r3.setDataPosition(r1);	 Catch:{ ParseException -> 0x00c0, all -> 0x00e6 }
        r1 = com.google.android.gms.internal.measurement.zzeu.CREATOR;	 Catch:{ ParseException -> 0x00c0, all -> 0x00e6 }
        r1 = r1.createFromParcel(r3);	 Catch:{ ParseException -> 0x00c0, all -> 0x00e6 }
        r1 = (com.google.android.gms.internal.measurement.zzeu) r1;	 Catch:{ ParseException -> 0x00c0, all -> 0x00e6 }
        r3.recycle();
        if (r1 == 0) goto L_0x0067;	 Catch:{ SQLiteFullException -> 0x009c, SQLiteDatabaseLockedException -> 0x00d3, SQLiteException -> 0x00eb, all -> 0x0138 }
    L_0x0098:
        r10.add(r1);	 Catch:{ SQLiteFullException -> 0x009c, SQLiteDatabaseLockedException -> 0x00d3, SQLiteException -> 0x00eb, all -> 0x0138 }
        goto L_0x0067;
    L_0x009c:
        r1 = move-exception;
        r3 = r0;
    L_0x009e:
        r0 = r13.zzgg();	 Catch:{ all -> 0x0200 }
        r0 = r0.zzil();	 Catch:{ all -> 0x0200 }
        r4 = "Error reading entries from local database";	 Catch:{ all -> 0x0200 }
        r0.zzg(r4, r1);	 Catch:{ all -> 0x0200 }
        r0 = 1;	 Catch:{ all -> 0x0200 }
        r13.zzaih = r0;	 Catch:{ all -> 0x0200 }
        if (r2 == 0) goto L_0x00b4;
    L_0x00b1:
        r2.close();
    L_0x00b4:
        if (r3 == 0) goto L_0x0221;
    L_0x00b6:
        r3.close();
        r0 = r9;
    L_0x00ba:
        r1 = r12 + 1;
        r12 = r1;
        r9 = r0;
        goto L_0x0024;
    L_0x00c0:
        r1 = move-exception;
        r1 = r13.zzgg();	 Catch:{ ParseException -> 0x00c0, all -> 0x00e6 }
        r1 = r1.zzil();	 Catch:{ ParseException -> 0x00c0, all -> 0x00e6 }
        r6 = "Failed to load event from local database";	 Catch:{ ParseException -> 0x00c0, all -> 0x00e6 }
        r1.log(r6);	 Catch:{ ParseException -> 0x00c0, all -> 0x00e6 }
        r3.recycle();
        goto L_0x0067;
    L_0x00d3:
        r1 = move-exception;
        r3 = r0;
    L_0x00d5:
        r0 = (long) r9;
        android.os.SystemClock.sleep(r0);	 Catch:{ all -> 0x0200 }
        r0 = r9 + 20;
        if (r2 == 0) goto L_0x00e0;
    L_0x00dd:
        r2.close();
    L_0x00e0:
        if (r3 == 0) goto L_0x00ba;
    L_0x00e2:
        r3.close();
        goto L_0x00ba;
    L_0x00e6:
        r1 = move-exception;
        r3.recycle();	 Catch:{ SQLiteFullException -> 0x009c, SQLiteDatabaseLockedException -> 0x00d3, SQLiteException -> 0x00eb, all -> 0x0138 }
        throw r1;	 Catch:{ SQLiteFullException -> 0x009c, SQLiteDatabaseLockedException -> 0x00d3, SQLiteException -> 0x00eb, all -> 0x0138 }
    L_0x00eb:
        r1 = move-exception;
        r3 = r0;
    L_0x00ed:
        if (r3 == 0) goto L_0x00f8;
    L_0x00ef:
        r0 = r3.inTransaction();	 Catch:{ all -> 0x0200 }
        if (r0 == 0) goto L_0x00f8;	 Catch:{ all -> 0x0200 }
    L_0x00f5:
        r3.endTransaction();	 Catch:{ all -> 0x0200 }
    L_0x00f8:
        r0 = r13.zzgg();	 Catch:{ all -> 0x0200 }
        r0 = r0.zzil();	 Catch:{ all -> 0x0200 }
        r4 = "Error reading entries from local database";	 Catch:{ all -> 0x0200 }
        r0.zzg(r4, r1);	 Catch:{ all -> 0x0200 }
        r0 = 1;	 Catch:{ all -> 0x0200 }
        r13.zzaih = r0;	 Catch:{ all -> 0x0200 }
        if (r2 == 0) goto L_0x010e;
    L_0x010b:
        r2.close();
    L_0x010e:
        if (r3 == 0) goto L_0x0221;
    L_0x0110:
        r3.close();
        r0 = r9;
        goto L_0x00ba;
    L_0x0115:
        r3 = 1;
        if (r1 != r3) goto L_0x015e;
    L_0x0118:
        r7 = android.os.Parcel.obtain();	 Catch:{ SQLiteFullException -> 0x009c, SQLiteDatabaseLockedException -> 0x00d3, SQLiteException -> 0x00eb, all -> 0x0138 }
        r3 = 0;
        r1 = 0;
        r8 = r6.length;	 Catch:{ ParseException -> 0x0145, all -> 0x0159 }
        r7.unmarshall(r6, r1, r8);	 Catch:{ ParseException -> 0x0145, all -> 0x0159 }
        r1 = 0;	 Catch:{ ParseException -> 0x0145, all -> 0x0159 }
        r7.setDataPosition(r1);	 Catch:{ ParseException -> 0x0145, all -> 0x0159 }
        r1 = com.google.android.gms.internal.measurement.zzjs.CREATOR;	 Catch:{ ParseException -> 0x0145, all -> 0x0159 }
        r1 = r1.createFromParcel(r7);	 Catch:{ ParseException -> 0x0145, all -> 0x0159 }
        r1 = (com.google.android.gms.internal.measurement.zzjs) r1;	 Catch:{ ParseException -> 0x0145, all -> 0x0159 }
        r7.recycle();
    L_0x0131:
        if (r1 == 0) goto L_0x0067;	 Catch:{ SQLiteFullException -> 0x009c, SQLiteDatabaseLockedException -> 0x00d3, SQLiteException -> 0x00eb, all -> 0x0138 }
    L_0x0133:
        r10.add(r1);	 Catch:{ SQLiteFullException -> 0x009c, SQLiteDatabaseLockedException -> 0x00d3, SQLiteException -> 0x00eb, all -> 0x0138 }
        goto L_0x0067;
    L_0x0138:
        r1 = move-exception;
        r3 = r0;
    L_0x013a:
        if (r2 == 0) goto L_0x013f;
    L_0x013c:
        r2.close();
    L_0x013f:
        if (r3 == 0) goto L_0x0144;
    L_0x0141:
        r3.close();
    L_0x0144:
        throw r1;
    L_0x0145:
        r1 = move-exception;
        r1 = r13.zzgg();	 Catch:{ ParseException -> 0x0145, all -> 0x0159 }
        r1 = r1.zzil();	 Catch:{ ParseException -> 0x0145, all -> 0x0159 }
        r6 = "Failed to load user property from local database";	 Catch:{ ParseException -> 0x0145, all -> 0x0159 }
        r1.log(r6);	 Catch:{ ParseException -> 0x0145, all -> 0x0159 }
        r7.recycle();
        r1 = r3;	 Catch:{ SQLiteFullException -> 0x009c, SQLiteDatabaseLockedException -> 0x00d3, SQLiteException -> 0x00eb, all -> 0x0138 }
        goto L_0x0131;	 Catch:{ SQLiteFullException -> 0x009c, SQLiteDatabaseLockedException -> 0x00d3, SQLiteException -> 0x00eb, all -> 0x0138 }
    L_0x0159:
        r1 = move-exception;	 Catch:{ SQLiteFullException -> 0x009c, SQLiteDatabaseLockedException -> 0x00d3, SQLiteException -> 0x00eb, all -> 0x0138 }
        r7.recycle();	 Catch:{ SQLiteFullException -> 0x009c, SQLiteDatabaseLockedException -> 0x00d3, SQLiteException -> 0x00eb, all -> 0x0138 }
        throw r1;	 Catch:{ SQLiteFullException -> 0x009c, SQLiteDatabaseLockedException -> 0x00d3, SQLiteException -> 0x00eb, all -> 0x0138 }
    L_0x015e:
        r3 = 2;	 Catch:{ SQLiteFullException -> 0x009c, SQLiteDatabaseLockedException -> 0x00d3, SQLiteException -> 0x00eb, all -> 0x0138 }
        if (r1 != r3) goto L_0x019a;	 Catch:{ SQLiteFullException -> 0x009c, SQLiteDatabaseLockedException -> 0x00d3, SQLiteException -> 0x00eb, all -> 0x0138 }
    L_0x0161:
        r7 = android.os.Parcel.obtain();	 Catch:{ SQLiteFullException -> 0x009c, SQLiteDatabaseLockedException -> 0x00d3, SQLiteException -> 0x00eb, all -> 0x0138 }
        r3 = 0;
        r1 = 0;
        r8 = r6.length;	 Catch:{ ParseException -> 0x0181, all -> 0x0195 }
        r7.unmarshall(r6, r1, r8);	 Catch:{ ParseException -> 0x0181, all -> 0x0195 }
        r1 = 0;	 Catch:{ ParseException -> 0x0181, all -> 0x0195 }
        r7.setDataPosition(r1);	 Catch:{ ParseException -> 0x0181, all -> 0x0195 }
        r1 = com.google.android.gms.internal.measurement.zzef.CREATOR;	 Catch:{ ParseException -> 0x0181, all -> 0x0195 }
        r1 = r1.createFromParcel(r7);	 Catch:{ ParseException -> 0x0181, all -> 0x0195 }
        r1 = (com.google.android.gms.internal.measurement.zzef) r1;	 Catch:{ ParseException -> 0x0181, all -> 0x0195 }
        r7.recycle();
    L_0x017a:
        if (r1 == 0) goto L_0x0067;	 Catch:{ SQLiteFullException -> 0x009c, SQLiteDatabaseLockedException -> 0x00d3, SQLiteException -> 0x00eb, all -> 0x0138 }
    L_0x017c:
        r10.add(r1);	 Catch:{ SQLiteFullException -> 0x009c, SQLiteDatabaseLockedException -> 0x00d3, SQLiteException -> 0x00eb, all -> 0x0138 }
        goto L_0x0067;
    L_0x0181:
        r1 = move-exception;
        r1 = r13.zzgg();	 Catch:{ ParseException -> 0x0181, all -> 0x0195 }
        r1 = r1.zzil();	 Catch:{ ParseException -> 0x0181, all -> 0x0195 }
        r6 = "Failed to load user property from local database";	 Catch:{ ParseException -> 0x0181, all -> 0x0195 }
        r1.log(r6);	 Catch:{ ParseException -> 0x0181, all -> 0x0195 }
        r7.recycle();
        r1 = r3;	 Catch:{ SQLiteFullException -> 0x009c, SQLiteDatabaseLockedException -> 0x00d3, SQLiteException -> 0x00eb, all -> 0x0138 }
        goto L_0x017a;	 Catch:{ SQLiteFullException -> 0x009c, SQLiteDatabaseLockedException -> 0x00d3, SQLiteException -> 0x00eb, all -> 0x0138 }
    L_0x0195:
        r1 = move-exception;	 Catch:{ SQLiteFullException -> 0x009c, SQLiteDatabaseLockedException -> 0x00d3, SQLiteException -> 0x00eb, all -> 0x0138 }
        r7.recycle();	 Catch:{ SQLiteFullException -> 0x009c, SQLiteDatabaseLockedException -> 0x00d3, SQLiteException -> 0x00eb, all -> 0x0138 }
        throw r1;	 Catch:{ SQLiteFullException -> 0x009c, SQLiteDatabaseLockedException -> 0x00d3, SQLiteException -> 0x00eb, all -> 0x0138 }
    L_0x019a:
        r1 = r13.zzgg();	 Catch:{ SQLiteFullException -> 0x009c, SQLiteDatabaseLockedException -> 0x00d3, SQLiteException -> 0x00eb, all -> 0x0138 }
        r1 = r1.zzil();	 Catch:{ SQLiteFullException -> 0x009c, SQLiteDatabaseLockedException -> 0x00d3, SQLiteException -> 0x00eb, all -> 0x0138 }
        r3 = "Unknown record type in local database";	 Catch:{ SQLiteFullException -> 0x009c, SQLiteDatabaseLockedException -> 0x00d3, SQLiteException -> 0x00eb, all -> 0x0138 }
        r1.log(r3);	 Catch:{ SQLiteFullException -> 0x009c, SQLiteDatabaseLockedException -> 0x00d3, SQLiteException -> 0x00eb, all -> 0x0138 }
        goto L_0x0067;	 Catch:{ SQLiteFullException -> 0x009c, SQLiteDatabaseLockedException -> 0x00d3, SQLiteException -> 0x00eb, all -> 0x0138 }
    L_0x01aa:
        r1 = "messages";	 Catch:{ SQLiteFullException -> 0x009c, SQLiteDatabaseLockedException -> 0x00d3, SQLiteException -> 0x00eb, all -> 0x0138 }
        r3 = "rowid <= ?";	 Catch:{ SQLiteFullException -> 0x009c, SQLiteDatabaseLockedException -> 0x00d3, SQLiteException -> 0x00eb, all -> 0x0138 }
        r6 = 1;	 Catch:{ SQLiteFullException -> 0x009c, SQLiteDatabaseLockedException -> 0x00d3, SQLiteException -> 0x00eb, all -> 0x0138 }
        r6 = new java.lang.String[r6];	 Catch:{ SQLiteFullException -> 0x009c, SQLiteDatabaseLockedException -> 0x00d3, SQLiteException -> 0x00eb, all -> 0x0138 }
        r7 = 0;	 Catch:{ SQLiteFullException -> 0x009c, SQLiteDatabaseLockedException -> 0x00d3, SQLiteException -> 0x00eb, all -> 0x0138 }
        r4 = java.lang.Long.toString(r4);	 Catch:{ SQLiteFullException -> 0x009c, SQLiteDatabaseLockedException -> 0x00d3, SQLiteException -> 0x00eb, all -> 0x0138 }
        r6[r7] = r4;	 Catch:{ SQLiteFullException -> 0x009c, SQLiteDatabaseLockedException -> 0x00d3, SQLiteException -> 0x00eb, all -> 0x0138 }
        r1 = r0.delete(r1, r3, r6);	 Catch:{ SQLiteFullException -> 0x009c, SQLiteDatabaseLockedException -> 0x00d3, SQLiteException -> 0x00eb, all -> 0x0138 }
        r3 = r10.size();	 Catch:{ SQLiteFullException -> 0x009c, SQLiteDatabaseLockedException -> 0x00d3, SQLiteException -> 0x00eb, all -> 0x0138 }
        if (r1 >= r3) goto L_0x01d2;	 Catch:{ SQLiteFullException -> 0x009c, SQLiteDatabaseLockedException -> 0x00d3, SQLiteException -> 0x00eb, all -> 0x0138 }
    L_0x01c4:
        r1 = r13.zzgg();	 Catch:{ SQLiteFullException -> 0x009c, SQLiteDatabaseLockedException -> 0x00d3, SQLiteException -> 0x00eb, all -> 0x0138 }
        r1 = r1.zzil();	 Catch:{ SQLiteFullException -> 0x009c, SQLiteDatabaseLockedException -> 0x00d3, SQLiteException -> 0x00eb, all -> 0x0138 }
        r3 = "Fewer entries removed from local database than expected";	 Catch:{ SQLiteFullException -> 0x009c, SQLiteDatabaseLockedException -> 0x00d3, SQLiteException -> 0x00eb, all -> 0x0138 }
        r1.log(r3);	 Catch:{ SQLiteFullException -> 0x009c, SQLiteDatabaseLockedException -> 0x00d3, SQLiteException -> 0x00eb, all -> 0x0138 }
    L_0x01d2:
        r0.setTransactionSuccessful();	 Catch:{ SQLiteFullException -> 0x009c, SQLiteDatabaseLockedException -> 0x00d3, SQLiteException -> 0x00eb, all -> 0x0138 }
        r0.endTransaction();	 Catch:{ SQLiteFullException -> 0x009c, SQLiteDatabaseLockedException -> 0x00d3, SQLiteException -> 0x00eb, all -> 0x0138 }
        if (r2 == 0) goto L_0x01dd;
    L_0x01da:
        r2.close();
    L_0x01dd:
        if (r0 == 0) goto L_0x01e2;
    L_0x01df:
        r0.close();
    L_0x01e2:
        r0 = r10;
        goto L_0x0008;
    L_0x01e5:
        r0 = r13.zzgg();
        r0 = r0.zzin();
        r1 = "Failed to read events from database in reasonable time";
        r0.log(r1);
        r0 = 0;
        goto L_0x0008;
    L_0x01f6:
        r0 = move-exception;
        r1 = r0;
        r2 = r11;
        goto L_0x013a;
    L_0x01fb:
        r1 = move-exception;
        r2 = r11;
        r3 = r0;
        goto L_0x013a;
    L_0x0200:
        r0 = move-exception;
        r1 = r0;
        goto L_0x013a;
    L_0x0204:
        r0 = move-exception;
        r1 = r0;
        r2 = r11;
        goto L_0x00ed;
    L_0x0209:
        r1 = move-exception;
        r2 = r11;
        r3 = r0;
        goto L_0x00ed;
    L_0x020e:
        r0 = move-exception;
        r2 = r11;
        goto L_0x00d5;
    L_0x0212:
        r1 = move-exception;
        r2 = r11;
        r3 = r0;
        goto L_0x00d5;
    L_0x0217:
        r0 = move-exception;
        r1 = r0;
        r2 = r11;
        goto L_0x009e;
    L_0x021c:
        r1 = move-exception;
        r2 = r11;
        r3 = r0;
        goto L_0x009e;
    L_0x0221:
        r0 = r9;
        goto L_0x00ba;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzfc.zzp(int):java.util.List<com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable>");
    }
}
