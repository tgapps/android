package com.google.android.gms.internal.measurement;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.util.Pair;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.ads.identifier.AdvertisingIdClient.Info;
import java.math.BigInteger;
import java.util.Locale;

final class zzfr extends zzhh {
    static final Pair<String, Long> zzajs = new Pair(TtmlNode.ANONYMOUS_REGION_ID, Long.valueOf(0));
    private SharedPreferences zzabf;
    public zzfv zzajt;
    public final zzfu zzaju = new zzfu(this, "last_upload", 0);
    public final zzfu zzajv = new zzfu(this, "last_upload_attempt", 0);
    public final zzfu zzajw = new zzfu(this, "backoff", 0);
    public final zzfu zzajx = new zzfu(this, "last_delete_stale", 0);
    public final zzfu zzajy = new zzfu(this, "midnight_offset", 0);
    public final zzfu zzajz = new zzfu(this, "first_open_time", 0);
    public final zzfu zzaka = new zzfu(this, "app_install_time", 0);
    public final zzfw zzakb = new zzfw(this, "app_instance_id", null);
    private String zzakc;
    private boolean zzakd;
    private long zzake;
    private String zzakf;
    private long zzakg;
    private final Object zzakh = new Object();
    public final zzfu zzaki = new zzfu(this, "time_before_start", 10000);
    public final zzfu zzakj = new zzfu(this, "session_timeout", 1800000);
    public final zzft zzakk = new zzft(this, "start_new_session", true);
    public final zzfu zzakl = new zzfu(this, "last_pause_time", 0);
    public final zzfu zzakm = new zzfu(this, "time_active", 0);
    public boolean zzakn;

    zzfr(zzgl com_google_android_gms_internal_measurement_zzgl) {
        super(com_google_android_gms_internal_measurement_zzgl);
    }

    private final SharedPreferences zziy() {
        zzab();
        zzch();
        return this.zzabf;
    }

    final void setMeasurementEnabled(boolean z) {
        zzab();
        zzge().zzit().zzg("Setting measurementEnabled", Boolean.valueOf(z));
        Editor edit = zziy().edit();
        edit.putBoolean("measurement_enabled", z);
        edit.apply();
    }

    final Pair<String, Boolean> zzbo(String str) {
        zzab();
        long elapsedRealtime = zzbt().elapsedRealtime();
        if (this.zzakc != null && elapsedRealtime < this.zzake) {
            return new Pair(this.zzakc, Boolean.valueOf(this.zzakd));
        }
        this.zzake = elapsedRealtime + zzgg().zza(str, zzew.zzagj);
        AdvertisingIdClient.setShouldSkipGmsCoreVersionCheck(true);
        try {
            Info advertisingIdInfo = AdvertisingIdClient.getAdvertisingIdInfo(getContext());
            if (advertisingIdInfo != null) {
                this.zzakc = advertisingIdInfo.getId();
                this.zzakd = advertisingIdInfo.isLimitAdTrackingEnabled();
            }
            if (this.zzakc == null) {
                this.zzakc = TtmlNode.ANONYMOUS_REGION_ID;
            }
        } catch (Exception e) {
            zzge().zzis().zzg("Unable to get advertising id", e);
            this.zzakc = TtmlNode.ANONYMOUS_REGION_ID;
        }
        AdvertisingIdClient.setShouldSkipGmsCoreVersionCheck(false);
        return new Pair(this.zzakc, Boolean.valueOf(this.zzakd));
    }

    final String zzbp(String str) {
        zzab();
        String str2 = (String) zzbo(str).first;
        if (zzka.getMessageDigest("MD5") == null) {
            return null;
        }
        return String.format(Locale.US, "%032X", new Object[]{new BigInteger(1, zzka.getMessageDigest("MD5").digest(str2.getBytes()))});
    }

    final void zzbq(String str) {
        zzab();
        Editor edit = zziy().edit();
        edit.putString("gmp_app_id", str);
        edit.apply();
    }

    final void zzbr(String str) {
        synchronized (this.zzakh) {
            this.zzakf = str;
            this.zzakg = zzbt().elapsedRealtime();
        }
    }

    final void zzf(boolean z) {
        zzab();
        zzge().zzit().zzg("Setting useService", Boolean.valueOf(z));
        Editor edit = zziy().edit();
        edit.putBoolean("use_service", z);
        edit.apply();
    }

    final boolean zzg(boolean z) {
        zzab();
        return zziy().getBoolean("measurement_enabled", z);
    }

    final void zzh(boolean z) {
        zzab();
        zzge().zzit().zzg("Updating deferred analytics collection", Boolean.valueOf(z));
        Editor edit = zziy().edit();
        edit.putBoolean("deferred_analytics_collection", z);
        edit.apply();
    }

    protected final boolean zzhf() {
        return true;
    }

    protected final void zzih() {
        this.zzabf = getContext().getSharedPreferences("com.google.android.gms.measurement.prefs", 0);
        this.zzakn = this.zzabf.getBoolean("has_been_opened", false);
        if (!this.zzakn) {
            Editor edit = this.zzabf.edit();
            edit.putBoolean("has_been_opened", true);
            edit.apply();
        }
        this.zzajt = new zzfv(this, "health_monitor", Math.max(0, ((Long) zzew.zzagk.get()).longValue()));
    }

    final String zziz() {
        zzab();
        return zziy().getString("gmp_app_id", null);
    }

    final String zzja() {
        String str;
        synchronized (this.zzakh) {
            if (Math.abs(zzbt().elapsedRealtime() - this.zzakg) < 1000) {
                str = this.zzakf;
            } else {
                str = null;
            }
        }
        return str;
    }

    final Boolean zzjb() {
        zzab();
        return !zziy().contains("use_service") ? null : Boolean.valueOf(zziy().getBoolean("use_service", false));
    }

    final void zzjc() {
        boolean z = true;
        zzab();
        zzge().zzit().log("Clearing collection preferences.");
        boolean contains = zziy().contains("measurement_enabled");
        if (contains) {
            z = zzg(true);
        }
        Editor edit = zziy().edit();
        edit.clear();
        edit.apply();
        if (contains) {
            setMeasurementEnabled(z);
        }
    }

    protected final String zzjd() {
        zzab();
        String string = zziy().getString("previous_os_version", null);
        zzfw().zzch();
        String str = VERSION.RELEASE;
        if (!(TextUtils.isEmpty(str) || str.equals(string))) {
            Editor edit = zziy().edit();
            edit.putString("previous_os_version", str);
            edit.apply();
        }
        return string;
    }

    final boolean zzje() {
        zzab();
        return zziy().getBoolean("deferred_analytics_collection", false);
    }

    final boolean zzjf() {
        return this.zzabf.contains("deferred_analytics_collection");
    }
}
