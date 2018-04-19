package com.google.android.gms.internal.measurement;

import android.content.ContentValues;
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
        if (i == 6) {
            if (list == null || list.size() == 0) {
                return null;
            }
        } else if (str2 == null) {
            return null;
        }
        if (!(z || i == 1)) {
            str = str.toUpperCase(Locale.ENGLISH);
        }
        switch (i) {
            case 1:
                try {
                    return Boolean.valueOf(Pattern.compile(str3, z ? 0 : 66).matcher(str).matches());
                } catch (PatternSyntaxException e) {
                    zzgg().zzin().zzg("Invalid regular expression in REGEXP audience filter. expression", str3);
                    return null;
                }
            case 2:
                return Boolean.valueOf(str.startsWith(str2));
            case 3:
                return Boolean.valueOf(str.endsWith(str2));
            case 4:
                return Boolean.valueOf(str.contains(str2));
            case 5:
                return Boolean.valueOf(str.equals(str2));
            case 6:
                return Boolean.valueOf(list.contains(str));
            default:
                return null;
        }
    }

    private final Boolean zza(String str, zzkb com_google_android_gms_internal_measurement_zzkb) {
        Boolean bool = null;
        if (zzjv.zzcd(str)) {
            try {
                bool = zza(new BigDecimal(str), com_google_android_gms_internal_measurement_zzkb, 0.0d);
            } catch (NumberFormatException e) {
            }
        }
        return bool;
    }

    private final Boolean zza(String str, zzkd com_google_android_gms_internal_measurement_zzkd) {
        int i = 0;
        String str2 = null;
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzkd);
        if (str == null || com_google_android_gms_internal_measurement_zzkd.zzasc == null || com_google_android_gms_internal_measurement_zzkd.zzasc.intValue() == 0) {
            return null;
        }
        List list;
        if (com_google_android_gms_internal_measurement_zzkd.zzasc.intValue() == 6) {
            if (com_google_android_gms_internal_measurement_zzkd.zzasf == null || com_google_android_gms_internal_measurement_zzkd.zzasf.length == 0) {
                return null;
            }
        } else if (com_google_android_gms_internal_measurement_zzkd.zzasd == null) {
            return null;
        }
        int intValue = com_google_android_gms_internal_measurement_zzkd.zzasc.intValue();
        boolean z = com_google_android_gms_internal_measurement_zzkd.zzase != null && com_google_android_gms_internal_measurement_zzkd.zzase.booleanValue();
        String toUpperCase = (z || intValue == 1 || intValue == 6) ? com_google_android_gms_internal_measurement_zzkd.zzasd : com_google_android_gms_internal_measurement_zzkd.zzasd.toUpperCase(Locale.ENGLISH);
        if (com_google_android_gms_internal_measurement_zzkd.zzasf == null) {
            list = null;
        } else {
            String[] strArr = com_google_android_gms_internal_measurement_zzkd.zzasf;
            if (z) {
                list = Arrays.asList(strArr);
            } else {
                list = new ArrayList();
                int length = strArr.length;
                while (i < length) {
                    list.add(strArr[i].toUpperCase(Locale.ENGLISH));
                    i++;
                }
            }
        }
        if (intValue == 1) {
            str2 = toUpperCase;
        }
        return zza(str, intValue, z, toUpperCase, list, str2);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.Boolean zza(java.math.BigDecimal r10, com.google.android.gms.internal.measurement.zzkb r11, double r12) {
        /*
        r8 = 4;
        r7 = -1;
        r1 = 0;
        r0 = 1;
        r2 = 0;
        com.google.android.gms.common.internal.Preconditions.checkNotNull(r11);
        r3 = r11.zzaru;
        if (r3 == 0) goto L_0x0014;
    L_0x000c:
        r3 = r11.zzaru;
        r3 = r3.intValue();
        if (r3 != 0) goto L_0x0016;
    L_0x0014:
        r0 = r2;
    L_0x0015:
        return r0;
    L_0x0016:
        r3 = r11.zzaru;
        r3 = r3.intValue();
        if (r3 != r8) goto L_0x0028;
    L_0x001e:
        r3 = r11.zzarx;
        if (r3 == 0) goto L_0x0026;
    L_0x0022:
        r3 = r11.zzary;
        if (r3 != 0) goto L_0x002e;
    L_0x0026:
        r0 = r2;
        goto L_0x0015;
    L_0x0028:
        r3 = r11.zzarw;
        if (r3 != 0) goto L_0x002e;
    L_0x002c:
        r0 = r2;
        goto L_0x0015;
    L_0x002e:
        r3 = r11.zzaru;
        r6 = r3.intValue();
        r3 = r11.zzaru;
        r3 = r3.intValue();
        if (r3 != r8) goto L_0x0066;
    L_0x003c:
        r3 = r11.zzarx;
        r3 = com.google.android.gms.internal.measurement.zzjv.zzcd(r3);
        if (r3 == 0) goto L_0x004c;
    L_0x0044:
        r3 = r11.zzary;
        r3 = com.google.android.gms.internal.measurement.zzjv.zzcd(r3);
        if (r3 != 0) goto L_0x004e;
    L_0x004c:
        r0 = r2;
        goto L_0x0015;
    L_0x004e:
        r4 = new java.math.BigDecimal;	 Catch:{ NumberFormatException -> 0x0063 }
        r3 = r11.zzarx;	 Catch:{ NumberFormatException -> 0x0063 }
        r4.<init>(r3);	 Catch:{ NumberFormatException -> 0x0063 }
        r3 = new java.math.BigDecimal;	 Catch:{ NumberFormatException -> 0x0063 }
        r5 = r11.zzary;	 Catch:{ NumberFormatException -> 0x0063 }
        r3.<init>(r5);	 Catch:{ NumberFormatException -> 0x0063 }
        r5 = r2;
    L_0x005d:
        if (r6 != r8) goto L_0x007d;
    L_0x005f:
        if (r4 != 0) goto L_0x007f;
    L_0x0061:
        r0 = r2;
        goto L_0x0015;
    L_0x0063:
        r0 = move-exception;
        r0 = r2;
        goto L_0x0015;
    L_0x0066:
        r3 = r11.zzarw;
        r3 = com.google.android.gms.internal.measurement.zzjv.zzcd(r3);
        if (r3 != 0) goto L_0x0070;
    L_0x006e:
        r0 = r2;
        goto L_0x0015;
    L_0x0070:
        r5 = new java.math.BigDecimal;	 Catch:{ NumberFormatException -> 0x007a }
        r3 = r11.zzarw;	 Catch:{ NumberFormatException -> 0x007a }
        r5.<init>(r3);	 Catch:{ NumberFormatException -> 0x007a }
        r3 = r2;
        r4 = r2;
        goto L_0x005d;
    L_0x007a:
        r0 = move-exception;
        r0 = r2;
        goto L_0x0015;
    L_0x007d:
        if (r5 == 0) goto L_0x0082;
    L_0x007f:
        switch(r6) {
            case 1: goto L_0x0084;
            case 2: goto L_0x0091;
            case 3: goto L_0x009f;
            case 4: goto L_0x00ed;
            default: goto L_0x0082;
        };
    L_0x0082:
        r0 = r2;
        goto L_0x0015;
    L_0x0084:
        r2 = r10.compareTo(r5);
        if (r2 != r7) goto L_0x008f;
    L_0x008a:
        r0 = java.lang.Boolean.valueOf(r0);
        goto L_0x0015;
    L_0x008f:
        r0 = r1;
        goto L_0x008a;
    L_0x0091:
        r2 = r10.compareTo(r5);
        if (r2 != r0) goto L_0x009d;
    L_0x0097:
        r0 = java.lang.Boolean.valueOf(r0);
        goto L_0x0015;
    L_0x009d:
        r0 = r1;
        goto L_0x0097;
    L_0x009f:
        r2 = 0;
        r2 = (r12 > r2 ? 1 : (r12 == r2 ? 0 : -1));
        if (r2 == 0) goto L_0x00df;
    L_0x00a5:
        r2 = new java.math.BigDecimal;
        r2.<init>(r12);
        r3 = new java.math.BigDecimal;
        r4 = 2;
        r3.<init>(r4);
        r2 = r2.multiply(r3);
        r2 = r5.subtract(r2);
        r2 = r10.compareTo(r2);
        if (r2 != r0) goto L_0x00dd;
    L_0x00be:
        r2 = new java.math.BigDecimal;
        r2.<init>(r12);
        r3 = new java.math.BigDecimal;
        r4 = 2;
        r3.<init>(r4);
        r2 = r2.multiply(r3);
        r2 = r5.add(r2);
        r2 = r10.compareTo(r2);
        if (r2 != r7) goto L_0x00dd;
    L_0x00d7:
        r0 = java.lang.Boolean.valueOf(r0);
        goto L_0x0015;
    L_0x00dd:
        r0 = r1;
        goto L_0x00d7;
    L_0x00df:
        r2 = r10.compareTo(r5);
        if (r2 != 0) goto L_0x00eb;
    L_0x00e5:
        r0 = java.lang.Boolean.valueOf(r0);
        goto L_0x0015;
    L_0x00eb:
        r0 = r1;
        goto L_0x00e5;
    L_0x00ed:
        r2 = r10.compareTo(r4);
        if (r2 == r7) goto L_0x00ff;
    L_0x00f3:
        r2 = r10.compareTo(r3);
        if (r2 == r0) goto L_0x00ff;
    L_0x00f9:
        r0 = java.lang.Boolean.valueOf(r0);
        goto L_0x0015;
    L_0x00ff:
        r0 = r1;
        goto L_0x00f9;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzee.zza(java.math.BigDecimal, com.google.android.gms.internal.measurement.zzkb, double):java.lang.Boolean");
    }

    final zzkh[] zza(String str, zzki[] com_google_android_gms_internal_measurement_zzkiArr, zzkn[] com_google_android_gms_internal_measurement_zzknArr) {
        int intValue;
        BitSet bitSet;
        BitSet bitSet2;
        int i;
        zzhj zzga;
        int i2;
        int length;
        Map map;
        Map map2;
        zzkh com_google_android_gms_internal_measurement_zzkh;
        Preconditions.checkNotEmpty(str);
        HashSet hashSet = new HashSet();
        ArrayMap arrayMap = new ArrayMap();
        Map arrayMap2 = new ArrayMap();
        ArrayMap arrayMap3 = new ArrayMap();
        Map zzba = zzga().zzba(str);
        if (zzba != null) {
            for (Integer intValue2 : zzba.keySet()) {
                intValue = intValue2.intValue();
                zzkm com_google_android_gms_internal_measurement_zzkm = (zzkm) zzba.get(Integer.valueOf(intValue));
                bitSet = (BitSet) arrayMap2.get(Integer.valueOf(intValue));
                bitSet2 = (BitSet) arrayMap3.get(Integer.valueOf(intValue));
                if (bitSet == null) {
                    bitSet = new BitSet();
                    arrayMap2.put(Integer.valueOf(intValue), bitSet);
                    bitSet2 = new BitSet();
                    arrayMap3.put(Integer.valueOf(intValue), bitSet2);
                }
                for (i = 0; i < (com_google_android_gms_internal_measurement_zzkm.zzauf.length << 6); i++) {
                    if (zzjv.zza(com_google_android_gms_internal_measurement_zzkm.zzauf, i)) {
                        zzgg().zzir().zze("Filter already evaluated. audience ID, filter ID", Integer.valueOf(intValue), Integer.valueOf(i));
                        bitSet2.set(i);
                        if (zzjv.zza(com_google_android_gms_internal_measurement_zzkm.zzaug, i)) {
                            bitSet.set(i);
                        }
                    }
                }
                zzkh com_google_android_gms_internal_measurement_zzkh2 = new zzkh();
                arrayMap.put(Integer.valueOf(intValue), com_google_android_gms_internal_measurement_zzkh2);
                com_google_android_gms_internal_measurement_zzkh2.zzast = Boolean.valueOf(false);
                com_google_android_gms_internal_measurement_zzkh2.zzass = com_google_android_gms_internal_measurement_zzkm;
                com_google_android_gms_internal_measurement_zzkh2.zzasr = new zzkm();
                com_google_android_gms_internal_measurement_zzkh2.zzasr.zzaug = zzjv.zza(bitSet);
                com_google_android_gms_internal_measurement_zzkh2.zzasr.zzauf = zzjv.zza(bitSet2);
            }
        }
        if (com_google_android_gms_internal_measurement_zzkiArr != null) {
            zzki com_google_android_gms_internal_measurement_zzki = null;
            long j = 0;
            Long l = null;
            ArrayMap arrayMap4 = new ArrayMap();
            for (zzki com_google_android_gms_internal_measurement_zzki2 : com_google_android_gms_internal_measurement_zzkiArr) {
                zzkj[] com_google_android_gms_internal_measurement_zzkjArr;
                String str2;
                Long l2;
                long j2;
                zzki com_google_android_gms_internal_measurement_zzki3;
                zzeq zze;
                zzeq com_google_android_gms_internal_measurement_zzeq;
                long j3;
                int intValue3;
                BitSet bitSet3;
                BitSet bitSet4;
                String str3 = com_google_android_gms_internal_measurement_zzki2.name;
                zzkj[] com_google_android_gms_internal_measurement_zzkjArr2 = com_google_android_gms_internal_measurement_zzki2.zzasv;
                if (zzgi().zzd(str, zzew.zzahy)) {
                    int length2;
                    zzkj com_google_android_gms_internal_measurement_zzkj;
                    zzgc();
                    Long l3 = (Long) zzjv.zzb(com_google_android_gms_internal_measurement_zzki2, "_eid");
                    Object obj = l3 != null ? 1 : null;
                    Object obj2 = (obj == null || !str3.equals("_ep")) ? null : 1;
                    if (obj2 != null) {
                        zzgc();
                        str3 = (String) zzjv.zzb(com_google_android_gms_internal_measurement_zzki2, "_en");
                        if (TextUtils.isEmpty(str3)) {
                            zzgg().zzil().zzg("Extra parameter without an event name. eventId", l3);
                        } else {
                            Long l4;
                            int i3;
                            if (com_google_android_gms_internal_measurement_zzki == null || l == null || l3.longValue() != l.longValue()) {
                                Pair zza = zzga().zza(str, l3);
                                if (zza == null || zza.first == null) {
                                    zzgg().zzil().zze("Extra parameter without existing main event. eventName, eventId", str3, l3);
                                } else {
                                    zzki com_google_android_gms_internal_measurement_zzki4 = (zzki) zza.first;
                                    j = ((Long) zza.second).longValue();
                                    zzgc();
                                    l4 = (Long) zzjv.zzb(com_google_android_gms_internal_measurement_zzki4, "_eid");
                                    com_google_android_gms_internal_measurement_zzki = com_google_android_gms_internal_measurement_zzki4;
                                }
                            } else {
                                l4 = l;
                            }
                            j--;
                            if (j <= 0) {
                                zzga = zzga();
                                zzga.zzab();
                                zzga.zzgg().zzir().zzg("Clearing complex main event info. appId", str);
                                try {
                                    zzga.getWritableDatabase().execSQL("delete from main_event_params where app_id=?", new String[]{str});
                                } catch (SQLiteException e) {
                                    zzga.zzgg().zzil().zzg("Error clearing complex main event", e);
                                }
                            } else {
                                zzga().zza(str, l3, j, com_google_android_gms_internal_measurement_zzki);
                            }
                            zzkj[] com_google_android_gms_internal_measurement_zzkjArr3 = new zzkj[(com_google_android_gms_internal_measurement_zzki.zzasv.length + com_google_android_gms_internal_measurement_zzkjArr2.length)];
                            i2 = 0;
                            zzkj[] com_google_android_gms_internal_measurement_zzkjArr4 = com_google_android_gms_internal_measurement_zzki.zzasv;
                            length2 = com_google_android_gms_internal_measurement_zzkjArr4.length;
                            i = 0;
                            while (i < length2) {
                                com_google_android_gms_internal_measurement_zzkj = com_google_android_gms_internal_measurement_zzkjArr4[i];
                                zzgc();
                                if (zzjv.zza(com_google_android_gms_internal_measurement_zzki2, com_google_android_gms_internal_measurement_zzkj.name) == null) {
                                    i3 = i2 + 1;
                                    com_google_android_gms_internal_measurement_zzkjArr3[i2] = com_google_android_gms_internal_measurement_zzkj;
                                } else {
                                    i3 = i2;
                                }
                                i++;
                                i2 = i3;
                            }
                            if (i2 > 0) {
                                length = com_google_android_gms_internal_measurement_zzkjArr2.length;
                                i3 = 0;
                                while (i3 < length) {
                                    i = i2 + 1;
                                    com_google_android_gms_internal_measurement_zzkjArr3[i2] = com_google_android_gms_internal_measurement_zzkjArr2[i3];
                                    i3++;
                                    i2 = i;
                                }
                                com_google_android_gms_internal_measurement_zzkjArr = i2 == com_google_android_gms_internal_measurement_zzkjArr3.length ? com_google_android_gms_internal_measurement_zzkjArr3 : (zzkj[]) Arrays.copyOf(com_google_android_gms_internal_measurement_zzkjArr3, i2);
                                str2 = str3;
                                l2 = l4;
                                j2 = j;
                                com_google_android_gms_internal_measurement_zzki3 = com_google_android_gms_internal_measurement_zzki;
                            } else {
                                zzgg().zzin().zzg("No unique parameters in main event. eventName", str3);
                                com_google_android_gms_internal_measurement_zzkjArr = com_google_android_gms_internal_measurement_zzkjArr2;
                                str2 = str3;
                                l2 = l4;
                                j2 = j;
                                com_google_android_gms_internal_measurement_zzki3 = com_google_android_gms_internal_measurement_zzki;
                            }
                        }
                    } else if (obj != null) {
                        zzgc();
                        Long valueOf = Long.valueOf(0);
                        l = zzjv.zzb(com_google_android_gms_internal_measurement_zzki2, "_epc");
                        if (l != null) {
                            valueOf = l;
                        }
                        j = valueOf.longValue();
                        if (j <= 0) {
                            zzgg().zzin().zzg("Complex event with zero extra param count. eventName", str3);
                            com_google_android_gms_internal_measurement_zzkjArr = com_google_android_gms_internal_measurement_zzkjArr2;
                            str2 = str3;
                            l2 = l3;
                            j2 = j;
                            com_google_android_gms_internal_measurement_zzki3 = com_google_android_gms_internal_measurement_zzki2;
                        } else {
                            zzga().zza(str, l3, j, com_google_android_gms_internal_measurement_zzki2);
                            com_google_android_gms_internal_measurement_zzkjArr = com_google_android_gms_internal_measurement_zzkjArr2;
                            str2 = str3;
                            l2 = l3;
                            j2 = j;
                            com_google_android_gms_internal_measurement_zzki3 = com_google_android_gms_internal_measurement_zzki2;
                        }
                    }
                    zze = zzga().zze(str, com_google_android_gms_internal_measurement_zzki2.name);
                    if (zze != null) {
                        zzgg().zzin().zze("Event aggregate wasn't created during raw event logging. appId, event", zzfg.zzbh(str), zzgb().zzbe(str2));
                        com_google_android_gms_internal_measurement_zzeq = new zzeq(str, com_google_android_gms_internal_measurement_zzki2.name, 1, 1, com_google_android_gms_internal_measurement_zzki2.zzasw.longValue(), 0, null, null, null);
                    } else {
                        com_google_android_gms_internal_measurement_zzeq = zze.zzie();
                    }
                    zzga().zza(com_google_android_gms_internal_measurement_zzeq);
                    j3 = com_google_android_gms_internal_measurement_zzeq.zzafp;
                    map = (Map) arrayMap4.get(str2);
                    if (map != null) {
                        map = zzga().zzj(str, str2);
                        if (map == null) {
                            map = new ArrayMap();
                        }
                        arrayMap4.put(str2, map);
                        map2 = map;
                    } else {
                        map2 = map;
                    }
                    for (Integer intValue22 : r7.keySet()) {
                        intValue3 = intValue22.intValue();
                        if (hashSet.contains(Integer.valueOf(intValue3))) {
                            bitSet = (BitSet) arrayMap2.get(Integer.valueOf(intValue3));
                            bitSet2 = (BitSet) arrayMap3.get(Integer.valueOf(intValue3));
                            if (((zzkh) arrayMap.get(Integer.valueOf(intValue3))) != null) {
                                com_google_android_gms_internal_measurement_zzkh = new zzkh();
                                arrayMap.put(Integer.valueOf(intValue3), com_google_android_gms_internal_measurement_zzkh);
                                com_google_android_gms_internal_measurement_zzkh.zzast = Boolean.valueOf(true);
                                bitSet = new BitSet();
                                arrayMap2.put(Integer.valueOf(intValue3), bitSet);
                                bitSet2 = new BitSet();
                                arrayMap3.put(Integer.valueOf(intValue3), bitSet2);
                                bitSet3 = bitSet2;
                                bitSet4 = bitSet;
                            } else {
                                bitSet3 = bitSet2;
                                bitSet4 = bitSet;
                            }
                            for (zzjz com_google_android_gms_internal_measurement_zzjz : (List) r7.get(Integer.valueOf(intValue3))) {
                                if (zzgg().isLoggable(2)) {
                                    zzgg().zzir().zzd("Evaluating filter. audience, filter, event", Integer.valueOf(intValue3), com_google_android_gms_internal_measurement_zzjz.zzark, zzgb().zzbe(com_google_android_gms_internal_measurement_zzjz.zzarl));
                                    zzgg().zzir().zzg("Filter definition", zzgb().zza(com_google_android_gms_internal_measurement_zzjz));
                                }
                                if (com_google_android_gms_internal_measurement_zzjz.zzark != null || com_google_android_gms_internal_measurement_zzjz.zzark.intValue() > 256) {
                                    zzgg().zzin().zze("Invalid event filter ID. appId, id", zzfg.zzbh(str), String.valueOf(com_google_android_gms_internal_measurement_zzjz.zzark));
                                } else if (bitSet4.get(com_google_android_gms_internal_measurement_zzjz.zzark.intValue())) {
                                    zzgg().zzir().zze("Event filter already evaluated true. audience ID, filter ID", Integer.valueOf(intValue3), com_google_android_gms_internal_measurement_zzjz.zzark);
                                } else {
                                    Boolean zza2;
                                    zzfi zzir;
                                    String str4;
                                    Boolean bool;
                                    if (com_google_android_gms_internal_measurement_zzjz.zzaro != null) {
                                        zza2 = zza(j3, com_google_android_gms_internal_measurement_zzjz.zzaro);
                                        if (zza2 == null) {
                                            zza2 = null;
                                        } else if (!zza2.booleanValue()) {
                                            zza2 = Boolean.valueOf(false);
                                        }
                                        zzir = zzgg().zzir();
                                        str4 = "Event filter result";
                                        if (zza2 != null) {
                                            obj = "null";
                                        } else {
                                            bool = zza2;
                                        }
                                        zzir.zzg(str4, obj);
                                        if (zza2 != null) {
                                            hashSet.add(Integer.valueOf(intValue3));
                                        } else {
                                            bitSet3.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                            if (zza2.booleanValue()) {
                                                bitSet4.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                            }
                                        }
                                    }
                                    Set hashSet2 = new HashSet();
                                    for (zzka com_google_android_gms_internal_measurement_zzka : com_google_android_gms_internal_measurement_zzjz.zzarm) {
                                        if (TextUtils.isEmpty(com_google_android_gms_internal_measurement_zzka.zzart)) {
                                            zzgg().zzin().zzg("null or empty param name in filter. event", zzgb().zzbe(str2));
                                            zza2 = null;
                                            break;
                                        }
                                        hashSet2.add(com_google_android_gms_internal_measurement_zzka.zzart);
                                    }
                                    ArrayMap arrayMap5 = new ArrayMap();
                                    for (zzkj com_google_android_gms_internal_measurement_zzkj2 : r19) {
                                        if (hashSet2.contains(com_google_android_gms_internal_measurement_zzkj2.name)) {
                                            if (com_google_android_gms_internal_measurement_zzkj2.zzasz == null) {
                                                if (com_google_android_gms_internal_measurement_zzkj2.zzaqx == null) {
                                                    if (com_google_android_gms_internal_measurement_zzkj2.zzajf == null) {
                                                        zzgg().zzin().zze("Unknown value for param. event, param", zzgb().zzbe(str2), zzgb().zzbf(com_google_android_gms_internal_measurement_zzkj2.name));
                                                        zza2 = null;
                                                        break;
                                                    }
                                                    arrayMap5.put(com_google_android_gms_internal_measurement_zzkj2.name, com_google_android_gms_internal_measurement_zzkj2.zzajf);
                                                } else {
                                                    arrayMap5.put(com_google_android_gms_internal_measurement_zzkj2.name, com_google_android_gms_internal_measurement_zzkj2.zzaqx);
                                                }
                                            } else {
                                                arrayMap5.put(com_google_android_gms_internal_measurement_zzkj2.name, com_google_android_gms_internal_measurement_zzkj2.zzasz);
                                            }
                                        }
                                    }
                                    for (zzka com_google_android_gms_internal_measurement_zzka2 : com_google_android_gms_internal_measurement_zzjz.zzarm) {
                                        boolean equals = Boolean.TRUE.equals(com_google_android_gms_internal_measurement_zzka2.zzars);
                                        String str5 = com_google_android_gms_internal_measurement_zzka2.zzart;
                                        if (TextUtils.isEmpty(str5)) {
                                            zzgg().zzin().zzg("Event has empty param name. event", zzgb().zzbe(str2));
                                            zza2 = null;
                                            break;
                                        }
                                        Object obj3 = arrayMap5.get(str5);
                                        if (obj3 instanceof Long) {
                                            if (com_google_android_gms_internal_measurement_zzka2.zzarr == null) {
                                                zzgg().zzin().zze("No number filter for long param. event, param", zzgb().zzbe(str2), zzgb().zzbf(str5));
                                                zza2 = null;
                                                break;
                                            }
                                            zza2 = zza(((Long) obj3).longValue(), com_google_android_gms_internal_measurement_zzka2.zzarr);
                                            if (zza2 == null) {
                                                zza2 = null;
                                                break;
                                            }
                                            if (((!zza2.booleanValue() ? 1 : 0) ^ equals) != 0) {
                                                zza2 = Boolean.valueOf(false);
                                                break;
                                            }
                                        } else if (obj3 instanceof Double) {
                                            if (com_google_android_gms_internal_measurement_zzka2.zzarr == null) {
                                                zzgg().zzin().zze("No number filter for double param. event, param", zzgb().zzbe(str2), zzgb().zzbf(str5));
                                                zza2 = null;
                                                break;
                                            }
                                            zza2 = zza(((Double) obj3).doubleValue(), com_google_android_gms_internal_measurement_zzka2.zzarr);
                                            if (zza2 == null) {
                                                zza2 = null;
                                                break;
                                            }
                                            if (((!zza2.booleanValue() ? 1 : 0) ^ equals) != 0) {
                                                zza2 = Boolean.valueOf(false);
                                                break;
                                            }
                                        } else if (obj3 instanceof String) {
                                            if (com_google_android_gms_internal_measurement_zzka2.zzarq == null) {
                                                if (com_google_android_gms_internal_measurement_zzka2.zzarr != null) {
                                                    if (!zzjv.zzcd((String) obj3)) {
                                                        zzgg().zzin().zze("Invalid param value for number filter. event, param", zzgb().zzbe(str2), zzgb().zzbf(str5));
                                                        zza2 = null;
                                                        break;
                                                    }
                                                    zza2 = zza((String) obj3, com_google_android_gms_internal_measurement_zzka2.zzarr);
                                                } else {
                                                    zzgg().zzin().zze("No filter for String param. event, param", zzgb().zzbe(str2), zzgb().zzbf(str5));
                                                    zza2 = null;
                                                    break;
                                                }
                                            }
                                            zza2 = zza((String) obj3, com_google_android_gms_internal_measurement_zzka2.zzarq);
                                            if (zza2 == null) {
                                                zza2 = null;
                                                break;
                                            }
                                            if (((!zza2.booleanValue() ? 1 : 0) ^ equals) != 0) {
                                                zza2 = Boolean.valueOf(false);
                                                break;
                                            }
                                        } else if (obj3 == null) {
                                            zzgg().zzir().zze("Missing param for filter. event, param", zzgb().zzbe(str2), zzgb().zzbf(str5));
                                            zza2 = Boolean.valueOf(false);
                                        } else {
                                            zzgg().zzin().zze("Unknown param type. event, param", zzgb().zzbe(str2), zzgb().zzbf(str5));
                                            zza2 = null;
                                        }
                                    }
                                    zza2 = Boolean.valueOf(true);
                                    zzir = zzgg().zzir();
                                    str4 = "Event filter result";
                                    if (zza2 != null) {
                                        bool = zza2;
                                    } else {
                                        obj = "null";
                                    }
                                    zzir.zzg(str4, obj);
                                    if (zza2 != null) {
                                        bitSet3.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                        if (zza2.booleanValue()) {
                                            bitSet4.set(com_google_android_gms_internal_measurement_zzjz.zzark.intValue());
                                        }
                                    } else {
                                        hashSet.add(Integer.valueOf(intValue3));
                                    }
                                }
                            }
                        } else {
                            zzgg().zzir().zzg("Skipping failed audience ID", Integer.valueOf(intValue3));
                        }
                    }
                    l = l2;
                    j = j2;
                    com_google_android_gms_internal_measurement_zzki = com_google_android_gms_internal_measurement_zzki3;
                }
                com_google_android_gms_internal_measurement_zzkjArr = com_google_android_gms_internal_measurement_zzkjArr2;
                str2 = str3;
                l2 = l;
                j2 = j;
                com_google_android_gms_internal_measurement_zzki3 = com_google_android_gms_internal_measurement_zzki;
                zze = zzga().zze(str, com_google_android_gms_internal_measurement_zzki2.name);
                if (zze != null) {
                    com_google_android_gms_internal_measurement_zzeq = zze.zzie();
                } else {
                    zzgg().zzin().zze("Event aggregate wasn't created during raw event logging. appId, event", zzfg.zzbh(str), zzgb().zzbe(str2));
                    com_google_android_gms_internal_measurement_zzeq = new zzeq(str, com_google_android_gms_internal_measurement_zzki2.name, 1, 1, com_google_android_gms_internal_measurement_zzki2.zzasw.longValue(), 0, null, null, null);
                }
                zzga().zza(com_google_android_gms_internal_measurement_zzeq);
                j3 = com_google_android_gms_internal_measurement_zzeq.zzafp;
                map = (Map) arrayMap4.get(str2);
                if (map != null) {
                    map2 = map;
                } else {
                    map = zzga().zzj(str, str2);
                    if (map == null) {
                        map = new ArrayMap();
                    }
                    arrayMap4.put(str2, map);
                    map2 = map;
                }
                while (r11.hasNext()) {
                    intValue3 = intValue22.intValue();
                    if (hashSet.contains(Integer.valueOf(intValue3))) {
                        bitSet = (BitSet) arrayMap2.get(Integer.valueOf(intValue3));
                        bitSet2 = (BitSet) arrayMap3.get(Integer.valueOf(intValue3));
                        if (((zzkh) arrayMap.get(Integer.valueOf(intValue3))) != null) {
                            bitSet3 = bitSet2;
                            bitSet4 = bitSet;
                        } else {
                            com_google_android_gms_internal_measurement_zzkh = new zzkh();
                            arrayMap.put(Integer.valueOf(intValue3), com_google_android_gms_internal_measurement_zzkh);
                            com_google_android_gms_internal_measurement_zzkh.zzast = Boolean.valueOf(true);
                            bitSet = new BitSet();
                            arrayMap2.put(Integer.valueOf(intValue3), bitSet);
                            bitSet2 = new BitSet();
                            arrayMap3.put(Integer.valueOf(intValue3), bitSet2);
                            bitSet3 = bitSet2;
                            bitSet4 = bitSet;
                        }
                        for (zzjz com_google_android_gms_internal_measurement_zzjz2 : (List) r7.get(Integer.valueOf(intValue3))) {
                            if (zzgg().isLoggable(2)) {
                                zzgg().zzir().zzd("Evaluating filter. audience, filter, event", Integer.valueOf(intValue3), com_google_android_gms_internal_measurement_zzjz2.zzark, zzgb().zzbe(com_google_android_gms_internal_measurement_zzjz2.zzarl));
                                zzgg().zzir().zzg("Filter definition", zzgb().zza(com_google_android_gms_internal_measurement_zzjz2));
                            }
                            if (com_google_android_gms_internal_measurement_zzjz2.zzark != null) {
                            }
                            zzgg().zzin().zze("Invalid event filter ID. appId, id", zzfg.zzbh(str), String.valueOf(com_google_android_gms_internal_measurement_zzjz2.zzark));
                        }
                    } else {
                        zzgg().zzir().zzg("Skipping failed audience ID", Integer.valueOf(intValue3));
                    }
                }
                l = l2;
                j = j2;
                com_google_android_gms_internal_measurement_zzki = com_google_android_gms_internal_measurement_zzki3;
            }
        }
        if (com_google_android_gms_internal_measurement_zzknArr != null) {
            Map arrayMap6 = new ArrayMap();
            for (zzkn com_google_android_gms_internal_measurement_zzkn : com_google_android_gms_internal_measurement_zzknArr) {
                map = (Map) arrayMap6.get(com_google_android_gms_internal_measurement_zzkn.name);
                if (map == null) {
                    map = zzga().zzk(str, com_google_android_gms_internal_measurement_zzkn.name);
                    if (map == null) {
                        map = new ArrayMap();
                    }
                    arrayMap6.put(com_google_android_gms_internal_measurement_zzkn.name, map);
                    map2 = map;
                } else {
                    map2 = map;
                }
                for (Integer intValue222 : r7.keySet()) {
                    length = intValue222.intValue();
                    if (hashSet.contains(Integer.valueOf(length))) {
                        zzgg().zzir().zzg("Skipping failed audience ID", Integer.valueOf(length));
                    } else {
                        bitSet = (BitSet) arrayMap2.get(Integer.valueOf(length));
                        bitSet2 = (BitSet) arrayMap3.get(Integer.valueOf(length));
                        if (((zzkh) arrayMap.get(Integer.valueOf(length))) == null) {
                            com_google_android_gms_internal_measurement_zzkh = new zzkh();
                            arrayMap.put(Integer.valueOf(length), com_google_android_gms_internal_measurement_zzkh);
                            com_google_android_gms_internal_measurement_zzkh.zzast = Boolean.valueOf(true);
                            bitSet = new BitSet();
                            arrayMap2.put(Integer.valueOf(length), bitSet);
                            bitSet2 = new BitSet();
                            arrayMap3.put(Integer.valueOf(length), bitSet2);
                        }
                        for (zzkc com_google_android_gms_internal_measurement_zzkc : (List) r7.get(Integer.valueOf(length))) {
                            if (zzgg().isLoggable(2)) {
                                zzgg().zzir().zzd("Evaluating filter. audience, filter, property", Integer.valueOf(length), com_google_android_gms_internal_measurement_zzkc.zzark, zzgb().zzbg(com_google_android_gms_internal_measurement_zzkc.zzasa));
                                zzgg().zzir().zzg("Filter definition", zzgb().zza(com_google_android_gms_internal_measurement_zzkc));
                            }
                            if (com_google_android_gms_internal_measurement_zzkc.zzark == null || com_google_android_gms_internal_measurement_zzkc.zzark.intValue() > 256) {
                                zzgg().zzin().zze("Invalid property filter ID. appId, id", zzfg.zzbh(str), String.valueOf(com_google_android_gms_internal_measurement_zzkc.zzark));
                                hashSet.add(Integer.valueOf(length));
                                break;
                            } else if (bitSet.get(com_google_android_gms_internal_measurement_zzkc.zzark.intValue())) {
                                zzgg().zzir().zze("Property filter already evaluated true. audience ID, filter ID", Integer.valueOf(length), com_google_android_gms_internal_measurement_zzkc.zzark);
                            } else {
                                Boolean bool2;
                                Object obj4;
                                zzka com_google_android_gms_internal_measurement_zzka3 = com_google_android_gms_internal_measurement_zzkc.zzasb;
                                if (com_google_android_gms_internal_measurement_zzka3 == null) {
                                    zzgg().zzin().zzg("Missing property filter. property", zzgb().zzbg(com_google_android_gms_internal_measurement_zzkn.name));
                                    bool2 = null;
                                } else {
                                    boolean equals2 = Boolean.TRUE.equals(com_google_android_gms_internal_measurement_zzka3.zzars);
                                    if (com_google_android_gms_internal_measurement_zzkn.zzasz != null) {
                                        if (com_google_android_gms_internal_measurement_zzka3.zzarr == null) {
                                            zzgg().zzin().zzg("No number filter for long property. property", zzgb().zzbg(com_google_android_gms_internal_measurement_zzkn.name));
                                            bool2 = null;
                                        } else {
                                            bool2 = zza(zza(com_google_android_gms_internal_measurement_zzkn.zzasz.longValue(), com_google_android_gms_internal_measurement_zzka3.zzarr), equals2);
                                        }
                                    } else if (com_google_android_gms_internal_measurement_zzkn.zzaqx != null) {
                                        if (com_google_android_gms_internal_measurement_zzka3.zzarr == null) {
                                            zzgg().zzin().zzg("No number filter for double property. property", zzgb().zzbg(com_google_android_gms_internal_measurement_zzkn.name));
                                            bool2 = null;
                                        } else {
                                            bool2 = zza(zza(com_google_android_gms_internal_measurement_zzkn.zzaqx.doubleValue(), com_google_android_gms_internal_measurement_zzka3.zzarr), equals2);
                                        }
                                    } else if (com_google_android_gms_internal_measurement_zzkn.zzajf == null) {
                                        zzgg().zzin().zzg("User property has no value, property", zzgb().zzbg(com_google_android_gms_internal_measurement_zzkn.name));
                                        bool2 = null;
                                    } else if (com_google_android_gms_internal_measurement_zzka3.zzarq == null) {
                                        if (com_google_android_gms_internal_measurement_zzka3.zzarr == null) {
                                            zzgg().zzin().zzg("No string or number filter defined. property", zzgb().zzbg(com_google_android_gms_internal_measurement_zzkn.name));
                                        } else if (zzjv.zzcd(com_google_android_gms_internal_measurement_zzkn.zzajf)) {
                                            bool2 = zza(zza(com_google_android_gms_internal_measurement_zzkn.zzajf, com_google_android_gms_internal_measurement_zzka3.zzarr), equals2);
                                        } else {
                                            zzgg().zzin().zze("Invalid user property value for Numeric number filter. property, value", zzgb().zzbg(com_google_android_gms_internal_measurement_zzkn.name), com_google_android_gms_internal_measurement_zzkn.zzajf);
                                        }
                                        bool2 = null;
                                    } else {
                                        bool2 = zza(zza(com_google_android_gms_internal_measurement_zzkn.zzajf, com_google_android_gms_internal_measurement_zzka3.zzarq), equals2);
                                    }
                                }
                                zzfi zzir2 = zzgg().zzir();
                                String str6 = "Property filter result";
                                if (bool2 == null) {
                                    obj4 = "null";
                                } else {
                                    Boolean bool3 = bool2;
                                }
                                zzir2.zzg(str6, obj4);
                                if (bool2 == null) {
                                    hashSet.add(Integer.valueOf(length));
                                } else {
                                    bitSet2.set(com_google_android_gms_internal_measurement_zzkc.zzark.intValue());
                                    if (bool2.booleanValue()) {
                                        bitSet.set(com_google_android_gms_internal_measurement_zzkc.zzark.intValue());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        zzkh[] com_google_android_gms_internal_measurement_zzkhArr = new zzkh[arrayMap2.size()];
        i2 = 0;
        for (Integer intValue2222 : arrayMap2.keySet()) {
            intValue = intValue2222.intValue();
            if (!hashSet.contains(Integer.valueOf(intValue))) {
                com_google_android_gms_internal_measurement_zzkh = (zzkh) arrayMap.get(Integer.valueOf(intValue));
                com_google_android_gms_internal_measurement_zzkh2 = com_google_android_gms_internal_measurement_zzkh == null ? new zzkh() : com_google_android_gms_internal_measurement_zzkh;
                int i4 = i2 + 1;
                com_google_android_gms_internal_measurement_zzkhArr[i2] = com_google_android_gms_internal_measurement_zzkh2;
                com_google_android_gms_internal_measurement_zzkh2.zzarg = Integer.valueOf(intValue);
                com_google_android_gms_internal_measurement_zzkh2.zzasr = new zzkm();
                com_google_android_gms_internal_measurement_zzkh2.zzasr.zzaug = zzjv.zza((BitSet) arrayMap2.get(Integer.valueOf(intValue)));
                com_google_android_gms_internal_measurement_zzkh2.zzasr.zzauf = zzjv.zza((BitSet) arrayMap3.get(Integer.valueOf(intValue)));
                zzga = zzga();
                zzabj com_google_android_gms_internal_measurement_zzabj = com_google_android_gms_internal_measurement_zzkh2.zzasr;
                zzga.zzch();
                zzga.zzab();
                Preconditions.checkNotEmpty(str);
                Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzabj);
                try {
                    byte[] bArr = new byte[com_google_android_gms_internal_measurement_zzabj.zzwg()];
                    zzabb zzb = zzabb.zzb(bArr, 0, bArr.length);
                    com_google_android_gms_internal_measurement_zzabj.zza(zzb);
                    zzb.zzvy();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("app_id", str);
                    contentValues.put("audience_id", Integer.valueOf(intValue));
                    contentValues.put("current_results", bArr);
                    try {
                        if (zzga.getWritableDatabase().insertWithOnConflict("audience_filter_values", null, contentValues, 5) == -1) {
                            zzga.zzgg().zzil().zzg("Failed to insert filter results (got -1). appId", zzfg.zzbh(str));
                        }
                        i2 = i4;
                    } catch (SQLiteException e2) {
                        zzga.zzgg().zzil().zze("Error storing filter results. appId", zzfg.zzbh(str), e2);
                        i2 = i4;
                    }
                } catch (IOException e3) {
                    zzga.zzgg().zzil().zze("Configuration loss. Failed to serialize filter results. appId", zzfg.zzbh(str), e3);
                    i2 = i4;
                }
            }
        }
        return (zzkh[]) Arrays.copyOf(com_google_android_gms_internal_measurement_zzkhArr, i2);
    }

    protected final boolean zzhh() {
        return false;
    }
}
