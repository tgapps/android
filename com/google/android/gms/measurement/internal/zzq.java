package com.google.android.gms.measurement.internal;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Pair;
import com.google.android.exoplayer2.upstream.DataSchemeDataSource;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.internal.measurement.zzfu;
import com.google.android.gms.internal.measurement.zzfv;
import com.google.android.gms.internal.measurement.zzfy;
import com.google.android.gms.internal.measurement.zzgf;
import com.google.android.gms.internal.measurement.zzgg;
import com.google.android.gms.internal.measurement.zzgi;
import com.google.android.gms.internal.measurement.zzgj;
import com.google.android.gms.internal.measurement.zzyx;
import com.google.android.gms.internal.measurement.zzyy;
import com.google.android.gms.internal.measurement.zzzg;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

final class zzq extends zzez {
    private static final String[] zzahi = new String[]{"last_bundled_timestamp", "ALTER TABLE events ADD COLUMN last_bundled_timestamp INTEGER;", "last_bundled_day", "ALTER TABLE events ADD COLUMN last_bundled_day INTEGER;", "last_sampled_complex_event_id", "ALTER TABLE events ADD COLUMN last_sampled_complex_event_id INTEGER;", "last_sampling_rate", "ALTER TABLE events ADD COLUMN last_sampling_rate INTEGER;", "last_exempt_from_sampling", "ALTER TABLE events ADD COLUMN last_exempt_from_sampling INTEGER;"};
    private static final String[] zzahj = new String[]{TtmlNode.ATTR_TTS_ORIGIN, "ALTER TABLE user_attributes ADD COLUMN origin TEXT;"};
    private static final String[] zzahk = new String[]{"app_version", "ALTER TABLE apps ADD COLUMN app_version TEXT;", "app_store", "ALTER TABLE apps ADD COLUMN app_store TEXT;", "gmp_version", "ALTER TABLE apps ADD COLUMN gmp_version INTEGER;", "dev_cert_hash", "ALTER TABLE apps ADD COLUMN dev_cert_hash INTEGER;", "measurement_enabled", "ALTER TABLE apps ADD COLUMN measurement_enabled INTEGER;", "last_bundle_start_timestamp", "ALTER TABLE apps ADD COLUMN last_bundle_start_timestamp INTEGER;", "day", "ALTER TABLE apps ADD COLUMN day INTEGER;", "daily_public_events_count", "ALTER TABLE apps ADD COLUMN daily_public_events_count INTEGER;", "daily_events_count", "ALTER TABLE apps ADD COLUMN daily_events_count INTEGER;", "daily_conversions_count", "ALTER TABLE apps ADD COLUMN daily_conversions_count INTEGER;", "remote_config", "ALTER TABLE apps ADD COLUMN remote_config BLOB;", "config_fetched_time", "ALTER TABLE apps ADD COLUMN config_fetched_time INTEGER;", "failed_config_fetch_time", "ALTER TABLE apps ADD COLUMN failed_config_fetch_time INTEGER;", "app_version_int", "ALTER TABLE apps ADD COLUMN app_version_int INTEGER;", "firebase_instance_id", "ALTER TABLE apps ADD COLUMN firebase_instance_id TEXT;", "daily_error_events_count", "ALTER TABLE apps ADD COLUMN daily_error_events_count INTEGER;", "daily_realtime_events_count", "ALTER TABLE apps ADD COLUMN daily_realtime_events_count INTEGER;", "health_monitor_sample", "ALTER TABLE apps ADD COLUMN health_monitor_sample TEXT;", "android_id", "ALTER TABLE apps ADD COLUMN android_id INTEGER;", "adid_reporting_enabled", "ALTER TABLE apps ADD COLUMN adid_reporting_enabled INTEGER;", "ssaid_reporting_enabled", "ALTER TABLE apps ADD COLUMN ssaid_reporting_enabled INTEGER;", "admob_app_id", "ALTER TABLE apps ADD COLUMN admob_app_id TEXT;", "linked_admob_app_id", "ALTER TABLE apps ADD COLUMN linked_admob_app_id TEXT;"};
    private static final String[] zzahl = new String[]{"realtime", "ALTER TABLE raw_events ADD COLUMN realtime INTEGER;"};
    private static final String[] zzahm = new String[]{"has_realtime", "ALTER TABLE queue ADD COLUMN has_realtime INTEGER;", "retry_count", "ALTER TABLE queue ADD COLUMN retry_count INTEGER;"};
    private static final String[] zzahn = new String[]{"previous_install_count", "ALTER TABLE app2 ADD COLUMN previous_install_count INTEGER;"};
    private final zzt zzaho = new zzt(this, getContext(), "google_app_measurement.db");
    private final zzev zzahp = new zzev(zzbx());

    zzq(zzfa com_google_android_gms_measurement_internal_zzfa) {
        super(com_google_android_gms_measurement_internal_zzfa);
    }

    protected final boolean zzgt() {
        return false;
    }

    public final void beginTransaction() {
        zzcl();
        getWritableDatabase().beginTransaction();
    }

    public final void setTransactionSuccessful() {
        zzcl();
        getWritableDatabase().setTransactionSuccessful();
    }

    public final void endTransaction() {
        zzcl();
        getWritableDatabase().endTransaction();
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
            zzgo().zzjd().zze("Database error", str, e);
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
            zzgo().zzjd().zze("Database error", str, e);
            throw e;
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    final SQLiteDatabase getWritableDatabase() {
        zzaf();
        try {
            return this.zzaho.getWritableDatabase();
        } catch (SQLiteException e) {
            zzgo().zzjg().zzg("Error opening database", e);
            throw e;
        }
    }

    public final zzz zzg(String str, String str2) {
        Cursor query;
        Object e;
        Cursor cursor;
        Throwable th;
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotEmpty(str2);
        zzaf();
        zzcl();
        try {
            query = getWritableDatabase().query("events", new String[]{"lifetime_count", "current_bundle_count", "last_fire_timestamp", "last_bundled_timestamp", "last_bundled_day", "last_sampled_complex_event_id", "last_sampling_rate", "last_exempt_from_sampling"}, "app_id=? and name=?", new String[]{str, str2}, null, null, null);
            try {
                if (query.moveToFirst()) {
                    long j = query.getLong(0);
                    long j2 = query.getLong(1);
                    long j3 = query.getLong(2);
                    long j4 = query.isNull(3) ? 0 : query.getLong(3);
                    Long valueOf = query.isNull(4) ? null : Long.valueOf(query.getLong(4));
                    Long valueOf2 = query.isNull(5) ? null : Long.valueOf(query.getLong(5));
                    Long valueOf3 = query.isNull(6) ? null : Long.valueOf(query.getLong(6));
                    Boolean bool = null;
                    if (!query.isNull(7)) {
                        bool = Boolean.valueOf(query.getLong(7) == 1);
                    }
                    zzz com_google_android_gms_measurement_internal_zzz = new zzz(str, str2, j, j2, j3, j4, valueOf, valueOf2, valueOf3, bool);
                    if (query.moveToNext()) {
                        zzgo().zzjd().zzg("Got multiple records for event aggregates, expected one. appId", zzap.zzbv(str));
                    }
                    if (query == null) {
                        return com_google_android_gms_measurement_internal_zzz;
                    }
                    query.close();
                    return com_google_android_gms_measurement_internal_zzz;
                }
                if (query != null) {
                    query.close();
                }
                return null;
            } catch (SQLiteException e2) {
                e = e2;
                cursor = query;
                try {
                    zzgo().zzjd().zzd("Error querying events. appId", zzap.zzbv(str), zzgl().zzbs(str2), e);
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
            zzgo().zzjd().zzd("Error querying events. appId", zzap.zzbv(str), zzgl().zzbs(str2), e);
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

    public final void zza(zzz com_google_android_gms_measurement_internal_zzz) {
        Long l = null;
        Preconditions.checkNotNull(com_google_android_gms_measurement_internal_zzz);
        zzaf();
        zzcl();
        ContentValues contentValues = new ContentValues();
        contentValues.put("app_id", com_google_android_gms_measurement_internal_zzz.zztt);
        contentValues.put("name", com_google_android_gms_measurement_internal_zzz.name);
        contentValues.put("lifetime_count", Long.valueOf(com_google_android_gms_measurement_internal_zzz.zzaie));
        contentValues.put("current_bundle_count", Long.valueOf(com_google_android_gms_measurement_internal_zzz.zzaif));
        contentValues.put("last_fire_timestamp", Long.valueOf(com_google_android_gms_measurement_internal_zzz.zzaig));
        contentValues.put("last_bundled_timestamp", Long.valueOf(com_google_android_gms_measurement_internal_zzz.zzaih));
        contentValues.put("last_bundled_day", com_google_android_gms_measurement_internal_zzz.zzaii);
        contentValues.put("last_sampled_complex_event_id", com_google_android_gms_measurement_internal_zzz.zzaij);
        contentValues.put("last_sampling_rate", com_google_android_gms_measurement_internal_zzz.zzaik);
        if (com_google_android_gms_measurement_internal_zzz.zzail != null && com_google_android_gms_measurement_internal_zzz.zzail.booleanValue()) {
            l = Long.valueOf(1);
        }
        contentValues.put("last_exempt_from_sampling", l);
        try {
            if (getWritableDatabase().insertWithOnConflict("events", null, contentValues, 5) == -1) {
                zzgo().zzjd().zzg("Failed to insert/update event aggregates (got -1). appId", zzap.zzbv(com_google_android_gms_measurement_internal_zzz.zztt));
            }
        } catch (SQLiteException e) {
            zzgo().zzjd().zze("Error storing event aggregates. appId", zzap.zzbv(com_google_android_gms_measurement_internal_zzz.zztt), e);
        }
    }

    public final void zzh(String str, String str2) {
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotEmpty(str2);
        zzaf();
        zzcl();
        try {
            zzgo().zzjl().zzg("Deleted user attribute rows", Integer.valueOf(getWritableDatabase().delete("user_attributes", "app_id=? and name=?", new String[]{str, str2})));
        } catch (SQLiteException e) {
            zzgo().zzjd().zzd("Error deleting user attribute. appId", zzap.zzbv(str), zzgl().zzbu(str2), e);
        }
    }

    public final boolean zza(zzfj com_google_android_gms_measurement_internal_zzfj) {
        Preconditions.checkNotNull(com_google_android_gms_measurement_internal_zzfj);
        zzaf();
        zzcl();
        if (zzi(com_google_android_gms_measurement_internal_zzfj.zztt, com_google_android_gms_measurement_internal_zzfj.name) == null) {
            if (zzfk.zzcq(com_google_android_gms_measurement_internal_zzfj.name)) {
                if (zza("select count(1) from user_attributes where app_id=? and name not like '!_%' escape '!'", new String[]{com_google_android_gms_measurement_internal_zzfj.zztt}) >= 25) {
                    return false;
                }
            }
            long zza = zza("select count(1) from user_attributes where app_id=? and origin=? AND name like '!_%' escape '!'", new String[]{com_google_android_gms_measurement_internal_zzfj.zztt, com_google_android_gms_measurement_internal_zzfj.origin});
            if (zzgq().zze(com_google_android_gms_measurement_internal_zzfj.zztt, zzaf.zzalj)) {
                if (!"_ap".equals(com_google_android_gms_measurement_internal_zzfj.name) && zza >= 25) {
                    return false;
                }
            } else if (zza >= 25) {
                return false;
            }
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("app_id", com_google_android_gms_measurement_internal_zzfj.zztt);
        contentValues.put(TtmlNode.ATTR_TTS_ORIGIN, com_google_android_gms_measurement_internal_zzfj.origin);
        contentValues.put("name", com_google_android_gms_measurement_internal_zzfj.name);
        contentValues.put("set_timestamp", Long.valueOf(com_google_android_gms_measurement_internal_zzfj.zzaue));
        zza(contentValues, "value", com_google_android_gms_measurement_internal_zzfj.value);
        try {
            if (getWritableDatabase().insertWithOnConflict("user_attributes", null, contentValues, 5) == -1) {
                zzgo().zzjd().zzg("Failed to insert/update user property (got -1). appId", zzap.zzbv(com_google_android_gms_measurement_internal_zzfj.zztt));
            }
        } catch (SQLiteException e) {
            zzgo().zzjd().zze("Error storing user property. appId", zzap.zzbv(com_google_android_gms_measurement_internal_zzfj.zztt), e);
        }
        return true;
    }

    public final zzfj zzi(String str, String str2) {
        Object e;
        Cursor cursor;
        Throwable th;
        Cursor cursor2 = null;
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotEmpty(str2);
        zzaf();
        zzcl();
        try {
            Cursor query = getWritableDatabase().query("user_attributes", new String[]{"set_timestamp", "value", TtmlNode.ATTR_TTS_ORIGIN}, "app_id=? and name=?", new String[]{str, str2}, null, null, null);
            try {
                if (query.moveToFirst()) {
                    String str3 = str;
                    zzfj com_google_android_gms_measurement_internal_zzfj = new zzfj(str3, query.getString(2), str2, query.getLong(0), zza(query, 1));
                    if (query.moveToNext()) {
                        zzgo().zzjd().zzg("Got multiple records for user property, expected one. appId", zzap.zzbv(str));
                    }
                    if (query == null) {
                        return com_google_android_gms_measurement_internal_zzfj;
                    }
                    query.close();
                    return com_google_android_gms_measurement_internal_zzfj;
                }
                if (query != null) {
                    query.close();
                }
                return null;
            } catch (SQLiteException e2) {
                e = e2;
                cursor = query;
                try {
                    zzgo().zzjd().zzd("Error querying user property. appId", zzap.zzbv(str), zzgl().zzbu(str2), e);
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
            zzgo().zzjd().zzd("Error querying user property. appId", zzap.zzbv(str), zzgl().zzbu(str2), e);
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

    public final List<zzfj> zzbk(String str) {
        Object e;
        Cursor cursor;
        Throwable th;
        Cursor cursor2 = null;
        Preconditions.checkNotEmpty(str);
        zzaf();
        zzcl();
        List<zzfj> arrayList = new ArrayList();
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
                            zzgo().zzjd().zzg("Read invalid user property value, ignoring it. appId", zzap.zzbv(str));
                        } else {
                            arrayList.add(new zzfj(str, string2, string, j, zza));
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
                zzgo().zzjd().zze("Error querying user properties. appId", zzap.zzbv(str), e);
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

    public final List<zzfj> zzb(String str, String str2, String str3) {
        Object e;
        Cursor cursor;
        Throwable th;
        Cursor cursor2 = null;
        Preconditions.checkNotEmpty(str);
        zzaf();
        zzcl();
        List<zzfj> arrayList = new ArrayList();
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
                                zzgo().zzjd().zzd("(2)Read invalid user property value, ignoring it", zzap.zzbv(str), string, str3);
                            } else {
                                arrayList.add(new zzfj(str, string, string2, j, zza));
                            }
                            if (!query.moveToNext()) {
                                break;
                            }
                            str2 = string;
                        } catch (SQLiteException e2) {
                            e = e2;
                            cursor = query;
                            str2 = string;
                        } catch (Throwable th2) {
                            th = th2;
                            cursor2 = query;
                        }
                    }
                    zzgo().zzjd().zzg("Read more than the max allowed user properties, ignoring excess", Integer.valueOf(1000));
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
                zzgo().zzjd().zzd("(2)Error querying user properties", zzap.zzbv(str), str2, e);
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

    public final boolean zza(zzl com_google_android_gms_measurement_internal_zzl) {
        Preconditions.checkNotNull(com_google_android_gms_measurement_internal_zzl);
        zzaf();
        zzcl();
        if (zzi(com_google_android_gms_measurement_internal_zzl.packageName, com_google_android_gms_measurement_internal_zzl.zzahb.name) == null) {
            if (zza("SELECT COUNT(1) FROM conditional_properties WHERE app_id=?", new String[]{com_google_android_gms_measurement_internal_zzl.packageName}) >= 1000) {
                return false;
            }
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("app_id", com_google_android_gms_measurement_internal_zzl.packageName);
        contentValues.put(TtmlNode.ATTR_TTS_ORIGIN, com_google_android_gms_measurement_internal_zzl.origin);
        contentValues.put("name", com_google_android_gms_measurement_internal_zzl.zzahb.name);
        zza(contentValues, "value", com_google_android_gms_measurement_internal_zzl.zzahb.getValue());
        contentValues.put("active", Boolean.valueOf(com_google_android_gms_measurement_internal_zzl.active));
        contentValues.put("trigger_event_name", com_google_android_gms_measurement_internal_zzl.triggerEventName);
        contentValues.put("trigger_timeout", Long.valueOf(com_google_android_gms_measurement_internal_zzl.triggerTimeout));
        zzgm();
        contentValues.put("timed_out_event", zzfk.zza(com_google_android_gms_measurement_internal_zzl.zzahc));
        contentValues.put("creation_timestamp", Long.valueOf(com_google_android_gms_measurement_internal_zzl.creationTimestamp));
        zzgm();
        contentValues.put("triggered_event", zzfk.zza(com_google_android_gms_measurement_internal_zzl.zzahd));
        contentValues.put("triggered_timestamp", Long.valueOf(com_google_android_gms_measurement_internal_zzl.zzahb.zzaue));
        contentValues.put("time_to_live", Long.valueOf(com_google_android_gms_measurement_internal_zzl.timeToLive));
        zzgm();
        contentValues.put("expired_event", zzfk.zza(com_google_android_gms_measurement_internal_zzl.zzahe));
        try {
            if (getWritableDatabase().insertWithOnConflict("conditional_properties", null, contentValues, 5) == -1) {
                zzgo().zzjd().zzg("Failed to insert/update conditional user property (got -1)", zzap.zzbv(com_google_android_gms_measurement_internal_zzl.packageName));
            }
        } catch (SQLiteException e) {
            zzgo().zzjd().zze("Error storing conditional user property", zzap.zzbv(com_google_android_gms_measurement_internal_zzl.packageName), e);
        }
        return true;
    }

    public final zzl zzj(String str, String str2) {
        Cursor query;
        Object e;
        Cursor cursor;
        Throwable th;
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotEmpty(str2);
        zzaf();
        zzcl();
        try {
            query = getWritableDatabase().query("conditional_properties", new String[]{TtmlNode.ATTR_TTS_ORIGIN, "value", "active", "trigger_event_name", "trigger_timeout", "timed_out_event", "creation_timestamp", "triggered_event", "triggered_timestamp", "time_to_live", "expired_event"}, "app_id=? and name=?", new String[]{str, str2}, null, null, null);
            try {
                if (query.moveToFirst()) {
                    String string = query.getString(0);
                    Object zza = zza(query, 1);
                    boolean z = query.getInt(2) != 0;
                    String string2 = query.getString(3);
                    long j = query.getLong(4);
                    zzad com_google_android_gms_measurement_internal_zzad = (zzad) zzjo().zza(query.getBlob(5), zzad.CREATOR);
                    long j2 = query.getLong(6);
                    zzad com_google_android_gms_measurement_internal_zzad2 = (zzad) zzjo().zza(query.getBlob(7), zzad.CREATOR);
                    long j3 = query.getLong(8);
                    zzl com_google_android_gms_measurement_internal_zzl = new zzl(str, string, new zzfh(str2, j3, zza, string), j2, z, string2, com_google_android_gms_measurement_internal_zzad, j, com_google_android_gms_measurement_internal_zzad2, query.getLong(9), (zzad) zzjo().zza(query.getBlob(10), zzad.CREATOR));
                    if (query.moveToNext()) {
                        zzgo().zzjd().zze("Got multiple records for conditional property, expected one", zzap.zzbv(str), zzgl().zzbu(str2));
                    }
                    if (query == null) {
                        return com_google_android_gms_measurement_internal_zzl;
                    }
                    query.close();
                    return com_google_android_gms_measurement_internal_zzl;
                }
                if (query != null) {
                    query.close();
                }
                return null;
            } catch (SQLiteException e2) {
                e = e2;
                cursor = query;
                try {
                    zzgo().zzjd().zzd("Error querying conditional property", zzap.zzbv(str), zzgl().zzbu(str2), e);
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
            zzgo().zzjd().zzd("Error querying conditional property", zzap.zzbv(str), zzgl().zzbu(str2), e);
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

    public final int zzk(String str, String str2) {
        int i = 0;
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotEmpty(str2);
        zzaf();
        zzcl();
        try {
            i = getWritableDatabase().delete("conditional_properties", "app_id=? and name=?", new String[]{str, str2});
        } catch (SQLiteException e) {
            zzgo().zzjd().zzd("Error deleting conditional property", zzap.zzbv(str), zzgl().zzbu(str2), e);
        }
        return i;
    }

    public final List<zzl> zzc(String str, String str2, String str3) {
        Preconditions.checkNotEmpty(str);
        zzaf();
        zzcl();
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

    public final List<zzl> zzb(String str, String[] strArr) {
        Cursor query;
        Object e;
        Cursor cursor;
        Throwable th;
        zzaf();
        zzcl();
        List<zzl> arrayList = new ArrayList();
        try {
            query = getWritableDatabase().query("conditional_properties", new String[]{"app_id", TtmlNode.ATTR_TTS_ORIGIN, "name", "value", "active", "trigger_event_name", "trigger_timeout", "timed_out_event", "creation_timestamp", "triggered_event", "triggered_timestamp", "time_to_live", "expired_event"}, str, strArr, null, null, "rowid", "1001");
            try {
                if (query.moveToFirst()) {
                    do {
                        if (arrayList.size() >= 1000) {
                            zzgo().zzjd().zzg("Read more than the max allowed conditional properties, ignoring extra", Integer.valueOf(1000));
                            break;
                        }
                        String string = query.getString(0);
                        String string2 = query.getString(1);
                        String string3 = query.getString(2);
                        Object zza = zza(query, 3);
                        boolean z = query.getInt(4) != 0;
                        String string4 = query.getString(5);
                        long j = query.getLong(6);
                        zzad com_google_android_gms_measurement_internal_zzad = (zzad) zzjo().zza(query.getBlob(7), zzad.CREATOR);
                        long j2 = query.getLong(8);
                        zzad com_google_android_gms_measurement_internal_zzad2 = (zzad) zzjo().zza(query.getBlob(9), zzad.CREATOR);
                        long j3 = query.getLong(10);
                        List<zzl> list = arrayList;
                        list.add(new zzl(string, string2, new zzfh(string3, j3, zza, string2), j2, z, string4, com_google_android_gms_measurement_internal_zzad, j, com_google_android_gms_measurement_internal_zzad2, query.getLong(11), (zzad) zzjo().zza(query.getBlob(12), zzad.CREATOR)));
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
                zzgo().zzjd().zzg("Error querying conditional user property value", e);
                List<zzl> emptyList = Collections.emptyList();
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

    public final zzg zzbl(String str) {
        Cursor query;
        Object e;
        Throwable th;
        Preconditions.checkNotEmpty(str);
        zzaf();
        zzcl();
        try {
            query = getWritableDatabase().query("apps", new String[]{"app_instance_id", "gmp_app_id", "resettable_device_id_hash", "last_bundle_index", "last_bundle_start_timestamp", "last_bundle_end_timestamp", "app_version", "app_store", "gmp_version", "dev_cert_hash", "measurement_enabled", "day", "daily_public_events_count", "daily_events_count", "daily_conversions_count", "config_fetched_time", "failed_config_fetch_time", "app_version_int", "firebase_instance_id", "daily_error_events_count", "daily_realtime_events_count", "health_monitor_sample", "android_id", "adid_reporting_enabled", "ssaid_reporting_enabled", "admob_app_id"}, "app_id=?", new String[]{str}, null, null, null);
            try {
                if (query.moveToFirst()) {
                    zzg com_google_android_gms_measurement_internal_zzg = new zzg(this.zzamz.zzmb(), str);
                    com_google_android_gms_measurement_internal_zzg.zzam(query.getString(0));
                    com_google_android_gms_measurement_internal_zzg.zzan(query.getString(1));
                    com_google_android_gms_measurement_internal_zzg.zzap(query.getString(2));
                    com_google_android_gms_measurement_internal_zzg.zzx(query.getLong(3));
                    com_google_android_gms_measurement_internal_zzg.zzs(query.getLong(4));
                    com_google_android_gms_measurement_internal_zzg.zzt(query.getLong(5));
                    com_google_android_gms_measurement_internal_zzg.setAppVersion(query.getString(6));
                    com_google_android_gms_measurement_internal_zzg.zzar(query.getString(7));
                    com_google_android_gms_measurement_internal_zzg.zzv(query.getLong(8));
                    com_google_android_gms_measurement_internal_zzg.zzw(query.getLong(9));
                    boolean z = query.isNull(10) || query.getInt(10) != 0;
                    com_google_android_gms_measurement_internal_zzg.setMeasurementEnabled(z);
                    com_google_android_gms_measurement_internal_zzg.zzaa(query.getLong(11));
                    com_google_android_gms_measurement_internal_zzg.zzab(query.getLong(12));
                    com_google_android_gms_measurement_internal_zzg.zzac(query.getLong(13));
                    com_google_android_gms_measurement_internal_zzg.zzad(query.getLong(14));
                    com_google_android_gms_measurement_internal_zzg.zzy(query.getLong(15));
                    com_google_android_gms_measurement_internal_zzg.zzz(query.getLong(16));
                    com_google_android_gms_measurement_internal_zzg.zzu(query.isNull(17) ? -2147483648L : (long) query.getInt(17));
                    com_google_android_gms_measurement_internal_zzg.zzaq(query.getString(18));
                    com_google_android_gms_measurement_internal_zzg.zzaf(query.getLong(19));
                    com_google_android_gms_measurement_internal_zzg.zzae(query.getLong(20));
                    com_google_android_gms_measurement_internal_zzg.zzas(query.getString(21));
                    com_google_android_gms_measurement_internal_zzg.zzag(query.isNull(22) ? 0 : query.getLong(22));
                    if (query.isNull(23) || query.getInt(23) != 0) {
                        z = true;
                    } else {
                        z = false;
                    }
                    com_google_android_gms_measurement_internal_zzg.zze(z);
                    if (query.isNull(24) || query.getInt(24) != 0) {
                        z = true;
                    } else {
                        z = false;
                    }
                    com_google_android_gms_measurement_internal_zzg.zzf(z);
                    com_google_android_gms_measurement_internal_zzg.zzao(query.getString(25));
                    com_google_android_gms_measurement_internal_zzg.zzgv();
                    if (query.moveToNext()) {
                        zzgo().zzjd().zzg("Got multiple records for app, expected one. appId", zzap.zzbv(str));
                    }
                    if (query == null) {
                        return com_google_android_gms_measurement_internal_zzg;
                    }
                    query.close();
                    return com_google_android_gms_measurement_internal_zzg;
                }
                if (query != null) {
                    query.close();
                }
                return null;
            } catch (SQLiteException e2) {
                e = e2;
                try {
                    zzgo().zzjd().zze("Error querying app. appId", zzap.zzbv(str), e);
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
            zzgo().zzjd().zze("Error querying app. appId", zzap.zzbv(str), e);
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

    public final void zza(zzg com_google_android_gms_measurement_internal_zzg) {
        Preconditions.checkNotNull(com_google_android_gms_measurement_internal_zzg);
        zzaf();
        zzcl();
        ContentValues contentValues = new ContentValues();
        contentValues.put("app_id", com_google_android_gms_measurement_internal_zzg.zzal());
        contentValues.put("app_instance_id", com_google_android_gms_measurement_internal_zzg.getAppInstanceId());
        contentValues.put("gmp_app_id", com_google_android_gms_measurement_internal_zzg.getGmpAppId());
        contentValues.put("resettable_device_id_hash", com_google_android_gms_measurement_internal_zzg.zzgx());
        contentValues.put("last_bundle_index", Long.valueOf(com_google_android_gms_measurement_internal_zzg.zzhe()));
        contentValues.put("last_bundle_start_timestamp", Long.valueOf(com_google_android_gms_measurement_internal_zzg.zzgy()));
        contentValues.put("last_bundle_end_timestamp", Long.valueOf(com_google_android_gms_measurement_internal_zzg.zzgz()));
        contentValues.put("app_version", com_google_android_gms_measurement_internal_zzg.zzak());
        contentValues.put("app_store", com_google_android_gms_measurement_internal_zzg.zzhb());
        contentValues.put("gmp_version", Long.valueOf(com_google_android_gms_measurement_internal_zzg.zzhc()));
        contentValues.put("dev_cert_hash", Long.valueOf(com_google_android_gms_measurement_internal_zzg.zzhd()));
        contentValues.put("measurement_enabled", Boolean.valueOf(com_google_android_gms_measurement_internal_zzg.isMeasurementEnabled()));
        contentValues.put("day", Long.valueOf(com_google_android_gms_measurement_internal_zzg.zzhi()));
        contentValues.put("daily_public_events_count", Long.valueOf(com_google_android_gms_measurement_internal_zzg.zzhj()));
        contentValues.put("daily_events_count", Long.valueOf(com_google_android_gms_measurement_internal_zzg.zzhk()));
        contentValues.put("daily_conversions_count", Long.valueOf(com_google_android_gms_measurement_internal_zzg.zzhl()));
        contentValues.put("config_fetched_time", Long.valueOf(com_google_android_gms_measurement_internal_zzg.zzhf()));
        contentValues.put("failed_config_fetch_time", Long.valueOf(com_google_android_gms_measurement_internal_zzg.zzhg()));
        contentValues.put("app_version_int", Long.valueOf(com_google_android_gms_measurement_internal_zzg.zzha()));
        contentValues.put("firebase_instance_id", com_google_android_gms_measurement_internal_zzg.getFirebaseInstanceId());
        contentValues.put("daily_error_events_count", Long.valueOf(com_google_android_gms_measurement_internal_zzg.zzhn()));
        contentValues.put("daily_realtime_events_count", Long.valueOf(com_google_android_gms_measurement_internal_zzg.zzhm()));
        contentValues.put("health_monitor_sample", com_google_android_gms_measurement_internal_zzg.zzho());
        contentValues.put("android_id", Long.valueOf(com_google_android_gms_measurement_internal_zzg.zzhq()));
        contentValues.put("adid_reporting_enabled", Boolean.valueOf(com_google_android_gms_measurement_internal_zzg.zzhr()));
        contentValues.put("ssaid_reporting_enabled", Boolean.valueOf(com_google_android_gms_measurement_internal_zzg.zzhs()));
        contentValues.put("admob_app_id", com_google_android_gms_measurement_internal_zzg.zzgw());
        try {
            SQLiteDatabase writableDatabase = getWritableDatabase();
            if (((long) writableDatabase.update("apps", contentValues, "app_id = ?", new String[]{com_google_android_gms_measurement_internal_zzg.zzal()})) == 0 && writableDatabase.insertWithOnConflict("apps", null, contentValues, 5) == -1) {
                zzgo().zzjd().zzg("Failed to insert/update app (got -1). appId", zzap.zzbv(com_google_android_gms_measurement_internal_zzg.zzal()));
            }
        } catch (SQLiteException e) {
            zzgo().zzjd().zze("Error storing app. appId", zzap.zzbv(com_google_android_gms_measurement_internal_zzg.zzal()), e);
        }
    }

    public final long zzbm(String str) {
        Preconditions.checkNotEmpty(str);
        zzaf();
        zzcl();
        try {
            SQLiteDatabase writableDatabase = getWritableDatabase();
            String valueOf = String.valueOf(Math.max(0, Math.min(1000000, zzgq().zzb(str, zzaf.zzajs))));
            return (long) writableDatabase.delete("raw_events", "rowid in (select rowid from raw_events where app_id=? order by rowid desc limit -1 offset ?)", new String[]{str, valueOf});
        } catch (SQLiteException e) {
            zzgo().zzjd().zze("Error deleting over the limit events. appId", zzap.zzbv(str), e);
            return 0;
        }
    }

    public final zzr zza(long j, String str, boolean z, boolean z2, boolean z3, boolean z4, boolean z5) {
        Object e;
        Throwable th;
        Preconditions.checkNotEmpty(str);
        zzaf();
        zzcl();
        String[] strArr = new String[]{str};
        zzr com_google_android_gms_measurement_internal_zzr = new zzr();
        Cursor query;
        try {
            SQLiteDatabase writableDatabase = getWritableDatabase();
            query = writableDatabase.query("apps", new String[]{"day", "daily_events_count", "daily_public_events_count", "daily_conversions_count", "daily_error_events_count", "daily_realtime_events_count"}, "app_id=?", new String[]{str}, null, null, null);
            try {
                if (query.moveToFirst()) {
                    if (query.getLong(0) == j) {
                        com_google_android_gms_measurement_internal_zzr.zzahr = query.getLong(1);
                        com_google_android_gms_measurement_internal_zzr.zzahq = query.getLong(2);
                        com_google_android_gms_measurement_internal_zzr.zzahs = query.getLong(3);
                        com_google_android_gms_measurement_internal_zzr.zzaht = query.getLong(4);
                        com_google_android_gms_measurement_internal_zzr.zzahu = query.getLong(5);
                    }
                    if (z) {
                        com_google_android_gms_measurement_internal_zzr.zzahr++;
                    }
                    if (z2) {
                        com_google_android_gms_measurement_internal_zzr.zzahq++;
                    }
                    if (z3) {
                        com_google_android_gms_measurement_internal_zzr.zzahs++;
                    }
                    if (z4) {
                        com_google_android_gms_measurement_internal_zzr.zzaht++;
                    }
                    if (z5) {
                        com_google_android_gms_measurement_internal_zzr.zzahu++;
                    }
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("day", Long.valueOf(j));
                    contentValues.put("daily_public_events_count", Long.valueOf(com_google_android_gms_measurement_internal_zzr.zzahq));
                    contentValues.put("daily_events_count", Long.valueOf(com_google_android_gms_measurement_internal_zzr.zzahr));
                    contentValues.put("daily_conversions_count", Long.valueOf(com_google_android_gms_measurement_internal_zzr.zzahs));
                    contentValues.put("daily_error_events_count", Long.valueOf(com_google_android_gms_measurement_internal_zzr.zzaht));
                    contentValues.put("daily_realtime_events_count", Long.valueOf(com_google_android_gms_measurement_internal_zzr.zzahu));
                    writableDatabase.update("apps", contentValues, "app_id=?", strArr);
                    if (query != null) {
                        query.close();
                    }
                    return com_google_android_gms_measurement_internal_zzr;
                }
                zzgo().zzjg().zzg("Not updating daily counts, app is not known. appId", zzap.zzbv(str));
                if (query != null) {
                    query.close();
                }
                return com_google_android_gms_measurement_internal_zzr;
            } catch (SQLiteException e2) {
                e = e2;
                try {
                    zzgo().zzjd().zze("Error updating daily counts. appId", zzap.zzbv(str), e);
                    if (query != null) {
                        query.close();
                    }
                    return com_google_android_gms_measurement_internal_zzr;
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
            zzgo().zzjd().zze("Error updating daily counts. appId", zzap.zzbv(str), e);
            if (query != null) {
                query.close();
            }
            return com_google_android_gms_measurement_internal_zzr;
        } catch (Throwable th3) {
            th = th3;
            query = null;
            if (query != null) {
                query.close();
            }
            throw th;
        }
    }

    public final byte[] zzbn(String str) {
        Object e;
        Throwable th;
        Preconditions.checkNotEmpty(str);
        zzaf();
        zzcl();
        Cursor query;
        try {
            query = getWritableDatabase().query("apps", new String[]{"remote_config"}, "app_id=?", new String[]{str}, null, null, null);
            try {
                if (query.moveToFirst()) {
                    byte[] blob = query.getBlob(0);
                    if (query.moveToNext()) {
                        zzgo().zzjd().zzg("Got multiple records for app config, expected one. appId", zzap.zzbv(str));
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
                    zzgo().zzjd().zze("Error querying remote config. appId", zzap.zzbv(str), e);
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
            zzgo().zzjd().zze("Error querying remote config. appId", zzap.zzbv(str), e);
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

    public final boolean zza(zzgi com_google_android_gms_internal_measurement_zzgi, boolean z) {
        zzaf();
        zzcl();
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzgi);
        Preconditions.checkNotEmpty(com_google_android_gms_internal_measurement_zzgi.zztt);
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzgi.zzaxf);
        zzif();
        long currentTimeMillis = zzbx().currentTimeMillis();
        if (com_google_android_gms_internal_measurement_zzgi.zzaxf.longValue() < currentTimeMillis - zzn.zzhw() || com_google_android_gms_internal_measurement_zzgi.zzaxf.longValue() > zzn.zzhw() + currentTimeMillis) {
            zzgo().zzjg().zzd("Storing bundle outside of the max uploading time span. appId, now, timestamp", zzap.zzbv(com_google_android_gms_internal_measurement_zzgi.zztt), Long.valueOf(currentTimeMillis), com_google_android_gms_internal_measurement_zzgi.zzaxf);
        }
        try {
            int i;
            byte[] bArr = new byte[com_google_android_gms_internal_measurement_zzgi.zzvu()];
            zzyy zzk = zzyy.zzk(bArr, 0, bArr.length);
            com_google_android_gms_internal_measurement_zzgi.zza(zzk);
            zzk.zzyt();
            bArr = zzjo().zzb(bArr);
            zzgo().zzjl().zzg("Saving bundle, size", Integer.valueOf(bArr.length));
            ContentValues contentValues = new ContentValues();
            contentValues.put("app_id", com_google_android_gms_internal_measurement_zzgi.zztt);
            contentValues.put("bundle_end_timestamp", com_google_android_gms_internal_measurement_zzgi.zzaxf);
            contentValues.put(DataSchemeDataSource.SCHEME_DATA, bArr);
            String str = "has_realtime";
            if (z) {
                i = 1;
            } else {
                i = 0;
            }
            contentValues.put(str, Integer.valueOf(i));
            if (com_google_android_gms_internal_measurement_zzgi.zzayc != null) {
                contentValues.put("retry_count", com_google_android_gms_internal_measurement_zzgi.zzayc);
            }
            try {
                if (getWritableDatabase().insert("queue", null, contentValues) != -1) {
                    return true;
                }
                zzgo().zzjd().zzg("Failed to insert bundle (got -1). appId", zzap.zzbv(com_google_android_gms_internal_measurement_zzgi.zztt));
                return false;
            } catch (SQLiteException e) {
                zzgo().zzjd().zze("Error storing bundle. appId", zzap.zzbv(com_google_android_gms_internal_measurement_zzgi.zztt), e);
                return false;
            }
        } catch (IOException e2) {
            zzgo().zzjd().zze("Data loss. Failed to serialize bundle. appId", zzap.zzbv(com_google_android_gms_internal_measurement_zzgi.zztt), e2);
            return false;
        }
    }

    public final String zzid() {
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
                    zzgo().zzjd().zzg("Database error getting next bundle app id", e);
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
            zzgo().zzjd().zzg("Database error getting next bundle app id", e);
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

    public final boolean zzie() {
        return zza("select count(1) > 0 from queue where has_realtime = 1", null) != 0;
    }

    public final List<Pair<zzgi, Long>> zzb(String str, int i, int i2) {
        List<Pair<zzgi, Long>> arrayList;
        Object e;
        Cursor cursor;
        Throwable th;
        boolean z = true;
        zzaf();
        zzcl();
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
                            byte[] zza = zzjo().zza(query.getBlob(1));
                            if (!arrayList.isEmpty() && zza.length + i3 > i2) {
                                break;
                            }
                            zzyx zzj = zzyx.zzj(zza, 0, zza.length);
                            zzzg com_google_android_gms_internal_measurement_zzgi = new zzgi();
                            try {
                                com_google_android_gms_internal_measurement_zzgi.zza(zzj);
                                if (!query.isNull(2)) {
                                    com_google_android_gms_internal_measurement_zzgi.zzayc = Integer.valueOf(query.getInt(2));
                                }
                                length = zza.length + i3;
                                arrayList.add(Pair.create(com_google_android_gms_internal_measurement_zzgi, Long.valueOf(j)));
                            } catch (IOException e2) {
                                zzgo().zzjd().zze("Failed to merge queued bundle. appId", zzap.zzbv(str), e2);
                                length = i3;
                            }
                            if (!query.moveToNext() || length > i2) {
                                break;
                            }
                            i3 = length;
                        } catch (IOException e22) {
                            zzgo().zzjd().zze("Failed to unzip queued bundle. appId", zzap.zzbv(str), e22);
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
                zzgo().zzjd().zze("Error querying bundles. appId", zzap.zzbv(str), e);
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

    final void zzif() {
        zzaf();
        zzcl();
        if (zzil()) {
            long j = zzgp().zzanh.get();
            long elapsedRealtime = zzbx().elapsedRealtime();
            if (Math.abs(elapsedRealtime - j) > ((Long) zzaf.zzakb.get()).longValue()) {
                zzgp().zzanh.set(elapsedRealtime);
                zzaf();
                zzcl();
                if (zzil()) {
                    int delete = getWritableDatabase().delete("queue", "abs(bundle_end_timestamp - ?) > cast(? as integer)", new String[]{String.valueOf(zzbx().currentTimeMillis()), String.valueOf(zzn.zzhw())});
                    if (delete > 0) {
                        zzgo().zzjl().zzg("Deleted stale rows. rowsDeleted", Integer.valueOf(delete));
                    }
                }
            }
        }
    }

    final void zzc(List<Long> list) {
        zzaf();
        zzcl();
        Preconditions.checkNotNull(list);
        Preconditions.checkNotZero(list.size());
        if (zzil()) {
            String join = TextUtils.join(",", list);
            join = new StringBuilder(String.valueOf(join).length() + 2).append("(").append(join).append(")").toString();
            if (zza(new StringBuilder(String.valueOf(join).length() + 80).append("SELECT COUNT(1) FROM queue WHERE rowid IN ").append(join).append(" AND retry_count =  2147483647 LIMIT 1").toString(), null) > 0) {
                zzgo().zzjg().zzbx("The number of upload retries exceeds the limit. Will remain unchanged.");
            }
            try {
                getWritableDatabase().execSQL(new StringBuilder(String.valueOf(join).length() + 127).append("UPDATE queue SET retry_count = IFNULL(retry_count, 0) + 1 WHERE rowid IN ").append(join).append(" AND (retry_count IS NULL OR retry_count < 2147483647)").toString());
            } catch (SQLiteException e) {
                zzgo().zzjd().zzg("Error incrementing retry count. error", e);
            }
        }
    }

    final void zza(String str, zzfu[] com_google_android_gms_internal_measurement_zzfuArr) {
        int i = 0;
        zzcl();
        zzaf();
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzfuArr);
        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.beginTransaction();
        try {
            int i2;
            zzcl();
            zzaf();
            Preconditions.checkNotEmpty(str);
            SQLiteDatabase writableDatabase2 = getWritableDatabase();
            writableDatabase2.delete("property_filters", "app_id=?", new String[]{str});
            writableDatabase2.delete("event_filters", "app_id=?", new String[]{str});
            for (zzfu com_google_android_gms_internal_measurement_zzfu : com_google_android_gms_internal_measurement_zzfuArr) {
                zzcl();
                zzaf();
                Preconditions.checkNotEmpty(str);
                Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzfu);
                Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzfu.zzava);
                Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzfu.zzauz);
                if (com_google_android_gms_internal_measurement_zzfu.zzauy == null) {
                    zzgo().zzjg().zzg("Audience with no ID. appId", zzap.zzbv(str));
                } else {
                    int intValue = com_google_android_gms_internal_measurement_zzfu.zzauy.intValue();
                    for (zzfv com_google_android_gms_internal_measurement_zzfv : com_google_android_gms_internal_measurement_zzfu.zzava) {
                        if (com_google_android_gms_internal_measurement_zzfv.zzave == null) {
                            zzgo().zzjg().zze("Event filter with no ID. Audience definition ignored. appId, audienceId", zzap.zzbv(str), com_google_android_gms_internal_measurement_zzfu.zzauy);
                            break;
                        }
                    }
                    for (zzfy com_google_android_gms_internal_measurement_zzfy : com_google_android_gms_internal_measurement_zzfu.zzauz) {
                        if (com_google_android_gms_internal_measurement_zzfy.zzave == null) {
                            zzgo().zzjg().zze("Property filter with no ID. Audience definition ignored. appId, audienceId", zzap.zzbv(str), com_google_android_gms_internal_measurement_zzfu.zzauy);
                            break;
                        }
                    }
                    for (zzfv com_google_android_gms_internal_measurement_zzfv2 : com_google_android_gms_internal_measurement_zzfu.zzava) {
                        if (!zza(str, intValue, com_google_android_gms_internal_measurement_zzfv2)) {
                            i2 = 0;
                            break;
                        }
                    }
                    i2 = 1;
                    if (i2 != 0) {
                        for (zzfy com_google_android_gms_internal_measurement_zzfy2 : com_google_android_gms_internal_measurement_zzfu.zzauz) {
                            if (!zza(str, intValue, com_google_android_gms_internal_measurement_zzfy2)) {
                                i2 = 0;
                                break;
                            }
                        }
                    }
                    if (i2 == 0) {
                        zzcl();
                        zzaf();
                        Preconditions.checkNotEmpty(str);
                        SQLiteDatabase writableDatabase3 = getWritableDatabase();
                        writableDatabase3.delete("property_filters", "app_id=? and audience_id=?", new String[]{str, String.valueOf(intValue)});
                        writableDatabase3.delete("event_filters", "app_id=? and audience_id=?", new String[]{str, String.valueOf(intValue)});
                    }
                }
            }
            List arrayList = new ArrayList();
            i2 = com_google_android_gms_internal_measurement_zzfuArr.length;
            while (i < i2) {
                arrayList.add(com_google_android_gms_internal_measurement_zzfuArr[i].zzauy);
                i++;
            }
            zza(str, arrayList);
            writableDatabase.setTransactionSuccessful();
        } finally {
            writableDatabase.endTransaction();
        }
    }

    private final boolean zza(String str, int i, zzfv com_google_android_gms_internal_measurement_zzfv) {
        zzcl();
        zzaf();
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzfv);
        if (TextUtils.isEmpty(com_google_android_gms_internal_measurement_zzfv.zzavf)) {
            zzgo().zzjg().zzd("Event filter had no event name. Audience definition ignored. appId, audienceId, filterId", zzap.zzbv(str), Integer.valueOf(i), String.valueOf(com_google_android_gms_internal_measurement_zzfv.zzave));
            return false;
        }
        try {
            byte[] bArr = new byte[com_google_android_gms_internal_measurement_zzfv.zzvu()];
            zzyy zzk = zzyy.zzk(bArr, 0, bArr.length);
            com_google_android_gms_internal_measurement_zzfv.zza(zzk);
            zzk.zzyt();
            ContentValues contentValues = new ContentValues();
            contentValues.put("app_id", str);
            contentValues.put("audience_id", Integer.valueOf(i));
            contentValues.put("filter_id", com_google_android_gms_internal_measurement_zzfv.zzave);
            contentValues.put("event_name", com_google_android_gms_internal_measurement_zzfv.zzavf);
            contentValues.put(DataSchemeDataSource.SCHEME_DATA, bArr);
            try {
                if (getWritableDatabase().insertWithOnConflict("event_filters", null, contentValues, 5) == -1) {
                    zzgo().zzjd().zzg("Failed to insert event filter (got -1). appId", zzap.zzbv(str));
                }
                return true;
            } catch (SQLiteException e) {
                zzgo().zzjd().zze("Error storing event filter. appId", zzap.zzbv(str), e);
                return false;
            }
        } catch (IOException e2) {
            zzgo().zzjd().zze("Configuration loss. Failed to serialize event filter. appId", zzap.zzbv(str), e2);
            return false;
        }
    }

    private final boolean zza(String str, int i, zzfy com_google_android_gms_internal_measurement_zzfy) {
        zzcl();
        zzaf();
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzfy);
        if (TextUtils.isEmpty(com_google_android_gms_internal_measurement_zzfy.zzavu)) {
            zzgo().zzjg().zzd("Property filter had no property name. Audience definition ignored. appId, audienceId, filterId", zzap.zzbv(str), Integer.valueOf(i), String.valueOf(com_google_android_gms_internal_measurement_zzfy.zzave));
            return false;
        }
        try {
            byte[] bArr = new byte[com_google_android_gms_internal_measurement_zzfy.zzvu()];
            zzyy zzk = zzyy.zzk(bArr, 0, bArr.length);
            com_google_android_gms_internal_measurement_zzfy.zza(zzk);
            zzk.zzyt();
            ContentValues contentValues = new ContentValues();
            contentValues.put("app_id", str);
            contentValues.put("audience_id", Integer.valueOf(i));
            contentValues.put("filter_id", com_google_android_gms_internal_measurement_zzfy.zzave);
            contentValues.put("property_name", com_google_android_gms_internal_measurement_zzfy.zzavu);
            contentValues.put(DataSchemeDataSource.SCHEME_DATA, bArr);
            try {
                if (getWritableDatabase().insertWithOnConflict("property_filters", null, contentValues, 5) != -1) {
                    return true;
                }
                zzgo().zzjd().zzg("Failed to insert property filter (got -1). appId", zzap.zzbv(str));
                return false;
            } catch (SQLiteException e) {
                zzgo().zzjd().zze("Error storing property filter. appId", zzap.zzbv(str), e);
                return false;
            }
        } catch (IOException e2) {
            zzgo().zzjd().zze("Configuration loss. Failed to serialize property filter. appId", zzap.zzbv(str), e2);
            return false;
        }
    }

    final Map<Integer, List<zzfv>> zzl(String str, String str2) {
        Object e;
        Throwable th;
        zzcl();
        zzaf();
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotEmpty(str2);
        Map<Integer, List<zzfv>> arrayMap = new ArrayMap();
        Cursor query;
        try {
            query = getWritableDatabase().query("event_filters", new String[]{"audience_id", DataSchemeDataSource.SCHEME_DATA}, "app_id=? AND event_name=?", new String[]{str, str2}, null, null, null);
            if (query.moveToFirst()) {
                do {
                    byte[] blob = query.getBlob(1);
                    zzyx zzj = zzyx.zzj(blob, 0, blob.length);
                    zzzg com_google_android_gms_internal_measurement_zzfv = new zzfv();
                    try {
                        com_google_android_gms_internal_measurement_zzfv.zza(zzj);
                        int i = query.getInt(0);
                        List list = (List) arrayMap.get(Integer.valueOf(i));
                        if (list == null) {
                            list = new ArrayList();
                            arrayMap.put(Integer.valueOf(i), list);
                        }
                        list.add(com_google_android_gms_internal_measurement_zzfv);
                    } catch (IOException e2) {
                        try {
                            zzgo().zzjd().zze("Failed to merge filter. appId", zzap.zzbv(str), e2);
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
            Map<Integer, List<zzfv>> emptyMap = Collections.emptyMap();
            if (query == null) {
                return emptyMap;
            }
            query.close();
            return emptyMap;
        } catch (SQLiteException e4) {
            e = e4;
            query = null;
            try {
                zzgo().zzjd().zze("Database error querying filters. appId", zzap.zzbv(str), e);
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

    final Map<Integer, List<zzfy>> zzm(String str, String str2) {
        Cursor query;
        Object e;
        Throwable th;
        zzcl();
        zzaf();
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotEmpty(str2);
        Map<Integer, List<zzfy>> arrayMap = new ArrayMap();
        try {
            query = getWritableDatabase().query("property_filters", new String[]{"audience_id", DataSchemeDataSource.SCHEME_DATA}, "app_id=? AND property_name=?", new String[]{str, str2}, null, null, null);
            if (query.moveToFirst()) {
                do {
                    byte[] blob = query.getBlob(1);
                    zzyx zzj = zzyx.zzj(blob, 0, blob.length);
                    zzzg com_google_android_gms_internal_measurement_zzfy = new zzfy();
                    try {
                        com_google_android_gms_internal_measurement_zzfy.zza(zzj);
                        int i = query.getInt(0);
                        List list = (List) arrayMap.get(Integer.valueOf(i));
                        if (list == null) {
                            list = new ArrayList();
                            arrayMap.put(Integer.valueOf(i), list);
                        }
                        list.add(com_google_android_gms_internal_measurement_zzfy);
                    } catch (IOException e2) {
                        try {
                            zzgo().zzjd().zze("Failed to merge filter", zzap.zzbv(str), e2);
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
            Map<Integer, List<zzfy>> emptyMap = Collections.emptyMap();
            if (query == null) {
                return emptyMap;
            }
            query.close();
            return emptyMap;
        } catch (SQLiteException e4) {
            e = e4;
            query = null;
            try {
                zzgo().zzjd().zze("Database error querying filters. appId", zzap.zzbv(str), e);
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

    private final boolean zza(String str, List<Integer> list) {
        Preconditions.checkNotEmpty(str);
        zzcl();
        zzaf();
        SQLiteDatabase writableDatabase = getWritableDatabase();
        try {
            if (zza("select count(1) from audience_filter_values where app_id=?", new String[]{str}) <= ((long) Math.max(0, Math.min(2000, zzgq().zzb(str, zzaf.zzaki))))) {
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
            if (writableDatabase.delete("audience_filter_values", new StringBuilder(String.valueOf(join).length() + 140).append("audience_id in (select audience_id from audience_filter_values where app_id=? and audience_id not in ").append(join).append(" order by rowid desc limit -1 offset ?)").toString(), new String[]{str, Integer.toString(r5)}) > 0) {
                return true;
            }
            return false;
        } catch (SQLiteException e) {
            zzgo().zzjd().zze("Database error querying filters. appId", zzap.zzbv(str), e);
            return false;
        }
    }

    final Map<Integer, zzgj> zzbo(String str) {
        Object e;
        Throwable th;
        zzcl();
        zzaf();
        Preconditions.checkNotEmpty(str);
        Cursor query;
        try {
            query = getWritableDatabase().query("audience_filter_values", new String[]{"audience_id", "current_results"}, "app_id=?", new String[]{str}, null, null, null);
            if (query.moveToFirst()) {
                Map<Integer, zzgj> arrayMap = new ArrayMap();
                do {
                    int i = query.getInt(0);
                    byte[] blob = query.getBlob(1);
                    zzyx zzj = zzyx.zzj(blob, 0, blob.length);
                    zzzg com_google_android_gms_internal_measurement_zzgj = new zzgj();
                    try {
                        com_google_android_gms_internal_measurement_zzgj.zza(zzj);
                        arrayMap.put(Integer.valueOf(i), com_google_android_gms_internal_measurement_zzgj);
                    } catch (IOException e2) {
                        try {
                            zzgo().zzjd().zzd("Failed to merge filter results. appId, audienceId, error", zzap.zzbv(str), Integer.valueOf(i), e2);
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
                zzgo().zzjd().zze("Database error querying filter results. appId", zzap.zzbv(str), e);
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

    private final Object zza(Cursor cursor, int i) {
        int type = cursor.getType(i);
        switch (type) {
            case 0:
                zzgo().zzjd().zzbx("Loaded invalid null value from database");
                return null;
            case 1:
                return Long.valueOf(cursor.getLong(i));
            case 2:
                return Double.valueOf(cursor.getDouble(i));
            case 3:
                return cursor.getString(i);
            case 4:
                zzgo().zzjd().zzbx("Loaded invalid blob type value, ignoring it");
                return null;
            default:
                zzgo().zzjd().zzg("Loaded invalid unknown value type, ignoring it", Integer.valueOf(type));
                return null;
        }
    }

    public final long zzig() {
        return zza("select max(bundle_end_timestamp) from queue", null, 0);
    }

    protected final long zzn(String str, String str2) {
        long zza;
        Object e;
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotEmpty(str2);
        zzaf();
        zzcl();
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
                    zzgo().zzjd().zze("Failed to insert column (got -1). appId", zzap.zzbv(str), str2);
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
                    zzgo().zzjd().zze("Failed to update column (got 0). appId", zzap.zzbv(str), str2);
                    writableDatabase.endTransaction();
                    return -1;
                }
                writableDatabase.setTransactionSuccessful();
                writableDatabase.endTransaction();
                return zza;
            } catch (SQLiteException e2) {
                e = e2;
                try {
                    zzgo().zzjd().zzd("Error inserting column. appId", zzap.zzbv(str), str2, e);
                    return zza;
                } finally {
                    writableDatabase.endTransaction();
                }
            }
        } catch (SQLiteException e3) {
            e = e3;
            zza = 0;
            zzgo().zzjd().zzd("Error inserting column. appId", zzap.zzbv(str), str2, e);
            return zza;
        }
    }

    public final long zzih() {
        return zza("select max(timestamp) from raw_events", null, 0);
    }

    public final long zza(zzgi com_google_android_gms_internal_measurement_zzgi) throws IOException {
        zzaf();
        zzcl();
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzgi);
        Preconditions.checkNotEmpty(com_google_android_gms_internal_measurement_zzgi.zztt);
        try {
            long j;
            Object obj = new byte[com_google_android_gms_internal_measurement_zzgi.zzvu()];
            zzyy zzk = zzyy.zzk(obj, 0, obj.length);
            com_google_android_gms_internal_measurement_zzgi.zza(zzk);
            zzk.zzyt();
            zzco zzjo = zzjo();
            Preconditions.checkNotNull(obj);
            zzjo.zzgm().zzaf();
            MessageDigest messageDigest = zzfk.getMessageDigest();
            if (messageDigest == null) {
                zzjo.zzgo().zzjd().zzbx("Failed to get MD5");
                j = 0;
            } else {
                j = zzfk.zzc(messageDigest.digest(obj));
            }
            ContentValues contentValues = new ContentValues();
            contentValues.put("app_id", com_google_android_gms_internal_measurement_zzgi.zztt);
            contentValues.put("metadata_fingerprint", Long.valueOf(j));
            contentValues.put(TtmlNode.TAG_METADATA, obj);
            try {
                getWritableDatabase().insertWithOnConflict("raw_events_metadata", null, contentValues, 4);
                return j;
            } catch (SQLiteException e) {
                zzgo().zzjd().zze("Error storing raw event metadata. appId", zzap.zzbv(com_google_android_gms_internal_measurement_zzgi.zztt), e);
                throw e;
            }
        } catch (IOException e2) {
            zzgo().zzjd().zze("Data loss. Failed to serialize event metadata. appId", zzap.zzbv(com_google_android_gms_internal_measurement_zzgi.zztt), e2);
            throw e2;
        }
    }

    public final boolean zzii() {
        return zza("select count(1) > 0 from raw_events", null) != 0;
    }

    public final boolean zzij() {
        return zza("select count(1) > 0 from raw_events where realtime = 1", null) != 0;
    }

    public final long zzbp(String str) {
        Preconditions.checkNotEmpty(str);
        return zza("select count(1) from events where app_id=? and name not like '!_%' escape '!'", new String[]{str}, 0);
    }

    public final String zzah(long j) {
        Cursor rawQuery;
        Object e;
        Throwable th;
        String str = null;
        zzaf();
        zzcl();
        try {
            rawQuery = getWritableDatabase().rawQuery("select app_id from apps where app_id in (select distinct app_id from raw_events) and config_fetched_time < ? order by failed_config_fetch_time limit 1;", new String[]{String.valueOf(j)});
            try {
                if (rawQuery.moveToFirst()) {
                    str = rawQuery.getString(0);
                    if (rawQuery != null) {
                        rawQuery.close();
                    }
                } else {
                    zzgo().zzjl().zzbx("No expired configs for apps with pending events");
                    if (rawQuery != null) {
                        rawQuery.close();
                    }
                }
            } catch (SQLiteException e2) {
                e = e2;
                try {
                    zzgo().zzjd().zzg("Error selecting expired configs", e);
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
            zzgo().zzjd().zzg("Error selecting expired configs", e);
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

    public final long zzik() {
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
            zzgo().zzjd().zzg("Error querying raw events", e);
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

    public final Pair<zzgf, Long> zza(String str, Long l) {
        Cursor rawQuery;
        Object e;
        Throwable th;
        Pair<zzgf, Long> pair = null;
        zzaf();
        zzcl();
        try {
            rawQuery = getWritableDatabase().rawQuery("select main_event, children_to_process from main_event_params where app_id=? and event_id=?", new String[]{str, String.valueOf(l)});
            try {
                if (rawQuery.moveToFirst()) {
                    byte[] blob = rawQuery.getBlob(0);
                    Long valueOf = Long.valueOf(rawQuery.getLong(1));
                    zzyx zzj = zzyx.zzj(blob, 0, blob.length);
                    zzzg com_google_android_gms_internal_measurement_zzgf = new zzgf();
                    try {
                        com_google_android_gms_internal_measurement_zzgf.zza(zzj);
                        pair = Pair.create(com_google_android_gms_internal_measurement_zzgf, valueOf);
                        if (rawQuery != null) {
                            rawQuery.close();
                        }
                    } catch (IOException e2) {
                        zzgo().zzjd().zzd("Failed to merge main event. appId, eventId", zzap.zzbv(str), l, e2);
                        if (rawQuery != null) {
                            rawQuery.close();
                        }
                    }
                } else {
                    zzgo().zzjl().zzbx("Main event not found");
                    if (rawQuery != null) {
                        rawQuery.close();
                    }
                }
            } catch (SQLiteException e3) {
                e = e3;
                try {
                    zzgo().zzjd().zzg("Error selecting main event", e);
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
            zzgo().zzjd().zzg("Error selecting main event", e);
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

    public final boolean zza(String str, Long l, long j, zzgf com_google_android_gms_internal_measurement_zzgf) {
        zzaf();
        zzcl();
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzgf);
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotNull(l);
        try {
            byte[] bArr = new byte[com_google_android_gms_internal_measurement_zzgf.zzvu()];
            zzyy zzk = zzyy.zzk(bArr, 0, bArr.length);
            com_google_android_gms_internal_measurement_zzgf.zza(zzk);
            zzk.zzyt();
            zzgo().zzjl().zze("Saving complex main event, appId, data size", zzgl().zzbs(str), Integer.valueOf(bArr.length));
            ContentValues contentValues = new ContentValues();
            contentValues.put("app_id", str);
            contentValues.put("event_id", l);
            contentValues.put("children_to_process", Long.valueOf(j));
            contentValues.put("main_event", bArr);
            try {
                if (getWritableDatabase().insertWithOnConflict("main_event_params", null, contentValues, 5) != -1) {
                    return true;
                }
                zzgo().zzjd().zzg("Failed to insert complex main event (got -1). appId", zzap.zzbv(str));
                return false;
            } catch (SQLiteException e) {
                zzgo().zzjd().zze("Error storing complex main event. appId", zzap.zzbv(str), e);
                return false;
            }
        } catch (IOException e2) {
            zzgo().zzjd().zzd("Data loss. Failed to serialize event params/data. appId, eventId", zzap.zzbv(str), l, e2);
            return false;
        }
    }

    public final boolean zza(zzy com_google_android_gms_measurement_internal_zzy, long j, boolean z) {
        zzaf();
        zzcl();
        Preconditions.checkNotNull(com_google_android_gms_measurement_internal_zzy);
        Preconditions.checkNotEmpty(com_google_android_gms_measurement_internal_zzy.zztt);
        zzzg com_google_android_gms_internal_measurement_zzgf = new zzgf();
        com_google_android_gms_internal_measurement_zzgf.zzawv = Long.valueOf(com_google_android_gms_measurement_internal_zzy.zzaic);
        com_google_android_gms_internal_measurement_zzgf.zzawt = new zzgg[com_google_android_gms_measurement_internal_zzy.zzaid.size()];
        Iterator it = com_google_android_gms_measurement_internal_zzy.zzaid.iterator();
        int i = 0;
        while (it.hasNext()) {
            String str = (String) it.next();
            zzgg com_google_android_gms_internal_measurement_zzgg = new zzgg();
            int i2 = i + 1;
            com_google_android_gms_internal_measurement_zzgf.zzawt[i] = com_google_android_gms_internal_measurement_zzgg;
            com_google_android_gms_internal_measurement_zzgg.name = str;
            zzjo().zza(com_google_android_gms_internal_measurement_zzgg, com_google_android_gms_measurement_internal_zzy.zzaid.get(str));
            i = i2;
        }
        try {
            byte[] bArr = new byte[com_google_android_gms_internal_measurement_zzgf.zzvu()];
            zzyy zzk = zzyy.zzk(bArr, 0, bArr.length);
            com_google_android_gms_internal_measurement_zzgf.zza(zzk);
            zzk.zzyt();
            zzgo().zzjl().zze("Saving event, name, data size", zzgl().zzbs(com_google_android_gms_measurement_internal_zzy.name), Integer.valueOf(bArr.length));
            ContentValues contentValues = new ContentValues();
            contentValues.put("app_id", com_google_android_gms_measurement_internal_zzy.zztt);
            contentValues.put("name", com_google_android_gms_measurement_internal_zzy.name);
            contentValues.put("timestamp", Long.valueOf(com_google_android_gms_measurement_internal_zzy.timestamp));
            contentValues.put("metadata_fingerprint", Long.valueOf(j));
            contentValues.put(DataSchemeDataSource.SCHEME_DATA, bArr);
            contentValues.put("realtime", Integer.valueOf(z ? 1 : 0));
            try {
                if (getWritableDatabase().insert("raw_events", null, contentValues) != -1) {
                    return true;
                }
                zzgo().zzjd().zzg("Failed to insert raw event (got -1). appId", zzap.zzbv(com_google_android_gms_measurement_internal_zzy.zztt));
                return false;
            } catch (SQLiteException e) {
                zzgo().zzjd().zze("Error storing raw event. appId", zzap.zzbv(com_google_android_gms_measurement_internal_zzy.zztt), e);
                return false;
            }
        } catch (IOException e2) {
            zzgo().zzjd().zze("Data loss. Failed to serialize event params/data. appId", zzap.zzbv(com_google_android_gms_measurement_internal_zzy.zztt), e2);
            return false;
        }
    }

    private final boolean zzil() {
        return getContext().getDatabasePath("google_app_measurement.db").exists();
    }
}
