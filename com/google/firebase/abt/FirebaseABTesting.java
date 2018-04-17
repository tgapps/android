package com.google.firebase.abt;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.internal.firebase_abt.zzj;
import com.google.android.gms.internal.firebase_abt.zzo;
import com.google.android.gms.measurement.AppMeasurement;
import com.google.android.gms.measurement.AppMeasurement.ConditionalUserProperty;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FirebaseABTesting {
    private AppMeasurement zza;
    private String zzb;
    private int zzc;
    private long zzd;
    private SharedPreferences zze;
    private String zzf;
    private Integer zzg = null;

    public FirebaseABTesting(Context context, String str, int i) throws NoClassDefFoundError {
        this.zza = AppMeasurement.getInstance(context);
        this.zzb = str;
        this.zzc = i;
        this.zze = context.getSharedPreferences("com.google.firebase.abt", 0);
        this.zzf = String.format("%s_lastKnownExperimentStartTime", new Object[]{str});
        this.zzd = this.zze.getLong(this.zzf, 0);
    }

    private static zzo zza(byte[] bArr) {
        try {
            return (zzo) zzj.zza(new zzo(), bArr, 0, bArr.length);
        } catch (Throwable e) {
            Log.e("FirebaseABTesting", "Payload was not defined or could not be deserialized.", e);
            return null;
        }
    }

    private final void zza() {
        if (this.zze.getLong(this.zzf, 0) != this.zzd) {
            Editor edit = this.zze.edit();
            edit.putLong(this.zzf, this.zzd);
            edit.apply();
        }
    }

    private final void zza(String str) {
        this.zza.clearConditionalUserProperty(str, null, null);
    }

    private final void zza(Collection<ConditionalUserProperty> collection) {
        for (ConditionalUserProperty conditionalUserProperty : collection) {
            zza(conditionalUserProperty.mName);
        }
    }

    private final boolean zza(zzo com_google_android_gms_internal_firebase_abt_zzo) {
        int i = com_google_android_gms_internal_firebase_abt_zzo.zzc;
        int i2 = this.zzc;
        if (i == 0) {
            i = i2 != 0 ? i2 : 1;
        }
        if (i == 1) {
            return true;
        }
        if (Log.isLoggable("FirebaseABTesting", 3)) {
            Log.d("FirebaseABTesting", String.format("Experiment won't be set due to the overflow policy: [%s, %s]", new Object[]{com_google_android_gms_internal_firebase_abt_zzo.zzaq, com_google_android_gms_internal_firebase_abt_zzo.zzar}));
        }
        return false;
    }

    private final ConditionalUserProperty zzb(zzo com_google_android_gms_internal_firebase_abt_zzo) {
        ConditionalUserProperty conditionalUserProperty = new ConditionalUserProperty();
        conditionalUserProperty.mOrigin = this.zzb;
        conditionalUserProperty.mCreationTimestamp = com_google_android_gms_internal_firebase_abt_zzo.zzas;
        conditionalUserProperty.mName = com_google_android_gms_internal_firebase_abt_zzo.zzaq;
        conditionalUserProperty.mValue = com_google_android_gms_internal_firebase_abt_zzo.zzar;
        conditionalUserProperty.mTriggerEventName = TextUtils.isEmpty(com_google_android_gms_internal_firebase_abt_zzo.zzat) ? null : com_google_android_gms_internal_firebase_abt_zzo.zzat;
        conditionalUserProperty.mTriggerTimeout = com_google_android_gms_internal_firebase_abt_zzo.zzau;
        conditionalUserProperty.mTimeToLive = com_google_android_gms_internal_firebase_abt_zzo.zzav;
        return conditionalUserProperty;
    }

    private final List<ConditionalUserProperty> zzb() {
        return this.zza.getConditionalUserProperties(this.zzb, TtmlNode.ANONYMOUS_REGION_ID);
    }

    private final int zzc() {
        if (this.zzg == null) {
            this.zzg = Integer.valueOf(this.zza.getMaxUserProperties(this.zzb));
        }
        return this.zzg.intValue();
    }

    public void removeAllExperiments() {
        zza(zzb());
    }

    public void replaceAllExperiments(List<byte[]> list) {
        String str;
        String str2;
        if (list == null) {
            str = "FirebaseABTesting";
            str2 = "Cannot replace experiments because experimentPayloads is null.";
        } else if (list.isEmpty()) {
            removeAllExperiments();
            return;
        } else {
            List arrayList = new ArrayList();
            for (byte[] zza : list) {
                zzo zza2 = zza(zza);
                if (zza2 != null) {
                    arrayList.add(zza2);
                }
            }
            if (arrayList.isEmpty()) {
                str = "FirebaseABTesting";
                str2 = "All payloads are either not defined or cannot not be deserialized.";
            } else {
                Set hashSet = new HashSet();
                ArrayList arrayList2 = (ArrayList) arrayList;
                int size = arrayList2.size();
                int i = 0;
                int i2 = 0;
                while (i2 < size) {
                    Object obj = arrayList2.get(i2);
                    i2++;
                    hashSet.add(((zzo) obj).zzaq);
                }
                List<ConditionalUserProperty> zzb = zzb();
                Set hashSet2 = new HashSet();
                for (ConditionalUserProperty conditionalUserProperty : zzb) {
                    hashSet2.add(conditionalUserProperty.mName);
                }
                Collection arrayList3 = new ArrayList();
                for (ConditionalUserProperty conditionalUserProperty2 : zzb) {
                    if (!hashSet.contains(conditionalUserProperty2.mName)) {
                        arrayList3.add(conditionalUserProperty2);
                    }
                }
                zza(arrayList3);
                ArrayList arrayList4 = new ArrayList();
                size = arrayList2.size();
                int i3 = 0;
                while (i3 < size) {
                    Object obj2 = arrayList2.get(i3);
                    i3++;
                    zzo com_google_android_gms_internal_firebase_abt_zzo = (zzo) obj2;
                    if (!hashSet2.contains(com_google_android_gms_internal_firebase_abt_zzo.zzaq)) {
                        int i4 = 1;
                        if (com_google_android_gms_internal_firebase_abt_zzo.zzas <= this.zzd) {
                            if (Log.isLoggable("FirebaseABTesting", 3)) {
                                Log.d("FirebaseABTesting", String.format("The experiment [%s, %s, %d] is not new since its startTime is before lastKnownStartTime: %d", new Object[]{com_google_android_gms_internal_firebase_abt_zzo.zzaq, com_google_android_gms_internal_firebase_abt_zzo.zzar, Long.valueOf(com_google_android_gms_internal_firebase_abt_zzo.zzas), Long.valueOf(this.zzd)}));
                            }
                            i4 = 0;
                        }
                        if (i4 != 0) {
                            arrayList4.add(com_google_android_gms_internal_firebase_abt_zzo);
                        }
                    }
                }
                Deque arrayDeque = new ArrayDeque(zzb());
                i2 = zzc();
                arrayList4 = arrayList4;
                i3 = arrayList4.size();
                int i5 = 0;
                while (i5 < i3) {
                    Object obj3 = arrayList4.get(i5);
                    i5++;
                    zzo com_google_android_gms_internal_firebase_abt_zzo2 = (zzo) obj3;
                    if (arrayDeque.size() >= i2) {
                        if (zza(com_google_android_gms_internal_firebase_abt_zzo2)) {
                            while (arrayDeque.size() >= i2) {
                                zza(((ConditionalUserProperty) arrayDeque.pollFirst()).mName);
                            }
                        }
                    }
                    ConditionalUserProperty zzb2 = zzb(com_google_android_gms_internal_firebase_abt_zzo2);
                    this.zza.setConditionalUserProperty(zzb2);
                    arrayDeque.offer(zzb2);
                }
                int size2 = arrayList2.size();
                while (i < size2) {
                    Object obj4 = arrayList2.get(i);
                    i++;
                    this.zzd = Math.max(this.zzd, ((zzo) obj4).zzas);
                }
                zza();
                return;
            }
        }
        Log.e(str, str2);
    }
}
