package com.google.android.gms.internal.measurement;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.util.Pair;

final class zzfr extends zzhk {
    static final Pair<String, Long> zzajr = new Pair(TtmlNode.ANONYMOUS_REGION_ID, Long.valueOf(0));
    private SharedPreferences zzaba;
    public zzfv zzajs;
    public final zzfu zzajt = new zzfu(this, "last_upload", 0);
    public final zzfu zzaju = new zzfu(this, "last_upload_attempt", 0);
    public final zzfu zzajv = new zzfu(this, "backoff", 0);
    public final zzfu zzajw = new zzfu(this, "last_delete_stale", 0);
    public final zzfu zzajx = new zzfu(this, "midnight_offset", 0);
    public final zzfu zzajy = new zzfu(this, "first_open_time", 0);
    public final zzfu zzajz = new zzfu(this, "app_install_time", 0);
    public final zzfw zzaka = new zzfw(this, "app_instance_id", null);
    private String zzake;
    private long zzakf;
    private final Object zzakg = new Object();
    public final zzfu zzakh = new zzfu(this, "time_before_start", 10000);
    public final zzfu zzaki = new zzfu(this, "session_timeout", 1800000);
    public final zzft zzakj = new zzft(this, "start_new_session", true);
    public final zzfu zzakk = new zzfu(this, "last_pause_time", 0);
    public final zzfu zzakl = new zzfu(this, "time_active", 0);
    public boolean zzakm;

    zzfr(zzgl com_google_android_gms_internal_measurement_zzgl) {
        super(com_google_android_gms_internal_measurement_zzgl);
    }

    private final SharedPreferences zziu() {
        zzab();
        zzch();
        return this.zzaba;
    }

    final void setMeasurementEnabled(boolean z) {
        zzab();
        zzgg().zzir().zzg("Setting measurementEnabled", Boolean.valueOf(z));
        Editor edit = zziu().edit();
        edit.putBoolean("measurement_enabled", z);
        edit.apply();
    }

    final void zzbl(String str) {
        zzab();
        Editor edit = zziu().edit();
        edit.putString("gmp_app_id", str);
        edit.apply();
    }

    final void zzbm(String str) {
        synchronized (this.zzakg) {
            this.zzake = str;
            this.zzakf = zzbt().elapsedRealtime();
        }
    }

    final void zzf(boolean z) {
        zzab();
        zzgg().zzir().zzg("Setting useService", Boolean.valueOf(z));
        Editor edit = zziu().edit();
        edit.putBoolean("use_service", z);
        edit.apply();
    }

    final boolean zzg(boolean z) {
        zzab();
        return zziu().getBoolean("measurement_enabled", z);
    }

    final void zzh(boolean z) {
        zzab();
        zzgg().zzir().zzg("Updating deferred analytics collection", Boolean.valueOf(z));
        Editor edit = zziu().edit();
        edit.putBoolean("deferred_analytics_collection", z);
        edit.apply();
    }

    protected final boolean zzhh() {
        return true;
    }

    protected final void zzig() {
        this.zzaba = getContext().getSharedPreferences("com.google.android.gms.measurement.prefs", 0);
        this.zzakm = this.zzaba.getBoolean("has_been_opened", false);
        if (!this.zzakm) {
            Editor edit = this.zzaba.edit();
            edit.putBoolean("has_been_opened", true);
            edit.apply();
        }
        this.zzajs = new zzfv(this, "health_monitor", Math.max(0, ((Long) zzew.zzagn.get()).longValue()));
    }

    final String zziv() {
        zzab();
        return zziu().getString("gmp_app_id", null);
    }

    final String zziw() {
        String str;
        synchronized (this.zzakg) {
            if (Math.abs(zzbt().elapsedRealtime() - this.zzakf) < 1000) {
                str = this.zzake;
            } else {
                str = null;
            }
        }
        return str;
    }

    final Boolean zzix() {
        zzab();
        return !zziu().contains("use_service") ? null : Boolean.valueOf(zziu().getBoolean("use_service", false));
    }

    final void zziy() {
        boolean z = true;
        zzab();
        zzgg().zzir().log("Clearing collection preferences.");
        boolean contains = zziu().contains("measurement_enabled");
        if (contains) {
            z = zzg(true);
        }
        Editor edit = zziu().edit();
        edit.clear();
        edit.apply();
        if (contains) {
            setMeasurementEnabled(z);
        }
    }

    protected final String zziz() {
        zzab();
        String string = zziu().getString("previous_os_version", null);
        zzfw().zzch();
        String str = VERSION.RELEASE;
        if (!(TextUtils.isEmpty(str) || str.equals(string))) {
            Editor edit = zziu().edit();
            edit.putString("previous_os_version", str);
            edit.apply();
        }
        return string;
    }

    final boolean zzja() {
        zzab();
        return zziu().getBoolean("deferred_analytics_collection", false);
    }

    final boolean zzjb() {
        return this.zzaba.contains("deferred_analytics_collection");
    }
}
