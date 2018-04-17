package com.google.android.gms.internal.measurement;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Pair;
import com.google.android.gms.common.internal.Preconditions;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.telegram.messenger.exoplayer2.upstream.DataSchemeDataSource;

final class zzei extends zzhk {
    private static final String[] zzaeu = new String[]{"last_bundled_timestamp", "ALTER TABLE events ADD COLUMN last_bundled_timestamp INTEGER;", "last_sampled_complex_event_id", "ALTER TABLE events ADD COLUMN last_sampled_complex_event_id INTEGER;", "last_sampling_rate", "ALTER TABLE events ADD COLUMN last_sampling_rate INTEGER;", "last_exempt_from_sampling", "ALTER TABLE events ADD COLUMN last_exempt_from_sampling INTEGER;"};
    private static final String[] zzaev = new String[]{TtmlNode.ATTR_TTS_ORIGIN, "ALTER TABLE user_attributes ADD COLUMN origin TEXT;"};
    private static final String[] zzaew = new String[]{"app_version", "ALTER TABLE apps ADD COLUMN app_version TEXT;", "app_store", "ALTER TABLE apps ADD COLUMN app_store TEXT;", "gmp_version", "ALTER TABLE apps ADD COLUMN gmp_version INTEGER;", "dev_cert_hash", "ALTER TABLE apps ADD COLUMN dev_cert_hash INTEGER;", "measurement_enabled", "ALTER TABLE apps ADD COLUMN measurement_enabled INTEGER;", "last_bundle_start_timestamp", "ALTER TABLE apps ADD COLUMN last_bundle_start_timestamp INTEGER;", "day", "ALTER TABLE apps ADD COLUMN day INTEGER;", "daily_public_events_count", "ALTER TABLE apps ADD COLUMN daily_public_events_count INTEGER;", "daily_events_count", "ALTER TABLE apps ADD COLUMN daily_events_count INTEGER;", "daily_conversions_count", "ALTER TABLE apps ADD COLUMN daily_conversions_count INTEGER;", "remote_config", "ALTER TABLE apps ADD COLUMN remote_config BLOB;", "config_fetched_time", "ALTER TABLE apps ADD COLUMN config_fetched_time INTEGER;", "failed_config_fetch_time", "ALTER TABLE apps ADD COLUMN failed_config_fetch_time INTEGER;", "app_version_int", "ALTER TABLE apps ADD COLUMN app_version_int INTEGER;", "firebase_instance_id", "ALTER TABLE apps ADD COLUMN firebase_instance_id TEXT;", "daily_error_events_count", "ALTER TABLE apps ADD COLUMN daily_error_events_count INTEGER;", "daily_realtime_events_count", "ALTER TABLE apps ADD COLUMN daily_realtime_events_count INTEGER;", "health_monitor_sample", "ALTER TABLE apps ADD COLUMN health_monitor_sample TEXT;", "android_id", "ALTER TABLE apps ADD COLUMN android_id INTEGER;", "adid_reporting_enabled", "ALTER TABLE apps ADD COLUMN adid_reporting_enabled INTEGER;", "ssaid_reporting_enabled", "ALTER TABLE apps ADD COLUMN ssaid_reporting_enabled INTEGER;"};
    private static final String[] zzaex = new String[]{"realtime", "ALTER TABLE raw_events ADD COLUMN realtime INTEGER;"};
    private static final String[] zzaey = new String[]{"has_realtime", "ALTER TABLE queue ADD COLUMN has_realtime INTEGER;", "retry_count", "ALTER TABLE queue ADD COLUMN retry_count INTEGER;"};
    private static final String[] zzaez = new String[]{"previous_install_count", "ALTER TABLE app2 ADD COLUMN previous_install_count INTEGER;"};
    private final zzel zzafa = new zzel(this, getContext(), "google_app_measurement.db");
    private final zzjp zzafb = new zzjp(zzbt());

    zzei(zzgl com_google_android_gms_internal_measurement_zzgl) {
        super(com_google_android_gms_internal_measurement_zzgl);
    }

    private final long zza(String str, String[] strArr) {
        Object e;
        Throwable th;
        Cursor cursor = null;
        try {
            Cursor rawQuery = getWritableDatabase().rawQuery(str, strArr);
            try {
                if (rawQuery.moveToFirst()) {
                    long j = rawQuery.getLong(0);
                    if (rawQuery != null) {
                        rawQuery.close();
                    }
                    return j;
                }
                throw new SQLiteException("Database returned empty set");
            } catch (SQLiteException e2) {
                e = e2;
                cursor = rawQuery;
                try {
                    zzgg().zzil().zze("Database error", str, e);
                    throw e;
                } catch (Throwable th2) {
                    th = th2;
                    rawQuery = cursor;
                    if (rawQuery != null) {
                        rawQuery.close();
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                if (rawQuery != null) {
                    rawQuery.close();
                }
                throw th;
            }
        } catch (SQLiteException e3) {
            e = e3;
            zzgg().zzil().zze("Database error", str, e);
            throw e;
        }
    }

    private final long zza(String str, String[] strArr, long j) {
        Object e;
        Throwable th;
        Cursor cursor = null;
        try {
            Cursor rawQuery = getWritableDatabase().rawQuery(str, strArr);
            try {
                if (rawQuery.moveToFirst()) {
                    j = rawQuery.getLong(0);
                    if (rawQuery != null) {
                        rawQuery.close();
                    }
                    return j;
                }
                if (rawQuery != null) {
                    rawQuery.close();
                }
                return j;
            } catch (SQLiteException e2) {
                e = e2;
                cursor = rawQuery;
                try {
                    zzgg().zzil().zze("Database error", str, e);
                    throw e;
                } catch (Throwable th2) {
                    th = th2;
                    if (cursor != null) {
                        cursor.close();
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                cursor = rawQuery;
                if (cursor != null) {
                    cursor.close();
                }
                throw th;
            }
        } catch (SQLiteException e3) {
            e = e3;
            zzgg().zzil().zze("Database error", str, e);
            throw e;
        }
    }

    private final Object zza(Cursor cursor, int i) {
        int type = cursor.getType(i);
        switch (type) {
            case 0:
                zzgg().zzil().log("Loaded invalid null value from database");
                return null;
            case 1:
                return Long.valueOf(cursor.getLong(i));
            case 2:
                return Double.valueOf(cursor.getDouble(i));
            case 3:
                return cursor.getString(i);
            case 4:
                zzgg().zzil().log("Loaded invalid blob type value, ignoring it");
                return null;
            default:
                zzgg().zzil().zzg("Loaded invalid unknown value type, ignoring it", Integer.valueOf(type));
                return null;
        }
    }

    private static void zza(ContentValues contentValues, String str, Object obj) {
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotNull(obj);
        if (obj instanceof String) {
            contentValues.put(str, (String) obj);
        } else if (obj instanceof Long) {
            contentValues.put(str, (Long) obj);
        } else if (obj instanceof Double) {
            contentValues.put(str, (Double) obj);
        } else {
            throw new IllegalArgumentException("Invalid value type");
        }
    }

    static void zza(zzfg com_google_android_gms_internal_measurement_zzfg, SQLiteDatabase sQLiteDatabase) {
        if (com_google_android_gms_internal_measurement_zzfg == null) {
            throw new IllegalArgumentException("Monitor must not be null");
        }
        File file = new File(sQLiteDatabase.getPath());
        if (!file.setReadable(false, false)) {
            com_google_android_gms_internal_measurement_zzfg.zzin().log("Failed to turn off database read permission");
        }
        if (!file.setWritable(false, false)) {
            com_google_android_gms_internal_measurement_zzfg.zzin().log("Failed to turn off database write permission");
        }
        if (!file.setReadable(true, true)) {
            com_google_android_gms_internal_measurement_zzfg.zzin().log("Failed to turn on database read permission for owner");
        }
        if (!file.setWritable(true, true)) {
            com_google_android_gms_internal_measurement_zzfg.zzin().log("Failed to turn on database write permission for owner");
        }
    }

    static void zza(zzfg com_google_android_gms_internal_measurement_zzfg, SQLiteDatabase sQLiteDatabase, String str, String str2, String str3, String[] strArr) throws SQLiteException {
        if (com_google_android_gms_internal_measurement_zzfg == null) {
            throw new IllegalArgumentException("Monitor must not be null");
        }
        if (!zza(com_google_android_gms_internal_measurement_zzfg, sQLiteDatabase, str)) {
            sQLiteDatabase.execSQL(str2);
        }
        if (com_google_android_gms_internal_measurement_zzfg == null) {
            try {
                throw new IllegalArgumentException("Monitor must not be null");
            } catch (SQLiteException e) {
                com_google_android_gms_internal_measurement_zzfg.zzil().zzg("Failed to verify columns on table that was just created", str);
                throw e;
            }
        }
        Iterable zzb = zzb(sQLiteDatabase, str);
        String[] split = str3.split(",");
        int length = split.length;
        int i = 0;
        int i2 = 0;
        while (i2 < length) {
            String str4 = split[i2];
            if (zzb.remove(str4)) {
                i2++;
            } else {
                StringBuilder stringBuilder = new StringBuilder((35 + String.valueOf(str).length()) + String.valueOf(str4).length());
                stringBuilder.append("Table ");
                stringBuilder.append(str);
                stringBuilder.append(" is missing required column: ");
                stringBuilder.append(str4);
                throw new SQLiteException(stringBuilder.toString());
            }
        }
        if (strArr != null) {
            while (i < strArr.length) {
                if (!zzb.remove(strArr[i])) {
                    sQLiteDatabase.execSQL(strArr[i + 1]);
                }
                i += 2;
            }
        }
        if (!zzb.isEmpty()) {
            com_google_android_gms_internal_measurement_zzfg.zzin().zze("Table has extra columns. table, columns", str, TextUtils.join(", ", zzb));
        }
    }

    private static boolean zza(zzfg com_google_android_gms_internal_measurement_zzfg, SQLiteDatabase sQLiteDatabase, String str) {
        Object obj;
        Throwable th;
        if (com_google_android_gms_internal_measurement_zzfg == null) {
            throw new IllegalArgumentException("Monitor must not be null");
        }
        Cursor cursor = null;
        try {
            SQLiteDatabase sQLiteDatabase2 = sQLiteDatabase;
            Cursor query = sQLiteDatabase2.query("SQLITE_MASTER", new String[]{"name"}, "name=?", new String[]{str}, null, null, null);
            try {
                boolean moveToFirst = query.moveToFirst();
                if (query != null) {
                    query.close();
                }
                return moveToFirst;
            } catch (SQLiteException e) {
                SQLiteException sQLiteException = e;
                cursor = query;
                obj = sQLiteException;
                try {
                    com_google_android_gms_internal_measurement_zzfg.zzin().zze("Error querying for table", str, obj);
                    if (cursor != null) {
                        cursor.close();
                    }
                    return false;
                } catch (Throwable th2) {
                    th = th2;
                    if (cursor != null) {
                        cursor.close();
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                cursor = query;
                if (cursor != null) {
                    cursor.close();
                }
                throw th;
            }
        } catch (SQLiteException e2) {
            obj = e2;
            com_google_android_gms_internal_measurement_zzfg.zzin().zze("Error querying for table", str, obj);
            if (cursor != null) {
                cursor.close();
            }
            return false;
        }
    }

    private final boolean zza(String str, int i, zzjz com_google_android_gms_internal_measurement_zzjz) {
        zzch();
        zzab();
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzjz);
        if (TextUtils.isEmpty(com_google_android_gms_internal_measurement_zzjz.zzarl)) {
            zzgg().zzin().zzd("Event filter had no event name. Audience definition ignored. appId, audienceId, filterId", zzfg.zzbh(str), Integer.valueOf(i), String.valueOf(com_google_android_gms_internal_measurement_zzjz.zzark));
            return false;
        }
        try {
            byte[] bArr = new byte[com_google_android_gms_internal_measurement_zzjz.zzwg()];
            zzabb zzb = zzabb.zzb(bArr, 0, bArr.length);
            com_google_android_gms_internal_measurement_zzjz.zza(zzb);
            zzb.zzvy();
            ContentValues contentValues = new ContentValues();
            contentValues.put("app_id", str);
            contentValues.put("audience_id", Integer.valueOf(i));
            contentValues.put("filter_id", com_google_android_gms_internal_measurement_zzjz.zzark);
            contentValues.put("event_name", com_google_android_gms_internal_measurement_zzjz.zzarl);
            contentValues.put(DataSchemeDataSource.SCHEME_DATA, bArr);
            try {
                if (getWritableDatabase().insertWithOnConflict("event_filters", null, contentValues, 5) == -1) {
                    zzgg().zzil().zzg("Failed to insert event filter (got -1). appId", zzfg.zzbh(str));
                }
                return true;
            } catch (SQLiteException e) {
                zzgg().zzil().zze("Error storing event filter. appId", zzfg.zzbh(str), e);
                return false;
            }
        } catch (IOException e2) {
            zzgg().zzil().zze("Configuration loss. Failed to serialize event filter. appId", zzfg.zzbh(str), e2);
            return false;
        }
    }

    private final boolean zza(String str, int i, zzkc com_google_android_gms_internal_measurement_zzkc) {
        zzch();
        zzab();
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzkc);
        if (TextUtils.isEmpty(com_google_android_gms_internal_measurement_zzkc.zzasa)) {
            zzgg().zzin().zzd("Property filter had no property name. Audience definition ignored. appId, audienceId, filterId", zzfg.zzbh(str), Integer.valueOf(i), String.valueOf(com_google_android_gms_internal_measurement_zzkc.zzark));
            return false;
        }
        try {
            byte[] bArr = new byte[com_google_android_gms_internal_measurement_zzkc.zzwg()];
            zzabb zzb = zzabb.zzb(bArr, 0, bArr.length);
            com_google_android_gms_internal_measurement_zzkc.zza(zzb);
            zzb.zzvy();
            ContentValues contentValues = new ContentValues();
            contentValues.put("app_id", str);
            contentValues.put("audience_id", Integer.valueOf(i));
            contentValues.put("filter_id", com_google_android_gms_internal_measurement_zzkc.zzark);
            contentValues.put("property_name", com_google_android_gms_internal_measurement_zzkc.zzasa);
            contentValues.put(DataSchemeDataSource.SCHEME_DATA, bArr);
            try {
                if (getWritableDatabase().insertWithOnConflict("property_filters", null, contentValues, 5) != -1) {
                    return true;
                }
                zzgg().zzil().zzg("Failed to insert property filter (got -1). appId", zzfg.zzbh(str));
                return false;
            } catch (SQLiteException e) {
                zzgg().zzil().zze("Error storing property filter. appId", zzfg.zzbh(str), e);
                return false;
            }
        } catch (IOException e2) {
            zzgg().zzil().zze("Configuration loss. Failed to serialize property filter. appId", zzfg.zzbh(str), e2);
            return false;
        }
    }

    private final boolean zza(String str, List<Integer> list) {
        Preconditions.checkNotEmpty(str);
        zzch();
        zzab();
        SQLiteDatabase writableDatabase = getWritableDatabase();
        try {
            if (zza("select count(1) from audience_filter_values where app_id=?", new String[]{str}) <= ((long) Math.max(0, Math.min(2000, zzgi().zzb(str, zzew.zzahq))))) {
                return false;
            }
            Iterable arrayList = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                Integer num = (Integer) list.get(i);
                if (num == null || !(num instanceof Integer)) {
                    return false;
                }
                arrayList.add(Integer.toString(num.intValue()));
            }
            String join = TextUtils.join(",", arrayList);
            StringBuilder stringBuilder = new StringBuilder(String.valueOf(join).length() + 2);
            stringBuilder.append("(");
            stringBuilder.append(join);
            stringBuilder.append(")");
            join = stringBuilder.toString();
            StringBuilder stringBuilder2 = new StringBuilder(140 + String.valueOf(join).length());
            stringBuilder2.append("audience_id in (select audience_id from audience_filter_values where app_id=? and audience_id not in ");
            stringBuilder2.append(join);
            stringBuilder2.append(" order by rowid desc limit -1 offset ?)");
            return writableDatabase.delete("audience_filter_values", stringBuilder2.toString(), new String[]{str, Integer.toString(r2)}) > 0;
        } catch (SQLiteException e) {
            zzgg().zzil().zze("Database error querying filters. appId", zzfg.zzbh(str), e);
            return false;
        }
    }

    private static Set<String> zzb(SQLiteDatabase sQLiteDatabase, String str) {
        Object hashSet = new HashSet();
        StringBuilder stringBuilder = new StringBuilder(22 + String.valueOf(str).length());
        stringBuilder.append("SELECT * FROM ");
        stringBuilder.append(str);
        stringBuilder.append(" LIMIT 0");
        Cursor rawQuery = sQLiteDatabase.rawQuery(stringBuilder.toString(), null);
        try {
            Collections.addAll(hashSet, rawQuery.getColumnNames());
            return hashSet;
        } finally {
            rawQuery.close();
        }
    }

    private final boolean zzhv() {
        return getContext().getDatabasePath("google_app_measurement.db").exists();
    }

    public final void beginTransaction() {
        zzch();
        getWritableDatabase().beginTransaction();
    }

    public final void endTransaction() {
        zzch();
        getWritableDatabase().endTransaction();
    }

    final SQLiteDatabase getWritableDatabase() {
        zzab();
        try {
            return this.zzafa.getWritableDatabase();
        } catch (SQLiteException e) {
            zzgg().zzin().zzg("Error opening database", e);
            throw e;
        }
    }

    public final void setTransactionSuccessful() {
        zzch();
        getWritableDatabase().setTransactionSuccessful();
    }

    public final Pair<zzki, Long> zza(String str, Long l) {
        Cursor rawQuery;
        Object e;
        Throwable th;
        zzab();
        zzch();
        try {
            rawQuery = getWritableDatabase().rawQuery("select main_event, children_to_process from main_event_params where app_id=? and event_id=?", new String[]{str, String.valueOf(l)});
            try {
                if (rawQuery.moveToFirst()) {
                    byte[] blob = rawQuery.getBlob(0);
                    Long valueOf = Long.valueOf(rawQuery.getLong(1));
                    zzaba zza = zzaba.zza(blob, 0, blob.length);
                    zzabj com_google_android_gms_internal_measurement_zzki = new zzki();
                    try {
                        com_google_android_gms_internal_measurement_zzki.zzb(zza);
                        Pair<zzki, Long> create = Pair.create(com_google_android_gms_internal_measurement_zzki, valueOf);
                        if (rawQuery != null) {
                            rawQuery.close();
                        }
                        return create;
                    } catch (IOException e2) {
                        zzgg().zzil().zzd("Failed to merge main event. appId, eventId", zzfg.zzbh(str), l, e2);
                        if (rawQuery != null) {
                            rawQuery.close();
                        }
                        return null;
                    }
                }
                zzgg().zzir().log("Main event not found");
                if (rawQuery != null) {
                    rawQuery.close();
                }
                return null;
            } catch (SQLiteException e3) {
                e = e3;
                try {
                    zzgg().zzil().zzg("Error selecting main event", e);
                    if (rawQuery != null) {
                        rawQuery.close();
                    }
                    return null;
                } catch (Throwable th2) {
                    th = th2;
                    if (rawQuery != null) {
                        rawQuery.close();
                    }
                    throw th;
                }
            }
        } catch (SQLiteException e4) {
            e = e4;
            rawQuery = null;
            zzgg().zzil().zzg("Error selecting main event", e);
            if (rawQuery != null) {
                rawQuery.close();
            }
            return null;
        } catch (Throwable th3) {
            th = th3;
            rawQuery = null;
            if (rawQuery != null) {
                rawQuery.close();
            }
            throw th;
        }
    }

    public final zzej zza(long j, String str, boolean z, boolean z2, boolean z3, boolean z4, boolean z5) {
        Cursor query;
        Object obj;
        Cursor cursor;
        Throwable th;
        Preconditions.checkNotEmpty(str);
        zzab();
        zzch();
        String[] strArr = new String[]{str};
        zzej com_google_android_gms_internal_measurement_zzej = new zzej();
        try {
            SQLiteDatabase writableDatabase = getWritableDatabase();
            String[] strArr2 = new String[]{str};
            SQLiteDatabase sQLiteDatabase = writableDatabase;
            int i = 5;
            i = 4;
            i = 3;
            i = 2;
            query = sQLiteDatabase.query("apps", new String[]{"day", "daily_events_count", "daily_public_events_count", "daily_conversions_count", "daily_error_events_count", "daily_realtime_events_count"}, "app_id=?", strArr2, null, null, null);
            try {
                if (query.moveToFirst()) {
                    if (query.getLong(0) == j) {
                        com_google_android_gms_internal_measurement_zzej.zzafd = query.getLong(1);
                        com_google_android_gms_internal_measurement_zzej.zzafc = query.getLong(i);
                        com_google_android_gms_internal_measurement_zzej.zzafe = query.getLong(3);
                        com_google_android_gms_internal_measurement_zzej.zzaff = query.getLong(4);
                        com_google_android_gms_internal_measurement_zzej.zzafg = query.getLong(5);
                    }
                    if (z) {
                        com_google_android_gms_internal_measurement_zzej.zzafd++;
                    }
                    if (z2) {
                        com_google_android_gms_internal_measurement_zzej.zzafc++;
                    }
                    if (z3) {
                        com_google_android_gms_internal_measurement_zzej.zzafe++;
                    }
                    if (z4) {
                        com_google_android_gms_internal_measurement_zzej.zzaff++;
                    }
                    if (z5) {
                        com_google_android_gms_internal_measurement_zzej.zzafg++;
                    }
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("day", Long.valueOf(j));
                    contentValues.put("daily_public_events_count", Long.valueOf(com_google_android_gms_internal_measurement_zzej.zzafc));
                    contentValues.put("daily_events_count", Long.valueOf(com_google_android_gms_internal_measurement_zzej.zzafd));
                    contentValues.put("daily_conversions_count", Long.valueOf(com_google_android_gms_internal_measurement_zzej.zzafe));
                    contentValues.put("daily_error_events_count", Long.valueOf(com_google_android_gms_internal_measurement_zzej.zzaff));
                    contentValues.put("daily_realtime_events_count", Long.valueOf(com_google_android_gms_internal_measurement_zzej.zzafg));
                    writableDatabase.update("apps", contentValues, "app_id=?", strArr);
                    if (query != null) {
                        query.close();
                    }
                    return com_google_android_gms_internal_measurement_zzej;
                }
                zzgg().zzin().zzg("Not updating daily counts, app is not known. appId", zzfg.zzbh(str));
                if (query != null) {
                    query.close();
                }
                return com_google_android_gms_internal_measurement_zzej;
            } catch (SQLiteException e) {
                obj = e;
                cursor = query;
                try {
                    zzgg().zzil().zze("Error updating daily counts. appId", zzfg.zzbh(str), obj);
                    if (cursor != null) {
                        cursor.close();
                    }
                    return com_google_android_gms_internal_measurement_zzej;
                } catch (Throwable th2) {
                    th = th2;
                    query = cursor;
                    if (query != null) {
                        query.close();
                    }
                    throw th;
                }
            } catch (Throwable th22) {
                th = th22;
                if (query != null) {
                    query.close();
                }
                throw th;
            }
        } catch (SQLiteException e2) {
            obj = e2;
            cursor = null;
            zzgg().zzil().zze("Error updating daily counts. appId", zzfg.zzbh(str), obj);
            if (cursor != null) {
                cursor.close();
            }
            return com_google_android_gms_internal_measurement_zzej;
        } catch (Throwable th222) {
            th = th222;
            query = null;
            if (query != null) {
                query.close();
            }
            throw th;
        }
    }

    public final void zza(zzeb com_google_android_gms_internal_measurement_zzeb) {
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzeb);
        zzab();
        zzch();
        ContentValues contentValues = new ContentValues();
        contentValues.put("app_id", com_google_android_gms_internal_measurement_zzeb.zzah());
        contentValues.put("app_instance_id", com_google_android_gms_internal_measurement_zzeb.getAppInstanceId());
        contentValues.put("gmp_app_id", com_google_android_gms_internal_measurement_zzeb.getGmpAppId());
        contentValues.put("resettable_device_id_hash", com_google_android_gms_internal_measurement_zzeb.zzgk());
        contentValues.put("last_bundle_index", Long.valueOf(com_google_android_gms_internal_measurement_zzeb.zzgs()));
        contentValues.put("last_bundle_start_timestamp", Long.valueOf(com_google_android_gms_internal_measurement_zzeb.zzgm()));
        contentValues.put("last_bundle_end_timestamp", Long.valueOf(com_google_android_gms_internal_measurement_zzeb.zzgn()));
        contentValues.put("app_version", com_google_android_gms_internal_measurement_zzeb.zzag());
        contentValues.put("app_store", com_google_android_gms_internal_measurement_zzeb.zzgp());
        contentValues.put("gmp_version", Long.valueOf(com_google_android_gms_internal_measurement_zzeb.zzgq()));
        contentValues.put("dev_cert_hash", Long.valueOf(com_google_android_gms_internal_measurement_zzeb.zzgr()));
        contentValues.put("measurement_enabled", Boolean.valueOf(com_google_android_gms_internal_measurement_zzeb.isMeasurementEnabled()));
        contentValues.put("day", Long.valueOf(com_google_android_gms_internal_measurement_zzeb.zzgw()));
        contentValues.put("daily_public_events_count", Long.valueOf(com_google_android_gms_internal_measurement_zzeb.zzgx()));
        contentValues.put("daily_events_count", Long.valueOf(com_google_android_gms_internal_measurement_zzeb.zzgy()));
        contentValues.put("daily_conversions_count", Long.valueOf(com_google_android_gms_internal_measurement_zzeb.zzgz()));
        contentValues.put("config_fetched_time", Long.valueOf(com_google_android_gms_internal_measurement_zzeb.zzgt()));
        contentValues.put("failed_config_fetch_time", Long.valueOf(com_google_android_gms_internal_measurement_zzeb.zzgu()));
        contentValues.put("app_version_int", Long.valueOf(com_google_android_gms_internal_measurement_zzeb.zzgo()));
        contentValues.put("firebase_instance_id", com_google_android_gms_internal_measurement_zzeb.zzgl());
        contentValues.put("daily_error_events_count", Long.valueOf(com_google_android_gms_internal_measurement_zzeb.zzhb()));
        contentValues.put("daily_realtime_events_count", Long.valueOf(com_google_android_gms_internal_measurement_zzeb.zzha()));
        contentValues.put("health_monitor_sample", com_google_android_gms_internal_measurement_zzeb.zzhc());
        contentValues.put("android_id", Long.valueOf(com_google_android_gms_internal_measurement_zzeb.zzhe()));
        contentValues.put("adid_reporting_enabled", Boolean.valueOf(com_google_android_gms_internal_measurement_zzeb.zzhf()));
        contentValues.put("ssaid_reporting_enabled", Boolean.valueOf(com_google_android_gms_internal_measurement_zzeb.zzhg()));
        try {
            SQLiteDatabase writableDatabase = getWritableDatabase();
            if (((long) writableDatabase.update("apps", contentValues, "app_id = ?", new String[]{com_google_android_gms_internal_measurement_zzeb.zzah()})) == 0 && writableDatabase.insertWithOnConflict("apps", null, contentValues, 5) == -1) {
                zzgg().zzil().zzg("Failed to insert/update app (got -1). appId", zzfg.zzbh(com_google_android_gms_internal_measurement_zzeb.zzah()));
            }
        } catch (SQLiteException e) {
            zzgg().zzil().zze("Error storing app. appId", zzfg.zzbh(com_google_android_gms_internal_measurement_zzeb.zzah()), e);
        }
    }

    public final void zza(zzeq com_google_android_gms_internal_measurement_zzeq) {
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzeq);
        zzab();
        zzch();
        ContentValues contentValues = new ContentValues();
        contentValues.put("app_id", com_google_android_gms_internal_measurement_zzeq.zztd);
        contentValues.put("name", com_google_android_gms_internal_measurement_zzeq.name);
        contentValues.put("lifetime_count", Long.valueOf(com_google_android_gms_internal_measurement_zzeq.zzafp));
        contentValues.put("current_bundle_count", Long.valueOf(com_google_android_gms_internal_measurement_zzeq.zzafq));
        contentValues.put("last_fire_timestamp", Long.valueOf(com_google_android_gms_internal_measurement_zzeq.zzafr));
        contentValues.put("last_bundled_timestamp", Long.valueOf(com_google_android_gms_internal_measurement_zzeq.zzafs));
        contentValues.put("last_sampled_complex_event_id", com_google_android_gms_internal_measurement_zzeq.zzaft);
        contentValues.put("last_sampling_rate", com_google_android_gms_internal_measurement_zzeq.zzafu);
        Long valueOf = (com_google_android_gms_internal_measurement_zzeq.zzafv == null || !com_google_android_gms_internal_measurement_zzeq.zzafv.booleanValue()) ? null : Long.valueOf(1);
        contentValues.put("last_exempt_from_sampling", valueOf);
        try {
            if (getWritableDatabase().insertWithOnConflict("events", null, contentValues, 5) == -1) {
                zzgg().zzil().zzg("Failed to insert/update event aggregates (got -1). appId", zzfg.zzbh(com_google_android_gms_internal_measurement_zzeq.zztd));
            }
        } catch (SQLiteException e) {
            zzgg().zzil().zze("Error storing event aggregates. appId", zzfg.zzbh(com_google_android_gms_internal_measurement_zzeq.zztd), e);
        }
    }

    final void zza(String str, zzjy[] com_google_android_gms_internal_measurement_zzjyArr) {
        zzch();
        zzab();
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzjyArr);
        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.beginTransaction();
        try {
            zzch();
            zzab();
            Preconditions.checkNotEmpty(str);
            SQLiteDatabase writableDatabase2 = getWritableDatabase();
            String[] strArr = new String[1];
            int i = 0;
            strArr[0] = str;
            writableDatabase2.delete("property_filters", "app_id=?", strArr);
            writableDatabase2.delete("event_filters", "app_id=?", new String[]{str});
            for (zzjy com_google_android_gms_internal_measurement_zzjy : com_google_android_gms_internal_measurement_zzjyArr) {
                zzch();
                zzab();
                Preconditions.checkNotEmpty(str);
                Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzjy);
                Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzjy.zzari);
                Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzjy.zzarh);
                if (com_google_android_gms_internal_measurement_zzjy.zzarg == null) {
                    zzgg().zzin().zzg("Audience with no ID. appId", zzfg.zzbh(str));
                } else {
                    zzfi zzin;
                    String str2;
                    Object zzbh;
                    Object obj;
                    int i2;
                    int intValue = com_google_android_gms_internal_measurement_zzjy.zzarg.intValue();
                    for (zzjz com_google_android_gms_internal_measurement_zzjz : com_google_android_gms_internal_measurement_zzjy.zzari) {
                        if (com_google_android_gms_internal_measurement_zzjz.zzark == null) {
                            zzin = zzgg().zzin();
                            str2 = "Event filter with no ID. Audience definition ignored. appId, audienceId";
                            zzbh = zzfg.zzbh(str);
                            obj = com_google_android_gms_internal_measurement_zzjy.zzarg;
                            break;
                        }
                    }
                    for (zzkc com_google_android_gms_internal_measurement_zzkc : com_google_android_gms_internal_measurement_zzjy.zzarh) {
                        if (com_google_android_gms_internal_measurement_zzkc.zzark == null) {
                            zzin = zzgg().zzin();
                            str2 = "Property filter with no ID. Audience definition ignored. appId, audienceId";
                            zzbh = zzfg.zzbh(str);
                            obj = com_google_android_gms_internal_measurement_zzjy.zzarg;
                            zzin.zze(str2, zzbh, obj);
                            break;
                        }
                    }
                    for (zzjz com_google_android_gms_internal_measurement_zzjz2 : com_google_android_gms_internal_measurement_zzjy.zzari) {
                        if (!zza(str, intValue, com_google_android_gms_internal_measurement_zzjz2)) {
                            i2 = 0;
                            break;
                        }
                    }
                    i2 = 1;
                    if (i2 != 0) {
                        for (zzkc com_google_android_gms_internal_measurement_zzkc2 : com_google_android_gms_internal_measurement_zzjy.zzarh) {
                            if (!zza(str, intValue, com_google_android_gms_internal_measurement_zzkc2)) {
                                i2 = 0;
                                break;
                            }
                        }
                    }
                    if (i2 == 0) {
                        zzch();
                        zzab();
                        Preconditions.checkNotEmpty(str);
                        SQLiteDatabase writableDatabase3 = getWritableDatabase();
                        writableDatabase3.delete("property_filters", "app_id=? and audience_id=?", new String[]{str, String.valueOf(intValue)});
                        writableDatabase3.delete("event_filters", "app_id=? and audience_id=?", new String[]{str, String.valueOf(intValue)});
                    }
                }
            }
            List arrayList = new ArrayList();
            int length = com_google_android_gms_internal_measurement_zzjyArr.length;
            while (i < length) {
                arrayList.add(com_google_android_gms_internal_measurement_zzjyArr[i].zzarg);
                i++;
            }
            zza(str, arrayList);
            writableDatabase.setTransactionSuccessful();
        } finally {
            writableDatabase.endTransaction();
        }
    }

    public final boolean zza(zzju com_google_android_gms_internal_measurement_zzju) {
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzju);
        zzab();
        zzch();
        if (zzg(com_google_android_gms_internal_measurement_zzju.zztd, com_google_android_gms_internal_measurement_zzju.name) == null) {
            if (zzjv.zzbv(com_google_android_gms_internal_measurement_zzju.name)) {
                if (zza("select count(1) from user_attributes where app_id=? and name not like '!_%' escape '!'", new String[]{com_google_android_gms_internal_measurement_zzju.zztd}) >= 25) {
                    return false;
                }
            }
            if (zza("select count(1) from user_attributes where app_id=? and origin=? AND name like '!_%' escape '!'", new String[]{com_google_android_gms_internal_measurement_zzju.zztd, com_google_android_gms_internal_measurement_zzju.zzaek}) >= 25) {
                return false;
            }
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("app_id", com_google_android_gms_internal_measurement_zzju.zztd);
        contentValues.put(TtmlNode.ATTR_TTS_ORIGIN, com_google_android_gms_internal_measurement_zzju.zzaek);
        contentValues.put("name", com_google_android_gms_internal_measurement_zzju.name);
        contentValues.put("set_timestamp", Long.valueOf(com_google_android_gms_internal_measurement_zzju.zzaqu));
        zza(contentValues, "value", com_google_android_gms_internal_measurement_zzju.value);
        try {
            if (getWritableDatabase().insertWithOnConflict("user_attributes", null, contentValues, 5) == -1) {
                zzgg().zzil().zzg("Failed to insert/update user property (got -1). appId", zzfg.zzbh(com_google_android_gms_internal_measurement_zzju.zztd));
                return true;
            }
        } catch (SQLiteException e) {
            zzgg().zzil().zze("Error storing user property. appId", zzfg.zzbh(com_google_android_gms_internal_measurement_zzju.zztd), e);
        }
        return true;
    }

    public final boolean zza(zzkl com_google_android_gms_internal_measurement_zzkl, boolean z) {
        Object e;
        zzfi zzil;
        String str;
        zzab();
        zzch();
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzkl);
        Preconditions.checkNotEmpty(com_google_android_gms_internal_measurement_zzkl.zztd);
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzkl.zzath);
        zzhp();
        long currentTimeMillis = zzbt().currentTimeMillis();
        if (com_google_android_gms_internal_measurement_zzkl.zzath.longValue() < currentTimeMillis - zzeh.zzhj() || com_google_android_gms_internal_measurement_zzkl.zzath.longValue() > currentTimeMillis + zzeh.zzhj()) {
            zzgg().zzin().zzd("Storing bundle outside of the max uploading time span. appId, now, timestamp", zzfg.zzbh(com_google_android_gms_internal_measurement_zzkl.zztd), Long.valueOf(currentTimeMillis), com_google_android_gms_internal_measurement_zzkl.zzath);
        }
        try {
            byte[] bArr = new byte[com_google_android_gms_internal_measurement_zzkl.zzwg()];
            zzabb zzb = zzabb.zzb(bArr, 0, bArr.length);
            com_google_android_gms_internal_measurement_zzkl.zza(zzb);
            zzb.zzvy();
            bArr = zzgc().zza(bArr);
            zzgg().zzir().zzg("Saving bundle, size", Integer.valueOf(bArr.length));
            ContentValues contentValues = new ContentValues();
            contentValues.put("app_id", com_google_android_gms_internal_measurement_zzkl.zztd);
            contentValues.put("bundle_end_timestamp", com_google_android_gms_internal_measurement_zzkl.zzath);
            contentValues.put(DataSchemeDataSource.SCHEME_DATA, bArr);
            contentValues.put("has_realtime", Integer.valueOf(z));
            if (com_google_android_gms_internal_measurement_zzkl.zzaue != null) {
                contentValues.put("retry_count", com_google_android_gms_internal_measurement_zzkl.zzaue);
            }
            try {
                if (getWritableDatabase().insert("queue", null, contentValues) != -1) {
                    return true;
                }
                zzgg().zzil().zzg("Failed to insert bundle (got -1). appId", zzfg.zzbh(com_google_android_gms_internal_measurement_zzkl.zztd));
                return false;
            } catch (SQLiteException e2) {
                e = e2;
                zzil = zzgg().zzil();
                str = "Error storing bundle. appId";
                zzil.zze(str, zzfg.zzbh(com_google_android_gms_internal_measurement_zzkl.zztd), e);
                return false;
            }
        } catch (IOException e3) {
            e = e3;
            zzil = zzgg().zzil();
            str = "Data loss. Failed to serialize bundle. appId";
            zzil.zze(str, zzfg.zzbh(com_google_android_gms_internal_measurement_zzkl.zztd), e);
            return false;
        }
    }

    public final boolean zza(String str, Long l, long j, zzki com_google_android_gms_internal_measurement_zzki) {
        zzab();
        zzch();
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzki);
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotNull(l);
        try {
            byte[] bArr = new byte[com_google_android_gms_internal_measurement_zzki.zzwg()];
            zzabb zzb = zzabb.zzb(bArr, 0, bArr.length);
            com_google_android_gms_internal_measurement_zzki.zza(zzb);
            zzb.zzvy();
            zzgg().zzir().zze("Saving complex main event, appId, data size", zzgb().zzbe(str), Integer.valueOf(bArr.length));
            ContentValues contentValues = new ContentValues();
            contentValues.put("app_id", str);
            contentValues.put("event_id", l);
            contentValues.put("children_to_process", Long.valueOf(j));
            contentValues.put("main_event", bArr);
            try {
                if (getWritableDatabase().insertWithOnConflict("main_event_params", null, contentValues, 5) != -1) {
                    return true;
                }
                zzgg().zzil().zzg("Failed to insert complex main event (got -1). appId", zzfg.zzbh(str));
                return false;
            } catch (SQLiteException e) {
                zzgg().zzil().zze("Error storing complex main event. appId", zzfg.zzbh(str), e);
                return false;
            }
        } catch (IOException e2) {
            zzgg().zzil().zzd("Data loss. Failed to serialize event params/data. appId, eventId", zzfg.zzbh(str), l, e2);
            return false;
        }
    }

    public final String zzab(long j) {
        Object e;
        Throwable th;
        zzab();
        zzch();
        Cursor rawQuery;
        try {
            rawQuery = getWritableDatabase().rawQuery("select app_id from apps where app_id in (select distinct app_id from raw_events) and config_fetched_time < ? order by failed_config_fetch_time limit 1;", new String[]{String.valueOf(j)});
            try {
                if (rawQuery.moveToFirst()) {
                    String string = rawQuery.getString(0);
                    if (rawQuery != null) {
                        rawQuery.close();
                    }
                    return string;
                }
                zzgg().zzir().log("No expired configs for apps with pending events");
                if (rawQuery != null) {
                    rawQuery.close();
                }
                return null;
            } catch (SQLiteException e2) {
                e = e2;
                try {
                    zzgg().zzil().zzg("Error selecting expired configs", e);
                    if (rawQuery != null) {
                        rawQuery.close();
                    }
                    return null;
                } catch (Throwable th2) {
                    th = th2;
                    if (rawQuery != null) {
                        rawQuery.close();
                    }
                    throw th;
                }
            }
        } catch (SQLiteException e3) {
            e = e3;
            rawQuery = null;
            zzgg().zzil().zzg("Error selecting expired configs", e);
            if (rawQuery != null) {
                rawQuery.close();
            }
            return null;
        } catch (Throwable th3) {
            th = th3;
            rawQuery = null;
            if (rawQuery != null) {
                rawQuery.close();
            }
            throw th;
        }
    }

    public final zzeb zzax(String str) {
        SQLiteException e;
        Cursor cursor;
        Object obj;
        Throwable th;
        Throwable th2;
        zzei com_google_android_gms_internal_measurement_zzei;
        String str2 = str;
        Preconditions.checkNotEmpty(str);
        zzab();
        zzch();
        Cursor query;
        try {
            r5 = new String[25];
            boolean z = true;
            r5[1] = "gmp_app_id";
            r5[2] = "resettable_device_id_hash";
            r5[3] = "last_bundle_index";
            r5[4] = "last_bundle_start_timestamp";
            r5[5] = "last_bundle_end_timestamp";
            r5[6] = "app_version";
            r5[7] = "app_store";
            r5[8] = "gmp_version";
            r5[9] = "dev_cert_hash";
            r5[10] = "measurement_enabled";
            r5[11] = "day";
            r5[12] = "daily_public_events_count";
            r5[13] = "daily_events_count";
            r5[14] = "daily_conversions_count";
            r5[15] = "config_fetched_time";
            r5[16] = "failed_config_fetch_time";
            r5[17] = "app_version_int";
            r5[18] = "firebase_instance_id";
            r5[19] = "daily_error_events_count";
            r5[20] = "daily_realtime_events_count";
            r5[21] = "health_monitor_sample";
            r5[22] = "android_id";
            r5[23] = "adid_reporting_enabled";
            r5[24] = "ssaid_reporting_enabled";
            int i = 7;
            i = 6;
            i = 5;
            query = getWritableDatabase().query("apps", r5, "app_id=?", new String[]{str2}, null, null, null);
            try {
                if (query.moveToFirst()) {
                    try {
                        boolean z2;
                        zzeb com_google_android_gms_internal_measurement_zzeb = new zzeb(this.zzacr, str2);
                        com_google_android_gms_internal_measurement_zzeb.zzal(query.getString(0));
                        com_google_android_gms_internal_measurement_zzeb.zzam(query.getString(1));
                        com_google_android_gms_internal_measurement_zzeb.zzan(query.getString(2));
                        com_google_android_gms_internal_measurement_zzeb.zzr(query.getLong(3));
                        com_google_android_gms_internal_measurement_zzeb.zzm(query.getLong(4));
                        com_google_android_gms_internal_measurement_zzeb.zzn(query.getLong(i));
                        com_google_android_gms_internal_measurement_zzeb.setAppVersion(query.getString(6));
                        com_google_android_gms_internal_measurement_zzeb.zzap(query.getString(7));
                        com_google_android_gms_internal_measurement_zzeb.zzp(query.getLong(8));
                        com_google_android_gms_internal_measurement_zzeb.zzq(query.getLong(9));
                        if (!query.isNull(10)) {
                            if (query.getInt(10) == 0) {
                                z2 = false;
                                com_google_android_gms_internal_measurement_zzeb.setMeasurementEnabled(z2);
                                com_google_android_gms_internal_measurement_zzeb.zzu(query.getLong(11));
                                com_google_android_gms_internal_measurement_zzeb.zzv(query.getLong(12));
                                com_google_android_gms_internal_measurement_zzeb.zzw(query.getLong(13));
                                com_google_android_gms_internal_measurement_zzeb.zzx(query.getLong(14));
                                com_google_android_gms_internal_measurement_zzeb.zzs(query.getLong(15));
                                com_google_android_gms_internal_measurement_zzeb.zzt(query.getLong(16));
                                com_google_android_gms_internal_measurement_zzeb.zzo(query.isNull(17) ? -2147483648L : (long) query.getInt(17));
                                com_google_android_gms_internal_measurement_zzeb.zzao(query.getString(18));
                                com_google_android_gms_internal_measurement_zzeb.zzz(query.getLong(19));
                                com_google_android_gms_internal_measurement_zzeb.zzy(query.getLong(20));
                                com_google_android_gms_internal_measurement_zzeb.zzaq(query.getString(21));
                                com_google_android_gms_internal_measurement_zzeb.zzaa(query.isNull(22) ? 0 : query.getLong(22));
                                if (!query.isNull(23)) {
                                    if (query.getInt(23) != 0) {
                                        z2 = false;
                                        com_google_android_gms_internal_measurement_zzeb.zzd(z2);
                                        if (!query.isNull(24)) {
                                            if (query.getInt(24) == 0) {
                                                z = false;
                                            }
                                        }
                                        com_google_android_gms_internal_measurement_zzeb.zze(z);
                                        com_google_android_gms_internal_measurement_zzeb.zzgj();
                                        if (query.moveToNext()) {
                                            zzgg().zzil().zzg("Got multiple records for app, expected one. appId", zzfg.zzbh(str));
                                        }
                                        if (query != null) {
                                            query.close();
                                        }
                                        return com_google_android_gms_internal_measurement_zzeb;
                                    }
                                }
                                z2 = true;
                                com_google_android_gms_internal_measurement_zzeb.zzd(z2);
                                if (query.isNull(24)) {
                                    if (query.getInt(24) == 0) {
                                        z = false;
                                    }
                                }
                                com_google_android_gms_internal_measurement_zzeb.zze(z);
                                com_google_android_gms_internal_measurement_zzeb.zzgj();
                                if (query.moveToNext()) {
                                    zzgg().zzil().zzg("Got multiple records for app, expected one. appId", zzfg.zzbh(str));
                                }
                                if (query != null) {
                                    query.close();
                                }
                                return com_google_android_gms_internal_measurement_zzeb;
                            }
                        }
                        z2 = true;
                        com_google_android_gms_internal_measurement_zzeb.setMeasurementEnabled(z2);
                        com_google_android_gms_internal_measurement_zzeb.zzu(query.getLong(11));
                        com_google_android_gms_internal_measurement_zzeb.zzv(query.getLong(12));
                        com_google_android_gms_internal_measurement_zzeb.zzw(query.getLong(13));
                        com_google_android_gms_internal_measurement_zzeb.zzx(query.getLong(14));
                        com_google_android_gms_internal_measurement_zzeb.zzs(query.getLong(15));
                        com_google_android_gms_internal_measurement_zzeb.zzt(query.getLong(16));
                        if (query.isNull(17)) {
                        }
                        com_google_android_gms_internal_measurement_zzeb.zzo(query.isNull(17) ? -2147483648L : (long) query.getInt(17));
                        com_google_android_gms_internal_measurement_zzeb.zzao(query.getString(18));
                        com_google_android_gms_internal_measurement_zzeb.zzz(query.getLong(19));
                        com_google_android_gms_internal_measurement_zzeb.zzy(query.getLong(20));
                        com_google_android_gms_internal_measurement_zzeb.zzaq(query.getString(21));
                        if (query.isNull(22)) {
                        }
                        com_google_android_gms_internal_measurement_zzeb.zzaa(query.isNull(22) ? 0 : query.getLong(22));
                        if (query.isNull(23)) {
                            if (query.getInt(23) != 0) {
                                z2 = false;
                                com_google_android_gms_internal_measurement_zzeb.zzd(z2);
                                if (query.isNull(24)) {
                                    if (query.getInt(24) == 0) {
                                        z = false;
                                    }
                                }
                                com_google_android_gms_internal_measurement_zzeb.zze(z);
                                com_google_android_gms_internal_measurement_zzeb.zzgj();
                                if (query.moveToNext()) {
                                    zzgg().zzil().zzg("Got multiple records for app, expected one. appId", zzfg.zzbh(str));
                                }
                                if (query != null) {
                                    query.close();
                                }
                                return com_google_android_gms_internal_measurement_zzeb;
                            }
                        }
                        z2 = true;
                        com_google_android_gms_internal_measurement_zzeb.zzd(z2);
                        if (query.isNull(24)) {
                            if (query.getInt(24) == 0) {
                                z = false;
                            }
                        }
                        com_google_android_gms_internal_measurement_zzeb.zze(z);
                        com_google_android_gms_internal_measurement_zzeb.zzgj();
                        if (query.moveToNext()) {
                            zzgg().zzil().zzg("Got multiple records for app, expected one. appId", zzfg.zzbh(str));
                        }
                        if (query != null) {
                            query.close();
                        }
                        return com_google_android_gms_internal_measurement_zzeb;
                    } catch (SQLiteException e2) {
                        e = e2;
                        cursor = query;
                        obj = e;
                        try {
                            zzgg().zzil().zze("Error querying app. appId", zzfg.zzbh(str), obj);
                            if (cursor != null) {
                                cursor.close();
                            }
                            return null;
                        } catch (Throwable th22) {
                            th = th22;
                            query = cursor;
                            if (query != null) {
                                query.close();
                            }
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th22 = th3;
                        th = th22;
                        if (query != null) {
                            query.close();
                        }
                        throw th;
                    }
                }
                if (query != null) {
                    query.close();
                }
                return null;
            } catch (SQLiteException e3) {
                e = e3;
                com_google_android_gms_internal_measurement_zzei = this;
                cursor = query;
                obj = e;
                zzgg().zzil().zze("Error querying app. appId", zzfg.zzbh(str), obj);
                if (cursor != null) {
                    cursor.close();
                }
                return null;
            } catch (Throwable th4) {
                th22 = th4;
                com_google_android_gms_internal_measurement_zzei = this;
                th = th22;
                if (query != null) {
                    query.close();
                }
                throw th;
            }
        } catch (SQLiteException e4) {
            com_google_android_gms_internal_measurement_zzei = this;
            obj = e4;
            cursor = null;
            zzgg().zzil().zze("Error querying app. appId", zzfg.zzbh(str), obj);
            if (cursor != null) {
                cursor.close();
            }
            return null;
        } catch (Throwable th222) {
            com_google_android_gms_internal_measurement_zzei = this;
            th = th222;
            query = null;
            if (query != null) {
                query.close();
            }
            throw th;
        }
    }

    public final byte[] zzaz(String str) {
        Cursor query;
        Object e;
        Throwable th;
        Preconditions.checkNotEmpty(str);
        zzab();
        zzch();
        try {
            query = getWritableDatabase().query("apps", new String[]{"remote_config"}, "app_id=?", new String[]{str}, null, null, null);
            try {
                if (query.moveToFirst()) {
                    byte[] blob = query.getBlob(0);
                    if (query.moveToNext()) {
                        zzgg().zzil().zzg("Got multiple records for app config, expected one. appId", zzfg.zzbh(str));
                    }
                    if (query != null) {
                        query.close();
                    }
                    return blob;
                }
                if (query != null) {
                    query.close();
                }
                return null;
            } catch (SQLiteException e2) {
                e = e2;
                try {
                    zzgg().zzil().zze("Error querying remote config. appId", zzfg.zzbh(str), e);
                    if (query != null) {
                        query.close();
                    }
                    return null;
                } catch (Throwable th2) {
                    th = th2;
                    if (query != null) {
                        query.close();
                    }
                    throw th;
                }
            }
        } catch (SQLiteException e3) {
            e = e3;
            query = null;
            zzgg().zzil().zze("Error querying remote config. appId", zzfg.zzbh(str), e);
            if (query != null) {
                query.close();
            }
            return null;
        } catch (Throwable th3) {
            th = th3;
            query = null;
            if (query != null) {
                query.close();
            }
            throw th;
        }
    }

    public final List<Pair<zzkl, Long>> zzb(String str, int i, int i2) {
        Object obj;
        Throwable th;
        int i3 = i2;
        zzab();
        zzch();
        Preconditions.checkArgument(i > 0);
        Preconditions.checkArgument(i3 > 0);
        Preconditions.checkNotEmpty(str);
        Cursor cursor = null;
        Cursor query;
        List<Pair<zzkl, Long>> emptyList;
        try {
            query = getWritableDatabase().query("queue", new String[]{"rowid", DataSchemeDataSource.SCHEME_DATA, "retry_count"}, "app_id=?", new String[]{str}, null, null, "rowid", String.valueOf(i));
            try {
                if (query.moveToFirst()) {
                    List<Pair<zzkl, Long>> arrayList = new ArrayList();
                    int i4 = 0;
                    do {
                        long j = query.getLong(0);
                        try {
                            byte[] zzb = zzgc().zzb(query.getBlob(1));
                            if (!arrayList.isEmpty() && zzb.length + i4 > i3) {
                                break;
                            }
                            zzaba zza = zzaba.zza(zzb, 0, zzb.length);
                            zzabj com_google_android_gms_internal_measurement_zzkl = new zzkl();
                            try {
                                com_google_android_gms_internal_measurement_zzkl.zzb(zza);
                                if (!query.isNull(2)) {
                                    com_google_android_gms_internal_measurement_zzkl.zzaue = Integer.valueOf(query.getInt(2));
                                }
                                i4 += zzb.length;
                                arrayList.add(Pair.create(com_google_android_gms_internal_measurement_zzkl, Long.valueOf(j)));
                            } catch (IOException e) {
                                zzgg().zzil().zze("Failed to merge queued bundle. appId", zzfg.zzbh(str), e);
                            }
                            if (!query.moveToNext()) {
                                break;
                            }
                        } catch (IOException e2) {
                            zzgg().zzil().zze("Failed to unzip queued bundle. appId", zzfg.zzbh(str), e2);
                        }
                    } while (i4 <= i3);
                    if (query != null) {
                        query.close();
                    }
                    return arrayList;
                }
                emptyList = Collections.emptyList();
                if (query != null) {
                    query.close();
                }
                return emptyList;
            } catch (SQLiteException e3) {
                obj = e3;
                cursor = query;
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (SQLiteException e32) {
            obj = e32;
            try {
                zzgg().zzil().zze("Error querying bundles. appId", zzfg.zzbh(str), obj);
                emptyList = Collections.emptyList();
                if (cursor != null) {
                    cursor.close();
                }
                return emptyList;
            } catch (Throwable th22) {
                th = th22;
                query = cursor;
                if (query != null) {
                    query.close();
                }
                throw th;
            }
        }
    }

    final Map<Integer, zzkm> zzba(String str) {
        Object e;
        Throwable th;
        zzch();
        zzab();
        Preconditions.checkNotEmpty(str);
        Cursor query;
        try {
            query = getWritableDatabase().query("audience_filter_values", new String[]{"audience_id", "current_results"}, "app_id=?", new String[]{str}, null, null, null);
            if (query.moveToFirst()) {
                Map<Integer, zzkm> arrayMap = new ArrayMap();
                do {
                    int i = query.getInt(0);
                    byte[] blob = query.getBlob(1);
                    zzaba zza = zzaba.zza(blob, 0, blob.length);
                    zzabj com_google_android_gms_internal_measurement_zzkm = new zzkm();
                    try {
                        com_google_android_gms_internal_measurement_zzkm.zzb(zza);
                    } catch (IOException e2) {
                        zzgg().zzil().zzd("Failed to merge filter results. appId, audienceId, error", zzfg.zzbh(str), Integer.valueOf(i), e2);
                    }
                    try {
                        arrayMap.put(Integer.valueOf(i), com_google_android_gms_internal_measurement_zzkm);
                    } catch (SQLiteException e3) {
                        e = e3;
                    }
                } while (query.moveToNext());
                if (query != null) {
                    query.close();
                }
                return arrayMap;
            }
            if (query != null) {
                query.close();
            }
            return null;
        } catch (SQLiteException e4) {
            e = e4;
            query = null;
            try {
                zzgg().zzil().zze("Database error querying filter results. appId", zzfg.zzbh(str), e);
                if (query != null) {
                    query.close();
                }
                return null;
            } catch (Throwable th2) {
                th = th2;
                if (query != null) {
                    query.close();
                }
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
            query = null;
            if (query != null) {
                query.close();
            }
            throw th;
        }
    }

    final void zzc(List<Long> list) {
        zzab();
        zzch();
        Preconditions.checkNotNull(list);
        Preconditions.checkNotZero(list.size());
        if (zzhv()) {
            String join = TextUtils.join(",", list);
            StringBuilder stringBuilder = new StringBuilder(2 + String.valueOf(join).length());
            stringBuilder.append("(");
            stringBuilder.append(join);
            stringBuilder.append(")");
            join = stringBuilder.toString();
            stringBuilder = new StringBuilder(80 + String.valueOf(join).length());
            stringBuilder.append("SELECT COUNT(1) FROM queue WHERE rowid IN ");
            stringBuilder.append(join);
            stringBuilder.append(" AND retry_count =  2147483647 LIMIT 1");
            if (zza(stringBuilder.toString(), null) > 0) {
                zzgg().zzin().log("The number of upload retries exceeds the limit. Will remain unchanged.");
            }
            try {
                SQLiteDatabase writableDatabase = getWritableDatabase();
                StringBuilder stringBuilder2 = new StringBuilder(127 + String.valueOf(join).length());
                stringBuilder2.append("UPDATE queue SET retry_count = IFNULL(retry_count, 0) + 1 WHERE rowid IN ");
                stringBuilder2.append(join);
                stringBuilder2.append(" AND (retry_count IS NULL OR retry_count < 2147483647)");
                writableDatabase.execSQL(stringBuilder2.toString());
            } catch (SQLiteException e) {
                zzgg().zzil().zzg("Error incrementing retry count. error", e);
            }
        }
    }

    public final zzeq zze(String str, String str2) {
        Object obj;
        SQLiteException e;
        Cursor cursor;
        Throwable th;
        Throwable th2;
        String str3 = str2;
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotEmpty(str2);
        zzab();
        zzch();
        try {
            r3 = new String[7];
            boolean z = false;
            r3[0] = "lifetime_count";
            r3[1] = "current_bundle_count";
            r3[2] = "last_fire_timestamp";
            r3[3] = "last_bundled_timestamp";
            r3[4] = "last_sampled_complex_event_id";
            r3[5] = "last_sampling_rate";
            r3[6] = "last_exempt_from_sampling";
            Cursor query = getWritableDatabase().query("events", r3, "app_id=? and name=?", new String[]{str, str3}, null, null, null);
            try {
                if (query.moveToFirst()) {
                    zzeq com_google_android_gms_internal_measurement_zzeq;
                    Boolean bool;
                    long j = query.getLong(0);
                    long j2 = query.getLong(1);
                    long j3 = query.getLong(2);
                    long j4 = query.isNull(3) ? 0 : query.getLong(3);
                    zzeq valueOf = query.isNull(4) ? null : Long.valueOf(query.getLong(4));
                    if (query.isNull(5)) {
                        com_google_android_gms_internal_measurement_zzeq = null;
                    } else {
                        Object valueOf2 = Long.valueOf(query.getLong(5));
                    }
                    if (query.isNull(6)) {
                        bool = null;
                    } else {
                        try {
                            if (query.getLong(6) == 1) {
                                z = true;
                            }
                            bool = Boolean.valueOf(z);
                        } catch (SQLiteException e2) {
                            obj = e2;
                            cursor = query;
                            try {
                                zzgg().zzil().zzd("Error querying events. appId", zzfg.zzbh(str), zzgb().zzbe(str2), obj);
                                if (cursor != null) {
                                    cursor.close();
                                }
                                return null;
                            } catch (Throwable th3) {
                                th = th3;
                                th2 = th;
                                if (cursor != null) {
                                    cursor.close();
                                }
                                throw th2;
                            }
                        } catch (Throwable th4) {
                            th2 = th4;
                            cursor = query;
                            if (cursor != null) {
                                cursor.close();
                            }
                            throw th2;
                        }
                    }
                    zzeq com_google_android_gms_internal_measurement_zzeq2 = com_google_android_gms_internal_measurement_zzeq2;
                    String str4 = str3;
                    cursor = query;
                    try {
                        com_google_android_gms_internal_measurement_zzeq2 = new zzeq(str, str4, j, j2, j3, j4, valueOf, com_google_android_gms_internal_measurement_zzeq, bool);
                        if (cursor.moveToNext()) {
                            zzgg().zzil().zzg("Got multiple records for event aggregates, expected one. appId", zzfg.zzbh(str));
                        }
                        if (cursor != null) {
                            cursor.close();
                        }
                        return com_google_android_gms_internal_measurement_zzeq2;
                    } catch (SQLiteException e3) {
                        e2 = e3;
                        obj = e2;
                        zzgg().zzil().zzd("Error querying events. appId", zzfg.zzbh(str), zzgb().zzbe(str2), obj);
                        if (cursor != null) {
                            cursor.close();
                        }
                        return null;
                    }
                }
                if (query != null) {
                    query.close();
                }
                return null;
            } catch (SQLiteException e4) {
                e2 = e4;
                cursor = query;
                obj = e2;
                zzgg().zzil().zzd("Error querying events. appId", zzfg.zzbh(str), zzgb().zzbe(str2), obj);
                if (cursor != null) {
                    cursor.close();
                }
                return null;
            } catch (Throwable th5) {
                th4 = th5;
                cursor = query;
                th2 = th4;
                if (cursor != null) {
                    cursor.close();
                }
                throw th2;
            }
        } catch (SQLiteException e22) {
            obj = e22;
            cursor = null;
            zzgg().zzil().zzd("Error querying events. appId", zzfg.zzbh(str), zzgb().zzbe(str2), obj);
            if (cursor != null) {
                cursor.close();
            }
            return null;
        } catch (Throwable th42) {
            th2 = th42;
            cursor = null;
            if (cursor != null) {
                cursor.close();
            }
            throw th2;
        }
    }

    public final zzju zzg(String str, String str2) {
        Cursor query;
        SQLiteException e;
        Object obj;
        Throwable th;
        Throwable th2;
        zzei com_google_android_gms_internal_measurement_zzei;
        String str3 = str2;
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotEmpty(str2);
        zzab();
        zzch();
        try {
            query = getWritableDatabase().query("user_attributes", new String[]{"set_timestamp", "value", TtmlNode.ATTR_TTS_ORIGIN}, "app_id=? and name=?", new String[]{str, str3}, null, null, null);
            try {
                if (query.moveToFirst()) {
                    long j = query.getLong(0);
                    try {
                        String str4 = str;
                        zzju com_google_android_gms_internal_measurement_zzju = new zzju(str4, query.getString(2), str3, j, zza(query, 1));
                        if (query.moveToNext()) {
                            zzgg().zzil().zzg("Got multiple records for user property, expected one. appId", zzfg.zzbh(str));
                        }
                        if (query != null) {
                            query.close();
                        }
                        return com_google_android_gms_internal_measurement_zzju;
                    } catch (SQLiteException e2) {
                        e = e2;
                        obj = e;
                        try {
                            zzgg().zzil().zzd("Error querying user property. appId", zzfg.zzbh(str), zzgb().zzbg(str3), obj);
                            if (query != null) {
                                query.close();
                            }
                            return null;
                        } catch (Throwable th3) {
                            th = th3;
                            th2 = th;
                            if (query != null) {
                                query.close();
                            }
                            throw th2;
                        }
                    }
                }
                if (query != null) {
                    query.close();
                }
                return null;
            } catch (SQLiteException e3) {
                e = e3;
                com_google_android_gms_internal_measurement_zzei = this;
                obj = e;
                zzgg().zzil().zzd("Error querying user property. appId", zzfg.zzbh(str), zzgb().zzbg(str3), obj);
                if (query != null) {
                    query.close();
                }
                return null;
            } catch (Throwable th4) {
                th = th4;
                com_google_android_gms_internal_measurement_zzei = this;
                th2 = th;
                if (query != null) {
                    query.close();
                }
                throw th2;
            }
        } catch (SQLiteException e4) {
            com_google_android_gms_internal_measurement_zzei = this;
            obj = e4;
            query = null;
            zzgg().zzil().zzd("Error querying user property. appId", zzfg.zzbh(str), zzgb().zzbg(str3), obj);
            if (query != null) {
                query.close();
            }
            return null;
        } catch (Throwable th5) {
            com_google_android_gms_internal_measurement_zzei = this;
            th2 = th5;
            query = null;
            if (query != null) {
                query.close();
            }
            throw th2;
        }
    }

    protected final boolean zzhh() {
        return false;
    }

    public final String zzhn() {
        Object e;
        Throwable th;
        Cursor rawQuery;
        try {
            rawQuery = getWritableDatabase().rawQuery("select app_id from queue order by has_realtime desc, rowid asc limit 1;", null);
            try {
                if (rawQuery.moveToFirst()) {
                    String string = rawQuery.getString(0);
                    if (rawQuery != null) {
                        rawQuery.close();
                    }
                    return string;
                }
                if (rawQuery != null) {
                    rawQuery.close();
                }
                return null;
            } catch (SQLiteException e2) {
                e = e2;
                try {
                    zzgg().zzil().zzg("Database error getting next bundle app id", e);
                    if (rawQuery != null) {
                        rawQuery.close();
                    }
                    return null;
                } catch (Throwable th2) {
                    th = th2;
                    if (rawQuery != null) {
                        rawQuery.close();
                    }
                    throw th;
                }
            }
        } catch (SQLiteException e3) {
            e = e3;
            rawQuery = null;
            zzgg().zzil().zzg("Database error getting next bundle app id", e);
            if (rawQuery != null) {
                rawQuery.close();
            }
            return null;
        } catch (Throwable th3) {
            th = th3;
            rawQuery = null;
            if (rawQuery != null) {
                rawQuery.close();
            }
            throw th;
        }
    }

    public final boolean zzho() {
        return zza("select count(1) > 0 from queue where has_realtime = 1", null) != 0;
    }

    final void zzhp() {
        zzab();
        zzch();
        if (zzhv()) {
            long j = zzgh().zzajw.get();
            long elapsedRealtime = zzbt().elapsedRealtime();
            if (Math.abs(elapsedRealtime - j) > ((Long) zzew.zzahj.get()).longValue()) {
                zzgh().zzajw.set(elapsedRealtime);
                zzab();
                zzch();
                if (zzhv()) {
                    int delete = getWritableDatabase().delete("queue", "abs(bundle_end_timestamp - ?) > cast(? as integer)", new String[]{String.valueOf(zzbt().currentTimeMillis()), String.valueOf(zzeh.zzhj())});
                    if (delete > 0) {
                        zzgg().zzir().zzg("Deleted stale rows. rowsDeleted", Integer.valueOf(delete));
                    }
                }
            }
        }
    }

    public final long zzhq() {
        return zza("select max(bundle_end_timestamp) from queue", null, 0);
    }

    public final long zzhr() {
        return zza("select max(timestamp) from raw_events", null, 0);
    }

    public final boolean zzhs() {
        return zza("select count(1) > 0 from raw_events", null) != 0;
    }

    public final boolean zzht() {
        return zza("select count(1) > 0 from raw_events where realtime = 1", null) != 0;
    }

    public final long zzhu() {
        Object obj;
        Throwable th;
        Cursor cursor = null;
        try {
            Cursor rawQuery = getWritableDatabase().rawQuery("select rowid from raw_events order by rowid desc limit 1;", null);
            try {
                if (rawQuery.moveToFirst()) {
                    long j = rawQuery.getLong(0);
                    if (rawQuery != null) {
                        rawQuery.close();
                    }
                    return j;
                }
                if (rawQuery != null) {
                    rawQuery.close();
                }
                return -1;
            } catch (SQLiteException e) {
                Cursor cursor2 = rawQuery;
                obj = e;
                cursor = cursor2;
                try {
                    zzgg().zzil().zzg("Error querying raw events", obj);
                    if (cursor != null) {
                        cursor.close();
                    }
                    return -1;
                } catch (Throwable th2) {
                    th = th2;
                    if (cursor != null) {
                        cursor.close();
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                cursor = rawQuery;
                if (cursor != null) {
                    cursor.close();
                }
                throw th;
            }
        } catch (SQLiteException e2) {
            obj = e2;
            zzgg().zzil().zzg("Error querying raw events", obj);
            if (cursor != null) {
                cursor.close();
            }
            return -1;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    final java.util.Map<java.lang.Integer, java.util.List<com.google.android.gms.internal.measurement.zzjz>> zzj(java.lang.String r13, java.lang.String r14) {
        /*
        r12 = this;
        r12.zzch();
        r12.zzab();
        com.google.android.gms.common.internal.Preconditions.checkNotEmpty(r13);
        com.google.android.gms.common.internal.Preconditions.checkNotEmpty(r14);
        r0 = new android.support.v4.util.ArrayMap;
        r0.<init>();
        r1 = r12.getWritableDatabase();
        r9 = 0;
        r2 = "event_filters";
        r3 = 2;
        r4 = new java.lang.String[r3];	 Catch:{ SQLiteException -> 0x009c, all -> 0x0099 }
        r5 = "audience_id";
        r10 = 0;
        r4[r10] = r5;	 Catch:{ SQLiteException -> 0x009c, all -> 0x0099 }
        r5 = "data";
        r11 = 1;
        r4[r11] = r5;	 Catch:{ SQLiteException -> 0x009c, all -> 0x0099 }
        r5 = "app_id=? AND event_name=?";
        r6 = new java.lang.String[r3];	 Catch:{ SQLiteException -> 0x009c, all -> 0x0099 }
        r6[r10] = r13;	 Catch:{ SQLiteException -> 0x009c, all -> 0x0099 }
        r6[r11] = r14;	 Catch:{ SQLiteException -> 0x009c, all -> 0x0099 }
        r14 = 0;
        r7 = 0;
        r8 = 0;
        r3 = r4;
        r4 = r5;
        r5 = r6;
        r6 = r14;
        r14 = r1.query(r2, r3, r4, r5, r6, r7, r8);	 Catch:{ SQLiteException -> 0x009c, all -> 0x0099 }
        r1 = r14.moveToFirst();	 Catch:{ SQLiteException -> 0x0097 }
        if (r1 != 0) goto L_0x0048;
    L_0x003e:
        r0 = java.util.Collections.emptyMap();	 Catch:{ SQLiteException -> 0x0097 }
        if (r14 == 0) goto L_0x0047;
    L_0x0044:
        r14.close();
    L_0x0047:
        return r0;
    L_0x0048:
        r1 = r14.getBlob(r11);	 Catch:{ SQLiteException -> 0x0097 }
        r2 = r1.length;	 Catch:{ SQLiteException -> 0x0097 }
        r1 = com.google.android.gms.internal.measurement.zzaba.zza(r1, r10, r2);	 Catch:{ SQLiteException -> 0x0097 }
        r2 = new com.google.android.gms.internal.measurement.zzjz;	 Catch:{ SQLiteException -> 0x0097 }
        r2.<init>();	 Catch:{ SQLiteException -> 0x0097 }
        r2.zzb(r1);	 Catch:{ IOException -> 0x0079 }
        r1 = r14.getInt(r10);	 Catch:{ SQLiteException -> 0x0097 }
        r3 = java.lang.Integer.valueOf(r1);	 Catch:{ SQLiteException -> 0x0097 }
        r3 = r0.get(r3);	 Catch:{ SQLiteException -> 0x0097 }
        r3 = (java.util.List) r3;	 Catch:{ SQLiteException -> 0x0097 }
        if (r3 != 0) goto L_0x0075;
    L_0x0069:
        r3 = new java.util.ArrayList;	 Catch:{ SQLiteException -> 0x0097 }
        r3.<init>();	 Catch:{ SQLiteException -> 0x0097 }
        r1 = java.lang.Integer.valueOf(r1);	 Catch:{ SQLiteException -> 0x0097 }
        r0.put(r1, r3);	 Catch:{ SQLiteException -> 0x0097 }
    L_0x0075:
        r3.add(r2);	 Catch:{ SQLiteException -> 0x0097 }
        goto L_0x008b;
    L_0x0079:
        r1 = move-exception;
        r2 = r12.zzgg();	 Catch:{ SQLiteException -> 0x0097 }
        r2 = r2.zzil();	 Catch:{ SQLiteException -> 0x0097 }
        r3 = "Failed to merge filter. appId";
        r4 = com.google.android.gms.internal.measurement.zzfg.zzbh(r13);	 Catch:{ SQLiteException -> 0x0097 }
        r2.zze(r3, r4, r1);	 Catch:{ SQLiteException -> 0x0097 }
    L_0x008b:
        r1 = r14.moveToNext();	 Catch:{ SQLiteException -> 0x0097 }
        if (r1 != 0) goto L_0x0048;
    L_0x0091:
        if (r14 == 0) goto L_0x0096;
    L_0x0093:
        r14.close();
    L_0x0096:
        return r0;
    L_0x0097:
        r0 = move-exception;
        goto L_0x009e;
    L_0x0099:
        r13 = move-exception;
        r14 = r9;
        goto L_0x00b6;
    L_0x009c:
        r0 = move-exception;
        r14 = r9;
    L_0x009e:
        r1 = r12.zzgg();	 Catch:{ all -> 0x00b5 }
        r1 = r1.zzil();	 Catch:{ all -> 0x00b5 }
        r2 = "Database error querying filters. appId";
        r13 = com.google.android.gms.internal.measurement.zzfg.zzbh(r13);	 Catch:{ all -> 0x00b5 }
        r1.zze(r2, r13, r0);	 Catch:{ all -> 0x00b5 }
        if (r14 == 0) goto L_0x00b4;
    L_0x00b1:
        r14.close();
    L_0x00b4:
        return r9;
    L_0x00b5:
        r13 = move-exception;
    L_0x00b6:
        if (r14 == 0) goto L_0x00bb;
    L_0x00b8:
        r14.close();
    L_0x00bb:
        throw r13;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzei.zzj(java.lang.String, java.lang.String):java.util.Map<java.lang.Integer, java.util.List<com.google.android.gms.internal.measurement.zzjz>>");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    final java.util.Map<java.lang.Integer, java.util.List<com.google.android.gms.internal.measurement.zzkc>> zzk(java.lang.String r13, java.lang.String r14) {
        /*
        r12 = this;
        r12.zzch();
        r12.zzab();
        com.google.android.gms.common.internal.Preconditions.checkNotEmpty(r13);
        com.google.android.gms.common.internal.Preconditions.checkNotEmpty(r14);
        r0 = new android.support.v4.util.ArrayMap;
        r0.<init>();
        r1 = r12.getWritableDatabase();
        r9 = 0;
        r2 = "property_filters";
        r3 = 2;
        r4 = new java.lang.String[r3];	 Catch:{ SQLiteException -> 0x009c, all -> 0x0099 }
        r5 = "audience_id";
        r10 = 0;
        r4[r10] = r5;	 Catch:{ SQLiteException -> 0x009c, all -> 0x0099 }
        r5 = "data";
        r11 = 1;
        r4[r11] = r5;	 Catch:{ SQLiteException -> 0x009c, all -> 0x0099 }
        r5 = "app_id=? AND property_name=?";
        r6 = new java.lang.String[r3];	 Catch:{ SQLiteException -> 0x009c, all -> 0x0099 }
        r6[r10] = r13;	 Catch:{ SQLiteException -> 0x009c, all -> 0x0099 }
        r6[r11] = r14;	 Catch:{ SQLiteException -> 0x009c, all -> 0x0099 }
        r14 = 0;
        r7 = 0;
        r8 = 0;
        r3 = r4;
        r4 = r5;
        r5 = r6;
        r6 = r14;
        r14 = r1.query(r2, r3, r4, r5, r6, r7, r8);	 Catch:{ SQLiteException -> 0x009c, all -> 0x0099 }
        r1 = r14.moveToFirst();	 Catch:{ SQLiteException -> 0x0097 }
        if (r1 != 0) goto L_0x0048;
    L_0x003e:
        r0 = java.util.Collections.emptyMap();	 Catch:{ SQLiteException -> 0x0097 }
        if (r14 == 0) goto L_0x0047;
    L_0x0044:
        r14.close();
    L_0x0047:
        return r0;
    L_0x0048:
        r1 = r14.getBlob(r11);	 Catch:{ SQLiteException -> 0x0097 }
        r2 = r1.length;	 Catch:{ SQLiteException -> 0x0097 }
        r1 = com.google.android.gms.internal.measurement.zzaba.zza(r1, r10, r2);	 Catch:{ SQLiteException -> 0x0097 }
        r2 = new com.google.android.gms.internal.measurement.zzkc;	 Catch:{ SQLiteException -> 0x0097 }
        r2.<init>();	 Catch:{ SQLiteException -> 0x0097 }
        r2.zzb(r1);	 Catch:{ IOException -> 0x0079 }
        r1 = r14.getInt(r10);	 Catch:{ SQLiteException -> 0x0097 }
        r3 = java.lang.Integer.valueOf(r1);	 Catch:{ SQLiteException -> 0x0097 }
        r3 = r0.get(r3);	 Catch:{ SQLiteException -> 0x0097 }
        r3 = (java.util.List) r3;	 Catch:{ SQLiteException -> 0x0097 }
        if (r3 != 0) goto L_0x0075;
    L_0x0069:
        r3 = new java.util.ArrayList;	 Catch:{ SQLiteException -> 0x0097 }
        r3.<init>();	 Catch:{ SQLiteException -> 0x0097 }
        r1 = java.lang.Integer.valueOf(r1);	 Catch:{ SQLiteException -> 0x0097 }
        r0.put(r1, r3);	 Catch:{ SQLiteException -> 0x0097 }
    L_0x0075:
        r3.add(r2);	 Catch:{ SQLiteException -> 0x0097 }
        goto L_0x008b;
    L_0x0079:
        r1 = move-exception;
        r2 = r12.zzgg();	 Catch:{ SQLiteException -> 0x0097 }
        r2 = r2.zzil();	 Catch:{ SQLiteException -> 0x0097 }
        r3 = "Failed to merge filter";
        r4 = com.google.android.gms.internal.measurement.zzfg.zzbh(r13);	 Catch:{ SQLiteException -> 0x0097 }
        r2.zze(r3, r4, r1);	 Catch:{ SQLiteException -> 0x0097 }
    L_0x008b:
        r1 = r14.moveToNext();	 Catch:{ SQLiteException -> 0x0097 }
        if (r1 != 0) goto L_0x0048;
    L_0x0091:
        if (r14 == 0) goto L_0x0096;
    L_0x0093:
        r14.close();
    L_0x0096:
        return r0;
    L_0x0097:
        r0 = move-exception;
        goto L_0x009e;
    L_0x0099:
        r13 = move-exception;
        r14 = r9;
        goto L_0x00b6;
    L_0x009c:
        r0 = move-exception;
        r14 = r9;
    L_0x009e:
        r1 = r12.zzgg();	 Catch:{ all -> 0x00b5 }
        r1 = r1.zzil();	 Catch:{ all -> 0x00b5 }
        r2 = "Database error querying filters. appId";
        r13 = com.google.android.gms.internal.measurement.zzfg.zzbh(r13);	 Catch:{ all -> 0x00b5 }
        r1.zze(r2, r13, r0);	 Catch:{ all -> 0x00b5 }
        if (r14 == 0) goto L_0x00b4;
    L_0x00b1:
        r14.close();
    L_0x00b4:
        return r9;
    L_0x00b5:
        r13 = move-exception;
    L_0x00b6:
        if (r14 == 0) goto L_0x00bb;
    L_0x00b8:
        r14.close();
    L_0x00bb:
        throw r13;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzei.zzk(java.lang.String, java.lang.String):java.util.Map<java.lang.Integer, java.util.List<com.google.android.gms.internal.measurement.zzkc>>");
    }
}
