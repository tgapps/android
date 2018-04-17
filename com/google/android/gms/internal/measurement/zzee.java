package com.google.android.gms.internal.measurement;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Pair;
import com.google.android.gms.common.internal.Preconditions;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

final class zzee extends zzhk {
    zzee(zzgl com_google_android_gms_internal_measurement_zzgl) {
        super(com_google_android_gms_internal_measurement_zzgl);
    }

    private final Boolean zza(double d, zzkb com_google_android_gms_internal_measurement_zzkb) {
        try {
            return zza(new BigDecimal(d), com_google_android_gms_internal_measurement_zzkb, Math.ulp(d));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private final Boolean zza(long j, zzkb com_google_android_gms_internal_measurement_zzkb) {
        try {
            return zza(new BigDecimal(j), com_google_android_gms_internal_measurement_zzkb, 0.0d);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static Boolean zza(Boolean bool, boolean z) {
        return bool == null ? null : Boolean.valueOf(bool.booleanValue() ^ z);
    }

    private final Boolean zza(String str, int i, boolean z, String str2, List<String> list, String str3) {
        if (str == null) {
            return null;
        }
        boolean startsWith;
        if (i == 6) {
            if (list == null || list.size() == 0) {
                return null;
            }
        } else if (str2 == null) {
            return null;
        }
        if (!z) {
            if (i != 1) {
                CharSequence toUpperCase = str.toUpperCase(Locale.ENGLISH);
            }
        }
        switch (i) {
            case 1:
                try {
                    return Boolean.valueOf(Pattern.compile(str3, z ? 0 : 66).matcher(toUpperCase).matches());
                } catch (PatternSyntaxException e) {
                    zzgg().zzin().zzg("Invalid regular expression in REGEXP audience filter. expression", str3);
                    return null;
                }
            case 2:
                startsWith = toUpperCase.startsWith(str2);
                break;
            case 3:
                startsWith = toUpperCase.endsWith(str2);
                break;
            case 4:
                startsWith = toUpperCase.contains(str2);
                break;
            case 5:
                startsWith = toUpperCase.equals(str2);
                break;
            case 6:
                startsWith = list.contains(toUpperCase);
                break;
            default:
                return null;
        }
        return Boolean.valueOf(startsWith);
    }

    private final Boolean zza(String str, zzkb com_google_android_gms_internal_measurement_zzkb) {
        if (!zzjv.zzcd(str)) {
            return null;
        }
        try {
            return zza(new BigDecimal(str), com_google_android_gms_internal_measurement_zzkb, 0.0d);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private final Boolean zza(String str, zzkd com_google_android_gms_internal_measurement_zzkd) {
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzkd);
        if (str == null || com_google_android_gms_internal_measurement_zzkd.zzasc == null || com_google_android_gms_internal_measurement_zzkd.zzasc.intValue() == 0) {
            return null;
        }
        String toUpperCase;
        String str2;
        List list;
        String[] strArr;
        List arrayList;
        int length;
        if (com_google_android_gms_internal_measurement_zzkd.zzasc.intValue() == 6) {
            if (com_google_android_gms_internal_measurement_zzkd.zzasf == null || com_google_android_gms_internal_measurement_zzkd.zzasf.length == 0) {
                return null;
            }
        } else if (com_google_android_gms_internal_measurement_zzkd.zzasd == null) {
            return null;
        }
        int intValue = com_google_android_gms_internal_measurement_zzkd.zzasc.intValue();
        int i = 0;
        boolean z = com_google_android_gms_internal_measurement_zzkd.zzase != null && com_google_android_gms_internal_measurement_zzkd.zzase.booleanValue();
        if (!(z || intValue == 1)) {
            if (intValue != 6) {
                toUpperCase = com_google_android_gms_internal_measurement_zzkd.zzasd.toUpperCase(Locale.ENGLISH);
                str2 = toUpperCase;
                if (com_google_android_gms_internal_measurement_zzkd.zzasf != null) {
                    list = null;
                } else {
                    strArr = com_google_android_gms_internal_measurement_zzkd.zzasf;
                    if (z) {
                        arrayList = new ArrayList();
                        length = strArr.length;
                        while (i < length) {
                            arrayList.add(strArr[i].toUpperCase(Locale.ENGLISH));
                            i++;
                        }
                        list = arrayList;
                    } else {
                        list = Arrays.asList(strArr);
                    }
                }
                return zza(str, intValue, z, str2, list, intValue != 1 ? str2 : null);
            }
        }
        toUpperCase = com_google_android_gms_internal_measurement_zzkd.zzasd;
        str2 = toUpperCase;
        if (com_google_android_gms_internal_measurement_zzkd.zzasf != null) {
            strArr = com_google_android_gms_internal_measurement_zzkd.zzasf;
            if (z) {
                arrayList = new ArrayList();
                length = strArr.length;
                while (i < length) {
                    arrayList.add(strArr[i].toUpperCase(Locale.ENGLISH));
                    i++;
                }
                list = arrayList;
            } else {
                list = Arrays.asList(strArr);
            }
        } else {
            list = null;
        }
        if (intValue != 1) {
        }
        return zza(str, intValue, z, str2, list, intValue != 1 ? str2 : null);
    }

    private static Boolean zza(BigDecimal bigDecimal, zzkb com_google_android_gms_internal_measurement_zzkb, double d) {
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzkb);
        if (com_google_android_gms_internal_measurement_zzkb.zzaru == null || com_google_android_gms_internal_measurement_zzkb.zzaru.intValue() == 0) {
            return null;
        }
        BigDecimal bigDecimal2;
        BigDecimal bigDecimal3;
        if (com_google_android_gms_internal_measurement_zzkb.zzaru.intValue() == 4) {
            if (com_google_android_gms_internal_measurement_zzkb.zzarx == null || com_google_android_gms_internal_measurement_zzkb.zzary == null) {
                return null;
            }
        } else if (com_google_android_gms_internal_measurement_zzkb.zzarw == null) {
            return null;
        }
        int intValue = com_google_android_gms_internal_measurement_zzkb.zzaru.intValue();
        BigDecimal bigDecimal4;
        if (com_google_android_gms_internal_measurement_zzkb.zzaru.intValue() == 4) {
            if (!zzjv.zzcd(com_google_android_gms_internal_measurement_zzkb.zzarx) || !zzjv.zzcd(com_google_android_gms_internal_measurement_zzkb.zzary)) {
                return null;
            }
            try {
                bigDecimal2 = new BigDecimal(com_google_android_gms_internal_measurement_zzkb.zzarx);
                bigDecimal4 = new BigDecimal(com_google_android_gms_internal_measurement_zzkb.zzary);
                bigDecimal3 = bigDecimal2;
                bigDecimal2 = null;
            } catch (NumberFormatException e) {
                return null;
            }
        } else if (!zzjv.zzcd(com_google_android_gms_internal_measurement_zzkb.zzarw)) {
            return null;
        } else {
            try {
                bigDecimal2 = new BigDecimal(com_google_android_gms_internal_measurement_zzkb.zzarw);
                bigDecimal3 = null;
                bigDecimal4 = bigDecimal3;
            } catch (NumberFormatException e2) {
                return null;
            }
        }
        if (intValue == 4) {
            if (bigDecimal3 == null) {
                return null;
            }
        } else if (bigDecimal2 == null) {
            return null;
        }
        boolean z = false;
        switch (intValue) {
            case 1:
                if (bigDecimal.compareTo(bigDecimal2) == -1) {
                    z = true;
                }
                return Boolean.valueOf(z);
            case 2:
                if (bigDecimal.compareTo(bigDecimal2) == 1) {
                    z = true;
                }
                return Boolean.valueOf(z);
            case 3:
                if (d != 0.0d) {
                    if (bigDecimal.compareTo(bigDecimal2.subtract(new BigDecimal(d).multiply(new BigDecimal(2)))) == 1 && bigDecimal.compareTo(bigDecimal2.add(new BigDecimal(d).multiply(new BigDecimal(2)))) == -1) {
                        z = true;
                    }
                    return Boolean.valueOf(z);
                }
                if (bigDecimal.compareTo(bigDecimal2) == 0) {
                    z = true;
                }
                return Boolean.valueOf(z);
            case 4:
                if (!(bigDecimal.compareTo(bigDecimal3) == -1 || bigDecimal.compareTo(r4) == 1)) {
                    z = true;
                }
                return Boolean.valueOf(z);
            default:
                return null;
        }
    }

    final zzkh[] zza(String str, zzki[] com_google_android_gms_internal_measurement_zzkiArr, zzkn[] com_google_android_gms_internal_measurement_zzknArr) {
        int intValue;
        int i;
        Map map;
        Map map2;
        int length;
        Object obj;
        Object obj2;
        Long l;
        long j;
        String str2;
        int i2;
        int i3;
        SQLiteException e;
        int i4;
        int i5;
        zzkj[] com_google_android_gms_internal_measurement_zzkjArr;
        ArrayMap arrayMap;
        zzkj[] com_google_android_gms_internal_measurement_zzkjArr2;
        int i6;
        Long l2;
        long j2;
        zzeq zze;
        HashSet hashSet;
        zzkj[] com_google_android_gms_internal_measurement_zzkjArr3;
        long j3;
        String str3;
        long j4;
        Iterator it;
        HashSet hashSet2;
        BitSet bitSet;
        BitSet bitSet2;
        zzkh com_google_android_gms_internal_measurement_zzkh;
        BitSet bitSet3;
        Iterator it2;
        Map map3;
        Iterator it3;
        Iterator it4;
        Map map4;
        Map map5;
        Map map6;
        Boolean zza;
        long j5;
        Boolean valueOf;
        zzkj[] com_google_android_gms_internal_measurement_zzkjArr4;
        Set hashSet3;
        zzfi zzin;
        String str4;
        Object zzbe;
        Object zzbf;
        zzka[] com_google_android_gms_internal_measurement_zzkaArr;
        zzka[] com_google_android_gms_internal_measurement_zzkaArr2;
        int i7;
        String str5;
        boolean z;
        ArrayMap arrayMap2;
        int intValue2;
        Map map7;
        Map map8;
        Map map9;
        Object obj3;
        zzfi zzil;
        String str6;
        zzee com_google_android_gms_internal_measurement_zzee = this;
        String str7 = str;
        zzki[] com_google_android_gms_internal_measurement_zzkiArr2 = com_google_android_gms_internal_measurement_zzkiArr;
        zzkn[] com_google_android_gms_internal_measurement_zzknArr2 = com_google_android_gms_internal_measurement_zzknArr;
        Preconditions.checkNotEmpty(str);
        Set hashSet4 = new HashSet();
        Map arrayMap3 = new ArrayMap();
        Map arrayMap4 = new ArrayMap();
        Map arrayMap5 = new ArrayMap();
        Map zzba = zzga().zzba(str7);
        if (zzba != null) {
            Iterator it5 = zzba.keySet().iterator();
            while (it5.hasNext()) {
                Iterator it6;
                intValue = ((Integer) it5.next()).intValue();
                zzkm com_google_android_gms_internal_measurement_zzkm = (zzkm) zzba.get(Integer.valueOf(intValue));
                BitSet bitSet4 = (BitSet) arrayMap4.get(Integer.valueOf(intValue));
                BitSet bitSet5 = (BitSet) arrayMap5.get(Integer.valueOf(intValue));
                if (bitSet4 == null) {
                    bitSet4 = new BitSet();
                    arrayMap4.put(Integer.valueOf(intValue), bitSet4);
                    bitSet5 = new BitSet();
                    arrayMap5.put(Integer.valueOf(intValue), bitSet5);
                }
                Map map10 = zzba;
                i = 0;
                while (i < (com_google_android_gms_internal_measurement_zzkm.zzauf.length << 6)) {
                    if (zzjv.zza(com_google_android_gms_internal_measurement_zzkm.zzauf, i)) {
                        it6 = it5;
                        map = arrayMap4;
                        map2 = arrayMap5;
                        zzgg().zzir().zze("Filter already evaluated. audience ID, filter ID", Integer.valueOf(intValue), Integer.valueOf(i));
                        bitSet5.set(i);
                        if (zzjv.zza(com_google_android_gms_internal_measurement_zzkm.zzaug, i)) {
                            bitSet4.set(i);
                        }
                    } else {
                        it6 = it5;
                        map = arrayMap4;
                        map2 = arrayMap5;
                    }
                    i++;
                    it5 = it6;
                    arrayMap4 = map;
                    arrayMap5 = map2;
                }
                it6 = it5;
                map = arrayMap4;
                map2 = arrayMap5;
                zzkh com_google_android_gms_internal_measurement_zzkh2 = new zzkh();
                arrayMap3.put(Integer.valueOf(intValue), com_google_android_gms_internal_measurement_zzkh2);
                com_google_android_gms_internal_measurement_zzkh2.zzast = Boolean.valueOf(false);
                com_google_android_gms_internal_measurement_zzkh2.zzass = com_google_android_gms_internal_measurement_zzkm;
                com_google_android_gms_internal_measurement_zzkh2.zzasr = new zzkm();
                com_google_android_gms_internal_measurement_zzkh2.zzasr.zzaug = zzjv.zza(bitSet4);
                com_google_android_gms_internal_measurement_zzkh2.zzasr.zzauf = zzjv.zza(bitSet5);
                zzba = map10;
                it5 = it6;
            }
        }
        map = arrayMap4;
        map2 = arrayMap5;
        if (com_google_android_gms_internal_measurement_zzkiArr2 != null) {
            ArrayMap arrayMap6 = new ArrayMap();
            length = com_google_android_gms_internal_measurement_zzkiArr2.length;
            int i8 = 0;
            Long l3 = null;
            zzki com_google_android_gms_internal_measurement_zzki = null;
            long j6 = 0;
            while (i8 < length) {
                int i9;
                int length2;
                String str8;
                Map map11;
                Map map12;
                Map map13;
                Map map14;
                Map map15;
                String str9;
                Map map16;
                Map map17;
                zzjz com_google_android_gms_internal_measurement_zzjz;
                int i10;
                int length3;
                zzkj com_google_android_gms_internal_measurement_zzkj;
                zzka com_google_android_gms_internal_measurement_zzka;
                boolean equals;
                Boolean zza2;
                zzki com_google_android_gms_internal_measurement_zzki2 = com_google_android_gms_internal_measurement_zzkiArr2[i8];
                String str10 = com_google_android_gms_internal_measurement_zzki2.name;
                zzkj[] com_google_android_gms_internal_measurement_zzkjArr5 = com_google_android_gms_internal_measurement_zzki2.zzasv;
                if (zzgi().zzd(str7, zzew.zzahy)) {
                    int i11;
                    zzki com_google_android_gms_internal_measurement_zzki3;
                    zzki com_google_android_gms_internal_measurement_zzki4;
                    zzhj zzga;
                    SQLiteDatabase writableDatabase;
                    ArrayMap arrayMap7;
                    zzkj[] com_google_android_gms_internal_measurement_zzkjArr6;
                    String[] strArr;
                    zzkj[] com_google_android_gms_internal_measurement_zzkjArr7;
                    zzkj com_google_android_gms_internal_measurement_zzkj2;
                    long j7;
                    Pair zza3;
                    zzki com_google_android_gms_internal_measurement_zzki5;
                    Long valueOf2;
                    Long l4;
                    zzgc();
                    Long l5 = (Long) zzjv.zzb(com_google_android_gms_internal_measurement_zzki2, "_eid");
                    obj = l5 != null ? 1 : null;
                    if (obj != null) {
                        i11 = i8;
                        if (str10.equals("_ep")) {
                            obj2 = 1;
                            if (obj2 == null) {
                                zzgc();
                                str10 = (String) zzjv.zzb(com_google_android_gms_internal_measurement_zzki2, "_en");
                                if (!TextUtils.isEmpty(str10)) {
                                    if (!(com_google_android_gms_internal_measurement_zzki == null || l3 == null)) {
                                        if (l5.longValue() != l3.longValue()) {
                                            com_google_android_gms_internal_measurement_zzki3 = com_google_android_gms_internal_measurement_zzki;
                                            l = l3;
                                            com_google_android_gms_internal_measurement_zzki4 = com_google_android_gms_internal_measurement_zzki3;
                                            j = j6 - 1;
                                            if (j > 0) {
                                                zzga = zzga();
                                                zzga.zzab();
                                                zzga.zzgg().zzir().zzg("Clearing complex main event info. appId", str7);
                                                try {
                                                    writableDatabase = zzga.getWritableDatabase();
                                                    str2 = "delete from main_event_params where app_id=?";
                                                    arrayMap7 = arrayMap6;
                                                    com_google_android_gms_internal_measurement_zzkjArr6 = com_google_android_gms_internal_measurement_zzkjArr5;
                                                    i2 = 1;
                                                    try {
                                                        strArr = new String[1];
                                                        i3 = 0;
                                                    } catch (SQLiteException e2) {
                                                        e = e2;
                                                        i3 = 0;
                                                        zzga.zzgg().zzil().zzg("Error clearing complex main event", e);
                                                        i4 = length;
                                                        i9 = i2;
                                                        i5 = i11;
                                                        com_google_android_gms_internal_measurement_zzkjArr = com_google_android_gms_internal_measurement_zzkjArr6;
                                                        arrayMap = arrayMap7;
                                                        com_google_android_gms_internal_measurement_zzkjArr7 = new zzkj[(com_google_android_gms_internal_measurement_zzki4.zzasv.length + com_google_android_gms_internal_measurement_zzkjArr.length)];
                                                        com_google_android_gms_internal_measurement_zzkjArr2 = com_google_android_gms_internal_measurement_zzki4.zzasv;
                                                        intValue = com_google_android_gms_internal_measurement_zzkjArr2.length;
                                                        i6 = i3;
                                                        length = i6;
                                                        while (i6 < intValue) {
                                                            com_google_android_gms_internal_measurement_zzkj2 = com_google_android_gms_internal_measurement_zzkjArr2[i6];
                                                            zzgc();
                                                            if (zzjv.zza(com_google_android_gms_internal_measurement_zzki2, com_google_android_gms_internal_measurement_zzkj2.name) != null) {
                                                                i9 = length + 1;
                                                                com_google_android_gms_internal_measurement_zzkjArr7[length] = com_google_android_gms_internal_measurement_zzkj2;
                                                                length = i9;
                                                            }
                                                            i6++;
                                                        }
                                                        if (length > 0) {
                                                            zzgg().zzin().zzg("No unique parameters in main event. eventName", str10);
                                                        } else {
                                                            length2 = com_google_android_gms_internal_measurement_zzkjArr.length;
                                                            intValue = i3;
                                                            while (intValue < length2) {
                                                                i2 = length + 1;
                                                                com_google_android_gms_internal_measurement_zzkjArr7[length] = com_google_android_gms_internal_measurement_zzkjArr[intValue];
                                                                intValue++;
                                                                length = i2;
                                                            }
                                                            if (length != com_google_android_gms_internal_measurement_zzkjArr7.length) {
                                                                com_google_android_gms_internal_measurement_zzkjArr7 = (zzkj[]) Arrays.copyOf(com_google_android_gms_internal_measurement_zzkjArr7, length);
                                                            }
                                                            com_google_android_gms_internal_measurement_zzkjArr = com_google_android_gms_internal_measurement_zzkjArr7;
                                                        }
                                                        str8 = str10;
                                                        l2 = l;
                                                        j2 = 0;
                                                        com_google_android_gms_internal_measurement_zzki = com_google_android_gms_internal_measurement_zzki4;
                                                        zze = zzga().zze(str7, com_google_android_gms_internal_measurement_zzki2.name);
                                                        if (zze != null) {
                                                            zzgg().zzin().zze("Event aggregate wasn't created during raw event logging. appId, event", zzfg.zzbh(str), zzgb().zzbe(str8));
                                                            map11 = map;
                                                            map12 = map2;
                                                            hashSet = hashSet4;
                                                            map13 = arrayMap3;
                                                            com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr;
                                                            j3 = j2;
                                                            str3 = str8;
                                                            zze = new zzeq(str7, com_google_android_gms_internal_measurement_zzki2.name, 1, 1, com_google_android_gms_internal_measurement_zzki2.zzasw.longValue(), 0, null, null, null);
                                                        } else {
                                                            hashSet = hashSet4;
                                                            map13 = arrayMap3;
                                                            com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr;
                                                            str3 = str8;
                                                            map11 = map;
                                                            map12 = map2;
                                                            j3 = j2;
                                                            zze = zze.zzie();
                                                        }
                                                        zzga().zza(zze);
                                                        j4 = zze.zzafp;
                                                        map14 = arrayMap;
                                                        str10 = str3;
                                                        map15 = (Map) map14.get(str10);
                                                        if (map15 != null) {
                                                            str9 = str;
                                                            map15 = zzga().zzj(str9, str10);
                                                            if (map15 == null) {
                                                                map15 = new ArrayMap();
                                                            }
                                                            map14.put(str10, map15);
                                                        } else {
                                                            str9 = str;
                                                        }
                                                        it = map15.keySet().iterator();
                                                        while (it.hasNext()) {
                                                            i = ((Integer) it.next()).intValue();
                                                            hashSet2 = hashSet;
                                                            if (hashSet2.contains(Integer.valueOf(i))) {
                                                                arrayMap3 = map13;
                                                                map16 = map11;
                                                                bitSet = (BitSet) map16.get(Integer.valueOf(i));
                                                                map17 = map14;
                                                                map14 = map12;
                                                                bitSet2 = (BitSet) map14.get(Integer.valueOf(i));
                                                                if (((zzkh) arrayMap3.get(Integer.valueOf(i))) == null) {
                                                                    com_google_android_gms_internal_measurement_zzkh = new zzkh();
                                                                    arrayMap3.put(Integer.valueOf(i), com_google_android_gms_internal_measurement_zzkh);
                                                                    com_google_android_gms_internal_measurement_zzkh.zzast = Boolean.valueOf(true);
                                                                    bitSet3 = new BitSet();
                                                                    map16.put(Integer.valueOf(i), bitSet3);
                                                                    bitSet2 = new BitSet();
                                                                    map14.put(Integer.valueOf(i), bitSet2);
                                                                    bitSet = bitSet3;
                                                                }
                                                                it2 = ((List) map15.get(Integer.valueOf(i))).iterator();
                                                                while (it2.hasNext()) {
                                                                    map3 = map15;
                                                                    com_google_android_gms_internal_measurement_zzjz = (zzjz) it2.next();
                                                                    it3 = it;
                                                                    it4 = it2;
                                                                    if (zzgg().isLoggable(2)) {
                                                                        map4 = map14;
                                                                        map5 = arrayMap3;
                                                                        map6 = map16;
                                                                    } else {
                                                                        map4 = map14;
                                                                        map6 = map16;
                                                                        map5 = arrayMap3;
                                                                        zzgg().zzir().zzd("Evaluating filter. audience, filter, event", Integer.valueOf(i), com_google_android_gms_internal_measurement_zzjz.zzark, zzgb().zzbe(com_google_android_gms_internal_measurement_zzjz.zzarl));
                                                                        zzgg().zzir().zzg("Filter definition", zzgb().zza(com_google_android_gms_internal_measurement_zzjz));
                                                                    }
                                                                    if (com_google_android_gms_internal_measurement_zzjz.zzark != null) {
                                                                        if (com_google_android_gms_internal_measurement_zzjz.zzark.intValue() > 256) {
                                                                            if (bitSet.get(com_google_android_gms_internal_measurement_zzjz.zzark.intValue())) {
                                                                                if (com_google_android_gms_internal_measurement_zzjz.zzaro != null) {
                                                                                    zza = zza(j4, com_google_android_gms_internal_measurement_zzjz.zzaro);
                                                                                    if (zza != null) {
                                                                                        if (!zza.booleanValue()) {
                                                                                            j5 = j4;
                                                                                            valueOf = Boolean.valueOf(false);
                                                                                            com_google_android_gms_internal_measurement_zzkjArr4 = com_google_android_gms_internal_measurement_zzkjArr3;
                                                                                            zzgg().zzir().zzg("Event filter result", valueOf != null ? "null" : valueOf);
                                                                                            if (valueOf != null) {
                                                                                                hashSet2.add(Integer.valueOf(i));
                                                                                            } else {
                                                                                                bitSet2.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                                                                if (valueOf.booleanValue()) {
                                                                                                    bitSet.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                                                                }
                                                                                            }
                                                                                            com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr4;
                                                                                            map15 = map3;
                                                                                            it = it3;
                                                                                            it2 = it4;
                                                                                            map14 = map4;
                                                                                            map16 = map6;
                                                                                            arrayMap3 = map5;
                                                                                            j4 = j5;
                                                                                        }
                                                                                    }
                                                                                    j5 = j4;
                                                                                    com_google_android_gms_internal_measurement_zzkjArr4 = com_google_android_gms_internal_measurement_zzkjArr3;
                                                                                    valueOf = null;
                                                                                    if (valueOf != null) {
                                                                                    }
                                                                                    zzgg().zzir().zzg("Event filter result", valueOf != null ? "null" : valueOf);
                                                                                    if (valueOf != null) {
                                                                                        bitSet2.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                                                        if (valueOf.booleanValue()) {
                                                                                            bitSet.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                                                        }
                                                                                    } else {
                                                                                        hashSet2.add(Integer.valueOf(i));
                                                                                    }
                                                                                    com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr4;
                                                                                    map15 = map3;
                                                                                    it = it3;
                                                                                    it2 = it4;
                                                                                    map14 = map4;
                                                                                    map16 = map6;
                                                                                    arrayMap3 = map5;
                                                                                    j4 = j5;
                                                                                }
                                                                                hashSet3 = new HashSet();
                                                                                for (zzka com_google_android_gms_internal_measurement_zzka2 : com_google_android_gms_internal_measurement_zzjz.zzarm) {
                                                                                    if (TextUtils.isEmpty(com_google_android_gms_internal_measurement_zzka2.zzart)) {
                                                                                        zzgg().zzin().zzg("null or empty param name in filter. event", zzgb().zzbe(str10));
                                                                                        j5 = j4;
                                                                                        com_google_android_gms_internal_measurement_zzkjArr4 = com_google_android_gms_internal_measurement_zzkjArr3;
                                                                                        break;
                                                                                    }
                                                                                    hashSet3.add(com_google_android_gms_internal_measurement_zzka2.zzart);
                                                                                }
                                                                                map14 = new ArrayMap();
                                                                                com_google_android_gms_internal_measurement_zzkjArr4 = com_google_android_gms_internal_measurement_zzkjArr3;
                                                                                length3 = com_google_android_gms_internal_measurement_zzkjArr4.length;
                                                                                i10 = 0;
                                                                                while (i10 < length3) {
                                                                                    com_google_android_gms_internal_measurement_zzkj = com_google_android_gms_internal_measurement_zzkjArr4[i10];
                                                                                    j5 = j4;
                                                                                    if (!hashSet3.contains(com_google_android_gms_internal_measurement_zzkj.name)) {
                                                                                        if (com_google_android_gms_internal_measurement_zzkj.zzasz == null) {
                                                                                            if (com_google_android_gms_internal_measurement_zzkj.zzaqx == null) {
                                                                                                if (com_google_android_gms_internal_measurement_zzkj.zzajf != null) {
                                                                                                    zzin = zzgg().zzin();
                                                                                                    str4 = "Unknown value for param. event, param";
                                                                                                    zzbe = zzgb().zzbe(str10);
                                                                                                    zzbf = zzgb().zzbf(com_google_android_gms_internal_measurement_zzkj.name);
                                                                                                    break;
                                                                                                }
                                                                                                obj2 = com_google_android_gms_internal_measurement_zzkj.name;
                                                                                                obj = com_google_android_gms_internal_measurement_zzkj.zzajf;
                                                                                            } else {
                                                                                                obj2 = com_google_android_gms_internal_measurement_zzkj.name;
                                                                                                obj = com_google_android_gms_internal_measurement_zzkj.zzaqx;
                                                                                            }
                                                                                        } else {
                                                                                            obj2 = com_google_android_gms_internal_measurement_zzkj.name;
                                                                                            obj = com_google_android_gms_internal_measurement_zzkj.zzasz;
                                                                                        }
                                                                                        map14.put(obj2, obj);
                                                                                    }
                                                                                    i10++;
                                                                                    j4 = j5;
                                                                                }
                                                                                j5 = j4;
                                                                                com_google_android_gms_internal_measurement_zzkaArr = com_google_android_gms_internal_measurement_zzjz.zzarm;
                                                                                length2 = com_google_android_gms_internal_measurement_zzkaArr.length;
                                                                                i2 = 0;
                                                                                while (i2 < length2) {
                                                                                    com_google_android_gms_internal_measurement_zzka = com_google_android_gms_internal_measurement_zzkaArr[i2];
                                                                                    equals = Boolean.TRUE.equals(com_google_android_gms_internal_measurement_zzka.zzars);
                                                                                    str8 = com_google_android_gms_internal_measurement_zzka.zzart;
                                                                                    if (TextUtils.isEmpty(str8)) {
                                                                                        com_google_android_gms_internal_measurement_zzkaArr2 = com_google_android_gms_internal_measurement_zzkaArr;
                                                                                        obj2 = map14.get(str8);
                                                                                        i7 = length2;
                                                                                        if (obj2 instanceof Long) {
                                                                                            if (obj2 instanceof Double) {
                                                                                                if (!(obj2 instanceof String)) {
                                                                                                    if (com_google_android_gms_internal_measurement_zzka.zzarq != null) {
                                                                                                        zza2 = zza((String) obj2, com_google_android_gms_internal_measurement_zzka.zzarq);
                                                                                                    } else if (com_google_android_gms_internal_measurement_zzka.zzarr == null) {
                                                                                                        str5 = (String) obj2;
                                                                                                        if (zzjv.zzcd(str5)) {
                                                                                                            zzin = zzgg().zzin();
                                                                                                            str4 = "Invalid param value for number filter. event, param";
                                                                                                        } else {
                                                                                                            zza2 = zza(str5, com_google_android_gms_internal_measurement_zzka.zzarr);
                                                                                                        }
                                                                                                    } else {
                                                                                                        zzin = zzgg().zzin();
                                                                                                        str4 = "No filter for String param. event, param";
                                                                                                    }
                                                                                                    if (zza2 == null) {
                                                                                                        if (((zza2.booleanValue() ^ 1) ^ equals) != 0) {
                                                                                                            z = false;
                                                                                                            break;
                                                                                                        }
                                                                                                        i2++;
                                                                                                        com_google_android_gms_internal_measurement_zzkaArr = com_google_android_gms_internal_measurement_zzkaArr2;
                                                                                                        length2 = i7;
                                                                                                    }
                                                                                                } else if (obj2 == null) {
                                                                                                    zzgg().zzir().zze("Missing param for filter. event, param", zzgb().zzbe(str10), zzgb().zzbf(str8));
                                                                                                    z = false;
                                                                                                    break;
                                                                                                } else {
                                                                                                    zzin = zzgg().zzin();
                                                                                                    str4 = "Unknown param type. event, param";
                                                                                                }
                                                                                            } else if (com_google_android_gms_internal_measurement_zzka.zzarr != null) {
                                                                                                zzin = zzgg().zzin();
                                                                                                str4 = "No number filter for double param. event, param";
                                                                                            } else {
                                                                                                zza2 = zza(((Double) obj2).doubleValue(), com_google_android_gms_internal_measurement_zzka.zzarr);
                                                                                                if (zza2 == null) {
                                                                                                    if (((zza2.booleanValue() ^ 1) ^ equals) != 0) {
                                                                                                        z = false;
                                                                                                        break;
                                                                                                    }
                                                                                                    i2++;
                                                                                                    com_google_android_gms_internal_measurement_zzkaArr = com_google_android_gms_internal_measurement_zzkaArr2;
                                                                                                    length2 = i7;
                                                                                                }
                                                                                            }
                                                                                        } else if (com_google_android_gms_internal_measurement_zzka.zzarr != null) {
                                                                                            zzin = zzgg().zzin();
                                                                                            str4 = "No number filter for long param. event, param";
                                                                                        } else {
                                                                                            zza2 = zza(((Long) obj2).longValue(), com_google_android_gms_internal_measurement_zzka.zzarr);
                                                                                            if (zza2 == null) {
                                                                                                if (((zza2.booleanValue() ^ 1) ^ equals) != 0) {
                                                                                                    z = false;
                                                                                                    break;
                                                                                                }
                                                                                                i2++;
                                                                                                com_google_android_gms_internal_measurement_zzkaArr = com_google_android_gms_internal_measurement_zzkaArr2;
                                                                                                length2 = i7;
                                                                                            }
                                                                                        }
                                                                                        zzbe = zzgb().zzbe(str10);
                                                                                        zzbf = zzgb().zzbf(str8);
                                                                                        zzin.zze(str4, zzbe, zzbf);
                                                                                    } else {
                                                                                        zzgg().zzin().zzg("Event has empty param name. event", zzgb().zzbe(str10));
                                                                                    }
                                                                                    valueOf = null;
                                                                                }
                                                                                z = true;
                                                                                valueOf = Boolean.valueOf(z);
                                                                                if (valueOf != null) {
                                                                                }
                                                                                zzgg().zzir().zzg("Event filter result", valueOf != null ? "null" : valueOf);
                                                                                if (valueOf != null) {
                                                                                    hashSet2.add(Integer.valueOf(i));
                                                                                } else {
                                                                                    bitSet2.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                                                    if (valueOf.booleanValue()) {
                                                                                        bitSet.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                                                    }
                                                                                }
                                                                                com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr4;
                                                                                map15 = map3;
                                                                                it = it3;
                                                                                it2 = it4;
                                                                                map14 = map4;
                                                                                map16 = map6;
                                                                                arrayMap3 = map5;
                                                                                j4 = j5;
                                                                            } else {
                                                                                zzgg().zzir().zze("Event filter already evaluated true. audience ID, filter ID", Integer.valueOf(i), com_google_android_gms_internal_measurement_zzjz.zzark);
                                                                                map15 = map3;
                                                                                it = it3;
                                                                                it2 = it4;
                                                                                map14 = map4;
                                                                                map16 = map6;
                                                                                arrayMap3 = map5;
                                                                            }
                                                                            str9 = str;
                                                                        }
                                                                    }
                                                                    j5 = j4;
                                                                    com_google_android_gms_internal_measurement_zzkjArr4 = com_google_android_gms_internal_measurement_zzkjArr3;
                                                                    str2 = str;
                                                                    zzgg().zzin().zze("Invalid event filter ID. appId, id", zzfg.zzbh(str), String.valueOf(com_google_android_gms_internal_measurement_zzjz.zzark));
                                                                    com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr4;
                                                                    map15 = map3;
                                                                    it = it3;
                                                                    it2 = it4;
                                                                    map16 = map6;
                                                                    arrayMap3 = map5;
                                                                    j4 = j5;
                                                                    str9 = str2;
                                                                    map14 = map4;
                                                                }
                                                                map4 = map14;
                                                                str2 = str9;
                                                                hashSet = hashSet2;
                                                                map13 = arrayMap3;
                                                                map11 = map16;
                                                                map14 = map17;
                                                                map12 = map4;
                                                            } else {
                                                                zzgg().zzir().zzg("Skipping failed audience ID", Integer.valueOf(i));
                                                                hashSet = hashSet2;
                                                            }
                                                        }
                                                        arrayMap2 = map14;
                                                        str2 = str9;
                                                        map6 = map11;
                                                        map4 = map12;
                                                        hashSet4 = hashSet;
                                                        map5 = map13;
                                                        l3 = l2;
                                                        j6 = j;
                                                        i8 = i5 + 1;
                                                        com_google_android_gms_internal_measurement_zzkiArr2 = com_google_android_gms_internal_measurement_zzkiArr;
                                                        str7 = str2;
                                                        length = i4;
                                                        arrayMap6 = arrayMap2;
                                                        map2 = map4;
                                                        map = map6;
                                                        arrayMap3 = map5;
                                                        com_google_android_gms_internal_measurement_zzknArr2 = com_google_android_gms_internal_measurement_zzknArr;
                                                    }
                                                    try {
                                                        strArr[0] = str7;
                                                        writableDatabase.execSQL(str2, strArr);
                                                    } catch (SQLiteException e3) {
                                                        e = e3;
                                                        zzga.zzgg().zzil().zzg("Error clearing complex main event", e);
                                                        i4 = length;
                                                        i9 = i2;
                                                        i5 = i11;
                                                        com_google_android_gms_internal_measurement_zzkjArr = com_google_android_gms_internal_measurement_zzkjArr6;
                                                        arrayMap = arrayMap7;
                                                        com_google_android_gms_internal_measurement_zzkjArr7 = new zzkj[(com_google_android_gms_internal_measurement_zzki4.zzasv.length + com_google_android_gms_internal_measurement_zzkjArr.length)];
                                                        com_google_android_gms_internal_measurement_zzkjArr2 = com_google_android_gms_internal_measurement_zzki4.zzasv;
                                                        intValue = com_google_android_gms_internal_measurement_zzkjArr2.length;
                                                        i6 = i3;
                                                        length = i6;
                                                        while (i6 < intValue) {
                                                            com_google_android_gms_internal_measurement_zzkj2 = com_google_android_gms_internal_measurement_zzkjArr2[i6];
                                                            zzgc();
                                                            if (zzjv.zza(com_google_android_gms_internal_measurement_zzki2, com_google_android_gms_internal_measurement_zzkj2.name) != null) {
                                                                i9 = length + 1;
                                                                com_google_android_gms_internal_measurement_zzkjArr7[length] = com_google_android_gms_internal_measurement_zzkj2;
                                                                length = i9;
                                                            }
                                                            i6++;
                                                        }
                                                        if (length > 0) {
                                                            length2 = com_google_android_gms_internal_measurement_zzkjArr.length;
                                                            intValue = i3;
                                                            while (intValue < length2) {
                                                                i2 = length + 1;
                                                                com_google_android_gms_internal_measurement_zzkjArr7[length] = com_google_android_gms_internal_measurement_zzkjArr[intValue];
                                                                intValue++;
                                                                length = i2;
                                                            }
                                                            if (length != com_google_android_gms_internal_measurement_zzkjArr7.length) {
                                                                com_google_android_gms_internal_measurement_zzkjArr7 = (zzkj[]) Arrays.copyOf(com_google_android_gms_internal_measurement_zzkjArr7, length);
                                                            }
                                                            com_google_android_gms_internal_measurement_zzkjArr = com_google_android_gms_internal_measurement_zzkjArr7;
                                                        } else {
                                                            zzgg().zzin().zzg("No unique parameters in main event. eventName", str10);
                                                        }
                                                        str8 = str10;
                                                        l2 = l;
                                                        j2 = 0;
                                                        com_google_android_gms_internal_measurement_zzki = com_google_android_gms_internal_measurement_zzki4;
                                                        zze = zzga().zze(str7, com_google_android_gms_internal_measurement_zzki2.name);
                                                        if (zze != null) {
                                                            hashSet = hashSet4;
                                                            map13 = arrayMap3;
                                                            com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr;
                                                            str3 = str8;
                                                            map11 = map;
                                                            map12 = map2;
                                                            j3 = j2;
                                                            zze = zze.zzie();
                                                        } else {
                                                            zzgg().zzin().zze("Event aggregate wasn't created during raw event logging. appId, event", zzfg.zzbh(str), zzgb().zzbe(str8));
                                                            map11 = map;
                                                            map12 = map2;
                                                            hashSet = hashSet4;
                                                            map13 = arrayMap3;
                                                            com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr;
                                                            j3 = j2;
                                                            str3 = str8;
                                                            zze = new zzeq(str7, com_google_android_gms_internal_measurement_zzki2.name, 1, 1, com_google_android_gms_internal_measurement_zzki2.zzasw.longValue(), 0, null, null, null);
                                                        }
                                                        zzga().zza(zze);
                                                        j4 = zze.zzafp;
                                                        map14 = arrayMap;
                                                        str10 = str3;
                                                        map15 = (Map) map14.get(str10);
                                                        if (map15 != null) {
                                                            str9 = str;
                                                        } else {
                                                            str9 = str;
                                                            map15 = zzga().zzj(str9, str10);
                                                            if (map15 == null) {
                                                                map15 = new ArrayMap();
                                                            }
                                                            map14.put(str10, map15);
                                                        }
                                                        it = map15.keySet().iterator();
                                                        while (it.hasNext()) {
                                                            i = ((Integer) it.next()).intValue();
                                                            hashSet2 = hashSet;
                                                            if (hashSet2.contains(Integer.valueOf(i))) {
                                                                arrayMap3 = map13;
                                                                map16 = map11;
                                                                bitSet = (BitSet) map16.get(Integer.valueOf(i));
                                                                map17 = map14;
                                                                map14 = map12;
                                                                bitSet2 = (BitSet) map14.get(Integer.valueOf(i));
                                                                if (((zzkh) arrayMap3.get(Integer.valueOf(i))) == null) {
                                                                    com_google_android_gms_internal_measurement_zzkh = new zzkh();
                                                                    arrayMap3.put(Integer.valueOf(i), com_google_android_gms_internal_measurement_zzkh);
                                                                    com_google_android_gms_internal_measurement_zzkh.zzast = Boolean.valueOf(true);
                                                                    bitSet3 = new BitSet();
                                                                    map16.put(Integer.valueOf(i), bitSet3);
                                                                    bitSet2 = new BitSet();
                                                                    map14.put(Integer.valueOf(i), bitSet2);
                                                                    bitSet = bitSet3;
                                                                }
                                                                it2 = ((List) map15.get(Integer.valueOf(i))).iterator();
                                                                while (it2.hasNext()) {
                                                                    map3 = map15;
                                                                    com_google_android_gms_internal_measurement_zzjz = (zzjz) it2.next();
                                                                    it3 = it;
                                                                    it4 = it2;
                                                                    if (zzgg().isLoggable(2)) {
                                                                        map4 = map14;
                                                                        map5 = arrayMap3;
                                                                        map6 = map16;
                                                                    } else {
                                                                        map4 = map14;
                                                                        map6 = map16;
                                                                        map5 = arrayMap3;
                                                                        zzgg().zzir().zzd("Evaluating filter. audience, filter, event", Integer.valueOf(i), com_google_android_gms_internal_measurement_zzjz.zzark, zzgb().zzbe(com_google_android_gms_internal_measurement_zzjz.zzarl));
                                                                        zzgg().zzir().zzg("Filter definition", zzgb().zza(com_google_android_gms_internal_measurement_zzjz));
                                                                    }
                                                                    if (com_google_android_gms_internal_measurement_zzjz.zzark != null) {
                                                                        if (com_google_android_gms_internal_measurement_zzjz.zzark.intValue() > 256) {
                                                                            if (bitSet.get(com_google_android_gms_internal_measurement_zzjz.zzark.intValue())) {
                                                                                if (com_google_android_gms_internal_measurement_zzjz.zzaro != null) {
                                                                                    zza = zza(j4, com_google_android_gms_internal_measurement_zzjz.zzaro);
                                                                                    if (zza != null) {
                                                                                        if (zza.booleanValue()) {
                                                                                            j5 = j4;
                                                                                            valueOf = Boolean.valueOf(false);
                                                                                            com_google_android_gms_internal_measurement_zzkjArr4 = com_google_android_gms_internal_measurement_zzkjArr3;
                                                                                            if (valueOf != null) {
                                                                                            }
                                                                                            zzgg().zzir().zzg("Event filter result", valueOf != null ? "null" : valueOf);
                                                                                            if (valueOf != null) {
                                                                                                bitSet2.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                                                                if (valueOf.booleanValue()) {
                                                                                                    bitSet.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                                                                }
                                                                                            } else {
                                                                                                hashSet2.add(Integer.valueOf(i));
                                                                                            }
                                                                                            com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr4;
                                                                                            map15 = map3;
                                                                                            it = it3;
                                                                                            it2 = it4;
                                                                                            map14 = map4;
                                                                                            map16 = map6;
                                                                                            arrayMap3 = map5;
                                                                                            j4 = j5;
                                                                                        }
                                                                                    }
                                                                                    j5 = j4;
                                                                                    com_google_android_gms_internal_measurement_zzkjArr4 = com_google_android_gms_internal_measurement_zzkjArr3;
                                                                                    valueOf = null;
                                                                                    if (valueOf != null) {
                                                                                    }
                                                                                    zzgg().zzir().zzg("Event filter result", valueOf != null ? "null" : valueOf);
                                                                                    if (valueOf != null) {
                                                                                        hashSet2.add(Integer.valueOf(i));
                                                                                    } else {
                                                                                        bitSet2.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                                                        if (valueOf.booleanValue()) {
                                                                                            bitSet.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                                                        }
                                                                                    }
                                                                                    com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr4;
                                                                                    map15 = map3;
                                                                                    it = it3;
                                                                                    it2 = it4;
                                                                                    map14 = map4;
                                                                                    map16 = map6;
                                                                                    arrayMap3 = map5;
                                                                                    j4 = j5;
                                                                                }
                                                                                hashSet3 = new HashSet();
                                                                                for (i9 = 0; i9 < i10; i9++) {
                                                                                    if (TextUtils.isEmpty(com_google_android_gms_internal_measurement_zzka2.zzart)) {
                                                                                        zzgg().zzin().zzg("null or empty param name in filter. event", zzgb().zzbe(str10));
                                                                                        j5 = j4;
                                                                                        com_google_android_gms_internal_measurement_zzkjArr4 = com_google_android_gms_internal_measurement_zzkjArr3;
                                                                                        break;
                                                                                    }
                                                                                    hashSet3.add(com_google_android_gms_internal_measurement_zzka2.zzart);
                                                                                }
                                                                                map14 = new ArrayMap();
                                                                                com_google_android_gms_internal_measurement_zzkjArr4 = com_google_android_gms_internal_measurement_zzkjArr3;
                                                                                length3 = com_google_android_gms_internal_measurement_zzkjArr4.length;
                                                                                i10 = 0;
                                                                                while (i10 < length3) {
                                                                                    com_google_android_gms_internal_measurement_zzkj = com_google_android_gms_internal_measurement_zzkjArr4[i10];
                                                                                    j5 = j4;
                                                                                    if (!hashSet3.contains(com_google_android_gms_internal_measurement_zzkj.name)) {
                                                                                        if (com_google_android_gms_internal_measurement_zzkj.zzasz == null) {
                                                                                            obj2 = com_google_android_gms_internal_measurement_zzkj.name;
                                                                                            obj = com_google_android_gms_internal_measurement_zzkj.zzasz;
                                                                                        } else if (com_google_android_gms_internal_measurement_zzkj.zzaqx == null) {
                                                                                            obj2 = com_google_android_gms_internal_measurement_zzkj.name;
                                                                                            obj = com_google_android_gms_internal_measurement_zzkj.zzaqx;
                                                                                        } else if (com_google_android_gms_internal_measurement_zzkj.zzajf != null) {
                                                                                            zzin = zzgg().zzin();
                                                                                            str4 = "Unknown value for param. event, param";
                                                                                            zzbe = zzgb().zzbe(str10);
                                                                                            zzbf = zzgb().zzbf(com_google_android_gms_internal_measurement_zzkj.name);
                                                                                            break;
                                                                                        } else {
                                                                                            obj2 = com_google_android_gms_internal_measurement_zzkj.name;
                                                                                            obj = com_google_android_gms_internal_measurement_zzkj.zzajf;
                                                                                        }
                                                                                        map14.put(obj2, obj);
                                                                                    }
                                                                                    i10++;
                                                                                    j4 = j5;
                                                                                }
                                                                                j5 = j4;
                                                                                com_google_android_gms_internal_measurement_zzkaArr = com_google_android_gms_internal_measurement_zzjz.zzarm;
                                                                                length2 = com_google_android_gms_internal_measurement_zzkaArr.length;
                                                                                i2 = 0;
                                                                                while (i2 < length2) {
                                                                                    com_google_android_gms_internal_measurement_zzka = com_google_android_gms_internal_measurement_zzkaArr[i2];
                                                                                    equals = Boolean.TRUE.equals(com_google_android_gms_internal_measurement_zzka.zzars);
                                                                                    str8 = com_google_android_gms_internal_measurement_zzka.zzart;
                                                                                    if (TextUtils.isEmpty(str8)) {
                                                                                        com_google_android_gms_internal_measurement_zzkaArr2 = com_google_android_gms_internal_measurement_zzkaArr;
                                                                                        obj2 = map14.get(str8);
                                                                                        i7 = length2;
                                                                                        if (obj2 instanceof Long) {
                                                                                            if (obj2 instanceof Double) {
                                                                                                if (!(obj2 instanceof String)) {
                                                                                                    if (obj2 == null) {
                                                                                                        zzgg().zzir().zze("Missing param for filter. event, param", zzgb().zzbe(str10), zzgb().zzbf(str8));
                                                                                                        z = false;
                                                                                                        break;
                                                                                                    }
                                                                                                    zzin = zzgg().zzin();
                                                                                                    str4 = "Unknown param type. event, param";
                                                                                                } else {
                                                                                                    if (com_google_android_gms_internal_measurement_zzka.zzarq != null) {
                                                                                                        zza2 = zza((String) obj2, com_google_android_gms_internal_measurement_zzka.zzarq);
                                                                                                    } else if (com_google_android_gms_internal_measurement_zzka.zzarr == null) {
                                                                                                        zzin = zzgg().zzin();
                                                                                                        str4 = "No filter for String param. event, param";
                                                                                                    } else {
                                                                                                        str5 = (String) obj2;
                                                                                                        if (zzjv.zzcd(str5)) {
                                                                                                            zzin = zzgg().zzin();
                                                                                                            str4 = "Invalid param value for number filter. event, param";
                                                                                                        } else {
                                                                                                            zza2 = zza(str5, com_google_android_gms_internal_measurement_zzka.zzarr);
                                                                                                        }
                                                                                                    }
                                                                                                    if (zza2 == null) {
                                                                                                        if (((zza2.booleanValue() ^ 1) ^ equals) != 0) {
                                                                                                            z = false;
                                                                                                            break;
                                                                                                        }
                                                                                                        i2++;
                                                                                                        com_google_android_gms_internal_measurement_zzkaArr = com_google_android_gms_internal_measurement_zzkaArr2;
                                                                                                        length2 = i7;
                                                                                                    }
                                                                                                }
                                                                                            } else if (com_google_android_gms_internal_measurement_zzka.zzarr != null) {
                                                                                                zza2 = zza(((Double) obj2).doubleValue(), com_google_android_gms_internal_measurement_zzka.zzarr);
                                                                                                if (zza2 == null) {
                                                                                                    if (((zza2.booleanValue() ^ 1) ^ equals) != 0) {
                                                                                                        z = false;
                                                                                                        break;
                                                                                                    }
                                                                                                    i2++;
                                                                                                    com_google_android_gms_internal_measurement_zzkaArr = com_google_android_gms_internal_measurement_zzkaArr2;
                                                                                                    length2 = i7;
                                                                                                }
                                                                                            } else {
                                                                                                zzin = zzgg().zzin();
                                                                                                str4 = "No number filter for double param. event, param";
                                                                                            }
                                                                                        } else if (com_google_android_gms_internal_measurement_zzka.zzarr != null) {
                                                                                            zza2 = zza(((Long) obj2).longValue(), com_google_android_gms_internal_measurement_zzka.zzarr);
                                                                                            if (zza2 == null) {
                                                                                                if (((zza2.booleanValue() ^ 1) ^ equals) != 0) {
                                                                                                    z = false;
                                                                                                    break;
                                                                                                }
                                                                                                i2++;
                                                                                                com_google_android_gms_internal_measurement_zzkaArr = com_google_android_gms_internal_measurement_zzkaArr2;
                                                                                                length2 = i7;
                                                                                            }
                                                                                        } else {
                                                                                            zzin = zzgg().zzin();
                                                                                            str4 = "No number filter for long param. event, param";
                                                                                        }
                                                                                        zzbe = zzgb().zzbe(str10);
                                                                                        zzbf = zzgb().zzbf(str8);
                                                                                        zzin.zze(str4, zzbe, zzbf);
                                                                                    } else {
                                                                                        zzgg().zzin().zzg("Event has empty param name. event", zzgb().zzbe(str10));
                                                                                    }
                                                                                    valueOf = null;
                                                                                }
                                                                                z = true;
                                                                                valueOf = Boolean.valueOf(z);
                                                                                if (valueOf != null) {
                                                                                }
                                                                                zzgg().zzir().zzg("Event filter result", valueOf != null ? "null" : valueOf);
                                                                                if (valueOf != null) {
                                                                                    bitSet2.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                                                    if (valueOf.booleanValue()) {
                                                                                        bitSet.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                                                    }
                                                                                } else {
                                                                                    hashSet2.add(Integer.valueOf(i));
                                                                                }
                                                                                com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr4;
                                                                                map15 = map3;
                                                                                it = it3;
                                                                                it2 = it4;
                                                                                map14 = map4;
                                                                                map16 = map6;
                                                                                arrayMap3 = map5;
                                                                                j4 = j5;
                                                                            } else {
                                                                                zzgg().zzir().zze("Event filter already evaluated true. audience ID, filter ID", Integer.valueOf(i), com_google_android_gms_internal_measurement_zzjz.zzark);
                                                                                map15 = map3;
                                                                                it = it3;
                                                                                it2 = it4;
                                                                                map14 = map4;
                                                                                map16 = map6;
                                                                                arrayMap3 = map5;
                                                                            }
                                                                            str9 = str;
                                                                        }
                                                                    }
                                                                    j5 = j4;
                                                                    com_google_android_gms_internal_measurement_zzkjArr4 = com_google_android_gms_internal_measurement_zzkjArr3;
                                                                    str2 = str;
                                                                    zzgg().zzin().zze("Invalid event filter ID. appId, id", zzfg.zzbh(str), String.valueOf(com_google_android_gms_internal_measurement_zzjz.zzark));
                                                                    com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr4;
                                                                    map15 = map3;
                                                                    it = it3;
                                                                    it2 = it4;
                                                                    map16 = map6;
                                                                    arrayMap3 = map5;
                                                                    j4 = j5;
                                                                    str9 = str2;
                                                                    map14 = map4;
                                                                }
                                                                map4 = map14;
                                                                str2 = str9;
                                                                hashSet = hashSet2;
                                                                map13 = arrayMap3;
                                                                map11 = map16;
                                                                map14 = map17;
                                                                map12 = map4;
                                                            } else {
                                                                zzgg().zzir().zzg("Skipping failed audience ID", Integer.valueOf(i));
                                                                hashSet = hashSet2;
                                                            }
                                                        }
                                                        arrayMap2 = map14;
                                                        str2 = str9;
                                                        map6 = map11;
                                                        map4 = map12;
                                                        hashSet4 = hashSet;
                                                        map5 = map13;
                                                        l3 = l2;
                                                        j6 = j;
                                                        i8 = i5 + 1;
                                                        com_google_android_gms_internal_measurement_zzkiArr2 = com_google_android_gms_internal_measurement_zzkiArr;
                                                        str7 = str2;
                                                        length = i4;
                                                        arrayMap6 = arrayMap2;
                                                        map2 = map4;
                                                        map = map6;
                                                        arrayMap3 = map5;
                                                        com_google_android_gms_internal_measurement_zzknArr2 = com_google_android_gms_internal_measurement_zzknArr;
                                                    }
                                                } catch (SQLiteException e4) {
                                                    e = e4;
                                                    arrayMap7 = arrayMap6;
                                                    com_google_android_gms_internal_measurement_zzkjArr6 = com_google_android_gms_internal_measurement_zzkjArr5;
                                                    i2 = 1;
                                                    i3 = 0;
                                                    zzga.zzgg().zzil().zzg("Error clearing complex main event", e);
                                                    i4 = length;
                                                    i9 = i2;
                                                    i5 = i11;
                                                    com_google_android_gms_internal_measurement_zzkjArr = com_google_android_gms_internal_measurement_zzkjArr6;
                                                    arrayMap = arrayMap7;
                                                    com_google_android_gms_internal_measurement_zzkjArr7 = new zzkj[(com_google_android_gms_internal_measurement_zzki4.zzasv.length + com_google_android_gms_internal_measurement_zzkjArr.length)];
                                                    com_google_android_gms_internal_measurement_zzkjArr2 = com_google_android_gms_internal_measurement_zzki4.zzasv;
                                                    intValue = com_google_android_gms_internal_measurement_zzkjArr2.length;
                                                    i6 = i3;
                                                    length = i6;
                                                    while (i6 < intValue) {
                                                        com_google_android_gms_internal_measurement_zzkj2 = com_google_android_gms_internal_measurement_zzkjArr2[i6];
                                                        zzgc();
                                                        if (zzjv.zza(com_google_android_gms_internal_measurement_zzki2, com_google_android_gms_internal_measurement_zzkj2.name) != null) {
                                                            i9 = length + 1;
                                                            com_google_android_gms_internal_measurement_zzkjArr7[length] = com_google_android_gms_internal_measurement_zzkj2;
                                                            length = i9;
                                                        }
                                                        i6++;
                                                    }
                                                    if (length > 0) {
                                                        length2 = com_google_android_gms_internal_measurement_zzkjArr.length;
                                                        intValue = i3;
                                                        while (intValue < length2) {
                                                            i2 = length + 1;
                                                            com_google_android_gms_internal_measurement_zzkjArr7[length] = com_google_android_gms_internal_measurement_zzkjArr[intValue];
                                                            intValue++;
                                                            length = i2;
                                                        }
                                                        if (length != com_google_android_gms_internal_measurement_zzkjArr7.length) {
                                                            com_google_android_gms_internal_measurement_zzkjArr7 = (zzkj[]) Arrays.copyOf(com_google_android_gms_internal_measurement_zzkjArr7, length);
                                                        }
                                                        com_google_android_gms_internal_measurement_zzkjArr = com_google_android_gms_internal_measurement_zzkjArr7;
                                                    } else {
                                                        zzgg().zzin().zzg("No unique parameters in main event. eventName", str10);
                                                    }
                                                    str8 = str10;
                                                    l2 = l;
                                                    j2 = 0;
                                                    com_google_android_gms_internal_measurement_zzki = com_google_android_gms_internal_measurement_zzki4;
                                                    zze = zzga().zze(str7, com_google_android_gms_internal_measurement_zzki2.name);
                                                    if (zze != null) {
                                                        hashSet = hashSet4;
                                                        map13 = arrayMap3;
                                                        com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr;
                                                        str3 = str8;
                                                        map11 = map;
                                                        map12 = map2;
                                                        j3 = j2;
                                                        zze = zze.zzie();
                                                    } else {
                                                        zzgg().zzin().zze("Event aggregate wasn't created during raw event logging. appId, event", zzfg.zzbh(str), zzgb().zzbe(str8));
                                                        map11 = map;
                                                        map12 = map2;
                                                        hashSet = hashSet4;
                                                        map13 = arrayMap3;
                                                        com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr;
                                                        j3 = j2;
                                                        str3 = str8;
                                                        zze = new zzeq(str7, com_google_android_gms_internal_measurement_zzki2.name, 1, 1, com_google_android_gms_internal_measurement_zzki2.zzasw.longValue(), 0, null, null, null);
                                                    }
                                                    zzga().zza(zze);
                                                    j4 = zze.zzafp;
                                                    map14 = arrayMap;
                                                    str10 = str3;
                                                    map15 = (Map) map14.get(str10);
                                                    if (map15 != null) {
                                                        str9 = str;
                                                    } else {
                                                        str9 = str;
                                                        map15 = zzga().zzj(str9, str10);
                                                        if (map15 == null) {
                                                            map15 = new ArrayMap();
                                                        }
                                                        map14.put(str10, map15);
                                                    }
                                                    it = map15.keySet().iterator();
                                                    while (it.hasNext()) {
                                                        i = ((Integer) it.next()).intValue();
                                                        hashSet2 = hashSet;
                                                        if (hashSet2.contains(Integer.valueOf(i))) {
                                                            arrayMap3 = map13;
                                                            map16 = map11;
                                                            bitSet = (BitSet) map16.get(Integer.valueOf(i));
                                                            map17 = map14;
                                                            map14 = map12;
                                                            bitSet2 = (BitSet) map14.get(Integer.valueOf(i));
                                                            if (((zzkh) arrayMap3.get(Integer.valueOf(i))) == null) {
                                                                com_google_android_gms_internal_measurement_zzkh = new zzkh();
                                                                arrayMap3.put(Integer.valueOf(i), com_google_android_gms_internal_measurement_zzkh);
                                                                com_google_android_gms_internal_measurement_zzkh.zzast = Boolean.valueOf(true);
                                                                bitSet3 = new BitSet();
                                                                map16.put(Integer.valueOf(i), bitSet3);
                                                                bitSet2 = new BitSet();
                                                                map14.put(Integer.valueOf(i), bitSet2);
                                                                bitSet = bitSet3;
                                                            }
                                                            it2 = ((List) map15.get(Integer.valueOf(i))).iterator();
                                                            while (it2.hasNext()) {
                                                                map3 = map15;
                                                                com_google_android_gms_internal_measurement_zzjz = (zzjz) it2.next();
                                                                it3 = it;
                                                                it4 = it2;
                                                                if (zzgg().isLoggable(2)) {
                                                                    map4 = map14;
                                                                    map5 = arrayMap3;
                                                                    map6 = map16;
                                                                } else {
                                                                    map4 = map14;
                                                                    map6 = map16;
                                                                    map5 = arrayMap3;
                                                                    zzgg().zzir().zzd("Evaluating filter. audience, filter, event", Integer.valueOf(i), com_google_android_gms_internal_measurement_zzjz.zzark, zzgb().zzbe(com_google_android_gms_internal_measurement_zzjz.zzarl));
                                                                    zzgg().zzir().zzg("Filter definition", zzgb().zza(com_google_android_gms_internal_measurement_zzjz));
                                                                }
                                                                if (com_google_android_gms_internal_measurement_zzjz.zzark != null) {
                                                                    if (com_google_android_gms_internal_measurement_zzjz.zzark.intValue() > 256) {
                                                                        if (bitSet.get(com_google_android_gms_internal_measurement_zzjz.zzark.intValue())) {
                                                                            if (com_google_android_gms_internal_measurement_zzjz.zzaro != null) {
                                                                                zza = zza(j4, com_google_android_gms_internal_measurement_zzjz.zzaro);
                                                                                if (zza != null) {
                                                                                    if (zza.booleanValue()) {
                                                                                        j5 = j4;
                                                                                        valueOf = Boolean.valueOf(false);
                                                                                        com_google_android_gms_internal_measurement_zzkjArr4 = com_google_android_gms_internal_measurement_zzkjArr3;
                                                                                        if (valueOf != null) {
                                                                                        }
                                                                                        zzgg().zzir().zzg("Event filter result", valueOf != null ? "null" : valueOf);
                                                                                        if (valueOf != null) {
                                                                                            bitSet2.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                                                            if (valueOf.booleanValue()) {
                                                                                                bitSet.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                                                            }
                                                                                        } else {
                                                                                            hashSet2.add(Integer.valueOf(i));
                                                                                        }
                                                                                        com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr4;
                                                                                        map15 = map3;
                                                                                        it = it3;
                                                                                        it2 = it4;
                                                                                        map14 = map4;
                                                                                        map16 = map6;
                                                                                        arrayMap3 = map5;
                                                                                        j4 = j5;
                                                                                    }
                                                                                }
                                                                                j5 = j4;
                                                                                com_google_android_gms_internal_measurement_zzkjArr4 = com_google_android_gms_internal_measurement_zzkjArr3;
                                                                                valueOf = null;
                                                                                if (valueOf != null) {
                                                                                }
                                                                                zzgg().zzir().zzg("Event filter result", valueOf != null ? "null" : valueOf);
                                                                                if (valueOf != null) {
                                                                                    hashSet2.add(Integer.valueOf(i));
                                                                                } else {
                                                                                    bitSet2.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                                                    if (valueOf.booleanValue()) {
                                                                                        bitSet.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                                                    }
                                                                                }
                                                                                com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr4;
                                                                                map15 = map3;
                                                                                it = it3;
                                                                                it2 = it4;
                                                                                map14 = map4;
                                                                                map16 = map6;
                                                                                arrayMap3 = map5;
                                                                                j4 = j5;
                                                                            }
                                                                            hashSet3 = new HashSet();
                                                                            for (i9 = 0; i9 < i10; i9++) {
                                                                                if (TextUtils.isEmpty(com_google_android_gms_internal_measurement_zzka2.zzart)) {
                                                                                    zzgg().zzin().zzg("null or empty param name in filter. event", zzgb().zzbe(str10));
                                                                                    j5 = j4;
                                                                                    com_google_android_gms_internal_measurement_zzkjArr4 = com_google_android_gms_internal_measurement_zzkjArr3;
                                                                                    break;
                                                                                }
                                                                                hashSet3.add(com_google_android_gms_internal_measurement_zzka2.zzart);
                                                                            }
                                                                            map14 = new ArrayMap();
                                                                            com_google_android_gms_internal_measurement_zzkjArr4 = com_google_android_gms_internal_measurement_zzkjArr3;
                                                                            length3 = com_google_android_gms_internal_measurement_zzkjArr4.length;
                                                                            i10 = 0;
                                                                            while (i10 < length3) {
                                                                                com_google_android_gms_internal_measurement_zzkj = com_google_android_gms_internal_measurement_zzkjArr4[i10];
                                                                                j5 = j4;
                                                                                if (!hashSet3.contains(com_google_android_gms_internal_measurement_zzkj.name)) {
                                                                                    if (com_google_android_gms_internal_measurement_zzkj.zzasz == null) {
                                                                                        obj2 = com_google_android_gms_internal_measurement_zzkj.name;
                                                                                        obj = com_google_android_gms_internal_measurement_zzkj.zzasz;
                                                                                    } else if (com_google_android_gms_internal_measurement_zzkj.zzaqx == null) {
                                                                                        obj2 = com_google_android_gms_internal_measurement_zzkj.name;
                                                                                        obj = com_google_android_gms_internal_measurement_zzkj.zzaqx;
                                                                                    } else if (com_google_android_gms_internal_measurement_zzkj.zzajf != null) {
                                                                                        zzin = zzgg().zzin();
                                                                                        str4 = "Unknown value for param. event, param";
                                                                                        zzbe = zzgb().zzbe(str10);
                                                                                        zzbf = zzgb().zzbf(com_google_android_gms_internal_measurement_zzkj.name);
                                                                                        break;
                                                                                    } else {
                                                                                        obj2 = com_google_android_gms_internal_measurement_zzkj.name;
                                                                                        obj = com_google_android_gms_internal_measurement_zzkj.zzajf;
                                                                                    }
                                                                                    map14.put(obj2, obj);
                                                                                }
                                                                                i10++;
                                                                                j4 = j5;
                                                                            }
                                                                            j5 = j4;
                                                                            com_google_android_gms_internal_measurement_zzkaArr = com_google_android_gms_internal_measurement_zzjz.zzarm;
                                                                            length2 = com_google_android_gms_internal_measurement_zzkaArr.length;
                                                                            i2 = 0;
                                                                            while (i2 < length2) {
                                                                                com_google_android_gms_internal_measurement_zzka = com_google_android_gms_internal_measurement_zzkaArr[i2];
                                                                                equals = Boolean.TRUE.equals(com_google_android_gms_internal_measurement_zzka.zzars);
                                                                                str8 = com_google_android_gms_internal_measurement_zzka.zzart;
                                                                                if (TextUtils.isEmpty(str8)) {
                                                                                    com_google_android_gms_internal_measurement_zzkaArr2 = com_google_android_gms_internal_measurement_zzkaArr;
                                                                                    obj2 = map14.get(str8);
                                                                                    i7 = length2;
                                                                                    if (obj2 instanceof Long) {
                                                                                        if (obj2 instanceof Double) {
                                                                                            if (!(obj2 instanceof String)) {
                                                                                                if (obj2 == null) {
                                                                                                    zzgg().zzir().zze("Missing param for filter. event, param", zzgb().zzbe(str10), zzgb().zzbf(str8));
                                                                                                    z = false;
                                                                                                    break;
                                                                                                }
                                                                                                zzin = zzgg().zzin();
                                                                                                str4 = "Unknown param type. event, param";
                                                                                            } else {
                                                                                                if (com_google_android_gms_internal_measurement_zzka.zzarq != null) {
                                                                                                    zza2 = zza((String) obj2, com_google_android_gms_internal_measurement_zzka.zzarq);
                                                                                                } else if (com_google_android_gms_internal_measurement_zzka.zzarr == null) {
                                                                                                    zzin = zzgg().zzin();
                                                                                                    str4 = "No filter for String param. event, param";
                                                                                                } else {
                                                                                                    str5 = (String) obj2;
                                                                                                    if (zzjv.zzcd(str5)) {
                                                                                                        zzin = zzgg().zzin();
                                                                                                        str4 = "Invalid param value for number filter. event, param";
                                                                                                    } else {
                                                                                                        zza2 = zza(str5, com_google_android_gms_internal_measurement_zzka.zzarr);
                                                                                                    }
                                                                                                }
                                                                                                if (zza2 == null) {
                                                                                                    if (((zza2.booleanValue() ^ 1) ^ equals) != 0) {
                                                                                                        z = false;
                                                                                                        break;
                                                                                                    }
                                                                                                    i2++;
                                                                                                    com_google_android_gms_internal_measurement_zzkaArr = com_google_android_gms_internal_measurement_zzkaArr2;
                                                                                                    length2 = i7;
                                                                                                }
                                                                                            }
                                                                                        } else if (com_google_android_gms_internal_measurement_zzka.zzarr != null) {
                                                                                            zza2 = zza(((Double) obj2).doubleValue(), com_google_android_gms_internal_measurement_zzka.zzarr);
                                                                                            if (zza2 == null) {
                                                                                                if (((zza2.booleanValue() ^ 1) ^ equals) != 0) {
                                                                                                    z = false;
                                                                                                    break;
                                                                                                }
                                                                                                i2++;
                                                                                                com_google_android_gms_internal_measurement_zzkaArr = com_google_android_gms_internal_measurement_zzkaArr2;
                                                                                                length2 = i7;
                                                                                            }
                                                                                        } else {
                                                                                            zzin = zzgg().zzin();
                                                                                            str4 = "No number filter for double param. event, param";
                                                                                        }
                                                                                    } else if (com_google_android_gms_internal_measurement_zzka.zzarr != null) {
                                                                                        zza2 = zza(((Long) obj2).longValue(), com_google_android_gms_internal_measurement_zzka.zzarr);
                                                                                        if (zza2 == null) {
                                                                                            if (((zza2.booleanValue() ^ 1) ^ equals) != 0) {
                                                                                                z = false;
                                                                                                break;
                                                                                            }
                                                                                            i2++;
                                                                                            com_google_android_gms_internal_measurement_zzkaArr = com_google_android_gms_internal_measurement_zzkaArr2;
                                                                                            length2 = i7;
                                                                                        }
                                                                                    } else {
                                                                                        zzin = zzgg().zzin();
                                                                                        str4 = "No number filter for long param. event, param";
                                                                                    }
                                                                                    zzbe = zzgb().zzbe(str10);
                                                                                    zzbf = zzgb().zzbf(str8);
                                                                                    zzin.zze(str4, zzbe, zzbf);
                                                                                } else {
                                                                                    zzgg().zzin().zzg("Event has empty param name. event", zzgb().zzbe(str10));
                                                                                }
                                                                                valueOf = null;
                                                                            }
                                                                            z = true;
                                                                            valueOf = Boolean.valueOf(z);
                                                                            if (valueOf != null) {
                                                                            }
                                                                            zzgg().zzir().zzg("Event filter result", valueOf != null ? "null" : valueOf);
                                                                            if (valueOf != null) {
                                                                                bitSet2.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                                                if (valueOf.booleanValue()) {
                                                                                    bitSet.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                                                }
                                                                            } else {
                                                                                hashSet2.add(Integer.valueOf(i));
                                                                            }
                                                                            com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr4;
                                                                            map15 = map3;
                                                                            it = it3;
                                                                            it2 = it4;
                                                                            map14 = map4;
                                                                            map16 = map6;
                                                                            arrayMap3 = map5;
                                                                            j4 = j5;
                                                                        } else {
                                                                            zzgg().zzir().zze("Event filter already evaluated true. audience ID, filter ID", Integer.valueOf(i), com_google_android_gms_internal_measurement_zzjz.zzark);
                                                                            map15 = map3;
                                                                            it = it3;
                                                                            it2 = it4;
                                                                            map14 = map4;
                                                                            map16 = map6;
                                                                            arrayMap3 = map5;
                                                                        }
                                                                        str9 = str;
                                                                    }
                                                                }
                                                                j5 = j4;
                                                                com_google_android_gms_internal_measurement_zzkjArr4 = com_google_android_gms_internal_measurement_zzkjArr3;
                                                                str2 = str;
                                                                zzgg().zzin().zze("Invalid event filter ID. appId, id", zzfg.zzbh(str), String.valueOf(com_google_android_gms_internal_measurement_zzjz.zzark));
                                                                com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr4;
                                                                map15 = map3;
                                                                it = it3;
                                                                it2 = it4;
                                                                map16 = map6;
                                                                arrayMap3 = map5;
                                                                j4 = j5;
                                                                str9 = str2;
                                                                map14 = map4;
                                                            }
                                                            map4 = map14;
                                                            str2 = str9;
                                                            hashSet = hashSet2;
                                                            map13 = arrayMap3;
                                                            map11 = map16;
                                                            map14 = map17;
                                                            map12 = map4;
                                                        } else {
                                                            zzgg().zzir().zzg("Skipping failed audience ID", Integer.valueOf(i));
                                                            hashSet = hashSet2;
                                                        }
                                                    }
                                                    arrayMap2 = map14;
                                                    str2 = str9;
                                                    map6 = map11;
                                                    map4 = map12;
                                                    hashSet4 = hashSet;
                                                    map5 = map13;
                                                    l3 = l2;
                                                    j6 = j;
                                                    i8 = i5 + 1;
                                                    com_google_android_gms_internal_measurement_zzkiArr2 = com_google_android_gms_internal_measurement_zzkiArr;
                                                    str7 = str2;
                                                    length = i4;
                                                    arrayMap6 = arrayMap2;
                                                    map2 = map4;
                                                    map = map6;
                                                    arrayMap3 = map5;
                                                    com_google_android_gms_internal_measurement_zzknArr2 = com_google_android_gms_internal_measurement_zzknArr;
                                                }
                                                i4 = length;
                                                i9 = i2;
                                                i5 = i11;
                                                com_google_android_gms_internal_measurement_zzkjArr = com_google_android_gms_internal_measurement_zzkjArr6;
                                                arrayMap = arrayMap7;
                                            } else {
                                                i3 = 0;
                                                i5 = i11;
                                                j7 = 0;
                                                i4 = length;
                                                arrayMap = arrayMap6;
                                                i9 = 1;
                                                com_google_android_gms_internal_measurement_zzkjArr = com_google_android_gms_internal_measurement_zzkjArr5;
                                                zzga().zza(str7, l5, j, com_google_android_gms_internal_measurement_zzki4);
                                            }
                                            com_google_android_gms_internal_measurement_zzkjArr7 = new zzkj[(com_google_android_gms_internal_measurement_zzki4.zzasv.length + com_google_android_gms_internal_measurement_zzkjArr.length)];
                                            com_google_android_gms_internal_measurement_zzkjArr2 = com_google_android_gms_internal_measurement_zzki4.zzasv;
                                            intValue = com_google_android_gms_internal_measurement_zzkjArr2.length;
                                            i6 = i3;
                                            length = i6;
                                            while (i6 < intValue) {
                                                com_google_android_gms_internal_measurement_zzkj2 = com_google_android_gms_internal_measurement_zzkjArr2[i6];
                                                zzgc();
                                                if (zzjv.zza(com_google_android_gms_internal_measurement_zzki2, com_google_android_gms_internal_measurement_zzkj2.name) != null) {
                                                    i9 = length + 1;
                                                    com_google_android_gms_internal_measurement_zzkjArr7[length] = com_google_android_gms_internal_measurement_zzkj2;
                                                    length = i9;
                                                }
                                                i6++;
                                            }
                                            if (length > 0) {
                                                length2 = com_google_android_gms_internal_measurement_zzkjArr.length;
                                                intValue = i3;
                                                while (intValue < length2) {
                                                    i2 = length + 1;
                                                    com_google_android_gms_internal_measurement_zzkjArr7[length] = com_google_android_gms_internal_measurement_zzkjArr[intValue];
                                                    intValue++;
                                                    length = i2;
                                                }
                                                if (length != com_google_android_gms_internal_measurement_zzkjArr7.length) {
                                                    com_google_android_gms_internal_measurement_zzkjArr7 = (zzkj[]) Arrays.copyOf(com_google_android_gms_internal_measurement_zzkjArr7, length);
                                                }
                                                com_google_android_gms_internal_measurement_zzkjArr = com_google_android_gms_internal_measurement_zzkjArr7;
                                            } else {
                                                zzgg().zzin().zzg("No unique parameters in main event. eventName", str10);
                                            }
                                            str8 = str10;
                                            l2 = l;
                                            j2 = 0;
                                            com_google_android_gms_internal_measurement_zzki = com_google_android_gms_internal_measurement_zzki4;
                                        }
                                    }
                                    zza3 = zzga().zza(str7, l5);
                                    if (zza3 != null) {
                                        if (zza3.first == null) {
                                            com_google_android_gms_internal_measurement_zzki5 = (zzki) zza3.first;
                                            j6 = ((Long) zza3.second).longValue();
                                            zzgc();
                                            l = (Long) zzjv.zzb(com_google_android_gms_internal_measurement_zzki5, "_eid");
                                            com_google_android_gms_internal_measurement_zzki4 = com_google_android_gms_internal_measurement_zzki5;
                                            j = j6 - 1;
                                            if (j > 0) {
                                                i3 = 0;
                                                i5 = i11;
                                                j7 = 0;
                                                i4 = length;
                                                arrayMap = arrayMap6;
                                                i9 = 1;
                                                com_google_android_gms_internal_measurement_zzkjArr = com_google_android_gms_internal_measurement_zzkjArr5;
                                                zzga().zza(str7, l5, j, com_google_android_gms_internal_measurement_zzki4);
                                            } else {
                                                zzga = zzga();
                                                zzga.zzab();
                                                zzga.zzgg().zzir().zzg("Clearing complex main event info. appId", str7);
                                                writableDatabase = zzga.getWritableDatabase();
                                                str2 = "delete from main_event_params where app_id=?";
                                                arrayMap7 = arrayMap6;
                                                com_google_android_gms_internal_measurement_zzkjArr6 = com_google_android_gms_internal_measurement_zzkjArr5;
                                                i2 = 1;
                                                strArr = new String[1];
                                                i3 = 0;
                                                strArr[0] = str7;
                                                writableDatabase.execSQL(str2, strArr);
                                                i4 = length;
                                                i9 = i2;
                                                i5 = i11;
                                                com_google_android_gms_internal_measurement_zzkjArr = com_google_android_gms_internal_measurement_zzkjArr6;
                                                arrayMap = arrayMap7;
                                            }
                                            com_google_android_gms_internal_measurement_zzkjArr7 = new zzkj[(com_google_android_gms_internal_measurement_zzki4.zzasv.length + com_google_android_gms_internal_measurement_zzkjArr.length)];
                                            com_google_android_gms_internal_measurement_zzkjArr2 = com_google_android_gms_internal_measurement_zzki4.zzasv;
                                            intValue = com_google_android_gms_internal_measurement_zzkjArr2.length;
                                            i6 = i3;
                                            length = i6;
                                            while (i6 < intValue) {
                                                com_google_android_gms_internal_measurement_zzkj2 = com_google_android_gms_internal_measurement_zzkjArr2[i6];
                                                zzgc();
                                                if (zzjv.zza(com_google_android_gms_internal_measurement_zzki2, com_google_android_gms_internal_measurement_zzkj2.name) != null) {
                                                    i9 = length + 1;
                                                    com_google_android_gms_internal_measurement_zzkjArr7[length] = com_google_android_gms_internal_measurement_zzkj2;
                                                    length = i9;
                                                }
                                                i6++;
                                            }
                                            if (length > 0) {
                                                zzgg().zzin().zzg("No unique parameters in main event. eventName", str10);
                                            } else {
                                                length2 = com_google_android_gms_internal_measurement_zzkjArr.length;
                                                intValue = i3;
                                                while (intValue < length2) {
                                                    i2 = length + 1;
                                                    com_google_android_gms_internal_measurement_zzkjArr7[length] = com_google_android_gms_internal_measurement_zzkjArr[intValue];
                                                    intValue++;
                                                    length = i2;
                                                }
                                                if (length != com_google_android_gms_internal_measurement_zzkjArr7.length) {
                                                    com_google_android_gms_internal_measurement_zzkjArr7 = (zzkj[]) Arrays.copyOf(com_google_android_gms_internal_measurement_zzkjArr7, length);
                                                }
                                                com_google_android_gms_internal_measurement_zzkjArr = com_google_android_gms_internal_measurement_zzkjArr7;
                                            }
                                            str8 = str10;
                                            l2 = l;
                                            j2 = 0;
                                            com_google_android_gms_internal_measurement_zzki = com_google_android_gms_internal_measurement_zzki4;
                                        }
                                    }
                                    arrayMap = arrayMap6;
                                    i4 = length;
                                    i5 = i11;
                                    zzgg().zzil().zze("Extra parameter without existing main event. eventName, eventId", str10, l5);
                                } else {
                                    zzgg().zzil().zzg("Extra parameter without an event name. eventId", l5);
                                    arrayMap = arrayMap6;
                                    i4 = length;
                                    i5 = i11;
                                }
                                map5 = arrayMap3;
                                str2 = str7;
                                map6 = map;
                                map4 = map2;
                                arrayMap2 = arrayMap;
                                i8 = i5 + 1;
                                com_google_android_gms_internal_measurement_zzkiArr2 = com_google_android_gms_internal_measurement_zzkiArr;
                                str7 = str2;
                                length = i4;
                                arrayMap6 = arrayMap2;
                                map2 = map4;
                                map = map6;
                                arrayMap3 = map5;
                                com_google_android_gms_internal_measurement_zzknArr2 = com_google_android_gms_internal_measurement_zzknArr;
                            } else {
                                arrayMap = arrayMap6;
                                i4 = length;
                                com_google_android_gms_internal_measurement_zzkjArr = com_google_android_gms_internal_measurement_zzkjArr5;
                                i5 = i11;
                                if (obj != null) {
                                    zzgc();
                                    valueOf2 = Long.valueOf(0);
                                    obj2 = zzjv.zzb(com_google_android_gms_internal_measurement_zzki2, "_epc");
                                    if (obj2 == null) {
                                        obj2 = valueOf2;
                                    }
                                    j6 = ((Long) obj2).longValue();
                                    if (j6 > 0) {
                                        zzgg().zzin().zzg("Complex event with zero extra param count. eventName", str10);
                                        l4 = l5;
                                        j2 = 0;
                                    } else {
                                        l4 = l5;
                                        j2 = 0;
                                        zzga().zza(str7, l5, j6, com_google_android_gms_internal_measurement_zzki2);
                                    }
                                    com_google_android_gms_internal_measurement_zzki = com_google_android_gms_internal_measurement_zzki2;
                                    j = j6;
                                    l2 = l4;
                                    str8 = str10;
                                }
                            }
                            zze = zzga().zze(str7, com_google_android_gms_internal_measurement_zzki2.name);
                            if (zze != null) {
                                zzgg().zzin().zze("Event aggregate wasn't created during raw event logging. appId, event", zzfg.zzbh(str), zzgb().zzbe(str8));
                                map11 = map;
                                map12 = map2;
                                hashSet = hashSet4;
                                map13 = arrayMap3;
                                com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr;
                                j3 = j2;
                                str3 = str8;
                                zze = new zzeq(str7, com_google_android_gms_internal_measurement_zzki2.name, 1, 1, com_google_android_gms_internal_measurement_zzki2.zzasw.longValue(), 0, null, null, null);
                            } else {
                                hashSet = hashSet4;
                                map13 = arrayMap3;
                                com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr;
                                str3 = str8;
                                map11 = map;
                                map12 = map2;
                                j3 = j2;
                                zze = zze.zzie();
                            }
                            zzga().zza(zze);
                            j4 = zze.zzafp;
                            map14 = arrayMap;
                            str10 = str3;
                            map15 = (Map) map14.get(str10);
                            if (map15 != null) {
                                str9 = str;
                                map15 = zzga().zzj(str9, str10);
                                if (map15 == null) {
                                    map15 = new ArrayMap();
                                }
                                map14.put(str10, map15);
                            } else {
                                str9 = str;
                            }
                            it = map15.keySet().iterator();
                            while (it.hasNext()) {
                                i = ((Integer) it.next()).intValue();
                                hashSet2 = hashSet;
                                if (hashSet2.contains(Integer.valueOf(i))) {
                                    zzgg().zzir().zzg("Skipping failed audience ID", Integer.valueOf(i));
                                    hashSet = hashSet2;
                                } else {
                                    arrayMap3 = map13;
                                    map16 = map11;
                                    bitSet = (BitSet) map16.get(Integer.valueOf(i));
                                    map17 = map14;
                                    map14 = map12;
                                    bitSet2 = (BitSet) map14.get(Integer.valueOf(i));
                                    if (((zzkh) arrayMap3.get(Integer.valueOf(i))) == null) {
                                        com_google_android_gms_internal_measurement_zzkh = new zzkh();
                                        arrayMap3.put(Integer.valueOf(i), com_google_android_gms_internal_measurement_zzkh);
                                        com_google_android_gms_internal_measurement_zzkh.zzast = Boolean.valueOf(true);
                                        bitSet3 = new BitSet();
                                        map16.put(Integer.valueOf(i), bitSet3);
                                        bitSet2 = new BitSet();
                                        map14.put(Integer.valueOf(i), bitSet2);
                                        bitSet = bitSet3;
                                    }
                                    it2 = ((List) map15.get(Integer.valueOf(i))).iterator();
                                    while (it2.hasNext()) {
                                        map3 = map15;
                                        com_google_android_gms_internal_measurement_zzjz = (zzjz) it2.next();
                                        it3 = it;
                                        it4 = it2;
                                        if (zzgg().isLoggable(2)) {
                                            map4 = map14;
                                            map6 = map16;
                                            map5 = arrayMap3;
                                            zzgg().zzir().zzd("Evaluating filter. audience, filter, event", Integer.valueOf(i), com_google_android_gms_internal_measurement_zzjz.zzark, zzgb().zzbe(com_google_android_gms_internal_measurement_zzjz.zzarl));
                                            zzgg().zzir().zzg("Filter definition", zzgb().zza(com_google_android_gms_internal_measurement_zzjz));
                                        } else {
                                            map4 = map14;
                                            map5 = arrayMap3;
                                            map6 = map16;
                                        }
                                        if (com_google_android_gms_internal_measurement_zzjz.zzark != null) {
                                            if (com_google_android_gms_internal_measurement_zzjz.zzark.intValue() > 256) {
                                                if (bitSet.get(com_google_android_gms_internal_measurement_zzjz.zzark.intValue())) {
                                                    zzgg().zzir().zze("Event filter already evaluated true. audience ID, filter ID", Integer.valueOf(i), com_google_android_gms_internal_measurement_zzjz.zzark);
                                                    map15 = map3;
                                                    it = it3;
                                                    it2 = it4;
                                                    map14 = map4;
                                                    map16 = map6;
                                                    arrayMap3 = map5;
                                                } else {
                                                    if (com_google_android_gms_internal_measurement_zzjz.zzaro != null) {
                                                        zza = zza(j4, com_google_android_gms_internal_measurement_zzjz.zzaro);
                                                        if (zza != null) {
                                                            if (zza.booleanValue()) {
                                                                j5 = j4;
                                                                valueOf = Boolean.valueOf(false);
                                                                com_google_android_gms_internal_measurement_zzkjArr4 = com_google_android_gms_internal_measurement_zzkjArr3;
                                                                if (valueOf != null) {
                                                                }
                                                                zzgg().zzir().zzg("Event filter result", valueOf != null ? "null" : valueOf);
                                                                if (valueOf != null) {
                                                                    hashSet2.add(Integer.valueOf(i));
                                                                } else {
                                                                    bitSet2.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                                    if (valueOf.booleanValue()) {
                                                                        bitSet.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                                    }
                                                                }
                                                                com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr4;
                                                                map15 = map3;
                                                                it = it3;
                                                                it2 = it4;
                                                                map14 = map4;
                                                                map16 = map6;
                                                                arrayMap3 = map5;
                                                                j4 = j5;
                                                            }
                                                        }
                                                        j5 = j4;
                                                        com_google_android_gms_internal_measurement_zzkjArr4 = com_google_android_gms_internal_measurement_zzkjArr3;
                                                        valueOf = null;
                                                        if (valueOf != null) {
                                                        }
                                                        zzgg().zzir().zzg("Event filter result", valueOf != null ? "null" : valueOf);
                                                        if (valueOf != null) {
                                                            bitSet2.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                            if (valueOf.booleanValue()) {
                                                                bitSet.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                            }
                                                        } else {
                                                            hashSet2.add(Integer.valueOf(i));
                                                        }
                                                        com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr4;
                                                        map15 = map3;
                                                        it = it3;
                                                        it2 = it4;
                                                        map14 = map4;
                                                        map16 = map6;
                                                        arrayMap3 = map5;
                                                        j4 = j5;
                                                    }
                                                    hashSet3 = new HashSet();
                                                    for (i9 = 0; i9 < i10; i9++) {
                                                        if (TextUtils.isEmpty(com_google_android_gms_internal_measurement_zzka2.zzart)) {
                                                            zzgg().zzin().zzg("null or empty param name in filter. event", zzgb().zzbe(str10));
                                                            j5 = j4;
                                                            com_google_android_gms_internal_measurement_zzkjArr4 = com_google_android_gms_internal_measurement_zzkjArr3;
                                                            break;
                                                        }
                                                        hashSet3.add(com_google_android_gms_internal_measurement_zzka2.zzart);
                                                    }
                                                    map14 = new ArrayMap();
                                                    com_google_android_gms_internal_measurement_zzkjArr4 = com_google_android_gms_internal_measurement_zzkjArr3;
                                                    length3 = com_google_android_gms_internal_measurement_zzkjArr4.length;
                                                    i10 = 0;
                                                    while (i10 < length3) {
                                                        com_google_android_gms_internal_measurement_zzkj = com_google_android_gms_internal_measurement_zzkjArr4[i10];
                                                        j5 = j4;
                                                        if (!hashSet3.contains(com_google_android_gms_internal_measurement_zzkj.name)) {
                                                            if (com_google_android_gms_internal_measurement_zzkj.zzasz == null) {
                                                                if (com_google_android_gms_internal_measurement_zzkj.zzaqx == null) {
                                                                    if (com_google_android_gms_internal_measurement_zzkj.zzajf != null) {
                                                                        zzin = zzgg().zzin();
                                                                        str4 = "Unknown value for param. event, param";
                                                                        zzbe = zzgb().zzbe(str10);
                                                                        zzbf = zzgb().zzbf(com_google_android_gms_internal_measurement_zzkj.name);
                                                                        break;
                                                                    }
                                                                    obj2 = com_google_android_gms_internal_measurement_zzkj.name;
                                                                    obj = com_google_android_gms_internal_measurement_zzkj.zzajf;
                                                                } else {
                                                                    obj2 = com_google_android_gms_internal_measurement_zzkj.name;
                                                                    obj = com_google_android_gms_internal_measurement_zzkj.zzaqx;
                                                                }
                                                            } else {
                                                                obj2 = com_google_android_gms_internal_measurement_zzkj.name;
                                                                obj = com_google_android_gms_internal_measurement_zzkj.zzasz;
                                                            }
                                                            map14.put(obj2, obj);
                                                        }
                                                        i10++;
                                                        j4 = j5;
                                                    }
                                                    j5 = j4;
                                                    com_google_android_gms_internal_measurement_zzkaArr = com_google_android_gms_internal_measurement_zzjz.zzarm;
                                                    length2 = com_google_android_gms_internal_measurement_zzkaArr.length;
                                                    i2 = 0;
                                                    while (i2 < length2) {
                                                        com_google_android_gms_internal_measurement_zzka = com_google_android_gms_internal_measurement_zzkaArr[i2];
                                                        equals = Boolean.TRUE.equals(com_google_android_gms_internal_measurement_zzka.zzars);
                                                        str8 = com_google_android_gms_internal_measurement_zzka.zzart;
                                                        if (TextUtils.isEmpty(str8)) {
                                                            zzgg().zzin().zzg("Event has empty param name. event", zzgb().zzbe(str10));
                                                        } else {
                                                            com_google_android_gms_internal_measurement_zzkaArr2 = com_google_android_gms_internal_measurement_zzkaArr;
                                                            obj2 = map14.get(str8);
                                                            i7 = length2;
                                                            if (obj2 instanceof Long) {
                                                                if (com_google_android_gms_internal_measurement_zzka.zzarr != null) {
                                                                    zzin = zzgg().zzin();
                                                                    str4 = "No number filter for long param. event, param";
                                                                } else {
                                                                    zza2 = zza(((Long) obj2).longValue(), com_google_android_gms_internal_measurement_zzka.zzarr);
                                                                    if (zza2 == null) {
                                                                        if (((zza2.booleanValue() ^ 1) ^ equals) != 0) {
                                                                            z = false;
                                                                            break;
                                                                        }
                                                                        i2++;
                                                                        com_google_android_gms_internal_measurement_zzkaArr = com_google_android_gms_internal_measurement_zzkaArr2;
                                                                        length2 = i7;
                                                                    }
                                                                }
                                                            } else if (obj2 instanceof Double) {
                                                                if (com_google_android_gms_internal_measurement_zzka.zzarr != null) {
                                                                    zzin = zzgg().zzin();
                                                                    str4 = "No number filter for double param. event, param";
                                                                } else {
                                                                    zza2 = zza(((Double) obj2).doubleValue(), com_google_android_gms_internal_measurement_zzka.zzarr);
                                                                    if (zza2 == null) {
                                                                        if (((zza2.booleanValue() ^ 1) ^ equals) != 0) {
                                                                            z = false;
                                                                            break;
                                                                        }
                                                                        i2++;
                                                                        com_google_android_gms_internal_measurement_zzkaArr = com_google_android_gms_internal_measurement_zzkaArr2;
                                                                        length2 = i7;
                                                                    }
                                                                }
                                                            } else if (!(obj2 instanceof String)) {
                                                                if (com_google_android_gms_internal_measurement_zzka.zzarq != null) {
                                                                    zza2 = zza((String) obj2, com_google_android_gms_internal_measurement_zzka.zzarq);
                                                                } else if (com_google_android_gms_internal_measurement_zzka.zzarr == null) {
                                                                    str5 = (String) obj2;
                                                                    if (zzjv.zzcd(str5)) {
                                                                        zza2 = zza(str5, com_google_android_gms_internal_measurement_zzka.zzarr);
                                                                    } else {
                                                                        zzin = zzgg().zzin();
                                                                        str4 = "Invalid param value for number filter. event, param";
                                                                    }
                                                                } else {
                                                                    zzin = zzgg().zzin();
                                                                    str4 = "No filter for String param. event, param";
                                                                }
                                                                if (zza2 == null) {
                                                                    if (((zza2.booleanValue() ^ 1) ^ equals) != 0) {
                                                                        z = false;
                                                                        break;
                                                                    }
                                                                    i2++;
                                                                    com_google_android_gms_internal_measurement_zzkaArr = com_google_android_gms_internal_measurement_zzkaArr2;
                                                                    length2 = i7;
                                                                }
                                                            } else if (obj2 == null) {
                                                                zzgg().zzir().zze("Missing param for filter. event, param", zzgb().zzbe(str10), zzgb().zzbf(str8));
                                                                z = false;
                                                                break;
                                                            } else {
                                                                zzin = zzgg().zzin();
                                                                str4 = "Unknown param type. event, param";
                                                            }
                                                            zzbe = zzgb().zzbe(str10);
                                                            zzbf = zzgb().zzbf(str8);
                                                            zzin.zze(str4, zzbe, zzbf);
                                                        }
                                                        valueOf = null;
                                                    }
                                                    z = true;
                                                    valueOf = Boolean.valueOf(z);
                                                    if (valueOf != null) {
                                                    }
                                                    zzgg().zzir().zzg("Event filter result", valueOf != null ? "null" : valueOf);
                                                    if (valueOf != null) {
                                                        hashSet2.add(Integer.valueOf(i));
                                                    } else {
                                                        bitSet2.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                        if (valueOf.booleanValue()) {
                                                            bitSet.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                        }
                                                    }
                                                    com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr4;
                                                    map15 = map3;
                                                    it = it3;
                                                    it2 = it4;
                                                    map14 = map4;
                                                    map16 = map6;
                                                    arrayMap3 = map5;
                                                    j4 = j5;
                                                }
                                                str9 = str;
                                            }
                                        }
                                        j5 = j4;
                                        com_google_android_gms_internal_measurement_zzkjArr4 = com_google_android_gms_internal_measurement_zzkjArr3;
                                        str2 = str;
                                        zzgg().zzin().zze("Invalid event filter ID. appId, id", zzfg.zzbh(str), String.valueOf(com_google_android_gms_internal_measurement_zzjz.zzark));
                                        com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr4;
                                        map15 = map3;
                                        it = it3;
                                        it2 = it4;
                                        map16 = map6;
                                        arrayMap3 = map5;
                                        j4 = j5;
                                        str9 = str2;
                                        map14 = map4;
                                    }
                                    map4 = map14;
                                    str2 = str9;
                                    hashSet = hashSet2;
                                    map13 = arrayMap3;
                                    map11 = map16;
                                    map14 = map17;
                                    map12 = map4;
                                }
                            }
                            arrayMap2 = map14;
                            str2 = str9;
                            map6 = map11;
                            map4 = map12;
                            hashSet4 = hashSet;
                            map5 = map13;
                            l3 = l2;
                            j6 = j;
                            i8 = i5 + 1;
                            com_google_android_gms_internal_measurement_zzkiArr2 = com_google_android_gms_internal_measurement_zzkiArr;
                            str7 = str2;
                            length = i4;
                            arrayMap6 = arrayMap2;
                            map2 = map4;
                            map = map6;
                            arrayMap3 = map5;
                            com_google_android_gms_internal_measurement_zzknArr2 = com_google_android_gms_internal_measurement_zzknArr;
                        }
                    } else {
                        i11 = i8;
                    }
                    obj2 = null;
                    if (obj2 == null) {
                        arrayMap = arrayMap6;
                        i4 = length;
                        com_google_android_gms_internal_measurement_zzkjArr = com_google_android_gms_internal_measurement_zzkjArr5;
                        i5 = i11;
                        if (obj != null) {
                            zzgc();
                            valueOf2 = Long.valueOf(0);
                            obj2 = zzjv.zzb(com_google_android_gms_internal_measurement_zzki2, "_epc");
                            if (obj2 == null) {
                                obj2 = valueOf2;
                            }
                            j6 = ((Long) obj2).longValue();
                            if (j6 > 0) {
                                l4 = l5;
                                j2 = 0;
                                zzga().zza(str7, l5, j6, com_google_android_gms_internal_measurement_zzki2);
                            } else {
                                zzgg().zzin().zzg("Complex event with zero extra param count. eventName", str10);
                                l4 = l5;
                                j2 = 0;
                            }
                            com_google_android_gms_internal_measurement_zzki = com_google_android_gms_internal_measurement_zzki2;
                            j = j6;
                            l2 = l4;
                            str8 = str10;
                        }
                    } else {
                        zzgc();
                        str10 = (String) zzjv.zzb(com_google_android_gms_internal_measurement_zzki2, "_en");
                        if (!TextUtils.isEmpty(str10)) {
                            zzgg().zzil().zzg("Extra parameter without an event name. eventId", l5);
                            arrayMap = arrayMap6;
                            i4 = length;
                            i5 = i11;
                        } else if (l5.longValue() != l3.longValue()) {
                            com_google_android_gms_internal_measurement_zzki3 = com_google_android_gms_internal_measurement_zzki;
                            l = l3;
                            com_google_android_gms_internal_measurement_zzki4 = com_google_android_gms_internal_measurement_zzki3;
                            j = j6 - 1;
                            if (j > 0) {
                                zzga = zzga();
                                zzga.zzab();
                                zzga.zzgg().zzir().zzg("Clearing complex main event info. appId", str7);
                                writableDatabase = zzga.getWritableDatabase();
                                str2 = "delete from main_event_params where app_id=?";
                                arrayMap7 = arrayMap6;
                                com_google_android_gms_internal_measurement_zzkjArr6 = com_google_android_gms_internal_measurement_zzkjArr5;
                                i2 = 1;
                                strArr = new String[1];
                                i3 = 0;
                                strArr[0] = str7;
                                writableDatabase.execSQL(str2, strArr);
                                i4 = length;
                                i9 = i2;
                                i5 = i11;
                                com_google_android_gms_internal_measurement_zzkjArr = com_google_android_gms_internal_measurement_zzkjArr6;
                                arrayMap = arrayMap7;
                            } else {
                                i3 = 0;
                                i5 = i11;
                                j7 = 0;
                                i4 = length;
                                arrayMap = arrayMap6;
                                i9 = 1;
                                com_google_android_gms_internal_measurement_zzkjArr = com_google_android_gms_internal_measurement_zzkjArr5;
                                zzga().zza(str7, l5, j, com_google_android_gms_internal_measurement_zzki4);
                            }
                            com_google_android_gms_internal_measurement_zzkjArr7 = new zzkj[(com_google_android_gms_internal_measurement_zzki4.zzasv.length + com_google_android_gms_internal_measurement_zzkjArr.length)];
                            com_google_android_gms_internal_measurement_zzkjArr2 = com_google_android_gms_internal_measurement_zzki4.zzasv;
                            intValue = com_google_android_gms_internal_measurement_zzkjArr2.length;
                            i6 = i3;
                            length = i6;
                            while (i6 < intValue) {
                                com_google_android_gms_internal_measurement_zzkj2 = com_google_android_gms_internal_measurement_zzkjArr2[i6];
                                zzgc();
                                if (zzjv.zza(com_google_android_gms_internal_measurement_zzki2, com_google_android_gms_internal_measurement_zzkj2.name) != null) {
                                    i9 = length + 1;
                                    com_google_android_gms_internal_measurement_zzkjArr7[length] = com_google_android_gms_internal_measurement_zzkj2;
                                    length = i9;
                                }
                                i6++;
                            }
                            if (length > 0) {
                                length2 = com_google_android_gms_internal_measurement_zzkjArr.length;
                                intValue = i3;
                                while (intValue < length2) {
                                    i2 = length + 1;
                                    com_google_android_gms_internal_measurement_zzkjArr7[length] = com_google_android_gms_internal_measurement_zzkjArr[intValue];
                                    intValue++;
                                    length = i2;
                                }
                                if (length != com_google_android_gms_internal_measurement_zzkjArr7.length) {
                                    com_google_android_gms_internal_measurement_zzkjArr7 = (zzkj[]) Arrays.copyOf(com_google_android_gms_internal_measurement_zzkjArr7, length);
                                }
                                com_google_android_gms_internal_measurement_zzkjArr = com_google_android_gms_internal_measurement_zzkjArr7;
                            } else {
                                zzgg().zzin().zzg("No unique parameters in main event. eventName", str10);
                            }
                            str8 = str10;
                            l2 = l;
                            j2 = 0;
                            com_google_android_gms_internal_measurement_zzki = com_google_android_gms_internal_measurement_zzki4;
                        } else {
                            zza3 = zzga().zza(str7, l5);
                            if (zza3 != null) {
                                if (zza3.first == null) {
                                    com_google_android_gms_internal_measurement_zzki5 = (zzki) zza3.first;
                                    j6 = ((Long) zza3.second).longValue();
                                    zzgc();
                                    l = (Long) zzjv.zzb(com_google_android_gms_internal_measurement_zzki5, "_eid");
                                    com_google_android_gms_internal_measurement_zzki4 = com_google_android_gms_internal_measurement_zzki5;
                                    j = j6 - 1;
                                    if (j > 0) {
                                        i3 = 0;
                                        i5 = i11;
                                        j7 = 0;
                                        i4 = length;
                                        arrayMap = arrayMap6;
                                        i9 = 1;
                                        com_google_android_gms_internal_measurement_zzkjArr = com_google_android_gms_internal_measurement_zzkjArr5;
                                        zzga().zza(str7, l5, j, com_google_android_gms_internal_measurement_zzki4);
                                    } else {
                                        zzga = zzga();
                                        zzga.zzab();
                                        zzga.zzgg().zzir().zzg("Clearing complex main event info. appId", str7);
                                        writableDatabase = zzga.getWritableDatabase();
                                        str2 = "delete from main_event_params where app_id=?";
                                        arrayMap7 = arrayMap6;
                                        com_google_android_gms_internal_measurement_zzkjArr6 = com_google_android_gms_internal_measurement_zzkjArr5;
                                        i2 = 1;
                                        strArr = new String[1];
                                        i3 = 0;
                                        strArr[0] = str7;
                                        writableDatabase.execSQL(str2, strArr);
                                        i4 = length;
                                        i9 = i2;
                                        i5 = i11;
                                        com_google_android_gms_internal_measurement_zzkjArr = com_google_android_gms_internal_measurement_zzkjArr6;
                                        arrayMap = arrayMap7;
                                    }
                                    com_google_android_gms_internal_measurement_zzkjArr7 = new zzkj[(com_google_android_gms_internal_measurement_zzki4.zzasv.length + com_google_android_gms_internal_measurement_zzkjArr.length)];
                                    com_google_android_gms_internal_measurement_zzkjArr2 = com_google_android_gms_internal_measurement_zzki4.zzasv;
                                    intValue = com_google_android_gms_internal_measurement_zzkjArr2.length;
                                    i6 = i3;
                                    length = i6;
                                    while (i6 < intValue) {
                                        com_google_android_gms_internal_measurement_zzkj2 = com_google_android_gms_internal_measurement_zzkjArr2[i6];
                                        zzgc();
                                        if (zzjv.zza(com_google_android_gms_internal_measurement_zzki2, com_google_android_gms_internal_measurement_zzkj2.name) != null) {
                                            i9 = length + 1;
                                            com_google_android_gms_internal_measurement_zzkjArr7[length] = com_google_android_gms_internal_measurement_zzkj2;
                                            length = i9;
                                        }
                                        i6++;
                                    }
                                    if (length > 0) {
                                        zzgg().zzin().zzg("No unique parameters in main event. eventName", str10);
                                    } else {
                                        length2 = com_google_android_gms_internal_measurement_zzkjArr.length;
                                        intValue = i3;
                                        while (intValue < length2) {
                                            i2 = length + 1;
                                            com_google_android_gms_internal_measurement_zzkjArr7[length] = com_google_android_gms_internal_measurement_zzkjArr[intValue];
                                            intValue++;
                                            length = i2;
                                        }
                                        if (length != com_google_android_gms_internal_measurement_zzkjArr7.length) {
                                            com_google_android_gms_internal_measurement_zzkjArr7 = (zzkj[]) Arrays.copyOf(com_google_android_gms_internal_measurement_zzkjArr7, length);
                                        }
                                        com_google_android_gms_internal_measurement_zzkjArr = com_google_android_gms_internal_measurement_zzkjArr7;
                                    }
                                    str8 = str10;
                                    l2 = l;
                                    j2 = 0;
                                    com_google_android_gms_internal_measurement_zzki = com_google_android_gms_internal_measurement_zzki4;
                                }
                            }
                            arrayMap = arrayMap6;
                            i4 = length;
                            i5 = i11;
                            zzgg().zzil().zze("Extra parameter without existing main event. eventName, eventId", str10, l5);
                        }
                        map5 = arrayMap3;
                        str2 = str7;
                        map6 = map;
                        map4 = map2;
                        arrayMap2 = arrayMap;
                        i8 = i5 + 1;
                        com_google_android_gms_internal_measurement_zzkiArr2 = com_google_android_gms_internal_measurement_zzkiArr;
                        str7 = str2;
                        length = i4;
                        arrayMap6 = arrayMap2;
                        map2 = map4;
                        map = map6;
                        arrayMap3 = map5;
                        com_google_android_gms_internal_measurement_zzknArr2 = com_google_android_gms_internal_measurement_zzknArr;
                    }
                    zze = zzga().zze(str7, com_google_android_gms_internal_measurement_zzki2.name);
                    if (zze != null) {
                        hashSet = hashSet4;
                        map13 = arrayMap3;
                        com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr;
                        str3 = str8;
                        map11 = map;
                        map12 = map2;
                        j3 = j2;
                        zze = zze.zzie();
                    } else {
                        zzgg().zzin().zze("Event aggregate wasn't created during raw event logging. appId, event", zzfg.zzbh(str), zzgb().zzbe(str8));
                        map11 = map;
                        map12 = map2;
                        hashSet = hashSet4;
                        map13 = arrayMap3;
                        com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr;
                        j3 = j2;
                        str3 = str8;
                        zze = new zzeq(str7, com_google_android_gms_internal_measurement_zzki2.name, 1, 1, com_google_android_gms_internal_measurement_zzki2.zzasw.longValue(), 0, null, null, null);
                    }
                    zzga().zza(zze);
                    j4 = zze.zzafp;
                    map14 = arrayMap;
                    str10 = str3;
                    map15 = (Map) map14.get(str10);
                    if (map15 != null) {
                        str9 = str;
                    } else {
                        str9 = str;
                        map15 = zzga().zzj(str9, str10);
                        if (map15 == null) {
                            map15 = new ArrayMap();
                        }
                        map14.put(str10, map15);
                    }
                    it = map15.keySet().iterator();
                    while (it.hasNext()) {
                        i = ((Integer) it.next()).intValue();
                        hashSet2 = hashSet;
                        if (hashSet2.contains(Integer.valueOf(i))) {
                            arrayMap3 = map13;
                            map16 = map11;
                            bitSet = (BitSet) map16.get(Integer.valueOf(i));
                            map17 = map14;
                            map14 = map12;
                            bitSet2 = (BitSet) map14.get(Integer.valueOf(i));
                            if (((zzkh) arrayMap3.get(Integer.valueOf(i))) == null) {
                                com_google_android_gms_internal_measurement_zzkh = new zzkh();
                                arrayMap3.put(Integer.valueOf(i), com_google_android_gms_internal_measurement_zzkh);
                                com_google_android_gms_internal_measurement_zzkh.zzast = Boolean.valueOf(true);
                                bitSet3 = new BitSet();
                                map16.put(Integer.valueOf(i), bitSet3);
                                bitSet2 = new BitSet();
                                map14.put(Integer.valueOf(i), bitSet2);
                                bitSet = bitSet3;
                            }
                            it2 = ((List) map15.get(Integer.valueOf(i))).iterator();
                            while (it2.hasNext()) {
                                map3 = map15;
                                com_google_android_gms_internal_measurement_zzjz = (zzjz) it2.next();
                                it3 = it;
                                it4 = it2;
                                if (zzgg().isLoggable(2)) {
                                    map4 = map14;
                                    map5 = arrayMap3;
                                    map6 = map16;
                                } else {
                                    map4 = map14;
                                    map6 = map16;
                                    map5 = arrayMap3;
                                    zzgg().zzir().zzd("Evaluating filter. audience, filter, event", Integer.valueOf(i), com_google_android_gms_internal_measurement_zzjz.zzark, zzgb().zzbe(com_google_android_gms_internal_measurement_zzjz.zzarl));
                                    zzgg().zzir().zzg("Filter definition", zzgb().zza(com_google_android_gms_internal_measurement_zzjz));
                                }
                                if (com_google_android_gms_internal_measurement_zzjz.zzark != null) {
                                    if (com_google_android_gms_internal_measurement_zzjz.zzark.intValue() > 256) {
                                        if (bitSet.get(com_google_android_gms_internal_measurement_zzjz.zzark.intValue())) {
                                            if (com_google_android_gms_internal_measurement_zzjz.zzaro != null) {
                                                zza = zza(j4, com_google_android_gms_internal_measurement_zzjz.zzaro);
                                                if (zza != null) {
                                                    if (zza.booleanValue()) {
                                                        j5 = j4;
                                                        valueOf = Boolean.valueOf(false);
                                                        com_google_android_gms_internal_measurement_zzkjArr4 = com_google_android_gms_internal_measurement_zzkjArr3;
                                                        if (valueOf != null) {
                                                        }
                                                        zzgg().zzir().zzg("Event filter result", valueOf != null ? "null" : valueOf);
                                                        if (valueOf != null) {
                                                            bitSet2.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                            if (valueOf.booleanValue()) {
                                                                bitSet.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                            }
                                                        } else {
                                                            hashSet2.add(Integer.valueOf(i));
                                                        }
                                                        com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr4;
                                                        map15 = map3;
                                                        it = it3;
                                                        it2 = it4;
                                                        map14 = map4;
                                                        map16 = map6;
                                                        arrayMap3 = map5;
                                                        j4 = j5;
                                                    }
                                                }
                                                j5 = j4;
                                                com_google_android_gms_internal_measurement_zzkjArr4 = com_google_android_gms_internal_measurement_zzkjArr3;
                                                valueOf = null;
                                                if (valueOf != null) {
                                                }
                                                zzgg().zzir().zzg("Event filter result", valueOf != null ? "null" : valueOf);
                                                if (valueOf != null) {
                                                    hashSet2.add(Integer.valueOf(i));
                                                } else {
                                                    bitSet2.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                    if (valueOf.booleanValue()) {
                                                        bitSet.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                    }
                                                }
                                                com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr4;
                                                map15 = map3;
                                                it = it3;
                                                it2 = it4;
                                                map14 = map4;
                                                map16 = map6;
                                                arrayMap3 = map5;
                                                j4 = j5;
                                            }
                                            hashSet3 = new HashSet();
                                            for (i9 = 0; i9 < i10; i9++) {
                                                if (TextUtils.isEmpty(com_google_android_gms_internal_measurement_zzka2.zzart)) {
                                                    zzgg().zzin().zzg("null or empty param name in filter. event", zzgb().zzbe(str10));
                                                    j5 = j4;
                                                    com_google_android_gms_internal_measurement_zzkjArr4 = com_google_android_gms_internal_measurement_zzkjArr3;
                                                    break;
                                                }
                                                hashSet3.add(com_google_android_gms_internal_measurement_zzka2.zzart);
                                            }
                                            map14 = new ArrayMap();
                                            com_google_android_gms_internal_measurement_zzkjArr4 = com_google_android_gms_internal_measurement_zzkjArr3;
                                            length3 = com_google_android_gms_internal_measurement_zzkjArr4.length;
                                            i10 = 0;
                                            while (i10 < length3) {
                                                com_google_android_gms_internal_measurement_zzkj = com_google_android_gms_internal_measurement_zzkjArr4[i10];
                                                j5 = j4;
                                                if (!hashSet3.contains(com_google_android_gms_internal_measurement_zzkj.name)) {
                                                    if (com_google_android_gms_internal_measurement_zzkj.zzasz == null) {
                                                        obj2 = com_google_android_gms_internal_measurement_zzkj.name;
                                                        obj = com_google_android_gms_internal_measurement_zzkj.zzasz;
                                                    } else if (com_google_android_gms_internal_measurement_zzkj.zzaqx == null) {
                                                        obj2 = com_google_android_gms_internal_measurement_zzkj.name;
                                                        obj = com_google_android_gms_internal_measurement_zzkj.zzaqx;
                                                    } else if (com_google_android_gms_internal_measurement_zzkj.zzajf != null) {
                                                        zzin = zzgg().zzin();
                                                        str4 = "Unknown value for param. event, param";
                                                        zzbe = zzgb().zzbe(str10);
                                                        zzbf = zzgb().zzbf(com_google_android_gms_internal_measurement_zzkj.name);
                                                        break;
                                                    } else {
                                                        obj2 = com_google_android_gms_internal_measurement_zzkj.name;
                                                        obj = com_google_android_gms_internal_measurement_zzkj.zzajf;
                                                    }
                                                    map14.put(obj2, obj);
                                                }
                                                i10++;
                                                j4 = j5;
                                            }
                                            j5 = j4;
                                            com_google_android_gms_internal_measurement_zzkaArr = com_google_android_gms_internal_measurement_zzjz.zzarm;
                                            length2 = com_google_android_gms_internal_measurement_zzkaArr.length;
                                            i2 = 0;
                                            while (i2 < length2) {
                                                com_google_android_gms_internal_measurement_zzka = com_google_android_gms_internal_measurement_zzkaArr[i2];
                                                equals = Boolean.TRUE.equals(com_google_android_gms_internal_measurement_zzka.zzars);
                                                str8 = com_google_android_gms_internal_measurement_zzka.zzart;
                                                if (TextUtils.isEmpty(str8)) {
                                                    com_google_android_gms_internal_measurement_zzkaArr2 = com_google_android_gms_internal_measurement_zzkaArr;
                                                    obj2 = map14.get(str8);
                                                    i7 = length2;
                                                    if (obj2 instanceof Long) {
                                                        if (obj2 instanceof Double) {
                                                            if (!(obj2 instanceof String)) {
                                                                if (obj2 == null) {
                                                                    zzgg().zzir().zze("Missing param for filter. event, param", zzgb().zzbe(str10), zzgb().zzbf(str8));
                                                                    z = false;
                                                                    break;
                                                                }
                                                                zzin = zzgg().zzin();
                                                                str4 = "Unknown param type. event, param";
                                                            } else {
                                                                if (com_google_android_gms_internal_measurement_zzka.zzarq != null) {
                                                                    zza2 = zza((String) obj2, com_google_android_gms_internal_measurement_zzka.zzarq);
                                                                } else if (com_google_android_gms_internal_measurement_zzka.zzarr == null) {
                                                                    zzin = zzgg().zzin();
                                                                    str4 = "No filter for String param. event, param";
                                                                } else {
                                                                    str5 = (String) obj2;
                                                                    if (zzjv.zzcd(str5)) {
                                                                        zzin = zzgg().zzin();
                                                                        str4 = "Invalid param value for number filter. event, param";
                                                                    } else {
                                                                        zza2 = zza(str5, com_google_android_gms_internal_measurement_zzka.zzarr);
                                                                    }
                                                                }
                                                                if (zza2 == null) {
                                                                    if (((zza2.booleanValue() ^ 1) ^ equals) != 0) {
                                                                        z = false;
                                                                        break;
                                                                    }
                                                                    i2++;
                                                                    com_google_android_gms_internal_measurement_zzkaArr = com_google_android_gms_internal_measurement_zzkaArr2;
                                                                    length2 = i7;
                                                                }
                                                            }
                                                        } else if (com_google_android_gms_internal_measurement_zzka.zzarr != null) {
                                                            zza2 = zza(((Double) obj2).doubleValue(), com_google_android_gms_internal_measurement_zzka.zzarr);
                                                            if (zza2 == null) {
                                                                if (((zza2.booleanValue() ^ 1) ^ equals) != 0) {
                                                                    z = false;
                                                                    break;
                                                                }
                                                                i2++;
                                                                com_google_android_gms_internal_measurement_zzkaArr = com_google_android_gms_internal_measurement_zzkaArr2;
                                                                length2 = i7;
                                                            }
                                                        } else {
                                                            zzin = zzgg().zzin();
                                                            str4 = "No number filter for double param. event, param";
                                                        }
                                                    } else if (com_google_android_gms_internal_measurement_zzka.zzarr != null) {
                                                        zza2 = zza(((Long) obj2).longValue(), com_google_android_gms_internal_measurement_zzka.zzarr);
                                                        if (zza2 == null) {
                                                            if (((zza2.booleanValue() ^ 1) ^ equals) != 0) {
                                                                z = false;
                                                                break;
                                                            }
                                                            i2++;
                                                            com_google_android_gms_internal_measurement_zzkaArr = com_google_android_gms_internal_measurement_zzkaArr2;
                                                            length2 = i7;
                                                        }
                                                    } else {
                                                        zzin = zzgg().zzin();
                                                        str4 = "No number filter for long param. event, param";
                                                    }
                                                    zzbe = zzgb().zzbe(str10);
                                                    zzbf = zzgb().zzbf(str8);
                                                    zzin.zze(str4, zzbe, zzbf);
                                                } else {
                                                    zzgg().zzin().zzg("Event has empty param name. event", zzgb().zzbe(str10));
                                                }
                                                valueOf = null;
                                            }
                                            z = true;
                                            valueOf = Boolean.valueOf(z);
                                            if (valueOf != null) {
                                            }
                                            zzgg().zzir().zzg("Event filter result", valueOf != null ? "null" : valueOf);
                                            if (valueOf != null) {
                                                bitSet2.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                if (valueOf.booleanValue()) {
                                                    bitSet.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                }
                                            } else {
                                                hashSet2.add(Integer.valueOf(i));
                                            }
                                            com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr4;
                                            map15 = map3;
                                            it = it3;
                                            it2 = it4;
                                            map14 = map4;
                                            map16 = map6;
                                            arrayMap3 = map5;
                                            j4 = j5;
                                        } else {
                                            zzgg().zzir().zze("Event filter already evaluated true. audience ID, filter ID", Integer.valueOf(i), com_google_android_gms_internal_measurement_zzjz.zzark);
                                            map15 = map3;
                                            it = it3;
                                            it2 = it4;
                                            map14 = map4;
                                            map16 = map6;
                                            arrayMap3 = map5;
                                        }
                                        str9 = str;
                                    }
                                }
                                j5 = j4;
                                com_google_android_gms_internal_measurement_zzkjArr4 = com_google_android_gms_internal_measurement_zzkjArr3;
                                str2 = str;
                                zzgg().zzin().zze("Invalid event filter ID. appId, id", zzfg.zzbh(str), String.valueOf(com_google_android_gms_internal_measurement_zzjz.zzark));
                                com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr4;
                                map15 = map3;
                                it = it3;
                                it2 = it4;
                                map16 = map6;
                                arrayMap3 = map5;
                                j4 = j5;
                                str9 = str2;
                                map14 = map4;
                            }
                            map4 = map14;
                            str2 = str9;
                            hashSet = hashSet2;
                            map13 = arrayMap3;
                            map11 = map16;
                            map14 = map17;
                            map12 = map4;
                        } else {
                            zzgg().zzir().zzg("Skipping failed audience ID", Integer.valueOf(i));
                            hashSet = hashSet2;
                        }
                    }
                    arrayMap2 = map14;
                    str2 = str9;
                    map6 = map11;
                    map4 = map12;
                    hashSet4 = hashSet;
                    map5 = map13;
                    l3 = l2;
                    j6 = j;
                    i8 = i5 + 1;
                    com_google_android_gms_internal_measurement_zzkiArr2 = com_google_android_gms_internal_measurement_zzkiArr;
                    str7 = str2;
                    length = i4;
                    arrayMap6 = arrayMap2;
                    map2 = map4;
                    map = map6;
                    arrayMap3 = map5;
                    com_google_android_gms_internal_measurement_zzknArr2 = com_google_android_gms_internal_measurement_zzknArr;
                } else {
                    i5 = i8;
                    arrayMap = arrayMap6;
                    i4 = length;
                    com_google_android_gms_internal_measurement_zzkjArr = com_google_android_gms_internal_measurement_zzkjArr5;
                }
                j2 = 0;
                str8 = str10;
                j = j6;
                l2 = l3;
                zze = zzga().zze(str7, com_google_android_gms_internal_measurement_zzki2.name);
                if (zze != null) {
                    zzgg().zzin().zze("Event aggregate wasn't created during raw event logging. appId, event", zzfg.zzbh(str), zzgb().zzbe(str8));
                    map11 = map;
                    map12 = map2;
                    hashSet = hashSet4;
                    map13 = arrayMap3;
                    com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr;
                    j3 = j2;
                    str3 = str8;
                    zze = new zzeq(str7, com_google_android_gms_internal_measurement_zzki2.name, 1, 1, com_google_android_gms_internal_measurement_zzki2.zzasw.longValue(), 0, null, null, null);
                } else {
                    hashSet = hashSet4;
                    map13 = arrayMap3;
                    com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr;
                    str3 = str8;
                    map11 = map;
                    map12 = map2;
                    j3 = j2;
                    zze = zze.zzie();
                }
                zzga().zza(zze);
                j4 = zze.zzafp;
                map14 = arrayMap;
                str10 = str3;
                map15 = (Map) map14.get(str10);
                if (map15 != null) {
                    str9 = str;
                    map15 = zzga().zzj(str9, str10);
                    if (map15 == null) {
                        map15 = new ArrayMap();
                    }
                    map14.put(str10, map15);
                } else {
                    str9 = str;
                }
                it = map15.keySet().iterator();
                while (it.hasNext()) {
                    i = ((Integer) it.next()).intValue();
                    hashSet2 = hashSet;
                    if (hashSet2.contains(Integer.valueOf(i))) {
                        zzgg().zzir().zzg("Skipping failed audience ID", Integer.valueOf(i));
                        hashSet = hashSet2;
                    } else {
                        arrayMap3 = map13;
                        map16 = map11;
                        bitSet = (BitSet) map16.get(Integer.valueOf(i));
                        map17 = map14;
                        map14 = map12;
                        bitSet2 = (BitSet) map14.get(Integer.valueOf(i));
                        if (((zzkh) arrayMap3.get(Integer.valueOf(i))) == null) {
                            com_google_android_gms_internal_measurement_zzkh = new zzkh();
                            arrayMap3.put(Integer.valueOf(i), com_google_android_gms_internal_measurement_zzkh);
                            com_google_android_gms_internal_measurement_zzkh.zzast = Boolean.valueOf(true);
                            bitSet3 = new BitSet();
                            map16.put(Integer.valueOf(i), bitSet3);
                            bitSet2 = new BitSet();
                            map14.put(Integer.valueOf(i), bitSet2);
                            bitSet = bitSet3;
                        }
                        it2 = ((List) map15.get(Integer.valueOf(i))).iterator();
                        while (it2.hasNext()) {
                            map3 = map15;
                            com_google_android_gms_internal_measurement_zzjz = (zzjz) it2.next();
                            it3 = it;
                            it4 = it2;
                            if (zzgg().isLoggable(2)) {
                                map4 = map14;
                                map6 = map16;
                                map5 = arrayMap3;
                                zzgg().zzir().zzd("Evaluating filter. audience, filter, event", Integer.valueOf(i), com_google_android_gms_internal_measurement_zzjz.zzark, zzgb().zzbe(com_google_android_gms_internal_measurement_zzjz.zzarl));
                                zzgg().zzir().zzg("Filter definition", zzgb().zza(com_google_android_gms_internal_measurement_zzjz));
                            } else {
                                map4 = map14;
                                map5 = arrayMap3;
                                map6 = map16;
                            }
                            if (com_google_android_gms_internal_measurement_zzjz.zzark != null) {
                                if (com_google_android_gms_internal_measurement_zzjz.zzark.intValue() > 256) {
                                    if (bitSet.get(com_google_android_gms_internal_measurement_zzjz.zzark.intValue())) {
                                        zzgg().zzir().zze("Event filter already evaluated true. audience ID, filter ID", Integer.valueOf(i), com_google_android_gms_internal_measurement_zzjz.zzark);
                                        map15 = map3;
                                        it = it3;
                                        it2 = it4;
                                        map14 = map4;
                                        map16 = map6;
                                        arrayMap3 = map5;
                                    } else {
                                        if (com_google_android_gms_internal_measurement_zzjz.zzaro != null) {
                                            zza = zza(j4, com_google_android_gms_internal_measurement_zzjz.zzaro);
                                            if (zza != null) {
                                                if (zza.booleanValue()) {
                                                    j5 = j4;
                                                    valueOf = Boolean.valueOf(false);
                                                    com_google_android_gms_internal_measurement_zzkjArr4 = com_google_android_gms_internal_measurement_zzkjArr3;
                                                    if (valueOf != null) {
                                                    }
                                                    zzgg().zzir().zzg("Event filter result", valueOf != null ? "null" : valueOf);
                                                    if (valueOf != null) {
                                                        hashSet2.add(Integer.valueOf(i));
                                                    } else {
                                                        bitSet2.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                        if (valueOf.booleanValue()) {
                                                            bitSet.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                        }
                                                    }
                                                    com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr4;
                                                    map15 = map3;
                                                    it = it3;
                                                    it2 = it4;
                                                    map14 = map4;
                                                    map16 = map6;
                                                    arrayMap3 = map5;
                                                    j4 = j5;
                                                }
                                            }
                                            j5 = j4;
                                            com_google_android_gms_internal_measurement_zzkjArr4 = com_google_android_gms_internal_measurement_zzkjArr3;
                                            valueOf = null;
                                            if (valueOf != null) {
                                            }
                                            zzgg().zzir().zzg("Event filter result", valueOf != null ? "null" : valueOf);
                                            if (valueOf != null) {
                                                bitSet2.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                if (valueOf.booleanValue()) {
                                                    bitSet.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                }
                                            } else {
                                                hashSet2.add(Integer.valueOf(i));
                                            }
                                            com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr4;
                                            map15 = map3;
                                            it = it3;
                                            it2 = it4;
                                            map14 = map4;
                                            map16 = map6;
                                            arrayMap3 = map5;
                                            j4 = j5;
                                        }
                                        hashSet3 = new HashSet();
                                        for (i9 = 0; i9 < i10; i9++) {
                                            if (TextUtils.isEmpty(com_google_android_gms_internal_measurement_zzka2.zzart)) {
                                                zzgg().zzin().zzg("null or empty param name in filter. event", zzgb().zzbe(str10));
                                                j5 = j4;
                                                com_google_android_gms_internal_measurement_zzkjArr4 = com_google_android_gms_internal_measurement_zzkjArr3;
                                                break;
                                            }
                                            hashSet3.add(com_google_android_gms_internal_measurement_zzka2.zzart);
                                        }
                                        map14 = new ArrayMap();
                                        com_google_android_gms_internal_measurement_zzkjArr4 = com_google_android_gms_internal_measurement_zzkjArr3;
                                        length3 = com_google_android_gms_internal_measurement_zzkjArr4.length;
                                        i10 = 0;
                                        while (i10 < length3) {
                                            com_google_android_gms_internal_measurement_zzkj = com_google_android_gms_internal_measurement_zzkjArr4[i10];
                                            j5 = j4;
                                            if (!hashSet3.contains(com_google_android_gms_internal_measurement_zzkj.name)) {
                                                if (com_google_android_gms_internal_measurement_zzkj.zzasz == null) {
                                                    if (com_google_android_gms_internal_measurement_zzkj.zzaqx == null) {
                                                        if (com_google_android_gms_internal_measurement_zzkj.zzajf != null) {
                                                            zzin = zzgg().zzin();
                                                            str4 = "Unknown value for param. event, param";
                                                            zzbe = zzgb().zzbe(str10);
                                                            zzbf = zzgb().zzbf(com_google_android_gms_internal_measurement_zzkj.name);
                                                            break;
                                                        }
                                                        obj2 = com_google_android_gms_internal_measurement_zzkj.name;
                                                        obj = com_google_android_gms_internal_measurement_zzkj.zzajf;
                                                    } else {
                                                        obj2 = com_google_android_gms_internal_measurement_zzkj.name;
                                                        obj = com_google_android_gms_internal_measurement_zzkj.zzaqx;
                                                    }
                                                } else {
                                                    obj2 = com_google_android_gms_internal_measurement_zzkj.name;
                                                    obj = com_google_android_gms_internal_measurement_zzkj.zzasz;
                                                }
                                                map14.put(obj2, obj);
                                            }
                                            i10++;
                                            j4 = j5;
                                        }
                                        j5 = j4;
                                        com_google_android_gms_internal_measurement_zzkaArr = com_google_android_gms_internal_measurement_zzjz.zzarm;
                                        length2 = com_google_android_gms_internal_measurement_zzkaArr.length;
                                        i2 = 0;
                                        while (i2 < length2) {
                                            com_google_android_gms_internal_measurement_zzka = com_google_android_gms_internal_measurement_zzkaArr[i2];
                                            equals = Boolean.TRUE.equals(com_google_android_gms_internal_measurement_zzka.zzars);
                                            str8 = com_google_android_gms_internal_measurement_zzka.zzart;
                                            if (TextUtils.isEmpty(str8)) {
                                                zzgg().zzin().zzg("Event has empty param name. event", zzgb().zzbe(str10));
                                            } else {
                                                com_google_android_gms_internal_measurement_zzkaArr2 = com_google_android_gms_internal_measurement_zzkaArr;
                                                obj2 = map14.get(str8);
                                                i7 = length2;
                                                if (obj2 instanceof Long) {
                                                    if (com_google_android_gms_internal_measurement_zzka.zzarr != null) {
                                                        zzin = zzgg().zzin();
                                                        str4 = "No number filter for long param. event, param";
                                                    } else {
                                                        zza2 = zza(((Long) obj2).longValue(), com_google_android_gms_internal_measurement_zzka.zzarr);
                                                        if (zza2 == null) {
                                                            if (((zza2.booleanValue() ^ 1) ^ equals) != 0) {
                                                                z = false;
                                                                break;
                                                            }
                                                            i2++;
                                                            com_google_android_gms_internal_measurement_zzkaArr = com_google_android_gms_internal_measurement_zzkaArr2;
                                                            length2 = i7;
                                                        }
                                                    }
                                                } else if (obj2 instanceof Double) {
                                                    if (com_google_android_gms_internal_measurement_zzka.zzarr != null) {
                                                        zzin = zzgg().zzin();
                                                        str4 = "No number filter for double param. event, param";
                                                    } else {
                                                        zza2 = zza(((Double) obj2).doubleValue(), com_google_android_gms_internal_measurement_zzka.zzarr);
                                                        if (zza2 == null) {
                                                            if (((zza2.booleanValue() ^ 1) ^ equals) != 0) {
                                                                z = false;
                                                                break;
                                                            }
                                                            i2++;
                                                            com_google_android_gms_internal_measurement_zzkaArr = com_google_android_gms_internal_measurement_zzkaArr2;
                                                            length2 = i7;
                                                        }
                                                    }
                                                } else if (!(obj2 instanceof String)) {
                                                    if (com_google_android_gms_internal_measurement_zzka.zzarq != null) {
                                                        zza2 = zza((String) obj2, com_google_android_gms_internal_measurement_zzka.zzarq);
                                                    } else if (com_google_android_gms_internal_measurement_zzka.zzarr == null) {
                                                        str5 = (String) obj2;
                                                        if (zzjv.zzcd(str5)) {
                                                            zza2 = zza(str5, com_google_android_gms_internal_measurement_zzka.zzarr);
                                                        } else {
                                                            zzin = zzgg().zzin();
                                                            str4 = "Invalid param value for number filter. event, param";
                                                        }
                                                    } else {
                                                        zzin = zzgg().zzin();
                                                        str4 = "No filter for String param. event, param";
                                                    }
                                                    if (zza2 == null) {
                                                        if (((zza2.booleanValue() ^ 1) ^ equals) != 0) {
                                                            z = false;
                                                            break;
                                                        }
                                                        i2++;
                                                        com_google_android_gms_internal_measurement_zzkaArr = com_google_android_gms_internal_measurement_zzkaArr2;
                                                        length2 = i7;
                                                    }
                                                } else if (obj2 == null) {
                                                    zzgg().zzir().zze("Missing param for filter. event, param", zzgb().zzbe(str10), zzgb().zzbf(str8));
                                                    z = false;
                                                    break;
                                                } else {
                                                    zzin = zzgg().zzin();
                                                    str4 = "Unknown param type. event, param";
                                                }
                                                zzbe = zzgb().zzbe(str10);
                                                zzbf = zzgb().zzbf(str8);
                                                zzin.zze(str4, zzbe, zzbf);
                                            }
                                            valueOf = null;
                                        }
                                        z = true;
                                        valueOf = Boolean.valueOf(z);
                                        if (valueOf != null) {
                                        }
                                        zzgg().zzir().zzg("Event filter result", valueOf != null ? "null" : valueOf);
                                        if (valueOf != null) {
                                            hashSet2.add(Integer.valueOf(i));
                                        } else {
                                            bitSet2.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                            if (valueOf.booleanValue()) {
                                                bitSet.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                            }
                                        }
                                        com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr4;
                                        map15 = map3;
                                        it = it3;
                                        it2 = it4;
                                        map14 = map4;
                                        map16 = map6;
                                        arrayMap3 = map5;
                                        j4 = j5;
                                    }
                                    str9 = str;
                                }
                            }
                            j5 = j4;
                            com_google_android_gms_internal_measurement_zzkjArr4 = com_google_android_gms_internal_measurement_zzkjArr3;
                            str2 = str;
                            zzgg().zzin().zze("Invalid event filter ID. appId, id", zzfg.zzbh(str), String.valueOf(com_google_android_gms_internal_measurement_zzjz.zzark));
                            com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr4;
                            map15 = map3;
                            it = it3;
                            it2 = it4;
                            map16 = map6;
                            arrayMap3 = map5;
                            j4 = j5;
                            str9 = str2;
                            map14 = map4;
                        }
                        map4 = map14;
                        str2 = str9;
                        hashSet = hashSet2;
                        map13 = arrayMap3;
                        map11 = map16;
                        map14 = map17;
                        map12 = map4;
                    }
                }
                arrayMap2 = map14;
                str2 = str9;
                map6 = map11;
                map4 = map12;
                hashSet4 = hashSet;
                map5 = map13;
                l3 = l2;
                j6 = j;
                i8 = i5 + 1;
                com_google_android_gms_internal_measurement_zzkiArr2 = com_google_android_gms_internal_measurement_zzkiArr;
                str7 = str2;
                length = i4;
                arrayMap6 = arrayMap2;
                map2 = map4;
                map = map6;
                arrayMap3 = map5;
                com_google_android_gms_internal_measurement_zzknArr2 = com_google_android_gms_internal_measurement_zzknArr;
            }
        }
        map5 = arrayMap3;
        str2 = str7;
        map6 = map;
        map4 = map2;
        zzkn[] com_google_android_gms_internal_measurement_zzknArr3 = com_google_android_gms_internal_measurement_zzknArr2;
        if (com_google_android_gms_internal_measurement_zzknArr3 != null) {
            Map arrayMap8 = new ArrayMap();
            i6 = com_google_android_gms_internal_measurement_zzknArr3.length;
            length = 0;
            while (length < i6) {
                Map map18;
                int i12;
                Map map19;
                zzkn com_google_android_gms_internal_measurement_zzkn = com_google_android_gms_internal_measurement_zzknArr3[length];
                Map map20 = (Map) arrayMap8.get(com_google_android_gms_internal_measurement_zzkn.name);
                if (map20 == null) {
                    map20 = zzga().zzk(str2, com_google_android_gms_internal_measurement_zzkn.name);
                    if (map20 == null) {
                        map20 = new ArrayMap();
                    }
                    arrayMap8.put(com_google_android_gms_internal_measurement_zzkn.name, map20);
                }
                it2 = map20.keySet().iterator();
                while (it2.hasNext()) {
                    intValue2 = ((Integer) it2.next()).intValue();
                    if (hashSet4.contains(Integer.valueOf(intValue2))) {
                        zzgg().zzir().zzg("Skipping failed audience ID", Integer.valueOf(intValue2));
                    } else {
                        Map map21;
                        map7 = map5;
                        Map map22 = map6;
                        BitSet bitSet6 = (BitSet) map22.get(Integer.valueOf(intValue2));
                        map18 = arrayMap8;
                        arrayMap8 = map4;
                        BitSet bitSet7 = (BitSet) arrayMap8.get(Integer.valueOf(intValue2));
                        if (((zzkh) map7.get(Integer.valueOf(intValue2))) == null) {
                            com_google_android_gms_internal_measurement_zzkh2 = new zzkh();
                            map7.put(Integer.valueOf(intValue2), com_google_android_gms_internal_measurement_zzkh2);
                            com_google_android_gms_internal_measurement_zzkh2.zzast = Boolean.valueOf(true);
                            bitSet6 = new BitSet();
                            map22.put(Integer.valueOf(intValue2), bitSet6);
                            bitSet7 = new BitSet();
                            arrayMap8.put(Integer.valueOf(intValue2), bitSet7);
                        }
                        Iterator it7 = ((List) map20.get(Integer.valueOf(intValue2))).iterator();
                        while (it7.hasNext()) {
                            Iterator it8;
                            i12 = i6;
                            zzkc com_google_android_gms_internal_measurement_zzkc = (zzkc) it7.next();
                            map21 = map20;
                            Iterator it9 = it2;
                            if (zzgg().isLoggable(2)) {
                                it8 = it7;
                                map8 = arrayMap8;
                                map9 = map7;
                                map19 = map22;
                                zzgg().zzir().zzd("Evaluating filter. audience, filter, property", Integer.valueOf(intValue2), com_google_android_gms_internal_measurement_zzkc.zzark, zzgb().zzbg(com_google_android_gms_internal_measurement_zzkc.zzasa));
                                zzgg().zzir().zzg("Filter definition", zzgb().zza(com_google_android_gms_internal_measurement_zzkc));
                            } else {
                                map8 = arrayMap8;
                                it8 = it7;
                                map9 = map7;
                                map19 = map22;
                            }
                            if (com_google_android_gms_internal_measurement_zzkc.zzark != null) {
                                if (com_google_android_gms_internal_measurement_zzkc.zzark.intValue() <= 256) {
                                    if (bitSet6.get(com_google_android_gms_internal_measurement_zzkc.zzark.intValue())) {
                                        zzgg().zzir().zze("Property filter already evaluated true. audience ID, filter ID", Integer.valueOf(intValue2), com_google_android_gms_internal_measurement_zzkc.zzark);
                                    } else {
                                        zzfi zzin2;
                                        String str11;
                                        zzka com_google_android_gms_internal_measurement_zzka3 = com_google_android_gms_internal_measurement_zzkc.zzasb;
                                        if (com_google_android_gms_internal_measurement_zzka3 == null) {
                                            zzin2 = zzgg().zzin();
                                            str11 = "Missing property filter. property";
                                        } else {
                                            boolean equals2 = Boolean.TRUE.equals(com_google_android_gms_internal_measurement_zzka3.zzars);
                                            if (com_google_android_gms_internal_measurement_zzkn.zzasz != null) {
                                                if (com_google_android_gms_internal_measurement_zzka3.zzarr == null) {
                                                    zzin2 = zzgg().zzin();
                                                    str11 = "No number filter for long property. property";
                                                } else {
                                                    valueOf = zza(com_google_android_gms_internal_measurement_zzkn.zzasz.longValue(), com_google_android_gms_internal_measurement_zzka3.zzarr);
                                                }
                                            } else if (com_google_android_gms_internal_measurement_zzkn.zzaqx != null) {
                                                if (com_google_android_gms_internal_measurement_zzka3.zzarr == null) {
                                                    zzin2 = zzgg().zzin();
                                                    str11 = "No number filter for double property. property";
                                                } else {
                                                    valueOf = zza(com_google_android_gms_internal_measurement_zzkn.zzaqx.doubleValue(), com_google_android_gms_internal_measurement_zzka3.zzarr);
                                                }
                                            } else if (com_google_android_gms_internal_measurement_zzkn.zzajf == null) {
                                                zzin2 = zzgg().zzin();
                                                str11 = "User property has no value, property";
                                            } else if (com_google_android_gms_internal_measurement_zzka3.zzarq != null) {
                                                valueOf = zza(com_google_android_gms_internal_measurement_zzkn.zzajf, com_google_android_gms_internal_measurement_zzka3.zzarq);
                                            } else if (com_google_android_gms_internal_measurement_zzka3.zzarr == null) {
                                                zzin2 = zzgg().zzin();
                                                str11 = "No string or number filter defined. property";
                                            } else if (zzjv.zzcd(com_google_android_gms_internal_measurement_zzkn.zzajf)) {
                                                valueOf = zza(com_google_android_gms_internal_measurement_zzkn.zzajf, com_google_android_gms_internal_measurement_zzka3.zzarr);
                                            } else {
                                                zzgg().zzin().zze("Invalid user property value for Numeric number filter. property, value", zzgb().zzbg(com_google_android_gms_internal_measurement_zzkn.name), com_google_android_gms_internal_measurement_zzkn.zzajf);
                                                valueOf = null;
                                                zzgg().zzir().zzg("Property filter result", valueOf == null ? "null" : valueOf);
                                                if (valueOf == null) {
                                                    hashSet4.add(Integer.valueOf(intValue2));
                                                } else {
                                                    bitSet7.set(com_google_android_gms_internal_measurement_zzkc.zzark.intValue());
                                                    if (!valueOf.booleanValue()) {
                                                        bitSet6.set(com_google_android_gms_internal_measurement_zzkc.zzark.intValue());
                                                    }
                                                }
                                            }
                                            valueOf = zza(valueOf, equals2);
                                            if (valueOf == null) {
                                            }
                                            zzgg().zzir().zzg("Property filter result", valueOf == null ? "null" : valueOf);
                                            if (valueOf == null) {
                                                bitSet7.set(com_google_android_gms_internal_measurement_zzkc.zzark.intValue());
                                                if (!valueOf.booleanValue()) {
                                                    bitSet6.set(com_google_android_gms_internal_measurement_zzkc.zzark.intValue());
                                                }
                                            } else {
                                                hashSet4.add(Integer.valueOf(intValue2));
                                            }
                                        }
                                        zzin2.zzg(str11, zzgb().zzbg(com_google_android_gms_internal_measurement_zzkn.name));
                                        valueOf = null;
                                        if (valueOf == null) {
                                        }
                                        zzgg().zzir().zzg("Property filter result", valueOf == null ? "null" : valueOf);
                                        if (valueOf == null) {
                                            hashSet4.add(Integer.valueOf(intValue2));
                                        } else {
                                            bitSet7.set(com_google_android_gms_internal_measurement_zzkc.zzark.intValue());
                                            if (!valueOf.booleanValue()) {
                                                bitSet6.set(com_google_android_gms_internal_measurement_zzkc.zzark.intValue());
                                            }
                                        }
                                    }
                                    i6 = i12;
                                    map20 = map21;
                                    it2 = it9;
                                    it7 = it8;
                                    arrayMap8 = map8;
                                    map7 = map9;
                                    map22 = map19;
                                }
                            }
                            zzgg().zzin().zze("Invalid property filter ID. appId, id", zzfg.zzbh(str), String.valueOf(com_google_android_gms_internal_measurement_zzkc.zzark));
                            hashSet4.add(Integer.valueOf(intValue2));
                            arrayMap8 = map18;
                            i6 = i12;
                            map20 = map21;
                            it2 = it9;
                            map4 = map8;
                            map5 = map9;
                            map6 = map19;
                        }
                        map21 = map20;
                        map4 = arrayMap8;
                        map5 = map7;
                        map6 = map22;
                        arrayMap8 = map18;
                        com_google_android_gms_internal_measurement_zzknArr3 = com_google_android_gms_internal_measurement_zzknArr;
                    }
                }
                map18 = arrayMap8;
                i12 = i6;
                map8 = map4;
                map19 = map6;
                map9 = map5;
                length++;
                com_google_android_gms_internal_measurement_zzknArr3 = com_google_android_gms_internal_measurement_zzknArr;
            }
        }
        map8 = map4;
        map9 = map5;
        zzba = map6;
        zzkh[] com_google_android_gms_internal_measurement_zzkhArr = new zzkh[zzba.size()];
        i = 0;
        for (Integer intValue3 : zzba.keySet()) {
            length = intValue3.intValue();
            if (!hashSet4.contains(Integer.valueOf(length))) {
                arrayMap4 = map9;
                zzkh com_google_android_gms_internal_measurement_zzkh3 = (zzkh) arrayMap4.get(Integer.valueOf(length));
                if (com_google_android_gms_internal_measurement_zzkh3 == null) {
                    com_google_android_gms_internal_measurement_zzkh3 = new zzkh();
                }
                intValue2 = i + 1;
                com_google_android_gms_internal_measurement_zzkhArr[i] = com_google_android_gms_internal_measurement_zzkh3;
                com_google_android_gms_internal_measurement_zzkh3.zzarg = Integer.valueOf(length);
                com_google_android_gms_internal_measurement_zzkh3.zzasr = new zzkm();
                com_google_android_gms_internal_measurement_zzkh3.zzasr.zzaug = zzjv.zza((BitSet) zzba.get(Integer.valueOf(length)));
                map7 = map8;
                com_google_android_gms_internal_measurement_zzkh3.zzasr.zzauf = zzjv.zza((BitSet) map7.get(Integer.valueOf(length)));
                zzhj zzga2 = zzga();
                zzabj com_google_android_gms_internal_measurement_zzabj = com_google_android_gms_internal_measurement_zzkh3.zzasr;
                zzga2.zzch();
                zzga2.zzab();
                Preconditions.checkNotEmpty(str);
                Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzabj);
                try {
                    byte[] bArr = new byte[com_google_android_gms_internal_measurement_zzabj.zzwg()];
                    zzabb zzb = zzabb.zzb(bArr, 0, bArr.length);
                    com_google_android_gms_internal_measurement_zzabj.zza(zzb);
                    zzb.zzvy();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("app_id", str2);
                    contentValues.put("audience_id", Integer.valueOf(length));
                    contentValues.put("current_results", bArr);
                    try {
                        try {
                            if (zzga2.getWritableDatabase().insertWithOnConflict("audience_filter_values", null, contentValues, 5) == -1) {
                                zzga2.zzgg().zzil().zzg("Failed to insert filter results (got -1). appId", zzfg.zzbh(str));
                            }
                        } catch (SQLiteException e5) {
                            e = e5;
                            obj3 = e;
                            zzil = zzga2.zzgg().zzil();
                            str6 = "Error storing filter results. appId";
                            zzil.zze(str6, zzfg.zzbh(str), obj3);
                            map9 = arrayMap4;
                            i = intValue2;
                            map8 = map7;
                        }
                    } catch (SQLiteException e6) {
                        e = e6;
                        obj3 = e;
                        zzil = zzga2.zzgg().zzil();
                        str6 = "Error storing filter results. appId";
                        zzil.zze(str6, zzfg.zzbh(str), obj3);
                        map9 = arrayMap4;
                        i = intValue2;
                        map8 = map7;
                    }
                } catch (IOException e7) {
                    obj3 = e7;
                    zzil = zzga2.zzgg().zzil();
                    str6 = "Configuration loss. Failed to serialize filter results. appId";
                    zzil.zze(str6, zzfg.zzbh(str), obj3);
                    map9 = arrayMap4;
                    i = intValue2;
                    map8 = map7;
                }
                map9 = arrayMap4;
                i = intValue2;
                map8 = map7;
            }
        }
        return (zzkh[]) Arrays.copyOf(com_google_android_gms_internal_measurement_zzkhArr, i);
    }

    protected final boolean zzhh() {
        return false;
    }
}
