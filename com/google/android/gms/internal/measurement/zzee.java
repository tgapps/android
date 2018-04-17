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
        zzhj zzga;
        String str2;
        ArrayMap arrayMap;
        zzkj[] com_google_android_gms_internal_measurement_zzkjArr;
        SQLiteException e;
        int i2;
        int i3;
        int i4;
        zzkj[] com_google_android_gms_internal_measurement_zzkjArr2;
        ArrayMap arrayMap2;
        int i5;
        zzkj com_google_android_gms_internal_measurement_zzkj;
        String str3;
        Map map3;
        Map map4;
        HashSet hashSet;
        Map map5;
        String str4;
        Map map6;
        BitSet bitSet;
        Map map7;
        BitSet bitSet2;
        Iterator it;
        Map map8;
        Map map9;
        Map map10;
        Boolean zza;
        Boolean valueOf;
        Set hashSet2;
        int i6;
        int length2;
        zzka[] com_google_android_gms_internal_measurement_zzkaArr;
        boolean equals;
        Boolean zza2;
        String str5;
        ArrayMap arrayMap3;
        int intValue2;
        Map map11;
        Map map12;
        Map map13;
        Object obj3;
        zzfi zzil;
        String str6;
        zzee com_google_android_gms_internal_measurement_zzee = this;
        String str7 = str;
        zzki[] com_google_android_gms_internal_measurement_zzkiArr2 = com_google_android_gms_internal_measurement_zzkiArr;
        zzkn[] com_google_android_gms_internal_measurement_zzknArr2 = com_google_android_gms_internal_measurement_zzknArr;
        Preconditions.checkNotEmpty(str);
        Set hashSet3 = new HashSet();
        Map arrayMap4 = new ArrayMap();
        Map arrayMap5 = new ArrayMap();
        Map arrayMap6 = new ArrayMap();
        Map zzba = zzga().zzba(str7);
        if (zzba != null) {
            Iterator it2 = zzba.keySet().iterator();
            while (it2.hasNext()) {
                Iterator it3;
                intValue = ((Integer) it2.next()).intValue();
                zzkm com_google_android_gms_internal_measurement_zzkm = (zzkm) zzba.get(Integer.valueOf(intValue));
                BitSet bitSet3 = (BitSet) arrayMap5.get(Integer.valueOf(intValue));
                BitSet bitSet4 = (BitSet) arrayMap6.get(Integer.valueOf(intValue));
                if (bitSet3 == null) {
                    bitSet3 = new BitSet();
                    arrayMap5.put(Integer.valueOf(intValue), bitSet3);
                    bitSet4 = new BitSet();
                    arrayMap6.put(Integer.valueOf(intValue), bitSet4);
                }
                Map map14 = zzba;
                i = 0;
                while (i < (com_google_android_gms_internal_measurement_zzkm.zzauf.length << 6)) {
                    if (zzjv.zza(com_google_android_gms_internal_measurement_zzkm.zzauf, i)) {
                        it3 = it2;
                        map = arrayMap5;
                        map2 = arrayMap6;
                        zzgg().zzir().zze("Filter already evaluated. audience ID, filter ID", Integer.valueOf(intValue), Integer.valueOf(i));
                        bitSet4.set(i);
                        if (zzjv.zza(com_google_android_gms_internal_measurement_zzkm.zzaug, i)) {
                            bitSet3.set(i);
                        }
                    } else {
                        it3 = it2;
                        map = arrayMap5;
                        map2 = arrayMap6;
                    }
                    i++;
                    it2 = it3;
                    arrayMap5 = map;
                    arrayMap6 = map2;
                }
                it3 = it2;
                map = arrayMap5;
                map2 = arrayMap6;
                zzkh com_google_android_gms_internal_measurement_zzkh = new zzkh();
                arrayMap4.put(Integer.valueOf(intValue), com_google_android_gms_internal_measurement_zzkh);
                com_google_android_gms_internal_measurement_zzkh.zzast = Boolean.valueOf(false);
                com_google_android_gms_internal_measurement_zzkh.zzass = com_google_android_gms_internal_measurement_zzkm;
                com_google_android_gms_internal_measurement_zzkh.zzasr = new zzkm();
                com_google_android_gms_internal_measurement_zzkh.zzasr.zzaug = zzjv.zza(bitSet3);
                com_google_android_gms_internal_measurement_zzkh.zzasr.zzauf = zzjv.zza(bitSet4);
                zzba = map14;
                it2 = it3;
            }
        }
        map = arrayMap5;
        map2 = arrayMap6;
        if (com_google_android_gms_internal_measurement_zzkiArr2 != null) {
            ArrayMap arrayMap7 = new ArrayMap();
            length = com_google_android_gms_internal_measurement_zzkiArr2.length;
            int i7 = 0;
            Long l = null;
            zzki com_google_android_gms_internal_measurement_zzki = null;
            long j = 0;
            while (i7 < length) {
                long j2;
                int i8;
                int length3;
                Long l2;
                long j3;
                zzeq zze;
                zzkj[] com_google_android_gms_internal_measurement_zzkjArr3;
                long j4;
                String str8;
                long j5;
                Map map15;
                Map map16;
                Iterator it4;
                HashSet hashSet4;
                zzkh com_google_android_gms_internal_measurement_zzkh2;
                BitSet bitSet5;
                Map map17;
                zzjz com_google_android_gms_internal_measurement_zzjz;
                Iterator it5;
                Iterator it6;
                long j6;
                zzkj[] com_google_android_gms_internal_measurement_zzkjArr4;
                zzkj com_google_android_gms_internal_measurement_zzkj2;
                zzfi zzin;
                String str9;
                Object zzbe;
                Object zzbf;
                zzka com_google_android_gms_internal_measurement_zzka;
                zzka[] com_google_android_gms_internal_measurement_zzkaArr2;
                int i9;
                boolean z;
                zzki com_google_android_gms_internal_measurement_zzki2 = com_google_android_gms_internal_measurement_zzkiArr2[i7];
                String str10 = com_google_android_gms_internal_measurement_zzki2.name;
                zzkj[] com_google_android_gms_internal_measurement_zzkjArr5 = com_google_android_gms_internal_measurement_zzki2.zzasv;
                if (zzgi().zzd(str7, zzew.zzahy)) {
                    int i10;
                    zzki com_google_android_gms_internal_measurement_zzki3;
                    Long l3;
                    zzki com_google_android_gms_internal_measurement_zzki4;
                    SQLiteDatabase writableDatabase;
                    String[] strArr;
                    int i11;
                    zzkj[] com_google_android_gms_internal_measurement_zzkjArr6;
                    zzkj[] com_google_android_gms_internal_measurement_zzkjArr7;
                    long j7;
                    Pair zza3;
                    zzki com_google_android_gms_internal_measurement_zzki5;
                    Long valueOf2;
                    Long l4;
                    zzgc();
                    Long l5 = (Long) zzjv.zzb(com_google_android_gms_internal_measurement_zzki2, "_eid");
                    obj = l5 != null ? 1 : null;
                    if (obj != null) {
                        i10 = i7;
                        if (str10.equals("_ep")) {
                            obj2 = 1;
                            if (obj2 == null) {
                                zzgc();
                                str10 = (String) zzjv.zzb(com_google_android_gms_internal_measurement_zzki2, "_en");
                                if (!TextUtils.isEmpty(str10)) {
                                    if (!(com_google_android_gms_internal_measurement_zzki == null || l == null)) {
                                        if (l5.longValue() != l.longValue()) {
                                            com_google_android_gms_internal_measurement_zzki3 = com_google_android_gms_internal_measurement_zzki;
                                            l3 = l;
                                            com_google_android_gms_internal_measurement_zzki4 = com_google_android_gms_internal_measurement_zzki3;
                                            j2 = j - 1;
                                            if (j2 > 0) {
                                                zzga = zzga();
                                                zzga.zzab();
                                                zzga.zzgg().zzir().zzg("Clearing complex main event info. appId", str7);
                                                try {
                                                    writableDatabase = zzga.getWritableDatabase();
                                                    str2 = "delete from main_event_params where app_id=?";
                                                    arrayMap = arrayMap7;
                                                    com_google_android_gms_internal_measurement_zzkjArr = com_google_android_gms_internal_measurement_zzkjArr5;
                                                    i8 = 1;
                                                    try {
                                                        strArr = new String[1];
                                                        i11 = 0;
                                                    } catch (SQLiteException e2) {
                                                        e = e2;
                                                        i11 = 0;
                                                        zzga.zzgg().zzil().zzg("Error clearing complex main event", e);
                                                        i2 = length;
                                                        i3 = i8;
                                                        i4 = i10;
                                                        com_google_android_gms_internal_measurement_zzkjArr2 = com_google_android_gms_internal_measurement_zzkjArr;
                                                        arrayMap2 = arrayMap;
                                                        com_google_android_gms_internal_measurement_zzkjArr6 = new zzkj[(com_google_android_gms_internal_measurement_zzki4.zzasv.length + com_google_android_gms_internal_measurement_zzkjArr2.length)];
                                                        com_google_android_gms_internal_measurement_zzkjArr7 = com_google_android_gms_internal_measurement_zzki4.zzasv;
                                                        intValue = com_google_android_gms_internal_measurement_zzkjArr7.length;
                                                        i5 = i11;
                                                        length = i5;
                                                        while (i5 < intValue) {
                                                            com_google_android_gms_internal_measurement_zzkj = com_google_android_gms_internal_measurement_zzkjArr7[i5];
                                                            zzgc();
                                                            if (zzjv.zza(com_google_android_gms_internal_measurement_zzki2, com_google_android_gms_internal_measurement_zzkj.name) != null) {
                                                                i3 = length + 1;
                                                                com_google_android_gms_internal_measurement_zzkjArr6[length] = com_google_android_gms_internal_measurement_zzkj;
                                                                length = i3;
                                                            }
                                                            i5++;
                                                        }
                                                        if (length > 0) {
                                                            zzgg().zzin().zzg("No unique parameters in main event. eventName", str10);
                                                        } else {
                                                            length3 = com_google_android_gms_internal_measurement_zzkjArr2.length;
                                                            intValue = i11;
                                                            while (intValue < length3) {
                                                                i8 = length + 1;
                                                                com_google_android_gms_internal_measurement_zzkjArr6[length] = com_google_android_gms_internal_measurement_zzkjArr2[intValue];
                                                                intValue++;
                                                                length = i8;
                                                            }
                                                            if (length != com_google_android_gms_internal_measurement_zzkjArr6.length) {
                                                                com_google_android_gms_internal_measurement_zzkjArr6 = (zzkj[]) Arrays.copyOf(com_google_android_gms_internal_measurement_zzkjArr6, length);
                                                            }
                                                            com_google_android_gms_internal_measurement_zzkjArr2 = com_google_android_gms_internal_measurement_zzkjArr6;
                                                        }
                                                        str3 = str10;
                                                        l2 = l3;
                                                        j3 = 0;
                                                        com_google_android_gms_internal_measurement_zzki = com_google_android_gms_internal_measurement_zzki4;
                                                        zze = zzga().zze(str7, com_google_android_gms_internal_measurement_zzki2.name);
                                                        if (zze != null) {
                                                            zzgg().zzin().zze("Event aggregate wasn't created during raw event logging. appId, event", zzfg.zzbh(str), zzgb().zzbe(str3));
                                                            map3 = map;
                                                            map4 = map2;
                                                            hashSet = hashSet3;
                                                            map5 = arrayMap4;
                                                            com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr2;
                                                            j4 = j3;
                                                            str8 = str3;
                                                            zze = new zzeq(str7, com_google_android_gms_internal_measurement_zzki2.name, 1, 1, com_google_android_gms_internal_measurement_zzki2.zzasw.longValue(), 0, null, null, null);
                                                        } else {
                                                            hashSet = hashSet3;
                                                            map5 = arrayMap4;
                                                            com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr2;
                                                            str8 = str3;
                                                            map3 = map;
                                                            map4 = map2;
                                                            j4 = j3;
                                                            zze = zze.zzie();
                                                        }
                                                        zzga().zza(zze);
                                                        j5 = zze.zzafp;
                                                        map15 = arrayMap2;
                                                        str10 = str8;
                                                        map16 = (Map) map15.get(str10);
                                                        if (map16 != null) {
                                                            str4 = str;
                                                            map16 = zzga().zzj(str4, str10);
                                                            if (map16 == null) {
                                                                map16 = new ArrayMap();
                                                            }
                                                            map15.put(str10, map16);
                                                        } else {
                                                            str4 = str;
                                                        }
                                                        it4 = map16.keySet().iterator();
                                                        while (it4.hasNext()) {
                                                            i = ((Integer) it4.next()).intValue();
                                                            hashSet4 = hashSet;
                                                            if (hashSet4.contains(Integer.valueOf(i))) {
                                                                arrayMap4 = map5;
                                                                map6 = map3;
                                                                bitSet = (BitSet) map6.get(Integer.valueOf(i));
                                                                map7 = map15;
                                                                map15 = map4;
                                                                bitSet2 = (BitSet) map15.get(Integer.valueOf(i));
                                                                if (((zzkh) arrayMap4.get(Integer.valueOf(i))) == null) {
                                                                    com_google_android_gms_internal_measurement_zzkh2 = new zzkh();
                                                                    arrayMap4.put(Integer.valueOf(i), com_google_android_gms_internal_measurement_zzkh2);
                                                                    com_google_android_gms_internal_measurement_zzkh2.zzast = Boolean.valueOf(true);
                                                                    bitSet5 = new BitSet();
                                                                    map6.put(Integer.valueOf(i), bitSet5);
                                                                    bitSet2 = new BitSet();
                                                                    map15.put(Integer.valueOf(i), bitSet2);
                                                                    bitSet = bitSet5;
                                                                }
                                                                it = ((List) map16.get(Integer.valueOf(i))).iterator();
                                                                while (it.hasNext()) {
                                                                    map17 = map16;
                                                                    com_google_android_gms_internal_measurement_zzjz = (zzjz) it.next();
                                                                    it5 = it4;
                                                                    it6 = it;
                                                                    if (zzgg().isLoggable(2)) {
                                                                        map8 = map15;
                                                                        map9 = arrayMap4;
                                                                        map10 = map6;
                                                                    } else {
                                                                        map8 = map15;
                                                                        map10 = map6;
                                                                        map9 = arrayMap4;
                                                                        zzgg().zzir().zzd("Evaluating filter. audience, filter, event", Integer.valueOf(i), com_google_android_gms_internal_measurement_zzjz.zzark, zzgb().zzbe(com_google_android_gms_internal_measurement_zzjz.zzarl));
                                                                        zzgg().zzir().zzg("Filter definition", zzgb().zza(com_google_android_gms_internal_measurement_zzjz));
                                                                    }
                                                                    if (com_google_android_gms_internal_measurement_zzjz.zzark != null) {
                                                                        if (com_google_android_gms_internal_measurement_zzjz.zzark.intValue() > 256) {
                                                                            if (bitSet.get(com_google_android_gms_internal_measurement_zzjz.zzark.intValue())) {
                                                                                if (com_google_android_gms_internal_measurement_zzjz.zzaro != null) {
                                                                                    zza = zza(j5, com_google_android_gms_internal_measurement_zzjz.zzaro);
                                                                                    if (zza != null) {
                                                                                        if (!zza.booleanValue()) {
                                                                                            j6 = j5;
                                                                                            valueOf = Boolean.valueOf(false);
                                                                                            com_google_android_gms_internal_measurement_zzkjArr4 = com_google_android_gms_internal_measurement_zzkjArr3;
                                                                                            zzgg().zzir().zzg("Event filter result", valueOf != null ? "null" : valueOf);
                                                                                            if (valueOf != null) {
                                                                                                hashSet4.add(Integer.valueOf(i));
                                                                                            } else {
                                                                                                bitSet2.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                                                                if (valueOf.booleanValue()) {
                                                                                                    bitSet.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                                                                }
                                                                                            }
                                                                                            com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr4;
                                                                                            map16 = map17;
                                                                                            it4 = it5;
                                                                                            it = it6;
                                                                                            map15 = map8;
                                                                                            map6 = map10;
                                                                                            arrayMap4 = map9;
                                                                                            j5 = j6;
                                                                                        }
                                                                                    }
                                                                                    j6 = j5;
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
                                                                                        hashSet4.add(Integer.valueOf(i));
                                                                                    }
                                                                                    com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr4;
                                                                                    map16 = map17;
                                                                                    it4 = it5;
                                                                                    it = it6;
                                                                                    map15 = map8;
                                                                                    map6 = map10;
                                                                                    arrayMap4 = map9;
                                                                                    j5 = j6;
                                                                                }
                                                                                hashSet2 = new HashSet();
                                                                                for (zzka com_google_android_gms_internal_measurement_zzka2 : com_google_android_gms_internal_measurement_zzjz.zzarm) {
                                                                                    if (TextUtils.isEmpty(com_google_android_gms_internal_measurement_zzka2.zzart)) {
                                                                                        zzgg().zzin().zzg("null or empty param name in filter. event", zzgb().zzbe(str10));
                                                                                        j6 = j5;
                                                                                        com_google_android_gms_internal_measurement_zzkjArr4 = com_google_android_gms_internal_measurement_zzkjArr3;
                                                                                        break;
                                                                                    }
                                                                                    hashSet2.add(com_google_android_gms_internal_measurement_zzka2.zzart);
                                                                                }
                                                                                map15 = new ArrayMap();
                                                                                com_google_android_gms_internal_measurement_zzkjArr4 = com_google_android_gms_internal_measurement_zzkjArr3;
                                                                                length2 = com_google_android_gms_internal_measurement_zzkjArr4.length;
                                                                                i6 = 0;
                                                                                while (i6 < length2) {
                                                                                    com_google_android_gms_internal_measurement_zzkj2 = com_google_android_gms_internal_measurement_zzkjArr4[i6];
                                                                                    j6 = j5;
                                                                                    if (!hashSet2.contains(com_google_android_gms_internal_measurement_zzkj2.name)) {
                                                                                        if (com_google_android_gms_internal_measurement_zzkj2.zzasz == null) {
                                                                                            if (com_google_android_gms_internal_measurement_zzkj2.zzaqx == null) {
                                                                                                if (com_google_android_gms_internal_measurement_zzkj2.zzajf != null) {
                                                                                                    zzin = zzgg().zzin();
                                                                                                    str9 = "Unknown value for param. event, param";
                                                                                                    zzbe = zzgb().zzbe(str10);
                                                                                                    zzbf = zzgb().zzbf(com_google_android_gms_internal_measurement_zzkj2.name);
                                                                                                    break;
                                                                                                }
                                                                                                obj2 = com_google_android_gms_internal_measurement_zzkj2.name;
                                                                                                obj = com_google_android_gms_internal_measurement_zzkj2.zzajf;
                                                                                            } else {
                                                                                                obj2 = com_google_android_gms_internal_measurement_zzkj2.name;
                                                                                                obj = com_google_android_gms_internal_measurement_zzkj2.zzaqx;
                                                                                            }
                                                                                        } else {
                                                                                            obj2 = com_google_android_gms_internal_measurement_zzkj2.name;
                                                                                            obj = com_google_android_gms_internal_measurement_zzkj2.zzasz;
                                                                                        }
                                                                                        map15.put(obj2, obj);
                                                                                    }
                                                                                    i6++;
                                                                                    j5 = j6;
                                                                                }
                                                                                j6 = j5;
                                                                                com_google_android_gms_internal_measurement_zzkaArr = com_google_android_gms_internal_measurement_zzjz.zzarm;
                                                                                length3 = com_google_android_gms_internal_measurement_zzkaArr.length;
                                                                                i8 = 0;
                                                                                while (i8 < length3) {
                                                                                    com_google_android_gms_internal_measurement_zzka = com_google_android_gms_internal_measurement_zzkaArr[i8];
                                                                                    equals = Boolean.TRUE.equals(com_google_android_gms_internal_measurement_zzka.zzars);
                                                                                    str3 = com_google_android_gms_internal_measurement_zzka.zzart;
                                                                                    if (TextUtils.isEmpty(str3)) {
                                                                                        com_google_android_gms_internal_measurement_zzkaArr2 = com_google_android_gms_internal_measurement_zzkaArr;
                                                                                        obj2 = map15.get(str3);
                                                                                        i9 = length3;
                                                                                        if (obj2 instanceof Long) {
                                                                                            if (obj2 instanceof Double) {
                                                                                                if (!(obj2 instanceof String)) {
                                                                                                    if (com_google_android_gms_internal_measurement_zzka.zzarq != null) {
                                                                                                        zza2 = zza((String) obj2, com_google_android_gms_internal_measurement_zzka.zzarq);
                                                                                                    } else if (com_google_android_gms_internal_measurement_zzka.zzarr == null) {
                                                                                                        str5 = (String) obj2;
                                                                                                        if (zzjv.zzcd(str5)) {
                                                                                                            zzin = zzgg().zzin();
                                                                                                            str9 = "Invalid param value for number filter. event, param";
                                                                                                        } else {
                                                                                                            zza2 = zza(str5, com_google_android_gms_internal_measurement_zzka.zzarr);
                                                                                                        }
                                                                                                    } else {
                                                                                                        zzin = zzgg().zzin();
                                                                                                        str9 = "No filter for String param. event, param";
                                                                                                    }
                                                                                                    if (zza2 == null) {
                                                                                                        if (((zza2.booleanValue() ^ 1) ^ equals) != 0) {
                                                                                                            z = false;
                                                                                                            break;
                                                                                                        }
                                                                                                        i8++;
                                                                                                        com_google_android_gms_internal_measurement_zzkaArr = com_google_android_gms_internal_measurement_zzkaArr2;
                                                                                                        length3 = i9;
                                                                                                    }
                                                                                                } else if (obj2 == null) {
                                                                                                    zzgg().zzir().zze("Missing param for filter. event, param", zzgb().zzbe(str10), zzgb().zzbf(str3));
                                                                                                    z = false;
                                                                                                    break;
                                                                                                } else {
                                                                                                    zzin = zzgg().zzin();
                                                                                                    str9 = "Unknown param type. event, param";
                                                                                                }
                                                                                            } else if (com_google_android_gms_internal_measurement_zzka.zzarr != null) {
                                                                                                zzin = zzgg().zzin();
                                                                                                str9 = "No number filter for double param. event, param";
                                                                                            } else {
                                                                                                zza2 = zza(((Double) obj2).doubleValue(), com_google_android_gms_internal_measurement_zzka.zzarr);
                                                                                                if (zza2 == null) {
                                                                                                    if (((zza2.booleanValue() ^ 1) ^ equals) != 0) {
                                                                                                        z = false;
                                                                                                        break;
                                                                                                    }
                                                                                                    i8++;
                                                                                                    com_google_android_gms_internal_measurement_zzkaArr = com_google_android_gms_internal_measurement_zzkaArr2;
                                                                                                    length3 = i9;
                                                                                                }
                                                                                            }
                                                                                        } else if (com_google_android_gms_internal_measurement_zzka.zzarr != null) {
                                                                                            zzin = zzgg().zzin();
                                                                                            str9 = "No number filter for long param. event, param";
                                                                                        } else {
                                                                                            zza2 = zza(((Long) obj2).longValue(), com_google_android_gms_internal_measurement_zzka.zzarr);
                                                                                            if (zza2 == null) {
                                                                                                if (((zza2.booleanValue() ^ 1) ^ equals) != 0) {
                                                                                                    z = false;
                                                                                                    break;
                                                                                                }
                                                                                                i8++;
                                                                                                com_google_android_gms_internal_measurement_zzkaArr = com_google_android_gms_internal_measurement_zzkaArr2;
                                                                                                length3 = i9;
                                                                                            }
                                                                                        }
                                                                                        zzbe = zzgb().zzbe(str10);
                                                                                        zzbf = zzgb().zzbf(str3);
                                                                                        zzin.zze(str9, zzbe, zzbf);
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
                                                                                    hashSet4.add(Integer.valueOf(i));
                                                                                } else {
                                                                                    bitSet2.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                                                    if (valueOf.booleanValue()) {
                                                                                        bitSet.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                                                    }
                                                                                }
                                                                                com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr4;
                                                                                map16 = map17;
                                                                                it4 = it5;
                                                                                it = it6;
                                                                                map15 = map8;
                                                                                map6 = map10;
                                                                                arrayMap4 = map9;
                                                                                j5 = j6;
                                                                            } else {
                                                                                zzgg().zzir().zze("Event filter already evaluated true. audience ID, filter ID", Integer.valueOf(i), com_google_android_gms_internal_measurement_zzjz.zzark);
                                                                                map16 = map17;
                                                                                it4 = it5;
                                                                                it = it6;
                                                                                map15 = map8;
                                                                                map6 = map10;
                                                                                arrayMap4 = map9;
                                                                            }
                                                                            str4 = str;
                                                                        }
                                                                    }
                                                                    j6 = j5;
                                                                    com_google_android_gms_internal_measurement_zzkjArr4 = com_google_android_gms_internal_measurement_zzkjArr3;
                                                                    str2 = str;
                                                                    zzgg().zzin().zze("Invalid event filter ID. appId, id", zzfg.zzbh(str), String.valueOf(com_google_android_gms_internal_measurement_zzjz.zzark));
                                                                    com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr4;
                                                                    map16 = map17;
                                                                    it4 = it5;
                                                                    it = it6;
                                                                    map6 = map10;
                                                                    arrayMap4 = map9;
                                                                    j5 = j6;
                                                                    str4 = str2;
                                                                    map15 = map8;
                                                                }
                                                                map8 = map15;
                                                                str2 = str4;
                                                                hashSet = hashSet4;
                                                                map5 = arrayMap4;
                                                                map3 = map6;
                                                                map15 = map7;
                                                                map4 = map8;
                                                            } else {
                                                                zzgg().zzir().zzg("Skipping failed audience ID", Integer.valueOf(i));
                                                                hashSet = hashSet4;
                                                            }
                                                        }
                                                        arrayMap3 = map15;
                                                        str2 = str4;
                                                        map10 = map3;
                                                        map8 = map4;
                                                        hashSet3 = hashSet;
                                                        map9 = map5;
                                                        l = l2;
                                                        j = j2;
                                                        i7 = i4 + 1;
                                                        com_google_android_gms_internal_measurement_zzkiArr2 = com_google_android_gms_internal_measurement_zzkiArr;
                                                        str7 = str2;
                                                        length = i2;
                                                        arrayMap7 = arrayMap3;
                                                        map2 = map8;
                                                        map = map10;
                                                        arrayMap4 = map9;
                                                        com_google_android_gms_internal_measurement_zzknArr2 = com_google_android_gms_internal_measurement_zzknArr;
                                                    }
                                                    try {
                                                        strArr[0] = str7;
                                                        writableDatabase.execSQL(str2, strArr);
                                                    } catch (SQLiteException e3) {
                                                        e = e3;
                                                        zzga.zzgg().zzil().zzg("Error clearing complex main event", e);
                                                        i2 = length;
                                                        i3 = i8;
                                                        i4 = i10;
                                                        com_google_android_gms_internal_measurement_zzkjArr2 = com_google_android_gms_internal_measurement_zzkjArr;
                                                        arrayMap2 = arrayMap;
                                                        com_google_android_gms_internal_measurement_zzkjArr6 = new zzkj[(com_google_android_gms_internal_measurement_zzki4.zzasv.length + com_google_android_gms_internal_measurement_zzkjArr2.length)];
                                                        com_google_android_gms_internal_measurement_zzkjArr7 = com_google_android_gms_internal_measurement_zzki4.zzasv;
                                                        intValue = com_google_android_gms_internal_measurement_zzkjArr7.length;
                                                        i5 = i11;
                                                        length = i5;
                                                        while (i5 < intValue) {
                                                            com_google_android_gms_internal_measurement_zzkj = com_google_android_gms_internal_measurement_zzkjArr7[i5];
                                                            zzgc();
                                                            if (zzjv.zza(com_google_android_gms_internal_measurement_zzki2, com_google_android_gms_internal_measurement_zzkj.name) != null) {
                                                                i3 = length + 1;
                                                                com_google_android_gms_internal_measurement_zzkjArr6[length] = com_google_android_gms_internal_measurement_zzkj;
                                                                length = i3;
                                                            }
                                                            i5++;
                                                        }
                                                        if (length > 0) {
                                                            length3 = com_google_android_gms_internal_measurement_zzkjArr2.length;
                                                            intValue = i11;
                                                            while (intValue < length3) {
                                                                i8 = length + 1;
                                                                com_google_android_gms_internal_measurement_zzkjArr6[length] = com_google_android_gms_internal_measurement_zzkjArr2[intValue];
                                                                intValue++;
                                                                length = i8;
                                                            }
                                                            if (length != com_google_android_gms_internal_measurement_zzkjArr6.length) {
                                                                com_google_android_gms_internal_measurement_zzkjArr6 = (zzkj[]) Arrays.copyOf(com_google_android_gms_internal_measurement_zzkjArr6, length);
                                                            }
                                                            com_google_android_gms_internal_measurement_zzkjArr2 = com_google_android_gms_internal_measurement_zzkjArr6;
                                                        } else {
                                                            zzgg().zzin().zzg("No unique parameters in main event. eventName", str10);
                                                        }
                                                        str3 = str10;
                                                        l2 = l3;
                                                        j3 = 0;
                                                        com_google_android_gms_internal_measurement_zzki = com_google_android_gms_internal_measurement_zzki4;
                                                        zze = zzga().zze(str7, com_google_android_gms_internal_measurement_zzki2.name);
                                                        if (zze != null) {
                                                            hashSet = hashSet3;
                                                            map5 = arrayMap4;
                                                            com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr2;
                                                            str8 = str3;
                                                            map3 = map;
                                                            map4 = map2;
                                                            j4 = j3;
                                                            zze = zze.zzie();
                                                        } else {
                                                            zzgg().zzin().zze("Event aggregate wasn't created during raw event logging. appId, event", zzfg.zzbh(str), zzgb().zzbe(str3));
                                                            map3 = map;
                                                            map4 = map2;
                                                            hashSet = hashSet3;
                                                            map5 = arrayMap4;
                                                            com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr2;
                                                            j4 = j3;
                                                            str8 = str3;
                                                            zze = new zzeq(str7, com_google_android_gms_internal_measurement_zzki2.name, 1, 1, com_google_android_gms_internal_measurement_zzki2.zzasw.longValue(), 0, null, null, null);
                                                        }
                                                        zzga().zza(zze);
                                                        j5 = zze.zzafp;
                                                        map15 = arrayMap2;
                                                        str10 = str8;
                                                        map16 = (Map) map15.get(str10);
                                                        if (map16 != null) {
                                                            str4 = str;
                                                        } else {
                                                            str4 = str;
                                                            map16 = zzga().zzj(str4, str10);
                                                            if (map16 == null) {
                                                                map16 = new ArrayMap();
                                                            }
                                                            map15.put(str10, map16);
                                                        }
                                                        it4 = map16.keySet().iterator();
                                                        while (it4.hasNext()) {
                                                            i = ((Integer) it4.next()).intValue();
                                                            hashSet4 = hashSet;
                                                            if (hashSet4.contains(Integer.valueOf(i))) {
                                                                arrayMap4 = map5;
                                                                map6 = map3;
                                                                bitSet = (BitSet) map6.get(Integer.valueOf(i));
                                                                map7 = map15;
                                                                map15 = map4;
                                                                bitSet2 = (BitSet) map15.get(Integer.valueOf(i));
                                                                if (((zzkh) arrayMap4.get(Integer.valueOf(i))) == null) {
                                                                    com_google_android_gms_internal_measurement_zzkh2 = new zzkh();
                                                                    arrayMap4.put(Integer.valueOf(i), com_google_android_gms_internal_measurement_zzkh2);
                                                                    com_google_android_gms_internal_measurement_zzkh2.zzast = Boolean.valueOf(true);
                                                                    bitSet5 = new BitSet();
                                                                    map6.put(Integer.valueOf(i), bitSet5);
                                                                    bitSet2 = new BitSet();
                                                                    map15.put(Integer.valueOf(i), bitSet2);
                                                                    bitSet = bitSet5;
                                                                }
                                                                it = ((List) map16.get(Integer.valueOf(i))).iterator();
                                                                while (it.hasNext()) {
                                                                    map17 = map16;
                                                                    com_google_android_gms_internal_measurement_zzjz = (zzjz) it.next();
                                                                    it5 = it4;
                                                                    it6 = it;
                                                                    if (zzgg().isLoggable(2)) {
                                                                        map8 = map15;
                                                                        map9 = arrayMap4;
                                                                        map10 = map6;
                                                                    } else {
                                                                        map8 = map15;
                                                                        map10 = map6;
                                                                        map9 = arrayMap4;
                                                                        zzgg().zzir().zzd("Evaluating filter. audience, filter, event", Integer.valueOf(i), com_google_android_gms_internal_measurement_zzjz.zzark, zzgb().zzbe(com_google_android_gms_internal_measurement_zzjz.zzarl));
                                                                        zzgg().zzir().zzg("Filter definition", zzgb().zza(com_google_android_gms_internal_measurement_zzjz));
                                                                    }
                                                                    if (com_google_android_gms_internal_measurement_zzjz.zzark != null) {
                                                                        if (com_google_android_gms_internal_measurement_zzjz.zzark.intValue() > 256) {
                                                                            if (bitSet.get(com_google_android_gms_internal_measurement_zzjz.zzark.intValue())) {
                                                                                if (com_google_android_gms_internal_measurement_zzjz.zzaro != null) {
                                                                                    zza = zza(j5, com_google_android_gms_internal_measurement_zzjz.zzaro);
                                                                                    if (zza != null) {
                                                                                        if (zza.booleanValue()) {
                                                                                            j6 = j5;
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
                                                                                                hashSet4.add(Integer.valueOf(i));
                                                                                            }
                                                                                            com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr4;
                                                                                            map16 = map17;
                                                                                            it4 = it5;
                                                                                            it = it6;
                                                                                            map15 = map8;
                                                                                            map6 = map10;
                                                                                            arrayMap4 = map9;
                                                                                            j5 = j6;
                                                                                        }
                                                                                    }
                                                                                    j6 = j5;
                                                                                    com_google_android_gms_internal_measurement_zzkjArr4 = com_google_android_gms_internal_measurement_zzkjArr3;
                                                                                    valueOf = null;
                                                                                    if (valueOf != null) {
                                                                                    }
                                                                                    zzgg().zzir().zzg("Event filter result", valueOf != null ? "null" : valueOf);
                                                                                    if (valueOf != null) {
                                                                                        hashSet4.add(Integer.valueOf(i));
                                                                                    } else {
                                                                                        bitSet2.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                                                        if (valueOf.booleanValue()) {
                                                                                            bitSet.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                                                        }
                                                                                    }
                                                                                    com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr4;
                                                                                    map16 = map17;
                                                                                    it4 = it5;
                                                                                    it = it6;
                                                                                    map15 = map8;
                                                                                    map6 = map10;
                                                                                    arrayMap4 = map9;
                                                                                    j5 = j6;
                                                                                }
                                                                                hashSet2 = new HashSet();
                                                                                for (i3 = 0; i3 < i6; i3++) {
                                                                                    if (TextUtils.isEmpty(com_google_android_gms_internal_measurement_zzka2.zzart)) {
                                                                                        zzgg().zzin().zzg("null or empty param name in filter. event", zzgb().zzbe(str10));
                                                                                        j6 = j5;
                                                                                        com_google_android_gms_internal_measurement_zzkjArr4 = com_google_android_gms_internal_measurement_zzkjArr3;
                                                                                        break;
                                                                                    }
                                                                                    hashSet2.add(com_google_android_gms_internal_measurement_zzka2.zzart);
                                                                                }
                                                                                map15 = new ArrayMap();
                                                                                com_google_android_gms_internal_measurement_zzkjArr4 = com_google_android_gms_internal_measurement_zzkjArr3;
                                                                                length2 = com_google_android_gms_internal_measurement_zzkjArr4.length;
                                                                                i6 = 0;
                                                                                while (i6 < length2) {
                                                                                    com_google_android_gms_internal_measurement_zzkj2 = com_google_android_gms_internal_measurement_zzkjArr4[i6];
                                                                                    j6 = j5;
                                                                                    if (!hashSet2.contains(com_google_android_gms_internal_measurement_zzkj2.name)) {
                                                                                        if (com_google_android_gms_internal_measurement_zzkj2.zzasz == null) {
                                                                                            obj2 = com_google_android_gms_internal_measurement_zzkj2.name;
                                                                                            obj = com_google_android_gms_internal_measurement_zzkj2.zzasz;
                                                                                        } else if (com_google_android_gms_internal_measurement_zzkj2.zzaqx == null) {
                                                                                            obj2 = com_google_android_gms_internal_measurement_zzkj2.name;
                                                                                            obj = com_google_android_gms_internal_measurement_zzkj2.zzaqx;
                                                                                        } else if (com_google_android_gms_internal_measurement_zzkj2.zzajf != null) {
                                                                                            zzin = zzgg().zzin();
                                                                                            str9 = "Unknown value for param. event, param";
                                                                                            zzbe = zzgb().zzbe(str10);
                                                                                            zzbf = zzgb().zzbf(com_google_android_gms_internal_measurement_zzkj2.name);
                                                                                            break;
                                                                                        } else {
                                                                                            obj2 = com_google_android_gms_internal_measurement_zzkj2.name;
                                                                                            obj = com_google_android_gms_internal_measurement_zzkj2.zzajf;
                                                                                        }
                                                                                        map15.put(obj2, obj);
                                                                                    }
                                                                                    i6++;
                                                                                    j5 = j6;
                                                                                }
                                                                                j6 = j5;
                                                                                com_google_android_gms_internal_measurement_zzkaArr = com_google_android_gms_internal_measurement_zzjz.zzarm;
                                                                                length3 = com_google_android_gms_internal_measurement_zzkaArr.length;
                                                                                i8 = 0;
                                                                                while (i8 < length3) {
                                                                                    com_google_android_gms_internal_measurement_zzka = com_google_android_gms_internal_measurement_zzkaArr[i8];
                                                                                    equals = Boolean.TRUE.equals(com_google_android_gms_internal_measurement_zzka.zzars);
                                                                                    str3 = com_google_android_gms_internal_measurement_zzka.zzart;
                                                                                    if (TextUtils.isEmpty(str3)) {
                                                                                        com_google_android_gms_internal_measurement_zzkaArr2 = com_google_android_gms_internal_measurement_zzkaArr;
                                                                                        obj2 = map15.get(str3);
                                                                                        i9 = length3;
                                                                                        if (obj2 instanceof Long) {
                                                                                            if (obj2 instanceof Double) {
                                                                                                if (!(obj2 instanceof String)) {
                                                                                                    if (obj2 == null) {
                                                                                                        zzgg().zzir().zze("Missing param for filter. event, param", zzgb().zzbe(str10), zzgb().zzbf(str3));
                                                                                                        z = false;
                                                                                                        break;
                                                                                                    }
                                                                                                    zzin = zzgg().zzin();
                                                                                                    str9 = "Unknown param type. event, param";
                                                                                                } else {
                                                                                                    if (com_google_android_gms_internal_measurement_zzka.zzarq != null) {
                                                                                                        zza2 = zza((String) obj2, com_google_android_gms_internal_measurement_zzka.zzarq);
                                                                                                    } else if (com_google_android_gms_internal_measurement_zzka.zzarr == null) {
                                                                                                        zzin = zzgg().zzin();
                                                                                                        str9 = "No filter for String param. event, param";
                                                                                                    } else {
                                                                                                        str5 = (String) obj2;
                                                                                                        if (zzjv.zzcd(str5)) {
                                                                                                            zzin = zzgg().zzin();
                                                                                                            str9 = "Invalid param value for number filter. event, param";
                                                                                                        } else {
                                                                                                            zza2 = zza(str5, com_google_android_gms_internal_measurement_zzka.zzarr);
                                                                                                        }
                                                                                                    }
                                                                                                    if (zza2 == null) {
                                                                                                        if (((zza2.booleanValue() ^ 1) ^ equals) != 0) {
                                                                                                            z = false;
                                                                                                            break;
                                                                                                        }
                                                                                                        i8++;
                                                                                                        com_google_android_gms_internal_measurement_zzkaArr = com_google_android_gms_internal_measurement_zzkaArr2;
                                                                                                        length3 = i9;
                                                                                                    }
                                                                                                }
                                                                                            } else if (com_google_android_gms_internal_measurement_zzka.zzarr != null) {
                                                                                                zza2 = zza(((Double) obj2).doubleValue(), com_google_android_gms_internal_measurement_zzka.zzarr);
                                                                                                if (zza2 == null) {
                                                                                                    if (((zza2.booleanValue() ^ 1) ^ equals) != 0) {
                                                                                                        z = false;
                                                                                                        break;
                                                                                                    }
                                                                                                    i8++;
                                                                                                    com_google_android_gms_internal_measurement_zzkaArr = com_google_android_gms_internal_measurement_zzkaArr2;
                                                                                                    length3 = i9;
                                                                                                }
                                                                                            } else {
                                                                                                zzin = zzgg().zzin();
                                                                                                str9 = "No number filter for double param. event, param";
                                                                                            }
                                                                                        } else if (com_google_android_gms_internal_measurement_zzka.zzarr != null) {
                                                                                            zza2 = zza(((Long) obj2).longValue(), com_google_android_gms_internal_measurement_zzka.zzarr);
                                                                                            if (zza2 == null) {
                                                                                                if (((zza2.booleanValue() ^ 1) ^ equals) != 0) {
                                                                                                    z = false;
                                                                                                    break;
                                                                                                }
                                                                                                i8++;
                                                                                                com_google_android_gms_internal_measurement_zzkaArr = com_google_android_gms_internal_measurement_zzkaArr2;
                                                                                                length3 = i9;
                                                                                            }
                                                                                        } else {
                                                                                            zzin = zzgg().zzin();
                                                                                            str9 = "No number filter for long param. event, param";
                                                                                        }
                                                                                        zzbe = zzgb().zzbe(str10);
                                                                                        zzbf = zzgb().zzbf(str3);
                                                                                        zzin.zze(str9, zzbe, zzbf);
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
                                                                                    hashSet4.add(Integer.valueOf(i));
                                                                                }
                                                                                com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr4;
                                                                                map16 = map17;
                                                                                it4 = it5;
                                                                                it = it6;
                                                                                map15 = map8;
                                                                                map6 = map10;
                                                                                arrayMap4 = map9;
                                                                                j5 = j6;
                                                                            } else {
                                                                                zzgg().zzir().zze("Event filter already evaluated true. audience ID, filter ID", Integer.valueOf(i), com_google_android_gms_internal_measurement_zzjz.zzark);
                                                                                map16 = map17;
                                                                                it4 = it5;
                                                                                it = it6;
                                                                                map15 = map8;
                                                                                map6 = map10;
                                                                                arrayMap4 = map9;
                                                                            }
                                                                            str4 = str;
                                                                        }
                                                                    }
                                                                    j6 = j5;
                                                                    com_google_android_gms_internal_measurement_zzkjArr4 = com_google_android_gms_internal_measurement_zzkjArr3;
                                                                    str2 = str;
                                                                    zzgg().zzin().zze("Invalid event filter ID. appId, id", zzfg.zzbh(str), String.valueOf(com_google_android_gms_internal_measurement_zzjz.zzark));
                                                                    com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr4;
                                                                    map16 = map17;
                                                                    it4 = it5;
                                                                    it = it6;
                                                                    map6 = map10;
                                                                    arrayMap4 = map9;
                                                                    j5 = j6;
                                                                    str4 = str2;
                                                                    map15 = map8;
                                                                }
                                                                map8 = map15;
                                                                str2 = str4;
                                                                hashSet = hashSet4;
                                                                map5 = arrayMap4;
                                                                map3 = map6;
                                                                map15 = map7;
                                                                map4 = map8;
                                                            } else {
                                                                zzgg().zzir().zzg("Skipping failed audience ID", Integer.valueOf(i));
                                                                hashSet = hashSet4;
                                                            }
                                                        }
                                                        arrayMap3 = map15;
                                                        str2 = str4;
                                                        map10 = map3;
                                                        map8 = map4;
                                                        hashSet3 = hashSet;
                                                        map9 = map5;
                                                        l = l2;
                                                        j = j2;
                                                        i7 = i4 + 1;
                                                        com_google_android_gms_internal_measurement_zzkiArr2 = com_google_android_gms_internal_measurement_zzkiArr;
                                                        str7 = str2;
                                                        length = i2;
                                                        arrayMap7 = arrayMap3;
                                                        map2 = map8;
                                                        map = map10;
                                                        arrayMap4 = map9;
                                                        com_google_android_gms_internal_measurement_zzknArr2 = com_google_android_gms_internal_measurement_zzknArr;
                                                    }
                                                } catch (SQLiteException e4) {
                                                    e = e4;
                                                    arrayMap = arrayMap7;
                                                    com_google_android_gms_internal_measurement_zzkjArr = com_google_android_gms_internal_measurement_zzkjArr5;
                                                    i8 = 1;
                                                    i11 = 0;
                                                    zzga.zzgg().zzil().zzg("Error clearing complex main event", e);
                                                    i2 = length;
                                                    i3 = i8;
                                                    i4 = i10;
                                                    com_google_android_gms_internal_measurement_zzkjArr2 = com_google_android_gms_internal_measurement_zzkjArr;
                                                    arrayMap2 = arrayMap;
                                                    com_google_android_gms_internal_measurement_zzkjArr6 = new zzkj[(com_google_android_gms_internal_measurement_zzki4.zzasv.length + com_google_android_gms_internal_measurement_zzkjArr2.length)];
                                                    com_google_android_gms_internal_measurement_zzkjArr7 = com_google_android_gms_internal_measurement_zzki4.zzasv;
                                                    intValue = com_google_android_gms_internal_measurement_zzkjArr7.length;
                                                    i5 = i11;
                                                    length = i5;
                                                    while (i5 < intValue) {
                                                        com_google_android_gms_internal_measurement_zzkj = com_google_android_gms_internal_measurement_zzkjArr7[i5];
                                                        zzgc();
                                                        if (zzjv.zza(com_google_android_gms_internal_measurement_zzki2, com_google_android_gms_internal_measurement_zzkj.name) != null) {
                                                            i3 = length + 1;
                                                            com_google_android_gms_internal_measurement_zzkjArr6[length] = com_google_android_gms_internal_measurement_zzkj;
                                                            length = i3;
                                                        }
                                                        i5++;
                                                    }
                                                    if (length > 0) {
                                                        length3 = com_google_android_gms_internal_measurement_zzkjArr2.length;
                                                        intValue = i11;
                                                        while (intValue < length3) {
                                                            i8 = length + 1;
                                                            com_google_android_gms_internal_measurement_zzkjArr6[length] = com_google_android_gms_internal_measurement_zzkjArr2[intValue];
                                                            intValue++;
                                                            length = i8;
                                                        }
                                                        if (length != com_google_android_gms_internal_measurement_zzkjArr6.length) {
                                                            com_google_android_gms_internal_measurement_zzkjArr6 = (zzkj[]) Arrays.copyOf(com_google_android_gms_internal_measurement_zzkjArr6, length);
                                                        }
                                                        com_google_android_gms_internal_measurement_zzkjArr2 = com_google_android_gms_internal_measurement_zzkjArr6;
                                                    } else {
                                                        zzgg().zzin().zzg("No unique parameters in main event. eventName", str10);
                                                    }
                                                    str3 = str10;
                                                    l2 = l3;
                                                    j3 = 0;
                                                    com_google_android_gms_internal_measurement_zzki = com_google_android_gms_internal_measurement_zzki4;
                                                    zze = zzga().zze(str7, com_google_android_gms_internal_measurement_zzki2.name);
                                                    if (zze != null) {
                                                        hashSet = hashSet3;
                                                        map5 = arrayMap4;
                                                        com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr2;
                                                        str8 = str3;
                                                        map3 = map;
                                                        map4 = map2;
                                                        j4 = j3;
                                                        zze = zze.zzie();
                                                    } else {
                                                        zzgg().zzin().zze("Event aggregate wasn't created during raw event logging. appId, event", zzfg.zzbh(str), zzgb().zzbe(str3));
                                                        map3 = map;
                                                        map4 = map2;
                                                        hashSet = hashSet3;
                                                        map5 = arrayMap4;
                                                        com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr2;
                                                        j4 = j3;
                                                        str8 = str3;
                                                        zze = new zzeq(str7, com_google_android_gms_internal_measurement_zzki2.name, 1, 1, com_google_android_gms_internal_measurement_zzki2.zzasw.longValue(), 0, null, null, null);
                                                    }
                                                    zzga().zza(zze);
                                                    j5 = zze.zzafp;
                                                    map15 = arrayMap2;
                                                    str10 = str8;
                                                    map16 = (Map) map15.get(str10);
                                                    if (map16 != null) {
                                                        str4 = str;
                                                    } else {
                                                        str4 = str;
                                                        map16 = zzga().zzj(str4, str10);
                                                        if (map16 == null) {
                                                            map16 = new ArrayMap();
                                                        }
                                                        map15.put(str10, map16);
                                                    }
                                                    it4 = map16.keySet().iterator();
                                                    while (it4.hasNext()) {
                                                        i = ((Integer) it4.next()).intValue();
                                                        hashSet4 = hashSet;
                                                        if (hashSet4.contains(Integer.valueOf(i))) {
                                                            arrayMap4 = map5;
                                                            map6 = map3;
                                                            bitSet = (BitSet) map6.get(Integer.valueOf(i));
                                                            map7 = map15;
                                                            map15 = map4;
                                                            bitSet2 = (BitSet) map15.get(Integer.valueOf(i));
                                                            if (((zzkh) arrayMap4.get(Integer.valueOf(i))) == null) {
                                                                com_google_android_gms_internal_measurement_zzkh2 = new zzkh();
                                                                arrayMap4.put(Integer.valueOf(i), com_google_android_gms_internal_measurement_zzkh2);
                                                                com_google_android_gms_internal_measurement_zzkh2.zzast = Boolean.valueOf(true);
                                                                bitSet5 = new BitSet();
                                                                map6.put(Integer.valueOf(i), bitSet5);
                                                                bitSet2 = new BitSet();
                                                                map15.put(Integer.valueOf(i), bitSet2);
                                                                bitSet = bitSet5;
                                                            }
                                                            it = ((List) map16.get(Integer.valueOf(i))).iterator();
                                                            while (it.hasNext()) {
                                                                map17 = map16;
                                                                com_google_android_gms_internal_measurement_zzjz = (zzjz) it.next();
                                                                it5 = it4;
                                                                it6 = it;
                                                                if (zzgg().isLoggable(2)) {
                                                                    map8 = map15;
                                                                    map9 = arrayMap4;
                                                                    map10 = map6;
                                                                } else {
                                                                    map8 = map15;
                                                                    map10 = map6;
                                                                    map9 = arrayMap4;
                                                                    zzgg().zzir().zzd("Evaluating filter. audience, filter, event", Integer.valueOf(i), com_google_android_gms_internal_measurement_zzjz.zzark, zzgb().zzbe(com_google_android_gms_internal_measurement_zzjz.zzarl));
                                                                    zzgg().zzir().zzg("Filter definition", zzgb().zza(com_google_android_gms_internal_measurement_zzjz));
                                                                }
                                                                if (com_google_android_gms_internal_measurement_zzjz.zzark != null) {
                                                                    if (com_google_android_gms_internal_measurement_zzjz.zzark.intValue() > 256) {
                                                                        if (bitSet.get(com_google_android_gms_internal_measurement_zzjz.zzark.intValue())) {
                                                                            if (com_google_android_gms_internal_measurement_zzjz.zzaro != null) {
                                                                                zza = zza(j5, com_google_android_gms_internal_measurement_zzjz.zzaro);
                                                                                if (zza != null) {
                                                                                    if (zza.booleanValue()) {
                                                                                        j6 = j5;
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
                                                                                            hashSet4.add(Integer.valueOf(i));
                                                                                        }
                                                                                        com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr4;
                                                                                        map16 = map17;
                                                                                        it4 = it5;
                                                                                        it = it6;
                                                                                        map15 = map8;
                                                                                        map6 = map10;
                                                                                        arrayMap4 = map9;
                                                                                        j5 = j6;
                                                                                    }
                                                                                }
                                                                                j6 = j5;
                                                                                com_google_android_gms_internal_measurement_zzkjArr4 = com_google_android_gms_internal_measurement_zzkjArr3;
                                                                                valueOf = null;
                                                                                if (valueOf != null) {
                                                                                }
                                                                                zzgg().zzir().zzg("Event filter result", valueOf != null ? "null" : valueOf);
                                                                                if (valueOf != null) {
                                                                                    hashSet4.add(Integer.valueOf(i));
                                                                                } else {
                                                                                    bitSet2.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                                                    if (valueOf.booleanValue()) {
                                                                                        bitSet.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                                                    }
                                                                                }
                                                                                com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr4;
                                                                                map16 = map17;
                                                                                it4 = it5;
                                                                                it = it6;
                                                                                map15 = map8;
                                                                                map6 = map10;
                                                                                arrayMap4 = map9;
                                                                                j5 = j6;
                                                                            }
                                                                            hashSet2 = new HashSet();
                                                                            for (i3 = 0; i3 < i6; i3++) {
                                                                                if (TextUtils.isEmpty(com_google_android_gms_internal_measurement_zzka2.zzart)) {
                                                                                    zzgg().zzin().zzg("null or empty param name in filter. event", zzgb().zzbe(str10));
                                                                                    j6 = j5;
                                                                                    com_google_android_gms_internal_measurement_zzkjArr4 = com_google_android_gms_internal_measurement_zzkjArr3;
                                                                                    break;
                                                                                }
                                                                                hashSet2.add(com_google_android_gms_internal_measurement_zzka2.zzart);
                                                                            }
                                                                            map15 = new ArrayMap();
                                                                            com_google_android_gms_internal_measurement_zzkjArr4 = com_google_android_gms_internal_measurement_zzkjArr3;
                                                                            length2 = com_google_android_gms_internal_measurement_zzkjArr4.length;
                                                                            i6 = 0;
                                                                            while (i6 < length2) {
                                                                                com_google_android_gms_internal_measurement_zzkj2 = com_google_android_gms_internal_measurement_zzkjArr4[i6];
                                                                                j6 = j5;
                                                                                if (!hashSet2.contains(com_google_android_gms_internal_measurement_zzkj2.name)) {
                                                                                    if (com_google_android_gms_internal_measurement_zzkj2.zzasz == null) {
                                                                                        obj2 = com_google_android_gms_internal_measurement_zzkj2.name;
                                                                                        obj = com_google_android_gms_internal_measurement_zzkj2.zzasz;
                                                                                    } else if (com_google_android_gms_internal_measurement_zzkj2.zzaqx == null) {
                                                                                        obj2 = com_google_android_gms_internal_measurement_zzkj2.name;
                                                                                        obj = com_google_android_gms_internal_measurement_zzkj2.zzaqx;
                                                                                    } else if (com_google_android_gms_internal_measurement_zzkj2.zzajf != null) {
                                                                                        zzin = zzgg().zzin();
                                                                                        str9 = "Unknown value for param. event, param";
                                                                                        zzbe = zzgb().zzbe(str10);
                                                                                        zzbf = zzgb().zzbf(com_google_android_gms_internal_measurement_zzkj2.name);
                                                                                        break;
                                                                                    } else {
                                                                                        obj2 = com_google_android_gms_internal_measurement_zzkj2.name;
                                                                                        obj = com_google_android_gms_internal_measurement_zzkj2.zzajf;
                                                                                    }
                                                                                    map15.put(obj2, obj);
                                                                                }
                                                                                i6++;
                                                                                j5 = j6;
                                                                            }
                                                                            j6 = j5;
                                                                            com_google_android_gms_internal_measurement_zzkaArr = com_google_android_gms_internal_measurement_zzjz.zzarm;
                                                                            length3 = com_google_android_gms_internal_measurement_zzkaArr.length;
                                                                            i8 = 0;
                                                                            while (i8 < length3) {
                                                                                com_google_android_gms_internal_measurement_zzka = com_google_android_gms_internal_measurement_zzkaArr[i8];
                                                                                equals = Boolean.TRUE.equals(com_google_android_gms_internal_measurement_zzka.zzars);
                                                                                str3 = com_google_android_gms_internal_measurement_zzka.zzart;
                                                                                if (TextUtils.isEmpty(str3)) {
                                                                                    com_google_android_gms_internal_measurement_zzkaArr2 = com_google_android_gms_internal_measurement_zzkaArr;
                                                                                    obj2 = map15.get(str3);
                                                                                    i9 = length3;
                                                                                    if (obj2 instanceof Long) {
                                                                                        if (obj2 instanceof Double) {
                                                                                            if (!(obj2 instanceof String)) {
                                                                                                if (obj2 == null) {
                                                                                                    zzgg().zzir().zze("Missing param for filter. event, param", zzgb().zzbe(str10), zzgb().zzbf(str3));
                                                                                                    z = false;
                                                                                                    break;
                                                                                                }
                                                                                                zzin = zzgg().zzin();
                                                                                                str9 = "Unknown param type. event, param";
                                                                                            } else {
                                                                                                if (com_google_android_gms_internal_measurement_zzka.zzarq != null) {
                                                                                                    zza2 = zza((String) obj2, com_google_android_gms_internal_measurement_zzka.zzarq);
                                                                                                } else if (com_google_android_gms_internal_measurement_zzka.zzarr == null) {
                                                                                                    zzin = zzgg().zzin();
                                                                                                    str9 = "No filter for String param. event, param";
                                                                                                } else {
                                                                                                    str5 = (String) obj2;
                                                                                                    if (zzjv.zzcd(str5)) {
                                                                                                        zzin = zzgg().zzin();
                                                                                                        str9 = "Invalid param value for number filter. event, param";
                                                                                                    } else {
                                                                                                        zza2 = zza(str5, com_google_android_gms_internal_measurement_zzka.zzarr);
                                                                                                    }
                                                                                                }
                                                                                                if (zza2 == null) {
                                                                                                    if (((zza2.booleanValue() ^ 1) ^ equals) != 0) {
                                                                                                        z = false;
                                                                                                        break;
                                                                                                    }
                                                                                                    i8++;
                                                                                                    com_google_android_gms_internal_measurement_zzkaArr = com_google_android_gms_internal_measurement_zzkaArr2;
                                                                                                    length3 = i9;
                                                                                                }
                                                                                            }
                                                                                        } else if (com_google_android_gms_internal_measurement_zzka.zzarr != null) {
                                                                                            zza2 = zza(((Double) obj2).doubleValue(), com_google_android_gms_internal_measurement_zzka.zzarr);
                                                                                            if (zza2 == null) {
                                                                                                if (((zza2.booleanValue() ^ 1) ^ equals) != 0) {
                                                                                                    z = false;
                                                                                                    break;
                                                                                                }
                                                                                                i8++;
                                                                                                com_google_android_gms_internal_measurement_zzkaArr = com_google_android_gms_internal_measurement_zzkaArr2;
                                                                                                length3 = i9;
                                                                                            }
                                                                                        } else {
                                                                                            zzin = zzgg().zzin();
                                                                                            str9 = "No number filter for double param. event, param";
                                                                                        }
                                                                                    } else if (com_google_android_gms_internal_measurement_zzka.zzarr != null) {
                                                                                        zza2 = zza(((Long) obj2).longValue(), com_google_android_gms_internal_measurement_zzka.zzarr);
                                                                                        if (zza2 == null) {
                                                                                            if (((zza2.booleanValue() ^ 1) ^ equals) != 0) {
                                                                                                z = false;
                                                                                                break;
                                                                                            }
                                                                                            i8++;
                                                                                            com_google_android_gms_internal_measurement_zzkaArr = com_google_android_gms_internal_measurement_zzkaArr2;
                                                                                            length3 = i9;
                                                                                        }
                                                                                    } else {
                                                                                        zzin = zzgg().zzin();
                                                                                        str9 = "No number filter for long param. event, param";
                                                                                    }
                                                                                    zzbe = zzgb().zzbe(str10);
                                                                                    zzbf = zzgb().zzbf(str3);
                                                                                    zzin.zze(str9, zzbe, zzbf);
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
                                                                                hashSet4.add(Integer.valueOf(i));
                                                                            }
                                                                            com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr4;
                                                                            map16 = map17;
                                                                            it4 = it5;
                                                                            it = it6;
                                                                            map15 = map8;
                                                                            map6 = map10;
                                                                            arrayMap4 = map9;
                                                                            j5 = j6;
                                                                        } else {
                                                                            zzgg().zzir().zze("Event filter already evaluated true. audience ID, filter ID", Integer.valueOf(i), com_google_android_gms_internal_measurement_zzjz.zzark);
                                                                            map16 = map17;
                                                                            it4 = it5;
                                                                            it = it6;
                                                                            map15 = map8;
                                                                            map6 = map10;
                                                                            arrayMap4 = map9;
                                                                        }
                                                                        str4 = str;
                                                                    }
                                                                }
                                                                j6 = j5;
                                                                com_google_android_gms_internal_measurement_zzkjArr4 = com_google_android_gms_internal_measurement_zzkjArr3;
                                                                str2 = str;
                                                                zzgg().zzin().zze("Invalid event filter ID. appId, id", zzfg.zzbh(str), String.valueOf(com_google_android_gms_internal_measurement_zzjz.zzark));
                                                                com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr4;
                                                                map16 = map17;
                                                                it4 = it5;
                                                                it = it6;
                                                                map6 = map10;
                                                                arrayMap4 = map9;
                                                                j5 = j6;
                                                                str4 = str2;
                                                                map15 = map8;
                                                            }
                                                            map8 = map15;
                                                            str2 = str4;
                                                            hashSet = hashSet4;
                                                            map5 = arrayMap4;
                                                            map3 = map6;
                                                            map15 = map7;
                                                            map4 = map8;
                                                        } else {
                                                            zzgg().zzir().zzg("Skipping failed audience ID", Integer.valueOf(i));
                                                            hashSet = hashSet4;
                                                        }
                                                    }
                                                    arrayMap3 = map15;
                                                    str2 = str4;
                                                    map10 = map3;
                                                    map8 = map4;
                                                    hashSet3 = hashSet;
                                                    map9 = map5;
                                                    l = l2;
                                                    j = j2;
                                                    i7 = i4 + 1;
                                                    com_google_android_gms_internal_measurement_zzkiArr2 = com_google_android_gms_internal_measurement_zzkiArr;
                                                    str7 = str2;
                                                    length = i2;
                                                    arrayMap7 = arrayMap3;
                                                    map2 = map8;
                                                    map = map10;
                                                    arrayMap4 = map9;
                                                    com_google_android_gms_internal_measurement_zzknArr2 = com_google_android_gms_internal_measurement_zzknArr;
                                                }
                                                i2 = length;
                                                i3 = i8;
                                                i4 = i10;
                                                com_google_android_gms_internal_measurement_zzkjArr2 = com_google_android_gms_internal_measurement_zzkjArr;
                                                arrayMap2 = arrayMap;
                                            } else {
                                                i11 = 0;
                                                i4 = i10;
                                                j7 = 0;
                                                i2 = length;
                                                arrayMap2 = arrayMap7;
                                                i3 = 1;
                                                com_google_android_gms_internal_measurement_zzkjArr2 = com_google_android_gms_internal_measurement_zzkjArr5;
                                                zzga().zza(str7, l5, j2, com_google_android_gms_internal_measurement_zzki4);
                                            }
                                            com_google_android_gms_internal_measurement_zzkjArr6 = new zzkj[(com_google_android_gms_internal_measurement_zzki4.zzasv.length + com_google_android_gms_internal_measurement_zzkjArr2.length)];
                                            com_google_android_gms_internal_measurement_zzkjArr7 = com_google_android_gms_internal_measurement_zzki4.zzasv;
                                            intValue = com_google_android_gms_internal_measurement_zzkjArr7.length;
                                            i5 = i11;
                                            length = i5;
                                            while (i5 < intValue) {
                                                com_google_android_gms_internal_measurement_zzkj = com_google_android_gms_internal_measurement_zzkjArr7[i5];
                                                zzgc();
                                                if (zzjv.zza(com_google_android_gms_internal_measurement_zzki2, com_google_android_gms_internal_measurement_zzkj.name) != null) {
                                                    i3 = length + 1;
                                                    com_google_android_gms_internal_measurement_zzkjArr6[length] = com_google_android_gms_internal_measurement_zzkj;
                                                    length = i3;
                                                }
                                                i5++;
                                            }
                                            if (length > 0) {
                                                length3 = com_google_android_gms_internal_measurement_zzkjArr2.length;
                                                intValue = i11;
                                                while (intValue < length3) {
                                                    i8 = length + 1;
                                                    com_google_android_gms_internal_measurement_zzkjArr6[length] = com_google_android_gms_internal_measurement_zzkjArr2[intValue];
                                                    intValue++;
                                                    length = i8;
                                                }
                                                if (length != com_google_android_gms_internal_measurement_zzkjArr6.length) {
                                                    com_google_android_gms_internal_measurement_zzkjArr6 = (zzkj[]) Arrays.copyOf(com_google_android_gms_internal_measurement_zzkjArr6, length);
                                                }
                                                com_google_android_gms_internal_measurement_zzkjArr2 = com_google_android_gms_internal_measurement_zzkjArr6;
                                            } else {
                                                zzgg().zzin().zzg("No unique parameters in main event. eventName", str10);
                                            }
                                            str3 = str10;
                                            l2 = l3;
                                            j3 = 0;
                                            com_google_android_gms_internal_measurement_zzki = com_google_android_gms_internal_measurement_zzki4;
                                        }
                                    }
                                    zza3 = zzga().zza(str7, l5);
                                    if (zza3 != null) {
                                        if (zza3.first == null) {
                                            com_google_android_gms_internal_measurement_zzki5 = (zzki) zza3.first;
                                            j = ((Long) zza3.second).longValue();
                                            zzgc();
                                            l3 = (Long) zzjv.zzb(com_google_android_gms_internal_measurement_zzki5, "_eid");
                                            com_google_android_gms_internal_measurement_zzki4 = com_google_android_gms_internal_measurement_zzki5;
                                            j2 = j - 1;
                                            if (j2 > 0) {
                                                i11 = 0;
                                                i4 = i10;
                                                j7 = 0;
                                                i2 = length;
                                                arrayMap2 = arrayMap7;
                                                i3 = 1;
                                                com_google_android_gms_internal_measurement_zzkjArr2 = com_google_android_gms_internal_measurement_zzkjArr5;
                                                zzga().zza(str7, l5, j2, com_google_android_gms_internal_measurement_zzki4);
                                            } else {
                                                zzga = zzga();
                                                zzga.zzab();
                                                zzga.zzgg().zzir().zzg("Clearing complex main event info. appId", str7);
                                                writableDatabase = zzga.getWritableDatabase();
                                                str2 = "delete from main_event_params where app_id=?";
                                                arrayMap = arrayMap7;
                                                com_google_android_gms_internal_measurement_zzkjArr = com_google_android_gms_internal_measurement_zzkjArr5;
                                                i8 = 1;
                                                strArr = new String[1];
                                                i11 = 0;
                                                strArr[0] = str7;
                                                writableDatabase.execSQL(str2, strArr);
                                                i2 = length;
                                                i3 = i8;
                                                i4 = i10;
                                                com_google_android_gms_internal_measurement_zzkjArr2 = com_google_android_gms_internal_measurement_zzkjArr;
                                                arrayMap2 = arrayMap;
                                            }
                                            com_google_android_gms_internal_measurement_zzkjArr6 = new zzkj[(com_google_android_gms_internal_measurement_zzki4.zzasv.length + com_google_android_gms_internal_measurement_zzkjArr2.length)];
                                            com_google_android_gms_internal_measurement_zzkjArr7 = com_google_android_gms_internal_measurement_zzki4.zzasv;
                                            intValue = com_google_android_gms_internal_measurement_zzkjArr7.length;
                                            i5 = i11;
                                            length = i5;
                                            while (i5 < intValue) {
                                                com_google_android_gms_internal_measurement_zzkj = com_google_android_gms_internal_measurement_zzkjArr7[i5];
                                                zzgc();
                                                if (zzjv.zza(com_google_android_gms_internal_measurement_zzki2, com_google_android_gms_internal_measurement_zzkj.name) != null) {
                                                    i3 = length + 1;
                                                    com_google_android_gms_internal_measurement_zzkjArr6[length] = com_google_android_gms_internal_measurement_zzkj;
                                                    length = i3;
                                                }
                                                i5++;
                                            }
                                            if (length > 0) {
                                                zzgg().zzin().zzg("No unique parameters in main event. eventName", str10);
                                            } else {
                                                length3 = com_google_android_gms_internal_measurement_zzkjArr2.length;
                                                intValue = i11;
                                                while (intValue < length3) {
                                                    i8 = length + 1;
                                                    com_google_android_gms_internal_measurement_zzkjArr6[length] = com_google_android_gms_internal_measurement_zzkjArr2[intValue];
                                                    intValue++;
                                                    length = i8;
                                                }
                                                if (length != com_google_android_gms_internal_measurement_zzkjArr6.length) {
                                                    com_google_android_gms_internal_measurement_zzkjArr6 = (zzkj[]) Arrays.copyOf(com_google_android_gms_internal_measurement_zzkjArr6, length);
                                                }
                                                com_google_android_gms_internal_measurement_zzkjArr2 = com_google_android_gms_internal_measurement_zzkjArr6;
                                            }
                                            str3 = str10;
                                            l2 = l3;
                                            j3 = 0;
                                            com_google_android_gms_internal_measurement_zzki = com_google_android_gms_internal_measurement_zzki4;
                                        }
                                    }
                                    arrayMap2 = arrayMap7;
                                    i2 = length;
                                    i4 = i10;
                                    zzgg().zzil().zze("Extra parameter without existing main event. eventName, eventId", str10, l5);
                                } else {
                                    zzgg().zzil().zzg("Extra parameter without an event name. eventId", l5);
                                    arrayMap2 = arrayMap7;
                                    i2 = length;
                                    i4 = i10;
                                }
                                map9 = arrayMap4;
                                str2 = str7;
                                map10 = map;
                                map8 = map2;
                                arrayMap3 = arrayMap2;
                                i7 = i4 + 1;
                                com_google_android_gms_internal_measurement_zzkiArr2 = com_google_android_gms_internal_measurement_zzkiArr;
                                str7 = str2;
                                length = i2;
                                arrayMap7 = arrayMap3;
                                map2 = map8;
                                map = map10;
                                arrayMap4 = map9;
                                com_google_android_gms_internal_measurement_zzknArr2 = com_google_android_gms_internal_measurement_zzknArr;
                            } else {
                                arrayMap2 = arrayMap7;
                                i2 = length;
                                com_google_android_gms_internal_measurement_zzkjArr2 = com_google_android_gms_internal_measurement_zzkjArr5;
                                i4 = i10;
                                if (obj != null) {
                                    zzgc();
                                    valueOf2 = Long.valueOf(0);
                                    obj2 = zzjv.zzb(com_google_android_gms_internal_measurement_zzki2, "_epc");
                                    if (obj2 == null) {
                                        obj2 = valueOf2;
                                    }
                                    j = ((Long) obj2).longValue();
                                    if (j > 0) {
                                        zzgg().zzin().zzg("Complex event with zero extra param count. eventName", str10);
                                        l4 = l5;
                                        j3 = 0;
                                    } else {
                                        l4 = l5;
                                        j3 = 0;
                                        zzga().zza(str7, l5, j, com_google_android_gms_internal_measurement_zzki2);
                                    }
                                    com_google_android_gms_internal_measurement_zzki = com_google_android_gms_internal_measurement_zzki2;
                                    j2 = j;
                                    l2 = l4;
                                    str3 = str10;
                                }
                            }
                            zze = zzga().zze(str7, com_google_android_gms_internal_measurement_zzki2.name);
                            if (zze != null) {
                                zzgg().zzin().zze("Event aggregate wasn't created during raw event logging. appId, event", zzfg.zzbh(str), zzgb().zzbe(str3));
                                map3 = map;
                                map4 = map2;
                                hashSet = hashSet3;
                                map5 = arrayMap4;
                                com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr2;
                                j4 = j3;
                                str8 = str3;
                                zze = new zzeq(str7, com_google_android_gms_internal_measurement_zzki2.name, 1, 1, com_google_android_gms_internal_measurement_zzki2.zzasw.longValue(), 0, null, null, null);
                            } else {
                                hashSet = hashSet3;
                                map5 = arrayMap4;
                                com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr2;
                                str8 = str3;
                                map3 = map;
                                map4 = map2;
                                j4 = j3;
                                zze = zze.zzie();
                            }
                            zzga().zza(zze);
                            j5 = zze.zzafp;
                            map15 = arrayMap2;
                            str10 = str8;
                            map16 = (Map) map15.get(str10);
                            if (map16 != null) {
                                str4 = str;
                                map16 = zzga().zzj(str4, str10);
                                if (map16 == null) {
                                    map16 = new ArrayMap();
                                }
                                map15.put(str10, map16);
                            } else {
                                str4 = str;
                            }
                            it4 = map16.keySet().iterator();
                            while (it4.hasNext()) {
                                i = ((Integer) it4.next()).intValue();
                                hashSet4 = hashSet;
                                if (hashSet4.contains(Integer.valueOf(i))) {
                                    zzgg().zzir().zzg("Skipping failed audience ID", Integer.valueOf(i));
                                    hashSet = hashSet4;
                                } else {
                                    arrayMap4 = map5;
                                    map6 = map3;
                                    bitSet = (BitSet) map6.get(Integer.valueOf(i));
                                    map7 = map15;
                                    map15 = map4;
                                    bitSet2 = (BitSet) map15.get(Integer.valueOf(i));
                                    if (((zzkh) arrayMap4.get(Integer.valueOf(i))) == null) {
                                        com_google_android_gms_internal_measurement_zzkh2 = new zzkh();
                                        arrayMap4.put(Integer.valueOf(i), com_google_android_gms_internal_measurement_zzkh2);
                                        com_google_android_gms_internal_measurement_zzkh2.zzast = Boolean.valueOf(true);
                                        bitSet5 = new BitSet();
                                        map6.put(Integer.valueOf(i), bitSet5);
                                        bitSet2 = new BitSet();
                                        map15.put(Integer.valueOf(i), bitSet2);
                                        bitSet = bitSet5;
                                    }
                                    it = ((List) map16.get(Integer.valueOf(i))).iterator();
                                    while (it.hasNext()) {
                                        map17 = map16;
                                        com_google_android_gms_internal_measurement_zzjz = (zzjz) it.next();
                                        it5 = it4;
                                        it6 = it;
                                        if (zzgg().isLoggable(2)) {
                                            map8 = map15;
                                            map10 = map6;
                                            map9 = arrayMap4;
                                            zzgg().zzir().zzd("Evaluating filter. audience, filter, event", Integer.valueOf(i), com_google_android_gms_internal_measurement_zzjz.zzark, zzgb().zzbe(com_google_android_gms_internal_measurement_zzjz.zzarl));
                                            zzgg().zzir().zzg("Filter definition", zzgb().zza(com_google_android_gms_internal_measurement_zzjz));
                                        } else {
                                            map8 = map15;
                                            map9 = arrayMap4;
                                            map10 = map6;
                                        }
                                        if (com_google_android_gms_internal_measurement_zzjz.zzark != null) {
                                            if (com_google_android_gms_internal_measurement_zzjz.zzark.intValue() > 256) {
                                                if (bitSet.get(com_google_android_gms_internal_measurement_zzjz.zzark.intValue())) {
                                                    zzgg().zzir().zze("Event filter already evaluated true. audience ID, filter ID", Integer.valueOf(i), com_google_android_gms_internal_measurement_zzjz.zzark);
                                                    map16 = map17;
                                                    it4 = it5;
                                                    it = it6;
                                                    map15 = map8;
                                                    map6 = map10;
                                                    arrayMap4 = map9;
                                                } else {
                                                    if (com_google_android_gms_internal_measurement_zzjz.zzaro != null) {
                                                        zza = zza(j5, com_google_android_gms_internal_measurement_zzjz.zzaro);
                                                        if (zza != null) {
                                                            if (zza.booleanValue()) {
                                                                j6 = j5;
                                                                valueOf = Boolean.valueOf(false);
                                                                com_google_android_gms_internal_measurement_zzkjArr4 = com_google_android_gms_internal_measurement_zzkjArr3;
                                                                if (valueOf != null) {
                                                                }
                                                                zzgg().zzir().zzg("Event filter result", valueOf != null ? "null" : valueOf);
                                                                if (valueOf != null) {
                                                                    hashSet4.add(Integer.valueOf(i));
                                                                } else {
                                                                    bitSet2.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                                    if (valueOf.booleanValue()) {
                                                                        bitSet.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                                    }
                                                                }
                                                                com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr4;
                                                                map16 = map17;
                                                                it4 = it5;
                                                                it = it6;
                                                                map15 = map8;
                                                                map6 = map10;
                                                                arrayMap4 = map9;
                                                                j5 = j6;
                                                            }
                                                        }
                                                        j6 = j5;
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
                                                            hashSet4.add(Integer.valueOf(i));
                                                        }
                                                        com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr4;
                                                        map16 = map17;
                                                        it4 = it5;
                                                        it = it6;
                                                        map15 = map8;
                                                        map6 = map10;
                                                        arrayMap4 = map9;
                                                        j5 = j6;
                                                    }
                                                    hashSet2 = new HashSet();
                                                    for (i3 = 0; i3 < i6; i3++) {
                                                        if (TextUtils.isEmpty(com_google_android_gms_internal_measurement_zzka2.zzart)) {
                                                            zzgg().zzin().zzg("null or empty param name in filter. event", zzgb().zzbe(str10));
                                                            j6 = j5;
                                                            com_google_android_gms_internal_measurement_zzkjArr4 = com_google_android_gms_internal_measurement_zzkjArr3;
                                                            break;
                                                        }
                                                        hashSet2.add(com_google_android_gms_internal_measurement_zzka2.zzart);
                                                    }
                                                    map15 = new ArrayMap();
                                                    com_google_android_gms_internal_measurement_zzkjArr4 = com_google_android_gms_internal_measurement_zzkjArr3;
                                                    length2 = com_google_android_gms_internal_measurement_zzkjArr4.length;
                                                    i6 = 0;
                                                    while (i6 < length2) {
                                                        com_google_android_gms_internal_measurement_zzkj2 = com_google_android_gms_internal_measurement_zzkjArr4[i6];
                                                        j6 = j5;
                                                        if (!hashSet2.contains(com_google_android_gms_internal_measurement_zzkj2.name)) {
                                                            if (com_google_android_gms_internal_measurement_zzkj2.zzasz == null) {
                                                                if (com_google_android_gms_internal_measurement_zzkj2.zzaqx == null) {
                                                                    if (com_google_android_gms_internal_measurement_zzkj2.zzajf != null) {
                                                                        zzin = zzgg().zzin();
                                                                        str9 = "Unknown value for param. event, param";
                                                                        zzbe = zzgb().zzbe(str10);
                                                                        zzbf = zzgb().zzbf(com_google_android_gms_internal_measurement_zzkj2.name);
                                                                        break;
                                                                    }
                                                                    obj2 = com_google_android_gms_internal_measurement_zzkj2.name;
                                                                    obj = com_google_android_gms_internal_measurement_zzkj2.zzajf;
                                                                } else {
                                                                    obj2 = com_google_android_gms_internal_measurement_zzkj2.name;
                                                                    obj = com_google_android_gms_internal_measurement_zzkj2.zzaqx;
                                                                }
                                                            } else {
                                                                obj2 = com_google_android_gms_internal_measurement_zzkj2.name;
                                                                obj = com_google_android_gms_internal_measurement_zzkj2.zzasz;
                                                            }
                                                            map15.put(obj2, obj);
                                                        }
                                                        i6++;
                                                        j5 = j6;
                                                    }
                                                    j6 = j5;
                                                    com_google_android_gms_internal_measurement_zzkaArr = com_google_android_gms_internal_measurement_zzjz.zzarm;
                                                    length3 = com_google_android_gms_internal_measurement_zzkaArr.length;
                                                    i8 = 0;
                                                    while (i8 < length3) {
                                                        com_google_android_gms_internal_measurement_zzka = com_google_android_gms_internal_measurement_zzkaArr[i8];
                                                        equals = Boolean.TRUE.equals(com_google_android_gms_internal_measurement_zzka.zzars);
                                                        str3 = com_google_android_gms_internal_measurement_zzka.zzart;
                                                        if (TextUtils.isEmpty(str3)) {
                                                            zzgg().zzin().zzg("Event has empty param name. event", zzgb().zzbe(str10));
                                                        } else {
                                                            com_google_android_gms_internal_measurement_zzkaArr2 = com_google_android_gms_internal_measurement_zzkaArr;
                                                            obj2 = map15.get(str3);
                                                            i9 = length3;
                                                            if (obj2 instanceof Long) {
                                                                if (com_google_android_gms_internal_measurement_zzka.zzarr != null) {
                                                                    zzin = zzgg().zzin();
                                                                    str9 = "No number filter for long param. event, param";
                                                                } else {
                                                                    zza2 = zza(((Long) obj2).longValue(), com_google_android_gms_internal_measurement_zzka.zzarr);
                                                                    if (zza2 == null) {
                                                                        if (((zza2.booleanValue() ^ 1) ^ equals) != 0) {
                                                                            z = false;
                                                                            break;
                                                                        }
                                                                        i8++;
                                                                        com_google_android_gms_internal_measurement_zzkaArr = com_google_android_gms_internal_measurement_zzkaArr2;
                                                                        length3 = i9;
                                                                    }
                                                                }
                                                            } else if (obj2 instanceof Double) {
                                                                if (com_google_android_gms_internal_measurement_zzka.zzarr != null) {
                                                                    zzin = zzgg().zzin();
                                                                    str9 = "No number filter for double param. event, param";
                                                                } else {
                                                                    zza2 = zza(((Double) obj2).doubleValue(), com_google_android_gms_internal_measurement_zzka.zzarr);
                                                                    if (zza2 == null) {
                                                                        if (((zza2.booleanValue() ^ 1) ^ equals) != 0) {
                                                                            z = false;
                                                                            break;
                                                                        }
                                                                        i8++;
                                                                        com_google_android_gms_internal_measurement_zzkaArr = com_google_android_gms_internal_measurement_zzkaArr2;
                                                                        length3 = i9;
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
                                                                        str9 = "Invalid param value for number filter. event, param";
                                                                    }
                                                                } else {
                                                                    zzin = zzgg().zzin();
                                                                    str9 = "No filter for String param. event, param";
                                                                }
                                                                if (zza2 == null) {
                                                                    if (((zza2.booleanValue() ^ 1) ^ equals) != 0) {
                                                                        z = false;
                                                                        break;
                                                                    }
                                                                    i8++;
                                                                    com_google_android_gms_internal_measurement_zzkaArr = com_google_android_gms_internal_measurement_zzkaArr2;
                                                                    length3 = i9;
                                                                }
                                                            } else if (obj2 == null) {
                                                                zzgg().zzir().zze("Missing param for filter. event, param", zzgb().zzbe(str10), zzgb().zzbf(str3));
                                                                z = false;
                                                                break;
                                                            } else {
                                                                zzin = zzgg().zzin();
                                                                str9 = "Unknown param type. event, param";
                                                            }
                                                            zzbe = zzgb().zzbe(str10);
                                                            zzbf = zzgb().zzbf(str3);
                                                            zzin.zze(str9, zzbe, zzbf);
                                                        }
                                                        valueOf = null;
                                                    }
                                                    z = true;
                                                    valueOf = Boolean.valueOf(z);
                                                    if (valueOf != null) {
                                                    }
                                                    zzgg().zzir().zzg("Event filter result", valueOf != null ? "null" : valueOf);
                                                    if (valueOf != null) {
                                                        hashSet4.add(Integer.valueOf(i));
                                                    } else {
                                                        bitSet2.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                        if (valueOf.booleanValue()) {
                                                            bitSet.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                        }
                                                    }
                                                    com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr4;
                                                    map16 = map17;
                                                    it4 = it5;
                                                    it = it6;
                                                    map15 = map8;
                                                    map6 = map10;
                                                    arrayMap4 = map9;
                                                    j5 = j6;
                                                }
                                                str4 = str;
                                            }
                                        }
                                        j6 = j5;
                                        com_google_android_gms_internal_measurement_zzkjArr4 = com_google_android_gms_internal_measurement_zzkjArr3;
                                        str2 = str;
                                        zzgg().zzin().zze("Invalid event filter ID. appId, id", zzfg.zzbh(str), String.valueOf(com_google_android_gms_internal_measurement_zzjz.zzark));
                                        com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr4;
                                        map16 = map17;
                                        it4 = it5;
                                        it = it6;
                                        map6 = map10;
                                        arrayMap4 = map9;
                                        j5 = j6;
                                        str4 = str2;
                                        map15 = map8;
                                    }
                                    map8 = map15;
                                    str2 = str4;
                                    hashSet = hashSet4;
                                    map5 = arrayMap4;
                                    map3 = map6;
                                    map15 = map7;
                                    map4 = map8;
                                }
                            }
                            arrayMap3 = map15;
                            str2 = str4;
                            map10 = map3;
                            map8 = map4;
                            hashSet3 = hashSet;
                            map9 = map5;
                            l = l2;
                            j = j2;
                            i7 = i4 + 1;
                            com_google_android_gms_internal_measurement_zzkiArr2 = com_google_android_gms_internal_measurement_zzkiArr;
                            str7 = str2;
                            length = i2;
                            arrayMap7 = arrayMap3;
                            map2 = map8;
                            map = map10;
                            arrayMap4 = map9;
                            com_google_android_gms_internal_measurement_zzknArr2 = com_google_android_gms_internal_measurement_zzknArr;
                        }
                    } else {
                        i10 = i7;
                    }
                    obj2 = null;
                    if (obj2 == null) {
                        arrayMap2 = arrayMap7;
                        i2 = length;
                        com_google_android_gms_internal_measurement_zzkjArr2 = com_google_android_gms_internal_measurement_zzkjArr5;
                        i4 = i10;
                        if (obj != null) {
                            zzgc();
                            valueOf2 = Long.valueOf(0);
                            obj2 = zzjv.zzb(com_google_android_gms_internal_measurement_zzki2, "_epc");
                            if (obj2 == null) {
                                obj2 = valueOf2;
                            }
                            j = ((Long) obj2).longValue();
                            if (j > 0) {
                                l4 = l5;
                                j3 = 0;
                                zzga().zza(str7, l5, j, com_google_android_gms_internal_measurement_zzki2);
                            } else {
                                zzgg().zzin().zzg("Complex event with zero extra param count. eventName", str10);
                                l4 = l5;
                                j3 = 0;
                            }
                            com_google_android_gms_internal_measurement_zzki = com_google_android_gms_internal_measurement_zzki2;
                            j2 = j;
                            l2 = l4;
                            str3 = str10;
                        }
                    } else {
                        zzgc();
                        str10 = (String) zzjv.zzb(com_google_android_gms_internal_measurement_zzki2, "_en");
                        if (!TextUtils.isEmpty(str10)) {
                            zzgg().zzil().zzg("Extra parameter without an event name. eventId", l5);
                            arrayMap2 = arrayMap7;
                            i2 = length;
                            i4 = i10;
                        } else if (l5.longValue() != l.longValue()) {
                            com_google_android_gms_internal_measurement_zzki3 = com_google_android_gms_internal_measurement_zzki;
                            l3 = l;
                            com_google_android_gms_internal_measurement_zzki4 = com_google_android_gms_internal_measurement_zzki3;
                            j2 = j - 1;
                            if (j2 > 0) {
                                zzga = zzga();
                                zzga.zzab();
                                zzga.zzgg().zzir().zzg("Clearing complex main event info. appId", str7);
                                writableDatabase = zzga.getWritableDatabase();
                                str2 = "delete from main_event_params where app_id=?";
                                arrayMap = arrayMap7;
                                com_google_android_gms_internal_measurement_zzkjArr = com_google_android_gms_internal_measurement_zzkjArr5;
                                i8 = 1;
                                strArr = new String[1];
                                i11 = 0;
                                strArr[0] = str7;
                                writableDatabase.execSQL(str2, strArr);
                                i2 = length;
                                i3 = i8;
                                i4 = i10;
                                com_google_android_gms_internal_measurement_zzkjArr2 = com_google_android_gms_internal_measurement_zzkjArr;
                                arrayMap2 = arrayMap;
                            } else {
                                i11 = 0;
                                i4 = i10;
                                j7 = 0;
                                i2 = length;
                                arrayMap2 = arrayMap7;
                                i3 = 1;
                                com_google_android_gms_internal_measurement_zzkjArr2 = com_google_android_gms_internal_measurement_zzkjArr5;
                                zzga().zza(str7, l5, j2, com_google_android_gms_internal_measurement_zzki4);
                            }
                            com_google_android_gms_internal_measurement_zzkjArr6 = new zzkj[(com_google_android_gms_internal_measurement_zzki4.zzasv.length + com_google_android_gms_internal_measurement_zzkjArr2.length)];
                            com_google_android_gms_internal_measurement_zzkjArr7 = com_google_android_gms_internal_measurement_zzki4.zzasv;
                            intValue = com_google_android_gms_internal_measurement_zzkjArr7.length;
                            i5 = i11;
                            length = i5;
                            while (i5 < intValue) {
                                com_google_android_gms_internal_measurement_zzkj = com_google_android_gms_internal_measurement_zzkjArr7[i5];
                                zzgc();
                                if (zzjv.zza(com_google_android_gms_internal_measurement_zzki2, com_google_android_gms_internal_measurement_zzkj.name) != null) {
                                    i3 = length + 1;
                                    com_google_android_gms_internal_measurement_zzkjArr6[length] = com_google_android_gms_internal_measurement_zzkj;
                                    length = i3;
                                }
                                i5++;
                            }
                            if (length > 0) {
                                length3 = com_google_android_gms_internal_measurement_zzkjArr2.length;
                                intValue = i11;
                                while (intValue < length3) {
                                    i8 = length + 1;
                                    com_google_android_gms_internal_measurement_zzkjArr6[length] = com_google_android_gms_internal_measurement_zzkjArr2[intValue];
                                    intValue++;
                                    length = i8;
                                }
                                if (length != com_google_android_gms_internal_measurement_zzkjArr6.length) {
                                    com_google_android_gms_internal_measurement_zzkjArr6 = (zzkj[]) Arrays.copyOf(com_google_android_gms_internal_measurement_zzkjArr6, length);
                                }
                                com_google_android_gms_internal_measurement_zzkjArr2 = com_google_android_gms_internal_measurement_zzkjArr6;
                            } else {
                                zzgg().zzin().zzg("No unique parameters in main event. eventName", str10);
                            }
                            str3 = str10;
                            l2 = l3;
                            j3 = 0;
                            com_google_android_gms_internal_measurement_zzki = com_google_android_gms_internal_measurement_zzki4;
                        } else {
                            zza3 = zzga().zza(str7, l5);
                            if (zza3 != null) {
                                if (zza3.first == null) {
                                    com_google_android_gms_internal_measurement_zzki5 = (zzki) zza3.first;
                                    j = ((Long) zza3.second).longValue();
                                    zzgc();
                                    l3 = (Long) zzjv.zzb(com_google_android_gms_internal_measurement_zzki5, "_eid");
                                    com_google_android_gms_internal_measurement_zzki4 = com_google_android_gms_internal_measurement_zzki5;
                                    j2 = j - 1;
                                    if (j2 > 0) {
                                        i11 = 0;
                                        i4 = i10;
                                        j7 = 0;
                                        i2 = length;
                                        arrayMap2 = arrayMap7;
                                        i3 = 1;
                                        com_google_android_gms_internal_measurement_zzkjArr2 = com_google_android_gms_internal_measurement_zzkjArr5;
                                        zzga().zza(str7, l5, j2, com_google_android_gms_internal_measurement_zzki4);
                                    } else {
                                        zzga = zzga();
                                        zzga.zzab();
                                        zzga.zzgg().zzir().zzg("Clearing complex main event info. appId", str7);
                                        writableDatabase = zzga.getWritableDatabase();
                                        str2 = "delete from main_event_params where app_id=?";
                                        arrayMap = arrayMap7;
                                        com_google_android_gms_internal_measurement_zzkjArr = com_google_android_gms_internal_measurement_zzkjArr5;
                                        i8 = 1;
                                        strArr = new String[1];
                                        i11 = 0;
                                        strArr[0] = str7;
                                        writableDatabase.execSQL(str2, strArr);
                                        i2 = length;
                                        i3 = i8;
                                        i4 = i10;
                                        com_google_android_gms_internal_measurement_zzkjArr2 = com_google_android_gms_internal_measurement_zzkjArr;
                                        arrayMap2 = arrayMap;
                                    }
                                    com_google_android_gms_internal_measurement_zzkjArr6 = new zzkj[(com_google_android_gms_internal_measurement_zzki4.zzasv.length + com_google_android_gms_internal_measurement_zzkjArr2.length)];
                                    com_google_android_gms_internal_measurement_zzkjArr7 = com_google_android_gms_internal_measurement_zzki4.zzasv;
                                    intValue = com_google_android_gms_internal_measurement_zzkjArr7.length;
                                    i5 = i11;
                                    length = i5;
                                    while (i5 < intValue) {
                                        com_google_android_gms_internal_measurement_zzkj = com_google_android_gms_internal_measurement_zzkjArr7[i5];
                                        zzgc();
                                        if (zzjv.zza(com_google_android_gms_internal_measurement_zzki2, com_google_android_gms_internal_measurement_zzkj.name) != null) {
                                            i3 = length + 1;
                                            com_google_android_gms_internal_measurement_zzkjArr6[length] = com_google_android_gms_internal_measurement_zzkj;
                                            length = i3;
                                        }
                                        i5++;
                                    }
                                    if (length > 0) {
                                        zzgg().zzin().zzg("No unique parameters in main event. eventName", str10);
                                    } else {
                                        length3 = com_google_android_gms_internal_measurement_zzkjArr2.length;
                                        intValue = i11;
                                        while (intValue < length3) {
                                            i8 = length + 1;
                                            com_google_android_gms_internal_measurement_zzkjArr6[length] = com_google_android_gms_internal_measurement_zzkjArr2[intValue];
                                            intValue++;
                                            length = i8;
                                        }
                                        if (length != com_google_android_gms_internal_measurement_zzkjArr6.length) {
                                            com_google_android_gms_internal_measurement_zzkjArr6 = (zzkj[]) Arrays.copyOf(com_google_android_gms_internal_measurement_zzkjArr6, length);
                                        }
                                        com_google_android_gms_internal_measurement_zzkjArr2 = com_google_android_gms_internal_measurement_zzkjArr6;
                                    }
                                    str3 = str10;
                                    l2 = l3;
                                    j3 = 0;
                                    com_google_android_gms_internal_measurement_zzki = com_google_android_gms_internal_measurement_zzki4;
                                }
                            }
                            arrayMap2 = arrayMap7;
                            i2 = length;
                            i4 = i10;
                            zzgg().zzil().zze("Extra parameter without existing main event. eventName, eventId", str10, l5);
                        }
                        map9 = arrayMap4;
                        str2 = str7;
                        map10 = map;
                        map8 = map2;
                        arrayMap3 = arrayMap2;
                        i7 = i4 + 1;
                        com_google_android_gms_internal_measurement_zzkiArr2 = com_google_android_gms_internal_measurement_zzkiArr;
                        str7 = str2;
                        length = i2;
                        arrayMap7 = arrayMap3;
                        map2 = map8;
                        map = map10;
                        arrayMap4 = map9;
                        com_google_android_gms_internal_measurement_zzknArr2 = com_google_android_gms_internal_measurement_zzknArr;
                    }
                    zze = zzga().zze(str7, com_google_android_gms_internal_measurement_zzki2.name);
                    if (zze != null) {
                        hashSet = hashSet3;
                        map5 = arrayMap4;
                        com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr2;
                        str8 = str3;
                        map3 = map;
                        map4 = map2;
                        j4 = j3;
                        zze = zze.zzie();
                    } else {
                        zzgg().zzin().zze("Event aggregate wasn't created during raw event logging. appId, event", zzfg.zzbh(str), zzgb().zzbe(str3));
                        map3 = map;
                        map4 = map2;
                        hashSet = hashSet3;
                        map5 = arrayMap4;
                        com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr2;
                        j4 = j3;
                        str8 = str3;
                        zze = new zzeq(str7, com_google_android_gms_internal_measurement_zzki2.name, 1, 1, com_google_android_gms_internal_measurement_zzki2.zzasw.longValue(), 0, null, null, null);
                    }
                    zzga().zza(zze);
                    j5 = zze.zzafp;
                    map15 = arrayMap2;
                    str10 = str8;
                    map16 = (Map) map15.get(str10);
                    if (map16 != null) {
                        str4 = str;
                    } else {
                        str4 = str;
                        map16 = zzga().zzj(str4, str10);
                        if (map16 == null) {
                            map16 = new ArrayMap();
                        }
                        map15.put(str10, map16);
                    }
                    it4 = map16.keySet().iterator();
                    while (it4.hasNext()) {
                        i = ((Integer) it4.next()).intValue();
                        hashSet4 = hashSet;
                        if (hashSet4.contains(Integer.valueOf(i))) {
                            arrayMap4 = map5;
                            map6 = map3;
                            bitSet = (BitSet) map6.get(Integer.valueOf(i));
                            map7 = map15;
                            map15 = map4;
                            bitSet2 = (BitSet) map15.get(Integer.valueOf(i));
                            if (((zzkh) arrayMap4.get(Integer.valueOf(i))) == null) {
                                com_google_android_gms_internal_measurement_zzkh2 = new zzkh();
                                arrayMap4.put(Integer.valueOf(i), com_google_android_gms_internal_measurement_zzkh2);
                                com_google_android_gms_internal_measurement_zzkh2.zzast = Boolean.valueOf(true);
                                bitSet5 = new BitSet();
                                map6.put(Integer.valueOf(i), bitSet5);
                                bitSet2 = new BitSet();
                                map15.put(Integer.valueOf(i), bitSet2);
                                bitSet = bitSet5;
                            }
                            it = ((List) map16.get(Integer.valueOf(i))).iterator();
                            while (it.hasNext()) {
                                map17 = map16;
                                com_google_android_gms_internal_measurement_zzjz = (zzjz) it.next();
                                it5 = it4;
                                it6 = it;
                                if (zzgg().isLoggable(2)) {
                                    map8 = map15;
                                    map9 = arrayMap4;
                                    map10 = map6;
                                } else {
                                    map8 = map15;
                                    map10 = map6;
                                    map9 = arrayMap4;
                                    zzgg().zzir().zzd("Evaluating filter. audience, filter, event", Integer.valueOf(i), com_google_android_gms_internal_measurement_zzjz.zzark, zzgb().zzbe(com_google_android_gms_internal_measurement_zzjz.zzarl));
                                    zzgg().zzir().zzg("Filter definition", zzgb().zza(com_google_android_gms_internal_measurement_zzjz));
                                }
                                if (com_google_android_gms_internal_measurement_zzjz.zzark != null) {
                                    if (com_google_android_gms_internal_measurement_zzjz.zzark.intValue() > 256) {
                                        if (bitSet.get(com_google_android_gms_internal_measurement_zzjz.zzark.intValue())) {
                                            if (com_google_android_gms_internal_measurement_zzjz.zzaro != null) {
                                                zza = zza(j5, com_google_android_gms_internal_measurement_zzjz.zzaro);
                                                if (zza != null) {
                                                    if (zza.booleanValue()) {
                                                        j6 = j5;
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
                                                            hashSet4.add(Integer.valueOf(i));
                                                        }
                                                        com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr4;
                                                        map16 = map17;
                                                        it4 = it5;
                                                        it = it6;
                                                        map15 = map8;
                                                        map6 = map10;
                                                        arrayMap4 = map9;
                                                        j5 = j6;
                                                    }
                                                }
                                                j6 = j5;
                                                com_google_android_gms_internal_measurement_zzkjArr4 = com_google_android_gms_internal_measurement_zzkjArr3;
                                                valueOf = null;
                                                if (valueOf != null) {
                                                }
                                                zzgg().zzir().zzg("Event filter result", valueOf != null ? "null" : valueOf);
                                                if (valueOf != null) {
                                                    hashSet4.add(Integer.valueOf(i));
                                                } else {
                                                    bitSet2.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                    if (valueOf.booleanValue()) {
                                                        bitSet.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                    }
                                                }
                                                com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr4;
                                                map16 = map17;
                                                it4 = it5;
                                                it = it6;
                                                map15 = map8;
                                                map6 = map10;
                                                arrayMap4 = map9;
                                                j5 = j6;
                                            }
                                            hashSet2 = new HashSet();
                                            for (i3 = 0; i3 < i6; i3++) {
                                                if (TextUtils.isEmpty(com_google_android_gms_internal_measurement_zzka2.zzart)) {
                                                    zzgg().zzin().zzg("null or empty param name in filter. event", zzgb().zzbe(str10));
                                                    j6 = j5;
                                                    com_google_android_gms_internal_measurement_zzkjArr4 = com_google_android_gms_internal_measurement_zzkjArr3;
                                                    break;
                                                }
                                                hashSet2.add(com_google_android_gms_internal_measurement_zzka2.zzart);
                                            }
                                            map15 = new ArrayMap();
                                            com_google_android_gms_internal_measurement_zzkjArr4 = com_google_android_gms_internal_measurement_zzkjArr3;
                                            length2 = com_google_android_gms_internal_measurement_zzkjArr4.length;
                                            i6 = 0;
                                            while (i6 < length2) {
                                                com_google_android_gms_internal_measurement_zzkj2 = com_google_android_gms_internal_measurement_zzkjArr4[i6];
                                                j6 = j5;
                                                if (!hashSet2.contains(com_google_android_gms_internal_measurement_zzkj2.name)) {
                                                    if (com_google_android_gms_internal_measurement_zzkj2.zzasz == null) {
                                                        obj2 = com_google_android_gms_internal_measurement_zzkj2.name;
                                                        obj = com_google_android_gms_internal_measurement_zzkj2.zzasz;
                                                    } else if (com_google_android_gms_internal_measurement_zzkj2.zzaqx == null) {
                                                        obj2 = com_google_android_gms_internal_measurement_zzkj2.name;
                                                        obj = com_google_android_gms_internal_measurement_zzkj2.zzaqx;
                                                    } else if (com_google_android_gms_internal_measurement_zzkj2.zzajf != null) {
                                                        zzin = zzgg().zzin();
                                                        str9 = "Unknown value for param. event, param";
                                                        zzbe = zzgb().zzbe(str10);
                                                        zzbf = zzgb().zzbf(com_google_android_gms_internal_measurement_zzkj2.name);
                                                        break;
                                                    } else {
                                                        obj2 = com_google_android_gms_internal_measurement_zzkj2.name;
                                                        obj = com_google_android_gms_internal_measurement_zzkj2.zzajf;
                                                    }
                                                    map15.put(obj2, obj);
                                                }
                                                i6++;
                                                j5 = j6;
                                            }
                                            j6 = j5;
                                            com_google_android_gms_internal_measurement_zzkaArr = com_google_android_gms_internal_measurement_zzjz.zzarm;
                                            length3 = com_google_android_gms_internal_measurement_zzkaArr.length;
                                            i8 = 0;
                                            while (i8 < length3) {
                                                com_google_android_gms_internal_measurement_zzka = com_google_android_gms_internal_measurement_zzkaArr[i8];
                                                equals = Boolean.TRUE.equals(com_google_android_gms_internal_measurement_zzka.zzars);
                                                str3 = com_google_android_gms_internal_measurement_zzka.zzart;
                                                if (TextUtils.isEmpty(str3)) {
                                                    com_google_android_gms_internal_measurement_zzkaArr2 = com_google_android_gms_internal_measurement_zzkaArr;
                                                    obj2 = map15.get(str3);
                                                    i9 = length3;
                                                    if (obj2 instanceof Long) {
                                                        if (obj2 instanceof Double) {
                                                            if (!(obj2 instanceof String)) {
                                                                if (obj2 == null) {
                                                                    zzgg().zzir().zze("Missing param for filter. event, param", zzgb().zzbe(str10), zzgb().zzbf(str3));
                                                                    z = false;
                                                                    break;
                                                                }
                                                                zzin = zzgg().zzin();
                                                                str9 = "Unknown param type. event, param";
                                                            } else {
                                                                if (com_google_android_gms_internal_measurement_zzka.zzarq != null) {
                                                                    zza2 = zza((String) obj2, com_google_android_gms_internal_measurement_zzka.zzarq);
                                                                } else if (com_google_android_gms_internal_measurement_zzka.zzarr == null) {
                                                                    zzin = zzgg().zzin();
                                                                    str9 = "No filter for String param. event, param";
                                                                } else {
                                                                    str5 = (String) obj2;
                                                                    if (zzjv.zzcd(str5)) {
                                                                        zzin = zzgg().zzin();
                                                                        str9 = "Invalid param value for number filter. event, param";
                                                                    } else {
                                                                        zza2 = zza(str5, com_google_android_gms_internal_measurement_zzka.zzarr);
                                                                    }
                                                                }
                                                                if (zza2 == null) {
                                                                    if (((zza2.booleanValue() ^ 1) ^ equals) != 0) {
                                                                        z = false;
                                                                        break;
                                                                    }
                                                                    i8++;
                                                                    com_google_android_gms_internal_measurement_zzkaArr = com_google_android_gms_internal_measurement_zzkaArr2;
                                                                    length3 = i9;
                                                                }
                                                            }
                                                        } else if (com_google_android_gms_internal_measurement_zzka.zzarr != null) {
                                                            zza2 = zza(((Double) obj2).doubleValue(), com_google_android_gms_internal_measurement_zzka.zzarr);
                                                            if (zza2 == null) {
                                                                if (((zza2.booleanValue() ^ 1) ^ equals) != 0) {
                                                                    z = false;
                                                                    break;
                                                                }
                                                                i8++;
                                                                com_google_android_gms_internal_measurement_zzkaArr = com_google_android_gms_internal_measurement_zzkaArr2;
                                                                length3 = i9;
                                                            }
                                                        } else {
                                                            zzin = zzgg().zzin();
                                                            str9 = "No number filter for double param. event, param";
                                                        }
                                                    } else if (com_google_android_gms_internal_measurement_zzka.zzarr != null) {
                                                        zza2 = zza(((Long) obj2).longValue(), com_google_android_gms_internal_measurement_zzka.zzarr);
                                                        if (zza2 == null) {
                                                            if (((zza2.booleanValue() ^ 1) ^ equals) != 0) {
                                                                z = false;
                                                                break;
                                                            }
                                                            i8++;
                                                            com_google_android_gms_internal_measurement_zzkaArr = com_google_android_gms_internal_measurement_zzkaArr2;
                                                            length3 = i9;
                                                        }
                                                    } else {
                                                        zzin = zzgg().zzin();
                                                        str9 = "No number filter for long param. event, param";
                                                    }
                                                    zzbe = zzgb().zzbe(str10);
                                                    zzbf = zzgb().zzbf(str3);
                                                    zzin.zze(str9, zzbe, zzbf);
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
                                                hashSet4.add(Integer.valueOf(i));
                                            }
                                            com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr4;
                                            map16 = map17;
                                            it4 = it5;
                                            it = it6;
                                            map15 = map8;
                                            map6 = map10;
                                            arrayMap4 = map9;
                                            j5 = j6;
                                        } else {
                                            zzgg().zzir().zze("Event filter already evaluated true. audience ID, filter ID", Integer.valueOf(i), com_google_android_gms_internal_measurement_zzjz.zzark);
                                            map16 = map17;
                                            it4 = it5;
                                            it = it6;
                                            map15 = map8;
                                            map6 = map10;
                                            arrayMap4 = map9;
                                        }
                                        str4 = str;
                                    }
                                }
                                j6 = j5;
                                com_google_android_gms_internal_measurement_zzkjArr4 = com_google_android_gms_internal_measurement_zzkjArr3;
                                str2 = str;
                                zzgg().zzin().zze("Invalid event filter ID. appId, id", zzfg.zzbh(str), String.valueOf(com_google_android_gms_internal_measurement_zzjz.zzark));
                                com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr4;
                                map16 = map17;
                                it4 = it5;
                                it = it6;
                                map6 = map10;
                                arrayMap4 = map9;
                                j5 = j6;
                                str4 = str2;
                                map15 = map8;
                            }
                            map8 = map15;
                            str2 = str4;
                            hashSet = hashSet4;
                            map5 = arrayMap4;
                            map3 = map6;
                            map15 = map7;
                            map4 = map8;
                        } else {
                            zzgg().zzir().zzg("Skipping failed audience ID", Integer.valueOf(i));
                            hashSet = hashSet4;
                        }
                    }
                    arrayMap3 = map15;
                    str2 = str4;
                    map10 = map3;
                    map8 = map4;
                    hashSet3 = hashSet;
                    map9 = map5;
                    l = l2;
                    j = j2;
                    i7 = i4 + 1;
                    com_google_android_gms_internal_measurement_zzkiArr2 = com_google_android_gms_internal_measurement_zzkiArr;
                    str7 = str2;
                    length = i2;
                    arrayMap7 = arrayMap3;
                    map2 = map8;
                    map = map10;
                    arrayMap4 = map9;
                    com_google_android_gms_internal_measurement_zzknArr2 = com_google_android_gms_internal_measurement_zzknArr;
                } else {
                    i4 = i7;
                    arrayMap2 = arrayMap7;
                    i2 = length;
                    com_google_android_gms_internal_measurement_zzkjArr2 = com_google_android_gms_internal_measurement_zzkjArr5;
                }
                j3 = 0;
                str3 = str10;
                j2 = j;
                l2 = l;
                zze = zzga().zze(str7, com_google_android_gms_internal_measurement_zzki2.name);
                if (zze != null) {
                    zzgg().zzin().zze("Event aggregate wasn't created during raw event logging. appId, event", zzfg.zzbh(str), zzgb().zzbe(str3));
                    map3 = map;
                    map4 = map2;
                    hashSet = hashSet3;
                    map5 = arrayMap4;
                    com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr2;
                    j4 = j3;
                    str8 = str3;
                    zze = new zzeq(str7, com_google_android_gms_internal_measurement_zzki2.name, 1, 1, com_google_android_gms_internal_measurement_zzki2.zzasw.longValue(), 0, null, null, null);
                } else {
                    hashSet = hashSet3;
                    map5 = arrayMap4;
                    com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr2;
                    str8 = str3;
                    map3 = map;
                    map4 = map2;
                    j4 = j3;
                    zze = zze.zzie();
                }
                zzga().zza(zze);
                j5 = zze.zzafp;
                map15 = arrayMap2;
                str10 = str8;
                map16 = (Map) map15.get(str10);
                if (map16 != null) {
                    str4 = str;
                    map16 = zzga().zzj(str4, str10);
                    if (map16 == null) {
                        map16 = new ArrayMap();
                    }
                    map15.put(str10, map16);
                } else {
                    str4 = str;
                }
                it4 = map16.keySet().iterator();
                while (it4.hasNext()) {
                    i = ((Integer) it4.next()).intValue();
                    hashSet4 = hashSet;
                    if (hashSet4.contains(Integer.valueOf(i))) {
                        zzgg().zzir().zzg("Skipping failed audience ID", Integer.valueOf(i));
                        hashSet = hashSet4;
                    } else {
                        arrayMap4 = map5;
                        map6 = map3;
                        bitSet = (BitSet) map6.get(Integer.valueOf(i));
                        map7 = map15;
                        map15 = map4;
                        bitSet2 = (BitSet) map15.get(Integer.valueOf(i));
                        if (((zzkh) arrayMap4.get(Integer.valueOf(i))) == null) {
                            com_google_android_gms_internal_measurement_zzkh2 = new zzkh();
                            arrayMap4.put(Integer.valueOf(i), com_google_android_gms_internal_measurement_zzkh2);
                            com_google_android_gms_internal_measurement_zzkh2.zzast = Boolean.valueOf(true);
                            bitSet5 = new BitSet();
                            map6.put(Integer.valueOf(i), bitSet5);
                            bitSet2 = new BitSet();
                            map15.put(Integer.valueOf(i), bitSet2);
                            bitSet = bitSet5;
                        }
                        it = ((List) map16.get(Integer.valueOf(i))).iterator();
                        while (it.hasNext()) {
                            map17 = map16;
                            com_google_android_gms_internal_measurement_zzjz = (zzjz) it.next();
                            it5 = it4;
                            it6 = it;
                            if (zzgg().isLoggable(2)) {
                                map8 = map15;
                                map10 = map6;
                                map9 = arrayMap4;
                                zzgg().zzir().zzd("Evaluating filter. audience, filter, event", Integer.valueOf(i), com_google_android_gms_internal_measurement_zzjz.zzark, zzgb().zzbe(com_google_android_gms_internal_measurement_zzjz.zzarl));
                                zzgg().zzir().zzg("Filter definition", zzgb().zza(com_google_android_gms_internal_measurement_zzjz));
                            } else {
                                map8 = map15;
                                map9 = arrayMap4;
                                map10 = map6;
                            }
                            if (com_google_android_gms_internal_measurement_zzjz.zzark != null) {
                                if (com_google_android_gms_internal_measurement_zzjz.zzark.intValue() > 256) {
                                    if (bitSet.get(com_google_android_gms_internal_measurement_zzjz.zzark.intValue())) {
                                        zzgg().zzir().zze("Event filter already evaluated true. audience ID, filter ID", Integer.valueOf(i), com_google_android_gms_internal_measurement_zzjz.zzark);
                                        map16 = map17;
                                        it4 = it5;
                                        it = it6;
                                        map15 = map8;
                                        map6 = map10;
                                        arrayMap4 = map9;
                                    } else {
                                        if (com_google_android_gms_internal_measurement_zzjz.zzaro != null) {
                                            zza = zza(j5, com_google_android_gms_internal_measurement_zzjz.zzaro);
                                            if (zza != null) {
                                                if (zza.booleanValue()) {
                                                    j6 = j5;
                                                    valueOf = Boolean.valueOf(false);
                                                    com_google_android_gms_internal_measurement_zzkjArr4 = com_google_android_gms_internal_measurement_zzkjArr3;
                                                    if (valueOf != null) {
                                                    }
                                                    zzgg().zzir().zzg("Event filter result", valueOf != null ? "null" : valueOf);
                                                    if (valueOf != null) {
                                                        hashSet4.add(Integer.valueOf(i));
                                                    } else {
                                                        bitSet2.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                        if (valueOf.booleanValue()) {
                                                            bitSet.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                        }
                                                    }
                                                    com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr4;
                                                    map16 = map17;
                                                    it4 = it5;
                                                    it = it6;
                                                    map15 = map8;
                                                    map6 = map10;
                                                    arrayMap4 = map9;
                                                    j5 = j6;
                                                }
                                            }
                                            j6 = j5;
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
                                                hashSet4.add(Integer.valueOf(i));
                                            }
                                            com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr4;
                                            map16 = map17;
                                            it4 = it5;
                                            it = it6;
                                            map15 = map8;
                                            map6 = map10;
                                            arrayMap4 = map9;
                                            j5 = j6;
                                        }
                                        hashSet2 = new HashSet();
                                        for (i3 = 0; i3 < i6; i3++) {
                                            if (TextUtils.isEmpty(com_google_android_gms_internal_measurement_zzka2.zzart)) {
                                                zzgg().zzin().zzg("null or empty param name in filter. event", zzgb().zzbe(str10));
                                                j6 = j5;
                                                com_google_android_gms_internal_measurement_zzkjArr4 = com_google_android_gms_internal_measurement_zzkjArr3;
                                                break;
                                            }
                                            hashSet2.add(com_google_android_gms_internal_measurement_zzka2.zzart);
                                        }
                                        map15 = new ArrayMap();
                                        com_google_android_gms_internal_measurement_zzkjArr4 = com_google_android_gms_internal_measurement_zzkjArr3;
                                        length2 = com_google_android_gms_internal_measurement_zzkjArr4.length;
                                        i6 = 0;
                                        while (i6 < length2) {
                                            com_google_android_gms_internal_measurement_zzkj2 = com_google_android_gms_internal_measurement_zzkjArr4[i6];
                                            j6 = j5;
                                            if (!hashSet2.contains(com_google_android_gms_internal_measurement_zzkj2.name)) {
                                                if (com_google_android_gms_internal_measurement_zzkj2.zzasz == null) {
                                                    if (com_google_android_gms_internal_measurement_zzkj2.zzaqx == null) {
                                                        if (com_google_android_gms_internal_measurement_zzkj2.zzajf != null) {
                                                            zzin = zzgg().zzin();
                                                            str9 = "Unknown value for param. event, param";
                                                            zzbe = zzgb().zzbe(str10);
                                                            zzbf = zzgb().zzbf(com_google_android_gms_internal_measurement_zzkj2.name);
                                                            break;
                                                        }
                                                        obj2 = com_google_android_gms_internal_measurement_zzkj2.name;
                                                        obj = com_google_android_gms_internal_measurement_zzkj2.zzajf;
                                                    } else {
                                                        obj2 = com_google_android_gms_internal_measurement_zzkj2.name;
                                                        obj = com_google_android_gms_internal_measurement_zzkj2.zzaqx;
                                                    }
                                                } else {
                                                    obj2 = com_google_android_gms_internal_measurement_zzkj2.name;
                                                    obj = com_google_android_gms_internal_measurement_zzkj2.zzasz;
                                                }
                                                map15.put(obj2, obj);
                                            }
                                            i6++;
                                            j5 = j6;
                                        }
                                        j6 = j5;
                                        com_google_android_gms_internal_measurement_zzkaArr = com_google_android_gms_internal_measurement_zzjz.zzarm;
                                        length3 = com_google_android_gms_internal_measurement_zzkaArr.length;
                                        i8 = 0;
                                        while (i8 < length3) {
                                            com_google_android_gms_internal_measurement_zzka = com_google_android_gms_internal_measurement_zzkaArr[i8];
                                            equals = Boolean.TRUE.equals(com_google_android_gms_internal_measurement_zzka.zzars);
                                            str3 = com_google_android_gms_internal_measurement_zzka.zzart;
                                            if (TextUtils.isEmpty(str3)) {
                                                zzgg().zzin().zzg("Event has empty param name. event", zzgb().zzbe(str10));
                                            } else {
                                                com_google_android_gms_internal_measurement_zzkaArr2 = com_google_android_gms_internal_measurement_zzkaArr;
                                                obj2 = map15.get(str3);
                                                i9 = length3;
                                                if (obj2 instanceof Long) {
                                                    if (com_google_android_gms_internal_measurement_zzka.zzarr != null) {
                                                        zzin = zzgg().zzin();
                                                        str9 = "No number filter for long param. event, param";
                                                    } else {
                                                        zza2 = zza(((Long) obj2).longValue(), com_google_android_gms_internal_measurement_zzka.zzarr);
                                                        if (zza2 == null) {
                                                            if (((zza2.booleanValue() ^ 1) ^ equals) != 0) {
                                                                z = false;
                                                                break;
                                                            }
                                                            i8++;
                                                            com_google_android_gms_internal_measurement_zzkaArr = com_google_android_gms_internal_measurement_zzkaArr2;
                                                            length3 = i9;
                                                        }
                                                    }
                                                } else if (obj2 instanceof Double) {
                                                    if (com_google_android_gms_internal_measurement_zzka.zzarr != null) {
                                                        zzin = zzgg().zzin();
                                                        str9 = "No number filter for double param. event, param";
                                                    } else {
                                                        zza2 = zza(((Double) obj2).doubleValue(), com_google_android_gms_internal_measurement_zzka.zzarr);
                                                        if (zza2 == null) {
                                                            if (((zza2.booleanValue() ^ 1) ^ equals) != 0) {
                                                                z = false;
                                                                break;
                                                            }
                                                            i8++;
                                                            com_google_android_gms_internal_measurement_zzkaArr = com_google_android_gms_internal_measurement_zzkaArr2;
                                                            length3 = i9;
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
                                                            str9 = "Invalid param value for number filter. event, param";
                                                        }
                                                    } else {
                                                        zzin = zzgg().zzin();
                                                        str9 = "No filter for String param. event, param";
                                                    }
                                                    if (zza2 == null) {
                                                        if (((zza2.booleanValue() ^ 1) ^ equals) != 0) {
                                                            z = false;
                                                            break;
                                                        }
                                                        i8++;
                                                        com_google_android_gms_internal_measurement_zzkaArr = com_google_android_gms_internal_measurement_zzkaArr2;
                                                        length3 = i9;
                                                    }
                                                } else if (obj2 == null) {
                                                    zzgg().zzir().zze("Missing param for filter. event, param", zzgb().zzbe(str10), zzgb().zzbf(str3));
                                                    z = false;
                                                    break;
                                                } else {
                                                    zzin = zzgg().zzin();
                                                    str9 = "Unknown param type. event, param";
                                                }
                                                zzbe = zzgb().zzbe(str10);
                                                zzbf = zzgb().zzbf(str3);
                                                zzin.zze(str9, zzbe, zzbf);
                                            }
                                            valueOf = null;
                                        }
                                        z = true;
                                        valueOf = Boolean.valueOf(z);
                                        if (valueOf != null) {
                                        }
                                        zzgg().zzir().zzg("Event filter result", valueOf != null ? "null" : valueOf);
                                        if (valueOf != null) {
                                            hashSet4.add(Integer.valueOf(i));
                                        } else {
                                            bitSet2.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                            if (valueOf.booleanValue()) {
                                                bitSet.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                            }
                                        }
                                        com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr4;
                                        map16 = map17;
                                        it4 = it5;
                                        it = it6;
                                        map15 = map8;
                                        map6 = map10;
                                        arrayMap4 = map9;
                                        j5 = j6;
                                    }
                                    str4 = str;
                                }
                            }
                            j6 = j5;
                            com_google_android_gms_internal_measurement_zzkjArr4 = com_google_android_gms_internal_measurement_zzkjArr3;
                            str2 = str;
                            zzgg().zzin().zze("Invalid event filter ID. appId, id", zzfg.zzbh(str), String.valueOf(com_google_android_gms_internal_measurement_zzjz.zzark));
                            com_google_android_gms_internal_measurement_zzkjArr3 = com_google_android_gms_internal_measurement_zzkjArr4;
                            map16 = map17;
                            it4 = it5;
                            it = it6;
                            map6 = map10;
                            arrayMap4 = map9;
                            j5 = j6;
                            str4 = str2;
                            map15 = map8;
                        }
                        map8 = map15;
                        str2 = str4;
                        hashSet = hashSet4;
                        map5 = arrayMap4;
                        map3 = map6;
                        map15 = map7;
                        map4 = map8;
                    }
                }
                arrayMap3 = map15;
                str2 = str4;
                map10 = map3;
                map8 = map4;
                hashSet3 = hashSet;
                map9 = map5;
                l = l2;
                j = j2;
                i7 = i4 + 1;
                com_google_android_gms_internal_measurement_zzkiArr2 = com_google_android_gms_internal_measurement_zzkiArr;
                str7 = str2;
                length = i2;
                arrayMap7 = arrayMap3;
                map2 = map8;
                map = map10;
                arrayMap4 = map9;
                com_google_android_gms_internal_measurement_zzknArr2 = com_google_android_gms_internal_measurement_zzknArr;
            }
        }
        map9 = arrayMap4;
        str2 = str7;
        map10 = map;
        map8 = map2;
        zzkn[] com_google_android_gms_internal_measurement_zzknArr3 = com_google_android_gms_internal_measurement_zzknArr2;
        if (com_google_android_gms_internal_measurement_zzknArr3 != null) {
            Map arrayMap8 = new ArrayMap();
            i5 = com_google_android_gms_internal_measurement_zzknArr3.length;
            length = 0;
            while (length < i5) {
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
                it = map20.keySet().iterator();
                while (it.hasNext()) {
                    intValue2 = ((Integer) it.next()).intValue();
                    if (hashSet3.contains(Integer.valueOf(intValue2))) {
                        zzgg().zzir().zzg("Skipping failed audience ID", Integer.valueOf(intValue2));
                    } else {
                        Map map21;
                        map11 = map9;
                        Map map22 = map10;
                        BitSet bitSet6 = (BitSet) map22.get(Integer.valueOf(intValue2));
                        map18 = arrayMap8;
                        arrayMap8 = map8;
                        BitSet bitSet7 = (BitSet) arrayMap8.get(Integer.valueOf(intValue2));
                        if (((zzkh) map11.get(Integer.valueOf(intValue2))) == null) {
                            com_google_android_gms_internal_measurement_zzkh = new zzkh();
                            map11.put(Integer.valueOf(intValue2), com_google_android_gms_internal_measurement_zzkh);
                            com_google_android_gms_internal_measurement_zzkh.zzast = Boolean.valueOf(true);
                            bitSet6 = new BitSet();
                            map22.put(Integer.valueOf(intValue2), bitSet6);
                            bitSet7 = new BitSet();
                            arrayMap8.put(Integer.valueOf(intValue2), bitSet7);
                        }
                        Iterator it7 = ((List) map20.get(Integer.valueOf(intValue2))).iterator();
                        while (it7.hasNext()) {
                            Iterator it8;
                            i12 = i5;
                            zzkc com_google_android_gms_internal_measurement_zzkc = (zzkc) it7.next();
                            map21 = map20;
                            Iterator it9 = it;
                            if (zzgg().isLoggable(2)) {
                                it8 = it7;
                                map12 = arrayMap8;
                                map13 = map11;
                                map19 = map22;
                                zzgg().zzir().zzd("Evaluating filter. audience, filter, property", Integer.valueOf(intValue2), com_google_android_gms_internal_measurement_zzkc.zzark, zzgb().zzbg(com_google_android_gms_internal_measurement_zzkc.zzasa));
                                zzgg().zzir().zzg("Filter definition", zzgb().zza(com_google_android_gms_internal_measurement_zzkc));
                            } else {
                                map12 = arrayMap8;
                                it8 = it7;
                                map13 = map11;
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
                                                    hashSet3.add(Integer.valueOf(intValue2));
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
                                                hashSet3.add(Integer.valueOf(intValue2));
                                            }
                                        }
                                        zzin2.zzg(str11, zzgb().zzbg(com_google_android_gms_internal_measurement_zzkn.name));
                                        valueOf = null;
                                        if (valueOf == null) {
                                        }
                                        zzgg().zzir().zzg("Property filter result", valueOf == null ? "null" : valueOf);
                                        if (valueOf == null) {
                                            hashSet3.add(Integer.valueOf(intValue2));
                                        } else {
                                            bitSet7.set(com_google_android_gms_internal_measurement_zzkc.zzark.intValue());
                                            if (!valueOf.booleanValue()) {
                                                bitSet6.set(com_google_android_gms_internal_measurement_zzkc.zzark.intValue());
                                            }
                                        }
                                    }
                                    i5 = i12;
                                    map20 = map21;
                                    it = it9;
                                    it7 = it8;
                                    arrayMap8 = map12;
                                    map11 = map13;
                                    map22 = map19;
                                }
                            }
                            zzgg().zzin().zze("Invalid property filter ID. appId, id", zzfg.zzbh(str), String.valueOf(com_google_android_gms_internal_measurement_zzkc.zzark));
                            hashSet3.add(Integer.valueOf(intValue2));
                            arrayMap8 = map18;
                            i5 = i12;
                            map20 = map21;
                            it = it9;
                            map8 = map12;
                            map9 = map13;
                            map10 = map19;
                        }
                        map21 = map20;
                        map8 = arrayMap8;
                        map9 = map11;
                        map10 = map22;
                        arrayMap8 = map18;
                        com_google_android_gms_internal_measurement_zzknArr3 = com_google_android_gms_internal_measurement_zzknArr;
                    }
                }
                map18 = arrayMap8;
                i12 = i5;
                map12 = map8;
                map19 = map10;
                map13 = map9;
                length++;
                com_google_android_gms_internal_measurement_zzknArr3 = com_google_android_gms_internal_measurement_zzknArr;
            }
        }
        map12 = map8;
        map13 = map9;
        zzba = map10;
        zzkh[] com_google_android_gms_internal_measurement_zzkhArr = new zzkh[zzba.size()];
        i = 0;
        for (Integer intValue3 : zzba.keySet()) {
            length = intValue3.intValue();
            if (!hashSet3.contains(Integer.valueOf(length))) {
                arrayMap5 = map13;
                zzkh com_google_android_gms_internal_measurement_zzkh3 = (zzkh) arrayMap5.get(Integer.valueOf(length));
                if (com_google_android_gms_internal_measurement_zzkh3 == null) {
                    com_google_android_gms_internal_measurement_zzkh3 = new zzkh();
                }
                intValue2 = i + 1;
                com_google_android_gms_internal_measurement_zzkhArr[i] = com_google_android_gms_internal_measurement_zzkh3;
                com_google_android_gms_internal_measurement_zzkh3.zzarg = Integer.valueOf(length);
                com_google_android_gms_internal_measurement_zzkh3.zzasr = new zzkm();
                com_google_android_gms_internal_measurement_zzkh3.zzasr.zzaug = zzjv.zza((BitSet) zzba.get(Integer.valueOf(length)));
                map11 = map12;
                com_google_android_gms_internal_measurement_zzkh3.zzasr.zzauf = zzjv.zza((BitSet) map11.get(Integer.valueOf(length)));
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
                            map13 = arrayMap5;
                            i = intValue2;
                            map12 = map11;
                        }
                    } catch (SQLiteException e6) {
                        e = e6;
                        obj3 = e;
                        zzil = zzga2.zzgg().zzil();
                        str6 = "Error storing filter results. appId";
                        zzil.zze(str6, zzfg.zzbh(str), obj3);
                        map13 = arrayMap5;
                        i = intValue2;
                        map12 = map11;
                    }
                } catch (IOException e7) {
                    obj3 = e7;
                    zzil = zzga2.zzgg().zzil();
                    str6 = "Configuration loss. Failed to serialize filter results. appId";
                    zzil.zze(str6, zzfg.zzbh(str), obj3);
                    map13 = arrayMap5;
                    i = intValue2;
                    map12 = map11;
                }
                map13 = arrayMap5;
                i = intValue2;
                map12 = map11;
            }
        }
        return (zzkh[]) Arrays.copyOf(com_google_android_gms_internal_measurement_zzkhArr, i);
    }

    protected final boolean zzhh() {
        return false;
    }
}
