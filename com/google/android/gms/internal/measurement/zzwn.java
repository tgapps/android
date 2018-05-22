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

public class zzwn {
    private static final Uri CONTENT_URI = Uri.parse("content://com.google.android.gsf.gservices");
    private static final Uri zzbmh = Uri.parse("content://com.google.android.gsf.gservices/prefix");
    public static final Pattern zzbmi = Pattern.compile("^(1|true|t|on|yes|y)$", 2);
    public static final Pattern zzbmj = Pattern.compile("^(0|false|f|off|no|n)$", 2);
    private static final AtomicBoolean zzbmk = new AtomicBoolean();
    private static HashMap<String, String> zzbml;
    private static final HashMap<String, Boolean> zzbmm = new HashMap();
    private static final HashMap<String, Integer> zzbmn = new HashMap();
    private static final HashMap<String, Long> zzbmo = new HashMap();
    private static final HashMap<String, Float> zzbmp = new HashMap();
    private static Object zzbmq;
    private static boolean zzbmr;
    private static String[] zzbms = new String[0];

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static <T> T zza(java.util.HashMap<java.lang.String, T> r2, java.lang.String r3, T r4) {
        /*
        r1 = com.google.android.gms.internal.measurement.zzwn.class;
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
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzwn.zza(java.util.HashMap, java.lang.String, java.lang.Object):T");
    }

    public static String zza(ContentResolver contentResolver, String str, String str2) {
        String str3 = null;
        synchronized (zzwn.class) {
            zza(contentResolver);
            Object obj = zzbmq;
            String str4;
            if (zzbml.containsKey(str)) {
                str4 = (String) zzbml.get(str);
                if (str4 != null) {
                    str3 = str4;
                }
            } else {
                String[] strArr = zzbms;
                int length = strArr.length;
                int i = 0;
                while (i < length) {
                    if (str.startsWith(strArr[i])) {
                        if (!zzbmr || zzbml.isEmpty()) {
                            zzbml.putAll(zza(contentResolver, zzbms));
                            zzbmr = true;
                            if (zzbml.containsKey(str)) {
                                str4 = (String) zzbml.get(str);
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
                        }
                    } catch (Throwable th) {
                        if (query != null) {
                            query.close();
                        }
                    }
                }
                zza(obj, str, null);
                if (query != null) {
                    query.close();
                }
            }
        }
        return str3;
    }

    private static Map<String, String> zza(ContentResolver contentResolver, String... strArr) {
        Cursor query = contentResolver.query(zzbmh, null, null, strArr, null);
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

    private static void zza(ContentResolver contentResolver) {
        if (zzbml == null) {
            zzbmk.set(false);
            zzbml = new HashMap();
            zzbmq = new Object();
            zzbmr = false;
            contentResolver.registerContentObserver(CONTENT_URI, true, new zzwo(null));
        } else if (zzbmk.getAndSet(false)) {
            zzbml.clear();
            zzbmm.clear();
            zzbmn.clear();
            zzbmo.clear();
            zzbmp.clear();
            zzbmq = new Object();
            zzbmr = false;
        }
    }

    private static void zza(Object obj, String str, String str2) {
        synchronized (zzwn.class) {
            if (obj == zzbmq) {
                zzbml.put(str, str2);
            }
        }
    }

    public static boolean zza(ContentResolver contentResolver, String str, boolean z) {
        Object zzb = zzb(contentResolver);
        Object obj = (Boolean) zza(zzbmm, str, Boolean.valueOf(z));
        if (obj != null) {
            return obj.booleanValue();
        }
        Object zza = zza(contentResolver, str, null);
        if (!(zza == null || zza.equals(TtmlNode.ANONYMOUS_REGION_ID))) {
            if (zzbmi.matcher(zza).matches()) {
                obj = Boolean.valueOf(true);
                z = true;
            } else if (zzbmj.matcher(zza).matches()) {
                obj = Boolean.valueOf(false);
                z = false;
            } else {
                Log.w("Gservices", "attempt to read gservices key " + str + " (value \"" + zza + "\") as boolean");
            }
        }
        HashMap hashMap = zzbmm;
        synchronized (zzwn.class) {
            if (zzb == zzbmq) {
                hashMap.put(str, obj);
                zzbml.remove(str);
            }
        }
        return z;
    }

    private static Object zzb(ContentResolver contentResolver) {
        Object obj;
        synchronized (zzwn.class) {
            zza(contentResolver);
            obj = zzbmq;
        }
        return obj;
    }
}
