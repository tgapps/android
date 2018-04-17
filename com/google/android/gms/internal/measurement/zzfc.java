package com.google.android.gms.internal.measurement;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Parcel;
import android.os.Parcelable;
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

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final boolean zza(int r20, byte[] r21) {
        /*
        r19 = this;
        r1 = r19;
        r19.zzab();
        r2 = r1.zzaih;
        r3 = 0;
        if (r2 == 0) goto L_0x000b;
    L_0x000a:
        return r3;
    L_0x000b:
        r2 = new android.content.ContentValues;
        r2.<init>();
        r4 = "type";
        r5 = java.lang.Integer.valueOf(r20);
        r2.put(r4, r5);
        r4 = "entry";
        r5 = r21;
        r2.put(r4, r5);
        r4 = 5;
        r5 = r3;
        r6 = r4;
    L_0x0023:
        if (r5 >= r4) goto L_0x015e;
    L_0x0025:
        r7 = 0;
        r8 = 1;
        r9 = r19.getWritableDatabase();	 Catch:{ SQLiteFullException -> 0x012e, SQLiteDatabaseLockedException -> 0x011a, SQLiteException -> 0x00eb, all -> 0x00e4 }
        if (r9 != 0) goto L_0x0043;
    L_0x002d:
        r1.zzaih = r8;	 Catch:{ SQLiteFullException -> 0x003f, SQLiteDatabaseLockedException -> 0x003b, SQLiteException -> 0x0035 }
        if (r9 == 0) goto L_0x0034;
    L_0x0031:
        r9.close();
    L_0x0034:
        return r3;
    L_0x0035:
        r0 = move-exception;
        r3 = r0;
        r12 = r7;
    L_0x0038:
        r7 = r9;
        goto L_0x00ef;
    L_0x003b:
        r0 = move-exception;
        r4 = r7;
        goto L_0x00de;
    L_0x003f:
        r0 = move-exception;
    L_0x0040:
        r3 = r0;
        goto L_0x0132;
    L_0x0043:
        r9.beginTransaction();	 Catch:{ SQLiteFullException -> 0x00e0, SQLiteDatabaseLockedException -> 0x003b, SQLiteException -> 0x00d8, all -> 0x00d2 }
        r10 = 0;
        r12 = "select count(1) from messages";
        r12 = r9.rawQuery(r12, r7);	 Catch:{ SQLiteFullException -> 0x00e0, SQLiteDatabaseLockedException -> 0x003b, SQLiteException -> 0x00d8, all -> 0x00d2 }
        if (r12 == 0) goto L_0x0069;
    L_0x0050:
        r13 = r12.moveToFirst();	 Catch:{ SQLiteFullException -> 0x0064, SQLiteDatabaseLockedException -> 0x0062, SQLiteException -> 0x005f, all -> 0x005b }
        if (r13 == 0) goto L_0x0069;
    L_0x0056:
        r10 = r12.getLong(r3);	 Catch:{ SQLiteFullException -> 0x0064, SQLiteDatabaseLockedException -> 0x0062, SQLiteException -> 0x005f, all -> 0x005b }
        goto L_0x0069;
    L_0x005b:
        r0 = move-exception;
        r2 = r0;
        goto L_0x0153;
    L_0x005f:
        r0 = move-exception;
        r3 = r0;
        goto L_0x0038;
    L_0x0062:
        r0 = move-exception;
        goto L_0x00cf;
    L_0x0064:
        r0 = move-exception;
        r3 = r0;
        r7 = r12;
        goto L_0x0132;
    L_0x0069:
        r13 = 100000; // 0x186a0 float:1.4013E-40 double:4.94066E-319;
        r15 = (r10 > r13 ? 1 : (r10 == r13 ? 0 : -1));
        if (r15 < 0) goto L_0x00b7;
    L_0x0070:
        r15 = r19.zzgg();	 Catch:{ SQLiteFullException -> 0x0064, SQLiteDatabaseLockedException -> 0x0062, SQLiteException -> 0x005f, all -> 0x005b }
        r15 = r15.zzil();	 Catch:{ SQLiteFullException -> 0x0064, SQLiteDatabaseLockedException -> 0x0062, SQLiteException -> 0x005f, all -> 0x005b }
        r4 = "Data loss, local db full";
        r15.log(r4);	 Catch:{ SQLiteFullException -> 0x0064, SQLiteDatabaseLockedException -> 0x0062, SQLiteException -> 0x005f, all -> 0x005b }
        r16 = r13 - r10;
        r10 = 1;
        r13 = r16 + r10;
        r4 = "messages";
        r10 = "rowid in (select rowid from messages order by rowid asc limit ?)";
        r11 = new java.lang.String[r8];	 Catch:{ SQLiteFullException -> 0x0064, SQLiteDatabaseLockedException -> 0x0062, SQLiteException -> 0x005f, all -> 0x005b }
        r15 = java.lang.Long.toString(r13);	 Catch:{ SQLiteFullException -> 0x0064, SQLiteDatabaseLockedException -> 0x0062, SQLiteException -> 0x005f, all -> 0x005b }
        r11[r3] = r15;	 Catch:{ SQLiteFullException -> 0x0064, SQLiteDatabaseLockedException -> 0x0062, SQLiteException -> 0x005f, all -> 0x005b }
        r4 = r9.delete(r4, r10, r11);	 Catch:{ SQLiteFullException -> 0x0064, SQLiteDatabaseLockedException -> 0x0062, SQLiteException -> 0x005f, all -> 0x005b }
        r10 = (long) r4;	 Catch:{ SQLiteFullException -> 0x0064, SQLiteDatabaseLockedException -> 0x0062, SQLiteException -> 0x005f, all -> 0x005b }
        r4 = (r10 > r13 ? 1 : (r10 == r13 ? 0 : -1));
        if (r4 == 0) goto L_0x00b7;
    L_0x0098:
        r4 = r19.zzgg();	 Catch:{ SQLiteFullException -> 0x0064, SQLiteDatabaseLockedException -> 0x0062, SQLiteException -> 0x005f, all -> 0x005b }
        r4 = r4.zzil();	 Catch:{ SQLiteFullException -> 0x0064, SQLiteDatabaseLockedException -> 0x0062, SQLiteException -> 0x005f, all -> 0x005b }
        r15 = "Different delete count than expected in local db. expected, received, difference";
        r3 = java.lang.Long.valueOf(r13);	 Catch:{ SQLiteFullException -> 0x0064, SQLiteDatabaseLockedException -> 0x0062, SQLiteException -> 0x005f, all -> 0x005b }
        r8 = java.lang.Long.valueOf(r10);	 Catch:{ SQLiteFullException -> 0x0064, SQLiteDatabaseLockedException -> 0x0062, SQLiteException -> 0x005f, all -> 0x005b }
        r18 = r8;
        r7 = r13 - r10;
        r7 = java.lang.Long.valueOf(r7);	 Catch:{ SQLiteFullException -> 0x0064, SQLiteDatabaseLockedException -> 0x0062, SQLiteException -> 0x005f, all -> 0x005b }
        r8 = r18;
        r4.zzd(r15, r3, r8, r7);	 Catch:{ SQLiteFullException -> 0x0064, SQLiteDatabaseLockedException -> 0x0062, SQLiteException -> 0x005f, all -> 0x005b }
    L_0x00b7:
        r3 = "messages";
        r4 = 0;
        r9.insertOrThrow(r3, r4, r2);	 Catch:{ SQLiteFullException -> 0x0064, SQLiteDatabaseLockedException -> 0x0062, SQLiteException -> 0x005f, all -> 0x005b }
        r9.setTransactionSuccessful();	 Catch:{ SQLiteFullException -> 0x0064, SQLiteDatabaseLockedException -> 0x0062, SQLiteException -> 0x005f, all -> 0x005b }
        r9.endTransaction();	 Catch:{ SQLiteFullException -> 0x0064, SQLiteDatabaseLockedException -> 0x0062, SQLiteException -> 0x005f, all -> 0x005b }
        if (r12 == 0) goto L_0x00c8;
    L_0x00c5:
        r12.close();
    L_0x00c8:
        if (r9 == 0) goto L_0x00cd;
    L_0x00ca:
        r9.close();
    L_0x00cd:
        r2 = 1;
        return r2;
    L_0x00cf:
        r7 = r12;
        goto L_0x011d;
    L_0x00d2:
        r0 = move-exception;
        r4 = r7;
        r2 = r0;
        r12 = r4;
        goto L_0x0153;
    L_0x00d8:
        r0 = move-exception;
        r4 = r7;
        r3 = r0;
        r12 = r4;
        goto L_0x0038;
    L_0x00de:
        r7 = r4;
        goto L_0x011d;
    L_0x00e0:
        r0 = move-exception;
        r4 = r7;
        goto L_0x0040;
    L_0x00e4:
        r0 = move-exception;
        r4 = r7;
        r2 = r0;
        r9 = r4;
        r12 = r9;
        goto L_0x0153;
    L_0x00eb:
        r0 = move-exception;
        r4 = r7;
        r3 = r0;
        r12 = r7;
    L_0x00ef:
        if (r7 == 0) goto L_0x00ff;
    L_0x00f1:
        r4 = r7.inTransaction();	 Catch:{ all -> 0x00fb }
        if (r4 == 0) goto L_0x00ff;
    L_0x00f7:
        r7.endTransaction();	 Catch:{ all -> 0x00fb }
        goto L_0x00ff;
    L_0x00fb:
        r0 = move-exception;
        r2 = r0;
        r9 = r7;
        goto L_0x0153;
    L_0x00ff:
        r4 = r19.zzgg();	 Catch:{ all -> 0x00fb }
        r4 = r4.zzil();	 Catch:{ all -> 0x00fb }
        r8 = "Error writing entry to local database";
        r4.zzg(r8, r3);	 Catch:{ all -> 0x00fb }
        r3 = 1;
        r1.zzaih = r3;	 Catch:{ all -> 0x00fb }
        if (r12 == 0) goto L_0x0114;
    L_0x0111:
        r12.close();
    L_0x0114:
        if (r7 == 0) goto L_0x014a;
    L_0x0116:
        r7.close();
        goto L_0x014a;
    L_0x011a:
        r0 = move-exception;
        r4 = r7;
        r9 = r7;
    L_0x011d:
        r3 = (long) r6;
        android.os.SystemClock.sleep(r3);	 Catch:{ all -> 0x0150 }
        r6 = r6 + 20;
        if (r7 == 0) goto L_0x0128;
    L_0x0125:
        r7.close();
    L_0x0128:
        if (r9 == 0) goto L_0x014a;
    L_0x012a:
        r9.close();
        goto L_0x014a;
    L_0x012e:
        r0 = move-exception;
        r4 = r7;
        r3 = r0;
        r9 = r7;
    L_0x0132:
        r4 = r19.zzgg();	 Catch:{ all -> 0x0150 }
        r4 = r4.zzil();	 Catch:{ all -> 0x0150 }
        r8 = "Error writing entry to local database";
        r4.zzg(r8, r3);	 Catch:{ all -> 0x0150 }
        r3 = 1;
        r1.zzaih = r3;	 Catch:{ all -> 0x0150 }
        if (r7 == 0) goto L_0x0147;
    L_0x0144:
        r7.close();
    L_0x0147:
        if (r9 == 0) goto L_0x014a;
    L_0x0149:
        goto L_0x012a;
    L_0x014a:
        r5 = r5 + 1;
        r3 = 0;
        r4 = 5;
        goto L_0x0023;
    L_0x0150:
        r0 = move-exception;
        r2 = r0;
        r12 = r7;
    L_0x0153:
        if (r12 == 0) goto L_0x0158;
    L_0x0155:
        r12.close();
    L_0x0158:
        if (r9 == 0) goto L_0x015d;
    L_0x015a:
        r9.close();
    L_0x015d:
        throw r2;
    L_0x015e:
        r2 = r19.zzgg();
        r2 = r2.zzin();
        r3 = "Failed to write entry to local database";
        r2.log(r3);
        r2 = 0;
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzfc.zza(int, byte[]):boolean");
    }

    public final /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    public final void resetAnalyticsData() {
        zzab();
        try {
            int delete = 0 + getWritableDatabase().delete("messages", null, null);
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

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.util.List<com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable> zzp(int r22) {
        /*
        r21 = this;
        r1 = r21;
        r21.zzab();
        r2 = r1.zzaih;
        r3 = 0;
        if (r2 == 0) goto L_0x000b;
    L_0x000a:
        return r3;
    L_0x000b:
        r2 = new java.util.ArrayList;
        r2.<init>();
        r4 = r21.getContext();
        r5 = "google_app_measurement_local.db";
        r4 = r4.getDatabasePath(r5);
        r4 = r4.exists();
        if (r4 != 0) goto L_0x0021;
    L_0x0020:
        return r2;
    L_0x0021:
        r4 = 5;
        r5 = 0;
        r7 = r4;
        r6 = r5;
    L_0x0025:
        if (r6 >= r4) goto L_0x0215;
    L_0x0027:
        r8 = 1;
        r15 = r21.getWritableDatabase();	 Catch:{ SQLiteFullException -> 0x01e4, SQLiteDatabaseLockedException -> 0x01cb, SQLiteException -> 0x01a5, all -> 0x019f }
        if (r15 != 0) goto L_0x0049;
    L_0x002e:
        r1.zzaih = r8;	 Catch:{ SQLiteFullException -> 0x0044, SQLiteDatabaseLockedException -> 0x0040, SQLiteException -> 0x003b, all -> 0x0036 }
        if (r15 == 0) goto L_0x0035;
    L_0x0032:
        r15.close();
    L_0x0035:
        return r3;
    L_0x0036:
        r0 = move-exception;
        r2 = r0;
        r9 = r3;
        goto L_0x0209;
    L_0x003b:
        r0 = move-exception;
        r9 = r3;
    L_0x003d:
        r3 = r0;
        goto L_0x01a9;
    L_0x0040:
        r0 = move-exception;
        r3 = r15;
        goto L_0x0197;
    L_0x0044:
        r0 = move-exception;
        r9 = r3;
    L_0x0046:
        r3 = r0;
        goto L_0x01e8;
    L_0x0049:
        r15.beginTransaction();	 Catch:{ SQLiteFullException -> 0x019a, SQLiteDatabaseLockedException -> 0x0040, SQLiteException -> 0x0192, all -> 0x018e }
        r10 = "messages";
        r9 = 3;
        r11 = new java.lang.String[r9];	 Catch:{ SQLiteFullException -> 0x019a, SQLiteDatabaseLockedException -> 0x0040, SQLiteException -> 0x0192, all -> 0x018e }
        r9 = "rowid";
        r11[r5] = r9;	 Catch:{ SQLiteFullException -> 0x019a, SQLiteDatabaseLockedException -> 0x0040, SQLiteException -> 0x0192, all -> 0x018e }
        r9 = "type";
        r11[r8] = r9;	 Catch:{ SQLiteFullException -> 0x019a, SQLiteDatabaseLockedException -> 0x0040, SQLiteException -> 0x0192, all -> 0x018e }
        r9 = "entry";
        r14 = 2;
        r11[r14] = r9;	 Catch:{ SQLiteFullException -> 0x019a, SQLiteDatabaseLockedException -> 0x0040, SQLiteException -> 0x0192, all -> 0x018e }
        r12 = 0;
        r13 = 0;
        r16 = 0;
        r17 = 0;
        r18 = "rowid asc";
        r9 = 100;
        r19 = java.lang.Integer.toString(r9);	 Catch:{ SQLiteFullException -> 0x019a, SQLiteDatabaseLockedException -> 0x0040, SQLiteException -> 0x0192, all -> 0x018e }
        r9 = r15;
        r4 = r14;
        r14 = r16;
        r3 = r15;
        r15 = r17;
        r16 = r18;
        r17 = r19;
        r9 = r9.query(r10, r11, r12, r13, r14, r15, r16, r17);	 Catch:{ SQLiteFullException -> 0x018b, SQLiteDatabaseLockedException -> 0x0189, SQLiteException -> 0x0186, all -> 0x0184 }
        r10 = -1;
    L_0x007d:
        r12 = r9.moveToNext();	 Catch:{ SQLiteFullException -> 0x0180, SQLiteDatabaseLockedException -> 0x017c, SQLiteException -> 0x0178, all -> 0x0174 }
        if (r12 == 0) goto L_0x0140;
    L_0x0083:
        r10 = r9.getLong(r5);	 Catch:{ SQLiteFullException -> 0x0180, SQLiteDatabaseLockedException -> 0x017c, SQLiteException -> 0x0178, all -> 0x0174 }
        r12 = r9.getInt(r8);	 Catch:{ SQLiteFullException -> 0x0180, SQLiteDatabaseLockedException -> 0x017c, SQLiteException -> 0x0178, all -> 0x0174 }
        r13 = r9.getBlob(r4);	 Catch:{ SQLiteFullException -> 0x0180, SQLiteDatabaseLockedException -> 0x017c, SQLiteException -> 0x0178, all -> 0x0174 }
        if (r12 != 0) goto L_0x00c6;
    L_0x0091:
        r12 = android.os.Parcel.obtain();	 Catch:{ SQLiteFullException -> 0x0180, SQLiteDatabaseLockedException -> 0x017c, SQLiteException -> 0x0178, all -> 0x0174 }
        r14 = r13.length;	 Catch:{ ParseException -> 0x00b0 }
        r12.unmarshall(r13, r5, r14);	 Catch:{ ParseException -> 0x00b0 }
        r12.setDataPosition(r5);	 Catch:{ ParseException -> 0x00b0 }
        r13 = com.google.android.gms.internal.measurement.zzeu.CREATOR;	 Catch:{ ParseException -> 0x00b0 }
        r13 = r13.createFromParcel(r12);	 Catch:{ ParseException -> 0x00b0 }
        r13 = (com.google.android.gms.internal.measurement.zzeu) r13;	 Catch:{ ParseException -> 0x00b0 }
        r12.recycle();	 Catch:{ SQLiteFullException -> 0x0180, SQLiteDatabaseLockedException -> 0x017c, SQLiteException -> 0x0178, all -> 0x0174 }
        if (r13 == 0) goto L_0x007d;
    L_0x00a9:
        r2.add(r13);	 Catch:{ SQLiteFullException -> 0x0180, SQLiteDatabaseLockedException -> 0x017c, SQLiteException -> 0x0178, all -> 0x0174 }
        goto L_0x007d;
    L_0x00ad:
        r0 = move-exception;
        r4 = r0;
        goto L_0x00c2;
    L_0x00b0:
        r0 = move-exception;
        r13 = r21.zzgg();	 Catch:{ all -> 0x00ad }
        r13 = r13.zzil();	 Catch:{ all -> 0x00ad }
        r14 = "Failed to load event from local database";
        r13.log(r14);	 Catch:{ all -> 0x00ad }
        r12.recycle();	 Catch:{ SQLiteFullException -> 0x0180, SQLiteDatabaseLockedException -> 0x017c, SQLiteException -> 0x0178, all -> 0x0174 }
        goto L_0x007d;
    L_0x00c2:
        r12.recycle();	 Catch:{ SQLiteFullException -> 0x0180, SQLiteDatabaseLockedException -> 0x017c, SQLiteException -> 0x0178, all -> 0x0174 }
        throw r4;	 Catch:{ SQLiteFullException -> 0x0180, SQLiteDatabaseLockedException -> 0x017c, SQLiteException -> 0x0178, all -> 0x0174 }
    L_0x00c6:
        if (r12 != r8) goto L_0x00fb;
    L_0x00c8:
        r12 = android.os.Parcel.obtain();	 Catch:{ SQLiteFullException -> 0x0180, SQLiteDatabaseLockedException -> 0x017c, SQLiteException -> 0x0178, all -> 0x0174 }
        r14 = r13.length;	 Catch:{ ParseException -> 0x00e2 }
        r12.unmarshall(r13, r5, r14);	 Catch:{ ParseException -> 0x00e2 }
        r12.setDataPosition(r5);	 Catch:{ ParseException -> 0x00e2 }
        r13 = com.google.android.gms.internal.measurement.zzjs.CREATOR;	 Catch:{ ParseException -> 0x00e2 }
        r13 = r13.createFromParcel(r12);	 Catch:{ ParseException -> 0x00e2 }
        r13 = (com.google.android.gms.internal.measurement.zzjs) r13;	 Catch:{ ParseException -> 0x00e2 }
        r12.recycle();	 Catch:{ SQLiteFullException -> 0x0180, SQLiteDatabaseLockedException -> 0x017c, SQLiteException -> 0x0178, all -> 0x0174 }
        goto L_0x00f4;
    L_0x00df:
        r0 = move-exception;
        r4 = r0;
        goto L_0x00f7;
    L_0x00e2:
        r0 = move-exception;
        r13 = r21.zzgg();	 Catch:{ all -> 0x00df }
        r13 = r13.zzil();	 Catch:{ all -> 0x00df }
        r14 = "Failed to load user property from local database";
        r13.log(r14);	 Catch:{ all -> 0x00df }
        r12.recycle();	 Catch:{ SQLiteFullException -> 0x0180, SQLiteDatabaseLockedException -> 0x017c, SQLiteException -> 0x0178, all -> 0x0174 }
        r13 = 0;
    L_0x00f4:
        if (r13 == 0) goto L_0x007d;
    L_0x00f6:
        goto L_0x00a9;
    L_0x00f7:
        r12.recycle();	 Catch:{ SQLiteFullException -> 0x0180, SQLiteDatabaseLockedException -> 0x017c, SQLiteException -> 0x0178, all -> 0x0174 }
        throw r4;	 Catch:{ SQLiteFullException -> 0x0180, SQLiteDatabaseLockedException -> 0x017c, SQLiteException -> 0x0178, all -> 0x0174 }
    L_0x00fb:
        if (r12 != r4) goto L_0x0131;
    L_0x00fd:
        r12 = android.os.Parcel.obtain();	 Catch:{ SQLiteFullException -> 0x0180, SQLiteDatabaseLockedException -> 0x017c, SQLiteException -> 0x0178, all -> 0x0174 }
        r14 = r13.length;	 Catch:{ ParseException -> 0x0117 }
        r12.unmarshall(r13, r5, r14);	 Catch:{ ParseException -> 0x0117 }
        r12.setDataPosition(r5);	 Catch:{ ParseException -> 0x0117 }
        r13 = com.google.android.gms.internal.measurement.zzef.CREATOR;	 Catch:{ ParseException -> 0x0117 }
        r13 = r13.createFromParcel(r12);	 Catch:{ ParseException -> 0x0117 }
        r13 = (com.google.android.gms.internal.measurement.zzef) r13;	 Catch:{ ParseException -> 0x0117 }
        r12.recycle();	 Catch:{ SQLiteFullException -> 0x0180, SQLiteDatabaseLockedException -> 0x017c, SQLiteException -> 0x0178, all -> 0x0174 }
        goto L_0x0129;
    L_0x0114:
        r0 = move-exception;
        r4 = r0;
        goto L_0x012d;
    L_0x0117:
        r0 = move-exception;
        r13 = r21.zzgg();	 Catch:{ all -> 0x0114 }
        r13 = r13.zzil();	 Catch:{ all -> 0x0114 }
        r14 = "Failed to load user property from local database";
        r13.log(r14);	 Catch:{ all -> 0x0114 }
        r12.recycle();	 Catch:{ SQLiteFullException -> 0x0180, SQLiteDatabaseLockedException -> 0x017c, SQLiteException -> 0x0178, all -> 0x0174 }
        r13 = 0;
    L_0x0129:
        if (r13 == 0) goto L_0x007d;
    L_0x012b:
        goto L_0x00a9;
    L_0x012d:
        r12.recycle();	 Catch:{ SQLiteFullException -> 0x0180, SQLiteDatabaseLockedException -> 0x017c, SQLiteException -> 0x0178, all -> 0x0174 }
        throw r4;	 Catch:{ SQLiteFullException -> 0x0180, SQLiteDatabaseLockedException -> 0x017c, SQLiteException -> 0x0178, all -> 0x0174 }
    L_0x0131:
        r12 = r21.zzgg();	 Catch:{ SQLiteFullException -> 0x0180, SQLiteDatabaseLockedException -> 0x017c, SQLiteException -> 0x0178, all -> 0x0174 }
        r12 = r12.zzil();	 Catch:{ SQLiteFullException -> 0x0180, SQLiteDatabaseLockedException -> 0x017c, SQLiteException -> 0x0178, all -> 0x0174 }
        r13 = "Unknown record type in local database";
        r12.log(r13);	 Catch:{ SQLiteFullException -> 0x0180, SQLiteDatabaseLockedException -> 0x017c, SQLiteException -> 0x0178, all -> 0x0174 }
        goto L_0x007d;
    L_0x0140:
        r4 = "messages";
        r12 = "rowid <= ?";
        r13 = new java.lang.String[r8];	 Catch:{ SQLiteFullException -> 0x0180, SQLiteDatabaseLockedException -> 0x017c, SQLiteException -> 0x0178, all -> 0x0174 }
        r10 = java.lang.Long.toString(r10);	 Catch:{ SQLiteFullException -> 0x0180, SQLiteDatabaseLockedException -> 0x017c, SQLiteException -> 0x0178, all -> 0x0174 }
        r13[r5] = r10;	 Catch:{ SQLiteFullException -> 0x0180, SQLiteDatabaseLockedException -> 0x017c, SQLiteException -> 0x0178, all -> 0x0174 }
        r4 = r3.delete(r4, r12, r13);	 Catch:{ SQLiteFullException -> 0x0180, SQLiteDatabaseLockedException -> 0x017c, SQLiteException -> 0x0178, all -> 0x0174 }
        r10 = r2.size();	 Catch:{ SQLiteFullException -> 0x0180, SQLiteDatabaseLockedException -> 0x017c, SQLiteException -> 0x0178, all -> 0x0174 }
        if (r4 >= r10) goto L_0x0163;
    L_0x0156:
        r4 = r21.zzgg();	 Catch:{ SQLiteFullException -> 0x0180, SQLiteDatabaseLockedException -> 0x017c, SQLiteException -> 0x0178, all -> 0x0174 }
        r4 = r4.zzil();	 Catch:{ SQLiteFullException -> 0x0180, SQLiteDatabaseLockedException -> 0x017c, SQLiteException -> 0x0178, all -> 0x0174 }
        r10 = "Fewer entries removed from local database than expected";
        r4.log(r10);	 Catch:{ SQLiteFullException -> 0x0180, SQLiteDatabaseLockedException -> 0x017c, SQLiteException -> 0x0178, all -> 0x0174 }
    L_0x0163:
        r3.setTransactionSuccessful();	 Catch:{ SQLiteFullException -> 0x0180, SQLiteDatabaseLockedException -> 0x017c, SQLiteException -> 0x0178, all -> 0x0174 }
        r3.endTransaction();	 Catch:{ SQLiteFullException -> 0x0180, SQLiteDatabaseLockedException -> 0x017c, SQLiteException -> 0x0178, all -> 0x0174 }
        if (r9 == 0) goto L_0x016e;
    L_0x016b:
        r9.close();
    L_0x016e:
        if (r3 == 0) goto L_0x0173;
    L_0x0170:
        r3.close();
    L_0x0173:
        return r2;
    L_0x0174:
        r0 = move-exception;
        r2 = r0;
        goto L_0x020a;
    L_0x0178:
        r0 = move-exception;
        r15 = r3;
        goto L_0x003d;
    L_0x017c:
        r0 = move-exception;
        r4 = r3;
        r3 = r9;
        goto L_0x01ce;
    L_0x0180:
        r0 = move-exception;
        r15 = r3;
        goto L_0x0046;
    L_0x0184:
        r0 = move-exception;
        goto L_0x0190;
    L_0x0186:
        r0 = move-exception;
        r15 = r3;
        goto L_0x0194;
    L_0x0189:
        r0 = move-exception;
        goto L_0x0197;
    L_0x018b:
        r0 = move-exception;
        r15 = r3;
        goto L_0x019c;
    L_0x018e:
        r0 = move-exception;
        r3 = r15;
    L_0x0190:
        r2 = r0;
        goto L_0x01a2;
    L_0x0192:
        r0 = move-exception;
        r3 = r15;
    L_0x0194:
        r9 = 0;
        goto L_0x003d;
    L_0x0197:
        r4 = r3;
        r3 = 0;
        goto L_0x01ce;
    L_0x019a:
        r0 = move-exception;
        r3 = r15;
    L_0x019c:
        r9 = 0;
        goto L_0x0046;
    L_0x019f:
        r0 = move-exception;
        r2 = r0;
        r3 = 0;
    L_0x01a2:
        r9 = 0;
        goto L_0x020a;
    L_0x01a5:
        r0 = move-exception;
        r3 = r0;
        r9 = 0;
        r15 = 0;
    L_0x01a9:
        if (r15 == 0) goto L_0x01b4;
    L_0x01ab:
        r4 = r15.inTransaction();	 Catch:{ all -> 0x0207 }
        if (r4 == 0) goto L_0x01b4;
    L_0x01b1:
        r15.endTransaction();	 Catch:{ all -> 0x0207 }
    L_0x01b4:
        r4 = r21.zzgg();	 Catch:{ all -> 0x0207 }
        r4 = r4.zzil();	 Catch:{ all -> 0x0207 }
        r10 = "Error reading entries from local database";
        r4.zzg(r10, r3);	 Catch:{ all -> 0x0207 }
        r1.zzaih = r8;	 Catch:{ all -> 0x0207 }
        if (r9 == 0) goto L_0x01c8;
    L_0x01c5:
        r9.close();
    L_0x01c8:
        if (r15 == 0) goto L_0x0201;
    L_0x01ca:
        goto L_0x01fe;
    L_0x01cb:
        r0 = move-exception;
        r3 = 0;
        r4 = 0;
    L_0x01ce:
        r8 = (long) r7;
        android.os.SystemClock.sleep(r8);	 Catch:{ all -> 0x01df }
        r7 = r7 + 20;
        if (r3 == 0) goto L_0x01d9;
    L_0x01d6:
        r3.close();
    L_0x01d9:
        if (r4 == 0) goto L_0x0201;
    L_0x01db:
        r4.close();
        goto L_0x0201;
    L_0x01df:
        r0 = move-exception;
        r2 = r0;
        r9 = r3;
        r3 = r4;
        goto L_0x020a;
    L_0x01e4:
        r0 = move-exception;
        r3 = r0;
        r9 = 0;
        r15 = 0;
    L_0x01e8:
        r4 = r21.zzgg();	 Catch:{ all -> 0x0207 }
        r4 = r4.zzil();	 Catch:{ all -> 0x0207 }
        r10 = "Error reading entries from local database";
        r4.zzg(r10, r3);	 Catch:{ all -> 0x0207 }
        r1.zzaih = r8;	 Catch:{ all -> 0x0207 }
        if (r9 == 0) goto L_0x01fc;
    L_0x01f9:
        r9.close();
    L_0x01fc:
        if (r15 == 0) goto L_0x0201;
    L_0x01fe:
        r15.close();
    L_0x0201:
        r6 = r6 + 1;
        r3 = 0;
        r4 = 5;
        goto L_0x0025;
    L_0x0207:
        r0 = move-exception;
        r2 = r0;
    L_0x0209:
        r3 = r15;
    L_0x020a:
        if (r9 == 0) goto L_0x020f;
    L_0x020c:
        r9.close();
    L_0x020f:
        if (r3 == 0) goto L_0x0214;
    L_0x0211:
        r3.close();
    L_0x0214:
        throw r2;
    L_0x0215:
        r2 = r21.zzgg();
        r2 = r2.zzin();
        r3 = "Failed to read events from database in reasonable time";
        r2.log(r3);
        r2 = 0;
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzfc.zzp(int):java.util.List<com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable>");
    }
}
