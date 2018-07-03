package com.google.android.gms.internal.measurement;

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

public class zzjr implements zzec {
    private zzgl zzacw;
    zzgf zzaqa;
    zzfk zzaqb;
    private zzei zzaqc;
    private zzfp zzaqd;
    private zzjn zzaqe;
    private zzeb zzaqf;
    private boolean zzaqg;
    private long zzaqh;
    private List<Runnable> zzaqi;
    private int zzaqj;
    private int zzaqk;
    private boolean zzaql;
    private boolean zzaqm;
    private boolean zzaqn;
    private FileLock zzaqo;
    private FileChannel zzaqp;
    private List<Long> zzaqq;
    private List<Long> zzaqr;
    long zzaqs;
    private boolean zzvo = false;

    private final int zza(FileChannel fileChannel) {
        int i = 0;
        zzab();
        if (fileChannel == null || !fileChannel.isOpen()) {
            zzge().zzim().log("Bad channel to read from");
        } else {
            ByteBuffer allocate = ByteBuffer.allocate(4);
            try {
                fileChannel.position(0);
                int read = fileChannel.read(allocate);
                if (read == 4) {
                    allocate.flip();
                    i = allocate.getInt();
                } else if (read != -1) {
                    zzge().zzip().zzg("Unexpected data length. Bytes read", Integer.valueOf(read));
                }
            } catch (IOException e) {
                zzge().zzim().zzg("Failed to read from channel", e);
            }
        }
        return i;
    }

    private final zzdz zza(Context context, String str, String str2, boolean z, boolean z2, boolean z3, long j) {
        Object charSequence;
        String str3 = "Unknown";
        String str4 = "Unknown";
        int i = Integer.MIN_VALUE;
        String str5 = "Unknown";
        PackageManager packageManager = context.getPackageManager();
        if (packageManager == null) {
            zzge().zzim().log("PackageManager is null, can not log app install information");
            return null;
        }
        try {
            str3 = packageManager.getInstallerPackageName(str);
        } catch (IllegalArgumentException e) {
            zzge().zzim().zzg("Error retrieving installer package name. appId", zzfg.zzbm(str));
        }
        if (str3 == null) {
            str3 = "manual_install";
        } else if ("com.android.vending".equals(str3)) {
            str3 = TtmlNode.ANONYMOUS_REGION_ID;
        }
        try {
            PackageInfo packageInfo = Wrappers.packageManager(context).getPackageInfo(str, 0);
            if (packageInfo != null) {
                CharSequence applicationLabel = Wrappers.packageManager(context).getApplicationLabel(str);
                if (TextUtils.isEmpty(applicationLabel)) {
                    String str6 = str5;
                } else {
                    charSequence = applicationLabel.toString();
                }
                try {
                    str4 = packageInfo.versionName;
                    i = packageInfo.versionCode;
                } catch (NameNotFoundException e2) {
                    zzge().zzim().zze("Error retrieving newly installed package info. appId, appName", zzfg.zzbm(str), charSequence);
                    return null;
                }
            }
            long j2 = 0;
            if (zzgg().zzba(str)) {
                j2 = j;
            }
            return new zzdz(str, str2, str4, (long) i, str3, 12451, zzgb().zzd(context, str), null, z, false, TtmlNode.ANONYMOUS_REGION_ID, 0, j2, 0, z2, z3, false);
        } catch (NameNotFoundException e3) {
            charSequence = str5;
            zzge().zzim().zze("Error retrieving newly installed package info. appId, appName", zzfg.zzbm(str), charSequence);
            return null;
        }
    }

    private static void zza(zzjq com_google_android_gms_internal_measurement_zzjq) {
        if (com_google_android_gms_internal_measurement_zzjq == null) {
            throw new IllegalStateException("Upload component not created");
        } else if (!com_google_android_gms_internal_measurement_zzjq.isInitialized()) {
            String valueOf = String.valueOf(com_google_android_gms_internal_measurement_zzjq.getClass());
            throw new IllegalStateException(new StringBuilder(String.valueOf(valueOf).length() + 27).append("Component not initialized: ").append(valueOf).toString());
        }
    }

    private final boolean zza(int i, FileChannel fileChannel) {
        zzab();
        if (fileChannel == null || !fileChannel.isOpen()) {
            zzge().zzim().log("Bad channel to read from");
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
            zzge().zzim().zzg("Error writing to channel. Bytes written", Long.valueOf(fileChannel.size()));
            return true;
        } catch (IOException e) {
            zzge().zzim().zzg("Failed to write to channel", e);
            return false;
        }
    }

    private final boolean zza(String str, zzeu com_google_android_gms_internal_measurement_zzeu) {
        long round;
        Object string = com_google_android_gms_internal_measurement_zzeu.zzafq.getString("currency");
        if ("ecommerce_purchase".equals(com_google_android_gms_internal_measurement_zzeu.name)) {
            double doubleValue = com_google_android_gms_internal_measurement_zzeu.zzafq.zzbh("value").doubleValue() * 1000000.0d;
            if (doubleValue == 0.0d) {
                doubleValue = ((double) com_google_android_gms_internal_measurement_zzeu.zzafq.getLong("value").longValue()) * 1000000.0d;
            }
            if (doubleValue > 9.223372036854776E18d || doubleValue < -9.223372036854776E18d) {
                zzge().zzip().zze("Data lost. Currency value is too big. appId", zzfg.zzbm(str), Double.valueOf(doubleValue));
                return false;
            }
            round = Math.round(doubleValue);
        } else {
            round = com_google_android_gms_internal_measurement_zzeu.zzafq.getLong("value").longValue();
        }
        if (!TextUtils.isEmpty(string)) {
            String toUpperCase = string.toUpperCase(Locale.US);
            if (toUpperCase.matches("[A-Z]{3}")) {
                String valueOf = String.valueOf("_ltv_");
                toUpperCase = String.valueOf(toUpperCase);
                String concat = toUpperCase.length() != 0 ? valueOf.concat(toUpperCase) : new String(valueOf);
                zzjz zzh = zzix().zzh(str, concat);
                if (zzh == null || !(zzh.value instanceof Long)) {
                    zzhg zzix = zzix();
                    int zzb = zzgg().zzb(str, zzew.zzahm) - 1;
                    Preconditions.checkNotEmpty(str);
                    zzix.zzab();
                    zzix.zzch();
                    try {
                        zzix.getWritableDatabase().execSQL("delete from user_attributes where app_id=? and name in (select name from user_attributes where app_id=? and name like '_ltv_%' order by set_timestamp desc limit ?,10);", new String[]{str, str, String.valueOf(zzb)});
                    } catch (SQLiteException e) {
                        zzix.zzge().zzim().zze("Error pruning currencies. appId", zzfg.zzbm(str), e);
                    }
                    zzh = new zzjz(str, com_google_android_gms_internal_measurement_zzeu.origin, concat, zzbt().currentTimeMillis(), Long.valueOf(round));
                } else {
                    zzh = new zzjz(str, com_google_android_gms_internal_measurement_zzeu.origin, concat, zzbt().currentTimeMillis(), Long.valueOf(round + ((Long) zzh.value).longValue()));
                }
                if (!zzix().zza(zzh)) {
                    zzge().zzim().zzd("Too many unique user properties are set. Ignoring user property. appId", zzfg.zzbm(str), zzga().zzbl(zzh.name), zzh.value);
                    zzgb().zza(str, 9, null, null, 0);
                }
            }
        }
        return true;
    }

    private final zzkm[] zza(String str, zzks[] com_google_android_gms_internal_measurement_zzksArr, zzkn[] com_google_android_gms_internal_measurement_zzknArr) {
        Preconditions.checkNotEmpty(str);
        return zziw().zza(str, com_google_android_gms_internal_measurement_zzknArr, com_google_android_gms_internal_measurement_zzksArr);
    }

    private final void zzb(zzdy com_google_android_gms_internal_measurement_zzdy) {
        zzab();
        if (TextUtils.isEmpty(com_google_android_gms_internal_measurement_zzdy.getGmpAppId())) {
            zzb(com_google_android_gms_internal_measurement_zzdy.zzah(), 204, null, null, null);
            return;
        }
        String gmpAppId = com_google_android_gms_internal_measurement_zzdy.getGmpAppId();
        String appInstanceId = com_google_android_gms_internal_measurement_zzdy.getAppInstanceId();
        Builder builder = new Builder();
        Builder encodedAuthority = builder.scheme((String) zzew.zzagm.get()).encodedAuthority((String) zzew.zzagn.get());
        String str = "config/app/";
        String valueOf = String.valueOf(gmpAppId);
        encodedAuthority.path(valueOf.length() != 0 ? str.concat(valueOf) : new String(str)).appendQueryParameter("app_instance_id", appInstanceId).appendQueryParameter("platform", "android").appendQueryParameter("gmp_version", "12451");
        String uri = builder.build().toString();
        try {
            Map map;
            URL url = new URL(uri);
            zzge().zzit().zzg("Fetching remote configuration", com_google_android_gms_internal_measurement_zzdy.zzah());
            zzkk zzbu = zzkm().zzbu(com_google_android_gms_internal_measurement_zzdy.zzah());
            CharSequence zzbv = zzkm().zzbv(com_google_android_gms_internal_measurement_zzdy.zzah());
            if (zzbu == null || TextUtils.isEmpty(zzbv)) {
                map = null;
            } else {
                Map arrayMap = new ArrayMap();
                arrayMap.put("If-Modified-Since", zzbv);
                map = arrayMap;
            }
            this.zzaql = true;
            zzhg zzkn = zzkn();
            appInstanceId = com_google_android_gms_internal_measurement_zzdy.zzah();
            zzfm com_google_android_gms_internal_measurement_zzjt = new zzjt(this);
            zzkn.zzab();
            zzkn.zzch();
            Preconditions.checkNotNull(url);
            Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzjt);
            zzkn.zzgd().zzd(new zzfo(zzkn, appInstanceId, url, null, map, com_google_android_gms_internal_measurement_zzjt));
        } catch (MalformedURLException e) {
            zzge().zzim().zze("Failed to parse config URL. Not fetching. appId", zzfg.zzbm(com_google_android_gms_internal_measurement_zzdy.zzah()), uri);
        }
    }

    private final Boolean zzc(zzdy com_google_android_gms_internal_measurement_zzdy) {
        try {
            if (com_google_android_gms_internal_measurement_zzdy.zzgm() != -2147483648L) {
                if (com_google_android_gms_internal_measurement_zzdy.zzgm() == ((long) Wrappers.packageManager(getContext()).getPackageInfo(com_google_android_gms_internal_measurement_zzdy.zzah(), 0).versionCode)) {
                    return Boolean.valueOf(true);
                }
            }
            String str = Wrappers.packageManager(getContext()).getPackageInfo(com_google_android_gms_internal_measurement_zzdy.zzah(), 0).versionName;
            if (com_google_android_gms_internal_measurement_zzdy.zzag() != null && com_google_android_gms_internal_measurement_zzdy.zzag().equals(str)) {
                return Boolean.valueOf(true);
            }
            return Boolean.valueOf(false);
        } catch (NameNotFoundException e) {
            return null;
        }
    }

    private final void zzc(zzeu com_google_android_gms_internal_measurement_zzeu, zzdz com_google_android_gms_internal_measurement_zzdz) {
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzdz);
        Preconditions.checkNotEmpty(com_google_android_gms_internal_measurement_zzdz.packageName);
        long nanoTime = System.nanoTime();
        zzab();
        zzkq();
        String str = com_google_android_gms_internal_measurement_zzdz.packageName;
        zzgb();
        if (!zzka.zzd(com_google_android_gms_internal_measurement_zzeu, com_google_android_gms_internal_measurement_zzdz)) {
            return;
        }
        if (!com_google_android_gms_internal_measurement_zzdz.zzadw) {
            zzg(com_google_android_gms_internal_measurement_zzdz);
        } else if (zzkm().zzn(str, com_google_android_gms_internal_measurement_zzeu.name)) {
            zzge().zzip().zze("Dropping blacklisted event. appId", zzfg.zzbm(str), zzga().zzbj(com_google_android_gms_internal_measurement_zzeu.name));
            Object obj = (zzkm().zzby(str) || zzkm().zzbz(str)) ? 1 : null;
            if (obj == null && !"_err".equals(com_google_android_gms_internal_measurement_zzeu.name)) {
                zzgb().zza(str, 11, "_ev", com_google_android_gms_internal_measurement_zzeu.name, 0);
            }
            if (obj != null) {
                zzdy zzbc = zzix().zzbc(str);
                if (zzbc != null) {
                    if (Math.abs(zzbt().currentTimeMillis() - Math.max(zzbc.zzgs(), zzbc.zzgr())) > ((Long) zzew.zzahh.get()).longValue()) {
                        zzge().zzis().log("Fetching config for blacklisted app");
                        zzb(zzbc);
                    }
                }
            }
        } else {
            if (zzge().isLoggable(2)) {
                zzge().zzit().zzg("Logging event", zzga().zzb(com_google_android_gms_internal_measurement_zzeu));
            }
            zzix().beginTransaction();
            zzg(com_google_android_gms_internal_measurement_zzdz);
            if (("_iap".equals(com_google_android_gms_internal_measurement_zzeu.name) || "ecommerce_purchase".equals(com_google_android_gms_internal_measurement_zzeu.name)) && !zza(str, com_google_android_gms_internal_measurement_zzeu)) {
                zzix().setTransactionSuccessful();
                zzix().endTransaction();
                return;
            }
            zzkq com_google_android_gms_internal_measurement_zzkq;
            try {
                boolean zzcc = zzka.zzcc(com_google_android_gms_internal_measurement_zzeu.name);
                boolean equals = "_err".equals(com_google_android_gms_internal_measurement_zzeu.name);
                zzej zza = zzix().zza(zzkr(), str, true, zzcc, false, equals, false);
                long intValue = zza.zzafe - ((long) ((Integer) zzew.zzags.get()).intValue());
                if (intValue > 0) {
                    if (intValue % 1000 == 1) {
                        zzge().zzim().zze("Data loss. Too many events logged. appId, count", zzfg.zzbm(str), Long.valueOf(zza.zzafe));
                    }
                    zzix().setTransactionSuccessful();
                    zzix().endTransaction();
                    return;
                }
                zzeq zzac;
                zzep com_google_android_gms_internal_measurement_zzep;
                boolean z;
                if (zzcc) {
                    intValue = zza.zzafd - ((long) ((Integer) zzew.zzagu.get()).intValue());
                    if (intValue > 0) {
                        if (intValue % 1000 == 1) {
                            zzge().zzim().zze("Data loss. Too many public events logged. appId, count", zzfg.zzbm(str), Long.valueOf(zza.zzafd));
                        }
                        zzgb().zza(str, 16, "_ev", com_google_android_gms_internal_measurement_zzeu.name, 0);
                        zzix().setTransactionSuccessful();
                        zzix().endTransaction();
                        return;
                    }
                }
                if (equals) {
                    intValue = zza.zzafg - ((long) Math.max(0, Math.min(1000000, zzgg().zzb(com_google_android_gms_internal_measurement_zzdz.packageName, zzew.zzagt))));
                    if (intValue > 0) {
                        if (intValue == 1) {
                            zzge().zzim().zze("Too many error events logged. appId, count", zzfg.zzbm(str), Long.valueOf(zza.zzafg));
                        }
                        zzix().setTransactionSuccessful();
                        zzix().endTransaction();
                        return;
                    }
                }
                Bundle zzif = com_google_android_gms_internal_measurement_zzeu.zzafq.zzif();
                zzgb().zza(zzif, "_o", com_google_android_gms_internal_measurement_zzeu.origin);
                if (zzgb().zzcj(str)) {
                    zzgb().zza(zzif, "_dbg", Long.valueOf(1));
                    zzgb().zza(zzif, "_r", Long.valueOf(1));
                }
                long zzbd = zzix().zzbd(str);
                if (zzbd > 0) {
                    zzge().zzip().zze("Data lost. Too many events stored on disk, deleted. appId", zzfg.zzbm(str), Long.valueOf(zzbd));
                }
                zzep com_google_android_gms_internal_measurement_zzep2 = new zzep(this.zzacw, com_google_android_gms_internal_measurement_zzeu.origin, str, com_google_android_gms_internal_measurement_zzeu.name, com_google_android_gms_internal_measurement_zzeu.zzagb, 0, zzif);
                zzeq zzf = zzix().zzf(str, com_google_android_gms_internal_measurement_zzep2.name);
                if (zzf != null) {
                    com_google_android_gms_internal_measurement_zzep2 = com_google_android_gms_internal_measurement_zzep2.zza(this.zzacw, zzf.zzaft);
                    zzac = zzf.zzac(com_google_android_gms_internal_measurement_zzep2.timestamp);
                    com_google_android_gms_internal_measurement_zzep = com_google_android_gms_internal_measurement_zzep2;
                } else if (zzix().zzbg(str) < 500 || !zzcc) {
                    zzac = new zzeq(str, com_google_android_gms_internal_measurement_zzep2.name, 0, 0, com_google_android_gms_internal_measurement_zzep2.timestamp, 0, null, null, null);
                    com_google_android_gms_internal_measurement_zzep = com_google_android_gms_internal_measurement_zzep2;
                } else {
                    zzge().zzim().zzd("Too many event names used, ignoring event. appId, name, supported count", zzfg.zzbm(str), zzga().zzbj(com_google_android_gms_internal_measurement_zzep2.name), Integer.valueOf(500));
                    zzgb().zza(str, 8, null, null, 0);
                    zzix().endTransaction();
                    return;
                }
                zzix().zza(zzac);
                zzab();
                zzkq();
                Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzep);
                Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzdz);
                Preconditions.checkNotEmpty(com_google_android_gms_internal_measurement_zzep.zzti);
                Preconditions.checkArgument(com_google_android_gms_internal_measurement_zzep.zzti.equals(com_google_android_gms_internal_measurement_zzdz.packageName));
                com_google_android_gms_internal_measurement_zzkq = new zzkq();
                com_google_android_gms_internal_measurement_zzkq.zzath = Integer.valueOf(1);
                com_google_android_gms_internal_measurement_zzkq.zzatp = "android";
                com_google_android_gms_internal_measurement_zzkq.zzti = com_google_android_gms_internal_measurement_zzdz.packageName;
                com_google_android_gms_internal_measurement_zzkq.zzadt = com_google_android_gms_internal_measurement_zzdz.zzadt;
                com_google_android_gms_internal_measurement_zzkq.zzth = com_google_android_gms_internal_measurement_zzdz.zzth;
                com_google_android_gms_internal_measurement_zzkq.zzaub = com_google_android_gms_internal_measurement_zzdz.zzads == -2147483648L ? null : Integer.valueOf((int) com_google_android_gms_internal_measurement_zzdz.zzads);
                com_google_android_gms_internal_measurement_zzkq.zzatt = Long.valueOf(com_google_android_gms_internal_measurement_zzdz.zzadu);
                com_google_android_gms_internal_measurement_zzkq.zzadm = com_google_android_gms_internal_measurement_zzdz.zzadm;
                com_google_android_gms_internal_measurement_zzkq.zzatx = com_google_android_gms_internal_measurement_zzdz.zzadv == 0 ? null : Long.valueOf(com_google_android_gms_internal_measurement_zzdz.zzadv);
                Pair zzbo = zzgf().zzbo(com_google_android_gms_internal_measurement_zzdz.packageName);
                if (zzbo == null || TextUtils.isEmpty((CharSequence) zzbo.first)) {
                    if (!zzfw().zzf(getContext()) && com_google_android_gms_internal_measurement_zzdz.zzadz) {
                        String string = Secure.getString(getContext().getContentResolver(), "android_id");
                        if (string == null) {
                            zzge().zzip().zzg("null secure ID. appId", zzfg.zzbm(com_google_android_gms_internal_measurement_zzkq.zzti));
                            string = "null";
                        } else if (string.isEmpty()) {
                            zzge().zzip().zzg("empty secure ID. appId", zzfg.zzbm(com_google_android_gms_internal_measurement_zzkq.zzti));
                        }
                        com_google_android_gms_internal_measurement_zzkq.zzaue = string;
                    }
                } else if (com_google_android_gms_internal_measurement_zzdz.zzady) {
                    com_google_android_gms_internal_measurement_zzkq.zzatv = (String) zzbo.first;
                    com_google_android_gms_internal_measurement_zzkq.zzatw = (Boolean) zzbo.second;
                }
                zzfw().zzch();
                com_google_android_gms_internal_measurement_zzkq.zzatr = Build.MODEL;
                zzfw().zzch();
                com_google_android_gms_internal_measurement_zzkq.zzatq = VERSION.RELEASE;
                com_google_android_gms_internal_measurement_zzkq.zzats = Integer.valueOf((int) zzfw().zzic());
                com_google_android_gms_internal_measurement_zzkq.zzafn = zzfw().zzid();
                com_google_android_gms_internal_measurement_zzkq.zzatu = null;
                com_google_android_gms_internal_measurement_zzkq.zzatk = null;
                com_google_android_gms_internal_measurement_zzkq.zzatl = null;
                com_google_android_gms_internal_measurement_zzkq.zzatm = null;
                com_google_android_gms_internal_measurement_zzkq.zzaug = Long.valueOf(com_google_android_gms_internal_measurement_zzdz.zzadx);
                if (this.zzacw.isEnabled() && zzef.zzhk()) {
                    com_google_android_gms_internal_measurement_zzkq.zzauh = null;
                }
                zzdy zzbc2 = zzix().zzbc(com_google_android_gms_internal_measurement_zzdz.packageName);
                if (zzbc2 == null) {
                    zzbc2 = new zzdy(this.zzacw, com_google_android_gms_internal_measurement_zzdz.packageName);
                    zzbc2.zzal(this.zzacw.zzfv().zzii());
                    zzbc2.zzao(com_google_android_gms_internal_measurement_zzdz.zzado);
                    zzbc2.zzam(com_google_android_gms_internal_measurement_zzdz.zzadm);
                    zzbc2.zzan(zzgf().zzbp(com_google_android_gms_internal_measurement_zzdz.packageName));
                    zzbc2.zzr(0);
                    zzbc2.zzm(0);
                    zzbc2.zzn(0);
                    zzbc2.setAppVersion(com_google_android_gms_internal_measurement_zzdz.zzth);
                    zzbc2.zzo(com_google_android_gms_internal_measurement_zzdz.zzads);
                    zzbc2.zzap(com_google_android_gms_internal_measurement_zzdz.zzadt);
                    zzbc2.zzp(com_google_android_gms_internal_measurement_zzdz.zzadu);
                    zzbc2.zzq(com_google_android_gms_internal_measurement_zzdz.zzadv);
                    zzbc2.setMeasurementEnabled(com_google_android_gms_internal_measurement_zzdz.zzadw);
                    zzbc2.zzaa(com_google_android_gms_internal_measurement_zzdz.zzadx);
                    zzix().zza(zzbc2);
                }
                com_google_android_gms_internal_measurement_zzkq.zzadl = zzbc2.getAppInstanceId();
                com_google_android_gms_internal_measurement_zzkq.zzado = zzbc2.zzgj();
                List zzbb = zzix().zzbb(com_google_android_gms_internal_measurement_zzdz.packageName);
                com_google_android_gms_internal_measurement_zzkq.zzatj = new zzks[zzbb.size()];
                for (int i = 0; i < zzbb.size(); i++) {
                    zzks com_google_android_gms_internal_measurement_zzks = new zzks();
                    com_google_android_gms_internal_measurement_zzkq.zzatj[i] = com_google_android_gms_internal_measurement_zzks;
                    com_google_android_gms_internal_measurement_zzks.name = ((zzjz) zzbb.get(i)).name;
                    com_google_android_gms_internal_measurement_zzks.zzaun = Long.valueOf(((zzjz) zzbb.get(i)).zzaqz);
                    zzgb().zza(com_google_android_gms_internal_measurement_zzks, ((zzjz) zzbb.get(i)).value);
                }
                long zza2 = zzix().zza(com_google_android_gms_internal_measurement_zzkq);
                zzei zzix = zzix();
                if (com_google_android_gms_internal_measurement_zzep.zzafq != null) {
                    Iterator it = com_google_android_gms_internal_measurement_zzep.zzafq.iterator();
                    while (it.hasNext()) {
                        if ("_r".equals((String) it.next())) {
                            z = true;
                            break;
                        }
                    }
                    z = zzkm().zzo(com_google_android_gms_internal_measurement_zzep.zzti, com_google_android_gms_internal_measurement_zzep.name);
                    zzej zza3 = zzix().zza(zzkr(), com_google_android_gms_internal_measurement_zzep.zzti, false, false, false, false, false);
                    if (z && zza3.zzafh < ((long) zzgg().zzar(com_google_android_gms_internal_measurement_zzep.zzti))) {
                        z = true;
                        if (zzix.zza(com_google_android_gms_internal_measurement_zzep, zza2, z)) {
                            this.zzaqh = 0;
                        }
                        zzix().setTransactionSuccessful();
                        if (zzge().isLoggable(2)) {
                            zzge().zzit().zzg("Event recorded", zzga().zza(com_google_android_gms_internal_measurement_zzep));
                        }
                        zzix().endTransaction();
                        zzku();
                        zzge().zzit().zzg("Background event processing time, ms", Long.valueOf(((System.nanoTime() - nanoTime) + 500000) / 1000000));
                    }
                }
                z = false;
                if (zzix.zza(com_google_android_gms_internal_measurement_zzep, zza2, z)) {
                    this.zzaqh = 0;
                }
                zzix().setTransactionSuccessful();
                if (zzge().isLoggable(2)) {
                    zzge().zzit().zzg("Event recorded", zzga().zza(com_google_android_gms_internal_measurement_zzep));
                }
                zzix().endTransaction();
                zzku();
                zzge().zzit().zzg("Background event processing time, ms", Long.valueOf(((System.nanoTime() - nanoTime) + 500000) / 1000000));
            } catch (IOException e) {
                zzge().zzim().zze("Data loss. Failed to insert raw event metadata. appId", zzfg.zzbm(com_google_android_gms_internal_measurement_zzkq.zzti), e);
            } catch (Throwable th) {
                zzix().endTransaction();
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final boolean zzd(java.lang.String r31, long r32) {
        /*
        r30 = this;
        r2 = r30.zzix();
        r2.beginTransaction();
        r21 = new com.google.android.gms.internal.measurement.zzjv;	 Catch:{ all -> 0x01c5 }
        r2 = 0;
        r0 = r21;
        r1 = r30;
        r0.<init>(r1);	 Catch:{ all -> 0x01c5 }
        r14 = r30.zzix();	 Catch:{ all -> 0x01c5 }
        r4 = 0;
        r0 = r30;
        r0 = r0.zzaqs;	 Catch:{ all -> 0x01c5 }
        r16 = r0;
        com.google.android.gms.common.internal.Preconditions.checkNotNull(r21);	 Catch:{ all -> 0x01c5 }
        r14.zzab();	 Catch:{ all -> 0x01c5 }
        r14.zzch();	 Catch:{ all -> 0x01c5 }
        r3 = 0;
        r2 = r14.getWritableDatabase();	 Catch:{ SQLiteException -> 0x0c02 }
        r5 = 0;
        r5 = android.text.TextUtils.isEmpty(r5);	 Catch:{ SQLiteException -> 0x0c02 }
        if (r5 == 0) goto L_0x01ce;
    L_0x0031:
        r6 = -1;
        r5 = (r16 > r6 ? 1 : (r16 == r6 ? 0 : -1));
        if (r5 == 0) goto L_0x0160;
    L_0x0037:
        r5 = 2;
        r5 = new java.lang.String[r5];	 Catch:{ SQLiteException -> 0x0c02 }
        r6 = 0;
        r7 = java.lang.String.valueOf(r16);	 Catch:{ SQLiteException -> 0x0c02 }
        r5[r6] = r7;	 Catch:{ SQLiteException -> 0x0c02 }
        r6 = 1;
        r7 = java.lang.String.valueOf(r32);	 Catch:{ SQLiteException -> 0x0c02 }
        r5[r6] = r7;	 Catch:{ SQLiteException -> 0x0c02 }
        r6 = r5;
    L_0x0049:
        r8 = -1;
        r5 = (r16 > r8 ? 1 : (r16 == r8 ? 0 : -1));
        if (r5 == 0) goto L_0x016d;
    L_0x004f:
        r5 = "rowid <= ? and ";
    L_0x0052:
        r7 = java.lang.String.valueOf(r5);	 Catch:{ SQLiteException -> 0x0c02 }
        r7 = r7.length();	 Catch:{ SQLiteException -> 0x0c02 }
        r7 = r7 + 148;
        r8 = new java.lang.StringBuilder;	 Catch:{ SQLiteException -> 0x0c02 }
        r8.<init>(r7);	 Catch:{ SQLiteException -> 0x0c02 }
        r7 = "select app_id, metadata_fingerprint from raw_events where ";
        r7 = r8.append(r7);	 Catch:{ SQLiteException -> 0x0c02 }
        r5 = r7.append(r5);	 Catch:{ SQLiteException -> 0x0c02 }
        r7 = "app_id in (select app_id from apps where config_fetched_time >= ?) order by rowid limit 1;";
        r5 = r5.append(r7);	 Catch:{ SQLiteException -> 0x0c02 }
        r5 = r5.toString();	 Catch:{ SQLiteException -> 0x0c02 }
        r3 = r2.rawQuery(r5, r6);	 Catch:{ SQLiteException -> 0x0c02 }
        r5 = r3.moveToFirst();	 Catch:{ SQLiteException -> 0x0c02 }
        if (r5 != 0) goto L_0x0172;
    L_0x0081:
        if (r3 == 0) goto L_0x0086;
    L_0x0083:
        r3.close();	 Catch:{ all -> 0x01c5 }
    L_0x0086:
        r0 = r21;
        r2 = r0.zzaqx;	 Catch:{ all -> 0x01c5 }
        if (r2 == 0) goto L_0x0096;
    L_0x008c:
        r0 = r21;
        r2 = r0.zzaqx;	 Catch:{ all -> 0x01c5 }
        r2 = r2.isEmpty();	 Catch:{ all -> 0x01c5 }
        if (r2 == 0) goto L_0x0372;
    L_0x0096:
        r2 = 1;
    L_0x0097:
        if (r2 != 0) goto L_0x0bee;
    L_0x0099:
        r17 = 0;
        r0 = r21;
        r0 = r0.zzaqv;	 Catch:{ all -> 0x01c5 }
        r22 = r0;
        r0 = r21;
        r2 = r0.zzaqx;	 Catch:{ all -> 0x01c5 }
        r2 = r2.size();	 Catch:{ all -> 0x01c5 }
        r2 = new com.google.android.gms.internal.measurement.zzkn[r2];	 Catch:{ all -> 0x01c5 }
        r0 = r22;
        r0.zzati = r2;	 Catch:{ all -> 0x01c5 }
        r13 = 0;
        r14 = 0;
        r2 = r30.zzgg();	 Catch:{ all -> 0x01c5 }
        r0 = r22;
        r3 = r0.zzti;	 Catch:{ all -> 0x01c5 }
        r18 = r2.zzav(r3);	 Catch:{ all -> 0x01c5 }
        r2 = 0;
        r16 = r2;
    L_0x00c1:
        r0 = r21;
        r2 = r0.zzaqx;	 Catch:{ all -> 0x01c5 }
        r2 = r2.size();	 Catch:{ all -> 0x01c5 }
        r0 = r16;
        if (r0 >= r2) goto L_0x05e0;
    L_0x00cd:
        r0 = r21;
        r2 = r0.zzaqx;	 Catch:{ all -> 0x01c5 }
        r0 = r16;
        r2 = r2.get(r0);	 Catch:{ all -> 0x01c5 }
        r0 = r2;
        r0 = (com.google.android.gms.internal.measurement.zzkn) r0;	 Catch:{ all -> 0x01c5 }
        r12 = r0;
        r2 = r30.zzkm();	 Catch:{ all -> 0x01c5 }
        r0 = r21;
        r3 = r0.zzaqv;	 Catch:{ all -> 0x01c5 }
        r3 = r3.zzti;	 Catch:{ all -> 0x01c5 }
        r4 = r12.name;	 Catch:{ all -> 0x01c5 }
        r2 = r2.zzn(r3, r4);	 Catch:{ all -> 0x01c5 }
        if (r2 == 0) goto L_0x0378;
    L_0x00ed:
        r2 = r30.zzge();	 Catch:{ all -> 0x01c5 }
        r2 = r2.zzip();	 Catch:{ all -> 0x01c5 }
        r3 = "Dropping blacklisted raw event. appId";
        r0 = r21;
        r4 = r0.zzaqv;	 Catch:{ all -> 0x01c5 }
        r4 = r4.zzti;	 Catch:{ all -> 0x01c5 }
        r4 = com.google.android.gms.internal.measurement.zzfg.zzbm(r4);	 Catch:{ all -> 0x01c5 }
        r5 = r30.zzga();	 Catch:{ all -> 0x01c5 }
        r6 = r12.name;	 Catch:{ all -> 0x01c5 }
        r5 = r5.zzbj(r6);	 Catch:{ all -> 0x01c5 }
        r2.zze(r3, r4, r5);	 Catch:{ all -> 0x01c5 }
        r2 = r30.zzkm();	 Catch:{ all -> 0x01c5 }
        r0 = r21;
        r3 = r0.zzaqv;	 Catch:{ all -> 0x01c5 }
        r3 = r3.zzti;	 Catch:{ all -> 0x01c5 }
        r2 = r2.zzby(r3);	 Catch:{ all -> 0x01c5 }
        if (r2 != 0) goto L_0x012f;
    L_0x011f:
        r2 = r30.zzkm();	 Catch:{ all -> 0x01c5 }
        r0 = r21;
        r3 = r0.zzaqv;	 Catch:{ all -> 0x01c5 }
        r3 = r3.zzti;	 Catch:{ all -> 0x01c5 }
        r2 = r2.zzbz(r3);	 Catch:{ all -> 0x01c5 }
        if (r2 == 0) goto L_0x0375;
    L_0x012f:
        r2 = 1;
    L_0x0130:
        if (r2 != 0) goto L_0x0c20;
    L_0x0132:
        r2 = "_err";
        r3 = r12.name;	 Catch:{ all -> 0x01c5 }
        r2 = r2.equals(r3);	 Catch:{ all -> 0x01c5 }
        if (r2 != 0) goto L_0x0c20;
    L_0x013d:
        r2 = r30.zzgb();	 Catch:{ all -> 0x01c5 }
        r0 = r21;
        r3 = r0.zzaqv;	 Catch:{ all -> 0x01c5 }
        r3 = r3.zzti;	 Catch:{ all -> 0x01c5 }
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
        r5 = new java.lang.String[r5];	 Catch:{ SQLiteException -> 0x0c02 }
        r6 = 0;
        r7 = java.lang.String.valueOf(r32);	 Catch:{ SQLiteException -> 0x0c02 }
        r5[r6] = r7;	 Catch:{ SQLiteException -> 0x0c02 }
        r6 = r5;
        goto L_0x0049;
    L_0x016d:
        r5 = "";
        goto L_0x0052;
    L_0x0172:
        r5 = 0;
        r4 = r3.getString(r5);	 Catch:{ SQLiteException -> 0x0c02 }
        r5 = 1;
        r5 = r3.getString(r5);	 Catch:{ SQLiteException -> 0x0c02 }
        r3.close();	 Catch:{ SQLiteException -> 0x0c02 }
        r13 = r5;
        r11 = r3;
        r12 = r4;
    L_0x0182:
        r3 = "raw_events_metadata";
        r4 = 1;
        r4 = new java.lang.String[r4];	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0bfe }
        r5 = 0;
        r6 = "metadata";
        r4[r5] = r6;	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0bfe }
        r5 = "app_id = ? and metadata_fingerprint = ?";
        r6 = 2;
        r6 = new java.lang.String[r6];	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0bfe }
        r7 = 0;
        r6[r7] = r12;	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0bfe }
        r7 = 1;
        r6[r7] = r13;	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0bfe }
        r7 = 0;
        r8 = 0;
        r9 = "rowid";
        r10 = "2";
        r11 = r2.query(r3, r4, r5, r6, r7, r8, r9, r10);	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0bfe }
        r3 = r11.moveToFirst();	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0bfe }
        if (r3 != 0) goto L_0x023c;
    L_0x01ac:
        r2 = r14.zzge();	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0bfe }
        r2 = r2.zzim();	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0bfe }
        r3 = "Raw event metadata record is missing. appId";
        r4 = com.google.android.gms.internal.measurement.zzfg.zzbm(r12);	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0bfe }
        r2.zzg(r3, r4);	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0bfe }
        if (r11 == 0) goto L_0x0086;
    L_0x01c0:
        r11.close();	 Catch:{ all -> 0x01c5 }
        goto L_0x0086;
    L_0x01c5:
        r2 = move-exception;
        r3 = r30.zzix();
        r3.endTransaction();
        throw r2;
    L_0x01ce:
        r6 = -1;
        r5 = (r16 > r6 ? 1 : (r16 == r6 ? 0 : -1));
        if (r5 == 0) goto L_0x0222;
    L_0x01d4:
        r5 = 2;
        r5 = new java.lang.String[r5];	 Catch:{ SQLiteException -> 0x0c02 }
        r6 = 0;
        r7 = 0;
        r5[r6] = r7;	 Catch:{ SQLiteException -> 0x0c02 }
        r6 = 1;
        r7 = java.lang.String.valueOf(r16);	 Catch:{ SQLiteException -> 0x0c02 }
        r5[r6] = r7;	 Catch:{ SQLiteException -> 0x0c02 }
        r6 = r5;
    L_0x01e3:
        r8 = -1;
        r5 = (r16 > r8 ? 1 : (r16 == r8 ? 0 : -1));
        if (r5 == 0) goto L_0x022b;
    L_0x01e9:
        r5 = " and rowid <= ?";
    L_0x01ec:
        r7 = java.lang.String.valueOf(r5);	 Catch:{ SQLiteException -> 0x0c02 }
        r7 = r7.length();	 Catch:{ SQLiteException -> 0x0c02 }
        r7 = r7 + 84;
        r8 = new java.lang.StringBuilder;	 Catch:{ SQLiteException -> 0x0c02 }
        r8.<init>(r7);	 Catch:{ SQLiteException -> 0x0c02 }
        r7 = "select metadata_fingerprint from raw_events where app_id = ?";
        r7 = r8.append(r7);	 Catch:{ SQLiteException -> 0x0c02 }
        r5 = r7.append(r5);	 Catch:{ SQLiteException -> 0x0c02 }
        r7 = " order by rowid limit 1;";
        r5 = r5.append(r7);	 Catch:{ SQLiteException -> 0x0c02 }
        r5 = r5.toString();	 Catch:{ SQLiteException -> 0x0c02 }
        r3 = r2.rawQuery(r5, r6);	 Catch:{ SQLiteException -> 0x0c02 }
        r5 = r3.moveToFirst();	 Catch:{ SQLiteException -> 0x0c02 }
        if (r5 != 0) goto L_0x022f;
    L_0x021b:
        if (r3 == 0) goto L_0x0086;
    L_0x021d:
        r3.close();	 Catch:{ all -> 0x01c5 }
        goto L_0x0086;
    L_0x0222:
        r5 = 1;
        r5 = new java.lang.String[r5];	 Catch:{ SQLiteException -> 0x0c02 }
        r6 = 0;
        r7 = 0;
        r5[r6] = r7;	 Catch:{ SQLiteException -> 0x0c02 }
        r6 = r5;
        goto L_0x01e3;
    L_0x022b:
        r5 = "";
        goto L_0x01ec;
    L_0x022f:
        r5 = 0;
        r5 = r3.getString(r5);	 Catch:{ SQLiteException -> 0x0c02 }
        r3.close();	 Catch:{ SQLiteException -> 0x0c02 }
        r13 = r5;
        r11 = r3;
        r12 = r4;
        goto L_0x0182;
    L_0x023c:
        r3 = 0;
        r3 = r11.getBlob(r3);	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0bfe }
        r4 = 0;
        r5 = r3.length;	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0bfe }
        r3 = com.google.android.gms.internal.measurement.zzabv.zza(r3, r4, r5);	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0bfe }
        r4 = new com.google.android.gms.internal.measurement.zzkq;	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0bfe }
        r4.<init>();	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0bfe }
        r4.zzb(r3);	 Catch:{ IOException -> 0x02cf }
        r3 = r11.moveToNext();	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0bfe }
        if (r3 == 0) goto L_0x0267;
    L_0x0255:
        r3 = r14.zzge();	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0bfe }
        r3 = r3.zzip();	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0bfe }
        r5 = "Get multiple raw event metadata records, expected one. appId";
        r6 = com.google.android.gms.internal.measurement.zzfg.zzbm(r12);	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0bfe }
        r3.zzg(r5, r6);	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0bfe }
    L_0x0267:
        r11.close();	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0bfe }
        r0 = r21;
        r0.zzb(r4);	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0bfe }
        r4 = -1;
        r3 = (r16 > r4 ? 1 : (r16 == r4 ? 0 : -1));
        if (r3 == 0) goto L_0x02e9;
    L_0x0275:
        r5 = "app_id = ? and metadata_fingerprint = ? and rowid <= ?";
        r3 = 3;
        r6 = new java.lang.String[r3];	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0bfe }
        r3 = 0;
        r6[r3] = r12;	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0bfe }
        r3 = 1;
        r6[r3] = r13;	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0bfe }
        r3 = 2;
        r4 = java.lang.String.valueOf(r16);	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0bfe }
        r6[r3] = r4;	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0bfe }
    L_0x0288:
        r3 = "raw_events";
        r4 = 4;
        r4 = new java.lang.String[r4];	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0bfe }
        r7 = 0;
        r8 = "rowid";
        r4[r7] = r8;	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0bfe }
        r7 = 1;
        r8 = "name";
        r4[r7] = r8;	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0bfe }
        r7 = 2;
        r8 = "timestamp";
        r4[r7] = r8;	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0bfe }
        r7 = 3;
        r8 = "data";
        r4[r7] = r8;	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0bfe }
        r7 = 0;
        r8 = 0;
        r9 = "rowid";
        r10 = 0;
        r3 = r2.query(r3, r4, r5, r6, r7, r8, r9, r10);	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0bfe }
        r2 = r3.moveToFirst();	 Catch:{ SQLiteException -> 0x0c05 }
        if (r2 != 0) goto L_0x0312;
    L_0x02b6:
        r2 = r14.zzge();	 Catch:{ SQLiteException -> 0x0c05 }
        r2 = r2.zzip();	 Catch:{ SQLiteException -> 0x0c05 }
        r4 = "Raw event data disappeared while in transaction. appId";
        r5 = com.google.android.gms.internal.measurement.zzfg.zzbm(r12);	 Catch:{ SQLiteException -> 0x0c05 }
        r2.zzg(r4, r5);	 Catch:{ SQLiteException -> 0x0c05 }
        if (r3 == 0) goto L_0x0086;
    L_0x02ca:
        r3.close();	 Catch:{ all -> 0x01c5 }
        goto L_0x0086;
    L_0x02cf:
        r2 = move-exception;
        r3 = r14.zzge();	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0bfe }
        r3 = r3.zzim();	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0bfe }
        r4 = "Data loss. Failed to merge raw event metadata. appId";
        r5 = com.google.android.gms.internal.measurement.zzfg.zzbm(r12);	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0bfe }
        r3.zze(r4, r5, r2);	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0bfe }
        if (r11 == 0) goto L_0x0086;
    L_0x02e4:
        r11.close();	 Catch:{ all -> 0x01c5 }
        goto L_0x0086;
    L_0x02e9:
        r5 = "app_id = ? and metadata_fingerprint = ?";
        r3 = 2;
        r6 = new java.lang.String[r3];	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0bfe }
        r3 = 0;
        r6[r3] = r12;	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0bfe }
        r3 = 1;
        r6[r3] = r13;	 Catch:{ SQLiteException -> 0x02f6, all -> 0x0bfe }
        goto L_0x0288;
    L_0x02f6:
        r2 = move-exception;
        r3 = r11;
        r4 = r12;
    L_0x02f9:
        r5 = r14.zzge();	 Catch:{ all -> 0x036b }
        r5 = r5.zzim();	 Catch:{ all -> 0x036b }
        r6 = "Data loss. Error selecting raw event. appId";
        r4 = com.google.android.gms.internal.measurement.zzfg.zzbm(r4);	 Catch:{ all -> 0x036b }
        r5.zze(r6, r4, r2);	 Catch:{ all -> 0x036b }
        if (r3 == 0) goto L_0x0086;
    L_0x030d:
        r3.close();	 Catch:{ all -> 0x01c5 }
        goto L_0x0086;
    L_0x0312:
        r2 = 0;
        r4 = r3.getLong(r2);	 Catch:{ SQLiteException -> 0x0c05 }
        r2 = 3;
        r2 = r3.getBlob(r2);	 Catch:{ SQLiteException -> 0x0c05 }
        r6 = 0;
        r7 = r2.length;	 Catch:{ SQLiteException -> 0x0c05 }
        r2 = com.google.android.gms.internal.measurement.zzabv.zza(r2, r6, r7);	 Catch:{ SQLiteException -> 0x0c05 }
        r6 = new com.google.android.gms.internal.measurement.zzkn;	 Catch:{ SQLiteException -> 0x0c05 }
        r6.<init>();	 Catch:{ SQLiteException -> 0x0c05 }
        r6.zzb(r2);	 Catch:{ IOException -> 0x034b }
        r2 = 1;
        r2 = r3.getString(r2);	 Catch:{ SQLiteException -> 0x0c05 }
        r6.name = r2;	 Catch:{ SQLiteException -> 0x0c05 }
        r2 = 2;
        r8 = r3.getLong(r2);	 Catch:{ SQLiteException -> 0x0c05 }
        r2 = java.lang.Long.valueOf(r8);	 Catch:{ SQLiteException -> 0x0c05 }
        r6.zzatb = r2;	 Catch:{ SQLiteException -> 0x0c05 }
        r0 = r21;
        r2 = r0.zza(r4, r6);	 Catch:{ SQLiteException -> 0x0c05 }
        if (r2 != 0) goto L_0x035e;
    L_0x0344:
        if (r3 == 0) goto L_0x0086;
    L_0x0346:
        r3.close();	 Catch:{ all -> 0x01c5 }
        goto L_0x0086;
    L_0x034b:
        r2 = move-exception;
        r4 = r14.zzge();	 Catch:{ SQLiteException -> 0x0c05 }
        r4 = r4.zzim();	 Catch:{ SQLiteException -> 0x0c05 }
        r5 = "Data loss. Failed to merge raw event. appId";
        r6 = com.google.android.gms.internal.measurement.zzfg.zzbm(r12);	 Catch:{ SQLiteException -> 0x0c05 }
        r4.zze(r5, r6, r2);	 Catch:{ SQLiteException -> 0x0c05 }
    L_0x035e:
        r2 = r3.moveToNext();	 Catch:{ SQLiteException -> 0x0c05 }
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
        r2 = r30.zzkm();	 Catch:{ all -> 0x01c5 }
        r0 = r21;
        r3 = r0.zzaqv;	 Catch:{ all -> 0x01c5 }
        r3 = r3.zzti;	 Catch:{ all -> 0x01c5 }
        r4 = r12.name;	 Catch:{ all -> 0x01c5 }
        r19 = r2.zzo(r3, r4);	 Catch:{ all -> 0x01c5 }
        if (r19 != 0) goto L_0x0395;
    L_0x038a:
        r30.zzgb();	 Catch:{ all -> 0x01c5 }
        r2 = r12.name;	 Catch:{ all -> 0x01c5 }
        r2 = com.google.android.gms.internal.measurement.zzka.zzcl(r2);	 Catch:{ all -> 0x01c5 }
        if (r2 == 0) goto L_0x05ae;
    L_0x0395:
        r4 = 0;
        r3 = 0;
        r2 = r12.zzata;	 Catch:{ all -> 0x01c5 }
        if (r2 != 0) goto L_0x03a0;
    L_0x039b:
        r2 = 0;
        r2 = new com.google.android.gms.internal.measurement.zzko[r2];	 Catch:{ all -> 0x01c5 }
        r12.zzata = r2;	 Catch:{ all -> 0x01c5 }
    L_0x03a0:
        r6 = r12.zzata;	 Catch:{ all -> 0x01c5 }
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
        r2.zzate = r4;	 Catch:{ all -> 0x01c5 }
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
        if (r8 == 0) goto L_0x0c1d;
    L_0x03cd:
        r8 = 1;
        r3 = java.lang.Long.valueOf(r8);	 Catch:{ all -> 0x01c5 }
        r2.zzate = r3;	 Catch:{ all -> 0x01c5 }
        r2 = 1;
        goto L_0x03be;
    L_0x03d7:
        if (r4 != 0) goto L_0x0419;
    L_0x03d9:
        if (r19 == 0) goto L_0x0419;
    L_0x03db:
        r2 = r30.zzge();	 Catch:{ all -> 0x01c5 }
        r2 = r2.zzit();	 Catch:{ all -> 0x01c5 }
        r4 = "Marking event as conversion";
        r5 = r30.zzga();	 Catch:{ all -> 0x01c5 }
        r6 = r12.name;	 Catch:{ all -> 0x01c5 }
        r5 = r5.zzbj(r6);	 Catch:{ all -> 0x01c5 }
        r2.zzg(r4, r5);	 Catch:{ all -> 0x01c5 }
        r2 = r12.zzata;	 Catch:{ all -> 0x01c5 }
        r4 = r12.zzata;	 Catch:{ all -> 0x01c5 }
        r4 = r4.length;	 Catch:{ all -> 0x01c5 }
        r4 = r4 + 1;
        r2 = java.util.Arrays.copyOf(r2, r4);	 Catch:{ all -> 0x01c5 }
        r2 = (com.google.android.gms.internal.measurement.zzko[]) r2;	 Catch:{ all -> 0x01c5 }
        r4 = new com.google.android.gms.internal.measurement.zzko;	 Catch:{ all -> 0x01c5 }
        r4.<init>();	 Catch:{ all -> 0x01c5 }
        r5 = "_c";
        r4.name = r5;	 Catch:{ all -> 0x01c5 }
        r6 = 1;
        r5 = java.lang.Long.valueOf(r6);	 Catch:{ all -> 0x01c5 }
        r4.zzate = r5;	 Catch:{ all -> 0x01c5 }
        r5 = r2.length;	 Catch:{ all -> 0x01c5 }
        r5 = r5 + -1;
        r2[r5] = r4;	 Catch:{ all -> 0x01c5 }
        r12.zzata = r2;	 Catch:{ all -> 0x01c5 }
    L_0x0419:
        if (r3 != 0) goto L_0x0459;
    L_0x041b:
        r2 = r30.zzge();	 Catch:{ all -> 0x01c5 }
        r2 = r2.zzit();	 Catch:{ all -> 0x01c5 }
        r3 = "Marking event as real-time";
        r4 = r30.zzga();	 Catch:{ all -> 0x01c5 }
        r5 = r12.name;	 Catch:{ all -> 0x01c5 }
        r4 = r4.zzbj(r5);	 Catch:{ all -> 0x01c5 }
        r2.zzg(r3, r4);	 Catch:{ all -> 0x01c5 }
        r2 = r12.zzata;	 Catch:{ all -> 0x01c5 }
        r3 = r12.zzata;	 Catch:{ all -> 0x01c5 }
        r3 = r3.length;	 Catch:{ all -> 0x01c5 }
        r3 = r3 + 1;
        r2 = java.util.Arrays.copyOf(r2, r3);	 Catch:{ all -> 0x01c5 }
        r2 = (com.google.android.gms.internal.measurement.zzko[]) r2;	 Catch:{ all -> 0x01c5 }
        r3 = new com.google.android.gms.internal.measurement.zzko;	 Catch:{ all -> 0x01c5 }
        r3.<init>();	 Catch:{ all -> 0x01c5 }
        r4 = "_r";
        r3.name = r4;	 Catch:{ all -> 0x01c5 }
        r4 = 1;
        r4 = java.lang.Long.valueOf(r4);	 Catch:{ all -> 0x01c5 }
        r3.zzate = r4;	 Catch:{ all -> 0x01c5 }
        r4 = r2.length;	 Catch:{ all -> 0x01c5 }
        r4 = r4 + -1;
        r2[r4] = r3;	 Catch:{ all -> 0x01c5 }
        r12.zzata = r2;	 Catch:{ all -> 0x01c5 }
    L_0x0459:
        r2 = 1;
        r3 = r30.zzix();	 Catch:{ all -> 0x01c5 }
        r4 = r30.zzkr();	 Catch:{ all -> 0x01c5 }
        r0 = r21;
        r6 = r0.zzaqv;	 Catch:{ all -> 0x01c5 }
        r6 = r6.zzti;	 Catch:{ all -> 0x01c5 }
        r7 = 0;
        r8 = 0;
        r9 = 0;
        r10 = 0;
        r11 = 1;
        r3 = r3.zza(r4, r6, r7, r8, r9, r10, r11);	 Catch:{ all -> 0x01c5 }
        r4 = r3.zzafh;	 Catch:{ all -> 0x01c5 }
        r3 = r30.zzgg();	 Catch:{ all -> 0x01c5 }
        r0 = r21;
        r6 = r0.zzaqv;	 Catch:{ all -> 0x01c5 }
        r6 = r6.zzti;	 Catch:{ all -> 0x01c5 }
        r3 = r3.zzar(r6);	 Catch:{ all -> 0x01c5 }
        r6 = (long) r3;	 Catch:{ all -> 0x01c5 }
        r3 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r3 <= 0) goto L_0x0c19;
    L_0x0486:
        r2 = 0;
    L_0x0487:
        r3 = r12.zzata;	 Catch:{ all -> 0x01c5 }
        r3 = r3.length;	 Catch:{ all -> 0x01c5 }
        if (r2 >= r3) goto L_0x04b9;
    L_0x048c:
        r3 = "_r";
        r4 = r12.zzata;	 Catch:{ all -> 0x01c5 }
        r4 = r4[r2];	 Catch:{ all -> 0x01c5 }
        r4 = r4.name;	 Catch:{ all -> 0x01c5 }
        r3 = r3.equals(r4);	 Catch:{ all -> 0x01c5 }
        if (r3 == 0) goto L_0x0523;
    L_0x049b:
        r3 = r12.zzata;	 Catch:{ all -> 0x01c5 }
        r3 = r3.length;	 Catch:{ all -> 0x01c5 }
        r3 = r3 + -1;
        r3 = new com.google.android.gms.internal.measurement.zzko[r3];	 Catch:{ all -> 0x01c5 }
        if (r2 <= 0) goto L_0x04ab;
    L_0x04a4:
        r4 = r12.zzata;	 Catch:{ all -> 0x01c5 }
        r5 = 0;
        r6 = 0;
        java.lang.System.arraycopy(r4, r5, r3, r6, r2);	 Catch:{ all -> 0x01c5 }
    L_0x04ab:
        r4 = r3.length;	 Catch:{ all -> 0x01c5 }
        if (r2 >= r4) goto L_0x04b7;
    L_0x04ae:
        r4 = r12.zzata;	 Catch:{ all -> 0x01c5 }
        r5 = r2 + 1;
        r6 = r3.length;	 Catch:{ all -> 0x01c5 }
        r6 = r6 - r2;
        java.lang.System.arraycopy(r4, r5, r3, r2, r6);	 Catch:{ all -> 0x01c5 }
    L_0x04b7:
        r12.zzata = r3;	 Catch:{ all -> 0x01c5 }
    L_0x04b9:
        r2 = r12.name;	 Catch:{ all -> 0x01c5 }
        r2 = com.google.android.gms.internal.measurement.zzka.zzcc(r2);	 Catch:{ all -> 0x01c5 }
        if (r2 == 0) goto L_0x05ae;
    L_0x04c1:
        if (r19 == 0) goto L_0x05ae;
    L_0x04c3:
        r3 = r30.zzix();	 Catch:{ all -> 0x01c5 }
        r4 = r30.zzkr();	 Catch:{ all -> 0x01c5 }
        r0 = r21;
        r2 = r0.zzaqv;	 Catch:{ all -> 0x01c5 }
        r6 = r2.zzti;	 Catch:{ all -> 0x01c5 }
        r7 = 0;
        r8 = 0;
        r9 = 1;
        r10 = 0;
        r11 = 0;
        r2 = r3.zza(r4, r6, r7, r8, r9, r10, r11);	 Catch:{ all -> 0x01c5 }
        r2 = r2.zzaff;	 Catch:{ all -> 0x01c5 }
        r4 = r30.zzgg();	 Catch:{ all -> 0x01c5 }
        r0 = r21;
        r5 = r0.zzaqv;	 Catch:{ all -> 0x01c5 }
        r5 = r5.zzti;	 Catch:{ all -> 0x01c5 }
        r6 = com.google.android.gms.internal.measurement.zzew.zzagv;	 Catch:{ all -> 0x01c5 }
        r4 = r4.zzb(r5, r6);	 Catch:{ all -> 0x01c5 }
        r4 = (long) r4;	 Catch:{ all -> 0x01c5 }
        r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
        if (r2 <= 0) goto L_0x05ae;
    L_0x04f1:
        r2 = r30.zzge();	 Catch:{ all -> 0x01c5 }
        r2 = r2.zzip();	 Catch:{ all -> 0x01c5 }
        r3 = "Too many conversions. Not logging as conversion. appId";
        r0 = r21;
        r4 = r0.zzaqv;	 Catch:{ all -> 0x01c5 }
        r4 = r4.zzti;	 Catch:{ all -> 0x01c5 }
        r4 = com.google.android.gms.internal.measurement.zzfg.zzbm(r4);	 Catch:{ all -> 0x01c5 }
        r2.zzg(r3, r4);	 Catch:{ all -> 0x01c5 }
        r4 = 0;
        r3 = 0;
        r6 = r12.zzata;	 Catch:{ all -> 0x01c5 }
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
        if (r2 == 0) goto L_0x0c16;
    L_0x0532:
        r4 = 1;
        r2 = r3;
        goto L_0x051f;
    L_0x0535:
        if (r4 == 0) goto L_0x0584;
    L_0x0537:
        if (r3 == 0) goto L_0x0584;
    L_0x0539:
        r2 = r12.zzata;	 Catch:{ all -> 0x01c5 }
        r4 = 1;
        r4 = new com.google.android.gms.internal.measurement.zzko[r4];	 Catch:{ all -> 0x01c5 }
        r5 = 0;
        r4[r5] = r3;	 Catch:{ all -> 0x01c5 }
        r2 = com.google.android.gms.common.util.ArrayUtils.removeAll(r2, r4);	 Catch:{ all -> 0x01c5 }
        r2 = (com.google.android.gms.internal.measurement.zzko[]) r2;	 Catch:{ all -> 0x01c5 }
        r12.zzata = r2;	 Catch:{ all -> 0x01c5 }
        r5 = r17;
    L_0x054b:
        if (r18 == 0) goto L_0x0c13;
    L_0x054d:
        r2 = "_e";
        r3 = r12.name;	 Catch:{ all -> 0x01c5 }
        r2 = r2.equals(r3);	 Catch:{ all -> 0x01c5 }
        if (r2 == 0) goto L_0x0c13;
    L_0x0558:
        r2 = r12.zzata;	 Catch:{ all -> 0x01c5 }
        if (r2 == 0) goto L_0x0561;
    L_0x055c:
        r2 = r12.zzata;	 Catch:{ all -> 0x01c5 }
        r2 = r2.length;	 Catch:{ all -> 0x01c5 }
        if (r2 != 0) goto L_0x05b1;
    L_0x0561:
        r2 = r30.zzge();	 Catch:{ all -> 0x01c5 }
        r2 = r2.zzip();	 Catch:{ all -> 0x01c5 }
        r3 = "Engagement event does not contain any parameters. appId";
        r0 = r21;
        r4 = r0.zzaqv;	 Catch:{ all -> 0x01c5 }
        r4 = r4.zzti;	 Catch:{ all -> 0x01c5 }
        r4 = com.google.android.gms.internal.measurement.zzfg.zzbm(r4);	 Catch:{ all -> 0x01c5 }
        r2.zzg(r3, r4);	 Catch:{ all -> 0x01c5 }
        r2 = r14;
    L_0x057a:
        r0 = r22;
        r6 = r0.zzati;	 Catch:{ all -> 0x01c5 }
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
        r3.zzate = r2;	 Catch:{ all -> 0x01c5 }
        r5 = r17;
        goto L_0x054b;
    L_0x0596:
        r2 = r30.zzge();	 Catch:{ all -> 0x01c5 }
        r2 = r2.zzim();	 Catch:{ all -> 0x01c5 }
        r3 = "Did not find conversion parameter. appId";
        r0 = r21;
        r4 = r0.zzaqv;	 Catch:{ all -> 0x01c5 }
        r4 = r4.zzti;	 Catch:{ all -> 0x01c5 }
        r4 = com.google.android.gms.internal.measurement.zzfg.zzbm(r4);	 Catch:{ all -> 0x01c5 }
        r2.zzg(r3, r4);	 Catch:{ all -> 0x01c5 }
    L_0x05ae:
        r5 = r17;
        goto L_0x054b;
    L_0x05b1:
        r30.zzgb();	 Catch:{ all -> 0x01c5 }
        r2 = "_et";
        r2 = com.google.android.gms.internal.measurement.zzka.zzb(r12, r2);	 Catch:{ all -> 0x01c5 }
        r2 = (java.lang.Long) r2;	 Catch:{ all -> 0x01c5 }
        if (r2 != 0) goto L_0x05d9;
    L_0x05bf:
        r2 = r30.zzge();	 Catch:{ all -> 0x01c5 }
        r2 = r2.zzip();	 Catch:{ all -> 0x01c5 }
        r3 = "Engagement event does not include duration. appId";
        r0 = r21;
        r4 = r0.zzaqv;	 Catch:{ all -> 0x01c5 }
        r4 = r4.zzti;	 Catch:{ all -> 0x01c5 }
        r4 = com.google.android.gms.internal.measurement.zzfg.zzbm(r4);	 Catch:{ all -> 0x01c5 }
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
        r2 = r0.zzaqx;	 Catch:{ all -> 0x01c5 }
        r2 = r2.size();	 Catch:{ all -> 0x01c5 }
        if (r13 >= r2) goto L_0x05f8;
    L_0x05ea:
        r0 = r22;
        r2 = r0.zzati;	 Catch:{ all -> 0x01c5 }
        r2 = java.util.Arrays.copyOf(r2, r13);	 Catch:{ all -> 0x01c5 }
        r2 = (com.google.android.gms.internal.measurement.zzkn[]) r2;	 Catch:{ all -> 0x01c5 }
        r0 = r22;
        r0.zzati = r2;	 Catch:{ all -> 0x01c5 }
    L_0x05f8:
        if (r18 == 0) goto L_0x06ad;
    L_0x05fa:
        r2 = r30.zzix();	 Catch:{ all -> 0x01c5 }
        r0 = r22;
        r3 = r0.zzti;	 Catch:{ all -> 0x01c5 }
        r4 = "_lte";
        r8 = r2.zzh(r3, r4);	 Catch:{ all -> 0x01c5 }
        if (r8 == 0) goto L_0x060f;
    L_0x060b:
        r2 = r8.value;	 Catch:{ all -> 0x01c5 }
        if (r2 != 0) goto L_0x078d;
    L_0x060f:
        r2 = new com.google.android.gms.internal.measurement.zzjz;	 Catch:{ all -> 0x01c5 }
        r0 = r22;
        r3 = r0.zzti;	 Catch:{ all -> 0x01c5 }
        r4 = "auto";
        r5 = "_lte";
        r6 = r30.zzbt();	 Catch:{ all -> 0x01c5 }
        r6 = r6.currentTimeMillis();	 Catch:{ all -> 0x01c5 }
        r8 = java.lang.Long.valueOf(r14);	 Catch:{ all -> 0x01c5 }
        r2.<init>(r3, r4, r5, r6, r8);	 Catch:{ all -> 0x01c5 }
        r4 = r2;
    L_0x062b:
        r5 = new com.google.android.gms.internal.measurement.zzks;	 Catch:{ all -> 0x01c5 }
        r5.<init>();	 Catch:{ all -> 0x01c5 }
        r2 = "_lte";
        r5.name = r2;	 Catch:{ all -> 0x01c5 }
        r2 = r30.zzbt();	 Catch:{ all -> 0x01c5 }
        r2 = r2.currentTimeMillis();	 Catch:{ all -> 0x01c5 }
        r2 = java.lang.Long.valueOf(r2);	 Catch:{ all -> 0x01c5 }
        r5.zzaun = r2;	 Catch:{ all -> 0x01c5 }
        r2 = r4.value;	 Catch:{ all -> 0x01c5 }
        r2 = (java.lang.Long) r2;	 Catch:{ all -> 0x01c5 }
        r5.zzate = r2;	 Catch:{ all -> 0x01c5 }
        r2 = 0;
        r3 = 0;
    L_0x064b:
        r0 = r22;
        r6 = r0.zzatj;	 Catch:{ all -> 0x01c5 }
        r6 = r6.length;	 Catch:{ all -> 0x01c5 }
        if (r3 >= r6) goto L_0x066a;
    L_0x0652:
        r6 = "_lte";
        r0 = r22;
        r7 = r0.zzatj;	 Catch:{ all -> 0x01c5 }
        r7 = r7[r3];	 Catch:{ all -> 0x01c5 }
        r7 = r7.name;	 Catch:{ all -> 0x01c5 }
        r6 = r6.equals(r7);	 Catch:{ all -> 0x01c5 }
        if (r6 == 0) goto L_0x07b4;
    L_0x0663:
        r0 = r22;
        r2 = r0.zzatj;	 Catch:{ all -> 0x01c5 }
        r2[r3] = r5;	 Catch:{ all -> 0x01c5 }
        r2 = 1;
    L_0x066a:
        if (r2 != 0) goto L_0x0690;
    L_0x066c:
        r0 = r22;
        r2 = r0.zzatj;	 Catch:{ all -> 0x01c5 }
        r0 = r22;
        r3 = r0.zzatj;	 Catch:{ all -> 0x01c5 }
        r3 = r3.length;	 Catch:{ all -> 0x01c5 }
        r3 = r3 + 1;
        r2 = java.util.Arrays.copyOf(r2, r3);	 Catch:{ all -> 0x01c5 }
        r2 = (com.google.android.gms.internal.measurement.zzks[]) r2;	 Catch:{ all -> 0x01c5 }
        r0 = r22;
        r0.zzatj = r2;	 Catch:{ all -> 0x01c5 }
        r0 = r22;
        r2 = r0.zzatj;	 Catch:{ all -> 0x01c5 }
        r0 = r21;
        r3 = r0.zzaqv;	 Catch:{ all -> 0x01c5 }
        r3 = r3.zzatj;	 Catch:{ all -> 0x01c5 }
        r3 = r3.length;	 Catch:{ all -> 0x01c5 }
        r3 = r3 + -1;
        r2[r3] = r5;	 Catch:{ all -> 0x01c5 }
    L_0x0690:
        r2 = 0;
        r2 = (r14 > r2 ? 1 : (r14 == r2 ? 0 : -1));
        if (r2 <= 0) goto L_0x06ad;
    L_0x0696:
        r2 = r30.zzix();	 Catch:{ all -> 0x01c5 }
        r2.zza(r4);	 Catch:{ all -> 0x01c5 }
        r2 = r30.zzge();	 Catch:{ all -> 0x01c5 }
        r2 = r2.zzis();	 Catch:{ all -> 0x01c5 }
        r3 = "Updated lifetime engagement user property with value. Value";
        r4 = r4.value;	 Catch:{ all -> 0x01c5 }
        r2.zzg(r3, r4);	 Catch:{ all -> 0x01c5 }
    L_0x06ad:
        r0 = r22;
        r2 = r0.zzti;	 Catch:{ all -> 0x01c5 }
        r0 = r22;
        r3 = r0.zzatj;	 Catch:{ all -> 0x01c5 }
        r0 = r22;
        r4 = r0.zzati;	 Catch:{ all -> 0x01c5 }
        r0 = r30;
        r2 = r0.zza(r2, r3, r4);	 Catch:{ all -> 0x01c5 }
        r0 = r22;
        r0.zzaua = r2;	 Catch:{ all -> 0x01c5 }
        r2 = r30.zzgg();	 Catch:{ all -> 0x01c5 }
        r0 = r21;
        r3 = r0.zzaqv;	 Catch:{ all -> 0x01c5 }
        r3 = r3.zzti;	 Catch:{ all -> 0x01c5 }
        r2 = r2.zzau(r3);	 Catch:{ all -> 0x01c5 }
        if (r2 == 0) goto L_0x09f9;
    L_0x06d3:
        r23 = new java.util.HashMap;	 Catch:{ all -> 0x01c5 }
        r23.<init>();	 Catch:{ all -> 0x01c5 }
        r0 = r22;
        r2 = r0.zzati;	 Catch:{ all -> 0x01c5 }
        r2 = r2.length;	 Catch:{ all -> 0x01c5 }
        r0 = new com.google.android.gms.internal.measurement.zzkn[r2];	 Catch:{ all -> 0x01c5 }
        r24 = r0;
        r18 = 0;
        r2 = r30.zzgb();	 Catch:{ all -> 0x01c5 }
        r25 = r2.zzlc();	 Catch:{ all -> 0x01c5 }
        r0 = r22;
        r0 = r0.zzati;	 Catch:{ all -> 0x01c5 }
        r26 = r0;
        r0 = r26;
        r0 = r0.length;	 Catch:{ all -> 0x01c5 }
        r27 = r0;
        r2 = 0;
        r19 = r2;
    L_0x06f9:
        r0 = r19;
        r1 = r27;
        if (r0 >= r1) goto L_0x09c0;
    L_0x06ff:
        r28 = r26[r19];	 Catch:{ all -> 0x01c5 }
        r0 = r28;
        r2 = r0.name;	 Catch:{ all -> 0x01c5 }
        r3 = "_ep";
        r2 = r2.equals(r3);	 Catch:{ all -> 0x01c5 }
        if (r2 == 0) goto L_0x07b8;
    L_0x070e:
        r30.zzgb();	 Catch:{ all -> 0x01c5 }
        r2 = "_en";
        r0 = r28;
        r2 = com.google.android.gms.internal.measurement.zzka.zzb(r0, r2);	 Catch:{ all -> 0x01c5 }
        r2 = (java.lang.String) r2;	 Catch:{ all -> 0x01c5 }
        r0 = r23;
        r3 = r0.get(r2);	 Catch:{ all -> 0x01c5 }
        r3 = (com.google.android.gms.internal.measurement.zzeq) r3;	 Catch:{ all -> 0x01c5 }
        if (r3 != 0) goto L_0x0739;
    L_0x0726:
        r3 = r30.zzix();	 Catch:{ all -> 0x01c5 }
        r0 = r21;
        r4 = r0.zzaqv;	 Catch:{ all -> 0x01c5 }
        r4 = r4.zzti;	 Catch:{ all -> 0x01c5 }
        r3 = r3.zzf(r4, r2);	 Catch:{ all -> 0x01c5 }
        r0 = r23;
        r0.put(r2, r3);	 Catch:{ all -> 0x01c5 }
    L_0x0739:
        r2 = r3.zzafv;	 Catch:{ all -> 0x01c5 }
        if (r2 != 0) goto L_0x09bc;
    L_0x073d:
        r2 = r3.zzafw;	 Catch:{ all -> 0x01c5 }
        r4 = r2.longValue();	 Catch:{ all -> 0x01c5 }
        r6 = 1;
        r2 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r2 <= 0) goto L_0x075d;
    L_0x0749:
        r30.zzgb();	 Catch:{ all -> 0x01c5 }
        r0 = r28;
        r2 = r0.zzata;	 Catch:{ all -> 0x01c5 }
        r4 = "_sr";
        r5 = r3.zzafw;	 Catch:{ all -> 0x01c5 }
        r2 = com.google.android.gms.internal.measurement.zzka.zza(r2, r4, r5);	 Catch:{ all -> 0x01c5 }
        r0 = r28;
        r0.zzata = r2;	 Catch:{ all -> 0x01c5 }
    L_0x075d:
        r2 = r3.zzafx;	 Catch:{ all -> 0x01c5 }
        if (r2 == 0) goto L_0x0781;
    L_0x0761:
        r2 = r3.zzafx;	 Catch:{ all -> 0x01c5 }
        r2 = r2.booleanValue();	 Catch:{ all -> 0x01c5 }
        if (r2 == 0) goto L_0x0781;
    L_0x0769:
        r30.zzgb();	 Catch:{ all -> 0x01c5 }
        r0 = r28;
        r2 = r0.zzata;	 Catch:{ all -> 0x01c5 }
        r3 = "_efs";
        r4 = 1;
        r4 = java.lang.Long.valueOf(r4);	 Catch:{ all -> 0x01c5 }
        r2 = com.google.android.gms.internal.measurement.zzka.zza(r2, r3, r4);	 Catch:{ all -> 0x01c5 }
        r0 = r28;
        r0.zzata = r2;	 Catch:{ all -> 0x01c5 }
    L_0x0781:
        r2 = r18 + 1;
        r24[r18] = r28;	 Catch:{ all -> 0x01c5 }
    L_0x0785:
        r3 = r19 + 1;
        r19 = r3;
        r18 = r2;
        goto L_0x06f9;
    L_0x078d:
        r2 = new com.google.android.gms.internal.measurement.zzjz;	 Catch:{ all -> 0x01c5 }
        r0 = r22;
        r3 = r0.zzti;	 Catch:{ all -> 0x01c5 }
        r4 = "auto";
        r5 = "_lte";
        r6 = r30.zzbt();	 Catch:{ all -> 0x01c5 }
        r6 = r6.currentTimeMillis();	 Catch:{ all -> 0x01c5 }
        r8 = r8.value;	 Catch:{ all -> 0x01c5 }
        r8 = (java.lang.Long) r8;	 Catch:{ all -> 0x01c5 }
        r8 = r8.longValue();	 Catch:{ all -> 0x01c5 }
        r8 = r8 + r14;
        r8 = java.lang.Long.valueOf(r8);	 Catch:{ all -> 0x01c5 }
        r2.<init>(r3, r4, r5, r6, r8);	 Catch:{ all -> 0x01c5 }
        r4 = r2;
        goto L_0x062b;
    L_0x07b4:
        r3 = r3 + 1;
        goto L_0x064b;
    L_0x07b8:
        r2 = 1;
        r4 = "_dbg";
        r6 = 1;
        r5 = java.lang.Long.valueOf(r6);	 Catch:{ all -> 0x01c5 }
        r3 = android.text.TextUtils.isEmpty(r4);	 Catch:{ all -> 0x01c5 }
        if (r3 != 0) goto L_0x07ca;
    L_0x07c8:
        if (r5 != 0) goto L_0x07fe;
    L_0x07ca:
        r3 = 0;
    L_0x07cb:
        if (r3 != 0) goto L_0x0c0f;
    L_0x07cd:
        r2 = r30.zzkm();	 Catch:{ all -> 0x01c5 }
        r0 = r21;
        r3 = r0.zzaqv;	 Catch:{ all -> 0x01c5 }
        r3 = r3.zzti;	 Catch:{ all -> 0x01c5 }
        r0 = r28;
        r4 = r0.name;	 Catch:{ all -> 0x01c5 }
        r2 = r2.zzp(r3, r4);	 Catch:{ all -> 0x01c5 }
        r20 = r2;
    L_0x07e1:
        if (r20 > 0) goto L_0x083d;
    L_0x07e3:
        r2 = r30.zzge();	 Catch:{ all -> 0x01c5 }
        r2 = r2.zzip();	 Catch:{ all -> 0x01c5 }
        r3 = "Sample rate must be positive. event, rate";
        r0 = r28;
        r4 = r0.name;	 Catch:{ all -> 0x01c5 }
        r5 = java.lang.Integer.valueOf(r20);	 Catch:{ all -> 0x01c5 }
        r2.zze(r3, r4, r5);	 Catch:{ all -> 0x01c5 }
        r2 = r18 + 1;
        r24[r18] = r28;	 Catch:{ all -> 0x01c5 }
        goto L_0x0785;
    L_0x07fe:
        r0 = r28;
        r6 = r0.zzata;	 Catch:{ all -> 0x01c5 }
        r7 = r6.length;	 Catch:{ all -> 0x01c5 }
        r3 = 0;
    L_0x0804:
        if (r3 >= r7) goto L_0x083b;
    L_0x0806:
        r8 = r6[r3];	 Catch:{ all -> 0x01c5 }
        r9 = r8.name;	 Catch:{ all -> 0x01c5 }
        r9 = r4.equals(r9);	 Catch:{ all -> 0x01c5 }
        if (r9 == 0) goto L_0x0838;
    L_0x0810:
        r3 = r5 instanceof java.lang.Long;	 Catch:{ all -> 0x01c5 }
        if (r3 == 0) goto L_0x081c;
    L_0x0814:
        r3 = r8.zzate;	 Catch:{ all -> 0x01c5 }
        r3 = r5.equals(r3);	 Catch:{ all -> 0x01c5 }
        if (r3 != 0) goto L_0x0834;
    L_0x081c:
        r3 = r5 instanceof java.lang.String;	 Catch:{ all -> 0x01c5 }
        if (r3 == 0) goto L_0x0828;
    L_0x0820:
        r3 = r8.zzajf;	 Catch:{ all -> 0x01c5 }
        r3 = r5.equals(r3);	 Catch:{ all -> 0x01c5 }
        if (r3 != 0) goto L_0x0834;
    L_0x0828:
        r3 = r5 instanceof java.lang.Double;	 Catch:{ all -> 0x01c5 }
        if (r3 == 0) goto L_0x0836;
    L_0x082c:
        r3 = r8.zzarc;	 Catch:{ all -> 0x01c5 }
        r3 = r5.equals(r3);	 Catch:{ all -> 0x01c5 }
        if (r3 == 0) goto L_0x0836;
    L_0x0834:
        r3 = 1;
        goto L_0x07cb;
    L_0x0836:
        r3 = 0;
        goto L_0x07cb;
    L_0x0838:
        r3 = r3 + 1;
        goto L_0x0804;
    L_0x083b:
        r3 = 0;
        goto L_0x07cb;
    L_0x083d:
        r0 = r28;
        r2 = r0.name;	 Catch:{ all -> 0x01c5 }
        r0 = r23;
        r2 = r0.get(r2);	 Catch:{ all -> 0x01c5 }
        r2 = (com.google.android.gms.internal.measurement.zzeq) r2;	 Catch:{ all -> 0x01c5 }
        if (r2 != 0) goto L_0x0c0c;
    L_0x084b:
        r2 = r30.zzix();	 Catch:{ all -> 0x01c5 }
        r0 = r21;
        r3 = r0.zzaqv;	 Catch:{ all -> 0x01c5 }
        r3 = r3.zzti;	 Catch:{ all -> 0x01c5 }
        r0 = r28;
        r4 = r0.name;	 Catch:{ all -> 0x01c5 }
        r3 = r2.zzf(r3, r4);	 Catch:{ all -> 0x01c5 }
        if (r3 != 0) goto L_0x0898;
    L_0x085f:
        r2 = r30.zzge();	 Catch:{ all -> 0x01c5 }
        r2 = r2.zzip();	 Catch:{ all -> 0x01c5 }
        r3 = "Event being bundled has no eventAggregate. appId, eventName";
        r0 = r21;
        r4 = r0.zzaqv;	 Catch:{ all -> 0x01c5 }
        r4 = r4.zzti;	 Catch:{ all -> 0x01c5 }
        r0 = r28;
        r5 = r0.name;	 Catch:{ all -> 0x01c5 }
        r2.zze(r3, r4, r5);	 Catch:{ all -> 0x01c5 }
        r3 = new com.google.android.gms.internal.measurement.zzeq;	 Catch:{ all -> 0x01c5 }
        r0 = r21;
        r2 = r0.zzaqv;	 Catch:{ all -> 0x01c5 }
        r4 = r2.zzti;	 Catch:{ all -> 0x01c5 }
        r0 = r28;
        r5 = r0.name;	 Catch:{ all -> 0x01c5 }
        r6 = 1;
        r8 = 1;
        r0 = r28;
        r2 = r0.zzatb;	 Catch:{ all -> 0x01c5 }
        r10 = r2.longValue();	 Catch:{ all -> 0x01c5 }
        r12 = 0;
        r14 = 0;
        r15 = 0;
        r16 = 0;
        r3.<init>(r4, r5, r6, r8, r10, r12, r14, r15, r16);	 Catch:{ all -> 0x01c5 }
    L_0x0898:
        r30.zzgb();	 Catch:{ all -> 0x01c5 }
        r2 = "_eid";
        r0 = r28;
        r2 = com.google.android.gms.internal.measurement.zzka.zzb(r0, r2);	 Catch:{ all -> 0x01c5 }
        r2 = (java.lang.Long) r2;	 Catch:{ all -> 0x01c5 }
        if (r2 == 0) goto L_0x08da;
    L_0x08a8:
        r4 = 1;
    L_0x08a9:
        r4 = java.lang.Boolean.valueOf(r4);	 Catch:{ all -> 0x01c5 }
        r5 = 1;
        r0 = r20;
        if (r0 != r5) goto L_0x08dc;
    L_0x08b2:
        r2 = r18 + 1;
        r24[r18] = r28;	 Catch:{ all -> 0x01c5 }
        r4 = r4.booleanValue();	 Catch:{ all -> 0x01c5 }
        if (r4 == 0) goto L_0x0785;
    L_0x08bc:
        r4 = r3.zzafv;	 Catch:{ all -> 0x01c5 }
        if (r4 != 0) goto L_0x08c8;
    L_0x08c0:
        r4 = r3.zzafw;	 Catch:{ all -> 0x01c5 }
        if (r4 != 0) goto L_0x08c8;
    L_0x08c4:
        r4 = r3.zzafx;	 Catch:{ all -> 0x01c5 }
        if (r4 == 0) goto L_0x0785;
    L_0x08c8:
        r4 = 0;
        r5 = 0;
        r6 = 0;
        r3 = r3.zza(r4, r5, r6);	 Catch:{ all -> 0x01c5 }
        r0 = r28;
        r4 = r0.name;	 Catch:{ all -> 0x01c5 }
        r0 = r23;
        r0.put(r4, r3);	 Catch:{ all -> 0x01c5 }
        goto L_0x0785;
    L_0x08da:
        r4 = 0;
        goto L_0x08a9;
    L_0x08dc:
        r0 = r25;
        r1 = r20;
        r5 = r0.nextInt(r1);	 Catch:{ all -> 0x01c5 }
        if (r5 != 0) goto L_0x092d;
    L_0x08e6:
        r30.zzgb();	 Catch:{ all -> 0x01c5 }
        r0 = r28;
        r2 = r0.zzata;	 Catch:{ all -> 0x01c5 }
        r5 = "_sr";
        r0 = r20;
        r6 = (long) r0;	 Catch:{ all -> 0x01c5 }
        r6 = java.lang.Long.valueOf(r6);	 Catch:{ all -> 0x01c5 }
        r2 = com.google.android.gms.internal.measurement.zzka.zza(r2, r5, r6);	 Catch:{ all -> 0x01c5 }
        r0 = r28;
        r0.zzata = r2;	 Catch:{ all -> 0x01c5 }
        r2 = r18 + 1;
        r24[r18] = r28;	 Catch:{ all -> 0x01c5 }
        r4 = r4.booleanValue();	 Catch:{ all -> 0x01c5 }
        if (r4 == 0) goto L_0x0916;
    L_0x0909:
        r4 = 0;
        r0 = r20;
        r6 = (long) r0;	 Catch:{ all -> 0x01c5 }
        r5 = java.lang.Long.valueOf(r6);	 Catch:{ all -> 0x01c5 }
        r6 = 0;
        r3 = r3.zza(r4, r5, r6);	 Catch:{ all -> 0x01c5 }
    L_0x0916:
        r0 = r28;
        r4 = r0.name;	 Catch:{ all -> 0x01c5 }
        r0 = r28;
        r5 = r0.zzatb;	 Catch:{ all -> 0x01c5 }
        r6 = r5.longValue();	 Catch:{ all -> 0x01c5 }
        r3 = r3.zzad(r6);	 Catch:{ all -> 0x01c5 }
        r0 = r23;
        r0.put(r4, r3);	 Catch:{ all -> 0x01c5 }
        goto L_0x0785;
    L_0x092d:
        r6 = r3.zzafu;	 Catch:{ all -> 0x01c5 }
        r0 = r28;
        r5 = r0.zzatb;	 Catch:{ all -> 0x01c5 }
        r8 = r5.longValue();	 Catch:{ all -> 0x01c5 }
        r6 = r8 - r6;
        r6 = java.lang.Math.abs(r6);	 Catch:{ all -> 0x01c5 }
        r8 = 86400000; // 0x5265c00 float:7.82218E-36 double:4.2687272E-316;
        r5 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1));
        if (r5 < 0) goto L_0x09a7;
    L_0x0944:
        r30.zzgb();	 Catch:{ all -> 0x01c5 }
        r0 = r28;
        r2 = r0.zzata;	 Catch:{ all -> 0x01c5 }
        r5 = "_efs";
        r6 = 1;
        r6 = java.lang.Long.valueOf(r6);	 Catch:{ all -> 0x01c5 }
        r2 = com.google.android.gms.internal.measurement.zzka.zza(r2, r5, r6);	 Catch:{ all -> 0x01c5 }
        r0 = r28;
        r0.zzata = r2;	 Catch:{ all -> 0x01c5 }
        r30.zzgb();	 Catch:{ all -> 0x01c5 }
        r0 = r28;
        r2 = r0.zzata;	 Catch:{ all -> 0x01c5 }
        r5 = "_sr";
        r0 = r20;
        r6 = (long) r0;	 Catch:{ all -> 0x01c5 }
        r6 = java.lang.Long.valueOf(r6);	 Catch:{ all -> 0x01c5 }
        r2 = com.google.android.gms.internal.measurement.zzka.zza(r2, r5, r6);	 Catch:{ all -> 0x01c5 }
        r0 = r28;
        r0.zzata = r2;	 Catch:{ all -> 0x01c5 }
        r2 = r18 + 1;
        r24[r18] = r28;	 Catch:{ all -> 0x01c5 }
        r4 = r4.booleanValue();	 Catch:{ all -> 0x01c5 }
        if (r4 == 0) goto L_0x0990;
    L_0x097f:
        r4 = 0;
        r0 = r20;
        r6 = (long) r0;	 Catch:{ all -> 0x01c5 }
        r5 = java.lang.Long.valueOf(r6);	 Catch:{ all -> 0x01c5 }
        r6 = 1;
        r6 = java.lang.Boolean.valueOf(r6);	 Catch:{ all -> 0x01c5 }
        r3 = r3.zza(r4, r5, r6);	 Catch:{ all -> 0x01c5 }
    L_0x0990:
        r0 = r28;
        r4 = r0.name;	 Catch:{ all -> 0x01c5 }
        r0 = r28;
        r5 = r0.zzatb;	 Catch:{ all -> 0x01c5 }
        r6 = r5.longValue();	 Catch:{ all -> 0x01c5 }
        r3 = r3.zzad(r6);	 Catch:{ all -> 0x01c5 }
        r0 = r23;
        r0.put(r4, r3);	 Catch:{ all -> 0x01c5 }
        goto L_0x0785;
    L_0x09a7:
        r4 = r4.booleanValue();	 Catch:{ all -> 0x01c5 }
        if (r4 == 0) goto L_0x09bc;
    L_0x09ad:
        r0 = r28;
        r4 = r0.name;	 Catch:{ all -> 0x01c5 }
        r5 = 0;
        r6 = 0;
        r2 = r3.zza(r2, r5, r6);	 Catch:{ all -> 0x01c5 }
        r0 = r23;
        r0.put(r4, r2);	 Catch:{ all -> 0x01c5 }
    L_0x09bc:
        r2 = r18;
        goto L_0x0785;
    L_0x09c0:
        r0 = r22;
        r2 = r0.zzati;	 Catch:{ all -> 0x01c5 }
        r2 = r2.length;	 Catch:{ all -> 0x01c5 }
        r0 = r18;
        if (r0 >= r2) goto L_0x09d7;
    L_0x09c9:
        r0 = r24;
        r1 = r18;
        r2 = java.util.Arrays.copyOf(r0, r1);	 Catch:{ all -> 0x01c5 }
        r2 = (com.google.android.gms.internal.measurement.zzkn[]) r2;	 Catch:{ all -> 0x01c5 }
        r0 = r22;
        r0.zzati = r2;	 Catch:{ all -> 0x01c5 }
    L_0x09d7:
        r2 = r23.entrySet();	 Catch:{ all -> 0x01c5 }
        r3 = r2.iterator();	 Catch:{ all -> 0x01c5 }
    L_0x09df:
        r2 = r3.hasNext();	 Catch:{ all -> 0x01c5 }
        if (r2 == 0) goto L_0x09f9;
    L_0x09e5:
        r2 = r3.next();	 Catch:{ all -> 0x01c5 }
        r2 = (java.util.Map.Entry) r2;	 Catch:{ all -> 0x01c5 }
        r4 = r30.zzix();	 Catch:{ all -> 0x01c5 }
        r2 = r2.getValue();	 Catch:{ all -> 0x01c5 }
        r2 = (com.google.android.gms.internal.measurement.zzeq) r2;	 Catch:{ all -> 0x01c5 }
        r4.zza(r2);	 Catch:{ all -> 0x01c5 }
        goto L_0x09df;
    L_0x09f9:
        r2 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
        r2 = java.lang.Long.valueOf(r2);	 Catch:{ all -> 0x01c5 }
        r0 = r22;
        r0.zzatl = r2;	 Catch:{ all -> 0x01c5 }
        r2 = -9223372036854775808;
        r2 = java.lang.Long.valueOf(r2);	 Catch:{ all -> 0x01c5 }
        r0 = r22;
        r0.zzatm = r2;	 Catch:{ all -> 0x01c5 }
        r2 = 0;
    L_0x0a11:
        r0 = r22;
        r3 = r0.zzati;	 Catch:{ all -> 0x01c5 }
        r3 = r3.length;	 Catch:{ all -> 0x01c5 }
        if (r2 >= r3) goto L_0x0a51;
    L_0x0a18:
        r0 = r22;
        r3 = r0.zzati;	 Catch:{ all -> 0x01c5 }
        r3 = r3[r2];	 Catch:{ all -> 0x01c5 }
        r4 = r3.zzatb;	 Catch:{ all -> 0x01c5 }
        r4 = r4.longValue();	 Catch:{ all -> 0x01c5 }
        r0 = r22;
        r6 = r0.zzatl;	 Catch:{ all -> 0x01c5 }
        r6 = r6.longValue();	 Catch:{ all -> 0x01c5 }
        r4 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r4 >= 0) goto L_0x0a36;
    L_0x0a30:
        r4 = r3.zzatb;	 Catch:{ all -> 0x01c5 }
        r0 = r22;
        r0.zzatl = r4;	 Catch:{ all -> 0x01c5 }
    L_0x0a36:
        r4 = r3.zzatb;	 Catch:{ all -> 0x01c5 }
        r4 = r4.longValue();	 Catch:{ all -> 0x01c5 }
        r0 = r22;
        r6 = r0.zzatm;	 Catch:{ all -> 0x01c5 }
        r6 = r6.longValue();	 Catch:{ all -> 0x01c5 }
        r4 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r4 <= 0) goto L_0x0a4e;
    L_0x0a48:
        r3 = r3.zzatb;	 Catch:{ all -> 0x01c5 }
        r0 = r22;
        r0.zzatm = r3;	 Catch:{ all -> 0x01c5 }
    L_0x0a4e:
        r2 = r2 + 1;
        goto L_0x0a11;
    L_0x0a51:
        r0 = r21;
        r2 = r0.zzaqv;	 Catch:{ all -> 0x01c5 }
        r6 = r2.zzti;	 Catch:{ all -> 0x01c5 }
        r2 = r30.zzix();	 Catch:{ all -> 0x01c5 }
        r7 = r2.zzbc(r6);	 Catch:{ all -> 0x01c5 }
        if (r7 != 0) goto L_0x0aef;
    L_0x0a61:
        r2 = r30.zzge();	 Catch:{ all -> 0x01c5 }
        r2 = r2.zzim();	 Catch:{ all -> 0x01c5 }
        r3 = "Bundling raw events w/o app info. appId";
        r0 = r21;
        r4 = r0.zzaqv;	 Catch:{ all -> 0x01c5 }
        r4 = r4.zzti;	 Catch:{ all -> 0x01c5 }
        r4 = com.google.android.gms.internal.measurement.zzfg.zzbm(r4);	 Catch:{ all -> 0x01c5 }
        r2.zzg(r3, r4);	 Catch:{ all -> 0x01c5 }
    L_0x0a79:
        r0 = r22;
        r2 = r0.zzati;	 Catch:{ all -> 0x01c5 }
        r2 = r2.length;	 Catch:{ all -> 0x01c5 }
        if (r2 <= 0) goto L_0x0ab5;
    L_0x0a80:
        r2 = r30.zzkm();	 Catch:{ all -> 0x01c5 }
        r0 = r21;
        r3 = r0.zzaqv;	 Catch:{ all -> 0x01c5 }
        r3 = r3.zzti;	 Catch:{ all -> 0x01c5 }
        r2 = r2.zzbu(r3);	 Catch:{ all -> 0x01c5 }
        if (r2 == 0) goto L_0x0a94;
    L_0x0a90:
        r3 = r2.zzasp;	 Catch:{ all -> 0x01c5 }
        if (r3 != 0) goto L_0x0b75;
    L_0x0a94:
        r0 = r21;
        r2 = r0.zzaqv;	 Catch:{ all -> 0x01c5 }
        r2 = r2.zzadm;	 Catch:{ all -> 0x01c5 }
        r2 = android.text.TextUtils.isEmpty(r2);	 Catch:{ all -> 0x01c5 }
        if (r2 == 0) goto L_0x0b5b;
    L_0x0aa0:
        r2 = -1;
        r2 = java.lang.Long.valueOf(r2);	 Catch:{ all -> 0x01c5 }
        r0 = r22;
        r0.zzauf = r2;	 Catch:{ all -> 0x01c5 }
    L_0x0aaa:
        r2 = r30.zzix();	 Catch:{ all -> 0x01c5 }
        r0 = r22;
        r1 = r17;
        r2.zza(r0, r1);	 Catch:{ all -> 0x01c5 }
    L_0x0ab5:
        r4 = r30.zzix();	 Catch:{ all -> 0x01c5 }
        r0 = r21;
        r5 = r0.zzaqw;	 Catch:{ all -> 0x01c5 }
        com.google.android.gms.common.internal.Preconditions.checkNotNull(r5);	 Catch:{ all -> 0x01c5 }
        r4.zzab();	 Catch:{ all -> 0x01c5 }
        r4.zzch();	 Catch:{ all -> 0x01c5 }
        r7 = new java.lang.StringBuilder;	 Catch:{ all -> 0x01c5 }
        r2 = "rowid in (";
        r7.<init>(r2);	 Catch:{ all -> 0x01c5 }
        r2 = 0;
        r3 = r2;
    L_0x0ad0:
        r2 = r5.size();	 Catch:{ all -> 0x01c5 }
        if (r3 >= r2) goto L_0x0b7d;
    L_0x0ad6:
        if (r3 == 0) goto L_0x0ade;
    L_0x0ad8:
        r2 = ",";
        r7.append(r2);	 Catch:{ all -> 0x01c5 }
    L_0x0ade:
        r2 = r5.get(r3);	 Catch:{ all -> 0x01c5 }
        r2 = (java.lang.Long) r2;	 Catch:{ all -> 0x01c5 }
        r8 = r2.longValue();	 Catch:{ all -> 0x01c5 }
        r7.append(r8);	 Catch:{ all -> 0x01c5 }
        r2 = r3 + 1;
        r3 = r2;
        goto L_0x0ad0;
    L_0x0aef:
        r0 = r22;
        r2 = r0.zzati;	 Catch:{ all -> 0x01c5 }
        r2 = r2.length;	 Catch:{ all -> 0x01c5 }
        if (r2 <= 0) goto L_0x0a79;
    L_0x0af6:
        r2 = r7.zzgl();	 Catch:{ all -> 0x01c5 }
        r4 = 0;
        r4 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
        if (r4 == 0) goto L_0x0b57;
    L_0x0b00:
        r4 = java.lang.Long.valueOf(r2);	 Catch:{ all -> 0x01c5 }
    L_0x0b04:
        r0 = r22;
        r0.zzato = r4;	 Catch:{ all -> 0x01c5 }
        r4 = r7.zzgk();	 Catch:{ all -> 0x01c5 }
        r8 = 0;
        r8 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1));
        if (r8 != 0) goto L_0x0c09;
    L_0x0b12:
        r4 = 0;
        r4 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
        if (r4 == 0) goto L_0x0b59;
    L_0x0b18:
        r2 = java.lang.Long.valueOf(r2);	 Catch:{ all -> 0x01c5 }
    L_0x0b1c:
        r0 = r22;
        r0.zzatn = r2;	 Catch:{ all -> 0x01c5 }
        r7.zzgt();	 Catch:{ all -> 0x01c5 }
        r2 = r7.zzgq();	 Catch:{ all -> 0x01c5 }
        r2 = (int) r2;	 Catch:{ all -> 0x01c5 }
        r2 = java.lang.Integer.valueOf(r2);	 Catch:{ all -> 0x01c5 }
        r0 = r22;
        r0.zzaty = r2;	 Catch:{ all -> 0x01c5 }
        r0 = r22;
        r2 = r0.zzatl;	 Catch:{ all -> 0x01c5 }
        r2 = r2.longValue();	 Catch:{ all -> 0x01c5 }
        r7.zzm(r2);	 Catch:{ all -> 0x01c5 }
        r0 = r22;
        r2 = r0.zzatm;	 Catch:{ all -> 0x01c5 }
        r2 = r2.longValue();	 Catch:{ all -> 0x01c5 }
        r7.zzn(r2);	 Catch:{ all -> 0x01c5 }
        r2 = r7.zzhb();	 Catch:{ all -> 0x01c5 }
        r0 = r22;
        r0.zzaek = r2;	 Catch:{ all -> 0x01c5 }
        r2 = r30.zzix();	 Catch:{ all -> 0x01c5 }
        r2.zza(r7);	 Catch:{ all -> 0x01c5 }
        goto L_0x0a79;
    L_0x0b57:
        r4 = 0;
        goto L_0x0b04;
    L_0x0b59:
        r2 = 0;
        goto L_0x0b1c;
    L_0x0b5b:
        r2 = r30.zzge();	 Catch:{ all -> 0x01c5 }
        r2 = r2.zzip();	 Catch:{ all -> 0x01c5 }
        r3 = "Did not find measurement config or missing version info. appId";
        r0 = r21;
        r4 = r0.zzaqv;	 Catch:{ all -> 0x01c5 }
        r4 = r4.zzti;	 Catch:{ all -> 0x01c5 }
        r4 = com.google.android.gms.internal.measurement.zzfg.zzbm(r4);	 Catch:{ all -> 0x01c5 }
        r2.zzg(r3, r4);	 Catch:{ all -> 0x01c5 }
        goto L_0x0aaa;
    L_0x0b75:
        r2 = r2.zzasp;	 Catch:{ all -> 0x01c5 }
        r0 = r22;
        r0.zzauf = r2;	 Catch:{ all -> 0x01c5 }
        goto L_0x0aaa;
    L_0x0b7d:
        r2 = ")";
        r7.append(r2);	 Catch:{ all -> 0x01c5 }
        r2 = r4.getWritableDatabase();	 Catch:{ all -> 0x01c5 }
        r3 = "raw_events";
        r7 = r7.toString();	 Catch:{ all -> 0x01c5 }
        r8 = 0;
        r2 = r2.delete(r3, r7, r8);	 Catch:{ all -> 0x01c5 }
        r3 = r5.size();	 Catch:{ all -> 0x01c5 }
        if (r2 == r3) goto L_0x0bb3;
    L_0x0b99:
        r3 = r4.zzge();	 Catch:{ all -> 0x01c5 }
        r3 = r3.zzim();	 Catch:{ all -> 0x01c5 }
        r4 = "Deleted fewer rows from raw events table than expected";
        r2 = java.lang.Integer.valueOf(r2);	 Catch:{ all -> 0x01c5 }
        r5 = r5.size();	 Catch:{ all -> 0x01c5 }
        r5 = java.lang.Integer.valueOf(r5);	 Catch:{ all -> 0x01c5 }
        r3.zze(r4, r2, r5);	 Catch:{ all -> 0x01c5 }
    L_0x0bb3:
        r3 = r30.zzix();	 Catch:{ all -> 0x01c5 }
        r2 = r3.getWritableDatabase();	 Catch:{ all -> 0x01c5 }
        r4 = "delete from raw_events_metadata where app_id=? and metadata_fingerprint not in (select distinct metadata_fingerprint from raw_events where app_id=?)";
        r5 = 2;
        r5 = new java.lang.String[r5];	 Catch:{ SQLiteException -> 0x0bda }
        r7 = 0;
        r5[r7] = r6;	 Catch:{ SQLiteException -> 0x0bda }
        r7 = 1;
        r5[r7] = r6;	 Catch:{ SQLiteException -> 0x0bda }
        r2.execSQL(r4, r5);	 Catch:{ SQLiteException -> 0x0bda }
    L_0x0bca:
        r2 = r30.zzix();	 Catch:{ all -> 0x01c5 }
        r2.setTransactionSuccessful();	 Catch:{ all -> 0x01c5 }
        r2 = r30.zzix();
        r2.endTransaction();
        r2 = 1;
    L_0x0bd9:
        return r2;
    L_0x0bda:
        r2 = move-exception;
        r3 = r3.zzge();	 Catch:{ all -> 0x01c5 }
        r3 = r3.zzim();	 Catch:{ all -> 0x01c5 }
        r4 = "Failed to remove unused event metadata. appId";
        r5 = com.google.android.gms.internal.measurement.zzfg.zzbm(r6);	 Catch:{ all -> 0x01c5 }
        r3.zze(r4, r5, r2);	 Catch:{ all -> 0x01c5 }
        goto L_0x0bca;
    L_0x0bee:
        r2 = r30.zzix();	 Catch:{ all -> 0x01c5 }
        r2.setTransactionSuccessful();	 Catch:{ all -> 0x01c5 }
        r2 = r30.zzix();
        r2.endTransaction();
        r2 = 0;
        goto L_0x0bd9;
    L_0x0bfe:
        r2 = move-exception;
        r3 = r11;
        goto L_0x036c;
    L_0x0c02:
        r2 = move-exception;
        goto L_0x02f9;
    L_0x0c05:
        r2 = move-exception;
        r4 = r12;
        goto L_0x02f9;
    L_0x0c09:
        r2 = r4;
        goto L_0x0b12;
    L_0x0c0c:
        r3 = r2;
        goto L_0x0898;
    L_0x0c0f:
        r20 = r2;
        goto L_0x07e1;
    L_0x0c13:
        r2 = r14;
        goto L_0x057a;
    L_0x0c16:
        r2 = r3;
        goto L_0x051f;
    L_0x0c19:
        r17 = r2;
        goto L_0x04b9;
    L_0x0c1d:
        r2 = r3;
        goto L_0x03be;
    L_0x0c20:
        r2 = r14;
        r4 = r13;
        r5 = r17;
        goto L_0x0156;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzjr.zzd(java.lang.String, long):boolean");
    }

    private final zzdy zzg(zzdz com_google_android_gms_internal_measurement_zzdz) {
        Object obj = 1;
        zzab();
        zzkq();
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzdz);
        Preconditions.checkNotEmpty(com_google_android_gms_internal_measurement_zzdz.packageName);
        zzdy zzbc = zzix().zzbc(com_google_android_gms_internal_measurement_zzdz.packageName);
        String zzbp = zzgf().zzbp(com_google_android_gms_internal_measurement_zzdz.packageName);
        Object obj2 = null;
        if (zzbc == null) {
            zzbc = new zzdy(this.zzacw, com_google_android_gms_internal_measurement_zzdz.packageName);
            zzbc.zzal(this.zzacw.zzfv().zzii());
            zzbc.zzan(zzbp);
            obj2 = 1;
        } else if (!zzbp.equals(zzbc.zzgi())) {
            zzbc.zzan(zzbp);
            zzbc.zzal(this.zzacw.zzfv().zzii());
            int i = 1;
        }
        if (!(TextUtils.isEmpty(com_google_android_gms_internal_measurement_zzdz.zzadm) || com_google_android_gms_internal_measurement_zzdz.zzadm.equals(zzbc.getGmpAppId()))) {
            zzbc.zzam(com_google_android_gms_internal_measurement_zzdz.zzadm);
            obj2 = 1;
        }
        if (!(TextUtils.isEmpty(com_google_android_gms_internal_measurement_zzdz.zzado) || com_google_android_gms_internal_measurement_zzdz.zzado.equals(zzbc.zzgj()))) {
            zzbc.zzao(com_google_android_gms_internal_measurement_zzdz.zzado);
            obj2 = 1;
        }
        if (!(com_google_android_gms_internal_measurement_zzdz.zzadu == 0 || com_google_android_gms_internal_measurement_zzdz.zzadu == zzbc.zzgo())) {
            zzbc.zzp(com_google_android_gms_internal_measurement_zzdz.zzadu);
            obj2 = 1;
        }
        if (!(TextUtils.isEmpty(com_google_android_gms_internal_measurement_zzdz.zzth) || com_google_android_gms_internal_measurement_zzdz.zzth.equals(zzbc.zzag()))) {
            zzbc.setAppVersion(com_google_android_gms_internal_measurement_zzdz.zzth);
            obj2 = 1;
        }
        if (com_google_android_gms_internal_measurement_zzdz.zzads != zzbc.zzgm()) {
            zzbc.zzo(com_google_android_gms_internal_measurement_zzdz.zzads);
            obj2 = 1;
        }
        if (!(com_google_android_gms_internal_measurement_zzdz.zzadt == null || com_google_android_gms_internal_measurement_zzdz.zzadt.equals(zzbc.zzgn()))) {
            zzbc.zzap(com_google_android_gms_internal_measurement_zzdz.zzadt);
            obj2 = 1;
        }
        if (com_google_android_gms_internal_measurement_zzdz.zzadv != zzbc.zzgp()) {
            zzbc.zzq(com_google_android_gms_internal_measurement_zzdz.zzadv);
            obj2 = 1;
        }
        if (com_google_android_gms_internal_measurement_zzdz.zzadw != zzbc.isMeasurementEnabled()) {
            zzbc.setMeasurementEnabled(com_google_android_gms_internal_measurement_zzdz.zzadw);
            obj2 = 1;
        }
        if (!(TextUtils.isEmpty(com_google_android_gms_internal_measurement_zzdz.zzaek) || com_google_android_gms_internal_measurement_zzdz.zzaek.equals(zzbc.zzha()))) {
            zzbc.zzaq(com_google_android_gms_internal_measurement_zzdz.zzaek);
            obj2 = 1;
        }
        if (com_google_android_gms_internal_measurement_zzdz.zzadx != zzbc.zzhc()) {
            zzbc.zzaa(com_google_android_gms_internal_measurement_zzdz.zzadx);
            obj2 = 1;
        }
        if (com_google_android_gms_internal_measurement_zzdz.zzady != zzbc.zzhd()) {
            zzbc.zzd(com_google_android_gms_internal_measurement_zzdz.zzady);
            obj2 = 1;
        }
        if (com_google_android_gms_internal_measurement_zzdz.zzadz != zzbc.zzhe()) {
            zzbc.zze(com_google_android_gms_internal_measurement_zzdz.zzadz);
        } else {
            obj = obj2;
        }
        if (obj != null) {
            zzix().zza(zzbc);
        }
        return zzbc;
    }

    private final zzgf zzkm() {
        zza(this.zzaqa);
        return this.zzaqa;
    }

    private final zzfp zzko() {
        if (this.zzaqd != null) {
            return this.zzaqd;
        }
        throw new IllegalStateException("Network broadcast receiver not created");
    }

    private final zzjn zzkp() {
        zza(this.zzaqe);
        return this.zzaqe;
    }

    private final long zzkr() {
        long currentTimeMillis = zzbt().currentTimeMillis();
        zzhg zzgf = zzgf();
        zzgf.zzch();
        zzgf.zzab();
        long j = zzgf.zzajy.get();
        if (j == 0) {
            j = 1 + ((long) zzgf.zzgb().zzlc().nextInt(86400000));
            zzgf.zzajy.set(j);
        }
        return ((((j + currentTimeMillis) / 1000) / 60) / 60) / 24;
    }

    private final boolean zzkt() {
        zzab();
        zzkq();
        return zzix().zzhs() || !TextUtils.isEmpty(zzix().zzhn());
    }

    private final void zzku() {
        zzab();
        zzkq();
        if (zzky()) {
            long abs;
            if (this.zzaqh > 0) {
                abs = 3600000 - Math.abs(zzbt().elapsedRealtime() - this.zzaqh);
                if (abs > 0) {
                    zzge().zzit().zzg("Upload has been suspended. Will update scheduling later in approximately ms", Long.valueOf(abs));
                    zzko().unregister();
                    zzkp().cancel();
                    return;
                }
                this.zzaqh = 0;
            }
            if (this.zzacw.zzjv() && zzkt()) {
                long currentTimeMillis = zzbt().currentTimeMillis();
                long max = Math.max(0, ((Long) zzew.zzahi.get()).longValue());
                Object obj = (zzix().zzht() || zzix().zzho()) ? 1 : null;
                if (obj != null) {
                    CharSequence zzhj = zzgg().zzhj();
                    abs = (TextUtils.isEmpty(zzhj) || ".none.".equals(zzhj)) ? Math.max(0, ((Long) zzew.zzahc.get()).longValue()) : Math.max(0, ((Long) zzew.zzahd.get()).longValue());
                } else {
                    abs = Math.max(0, ((Long) zzew.zzahb.get()).longValue());
                }
                long j = zzgf().zzaju.get();
                long j2 = zzgf().zzajv.get();
                long max2 = Math.max(zzix().zzhq(), zzix().zzhr());
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
                    if (!zzgb().zza(j, abs)) {
                        currentTimeMillis = j + abs;
                    }
                    if (j2 != 0 && j2 >= max2) {
                        for (int i = 0; i < Math.min(20, Math.max(0, ((Integer) zzew.zzahk.get()).intValue())); i++) {
                            currentTimeMillis += (1 << i) * Math.max(0, ((Long) zzew.zzahj.get()).longValue());
                            if (currentTimeMillis > j2) {
                                break;
                            }
                        }
                        currentTimeMillis = 0;
                    }
                }
                if (currentTimeMillis == 0) {
                    zzge().zzit().log("Next upload time is 0");
                    zzko().unregister();
                    zzkp().cancel();
                    return;
                } else if (zzkn().zzex()) {
                    long j3 = zzgf().zzajw.get();
                    abs = Math.max(0, ((Long) zzew.zzagz.get()).longValue());
                    abs = !zzgb().zza(j3, abs) ? Math.max(currentTimeMillis, abs + j3) : currentTimeMillis;
                    zzko().unregister();
                    abs -= zzbt().currentTimeMillis();
                    if (abs <= 0) {
                        abs = Math.max(0, ((Long) zzew.zzahe.get()).longValue());
                        zzgf().zzaju.set(zzbt().currentTimeMillis());
                    }
                    zzge().zzit().zzg("Upload scheduled in approximately ms", Long.valueOf(abs));
                    zzkp().zzh(abs);
                    return;
                } else {
                    zzge().zzit().log("No network");
                    zzko().zzeu();
                    zzkp().cancel();
                    return;
                }
            }
            zzge().zzit().log("Nothing to upload or uploading impossible");
            zzko().unregister();
            zzkp().cancel();
        }
    }

    private final void zzkv() {
        zzab();
        if (this.zzaql || this.zzaqm || this.zzaqn) {
            zzge().zzit().zzd("Not stopping services. fetch, network, upload", Boolean.valueOf(this.zzaql), Boolean.valueOf(this.zzaqm), Boolean.valueOf(this.zzaqn));
            return;
        }
        zzge().zzit().log("Stopping uploading service(s)");
        if (this.zzaqi != null) {
            for (Runnable run : this.zzaqi) {
                run.run();
            }
            this.zzaqi.clear();
        }
    }

    private final boolean zzkw() {
        zzab();
        try {
            this.zzaqp = new RandomAccessFile(new File(getContext().getFilesDir(), "google_app_measurement.db"), "rw").getChannel();
            this.zzaqo = this.zzaqp.tryLock();
            if (this.zzaqo != null) {
                zzge().zzit().log("Storage concurrent access okay");
                return true;
            }
            zzge().zzim().log("Storage concurrent data access panic");
            return false;
        } catch (FileNotFoundException e) {
            zzge().zzim().zzg("Failed to acquire storage lock", e);
        } catch (IOException e2) {
            zzge().zzim().zzg("Failed to access storage lock file", e2);
        }
    }

    private final boolean zzky() {
        zzab();
        zzkq();
        return this.zzaqg;
    }

    public Context getContext() {
        return this.zzacw.getContext();
    }

    protected void start() {
        zzab();
        zzix().zzhp();
        if (zzgf().zzaju.get() == 0) {
            zzgf().zzaju.set(zzbt().currentTimeMillis());
        }
        zzku();
    }

    protected final void zza(int i, Throwable th, byte[] bArr, String str) {
        zzab();
        zzkq();
        if (bArr == null) {
            try {
                bArr = new byte[0];
            } catch (Throwable th2) {
                this.zzaqm = false;
                zzkv();
            }
        }
        List<Long> list = this.zzaqq;
        this.zzaqq = null;
        if ((i == Callback.DEFAULT_DRAG_ANIMATION_DURATION || i == 204) && th == null) {
            try {
                zzgf().zzaju.set(zzbt().currentTimeMillis());
                zzgf().zzajv.set(0);
                zzku();
                zzge().zzit().zze("Successful upload. Got network response. code, size", Integer.valueOf(i), Integer.valueOf(bArr.length));
                zzix().beginTransaction();
                try {
                    for (Long l : list) {
                        zzhg zzix;
                        try {
                            zzix = zzix();
                            long longValue = l.longValue();
                            zzix.zzab();
                            zzix.zzch();
                            if (zzix.getWritableDatabase().delete("queue", "rowid=?", new String[]{String.valueOf(longValue)}) != 1) {
                                throw new SQLiteException("Deleted fewer rows from queue than expected");
                            }
                        } catch (SQLiteException e) {
                            zzix.zzge().zzim().zzg("Failed to delete a bundle in a queue table", e);
                            throw e;
                        } catch (SQLiteException e2) {
                            if (this.zzaqr == null || !this.zzaqr.contains(l)) {
                                throw e2;
                            }
                        }
                    }
                    zzix().setTransactionSuccessful();
                    this.zzaqr = null;
                    if (zzkn().zzex() && zzkt()) {
                        zzks();
                    } else {
                        this.zzaqs = -1;
                        zzku();
                    }
                    this.zzaqh = 0;
                } finally {
                    zzix().endTransaction();
                }
            } catch (SQLiteException e3) {
                zzge().zzim().zzg("Database error while trying to delete uploaded bundles", e3);
                this.zzaqh = zzbt().elapsedRealtime();
                zzge().zzit().zzg("Disable upload, time", Long.valueOf(this.zzaqh));
            }
        } else {
            zzge().zzit().zze("Network upload failed. Will retry later. code, error", Integer.valueOf(i), th);
            zzgf().zzajv.set(zzbt().currentTimeMillis());
            boolean z = i == 503 || i == 429;
            if (z) {
                zzgf().zzajw.set(zzbt().currentTimeMillis());
            }
            if (zzgg().zzax(str)) {
                zzix().zzc(list);
            }
            zzku();
        }
        this.zzaqm = false;
        zzkv();
    }

    final void zza(zzgl com_google_android_gms_internal_measurement_zzgl) {
        this.zzacw = com_google_android_gms_internal_measurement_zzgl;
    }

    final void zza(zzjw com_google_android_gms_internal_measurement_zzjw) {
        zzab();
        zzjq com_google_android_gms_internal_measurement_zzei = new zzei(this.zzacw);
        com_google_android_gms_internal_measurement_zzei.zzm();
        this.zzaqc = com_google_android_gms_internal_measurement_zzei;
        zzgg().zza(this.zzaqa);
        com_google_android_gms_internal_measurement_zzei = new zzeb(this.zzacw);
        com_google_android_gms_internal_measurement_zzei.zzm();
        this.zzaqf = com_google_android_gms_internal_measurement_zzei;
        com_google_android_gms_internal_measurement_zzei = new zzjn(this.zzacw);
        com_google_android_gms_internal_measurement_zzei.zzm();
        this.zzaqe = com_google_android_gms_internal_measurement_zzei;
        this.zzaqd = new zzfp(this.zzacw);
        if (this.zzaqj != this.zzaqk) {
            zzge().zzim().zze("Not all upload components initialized", Integer.valueOf(this.zzaqj), Integer.valueOf(this.zzaqk));
        }
        this.zzvo = true;
    }

    public final byte[] zza(zzeu com_google_android_gms_internal_measurement_zzeu, String str) {
        zzkq();
        zzab();
        zzgl.zzfr();
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzeu);
        Preconditions.checkNotEmpty(str);
        zzace com_google_android_gms_internal_measurement_zzkp = new zzkp();
        zzix().beginTransaction();
        try {
            zzdy zzbc = zzix().zzbc(str);
            byte[] bArr;
            if (zzbc == null) {
                zzge().zzis().zzg("Log and bundle not available. package_name", str);
                bArr = new byte[0];
                return bArr;
            } else if (zzbc.isMeasurementEnabled()) {
                zzks com_google_android_gms_internal_measurement_zzks;
                long j;
                if (("_iap".equals(com_google_android_gms_internal_measurement_zzeu.name) || "ecommerce_purchase".equals(com_google_android_gms_internal_measurement_zzeu.name)) && !zza(str, com_google_android_gms_internal_measurement_zzeu)) {
                    zzge().zzip().zzg("Failed to handle purchase event at single event bundle creation. appId", zzfg.zzbm(str));
                }
                boolean zzav = zzgg().zzav(str);
                Long valueOf = Long.valueOf(0);
                if (zzav && "_e".equals(com_google_android_gms_internal_measurement_zzeu.name)) {
                    if (com_google_android_gms_internal_measurement_zzeu.zzafq == null || com_google_android_gms_internal_measurement_zzeu.zzafq.size() == 0) {
                        zzge().zzip().zzg("The engagement event does not contain any parameters. appId", zzfg.zzbm(str));
                    } else if (com_google_android_gms_internal_measurement_zzeu.zzafq.getLong("_et") == null) {
                        zzge().zzip().zzg("The engagement event does not include duration. appId", zzfg.zzbm(str));
                    } else {
                        valueOf = com_google_android_gms_internal_measurement_zzeu.zzafq.getLong("_et");
                    }
                }
                zzkq com_google_android_gms_internal_measurement_zzkq = new zzkq();
                com_google_android_gms_internal_measurement_zzkp.zzatf = new zzkq[]{com_google_android_gms_internal_measurement_zzkq};
                com_google_android_gms_internal_measurement_zzkq.zzath = Integer.valueOf(1);
                com_google_android_gms_internal_measurement_zzkq.zzatp = "android";
                com_google_android_gms_internal_measurement_zzkq.zzti = zzbc.zzah();
                com_google_android_gms_internal_measurement_zzkq.zzadt = zzbc.zzgn();
                com_google_android_gms_internal_measurement_zzkq.zzth = zzbc.zzag();
                long zzgm = zzbc.zzgm();
                com_google_android_gms_internal_measurement_zzkq.zzaub = zzgm == -2147483648L ? null : Integer.valueOf((int) zzgm);
                com_google_android_gms_internal_measurement_zzkq.zzatt = Long.valueOf(zzbc.zzgo());
                com_google_android_gms_internal_measurement_zzkq.zzadm = zzbc.getGmpAppId();
                com_google_android_gms_internal_measurement_zzkq.zzatx = Long.valueOf(zzbc.zzgp());
                if (this.zzacw.isEnabled() && zzef.zzhk() && zzgg().zzat(com_google_android_gms_internal_measurement_zzkq.zzti)) {
                    com_google_android_gms_internal_measurement_zzkq.zzauh = null;
                }
                Pair zzbo = zzgf().zzbo(zzbc.zzah());
                if (!(!zzbc.zzhd() || zzbo == null || TextUtils.isEmpty((CharSequence) zzbo.first))) {
                    com_google_android_gms_internal_measurement_zzkq.zzatv = (String) zzbo.first;
                    com_google_android_gms_internal_measurement_zzkq.zzatw = (Boolean) zzbo.second;
                }
                zzfw().zzch();
                com_google_android_gms_internal_measurement_zzkq.zzatr = Build.MODEL;
                zzfw().zzch();
                com_google_android_gms_internal_measurement_zzkq.zzatq = VERSION.RELEASE;
                com_google_android_gms_internal_measurement_zzkq.zzats = Integer.valueOf((int) zzfw().zzic());
                com_google_android_gms_internal_measurement_zzkq.zzafn = zzfw().zzid();
                com_google_android_gms_internal_measurement_zzkq.zzadl = zzbc.getAppInstanceId();
                com_google_android_gms_internal_measurement_zzkq.zzado = zzbc.zzgj();
                List zzbb = zzix().zzbb(zzbc.zzah());
                com_google_android_gms_internal_measurement_zzkq.zzatj = new zzks[zzbb.size()];
                zzjz com_google_android_gms_internal_measurement_zzjz = null;
                if (zzav) {
                    zzjz zzh = zzix().zzh(com_google_android_gms_internal_measurement_zzkq.zzti, "_lte");
                    com_google_android_gms_internal_measurement_zzjz = (zzh == null || zzh.value == null) ? new zzjz(com_google_android_gms_internal_measurement_zzkq.zzti, "auto", "_lte", zzbt().currentTimeMillis(), valueOf) : valueOf.longValue() > 0 ? new zzjz(com_google_android_gms_internal_measurement_zzkq.zzti, "auto", "_lte", zzbt().currentTimeMillis(), Long.valueOf(((Long) zzh.value).longValue() + valueOf.longValue())) : zzh;
                }
                zzks com_google_android_gms_internal_measurement_zzks2 = null;
                int i = 0;
                while (i < zzbb.size()) {
                    zzks com_google_android_gms_internal_measurement_zzks3;
                    com_google_android_gms_internal_measurement_zzks = new zzks();
                    com_google_android_gms_internal_measurement_zzkq.zzatj[i] = com_google_android_gms_internal_measurement_zzks;
                    com_google_android_gms_internal_measurement_zzks.name = ((zzjz) zzbb.get(i)).name;
                    com_google_android_gms_internal_measurement_zzks.zzaun = Long.valueOf(((zzjz) zzbb.get(i)).zzaqz);
                    zzgb().zza(com_google_android_gms_internal_measurement_zzks, ((zzjz) zzbb.get(i)).value);
                    if (zzav && "_lte".equals(com_google_android_gms_internal_measurement_zzks.name)) {
                        com_google_android_gms_internal_measurement_zzks.zzate = (Long) com_google_android_gms_internal_measurement_zzjz.value;
                        com_google_android_gms_internal_measurement_zzks.zzaun = Long.valueOf(zzbt().currentTimeMillis());
                        com_google_android_gms_internal_measurement_zzks3 = com_google_android_gms_internal_measurement_zzks;
                    } else {
                        com_google_android_gms_internal_measurement_zzks3 = com_google_android_gms_internal_measurement_zzks2;
                    }
                    i++;
                    com_google_android_gms_internal_measurement_zzks2 = com_google_android_gms_internal_measurement_zzks3;
                }
                if (zzav && com_google_android_gms_internal_measurement_zzks2 == null) {
                    com_google_android_gms_internal_measurement_zzks = new zzks();
                    com_google_android_gms_internal_measurement_zzks.name = "_lte";
                    com_google_android_gms_internal_measurement_zzks.zzaun = Long.valueOf(zzbt().currentTimeMillis());
                    com_google_android_gms_internal_measurement_zzks.zzate = (Long) com_google_android_gms_internal_measurement_zzjz.value;
                    com_google_android_gms_internal_measurement_zzkq.zzatj = (zzks[]) Arrays.copyOf(com_google_android_gms_internal_measurement_zzkq.zzatj, com_google_android_gms_internal_measurement_zzkq.zzatj.length + 1);
                    com_google_android_gms_internal_measurement_zzkq.zzatj[com_google_android_gms_internal_measurement_zzkq.zzatj.length - 1] = com_google_android_gms_internal_measurement_zzks;
                }
                if (valueOf.longValue() > 0) {
                    zzix().zza(com_google_android_gms_internal_measurement_zzjz);
                }
                Bundle zzif = com_google_android_gms_internal_measurement_zzeu.zzafq.zzif();
                if ("_iap".equals(com_google_android_gms_internal_measurement_zzeu.name)) {
                    zzif.putLong("_c", 1);
                    zzge().zzis().log("Marking in-app purchase as real-time");
                    zzif.putLong("_r", 1);
                }
                zzif.putString("_o", com_google_android_gms_internal_measurement_zzeu.origin);
                if (zzgb().zzcj(com_google_android_gms_internal_measurement_zzkq.zzti)) {
                    zzgb().zza(zzif, "_dbg", Long.valueOf(1));
                    zzgb().zza(zzif, "_r", Long.valueOf(1));
                }
                zzeq zzf = zzix().zzf(str, com_google_android_gms_internal_measurement_zzeu.name);
                if (zzf == null) {
                    zzix().zza(new zzeq(str, com_google_android_gms_internal_measurement_zzeu.name, 1, 0, com_google_android_gms_internal_measurement_zzeu.zzagb, 0, null, null, null));
                    j = 0;
                } else {
                    j = zzf.zzaft;
                    zzix().zza(zzf.zzac(com_google_android_gms_internal_measurement_zzeu.zzagb).zzie());
                }
                zzep com_google_android_gms_internal_measurement_zzep = new zzep(this.zzacw, com_google_android_gms_internal_measurement_zzeu.origin, str, com_google_android_gms_internal_measurement_zzeu.name, com_google_android_gms_internal_measurement_zzeu.zzagb, j, zzif);
                zzkn com_google_android_gms_internal_measurement_zzkn = new zzkn();
                com_google_android_gms_internal_measurement_zzkq.zzati = new zzkn[]{com_google_android_gms_internal_measurement_zzkn};
                com_google_android_gms_internal_measurement_zzkn.zzatb = Long.valueOf(com_google_android_gms_internal_measurement_zzep.timestamp);
                com_google_android_gms_internal_measurement_zzkn.name = com_google_android_gms_internal_measurement_zzep.name;
                com_google_android_gms_internal_measurement_zzkn.zzatc = Long.valueOf(com_google_android_gms_internal_measurement_zzep.zzafp);
                com_google_android_gms_internal_measurement_zzkn.zzata = new zzko[com_google_android_gms_internal_measurement_zzep.zzafq.size()];
                Iterator it = com_google_android_gms_internal_measurement_zzep.zzafq.iterator();
                int i2 = 0;
                while (it.hasNext()) {
                    String str2 = (String) it.next();
                    zzko com_google_android_gms_internal_measurement_zzko = new zzko();
                    i = i2 + 1;
                    com_google_android_gms_internal_measurement_zzkn.zzata[i2] = com_google_android_gms_internal_measurement_zzko;
                    com_google_android_gms_internal_measurement_zzko.name = str2;
                    zzgb().zza(com_google_android_gms_internal_measurement_zzko, com_google_android_gms_internal_measurement_zzep.zzafq.get(str2));
                    i2 = i;
                }
                com_google_android_gms_internal_measurement_zzkq.zzaua = zza(zzbc.zzah(), com_google_android_gms_internal_measurement_zzkq.zzatj, com_google_android_gms_internal_measurement_zzkq.zzati);
                com_google_android_gms_internal_measurement_zzkq.zzatl = com_google_android_gms_internal_measurement_zzkn.zzatb;
                com_google_android_gms_internal_measurement_zzkq.zzatm = com_google_android_gms_internal_measurement_zzkn.zzatb;
                zzgm = zzbc.zzgl();
                com_google_android_gms_internal_measurement_zzkq.zzato = zzgm != 0 ? Long.valueOf(zzgm) : null;
                long zzgk = zzbc.zzgk();
                if (zzgk != 0) {
                    zzgm = zzgk;
                }
                com_google_android_gms_internal_measurement_zzkq.zzatn = zzgm != 0 ? Long.valueOf(zzgm) : null;
                zzbc.zzgt();
                com_google_android_gms_internal_measurement_zzkq.zzaty = Integer.valueOf((int) zzbc.zzgq());
                com_google_android_gms_internal_measurement_zzkq.zzatu = Long.valueOf(12451);
                com_google_android_gms_internal_measurement_zzkq.zzatk = Long.valueOf(zzbt().currentTimeMillis());
                com_google_android_gms_internal_measurement_zzkq.zzatz = Boolean.TRUE;
                zzbc.zzm(com_google_android_gms_internal_measurement_zzkq.zzatl.longValue());
                zzbc.zzn(com_google_android_gms_internal_measurement_zzkq.zzatm.longValue());
                zzix().zza(zzbc);
                zzix().setTransactionSuccessful();
                zzix().endTransaction();
                try {
                    bArr = new byte[com_google_android_gms_internal_measurement_zzkp.zzvm()];
                    zzabw zzb = zzabw.zzb(bArr, 0, bArr.length);
                    com_google_android_gms_internal_measurement_zzkp.zza(zzb);
                    zzb.zzve();
                    return zzgb().zza(bArr);
                } catch (IOException e) {
                    zzge().zzim().zze("Data loss. Failed to bundle and serialize. appId", zzfg.zzbm(str), e);
                    return null;
                }
            } else {
                zzge().zzis().zzg("Log and bundle disabled. package_name", str);
                bArr = new byte[0];
                zzix().endTransaction();
                return bArr;
            }
        } finally {
            zzix().endTransaction();
        }
    }

    public void zzab() {
        zzgd().zzab();
    }

    final void zzb(zzed com_google_android_gms_internal_measurement_zzed, zzdz com_google_android_gms_internal_measurement_zzdz) {
        boolean z = true;
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzed);
        Preconditions.checkNotEmpty(com_google_android_gms_internal_measurement_zzed.packageName);
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzed.origin);
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzed.zzaep);
        Preconditions.checkNotEmpty(com_google_android_gms_internal_measurement_zzed.zzaep.name);
        zzab();
        zzkq();
        if (!TextUtils.isEmpty(com_google_android_gms_internal_measurement_zzdz.zzadm)) {
            if (com_google_android_gms_internal_measurement_zzdz.zzadw) {
                zzed com_google_android_gms_internal_measurement_zzed2 = new zzed(com_google_android_gms_internal_measurement_zzed);
                com_google_android_gms_internal_measurement_zzed2.active = false;
                zzix().beginTransaction();
                try {
                    zzed zzi = zzix().zzi(com_google_android_gms_internal_measurement_zzed2.packageName, com_google_android_gms_internal_measurement_zzed2.zzaep.name);
                    if (!(zzi == null || zzi.origin.equals(com_google_android_gms_internal_measurement_zzed2.origin))) {
                        zzge().zzip().zzd("Updating a conditional user property with different origin. name, origin, origin (from DB)", zzga().zzbl(com_google_android_gms_internal_measurement_zzed2.zzaep.name), com_google_android_gms_internal_measurement_zzed2.origin, zzi.origin);
                    }
                    if (zzi != null && zzi.active) {
                        com_google_android_gms_internal_measurement_zzed2.origin = zzi.origin;
                        com_google_android_gms_internal_measurement_zzed2.creationTimestamp = zzi.creationTimestamp;
                        com_google_android_gms_internal_measurement_zzed2.triggerTimeout = zzi.triggerTimeout;
                        com_google_android_gms_internal_measurement_zzed2.triggerEventName = zzi.triggerEventName;
                        com_google_android_gms_internal_measurement_zzed2.zzaer = zzi.zzaer;
                        com_google_android_gms_internal_measurement_zzed2.active = zzi.active;
                        com_google_android_gms_internal_measurement_zzed2.zzaep = new zzjx(com_google_android_gms_internal_measurement_zzed2.zzaep.name, zzi.zzaep.zzaqz, com_google_android_gms_internal_measurement_zzed2.zzaep.getValue(), zzi.zzaep.origin);
                        z = false;
                    } else if (TextUtils.isEmpty(com_google_android_gms_internal_measurement_zzed2.triggerEventName)) {
                        com_google_android_gms_internal_measurement_zzed2.zzaep = new zzjx(com_google_android_gms_internal_measurement_zzed2.zzaep.name, com_google_android_gms_internal_measurement_zzed2.creationTimestamp, com_google_android_gms_internal_measurement_zzed2.zzaep.getValue(), com_google_android_gms_internal_measurement_zzed2.zzaep.origin);
                        com_google_android_gms_internal_measurement_zzed2.active = true;
                    } else {
                        z = false;
                    }
                    if (com_google_android_gms_internal_measurement_zzed2.active) {
                        zzjx com_google_android_gms_internal_measurement_zzjx = com_google_android_gms_internal_measurement_zzed2.zzaep;
                        zzjz com_google_android_gms_internal_measurement_zzjz = new zzjz(com_google_android_gms_internal_measurement_zzed2.packageName, com_google_android_gms_internal_measurement_zzed2.origin, com_google_android_gms_internal_measurement_zzjx.name, com_google_android_gms_internal_measurement_zzjx.zzaqz, com_google_android_gms_internal_measurement_zzjx.getValue());
                        if (zzix().zza(com_google_android_gms_internal_measurement_zzjz)) {
                            zzge().zzis().zzd("User property updated immediately", com_google_android_gms_internal_measurement_zzed2.packageName, zzga().zzbl(com_google_android_gms_internal_measurement_zzjz.name), com_google_android_gms_internal_measurement_zzjz.value);
                        } else {
                            zzge().zzim().zzd("(2)Too many active user properties, ignoring", zzfg.zzbm(com_google_android_gms_internal_measurement_zzed2.packageName), zzga().zzbl(com_google_android_gms_internal_measurement_zzjz.name), com_google_android_gms_internal_measurement_zzjz.value);
                        }
                        if (z && com_google_android_gms_internal_measurement_zzed2.zzaer != null) {
                            zzc(new zzeu(com_google_android_gms_internal_measurement_zzed2.zzaer, com_google_android_gms_internal_measurement_zzed2.creationTimestamp), com_google_android_gms_internal_measurement_zzdz);
                        }
                    }
                    if (zzix().zza(com_google_android_gms_internal_measurement_zzed2)) {
                        zzge().zzis().zzd("Conditional property added", com_google_android_gms_internal_measurement_zzed2.packageName, zzga().zzbl(com_google_android_gms_internal_measurement_zzed2.zzaep.name), com_google_android_gms_internal_measurement_zzed2.zzaep.getValue());
                    } else {
                        zzge().zzim().zzd("Too many conditional properties, ignoring", zzfg.zzbm(com_google_android_gms_internal_measurement_zzed2.packageName), zzga().zzbl(com_google_android_gms_internal_measurement_zzed2.zzaep.name), com_google_android_gms_internal_measurement_zzed2.zzaep.getValue());
                    }
                    zzix().setTransactionSuccessful();
                } finally {
                    zzix().endTransaction();
                }
            } else {
                zzg(com_google_android_gms_internal_measurement_zzdz);
            }
        }
    }

    final void zzb(zzeu com_google_android_gms_internal_measurement_zzeu, zzdz com_google_android_gms_internal_measurement_zzdz) {
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzdz);
        Preconditions.checkNotEmpty(com_google_android_gms_internal_measurement_zzdz.packageName);
        zzab();
        zzkq();
        String str = com_google_android_gms_internal_measurement_zzdz.packageName;
        long j = com_google_android_gms_internal_measurement_zzeu.zzagb;
        zzgb();
        if (!zzka.zzd(com_google_android_gms_internal_measurement_zzeu, com_google_android_gms_internal_measurement_zzdz)) {
            return;
        }
        if (com_google_android_gms_internal_measurement_zzdz.zzadw) {
            zzix().beginTransaction();
            try {
                List emptyList;
                Object obj;
                zzhg zzix = zzix();
                Preconditions.checkNotEmpty(str);
                zzix.zzab();
                zzix.zzch();
                if (j < 0) {
                    zzix.zzge().zzip().zze("Invalid time querying timed out conditional properties", zzfg.zzbm(str), Long.valueOf(j));
                    emptyList = Collections.emptyList();
                } else {
                    emptyList = zzix.zzb("active=0 and app_id=? and abs(? - creation_timestamp) > trigger_timeout", new String[]{str, String.valueOf(j)});
                }
                for (zzed com_google_android_gms_internal_measurement_zzed : r2) {
                    if (com_google_android_gms_internal_measurement_zzed != null) {
                        zzge().zzis().zzd("User property timed out", com_google_android_gms_internal_measurement_zzed.packageName, zzga().zzbl(com_google_android_gms_internal_measurement_zzed.zzaep.name), com_google_android_gms_internal_measurement_zzed.zzaep.getValue());
                        if (com_google_android_gms_internal_measurement_zzed.zzaeq != null) {
                            zzc(new zzeu(com_google_android_gms_internal_measurement_zzed.zzaeq, j), com_google_android_gms_internal_measurement_zzdz);
                        }
                        zzix().zzj(str, com_google_android_gms_internal_measurement_zzed.zzaep.name);
                    }
                }
                zzix = zzix();
                Preconditions.checkNotEmpty(str);
                zzix.zzab();
                zzix.zzch();
                if (j < 0) {
                    zzix.zzge().zzip().zze("Invalid time querying expired conditional properties", zzfg.zzbm(str), Long.valueOf(j));
                    emptyList = Collections.emptyList();
                } else {
                    emptyList = zzix.zzb("active<>0 and app_id=? and abs(? - triggered_timestamp) > time_to_live", new String[]{str, String.valueOf(j)});
                }
                List arrayList = new ArrayList(r2.size());
                for (zzed com_google_android_gms_internal_measurement_zzed2 : r2) {
                    if (com_google_android_gms_internal_measurement_zzed2 != null) {
                        zzge().zzis().zzd("User property expired", com_google_android_gms_internal_measurement_zzed2.packageName, zzga().zzbl(com_google_android_gms_internal_measurement_zzed2.zzaep.name), com_google_android_gms_internal_measurement_zzed2.zzaep.getValue());
                        zzix().zzg(str, com_google_android_gms_internal_measurement_zzed2.zzaep.name);
                        if (com_google_android_gms_internal_measurement_zzed2.zzaes != null) {
                            arrayList.add(com_google_android_gms_internal_measurement_zzed2.zzaes);
                        }
                        zzix().zzj(str, com_google_android_gms_internal_measurement_zzed2.zzaep.name);
                    }
                }
                ArrayList arrayList2 = (ArrayList) arrayList;
                int size = arrayList2.size();
                int i = 0;
                while (i < size) {
                    obj = arrayList2.get(i);
                    i++;
                    zzc(new zzeu((zzeu) obj, j), com_google_android_gms_internal_measurement_zzdz);
                }
                zzix = zzix();
                String str2 = com_google_android_gms_internal_measurement_zzeu.name;
                Preconditions.checkNotEmpty(str);
                Preconditions.checkNotEmpty(str2);
                zzix.zzab();
                zzix.zzch();
                if (j < 0) {
                    zzix.zzge().zzip().zzd("Invalid time querying triggered conditional properties", zzfg.zzbm(str), zzix.zzga().zzbj(str2), Long.valueOf(j));
                    emptyList = Collections.emptyList();
                } else {
                    emptyList = zzix.zzb("active=0 and app_id=? and trigger_event_name=? and abs(? - creation_timestamp) <= trigger_timeout", new String[]{str, str2, String.valueOf(j)});
                }
                List arrayList3 = new ArrayList(r2.size());
                for (zzed com_google_android_gms_internal_measurement_zzed3 : r2) {
                    if (com_google_android_gms_internal_measurement_zzed3 != null) {
                        zzjx com_google_android_gms_internal_measurement_zzjx = com_google_android_gms_internal_measurement_zzed3.zzaep;
                        zzjz com_google_android_gms_internal_measurement_zzjz = new zzjz(com_google_android_gms_internal_measurement_zzed3.packageName, com_google_android_gms_internal_measurement_zzed3.origin, com_google_android_gms_internal_measurement_zzjx.name, j, com_google_android_gms_internal_measurement_zzjx.getValue());
                        if (zzix().zza(com_google_android_gms_internal_measurement_zzjz)) {
                            zzge().zzis().zzd("User property triggered", com_google_android_gms_internal_measurement_zzed3.packageName, zzga().zzbl(com_google_android_gms_internal_measurement_zzjz.name), com_google_android_gms_internal_measurement_zzjz.value);
                        } else {
                            zzge().zzim().zzd("Too many active user properties, ignoring", zzfg.zzbm(com_google_android_gms_internal_measurement_zzed3.packageName), zzga().zzbl(com_google_android_gms_internal_measurement_zzjz.name), com_google_android_gms_internal_measurement_zzjz.value);
                        }
                        if (com_google_android_gms_internal_measurement_zzed3.zzaer != null) {
                            arrayList3.add(com_google_android_gms_internal_measurement_zzed3.zzaer);
                        }
                        com_google_android_gms_internal_measurement_zzed3.zzaep = new zzjx(com_google_android_gms_internal_measurement_zzjz);
                        com_google_android_gms_internal_measurement_zzed3.active = true;
                        zzix().zza(com_google_android_gms_internal_measurement_zzed3);
                    }
                }
                zzc(com_google_android_gms_internal_measurement_zzeu, com_google_android_gms_internal_measurement_zzdz);
                arrayList2 = (ArrayList) arrayList3;
                int size2 = arrayList2.size();
                i = 0;
                while (i < size2) {
                    obj = arrayList2.get(i);
                    i++;
                    zzc(new zzeu((zzeu) obj, j), com_google_android_gms_internal_measurement_zzdz);
                }
                zzix().setTransactionSuccessful();
            } finally {
                zzix().endTransaction();
            }
        } else {
            zzg(com_google_android_gms_internal_measurement_zzdz);
        }
    }

    final void zzb(zzjq com_google_android_gms_internal_measurement_zzjq) {
        this.zzaqj++;
    }

    final void zzb(zzjx com_google_android_gms_internal_measurement_zzjx, zzdz com_google_android_gms_internal_measurement_zzdz) {
        int i = 0;
        zzab();
        zzkq();
        if (!TextUtils.isEmpty(com_google_android_gms_internal_measurement_zzdz.zzadm)) {
            if (com_google_android_gms_internal_measurement_zzdz.zzadw) {
                int zzcf = zzgb().zzcf(com_google_android_gms_internal_measurement_zzjx.name);
                String zza;
                if (zzcf != 0) {
                    zzgb();
                    zza = zzka.zza(com_google_android_gms_internal_measurement_zzjx.name, 24, true);
                    if (com_google_android_gms_internal_measurement_zzjx.name != null) {
                        i = com_google_android_gms_internal_measurement_zzjx.name.length();
                    }
                    zzgb().zza(com_google_android_gms_internal_measurement_zzdz.packageName, zzcf, "_ev", zza, i);
                    return;
                }
                zzcf = zzgb().zzi(com_google_android_gms_internal_measurement_zzjx.name, com_google_android_gms_internal_measurement_zzjx.getValue());
                if (zzcf != 0) {
                    zzgb();
                    zza = zzka.zza(com_google_android_gms_internal_measurement_zzjx.name, 24, true);
                    Object value = com_google_android_gms_internal_measurement_zzjx.getValue();
                    if (value != null && ((value instanceof String) || (value instanceof CharSequence))) {
                        i = String.valueOf(value).length();
                    }
                    zzgb().zza(com_google_android_gms_internal_measurement_zzdz.packageName, zzcf, "_ev", zza, i);
                    return;
                }
                Object zzj = zzgb().zzj(com_google_android_gms_internal_measurement_zzjx.name, com_google_android_gms_internal_measurement_zzjx.getValue());
                if (zzj != null) {
                    zzjz com_google_android_gms_internal_measurement_zzjz = new zzjz(com_google_android_gms_internal_measurement_zzdz.packageName, com_google_android_gms_internal_measurement_zzjx.origin, com_google_android_gms_internal_measurement_zzjx.name, com_google_android_gms_internal_measurement_zzjx.zzaqz, zzj);
                    zzge().zzis().zze("Setting user property", zzga().zzbl(com_google_android_gms_internal_measurement_zzjz.name), zzj);
                    zzix().beginTransaction();
                    try {
                        zzg(com_google_android_gms_internal_measurement_zzdz);
                        boolean zza2 = zzix().zza(com_google_android_gms_internal_measurement_zzjz);
                        zzix().setTransactionSuccessful();
                        if (zza2) {
                            zzge().zzis().zze("User property set", zzga().zzbl(com_google_android_gms_internal_measurement_zzjz.name), com_google_android_gms_internal_measurement_zzjz.value);
                        } else {
                            zzge().zzim().zze("Too many unique user properties are set. Ignoring user property", zzga().zzbl(com_google_android_gms_internal_measurement_zzjz.name), com_google_android_gms_internal_measurement_zzjz.value);
                            zzgb().zza(com_google_android_gms_internal_measurement_zzdz.packageName, 9, null, null, 0);
                        }
                        zzix().endTransaction();
                        return;
                    } catch (Throwable th) {
                        zzix().endTransaction();
                    }
                } else {
                    return;
                }
            }
            zzg(com_google_android_gms_internal_measurement_zzdz);
        }
    }

    final void zzb(String str, int i, Throwable th, byte[] bArr, Map<String, List<String>> map) {
        boolean z = true;
        zzab();
        zzkq();
        Preconditions.checkNotEmpty(str);
        if (bArr == null) {
            try {
                bArr = new byte[0];
            } catch (Throwable th2) {
                this.zzaql = false;
                zzkv();
            }
        }
        zzge().zzit().zzg("onConfigFetched. Response size", Integer.valueOf(bArr.length));
        zzix().beginTransaction();
        zzdy zzbc = zzix().zzbc(str);
        boolean z2 = (i == Callback.DEFAULT_DRAG_ANIMATION_DURATION || i == 204 || i == 304) && th == null;
        if (zzbc == null) {
            zzge().zzip().zzg("App does not exist in onConfigFetched. appId", zzfg.zzbm(str));
        } else if (z2 || i == 404) {
            List list = map != null ? (List) map.get("Last-Modified") : null;
            String str2 = (list == null || list.size() <= 0) ? null : (String) list.get(0);
            if (i == 404 || i == 304) {
                if (zzkm().zzbu(str) == null && !zzkm().zza(str, null, null)) {
                    zzix().endTransaction();
                    this.zzaql = false;
                    zzkv();
                    return;
                }
            } else if (!zzkm().zza(str, bArr, str2)) {
                zzix().endTransaction();
                this.zzaql = false;
                zzkv();
                return;
            }
            zzbc.zzs(zzbt().currentTimeMillis());
            zzix().zza(zzbc);
            if (i == 404) {
                zzge().zziq().zzg("Config not found. Using empty config. appId", str);
            } else {
                zzge().zzit().zze("Successfully fetched config. Got network response. code, size", Integer.valueOf(i), Integer.valueOf(bArr.length));
            }
            if (zzkn().zzex() && zzkt()) {
                zzks();
            } else {
                zzku();
            }
        } else {
            zzbc.zzt(zzbt().currentTimeMillis());
            zzix().zza(zzbc);
            zzge().zzit().zze("Fetching config failed. code, error", Integer.valueOf(i), th);
            zzkm().zzbw(str);
            zzgf().zzajv.set(zzbt().currentTimeMillis());
            if (!(i == 503 || i == 429)) {
                z = false;
            }
            if (z) {
                zzgf().zzajw.set(zzbt().currentTimeMillis());
            }
            zzku();
        }
        zzix().setTransactionSuccessful();
        zzix().endTransaction();
        this.zzaql = false;
        zzkv();
    }

    public Clock zzbt() {
        return this.zzacw.zzbt();
    }

    final void zzc(zzed com_google_android_gms_internal_measurement_zzed, zzdz com_google_android_gms_internal_measurement_zzdz) {
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzed);
        Preconditions.checkNotEmpty(com_google_android_gms_internal_measurement_zzed.packageName);
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzed.zzaep);
        Preconditions.checkNotEmpty(com_google_android_gms_internal_measurement_zzed.zzaep.name);
        zzab();
        zzkq();
        if (!TextUtils.isEmpty(com_google_android_gms_internal_measurement_zzdz.zzadm)) {
            if (com_google_android_gms_internal_measurement_zzdz.zzadw) {
                zzix().beginTransaction();
                try {
                    zzg(com_google_android_gms_internal_measurement_zzdz);
                    zzed zzi = zzix().zzi(com_google_android_gms_internal_measurement_zzed.packageName, com_google_android_gms_internal_measurement_zzed.zzaep.name);
                    if (zzi != null) {
                        zzge().zzis().zze("Removing conditional user property", com_google_android_gms_internal_measurement_zzed.packageName, zzga().zzbl(com_google_android_gms_internal_measurement_zzed.zzaep.name));
                        zzix().zzj(com_google_android_gms_internal_measurement_zzed.packageName, com_google_android_gms_internal_measurement_zzed.zzaep.name);
                        if (zzi.active) {
                            zzix().zzg(com_google_android_gms_internal_measurement_zzed.packageName, com_google_android_gms_internal_measurement_zzed.zzaep.name);
                        }
                        if (com_google_android_gms_internal_measurement_zzed.zzaes != null) {
                            Bundle bundle = null;
                            if (com_google_android_gms_internal_measurement_zzed.zzaes.zzafq != null) {
                                bundle = com_google_android_gms_internal_measurement_zzed.zzaes.zzafq.zzif();
                            }
                            zzc(zzgb().zza(com_google_android_gms_internal_measurement_zzed.zzaes.name, bundle, zzi.origin, com_google_android_gms_internal_measurement_zzed.zzaes.zzagb, true, false), com_google_android_gms_internal_measurement_zzdz);
                        }
                    } else {
                        zzge().zzip().zze("Conditional user property doesn't exist", zzfg.zzbm(com_google_android_gms_internal_measurement_zzed.packageName), zzga().zzbl(com_google_android_gms_internal_measurement_zzed.zzaep.name));
                    }
                    zzix().setTransactionSuccessful();
                } finally {
                    zzix().endTransaction();
                }
            } else {
                zzg(com_google_android_gms_internal_measurement_zzdz);
            }
        }
    }

    final void zzc(zzeu com_google_android_gms_internal_measurement_zzeu, String str) {
        zzdy zzbc = zzix().zzbc(str);
        if (zzbc == null || TextUtils.isEmpty(zzbc.zzag())) {
            zzge().zzis().zzg("No app data available; dropping event", str);
            return;
        }
        Boolean zzc = zzc(zzbc);
        if (zzc == null) {
            if (!"_ui".equals(com_google_android_gms_internal_measurement_zzeu.name)) {
                zzge().zzip().zzg("Could not find package. appId", zzfg.zzbm(str));
            }
        } else if (!zzc.booleanValue()) {
            zzge().zzim().zzg("App version does not match; dropping event. appId", zzfg.zzbm(str));
            return;
        }
        zzeu com_google_android_gms_internal_measurement_zzeu2 = com_google_android_gms_internal_measurement_zzeu;
        zzb(com_google_android_gms_internal_measurement_zzeu2, new zzdz(str, zzbc.getGmpAppId(), zzbc.zzag(), zzbc.zzgm(), zzbc.zzgn(), zzbc.zzgo(), zzbc.zzgp(), null, zzbc.isMeasurementEnabled(), false, zzbc.zzgj(), zzbc.zzhc(), 0, 0, zzbc.zzhd(), zzbc.zzhe(), false));
    }

    final void zzc(zzjx com_google_android_gms_internal_measurement_zzjx, zzdz com_google_android_gms_internal_measurement_zzdz) {
        zzab();
        zzkq();
        if (!TextUtils.isEmpty(com_google_android_gms_internal_measurement_zzdz.zzadm)) {
            if (com_google_android_gms_internal_measurement_zzdz.zzadw) {
                zzge().zzis().zzg("Removing user property", zzga().zzbl(com_google_android_gms_internal_measurement_zzjx.name));
                zzix().beginTransaction();
                try {
                    zzg(com_google_android_gms_internal_measurement_zzdz);
                    zzix().zzg(com_google_android_gms_internal_measurement_zzdz.packageName, com_google_android_gms_internal_measurement_zzjx.name);
                    zzix().setTransactionSuccessful();
                    zzge().zzis().zzg("User property removed", zzga().zzbl(com_google_android_gms_internal_measurement_zzjx.name));
                } finally {
                    zzix().endTransaction();
                }
            } else {
                zzg(com_google_android_gms_internal_measurement_zzdz);
            }
        }
    }

    final zzdz zzcb(String str) {
        zzdy zzbc = zzix().zzbc(str);
        if (zzbc == null || TextUtils.isEmpty(zzbc.zzag())) {
            zzge().zzis().zzg("No app data available; dropping", str);
            return null;
        }
        Boolean zzc = zzc(zzbc);
        if (zzc == null || zzc.booleanValue()) {
            return new zzdz(str, zzbc.getGmpAppId(), zzbc.zzag(), zzbc.zzgm(), zzbc.zzgn(), zzbc.zzgo(), zzbc.zzgp(), null, zzbc.isMeasurementEnabled(), false, zzbc.zzgj(), zzbc.zzhc(), 0, 0, zzbc.zzhd(), zzbc.zzhe(), false);
        }
        zzge().zzim().zzg("App version does not match; dropping. appId", zzfg.zzbm(str));
        return null;
    }

    final void zzd(zzdz com_google_android_gms_internal_measurement_zzdz) {
        if (this.zzaqq != null) {
            this.zzaqr = new ArrayList();
            this.zzaqr.addAll(this.zzaqq);
        }
        zzhg zzix = zzix();
        String str = com_google_android_gms_internal_measurement_zzdz.packageName;
        Preconditions.checkNotEmpty(str);
        zzix.zzab();
        zzix.zzch();
        try {
            SQLiteDatabase writableDatabase = zzix.getWritableDatabase();
            String[] strArr = new String[]{str};
            int delete = writableDatabase.delete("main_event_params", "app_id=?", strArr) + ((((((((writableDatabase.delete("apps", "app_id=?", strArr) + 0) + writableDatabase.delete("events", "app_id=?", strArr)) + writableDatabase.delete("user_attributes", "app_id=?", strArr)) + writableDatabase.delete("conditional_properties", "app_id=?", strArr)) + writableDatabase.delete("raw_events", "app_id=?", strArr)) + writableDatabase.delete("raw_events_metadata", "app_id=?", strArr)) + writableDatabase.delete("queue", "app_id=?", strArr)) + writableDatabase.delete("audience_filter_values", "app_id=?", strArr));
            if (delete > 0) {
                zzix.zzge().zzit().zze("Reset analytics data. app, records", str, Integer.valueOf(delete));
            }
        } catch (SQLiteException e) {
            zzix.zzge().zzim().zze("Error resetting analytics data. appId, error", zzfg.zzbm(str), e);
        }
        zzdz zza = zza(getContext(), com_google_android_gms_internal_measurement_zzdz.packageName, com_google_android_gms_internal_measurement_zzdz.zzadm, com_google_android_gms_internal_measurement_zzdz.zzadw, com_google_android_gms_internal_measurement_zzdz.zzady, com_google_android_gms_internal_measurement_zzdz.zzadz, com_google_android_gms_internal_measurement_zzdz.zzaem);
        if (!zzgg().zzaz(com_google_android_gms_internal_measurement_zzdz.packageName) || com_google_android_gms_internal_measurement_zzdz.zzadw) {
            zzf(zza);
        }
    }

    final void zze(zzdz com_google_android_gms_internal_measurement_zzdz) {
        zzab();
        zzkq();
        Preconditions.checkNotEmpty(com_google_android_gms_internal_measurement_zzdz.packageName);
        zzg(com_google_android_gms_internal_measurement_zzdz);
    }

    public final void zzf(zzdz com_google_android_gms_internal_measurement_zzdz) {
        zzhg zzix;
        String zzah;
        zzab();
        zzkq();
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzdz);
        Preconditions.checkNotEmpty(com_google_android_gms_internal_measurement_zzdz.packageName);
        if (!TextUtils.isEmpty(com_google_android_gms_internal_measurement_zzdz.zzadm)) {
            zzdy zzbc = zzix().zzbc(com_google_android_gms_internal_measurement_zzdz.packageName);
            if (!(zzbc == null || !TextUtils.isEmpty(zzbc.getGmpAppId()) || TextUtils.isEmpty(com_google_android_gms_internal_measurement_zzdz.zzadm))) {
                zzbc.zzs(0);
                zzix().zza(zzbc);
                zzkm().zzbx(com_google_android_gms_internal_measurement_zzdz.packageName);
            }
            if (com_google_android_gms_internal_measurement_zzdz.zzadw) {
                int i;
                Bundle bundle;
                long j = com_google_android_gms_internal_measurement_zzdz.zzaem;
                if (j == 0) {
                    j = zzbt().currentTimeMillis();
                }
                int i2 = com_google_android_gms_internal_measurement_zzdz.zzaen;
                if (i2 == 0 || i2 == 1) {
                    i = i2;
                } else {
                    zzge().zzip().zze("Incorrect app type, assuming installed app. appId, appType", zzfg.zzbm(com_google_android_gms_internal_measurement_zzdz.packageName), Integer.valueOf(i2));
                    i = 0;
                }
                zzix().beginTransaction();
                try {
                    zzbc = zzix().zzbc(com_google_android_gms_internal_measurement_zzdz.packageName);
                    if (!(zzbc == null || zzbc.getGmpAppId() == null || zzbc.getGmpAppId().equals(com_google_android_gms_internal_measurement_zzdz.zzadm))) {
                        zzge().zzip().zzg("New GMP App Id passed in. Removing cached database data. appId", zzfg.zzbm(zzbc.zzah()));
                        zzix = zzix();
                        zzah = zzbc.zzah();
                        zzix.zzch();
                        zzix.zzab();
                        Preconditions.checkNotEmpty(zzah);
                        SQLiteDatabase writableDatabase = zzix.getWritableDatabase();
                        String[] strArr = new String[]{zzah};
                        i2 = writableDatabase.delete("audience_filter_values", "app_id=?", strArr) + ((((((((writableDatabase.delete("events", "app_id=?", strArr) + 0) + writableDatabase.delete("user_attributes", "app_id=?", strArr)) + writableDatabase.delete("conditional_properties", "app_id=?", strArr)) + writableDatabase.delete("apps", "app_id=?", strArr)) + writableDatabase.delete("raw_events", "app_id=?", strArr)) + writableDatabase.delete("raw_events_metadata", "app_id=?", strArr)) + writableDatabase.delete("event_filters", "app_id=?", strArr)) + writableDatabase.delete("property_filters", "app_id=?", strArr));
                        if (i2 > 0) {
                            zzix.zzge().zzit().zze("Deleted application data. app, records", zzah, Integer.valueOf(i2));
                        }
                        zzbc = null;
                    }
                } catch (SQLiteException e) {
                    zzix.zzge().zzim().zze("Error deleting application data. appId, error", zzfg.zzbm(zzah), e);
                } catch (Throwable th) {
                    zzix().endTransaction();
                }
                if (zzbc != null) {
                    if (zzbc.zzgm() != -2147483648L) {
                        if (zzbc.zzgm() != com_google_android_gms_internal_measurement_zzdz.zzads) {
                            bundle = new Bundle();
                            bundle.putString("_pv", zzbc.zzag());
                            zzb(new zzeu("_au", new zzer(bundle), "auto", j), com_google_android_gms_internal_measurement_zzdz);
                        }
                    } else if (!(zzbc.zzag() == null || zzbc.zzag().equals(com_google_android_gms_internal_measurement_zzdz.zzth))) {
                        bundle = new Bundle();
                        bundle.putString("_pv", zzbc.zzag());
                        zzb(new zzeu("_au", new zzer(bundle), "auto", j), com_google_android_gms_internal_measurement_zzdz);
                    }
                }
                zzg(com_google_android_gms_internal_measurement_zzdz);
                zzeq com_google_android_gms_internal_measurement_zzeq = null;
                if (i == 0) {
                    com_google_android_gms_internal_measurement_zzeq = zzix().zzf(com_google_android_gms_internal_measurement_zzdz.packageName, "_f");
                } else if (i == 1) {
                    com_google_android_gms_internal_measurement_zzeq = zzix().zzf(com_google_android_gms_internal_measurement_zzdz.packageName, "_v");
                }
                if (com_google_android_gms_internal_measurement_zzeq == null) {
                    long j2 = (1 + (j / 3600000)) * 3600000;
                    if (i == 0) {
                        zzb(new zzjx("_fot", j, Long.valueOf(j2), "auto"), com_google_android_gms_internal_measurement_zzdz);
                        zzab();
                        zzkq();
                        Bundle bundle2 = new Bundle();
                        bundle2.putLong("_c", 1);
                        bundle2.putLong("_r", 1);
                        bundle2.putLong("_uwa", 0);
                        bundle2.putLong("_pfo", 0);
                        bundle2.putLong("_sys", 0);
                        bundle2.putLong("_sysu", 0);
                        if (zzgg().zzaz(com_google_android_gms_internal_measurement_zzdz.packageName) && com_google_android_gms_internal_measurement_zzdz.zzaeo) {
                            bundle2.putLong("_dac", 1);
                        }
                        if (getContext().getPackageManager() == null) {
                            zzge().zzim().zzg("PackageManager is null, first open report might be inaccurate. appId", zzfg.zzbm(com_google_android_gms_internal_measurement_zzdz.packageName));
                        } else {
                            ApplicationInfo applicationInfo;
                            PackageInfo packageInfo = null;
                            try {
                                packageInfo = Wrappers.packageManager(getContext()).getPackageInfo(com_google_android_gms_internal_measurement_zzdz.packageName, 0);
                            } catch (NameNotFoundException e2) {
                                zzge().zzim().zze("Package info is null, first open report might be inaccurate. appId", zzfg.zzbm(com_google_android_gms_internal_measurement_zzdz.packageName), e2);
                            }
                            if (packageInfo != null) {
                                if (packageInfo.firstInstallTime != 0) {
                                    Object obj = null;
                                    if (packageInfo.firstInstallTime != packageInfo.lastUpdateTime) {
                                        bundle2.putLong("_uwa", 1);
                                    } else {
                                        obj = 1;
                                    }
                                    zzb(new zzjx("_fi", j, Long.valueOf(obj != null ? 1 : 0), "auto"), com_google_android_gms_internal_measurement_zzdz);
                                }
                            }
                            try {
                                applicationInfo = Wrappers.packageManager(getContext()).getApplicationInfo(com_google_android_gms_internal_measurement_zzdz.packageName, 0);
                            } catch (NameNotFoundException e22) {
                                zzge().zzim().zze("Application info is null, first open report might be inaccurate. appId", zzfg.zzbm(com_google_android_gms_internal_measurement_zzdz.packageName), e22);
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
                        zzhg zzix2 = zzix();
                        String str = com_google_android_gms_internal_measurement_zzdz.packageName;
                        Preconditions.checkNotEmpty(str);
                        zzix2.zzab();
                        zzix2.zzch();
                        j2 = zzix2.zzm(str, "first_open_count");
                        if (j2 >= 0) {
                            bundle2.putLong("_pfo", j2);
                        }
                        zzb(new zzeu("_f", new zzer(bundle2), "auto", j), com_google_android_gms_internal_measurement_zzdz);
                    } else if (i == 1) {
                        zzb(new zzjx("_fvt", j, Long.valueOf(j2), "auto"), com_google_android_gms_internal_measurement_zzdz);
                        zzab();
                        zzkq();
                        bundle = new Bundle();
                        bundle.putLong("_c", 1);
                        bundle.putLong("_r", 1);
                        if (zzgg().zzaz(com_google_android_gms_internal_measurement_zzdz.packageName) && com_google_android_gms_internal_measurement_zzdz.zzaeo) {
                            bundle.putLong("_dac", 1);
                        }
                        zzb(new zzeu("_v", new zzer(bundle), "auto", j), com_google_android_gms_internal_measurement_zzdz);
                    }
                    bundle = new Bundle();
                    bundle.putLong("_et", 1);
                    zzb(new zzeu("_e", new zzer(bundle), "auto", j), com_google_android_gms_internal_measurement_zzdz);
                } else if (com_google_android_gms_internal_measurement_zzdz.zzael) {
                    zzb(new zzeu("_cd", new zzer(new Bundle()), "auto", j), com_google_android_gms_internal_measurement_zzdz);
                }
                zzix().setTransactionSuccessful();
                zzix().endTransaction();
                return;
            }
            zzg(com_google_android_gms_internal_measurement_zzdz);
        }
    }

    public zzeo zzfw() {
        return this.zzacw.zzfw();
    }

    final void zzg(Runnable runnable) {
        zzab();
        if (this.zzaqi == null) {
            this.zzaqi = new ArrayList();
        }
        this.zzaqi.add(runnable);
    }

    public zzfe zzga() {
        return this.zzacw.zzga();
    }

    public zzka zzgb() {
        return this.zzacw.zzgb();
    }

    public zzgg zzgd() {
        return this.zzacw.zzgd();
    }

    public zzfg zzge() {
        return this.zzacw.zzge();
    }

    public zzfr zzgf() {
        return this.zzacw.zzgf();
    }

    public zzef zzgg() {
        return this.zzacw.zzgg();
    }

    public final String zzh(zzdz com_google_android_gms_internal_measurement_zzdz) {
        Object e;
        try {
            return (String) zzgd().zzb(new zzju(this, com_google_android_gms_internal_measurement_zzdz)).get(30000, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e2) {
            e = e2;
        } catch (InterruptedException e3) {
            e = e3;
        } catch (ExecutionException e4) {
            e = e4;
        }
        zzge().zzim().zze("Failed to get app instance id. appId", zzfg.zzbm(com_google_android_gms_internal_measurement_zzdz.packageName), e);
        return null;
    }

    public final zzeb zziw() {
        zza(this.zzaqf);
        return this.zzaqf;
    }

    public final zzei zzix() {
        zza(this.zzaqc);
        return this.zzaqc;
    }

    public final zzfk zzkn() {
        zza(this.zzaqb);
        return this.zzaqb;
    }

    final void zzkq() {
        if (!this.zzvo) {
            throw new IllegalStateException("UploadController is not initialized");
        }
    }

    public final void zzks() {
        String str;
        zzab();
        zzkq();
        this.zzaqn = true;
        String zzhn;
        try {
            Boolean zzkf = this.zzacw.zzfx().zzkf();
            if (zzkf == null) {
                zzge().zzip().log("Upload data called on the client side before use of service was decided");
                this.zzaqn = false;
                zzkv();
            } else if (zzkf.booleanValue()) {
                zzge().zzim().log("Upload called in the client side when service should be used");
                this.zzaqn = false;
                zzkv();
            } else if (this.zzaqh > 0) {
                zzku();
                this.zzaqn = false;
                zzkv();
            } else {
                zzab();
                if ((this.zzaqq != null ? 1 : null) != null) {
                    zzge().zzit().log("Uploading requested multiple times");
                    this.zzaqn = false;
                    zzkv();
                } else if (zzkn().zzex()) {
                    long currentTimeMillis = zzbt().currentTimeMillis();
                    zzd(null, currentTimeMillis - zzef.zzhi());
                    long j = zzgf().zzaju.get();
                    if (j != 0) {
                        zzge().zzis().zzg("Uploading events. Elapsed time since last upload attempt (ms)", Long.valueOf(Math.abs(currentTimeMillis - j)));
                    }
                    zzhn = zzix().zzhn();
                    Object zzab;
                    if (TextUtils.isEmpty(zzhn)) {
                        this.zzaqs = -1;
                        zzab = zzix().zzab(currentTimeMillis - zzef.zzhi());
                        if (!TextUtils.isEmpty(zzab)) {
                            zzdy zzbc = zzix().zzbc(zzab);
                            if (zzbc != null) {
                                zzb(zzbc);
                            }
                        }
                    } else {
                        if (this.zzaqs == -1) {
                            this.zzaqs = zzix().zzhu();
                        }
                        List<Pair> zzb = zzix().zzb(zzhn, zzgg().zzb(zzhn, zzew.zzago), Math.max(0, zzgg().zzb(zzhn, zzew.zzagp)));
                        if (!zzb.isEmpty()) {
                            zzkq com_google_android_gms_internal_measurement_zzkq;
                            Object obj;
                            int i;
                            List subList;
                            for (Pair pair : zzb) {
                                com_google_android_gms_internal_measurement_zzkq = (zzkq) pair.first;
                                if (!TextUtils.isEmpty(com_google_android_gms_internal_measurement_zzkq.zzatv)) {
                                    obj = com_google_android_gms_internal_measurement_zzkq.zzatv;
                                    break;
                                }
                            }
                            obj = null;
                            if (obj != null) {
                                for (i = 0; i < zzb.size(); i++) {
                                    com_google_android_gms_internal_measurement_zzkq = (zzkq) ((Pair) zzb.get(i)).first;
                                    if (!TextUtils.isEmpty(com_google_android_gms_internal_measurement_zzkq.zzatv) && !com_google_android_gms_internal_measurement_zzkq.zzatv.equals(obj)) {
                                        subList = zzb.subList(0, i);
                                        break;
                                    }
                                }
                            }
                            subList = zzb;
                            zzkp com_google_android_gms_internal_measurement_zzkp = new zzkp();
                            com_google_android_gms_internal_measurement_zzkp.zzatf = new zzkq[subList.size()];
                            Collection arrayList = new ArrayList(subList.size());
                            Object obj2 = (zzef.zzhk() && zzgg().zzat(zzhn)) ? 1 : null;
                            for (i = 0; i < com_google_android_gms_internal_measurement_zzkp.zzatf.length; i++) {
                                com_google_android_gms_internal_measurement_zzkp.zzatf[i] = (zzkq) ((Pair) subList.get(i)).first;
                                arrayList.add((Long) ((Pair) subList.get(i)).second);
                                com_google_android_gms_internal_measurement_zzkp.zzatf[i].zzatu = Long.valueOf(12451);
                                com_google_android_gms_internal_measurement_zzkp.zzatf[i].zzatk = Long.valueOf(currentTimeMillis);
                                com_google_android_gms_internal_measurement_zzkp.zzatf[i].zzatz = Boolean.valueOf(false);
                                if (obj2 == null) {
                                    com_google_android_gms_internal_measurement_zzkp.zzatf[i].zzauh = null;
                                }
                            }
                            obj2 = zzge().isLoggable(2) ? zzga().zza(com_google_android_gms_internal_measurement_zzkp) : null;
                            obj = zzgb().zzb(com_google_android_gms_internal_measurement_zzkp);
                            str = (String) zzew.zzagy.get();
                            URL url = new URL(str);
                            Preconditions.checkArgument(!arrayList.isEmpty());
                            if (this.zzaqq != null) {
                                zzge().zzim().log("Set uploading progress before finishing the previous upload");
                            } else {
                                this.zzaqq = new ArrayList(arrayList);
                            }
                            zzgf().zzajv.set(currentTimeMillis);
                            zzab = "?";
                            if (com_google_android_gms_internal_measurement_zzkp.zzatf.length > 0) {
                                zzab = com_google_android_gms_internal_measurement_zzkp.zzatf[0].zzti;
                            }
                            zzge().zzit().zzd("Uploading data. app, uncompressed size, data", zzab, Integer.valueOf(obj.length), obj2);
                            this.zzaqm = true;
                            zzhg zzkn = zzkn();
                            zzfm com_google_android_gms_internal_measurement_zzjs = new zzjs(this, zzhn);
                            zzkn.zzab();
                            zzkn.zzch();
                            Preconditions.checkNotNull(url);
                            Preconditions.checkNotNull(obj);
                            Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzjs);
                            zzkn.zzgd().zzd(new zzfo(zzkn, zzhn, url, obj, null, com_google_android_gms_internal_measurement_zzjs));
                        }
                    }
                    this.zzaqn = false;
                    zzkv();
                } else {
                    zzge().zzit().log("Network not connected, ignoring upload request");
                    zzku();
                    this.zzaqn = false;
                    zzkv();
                }
            }
        } catch (MalformedURLException e) {
            zzge().zzim().zze("Failed to parse upload URL. Not uploading. appId", zzfg.zzbm(zzhn), str);
        } catch (Throwable th) {
            this.zzaqn = false;
            zzkv();
        }
    }

    final void zzkx() {
        zzab();
        zzkq();
        if (!this.zzaqg) {
            zzge().zzir().log("This instance being marked as an uploader");
            zzab();
            zzkq();
            if (zzky() && zzkw()) {
                int zza = zza(this.zzaqp);
                int zzij = this.zzacw.zzfv().zzij();
                zzab();
                if (zza > zzij) {
                    zzge().zzim().zze("Panic: can't downgrade version. Previous, current version", Integer.valueOf(zza), Integer.valueOf(zzij));
                } else if (zza < zzij) {
                    if (zza(zzij, this.zzaqp)) {
                        zzge().zzit().zze("Storage version upgraded. Previous, current version", Integer.valueOf(zza), Integer.valueOf(zzij));
                    } else {
                        zzge().zzim().zze("Storage version upgrade failed. Previous, current version", Integer.valueOf(zza), Integer.valueOf(zzij));
                    }
                }
            }
            this.zzaqg = true;
            zzku();
        }
    }

    final void zzkz() {
        this.zzaqk++;
    }

    final zzgl zzla() {
        return this.zzacw;
    }

    public final void zzm(boolean z) {
        zzku();
    }
}
