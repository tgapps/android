package com.google.firebase.messaging;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.internal.measurement.zzabi;
import com.google.android.gms.internal.measurement.zzabj;
import com.google.android.gms.internal.measurement.zzabn;
import com.google.android.gms.internal.measurement.zzabo;
import com.google.android.gms.measurement.AppMeasurement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public final class zzc {
    private static Object zza(zzabo com_google_android_gms_internal_measurement_zzabo, String str, zzb com_google_firebase_messaging_zzb) {
        Object newInstance;
        Object obj = null;
        try {
            Class cls = Class.forName("com.google.android.gms.measurement.AppMeasurement$ConditionalUserProperty");
            Bundle zzw = zzw(com_google_android_gms_internal_measurement_zzabo.zzcag, com_google_android_gms_internal_measurement_zzabo.zzcah);
            newInstance = cls.getConstructor(new Class[0]).newInstance(new Object[0]);
            try {
                cls.getField("mOrigin").set(newInstance, str);
                cls.getField("mCreationTimestamp").set(newInstance, Long.valueOf(com_google_android_gms_internal_measurement_zzabo.zzcai));
                cls.getField("mName").set(newInstance, com_google_android_gms_internal_measurement_zzabo.zzcag);
                cls.getField("mValue").set(newInstance, com_google_android_gms_internal_measurement_zzabo.zzcah);
                if (!TextUtils.isEmpty(com_google_android_gms_internal_measurement_zzabo.zzcaj)) {
                    obj = com_google_android_gms_internal_measurement_zzabo.zzcaj;
                }
                cls.getField("mTriggerEventName").set(newInstance, obj);
                cls.getField("mTimedOutEventName").set(newInstance, !TextUtils.isEmpty(com_google_android_gms_internal_measurement_zzabo.zzbsn) ? com_google_android_gms_internal_measurement_zzabo.zzbsn : com_google_firebase_messaging_zzb.zztk());
                cls.getField("mTimedOutEventParams").set(newInstance, zzw);
                cls.getField("mTriggerTimeout").set(newInstance, Long.valueOf(com_google_android_gms_internal_measurement_zzabo.zzcak));
                cls.getField("mTriggeredEventName").set(newInstance, !TextUtils.isEmpty(com_google_android_gms_internal_measurement_zzabo.zzbsm) ? com_google_android_gms_internal_measurement_zzabo.zzbsm : com_google_firebase_messaging_zzb.zztj());
                cls.getField("mTriggeredEventParams").set(newInstance, zzw);
                cls.getField("mTimeToLive").set(newInstance, Long.valueOf(com_google_android_gms_internal_measurement_zzabo.zzrp));
                cls.getField("mExpiredEventName").set(newInstance, !TextUtils.isEmpty(com_google_android_gms_internal_measurement_zzabo.zzbso) ? com_google_android_gms_internal_measurement_zzabo.zzbso : com_google_firebase_messaging_zzb.zztl());
                cls.getField("mExpiredEventParams").set(newInstance, zzw);
            } catch (Exception e) {
                e = e;
                Log.e("FirebaseAbtUtil", "Could not complete the operation due to an internal error.", e);
                return newInstance;
            }
        } catch (Exception e2) {
            Throwable e3;
            e3 = e2;
            newInstance = null;
            Log.e("FirebaseAbtUtil", "Could not complete the operation due to an internal error.", e3);
            return newInstance;
        }
        return newInstance;
    }

    private static String zza(zzabo com_google_android_gms_internal_measurement_zzabo, zzb com_google_firebase_messaging_zzb) {
        return (com_google_android_gms_internal_measurement_zzabo == null || TextUtils.isEmpty(com_google_android_gms_internal_measurement_zzabo.zzbsp)) ? com_google_firebase_messaging_zzb.zztm() : com_google_android_gms_internal_measurement_zzabo.zzbsp;
    }

    private static List<Object> zza(AppMeasurement appMeasurement, String str) {
        List<Object> list;
        ArrayList arrayList = new ArrayList();
        try {
            Method declaredMethod = AppMeasurement.class.getDeclaredMethod("getConditionalUserProperties", new Class[]{String.class, String.class});
            declaredMethod.setAccessible(true);
            list = (List) declaredMethod.invoke(appMeasurement, new Object[]{str, TtmlNode.ANONYMOUS_REGION_ID});
        } catch (Throwable e) {
            Log.e("FirebaseAbtUtil", "Could not complete the operation due to an internal error.", e);
            Object obj = arrayList;
        }
        if (Log.isLoggable("FirebaseAbtUtil", 2)) {
            Log.v("FirebaseAbtUtil", new StringBuilder(String.valueOf(str).length() + 55).append("Number of currently set _Es for origin: ").append(str).append(" is ").append(list.size()).toString());
        }
        return list;
    }

    private static void zza(Context context, String str, String str2, String str3, String str4) {
        if (Log.isLoggable("FirebaseAbtUtil", 2)) {
            String str5 = "FirebaseAbtUtil";
            String str6 = "_CE(experimentId) called by ";
            String valueOf = String.valueOf(str);
            Log.v(str5, valueOf.length() != 0 ? str6.concat(valueOf) : new String(str6));
        }
        if (zzy(context)) {
            AppMeasurement zzx = zzx(context);
            try {
                Method declaredMethod = AppMeasurement.class.getDeclaredMethod("clearConditionalUserProperty", new Class[]{String.class, String.class, Bundle.class});
                declaredMethod.setAccessible(true);
                if (Log.isLoggable("FirebaseAbtUtil", 2)) {
                    Log.v("FirebaseAbtUtil", new StringBuilder((String.valueOf(str2).length() + 17) + String.valueOf(str3).length()).append("Clearing _E: [").append(str2).append(", ").append(str3).append("]").toString());
                }
                declaredMethod.invoke(zzx, new Object[]{str2, str4, zzw(str2, str3)});
            } catch (Throwable e) {
                Log.e("FirebaseAbtUtil", "Could not complete the operation due to an internal error.", e);
            }
        }
    }

    public static void zza(Context context, String str, byte[] bArr, zzb com_google_firebase_messaging_zzb, int i) {
        if (Log.isLoggable("FirebaseAbtUtil", 2)) {
            String str2 = "FirebaseAbtUtil";
            String str3 = "_SE called by ";
            String valueOf = String.valueOf(str);
            Log.v(str2, valueOf.length() != 0 ? str3.concat(valueOf) : new String(str3));
        }
        if (zzy(context)) {
            AppMeasurement zzx = zzx(context);
            zzabo zzi = zzi(bArr);
            if (zzi != null) {
                try {
                    Object obj;
                    Class.forName("com.google.android.gms.measurement.AppMeasurement$ConditionalUserProperty");
                    Object obj2 = null;
                    for (Object obj3 : zza(zzx, str)) {
                        String zzs = zzs(obj3);
                        String zzt = zzt(obj3);
                        long longValue = ((Long) Class.forName("com.google.android.gms.measurement.AppMeasurement$ConditionalUserProperty").getField("mCreationTimestamp").get(obj3)).longValue();
                        if (zzi.zzcag.equals(zzs) && zzi.zzcah.equals(zzt)) {
                            if (Log.isLoggable("FirebaseAbtUtil", 2)) {
                                Log.v("FirebaseAbtUtil", new StringBuilder((String.valueOf(zzs).length() + 23) + String.valueOf(zzt).length()).append("_E is already set. [").append(zzs).append(", ").append(zzt).append("]").toString());
                            }
                            obj2 = 1;
                        } else {
                            obj3 = null;
                            zzabn[] com_google_android_gms_internal_measurement_zzabnArr = zzi.zzcam;
                            int length = com_google_android_gms_internal_measurement_zzabnArr.length;
                            int i2 = 0;
                            while (i2 < length) {
                                if (com_google_android_gms_internal_measurement_zzabnArr[i2].zzcag.equals(zzs)) {
                                    if (Log.isLoggable("FirebaseAbtUtil", 2)) {
                                        Log.v("FirebaseAbtUtil", new StringBuilder((String.valueOf(zzs).length() + 33) + String.valueOf(zzt).length()).append("_E is found in the _OE list. [").append(zzs).append(", ").append(zzt).append("]").toString());
                                    }
                                    obj3 = 1;
                                    if (obj3 != null) {
                                        continue;
                                    } else if (zzi.zzcai > longValue) {
                                        if (Log.isLoggable("FirebaseAbtUtil", 2)) {
                                            Log.v("FirebaseAbtUtil", new StringBuilder((String.valueOf(zzs).length() + 115) + String.valueOf(zzt).length()).append("Clearing _E as it was not in the _OE list, andits start time is older than the start time of the _E to be set. [").append(zzs).append(", ").append(zzt).append("]").toString());
                                        }
                                        zza(context, str, zzs, zzt, zza(zzi, com_google_firebase_messaging_zzb));
                                    } else if (Log.isLoggable("FirebaseAbtUtil", 2)) {
                                        Log.v("FirebaseAbtUtil", new StringBuilder((String.valueOf(zzs).length() + 109) + String.valueOf(zzt).length()).append("_E was not found in the _OE list, but not clearing it as it has a new start time than the _E to be set.  [").append(zzs).append(", ").append(zzt).append("]").toString());
                                    }
                                } else {
                                    i2++;
                                }
                            }
                            if (obj3 != null) {
                                continue;
                            } else if (zzi.zzcai > longValue) {
                                if (Log.isLoggable("FirebaseAbtUtil", 2)) {
                                    Log.v("FirebaseAbtUtil", new StringBuilder((String.valueOf(zzs).length() + 115) + String.valueOf(zzt).length()).append("Clearing _E as it was not in the _OE list, andits start time is older than the start time of the _E to be set. [").append(zzs).append(", ").append(zzt).append("]").toString());
                                }
                                zza(context, str, zzs, zzt, zza(zzi, com_google_firebase_messaging_zzb));
                            } else if (Log.isLoggable("FirebaseAbtUtil", 2)) {
                                Log.v("FirebaseAbtUtil", new StringBuilder((String.valueOf(zzs).length() + 109) + String.valueOf(zzt).length()).append("_E was not found in the _OE list, but not clearing it as it has a new start time than the _E to be set.  [").append(zzs).append(", ").append(zzt).append("]").toString());
                            }
                        }
                    }
                    if (obj2 == null) {
                        if (Log.isLoggable("FirebaseAbtUtil", 2)) {
                            str2 = zzi.zzcag;
                            str3 = zzi.zzcah;
                            Log.v("FirebaseAbtUtil", new StringBuilder((String.valueOf(str2).length() + 7) + String.valueOf(str3).length()).append("_SEI: ").append(str2).append(" ").append(str3).toString());
                        }
                        try {
                            Class.forName("com.google.android.gms.measurement.AppMeasurement$ConditionalUserProperty");
                            List zza = zza(zzx, str);
                            if (zza(zzx, str).size() >= zzb(zzx, str)) {
                                if ((zzi.zzcal != 0 ? zzi.zzcal : 1) == 1) {
                                    obj3 = zza.get(0);
                                    str3 = zzs(obj3);
                                    valueOf = zzt(obj3);
                                    if (Log.isLoggable("FirebaseAbtUtil", 2)) {
                                        Log.v("FirebaseAbtUtil", new StringBuilder(String.valueOf(str3).length() + 38).append("Clearing _E due to overflow policy: [").append(str3).append("]").toString());
                                    }
                                    zza(context, str, str3, valueOf, zza(zzi, com_google_firebase_messaging_zzb));
                                } else if (Log.isLoggable("FirebaseAbtUtil", 2)) {
                                    str2 = zzi.zzcag;
                                    str3 = zzi.zzcah;
                                    Log.v("FirebaseAbtUtil", new StringBuilder((String.valueOf(str2).length() + 44) + String.valueOf(str3).length()).append("_E won't be set due to overflow policy. [").append(str2).append(", ").append(str3).append("]").toString());
                                    return;
                                } else {
                                    return;
                                }
                            }
                            for (Object next : zza) {
                                str3 = zzs(next);
                                str2 = zzt(next);
                                if (str3.equals(zzi.zzcag) && !str2.equals(zzi.zzcah) && Log.isLoggable("FirebaseAbtUtil", 2)) {
                                    Log.v("FirebaseAbtUtil", new StringBuilder((String.valueOf(str3).length() + 77) + String.valueOf(str2).length()).append("Clearing _E, as only one _V of the same _E can be set atany given time: [").append(str3).append(", ").append(str2).append("].").toString());
                                    zza(context, str, str3, str2, zza(zzi, com_google_firebase_messaging_zzb));
                                }
                            }
                            if (zza(zzi, str, com_google_firebase_messaging_zzb) != null) {
                                try {
                                    Method declaredMethod = AppMeasurement.class.getDeclaredMethod("setConditionalUserProperty", new Class[]{Class.forName("com.google.android.gms.measurement.AppMeasurement$ConditionalUserProperty")});
                                    declaredMethod.setAccessible(true);
                                    declaredMethod.invoke(zzx, new Object[]{obj3});
                                } catch (Throwable e) {
                                    Log.e("FirebaseAbtUtil", "Could not complete the operation due to an internal error.", e);
                                }
                            } else if (Log.isLoggable("FirebaseAbtUtil", 2)) {
                                str2 = zzi.zzcag;
                                str3 = zzi.zzcah;
                                Log.v("FirebaseAbtUtil", new StringBuilder((String.valueOf(str2).length() + 42) + String.valueOf(str3).length()).append("Could not create _CUP for: [").append(str2).append(", ").append(str3).append("]. Skipping.").toString());
                            }
                        } catch (Throwable e2) {
                            Log.e("FirebaseAbtUtil", "Could not complete the operation due to an internal error.", e2);
                        }
                    } else if (Log.isLoggable("FirebaseAbtUtil", 2)) {
                        str2 = zzi.zzcag;
                        str3 = zzi.zzcah;
                        Log.v("FirebaseAbtUtil", new StringBuilder((String.valueOf(str2).length() + 44) + String.valueOf(str3).length()).append("_E is already set. Not setting it again [").append(str2).append(", ").append(str3).append("]").toString());
                    }
                } catch (Throwable e22) {
                    Log.e("FirebaseAbtUtil", "Could not complete the operation due to an internal error.", e22);
                }
            } else if (Log.isLoggable("FirebaseAbtUtil", 2)) {
                Log.v("FirebaseAbtUtil", "_SE failed; either _P was not set, or we couldn't deserialize the _P.");
            }
        }
    }

    private static int zzb(AppMeasurement appMeasurement, String str) {
        try {
            Method declaredMethod = AppMeasurement.class.getDeclaredMethod("getMaxUserProperties", new Class[]{String.class});
            declaredMethod.setAccessible(true);
            return ((Integer) declaredMethod.invoke(appMeasurement, new Object[]{str})).intValue();
        } catch (Throwable e) {
            Log.e("FirebaseAbtUtil", "Could not complete the operation due to an internal error.", e);
            return 20;
        }
    }

    private static zzabo zzi(byte[] bArr) {
        try {
            return (zzabo) zzabj.zza(new zzabo(), bArr);
        } catch (zzabi e) {
            return null;
        }
    }

    private static String zzs(Object obj) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        return (String) Class.forName("com.google.android.gms.measurement.AppMeasurement$ConditionalUserProperty").getField("mName").get(obj);
    }

    private static String zzt(Object obj) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        return (String) Class.forName("com.google.android.gms.measurement.AppMeasurement$ConditionalUserProperty").getField("mValue").get(obj);
    }

    private static Bundle zzw(String str, String str2) {
        Bundle bundle = new Bundle();
        bundle.putString(str, str2);
        return bundle;
    }

    private static AppMeasurement zzx(Context context) {
        try {
            return AppMeasurement.getInstance(context);
        } catch (NoClassDefFoundError e) {
            return null;
        }
    }

    private static boolean zzy(Context context) {
        if (zzx(context) != null) {
            try {
                Class.forName("com.google.android.gms.measurement.AppMeasurement$ConditionalUserProperty");
                return true;
            } catch (ClassNotFoundException e) {
                if (!Log.isLoggable("FirebaseAbtUtil", 2)) {
                    return false;
                }
                Log.v("FirebaseAbtUtil", "Firebase Analytics library is missing support for abt. Please update to a more recent version.");
                return false;
            }
        } else if (!Log.isLoggable("FirebaseAbtUtil", 2)) {
            return false;
        } else {
            Log.v("FirebaseAbtUtil", "Firebase Analytics not available");
            return false;
        }
    }
}
