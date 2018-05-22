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

final class zzeb extends zzjq {
    zzeb(zzjr com_google_android_gms_internal_measurement_zzjr) {
        super(com_google_android_gms_internal_measurement_zzjr);
    }

    private final Boolean zza(double d, zzkg com_google_android_gms_internal_measurement_zzkg) {
        try {
            return zza(new BigDecimal(d), com_google_android_gms_internal_measurement_zzkg, Math.ulp(d));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private final Boolean zza(long j, zzkg com_google_android_gms_internal_measurement_zzkg) {
        try {
            return zza(new BigDecimal(j), com_google_android_gms_internal_measurement_zzkg, 0.0d);
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
                    zzge().zzip().zzg("Invalid regular expression in REGEXP audience filter. expression", str3);
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

    private final Boolean zza(String str, zzkg com_google_android_gms_internal_measurement_zzkg) {
        Boolean bool = null;
        if (zzka.zzck(str)) {
            try {
                bool = zza(new BigDecimal(str), com_google_android_gms_internal_measurement_zzkg, 0.0d);
            } catch (NumberFormatException e) {
            }
        }
        return bool;
    }

    private final Boolean zza(String str, zzki com_google_android_gms_internal_measurement_zzki) {
        int i = 0;
        String str2 = null;
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzki);
        if (str == null || com_google_android_gms_internal_measurement_zzki.zzash == null || com_google_android_gms_internal_measurement_zzki.zzash.intValue() == 0) {
            return null;
        }
        List list;
        if (com_google_android_gms_internal_measurement_zzki.zzash.intValue() == 6) {
            if (com_google_android_gms_internal_measurement_zzki.zzask == null || com_google_android_gms_internal_measurement_zzki.zzask.length == 0) {
                return null;
            }
        } else if (com_google_android_gms_internal_measurement_zzki.zzasi == null) {
            return null;
        }
        int intValue = com_google_android_gms_internal_measurement_zzki.zzash.intValue();
        boolean z = com_google_android_gms_internal_measurement_zzki.zzasj != null && com_google_android_gms_internal_measurement_zzki.zzasj.booleanValue();
        String toUpperCase = (z || intValue == 1 || intValue == 6) ? com_google_android_gms_internal_measurement_zzki.zzasi : com_google_android_gms_internal_measurement_zzki.zzasi.toUpperCase(Locale.ENGLISH);
        if (com_google_android_gms_internal_measurement_zzki.zzask == null) {
            list = null;
        } else {
            String[] strArr = com_google_android_gms_internal_measurement_zzki.zzask;
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
    private static java.lang.Boolean zza(java.math.BigDecimal r10, com.google.android.gms.internal.measurement.zzkg r11, double r12) {
        /*
        r8 = 4;
        r7 = -1;
        r1 = 0;
        r0 = 1;
        r2 = 0;
        com.google.android.gms.common.internal.Preconditions.checkNotNull(r11);
        r3 = r11.zzarz;
        if (r3 == 0) goto L_0x0014;
    L_0x000c:
        r3 = r11.zzarz;
        r3 = r3.intValue();
        if (r3 != 0) goto L_0x0016;
    L_0x0014:
        r0 = r2;
    L_0x0015:
        return r0;
    L_0x0016:
        r3 = r11.zzarz;
        r3 = r3.intValue();
        if (r3 != r8) goto L_0x0028;
    L_0x001e:
        r3 = r11.zzasc;
        if (r3 == 0) goto L_0x0026;
    L_0x0022:
        r3 = r11.zzasd;
        if (r3 != 0) goto L_0x002e;
    L_0x0026:
        r0 = r2;
        goto L_0x0015;
    L_0x0028:
        r3 = r11.zzasb;
        if (r3 != 0) goto L_0x002e;
    L_0x002c:
        r0 = r2;
        goto L_0x0015;
    L_0x002e:
        r3 = r11.zzarz;
        r6 = r3.intValue();
        r3 = r11.zzarz;
        r3 = r3.intValue();
        if (r3 != r8) goto L_0x0066;
    L_0x003c:
        r3 = r11.zzasc;
        r3 = com.google.android.gms.internal.measurement.zzka.zzck(r3);
        if (r3 == 0) goto L_0x004c;
    L_0x0044:
        r3 = r11.zzasd;
        r3 = com.google.android.gms.internal.measurement.zzka.zzck(r3);
        if (r3 != 0) goto L_0x004e;
    L_0x004c:
        r0 = r2;
        goto L_0x0015;
    L_0x004e:
        r4 = new java.math.BigDecimal;	 Catch:{ NumberFormatException -> 0x0063 }
        r3 = r11.zzasc;	 Catch:{ NumberFormatException -> 0x0063 }
        r4.<init>(r3);	 Catch:{ NumberFormatException -> 0x0063 }
        r3 = new java.math.BigDecimal;	 Catch:{ NumberFormatException -> 0x0063 }
        r5 = r11.zzasd;	 Catch:{ NumberFormatException -> 0x0063 }
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
        r3 = r11.zzasb;
        r3 = com.google.android.gms.internal.measurement.zzka.zzck(r3);
        if (r3 != 0) goto L_0x0070;
    L_0x006e:
        r0 = r2;
        goto L_0x0015;
    L_0x0070:
        r5 = new java.math.BigDecimal;	 Catch:{ NumberFormatException -> 0x007a }
        r3 = r11.zzasb;	 Catch:{ NumberFormatException -> 0x007a }
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
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzeb.zza(java.math.BigDecimal, com.google.android.gms.internal.measurement.zzkg, double):java.lang.Boolean");
    }

    final zzkm[] zza(String str, zzkn[] com_google_android_gms_internal_measurement_zzknArr, zzks[] com_google_android_gms_internal_measurement_zzksArr) {
        int intValue;
        BitSet bitSet;
        BitSet bitSet2;
        int i;
        zzhg zzix;
        int i2;
        int length;
        Map map;
        Map map2;
        zzkm com_google_android_gms_internal_measurement_zzkm;
        Preconditions.checkNotEmpty(str);
        HashSet hashSet = new HashSet();
        ArrayMap arrayMap = new ArrayMap();
        Map arrayMap2 = new ArrayMap();
        ArrayMap arrayMap3 = new ArrayMap();
        Map zzbf = zzix().zzbf(str);
        if (zzbf != null) {
            for (Integer intValue2 : zzbf.keySet()) {
                intValue = intValue2.intValue();
                zzkr com_google_android_gms_internal_measurement_zzkr = (zzkr) zzbf.get(Integer.valueOf(intValue));
                bitSet = (BitSet) arrayMap2.get(Integer.valueOf(intValue));
                bitSet2 = (BitSet) arrayMap3.get(Integer.valueOf(intValue));
                if (bitSet == null) {
                    bitSet = new BitSet();
                    arrayMap2.put(Integer.valueOf(intValue), bitSet);
                    bitSet2 = new BitSet();
                    arrayMap3.put(Integer.valueOf(intValue), bitSet2);
                }
                for (i = 0; i < (com_google_android_gms_internal_measurement_zzkr.zzauk.length << 6); i++) {
                    if (zzka.zza(com_google_android_gms_internal_measurement_zzkr.zzauk, i)) {
                        zzge().zzit().zze("Filter already evaluated. audience ID, filter ID", Integer.valueOf(intValue), Integer.valueOf(i));
                        bitSet2.set(i);
                        if (zzka.zza(com_google_android_gms_internal_measurement_zzkr.zzaul, i)) {
                            bitSet.set(i);
                        }
                    }
                }
                zzkm com_google_android_gms_internal_measurement_zzkm2 = new zzkm();
                arrayMap.put(Integer.valueOf(intValue), com_google_android_gms_internal_measurement_zzkm2);
                com_google_android_gms_internal_measurement_zzkm2.zzasy = Boolean.valueOf(false);
                com_google_android_gms_internal_measurement_zzkm2.zzasx = com_google_android_gms_internal_measurement_zzkr;
                com_google_android_gms_internal_measurement_zzkm2.zzasw = new zzkr();
                com_google_android_gms_internal_measurement_zzkm2.zzasw.zzaul = zzka.zza(bitSet);
                com_google_android_gms_internal_measurement_zzkm2.zzasw.zzauk = zzka.zza(bitSet2);
            }
        }
        if (com_google_android_gms_internal_measurement_zzknArr != null) {
            zzkn com_google_android_gms_internal_measurement_zzkn = null;
            long j = 0;
            Long l = null;
            ArrayMap arrayMap4 = new ArrayMap();
            for (zzkn com_google_android_gms_internal_measurement_zzkn2 : com_google_android_gms_internal_measurement_zzknArr) {
                zzko[] com_google_android_gms_internal_measurement_zzkoArr;
                String str2;
                Long l2;
                long j2;
                zzkn com_google_android_gms_internal_measurement_zzkn3;
                zzeq zzf;
                zzeq com_google_android_gms_internal_measurement_zzeq;
                long j3;
                int intValue3;
                BitSet bitSet3;
                BitSet bitSet4;
                String str3 = com_google_android_gms_internal_measurement_zzkn2.name;
                zzko[] com_google_android_gms_internal_measurement_zzkoArr2 = com_google_android_gms_internal_measurement_zzkn2.zzata;
                if (zzgg().zzd(str, zzew.zzahv)) {
                    int length2;
                    zzko com_google_android_gms_internal_measurement_zzko;
                    zzgb();
                    Long l3 = (Long) zzka.zzb(com_google_android_gms_internal_measurement_zzkn2, "_eid");
                    Object obj = l3 != null ? 1 : null;
                    Object obj2 = (obj == null || !str3.equals("_ep")) ? null : 1;
                    if (obj2 != null) {
                        zzgb();
                        str3 = (String) zzka.zzb(com_google_android_gms_internal_measurement_zzkn2, "_en");
                        if (TextUtils.isEmpty(str3)) {
                            zzge().zzim().zzg("Extra parameter without an event name. eventId", l3);
                        } else {
                            Long l4;
                            int i3;
                            if (com_google_android_gms_internal_measurement_zzkn == null || l == null || l3.longValue() != l.longValue()) {
                                Pair zza = zzix().zza(str, l3);
                                if (zza == null || zza.first == null) {
                                    zzge().zzim().zze("Extra parameter without existing main event. eventName, eventId", str3, l3);
                                } else {
                                    zzkn com_google_android_gms_internal_measurement_zzkn4 = (zzkn) zza.first;
                                    j = ((Long) zza.second).longValue();
                                    zzgb();
                                    l4 = (Long) zzka.zzb(com_google_android_gms_internal_measurement_zzkn4, "_eid");
                                    com_google_android_gms_internal_measurement_zzkn = com_google_android_gms_internal_measurement_zzkn4;
                                }
                            } else {
                                l4 = l;
                            }
                            j--;
                            if (j <= 0) {
                                zzix = zzix();
                                zzix.zzab();
                                zzix.zzge().zzit().zzg("Clearing complex main event info. appId", str);
                                try {
                                    zzix.getWritableDatabase().execSQL("delete from main_event_params where app_id=?", new String[]{str});
                                } catch (SQLiteException e) {
                                    zzix.zzge().zzim().zzg("Error clearing complex main event", e);
                                }
                            } else {
                                zzix().zza(str, l3, j, com_google_android_gms_internal_measurement_zzkn);
                            }
                            zzko[] com_google_android_gms_internal_measurement_zzkoArr3 = new zzko[(com_google_android_gms_internal_measurement_zzkn.zzata.length + com_google_android_gms_internal_measurement_zzkoArr2.length)];
                            i2 = 0;
                            zzko[] com_google_android_gms_internal_measurement_zzkoArr4 = com_google_android_gms_internal_measurement_zzkn.zzata;
                            length2 = com_google_android_gms_internal_measurement_zzkoArr4.length;
                            i = 0;
                            while (i < length2) {
                                com_google_android_gms_internal_measurement_zzko = com_google_android_gms_internal_measurement_zzkoArr4[i];
                                zzgb();
                                if (zzka.zza(com_google_android_gms_internal_measurement_zzkn2, com_google_android_gms_internal_measurement_zzko.name) == null) {
                                    i3 = i2 + 1;
                                    com_google_android_gms_internal_measurement_zzkoArr3[i2] = com_google_android_gms_internal_measurement_zzko;
                                } else {
                                    i3 = i2;
                                }
                                i++;
                                i2 = i3;
                            }
                            if (i2 > 0) {
                                length = com_google_android_gms_internal_measurement_zzkoArr2.length;
                                i3 = 0;
                                while (i3 < length) {
                                    i = i2 + 1;
                                    com_google_android_gms_internal_measurement_zzkoArr3[i2] = com_google_android_gms_internal_measurement_zzkoArr2[i3];
                                    i3++;
                                    i2 = i;
                                }
                                com_google_android_gms_internal_measurement_zzkoArr = i2 == com_google_android_gms_internal_measurement_zzkoArr3.length ? com_google_android_gms_internal_measurement_zzkoArr3 : (zzko[]) Arrays.copyOf(com_google_android_gms_internal_measurement_zzkoArr3, i2);
                                str2 = str3;
                                l2 = l4;
                                j2 = j;
                                com_google_android_gms_internal_measurement_zzkn3 = com_google_android_gms_internal_measurement_zzkn;
                            } else {
                                zzge().zzip().zzg("No unique parameters in main event. eventName", str3);
                                com_google_android_gms_internal_measurement_zzkoArr = com_google_android_gms_internal_measurement_zzkoArr2;
                                str2 = str3;
                                l2 = l4;
                                j2 = j;
                                com_google_android_gms_internal_measurement_zzkn3 = com_google_android_gms_internal_measurement_zzkn;
                            }
                        }
                    } else if (obj != null) {
                        zzgb();
                        Long valueOf = Long.valueOf(0);
                        l = zzka.zzb(com_google_android_gms_internal_measurement_zzkn2, "_epc");
                        if (l != null) {
                            valueOf = l;
                        }
                        j = valueOf.longValue();
                        if (j <= 0) {
                            zzge().zzip().zzg("Complex event with zero extra param count. eventName", str3);
                            com_google_android_gms_internal_measurement_zzkoArr = com_google_android_gms_internal_measurement_zzkoArr2;
                            str2 = str3;
                            l2 = l3;
                            j2 = j;
                            com_google_android_gms_internal_measurement_zzkn3 = com_google_android_gms_internal_measurement_zzkn2;
                        } else {
                            zzix().zza(str, l3, j, com_google_android_gms_internal_measurement_zzkn2);
                            com_google_android_gms_internal_measurement_zzkoArr = com_google_android_gms_internal_measurement_zzkoArr2;
                            str2 = str3;
                            l2 = l3;
                            j2 = j;
                            com_google_android_gms_internal_measurement_zzkn3 = com_google_android_gms_internal_measurement_zzkn2;
                        }
                    }
                    zzf = zzix().zzf(str, com_google_android_gms_internal_measurement_zzkn2.name);
                    if (zzf != null) {
                        zzge().zzip().zze("Event aggregate wasn't created during raw event logging. appId, event", zzfg.zzbm(str), zzga().zzbj(str2));
                        com_google_android_gms_internal_measurement_zzeq = new zzeq(str, com_google_android_gms_internal_measurement_zzkn2.name, 1, 1, com_google_android_gms_internal_measurement_zzkn2.zzatb.longValue(), 0, null, null, null);
                    } else {
                        com_google_android_gms_internal_measurement_zzeq = zzf.zzie();
                    }
                    zzix().zza(com_google_android_gms_internal_measurement_zzeq);
                    j3 = com_google_android_gms_internal_measurement_zzeq.zzafr;
                    map = (Map) arrayMap4.get(str2);
                    if (map != null) {
                        map = zzix().zzk(str, str2);
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
                            if (((zzkm) arrayMap.get(Integer.valueOf(intValue3))) != null) {
                                com_google_android_gms_internal_measurement_zzkm = new zzkm();
                                arrayMap.put(Integer.valueOf(intValue3), com_google_android_gms_internal_measurement_zzkm);
                                com_google_android_gms_internal_measurement_zzkm.zzasy = Boolean.valueOf(true);
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
                            for (zzke com_google_android_gms_internal_measurement_zzke : (List) r7.get(Integer.valueOf(intValue3))) {
                                if (zzge().isLoggable(2)) {
                                    zzge().zzit().zzd("Evaluating filter. audience, filter, event", Integer.valueOf(intValue3), com_google_android_gms_internal_measurement_zzke.zzarp, zzga().zzbj(com_google_android_gms_internal_measurement_zzke.zzarq));
                                    zzge().zzit().zzg("Filter definition", zzga().zza(com_google_android_gms_internal_measurement_zzke));
                                }
                                if (com_google_android_gms_internal_measurement_zzke.zzarp != null || com_google_android_gms_internal_measurement_zzke.zzarp.intValue() > 256) {
                                    zzge().zzip().zze("Invalid event filter ID. appId, id", zzfg.zzbm(str), String.valueOf(com_google_android_gms_internal_measurement_zzke.zzarp));
                                } else if (bitSet4.get(com_google_android_gms_internal_measurement_zzke.zzarp.intValue())) {
                                    zzge().zzit().zze("Event filter already evaluated true. audience ID, filter ID", Integer.valueOf(intValue3), com_google_android_gms_internal_measurement_zzke.zzarp);
                                } else {
                                    Boolean zza2;
                                    zzfi zzit;
                                    String str4;
                                    Boolean bool;
                                    if (com_google_android_gms_internal_measurement_zzke.zzart != null) {
                                        zza2 = zza(j3, com_google_android_gms_internal_measurement_zzke.zzart);
                                        if (zza2 == null) {
                                            zza2 = null;
                                        } else if (!zza2.booleanValue()) {
                                            zza2 = Boolean.valueOf(false);
                                        }
                                        zzit = zzge().zzit();
                                        str4 = "Event filter result";
                                        if (zza2 != null) {
                                            obj = "null";
                                        } else {
                                            bool = zza2;
                                        }
                                        zzit.zzg(str4, obj);
                                        if (zza2 != null) {
                                            hashSet.add(Integer.valueOf(intValue3));
                                        } else {
                                            bitSet3.set(com_google_android_gms_internal_measurement_zzke.zzarp.intValue());
                                            if (zza2.booleanValue()) {
                                                bitSet4.set(com_google_android_gms_internal_measurement_zzke.zzarp.intValue());
                                            }
                                        }
                                    }
                                    Set hashSet2 = new HashSet();
                                    for (zzkf com_google_android_gms_internal_measurement_zzkf : com_google_android_gms_internal_measurement_zzke.zzarr) {
                                        if (TextUtils.isEmpty(com_google_android_gms_internal_measurement_zzkf.zzary)) {
                                            zzge().zzip().zzg("null or empty param name in filter. event", zzga().zzbj(str2));
                                            zza2 = null;
                                            break;
                                        }
                                        hashSet2.add(com_google_android_gms_internal_measurement_zzkf.zzary);
                                    }
                                    ArrayMap arrayMap5 = new ArrayMap();
                                    for (zzko com_google_android_gms_internal_measurement_zzko2 : r19) {
                                        if (hashSet2.contains(com_google_android_gms_internal_measurement_zzko2.name)) {
                                            if (com_google_android_gms_internal_measurement_zzko2.zzate == null) {
                                                if (com_google_android_gms_internal_measurement_zzko2.zzarc == null) {
                                                    if (com_google_android_gms_internal_measurement_zzko2.zzajf == null) {
                                                        zzge().zzip().zze("Unknown value for param. event, param", zzga().zzbj(str2), zzga().zzbk(com_google_android_gms_internal_measurement_zzko2.name));
                                                        zza2 = null;
                                                        break;
                                                    }
                                                    arrayMap5.put(com_google_android_gms_internal_measurement_zzko2.name, com_google_android_gms_internal_measurement_zzko2.zzajf);
                                                } else {
                                                    arrayMap5.put(com_google_android_gms_internal_measurement_zzko2.name, com_google_android_gms_internal_measurement_zzko2.zzarc);
                                                }
                                            } else {
                                                arrayMap5.put(com_google_android_gms_internal_measurement_zzko2.name, com_google_android_gms_internal_measurement_zzko2.zzate);
                                            }
                                        }
                                    }
                                    for (zzkf com_google_android_gms_internal_measurement_zzkf2 : com_google_android_gms_internal_measurement_zzke.zzarr) {
                                        boolean equals = Boolean.TRUE.equals(com_google_android_gms_internal_measurement_zzkf2.zzarx);
                                        String str5 = com_google_android_gms_internal_measurement_zzkf2.zzary;
                                        if (TextUtils.isEmpty(str5)) {
                                            zzge().zzip().zzg("Event has empty param name. event", zzga().zzbj(str2));
                                            zza2 = null;
                                            break;
                                        }
                                        Object obj3 = arrayMap5.get(str5);
                                        if (obj3 instanceof Long) {
                                            if (com_google_android_gms_internal_measurement_zzkf2.zzarw == null) {
                                                zzge().zzip().zze("No number filter for long param. event, param", zzga().zzbj(str2), zzga().zzbk(str5));
                                                zza2 = null;
                                                break;
                                            }
                                            zza2 = zza(((Long) obj3).longValue(), com_google_android_gms_internal_measurement_zzkf2.zzarw);
                                            if (zza2 == null) {
                                                zza2 = null;
                                                break;
                                            }
                                            if (((!zza2.booleanValue() ? 1 : 0) ^ equals) != 0) {
                                                zza2 = Boolean.valueOf(false);
                                                break;
                                            }
                                        } else if (obj3 instanceof Double) {
                                            if (com_google_android_gms_internal_measurement_zzkf2.zzarw == null) {
                                                zzge().zzip().zze("No number filter for double param. event, param", zzga().zzbj(str2), zzga().zzbk(str5));
                                                zza2 = null;
                                                break;
                                            }
                                            zza2 = zza(((Double) obj3).doubleValue(), com_google_android_gms_internal_measurement_zzkf2.zzarw);
                                            if (zza2 == null) {
                                                zza2 = null;
                                                break;
                                            }
                                            if (((!zza2.booleanValue() ? 1 : 0) ^ equals) != 0) {
                                                zza2 = Boolean.valueOf(false);
                                                break;
                                            }
                                        } else if (obj3 instanceof String) {
                                            if (com_google_android_gms_internal_measurement_zzkf2.zzarv == null) {
                                                if (com_google_android_gms_internal_measurement_zzkf2.zzarw != null) {
                                                    if (!zzka.zzck((String) obj3)) {
                                                        zzge().zzip().zze("Invalid param value for number filter. event, param", zzga().zzbj(str2), zzga().zzbk(str5));
                                                        zza2 = null;
                                                        break;
                                                    }
                                                    zza2 = zza((String) obj3, com_google_android_gms_internal_measurement_zzkf2.zzarw);
                                                } else {
                                                    zzge().zzip().zze("No filter for String param. event, param", zzga().zzbj(str2), zzga().zzbk(str5));
                                                    zza2 = null;
                                                    break;
                                                }
                                            }
                                            zza2 = zza((String) obj3, com_google_android_gms_internal_measurement_zzkf2.zzarv);
                                            if (zza2 == null) {
                                                zza2 = null;
                                                break;
                                            }
                                            if (((!zza2.booleanValue() ? 1 : 0) ^ equals) != 0) {
                                                zza2 = Boolean.valueOf(false);
                                                break;
                                            }
                                        } else if (obj3 == null) {
                                            zzge().zzit().zze("Missing param for filter. event, param", zzga().zzbj(str2), zzga().zzbk(str5));
                                            zza2 = Boolean.valueOf(false);
                                        } else {
                                            zzge().zzip().zze("Unknown param type. event, param", zzga().zzbj(str2), zzga().zzbk(str5));
                                            zza2 = null;
                                        }
                                    }
                                    zza2 = Boolean.valueOf(true);
                                    zzit = zzge().zzit();
                                    str4 = "Event filter result";
                                    if (zza2 != null) {
                                        bool = zza2;
                                    } else {
                                        obj = "null";
                                    }
                                    zzit.zzg(str4, obj);
                                    if (zza2 != null) {
                                        bitSet3.set(com_google_android_gms_internal_measurement_zzke.zzarp.intValue());
                                        if (zza2.booleanValue()) {
                                            bitSet4.set(com_google_android_gms_internal_measurement_zzke.zzarp.intValue());
                                        }
                                    } else {
                                        hashSet.add(Integer.valueOf(intValue3));
                                    }
                                }
                            }
                        } else {
                            zzge().zzit().zzg("Skipping failed audience ID", Integer.valueOf(intValue3));
                        }
                    }
                    l = l2;
                    j = j2;
                    com_google_android_gms_internal_measurement_zzkn = com_google_android_gms_internal_measurement_zzkn3;
                }
                com_google_android_gms_internal_measurement_zzkoArr = com_google_android_gms_internal_measurement_zzkoArr2;
                str2 = str3;
                l2 = l;
                j2 = j;
                com_google_android_gms_internal_measurement_zzkn3 = com_google_android_gms_internal_measurement_zzkn;
                zzf = zzix().zzf(str, com_google_android_gms_internal_measurement_zzkn2.name);
                if (zzf != null) {
                    com_google_android_gms_internal_measurement_zzeq = zzf.zzie();
                } else {
                    zzge().zzip().zze("Event aggregate wasn't created during raw event logging. appId, event", zzfg.zzbm(str), zzga().zzbj(str2));
                    com_google_android_gms_internal_measurement_zzeq = new zzeq(str, com_google_android_gms_internal_measurement_zzkn2.name, 1, 1, com_google_android_gms_internal_measurement_zzkn2.zzatb.longValue(), 0, null, null, null);
                }
                zzix().zza(com_google_android_gms_internal_measurement_zzeq);
                j3 = com_google_android_gms_internal_measurement_zzeq.zzafr;
                map = (Map) arrayMap4.get(str2);
                if (map != null) {
                    map2 = map;
                } else {
                    map = zzix().zzk(str, str2);
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
                        if (((zzkm) arrayMap.get(Integer.valueOf(intValue3))) != null) {
                            bitSet3 = bitSet2;
                            bitSet4 = bitSet;
                        } else {
                            com_google_android_gms_internal_measurement_zzkm = new zzkm();
                            arrayMap.put(Integer.valueOf(intValue3), com_google_android_gms_internal_measurement_zzkm);
                            com_google_android_gms_internal_measurement_zzkm.zzasy = Boolean.valueOf(true);
                            bitSet = new BitSet();
                            arrayMap2.put(Integer.valueOf(intValue3), bitSet);
                            bitSet2 = new BitSet();
                            arrayMap3.put(Integer.valueOf(intValue3), bitSet2);
                            bitSet3 = bitSet2;
                            bitSet4 = bitSet;
                        }
                        for (zzke com_google_android_gms_internal_measurement_zzke2 : (List) r7.get(Integer.valueOf(intValue3))) {
                            if (zzge().isLoggable(2)) {
                                zzge().zzit().zzd("Evaluating filter. audience, filter, event", Integer.valueOf(intValue3), com_google_android_gms_internal_measurement_zzke2.zzarp, zzga().zzbj(com_google_android_gms_internal_measurement_zzke2.zzarq));
                                zzge().zzit().zzg("Filter definition", zzga().zza(com_google_android_gms_internal_measurement_zzke2));
                            }
                            if (com_google_android_gms_internal_measurement_zzke2.zzarp != null) {
                            }
                            zzge().zzip().zze("Invalid event filter ID. appId, id", zzfg.zzbm(str), String.valueOf(com_google_android_gms_internal_measurement_zzke2.zzarp));
                        }
                    } else {
                        zzge().zzit().zzg("Skipping failed audience ID", Integer.valueOf(intValue3));
                    }
                }
                l = l2;
                j = j2;
                com_google_android_gms_internal_measurement_zzkn = com_google_android_gms_internal_measurement_zzkn3;
            }
        }
        if (com_google_android_gms_internal_measurement_zzksArr != null) {
            Map arrayMap6 = new ArrayMap();
            for (zzks com_google_android_gms_internal_measurement_zzks : com_google_android_gms_internal_measurement_zzksArr) {
                map = (Map) arrayMap6.get(com_google_android_gms_internal_measurement_zzks.name);
                if (map == null) {
                    map = zzix().zzl(str, com_google_android_gms_internal_measurement_zzks.name);
                    if (map == null) {
                        map = new ArrayMap();
                    }
                    arrayMap6.put(com_google_android_gms_internal_measurement_zzks.name, map);
                    map2 = map;
                } else {
                    map2 = map;
                }
                for (Integer intValue222 : r7.keySet()) {
                    length = intValue222.intValue();
                    if (hashSet.contains(Integer.valueOf(length))) {
                        zzge().zzit().zzg("Skipping failed audience ID", Integer.valueOf(length));
                    } else {
                        bitSet = (BitSet) arrayMap2.get(Integer.valueOf(length));
                        bitSet2 = (BitSet) arrayMap3.get(Integer.valueOf(length));
                        if (((zzkm) arrayMap.get(Integer.valueOf(length))) == null) {
                            com_google_android_gms_internal_measurement_zzkm = new zzkm();
                            arrayMap.put(Integer.valueOf(length), com_google_android_gms_internal_measurement_zzkm);
                            com_google_android_gms_internal_measurement_zzkm.zzasy = Boolean.valueOf(true);
                            bitSet = new BitSet();
                            arrayMap2.put(Integer.valueOf(length), bitSet);
                            bitSet2 = new BitSet();
                            arrayMap3.put(Integer.valueOf(length), bitSet2);
                        }
                        for (zzkh com_google_android_gms_internal_measurement_zzkh : (List) r7.get(Integer.valueOf(length))) {
                            if (zzge().isLoggable(2)) {
                                zzge().zzit().zzd("Evaluating filter. audience, filter, property", Integer.valueOf(length), com_google_android_gms_internal_measurement_zzkh.zzarp, zzga().zzbl(com_google_android_gms_internal_measurement_zzkh.zzasf));
                                zzge().zzit().zzg("Filter definition", zzga().zza(com_google_android_gms_internal_measurement_zzkh));
                            }
                            if (com_google_android_gms_internal_measurement_zzkh.zzarp == null || com_google_android_gms_internal_measurement_zzkh.zzarp.intValue() > 256) {
                                zzge().zzip().zze("Invalid property filter ID. appId, id", zzfg.zzbm(str), String.valueOf(com_google_android_gms_internal_measurement_zzkh.zzarp));
                                hashSet.add(Integer.valueOf(length));
                                break;
                            } else if (bitSet.get(com_google_android_gms_internal_measurement_zzkh.zzarp.intValue())) {
                                zzge().zzit().zze("Property filter already evaluated true. audience ID, filter ID", Integer.valueOf(length), com_google_android_gms_internal_measurement_zzkh.zzarp);
                            } else {
                                Boolean bool2;
                                Object obj4;
                                zzkf com_google_android_gms_internal_measurement_zzkf3 = com_google_android_gms_internal_measurement_zzkh.zzasg;
                                if (com_google_android_gms_internal_measurement_zzkf3 == null) {
                                    zzge().zzip().zzg("Missing property filter. property", zzga().zzbl(com_google_android_gms_internal_measurement_zzks.name));
                                    bool2 = null;
                                } else {
                                    boolean equals2 = Boolean.TRUE.equals(com_google_android_gms_internal_measurement_zzkf3.zzarx);
                                    if (com_google_android_gms_internal_measurement_zzks.zzate != null) {
                                        if (com_google_android_gms_internal_measurement_zzkf3.zzarw == null) {
                                            zzge().zzip().zzg("No number filter for long property. property", zzga().zzbl(com_google_android_gms_internal_measurement_zzks.name));
                                            bool2 = null;
                                        } else {
                                            bool2 = zza(zza(com_google_android_gms_internal_measurement_zzks.zzate.longValue(), com_google_android_gms_internal_measurement_zzkf3.zzarw), equals2);
                                        }
                                    } else if (com_google_android_gms_internal_measurement_zzks.zzarc != null) {
                                        if (com_google_android_gms_internal_measurement_zzkf3.zzarw == null) {
                                            zzge().zzip().zzg("No number filter for double property. property", zzga().zzbl(com_google_android_gms_internal_measurement_zzks.name));
                                            bool2 = null;
                                        } else {
                                            bool2 = zza(zza(com_google_android_gms_internal_measurement_zzks.zzarc.doubleValue(), com_google_android_gms_internal_measurement_zzkf3.zzarw), equals2);
                                        }
                                    } else if (com_google_android_gms_internal_measurement_zzks.zzajf == null) {
                                        zzge().zzip().zzg("User property has no value, property", zzga().zzbl(com_google_android_gms_internal_measurement_zzks.name));
                                        bool2 = null;
                                    } else if (com_google_android_gms_internal_measurement_zzkf3.zzarv == null) {
                                        if (com_google_android_gms_internal_measurement_zzkf3.zzarw == null) {
                                            zzge().zzip().zzg("No string or number filter defined. property", zzga().zzbl(com_google_android_gms_internal_measurement_zzks.name));
                                        } else if (zzka.zzck(com_google_android_gms_internal_measurement_zzks.zzajf)) {
                                            bool2 = zza(zza(com_google_android_gms_internal_measurement_zzks.zzajf, com_google_android_gms_internal_measurement_zzkf3.zzarw), equals2);
                                        } else {
                                            zzge().zzip().zze("Invalid user property value for Numeric number filter. property, value", zzga().zzbl(com_google_android_gms_internal_measurement_zzks.name), com_google_android_gms_internal_measurement_zzks.zzajf);
                                        }
                                        bool2 = null;
                                    } else {
                                        bool2 = zza(zza(com_google_android_gms_internal_measurement_zzks.zzajf, com_google_android_gms_internal_measurement_zzkf3.zzarv), equals2);
                                    }
                                }
                                zzfi zzit2 = zzge().zzit();
                                String str6 = "Property filter result";
                                if (bool2 == null) {
                                    obj4 = "null";
                                } else {
                                    Boolean bool3 = bool2;
                                }
                                zzit2.zzg(str6, obj4);
                                if (bool2 == null) {
                                    hashSet.add(Integer.valueOf(length));
                                } else {
                                    bitSet2.set(com_google_android_gms_internal_measurement_zzkh.zzarp.intValue());
                                    if (bool2.booleanValue()) {
                                        bitSet.set(com_google_android_gms_internal_measurement_zzkh.zzarp.intValue());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        zzkm[] com_google_android_gms_internal_measurement_zzkmArr = new zzkm[arrayMap2.size()];
        i2 = 0;
        for (Integer intValue2222 : arrayMap2.keySet()) {
            intValue = intValue2222.intValue();
            if (!hashSet.contains(Integer.valueOf(intValue))) {
                com_google_android_gms_internal_measurement_zzkm = (zzkm) arrayMap.get(Integer.valueOf(intValue));
                com_google_android_gms_internal_measurement_zzkm2 = com_google_android_gms_internal_measurement_zzkm == null ? new zzkm() : com_google_android_gms_internal_measurement_zzkm;
                int i4 = i2 + 1;
                com_google_android_gms_internal_measurement_zzkmArr[i2] = com_google_android_gms_internal_measurement_zzkm2;
                com_google_android_gms_internal_measurement_zzkm2.zzarl = Integer.valueOf(intValue);
                com_google_android_gms_internal_measurement_zzkm2.zzasw = new zzkr();
                com_google_android_gms_internal_measurement_zzkm2.zzasw.zzaul = zzka.zza((BitSet) arrayMap2.get(Integer.valueOf(intValue)));
                com_google_android_gms_internal_measurement_zzkm2.zzasw.zzauk = zzka.zza((BitSet) arrayMap3.get(Integer.valueOf(intValue)));
                zzix = zzix();
                zzace com_google_android_gms_internal_measurement_zzace = com_google_android_gms_internal_measurement_zzkm2.zzasw;
                zzix.zzch();
                zzix.zzab();
                Preconditions.checkNotEmpty(str);
                Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzace);
                try {
                    byte[] bArr = new byte[com_google_android_gms_internal_measurement_zzace.zzvm()];
                    zzabw zzb = zzabw.zzb(bArr, 0, bArr.length);
                    com_google_android_gms_internal_measurement_zzace.zza(zzb);
                    zzb.zzve();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("app_id", str);
                    contentValues.put("audience_id", Integer.valueOf(intValue));
                    contentValues.put("current_results", bArr);
                    try {
                        if (zzix.getWritableDatabase().insertWithOnConflict("audience_filter_values", null, contentValues, 5) == -1) {
                            zzix.zzge().zzim().zzg("Failed to insert filter results (got -1). appId", zzfg.zzbm(str));
                        }
                        i2 = i4;
                    } catch (SQLiteException e2) {
                        zzix.zzge().zzim().zze("Error storing filter results. appId", zzfg.zzbm(str), e2);
                        i2 = i4;
                    }
                } catch (IOException e3) {
                    zzix.zzge().zzim().zze("Configuration loss. Failed to serialize filter results. appId", zzfg.zzbm(str), e3);
                    i2 = i4;
                }
            }
        }
        return (zzkm[]) Arrays.copyOf(com_google_android_gms_internal_measurement_zzkmArr, i2);
    }

    protected final boolean zzhf() {
        return false;
    }
}
