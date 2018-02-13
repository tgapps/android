package com.google.android.gms.internal;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri.Builder;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Pair;
import com.google.android.gms.common.api.internal.zzbz;
import com.google.android.gms.common.internal.zzbq;
import com.google.android.gms.common.util.zzd;
import com.google.android.gms.common.util.zzh;
import com.google.android.gms.measurement.AppMeasurement;
import com.google.firebase.analytics.FirebaseAnalytics;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;
import org.telegram.messenger.exoplayer2.C;
import org.telegram.messenger.support.widget.helper.ItemTouchHelper.Callback;

public class zzcim {
    private static volatile zzcim zzjev;
    private final Context mContext;
    private final zzd zzata;
    private boolean zzdtb = false;
    private final zzcgn zzjew;
    private final zzchx zzjex;
    private final zzchm zzjey;
    private final zzcih zzjez;
    private final zzclf zzjfa;
    private final zzcig zzjfb;
    private final AppMeasurement zzjfc;
    private final FirebaseAnalytics zzjfd;
    private final zzclq zzjfe;
    private final zzchk zzjff;
    private final zzcgo zzjfg;
    private final zzchi zzjfh;
    private final zzchq zzjfi;
    private final zzckc zzjfj;
    private final zzckg zzjfk;
    private final zzcgu zzjfl;
    private final zzcjn zzjfm;
    private final zzchh zzjfn;
    private final zzchv zzjfo;
    private final zzcll zzjfp;
    private final zzcgk zzjfq;
    private final zzcgd zzjfr;
    private boolean zzjfs;
    private Boolean zzjft;
    private long zzjfu;
    private FileLock zzjfv;
    private FileChannel zzjfw;
    private List<Long> zzjfx;
    private List<Runnable> zzjfy;
    private int zzjfz;
    private int zzjga;
    private long zzjgb;
    private long zzjgc;
    private boolean zzjgd;
    private boolean zzjge;
    private boolean zzjgf;
    private final long zzjgg;

    class zza implements zzcgq {
        List<zzcmb> zzapa;
        private /* synthetic */ zzcim zzjgh;
        zzcme zzjgi;
        List<Long> zzjgj;
        private long zzjgk;

        private zza(zzcim com_google_android_gms_internal_zzcim) {
            this.zzjgh = com_google_android_gms_internal_zzcim;
        }

        private static long zza(zzcmb com_google_android_gms_internal_zzcmb) {
            return ((com_google_android_gms_internal_zzcmb.zzjli.longValue() / 1000) / 60) / 60;
        }

        public final boolean zza(long j, zzcmb com_google_android_gms_internal_zzcmb) {
            zzbq.checkNotNull(com_google_android_gms_internal_zzcmb);
            if (this.zzapa == null) {
                this.zzapa = new ArrayList();
            }
            if (this.zzjgj == null) {
                this.zzjgj = new ArrayList();
            }
            if (this.zzapa.size() > 0 && zza((zzcmb) this.zzapa.get(0)) != zza(com_google_android_gms_internal_zzcmb)) {
                return false;
            }
            long zzho = this.zzjgk + ((long) com_google_android_gms_internal_zzcmb.zzho());
            if (zzho >= ((long) Math.max(0, ((Integer) zzchc.zzjal.get()).intValue()))) {
                return false;
            }
            this.zzjgk = zzho;
            this.zzapa.add(com_google_android_gms_internal_zzcmb);
            this.zzjgj.add(Long.valueOf(j));
            return this.zzapa.size() < Math.max(1, ((Integer) zzchc.zzjam.get()).intValue());
        }

        public final void zzb(zzcme com_google_android_gms_internal_zzcme) {
            zzbq.checkNotNull(com_google_android_gms_internal_zzcme);
            this.zzjgi = com_google_android_gms_internal_zzcme;
        }
    }

    private zzcim(zzcjm com_google_android_gms_internal_zzcjm) {
        zzbq.checkNotNull(com_google_android_gms_internal_zzcjm);
        this.mContext = com_google_android_gms_internal_zzcjm.mContext;
        this.zzjgb = -1;
        this.zzata = zzh.zzamg();
        this.zzjgg = this.zzata.currentTimeMillis();
        this.zzjew = new zzcgn(this);
        zzcjl com_google_android_gms_internal_zzchx = new zzchx(this);
        com_google_android_gms_internal_zzchx.initialize();
        this.zzjex = com_google_android_gms_internal_zzchx;
        com_google_android_gms_internal_zzchx = new zzchm(this);
        com_google_android_gms_internal_zzchx.initialize();
        this.zzjey = com_google_android_gms_internal_zzchx;
        com_google_android_gms_internal_zzchx = new zzclq(this);
        com_google_android_gms_internal_zzchx.initialize();
        this.zzjfe = com_google_android_gms_internal_zzchx;
        com_google_android_gms_internal_zzchx = new zzchk(this);
        com_google_android_gms_internal_zzchx.initialize();
        this.zzjff = com_google_android_gms_internal_zzchx;
        com_google_android_gms_internal_zzchx = new zzcgu(this);
        com_google_android_gms_internal_zzchx.initialize();
        this.zzjfl = com_google_android_gms_internal_zzchx;
        com_google_android_gms_internal_zzchx = new zzchh(this);
        com_google_android_gms_internal_zzchx.initialize();
        this.zzjfn = com_google_android_gms_internal_zzchx;
        com_google_android_gms_internal_zzchx = new zzcgo(this);
        com_google_android_gms_internal_zzchx.initialize();
        this.zzjfg = com_google_android_gms_internal_zzchx;
        com_google_android_gms_internal_zzchx = new zzchi(this);
        com_google_android_gms_internal_zzchx.initialize();
        this.zzjfh = com_google_android_gms_internal_zzchx;
        com_google_android_gms_internal_zzchx = new zzcgk(this);
        com_google_android_gms_internal_zzchx.initialize();
        this.zzjfq = com_google_android_gms_internal_zzchx;
        this.zzjfr = new zzcgd(this);
        com_google_android_gms_internal_zzchx = new zzchq(this);
        com_google_android_gms_internal_zzchx.initialize();
        this.zzjfi = com_google_android_gms_internal_zzchx;
        com_google_android_gms_internal_zzchx = new zzckc(this);
        com_google_android_gms_internal_zzchx.initialize();
        this.zzjfj = com_google_android_gms_internal_zzchx;
        com_google_android_gms_internal_zzchx = new zzckg(this);
        com_google_android_gms_internal_zzchx.initialize();
        this.zzjfk = com_google_android_gms_internal_zzchx;
        com_google_android_gms_internal_zzchx = new zzcjn(this);
        com_google_android_gms_internal_zzchx.initialize();
        this.zzjfm = com_google_android_gms_internal_zzchx;
        com_google_android_gms_internal_zzchx = new zzcll(this);
        com_google_android_gms_internal_zzchx.initialize();
        this.zzjfp = com_google_android_gms_internal_zzchx;
        this.zzjfo = new zzchv(this);
        this.zzjfc = new AppMeasurement(this);
        this.zzjfd = new FirebaseAnalytics(this);
        com_google_android_gms_internal_zzchx = new zzclf(this);
        com_google_android_gms_internal_zzchx.initialize();
        this.zzjfa = com_google_android_gms_internal_zzchx;
        com_google_android_gms_internal_zzchx = new zzcig(this);
        com_google_android_gms_internal_zzchx.initialize();
        this.zzjfb = com_google_android_gms_internal_zzchx;
        com_google_android_gms_internal_zzchx = new zzcih(this);
        com_google_android_gms_internal_zzchx.initialize();
        this.zzjez = com_google_android_gms_internal_zzchx;
        if (this.mContext.getApplicationContext() instanceof Application) {
            zzcjk zzawm = zzawm();
            if (zzawm.getContext().getApplicationContext() instanceof Application) {
                Application application = (Application) zzawm.getContext().getApplicationContext();
                if (zzawm.zzjgx == null) {
                    zzawm.zzjgx = new zzckb(zzawm);
                }
                application.unregisterActivityLifecycleCallbacks(zzawm.zzjgx);
                application.registerActivityLifecycleCallbacks(zzawm.zzjgx);
                zzawm.zzawy().zzazj().log("Registered activity lifecycle callback");
            }
        } else {
            zzawy().zzazf().log("Application context is not an Application");
        }
        this.zzjez.zzg(new zzcin(this));
    }

    private final int zza(FileChannel fileChannel) {
        int i = 0;
        zzawx().zzve();
        if (fileChannel == null || !fileChannel.isOpen()) {
            zzawy().zzazd().log("Bad chanel to read from");
        } else {
            ByteBuffer allocate = ByteBuffer.allocate(4);
            try {
                fileChannel.position(0);
                int read = fileChannel.read(allocate);
                if (read == 4) {
                    allocate.flip();
                    i = allocate.getInt();
                } else if (read != -1) {
                    zzawy().zzazf().zzj("Unexpected data length. Bytes read", Integer.valueOf(read));
                }
            } catch (IOException e) {
                zzawy().zzazd().zzj("Failed to read from channel", e);
            }
        }
        return i;
    }

    private final zzcgi zza(Context context, String str, String str2, boolean z, boolean z2) {
        Object charSequence;
        String str3 = "Unknown";
        String str4 = "Unknown";
        int i = Integer.MIN_VALUE;
        String str5 = "Unknown";
        PackageManager packageManager = context.getPackageManager();
        if (packageManager == null) {
            zzawy().zzazd().log("PackageManager is null, can not log app install information");
            return null;
        }
        try {
            str3 = packageManager.getInstallerPackageName(str);
        } catch (IllegalArgumentException e) {
            zzawy().zzazd().zzj("Error retrieving installer package name. appId", zzchm.zzjk(str));
        }
        if (str3 == null) {
            str3 = "manual_install";
        } else if ("com.android.vending".equals(str3)) {
            str3 = TtmlNode.ANONYMOUS_REGION_ID;
        }
        try {
            PackageInfo packageInfo = zzbhf.zzdb(context).getPackageInfo(str, 0);
            if (packageInfo != null) {
                CharSequence zzgt = zzbhf.zzdb(context).zzgt(str);
                if (TextUtils.isEmpty(zzgt)) {
                    String str6 = str5;
                } else {
                    charSequence = zzgt.toString();
                }
                try {
                    str4 = packageInfo.versionName;
                    i = packageInfo.versionCode;
                } catch (NameNotFoundException e2) {
                    zzawy().zzazd().zze("Error retrieving newly installed package info. appId, appName", zzchm.zzjk(str), charSequence);
                    return null;
                }
            }
            return new zzcgi(str, str2, str4, (long) i, str3, 11910, zzawu().zzaf(context, str), null, z, false, TtmlNode.ANONYMOUS_REGION_ID, 0, 0, 0, z2);
        } catch (NameNotFoundException e3) {
            charSequence = str5;
            zzawy().zzazd().zze("Error retrieving newly installed package info. appId, appName", zzchm.zzjk(str), charSequence);
            return null;
        }
    }

    private static void zza(zzcjk com_google_android_gms_internal_zzcjk) {
        if (com_google_android_gms_internal_zzcjk == null) {
            throw new IllegalStateException("Component not created");
        }
    }

    private static void zza(zzcjl com_google_android_gms_internal_zzcjl) {
        if (com_google_android_gms_internal_zzcjl == null) {
            throw new IllegalStateException("Component not created");
        } else if (!com_google_android_gms_internal_zzcjl.isInitialized()) {
            throw new IllegalStateException("Component not initialized");
        }
    }

    private final boolean zza(int i, FileChannel fileChannel) {
        zzawx().zzve();
        if (fileChannel == null || !fileChannel.isOpen()) {
            zzawy().zzazd().log("Bad chanel to read from");
            return false;
        }
        ByteBuffer allocate = ByteBuffer.allocate(4);
        allocate.putInt(i);
        allocate.flip();
        try {
            fileChannel.truncate(0);
            fileChannel.write(allocate);
            fileChannel.force(true);
            if (fileChannel.size() == 4) {
                return true;
            }
            zzawy().zzazd().zzj("Error writing to channel. Bytes written", Long.valueOf(fileChannel.size()));
            return true;
        } catch (IOException e) {
            zzawy().zzazd().zzj("Failed to write to channel", e);
            return false;
        }
    }

    private static boolean zza(zzcmb com_google_android_gms_internal_zzcmb, String str, Object obj) {
        if (TextUtils.isEmpty(str) || obj == null) {
            return false;
        }
        zzcmc[] com_google_android_gms_internal_zzcmcArr = com_google_android_gms_internal_zzcmb.zzjlh;
        int length = com_google_android_gms_internal_zzcmcArr.length;
        int i = 0;
        while (i < length) {
            zzcmc com_google_android_gms_internal_zzcmc = com_google_android_gms_internal_zzcmcArr[i];
            if (str.equals(com_google_android_gms_internal_zzcmc.name)) {
                return ((obj instanceof Long) && obj.equals(com_google_android_gms_internal_zzcmc.zzjll)) || (((obj instanceof String) && obj.equals(com_google_android_gms_internal_zzcmc.zzgcc)) || ((obj instanceof Double) && obj.equals(com_google_android_gms_internal_zzcmc.zzjjl)));
            } else {
                i++;
            }
        }
        return false;
    }

    private final boolean zza(String str, zzcha com_google_android_gms_internal_zzcha) {
        long round;
        Object string = com_google_android_gms_internal_zzcha.zzizt.getString("currency");
        if ("ecommerce_purchase".equals(com_google_android_gms_internal_zzcha.name)) {
            double doubleValue = com_google_android_gms_internal_zzcha.zzizt.getDouble("value").doubleValue() * 1000000.0d;
            if (doubleValue == 0.0d) {
                doubleValue = ((double) com_google_android_gms_internal_zzcha.zzizt.getLong("value").longValue()) * 1000000.0d;
            }
            if (doubleValue > 9.223372036854776E18d || doubleValue < -9.223372036854776E18d) {
                zzawy().zzazf().zze("Data lost. Currency value is too big. appId", zzchm.zzjk(str), Double.valueOf(doubleValue));
                return false;
            }
            round = Math.round(doubleValue);
        } else {
            round = com_google_android_gms_internal_zzcha.zzizt.getLong("value").longValue();
        }
        if (!TextUtils.isEmpty(string)) {
            String toUpperCase = string.toUpperCase(Locale.US);
            if (toUpperCase.matches("[A-Z]{3}")) {
                String valueOf = String.valueOf("_ltv_");
                toUpperCase = String.valueOf(toUpperCase);
                String concat = toUpperCase.length() != 0 ? valueOf.concat(toUpperCase) : new String(valueOf);
                zzclp zzag = zzaws().zzag(str, concat);
                if (zzag == null || !(zzag.mValue instanceof Long)) {
                    zzcjk zzaws = zzaws();
                    int zzb = this.zzjew.zzb(str, zzchc.zzjbh) - 1;
                    zzbq.zzgm(str);
                    zzaws.zzve();
                    zzaws.zzxf();
                    try {
                        zzaws.getWritableDatabase().execSQL("delete from user_attributes where app_id=? and name in (select name from user_attributes where app_id=? and name like '_ltv_%' order by set_timestamp desc limit ?,10);", new String[]{str, str, String.valueOf(zzb)});
                    } catch (SQLiteException e) {
                        zzaws.zzawy().zzazd().zze("Error pruning currencies. appId", zzchm.zzjk(str), e);
                    }
                    zzag = new zzclp(str, com_google_android_gms_internal_zzcha.zziyf, concat, this.zzata.currentTimeMillis(), Long.valueOf(round));
                } else {
                    zzag = new zzclp(str, com_google_android_gms_internal_zzcha.zziyf, concat, this.zzata.currentTimeMillis(), Long.valueOf(round + ((Long) zzag.mValue).longValue()));
                }
                if (!zzaws().zza(zzag)) {
                    zzawy().zzazd().zzd("Too many unique user properties are set. Ignoring user property. appId", zzchm.zzjk(str), zzawt().zzjj(zzag.mName), zzag.mValue);
                    zzawu().zza(str, 9, null, null, 0);
                }
            }
        }
        return true;
    }

    private final zzcma[] zza(String str, zzcmg[] com_google_android_gms_internal_zzcmgArr, zzcmb[] com_google_android_gms_internal_zzcmbArr) {
        zzbq.zzgm(str);
        return zzawl().zza(str, com_google_android_gms_internal_zzcmbArr, com_google_android_gms_internal_zzcmgArr);
    }

    static void zzawi() {
        throw new IllegalStateException("Unexpected call on client side");
    }

    private final void zzazw() {
        zzcho zzazh;
        zzawx().zzve();
        this.zzjfe.zzazw();
        this.zzjex.zzazw();
        this.zzjfn.zzazw();
        zzawy().zzazh().zzj("App measurement is starting up, version", Long.valueOf(11910));
        zzawy().zzazh().log("To enable debug logging run: adb shell setprop log.tag.FA VERBOSE");
        String appId = this.zzjfn.getAppId();
        if (zzawu().zzkj(appId)) {
            zzazh = zzawy().zzazh();
            appId = "Faster debug mode event logging enabled. To disable, run:\n  adb shell setprop debug.firebase.analytics.app .none.";
        } else {
            zzazh = zzawy().zzazh();
            String str = "To enable faster debug mode event logging run:\n  adb shell setprop debug.firebase.analytics.app ";
            appId = String.valueOf(appId);
            appId = appId.length() != 0 ? str.concat(appId) : new String(str);
        }
        zzazh.log(appId);
        zzawy().zzazi().log("Debug-level message logging enabled");
        if (this.zzjfz != this.zzjga) {
            zzawy().zzazd().zze("Not all components initialized", Integer.valueOf(this.zzjfz), Integer.valueOf(this.zzjga));
        }
        this.zzdtb = true;
    }

    private final void zzb(zzcgh com_google_android_gms_internal_zzcgh) {
        zzawx().zzve();
        if (TextUtils.isEmpty(com_google_android_gms_internal_zzcgh.getGmpAppId())) {
            zzb(com_google_android_gms_internal_zzcgh.getAppId(), 204, null, null, null);
            return;
        }
        String gmpAppId = com_google_android_gms_internal_zzcgh.getGmpAppId();
        String appInstanceId = com_google_android_gms_internal_zzcgh.getAppInstanceId();
        Builder builder = new Builder();
        Builder encodedAuthority = builder.scheme((String) zzchc.zzjah.get()).encodedAuthority((String) zzchc.zzjai.get());
        String str = "config/app/";
        String valueOf = String.valueOf(gmpAppId);
        encodedAuthority.path(valueOf.length() != 0 ? str.concat(valueOf) : new String(str)).appendQueryParameter("app_instance_id", appInstanceId).appendQueryParameter("platform", "android").appendQueryParameter("gmp_version", "11910");
        String uri = builder.build().toString();
        try {
            Map map;
            URL url = new URL(uri);
            zzawy().zzazj().zzj("Fetching remote configuration", com_google_android_gms_internal_zzcgh.getAppId());
            zzcly zzjs = zzawv().zzjs(com_google_android_gms_internal_zzcgh.getAppId());
            CharSequence zzjt = zzawv().zzjt(com_google_android_gms_internal_zzcgh.getAppId());
            if (zzjs == null || TextUtils.isEmpty(zzjt)) {
                map = null;
            } else {
                Map arrayMap = new ArrayMap();
                arrayMap.put("If-Modified-Since", zzjt);
                map = arrayMap;
            }
            this.zzjgd = true;
            zzcjk zzbab = zzbab();
            appInstanceId = com_google_android_gms_internal_zzcgh.getAppId();
            zzchs com_google_android_gms_internal_zzciq = new zzciq(this);
            zzbab.zzve();
            zzbab.zzxf();
            zzbq.checkNotNull(url);
            zzbq.checkNotNull(com_google_android_gms_internal_zzciq);
            zzbab.zzawx().zzh(new zzchu(zzbab, appInstanceId, url, null, map, com_google_android_gms_internal_zzciq));
        } catch (MalformedURLException e) {
            zzawy().zzazd().zze("Failed to parse config URL. Not fetching. appId", zzchm.zzjk(com_google_android_gms_internal_zzcgh.getAppId()), uri);
        }
    }

    private final zzchv zzbac() {
        if (this.zzjfo != null) {
            return this.zzjfo;
        }
        throw new IllegalStateException("Network broadcast receiver not created");
    }

    private final zzcll zzbad() {
        zza(this.zzjfp);
        return this.zzjfp;
    }

    private final boolean zzbae() {
        zzawx().zzve();
        try {
            this.zzjfw = new RandomAccessFile(new File(this.mContext.getFilesDir(), "google_app_measurement.db"), "rw").getChannel();
            this.zzjfv = this.zzjfw.tryLock();
            if (this.zzjfv != null) {
                zzawy().zzazj().log("Storage concurrent access okay");
                return true;
            }
            zzawy().zzazd().log("Storage concurrent data access panic");
            return false;
        } catch (FileNotFoundException e) {
            zzawy().zzazd().zzj("Failed to acquire storage lock", e);
        } catch (IOException e2) {
            zzawy().zzazd().zzj("Failed to access storage lock file", e2);
        }
    }

    private final long zzbag() {
        long currentTimeMillis = this.zzata.currentTimeMillis();
        zzcjk zzawz = zzawz();
        zzawz.zzxf();
        zzawz.zzve();
        long j = zzawz.zzjcv.get();
        if (j == 0) {
            j = 1 + ((long) zzawz.zzawu().zzbaz().nextInt(86400000));
            zzawz.zzjcv.set(j);
        }
        return ((((j + currentTimeMillis) / 1000) / 60) / 60) / 24;
    }

    private final boolean zzbai() {
        zzawx().zzve();
        zzxf();
        return zzaws().zzayk() || !TextUtils.isEmpty(zzaws().zzayf());
    }

    private final void zzbaj() {
        zzawx().zzve();
        zzxf();
        if (zzbam()) {
            long abs;
            if (this.zzjgc > 0) {
                abs = 3600000 - Math.abs(this.zzata.elapsedRealtime() - this.zzjgc);
                if (abs > 0) {
                    zzawy().zzazj().zzj("Upload has been suspended. Will update scheduling later in approximately ms", Long.valueOf(abs));
                    zzbac().unregister();
                    zzbad().cancel();
                    return;
                }
                this.zzjgc = 0;
            }
            if (zzazv() && zzbai()) {
                long currentTimeMillis = this.zzata.currentTimeMillis();
                long max = Math.max(0, ((Long) zzchc.zzjbd.get()).longValue());
                Object obj = (zzaws().zzayl() || zzaws().zzayg()) ? 1 : null;
                if (obj != null) {
                    CharSequence zzayd = this.zzjew.zzayd();
                    abs = (TextUtils.isEmpty(zzayd) || ".none.".equals(zzayd)) ? Math.max(0, ((Long) zzchc.zzjax.get()).longValue()) : Math.max(0, ((Long) zzchc.zzjay.get()).longValue());
                } else {
                    abs = Math.max(0, ((Long) zzchc.zzjaw.get()).longValue());
                }
                long j = zzawz().zzjcr.get();
                long j2 = zzawz().zzjcs.get();
                long max2 = Math.max(zzaws().zzayi(), zzaws().zzayj());
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
                    if (!zzawu().zzf(j, abs)) {
                        currentTimeMillis = j + abs;
                    }
                    if (j2 != 0 && j2 >= max2) {
                        for (int i = 0; i < Math.min(20, Math.max(0, ((Integer) zzchc.zzjbf.get()).intValue())); i++) {
                            currentTimeMillis += (1 << i) * Math.max(0, ((Long) zzchc.zzjbe.get()).longValue());
                            if (currentTimeMillis > j2) {
                                break;
                            }
                        }
                        currentTimeMillis = 0;
                    }
                }
                if (currentTimeMillis == 0) {
                    zzawy().zzazj().log("Next upload time is 0");
                    zzbac().unregister();
                    zzbad().cancel();
                    return;
                } else if (zzbab().zzzs()) {
                    long j3 = zzawz().zzjct.get();
                    abs = Math.max(0, ((Long) zzchc.zzjau.get()).longValue());
                    abs = !zzawu().zzf(j3, abs) ? Math.max(currentTimeMillis, abs + j3) : currentTimeMillis;
                    zzbac().unregister();
                    abs -= this.zzata.currentTimeMillis();
                    if (abs <= 0) {
                        abs = Math.max(0, ((Long) zzchc.zzjaz.get()).longValue());
                        zzawz().zzjcr.set(this.zzata.currentTimeMillis());
                    }
                    zzawy().zzazj().zzj("Upload scheduled in approximately ms", Long.valueOf(abs));
                    zzbad().zzs(abs);
                    return;
                } else {
                    zzawy().zzazj().log("No network");
                    zzbac().zzzp();
                    zzbad().cancel();
                    return;
                }
            }
            zzawy().zzazj().log("Nothing to upload or uploading impossible");
            zzbac().unregister();
            zzbad().cancel();
        }
    }

    private final boolean zzbam() {
        zzawx().zzve();
        zzxf();
        return this.zzjfs;
    }

    private final void zzban() {
        zzawx().zzve();
        if (this.zzjgd || this.zzjge || this.zzjgf) {
            zzawy().zzazj().zzd("Not stopping services. fetch, network, upload", Boolean.valueOf(this.zzjgd), Boolean.valueOf(this.zzjge), Boolean.valueOf(this.zzjgf));
            return;
        }
        zzawy().zzazj().log("Stopping uploading service(s)");
        if (this.zzjfy != null) {
            for (Runnable run : this.zzjfy) {
                run.run();
            }
            this.zzjfy.clear();
        }
    }

    private final void zzc(zzcha com_google_android_gms_internal_zzcha, zzcgi com_google_android_gms_internal_zzcgi) {
        zzbq.checkNotNull(com_google_android_gms_internal_zzcgi);
        zzbq.zzgm(com_google_android_gms_internal_zzcgi.packageName);
        long nanoTime = System.nanoTime();
        zzawx().zzve();
        zzxf();
        String str = com_google_android_gms_internal_zzcgi.packageName;
        zzawu();
        if (!zzclq.zzd(com_google_android_gms_internal_zzcha, com_google_android_gms_internal_zzcgi)) {
            return;
        }
        if (!com_google_android_gms_internal_zzcgi.zzixx) {
            zzg(com_google_android_gms_internal_zzcgi);
        } else if (zzawv().zzan(str, com_google_android_gms_internal_zzcha.name)) {
            zzawy().zzazf().zze("Dropping blacklisted event. appId", zzchm.zzjk(str), zzawt().zzjh(com_google_android_gms_internal_zzcha.name));
            Object obj = (zzawu().zzkl(str) || zzawu().zzkm(str)) ? 1 : null;
            if (obj == null && !"_err".equals(com_google_android_gms_internal_zzcha.name)) {
                zzawu().zza(str, 11, "_ev", com_google_android_gms_internal_zzcha.name, 0);
            }
            if (obj != null) {
                zzcgh zzjb = zzaws().zzjb(str);
                if (zzjb != null) {
                    if (Math.abs(this.zzata.currentTimeMillis() - Math.max(zzjb.zzaxn(), zzjb.zzaxm())) > ((Long) zzchc.zzjbc.get()).longValue()) {
                        zzawy().zzazi().log("Fetching config for blacklisted app");
                        zzb(zzjb);
                    }
                }
            }
        } else {
            if (zzawy().zzae(2)) {
                zzawy().zzazj().zzj("Logging event", zzawt().zzb(com_google_android_gms_internal_zzcha));
            }
            zzaws().beginTransaction();
            zzg(com_google_android_gms_internal_zzcgi);
            if (("_iap".equals(com_google_android_gms_internal_zzcha.name) || "ecommerce_purchase".equals(com_google_android_gms_internal_zzcha.name)) && !zza(str, com_google_android_gms_internal_zzcha)) {
                zzaws().setTransactionSuccessful();
                zzaws().endTransaction();
                return;
            }
            zzcme com_google_android_gms_internal_zzcme;
            try {
                boolean zzjz = zzclq.zzjz(com_google_android_gms_internal_zzcha.name);
                boolean equals = "_err".equals(com_google_android_gms_internal_zzcha.name);
                zzcgp zza = zzaws().zza(zzbag(), str, true, zzjz, false, equals, false);
                long intValue = zza.zziyy - ((long) ((Integer) zzchc.zzjan.get()).intValue());
                if (intValue > 0) {
                    if (intValue % 1000 == 1) {
                        zzawy().zzazd().zze("Data loss. Too many events logged. appId, count", zzchm.zzjk(str), Long.valueOf(zza.zziyy));
                    }
                    zzaws().setTransactionSuccessful();
                    zzaws().endTransaction();
                    return;
                }
                zzcgw zzbb;
                zzcgv com_google_android_gms_internal_zzcgv;
                boolean z;
                if (zzjz) {
                    intValue = zza.zziyx - ((long) ((Integer) zzchc.zzjap.get()).intValue());
                    if (intValue > 0) {
                        if (intValue % 1000 == 1) {
                            zzawy().zzazd().zze("Data loss. Too many public events logged. appId, count", zzchm.zzjk(str), Long.valueOf(zza.zziyx));
                        }
                        zzawu().zza(str, 16, "_ev", com_google_android_gms_internal_zzcha.name, 0);
                        zzaws().setTransactionSuccessful();
                        zzaws().endTransaction();
                        return;
                    }
                }
                if (equals) {
                    intValue = zza.zziza - ((long) Math.max(0, Math.min(1000000, this.zzjew.zzb(com_google_android_gms_internal_zzcgi.packageName, zzchc.zzjao))));
                    if (intValue > 0) {
                        if (intValue == 1) {
                            zzawy().zzazd().zze("Too many error events logged. appId, count", zzchm.zzjk(str), Long.valueOf(zza.zziza));
                        }
                        zzaws().setTransactionSuccessful();
                        zzaws().endTransaction();
                        return;
                    }
                }
                Bundle zzayx = com_google_android_gms_internal_zzcha.zzizt.zzayx();
                zzawu().zza(zzayx, "_o", com_google_android_gms_internal_zzcha.zziyf);
                if (zzawu().zzkj(str)) {
                    zzawu().zza(zzayx, "_dbg", Long.valueOf(1));
                    zzawu().zza(zzayx, "_r", Long.valueOf(1));
                }
                long zzjc = zzaws().zzjc(str);
                if (zzjc > 0) {
                    zzawy().zzazf().zze("Data lost. Too many events stored on disk, deleted. appId", zzchm.zzjk(str), Long.valueOf(zzjc));
                }
                zzcgv com_google_android_gms_internal_zzcgv2 = new zzcgv(this, com_google_android_gms_internal_zzcha.zziyf, str, com_google_android_gms_internal_zzcha.name, com_google_android_gms_internal_zzcha.zzizu, 0, zzayx);
                zzcgw zzae = zzaws().zzae(str, com_google_android_gms_internal_zzcgv2.mName);
                if (zzae != null) {
                    com_google_android_gms_internal_zzcgv2 = com_google_android_gms_internal_zzcgv2.zza(this, zzae.zzizm);
                    zzbb = zzae.zzbb(com_google_android_gms_internal_zzcgv2.zzfij);
                    com_google_android_gms_internal_zzcgv = com_google_android_gms_internal_zzcgv2;
                } else if (zzaws().zzjf(str) < 500 || !zzjz) {
                    zzbb = new zzcgw(str, com_google_android_gms_internal_zzcgv2.mName, 0, 0, com_google_android_gms_internal_zzcgv2.zzfij, 0, null, null, null);
                    com_google_android_gms_internal_zzcgv = com_google_android_gms_internal_zzcgv2;
                } else {
                    zzawy().zzazd().zzd("Too many event names used, ignoring event. appId, name, supported count", zzchm.zzjk(str), zzawt().zzjh(com_google_android_gms_internal_zzcgv2.mName), Integer.valueOf(500));
                    zzawu().zza(str, 8, null, null, 0);
                    zzaws().endTransaction();
                    return;
                }
                zzaws().zza(zzbb);
                zzawx().zzve();
                zzxf();
                zzbq.checkNotNull(com_google_android_gms_internal_zzcgv);
                zzbq.checkNotNull(com_google_android_gms_internal_zzcgi);
                zzbq.zzgm(com_google_android_gms_internal_zzcgv.mAppId);
                zzbq.checkArgument(com_google_android_gms_internal_zzcgv.mAppId.equals(com_google_android_gms_internal_zzcgi.packageName));
                com_google_android_gms_internal_zzcme = new zzcme();
                com_google_android_gms_internal_zzcme.zzjlo = Integer.valueOf(1);
                com_google_android_gms_internal_zzcme.zzjlw = "android";
                com_google_android_gms_internal_zzcme.zzcn = com_google_android_gms_internal_zzcgi.packageName;
                com_google_android_gms_internal_zzcme.zzixt = com_google_android_gms_internal_zzcgi.zzixt;
                com_google_android_gms_internal_zzcme.zzifm = com_google_android_gms_internal_zzcgi.zzifm;
                com_google_android_gms_internal_zzcme.zzjmj = com_google_android_gms_internal_zzcgi.zzixz == -2147483648L ? null : Integer.valueOf((int) com_google_android_gms_internal_zzcgi.zzixz);
                com_google_android_gms_internal_zzcme.zzjma = Long.valueOf(com_google_android_gms_internal_zzcgi.zzixu);
                com_google_android_gms_internal_zzcme.zzixs = com_google_android_gms_internal_zzcgi.zzixs;
                com_google_android_gms_internal_zzcme.zzjmf = com_google_android_gms_internal_zzcgi.zzixv == 0 ? null : Long.valueOf(com_google_android_gms_internal_zzcgi.zzixv);
                Pair zzjm = zzawz().zzjm(com_google_android_gms_internal_zzcgi.packageName);
                if (zzjm == null || TextUtils.isEmpty((CharSequence) zzjm.first)) {
                    if (!zzawo().zzdw(this.mContext)) {
                        String string = Secure.getString(this.mContext.getContentResolver(), "android_id");
                        if (string == null) {
                            zzawy().zzazf().zzj("null secure ID. appId", zzchm.zzjk(com_google_android_gms_internal_zzcme.zzcn));
                            string = "null";
                        } else if (string.isEmpty()) {
                            zzawy().zzazf().zzj("empty secure ID. appId", zzchm.zzjk(com_google_android_gms_internal_zzcme.zzcn));
                        }
                        com_google_android_gms_internal_zzcme.zzjmm = string;
                    }
                } else if (com_google_android_gms_internal_zzcgi.zziye) {
                    com_google_android_gms_internal_zzcme.zzjmc = (String) zzjm.first;
                    com_google_android_gms_internal_zzcme.zzjmd = (Boolean) zzjm.second;
                }
                zzawo().zzxf();
                com_google_android_gms_internal_zzcme.zzjlx = Build.MODEL;
                zzawo().zzxf();
                com_google_android_gms_internal_zzcme.zzdb = VERSION.RELEASE;
                com_google_android_gms_internal_zzcme.zzjlz = Integer.valueOf((int) zzawo().zzayu());
                com_google_android_gms_internal_zzcme.zzjly = zzawo().zzayv();
                com_google_android_gms_internal_zzcme.zzjmb = null;
                com_google_android_gms_internal_zzcme.zzjlr = null;
                com_google_android_gms_internal_zzcme.zzjls = null;
                com_google_android_gms_internal_zzcme.zzjlt = null;
                com_google_android_gms_internal_zzcme.zzfkk = Long.valueOf(com_google_android_gms_internal_zzcgi.zziyb);
                if (isEnabled() && zzcgn.zzaye()) {
                    zzawn();
                    com_google_android_gms_internal_zzcme.zzjmo = null;
                }
                zzcgh zzjb2 = zzaws().zzjb(com_google_android_gms_internal_zzcgi.packageName);
                if (zzjb2 == null) {
                    zzjb2 = new zzcgh(this, com_google_android_gms_internal_zzcgi.packageName);
                    zzjb2.zzir(zzawn().zzayz());
                    zzjb2.zziu(com_google_android_gms_internal_zzcgi.zziya);
                    zzjb2.zzis(com_google_android_gms_internal_zzcgi.zzixs);
                    zzjb2.zzit(zzawz().zzjn(com_google_android_gms_internal_zzcgi.packageName));
                    zzjb2.zzaq(0);
                    zzjb2.zzal(0);
                    zzjb2.zzam(0);
                    zzjb2.setAppVersion(com_google_android_gms_internal_zzcgi.zzifm);
                    zzjb2.zzan(com_google_android_gms_internal_zzcgi.zzixz);
                    zzjb2.zziv(com_google_android_gms_internal_zzcgi.zzixt);
                    zzjb2.zzao(com_google_android_gms_internal_zzcgi.zzixu);
                    zzjb2.zzap(com_google_android_gms_internal_zzcgi.zzixv);
                    zzjb2.setMeasurementEnabled(com_google_android_gms_internal_zzcgi.zzixx);
                    zzjb2.zzaz(com_google_android_gms_internal_zzcgi.zziyb);
                    zzaws().zza(zzjb2);
                }
                com_google_android_gms_internal_zzcme.zzjme = zzjb2.getAppInstanceId();
                com_google_android_gms_internal_zzcme.zziya = zzjb2.zzaxd();
                List zzja = zzaws().zzja(com_google_android_gms_internal_zzcgi.packageName);
                com_google_android_gms_internal_zzcme.zzjlq = new zzcmg[zzja.size()];
                for (int i = 0; i < zzja.size(); i++) {
                    zzcmg com_google_android_gms_internal_zzcmg = new zzcmg();
                    com_google_android_gms_internal_zzcme.zzjlq[i] = com_google_android_gms_internal_zzcmg;
                    com_google_android_gms_internal_zzcmg.name = ((zzclp) zzja.get(i)).mName;
                    com_google_android_gms_internal_zzcmg.zzjms = Long.valueOf(((zzclp) zzja.get(i)).zzjjm);
                    zzawu().zza(com_google_android_gms_internal_zzcmg, ((zzclp) zzja.get(i)).mValue);
                }
                long zza2 = zzaws().zza(com_google_android_gms_internal_zzcme);
                zzcgo zzaws = zzaws();
                if (com_google_android_gms_internal_zzcgv.zzizj != null) {
                    Iterator it = com_google_android_gms_internal_zzcgv.zzizj.iterator();
                    while (it.hasNext()) {
                        if ("_r".equals((String) it.next())) {
                            z = true;
                            break;
                        }
                    }
                    z = zzawv().zzao(com_google_android_gms_internal_zzcgv.mAppId, com_google_android_gms_internal_zzcgv.mName);
                    zzcgp zza3 = zzaws().zza(zzbag(), com_google_android_gms_internal_zzcgv.mAppId, false, false, false, false, false);
                    if (z && zza3.zzizb < ((long) this.zzjew.zzix(com_google_android_gms_internal_zzcgv.mAppId))) {
                        z = true;
                        if (zzaws.zza(com_google_android_gms_internal_zzcgv, zza2, z)) {
                            this.zzjgc = 0;
                        }
                        zzaws().setTransactionSuccessful();
                        if (zzawy().zzae(2)) {
                            zzawy().zzazj().zzj("Event recorded", zzawt().zza(com_google_android_gms_internal_zzcgv));
                        }
                        zzaws().endTransaction();
                        zzbaj();
                        zzawy().zzazj().zzj("Background event processing time, ms", Long.valueOf(((System.nanoTime() - nanoTime) + 500000) / C.MICROS_PER_SECOND));
                    }
                }
                z = false;
                if (zzaws.zza(com_google_android_gms_internal_zzcgv, zza2, z)) {
                    this.zzjgc = 0;
                }
                zzaws().setTransactionSuccessful();
                if (zzawy().zzae(2)) {
                    zzawy().zzazj().zzj("Event recorded", zzawt().zza(com_google_android_gms_internal_zzcgv));
                }
                zzaws().endTransaction();
                zzbaj();
                zzawy().zzazj().zzj("Background event processing time, ms", Long.valueOf(((System.nanoTime() - nanoTime) + 500000) / C.MICROS_PER_SECOND));
            } catch (IOException e) {
                zzawy().zzazd().zze("Data loss. Failed to insert raw event metadata. appId", zzchm.zzjk(com_google_android_gms_internal_zzcme.zzcn), e);
            } catch (Throwable th) {
                zzaws().endTransaction();
            }
        }
    }

    public static zzcim zzdx(Context context) {
        zzbq.checkNotNull(context);
        zzbq.checkNotNull(context.getApplicationContext());
        if (zzjev == null) {
            synchronized (zzcim.class) {
                if (zzjev == null) {
                    zzjev = new zzcim(new zzcjm(context));
                }
            }
        }
        return zzjev;
    }

    private final void zzg(zzcgi com_google_android_gms_internal_zzcgi) {
        Object obj = 1;
        zzawx().zzve();
        zzxf();
        zzbq.checkNotNull(com_google_android_gms_internal_zzcgi);
        zzbq.zzgm(com_google_android_gms_internal_zzcgi.packageName);
        zzcgh zzjb = zzaws().zzjb(com_google_android_gms_internal_zzcgi.packageName);
        String zzjn = zzawz().zzjn(com_google_android_gms_internal_zzcgi.packageName);
        Object obj2 = null;
        if (zzjb == null) {
            zzcgh com_google_android_gms_internal_zzcgh = new zzcgh(this, com_google_android_gms_internal_zzcgi.packageName);
            com_google_android_gms_internal_zzcgh.zzir(zzawn().zzayz());
            com_google_android_gms_internal_zzcgh.zzit(zzjn);
            zzjb = com_google_android_gms_internal_zzcgh;
            obj2 = 1;
        } else if (!zzjn.equals(zzjb.zzaxc())) {
            zzjb.zzit(zzjn);
            zzjb.zzir(zzawn().zzayz());
            int i = 1;
        }
        if (!(TextUtils.isEmpty(com_google_android_gms_internal_zzcgi.zzixs) || com_google_android_gms_internal_zzcgi.zzixs.equals(zzjb.getGmpAppId()))) {
            zzjb.zzis(com_google_android_gms_internal_zzcgi.zzixs);
            obj2 = 1;
        }
        if (!(TextUtils.isEmpty(com_google_android_gms_internal_zzcgi.zziya) || com_google_android_gms_internal_zzcgi.zziya.equals(zzjb.zzaxd()))) {
            zzjb.zziu(com_google_android_gms_internal_zzcgi.zziya);
            obj2 = 1;
        }
        if (!(com_google_android_gms_internal_zzcgi.zzixu == 0 || com_google_android_gms_internal_zzcgi.zzixu == zzjb.zzaxi())) {
            zzjb.zzao(com_google_android_gms_internal_zzcgi.zzixu);
            obj2 = 1;
        }
        if (!(TextUtils.isEmpty(com_google_android_gms_internal_zzcgi.zzifm) || com_google_android_gms_internal_zzcgi.zzifm.equals(zzjb.zzvj()))) {
            zzjb.setAppVersion(com_google_android_gms_internal_zzcgi.zzifm);
            obj2 = 1;
        }
        if (com_google_android_gms_internal_zzcgi.zzixz != zzjb.zzaxg()) {
            zzjb.zzan(com_google_android_gms_internal_zzcgi.zzixz);
            obj2 = 1;
        }
        if (!(com_google_android_gms_internal_zzcgi.zzixt == null || com_google_android_gms_internal_zzcgi.zzixt.equals(zzjb.zzaxh()))) {
            zzjb.zziv(com_google_android_gms_internal_zzcgi.zzixt);
            obj2 = 1;
        }
        if (com_google_android_gms_internal_zzcgi.zzixv != zzjb.zzaxj()) {
            zzjb.zzap(com_google_android_gms_internal_zzcgi.zzixv);
            obj2 = 1;
        }
        if (com_google_android_gms_internal_zzcgi.zzixx != zzjb.zzaxk()) {
            zzjb.setMeasurementEnabled(com_google_android_gms_internal_zzcgi.zzixx);
            obj2 = 1;
        }
        if (!(TextUtils.isEmpty(com_google_android_gms_internal_zzcgi.zzixw) || com_google_android_gms_internal_zzcgi.zzixw.equals(zzjb.zzaxv()))) {
            zzjb.zziw(com_google_android_gms_internal_zzcgi.zzixw);
            obj2 = 1;
        }
        if (com_google_android_gms_internal_zzcgi.zziyb != zzjb.zzaxx()) {
            zzjb.zzaz(com_google_android_gms_internal_zzcgi.zziyb);
            obj2 = 1;
        }
        if (com_google_android_gms_internal_zzcgi.zziye != zzjb.zzaxy()) {
            zzjb.zzbl(com_google_android_gms_internal_zzcgi.zziye);
        } else {
            obj = obj2;
        }
        if (obj != null) {
            zzaws().zza(zzjb);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final boolean zzg(java.lang.String r31, long r32) {
        /*
        r30 = this;
        r2 = r30.zzaws();
        r2.beginTransaction();
        r21 = new com.google.android.gms.internal.zzcim$zza;	 Catch:{ all -> 0x01cb }
        r2 = 0;
        r0 = r21;
        r1 = r30;
        r0.<init>();	 Catch:{ all -> 0x01cb }
        r14 = r30.zzaws();	 Catch:{ all -> 0x01cb }
        r4 = 0;
        r0 = r30;
        r0 = r0.zzjgb;	 Catch:{ all -> 0x01cb }
        r16 = r0;
        com.google.android.gms.common.internal.zzbq.checkNotNull(r21);	 Catch:{ all -> 0x01cb }
        r14.zzve();	 Catch:{ all -> 0x01cb }
        r14.zzxf();	 Catch:{ all -> 0x01cb }
        r3 = 0;
        r2 = r14.getWritableDatabase();	 Catch:{ SQLiteException -> 0x0afa }
        r5 = 0;
        r5 = android.text.TextUtils.isEmpty(r5);	 Catch:{ SQLiteException -> 0x0afa }
        if (r5 == 0) goto L_0x01d4;
    L_0x0031:
        r6 = -1;
        r5 = (r16 > r6 ? 1 : (r16 == r6 ? 0 : -1));
        if (r5 == 0) goto L_0x0166;
    L_0x0037:
        r5 = 2;
        r5 = new java.lang.String[r5];	 Catch:{ SQLiteException -> 0x0afa }
        r6 = 0;
        r7 = java.lang.String.valueOf(r16);	 Catch:{ SQLiteException -> 0x0afa }
        r5[r6] = r7;	 Catch:{ SQLiteException -> 0x0afa }
        r6 = 1;
        r7 = java.lang.String.valueOf(r32);	 Catch:{ SQLiteException -> 0x0afa }
        r5[r6] = r7;	 Catch:{ SQLiteException -> 0x0afa }
        r6 = r5;
    L_0x0049:
        r8 = -1;
        r5 = (r16 > r8 ? 1 : (r16 == r8 ? 0 : -1));
        if (r5 == 0) goto L_0x0173;
    L_0x004f:
        r5 = "rowid <= ? and ";
    L_0x0052:
        r7 = java.lang.String.valueOf(r5);	 Catch:{ SQLiteException -> 0x0afa }
        r7 = r7.length();	 Catch:{ SQLiteException -> 0x0afa }
        r7 = r7 + 148;
        r8 = new java.lang.StringBuilder;	 Catch:{ SQLiteException -> 0x0afa }
        r8.<init>(r7);	 Catch:{ SQLiteException -> 0x0afa }
        r7 = "select app_id, metadata_fingerprint from raw_events where ";
        r7 = r8.append(r7);	 Catch:{ SQLiteException -> 0x0afa }
        r5 = r7.append(r5);	 Catch:{ SQLiteException -> 0x0afa }
        r7 = "app_id in (select app_id from apps where config_fetched_time >= ?) order by rowid limit 1;";
        r5 = r5.append(r7);	 Catch:{ SQLiteException -> 0x0afa }
        r5 = r5.toString();	 Catch:{ SQLiteException -> 0x0afa }
        r3 = r2.rawQuery(r5, r6);	 Catch:{ SQLiteException -> 0x0afa }
        r5 = r3.moveToFirst();	 Catch:{ SQLiteException -> 0x0afa }
        if (r5 != 0) goto L_0x0178;
    L_0x0081:
        if (r3 == 0) goto L_0x0086;
    L_0x0083:
        r3.close();	 Catch:{ all -> 0x01cb }
    L_0x0086:
        r0 = r21;
        r2 = r0.zzapa;	 Catch:{ all -> 0x01cb }
        if (r2 == 0) goto L_0x0096;
    L_0x008c:
        r0 = r21;
        r2 = r0.zzapa;	 Catch:{ all -> 0x01cb }
        r2 = r2.isEmpty();	 Catch:{ all -> 0x01cb }
        if (r2 == 0) goto L_0x0378;
    L_0x0096:
        r2 = 1;
    L_0x0097:
        if (r2 != 0) goto L_0x0ae5;
    L_0x0099:
        r17 = 0;
        r0 = r21;
        r0 = r0.zzjgi;	 Catch:{ all -> 0x01cb }
        r22 = r0;
        r0 = r21;
        r2 = r0.zzapa;	 Catch:{ all -> 0x01cb }
        r2 = r2.size();	 Catch:{ all -> 0x01cb }
        r2 = new com.google.android.gms.internal.zzcmb[r2];	 Catch:{ all -> 0x01cb }
        r0 = r22;
        r0.zzjlp = r2;	 Catch:{ all -> 0x01cb }
        r12 = 0;
        r2 = 0;
        r13 = r2;
    L_0x00b2:
        r0 = r21;
        r2 = r0.zzapa;	 Catch:{ all -> 0x01cb }
        r2 = r2.size();	 Catch:{ all -> 0x01cb }
        if (r13 >= r2) goto L_0x063d;
    L_0x00bc:
        r3 = r30.zzawv();	 Catch:{ all -> 0x01cb }
        r0 = r21;
        r2 = r0.zzjgi;	 Catch:{ all -> 0x01cb }
        r4 = r2.zzcn;	 Catch:{ all -> 0x01cb }
        r0 = r21;
        r2 = r0.zzapa;	 Catch:{ all -> 0x01cb }
        r2 = r2.get(r13);	 Catch:{ all -> 0x01cb }
        r2 = (com.google.android.gms.internal.zzcmb) r2;	 Catch:{ all -> 0x01cb }
        r2 = r2.name;	 Catch:{ all -> 0x01cb }
        r2 = r3.zzan(r4, r2);	 Catch:{ all -> 0x01cb }
        if (r2 == 0) goto L_0x037e;
    L_0x00d8:
        r2 = r30.zzawy();	 Catch:{ all -> 0x01cb }
        r3 = r2.zzazf();	 Catch:{ all -> 0x01cb }
        r4 = "Dropping blacklisted raw event. appId";
        r0 = r21;
        r2 = r0.zzjgi;	 Catch:{ all -> 0x01cb }
        r2 = r2.zzcn;	 Catch:{ all -> 0x01cb }
        r5 = com.google.android.gms.internal.zzchm.zzjk(r2);	 Catch:{ all -> 0x01cb }
        r6 = r30.zzawt();	 Catch:{ all -> 0x01cb }
        r0 = r21;
        r2 = r0.zzapa;	 Catch:{ all -> 0x01cb }
        r2 = r2.get(r13);	 Catch:{ all -> 0x01cb }
        r2 = (com.google.android.gms.internal.zzcmb) r2;	 Catch:{ all -> 0x01cb }
        r2 = r2.name;	 Catch:{ all -> 0x01cb }
        r2 = r6.zzjh(r2);	 Catch:{ all -> 0x01cb }
        r3.zze(r4, r5, r2);	 Catch:{ all -> 0x01cb }
        r2 = r30.zzawu();	 Catch:{ all -> 0x01cb }
        r0 = r21;
        r3 = r0.zzjgi;	 Catch:{ all -> 0x01cb }
        r3 = r3.zzcn;	 Catch:{ all -> 0x01cb }
        r2 = r2.zzkl(r3);	 Catch:{ all -> 0x01cb }
        if (r2 != 0) goto L_0x0124;
    L_0x0114:
        r2 = r30.zzawu();	 Catch:{ all -> 0x01cb }
        r0 = r21;
        r3 = r0.zzjgi;	 Catch:{ all -> 0x01cb }
        r3 = r3.zzcn;	 Catch:{ all -> 0x01cb }
        r2 = r2.zzkm(r3);	 Catch:{ all -> 0x01cb }
        if (r2 == 0) goto L_0x037b;
    L_0x0124:
        r2 = 1;
    L_0x0125:
        if (r2 != 0) goto L_0x0b16;
    L_0x0127:
        r3 = "_err";
        r0 = r21;
        r2 = r0.zzapa;	 Catch:{ all -> 0x01cb }
        r2 = r2.get(r13);	 Catch:{ all -> 0x01cb }
        r2 = (com.google.android.gms.internal.zzcmb) r2;	 Catch:{ all -> 0x01cb }
        r2 = r2.name;	 Catch:{ all -> 0x01cb }
        r2 = r3.equals(r2);	 Catch:{ all -> 0x01cb }
        if (r2 != 0) goto L_0x0b16;
    L_0x013c:
        r2 = r30.zzawu();	 Catch:{ all -> 0x01cb }
        r0 = r21;
        r3 = r0.zzjgi;	 Catch:{ all -> 0x01cb }
        r3 = r3.zzcn;	 Catch:{ all -> 0x01cb }
        r4 = 11;
        r5 = "_ev";
        r0 = r21;
        r6 = r0.zzapa;	 Catch:{ all -> 0x01cb }
        r6 = r6.get(r13);	 Catch:{ all -> 0x01cb }
        r6 = (com.google.android.gms.internal.zzcmb) r6;	 Catch:{ all -> 0x01cb }
        r6 = r6.name;	 Catch:{ all -> 0x01cb }
        r7 = 0;
        r2.zza(r3, r4, r5, r6, r7);	 Catch:{ all -> 0x01cb }
        r2 = r12;
        r3 = r17;
    L_0x015e:
        r4 = r13 + 1;
        r13 = r4;
        r12 = r2;
        r17 = r3;
        goto L_0x00b2;
    L_0x0166:
        r5 = 1;
        r5 = new java.lang.String[r5];	 Catch:{ SQLiteException -> 0x0afa }
        r6 = 0;
        r7 = java.lang.String.valueOf(r32);	 Catch:{ SQLiteException -> 0x0afa }
        r5[r6] = r7;	 Catch:{ SQLiteException -> 0x0afa }
        r6 = r5;
        goto L_0x0049;
    L_0x0173:
        r5 = "";
        goto L_0x0052;
    L_0x0178:
        r5 = 0;
        r4 = r3.getString(r5);	 Catch:{ SQLiteException -> 0x0afa }
        r5 = 1;
        r5 = r3.getString(r5);	 Catch:{ SQLiteException -> 0x0afa }
        r3.close();	 Catch:{ SQLiteException -> 0x0afa }
        r13 = r5;
        r11 = r3;
        r12 = r4;
    L_0x0188:
        r3 = "raw_events_metadata";
        r4 = 1;
        r4 = new java.lang.String[r4];	 Catch:{ SQLiteException -> 0x02fc, all -> 0x0af6 }
        r5 = 0;
        r6 = "metadata";
        r4[r5] = r6;	 Catch:{ SQLiteException -> 0x02fc, all -> 0x0af6 }
        r5 = "app_id = ? and metadata_fingerprint = ?";
        r6 = 2;
        r6 = new java.lang.String[r6];	 Catch:{ SQLiteException -> 0x02fc, all -> 0x0af6 }
        r7 = 0;
        r6[r7] = r12;	 Catch:{ SQLiteException -> 0x02fc, all -> 0x0af6 }
        r7 = 1;
        r6[r7] = r13;	 Catch:{ SQLiteException -> 0x02fc, all -> 0x0af6 }
        r7 = 0;
        r8 = 0;
        r9 = "rowid";
        r10 = "2";
        r11 = r2.query(r3, r4, r5, r6, r7, r8, r9, r10);	 Catch:{ SQLiteException -> 0x02fc, all -> 0x0af6 }
        r3 = r11.moveToFirst();	 Catch:{ SQLiteException -> 0x02fc, all -> 0x0af6 }
        if (r3 != 0) goto L_0x0242;
    L_0x01b2:
        r2 = r14.zzawy();	 Catch:{ SQLiteException -> 0x02fc, all -> 0x0af6 }
        r2 = r2.zzazd();	 Catch:{ SQLiteException -> 0x02fc, all -> 0x0af6 }
        r3 = "Raw event metadata record is missing. appId";
        r4 = com.google.android.gms.internal.zzchm.zzjk(r12);	 Catch:{ SQLiteException -> 0x02fc, all -> 0x0af6 }
        r2.zzj(r3, r4);	 Catch:{ SQLiteException -> 0x02fc, all -> 0x0af6 }
        if (r11 == 0) goto L_0x0086;
    L_0x01c6:
        r11.close();	 Catch:{ all -> 0x01cb }
        goto L_0x0086;
    L_0x01cb:
        r2 = move-exception;
        r3 = r30.zzaws();
        r3.endTransaction();
        throw r2;
    L_0x01d4:
        r6 = -1;
        r5 = (r16 > r6 ? 1 : (r16 == r6 ? 0 : -1));
        if (r5 == 0) goto L_0x0228;
    L_0x01da:
        r5 = 2;
        r5 = new java.lang.String[r5];	 Catch:{ SQLiteException -> 0x0afa }
        r6 = 0;
        r7 = 0;
        r5[r6] = r7;	 Catch:{ SQLiteException -> 0x0afa }
        r6 = 1;
        r7 = java.lang.String.valueOf(r16);	 Catch:{ SQLiteException -> 0x0afa }
        r5[r6] = r7;	 Catch:{ SQLiteException -> 0x0afa }
        r6 = r5;
    L_0x01e9:
        r8 = -1;
        r5 = (r16 > r8 ? 1 : (r16 == r8 ? 0 : -1));
        if (r5 == 0) goto L_0x0231;
    L_0x01ef:
        r5 = " and rowid <= ?";
    L_0x01f2:
        r7 = java.lang.String.valueOf(r5);	 Catch:{ SQLiteException -> 0x0afa }
        r7 = r7.length();	 Catch:{ SQLiteException -> 0x0afa }
        r7 = r7 + 84;
        r8 = new java.lang.StringBuilder;	 Catch:{ SQLiteException -> 0x0afa }
        r8.<init>(r7);	 Catch:{ SQLiteException -> 0x0afa }
        r7 = "select metadata_fingerprint from raw_events where app_id = ?";
        r7 = r8.append(r7);	 Catch:{ SQLiteException -> 0x0afa }
        r5 = r7.append(r5);	 Catch:{ SQLiteException -> 0x0afa }
        r7 = " order by rowid limit 1;";
        r5 = r5.append(r7);	 Catch:{ SQLiteException -> 0x0afa }
        r5 = r5.toString();	 Catch:{ SQLiteException -> 0x0afa }
        r3 = r2.rawQuery(r5, r6);	 Catch:{ SQLiteException -> 0x0afa }
        r5 = r3.moveToFirst();	 Catch:{ SQLiteException -> 0x0afa }
        if (r5 != 0) goto L_0x0235;
    L_0x0221:
        if (r3 == 0) goto L_0x0086;
    L_0x0223:
        r3.close();	 Catch:{ all -> 0x01cb }
        goto L_0x0086;
    L_0x0228:
        r5 = 1;
        r5 = new java.lang.String[r5];	 Catch:{ SQLiteException -> 0x0afa }
        r6 = 0;
        r7 = 0;
        r5[r6] = r7;	 Catch:{ SQLiteException -> 0x0afa }
        r6 = r5;
        goto L_0x01e9;
    L_0x0231:
        r5 = "";
        goto L_0x01f2;
    L_0x0235:
        r5 = 0;
        r5 = r3.getString(r5);	 Catch:{ SQLiteException -> 0x0afa }
        r3.close();	 Catch:{ SQLiteException -> 0x0afa }
        r13 = r5;
        r11 = r3;
        r12 = r4;
        goto L_0x0188;
    L_0x0242:
        r3 = 0;
        r3 = r11.getBlob(r3);	 Catch:{ SQLiteException -> 0x02fc, all -> 0x0af6 }
        r4 = 0;
        r5 = r3.length;	 Catch:{ SQLiteException -> 0x02fc, all -> 0x0af6 }
        r3 = com.google.android.gms.internal.zzfjj.zzn(r3, r4, r5);	 Catch:{ SQLiteException -> 0x02fc, all -> 0x0af6 }
        r4 = new com.google.android.gms.internal.zzcme;	 Catch:{ SQLiteException -> 0x02fc, all -> 0x0af6 }
        r4.<init>();	 Catch:{ SQLiteException -> 0x02fc, all -> 0x0af6 }
        r4.zza(r3);	 Catch:{ IOException -> 0x02d5 }
        r3 = r11.moveToNext();	 Catch:{ SQLiteException -> 0x02fc, all -> 0x0af6 }
        if (r3 == 0) goto L_0x026d;
    L_0x025b:
        r3 = r14.zzawy();	 Catch:{ SQLiteException -> 0x02fc, all -> 0x0af6 }
        r3 = r3.zzazf();	 Catch:{ SQLiteException -> 0x02fc, all -> 0x0af6 }
        r5 = "Get multiple raw event metadata records, expected one. appId";
        r6 = com.google.android.gms.internal.zzchm.zzjk(r12);	 Catch:{ SQLiteException -> 0x02fc, all -> 0x0af6 }
        r3.zzj(r5, r6);	 Catch:{ SQLiteException -> 0x02fc, all -> 0x0af6 }
    L_0x026d:
        r11.close();	 Catch:{ SQLiteException -> 0x02fc, all -> 0x0af6 }
        r0 = r21;
        r0.zzb(r4);	 Catch:{ SQLiteException -> 0x02fc, all -> 0x0af6 }
        r4 = -1;
        r3 = (r16 > r4 ? 1 : (r16 == r4 ? 0 : -1));
        if (r3 == 0) goto L_0x02ef;
    L_0x027b:
        r5 = "app_id = ? and metadata_fingerprint = ? and rowid <= ?";
        r3 = 3;
        r6 = new java.lang.String[r3];	 Catch:{ SQLiteException -> 0x02fc, all -> 0x0af6 }
        r3 = 0;
        r6[r3] = r12;	 Catch:{ SQLiteException -> 0x02fc, all -> 0x0af6 }
        r3 = 1;
        r6[r3] = r13;	 Catch:{ SQLiteException -> 0x02fc, all -> 0x0af6 }
        r3 = 2;
        r4 = java.lang.String.valueOf(r16);	 Catch:{ SQLiteException -> 0x02fc, all -> 0x0af6 }
        r6[r3] = r4;	 Catch:{ SQLiteException -> 0x02fc, all -> 0x0af6 }
    L_0x028e:
        r3 = "raw_events";
        r4 = 4;
        r4 = new java.lang.String[r4];	 Catch:{ SQLiteException -> 0x02fc, all -> 0x0af6 }
        r7 = 0;
        r8 = "rowid";
        r4[r7] = r8;	 Catch:{ SQLiteException -> 0x02fc, all -> 0x0af6 }
        r7 = 1;
        r8 = "name";
        r4[r7] = r8;	 Catch:{ SQLiteException -> 0x02fc, all -> 0x0af6 }
        r7 = 2;
        r8 = "timestamp";
        r4[r7] = r8;	 Catch:{ SQLiteException -> 0x02fc, all -> 0x0af6 }
        r7 = 3;
        r8 = "data";
        r4[r7] = r8;	 Catch:{ SQLiteException -> 0x02fc, all -> 0x0af6 }
        r7 = 0;
        r8 = 0;
        r9 = "rowid";
        r10 = 0;
        r3 = r2.query(r3, r4, r5, r6, r7, r8, r9, r10);	 Catch:{ SQLiteException -> 0x02fc, all -> 0x0af6 }
        r2 = r3.moveToFirst();	 Catch:{ SQLiteException -> 0x0afd }
        if (r2 != 0) goto L_0x0318;
    L_0x02bc:
        r2 = r14.zzawy();	 Catch:{ SQLiteException -> 0x0afd }
        r2 = r2.zzazf();	 Catch:{ SQLiteException -> 0x0afd }
        r4 = "Raw event data disappeared while in transaction. appId";
        r5 = com.google.android.gms.internal.zzchm.zzjk(r12);	 Catch:{ SQLiteException -> 0x0afd }
        r2.zzj(r4, r5);	 Catch:{ SQLiteException -> 0x0afd }
        if (r3 == 0) goto L_0x0086;
    L_0x02d0:
        r3.close();	 Catch:{ all -> 0x01cb }
        goto L_0x0086;
    L_0x02d5:
        r2 = move-exception;
        r3 = r14.zzawy();	 Catch:{ SQLiteException -> 0x02fc, all -> 0x0af6 }
        r3 = r3.zzazd();	 Catch:{ SQLiteException -> 0x02fc, all -> 0x0af6 }
        r4 = "Data loss. Failed to merge raw event metadata. appId";
        r5 = com.google.android.gms.internal.zzchm.zzjk(r12);	 Catch:{ SQLiteException -> 0x02fc, all -> 0x0af6 }
        r3.zze(r4, r5, r2);	 Catch:{ SQLiteException -> 0x02fc, all -> 0x0af6 }
        if (r11 == 0) goto L_0x0086;
    L_0x02ea:
        r11.close();	 Catch:{ all -> 0x01cb }
        goto L_0x0086;
    L_0x02ef:
        r5 = "app_id = ? and metadata_fingerprint = ?";
        r3 = 2;
        r6 = new java.lang.String[r3];	 Catch:{ SQLiteException -> 0x02fc, all -> 0x0af6 }
        r3 = 0;
        r6[r3] = r12;	 Catch:{ SQLiteException -> 0x02fc, all -> 0x0af6 }
        r3 = 1;
        r6[r3] = r13;	 Catch:{ SQLiteException -> 0x02fc, all -> 0x0af6 }
        goto L_0x028e;
    L_0x02fc:
        r2 = move-exception;
        r3 = r11;
        r4 = r12;
    L_0x02ff:
        r5 = r14.zzawy();	 Catch:{ all -> 0x0371 }
        r5 = r5.zzazd();	 Catch:{ all -> 0x0371 }
        r6 = "Data loss. Error selecting raw event. appId";
        r4 = com.google.android.gms.internal.zzchm.zzjk(r4);	 Catch:{ all -> 0x0371 }
        r5.zze(r6, r4, r2);	 Catch:{ all -> 0x0371 }
        if (r3 == 0) goto L_0x0086;
    L_0x0313:
        r3.close();	 Catch:{ all -> 0x01cb }
        goto L_0x0086;
    L_0x0318:
        r2 = 0;
        r4 = r3.getLong(r2);	 Catch:{ SQLiteException -> 0x0afd }
        r2 = 3;
        r2 = r3.getBlob(r2);	 Catch:{ SQLiteException -> 0x0afd }
        r6 = 0;
        r7 = r2.length;	 Catch:{ SQLiteException -> 0x0afd }
        r2 = com.google.android.gms.internal.zzfjj.zzn(r2, r6, r7);	 Catch:{ SQLiteException -> 0x0afd }
        r6 = new com.google.android.gms.internal.zzcmb;	 Catch:{ SQLiteException -> 0x0afd }
        r6.<init>();	 Catch:{ SQLiteException -> 0x0afd }
        r6.zza(r2);	 Catch:{ IOException -> 0x0351 }
        r2 = 1;
        r2 = r3.getString(r2);	 Catch:{ SQLiteException -> 0x0afd }
        r6.name = r2;	 Catch:{ SQLiteException -> 0x0afd }
        r2 = 2;
        r8 = r3.getLong(r2);	 Catch:{ SQLiteException -> 0x0afd }
        r2 = java.lang.Long.valueOf(r8);	 Catch:{ SQLiteException -> 0x0afd }
        r6.zzjli = r2;	 Catch:{ SQLiteException -> 0x0afd }
        r0 = r21;
        r2 = r0.zza(r4, r6);	 Catch:{ SQLiteException -> 0x0afd }
        if (r2 != 0) goto L_0x0364;
    L_0x034a:
        if (r3 == 0) goto L_0x0086;
    L_0x034c:
        r3.close();	 Catch:{ all -> 0x01cb }
        goto L_0x0086;
    L_0x0351:
        r2 = move-exception;
        r4 = r14.zzawy();	 Catch:{ SQLiteException -> 0x0afd }
        r4 = r4.zzazd();	 Catch:{ SQLiteException -> 0x0afd }
        r5 = "Data loss. Failed to merge raw event. appId";
        r6 = com.google.android.gms.internal.zzchm.zzjk(r12);	 Catch:{ SQLiteException -> 0x0afd }
        r4.zze(r5, r6, r2);	 Catch:{ SQLiteException -> 0x0afd }
    L_0x0364:
        r2 = r3.moveToNext();	 Catch:{ SQLiteException -> 0x0afd }
        if (r2 != 0) goto L_0x0318;
    L_0x036a:
        if (r3 == 0) goto L_0x0086;
    L_0x036c:
        r3.close();	 Catch:{ all -> 0x01cb }
        goto L_0x0086;
    L_0x0371:
        r2 = move-exception;
    L_0x0372:
        if (r3 == 0) goto L_0x0377;
    L_0x0374:
        r3.close();	 Catch:{ all -> 0x01cb }
    L_0x0377:
        throw r2;	 Catch:{ all -> 0x01cb }
    L_0x0378:
        r2 = 0;
        goto L_0x0097;
    L_0x037b:
        r2 = 0;
        goto L_0x0125;
    L_0x037e:
        r3 = r30.zzawv();	 Catch:{ all -> 0x01cb }
        r0 = r21;
        r2 = r0.zzjgi;	 Catch:{ all -> 0x01cb }
        r4 = r2.zzcn;	 Catch:{ all -> 0x01cb }
        r0 = r21;
        r2 = r0.zzapa;	 Catch:{ all -> 0x01cb }
        r2 = r2.get(r13);	 Catch:{ all -> 0x01cb }
        r2 = (com.google.android.gms.internal.zzcmb) r2;	 Catch:{ all -> 0x01cb }
        r2 = r2.name;	 Catch:{ all -> 0x01cb }
        r14 = r3.zzao(r4, r2);	 Catch:{ all -> 0x01cb }
        if (r14 != 0) goto L_0x03af;
    L_0x039a:
        r30.zzawu();	 Catch:{ all -> 0x01cb }
        r0 = r21;
        r2 = r0.zzapa;	 Catch:{ all -> 0x01cb }
        r2 = r2.get(r13);	 Catch:{ all -> 0x01cb }
        r2 = (com.google.android.gms.internal.zzcmb) r2;	 Catch:{ all -> 0x01cb }
        r2 = r2.name;	 Catch:{ all -> 0x01cb }
        r2 = com.google.android.gms.internal.zzclq.zzkn(r2);	 Catch:{ all -> 0x01cb }
        if (r2 == 0) goto L_0x063a;
    L_0x03af:
        r3 = 0;
        r4 = 0;
        r0 = r21;
        r2 = r0.zzapa;	 Catch:{ all -> 0x01cb }
        r2 = r2.get(r13);	 Catch:{ all -> 0x01cb }
        r2 = (com.google.android.gms.internal.zzcmb) r2;	 Catch:{ all -> 0x01cb }
        r2 = r2.zzjlh;	 Catch:{ all -> 0x01cb }
        if (r2 != 0) goto L_0x03ce;
    L_0x03bf:
        r0 = r21;
        r2 = r0.zzapa;	 Catch:{ all -> 0x01cb }
        r2 = r2.get(r13);	 Catch:{ all -> 0x01cb }
        r2 = (com.google.android.gms.internal.zzcmb) r2;	 Catch:{ all -> 0x01cb }
        r5 = 0;
        r5 = new com.google.android.gms.internal.zzcmc[r5];	 Catch:{ all -> 0x01cb }
        r2.zzjlh = r5;	 Catch:{ all -> 0x01cb }
    L_0x03ce:
        r0 = r21;
        r2 = r0.zzapa;	 Catch:{ all -> 0x01cb }
        r2 = r2.get(r13);	 Catch:{ all -> 0x01cb }
        r2 = (com.google.android.gms.internal.zzcmb) r2;	 Catch:{ all -> 0x01cb }
        r6 = r2.zzjlh;	 Catch:{ all -> 0x01cb }
        r7 = r6.length;	 Catch:{ all -> 0x01cb }
        r2 = 0;
        r5 = r2;
    L_0x03dd:
        if (r5 >= r7) goto L_0x0411;
    L_0x03df:
        r2 = r6[r5];	 Catch:{ all -> 0x01cb }
        r8 = "_c";
        r9 = r2.name;	 Catch:{ all -> 0x01cb }
        r8 = r8.equals(r9);	 Catch:{ all -> 0x01cb }
        if (r8 == 0) goto L_0x03fc;
    L_0x03ec:
        r8 = 1;
        r3 = java.lang.Long.valueOf(r8);	 Catch:{ all -> 0x01cb }
        r2.zzjll = r3;	 Catch:{ all -> 0x01cb }
        r2 = 1;
        r3 = r2;
        r2 = r4;
    L_0x03f7:
        r4 = r5 + 1;
        r5 = r4;
        r4 = r2;
        goto L_0x03dd;
    L_0x03fc:
        r8 = "_r";
        r9 = r2.name;	 Catch:{ all -> 0x01cb }
        r8 = r8.equals(r9);	 Catch:{ all -> 0x01cb }
        if (r8 == 0) goto L_0x0b13;
    L_0x0407:
        r8 = 1;
        r4 = java.lang.Long.valueOf(r8);	 Catch:{ all -> 0x01cb }
        r2.zzjll = r4;	 Catch:{ all -> 0x01cb }
        r2 = 1;
        goto L_0x03f7;
    L_0x0411:
        if (r3 != 0) goto L_0x047b;
    L_0x0413:
        if (r14 == 0) goto L_0x047b;
    L_0x0415:
        r2 = r30.zzawy();	 Catch:{ all -> 0x01cb }
        r3 = r2.zzazj();	 Catch:{ all -> 0x01cb }
        r5 = "Marking event as conversion";
        r6 = r30.zzawt();	 Catch:{ all -> 0x01cb }
        r0 = r21;
        r2 = r0.zzapa;	 Catch:{ all -> 0x01cb }
        r2 = r2.get(r13);	 Catch:{ all -> 0x01cb }
        r2 = (com.google.android.gms.internal.zzcmb) r2;	 Catch:{ all -> 0x01cb }
        r2 = r2.name;	 Catch:{ all -> 0x01cb }
        r2 = r6.zzjh(r2);	 Catch:{ all -> 0x01cb }
        r3.zzj(r5, r2);	 Catch:{ all -> 0x01cb }
        r0 = r21;
        r2 = r0.zzapa;	 Catch:{ all -> 0x01cb }
        r2 = r2.get(r13);	 Catch:{ all -> 0x01cb }
        r2 = (com.google.android.gms.internal.zzcmb) r2;	 Catch:{ all -> 0x01cb }
        r3 = r2.zzjlh;	 Catch:{ all -> 0x01cb }
        r0 = r21;
        r2 = r0.zzapa;	 Catch:{ all -> 0x01cb }
        r2 = r2.get(r13);	 Catch:{ all -> 0x01cb }
        r2 = (com.google.android.gms.internal.zzcmb) r2;	 Catch:{ all -> 0x01cb }
        r2 = r2.zzjlh;	 Catch:{ all -> 0x01cb }
        r2 = r2.length;	 Catch:{ all -> 0x01cb }
        r2 = r2 + 1;
        r2 = java.util.Arrays.copyOf(r3, r2);	 Catch:{ all -> 0x01cb }
        r2 = (com.google.android.gms.internal.zzcmc[]) r2;	 Catch:{ all -> 0x01cb }
        r3 = new com.google.android.gms.internal.zzcmc;	 Catch:{ all -> 0x01cb }
        r3.<init>();	 Catch:{ all -> 0x01cb }
        r5 = "_c";
        r3.name = r5;	 Catch:{ all -> 0x01cb }
        r6 = 1;
        r5 = java.lang.Long.valueOf(r6);	 Catch:{ all -> 0x01cb }
        r3.zzjll = r5;	 Catch:{ all -> 0x01cb }
        r5 = r2.length;	 Catch:{ all -> 0x01cb }
        r5 = r5 + -1;
        r2[r5] = r3;	 Catch:{ all -> 0x01cb }
        r0 = r21;
        r3 = r0.zzapa;	 Catch:{ all -> 0x01cb }
        r3 = r3.get(r13);	 Catch:{ all -> 0x01cb }
        r3 = (com.google.android.gms.internal.zzcmb) r3;	 Catch:{ all -> 0x01cb }
        r3.zzjlh = r2;	 Catch:{ all -> 0x01cb }
    L_0x047b:
        if (r4 != 0) goto L_0x04e3;
    L_0x047d:
        r2 = r30.zzawy();	 Catch:{ all -> 0x01cb }
        r3 = r2.zzazj();	 Catch:{ all -> 0x01cb }
        r4 = "Marking event as real-time";
        r5 = r30.zzawt();	 Catch:{ all -> 0x01cb }
        r0 = r21;
        r2 = r0.zzapa;	 Catch:{ all -> 0x01cb }
        r2 = r2.get(r13);	 Catch:{ all -> 0x01cb }
        r2 = (com.google.android.gms.internal.zzcmb) r2;	 Catch:{ all -> 0x01cb }
        r2 = r2.name;	 Catch:{ all -> 0x01cb }
        r2 = r5.zzjh(r2);	 Catch:{ all -> 0x01cb }
        r3.zzj(r4, r2);	 Catch:{ all -> 0x01cb }
        r0 = r21;
        r2 = r0.zzapa;	 Catch:{ all -> 0x01cb }
        r2 = r2.get(r13);	 Catch:{ all -> 0x01cb }
        r2 = (com.google.android.gms.internal.zzcmb) r2;	 Catch:{ all -> 0x01cb }
        r3 = r2.zzjlh;	 Catch:{ all -> 0x01cb }
        r0 = r21;
        r2 = r0.zzapa;	 Catch:{ all -> 0x01cb }
        r2 = r2.get(r13);	 Catch:{ all -> 0x01cb }
        r2 = (com.google.android.gms.internal.zzcmb) r2;	 Catch:{ all -> 0x01cb }
        r2 = r2.zzjlh;	 Catch:{ all -> 0x01cb }
        r2 = r2.length;	 Catch:{ all -> 0x01cb }
        r2 = r2 + 1;
        r2 = java.util.Arrays.copyOf(r3, r2);	 Catch:{ all -> 0x01cb }
        r2 = (com.google.android.gms.internal.zzcmc[]) r2;	 Catch:{ all -> 0x01cb }
        r3 = new com.google.android.gms.internal.zzcmc;	 Catch:{ all -> 0x01cb }
        r3.<init>();	 Catch:{ all -> 0x01cb }
        r4 = "_r";
        r3.name = r4;	 Catch:{ all -> 0x01cb }
        r4 = 1;
        r4 = java.lang.Long.valueOf(r4);	 Catch:{ all -> 0x01cb }
        r3.zzjll = r4;	 Catch:{ all -> 0x01cb }
        r4 = r2.length;	 Catch:{ all -> 0x01cb }
        r4 = r4 + -1;
        r2[r4] = r3;	 Catch:{ all -> 0x01cb }
        r0 = r21;
        r3 = r0.zzapa;	 Catch:{ all -> 0x01cb }
        r3 = r3.get(r13);	 Catch:{ all -> 0x01cb }
        r3 = (com.google.android.gms.internal.zzcmb) r3;	 Catch:{ all -> 0x01cb }
        r3.zzjlh = r2;	 Catch:{ all -> 0x01cb }
    L_0x04e3:
        r2 = 1;
        r3 = r30.zzaws();	 Catch:{ all -> 0x01cb }
        r4 = r30.zzbag();	 Catch:{ all -> 0x01cb }
        r0 = r21;
        r6 = r0.zzjgi;	 Catch:{ all -> 0x01cb }
        r6 = r6.zzcn;	 Catch:{ all -> 0x01cb }
        r7 = 0;
        r8 = 0;
        r9 = 0;
        r10 = 0;
        r11 = 1;
        r3 = r3.zza(r4, r6, r7, r8, r9, r10, r11);	 Catch:{ all -> 0x01cb }
        r4 = r3.zzizb;	 Catch:{ all -> 0x01cb }
        r0 = r30;
        r3 = r0.zzjew;	 Catch:{ all -> 0x01cb }
        r0 = r21;
        r6 = r0.zzjgi;	 Catch:{ all -> 0x01cb }
        r6 = r6.zzcn;	 Catch:{ all -> 0x01cb }
        r3 = r3.zzix(r6);	 Catch:{ all -> 0x01cb }
        r6 = (long) r3;	 Catch:{ all -> 0x01cb }
        r3 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r3 <= 0) goto L_0x0b0f;
    L_0x0510:
        r0 = r21;
        r2 = r0.zzapa;	 Catch:{ all -> 0x01cb }
        r2 = r2.get(r13);	 Catch:{ all -> 0x01cb }
        r2 = (com.google.android.gms.internal.zzcmb) r2;	 Catch:{ all -> 0x01cb }
        r3 = 0;
    L_0x051b:
        r4 = r2.zzjlh;	 Catch:{ all -> 0x01cb }
        r4 = r4.length;	 Catch:{ all -> 0x01cb }
        if (r3 >= r4) goto L_0x054d;
    L_0x0520:
        r4 = "_r";
        r5 = r2.zzjlh;	 Catch:{ all -> 0x01cb }
        r5 = r5[r3];	 Catch:{ all -> 0x01cb }
        r5 = r5.name;	 Catch:{ all -> 0x01cb }
        r4 = r4.equals(r5);	 Catch:{ all -> 0x01cb }
        if (r4 == 0) goto L_0x05ce;
    L_0x052f:
        r4 = r2.zzjlh;	 Catch:{ all -> 0x01cb }
        r4 = r4.length;	 Catch:{ all -> 0x01cb }
        r4 = r4 + -1;
        r4 = new com.google.android.gms.internal.zzcmc[r4];	 Catch:{ all -> 0x01cb }
        if (r3 <= 0) goto L_0x053f;
    L_0x0538:
        r5 = r2.zzjlh;	 Catch:{ all -> 0x01cb }
        r6 = 0;
        r7 = 0;
        java.lang.System.arraycopy(r5, r6, r4, r7, r3);	 Catch:{ all -> 0x01cb }
    L_0x053f:
        r5 = r4.length;	 Catch:{ all -> 0x01cb }
        if (r3 >= r5) goto L_0x054b;
    L_0x0542:
        r5 = r2.zzjlh;	 Catch:{ all -> 0x01cb }
        r6 = r3 + 1;
        r7 = r4.length;	 Catch:{ all -> 0x01cb }
        r7 = r7 - r3;
        java.lang.System.arraycopy(r5, r6, r4, r3, r7);	 Catch:{ all -> 0x01cb }
    L_0x054b:
        r2.zzjlh = r4;	 Catch:{ all -> 0x01cb }
    L_0x054d:
        r0 = r21;
        r2 = r0.zzapa;	 Catch:{ all -> 0x01cb }
        r2 = r2.get(r13);	 Catch:{ all -> 0x01cb }
        r2 = (com.google.android.gms.internal.zzcmb) r2;	 Catch:{ all -> 0x01cb }
        r2 = r2.name;	 Catch:{ all -> 0x01cb }
        r2 = com.google.android.gms.internal.zzclq.zzjz(r2);	 Catch:{ all -> 0x01cb }
        if (r2 == 0) goto L_0x063a;
    L_0x055f:
        if (r14 == 0) goto L_0x063a;
    L_0x0561:
        r3 = r30.zzaws();	 Catch:{ all -> 0x01cb }
        r4 = r30.zzbag();	 Catch:{ all -> 0x01cb }
        r0 = r21;
        r2 = r0.zzjgi;	 Catch:{ all -> 0x01cb }
        r6 = r2.zzcn;	 Catch:{ all -> 0x01cb }
        r7 = 0;
        r8 = 0;
        r9 = 1;
        r10 = 0;
        r11 = 0;
        r2 = r3.zza(r4, r6, r7, r8, r9, r10, r11);	 Catch:{ all -> 0x01cb }
        r2 = r2.zziyz;	 Catch:{ all -> 0x01cb }
        r0 = r30;
        r4 = r0.zzjew;	 Catch:{ all -> 0x01cb }
        r0 = r21;
        r5 = r0.zzjgi;	 Catch:{ all -> 0x01cb }
        r5 = r5.zzcn;	 Catch:{ all -> 0x01cb }
        r6 = com.google.android.gms.internal.zzchc.zzjaq;	 Catch:{ all -> 0x01cb }
        r4 = r4.zzb(r5, r6);	 Catch:{ all -> 0x01cb }
        r4 = (long) r4;	 Catch:{ all -> 0x01cb }
        r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
        if (r2 <= 0) goto L_0x063a;
    L_0x058f:
        r2 = r30.zzawy();	 Catch:{ all -> 0x01cb }
        r2 = r2.zzazf();	 Catch:{ all -> 0x01cb }
        r3 = "Too many conversions. Not logging as conversion. appId";
        r0 = r21;
        r4 = r0.zzjgi;	 Catch:{ all -> 0x01cb }
        r4 = r4.zzcn;	 Catch:{ all -> 0x01cb }
        r4 = com.google.android.gms.internal.zzchm.zzjk(r4);	 Catch:{ all -> 0x01cb }
        r2.zzj(r3, r4);	 Catch:{ all -> 0x01cb }
        r0 = r21;
        r2 = r0.zzapa;	 Catch:{ all -> 0x01cb }
        r2 = r2.get(r13);	 Catch:{ all -> 0x01cb }
        r2 = (com.google.android.gms.internal.zzcmb) r2;	 Catch:{ all -> 0x01cb }
        r5 = 0;
        r4 = 0;
        r7 = r2.zzjlh;	 Catch:{ all -> 0x01cb }
        r8 = r7.length;	 Catch:{ all -> 0x01cb }
        r3 = 0;
        r6 = r3;
    L_0x05b8:
        if (r6 >= r8) goto L_0x05e4;
    L_0x05ba:
        r3 = r7[r6];	 Catch:{ all -> 0x01cb }
        r9 = "_c";
        r10 = r3.name;	 Catch:{ all -> 0x01cb }
        r9 = r9.equals(r10);	 Catch:{ all -> 0x01cb }
        if (r9 == 0) goto L_0x05d2;
    L_0x05c7:
        r4 = r5;
    L_0x05c8:
        r5 = r6 + 1;
        r6 = r5;
        r5 = r4;
        r4 = r3;
        goto L_0x05b8;
    L_0x05ce:
        r3 = r3 + 1;
        goto L_0x051b;
    L_0x05d2:
        r9 = "_err";
        r3 = r3.name;	 Catch:{ all -> 0x01cb }
        r3 = r9.equals(r3);	 Catch:{ all -> 0x01cb }
        if (r3 == 0) goto L_0x0b0b;
    L_0x05dd:
        r3 = 1;
        r29 = r4;
        r4 = r3;
        r3 = r29;
        goto L_0x05c8;
    L_0x05e4:
        if (r5 == 0) goto L_0x0610;
    L_0x05e6:
        if (r4 == 0) goto L_0x0610;
    L_0x05e8:
        r3 = r2.zzjlh;	 Catch:{ all -> 0x01cb }
        r5 = 1;
        r5 = new com.google.android.gms.internal.zzcmc[r5];	 Catch:{ all -> 0x01cb }
        r6 = 0;
        r5[r6] = r4;	 Catch:{ all -> 0x01cb }
        r3 = com.google.android.gms.common.util.zza.zza(r3, r5);	 Catch:{ all -> 0x01cb }
        r3 = (com.google.android.gms.internal.zzcmc[]) r3;	 Catch:{ all -> 0x01cb }
        r2.zzjlh = r3;	 Catch:{ all -> 0x01cb }
        r4 = r17;
    L_0x05fa:
        r0 = r22;
        r5 = r0.zzjlp;	 Catch:{ all -> 0x01cb }
        r3 = r12 + 1;
        r0 = r21;
        r2 = r0.zzapa;	 Catch:{ all -> 0x01cb }
        r2 = r2.get(r13);	 Catch:{ all -> 0x01cb }
        r2 = (com.google.android.gms.internal.zzcmb) r2;	 Catch:{ all -> 0x01cb }
        r5[r12] = r2;	 Catch:{ all -> 0x01cb }
        r2 = r3;
        r3 = r4;
        goto L_0x015e;
    L_0x0610:
        if (r4 == 0) goto L_0x0622;
    L_0x0612:
        r2 = "_err";
        r4.name = r2;	 Catch:{ all -> 0x01cb }
        r2 = 10;
        r2 = java.lang.Long.valueOf(r2);	 Catch:{ all -> 0x01cb }
        r4.zzjll = r2;	 Catch:{ all -> 0x01cb }
        r4 = r17;
        goto L_0x05fa;
    L_0x0622:
        r2 = r30.zzawy();	 Catch:{ all -> 0x01cb }
        r2 = r2.zzazd();	 Catch:{ all -> 0x01cb }
        r3 = "Did not find conversion parameter. appId";
        r0 = r21;
        r4 = r0.zzjgi;	 Catch:{ all -> 0x01cb }
        r4 = r4.zzcn;	 Catch:{ all -> 0x01cb }
        r4 = com.google.android.gms.internal.zzchm.zzjk(r4);	 Catch:{ all -> 0x01cb }
        r2.zzj(r3, r4);	 Catch:{ all -> 0x01cb }
    L_0x063a:
        r4 = r17;
        goto L_0x05fa;
    L_0x063d:
        r0 = r21;
        r2 = r0.zzapa;	 Catch:{ all -> 0x01cb }
        r2 = r2.size();	 Catch:{ all -> 0x01cb }
        if (r12 >= r2) goto L_0x0655;
    L_0x0647:
        r0 = r22;
        r2 = r0.zzjlp;	 Catch:{ all -> 0x01cb }
        r2 = java.util.Arrays.copyOf(r2, r12);	 Catch:{ all -> 0x01cb }
        r2 = (com.google.android.gms.internal.zzcmb[]) r2;	 Catch:{ all -> 0x01cb }
        r0 = r22;
        r0.zzjlp = r2;	 Catch:{ all -> 0x01cb }
    L_0x0655:
        r0 = r21;
        r2 = r0.zzjgi;	 Catch:{ all -> 0x01cb }
        r2 = r2.zzcn;	 Catch:{ all -> 0x01cb }
        r0 = r21;
        r3 = r0.zzjgi;	 Catch:{ all -> 0x01cb }
        r3 = r3.zzjlq;	 Catch:{ all -> 0x01cb }
        r0 = r22;
        r4 = r0.zzjlp;	 Catch:{ all -> 0x01cb }
        r0 = r30;
        r2 = r0.zza(r2, r3, r4);	 Catch:{ all -> 0x01cb }
        r0 = r22;
        r0.zzjmi = r2;	 Catch:{ all -> 0x01cb }
        r2 = com.google.android.gms.internal.zzchc.zzjac;	 Catch:{ all -> 0x01cb }
        r2 = r2.get();	 Catch:{ all -> 0x01cb }
        r2 = (java.lang.Boolean) r2;	 Catch:{ all -> 0x01cb }
        r2 = r2.booleanValue();	 Catch:{ all -> 0x01cb }
        if (r2 == 0) goto L_0x0954;
    L_0x067d:
        r0 = r30;
        r2 = r0.zzjew;	 Catch:{ all -> 0x01cb }
        r0 = r21;
        r3 = r0.zzjgi;	 Catch:{ all -> 0x01cb }
        r3 = r3.zzcn;	 Catch:{ all -> 0x01cb }
        r4 = "1";
        r2 = r2.zzawv();	 Catch:{ all -> 0x01cb }
        r5 = "measurement.event_sampling_enabled";
        r2 = r2.zzam(r3, r5);	 Catch:{ all -> 0x01cb }
        r2 = r4.equals(r2);	 Catch:{ all -> 0x01cb }
        if (r2 == 0) goto L_0x0954;
    L_0x069b:
        r23 = new java.util.HashMap;	 Catch:{ all -> 0x01cb }
        r23.<init>();	 Catch:{ all -> 0x01cb }
        r0 = r22;
        r2 = r0.zzjlp;	 Catch:{ all -> 0x01cb }
        r2 = r2.length;	 Catch:{ all -> 0x01cb }
        r0 = new com.google.android.gms.internal.zzcmb[r2];	 Catch:{ all -> 0x01cb }
        r24 = r0;
        r18 = 0;
        r2 = r30.zzawu();	 Catch:{ all -> 0x01cb }
        r25 = r2.zzbaz();	 Catch:{ all -> 0x01cb }
        r0 = r22;
        r0 = r0.zzjlp;	 Catch:{ all -> 0x01cb }
        r26 = r0;
        r0 = r26;
        r0 = r0.length;	 Catch:{ all -> 0x01cb }
        r27 = r0;
        r2 = 0;
        r20 = r2;
    L_0x06c1:
        r0 = r20;
        r1 = r27;
        if (r0 >= r1) goto L_0x091b;
    L_0x06c7:
        r28 = r26[r20];	 Catch:{ all -> 0x01cb }
        r0 = r28;
        r2 = r0.name;	 Catch:{ all -> 0x01cb }
        r3 = "_ep";
        r2 = r2.equals(r3);	 Catch:{ all -> 0x01cb }
        if (r2 == 0) goto L_0x0755;
    L_0x06d6:
        r30.zzawu();	 Catch:{ all -> 0x01cb }
        r2 = "_en";
        r0 = r28;
        r2 = com.google.android.gms.internal.zzclq.zza(r0, r2);	 Catch:{ all -> 0x01cb }
        r2 = (java.lang.String) r2;	 Catch:{ all -> 0x01cb }
        r0 = r23;
        r3 = r0.get(r2);	 Catch:{ all -> 0x01cb }
        r3 = (com.google.android.gms.internal.zzcgw) r3;	 Catch:{ all -> 0x01cb }
        if (r3 != 0) goto L_0x0701;
    L_0x06ee:
        r3 = r30.zzaws();	 Catch:{ all -> 0x01cb }
        r0 = r21;
        r4 = r0.zzjgi;	 Catch:{ all -> 0x01cb }
        r4 = r4.zzcn;	 Catch:{ all -> 0x01cb }
        r3 = r3.zzae(r4, r2);	 Catch:{ all -> 0x01cb }
        r0 = r23;
        r0.put(r2, r3);	 Catch:{ all -> 0x01cb }
    L_0x0701:
        r2 = r3.zzizo;	 Catch:{ all -> 0x01cb }
        if (r2 != 0) goto L_0x0917;
    L_0x0705:
        r2 = r3.zzizp;	 Catch:{ all -> 0x01cb }
        r4 = r2.longValue();	 Catch:{ all -> 0x01cb }
        r6 = 1;
        r2 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r2 <= 0) goto L_0x0725;
    L_0x0711:
        r30.zzawu();	 Catch:{ all -> 0x01cb }
        r0 = r28;
        r2 = r0.zzjlh;	 Catch:{ all -> 0x01cb }
        r4 = "_sr";
        r5 = r3.zzizp;	 Catch:{ all -> 0x01cb }
        r2 = com.google.android.gms.internal.zzclq.zza(r2, r4, r5);	 Catch:{ all -> 0x01cb }
        r0 = r28;
        r0.zzjlh = r2;	 Catch:{ all -> 0x01cb }
    L_0x0725:
        r2 = r3.zzizq;	 Catch:{ all -> 0x01cb }
        if (r2 == 0) goto L_0x0749;
    L_0x0729:
        r2 = r3.zzizq;	 Catch:{ all -> 0x01cb }
        r2 = r2.booleanValue();	 Catch:{ all -> 0x01cb }
        if (r2 == 0) goto L_0x0749;
    L_0x0731:
        r30.zzawu();	 Catch:{ all -> 0x01cb }
        r0 = r28;
        r2 = r0.zzjlh;	 Catch:{ all -> 0x01cb }
        r3 = "_efs";
        r4 = 1;
        r4 = java.lang.Long.valueOf(r4);	 Catch:{ all -> 0x01cb }
        r2 = com.google.android.gms.internal.zzclq.zza(r2, r3, r4);	 Catch:{ all -> 0x01cb }
        r0 = r28;
        r0.zzjlh = r2;	 Catch:{ all -> 0x01cb }
    L_0x0749:
        r2 = r18 + 1;
        r24[r18] = r28;	 Catch:{ all -> 0x01cb }
    L_0x074d:
        r3 = r20 + 1;
        r20 = r3;
        r18 = r2;
        goto L_0x06c1;
    L_0x0755:
        r2 = 1;
        r3 = "_dbg";
        r4 = 1;
        r4 = java.lang.Long.valueOf(r4);	 Catch:{ all -> 0x01cb }
        r0 = r28;
        r3 = zza(r0, r3, r4);	 Catch:{ all -> 0x01cb }
        if (r3 != 0) goto L_0x0b07;
    L_0x0767:
        r2 = r30.zzawv();	 Catch:{ all -> 0x01cb }
        r0 = r21;
        r3 = r0.zzjgi;	 Catch:{ all -> 0x01cb }
        r3 = r3.zzcn;	 Catch:{ all -> 0x01cb }
        r0 = r28;
        r4 = r0.name;	 Catch:{ all -> 0x01cb }
        r2 = r2.zzap(r3, r4);	 Catch:{ all -> 0x01cb }
        r19 = r2;
    L_0x077b:
        if (r19 > 0) goto L_0x0798;
    L_0x077d:
        r2 = r30.zzawy();	 Catch:{ all -> 0x01cb }
        r2 = r2.zzazf();	 Catch:{ all -> 0x01cb }
        r3 = "Sample rate must be positive. event, rate";
        r0 = r28;
        r4 = r0.name;	 Catch:{ all -> 0x01cb }
        r5 = java.lang.Integer.valueOf(r19);	 Catch:{ all -> 0x01cb }
        r2.zze(r3, r4, r5);	 Catch:{ all -> 0x01cb }
        r2 = r18 + 1;
        r24[r18] = r28;	 Catch:{ all -> 0x01cb }
        goto L_0x074d;
    L_0x0798:
        r0 = r28;
        r2 = r0.name;	 Catch:{ all -> 0x01cb }
        r0 = r23;
        r2 = r0.get(r2);	 Catch:{ all -> 0x01cb }
        r2 = (com.google.android.gms.internal.zzcgw) r2;	 Catch:{ all -> 0x01cb }
        if (r2 != 0) goto L_0x0b04;
    L_0x07a6:
        r2 = r30.zzaws();	 Catch:{ all -> 0x01cb }
        r0 = r21;
        r3 = r0.zzjgi;	 Catch:{ all -> 0x01cb }
        r3 = r3.zzcn;	 Catch:{ all -> 0x01cb }
        r0 = r28;
        r4 = r0.name;	 Catch:{ all -> 0x01cb }
        r3 = r2.zzae(r3, r4);	 Catch:{ all -> 0x01cb }
        if (r3 != 0) goto L_0x07f3;
    L_0x07ba:
        r2 = r30.zzawy();	 Catch:{ all -> 0x01cb }
        r2 = r2.zzazf();	 Catch:{ all -> 0x01cb }
        r3 = "Event being bundled has no eventAggregate. appId, eventName";
        r0 = r21;
        r4 = r0.zzjgi;	 Catch:{ all -> 0x01cb }
        r4 = r4.zzcn;	 Catch:{ all -> 0x01cb }
        r0 = r28;
        r5 = r0.name;	 Catch:{ all -> 0x01cb }
        r2.zze(r3, r4, r5);	 Catch:{ all -> 0x01cb }
        r3 = new com.google.android.gms.internal.zzcgw;	 Catch:{ all -> 0x01cb }
        r0 = r21;
        r2 = r0.zzjgi;	 Catch:{ all -> 0x01cb }
        r4 = r2.zzcn;	 Catch:{ all -> 0x01cb }
        r0 = r28;
        r5 = r0.name;	 Catch:{ all -> 0x01cb }
        r6 = 1;
        r8 = 1;
        r0 = r28;
        r2 = r0.zzjli;	 Catch:{ all -> 0x01cb }
        r10 = r2.longValue();	 Catch:{ all -> 0x01cb }
        r12 = 0;
        r14 = 0;
        r15 = 0;
        r16 = 0;
        r3.<init>(r4, r5, r6, r8, r10, r12, r14, r15, r16);	 Catch:{ all -> 0x01cb }
    L_0x07f3:
        r30.zzawu();	 Catch:{ all -> 0x01cb }
        r2 = "_eid";
        r0 = r28;
        r2 = com.google.android.gms.internal.zzclq.zza(r0, r2);	 Catch:{ all -> 0x01cb }
        r2 = (java.lang.Long) r2;	 Catch:{ all -> 0x01cb }
        if (r2 == 0) goto L_0x0835;
    L_0x0803:
        r4 = 1;
    L_0x0804:
        r4 = java.lang.Boolean.valueOf(r4);	 Catch:{ all -> 0x01cb }
        r5 = 1;
        r0 = r19;
        if (r0 != r5) goto L_0x0837;
    L_0x080d:
        r2 = r18 + 1;
        r24[r18] = r28;	 Catch:{ all -> 0x01cb }
        r4 = r4.booleanValue();	 Catch:{ all -> 0x01cb }
        if (r4 == 0) goto L_0x074d;
    L_0x0817:
        r4 = r3.zzizo;	 Catch:{ all -> 0x01cb }
        if (r4 != 0) goto L_0x0823;
    L_0x081b:
        r4 = r3.zzizp;	 Catch:{ all -> 0x01cb }
        if (r4 != 0) goto L_0x0823;
    L_0x081f:
        r4 = r3.zzizq;	 Catch:{ all -> 0x01cb }
        if (r4 == 0) goto L_0x074d;
    L_0x0823:
        r4 = 0;
        r5 = 0;
        r6 = 0;
        r3 = r3.zza(r4, r5, r6);	 Catch:{ all -> 0x01cb }
        r0 = r28;
        r4 = r0.name;	 Catch:{ all -> 0x01cb }
        r0 = r23;
        r0.put(r4, r3);	 Catch:{ all -> 0x01cb }
        goto L_0x074d;
    L_0x0835:
        r4 = 0;
        goto L_0x0804;
    L_0x0837:
        r0 = r25;
        r1 = r19;
        r5 = r0.nextInt(r1);	 Catch:{ all -> 0x01cb }
        if (r5 != 0) goto L_0x0888;
    L_0x0841:
        r30.zzawu();	 Catch:{ all -> 0x01cb }
        r0 = r28;
        r2 = r0.zzjlh;	 Catch:{ all -> 0x01cb }
        r5 = "_sr";
        r0 = r19;
        r6 = (long) r0;	 Catch:{ all -> 0x01cb }
        r6 = java.lang.Long.valueOf(r6);	 Catch:{ all -> 0x01cb }
        r2 = com.google.android.gms.internal.zzclq.zza(r2, r5, r6);	 Catch:{ all -> 0x01cb }
        r0 = r28;
        r0.zzjlh = r2;	 Catch:{ all -> 0x01cb }
        r2 = r18 + 1;
        r24[r18] = r28;	 Catch:{ all -> 0x01cb }
        r4 = r4.booleanValue();	 Catch:{ all -> 0x01cb }
        if (r4 == 0) goto L_0x0871;
    L_0x0864:
        r4 = 0;
        r0 = r19;
        r6 = (long) r0;	 Catch:{ all -> 0x01cb }
        r5 = java.lang.Long.valueOf(r6);	 Catch:{ all -> 0x01cb }
        r6 = 0;
        r3 = r3.zza(r4, r5, r6);	 Catch:{ all -> 0x01cb }
    L_0x0871:
        r0 = r28;
        r4 = r0.name;	 Catch:{ all -> 0x01cb }
        r0 = r28;
        r5 = r0.zzjli;	 Catch:{ all -> 0x01cb }
        r6 = r5.longValue();	 Catch:{ all -> 0x01cb }
        r3 = r3.zzbc(r6);	 Catch:{ all -> 0x01cb }
        r0 = r23;
        r0.put(r4, r3);	 Catch:{ all -> 0x01cb }
        goto L_0x074d;
    L_0x0888:
        r6 = r3.zzizn;	 Catch:{ all -> 0x01cb }
        r0 = r28;
        r5 = r0.zzjli;	 Catch:{ all -> 0x01cb }
        r8 = r5.longValue();	 Catch:{ all -> 0x01cb }
        r6 = r8 - r6;
        r6 = java.lang.Math.abs(r6);	 Catch:{ all -> 0x01cb }
        r8 = 86400000; // 0x5265c00 float:7.82218E-36 double:4.2687272E-316;
        r5 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1));
        if (r5 < 0) goto L_0x0902;
    L_0x089f:
        r30.zzawu();	 Catch:{ all -> 0x01cb }
        r0 = r28;
        r2 = r0.zzjlh;	 Catch:{ all -> 0x01cb }
        r5 = "_efs";
        r6 = 1;
        r6 = java.lang.Long.valueOf(r6);	 Catch:{ all -> 0x01cb }
        r2 = com.google.android.gms.internal.zzclq.zza(r2, r5, r6);	 Catch:{ all -> 0x01cb }
        r0 = r28;
        r0.zzjlh = r2;	 Catch:{ all -> 0x01cb }
        r30.zzawu();	 Catch:{ all -> 0x01cb }
        r0 = r28;
        r2 = r0.zzjlh;	 Catch:{ all -> 0x01cb }
        r5 = "_sr";
        r0 = r19;
        r6 = (long) r0;	 Catch:{ all -> 0x01cb }
        r6 = java.lang.Long.valueOf(r6);	 Catch:{ all -> 0x01cb }
        r2 = com.google.android.gms.internal.zzclq.zza(r2, r5, r6);	 Catch:{ all -> 0x01cb }
        r0 = r28;
        r0.zzjlh = r2;	 Catch:{ all -> 0x01cb }
        r2 = r18 + 1;
        r24[r18] = r28;	 Catch:{ all -> 0x01cb }
        r4 = r4.booleanValue();	 Catch:{ all -> 0x01cb }
        if (r4 == 0) goto L_0x08eb;
    L_0x08da:
        r4 = 0;
        r0 = r19;
        r6 = (long) r0;	 Catch:{ all -> 0x01cb }
        r5 = java.lang.Long.valueOf(r6);	 Catch:{ all -> 0x01cb }
        r6 = 1;
        r6 = java.lang.Boolean.valueOf(r6);	 Catch:{ all -> 0x01cb }
        r3 = r3.zza(r4, r5, r6);	 Catch:{ all -> 0x01cb }
    L_0x08eb:
        r0 = r28;
        r4 = r0.name;	 Catch:{ all -> 0x01cb }
        r0 = r28;
        r5 = r0.zzjli;	 Catch:{ all -> 0x01cb }
        r6 = r5.longValue();	 Catch:{ all -> 0x01cb }
        r3 = r3.zzbc(r6);	 Catch:{ all -> 0x01cb }
        r0 = r23;
        r0.put(r4, r3);	 Catch:{ all -> 0x01cb }
        goto L_0x074d;
    L_0x0902:
        r4 = r4.booleanValue();	 Catch:{ all -> 0x01cb }
        if (r4 == 0) goto L_0x0917;
    L_0x0908:
        r0 = r28;
        r4 = r0.name;	 Catch:{ all -> 0x01cb }
        r5 = 0;
        r6 = 0;
        r2 = r3.zza(r2, r5, r6);	 Catch:{ all -> 0x01cb }
        r0 = r23;
        r0.put(r4, r2);	 Catch:{ all -> 0x01cb }
    L_0x0917:
        r2 = r18;
        goto L_0x074d;
    L_0x091b:
        r0 = r22;
        r2 = r0.zzjlp;	 Catch:{ all -> 0x01cb }
        r2 = r2.length;	 Catch:{ all -> 0x01cb }
        r0 = r18;
        if (r0 >= r2) goto L_0x0932;
    L_0x0924:
        r0 = r24;
        r1 = r18;
        r2 = java.util.Arrays.copyOf(r0, r1);	 Catch:{ all -> 0x01cb }
        r2 = (com.google.android.gms.internal.zzcmb[]) r2;	 Catch:{ all -> 0x01cb }
        r0 = r22;
        r0.zzjlp = r2;	 Catch:{ all -> 0x01cb }
    L_0x0932:
        r2 = r23.entrySet();	 Catch:{ all -> 0x01cb }
        r3 = r2.iterator();	 Catch:{ all -> 0x01cb }
    L_0x093a:
        r2 = r3.hasNext();	 Catch:{ all -> 0x01cb }
        if (r2 == 0) goto L_0x0954;
    L_0x0940:
        r2 = r3.next();	 Catch:{ all -> 0x01cb }
        r2 = (java.util.Map.Entry) r2;	 Catch:{ all -> 0x01cb }
        r4 = r30.zzaws();	 Catch:{ all -> 0x01cb }
        r2 = r2.getValue();	 Catch:{ all -> 0x01cb }
        r2 = (com.google.android.gms.internal.zzcgw) r2;	 Catch:{ all -> 0x01cb }
        r4.zza(r2);	 Catch:{ all -> 0x01cb }
        goto L_0x093a;
    L_0x0954:
        r2 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
        r2 = java.lang.Long.valueOf(r2);	 Catch:{ all -> 0x01cb }
        r0 = r22;
        r0.zzjls = r2;	 Catch:{ all -> 0x01cb }
        r2 = -9223372036854775808;
        r2 = java.lang.Long.valueOf(r2);	 Catch:{ all -> 0x01cb }
        r0 = r22;
        r0.zzjlt = r2;	 Catch:{ all -> 0x01cb }
        r2 = 0;
    L_0x096c:
        r0 = r22;
        r3 = r0.zzjlp;	 Catch:{ all -> 0x01cb }
        r3 = r3.length;	 Catch:{ all -> 0x01cb }
        if (r2 >= r3) goto L_0x09ac;
    L_0x0973:
        r0 = r22;
        r3 = r0.zzjlp;	 Catch:{ all -> 0x01cb }
        r3 = r3[r2];	 Catch:{ all -> 0x01cb }
        r4 = r3.zzjli;	 Catch:{ all -> 0x01cb }
        r4 = r4.longValue();	 Catch:{ all -> 0x01cb }
        r0 = r22;
        r6 = r0.zzjls;	 Catch:{ all -> 0x01cb }
        r6 = r6.longValue();	 Catch:{ all -> 0x01cb }
        r4 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r4 >= 0) goto L_0x0991;
    L_0x098b:
        r4 = r3.zzjli;	 Catch:{ all -> 0x01cb }
        r0 = r22;
        r0.zzjls = r4;	 Catch:{ all -> 0x01cb }
    L_0x0991:
        r4 = r3.zzjli;	 Catch:{ all -> 0x01cb }
        r4 = r4.longValue();	 Catch:{ all -> 0x01cb }
        r0 = r22;
        r6 = r0.zzjlt;	 Catch:{ all -> 0x01cb }
        r6 = r6.longValue();	 Catch:{ all -> 0x01cb }
        r4 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r4 <= 0) goto L_0x09a9;
    L_0x09a3:
        r3 = r3.zzjli;	 Catch:{ all -> 0x01cb }
        r0 = r22;
        r0.zzjlt = r3;	 Catch:{ all -> 0x01cb }
    L_0x09a9:
        r2 = r2 + 1;
        goto L_0x096c;
    L_0x09ac:
        r0 = r21;
        r2 = r0.zzjgi;	 Catch:{ all -> 0x01cb }
        r6 = r2.zzcn;	 Catch:{ all -> 0x01cb }
        r2 = r30.zzaws();	 Catch:{ all -> 0x01cb }
        r7 = r2.zzjb(r6);	 Catch:{ all -> 0x01cb }
        if (r7 != 0) goto L_0x0a42;
    L_0x09bc:
        r2 = r30.zzawy();	 Catch:{ all -> 0x01cb }
        r2 = r2.zzazd();	 Catch:{ all -> 0x01cb }
        r3 = "Bundling raw events w/o app info. appId";
        r0 = r21;
        r4 = r0.zzjgi;	 Catch:{ all -> 0x01cb }
        r4 = r4.zzcn;	 Catch:{ all -> 0x01cb }
        r4 = com.google.android.gms.internal.zzchm.zzjk(r4);	 Catch:{ all -> 0x01cb }
        r2.zzj(r3, r4);	 Catch:{ all -> 0x01cb }
    L_0x09d4:
        r0 = r22;
        r2 = r0.zzjlp;	 Catch:{ all -> 0x01cb }
        r2 = r2.length;	 Catch:{ all -> 0x01cb }
        if (r2 <= 0) goto L_0x0a10;
    L_0x09db:
        r2 = r30.zzawv();	 Catch:{ all -> 0x01cb }
        r0 = r21;
        r3 = r0.zzjgi;	 Catch:{ all -> 0x01cb }
        r3 = r3.zzcn;	 Catch:{ all -> 0x01cb }
        r2 = r2.zzjs(r3);	 Catch:{ all -> 0x01cb }
        if (r2 == 0) goto L_0x09ef;
    L_0x09eb:
        r3 = r2.zzjkw;	 Catch:{ all -> 0x01cb }
        if (r3 != 0) goto L_0x0ac8;
    L_0x09ef:
        r0 = r21;
        r2 = r0.zzjgi;	 Catch:{ all -> 0x01cb }
        r2 = r2.zzixs;	 Catch:{ all -> 0x01cb }
        r2 = android.text.TextUtils.isEmpty(r2);	 Catch:{ all -> 0x01cb }
        if (r2 == 0) goto L_0x0aae;
    L_0x09fb:
        r2 = -1;
        r2 = java.lang.Long.valueOf(r2);	 Catch:{ all -> 0x01cb }
        r0 = r22;
        r0.zzjmn = r2;	 Catch:{ all -> 0x01cb }
    L_0x0a05:
        r2 = r30.zzaws();	 Catch:{ all -> 0x01cb }
        r0 = r22;
        r1 = r17;
        r2.zza(r0, r1);	 Catch:{ all -> 0x01cb }
    L_0x0a10:
        r2 = r30.zzaws();	 Catch:{ all -> 0x01cb }
        r0 = r21;
        r3 = r0.zzjgj;	 Catch:{ all -> 0x01cb }
        r2.zzah(r3);	 Catch:{ all -> 0x01cb }
        r3 = r30.zzaws();	 Catch:{ all -> 0x01cb }
        r2 = r3.getWritableDatabase();	 Catch:{ all -> 0x01cb }
        r4 = "delete from raw_events_metadata where app_id=? and metadata_fingerprint not in (select distinct metadata_fingerprint from raw_events where app_id=?)";
        r5 = 2;
        r5 = new java.lang.String[r5];	 Catch:{ SQLiteException -> 0x0ad0 }
        r7 = 0;
        r5[r7] = r6;	 Catch:{ SQLiteException -> 0x0ad0 }
        r7 = 1;
        r5[r7] = r6;	 Catch:{ SQLiteException -> 0x0ad0 }
        r2.execSQL(r4, r5);	 Catch:{ SQLiteException -> 0x0ad0 }
    L_0x0a32:
        r2 = r30.zzaws();	 Catch:{ all -> 0x01cb }
        r2.setTransactionSuccessful();	 Catch:{ all -> 0x01cb }
        r2 = r30.zzaws();
        r2.endTransaction();
        r2 = 1;
    L_0x0a41:
        return r2;
    L_0x0a42:
        r0 = r22;
        r2 = r0.zzjlp;	 Catch:{ all -> 0x01cb }
        r2 = r2.length;	 Catch:{ all -> 0x01cb }
        if (r2 <= 0) goto L_0x09d4;
    L_0x0a49:
        r2 = r7.zzaxf();	 Catch:{ all -> 0x01cb }
        r4 = 0;
        r4 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
        if (r4 == 0) goto L_0x0aaa;
    L_0x0a53:
        r4 = java.lang.Long.valueOf(r2);	 Catch:{ all -> 0x01cb }
    L_0x0a57:
        r0 = r22;
        r0.zzjlv = r4;	 Catch:{ all -> 0x01cb }
        r4 = r7.zzaxe();	 Catch:{ all -> 0x01cb }
        r8 = 0;
        r8 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1));
        if (r8 != 0) goto L_0x0b01;
    L_0x0a65:
        r4 = 0;
        r4 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
        if (r4 == 0) goto L_0x0aac;
    L_0x0a6b:
        r2 = java.lang.Long.valueOf(r2);	 Catch:{ all -> 0x01cb }
    L_0x0a6f:
        r0 = r22;
        r0.zzjlu = r2;	 Catch:{ all -> 0x01cb }
        r7.zzaxo();	 Catch:{ all -> 0x01cb }
        r2 = r7.zzaxl();	 Catch:{ all -> 0x01cb }
        r2 = (int) r2;	 Catch:{ all -> 0x01cb }
        r2 = java.lang.Integer.valueOf(r2);	 Catch:{ all -> 0x01cb }
        r0 = r22;
        r0.zzjmg = r2;	 Catch:{ all -> 0x01cb }
        r0 = r22;
        r2 = r0.zzjls;	 Catch:{ all -> 0x01cb }
        r2 = r2.longValue();	 Catch:{ all -> 0x01cb }
        r7.zzal(r2);	 Catch:{ all -> 0x01cb }
        r0 = r22;
        r2 = r0.zzjlt;	 Catch:{ all -> 0x01cb }
        r2 = r2.longValue();	 Catch:{ all -> 0x01cb }
        r7.zzam(r2);	 Catch:{ all -> 0x01cb }
        r2 = r7.zzaxw();	 Catch:{ all -> 0x01cb }
        r0 = r22;
        r0.zzixw = r2;	 Catch:{ all -> 0x01cb }
        r2 = r30.zzaws();	 Catch:{ all -> 0x01cb }
        r2.zza(r7);	 Catch:{ all -> 0x01cb }
        goto L_0x09d4;
    L_0x0aaa:
        r4 = 0;
        goto L_0x0a57;
    L_0x0aac:
        r2 = 0;
        goto L_0x0a6f;
    L_0x0aae:
        r2 = r30.zzawy();	 Catch:{ all -> 0x01cb }
        r2 = r2.zzazf();	 Catch:{ all -> 0x01cb }
        r3 = "Did not find measurement config or missing version info. appId";
        r0 = r21;
        r4 = r0.zzjgi;	 Catch:{ all -> 0x01cb }
        r4 = r4.zzcn;	 Catch:{ all -> 0x01cb }
        r4 = com.google.android.gms.internal.zzchm.zzjk(r4);	 Catch:{ all -> 0x01cb }
        r2.zzj(r3, r4);	 Catch:{ all -> 0x01cb }
        goto L_0x0a05;
    L_0x0ac8:
        r2 = r2.zzjkw;	 Catch:{ all -> 0x01cb }
        r0 = r22;
        r0.zzjmn = r2;	 Catch:{ all -> 0x01cb }
        goto L_0x0a05;
    L_0x0ad0:
        r2 = move-exception;
        r3 = r3.zzawy();	 Catch:{ all -> 0x01cb }
        r3 = r3.zzazd();	 Catch:{ all -> 0x01cb }
        r4 = "Failed to remove unused event metadata. appId";
        r5 = com.google.android.gms.internal.zzchm.zzjk(r6);	 Catch:{ all -> 0x01cb }
        r3.zze(r4, r5, r2);	 Catch:{ all -> 0x01cb }
        goto L_0x0a32;
    L_0x0ae5:
        r2 = r30.zzaws();	 Catch:{ all -> 0x01cb }
        r2.setTransactionSuccessful();	 Catch:{ all -> 0x01cb }
        r2 = r30.zzaws();
        r2.endTransaction();
        r2 = 0;
        goto L_0x0a41;
    L_0x0af6:
        r2 = move-exception;
        r3 = r11;
        goto L_0x0372;
    L_0x0afa:
        r2 = move-exception;
        goto L_0x02ff;
    L_0x0afd:
        r2 = move-exception;
        r4 = r12;
        goto L_0x02ff;
    L_0x0b01:
        r2 = r4;
        goto L_0x0a65;
    L_0x0b04:
        r3 = r2;
        goto L_0x07f3;
    L_0x0b07:
        r19 = r2;
        goto L_0x077b;
    L_0x0b0b:
        r3 = r4;
        r4 = r5;
        goto L_0x05c8;
    L_0x0b0f:
        r17 = r2;
        goto L_0x054d;
    L_0x0b13:
        r2 = r4;
        goto L_0x03f7;
    L_0x0b16:
        r2 = r12;
        r3 = r17;
        goto L_0x015e;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzcim.zzg(java.lang.String, long):boolean");
    }

    private final zzcgi zzjw(String str) {
        zzcgh zzjb = zzaws().zzjb(str);
        if (zzjb == null || TextUtils.isEmpty(zzjb.zzvj())) {
            zzawy().zzazi().zzj("No app data available; dropping", str);
            return null;
        }
        try {
            String str2 = zzbhf.zzdb(this.mContext).getPackageInfo(str, 0).versionName;
            if (!(zzjb.zzvj() == null || zzjb.zzvj().equals(str2))) {
                zzawy().zzazf().zzj("App version does not match; dropping. appId", zzchm.zzjk(str));
                return null;
            }
        } catch (NameNotFoundException e) {
        }
        return new zzcgi(str, zzjb.getGmpAppId(), zzjb.zzvj(), zzjb.zzaxg(), zzjb.zzaxh(), zzjb.zzaxi(), zzjb.zzaxj(), null, zzjb.zzaxk(), false, zzjb.zzaxd(), zzjb.zzaxx(), 0, 0, zzjb.zzaxy());
    }

    public final Context getContext() {
        return this.mContext;
    }

    public final boolean isEnabled() {
        boolean z = false;
        zzawx().zzve();
        zzxf();
        if (this.zzjew.zzaya()) {
            return false;
        }
        Boolean zziy = this.zzjew.zziy("firebase_analytics_collection_enabled");
        if (zziy != null) {
            z = zziy.booleanValue();
        } else if (!zzbz.zzaji()) {
            z = true;
        }
        return zzawz().zzbn(z);
    }

    protected final void start() {
        zzawx().zzve();
        zzaws().zzayh();
        if (zzawz().zzjcr.get() == 0) {
            zzawz().zzjcr.set(this.zzata.currentTimeMillis());
        }
        if (Long.valueOf(zzawz().zzjcw.get()).longValue() == 0) {
            zzawy().zzazj().zzj("Persisting first open", Long.valueOf(this.zzjgg));
            zzawz().zzjcw.set(this.zzjgg);
        }
        if (zzazv()) {
            if (!TextUtils.isEmpty(zzawn().getGmpAppId())) {
                String zzazm = zzawz().zzazm();
                if (zzazm == null) {
                    zzawz().zzjo(zzawn().getGmpAppId());
                } else if (!zzazm.equals(zzawn().getGmpAppId())) {
                    zzawy().zzazh().log("Rechecking which service to use due to a GMP App Id change");
                    zzawz().zzazp();
                    this.zzjfk.disconnect();
                    this.zzjfk.zzyc();
                    zzawz().zzjo(zzawn().getGmpAppId());
                    zzawz().zzjcw.set(this.zzjgg);
                    zzawz().zzjcx.zzjq(null);
                }
            }
            zzawm().zzjp(zzawz().zzjcx.zzazr());
            if (!TextUtils.isEmpty(zzawn().getGmpAppId())) {
                zzcjk zzawm = zzawm();
                zzawm.zzve();
                zzawm.zzxf();
                if (zzawm.zziwf.zzazv()) {
                    zzawm.zzawp().zzbar();
                    String zzazq = zzawm.zzawz().zzazq();
                    if (!TextUtils.isEmpty(zzazq)) {
                        zzawm.zzawo().zzxf();
                        if (!zzazq.equals(VERSION.RELEASE)) {
                            Bundle bundle = new Bundle();
                            bundle.putString("_po", zzazq);
                            zzawm.zzc("auto", "_ou", bundle);
                        }
                    }
                }
                zzawp().zza(new AtomicReference());
            }
        } else if (isEnabled()) {
            if (!zzawu().zzeb("android.permission.INTERNET")) {
                zzawy().zzazd().log("App is missing INTERNET permission");
            }
            if (!zzawu().zzeb("android.permission.ACCESS_NETWORK_STATE")) {
                zzawy().zzazd().log("App is missing ACCESS_NETWORK_STATE permission");
            }
            if (!zzbhf.zzdb(this.mContext).zzamu()) {
                if (!zzcid.zzbk(this.mContext)) {
                    zzawy().zzazd().log("AppMeasurementReceiver not registered/enabled");
                }
                if (!zzcla.zzk(this.mContext, false)) {
                    zzawy().zzazd().log("AppMeasurementService not registered/enabled");
                }
            }
            zzawy().zzazd().log("Uploading is not possible. App measurement disabled");
        }
        zzbaj();
    }

    protected final void zza(int i, Throwable th, byte[] bArr) {
        zzawx().zzve();
        zzxf();
        if (bArr == null) {
            try {
                bArr = new byte[0];
            } catch (Throwable th2) {
                this.zzjge = false;
                zzban();
            }
        }
        List<Long> list = this.zzjfx;
        this.zzjfx = null;
        if ((i == Callback.DEFAULT_DRAG_ANIMATION_DURATION || i == 204) && th == null) {
            try {
                zzawz().zzjcr.set(this.zzata.currentTimeMillis());
                zzawz().zzjcs.set(0);
                zzbaj();
                zzawy().zzazj().zze("Successful upload. Got network response. code, size", Integer.valueOf(i), Integer.valueOf(bArr.length));
                zzaws().beginTransaction();
                zzcjk zzaws;
                try {
                    for (Long l : list) {
                        zzaws = zzaws();
                        long longValue = l.longValue();
                        zzaws.zzve();
                        zzaws.zzxf();
                        if (zzaws.getWritableDatabase().delete("queue", "rowid=?", new String[]{String.valueOf(longValue)}) != 1) {
                            throw new SQLiteException("Deleted fewer rows from queue than expected");
                        }
                    }
                    zzaws().setTransactionSuccessful();
                    zzaws().endTransaction();
                    if (zzbab().zzzs() && zzbai()) {
                        zzbah();
                    } else {
                        this.zzjgb = -1;
                        zzbaj();
                    }
                    this.zzjgc = 0;
                } catch (SQLiteException e) {
                    zzaws.zzawy().zzazd().zzj("Failed to delete a bundle in a queue table", e);
                    throw e;
                } catch (Throwable th3) {
                    zzaws().endTransaction();
                }
            } catch (SQLiteException e2) {
                zzawy().zzazd().zzj("Database error while trying to delete uploaded bundles", e2);
                this.zzjgc = this.zzata.elapsedRealtime();
                zzawy().zzazj().zzj("Disable upload, time", Long.valueOf(this.zzjgc));
            }
        } else {
            zzawy().zzazj().zze("Network upload failed. Will retry later. code, error", Integer.valueOf(i), th);
            zzawz().zzjcs.set(this.zzata.currentTimeMillis());
            boolean z = i == 503 || i == 429;
            if (z) {
                zzawz().zzjct.set(this.zzata.currentTimeMillis());
            }
            zzbaj();
        }
        this.zzjge = false;
        zzban();
    }

    public final byte[] zza(zzcha com_google_android_gms_internal_zzcha, String str) {
        zzxf();
        zzawx().zzve();
        zzawi();
        zzbq.checkNotNull(com_google_android_gms_internal_zzcha);
        zzbq.zzgm(str);
        zzfjs com_google_android_gms_internal_zzcmd = new zzcmd();
        zzaws().beginTransaction();
        try {
            zzcgh zzjb = zzaws().zzjb(str);
            byte[] bArr;
            if (zzjb == null) {
                zzawy().zzazi().zzj("Log and bundle not available. package_name", str);
                bArr = new byte[0];
                return bArr;
            } else if (zzjb.zzaxk()) {
                long j;
                if (("_iap".equals(com_google_android_gms_internal_zzcha.name) || "ecommerce_purchase".equals(com_google_android_gms_internal_zzcha.name)) && !zza(str, com_google_android_gms_internal_zzcha)) {
                    zzawy().zzazf().zzj("Failed to handle purchase event at single event bundle creation. appId", zzchm.zzjk(str));
                }
                zzcme com_google_android_gms_internal_zzcme = new zzcme();
                com_google_android_gms_internal_zzcmd.zzjlm = new zzcme[]{com_google_android_gms_internal_zzcme};
                com_google_android_gms_internal_zzcme.zzjlo = Integer.valueOf(1);
                com_google_android_gms_internal_zzcme.zzjlw = "android";
                com_google_android_gms_internal_zzcme.zzcn = zzjb.getAppId();
                com_google_android_gms_internal_zzcme.zzixt = zzjb.zzaxh();
                com_google_android_gms_internal_zzcme.zzifm = zzjb.zzvj();
                long zzaxg = zzjb.zzaxg();
                com_google_android_gms_internal_zzcme.zzjmj = zzaxg == -2147483648L ? null : Integer.valueOf((int) zzaxg);
                com_google_android_gms_internal_zzcme.zzjma = Long.valueOf(zzjb.zzaxi());
                com_google_android_gms_internal_zzcme.zzixs = zzjb.getGmpAppId();
                com_google_android_gms_internal_zzcme.zzjmf = Long.valueOf(zzjb.zzaxj());
                if (isEnabled() && zzcgn.zzaye() && this.zzjew.zziz(com_google_android_gms_internal_zzcme.zzcn)) {
                    zzawn();
                    com_google_android_gms_internal_zzcme.zzjmo = null;
                }
                Pair zzjm = zzawz().zzjm(zzjb.getAppId());
                if (!(!zzjb.zzaxy() || zzjm == null || TextUtils.isEmpty((CharSequence) zzjm.first))) {
                    com_google_android_gms_internal_zzcme.zzjmc = (String) zzjm.first;
                    com_google_android_gms_internal_zzcme.zzjmd = (Boolean) zzjm.second;
                }
                zzawo().zzxf();
                com_google_android_gms_internal_zzcme.zzjlx = Build.MODEL;
                zzawo().zzxf();
                com_google_android_gms_internal_zzcme.zzdb = VERSION.RELEASE;
                com_google_android_gms_internal_zzcme.zzjlz = Integer.valueOf((int) zzawo().zzayu());
                com_google_android_gms_internal_zzcme.zzjly = zzawo().zzayv();
                com_google_android_gms_internal_zzcme.zzjme = zzjb.getAppInstanceId();
                com_google_android_gms_internal_zzcme.zziya = zzjb.zzaxd();
                List zzja = zzaws().zzja(zzjb.getAppId());
                com_google_android_gms_internal_zzcme.zzjlq = new zzcmg[zzja.size()];
                for (int i = 0; i < zzja.size(); i++) {
                    zzcmg com_google_android_gms_internal_zzcmg = new zzcmg();
                    com_google_android_gms_internal_zzcme.zzjlq[i] = com_google_android_gms_internal_zzcmg;
                    com_google_android_gms_internal_zzcmg.name = ((zzclp) zzja.get(i)).mName;
                    com_google_android_gms_internal_zzcmg.zzjms = Long.valueOf(((zzclp) zzja.get(i)).zzjjm);
                    zzawu().zza(com_google_android_gms_internal_zzcmg, ((zzclp) zzja.get(i)).mValue);
                }
                Bundle zzayx = com_google_android_gms_internal_zzcha.zzizt.zzayx();
                if ("_iap".equals(com_google_android_gms_internal_zzcha.name)) {
                    zzayx.putLong("_c", 1);
                    zzawy().zzazi().log("Marking in-app purchase as real-time");
                    zzayx.putLong("_r", 1);
                }
                zzayx.putString("_o", com_google_android_gms_internal_zzcha.zziyf);
                if (zzawu().zzkj(com_google_android_gms_internal_zzcme.zzcn)) {
                    zzawu().zza(zzayx, "_dbg", Long.valueOf(1));
                    zzawu().zza(zzayx, "_r", Long.valueOf(1));
                }
                zzcgw zzae = zzaws().zzae(str, com_google_android_gms_internal_zzcha.name);
                if (zzae == null) {
                    zzaws().zza(new zzcgw(str, com_google_android_gms_internal_zzcha.name, 1, 0, com_google_android_gms_internal_zzcha.zzizu, 0, null, null, null));
                    j = 0;
                } else {
                    j = zzae.zzizm;
                    zzaws().zza(zzae.zzbb(com_google_android_gms_internal_zzcha.zzizu).zzayw());
                }
                zzcgv com_google_android_gms_internal_zzcgv = new zzcgv(this, com_google_android_gms_internal_zzcha.zziyf, str, com_google_android_gms_internal_zzcha.name, com_google_android_gms_internal_zzcha.zzizu, j, zzayx);
                zzcmb com_google_android_gms_internal_zzcmb = new zzcmb();
                com_google_android_gms_internal_zzcme.zzjlp = new zzcmb[]{com_google_android_gms_internal_zzcmb};
                com_google_android_gms_internal_zzcmb.zzjli = Long.valueOf(com_google_android_gms_internal_zzcgv.zzfij);
                com_google_android_gms_internal_zzcmb.name = com_google_android_gms_internal_zzcgv.mName;
                com_google_android_gms_internal_zzcmb.zzjlj = Long.valueOf(com_google_android_gms_internal_zzcgv.zzizi);
                com_google_android_gms_internal_zzcmb.zzjlh = new zzcmc[com_google_android_gms_internal_zzcgv.zzizj.size()];
                Iterator it = com_google_android_gms_internal_zzcgv.zzizj.iterator();
                int i2 = 0;
                while (it.hasNext()) {
                    String str2 = (String) it.next();
                    zzcmc com_google_android_gms_internal_zzcmc = new zzcmc();
                    int i3 = i2 + 1;
                    com_google_android_gms_internal_zzcmb.zzjlh[i2] = com_google_android_gms_internal_zzcmc;
                    com_google_android_gms_internal_zzcmc.name = str2;
                    zzawu().zza(com_google_android_gms_internal_zzcmc, com_google_android_gms_internal_zzcgv.zzizj.get(str2));
                    i2 = i3;
                }
                com_google_android_gms_internal_zzcme.zzjmi = zza(zzjb.getAppId(), com_google_android_gms_internal_zzcme.zzjlq, com_google_android_gms_internal_zzcme.zzjlp);
                com_google_android_gms_internal_zzcme.zzjls = com_google_android_gms_internal_zzcmb.zzjli;
                com_google_android_gms_internal_zzcme.zzjlt = com_google_android_gms_internal_zzcmb.zzjli;
                zzaxg = zzjb.zzaxf();
                com_google_android_gms_internal_zzcme.zzjlv = zzaxg != 0 ? Long.valueOf(zzaxg) : null;
                long zzaxe = zzjb.zzaxe();
                if (zzaxe != 0) {
                    zzaxg = zzaxe;
                }
                com_google_android_gms_internal_zzcme.zzjlu = zzaxg != 0 ? Long.valueOf(zzaxg) : null;
                zzjb.zzaxo();
                com_google_android_gms_internal_zzcme.zzjmg = Integer.valueOf((int) zzjb.zzaxl());
                com_google_android_gms_internal_zzcme.zzjmb = Long.valueOf(11910);
                com_google_android_gms_internal_zzcme.zzjlr = Long.valueOf(this.zzata.currentTimeMillis());
                com_google_android_gms_internal_zzcme.zzjmh = Boolean.TRUE;
                zzjb.zzal(com_google_android_gms_internal_zzcme.zzjls.longValue());
                zzjb.zzam(com_google_android_gms_internal_zzcme.zzjlt.longValue());
                zzaws().zza(zzjb);
                zzaws().setTransactionSuccessful();
                zzaws().endTransaction();
                try {
                    bArr = new byte[com_google_android_gms_internal_zzcmd.zzho()];
                    zzfjk zzo = zzfjk.zzo(bArr, 0, bArr.length);
                    com_google_android_gms_internal_zzcmd.zza(zzo);
                    zzo.zzcwt();
                    return zzawu().zzq(bArr);
                } catch (IOException e) {
                    zzawy().zzazd().zze("Data loss. Failed to bundle and serialize. appId", zzchm.zzjk(str), e);
                    return null;
                }
            } else {
                zzawy().zzazi().zzj("Log and bundle disabled. package_name", str);
                bArr = new byte[0];
                zzaws().endTransaction();
                return bArr;
            }
        } finally {
            zzaws().endTransaction();
        }
    }

    public final zzcgd zzawk() {
        zza(this.zzjfr);
        return this.zzjfr;
    }

    public final zzcgk zzawl() {
        zza(this.zzjfq);
        return this.zzjfq;
    }

    public final zzcjn zzawm() {
        zza(this.zzjfm);
        return this.zzjfm;
    }

    public final zzchh zzawn() {
        zza(this.zzjfn);
        return this.zzjfn;
    }

    public final zzcgu zzawo() {
        zza(this.zzjfl);
        return this.zzjfl;
    }

    public final zzckg zzawp() {
        zza(this.zzjfk);
        return this.zzjfk;
    }

    public final zzckc zzawq() {
        zza(this.zzjfj);
        return this.zzjfj;
    }

    public final zzchi zzawr() {
        zza(this.zzjfh);
        return this.zzjfh;
    }

    public final zzcgo zzaws() {
        zza(this.zzjfg);
        return this.zzjfg;
    }

    public final zzchk zzawt() {
        zza(this.zzjff);
        return this.zzjff;
    }

    public final zzclq zzawu() {
        zza(this.zzjfe);
        return this.zzjfe;
    }

    public final zzcig zzawv() {
        zza(this.zzjfb);
        return this.zzjfb;
    }

    public final zzclf zzaww() {
        zza(this.zzjfa);
        return this.zzjfa;
    }

    public final zzcih zzawx() {
        zza(this.zzjez);
        return this.zzjez;
    }

    public final zzchm zzawy() {
        zza(this.zzjey);
        return this.zzjey;
    }

    public final zzchx zzawz() {
        zza(this.zzjex);
        return this.zzjex;
    }

    public final zzcgn zzaxa() {
        return this.zzjew;
    }

    protected final boolean zzazv() {
        boolean z = false;
        zzxf();
        zzawx().zzve();
        if (this.zzjft == null || this.zzjfu == 0 || !(this.zzjft == null || this.zzjft.booleanValue() || Math.abs(this.zzata.elapsedRealtime() - this.zzjfu) <= 1000)) {
            this.zzjfu = this.zzata.elapsedRealtime();
            if (zzawu().zzeb("android.permission.INTERNET") && zzawu().zzeb("android.permission.ACCESS_NETWORK_STATE") && (zzbhf.zzdb(this.mContext).zzamu() || (zzcid.zzbk(this.mContext) && zzcla.zzk(this.mContext, false)))) {
                z = true;
            }
            this.zzjft = Boolean.valueOf(z);
            if (this.zzjft.booleanValue()) {
                this.zzjft = Boolean.valueOf(zzawu().zzkg(zzawn().getGmpAppId()));
            }
        }
        return this.zzjft.booleanValue();
    }

    public final zzchm zzazx() {
        return (this.zzjey == null || !this.zzjey.isInitialized()) ? null : this.zzjey;
    }

    final zzcih zzazy() {
        return this.zzjez;
    }

    public final AppMeasurement zzazz() {
        return this.zzjfc;
    }

    final void zzb(zzcgl com_google_android_gms_internal_zzcgl, zzcgi com_google_android_gms_internal_zzcgi) {
        boolean z = true;
        zzbq.checkNotNull(com_google_android_gms_internal_zzcgl);
        zzbq.zzgm(com_google_android_gms_internal_zzcgl.packageName);
        zzbq.checkNotNull(com_google_android_gms_internal_zzcgl.zziyf);
        zzbq.checkNotNull(com_google_android_gms_internal_zzcgl.zziyg);
        zzbq.zzgm(com_google_android_gms_internal_zzcgl.zziyg.name);
        zzawx().zzve();
        zzxf();
        if (!TextUtils.isEmpty(com_google_android_gms_internal_zzcgi.zzixs)) {
            if (com_google_android_gms_internal_zzcgi.zzixx) {
                zzcgl com_google_android_gms_internal_zzcgl2 = new zzcgl(com_google_android_gms_internal_zzcgl);
                com_google_android_gms_internal_zzcgl2.zziyi = false;
                zzaws().beginTransaction();
                try {
                    zzcgl zzah = zzaws().zzah(com_google_android_gms_internal_zzcgl2.packageName, com_google_android_gms_internal_zzcgl2.zziyg.name);
                    if (!(zzah == null || zzah.zziyf.equals(com_google_android_gms_internal_zzcgl2.zziyf))) {
                        zzawy().zzazf().zzd("Updating a conditional user property with different origin. name, origin, origin (from DB)", zzawt().zzjj(com_google_android_gms_internal_zzcgl2.zziyg.name), com_google_android_gms_internal_zzcgl2.zziyf, zzah.zziyf);
                    }
                    if (zzah != null && zzah.zziyi) {
                        com_google_android_gms_internal_zzcgl2.zziyf = zzah.zziyf;
                        com_google_android_gms_internal_zzcgl2.zziyh = zzah.zziyh;
                        com_google_android_gms_internal_zzcgl2.zziyl = zzah.zziyl;
                        com_google_android_gms_internal_zzcgl2.zziyj = zzah.zziyj;
                        com_google_android_gms_internal_zzcgl2.zziym = zzah.zziym;
                        com_google_android_gms_internal_zzcgl2.zziyi = zzah.zziyi;
                        com_google_android_gms_internal_zzcgl2.zziyg = new zzcln(com_google_android_gms_internal_zzcgl2.zziyg.name, zzah.zziyg.zzjji, com_google_android_gms_internal_zzcgl2.zziyg.getValue(), zzah.zziyg.zziyf);
                        z = false;
                    } else if (TextUtils.isEmpty(com_google_android_gms_internal_zzcgl2.zziyj)) {
                        com_google_android_gms_internal_zzcgl2.zziyg = new zzcln(com_google_android_gms_internal_zzcgl2.zziyg.name, com_google_android_gms_internal_zzcgl2.zziyh, com_google_android_gms_internal_zzcgl2.zziyg.getValue(), com_google_android_gms_internal_zzcgl2.zziyg.zziyf);
                        com_google_android_gms_internal_zzcgl2.zziyi = true;
                    } else {
                        z = false;
                    }
                    if (com_google_android_gms_internal_zzcgl2.zziyi) {
                        zzcln com_google_android_gms_internal_zzcln = com_google_android_gms_internal_zzcgl2.zziyg;
                        zzclp com_google_android_gms_internal_zzclp = new zzclp(com_google_android_gms_internal_zzcgl2.packageName, com_google_android_gms_internal_zzcgl2.zziyf, com_google_android_gms_internal_zzcln.name, com_google_android_gms_internal_zzcln.zzjji, com_google_android_gms_internal_zzcln.getValue());
                        if (zzaws().zza(com_google_android_gms_internal_zzclp)) {
                            zzawy().zzazi().zzd("User property updated immediately", com_google_android_gms_internal_zzcgl2.packageName, zzawt().zzjj(com_google_android_gms_internal_zzclp.mName), com_google_android_gms_internal_zzclp.mValue);
                        } else {
                            zzawy().zzazd().zzd("(2)Too many active user properties, ignoring", zzchm.zzjk(com_google_android_gms_internal_zzcgl2.packageName), zzawt().zzjj(com_google_android_gms_internal_zzclp.mName), com_google_android_gms_internal_zzclp.mValue);
                        }
                        if (z && com_google_android_gms_internal_zzcgl2.zziym != null) {
                            zzc(new zzcha(com_google_android_gms_internal_zzcgl2.zziym, com_google_android_gms_internal_zzcgl2.zziyh), com_google_android_gms_internal_zzcgi);
                        }
                    }
                    if (zzaws().zza(com_google_android_gms_internal_zzcgl2)) {
                        zzawy().zzazi().zzd("Conditional property added", com_google_android_gms_internal_zzcgl2.packageName, zzawt().zzjj(com_google_android_gms_internal_zzcgl2.zziyg.name), com_google_android_gms_internal_zzcgl2.zziyg.getValue());
                    } else {
                        zzawy().zzazd().zzd("Too many conditional properties, ignoring", zzchm.zzjk(com_google_android_gms_internal_zzcgl2.packageName), zzawt().zzjj(com_google_android_gms_internal_zzcgl2.zziyg.name), com_google_android_gms_internal_zzcgl2.zziyg.getValue());
                    }
                    zzaws().setTransactionSuccessful();
                } finally {
                    zzaws().endTransaction();
                }
            } else {
                zzg(com_google_android_gms_internal_zzcgi);
            }
        }
    }

    final void zzb(zzcha com_google_android_gms_internal_zzcha, zzcgi com_google_android_gms_internal_zzcgi) {
        zzbq.checkNotNull(com_google_android_gms_internal_zzcgi);
        zzbq.zzgm(com_google_android_gms_internal_zzcgi.packageName);
        zzawx().zzve();
        zzxf();
        String str = com_google_android_gms_internal_zzcgi.packageName;
        long j = com_google_android_gms_internal_zzcha.zzizu;
        zzawu();
        if (!zzclq.zzd(com_google_android_gms_internal_zzcha, com_google_android_gms_internal_zzcgi)) {
            return;
        }
        if (com_google_android_gms_internal_zzcgi.zzixx) {
            zzaws().beginTransaction();
            try {
                List emptyList;
                Object obj;
                zzcjk zzaws = zzaws();
                zzbq.zzgm(str);
                zzaws.zzve();
                zzaws.zzxf();
                if (j < 0) {
                    zzaws.zzawy().zzazf().zze("Invalid time querying timed out conditional properties", zzchm.zzjk(str), Long.valueOf(j));
                    emptyList = Collections.emptyList();
                } else {
                    emptyList = zzaws.zzc("active=0 and app_id=? and abs(? - creation_timestamp) > trigger_timeout", new String[]{str, String.valueOf(j)});
                }
                for (zzcgl com_google_android_gms_internal_zzcgl : r2) {
                    if (com_google_android_gms_internal_zzcgl != null) {
                        zzawy().zzazi().zzd("User property timed out", com_google_android_gms_internal_zzcgl.packageName, zzawt().zzjj(com_google_android_gms_internal_zzcgl.zziyg.name), com_google_android_gms_internal_zzcgl.zziyg.getValue());
                        if (com_google_android_gms_internal_zzcgl.zziyk != null) {
                            zzc(new zzcha(com_google_android_gms_internal_zzcgl.zziyk, j), com_google_android_gms_internal_zzcgi);
                        }
                        zzaws().zzai(str, com_google_android_gms_internal_zzcgl.zziyg.name);
                    }
                }
                zzaws = zzaws();
                zzbq.zzgm(str);
                zzaws.zzve();
                zzaws.zzxf();
                if (j < 0) {
                    zzaws.zzawy().zzazf().zze("Invalid time querying expired conditional properties", zzchm.zzjk(str), Long.valueOf(j));
                    emptyList = Collections.emptyList();
                } else {
                    emptyList = zzaws.zzc("active<>0 and app_id=? and abs(? - triggered_timestamp) > time_to_live", new String[]{str, String.valueOf(j)});
                }
                List arrayList = new ArrayList(r2.size());
                for (zzcgl com_google_android_gms_internal_zzcgl2 : r2) {
                    if (com_google_android_gms_internal_zzcgl2 != null) {
                        zzawy().zzazi().zzd("User property expired", com_google_android_gms_internal_zzcgl2.packageName, zzawt().zzjj(com_google_android_gms_internal_zzcgl2.zziyg.name), com_google_android_gms_internal_zzcgl2.zziyg.getValue());
                        zzaws().zzaf(str, com_google_android_gms_internal_zzcgl2.zziyg.name);
                        if (com_google_android_gms_internal_zzcgl2.zziyo != null) {
                            arrayList.add(com_google_android_gms_internal_zzcgl2.zziyo);
                        }
                        zzaws().zzai(str, com_google_android_gms_internal_zzcgl2.zziyg.name);
                    }
                }
                ArrayList arrayList2 = (ArrayList) arrayList;
                int size = arrayList2.size();
                int i = 0;
                while (i < size) {
                    obj = arrayList2.get(i);
                    i++;
                    zzc(new zzcha((zzcha) obj, j), com_google_android_gms_internal_zzcgi);
                }
                zzaws = zzaws();
                String str2 = com_google_android_gms_internal_zzcha.name;
                zzbq.zzgm(str);
                zzbq.zzgm(str2);
                zzaws.zzve();
                zzaws.zzxf();
                if (j < 0) {
                    zzaws.zzawy().zzazf().zzd("Invalid time querying triggered conditional properties", zzchm.zzjk(str), zzaws.zzawt().zzjh(str2), Long.valueOf(j));
                    emptyList = Collections.emptyList();
                } else {
                    emptyList = zzaws.zzc("active=0 and app_id=? and trigger_event_name=? and abs(? - creation_timestamp) <= trigger_timeout", new String[]{str, str2, String.valueOf(j)});
                }
                List arrayList3 = new ArrayList(r2.size());
                for (zzcgl com_google_android_gms_internal_zzcgl3 : r2) {
                    if (com_google_android_gms_internal_zzcgl3 != null) {
                        zzcln com_google_android_gms_internal_zzcln = com_google_android_gms_internal_zzcgl3.zziyg;
                        zzclp com_google_android_gms_internal_zzclp = new zzclp(com_google_android_gms_internal_zzcgl3.packageName, com_google_android_gms_internal_zzcgl3.zziyf, com_google_android_gms_internal_zzcln.name, j, com_google_android_gms_internal_zzcln.getValue());
                        if (zzaws().zza(com_google_android_gms_internal_zzclp)) {
                            zzawy().zzazi().zzd("User property triggered", com_google_android_gms_internal_zzcgl3.packageName, zzawt().zzjj(com_google_android_gms_internal_zzclp.mName), com_google_android_gms_internal_zzclp.mValue);
                        } else {
                            zzawy().zzazd().zzd("Too many active user properties, ignoring", zzchm.zzjk(com_google_android_gms_internal_zzcgl3.packageName), zzawt().zzjj(com_google_android_gms_internal_zzclp.mName), com_google_android_gms_internal_zzclp.mValue);
                        }
                        if (com_google_android_gms_internal_zzcgl3.zziym != null) {
                            arrayList3.add(com_google_android_gms_internal_zzcgl3.zziym);
                        }
                        com_google_android_gms_internal_zzcgl3.zziyg = new zzcln(com_google_android_gms_internal_zzclp);
                        com_google_android_gms_internal_zzcgl3.zziyi = true;
                        zzaws().zza(com_google_android_gms_internal_zzcgl3);
                    }
                }
                zzc(com_google_android_gms_internal_zzcha, com_google_android_gms_internal_zzcgi);
                arrayList2 = (ArrayList) arrayList3;
                int size2 = arrayList2.size();
                i = 0;
                while (i < size2) {
                    obj = arrayList2.get(i);
                    i++;
                    zzc(new zzcha((zzcha) obj, j), com_google_android_gms_internal_zzcgi);
                }
                zzaws().setTransactionSuccessful();
            } finally {
                zzaws().endTransaction();
            }
        } else {
            zzg(com_google_android_gms_internal_zzcgi);
        }
    }

    final void zzb(zzcha com_google_android_gms_internal_zzcha, String str) {
        zzcgh zzjb = zzaws().zzjb(str);
        if (zzjb == null || TextUtils.isEmpty(zzjb.zzvj())) {
            zzawy().zzazi().zzj("No app data available; dropping event", str);
            return;
        }
        try {
            String str2 = zzbhf.zzdb(this.mContext).getPackageInfo(str, 0).versionName;
            if (!(zzjb.zzvj() == null || zzjb.zzvj().equals(str2))) {
                zzawy().zzazf().zzj("App version does not match; dropping event. appId", zzchm.zzjk(str));
                return;
            }
        } catch (NameNotFoundException e) {
            if (!"_ui".equals(com_google_android_gms_internal_zzcha.name)) {
                zzawy().zzazf().zzj("Could not find package. appId", zzchm.zzjk(str));
            }
        }
        zzcha com_google_android_gms_internal_zzcha2 = com_google_android_gms_internal_zzcha;
        zzb(com_google_android_gms_internal_zzcha2, new zzcgi(str, zzjb.getGmpAppId(), zzjb.zzvj(), zzjb.zzaxg(), zzjb.zzaxh(), zzjb.zzaxi(), zzjb.zzaxj(), null, zzjb.zzaxk(), false, zzjb.zzaxd(), zzjb.zzaxx(), 0, 0, zzjb.zzaxy()));
    }

    final void zzb(zzcjl com_google_android_gms_internal_zzcjl) {
        this.zzjfz++;
    }

    final void zzb(zzcln com_google_android_gms_internal_zzcln, zzcgi com_google_android_gms_internal_zzcgi) {
        int i = 0;
        zzawx().zzve();
        zzxf();
        if (!TextUtils.isEmpty(com_google_android_gms_internal_zzcgi.zzixs)) {
            if (com_google_android_gms_internal_zzcgi.zzixx) {
                int zzkd = zzawu().zzkd(com_google_android_gms_internal_zzcln.name);
                String zza;
                if (zzkd != 0) {
                    zzawu();
                    zza = zzclq.zza(com_google_android_gms_internal_zzcln.name, 24, true);
                    if (com_google_android_gms_internal_zzcln.name != null) {
                        i = com_google_android_gms_internal_zzcln.name.length();
                    }
                    zzawu().zza(com_google_android_gms_internal_zzcgi.packageName, zzkd, "_ev", zza, i);
                    return;
                }
                zzkd = zzawu().zzl(com_google_android_gms_internal_zzcln.name, com_google_android_gms_internal_zzcln.getValue());
                if (zzkd != 0) {
                    zzawu();
                    zza = zzclq.zza(com_google_android_gms_internal_zzcln.name, 24, true);
                    Object value = com_google_android_gms_internal_zzcln.getValue();
                    if (value != null && ((value instanceof String) || (value instanceof CharSequence))) {
                        i = String.valueOf(value).length();
                    }
                    zzawu().zza(com_google_android_gms_internal_zzcgi.packageName, zzkd, "_ev", zza, i);
                    return;
                }
                Object zzm = zzawu().zzm(com_google_android_gms_internal_zzcln.name, com_google_android_gms_internal_zzcln.getValue());
                if (zzm != null) {
                    zzclp com_google_android_gms_internal_zzclp = new zzclp(com_google_android_gms_internal_zzcgi.packageName, com_google_android_gms_internal_zzcln.zziyf, com_google_android_gms_internal_zzcln.name, com_google_android_gms_internal_zzcln.zzjji, zzm);
                    zzawy().zzazi().zze("Setting user property", zzawt().zzjj(com_google_android_gms_internal_zzclp.mName), zzm);
                    zzaws().beginTransaction();
                    try {
                        zzg(com_google_android_gms_internal_zzcgi);
                        boolean zza2 = zzaws().zza(com_google_android_gms_internal_zzclp);
                        zzaws().setTransactionSuccessful();
                        if (zza2) {
                            zzawy().zzazi().zze("User property set", zzawt().zzjj(com_google_android_gms_internal_zzclp.mName), com_google_android_gms_internal_zzclp.mValue);
                        } else {
                            zzawy().zzazd().zze("Too many unique user properties are set. Ignoring user property", zzawt().zzjj(com_google_android_gms_internal_zzclp.mName), com_google_android_gms_internal_zzclp.mValue);
                            zzawu().zza(com_google_android_gms_internal_zzcgi.packageName, 9, null, null, 0);
                        }
                        zzaws().endTransaction();
                        return;
                    } catch (Throwable th) {
                        zzaws().endTransaction();
                    }
                } else {
                    return;
                }
            }
            zzg(com_google_android_gms_internal_zzcgi);
        }
    }

    final void zzb(String str, int i, Throwable th, byte[] bArr, Map<String, List<String>> map) {
        boolean z = true;
        zzawx().zzve();
        zzxf();
        zzbq.zzgm(str);
        if (bArr == null) {
            try {
                bArr = new byte[0];
            } catch (Throwable th2) {
                this.zzjgd = false;
                zzban();
            }
        }
        zzawy().zzazj().zzj("onConfigFetched. Response size", Integer.valueOf(bArr.length));
        zzaws().beginTransaction();
        zzcgh zzjb = zzaws().zzjb(str);
        boolean z2 = (i == Callback.DEFAULT_DRAG_ANIMATION_DURATION || i == 204 || i == 304) && th == null;
        if (zzjb == null) {
            zzawy().zzazf().zzj("App does not exist in onConfigFetched. appId", zzchm.zzjk(str));
        } else if (z2 || i == 404) {
            List list = map != null ? (List) map.get("Last-Modified") : null;
            String str2 = (list == null || list.size() <= 0) ? null : (String) list.get(0);
            if (i == 404 || i == 304) {
                if (zzawv().zzjs(str) == null && !zzawv().zzb(str, null, null)) {
                    zzaws().endTransaction();
                    this.zzjgd = false;
                    zzban();
                    return;
                }
            } else if (!zzawv().zzb(str, bArr, str2)) {
                zzaws().endTransaction();
                this.zzjgd = false;
                zzban();
                return;
            }
            zzjb.zzar(this.zzata.currentTimeMillis());
            zzaws().zza(zzjb);
            if (i == 404) {
                zzawy().zzazg().zzj("Config not found. Using empty config. appId", str);
            } else {
                zzawy().zzazj().zze("Successfully fetched config. Got network response. code, size", Integer.valueOf(i), Integer.valueOf(bArr.length));
            }
            if (zzbab().zzzs() && zzbai()) {
                zzbah();
            } else {
                zzbaj();
            }
        } else {
            zzjb.zzas(this.zzata.currentTimeMillis());
            zzaws().zza(zzjb);
            zzawy().zzazj().zze("Fetching config failed. code, error", Integer.valueOf(i), th);
            zzawv().zzju(str);
            zzawz().zzjcs.set(this.zzata.currentTimeMillis());
            if (!(i == 503 || i == 429)) {
                z = false;
            }
            if (z) {
                zzawz().zzjct.set(this.zzata.currentTimeMillis());
            }
            zzbaj();
        }
        zzaws().setTransactionSuccessful();
        zzaws().endTransaction();
        this.zzjgd = false;
        zzban();
    }

    public final FirebaseAnalytics zzbaa() {
        return this.zzjfd;
    }

    public final zzchq zzbab() {
        zza(this.zzjfi);
        return this.zzjfi;
    }

    final long zzbaf() {
        Long valueOf = Long.valueOf(zzawz().zzjcw.get());
        return valueOf.longValue() == 0 ? this.zzjgg : Math.min(this.zzjgg, valueOf.longValue());
    }

    public final void zzbah() {
        zzawx().zzve();
        zzxf();
        this.zzjgf = true;
        String zzayf;
        String str;
        try {
            Boolean zzbas = zzawp().zzbas();
            if (zzbas == null) {
                zzawy().zzazf().log("Upload data called on the client side before use of service was decided");
                this.zzjgf = false;
                zzban();
            } else if (zzbas.booleanValue()) {
                zzawy().zzazd().log("Upload called in the client side when service should be used");
                this.zzjgf = false;
                zzban();
            } else if (this.zzjgc > 0) {
                zzbaj();
                this.zzjgf = false;
                zzban();
            } else {
                zzawx().zzve();
                if ((this.zzjfx != null ? 1 : null) != null) {
                    zzawy().zzazj().log("Uploading requested multiple times");
                    this.zzjgf = false;
                    zzban();
                } else if (zzbab().zzzs()) {
                    long currentTimeMillis = this.zzata.currentTimeMillis();
                    zzg(null, currentTimeMillis - zzcgn.zzayc());
                    long j = zzawz().zzjcr.get();
                    if (j != 0) {
                        zzawy().zzazi().zzj("Uploading events. Elapsed time since last upload attempt (ms)", Long.valueOf(Math.abs(currentTimeMillis - j)));
                    }
                    zzayf = zzaws().zzayf();
                    Object zzba;
                    if (TextUtils.isEmpty(zzayf)) {
                        this.zzjgb = -1;
                        zzba = zzaws().zzba(currentTimeMillis - zzcgn.zzayc());
                        if (!TextUtils.isEmpty(zzba)) {
                            zzcgh zzjb = zzaws().zzjb(zzba);
                            if (zzjb != null) {
                                zzb(zzjb);
                            }
                        }
                    } else {
                        if (this.zzjgb == -1) {
                            this.zzjgb = zzaws().zzaym();
                        }
                        List<Pair> zzl = zzaws().zzl(zzayf, this.zzjew.zzb(zzayf, zzchc.zzjaj), Math.max(0, this.zzjew.zzb(zzayf, zzchc.zzjak)));
                        if (!zzl.isEmpty()) {
                            zzcme com_google_android_gms_internal_zzcme;
                            Object obj;
                            int i;
                            List subList;
                            for (Pair pair : zzl) {
                                com_google_android_gms_internal_zzcme = (zzcme) pair.first;
                                if (!TextUtils.isEmpty(com_google_android_gms_internal_zzcme.zzjmc)) {
                                    obj = com_google_android_gms_internal_zzcme.zzjmc;
                                    break;
                                }
                            }
                            obj = null;
                            if (obj != null) {
                                for (i = 0; i < zzl.size(); i++) {
                                    com_google_android_gms_internal_zzcme = (zzcme) ((Pair) zzl.get(i)).first;
                                    if (!TextUtils.isEmpty(com_google_android_gms_internal_zzcme.zzjmc) && !com_google_android_gms_internal_zzcme.zzjmc.equals(obj)) {
                                        subList = zzl.subList(0, i);
                                        break;
                                    }
                                }
                            }
                            subList = zzl;
                            zzcmd com_google_android_gms_internal_zzcmd = new zzcmd();
                            com_google_android_gms_internal_zzcmd.zzjlm = new zzcme[subList.size()];
                            Collection arrayList = new ArrayList(subList.size());
                            Object obj2 = (zzcgn.zzaye() && this.zzjew.zziz(zzayf)) ? 1 : null;
                            for (i = 0; i < com_google_android_gms_internal_zzcmd.zzjlm.length; i++) {
                                com_google_android_gms_internal_zzcmd.zzjlm[i] = (zzcme) ((Pair) subList.get(i)).first;
                                arrayList.add((Long) ((Pair) subList.get(i)).second);
                                com_google_android_gms_internal_zzcmd.zzjlm[i].zzjmb = Long.valueOf(11910);
                                com_google_android_gms_internal_zzcmd.zzjlm[i].zzjlr = Long.valueOf(currentTimeMillis);
                                com_google_android_gms_internal_zzcmd.zzjlm[i].zzjmh = Boolean.valueOf(false);
                                if (obj2 == null) {
                                    com_google_android_gms_internal_zzcmd.zzjlm[i].zzjmo = null;
                                }
                            }
                            obj2 = zzawy().zzae(2) ? zzawt().zza(com_google_android_gms_internal_zzcmd) : null;
                            obj = zzawu().zzb(com_google_android_gms_internal_zzcmd);
                            str = (String) zzchc.zzjat.get();
                            URL url = new URL(str);
                            zzbq.checkArgument(!arrayList.isEmpty());
                            if (this.zzjfx != null) {
                                zzawy().zzazd().log("Set uploading progress before finishing the previous upload");
                            } else {
                                this.zzjfx = new ArrayList(arrayList);
                            }
                            zzawz().zzjcs.set(currentTimeMillis);
                            zzba = "?";
                            if (com_google_android_gms_internal_zzcmd.zzjlm.length > 0) {
                                zzba = com_google_android_gms_internal_zzcmd.zzjlm[0].zzcn;
                            }
                            zzawy().zzazj().zzd("Uploading data. app, uncompressed size, data", zzba, Integer.valueOf(obj.length), obj2);
                            this.zzjge = true;
                            zzcjk zzbab = zzbab();
                            zzchs com_google_android_gms_internal_zzcip = new zzcip(this);
                            zzbab.zzve();
                            zzbab.zzxf();
                            zzbq.checkNotNull(url);
                            zzbq.checkNotNull(obj);
                            zzbq.checkNotNull(com_google_android_gms_internal_zzcip);
                            zzbab.zzawx().zzh(new zzchu(zzbab, zzayf, url, obj, null, com_google_android_gms_internal_zzcip));
                        }
                    }
                    this.zzjgf = false;
                    zzban();
                } else {
                    zzawy().zzazj().log("Network not connected, ignoring upload request");
                    zzbaj();
                    this.zzjgf = false;
                    zzban();
                }
            }
        } catch (MalformedURLException e) {
            zzawy().zzazd().zze("Failed to parse upload URL. Not uploading. appId", zzchm.zzjk(zzayf), str);
        } catch (Throwable th) {
            this.zzjgf = false;
            zzban();
        }
    }

    final void zzbak() {
        this.zzjga++;
    }

    final void zzbal() {
        zzawx().zzve();
        zzxf();
        if (!this.zzjfs) {
            zzawy().zzazh().log("This instance being marked as an uploader");
            zzawx().zzve();
            zzxf();
            if (zzbam() && zzbae()) {
                int zza = zza(this.zzjfw);
                int zzaza = zzawn().zzaza();
                zzawx().zzve();
                if (zza > zzaza) {
                    zzawy().zzazd().zze("Panic: can't downgrade version. Previous, current version", Integer.valueOf(zza), Integer.valueOf(zzaza));
                } else if (zza < zzaza) {
                    if (zza(zzaza, this.zzjfw)) {
                        zzawy().zzazj().zze("Storage version upgraded. Previous, current version", Integer.valueOf(zza), Integer.valueOf(zzaza));
                    } else {
                        zzawy().zzazd().zze("Storage version upgrade failed. Previous, current version", Integer.valueOf(zza), Integer.valueOf(zzaza));
                    }
                }
            }
            this.zzjfs = true;
            zzbaj();
        }
    }

    public final void zzbo(boolean z) {
        zzbaj();
    }

    final void zzc(zzcgl com_google_android_gms_internal_zzcgl, zzcgi com_google_android_gms_internal_zzcgi) {
        zzbq.checkNotNull(com_google_android_gms_internal_zzcgl);
        zzbq.zzgm(com_google_android_gms_internal_zzcgl.packageName);
        zzbq.checkNotNull(com_google_android_gms_internal_zzcgl.zziyg);
        zzbq.zzgm(com_google_android_gms_internal_zzcgl.zziyg.name);
        zzawx().zzve();
        zzxf();
        if (!TextUtils.isEmpty(com_google_android_gms_internal_zzcgi.zzixs)) {
            if (com_google_android_gms_internal_zzcgi.zzixx) {
                zzaws().beginTransaction();
                try {
                    zzg(com_google_android_gms_internal_zzcgi);
                    zzcgl zzah = zzaws().zzah(com_google_android_gms_internal_zzcgl.packageName, com_google_android_gms_internal_zzcgl.zziyg.name);
                    if (zzah != null) {
                        zzawy().zzazi().zze("Removing conditional user property", com_google_android_gms_internal_zzcgl.packageName, zzawt().zzjj(com_google_android_gms_internal_zzcgl.zziyg.name));
                        zzaws().zzai(com_google_android_gms_internal_zzcgl.packageName, com_google_android_gms_internal_zzcgl.zziyg.name);
                        if (zzah.zziyi) {
                            zzaws().zzaf(com_google_android_gms_internal_zzcgl.packageName, com_google_android_gms_internal_zzcgl.zziyg.name);
                        }
                        if (com_google_android_gms_internal_zzcgl.zziyo != null) {
                            Bundle bundle = null;
                            if (com_google_android_gms_internal_zzcgl.zziyo.zzizt != null) {
                                bundle = com_google_android_gms_internal_zzcgl.zziyo.zzizt.zzayx();
                            }
                            zzc(zzawu().zza(com_google_android_gms_internal_zzcgl.zziyo.name, bundle, zzah.zziyf, com_google_android_gms_internal_zzcgl.zziyo.zzizu, true, false), com_google_android_gms_internal_zzcgi);
                        }
                    } else {
                        zzawy().zzazf().zze("Conditional user property doesn't exist", zzchm.zzjk(com_google_android_gms_internal_zzcgl.packageName), zzawt().zzjj(com_google_android_gms_internal_zzcgl.zziyg.name));
                    }
                    zzaws().setTransactionSuccessful();
                } finally {
                    zzaws().endTransaction();
                }
            } else {
                zzg(com_google_android_gms_internal_zzcgi);
            }
        }
    }

    final void zzc(zzcln com_google_android_gms_internal_zzcln, zzcgi com_google_android_gms_internal_zzcgi) {
        zzawx().zzve();
        zzxf();
        if (!TextUtils.isEmpty(com_google_android_gms_internal_zzcgi.zzixs)) {
            if (com_google_android_gms_internal_zzcgi.zzixx) {
                zzawy().zzazi().zzj("Removing user property", zzawt().zzjj(com_google_android_gms_internal_zzcln.name));
                zzaws().beginTransaction();
                try {
                    zzg(com_google_android_gms_internal_zzcgi);
                    zzaws().zzaf(com_google_android_gms_internal_zzcgi.packageName, com_google_android_gms_internal_zzcln.name);
                    zzaws().setTransactionSuccessful();
                    zzawy().zzazi().zzj("User property removed", zzawt().zzjj(com_google_android_gms_internal_zzcln.name));
                } finally {
                    zzaws().endTransaction();
                }
            } else {
                zzg(com_google_android_gms_internal_zzcgi);
            }
        }
    }

    final void zzd(zzcgi com_google_android_gms_internal_zzcgi) {
        zzaws().zzjb(com_google_android_gms_internal_zzcgi.packageName);
        zzcjk zzaws = zzaws();
        String str = com_google_android_gms_internal_zzcgi.packageName;
        zzbq.zzgm(str);
        zzaws.zzve();
        zzaws.zzxf();
        try {
            SQLiteDatabase writableDatabase = zzaws.getWritableDatabase();
            String[] strArr = new String[]{str};
            int delete = writableDatabase.delete("audience_filter_values", "app_id=?", strArr) + (((((((writableDatabase.delete("apps", "app_id=?", strArr) + 0) + writableDatabase.delete("events", "app_id=?", strArr)) + writableDatabase.delete("user_attributes", "app_id=?", strArr)) + writableDatabase.delete("conditional_properties", "app_id=?", strArr)) + writableDatabase.delete("raw_events", "app_id=?", strArr)) + writableDatabase.delete("raw_events_metadata", "app_id=?", strArr)) + writableDatabase.delete("queue", "app_id=?", strArr));
            if (delete > 0) {
                zzaws.zzawy().zzazj().zze("Reset analytics data. app, records", str, Integer.valueOf(delete));
            }
        } catch (SQLiteException e) {
            zzaws.zzawy().zzazd().zze("Error resetting analytics data. appId, error", zzchm.zzjk(str), e);
        }
        zzf(zza(this.mContext, com_google_android_gms_internal_zzcgi.packageName, com_google_android_gms_internal_zzcgi.zzixs, com_google_android_gms_internal_zzcgi.zzixx, com_google_android_gms_internal_zzcgi.zziye));
    }

    final void zzd(zzcgl com_google_android_gms_internal_zzcgl) {
        zzcgi zzjw = zzjw(com_google_android_gms_internal_zzcgl.packageName);
        if (zzjw != null) {
            zzb(com_google_android_gms_internal_zzcgl, zzjw);
        }
    }

    final void zze(zzcgi com_google_android_gms_internal_zzcgi) {
        zzawx().zzve();
        zzxf();
        zzbq.zzgm(com_google_android_gms_internal_zzcgi.packageName);
        zzg(com_google_android_gms_internal_zzcgi);
    }

    final void zze(zzcgl com_google_android_gms_internal_zzcgl) {
        zzcgi zzjw = zzjw(com_google_android_gms_internal_zzcgl.packageName);
        if (zzjw != null) {
            zzc(com_google_android_gms_internal_zzcgl, zzjw);
        }
    }

    public final void zzf(zzcgi com_google_android_gms_internal_zzcgi) {
        zzawx().zzve();
        zzxf();
        zzbq.checkNotNull(com_google_android_gms_internal_zzcgi);
        zzbq.zzgm(com_google_android_gms_internal_zzcgi.packageName);
        if (!TextUtils.isEmpty(com_google_android_gms_internal_zzcgi.zzixs)) {
            zzcgh zzjb = zzaws().zzjb(com_google_android_gms_internal_zzcgi.packageName);
            if (!(zzjb == null || !TextUtils.isEmpty(zzjb.getGmpAppId()) || TextUtils.isEmpty(com_google_android_gms_internal_zzcgi.zzixs))) {
                zzjb.zzar(0);
                zzaws().zza(zzjb);
                zzawv().zzjv(com_google_android_gms_internal_zzcgi.packageName);
            }
            if (com_google_android_gms_internal_zzcgi.zzixx) {
                int i;
                Bundle bundle;
                long j = com_google_android_gms_internal_zzcgi.zziyc;
                if (j == 0) {
                    j = this.zzata.currentTimeMillis();
                }
                int i2 = com_google_android_gms_internal_zzcgi.zziyd;
                if (i2 == 0 || i2 == 1) {
                    i = i2;
                } else {
                    zzawy().zzazf().zze("Incorrect app type, assuming installed app. appId, appType", zzchm.zzjk(com_google_android_gms_internal_zzcgi.packageName), Integer.valueOf(i2));
                    i = 0;
                }
                zzaws().beginTransaction();
                zzcjk zzaws;
                String appId;
                try {
                    zzjb = zzaws().zzjb(com_google_android_gms_internal_zzcgi.packageName);
                    if (!(zzjb == null || zzjb.getGmpAppId() == null || zzjb.getGmpAppId().equals(com_google_android_gms_internal_zzcgi.zzixs))) {
                        zzawy().zzazf().zzj("New GMP App Id passed in. Removing cached database data. appId", zzchm.zzjk(zzjb.getAppId()));
                        zzaws = zzaws();
                        appId = zzjb.getAppId();
                        zzaws.zzxf();
                        zzaws.zzve();
                        zzbq.zzgm(appId);
                        SQLiteDatabase writableDatabase = zzaws.getWritableDatabase();
                        String[] strArr = new String[]{appId};
                        i2 = writableDatabase.delete("audience_filter_values", "app_id=?", strArr) + ((((((((writableDatabase.delete("events", "app_id=?", strArr) + 0) + writableDatabase.delete("user_attributes", "app_id=?", strArr)) + writableDatabase.delete("conditional_properties", "app_id=?", strArr)) + writableDatabase.delete("apps", "app_id=?", strArr)) + writableDatabase.delete("raw_events", "app_id=?", strArr)) + writableDatabase.delete("raw_events_metadata", "app_id=?", strArr)) + writableDatabase.delete("event_filters", "app_id=?", strArr)) + writableDatabase.delete("property_filters", "app_id=?", strArr));
                        if (i2 > 0) {
                            zzaws.zzawy().zzazj().zze("Deleted application data. app, records", appId, Integer.valueOf(i2));
                        }
                        zzjb = null;
                    }
                } catch (SQLiteException e) {
                    zzaws.zzawy().zzazd().zze("Error deleting application data. appId, error", zzchm.zzjk(appId), e);
                } catch (Throwable th) {
                    zzaws().endTransaction();
                }
                if (zzjb != null) {
                    if (!(zzjb.zzvj() == null || zzjb.zzvj().equals(com_google_android_gms_internal_zzcgi.zzifm))) {
                        bundle = new Bundle();
                        bundle.putString("_pv", zzjb.zzvj());
                        zzb(new zzcha("_au", new zzcgx(bundle), "auto", j), com_google_android_gms_internal_zzcgi);
                    }
                }
                zzg(com_google_android_gms_internal_zzcgi);
                zzcgw com_google_android_gms_internal_zzcgw = null;
                if (i == 0) {
                    com_google_android_gms_internal_zzcgw = zzaws().zzae(com_google_android_gms_internal_zzcgi.packageName, "_f");
                } else if (i == 1) {
                    com_google_android_gms_internal_zzcgw = zzaws().zzae(com_google_android_gms_internal_zzcgi.packageName, "_v");
                }
                if (com_google_android_gms_internal_zzcgw == null) {
                    long j2 = (1 + (j / 3600000)) * 3600000;
                    if (i == 0) {
                        zzb(new zzcln("_fot", j, Long.valueOf(j2), "auto"), com_google_android_gms_internal_zzcgi);
                        zzawx().zzve();
                        zzxf();
                        Bundle bundle2 = new Bundle();
                        bundle2.putLong("_c", 1);
                        bundle2.putLong("_r", 1);
                        bundle2.putLong("_uwa", 0);
                        bundle2.putLong("_pfo", 0);
                        bundle2.putLong("_sys", 0);
                        bundle2.putLong("_sysu", 0);
                        if (this.mContext.getPackageManager() == null) {
                            zzawy().zzazd().zzj("PackageManager is null, first open report might be inaccurate. appId", zzchm.zzjk(com_google_android_gms_internal_zzcgi.packageName));
                        } else {
                            ApplicationInfo applicationInfo;
                            PackageInfo packageInfo = null;
                            try {
                                packageInfo = zzbhf.zzdb(this.mContext).getPackageInfo(com_google_android_gms_internal_zzcgi.packageName, 0);
                            } catch (NameNotFoundException e2) {
                                zzawy().zzazd().zze("Package info is null, first open report might be inaccurate. appId", zzchm.zzjk(com_google_android_gms_internal_zzcgi.packageName), e2);
                            }
                            if (packageInfo != null) {
                                if (packageInfo.firstInstallTime != 0) {
                                    Object obj = null;
                                    if (packageInfo.firstInstallTime != packageInfo.lastUpdateTime) {
                                        bundle2.putLong("_uwa", 1);
                                    } else {
                                        obj = 1;
                                    }
                                    zzb(new zzcln("_fi", j, Long.valueOf(obj != null ? 1 : 0), "auto"), com_google_android_gms_internal_zzcgi);
                                }
                            }
                            try {
                                applicationInfo = zzbhf.zzdb(this.mContext).getApplicationInfo(com_google_android_gms_internal_zzcgi.packageName, 0);
                            } catch (NameNotFoundException e22) {
                                zzawy().zzazd().zze("Application info is null, first open report might be inaccurate. appId", zzchm.zzjk(com_google_android_gms_internal_zzcgi.packageName), e22);
                                applicationInfo = null;
                            }
                            if (applicationInfo != null) {
                                if ((applicationInfo.flags & 1) != 0) {
                                    bundle2.putLong("_sys", 1);
                                }
                                if ((applicationInfo.flags & 128) != 0) {
                                    bundle2.putLong("_sysu", 1);
                                }
                            }
                        }
                        zzcjk zzaws2 = zzaws();
                        String str = com_google_android_gms_internal_zzcgi.packageName;
                        zzbq.zzgm(str);
                        zzaws2.zzve();
                        zzaws2.zzxf();
                        j2 = zzaws2.zzal(str, "first_open_count");
                        if (j2 >= 0) {
                            bundle2.putLong("_pfo", j2);
                        }
                        zzb(new zzcha("_f", new zzcgx(bundle2), "auto", j), com_google_android_gms_internal_zzcgi);
                    } else if (i == 1) {
                        zzb(new zzcln("_fvt", j, Long.valueOf(j2), "auto"), com_google_android_gms_internal_zzcgi);
                        zzawx().zzve();
                        zzxf();
                        bundle = new Bundle();
                        bundle.putLong("_c", 1);
                        bundle.putLong("_r", 1);
                        zzb(new zzcha("_v", new zzcgx(bundle), "auto", j), com_google_android_gms_internal_zzcgi);
                    }
                    bundle = new Bundle();
                    bundle.putLong("_et", 1);
                    zzb(new zzcha("_e", new zzcgx(bundle), "auto", j), com_google_android_gms_internal_zzcgi);
                } else if (com_google_android_gms_internal_zzcgi.zzixy) {
                    zzb(new zzcha("_cd", new zzcgx(new Bundle()), "auto", j), com_google_android_gms_internal_zzcgi);
                }
                zzaws().setTransactionSuccessful();
                zzaws().endTransaction();
                return;
            }
            zzg(com_google_android_gms_internal_zzcgi);
        }
    }

    final void zzi(Runnable runnable) {
        zzawx().zzve();
        if (this.zzjfy == null) {
            this.zzjfy = new ArrayList();
        }
        this.zzjfy.add(runnable);
    }

    public final String zzjx(String str) {
        Object e;
        try {
            return (String) zzawx().zzc(new zzcio(this, str)).get(30000, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e2) {
            e = e2;
        } catch (InterruptedException e3) {
            e = e3;
        } catch (ExecutionException e4) {
            e = e4;
        }
        zzawy().zzazd().zze("Failed to get app instance id. appId", zzchm.zzjk(str), e);
        return null;
    }

    public final zzd zzws() {
        return this.zzata;
    }

    final void zzxf() {
        if (!this.zzdtb) {
            throw new IllegalStateException("AppMeasurement is not initialized");
        }
    }
}
