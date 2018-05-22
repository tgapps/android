package com.google.android.gms.internal.measurement;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.Clock;
import com.google.android.gms.measurement.AppMeasurement.Event;
import com.google.android.gms.measurement.AppMeasurement.Param;
import com.google.android.gms.measurement.AppMeasurement.UserProperty;
import java.io.IOException;
import java.util.Map;

public final class zzgf extends zzjq implements zzeh {
    private static int zzalf = 65535;
    private static int zzalg = 2;
    private final Map<String, Map<String, String>> zzalh = new ArrayMap();
    private final Map<String, Map<String, Boolean>> zzali = new ArrayMap();
    private final Map<String, Map<String, Boolean>> zzalj = new ArrayMap();
    private final Map<String, zzkk> zzalk = new ArrayMap();
    private final Map<String, Map<String, Integer>> zzall = new ArrayMap();
    private final Map<String, String> zzalm = new ArrayMap();

    zzgf(zzjr com_google_android_gms_internal_measurement_zzjr) {
        super(com_google_android_gms_internal_measurement_zzjr);
    }

    private final zzkk zza(String str, byte[] bArr) {
        if (bArr == null) {
            return new zzkk();
        }
        zzabv zza = zzabv.zza(bArr, 0, bArr.length);
        zzace com_google_android_gms_internal_measurement_zzkk = new zzkk();
        try {
            com_google_android_gms_internal_measurement_zzkk.zzb(zza);
            zzge().zzit().zze("Parsed config. version, gmp_app_id", com_google_android_gms_internal_measurement_zzkk.zzasp, com_google_android_gms_internal_measurement_zzkk.zzadm);
            return com_google_android_gms_internal_measurement_zzkk;
        } catch (IOException e) {
            zzge().zzip().zze("Unable to merge remote config. appId", zzfg.zzbm(str), e);
            return new zzkk();
        }
    }

    private static Map<String, String> zza(zzkk com_google_android_gms_internal_measurement_zzkk) {
        Map<String, String> arrayMap = new ArrayMap();
        if (!(com_google_android_gms_internal_measurement_zzkk == null || com_google_android_gms_internal_measurement_zzkk.zzasr == null)) {
            for (zzkl com_google_android_gms_internal_measurement_zzkl : com_google_android_gms_internal_measurement_zzkk.zzasr) {
                if (com_google_android_gms_internal_measurement_zzkl != null) {
                    arrayMap.put(com_google_android_gms_internal_measurement_zzkl.zzny, com_google_android_gms_internal_measurement_zzkl.value);
                }
            }
        }
        return arrayMap;
    }

    private final void zza(String str, zzkk com_google_android_gms_internal_measurement_zzkk) {
        Map arrayMap = new ArrayMap();
        Map arrayMap2 = new ArrayMap();
        Map arrayMap3 = new ArrayMap();
        if (!(com_google_android_gms_internal_measurement_zzkk == null || com_google_android_gms_internal_measurement_zzkk.zzass == null)) {
            for (zzkj com_google_android_gms_internal_measurement_zzkj : com_google_android_gms_internal_measurement_zzkk.zzass) {
                if (TextUtils.isEmpty(com_google_android_gms_internal_measurement_zzkj.name)) {
                    zzge().zzip().log("EventConfig contained null event name");
                } else {
                    Object zzak = Event.zzak(com_google_android_gms_internal_measurement_zzkj.name);
                    if (!TextUtils.isEmpty(zzak)) {
                        com_google_android_gms_internal_measurement_zzkj.name = zzak;
                    }
                    arrayMap.put(com_google_android_gms_internal_measurement_zzkj.name, com_google_android_gms_internal_measurement_zzkj.zzasm);
                    arrayMap2.put(com_google_android_gms_internal_measurement_zzkj.name, com_google_android_gms_internal_measurement_zzkj.zzasn);
                    if (com_google_android_gms_internal_measurement_zzkj.zzaso != null) {
                        if (com_google_android_gms_internal_measurement_zzkj.zzaso.intValue() < zzalg || com_google_android_gms_internal_measurement_zzkj.zzaso.intValue() > zzalf) {
                            zzge().zzip().zze("Invalid sampling rate. Event name, sample rate", com_google_android_gms_internal_measurement_zzkj.name, com_google_android_gms_internal_measurement_zzkj.zzaso);
                        } else {
                            arrayMap3.put(com_google_android_gms_internal_measurement_zzkj.name, com_google_android_gms_internal_measurement_zzkj.zzaso);
                        }
                    }
                }
            }
        }
        this.zzali.put(str, arrayMap);
        this.zzalj.put(str, arrayMap2);
        this.zzall.put(str, arrayMap3);
    }

    private final void zzbt(String str) {
        zzch();
        zzab();
        Preconditions.checkNotEmpty(str);
        if (this.zzalk.get(str) == null) {
            byte[] zzbe = zzix().zzbe(str);
            if (zzbe == null) {
                this.zzalh.put(str, null);
                this.zzali.put(str, null);
                this.zzalj.put(str, null);
                this.zzalk.put(str, null);
                this.zzalm.put(str, null);
                this.zzall.put(str, null);
                return;
            }
            zzkk zza = zza(str, zzbe);
            this.zzalh.put(str, zza(zza));
            zza(str, zza);
            this.zzalk.put(str, zza);
            this.zzalm.put(str, null);
        }
    }

    public final /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    protected final boolean zza(String str, byte[] bArr, String str2) {
        zzch();
        zzab();
        Preconditions.checkNotEmpty(str);
        zzkk zza = zza(str, bArr);
        if (zza == null) {
            return false;
        }
        zza(str, zza);
        this.zzalk.put(str, zza);
        this.zzalm.put(str, str2);
        this.zzalh.put(str, zza(zza));
        zzjp zziw = zziw();
        zzkd[] com_google_android_gms_internal_measurement_zzkdArr = zza.zzast;
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzkdArr);
        for (zzkd com_google_android_gms_internal_measurement_zzkd : com_google_android_gms_internal_measurement_zzkdArr) {
            for (zzke com_google_android_gms_internal_measurement_zzke : com_google_android_gms_internal_measurement_zzkd.zzarn) {
                String zzak = Event.zzak(com_google_android_gms_internal_measurement_zzke.zzarq);
                if (zzak != null) {
                    com_google_android_gms_internal_measurement_zzke.zzarq = zzak;
                }
                for (zzkf com_google_android_gms_internal_measurement_zzkf : com_google_android_gms_internal_measurement_zzke.zzarr) {
                    String zzak2 = Param.zzak(com_google_android_gms_internal_measurement_zzkf.zzary);
                    if (zzak2 != null) {
                        com_google_android_gms_internal_measurement_zzkf.zzary = zzak2;
                    }
                }
            }
            for (zzkh com_google_android_gms_internal_measurement_zzkh : com_google_android_gms_internal_measurement_zzkd.zzarm) {
                String zzak3 = UserProperty.zzak(com_google_android_gms_internal_measurement_zzkh.zzasf);
                if (zzak3 != null) {
                    com_google_android_gms_internal_measurement_zzkh.zzasf = zzak3;
                }
            }
        }
        zziw.zzix().zza(str, com_google_android_gms_internal_measurement_zzkdArr);
        try {
            zza.zzast = null;
            byte[] bArr2 = new byte[zza.zzvm()];
            zza.zza(zzabw.zzb(bArr2, 0, bArr2.length));
            bArr = bArr2;
        } catch (IOException e) {
            zzge().zzip().zze("Unable to serialize reduced-size config. Storing full config instead. appId", zzfg.zzbm(str), e);
        }
        zzhg zzix = zzix();
        Preconditions.checkNotEmpty(str);
        zzix.zzab();
        zzix.zzch();
        ContentValues contentValues = new ContentValues();
        contentValues.put("remote_config", bArr);
        try {
            if (((long) zzix.getWritableDatabase().update("apps", contentValues, "app_id = ?", new String[]{str})) == 0) {
                zzix.zzge().zzim().zzg("Failed to update remote config (got 0). appId", zzfg.zzbm(str));
            }
        } catch (SQLiteException e2) {
            zzix.zzge().zzim().zze("Error storing remote config. appId", zzfg.zzbm(str), e2);
        }
        return true;
    }

    public final /* bridge */ /* synthetic */ void zzab() {
        super.zzab();
    }

    public final /* bridge */ /* synthetic */ Clock zzbt() {
        return super.zzbt();
    }

    protected final zzkk zzbu(String str) {
        zzch();
        zzab();
        Preconditions.checkNotEmpty(str);
        zzbt(str);
        return (zzkk) this.zzalk.get(str);
    }

    protected final String zzbv(String str) {
        zzab();
        return (String) this.zzalm.get(str);
    }

    protected final void zzbw(String str) {
        zzab();
        this.zzalm.put(str, null);
    }

    final void zzbx(String str) {
        zzab();
        this.zzalk.remove(str);
    }

    final boolean zzby(String str) {
        return "1".equals(zze(str, "measurement.upload.blacklist_internal"));
    }

    final boolean zzbz(String str) {
        return "1".equals(zze(str, "measurement.upload.blacklist_public"));
    }

    public final String zze(String str, String str2) {
        zzab();
        zzbt(str);
        Map map = (Map) this.zzalh.get(str);
        return map != null ? (String) map.get(str2) : null;
    }

    public final /* bridge */ /* synthetic */ void zzfr() {
        super.zzfr();
    }

    public final /* bridge */ /* synthetic */ void zzfs() {
        super.zzfs();
    }

    public final /* bridge */ /* synthetic */ zzdu zzft() {
        return super.zzft();
    }

    public final /* bridge */ /* synthetic */ zzhk zzfu() {
        return super.zzfu();
    }

    public final /* bridge */ /* synthetic */ zzfb zzfv() {
        return super.zzfv();
    }

    public final /* bridge */ /* synthetic */ zzeo zzfw() {
        return super.zzfw();
    }

    public final /* bridge */ /* synthetic */ zzii zzfx() {
        return super.zzfx();
    }

    public final /* bridge */ /* synthetic */ zzif zzfy() {
        return super.zzfy();
    }

    public final /* bridge */ /* synthetic */ zzfc zzfz() {
        return super.zzfz();
    }

    public final /* bridge */ /* synthetic */ zzfe zzga() {
        return super.zzga();
    }

    public final /* bridge */ /* synthetic */ zzka zzgb() {
        return super.zzgb();
    }

    public final /* bridge */ /* synthetic */ zzjh zzgc() {
        return super.zzgc();
    }

    public final /* bridge */ /* synthetic */ zzgg zzgd() {
        return super.zzgd();
    }

    public final /* bridge */ /* synthetic */ zzfg zzge() {
        return super.zzge();
    }

    public final /* bridge */ /* synthetic */ zzfr zzgf() {
        return super.zzgf();
    }

    public final /* bridge */ /* synthetic */ zzef zzgg() {
        return super.zzgg();
    }

    protected final boolean zzhf() {
        return false;
    }

    public final /* bridge */ /* synthetic */ zzeb zziw() {
        return super.zziw();
    }

    public final /* bridge */ /* synthetic */ zzei zzix() {
        return super.zzix();
    }

    final boolean zzn(String str, String str2) {
        zzab();
        zzbt(str);
        if (zzby(str) && zzka.zzci(str2)) {
            return true;
        }
        if (zzbz(str) && zzka.zzcc(str2)) {
            return true;
        }
        Map map = (Map) this.zzali.get(str);
        if (map == null) {
            return false;
        }
        Boolean bool = (Boolean) map.get(str2);
        return bool == null ? false : bool.booleanValue();
    }

    final boolean zzo(String str, String str2) {
        zzab();
        zzbt(str);
        if ("ecommerce_purchase".equals(str2)) {
            return true;
        }
        Map map = (Map) this.zzalj.get(str);
        if (map == null) {
            return false;
        }
        Boolean bool = (Boolean) map.get(str2);
        return bool == null ? false : bool.booleanValue();
    }

    final int zzp(String str, String str2) {
        zzab();
        zzbt(str);
        Map map = (Map) this.zzall.get(str);
        if (map == null) {
            return 1;
        }
        Integer num = (Integer) map.get(str2);
        return num == null ? 1 : num.intValue();
    }
}
