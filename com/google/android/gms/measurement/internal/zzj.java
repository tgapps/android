package com.google.android.gms.measurement.internal;

import android.content.ContentValues;
import android.database.sqlite.SQLiteException;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Pair;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.internal.measurement.zzfv;
import com.google.android.gms.internal.measurement.zzfw;
import com.google.android.gms.internal.measurement.zzfx;
import com.google.android.gms.internal.measurement.zzfy;
import com.google.android.gms.internal.measurement.zzfz;
import com.google.android.gms.internal.measurement.zzgd;
import com.google.android.gms.internal.measurement.zzge;
import com.google.android.gms.internal.measurement.zzgf;
import com.google.android.gms.internal.measurement.zzgg;
import com.google.android.gms.internal.measurement.zzgj;
import com.google.android.gms.internal.measurement.zzgk;
import com.google.android.gms.internal.measurement.zzgl;
import com.google.android.gms.internal.measurement.zzyy;
import com.google.android.gms.internal.measurement.zzzg;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

final class zzj extends zzez {
    zzj(zzfa com_google_android_gms_measurement_internal_zzfa) {
        super(com_google_android_gms_measurement_internal_zzfa);
    }

    protected final boolean zzgt() {
        return false;
    }

    final zzgd[] zza(String str, zzgf[] com_google_android_gms_internal_measurement_zzgfArr, zzgl[] com_google_android_gms_internal_measurement_zzglArr) {
        BitSet bitSet;
        BitSet bitSet2;
        Map arrayMap;
        int i;
        Map map;
        int i2;
        Object obj;
        zzgd com_google_android_gms_internal_measurement_zzgd;
        Long l;
        zzco zzjq;
        int i3;
        int length;
        Map map2;
        zzgd com_google_android_gms_internal_measurement_zzgd2;
        ArrayMap arrayMap2;
        ArrayMap arrayMap3;
        Preconditions.checkNotEmpty(str);
        HashSet hashSet = new HashSet();
        ArrayMap arrayMap4 = new ArrayMap();
        Map arrayMap5 = new ArrayMap();
        ArrayMap arrayMap6 = new ArrayMap();
        ArrayMap arrayMap7 = new ArrayMap();
        ArrayMap arrayMap8 = new ArrayMap();
        boolean zzd = zzgq().zzd(str, zzaf.zzakw);
        Map zzbo = zzjq().zzbo(str);
        if (zzbo != null) {
            for (Integer intValue : zzbo.keySet()) {
                int intValue2 = intValue.intValue();
                zzgj com_google_android_gms_internal_measurement_zzgj = (zzgj) zzbo.get(Integer.valueOf(intValue2));
                bitSet = (BitSet) arrayMap5.get(Integer.valueOf(intValue2));
                bitSet2 = (BitSet) arrayMap6.get(Integer.valueOf(intValue2));
                if (zzd) {
                    arrayMap = new ArrayMap();
                    if (!(com_google_android_gms_internal_measurement_zzgj == null || com_google_android_gms_internal_measurement_zzgj.zzayg == null)) {
                        for (zzge com_google_android_gms_internal_measurement_zzge : com_google_android_gms_internal_measurement_zzgj.zzayg) {
                            if (com_google_android_gms_internal_measurement_zzge.zzawq != null) {
                                arrayMap.put(com_google_android_gms_internal_measurement_zzge.zzawq, com_google_android_gms_internal_measurement_zzge.zzawr);
                            }
                        }
                    }
                    arrayMap7.put(Integer.valueOf(intValue2), arrayMap);
                    map = arrayMap;
                } else {
                    map = null;
                }
                if (bitSet == null) {
                    bitSet = new BitSet();
                    arrayMap5.put(Integer.valueOf(intValue2), bitSet);
                    bitSet2 = new BitSet();
                    arrayMap6.put(Integer.valueOf(intValue2), bitSet2);
                }
                for (i2 = 0; i2 < (com_google_android_gms_internal_measurement_zzgj.zzaye.length << 6); i2++) {
                    obj = null;
                    if (zzfg.zza(com_google_android_gms_internal_measurement_zzgj.zzaye, i2)) {
                        zzgo().zzjl().zze("Filter already evaluated. audience ID, filter ID", Integer.valueOf(intValue2), Integer.valueOf(i2));
                        bitSet2.set(i2);
                        if (zzfg.zza(com_google_android_gms_internal_measurement_zzgj.zzayf, i2)) {
                            bitSet.set(i2);
                            obj = 1;
                        }
                    }
                    if (map != null && r6 == null) {
                        map.remove(Integer.valueOf(i2));
                    }
                }
                com_google_android_gms_internal_measurement_zzgd = new zzgd();
                arrayMap4.put(Integer.valueOf(intValue2), com_google_android_gms_internal_measurement_zzgd);
                com_google_android_gms_internal_measurement_zzgd.zzawo = Boolean.valueOf(false);
                com_google_android_gms_internal_measurement_zzgd.zzawn = com_google_android_gms_internal_measurement_zzgj;
                com_google_android_gms_internal_measurement_zzgd.zzawm = new zzgj();
                com_google_android_gms_internal_measurement_zzgd.zzawm.zzayf = zzfg.zza(bitSet);
                com_google_android_gms_internal_measurement_zzgd.zzawm.zzaye = zzfg.zza(bitSet2);
                if (zzd) {
                    com_google_android_gms_internal_measurement_zzgd.zzawm.zzayg = zzd(map);
                    arrayMap8.put(Integer.valueOf(intValue2), new ArrayMap());
                }
            }
        }
        if (com_google_android_gms_internal_measurement_zzgfArr != null) {
            zzgf com_google_android_gms_internal_measurement_zzgf = null;
            long j = 0;
            l = null;
            ArrayMap arrayMap9 = new ArrayMap();
            for (zzgf com_google_android_gms_internal_measurement_zzgf2 : com_google_android_gms_internal_measurement_zzgfArr) {
                zzgg[] com_google_android_gms_internal_measurement_zzggArr;
                String str2;
                Long l2;
                long j2;
                zzgf com_google_android_gms_internal_measurement_zzgf3;
                zzz zzg;
                zzz com_google_android_gms_measurement_internal_zzz;
                Map map3;
                int intValue3;
                BitSet bitSet3;
                Map map4;
                BitSet bitSet4;
                String str3 = com_google_android_gms_internal_measurement_zzgf2.name;
                zzgg[] com_google_android_gms_internal_measurement_zzggArr2 = com_google_android_gms_internal_measurement_zzgf2.zzawt;
                if (zzgq().zzd(str, zzaf.zzakq)) {
                    zzjo();
                    Long l3 = (Long) zzfg.zzb(com_google_android_gms_internal_measurement_zzgf2, "_eid");
                    Object obj2 = l3 != null ? 1 : null;
                    Object obj3 = (obj2 == null || !str3.equals("_ep")) ? null : 1;
                    if (obj3 != null) {
                        zzjo();
                        str3 = (String) zzfg.zzb(com_google_android_gms_internal_measurement_zzgf2, "_en");
                        if (TextUtils.isEmpty(str3)) {
                            zzgo().zzjd().zzg("Extra parameter without an event name. eventId", l3);
                        } else {
                            Long l4;
                            int i4;
                            if (com_google_android_gms_internal_measurement_zzgf == null || l == null || l3.longValue() != l.longValue()) {
                                Pair zza = zzjq().zza(str, l3);
                                if (zza == null || zza.first == null) {
                                    zzgo().zzjd().zze("Extra parameter without existing main event. eventName, eventId", str3, l3);
                                } else {
                                    zzgf com_google_android_gms_internal_measurement_zzgf4 = (zzgf) zza.first;
                                    j = ((Long) zza.second).longValue();
                                    zzjo();
                                    l4 = (Long) zzfg.zzb(com_google_android_gms_internal_measurement_zzgf4, "_eid");
                                    com_google_android_gms_internal_measurement_zzgf = com_google_android_gms_internal_measurement_zzgf4;
                                }
                            } else {
                                l4 = l;
                            }
                            j--;
                            if (j <= 0) {
                                zzjq = zzjq();
                                zzjq.zzaf();
                                zzjq.zzgo().zzjl().zzg("Clearing complex main event info. appId", str);
                                try {
                                    zzjq.getWritableDatabase().execSQL("delete from main_event_params where app_id=?", new String[]{str});
                                } catch (SQLiteException e) {
                                    zzjq.zzgo().zzjd().zzg("Error clearing complex main event", e);
                                }
                            } else {
                                zzjq().zza(str, l3, j, com_google_android_gms_internal_measurement_zzgf);
                            }
                            zzgg[] com_google_android_gms_internal_measurement_zzggArr3 = new zzgg[(com_google_android_gms_internal_measurement_zzgf.zzawt.length + com_google_android_gms_internal_measurement_zzggArr2.length)];
                            i3 = 0;
                            zzgg[] com_google_android_gms_internal_measurement_zzggArr4 = com_google_android_gms_internal_measurement_zzgf.zzawt;
                            int length2 = com_google_android_gms_internal_measurement_zzggArr4.length;
                            i2 = 0;
                            while (i2 < length2) {
                                zzgg com_google_android_gms_internal_measurement_zzgg = com_google_android_gms_internal_measurement_zzggArr4[i2];
                                zzjo();
                                if (zzfg.zza(com_google_android_gms_internal_measurement_zzgf2, com_google_android_gms_internal_measurement_zzgg.name) == null) {
                                    i4 = i3 + 1;
                                    com_google_android_gms_internal_measurement_zzggArr3[i3] = com_google_android_gms_internal_measurement_zzgg;
                                } else {
                                    i4 = i3;
                                }
                                i2++;
                                i3 = i4;
                            }
                            if (i3 > 0) {
                                zzgg[] com_google_android_gms_internal_measurement_zzggArr5;
                                length = com_google_android_gms_internal_measurement_zzggArr2.length;
                                i4 = 0;
                                while (i4 < length) {
                                    i2 = i3 + 1;
                                    com_google_android_gms_internal_measurement_zzggArr3[i3] = com_google_android_gms_internal_measurement_zzggArr2[i4];
                                    i4++;
                                    i3 = i2;
                                }
                                if (i3 == com_google_android_gms_internal_measurement_zzggArr3.length) {
                                    com_google_android_gms_internal_measurement_zzggArr5 = com_google_android_gms_internal_measurement_zzggArr3;
                                } else {
                                    com_google_android_gms_internal_measurement_zzggArr5 = (zzgg[]) Arrays.copyOf(com_google_android_gms_internal_measurement_zzggArr3, i3);
                                }
                                com_google_android_gms_internal_measurement_zzggArr = com_google_android_gms_internal_measurement_zzggArr5;
                                str2 = str3;
                                l2 = l4;
                                j2 = j;
                                com_google_android_gms_internal_measurement_zzgf3 = com_google_android_gms_internal_measurement_zzgf;
                            } else {
                                zzgo().zzjg().zzg("No unique parameters in main event. eventName", str3);
                                com_google_android_gms_internal_measurement_zzggArr = com_google_android_gms_internal_measurement_zzggArr2;
                                str2 = str3;
                                l2 = l4;
                                j2 = j;
                                com_google_android_gms_internal_measurement_zzgf3 = com_google_android_gms_internal_measurement_zzgf;
                            }
                        }
                    } else if (obj2 != null) {
                        zzjo();
                        Long valueOf = Long.valueOf(0);
                        l = zzfg.zzb(com_google_android_gms_internal_measurement_zzgf2, "_epc");
                        if (l != null) {
                            valueOf = l;
                        }
                        j = valueOf.longValue();
                        if (j <= 0) {
                            zzgo().zzjg().zzg("Complex event with zero extra param count. eventName", str3);
                            com_google_android_gms_internal_measurement_zzggArr = com_google_android_gms_internal_measurement_zzggArr2;
                            str2 = str3;
                            l2 = l3;
                            j2 = j;
                            com_google_android_gms_internal_measurement_zzgf3 = com_google_android_gms_internal_measurement_zzgf2;
                        } else {
                            zzjq().zza(str, l3, j, com_google_android_gms_internal_measurement_zzgf2);
                            com_google_android_gms_internal_measurement_zzggArr = com_google_android_gms_internal_measurement_zzggArr2;
                            str2 = str3;
                            l2 = l3;
                            j2 = j;
                            com_google_android_gms_internal_measurement_zzgf3 = com_google_android_gms_internal_measurement_zzgf2;
                        }
                    }
                    zzg = zzjq().zzg(str, com_google_android_gms_internal_measurement_zzgf2.name);
                    if (zzg != null) {
                        zzgo().zzjg().zze("Event aggregate wasn't created during raw event logging. appId, event", zzap.zzbv(str), zzgl().zzbs(str2));
                        com_google_android_gms_measurement_internal_zzz = new zzz(str, com_google_android_gms_internal_measurement_zzgf2.name, 1, 1, com_google_android_gms_internal_measurement_zzgf2.zzawu.longValue(), 0, null, null, null, null);
                    } else {
                        com_google_android_gms_measurement_internal_zzz = zzg.zziu();
                    }
                    zzjq().zza(com_google_android_gms_measurement_internal_zzz);
                    j = com_google_android_gms_measurement_internal_zzz.zzaie;
                    map2 = (Map) arrayMap9.get(str2);
                    if (map2 != null) {
                        map2 = zzjq().zzl(str, str2);
                        if (map2 == null) {
                            map2 = new ArrayMap();
                        }
                        arrayMap9.put(str2, map2);
                        map3 = map2;
                    } else {
                        map3 = map2;
                    }
                    for (Integer intValue4 : r9.keySet()) {
                        intValue3 = intValue4.intValue();
                        if (hashSet.contains(Integer.valueOf(intValue3))) {
                            com_google_android_gms_internal_measurement_zzgd2 = (zzgd) arrayMap4.get(Integer.valueOf(intValue3));
                            bitSet = (BitSet) arrayMap5.get(Integer.valueOf(intValue3));
                            bitSet2 = (BitSet) arrayMap6.get(Integer.valueOf(intValue3));
                            arrayMap = null;
                            zzbo = null;
                            if (zzd) {
                                arrayMap = (Map) arrayMap7.get(Integer.valueOf(intValue3));
                                zzbo = (Map) arrayMap8.get(Integer.valueOf(intValue3));
                            }
                            if (com_google_android_gms_internal_measurement_zzgd2 != null) {
                                com_google_android_gms_internal_measurement_zzgd2 = new zzgd();
                                arrayMap4.put(Integer.valueOf(intValue3), com_google_android_gms_internal_measurement_zzgd2);
                                com_google_android_gms_internal_measurement_zzgd2.zzawo = Boolean.valueOf(true);
                                bitSet3 = new BitSet();
                                arrayMap5.put(Integer.valueOf(intValue3), bitSet3);
                                bitSet2 = new BitSet();
                                arrayMap6.put(Integer.valueOf(intValue3), bitSet2);
                                if (zzd) {
                                    map4 = arrayMap;
                                    bitSet4 = bitSet2;
                                } else {
                                    arrayMap2 = new ArrayMap();
                                    arrayMap7.put(Integer.valueOf(intValue3), arrayMap2);
                                    arrayMap3 = new ArrayMap();
                                    arrayMap8.put(Integer.valueOf(intValue3), arrayMap3);
                                    zzbo = arrayMap3;
                                    map4 = arrayMap2;
                                    bitSet4 = bitSet2;
                                }
                            } else {
                                map4 = arrayMap;
                                bitSet4 = bitSet2;
                                bitSet3 = bitSet;
                            }
                            for (zzfv com_google_android_gms_internal_measurement_zzfv : (List) r9.get(Integer.valueOf(intValue3))) {
                                if (zzgo().isLoggable(2)) {
                                    zzgo().zzjl().zzd("Evaluating filter. audience, filter, event", Integer.valueOf(intValue3), com_google_android_gms_internal_measurement_zzfv.zzave, zzgl().zzbs(com_google_android_gms_internal_measurement_zzfv.zzavf));
                                    zzgo().zzjl().zzg("Filter definition", zzjo().zza(com_google_android_gms_internal_measurement_zzfv));
                                }
                                if (com_google_android_gms_internal_measurement_zzfv.zzave != null || com_google_android_gms_internal_measurement_zzfv.zzave.intValue() > 256) {
                                    zzgo().zzjg().zze("Invalid event filter ID. appId, id", zzap.zzbv(str), String.valueOf(com_google_android_gms_internal_measurement_zzfv.zzave));
                                } else if (zzd) {
                                    Object obj4 = (com_google_android_gms_internal_measurement_zzfv == null || com_google_android_gms_internal_measurement_zzfv.zzavb == null || !com_google_android_gms_internal_measurement_zzfv.zzavb.booleanValue()) ? null : 1;
                                    Object obj5 = (com_google_android_gms_internal_measurement_zzfv == null || com_google_android_gms_internal_measurement_zzfv.zzavc == null || !com_google_android_gms_internal_measurement_zzfv.zzavc.booleanValue()) ? null : 1;
                                    if (bitSet3.get(com_google_android_gms_internal_measurement_zzfv.zzave.intValue()) && obj4 == null && obj5 == null) {
                                        zzgo().zzjl().zze("Event filter already evaluated true and it is not associated with a dynamic audience. audience ID, filter ID", Integer.valueOf(intValue3), com_google_android_gms_internal_measurement_zzfv.zzave);
                                    } else {
                                        r4 = zza(com_google_android_gms_internal_measurement_zzfv, str2, com_google_android_gms_internal_measurement_zzggArr, j);
                                        r5 = zzgo().zzjl();
                                        String str4 = "Event filter result";
                                        if (r4 == null) {
                                            obj3 = "null";
                                        } else {
                                            r2 = r4;
                                        }
                                        r5.zzg(str4, obj3);
                                        if (r4 == null) {
                                            hashSet.add(Integer.valueOf(intValue3));
                                        } else {
                                            bitSet4.set(com_google_android_gms_internal_measurement_zzfv.zzave.intValue());
                                            if (r4.booleanValue()) {
                                                bitSet3.set(com_google_android_gms_internal_measurement_zzfv.zzave.intValue());
                                                if (!((obj4 == null && obj5 == null) || com_google_android_gms_internal_measurement_zzgf2.zzawu == null)) {
                                                    if (obj5 != null) {
                                                        zzb(zzbo, com_google_android_gms_internal_measurement_zzfv.zzave.intValue(), com_google_android_gms_internal_measurement_zzgf2.zzawu.longValue());
                                                    } else {
                                                        zza(map4, com_google_android_gms_internal_measurement_zzfv.zzave.intValue(), com_google_android_gms_internal_measurement_zzgf2.zzawu.longValue());
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } else if (bitSet3.get(com_google_android_gms_internal_measurement_zzfv.zzave.intValue())) {
                                    zzgo().zzjl().zze("Event filter already evaluated true. audience ID, filter ID", Integer.valueOf(intValue3), com_google_android_gms_internal_measurement_zzfv.zzave);
                                } else {
                                    r4 = zza(com_google_android_gms_internal_measurement_zzfv, str2, com_google_android_gms_internal_measurement_zzggArr, j);
                                    r5 = zzgo().zzjl();
                                    String str5 = "Event filter result";
                                    if (r4 == null) {
                                        obj3 = "null";
                                    } else {
                                        r2 = r4;
                                    }
                                    r5.zzg(str5, obj3);
                                    if (r4 == null) {
                                        hashSet.add(Integer.valueOf(intValue3));
                                    } else {
                                        bitSet4.set(com_google_android_gms_internal_measurement_zzfv.zzave.intValue());
                                        if (r4.booleanValue()) {
                                            bitSet3.set(com_google_android_gms_internal_measurement_zzfv.zzave.intValue());
                                        }
                                    }
                                }
                            }
                        } else {
                            zzgo().zzjl().zzg("Skipping failed audience ID", Integer.valueOf(intValue3));
                        }
                    }
                    l = l2;
                    j = j2;
                    com_google_android_gms_internal_measurement_zzgf = com_google_android_gms_internal_measurement_zzgf3;
                }
                com_google_android_gms_internal_measurement_zzggArr = com_google_android_gms_internal_measurement_zzggArr2;
                str2 = str3;
                l2 = l;
                j2 = j;
                com_google_android_gms_internal_measurement_zzgf3 = com_google_android_gms_internal_measurement_zzgf;
                zzg = zzjq().zzg(str, com_google_android_gms_internal_measurement_zzgf2.name);
                if (zzg != null) {
                    com_google_android_gms_measurement_internal_zzz = zzg.zziu();
                } else {
                    zzgo().zzjg().zze("Event aggregate wasn't created during raw event logging. appId, event", zzap.zzbv(str), zzgl().zzbs(str2));
                    com_google_android_gms_measurement_internal_zzz = new zzz(str, com_google_android_gms_internal_measurement_zzgf2.name, 1, 1, com_google_android_gms_internal_measurement_zzgf2.zzawu.longValue(), 0, null, null, null, null);
                }
                zzjq().zza(com_google_android_gms_measurement_internal_zzz);
                j = com_google_android_gms_measurement_internal_zzz.zzaie;
                map2 = (Map) arrayMap9.get(str2);
                if (map2 != null) {
                    map3 = map2;
                } else {
                    map2 = zzjq().zzl(str, str2);
                    if (map2 == null) {
                        map2 = new ArrayMap();
                    }
                    arrayMap9.put(str2, map2);
                    map3 = map2;
                }
                while (r15.hasNext()) {
                    intValue3 = intValue4.intValue();
                    if (hashSet.contains(Integer.valueOf(intValue3))) {
                        com_google_android_gms_internal_measurement_zzgd2 = (zzgd) arrayMap4.get(Integer.valueOf(intValue3));
                        bitSet = (BitSet) arrayMap5.get(Integer.valueOf(intValue3));
                        bitSet2 = (BitSet) arrayMap6.get(Integer.valueOf(intValue3));
                        arrayMap = null;
                        zzbo = null;
                        if (zzd) {
                            arrayMap = (Map) arrayMap7.get(Integer.valueOf(intValue3));
                            zzbo = (Map) arrayMap8.get(Integer.valueOf(intValue3));
                        }
                        if (com_google_android_gms_internal_measurement_zzgd2 != null) {
                            map4 = arrayMap;
                            bitSet4 = bitSet2;
                            bitSet3 = bitSet;
                        } else {
                            com_google_android_gms_internal_measurement_zzgd2 = new zzgd();
                            arrayMap4.put(Integer.valueOf(intValue3), com_google_android_gms_internal_measurement_zzgd2);
                            com_google_android_gms_internal_measurement_zzgd2.zzawo = Boolean.valueOf(true);
                            bitSet3 = new BitSet();
                            arrayMap5.put(Integer.valueOf(intValue3), bitSet3);
                            bitSet2 = new BitSet();
                            arrayMap6.put(Integer.valueOf(intValue3), bitSet2);
                            if (zzd) {
                                map4 = arrayMap;
                                bitSet4 = bitSet2;
                            } else {
                                arrayMap2 = new ArrayMap();
                                arrayMap7.put(Integer.valueOf(intValue3), arrayMap2);
                                arrayMap3 = new ArrayMap();
                                arrayMap8.put(Integer.valueOf(intValue3), arrayMap3);
                                zzbo = arrayMap3;
                                map4 = arrayMap2;
                                bitSet4 = bitSet2;
                            }
                        }
                        for (zzfv com_google_android_gms_internal_measurement_zzfv2 : (List) r9.get(Integer.valueOf(intValue3))) {
                            if (zzgo().isLoggable(2)) {
                                zzgo().zzjl().zzd("Evaluating filter. audience, filter, event", Integer.valueOf(intValue3), com_google_android_gms_internal_measurement_zzfv2.zzave, zzgl().zzbs(com_google_android_gms_internal_measurement_zzfv2.zzavf));
                                zzgo().zzjl().zzg("Filter definition", zzjo().zza(com_google_android_gms_internal_measurement_zzfv2));
                            }
                            if (com_google_android_gms_internal_measurement_zzfv2.zzave != null) {
                            }
                            zzgo().zzjg().zze("Invalid event filter ID. appId, id", zzap.zzbv(str), String.valueOf(com_google_android_gms_internal_measurement_zzfv2.zzave));
                        }
                    } else {
                        zzgo().zzjl().zzg("Skipping failed audience ID", Integer.valueOf(intValue3));
                    }
                }
                l = l2;
                j = j2;
                com_google_android_gms_internal_measurement_zzgf = com_google_android_gms_internal_measurement_zzgf3;
            }
        }
        if (com_google_android_gms_internal_measurement_zzglArr != null) {
            Map arrayMap10 = new ArrayMap();
            for (zzgl com_google_android_gms_internal_measurement_zzgl : com_google_android_gms_internal_measurement_zzglArr) {
                map2 = (Map) arrayMap10.get(com_google_android_gms_internal_measurement_zzgl.name);
                if (map2 == null) {
                    map2 = zzjq().zzm(str, com_google_android_gms_internal_measurement_zzgl.name);
                    if (map2 == null) {
                        map2 = new ArrayMap();
                    }
                    arrayMap10.put(com_google_android_gms_internal_measurement_zzgl.name, map2);
                    map = map2;
                } else {
                    map = map2;
                }
                for (Integer intValue42 : r7.keySet()) {
                    int intValue5 = intValue42.intValue();
                    if (hashSet.contains(Integer.valueOf(intValue5))) {
                        zzgo().zzjl().zzg("Skipping failed audience ID", Integer.valueOf(intValue5));
                    } else {
                        BitSet bitSet5;
                        com_google_android_gms_internal_measurement_zzgd2 = (zzgd) arrayMap4.get(Integer.valueOf(intValue5));
                        bitSet = (BitSet) arrayMap5.get(Integer.valueOf(intValue5));
                        bitSet2 = (BitSet) arrayMap6.get(Integer.valueOf(intValue5));
                        zzbo = null;
                        Map map5 = null;
                        if (zzd) {
                            map5 = (Map) arrayMap8.get(Integer.valueOf(intValue5));
                            zzbo = (Map) arrayMap7.get(Integer.valueOf(intValue5));
                        }
                        if (com_google_android_gms_internal_measurement_zzgd2 == null) {
                            com_google_android_gms_internal_measurement_zzgd2 = new zzgd();
                            arrayMap4.put(Integer.valueOf(intValue5), com_google_android_gms_internal_measurement_zzgd2);
                            com_google_android_gms_internal_measurement_zzgd2.zzawo = Boolean.valueOf(true);
                            bitSet5 = new BitSet();
                            arrayMap5.put(Integer.valueOf(intValue5), bitSet5);
                            bitSet2 = new BitSet();
                            arrayMap6.put(Integer.valueOf(intValue5), bitSet2);
                            if (zzd) {
                                arrayMap2 = new ArrayMap();
                                arrayMap7.put(Integer.valueOf(intValue5), arrayMap2);
                                arrayMap3 = new ArrayMap();
                                arrayMap8.put(Integer.valueOf(intValue5), arrayMap3);
                                arrayMap = arrayMap3;
                                zzbo = arrayMap2;
                            } else {
                                arrayMap = map5;
                            }
                        } else {
                            arrayMap = map5;
                            bitSet5 = bitSet;
                        }
                        for (zzfy com_google_android_gms_internal_measurement_zzfy : (List) r7.get(Integer.valueOf(intValue5))) {
                            if (zzgo().isLoggable(2)) {
                                zzgo().zzjl().zzd("Evaluating filter. audience, filter, property", Integer.valueOf(intValue5), com_google_android_gms_internal_measurement_zzfy.zzave, zzgl().zzbu(com_google_android_gms_internal_measurement_zzfy.zzavu));
                                zzgo().zzjl().zzg("Filter definition", zzjo().zza(com_google_android_gms_internal_measurement_zzfy));
                            }
                            if (com_google_android_gms_internal_measurement_zzfy.zzave == null || com_google_android_gms_internal_measurement_zzfy.zzave.intValue() > 256) {
                                zzgo().zzjg().zze("Invalid property filter ID. appId, id", zzap.zzbv(str), String.valueOf(com_google_android_gms_internal_measurement_zzfy.zzave));
                                hashSet.add(Integer.valueOf(intValue5));
                                break;
                            } else if (zzd) {
                                Object obj6 = (com_google_android_gms_internal_measurement_zzfy == null || com_google_android_gms_internal_measurement_zzfy.zzavb == null || !com_google_android_gms_internal_measurement_zzfy.zzavb.booleanValue()) ? null : 1;
                                r3 = (com_google_android_gms_internal_measurement_zzfy == null || com_google_android_gms_internal_measurement_zzfy.zzavc == null || !com_google_android_gms_internal_measurement_zzfy.zzavc.booleanValue()) ? null : 1;
                                if (bitSet5.get(com_google_android_gms_internal_measurement_zzfy.zzave.intValue()) && obj6 == null && r3 == null) {
                                    zzgo().zzjl().zze("Property filter already evaluated true and it is not associated with a dynamic audience. audience ID, filter ID", Integer.valueOf(intValue5), com_google_android_gms_internal_measurement_zzfy.zzave);
                                } else {
                                    Boolean zza2 = zza(com_google_android_gms_internal_measurement_zzfy, com_google_android_gms_internal_measurement_zzgl);
                                    zzar zzjl = zzgo().zzjl();
                                    String str6 = "Property filter result";
                                    if (zza2 == null) {
                                        obj = "null";
                                    } else {
                                        r6 = zza2;
                                    }
                                    zzjl.zzg(str6, obj);
                                    if (zza2 == null) {
                                        hashSet.add(Integer.valueOf(intValue5));
                                    } else {
                                        bitSet2.set(com_google_android_gms_internal_measurement_zzfy.zzave.intValue());
                                        bitSet5.set(com_google_android_gms_internal_measurement_zzfy.zzave.intValue(), zza2.booleanValue());
                                        if (zza2.booleanValue() && !((obj6 == null && r3 == null) || com_google_android_gms_internal_measurement_zzgl.zzayl == null)) {
                                            if (r3 != null) {
                                                zzb(arrayMap, com_google_android_gms_internal_measurement_zzfy.zzave.intValue(), com_google_android_gms_internal_measurement_zzgl.zzayl.longValue());
                                            } else {
                                                zza(zzbo, com_google_android_gms_internal_measurement_zzfy.zzave.intValue(), com_google_android_gms_internal_measurement_zzgl.zzayl.longValue());
                                            }
                                        }
                                    }
                                }
                            } else if (bitSet5.get(com_google_android_gms_internal_measurement_zzfy.zzave.intValue())) {
                                zzgo().zzjl().zze("Property filter already evaluated true. audience ID, filter ID", Integer.valueOf(intValue5), com_google_android_gms_internal_measurement_zzfy.zzave);
                            } else {
                                r6 = zza(com_google_android_gms_internal_measurement_zzfy, com_google_android_gms_internal_measurement_zzgl);
                                zzar zzjl2 = zzgo().zzjl();
                                String str7 = "Property filter result";
                                if (r6 == null) {
                                    r3 = "null";
                                } else {
                                    Boolean bool = r6;
                                }
                                zzjl2.zzg(str7, r3);
                                if (r6 == null) {
                                    hashSet.add(Integer.valueOf(intValue5));
                                } else {
                                    bitSet2.set(com_google_android_gms_internal_measurement_zzfy.zzave.intValue());
                                    if (r6.booleanValue()) {
                                        bitSet5.set(com_google_android_gms_internal_measurement_zzfy.zzave.intValue());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        zzgd[] com_google_android_gms_internal_measurement_zzgdArr = new zzgd[arrayMap5.size()];
        i3 = 0;
        for (Integer intValue422 : arrayMap5.keySet()) {
            length = intValue422.intValue();
            if (!hashSet.contains(Integer.valueOf(length))) {
                com_google_android_gms_internal_measurement_zzgd2 = (zzgd) arrayMap4.get(Integer.valueOf(length));
                if (com_google_android_gms_internal_measurement_zzgd2 == null) {
                    com_google_android_gms_internal_measurement_zzgd = new zzgd();
                } else {
                    com_google_android_gms_internal_measurement_zzgd = com_google_android_gms_internal_measurement_zzgd2;
                }
                int i5 = i3 + 1;
                com_google_android_gms_internal_measurement_zzgdArr[i3] = com_google_android_gms_internal_measurement_zzgd;
                com_google_android_gms_internal_measurement_zzgd.zzauy = Integer.valueOf(length);
                com_google_android_gms_internal_measurement_zzgd.zzawm = new zzgj();
                com_google_android_gms_internal_measurement_zzgd.zzawm.zzayf = zzfg.zza((BitSet) arrayMap5.get(Integer.valueOf(length)));
                com_google_android_gms_internal_measurement_zzgd.zzawm.zzaye = zzfg.zza((BitSet) arrayMap6.get(Integer.valueOf(length)));
                if (zzd) {
                    zzgk[] com_google_android_gms_internal_measurement_zzgkArr;
                    com_google_android_gms_internal_measurement_zzgd.zzawm.zzayg = zzd((Map) arrayMap7.get(Integer.valueOf(length)));
                    zzgj com_google_android_gms_internal_measurement_zzgj2 = com_google_android_gms_internal_measurement_zzgd.zzawm;
                    map2 = (Map) arrayMap8.get(Integer.valueOf(length));
                    if (map2 == null) {
                        com_google_android_gms_internal_measurement_zzgkArr = new zzgk[0];
                    } else {
                        zzgk[] com_google_android_gms_internal_measurement_zzgkArr2 = new zzgk[map2.size()];
                        i = 0;
                        for (Integer num : map2.keySet()) {
                            zzgk com_google_android_gms_internal_measurement_zzgk = new zzgk();
                            com_google_android_gms_internal_measurement_zzgk.zzawq = num;
                            List<Long> list = (List) map2.get(num);
                            if (list != null) {
                                Collections.sort(list);
                                long[] jArr = new long[list.size()];
                                int i6 = 0;
                                for (Long l5 : list) {
                                    int i7 = i6 + 1;
                                    jArr[i6] = l5.longValue();
                                    i6 = i7;
                                }
                                com_google_android_gms_internal_measurement_zzgk.zzayj = jArr;
                            }
                            i3 = i + 1;
                            com_google_android_gms_internal_measurement_zzgkArr2[i] = com_google_android_gms_internal_measurement_zzgk;
                            i = i3;
                        }
                        com_google_android_gms_internal_measurement_zzgkArr = com_google_android_gms_internal_measurement_zzgkArr2;
                    }
                    com_google_android_gms_internal_measurement_zzgj2.zzayh = com_google_android_gms_internal_measurement_zzgkArr;
                }
                zzjq = zzjq();
                zzzg com_google_android_gms_internal_measurement_zzzg = com_google_android_gms_internal_measurement_zzgd.zzawm;
                zzjq.zzcl();
                zzjq.zzaf();
                Preconditions.checkNotEmpty(str);
                Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzzg);
                try {
                    byte[] bArr = new byte[com_google_android_gms_internal_measurement_zzzg.zzvu()];
                    zzyy zzk = zzyy.zzk(bArr, 0, bArr.length);
                    com_google_android_gms_internal_measurement_zzzg.zza(zzk);
                    zzk.zzyt();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("app_id", str);
                    contentValues.put("audience_id", Integer.valueOf(length));
                    contentValues.put("current_results", bArr);
                    try {
                        if (zzjq.getWritableDatabase().insertWithOnConflict("audience_filter_values", null, contentValues, 5) == -1) {
                            zzjq.zzgo().zzjd().zzg("Failed to insert filter results (got -1). appId", zzap.zzbv(str));
                        }
                        i3 = i5;
                    } catch (SQLiteException e2) {
                        zzjq.zzgo().zzjd().zze("Error storing filter results. appId", zzap.zzbv(str), e2);
                        i3 = i5;
                    }
                } catch (IOException e3) {
                    zzjq.zzgo().zzjd().zze("Configuration loss. Failed to serialize filter results. appId", zzap.zzbv(str), e3);
                    i3 = i5;
                }
            }
        }
        return (zzgd[]) Arrays.copyOf(com_google_android_gms_internal_measurement_zzgdArr, i3);
    }

    private final Boolean zza(zzfv com_google_android_gms_internal_measurement_zzfv, String str, zzgg[] com_google_android_gms_internal_measurement_zzggArr, long j) {
        Boolean zza;
        if (com_google_android_gms_internal_measurement_zzfv.zzavi != null) {
            zza = zza(j, com_google_android_gms_internal_measurement_zzfv.zzavi);
            if (zza == null) {
                return null;
            }
            if (!zza.booleanValue()) {
                return Boolean.valueOf(false);
            }
        }
        Set hashSet = new HashSet();
        for (zzfw com_google_android_gms_internal_measurement_zzfw : com_google_android_gms_internal_measurement_zzfv.zzavg) {
            if (TextUtils.isEmpty(com_google_android_gms_internal_measurement_zzfw.zzavn)) {
                zzgo().zzjg().zzg("null or empty param name in filter. event", zzgl().zzbs(str));
                return null;
            }
            hashSet.add(com_google_android_gms_internal_measurement_zzfw.zzavn);
        }
        Map arrayMap = new ArrayMap();
        for (zzgg com_google_android_gms_internal_measurement_zzgg : com_google_android_gms_internal_measurement_zzggArr) {
            if (hashSet.contains(com_google_android_gms_internal_measurement_zzgg.name)) {
                if (com_google_android_gms_internal_measurement_zzgg.zzawx != null) {
                    arrayMap.put(com_google_android_gms_internal_measurement_zzgg.name, com_google_android_gms_internal_measurement_zzgg.zzawx);
                } else if (com_google_android_gms_internal_measurement_zzgg.zzauh != null) {
                    arrayMap.put(com_google_android_gms_internal_measurement_zzgg.name, com_google_android_gms_internal_measurement_zzgg.zzauh);
                } else if (com_google_android_gms_internal_measurement_zzgg.zzamp != null) {
                    arrayMap.put(com_google_android_gms_internal_measurement_zzgg.name, com_google_android_gms_internal_measurement_zzgg.zzamp);
                } else {
                    zzgo().zzjg().zze("Unknown value for param. event, param", zzgl().zzbs(str), zzgl().zzbt(com_google_android_gms_internal_measurement_zzgg.name));
                    return null;
                }
            }
        }
        for (zzfw com_google_android_gms_internal_measurement_zzfw2 : com_google_android_gms_internal_measurement_zzfv.zzavg) {
            boolean equals = Boolean.TRUE.equals(com_google_android_gms_internal_measurement_zzfw2.zzavm);
            String str2 = com_google_android_gms_internal_measurement_zzfw2.zzavn;
            if (TextUtils.isEmpty(str2)) {
                zzgo().zzjg().zzg("Event has empty param name. event", zzgl().zzbs(str));
                return null;
            }
            Object obj = arrayMap.get(str2);
            if (obj instanceof Long) {
                if (com_google_android_gms_internal_measurement_zzfw2.zzavl == null) {
                    zzgo().zzjg().zze("No number filter for long param. event, param", zzgl().zzbs(str), zzgl().zzbt(str2));
                    return null;
                }
                zza = zza(((Long) obj).longValue(), com_google_android_gms_internal_measurement_zzfw2.zzavl);
                if (zza == null) {
                    return null;
                }
                if (((!zza.booleanValue() ? 1 : 0) ^ equals) != 0) {
                    return Boolean.valueOf(false);
                }
            } else if (obj instanceof Double) {
                if (com_google_android_gms_internal_measurement_zzfw2.zzavl == null) {
                    zzgo().zzjg().zze("No number filter for double param. event, param", zzgl().zzbs(str), zzgl().zzbt(str2));
                    return null;
                }
                zza = zza(((Double) obj).doubleValue(), com_google_android_gms_internal_measurement_zzfw2.zzavl);
                if (zza == null) {
                    return null;
                }
                if (((!zza.booleanValue() ? 1 : 0) ^ equals) != 0) {
                    return Boolean.valueOf(false);
                }
            } else if (obj instanceof String) {
                if (com_google_android_gms_internal_measurement_zzfw2.zzavk != null) {
                    zza = zza((String) obj, com_google_android_gms_internal_measurement_zzfw2.zzavk);
                } else if (com_google_android_gms_internal_measurement_zzfw2.zzavl == null) {
                    zzgo().zzjg().zze("No filter for String param. event, param", zzgl().zzbs(str), zzgl().zzbt(str2));
                    return null;
                } else if (zzfg.zzcp((String) obj)) {
                    zza = zza((String) obj, com_google_android_gms_internal_measurement_zzfw2.zzavl);
                } else {
                    zzgo().zzjg().zze("Invalid param value for number filter. event, param", zzgl().zzbs(str), zzgl().zzbt(str2));
                    return null;
                }
                if (zza == null) {
                    return null;
                }
                if (((!zza.booleanValue() ? 1 : 0) ^ equals) != 0) {
                    return Boolean.valueOf(false);
                }
            } else if (obj == null) {
                zzgo().zzjl().zze("Missing param for filter. event, param", zzgl().zzbs(str), zzgl().zzbt(str2));
                return Boolean.valueOf(false);
            } else {
                zzgo().zzjg().zze("Unknown param type. event, param", zzgl().zzbs(str), zzgl().zzbt(str2));
                return null;
            }
        }
        return Boolean.valueOf(true);
    }

    private final Boolean zza(zzfy com_google_android_gms_internal_measurement_zzfy, zzgl com_google_android_gms_internal_measurement_zzgl) {
        zzfw com_google_android_gms_internal_measurement_zzfw = com_google_android_gms_internal_measurement_zzfy.zzavv;
        if (com_google_android_gms_internal_measurement_zzfw == null) {
            zzgo().zzjg().zzg("Missing property filter. property", zzgl().zzbu(com_google_android_gms_internal_measurement_zzgl.name));
            return null;
        }
        boolean equals = Boolean.TRUE.equals(com_google_android_gms_internal_measurement_zzfw.zzavm);
        if (com_google_android_gms_internal_measurement_zzgl.zzawx != null) {
            if (com_google_android_gms_internal_measurement_zzfw.zzavl != null) {
                return zza(zza(com_google_android_gms_internal_measurement_zzgl.zzawx.longValue(), com_google_android_gms_internal_measurement_zzfw.zzavl), equals);
            }
            zzgo().zzjg().zzg("No number filter for long property. property", zzgl().zzbu(com_google_android_gms_internal_measurement_zzgl.name));
            return null;
        } else if (com_google_android_gms_internal_measurement_zzgl.zzauh != null) {
            if (com_google_android_gms_internal_measurement_zzfw.zzavl != null) {
                return zza(zza(com_google_android_gms_internal_measurement_zzgl.zzauh.doubleValue(), com_google_android_gms_internal_measurement_zzfw.zzavl), equals);
            }
            zzgo().zzjg().zzg("No number filter for double property. property", zzgl().zzbu(com_google_android_gms_internal_measurement_zzgl.name));
            return null;
        } else if (com_google_android_gms_internal_measurement_zzgl.zzamp == null) {
            zzgo().zzjg().zzg("User property has no value, property", zzgl().zzbu(com_google_android_gms_internal_measurement_zzgl.name));
            return null;
        } else if (com_google_android_gms_internal_measurement_zzfw.zzavk != null) {
            return zza(zza(com_google_android_gms_internal_measurement_zzgl.zzamp, com_google_android_gms_internal_measurement_zzfw.zzavk), equals);
        } else {
            if (com_google_android_gms_internal_measurement_zzfw.zzavl == null) {
                zzgo().zzjg().zzg("No string or number filter defined. property", zzgl().zzbu(com_google_android_gms_internal_measurement_zzgl.name));
                return null;
            } else if (zzfg.zzcp(com_google_android_gms_internal_measurement_zzgl.zzamp)) {
                return zza(zza(com_google_android_gms_internal_measurement_zzgl.zzamp, com_google_android_gms_internal_measurement_zzfw.zzavl), equals);
            } else {
                zzgo().zzjg().zze("Invalid user property value for Numeric number filter. property, value", zzgl().zzbu(com_google_android_gms_internal_measurement_zzgl.name), com_google_android_gms_internal_measurement_zzgl.zzamp);
                return null;
            }
        }
    }

    private static Boolean zza(Boolean bool, boolean z) {
        if (bool == null) {
            return null;
        }
        return Boolean.valueOf(bool.booleanValue() ^ z);
    }

    private final Boolean zza(String str, zzfz com_google_android_gms_internal_measurement_zzfz) {
        int i = 0;
        String str2 = null;
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzfz);
        if (str == null || com_google_android_gms_internal_measurement_zzfz.zzavw == null || com_google_android_gms_internal_measurement_zzfz.zzavw.intValue() == 0) {
            return null;
        }
        boolean z;
        String str3;
        List list;
        if (com_google_android_gms_internal_measurement_zzfz.zzavw.intValue() == 6) {
            if (com_google_android_gms_internal_measurement_zzfz.zzavz == null || com_google_android_gms_internal_measurement_zzfz.zzavz.length == 0) {
                return null;
            }
        } else if (com_google_android_gms_internal_measurement_zzfz.zzavx == null) {
            return null;
        }
        int intValue = com_google_android_gms_internal_measurement_zzfz.zzavw.intValue();
        if (com_google_android_gms_internal_measurement_zzfz.zzavy == null || !com_google_android_gms_internal_measurement_zzfz.zzavy.booleanValue()) {
            z = false;
        } else {
            z = true;
        }
        if (z || intValue == 1 || intValue == 6) {
            str3 = com_google_android_gms_internal_measurement_zzfz.zzavx;
        } else {
            str3 = com_google_android_gms_internal_measurement_zzfz.zzavx.toUpperCase(Locale.ENGLISH);
        }
        if (com_google_android_gms_internal_measurement_zzfz.zzavz == null) {
            list = null;
        } else {
            String[] strArr = com_google_android_gms_internal_measurement_zzfz.zzavz;
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
            str2 = str3;
        }
        return zza(str, intValue, z, str3, list, str2);
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
                    zzgo().zzjg().zzg("Invalid regular expression in REGEXP audience filter. expression", str3);
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

    private final Boolean zza(long j, zzfx com_google_android_gms_internal_measurement_zzfx) {
        try {
            return zza(new BigDecimal(j), com_google_android_gms_internal_measurement_zzfx, 0.0d);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private final Boolean zza(double d, zzfx com_google_android_gms_internal_measurement_zzfx) {
        try {
            return zza(new BigDecimal(d), com_google_android_gms_internal_measurement_zzfx, Math.ulp(d));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private final Boolean zza(String str, zzfx com_google_android_gms_internal_measurement_zzfx) {
        Boolean bool = null;
        if (zzfg.zzcp(str)) {
            try {
                bool = zza(new BigDecimal(str), com_google_android_gms_internal_measurement_zzfx, 0.0d);
            } catch (NumberFormatException e) {
            }
        }
        return bool;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.Boolean zza(java.math.BigDecimal r10, com.google.android.gms.internal.measurement.zzfx r11, double r12) {
        /*
        r8 = 4;
        r7 = -1;
        r1 = 0;
        r0 = 1;
        r2 = 0;
        com.google.android.gms.common.internal.Preconditions.checkNotNull(r11);
        r3 = r11.zzavo;
        if (r3 == 0) goto L_0x0014;
    L_0x000c:
        r3 = r11.zzavo;
        r3 = r3.intValue();
        if (r3 != 0) goto L_0x0016;
    L_0x0014:
        r0 = r2;
    L_0x0015:
        return r0;
    L_0x0016:
        r3 = r11.zzavo;
        r3 = r3.intValue();
        if (r3 != r8) goto L_0x0028;
    L_0x001e:
        r3 = r11.zzavr;
        if (r3 == 0) goto L_0x0026;
    L_0x0022:
        r3 = r11.zzavs;
        if (r3 != 0) goto L_0x002e;
    L_0x0026:
        r0 = r2;
        goto L_0x0015;
    L_0x0028:
        r3 = r11.zzavq;
        if (r3 != 0) goto L_0x002e;
    L_0x002c:
        r0 = r2;
        goto L_0x0015;
    L_0x002e:
        r3 = r11.zzavo;
        r6 = r3.intValue();
        r3 = r11.zzavo;
        r3 = r3.intValue();
        if (r3 != r8) goto L_0x0066;
    L_0x003c:
        r3 = r11.zzavr;
        r3 = com.google.android.gms.measurement.internal.zzfg.zzcp(r3);
        if (r3 == 0) goto L_0x004c;
    L_0x0044:
        r3 = r11.zzavs;
        r3 = com.google.android.gms.measurement.internal.zzfg.zzcp(r3);
        if (r3 != 0) goto L_0x004e;
    L_0x004c:
        r0 = r2;
        goto L_0x0015;
    L_0x004e:
        r4 = new java.math.BigDecimal;	 Catch:{ NumberFormatException -> 0x0063 }
        r3 = r11.zzavr;	 Catch:{ NumberFormatException -> 0x0063 }
        r4.<init>(r3);	 Catch:{ NumberFormatException -> 0x0063 }
        r3 = new java.math.BigDecimal;	 Catch:{ NumberFormatException -> 0x0063 }
        r5 = r11.zzavs;	 Catch:{ NumberFormatException -> 0x0063 }
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
        r3 = r11.zzavq;
        r3 = com.google.android.gms.measurement.internal.zzfg.zzcp(r3);
        if (r3 != 0) goto L_0x0070;
    L_0x006e:
        r0 = r2;
        goto L_0x0015;
    L_0x0070:
        r5 = new java.math.BigDecimal;	 Catch:{ NumberFormatException -> 0x007a }
        r3 = r11.zzavq;	 Catch:{ NumberFormatException -> 0x007a }
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
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.zzj.zza(java.math.BigDecimal, com.google.android.gms.internal.measurement.zzfx, double):java.lang.Boolean");
    }

    private static zzge[] zzd(Map<Integer, Long> map) {
        if (map == null) {
            return null;
        }
        zzge[] com_google_android_gms_internal_measurement_zzgeArr = new zzge[map.size()];
        int i = 0;
        for (Integer num : map.keySet()) {
            zzge com_google_android_gms_internal_measurement_zzge = new zzge();
            com_google_android_gms_internal_measurement_zzge.zzawq = num;
            com_google_android_gms_internal_measurement_zzge.zzawr = (Long) map.get(num);
            int i2 = i + 1;
            com_google_android_gms_internal_measurement_zzgeArr[i] = com_google_android_gms_internal_measurement_zzge;
            i = i2;
        }
        return com_google_android_gms_internal_measurement_zzgeArr;
    }

    private static void zza(Map<Integer, Long> map, int i, long j) {
        Long l = (Long) map.get(Integer.valueOf(i));
        long j2 = j / 1000;
        if (l == null || j2 > l.longValue()) {
            map.put(Integer.valueOf(i), Long.valueOf(j2));
        }
    }

    private static void zzb(Map<Integer, List<Long>> map, int i, long j) {
        List list = (List) map.get(Integer.valueOf(i));
        if (list == null) {
            list = new ArrayList();
            map.put(Integer.valueOf(i), list);
        }
        list.add(Long.valueOf(j / 1000));
    }
}
