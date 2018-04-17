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
        Long l;
        zzki com_google_android_gms_internal_measurement_zzki;
        zzhj zzga;
        String str2;
        ArrayMap arrayMap;
        zzkj[] com_google_android_gms_internal_measurement_zzkjArr;
        int i2;
        SQLiteException e;
        zzkj[] com_google_android_gms_internal_measurement_zzkjArr2;
        zzkj[] com_google_android_gms_internal_measurement_zzkjArr3;
        zzkj[] com_google_android_gms_internal_measurement_zzkjArr4;
        int i3;
        Map map3;
        Map map4;
        BitSet bitSet;
        zzkh com_google_android_gms_internal_measurement_zzkh;
        BitSet bitSet2;
        Iterator it;
        Map map5;
        Map map6;
        Map map7;
        Boolean zza;
        Boolean valueOf;
        Set hashSet;
        int i4;
        zzfi zzin;
        String str3;
        Object zzbe;
        Object zzbf;
        zzka com_google_android_gms_internal_measurement_zzka;
        boolean equals;
        zzka[] com_google_android_gms_internal_measurement_zzkaArr;
        int i5;
        Boolean zza2;
        int intValue2;
        Map map8;
        Map map9;
        Map map10;
        Object obj2;
        zzfi zzil;
        String str4;
        zzee com_google_android_gms_internal_measurement_zzee = this;
        String str5 = str;
        zzki[] com_google_android_gms_internal_measurement_zzkiArr2 = com_google_android_gms_internal_measurement_zzkiArr;
        zzkn[] com_google_android_gms_internal_measurement_zzknArr2 = com_google_android_gms_internal_measurement_zzknArr;
        Preconditions.checkNotEmpty(str);
        Set hashSet2 = new HashSet();
        Map arrayMap2 = new ArrayMap();
        Map arrayMap3 = new ArrayMap();
        Map arrayMap4 = new ArrayMap();
        Map zzba = zzga().zzba(str5);
        if (zzba != null) {
            Iterator it2 = zzba.keySet().iterator();
            while (it2.hasNext()) {
                Iterator it3;
                intValue = ((Integer) it2.next()).intValue();
                zzkm com_google_android_gms_internal_measurement_zzkm = (zzkm) zzba.get(Integer.valueOf(intValue));
                BitSet bitSet3 = (BitSet) arrayMap3.get(Integer.valueOf(intValue));
                BitSet bitSet4 = (BitSet) arrayMap4.get(Integer.valueOf(intValue));
                if (bitSet3 == null) {
                    bitSet3 = new BitSet();
                    arrayMap3.put(Integer.valueOf(intValue), bitSet3);
                    bitSet4 = new BitSet();
                    arrayMap4.put(Integer.valueOf(intValue), bitSet4);
                }
                Map map11 = zzba;
                i = 0;
                while (i < (com_google_android_gms_internal_measurement_zzkm.zzauf.length << 6)) {
                    if (zzjv.zza(com_google_android_gms_internal_measurement_zzkm.zzauf, i)) {
                        it3 = it2;
                        map = arrayMap3;
                        map2 = arrayMap4;
                        zzgg().zzir().zze("Filter already evaluated. audience ID, filter ID", Integer.valueOf(intValue), Integer.valueOf(i));
                        bitSet4.set(i);
                        if (zzjv.zza(com_google_android_gms_internal_measurement_zzkm.zzaug, i)) {
                            bitSet3.set(i);
                        }
                    } else {
                        it3 = it2;
                        map = arrayMap3;
                        map2 = arrayMap4;
                    }
                    i++;
                    it2 = it3;
                    arrayMap3 = map;
                    arrayMap4 = map2;
                }
                it3 = it2;
                map = arrayMap3;
                map2 = arrayMap4;
                zzkh com_google_android_gms_internal_measurement_zzkh2 = new zzkh();
                arrayMap2.put(Integer.valueOf(intValue), com_google_android_gms_internal_measurement_zzkh2);
                com_google_android_gms_internal_measurement_zzkh2.zzast = Boolean.valueOf(false);
                com_google_android_gms_internal_measurement_zzkh2.zzass = com_google_android_gms_internal_measurement_zzkm;
                com_google_android_gms_internal_measurement_zzkh2.zzasr = new zzkm();
                com_google_android_gms_internal_measurement_zzkh2.zzasr.zzaug = zzjv.zza(bitSet3);
                com_google_android_gms_internal_measurement_zzkh2.zzasr.zzauf = zzjv.zza(bitSet4);
                zzba = map11;
                it2 = it3;
            }
        }
        map = arrayMap3;
        map2 = arrayMap4;
        if (com_google_android_gms_internal_measurement_zzkiArr2 != null) {
            ArrayMap arrayMap5 = new ArrayMap();
            length = com_google_android_gms_internal_measurement_zzkiArr2.length;
            int i6 = 0;
            Long l2 = null;
            zzki com_google_android_gms_internal_measurement_zzki2 = null;
            long j = 0;
            while (i6 < length) {
                Object obj3;
                long j2;
                int i7;
                int i8;
                int i9;
                int i10;
                ArrayMap arrayMap6;
                int length2;
                String str6;
                Long l3;
                long j3;
                zzeq zze;
                Map map12;
                Map map13;
                HashSet hashSet3;
                Map map14;
                zzkj[] com_google_android_gms_internal_measurement_zzkjArr5;
                long j4;
                String str7;
                long j5;
                Map map15;
                Map map16;
                String str8;
                Iterator it4;
                HashSet hashSet4;
                BitSet bitSet5;
                Map map17;
                zzjz com_google_android_gms_internal_measurement_zzjz;
                Iterator it5;
                Iterator it6;
                long j6;
                zzkj[] com_google_android_gms_internal_measurement_zzkjArr6;
                int length3;
                zzkj com_google_android_gms_internal_measurement_zzkj;
                zzka[] com_google_android_gms_internal_measurement_zzkaArr2;
                String str9;
                boolean z;
                ArrayMap arrayMap7;
                zzki com_google_android_gms_internal_measurement_zzki3 = com_google_android_gms_internal_measurement_zzkiArr2[i6];
                String str10 = com_google_android_gms_internal_measurement_zzki3.name;
                zzkj[] com_google_android_gms_internal_measurement_zzkjArr7 = com_google_android_gms_internal_measurement_zzki3.zzasv;
                if (zzgi().zzd(str5, zzew.zzahy)) {
                    int i11;
                    zzki com_google_android_gms_internal_measurement_zzki4;
                    SQLiteDatabase writableDatabase;
                    String[] strArr;
                    zzkj com_google_android_gms_internal_measurement_zzkj2;
                    long j7;
                    Pair zza3;
                    zzki com_google_android_gms_internal_measurement_zzki5;
                    Long valueOf2;
                    Long l4;
                    zzgc();
                    Long l5 = (Long) zzjv.zzb(com_google_android_gms_internal_measurement_zzki3, "_eid");
                    obj3 = l5 != null ? 1 : null;
                    if (obj3 != null) {
                        i11 = i6;
                        if (str10.equals("_ep")) {
                            obj = 1;
                            if (obj == null) {
                                zzgc();
                                str10 = (String) zzjv.zzb(com_google_android_gms_internal_measurement_zzki3, "_en");
                                if (!TextUtils.isEmpty(str10)) {
                                    if (!(com_google_android_gms_internal_measurement_zzki2 == null || l2 == null)) {
                                        if (l5.longValue() != l2.longValue()) {
                                            com_google_android_gms_internal_measurement_zzki4 = com_google_android_gms_internal_measurement_zzki2;
                                            l = l2;
                                            com_google_android_gms_internal_measurement_zzki = com_google_android_gms_internal_measurement_zzki4;
                                            j2 = j - 1;
                                            if (j2 > 0) {
                                                zzga = zzga();
                                                zzga.zzab();
                                                zzga.zzgg().zzir().zzg("Clearing complex main event info. appId", str5);
                                                try {
                                                    writableDatabase = zzga.getWritableDatabase();
                                                    str2 = "delete from main_event_params where app_id=?";
                                                    arrayMap = arrayMap5;
                                                    com_google_android_gms_internal_measurement_zzkjArr = com_google_android_gms_internal_measurement_zzkjArr7;
                                                    i7 = 1;
                                                    try {
                                                        strArr = new String[1];
                                                        i2 = 0;
                                                    } catch (SQLiteException e2) {
                                                        e = e2;
                                                        i2 = 0;
                                                        zzga.zzgg().zzil().zzg("Error clearing complex main event", e);
                                                        i8 = length;
                                                        i9 = i7;
                                                        i10 = i11;
                                                        com_google_android_gms_internal_measurement_zzkjArr2 = com_google_android_gms_internal_measurement_zzkjArr;
                                                        arrayMap6 = arrayMap;
                                                        com_google_android_gms_internal_measurement_zzkjArr3 = new zzkj[(com_google_android_gms_internal_measurement_zzki.zzasv.length + com_google_android_gms_internal_measurement_zzkjArr2.length)];
                                                        com_google_android_gms_internal_measurement_zzkjArr4 = com_google_android_gms_internal_measurement_zzki.zzasv;
                                                        intValue = com_google_android_gms_internal_measurement_zzkjArr4.length;
                                                        i3 = i2;
                                                        length = i3;
                                                        while (i3 < intValue) {
                                                            com_google_android_gms_internal_measurement_zzkj2 = com_google_android_gms_internal_measurement_zzkjArr4[i3];
                                                            zzgc();
                                                            if (zzjv.zza(com_google_android_gms_internal_measurement_zzki3, com_google_android_gms_internal_measurement_zzkj2.name) != null) {
                                                                i9 = length + 1;
                                                                com_google_android_gms_internal_measurement_zzkjArr3[length] = com_google_android_gms_internal_measurement_zzkj2;
                                                                length = i9;
                                                            }
                                                            i3++;
                                                        }
                                                        if (length > 0) {
                                                            zzgg().zzin().zzg("No unique parameters in main event. eventName", str10);
                                                        } else {
                                                            length2 = com_google_android_gms_internal_measurement_zzkjArr2.length;
                                                            intValue = i2;
                                                            while (intValue < length2) {
                                                                i7 = length + 1;
                                                                com_google_android_gms_internal_measurement_zzkjArr3[length] = com_google_android_gms_internal_measurement_zzkjArr2[intValue];
                                                                intValue++;
                                                                length = i7;
                                                            }
                                                            if (length != com_google_android_gms_internal_measurement_zzkjArr3.length) {
                                                                com_google_android_gms_internal_measurement_zzkjArr3 = (zzkj[]) Arrays.copyOf(com_google_android_gms_internal_measurement_zzkjArr3, length);
                                                            }
                                                            com_google_android_gms_internal_measurement_zzkjArr2 = com_google_android_gms_internal_measurement_zzkjArr3;
                                                        }
                                                        str6 = str10;
                                                        l3 = l;
                                                        j3 = 0;
                                                        com_google_android_gms_internal_measurement_zzki2 = com_google_android_gms_internal_measurement_zzki;
                                                        zze = zzga().zze(str5, com_google_android_gms_internal_measurement_zzki3.name);
                                                        if (zze != null) {
                                                            zzgg().zzin().zze("Event aggregate wasn't created during raw event logging. appId, event", zzfg.zzbh(str), zzgb().zzbe(str6));
                                                            map12 = map;
                                                            map13 = map2;
                                                            hashSet3 = hashSet2;
                                                            map14 = arrayMap2;
                                                            com_google_android_gms_internal_measurement_zzkjArr5 = com_google_android_gms_internal_measurement_zzkjArr2;
                                                            j4 = j3;
                                                            str7 = str6;
                                                            zze = new zzeq(str5, com_google_android_gms_internal_measurement_zzki3.name, 1, 1, com_google_android_gms_internal_measurement_zzki3.zzasw.longValue(), 0, null, null, null);
                                                        } else {
                                                            hashSet3 = hashSet2;
                                                            map14 = arrayMap2;
                                                            com_google_android_gms_internal_measurement_zzkjArr5 = com_google_android_gms_internal_measurement_zzkjArr2;
                                                            str7 = str6;
                                                            map12 = map;
                                                            map13 = map2;
                                                            j4 = j3;
                                                            zze = zze.zzie();
                                                        }
                                                        zzga().zza(zze);
                                                        j5 = zze.zzafp;
                                                        map15 = arrayMap6;
                                                        str10 = str7;
                                                        map16 = (Map) map15.get(str10);
                                                        if (map16 != null) {
                                                            str8 = str;
                                                            map16 = zzga().zzj(str8, str10);
                                                            if (map16 == null) {
                                                                map16 = new ArrayMap();
                                                            }
                                                            map15.put(str10, map16);
                                                        } else {
                                                            str8 = str;
                                                        }
                                                        it4 = map16.keySet().iterator();
                                                        while (it4.hasNext()) {
                                                            i = ((Integer) it4.next()).intValue();
                                                            hashSet4 = hashSet3;
                                                            if (hashSet4.contains(Integer.valueOf(i))) {
                                                                arrayMap2 = map14;
                                                                map3 = map12;
                                                                bitSet5 = (BitSet) map3.get(Integer.valueOf(i));
                                                                map4 = map15;
                                                                map15 = map13;
                                                                bitSet = (BitSet) map15.get(Integer.valueOf(i));
                                                                if (((zzkh) arrayMap2.get(Integer.valueOf(i))) == null) {
                                                                    com_google_android_gms_internal_measurement_zzkh = new zzkh();
                                                                    arrayMap2.put(Integer.valueOf(i), com_google_android_gms_internal_measurement_zzkh);
                                                                    com_google_android_gms_internal_measurement_zzkh.zzast = Boolean.valueOf(true);
                                                                    bitSet2 = new BitSet();
                                                                    map3.put(Integer.valueOf(i), bitSet2);
                                                                    bitSet = new BitSet();
                                                                    map15.put(Integer.valueOf(i), bitSet);
                                                                    bitSet5 = bitSet2;
                                                                }
                                                                it = ((List) map16.get(Integer.valueOf(i))).iterator();
                                                                while (it.hasNext()) {
                                                                    map17 = map16;
                                                                    com_google_android_gms_internal_measurement_zzjz = (zzjz) it.next();
                                                                    it5 = it4;
                                                                    it6 = it;
                                                                    if (zzgg().isLoggable(2)) {
                                                                        map5 = map15;
                                                                        map6 = arrayMap2;
                                                                        map7 = map3;
                                                                    } else {
                                                                        map5 = map15;
                                                                        map7 = map3;
                                                                        map6 = arrayMap2;
                                                                        zzgg().zzir().zzd("Evaluating filter. audience, filter, event", Integer.valueOf(i), com_google_android_gms_internal_measurement_zzjz.zzark, zzgb().zzbe(com_google_android_gms_internal_measurement_zzjz.zzarl));
                                                                        zzgg().zzir().zzg("Filter definition", zzgb().zza(com_google_android_gms_internal_measurement_zzjz));
                                                                    }
                                                                    if (com_google_android_gms_internal_measurement_zzjz.zzark != null) {
                                                                        if (com_google_android_gms_internal_measurement_zzjz.zzark.intValue() > 256) {
                                                                            if (bitSet5.get(com_google_android_gms_internal_measurement_zzjz.zzark.intValue())) {
                                                                                if (com_google_android_gms_internal_measurement_zzjz.zzaro != null) {
                                                                                    zza = zza(j5, com_google_android_gms_internal_measurement_zzjz.zzaro);
                                                                                    if (zza != null) {
                                                                                        if (!zza.booleanValue()) {
                                                                                            j6 = j5;
                                                                                            valueOf = Boolean.valueOf(false);
                                                                                            com_google_android_gms_internal_measurement_zzkjArr6 = com_google_android_gms_internal_measurement_zzkjArr5;
                                                                                            zzgg().zzir().zzg("Event filter result", valueOf != null ? "null" : valueOf);
                                                                                            if (valueOf != null) {
                                                                                                hashSet4.add(Integer.valueOf(i));
                                                                                            } else {
                                                                                                bitSet.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                                                                if (valueOf.booleanValue()) {
                                                                                                    bitSet5.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                                                                }
                                                                                            }
                                                                                            com_google_android_gms_internal_measurement_zzkjArr5 = com_google_android_gms_internal_measurement_zzkjArr6;
                                                                                            map16 = map17;
                                                                                            it4 = it5;
                                                                                            it = it6;
                                                                                            map15 = map5;
                                                                                            map3 = map7;
                                                                                            arrayMap2 = map6;
                                                                                            j5 = j6;
                                                                                        }
                                                                                    }
                                                                                    j6 = j5;
                                                                                    com_google_android_gms_internal_measurement_zzkjArr6 = com_google_android_gms_internal_measurement_zzkjArr5;
                                                                                    valueOf = null;
                                                                                    if (valueOf != null) {
                                                                                    }
                                                                                    zzgg().zzir().zzg("Event filter result", valueOf != null ? "null" : valueOf);
                                                                                    if (valueOf != null) {
                                                                                        bitSet.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                                                        if (valueOf.booleanValue()) {
                                                                                            bitSet5.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                                                        }
                                                                                    } else {
                                                                                        hashSet4.add(Integer.valueOf(i));
                                                                                    }
                                                                                    com_google_android_gms_internal_measurement_zzkjArr5 = com_google_android_gms_internal_measurement_zzkjArr6;
                                                                                    map16 = map17;
                                                                                    it4 = it5;
                                                                                    it = it6;
                                                                                    map15 = map5;
                                                                                    map3 = map7;
                                                                                    arrayMap2 = map6;
                                                                                    j5 = j6;
                                                                                }
                                                                                hashSet = new HashSet();
                                                                                for (zzka com_google_android_gms_internal_measurement_zzka2 : com_google_android_gms_internal_measurement_zzjz.zzarm) {
                                                                                    if (TextUtils.isEmpty(com_google_android_gms_internal_measurement_zzka2.zzart)) {
                                                                                        zzgg().zzin().zzg("null or empty param name in filter. event", zzgb().zzbe(str10));
                                                                                        j6 = j5;
                                                                                        com_google_android_gms_internal_measurement_zzkjArr6 = com_google_android_gms_internal_measurement_zzkjArr5;
                                                                                        break;
                                                                                    }
                                                                                    hashSet.add(com_google_android_gms_internal_measurement_zzka2.zzart);
                                                                                }
                                                                                map15 = new ArrayMap();
                                                                                com_google_android_gms_internal_measurement_zzkjArr6 = com_google_android_gms_internal_measurement_zzkjArr5;
                                                                                length3 = com_google_android_gms_internal_measurement_zzkjArr6.length;
                                                                                i4 = 0;
                                                                                while (i4 < length3) {
                                                                                    com_google_android_gms_internal_measurement_zzkj = com_google_android_gms_internal_measurement_zzkjArr6[i4];
                                                                                    j6 = j5;
                                                                                    if (!hashSet.contains(com_google_android_gms_internal_measurement_zzkj.name)) {
                                                                                        if (com_google_android_gms_internal_measurement_zzkj.zzasz == null) {
                                                                                            if (com_google_android_gms_internal_measurement_zzkj.zzaqx == null) {
                                                                                                if (com_google_android_gms_internal_measurement_zzkj.zzajf != null) {
                                                                                                    zzin = zzgg().zzin();
                                                                                                    str3 = "Unknown value for param. event, param";
                                                                                                    zzbe = zzgb().zzbe(str10);
                                                                                                    zzbf = zzgb().zzbf(com_google_android_gms_internal_measurement_zzkj.name);
                                                                                                    break;
                                                                                                }
                                                                                                obj = com_google_android_gms_internal_measurement_zzkj.name;
                                                                                                obj3 = com_google_android_gms_internal_measurement_zzkj.zzajf;
                                                                                            } else {
                                                                                                obj = com_google_android_gms_internal_measurement_zzkj.name;
                                                                                                obj3 = com_google_android_gms_internal_measurement_zzkj.zzaqx;
                                                                                            }
                                                                                        } else {
                                                                                            obj = com_google_android_gms_internal_measurement_zzkj.name;
                                                                                            obj3 = com_google_android_gms_internal_measurement_zzkj.zzasz;
                                                                                        }
                                                                                        map15.put(obj, obj3);
                                                                                    }
                                                                                    i4++;
                                                                                    j5 = j6;
                                                                                }
                                                                                j6 = j5;
                                                                                com_google_android_gms_internal_measurement_zzkaArr2 = com_google_android_gms_internal_measurement_zzjz.zzarm;
                                                                                length2 = com_google_android_gms_internal_measurement_zzkaArr2.length;
                                                                                i7 = 0;
                                                                                while (i7 < length2) {
                                                                                    com_google_android_gms_internal_measurement_zzka = com_google_android_gms_internal_measurement_zzkaArr2[i7];
                                                                                    equals = Boolean.TRUE.equals(com_google_android_gms_internal_measurement_zzka.zzars);
                                                                                    str6 = com_google_android_gms_internal_measurement_zzka.zzart;
                                                                                    if (TextUtils.isEmpty(str6)) {
                                                                                        com_google_android_gms_internal_measurement_zzkaArr = com_google_android_gms_internal_measurement_zzkaArr2;
                                                                                        obj = map15.get(str6);
                                                                                        i5 = length2;
                                                                                        if (obj instanceof Long) {
                                                                                            if (obj instanceof Double) {
                                                                                                if (!(obj instanceof String)) {
                                                                                                    if (com_google_android_gms_internal_measurement_zzka.zzarq != null) {
                                                                                                        zza2 = zza((String) obj, com_google_android_gms_internal_measurement_zzka.zzarq);
                                                                                                    } else if (com_google_android_gms_internal_measurement_zzka.zzarr == null) {
                                                                                                        str9 = (String) obj;
                                                                                                        if (zzjv.zzcd(str9)) {
                                                                                                            zzin = zzgg().zzin();
                                                                                                            str3 = "Invalid param value for number filter. event, param";
                                                                                                        } else {
                                                                                                            zza2 = zza(str9, com_google_android_gms_internal_measurement_zzka.zzarr);
                                                                                                        }
                                                                                                    } else {
                                                                                                        zzin = zzgg().zzin();
                                                                                                        str3 = "No filter for String param. event, param";
                                                                                                    }
                                                                                                    if (zza2 == null) {
                                                                                                        if (((zza2.booleanValue() ^ 1) ^ equals) != 0) {
                                                                                                            z = false;
                                                                                                            break;
                                                                                                        }
                                                                                                        i7++;
                                                                                                        com_google_android_gms_internal_measurement_zzkaArr2 = com_google_android_gms_internal_measurement_zzkaArr;
                                                                                                        length2 = i5;
                                                                                                    }
                                                                                                } else if (obj == null) {
                                                                                                    zzgg().zzir().zze("Missing param for filter. event, param", zzgb().zzbe(str10), zzgb().zzbf(str6));
                                                                                                    z = false;
                                                                                                    break;
                                                                                                } else {
                                                                                                    zzin = zzgg().zzin();
                                                                                                    str3 = "Unknown param type. event, param";
                                                                                                }
                                                                                            } else if (com_google_android_gms_internal_measurement_zzka.zzarr != null) {
                                                                                                zzin = zzgg().zzin();
                                                                                                str3 = "No number filter for double param. event, param";
                                                                                            } else {
                                                                                                zza2 = zza(((Double) obj).doubleValue(), com_google_android_gms_internal_measurement_zzka.zzarr);
                                                                                                if (zza2 == null) {
                                                                                                    if (((zza2.booleanValue() ^ 1) ^ equals) != 0) {
                                                                                                        z = false;
                                                                                                        break;
                                                                                                    }
                                                                                                    i7++;
                                                                                                    com_google_android_gms_internal_measurement_zzkaArr2 = com_google_android_gms_internal_measurement_zzkaArr;
                                                                                                    length2 = i5;
                                                                                                }
                                                                                            }
                                                                                        } else if (com_google_android_gms_internal_measurement_zzka.zzarr != null) {
                                                                                            zzin = zzgg().zzin();
                                                                                            str3 = "No number filter for long param. event, param";
                                                                                        } else {
                                                                                            zza2 = zza(((Long) obj).longValue(), com_google_android_gms_internal_measurement_zzka.zzarr);
                                                                                            if (zza2 == null) {
                                                                                                if (((zza2.booleanValue() ^ 1) ^ equals) != 0) {
                                                                                                    z = false;
                                                                                                    break;
                                                                                                }
                                                                                                i7++;
                                                                                                com_google_android_gms_internal_measurement_zzkaArr2 = com_google_android_gms_internal_measurement_zzkaArr;
                                                                                                length2 = i5;
                                                                                            }
                                                                                        }
                                                                                        zzbe = zzgb().zzbe(str10);
                                                                                        zzbf = zzgb().zzbf(str6);
                                                                                        zzin.zze(str3, zzbe, zzbf);
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
                                                                                    bitSet.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                                                    if (valueOf.booleanValue()) {
                                                                                        bitSet5.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                                                    }
                                                                                }
                                                                                com_google_android_gms_internal_measurement_zzkjArr5 = com_google_android_gms_internal_measurement_zzkjArr6;
                                                                                map16 = map17;
                                                                                it4 = it5;
                                                                                it = it6;
                                                                                map15 = map5;
                                                                                map3 = map7;
                                                                                arrayMap2 = map6;
                                                                                j5 = j6;
                                                                            } else {
                                                                                zzgg().zzir().zze("Event filter already evaluated true. audience ID, filter ID", Integer.valueOf(i), com_google_android_gms_internal_measurement_zzjz.zzark);
                                                                                map16 = map17;
                                                                                it4 = it5;
                                                                                it = it6;
                                                                                map15 = map5;
                                                                                map3 = map7;
                                                                                arrayMap2 = map6;
                                                                            }
                                                                            str8 = str;
                                                                        }
                                                                    }
                                                                    j6 = j5;
                                                                    com_google_android_gms_internal_measurement_zzkjArr6 = com_google_android_gms_internal_measurement_zzkjArr5;
                                                                    str2 = str;
                                                                    zzgg().zzin().zze("Invalid event filter ID. appId, id", zzfg.zzbh(str), String.valueOf(com_google_android_gms_internal_measurement_zzjz.zzark));
                                                                    com_google_android_gms_internal_measurement_zzkjArr5 = com_google_android_gms_internal_measurement_zzkjArr6;
                                                                    map16 = map17;
                                                                    it4 = it5;
                                                                    it = it6;
                                                                    map3 = map7;
                                                                    arrayMap2 = map6;
                                                                    j5 = j6;
                                                                    str8 = str2;
                                                                    map15 = map5;
                                                                }
                                                                map5 = map15;
                                                                str2 = str8;
                                                                hashSet3 = hashSet4;
                                                                map14 = arrayMap2;
                                                                map12 = map3;
                                                                map15 = map4;
                                                                map13 = map5;
                                                            } else {
                                                                zzgg().zzir().zzg("Skipping failed audience ID", Integer.valueOf(i));
                                                                hashSet3 = hashSet4;
                                                            }
                                                        }
                                                        arrayMap7 = map15;
                                                        str2 = str8;
                                                        map7 = map12;
                                                        map5 = map13;
                                                        hashSet2 = hashSet3;
                                                        map6 = map14;
                                                        l2 = l3;
                                                        j = j2;
                                                        i6 = i10 + 1;
                                                        com_google_android_gms_internal_measurement_zzkiArr2 = com_google_android_gms_internal_measurement_zzkiArr;
                                                        str5 = str2;
                                                        length = i8;
                                                        arrayMap5 = arrayMap7;
                                                        map2 = map5;
                                                        map = map7;
                                                        arrayMap2 = map6;
                                                        com_google_android_gms_internal_measurement_zzknArr2 = com_google_android_gms_internal_measurement_zzknArr;
                                                    }
                                                    try {
                                                        strArr[0] = str5;
                                                        writableDatabase.execSQL(str2, strArr);
                                                    } catch (SQLiteException e3) {
                                                        e = e3;
                                                        zzga.zzgg().zzil().zzg("Error clearing complex main event", e);
                                                        i8 = length;
                                                        i9 = i7;
                                                        i10 = i11;
                                                        com_google_android_gms_internal_measurement_zzkjArr2 = com_google_android_gms_internal_measurement_zzkjArr;
                                                        arrayMap6 = arrayMap;
                                                        com_google_android_gms_internal_measurement_zzkjArr3 = new zzkj[(com_google_android_gms_internal_measurement_zzki.zzasv.length + com_google_android_gms_internal_measurement_zzkjArr2.length)];
                                                        com_google_android_gms_internal_measurement_zzkjArr4 = com_google_android_gms_internal_measurement_zzki.zzasv;
                                                        intValue = com_google_android_gms_internal_measurement_zzkjArr4.length;
                                                        i3 = i2;
                                                        length = i3;
                                                        while (i3 < intValue) {
                                                            com_google_android_gms_internal_measurement_zzkj2 = com_google_android_gms_internal_measurement_zzkjArr4[i3];
                                                            zzgc();
                                                            if (zzjv.zza(com_google_android_gms_internal_measurement_zzki3, com_google_android_gms_internal_measurement_zzkj2.name) != null) {
                                                                i9 = length + 1;
                                                                com_google_android_gms_internal_measurement_zzkjArr3[length] = com_google_android_gms_internal_measurement_zzkj2;
                                                                length = i9;
                                                            }
                                                            i3++;
                                                        }
                                                        if (length > 0) {
                                                            length2 = com_google_android_gms_internal_measurement_zzkjArr2.length;
                                                            intValue = i2;
                                                            while (intValue < length2) {
                                                                i7 = length + 1;
                                                                com_google_android_gms_internal_measurement_zzkjArr3[length] = com_google_android_gms_internal_measurement_zzkjArr2[intValue];
                                                                intValue++;
                                                                length = i7;
                                                            }
                                                            if (length != com_google_android_gms_internal_measurement_zzkjArr3.length) {
                                                                com_google_android_gms_internal_measurement_zzkjArr3 = (zzkj[]) Arrays.copyOf(com_google_android_gms_internal_measurement_zzkjArr3, length);
                                                            }
                                                            com_google_android_gms_internal_measurement_zzkjArr2 = com_google_android_gms_internal_measurement_zzkjArr3;
                                                        } else {
                                                            zzgg().zzin().zzg("No unique parameters in main event. eventName", str10);
                                                        }
                                                        str6 = str10;
                                                        l3 = l;
                                                        j3 = 0;
                                                        com_google_android_gms_internal_measurement_zzki2 = com_google_android_gms_internal_measurement_zzki;
                                                        zze = zzga().zze(str5, com_google_android_gms_internal_measurement_zzki3.name);
                                                        if (zze != null) {
                                                            hashSet3 = hashSet2;
                                                            map14 = arrayMap2;
                                                            com_google_android_gms_internal_measurement_zzkjArr5 = com_google_android_gms_internal_measurement_zzkjArr2;
                                                            str7 = str6;
                                                            map12 = map;
                                                            map13 = map2;
                                                            j4 = j3;
                                                            zze = zze.zzie();
                                                        } else {
                                                            zzgg().zzin().zze("Event aggregate wasn't created during raw event logging. appId, event", zzfg.zzbh(str), zzgb().zzbe(str6));
                                                            map12 = map;
                                                            map13 = map2;
                                                            hashSet3 = hashSet2;
                                                            map14 = arrayMap2;
                                                            com_google_android_gms_internal_measurement_zzkjArr5 = com_google_android_gms_internal_measurement_zzkjArr2;
                                                            j4 = j3;
                                                            str7 = str6;
                                                            zze = new zzeq(str5, com_google_android_gms_internal_measurement_zzki3.name, 1, 1, com_google_android_gms_internal_measurement_zzki3.zzasw.longValue(), 0, null, null, null);
                                                        }
                                                        zzga().zza(zze);
                                                        j5 = zze.zzafp;
                                                        map15 = arrayMap6;
                                                        str10 = str7;
                                                        map16 = (Map) map15.get(str10);
                                                        if (map16 != null) {
                                                            str8 = str;
                                                        } else {
                                                            str8 = str;
                                                            map16 = zzga().zzj(str8, str10);
                                                            if (map16 == null) {
                                                                map16 = new ArrayMap();
                                                            }
                                                            map15.put(str10, map16);
                                                        }
                                                        it4 = map16.keySet().iterator();
                                                        while (it4.hasNext()) {
                                                            i = ((Integer) it4.next()).intValue();
                                                            hashSet4 = hashSet3;
                                                            if (hashSet4.contains(Integer.valueOf(i))) {
                                                                arrayMap2 = map14;
                                                                map3 = map12;
                                                                bitSet5 = (BitSet) map3.get(Integer.valueOf(i));
                                                                map4 = map15;
                                                                map15 = map13;
                                                                bitSet = (BitSet) map15.get(Integer.valueOf(i));
                                                                if (((zzkh) arrayMap2.get(Integer.valueOf(i))) == null) {
                                                                    com_google_android_gms_internal_measurement_zzkh = new zzkh();
                                                                    arrayMap2.put(Integer.valueOf(i), com_google_android_gms_internal_measurement_zzkh);
                                                                    com_google_android_gms_internal_measurement_zzkh.zzast = Boolean.valueOf(true);
                                                                    bitSet2 = new BitSet();
                                                                    map3.put(Integer.valueOf(i), bitSet2);
                                                                    bitSet = new BitSet();
                                                                    map15.put(Integer.valueOf(i), bitSet);
                                                                    bitSet5 = bitSet2;
                                                                }
                                                                it = ((List) map16.get(Integer.valueOf(i))).iterator();
                                                                while (it.hasNext()) {
                                                                    map17 = map16;
                                                                    com_google_android_gms_internal_measurement_zzjz = (zzjz) it.next();
                                                                    it5 = it4;
                                                                    it6 = it;
                                                                    if (zzgg().isLoggable(2)) {
                                                                        map5 = map15;
                                                                        map6 = arrayMap2;
                                                                        map7 = map3;
                                                                    } else {
                                                                        map5 = map15;
                                                                        map7 = map3;
                                                                        map6 = arrayMap2;
                                                                        zzgg().zzir().zzd("Evaluating filter. audience, filter, event", Integer.valueOf(i), com_google_android_gms_internal_measurement_zzjz.zzark, zzgb().zzbe(com_google_android_gms_internal_measurement_zzjz.zzarl));
                                                                        zzgg().zzir().zzg("Filter definition", zzgb().zza(com_google_android_gms_internal_measurement_zzjz));
                                                                    }
                                                                    if (com_google_android_gms_internal_measurement_zzjz.zzark != null) {
                                                                        if (com_google_android_gms_internal_measurement_zzjz.zzark.intValue() > 256) {
                                                                            if (bitSet5.get(com_google_android_gms_internal_measurement_zzjz.zzark.intValue())) {
                                                                                if (com_google_android_gms_internal_measurement_zzjz.zzaro != null) {
                                                                                    zza = zza(j5, com_google_android_gms_internal_measurement_zzjz.zzaro);
                                                                                    if (zza != null) {
                                                                                        if (zza.booleanValue()) {
                                                                                            j6 = j5;
                                                                                            valueOf = Boolean.valueOf(false);
                                                                                            com_google_android_gms_internal_measurement_zzkjArr6 = com_google_android_gms_internal_measurement_zzkjArr5;
                                                                                            if (valueOf != null) {
                                                                                            }
                                                                                            zzgg().zzir().zzg("Event filter result", valueOf != null ? "null" : valueOf);
                                                                                            if (valueOf != null) {
                                                                                                bitSet.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                                                                if (valueOf.booleanValue()) {
                                                                                                    bitSet5.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                                                                }
                                                                                            } else {
                                                                                                hashSet4.add(Integer.valueOf(i));
                                                                                            }
                                                                                            com_google_android_gms_internal_measurement_zzkjArr5 = com_google_android_gms_internal_measurement_zzkjArr6;
                                                                                            map16 = map17;
                                                                                            it4 = it5;
                                                                                            it = it6;
                                                                                            map15 = map5;
                                                                                            map3 = map7;
                                                                                            arrayMap2 = map6;
                                                                                            j5 = j6;
                                                                                        }
                                                                                    }
                                                                                    j6 = j5;
                                                                                    com_google_android_gms_internal_measurement_zzkjArr6 = com_google_android_gms_internal_measurement_zzkjArr5;
                                                                                    valueOf = null;
                                                                                    if (valueOf != null) {
                                                                                    }
                                                                                    zzgg().zzir().zzg("Event filter result", valueOf != null ? "null" : valueOf);
                                                                                    if (valueOf != null) {
                                                                                        hashSet4.add(Integer.valueOf(i));
                                                                                    } else {
                                                                                        bitSet.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                                                        if (valueOf.booleanValue()) {
                                                                                            bitSet5.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                                                        }
                                                                                    }
                                                                                    com_google_android_gms_internal_measurement_zzkjArr5 = com_google_android_gms_internal_measurement_zzkjArr6;
                                                                                    map16 = map17;
                                                                                    it4 = it5;
                                                                                    it = it6;
                                                                                    map15 = map5;
                                                                                    map3 = map7;
                                                                                    arrayMap2 = map6;
                                                                                    j5 = j6;
                                                                                }
                                                                                hashSet = new HashSet();
                                                                                for (i9 = 0; i9 < i4; i9++) {
                                                                                    if (TextUtils.isEmpty(com_google_android_gms_internal_measurement_zzka2.zzart)) {
                                                                                        zzgg().zzin().zzg("null or empty param name in filter. event", zzgb().zzbe(str10));
                                                                                        j6 = j5;
                                                                                        com_google_android_gms_internal_measurement_zzkjArr6 = com_google_android_gms_internal_measurement_zzkjArr5;
                                                                                        break;
                                                                                    }
                                                                                    hashSet.add(com_google_android_gms_internal_measurement_zzka2.zzart);
                                                                                }
                                                                                map15 = new ArrayMap();
                                                                                com_google_android_gms_internal_measurement_zzkjArr6 = com_google_android_gms_internal_measurement_zzkjArr5;
                                                                                length3 = com_google_android_gms_internal_measurement_zzkjArr6.length;
                                                                                i4 = 0;
                                                                                while (i4 < length3) {
                                                                                    com_google_android_gms_internal_measurement_zzkj = com_google_android_gms_internal_measurement_zzkjArr6[i4];
                                                                                    j6 = j5;
                                                                                    if (!hashSet.contains(com_google_android_gms_internal_measurement_zzkj.name)) {
                                                                                        if (com_google_android_gms_internal_measurement_zzkj.zzasz == null) {
                                                                                            obj = com_google_android_gms_internal_measurement_zzkj.name;
                                                                                            obj3 = com_google_android_gms_internal_measurement_zzkj.zzasz;
                                                                                        } else if (com_google_android_gms_internal_measurement_zzkj.zzaqx == null) {
                                                                                            obj = com_google_android_gms_internal_measurement_zzkj.name;
                                                                                            obj3 = com_google_android_gms_internal_measurement_zzkj.zzaqx;
                                                                                        } else if (com_google_android_gms_internal_measurement_zzkj.zzajf != null) {
                                                                                            zzin = zzgg().zzin();
                                                                                            str3 = "Unknown value for param. event, param";
                                                                                            zzbe = zzgb().zzbe(str10);
                                                                                            zzbf = zzgb().zzbf(com_google_android_gms_internal_measurement_zzkj.name);
                                                                                            break;
                                                                                        } else {
                                                                                            obj = com_google_android_gms_internal_measurement_zzkj.name;
                                                                                            obj3 = com_google_android_gms_internal_measurement_zzkj.zzajf;
                                                                                        }
                                                                                        map15.put(obj, obj3);
                                                                                    }
                                                                                    i4++;
                                                                                    j5 = j6;
                                                                                }
                                                                                j6 = j5;
                                                                                com_google_android_gms_internal_measurement_zzkaArr2 = com_google_android_gms_internal_measurement_zzjz.zzarm;
                                                                                length2 = com_google_android_gms_internal_measurement_zzkaArr2.length;
                                                                                i7 = 0;
                                                                                while (i7 < length2) {
                                                                                    com_google_android_gms_internal_measurement_zzka = com_google_android_gms_internal_measurement_zzkaArr2[i7];
                                                                                    equals = Boolean.TRUE.equals(com_google_android_gms_internal_measurement_zzka.zzars);
                                                                                    str6 = com_google_android_gms_internal_measurement_zzka.zzart;
                                                                                    if (TextUtils.isEmpty(str6)) {
                                                                                        com_google_android_gms_internal_measurement_zzkaArr = com_google_android_gms_internal_measurement_zzkaArr2;
                                                                                        obj = map15.get(str6);
                                                                                        i5 = length2;
                                                                                        if (obj instanceof Long) {
                                                                                            if (obj instanceof Double) {
                                                                                                if (!(obj instanceof String)) {
                                                                                                    if (obj == null) {
                                                                                                        zzgg().zzir().zze("Missing param for filter. event, param", zzgb().zzbe(str10), zzgb().zzbf(str6));
                                                                                                        z = false;
                                                                                                        break;
                                                                                                    }
                                                                                                    zzin = zzgg().zzin();
                                                                                                    str3 = "Unknown param type. event, param";
                                                                                                } else {
                                                                                                    if (com_google_android_gms_internal_measurement_zzka.zzarq != null) {
                                                                                                        zza2 = zza((String) obj, com_google_android_gms_internal_measurement_zzka.zzarq);
                                                                                                    } else if (com_google_android_gms_internal_measurement_zzka.zzarr == null) {
                                                                                                        zzin = zzgg().zzin();
                                                                                                        str3 = "No filter for String param. event, param";
                                                                                                    } else {
                                                                                                        str9 = (String) obj;
                                                                                                        if (zzjv.zzcd(str9)) {
                                                                                                            zzin = zzgg().zzin();
                                                                                                            str3 = "Invalid param value for number filter. event, param";
                                                                                                        } else {
                                                                                                            zza2 = zza(str9, com_google_android_gms_internal_measurement_zzka.zzarr);
                                                                                                        }
                                                                                                    }
                                                                                                    if (zza2 == null) {
                                                                                                        if (((zza2.booleanValue() ^ 1) ^ equals) != 0) {
                                                                                                            z = false;
                                                                                                            break;
                                                                                                        }
                                                                                                        i7++;
                                                                                                        com_google_android_gms_internal_measurement_zzkaArr2 = com_google_android_gms_internal_measurement_zzkaArr;
                                                                                                        length2 = i5;
                                                                                                    }
                                                                                                }
                                                                                            } else if (com_google_android_gms_internal_measurement_zzka.zzarr != null) {
                                                                                                zza2 = zza(((Double) obj).doubleValue(), com_google_android_gms_internal_measurement_zzka.zzarr);
                                                                                                if (zza2 == null) {
                                                                                                    if (((zza2.booleanValue() ^ 1) ^ equals) != 0) {
                                                                                                        z = false;
                                                                                                        break;
                                                                                                    }
                                                                                                    i7++;
                                                                                                    com_google_android_gms_internal_measurement_zzkaArr2 = com_google_android_gms_internal_measurement_zzkaArr;
                                                                                                    length2 = i5;
                                                                                                }
                                                                                            } else {
                                                                                                zzin = zzgg().zzin();
                                                                                                str3 = "No number filter for double param. event, param";
                                                                                            }
                                                                                        } else if (com_google_android_gms_internal_measurement_zzka.zzarr != null) {
                                                                                            zza2 = zza(((Long) obj).longValue(), com_google_android_gms_internal_measurement_zzka.zzarr);
                                                                                            if (zza2 == null) {
                                                                                                if (((zza2.booleanValue() ^ 1) ^ equals) != 0) {
                                                                                                    z = false;
                                                                                                    break;
                                                                                                }
                                                                                                i7++;
                                                                                                com_google_android_gms_internal_measurement_zzkaArr2 = com_google_android_gms_internal_measurement_zzkaArr;
                                                                                                length2 = i5;
                                                                                            }
                                                                                        } else {
                                                                                            zzin = zzgg().zzin();
                                                                                            str3 = "No number filter for long param. event, param";
                                                                                        }
                                                                                        zzbe = zzgb().zzbe(str10);
                                                                                        zzbf = zzgb().zzbf(str6);
                                                                                        zzin.zze(str3, zzbe, zzbf);
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
                                                                                    bitSet.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                                                    if (valueOf.booleanValue()) {
                                                                                        bitSet5.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                                                    }
                                                                                } else {
                                                                                    hashSet4.add(Integer.valueOf(i));
                                                                                }
                                                                                com_google_android_gms_internal_measurement_zzkjArr5 = com_google_android_gms_internal_measurement_zzkjArr6;
                                                                                map16 = map17;
                                                                                it4 = it5;
                                                                                it = it6;
                                                                                map15 = map5;
                                                                                map3 = map7;
                                                                                arrayMap2 = map6;
                                                                                j5 = j6;
                                                                            } else {
                                                                                zzgg().zzir().zze("Event filter already evaluated true. audience ID, filter ID", Integer.valueOf(i), com_google_android_gms_internal_measurement_zzjz.zzark);
                                                                                map16 = map17;
                                                                                it4 = it5;
                                                                                it = it6;
                                                                                map15 = map5;
                                                                                map3 = map7;
                                                                                arrayMap2 = map6;
                                                                            }
                                                                            str8 = str;
                                                                        }
                                                                    }
                                                                    j6 = j5;
                                                                    com_google_android_gms_internal_measurement_zzkjArr6 = com_google_android_gms_internal_measurement_zzkjArr5;
                                                                    str2 = str;
                                                                    zzgg().zzin().zze("Invalid event filter ID. appId, id", zzfg.zzbh(str), String.valueOf(com_google_android_gms_internal_measurement_zzjz.zzark));
                                                                    com_google_android_gms_internal_measurement_zzkjArr5 = com_google_android_gms_internal_measurement_zzkjArr6;
                                                                    map16 = map17;
                                                                    it4 = it5;
                                                                    it = it6;
                                                                    map3 = map7;
                                                                    arrayMap2 = map6;
                                                                    j5 = j6;
                                                                    str8 = str2;
                                                                    map15 = map5;
                                                                }
                                                                map5 = map15;
                                                                str2 = str8;
                                                                hashSet3 = hashSet4;
                                                                map14 = arrayMap2;
                                                                map12 = map3;
                                                                map15 = map4;
                                                                map13 = map5;
                                                            } else {
                                                                zzgg().zzir().zzg("Skipping failed audience ID", Integer.valueOf(i));
                                                                hashSet3 = hashSet4;
                                                            }
                                                        }
                                                        arrayMap7 = map15;
                                                        str2 = str8;
                                                        map7 = map12;
                                                        map5 = map13;
                                                        hashSet2 = hashSet3;
                                                        map6 = map14;
                                                        l2 = l3;
                                                        j = j2;
                                                        i6 = i10 + 1;
                                                        com_google_android_gms_internal_measurement_zzkiArr2 = com_google_android_gms_internal_measurement_zzkiArr;
                                                        str5 = str2;
                                                        length = i8;
                                                        arrayMap5 = arrayMap7;
                                                        map2 = map5;
                                                        map = map7;
                                                        arrayMap2 = map6;
                                                        com_google_android_gms_internal_measurement_zzknArr2 = com_google_android_gms_internal_measurement_zzknArr;
                                                    }
                                                } catch (SQLiteException e4) {
                                                    e = e4;
                                                    arrayMap = arrayMap5;
                                                    com_google_android_gms_internal_measurement_zzkjArr = com_google_android_gms_internal_measurement_zzkjArr7;
                                                    i7 = 1;
                                                    i2 = 0;
                                                    zzga.zzgg().zzil().zzg("Error clearing complex main event", e);
                                                    i8 = length;
                                                    i9 = i7;
                                                    i10 = i11;
                                                    com_google_android_gms_internal_measurement_zzkjArr2 = com_google_android_gms_internal_measurement_zzkjArr;
                                                    arrayMap6 = arrayMap;
                                                    com_google_android_gms_internal_measurement_zzkjArr3 = new zzkj[(com_google_android_gms_internal_measurement_zzki.zzasv.length + com_google_android_gms_internal_measurement_zzkjArr2.length)];
                                                    com_google_android_gms_internal_measurement_zzkjArr4 = com_google_android_gms_internal_measurement_zzki.zzasv;
                                                    intValue = com_google_android_gms_internal_measurement_zzkjArr4.length;
                                                    i3 = i2;
                                                    length = i3;
                                                    while (i3 < intValue) {
                                                        com_google_android_gms_internal_measurement_zzkj2 = com_google_android_gms_internal_measurement_zzkjArr4[i3];
                                                        zzgc();
                                                        if (zzjv.zza(com_google_android_gms_internal_measurement_zzki3, com_google_android_gms_internal_measurement_zzkj2.name) != null) {
                                                            i9 = length + 1;
                                                            com_google_android_gms_internal_measurement_zzkjArr3[length] = com_google_android_gms_internal_measurement_zzkj2;
                                                            length = i9;
                                                        }
                                                        i3++;
                                                    }
                                                    if (length > 0) {
                                                        length2 = com_google_android_gms_internal_measurement_zzkjArr2.length;
                                                        intValue = i2;
                                                        while (intValue < length2) {
                                                            i7 = length + 1;
                                                            com_google_android_gms_internal_measurement_zzkjArr3[length] = com_google_android_gms_internal_measurement_zzkjArr2[intValue];
                                                            intValue++;
                                                            length = i7;
                                                        }
                                                        if (length != com_google_android_gms_internal_measurement_zzkjArr3.length) {
                                                            com_google_android_gms_internal_measurement_zzkjArr3 = (zzkj[]) Arrays.copyOf(com_google_android_gms_internal_measurement_zzkjArr3, length);
                                                        }
                                                        com_google_android_gms_internal_measurement_zzkjArr2 = com_google_android_gms_internal_measurement_zzkjArr3;
                                                    } else {
                                                        zzgg().zzin().zzg("No unique parameters in main event. eventName", str10);
                                                    }
                                                    str6 = str10;
                                                    l3 = l;
                                                    j3 = 0;
                                                    com_google_android_gms_internal_measurement_zzki2 = com_google_android_gms_internal_measurement_zzki;
                                                    zze = zzga().zze(str5, com_google_android_gms_internal_measurement_zzki3.name);
                                                    if (zze != null) {
                                                        hashSet3 = hashSet2;
                                                        map14 = arrayMap2;
                                                        com_google_android_gms_internal_measurement_zzkjArr5 = com_google_android_gms_internal_measurement_zzkjArr2;
                                                        str7 = str6;
                                                        map12 = map;
                                                        map13 = map2;
                                                        j4 = j3;
                                                        zze = zze.zzie();
                                                    } else {
                                                        zzgg().zzin().zze("Event aggregate wasn't created during raw event logging. appId, event", zzfg.zzbh(str), zzgb().zzbe(str6));
                                                        map12 = map;
                                                        map13 = map2;
                                                        hashSet3 = hashSet2;
                                                        map14 = arrayMap2;
                                                        com_google_android_gms_internal_measurement_zzkjArr5 = com_google_android_gms_internal_measurement_zzkjArr2;
                                                        j4 = j3;
                                                        str7 = str6;
                                                        zze = new zzeq(str5, com_google_android_gms_internal_measurement_zzki3.name, 1, 1, com_google_android_gms_internal_measurement_zzki3.zzasw.longValue(), 0, null, null, null);
                                                    }
                                                    zzga().zza(zze);
                                                    j5 = zze.zzafp;
                                                    map15 = arrayMap6;
                                                    str10 = str7;
                                                    map16 = (Map) map15.get(str10);
                                                    if (map16 != null) {
                                                        str8 = str;
                                                    } else {
                                                        str8 = str;
                                                        map16 = zzga().zzj(str8, str10);
                                                        if (map16 == null) {
                                                            map16 = new ArrayMap();
                                                        }
                                                        map15.put(str10, map16);
                                                    }
                                                    it4 = map16.keySet().iterator();
                                                    while (it4.hasNext()) {
                                                        i = ((Integer) it4.next()).intValue();
                                                        hashSet4 = hashSet3;
                                                        if (hashSet4.contains(Integer.valueOf(i))) {
                                                            arrayMap2 = map14;
                                                            map3 = map12;
                                                            bitSet5 = (BitSet) map3.get(Integer.valueOf(i));
                                                            map4 = map15;
                                                            map15 = map13;
                                                            bitSet = (BitSet) map15.get(Integer.valueOf(i));
                                                            if (((zzkh) arrayMap2.get(Integer.valueOf(i))) == null) {
                                                                com_google_android_gms_internal_measurement_zzkh = new zzkh();
                                                                arrayMap2.put(Integer.valueOf(i), com_google_android_gms_internal_measurement_zzkh);
                                                                com_google_android_gms_internal_measurement_zzkh.zzast = Boolean.valueOf(true);
                                                                bitSet2 = new BitSet();
                                                                map3.put(Integer.valueOf(i), bitSet2);
                                                                bitSet = new BitSet();
                                                                map15.put(Integer.valueOf(i), bitSet);
                                                                bitSet5 = bitSet2;
                                                            }
                                                            it = ((List) map16.get(Integer.valueOf(i))).iterator();
                                                            while (it.hasNext()) {
                                                                map17 = map16;
                                                                com_google_android_gms_internal_measurement_zzjz = (zzjz) it.next();
                                                                it5 = it4;
                                                                it6 = it;
                                                                if (zzgg().isLoggable(2)) {
                                                                    map5 = map15;
                                                                    map6 = arrayMap2;
                                                                    map7 = map3;
                                                                } else {
                                                                    map5 = map15;
                                                                    map7 = map3;
                                                                    map6 = arrayMap2;
                                                                    zzgg().zzir().zzd("Evaluating filter. audience, filter, event", Integer.valueOf(i), com_google_android_gms_internal_measurement_zzjz.zzark, zzgb().zzbe(com_google_android_gms_internal_measurement_zzjz.zzarl));
                                                                    zzgg().zzir().zzg("Filter definition", zzgb().zza(com_google_android_gms_internal_measurement_zzjz));
                                                                }
                                                                if (com_google_android_gms_internal_measurement_zzjz.zzark != null) {
                                                                    if (com_google_android_gms_internal_measurement_zzjz.zzark.intValue() > 256) {
                                                                        if (bitSet5.get(com_google_android_gms_internal_measurement_zzjz.zzark.intValue())) {
                                                                            if (com_google_android_gms_internal_measurement_zzjz.zzaro != null) {
                                                                                zza = zza(j5, com_google_android_gms_internal_measurement_zzjz.zzaro);
                                                                                if (zza != null) {
                                                                                    if (zza.booleanValue()) {
                                                                                        j6 = j5;
                                                                                        valueOf = Boolean.valueOf(false);
                                                                                        com_google_android_gms_internal_measurement_zzkjArr6 = com_google_android_gms_internal_measurement_zzkjArr5;
                                                                                        if (valueOf != null) {
                                                                                        }
                                                                                        zzgg().zzir().zzg("Event filter result", valueOf != null ? "null" : valueOf);
                                                                                        if (valueOf != null) {
                                                                                            bitSet.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                                                            if (valueOf.booleanValue()) {
                                                                                                bitSet5.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                                                            }
                                                                                        } else {
                                                                                            hashSet4.add(Integer.valueOf(i));
                                                                                        }
                                                                                        com_google_android_gms_internal_measurement_zzkjArr5 = com_google_android_gms_internal_measurement_zzkjArr6;
                                                                                        map16 = map17;
                                                                                        it4 = it5;
                                                                                        it = it6;
                                                                                        map15 = map5;
                                                                                        map3 = map7;
                                                                                        arrayMap2 = map6;
                                                                                        j5 = j6;
                                                                                    }
                                                                                }
                                                                                j6 = j5;
                                                                                com_google_android_gms_internal_measurement_zzkjArr6 = com_google_android_gms_internal_measurement_zzkjArr5;
                                                                                valueOf = null;
                                                                                if (valueOf != null) {
                                                                                }
                                                                                zzgg().zzir().zzg("Event filter result", valueOf != null ? "null" : valueOf);
                                                                                if (valueOf != null) {
                                                                                    hashSet4.add(Integer.valueOf(i));
                                                                                } else {
                                                                                    bitSet.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                                                    if (valueOf.booleanValue()) {
                                                                                        bitSet5.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                                                    }
                                                                                }
                                                                                com_google_android_gms_internal_measurement_zzkjArr5 = com_google_android_gms_internal_measurement_zzkjArr6;
                                                                                map16 = map17;
                                                                                it4 = it5;
                                                                                it = it6;
                                                                                map15 = map5;
                                                                                map3 = map7;
                                                                                arrayMap2 = map6;
                                                                                j5 = j6;
                                                                            }
                                                                            hashSet = new HashSet();
                                                                            for (i9 = 0; i9 < i4; i9++) {
                                                                                if (TextUtils.isEmpty(com_google_android_gms_internal_measurement_zzka2.zzart)) {
                                                                                    zzgg().zzin().zzg("null or empty param name in filter. event", zzgb().zzbe(str10));
                                                                                    j6 = j5;
                                                                                    com_google_android_gms_internal_measurement_zzkjArr6 = com_google_android_gms_internal_measurement_zzkjArr5;
                                                                                    break;
                                                                                }
                                                                                hashSet.add(com_google_android_gms_internal_measurement_zzka2.zzart);
                                                                            }
                                                                            map15 = new ArrayMap();
                                                                            com_google_android_gms_internal_measurement_zzkjArr6 = com_google_android_gms_internal_measurement_zzkjArr5;
                                                                            length3 = com_google_android_gms_internal_measurement_zzkjArr6.length;
                                                                            i4 = 0;
                                                                            while (i4 < length3) {
                                                                                com_google_android_gms_internal_measurement_zzkj = com_google_android_gms_internal_measurement_zzkjArr6[i4];
                                                                                j6 = j5;
                                                                                if (!hashSet.contains(com_google_android_gms_internal_measurement_zzkj.name)) {
                                                                                    if (com_google_android_gms_internal_measurement_zzkj.zzasz == null) {
                                                                                        obj = com_google_android_gms_internal_measurement_zzkj.name;
                                                                                        obj3 = com_google_android_gms_internal_measurement_zzkj.zzasz;
                                                                                    } else if (com_google_android_gms_internal_measurement_zzkj.zzaqx == null) {
                                                                                        obj = com_google_android_gms_internal_measurement_zzkj.name;
                                                                                        obj3 = com_google_android_gms_internal_measurement_zzkj.zzaqx;
                                                                                    } else if (com_google_android_gms_internal_measurement_zzkj.zzajf != null) {
                                                                                        zzin = zzgg().zzin();
                                                                                        str3 = "Unknown value for param. event, param";
                                                                                        zzbe = zzgb().zzbe(str10);
                                                                                        zzbf = zzgb().zzbf(com_google_android_gms_internal_measurement_zzkj.name);
                                                                                        break;
                                                                                    } else {
                                                                                        obj = com_google_android_gms_internal_measurement_zzkj.name;
                                                                                        obj3 = com_google_android_gms_internal_measurement_zzkj.zzajf;
                                                                                    }
                                                                                    map15.put(obj, obj3);
                                                                                }
                                                                                i4++;
                                                                                j5 = j6;
                                                                            }
                                                                            j6 = j5;
                                                                            com_google_android_gms_internal_measurement_zzkaArr2 = com_google_android_gms_internal_measurement_zzjz.zzarm;
                                                                            length2 = com_google_android_gms_internal_measurement_zzkaArr2.length;
                                                                            i7 = 0;
                                                                            while (i7 < length2) {
                                                                                com_google_android_gms_internal_measurement_zzka = com_google_android_gms_internal_measurement_zzkaArr2[i7];
                                                                                equals = Boolean.TRUE.equals(com_google_android_gms_internal_measurement_zzka.zzars);
                                                                                str6 = com_google_android_gms_internal_measurement_zzka.zzart;
                                                                                if (TextUtils.isEmpty(str6)) {
                                                                                    com_google_android_gms_internal_measurement_zzkaArr = com_google_android_gms_internal_measurement_zzkaArr2;
                                                                                    obj = map15.get(str6);
                                                                                    i5 = length2;
                                                                                    if (obj instanceof Long) {
                                                                                        if (obj instanceof Double) {
                                                                                            if (!(obj instanceof String)) {
                                                                                                if (obj == null) {
                                                                                                    zzgg().zzir().zze("Missing param for filter. event, param", zzgb().zzbe(str10), zzgb().zzbf(str6));
                                                                                                    z = false;
                                                                                                    break;
                                                                                                }
                                                                                                zzin = zzgg().zzin();
                                                                                                str3 = "Unknown param type. event, param";
                                                                                            } else {
                                                                                                if (com_google_android_gms_internal_measurement_zzka.zzarq != null) {
                                                                                                    zza2 = zza((String) obj, com_google_android_gms_internal_measurement_zzka.zzarq);
                                                                                                } else if (com_google_android_gms_internal_measurement_zzka.zzarr == null) {
                                                                                                    zzin = zzgg().zzin();
                                                                                                    str3 = "No filter for String param. event, param";
                                                                                                } else {
                                                                                                    str9 = (String) obj;
                                                                                                    if (zzjv.zzcd(str9)) {
                                                                                                        zzin = zzgg().zzin();
                                                                                                        str3 = "Invalid param value for number filter. event, param";
                                                                                                    } else {
                                                                                                        zza2 = zza(str9, com_google_android_gms_internal_measurement_zzka.zzarr);
                                                                                                    }
                                                                                                }
                                                                                                if (zza2 == null) {
                                                                                                    if (((zza2.booleanValue() ^ 1) ^ equals) != 0) {
                                                                                                        z = false;
                                                                                                        break;
                                                                                                    }
                                                                                                    i7++;
                                                                                                    com_google_android_gms_internal_measurement_zzkaArr2 = com_google_android_gms_internal_measurement_zzkaArr;
                                                                                                    length2 = i5;
                                                                                                }
                                                                                            }
                                                                                        } else if (com_google_android_gms_internal_measurement_zzka.zzarr != null) {
                                                                                            zza2 = zza(((Double) obj).doubleValue(), com_google_android_gms_internal_measurement_zzka.zzarr);
                                                                                            if (zza2 == null) {
                                                                                                if (((zza2.booleanValue() ^ 1) ^ equals) != 0) {
                                                                                                    z = false;
                                                                                                    break;
                                                                                                }
                                                                                                i7++;
                                                                                                com_google_android_gms_internal_measurement_zzkaArr2 = com_google_android_gms_internal_measurement_zzkaArr;
                                                                                                length2 = i5;
                                                                                            }
                                                                                        } else {
                                                                                            zzin = zzgg().zzin();
                                                                                            str3 = "No number filter for double param. event, param";
                                                                                        }
                                                                                    } else if (com_google_android_gms_internal_measurement_zzka.zzarr != null) {
                                                                                        zza2 = zza(((Long) obj).longValue(), com_google_android_gms_internal_measurement_zzka.zzarr);
                                                                                        if (zza2 == null) {
                                                                                            if (((zza2.booleanValue() ^ 1) ^ equals) != 0) {
                                                                                                z = false;
                                                                                                break;
                                                                                            }
                                                                                            i7++;
                                                                                            com_google_android_gms_internal_measurement_zzkaArr2 = com_google_android_gms_internal_measurement_zzkaArr;
                                                                                            length2 = i5;
                                                                                        }
                                                                                    } else {
                                                                                        zzin = zzgg().zzin();
                                                                                        str3 = "No number filter for long param. event, param";
                                                                                    }
                                                                                    zzbe = zzgb().zzbe(str10);
                                                                                    zzbf = zzgb().zzbf(str6);
                                                                                    zzin.zze(str3, zzbe, zzbf);
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
                                                                                bitSet.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                                                if (valueOf.booleanValue()) {
                                                                                    bitSet5.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                                                }
                                                                            } else {
                                                                                hashSet4.add(Integer.valueOf(i));
                                                                            }
                                                                            com_google_android_gms_internal_measurement_zzkjArr5 = com_google_android_gms_internal_measurement_zzkjArr6;
                                                                            map16 = map17;
                                                                            it4 = it5;
                                                                            it = it6;
                                                                            map15 = map5;
                                                                            map3 = map7;
                                                                            arrayMap2 = map6;
                                                                            j5 = j6;
                                                                        } else {
                                                                            zzgg().zzir().zze("Event filter already evaluated true. audience ID, filter ID", Integer.valueOf(i), com_google_android_gms_internal_measurement_zzjz.zzark);
                                                                            map16 = map17;
                                                                            it4 = it5;
                                                                            it = it6;
                                                                            map15 = map5;
                                                                            map3 = map7;
                                                                            arrayMap2 = map6;
                                                                        }
                                                                        str8 = str;
                                                                    }
                                                                }
                                                                j6 = j5;
                                                                com_google_android_gms_internal_measurement_zzkjArr6 = com_google_android_gms_internal_measurement_zzkjArr5;
                                                                str2 = str;
                                                                zzgg().zzin().zze("Invalid event filter ID. appId, id", zzfg.zzbh(str), String.valueOf(com_google_android_gms_internal_measurement_zzjz.zzark));
                                                                com_google_android_gms_internal_measurement_zzkjArr5 = com_google_android_gms_internal_measurement_zzkjArr6;
                                                                map16 = map17;
                                                                it4 = it5;
                                                                it = it6;
                                                                map3 = map7;
                                                                arrayMap2 = map6;
                                                                j5 = j6;
                                                                str8 = str2;
                                                                map15 = map5;
                                                            }
                                                            map5 = map15;
                                                            str2 = str8;
                                                            hashSet3 = hashSet4;
                                                            map14 = arrayMap2;
                                                            map12 = map3;
                                                            map15 = map4;
                                                            map13 = map5;
                                                        } else {
                                                            zzgg().zzir().zzg("Skipping failed audience ID", Integer.valueOf(i));
                                                            hashSet3 = hashSet4;
                                                        }
                                                    }
                                                    arrayMap7 = map15;
                                                    str2 = str8;
                                                    map7 = map12;
                                                    map5 = map13;
                                                    hashSet2 = hashSet3;
                                                    map6 = map14;
                                                    l2 = l3;
                                                    j = j2;
                                                    i6 = i10 + 1;
                                                    com_google_android_gms_internal_measurement_zzkiArr2 = com_google_android_gms_internal_measurement_zzkiArr;
                                                    str5 = str2;
                                                    length = i8;
                                                    arrayMap5 = arrayMap7;
                                                    map2 = map5;
                                                    map = map7;
                                                    arrayMap2 = map6;
                                                    com_google_android_gms_internal_measurement_zzknArr2 = com_google_android_gms_internal_measurement_zzknArr;
                                                }
                                                i8 = length;
                                                i9 = i7;
                                                i10 = i11;
                                                com_google_android_gms_internal_measurement_zzkjArr2 = com_google_android_gms_internal_measurement_zzkjArr;
                                                arrayMap6 = arrayMap;
                                            } else {
                                                i2 = 0;
                                                i10 = i11;
                                                j7 = 0;
                                                i8 = length;
                                                arrayMap6 = arrayMap5;
                                                i9 = 1;
                                                com_google_android_gms_internal_measurement_zzkjArr2 = com_google_android_gms_internal_measurement_zzkjArr7;
                                                zzga().zza(str5, l5, j2, com_google_android_gms_internal_measurement_zzki);
                                            }
                                            com_google_android_gms_internal_measurement_zzkjArr3 = new zzkj[(com_google_android_gms_internal_measurement_zzki.zzasv.length + com_google_android_gms_internal_measurement_zzkjArr2.length)];
                                            com_google_android_gms_internal_measurement_zzkjArr4 = com_google_android_gms_internal_measurement_zzki.zzasv;
                                            intValue = com_google_android_gms_internal_measurement_zzkjArr4.length;
                                            i3 = i2;
                                            length = i3;
                                            while (i3 < intValue) {
                                                com_google_android_gms_internal_measurement_zzkj2 = com_google_android_gms_internal_measurement_zzkjArr4[i3];
                                                zzgc();
                                                if (zzjv.zza(com_google_android_gms_internal_measurement_zzki3, com_google_android_gms_internal_measurement_zzkj2.name) != null) {
                                                    i9 = length + 1;
                                                    com_google_android_gms_internal_measurement_zzkjArr3[length] = com_google_android_gms_internal_measurement_zzkj2;
                                                    length = i9;
                                                }
                                                i3++;
                                            }
                                            if (length > 0) {
                                                length2 = com_google_android_gms_internal_measurement_zzkjArr2.length;
                                                intValue = i2;
                                                while (intValue < length2) {
                                                    i7 = length + 1;
                                                    com_google_android_gms_internal_measurement_zzkjArr3[length] = com_google_android_gms_internal_measurement_zzkjArr2[intValue];
                                                    intValue++;
                                                    length = i7;
                                                }
                                                if (length != com_google_android_gms_internal_measurement_zzkjArr3.length) {
                                                    com_google_android_gms_internal_measurement_zzkjArr3 = (zzkj[]) Arrays.copyOf(com_google_android_gms_internal_measurement_zzkjArr3, length);
                                                }
                                                com_google_android_gms_internal_measurement_zzkjArr2 = com_google_android_gms_internal_measurement_zzkjArr3;
                                            } else {
                                                zzgg().zzin().zzg("No unique parameters in main event. eventName", str10);
                                            }
                                            str6 = str10;
                                            l3 = l;
                                            j3 = 0;
                                            com_google_android_gms_internal_measurement_zzki2 = com_google_android_gms_internal_measurement_zzki;
                                        }
                                    }
                                    zza3 = zzga().zza(str5, l5);
                                    if (zza3 != null) {
                                        if (zza3.first == null) {
                                            com_google_android_gms_internal_measurement_zzki5 = (zzki) zza3.first;
                                            j = ((Long) zza3.second).longValue();
                                            zzgc();
                                            l = (Long) zzjv.zzb(com_google_android_gms_internal_measurement_zzki5, "_eid");
                                            com_google_android_gms_internal_measurement_zzki = com_google_android_gms_internal_measurement_zzki5;
                                            j2 = j - 1;
                                            if (j2 > 0) {
                                                i2 = 0;
                                                i10 = i11;
                                                j7 = 0;
                                                i8 = length;
                                                arrayMap6 = arrayMap5;
                                                i9 = 1;
                                                com_google_android_gms_internal_measurement_zzkjArr2 = com_google_android_gms_internal_measurement_zzkjArr7;
                                                zzga().zza(str5, l5, j2, com_google_android_gms_internal_measurement_zzki);
                                            } else {
                                                zzga = zzga();
                                                zzga.zzab();
                                                zzga.zzgg().zzir().zzg("Clearing complex main event info. appId", str5);
                                                writableDatabase = zzga.getWritableDatabase();
                                                str2 = "delete from main_event_params where app_id=?";
                                                arrayMap = arrayMap5;
                                                com_google_android_gms_internal_measurement_zzkjArr = com_google_android_gms_internal_measurement_zzkjArr7;
                                                i7 = 1;
                                                strArr = new String[1];
                                                i2 = 0;
                                                strArr[0] = str5;
                                                writableDatabase.execSQL(str2, strArr);
                                                i8 = length;
                                                i9 = i7;
                                                i10 = i11;
                                                com_google_android_gms_internal_measurement_zzkjArr2 = com_google_android_gms_internal_measurement_zzkjArr;
                                                arrayMap6 = arrayMap;
                                            }
                                            com_google_android_gms_internal_measurement_zzkjArr3 = new zzkj[(com_google_android_gms_internal_measurement_zzki.zzasv.length + com_google_android_gms_internal_measurement_zzkjArr2.length)];
                                            com_google_android_gms_internal_measurement_zzkjArr4 = com_google_android_gms_internal_measurement_zzki.zzasv;
                                            intValue = com_google_android_gms_internal_measurement_zzkjArr4.length;
                                            i3 = i2;
                                            length = i3;
                                            while (i3 < intValue) {
                                                com_google_android_gms_internal_measurement_zzkj2 = com_google_android_gms_internal_measurement_zzkjArr4[i3];
                                                zzgc();
                                                if (zzjv.zza(com_google_android_gms_internal_measurement_zzki3, com_google_android_gms_internal_measurement_zzkj2.name) != null) {
                                                    i9 = length + 1;
                                                    com_google_android_gms_internal_measurement_zzkjArr3[length] = com_google_android_gms_internal_measurement_zzkj2;
                                                    length = i9;
                                                }
                                                i3++;
                                            }
                                            if (length > 0) {
                                                zzgg().zzin().zzg("No unique parameters in main event. eventName", str10);
                                            } else {
                                                length2 = com_google_android_gms_internal_measurement_zzkjArr2.length;
                                                intValue = i2;
                                                while (intValue < length2) {
                                                    i7 = length + 1;
                                                    com_google_android_gms_internal_measurement_zzkjArr3[length] = com_google_android_gms_internal_measurement_zzkjArr2[intValue];
                                                    intValue++;
                                                    length = i7;
                                                }
                                                if (length != com_google_android_gms_internal_measurement_zzkjArr3.length) {
                                                    com_google_android_gms_internal_measurement_zzkjArr3 = (zzkj[]) Arrays.copyOf(com_google_android_gms_internal_measurement_zzkjArr3, length);
                                                }
                                                com_google_android_gms_internal_measurement_zzkjArr2 = com_google_android_gms_internal_measurement_zzkjArr3;
                                            }
                                            str6 = str10;
                                            l3 = l;
                                            j3 = 0;
                                            com_google_android_gms_internal_measurement_zzki2 = com_google_android_gms_internal_measurement_zzki;
                                        }
                                    }
                                    arrayMap6 = arrayMap5;
                                    i8 = length;
                                    i10 = i11;
                                    zzgg().zzil().zze("Extra parameter without existing main event. eventName, eventId", str10, l5);
                                } else {
                                    zzgg().zzil().zzg("Extra parameter without an event name. eventId", l5);
                                    arrayMap6 = arrayMap5;
                                    i8 = length;
                                    i10 = i11;
                                }
                                map6 = arrayMap2;
                                str2 = str5;
                                map7 = map;
                                map5 = map2;
                                arrayMap7 = arrayMap6;
                                i6 = i10 + 1;
                                com_google_android_gms_internal_measurement_zzkiArr2 = com_google_android_gms_internal_measurement_zzkiArr;
                                str5 = str2;
                                length = i8;
                                arrayMap5 = arrayMap7;
                                map2 = map5;
                                map = map7;
                                arrayMap2 = map6;
                                com_google_android_gms_internal_measurement_zzknArr2 = com_google_android_gms_internal_measurement_zzknArr;
                            } else {
                                arrayMap6 = arrayMap5;
                                i8 = length;
                                com_google_android_gms_internal_measurement_zzkjArr2 = com_google_android_gms_internal_measurement_zzkjArr7;
                                i10 = i11;
                                if (obj3 != null) {
                                    zzgc();
                                    valueOf2 = Long.valueOf(0);
                                    obj = zzjv.zzb(com_google_android_gms_internal_measurement_zzki3, "_epc");
                                    if (obj == null) {
                                        obj = valueOf2;
                                    }
                                    j = ((Long) obj).longValue();
                                    if (j > 0) {
                                        zzgg().zzin().zzg("Complex event with zero extra param count. eventName", str10);
                                        l4 = l5;
                                        j3 = 0;
                                    } else {
                                        l4 = l5;
                                        j3 = 0;
                                        zzga().zza(str5, l5, j, com_google_android_gms_internal_measurement_zzki3);
                                    }
                                    com_google_android_gms_internal_measurement_zzki2 = com_google_android_gms_internal_measurement_zzki3;
                                    j2 = j;
                                    l3 = l4;
                                    str6 = str10;
                                }
                            }
                            zze = zzga().zze(str5, com_google_android_gms_internal_measurement_zzki3.name);
                            if (zze != null) {
                                zzgg().zzin().zze("Event aggregate wasn't created during raw event logging. appId, event", zzfg.zzbh(str), zzgb().zzbe(str6));
                                map12 = map;
                                map13 = map2;
                                hashSet3 = hashSet2;
                                map14 = arrayMap2;
                                com_google_android_gms_internal_measurement_zzkjArr5 = com_google_android_gms_internal_measurement_zzkjArr2;
                                j4 = j3;
                                str7 = str6;
                                zze = new zzeq(str5, com_google_android_gms_internal_measurement_zzki3.name, 1, 1, com_google_android_gms_internal_measurement_zzki3.zzasw.longValue(), 0, null, null, null);
                            } else {
                                hashSet3 = hashSet2;
                                map14 = arrayMap2;
                                com_google_android_gms_internal_measurement_zzkjArr5 = com_google_android_gms_internal_measurement_zzkjArr2;
                                str7 = str6;
                                map12 = map;
                                map13 = map2;
                                j4 = j3;
                                zze = zze.zzie();
                            }
                            zzga().zza(zze);
                            j5 = zze.zzafp;
                            map15 = arrayMap6;
                            str10 = str7;
                            map16 = (Map) map15.get(str10);
                            if (map16 != null) {
                                str8 = str;
                                map16 = zzga().zzj(str8, str10);
                                if (map16 == null) {
                                    map16 = new ArrayMap();
                                }
                                map15.put(str10, map16);
                            } else {
                                str8 = str;
                            }
                            it4 = map16.keySet().iterator();
                            while (it4.hasNext()) {
                                i = ((Integer) it4.next()).intValue();
                                hashSet4 = hashSet3;
                                if (hashSet4.contains(Integer.valueOf(i))) {
                                    zzgg().zzir().zzg("Skipping failed audience ID", Integer.valueOf(i));
                                    hashSet3 = hashSet4;
                                } else {
                                    arrayMap2 = map14;
                                    map3 = map12;
                                    bitSet5 = (BitSet) map3.get(Integer.valueOf(i));
                                    map4 = map15;
                                    map15 = map13;
                                    bitSet = (BitSet) map15.get(Integer.valueOf(i));
                                    if (((zzkh) arrayMap2.get(Integer.valueOf(i))) == null) {
                                        com_google_android_gms_internal_measurement_zzkh = new zzkh();
                                        arrayMap2.put(Integer.valueOf(i), com_google_android_gms_internal_measurement_zzkh);
                                        com_google_android_gms_internal_measurement_zzkh.zzast = Boolean.valueOf(true);
                                        bitSet2 = new BitSet();
                                        map3.put(Integer.valueOf(i), bitSet2);
                                        bitSet = new BitSet();
                                        map15.put(Integer.valueOf(i), bitSet);
                                        bitSet5 = bitSet2;
                                    }
                                    it = ((List) map16.get(Integer.valueOf(i))).iterator();
                                    while (it.hasNext()) {
                                        map17 = map16;
                                        com_google_android_gms_internal_measurement_zzjz = (zzjz) it.next();
                                        it5 = it4;
                                        it6 = it;
                                        if (zzgg().isLoggable(2)) {
                                            map5 = map15;
                                            map7 = map3;
                                            map6 = arrayMap2;
                                            zzgg().zzir().zzd("Evaluating filter. audience, filter, event", Integer.valueOf(i), com_google_android_gms_internal_measurement_zzjz.zzark, zzgb().zzbe(com_google_android_gms_internal_measurement_zzjz.zzarl));
                                            zzgg().zzir().zzg("Filter definition", zzgb().zza(com_google_android_gms_internal_measurement_zzjz));
                                        } else {
                                            map5 = map15;
                                            map6 = arrayMap2;
                                            map7 = map3;
                                        }
                                        if (com_google_android_gms_internal_measurement_zzjz.zzark != null) {
                                            if (com_google_android_gms_internal_measurement_zzjz.zzark.intValue() > 256) {
                                                if (bitSet5.get(com_google_android_gms_internal_measurement_zzjz.zzark.intValue())) {
                                                    zzgg().zzir().zze("Event filter already evaluated true. audience ID, filter ID", Integer.valueOf(i), com_google_android_gms_internal_measurement_zzjz.zzark);
                                                    map16 = map17;
                                                    it4 = it5;
                                                    it = it6;
                                                    map15 = map5;
                                                    map3 = map7;
                                                    arrayMap2 = map6;
                                                } else {
                                                    if (com_google_android_gms_internal_measurement_zzjz.zzaro != null) {
                                                        zza = zza(j5, com_google_android_gms_internal_measurement_zzjz.zzaro);
                                                        if (zza != null) {
                                                            if (zza.booleanValue()) {
                                                                j6 = j5;
                                                                valueOf = Boolean.valueOf(false);
                                                                com_google_android_gms_internal_measurement_zzkjArr6 = com_google_android_gms_internal_measurement_zzkjArr5;
                                                                if (valueOf != null) {
                                                                }
                                                                zzgg().zzir().zzg("Event filter result", valueOf != null ? "null" : valueOf);
                                                                if (valueOf != null) {
                                                                    hashSet4.add(Integer.valueOf(i));
                                                                } else {
                                                                    bitSet.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                                    if (valueOf.booleanValue()) {
                                                                        bitSet5.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                                    }
                                                                }
                                                                com_google_android_gms_internal_measurement_zzkjArr5 = com_google_android_gms_internal_measurement_zzkjArr6;
                                                                map16 = map17;
                                                                it4 = it5;
                                                                it = it6;
                                                                map15 = map5;
                                                                map3 = map7;
                                                                arrayMap2 = map6;
                                                                j5 = j6;
                                                            }
                                                        }
                                                        j6 = j5;
                                                        com_google_android_gms_internal_measurement_zzkjArr6 = com_google_android_gms_internal_measurement_zzkjArr5;
                                                        valueOf = null;
                                                        if (valueOf != null) {
                                                        }
                                                        zzgg().zzir().zzg("Event filter result", valueOf != null ? "null" : valueOf);
                                                        if (valueOf != null) {
                                                            bitSet.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                            if (valueOf.booleanValue()) {
                                                                bitSet5.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                            }
                                                        } else {
                                                            hashSet4.add(Integer.valueOf(i));
                                                        }
                                                        com_google_android_gms_internal_measurement_zzkjArr5 = com_google_android_gms_internal_measurement_zzkjArr6;
                                                        map16 = map17;
                                                        it4 = it5;
                                                        it = it6;
                                                        map15 = map5;
                                                        map3 = map7;
                                                        arrayMap2 = map6;
                                                        j5 = j6;
                                                    }
                                                    hashSet = new HashSet();
                                                    for (i9 = 0; i9 < i4; i9++) {
                                                        if (TextUtils.isEmpty(com_google_android_gms_internal_measurement_zzka2.zzart)) {
                                                            zzgg().zzin().zzg("null or empty param name in filter. event", zzgb().zzbe(str10));
                                                            j6 = j5;
                                                            com_google_android_gms_internal_measurement_zzkjArr6 = com_google_android_gms_internal_measurement_zzkjArr5;
                                                            break;
                                                        }
                                                        hashSet.add(com_google_android_gms_internal_measurement_zzka2.zzart);
                                                    }
                                                    map15 = new ArrayMap();
                                                    com_google_android_gms_internal_measurement_zzkjArr6 = com_google_android_gms_internal_measurement_zzkjArr5;
                                                    length3 = com_google_android_gms_internal_measurement_zzkjArr6.length;
                                                    i4 = 0;
                                                    while (i4 < length3) {
                                                        com_google_android_gms_internal_measurement_zzkj = com_google_android_gms_internal_measurement_zzkjArr6[i4];
                                                        j6 = j5;
                                                        if (!hashSet.contains(com_google_android_gms_internal_measurement_zzkj.name)) {
                                                            if (com_google_android_gms_internal_measurement_zzkj.zzasz == null) {
                                                                if (com_google_android_gms_internal_measurement_zzkj.zzaqx == null) {
                                                                    if (com_google_android_gms_internal_measurement_zzkj.zzajf != null) {
                                                                        zzin = zzgg().zzin();
                                                                        str3 = "Unknown value for param. event, param";
                                                                        zzbe = zzgb().zzbe(str10);
                                                                        zzbf = zzgb().zzbf(com_google_android_gms_internal_measurement_zzkj.name);
                                                                        break;
                                                                    }
                                                                    obj = com_google_android_gms_internal_measurement_zzkj.name;
                                                                    obj3 = com_google_android_gms_internal_measurement_zzkj.zzajf;
                                                                } else {
                                                                    obj = com_google_android_gms_internal_measurement_zzkj.name;
                                                                    obj3 = com_google_android_gms_internal_measurement_zzkj.zzaqx;
                                                                }
                                                            } else {
                                                                obj = com_google_android_gms_internal_measurement_zzkj.name;
                                                                obj3 = com_google_android_gms_internal_measurement_zzkj.zzasz;
                                                            }
                                                            map15.put(obj, obj3);
                                                        }
                                                        i4++;
                                                        j5 = j6;
                                                    }
                                                    j6 = j5;
                                                    com_google_android_gms_internal_measurement_zzkaArr2 = com_google_android_gms_internal_measurement_zzjz.zzarm;
                                                    length2 = com_google_android_gms_internal_measurement_zzkaArr2.length;
                                                    i7 = 0;
                                                    while (i7 < length2) {
                                                        com_google_android_gms_internal_measurement_zzka = com_google_android_gms_internal_measurement_zzkaArr2[i7];
                                                        equals = Boolean.TRUE.equals(com_google_android_gms_internal_measurement_zzka.zzars);
                                                        str6 = com_google_android_gms_internal_measurement_zzka.zzart;
                                                        if (TextUtils.isEmpty(str6)) {
                                                            zzgg().zzin().zzg("Event has empty param name. event", zzgb().zzbe(str10));
                                                        } else {
                                                            com_google_android_gms_internal_measurement_zzkaArr = com_google_android_gms_internal_measurement_zzkaArr2;
                                                            obj = map15.get(str6);
                                                            i5 = length2;
                                                            if (obj instanceof Long) {
                                                                if (com_google_android_gms_internal_measurement_zzka.zzarr != null) {
                                                                    zzin = zzgg().zzin();
                                                                    str3 = "No number filter for long param. event, param";
                                                                } else {
                                                                    zza2 = zza(((Long) obj).longValue(), com_google_android_gms_internal_measurement_zzka.zzarr);
                                                                    if (zza2 == null) {
                                                                        if (((zza2.booleanValue() ^ 1) ^ equals) != 0) {
                                                                            z = false;
                                                                            break;
                                                                        }
                                                                        i7++;
                                                                        com_google_android_gms_internal_measurement_zzkaArr2 = com_google_android_gms_internal_measurement_zzkaArr;
                                                                        length2 = i5;
                                                                    }
                                                                }
                                                            } else if (obj instanceof Double) {
                                                                if (com_google_android_gms_internal_measurement_zzka.zzarr != null) {
                                                                    zzin = zzgg().zzin();
                                                                    str3 = "No number filter for double param. event, param";
                                                                } else {
                                                                    zza2 = zza(((Double) obj).doubleValue(), com_google_android_gms_internal_measurement_zzka.zzarr);
                                                                    if (zza2 == null) {
                                                                        if (((zza2.booleanValue() ^ 1) ^ equals) != 0) {
                                                                            z = false;
                                                                            break;
                                                                        }
                                                                        i7++;
                                                                        com_google_android_gms_internal_measurement_zzkaArr2 = com_google_android_gms_internal_measurement_zzkaArr;
                                                                        length2 = i5;
                                                                    }
                                                                }
                                                            } else if (!(obj instanceof String)) {
                                                                if (com_google_android_gms_internal_measurement_zzka.zzarq != null) {
                                                                    zza2 = zza((String) obj, com_google_android_gms_internal_measurement_zzka.zzarq);
                                                                } else if (com_google_android_gms_internal_measurement_zzka.zzarr == null) {
                                                                    str9 = (String) obj;
                                                                    if (zzjv.zzcd(str9)) {
                                                                        zza2 = zza(str9, com_google_android_gms_internal_measurement_zzka.zzarr);
                                                                    } else {
                                                                        zzin = zzgg().zzin();
                                                                        str3 = "Invalid param value for number filter. event, param";
                                                                    }
                                                                } else {
                                                                    zzin = zzgg().zzin();
                                                                    str3 = "No filter for String param. event, param";
                                                                }
                                                                if (zza2 == null) {
                                                                    if (((zza2.booleanValue() ^ 1) ^ equals) != 0) {
                                                                        z = false;
                                                                        break;
                                                                    }
                                                                    i7++;
                                                                    com_google_android_gms_internal_measurement_zzkaArr2 = com_google_android_gms_internal_measurement_zzkaArr;
                                                                    length2 = i5;
                                                                }
                                                            } else if (obj == null) {
                                                                zzgg().zzir().zze("Missing param for filter. event, param", zzgb().zzbe(str10), zzgb().zzbf(str6));
                                                                z = false;
                                                                break;
                                                            } else {
                                                                zzin = zzgg().zzin();
                                                                str3 = "Unknown param type. event, param";
                                                            }
                                                            zzbe = zzgb().zzbe(str10);
                                                            zzbf = zzgb().zzbf(str6);
                                                            zzin.zze(str3, zzbe, zzbf);
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
                                                        bitSet.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                        if (valueOf.booleanValue()) {
                                                            bitSet5.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                        }
                                                    }
                                                    com_google_android_gms_internal_measurement_zzkjArr5 = com_google_android_gms_internal_measurement_zzkjArr6;
                                                    map16 = map17;
                                                    it4 = it5;
                                                    it = it6;
                                                    map15 = map5;
                                                    map3 = map7;
                                                    arrayMap2 = map6;
                                                    j5 = j6;
                                                }
                                                str8 = str;
                                            }
                                        }
                                        j6 = j5;
                                        com_google_android_gms_internal_measurement_zzkjArr6 = com_google_android_gms_internal_measurement_zzkjArr5;
                                        str2 = str;
                                        zzgg().zzin().zze("Invalid event filter ID. appId, id", zzfg.zzbh(str), String.valueOf(com_google_android_gms_internal_measurement_zzjz.zzark));
                                        com_google_android_gms_internal_measurement_zzkjArr5 = com_google_android_gms_internal_measurement_zzkjArr6;
                                        map16 = map17;
                                        it4 = it5;
                                        it = it6;
                                        map3 = map7;
                                        arrayMap2 = map6;
                                        j5 = j6;
                                        str8 = str2;
                                        map15 = map5;
                                    }
                                    map5 = map15;
                                    str2 = str8;
                                    hashSet3 = hashSet4;
                                    map14 = arrayMap2;
                                    map12 = map3;
                                    map15 = map4;
                                    map13 = map5;
                                }
                            }
                            arrayMap7 = map15;
                            str2 = str8;
                            map7 = map12;
                            map5 = map13;
                            hashSet2 = hashSet3;
                            map6 = map14;
                            l2 = l3;
                            j = j2;
                            i6 = i10 + 1;
                            com_google_android_gms_internal_measurement_zzkiArr2 = com_google_android_gms_internal_measurement_zzkiArr;
                            str5 = str2;
                            length = i8;
                            arrayMap5 = arrayMap7;
                            map2 = map5;
                            map = map7;
                            arrayMap2 = map6;
                            com_google_android_gms_internal_measurement_zzknArr2 = com_google_android_gms_internal_measurement_zzknArr;
                        }
                    } else {
                        i11 = i6;
                    }
                    obj = null;
                    if (obj == null) {
                        arrayMap6 = arrayMap5;
                        i8 = length;
                        com_google_android_gms_internal_measurement_zzkjArr2 = com_google_android_gms_internal_measurement_zzkjArr7;
                        i10 = i11;
                        if (obj3 != null) {
                            zzgc();
                            valueOf2 = Long.valueOf(0);
                            obj = zzjv.zzb(com_google_android_gms_internal_measurement_zzki3, "_epc");
                            if (obj == null) {
                                obj = valueOf2;
                            }
                            j = ((Long) obj).longValue();
                            if (j > 0) {
                                l4 = l5;
                                j3 = 0;
                                zzga().zza(str5, l5, j, com_google_android_gms_internal_measurement_zzki3);
                            } else {
                                zzgg().zzin().zzg("Complex event with zero extra param count. eventName", str10);
                                l4 = l5;
                                j3 = 0;
                            }
                            com_google_android_gms_internal_measurement_zzki2 = com_google_android_gms_internal_measurement_zzki3;
                            j2 = j;
                            l3 = l4;
                            str6 = str10;
                        }
                    } else {
                        zzgc();
                        str10 = (String) zzjv.zzb(com_google_android_gms_internal_measurement_zzki3, "_en");
                        if (!TextUtils.isEmpty(str10)) {
                            zzgg().zzil().zzg("Extra parameter without an event name. eventId", l5);
                            arrayMap6 = arrayMap5;
                            i8 = length;
                            i10 = i11;
                        } else if (l5.longValue() != l2.longValue()) {
                            com_google_android_gms_internal_measurement_zzki4 = com_google_android_gms_internal_measurement_zzki2;
                            l = l2;
                            com_google_android_gms_internal_measurement_zzki = com_google_android_gms_internal_measurement_zzki4;
                            j2 = j - 1;
                            if (j2 > 0) {
                                zzga = zzga();
                                zzga.zzab();
                                zzga.zzgg().zzir().zzg("Clearing complex main event info. appId", str5);
                                writableDatabase = zzga.getWritableDatabase();
                                str2 = "delete from main_event_params where app_id=?";
                                arrayMap = arrayMap5;
                                com_google_android_gms_internal_measurement_zzkjArr = com_google_android_gms_internal_measurement_zzkjArr7;
                                i7 = 1;
                                strArr = new String[1];
                                i2 = 0;
                                strArr[0] = str5;
                                writableDatabase.execSQL(str2, strArr);
                                i8 = length;
                                i9 = i7;
                                i10 = i11;
                                com_google_android_gms_internal_measurement_zzkjArr2 = com_google_android_gms_internal_measurement_zzkjArr;
                                arrayMap6 = arrayMap;
                            } else {
                                i2 = 0;
                                i10 = i11;
                                j7 = 0;
                                i8 = length;
                                arrayMap6 = arrayMap5;
                                i9 = 1;
                                com_google_android_gms_internal_measurement_zzkjArr2 = com_google_android_gms_internal_measurement_zzkjArr7;
                                zzga().zza(str5, l5, j2, com_google_android_gms_internal_measurement_zzki);
                            }
                            com_google_android_gms_internal_measurement_zzkjArr3 = new zzkj[(com_google_android_gms_internal_measurement_zzki.zzasv.length + com_google_android_gms_internal_measurement_zzkjArr2.length)];
                            com_google_android_gms_internal_measurement_zzkjArr4 = com_google_android_gms_internal_measurement_zzki.zzasv;
                            intValue = com_google_android_gms_internal_measurement_zzkjArr4.length;
                            i3 = i2;
                            length = i3;
                            while (i3 < intValue) {
                                com_google_android_gms_internal_measurement_zzkj2 = com_google_android_gms_internal_measurement_zzkjArr4[i3];
                                zzgc();
                                if (zzjv.zza(com_google_android_gms_internal_measurement_zzki3, com_google_android_gms_internal_measurement_zzkj2.name) != null) {
                                    i9 = length + 1;
                                    com_google_android_gms_internal_measurement_zzkjArr3[length] = com_google_android_gms_internal_measurement_zzkj2;
                                    length = i9;
                                }
                                i3++;
                            }
                            if (length > 0) {
                                length2 = com_google_android_gms_internal_measurement_zzkjArr2.length;
                                intValue = i2;
                                while (intValue < length2) {
                                    i7 = length + 1;
                                    com_google_android_gms_internal_measurement_zzkjArr3[length] = com_google_android_gms_internal_measurement_zzkjArr2[intValue];
                                    intValue++;
                                    length = i7;
                                }
                                if (length != com_google_android_gms_internal_measurement_zzkjArr3.length) {
                                    com_google_android_gms_internal_measurement_zzkjArr3 = (zzkj[]) Arrays.copyOf(com_google_android_gms_internal_measurement_zzkjArr3, length);
                                }
                                com_google_android_gms_internal_measurement_zzkjArr2 = com_google_android_gms_internal_measurement_zzkjArr3;
                            } else {
                                zzgg().zzin().zzg("No unique parameters in main event. eventName", str10);
                            }
                            str6 = str10;
                            l3 = l;
                            j3 = 0;
                            com_google_android_gms_internal_measurement_zzki2 = com_google_android_gms_internal_measurement_zzki;
                        } else {
                            zza3 = zzga().zza(str5, l5);
                            if (zza3 != null) {
                                if (zza3.first == null) {
                                    com_google_android_gms_internal_measurement_zzki5 = (zzki) zza3.first;
                                    j = ((Long) zza3.second).longValue();
                                    zzgc();
                                    l = (Long) zzjv.zzb(com_google_android_gms_internal_measurement_zzki5, "_eid");
                                    com_google_android_gms_internal_measurement_zzki = com_google_android_gms_internal_measurement_zzki5;
                                    j2 = j - 1;
                                    if (j2 > 0) {
                                        i2 = 0;
                                        i10 = i11;
                                        j7 = 0;
                                        i8 = length;
                                        arrayMap6 = arrayMap5;
                                        i9 = 1;
                                        com_google_android_gms_internal_measurement_zzkjArr2 = com_google_android_gms_internal_measurement_zzkjArr7;
                                        zzga().zza(str5, l5, j2, com_google_android_gms_internal_measurement_zzki);
                                    } else {
                                        zzga = zzga();
                                        zzga.zzab();
                                        zzga.zzgg().zzir().zzg("Clearing complex main event info. appId", str5);
                                        writableDatabase = zzga.getWritableDatabase();
                                        str2 = "delete from main_event_params where app_id=?";
                                        arrayMap = arrayMap5;
                                        com_google_android_gms_internal_measurement_zzkjArr = com_google_android_gms_internal_measurement_zzkjArr7;
                                        i7 = 1;
                                        strArr = new String[1];
                                        i2 = 0;
                                        strArr[0] = str5;
                                        writableDatabase.execSQL(str2, strArr);
                                        i8 = length;
                                        i9 = i7;
                                        i10 = i11;
                                        com_google_android_gms_internal_measurement_zzkjArr2 = com_google_android_gms_internal_measurement_zzkjArr;
                                        arrayMap6 = arrayMap;
                                    }
                                    com_google_android_gms_internal_measurement_zzkjArr3 = new zzkj[(com_google_android_gms_internal_measurement_zzki.zzasv.length + com_google_android_gms_internal_measurement_zzkjArr2.length)];
                                    com_google_android_gms_internal_measurement_zzkjArr4 = com_google_android_gms_internal_measurement_zzki.zzasv;
                                    intValue = com_google_android_gms_internal_measurement_zzkjArr4.length;
                                    i3 = i2;
                                    length = i3;
                                    while (i3 < intValue) {
                                        com_google_android_gms_internal_measurement_zzkj2 = com_google_android_gms_internal_measurement_zzkjArr4[i3];
                                        zzgc();
                                        if (zzjv.zza(com_google_android_gms_internal_measurement_zzki3, com_google_android_gms_internal_measurement_zzkj2.name) != null) {
                                            i9 = length + 1;
                                            com_google_android_gms_internal_measurement_zzkjArr3[length] = com_google_android_gms_internal_measurement_zzkj2;
                                            length = i9;
                                        }
                                        i3++;
                                    }
                                    if (length > 0) {
                                        zzgg().zzin().zzg("No unique parameters in main event. eventName", str10);
                                    } else {
                                        length2 = com_google_android_gms_internal_measurement_zzkjArr2.length;
                                        intValue = i2;
                                        while (intValue < length2) {
                                            i7 = length + 1;
                                            com_google_android_gms_internal_measurement_zzkjArr3[length] = com_google_android_gms_internal_measurement_zzkjArr2[intValue];
                                            intValue++;
                                            length = i7;
                                        }
                                        if (length != com_google_android_gms_internal_measurement_zzkjArr3.length) {
                                            com_google_android_gms_internal_measurement_zzkjArr3 = (zzkj[]) Arrays.copyOf(com_google_android_gms_internal_measurement_zzkjArr3, length);
                                        }
                                        com_google_android_gms_internal_measurement_zzkjArr2 = com_google_android_gms_internal_measurement_zzkjArr3;
                                    }
                                    str6 = str10;
                                    l3 = l;
                                    j3 = 0;
                                    com_google_android_gms_internal_measurement_zzki2 = com_google_android_gms_internal_measurement_zzki;
                                }
                            }
                            arrayMap6 = arrayMap5;
                            i8 = length;
                            i10 = i11;
                            zzgg().zzil().zze("Extra parameter without existing main event. eventName, eventId", str10, l5);
                        }
                        map6 = arrayMap2;
                        str2 = str5;
                        map7 = map;
                        map5 = map2;
                        arrayMap7 = arrayMap6;
                        i6 = i10 + 1;
                        com_google_android_gms_internal_measurement_zzkiArr2 = com_google_android_gms_internal_measurement_zzkiArr;
                        str5 = str2;
                        length = i8;
                        arrayMap5 = arrayMap7;
                        map2 = map5;
                        map = map7;
                        arrayMap2 = map6;
                        com_google_android_gms_internal_measurement_zzknArr2 = com_google_android_gms_internal_measurement_zzknArr;
                    }
                    zze = zzga().zze(str5, com_google_android_gms_internal_measurement_zzki3.name);
                    if (zze != null) {
                        hashSet3 = hashSet2;
                        map14 = arrayMap2;
                        com_google_android_gms_internal_measurement_zzkjArr5 = com_google_android_gms_internal_measurement_zzkjArr2;
                        str7 = str6;
                        map12 = map;
                        map13 = map2;
                        j4 = j3;
                        zze = zze.zzie();
                    } else {
                        zzgg().zzin().zze("Event aggregate wasn't created during raw event logging. appId, event", zzfg.zzbh(str), zzgb().zzbe(str6));
                        map12 = map;
                        map13 = map2;
                        hashSet3 = hashSet2;
                        map14 = arrayMap2;
                        com_google_android_gms_internal_measurement_zzkjArr5 = com_google_android_gms_internal_measurement_zzkjArr2;
                        j4 = j3;
                        str7 = str6;
                        zze = new zzeq(str5, com_google_android_gms_internal_measurement_zzki3.name, 1, 1, com_google_android_gms_internal_measurement_zzki3.zzasw.longValue(), 0, null, null, null);
                    }
                    zzga().zza(zze);
                    j5 = zze.zzafp;
                    map15 = arrayMap6;
                    str10 = str7;
                    map16 = (Map) map15.get(str10);
                    if (map16 != null) {
                        str8 = str;
                    } else {
                        str8 = str;
                        map16 = zzga().zzj(str8, str10);
                        if (map16 == null) {
                            map16 = new ArrayMap();
                        }
                        map15.put(str10, map16);
                    }
                    it4 = map16.keySet().iterator();
                    while (it4.hasNext()) {
                        i = ((Integer) it4.next()).intValue();
                        hashSet4 = hashSet3;
                        if (hashSet4.contains(Integer.valueOf(i))) {
                            arrayMap2 = map14;
                            map3 = map12;
                            bitSet5 = (BitSet) map3.get(Integer.valueOf(i));
                            map4 = map15;
                            map15 = map13;
                            bitSet = (BitSet) map15.get(Integer.valueOf(i));
                            if (((zzkh) arrayMap2.get(Integer.valueOf(i))) == null) {
                                com_google_android_gms_internal_measurement_zzkh = new zzkh();
                                arrayMap2.put(Integer.valueOf(i), com_google_android_gms_internal_measurement_zzkh);
                                com_google_android_gms_internal_measurement_zzkh.zzast = Boolean.valueOf(true);
                                bitSet2 = new BitSet();
                                map3.put(Integer.valueOf(i), bitSet2);
                                bitSet = new BitSet();
                                map15.put(Integer.valueOf(i), bitSet);
                                bitSet5 = bitSet2;
                            }
                            it = ((List) map16.get(Integer.valueOf(i))).iterator();
                            while (it.hasNext()) {
                                map17 = map16;
                                com_google_android_gms_internal_measurement_zzjz = (zzjz) it.next();
                                it5 = it4;
                                it6 = it;
                                if (zzgg().isLoggable(2)) {
                                    map5 = map15;
                                    map6 = arrayMap2;
                                    map7 = map3;
                                } else {
                                    map5 = map15;
                                    map7 = map3;
                                    map6 = arrayMap2;
                                    zzgg().zzir().zzd("Evaluating filter. audience, filter, event", Integer.valueOf(i), com_google_android_gms_internal_measurement_zzjz.zzark, zzgb().zzbe(com_google_android_gms_internal_measurement_zzjz.zzarl));
                                    zzgg().zzir().zzg("Filter definition", zzgb().zza(com_google_android_gms_internal_measurement_zzjz));
                                }
                                if (com_google_android_gms_internal_measurement_zzjz.zzark != null) {
                                    if (com_google_android_gms_internal_measurement_zzjz.zzark.intValue() > 256) {
                                        if (bitSet5.get(com_google_android_gms_internal_measurement_zzjz.zzark.intValue())) {
                                            if (com_google_android_gms_internal_measurement_zzjz.zzaro != null) {
                                                zza = zza(j5, com_google_android_gms_internal_measurement_zzjz.zzaro);
                                                if (zza != null) {
                                                    if (zza.booleanValue()) {
                                                        j6 = j5;
                                                        valueOf = Boolean.valueOf(false);
                                                        com_google_android_gms_internal_measurement_zzkjArr6 = com_google_android_gms_internal_measurement_zzkjArr5;
                                                        if (valueOf != null) {
                                                        }
                                                        zzgg().zzir().zzg("Event filter result", valueOf != null ? "null" : valueOf);
                                                        if (valueOf != null) {
                                                            bitSet.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                            if (valueOf.booleanValue()) {
                                                                bitSet5.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                            }
                                                        } else {
                                                            hashSet4.add(Integer.valueOf(i));
                                                        }
                                                        com_google_android_gms_internal_measurement_zzkjArr5 = com_google_android_gms_internal_measurement_zzkjArr6;
                                                        map16 = map17;
                                                        it4 = it5;
                                                        it = it6;
                                                        map15 = map5;
                                                        map3 = map7;
                                                        arrayMap2 = map6;
                                                        j5 = j6;
                                                    }
                                                }
                                                j6 = j5;
                                                com_google_android_gms_internal_measurement_zzkjArr6 = com_google_android_gms_internal_measurement_zzkjArr5;
                                                valueOf = null;
                                                if (valueOf != null) {
                                                }
                                                zzgg().zzir().zzg("Event filter result", valueOf != null ? "null" : valueOf);
                                                if (valueOf != null) {
                                                    hashSet4.add(Integer.valueOf(i));
                                                } else {
                                                    bitSet.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                    if (valueOf.booleanValue()) {
                                                        bitSet5.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                    }
                                                }
                                                com_google_android_gms_internal_measurement_zzkjArr5 = com_google_android_gms_internal_measurement_zzkjArr6;
                                                map16 = map17;
                                                it4 = it5;
                                                it = it6;
                                                map15 = map5;
                                                map3 = map7;
                                                arrayMap2 = map6;
                                                j5 = j6;
                                            }
                                            hashSet = new HashSet();
                                            for (i9 = 0; i9 < i4; i9++) {
                                                if (TextUtils.isEmpty(com_google_android_gms_internal_measurement_zzka2.zzart)) {
                                                    zzgg().zzin().zzg("null or empty param name in filter. event", zzgb().zzbe(str10));
                                                    j6 = j5;
                                                    com_google_android_gms_internal_measurement_zzkjArr6 = com_google_android_gms_internal_measurement_zzkjArr5;
                                                    break;
                                                }
                                                hashSet.add(com_google_android_gms_internal_measurement_zzka2.zzart);
                                            }
                                            map15 = new ArrayMap();
                                            com_google_android_gms_internal_measurement_zzkjArr6 = com_google_android_gms_internal_measurement_zzkjArr5;
                                            length3 = com_google_android_gms_internal_measurement_zzkjArr6.length;
                                            i4 = 0;
                                            while (i4 < length3) {
                                                com_google_android_gms_internal_measurement_zzkj = com_google_android_gms_internal_measurement_zzkjArr6[i4];
                                                j6 = j5;
                                                if (!hashSet.contains(com_google_android_gms_internal_measurement_zzkj.name)) {
                                                    if (com_google_android_gms_internal_measurement_zzkj.zzasz == null) {
                                                        obj = com_google_android_gms_internal_measurement_zzkj.name;
                                                        obj3 = com_google_android_gms_internal_measurement_zzkj.zzasz;
                                                    } else if (com_google_android_gms_internal_measurement_zzkj.zzaqx == null) {
                                                        obj = com_google_android_gms_internal_measurement_zzkj.name;
                                                        obj3 = com_google_android_gms_internal_measurement_zzkj.zzaqx;
                                                    } else if (com_google_android_gms_internal_measurement_zzkj.zzajf != null) {
                                                        zzin = zzgg().zzin();
                                                        str3 = "Unknown value for param. event, param";
                                                        zzbe = zzgb().zzbe(str10);
                                                        zzbf = zzgb().zzbf(com_google_android_gms_internal_measurement_zzkj.name);
                                                        break;
                                                    } else {
                                                        obj = com_google_android_gms_internal_measurement_zzkj.name;
                                                        obj3 = com_google_android_gms_internal_measurement_zzkj.zzajf;
                                                    }
                                                    map15.put(obj, obj3);
                                                }
                                                i4++;
                                                j5 = j6;
                                            }
                                            j6 = j5;
                                            com_google_android_gms_internal_measurement_zzkaArr2 = com_google_android_gms_internal_measurement_zzjz.zzarm;
                                            length2 = com_google_android_gms_internal_measurement_zzkaArr2.length;
                                            i7 = 0;
                                            while (i7 < length2) {
                                                com_google_android_gms_internal_measurement_zzka = com_google_android_gms_internal_measurement_zzkaArr2[i7];
                                                equals = Boolean.TRUE.equals(com_google_android_gms_internal_measurement_zzka.zzars);
                                                str6 = com_google_android_gms_internal_measurement_zzka.zzart;
                                                if (TextUtils.isEmpty(str6)) {
                                                    com_google_android_gms_internal_measurement_zzkaArr = com_google_android_gms_internal_measurement_zzkaArr2;
                                                    obj = map15.get(str6);
                                                    i5 = length2;
                                                    if (obj instanceof Long) {
                                                        if (obj instanceof Double) {
                                                            if (!(obj instanceof String)) {
                                                                if (obj == null) {
                                                                    zzgg().zzir().zze("Missing param for filter. event, param", zzgb().zzbe(str10), zzgb().zzbf(str6));
                                                                    z = false;
                                                                    break;
                                                                }
                                                                zzin = zzgg().zzin();
                                                                str3 = "Unknown param type. event, param";
                                                            } else {
                                                                if (com_google_android_gms_internal_measurement_zzka.zzarq != null) {
                                                                    zza2 = zza((String) obj, com_google_android_gms_internal_measurement_zzka.zzarq);
                                                                } else if (com_google_android_gms_internal_measurement_zzka.zzarr == null) {
                                                                    zzin = zzgg().zzin();
                                                                    str3 = "No filter for String param. event, param";
                                                                } else {
                                                                    str9 = (String) obj;
                                                                    if (zzjv.zzcd(str9)) {
                                                                        zzin = zzgg().zzin();
                                                                        str3 = "Invalid param value for number filter. event, param";
                                                                    } else {
                                                                        zza2 = zza(str9, com_google_android_gms_internal_measurement_zzka.zzarr);
                                                                    }
                                                                }
                                                                if (zza2 == null) {
                                                                    if (((zza2.booleanValue() ^ 1) ^ equals) != 0) {
                                                                        z = false;
                                                                        break;
                                                                    }
                                                                    i7++;
                                                                    com_google_android_gms_internal_measurement_zzkaArr2 = com_google_android_gms_internal_measurement_zzkaArr;
                                                                    length2 = i5;
                                                                }
                                                            }
                                                        } else if (com_google_android_gms_internal_measurement_zzka.zzarr != null) {
                                                            zza2 = zza(((Double) obj).doubleValue(), com_google_android_gms_internal_measurement_zzka.zzarr);
                                                            if (zza2 == null) {
                                                                if (((zza2.booleanValue() ^ 1) ^ equals) != 0) {
                                                                    z = false;
                                                                    break;
                                                                }
                                                                i7++;
                                                                com_google_android_gms_internal_measurement_zzkaArr2 = com_google_android_gms_internal_measurement_zzkaArr;
                                                                length2 = i5;
                                                            }
                                                        } else {
                                                            zzin = zzgg().zzin();
                                                            str3 = "No number filter for double param. event, param";
                                                        }
                                                    } else if (com_google_android_gms_internal_measurement_zzka.zzarr != null) {
                                                        zza2 = zza(((Long) obj).longValue(), com_google_android_gms_internal_measurement_zzka.zzarr);
                                                        if (zza2 == null) {
                                                            if (((zza2.booleanValue() ^ 1) ^ equals) != 0) {
                                                                z = false;
                                                                break;
                                                            }
                                                            i7++;
                                                            com_google_android_gms_internal_measurement_zzkaArr2 = com_google_android_gms_internal_measurement_zzkaArr;
                                                            length2 = i5;
                                                        }
                                                    } else {
                                                        zzin = zzgg().zzin();
                                                        str3 = "No number filter for long param. event, param";
                                                    }
                                                    zzbe = zzgb().zzbe(str10);
                                                    zzbf = zzgb().zzbf(str6);
                                                    zzin.zze(str3, zzbe, zzbf);
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
                                                bitSet.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                if (valueOf.booleanValue()) {
                                                    bitSet5.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                }
                                            } else {
                                                hashSet4.add(Integer.valueOf(i));
                                            }
                                            com_google_android_gms_internal_measurement_zzkjArr5 = com_google_android_gms_internal_measurement_zzkjArr6;
                                            map16 = map17;
                                            it4 = it5;
                                            it = it6;
                                            map15 = map5;
                                            map3 = map7;
                                            arrayMap2 = map6;
                                            j5 = j6;
                                        } else {
                                            zzgg().zzir().zze("Event filter already evaluated true. audience ID, filter ID", Integer.valueOf(i), com_google_android_gms_internal_measurement_zzjz.zzark);
                                            map16 = map17;
                                            it4 = it5;
                                            it = it6;
                                            map15 = map5;
                                            map3 = map7;
                                            arrayMap2 = map6;
                                        }
                                        str8 = str;
                                    }
                                }
                                j6 = j5;
                                com_google_android_gms_internal_measurement_zzkjArr6 = com_google_android_gms_internal_measurement_zzkjArr5;
                                str2 = str;
                                zzgg().zzin().zze("Invalid event filter ID. appId, id", zzfg.zzbh(str), String.valueOf(com_google_android_gms_internal_measurement_zzjz.zzark));
                                com_google_android_gms_internal_measurement_zzkjArr5 = com_google_android_gms_internal_measurement_zzkjArr6;
                                map16 = map17;
                                it4 = it5;
                                it = it6;
                                map3 = map7;
                                arrayMap2 = map6;
                                j5 = j6;
                                str8 = str2;
                                map15 = map5;
                            }
                            map5 = map15;
                            str2 = str8;
                            hashSet3 = hashSet4;
                            map14 = arrayMap2;
                            map12 = map3;
                            map15 = map4;
                            map13 = map5;
                        } else {
                            zzgg().zzir().zzg("Skipping failed audience ID", Integer.valueOf(i));
                            hashSet3 = hashSet4;
                        }
                    }
                    arrayMap7 = map15;
                    str2 = str8;
                    map7 = map12;
                    map5 = map13;
                    hashSet2 = hashSet3;
                    map6 = map14;
                    l2 = l3;
                    j = j2;
                    i6 = i10 + 1;
                    com_google_android_gms_internal_measurement_zzkiArr2 = com_google_android_gms_internal_measurement_zzkiArr;
                    str5 = str2;
                    length = i8;
                    arrayMap5 = arrayMap7;
                    map2 = map5;
                    map = map7;
                    arrayMap2 = map6;
                    com_google_android_gms_internal_measurement_zzknArr2 = com_google_android_gms_internal_measurement_zzknArr;
                } else {
                    i10 = i6;
                    arrayMap6 = arrayMap5;
                    i8 = length;
                    com_google_android_gms_internal_measurement_zzkjArr2 = com_google_android_gms_internal_measurement_zzkjArr7;
                }
                j3 = 0;
                str6 = str10;
                j2 = j;
                l3 = l2;
                zze = zzga().zze(str5, com_google_android_gms_internal_measurement_zzki3.name);
                if (zze != null) {
                    zzgg().zzin().zze("Event aggregate wasn't created during raw event logging. appId, event", zzfg.zzbh(str), zzgb().zzbe(str6));
                    map12 = map;
                    map13 = map2;
                    hashSet3 = hashSet2;
                    map14 = arrayMap2;
                    com_google_android_gms_internal_measurement_zzkjArr5 = com_google_android_gms_internal_measurement_zzkjArr2;
                    j4 = j3;
                    str7 = str6;
                    zze = new zzeq(str5, com_google_android_gms_internal_measurement_zzki3.name, 1, 1, com_google_android_gms_internal_measurement_zzki3.zzasw.longValue(), 0, null, null, null);
                } else {
                    hashSet3 = hashSet2;
                    map14 = arrayMap2;
                    com_google_android_gms_internal_measurement_zzkjArr5 = com_google_android_gms_internal_measurement_zzkjArr2;
                    str7 = str6;
                    map12 = map;
                    map13 = map2;
                    j4 = j3;
                    zze = zze.zzie();
                }
                zzga().zza(zze);
                j5 = zze.zzafp;
                map15 = arrayMap6;
                str10 = str7;
                map16 = (Map) map15.get(str10);
                if (map16 != null) {
                    str8 = str;
                    map16 = zzga().zzj(str8, str10);
                    if (map16 == null) {
                        map16 = new ArrayMap();
                    }
                    map15.put(str10, map16);
                } else {
                    str8 = str;
                }
                it4 = map16.keySet().iterator();
                while (it4.hasNext()) {
                    i = ((Integer) it4.next()).intValue();
                    hashSet4 = hashSet3;
                    if (hashSet4.contains(Integer.valueOf(i))) {
                        zzgg().zzir().zzg("Skipping failed audience ID", Integer.valueOf(i));
                        hashSet3 = hashSet4;
                    } else {
                        arrayMap2 = map14;
                        map3 = map12;
                        bitSet5 = (BitSet) map3.get(Integer.valueOf(i));
                        map4 = map15;
                        map15 = map13;
                        bitSet = (BitSet) map15.get(Integer.valueOf(i));
                        if (((zzkh) arrayMap2.get(Integer.valueOf(i))) == null) {
                            com_google_android_gms_internal_measurement_zzkh = new zzkh();
                            arrayMap2.put(Integer.valueOf(i), com_google_android_gms_internal_measurement_zzkh);
                            com_google_android_gms_internal_measurement_zzkh.zzast = Boolean.valueOf(true);
                            bitSet2 = new BitSet();
                            map3.put(Integer.valueOf(i), bitSet2);
                            bitSet = new BitSet();
                            map15.put(Integer.valueOf(i), bitSet);
                            bitSet5 = bitSet2;
                        }
                        it = ((List) map16.get(Integer.valueOf(i))).iterator();
                        while (it.hasNext()) {
                            map17 = map16;
                            com_google_android_gms_internal_measurement_zzjz = (zzjz) it.next();
                            it5 = it4;
                            it6 = it;
                            if (zzgg().isLoggable(2)) {
                                map5 = map15;
                                map7 = map3;
                                map6 = arrayMap2;
                                zzgg().zzir().zzd("Evaluating filter. audience, filter, event", Integer.valueOf(i), com_google_android_gms_internal_measurement_zzjz.zzark, zzgb().zzbe(com_google_android_gms_internal_measurement_zzjz.zzarl));
                                zzgg().zzir().zzg("Filter definition", zzgb().zza(com_google_android_gms_internal_measurement_zzjz));
                            } else {
                                map5 = map15;
                                map6 = arrayMap2;
                                map7 = map3;
                            }
                            if (com_google_android_gms_internal_measurement_zzjz.zzark != null) {
                                if (com_google_android_gms_internal_measurement_zzjz.zzark.intValue() > 256) {
                                    if (bitSet5.get(com_google_android_gms_internal_measurement_zzjz.zzark.intValue())) {
                                        zzgg().zzir().zze("Event filter already evaluated true. audience ID, filter ID", Integer.valueOf(i), com_google_android_gms_internal_measurement_zzjz.zzark);
                                        map16 = map17;
                                        it4 = it5;
                                        it = it6;
                                        map15 = map5;
                                        map3 = map7;
                                        arrayMap2 = map6;
                                    } else {
                                        if (com_google_android_gms_internal_measurement_zzjz.zzaro != null) {
                                            zza = zza(j5, com_google_android_gms_internal_measurement_zzjz.zzaro);
                                            if (zza != null) {
                                                if (zza.booleanValue()) {
                                                    j6 = j5;
                                                    valueOf = Boolean.valueOf(false);
                                                    com_google_android_gms_internal_measurement_zzkjArr6 = com_google_android_gms_internal_measurement_zzkjArr5;
                                                    if (valueOf != null) {
                                                    }
                                                    zzgg().zzir().zzg("Event filter result", valueOf != null ? "null" : valueOf);
                                                    if (valueOf != null) {
                                                        hashSet4.add(Integer.valueOf(i));
                                                    } else {
                                                        bitSet.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                        if (valueOf.booleanValue()) {
                                                            bitSet5.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                        }
                                                    }
                                                    com_google_android_gms_internal_measurement_zzkjArr5 = com_google_android_gms_internal_measurement_zzkjArr6;
                                                    map16 = map17;
                                                    it4 = it5;
                                                    it = it6;
                                                    map15 = map5;
                                                    map3 = map7;
                                                    arrayMap2 = map6;
                                                    j5 = j6;
                                                }
                                            }
                                            j6 = j5;
                                            com_google_android_gms_internal_measurement_zzkjArr6 = com_google_android_gms_internal_measurement_zzkjArr5;
                                            valueOf = null;
                                            if (valueOf != null) {
                                            }
                                            zzgg().zzir().zzg("Event filter result", valueOf != null ? "null" : valueOf);
                                            if (valueOf != null) {
                                                bitSet.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                if (valueOf.booleanValue()) {
                                                    bitSet5.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                                }
                                            } else {
                                                hashSet4.add(Integer.valueOf(i));
                                            }
                                            com_google_android_gms_internal_measurement_zzkjArr5 = com_google_android_gms_internal_measurement_zzkjArr6;
                                            map16 = map17;
                                            it4 = it5;
                                            it = it6;
                                            map15 = map5;
                                            map3 = map7;
                                            arrayMap2 = map6;
                                            j5 = j6;
                                        }
                                        hashSet = new HashSet();
                                        for (i9 = 0; i9 < i4; i9++) {
                                            if (TextUtils.isEmpty(com_google_android_gms_internal_measurement_zzka2.zzart)) {
                                                zzgg().zzin().zzg("null or empty param name in filter. event", zzgb().zzbe(str10));
                                                j6 = j5;
                                                com_google_android_gms_internal_measurement_zzkjArr6 = com_google_android_gms_internal_measurement_zzkjArr5;
                                                break;
                                            }
                                            hashSet.add(com_google_android_gms_internal_measurement_zzka2.zzart);
                                        }
                                        map15 = new ArrayMap();
                                        com_google_android_gms_internal_measurement_zzkjArr6 = com_google_android_gms_internal_measurement_zzkjArr5;
                                        length3 = com_google_android_gms_internal_measurement_zzkjArr6.length;
                                        i4 = 0;
                                        while (i4 < length3) {
                                            com_google_android_gms_internal_measurement_zzkj = com_google_android_gms_internal_measurement_zzkjArr6[i4];
                                            j6 = j5;
                                            if (!hashSet.contains(com_google_android_gms_internal_measurement_zzkj.name)) {
                                                if (com_google_android_gms_internal_measurement_zzkj.zzasz == null) {
                                                    if (com_google_android_gms_internal_measurement_zzkj.zzaqx == null) {
                                                        if (com_google_android_gms_internal_measurement_zzkj.zzajf != null) {
                                                            zzin = zzgg().zzin();
                                                            str3 = "Unknown value for param. event, param";
                                                            zzbe = zzgb().zzbe(str10);
                                                            zzbf = zzgb().zzbf(com_google_android_gms_internal_measurement_zzkj.name);
                                                            break;
                                                        }
                                                        obj = com_google_android_gms_internal_measurement_zzkj.name;
                                                        obj3 = com_google_android_gms_internal_measurement_zzkj.zzajf;
                                                    } else {
                                                        obj = com_google_android_gms_internal_measurement_zzkj.name;
                                                        obj3 = com_google_android_gms_internal_measurement_zzkj.zzaqx;
                                                    }
                                                } else {
                                                    obj = com_google_android_gms_internal_measurement_zzkj.name;
                                                    obj3 = com_google_android_gms_internal_measurement_zzkj.zzasz;
                                                }
                                                map15.put(obj, obj3);
                                            }
                                            i4++;
                                            j5 = j6;
                                        }
                                        j6 = j5;
                                        com_google_android_gms_internal_measurement_zzkaArr2 = com_google_android_gms_internal_measurement_zzjz.zzarm;
                                        length2 = com_google_android_gms_internal_measurement_zzkaArr2.length;
                                        i7 = 0;
                                        while (i7 < length2) {
                                            com_google_android_gms_internal_measurement_zzka = com_google_android_gms_internal_measurement_zzkaArr2[i7];
                                            equals = Boolean.TRUE.equals(com_google_android_gms_internal_measurement_zzka.zzars);
                                            str6 = com_google_android_gms_internal_measurement_zzka.zzart;
                                            if (TextUtils.isEmpty(str6)) {
                                                zzgg().zzin().zzg("Event has empty param name. event", zzgb().zzbe(str10));
                                            } else {
                                                com_google_android_gms_internal_measurement_zzkaArr = com_google_android_gms_internal_measurement_zzkaArr2;
                                                obj = map15.get(str6);
                                                i5 = length2;
                                                if (obj instanceof Long) {
                                                    if (com_google_android_gms_internal_measurement_zzka.zzarr != null) {
                                                        zzin = zzgg().zzin();
                                                        str3 = "No number filter for long param. event, param";
                                                    } else {
                                                        zza2 = zza(((Long) obj).longValue(), com_google_android_gms_internal_measurement_zzka.zzarr);
                                                        if (zza2 == null) {
                                                            if (((zza2.booleanValue() ^ 1) ^ equals) != 0) {
                                                                z = false;
                                                                break;
                                                            }
                                                            i7++;
                                                            com_google_android_gms_internal_measurement_zzkaArr2 = com_google_android_gms_internal_measurement_zzkaArr;
                                                            length2 = i5;
                                                        }
                                                    }
                                                } else if (obj instanceof Double) {
                                                    if (com_google_android_gms_internal_measurement_zzka.zzarr != null) {
                                                        zzin = zzgg().zzin();
                                                        str3 = "No number filter for double param. event, param";
                                                    } else {
                                                        zza2 = zza(((Double) obj).doubleValue(), com_google_android_gms_internal_measurement_zzka.zzarr);
                                                        if (zza2 == null) {
                                                            if (((zza2.booleanValue() ^ 1) ^ equals) != 0) {
                                                                z = false;
                                                                break;
                                                            }
                                                            i7++;
                                                            com_google_android_gms_internal_measurement_zzkaArr2 = com_google_android_gms_internal_measurement_zzkaArr;
                                                            length2 = i5;
                                                        }
                                                    }
                                                } else if (!(obj instanceof String)) {
                                                    if (com_google_android_gms_internal_measurement_zzka.zzarq != null) {
                                                        zza2 = zza((String) obj, com_google_android_gms_internal_measurement_zzka.zzarq);
                                                    } else if (com_google_android_gms_internal_measurement_zzka.zzarr == null) {
                                                        str9 = (String) obj;
                                                        if (zzjv.zzcd(str9)) {
                                                            zza2 = zza(str9, com_google_android_gms_internal_measurement_zzka.zzarr);
                                                        } else {
                                                            zzin = zzgg().zzin();
                                                            str3 = "Invalid param value for number filter. event, param";
                                                        }
                                                    } else {
                                                        zzin = zzgg().zzin();
                                                        str3 = "No filter for String param. event, param";
                                                    }
                                                    if (zza2 == null) {
                                                        if (((zza2.booleanValue() ^ 1) ^ equals) != 0) {
                                                            z = false;
                                                            break;
                                                        }
                                                        i7++;
                                                        com_google_android_gms_internal_measurement_zzkaArr2 = com_google_android_gms_internal_measurement_zzkaArr;
                                                        length2 = i5;
                                                    }
                                                } else if (obj == null) {
                                                    zzgg().zzir().zze("Missing param for filter. event, param", zzgb().zzbe(str10), zzgb().zzbf(str6));
                                                    z = false;
                                                    break;
                                                } else {
                                                    zzin = zzgg().zzin();
                                                    str3 = "Unknown param type. event, param";
                                                }
                                                zzbe = zzgb().zzbe(str10);
                                                zzbf = zzgb().zzbf(str6);
                                                zzin.zze(str3, zzbe, zzbf);
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
                                            bitSet.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                            if (valueOf.booleanValue()) {
                                                bitSet5.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                            }
                                        }
                                        com_google_android_gms_internal_measurement_zzkjArr5 = com_google_android_gms_internal_measurement_zzkjArr6;
                                        map16 = map17;
                                        it4 = it5;
                                        it = it6;
                                        map15 = map5;
                                        map3 = map7;
                                        arrayMap2 = map6;
                                        j5 = j6;
                                    }
                                    str8 = str;
                                }
                            }
                            j6 = j5;
                            com_google_android_gms_internal_measurement_zzkjArr6 = com_google_android_gms_internal_measurement_zzkjArr5;
                            str2 = str;
                            zzgg().zzin().zze("Invalid event filter ID. appId, id", zzfg.zzbh(str), String.valueOf(com_google_android_gms_internal_measurement_zzjz.zzark));
                            com_google_android_gms_internal_measurement_zzkjArr5 = com_google_android_gms_internal_measurement_zzkjArr6;
                            map16 = map17;
                            it4 = it5;
                            it = it6;
                            map3 = map7;
                            arrayMap2 = map6;
                            j5 = j6;
                            str8 = str2;
                            map15 = map5;
                        }
                        map5 = map15;
                        str2 = str8;
                        hashSet3 = hashSet4;
                        map14 = arrayMap2;
                        map12 = map3;
                        map15 = map4;
                        map13 = map5;
                    }
                }
                arrayMap7 = map15;
                str2 = str8;
                map7 = map12;
                map5 = map13;
                hashSet2 = hashSet3;
                map6 = map14;
                l2 = l3;
                j = j2;
                i6 = i10 + 1;
                com_google_android_gms_internal_measurement_zzkiArr2 = com_google_android_gms_internal_measurement_zzkiArr;
                str5 = str2;
                length = i8;
                arrayMap5 = arrayMap7;
                map2 = map5;
                map = map7;
                arrayMap2 = map6;
                com_google_android_gms_internal_measurement_zzknArr2 = com_google_android_gms_internal_measurement_zzknArr;
            }
        }
        map6 = arrayMap2;
        str2 = str5;
        map7 = map;
        map5 = map2;
        zzkn[] com_google_android_gms_internal_measurement_zzknArr3 = com_google_android_gms_internal_measurement_zzknArr2;
        if (com_google_android_gms_internal_measurement_zzknArr3 != null) {
            Map arrayMap8 = new ArrayMap();
            i3 = com_google_android_gms_internal_measurement_zzknArr3.length;
            length = 0;
            while (length < i3) {
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
                    if (hashSet2.contains(Integer.valueOf(intValue2))) {
                        zzgg().zzir().zzg("Skipping failed audience ID", Integer.valueOf(intValue2));
                    } else {
                        Map map21;
                        map8 = map6;
                        Map map22 = map7;
                        BitSet bitSet6 = (BitSet) map22.get(Integer.valueOf(intValue2));
                        map18 = arrayMap8;
                        arrayMap8 = map5;
                        BitSet bitSet7 = (BitSet) arrayMap8.get(Integer.valueOf(intValue2));
                        if (((zzkh) map8.get(Integer.valueOf(intValue2))) == null) {
                            com_google_android_gms_internal_measurement_zzkh2 = new zzkh();
                            map8.put(Integer.valueOf(intValue2), com_google_android_gms_internal_measurement_zzkh2);
                            com_google_android_gms_internal_measurement_zzkh2.zzast = Boolean.valueOf(true);
                            bitSet6 = new BitSet();
                            map22.put(Integer.valueOf(intValue2), bitSet6);
                            bitSet7 = new BitSet();
                            arrayMap8.put(Integer.valueOf(intValue2), bitSet7);
                        }
                        Iterator it7 = ((List) map20.get(Integer.valueOf(intValue2))).iterator();
                        while (it7.hasNext()) {
                            Iterator it8;
                            i12 = i3;
                            zzkc com_google_android_gms_internal_measurement_zzkc = (zzkc) it7.next();
                            map21 = map20;
                            Iterator it9 = it;
                            if (zzgg().isLoggable(2)) {
                                it8 = it7;
                                map9 = arrayMap8;
                                map10 = map8;
                                map19 = map22;
                                zzgg().zzir().zzd("Evaluating filter. audience, filter, property", Integer.valueOf(intValue2), com_google_android_gms_internal_measurement_zzkc.zzark, zzgb().zzbg(com_google_android_gms_internal_measurement_zzkc.zzasa));
                                zzgg().zzir().zzg("Filter definition", zzgb().zza(com_google_android_gms_internal_measurement_zzkc));
                            } else {
                                map9 = arrayMap8;
                                it8 = it7;
                                map10 = map8;
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
                                                    hashSet2.add(Integer.valueOf(intValue2));
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
                                                hashSet2.add(Integer.valueOf(intValue2));
                                            }
                                        }
                                        zzin2.zzg(str11, zzgb().zzbg(com_google_android_gms_internal_measurement_zzkn.name));
                                        valueOf = null;
                                        if (valueOf == null) {
                                        }
                                        zzgg().zzir().zzg("Property filter result", valueOf == null ? "null" : valueOf);
                                        if (valueOf == null) {
                                            hashSet2.add(Integer.valueOf(intValue2));
                                        } else {
                                            bitSet7.set(com_google_android_gms_internal_measurement_zzkc.zzark.intValue());
                                            if (!valueOf.booleanValue()) {
                                                bitSet6.set(com_google_android_gms_internal_measurement_zzkc.zzark.intValue());
                                            }
                                        }
                                    }
                                    i3 = i12;
                                    map20 = map21;
                                    it = it9;
                                    it7 = it8;
                                    arrayMap8 = map9;
                                    map8 = map10;
                                    map22 = map19;
                                }
                            }
                            zzgg().zzin().zze("Invalid property filter ID. appId, id", zzfg.zzbh(str), String.valueOf(com_google_android_gms_internal_measurement_zzkc.zzark));
                            hashSet2.add(Integer.valueOf(intValue2));
                            arrayMap8 = map18;
                            i3 = i12;
                            map20 = map21;
                            it = it9;
                            map5 = map9;
                            map6 = map10;
                            map7 = map19;
                        }
                        map21 = map20;
                        map5 = arrayMap8;
                        map6 = map8;
                        map7 = map22;
                        arrayMap8 = map18;
                        com_google_android_gms_internal_measurement_zzknArr3 = com_google_android_gms_internal_measurement_zzknArr;
                    }
                }
                map18 = arrayMap8;
                i12 = i3;
                map9 = map5;
                map19 = map7;
                map10 = map6;
                length++;
                com_google_android_gms_internal_measurement_zzknArr3 = com_google_android_gms_internal_measurement_zzknArr;
            }
        }
        map9 = map5;
        map10 = map6;
        zzba = map7;
        zzkh[] com_google_android_gms_internal_measurement_zzkhArr = new zzkh[zzba.size()];
        i = 0;
        for (Integer intValue3 : zzba.keySet()) {
            length = intValue3.intValue();
            if (!hashSet2.contains(Integer.valueOf(length))) {
                arrayMap3 = map10;
                zzkh com_google_android_gms_internal_measurement_zzkh3 = (zzkh) arrayMap3.get(Integer.valueOf(length));
                if (com_google_android_gms_internal_measurement_zzkh3 == null) {
                    com_google_android_gms_internal_measurement_zzkh3 = new zzkh();
                }
                intValue2 = i + 1;
                com_google_android_gms_internal_measurement_zzkhArr[i] = com_google_android_gms_internal_measurement_zzkh3;
                com_google_android_gms_internal_measurement_zzkh3.zzarg = Integer.valueOf(length);
                com_google_android_gms_internal_measurement_zzkh3.zzasr = new zzkm();
                com_google_android_gms_internal_measurement_zzkh3.zzasr.zzaug = zzjv.zza((BitSet) zzba.get(Integer.valueOf(length)));
                map8 = map9;
                com_google_android_gms_internal_measurement_zzkh3.zzasr.zzauf = zzjv.zza((BitSet) map8.get(Integer.valueOf(length)));
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
                            obj2 = e;
                            zzil = zzga2.zzgg().zzil();
                            str4 = "Error storing filter results. appId";
                            zzil.zze(str4, zzfg.zzbh(str), obj2);
                            map10 = arrayMap3;
                            i = intValue2;
                            map9 = map8;
                        }
                    } catch (SQLiteException e6) {
                        e = e6;
                        obj2 = e;
                        zzil = zzga2.zzgg().zzil();
                        str4 = "Error storing filter results. appId";
                        zzil.zze(str4, zzfg.zzbh(str), obj2);
                        map10 = arrayMap3;
                        i = intValue2;
                        map9 = map8;
                    }
                } catch (IOException e7) {
                    obj2 = e7;
                    zzil = zzga2.zzgg().zzil();
                    str4 = "Configuration loss. Failed to serialize filter results. appId";
                    zzil.zze(str4, zzfg.zzbh(str), obj2);
                    map10 = arrayMap3;
                    i = intValue2;
                    map9 = map8;
                }
                map10 = arrayMap3;
                i = intValue2;
                map9 = map8;
            }
        }
        return (zzkh[]) Arrays.copyOf(com_google_android_gms_internal_measurement_zzkhArr, i);
    }

    protected final boolean zzhh() {
        return false;
    }
}
