package com.google.android.gms.internal;

import android.content.ContentValues;
import android.database.sqlite.SQLiteException;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import com.google.android.gms.common.internal.zzbq;
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

final class zzcgk extends zzcjl {
    zzcgk(zzcim com_google_android_gms_internal_zzcim) {
        super(com_google_android_gms_internal_zzcim);
    }

    private final Boolean zza(double d, zzclu com_google_android_gms_internal_zzclu) {
        try {
            return zza(new BigDecimal(d), com_google_android_gms_internal_zzclu, Math.ulp(d));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private final Boolean zza(long j, zzclu com_google_android_gms_internal_zzclu) {
        try {
            return zza(new BigDecimal(j), com_google_android_gms_internal_zzclu, 0.0d);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private final Boolean zza(zzcls com_google_android_gms_internal_zzcls, zzcmb com_google_android_gms_internal_zzcmb, long j) {
        Boolean zza;
        if (com_google_android_gms_internal_zzcls.zzjka != null) {
            zza = zza(j, com_google_android_gms_internal_zzcls.zzjka);
            if (zza == null) {
                return null;
            }
            if (!zza.booleanValue()) {
                return Boolean.valueOf(false);
            }
        }
        Set hashSet = new HashSet();
        for (zzclt com_google_android_gms_internal_zzclt : com_google_android_gms_internal_zzcls.zzjjy) {
            if (TextUtils.isEmpty(com_google_android_gms_internal_zzclt.zzjkf)) {
                zzawy().zzazf().zzj("null or empty param name in filter. event", zzawt().zzjh(com_google_android_gms_internal_zzcmb.name));
                return null;
            }
            hashSet.add(com_google_android_gms_internal_zzclt.zzjkf);
        }
        Map arrayMap = new ArrayMap();
        for (zzcmc com_google_android_gms_internal_zzcmc : com_google_android_gms_internal_zzcmb.zzjlh) {
            if (hashSet.contains(com_google_android_gms_internal_zzcmc.name)) {
                if (com_google_android_gms_internal_zzcmc.zzjll != null) {
                    arrayMap.put(com_google_android_gms_internal_zzcmc.name, com_google_android_gms_internal_zzcmc.zzjll);
                } else if (com_google_android_gms_internal_zzcmc.zzjjl != null) {
                    arrayMap.put(com_google_android_gms_internal_zzcmc.name, com_google_android_gms_internal_zzcmc.zzjjl);
                } else if (com_google_android_gms_internal_zzcmc.zzgcc != null) {
                    arrayMap.put(com_google_android_gms_internal_zzcmc.name, com_google_android_gms_internal_zzcmc.zzgcc);
                } else {
                    zzawy().zzazf().zze("Unknown value for param. event, param", zzawt().zzjh(com_google_android_gms_internal_zzcmb.name), zzawt().zzji(com_google_android_gms_internal_zzcmc.name));
                    return null;
                }
            }
        }
        for (zzclt com_google_android_gms_internal_zzclt2 : com_google_android_gms_internal_zzcls.zzjjy) {
            boolean equals = Boolean.TRUE.equals(com_google_android_gms_internal_zzclt2.zzjke);
            String str = com_google_android_gms_internal_zzclt2.zzjkf;
            if (TextUtils.isEmpty(str)) {
                zzawy().zzazf().zzj("Event has empty param name. event", zzawt().zzjh(com_google_android_gms_internal_zzcmb.name));
                return null;
            }
            Object obj = arrayMap.get(str);
            if (obj instanceof Long) {
                if (com_google_android_gms_internal_zzclt2.zzjkd == null) {
                    zzawy().zzazf().zze("No number filter for long param. event, param", zzawt().zzjh(com_google_android_gms_internal_zzcmb.name), zzawt().zzji(str));
                    return null;
                }
                zza = zza(((Long) obj).longValue(), com_google_android_gms_internal_zzclt2.zzjkd);
                if (zza == null) {
                    return null;
                }
                if (((!zza.booleanValue() ? 1 : 0) ^ equals) != 0) {
                    return Boolean.valueOf(false);
                }
            } else if (obj instanceof Double) {
                if (com_google_android_gms_internal_zzclt2.zzjkd == null) {
                    zzawy().zzazf().zze("No number filter for double param. event, param", zzawt().zzjh(com_google_android_gms_internal_zzcmb.name), zzawt().zzji(str));
                    return null;
                }
                zza = zza(((Double) obj).doubleValue(), com_google_android_gms_internal_zzclt2.zzjkd);
                if (zza == null) {
                    return null;
                }
                if (((!zza.booleanValue() ? 1 : 0) ^ equals) != 0) {
                    return Boolean.valueOf(false);
                }
            } else if (obj instanceof String) {
                if (com_google_android_gms_internal_zzclt2.zzjkc != null) {
                    zza = zza((String) obj, com_google_android_gms_internal_zzclt2.zzjkc);
                } else if (com_google_android_gms_internal_zzclt2.zzjkd == null) {
                    zzawy().zzazf().zze("No filter for String param. event, param", zzawt().zzjh(com_google_android_gms_internal_zzcmb.name), zzawt().zzji(str));
                    return null;
                } else if (zzclq.zzkk((String) obj)) {
                    zza = zza((String) obj, com_google_android_gms_internal_zzclt2.zzjkd);
                } else {
                    zzawy().zzazf().zze("Invalid param value for number filter. event, param", zzawt().zzjh(com_google_android_gms_internal_zzcmb.name), zzawt().zzji(str));
                    return null;
                }
                if (zza == null) {
                    return null;
                }
                if (((!zza.booleanValue() ? 1 : 0) ^ equals) != 0) {
                    return Boolean.valueOf(false);
                }
            } else if (obj == null) {
                zzawy().zzazj().zze("Missing param for filter. event, param", zzawt().zzjh(com_google_android_gms_internal_zzcmb.name), zzawt().zzji(str));
                return Boolean.valueOf(false);
            } else {
                zzawy().zzazf().zze("Unknown param type. event, param", zzawt().zzjh(com_google_android_gms_internal_zzcmb.name), zzawt().zzji(str));
                return null;
            }
        }
        return Boolean.valueOf(true);
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
                    zzawy().zzazf().zzj("Invalid regular expression in REGEXP audience filter. expression", str3);
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

    private final Boolean zza(String str, zzclu com_google_android_gms_internal_zzclu) {
        Boolean bool = null;
        if (zzclq.zzkk(str)) {
            try {
                bool = zza(new BigDecimal(str), com_google_android_gms_internal_zzclu, 0.0d);
            } catch (NumberFormatException e) {
            }
        }
        return bool;
    }

    private final Boolean zza(String str, zzclw com_google_android_gms_internal_zzclw) {
        int i = 0;
        String str2 = null;
        zzbq.checkNotNull(com_google_android_gms_internal_zzclw);
        if (str == null || com_google_android_gms_internal_zzclw.zzjko == null || com_google_android_gms_internal_zzclw.zzjko.intValue() == 0) {
            return null;
        }
        List list;
        if (com_google_android_gms_internal_zzclw.zzjko.intValue() == 6) {
            if (com_google_android_gms_internal_zzclw.zzjkr == null || com_google_android_gms_internal_zzclw.zzjkr.length == 0) {
                return null;
            }
        } else if (com_google_android_gms_internal_zzclw.zzjkp == null) {
            return null;
        }
        int intValue = com_google_android_gms_internal_zzclw.zzjko.intValue();
        boolean z = com_google_android_gms_internal_zzclw.zzjkq != null && com_google_android_gms_internal_zzclw.zzjkq.booleanValue();
        String toUpperCase = (z || intValue == 1 || intValue == 6) ? com_google_android_gms_internal_zzclw.zzjkp : com_google_android_gms_internal_zzclw.zzjkp.toUpperCase(Locale.ENGLISH);
        if (com_google_android_gms_internal_zzclw.zzjkr == null) {
            list = null;
        } else {
            String[] strArr = com_google_android_gms_internal_zzclw.zzjkr;
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
    private static java.lang.Boolean zza(java.math.BigDecimal r10, com.google.android.gms.internal.zzclu r11, double r12) {
        /*
        r8 = 4;
        r7 = -1;
        r1 = 0;
        r0 = 1;
        r2 = 0;
        com.google.android.gms.common.internal.zzbq.checkNotNull(r11);
        r3 = r11.zzjkg;
        if (r3 == 0) goto L_0x0014;
    L_0x000c:
        r3 = r11.zzjkg;
        r3 = r3.intValue();
        if (r3 != 0) goto L_0x0016;
    L_0x0014:
        r0 = r2;
    L_0x0015:
        return r0;
    L_0x0016:
        r3 = r11.zzjkg;
        r3 = r3.intValue();
        if (r3 != r8) goto L_0x0028;
    L_0x001e:
        r3 = r11.zzjkj;
        if (r3 == 0) goto L_0x0026;
    L_0x0022:
        r3 = r11.zzjkk;
        if (r3 != 0) goto L_0x002e;
    L_0x0026:
        r0 = r2;
        goto L_0x0015;
    L_0x0028:
        r3 = r11.zzjki;
        if (r3 != 0) goto L_0x002e;
    L_0x002c:
        r0 = r2;
        goto L_0x0015;
    L_0x002e:
        r3 = r11.zzjkg;
        r6 = r3.intValue();
        r3 = r11.zzjkg;
        r3 = r3.intValue();
        if (r3 != r8) goto L_0x0066;
    L_0x003c:
        r3 = r11.zzjkj;
        r3 = com.google.android.gms.internal.zzclq.zzkk(r3);
        if (r3 == 0) goto L_0x004c;
    L_0x0044:
        r3 = r11.zzjkk;
        r3 = com.google.android.gms.internal.zzclq.zzkk(r3);
        if (r3 != 0) goto L_0x004e;
    L_0x004c:
        r0 = r2;
        goto L_0x0015;
    L_0x004e:
        r4 = new java.math.BigDecimal;	 Catch:{ NumberFormatException -> 0x0063 }
        r3 = r11.zzjkj;	 Catch:{ NumberFormatException -> 0x0063 }
        r4.<init>(r3);	 Catch:{ NumberFormatException -> 0x0063 }
        r3 = new java.math.BigDecimal;	 Catch:{ NumberFormatException -> 0x0063 }
        r5 = r11.zzjkk;	 Catch:{ NumberFormatException -> 0x0063 }
        r3.<init>(r5);	 Catch:{ NumberFormatException -> 0x0063 }
        r5 = r2;
    L_0x005d:
        if (r6 != r8) goto L_0x007e;
    L_0x005f:
        if (r4 != 0) goto L_0x0080;
    L_0x0061:
        r0 = r2;
        goto L_0x0015;
    L_0x0063:
        r0 = move-exception;
        r0 = r2;
        goto L_0x0015;
    L_0x0066:
        r3 = r11.zzjki;
        r3 = com.google.android.gms.internal.zzclq.zzkk(r3);
        if (r3 != 0) goto L_0x0070;
    L_0x006e:
        r0 = r2;
        goto L_0x0015;
    L_0x0070:
        r3 = new java.math.BigDecimal;	 Catch:{ NumberFormatException -> 0x007b }
        r4 = r11.zzjki;	 Catch:{ NumberFormatException -> 0x007b }
        r3.<init>(r4);	 Catch:{ NumberFormatException -> 0x007b }
        r4 = r2;
        r5 = r3;
        r3 = r2;
        goto L_0x005d;
    L_0x007b:
        r0 = move-exception;
        r0 = r2;
        goto L_0x0015;
    L_0x007e:
        if (r5 == 0) goto L_0x0083;
    L_0x0080:
        switch(r6) {
            case 1: goto L_0x0085;
            case 2: goto L_0x0092;
            case 3: goto L_0x00a0;
            case 4: goto L_0x00ee;
            default: goto L_0x0083;
        };
    L_0x0083:
        r0 = r2;
        goto L_0x0015;
    L_0x0085:
        r2 = r10.compareTo(r5);
        if (r2 != r7) goto L_0x0090;
    L_0x008b:
        r0 = java.lang.Boolean.valueOf(r0);
        goto L_0x0015;
    L_0x0090:
        r0 = r1;
        goto L_0x008b;
    L_0x0092:
        r2 = r10.compareTo(r5);
        if (r2 != r0) goto L_0x009e;
    L_0x0098:
        r0 = java.lang.Boolean.valueOf(r0);
        goto L_0x0015;
    L_0x009e:
        r0 = r1;
        goto L_0x0098;
    L_0x00a0:
        r2 = 0;
        r2 = (r12 > r2 ? 1 : (r12 == r2 ? 0 : -1));
        if (r2 == 0) goto L_0x00e0;
    L_0x00a6:
        r2 = new java.math.BigDecimal;
        r2.<init>(r12);
        r3 = new java.math.BigDecimal;
        r4 = 2;
        r3.<init>(r4);
        r2 = r2.multiply(r3);
        r2 = r5.subtract(r2);
        r2 = r10.compareTo(r2);
        if (r2 != r0) goto L_0x00de;
    L_0x00bf:
        r2 = new java.math.BigDecimal;
        r2.<init>(r12);
        r3 = new java.math.BigDecimal;
        r4 = 2;
        r3.<init>(r4);
        r2 = r2.multiply(r3);
        r2 = r5.add(r2);
        r2 = r10.compareTo(r2);
        if (r2 != r7) goto L_0x00de;
    L_0x00d8:
        r0 = java.lang.Boolean.valueOf(r0);
        goto L_0x0015;
    L_0x00de:
        r0 = r1;
        goto L_0x00d8;
    L_0x00e0:
        r2 = r10.compareTo(r5);
        if (r2 != 0) goto L_0x00ec;
    L_0x00e6:
        r0 = java.lang.Boolean.valueOf(r0);
        goto L_0x0015;
    L_0x00ec:
        r0 = r1;
        goto L_0x00e6;
    L_0x00ee:
        r2 = r10.compareTo(r4);
        if (r2 == r7) goto L_0x0100;
    L_0x00f4:
        r2 = r10.compareTo(r3);
        if (r2 == r0) goto L_0x0100;
    L_0x00fa:
        r0 = java.lang.Boolean.valueOf(r0);
        goto L_0x0015;
    L_0x0100:
        r0 = r1;
        goto L_0x00fa;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzcgk.zza(java.math.BigDecimal, com.google.android.gms.internal.zzclu, double):java.lang.Boolean");
    }

    final zzcma[] zza(String str, zzcmb[] com_google_android_gms_internal_zzcmbArr, zzcmg[] com_google_android_gms_internal_zzcmgArr) {
        int intValue;
        BitSet bitSet;
        BitSet bitSet2;
        Map map;
        Map map2;
        zzbq.zzgm(str);
        HashSet hashSet = new HashSet();
        ArrayMap arrayMap = new ArrayMap();
        Map arrayMap2 = new ArrayMap();
        ArrayMap arrayMap3 = new ArrayMap();
        Map zzje = zzaws().zzje(str);
        if (zzje != null) {
            for (Integer intValue2 : zzje.keySet()) {
                intValue = intValue2.intValue();
                zzcmf com_google_android_gms_internal_zzcmf = (zzcmf) zzje.get(Integer.valueOf(intValue));
                bitSet = (BitSet) arrayMap2.get(Integer.valueOf(intValue));
                bitSet2 = (BitSet) arrayMap3.get(Integer.valueOf(intValue));
                if (bitSet == null) {
                    bitSet = new BitSet();
                    arrayMap2.put(Integer.valueOf(intValue), bitSet);
                    bitSet2 = new BitSet();
                    arrayMap3.put(Integer.valueOf(intValue), bitSet2);
                }
                for (int i = 0; i < (com_google_android_gms_internal_zzcmf.zzjmp.length << 6); i++) {
                    if (zzclq.zza(com_google_android_gms_internal_zzcmf.zzjmp, i)) {
                        zzawy().zzazj().zze("Filter already evaluated. audience ID, filter ID", Integer.valueOf(intValue), Integer.valueOf(i));
                        bitSet2.set(i);
                        if (zzclq.zza(com_google_android_gms_internal_zzcmf.zzjmq, i)) {
                            bitSet.set(i);
                        }
                    }
                }
                zzcma com_google_android_gms_internal_zzcma = new zzcma();
                arrayMap.put(Integer.valueOf(intValue), com_google_android_gms_internal_zzcma);
                com_google_android_gms_internal_zzcma.zzjlf = Boolean.valueOf(false);
                com_google_android_gms_internal_zzcma.zzjle = com_google_android_gms_internal_zzcmf;
                com_google_android_gms_internal_zzcma.zzjld = new zzcmf();
                com_google_android_gms_internal_zzcma.zzjld.zzjmq = zzclq.zza(bitSet);
                com_google_android_gms_internal_zzcma.zzjld.zzjmp = zzclq.zza(bitSet2);
            }
        }
        if (com_google_android_gms_internal_zzcmbArr != null) {
            ArrayMap arrayMap4 = new ArrayMap();
            for (zzcmb com_google_android_gms_internal_zzcmb : com_google_android_gms_internal_zzcmbArr) {
                zzcgw com_google_android_gms_internal_zzcgw;
                zzcgw zzae = zzaws().zzae(str, com_google_android_gms_internal_zzcmb.name);
                if (zzae == null) {
                    zzawy().zzazf().zze("Event aggregate wasn't created during raw event logging. appId, event", zzchm.zzjk(str), zzawt().zzjh(com_google_android_gms_internal_zzcmb.name));
                    com_google_android_gms_internal_zzcgw = new zzcgw(str, com_google_android_gms_internal_zzcmb.name, 1, 1, com_google_android_gms_internal_zzcmb.zzjli.longValue(), 0, null, null, null);
                } else {
                    com_google_android_gms_internal_zzcgw = zzae.zzayw();
                }
                zzaws().zza(com_google_android_gms_internal_zzcgw);
                long j = com_google_android_gms_internal_zzcgw.zzizk;
                map = (Map) arrayMap4.get(com_google_android_gms_internal_zzcmb.name);
                if (map == null) {
                    map = zzaws().zzaj(str, com_google_android_gms_internal_zzcmb.name);
                    if (map == null) {
                        map = new ArrayMap();
                    }
                    arrayMap4.put(com_google_android_gms_internal_zzcmb.name, map);
                    map2 = map;
                } else {
                    map2 = map;
                }
                for (Integer intValue22 : r7.keySet()) {
                    int intValue3 = intValue22.intValue();
                    if (hashSet.contains(Integer.valueOf(intValue3))) {
                        zzawy().zzazj().zzj("Skipping failed audience ID", Integer.valueOf(intValue3));
                    } else {
                        bitSet = (BitSet) arrayMap2.get(Integer.valueOf(intValue3));
                        bitSet2 = (BitSet) arrayMap3.get(Integer.valueOf(intValue3));
                        if (((zzcma) arrayMap.get(Integer.valueOf(intValue3))) == null) {
                            zzcma com_google_android_gms_internal_zzcma2 = new zzcma();
                            arrayMap.put(Integer.valueOf(intValue3), com_google_android_gms_internal_zzcma2);
                            com_google_android_gms_internal_zzcma2.zzjlf = Boolean.valueOf(true);
                            bitSet = new BitSet();
                            arrayMap2.put(Integer.valueOf(intValue3), bitSet);
                            bitSet2 = new BitSet();
                            arrayMap3.put(Integer.valueOf(intValue3), bitSet2);
                        }
                        for (zzcls com_google_android_gms_internal_zzcls : (List) r7.get(Integer.valueOf(intValue3))) {
                            if (zzawy().zzae(2)) {
                                zzawy().zzazj().zzd("Evaluating filter. audience, filter, event", Integer.valueOf(intValue3), com_google_android_gms_internal_zzcls.zzjjw, zzawt().zzjh(com_google_android_gms_internal_zzcls.zzjjx));
                                zzawy().zzazj().zzj("Filter definition", zzawt().zza(com_google_android_gms_internal_zzcls));
                            }
                            if (com_google_android_gms_internal_zzcls.zzjjw == null || com_google_android_gms_internal_zzcls.zzjjw.intValue() > 256) {
                                zzawy().zzazf().zze("Invalid event filter ID. appId, id", zzchm.zzjk(str), String.valueOf(com_google_android_gms_internal_zzcls.zzjjw));
                            } else if (bitSet.get(com_google_android_gms_internal_zzcls.zzjjw.intValue())) {
                                zzawy().zzazj().zze("Event filter already evaluated true. audience ID, filter ID", Integer.valueOf(intValue3), com_google_android_gms_internal_zzcls.zzjjw);
                            } else {
                                Object obj;
                                Boolean zza = zza(com_google_android_gms_internal_zzcls, com_google_android_gms_internal_zzcmb, j);
                                zzcho zzazj = zzawy().zzazj();
                                String str2 = "Event filter result";
                                if (zza == null) {
                                    obj = "null";
                                } else {
                                    Boolean bool = zza;
                                }
                                zzazj.zzj(str2, obj);
                                if (zza == null) {
                                    hashSet.add(Integer.valueOf(intValue3));
                                } else {
                                    bitSet2.set(com_google_android_gms_internal_zzcls.zzjjw.intValue());
                                    if (zza.booleanValue()) {
                                        bitSet.set(com_google_android_gms_internal_zzcls.zzjjw.intValue());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (com_google_android_gms_internal_zzcmgArr != null) {
            Map arrayMap5 = new ArrayMap();
            for (zzcmg com_google_android_gms_internal_zzcmg : com_google_android_gms_internal_zzcmgArr) {
                map = (Map) arrayMap5.get(com_google_android_gms_internal_zzcmg.name);
                if (map == null) {
                    map = zzaws().zzak(str, com_google_android_gms_internal_zzcmg.name);
                    if (map == null) {
                        map = new ArrayMap();
                    }
                    arrayMap5.put(com_google_android_gms_internal_zzcmg.name, map);
                    map2 = map;
                } else {
                    map2 = map;
                }
                for (Integer intValue222 : r7.keySet()) {
                    int intValue4 = intValue222.intValue();
                    if (hashSet.contains(Integer.valueOf(intValue4))) {
                        zzawy().zzazj().zzj("Skipping failed audience ID", Integer.valueOf(intValue4));
                    } else {
                        bitSet = (BitSet) arrayMap2.get(Integer.valueOf(intValue4));
                        bitSet2 = (BitSet) arrayMap3.get(Integer.valueOf(intValue4));
                        if (((zzcma) arrayMap.get(Integer.valueOf(intValue4))) == null) {
                            com_google_android_gms_internal_zzcma2 = new zzcma();
                            arrayMap.put(Integer.valueOf(intValue4), com_google_android_gms_internal_zzcma2);
                            com_google_android_gms_internal_zzcma2.zzjlf = Boolean.valueOf(true);
                            bitSet = new BitSet();
                            arrayMap2.put(Integer.valueOf(intValue4), bitSet);
                            bitSet2 = new BitSet();
                            arrayMap3.put(Integer.valueOf(intValue4), bitSet2);
                        }
                        for (zzclv com_google_android_gms_internal_zzclv : (List) r7.get(Integer.valueOf(intValue4))) {
                            if (zzawy().zzae(2)) {
                                zzawy().zzazj().zzd("Evaluating filter. audience, filter, property", Integer.valueOf(intValue4), com_google_android_gms_internal_zzclv.zzjjw, zzawt().zzjj(com_google_android_gms_internal_zzclv.zzjkm));
                                zzawy().zzazj().zzj("Filter definition", zzawt().zza(com_google_android_gms_internal_zzclv));
                            }
                            if (com_google_android_gms_internal_zzclv.zzjjw == null || com_google_android_gms_internal_zzclv.zzjjw.intValue() > 256) {
                                zzawy().zzazf().zze("Invalid property filter ID. appId, id", zzchm.zzjk(str), String.valueOf(com_google_android_gms_internal_zzclv.zzjjw));
                                hashSet.add(Integer.valueOf(intValue4));
                                break;
                            } else if (bitSet.get(com_google_android_gms_internal_zzclv.zzjjw.intValue())) {
                                zzawy().zzazj().zze("Property filter already evaluated true. audience ID, filter ID", Integer.valueOf(intValue4), com_google_android_gms_internal_zzclv.zzjjw);
                            } else {
                                Object obj2;
                                zzclt com_google_android_gms_internal_zzclt = com_google_android_gms_internal_zzclv.zzjkn;
                                if (com_google_android_gms_internal_zzclt == null) {
                                    zzawy().zzazf().zzj("Missing property filter. property", zzawt().zzjj(com_google_android_gms_internal_zzcmg.name));
                                    bool = null;
                                } else {
                                    boolean equals = Boolean.TRUE.equals(com_google_android_gms_internal_zzclt.zzjke);
                                    if (com_google_android_gms_internal_zzcmg.zzjll != null) {
                                        if (com_google_android_gms_internal_zzclt.zzjkd == null) {
                                            zzawy().zzazf().zzj("No number filter for long property. property", zzawt().zzjj(com_google_android_gms_internal_zzcmg.name));
                                            bool = null;
                                        } else {
                                            bool = zza(zza(com_google_android_gms_internal_zzcmg.zzjll.longValue(), com_google_android_gms_internal_zzclt.zzjkd), equals);
                                        }
                                    } else if (com_google_android_gms_internal_zzcmg.zzjjl != null) {
                                        if (com_google_android_gms_internal_zzclt.zzjkd == null) {
                                            zzawy().zzazf().zzj("No number filter for double property. property", zzawt().zzjj(com_google_android_gms_internal_zzcmg.name));
                                            bool = null;
                                        } else {
                                            bool = zza(zza(com_google_android_gms_internal_zzcmg.zzjjl.doubleValue(), com_google_android_gms_internal_zzclt.zzjkd), equals);
                                        }
                                    } else if (com_google_android_gms_internal_zzcmg.zzgcc == null) {
                                        zzawy().zzazf().zzj("User property has no value, property", zzawt().zzjj(com_google_android_gms_internal_zzcmg.name));
                                        bool = null;
                                    } else if (com_google_android_gms_internal_zzclt.zzjkc == null) {
                                        if (com_google_android_gms_internal_zzclt.zzjkd == null) {
                                            zzawy().zzazf().zzj("No string or number filter defined. property", zzawt().zzjj(com_google_android_gms_internal_zzcmg.name));
                                        } else if (zzclq.zzkk(com_google_android_gms_internal_zzcmg.zzgcc)) {
                                            bool = zza(zza(com_google_android_gms_internal_zzcmg.zzgcc, com_google_android_gms_internal_zzclt.zzjkd), equals);
                                        } else {
                                            zzawy().zzazf().zze("Invalid user property value for Numeric number filter. property, value", zzawt().zzjj(com_google_android_gms_internal_zzcmg.name), com_google_android_gms_internal_zzcmg.zzgcc);
                                        }
                                        bool = null;
                                    } else {
                                        bool = zza(zza(com_google_android_gms_internal_zzcmg.zzgcc, com_google_android_gms_internal_zzclt.zzjkc), equals);
                                    }
                                }
                                zzcho zzazj2 = zzawy().zzazj();
                                String str3 = "Property filter result";
                                if (bool == null) {
                                    obj2 = "null";
                                } else {
                                    zza = bool;
                                }
                                zzazj2.zzj(str3, obj2);
                                if (bool == null) {
                                    hashSet.add(Integer.valueOf(intValue4));
                                } else {
                                    bitSet2.set(com_google_android_gms_internal_zzclv.zzjjw.intValue());
                                    if (bool.booleanValue()) {
                                        bitSet.set(com_google_android_gms_internal_zzclv.zzjjw.intValue());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        zzcma[] com_google_android_gms_internal_zzcmaArr = new zzcma[arrayMap2.size()];
        int i2 = 0;
        for (Integer intValue2222 : arrayMap2.keySet()) {
            intValue = intValue2222.intValue();
            if (!hashSet.contains(Integer.valueOf(intValue))) {
                com_google_android_gms_internal_zzcma2 = (zzcma) arrayMap.get(Integer.valueOf(intValue));
                com_google_android_gms_internal_zzcma = com_google_android_gms_internal_zzcma2 == null ? new zzcma() : com_google_android_gms_internal_zzcma2;
                int i3 = i2 + 1;
                com_google_android_gms_internal_zzcmaArr[i2] = com_google_android_gms_internal_zzcma;
                com_google_android_gms_internal_zzcma.zzjjs = Integer.valueOf(intValue);
                com_google_android_gms_internal_zzcma.zzjld = new zzcmf();
                com_google_android_gms_internal_zzcma.zzjld.zzjmq = zzclq.zza((BitSet) arrayMap2.get(Integer.valueOf(intValue)));
                com_google_android_gms_internal_zzcma.zzjld.zzjmp = zzclq.zza((BitSet) arrayMap3.get(Integer.valueOf(intValue)));
                zzcjk zzaws = zzaws();
                zzfjs com_google_android_gms_internal_zzfjs = com_google_android_gms_internal_zzcma.zzjld;
                zzaws.zzxf();
                zzaws.zzve();
                zzbq.zzgm(str);
                zzbq.checkNotNull(com_google_android_gms_internal_zzfjs);
                try {
                    byte[] bArr = new byte[com_google_android_gms_internal_zzfjs.zzho()];
                    zzfjk zzo = zzfjk.zzo(bArr, 0, bArr.length);
                    com_google_android_gms_internal_zzfjs.zza(zzo);
                    zzo.zzcwt();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("app_id", str);
                    contentValues.put("audience_id", Integer.valueOf(intValue));
                    contentValues.put("current_results", bArr);
                    try {
                        if (zzaws.getWritableDatabase().insertWithOnConflict("audience_filter_values", null, contentValues, 5) == -1) {
                            zzaws.zzawy().zzazd().zzj("Failed to insert filter results (got -1). appId", zzchm.zzjk(str));
                        }
                        i2 = i3;
                    } catch (SQLiteException e) {
                        zzaws.zzawy().zzazd().zze("Error storing filter results. appId", zzchm.zzjk(str), e);
                        i2 = i3;
                    }
                } catch (IOException e2) {
                    zzaws.zzawy().zzazd().zze("Configuration loss. Failed to serialize filter results. appId", zzchm.zzjk(str), e2);
                    i2 = i3;
                }
            }
        }
        return (zzcma[]) Arrays.copyOf(com_google_android_gms_internal_zzcmaArr, i2);
    }

    protected final boolean zzaxz() {
        return false;
    }
}
