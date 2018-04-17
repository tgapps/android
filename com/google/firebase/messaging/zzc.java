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
        Throwable e;
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
                return newInstance;
            } catch (Exception e2) {
                e = e2;
                Log.e("FirebaseAbtUtil", "Could not complete the operation due to an internal error.", e);
                return newInstance;
            }
        } catch (Exception e3) {
            e = e3;
            newInstance = null;
            Log.e("FirebaseAbtUtil", "Could not complete the operation due to an internal error.", e);
            return newInstance;
        }
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
            list = arrayList;
        }
        if (Log.isLoggable("FirebaseAbtUtil", 2)) {
            int size = list.size();
            StringBuilder stringBuilder = new StringBuilder(55 + String.valueOf(str).length());
            stringBuilder.append("Number of currently set _Es for origin: ");
            stringBuilder.append(str);
            stringBuilder.append(" is ");
            stringBuilder.append(size);
            Log.v("FirebaseAbtUtil", stringBuilder.toString());
        }
        return list;
    }

    private static void zza(Context context, String str, String str2, String str3, String str4) {
        if (Log.isLoggable("FirebaseAbtUtil", 2)) {
            String str5 = "FirebaseAbtUtil";
            String str6 = "_CE(experimentId) called by ";
            str = String.valueOf(str);
            Log.v(str5, str.length() != 0 ? str6.concat(str) : new String(str6));
        }
        if (zzy(context)) {
            AppMeasurement zzx = zzx(context);
            try {
                Method declaredMethod = AppMeasurement.class.getDeclaredMethod("clearConditionalUserProperty", new Class[]{String.class, String.class, Bundle.class});
                declaredMethod.setAccessible(true);
                if (Log.isLoggable("FirebaseAbtUtil", 2)) {
                    StringBuilder stringBuilder = new StringBuilder((17 + String.valueOf(str2).length()) + String.valueOf(str3).length());
                    stringBuilder.append("Clearing _E: [");
                    stringBuilder.append(str2);
                    stringBuilder.append(", ");
                    stringBuilder.append(str3);
                    stringBuilder.append("]");
                    Log.v("FirebaseAbtUtil", stringBuilder.toString());
                }
                declaredMethod.invoke(zzx, new Object[]{str2, str4, zzw(str2, str3)});
            } catch (Throwable e) {
                Log.e("FirebaseAbtUtil", "Could not complete the operation due to an internal error.", e);
            }
        }
    }

    public static void zza(Context context, String str, byte[] bArr, zzb com_google_firebase_messaging_zzb, int i) {
        Context context2 = context;
        String str2 = str;
        zzb com_google_firebase_messaging_zzb2 = com_google_firebase_messaging_zzb;
        int i2 = 2;
        if (Log.isLoggable("FirebaseAbtUtil", 2)) {
            String str3 = "FirebaseAbtUtil";
            String str4 = "_SE called by ";
            String valueOf = String.valueOf(str);
            Log.v(str3, valueOf.length() != 0 ? str4.concat(valueOf) : new String(str4));
        }
        if (zzy(context)) {
            AppMeasurement zzx = zzx(context);
            zzabo zzi = zzi(bArr);
            if (zzi == null) {
                if (Log.isLoggable("FirebaseAbtUtil", 2)) {
                    Log.v("FirebaseAbtUtil", "_SE failed; either _P was not set, or we couldn't deserialize the _P.");
                }
                return;
            }
            try {
                int length;
                int i3;
                Object obj;
                Class.forName("com.google.android.gms.measurement.AppMeasurement$ConditionalUserProperty");
                Object obj2 = null;
                for (Object next : zza(zzx, str2)) {
                    String zzs = zzs(next);
                    String zzt = zzt(next);
                    long longValue = ((Long) Class.forName("com.google.android.gms.measurement.AppMeasurement$ConditionalUserProperty").getField("mCreationTimestamp").get(next)).longValue();
                    if (zzi.zzcag.equals(zzs) && zzi.zzcah.equals(zzt)) {
                        if (Log.isLoggable("FirebaseAbtUtil", i2)) {
                            StringBuilder stringBuilder = new StringBuilder((23 + String.valueOf(zzs).length()) + String.valueOf(zzt).length());
                            stringBuilder.append("_E is already set. [");
                            stringBuilder.append(zzs);
                            stringBuilder.append(", ");
                            stringBuilder.append(zzt);
                            stringBuilder.append("]");
                            Log.v("FirebaseAbtUtil", stringBuilder.toString());
                        }
                        obj2 = 1;
                    } else {
                        StringBuilder stringBuilder2;
                        zzabn[] com_google_android_gms_internal_measurement_zzabnArr = zzi.zzcam;
                        length = com_google_android_gms_internal_measurement_zzabnArr.length;
                        i3 = 0;
                        while (i3 < length) {
                            if (com_google_android_gms_internal_measurement_zzabnArr[i3].zzcag.equals(zzs)) {
                                if (Log.isLoggable("FirebaseAbtUtil", 2)) {
                                    stringBuilder2 = new StringBuilder((33 + String.valueOf(zzs).length()) + String.valueOf(zzt).length());
                                    stringBuilder2.append("_E is found in the _OE list. [");
                                    stringBuilder2.append(zzs);
                                    stringBuilder2.append(", ");
                                    stringBuilder2.append(zzt);
                                    stringBuilder2.append("]");
                                    Log.v("FirebaseAbtUtil", stringBuilder2.toString());
                                }
                                obj = 1;
                                if (obj == null) {
                                    if (zzi.zzcai > longValue) {
                                        if (Log.isLoggable("FirebaseAbtUtil", 2)) {
                                            stringBuilder2 = new StringBuilder((115 + String.valueOf(zzs).length()) + String.valueOf(zzt).length());
                                            stringBuilder2.append("Clearing _E as it was not in the _OE list, andits start time is older than the start time of the _E to be set. [");
                                            stringBuilder2.append(zzs);
                                            stringBuilder2.append(", ");
                                            stringBuilder2.append(zzt);
                                            stringBuilder2.append("]");
                                            Log.v("FirebaseAbtUtil", stringBuilder2.toString());
                                        }
                                        zza(context2, str2, zzs, zzt, zza(zzi, com_google_firebase_messaging_zzb2));
                                    } else if (Log.isLoggable("FirebaseAbtUtil", 2)) {
                                        stringBuilder2 = new StringBuilder((109 + String.valueOf(zzs).length()) + String.valueOf(zzt).length());
                                        stringBuilder2.append("_E was not found in the _OE list, but not clearing it as it has a new start time than the _E to be set.  [");
                                        stringBuilder2.append(zzs);
                                        stringBuilder2.append(", ");
                                        stringBuilder2.append(zzt);
                                        stringBuilder2.append("]");
                                        Log.v("FirebaseAbtUtil", stringBuilder2.toString());
                                    }
                                }
                                i2 = 2;
                            } else {
                                i3++;
                            }
                        }
                        obj = null;
                        if (obj == null) {
                            if (zzi.zzcai > longValue) {
                                if (Log.isLoggable("FirebaseAbtUtil", 2)) {
                                    stringBuilder2 = new StringBuilder((115 + String.valueOf(zzs).length()) + String.valueOf(zzt).length());
                                    stringBuilder2.append("Clearing _E as it was not in the _OE list, andits start time is older than the start time of the _E to be set. [");
                                    stringBuilder2.append(zzs);
                                    stringBuilder2.append(", ");
                                    stringBuilder2.append(zzt);
                                    stringBuilder2.append("]");
                                    Log.v("FirebaseAbtUtil", stringBuilder2.toString());
                                }
                                zza(context2, str2, zzs, zzt, zza(zzi, com_google_firebase_messaging_zzb2));
                            } else if (Log.isLoggable("FirebaseAbtUtil", 2)) {
                                stringBuilder2 = new StringBuilder((109 + String.valueOf(zzs).length()) + String.valueOf(zzt).length());
                                stringBuilder2.append("_E was not found in the _OE list, but not clearing it as it has a new start time than the _E to be set.  [");
                                stringBuilder2.append(zzs);
                                stringBuilder2.append(", ");
                                stringBuilder2.append(zzt);
                                stringBuilder2.append("]");
                                Log.v("FirebaseAbtUtil", stringBuilder2.toString());
                            }
                        }
                        i2 = 2;
                    }
                }
                String str5;
                StringBuilder stringBuilder3;
                if (obj2 != null) {
                    if (Log.isLoggable("FirebaseAbtUtil", 2)) {
                        str2 = zzi.zzcag;
                        str5 = zzi.zzcah;
                        stringBuilder3 = new StringBuilder((44 + String.valueOf(str2).length()) + String.valueOf(str5).length());
                        stringBuilder3.append("_E is already set. Not setting it again [");
                        stringBuilder3.append(str2);
                        stringBuilder3.append(", ");
                        stringBuilder3.append(str5);
                        stringBuilder3.append("]");
                        Log.v("FirebaseAbtUtil", stringBuilder3.toString());
                    }
                    return;
                }
                String str6;
                StringBuilder stringBuilder4;
                if (Log.isLoggable("FirebaseAbtUtil", 2)) {
                    str6 = zzi.zzcag;
                    String str7 = zzi.zzcah;
                    stringBuilder4 = new StringBuilder((7 + String.valueOf(str6).length()) + String.valueOf(str7).length());
                    stringBuilder4.append("_SEI: ");
                    stringBuilder4.append(str6);
                    stringBuilder4.append(" ");
                    stringBuilder4.append(str7);
                    Log.v("FirebaseAbtUtil", stringBuilder4.toString());
                }
                try {
                    Class.forName("com.google.android.gms.measurement.AppMeasurement$ConditionalUserProperty");
                    List zza = zza(zzx, str2);
                    if (zza(zzx, str2).size() >= zzb(zzx, str2)) {
                        if (zzi.zzcal != 0) {
                            i3 = zzi.zzcal;
                            length = 1;
                        } else {
                            length = 1;
                            i3 = 1;
                        }
                        if (i3 == length) {
                            obj = zza.get(0);
                            str6 = zzs(obj);
                            String zzt2 = zzt(obj);
                            if (Log.isLoggable("FirebaseAbtUtil", 2)) {
                                stringBuilder4 = new StringBuilder(38 + String.valueOf(str6).length());
                                stringBuilder4.append("Clearing _E due to overflow policy: [");
                                stringBuilder4.append(str6);
                                stringBuilder4.append("]");
                                Log.v("FirebaseAbtUtil", stringBuilder4.toString());
                            }
                            zza(context2, str2, str6, zzt2, zza(zzi, com_google_firebase_messaging_zzb2));
                        } else {
                            if (Log.isLoggable("FirebaseAbtUtil", 2)) {
                                str2 = zzi.zzcag;
                                str5 = zzi.zzcah;
                                stringBuilder3 = new StringBuilder((44 + String.valueOf(str2).length()) + String.valueOf(str5).length());
                                stringBuilder3.append("_E won't be set due to overflow policy. [");
                                stringBuilder3.append(str2);
                                stringBuilder3.append(", ");
                                stringBuilder3.append(str5);
                                stringBuilder3.append("]");
                                Log.v("FirebaseAbtUtil", stringBuilder3.toString());
                            }
                            return;
                        }
                    }
                    for (Object next2 : zza) {
                        str6 = zzs(next2);
                        valueOf = zzt(next2);
                        if (str6.equals(zzi.zzcag) && !valueOf.equals(zzi.zzcah) && Log.isLoggable("FirebaseAbtUtil", 2)) {
                            stringBuilder4 = new StringBuilder((77 + String.valueOf(str6).length()) + String.valueOf(valueOf).length());
                            stringBuilder4.append("Clearing _E, as only one _V of the same _E can be set atany given time: [");
                            stringBuilder4.append(str6);
                            stringBuilder4.append(", ");
                            stringBuilder4.append(valueOf);
                            stringBuilder4.append("].");
                            Log.v("FirebaseAbtUtil", stringBuilder4.toString());
                            zza(context2, str2, str6, valueOf, zza(zzi, com_google_firebase_messaging_zzb2));
                        }
                    }
                    if (zza(zzi, str2, com_google_firebase_messaging_zzb2) == null) {
                        if (Log.isLoggable("FirebaseAbtUtil", 2)) {
                            str2 = zzi.zzcag;
                            str5 = zzi.zzcah;
                            StringBuilder stringBuilder5 = new StringBuilder((42 + String.valueOf(str2).length()) + String.valueOf(str5).length());
                            stringBuilder5.append("Could not create _CUP for: [");
                            stringBuilder5.append(str2);
                            stringBuilder5.append(", ");
                            stringBuilder5.append(str5);
                            stringBuilder5.append("]. Skipping.");
                            Log.v("FirebaseAbtUtil", stringBuilder5.toString());
                        }
                        return;
                    }
                    try {
                        Method declaredMethod = AppMeasurement.class.getDeclaredMethod("setConditionalUserProperty", new Class[]{Class.forName("com.google.android.gms.measurement.AppMeasurement$ConditionalUserProperty")});
                        declaredMethod.setAccessible(true);
                        declaredMethod.invoke(zzx, new Object[]{r1});
                    } catch (Throwable e) {
                        Log.e("FirebaseAbtUtil", "Could not complete the operation due to an internal error.", e);
                    }
                } catch (Throwable e2) {
                    Log.e("FirebaseAbtUtil", "Could not complete the operation due to an internal error.", e2);
                }
            } catch (Throwable e22) {
                Log.e("FirebaseAbtUtil", "Could not complete the operation due to an internal error.", e22);
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
        if (zzx(context) == null) {
            if (Log.isLoggable("FirebaseAbtUtil", 2)) {
                Log.v("FirebaseAbtUtil", "Firebase Analytics not available");
            }
            return false;
        }
        try {
            Class.forName("com.google.android.gms.measurement.AppMeasurement$ConditionalUserProperty");
            return true;
        } catch (ClassNotFoundException e) {
            if (Log.isLoggable("FirebaseAbtUtil", 2)) {
                Log.v("FirebaseAbtUtil", "Firebase Analytics library is missing support for abt. Please update to a more recent version.");
            }
            return false;
        }
    }
}
