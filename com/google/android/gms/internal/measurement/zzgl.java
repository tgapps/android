package com.google.android.gms.internal.measurement;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.net.Uri.Builder;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Pair;
import com.google.android.gms.common.api.internal.GoogleServices;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.Clock;
import com.google.android.gms.common.util.DefaultClock;
import com.google.android.gms.common.wrappers.Wrappers;
import com.google.android.gms.measurement.AppMeasurement;
import com.google.firebase.analytics.FirebaseAnalytics;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import org.telegram.messenger.support.widget.helper.ItemTouchHelper.Callback;

public class zzgl {
    private static volatile zzgl zzamc;
    private final long zzaeh;
    private final zzeh zzamd;
    private final zzfr zzame;
    private final zzfg zzamf;
    private final zzgg zzamg;
    private final zzjk zzamh;
    private final zzgf zzami;
    private final AppMeasurement zzamj;
    private final FirebaseAnalytics zzamk;
    private final zzjv zzaml;
    private final zzfe zzamm;
    private final zzfk zzamn;
    private final zzih zzamo;
    private final zzhm zzamp;
    private final zzdx zzamq;
    private zzei zzamr;
    private zzfc zzams;
    private zzil zzamt;
    private zzeo zzamu;
    private zzfb zzamv;
    private zzfp zzamw;
    private zzjq zzamx;
    private zzee zzamy;
    private zzfx zzamz;
    private boolean zzana;
    private Boolean zzanb;
    private long zzanc;
    private List<Long> zzanf;
    private List<Runnable> zzang;
    private int zzanh;
    private int zzani;
    private long zzanj;
    private long zzank;
    private boolean zzanl;
    private boolean zzanm;
    private boolean zzann;
    private final Context zzqs;
    private final Clock zzrj;
    private boolean zzvj = false;

    class zza implements zzek {
        private final /* synthetic */ zzgl zzanp;
        zzkl zzans;
        List<Long> zzant;
        List<zzki> zzanu;
        private long zzanv;

        private zza(zzgl com_google_android_gms_internal_measurement_zzgl) {
            this.zzanp = com_google_android_gms_internal_measurement_zzgl;
        }

        private static long zza(zzki com_google_android_gms_internal_measurement_zzki) {
            return ((com_google_android_gms_internal_measurement_zzki.zzasw.longValue() / 1000) / 60) / 60;
        }

        public final boolean zza(long j, zzki com_google_android_gms_internal_measurement_zzki) {
            Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzki);
            if (this.zzanu == null) {
                this.zzanu = new ArrayList();
            }
            if (this.zzant == null) {
                this.zzant = new ArrayList();
            }
            if (this.zzanu.size() > 0 && zza((zzki) this.zzanu.get(0)) != zza(com_google_android_gms_internal_measurement_zzki)) {
                return false;
            }
            long zzwg = this.zzanv + ((long) com_google_android_gms_internal_measurement_zzki.zzwg());
            if (zzwg >= ((long) Math.max(0, ((Integer) zzew.zzagt.get()).intValue()))) {
                return false;
            }
            this.zzanv = zzwg;
            this.zzanu.add(com_google_android_gms_internal_measurement_zzki);
            this.zzant.add(Long.valueOf(j));
            return this.zzanu.size() < Math.max(1, ((Integer) zzew.zzagu.get()).intValue());
        }

        public final void zzb(zzkl com_google_android_gms_internal_measurement_zzkl) {
            Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzkl);
            this.zzans = com_google_android_gms_internal_measurement_zzkl;
        }
    }

    private zzgl(zzhl com_google_android_gms_internal_measurement_zzhl) {
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzhl);
        this.zzqs = com_google_android_gms_internal_measurement_zzhl.zzqs;
        this.zzanj = -1;
        this.zzrj = DefaultClock.getInstance();
        this.zzaeh = this.zzrj.currentTimeMillis();
        this.zzamd = new zzeh(this);
        zzhk com_google_android_gms_internal_measurement_zzfr = new zzfr(this);
        com_google_android_gms_internal_measurement_zzfr.zzm();
        this.zzame = com_google_android_gms_internal_measurement_zzfr;
        com_google_android_gms_internal_measurement_zzfr = new zzfg(this);
        com_google_android_gms_internal_measurement_zzfr.zzm();
        this.zzamf = com_google_android_gms_internal_measurement_zzfr;
        com_google_android_gms_internal_measurement_zzfr = new zzjv(this);
        com_google_android_gms_internal_measurement_zzfr.zzm();
        this.zzaml = com_google_android_gms_internal_measurement_zzfr;
        com_google_android_gms_internal_measurement_zzfr = new zzfe(this);
        com_google_android_gms_internal_measurement_zzfr.zzm();
        this.zzamm = com_google_android_gms_internal_measurement_zzfr;
        this.zzamq = new zzdx(this);
        com_google_android_gms_internal_measurement_zzfr = new zzfk(this);
        com_google_android_gms_internal_measurement_zzfr.zzm();
        this.zzamn = com_google_android_gms_internal_measurement_zzfr;
        com_google_android_gms_internal_measurement_zzfr = new zzih(this);
        com_google_android_gms_internal_measurement_zzfr.zzm();
        this.zzamo = com_google_android_gms_internal_measurement_zzfr;
        com_google_android_gms_internal_measurement_zzfr = new zzhm(this);
        com_google_android_gms_internal_measurement_zzfr.zzm();
        this.zzamp = com_google_android_gms_internal_measurement_zzfr;
        this.zzamj = new AppMeasurement(this);
        this.zzamk = new FirebaseAnalytics(this);
        com_google_android_gms_internal_measurement_zzfr = new zzjk(this);
        com_google_android_gms_internal_measurement_zzfr.zzm();
        this.zzamh = com_google_android_gms_internal_measurement_zzfr;
        com_google_android_gms_internal_measurement_zzfr = new zzgf(this);
        com_google_android_gms_internal_measurement_zzfr.zzm();
        this.zzami = com_google_android_gms_internal_measurement_zzfr;
        com_google_android_gms_internal_measurement_zzfr = new zzgg(this);
        com_google_android_gms_internal_measurement_zzfr.zzm();
        this.zzamg = com_google_android_gms_internal_measurement_zzfr;
        if (this.zzqs.getApplicationContext() instanceof Application) {
            zzhj zzfu = zzfu();
            if (zzfu.getContext().getApplicationContext() instanceof Application) {
                Application application = (Application) zzfu.getContext().getApplicationContext();
                if (zzfu.zzaoi == null) {
                    zzfu.zzaoi = new zzif(zzfu);
                }
                application.unregisterActivityLifecycleCallbacks(zzfu.zzaoi);
                application.registerActivityLifecycleCallbacks(zzfu.zzaoi);
                zzfu.zzgg().zzir().log("Registered activity lifecycle callback");
            }
        } else {
            zzgg().zzin().log("Application context is not an Application");
        }
        this.zzamg.zzc(new zzgm(this, com_google_android_gms_internal_measurement_zzhl));
    }

    private static void zza(zzhj com_google_android_gms_internal_measurement_zzhj) {
        if (com_google_android_gms_internal_measurement_zzhj == null) {
            throw new IllegalStateException("Component not created");
        }
    }

    private static void zza(zzhk com_google_android_gms_internal_measurement_zzhk) {
        if (com_google_android_gms_internal_measurement_zzhk == null) {
            throw new IllegalStateException("Component not created");
        } else if (!com_google_android_gms_internal_measurement_zzhk.isInitialized()) {
            String valueOf = String.valueOf(com_google_android_gms_internal_measurement_zzhk.getClass());
            throw new IllegalStateException(new StringBuilder(String.valueOf(valueOf).length() + 27).append("Component not initialized: ").append(valueOf).toString());
        }
    }

    private final void zza(zzhl com_google_android_gms_internal_measurement_zzhl) {
        zzfi zzip;
        zzgf().zzab();
        zzhk com_google_android_gms_internal_measurement_zzeo = new zzeo(this);
        com_google_android_gms_internal_measurement_zzeo.zzm();
        this.zzamu = com_google_android_gms_internal_measurement_zzeo;
        com_google_android_gms_internal_measurement_zzeo = new zzfb(this);
        com_google_android_gms_internal_measurement_zzeo.zzm();
        this.zzamv = com_google_android_gms_internal_measurement_zzeo;
        zzhk com_google_android_gms_internal_measurement_zzei = new zzei(this);
        com_google_android_gms_internal_measurement_zzei.zzm();
        this.zzamr = com_google_android_gms_internal_measurement_zzei;
        com_google_android_gms_internal_measurement_zzei = new zzfc(this);
        com_google_android_gms_internal_measurement_zzei.zzm();
        this.zzams = com_google_android_gms_internal_measurement_zzei;
        com_google_android_gms_internal_measurement_zzei = new zzee(this);
        com_google_android_gms_internal_measurement_zzei.zzm();
        this.zzamy = com_google_android_gms_internal_measurement_zzei;
        com_google_android_gms_internal_measurement_zzei = new zzil(this);
        com_google_android_gms_internal_measurement_zzei.zzm();
        this.zzamt = com_google_android_gms_internal_measurement_zzei;
        com_google_android_gms_internal_measurement_zzei = new zzjq(this);
        com_google_android_gms_internal_measurement_zzei.zzm();
        this.zzamx = com_google_android_gms_internal_measurement_zzei;
        this.zzamw = new zzfp(this);
        this.zzaml.zzkd();
        this.zzame.zzkd();
        this.zzamz = new zzfx(this);
        this.zzamv.zzkd();
        zzgg().zzip().zzg("App measurement is starting up, version", Long.valueOf(12451));
        zzgg().zzip().log("To enable debug logging run: adb shell setprop log.tag.FA VERBOSE");
        String zzah = com_google_android_gms_internal_measurement_zzeo.zzah();
        if (zzgc().zzcc(zzah)) {
            zzip = zzgg().zzip();
            zzah = "Faster debug mode event logging enabled. To disable, run:\n  adb shell setprop debug.firebase.analytics.app .none.";
        } else {
            zzip = zzgg().zzip();
            String str = "To enable faster debug mode event logging run:\n  adb shell setprop debug.firebase.analytics.app ";
            zzah = String.valueOf(zzah);
            zzah = zzah.length() != 0 ? str.concat(zzah) : new String(str);
        }
        zzip.log(zzah);
        zzgg().zziq().log("Debug-level message logging enabled");
        if (this.zzanh != this.zzani) {
            zzgg().zzil().zze("Not all components initialized", Integer.valueOf(this.zzanh), Integer.valueOf(this.zzani));
        }
        this.zzvj = true;
    }

    private final zzkh[] zza(String str, zzkn[] com_google_android_gms_internal_measurement_zzknArr, zzki[] com_google_android_gms_internal_measurement_zzkiArr) {
        Preconditions.checkNotEmpty(str);
        return zzft().zza(str, com_google_android_gms_internal_measurement_zzkiArr, com_google_android_gms_internal_measurement_zzknArr);
    }

    private final void zzb(zzeb com_google_android_gms_internal_measurement_zzeb) {
        zzgf().zzab();
        if (TextUtils.isEmpty(com_google_android_gms_internal_measurement_zzeb.getGmpAppId())) {
            zzb(com_google_android_gms_internal_measurement_zzeb.zzah(), 204, null, null, null);
            return;
        }
        String gmpAppId = com_google_android_gms_internal_measurement_zzeb.getGmpAppId();
        String appInstanceId = com_google_android_gms_internal_measurement_zzeb.getAppInstanceId();
        Builder builder = new Builder();
        Builder encodedAuthority = builder.scheme((String) zzew.zzagp.get()).encodedAuthority((String) zzew.zzagq.get());
        String str = "config/app/";
        String valueOf = String.valueOf(gmpAppId);
        encodedAuthority.path(valueOf.length() != 0 ? str.concat(valueOf) : new String(str)).appendQueryParameter("app_instance_id", appInstanceId).appendQueryParameter("platform", "android").appendQueryParameter("gmp_version", "12451");
        String uri = builder.build().toString();
        try {
            Map map;
            URL url = new URL(uri);
            zzgg().zzir().zzg("Fetching remote configuration", com_google_android_gms_internal_measurement_zzeb.zzah());
            zzkf zzbp = zzgd().zzbp(com_google_android_gms_internal_measurement_zzeb.zzah());
            CharSequence zzbq = zzgd().zzbq(com_google_android_gms_internal_measurement_zzeb.zzah());
            if (zzbp == null || TextUtils.isEmpty(zzbq)) {
                map = null;
            } else {
                Map arrayMap = new ArrayMap();
                arrayMap.put("If-Modified-Since", zzbq);
                map = arrayMap;
            }
            this.zzanl = true;
            zzhj zzjq = zzjq();
            appInstanceId = com_google_android_gms_internal_measurement_zzeb.zzah();
            zzfm com_google_android_gms_internal_measurement_zzgp = new zzgp(this);
            zzjq.zzab();
            zzjq.zzch();
            Preconditions.checkNotNull(url);
            Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzgp);
            zzjq.zzgf().zzd(new zzfo(zzjq, appInstanceId, url, null, map, com_google_android_gms_internal_measurement_zzgp));
        } catch (MalformedURLException e) {
            zzgg().zzil().zze("Failed to parse config URL. Not fetching. appId", zzfg.zzbh(com_google_android_gms_internal_measurement_zzeb.zzah()), uri);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final boolean zzd(java.lang.String r31, long r32) {
        /*
        r30 = this;
        r2 = r30.zzga();
        r2.beginTransaction();
        r21 = new com.google.android.gms.internal.measurement.zzgl$zza;	 Catch:{ all -> 0x01c5 }
        r2 = 0;
        r0 = r21;
        r1 = r30;
        r0.<init>();	 Catch:{ all -> 0x01c5 }
        r14 = r30.zzga();	 Catch:{ all -> 0x01c5 }
        r4 = 0;
        r0 = r30;
        r0 = r0.zzanj;	 Catch:{ all -> 0x01c5 }
        r16 = r0;
        com.google.android.gms.common.internal.Preconditions.checkNotNull(r21);	 Catch:{ all -> 0x01c5 }
        r14.zzab();	 Catch:{ all -> 0x01c5 }
        r14.zzch();	 Catch:{ all -> 0x01c5 }
        r3 = 0;
        r2 = r14.getWritableDatabase();	 Catch:{ SQLiteException -> 0x0c1e }
        r5 = 0;
        r5 = android.text.TextUtils.isEmpty(r5);	 Catch:{ SQLiteException -> 0x0c1e }
        if (r5 == 0) goto L_0x01ce;
    L_0x0031:
        r6 = -1;
        r5 = (r16 > r6 ? 1 : (r16 == r6 ? 0 : -1));
        if (r5 == 0) goto L_0x0160;
    L_0x0037:
        r5 = 2;
        r5 = new java.lang.String[r5];	 Catch:{ SQLiteException -> 0x0c1e }
        r6 = 0;
        r7 = java.lang.String.valueOf(r16);	 Catch:{ SQLiteException -> 0x0c1e }
        r5[r6] = r7;	 Catch:{ SQLiteException -> 0x0c1e }
        r6 = 1;
        r7 = java.lang.String.valueOf(r32);	 Catch:{ SQLiteException -> 0x0c1e }
        r5[r6] = r7;	 Catch:{ SQLiteException -> 0x0c1e }
        r6 = r5;
    L_0x0049:
        r8 = -1;
        r5 = (r16 > r8 ? 1 : (r16 == r8 ? 0 : -1));
        if (r5 == 0) goto L_0x016d;
    L_0x004f:
        r5 = "rowid <= ? and ";
    L_0x0052:
        r7 = java.lang.String.valueOf(r5);	 Catch:{ SQLiteException -> 0x0c1e }
        r7 = r7.length();	 Catch:{ SQLiteException -> 0x0c1e }
        r7 = r7 + 148;
        r8 = new java.lang.StringBuilder;	 Catch:{ SQLiteException -> 0x0c1e }
        r8.<init>(r7);	 Catch:{ SQLiteException -> 0x0c1e }
        r7 = "select app_id, metadata_fingerprint from raw_events where ";
        r7 = r8.append(r7);	 Catch:{ SQLiteException -> 0x0c1e }
        r5 = r7.append(r5);	 Catch:{ SQLiteException -> 0x0c1e }
        r7 = "app_id in (select app_id from apps where config_fetched_time >= ?) order by rowid limit 1;";
        r5 = r5.append(r7);	 Catch:{ SQLiteException -> 0x0c1e }
        r5 = r5.toString();	 Catch:{ SQLiteException -> 0x0c1e }
        r3 = r2.rawQuery(r5, r6);	 Catch:{ SQLiteException -> 0x0c1e }
        r5 = r3.moveToFirst();	 Catch:{ SQLiteException -> 0x0c1e }
        if (r5 != 0) goto L_0x0172;
    L_0x0081:
        if (r3 == 0) goto L_0x0086;
    L_0x0083:
        r3.close();	 Catch:{ all -> 0x01c5 }
    L_0x0086:
        r0 = r21;
        r2 = r0.zzanu;	 Catch:{ all -> 0x01c5 }
        if (r2 == 0) goto L_0x0096;
    L_0x008c:
        r0 = r21;
        r2 = r0.zzanu;	 Catch:{ all -> 0x01c5 }
        r2 = r2.isEmpty();	 Catch:{ all -> 0x01c5 }
        if (r2 == 0) goto L_0x0372;
    L_0x0096:
        r2 = 1;
    L_0x0097:
        if (r2 != 0) goto L_0x0c0a;
    L_0x0099:
        r17 = 0;
        r0 = r21;
        r0 = r0.zzans;	 Catch:{ all -> 0x01c5 }
        r22 = r0;
        r0 = r21;
        r2 = r0.zzanu;	 Catch:{ all -> 0x01c5 }
        r2 = r2.size();	 Catch:{ all -> 0x01c5 }
        r2 = new com.google.android.gms.internal.measurement.zzki[r2];	 Catch:{ all -> 0x01c5 }
        r0 = r22;
        r0.zzatd = r2;	 Catch:{ all -> 0x01c5 }
        r13 = 0;
        r14 = 0;
        r0 = r30;
        r2 = r0.zzamd;	 Catch:{ all -> 0x01c5 }
        r0 = r22;
        r3 = r0.zztd;	 Catch:{ all -> 0x01c5 }
        r18 = r2.zzau(r3);	 Catch:{ all -> 0x01c5 }
        r2 = 0;
        r16 = r2;
    L_0x00c1:
        r0 = r21;
        r2 = r0.zzanu;	 Catch:{ all -> 0x01c5 }
        r2 = r2.size();	 Catch:{ all -> 0x01c5 }
        r0 = r16;
        if (r0 >= r2) goto L_0x05e0;
    L_0x00cd:
        r0 = r21;
        r2 = r0.zzanu;	 Catch:{ all -> 0x01c5 }
        r0 = r16;
        r2 = r2.get(r0);	 Catch:{ all -> 0x01c5 }
        r0 = r2;
        r0 = (com.google.android.gms.internal.measurement.zzki) r0;	 Catch:{ all -> 0x01c5 }
        r12 = r0;
        r2 = r30.zzgd();	 Catch:{ all -> 0x01c5 }
        r0 = r21;
        r3 = r0.zzans;	 Catch:{ all -> 0x01c5 }
        r3 = r3.zztd;	 Catch:{ all -> 0x01c5 }
        r4 = r12.name;	 Catch:{ all -> 0x01c5 }
        r2 = r2.zzn(r3, r4);	 Catch:{ all -> 0x01c5 }
        if (r2 == 0) goto L_0x0378;
    L_0x00ed:
        r2 = r30.zzgg();	 Catch:{ all -> 0x01c5 }
        r2 = r2.zzin();	 Catch:{ all -> 0x01c5 }
        r3 = "Dropping blacklisted raw event. appId";
        r0 = r21;
        r4 = r0.zzans;	 Catch:{ all -> 0x01c5 }
        r4 = r4.zztd;	 Catch:{ all -> 0x01c5 }
        r4 = com.google.android.gms.internal.measurement.zzfg.zzbh(r4);	 Catch:{ all -> 0x01c5 }
        r5 = r30.zzgb();	 Catch:{ all -> 0x01c5 }
        r6 = r12.name;	 Catch:{ all -> 0x01c5 }
        r5 = r5.zzbe(r6);	 Catch:{ all -> 0x01c5 }
        r2.zze(r3, r4, r5);	 Catch:{ all -> 0x01c5 }
        r2 = r30.zzgc();	 Catch:{ all -> 0x01c5 }
        r0 = r21;
        r3 = r0.zzans;	 Catch:{ all -> 0x01c5 }
        r3 = r3.zztd;	 Catch:{ all -> 0x01c5 }
        r2 = r2.zzce(r3);	 Catch:{ all -> 0x01c5 }
        if (r2 != 0) goto L_0x012f;
    L_0x011f:
        r2 = r30.zzgc();	 Catch:{ all -> 0x01c5 }
        r0 = r21;
        r3 = r0.zzans;	 Catch:{ all -> 0x01c5 }
        r3 = r3.zztd;	 Catch:{ all -> 0x01c5 }
        r2 = r2.zzcf(r3);	 Catch:{ all -> 0x01c5 }
        if (r2 == 0) goto L_0x0375;
    L_0x012f:
        r2 = 1;
    L_0x0130:
        if (r2 != 0) goto L_0x0c3c;
    L_0x0132:
        r2 = "_err";
        r3 = r12.name;	 Catch:{ all -> 0x01c5 }
        r2 = r2.equals(r3);	 Catch:{ all -> 0x01c5 }
        if (r2 != 0) goto L_0x0c3c;
    L_0x013d:
        r2 = r30.zzgc();	 Catch:{ all -> 0x01c5 }
        r0 = r21;
        r3 = r0.zzans;	 Catch:{ all -> 0x01c5 }
        r3 = r3.zztd;	 Catch:{ all -> 0x01c5 }
        r4 = 11;
        r5 = "_ev";
        r6 = r12.name;	 Catch:{ all -> 0x01c5 }
        r7 = 0;
        r2.zza(r3, r4, r5, r6, r7);	 Catch:{ all -> 0x01c5 }
        r2 = r14;
        r4 = r13;
        r5 = r17;
    L_0x0156:
        r6 = r16 + 1;
        r16 = r6;
        r14 = r2;
        r13 = r4;
        r17 = r5;
        goto L_0x00c1;
    L_0x0160:
        r5 = 1;
        r5 = new java.lang.String[r5];	 Catch:{ SQLiteException -> 0x0c1e }
        r6 = 0;
        r7 = java.lang.String.valueOf(r32);	 Catch:{ SQLiteException -> 0x0c1e }
        r5[r6] = r7;	 Catch:{ SQLiteException -> 0x0c1e }
        r6 = r5;
        goto L_0x0049;
    L_0x016d:
        r5 = "";
        goto L_0x0052;
    L_0x0172:
        r5 = 0;
        r4 = r3.getString(r5);	 Catch:{ SQLiteException -> 0x0c1e }
        r5 = 1;
        r5 = r3.getString(r5);	 Catch:{ SQLiteException -> 0x0c1e }
        r3.close();	 Catch:{ SQLiteException -> 0x0c1e }
        r13 = r5;
        r11 = r3;
        r12 = r4;
    L_0x0182:
        r3 = "raw_events_metadata";
        r4 = 1;
        r4 = new java.lang.String[r4];	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0c1a }
        r5 = 0;
        r6 = "metadata";
        r4[r5] = r6;	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0c1a }
        r5 = "app_id = ? and metadata_fingerprint = ?";
        r6 = 2;
        r6 = new java.lang.String[r6];	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0c1a }
        r7 = 0;
        r6[r7] = r12;	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0c1a }
        r7 = 1;
        r6[r7] = r13;	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0c1a }
        r7 = 0;
        r8 = 0;
        r9 = "rowid";
        r10 = "2";
        r11 = r2.query(r3, r4, r5, r6, r7, r8, r9, r10);	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0c1a }
        r3 = r11.moveToFirst();	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0c1a }
        if (r3 != 0) goto L_0x023c;
    L_0x01ac:
        r2 = r14.zzgg();	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0c1a }
        r2 = r2.zzil();	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0c1a }
        r3 = "Raw event metadata record is missing. appId";
        r4 = com.google.android.gms.internal.measurement.zzfg.zzbh(r12);	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0c1a }
        r2.zzg(r3, r4);	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0c1a }
        if (r11 == 0) goto L_0x0086;
    L_0x01c0:
        r11.close();	 Catch:{ all -> 0x01c5 }
        goto L_0x0086;
    L_0x01c5:
        r2 = move-exception;
        r3 = r30.zzga();
        r3.endTransaction();
        throw r2;
    L_0x01ce:
        r6 = -1;
        r5 = (r16 > r6 ? 1 : (r16 == r6 ? 0 : -1));
        if (r5 == 0) goto L_0x0222;
    L_0x01d4:
        r5 = 2;
        r5 = new java.lang.String[r5];	 Catch:{ SQLiteException -> 0x0c1e }
        r6 = 0;
        r7 = 0;
        r5[r6] = r7;	 Catch:{ SQLiteException -> 0x0c1e }
        r6 = 1;
        r7 = java.lang.String.valueOf(r16);	 Catch:{ SQLiteException -> 0x0c1e }
        r5[r6] = r7;	 Catch:{ SQLiteException -> 0x0c1e }
        r6 = r5;
    L_0x01e3:
        r8 = -1;
        r5 = (r16 > r8 ? 1 : (r16 == r8 ? 0 : -1));
        if (r5 == 0) goto L_0x022b;
    L_0x01e9:
        r5 = " and rowid <= ?";
    L_0x01ec:
        r7 = java.lang.String.valueOf(r5);	 Catch:{ SQLiteException -> 0x0c1e }
        r7 = r7.length();	 Catch:{ SQLiteException -> 0x0c1e }
        r7 = r7 + 84;
        r8 = new java.lang.StringBuilder;	 Catch:{ SQLiteException -> 0x0c1e }
        r8.<init>(r7);	 Catch:{ SQLiteException -> 0x0c1e }
        r7 = "select metadata_fingerprint from raw_events where app_id = ?";
        r7 = r8.append(r7);	 Catch:{ SQLiteException -> 0x0c1e }
        r5 = r7.append(r5);	 Catch:{ SQLiteException -> 0x0c1e }
        r7 = " order by rowid limit 1;";
        r5 = r5.append(r7);	 Catch:{ SQLiteException -> 0x0c1e }
        r5 = r5.toString();	 Catch:{ SQLiteException -> 0x0c1e }
        r3 = r2.rawQuery(r5, r6);	 Catch:{ SQLiteException -> 0x0c1e }
        r5 = r3.moveToFirst();	 Catch:{ SQLiteException -> 0x0c1e }
        if (r5 != 0) goto L_0x022f;
    L_0x021b:
        if (r3 == 0) goto L_0x0086;
    L_0x021d:
        r3.close();	 Catch:{ all -> 0x01c5 }
        goto L_0x0086;
    L_0x0222:
        r5 = 1;
        r5 = new java.lang.String[r5];	 Catch:{ SQLiteException -> 0x0c1e }
        r6 = 0;
        r7 = 0;
        r5[r6] = r7;	 Catch:{ SQLiteException -> 0x0c1e }
        r6 = r5;
        goto L_0x01e3;
    L_0x022b:
        r5 = "";
        goto L_0x01ec;
    L_0x022f:
        r5 = 0;
        r5 = r3.getString(r5);	 Catch:{ SQLiteException -> 0x0c1e }
        r3.close();	 Catch:{ SQLiteException -> 0x0c1e }
        r13 = r5;
        r11 = r3;
        r12 = r4;
        goto L_0x0182;
    L_0x023c:
        r3 = 0;
        r3 = r11.getBlob(r3);	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0c1a }
        r4 = 0;
        r5 = r3.length;	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0c1a }
        r3 = com.google.android.gms.internal.measurement.zzaba.zza(r3, r4, r5);	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0c1a }
        r4 = new com.google.android.gms.internal.measurement.zzkl;	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0c1a }
        r4.<init>();	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0c1a }
        r4.zzb(r3);	 Catch:{ IOException -> 0x02cf }
        r3 = r11.moveToNext();	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0c1a }
        if (r3 == 0) goto L_0x0267;
    L_0x0255:
        r3 = r14.zzgg();	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0c1a }
        r3 = r3.zzin();	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0c1a }
        r5 = "Get multiple raw event metadata records, expected one. appId";
        r6 = com.google.android.gms.internal.measurement.zzfg.zzbh(r12);	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0c1a }
        r3.zzg(r5, r6);	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0c1a }
    L_0x0267:
        r11.close();	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0c1a }
        r0 = r21;
        r0.zzb(r4);	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0c1a }
        r4 = -1;
        r3 = (r16 > r4 ? 1 : (r16 == r4 ? 0 : -1));
        if (r3 == 0) goto L_0x02e9;
    L_0x0275:
        r5 = "app_id = ? and metadata_fingerprint = ? and rowid <= ?";
        r3 = 3;
        r6 = new java.lang.String[r3];	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0c1a }
        r3 = 0;
        r6[r3] = r12;	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0c1a }
        r3 = 1;
        r6[r3] = r13;	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0c1a }
        r3 = 2;
        r4 = java.lang.String.valueOf(r16);	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0c1a }
        r6[r3] = r4;	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0c1a }
    L_0x0288:
        r3 = "raw_events";
        r4 = 4;
        r4 = new java.lang.String[r4];	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0c1a }
        r7 = 0;
        r8 = "rowid";
        r4[r7] = r8;	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0c1a }
        r7 = 1;
        r8 = "name";
        r4[r7] = r8;	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0c1a }
        r7 = 2;
        r8 = "timestamp";
        r4[r7] = r8;	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0c1a }
        r7 = 3;
        r8 = "data";
        r4[r7] = r8;	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0c1a }
        r7 = 0;
        r8 = 0;
        r9 = "rowid";
        r10 = 0;
        r3 = r2.query(r3, r4, r5, r6, r7, r8, r9, r10);	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0c1a }
        r2 = r3.moveToFirst();	 Catch:{ SQLiteException -> 0x0c21 }
        if (r2 != 0) goto L_0x0312;
    L_0x02b6:
        r2 = r14.zzgg();	 Catch:{ SQLiteException -> 0x0c21 }
        r2 = r2.zzin();	 Catch:{ SQLiteException -> 0x0c21 }
        r4 = "Raw event data disappeared while in transaction. appId";
        r5 = com.google.android.gms.internal.measurement.zzfg.zzbh(r12);	 Catch:{ SQLiteException -> 0x0c21 }
        r2.zzg(r4, r5);	 Catch:{ SQLiteException -> 0x0c21 }
        if (r3 == 0) goto L_0x0086;
    L_0x02ca:
        r3.close();	 Catch:{ all -> 0x01c5 }
        goto L_0x0086;
    L_0x02cf:
        r2 = move-exception;
        r3 = r14.zzgg();	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0c1a }
        r3 = r3.zzil();	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0c1a }
        r4 = "Data loss. Failed to merge raw event metadata. appId";
        r5 = com.google.android.gms.internal.measurement.zzfg.zzbh(r12);	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0c1a }
        r3.zze(r4, r5, r2);	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0c1a }
        if (r11 == 0) goto L_0x0086;
    L_0x02e4:
        r11.close();	 Catch:{ all -> 0x01c5 }
        goto L_0x0086;
    L_0x02e9:
        r5 = "app_id = ? and metadata_fingerprint = ?";
        r3 = 2;
        r6 = new java.lang.String[r3];	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0c1a }
        r3 = 0;
        r6[r3] = r12;	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0c1a }
        r3 = 1;
        r6[r3] = r13;	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0c1a }
        goto L_0x0288;
    L_0x02f6:
        r2 = move-exception;
        r3 = r11;
        r4 = r12;
    L_0x02f9:
        r5 = r14.zzgg();	 Catch:{ all -> 0x036b }
        r5 = r5.zzil();	 Catch:{ all -> 0x036b }
        r6 = "Data loss. Error selecting raw event. appId";
        r4 = com.google.android.gms.internal.measurement.zzfg.zzbh(r4);	 Catch:{ all -> 0x036b }
        r5.zze(r6, r4, r2);	 Catch:{ all -> 0x036b }
        if (r3 == 0) goto L_0x0086;
    L_0x030d:
        r3.close();	 Catch:{ all -> 0x01c5 }
        goto L_0x0086;
    L_0x0312:
        r2 = 0;
        r4 = r3.getLong(r2);	 Catch:{ SQLiteException -> 0x0c21 }
        r2 = 3;
        r2 = r3.getBlob(r2);	 Catch:{ SQLiteException -> 0x0c21 }
        r6 = 0;
        r7 = r2.length;	 Catch:{ SQLiteException -> 0x0c21 }
        r2 = com.google.android.gms.internal.measurement.zzaba.zza(r2, r6, r7);	 Catch:{ SQLiteException -> 0x0c21 }
        r6 = new com.google.android.gms.internal.measurement.zzki;	 Catch:{ SQLiteException -> 0x0c21 }
        r6.<init>();	 Catch:{ SQLiteException -> 0x0c21 }
        r6.zzb(r2);	 Catch:{ IOException -> 0x034b }
        r2 = 1;
        r2 = r3.getString(r2);	 Catch:{ SQLiteException -> 0x0c21 }
        r6.name = r2;	 Catch:{ SQLiteException -> 0x0c21 }
        r2 = 2;
        r8 = r3.getLong(r2);	 Catch:{ SQLiteException -> 0x0c21 }
        r2 = java.lang.Long.valueOf(r8);	 Catch:{ SQLiteException -> 0x0c21 }
        r6.zzasw = r2;	 Catch:{ SQLiteException -> 0x0c21 }
        r0 = r21;
        r2 = r0.zza(r4, r6);	 Catch:{ SQLiteException -> 0x0c21 }
        if (r2 != 0) goto L_0x035e;
    L_0x0344:
        if (r3 == 0) goto L_0x0086;
    L_0x0346:
        r3.close();	 Catch:{ all -> 0x01c5 }
        goto L_0x0086;
    L_0x034b:
        r2 = move-exception;
        r4 = r14.zzgg();	 Catch:{ SQLiteException -> 0x0c21 }
        r4 = r4.zzil();	 Catch:{ SQLiteException -> 0x0c21 }
        r5 = "Data loss. Failed to merge raw event. appId";
        r6 = com.google.android.gms.internal.measurement.zzfg.zzbh(r12);	 Catch:{ SQLiteException -> 0x0c21 }
        r4.zze(r5, r6, r2);	 Catch:{ SQLiteException -> 0x0c21 }
    L_0x035e:
        r2 = r3.moveToNext();	 Catch:{ SQLiteException -> 0x0c21 }
        if (r2 != 0) goto L_0x0312;
    L_0x0364:
        if (r3 == 0) goto L_0x0086;
    L_0x0366:
        r3.close();	 Catch:{ all -> 0x01c5 }
        goto L_0x0086;
    L_0x036b:
        r2 = move-exception;
    L_0x036c:
        if (r3 == 0) goto L_0x0371;
    L_0x036e:
        r3.close();	 Catch:{ all -> 0x01c5 }
    L_0x0371:
        throw r2;	 Catch:{ all -> 0x01c5 }
    L_0x0372:
        r2 = 0;
        goto L_0x0097;
    L_0x0375:
        r2 = 0;
        goto L_0x0130;
    L_0x0378:
        r2 = r30.zzgd();	 Catch:{ all -> 0x01c5 }
        r0 = r21;
        r3 = r0.zzans;	 Catch:{ all -> 0x01c5 }
        r3 = r3.zztd;	 Catch:{ all -> 0x01c5 }
        r4 = r12.name;	 Catch:{ all -> 0x01c5 }
        r19 = r2.zzo(r3, r4);	 Catch:{ all -> 0x01c5 }
        if (r19 != 0) goto L_0x0395;
    L_0x038a:
        r30.zzgc();	 Catch:{ all -> 0x01c5 }
        r2 = r12.name;	 Catch:{ all -> 0x01c5 }
        r2 = com.google.android.gms.internal.measurement.zzjv.zzcg(r2);	 Catch:{ all -> 0x01c5 }
        if (r2 == 0) goto L_0x05ae;
    L_0x0395:
        r4 = 0;
        r3 = 0;
        r2 = r12.zzasv;	 Catch:{ all -> 0x01c5 }
        if (r2 != 0) goto L_0x03a0;
    L_0x039b:
        r2 = 0;
        r2 = new com.google.android.gms.internal.measurement.zzkj[r2];	 Catch:{ all -> 0x01c5 }
        r12.zzasv = r2;	 Catch:{ all -> 0x01c5 }
    L_0x03a0:
        r6 = r12.zzasv;	 Catch:{ all -> 0x01c5 }
        r7 = r6.length;	 Catch:{ all -> 0x01c5 }
        r2 = 0;
        r5 = r2;
    L_0x03a5:
        if (r5 >= r7) goto L_0x03d7;
    L_0x03a7:
        r2 = r6[r5];	 Catch:{ all -> 0x01c5 }
        r8 = "_c";
        r9 = r2.name;	 Catch:{ all -> 0x01c5 }
        r8 = r8.equals(r9);	 Catch:{ all -> 0x01c5 }
        if (r8 == 0) goto L_0x03c2;
    L_0x03b4:
        r8 = 1;
        r4 = java.lang.Long.valueOf(r8);	 Catch:{ all -> 0x01c5 }
        r2.zzasz = r4;	 Catch:{ all -> 0x01c5 }
        r4 = 1;
        r2 = r3;
    L_0x03be:
        r5 = r5 + 1;
        r3 = r2;
        goto L_0x03a5;
    L_0x03c2:
        r8 = "_r";
        r9 = r2.name;	 Catch:{ all -> 0x01c5 }
        r8 = r8.equals(r9);	 Catch:{ all -> 0x01c5 }
        if (r8 == 0) goto L_0x0c39;
    L_0x03cd:
        r8 = 1;
        r3 = java.lang.Long.valueOf(r8);	 Catch:{ all -> 0x01c5 }
        r2.zzasz = r3;	 Catch:{ all -> 0x01c5 }
        r2 = 1;
        goto L_0x03be;
    L_0x03d7:
        if (r4 != 0) goto L_0x0419;
    L_0x03d9:
        if (r19 == 0) goto L_0x0419;
    L_0x03db:
        r2 = r30.zzgg();	 Catch:{ all -> 0x01c5 }
        r2 = r2.zzir();	 Catch:{ all -> 0x01c5 }
        r4 = "Marking event as conversion";
        r5 = r30.zzgb();	 Catch:{ all -> 0x01c5 }
        r6 = r12.name;	 Catch:{ all -> 0x01c5 }
        r5 = r5.zzbe(r6);	 Catch:{ all -> 0x01c5 }
        r2.zzg(r4, r5);	 Catch:{ all -> 0x01c5 }
        r2 = r12.zzasv;	 Catch:{ all -> 0x01c5 }
        r4 = r12.zzasv;	 Catch:{ all -> 0x01c5 }
        r4 = r4.length;	 Catch:{ all -> 0x01c5 }
        r4 = r4 + 1;
        r2 = java.util.Arrays.copyOf(r2, r4);	 Catch:{ all -> 0x01c5 }
        r2 = (com.google.android.gms.internal.measurement.zzkj[]) r2;	 Catch:{ all -> 0x01c5 }
        r4 = new com.google.android.gms.internal.measurement.zzkj;	 Catch:{ all -> 0x01c5 }
        r4.<init>();	 Catch:{ all -> 0x01c5 }
        r5 = "_c";
        r4.name = r5;	 Catch:{ all -> 0x01c5 }
        r6 = 1;
        r5 = java.lang.Long.valueOf(r6);	 Catch:{ all -> 0x01c5 }
        r4.zzasz = r5;	 Catch:{ all -> 0x01c5 }
        r5 = r2.length;	 Catch:{ all -> 0x01c5 }
        r5 = r5 + -1;
        r2[r5] = r4;	 Catch:{ all -> 0x01c5 }
        r12.zzasv = r2;	 Catch:{ all -> 0x01c5 }
    L_0x0419:
        if (r3 != 0) goto L_0x0459;
    L_0x041b:
        r2 = r30.zzgg();	 Catch:{ all -> 0x01c5 }
        r2 = r2.zzir();	 Catch:{ all -> 0x01c5 }
        r3 = "Marking event as real-time";
        r4 = r30.zzgb();	 Catch:{ all -> 0x01c5 }
        r5 = r12.name;	 Catch:{ all -> 0x01c5 }
        r4 = r4.zzbe(r5);	 Catch:{ all -> 0x01c5 }
        r2.zzg(r3, r4);	 Catch:{ all -> 0x01c5 }
        r2 = r12.zzasv;	 Catch:{ all -> 0x01c5 }
        r3 = r12.zzasv;	 Catch:{ all -> 0x01c5 }
        r3 = r3.length;	 Catch:{ all -> 0x01c5 }
        r3 = r3 + 1;
        r2 = java.util.Arrays.copyOf(r2, r3);	 Catch:{ all -> 0x01c5 }
        r2 = (com.google.android.gms.internal.measurement.zzkj[]) r2;	 Catch:{ all -> 0x01c5 }
        r3 = new com.google.android.gms.internal.measurement.zzkj;	 Catch:{ all -> 0x01c5 }
        r3.<init>();	 Catch:{ all -> 0x01c5 }
        r4 = "_r";
        r3.name = r4;	 Catch:{ all -> 0x01c5 }
        r4 = 1;
        r4 = java.lang.Long.valueOf(r4);	 Catch:{ all -> 0x01c5 }
        r3.zzasz = r4;	 Catch:{ all -> 0x01c5 }
        r4 = r2.length;	 Catch:{ all -> 0x01c5 }
        r4 = r4 + -1;
        r2[r4] = r3;	 Catch:{ all -> 0x01c5 }
        r12.zzasv = r2;	 Catch:{ all -> 0x01c5 }
    L_0x0459:
        r2 = 1;
        r3 = r30.zzga();	 Catch:{ all -> 0x01c5 }
        r4 = r30.zzjv();	 Catch:{ all -> 0x01c5 }
        r0 = r21;
        r6 = r0.zzans;	 Catch:{ all -> 0x01c5 }
        r6 = r6.zztd;	 Catch:{ all -> 0x01c5 }
        r7 = 0;
        r8 = 0;
        r9 = 0;
        r10 = 0;
        r11 = 1;
        r3 = r3.zza(r4, r6, r7, r8, r9, r10, r11);	 Catch:{ all -> 0x01c5 }
        r4 = r3.zzafg;	 Catch:{ all -> 0x01c5 }
        r0 = r30;
        r3 = r0.zzamd;	 Catch:{ all -> 0x01c5 }
        r0 = r21;
        r6 = r0.zzans;	 Catch:{ all -> 0x01c5 }
        r6 = r6.zztd;	 Catch:{ all -> 0x01c5 }
        r3 = r3.zzar(r6);	 Catch:{ all -> 0x01c5 }
        r6 = (long) r3;	 Catch:{ all -> 0x01c5 }
        r3 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r3 <= 0) goto L_0x0c35;
    L_0x0486:
        r2 = 0;
    L_0x0487:
        r3 = r12.zzasv;	 Catch:{ all -> 0x01c5 }
        r3 = r3.length;	 Catch:{ all -> 0x01c5 }
        if (r2 >= r3) goto L_0x04b9;
    L_0x048c:
        r3 = "_r";
        r4 = r12.zzasv;	 Catch:{ all -> 0x01c5 }
        r4 = r4[r2];	 Catch:{ all -> 0x01c5 }
        r4 = r4.name;	 Catch:{ all -> 0x01c5 }
        r3 = r3.equals(r4);	 Catch:{ all -> 0x01c5 }
        if (r3 == 0) goto L_0x0523;
    L_0x049b:
        r3 = r12.zzasv;	 Catch:{ all -> 0x01c5 }
        r3 = r3.length;	 Catch:{ all -> 0x01c5 }
        r3 = r3 + -1;
        r3 = new com.google.android.gms.internal.measurement.zzkj[r3];	 Catch:{ all -> 0x01c5 }
        if (r2 <= 0) goto L_0x04ab;
    L_0x04a4:
        r4 = r12.zzasv;	 Catch:{ all -> 0x01c5 }
        r5 = 0;
        r6 = 0;
        java.lang.System.arraycopy(r4, r5, r3, r6, r2);	 Catch:{ all -> 0x01c5 }
    L_0x04ab:
        r4 = r3.length;	 Catch:{ all -> 0x01c5 }
        if (r2 >= r4) goto L_0x04b7;
    L_0x04ae:
        r4 = r12.zzasv;	 Catch:{ all -> 0x01c5 }
        r5 = r2 + 1;
        r6 = r3.length;	 Catch:{ all -> 0x01c5 }
        r6 = r6 - r2;
        java.lang.System.arraycopy(r4, r5, r3, r2, r6);	 Catch:{ all -> 0x01c5 }
    L_0x04b7:
        r12.zzasv = r3;	 Catch:{ all -> 0x01c5 }
    L_0x04b9:
        r2 = r12.name;	 Catch:{ all -> 0x01c5 }
        r2 = com.google.android.gms.internal.measurement.zzjv.zzbv(r2);	 Catch:{ all -> 0x01c5 }
        if (r2 == 0) goto L_0x05ae;
    L_0x04c1:
        if (r19 == 0) goto L_0x05ae;
    L_0x04c3:
        r3 = r30.zzga();	 Catch:{ all -> 0x01c5 }
        r4 = r30.zzjv();	 Catch:{ all -> 0x01c5 }
        r0 = r21;
        r2 = r0.zzans;	 Catch:{ all -> 0x01c5 }
        r6 = r2.zztd;	 Catch:{ all -> 0x01c5 }
        r7 = 0;
        r8 = 0;
        r9 = 1;
        r10 = 0;
        r11 = 0;
        r2 = r3.zza(r4, r6, r7, r8, r9, r10, r11);	 Catch:{ all -> 0x01c5 }
        r2 = r2.zzafe;	 Catch:{ all -> 0x01c5 }
        r0 = r30;
        r4 = r0.zzamd;	 Catch:{ all -> 0x01c5 }
        r0 = r21;
        r5 = r0.zzans;	 Catch:{ all -> 0x01c5 }
        r5 = r5.zztd;	 Catch:{ all -> 0x01c5 }
        r6 = com.google.android.gms.internal.measurement.zzew.zzagy;	 Catch:{ all -> 0x01c5 }
        r4 = r4.zzb(r5, r6);	 Catch:{ all -> 0x01c5 }
        r4 = (long) r4;	 Catch:{ all -> 0x01c5 }
        r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
        if (r2 <= 0) goto L_0x05ae;
    L_0x04f1:
        r2 = r30.zzgg();	 Catch:{ all -> 0x01c5 }
        r2 = r2.zzin();	 Catch:{ all -> 0x01c5 }
        r3 = "Too many conversions. Not logging as conversion. appId";
        r0 = r21;
        r4 = r0.zzans;	 Catch:{ all -> 0x01c5 }
        r4 = r4.zztd;	 Catch:{ all -> 0x01c5 }
        r4 = com.google.android.gms.internal.measurement.zzfg.zzbh(r4);	 Catch:{ all -> 0x01c5 }
        r2.zzg(r3, r4);	 Catch:{ all -> 0x01c5 }
        r4 = 0;
        r3 = 0;
        r6 = r12.zzasv;	 Catch:{ all -> 0x01c5 }
        r7 = r6.length;	 Catch:{ all -> 0x01c5 }
        r2 = 0;
        r5 = r2;
    L_0x0510:
        if (r5 >= r7) goto L_0x0535;
    L_0x0512:
        r2 = r6[r5];	 Catch:{ all -> 0x01c5 }
        r8 = "_c";
        r9 = r2.name;	 Catch:{ all -> 0x01c5 }
        r8 = r8.equals(r9);	 Catch:{ all -> 0x01c5 }
        if (r8 == 0) goto L_0x0527;
    L_0x051f:
        r5 = r5 + 1;
        r3 = r2;
        goto L_0x0510;
    L_0x0523:
        r2 = r2 + 1;
        goto L_0x0487;
    L_0x0527:
        r8 = "_err";
        r2 = r2.name;	 Catch:{ all -> 0x01c5 }
        r2 = r8.equals(r2);	 Catch:{ all -> 0x01c5 }
        if (r2 == 0) goto L_0x0c32;
    L_0x0532:
        r4 = 1;
        r2 = r3;
        goto L_0x051f;
    L_0x0535:
        if (r4 == 0) goto L_0x0584;
    L_0x0537:
        if (r3 == 0) goto L_0x0584;
    L_0x0539:
        r2 = r12.zzasv;	 Catch:{ all -> 0x01c5 }
        r4 = 1;
        r4 = new com.google.android.gms.internal.measurement.zzkj[r4];	 Catch:{ all -> 0x01c5 }
        r5 = 0;
        r4[r5] = r3;	 Catch:{ all -> 0x01c5 }
        r2 = com.google.android.gms.common.util.ArrayUtils.removeAll(r2, r4);	 Catch:{ all -> 0x01c5 }
        r2 = (com.google.android.gms.internal.measurement.zzkj[]) r2;	 Catch:{ all -> 0x01c5 }
        r12.zzasv = r2;	 Catch:{ all -> 0x01c5 }
        r5 = r17;
    L_0x054b:
        if (r18 == 0) goto L_0x0c2f;
    L_0x054d:
        r2 = "_e";
        r3 = r12.name;	 Catch:{ all -> 0x01c5 }
        r2 = r2.equals(r3);	 Catch:{ all -> 0x01c5 }
        if (r2 == 0) goto L_0x0c2f;
    L_0x0558:
        r2 = r12.zzasv;	 Catch:{ all -> 0x01c5 }
        if (r2 == 0) goto L_0x0561;
    L_0x055c:
        r2 = r12.zzasv;	 Catch:{ all -> 0x01c5 }
        r2 = r2.length;	 Catch:{ all -> 0x01c5 }
        if (r2 != 0) goto L_0x05b1;
    L_0x0561:
        r2 = r30.zzgg();	 Catch:{ all -> 0x01c5 }
        r2 = r2.zzin();	 Catch:{ all -> 0x01c5 }
        r3 = "Engagement event does not contain any parameters. appId";
        r0 = r21;
        r4 = r0.zzans;	 Catch:{ all -> 0x01c5 }
        r4 = r4.zztd;	 Catch:{ all -> 0x01c5 }
        r4 = com.google.android.gms.internal.measurement.zzfg.zzbh(r4);	 Catch:{ all -> 0x01c5 }
        r2.zzg(r3, r4);	 Catch:{ all -> 0x01c5 }
        r2 = r14;
    L_0x057a:
        r0 = r22;
        r6 = r0.zzatd;	 Catch:{ all -> 0x01c5 }
        r4 = r13 + 1;
        r6[r13] = r12;	 Catch:{ all -> 0x01c5 }
        goto L_0x0156;
    L_0x0584:
        if (r3 == 0) goto L_0x0596;
    L_0x0586:
        r2 = "_err";
        r3.name = r2;	 Catch:{ all -> 0x01c5 }
        r4 = 10;
        r2 = java.lang.Long.valueOf(r4);	 Catch:{ all -> 0x01c5 }
        r3.zzasz = r2;	 Catch:{ all -> 0x01c5 }
        r5 = r17;
        goto L_0x054b;
    L_0x0596:
        r2 = r30.zzgg();	 Catch:{ all -> 0x01c5 }
        r2 = r2.zzil();	 Catch:{ all -> 0x01c5 }
        r3 = "Did not find conversion parameter. appId";
        r0 = r21;
        r4 = r0.zzans;	 Catch:{ all -> 0x01c5 }
        r4 = r4.zztd;	 Catch:{ all -> 0x01c5 }
        r4 = com.google.android.gms.internal.measurement.zzfg.zzbh(r4);	 Catch:{ all -> 0x01c5 }
        r2.zzg(r3, r4);	 Catch:{ all -> 0x01c5 }
    L_0x05ae:
        r5 = r17;
        goto L_0x054b;
    L_0x05b1:
        r30.zzgc();	 Catch:{ all -> 0x01c5 }
        r2 = "_et";
        r2 = com.google.android.gms.internal.measurement.zzjv.zzb(r12, r2);	 Catch:{ all -> 0x01c5 }
        r2 = (java.lang.Long) r2;	 Catch:{ all -> 0x01c5 }
        if (r2 != 0) goto L_0x05d9;
    L_0x05bf:
        r2 = r30.zzgg();	 Catch:{ all -> 0x01c5 }
        r2 = r2.zzin();	 Catch:{ all -> 0x01c5 }
        r3 = "Engagement event does not include duration. appId";
        r0 = r21;
        r4 = r0.zzans;	 Catch:{ all -> 0x01c5 }
        r4 = r4.zztd;	 Catch:{ all -> 0x01c5 }
        r4 = com.google.android.gms.internal.measurement.zzfg.zzbh(r4);	 Catch:{ all -> 0x01c5 }
        r2.zzg(r3, r4);	 Catch:{ all -> 0x01c5 }
        r2 = r14;
        goto L_0x057a;
    L_0x05d9:
        r2 = r2.longValue();	 Catch:{ all -> 0x01c5 }
        r14 = r14 + r2;
        r2 = r14;
        goto L_0x057a;
    L_0x05e0:
        r0 = r21;
        r2 = r0.zzanu;	 Catch:{ all -> 0x01c5 }
        r2 = r2.size();	 Catch:{ all -> 0x01c5 }
        if (r13 >= r2) goto L_0x05f8;
    L_0x05ea:
        r0 = r22;
        r2 = r0.zzatd;	 Catch:{ all -> 0x01c5 }
        r2 = java.util.Arrays.copyOf(r2, r13);	 Catch:{ all -> 0x01c5 }
        r2 = (com.google.android.gms.internal.measurement.zzki[]) r2;	 Catch:{ all -> 0x01c5 }
        r0 = r22;
        r0.zzatd = r2;	 Catch:{ all -> 0x01c5 }
    L_0x05f8:
        if (r18 == 0) goto L_0x06ad;
    L_0x05fa:
        r2 = r30.zzga();	 Catch:{ all -> 0x01c5 }
        r0 = r22;
        r3 = r0.zztd;	 Catch:{ all -> 0x01c5 }
        r4 = "_lte";
        r8 = r2.zzg(r3, r4);	 Catch:{ all -> 0x01c5 }
        if (r8 == 0) goto L_0x060f;
    L_0x060b:
        r2 = r8.value;	 Catch:{ all -> 0x01c5 }
        if (r2 != 0) goto L_0x07a9;
    L_0x060f:
        r2 = new com.google.android.gms.internal.measurement.zzju;	 Catch:{ all -> 0x01c5 }
        r0 = r22;
        r3 = r0.zztd;	 Catch:{ all -> 0x01c5 }
        r4 = "auto";
        r5 = "_lte";
        r0 = r30;
        r6 = r0.zzrj;	 Catch:{ all -> 0x01c5 }
        r6 = r6.currentTimeMillis();	 Catch:{ all -> 0x01c5 }
        r8 = java.lang.Long.valueOf(r14);	 Catch:{ all -> 0x01c5 }
        r2.<init>(r3, r4, r5, r6, r8);	 Catch:{ all -> 0x01c5 }
        r4 = r2;
    L_0x062b:
        r5 = new com.google.android.gms.internal.measurement.zzkn;	 Catch:{ all -> 0x01c5 }
        r5.<init>();	 Catch:{ all -> 0x01c5 }
        r2 = "_lte";
        r5.name = r2;	 Catch:{ all -> 0x01c5 }
        r0 = r30;
        r2 = r0.zzrj;	 Catch:{ all -> 0x01c5 }
        r2 = r2.currentTimeMillis();	 Catch:{ all -> 0x01c5 }
        r2 = java.lang.Long.valueOf(r2);	 Catch:{ all -> 0x01c5 }
        r5.zzaui = r2;	 Catch:{ all -> 0x01c5 }
        r2 = r4.value;	 Catch:{ all -> 0x01c5 }
        r2 = (java.lang.Long) r2;	 Catch:{ all -> 0x01c5 }
        r5.zzasz = r2;	 Catch:{ all -> 0x01c5 }
        r2 = 0;
        r3 = 0;
    L_0x064b:
        r0 = r22;
        r6 = r0.zzate;	 Catch:{ all -> 0x01c5 }
        r6 = r6.length;	 Catch:{ all -> 0x01c5 }
        if (r3 >= r6) goto L_0x066a;
    L_0x0652:
        r6 = "_lte";
        r0 = r22;
        r7 = r0.zzate;	 Catch:{ all -> 0x01c5 }
        r7 = r7[r3];	 Catch:{ all -> 0x01c5 }
        r7 = r7.name;	 Catch:{ all -> 0x01c5 }
        r6 = r6.equals(r7);	 Catch:{ all -> 0x01c5 }
        if (r6 == 0) goto L_0x07d0;
    L_0x0663:
        r0 = r22;
        r2 = r0.zzate;	 Catch:{ all -> 0x01c5 }
        r2[r3] = r5;	 Catch:{ all -> 0x01c5 }
        r2 = 1;
    L_0x066a:
        if (r2 != 0) goto L_0x0690;
    L_0x066c:
        r0 = r22;
        r2 = r0.zzate;	 Catch:{ all -> 0x01c5 }
        r0 = r22;
        r3 = r0.zzate;	 Catch:{ all -> 0x01c5 }
        r3 = r3.length;	 Catch:{ all -> 0x01c5 }
        r3 = r3 + 1;
        r2 = java.util.Arrays.copyOf(r2, r3);	 Catch:{ all -> 0x01c5 }
        r2 = (com.google.android.gms.internal.measurement.zzkn[]) r2;	 Catch:{ all -> 0x01c5 }
        r0 = r22;
        r0.zzate = r2;	 Catch:{ all -> 0x01c5 }
        r0 = r22;
        r2 = r0.zzate;	 Catch:{ all -> 0x01c5 }
        r0 = r21;
        r3 = r0.zzans;	 Catch:{ all -> 0x01c5 }
        r3 = r3.zzate;	 Catch:{ all -> 0x01c5 }
        r3 = r3.length;	 Catch:{ all -> 0x01c5 }
        r3 = r3 + -1;
        r2[r3] = r5;	 Catch:{ all -> 0x01c5 }
    L_0x0690:
        r2 = 0;
        r2 = (r14 > r2 ? 1 : (r14 == r2 ? 0 : -1));
        if (r2 <= 0) goto L_0x06ad;
    L_0x0696:
        r2 = r30.zzga();	 Catch:{ all -> 0x01c5 }
        r2.zza(r4);	 Catch:{ all -> 0x01c5 }
        r2 = r30.zzgg();	 Catch:{ all -> 0x01c5 }
        r2 = r2.zziq();	 Catch:{ all -> 0x01c5 }
        r3 = "Updated lifetime engagement user property with value. Value";
        r4 = r4.value;	 Catch:{ all -> 0x01c5 }
        r2.zzg(r3, r4);	 Catch:{ all -> 0x01c5 }
    L_0x06ad:
        r0 = r22;
        r2 = r0.zztd;	 Catch:{ all -> 0x01c5 }
        r0 = r22;
        r3 = r0.zzate;	 Catch:{ all -> 0x01c5 }
        r0 = r22;
        r4 = r0.zzatd;	 Catch:{ all -> 0x01c5 }
        r0 = r30;
        r2 = r0.zza(r2, r3, r4);	 Catch:{ all -> 0x01c5 }
        r0 = r22;
        r0.zzatv = r2;	 Catch:{ all -> 0x01c5 }
        r2 = com.google.android.gms.internal.measurement.zzew.zzagk;	 Catch:{ all -> 0x01c5 }
        r2 = r2.get();	 Catch:{ all -> 0x01c5 }
        r2 = (java.lang.Boolean) r2;	 Catch:{ all -> 0x01c5 }
        r2 = r2.booleanValue();	 Catch:{ all -> 0x01c5 }
        if (r2 == 0) goto L_0x0a15;
    L_0x06d1:
        r0 = r30;
        r2 = r0.zzamd;	 Catch:{ all -> 0x01c5 }
        r0 = r21;
        r3 = r0.zzans;	 Catch:{ all -> 0x01c5 }
        r3 = r3.zztd;	 Catch:{ all -> 0x01c5 }
        r4 = "1";
        r2 = r2.zzgd();	 Catch:{ all -> 0x01c5 }
        r5 = "measurement.event_sampling_enabled";
        r2 = r2.zzm(r3, r5);	 Catch:{ all -> 0x01c5 }
        r2 = r4.equals(r2);	 Catch:{ all -> 0x01c5 }
        if (r2 == 0) goto L_0x0a15;
    L_0x06ef:
        r23 = new java.util.HashMap;	 Catch:{ all -> 0x01c5 }
        r23.<init>();	 Catch:{ all -> 0x01c5 }
        r0 = r22;
        r2 = r0.zzatd;	 Catch:{ all -> 0x01c5 }
        r2 = r2.length;	 Catch:{ all -> 0x01c5 }
        r0 = new com.google.android.gms.internal.measurement.zzki[r2];	 Catch:{ all -> 0x01c5 }
        r24 = r0;
        r18 = 0;
        r2 = r30.zzgc();	 Catch:{ all -> 0x01c5 }
        r25 = r2.zzku();	 Catch:{ all -> 0x01c5 }
        r0 = r22;
        r0 = r0.zzatd;	 Catch:{ all -> 0x01c5 }
        r26 = r0;
        r0 = r26;
        r0 = r0.length;	 Catch:{ all -> 0x01c5 }
        r27 = r0;
        r2 = 0;
        r19 = r2;
    L_0x0715:
        r0 = r19;
        r1 = r27;
        if (r0 >= r1) goto L_0x09dc;
    L_0x071b:
        r28 = r26[r19];	 Catch:{ all -> 0x01c5 }
        r0 = r28;
        r2 = r0.name;	 Catch:{ all -> 0x01c5 }
        r3 = "_ep";
        r2 = r2.equals(r3);	 Catch:{ all -> 0x01c5 }
        if (r2 == 0) goto L_0x07d4;
    L_0x072a:
        r30.zzgc();	 Catch:{ all -> 0x01c5 }
        r2 = "_en";
        r0 = r28;
        r2 = com.google.android.gms.internal.measurement.zzjv.zzb(r0, r2);	 Catch:{ all -> 0x01c5 }
        r2 = (java.lang.String) r2;	 Catch:{ all -> 0x01c5 }
        r0 = r23;
        r3 = r0.get(r2);	 Catch:{ all -> 0x01c5 }
        r3 = (com.google.android.gms.internal.measurement.zzeq) r3;	 Catch:{ all -> 0x01c5 }
        if (r3 != 0) goto L_0x0755;
    L_0x0742:
        r3 = r30.zzga();	 Catch:{ all -> 0x01c5 }
        r0 = r21;
        r4 = r0.zzans;	 Catch:{ all -> 0x01c5 }
        r4 = r4.zztd;	 Catch:{ all -> 0x01c5 }
        r3 = r3.zze(r4, r2);	 Catch:{ all -> 0x01c5 }
        r0 = r23;
        r0.put(r2, r3);	 Catch:{ all -> 0x01c5 }
    L_0x0755:
        r2 = r3.zzaft;	 Catch:{ all -> 0x01c5 }
        if (r2 != 0) goto L_0x09d8;
    L_0x0759:
        r2 = r3.zzafu;	 Catch:{ all -> 0x01c5 }
        r4 = r2.longValue();	 Catch:{ all -> 0x01c5 }
        r6 = 1;
        r2 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r2 <= 0) goto L_0x0779;
    L_0x0765:
        r30.zzgc();	 Catch:{ all -> 0x01c5 }
        r0 = r28;
        r2 = r0.zzasv;	 Catch:{ all -> 0x01c5 }
        r4 = "_sr";
        r5 = r3.zzafu;	 Catch:{ all -> 0x01c5 }
        r2 = com.google.android.gms.internal.measurement.zzjv.zza(r2, r4, r5);	 Catch:{ all -> 0x01c5 }
        r0 = r28;
        r0.zzasv = r2;	 Catch:{ all -> 0x01c5 }
    L_0x0779:
        r2 = r3.zzafv;	 Catch:{ all -> 0x01c5 }
        if (r2 == 0) goto L_0x079d;
    L_0x077d:
        r2 = r3.zzafv;	 Catch:{ all -> 0x01c5 }
        r2 = r2.booleanValue();	 Catch:{ all -> 0x01c5 }
        if (r2 == 0) goto L_0x079d;
    L_0x0785:
        r30.zzgc();	 Catch:{ all -> 0x01c5 }
        r0 = r28;
        r2 = r0.zzasv;	 Catch:{ all -> 0x01c5 }
        r3 = "_efs";
        r4 = 1;
        r4 = java.lang.Long.valueOf(r4);	 Catch:{ all -> 0x01c5 }
        r2 = com.google.android.gms.internal.measurement.zzjv.zza(r2, r3, r4);	 Catch:{ all -> 0x01c5 }
        r0 = r28;
        r0.zzasv = r2;	 Catch:{ all -> 0x01c5 }
    L_0x079d:
        r2 = r18 + 1;
        r24[r18] = r28;	 Catch:{ all -> 0x01c5 }
    L_0x07a1:
        r3 = r19 + 1;
        r19 = r3;
        r18 = r2;
        goto L_0x0715;
    L_0x07a9:
        r2 = new com.google.android.gms.internal.measurement.zzju;	 Catch:{ all -> 0x01c5 }
        r0 = r22;
        r3 = r0.zztd;	 Catch:{ all -> 0x01c5 }
        r4 = "auto";
        r5 = "_lte";
        r0 = r30;
        r6 = r0.zzrj;	 Catch:{ all -> 0x01c5 }
        r6 = r6.currentTimeMillis();	 Catch:{ all -> 0x01c5 }
        r8 = r8.value;	 Catch:{ all -> 0x01c5 }
        r8 = (java.lang.Long) r8;	 Catch:{ all -> 0x01c5 }
        r8 = r8.longValue();	 Catch:{ all -> 0x01c5 }
        r8 = r8 + r14;
        r8 = java.lang.Long.valueOf(r8);	 Catch:{ all -> 0x01c5 }
        r2.<init>(r3, r4, r5, r6, r8);	 Catch:{ all -> 0x01c5 }
        r4 = r2;
        goto L_0x062b;
    L_0x07d0:
        r3 = r3 + 1;
        goto L_0x064b;
    L_0x07d4:
        r2 = 1;
        r4 = "_dbg";
        r6 = 1;
        r5 = java.lang.Long.valueOf(r6);	 Catch:{ all -> 0x01c5 }
        r3 = android.text.TextUtils.isEmpty(r4);	 Catch:{ all -> 0x01c5 }
        if (r3 != 0) goto L_0x07e6;
    L_0x07e4:
        if (r5 != 0) goto L_0x081a;
    L_0x07e6:
        r3 = 0;
    L_0x07e7:
        if (r3 != 0) goto L_0x0c2b;
    L_0x07e9:
        r2 = r30.zzgd();	 Catch:{ all -> 0x01c5 }
        r0 = r21;
        r3 = r0.zzans;	 Catch:{ all -> 0x01c5 }
        r3 = r3.zztd;	 Catch:{ all -> 0x01c5 }
        r0 = r28;
        r4 = r0.name;	 Catch:{ all -> 0x01c5 }
        r2 = r2.zzp(r3, r4);	 Catch:{ all -> 0x01c5 }
        r20 = r2;
    L_0x07fd:
        if (r20 > 0) goto L_0x0859;
    L_0x07ff:
        r2 = r30.zzgg();	 Catch:{ all -> 0x01c5 }
        r2 = r2.zzin();	 Catch:{ all -> 0x01c5 }
        r3 = "Sample rate must be positive. event, rate";
        r0 = r28;
        r4 = r0.name;	 Catch:{ all -> 0x01c5 }
        r5 = java.lang.Integer.valueOf(r20);	 Catch:{ all -> 0x01c5 }
        r2.zze(r3, r4, r5);	 Catch:{ all -> 0x01c5 }
        r2 = r18 + 1;
        r24[r18] = r28;	 Catch:{ all -> 0x01c5 }
        goto L_0x07a1;
    L_0x081a:
        r0 = r28;
        r6 = r0.zzasv;	 Catch:{ all -> 0x01c5 }
        r7 = r6.length;	 Catch:{ all -> 0x01c5 }
        r3 = 0;
    L_0x0820:
        if (r3 >= r7) goto L_0x0857;
    L_0x0822:
        r8 = r6[r3];	 Catch:{ all -> 0x01c5 }
        r9 = r8.name;	 Catch:{ all -> 0x01c5 }
        r9 = r4.equals(r9);	 Catch:{ all -> 0x01c5 }
        if (r9 == 0) goto L_0x0854;
    L_0x082c:
        r3 = r5 instanceof java.lang.Long;	 Catch:{ all -> 0x01c5 }
        if (r3 == 0) goto L_0x0838;
    L_0x0830:
        r3 = r8.zzasz;	 Catch:{ all -> 0x01c5 }
        r3 = r5.equals(r3);	 Catch:{ all -> 0x01c5 }
        if (r3 != 0) goto L_0x0850;
    L_0x0838:
        r3 = r5 instanceof java.lang.String;	 Catch:{ all -> 0x01c5 }
        if (r3 == 0) goto L_0x0844;
    L_0x083c:
        r3 = r8.zzajf;	 Catch:{ all -> 0x01c5 }
        r3 = r5.equals(r3);	 Catch:{ all -> 0x01c5 }
        if (r3 != 0) goto L_0x0850;
    L_0x0844:
        r3 = r5 instanceof java.lang.Double;	 Catch:{ all -> 0x01c5 }
        if (r3 == 0) goto L_0x0852;
    L_0x0848:
        r3 = r8.zzaqx;	 Catch:{ all -> 0x01c5 }
        r3 = r5.equals(r3);	 Catch:{ all -> 0x01c5 }
        if (r3 == 0) goto L_0x0852;
    L_0x0850:
        r3 = 1;
        goto L_0x07e7;
    L_0x0852:
        r3 = 0;
        goto L_0x07e7;
    L_0x0854:
        r3 = r3 + 1;
        goto L_0x0820;
    L_0x0857:
        r3 = 0;
        goto L_0x07e7;
    L_0x0859:
        r0 = r28;
        r2 = r0.name;	 Catch:{ all -> 0x01c5 }
        r0 = r23;
        r2 = r0.get(r2);	 Catch:{ all -> 0x01c5 }
        r2 = (com.google.android.gms.internal.measurement.zzeq) r2;	 Catch:{ all -> 0x01c5 }
        if (r2 != 0) goto L_0x0c28;
    L_0x0867:
        r2 = r30.zzga();	 Catch:{ all -> 0x01c5 }
        r0 = r21;
        r3 = r0.zzans;	 Catch:{ all -> 0x01c5 }
        r3 = r3.zztd;	 Catch:{ all -> 0x01c5 }
        r0 = r28;
        r4 = r0.name;	 Catch:{ all -> 0x01c5 }
        r3 = r2.zze(r3, r4);	 Catch:{ all -> 0x01c5 }
        if (r3 != 0) goto L_0x08b4;
    L_0x087b:
        r2 = r30.zzgg();	 Catch:{ all -> 0x01c5 }
        r2 = r2.zzin();	 Catch:{ all -> 0x01c5 }
        r3 = "Event being bundled has no eventAggregate. appId, eventName";
        r0 = r21;
        r4 = r0.zzans;	 Catch:{ all -> 0x01c5 }
        r4 = r4.zztd;	 Catch:{ all -> 0x01c5 }
        r0 = r28;
        r5 = r0.name;	 Catch:{ all -> 0x01c5 }
        r2.zze(r3, r4, r5);	 Catch:{ all -> 0x01c5 }
        r3 = new com.google.android.gms.internal.measurement.zzeq;	 Catch:{ all -> 0x01c5 }
        r0 = r21;
        r2 = r0.zzans;	 Catch:{ all -> 0x01c5 }
        r4 = r2.zztd;	 Catch:{ all -> 0x01c5 }
        r0 = r28;
        r5 = r0.name;	 Catch:{ all -> 0x01c5 }
        r6 = 1;
        r8 = 1;
        r0 = r28;
        r2 = r0.zzasw;	 Catch:{ all -> 0x01c5 }
        r10 = r2.longValue();	 Catch:{ all -> 0x01c5 }
        r12 = 0;
        r14 = 0;
        r15 = 0;
        r16 = 0;
        r3.<init>(r4, r5, r6, r8, r10, r12, r14, r15, r16);	 Catch:{ all -> 0x01c5 }
    L_0x08b4:
        r30.zzgc();	 Catch:{ all -> 0x01c5 }
        r2 = "_eid";
        r0 = r28;
        r2 = com.google.android.gms.internal.measurement.zzjv.zzb(r0, r2);	 Catch:{ all -> 0x01c5 }
        r2 = (java.lang.Long) r2;	 Catch:{ all -> 0x01c5 }
        if (r2 == 0) goto L_0x08f6;
    L_0x08c4:
        r4 = 1;
    L_0x08c5:
        r4 = java.lang.Boolean.valueOf(r4);	 Catch:{ all -> 0x01c5 }
        r5 = 1;
        r0 = r20;
        if (r0 != r5) goto L_0x08f8;
    L_0x08ce:
        r2 = r18 + 1;
        r24[r18] = r28;	 Catch:{ all -> 0x01c5 }
        r4 = r4.booleanValue();	 Catch:{ all -> 0x01c5 }
        if (r4 == 0) goto L_0x07a1;
    L_0x08d8:
        r4 = r3.zzaft;	 Catch:{ all -> 0x01c5 }
        if (r4 != 0) goto L_0x08e4;
    L_0x08dc:
        r4 = r3.zzafu;	 Catch:{ all -> 0x01c5 }
        if (r4 != 0) goto L_0x08e4;
    L_0x08e0:
        r4 = r3.zzafv;	 Catch:{ all -> 0x01c5 }
        if (r4 == 0) goto L_0x07a1;
    L_0x08e4:
        r4 = 0;
        r5 = 0;
        r6 = 0;
        r3 = r3.zza(r4, r5, r6);	 Catch:{ all -> 0x01c5 }
        r0 = r28;
        r4 = r0.name;	 Catch:{ all -> 0x01c5 }
        r0 = r23;
        r0.put(r4, r3);	 Catch:{ all -> 0x01c5 }
        goto L_0x07a1;
    L_0x08f6:
        r4 = 0;
        goto L_0x08c5;
    L_0x08f8:
        r0 = r25;
        r1 = r20;
        r5 = r0.nextInt(r1);	 Catch:{ all -> 0x01c5 }
        if (r5 != 0) goto L_0x0949;
    L_0x0902:
        r30.zzgc();	 Catch:{ all -> 0x01c5 }
        r0 = r28;
        r2 = r0.zzasv;	 Catch:{ all -> 0x01c5 }
        r5 = "_sr";
        r0 = r20;
        r6 = (long) r0;	 Catch:{ all -> 0x01c5 }
        r6 = java.lang.Long.valueOf(r6);	 Catch:{ all -> 0x01c5 }
        r2 = com.google.android.gms.internal.measurement.zzjv.zza(r2, r5, r6);	 Catch:{ all -> 0x01c5 }
        r0 = r28;
        r0.zzasv = r2;	 Catch:{ all -> 0x01c5 }
        r2 = r18 + 1;
        r24[r18] = r28;	 Catch:{ all -> 0x01c5 }
        r4 = r4.booleanValue();	 Catch:{ all -> 0x01c5 }
        if (r4 == 0) goto L_0x0932;
    L_0x0925:
        r4 = 0;
        r0 = r20;
        r6 = (long) r0;	 Catch:{ all -> 0x01c5 }
        r5 = java.lang.Long.valueOf(r6);	 Catch:{ all -> 0x01c5 }
        r6 = 0;
        r3 = r3.zza(r4, r5, r6);	 Catch:{ all -> 0x01c5 }
    L_0x0932:
        r0 = r28;
        r4 = r0.name;	 Catch:{ all -> 0x01c5 }
        r0 = r28;
        r5 = r0.zzasw;	 Catch:{ all -> 0x01c5 }
        r6 = r5.longValue();	 Catch:{ all -> 0x01c5 }
        r3 = r3.zzad(r6);	 Catch:{ all -> 0x01c5 }
        r0 = r23;
        r0.put(r4, r3);	 Catch:{ all -> 0x01c5 }
        goto L_0x07a1;
    L_0x0949:
        r6 = r3.zzafs;	 Catch:{ all -> 0x01c5 }
        r0 = r28;
        r5 = r0.zzasw;	 Catch:{ all -> 0x01c5 }
        r8 = r5.longValue();	 Catch:{ all -> 0x01c5 }
        r6 = r8 - r6;
        r6 = java.lang.Math.abs(r6);	 Catch:{ all -> 0x01c5 }
        r8 = 86400000; // 0x5265c00 float:7.82218E-36 double:4.2687272E-316;
        r5 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1));
        if (r5 < 0) goto L_0x09c3;
    L_0x0960:
        r30.zzgc();	 Catch:{ all -> 0x01c5 }
        r0 = r28;
        r2 = r0.zzasv;	 Catch:{ all -> 0x01c5 }
        r5 = "_efs";
        r6 = 1;
        r6 = java.lang.Long.valueOf(r6);	 Catch:{ all -> 0x01c5 }
        r2 = com.google.android.gms.internal.measurement.zzjv.zza(r2, r5, r6);	 Catch:{ all -> 0x01c5 }
        r0 = r28;
        r0.zzasv = r2;	 Catch:{ all -> 0x01c5 }
        r30.zzgc();	 Catch:{ all -> 0x01c5 }
        r0 = r28;
        r2 = r0.zzasv;	 Catch:{ all -> 0x01c5 }
        r5 = "_sr";
        r0 = r20;
        r6 = (long) r0;	 Catch:{ all -> 0x01c5 }
        r6 = java.lang.Long.valueOf(r6);	 Catch:{ all -> 0x01c5 }
        r2 = com.google.android.gms.internal.measurement.zzjv.zza(r2, r5, r6);	 Catch:{ all -> 0x01c5 }
        r0 = r28;
        r0.zzasv = r2;	 Catch:{ all -> 0x01c5 }
        r2 = r18 + 1;
        r24[r18] = r28;	 Catch:{ all -> 0x01c5 }
        r4 = r4.booleanValue();	 Catch:{ all -> 0x01c5 }
        if (r4 == 0) goto L_0x09ac;
    L_0x099b:
        r4 = 0;
        r0 = r20;
        r6 = (long) r0;	 Catch:{ all -> 0x01c5 }
        r5 = java.lang.Long.valueOf(r6);	 Catch:{ all -> 0x01c5 }
        r6 = 1;
        r6 = java.lang.Boolean.valueOf(r6);	 Catch:{ all -> 0x01c5 }
        r3 = r3.zza(r4, r5, r6);	 Catch:{ all -> 0x01c5 }
    L_0x09ac:
        r0 = r28;
        r4 = r0.name;	 Catch:{ all -> 0x01c5 }
        r0 = r28;
        r5 = r0.zzasw;	 Catch:{ all -> 0x01c5 }
        r6 = r5.longValue();	 Catch:{ all -> 0x01c5 }
        r3 = r3.zzad(r6);	 Catch:{ all -> 0x01c5 }
        r0 = r23;
        r0.put(r4, r3);	 Catch:{ all -> 0x01c5 }
        goto L_0x07a1;
    L_0x09c3:
        r4 = r4.booleanValue();	 Catch:{ all -> 0x01c5 }
        if (r4 == 0) goto L_0x09d8;
    L_0x09c9:
        r0 = r28;
        r4 = r0.name;	 Catch:{ all -> 0x01c5 }
        r5 = 0;
        r6 = 0;
        r2 = r3.zza(r2, r5, r6);	 Catch:{ all -> 0x01c5 }
        r0 = r23;
        r0.put(r4, r2);	 Catch:{ all -> 0x01c5 }
    L_0x09d8:
        r2 = r18;
        goto L_0x07a1;
    L_0x09dc:
        r0 = r22;
        r2 = r0.zzatd;	 Catch:{ all -> 0x01c5 }
        r2 = r2.length;	 Catch:{ all -> 0x01c5 }
        r0 = r18;
        if (r0 >= r2) goto L_0x09f3;
    L_0x09e5:
        r0 = r24;
        r1 = r18;
        r2 = java.util.Arrays.copyOf(r0, r1);	 Catch:{ all -> 0x01c5 }
        r2 = (com.google.android.gms.internal.measurement.zzki[]) r2;	 Catch:{ all -> 0x01c5 }
        r0 = r22;
        r0.zzatd = r2;	 Catch:{ all -> 0x01c5 }
    L_0x09f3:
        r2 = r23.entrySet();	 Catch:{ all -> 0x01c5 }
        r3 = r2.iterator();	 Catch:{ all -> 0x01c5 }
    L_0x09fb:
        r2 = r3.hasNext();	 Catch:{ all -> 0x01c5 }
        if (r2 == 0) goto L_0x0a15;
    L_0x0a01:
        r2 = r3.next();	 Catch:{ all -> 0x01c5 }
        r2 = (java.util.Map.Entry) r2;	 Catch:{ all -> 0x01c5 }
        r4 = r30.zzga();	 Catch:{ all -> 0x01c5 }
        r2 = r2.getValue();	 Catch:{ all -> 0x01c5 }
        r2 = (com.google.android.gms.internal.measurement.zzeq) r2;	 Catch:{ all -> 0x01c5 }
        r4.zza(r2);	 Catch:{ all -> 0x01c5 }
        goto L_0x09fb;
    L_0x0a15:
        r2 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
        r2 = java.lang.Long.valueOf(r2);	 Catch:{ all -> 0x01c5 }
        r0 = r22;
        r0.zzatg = r2;	 Catch:{ all -> 0x01c5 }
        r2 = -9223372036854775808;
        r2 = java.lang.Long.valueOf(r2);	 Catch:{ all -> 0x01c5 }
        r0 = r22;
        r0.zzath = r2;	 Catch:{ all -> 0x01c5 }
        r2 = 0;
    L_0x0a2d:
        r0 = r22;
        r3 = r0.zzatd;	 Catch:{ all -> 0x01c5 }
        r3 = r3.length;	 Catch:{ all -> 0x01c5 }
        if (r2 >= r3) goto L_0x0a6d;
    L_0x0a34:
        r0 = r22;
        r3 = r0.zzatd;	 Catch:{ all -> 0x01c5 }
        r3 = r3[r2];	 Catch:{ all -> 0x01c5 }
        r4 = r3.zzasw;	 Catch:{ all -> 0x01c5 }
        r4 = r4.longValue();	 Catch:{ all -> 0x01c5 }
        r0 = r22;
        r6 = r0.zzatg;	 Catch:{ all -> 0x01c5 }
        r6 = r6.longValue();	 Catch:{ all -> 0x01c5 }
        r4 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r4 >= 0) goto L_0x0a52;
    L_0x0a4c:
        r4 = r3.zzasw;	 Catch:{ all -> 0x01c5 }
        r0 = r22;
        r0.zzatg = r4;	 Catch:{ all -> 0x01c5 }
    L_0x0a52:
        r4 = r3.zzasw;	 Catch:{ all -> 0x01c5 }
        r4 = r4.longValue();	 Catch:{ all -> 0x01c5 }
        r0 = r22;
        r6 = r0.zzath;	 Catch:{ all -> 0x01c5 }
        r6 = r6.longValue();	 Catch:{ all -> 0x01c5 }
        r4 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r4 <= 0) goto L_0x0a6a;
    L_0x0a64:
        r3 = r3.zzasw;	 Catch:{ all -> 0x01c5 }
        r0 = r22;
        r0.zzath = r3;	 Catch:{ all -> 0x01c5 }
    L_0x0a6a:
        r2 = r2 + 1;
        goto L_0x0a2d;
    L_0x0a6d:
        r0 = r21;
        r2 = r0.zzans;	 Catch:{ all -> 0x01c5 }
        r6 = r2.zztd;	 Catch:{ all -> 0x01c5 }
        r2 = r30.zzga();	 Catch:{ all -> 0x01c5 }
        r7 = r2.zzax(r6);	 Catch:{ all -> 0x01c5 }
        if (r7 != 0) goto L_0x0b0b;
    L_0x0a7d:
        r2 = r30.zzgg();	 Catch:{ all -> 0x01c5 }
        r2 = r2.zzil();	 Catch:{ all -> 0x01c5 }
        r3 = "Bundling raw events w/o app info. appId";
        r0 = r21;
        r4 = r0.zzans;	 Catch:{ all -> 0x01c5 }
        r4 = r4.zztd;	 Catch:{ all -> 0x01c5 }
        r4 = com.google.android.gms.internal.measurement.zzfg.zzbh(r4);	 Catch:{ all -> 0x01c5 }
        r2.zzg(r3, r4);	 Catch:{ all -> 0x01c5 }
    L_0x0a95:
        r0 = r22;
        r2 = r0.zzatd;	 Catch:{ all -> 0x01c5 }
        r2 = r2.length;	 Catch:{ all -> 0x01c5 }
        if (r2 <= 0) goto L_0x0ad1;
    L_0x0a9c:
        r2 = r30.zzgd();	 Catch:{ all -> 0x01c5 }
        r0 = r21;
        r3 = r0.zzans;	 Catch:{ all -> 0x01c5 }
        r3 = r3.zztd;	 Catch:{ all -> 0x01c5 }
        r2 = r2.zzbp(r3);	 Catch:{ all -> 0x01c5 }
        if (r2 == 0) goto L_0x0ab0;
    L_0x0aac:
        r3 = r2.zzask;	 Catch:{ all -> 0x01c5 }
        if (r3 != 0) goto L_0x0b91;
    L_0x0ab0:
        r0 = r21;
        r2 = r0.zzans;	 Catch:{ all -> 0x01c5 }
        r2 = r2.zzadh;	 Catch:{ all -> 0x01c5 }
        r2 = android.text.TextUtils.isEmpty(r2);	 Catch:{ all -> 0x01c5 }
        if (r2 == 0) goto L_0x0b77;
    L_0x0abc:
        r2 = -1;
        r2 = java.lang.Long.valueOf(r2);	 Catch:{ all -> 0x01c5 }
        r0 = r22;
        r0.zzaua = r2;	 Catch:{ all -> 0x01c5 }
    L_0x0ac6:
        r2 = r30.zzga();	 Catch:{ all -> 0x01c5 }
        r0 = r22;
        r1 = r17;
        r2.zza(r0, r1);	 Catch:{ all -> 0x01c5 }
    L_0x0ad1:
        r4 = r30.zzga();	 Catch:{ all -> 0x01c5 }
        r0 = r21;
        r5 = r0.zzant;	 Catch:{ all -> 0x01c5 }
        com.google.android.gms.common.internal.Preconditions.checkNotNull(r5);	 Catch:{ all -> 0x01c5 }
        r4.zzab();	 Catch:{ all -> 0x01c5 }
        r4.zzch();	 Catch:{ all -> 0x01c5 }
        r7 = new java.lang.StringBuilder;	 Catch:{ all -> 0x01c5 }
        r2 = "rowid in (";
        r7.<init>(r2);	 Catch:{ all -> 0x01c5 }
        r2 = 0;
        r3 = r2;
    L_0x0aec:
        r2 = r5.size();	 Catch:{ all -> 0x01c5 }
        if (r3 >= r2) goto L_0x0b99;
    L_0x0af2:
        if (r3 == 0) goto L_0x0afa;
    L_0x0af4:
        r2 = ",";
        r7.append(r2);	 Catch:{ all -> 0x01c5 }
    L_0x0afa:
        r2 = r5.get(r3);	 Catch:{ all -> 0x01c5 }
        r2 = (java.lang.Long) r2;	 Catch:{ all -> 0x01c5 }
        r8 = r2.longValue();	 Catch:{ all -> 0x01c5 }
        r7.append(r8);	 Catch:{ all -> 0x01c5 }
        r2 = r3 + 1;
        r3 = r2;
        goto L_0x0aec;
    L_0x0b0b:
        r0 = r22;
        r2 = r0.zzatd;	 Catch:{ all -> 0x01c5 }
        r2 = r2.length;	 Catch:{ all -> 0x01c5 }
        if (r2 <= 0) goto L_0x0a95;
    L_0x0b12:
        r2 = r7.zzgn();	 Catch:{ all -> 0x01c5 }
        r4 = 0;
        r4 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
        if (r4 == 0) goto L_0x0b73;
    L_0x0b1c:
        r4 = java.lang.Long.valueOf(r2);	 Catch:{ all -> 0x01c5 }
    L_0x0b20:
        r0 = r22;
        r0.zzatj = r4;	 Catch:{ all -> 0x01c5 }
        r4 = r7.zzgm();	 Catch:{ all -> 0x01c5 }
        r8 = 0;
        r8 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1));
        if (r8 != 0) goto L_0x0c25;
    L_0x0b2e:
        r4 = 0;
        r4 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
        if (r4 == 0) goto L_0x0b75;
    L_0x0b34:
        r2 = java.lang.Long.valueOf(r2);	 Catch:{ all -> 0x01c5 }
    L_0x0b38:
        r0 = r22;
        r0.zzati = r2;	 Catch:{ all -> 0x01c5 }
        r7.zzgv();	 Catch:{ all -> 0x01c5 }
        r2 = r7.zzgs();	 Catch:{ all -> 0x01c5 }
        r2 = (int) r2;	 Catch:{ all -> 0x01c5 }
        r2 = java.lang.Integer.valueOf(r2);	 Catch:{ all -> 0x01c5 }
        r0 = r22;
        r0.zzatt = r2;	 Catch:{ all -> 0x01c5 }
        r0 = r22;
        r2 = r0.zzatg;	 Catch:{ all -> 0x01c5 }
        r2 = r2.longValue();	 Catch:{ all -> 0x01c5 }
        r7.zzm(r2);	 Catch:{ all -> 0x01c5 }
        r0 = r22;
        r2 = r0.zzath;	 Catch:{ all -> 0x01c5 }
        r2 = r2.longValue();	 Catch:{ all -> 0x01c5 }
        r7.zzn(r2);	 Catch:{ all -> 0x01c5 }
        r2 = r7.zzhd();	 Catch:{ all -> 0x01c5 }
        r0 = r22;
        r0.zzaef = r2;	 Catch:{ all -> 0x01c5 }
        r2 = r30.zzga();	 Catch:{ all -> 0x01c5 }
        r2.zza(r7);	 Catch:{ all -> 0x01c5 }
        goto L_0x0a95;
    L_0x0b73:
        r4 = 0;
        goto L_0x0b20;
    L_0x0b75:
        r2 = 0;
        goto L_0x0b38;
    L_0x0b77:
        r2 = r30.zzgg();	 Catch:{ all -> 0x01c5 }
        r2 = r2.zzin();	 Catch:{ all -> 0x01c5 }
        r3 = "Did not find measurement config or missing version info. appId";
        r0 = r21;
        r4 = r0.zzans;	 Catch:{ all -> 0x01c5 }
        r4 = r4.zztd;	 Catch:{ all -> 0x01c5 }
        r4 = com.google.android.gms.internal.measurement.zzfg.zzbh(r4);	 Catch:{ all -> 0x01c5 }
        r2.zzg(r3, r4);	 Catch:{ all -> 0x01c5 }
        goto L_0x0ac6;
    L_0x0b91:
        r2 = r2.zzask;	 Catch:{ all -> 0x01c5 }
        r0 = r22;
        r0.zzaua = r2;	 Catch:{ all -> 0x01c5 }
        goto L_0x0ac6;
    L_0x0b99:
        r2 = ")";
        r7.append(r2);	 Catch:{ all -> 0x01c5 }
        r2 = r4.getWritableDatabase();	 Catch:{ all -> 0x01c5 }
        r3 = "raw_events";
        r7 = r7.toString();	 Catch:{ all -> 0x01c5 }
        r8 = 0;
        r2 = r2.delete(r3, r7, r8);	 Catch:{ all -> 0x01c5 }
        r3 = r5.size();	 Catch:{ all -> 0x01c5 }
        if (r2 == r3) goto L_0x0bcf;
    L_0x0bb5:
        r3 = r4.zzgg();	 Catch:{ all -> 0x01c5 }
        r3 = r3.zzil();	 Catch:{ all -> 0x01c5 }
        r4 = "Deleted fewer rows from raw events table than expected";
        r2 = java.lang.Integer.valueOf(r2);	 Catch:{ all -> 0x01c5 }
        r5 = r5.size();	 Catch:{ all -> 0x01c5 }
        r5 = java.lang.Integer.valueOf(r5);	 Catch:{ all -> 0x01c5 }
        r3.zze(r4, r2, r5);	 Catch:{ all -> 0x01c5 }
    L_0x0bcf:
        r3 = r30.zzga();	 Catch:{ all -> 0x01c5 }
        r2 = r3.getWritableDatabase();	 Catch:{ all -> 0x01c5 }
        r4 = "delete from raw_events_metadata where app_id=? and metadata_fingerprint not in (select distinct metadata_fingerprint from raw_events where app_id=?)";
        r5 = 2;
        r5 = new java.lang.String[r5];	 Catch:{ SQLiteException -> 0x0bf6 }
        r7 = 0;
        r5[r7] = r6;	 Catch:{ SQLiteException -> 0x0bf6 }
        r7 = 1;
        r5[r7] = r6;	 Catch:{ SQLiteException -> 0x0bf6 }
        r2.execSQL(r4, r5);	 Catch:{ SQLiteException -> 0x0bf6 }
    L_0x0be6:
        r2 = r30.zzga();	 Catch:{ all -> 0x01c5 }
        r2.setTransactionSuccessful();	 Catch:{ all -> 0x01c5 }
        r2 = r30.zzga();
        r2.endTransaction();
        r2 = 1;
    L_0x0bf5:
        return r2;
    L_0x0bf6:
        r2 = move-exception;
        r3 = r3.zzgg();	 Catch:{ all -> 0x01c5 }
        r3 = r3.zzil();	 Catch:{ all -> 0x01c5 }
        r4 = "Failed to remove unused event metadata. appId";
        r5 = com.google.android.gms.internal.measurement.zzfg.zzbh(r6);	 Catch:{ all -> 0x01c5 }
        r3.zze(r4, r5, r2);	 Catch:{ all -> 0x01c5 }
        goto L_0x0be6;
    L_0x0c0a:
        r2 = r30.zzga();	 Catch:{ all -> 0x01c5 }
        r2.setTransactionSuccessful();	 Catch:{ all -> 0x01c5 }
        r2 = r30.zzga();
        r2.endTransaction();
        r2 = 0;
        goto L_0x0bf5;
    L_0x0c1a:
        r2 = move-exception;
        r3 = r11;
        goto L_0x036c;
    L_0x0c1e:
        r2 = move-exception;
        goto L_0x02f9;
    L_0x0c21:
        r2 = move-exception;
        r4 = r12;
        goto L_0x02f9;
    L_0x0c25:
        r2 = r4;
        goto L_0x0b2e;
    L_0x0c28:
        r3 = r2;
        goto L_0x08b4;
    L_0x0c2b:
        r20 = r2;
        goto L_0x07fd;
    L_0x0c2f:
        r2 = r14;
        goto L_0x057a;
    L_0x0c32:
        r2 = r3;
        goto L_0x051f;
    L_0x0c35:
        r17 = r2;
        goto L_0x04b9;
    L_0x0c39:
        r2 = r3;
        goto L_0x03be;
    L_0x0c3c:
        r2 = r14;
        r4 = r13;
        r5 = r17;
        goto L_0x0156;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzgl.zzd(java.lang.String, long):boolean");
    }

    static void zzfq() {
        throw new IllegalStateException("Unexpected call on client side");
    }

    public static zzgl zzg(Context context) {
        Preconditions.checkNotNull(context);
        Preconditions.checkNotNull(context.getApplicationContext());
        if (zzamc == null) {
            synchronized (zzgl.class) {
                if (zzamc == null) {
                    zzamc = new zzgl(new zzhl(context));
                }
            }
        }
        return zzamc;
    }

    private final zzfp zzjr() {
        if (this.zzamw != null) {
            return this.zzamw;
        }
        throw new IllegalStateException("Network broadcast receiver not created");
    }

    private final zzjq zzjs() {
        zza(this.zzamx);
        return this.zzamx;
    }

    private final long zzjv() {
        long currentTimeMillis = this.zzrj.currentTimeMillis();
        zzhj zzgh = zzgh();
        zzgh.zzch();
        zzgh.zzab();
        long j = zzgh.zzajx.get();
        if (j == 0) {
            j = 1 + ((long) zzgh.zzgc().zzku().nextInt(86400000));
            zzgh.zzajx.set(j);
        }
        return ((((j + currentTimeMillis) / 1000) / 60) / 60) / 24;
    }

    private final boolean zzjx() {
        zzgf().zzab();
        zzch();
        return zzga().zzhs() || !TextUtils.isEmpty(zzga().zzhn());
    }

    private final void zzjy() {
        zzgf().zzab();
        zzch();
        if (zzkb()) {
            long abs;
            if (this.zzank > 0) {
                abs = 3600000 - Math.abs(this.zzrj.elapsedRealtime() - this.zzank);
                if (abs > 0) {
                    zzgg().zzir().zzg("Upload has been suspended. Will update scheduling later in approximately ms", Long.valueOf(abs));
                    zzjr().unregister();
                    zzjs().cancel();
                    return;
                }
                this.zzank = 0;
            }
            if (zzjk() && zzjx()) {
                long currentTimeMillis = this.zzrj.currentTimeMillis();
                long max = Math.max(0, ((Long) zzew.zzahl.get()).longValue());
                Object obj = (zzga().zzht() || zzga().zzho()) ? 1 : null;
                if (obj != null) {
                    CharSequence zzhl = this.zzamd.zzhl();
                    abs = (TextUtils.isEmpty(zzhl) || ".none.".equals(zzhl)) ? Math.max(0, ((Long) zzew.zzahf.get()).longValue()) : Math.max(0, ((Long) zzew.zzahg.get()).longValue());
                } else {
                    abs = Math.max(0, ((Long) zzew.zzahe.get()).longValue());
                }
                long j = zzgh().zzajt.get();
                long j2 = zzgh().zzaju.get();
                long max2 = Math.max(zzga().zzhq(), zzga().zzhr());
                if (max2 == 0) {
                    currentTimeMillis = 0;
                } else {
                    max2 = currentTimeMillis - Math.abs(max2 - currentTimeMillis);
                    j2 = currentTimeMillis - Math.abs(j2 - currentTimeMillis);
                    j = Math.max(currentTimeMillis - Math.abs(j - currentTimeMillis), j2);
                    currentTimeMillis = max2 + max;
                    if (obj != null && j > 0) {
                        currentTimeMillis = Math.min(max2, j) + abs;
                    }
                    if (!zzgc().zza(j, abs)) {
                        currentTimeMillis = j + abs;
                    }
                    if (j2 != 0 && j2 >= max2) {
                        for (int i = 0; i < Math.min(20, Math.max(0, ((Integer) zzew.zzahn.get()).intValue())); i++) {
                            currentTimeMillis += (1 << i) * Math.max(0, ((Long) zzew.zzahm.get()).longValue());
                            if (currentTimeMillis > j2) {
                                break;
                            }
                        }
                        currentTimeMillis = 0;
                    }
                }
                if (currentTimeMillis == 0) {
                    zzgg().zzir().log("Next upload time is 0");
                    zzjr().unregister();
                    zzjs().cancel();
                    return;
                } else if (zzjq().zzew()) {
                    long j3 = zzgh().zzajv.get();
                    abs = Math.max(0, ((Long) zzew.zzahc.get()).longValue());
                    abs = !zzgc().zza(j3, abs) ? Math.max(currentTimeMillis, abs + j3) : currentTimeMillis;
                    zzjr().unregister();
                    abs -= this.zzrj.currentTimeMillis();
                    if (abs <= 0) {
                        abs = Math.max(0, ((Long) zzew.zzahh.get()).longValue());
                        zzgh().zzajt.set(this.zzrj.currentTimeMillis());
                    }
                    zzgg().zzir().zzg("Upload scheduled in approximately ms", Long.valueOf(abs));
                    zzjs().zzh(abs);
                    return;
                } else {
                    zzgg().zzir().log("No network");
                    zzjr().zzet();
                    zzjs().cancel();
                    return;
                }
            }
            zzgg().zzir().log("Nothing to upload or uploading impossible");
            zzjr().unregister();
            zzjs().cancel();
        }
    }

    private final boolean zzkb() {
        zzgf().zzab();
        zzch();
        return this.zzana;
    }

    private final void zzkc() {
        zzgf().zzab();
        if (this.zzanl || this.zzanm || this.zzann) {
            zzgg().zzir().zzd("Not stopping services. fetch, network, upload", Boolean.valueOf(this.zzanl), Boolean.valueOf(this.zzanm), Boolean.valueOf(this.zzann));
            return;
        }
        zzgg().zzir().log("Stopping uploading service(s)");
        if (this.zzang != null) {
            for (Runnable run : this.zzang) {
                run.run();
            }
            this.zzang.clear();
        }
    }

    public final Context getContext() {
        return this.zzqs;
    }

    public final boolean isEnabled() {
        boolean z = false;
        zzgf().zzab();
        zzch();
        if (this.zzamd.zzhi()) {
            return false;
        }
        Boolean zzas = this.zzamd.zzas("firebase_analytics_collection_enabled");
        if (zzas != null) {
            z = zzas.booleanValue();
        } else if (!GoogleServices.isMeasurementExplicitlyDisabled()) {
            z = true;
        }
        return zzgh().zzg(z);
    }

    protected final void start() {
        boolean z = false;
        zzgf().zzab();
        zzga().zzhp();
        if (zzgh().zzajt.get() == 0) {
            zzgh().zzajt.set(this.zzrj.currentTimeMillis());
        }
        if (Long.valueOf(zzgh().zzajy.get()).longValue() == 0) {
            zzgg().zzir().zzg("Persisting first open", Long.valueOf(this.zzaeh));
            zzgh().zzajy.set(this.zzaeh);
        }
        if (zzjk()) {
            if (!TextUtils.isEmpty(zzfv().getGmpAppId())) {
                String zziv = zzgh().zziv();
                if (zziv == null) {
                    zzgh().zzbl(zzfv().getGmpAppId());
                } else if (!zziv.equals(zzfv().getGmpAppId())) {
                    zzgg().zzip().log("Rechecking which service to use due to a GMP App Id change");
                    zzgh().zziy();
                    this.zzamt.disconnect();
                    this.zzamt.zzdf();
                    zzgh().zzbl(zzfv().getGmpAppId());
                    zzgh().zzajy.set(this.zzaeh);
                    zzgh().zzaka.zzbn(null);
                }
            }
            zzfu().zzbm(zzgh().zzaka.zzjc());
            if (!TextUtils.isEmpty(zzfv().getGmpAppId())) {
                boolean isEnabled = isEnabled();
                if (!(zzgh().zzjb() || this.zzamd.zzhi())) {
                    zzfr zzgh = zzgh();
                    if (!isEnabled) {
                        z = true;
                    }
                    zzgh.zzh(z);
                }
                if (!this.zzamd.zzav(zzfv().zzah()) || isEnabled) {
                    zzfu().zzkj();
                }
                zzfx().zza(new AtomicReference());
            }
        } else if (isEnabled()) {
            if (!zzgc().zzx("android.permission.INTERNET")) {
                zzgg().zzil().log("App is missing INTERNET permission");
            }
            if (!zzgc().zzx("android.permission.ACCESS_NETWORK_STATE")) {
                zzgg().zzil().log("App is missing ACCESS_NETWORK_STATE permission");
            }
            if (!Wrappers.packageManager(this.zzqs).isCallerInstantApp()) {
                if (!zzgb.zza(this.zzqs)) {
                    zzgg().zzil().log("AppMeasurementReceiver not registered/enabled");
                }
                if (!zzjf.zza(this.zzqs, false)) {
                    zzgg().zzil().log("AppMeasurementService not registered/enabled");
                }
            }
            zzgg().zzil().log("Uploading is not possible. App measurement disabled");
        }
        zzjy();
    }

    protected final void zza(int i, Throwable th, byte[] bArr, String str) {
        zzgf().zzab();
        zzch();
        if (bArr == null) {
            try {
                bArr = new byte[0];
            } catch (Throwable th2) {
                this.zzanm = false;
                zzkc();
            }
        }
        List<Long> list = this.zzanf;
        this.zzanf = null;
        if ((i == Callback.DEFAULT_DRAG_ANIMATION_DURATION || i == 204) && th == null) {
            try {
                zzgh().zzajt.set(this.zzrj.currentTimeMillis());
                zzgh().zzaju.set(0);
                zzjy();
                zzgg().zzir().zze("Successful upload. Got network response. code, size", Integer.valueOf(i), Integer.valueOf(bArr.length));
                zzga().beginTransaction();
                zzhj zzga;
                try {
                    for (Long l : list) {
                        zzga = zzga();
                        long longValue = l.longValue();
                        zzga.zzab();
                        zzga.zzch();
                        if (zzga.getWritableDatabase().delete("queue", "rowid=?", new String[]{String.valueOf(longValue)}) != 1) {
                            throw new SQLiteException("Deleted fewer rows from queue than expected");
                        }
                    }
                    zzga().setTransactionSuccessful();
                    zzga().endTransaction();
                    if (zzjq().zzew() && zzjx()) {
                        zzjw();
                    } else {
                        this.zzanj = -1;
                        zzjy();
                    }
                    this.zzank = 0;
                } catch (SQLiteException e) {
                    zzga.zzgg().zzil().zzg("Failed to delete a bundle in a queue table", e);
                    throw e;
                } catch (Throwable th3) {
                    zzga().endTransaction();
                }
            } catch (SQLiteException e2) {
                zzgg().zzil().zzg("Database error while trying to delete uploaded bundles", e2);
                this.zzank = this.zzrj.elapsedRealtime();
                zzgg().zzir().zzg("Disable upload, time", Long.valueOf(this.zzank));
            }
        } else {
            zzgg().zzir().zze("Network upload failed. Will retry later. code, error", Integer.valueOf(i), th);
            zzgh().zzaju.set(this.zzrj.currentTimeMillis());
            boolean z = i == 503 || i == 429;
            if (z) {
                zzgh().zzajv.set(this.zzrj.currentTimeMillis());
            }
            if (this.zzamd.zzd(str, zzew.zzaia)) {
                zzga().zzc(list);
            }
            zzjy();
        }
        this.zzanm = false;
        zzkc();
    }

    final void zzb(zzhk com_google_android_gms_internal_measurement_zzhk) {
        this.zzanh++;
    }

    final void zzb(String str, int i, Throwable th, byte[] bArr, Map<String, List<String>> map) {
        boolean z = true;
        zzgf().zzab();
        zzch();
        Preconditions.checkNotEmpty(str);
        if (bArr == null) {
            try {
                bArr = new byte[0];
            } catch (Throwable th2) {
                this.zzanl = false;
                zzkc();
            }
        }
        zzgg().zzir().zzg("onConfigFetched. Response size", Integer.valueOf(bArr.length));
        zzga().beginTransaction();
        zzeb zzax = zzga().zzax(str);
        boolean z2 = (i == Callback.DEFAULT_DRAG_ANIMATION_DURATION || i == 204 || i == 304) && th == null;
        if (zzax == null) {
            zzgg().zzin().zzg("App does not exist in onConfigFetched. appId", zzfg.zzbh(str));
        } else if (z2 || i == 404) {
            List list = map != null ? (List) map.get("Last-Modified") : null;
            String str2 = (list == null || list.size() <= 0) ? null : (String) list.get(0);
            if (i == 404 || i == 304) {
                if (zzgd().zzbp(str) == null && !zzgd().zza(str, null, null)) {
                    zzga().endTransaction();
                    this.zzanl = false;
                    zzkc();
                    return;
                }
            } else if (!zzgd().zza(str, bArr, str2)) {
                zzga().endTransaction();
                this.zzanl = false;
                zzkc();
                return;
            }
            zzax.zzs(this.zzrj.currentTimeMillis());
            zzga().zza(zzax);
            if (i == 404) {
                zzgg().zzio().zzg("Config not found. Using empty config. appId", str);
            } else {
                zzgg().zzir().zze("Successfully fetched config. Got network response. code, size", Integer.valueOf(i), Integer.valueOf(bArr.length));
            }
            if (zzjq().zzew() && zzjx()) {
                zzjw();
            } else {
                zzjy();
            }
        } else {
            zzax.zzt(this.zzrj.currentTimeMillis());
            zzga().zza(zzax);
            zzgg().zzir().zze("Fetching config failed. code, error", Integer.valueOf(i), th);
            zzgd().zzbr(str);
            zzgh().zzaju.set(this.zzrj.currentTimeMillis());
            if (!(i == 503 || i == 429)) {
                z = false;
            }
            if (z) {
                zzgh().zzajv.set(this.zzrj.currentTimeMillis());
            }
            zzjy();
        }
        zzga().setTransactionSuccessful();
        zzga().endTransaction();
        this.zzanl = false;
        zzkc();
    }

    public final Clock zzbt() {
        return this.zzrj;
    }

    final void zzch() {
        if (!this.zzvj) {
            throw new IllegalStateException("AppMeasurement is not initialized");
        }
    }

    public final zzdx zzfs() {
        zza(this.zzamq);
        return this.zzamq;
    }

    public final zzee zzft() {
        zza(this.zzamy);
        return this.zzamy;
    }

    public final zzhm zzfu() {
        zza(this.zzamp);
        return this.zzamp;
    }

    public final zzfb zzfv() {
        zza(this.zzamv);
        return this.zzamv;
    }

    public final zzeo zzfw() {
        zza(this.zzamu);
        return this.zzamu;
    }

    public final zzil zzfx() {
        zza(this.zzamt);
        return this.zzamt;
    }

    public final zzih zzfy() {
        zza(this.zzamo);
        return this.zzamo;
    }

    public final zzfc zzfz() {
        zza(this.zzams);
        return this.zzams;
    }

    public final zzei zzga() {
        zza(this.zzamr);
        return this.zzamr;
    }

    public final zzfe zzgb() {
        zza(this.zzamm);
        return this.zzamm;
    }

    public final zzjv zzgc() {
        zza(this.zzaml);
        return this.zzaml;
    }

    public final zzgf zzgd() {
        zza(this.zzami);
        return this.zzami;
    }

    public final zzjk zzge() {
        zza(this.zzamh);
        return this.zzamh;
    }

    public final zzgg zzgf() {
        zza(this.zzamg);
        return this.zzamg;
    }

    public final zzfg zzgg() {
        zza(this.zzamf);
        return this.zzamf;
    }

    public final zzfr zzgh() {
        zza(this.zzame);
        return this.zzame;
    }

    public final zzeh zzgi() {
        return this.zzamd;
    }

    public final void zzi(boolean z) {
        zzjy();
    }

    protected final boolean zzjk() {
        boolean z = false;
        zzch();
        zzgf().zzab();
        if (this.zzanb == null || this.zzanc == 0 || !(this.zzanb == null || this.zzanb.booleanValue() || Math.abs(this.zzrj.elapsedRealtime() - this.zzanc) <= 1000)) {
            this.zzanc = this.zzrj.elapsedRealtime();
            if (zzgc().zzx("android.permission.INTERNET") && zzgc().zzx("android.permission.ACCESS_NETWORK_STATE") && (Wrappers.packageManager(this.zzqs).isCallerInstantApp() || (zzgb.zza(this.zzqs) && zzjf.zza(this.zzqs, false)))) {
                z = true;
            }
            this.zzanb = Boolean.valueOf(z);
            if (this.zzanb.booleanValue()) {
                this.zzanb = Boolean.valueOf(zzgc().zzbz(zzfv().getGmpAppId()));
            }
        }
        return this.zzanb.booleanValue();
    }

    public final zzfg zzjl() {
        return (this.zzamf == null || !this.zzamf.isInitialized()) ? null : this.zzamf;
    }

    final zzgg zzjn() {
        return this.zzamg;
    }

    public final AppMeasurement zzjo() {
        return this.zzamj;
    }

    public final FirebaseAnalytics zzjp() {
        return this.zzamk;
    }

    public final zzfk zzjq() {
        zza(this.zzamn);
        return this.zzamn;
    }

    final long zzju() {
        Long valueOf = Long.valueOf(zzgh().zzajy.get());
        return valueOf.longValue() == 0 ? this.zzaeh : Math.min(this.zzaeh, valueOf.longValue());
    }

    public final void zzjw() {
        zzgf().zzab();
        zzch();
        this.zzann = true;
        String zzhn;
        String str;
        try {
            Boolean zzkn = zzfx().zzkn();
            if (zzkn == null) {
                zzgg().zzin().log("Upload data called on the client side before use of service was decided");
                this.zzann = false;
                zzkc();
            } else if (zzkn.booleanValue()) {
                zzgg().zzil().log("Upload called in the client side when service should be used");
                this.zzann = false;
                zzkc();
            } else if (this.zzank > 0) {
                zzjy();
                this.zzann = false;
                zzkc();
            } else {
                zzgf().zzab();
                if ((this.zzanf != null ? 1 : null) != null) {
                    zzgg().zzir().log("Uploading requested multiple times");
                    this.zzann = false;
                    zzkc();
                } else if (zzjq().zzew()) {
                    long currentTimeMillis = this.zzrj.currentTimeMillis();
                    zzd(null, currentTimeMillis - zzeh.zzhk());
                    long j = zzgh().zzajt.get();
                    if (j != 0) {
                        zzgg().zziq().zzg("Uploading events. Elapsed time since last upload attempt (ms)", Long.valueOf(Math.abs(currentTimeMillis - j)));
                    }
                    zzhn = zzga().zzhn();
                    Object zzab;
                    if (TextUtils.isEmpty(zzhn)) {
                        this.zzanj = -1;
                        zzab = zzga().zzab(currentTimeMillis - zzeh.zzhk());
                        if (!TextUtils.isEmpty(zzab)) {
                            zzeb zzax = zzga().zzax(zzab);
                            if (zzax != null) {
                                zzb(zzax);
                            }
                        }
                    } else {
                        if (this.zzanj == -1) {
                            this.zzanj = zzga().zzhu();
                        }
                        List<Pair> zzb = zzga().zzb(zzhn, this.zzamd.zzb(zzhn, zzew.zzagr), Math.max(0, this.zzamd.zzb(zzhn, zzew.zzags)));
                        if (!zzb.isEmpty()) {
                            zzkl com_google_android_gms_internal_measurement_zzkl;
                            Object obj;
                            int i;
                            List subList;
                            for (Pair pair : zzb) {
                                com_google_android_gms_internal_measurement_zzkl = (zzkl) pair.first;
                                if (!TextUtils.isEmpty(com_google_android_gms_internal_measurement_zzkl.zzatq)) {
                                    obj = com_google_android_gms_internal_measurement_zzkl.zzatq;
                                    break;
                                }
                            }
                            obj = null;
                            if (obj != null) {
                                for (i = 0; i < zzb.size(); i++) {
                                    com_google_android_gms_internal_measurement_zzkl = (zzkl) ((Pair) zzb.get(i)).first;
                                    if (!TextUtils.isEmpty(com_google_android_gms_internal_measurement_zzkl.zzatq) && !com_google_android_gms_internal_measurement_zzkl.zzatq.equals(obj)) {
                                        subList = zzb.subList(0, i);
                                        break;
                                    }
                                }
                            }
                            subList = zzb;
                            zzkk com_google_android_gms_internal_measurement_zzkk = new zzkk();
                            com_google_android_gms_internal_measurement_zzkk.zzata = new zzkl[subList.size()];
                            Collection arrayList = new ArrayList(subList.size());
                            Object obj2 = (zzeh.zzhm() && this.zzamd.zzat(zzhn)) ? 1 : null;
                            for (i = 0; i < com_google_android_gms_internal_measurement_zzkk.zzata.length; i++) {
                                com_google_android_gms_internal_measurement_zzkk.zzata[i] = (zzkl) ((Pair) subList.get(i)).first;
                                arrayList.add((Long) ((Pair) subList.get(i)).second);
                                com_google_android_gms_internal_measurement_zzkk.zzata[i].zzatp = Long.valueOf(12451);
                                com_google_android_gms_internal_measurement_zzkk.zzata[i].zzatf = Long.valueOf(currentTimeMillis);
                                com_google_android_gms_internal_measurement_zzkk.zzata[i].zzatu = Boolean.valueOf(false);
                                if (obj2 == null) {
                                    com_google_android_gms_internal_measurement_zzkk.zzata[i].zzauc = null;
                                }
                            }
                            obj2 = zzgg().isLoggable(2) ? zzgb().zza(com_google_android_gms_internal_measurement_zzkk) : null;
                            obj = zzgc().zzb(com_google_android_gms_internal_measurement_zzkk);
                            str = (String) zzew.zzahb.get();
                            URL url = new URL(str);
                            Preconditions.checkArgument(!arrayList.isEmpty());
                            if (this.zzanf != null) {
                                zzgg().zzil().log("Set uploading progress before finishing the previous upload");
                            } else {
                                this.zzanf = new ArrayList(arrayList);
                            }
                            zzgh().zzaju.set(currentTimeMillis);
                            zzab = "?";
                            if (com_google_android_gms_internal_measurement_zzkk.zzata.length > 0) {
                                zzab = com_google_android_gms_internal_measurement_zzkk.zzata[0].zztd;
                            }
                            zzgg().zzir().zzd("Uploading data. app, uncompressed size, data", zzab, Integer.valueOf(obj.length), obj2);
                            this.zzanm = true;
                            zzhj zzjq = zzjq();
                            zzfm com_google_android_gms_internal_measurement_zzgo = new zzgo(this, zzhn);
                            zzjq.zzab();
                            zzjq.zzch();
                            Preconditions.checkNotNull(url);
                            Preconditions.checkNotNull(obj);
                            Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzgo);
                            zzjq.zzgf().zzd(new zzfo(zzjq, zzhn, url, obj, null, com_google_android_gms_internal_measurement_zzgo));
                        }
                    }
                    this.zzann = false;
                    zzkc();
                } else {
                    zzgg().zzir().log("Network not connected, ignoring upload request");
                    zzjy();
                    this.zzann = false;
                    zzkc();
                }
            }
        } catch (MalformedURLException e) {
            zzgg().zzil().zze("Failed to parse upload URL. Not uploading. appId", zzfg.zzbh(zzhn), str);
        } catch (Throwable th) {
            this.zzann = false;
            zzkc();
        }
    }

    final void zzjz() {
        this.zzani++;
    }
}
