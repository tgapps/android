package com.google.android.gms.internal.measurement;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

public class zzsg {
    private static final Uri CONTENT_URI = Uri.parse("content://com.google.android.gsf.gservices");
    private static final Uri zzbqd = Uri.parse("content://com.google.android.gsf.gservices/prefix");
    public static final Pattern zzbqe = Pattern.compile("^(1|true|t|on|yes|y)$", 2);
    public static final Pattern zzbqf = Pattern.compile("^(0|false|f|off|no|n)$", 2);
    private static final AtomicBoolean zzbqg = new AtomicBoolean();
    private static HashMap<String, String> zzbqh;
    private static final HashMap<String, Boolean> zzbqi = new HashMap();
    private static final HashMap<String, Integer> zzbqj = new HashMap();
    private static final HashMap<String, Long> zzbqk = new HashMap();
    private static final HashMap<String, Float> zzbql = new HashMap();
    private static Object zzbqm;
    private static boolean zzbqn;
    private static String[] zzbqo = new String[0];

    private static void zza(ContentResolver contentResolver) {
        if (zzbqh == null) {
            zzbqg.set(false);
            zzbqh = new HashMap();
            zzbqm = new Object();
            zzbqn = false;
            contentResolver.registerContentObserver(CONTENT_URI, true, new zzsh(null));
        } else if (zzbqg.getAndSet(false)) {
            zzbqh.clear();
            zzbqi.clear();
            zzbqj.clear();
            zzbqk.clear();
            zzbql.clear();
            zzbqm = new Object();
            zzbqn = false;
        }
    }

    public static String zza(ContentResolver contentResolver, String str, String str2) {
        String str3 = null;
        synchronized (zzsg.class) {
            zza(contentResolver);
            Object obj = zzbqm;
            String str4;
            if (zzbqh.containsKey(str)) {
                str4 = (String) zzbqh.get(str);
                if (str4 != null) {
                    str3 = str4;
                }
            } else {
                String[] strArr = zzbqo;
                int length = strArr.length;
                int i = 0;
                while (i < length) {
                    if (str.startsWith(strArr[i])) {
                        if (!zzbqn || zzbqh.isEmpty()) {
                            zzbqh.putAll(zza(contentResolver, zzbqo));
                            zzbqn = true;
                            if (zzbqh.containsKey(str)) {
                                str4 = (String) zzbqh.get(str);
                                if (str4 != null) {
                                    str3 = str4;
                                }
                            }
                        }
                    } else {
                        i++;
                    }
                }
                Cursor query = contentResolver.query(CONTENT_URI, null, null, new String[]{str}, null);
                if (query != null) {
                    try {
                        if (query.moveToFirst()) {
                            str4 = query.getString(1);
                            if (str4 != null && str4.equals(null)) {
                                str4 = null;
                            }
                            zza(obj, str, str4);
                            if (str4 != null) {
                                str3 = str4;
                            }
                            if (query != null) {
                                query.close();
                            }
                        } else {
                            zza(obj, str, null);
                            if (query != null) {
                                query.close();
                            }
                        }
                    } catch (Throwable th) {
                        if (query != null) {
                            query.close();
                        }
                    }
                } else if (query != null) {
                    query.close();
                }
            }
        }
        return str3;
    }

    private static void zza(Object obj, String str, String str2) {
        synchronized (zzsg.class) {
            if (obj == zzbqm) {
                zzbqh.put(str, str2);
            }
        }
    }

    public static boolean zza(ContentResolver contentResolver, String str, boolean z) {
        Object zzb = zzb(contentResolver);
        Object obj = (Boolean) zza(zzbqi, str, Boolean.valueOf(z));
        if (obj != null) {
            return obj.booleanValue();
        }
        Object zza = zza(contentResolver, str, null);
        if (!(zza == null || zza.equals(TtmlNode.ANONYMOUS_REGION_ID))) {
            if (zzbqe.matcher(zza).matches()) {
                obj = Boolean.valueOf(true);
                z = true;
            } else if (zzbqf.matcher(zza).matches()) {
                obj = Boolean.valueOf(false);
                z = false;
            } else {
                Log.w("Gservices", "attempt to read gservices key " + str + " (value \"" + zza + "\") as boolean");
            }
        }
        zza(zzb, zzbqi, str, obj);
        return z;
    }

    private static Map<String, String> zza(ContentResolver contentResolver, String... strArr) {
        Cursor query = contentResolver.query(zzbqd, null, null, strArr, null);
        Map<String, String> treeMap = new TreeMap();
        if (query != null) {
            while (query.moveToNext()) {
                try {
                    treeMap.put(query.getString(0), query.getString(1));
                } finally {
                    query.close();
                }
            }
        }
        return treeMap;
    }

    private static Object zzb(ContentResolver contentResolver) {
        Object obj;
        synchronized (zzsg.class) {
            zza(contentResolver);
            obj = zzbqm;
        }
        return obj;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static <T> T zza(java.util.HashMap<java.lang.String, T> r2, java.lang.String r3, T r4) {
        /*
        r1 = com.google.android.gms.internal.measurement.zzsg.class;
        monitor-enter(r1);
        r0 = r2.containsKey(r3);	 Catch:{ all -> 0x0016 }
        if (r0 == 0) goto L_0x0013;
    L_0x0009:
        r0 = r2.get(r3);	 Catch:{ all -> 0x0016 }
        if (r0 == 0) goto L_0x0011;
    L_0x000f:
        monitor-exit(r1);	 Catch:{ all -> 0x0016 }
    L_0x0010:
        return r0;
    L_0x0011:
        r0 = r4;
        goto L_0x000f;
    L_0x0013:
        monitor-exit(r1);	 Catch:{ all -> 0x0016 }
        r0 = 0;
        goto L_0x0010;
    L_0x0016:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0016 }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzsg.zza(java.util.HashMap, java.lang.String, java.lang.Object):T");
    }

    private static <T> void zza(Object obj, HashMap<String, T> hashMap, String str, T t) {
        synchronized (zzsg.class) {
            if (obj == zzbqm) {
                hashMap.put(str, t);
                zzbqh.remove(str);
            }
        }
    }
}
