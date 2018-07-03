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
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.telegram.messenger.exoplayer2.upstream.DataSchemeDataSource;

final class zzei extends zzjq {
    private static final String[] zzaev = new String[]{"last_bundled_timestamp", "ALTER TABLE events ADD COLUMN last_bundled_timestamp INTEGER;", "last_sampled_complex_event_id", "ALTER TABLE events ADD COLUMN last_sampled_complex_event_id INTEGER;", "last_sampling_rate", "ALTER TABLE events ADD COLUMN last_sampling_rate INTEGER;", "last_exempt_from_sampling", "ALTER TABLE events ADD COLUMN last_exempt_from_sampling INTEGER;"};
    private static final String[] zzaew = new String[]{TtmlNode.ATTR_TTS_ORIGIN, "ALTER TABLE user_attributes ADD COLUMN origin TEXT;"};
    private static final String[] zzaex = new String[]{"app_version", "ALTER TABLE apps ADD COLUMN app_version TEXT;", "app_store", "ALTER TABLE apps ADD COLUMN app_store TEXT;", "gmp_version", "ALTER TABLE apps ADD COLUMN gmp_version INTEGER;", "dev_cert_hash", "ALTER TABLE apps ADD COLUMN dev_cert_hash INTEGER;", "measurement_enabled", "ALTER TABLE apps ADD COLUMN measurement_enabled INTEGER;", "last_bundle_start_timestamp", "ALTER TABLE apps ADD COLUMN last_bundle_start_timestamp INTEGER;", "day", "ALTER TABLE apps ADD COLUMN day INTEGER;", "daily_public_events_count", "ALTER TABLE apps ADD COLUMN daily_public_events_count INTEGER;", "daily_events_count", "ALTER TABLE apps ADD COLUMN daily_events_count INTEGER;", "daily_conversions_count", "ALTER TABLE apps ADD COLUMN daily_conversions_count INTEGER;", "remote_config", "ALTER TABLE apps ADD COLUMN remote_config BLOB;", "config_fetched_time", "ALTER TABLE apps ADD COLUMN config_fetched_time INTEGER;", "failed_config_fetch_time", "ALTER TABLE apps ADD COLUMN failed_config_fetch_time INTEGER;", "app_version_int", "ALTER TABLE apps ADD COLUMN app_version_int INTEGER;", "firebase_instance_id", "ALTER TABLE apps ADD COLUMN firebase_instance_id TEXT;", "daily_error_events_count", "ALTER TABLE apps ADD COLUMN daily_error_events_count INTEGER;", "daily_realtime_events_count", "ALTER TABLE apps ADD COLUMN daily_realtime_events_count INTEGER;", "health_monitor_sample", "ALTER TABLE apps ADD COLUMN health_monitor_sample TEXT;", "android_id", "ALTER TABLE apps ADD COLUMN android_id INTEGER;", "adid_reporting_enabled", "ALTER TABLE apps ADD COLUMN adid_reporting_enabled INTEGER;", "ssaid_reporting_enabled", "ALTER TABLE apps ADD COLUMN ssaid_reporting_enabled INTEGER;"};
    private static final String[] zzaey = new String[]{"realtime", "ALTER TABLE raw_events ADD COLUMN realtime INTEGER;"};
    private static final String[] zzaez = new String[]{"has_realtime", "ALTER TABLE queue ADD COLUMN has_realtime INTEGER;", "retry_count", "ALTER TABLE queue ADD COLUMN retry_count INTEGER;"};
    private static final String[] zzafa = new String[]{"previous_install_count", "ALTER TABLE app2 ADD COLUMN previous_install_count INTEGER;"};
    private final zzel zzafb = new zzel(this, getContext(), "google_app_measurement.db");
    private final zzjm zzafc = new zzjm(zzbt());

    zzei(zzjr com_google_android_gms_internal_measurement_zzjr) {
        super(com_google_android_gms_internal_measurement_zzjr);
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
            zzge().zzim().zze("Database error", str, e);
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
            zzge().zzim().zze("Database error", str, e);
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
                zzge().zzim().log("Loaded invalid null value from database");
                return null;
            case 1:
                return Long.valueOf(cursor.getLong(i));
            case 2:
                return Double.valueOf(cursor.getDouble(i));
            case 3:
                return cursor.getString(i);
            case 4:
                zzge().zzim().log("Loaded invalid blob type value, ignoring it");
                return null;
            default:
                zzge().zzim().zzg("Loaded invalid unknown value type, ignoring it", Integer.valueOf(type));
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
            com_google_android_gms_internal_measurement_zzfg.zzip().log("Failed to turn off database read permission");
        }
        if (!file.setWritable(false, false)) {
            com_google_android_gms_internal_measurement_zzfg.zzip().log("Failed to turn off database write permission");
        }
        if (!file.setReadable(true, true)) {
            com_google_android_gms_internal_measurement_zzfg.zzip().log("Failed to turn on database read permission for owner");
        }
        if (!file.setWritable(true, true)) {
            com_google_android_gms_internal_measurement_zzfg.zzip().log("Failed to turn on database write permission for owner");
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
                com_google_android_gms_internal_measurement_zzfg.zzim().zzg("Failed to verify columns on table that was just created", str);
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
            com_google_android_gms_internal_measurement_zzfg.zzip().zze("Table has extra columns. table, columns", str, TextUtils.join(", ", zzb));
        }
    }

    private static boolean zza(zzfg com_google_android_gms_internal_measurement_zzfg, SQLiteDatabase sQLiteDatabase, String str) {
        Cursor query;
        Object e;
        Throwable th;
        Cursor cursor = null;
        if (com_google_android_gms_internal_measurement_zzfg == null) {
            throw new IllegalArgumentException("Monitor must not be null");
        }
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
                    com_google_android_gms_internal_measurement_zzfg.zzip().zze("Error querying for table", str, e);
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
            com_google_android_gms_internal_measurement_zzfg.zzip().zze("Error querying for table", str, e);
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

    private final boolean zza(String str, int i, zzke com_google_android_gms_internal_measurement_zzke) {
        zzch();
        zzab();
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzke);
        if (TextUtils.isEmpty(com_google_android_gms_internal_measurement_zzke.zzarq)) {
            zzge().zzip().zzd("Event filter had no event name. Audience definition ignored. appId, audienceId, filterId", zzfg.zzbm(str), Integer.valueOf(i), String.valueOf(com_google_android_gms_internal_measurement_zzke.zzarp));
            return false;
        }
        try {
            byte[] bArr = new byte[com_google_android_gms_internal_measurement_zzke.zzvm()];
            zzabw zzb = zzabw.zzb(bArr, 0, bArr.length);
            com_google_android_gms_internal_measurement_zzke.zza(zzb);
            zzb.zzve();
            ContentValues contentValues = new ContentValues();
            contentValues.put("app_id", str);
            contentValues.put("audience_id", Integer.valueOf(i));
            contentValues.put("filter_id", com_google_android_gms_internal_measurement_zzke.zzarp);
            contentValues.put("event_name", com_google_android_gms_internal_measurement_zzke.zzarq);
            contentValues.put(DataSchemeDataSource.SCHEME_DATA, bArr);
            try {
                if (getWritableDatabase().insertWithOnConflict("event_filters", null, contentValues, 5) == -1) {
                    zzge().zzim().zzg("Failed to insert event filter (got -1). appId", zzfg.zzbm(str));
                }
                return true;
            } catch (SQLiteException e) {
                zzge().zzim().zze("Error storing event filter. appId", zzfg.zzbm(str), e);
                return false;
            }
        } catch (IOException e2) {
            zzge().zzim().zze("Configuration loss. Failed to serialize event filter. appId", zzfg.zzbm(str), e2);
            return false;
        }
    }

    private final boolean zza(String str, int i, zzkh com_google_android_gms_internal_measurement_zzkh) {
        zzch();
        zzab();
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzkh);
        if (TextUtils.isEmpty(com_google_android_gms_internal_measurement_zzkh.zzasf)) {
            zzge().zzip().zzd("Property filter had no property name. Audience definition ignored. appId, audienceId, filterId", zzfg.zzbm(str), Integer.valueOf(i), String.valueOf(com_google_android_gms_internal_measurement_zzkh.zzarp));
            return false;
        }
        try {
            byte[] bArr = new byte[com_google_android_gms_internal_measurement_zzkh.zzvm()];
            zzabw zzb = zzabw.zzb(bArr, 0, bArr.length);
            com_google_android_gms_internal_measurement_zzkh.zza(zzb);
            zzb.zzve();
            ContentValues contentValues = new ContentValues();
            contentValues.put("app_id", str);
            contentValues.put("audience_id", Integer.valueOf(i));
            contentValues.put("filter_id", com_google_android_gms_internal_measurement_zzkh.zzarp);
            contentValues.put("property_name", com_google_android_gms_internal_measurement_zzkh.zzasf);
            contentValues.put(DataSchemeDataSource.SCHEME_DATA, bArr);
            try {
                if (getWritableDatabase().insertWithOnConflict("property_filters", null, contentValues, 5) != -1) {
                    return true;
                }
                zzge().zzim().zzg("Failed to insert property filter (got -1). appId", zzfg.zzbm(str));
                return false;
            } catch (SQLiteException e) {
                zzge().zzim().zze("Error storing property filter. appId", zzfg.zzbm(str), e);
                return false;
            }
        } catch (IOException e2) {
            zzge().zzim().zze("Configuration loss. Failed to serialize property filter. appId", zzfg.zzbm(str), e2);
            return false;
        }
    }

    private final boolean zza(String str, List<Integer> list) {
        Preconditions.checkNotEmpty(str);
        zzch();
        zzab();
        SQLiteDatabase writableDatabase = getWritableDatabase();
        try {
            if (zza("select count(1) from audience_filter_values where app_id=?", new String[]{str}) <= ((long) Math.max(0, Math.min(2000, zzgg().zzb(str, zzew.zzahn))))) {
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
            zzge().zzim().zze("Database error querying filters. appId", zzfg.zzbm(str), e);
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
            return this.zzafb.getWritableDatabase();
        } catch (SQLiteException e) {
            zzge().zzip().zzg("Error opening database", e);
            throw e;
        }
    }

    public final void setTransactionSuccessful() {
        zzch();
        getWritableDatabase().setTransactionSuccessful();
    }

    public final long zza(zzkq com_google_android_gms_internal_measurement_zzkq) throws IOException {
        zzab();
        zzch();
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzkq);
        Preconditions.checkNotEmpty(com_google_android_gms_internal_measurement_zzkq.zzti);
        try {
            long j;
            Object obj = new byte[com_google_android_gms_internal_measurement_zzkq.zzvm()];
            zzabw zzb = zzabw.zzb(obj, 0, obj.length);
            com_google_android_gms_internal_measurement_zzkq.zza(zzb);
            zzb.zzve();
            zzhg zzgb = zzgb();
            Preconditions.checkNotNull(obj);
            zzgb.zzab();
            MessageDigest messageDigest = zzka.getMessageDigest("MD5");
            if (messageDigest == null) {
                zzgb.zzge().zzim().log("Failed to get MD5");
                j = 0;
            } else {
                j = zzka.zzc(messageDigest.digest(obj));
            }
            ContentValues contentValues = new ContentValues();
            contentValues.put("app_id", com_google_android_gms_internal_measurement_zzkq.zzti);
            contentValues.put("metadata_fingerprint", Long.valueOf(j));
            contentValues.put(TtmlNode.TAG_METADATA, obj);
            try {
                getWritableDatabase().insertWithOnConflict("raw_events_metadata", null, contentValues, 4);
                return j;
            } catch (SQLiteException e) {
                zzge().zzim().zze("Error storing raw event metadata. appId", zzfg.zzbm(com_google_android_gms_internal_measurement_zzkq.zzti), e);
                throw e;
            }
        } catch (IOException e2) {
            zzge().zzim().zze("Data loss. Failed to serialize event metadata. appId", zzfg.zzbm(com_google_android_gms_internal_measurement_zzkq.zzti), e2);
            throw e2;
        }
    }

    public final Pair<zzkn, Long> zza(String str, Long l) {
        Cursor rawQuery;
        Object e;
        Throwable th;
        Pair<zzkn, Long> pair = null;
        zzab();
        zzch();
        try {
            rawQuery = getWritableDatabase().rawQuery("select main_event, children_to_process from main_event_params where app_id=? and event_id=?", new String[]{str, String.valueOf(l)});
            try {
                if (rawQuery.moveToFirst()) {
                    byte[] blob = rawQuery.getBlob(0);
                    Long valueOf = Long.valueOf(rawQuery.getLong(1));
                    zzabv zza = zzabv.zza(blob, 0, blob.length);
                    zzace com_google_android_gms_internal_measurement_zzkn = new zzkn();
                    try {
                        com_google_android_gms_internal_measurement_zzkn.zzb(zza);
                        pair = Pair.create(com_google_android_gms_internal_measurement_zzkn, valueOf);
                        if (rawQuery != null) {
                            rawQuery.close();
                        }
                    } catch (IOException e2) {
                        zzge().zzim().zzd("Failed to merge main event. appId, eventId", zzfg.zzbm(str), l, e2);
                        if (rawQuery != null) {
                            rawQuery.close();
                        }
                    }
                } else {
                    zzge().zzit().log("Main event not found");
                    if (rawQuery != null) {
                        rawQuery.close();
                    }
                }
            } catch (SQLiteException e3) {
                e = e3;
                try {
                    zzge().zzim().zzg("Error selecting main event", e);
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
            zzge().zzim().zzg("Error selecting main event", e);
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
                        com_google_android_gms_internal_measurement_zzej.zzafe = query.getLong(1);
                        com_google_android_gms_internal_measurement_zzej.zzafd = query.getLong(2);
                        com_google_android_gms_internal_measurement_zzej.zzaff = query.getLong(3);
                        com_google_android_gms_internal_measurement_zzej.zzafg = query.getLong(4);
                        com_google_android_gms_internal_measurement_zzej.zzafh = query.getLong(5);
                    }
                    if (z) {
                        com_google_android_gms_internal_measurement_zzej.zzafe++;
                    }
                    if (z2) {
                        com_google_android_gms_internal_measurement_zzej.zzafd++;
                    }
                    if (z3) {
                        com_google_android_gms_internal_measurement_zzej.zzaff++;
                    }
                    if (z4) {
                        com_google_android_gms_internal_measurement_zzej.zzafg++;
                    }
                    if (z5) {
                        com_google_android_gms_internal_measurement_zzej.zzafh++;
                    }
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("day", Long.valueOf(j));
                    contentValues.put("daily_public_events_count", Long.valueOf(com_google_android_gms_internal_measurement_zzej.zzafd));
                    contentValues.put("daily_events_count", Long.valueOf(com_google_android_gms_internal_measurement_zzej.zzafe));
                    contentValues.put("daily_conversions_count", Long.valueOf(com_google_android_gms_internal_measurement_zzej.zzaff));
                    contentValues.put("daily_error_events_count", Long.valueOf(com_google_android_gms_internal_measurement_zzej.zzafg));
                    contentValues.put("daily_realtime_events_count", Long.valueOf(com_google_android_gms_internal_measurement_zzej.zzafh));
                    writableDatabase.update("apps", contentValues, "app_id=?", strArr);
                    if (query != null) {
                        query.close();
                    }
                    return com_google_android_gms_internal_measurement_zzej;
                }
                zzge().zzip().zzg("Not updating daily counts, app is not known. appId", zzfg.zzbm(str));
                if (query != null) {
                    query.close();
                }
                return com_google_android_gms_internal_measurement_zzej;
            } catch (SQLiteException e2) {
                e = e2;
                try {
                    zzge().zzim().zze("Error updating daily counts. appId", zzfg.zzbm(str), e);
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
            zzge().zzim().zze("Error updating daily counts. appId", zzfg.zzbm(str), e);
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

    public final void zza(zzdy com_google_android_gms_internal_measurement_zzdy) {
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzdy);
        zzab();
        zzch();
        ContentValues contentValues = new ContentValues();
        contentValues.put("app_id", com_google_android_gms_internal_measurement_zzdy.zzah());
        contentValues.put("app_instance_id", com_google_android_gms_internal_measurement_zzdy.getAppInstanceId());
        contentValues.put("gmp_app_id", com_google_android_gms_internal_measurement_zzdy.getGmpAppId());
        contentValues.put("resettable_device_id_hash", com_google_android_gms_internal_measurement_zzdy.zzgi());
        contentValues.put("last_bundle_index", Long.valueOf(com_google_android_gms_internal_measurement_zzdy.zzgq()));
        contentValues.put("last_bundle_start_timestamp", Long.valueOf(com_google_android_gms_internal_measurement_zzdy.zzgk()));
        contentValues.put("last_bundle_end_timestamp", Long.valueOf(com_google_android_gms_internal_measurement_zzdy.zzgl()));
        contentValues.put("app_version", com_google_android_gms_internal_measurement_zzdy.zzag());
        contentValues.put("app_store", com_google_android_gms_internal_measurement_zzdy.zzgn());
        contentValues.put("gmp_version", Long.valueOf(com_google_android_gms_internal_measurement_zzdy.zzgo()));
        contentValues.put("dev_cert_hash", Long.valueOf(com_google_android_gms_internal_measurement_zzdy.zzgp()));
        contentValues.put("measurement_enabled", Boolean.valueOf(com_google_android_gms_internal_measurement_zzdy.isMeasurementEnabled()));
        contentValues.put("day", Long.valueOf(com_google_android_gms_internal_measurement_zzdy.zzgu()));
        contentValues.put("daily_public_events_count", Long.valueOf(com_google_android_gms_internal_measurement_zzdy.zzgv()));
        contentValues.put("daily_events_count", Long.valueOf(com_google_android_gms_internal_measurement_zzdy.zzgw()));
        contentValues.put("daily_conversions_count", Long.valueOf(com_google_android_gms_internal_measurement_zzdy.zzgx()));
        contentValues.put("config_fetched_time", Long.valueOf(com_google_android_gms_internal_measurement_zzdy.zzgr()));
        contentValues.put("failed_config_fetch_time", Long.valueOf(com_google_android_gms_internal_measurement_zzdy.zzgs()));
        contentValues.put("app_version_int", Long.valueOf(com_google_android_gms_internal_measurement_zzdy.zzgm()));
        contentValues.put("firebase_instance_id", com_google_android_gms_internal_measurement_zzdy.zzgj());
        contentValues.put("daily_error_events_count", Long.valueOf(com_google_android_gms_internal_measurement_zzdy.zzgz()));
        contentValues.put("daily_realtime_events_count", Long.valueOf(com_google_android_gms_internal_measurement_zzdy.zzgy()));
        contentValues.put("health_monitor_sample", com_google_android_gms_internal_measurement_zzdy.zzha());
        contentValues.put("android_id", Long.valueOf(com_google_android_gms_internal_measurement_zzdy.zzhc()));
        contentValues.put("adid_reporting_enabled", Boolean.valueOf(com_google_android_gms_internal_measurement_zzdy.zzhd()));
        contentValues.put("ssaid_reporting_enabled", Boolean.valueOf(com_google_android_gms_internal_measurement_zzdy.zzhe()));
        try {
            SQLiteDatabase writableDatabase = getWritableDatabase();
            if (((long) writableDatabase.update("apps", contentValues, "app_id = ?", new String[]{com_google_android_gms_internal_measurement_zzdy.zzah()})) == 0 && writableDatabase.insertWithOnConflict("apps", null, contentValues, 5) == -1) {
                zzge().zzim().zzg("Failed to insert/update app (got -1). appId", zzfg.zzbm(com_google_android_gms_internal_measurement_zzdy.zzah()));
            }
        } catch (SQLiteException e) {
            zzge().zzim().zze("Error storing app. appId", zzfg.zzbm(com_google_android_gms_internal_measurement_zzdy.zzah()), e);
        }
    }

    public final void zza(zzeq com_google_android_gms_internal_measurement_zzeq) {
        Long l = null;
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzeq);
        zzab();
        zzch();
        ContentValues contentValues = new ContentValues();
        contentValues.put("app_id", com_google_android_gms_internal_measurement_zzeq.zzti);
        contentValues.put("name", com_google_android_gms_internal_measurement_zzeq.name);
        contentValues.put("lifetime_count", Long.valueOf(com_google_android_gms_internal_measurement_zzeq.zzafr));
        contentValues.put("current_bundle_count", Long.valueOf(com_google_android_gms_internal_measurement_zzeq.zzafs));
        contentValues.put("last_fire_timestamp", Long.valueOf(com_google_android_gms_internal_measurement_zzeq.zzaft));
        contentValues.put("last_bundled_timestamp", Long.valueOf(com_google_android_gms_internal_measurement_zzeq.zzafu));
        contentValues.put("last_sampled_complex_event_id", com_google_android_gms_internal_measurement_zzeq.zzafv);
        contentValues.put("last_sampling_rate", com_google_android_gms_internal_measurement_zzeq.zzafw);
        if (com_google_android_gms_internal_measurement_zzeq.zzafx != null && com_google_android_gms_internal_measurement_zzeq.zzafx.booleanValue()) {
            l = Long.valueOf(1);
        }
        contentValues.put("last_exempt_from_sampling", l);
        try {
            if (getWritableDatabase().insertWithOnConflict("events", null, contentValues, 5) == -1) {
                zzge().zzim().zzg("Failed to insert/update event aggregates (got -1). appId", zzfg.zzbm(com_google_android_gms_internal_measurement_zzeq.zzti));
            }
        } catch (SQLiteException e) {
            zzge().zzim().zze("Error storing event aggregates. appId", zzfg.zzbm(com_google_android_gms_internal_measurement_zzeq.zzti), e);
        }
    }

    final void zza(String str, zzkd[] com_google_android_gms_internal_measurement_zzkdArr) {
        int i = 0;
        zzch();
        zzab();
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzkdArr);
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
            for (zzkd com_google_android_gms_internal_measurement_zzkd : com_google_android_gms_internal_measurement_zzkdArr) {
                zzch();
                zzab();
                Preconditions.checkNotEmpty(str);
                Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzkd);
                Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzkd.zzarn);
                Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzkd.zzarm);
                if (com_google_android_gms_internal_measurement_zzkd.zzarl == null) {
                    zzge().zzip().zzg("Audience with no ID. appId", zzfg.zzbm(str));
                } else {
                    int intValue = com_google_android_gms_internal_measurement_zzkd.zzarl.intValue();
                    for (zzke com_google_android_gms_internal_measurement_zzke : com_google_android_gms_internal_measurement_zzkd.zzarn) {
                        if (com_google_android_gms_internal_measurement_zzke.zzarp == null) {
                            zzge().zzip().zze("Event filter with no ID. Audience definition ignored. appId, audienceId", zzfg.zzbm(str), com_google_android_gms_internal_measurement_zzkd.zzarl);
                            break;
                        }
                    }
                    for (zzkh com_google_android_gms_internal_measurement_zzkh : com_google_android_gms_internal_measurement_zzkd.zzarm) {
                        if (com_google_android_gms_internal_measurement_zzkh.zzarp == null) {
                            zzge().zzip().zze("Property filter with no ID. Audience definition ignored. appId, audienceId", zzfg.zzbm(str), com_google_android_gms_internal_measurement_zzkd.zzarl);
                            break;
                        }
                    }
                    for (zzke com_google_android_gms_internal_measurement_zzke2 : com_google_android_gms_internal_measurement_zzkd.zzarn) {
                        if (!zza(str, intValue, com_google_android_gms_internal_measurement_zzke2)) {
                            i2 = 0;
                            break;
                        }
                    }
                    i2 = 1;
                    if (i2 != 0) {
                        for (zzkh com_google_android_gms_internal_measurement_zzkh2 : com_google_android_gms_internal_measurement_zzkd.zzarm) {
                            if (!zza(str, intValue, com_google_android_gms_internal_measurement_zzkh2)) {
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
            i2 = com_google_android_gms_internal_measurement_zzkdArr.length;
            while (i < i2) {
                arrayList.add(com_google_android_gms_internal_measurement_zzkdArr[i].zzarl);
                i++;
            }
            zza(str, arrayList);
            writableDatabase.setTransactionSuccessful();
        } finally {
            writableDatabase.endTransaction();
        }
    }

    public final boolean zza(zzed com_google_android_gms_internal_measurement_zzed) {
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzed);
        zzab();
        zzch();
        if (zzh(com_google_android_gms_internal_measurement_zzed.packageName, com_google_android_gms_internal_measurement_zzed.zzaep.name) == null) {
            if (zza("SELECT COUNT(1) FROM conditional_properties WHERE app_id=?", new String[]{com_google_android_gms_internal_measurement_zzed.packageName}) >= 1000) {
                return false;
            }
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("app_id", com_google_android_gms_internal_measurement_zzed.packageName);
        contentValues.put(TtmlNode.ATTR_TTS_ORIGIN, com_google_android_gms_internal_measurement_zzed.origin);
        contentValues.put("name", com_google_android_gms_internal_measurement_zzed.zzaep.name);
        zza(contentValues, "value", com_google_android_gms_internal_measurement_zzed.zzaep.getValue());
        contentValues.put("active", Boolean.valueOf(com_google_android_gms_internal_measurement_zzed.active));
        contentValues.put("trigger_event_name", com_google_android_gms_internal_measurement_zzed.triggerEventName);
        contentValues.put("trigger_timeout", Long.valueOf(com_google_android_gms_internal_measurement_zzed.triggerTimeout));
        zzgb();
        contentValues.put("timed_out_event", zzka.zza(com_google_android_gms_internal_measurement_zzed.zzaeq));
        contentValues.put("creation_timestamp", Long.valueOf(com_google_android_gms_internal_measurement_zzed.creationTimestamp));
        zzgb();
        contentValues.put("triggered_event", zzka.zza(com_google_android_gms_internal_measurement_zzed.zzaer));
        contentValues.put("triggered_timestamp", Long.valueOf(com_google_android_gms_internal_measurement_zzed.zzaep.zzaqz));
        contentValues.put("time_to_live", Long.valueOf(com_google_android_gms_internal_measurement_zzed.timeToLive));
        zzgb();
        contentValues.put("expired_event", zzka.zza(com_google_android_gms_internal_measurement_zzed.zzaes));
        try {
            if (getWritableDatabase().insertWithOnConflict("conditional_properties", null, contentValues, 5) == -1) {
                zzge().zzim().zzg("Failed to insert/update conditional user property (got -1)", zzfg.zzbm(com_google_android_gms_internal_measurement_zzed.packageName));
            }
        } catch (SQLiteException e) {
            zzge().zzim().zze("Error storing conditional user property", zzfg.zzbm(com_google_android_gms_internal_measurement_zzed.packageName), e);
        }
        return true;
    }

    public final boolean zza(zzep com_google_android_gms_internal_measurement_zzep, long j, boolean z) {
        zzab();
        zzch();
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzep);
        Preconditions.checkNotEmpty(com_google_android_gms_internal_measurement_zzep.zzti);
        zzace com_google_android_gms_internal_measurement_zzkn = new zzkn();
        com_google_android_gms_internal_measurement_zzkn.zzatc = Long.valueOf(com_google_android_gms_internal_measurement_zzep.zzafp);
        com_google_android_gms_internal_measurement_zzkn.zzata = new zzko[com_google_android_gms_internal_measurement_zzep.zzafq.size()];
        Iterator it = com_google_android_gms_internal_measurement_zzep.zzafq.iterator();
        int i = 0;
        while (it.hasNext()) {
            String str = (String) it.next();
            zzko com_google_android_gms_internal_measurement_zzko = new zzko();
            int i2 = i + 1;
            com_google_android_gms_internal_measurement_zzkn.zzata[i] = com_google_android_gms_internal_measurement_zzko;
            com_google_android_gms_internal_measurement_zzko.name = str;
            zzgb().zza(com_google_android_gms_internal_measurement_zzko, com_google_android_gms_internal_measurement_zzep.zzafq.get(str));
            i = i2;
        }
        try {
            byte[] bArr = new byte[com_google_android_gms_internal_measurement_zzkn.zzvm()];
            zzabw zzb = zzabw.zzb(bArr, 0, bArr.length);
            com_google_android_gms_internal_measurement_zzkn.zza(zzb);
            zzb.zzve();
            zzge().zzit().zze("Saving event, name, data size", zzga().zzbj(com_google_android_gms_internal_measurement_zzep.name), Integer.valueOf(bArr.length));
            ContentValues contentValues = new ContentValues();
            contentValues.put("app_id", com_google_android_gms_internal_measurement_zzep.zzti);
            contentValues.put("name", com_google_android_gms_internal_measurement_zzep.name);
            contentValues.put("timestamp", Long.valueOf(com_google_android_gms_internal_measurement_zzep.timestamp));
            contentValues.put("metadata_fingerprint", Long.valueOf(j));
            contentValues.put(DataSchemeDataSource.SCHEME_DATA, bArr);
            contentValues.put("realtime", Integer.valueOf(z ? 1 : 0));
            try {
                if (getWritableDatabase().insert("raw_events", null, contentValues) != -1) {
                    return true;
                }
                zzge().zzim().zzg("Failed to insert raw event (got -1). appId", zzfg.zzbm(com_google_android_gms_internal_measurement_zzep.zzti));
                return false;
            } catch (SQLiteException e) {
                zzge().zzim().zze("Error storing raw event. appId", zzfg.zzbm(com_google_android_gms_internal_measurement_zzep.zzti), e);
                return false;
            }
        } catch (IOException e2) {
            zzge().zzim().zze("Data loss. Failed to serialize event params/data. appId", zzfg.zzbm(com_google_android_gms_internal_measurement_zzep.zzti), e2);
            return false;
        }
    }

    public final boolean zza(zzjz com_google_android_gms_internal_measurement_zzjz) {
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzjz);
        zzab();
        zzch();
        if (zzh(com_google_android_gms_internal_measurement_zzjz.zzti, com_google_android_gms_internal_measurement_zzjz.name) == null) {
            if (zzka.zzcc(com_google_android_gms_internal_measurement_zzjz.name)) {
                if (zza("select count(1) from user_attributes where app_id=? and name not like '!_%' escape '!'", new String[]{com_google_android_gms_internal_measurement_zzjz.zzti}) >= 25) {
                    return false;
                }
            }
            if (zza("select count(1) from user_attributes where app_id=? and origin=? AND name like '!_%' escape '!'", new String[]{com_google_android_gms_internal_measurement_zzjz.zzti, com_google_android_gms_internal_measurement_zzjz.origin}) >= 25) {
                return false;
            }
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("app_id", com_google_android_gms_internal_measurement_zzjz.zzti);
        contentValues.put(TtmlNode.ATTR_TTS_ORIGIN, com_google_android_gms_internal_measurement_zzjz.origin);
        contentValues.put("name", com_google_android_gms_internal_measurement_zzjz.name);
        contentValues.put("set_timestamp", Long.valueOf(com_google_android_gms_internal_measurement_zzjz.zzaqz));
        zza(contentValues, "value", com_google_android_gms_internal_measurement_zzjz.value);
        try {
            if (getWritableDatabase().insertWithOnConflict("user_attributes", null, contentValues, 5) == -1) {
                zzge().zzim().zzg("Failed to insert/update user property (got -1). appId", zzfg.zzbm(com_google_android_gms_internal_measurement_zzjz.zzti));
            }
        } catch (SQLiteException e) {
            zzge().zzim().zze("Error storing user property. appId", zzfg.zzbm(com_google_android_gms_internal_measurement_zzjz.zzti), e);
        }
        return true;
    }

    public final boolean zza(zzkq com_google_android_gms_internal_measurement_zzkq, boolean z) {
        zzab();
        zzch();
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzkq);
        Preconditions.checkNotEmpty(com_google_android_gms_internal_measurement_zzkq.zzti);
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzkq.zzatm);
        zzhp();
        long currentTimeMillis = zzbt().currentTimeMillis();
        if (com_google_android_gms_internal_measurement_zzkq.zzatm.longValue() < currentTimeMillis - zzef.zzhh() || com_google_android_gms_internal_measurement_zzkq.zzatm.longValue() > zzef.zzhh() + currentTimeMillis) {
            zzge().zzip().zzd("Storing bundle outside of the max uploading time span. appId, now, timestamp", zzfg.zzbm(com_google_android_gms_internal_measurement_zzkq.zzti), Long.valueOf(currentTimeMillis), com_google_android_gms_internal_measurement_zzkq.zzatm);
        }
        try {
            byte[] bArr = new byte[com_google_android_gms_internal_measurement_zzkq.zzvm()];
            zzabw zzb = zzabw.zzb(bArr, 0, bArr.length);
            com_google_android_gms_internal_measurement_zzkq.zza(zzb);
            zzb.zzve();
            bArr = zzgb().zza(bArr);
            zzge().zzit().zzg("Saving bundle, size", Integer.valueOf(bArr.length));
            ContentValues contentValues = new ContentValues();
            contentValues.put("app_id", com_google_android_gms_internal_measurement_zzkq.zzti);
            contentValues.put("bundle_end_timestamp", com_google_android_gms_internal_measurement_zzkq.zzatm);
            contentValues.put(DataSchemeDataSource.SCHEME_DATA, bArr);
            contentValues.put("has_realtime", Integer.valueOf(z ? 1 : 0));
            if (com_google_android_gms_internal_measurement_zzkq.zzauj != null) {
                contentValues.put("retry_count", com_google_android_gms_internal_measurement_zzkq.zzauj);
            }
            try {
                if (getWritableDatabase().insert("queue", null, contentValues) != -1) {
                    return true;
                }
                zzge().zzim().zzg("Failed to insert bundle (got -1). appId", zzfg.zzbm(com_google_android_gms_internal_measurement_zzkq.zzti));
                return false;
            } catch (SQLiteException e) {
                zzge().zzim().zze("Error storing bundle. appId", zzfg.zzbm(com_google_android_gms_internal_measurement_zzkq.zzti), e);
                return false;
            }
        } catch (IOException e2) {
            zzge().zzim().zze("Data loss. Failed to serialize bundle. appId", zzfg.zzbm(com_google_android_gms_internal_measurement_zzkq.zzti), e2);
            return false;
        }
    }

    public final boolean zza(String str, Long l, long j, zzkn com_google_android_gms_internal_measurement_zzkn) {
        zzab();
        zzch();
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzkn);
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotNull(l);
        try {
            byte[] bArr = new byte[com_google_android_gms_internal_measurement_zzkn.zzvm()];
            zzabw zzb = zzabw.zzb(bArr, 0, bArr.length);
            com_google_android_gms_internal_measurement_zzkn.zza(zzb);
            zzb.zzve();
            zzge().zzit().zze("Saving complex main event, appId, data size", zzga().zzbj(str), Integer.valueOf(bArr.length));
            ContentValues contentValues = new ContentValues();
            contentValues.put("app_id", str);
            contentValues.put("event_id", l);
            contentValues.put("children_to_process", Long.valueOf(j));
            contentValues.put("main_event", bArr);
            try {
                if (getWritableDatabase().insertWithOnConflict("main_event_params", null, contentValues, 5) != -1) {
                    return true;
                }
                zzge().zzim().zzg("Failed to insert complex main event (got -1). appId", zzfg.zzbm(str));
                return false;
            } catch (SQLiteException e) {
                zzge().zzim().zze("Error storing complex main event. appId", zzfg.zzbm(str), e);
                return false;
            }
        } catch (IOException e2) {
            zzge().zzim().zzd("Data loss. Failed to serialize event params/data. appId, eventId", zzfg.zzbm(str), l, e2);
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
                    zzge().zzit().log("No expired configs for apps with pending events");
                    if (rawQuery != null) {
                        rawQuery.close();
                    }
                }
            } catch (SQLiteException e2) {
                e = e2;
                try {
                    zzge().zzim().zzg("Error selecting expired configs", e);
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
            zzge().zzim().zzg("Error selecting expired configs", e);
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

    public final List<Pair<zzkq, Long>> zzb(String str, int i, int i2) {
        List<Pair<zzkq, Long>> arrayList;
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
                            byte[] zzb = zzgb().zzb(query.getBlob(1));
                            if (!arrayList.isEmpty() && zzb.length + i3 > i2) {
                                break;
                            }
                            zzabv zza = zzabv.zza(zzb, 0, zzb.length);
                            zzace com_google_android_gms_internal_measurement_zzkq = new zzkq();
                            try {
                                com_google_android_gms_internal_measurement_zzkq.zzb(zza);
                                if (!query.isNull(2)) {
                                    com_google_android_gms_internal_measurement_zzkq.zzauj = Integer.valueOf(query.getInt(2));
                                }
                                length = zzb.length + i3;
                                arrayList.add(Pair.create(com_google_android_gms_internal_measurement_zzkq, Long.valueOf(j)));
                            } catch (IOException e2) {
                                zzge().zzim().zze("Failed to merge queued bundle. appId", zzfg.zzbm(str), e2);
                                length = i3;
                            }
                            if (!query.moveToNext() || length > i2) {
                                break;
                            }
                            i3 = length;
                        } catch (IOException e22) {
                            zzge().zzim().zze("Failed to unzip queued bundle. appId", zzfg.zzbm(str), e22);
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
                zzge().zzim().zze("Error querying bundles. appId", zzfg.zzbm(str), e);
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

    public final List<zzjz> zzb(String str, String str2, String str3) {
        Object obj;
        Object e;
        Cursor cursor;
        Throwable th;
        Cursor cursor2 = null;
        Preconditions.checkNotEmpty(str);
        zzab();
        zzch();
        List<zzjz> arrayList = new ArrayList();
        try {
            List arrayList2 = new ArrayList(3);
            arrayList2.add(str);
            StringBuilder stringBuilder = new StringBuilder("app_id=?");
            if (!TextUtils.isEmpty(str2)) {
                arrayList2.add(str2);
                stringBuilder.append(" and origin=?");
            }
            if (!TextUtils.isEmpty(str3)) {
                arrayList2.add(String.valueOf(str3).concat("*"));
                stringBuilder.append(" and name glob ?");
            }
            String[] strArr = new String[]{"name", "set_timestamp", "value", TtmlNode.ATTR_TTS_ORIGIN};
            Cursor query = getWritableDatabase().query("user_attributes", strArr, stringBuilder.toString(), (String[]) arrayList2.toArray(new String[arrayList2.size()]), null, null, "rowid", "1001");
            try {
                if (query.moveToFirst()) {
                    while (arrayList.size() < 1000) {
                        String string;
                        try {
                            String string2 = query.getString(0);
                            long j = query.getLong(1);
                            Object zza = zza(query, 2);
                            string = query.getString(3);
                            if (zza == null) {
                                zzge().zzim().zzd("(2)Read invalid user property value, ignoring it", zzfg.zzbm(str), string, str3);
                            } else {
                                arrayList.add(new zzjz(str, string, string2, j, zza));
                            }
                            if (!query.moveToNext()) {
                                break;
                            }
                            obj = string;
                        } catch (SQLiteException e2) {
                            e = e2;
                            cursor = query;
                            obj = string;
                        } catch (Throwable th2) {
                            th = th2;
                            cursor2 = query;
                        }
                    }
                    zzge().zzim().zzg("Read more than the max allowed user properties, ignoring excess", Integer.valueOf(1000));
                    if (query != null) {
                        query.close();
                    }
                    return arrayList;
                }
                if (query != null) {
                    query.close();
                }
                return arrayList;
            } catch (SQLiteException e3) {
                e = e3;
                cursor = query;
            } catch (Throwable th22) {
                th = th22;
                cursor2 = query;
            }
        } catch (SQLiteException e4) {
            e = e4;
            cursor = null;
            try {
                zzge().zzim().zzd("(2)Error querying user properties", zzfg.zzbm(str), obj, e);
                if (cursor != null) {
                    cursor.close();
                }
                return null;
            } catch (Throwable th3) {
                th = th3;
                cursor2 = cursor;
                if (cursor2 != null) {
                    cursor2.close();
                }
                throw th;
            }
        } catch (Throwable th4) {
            th = th4;
            if (cursor2 != null) {
                cursor2.close();
            }
            throw th;
        }
    }

    public final List<zzed> zzb(String str, String[] strArr) {
        Cursor query;
        Object e;
        Cursor cursor;
        Throwable th;
        zzab();
        zzch();
        List<zzed> arrayList = new ArrayList();
        try {
            query = getWritableDatabase().query("conditional_properties", new String[]{"app_id", TtmlNode.ATTR_TTS_ORIGIN, "name", "value", "active", "trigger_event_name", "trigger_timeout", "timed_out_event", "creation_timestamp", "triggered_event", "triggered_timestamp", "time_to_live", "expired_event"}, str, strArr, null, null, "rowid", "1001");
            try {
                if (query.moveToFirst()) {
                    do {
                        if (arrayList.size() >= 1000) {
                            zzge().zzim().zzg("Read more than the max allowed conditional properties, ignoring extra", Integer.valueOf(1000));
                            break;
                        }
                        String string = query.getString(0);
                        String string2 = query.getString(1);
                        String string3 = query.getString(2);
                        Object zza = zza(query, 3);
                        boolean z = query.getInt(4) != 0;
                        String string4 = query.getString(5);
                        long j = query.getLong(6);
                        zzeu com_google_android_gms_internal_measurement_zzeu = (zzeu) zzgb().zza(query.getBlob(7), zzeu.CREATOR);
                        long j2 = query.getLong(8);
                        zzeu com_google_android_gms_internal_measurement_zzeu2 = (zzeu) zzgb().zza(query.getBlob(9), zzeu.CREATOR);
                        long j3 = query.getLong(10);
                        List<zzed> list = arrayList;
                        list.add(new zzed(string, string2, new zzjx(string3, j3, zza, string2), j2, z, string4, com_google_android_gms_internal_measurement_zzeu, j, com_google_android_gms_internal_measurement_zzeu2, query.getLong(11), (zzeu) zzgb().zza(query.getBlob(12), zzeu.CREATOR)));
                    } while (query.moveToNext());
                    if (query != null) {
                        query.close();
                    }
                    return arrayList;
                }
                if (query != null) {
                    query.close();
                }
                return arrayList;
            } catch (SQLiteException e2) {
                e = e2;
                cursor = query;
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (SQLiteException e3) {
            e = e3;
            cursor = null;
            try {
                zzge().zzim().zzg("Error querying conditional user property value", e);
                List<zzed> emptyList = Collections.emptyList();
                if (cursor == null) {
                    return emptyList;
                }
                cursor.close();
                return emptyList;
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
    }

    public final List<zzjz> zzbb(String str) {
        Object e;
        Cursor cursor;
        Throwable th;
        Cursor cursor2 = null;
        Preconditions.checkNotEmpty(str);
        zzab();
        zzch();
        List<zzjz> arrayList = new ArrayList();
        try {
            Cursor query = getWritableDatabase().query("user_attributes", new String[]{"name", TtmlNode.ATTR_TTS_ORIGIN, "set_timestamp", "value"}, "app_id=?", new String[]{str}, null, null, "rowid", "1000");
            try {
                if (query.moveToFirst()) {
                    do {
                        String string = query.getString(0);
                        String string2 = query.getString(1);
                        if (string2 == null) {
                            string2 = TtmlNode.ANONYMOUS_REGION_ID;
                        }
                        long j = query.getLong(2);
                        Object zza = zza(query, 3);
                        if (zza == null) {
                            zzge().zzim().zzg("Read invalid user property value, ignoring it. appId", zzfg.zzbm(str));
                        } else {
                            arrayList.add(new zzjz(str, string2, string, j, zza));
                        }
                    } while (query.moveToNext());
                    if (query != null) {
                        query.close();
                    }
                    return arrayList;
                }
                if (query != null) {
                    query.close();
                }
                return arrayList;
            } catch (SQLiteException e2) {
                e = e2;
                cursor = query;
            } catch (Throwable th2) {
                th = th2;
                cursor2 = query;
            }
        } catch (SQLiteException e3) {
            e = e3;
            cursor = null;
            try {
                zzge().zzim().zze("Error querying user properties. appId", zzfg.zzbm(str), e);
                if (cursor != null) {
                    cursor.close();
                }
                return null;
            } catch (Throwable th3) {
                th = th3;
                cursor2 = cursor;
                if (cursor2 != null) {
                    cursor2.close();
                }
                throw th;
            }
        } catch (Throwable th4) {
            th = th4;
            if (cursor2 != null) {
                cursor2.close();
            }
            throw th;
        }
    }

    public final zzdy zzbc(String str) {
        Object e;
        Throwable th;
        Preconditions.checkNotEmpty(str);
        zzab();
        zzch();
        Cursor query;
        try {
            query = getWritableDatabase().query("apps", new String[]{"app_instance_id", "gmp_app_id", "resettable_device_id_hash", "last_bundle_index", "last_bundle_start_timestamp", "last_bundle_end_timestamp", "app_version", "app_store", "gmp_version", "dev_cert_hash", "measurement_enabled", "day", "daily_public_events_count", "daily_events_count", "daily_conversions_count", "config_fetched_time", "failed_config_fetch_time", "app_version_int", "firebase_instance_id", "daily_error_events_count", "daily_realtime_events_count", "health_monitor_sample", "android_id", "adid_reporting_enabled", "ssaid_reporting_enabled"}, "app_id=?", new String[]{str}, null, null, null);
            try {
                if (query.moveToFirst()) {
                    zzdy com_google_android_gms_internal_measurement_zzdy = new zzdy(this.zzajp.zzla(), str);
                    com_google_android_gms_internal_measurement_zzdy.zzal(query.getString(0));
                    com_google_android_gms_internal_measurement_zzdy.zzam(query.getString(1));
                    com_google_android_gms_internal_measurement_zzdy.zzan(query.getString(2));
                    com_google_android_gms_internal_measurement_zzdy.zzr(query.getLong(3));
                    com_google_android_gms_internal_measurement_zzdy.zzm(query.getLong(4));
                    com_google_android_gms_internal_measurement_zzdy.zzn(query.getLong(5));
                    com_google_android_gms_internal_measurement_zzdy.setAppVersion(query.getString(6));
                    com_google_android_gms_internal_measurement_zzdy.zzap(query.getString(7));
                    com_google_android_gms_internal_measurement_zzdy.zzp(query.getLong(8));
                    com_google_android_gms_internal_measurement_zzdy.zzq(query.getLong(9));
                    boolean z = query.isNull(10) || query.getInt(10) != 0;
                    com_google_android_gms_internal_measurement_zzdy.setMeasurementEnabled(z);
                    com_google_android_gms_internal_measurement_zzdy.zzu(query.getLong(11));
                    com_google_android_gms_internal_measurement_zzdy.zzv(query.getLong(12));
                    com_google_android_gms_internal_measurement_zzdy.zzw(query.getLong(13));
                    com_google_android_gms_internal_measurement_zzdy.zzx(query.getLong(14));
                    com_google_android_gms_internal_measurement_zzdy.zzs(query.getLong(15));
                    com_google_android_gms_internal_measurement_zzdy.zzt(query.getLong(16));
                    com_google_android_gms_internal_measurement_zzdy.zzo(query.isNull(17) ? -2147483648L : (long) query.getInt(17));
                    com_google_android_gms_internal_measurement_zzdy.zzao(query.getString(18));
                    com_google_android_gms_internal_measurement_zzdy.zzz(query.getLong(19));
                    com_google_android_gms_internal_measurement_zzdy.zzy(query.getLong(20));
                    com_google_android_gms_internal_measurement_zzdy.zzaq(query.getString(21));
                    com_google_android_gms_internal_measurement_zzdy.zzaa(query.isNull(22) ? 0 : query.getLong(22));
                    z = query.isNull(23) || query.getInt(23) != 0;
                    com_google_android_gms_internal_measurement_zzdy.zzd(z);
                    z = query.isNull(24) || query.getInt(24) != 0;
                    com_google_android_gms_internal_measurement_zzdy.zze(z);
                    com_google_android_gms_internal_measurement_zzdy.zzgh();
                    if (query.moveToNext()) {
                        zzge().zzim().zzg("Got multiple records for app, expected one. appId", zzfg.zzbm(str));
                    }
                    if (query == null) {
                        return com_google_android_gms_internal_measurement_zzdy;
                    }
                    query.close();
                    return com_google_android_gms_internal_measurement_zzdy;
                }
                if (query != null) {
                    query.close();
                }
                return null;
            } catch (SQLiteException e2) {
                e = e2;
                try {
                    zzge().zzim().zze("Error querying app. appId", zzfg.zzbm(str), e);
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
            zzge().zzim().zze("Error querying app. appId", zzfg.zzbm(str), e);
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

    public final long zzbd(String str) {
        Preconditions.checkNotEmpty(str);
        zzab();
        zzch();
        try {
            SQLiteDatabase writableDatabase = getWritableDatabase();
            String valueOf = String.valueOf(Math.max(0, Math.min(1000000, zzgg().zzb(str, zzew.zzagx))));
            return (long) writableDatabase.delete("raw_events", "rowid in (select rowid from raw_events where app_id=? order by rowid desc limit -1 offset ?)", new String[]{str, valueOf});
        } catch (SQLiteException e) {
            zzge().zzim().zze("Error deleting over the limit events. appId", zzfg.zzbm(str), e);
            return 0;
        }
    }

    public final byte[] zzbe(String str) {
        Object e;
        Throwable th;
        Preconditions.checkNotEmpty(str);
        zzab();
        zzch();
        Cursor query;
        try {
            query = getWritableDatabase().query("apps", new String[]{"remote_config"}, "app_id=?", new String[]{str}, null, null, null);
            try {
                if (query.moveToFirst()) {
                    byte[] blob = query.getBlob(0);
                    if (query.moveToNext()) {
                        zzge().zzim().zzg("Got multiple records for app config, expected one. appId", zzfg.zzbm(str));
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
                    zzge().zzim().zze("Error querying remote config. appId", zzfg.zzbm(str), e);
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
            zzge().zzim().zze("Error querying remote config. appId", zzfg.zzbm(str), e);
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

    final Map<Integer, zzkr> zzbf(String str) {
        Cursor query;
        Object e;
        Throwable th;
        zzch();
        zzab();
        Preconditions.checkNotEmpty(str);
        try {
            query = getWritableDatabase().query("audience_filter_values", new String[]{"audience_id", "current_results"}, "app_id=?", new String[]{str}, null, null, null);
            if (query.moveToFirst()) {
                Map<Integer, zzkr> arrayMap = new ArrayMap();
                do {
                    int i = query.getInt(0);
                    byte[] blob = query.getBlob(1);
                    zzabv zza = zzabv.zza(blob, 0, blob.length);
                    zzace com_google_android_gms_internal_measurement_zzkr = new zzkr();
                    try {
                        com_google_android_gms_internal_measurement_zzkr.zzb(zza);
                        arrayMap.put(Integer.valueOf(i), com_google_android_gms_internal_measurement_zzkr);
                    } catch (IOException e2) {
                        try {
                            zzge().zzim().zzd("Failed to merge filter results. appId, audienceId, error", zzfg.zzbm(str), Integer.valueOf(i), e2);
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
                zzge().zzim().zze("Database error querying filter results. appId", zzfg.zzbm(str), e);
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

    public final long zzbg(String str) {
        Preconditions.checkNotEmpty(str);
        return zza("select count(1) from events where app_id=? and name not like '!_%' escape '!'", new String[]{str}, 0);
    }

    public final List<zzed> zzc(String str, String str2, String str3) {
        Preconditions.checkNotEmpty(str);
        zzab();
        zzch();
        List arrayList = new ArrayList(3);
        arrayList.add(str);
        StringBuilder stringBuilder = new StringBuilder("app_id=?");
        if (!TextUtils.isEmpty(str2)) {
            arrayList.add(str2);
            stringBuilder.append(" and origin=?");
        }
        if (!TextUtils.isEmpty(str3)) {
            arrayList.add(String.valueOf(str3).concat("*"));
            stringBuilder.append(" and name glob ?");
        }
        return zzb(stringBuilder.toString(), (String[]) arrayList.toArray(new String[arrayList.size()]));
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
                zzge().zzip().log("The number of upload retries exceeds the limit. Will remain unchanged.");
            }
            try {
                getWritableDatabase().execSQL(new StringBuilder(String.valueOf(join).length() + 127).append("UPDATE queue SET retry_count = IFNULL(retry_count, 0) + 1 WHERE rowid IN ").append(join).append(" AND (retry_count IS NULL OR retry_count < 2147483647)").toString());
            } catch (SQLiteException e) {
                zzge().zzim().zzg("Error incrementing retry count. error", e);
            }
        }
    }

    public final zzeq zzf(String str, String str2) {
        Object e;
        Cursor cursor;
        Throwable th;
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotEmpty(str2);
        zzab();
        zzch();
        Cursor query;
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
                        zzge().zzim().zzg("Got multiple records for event aggregates, expected one. appId", zzfg.zzbm(str));
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
                    zzge().zzim().zzd("Error querying events. appId", zzfg.zzbm(str), zzga().zzbj(str2), e);
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
            zzge().zzim().zzd("Error querying events. appId", zzfg.zzbm(str), zzga().zzbj(str2), e);
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

    public final void zzg(String str, String str2) {
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotEmpty(str2);
        zzab();
        zzch();
        try {
            zzge().zzit().zzg("Deleted user attribute rows", Integer.valueOf(getWritableDatabase().delete("user_attributes", "app_id=? and name=?", new String[]{str, str2})));
        } catch (SQLiteException e) {
            zzge().zzim().zzd("Error deleting user attribute. appId", zzfg.zzbm(str), zzga().zzbl(str2), e);
        }
    }

    public final zzjz zzh(String str, String str2) {
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
                    zzjz com_google_android_gms_internal_measurement_zzjz = new zzjz(str3, query.getString(2), str2, query.getLong(0), zza(query, 1));
                    if (query.moveToNext()) {
                        zzge().zzim().zzg("Got multiple records for user property, expected one. appId", zzfg.zzbm(str));
                    }
                    if (query == null) {
                        return com_google_android_gms_internal_measurement_zzjz;
                    }
                    query.close();
                    return com_google_android_gms_internal_measurement_zzjz;
                }
                if (query != null) {
                    query.close();
                }
                return null;
            } catch (SQLiteException e2) {
                e = e2;
                cursor = query;
                try {
                    zzge().zzim().zzd("Error querying user property. appId", zzfg.zzbm(str), zzga().zzbl(str2), e);
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
            zzge().zzim().zzd("Error querying user property. appId", zzfg.zzbm(str), zzga().zzbl(str2), e);
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

    protected final boolean zzhf() {
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
                    zzge().zzim().zzg("Database error getting next bundle app id", e);
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
            zzge().zzim().zzg("Database error getting next bundle app id", e);
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
            long j = zzgf().zzajx.get();
            long elapsedRealtime = zzbt().elapsedRealtime();
            if (Math.abs(elapsedRealtime - j) > ((Long) zzew.zzahg.get()).longValue()) {
                zzgf().zzajx.set(elapsedRealtime);
                zzab();
                zzch();
                if (zzhv()) {
                    int delete = getWritableDatabase().delete("queue", "abs(bundle_end_timestamp - ?) > cast(? as integer)", new String[]{String.valueOf(zzbt().currentTimeMillis()), String.valueOf(zzef.zzhh())});
                    if (delete > 0) {
                        zzge().zzit().zzg("Deleted stale rows. rowsDeleted", Integer.valueOf(delete));
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
            zzge().zzim().zzg("Error querying raw events", e);
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

    public final zzed zzi(String str, String str2) {
        Object e;
        Cursor cursor;
        Throwable th;
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotEmpty(str2);
        zzab();
        zzch();
        Cursor query;
        try {
            query = getWritableDatabase().query("conditional_properties", new String[]{TtmlNode.ATTR_TTS_ORIGIN, "value", "active", "trigger_event_name", "trigger_timeout", "timed_out_event", "creation_timestamp", "triggered_event", "triggered_timestamp", "time_to_live", "expired_event"}, "app_id=? and name=?", new String[]{str, str2}, null, null, null);
            try {
                if (query.moveToFirst()) {
                    String string = query.getString(0);
                    Object zza = zza(query, 1);
                    boolean z = query.getInt(2) != 0;
                    String string2 = query.getString(3);
                    long j = query.getLong(4);
                    zzeu com_google_android_gms_internal_measurement_zzeu = (zzeu) zzgb().zza(query.getBlob(5), zzeu.CREATOR);
                    long j2 = query.getLong(6);
                    zzeu com_google_android_gms_internal_measurement_zzeu2 = (zzeu) zzgb().zza(query.getBlob(7), zzeu.CREATOR);
                    long j3 = query.getLong(8);
                    zzed com_google_android_gms_internal_measurement_zzed = new zzed(str, string, new zzjx(str2, j3, zza, string), j2, z, string2, com_google_android_gms_internal_measurement_zzeu, j, com_google_android_gms_internal_measurement_zzeu2, query.getLong(9), (zzeu) zzgb().zza(query.getBlob(10), zzeu.CREATOR));
                    if (query.moveToNext()) {
                        zzge().zzim().zze("Got multiple records for conditional property, expected one", zzfg.zzbm(str), zzga().zzbl(str2));
                    }
                    if (query == null) {
                        return com_google_android_gms_internal_measurement_zzed;
                    }
                    query.close();
                    return com_google_android_gms_internal_measurement_zzed;
                }
                if (query != null) {
                    query.close();
                }
                return null;
            } catch (SQLiteException e2) {
                e = e2;
                cursor = query;
                try {
                    zzge().zzim().zzd("Error querying conditional property", zzfg.zzbm(str), zzga().zzbl(str2), e);
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
            zzge().zzim().zzd("Error querying conditional property", zzfg.zzbm(str), zzga().zzbl(str2), e);
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

    public final int zzj(String str, String str2) {
        int i = 0;
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotEmpty(str2);
        zzab();
        zzch();
        try {
            i = getWritableDatabase().delete("conditional_properties", "app_id=? and name=?", new String[]{str, str2});
        } catch (SQLiteException e) {
            zzge().zzim().zzd("Error deleting conditional property", zzfg.zzbm(str), zzga().zzbl(str2), e);
        }
        return i;
    }

    final Map<Integer, List<zzke>> zzk(String str, String str2) {
        Object e;
        Throwable th;
        zzch();
        zzab();
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotEmpty(str2);
        Map<Integer, List<zzke>> arrayMap = new ArrayMap();
        Cursor query;
        try {
            query = getWritableDatabase().query("event_filters", new String[]{"audience_id", DataSchemeDataSource.SCHEME_DATA}, "app_id=? AND event_name=?", new String[]{str, str2}, null, null, null);
            if (query.moveToFirst()) {
                do {
                    byte[] blob = query.getBlob(1);
                    zzabv zza = zzabv.zza(blob, 0, blob.length);
                    zzace com_google_android_gms_internal_measurement_zzke = new zzke();
                    try {
                        com_google_android_gms_internal_measurement_zzke.zzb(zza);
                        int i = query.getInt(0);
                        List list = (List) arrayMap.get(Integer.valueOf(i));
                        if (list == null) {
                            list = new ArrayList();
                            arrayMap.put(Integer.valueOf(i), list);
                        }
                        list.add(com_google_android_gms_internal_measurement_zzke);
                    } catch (IOException e2) {
                        try {
                            zzge().zzim().zze("Failed to merge filter. appId", zzfg.zzbm(str), e2);
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
            Map<Integer, List<zzke>> emptyMap = Collections.emptyMap();
            if (query == null) {
                return emptyMap;
            }
            query.close();
            return emptyMap;
        } catch (SQLiteException e4) {
            e = e4;
            query = null;
            try {
                zzge().zzim().zze("Database error querying filters. appId", zzfg.zzbm(str), e);
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

    final Map<Integer, List<zzkh>> zzl(String str, String str2) {
        Object e;
        Throwable th;
        zzch();
        zzab();
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotEmpty(str2);
        Map<Integer, List<zzkh>> arrayMap = new ArrayMap();
        Cursor query;
        try {
            query = getWritableDatabase().query("property_filters", new String[]{"audience_id", DataSchemeDataSource.SCHEME_DATA}, "app_id=? AND property_name=?", new String[]{str, str2}, null, null, null);
            if (query.moveToFirst()) {
                do {
                    byte[] blob = query.getBlob(1);
                    zzabv zza = zzabv.zza(blob, 0, blob.length);
                    zzace com_google_android_gms_internal_measurement_zzkh = new zzkh();
                    try {
                        com_google_android_gms_internal_measurement_zzkh.zzb(zza);
                        int i = query.getInt(0);
                        List list = (List) arrayMap.get(Integer.valueOf(i));
                        if (list == null) {
                            list = new ArrayList();
                            arrayMap.put(Integer.valueOf(i), list);
                        }
                        list.add(com_google_android_gms_internal_measurement_zzkh);
                    } catch (IOException e2) {
                        try {
                            zzge().zzim().zze("Failed to merge filter", zzfg.zzbm(str), e2);
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
            Map<Integer, List<zzkh>> emptyMap = Collections.emptyMap();
            if (query == null) {
                return emptyMap;
            }
            query.close();
            return emptyMap;
        } catch (SQLiteException e4) {
            e = e4;
            query = null;
            try {
                zzge().zzim().zze("Database error querying filters. appId", zzfg.zzbm(str), e);
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

    protected final long zzm(String str, String str2) {
        long zza;
        Object e;
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotEmpty(str2);
        zzab();
        zzch();
        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.beginTransaction();
        try {
            zza = zza(new StringBuilder(String.valueOf(str2).length() + 32).append("select ").append(str2).append(" from app2 where app_id=?").toString(), new String[]{str}, -1);
            if (zza == -1) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("app_id", str);
                contentValues.put("first_open_count", Integer.valueOf(0));
                contentValues.put("previous_install_count", Integer.valueOf(0));
                if (writableDatabase.insertWithOnConflict("app2", null, contentValues, 5) == -1) {
                    zzge().zzim().zze("Failed to insert column (got -1). appId", zzfg.zzbm(str), str2);
                    writableDatabase.endTransaction();
                    return -1;
                }
                zza = 0;
            }
            try {
                ContentValues contentValues2 = new ContentValues();
                contentValues2.put("app_id", str);
                contentValues2.put(str2, Long.valueOf(1 + zza));
                if (((long) writableDatabase.update("app2", contentValues2, "app_id = ?", new String[]{str})) == 0) {
                    zzge().zzim().zze("Failed to update column (got 0). appId", zzfg.zzbm(str), str2);
                    writableDatabase.endTransaction();
                    return -1;
                }
                writableDatabase.setTransactionSuccessful();
                writableDatabase.endTransaction();
                return zza;
            } catch (SQLiteException e2) {
                e = e2;
                try {
                    zzge().zzim().zzd("Error inserting column. appId", zzfg.zzbm(str), str2, e);
                    return zza;
                } finally {
                    writableDatabase.endTransaction();
                }
            }
        } catch (SQLiteException e3) {
            e = e3;
            zza = 0;
            zzge().zzim().zzd("Error inserting column. appId", zzfg.zzbm(str), str2, e);
            return zza;
        }
    }
}
