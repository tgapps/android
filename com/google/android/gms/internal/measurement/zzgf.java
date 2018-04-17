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

public final class zzgf extends zzhk {
    private static int zzald = 65535;
    private static int zzale = 2;
    private final Map<String, Map<String, String>> zzalf = new ArrayMap();
    private final Map<String, Map<String, Boolean>> zzalg = new ArrayMap();
    private final Map<String, Map<String, Boolean>> zzalh = new ArrayMap();
    private final Map<String, zzkf> zzali = new ArrayMap();
    private final Map<String, Map<String, Integer>> zzalj = new ArrayMap();
    private final Map<String, String> zzalk = new ArrayMap();

    zzgf(zzgl com_google_android_gms_internal_measurement_zzgl) {
        super(com_google_android_gms_internal_measurement_zzgl);
    }

    private final zzkf zza(String str, byte[] bArr) {
        if (bArr == null) {
            return new zzkf();
        }
        zzaba zza = zzaba.zza(bArr, 0, bArr.length);
        zzabj com_google_android_gms_internal_measurement_zzkf = new zzkf();
        try {
            com_google_android_gms_internal_measurement_zzkf.zzb(zza);
            zzgg().zzir().zze("Parsed config. version, gmp_app_id", com_google_android_gms_internal_measurement_zzkf.zzask, com_google_android_gms_internal_measurement_zzkf.zzadh);
            return com_google_android_gms_internal_measurement_zzkf;
        } catch (IOException e) {
            zzgg().zzin().zze("Unable to merge remote config. appId", zzfg.zzbh(str), e);
            return new zzkf();
        }
    }

    private static Map<String, String> zza(zzkf com_google_android_gms_internal_measurement_zzkf) {
        Map<String, String> arrayMap = new ArrayMap();
        if (!(com_google_android_gms_internal_measurement_zzkf == null || com_google_android_gms_internal_measurement_zzkf.zzasm == null)) {
            for (zzkg com_google_android_gms_internal_measurement_zzkg : com_google_android_gms_internal_measurement_zzkf.zzasm) {
                if (com_google_android_gms_internal_measurement_zzkg != null) {
                    arrayMap.put(com_google_android_gms_internal_measurement_zzkg.zznt, com_google_android_gms_internal_measurement_zzkg.value);
                }
            }
        }
        return arrayMap;
    }

    private final void zza(String str, zzkf com_google_android_gms_internal_measurement_zzkf) {
        Map arrayMap = new ArrayMap();
        Map arrayMap2 = new ArrayMap();
        Map arrayMap3 = new ArrayMap();
        if (!(com_google_android_gms_internal_measurement_zzkf == null || com_google_android_gms_internal_measurement_zzkf.zzasn == null)) {
            for (zzke com_google_android_gms_internal_measurement_zzke : com_google_android_gms_internal_measurement_zzkf.zzasn) {
                if (TextUtils.isEmpty(com_google_android_gms_internal_measurement_zzke.name)) {
                    zzgg().zzin().log("EventConfig contained null event name");
                } else {
                    Object zzak = Event.zzak(com_google_android_gms_internal_measurement_zzke.name);
                    if (!TextUtils.isEmpty(zzak)) {
                        com_google_android_gms_internal_measurement_zzke.name = zzak;
                    }
                    arrayMap.put(com_google_android_gms_internal_measurement_zzke.name, com_google_android_gms_internal_measurement_zzke.zzash);
                    arrayMap2.put(com_google_android_gms_internal_measurement_zzke.name, com_google_android_gms_internal_measurement_zzke.zzasi);
                    if (com_google_android_gms_internal_measurement_zzke.zzasj != null) {
                        if (com_google_android_gms_internal_measurement_zzke.zzasj.intValue() >= zzale) {
                            if (com_google_android_gms_internal_measurement_zzke.zzasj.intValue() <= zzald) {
                                arrayMap3.put(com_google_android_gms_internal_measurement_zzke.name, com_google_android_gms_internal_measurement_zzke.zzasj);
                            }
                        }
                        zzgg().zzin().zze("Invalid sampling rate. Event name, sample rate", com_google_android_gms_internal_measurement_zzke.name, com_google_android_gms_internal_measurement_zzke.zzasj);
                    }
                }
            }
        }
        this.zzalg.put(str, arrayMap);
        this.zzalh.put(str, arrayMap2);
        this.zzalj.put(str, arrayMap3);
    }

    private final void zzbo(String str) {
        zzch();
        zzab();
        Preconditions.checkNotEmpty(str);
        if (this.zzali.get(str) == null) {
            byte[] zzaz = zzga().zzaz(str);
            if (zzaz == null) {
                this.zzalf.put(str, null);
                this.zzalg.put(str, null);
                this.zzalh.put(str, null);
                this.zzali.put(str, null);
                this.zzalk.put(str, null);
                this.zzalj.put(str, null);
                return;
            }
            zzkf zza = zza(str, zzaz);
            this.zzalf.put(str, zza(zza));
            zza(str, zza);
            this.zzali.put(str, zza);
            this.zzalk.put(str, null);
        }
    }

    public final /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    protected final boolean zza(String str, byte[] bArr, String str2) {
        zzgf com_google_android_gms_internal_measurement_zzgf = this;
        String str3 = str;
        zzch();
        zzab();
        Preconditions.checkNotEmpty(str);
        zzkf zza = zza(str, bArr);
        boolean z = false;
        if (zza == null) {
            return false;
        }
        byte[] bArr2;
        zza(str3, zza);
        com_google_android_gms_internal_measurement_zzgf.zzali.put(str3, zza);
        com_google_android_gms_internal_measurement_zzgf.zzalk.put(str3, str2);
        com_google_android_gms_internal_measurement_zzgf.zzalf.put(str3, zza(zza));
        zzhj zzft = zzft();
        zzjy[] com_google_android_gms_internal_measurement_zzjyArr = zza.zzaso;
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzjyArr);
        int length = com_google_android_gms_internal_measurement_zzjyArr.length;
        int i = 0;
        while (i < length) {
            int i2;
            zzjy com_google_android_gms_internal_measurement_zzjy = com_google_android_gms_internal_measurement_zzjyArr[i];
            zzjz[] com_google_android_gms_internal_measurement_zzjzArr = com_google_android_gms_internal_measurement_zzjy.zzari;
            int length2 = com_google_android_gms_internal_measurement_zzjzArr.length;
            int i3 = z;
            while (i3 < length2) {
                zzjz com_google_android_gms_internal_measurement_zzjz = com_google_android_gms_internal_measurement_zzjzArr[i3];
                String zzak = Event.zzak(com_google_android_gms_internal_measurement_zzjz.zzarl);
                if (zzak != null) {
                    com_google_android_gms_internal_measurement_zzjz.zzarl = zzak;
                }
                zzka[] com_google_android_gms_internal_measurement_zzkaArr = com_google_android_gms_internal_measurement_zzjz.zzarm;
                int length3 = com_google_android_gms_internal_measurement_zzkaArr.length;
                int i4 = z;
                while (i4 < length3) {
                    zzka com_google_android_gms_internal_measurement_zzka = com_google_android_gms_internal_measurement_zzkaArr[i4];
                    i2 = length;
                    String zzak2 = Param.zzak(com_google_android_gms_internal_measurement_zzka.zzart);
                    if (zzak2 != null) {
                        com_google_android_gms_internal_measurement_zzka.zzart = zzak2;
                    }
                    i4++;
                    length = i2;
                }
                i2 = length;
                i3++;
                z = false;
            }
            i2 = length;
            for (zzkc com_google_android_gms_internal_measurement_zzkc : com_google_android_gms_internal_measurement_zzjy.zzarh) {
                String zzak3 = UserProperty.zzak(com_google_android_gms_internal_measurement_zzkc.zzasa);
                if (zzak3 != null) {
                    com_google_android_gms_internal_measurement_zzkc.zzasa = zzak3;
                }
            }
            i++;
            length = i2;
            z = false;
        }
        zzft.zzga().zza(str3, com_google_android_gms_internal_measurement_zzjyArr);
        try {
            zza.zzaso = null;
            bArr2 = new byte[zza.zzwg()];
            zza.zza(zzabb.zzb(bArr2, 0, bArr2.length));
        } catch (IOException e) {
            zzgg().zzin().zze("Unable to serialize reduced-size config. Storing full config instead. appId", zzfg.zzbh(str), e);
            bArr2 = bArr;
        }
        zzhj zzga = zzga();
        Preconditions.checkNotEmpty(str);
        zzga.zzab();
        zzga.zzch();
        ContentValues contentValues = new ContentValues();
        contentValues.put("remote_config", bArr2);
        try {
            if (((long) zzga.getWritableDatabase().update("apps", contentValues, "app_id = ?", new String[]{str3})) == 0) {
                zzga.zzgg().zzil().zzg("Failed to update remote config (got 0). appId", zzfg.zzbh(str));
                return true;
            }
        } catch (SQLiteException e2) {
            zzga.zzgg().zzil().zze("Error storing remote config. appId", zzfg.zzbh(str), e2);
        }
        return true;
    }

    public final /* bridge */ /* synthetic */ void zzab() {
        super.zzab();
    }

    protected final zzkf zzbp(String str) {
        zzch();
        zzab();
        Preconditions.checkNotEmpty(str);
        zzbo(str);
        return (zzkf) this.zzali.get(str);
    }

    protected final String zzbq(String str) {
        zzab();
        return (String) this.zzalk.get(str);
    }

    protected final void zzbr(String str) {
        zzab();
        this.zzalk.put(str, null);
    }

    public final /* bridge */ /* synthetic */ Clock zzbt() {
        return super.zzbt();
    }

    public final /* bridge */ /* synthetic */ void zzfq() {
        super.zzfq();
    }

    public final /* bridge */ /* synthetic */ void zzfr() {
        super.zzfr();
    }

    public final /* bridge */ /* synthetic */ zzdx zzfs() {
        return super.zzfs();
    }

    public final /* bridge */ /* synthetic */ zzee zzft() {
        return super.zzft();
    }

    public final /* bridge */ /* synthetic */ zzhm zzfu() {
        return super.zzfu();
    }

    public final /* bridge */ /* synthetic */ zzfb zzfv() {
        return super.zzfv();
    }

    public final /* bridge */ /* synthetic */ zzeo zzfw() {
        return super.zzfw();
    }

    public final /* bridge */ /* synthetic */ zzil zzfx() {
        return super.zzfx();
    }

    public final /* bridge */ /* synthetic */ zzih zzfy() {
        return super.zzfy();
    }

    public final /* bridge */ /* synthetic */ zzfc zzfz() {
        return super.zzfz();
    }

    public final /* bridge */ /* synthetic */ zzei zzga() {
        return super.zzga();
    }

    public final /* bridge */ /* synthetic */ zzfe zzgb() {
        return super.zzgb();
    }

    public final /* bridge */ /* synthetic */ zzjv zzgc() {
        return super.zzgc();
    }

    public final /* bridge */ /* synthetic */ zzgf zzgd() {
        return super.zzgd();
    }

    public final /* bridge */ /* synthetic */ zzjk zzge() {
        return super.zzge();
    }

    public final /* bridge */ /* synthetic */ zzgg zzgf() {
        return super.zzgf();
    }

    public final /* bridge */ /* synthetic */ zzfg zzgg() {
        return super.zzgg();
    }

    public final /* bridge */ /* synthetic */ zzfr zzgh() {
        return super.zzgh();
    }

    public final /* bridge */ /* synthetic */ zzeh zzgi() {
        return super.zzgi();
    }

    protected final boolean zzhh() {
        return false;
    }

    final String zzm(String str, String str2) {
        zzab();
        zzbo(str);
        Map map = (Map) this.zzalf.get(str);
        return map != null ? (String) map.get(str2) : null;
    }

    final boolean zzn(String str, String str2) {
        zzab();
        zzbo(str);
        if (zzgc().zzce(str) && zzjv.zzcb(str2)) {
            return true;
        }
        if (zzgc().zzcf(str) && zzjv.zzbv(str2)) {
            return true;
        }
        Map map = (Map) this.zzalg.get(str);
        if (map == null) {
            return false;
        }
        Boolean bool = (Boolean) map.get(str2);
        return bool == null ? false : bool.booleanValue();
    }

    final boolean zzo(String str, String str2) {
        zzab();
        zzbo(str);
        if ("ecommerce_purchase".equals(str2)) {
            return true;
        }
        Map map = (Map) this.zzalh.get(str);
        if (map == null) {
            return false;
        }
        Boolean bool = (Boolean) map.get(str2);
        return bool == null ? false : bool.booleanValue();
    }

    final int zzp(String str, String str2) {
        zzab();
        zzbo(str);
        Map map = (Map) this.zzalj.get(str);
        if (map == null) {
            return 1;
        }
        Integer num = (Integer) map.get(str2);
        return num == null ? 1 : num.intValue();
    }
}
