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
        Cursor cursor = null;
        try {
            cursor = getWritableDatabase().rawQuery(str, strArr);
            if (cursor.moveToFirst()) {
                long j = cursor.getLong(0);
                if (cursor != null) {
                    cursor.close();
                }
                return j;
            }
            throw new SQLiteException("Database returned empty set");
        } catch (SQLiteException e) {
            zzgg().zzil().zze("Database error", str, e);
            throw e;
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private final long zza(String str, String[] strArr, long j) {
        Cursor cursor = null;
        try {
            cursor = getWritableDatabase().rawQuery(str, strArr);
            if (cursor.moveToFirst()) {
                j = cursor.getLong(0);
                if (cursor != null) {
                    cursor.close();
                }
            } else if (cursor != null) {
                cursor.close();
            }
            return j;
        } catch (SQLiteException e) {
            zzgg().zzil().zze("Database error", str, e);
            throw e;
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
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
        int i = 0;
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
        int i2 = 0;
        while (i2 < length) {
            String str4 = split[i2];
            if (zzb.remove(str4)) {
                i2++;
            } else {
                throw new SQLiteException(new StringBuilder((String.valueOf(str).length() + 35) + String.valueOf(str4).length()).append("Table ").append(str).append(" is missing required column: ").append(str4).toString());
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
        Object e;
        Throwable th;
        Cursor cursor = null;
        if (com_google_android_gms_internal_measurement_zzfg == null) {
            throw new IllegalArgumentException("Monitor must not be null");
        }
        Cursor query;
        try {
            SQLiteDatabase sQLiteDatabase2 = sQLiteDatabase;
            query = sQLiteDatabase2.query("SQLITE_MASTER", new String[]{"name"}, "name=?", new String[]{str}, null, null, null);
            try {
                boolean moveToFirst = query.moveToFirst();
                if (query == null) {
                    return moveToFirst;
                }
                query.close();
                return moveToFirst;
            } catch (SQLiteException e2) {
                e = e2;
                try {
                    com_google_android_gms_internal_measurement_zzfg.zzin().zze("Error querying for table", str, e);
                    if (query != null) {
                        query.close();
                    }
                    return false;
                } catch (Throwable th2) {
                    th = th2;
                    cursor = query;
                    if (cursor != null) {
                        cursor.close();
                    }
                    throw th;
                }
            }
        } catch (SQLiteException e3) {
            e = e3;
            query = null;
            com_google_android_gms_internal_measurement_zzfg.zzin().zze("Error querying for table", str, e);
            if (query != null) {
                query.close();
            }
            return false;
        } catch (Throwable th3) {
            th = th3;
            if (cursor != null) {
                cursor.close();
            }
            throw th;
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
            join = new StringBuilder(String.valueOf(join).length() + 2).append("(").append(join).append(")").toString();
            return writableDatabase.delete("audience_filter_values", new StringBuilder(String.valueOf(join).length() + 140).append("audience_id in (select audience_id from audience_filter_values where app_id=? and audience_id not in ").append(join).append(" order by rowid desc limit -1 offset ?)").toString(), new String[]{str, Integer.toString(r5)}) > 0;
        } catch (SQLiteException e) {
            zzgg().zzil().zze("Database error querying filters. appId", zzfg.zzbh(str), e);
            return false;
        }
    }

    private static Set<String> zzb(SQLiteDatabase sQLiteDatabase, String str) {
        Set<String> hashSet = new HashSet();
        Cursor rawQuery = sQLiteDatabase.rawQuery(new StringBuilder(String.valueOf(str).length() + 22).append("SELECT * FROM ").append(str).append(" LIMIT 0").toString(), null);
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
        Pair<zzki, Long> pair = null;
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
                        pair = Pair.create(com_google_android_gms_internal_measurement_zzki, valueOf);
                        if (rawQuery != null) {
                            rawQuery.close();
                        }
                    } catch (IOException e2) {
                        zzgg().zzil().zzd("Failed to merge main event. appId, eventId", zzfg.zzbh(str), l, e2);
                        if (rawQuery != null) {
                            rawQuery.close();
                        }
                    }
                } else {
                    zzgg().zzir().log("Main event not found");
                    if (rawQuery != null) {
                        rawQuery.close();
                    }
                }
            } catch (SQLiteException e3) {
                e = e3;
                try {
                    zzgg().zzil().zzg("Error selecting main event", e);
                    if (rawQuery != null) {
                        rawQuery.close();
                    }
                    return pair;
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
            rawQuery = pair;
            zzgg().zzil().zzg("Error selecting main event", e);
            if (rawQuery != null) {
                rawQuery.close();
            }
            return pair;
        } catch (Throwable th3) {
            th = th3;
            rawQuery = pair;
            if (rawQuery != null) {
                rawQuery.close();
            }
            throw th;
        }
        return pair;
    }

    public final zzej zza(long j, String str, boolean z, boolean z2, boolean z3, boolean z4, boolean z5) {
        Object e;
        Throwable th;
        Preconditions.checkNotEmpty(str);
        zzab();
        zzch();
        String[] strArr = new String[]{str};
        zzej com_google_android_gms_internal_measurement_zzej = new zzej();
        Cursor query;
        try {
            SQLiteDatabase writableDatabase = getWritableDatabase();
            query = writableDatabase.query("apps", new String[]{"day", "daily_events_count", "daily_public_events_count", "daily_conversions_count", "daily_error_events_count", "daily_realtime_events_count"}, "app_id=?", new String[]{str}, null, null, null);
            try {
                if (query.moveToFirst()) {
                    if (query.getLong(0) == j) {
                        com_google_android_gms_internal_measurement_zzej.zzafd = query.getLong(1);
                        com_google_android_gms_internal_measurement_zzej.zzafc = query.getLong(2);
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
            } catch (SQLiteException e2) {
                e = e2;
                try {
                    zzgg().zzil().zze("Error updating daily counts. appId", zzfg.zzbh(str), e);
                    if (query != null) {
                        query.close();
                    }
                    return com_google_android_gms_internal_measurement_zzej;
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
            zzgg().zzil().zze("Error updating daily counts. appId", zzfg.zzbh(str), e);
            if (query != null) {
                query.close();
            }
            return com_google_android_gms_internal_measurement_zzej;
        } catch (Throwable th3) {
            th = th3;
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
        Long l = null;
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
        if (com_google_android_gms_internal_measurement_zzeq.zzafv != null && com_google_android_gms_internal_measurement_zzeq.zzafv.booleanValue()) {
            l = Long.valueOf(1);
        }
        contentValues.put("last_exempt_from_sampling", l);
        try {
            if (getWritableDatabase().insertWithOnConflict("events", null, contentValues, 5) == -1) {
                zzgg().zzil().zzg("Failed to insert/update event aggregates (got -1). appId", zzfg.zzbh(com_google_android_gms_internal_measurement_zzeq.zztd));
            }
        } catch (SQLiteException e) {
            zzgg().zzil().zze("Error storing event aggregates. appId", zzfg.zzbh(com_google_android_gms_internal_measurement_zzeq.zztd), e);
        }
    }

    final void zza(String str, zzjy[] com_google_android_gms_internal_measurement_zzjyArr) {
        int i = 0;
        zzch();
        zzab();
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzjyArr);
        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.beginTransaction();
        try {
            int i2;
            zzch();
            zzab();
            Preconditions.checkNotEmpty(str);
            SQLiteDatabase writableDatabase2 = getWritableDatabase();
            writableDatabase2.delete("property_filters", "app_id=?", new String[]{str});
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
                    int intValue = com_google_android_gms_internal_measurement_zzjy.zzarg.intValue();
                    for (zzjz com_google_android_gms_internal_measurement_zzjz : com_google_android_gms_internal_measurement_zzjy.zzari) {
                        if (com_google_android_gms_internal_measurement_zzjz.zzark == null) {
                            zzgg().zzin().zze("Event filter with no ID. Audience definition ignored. appId, audienceId", zzfg.zzbh(str), com_google_android_gms_internal_measurement_zzjy.zzarg);
                            break;
                        }
                    }
                    for (zzkc com_google_android_gms_internal_measurement_zzkc : com_google_android_gms_internal_measurement_zzjy.zzarh) {
                        if (com_google_android_gms_internal_measurement_zzkc.zzark == null) {
                            zzgg().zzin().zze("Property filter with no ID. Audience definition ignored. appId, audienceId", zzfg.zzbh(str), com_google_android_gms_internal_measurement_zzjy.zzarg);
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
            i2 = com_google_android_gms_internal_measurement_zzjyArr.length;
            while (i < i2) {
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
            }
        } catch (SQLiteException e) {
            zzgg().zzil().zze("Error storing user property. appId", zzfg.zzbh(com_google_android_gms_internal_measurement_zzju.zztd), e);
        }
        return true;
    }

    public final boolean zza(zzkl com_google_android_gms_internal_measurement_zzkl, boolean z) {
        zzab();
        zzch();
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzkl);
        Preconditions.checkNotEmpty(com_google_android_gms_internal_measurement_zzkl.zztd);
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzkl.zzath);
        zzhp();
        long currentTimeMillis = zzbt().currentTimeMillis();
        if (com_google_android_gms_internal_measurement_zzkl.zzath.longValue() < currentTimeMillis - zzeh.zzhj() || com_google_android_gms_internal_measurement_zzkl.zzath.longValue() > zzeh.zzhj() + currentTimeMillis) {
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
            contentValues.put("has_realtime", Integer.valueOf(z ? 1 : 0));
            if (com_google_android_gms_internal_measurement_zzkl.zzaue != null) {
                contentValues.put("retry_count", com_google_android_gms_internal_measurement_zzkl.zzaue);
            }
            try {
                if (getWritableDatabase().insert("queue", null, contentValues) != -1) {
                    return true;
                }
                zzgg().zzil().zzg("Failed to insert bundle (got -1). appId", zzfg.zzbh(com_google_android_gms_internal_measurement_zzkl.zztd));
                return false;
            } catch (SQLiteException e) {
                zzgg().zzil().zze("Error storing bundle. appId", zzfg.zzbh(com_google_android_gms_internal_measurement_zzkl.zztd), e);
                return false;
            }
        } catch (IOException e2) {
            zzgg().zzil().zze("Data loss. Failed to serialize bundle. appId", zzfg.zzbh(com_google_android_gms_internal_measurement_zzkl.zztd), e2);
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
        Cursor rawQuery;
        Object e;
        Throwable th;
        String str = null;
        zzab();
        zzch();
        try {
            rawQuery = getWritableDatabase().rawQuery("select app_id from apps where app_id in (select distinct app_id from raw_events) and config_fetched_time < ? order by failed_config_fetch_time limit 1;", new String[]{String.valueOf(j)});
            try {
                if (rawQuery.moveToFirst()) {
                    str = rawQuery.getString(0);
                    if (rawQuery != null) {
                        rawQuery.close();
                    }
                } else {
                    zzgg().zzir().log("No expired configs for apps with pending events");
                    if (rawQuery != null) {
                        rawQuery.close();
                    }
                }
            } catch (SQLiteException e2) {
                e = e2;
                try {
                    zzgg().zzil().zzg("Error selecting expired configs", e);
                    if (rawQuery != null) {
                        rawQuery.close();
                    }
                    return str;
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
            rawQuery = str;
            zzgg().zzil().zzg("Error selecting expired configs", e);
            if (rawQuery != null) {
                rawQuery.close();
            }
            return str;
        } catch (Throwable th3) {
            th = th3;
            rawQuery = str;
            if (rawQuery != null) {
                rawQuery.close();
            }
            throw th;
        }
        return str;
    }

    public final zzeb zzax(String str) {
        Cursor query;
        Object e;
        Throwable th;
        Preconditions.checkNotEmpty(str);
        zzab();
        zzch();
        try {
            query = getWritableDatabase().query("apps", new String[]{"app_instance_id", "gmp_app_id", "resettable_device_id_hash", "last_bundle_index", "last_bundle_start_timestamp", "last_bundle_end_timestamp", "app_version", "app_store", "gmp_version", "dev_cert_hash", "measurement_enabled", "day", "daily_public_events_count", "daily_events_count", "daily_conversions_count", "config_fetched_time", "failed_config_fetch_time", "app_version_int", "firebase_instance_id", "daily_error_events_count", "daily_realtime_events_count", "health_monitor_sample", "android_id", "adid_reporting_enabled", "ssaid_reporting_enabled"}, "app_id=?", new String[]{str}, null, null, null);
            try {
                if (query.moveToFirst()) {
                    zzeb com_google_android_gms_internal_measurement_zzeb = new zzeb(this.zzacr, str);
                    com_google_android_gms_internal_measurement_zzeb.zzal(query.getString(0));
                    com_google_android_gms_internal_measurement_zzeb.zzam(query.getString(1));
                    com_google_android_gms_internal_measurement_zzeb.zzan(query.getString(2));
                    com_google_android_gms_internal_measurement_zzeb.zzr(query.getLong(3));
                    com_google_android_gms_internal_measurement_zzeb.zzm(query.getLong(4));
                    com_google_android_gms_internal_measurement_zzeb.zzn(query.getLong(5));
                    com_google_android_gms_internal_measurement_zzeb.setAppVersion(query.getString(6));
                    com_google_android_gms_internal_measurement_zzeb.zzap(query.getString(7));
                    com_google_android_gms_internal_measurement_zzeb.zzp(query.getLong(8));
                    com_google_android_gms_internal_measurement_zzeb.zzq(query.getLong(9));
                    boolean z = query.isNull(10) || query.getInt(10) != 0;
                    com_google_android_gms_internal_measurement_zzeb.setMeasurementEnabled(z);
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
                    z = query.isNull(23) || query.getInt(23) != 0;
                    com_google_android_gms_internal_measurement_zzeb.zzd(z);
                    z = query.isNull(24) || query.getInt(24) != 0;
                    com_google_android_gms_internal_measurement_zzeb.zze(z);
                    com_google_android_gms_internal_measurement_zzeb.zzgj();
                    if (query.moveToNext()) {
                        zzgg().zzil().zzg("Got multiple records for app, expected one. appId", zzfg.zzbh(str));
                    }
                    if (query == null) {
                        return com_google_android_gms_internal_measurement_zzeb;
                    }
                    query.close();
                    return com_google_android_gms_internal_measurement_zzeb;
                }
                if (query != null) {
                    query.close();
                }
                return null;
            } catch (SQLiteException e2) {
                e = e2;
                try {
                    zzgg().zzil().zze("Error querying app. appId", zzfg.zzbh(str), e);
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
            zzgg().zzil().zze("Error querying app. appId", zzfg.zzbh(str), e);
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
                    if (query == null) {
                        return blob;
                    }
                    query.close();
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
        List<Pair<zzkl, Long>> arrayList;
        Object e;
        Cursor cursor;
        Throwable th;
        boolean z = true;
        zzab();
        zzch();
        Preconditions.checkArgument(i > 0);
        if (i2 <= 0) {
            z = false;
        }
        Preconditions.checkArgument(z);
        Preconditions.checkNotEmpty(str);
        Cursor query;
        try {
            query = getWritableDatabase().query("queue", new String[]{"rowid", DataSchemeDataSource.SCHEME_DATA, "retry_count"}, "app_id=?", new String[]{str}, null, null, "rowid", String.valueOf(i));
            try {
                if (query.moveToFirst()) {
                    arrayList = new ArrayList();
                    int i3 = 0;
                    while (true) {
                        long j = query.getLong(0);
                        int length;
                        try {
                            byte[] zzb = zzgc().zzb(query.getBlob(1));
                            if (!arrayList.isEmpty() && zzb.length + i3 > i2) {
                                break;
                            }
                            zzaba zza = zzaba.zza(zzb, 0, zzb.length);
                            zzabj com_google_android_gms_internal_measurement_zzkl = new zzkl();
                            try {
                                com_google_android_gms_internal_measurement_zzkl.zzb(zza);
                                if (!query.isNull(2)) {
                                    com_google_android_gms_internal_measurement_zzkl.zzaue = Integer.valueOf(query.getInt(2));
                                }
                                length = zzb.length + i3;
                                arrayList.add(Pair.create(com_google_android_gms_internal_measurement_zzkl, Long.valueOf(j)));
                            } catch (IOException e2) {
                                zzgg().zzil().zze("Failed to merge queued bundle. appId", zzfg.zzbh(str), e2);
                                length = i3;
                            }
                            if (!query.moveToNext() || length > i2) {
                                break;
                            }
                            i3 = length;
                        } catch (IOException e22) {
                            zzgg().zzil().zze("Failed to unzip queued bundle. appId", zzfg.zzbh(str), e22);
                            length = i3;
                        }
                    }
                    if (query != null) {
                        query.close();
                    }
                } else {
                    arrayList = Collections.emptyList();
                    if (query != null) {
                        query.close();
                    }
                }
            } catch (SQLiteException e3) {
                e = e3;
                cursor = query;
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (SQLiteException e4) {
            e = e4;
            cursor = null;
            try {
                zzgg().zzil().zze("Error querying bundles. appId", zzfg.zzbh(str), e);
                arrayList = Collections.emptyList();
                if (cursor != null) {
                    cursor.close();
                }
                return arrayList;
            } catch (Throwable th3) {
                th = th3;
                query = cursor;
                if (query != null) {
                    query.close();
                }
                throw th;
            }
        } catch (Throwable th4) {
            th = th4;
            query = null;
            if (query != null) {
                query.close();
            }
            throw th;
        }
        return arrayList;
    }

    final Map<Integer, zzkm> zzba(String str) {
        Cursor query;
        Object e;
        Throwable th;
        zzch();
        zzab();
        Preconditions.checkNotEmpty(str);
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
                        arrayMap.put(Integer.valueOf(i), com_google_android_gms_internal_measurement_zzkm);
                    } catch (IOException e2) {
                        try {
                            zzgg().zzil().zzd("Failed to merge filter results. appId, audienceId, error", zzfg.zzbh(str), Integer.valueOf(i), e2);
                        } catch (SQLiteException e3) {
                            e = e3;
                        }
                    }
                } while (query.moveToNext());
                if (query == null) {
                    return arrayMap;
                }
                query.close();
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
            join = new StringBuilder(String.valueOf(join).length() + 2).append("(").append(join).append(")").toString();
            if (zza(new StringBuilder(String.valueOf(join).length() + 80).append("SELECT COUNT(1) FROM queue WHERE rowid IN ").append(join).append(" AND retry_count =  2147483647 LIMIT 1").toString(), null) > 0) {
                zzgg().zzin().log("The number of upload retries exceeds the limit. Will remain unchanged.");
            }
            try {
                getWritableDatabase().execSQL(new StringBuilder(String.valueOf(join).length() + 127).append("UPDATE queue SET retry_count = IFNULL(retry_count, 0) + 1 WHERE rowid IN ").append(join).append(" AND (retry_count IS NULL OR retry_count < 2147483647)").toString());
            } catch (SQLiteException e) {
                zzgg().zzil().zzg("Error incrementing retry count. error", e);
            }
        }
    }

    public final zzeq zze(String str, String str2) {
        Cursor query;
        Object e;
        Cursor cursor;
        Throwable th;
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotEmpty(str2);
        zzab();
        zzch();
        try {
            query = getWritableDatabase().query("events", new String[]{"lifetime_count", "current_bundle_count", "last_fire_timestamp", "last_bundled_timestamp", "last_sampled_complex_event_id", "last_sampling_rate", "last_exempt_from_sampling"}, "app_id=? and name=?", new String[]{str, str2}, null, null, null);
            try {
                if (query.moveToFirst()) {
                    long j = query.getLong(0);
                    long j2 = query.getLong(1);
                    long j3 = query.getLong(2);
                    long j4 = query.isNull(3) ? 0 : query.getLong(3);
                    Long valueOf = query.isNull(4) ? null : Long.valueOf(query.getLong(4));
                    Long valueOf2 = query.isNull(5) ? null : Long.valueOf(query.getLong(5));
                    Boolean bool = null;
                    if (!query.isNull(6)) {
                        bool = Boolean.valueOf(query.getLong(6) == 1);
                    }
                    zzeq com_google_android_gms_internal_measurement_zzeq = new zzeq(str, str2, j, j2, j3, j4, valueOf, valueOf2, bool);
                    if (query.moveToNext()) {
                        zzgg().zzil().zzg("Got multiple records for event aggregates, expected one. appId", zzfg.zzbh(str));
                    }
                    if (query == null) {
                        return com_google_android_gms_internal_measurement_zzeq;
                    }
                    query.close();
                    return com_google_android_gms_internal_measurement_zzeq;
                }
                if (query != null) {
                    query.close();
                }
                return null;
            } catch (SQLiteException e2) {
                e = e2;
                cursor = query;
                try {
                    zzgg().zzil().zzd("Error querying events. appId", zzfg.zzbh(str), zzgb().zzbe(str2), e);
                    if (cursor != null) {
                        cursor.close();
                    }
                    return null;
                } catch (Throwable th2) {
                    th = th2;
                    query = cursor;
                    if (query != null) {
                        query.close();
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                if (query != null) {
                    query.close();
                }
                throw th;
            }
        } catch (SQLiteException e3) {
            e = e3;
            cursor = null;
            zzgg().zzil().zzd("Error querying events. appId", zzfg.zzbh(str), zzgb().zzbe(str2), e);
            if (cursor != null) {
                cursor.close();
            }
            return null;
        } catch (Throwable th4) {
            th = th4;
            query = null;
            if (query != null) {
                query.close();
            }
            throw th;
        }
    }

    public final zzju zzg(String str, String str2) {
        Object e;
        Cursor cursor;
        Throwable th;
        Cursor cursor2 = null;
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotEmpty(str2);
        zzab();
        zzch();
        try {
            Cursor query = getWritableDatabase().query("user_attributes", new String[]{"set_timestamp", "value", TtmlNode.ATTR_TTS_ORIGIN}, "app_id=? and name=?", new String[]{str, str2}, null, null, null);
            try {
                if (query.moveToFirst()) {
                    String str3 = str;
                    zzju com_google_android_gms_internal_measurement_zzju = new zzju(str3, query.getString(2), str2, query.getLong(0), zza(query, 1));
                    if (query.moveToNext()) {
                        zzgg().zzil().zzg("Got multiple records for user property, expected one. appId", zzfg.zzbh(str));
                    }
                    if (query == null) {
                        return com_google_android_gms_internal_measurement_zzju;
                    }
                    query.close();
                    return com_google_android_gms_internal_measurement_zzju;
                }
                if (query != null) {
                    query.close();
                }
                return null;
            } catch (SQLiteException e2) {
                e = e2;
                cursor = query;
                try {
                    zzgg().zzil().zzd("Error querying user property. appId", zzfg.zzbh(str), zzgb().zzbg(str2), e);
                    if (cursor != null) {
                        cursor.close();
                    }
                    return null;
                } catch (Throwable th2) {
                    th = th2;
                    cursor2 = cursor;
                    if (cursor2 != null) {
                        cursor2.close();
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                cursor2 = query;
                if (cursor2 != null) {
                    cursor2.close();
                }
                throw th;
            }
        } catch (SQLiteException e3) {
            e = e3;
            cursor = null;
            zzgg().zzil().zzd("Error querying user property. appId", zzfg.zzbh(str), zzgb().zzbg(str2), e);
            if (cursor != null) {
                cursor.close();
            }
            return null;
        } catch (Throwable th4) {
            th = th4;
            if (cursor2 != null) {
                cursor2.close();
            }
            throw th;
        }
    }

    protected final boolean zzhh() {
        return false;
    }

    public final String zzhn() {
        Cursor rawQuery;
        Object e;
        Throwable th;
        String str = null;
        try {
            rawQuery = getWritableDatabase().rawQuery("select app_id from queue order by has_realtime desc, rowid asc limit 1;", null);
            try {
                if (rawQuery.moveToFirst()) {
                    str = rawQuery.getString(0);
                    if (rawQuery != null) {
                        rawQuery.close();
                    }
                } else if (rawQuery != null) {
                    rawQuery.close();
                }
            } catch (SQLiteException e2) {
                e = e2;
                try {
                    zzgg().zzil().zzg("Database error getting next bundle app id", e);
                    if (rawQuery != null) {
                        rawQuery.close();
                    }
                    return str;
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
            return str;
        } catch (Throwable th3) {
            th = th3;
            rawQuery = null;
            if (rawQuery != null) {
                rawQuery.close();
            }
            throw th;
        }
        return str;
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
        long j = -1;
        Cursor cursor = null;
        try {
            cursor = getWritableDatabase().rawQuery("select rowid from raw_events order by rowid desc limit 1;", null);
            if (cursor.moveToFirst()) {
                j = cursor.getLong(0);
                if (cursor != null) {
                    cursor.close();
                }
            } else if (cursor != null) {
                cursor.close();
            }
        } catch (SQLiteException e) {
            zzgg().zzil().zzg("Error querying raw events", e);
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return j;
    }

    final Map<Integer, List<zzjz>> zzj(String str, String str2) {
        Object e;
        Throwable th;
        zzch();
        zzab();
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotEmpty(str2);
        Map<Integer, List<zzjz>> arrayMap = new ArrayMap();
        Cursor query;
        try {
            query = getWritableDatabase().query("event_filters", new String[]{"audience_id", DataSchemeDataSource.SCHEME_DATA}, "app_id=? AND event_name=?", new String[]{str, str2}, null, null, null);
            if (query.moveToFirst()) {
                do {
                    byte[] blob = query.getBlob(1);
                    zzaba zza = zzaba.zza(blob, 0, blob.length);
                    zzabj com_google_android_gms_internal_measurement_zzjz = new zzjz();
                    try {
                        com_google_android_gms_internal_measurement_zzjz.zzb(zza);
                        int i = query.getInt(0);
                        List list = (List) arrayMap.get(Integer.valueOf(i));
                        if (list == null) {
                            list = new ArrayList();
                            arrayMap.put(Integer.valueOf(i), list);
                        }
                        list.add(com_google_android_gms_internal_measurement_zzjz);
                    } catch (IOException e2) {
                        try {
                            zzgg().zzil().zze("Failed to merge filter. appId", zzfg.zzbh(str), e2);
                        } catch (SQLiteException e3) {
                            e = e3;
                        }
                    }
                } while (query.moveToNext());
                if (query != null) {
                    query.close();
                }
                return arrayMap;
            }
            Map<Integer, List<zzjz>> emptyMap = Collections.emptyMap();
            if (query == null) {
                return emptyMap;
            }
            query.close();
            return emptyMap;
        } catch (SQLiteException e4) {
            e = e4;
            query = null;
            try {
                zzgg().zzil().zze("Database error querying filters. appId", zzfg.zzbh(str), e);
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

    final Map<Integer, List<zzkc>> zzk(String str, String str2) {
        Object e;
        Throwable th;
        zzch();
        zzab();
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotEmpty(str2);
        Map<Integer, List<zzkc>> arrayMap = new ArrayMap();
        Cursor query;
        try {
            query = getWritableDatabase().query("property_filters", new String[]{"audience_id", DataSchemeDataSource.SCHEME_DATA}, "app_id=? AND property_name=?", new String[]{str, str2}, null, null, null);
            if (query.moveToFirst()) {
                do {
                    byte[] blob = query.getBlob(1);
                    zzaba zza = zzaba.zza(blob, 0, blob.length);
                    zzabj com_google_android_gms_internal_measurement_zzkc = new zzkc();
                    try {
                        com_google_android_gms_internal_measurement_zzkc.zzb(zza);
                        int i = query.getInt(0);
                        List list = (List) arrayMap.get(Integer.valueOf(i));
                        if (list == null) {
                            list = new ArrayList();
                            arrayMap.put(Integer.valueOf(i), list);
                        }
                        list.add(com_google_android_gms_internal_measurement_zzkc);
                    } catch (IOException e2) {
                        try {
                            zzgg().zzil().zze("Failed to merge filter", zzfg.zzbh(str), e2);
                        } catch (SQLiteException e3) {
                            e = e3;
                        }
                    }
                } while (query.moveToNext());
                if (query != null) {
                    query.close();
                }
                return arrayMap;
            }
            Map<Integer, List<zzkc>> emptyMap = Collections.emptyMap();
            if (query == null) {
                return emptyMap;
            }
            query.close();
            return emptyMap;
        } catch (SQLiteException e4) {
            e = e4;
            query = null;
            try {
                zzgg().zzil().zze("Database error querying filters. appId", zzfg.zzbh(str), e);
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
}
