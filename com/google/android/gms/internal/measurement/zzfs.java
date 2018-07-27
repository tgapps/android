package com.google.android.gms.internal.measurement;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.util.Pair;

final class zzfs extends zzhi {
    static final Pair<String, Long> zzakb = new Pair(TtmlNode.ANONYMOUS_REGION_ID, Long.valueOf(0));
    private SharedPreferences zzabf;
    public zzfw zzakc;
    public final zzfv zzakd = new zzfv(this, "last_upload", 0);
    public final zzfv zzake = new zzfv(this, "last_upload_attempt", 0);
    public final zzfv zzakf = new zzfv(this, "backoff", 0);
    public final zzfv zzakg = new zzfv(this, "last_delete_stale", 0);
    public final zzfv zzakh = new zzfv(this, "midnight_offset", 0);
    public final zzfv zzaki = new zzfv(this, "first_open_time", 0);
    public final zzfv zzakj = new zzfv(this, "app_install_time", 0);
    public final zzfx zzakk = new zzfx(this, "app_instance_id", null);
    private String zzako;
    private long zzakp;
    private final Object zzakq = new Object();
    public final zzfv zzakr = new zzfv(this, "time_before_start", 10000);
    public final zzfv zzaks = new zzfv(this, "session_timeout", 1800000);
    public final zzfu zzakt = new zzfu(this, "start_new_session", true);
    public final zzfv zzaku = new zzfv(this, "last_pause_time", 0);
    public final zzfv zzakv = new zzfv(this, "time_active", 0);
    public boolean zzakw;

    zzfs(zzgm com_google_android_gms_internal_measurement_zzgm) {
        super(com_google_android_gms_internal_measurement_zzgm);
    }

    private final SharedPreferences zzjf() {
        zzab();
        zzch();
        return this.zzabf;
    }

    final void setMeasurementEnabled(boolean z) {
        zzab();
        zzgf().zziz().zzg("Setting measurementEnabled", Boolean.valueOf(z));
        Editor edit = zzjf().edit();
        edit.putBoolean("measurement_enabled", z);
        edit.apply();
    }

    final void zzbp(String str) {
        zzab();
        Editor edit = zzjf().edit();
        edit.putString("gmp_app_id", str);
        edit.apply();
    }

    final void zzbq(String str) {
        synchronized (this.zzakq) {
            this.zzako = str;
            this.zzakp = zzbt().elapsedRealtime();
        }
    }

    final void zzf(boolean z) {
        zzab();
        zzgf().zziz().zzg("Setting useService", Boolean.valueOf(z));
        Editor edit = zzjf().edit();
        edit.putBoolean("use_service", z);
        edit.apply();
    }

    final boolean zzg(boolean z) {
        zzab();
        return zzjf().getBoolean("measurement_enabled", z);
    }

    final void zzh(boolean z) {
        zzab();
        zzgf().zziz().zzg("Updating deferred analytics collection", Boolean.valueOf(z));
        Editor edit = zzjf().edit();
        edit.putBoolean("deferred_analytics_collection", z);
        edit.apply();
    }

    protected final boolean zzhh() {
        return true;
    }

    protected final void zzin() {
        this.zzabf = getContext().getSharedPreferences("com.google.android.gms.measurement.prefs", 0);
        this.zzakw = this.zzabf.getBoolean("has_been_opened", false);
        if (!this.zzakw) {
            Editor edit = this.zzabf.edit();
            edit.putBoolean("has_been_opened", true);
            edit.apply();
        }
        this.zzakc = new zzfw(this, "health_monitor", Math.max(0, ((Long) zzey.zzagr.get()).longValue()));
    }

    final String zzjg() {
        zzab();
        return zzjf().getString("gmp_app_id", null);
    }

    final String zzjh() {
        String str;
        synchronized (this.zzakq) {
            if (Math.abs(zzbt().elapsedRealtime() - this.zzakp) < 1000) {
                str = this.zzako;
            } else {
                str = null;
            }
        }
        return str;
    }

    final Boolean zzji() {
        zzab();
        return !zzjf().contains("use_service") ? null : Boolean.valueOf(zzjf().getBoolean("use_service", false));
    }

    final void zzjj() {
        boolean z = true;
        zzab();
        zzgf().zziz().log("Clearing collection preferences.");
        boolean contains = zzjf().contains("measurement_enabled");
        if (contains) {
            z = zzg(true);
        }
        Editor edit = zzjf().edit();
        edit.clear();
        edit.apply();
        if (contains) {
            setMeasurementEnabled(z);
        }
    }

    protected final String zzjk() {
        zzab();
        String string = zzjf().getString("previous_os_version", null);
        zzfx().zzch();
        String str = VERSION.RELEASE;
        if (!(TextUtils.isEmpty(str) || str.equals(string))) {
            Editor edit = zzjf().edit();
            edit.putString("previous_os_version", str);
            edit.apply();
        }
        return string;
    }

    final boolean zzjl() {
        zzab();
        return zzjf().getBoolean("deferred_analytics_collection", false);
    }

    final boolean zzjm() {
        return this.zzabf.contains("deferred_analytics_collection");
    }
}
