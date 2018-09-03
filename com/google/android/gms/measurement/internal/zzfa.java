package com.google.android.gms.measurement.internal;

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
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.Clock;
import com.google.android.gms.common.wrappers.Wrappers;
import com.google.android.gms.internal.measurement.zzgb;
import com.google.android.gms.internal.measurement.zzgd;
import com.google.android.gms.internal.measurement.zzgf;
import com.google.android.gms.internal.measurement.zzgg;
import com.google.android.gms.internal.measurement.zzgh;
import com.google.android.gms.internal.measurement.zzgi;
import com.google.android.gms.internal.measurement.zzgl;
import com.google.android.gms.internal.measurement.zzyy;
import com.google.android.gms.internal.measurement.zzzg;
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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.telegram.messenger.support.widget.helper.ItemTouchHelper.Callback;

public class zzfa implements zzcq {
    private static volatile zzfa zzatc;
    private final zzbt zzadj;
    private zzbn zzatd;
    private zzat zzate;
    private zzq zzatf;
    private zzay zzatg;
    private zzew zzath;
    private zzj zzati;
    private final zzfg zzatj;
    private boolean zzatk;
    private long zzatl;
    private List<Runnable> zzatm;
    private int zzatn;
    private int zzato;
    private boolean zzatp;
    private boolean zzatq;
    private boolean zzatr;
    private FileLock zzats;
    private FileChannel zzatt;
    private List<Long> zzatu;
    private List<Long> zzatv;
    private long zzatw;
    private boolean zzvz;

    class zza implements zzs {
        private final /* synthetic */ zzfa zzaty;
        zzgi zzaua;
        List<Long> zzaub;
        List<zzgf> zzauc;
        private long zzaud;

        private zza(zzfa com_google_android_gms_measurement_internal_zzfa) {
            this.zzaty = com_google_android_gms_measurement_internal_zzfa;
        }

        public final void zzb(zzgi com_google_android_gms_internal_measurement_zzgi) {
            Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzgi);
            this.zzaua = com_google_android_gms_internal_measurement_zzgi;
        }

        public final boolean zza(long j, zzgf com_google_android_gms_internal_measurement_zzgf) {
            Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzgf);
            if (this.zzauc == null) {
                this.zzauc = new ArrayList();
            }
            if (this.zzaub == null) {
                this.zzaub = new ArrayList();
            }
            if (this.zzauc.size() > 0 && zza((zzgf) this.zzauc.get(0)) != zza(com_google_android_gms_internal_measurement_zzgf)) {
                return false;
            }
            long zzvu = this.zzaud + ((long) com_google_android_gms_internal_measurement_zzgf.zzvu());
            if (zzvu >= ((long) Math.max(0, ((Integer) zzaf.zzajl.get()).intValue()))) {
                return false;
            }
            this.zzaud = zzvu;
            this.zzauc.add(com_google_android_gms_internal_measurement_zzgf);
            this.zzaub.add(Long.valueOf(j));
            if (this.zzauc.size() >= Math.max(1, ((Integer) zzaf.zzajm.get()).intValue())) {
                return false;
            }
            return true;
        }

        private static long zza(zzgf com_google_android_gms_internal_measurement_zzgf) {
            return ((com_google_android_gms_internal_measurement_zzgf.zzawu.longValue() / 1000) / 60) / 60;
        }
    }

    public static zzfa zzm(Context context) {
        Preconditions.checkNotNull(context);
        Preconditions.checkNotNull(context.getApplicationContext());
        if (zzatc == null) {
            synchronized (zzfa.class) {
                if (zzatc == null) {
                    zzatc = new zzfa(new zzff(context));
                }
            }
        }
        return zzatc;
    }

    private zzfa(zzff com_google_android_gms_measurement_internal_zzff) {
        this(com_google_android_gms_measurement_internal_zzff, null);
    }

    private zzfa(zzff com_google_android_gms_measurement_internal_zzff, zzbt com_google_android_gms_measurement_internal_zzbt) {
        this.zzvz = false;
        Preconditions.checkNotNull(com_google_android_gms_measurement_internal_zzff);
        this.zzadj = zzbt.zza(com_google_android_gms_measurement_internal_zzff.zzri, null);
        this.zzatw = -1;
        zzez com_google_android_gms_measurement_internal_zzfg = new zzfg(this);
        com_google_android_gms_measurement_internal_zzfg.zzq();
        this.zzatj = com_google_android_gms_measurement_internal_zzfg;
        com_google_android_gms_measurement_internal_zzfg = new zzat(this);
        com_google_android_gms_measurement_internal_zzfg.zzq();
        this.zzate = com_google_android_gms_measurement_internal_zzfg;
        com_google_android_gms_measurement_internal_zzfg = new zzbn(this);
        com_google_android_gms_measurement_internal_zzfg.zzq();
        this.zzatd = com_google_android_gms_measurement_internal_zzfg;
        this.zzadj.zzgn().zzc(new zzfb(this, com_google_android_gms_measurement_internal_zzff));
    }

    private final void zza(zzff com_google_android_gms_measurement_internal_zzff) {
        this.zzadj.zzgn().zzaf();
        zzez com_google_android_gms_measurement_internal_zzq = new zzq(this);
        com_google_android_gms_measurement_internal_zzq.zzq();
        this.zzatf = com_google_android_gms_measurement_internal_zzq;
        this.zzadj.zzgq().zza(this.zzatd);
        com_google_android_gms_measurement_internal_zzq = new zzj(this);
        com_google_android_gms_measurement_internal_zzq.zzq();
        this.zzati = com_google_android_gms_measurement_internal_zzq;
        com_google_android_gms_measurement_internal_zzq = new zzew(this);
        com_google_android_gms_measurement_internal_zzq.zzq();
        this.zzath = com_google_android_gms_measurement_internal_zzq;
        this.zzatg = new zzay(this);
        if (this.zzatn != this.zzato) {
            this.zzadj.zzgo().zzjd().zze("Not all upload components initialized", Integer.valueOf(this.zzatn), Integer.valueOf(this.zzato));
        }
        this.zzvz = true;
    }

    protected final void start() {
        this.zzadj.zzgn().zzaf();
        zzjq().zzif();
        if (this.zzadj.zzgp().zzane.get() == 0) {
            this.zzadj.zzgp().zzane.set(this.zzadj.zzbx().currentTimeMillis());
        }
        zzlv();
    }

    public final zzk zzgr() {
        return this.zzadj.zzgr();
    }

    public final zzn zzgq() {
        return this.zzadj.zzgq();
    }

    public final zzap zzgo() {
        return this.zzadj.zzgo();
    }

    public final zzbo zzgn() {
        return this.zzadj.zzgn();
    }

    private final zzbn zzln() {
        zza(this.zzatd);
        return this.zzatd;
    }

    public final zzat zzlo() {
        zza(this.zzate);
        return this.zzate;
    }

    public final zzq zzjq() {
        zza(this.zzatf);
        return this.zzatf;
    }

    private final zzay zzlp() {
        if (this.zzatg != null) {
            return this.zzatg;
        }
        throw new IllegalStateException("Network broadcast receiver not created");
    }

    private final zzew zzlq() {
        zza(this.zzath);
        return this.zzath;
    }

    public final zzj zzjp() {
        zza(this.zzati);
        return this.zzati;
    }

    public final zzfg zzjo() {
        zza(this.zzatj);
        return this.zzatj;
    }

    public final zzan zzgl() {
        return this.zzadj.zzgl();
    }

    public final Context getContext() {
        return this.zzadj.getContext();
    }

    public final Clock zzbx() {
        return this.zzadj.zzbx();
    }

    public final zzfk zzgm() {
        return this.zzadj.zzgm();
    }

    private final void zzaf() {
        this.zzadj.zzgn().zzaf();
    }

    final void zzlr() {
        if (!this.zzvz) {
            throw new IllegalStateException("UploadController is not initialized");
        }
    }

    private static void zza(zzez com_google_android_gms_measurement_internal_zzez) {
        if (com_google_android_gms_measurement_internal_zzez == null) {
            throw new IllegalStateException("Upload Component not created");
        } else if (!com_google_android_gms_measurement_internal_zzez.isInitialized()) {
            String valueOf = String.valueOf(com_google_android_gms_measurement_internal_zzez.getClass());
            throw new IllegalStateException(new StringBuilder(String.valueOf(valueOf).length() + 27).append("Component not initialized: ").append(valueOf).toString());
        }
    }

    final void zze(zzh com_google_android_gms_measurement_internal_zzh) {
        zzaf();
        zzlr();
        Preconditions.checkNotEmpty(com_google_android_gms_measurement_internal_zzh.packageName);
        zzg(com_google_android_gms_measurement_internal_zzh);
    }

    private final long zzls() {
        long currentTimeMillis = this.zzadj.zzbx().currentTimeMillis();
        zzco zzgp = this.zzadj.zzgp();
        zzgp.zzcl();
        zzgp.zzaf();
        long j = zzgp.zzani.get();
        if (j == 0) {
            j = 1 + ((long) zzgp.zzgm().zzmd().nextInt(86400000));
            zzgp.zzani.set(j);
        }
        return ((((j + currentTimeMillis) / 1000) / 60) / 60) / 24;
    }

    final void zzc(zzad com_google_android_gms_measurement_internal_zzad, String str) {
        zzg zzbl = zzjq().zzbl(str);
        if (zzbl == null || TextUtils.isEmpty(zzbl.zzak())) {
            this.zzadj.zzgo().zzjk().zzg("No app data available; dropping event", str);
            return;
        }
        Boolean zzc = zzc(zzbl);
        if (zzc == null) {
            if (!"_ui".equals(com_google_android_gms_measurement_internal_zzad.name)) {
                this.zzadj.zzgo().zzjg().zzg("Could not find package. appId", zzap.zzbv(str));
            }
        } else if (!zzc.booleanValue()) {
            this.zzadj.zzgo().zzjd().zzg("App version does not match; dropping event. appId", zzap.zzbv(str));
            return;
        }
        zzad com_google_android_gms_measurement_internal_zzad2 = com_google_android_gms_measurement_internal_zzad;
        zzc(com_google_android_gms_measurement_internal_zzad2, new zzh(str, zzbl.getGmpAppId(), zzbl.zzak(), zzbl.zzha(), zzbl.zzhb(), zzbl.zzhc(), zzbl.zzhd(), null, zzbl.isMeasurementEnabled(), false, zzbl.getFirebaseInstanceId(), zzbl.zzhq(), 0, 0, zzbl.zzhr(), zzbl.zzhs(), false, zzbl.zzgw()));
    }

    final void zzc(zzad com_google_android_gms_measurement_internal_zzad, zzh com_google_android_gms_measurement_internal_zzh) {
        Preconditions.checkNotNull(com_google_android_gms_measurement_internal_zzh);
        Preconditions.checkNotEmpty(com_google_android_gms_measurement_internal_zzh.packageName);
        zzaf();
        zzlr();
        String str = com_google_android_gms_measurement_internal_zzh.packageName;
        long j = com_google_android_gms_measurement_internal_zzad.zzaip;
        if (!zzjo().zze(com_google_android_gms_measurement_internal_zzad, com_google_android_gms_measurement_internal_zzh)) {
            return;
        }
        if (com_google_android_gms_measurement_internal_zzh.zzagg) {
            zzjq().beginTransaction();
            try {
                List emptyList;
                Object obj;
                zzco zzjq = zzjq();
                Preconditions.checkNotEmpty(str);
                zzjq.zzaf();
                zzjq.zzcl();
                if (j < 0) {
                    zzjq.zzgo().zzjg().zze("Invalid time querying timed out conditional properties", zzap.zzbv(str), Long.valueOf(j));
                    emptyList = Collections.emptyList();
                } else {
                    emptyList = zzjq.zzb("active=0 and app_id=? and abs(? - creation_timestamp) > trigger_timeout", new String[]{str, String.valueOf(j)});
                }
                for (zzl com_google_android_gms_measurement_internal_zzl : r2) {
                    if (com_google_android_gms_measurement_internal_zzl != null) {
                        this.zzadj.zzgo().zzjk().zzd("User property timed out", com_google_android_gms_measurement_internal_zzl.packageName, this.zzadj.zzgl().zzbu(com_google_android_gms_measurement_internal_zzl.zzahb.name), com_google_android_gms_measurement_internal_zzl.zzahb.getValue());
                        if (com_google_android_gms_measurement_internal_zzl.zzahc != null) {
                            zzd(new zzad(com_google_android_gms_measurement_internal_zzl.zzahc, j), com_google_android_gms_measurement_internal_zzh);
                        }
                        zzjq().zzk(str, com_google_android_gms_measurement_internal_zzl.zzahb.name);
                    }
                }
                zzjq = zzjq();
                Preconditions.checkNotEmpty(str);
                zzjq.zzaf();
                zzjq.zzcl();
                if (j < 0) {
                    zzjq.zzgo().zzjg().zze("Invalid time querying expired conditional properties", zzap.zzbv(str), Long.valueOf(j));
                    emptyList = Collections.emptyList();
                } else {
                    emptyList = zzjq.zzb("active<>0 and app_id=? and abs(? - triggered_timestamp) > time_to_live", new String[]{str, String.valueOf(j)});
                }
                List arrayList = new ArrayList(r2.size());
                for (zzl com_google_android_gms_measurement_internal_zzl2 : r2) {
                    if (com_google_android_gms_measurement_internal_zzl2 != null) {
                        this.zzadj.zzgo().zzjk().zzd("User property expired", com_google_android_gms_measurement_internal_zzl2.packageName, this.zzadj.zzgl().zzbu(com_google_android_gms_measurement_internal_zzl2.zzahb.name), com_google_android_gms_measurement_internal_zzl2.zzahb.getValue());
                        zzjq().zzh(str, com_google_android_gms_measurement_internal_zzl2.zzahb.name);
                        if (com_google_android_gms_measurement_internal_zzl2.zzahe != null) {
                            arrayList.add(com_google_android_gms_measurement_internal_zzl2.zzahe);
                        }
                        zzjq().zzk(str, com_google_android_gms_measurement_internal_zzl2.zzahb.name);
                    }
                }
                ArrayList arrayList2 = (ArrayList) arrayList;
                int size = arrayList2.size();
                int i = 0;
                while (i < size) {
                    obj = arrayList2.get(i);
                    i++;
                    zzd(new zzad((zzad) obj, j), com_google_android_gms_measurement_internal_zzh);
                }
                zzjq = zzjq();
                String str2 = com_google_android_gms_measurement_internal_zzad.name;
                Preconditions.checkNotEmpty(str);
                Preconditions.checkNotEmpty(str2);
                zzjq.zzaf();
                zzjq.zzcl();
                if (j < 0) {
                    zzjq.zzgo().zzjg().zzd("Invalid time querying triggered conditional properties", zzap.zzbv(str), zzjq.zzgl().zzbs(str2), Long.valueOf(j));
                    emptyList = Collections.emptyList();
                } else {
                    emptyList = zzjq.zzb("active=0 and app_id=? and trigger_event_name=? and abs(? - creation_timestamp) <= trigger_timeout", new String[]{str, str2, String.valueOf(j)});
                }
                List arrayList3 = new ArrayList(r2.size());
                for (zzl com_google_android_gms_measurement_internal_zzl3 : r2) {
                    if (com_google_android_gms_measurement_internal_zzl3 != null) {
                        zzfh com_google_android_gms_measurement_internal_zzfh = com_google_android_gms_measurement_internal_zzl3.zzahb;
                        zzfj com_google_android_gms_measurement_internal_zzfj = new zzfj(com_google_android_gms_measurement_internal_zzl3.packageName, com_google_android_gms_measurement_internal_zzl3.origin, com_google_android_gms_measurement_internal_zzfh.name, j, com_google_android_gms_measurement_internal_zzfh.getValue());
                        if (zzjq().zza(com_google_android_gms_measurement_internal_zzfj)) {
                            this.zzadj.zzgo().zzjk().zzd("User property triggered", com_google_android_gms_measurement_internal_zzl3.packageName, this.zzadj.zzgl().zzbu(com_google_android_gms_measurement_internal_zzfj.name), com_google_android_gms_measurement_internal_zzfj.value);
                        } else {
                            this.zzadj.zzgo().zzjd().zzd("Too many active user properties, ignoring", zzap.zzbv(com_google_android_gms_measurement_internal_zzl3.packageName), this.zzadj.zzgl().zzbu(com_google_android_gms_measurement_internal_zzfj.name), com_google_android_gms_measurement_internal_zzfj.value);
                        }
                        if (com_google_android_gms_measurement_internal_zzl3.zzahd != null) {
                            arrayList3.add(com_google_android_gms_measurement_internal_zzl3.zzahd);
                        }
                        com_google_android_gms_measurement_internal_zzl3.zzahb = new zzfh(com_google_android_gms_measurement_internal_zzfj);
                        com_google_android_gms_measurement_internal_zzl3.active = true;
                        zzjq().zza(com_google_android_gms_measurement_internal_zzl3);
                    }
                }
                zzd(com_google_android_gms_measurement_internal_zzad, com_google_android_gms_measurement_internal_zzh);
                arrayList2 = (ArrayList) arrayList3;
                int size2 = arrayList2.size();
                i = 0;
                while (i < size2) {
                    obj = arrayList2.get(i);
                    i++;
                    zzd(new zzad((zzad) obj, j), com_google_android_gms_measurement_internal_zzh);
                }
                zzjq().setTransactionSuccessful();
            } finally {
                zzjq().endTransaction();
            }
        } else {
            zzg(com_google_android_gms_measurement_internal_zzh);
        }
    }

    private final void zzd(zzad com_google_android_gms_measurement_internal_zzad, zzh com_google_android_gms_measurement_internal_zzh) {
        zzgi com_google_android_gms_internal_measurement_zzgi;
        Preconditions.checkNotNull(com_google_android_gms_measurement_internal_zzh);
        Preconditions.checkNotEmpty(com_google_android_gms_measurement_internal_zzh.packageName);
        long nanoTime = System.nanoTime();
        zzaf();
        zzlr();
        String str = com_google_android_gms_measurement_internal_zzh.packageName;
        if (!zzjo().zze(com_google_android_gms_measurement_internal_zzad, com_google_android_gms_measurement_internal_zzh)) {
            return;
        }
        if (!com_google_android_gms_measurement_internal_zzh.zzagg) {
            zzg(com_google_android_gms_measurement_internal_zzh);
        } else if (zzln().zzo(str, com_google_android_gms_measurement_internal_zzad.name)) {
            Object obj;
            this.zzadj.zzgo().zzjg().zze("Dropping blacklisted event. appId", zzap.zzbv(str), this.zzadj.zzgl().zzbs(com_google_android_gms_measurement_internal_zzad.name));
            if (zzln().zzck(str) || zzln().zzcl(str)) {
                obj = 1;
            } else {
                obj = null;
            }
            if (obj == null && !"_err".equals(com_google_android_gms_measurement_internal_zzad.name)) {
                this.zzadj.zzgm().zza(str, 11, "_ev", com_google_android_gms_measurement_internal_zzad.name, 0);
            }
            if (obj != null) {
                zzg zzbl = zzjq().zzbl(str);
                if (zzbl != null) {
                    if (Math.abs(this.zzadj.zzbx().currentTimeMillis() - Math.max(zzbl.zzhg(), zzbl.zzhf())) > ((Long) zzaf.zzakc.get()).longValue()) {
                        this.zzadj.zzgo().zzjk().zzbx("Fetching config for blacklisted app");
                        zzb(zzbl);
                    }
                }
            }
        } else {
            if (this.zzadj.zzgo().isLoggable(2)) {
                this.zzadj.zzgo().zzjl().zzg("Logging event", this.zzadj.zzgl().zzb(com_google_android_gms_measurement_internal_zzad));
            }
            zzjq().beginTransaction();
            zzg(com_google_android_gms_measurement_internal_zzh);
            if (("_iap".equals(com_google_android_gms_measurement_internal_zzad.name) || "ecommerce_purchase".equals(com_google_android_gms_measurement_internal_zzad.name)) && !zza(str, com_google_android_gms_measurement_internal_zzad)) {
                zzjq().setTransactionSuccessful();
                zzjq().endTransaction();
                return;
            }
            try {
                boolean zzcq = zzfk.zzcq(com_google_android_gms_measurement_internal_zzad.name);
                boolean equals = "_err".equals(com_google_android_gms_measurement_internal_zzad.name);
                zzr zza = zzjq().zza(zzls(), str, true, zzcq, false, equals, false);
                long intValue = zza.zzahr - ((long) ((Integer) zzaf.zzajn.get()).intValue());
                if (intValue > 0) {
                    if (intValue % 1000 == 1) {
                        this.zzadj.zzgo().zzjd().zze("Data loss. Too many events logged. appId, count", zzap.zzbv(str), Long.valueOf(zza.zzahr));
                    }
                    zzjq().setTransactionSuccessful();
                    zzjq().endTransaction();
                    return;
                }
                zzz zzai;
                zzy com_google_android_gms_measurement_internal_zzy;
                boolean z;
                if (zzcq) {
                    intValue = zza.zzahq - ((long) ((Integer) zzaf.zzajp.get()).intValue());
                    if (intValue > 0) {
                        if (intValue % 1000 == 1) {
                            this.zzadj.zzgo().zzjd().zze("Data loss. Too many public events logged. appId, count", zzap.zzbv(str), Long.valueOf(zza.zzahq));
                        }
                        this.zzadj.zzgm().zza(str, 16, "_ev", com_google_android_gms_measurement_internal_zzad.name, 0);
                        zzjq().setTransactionSuccessful();
                        zzjq().endTransaction();
                        return;
                    }
                }
                if (equals) {
                    intValue = zza.zzaht - ((long) Math.max(0, Math.min(1000000, this.zzadj.zzgq().zzb(com_google_android_gms_measurement_internal_zzh.packageName, zzaf.zzajo))));
                    if (intValue > 0) {
                        if (intValue == 1) {
                            this.zzadj.zzgo().zzjd().zze("Too many error events logged. appId, count", zzap.zzbv(str), Long.valueOf(zza.zzaht));
                        }
                        zzjq().setTransactionSuccessful();
                        zzjq().endTransaction();
                        return;
                    }
                }
                Bundle zziv = com_google_android_gms_measurement_internal_zzad.zzaid.zziv();
                this.zzadj.zzgm().zza(zziv, "_o", com_google_android_gms_measurement_internal_zzad.origin);
                if (this.zzadj.zzgm().zzcw(str)) {
                    this.zzadj.zzgm().zza(zziv, "_dbg", Long.valueOf(1));
                    this.zzadj.zzgm().zza(zziv, "_r", Long.valueOf(1));
                }
                long zzbm = zzjq().zzbm(str);
                if (zzbm > 0) {
                    this.zzadj.zzgo().zzjg().zze("Data lost. Too many events stored on disk, deleted. appId", zzap.zzbv(str), Long.valueOf(zzbm));
                }
                zzy com_google_android_gms_measurement_internal_zzy2 = new zzy(this.zzadj, com_google_android_gms_measurement_internal_zzad.origin, str, com_google_android_gms_measurement_internal_zzad.name, com_google_android_gms_measurement_internal_zzad.zzaip, 0, zziv);
                zzz zzg = zzjq().zzg(str, com_google_android_gms_measurement_internal_zzy2.name);
                if (zzg != null) {
                    com_google_android_gms_measurement_internal_zzy2 = com_google_android_gms_measurement_internal_zzy2.zza(this.zzadj, zzg.zzaig);
                    zzai = zzg.zzai(com_google_android_gms_measurement_internal_zzy2.timestamp);
                    com_google_android_gms_measurement_internal_zzy = com_google_android_gms_measurement_internal_zzy2;
                } else if (zzjq().zzbp(str) < 500 || !zzcq) {
                    zzai = new zzz(str, com_google_android_gms_measurement_internal_zzy2.name, 0, 0, com_google_android_gms_measurement_internal_zzy2.timestamp, 0, null, null, null, null);
                    com_google_android_gms_measurement_internal_zzy = com_google_android_gms_measurement_internal_zzy2;
                } else {
                    this.zzadj.zzgo().zzjd().zzd("Too many event names used, ignoring event. appId, name, supported count", zzap.zzbv(str), this.zzadj.zzgl().zzbs(com_google_android_gms_measurement_internal_zzy2.name), Integer.valueOf(500));
                    this.zzadj.zzgm().zza(str, 8, null, null, 0);
                    zzjq().endTransaction();
                    return;
                }
                zzjq().zza(zzai);
                zzaf();
                zzlr();
                Preconditions.checkNotNull(com_google_android_gms_measurement_internal_zzy);
                Preconditions.checkNotNull(com_google_android_gms_measurement_internal_zzh);
                Preconditions.checkNotEmpty(com_google_android_gms_measurement_internal_zzy.zztt);
                Preconditions.checkArgument(com_google_android_gms_measurement_internal_zzy.zztt.equals(com_google_android_gms_measurement_internal_zzh.packageName));
                com_google_android_gms_internal_measurement_zzgi = new zzgi();
                com_google_android_gms_internal_measurement_zzgi.zzaxa = Integer.valueOf(1);
                com_google_android_gms_internal_measurement_zzgi.zzaxi = "android";
                com_google_android_gms_internal_measurement_zzgi.zztt = com_google_android_gms_measurement_internal_zzh.packageName;
                com_google_android_gms_internal_measurement_zzgi.zzage = com_google_android_gms_measurement_internal_zzh.zzage;
                com_google_android_gms_internal_measurement_zzgi.zzts = com_google_android_gms_measurement_internal_zzh.zzts;
                com_google_android_gms_internal_measurement_zzgi.zzaxu = com_google_android_gms_measurement_internal_zzh.zzagd == -2147483648L ? null : Integer.valueOf((int) com_google_android_gms_measurement_internal_zzh.zzagd);
                com_google_android_gms_internal_measurement_zzgi.zzaxm = Long.valueOf(com_google_android_gms_measurement_internal_zzh.zzadt);
                com_google_android_gms_internal_measurement_zzgi.zzafx = com_google_android_gms_measurement_internal_zzh.zzafx;
                com_google_android_gms_internal_measurement_zzgi.zzawj = com_google_android_gms_measurement_internal_zzh.zzagk;
                com_google_android_gms_internal_measurement_zzgi.zzaxq = com_google_android_gms_measurement_internal_zzh.zzagf == 0 ? null : Long.valueOf(com_google_android_gms_measurement_internal_zzh.zzagf);
                Pair zzby = this.zzadj.zzgp().zzby(com_google_android_gms_measurement_internal_zzh.packageName);
                if (zzby == null || TextUtils.isEmpty((CharSequence) zzby.first)) {
                    if (!this.zzadj.zzgk().zzl(this.zzadj.getContext()) && com_google_android_gms_measurement_internal_zzh.zzagj) {
                        String string = Secure.getString(this.zzadj.getContext().getContentResolver(), "android_id");
                        if (string == null) {
                            this.zzadj.zzgo().zzjg().zzg("null secure ID. appId", zzap.zzbv(com_google_android_gms_internal_measurement_zzgi.zztt));
                            string = "null";
                        } else if (string.isEmpty()) {
                            this.zzadj.zzgo().zzjg().zzg("empty secure ID. appId", zzap.zzbv(com_google_android_gms_internal_measurement_zzgi.zztt));
                        }
                        com_google_android_gms_internal_measurement_zzgi.zzaxx = string;
                    }
                } else if (com_google_android_gms_measurement_internal_zzh.zzagi) {
                    com_google_android_gms_internal_measurement_zzgi.zzaxo = (String) zzby.first;
                    com_google_android_gms_internal_measurement_zzgi.zzaxp = (Boolean) zzby.second;
                }
                this.zzadj.zzgk().zzcl();
                com_google_android_gms_internal_measurement_zzgi.zzaxk = Build.MODEL;
                this.zzadj.zzgk().zzcl();
                com_google_android_gms_internal_measurement_zzgi.zzaxj = VERSION.RELEASE;
                com_google_android_gms_internal_measurement_zzgi.zzaxl = Integer.valueOf((int) this.zzadj.zzgk().zzis());
                com_google_android_gms_internal_measurement_zzgi.zzaia = this.zzadj.zzgk().zzit();
                com_google_android_gms_internal_measurement_zzgi.zzaxn = null;
                com_google_android_gms_internal_measurement_zzgi.zzaxd = null;
                com_google_android_gms_internal_measurement_zzgi.zzaxe = null;
                com_google_android_gms_internal_measurement_zzgi.zzaxf = null;
                com_google_android_gms_internal_measurement_zzgi.zzaxz = Long.valueOf(com_google_android_gms_measurement_internal_zzh.zzagh);
                if (this.zzadj.isEnabled() && zzn.zzhz()) {
                    com_google_android_gms_internal_measurement_zzgi.zzaya = null;
                }
                zzg zzbl2 = zzjq().zzbl(com_google_android_gms_measurement_internal_zzh.packageName);
                if (zzbl2 == null) {
                    zzbl2 = new zzg(this.zzadj, com_google_android_gms_measurement_internal_zzh.packageName);
                    zzbl2.zzam(this.zzadj.zzgm().zzmf());
                    zzbl2.zzaq(com_google_android_gms_measurement_internal_zzh.zzafz);
                    zzbl2.zzan(com_google_android_gms_measurement_internal_zzh.zzafx);
                    zzbl2.zzap(this.zzadj.zzgp().zzbz(com_google_android_gms_measurement_internal_zzh.packageName));
                    zzbl2.zzx(0);
                    zzbl2.zzs(0);
                    zzbl2.zzt(0);
                    zzbl2.setAppVersion(com_google_android_gms_measurement_internal_zzh.zzts);
                    zzbl2.zzu(com_google_android_gms_measurement_internal_zzh.zzagd);
                    zzbl2.zzar(com_google_android_gms_measurement_internal_zzh.zzage);
                    zzbl2.zzv(com_google_android_gms_measurement_internal_zzh.zzadt);
                    zzbl2.zzw(com_google_android_gms_measurement_internal_zzh.zzagf);
                    zzbl2.setMeasurementEnabled(com_google_android_gms_measurement_internal_zzh.zzagg);
                    zzbl2.zzag(com_google_android_gms_measurement_internal_zzh.zzagh);
                    zzjq().zza(zzbl2);
                }
                com_google_android_gms_internal_measurement_zzgi.zzafw = zzbl2.getAppInstanceId();
                com_google_android_gms_internal_measurement_zzgi.zzafz = zzbl2.getFirebaseInstanceId();
                List zzbk = zzjq().zzbk(com_google_android_gms_measurement_internal_zzh.packageName);
                com_google_android_gms_internal_measurement_zzgi.zzaxc = new zzgl[zzbk.size()];
                for (int i = 0; i < zzbk.size(); i++) {
                    zzgl com_google_android_gms_internal_measurement_zzgl = new zzgl();
                    com_google_android_gms_internal_measurement_zzgi.zzaxc[i] = com_google_android_gms_internal_measurement_zzgl;
                    com_google_android_gms_internal_measurement_zzgl.name = ((zzfj) zzbk.get(i)).name;
                    com_google_android_gms_internal_measurement_zzgl.zzayl = Long.valueOf(((zzfj) zzbk.get(i)).zzaue);
                    zzjo().zza(com_google_android_gms_internal_measurement_zzgl, ((zzfj) zzbk.get(i)).value);
                }
                long zza2 = zzjq().zza(com_google_android_gms_internal_measurement_zzgi);
                zzq zzjq = zzjq();
                if (com_google_android_gms_measurement_internal_zzy.zzaid != null) {
                    Iterator it = com_google_android_gms_measurement_internal_zzy.zzaid.iterator();
                    while (it.hasNext()) {
                        if ("_r".equals((String) it.next())) {
                            z = true;
                            break;
                        }
                    }
                    z = zzln().zzp(com_google_android_gms_measurement_internal_zzy.zztt, com_google_android_gms_measurement_internal_zzy.name);
                    zzr zza3 = zzjq().zza(zzls(), com_google_android_gms_measurement_internal_zzy.zztt, false, false, false, false, false);
                    if (z && zza3.zzahu < ((long) this.zzadj.zzgq().zzat(com_google_android_gms_measurement_internal_zzy.zztt))) {
                        z = true;
                        if (zzjq.zza(com_google_android_gms_measurement_internal_zzy, zza2, z)) {
                            this.zzatl = 0;
                        }
                        zzjq().setTransactionSuccessful();
                        if (this.zzadj.zzgo().isLoggable(2)) {
                            this.zzadj.zzgo().zzjl().zzg("Event recorded", this.zzadj.zzgl().zza(com_google_android_gms_measurement_internal_zzy));
                        }
                        zzjq().endTransaction();
                        zzlv();
                        this.zzadj.zzgo().zzjl().zzg("Background event processing time, ms", Long.valueOf(((System.nanoTime() - nanoTime) + 500000) / 1000000));
                    }
                }
                z = false;
                if (zzjq.zza(com_google_android_gms_measurement_internal_zzy, zza2, z)) {
                    this.zzatl = 0;
                }
                zzjq().setTransactionSuccessful();
                if (this.zzadj.zzgo().isLoggable(2)) {
                    this.zzadj.zzgo().zzjl().zzg("Event recorded", this.zzadj.zzgl().zza(com_google_android_gms_measurement_internal_zzy));
                }
                zzjq().endTransaction();
                zzlv();
                this.zzadj.zzgo().zzjl().zzg("Background event processing time, ms", Long.valueOf(((System.nanoTime() - nanoTime) + 500000) / 1000000));
            } catch (IOException e) {
                this.zzadj.zzgo().zzjd().zze("Data loss. Failed to insert raw event metadata. appId", zzap.zzbv(com_google_android_gms_internal_measurement_zzgi.zztt), e);
            } catch (Throwable th) {
                zzjq().endTransaction();
            }
        }
    }

    private final boolean zza(String str, zzad com_google_android_gms_measurement_internal_zzad) {
        long round;
        Object string = com_google_android_gms_measurement_internal_zzad.zzaid.getString("currency");
        if ("ecommerce_purchase".equals(com_google_android_gms_measurement_internal_zzad.name)) {
            double doubleValue = com_google_android_gms_measurement_internal_zzad.zzaid.zzbq("value").doubleValue() * 1000000.0d;
            if (doubleValue == 0.0d) {
                doubleValue = ((double) com_google_android_gms_measurement_internal_zzad.zzaid.getLong("value").longValue()) * 1000000.0d;
            }
            if (doubleValue > 9.223372036854776E18d || doubleValue < -9.223372036854776E18d) {
                this.zzadj.zzgo().zzjg().zze("Data lost. Currency value is too big. appId", zzap.zzbv(str), Double.valueOf(doubleValue));
                return false;
            }
            round = Math.round(doubleValue);
        } else {
            round = com_google_android_gms_measurement_internal_zzad.zzaid.getLong("value").longValue();
        }
        if (!TextUtils.isEmpty(string)) {
            String toUpperCase = string.toUpperCase(Locale.US);
            if (toUpperCase.matches("[A-Z]{3}")) {
                String concat;
                String valueOf = String.valueOf("_ltv_");
                toUpperCase = String.valueOf(toUpperCase);
                if (toUpperCase.length() != 0) {
                    concat = valueOf.concat(toUpperCase);
                } else {
                    concat = new String(valueOf);
                }
                zzfj zzi = zzjq().zzi(str, concat);
                if (zzi == null || !(zzi.value instanceof Long)) {
                    zzco zzjq = zzjq();
                    int zzb = this.zzadj.zzgq().zzb(str, zzaf.zzakh) - 1;
                    Preconditions.checkNotEmpty(str);
                    zzjq.zzaf();
                    zzjq.zzcl();
                    try {
                        zzjq.getWritableDatabase().execSQL("delete from user_attributes where app_id=? and name in (select name from user_attributes where app_id=? and name like '_ltv_%' order by set_timestamp desc limit ?,10);", new String[]{str, str, String.valueOf(zzb)});
                    } catch (SQLiteException e) {
                        zzjq.zzgo().zzjd().zze("Error pruning currencies. appId", zzap.zzbv(str), e);
                    }
                    zzi = new zzfj(str, com_google_android_gms_measurement_internal_zzad.origin, concat, this.zzadj.zzbx().currentTimeMillis(), Long.valueOf(round));
                } else {
                    valueOf = str;
                    zzi = new zzfj(valueOf, com_google_android_gms_measurement_internal_zzad.origin, concat, this.zzadj.zzbx().currentTimeMillis(), Long.valueOf(round + ((Long) zzi.value).longValue()));
                }
                if (!zzjq().zza(zzi)) {
                    this.zzadj.zzgo().zzjd().zzd("Too many unique user properties are set. Ignoring user property. appId", zzap.zzbv(str), this.zzadj.zzgl().zzbu(zzi.name), zzi.value);
                    this.zzadj.zzgm().zza(str, 9, null, null, 0);
                }
            }
        }
        return true;
    }

    final void zzlt() {
        zzaf();
        zzlr();
        this.zzatr = true;
        String zzid;
        String str;
        try {
            this.zzadj.zzgr();
            Boolean zzle = this.zzadj.zzgg().zzle();
            if (zzle == null) {
                this.zzadj.zzgo().zzjg().zzbx("Upload data called on the client side before use of service was decided");
                this.zzatr = false;
                zzlw();
            } else if (zzle.booleanValue()) {
                this.zzadj.zzgo().zzjd().zzbx("Upload called in the client side when service should be used");
                this.zzatr = false;
                zzlw();
            } else if (this.zzatl > 0) {
                zzlv();
                this.zzatr = false;
                zzlw();
            } else {
                zzaf();
                if ((this.zzatu != null ? 1 : null) != null) {
                    this.zzadj.zzgo().zzjl().zzbx("Uploading requested multiple times");
                    this.zzatr = false;
                    zzlw();
                } else if (zzlo().zzfb()) {
                    long currentTimeMillis = this.zzadj.zzbx().currentTimeMillis();
                    zzd(null, currentTimeMillis - zzn.zzhx());
                    long j = this.zzadj.zzgp().zzane.get();
                    if (j != 0) {
                        this.zzadj.zzgo().zzjk().zzg("Uploading events. Elapsed time since last upload attempt (ms)", Long.valueOf(Math.abs(currentTimeMillis - j)));
                    }
                    zzid = zzjq().zzid();
                    Object zzah;
                    if (TextUtils.isEmpty(zzid)) {
                        this.zzatw = -1;
                        zzah = zzjq().zzah(currentTimeMillis - zzn.zzhx());
                        if (!TextUtils.isEmpty(zzah)) {
                            zzg zzbl = zzjq().zzbl(zzah);
                            if (zzbl != null) {
                                zzb(zzbl);
                            }
                        }
                    } else {
                        if (this.zzatw == -1) {
                            this.zzatw = zzjq().zzik();
                        }
                        List<Pair> zzb = zzjq().zzb(zzid, this.zzadj.zzgq().zzb(zzid, zzaf.zzajj), Math.max(0, this.zzadj.zzgq().zzb(zzid, zzaf.zzajk)));
                        if (!zzb.isEmpty()) {
                            zzgi com_google_android_gms_internal_measurement_zzgi;
                            Object obj;
                            int i;
                            List subList;
                            Object obj2;
                            for (Pair pair : zzb) {
                                com_google_android_gms_internal_measurement_zzgi = (zzgi) pair.first;
                                if (!TextUtils.isEmpty(com_google_android_gms_internal_measurement_zzgi.zzaxo)) {
                                    obj = com_google_android_gms_internal_measurement_zzgi.zzaxo;
                                    break;
                                }
                            }
                            obj = null;
                            if (obj != null) {
                                for (i = 0; i < zzb.size(); i++) {
                                    com_google_android_gms_internal_measurement_zzgi = (zzgi) ((Pair) zzb.get(i)).first;
                                    if (!TextUtils.isEmpty(com_google_android_gms_internal_measurement_zzgi.zzaxo) && !com_google_android_gms_internal_measurement_zzgi.zzaxo.equals(obj)) {
                                        subList = zzb.subList(0, i);
                                        break;
                                    }
                                }
                            }
                            subList = zzb;
                            zzgh com_google_android_gms_internal_measurement_zzgh = new zzgh();
                            com_google_android_gms_internal_measurement_zzgh.zzawy = new zzgi[subList.size()];
                            Collection arrayList = new ArrayList(subList.size());
                            if (zzn.zzhz() && this.zzadj.zzgq().zzav(zzid)) {
                                obj2 = 1;
                            } else {
                                obj2 = null;
                            }
                            for (i = 0; i < com_google_android_gms_internal_measurement_zzgh.zzawy.length; i++) {
                                com_google_android_gms_internal_measurement_zzgh.zzawy[i] = (zzgi) ((Pair) subList.get(i)).first;
                                arrayList.add((Long) ((Pair) subList.get(i)).second);
                                com_google_android_gms_internal_measurement_zzgh.zzawy[i].zzaxn = Long.valueOf(this.zzadj.zzgq().zzhc());
                                com_google_android_gms_internal_measurement_zzgh.zzawy[i].zzaxd = Long.valueOf(currentTimeMillis);
                                com_google_android_gms_internal_measurement_zzgi = com_google_android_gms_internal_measurement_zzgh.zzawy[i];
                                this.zzadj.zzgr();
                                com_google_android_gms_internal_measurement_zzgi.zzaxs = Boolean.valueOf(false);
                                if (obj2 == null) {
                                    com_google_android_gms_internal_measurement_zzgh.zzawy[i].zzaya = null;
                                }
                            }
                            if (this.zzadj.zzgo().isLoggable(2)) {
                                obj2 = zzjo().zzb(com_google_android_gms_internal_measurement_zzgh);
                            } else {
                                obj2 = null;
                            }
                            obj = zzjo().zza(com_google_android_gms_internal_measurement_zzgh);
                            str = (String) zzaf.zzajt.get();
                            URL url = new URL(str);
                            Preconditions.checkArgument(!arrayList.isEmpty());
                            if (this.zzatu != null) {
                                this.zzadj.zzgo().zzjd().zzbx("Set uploading progress before finishing the previous upload");
                            } else {
                                this.zzatu = new ArrayList(arrayList);
                            }
                            this.zzadj.zzgp().zzanf.set(currentTimeMillis);
                            zzah = "?";
                            if (com_google_android_gms_internal_measurement_zzgh.zzawy.length > 0) {
                                zzah = com_google_android_gms_internal_measurement_zzgh.zzawy[0].zztt;
                            }
                            this.zzadj.zzgo().zzjl().zzd("Uploading data. app, uncompressed size, data", zzah, Integer.valueOf(obj.length), obj2);
                            this.zzatq = true;
                            zzco zzlo = zzlo();
                            zzav com_google_android_gms_measurement_internal_zzfc = new zzfc(this, zzid);
                            zzlo.zzaf();
                            zzlo.zzcl();
                            Preconditions.checkNotNull(url);
                            Preconditions.checkNotNull(obj);
                            Preconditions.checkNotNull(com_google_android_gms_measurement_internal_zzfc);
                            zzlo.zzgn().zzd(new zzax(zzlo, zzid, url, obj, null, com_google_android_gms_measurement_internal_zzfc));
                        }
                    }
                    this.zzatr = false;
                    zzlw();
                } else {
                    this.zzadj.zzgo().zzjl().zzbx("Network not connected, ignoring upload request");
                    zzlv();
                    this.zzatr = false;
                    zzlw();
                }
            }
        } catch (MalformedURLException e) {
            this.zzadj.zzgo().zzjd().zze("Failed to parse upload URL. Not uploading. appId", zzap.zzbv(zzid), str);
        } catch (Throwable th) {
            this.zzatr = false;
            zzlw();
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final boolean zzd(java.lang.String r35, long r36) {
        /*
        r34 = this;
        r2 = r34.zzjq();
        r2.beginTransaction();
        r22 = new com.google.android.gms.measurement.internal.zzfa$zza;	 Catch:{ all -> 0x01d5 }
        r2 = 0;
        r0 = r22;
        r1 = r34;
        r0.<init>();	 Catch:{ all -> 0x01d5 }
        r14 = r34.zzjq();	 Catch:{ all -> 0x01d5 }
        r4 = 0;
        r0 = r34;
        r0 = r0.zzatw;	 Catch:{ all -> 0x01d5 }
        r16 = r0;
        com.google.android.gms.common.internal.Preconditions.checkNotNull(r22);	 Catch:{ all -> 0x01d5 }
        r14.zzaf();	 Catch:{ all -> 0x01d5 }
        r14.zzcl();	 Catch:{ all -> 0x01d5 }
        r3 = 0;
        r2 = r14.getWritableDatabase();	 Catch:{ SQLiteException -> 0x0dd3 }
        r5 = 0;
        r5 = android.text.TextUtils.isEmpty(r5);	 Catch:{ SQLiteException -> 0x0dd3 }
        if (r5 == 0) goto L_0x01de;
    L_0x0031:
        r6 = -1;
        r5 = (r16 > r6 ? 1 : (r16 == r6 ? 0 : -1));
        if (r5 == 0) goto L_0x0170;
    L_0x0037:
        r5 = 2;
        r5 = new java.lang.String[r5];	 Catch:{ SQLiteException -> 0x0dd3 }
        r6 = 0;
        r7 = java.lang.String.valueOf(r16);	 Catch:{ SQLiteException -> 0x0dd3 }
        r5[r6] = r7;	 Catch:{ SQLiteException -> 0x0dd3 }
        r6 = 1;
        r7 = java.lang.String.valueOf(r36);	 Catch:{ SQLiteException -> 0x0dd3 }
        r5[r6] = r7;	 Catch:{ SQLiteException -> 0x0dd3 }
        r6 = r5;
    L_0x0049:
        r8 = -1;
        r5 = (r16 > r8 ? 1 : (r16 == r8 ? 0 : -1));
        if (r5 == 0) goto L_0x017d;
    L_0x004f:
        r5 = "rowid <= ? and ";
    L_0x0052:
        r7 = java.lang.String.valueOf(r5);	 Catch:{ SQLiteException -> 0x0dd3 }
        r7 = r7.length();	 Catch:{ SQLiteException -> 0x0dd3 }
        r7 = r7 + 148;
        r8 = new java.lang.StringBuilder;	 Catch:{ SQLiteException -> 0x0dd3 }
        r8.<init>(r7);	 Catch:{ SQLiteException -> 0x0dd3 }
        r7 = "select app_id, metadata_fingerprint from raw_events where ";
        r7 = r8.append(r7);	 Catch:{ SQLiteException -> 0x0dd3 }
        r5 = r7.append(r5);	 Catch:{ SQLiteException -> 0x0dd3 }
        r7 = "app_id in (select app_id from apps where config_fetched_time >= ?) order by rowid limit 1;";
        r5 = r5.append(r7);	 Catch:{ SQLiteException -> 0x0dd3 }
        r5 = r5.toString();	 Catch:{ SQLiteException -> 0x0dd3 }
        r3 = r2.rawQuery(r5, r6);	 Catch:{ SQLiteException -> 0x0dd3 }
        r5 = r3.moveToFirst();	 Catch:{ SQLiteException -> 0x0dd3 }
        if (r5 != 0) goto L_0x0182;
    L_0x0081:
        if (r3 == 0) goto L_0x0086;
    L_0x0083:
        r3.close();	 Catch:{ all -> 0x01d5 }
    L_0x0086:
        r0 = r22;
        r2 = r0.zzauc;	 Catch:{ all -> 0x01d5 }
        if (r2 == 0) goto L_0x0096;
    L_0x008c:
        r0 = r22;
        r2 = r0.zzauc;	 Catch:{ all -> 0x01d5 }
        r2 = r2.isEmpty();	 Catch:{ all -> 0x01d5 }
        if (r2 == 0) goto L_0x0382;
    L_0x0096:
        r2 = 1;
    L_0x0097:
        if (r2 != 0) goto L_0x0dbf;
    L_0x0099:
        r18 = 0;
        r0 = r22;
        r0 = r0.zzaua;	 Catch:{ all -> 0x01d5 }
        r23 = r0;
        r0 = r22;
        r2 = r0.zzauc;	 Catch:{ all -> 0x01d5 }
        r2 = r2.size();	 Catch:{ all -> 0x01d5 }
        r2 = new com.google.android.gms.internal.measurement.zzgf[r2];	 Catch:{ all -> 0x01d5 }
        r0 = r23;
        r0.zzaxb = r2;	 Catch:{ all -> 0x01d5 }
        r13 = 0;
        r14 = 0;
        r0 = r34;
        r2 = r0.zzadj;	 Catch:{ all -> 0x01d5 }
        r2 = r2.zzgq();	 Catch:{ all -> 0x01d5 }
        r0 = r23;
        r3 = r0.zztt;	 Catch:{ all -> 0x01d5 }
        r17 = r2.zzax(r3);	 Catch:{ all -> 0x01d5 }
        r2 = 0;
        r16 = r2;
    L_0x00c5:
        r0 = r22;
        r2 = r0.zzauc;	 Catch:{ all -> 0x01d5 }
        r2 = r2.size();	 Catch:{ all -> 0x01d5 }
        r0 = r16;
        if (r0 >= r2) goto L_0x0713;
    L_0x00d1:
        r0 = r22;
        r2 = r0.zzauc;	 Catch:{ all -> 0x01d5 }
        r0 = r16;
        r2 = r2.get(r0);	 Catch:{ all -> 0x01d5 }
        r0 = r2;
        r0 = (com.google.android.gms.internal.measurement.zzgf) r0;	 Catch:{ all -> 0x01d5 }
        r12 = r0;
        r2 = r34.zzln();	 Catch:{ all -> 0x01d5 }
        r0 = r22;
        r3 = r0.zzaua;	 Catch:{ all -> 0x01d5 }
        r3 = r3.zztt;	 Catch:{ all -> 0x01d5 }
        r4 = r12.name;	 Catch:{ all -> 0x01d5 }
        r2 = r2.zzo(r3, r4);	 Catch:{ all -> 0x01d5 }
        if (r2 == 0) goto L_0x0388;
    L_0x00f1:
        r0 = r34;
        r2 = r0.zzadj;	 Catch:{ all -> 0x01d5 }
        r2 = r2.zzgo();	 Catch:{ all -> 0x01d5 }
        r2 = r2.zzjg();	 Catch:{ all -> 0x01d5 }
        r3 = "Dropping blacklisted raw event. appId";
        r0 = r22;
        r4 = r0.zzaua;	 Catch:{ all -> 0x01d5 }
        r4 = r4.zztt;	 Catch:{ all -> 0x01d5 }
        r4 = com.google.android.gms.measurement.internal.zzap.zzbv(r4);	 Catch:{ all -> 0x01d5 }
        r0 = r34;
        r5 = r0.zzadj;	 Catch:{ all -> 0x01d5 }
        r5 = r5.zzgl();	 Catch:{ all -> 0x01d5 }
        r6 = r12.name;	 Catch:{ all -> 0x01d5 }
        r5 = r5.zzbs(r6);	 Catch:{ all -> 0x01d5 }
        r2.zze(r3, r4, r5);	 Catch:{ all -> 0x01d5 }
        r2 = r34.zzln();	 Catch:{ all -> 0x01d5 }
        r0 = r22;
        r3 = r0.zzaua;	 Catch:{ all -> 0x01d5 }
        r3 = r3.zztt;	 Catch:{ all -> 0x01d5 }
        r2 = r2.zzck(r3);	 Catch:{ all -> 0x01d5 }
        if (r2 != 0) goto L_0x013b;
    L_0x012b:
        r2 = r34.zzln();	 Catch:{ all -> 0x01d5 }
        r0 = r22;
        r3 = r0.zzaua;	 Catch:{ all -> 0x01d5 }
        r3 = r3.zztt;	 Catch:{ all -> 0x01d5 }
        r2 = r2.zzcl(r3);	 Catch:{ all -> 0x01d5 }
        if (r2 == 0) goto L_0x0385;
    L_0x013b:
        r2 = 1;
    L_0x013c:
        if (r2 != 0) goto L_0x0df4;
    L_0x013e:
        r2 = "_err";
        r3 = r12.name;	 Catch:{ all -> 0x01d5 }
        r2 = r2.equals(r3);	 Catch:{ all -> 0x01d5 }
        if (r2 != 0) goto L_0x0df4;
    L_0x0149:
        r0 = r34;
        r2 = r0.zzadj;	 Catch:{ all -> 0x01d5 }
        r2 = r2.zzgm();	 Catch:{ all -> 0x01d5 }
        r0 = r22;
        r3 = r0.zzaua;	 Catch:{ all -> 0x01d5 }
        r3 = r3.zztt;	 Catch:{ all -> 0x01d5 }
        r4 = 11;
        r5 = "_ev";
        r6 = r12.name;	 Catch:{ all -> 0x01d5 }
        r7 = 0;
        r2.zza(r3, r4, r5, r6, r7);	 Catch:{ all -> 0x01d5 }
        r2 = r14;
        r4 = r13;
        r5 = r18;
    L_0x0166:
        r6 = r16 + 1;
        r16 = r6;
        r14 = r2;
        r13 = r4;
        r18 = r5;
        goto L_0x00c5;
    L_0x0170:
        r5 = 1;
        r5 = new java.lang.String[r5];	 Catch:{ SQLiteException -> 0x0dd3 }
        r6 = 0;
        r7 = java.lang.String.valueOf(r36);	 Catch:{ SQLiteException -> 0x0dd3 }
        r5[r6] = r7;	 Catch:{ SQLiteException -> 0x0dd3 }
        r6 = r5;
        goto L_0x0049;
    L_0x017d:
        r5 = "";
        goto L_0x0052;
    L_0x0182:
        r5 = 0;
        r4 = r3.getString(r5);	 Catch:{ SQLiteException -> 0x0dd3 }
        r5 = 1;
        r5 = r3.getString(r5);	 Catch:{ SQLiteException -> 0x0dd3 }
        r3.close();	 Catch:{ SQLiteException -> 0x0dd3 }
        r13 = r5;
        r11 = r3;
        r12 = r4;
    L_0x0192:
        r3 = "raw_events_metadata";
        r4 = 1;
        r4 = new java.lang.String[r4];	 Catch:{ SQLiteException -> 0x0306, all -> 0x0dcf }
        r5 = 0;
        r6 = "metadata";
        r4[r5] = r6;	 Catch:{ SQLiteException -> 0x0306, all -> 0x0dcf }
        r5 = "app_id = ? and metadata_fingerprint = ?";
        r6 = 2;
        r6 = new java.lang.String[r6];	 Catch:{ SQLiteException -> 0x0306, all -> 0x0dcf }
        r7 = 0;
        r6[r7] = r12;	 Catch:{ SQLiteException -> 0x0306, all -> 0x0dcf }
        r7 = 1;
        r6[r7] = r13;	 Catch:{ SQLiteException -> 0x0306, all -> 0x0dcf }
        r7 = 0;
        r8 = 0;
        r9 = "rowid";
        r10 = "2";
        r11 = r2.query(r3, r4, r5, r6, r7, r8, r9, r10);	 Catch:{ SQLiteException -> 0x0306, all -> 0x0dcf }
        r3 = r11.moveToFirst();	 Catch:{ SQLiteException -> 0x0306, all -> 0x0dcf }
        if (r3 != 0) goto L_0x024c;
    L_0x01bc:
        r2 = r14.zzgo();	 Catch:{ SQLiteException -> 0x0306, all -> 0x0dcf }
        r2 = r2.zzjd();	 Catch:{ SQLiteException -> 0x0306, all -> 0x0dcf }
        r3 = "Raw event metadata record is missing. appId";
        r4 = com.google.android.gms.measurement.internal.zzap.zzbv(r12);	 Catch:{ SQLiteException -> 0x0306, all -> 0x0dcf }
        r2.zzg(r3, r4);	 Catch:{ SQLiteException -> 0x0306, all -> 0x0dcf }
        if (r11 == 0) goto L_0x0086;
    L_0x01d0:
        r11.close();	 Catch:{ all -> 0x01d5 }
        goto L_0x0086;
    L_0x01d5:
        r2 = move-exception;
        r3 = r34.zzjq();
        r3.endTransaction();
        throw r2;
    L_0x01de:
        r6 = -1;
        r5 = (r16 > r6 ? 1 : (r16 == r6 ? 0 : -1));
        if (r5 == 0) goto L_0x0232;
    L_0x01e4:
        r5 = 2;
        r5 = new java.lang.String[r5];	 Catch:{ SQLiteException -> 0x0dd3 }
        r6 = 0;
        r7 = 0;
        r5[r6] = r7;	 Catch:{ SQLiteException -> 0x0dd3 }
        r6 = 1;
        r7 = java.lang.String.valueOf(r16);	 Catch:{ SQLiteException -> 0x0dd3 }
        r5[r6] = r7;	 Catch:{ SQLiteException -> 0x0dd3 }
        r6 = r5;
    L_0x01f3:
        r8 = -1;
        r5 = (r16 > r8 ? 1 : (r16 == r8 ? 0 : -1));
        if (r5 == 0) goto L_0x023b;
    L_0x01f9:
        r5 = " and rowid <= ?";
    L_0x01fc:
        r7 = java.lang.String.valueOf(r5);	 Catch:{ SQLiteException -> 0x0dd3 }
        r7 = r7.length();	 Catch:{ SQLiteException -> 0x0dd3 }
        r7 = r7 + 84;
        r8 = new java.lang.StringBuilder;	 Catch:{ SQLiteException -> 0x0dd3 }
        r8.<init>(r7);	 Catch:{ SQLiteException -> 0x0dd3 }
        r7 = "select metadata_fingerprint from raw_events where app_id = ?";
        r7 = r8.append(r7);	 Catch:{ SQLiteException -> 0x0dd3 }
        r5 = r7.append(r5);	 Catch:{ SQLiteException -> 0x0dd3 }
        r7 = " order by rowid limit 1;";
        r5 = r5.append(r7);	 Catch:{ SQLiteException -> 0x0dd3 }
        r5 = r5.toString();	 Catch:{ SQLiteException -> 0x0dd3 }
        r3 = r2.rawQuery(r5, r6);	 Catch:{ SQLiteException -> 0x0dd3 }
        r5 = r3.moveToFirst();	 Catch:{ SQLiteException -> 0x0dd3 }
        if (r5 != 0) goto L_0x023f;
    L_0x022b:
        if (r3 == 0) goto L_0x0086;
    L_0x022d:
        r3.close();	 Catch:{ all -> 0x01d5 }
        goto L_0x0086;
    L_0x0232:
        r5 = 1;
        r5 = new java.lang.String[r5];	 Catch:{ SQLiteException -> 0x0dd3 }
        r6 = 0;
        r7 = 0;
        r5[r6] = r7;	 Catch:{ SQLiteException -> 0x0dd3 }
        r6 = r5;
        goto L_0x01f3;
    L_0x023b:
        r5 = "";
        goto L_0x01fc;
    L_0x023f:
        r5 = 0;
        r5 = r3.getString(r5);	 Catch:{ SQLiteException -> 0x0dd3 }
        r3.close();	 Catch:{ SQLiteException -> 0x0dd3 }
        r13 = r5;
        r11 = r3;
        r12 = r4;
        goto L_0x0192;
    L_0x024c:
        r3 = 0;
        r3 = r11.getBlob(r3);	 Catch:{ SQLiteException -> 0x0306, all -> 0x0dcf }
        r4 = 0;
        r5 = r3.length;	 Catch:{ SQLiteException -> 0x0306, all -> 0x0dcf }
        r3 = com.google.android.gms.internal.measurement.zzyx.zzj(r3, r4, r5);	 Catch:{ SQLiteException -> 0x0306, all -> 0x0dcf }
        r4 = new com.google.android.gms.internal.measurement.zzgi;	 Catch:{ SQLiteException -> 0x0306, all -> 0x0dcf }
        r4.<init>();	 Catch:{ SQLiteException -> 0x0306, all -> 0x0dcf }
        r4.zza(r3);	 Catch:{ IOException -> 0x02df }
        r3 = r11.moveToNext();	 Catch:{ SQLiteException -> 0x0306, all -> 0x0dcf }
        if (r3 == 0) goto L_0x0277;
    L_0x0265:
        r3 = r14.zzgo();	 Catch:{ SQLiteException -> 0x0306, all -> 0x0dcf }
        r3 = r3.zzjg();	 Catch:{ SQLiteException -> 0x0306, all -> 0x0dcf }
        r5 = "Get multiple raw event metadata records, expected one. appId";
        r6 = com.google.android.gms.measurement.internal.zzap.zzbv(r12);	 Catch:{ SQLiteException -> 0x0306, all -> 0x0dcf }
        r3.zzg(r5, r6);	 Catch:{ SQLiteException -> 0x0306, all -> 0x0dcf }
    L_0x0277:
        r11.close();	 Catch:{ SQLiteException -> 0x0306, all -> 0x0dcf }
        r0 = r22;
        r0.zzb(r4);	 Catch:{ SQLiteException -> 0x0306, all -> 0x0dcf }
        r4 = -1;
        r3 = (r16 > r4 ? 1 : (r16 == r4 ? 0 : -1));
        if (r3 == 0) goto L_0x02f9;
    L_0x0285:
        r5 = "app_id = ? and metadata_fingerprint = ? and rowid <= ?";
        r3 = 3;
        r6 = new java.lang.String[r3];	 Catch:{ SQLiteException -> 0x0306, all -> 0x0dcf }
        r3 = 0;
        r6[r3] = r12;	 Catch:{ SQLiteException -> 0x0306, all -> 0x0dcf }
        r3 = 1;
        r6[r3] = r13;	 Catch:{ SQLiteException -> 0x0306, all -> 0x0dcf }
        r3 = 2;
        r4 = java.lang.String.valueOf(r16);	 Catch:{ SQLiteException -> 0x0306, all -> 0x0dcf }
        r6[r3] = r4;	 Catch:{ SQLiteException -> 0x0306, all -> 0x0dcf }
    L_0x0298:
        r3 = "raw_events";
        r4 = 4;
        r4 = new java.lang.String[r4];	 Catch:{ SQLiteException -> 0x0306, all -> 0x0dcf }
        r7 = 0;
        r8 = "rowid";
        r4[r7] = r8;	 Catch:{ SQLiteException -> 0x0306, all -> 0x0dcf }
        r7 = 1;
        r8 = "name";
        r4[r7] = r8;	 Catch:{ SQLiteException -> 0x0306, all -> 0x0dcf }
        r7 = 2;
        r8 = "timestamp";
        r4[r7] = r8;	 Catch:{ SQLiteException -> 0x0306, all -> 0x0dcf }
        r7 = 3;
        r8 = "data";
        r4[r7] = r8;	 Catch:{ SQLiteException -> 0x0306, all -> 0x0dcf }
        r7 = 0;
        r8 = 0;
        r9 = "rowid";
        r10 = 0;
        r3 = r2.query(r3, r4, r5, r6, r7, r8, r9, r10);	 Catch:{ SQLiteException -> 0x0306, all -> 0x0dcf }
        r2 = r3.moveToFirst();	 Catch:{ SQLiteException -> 0x0dd6 }
        if (r2 != 0) goto L_0x0322;
    L_0x02c6:
        r2 = r14.zzgo();	 Catch:{ SQLiteException -> 0x0dd6 }
        r2 = r2.zzjg();	 Catch:{ SQLiteException -> 0x0dd6 }
        r4 = "Raw event data disappeared while in transaction. appId";
        r5 = com.google.android.gms.measurement.internal.zzap.zzbv(r12);	 Catch:{ SQLiteException -> 0x0dd6 }
        r2.zzg(r4, r5);	 Catch:{ SQLiteException -> 0x0dd6 }
        if (r3 == 0) goto L_0x0086;
    L_0x02da:
        r3.close();	 Catch:{ all -> 0x01d5 }
        goto L_0x0086;
    L_0x02df:
        r2 = move-exception;
        r3 = r14.zzgo();	 Catch:{ SQLiteException -> 0x0306, all -> 0x0dcf }
        r3 = r3.zzjd();	 Catch:{ SQLiteException -> 0x0306, all -> 0x0dcf }
        r4 = "Data loss. Failed to merge raw event metadata. appId";
        r5 = com.google.android.gms.measurement.internal.zzap.zzbv(r12);	 Catch:{ SQLiteException -> 0x0306, all -> 0x0dcf }
        r3.zze(r4, r5, r2);	 Catch:{ SQLiteException -> 0x0306, all -> 0x0dcf }
        if (r11 == 0) goto L_0x0086;
    L_0x02f4:
        r11.close();	 Catch:{ all -> 0x01d5 }
        goto L_0x0086;
    L_0x02f9:
        r5 = "app_id = ? and metadata_fingerprint = ?";
        r3 = 2;
        r6 = new java.lang.String[r3];	 Catch:{ SQLiteException -> 0x0306, all -> 0x0dcf }
        r3 = 0;
        r6[r3] = r12;	 Catch:{ SQLiteException -> 0x0306, all -> 0x0dcf }
        r3 = 1;
        r6[r3] = r13;	 Catch:{ SQLiteException -> 0x0306, all -> 0x0dcf }
        goto L_0x0298;
    L_0x0306:
        r2 = move-exception;
        r3 = r11;
        r4 = r12;
    L_0x0309:
        r5 = r14.zzgo();	 Catch:{ all -> 0x037b }
        r5 = r5.zzjd();	 Catch:{ all -> 0x037b }
        r6 = "Data loss. Error selecting raw event. appId";
        r4 = com.google.android.gms.measurement.internal.zzap.zzbv(r4);	 Catch:{ all -> 0x037b }
        r5.zze(r6, r4, r2);	 Catch:{ all -> 0x037b }
        if (r3 == 0) goto L_0x0086;
    L_0x031d:
        r3.close();	 Catch:{ all -> 0x01d5 }
        goto L_0x0086;
    L_0x0322:
        r2 = 0;
        r4 = r3.getLong(r2);	 Catch:{ SQLiteException -> 0x0dd6 }
        r2 = 3;
        r2 = r3.getBlob(r2);	 Catch:{ SQLiteException -> 0x0dd6 }
        r6 = 0;
        r7 = r2.length;	 Catch:{ SQLiteException -> 0x0dd6 }
        r2 = com.google.android.gms.internal.measurement.zzyx.zzj(r2, r6, r7);	 Catch:{ SQLiteException -> 0x0dd6 }
        r6 = new com.google.android.gms.internal.measurement.zzgf;	 Catch:{ SQLiteException -> 0x0dd6 }
        r6.<init>();	 Catch:{ SQLiteException -> 0x0dd6 }
        r6.zza(r2);	 Catch:{ IOException -> 0x035b }
        r2 = 1;
        r2 = r3.getString(r2);	 Catch:{ SQLiteException -> 0x0dd6 }
        r6.name = r2;	 Catch:{ SQLiteException -> 0x0dd6 }
        r2 = 2;
        r8 = r3.getLong(r2);	 Catch:{ SQLiteException -> 0x0dd6 }
        r2 = java.lang.Long.valueOf(r8);	 Catch:{ SQLiteException -> 0x0dd6 }
        r6.zzawu = r2;	 Catch:{ SQLiteException -> 0x0dd6 }
        r0 = r22;
        r2 = r0.zza(r4, r6);	 Catch:{ SQLiteException -> 0x0dd6 }
        if (r2 != 0) goto L_0x036e;
    L_0x0354:
        if (r3 == 0) goto L_0x0086;
    L_0x0356:
        r3.close();	 Catch:{ all -> 0x01d5 }
        goto L_0x0086;
    L_0x035b:
        r2 = move-exception;
        r4 = r14.zzgo();	 Catch:{ SQLiteException -> 0x0dd6 }
        r4 = r4.zzjd();	 Catch:{ SQLiteException -> 0x0dd6 }
        r5 = "Data loss. Failed to merge raw event. appId";
        r6 = com.google.android.gms.measurement.internal.zzap.zzbv(r12);	 Catch:{ SQLiteException -> 0x0dd6 }
        r4.zze(r5, r6, r2);	 Catch:{ SQLiteException -> 0x0dd6 }
    L_0x036e:
        r2 = r3.moveToNext();	 Catch:{ SQLiteException -> 0x0dd6 }
        if (r2 != 0) goto L_0x0322;
    L_0x0374:
        if (r3 == 0) goto L_0x0086;
    L_0x0376:
        r3.close();	 Catch:{ all -> 0x01d5 }
        goto L_0x0086;
    L_0x037b:
        r2 = move-exception;
    L_0x037c:
        if (r3 == 0) goto L_0x0381;
    L_0x037e:
        r3.close();	 Catch:{ all -> 0x01d5 }
    L_0x0381:
        throw r2;	 Catch:{ all -> 0x01d5 }
    L_0x0382:
        r2 = 0;
        goto L_0x0097;
    L_0x0385:
        r2 = 0;
        goto L_0x013c;
    L_0x0388:
        r2 = r34.zzln();	 Catch:{ all -> 0x01d5 }
        r0 = r22;
        r3 = r0.zzaua;	 Catch:{ all -> 0x01d5 }
        r3 = r3.zztt;	 Catch:{ all -> 0x01d5 }
        r4 = r12.name;	 Catch:{ all -> 0x01d5 }
        r19 = r2.zzp(r3, r4);	 Catch:{ all -> 0x01d5 }
        if (r19 != 0) goto L_0x03b0;
    L_0x039a:
        r34.zzjo();	 Catch:{ all -> 0x01d5 }
        r3 = r12.name;	 Catch:{ all -> 0x01d5 }
        com.google.android.gms.common.internal.Preconditions.checkNotEmpty(r3);	 Catch:{ all -> 0x01d5 }
        r2 = -1;
        r4 = r3.hashCode();	 Catch:{ all -> 0x01d5 }
        switch(r4) {
            case 94660: goto L_0x03dd;
            case 95025: goto L_0x03f3;
            case 95027: goto L_0x03e8;
            default: goto L_0x03aa;
        };	 Catch:{ all -> 0x01d5 }
    L_0x03aa:
        switch(r2) {
            case 0: goto L_0x03fe;
            case 1: goto L_0x03fe;
            case 2: goto L_0x03fe;
            default: goto L_0x03ad;
        };	 Catch:{ all -> 0x01d5 }
    L_0x03ad:
        r2 = 0;
    L_0x03ae:
        if (r2 == 0) goto L_0x0602;
    L_0x03b0:
        r4 = 0;
        r3 = 0;
        r2 = r12.zzawt;	 Catch:{ all -> 0x01d5 }
        if (r2 != 0) goto L_0x03bb;
    L_0x03b6:
        r2 = 0;
        r2 = new com.google.android.gms.internal.measurement.zzgg[r2];	 Catch:{ all -> 0x01d5 }
        r12.zzawt = r2;	 Catch:{ all -> 0x01d5 }
    L_0x03bb:
        r6 = r12.zzawt;	 Catch:{ all -> 0x01d5 }
        r7 = r6.length;	 Catch:{ all -> 0x01d5 }
        r2 = 0;
        r5 = r2;
    L_0x03c0:
        if (r5 >= r7) goto L_0x0415;
    L_0x03c2:
        r2 = r6[r5];	 Catch:{ all -> 0x01d5 }
        r8 = "_c";
        r9 = r2.name;	 Catch:{ all -> 0x01d5 }
        r8 = r8.equals(r9);	 Catch:{ all -> 0x01d5 }
        if (r8 == 0) goto L_0x0400;
    L_0x03cf:
        r8 = 1;
        r4 = java.lang.Long.valueOf(r8);	 Catch:{ all -> 0x01d5 }
        r2.zzawx = r4;	 Catch:{ all -> 0x01d5 }
        r4 = 1;
        r2 = r3;
    L_0x03d9:
        r5 = r5 + 1;
        r3 = r2;
        goto L_0x03c0;
    L_0x03dd:
        r4 = "_in";
        r3 = r3.equals(r4);	 Catch:{ all -> 0x01d5 }
        if (r3 == 0) goto L_0x03aa;
    L_0x03e6:
        r2 = 0;
        goto L_0x03aa;
    L_0x03e8:
        r4 = "_ui";
        r3 = r3.equals(r4);	 Catch:{ all -> 0x01d5 }
        if (r3 == 0) goto L_0x03aa;
    L_0x03f1:
        r2 = 1;
        goto L_0x03aa;
    L_0x03f3:
        r4 = "_ug";
        r3 = r3.equals(r4);	 Catch:{ all -> 0x01d5 }
        if (r3 == 0) goto L_0x03aa;
    L_0x03fc:
        r2 = 2;
        goto L_0x03aa;
    L_0x03fe:
        r2 = 1;
        goto L_0x03ae;
    L_0x0400:
        r8 = "_r";
        r9 = r2.name;	 Catch:{ all -> 0x01d5 }
        r8 = r8.equals(r9);	 Catch:{ all -> 0x01d5 }
        if (r8 == 0) goto L_0x0df1;
    L_0x040b:
        r8 = 1;
        r3 = java.lang.Long.valueOf(r8);	 Catch:{ all -> 0x01d5 }
        r2.zzawx = r3;	 Catch:{ all -> 0x01d5 }
        r2 = 1;
        goto L_0x03d9;
    L_0x0415:
        if (r4 != 0) goto L_0x045f;
    L_0x0417:
        if (r19 == 0) goto L_0x045f;
    L_0x0419:
        r0 = r34;
        r2 = r0.zzadj;	 Catch:{ all -> 0x01d5 }
        r2 = r2.zzgo();	 Catch:{ all -> 0x01d5 }
        r2 = r2.zzjl();	 Catch:{ all -> 0x01d5 }
        r4 = "Marking event as conversion";
        r0 = r34;
        r5 = r0.zzadj;	 Catch:{ all -> 0x01d5 }
        r5 = r5.zzgl();	 Catch:{ all -> 0x01d5 }
        r6 = r12.name;	 Catch:{ all -> 0x01d5 }
        r5 = r5.zzbs(r6);	 Catch:{ all -> 0x01d5 }
        r2.zzg(r4, r5);	 Catch:{ all -> 0x01d5 }
        r2 = r12.zzawt;	 Catch:{ all -> 0x01d5 }
        r4 = r12.zzawt;	 Catch:{ all -> 0x01d5 }
        r4 = r4.length;	 Catch:{ all -> 0x01d5 }
        r4 = r4 + 1;
        r2 = java.util.Arrays.copyOf(r2, r4);	 Catch:{ all -> 0x01d5 }
        r2 = (com.google.android.gms.internal.measurement.zzgg[]) r2;	 Catch:{ all -> 0x01d5 }
        r4 = new com.google.android.gms.internal.measurement.zzgg;	 Catch:{ all -> 0x01d5 }
        r4.<init>();	 Catch:{ all -> 0x01d5 }
        r5 = "_c";
        r4.name = r5;	 Catch:{ all -> 0x01d5 }
        r6 = 1;
        r5 = java.lang.Long.valueOf(r6);	 Catch:{ all -> 0x01d5 }
        r4.zzawx = r5;	 Catch:{ all -> 0x01d5 }
        r5 = r2.length;	 Catch:{ all -> 0x01d5 }
        r5 = r5 + -1;
        r2[r5] = r4;	 Catch:{ all -> 0x01d5 }
        r12.zzawt = r2;	 Catch:{ all -> 0x01d5 }
    L_0x045f:
        if (r3 != 0) goto L_0x04a7;
    L_0x0461:
        r0 = r34;
        r2 = r0.zzadj;	 Catch:{ all -> 0x01d5 }
        r2 = r2.zzgo();	 Catch:{ all -> 0x01d5 }
        r2 = r2.zzjl();	 Catch:{ all -> 0x01d5 }
        r3 = "Marking event as real-time";
        r0 = r34;
        r4 = r0.zzadj;	 Catch:{ all -> 0x01d5 }
        r4 = r4.zzgl();	 Catch:{ all -> 0x01d5 }
        r5 = r12.name;	 Catch:{ all -> 0x01d5 }
        r4 = r4.zzbs(r5);	 Catch:{ all -> 0x01d5 }
        r2.zzg(r3, r4);	 Catch:{ all -> 0x01d5 }
        r2 = r12.zzawt;	 Catch:{ all -> 0x01d5 }
        r3 = r12.zzawt;	 Catch:{ all -> 0x01d5 }
        r3 = r3.length;	 Catch:{ all -> 0x01d5 }
        r3 = r3 + 1;
        r2 = java.util.Arrays.copyOf(r2, r3);	 Catch:{ all -> 0x01d5 }
        r2 = (com.google.android.gms.internal.measurement.zzgg[]) r2;	 Catch:{ all -> 0x01d5 }
        r3 = new com.google.android.gms.internal.measurement.zzgg;	 Catch:{ all -> 0x01d5 }
        r3.<init>();	 Catch:{ all -> 0x01d5 }
        r4 = "_r";
        r3.name = r4;	 Catch:{ all -> 0x01d5 }
        r4 = 1;
        r4 = java.lang.Long.valueOf(r4);	 Catch:{ all -> 0x01d5 }
        r3.zzawx = r4;	 Catch:{ all -> 0x01d5 }
        r4 = r2.length;	 Catch:{ all -> 0x01d5 }
        r4 = r4 + -1;
        r2[r4] = r3;	 Catch:{ all -> 0x01d5 }
        r12.zzawt = r2;	 Catch:{ all -> 0x01d5 }
    L_0x04a7:
        r2 = 1;
        r3 = r34.zzjq();	 Catch:{ all -> 0x01d5 }
        r4 = r34.zzls();	 Catch:{ all -> 0x01d5 }
        r0 = r22;
        r6 = r0.zzaua;	 Catch:{ all -> 0x01d5 }
        r6 = r6.zztt;	 Catch:{ all -> 0x01d5 }
        r7 = 0;
        r8 = 0;
        r9 = 0;
        r10 = 0;
        r11 = 1;
        r3 = r3.zza(r4, r6, r7, r8, r9, r10, r11);	 Catch:{ all -> 0x01d5 }
        r4 = r3.zzahu;	 Catch:{ all -> 0x01d5 }
        r0 = r34;
        r3 = r0.zzadj;	 Catch:{ all -> 0x01d5 }
        r3 = r3.zzgq();	 Catch:{ all -> 0x01d5 }
        r0 = r22;
        r6 = r0.zzaua;	 Catch:{ all -> 0x01d5 }
        r6 = r6.zztt;	 Catch:{ all -> 0x01d5 }
        r3 = r3.zzat(r6);	 Catch:{ all -> 0x01d5 }
        r6 = (long) r3;	 Catch:{ all -> 0x01d5 }
        r3 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r3 <= 0) goto L_0x0ded;
    L_0x04d8:
        r2 = 0;
    L_0x04d9:
        r3 = r12.zzawt;	 Catch:{ all -> 0x01d5 }
        r3 = r3.length;	 Catch:{ all -> 0x01d5 }
        if (r2 >= r3) goto L_0x050b;
    L_0x04de:
        r3 = "_r";
        r4 = r12.zzawt;	 Catch:{ all -> 0x01d5 }
        r4 = r4[r2];	 Catch:{ all -> 0x01d5 }
        r4 = r4.name;	 Catch:{ all -> 0x01d5 }
        r3 = r3.equals(r4);	 Catch:{ all -> 0x01d5 }
        if (r3 == 0) goto L_0x057d;
    L_0x04ed:
        r3 = r12.zzawt;	 Catch:{ all -> 0x01d5 }
        r3 = r3.length;	 Catch:{ all -> 0x01d5 }
        r3 = r3 + -1;
        r3 = new com.google.android.gms.internal.measurement.zzgg[r3];	 Catch:{ all -> 0x01d5 }
        if (r2 <= 0) goto L_0x04fd;
    L_0x04f6:
        r4 = r12.zzawt;	 Catch:{ all -> 0x01d5 }
        r5 = 0;
        r6 = 0;
        java.lang.System.arraycopy(r4, r5, r3, r6, r2);	 Catch:{ all -> 0x01d5 }
    L_0x04fd:
        r4 = r3.length;	 Catch:{ all -> 0x01d5 }
        if (r2 >= r4) goto L_0x0509;
    L_0x0500:
        r4 = r12.zzawt;	 Catch:{ all -> 0x01d5 }
        r5 = r2 + 1;
        r6 = r3.length;	 Catch:{ all -> 0x01d5 }
        r6 = r6 - r2;
        java.lang.System.arraycopy(r4, r5, r3, r2, r6);	 Catch:{ all -> 0x01d5 }
    L_0x0509:
        r12.zzawt = r3;	 Catch:{ all -> 0x01d5 }
    L_0x050b:
        r2 = r12.name;	 Catch:{ all -> 0x01d5 }
        r2 = com.google.android.gms.measurement.internal.zzfk.zzcq(r2);	 Catch:{ all -> 0x01d5 }
        if (r2 == 0) goto L_0x0602;
    L_0x0513:
        if (r19 == 0) goto L_0x0602;
    L_0x0515:
        r3 = r34.zzjq();	 Catch:{ all -> 0x01d5 }
        r4 = r34.zzls();	 Catch:{ all -> 0x01d5 }
        r0 = r22;
        r2 = r0.zzaua;	 Catch:{ all -> 0x01d5 }
        r6 = r2.zztt;	 Catch:{ all -> 0x01d5 }
        r7 = 0;
        r8 = 0;
        r9 = 1;
        r10 = 0;
        r11 = 0;
        r2 = r3.zza(r4, r6, r7, r8, r9, r10, r11);	 Catch:{ all -> 0x01d5 }
        r2 = r2.zzahs;	 Catch:{ all -> 0x01d5 }
        r0 = r34;
        r4 = r0.zzadj;	 Catch:{ all -> 0x01d5 }
        r4 = r4.zzgq();	 Catch:{ all -> 0x01d5 }
        r0 = r22;
        r5 = r0.zzaua;	 Catch:{ all -> 0x01d5 }
        r5 = r5.zztt;	 Catch:{ all -> 0x01d5 }
        r6 = com.google.android.gms.measurement.internal.zzaf.zzajq;	 Catch:{ all -> 0x01d5 }
        r4 = r4.zzb(r5, r6);	 Catch:{ all -> 0x01d5 }
        r4 = (long) r4;	 Catch:{ all -> 0x01d5 }
        r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
        if (r2 <= 0) goto L_0x0602;
    L_0x0547:
        r0 = r34;
        r2 = r0.zzadj;	 Catch:{ all -> 0x01d5 }
        r2 = r2.zzgo();	 Catch:{ all -> 0x01d5 }
        r2 = r2.zzjg();	 Catch:{ all -> 0x01d5 }
        r3 = "Too many conversions. Not logging as conversion. appId";
        r0 = r22;
        r4 = r0.zzaua;	 Catch:{ all -> 0x01d5 }
        r4 = r4.zztt;	 Catch:{ all -> 0x01d5 }
        r4 = com.google.android.gms.measurement.internal.zzap.zzbv(r4);	 Catch:{ all -> 0x01d5 }
        r2.zzg(r3, r4);	 Catch:{ all -> 0x01d5 }
        r4 = 0;
        r3 = 0;
        r6 = r12.zzawt;	 Catch:{ all -> 0x01d5 }
        r7 = r6.length;	 Catch:{ all -> 0x01d5 }
        r2 = 0;
        r5 = r2;
    L_0x056a:
        if (r5 >= r7) goto L_0x058f;
    L_0x056c:
        r2 = r6[r5];	 Catch:{ all -> 0x01d5 }
        r8 = "_c";
        r9 = r2.name;	 Catch:{ all -> 0x01d5 }
        r8 = r8.equals(r9);	 Catch:{ all -> 0x01d5 }
        if (r8 == 0) goto L_0x0581;
    L_0x0579:
        r5 = r5 + 1;
        r3 = r2;
        goto L_0x056a;
    L_0x057d:
        r2 = r2 + 1;
        goto L_0x04d9;
    L_0x0581:
        r8 = "_err";
        r2 = r2.name;	 Catch:{ all -> 0x01d5 }
        r2 = r8.equals(r2);	 Catch:{ all -> 0x01d5 }
        if (r2 == 0) goto L_0x0dea;
    L_0x058c:
        r4 = 1;
        r2 = r3;
        goto L_0x0579;
    L_0x058f:
        if (r4 == 0) goto L_0x05d4;
    L_0x0591:
        if (r3 == 0) goto L_0x05d4;
    L_0x0593:
        r2 = r12.zzawt;	 Catch:{ all -> 0x01d5 }
        r4 = 1;
        r4 = new com.google.android.gms.internal.measurement.zzgg[r4];	 Catch:{ all -> 0x01d5 }
        r5 = 0;
        r4[r5] = r3;	 Catch:{ all -> 0x01d5 }
        r2 = com.google.android.gms.common.util.ArrayUtils.removeAll(r2, r4);	 Catch:{ all -> 0x01d5 }
        r2 = (com.google.android.gms.internal.measurement.zzgg[]) r2;	 Catch:{ all -> 0x01d5 }
        r12.zzawt = r2;	 Catch:{ all -> 0x01d5 }
        r5 = r18;
    L_0x05a5:
        r0 = r34;
        r2 = r0.zzadj;	 Catch:{ all -> 0x01d5 }
        r2 = r2.zzgq();	 Catch:{ all -> 0x01d5 }
        r0 = r22;
        r3 = r0.zzaua;	 Catch:{ all -> 0x01d5 }
        r3 = r3.zztt;	 Catch:{ all -> 0x01d5 }
        r2 = r2.zzbf(r3);	 Catch:{ all -> 0x01d5 }
        if (r2 == 0) goto L_0x064b;
    L_0x05b9:
        if (r19 == 0) goto L_0x064b;
    L_0x05bb:
        r6 = r12.zzawt;	 Catch:{ all -> 0x01d5 }
        r4 = -1;
        r2 = -1;
        r3 = 0;
    L_0x05c0:
        r7 = r6.length;	 Catch:{ all -> 0x01d5 }
        if (r3 >= r7) goto L_0x0614;
    L_0x05c3:
        r7 = "value";
        r8 = r6[r3];	 Catch:{ all -> 0x01d5 }
        r8 = r8.name;	 Catch:{ all -> 0x01d5 }
        r7 = r7.equals(r8);	 Catch:{ all -> 0x01d5 }
        if (r7 == 0) goto L_0x0605;
    L_0x05d0:
        r4 = r3;
    L_0x05d1:
        r3 = r3 + 1;
        goto L_0x05c0;
    L_0x05d4:
        if (r3 == 0) goto L_0x05e6;
    L_0x05d6:
        r2 = "_err";
        r3.name = r2;	 Catch:{ all -> 0x01d5 }
        r4 = 10;
        r2 = java.lang.Long.valueOf(r4);	 Catch:{ all -> 0x01d5 }
        r3.zzawx = r2;	 Catch:{ all -> 0x01d5 }
        r5 = r18;
        goto L_0x05a5;
    L_0x05e6:
        r0 = r34;
        r2 = r0.zzadj;	 Catch:{ all -> 0x01d5 }
        r2 = r2.zzgo();	 Catch:{ all -> 0x01d5 }
        r2 = r2.zzjd();	 Catch:{ all -> 0x01d5 }
        r3 = "Did not find conversion parameter. appId";
        r0 = r22;
        r4 = r0.zzaua;	 Catch:{ all -> 0x01d5 }
        r4 = r4.zztt;	 Catch:{ all -> 0x01d5 }
        r4 = com.google.android.gms.measurement.internal.zzap.zzbv(r4);	 Catch:{ all -> 0x01d5 }
        r2.zzg(r3, r4);	 Catch:{ all -> 0x01d5 }
    L_0x0602:
        r5 = r18;
        goto L_0x05a5;
    L_0x0605:
        r7 = "currency";
        r8 = r6[r3];	 Catch:{ all -> 0x01d5 }
        r8 = r8.name;	 Catch:{ all -> 0x01d5 }
        r7 = r7.equals(r8);	 Catch:{ all -> 0x01d5 }
        if (r7 == 0) goto L_0x05d1;
    L_0x0612:
        r2 = r3;
        goto L_0x05d1;
    L_0x0614:
        r3 = -1;
        if (r4 == r3) goto L_0x0de7;
    L_0x0617:
        r3 = r6[r4];	 Catch:{ all -> 0x01d5 }
        r3 = r3.zzawx;	 Catch:{ all -> 0x01d5 }
        if (r3 != 0) goto L_0x0688;
    L_0x061d:
        r3 = r6[r4];	 Catch:{ all -> 0x01d5 }
        r3 = r3.zzauh;	 Catch:{ all -> 0x01d5 }
        if (r3 != 0) goto L_0x0688;
    L_0x0623:
        r0 = r34;
        r2 = r0.zzadj;	 Catch:{ all -> 0x01d5 }
        r2 = r2.zzgo();	 Catch:{ all -> 0x01d5 }
        r2 = r2.zzji();	 Catch:{ all -> 0x01d5 }
        r3 = "Value must be specified with a numeric type.";
        r2.zzbx(r3);	 Catch:{ all -> 0x01d5 }
        r2 = zza(r6, r4);	 Catch:{ all -> 0x01d5 }
        r3 = "_c";
        r2 = zza(r2, r3);	 Catch:{ all -> 0x01d5 }
        r3 = 18;
        r4 = "value";
        r2 = zza(r2, r3, r4);	 Catch:{ all -> 0x01d5 }
    L_0x0649:
        r12.zzawt = r2;	 Catch:{ all -> 0x01d5 }
    L_0x064b:
        if (r17 == 0) goto L_0x0dfa;
    L_0x064d:
        r2 = "_e";
        r3 = r12.name;	 Catch:{ all -> 0x01d5 }
        r2 = r2.equals(r3);	 Catch:{ all -> 0x01d5 }
        if (r2 == 0) goto L_0x0dfa;
    L_0x0658:
        r2 = r12.zzawt;	 Catch:{ all -> 0x01d5 }
        if (r2 == 0) goto L_0x0661;
    L_0x065c:
        r2 = r12.zzawt;	 Catch:{ all -> 0x01d5 }
        r2 = r2.length;	 Catch:{ all -> 0x01d5 }
        if (r2 != 0) goto L_0x06de;
    L_0x0661:
        r0 = r34;
        r2 = r0.zzadj;	 Catch:{ all -> 0x01d5 }
        r2 = r2.zzgo();	 Catch:{ all -> 0x01d5 }
        r2 = r2.zzjg();	 Catch:{ all -> 0x01d5 }
        r3 = "Engagement event does not contain any parameters. appId";
        r0 = r22;
        r4 = r0.zzaua;	 Catch:{ all -> 0x01d5 }
        r4 = r4.zztt;	 Catch:{ all -> 0x01d5 }
        r4 = com.google.android.gms.measurement.internal.zzap.zzbv(r4);	 Catch:{ all -> 0x01d5 }
        r2.zzg(r3, r4);	 Catch:{ all -> 0x01d5 }
        r2 = r14;
    L_0x067e:
        r0 = r23;
        r6 = r0.zzaxb;	 Catch:{ all -> 0x01d5 }
        r4 = r13 + 1;
        r6[r13] = r12;	 Catch:{ all -> 0x01d5 }
        goto L_0x0166;
    L_0x0688:
        r3 = 0;
        r7 = -1;
        if (r2 != r7) goto L_0x06b6;
    L_0x068c:
        r2 = 1;
    L_0x068d:
        if (r2 == 0) goto L_0x0de7;
    L_0x068f:
        r0 = r34;
        r2 = r0.zzadj;	 Catch:{ all -> 0x01d5 }
        r2 = r2.zzgo();	 Catch:{ all -> 0x01d5 }
        r2 = r2.zzji();	 Catch:{ all -> 0x01d5 }
        r3 = "Value parameter discarded. You must also supply a 3-letter ISO_4217 currency code in the currency parameter.";
        r2.zzbx(r3);	 Catch:{ all -> 0x01d5 }
        r2 = zza(r6, r4);	 Catch:{ all -> 0x01d5 }
        r3 = "_c";
        r2 = zza(r2, r3);	 Catch:{ all -> 0x01d5 }
        r3 = 19;
        r4 = "currency";
        r2 = zza(r2, r3, r4);	 Catch:{ all -> 0x01d5 }
        goto L_0x0649;
    L_0x06b6:
        r2 = r6[r2];	 Catch:{ all -> 0x01d5 }
        r7 = r2.zzamp;	 Catch:{ all -> 0x01d5 }
        if (r7 == 0) goto L_0x06c3;
    L_0x06bc:
        r2 = r7.length();	 Catch:{ all -> 0x01d5 }
        r8 = 3;
        if (r2 == r8) goto L_0x06c5;
    L_0x06c3:
        r2 = 1;
        goto L_0x068d;
    L_0x06c5:
        r2 = 0;
    L_0x06c6:
        r8 = r7.length();	 Catch:{ all -> 0x01d5 }
        if (r2 >= r8) goto L_0x0dfd;
    L_0x06cc:
        r8 = r7.codePointAt(r2);	 Catch:{ all -> 0x01d5 }
        r9 = java.lang.Character.isLetter(r8);	 Catch:{ all -> 0x01d5 }
        if (r9 != 0) goto L_0x06d8;
    L_0x06d6:
        r2 = 1;
        goto L_0x068d;
    L_0x06d8:
        r8 = java.lang.Character.charCount(r8);	 Catch:{ all -> 0x01d5 }
        r2 = r2 + r8;
        goto L_0x06c6;
    L_0x06de:
        r34.zzjo();	 Catch:{ all -> 0x01d5 }
        r2 = "_et";
        r2 = com.google.android.gms.measurement.internal.zzfg.zzb(r12, r2);	 Catch:{ all -> 0x01d5 }
        r2 = (java.lang.Long) r2;	 Catch:{ all -> 0x01d5 }
        if (r2 != 0) goto L_0x070b;
    L_0x06ec:
        r0 = r34;
        r2 = r0.zzadj;	 Catch:{ all -> 0x01d5 }
        r2 = r2.zzgo();	 Catch:{ all -> 0x01d5 }
        r2 = r2.zzjg();	 Catch:{ all -> 0x01d5 }
        r3 = "Engagement event does not include duration. appId";
        r0 = r22;
        r4 = r0.zzaua;	 Catch:{ all -> 0x01d5 }
        r4 = r4.zztt;	 Catch:{ all -> 0x01d5 }
        r4 = com.google.android.gms.measurement.internal.zzap.zzbv(r4);	 Catch:{ all -> 0x01d5 }
        r2.zzg(r3, r4);	 Catch:{ all -> 0x01d5 }
        r2 = r14;
        goto L_0x067e;
    L_0x070b:
        r2 = r2.longValue();	 Catch:{ all -> 0x01d5 }
        r14 = r14 + r2;
        r2 = r14;
        goto L_0x067e;
    L_0x0713:
        r0 = r22;
        r2 = r0.zzauc;	 Catch:{ all -> 0x01d5 }
        r2 = r2.size();	 Catch:{ all -> 0x01d5 }
        if (r13 >= r2) goto L_0x072b;
    L_0x071d:
        r0 = r23;
        r2 = r0.zzaxb;	 Catch:{ all -> 0x01d5 }
        r2 = java.util.Arrays.copyOf(r2, r13);	 Catch:{ all -> 0x01d5 }
        r2 = (com.google.android.gms.internal.measurement.zzgf[]) r2;	 Catch:{ all -> 0x01d5 }
        r0 = r23;
        r0.zzaxb = r2;	 Catch:{ all -> 0x01d5 }
    L_0x072b:
        if (r17 == 0) goto L_0x07ec;
    L_0x072d:
        r2 = r34.zzjq();	 Catch:{ all -> 0x01d5 }
        r0 = r23;
        r3 = r0.zztt;	 Catch:{ all -> 0x01d5 }
        r4 = "_lte";
        r8 = r2.zzi(r3, r4);	 Catch:{ all -> 0x01d5 }
        if (r8 == 0) goto L_0x0742;
    L_0x073e:
        r2 = r8.value;	 Catch:{ all -> 0x01d5 }
        if (r2 != 0) goto L_0x08d4;
    L_0x0742:
        r2 = new com.google.android.gms.measurement.internal.zzfj;	 Catch:{ all -> 0x01d5 }
        r0 = r23;
        r3 = r0.zztt;	 Catch:{ all -> 0x01d5 }
        r4 = "auto";
        r5 = "_lte";
        r0 = r34;
        r6 = r0.zzadj;	 Catch:{ all -> 0x01d5 }
        r6 = r6.zzbx();	 Catch:{ all -> 0x01d5 }
        r6 = r6.currentTimeMillis();	 Catch:{ all -> 0x01d5 }
        r8 = java.lang.Long.valueOf(r14);	 Catch:{ all -> 0x01d5 }
        r2.<init>(r3, r4, r5, r6, r8);	 Catch:{ all -> 0x01d5 }
        r4 = r2;
    L_0x0762:
        r5 = new com.google.android.gms.internal.measurement.zzgl;	 Catch:{ all -> 0x01d5 }
        r5.<init>();	 Catch:{ all -> 0x01d5 }
        r2 = "_lte";
        r5.name = r2;	 Catch:{ all -> 0x01d5 }
        r0 = r34;
        r2 = r0.zzadj;	 Catch:{ all -> 0x01d5 }
        r2 = r2.zzbx();	 Catch:{ all -> 0x01d5 }
        r2 = r2.currentTimeMillis();	 Catch:{ all -> 0x01d5 }
        r2 = java.lang.Long.valueOf(r2);	 Catch:{ all -> 0x01d5 }
        r5.zzayl = r2;	 Catch:{ all -> 0x01d5 }
        r2 = r4.value;	 Catch:{ all -> 0x01d5 }
        r2 = (java.lang.Long) r2;	 Catch:{ all -> 0x01d5 }
        r5.zzawx = r2;	 Catch:{ all -> 0x01d5 }
        r2 = 0;
        r3 = 0;
    L_0x0786:
        r0 = r23;
        r6 = r0.zzaxc;	 Catch:{ all -> 0x01d5 }
        r6 = r6.length;	 Catch:{ all -> 0x01d5 }
        if (r3 >= r6) goto L_0x07a5;
    L_0x078d:
        r6 = "_lte";
        r0 = r23;
        r7 = r0.zzaxc;	 Catch:{ all -> 0x01d5 }
        r7 = r7[r3];	 Catch:{ all -> 0x01d5 }
        r7 = r7.name;	 Catch:{ all -> 0x01d5 }
        r6 = r6.equals(r7);	 Catch:{ all -> 0x01d5 }
        if (r6 == 0) goto L_0x08ff;
    L_0x079e:
        r0 = r23;
        r2 = r0.zzaxc;	 Catch:{ all -> 0x01d5 }
        r2[r3] = r5;	 Catch:{ all -> 0x01d5 }
        r2 = 1;
    L_0x07a5:
        if (r2 != 0) goto L_0x07cb;
    L_0x07a7:
        r0 = r23;
        r2 = r0.zzaxc;	 Catch:{ all -> 0x01d5 }
        r0 = r23;
        r3 = r0.zzaxc;	 Catch:{ all -> 0x01d5 }
        r3 = r3.length;	 Catch:{ all -> 0x01d5 }
        r3 = r3 + 1;
        r2 = java.util.Arrays.copyOf(r2, r3);	 Catch:{ all -> 0x01d5 }
        r2 = (com.google.android.gms.internal.measurement.zzgl[]) r2;	 Catch:{ all -> 0x01d5 }
        r0 = r23;
        r0.zzaxc = r2;	 Catch:{ all -> 0x01d5 }
        r0 = r23;
        r2 = r0.zzaxc;	 Catch:{ all -> 0x01d5 }
        r0 = r22;
        r3 = r0.zzaua;	 Catch:{ all -> 0x01d5 }
        r3 = r3.zzaxc;	 Catch:{ all -> 0x01d5 }
        r3 = r3.length;	 Catch:{ all -> 0x01d5 }
        r3 = r3 + -1;
        r2[r3] = r5;	 Catch:{ all -> 0x01d5 }
    L_0x07cb:
        r2 = 0;
        r2 = (r14 > r2 ? 1 : (r14 == r2 ? 0 : -1));
        if (r2 <= 0) goto L_0x07ec;
    L_0x07d1:
        r2 = r34.zzjq();	 Catch:{ all -> 0x01d5 }
        r2.zza(r4);	 Catch:{ all -> 0x01d5 }
        r0 = r34;
        r2 = r0.zzadj;	 Catch:{ all -> 0x01d5 }
        r2 = r2.zzgo();	 Catch:{ all -> 0x01d5 }
        r2 = r2.zzjk();	 Catch:{ all -> 0x01d5 }
        r3 = "Updated lifetime engagement user property with value. Value";
        r4 = r4.value;	 Catch:{ all -> 0x01d5 }
        r2.zzg(r3, r4);	 Catch:{ all -> 0x01d5 }
    L_0x07ec:
        r0 = r23;
        r2 = r0.zztt;	 Catch:{ all -> 0x01d5 }
        r0 = r23;
        r3 = r0.zzaxc;	 Catch:{ all -> 0x01d5 }
        r0 = r23;
        r4 = r0.zzaxb;	 Catch:{ all -> 0x01d5 }
        r0 = r34;
        r2 = r0.zza(r2, r3, r4);	 Catch:{ all -> 0x01d5 }
        r0 = r23;
        r0.zzaxt = r2;	 Catch:{ all -> 0x01d5 }
        r0 = r34;
        r2 = r0.zzadj;	 Catch:{ all -> 0x01d5 }
        r2 = r2.zzgq();	 Catch:{ all -> 0x01d5 }
        r0 = r22;
        r3 = r0.zzaua;	 Catch:{ all -> 0x01d5 }
        r3 = r3.zztt;	 Catch:{ all -> 0x01d5 }
        r2 = r2.zzaw(r3);	 Catch:{ all -> 0x01d5 }
        if (r2 == 0) goto L_0x0bbb;
    L_0x0816:
        r24 = new java.util.HashMap;	 Catch:{ all -> 0x01d5 }
        r24.<init>();	 Catch:{ all -> 0x01d5 }
        r0 = r23;
        r2 = r0.zzaxb;	 Catch:{ all -> 0x01d5 }
        r2 = r2.length;	 Catch:{ all -> 0x01d5 }
        r0 = new com.google.android.gms.internal.measurement.zzgf[r2];	 Catch:{ all -> 0x01d5 }
        r25 = r0;
        r19 = 0;
        r0 = r34;
        r2 = r0.zzadj;	 Catch:{ all -> 0x01d5 }
        r2 = r2.zzgm();	 Catch:{ all -> 0x01d5 }
        r26 = r2.zzmd();	 Catch:{ all -> 0x01d5 }
        r0 = r23;
        r0 = r0.zzaxb;	 Catch:{ all -> 0x01d5 }
        r27 = r0;
        r0 = r27;
        r0 = r0.length;	 Catch:{ all -> 0x01d5 }
        r28 = r0;
        r2 = 0;
        r21 = r2;
    L_0x0840:
        r0 = r21;
        r1 = r28;
        if (r0 >= r1) goto L_0x0b82;
    L_0x0846:
        r29 = r27[r21];	 Catch:{ all -> 0x01d5 }
        r0 = r29;
        r2 = r0.name;	 Catch:{ all -> 0x01d5 }
        r3 = "_ep";
        r2 = r2.equals(r3);	 Catch:{ all -> 0x01d5 }
        if (r2 == 0) goto L_0x0903;
    L_0x0855:
        r34.zzjo();	 Catch:{ all -> 0x01d5 }
        r2 = "_en";
        r0 = r29;
        r2 = com.google.android.gms.measurement.internal.zzfg.zzb(r0, r2);	 Catch:{ all -> 0x01d5 }
        r2 = (java.lang.String) r2;	 Catch:{ all -> 0x01d5 }
        r0 = r24;
        r3 = r0.get(r2);	 Catch:{ all -> 0x01d5 }
        r3 = (com.google.android.gms.measurement.internal.zzz) r3;	 Catch:{ all -> 0x01d5 }
        if (r3 != 0) goto L_0x0880;
    L_0x086d:
        r3 = r34.zzjq();	 Catch:{ all -> 0x01d5 }
        r0 = r22;
        r4 = r0.zzaua;	 Catch:{ all -> 0x01d5 }
        r4 = r4.zztt;	 Catch:{ all -> 0x01d5 }
        r3 = r3.zzg(r4, r2);	 Catch:{ all -> 0x01d5 }
        r0 = r24;
        r0.put(r2, r3);	 Catch:{ all -> 0x01d5 }
    L_0x0880:
        r2 = r3.zzaij;	 Catch:{ all -> 0x01d5 }
        if (r2 != 0) goto L_0x0b7e;
    L_0x0884:
        r2 = r3.zzaik;	 Catch:{ all -> 0x01d5 }
        r4 = r2.longValue();	 Catch:{ all -> 0x01d5 }
        r6 = 1;
        r2 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r2 <= 0) goto L_0x08a4;
    L_0x0890:
        r34.zzjo();	 Catch:{ all -> 0x01d5 }
        r0 = r29;
        r2 = r0.zzawt;	 Catch:{ all -> 0x01d5 }
        r4 = "_sr";
        r5 = r3.zzaik;	 Catch:{ all -> 0x01d5 }
        r2 = com.google.android.gms.measurement.internal.zzfg.zza(r2, r4, r5);	 Catch:{ all -> 0x01d5 }
        r0 = r29;
        r0.zzawt = r2;	 Catch:{ all -> 0x01d5 }
    L_0x08a4:
        r2 = r3.zzail;	 Catch:{ all -> 0x01d5 }
        if (r2 == 0) goto L_0x08c8;
    L_0x08a8:
        r2 = r3.zzail;	 Catch:{ all -> 0x01d5 }
        r2 = r2.booleanValue();	 Catch:{ all -> 0x01d5 }
        if (r2 == 0) goto L_0x08c8;
    L_0x08b0:
        r34.zzjo();	 Catch:{ all -> 0x01d5 }
        r0 = r29;
        r2 = r0.zzawt;	 Catch:{ all -> 0x01d5 }
        r3 = "_efs";
        r4 = 1;
        r4 = java.lang.Long.valueOf(r4);	 Catch:{ all -> 0x01d5 }
        r2 = com.google.android.gms.measurement.internal.zzfg.zza(r2, r3, r4);	 Catch:{ all -> 0x01d5 }
        r0 = r29;
        r0.zzawt = r2;	 Catch:{ all -> 0x01d5 }
    L_0x08c8:
        r2 = r19 + 1;
        r25[r19] = r29;	 Catch:{ all -> 0x01d5 }
    L_0x08cc:
        r3 = r21 + 1;
        r21 = r3;
        r19 = r2;
        goto L_0x0840;
    L_0x08d4:
        r2 = new com.google.android.gms.measurement.internal.zzfj;	 Catch:{ all -> 0x01d5 }
        r0 = r23;
        r3 = r0.zztt;	 Catch:{ all -> 0x01d5 }
        r4 = "auto";
        r5 = "_lte";
        r0 = r34;
        r6 = r0.zzadj;	 Catch:{ all -> 0x01d5 }
        r6 = r6.zzbx();	 Catch:{ all -> 0x01d5 }
        r6 = r6.currentTimeMillis();	 Catch:{ all -> 0x01d5 }
        r8 = r8.value;	 Catch:{ all -> 0x01d5 }
        r8 = (java.lang.Long) r8;	 Catch:{ all -> 0x01d5 }
        r8 = r8.longValue();	 Catch:{ all -> 0x01d5 }
        r8 = r8 + r14;
        r8 = java.lang.Long.valueOf(r8);	 Catch:{ all -> 0x01d5 }
        r2.<init>(r3, r4, r5, r6, r8);	 Catch:{ all -> 0x01d5 }
        r4 = r2;
        goto L_0x0762;
    L_0x08ff:
        r3 = r3 + 1;
        goto L_0x0786;
    L_0x0903:
        r2 = r34.zzln();	 Catch:{ all -> 0x01d5 }
        r0 = r22;
        r3 = r0.zzaua;	 Catch:{ all -> 0x01d5 }
        r3 = r3.zztt;	 Catch:{ all -> 0x01d5 }
        r30 = r2.zzcj(r3);	 Catch:{ all -> 0x01d5 }
        r0 = r34;
        r2 = r0.zzadj;	 Catch:{ all -> 0x01d5 }
        r2.zzgm();	 Catch:{ all -> 0x01d5 }
        r0 = r29;
        r2 = r0.zzawu;	 Catch:{ all -> 0x01d5 }
        r2 = r2.longValue();	 Catch:{ all -> 0x01d5 }
        r0 = r30;
        r32 = com.google.android.gms.measurement.internal.zzfk.zzc(r2, r0);	 Catch:{ all -> 0x01d5 }
        r2 = 1;
        r4 = "_dbg";
        r6 = 1;
        r5 = java.lang.Long.valueOf(r6);	 Catch:{ all -> 0x01d5 }
        r3 = android.text.TextUtils.isEmpty(r4);	 Catch:{ all -> 0x01d5 }
        if (r3 != 0) goto L_0x0938;
    L_0x0936:
        if (r5 != 0) goto L_0x0971;
    L_0x0938:
        r3 = 0;
    L_0x0939:
        if (r3 != 0) goto L_0x0de3;
    L_0x093b:
        r2 = r34.zzln();	 Catch:{ all -> 0x01d5 }
        r0 = r22;
        r3 = r0.zzaua;	 Catch:{ all -> 0x01d5 }
        r3 = r3.zztt;	 Catch:{ all -> 0x01d5 }
        r0 = r29;
        r4 = r0.name;	 Catch:{ all -> 0x01d5 }
        r2 = r2.zzq(r3, r4);	 Catch:{ all -> 0x01d5 }
        r20 = r2;
    L_0x094f:
        if (r20 > 0) goto L_0x09b0;
    L_0x0951:
        r0 = r34;
        r2 = r0.zzadj;	 Catch:{ all -> 0x01d5 }
        r2 = r2.zzgo();	 Catch:{ all -> 0x01d5 }
        r2 = r2.zzjg();	 Catch:{ all -> 0x01d5 }
        r3 = "Sample rate must be positive. event, rate";
        r0 = r29;
        r4 = r0.name;	 Catch:{ all -> 0x01d5 }
        r5 = java.lang.Integer.valueOf(r20);	 Catch:{ all -> 0x01d5 }
        r2.zze(r3, r4, r5);	 Catch:{ all -> 0x01d5 }
        r2 = r19 + 1;
        r25[r19] = r29;	 Catch:{ all -> 0x01d5 }
        goto L_0x08cc;
    L_0x0971:
        r0 = r29;
        r6 = r0.zzawt;	 Catch:{ all -> 0x01d5 }
        r7 = r6.length;	 Catch:{ all -> 0x01d5 }
        r3 = 0;
    L_0x0977:
        if (r3 >= r7) goto L_0x09ae;
    L_0x0979:
        r8 = r6[r3];	 Catch:{ all -> 0x01d5 }
        r9 = r8.name;	 Catch:{ all -> 0x01d5 }
        r9 = r4.equals(r9);	 Catch:{ all -> 0x01d5 }
        if (r9 == 0) goto L_0x09ab;
    L_0x0983:
        r3 = r5 instanceof java.lang.Long;	 Catch:{ all -> 0x01d5 }
        if (r3 == 0) goto L_0x098f;
    L_0x0987:
        r3 = r8.zzawx;	 Catch:{ all -> 0x01d5 }
        r3 = r5.equals(r3);	 Catch:{ all -> 0x01d5 }
        if (r3 != 0) goto L_0x09a7;
    L_0x098f:
        r3 = r5 instanceof java.lang.String;	 Catch:{ all -> 0x01d5 }
        if (r3 == 0) goto L_0x099b;
    L_0x0993:
        r3 = r8.zzamp;	 Catch:{ all -> 0x01d5 }
        r3 = r5.equals(r3);	 Catch:{ all -> 0x01d5 }
        if (r3 != 0) goto L_0x09a7;
    L_0x099b:
        r3 = r5 instanceof java.lang.Double;	 Catch:{ all -> 0x01d5 }
        if (r3 == 0) goto L_0x09a9;
    L_0x099f:
        r3 = r8.zzauh;	 Catch:{ all -> 0x01d5 }
        r3 = r5.equals(r3);	 Catch:{ all -> 0x01d5 }
        if (r3 == 0) goto L_0x09a9;
    L_0x09a7:
        r3 = 1;
        goto L_0x0939;
    L_0x09a9:
        r3 = 0;
        goto L_0x0939;
    L_0x09ab:
        r3 = r3 + 1;
        goto L_0x0977;
    L_0x09ae:
        r3 = 0;
        goto L_0x0939;
    L_0x09b0:
        r0 = r29;
        r2 = r0.name;	 Catch:{ all -> 0x01d5 }
        r0 = r24;
        r2 = r0.get(r2);	 Catch:{ all -> 0x01d5 }
        r2 = (com.google.android.gms.measurement.internal.zzz) r2;	 Catch:{ all -> 0x01d5 }
        if (r2 != 0) goto L_0x0de0;
    L_0x09be:
        r2 = r34.zzjq();	 Catch:{ all -> 0x01d5 }
        r0 = r22;
        r3 = r0.zzaua;	 Catch:{ all -> 0x01d5 }
        r3 = r3.zztt;	 Catch:{ all -> 0x01d5 }
        r0 = r29;
        r4 = r0.name;	 Catch:{ all -> 0x01d5 }
        r4 = r2.zzg(r3, r4);	 Catch:{ all -> 0x01d5 }
        if (r4 != 0) goto L_0x0a12;
    L_0x09d2:
        r0 = r34;
        r2 = r0.zzadj;	 Catch:{ all -> 0x01d5 }
        r2 = r2.zzgo();	 Catch:{ all -> 0x01d5 }
        r2 = r2.zzjg();	 Catch:{ all -> 0x01d5 }
        r3 = "Event being bundled has no eventAggregate. appId, eventName";
        r0 = r22;
        r4 = r0.zzaua;	 Catch:{ all -> 0x01d5 }
        r4 = r4.zztt;	 Catch:{ all -> 0x01d5 }
        r0 = r29;
        r5 = r0.name;	 Catch:{ all -> 0x01d5 }
        r2.zze(r3, r4, r5);	 Catch:{ all -> 0x01d5 }
        r3 = new com.google.android.gms.measurement.internal.zzz;	 Catch:{ all -> 0x01d5 }
        r0 = r22;
        r2 = r0.zzaua;	 Catch:{ all -> 0x01d5 }
        r4 = r2.zztt;	 Catch:{ all -> 0x01d5 }
        r0 = r29;
        r5 = r0.name;	 Catch:{ all -> 0x01d5 }
        r6 = 1;
        r8 = 1;
        r0 = r29;
        r2 = r0.zzawu;	 Catch:{ all -> 0x01d5 }
        r10 = r2.longValue();	 Catch:{ all -> 0x01d5 }
        r12 = 0;
        r14 = 0;
        r15 = 0;
        r16 = 0;
        r17 = 0;
        r3.<init>(r4, r5, r6, r8, r10, r12, r14, r15, r16, r17);	 Catch:{ all -> 0x01d5 }
        r4 = r3;
    L_0x0a12:
        r34.zzjo();	 Catch:{ all -> 0x01d5 }
        r2 = "_eid";
        r0 = r29;
        r2 = com.google.android.gms.measurement.internal.zzfg.zzb(r0, r2);	 Catch:{ all -> 0x01d5 }
        r2 = (java.lang.Long) r2;	 Catch:{ all -> 0x01d5 }
        if (r2 == 0) goto L_0x0a54;
    L_0x0a22:
        r3 = 1;
    L_0x0a23:
        r5 = java.lang.Boolean.valueOf(r3);	 Catch:{ all -> 0x01d5 }
        r3 = 1;
        r0 = r20;
        if (r0 != r3) goto L_0x0a56;
    L_0x0a2c:
        r2 = r19 + 1;
        r25[r19] = r29;	 Catch:{ all -> 0x01d5 }
        r3 = r5.booleanValue();	 Catch:{ all -> 0x01d5 }
        if (r3 == 0) goto L_0x08cc;
    L_0x0a36:
        r3 = r4.zzaij;	 Catch:{ all -> 0x01d5 }
        if (r3 != 0) goto L_0x0a42;
    L_0x0a3a:
        r3 = r4.zzaik;	 Catch:{ all -> 0x01d5 }
        if (r3 != 0) goto L_0x0a42;
    L_0x0a3e:
        r3 = r4.zzail;	 Catch:{ all -> 0x01d5 }
        if (r3 == 0) goto L_0x08cc;
    L_0x0a42:
        r3 = 0;
        r5 = 0;
        r6 = 0;
        r3 = r4.zza(r3, r5, r6);	 Catch:{ all -> 0x01d5 }
        r0 = r29;
        r4 = r0.name;	 Catch:{ all -> 0x01d5 }
        r0 = r24;
        r0.put(r4, r3);	 Catch:{ all -> 0x01d5 }
        goto L_0x08cc;
    L_0x0a54:
        r3 = 0;
        goto L_0x0a23;
    L_0x0a56:
        r0 = r26;
        r1 = r20;
        r3 = r0.nextInt(r1);	 Catch:{ all -> 0x01d5 }
        if (r3 != 0) goto L_0x0aa9;
    L_0x0a60:
        r34.zzjo();	 Catch:{ all -> 0x01d5 }
        r0 = r29;
        r2 = r0.zzawt;	 Catch:{ all -> 0x01d5 }
        r3 = "_sr";
        r0 = r20;
        r6 = (long) r0;	 Catch:{ all -> 0x01d5 }
        r6 = java.lang.Long.valueOf(r6);	 Catch:{ all -> 0x01d5 }
        r2 = com.google.android.gms.measurement.internal.zzfg.zza(r2, r3, r6);	 Catch:{ all -> 0x01d5 }
        r0 = r29;
        r0.zzawt = r2;	 Catch:{ all -> 0x01d5 }
        r2 = r19 + 1;
        r25[r19] = r29;	 Catch:{ all -> 0x01d5 }
        r3 = r5.booleanValue();	 Catch:{ all -> 0x01d5 }
        if (r3 == 0) goto L_0x0a90;
    L_0x0a83:
        r3 = 0;
        r0 = r20;
        r6 = (long) r0;	 Catch:{ all -> 0x01d5 }
        r5 = java.lang.Long.valueOf(r6);	 Catch:{ all -> 0x01d5 }
        r6 = 0;
        r4 = r4.zza(r3, r5, r6);	 Catch:{ all -> 0x01d5 }
    L_0x0a90:
        r0 = r29;
        r3 = r0.name;	 Catch:{ all -> 0x01d5 }
        r0 = r29;
        r5 = r0.zzawu;	 Catch:{ all -> 0x01d5 }
        r6 = r5.longValue();	 Catch:{ all -> 0x01d5 }
        r0 = r32;
        r4 = r4.zza(r6, r0);	 Catch:{ all -> 0x01d5 }
        r0 = r24;
        r0.put(r3, r4);	 Catch:{ all -> 0x01d5 }
        goto L_0x08cc;
    L_0x0aa9:
        r0 = r34;
        r3 = r0.zzadj;	 Catch:{ all -> 0x01d5 }
        r3 = r3.zzgq();	 Catch:{ all -> 0x01d5 }
        r0 = r22;
        r6 = r0.zzaua;	 Catch:{ all -> 0x01d5 }
        r6 = r6.zztt;	 Catch:{ all -> 0x01d5 }
        r3 = r3.zzbh(r6);	 Catch:{ all -> 0x01d5 }
        if (r3 == 0) goto L_0x0b4c;
    L_0x0abd:
        r3 = r4.zzaii;	 Catch:{ all -> 0x01d5 }
        if (r3 == 0) goto L_0x0b33;
    L_0x0ac1:
        r3 = r4.zzaii;	 Catch:{ all -> 0x01d5 }
        r6 = r3.longValue();	 Catch:{ all -> 0x01d5 }
    L_0x0ac7:
        r3 = (r6 > r32 ? 1 : (r6 == r32 ? 0 : -1));
        if (r3 == 0) goto L_0x0b4a;
    L_0x0acb:
        r3 = 1;
    L_0x0acc:
        if (r3 == 0) goto L_0x0b69;
    L_0x0ace:
        r34.zzjo();	 Catch:{ all -> 0x01d5 }
        r0 = r29;
        r2 = r0.zzawt;	 Catch:{ all -> 0x01d5 }
        r3 = "_efs";
        r6 = 1;
        r6 = java.lang.Long.valueOf(r6);	 Catch:{ all -> 0x01d5 }
        r2 = com.google.android.gms.measurement.internal.zzfg.zza(r2, r3, r6);	 Catch:{ all -> 0x01d5 }
        r0 = r29;
        r0.zzawt = r2;	 Catch:{ all -> 0x01d5 }
        r34.zzjo();	 Catch:{ all -> 0x01d5 }
        r0 = r29;
        r2 = r0.zzawt;	 Catch:{ all -> 0x01d5 }
        r3 = "_sr";
        r0 = r20;
        r6 = (long) r0;	 Catch:{ all -> 0x01d5 }
        r6 = java.lang.Long.valueOf(r6);	 Catch:{ all -> 0x01d5 }
        r2 = com.google.android.gms.measurement.internal.zzfg.zza(r2, r3, r6);	 Catch:{ all -> 0x01d5 }
        r0 = r29;
        r0.zzawt = r2;	 Catch:{ all -> 0x01d5 }
        r2 = r19 + 1;
        r25[r19] = r29;	 Catch:{ all -> 0x01d5 }
        r3 = r5.booleanValue();	 Catch:{ all -> 0x01d5 }
        if (r3 == 0) goto L_0x0ddd;
    L_0x0b09:
        r3 = 0;
        r0 = r20;
        r6 = (long) r0;	 Catch:{ all -> 0x01d5 }
        r5 = java.lang.Long.valueOf(r6);	 Catch:{ all -> 0x01d5 }
        r6 = 1;
        r6 = java.lang.Boolean.valueOf(r6);	 Catch:{ all -> 0x01d5 }
        r3 = r4.zza(r3, r5, r6);	 Catch:{ all -> 0x01d5 }
    L_0x0b1a:
        r0 = r29;
        r4 = r0.name;	 Catch:{ all -> 0x01d5 }
        r0 = r29;
        r5 = r0.zzawu;	 Catch:{ all -> 0x01d5 }
        r6 = r5.longValue();	 Catch:{ all -> 0x01d5 }
        r0 = r32;
        r3 = r3.zza(r6, r0);	 Catch:{ all -> 0x01d5 }
        r0 = r24;
        r0.put(r4, r3);	 Catch:{ all -> 0x01d5 }
        goto L_0x08cc;
    L_0x0b33:
        r0 = r34;
        r3 = r0.zzadj;	 Catch:{ all -> 0x01d5 }
        r3.zzgm();	 Catch:{ all -> 0x01d5 }
        r0 = r29;
        r3 = r0.zzawv;	 Catch:{ all -> 0x01d5 }
        r6 = r3.longValue();	 Catch:{ all -> 0x01d5 }
        r0 = r30;
        r6 = com.google.android.gms.measurement.internal.zzfk.zzc(r6, r0);	 Catch:{ all -> 0x01d5 }
        goto L_0x0ac7;
    L_0x0b4a:
        r3 = 0;
        goto L_0x0acc;
    L_0x0b4c:
        r6 = r4.zzaih;	 Catch:{ all -> 0x01d5 }
        r0 = r29;
        r3 = r0.zzawu;	 Catch:{ all -> 0x01d5 }
        r8 = r3.longValue();	 Catch:{ all -> 0x01d5 }
        r6 = r8 - r6;
        r6 = java.lang.Math.abs(r6);	 Catch:{ all -> 0x01d5 }
        r8 = 86400000; // 0x5265c00 float:7.82218E-36 double:4.2687272E-316;
        r3 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1));
        if (r3 < 0) goto L_0x0b66;
    L_0x0b63:
        r3 = 1;
        goto L_0x0acc;
    L_0x0b66:
        r3 = 0;
        goto L_0x0acc;
    L_0x0b69:
        r3 = r5.booleanValue();	 Catch:{ all -> 0x01d5 }
        if (r3 == 0) goto L_0x0b7e;
    L_0x0b6f:
        r0 = r29;
        r3 = r0.name;	 Catch:{ all -> 0x01d5 }
        r5 = 0;
        r6 = 0;
        r2 = r4.zza(r2, r5, r6);	 Catch:{ all -> 0x01d5 }
        r0 = r24;
        r0.put(r3, r2);	 Catch:{ all -> 0x01d5 }
    L_0x0b7e:
        r2 = r19;
        goto L_0x08cc;
    L_0x0b82:
        r0 = r23;
        r2 = r0.zzaxb;	 Catch:{ all -> 0x01d5 }
        r2 = r2.length;	 Catch:{ all -> 0x01d5 }
        r0 = r19;
        if (r0 >= r2) goto L_0x0b99;
    L_0x0b8b:
        r0 = r25;
        r1 = r19;
        r2 = java.util.Arrays.copyOf(r0, r1);	 Catch:{ all -> 0x01d5 }
        r2 = (com.google.android.gms.internal.measurement.zzgf[]) r2;	 Catch:{ all -> 0x01d5 }
        r0 = r23;
        r0.zzaxb = r2;	 Catch:{ all -> 0x01d5 }
    L_0x0b99:
        r2 = r24.entrySet();	 Catch:{ all -> 0x01d5 }
        r3 = r2.iterator();	 Catch:{ all -> 0x01d5 }
    L_0x0ba1:
        r2 = r3.hasNext();	 Catch:{ all -> 0x01d5 }
        if (r2 == 0) goto L_0x0bbb;
    L_0x0ba7:
        r2 = r3.next();	 Catch:{ all -> 0x01d5 }
        r2 = (java.util.Map.Entry) r2;	 Catch:{ all -> 0x01d5 }
        r4 = r34.zzjq();	 Catch:{ all -> 0x01d5 }
        r2 = r2.getValue();	 Catch:{ all -> 0x01d5 }
        r2 = (com.google.android.gms.measurement.internal.zzz) r2;	 Catch:{ all -> 0x01d5 }
        r4.zza(r2);	 Catch:{ all -> 0x01d5 }
        goto L_0x0ba1;
    L_0x0bbb:
        r2 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
        r2 = java.lang.Long.valueOf(r2);	 Catch:{ all -> 0x01d5 }
        r0 = r23;
        r0.zzaxe = r2;	 Catch:{ all -> 0x01d5 }
        r2 = -9223372036854775808;
        r2 = java.lang.Long.valueOf(r2);	 Catch:{ all -> 0x01d5 }
        r0 = r23;
        r0.zzaxf = r2;	 Catch:{ all -> 0x01d5 }
        r2 = 0;
    L_0x0bd3:
        r0 = r23;
        r3 = r0.zzaxb;	 Catch:{ all -> 0x01d5 }
        r3 = r3.length;	 Catch:{ all -> 0x01d5 }
        if (r2 >= r3) goto L_0x0c13;
    L_0x0bda:
        r0 = r23;
        r3 = r0.zzaxb;	 Catch:{ all -> 0x01d5 }
        r3 = r3[r2];	 Catch:{ all -> 0x01d5 }
        r4 = r3.zzawu;	 Catch:{ all -> 0x01d5 }
        r4 = r4.longValue();	 Catch:{ all -> 0x01d5 }
        r0 = r23;
        r6 = r0.zzaxe;	 Catch:{ all -> 0x01d5 }
        r6 = r6.longValue();	 Catch:{ all -> 0x01d5 }
        r4 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r4 >= 0) goto L_0x0bf8;
    L_0x0bf2:
        r4 = r3.zzawu;	 Catch:{ all -> 0x01d5 }
        r0 = r23;
        r0.zzaxe = r4;	 Catch:{ all -> 0x01d5 }
    L_0x0bf8:
        r4 = r3.zzawu;	 Catch:{ all -> 0x01d5 }
        r4 = r4.longValue();	 Catch:{ all -> 0x01d5 }
        r0 = r23;
        r6 = r0.zzaxf;	 Catch:{ all -> 0x01d5 }
        r6 = r6.longValue();	 Catch:{ all -> 0x01d5 }
        r4 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r4 <= 0) goto L_0x0c10;
    L_0x0c0a:
        r3 = r3.zzawu;	 Catch:{ all -> 0x01d5 }
        r0 = r23;
        r0.zzaxf = r3;	 Catch:{ all -> 0x01d5 }
    L_0x0c10:
        r2 = r2 + 1;
        goto L_0x0bd3;
    L_0x0c13:
        r0 = r22;
        r2 = r0.zzaua;	 Catch:{ all -> 0x01d5 }
        r6 = r2.zztt;	 Catch:{ all -> 0x01d5 }
        r2 = r34.zzjq();	 Catch:{ all -> 0x01d5 }
        r7 = r2.zzbl(r6);	 Catch:{ all -> 0x01d5 }
        if (r7 != 0) goto L_0x0cbc;
    L_0x0c23:
        r0 = r34;
        r2 = r0.zzadj;	 Catch:{ all -> 0x01d5 }
        r2 = r2.zzgo();	 Catch:{ all -> 0x01d5 }
        r2 = r2.zzjd();	 Catch:{ all -> 0x01d5 }
        r3 = "Bundling raw events w/o app info. appId";
        r0 = r22;
        r4 = r0.zzaua;	 Catch:{ all -> 0x01d5 }
        r4 = r4.zztt;	 Catch:{ all -> 0x01d5 }
        r4 = com.google.android.gms.measurement.internal.zzap.zzbv(r4);	 Catch:{ all -> 0x01d5 }
        r2.zzg(r3, r4);	 Catch:{ all -> 0x01d5 }
    L_0x0c3f:
        r0 = r23;
        r2 = r0.zzaxb;	 Catch:{ all -> 0x01d5 }
        r2 = r2.length;	 Catch:{ all -> 0x01d5 }
        if (r2 <= 0) goto L_0x0c82;
    L_0x0c46:
        r0 = r34;
        r2 = r0.zzadj;	 Catch:{ all -> 0x01d5 }
        r2.zzgr();	 Catch:{ all -> 0x01d5 }
        r2 = r34.zzln();	 Catch:{ all -> 0x01d5 }
        r0 = r22;
        r3 = r0.zzaua;	 Catch:{ all -> 0x01d5 }
        r3 = r3.zztt;	 Catch:{ all -> 0x01d5 }
        r2 = r2.zzcf(r3);	 Catch:{ all -> 0x01d5 }
        if (r2 == 0) goto L_0x0c61;
    L_0x0c5d:
        r3 = r2.zzawe;	 Catch:{ all -> 0x01d5 }
        if (r3 != 0) goto L_0x0d46;
    L_0x0c61:
        r0 = r22;
        r2 = r0.zzaua;	 Catch:{ all -> 0x01d5 }
        r2 = r2.zzafx;	 Catch:{ all -> 0x01d5 }
        r2 = android.text.TextUtils.isEmpty(r2);	 Catch:{ all -> 0x01d5 }
        if (r2 == 0) goto L_0x0d28;
    L_0x0c6d:
        r2 = -1;
        r2 = java.lang.Long.valueOf(r2);	 Catch:{ all -> 0x01d5 }
        r0 = r23;
        r0.zzaxy = r2;	 Catch:{ all -> 0x01d5 }
    L_0x0c77:
        r2 = r34.zzjq();	 Catch:{ all -> 0x01d5 }
        r0 = r23;
        r1 = r18;
        r2.zza(r0, r1);	 Catch:{ all -> 0x01d5 }
    L_0x0c82:
        r4 = r34.zzjq();	 Catch:{ all -> 0x01d5 }
        r0 = r22;
        r5 = r0.zzaub;	 Catch:{ all -> 0x01d5 }
        com.google.android.gms.common.internal.Preconditions.checkNotNull(r5);	 Catch:{ all -> 0x01d5 }
        r4.zzaf();	 Catch:{ all -> 0x01d5 }
        r4.zzcl();	 Catch:{ all -> 0x01d5 }
        r7 = new java.lang.StringBuilder;	 Catch:{ all -> 0x01d5 }
        r2 = "rowid in (";
        r7.<init>(r2);	 Catch:{ all -> 0x01d5 }
        r2 = 0;
        r3 = r2;
    L_0x0c9d:
        r2 = r5.size();	 Catch:{ all -> 0x01d5 }
        if (r3 >= r2) goto L_0x0d4e;
    L_0x0ca3:
        if (r3 == 0) goto L_0x0cab;
    L_0x0ca5:
        r2 = ",";
        r7.append(r2);	 Catch:{ all -> 0x01d5 }
    L_0x0cab:
        r2 = r5.get(r3);	 Catch:{ all -> 0x01d5 }
        r2 = (java.lang.Long) r2;	 Catch:{ all -> 0x01d5 }
        r8 = r2.longValue();	 Catch:{ all -> 0x01d5 }
        r7.append(r8);	 Catch:{ all -> 0x01d5 }
        r2 = r3 + 1;
        r3 = r2;
        goto L_0x0c9d;
    L_0x0cbc:
        r0 = r23;
        r2 = r0.zzaxb;	 Catch:{ all -> 0x01d5 }
        r2 = r2.length;	 Catch:{ all -> 0x01d5 }
        if (r2 <= 0) goto L_0x0c3f;
    L_0x0cc3:
        r2 = r7.zzgz();	 Catch:{ all -> 0x01d5 }
        r4 = 0;
        r4 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
        if (r4 == 0) goto L_0x0d24;
    L_0x0ccd:
        r4 = java.lang.Long.valueOf(r2);	 Catch:{ all -> 0x01d5 }
    L_0x0cd1:
        r0 = r23;
        r0.zzaxh = r4;	 Catch:{ all -> 0x01d5 }
        r4 = r7.zzgy();	 Catch:{ all -> 0x01d5 }
        r8 = 0;
        r8 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1));
        if (r8 != 0) goto L_0x0dda;
    L_0x0cdf:
        r4 = 0;
        r4 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
        if (r4 == 0) goto L_0x0d26;
    L_0x0ce5:
        r2 = java.lang.Long.valueOf(r2);	 Catch:{ all -> 0x01d5 }
    L_0x0ce9:
        r0 = r23;
        r0.zzaxg = r2;	 Catch:{ all -> 0x01d5 }
        r7.zzhh();	 Catch:{ all -> 0x01d5 }
        r2 = r7.zzhe();	 Catch:{ all -> 0x01d5 }
        r2 = (int) r2;	 Catch:{ all -> 0x01d5 }
        r2 = java.lang.Integer.valueOf(r2);	 Catch:{ all -> 0x01d5 }
        r0 = r23;
        r0.zzaxr = r2;	 Catch:{ all -> 0x01d5 }
        r0 = r23;
        r2 = r0.zzaxe;	 Catch:{ all -> 0x01d5 }
        r2 = r2.longValue();	 Catch:{ all -> 0x01d5 }
        r7.zzs(r2);	 Catch:{ all -> 0x01d5 }
        r0 = r23;
        r2 = r0.zzaxf;	 Catch:{ all -> 0x01d5 }
        r2 = r2.longValue();	 Catch:{ all -> 0x01d5 }
        r7.zzt(r2);	 Catch:{ all -> 0x01d5 }
        r2 = r7.zzhp();	 Catch:{ all -> 0x01d5 }
        r0 = r23;
        r0.zzagv = r2;	 Catch:{ all -> 0x01d5 }
        r2 = r34.zzjq();	 Catch:{ all -> 0x01d5 }
        r2.zza(r7);	 Catch:{ all -> 0x01d5 }
        goto L_0x0c3f;
    L_0x0d24:
        r4 = 0;
        goto L_0x0cd1;
    L_0x0d26:
        r2 = 0;
        goto L_0x0ce9;
    L_0x0d28:
        r0 = r34;
        r2 = r0.zzadj;	 Catch:{ all -> 0x01d5 }
        r2 = r2.zzgo();	 Catch:{ all -> 0x01d5 }
        r2 = r2.zzjg();	 Catch:{ all -> 0x01d5 }
        r3 = "Did not find measurement config or missing version info. appId";
        r0 = r22;
        r4 = r0.zzaua;	 Catch:{ all -> 0x01d5 }
        r4 = r4.zztt;	 Catch:{ all -> 0x01d5 }
        r4 = com.google.android.gms.measurement.internal.zzap.zzbv(r4);	 Catch:{ all -> 0x01d5 }
        r2.zzg(r3, r4);	 Catch:{ all -> 0x01d5 }
        goto L_0x0c77;
    L_0x0d46:
        r2 = r2.zzawe;	 Catch:{ all -> 0x01d5 }
        r0 = r23;
        r0.zzaxy = r2;	 Catch:{ all -> 0x01d5 }
        goto L_0x0c77;
    L_0x0d4e:
        r2 = ")";
        r7.append(r2);	 Catch:{ all -> 0x01d5 }
        r2 = r4.getWritableDatabase();	 Catch:{ all -> 0x01d5 }
        r3 = "raw_events";
        r7 = r7.toString();	 Catch:{ all -> 0x01d5 }
        r8 = 0;
        r2 = r2.delete(r3, r7, r8);	 Catch:{ all -> 0x01d5 }
        r3 = r5.size();	 Catch:{ all -> 0x01d5 }
        if (r2 == r3) goto L_0x0d84;
    L_0x0d6a:
        r3 = r4.zzgo();	 Catch:{ all -> 0x01d5 }
        r3 = r3.zzjd();	 Catch:{ all -> 0x01d5 }
        r4 = "Deleted fewer rows from raw events table than expected";
        r2 = java.lang.Integer.valueOf(r2);	 Catch:{ all -> 0x01d5 }
        r5 = r5.size();	 Catch:{ all -> 0x01d5 }
        r5 = java.lang.Integer.valueOf(r5);	 Catch:{ all -> 0x01d5 }
        r3.zze(r4, r2, r5);	 Catch:{ all -> 0x01d5 }
    L_0x0d84:
        r3 = r34.zzjq();	 Catch:{ all -> 0x01d5 }
        r2 = r3.getWritableDatabase();	 Catch:{ all -> 0x01d5 }
        r4 = "delete from raw_events_metadata where app_id=? and metadata_fingerprint not in (select distinct metadata_fingerprint from raw_events where app_id=?)";
        r5 = 2;
        r5 = new java.lang.String[r5];	 Catch:{ SQLiteException -> 0x0dab }
        r7 = 0;
        r5[r7] = r6;	 Catch:{ SQLiteException -> 0x0dab }
        r7 = 1;
        r5[r7] = r6;	 Catch:{ SQLiteException -> 0x0dab }
        r2.execSQL(r4, r5);	 Catch:{ SQLiteException -> 0x0dab }
    L_0x0d9b:
        r2 = r34.zzjq();	 Catch:{ all -> 0x01d5 }
        r2.setTransactionSuccessful();	 Catch:{ all -> 0x01d5 }
        r2 = r34.zzjq();
        r2.endTransaction();
        r2 = 1;
    L_0x0daa:
        return r2;
    L_0x0dab:
        r2 = move-exception;
        r3 = r3.zzgo();	 Catch:{ all -> 0x01d5 }
        r3 = r3.zzjd();	 Catch:{ all -> 0x01d5 }
        r4 = "Failed to remove unused event metadata. appId";
        r5 = com.google.android.gms.measurement.internal.zzap.zzbv(r6);	 Catch:{ all -> 0x01d5 }
        r3.zze(r4, r5, r2);	 Catch:{ all -> 0x01d5 }
        goto L_0x0d9b;
    L_0x0dbf:
        r2 = r34.zzjq();	 Catch:{ all -> 0x01d5 }
        r2.setTransactionSuccessful();	 Catch:{ all -> 0x01d5 }
        r2 = r34.zzjq();
        r2.endTransaction();
        r2 = 0;
        goto L_0x0daa;
    L_0x0dcf:
        r2 = move-exception;
        r3 = r11;
        goto L_0x037c;
    L_0x0dd3:
        r2 = move-exception;
        goto L_0x0309;
    L_0x0dd6:
        r2 = move-exception;
        r4 = r12;
        goto L_0x0309;
    L_0x0dda:
        r2 = r4;
        goto L_0x0cdf;
    L_0x0ddd:
        r3 = r4;
        goto L_0x0b1a;
    L_0x0de0:
        r4 = r2;
        goto L_0x0a12;
    L_0x0de3:
        r20 = r2;
        goto L_0x094f;
    L_0x0de7:
        r2 = r6;
        goto L_0x0649;
    L_0x0dea:
        r2 = r3;
        goto L_0x0579;
    L_0x0ded:
        r18 = r2;
        goto L_0x050b;
    L_0x0df1:
        r2 = r3;
        goto L_0x03d9;
    L_0x0df4:
        r2 = r14;
        r4 = r13;
        r5 = r18;
        goto L_0x0166;
    L_0x0dfa:
        r2 = r14;
        goto L_0x067e;
    L_0x0dfd:
        r2 = r3;
        goto L_0x068d;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.zzfa.zzd(java.lang.String, long):boolean");
    }

    private static zzgg[] zza(zzgg[] com_google_android_gms_internal_measurement_zzggArr, String str) {
        int i = 0;
        while (i < com_google_android_gms_internal_measurement_zzggArr.length) {
            if (str.equals(com_google_android_gms_internal_measurement_zzggArr[i].name)) {
                break;
            }
            i++;
        }
        i = -1;
        return i < 0 ? com_google_android_gms_internal_measurement_zzggArr : zza(com_google_android_gms_internal_measurement_zzggArr, i);
    }

    private static zzgg[] zza(zzgg[] com_google_android_gms_internal_measurement_zzggArr, int i) {
        Object obj = new zzgg[(com_google_android_gms_internal_measurement_zzggArr.length - 1)];
        if (i > 0) {
            System.arraycopy(com_google_android_gms_internal_measurement_zzggArr, 0, obj, 0, i);
        }
        if (i < obj.length) {
            System.arraycopy(com_google_android_gms_internal_measurement_zzggArr, i + 1, obj, i, obj.length - i);
        }
        return obj;
    }

    private static zzgg[] zza(zzgg[] com_google_android_gms_internal_measurement_zzggArr, int i, String str) {
        for (zzgg com_google_android_gms_internal_measurement_zzgg : com_google_android_gms_internal_measurement_zzggArr) {
            if ("_err".equals(com_google_android_gms_internal_measurement_zzgg.name)) {
                return com_google_android_gms_internal_measurement_zzggArr;
            }
        }
        Object obj = new zzgg[(com_google_android_gms_internal_measurement_zzggArr.length + 2)];
        System.arraycopy(com_google_android_gms_internal_measurement_zzggArr, 0, obj, 0, com_google_android_gms_internal_measurement_zzggArr.length);
        zzgg com_google_android_gms_internal_measurement_zzgg2 = new zzgg();
        com_google_android_gms_internal_measurement_zzgg2.name = "_err";
        com_google_android_gms_internal_measurement_zzgg2.zzawx = Long.valueOf((long) i);
        zzgg com_google_android_gms_internal_measurement_zzgg3 = new zzgg();
        com_google_android_gms_internal_measurement_zzgg3.name = "_ev";
        com_google_android_gms_internal_measurement_zzgg3.zzamp = str;
        obj[obj.length - 2] = com_google_android_gms_internal_measurement_zzgg2;
        obj[obj.length - 1] = com_google_android_gms_internal_measurement_zzgg3;
        return obj;
    }

    private final zzgd[] zza(String str, zzgl[] com_google_android_gms_internal_measurement_zzglArr, zzgf[] com_google_android_gms_internal_measurement_zzgfArr) {
        Preconditions.checkNotEmpty(str);
        return zzjp().zza(str, com_google_android_gms_internal_measurement_zzgfArr, com_google_android_gms_internal_measurement_zzglArr);
    }

    final void zza(int i, Throwable th, byte[] bArr, String str) {
        zzco zzjq;
        zzaf();
        zzlr();
        if (bArr == null) {
            try {
                bArr = new byte[0];
            } catch (Throwable th2) {
                this.zzatq = false;
                zzlw();
            }
        }
        List<Long> list = this.zzatu;
        this.zzatu = null;
        if ((i == Callback.DEFAULT_DRAG_ANIMATION_DURATION || i == 204) && th == null) {
            try {
                this.zzadj.zzgp().zzane.set(this.zzadj.zzbx().currentTimeMillis());
                this.zzadj.zzgp().zzanf.set(0);
                zzlv();
                this.zzadj.zzgo().zzjl().zze("Successful upload. Got network response. code, size", Integer.valueOf(i), Integer.valueOf(bArr.length));
                zzjq().beginTransaction();
                try {
                    for (Long l : list) {
                        try {
                            zzjq = zzjq();
                            long longValue = l.longValue();
                            zzjq.zzaf();
                            zzjq.zzcl();
                            if (zzjq.getWritableDatabase().delete("queue", "rowid=?", new String[]{String.valueOf(longValue)}) != 1) {
                                throw new SQLiteException("Deleted fewer rows from queue than expected");
                            }
                        } catch (SQLiteException e) {
                            zzjq.zzgo().zzjd().zzg("Failed to delete a bundle in a queue table", e);
                            throw e;
                        } catch (SQLiteException e2) {
                            if (this.zzatv == null || !this.zzatv.contains(l)) {
                                throw e2;
                            }
                        }
                    }
                    zzjq().setTransactionSuccessful();
                    this.zzatv = null;
                    if (zzlo().zzfb() && zzlu()) {
                        zzlt();
                    } else {
                        this.zzatw = -1;
                        zzlv();
                    }
                    this.zzatl = 0;
                } finally {
                    zzjq().endTransaction();
                }
            } catch (SQLiteException e3) {
                this.zzadj.zzgo().zzjd().zzg("Database error while trying to delete uploaded bundles", e3);
                this.zzatl = this.zzadj.zzbx().elapsedRealtime();
                this.zzadj.zzgo().zzjl().zzg("Disable upload, time", Long.valueOf(this.zzatl));
            }
        } else {
            this.zzadj.zzgo().zzjl().zze("Network upload failed. Will retry later. code, error", Integer.valueOf(i), th);
            this.zzadj.zzgp().zzanf.set(this.zzadj.zzbx().currentTimeMillis());
            boolean z = i == 503 || i == 429;
            if (z) {
                this.zzadj.zzgp().zzang.set(this.zzadj.zzbx().currentTimeMillis());
            }
            if (this.zzadj.zzgq().zzaz(str)) {
                zzjq().zzc(list);
            }
            zzlv();
        }
        this.zzatq = false;
        zzlw();
    }

    private final boolean zzlu() {
        zzaf();
        zzlr();
        return zzjq().zzii() || !TextUtils.isEmpty(zzjq().zzid());
    }

    private final void zzb(zzg com_google_android_gms_measurement_internal_zzg) {
        zzaf();
        if (!TextUtils.isEmpty(com_google_android_gms_measurement_internal_zzg.getGmpAppId()) || (zzn.zzic() && !TextUtils.isEmpty(com_google_android_gms_measurement_internal_zzg.zzgw()))) {
            Object zzgw;
            CharSequence charSequence;
            zzn zzgq = this.zzadj.zzgq();
            Builder builder = new Builder();
            CharSequence gmpAppId = com_google_android_gms_measurement_internal_zzg.getGmpAppId();
            if (TextUtils.isEmpty(gmpAppId) && zzn.zzic()) {
                zzgw = com_google_android_gms_measurement_internal_zzg.zzgw();
            } else {
                charSequence = gmpAppId;
            }
            Builder encodedAuthority = builder.scheme((String) zzaf.zzajh.get()).encodedAuthority((String) zzaf.zzaji.get());
            String str = "config/app/";
            String valueOf = String.valueOf(zzgw);
            encodedAuthority.path(valueOf.length() != 0 ? str.concat(valueOf) : new String(str)).appendQueryParameter("app_instance_id", com_google_android_gms_measurement_internal_zzg.getAppInstanceId()).appendQueryParameter("platform", "android").appendQueryParameter("gmp_version", String.valueOf(zzgq.zzhc()));
            String uri = builder.build().toString();
            try {
                Map map;
                URL url = new URL(uri);
                this.zzadj.zzgo().zzjl().zzg("Fetching remote configuration", com_google_android_gms_measurement_internal_zzg.zzal());
                zzgb zzcf = zzln().zzcf(com_google_android_gms_measurement_internal_zzg.zzal());
                charSequence = zzln().zzcg(com_google_android_gms_measurement_internal_zzg.zzal());
                if (zzcf == null || TextUtils.isEmpty(charSequence)) {
                    map = null;
                } else {
                    Map arrayMap = new ArrayMap();
                    arrayMap.put("If-Modified-Since", charSequence);
                    map = arrayMap;
                }
                this.zzatp = true;
                zzco zzlo = zzlo();
                String zzal = com_google_android_gms_measurement_internal_zzg.zzal();
                zzav com_google_android_gms_measurement_internal_zzfd = new zzfd(this);
                zzlo.zzaf();
                zzlo.zzcl();
                Preconditions.checkNotNull(url);
                Preconditions.checkNotNull(com_google_android_gms_measurement_internal_zzfd);
                zzlo.zzgn().zzd(new zzax(zzlo, zzal, url, null, map, com_google_android_gms_measurement_internal_zzfd));
                return;
            } catch (MalformedURLException e) {
                this.zzadj.zzgo().zzjd().zze("Failed to parse config URL. Not fetching. appId", zzap.zzbv(com_google_android_gms_measurement_internal_zzg.zzal()), uri);
                return;
            }
        }
        zzb(com_google_android_gms_measurement_internal_zzg.zzal(), 204, null, null, null);
    }

    final void zzb(String str, int i, Throwable th, byte[] bArr, Map<String, List<String>> map) {
        boolean z = true;
        zzaf();
        zzlr();
        Preconditions.checkNotEmpty(str);
        if (bArr == null) {
            try {
                bArr = new byte[0];
            } catch (Throwable th2) {
                this.zzatp = false;
                zzlw();
            }
        }
        this.zzadj.zzgo().zzjl().zzg("onConfigFetched. Response size", Integer.valueOf(bArr.length));
        zzjq().beginTransaction();
        zzg zzbl = zzjq().zzbl(str);
        boolean z2 = (i == Callback.DEFAULT_DRAG_ANIMATION_DURATION || i == 204 || i == 304) && th == null;
        if (zzbl == null) {
            this.zzadj.zzgo().zzjg().zzg("App does not exist in onConfigFetched. appId", zzap.zzbv(str));
        } else if (z2 || i == 404) {
            String str2;
            List list = map != null ? (List) map.get("Last-Modified") : null;
            if (list == null || list.size() <= 0) {
                str2 = null;
            } else {
                str2 = (String) list.get(0);
            }
            if (i == 404 || i == 304) {
                if (zzln().zzcf(str) == null && !zzln().zza(str, null, null)) {
                    zzjq().endTransaction();
                    this.zzatp = false;
                    zzlw();
                    return;
                }
            } else if (!zzln().zza(str, bArr, str2)) {
                zzjq().endTransaction();
                this.zzatp = false;
                zzlw();
                return;
            }
            zzbl.zzy(this.zzadj.zzbx().currentTimeMillis());
            zzjq().zza(zzbl);
            if (i == 404) {
                this.zzadj.zzgo().zzji().zzg("Config not found. Using empty config. appId", str);
            } else {
                this.zzadj.zzgo().zzjl().zze("Successfully fetched config. Got network response. code, size", Integer.valueOf(i), Integer.valueOf(bArr.length));
            }
            if (zzlo().zzfb() && zzlu()) {
                zzlt();
            } else {
                zzlv();
            }
        } else {
            zzbl.zzz(this.zzadj.zzbx().currentTimeMillis());
            zzjq().zza(zzbl);
            this.zzadj.zzgo().zzjl().zze("Fetching config failed. code, error", Integer.valueOf(i), th);
            zzln().zzch(str);
            this.zzadj.zzgp().zzanf.set(this.zzadj.zzbx().currentTimeMillis());
            if (!(i == 503 || i == 429)) {
                z = false;
            }
            if (z) {
                this.zzadj.zzgp().zzang.set(this.zzadj.zzbx().currentTimeMillis());
            }
            zzlv();
        }
        zzjq().setTransactionSuccessful();
        zzjq().endTransaction();
        this.zzatp = false;
        zzlw();
    }

    private final void zzlv() {
        zzaf();
        zzlr();
        if (zzlz()) {
            long abs;
            if (this.zzatl > 0) {
                abs = 3600000 - Math.abs(this.zzadj.zzbx().elapsedRealtime() - this.zzatl);
                if (abs > 0) {
                    this.zzadj.zzgo().zzjl().zzg("Upload has been suspended. Will update scheduling later in approximately ms", Long.valueOf(abs));
                    zzlp().unregister();
                    zzlq().cancel();
                    return;
                }
                this.zzatl = 0;
            }
            if (this.zzadj.zzkr() && zzlu()) {
                long currentTimeMillis = this.zzadj.zzbx().currentTimeMillis();
                long max = Math.max(0, ((Long) zzaf.zzakd.get()).longValue());
                Object obj = (zzjq().zzij() || zzjq().zzie()) ? 1 : null;
                if (obj != null) {
                    CharSequence zzhy = this.zzadj.zzgq().zzhy();
                    if (TextUtils.isEmpty(zzhy) || ".none.".equals(zzhy)) {
                        abs = Math.max(0, ((Long) zzaf.zzajx.get()).longValue());
                    } else {
                        abs = Math.max(0, ((Long) zzaf.zzajy.get()).longValue());
                    }
                } else {
                    abs = Math.max(0, ((Long) zzaf.zzajw.get()).longValue());
                }
                long j = this.zzadj.zzgp().zzane.get();
                long j2 = this.zzadj.zzgp().zzanf.get();
                long max2 = Math.max(zzjq().zzig(), zzjq().zzih());
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
                    if (!zzjo().zzb(j, abs)) {
                        currentTimeMillis = j + abs;
                    }
                    if (j2 != 0 && j2 >= max2) {
                        for (int i = 0; i < Math.min(20, Math.max(0, ((Integer) zzaf.zzakf.get()).intValue())); i++) {
                            currentTimeMillis += (1 << i) * Math.max(0, ((Long) zzaf.zzake.get()).longValue());
                            if (currentTimeMillis > j2) {
                                break;
                            }
                        }
                        currentTimeMillis = 0;
                    }
                }
                if (currentTimeMillis == 0) {
                    this.zzadj.zzgo().zzjl().zzbx("Next upload time is 0");
                    zzlp().unregister();
                    zzlq().cancel();
                    return;
                } else if (zzlo().zzfb()) {
                    long j3 = this.zzadj.zzgp().zzang.get();
                    abs = Math.max(0, ((Long) zzaf.zzaju.get()).longValue());
                    if (zzjo().zzb(j3, abs)) {
                        abs = currentTimeMillis;
                    } else {
                        abs = Math.max(currentTimeMillis, abs + j3);
                    }
                    zzlp().unregister();
                    abs -= this.zzadj.zzbx().currentTimeMillis();
                    if (abs <= 0) {
                        abs = Math.max(0, ((Long) zzaf.zzajz.get()).longValue());
                        this.zzadj.zzgp().zzane.set(this.zzadj.zzbx().currentTimeMillis());
                    }
                    this.zzadj.zzgo().zzjl().zzg("Upload scheduled in approximately ms", Long.valueOf(abs));
                    zzlq().zzh(abs);
                    return;
                } else {
                    this.zzadj.zzgo().zzjl().zzbx("No network");
                    zzlp().zzey();
                    zzlq().cancel();
                    return;
                }
            }
            this.zzadj.zzgo().zzjl().zzbx("Nothing to upload or uploading impossible");
            zzlp().unregister();
            zzlq().cancel();
        }
    }

    final void zzg(Runnable runnable) {
        zzaf();
        if (this.zzatm == null) {
            this.zzatm = new ArrayList();
        }
        this.zzatm.add(runnable);
    }

    private final void zzlw() {
        zzaf();
        if (this.zzatp || this.zzatq || this.zzatr) {
            this.zzadj.zzgo().zzjl().zzd("Not stopping services. fetch, network, upload", Boolean.valueOf(this.zzatp), Boolean.valueOf(this.zzatq), Boolean.valueOf(this.zzatr));
            return;
        }
        this.zzadj.zzgo().zzjl().zzbx("Stopping uploading service(s)");
        if (this.zzatm != null) {
            for (Runnable run : this.zzatm) {
                run.run();
            }
            this.zzatm.clear();
        }
    }

    private final Boolean zzc(zzg com_google_android_gms_measurement_internal_zzg) {
        try {
            if (com_google_android_gms_measurement_internal_zzg.zzha() != -2147483648L) {
                if (com_google_android_gms_measurement_internal_zzg.zzha() == ((long) Wrappers.packageManager(this.zzadj.getContext()).getPackageInfo(com_google_android_gms_measurement_internal_zzg.zzal(), 0).versionCode)) {
                    return Boolean.valueOf(true);
                }
            }
            String str = Wrappers.packageManager(this.zzadj.getContext()).getPackageInfo(com_google_android_gms_measurement_internal_zzg.zzal(), 0).versionName;
            if (com_google_android_gms_measurement_internal_zzg.zzak() != null && com_google_android_gms_measurement_internal_zzg.zzak().equals(str)) {
                return Boolean.valueOf(true);
            }
            return Boolean.valueOf(false);
        } catch (NameNotFoundException e) {
            return null;
        }
    }

    private final boolean zzlx() {
        zzaf();
        try {
            this.zzatt = new RandomAccessFile(new File(this.zzadj.getContext().getFilesDir(), "google_app_measurement.db"), "rw").getChannel();
            this.zzats = this.zzatt.tryLock();
            if (this.zzats != null) {
                this.zzadj.zzgo().zzjl().zzbx("Storage concurrent access okay");
                return true;
            }
            this.zzadj.zzgo().zzjd().zzbx("Storage concurrent data access panic");
            return false;
        } catch (FileNotFoundException e) {
            this.zzadj.zzgo().zzjd().zzg("Failed to acquire storage lock", e);
        } catch (IOException e2) {
            this.zzadj.zzgo().zzjd().zzg("Failed to access storage lock file", e2);
        }
    }

    private final int zza(FileChannel fileChannel) {
        int i = 0;
        zzaf();
        if (fileChannel == null || !fileChannel.isOpen()) {
            this.zzadj.zzgo().zzjd().zzbx("Bad channel to read from");
        } else {
            ByteBuffer allocate = ByteBuffer.allocate(4);
            try {
                fileChannel.position(0);
                int read = fileChannel.read(allocate);
                if (read == 4) {
                    allocate.flip();
                    i = allocate.getInt();
                } else if (read != -1) {
                    this.zzadj.zzgo().zzjg().zzg("Unexpected data length. Bytes read", Integer.valueOf(read));
                }
            } catch (IOException e) {
                this.zzadj.zzgo().zzjd().zzg("Failed to read from channel", e);
            }
        }
        return i;
    }

    private final boolean zza(int i, FileChannel fileChannel) {
        zzaf();
        if (fileChannel == null || !fileChannel.isOpen()) {
            this.zzadj.zzgo().zzjd().zzbx("Bad channel to read from");
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
            this.zzadj.zzgo().zzjd().zzg("Error writing to channel. Bytes written", Long.valueOf(fileChannel.size()));
            return true;
        } catch (IOException e) {
            this.zzadj.zzgo().zzjd().zzg("Failed to write to channel", e);
            return false;
        }
    }

    final void zzly() {
        zzaf();
        zzlr();
        if (!this.zzatk) {
            this.zzadj.zzgo().zzjj().zzbx("This instance being marked as an uploader");
            zzaf();
            zzlr();
            if (zzlz() && zzlx()) {
                int zza = zza(this.zzatt);
                int zzja = this.zzadj.zzgf().zzja();
                zzaf();
                if (zza > zzja) {
                    this.zzadj.zzgo().zzjd().zze("Panic: can't downgrade version. Previous, current version", Integer.valueOf(zza), Integer.valueOf(zzja));
                } else if (zza < zzja) {
                    if (zza(zzja, this.zzatt)) {
                        this.zzadj.zzgo().zzjl().zze("Storage version upgraded. Previous, current version", Integer.valueOf(zza), Integer.valueOf(zzja));
                    } else {
                        this.zzadj.zzgo().zzjd().zze("Storage version upgrade failed. Previous, current version", Integer.valueOf(zza), Integer.valueOf(zzja));
                    }
                }
            }
            this.zzatk = true;
            zzlv();
        }
    }

    private final boolean zzlz() {
        zzaf();
        zzlr();
        return this.zzatk;
    }

    final void zzd(zzh com_google_android_gms_measurement_internal_zzh) {
        if (this.zzatu != null) {
            this.zzatv = new ArrayList();
            this.zzatv.addAll(this.zzatu);
        }
        zzco zzjq = zzjq();
        String str = com_google_android_gms_measurement_internal_zzh.packageName;
        Preconditions.checkNotEmpty(str);
        zzjq.zzaf();
        zzjq.zzcl();
        try {
            SQLiteDatabase writableDatabase = zzjq.getWritableDatabase();
            String[] strArr = new String[]{str};
            int delete = writableDatabase.delete("main_event_params", "app_id=?", strArr) + ((((((((writableDatabase.delete("apps", "app_id=?", strArr) + 0) + writableDatabase.delete("events", "app_id=?", strArr)) + writableDatabase.delete("user_attributes", "app_id=?", strArr)) + writableDatabase.delete("conditional_properties", "app_id=?", strArr)) + writableDatabase.delete("raw_events", "app_id=?", strArr)) + writableDatabase.delete("raw_events_metadata", "app_id=?", strArr)) + writableDatabase.delete("queue", "app_id=?", strArr)) + writableDatabase.delete("audience_filter_values", "app_id=?", strArr));
            if (delete > 0) {
                zzjq.zzgo().zzjl().zze("Reset analytics data. app, records", str, Integer.valueOf(delete));
            }
        } catch (SQLiteException e) {
            zzjq.zzgo().zzjd().zze("Error resetting analytics data. appId, error", zzap.zzbv(str), e);
        }
        zzh zza = zza(this.zzadj.getContext(), com_google_android_gms_measurement_internal_zzh.packageName, com_google_android_gms_measurement_internal_zzh.zzafx, com_google_android_gms_measurement_internal_zzh.zzagg, com_google_android_gms_measurement_internal_zzh.zzagi, com_google_android_gms_measurement_internal_zzh.zzagj, com_google_android_gms_measurement_internal_zzh.zzagx, com_google_android_gms_measurement_internal_zzh.zzagk);
        if (!this.zzadj.zzgq().zzbd(com_google_android_gms_measurement_internal_zzh.packageName) || com_google_android_gms_measurement_internal_zzh.zzagg) {
            zzf(zza);
        }
    }

    private final zzh zza(Context context, String str, String str2, boolean z, boolean z2, boolean z3, long j, String str3) {
        Object charSequence;
        String str4 = "Unknown";
        String str5 = "Unknown";
        int i = Integer.MIN_VALUE;
        String str6 = "Unknown";
        PackageManager packageManager = context.getPackageManager();
        if (packageManager == null) {
            this.zzadj.zzgo().zzjd().zzbx("PackageManager is null, can not log app install information");
            return null;
        }
        try {
            str4 = packageManager.getInstallerPackageName(str);
        } catch (IllegalArgumentException e) {
            this.zzadj.zzgo().zzjd().zzg("Error retrieving installer package name. appId", zzap.zzbv(str));
        }
        if (str4 == null) {
            str4 = "manual_install";
        } else if ("com.android.vending".equals(str4)) {
            str4 = TtmlNode.ANONYMOUS_REGION_ID;
        }
        try {
            PackageInfo packageInfo = Wrappers.packageManager(context).getPackageInfo(str, 0);
            if (packageInfo != null) {
                CharSequence applicationLabel = Wrappers.packageManager(context).getApplicationLabel(str);
                if (TextUtils.isEmpty(applicationLabel)) {
                    String str7 = str6;
                } else {
                    charSequence = applicationLabel.toString();
                }
                try {
                    str5 = packageInfo.versionName;
                    i = packageInfo.versionCode;
                } catch (NameNotFoundException e2) {
                    this.zzadj.zzgo().zzjd().zze("Error retrieving newly installed package info. appId, appName", zzap.zzbv(str), charSequence);
                    return null;
                }
            }
            this.zzadj.zzgr();
            long j2 = 0;
            if (this.zzadj.zzgq().zzbe(str)) {
                j2 = j;
            }
            return new zzh(str, str2, str5, (long) i, str4, this.zzadj.zzgq().zzhc(), this.zzadj.zzgm().zzd(context, str), null, z, false, TtmlNode.ANONYMOUS_REGION_ID, 0, j2, 0, z2, z3, false, str3);
        } catch (NameNotFoundException e3) {
            charSequence = str6;
            this.zzadj.zzgo().zzjd().zze("Error retrieving newly installed package info. appId, appName", zzap.zzbv(str), charSequence);
            return null;
        }
    }

    final void zzb(zzfh com_google_android_gms_measurement_internal_zzfh, zzh com_google_android_gms_measurement_internal_zzh) {
        int i = 0;
        zzaf();
        zzlr();
        if (!TextUtils.isEmpty(com_google_android_gms_measurement_internal_zzh.zzafx) || !TextUtils.isEmpty(com_google_android_gms_measurement_internal_zzh.zzagk)) {
            if (com_google_android_gms_measurement_internal_zzh.zzagg) {
                zzfj zzi;
                if (this.zzadj.zzgq().zze(com_google_android_gms_measurement_internal_zzh.packageName, zzaf.zzalj) && "_ap".equals(com_google_android_gms_measurement_internal_zzfh.name)) {
                    zzi = zzjq().zzi(com_google_android_gms_measurement_internal_zzh.packageName, "_ap");
                    if (!(zzi == null || !"auto".equals(com_google_android_gms_measurement_internal_zzfh.origin) || "auto".equals(zzi.origin))) {
                        this.zzadj.zzgo().zzjk().zzbx("Not setting lower priority ad personalization property");
                        return;
                    }
                }
                int zzcs = this.zzadj.zzgm().zzcs(com_google_android_gms_measurement_internal_zzfh.name);
                String zza;
                if (zzcs != 0) {
                    this.zzadj.zzgm();
                    zza = zzfk.zza(com_google_android_gms_measurement_internal_zzfh.name, 24, true);
                    if (com_google_android_gms_measurement_internal_zzfh.name != null) {
                        i = com_google_android_gms_measurement_internal_zzfh.name.length();
                    }
                    this.zzadj.zzgm().zza(com_google_android_gms_measurement_internal_zzh.packageName, zzcs, "_ev", zza, i);
                    return;
                }
                zzcs = this.zzadj.zzgm().zzi(com_google_android_gms_measurement_internal_zzfh.name, com_google_android_gms_measurement_internal_zzfh.getValue());
                if (zzcs != 0) {
                    this.zzadj.zzgm();
                    zza = zzfk.zza(com_google_android_gms_measurement_internal_zzfh.name, 24, true);
                    Object value = com_google_android_gms_measurement_internal_zzfh.getValue();
                    if (value != null && ((value instanceof String) || (value instanceof CharSequence))) {
                        i = String.valueOf(value).length();
                    }
                    this.zzadj.zzgm().zza(com_google_android_gms_measurement_internal_zzh.packageName, zzcs, "_ev", zza, i);
                    return;
                }
                Object zzj = this.zzadj.zzgm().zzj(com_google_android_gms_measurement_internal_zzfh.name, com_google_android_gms_measurement_internal_zzfh.getValue());
                if (zzj != null) {
                    zzi = new zzfj(com_google_android_gms_measurement_internal_zzh.packageName, com_google_android_gms_measurement_internal_zzfh.origin, com_google_android_gms_measurement_internal_zzfh.name, com_google_android_gms_measurement_internal_zzfh.zzaue, zzj);
                    this.zzadj.zzgo().zzjk().zze("Setting user property", this.zzadj.zzgl().zzbu(zzi.name), zzj);
                    zzjq().beginTransaction();
                    try {
                        zzg(com_google_android_gms_measurement_internal_zzh);
                        boolean zza2 = zzjq().zza(zzi);
                        zzjq().setTransactionSuccessful();
                        if (zza2) {
                            this.zzadj.zzgo().zzjk().zze("User property set", this.zzadj.zzgl().zzbu(zzi.name), zzi.value);
                        } else {
                            this.zzadj.zzgo().zzjd().zze("Too many unique user properties are set. Ignoring user property", this.zzadj.zzgl().zzbu(zzi.name), zzi.value);
                            this.zzadj.zzgm().zza(com_google_android_gms_measurement_internal_zzh.packageName, 9, null, null, 0);
                        }
                        zzjq().endTransaction();
                        return;
                    } catch (Throwable th) {
                        zzjq().endTransaction();
                    }
                } else {
                    return;
                }
            }
            zzg(com_google_android_gms_measurement_internal_zzh);
        }
    }

    final void zzc(zzfh com_google_android_gms_measurement_internal_zzfh, zzh com_google_android_gms_measurement_internal_zzh) {
        zzaf();
        zzlr();
        if (!TextUtils.isEmpty(com_google_android_gms_measurement_internal_zzh.zzafx) || !TextUtils.isEmpty(com_google_android_gms_measurement_internal_zzh.zzagk)) {
            if (com_google_android_gms_measurement_internal_zzh.zzagg) {
                if (this.zzadj.zzgq().zze(com_google_android_gms_measurement_internal_zzh.packageName, zzaf.zzalj) && "_ap".equals(com_google_android_gms_measurement_internal_zzfh.name)) {
                    zzfj zzi = zzjq().zzi(com_google_android_gms_measurement_internal_zzh.packageName, "_ap");
                    if (!(zzi == null || !"auto".equals(com_google_android_gms_measurement_internal_zzfh.origin) || "auto".equals(zzi.origin))) {
                        this.zzadj.zzgo().zzjk().zzbx("Not removing higher priority ad personalization property");
                        return;
                    }
                }
                this.zzadj.zzgo().zzjk().zzg("Removing user property", this.zzadj.zzgl().zzbu(com_google_android_gms_measurement_internal_zzfh.name));
                zzjq().beginTransaction();
                try {
                    zzg(com_google_android_gms_measurement_internal_zzh);
                    zzjq().zzh(com_google_android_gms_measurement_internal_zzh.packageName, com_google_android_gms_measurement_internal_zzfh.name);
                    zzjq().setTransactionSuccessful();
                    this.zzadj.zzgo().zzjk().zzg("User property removed", this.zzadj.zzgl().zzbu(com_google_android_gms_measurement_internal_zzfh.name));
                } finally {
                    zzjq().endTransaction();
                }
            } else {
                zzg(com_google_android_gms_measurement_internal_zzh);
            }
        }
    }

    final void zzb(zzez com_google_android_gms_measurement_internal_zzez) {
        this.zzatn++;
    }

    final void zzma() {
        this.zzato++;
    }

    final zzbt zzmb() {
        return this.zzadj;
    }

    final void zzf(zzh com_google_android_gms_measurement_internal_zzh) {
        zzco zzjq;
        String zzal;
        zzaf();
        zzlr();
        Preconditions.checkNotNull(com_google_android_gms_measurement_internal_zzh);
        Preconditions.checkNotEmpty(com_google_android_gms_measurement_internal_zzh.packageName);
        if (!TextUtils.isEmpty(com_google_android_gms_measurement_internal_zzh.zzafx) || !TextUtils.isEmpty(com_google_android_gms_measurement_internal_zzh.zzagk)) {
            zzg zzbl = zzjq().zzbl(com_google_android_gms_measurement_internal_zzh.packageName);
            if (!(zzbl == null || !TextUtils.isEmpty(zzbl.getGmpAppId()) || TextUtils.isEmpty(com_google_android_gms_measurement_internal_zzh.zzafx))) {
                zzbl.zzy(0);
                zzjq().zza(zzbl);
                zzln().zzci(com_google_android_gms_measurement_internal_zzh.packageName);
            }
            if (com_google_android_gms_measurement_internal_zzh.zzagg) {
                int i;
                Bundle bundle;
                long j = com_google_android_gms_measurement_internal_zzh.zzagx;
                if (j == 0) {
                    j = this.zzadj.zzbx().currentTimeMillis();
                }
                int i2 = com_google_android_gms_measurement_internal_zzh.zzagy;
                if (i2 == 0 || i2 == 1) {
                    i = i2;
                } else {
                    this.zzadj.zzgo().zzjg().zze("Incorrect app type, assuming installed app. appId, appType", zzap.zzbv(com_google_android_gms_measurement_internal_zzh.packageName), Integer.valueOf(i2));
                    i = 0;
                }
                zzjq().beginTransaction();
                try {
                    zzbl = zzjq().zzbl(com_google_android_gms_measurement_internal_zzh.packageName);
                    if (zzbl != null) {
                        this.zzadj.zzgm();
                        if (zzfk.zza(com_google_android_gms_measurement_internal_zzh.zzafx, zzbl.getGmpAppId(), com_google_android_gms_measurement_internal_zzh.zzagk, zzbl.zzgw())) {
                            this.zzadj.zzgo().zzjg().zzg("New GMP App Id passed in. Removing cached database data. appId", zzap.zzbv(zzbl.zzal()));
                            zzjq = zzjq();
                            zzal = zzbl.zzal();
                            zzjq.zzcl();
                            zzjq.zzaf();
                            Preconditions.checkNotEmpty(zzal);
                            SQLiteDatabase writableDatabase = zzjq.getWritableDatabase();
                            String[] strArr = new String[]{zzal};
                            i2 = writableDatabase.delete("audience_filter_values", "app_id=?", strArr) + ((((((((writableDatabase.delete("events", "app_id=?", strArr) + 0) + writableDatabase.delete("user_attributes", "app_id=?", strArr)) + writableDatabase.delete("conditional_properties", "app_id=?", strArr)) + writableDatabase.delete("apps", "app_id=?", strArr)) + writableDatabase.delete("raw_events", "app_id=?", strArr)) + writableDatabase.delete("raw_events_metadata", "app_id=?", strArr)) + writableDatabase.delete("event_filters", "app_id=?", strArr)) + writableDatabase.delete("property_filters", "app_id=?", strArr));
                            if (i2 > 0) {
                                zzjq.zzgo().zzjl().zze("Deleted application data. app, records", zzal, Integer.valueOf(i2));
                            }
                            zzbl = null;
                        }
                    }
                } catch (SQLiteException e) {
                    zzjq.zzgo().zzjd().zze("Error deleting application data. appId, error", zzap.zzbv(zzal), e);
                } catch (Throwable th) {
                    zzjq().endTransaction();
                }
                if (zzbl != null) {
                    if (zzbl.zzha() != -2147483648L) {
                        if (zzbl.zzha() != com_google_android_gms_measurement_internal_zzh.zzagd) {
                            bundle = new Bundle();
                            bundle.putString("_pv", zzbl.zzak());
                            zzc(new zzad("_au", new zzaa(bundle), "auto", j), com_google_android_gms_measurement_internal_zzh);
                        }
                    } else if (!(zzbl.zzak() == null || zzbl.zzak().equals(com_google_android_gms_measurement_internal_zzh.zzts))) {
                        bundle = new Bundle();
                        bundle.putString("_pv", zzbl.zzak());
                        zzc(new zzad("_au", new zzaa(bundle), "auto", j), com_google_android_gms_measurement_internal_zzh);
                    }
                }
                zzg(com_google_android_gms_measurement_internal_zzh);
                zzz com_google_android_gms_measurement_internal_zzz = null;
                if (i == 0) {
                    com_google_android_gms_measurement_internal_zzz = zzjq().zzg(com_google_android_gms_measurement_internal_zzh.packageName, "_f");
                } else if (i == 1) {
                    com_google_android_gms_measurement_internal_zzz = zzjq().zzg(com_google_android_gms_measurement_internal_zzh.packageName, "_v");
                }
                if (com_google_android_gms_measurement_internal_zzz == null) {
                    long j2 = (1 + (j / 3600000)) * 3600000;
                    if (i == 0) {
                        zzb(new zzfh("_fot", j, Long.valueOf(j2), "auto"), com_google_android_gms_measurement_internal_zzh);
                        if (this.zzadj.zzgq().zzbg(com_google_android_gms_measurement_internal_zzh.zzafx)) {
                            zzaf();
                            this.zzadj.zzkg().zzcd(com_google_android_gms_measurement_internal_zzh.packageName);
                        }
                        zzaf();
                        zzlr();
                        Bundle bundle2 = new Bundle();
                        bundle2.putLong("_c", 1);
                        bundle2.putLong("_r", 1);
                        bundle2.putLong("_uwa", 0);
                        bundle2.putLong("_pfo", 0);
                        bundle2.putLong("_sys", 0);
                        bundle2.putLong("_sysu", 0);
                        if (this.zzadj.zzgq().zzbd(com_google_android_gms_measurement_internal_zzh.packageName) && com_google_android_gms_measurement_internal_zzh.zzagz) {
                            bundle2.putLong("_dac", 1);
                        }
                        if (this.zzadj.getContext().getPackageManager() == null) {
                            this.zzadj.zzgo().zzjd().zzg("PackageManager is null, first open report might be inaccurate. appId", zzap.zzbv(com_google_android_gms_measurement_internal_zzh.packageName));
                        } else {
                            ApplicationInfo applicationInfo;
                            PackageInfo packageInfo = null;
                            try {
                                packageInfo = Wrappers.packageManager(this.zzadj.getContext()).getPackageInfo(com_google_android_gms_measurement_internal_zzh.packageName, 0);
                            } catch (NameNotFoundException e2) {
                                this.zzadj.zzgo().zzjd().zze("Package info is null, first open report might be inaccurate. appId", zzap.zzbv(com_google_android_gms_measurement_internal_zzh.packageName), e2);
                            }
                            if (packageInfo != null) {
                                if (packageInfo.firstInstallTime != 0) {
                                    Object obj = null;
                                    if (packageInfo.firstInstallTime != packageInfo.lastUpdateTime) {
                                        bundle2.putLong("_uwa", 1);
                                    } else {
                                        obj = 1;
                                    }
                                    zzb(new zzfh("_fi", j, Long.valueOf(obj != null ? 1 : 0), "auto"), com_google_android_gms_measurement_internal_zzh);
                                }
                            }
                            try {
                                applicationInfo = Wrappers.packageManager(this.zzadj.getContext()).getApplicationInfo(com_google_android_gms_measurement_internal_zzh.packageName, 0);
                            } catch (NameNotFoundException e22) {
                                this.zzadj.zzgo().zzjd().zze("Application info is null, first open report might be inaccurate. appId", zzap.zzbv(com_google_android_gms_measurement_internal_zzh.packageName), e22);
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
                        zzco zzjq2 = zzjq();
                        String str = com_google_android_gms_measurement_internal_zzh.packageName;
                        Preconditions.checkNotEmpty(str);
                        zzjq2.zzaf();
                        zzjq2.zzcl();
                        j2 = zzjq2.zzn(str, "first_open_count");
                        if (j2 >= 0) {
                            bundle2.putLong("_pfo", j2);
                        }
                        zzc(new zzad("_f", new zzaa(bundle2), "auto", j), com_google_android_gms_measurement_internal_zzh);
                    } else if (i == 1) {
                        zzb(new zzfh("_fvt", j, Long.valueOf(j2), "auto"), com_google_android_gms_measurement_internal_zzh);
                        zzaf();
                        zzlr();
                        bundle = new Bundle();
                        bundle.putLong("_c", 1);
                        bundle.putLong("_r", 1);
                        if (this.zzadj.zzgq().zzbd(com_google_android_gms_measurement_internal_zzh.packageName) && com_google_android_gms_measurement_internal_zzh.zzagz) {
                            bundle.putLong("_dac", 1);
                        }
                        zzc(new zzad("_v", new zzaa(bundle), "auto", j), com_google_android_gms_measurement_internal_zzh);
                    }
                    bundle = new Bundle();
                    bundle.putLong("_et", 1);
                    zzc(new zzad("_e", new zzaa(bundle), "auto", j), com_google_android_gms_measurement_internal_zzh);
                } else if (com_google_android_gms_measurement_internal_zzh.zzagw) {
                    zzc(new zzad("_cd", new zzaa(new Bundle()), "auto", j), com_google_android_gms_measurement_internal_zzh);
                }
                zzjq().setTransactionSuccessful();
                zzjq().endTransaction();
                return;
            }
            zzg(com_google_android_gms_measurement_internal_zzh);
        }
    }

    private final zzh zzco(String str) {
        zzg zzbl = zzjq().zzbl(str);
        if (zzbl == null || TextUtils.isEmpty(zzbl.zzak())) {
            this.zzadj.zzgo().zzjk().zzg("No app data available; dropping", str);
            return null;
        }
        Boolean zzc = zzc(zzbl);
        if (zzc == null || zzc.booleanValue()) {
            return new zzh(str, zzbl.getGmpAppId(), zzbl.zzak(), zzbl.zzha(), zzbl.zzhb(), zzbl.zzhc(), zzbl.zzhd(), null, zzbl.isMeasurementEnabled(), false, zzbl.getFirebaseInstanceId(), zzbl.zzhq(), 0, 0, zzbl.zzhr(), zzbl.zzhs(), false, zzbl.zzgw());
        }
        this.zzadj.zzgo().zzjd().zzg("App version does not match; dropping. appId", zzap.zzbv(str));
        return null;
    }

    final void zze(zzl com_google_android_gms_measurement_internal_zzl) {
        zzh zzco = zzco(com_google_android_gms_measurement_internal_zzl.packageName);
        if (zzco != null) {
            zzb(com_google_android_gms_measurement_internal_zzl, zzco);
        }
    }

    final void zzb(zzl com_google_android_gms_measurement_internal_zzl, zzh com_google_android_gms_measurement_internal_zzh) {
        boolean z = true;
        Preconditions.checkNotNull(com_google_android_gms_measurement_internal_zzl);
        Preconditions.checkNotEmpty(com_google_android_gms_measurement_internal_zzl.packageName);
        Preconditions.checkNotNull(com_google_android_gms_measurement_internal_zzl.origin);
        Preconditions.checkNotNull(com_google_android_gms_measurement_internal_zzl.zzahb);
        Preconditions.checkNotEmpty(com_google_android_gms_measurement_internal_zzl.zzahb.name);
        zzaf();
        zzlr();
        if (!TextUtils.isEmpty(com_google_android_gms_measurement_internal_zzh.zzafx) || !TextUtils.isEmpty(com_google_android_gms_measurement_internal_zzh.zzagk)) {
            if (com_google_android_gms_measurement_internal_zzh.zzagg) {
                zzl com_google_android_gms_measurement_internal_zzl2 = new zzl(com_google_android_gms_measurement_internal_zzl);
                com_google_android_gms_measurement_internal_zzl2.active = false;
                zzjq().beginTransaction();
                try {
                    zzl zzj = zzjq().zzj(com_google_android_gms_measurement_internal_zzl2.packageName, com_google_android_gms_measurement_internal_zzl2.zzahb.name);
                    if (!(zzj == null || zzj.origin.equals(com_google_android_gms_measurement_internal_zzl2.origin))) {
                        this.zzadj.zzgo().zzjg().zzd("Updating a conditional user property with different origin. name, origin, origin (from DB)", this.zzadj.zzgl().zzbu(com_google_android_gms_measurement_internal_zzl2.zzahb.name), com_google_android_gms_measurement_internal_zzl2.origin, zzj.origin);
                    }
                    if (zzj != null && zzj.active) {
                        com_google_android_gms_measurement_internal_zzl2.origin = zzj.origin;
                        com_google_android_gms_measurement_internal_zzl2.creationTimestamp = zzj.creationTimestamp;
                        com_google_android_gms_measurement_internal_zzl2.triggerTimeout = zzj.triggerTimeout;
                        com_google_android_gms_measurement_internal_zzl2.triggerEventName = zzj.triggerEventName;
                        com_google_android_gms_measurement_internal_zzl2.zzahd = zzj.zzahd;
                        com_google_android_gms_measurement_internal_zzl2.active = zzj.active;
                        com_google_android_gms_measurement_internal_zzl2.zzahb = new zzfh(com_google_android_gms_measurement_internal_zzl2.zzahb.name, zzj.zzahb.zzaue, com_google_android_gms_measurement_internal_zzl2.zzahb.getValue(), zzj.zzahb.origin);
                        z = false;
                    } else if (TextUtils.isEmpty(com_google_android_gms_measurement_internal_zzl2.triggerEventName)) {
                        com_google_android_gms_measurement_internal_zzl2.zzahb = new zzfh(com_google_android_gms_measurement_internal_zzl2.zzahb.name, com_google_android_gms_measurement_internal_zzl2.creationTimestamp, com_google_android_gms_measurement_internal_zzl2.zzahb.getValue(), com_google_android_gms_measurement_internal_zzl2.zzahb.origin);
                        com_google_android_gms_measurement_internal_zzl2.active = true;
                    } else {
                        z = false;
                    }
                    if (com_google_android_gms_measurement_internal_zzl2.active) {
                        zzfh com_google_android_gms_measurement_internal_zzfh = com_google_android_gms_measurement_internal_zzl2.zzahb;
                        zzfj com_google_android_gms_measurement_internal_zzfj = new zzfj(com_google_android_gms_measurement_internal_zzl2.packageName, com_google_android_gms_measurement_internal_zzl2.origin, com_google_android_gms_measurement_internal_zzfh.name, com_google_android_gms_measurement_internal_zzfh.zzaue, com_google_android_gms_measurement_internal_zzfh.getValue());
                        if (zzjq().zza(com_google_android_gms_measurement_internal_zzfj)) {
                            this.zzadj.zzgo().zzjk().zzd("User property updated immediately", com_google_android_gms_measurement_internal_zzl2.packageName, this.zzadj.zzgl().zzbu(com_google_android_gms_measurement_internal_zzfj.name), com_google_android_gms_measurement_internal_zzfj.value);
                        } else {
                            this.zzadj.zzgo().zzjd().zzd("(2)Too many active user properties, ignoring", zzap.zzbv(com_google_android_gms_measurement_internal_zzl2.packageName), this.zzadj.zzgl().zzbu(com_google_android_gms_measurement_internal_zzfj.name), com_google_android_gms_measurement_internal_zzfj.value);
                        }
                        if (z && com_google_android_gms_measurement_internal_zzl2.zzahd != null) {
                            zzd(new zzad(com_google_android_gms_measurement_internal_zzl2.zzahd, com_google_android_gms_measurement_internal_zzl2.creationTimestamp), com_google_android_gms_measurement_internal_zzh);
                        }
                    }
                    if (zzjq().zza(com_google_android_gms_measurement_internal_zzl2)) {
                        this.zzadj.zzgo().zzjk().zzd("Conditional property added", com_google_android_gms_measurement_internal_zzl2.packageName, this.zzadj.zzgl().zzbu(com_google_android_gms_measurement_internal_zzl2.zzahb.name), com_google_android_gms_measurement_internal_zzl2.zzahb.getValue());
                    } else {
                        this.zzadj.zzgo().zzjd().zzd("Too many conditional properties, ignoring", zzap.zzbv(com_google_android_gms_measurement_internal_zzl2.packageName), this.zzadj.zzgl().zzbu(com_google_android_gms_measurement_internal_zzl2.zzahb.name), com_google_android_gms_measurement_internal_zzl2.zzahb.getValue());
                    }
                    zzjq().setTransactionSuccessful();
                } finally {
                    zzjq().endTransaction();
                }
            } else {
                zzg(com_google_android_gms_measurement_internal_zzh);
            }
        }
    }

    final void zzf(zzl com_google_android_gms_measurement_internal_zzl) {
        zzh zzco = zzco(com_google_android_gms_measurement_internal_zzl.packageName);
        if (zzco != null) {
            zzc(com_google_android_gms_measurement_internal_zzl, zzco);
        }
    }

    final void zzc(zzl com_google_android_gms_measurement_internal_zzl, zzh com_google_android_gms_measurement_internal_zzh) {
        Preconditions.checkNotNull(com_google_android_gms_measurement_internal_zzl);
        Preconditions.checkNotEmpty(com_google_android_gms_measurement_internal_zzl.packageName);
        Preconditions.checkNotNull(com_google_android_gms_measurement_internal_zzl.zzahb);
        Preconditions.checkNotEmpty(com_google_android_gms_measurement_internal_zzl.zzahb.name);
        zzaf();
        zzlr();
        if (!TextUtils.isEmpty(com_google_android_gms_measurement_internal_zzh.zzafx) || !TextUtils.isEmpty(com_google_android_gms_measurement_internal_zzh.zzagk)) {
            if (com_google_android_gms_measurement_internal_zzh.zzagg) {
                zzjq().beginTransaction();
                try {
                    zzg(com_google_android_gms_measurement_internal_zzh);
                    zzl zzj = zzjq().zzj(com_google_android_gms_measurement_internal_zzl.packageName, com_google_android_gms_measurement_internal_zzl.zzahb.name);
                    if (zzj != null) {
                        this.zzadj.zzgo().zzjk().zze("Removing conditional user property", com_google_android_gms_measurement_internal_zzl.packageName, this.zzadj.zzgl().zzbu(com_google_android_gms_measurement_internal_zzl.zzahb.name));
                        zzjq().zzk(com_google_android_gms_measurement_internal_zzl.packageName, com_google_android_gms_measurement_internal_zzl.zzahb.name);
                        if (zzj.active) {
                            zzjq().zzh(com_google_android_gms_measurement_internal_zzl.packageName, com_google_android_gms_measurement_internal_zzl.zzahb.name);
                        }
                        if (com_google_android_gms_measurement_internal_zzl.zzahe != null) {
                            Bundle bundle = null;
                            if (com_google_android_gms_measurement_internal_zzl.zzahe.zzaid != null) {
                                bundle = com_google_android_gms_measurement_internal_zzl.zzahe.zzaid.zziv();
                            }
                            zzd(this.zzadj.zzgm().zza(com_google_android_gms_measurement_internal_zzl.packageName, com_google_android_gms_measurement_internal_zzl.zzahe.name, bundle, zzj.origin, com_google_android_gms_measurement_internal_zzl.zzahe.zzaip, true, false), com_google_android_gms_measurement_internal_zzh);
                        }
                    } else {
                        this.zzadj.zzgo().zzjg().zze("Conditional user property doesn't exist", zzap.zzbv(com_google_android_gms_measurement_internal_zzl.packageName), this.zzadj.zzgl().zzbu(com_google_android_gms_measurement_internal_zzl.zzahb.name));
                    }
                    zzjq().setTransactionSuccessful();
                } finally {
                    zzjq().endTransaction();
                }
            } else {
                zzg(com_google_android_gms_measurement_internal_zzh);
            }
        }
    }

    private final zzg zzg(zzh com_google_android_gms_measurement_internal_zzh) {
        Object obj = 1;
        zzaf();
        zzlr();
        Preconditions.checkNotNull(com_google_android_gms_measurement_internal_zzh);
        Preconditions.checkNotEmpty(com_google_android_gms_measurement_internal_zzh.packageName);
        zzg zzbl = zzjq().zzbl(com_google_android_gms_measurement_internal_zzh.packageName);
        String zzbz = this.zzadj.zzgp().zzbz(com_google_android_gms_measurement_internal_zzh.packageName);
        Object obj2 = null;
        if (zzbl == null) {
            zzbl = new zzg(this.zzadj, com_google_android_gms_measurement_internal_zzh.packageName);
            zzbl.zzam(this.zzadj.zzgm().zzmf());
            zzbl.zzap(zzbz);
            obj2 = 1;
        } else if (!zzbz.equals(zzbl.zzgx())) {
            zzbl.zzap(zzbz);
            zzbl.zzam(this.zzadj.zzgm().zzmf());
            int i = 1;
        }
        if (!TextUtils.equals(com_google_android_gms_measurement_internal_zzh.zzafx, zzbl.getGmpAppId())) {
            zzbl.zzan(com_google_android_gms_measurement_internal_zzh.zzafx);
            obj2 = 1;
        }
        if (!TextUtils.equals(com_google_android_gms_measurement_internal_zzh.zzagk, zzbl.zzgw())) {
            zzbl.zzao(com_google_android_gms_measurement_internal_zzh.zzagk);
            obj2 = 1;
        }
        if (!(TextUtils.isEmpty(com_google_android_gms_measurement_internal_zzh.zzafz) || com_google_android_gms_measurement_internal_zzh.zzafz.equals(zzbl.getFirebaseInstanceId()))) {
            zzbl.zzaq(com_google_android_gms_measurement_internal_zzh.zzafz);
            obj2 = 1;
        }
        if (!(com_google_android_gms_measurement_internal_zzh.zzadt == 0 || com_google_android_gms_measurement_internal_zzh.zzadt == zzbl.zzhc())) {
            zzbl.zzv(com_google_android_gms_measurement_internal_zzh.zzadt);
            obj2 = 1;
        }
        if (!(TextUtils.isEmpty(com_google_android_gms_measurement_internal_zzh.zzts) || com_google_android_gms_measurement_internal_zzh.zzts.equals(zzbl.zzak()))) {
            zzbl.setAppVersion(com_google_android_gms_measurement_internal_zzh.zzts);
            obj2 = 1;
        }
        if (com_google_android_gms_measurement_internal_zzh.zzagd != zzbl.zzha()) {
            zzbl.zzu(com_google_android_gms_measurement_internal_zzh.zzagd);
            obj2 = 1;
        }
        if (!(com_google_android_gms_measurement_internal_zzh.zzage == null || com_google_android_gms_measurement_internal_zzh.zzage.equals(zzbl.zzhb()))) {
            zzbl.zzar(com_google_android_gms_measurement_internal_zzh.zzage);
            obj2 = 1;
        }
        if (com_google_android_gms_measurement_internal_zzh.zzagf != zzbl.zzhd()) {
            zzbl.zzw(com_google_android_gms_measurement_internal_zzh.zzagf);
            obj2 = 1;
        }
        if (com_google_android_gms_measurement_internal_zzh.zzagg != zzbl.isMeasurementEnabled()) {
            zzbl.setMeasurementEnabled(com_google_android_gms_measurement_internal_zzh.zzagg);
            obj2 = 1;
        }
        if (!(TextUtils.isEmpty(com_google_android_gms_measurement_internal_zzh.zzagv) || com_google_android_gms_measurement_internal_zzh.zzagv.equals(zzbl.zzho()))) {
            zzbl.zzas(com_google_android_gms_measurement_internal_zzh.zzagv);
            obj2 = 1;
        }
        if (com_google_android_gms_measurement_internal_zzh.zzagh != zzbl.zzhq()) {
            zzbl.zzag(com_google_android_gms_measurement_internal_zzh.zzagh);
            obj2 = 1;
        }
        if (com_google_android_gms_measurement_internal_zzh.zzagi != zzbl.zzhr()) {
            zzbl.zze(com_google_android_gms_measurement_internal_zzh.zzagi);
            obj2 = 1;
        }
        if (com_google_android_gms_measurement_internal_zzh.zzagj != zzbl.zzhs()) {
            zzbl.zzf(com_google_android_gms_measurement_internal_zzh.zzagj);
        } else {
            obj = obj2;
        }
        if (obj != null) {
            zzjq().zza(zzbl);
        }
        return zzbl;
    }

    public final byte[] zza(zzad com_google_android_gms_measurement_internal_zzad, String str) {
        zzlr();
        zzaf();
        this.zzadj.zzga();
        Preconditions.checkNotNull(com_google_android_gms_measurement_internal_zzad);
        Preconditions.checkNotEmpty(str);
        zzzg com_google_android_gms_internal_measurement_zzgh = new zzgh();
        zzjq().beginTransaction();
        try {
            zzg zzbl = zzjq().zzbl(str);
            byte[] bArr;
            if (zzbl == null) {
                this.zzadj.zzgo().zzjk().zzg("Log and bundle not available. package_name", str);
                bArr = new byte[0];
                return bArr;
            } else if (zzbl.isMeasurementEnabled()) {
                Integer num;
                zzgl com_google_android_gms_internal_measurement_zzgl;
                long j;
                if (("_iap".equals(com_google_android_gms_measurement_internal_zzad.name) || "ecommerce_purchase".equals(com_google_android_gms_measurement_internal_zzad.name)) && !zza(str, com_google_android_gms_measurement_internal_zzad)) {
                    this.zzadj.zzgo().zzjg().zzg("Failed to handle purchase event at single event bundle creation. appId", zzap.zzbv(str));
                }
                boolean zzax = this.zzadj.zzgq().zzax(str);
                Long valueOf = Long.valueOf(0);
                if (zzax && "_e".equals(com_google_android_gms_measurement_internal_zzad.name)) {
                    if (com_google_android_gms_measurement_internal_zzad.zzaid == null || com_google_android_gms_measurement_internal_zzad.zzaid.size() == 0) {
                        this.zzadj.zzgo().zzjg().zzg("The engagement event does not contain any parameters. appId", zzap.zzbv(str));
                    } else if (com_google_android_gms_measurement_internal_zzad.zzaid.getLong("_et") == null) {
                        this.zzadj.zzgo().zzjg().zzg("The engagement event does not include duration. appId", zzap.zzbv(str));
                    } else {
                        valueOf = com_google_android_gms_measurement_internal_zzad.zzaid.getLong("_et");
                    }
                }
                zzgi com_google_android_gms_internal_measurement_zzgi = new zzgi();
                com_google_android_gms_internal_measurement_zzgh.zzawy = new zzgi[]{com_google_android_gms_internal_measurement_zzgi};
                com_google_android_gms_internal_measurement_zzgi.zzaxa = Integer.valueOf(1);
                com_google_android_gms_internal_measurement_zzgi.zzaxi = "android";
                com_google_android_gms_internal_measurement_zzgi.zztt = zzbl.zzal();
                com_google_android_gms_internal_measurement_zzgi.zzage = zzbl.zzhb();
                com_google_android_gms_internal_measurement_zzgi.zzts = zzbl.zzak();
                long zzha = zzbl.zzha();
                if (zzha == -2147483648L) {
                    num = null;
                } else {
                    num = Integer.valueOf((int) zzha);
                }
                com_google_android_gms_internal_measurement_zzgi.zzaxu = num;
                com_google_android_gms_internal_measurement_zzgi.zzaxm = Long.valueOf(zzbl.zzhc());
                com_google_android_gms_internal_measurement_zzgi.zzafx = zzbl.getGmpAppId();
                if (TextUtils.isEmpty(com_google_android_gms_internal_measurement_zzgi.zzafx)) {
                    com_google_android_gms_internal_measurement_zzgi.zzawj = zzbl.zzgw();
                }
                com_google_android_gms_internal_measurement_zzgi.zzaxq = Long.valueOf(zzbl.zzhd());
                if (this.zzadj.isEnabled() && zzn.zzhz() && this.zzadj.zzgq().zzav(com_google_android_gms_internal_measurement_zzgi.zztt)) {
                    com_google_android_gms_internal_measurement_zzgi.zzaya = null;
                }
                Pair zzby = this.zzadj.zzgp().zzby(zzbl.zzal());
                if (!(!zzbl.zzhr() || zzby == null || TextUtils.isEmpty((CharSequence) zzby.first))) {
                    com_google_android_gms_internal_measurement_zzgi.zzaxo = (String) zzby.first;
                    com_google_android_gms_internal_measurement_zzgi.zzaxp = (Boolean) zzby.second;
                }
                this.zzadj.zzgk().zzcl();
                com_google_android_gms_internal_measurement_zzgi.zzaxk = Build.MODEL;
                this.zzadj.zzgk().zzcl();
                com_google_android_gms_internal_measurement_zzgi.zzaxj = VERSION.RELEASE;
                com_google_android_gms_internal_measurement_zzgi.zzaxl = Integer.valueOf((int) this.zzadj.zzgk().zzis());
                com_google_android_gms_internal_measurement_zzgi.zzaia = this.zzadj.zzgk().zzit();
                com_google_android_gms_internal_measurement_zzgi.zzafw = zzbl.getAppInstanceId();
                com_google_android_gms_internal_measurement_zzgi.zzafz = zzbl.getFirebaseInstanceId();
                List zzbk = zzjq().zzbk(zzbl.zzal());
                com_google_android_gms_internal_measurement_zzgi.zzaxc = new zzgl[zzbk.size()];
                zzfj com_google_android_gms_measurement_internal_zzfj = null;
                if (zzax) {
                    zzfj zzi = zzjq().zzi(com_google_android_gms_internal_measurement_zzgi.zztt, "_lte");
                    if (zzi == null || zzi.value == null) {
                        com_google_android_gms_measurement_internal_zzfj = new zzfj(com_google_android_gms_internal_measurement_zzgi.zztt, "auto", "_lte", this.zzadj.zzbx().currentTimeMillis(), valueOf);
                    } else if (valueOf.longValue() > 0) {
                        com_google_android_gms_measurement_internal_zzfj = new zzfj(com_google_android_gms_internal_measurement_zzgi.zztt, "auto", "_lte", this.zzadj.zzbx().currentTimeMillis(), Long.valueOf(((Long) zzi.value).longValue() + valueOf.longValue()));
                    } else {
                        com_google_android_gms_measurement_internal_zzfj = zzi;
                    }
                }
                zzgl com_google_android_gms_internal_measurement_zzgl2 = null;
                int i = 0;
                while (i < zzbk.size()) {
                    zzgl com_google_android_gms_internal_measurement_zzgl3;
                    com_google_android_gms_internal_measurement_zzgl = new zzgl();
                    com_google_android_gms_internal_measurement_zzgi.zzaxc[i] = com_google_android_gms_internal_measurement_zzgl;
                    com_google_android_gms_internal_measurement_zzgl.name = ((zzfj) zzbk.get(i)).name;
                    com_google_android_gms_internal_measurement_zzgl.zzayl = Long.valueOf(((zzfj) zzbk.get(i)).zzaue);
                    zzjo().zza(com_google_android_gms_internal_measurement_zzgl, ((zzfj) zzbk.get(i)).value);
                    if (zzax && "_lte".equals(com_google_android_gms_internal_measurement_zzgl.name)) {
                        com_google_android_gms_internal_measurement_zzgl.zzawx = (Long) com_google_android_gms_measurement_internal_zzfj.value;
                        com_google_android_gms_internal_measurement_zzgl.zzayl = Long.valueOf(this.zzadj.zzbx().currentTimeMillis());
                        com_google_android_gms_internal_measurement_zzgl3 = com_google_android_gms_internal_measurement_zzgl;
                    } else {
                        com_google_android_gms_internal_measurement_zzgl3 = com_google_android_gms_internal_measurement_zzgl2;
                    }
                    i++;
                    com_google_android_gms_internal_measurement_zzgl2 = com_google_android_gms_internal_measurement_zzgl3;
                }
                if (zzax && com_google_android_gms_internal_measurement_zzgl2 == null) {
                    com_google_android_gms_internal_measurement_zzgl = new zzgl();
                    com_google_android_gms_internal_measurement_zzgl.name = "_lte";
                    com_google_android_gms_internal_measurement_zzgl.zzayl = Long.valueOf(this.zzadj.zzbx().currentTimeMillis());
                    com_google_android_gms_internal_measurement_zzgl.zzawx = (Long) com_google_android_gms_measurement_internal_zzfj.value;
                    com_google_android_gms_internal_measurement_zzgi.zzaxc = (zzgl[]) Arrays.copyOf(com_google_android_gms_internal_measurement_zzgi.zzaxc, com_google_android_gms_internal_measurement_zzgi.zzaxc.length + 1);
                    com_google_android_gms_internal_measurement_zzgi.zzaxc[com_google_android_gms_internal_measurement_zzgi.zzaxc.length - 1] = com_google_android_gms_internal_measurement_zzgl;
                }
                if (valueOf.longValue() > 0) {
                    zzjq().zza(com_google_android_gms_measurement_internal_zzfj);
                }
                Bundle zziv = com_google_android_gms_measurement_internal_zzad.zzaid.zziv();
                if ("_iap".equals(com_google_android_gms_measurement_internal_zzad.name)) {
                    zziv.putLong("_c", 1);
                    this.zzadj.zzgo().zzjk().zzbx("Marking in-app purchase as real-time");
                    zziv.putLong("_r", 1);
                }
                zziv.putString("_o", com_google_android_gms_measurement_internal_zzad.origin);
                if (this.zzadj.zzgm().zzcw(com_google_android_gms_internal_measurement_zzgi.zztt)) {
                    this.zzadj.zzgm().zza(zziv, "_dbg", Long.valueOf(1));
                    this.zzadj.zzgm().zza(zziv, "_r", Long.valueOf(1));
                }
                zzz zzg = zzjq().zzg(str, com_google_android_gms_measurement_internal_zzad.name);
                if (zzg == null) {
                    zzjq().zza(new zzz(str, com_google_android_gms_measurement_internal_zzad.name, 1, 0, com_google_android_gms_measurement_internal_zzad.zzaip, 0, null, null, null, null));
                    j = 0;
                } else {
                    j = zzg.zzaig;
                    zzjq().zza(zzg.zzai(com_google_android_gms_measurement_internal_zzad.zzaip).zziu());
                }
                zzy com_google_android_gms_measurement_internal_zzy = new zzy(this.zzadj, com_google_android_gms_measurement_internal_zzad.origin, str, com_google_android_gms_measurement_internal_zzad.name, com_google_android_gms_measurement_internal_zzad.zzaip, j, zziv);
                zzgf com_google_android_gms_internal_measurement_zzgf = new zzgf();
                com_google_android_gms_internal_measurement_zzgi.zzaxb = new zzgf[]{com_google_android_gms_internal_measurement_zzgf};
                com_google_android_gms_internal_measurement_zzgf.zzawu = Long.valueOf(com_google_android_gms_measurement_internal_zzy.timestamp);
                com_google_android_gms_internal_measurement_zzgf.name = com_google_android_gms_measurement_internal_zzy.name;
                com_google_android_gms_internal_measurement_zzgf.zzawv = Long.valueOf(com_google_android_gms_measurement_internal_zzy.zzaic);
                com_google_android_gms_internal_measurement_zzgf.zzawt = new zzgg[com_google_android_gms_measurement_internal_zzy.zzaid.size()];
                Iterator it = com_google_android_gms_measurement_internal_zzy.zzaid.iterator();
                int i2 = 0;
                while (it.hasNext()) {
                    String str2 = (String) it.next();
                    zzgg com_google_android_gms_internal_measurement_zzgg = new zzgg();
                    i = i2 + 1;
                    com_google_android_gms_internal_measurement_zzgf.zzawt[i2] = com_google_android_gms_internal_measurement_zzgg;
                    com_google_android_gms_internal_measurement_zzgg.name = str2;
                    zzjo().zza(com_google_android_gms_internal_measurement_zzgg, com_google_android_gms_measurement_internal_zzy.zzaid.get(str2));
                    i2 = i;
                }
                com_google_android_gms_internal_measurement_zzgi.zzaxt = zza(zzbl.zzal(), com_google_android_gms_internal_measurement_zzgi.zzaxc, com_google_android_gms_internal_measurement_zzgi.zzaxb);
                com_google_android_gms_internal_measurement_zzgi.zzaxe = com_google_android_gms_internal_measurement_zzgf.zzawu;
                com_google_android_gms_internal_measurement_zzgi.zzaxf = com_google_android_gms_internal_measurement_zzgf.zzawu;
                zzha = zzbl.zzgz();
                com_google_android_gms_internal_measurement_zzgi.zzaxh = zzha != 0 ? Long.valueOf(zzha) : null;
                long zzgy = zzbl.zzgy();
                if (zzgy != 0) {
                    zzha = zzgy;
                }
                com_google_android_gms_internal_measurement_zzgi.zzaxg = zzha != 0 ? Long.valueOf(zzha) : null;
                zzbl.zzhh();
                com_google_android_gms_internal_measurement_zzgi.zzaxr = Integer.valueOf((int) zzbl.zzhe());
                com_google_android_gms_internal_measurement_zzgi.zzaxn = Long.valueOf(this.zzadj.zzgq().zzhc());
                com_google_android_gms_internal_measurement_zzgi.zzaxd = Long.valueOf(this.zzadj.zzbx().currentTimeMillis());
                com_google_android_gms_internal_measurement_zzgi.zzaxs = Boolean.TRUE;
                zzbl.zzs(com_google_android_gms_internal_measurement_zzgi.zzaxe.longValue());
                zzbl.zzt(com_google_android_gms_internal_measurement_zzgi.zzaxf.longValue());
                zzjq().zza(zzbl);
                zzjq().setTransactionSuccessful();
                zzjq().endTransaction();
                try {
                    bArr = new byte[com_google_android_gms_internal_measurement_zzgh.zzvu()];
                    zzyy zzk = zzyy.zzk(bArr, 0, bArr.length);
                    com_google_android_gms_internal_measurement_zzgh.zza(zzk);
                    zzk.zzyt();
                    return zzjo().zzb(bArr);
                } catch (IOException e) {
                    this.zzadj.zzgo().zzjd().zze("Data loss. Failed to bundle and serialize. appId", zzap.zzbv(str), e);
                    return null;
                }
            } else {
                this.zzadj.zzgo().zzjk().zzg("Log and bundle disabled. package_name", str);
                bArr = new byte[0];
                zzjq().endTransaction();
                return bArr;
            }
        } finally {
            zzjq().endTransaction();
        }
    }

    final String zzh(zzh com_google_android_gms_measurement_internal_zzh) {
        Object e;
        try {
            return (String) this.zzadj.zzgn().zzb(new zzfe(this, com_google_android_gms_measurement_internal_zzh)).get(30000, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e2) {
            e = e2;
        } catch (InterruptedException e3) {
            e = e3;
        } catch (ExecutionException e4) {
            e = e4;
        }
        this.zzadj.zzgo().zzjd().zze("Failed to get app instance id. appId", zzap.zzbv(com_google_android_gms_measurement_internal_zzh.packageName), e);
        return null;
    }

    final void zzo(boolean z) {
        zzlv();
    }
}
