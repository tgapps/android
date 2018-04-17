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
        zzfi zzir;
        String str;
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
                zzir = zzfu.zzgg().zzir();
                str = "Registered activity lifecycle callback";
            }
            this.zzamg.zzc(new zzgm(this, com_google_android_gms_internal_measurement_zzhl));
        }
        zzir = zzgg().zzin();
        str = "Application context is not an Application";
        zzir.log(str);
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
            StringBuilder stringBuilder = new StringBuilder(27 + String.valueOf(valueOf).length());
            stringBuilder.append("Component not initialized: ");
            stringBuilder.append(valueOf);
            throw new IllegalStateException(stringBuilder.toString());
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
        gmpAppId = String.valueOf(gmpAppId);
        encodedAuthority.path(gmpAppId.length() != 0 ? str.concat(gmpAppId) : new String(str)).appendQueryParameter("app_instance_id", appInstanceId).appendQueryParameter("platform", "android").appendQueryParameter("gmp_version", "12451");
        gmpAppId = builder.build().toString();
        try {
            Map map;
            URL url = new URL(gmpAppId);
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
            String zzah = com_google_android_gms_internal_measurement_zzeb.zzah();
            zzfm com_google_android_gms_internal_measurement_zzgp = new zzgp(this);
            zzjq.zzab();
            zzjq.zzch();
            Preconditions.checkNotNull(url);
            Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzgp);
            zzjq.zzgf().zzd(new zzfo(zzjq, zzah, url, null, map, com_google_android_gms_internal_measurement_zzgp));
        } catch (MalformedURLException e) {
            zzgg().zzil().zze("Failed to parse config URL. Not fetching. appId", zzfg.zzbh(com_google_android_gms_internal_measurement_zzeb.zzah()), gmpAppId);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final boolean zzd(java.lang.String r60, long r61) {
        /*
        r59 = this;
        r1 = r59;
        r2 = r59.zzga();
        r2.beginTransaction();
        r2 = new com.google.android.gms.internal.measurement.zzgl$zza;	 Catch:{ all -> 0x0b35 }
        r3 = 0;
        r2.<init>();	 Catch:{ all -> 0x0b35 }
        r4 = r59.zzga();	 Catch:{ all -> 0x0b35 }
        r5 = r1.zzanj;	 Catch:{ all -> 0x0b35 }
        com.google.android.gms.common.internal.Preconditions.checkNotNull(r2);	 Catch:{ all -> 0x0b35 }
        r4.zzab();	 Catch:{ all -> 0x0b35 }
        r4.zzch();	 Catch:{ all -> 0x0b35 }
        r7 = -1;
        r9 = 2;
        r10 = 0;
        r11 = 1;
        r15 = r4.getWritableDatabase();	 Catch:{ SQLiteException -> 0x026f, all -> 0x026a }
        r12 = android.text.TextUtils.isEmpty(r3);	 Catch:{ SQLiteException -> 0x026f, all -> 0x026a }
        if (r12 == 0) goto L_0x00a2;
    L_0x002d:
        r12 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1));
        if (r12 == 0) goto L_0x004b;
    L_0x0031:
        r12 = new java.lang.String[r9];	 Catch:{ SQLiteException -> 0x0045, all -> 0x0040 }
        r13 = java.lang.String.valueOf(r5);	 Catch:{ SQLiteException -> 0x0045, all -> 0x0040 }
        r12[r10] = r13;	 Catch:{ SQLiteException -> 0x0045, all -> 0x0040 }
        r13 = java.lang.String.valueOf(r61);	 Catch:{ SQLiteException -> 0x0045, all -> 0x0040 }
        r12[r11] = r13;	 Catch:{ SQLiteException -> 0x0045, all -> 0x0040 }
        goto L_0x0053;
    L_0x0040:
        r0 = move-exception;
        r2 = r0;
        r6 = r3;
        goto L_0x0b2f;
    L_0x0045:
        r0 = move-exception;
        r6 = r3;
        r12 = r6;
    L_0x0048:
        r3 = r0;
        goto L_0x0273;
    L_0x004b:
        r12 = new java.lang.String[r11];	 Catch:{ SQLiteException -> 0x026f, all -> 0x026a }
        r13 = java.lang.String.valueOf(r61);	 Catch:{ SQLiteException -> 0x026f, all -> 0x026a }
        r12[r10] = r13;	 Catch:{ SQLiteException -> 0x026f, all -> 0x026a }
    L_0x0053:
        r13 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1));
        if (r13 == 0) goto L_0x005a;
    L_0x0057:
        r13 = "rowid <= ? and ";
        goto L_0x005c;
    L_0x005a:
        r13 = "";
    L_0x005c:
        r14 = 148; // 0x94 float:2.07E-43 double:7.3E-322;
        r3 = java.lang.String.valueOf(r13);	 Catch:{ SQLiteException -> 0x026f, all -> 0x026a }
        r3 = r3.length();	 Catch:{ SQLiteException -> 0x026f, all -> 0x026a }
        r14 = r14 + r3;
        r3 = new java.lang.StringBuilder;	 Catch:{ SQLiteException -> 0x026f, all -> 0x026a }
        r3.<init>(r14);	 Catch:{ SQLiteException -> 0x026f, all -> 0x026a }
        r14 = "select app_id, metadata_fingerprint from raw_events where ";
        r3.append(r14);	 Catch:{ SQLiteException -> 0x026f, all -> 0x026a }
        r3.append(r13);	 Catch:{ SQLiteException -> 0x026f, all -> 0x026a }
        r13 = "app_id in (select app_id from apps where config_fetched_time >= ?) order by rowid limit 1;";
        r3.append(r13);	 Catch:{ SQLiteException -> 0x026f, all -> 0x026a }
        r3 = r3.toString();	 Catch:{ SQLiteException -> 0x026f, all -> 0x026a }
        r3 = r15.rawQuery(r3, r12);	 Catch:{ SQLiteException -> 0x026f, all -> 0x026a }
        r12 = r3.moveToFirst();	 Catch:{ SQLiteException -> 0x0265, all -> 0x0040 }
        if (r12 != 0) goto L_0x008e;
    L_0x0087:
        if (r3 == 0) goto L_0x0287;
    L_0x0089:
        r3.close();	 Catch:{ all -> 0x0b35 }
        goto L_0x0287;
    L_0x008e:
        r12 = r3.getString(r10);	 Catch:{ SQLiteException -> 0x0265, all -> 0x0040 }
        r13 = r3.getString(r11);	 Catch:{ SQLiteException -> 0x009f, all -> 0x0040 }
        r3.close();	 Catch:{ SQLiteException -> 0x009f, all -> 0x0040 }
        r22 = r3;
        r3 = r12;
        r21 = r13;
        goto L_0x00fa;
    L_0x009f:
        r0 = move-exception;
        r6 = r3;
        goto L_0x0048;
    L_0x00a2:
        r3 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1));
        if (r3 == 0) goto L_0x00b2;
    L_0x00a6:
        r3 = new java.lang.String[r9];	 Catch:{ SQLiteException -> 0x026f, all -> 0x026a }
        r12 = 0;
        r3[r10] = r12;	 Catch:{ SQLiteException -> 0x026f, all -> 0x026a }
        r12 = java.lang.String.valueOf(r5);	 Catch:{ SQLiteException -> 0x026f, all -> 0x026a }
        r3[r11] = r12;	 Catch:{ SQLiteException -> 0x026f, all -> 0x026a }
        goto L_0x00b7;
    L_0x00b2:
        r3 = new java.lang.String[r11];	 Catch:{ SQLiteException -> 0x026f, all -> 0x026a }
        r12 = 0;
        r3[r10] = r12;	 Catch:{ SQLiteException -> 0x026f, all -> 0x026a }
    L_0x00b7:
        r12 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1));
        if (r12 == 0) goto L_0x00be;
    L_0x00bb:
        r12 = " and rowid <= ?";
        goto L_0x00c0;
    L_0x00be:
        r12 = "";
    L_0x00c0:
        r13 = 84;
        r14 = java.lang.String.valueOf(r12);	 Catch:{ SQLiteException -> 0x026f, all -> 0x026a }
        r14 = r14.length();	 Catch:{ SQLiteException -> 0x026f, all -> 0x026a }
        r13 = r13 + r14;
        r14 = new java.lang.StringBuilder;	 Catch:{ SQLiteException -> 0x026f, all -> 0x026a }
        r14.<init>(r13);	 Catch:{ SQLiteException -> 0x026f, all -> 0x026a }
        r13 = "select metadata_fingerprint from raw_events where app_id = ?";
        r14.append(r13);	 Catch:{ SQLiteException -> 0x026f, all -> 0x026a }
        r14.append(r12);	 Catch:{ SQLiteException -> 0x026f, all -> 0x026a }
        r12 = " order by rowid limit 1;";
        r14.append(r12);	 Catch:{ SQLiteException -> 0x026f, all -> 0x026a }
        r12 = r14.toString();	 Catch:{ SQLiteException -> 0x026f, all -> 0x026a }
        r3 = r15.rawQuery(r12, r3);	 Catch:{ SQLiteException -> 0x026f, all -> 0x026a }
        r12 = r3.moveToFirst();	 Catch:{ SQLiteException -> 0x0265, all -> 0x0040 }
        if (r12 != 0) goto L_0x00ee;
    L_0x00eb:
        if (r3 == 0) goto L_0x0287;
    L_0x00ed:
        goto L_0x0089;
    L_0x00ee:
        r13 = r3.getString(r10);	 Catch:{ SQLiteException -> 0x0265, all -> 0x0040 }
        r3.close();	 Catch:{ SQLiteException -> 0x0265, all -> 0x0040 }
        r22 = r3;
        r21 = r13;
        r3 = 0;
    L_0x00fa:
        r13 = "raw_events_metadata";
        r14 = new java.lang.String[r11];	 Catch:{ SQLiteException -> 0x025f, all -> 0x0259 }
        r12 = "metadata";
        r14[r10] = r12;	 Catch:{ SQLiteException -> 0x025f, all -> 0x0259 }
        r16 = "app_id = ? and metadata_fingerprint = ?";
        r12 = new java.lang.String[r9];	 Catch:{ SQLiteException -> 0x025f, all -> 0x0259 }
        r12[r10] = r3;	 Catch:{ SQLiteException -> 0x025f, all -> 0x0259 }
        r12[r11] = r21;	 Catch:{ SQLiteException -> 0x025f, all -> 0x0259 }
        r17 = 0;
        r18 = 0;
        r19 = "rowid";
        r20 = "2";
        r23 = r12;
        r12 = r15;
        r24 = r15;
        r15 = r16;
        r16 = r23;
        r15 = r12.query(r13, r14, r15, r16, r17, r18, r19, r20);	 Catch:{ SQLiteException -> 0x025f, all -> 0x0259 }
        r12 = r15.moveToFirst();	 Catch:{ SQLiteException -> 0x0254, all -> 0x0250 }
        if (r12 != 0) goto L_0x0147;
    L_0x0125:
        r5 = r4.zzgg();	 Catch:{ SQLiteException -> 0x0142, all -> 0x013d }
        r5 = r5.zzil();	 Catch:{ SQLiteException -> 0x0142, all -> 0x013d }
        r6 = "Raw event metadata record is missing. appId";
        r12 = com.google.android.gms.internal.measurement.zzfg.zzbh(r3);	 Catch:{ SQLiteException -> 0x0142, all -> 0x013d }
        r5.zzg(r6, r12);	 Catch:{ SQLiteException -> 0x0142, all -> 0x013d }
        if (r15 == 0) goto L_0x0287;
    L_0x0138:
        r15.close();	 Catch:{ all -> 0x0b35 }
        goto L_0x0287;
    L_0x013d:
        r0 = move-exception;
        r2 = r0;
        r6 = r15;
        goto L_0x0b2f;
    L_0x0142:
        r0 = move-exception;
        r12 = r3;
        r6 = r15;
        goto L_0x0048;
    L_0x0147:
        r12 = r15.getBlob(r10);	 Catch:{ SQLiteException -> 0x0254, all -> 0x0250 }
        r13 = r12.length;	 Catch:{ SQLiteException -> 0x0254, all -> 0x0250 }
        r12 = com.google.android.gms.internal.measurement.zzaba.zza(r12, r10, r13);	 Catch:{ SQLiteException -> 0x0254, all -> 0x0250 }
        r13 = new com.google.android.gms.internal.measurement.zzkl;	 Catch:{ SQLiteException -> 0x0254, all -> 0x0250 }
        r13.<init>();	 Catch:{ SQLiteException -> 0x0254, all -> 0x0250 }
        r13.zzb(r12);	 Catch:{ IOException -> 0x0235 }
        r12 = r15.moveToNext();	 Catch:{ SQLiteException -> 0x0254, all -> 0x0250 }
        if (r12 == 0) goto L_0x016f;
    L_0x015e:
        r12 = r4.zzgg();	 Catch:{ SQLiteException -> 0x0142, all -> 0x013d }
        r12 = r12.zzin();	 Catch:{ SQLiteException -> 0x0142, all -> 0x013d }
        r14 = "Get multiple raw event metadata records, expected one. appId";
        r9 = com.google.android.gms.internal.measurement.zzfg.zzbh(r3);	 Catch:{ SQLiteException -> 0x0142, all -> 0x013d }
        r12.zzg(r14, r9);	 Catch:{ SQLiteException -> 0x0142, all -> 0x013d }
    L_0x016f:
        r15.close();	 Catch:{ SQLiteException -> 0x0254, all -> 0x0250 }
        r2.zzb(r13);	 Catch:{ SQLiteException -> 0x0254, all -> 0x0250 }
        r9 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1));
        r14 = 3;
        if (r9 == 0) goto L_0x018d;
    L_0x017a:
        r9 = "app_id = ? and metadata_fingerprint = ? and rowid <= ?";
        r12 = new java.lang.String[r14];	 Catch:{ SQLiteException -> 0x0142, all -> 0x013d }
        r12[r10] = r3;	 Catch:{ SQLiteException -> 0x0142, all -> 0x013d }
        r12[r11] = r21;	 Catch:{ SQLiteException -> 0x0142, all -> 0x013d }
        r5 = java.lang.String.valueOf(r5);	 Catch:{ SQLiteException -> 0x0142, all -> 0x013d }
        r6 = 2;
        r12[r6] = r5;	 Catch:{ SQLiteException -> 0x0142, all -> 0x013d }
        r5 = r9;
        r16 = r12;
        goto L_0x0198;
    L_0x018d:
        r5 = "app_id = ? and metadata_fingerprint = ?";
        r6 = 2;
        r9 = new java.lang.String[r6];	 Catch:{ SQLiteException -> 0x0254, all -> 0x0250 }
        r9[r10] = r3;	 Catch:{ SQLiteException -> 0x0254, all -> 0x0250 }
        r9[r11] = r21;	 Catch:{ SQLiteException -> 0x0254, all -> 0x0250 }
        r16 = r9;
    L_0x0198:
        r13 = "raw_events";
        r6 = 4;
        r6 = new java.lang.String[r6];	 Catch:{ SQLiteException -> 0x0254, all -> 0x0250 }
        r9 = "rowid";
        r6[r10] = r9;	 Catch:{ SQLiteException -> 0x0254, all -> 0x0250 }
        r9 = "name";
        r6[r11] = r9;	 Catch:{ SQLiteException -> 0x0254, all -> 0x0250 }
        r9 = "timestamp";
        r12 = 2;
        r6[r12] = r9;	 Catch:{ SQLiteException -> 0x0254, all -> 0x0250 }
        r9 = "data";
        r6[r14] = r9;	 Catch:{ SQLiteException -> 0x0254, all -> 0x0250 }
        r17 = 0;
        r18 = 0;
        r19 = "rowid";
        r20 = 0;
        r12 = r24;
        r9 = r14;
        r14 = r6;
        r6 = r15;
        r15 = r5;
        r5 = r12.query(r13, r14, r15, r16, r17, r18, r19, r20);	 Catch:{ SQLiteException -> 0x024e }
        r6 = r5.moveToFirst();	 Catch:{ SQLiteException -> 0x0230, all -> 0x022b }
        if (r6 != 0) goto L_0x01de;
    L_0x01c6:
        r6 = r4.zzgg();	 Catch:{ SQLiteException -> 0x0230, all -> 0x022b }
        r6 = r6.zzin();	 Catch:{ SQLiteException -> 0x0230, all -> 0x022b }
        r9 = "Raw event data disappeared while in transaction. appId";
        r12 = com.google.android.gms.internal.measurement.zzfg.zzbh(r3);	 Catch:{ SQLiteException -> 0x0230, all -> 0x022b }
        r6.zzg(r9, r12);	 Catch:{ SQLiteException -> 0x0230, all -> 0x022b }
        if (r5 == 0) goto L_0x0287;
    L_0x01d9:
        r5.close();	 Catch:{ all -> 0x0b35 }
        goto L_0x0287;
    L_0x01de:
        r12 = r5.getLong(r10);	 Catch:{ SQLiteException -> 0x0230, all -> 0x022b }
        r6 = r5.getBlob(r9);	 Catch:{ SQLiteException -> 0x0230, all -> 0x022b }
        r14 = r6.length;	 Catch:{ SQLiteException -> 0x0230, all -> 0x022b }
        r6 = com.google.android.gms.internal.measurement.zzaba.zza(r6, r10, r14);	 Catch:{ SQLiteException -> 0x0230, all -> 0x022b }
        r14 = new com.google.android.gms.internal.measurement.zzki;	 Catch:{ SQLiteException -> 0x0230, all -> 0x022b }
        r14.<init>();	 Catch:{ SQLiteException -> 0x0230, all -> 0x022b }
        r14.zzb(r6);	 Catch:{ IOException -> 0x020d }
        r6 = r5.getString(r11);	 Catch:{ SQLiteException -> 0x0230, all -> 0x022b }
        r14.name = r6;	 Catch:{ SQLiteException -> 0x0230, all -> 0x022b }
        r6 = 2;
        r7 = r5.getLong(r6);	 Catch:{ SQLiteException -> 0x0230, all -> 0x022b }
        r6 = java.lang.Long.valueOf(r7);	 Catch:{ SQLiteException -> 0x0230, all -> 0x022b }
        r14.zzasw = r6;	 Catch:{ SQLiteException -> 0x0230, all -> 0x022b }
        r6 = r2.zza(r12, r14);	 Catch:{ SQLiteException -> 0x0230, all -> 0x022b }
        if (r6 != 0) goto L_0x021f;
    L_0x020a:
        if (r5 == 0) goto L_0x0287;
    L_0x020c:
        goto L_0x01d9;
    L_0x020d:
        r0 = move-exception;
        r6 = r4.zzgg();	 Catch:{ SQLiteException -> 0x0230, all -> 0x022b }
        r6 = r6.zzil();	 Catch:{ SQLiteException -> 0x0230, all -> 0x022b }
        r7 = "Data loss. Failed to merge raw event. appId";
        r8 = com.google.android.gms.internal.measurement.zzfg.zzbh(r3);	 Catch:{ SQLiteException -> 0x0230, all -> 0x022b }
        r6.zze(r7, r8, r0);	 Catch:{ SQLiteException -> 0x0230, all -> 0x022b }
    L_0x021f:
        r6 = r5.moveToNext();	 Catch:{ SQLiteException -> 0x0230, all -> 0x022b }
        if (r6 != 0) goto L_0x0228;
    L_0x0225:
        if (r5 == 0) goto L_0x0287;
    L_0x0227:
        goto L_0x01d9;
    L_0x0228:
        r7 = -1;
        goto L_0x01de;
    L_0x022b:
        r0 = move-exception;
        r2 = r0;
        r6 = r5;
        goto L_0x0b2f;
    L_0x0230:
        r0 = move-exception;
        r12 = r3;
        r6 = r5;
        goto L_0x0048;
    L_0x0235:
        r0 = move-exception;
        r6 = r15;
        r5 = r4.zzgg();	 Catch:{ SQLiteException -> 0x024e }
        r5 = r5.zzil();	 Catch:{ SQLiteException -> 0x024e }
        r7 = "Data loss. Failed to merge raw event metadata. appId";
        r8 = com.google.android.gms.internal.measurement.zzfg.zzbh(r3);	 Catch:{ SQLiteException -> 0x024e }
        r5.zze(r7, r8, r0);	 Catch:{ SQLiteException -> 0x024e }
        if (r6 == 0) goto L_0x0287;
    L_0x024a:
        r6.close();	 Catch:{ all -> 0x0b35 }
        goto L_0x0287;
    L_0x024e:
        r0 = move-exception;
        goto L_0x0256;
    L_0x0250:
        r0 = move-exception;
        r6 = r15;
        goto L_0x0b2e;
    L_0x0254:
        r0 = move-exception;
        r6 = r15;
    L_0x0256:
        r12 = r3;
        goto L_0x0048;
    L_0x0259:
        r0 = move-exception;
        r2 = r0;
        r6 = r22;
        goto L_0x0b2f;
    L_0x025f:
        r0 = move-exception;
        r12 = r3;
        r6 = r22;
        goto L_0x0048;
    L_0x0265:
        r0 = move-exception;
        r6 = r3;
        r12 = 0;
        goto L_0x0048;
    L_0x026a:
        r0 = move-exception;
        r2 = r0;
        r6 = 0;
        goto L_0x0b2f;
    L_0x026f:
        r0 = move-exception;
        r3 = r0;
        r6 = 0;
        r12 = 0;
    L_0x0273:
        r4 = r4.zzgg();	 Catch:{ all -> 0x0b2d }
        r4 = r4.zzil();	 Catch:{ all -> 0x0b2d }
        r5 = "Data loss. Error selecting raw event. appId";
        r7 = com.google.android.gms.internal.measurement.zzfg.zzbh(r12);	 Catch:{ all -> 0x0b2d }
        r4.zze(r5, r7, r3);	 Catch:{ all -> 0x0b2d }
        if (r6 == 0) goto L_0x0287;
    L_0x0286:
        goto L_0x024a;
    L_0x0287:
        r3 = r2.zzanu;	 Catch:{ all -> 0x0b35 }
        if (r3 == 0) goto L_0x0296;
    L_0x028b:
        r3 = r2.zzanu;	 Catch:{ all -> 0x0b35 }
        r3 = r3.isEmpty();	 Catch:{ all -> 0x0b35 }
        if (r3 == 0) goto L_0x0294;
    L_0x0293:
        goto L_0x0296;
    L_0x0294:
        r3 = r10;
        goto L_0x0297;
    L_0x0296:
        r3 = r11;
    L_0x0297:
        if (r3 != 0) goto L_0x0b1d;
    L_0x0299:
        r3 = r2.zzans;	 Catch:{ all -> 0x0b35 }
        r4 = r2.zzanu;	 Catch:{ all -> 0x0b35 }
        r4 = r4.size();	 Catch:{ all -> 0x0b35 }
        r4 = new com.google.android.gms.internal.measurement.zzki[r4];	 Catch:{ all -> 0x0b35 }
        r3.zzatd = r4;	 Catch:{ all -> 0x0b35 }
        r4 = r1.zzamd;	 Catch:{ all -> 0x0b35 }
        r5 = r3.zztd;	 Catch:{ all -> 0x0b35 }
        r4 = r4.zzau(r5);	 Catch:{ all -> 0x0b35 }
        r7 = r10;
        r8 = r7;
        r9 = r8;
        r12 = 0;
    L_0x02b2:
        r14 = r2.zzanu;	 Catch:{ all -> 0x0b35 }
        r14 = r14.size();	 Catch:{ all -> 0x0b35 }
        if (r7 >= r14) goto L_0x05a6;
    L_0x02ba:
        r14 = r2.zzanu;	 Catch:{ all -> 0x0b35 }
        r14 = r14.get(r7);	 Catch:{ all -> 0x0b35 }
        r14 = (com.google.android.gms.internal.measurement.zzki) r14;	 Catch:{ all -> 0x0b35 }
        r15 = r59.zzgd();	 Catch:{ all -> 0x0b35 }
        r11 = r2.zzans;	 Catch:{ all -> 0x0b35 }
        r11 = r11.zztd;	 Catch:{ all -> 0x0b35 }
        r5 = r14.name;	 Catch:{ all -> 0x0b35 }
        r5 = r15.zzn(r11, r5);	 Catch:{ all -> 0x0b35 }
        if (r5 == 0) goto L_0x0338;
    L_0x02d2:
        r5 = r59.zzgg();	 Catch:{ all -> 0x0b35 }
        r5 = r5.zzin();	 Catch:{ all -> 0x0b35 }
        r6 = "Dropping blacklisted raw event. appId";
        r11 = r2.zzans;	 Catch:{ all -> 0x0b35 }
        r11 = r11.zztd;	 Catch:{ all -> 0x0b35 }
        r11 = com.google.android.gms.internal.measurement.zzfg.zzbh(r11);	 Catch:{ all -> 0x0b35 }
        r15 = r59.zzgb();	 Catch:{ all -> 0x0b35 }
        r10 = r14.name;	 Catch:{ all -> 0x0b35 }
        r10 = r15.zzbe(r10);	 Catch:{ all -> 0x0b35 }
        r5.zze(r6, r11, r10);	 Catch:{ all -> 0x0b35 }
        r5 = r59.zzgc();	 Catch:{ all -> 0x0b35 }
        r6 = r2.zzans;	 Catch:{ all -> 0x0b35 }
        r6 = r6.zztd;	 Catch:{ all -> 0x0b35 }
        r5 = r5.zzce(r6);	 Catch:{ all -> 0x0b35 }
        if (r5 != 0) goto L_0x0310;
    L_0x02ff:
        r5 = r59.zzgc();	 Catch:{ all -> 0x0b35 }
        r6 = r2.zzans;	 Catch:{ all -> 0x0b35 }
        r6 = r6.zztd;	 Catch:{ all -> 0x0b35 }
        r5 = r5.zzcf(r6);	 Catch:{ all -> 0x0b35 }
        if (r5 == 0) goto L_0x030e;
    L_0x030d:
        goto L_0x0310;
    L_0x030e:
        r5 = 0;
        goto L_0x0311;
    L_0x0310:
        r5 = 1;
    L_0x0311:
        if (r5 != 0) goto L_0x0334;
    L_0x0313:
        r5 = "_err";
        r6 = r14.name;	 Catch:{ all -> 0x0b35 }
        r5 = r5.equals(r6);	 Catch:{ all -> 0x0b35 }
        if (r5 != 0) goto L_0x0334;
    L_0x031d:
        r15 = r59.zzgc();	 Catch:{ all -> 0x0b35 }
        r5 = r2.zzans;	 Catch:{ all -> 0x0b35 }
        r5 = r5.zztd;	 Catch:{ all -> 0x0b35 }
        r17 = 11;
        r18 = "_ev";
        r6 = r14.name;	 Catch:{ all -> 0x0b35 }
        r20 = 0;
        r16 = r5;
        r19 = r6;
        r15.zza(r16, r17, r18, r19, r20);	 Catch:{ all -> 0x0b35 }
    L_0x0334:
        r28 = r7;
        goto L_0x05a0;
    L_0x0338:
        r5 = r59.zzgd();	 Catch:{ all -> 0x0b35 }
        r6 = r2.zzans;	 Catch:{ all -> 0x0b35 }
        r6 = r6.zztd;	 Catch:{ all -> 0x0b35 }
        r10 = r14.name;	 Catch:{ all -> 0x0b35 }
        r5 = r5.zzo(r6, r10);	 Catch:{ all -> 0x0b35 }
        if (r5 != 0) goto L_0x0358;
    L_0x0348:
        r59.zzgc();	 Catch:{ all -> 0x0b35 }
        r6 = r14.name;	 Catch:{ all -> 0x0b35 }
        r6 = com.google.android.gms.internal.measurement.zzjv.zzcg(r6);	 Catch:{ all -> 0x0b35 }
        if (r6 == 0) goto L_0x0354;
    L_0x0353:
        goto L_0x0358;
    L_0x0354:
        r28 = r7;
        goto L_0x0544;
    L_0x0358:
        r6 = r14.zzasv;	 Catch:{ all -> 0x0b35 }
        if (r6 != 0) goto L_0x0361;
    L_0x035c:
        r6 = 0;
        r10 = new com.google.android.gms.internal.measurement.zzkj[r6];	 Catch:{ all -> 0x0b35 }
        r14.zzasv = r10;	 Catch:{ all -> 0x0b35 }
    L_0x0361:
        r6 = r14.zzasv;	 Catch:{ all -> 0x0b35 }
        r10 = r6.length;	 Catch:{ all -> 0x0b35 }
        r11 = 0;
        r15 = 0;
        r16 = 0;
    L_0x0368:
        if (r11 >= r10) goto L_0x03a9;
    L_0x036a:
        r25 = r10;
        r10 = r6[r11];	 Catch:{ all -> 0x0b35 }
        r26 = r6;
        r6 = "_c";
        r27 = r8;
        r8 = r10.name;	 Catch:{ all -> 0x0b35 }
        r6 = r6.equals(r8);	 Catch:{ all -> 0x0b35 }
        if (r6 == 0) goto L_0x0388;
    L_0x037c:
        r28 = r7;
        r6 = 1;
        r8 = java.lang.Long.valueOf(r6);	 Catch:{ all -> 0x0b35 }
        r10.zzasz = r8;	 Catch:{ all -> 0x0b35 }
        r15 = 1;
        goto L_0x039e;
    L_0x0388:
        r28 = r7;
        r6 = "_r";
        r7 = r10.name;	 Catch:{ all -> 0x0b35 }
        r6 = r6.equals(r7);	 Catch:{ all -> 0x0b35 }
        if (r6 == 0) goto L_0x039e;
    L_0x0394:
        r6 = 1;
        r8 = java.lang.Long.valueOf(r6);	 Catch:{ all -> 0x0b35 }
        r10.zzasz = r8;	 Catch:{ all -> 0x0b35 }
        r16 = 1;
    L_0x039e:
        r11 = r11 + 1;
        r10 = r25;
        r6 = r26;
        r8 = r27;
        r7 = r28;
        goto L_0x0368;
    L_0x03a9:
        r28 = r7;
        r27 = r8;
        if (r15 != 0) goto L_0x03ed;
    L_0x03af:
        if (r5 == 0) goto L_0x03ed;
    L_0x03b1:
        r6 = r59.zzgg();	 Catch:{ all -> 0x0b35 }
        r6 = r6.zzir();	 Catch:{ all -> 0x0b35 }
        r7 = "Marking event as conversion";
        r8 = r59.zzgb();	 Catch:{ all -> 0x0b35 }
        r10 = r14.name;	 Catch:{ all -> 0x0b35 }
        r8 = r8.zzbe(r10);	 Catch:{ all -> 0x0b35 }
        r6.zzg(r7, r8);	 Catch:{ all -> 0x0b35 }
        r6 = r14.zzasv;	 Catch:{ all -> 0x0b35 }
        r7 = r14.zzasv;	 Catch:{ all -> 0x0b35 }
        r7 = r7.length;	 Catch:{ all -> 0x0b35 }
        r8 = 1;
        r7 = r7 + r8;
        r6 = java.util.Arrays.copyOf(r6, r7);	 Catch:{ all -> 0x0b35 }
        r6 = (com.google.android.gms.internal.measurement.zzkj[]) r6;	 Catch:{ all -> 0x0b35 }
        r7 = new com.google.android.gms.internal.measurement.zzkj;	 Catch:{ all -> 0x0b35 }
        r7.<init>();	 Catch:{ all -> 0x0b35 }
        r8 = "_c";
        r7.name = r8;	 Catch:{ all -> 0x0b35 }
        r10 = 1;
        r8 = java.lang.Long.valueOf(r10);	 Catch:{ all -> 0x0b35 }
        r7.zzasz = r8;	 Catch:{ all -> 0x0b35 }
        r8 = r6.length;	 Catch:{ all -> 0x0b35 }
        r10 = 1;
        r8 = r8 - r10;
        r6[r8] = r7;	 Catch:{ all -> 0x0b35 }
        r14.zzasv = r6;	 Catch:{ all -> 0x0b35 }
    L_0x03ed:
        if (r16 != 0) goto L_0x042b;
    L_0x03ef:
        r6 = r59.zzgg();	 Catch:{ all -> 0x0b35 }
        r6 = r6.zzir();	 Catch:{ all -> 0x0b35 }
        r7 = "Marking event as real-time";
        r8 = r59.zzgb();	 Catch:{ all -> 0x0b35 }
        r10 = r14.name;	 Catch:{ all -> 0x0b35 }
        r8 = r8.zzbe(r10);	 Catch:{ all -> 0x0b35 }
        r6.zzg(r7, r8);	 Catch:{ all -> 0x0b35 }
        r6 = r14.zzasv;	 Catch:{ all -> 0x0b35 }
        r7 = r14.zzasv;	 Catch:{ all -> 0x0b35 }
        r7 = r7.length;	 Catch:{ all -> 0x0b35 }
        r8 = 1;
        r7 = r7 + r8;
        r6 = java.util.Arrays.copyOf(r6, r7);	 Catch:{ all -> 0x0b35 }
        r6 = (com.google.android.gms.internal.measurement.zzkj[]) r6;	 Catch:{ all -> 0x0b35 }
        r7 = new com.google.android.gms.internal.measurement.zzkj;	 Catch:{ all -> 0x0b35 }
        r7.<init>();	 Catch:{ all -> 0x0b35 }
        r8 = "_r";
        r7.name = r8;	 Catch:{ all -> 0x0b35 }
        r10 = 1;
        r8 = java.lang.Long.valueOf(r10);	 Catch:{ all -> 0x0b35 }
        r7.zzasz = r8;	 Catch:{ all -> 0x0b35 }
        r8 = r6.length;	 Catch:{ all -> 0x0b35 }
        r10 = 1;
        r8 = r8 - r10;
        r6[r8] = r7;	 Catch:{ all -> 0x0b35 }
        r14.zzasv = r6;	 Catch:{ all -> 0x0b35 }
    L_0x042b:
        r29 = r59.zzga();	 Catch:{ all -> 0x0b35 }
        r30 = r59.zzjv();	 Catch:{ all -> 0x0b35 }
        r6 = r2.zzans;	 Catch:{ all -> 0x0b35 }
        r6 = r6.zztd;	 Catch:{ all -> 0x0b35 }
        r33 = 0;
        r34 = 0;
        r35 = 0;
        r36 = 0;
        r37 = 1;
        r32 = r6;
        r6 = r29.zza(r30, r32, r33, r34, r35, r36, r37);	 Catch:{ all -> 0x0b35 }
        r6 = r6.zzafg;	 Catch:{ all -> 0x0b35 }
        r8 = r1.zzamd;	 Catch:{ all -> 0x0b35 }
        r10 = r2.zzans;	 Catch:{ all -> 0x0b35 }
        r10 = r10.zztd;	 Catch:{ all -> 0x0b35 }
        r8 = r8.zzar(r10);	 Catch:{ all -> 0x0b35 }
        r10 = (long) r8;	 Catch:{ all -> 0x0b35 }
        r8 = (r6 > r10 ? 1 : (r6 == r10 ? 0 : -1));
        if (r8 <= 0) goto L_0x0490;
    L_0x0458:
        r6 = 0;
    L_0x0459:
        r7 = r14.zzasv;	 Catch:{ all -> 0x0b35 }
        r7 = r7.length;	 Catch:{ all -> 0x0b35 }
        if (r6 >= r7) goto L_0x048d;
    L_0x045e:
        r7 = "_r";
        r8 = r14.zzasv;	 Catch:{ all -> 0x0b35 }
        r8 = r8[r6];	 Catch:{ all -> 0x0b35 }
        r8 = r8.name;	 Catch:{ all -> 0x0b35 }
        r7 = r7.equals(r8);	 Catch:{ all -> 0x0b35 }
        if (r7 == 0) goto L_0x048a;
    L_0x046c:
        r7 = r14.zzasv;	 Catch:{ all -> 0x0b35 }
        r7 = r7.length;	 Catch:{ all -> 0x0b35 }
        r8 = 1;
        r7 = r7 - r8;
        r7 = new com.google.android.gms.internal.measurement.zzkj[r7];	 Catch:{ all -> 0x0b35 }
        if (r6 <= 0) goto L_0x047b;
    L_0x0475:
        r8 = r14.zzasv;	 Catch:{ all -> 0x0b35 }
        r10 = 0;
        java.lang.System.arraycopy(r8, r10, r7, r10, r6);	 Catch:{ all -> 0x0b35 }
    L_0x047b:
        r8 = r7.length;	 Catch:{ all -> 0x0b35 }
        if (r6 >= r8) goto L_0x0487;
    L_0x047e:
        r8 = r14.zzasv;	 Catch:{ all -> 0x0b35 }
        r10 = r6 + 1;
        r11 = r7.length;	 Catch:{ all -> 0x0b35 }
        r11 = r11 - r6;
        java.lang.System.arraycopy(r8, r10, r7, r6, r11);	 Catch:{ all -> 0x0b35 }
    L_0x0487:
        r14.zzasv = r7;	 Catch:{ all -> 0x0b35 }
        goto L_0x048d;
    L_0x048a:
        r6 = r6 + 1;
        goto L_0x0459;
    L_0x048d:
        r8 = r27;
        goto L_0x0491;
    L_0x0490:
        r8 = 1;
    L_0x0491:
        r6 = r14.name;	 Catch:{ all -> 0x0b35 }
        r6 = com.google.android.gms.internal.measurement.zzjv.zzbv(r6);	 Catch:{ all -> 0x0b35 }
        if (r6 == 0) goto L_0x0544;
    L_0x0499:
        if (r5 == 0) goto L_0x0544;
    L_0x049b:
        r29 = r59.zzga();	 Catch:{ all -> 0x0b35 }
        r30 = r59.zzjv();	 Catch:{ all -> 0x0b35 }
        r5 = r2.zzans;	 Catch:{ all -> 0x0b35 }
        r5 = r5.zztd;	 Catch:{ all -> 0x0b35 }
        r33 = 0;
        r34 = 0;
        r35 = 1;
        r36 = 0;
        r37 = 0;
        r32 = r5;
        r5 = r29.zza(r30, r32, r33, r34, r35, r36, r37);	 Catch:{ all -> 0x0b35 }
        r5 = r5.zzafe;	 Catch:{ all -> 0x0b35 }
        r7 = r1.zzamd;	 Catch:{ all -> 0x0b35 }
        r10 = r2.zzans;	 Catch:{ all -> 0x0b35 }
        r10 = r10.zztd;	 Catch:{ all -> 0x0b35 }
        r11 = com.google.android.gms.internal.measurement.zzew.zzagy;	 Catch:{ all -> 0x0b35 }
        r7 = r7.zzb(r10, r11);	 Catch:{ all -> 0x0b35 }
        r10 = (long) r7;	 Catch:{ all -> 0x0b35 }
        r7 = (r5 > r10 ? 1 : (r5 == r10 ? 0 : -1));
        if (r7 <= 0) goto L_0x0544;
    L_0x04ca:
        r5 = r59.zzgg();	 Catch:{ all -> 0x0b35 }
        r5 = r5.zzin();	 Catch:{ all -> 0x0b35 }
        r6 = "Too many conversions. Not logging as conversion. appId";
        r7 = r2.zzans;	 Catch:{ all -> 0x0b35 }
        r7 = r7.zztd;	 Catch:{ all -> 0x0b35 }
        r7 = com.google.android.gms.internal.measurement.zzfg.zzbh(r7);	 Catch:{ all -> 0x0b35 }
        r5.zzg(r6, r7);	 Catch:{ all -> 0x0b35 }
        r5 = r14.zzasv;	 Catch:{ all -> 0x0b35 }
        r6 = r5.length;	 Catch:{ all -> 0x0b35 }
        r7 = 0;
        r10 = 0;
        r11 = 0;
    L_0x04e5:
        if (r7 >= r6) goto L_0x050b;
    L_0x04e7:
        r15 = r5[r7];	 Catch:{ all -> 0x0b35 }
        r38 = r5;
        r5 = "_c";
        r39 = r6;
        r6 = r15.name;	 Catch:{ all -> 0x0b35 }
        r5 = r5.equals(r6);	 Catch:{ all -> 0x0b35 }
        if (r5 == 0) goto L_0x04f9;
    L_0x04f7:
        r11 = r15;
        goto L_0x0504;
    L_0x04f9:
        r5 = "_err";
        r6 = r15.name;	 Catch:{ all -> 0x0b35 }
        r5 = r5.equals(r6);	 Catch:{ all -> 0x0b35 }
        if (r5 == 0) goto L_0x0504;
    L_0x0503:
        r10 = 1;
    L_0x0504:
        r7 = r7 + 1;
        r5 = r38;
        r6 = r39;
        goto L_0x04e5;
    L_0x050b:
        if (r10 == 0) goto L_0x0520;
    L_0x050d:
        if (r11 == 0) goto L_0x0520;
    L_0x050f:
        r5 = r14.zzasv;	 Catch:{ all -> 0x0b35 }
        r6 = 1;
        r7 = new com.google.android.gms.internal.measurement.zzkj[r6];	 Catch:{ all -> 0x0b35 }
        r6 = 0;
        r7[r6] = r11;	 Catch:{ all -> 0x0b35 }
        r5 = com.google.android.gms.common.util.ArrayUtils.removeAll(r5, r7);	 Catch:{ all -> 0x0b35 }
        r5 = (com.google.android.gms.internal.measurement.zzkj[]) r5;	 Catch:{ all -> 0x0b35 }
        r14.zzasv = r5;	 Catch:{ all -> 0x0b35 }
        goto L_0x0544;
    L_0x0520:
        if (r11 == 0) goto L_0x052f;
    L_0x0522:
        r5 = "_err";
        r11.name = r5;	 Catch:{ all -> 0x0b35 }
        r5 = 10;
        r5 = java.lang.Long.valueOf(r5);	 Catch:{ all -> 0x0b35 }
        r11.zzasz = r5;	 Catch:{ all -> 0x0b35 }
        goto L_0x0544;
    L_0x052f:
        r5 = r59.zzgg();	 Catch:{ all -> 0x0b35 }
        r5 = r5.zzil();	 Catch:{ all -> 0x0b35 }
        r6 = "Did not find conversion parameter. appId";
        r7 = r2.zzans;	 Catch:{ all -> 0x0b35 }
        r7 = r7.zztd;	 Catch:{ all -> 0x0b35 }
        r7 = com.google.android.gms.internal.measurement.zzfg.zzbh(r7);	 Catch:{ all -> 0x0b35 }
        r5.zzg(r6, r7);	 Catch:{ all -> 0x0b35 }
    L_0x0544:
        if (r4 == 0) goto L_0x0597;
    L_0x0546:
        r5 = "_e";
        r6 = r14.name;	 Catch:{ all -> 0x0b35 }
        r5 = r5.equals(r6);	 Catch:{ all -> 0x0b35 }
        if (r5 == 0) goto L_0x0597;
    L_0x0550:
        r5 = r14.zzasv;	 Catch:{ all -> 0x0b35 }
        if (r5 == 0) goto L_0x0584;
    L_0x0554:
        r5 = r14.zzasv;	 Catch:{ all -> 0x0b35 }
        r5 = r5.length;	 Catch:{ all -> 0x0b35 }
        if (r5 != 0) goto L_0x055a;
    L_0x0559:
        goto L_0x0584;
    L_0x055a:
        r59.zzgc();	 Catch:{ all -> 0x0b35 }
        r5 = "_et";
        r5 = com.google.android.gms.internal.measurement.zzjv.zzb(r14, r5);	 Catch:{ all -> 0x0b35 }
        r5 = (java.lang.Long) r5;	 Catch:{ all -> 0x0b35 }
        if (r5 != 0) goto L_0x057d;
    L_0x0567:
        r5 = r59.zzgg();	 Catch:{ all -> 0x0b35 }
        r5 = r5.zzin();	 Catch:{ all -> 0x0b35 }
        r6 = "Engagement event does not include duration. appId";
        r7 = r2.zzans;	 Catch:{ all -> 0x0b35 }
        r7 = r7.zztd;	 Catch:{ all -> 0x0b35 }
        r7 = com.google.android.gms.internal.measurement.zzfg.zzbh(r7);	 Catch:{ all -> 0x0b35 }
    L_0x0579:
        r5.zzg(r6, r7);	 Catch:{ all -> 0x0b35 }
        goto L_0x0597;
    L_0x057d:
        r5 = r5.longValue();	 Catch:{ all -> 0x0b35 }
        r10 = r12 + r5;
        goto L_0x0598;
    L_0x0584:
        r5 = r59.zzgg();	 Catch:{ all -> 0x0b35 }
        r5 = r5.zzin();	 Catch:{ all -> 0x0b35 }
        r6 = "Engagement event does not contain any parameters. appId";
        r7 = r2.zzans;	 Catch:{ all -> 0x0b35 }
        r7 = r7.zztd;	 Catch:{ all -> 0x0b35 }
        r7 = com.google.android.gms.internal.measurement.zzfg.zzbh(r7);	 Catch:{ all -> 0x0b35 }
        goto L_0x0579;
    L_0x0597:
        r10 = r12;
    L_0x0598:
        r5 = r3.zzatd;	 Catch:{ all -> 0x0b35 }
        r6 = r9 + 1;
        r5[r9] = r14;	 Catch:{ all -> 0x0b35 }
        r9 = r6;
        r12 = r10;
    L_0x05a0:
        r7 = r28 + 1;
        r10 = 0;
        r11 = 1;
        goto L_0x02b2;
    L_0x05a6:
        r27 = r8;
        r5 = r2.zzanu;	 Catch:{ all -> 0x0b35 }
        r5 = r5.size();	 Catch:{ all -> 0x0b35 }
        if (r9 >= r5) goto L_0x05ba;
    L_0x05b0:
        r5 = r3.zzatd;	 Catch:{ all -> 0x0b35 }
        r5 = java.util.Arrays.copyOf(r5, r9);	 Catch:{ all -> 0x0b35 }
        r5 = (com.google.android.gms.internal.measurement.zzki[]) r5;	 Catch:{ all -> 0x0b35 }
        r3.zzatd = r5;	 Catch:{ all -> 0x0b35 }
    L_0x05ba:
        if (r4 == 0) goto L_0x067b;
    L_0x05bc:
        r4 = r59.zzga();	 Catch:{ all -> 0x0b35 }
        r5 = r3.zztd;	 Catch:{ all -> 0x0b35 }
        r6 = "_lte";
        r4 = r4.zzg(r5, r6);	 Catch:{ all -> 0x0b35 }
        if (r4 == 0) goto L_0x05f1;
    L_0x05ca:
        r5 = r4.value;	 Catch:{ all -> 0x0b35 }
        if (r5 != 0) goto L_0x05cf;
    L_0x05ce:
        goto L_0x05f1;
    L_0x05cf:
        r5 = new com.google.android.gms.internal.measurement.zzju;	 Catch:{ all -> 0x0b35 }
        r15 = r3.zztd;	 Catch:{ all -> 0x0b35 }
        r16 = "auto";
        r17 = "_lte";
        r6 = r1.zzrj;	 Catch:{ all -> 0x0b35 }
        r18 = r6.currentTimeMillis();	 Catch:{ all -> 0x0b35 }
        r4 = r4.value;	 Catch:{ all -> 0x0b35 }
        r4 = (java.lang.Long) r4;	 Catch:{ all -> 0x0b35 }
        r6 = r4.longValue();	 Catch:{ all -> 0x0b35 }
        r8 = r6 + r12;
        r20 = java.lang.Long.valueOf(r8);	 Catch:{ all -> 0x0b35 }
        r14 = r5;
        r14.<init>(r15, r16, r17, r18, r20);	 Catch:{ all -> 0x0b35 }
        r4 = r5;
        goto L_0x060a;
    L_0x05f1:
        r4 = new com.google.android.gms.internal.measurement.zzju;	 Catch:{ all -> 0x0b35 }
        r5 = r3.zztd;	 Catch:{ all -> 0x0b35 }
        r30 = "auto";
        r31 = "_lte";
        r6 = r1.zzrj;	 Catch:{ all -> 0x0b35 }
        r32 = r6.currentTimeMillis();	 Catch:{ all -> 0x0b35 }
        r34 = java.lang.Long.valueOf(r12);	 Catch:{ all -> 0x0b35 }
        r28 = r4;
        r29 = r5;
        r28.<init>(r29, r30, r31, r32, r34);	 Catch:{ all -> 0x0b35 }
    L_0x060a:
        r5 = new com.google.android.gms.internal.measurement.zzkn;	 Catch:{ all -> 0x0b35 }
        r5.<init>();	 Catch:{ all -> 0x0b35 }
        r6 = "_lte";
        r5.name = r6;	 Catch:{ all -> 0x0b35 }
        r6 = r1.zzrj;	 Catch:{ all -> 0x0b35 }
        r6 = r6.currentTimeMillis();	 Catch:{ all -> 0x0b35 }
        r6 = java.lang.Long.valueOf(r6);	 Catch:{ all -> 0x0b35 }
        r5.zzaui = r6;	 Catch:{ all -> 0x0b35 }
        r6 = r4.value;	 Catch:{ all -> 0x0b35 }
        r6 = (java.lang.Long) r6;	 Catch:{ all -> 0x0b35 }
        r5.zzasz = r6;	 Catch:{ all -> 0x0b35 }
        r6 = 0;
    L_0x0626:
        r7 = r3.zzate;	 Catch:{ all -> 0x0b35 }
        r7 = r7.length;	 Catch:{ all -> 0x0b35 }
        if (r6 >= r7) goto L_0x0642;
    L_0x062b:
        r7 = "_lte";
        r8 = r3.zzate;	 Catch:{ all -> 0x0b35 }
        r8 = r8[r6];	 Catch:{ all -> 0x0b35 }
        r8 = r8.name;	 Catch:{ all -> 0x0b35 }
        r7 = r7.equals(r8);	 Catch:{ all -> 0x0b35 }
        if (r7 == 0) goto L_0x063f;
    L_0x0639:
        r7 = r3.zzate;	 Catch:{ all -> 0x0b35 }
        r7[r6] = r5;	 Catch:{ all -> 0x0b35 }
        r6 = 1;
        goto L_0x0643;
    L_0x063f:
        r6 = r6 + 1;
        goto L_0x0626;
    L_0x0642:
        r6 = 0;
    L_0x0643:
        if (r6 != 0) goto L_0x065f;
    L_0x0645:
        r6 = r3.zzate;	 Catch:{ all -> 0x0b35 }
        r7 = r3.zzate;	 Catch:{ all -> 0x0b35 }
        r7 = r7.length;	 Catch:{ all -> 0x0b35 }
        r8 = 1;
        r7 = r7 + r8;
        r6 = java.util.Arrays.copyOf(r6, r7);	 Catch:{ all -> 0x0b35 }
        r6 = (com.google.android.gms.internal.measurement.zzkn[]) r6;	 Catch:{ all -> 0x0b35 }
        r3.zzate = r6;	 Catch:{ all -> 0x0b35 }
        r6 = r3.zzate;	 Catch:{ all -> 0x0b35 }
        r7 = r2.zzans;	 Catch:{ all -> 0x0b35 }
        r7 = r7.zzate;	 Catch:{ all -> 0x0b35 }
        r7 = r7.length;	 Catch:{ all -> 0x0b35 }
        r8 = 1;
        r7 = r7 - r8;
        r6[r7] = r5;	 Catch:{ all -> 0x0b35 }
    L_0x065f:
        r5 = 0;
        r7 = (r12 > r5 ? 1 : (r12 == r5 ? 0 : -1));
        if (r7 <= 0) goto L_0x067b;
    L_0x0665:
        r5 = r59.zzga();	 Catch:{ all -> 0x0b35 }
        r5.zza(r4);	 Catch:{ all -> 0x0b35 }
        r5 = r59.zzgg();	 Catch:{ all -> 0x0b35 }
        r5 = r5.zziq();	 Catch:{ all -> 0x0b35 }
        r6 = "Updated lifetime engagement user property with value. Value";
        r4 = r4.value;	 Catch:{ all -> 0x0b35 }
        r5.zzg(r6, r4);	 Catch:{ all -> 0x0b35 }
    L_0x067b:
        r4 = r3.zztd;	 Catch:{ all -> 0x0b35 }
        r5 = r3.zzate;	 Catch:{ all -> 0x0b35 }
        r6 = r3.zzatd;	 Catch:{ all -> 0x0b35 }
        r4 = r1.zza(r4, r5, r6);	 Catch:{ all -> 0x0b35 }
        r3.zzatv = r4;	 Catch:{ all -> 0x0b35 }
        r4 = com.google.android.gms.internal.measurement.zzew.zzagk;	 Catch:{ all -> 0x0b35 }
        r4 = r4.get();	 Catch:{ all -> 0x0b35 }
        r4 = (java.lang.Boolean) r4;	 Catch:{ all -> 0x0b35 }
        r4 = r4.booleanValue();	 Catch:{ all -> 0x0b35 }
        if (r4 == 0) goto L_0x0963;
    L_0x0695:
        r4 = r1.zzamd;	 Catch:{ all -> 0x0b35 }
        r5 = r2.zzans;	 Catch:{ all -> 0x0b35 }
        r5 = r5.zztd;	 Catch:{ all -> 0x0b35 }
        r6 = "1";
        r4 = r4.zzgd();	 Catch:{ all -> 0x0b35 }
        r7 = "measurement.event_sampling_enabled";
        r4 = r4.zzm(r5, r7);	 Catch:{ all -> 0x0b35 }
        r4 = r6.equals(r4);	 Catch:{ all -> 0x0b35 }
        if (r4 == 0) goto L_0x0963;
    L_0x06ad:
        r4 = new java.util.HashMap;	 Catch:{ all -> 0x0b35 }
        r4.<init>();	 Catch:{ all -> 0x0b35 }
        r5 = r3.zzatd;	 Catch:{ all -> 0x0b35 }
        r5 = r5.length;	 Catch:{ all -> 0x0b35 }
        r5 = new com.google.android.gms.internal.measurement.zzki[r5];	 Catch:{ all -> 0x0b35 }
        r6 = r59.zzgc();	 Catch:{ all -> 0x0b35 }
        r6 = r6.zzku();	 Catch:{ all -> 0x0b35 }
        r7 = r3.zzatd;	 Catch:{ all -> 0x0b35 }
        r8 = r7.length;	 Catch:{ all -> 0x0b35 }
        r9 = 0;
        r10 = 0;
    L_0x06c4:
        if (r9 >= r8) goto L_0x0931;
    L_0x06c6:
        r11 = r7[r9];	 Catch:{ all -> 0x0b35 }
        r12 = r11.name;	 Catch:{ all -> 0x0b35 }
        r13 = "_ep";
        r12 = r12.equals(r13);	 Catch:{ all -> 0x0b35 }
        if (r12 == 0) goto L_0x074f;
    L_0x06d2:
        r59.zzgc();	 Catch:{ all -> 0x0b35 }
        r12 = "_en";
        r12 = com.google.android.gms.internal.measurement.zzjv.zzb(r11, r12);	 Catch:{ all -> 0x0b35 }
        r12 = (java.lang.String) r12;	 Catch:{ all -> 0x0b35 }
        r13 = r4.get(r12);	 Catch:{ all -> 0x0b35 }
        r13 = (com.google.android.gms.internal.measurement.zzeq) r13;	 Catch:{ all -> 0x0b35 }
        if (r13 != 0) goto L_0x06f4;
    L_0x06e5:
        r13 = r59.zzga();	 Catch:{ all -> 0x0b35 }
        r14 = r2.zzans;	 Catch:{ all -> 0x0b35 }
        r14 = r14.zztd;	 Catch:{ all -> 0x0b35 }
        r13 = r13.zze(r14, r12);	 Catch:{ all -> 0x0b35 }
        r4.put(r12, r13);	 Catch:{ all -> 0x0b35 }
    L_0x06f4:
        r12 = r13.zzaft;	 Catch:{ all -> 0x0b35 }
        if (r12 != 0) goto L_0x0746;
    L_0x06f8:
        r12 = r13.zzafu;	 Catch:{ all -> 0x0b35 }
        r14 = r12.longValue();	 Catch:{ all -> 0x0b35 }
        r16 = 1;
        r12 = (r14 > r16 ? 1 : (r14 == r16 ? 0 : -1));
        if (r12 <= 0) goto L_0x0713;
    L_0x0704:
        r59.zzgc();	 Catch:{ all -> 0x0b35 }
        r12 = r11.zzasv;	 Catch:{ all -> 0x0b35 }
        r14 = "_sr";
        r15 = r13.zzafu;	 Catch:{ all -> 0x0b35 }
        r12 = com.google.android.gms.internal.measurement.zzjv.zza(r12, r14, r15);	 Catch:{ all -> 0x0b35 }
        r11.zzasv = r12;	 Catch:{ all -> 0x0b35 }
    L_0x0713:
        r12 = r13.zzafv;	 Catch:{ all -> 0x0b35 }
        if (r12 == 0) goto L_0x0735;
    L_0x0717:
        r12 = r13.zzafv;	 Catch:{ all -> 0x0b35 }
        r12 = r12.booleanValue();	 Catch:{ all -> 0x0b35 }
        if (r12 == 0) goto L_0x0735;
    L_0x071f:
        r59.zzgc();	 Catch:{ all -> 0x0b35 }
        r12 = r11.zzasv;	 Catch:{ all -> 0x0b35 }
        r13 = "_efs";
        r40 = r7;
        r14 = 1;
        r7 = java.lang.Long.valueOf(r14);	 Catch:{ all -> 0x0b35 }
        r7 = com.google.android.gms.internal.measurement.zzjv.zza(r12, r13, r7);	 Catch:{ all -> 0x0b35 }
        r11.zzasv = r7;	 Catch:{ all -> 0x0b35 }
        goto L_0x0737;
    L_0x0735:
        r40 = r7;
    L_0x0737:
        r7 = r10 + 1;
        r5[r10] = r11;	 Catch:{ all -> 0x0b35 }
        r57 = r2;
        r58 = r3;
        r56 = r6;
        r10 = r7;
    L_0x0742:
        r41 = r8;
        goto L_0x07d8;
    L_0x0746:
        r40 = r7;
        r57 = r2;
        r58 = r3;
        r56 = r6;
        goto L_0x0742;
    L_0x074f:
        r40 = r7;
        r7 = "_dbg";
        r12 = 1;
        r14 = java.lang.Long.valueOf(r12);	 Catch:{ all -> 0x0b35 }
        r12 = android.text.TextUtils.isEmpty(r7);	 Catch:{ all -> 0x0b35 }
        if (r12 != 0) goto L_0x07a3;
    L_0x075f:
        if (r14 != 0) goto L_0x0762;
    L_0x0761:
        goto L_0x07a3;
    L_0x0762:
        r12 = r11.zzasv;	 Catch:{ all -> 0x0b35 }
        r13 = r12.length;	 Catch:{ all -> 0x0b35 }
        r15 = 0;
    L_0x0766:
        if (r15 >= r13) goto L_0x07a3;
    L_0x0768:
        r41 = r8;
        r8 = r12[r15];	 Catch:{ all -> 0x0b35 }
        r42 = r12;
        r12 = r8.name;	 Catch:{ all -> 0x0b35 }
        r12 = r7.equals(r12);	 Catch:{ all -> 0x0b35 }
        if (r12 == 0) goto L_0x079c;
    L_0x0776:
        r7 = r14 instanceof java.lang.Long;	 Catch:{ all -> 0x0b35 }
        if (r7 == 0) goto L_0x0782;
    L_0x077a:
        r7 = r8.zzasz;	 Catch:{ all -> 0x0b35 }
        r7 = r14.equals(r7);	 Catch:{ all -> 0x0b35 }
        if (r7 != 0) goto L_0x079a;
    L_0x0782:
        r7 = r14 instanceof java.lang.String;	 Catch:{ all -> 0x0b35 }
        if (r7 == 0) goto L_0x078e;
    L_0x0786:
        r7 = r8.zzajf;	 Catch:{ all -> 0x0b35 }
        r7 = r14.equals(r7);	 Catch:{ all -> 0x0b35 }
        if (r7 != 0) goto L_0x079a;
    L_0x078e:
        r7 = r14 instanceof java.lang.Double;	 Catch:{ all -> 0x0b35 }
        if (r7 == 0) goto L_0x07a5;
    L_0x0792:
        r7 = r8.zzaqx;	 Catch:{ all -> 0x0b35 }
        r7 = r14.equals(r7);	 Catch:{ all -> 0x0b35 }
        if (r7 == 0) goto L_0x07a5;
    L_0x079a:
        r7 = 1;
        goto L_0x07a6;
    L_0x079c:
        r15 = r15 + 1;
        r8 = r41;
        r12 = r42;
        goto L_0x0766;
    L_0x07a3:
        r41 = r8;
    L_0x07a5:
        r7 = 0;
    L_0x07a6:
        if (r7 != 0) goto L_0x07b7;
    L_0x07a8:
        r7 = r59.zzgd();	 Catch:{ all -> 0x0b35 }
        r8 = r2.zzans;	 Catch:{ all -> 0x0b35 }
        r8 = r8.zztd;	 Catch:{ all -> 0x0b35 }
        r12 = r11.name;	 Catch:{ all -> 0x0b35 }
        r7 = r7.zzp(r8, r12);	 Catch:{ all -> 0x0b35 }
        goto L_0x07b8;
    L_0x07b7:
        r7 = 1;
    L_0x07b8:
        if (r7 > 0) goto L_0x07dc;
    L_0x07ba:
        r8 = r59.zzgg();	 Catch:{ all -> 0x0b35 }
        r8 = r8.zzin();	 Catch:{ all -> 0x0b35 }
        r12 = "Sample rate must be positive. event, rate";
        r13 = r11.name;	 Catch:{ all -> 0x0b35 }
        r7 = java.lang.Integer.valueOf(r7);	 Catch:{ all -> 0x0b35 }
        r8.zze(r12, r13, r7);	 Catch:{ all -> 0x0b35 }
        r7 = r10 + 1;
        r5[r10] = r11;	 Catch:{ all -> 0x0b35 }
    L_0x07d1:
        r57 = r2;
        r58 = r3;
        r56 = r6;
        r10 = r7;
    L_0x07d8:
        r14 = 1;
        goto L_0x0923;
    L_0x07dc:
        r8 = r11.name;	 Catch:{ all -> 0x0b35 }
        r8 = r4.get(r8);	 Catch:{ all -> 0x0b35 }
        r8 = (com.google.android.gms.internal.measurement.zzeq) r8;	 Catch:{ all -> 0x0b35 }
        if (r8 != 0) goto L_0x082c;
    L_0x07e6:
        r8 = r59.zzga();	 Catch:{ all -> 0x0b35 }
        r12 = r2.zzans;	 Catch:{ all -> 0x0b35 }
        r12 = r12.zztd;	 Catch:{ all -> 0x0b35 }
        r13 = r11.name;	 Catch:{ all -> 0x0b35 }
        r8 = r8.zze(r12, r13);	 Catch:{ all -> 0x0b35 }
        if (r8 != 0) goto L_0x082c;
    L_0x07f6:
        r8 = r59.zzgg();	 Catch:{ all -> 0x0b35 }
        r8 = r8.zzin();	 Catch:{ all -> 0x0b35 }
        r12 = "Event being bundled has no eventAggregate. appId, eventName";
        r13 = r2.zzans;	 Catch:{ all -> 0x0b35 }
        r13 = r13.zztd;	 Catch:{ all -> 0x0b35 }
        r14 = r11.name;	 Catch:{ all -> 0x0b35 }
        r8.zze(r12, r13, r14);	 Catch:{ all -> 0x0b35 }
        r8 = new com.google.android.gms.internal.measurement.zzeq;	 Catch:{ all -> 0x0b35 }
        r12 = r2.zzans;	 Catch:{ all -> 0x0b35 }
        r12 = r12.zztd;	 Catch:{ all -> 0x0b35 }
        r13 = r11.name;	 Catch:{ all -> 0x0b35 }
        r45 = 1;
        r47 = 1;
        r14 = r11.zzasw;	 Catch:{ all -> 0x0b35 }
        r49 = r14.longValue();	 Catch:{ all -> 0x0b35 }
        r51 = 0;
        r53 = 0;
        r54 = 0;
        r55 = 0;
        r42 = r8;
        r43 = r12;
        r44 = r13;
        r42.<init>(r43, r44, r45, r47, r49, r51, r53, r54, r55);	 Catch:{ all -> 0x0b35 }
    L_0x082c:
        r59.zzgc();	 Catch:{ all -> 0x0b35 }
        r12 = "_eid";
        r12 = com.google.android.gms.internal.measurement.zzjv.zzb(r11, r12);	 Catch:{ all -> 0x0b35 }
        r12 = (java.lang.Long) r12;	 Catch:{ all -> 0x0b35 }
        if (r12 == 0) goto L_0x083b;
    L_0x0839:
        r13 = 1;
        goto L_0x083c;
    L_0x083b:
        r13 = 0;
    L_0x083c:
        r13 = java.lang.Boolean.valueOf(r13);	 Catch:{ all -> 0x0b35 }
        r14 = 1;
        if (r7 != r14) goto L_0x0865;
    L_0x0843:
        r7 = r10 + 1;
        r5[r10] = r11;	 Catch:{ all -> 0x0b35 }
        r10 = r13.booleanValue();	 Catch:{ all -> 0x0b35 }
        if (r10 == 0) goto L_0x07d1;
    L_0x084d:
        r10 = r8.zzaft;	 Catch:{ all -> 0x0b35 }
        if (r10 != 0) goto L_0x0859;
    L_0x0851:
        r10 = r8.zzafu;	 Catch:{ all -> 0x0b35 }
        if (r10 != 0) goto L_0x0859;
    L_0x0855:
        r10 = r8.zzafv;	 Catch:{ all -> 0x0b35 }
        if (r10 == 0) goto L_0x07d1;
    L_0x0859:
        r10 = 0;
        r8 = r8.zza(r10, r10, r10);	 Catch:{ all -> 0x0b35 }
        r10 = r11.name;	 Catch:{ all -> 0x0b35 }
        r4.put(r10, r8);	 Catch:{ all -> 0x0b35 }
        goto L_0x07d1;
    L_0x0865:
        r14 = r6.nextInt(r7);	 Catch:{ all -> 0x0b35 }
        if (r14 != 0) goto L_0x08a8;
    L_0x086b:
        r59.zzgc();	 Catch:{ all -> 0x0b35 }
        r12 = r11.zzasv;	 Catch:{ all -> 0x0b35 }
        r14 = "_sr";
        r56 = r6;
        r6 = (long) r7;	 Catch:{ all -> 0x0b35 }
        r15 = java.lang.Long.valueOf(r6);	 Catch:{ all -> 0x0b35 }
        r12 = com.google.android.gms.internal.measurement.zzjv.zza(r12, r14, r15);	 Catch:{ all -> 0x0b35 }
        r11.zzasv = r12;	 Catch:{ all -> 0x0b35 }
        r12 = r10 + 1;
        r5[r10] = r11;	 Catch:{ all -> 0x0b35 }
        r10 = r13.booleanValue();	 Catch:{ all -> 0x0b35 }
        if (r10 == 0) goto L_0x0892;
    L_0x0889:
        r6 = java.lang.Long.valueOf(r6);	 Catch:{ all -> 0x0b35 }
        r7 = 0;
        r8 = r8.zza(r7, r6, r7);	 Catch:{ all -> 0x0b35 }
    L_0x0892:
        r6 = r11.name;	 Catch:{ all -> 0x0b35 }
        r7 = r11.zzasw;	 Catch:{ all -> 0x0b35 }
        r10 = r7.longValue();	 Catch:{ all -> 0x0b35 }
        r7 = r8.zzad(r10);	 Catch:{ all -> 0x0b35 }
        r4.put(r6, r7);	 Catch:{ all -> 0x0b35 }
        r57 = r2;
        r58 = r3;
        r10 = r12;
        goto L_0x07d8;
    L_0x08a8:
        r56 = r6;
        r14 = r8.zzafs;	 Catch:{ all -> 0x0b35 }
        r6 = r11.zzasw;	 Catch:{ all -> 0x0b35 }
        r16 = r6.longValue();	 Catch:{ all -> 0x0b35 }
        r57 = r2;
        r58 = r3;
        r2 = r16 - r14;
        r2 = java.lang.Math.abs(r2);	 Catch:{ all -> 0x0b35 }
        r14 = 86400000; // 0x5265c00 float:7.82218E-36 double:4.2687272E-316;
        r6 = (r2 > r14 ? 1 : (r2 == r14 ? 0 : -1));
        if (r6 < 0) goto L_0x0911;
    L_0x08c3:
        r59.zzgc();	 Catch:{ all -> 0x0b35 }
        r2 = r11.zzasv;	 Catch:{ all -> 0x0b35 }
        r3 = "_efs";
        r14 = 1;
        r6 = java.lang.Long.valueOf(r14);	 Catch:{ all -> 0x0b35 }
        r2 = com.google.android.gms.internal.measurement.zzjv.zza(r2, r3, r6);	 Catch:{ all -> 0x0b35 }
        r11.zzasv = r2;	 Catch:{ all -> 0x0b35 }
        r59.zzgc();	 Catch:{ all -> 0x0b35 }
        r2 = r11.zzasv;	 Catch:{ all -> 0x0b35 }
        r3 = "_sr";
        r6 = (long) r7;	 Catch:{ all -> 0x0b35 }
        r12 = java.lang.Long.valueOf(r6);	 Catch:{ all -> 0x0b35 }
        r2 = com.google.android.gms.internal.measurement.zzjv.zza(r2, r3, r12);	 Catch:{ all -> 0x0b35 }
        r11.zzasv = r2;	 Catch:{ all -> 0x0b35 }
        r2 = r10 + 1;
        r5[r10] = r11;	 Catch:{ all -> 0x0b35 }
        r3 = r13.booleanValue();	 Catch:{ all -> 0x0b35 }
        if (r3 == 0) goto L_0x0900;
    L_0x08f2:
        r3 = java.lang.Long.valueOf(r6);	 Catch:{ all -> 0x0b35 }
        r6 = 1;
        r7 = java.lang.Boolean.valueOf(r6);	 Catch:{ all -> 0x0b35 }
        r6 = 0;
        r8 = r8.zza(r6, r3, r7);	 Catch:{ all -> 0x0b35 }
    L_0x0900:
        r3 = r11.name;	 Catch:{ all -> 0x0b35 }
        r6 = r11.zzasw;	 Catch:{ all -> 0x0b35 }
        r6 = r6.longValue();	 Catch:{ all -> 0x0b35 }
        r6 = r8.zzad(r6);	 Catch:{ all -> 0x0b35 }
        r4.put(r3, r6);	 Catch:{ all -> 0x0b35 }
        r10 = r2;
        goto L_0x0923;
    L_0x0911:
        r14 = 1;
        r2 = r13.booleanValue();	 Catch:{ all -> 0x0b35 }
        if (r2 == 0) goto L_0x0923;
    L_0x0919:
        r2 = r11.name;	 Catch:{ all -> 0x0b35 }
        r3 = 0;
        r6 = r8.zza(r12, r3, r3);	 Catch:{ all -> 0x0b35 }
        r4.put(r2, r6);	 Catch:{ all -> 0x0b35 }
    L_0x0923:
        r9 = r9 + 1;
        r7 = r40;
        r8 = r41;
        r6 = r56;
        r2 = r57;
        r3 = r58;
        goto L_0x06c4;
    L_0x0931:
        r57 = r2;
        r2 = r3;
        r3 = r2.zzatd;	 Catch:{ all -> 0x0b35 }
        r3 = r3.length;	 Catch:{ all -> 0x0b35 }
        if (r10 >= r3) goto L_0x0941;
    L_0x0939:
        r3 = java.util.Arrays.copyOf(r5, r10);	 Catch:{ all -> 0x0b35 }
        r3 = (com.google.android.gms.internal.measurement.zzki[]) r3;	 Catch:{ all -> 0x0b35 }
        r2.zzatd = r3;	 Catch:{ all -> 0x0b35 }
    L_0x0941:
        r3 = r4.entrySet();	 Catch:{ all -> 0x0b35 }
        r3 = r3.iterator();	 Catch:{ all -> 0x0b35 }
    L_0x0949:
        r4 = r3.hasNext();	 Catch:{ all -> 0x0b35 }
        if (r4 == 0) goto L_0x0966;
    L_0x094f:
        r4 = r3.next();	 Catch:{ all -> 0x0b35 }
        r4 = (java.util.Map.Entry) r4;	 Catch:{ all -> 0x0b35 }
        r5 = r59.zzga();	 Catch:{ all -> 0x0b35 }
        r4 = r4.getValue();	 Catch:{ all -> 0x0b35 }
        r4 = (com.google.android.gms.internal.measurement.zzeq) r4;	 Catch:{ all -> 0x0b35 }
        r5.zza(r4);	 Catch:{ all -> 0x0b35 }
        goto L_0x0949;
    L_0x0963:
        r57 = r2;
        r2 = r3;
    L_0x0966:
        r3 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
        r3 = java.lang.Long.valueOf(r3);	 Catch:{ all -> 0x0b35 }
        r2.zzatg = r3;	 Catch:{ all -> 0x0b35 }
        r3 = -9223372036854775808;
        r3 = java.lang.Long.valueOf(r3);	 Catch:{ all -> 0x0b35 }
        r2.zzath = r3;	 Catch:{ all -> 0x0b35 }
        r3 = 0;
    L_0x097a:
        r4 = r2.zzatd;	 Catch:{ all -> 0x0b35 }
        r4 = r4.length;	 Catch:{ all -> 0x0b35 }
        if (r3 >= r4) goto L_0x09ae;
    L_0x097f:
        r4 = r2.zzatd;	 Catch:{ all -> 0x0b35 }
        r4 = r4[r3];	 Catch:{ all -> 0x0b35 }
        r5 = r4.zzasw;	 Catch:{ all -> 0x0b35 }
        r5 = r5.longValue();	 Catch:{ all -> 0x0b35 }
        r7 = r2.zzatg;	 Catch:{ all -> 0x0b35 }
        r7 = r7.longValue();	 Catch:{ all -> 0x0b35 }
        r9 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1));
        if (r9 >= 0) goto L_0x0997;
    L_0x0993:
        r5 = r4.zzasw;	 Catch:{ all -> 0x0b35 }
        r2.zzatg = r5;	 Catch:{ all -> 0x0b35 }
    L_0x0997:
        r5 = r4.zzasw;	 Catch:{ all -> 0x0b35 }
        r5 = r5.longValue();	 Catch:{ all -> 0x0b35 }
        r7 = r2.zzath;	 Catch:{ all -> 0x0b35 }
        r7 = r7.longValue();	 Catch:{ all -> 0x0b35 }
        r9 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1));
        if (r9 <= 0) goto L_0x09ab;
    L_0x09a7:
        r4 = r4.zzasw;	 Catch:{ all -> 0x0b35 }
        r2.zzath = r4;	 Catch:{ all -> 0x0b35 }
    L_0x09ab:
        r3 = r3 + 1;
        goto L_0x097a;
    L_0x09ae:
        r3 = r57;
        r4 = r3.zzans;	 Catch:{ all -> 0x0b35 }
        r4 = r4.zztd;	 Catch:{ all -> 0x0b35 }
        r5 = r59.zzga();	 Catch:{ all -> 0x0b35 }
        r5 = r5.zzax(r4);	 Catch:{ all -> 0x0b35 }
        if (r5 != 0) goto L_0x09d4;
    L_0x09be:
        r5 = r59.zzgg();	 Catch:{ all -> 0x0b35 }
        r5 = r5.zzil();	 Catch:{ all -> 0x0b35 }
        r6 = "Bundling raw events w/o app info. appId";
        r7 = r3.zzans;	 Catch:{ all -> 0x0b35 }
        r7 = r7.zztd;	 Catch:{ all -> 0x0b35 }
        r7 = com.google.android.gms.internal.measurement.zzfg.zzbh(r7);	 Catch:{ all -> 0x0b35 }
        r5.zzg(r6, r7);	 Catch:{ all -> 0x0b35 }
        goto L_0x0a30;
    L_0x09d4:
        r6 = r2.zzatd;	 Catch:{ all -> 0x0b35 }
        r6 = r6.length;	 Catch:{ all -> 0x0b35 }
        if (r6 <= 0) goto L_0x0a30;
    L_0x09d9:
        r6 = r5.zzgn();	 Catch:{ all -> 0x0b35 }
        r8 = 0;
        r10 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1));
        if (r10 == 0) goto L_0x09e8;
    L_0x09e3:
        r8 = java.lang.Long.valueOf(r6);	 Catch:{ all -> 0x0b35 }
        goto L_0x09e9;
    L_0x09e8:
        r8 = 0;
    L_0x09e9:
        r2.zzatj = r8;	 Catch:{ all -> 0x0b35 }
        r8 = r5.zzgm();	 Catch:{ all -> 0x0b35 }
        r10 = 0;
        r12 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1));
        if (r12 != 0) goto L_0x09f6;
    L_0x09f5:
        goto L_0x09f7;
    L_0x09f6:
        r6 = r8;
    L_0x09f7:
        r8 = (r6 > r10 ? 1 : (r6 == r10 ? 0 : -1));
        if (r8 == 0) goto L_0x0a00;
    L_0x09fb:
        r6 = java.lang.Long.valueOf(r6);	 Catch:{ all -> 0x0b35 }
        goto L_0x0a01;
    L_0x0a00:
        r6 = 0;
    L_0x0a01:
        r2.zzati = r6;	 Catch:{ all -> 0x0b35 }
        r5.zzgv();	 Catch:{ all -> 0x0b35 }
        r6 = r5.zzgs();	 Catch:{ all -> 0x0b35 }
        r6 = (int) r6;	 Catch:{ all -> 0x0b35 }
        r6 = java.lang.Integer.valueOf(r6);	 Catch:{ all -> 0x0b35 }
        r2.zzatt = r6;	 Catch:{ all -> 0x0b35 }
        r6 = r2.zzatg;	 Catch:{ all -> 0x0b35 }
        r6 = r6.longValue();	 Catch:{ all -> 0x0b35 }
        r5.zzm(r6);	 Catch:{ all -> 0x0b35 }
        r6 = r2.zzath;	 Catch:{ all -> 0x0b35 }
        r6 = r6.longValue();	 Catch:{ all -> 0x0b35 }
        r5.zzn(r6);	 Catch:{ all -> 0x0b35 }
        r6 = r5.zzhd();	 Catch:{ all -> 0x0b35 }
        r2.zzaef = r6;	 Catch:{ all -> 0x0b35 }
        r6 = r59.zzga();	 Catch:{ all -> 0x0b35 }
        r6.zza(r5);	 Catch:{ all -> 0x0b35 }
    L_0x0a30:
        r5 = r2.zzatd;	 Catch:{ all -> 0x0b35 }
        r5 = r5.length;	 Catch:{ all -> 0x0b35 }
        if (r5 <= 0) goto L_0x0a7c;
    L_0x0a35:
        r5 = r59.zzgd();	 Catch:{ all -> 0x0b35 }
        r6 = r3.zzans;	 Catch:{ all -> 0x0b35 }
        r6 = r6.zztd;	 Catch:{ all -> 0x0b35 }
        r5 = r5.zzbp(r6);	 Catch:{ all -> 0x0b35 }
        if (r5 == 0) goto L_0x0a4d;
    L_0x0a43:
        r6 = r5.zzask;	 Catch:{ all -> 0x0b35 }
        if (r6 != 0) goto L_0x0a48;
    L_0x0a47:
        goto L_0x0a4d;
    L_0x0a48:
        r5 = r5.zzask;	 Catch:{ all -> 0x0b35 }
    L_0x0a4a:
        r2.zzaua = r5;	 Catch:{ all -> 0x0b35 }
        goto L_0x0a73;
    L_0x0a4d:
        r5 = r3.zzans;	 Catch:{ all -> 0x0b35 }
        r5 = r5.zzadh;	 Catch:{ all -> 0x0b35 }
        r5 = android.text.TextUtils.isEmpty(r5);	 Catch:{ all -> 0x0b35 }
        if (r5 == 0) goto L_0x0a5e;
    L_0x0a57:
        r5 = -1;
        r5 = java.lang.Long.valueOf(r5);	 Catch:{ all -> 0x0b35 }
        goto L_0x0a4a;
    L_0x0a5e:
        r5 = r59.zzgg();	 Catch:{ all -> 0x0b35 }
        r5 = r5.zzin();	 Catch:{ all -> 0x0b35 }
        r6 = "Did not find measurement config or missing version info. appId";
        r7 = r3.zzans;	 Catch:{ all -> 0x0b35 }
        r7 = r7.zztd;	 Catch:{ all -> 0x0b35 }
        r7 = com.google.android.gms.internal.measurement.zzfg.zzbh(r7);	 Catch:{ all -> 0x0b35 }
        r5.zzg(r6, r7);	 Catch:{ all -> 0x0b35 }
    L_0x0a73:
        r5 = r59.zzga();	 Catch:{ all -> 0x0b35 }
        r10 = r27;
        r5.zza(r2, r10);	 Catch:{ all -> 0x0b35 }
    L_0x0a7c:
        r2 = r59.zzga();	 Catch:{ all -> 0x0b35 }
        r3 = r3.zzant;	 Catch:{ all -> 0x0b35 }
        com.google.android.gms.common.internal.Preconditions.checkNotNull(r3);	 Catch:{ all -> 0x0b35 }
        r2.zzab();	 Catch:{ all -> 0x0b35 }
        r2.zzch();	 Catch:{ all -> 0x0b35 }
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0b35 }
        r6 = "rowid in (";
        r5.<init>(r6);	 Catch:{ all -> 0x0b35 }
        r6 = 0;
    L_0x0a93:
        r7 = r3.size();	 Catch:{ all -> 0x0b35 }
        if (r6 >= r7) goto L_0x0ab0;
    L_0x0a99:
        if (r6 == 0) goto L_0x0aa0;
    L_0x0a9b:
        r7 = ",";
        r5.append(r7);	 Catch:{ all -> 0x0b35 }
    L_0x0aa0:
        r7 = r3.get(r6);	 Catch:{ all -> 0x0b35 }
        r7 = (java.lang.Long) r7;	 Catch:{ all -> 0x0b35 }
        r7 = r7.longValue();	 Catch:{ all -> 0x0b35 }
        r5.append(r7);	 Catch:{ all -> 0x0b35 }
        r6 = r6 + 1;
        goto L_0x0a93;
    L_0x0ab0:
        r6 = ")";
        r5.append(r6);	 Catch:{ all -> 0x0b35 }
        r6 = r2.getWritableDatabase();	 Catch:{ all -> 0x0b35 }
        r7 = "raw_events";
        r5 = r5.toString();	 Catch:{ all -> 0x0b35 }
        r8 = 0;
        r5 = r6.delete(r7, r5, r8);	 Catch:{ all -> 0x0b35 }
        r6 = r3.size();	 Catch:{ all -> 0x0b35 }
        if (r5 == r6) goto L_0x0ae3;
    L_0x0aca:
        r2 = r2.zzgg();	 Catch:{ all -> 0x0b35 }
        r2 = r2.zzil();	 Catch:{ all -> 0x0b35 }
        r6 = "Deleted fewer rows from raw events table than expected";
        r5 = java.lang.Integer.valueOf(r5);	 Catch:{ all -> 0x0b35 }
        r3 = r3.size();	 Catch:{ all -> 0x0b35 }
        r3 = java.lang.Integer.valueOf(r3);	 Catch:{ all -> 0x0b35 }
        r2.zze(r6, r5, r3);	 Catch:{ all -> 0x0b35 }
    L_0x0ae3:
        r2 = r59.zzga();	 Catch:{ all -> 0x0b35 }
        r3 = r2.getWritableDatabase();	 Catch:{ all -> 0x0b35 }
        r5 = "delete from raw_events_metadata where app_id=? and metadata_fingerprint not in (select distinct metadata_fingerprint from raw_events where app_id=?)";
        r6 = 2;
        r6 = new java.lang.String[r6];	 Catch:{ SQLiteException -> 0x0afa }
        r7 = 0;
        r6[r7] = r4;	 Catch:{ SQLiteException -> 0x0afa }
        r7 = 1;
        r6[r7] = r4;	 Catch:{ SQLiteException -> 0x0afa }
        r3.execSQL(r5, r6);	 Catch:{ SQLiteException -> 0x0afa }
        goto L_0x0b0d;
    L_0x0afa:
        r0 = move-exception;
        r3 = r0;
        r2 = r2.zzgg();	 Catch:{ all -> 0x0b35 }
        r2 = r2.zzil();	 Catch:{ all -> 0x0b35 }
        r5 = "Failed to remove unused event metadata. appId";
        r4 = com.google.android.gms.internal.measurement.zzfg.zzbh(r4);	 Catch:{ all -> 0x0b35 }
        r2.zze(r5, r4, r3);	 Catch:{ all -> 0x0b35 }
    L_0x0b0d:
        r2 = r59.zzga();	 Catch:{ all -> 0x0b35 }
        r2.setTransactionSuccessful();	 Catch:{ all -> 0x0b35 }
        r2 = r59.zzga();
        r2.endTransaction();
        r2 = 1;
        return r2;
    L_0x0b1d:
        r2 = r59.zzga();	 Catch:{ all -> 0x0b35 }
        r2.setTransactionSuccessful();	 Catch:{ all -> 0x0b35 }
        r2 = r59.zzga();
        r2.endTransaction();
        r2 = 0;
        return r2;
    L_0x0b2d:
        r0 = move-exception;
    L_0x0b2e:
        r2 = r0;
    L_0x0b2f:
        if (r6 == 0) goto L_0x0b34;
    L_0x0b31:
        r6.close();	 Catch:{ all -> 0x0b35 }
    L_0x0b34:
        throw r2;	 Catch:{ all -> 0x0b35 }
    L_0x0b35:
        r0 = move-exception;
        r2 = r0;
        r3 = r59.zzga();
        r3.endTransaction();
        throw r2;
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
            long nextInt = 1 + ((long) zzgh.zzgc().zzku().nextInt(86400000));
            zzgh.zzajx.set(nextInt);
            j = nextInt;
        }
        return ((((currentTimeMillis + j) / 1000) / 60) / 60) / 24;
    }

    private final boolean zzjx() {
        zzgf().zzab();
        zzch();
        if (!zzga().zzhs()) {
            if (TextUtils.isEmpty(zzga().zzhn())) {
                return false;
            }
        }
        return true;
    }

    private final void zzjy() {
        zzgl com_google_android_gms_internal_measurement_zzgl = this;
        zzgf().zzab();
        zzch();
        if (zzkb()) {
            long abs;
            if (com_google_android_gms_internal_measurement_zzgl.zzank > 0) {
                abs = 3600000 - Math.abs(com_google_android_gms_internal_measurement_zzgl.zzrj.elapsedRealtime() - com_google_android_gms_internal_measurement_zzgl.zzank);
                if (abs > 0) {
                    zzgg().zzir().zzg("Upload has been suspended. Will update scheduling later in approximately ms", Long.valueOf(abs));
                    zzjr().unregister();
                    zzjs().cancel();
                    return;
                }
                com_google_android_gms_internal_measurement_zzgl.zzank = 0;
            }
            if (zzjk()) {
                if (zzjx()) {
                    Object obj;
                    CharSequence zzhl;
                    zzex com_google_android_gms_internal_measurement_zzex;
                    long max;
                    long j;
                    long j2;
                    long j3;
                    long j4;
                    long abs2;
                    long abs3;
                    long j5;
                    int i;
                    long currentTimeMillis = com_google_android_gms_internal_measurement_zzgl.zzrj.currentTimeMillis();
                    long max2 = Math.max(0, ((Long) zzew.zzahl.get()).longValue());
                    if (!zzga().zzht()) {
                        if (!zzga().zzho()) {
                            obj = null;
                            if (obj == null) {
                                zzhl = com_google_android_gms_internal_measurement_zzgl.zzamd.zzhl();
                                com_google_android_gms_internal_measurement_zzex = (TextUtils.isEmpty(zzhl) || ".none.".equals(zzhl)) ? zzew.zzahf : zzew.zzahg;
                            } else {
                                com_google_android_gms_internal_measurement_zzex = zzew.zzahe;
                            }
                            max = Math.max(0, ((Long) com_google_android_gms_internal_measurement_zzex.get()).longValue());
                            j = zzgh().zzajt.get();
                            j2 = zzgh().zzaju.get();
                            j3 = max;
                            j4 = max2;
                            max2 = Math.max(zzga().zzhq(), zzga().zzhr());
                            if (max2 != 0) {
                                abs2 = currentTimeMillis - Math.abs(max2 - currentTimeMillis);
                                abs3 = currentTimeMillis - Math.abs(j2 - currentTimeMillis);
                                currentTimeMillis = Math.max(currentTimeMillis - Math.abs(j - currentTimeMillis), abs3);
                                max2 = abs2 + j4;
                                if (obj != null && currentTimeMillis > 0) {
                                    max2 = Math.min(abs2, currentTimeMillis) + j3;
                                }
                                j5 = j3;
                                if (!zzgc().zza(currentTimeMillis, j5)) {
                                    max2 = currentTimeMillis + j5;
                                }
                                if (abs3 != 0 && abs3 >= abs2) {
                                    i = 0;
                                    while (i < Math.min(20, Math.max(0, ((Integer) zzew.zzahn.get()).intValue()))) {
                                        max = max2 + (Math.max(0, ((Long) zzew.zzahm.get()).longValue()) * (1 << i));
                                        if (max > abs3) {
                                            max2 = max;
                                            break;
                                        } else {
                                            i++;
                                            max2 = max;
                                        }
                                    }
                                }
                                if (max2 != 0) {
                                    zzgg().zzir().log("Next upload time is 0");
                                    zzjr().unregister();
                                    zzjs().cancel();
                                    return;
                                } else if (zzjq().zzew()) {
                                    currentTimeMillis = zzgh().zzajv.get();
                                    abs = Math.max(0, ((Long) zzew.zzahc.get()).longValue());
                                    if (!zzgc().zza(currentTimeMillis, abs)) {
                                        max2 = Math.max(max2, currentTimeMillis + abs);
                                    }
                                    zzjr().unregister();
                                    abs = max2 - com_google_android_gms_internal_measurement_zzgl.zzrj.currentTimeMillis();
                                    if (abs <= 0) {
                                        abs = Math.max(0, ((Long) zzew.zzahh.get()).longValue());
                                        zzgh().zzajt.set(com_google_android_gms_internal_measurement_zzgl.zzrj.currentTimeMillis());
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
                            max2 = 0;
                            if (max2 != 0) {
                                zzgg().zzir().log("Next upload time is 0");
                                zzjr().unregister();
                                zzjs().cancel();
                                return;
                            } else if (zzjq().zzew()) {
                                currentTimeMillis = zzgh().zzajv.get();
                                abs = Math.max(0, ((Long) zzew.zzahc.get()).longValue());
                                if (zzgc().zza(currentTimeMillis, abs)) {
                                    max2 = Math.max(max2, currentTimeMillis + abs);
                                }
                                zzjr().unregister();
                                abs = max2 - com_google_android_gms_internal_measurement_zzgl.zzrj.currentTimeMillis();
                                if (abs <= 0) {
                                    abs = Math.max(0, ((Long) zzew.zzahh.get()).longValue());
                                    zzgh().zzajt.set(com_google_android_gms_internal_measurement_zzgl.zzrj.currentTimeMillis());
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
                    }
                    obj = 1;
                    if (obj == null) {
                        com_google_android_gms_internal_measurement_zzex = zzew.zzahe;
                    } else {
                        zzhl = com_google_android_gms_internal_measurement_zzgl.zzamd.zzhl();
                        if (!TextUtils.isEmpty(zzhl)) {
                        }
                    }
                    max = Math.max(0, ((Long) com_google_android_gms_internal_measurement_zzex.get()).longValue());
                    j = zzgh().zzajt.get();
                    j2 = zzgh().zzaju.get();
                    j3 = max;
                    j4 = max2;
                    max2 = Math.max(zzga().zzhq(), zzga().zzhr());
                    if (max2 != 0) {
                        abs2 = currentTimeMillis - Math.abs(max2 - currentTimeMillis);
                        abs3 = currentTimeMillis - Math.abs(j2 - currentTimeMillis);
                        currentTimeMillis = Math.max(currentTimeMillis - Math.abs(j - currentTimeMillis), abs3);
                        max2 = abs2 + j4;
                        max2 = Math.min(abs2, currentTimeMillis) + j3;
                        j5 = j3;
                        if (zzgc().zza(currentTimeMillis, j5)) {
                            max2 = currentTimeMillis + j5;
                        }
                        i = 0;
                        while (i < Math.min(20, Math.max(0, ((Integer) zzew.zzahn.get()).intValue()))) {
                            max = max2 + (Math.max(0, ((Long) zzew.zzahm.get()).longValue()) * (1 << i));
                            if (max > abs3) {
                                max2 = max;
                                break;
                            } else {
                                i++;
                                max2 = max;
                            }
                        }
                    }
                    max2 = 0;
                    if (max2 != 0) {
                        zzgg().zzir().log("Next upload time is 0");
                        zzjr().unregister();
                        zzjs().cancel();
                        return;
                    } else if (zzjq().zzew()) {
                        zzgg().zzir().log("No network");
                        zzjr().zzet();
                        zzjs().cancel();
                        return;
                    } else {
                        currentTimeMillis = zzgh().zzajv.get();
                        abs = Math.max(0, ((Long) zzew.zzahc.get()).longValue());
                        if (zzgc().zza(currentTimeMillis, abs)) {
                            max2 = Math.max(max2, currentTimeMillis + abs);
                        }
                        zzjr().unregister();
                        abs = max2 - com_google_android_gms_internal_measurement_zzgl.zzrj.currentTimeMillis();
                        if (abs <= 0) {
                            abs = Math.max(0, ((Long) zzew.zzahh.get()).longValue());
                            zzgh().zzajt.set(com_google_android_gms_internal_measurement_zzgl.zzrj.currentTimeMillis());
                        }
                        zzgg().zzir().zzg("Upload scheduled in approximately ms", Long.valueOf(abs));
                        zzjs().zzh(abs);
                        return;
                    }
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
        if (!(this.zzanl || this.zzanm)) {
            if (!this.zzann) {
                zzgg().zzir().log("Stopping uploading service(s)");
                if (this.zzang != null) {
                    for (Runnable run : this.zzang) {
                        run.run();
                    }
                    this.zzang.clear();
                    return;
                }
                return;
            }
        }
        zzgg().zzir().zzd("Not stopping services. fetch, network, upload", Boolean.valueOf(this.zzanl), Boolean.valueOf(this.zzanm), Boolean.valueOf(this.zzann));
    }

    public final Context getContext() {
        return this.zzqs;
    }

    public final boolean isEnabled() {
        zzgf().zzab();
        zzch();
        boolean z = false;
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
                    zzgh().zzh(isEnabled ^ 1);
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
        int i2 = 1;
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
            if (i != 503) {
                if (i != 429) {
                    i2 = 0;
                }
            }
            if (i2 != 0) {
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
        zzei zzga;
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
        boolean z = true;
        boolean z2 = (i == Callback.DEFAULT_DRAG_ANIMATION_DURATION || i == 204 || i == 304) && th == null;
        if (zzax == null) {
            zzgg().zzin().zzg("App does not exist in onConfigFetched. appId", zzfg.zzbh(str));
            zzga().setTransactionSuccessful();
            zzga = zzga();
        } else {
            if (!z2) {
                if (i != 404) {
                    zzax.zzt(this.zzrj.currentTimeMillis());
                    zzga().zza(zzax);
                    zzgg().zzir().zze("Fetching config failed. code, error", Integer.valueOf(i), th);
                    zzgd().zzbr(str);
                    zzgh().zzaju.set(this.zzrj.currentTimeMillis());
                    if (i != 503) {
                        if (i != 429) {
                            z = false;
                        }
                    }
                    if (z) {
                        zzgh().zzajv.set(this.zzrj.currentTimeMillis());
                    }
                    zzjy();
                    zzga().setTransactionSuccessful();
                    zzga = zzga();
                }
            }
            List list = map != null ? (List) map.get("Last-Modified") : null;
            String str2 = (list == null || list.size() <= 0) ? null : (String) list.get(0);
            if (i != 404) {
                if (i != 304) {
                    if (!zzgd().zza(str, bArr, str2)) {
                        zzga = zzga();
                    }
                    zzax.zzs(this.zzrj.currentTimeMillis());
                    zzga().zza(zzax);
                    if (i != 404) {
                        zzgg().zzio().zzg("Config not found. Using empty config. appId", str);
                    } else {
                        zzgg().zzir().zze("Successfully fetched config. Got network response. code, size", Integer.valueOf(i), Integer.valueOf(bArr.length));
                    }
                    if (zzjq().zzew() && zzjx()) {
                        zzjw();
                        zzga().setTransactionSuccessful();
                        zzga = zzga();
                    }
                    zzjy();
                    zzga().setTransactionSuccessful();
                    zzga = zzga();
                }
            }
            if (zzgd().zzbp(str) == null && !zzgd().zza(str, null, null)) {
                zzga = zzga();
            }
            zzax.zzs(this.zzrj.currentTimeMillis());
            zzga().zza(zzax);
            if (i != 404) {
                zzgg().zzir().zze("Successfully fetched config. Got network response. code, size", Integer.valueOf(i), Integer.valueOf(bArr.length));
            } else {
                zzgg().zzio().zzg("Config not found. Using empty config. appId", str);
            }
            zzjw();
            zzga().setTransactionSuccessful();
            zzga = zzga();
        }
        zzga.endTransaction();
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
        zzch();
        zzgf().zzab();
        if (this.zzanb == null || this.zzanc == 0 || !(this.zzanb == null || this.zzanb.booleanValue() || Math.abs(this.zzrj.elapsedRealtime() - this.zzanc) <= 1000)) {
            this.zzanc = this.zzrj.elapsedRealtime();
            boolean z = false;
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
        String zzhn;
        String str;
        zzgf().zzab();
        zzch();
        this.zzann = true;
        try {
            zzfi zzin;
            String str2;
            Boolean zzkn = zzfx().zzkn();
            if (zzkn == null) {
                zzin = zzgg().zzin();
                str2 = "Upload data called on the client side before use of service was decided";
            } else if (zzkn.booleanValue()) {
                zzin = zzgg().zzil();
                str2 = "Upload called in the client side when service should be used";
            } else {
                if (this.zzank <= 0) {
                    zzgf().zzab();
                    if (this.zzanf != null) {
                        zzin = zzgg().zzir();
                        str2 = "Uploading requested multiple times";
                    } else if (zzjq().zzew()) {
                        long currentTimeMillis = this.zzrj.currentTimeMillis();
                        Object obj = null;
                        zzd(null, currentTimeMillis - zzeh.zzhk());
                        long j = zzgh().zzajt.get();
                        if (j != 0) {
                            zzgg().zziq().zzg("Uploading events. Elapsed time since last upload attempt (ms)", Long.valueOf(Math.abs(currentTimeMillis - j)));
                        }
                        zzhn = zzga().zzhn();
                        if (TextUtils.isEmpty(zzhn)) {
                            this.zzanj = -1;
                            Object zzab = zzga().zzab(currentTimeMillis - zzeh.zzhk());
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
                                Object obj2;
                                List subList;
                                for (Pair pair : zzb) {
                                    zzkl com_google_android_gms_internal_measurement_zzkl = (zzkl) pair.first;
                                    if (!TextUtils.isEmpty(com_google_android_gms_internal_measurement_zzkl.zzatq)) {
                                        obj2 = com_google_android_gms_internal_measurement_zzkl.zzatq;
                                        break;
                                    }
                                }
                                obj2 = null;
                                if (obj2 != null) {
                                    for (int i = 0; i < zzb.size(); i++) {
                                        zzkl com_google_android_gms_internal_measurement_zzkl2 = (zzkl) ((Pair) zzb.get(i)).first;
                                        if (!TextUtils.isEmpty(com_google_android_gms_internal_measurement_zzkl2.zzatq) && !com_google_android_gms_internal_measurement_zzkl2.zzatq.equals(obj2)) {
                                            subList = zzb.subList(0, i);
                                            break;
                                        }
                                    }
                                }
                                zzkk com_google_android_gms_internal_measurement_zzkk = new zzkk();
                                com_google_android_gms_internal_measurement_zzkk.zzata = new zzkl[subList.size()];
                                Collection arrayList = new ArrayList(subList.size());
                                boolean z = zzeh.zzhm() && this.zzamd.zzat(zzhn);
                                for (int i2 = 0; i2 < com_google_android_gms_internal_measurement_zzkk.zzata.length; i2++) {
                                    com_google_android_gms_internal_measurement_zzkk.zzata[i2] = (zzkl) ((Pair) subList.get(i2)).first;
                                    arrayList.add((Long) ((Pair) subList.get(i2)).second);
                                    com_google_android_gms_internal_measurement_zzkk.zzata[i2].zzatp = Long.valueOf(12451);
                                    com_google_android_gms_internal_measurement_zzkk.zzata[i2].zzatf = Long.valueOf(currentTimeMillis);
                                    com_google_android_gms_internal_measurement_zzkk.zzata[i2].zzatu = Boolean.valueOf(false);
                                    if (!z) {
                                        com_google_android_gms_internal_measurement_zzkk.zzata[i2].zzauc = null;
                                    }
                                }
                                if (zzgg().isLoggable(2)) {
                                    obj = zzgb().zza(com_google_android_gms_internal_measurement_zzkk);
                                }
                                Object zzb2 = zzgc().zzb(com_google_android_gms_internal_measurement_zzkk);
                                str = (String) zzew.zzahb.get();
                                URL url = new URL(str);
                                Preconditions.checkArgument(arrayList.isEmpty() ^ true);
                                if (this.zzanf != null) {
                                    zzgg().zzil().log("Set uploading progress before finishing the previous upload");
                                } else {
                                    this.zzanf = new ArrayList(arrayList);
                                }
                                zzgh().zzaju.set(currentTimeMillis);
                                Object obj3 = "?";
                                if (com_google_android_gms_internal_measurement_zzkk.zzata.length > 0) {
                                    obj3 = com_google_android_gms_internal_measurement_zzkk.zzata[0].zztd;
                                }
                                zzgg().zzir().zzd("Uploading data. app, uncompressed size, data", obj3, Integer.valueOf(zzb2.length), obj);
                                this.zzanm = true;
                                zzhj zzjq = zzjq();
                                zzfm com_google_android_gms_internal_measurement_zzgo = new zzgo(this, zzhn);
                                zzjq.zzab();
                                zzjq.zzch();
                                Preconditions.checkNotNull(url);
                                Preconditions.checkNotNull(zzb2);
                                Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzgo);
                                zzjq.zzgf().zzd(new zzfo(zzjq, zzhn, url, zzb2, null, com_google_android_gms_internal_measurement_zzgo));
                            }
                        }
                        this.zzann = false;
                        zzkc();
                    } else {
                        zzgg().zzir().log("Network not connected, ignoring upload request");
                    }
                }
                zzjy();
                this.zzann = false;
                zzkc();
            }
            zzin.log(str2);
        } catch (MalformedURLException e) {
            zzgg().zzil().zze("Failed to parse upload URL. Not uploading. appId", zzfg.zzbh(zzhn), str);
        } catch (Throwable th) {
            this.zzann = false;
            zzkc();
        }
        this.zzann = false;
        zzkc();
    }

    final void zzjz() {
        this.zzani++;
    }
}
