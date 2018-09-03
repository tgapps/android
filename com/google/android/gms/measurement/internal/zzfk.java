package com.google.android.gms.measurement.internal;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ServiceInfo;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import com.google.android.exoplayer2.source.chunk.ChunkedTrackBlacklistUtil;
import com.google.android.gms.common.GoogleApiAvailabilityLight;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.Clock;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.gms.common.wrappers.Wrappers;
import com.google.android.gms.measurement.AppMeasurement.Event;
import com.google.android.gms.measurement.AppMeasurement.UserProperty;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.security.auth.x500.X500Principal;

public final class zzfk extends zzcp {
    private static final String[] zzaui = new String[]{"firebase_", "google_", "ga_"};
    private int zzaed;
    private SecureRandom zzauj;
    private final AtomicLong zzauk = new AtomicLong(0);
    private Integer zzaul = null;

    zzfk(zzbt com_google_android_gms_measurement_internal_zzbt) {
        super(com_google_android_gms_measurement_internal_zzbt);
    }

    protected final boolean zzgt() {
        return true;
    }

    protected final void zzgu() {
        zzaf();
        SecureRandom secureRandom = new SecureRandom();
        long nextLong = secureRandom.nextLong();
        if (nextLong == 0) {
            nextLong = secureRandom.nextLong();
            if (nextLong == 0) {
                zzgo().zzjg().zzbx("Utils falling back to Random for random id");
            }
        }
        this.zzauk.set(nextLong);
    }

    public final long zzmc() {
        long nextLong;
        if (this.zzauk.get() == 0) {
            synchronized (this.zzauk) {
                nextLong = new Random(System.nanoTime() ^ zzbx().currentTimeMillis()).nextLong();
                int i = this.zzaed + 1;
                this.zzaed = i;
                nextLong += (long) i;
            }
        } else {
            synchronized (this.zzauk) {
                this.zzauk.compareAndSet(-1, 1);
                nextLong = this.zzauk.getAndIncrement();
            }
        }
        return nextLong;
    }

    final SecureRandom zzmd() {
        zzaf();
        if (this.zzauj == null) {
            this.zzauj = new SecureRandom();
        }
        return this.zzauj;
    }

    static boolean zzcq(String str) {
        Preconditions.checkNotEmpty(str);
        if (str.charAt(0) != '_' || str.equals("_ep")) {
            return true;
        }
        return false;
    }

    final Bundle zza(Uri uri) {
        Bundle bundle = null;
        if (uri != null) {
            try {
                Object queryParameter;
                Object queryParameter2;
                Object queryParameter3;
                Object queryParameter4;
                if (uri.isHierarchical()) {
                    queryParameter = uri.getQueryParameter("utm_campaign");
                    queryParameter2 = uri.getQueryParameter("utm_source");
                    queryParameter3 = uri.getQueryParameter("utm_medium");
                    queryParameter4 = uri.getQueryParameter("gclid");
                } else {
                    queryParameter4 = null;
                    queryParameter3 = null;
                    queryParameter2 = null;
                    queryParameter = null;
                }
                if (!(TextUtils.isEmpty(queryParameter) && TextUtils.isEmpty(queryParameter2) && TextUtils.isEmpty(queryParameter3) && TextUtils.isEmpty(queryParameter4))) {
                    bundle = new Bundle();
                    if (!TextUtils.isEmpty(queryParameter)) {
                        bundle.putString("campaign", queryParameter);
                    }
                    if (!TextUtils.isEmpty(queryParameter2)) {
                        bundle.putString("source", queryParameter2);
                    }
                    if (!TextUtils.isEmpty(queryParameter3)) {
                        bundle.putString("medium", queryParameter3);
                    }
                    if (!TextUtils.isEmpty(queryParameter4)) {
                        bundle.putString("gclid", queryParameter4);
                    }
                    queryParameter4 = uri.getQueryParameter("utm_term");
                    if (!TextUtils.isEmpty(queryParameter4)) {
                        bundle.putString("term", queryParameter4);
                    }
                    queryParameter4 = uri.getQueryParameter("utm_content");
                    if (!TextUtils.isEmpty(queryParameter4)) {
                        bundle.putString("content", queryParameter4);
                    }
                    queryParameter4 = uri.getQueryParameter("aclid");
                    if (!TextUtils.isEmpty(queryParameter4)) {
                        bundle.putString("aclid", queryParameter4);
                    }
                    queryParameter4 = uri.getQueryParameter("cp1");
                    if (!TextUtils.isEmpty(queryParameter4)) {
                        bundle.putString("cp1", queryParameter4);
                    }
                    queryParameter4 = uri.getQueryParameter("anid");
                    if (!TextUtils.isEmpty(queryParameter4)) {
                        bundle.putString("anid", queryParameter4);
                    }
                }
            } catch (UnsupportedOperationException e) {
                zzgo().zzjg().zzg("Install referrer url isn't a hierarchical URI", e);
            }
        }
        return bundle;
    }

    static boolean zzd(Intent intent) {
        String stringExtra = intent.getStringExtra("android.intent.extra.REFERRER_NAME");
        return "android-app://com.google.android.googlequicksearchbox/https/www.google.com".equals(stringExtra) || "https://www.google.com".equals(stringExtra) || "android-app://com.google.appcrawler".equals(stringExtra);
    }

    final boolean zzr(String str, String str2) {
        if (str2 == null) {
            zzgo().zzjd().zzg("Name is required and can't be null. Type", str);
            return false;
        } else if (str2.length() == 0) {
            zzgo().zzjd().zzg("Name is required and can't be empty. Type", str);
            return false;
        } else {
            int codePointAt = str2.codePointAt(0);
            if (Character.isLetter(codePointAt)) {
                int length = str2.length();
                codePointAt = Character.charCount(codePointAt);
                while (codePointAt < length) {
                    int codePointAt2 = str2.codePointAt(codePointAt);
                    if (codePointAt2 == 95 || Character.isLetterOrDigit(codePointAt2)) {
                        codePointAt += Character.charCount(codePointAt2);
                    } else {
                        zzgo().zzjd().zze("Name must consist of letters, digits or _ (underscores). Type, name", str, str2);
                        return false;
                    }
                }
                return true;
            }
            zzgo().zzjd().zze("Name must start with a letter. Type, name", str, str2);
            return false;
        }
    }

    private final boolean zzs(String str, String str2) {
        if (str2 == null) {
            zzgo().zzjd().zzg("Name is required and can't be null. Type", str);
            return false;
        } else if (str2.length() == 0) {
            zzgo().zzjd().zzg("Name is required and can't be empty. Type", str);
            return false;
        } else {
            int codePointAt = str2.codePointAt(0);
            if (Character.isLetter(codePointAt) || codePointAt == 95) {
                int length = str2.length();
                codePointAt = Character.charCount(codePointAt);
                while (codePointAt < length) {
                    int codePointAt2 = str2.codePointAt(codePointAt);
                    if (codePointAt2 == 95 || Character.isLetterOrDigit(codePointAt2)) {
                        codePointAt += Character.charCount(codePointAt2);
                    } else {
                        zzgo().zzjd().zze("Name must consist of letters, digits or _ (underscores). Type, name", str, str2);
                        return false;
                    }
                }
                return true;
            }
            zzgo().zzjd().zze("Name must start with a letter or _ (underscore). Type, name", str, str2);
            return false;
        }
    }

    final boolean zza(String str, String[] strArr, String str2) {
        if (str2 == null) {
            zzgo().zzjd().zzg("Name is required and can't be null. Type", str);
            return false;
        }
        boolean z;
        Preconditions.checkNotNull(str2);
        for (String startsWith : zzaui) {
            if (str2.startsWith(startsWith)) {
                z = true;
                break;
            }
        }
        z = false;
        if (z) {
            zzgo().zzjd().zze("Name starts with reserved prefix. Type, name", str, str2);
            return false;
        }
        if (strArr != null) {
            Preconditions.checkNotNull(strArr);
            for (String zzu : strArr) {
                if (zzu(str2, zzu)) {
                    z = true;
                    break;
                }
            }
            z = false;
            if (z) {
                zzgo().zzjd().zze("Name is reserved. Type, name", str, str2);
                return false;
            }
        }
        return true;
    }

    final boolean zza(String str, int i, String str2) {
        if (str2 == null) {
            zzgo().zzjd().zzg("Name is required and can't be null. Type", str);
            return false;
        } else if (str2.codePointCount(0, str2.length()) <= i) {
            return true;
        } else {
            zzgo().zzjd().zzd("Name is too long. Type, maximum supported length, name", str, Integer.valueOf(i), str2);
            return false;
        }
    }

    final int zzcr(String str) {
        if (!zzs("event", str)) {
            return 2;
        }
        if (!zza("event", Event.zzadk, str)) {
            return 13;
        }
        if (zza("event", 40, str)) {
            return 0;
        }
        return 2;
    }

    final int zzcs(String str) {
        if (!zzs("user property", str)) {
            return 6;
        }
        if (!zza("user property", UserProperty.zzado, str)) {
            return 15;
        }
        if (zza("user property", 24, str)) {
            return 0;
        }
        return 6;
    }

    private final boolean zza(String str, String str2, int i, Object obj, boolean z) {
        if (obj == null || (obj instanceof Long) || (obj instanceof Float) || (obj instanceof Integer) || (obj instanceof Byte) || (obj instanceof Short) || (obj instanceof Boolean) || (obj instanceof Double)) {
            return true;
        }
        if ((obj instanceof String) || (obj instanceof Character) || (obj instanceof CharSequence)) {
            String valueOf = String.valueOf(obj);
            if (valueOf.codePointCount(0, valueOf.length()) <= i) {
                return true;
            }
            zzgo().zzjg().zzd("Value is too long; discarded. Value kind, name, value length", str, str2, Integer.valueOf(valueOf.length()));
            return false;
        } else if ((obj instanceof Bundle) && z) {
            return true;
        } else {
            int length;
            int i2;
            Object obj2;
            if ((obj instanceof Parcelable[]) && z) {
                Parcelable[] parcelableArr = (Parcelable[]) obj;
                length = parcelableArr.length;
                i2 = 0;
                while (i2 < length) {
                    obj2 = parcelableArr[i2];
                    if (obj2 instanceof Bundle) {
                        i2++;
                    } else {
                        zzgo().zzjg().zze("All Parcelable[] elements must be of type Bundle. Value type, name", obj2.getClass(), str2);
                        return false;
                    }
                }
                return true;
            } else if (!(obj instanceof ArrayList) || !z) {
                return false;
            } else {
                ArrayList arrayList = (ArrayList) obj;
                length = arrayList.size();
                i2 = 0;
                while (i2 < length) {
                    obj2 = arrayList.get(i2);
                    i2++;
                    if (!(obj2 instanceof Bundle)) {
                        zzgo().zzjg().zze("All ArrayList elements must be of type Bundle. Value type, name", obj2.getClass(), str2);
                        return false;
                    }
                }
                return true;
            }
        }
    }

    final boolean zzt(String str, String str2) {
        if (TextUtils.isEmpty(str)) {
            if (TextUtils.isEmpty(str2)) {
                if (!this.zzadj.zzkj()) {
                    return false;
                }
                zzgo().zzjd().zzbx("Missing google_app_id. Firebase Analytics disabled. See https://goo.gl/NAOOOI");
                return false;
            } else if (!zzct(str2)) {
                zzgo().zzjd().zzg("Invalid gma_app_id. Analytics disabled.", zzap.zzbv(str2));
                return false;
            }
        } else if (!zzct(str)) {
            if (!this.zzadj.zzkj()) {
                return false;
            }
            zzgo().zzjd().zzg("Invalid google_app_id. Firebase Analytics disabled. See https://goo.gl/NAOOOI. provided id", zzap.zzbv(str));
            return false;
        }
        return true;
    }

    static boolean zza(String str, String str2, String str3, String str4) {
        boolean isEmpty = TextUtils.isEmpty(str);
        boolean isEmpty2 = TextUtils.isEmpty(str2);
        if (isEmpty || isEmpty2) {
            if (isEmpty && isEmpty2) {
                if (TextUtils.isEmpty(str3) || TextUtils.isEmpty(str4)) {
                    if (TextUtils.isEmpty(str4)) {
                        return false;
                    }
                    return true;
                } else if (str3.equals(str4)) {
                    return false;
                } else {
                    return true;
                }
            } else if (isEmpty || !isEmpty2) {
                if (TextUtils.isEmpty(str3) || !str3.equals(str4)) {
                    return true;
                }
                return false;
            } else if (TextUtils.isEmpty(str4)) {
                return false;
            } else {
                if (TextUtils.isEmpty(str3) || !str3.equals(str4)) {
                    return true;
                }
                return false;
            }
        } else if (str.equals(str2)) {
            return false;
        } else {
            return true;
        }
    }

    private static boolean zzct(String str) {
        Preconditions.checkNotNull(str);
        return str.matches("^(1:\\d+:android:[a-f0-9]+|ca-app-pub-.*)$");
    }

    private static Object zza(int i, Object obj, boolean z) {
        if (obj == null) {
            return null;
        }
        if ((obj instanceof Long) || (obj instanceof Double)) {
            return obj;
        }
        if (obj instanceof Integer) {
            return Long.valueOf((long) ((Integer) obj).intValue());
        }
        if (obj instanceof Byte) {
            return Long.valueOf((long) ((Byte) obj).byteValue());
        }
        if (obj instanceof Short) {
            return Long.valueOf((long) ((Short) obj).shortValue());
        }
        if (obj instanceof Boolean) {
            return Long.valueOf(((Boolean) obj).booleanValue() ? 1 : 0);
        } else if (obj instanceof Float) {
            return Double.valueOf(((Float) obj).doubleValue());
        } else {
            return ((obj instanceof String) || (obj instanceof Character) || (obj instanceof CharSequence)) ? zza(String.valueOf(obj), i, z) : null;
        }
    }

    public static String zza(String str, int i, boolean z) {
        if (str.codePointCount(0, str.length()) <= i) {
            return str;
        }
        if (z) {
            return String.valueOf(str.substring(0, str.offsetByCodePoints(0, i))).concat("...");
        }
        return null;
    }

    final Object zzh(String str, Object obj) {
        int i = 256;
        if ("_ev".equals(str)) {
            return zza(256, obj, true);
        }
        if (!zzcv(str)) {
            i = 100;
        }
        return zza(i, obj, false);
    }

    static Bundle[] zze(Object obj) {
        if (obj instanceof Bundle) {
            return new Bundle[]{(Bundle) obj};
        } else if (obj instanceof Parcelable[]) {
            return (Bundle[]) Arrays.copyOf((Parcelable[]) obj, ((Parcelable[]) obj).length, Bundle[].class);
        } else {
            if (!(obj instanceof ArrayList)) {
                return null;
            }
            ArrayList arrayList = (ArrayList) obj;
            return (Bundle[]) arrayList.toArray(new Bundle[arrayList.size()]);
        }
    }

    final Bundle zza(String str, String str2, Bundle bundle, List<String> list, boolean z, boolean z2) {
        if (bundle == null) {
            return null;
        }
        Bundle bundle2 = new Bundle(bundle);
        int i = 0;
        for (String str3 : bundle.keySet()) {
            int i2 = 0;
            if (list == null || !list.contains(str3)) {
                if (z) {
                    if (!zzr("event param", str3)) {
                        i2 = 3;
                    } else if (!zza("event param", null, str3)) {
                        i2 = 14;
                    } else if (zza("event param", 40, str3)) {
                        i2 = 0;
                    } else {
                        i2 = 3;
                    }
                }
                if (i2 == 0) {
                    if (!zzs("event param", str3)) {
                        i2 = 3;
                    } else if (!zza("event param", null, str3)) {
                        i2 = 14;
                    } else if (zza("event param", 40, str3)) {
                        i2 = 0;
                    } else {
                        i2 = 3;
                    }
                }
            }
            if (i2 != 0) {
                if (zza(bundle2, i2)) {
                    bundle2.putString("_ev", zza(str3, 40, true));
                    if (i2 == 3) {
                        zza(bundle2, (Object) str3);
                    }
                }
                bundle2.remove(str3);
            } else {
                boolean zza;
                Object obj = bundle.get(str3);
                zzaf();
                if (z2) {
                    Object obj2;
                    String str4 = "param";
                    if (obj instanceof Parcelable[]) {
                        i2 = ((Parcelable[]) obj).length;
                    } else if (obj instanceof ArrayList) {
                        i2 = ((ArrayList) obj).size();
                    } else {
                        obj2 = 1;
                        if (obj2 == null) {
                            i2 = 17;
                            if (i2 != 0 || "_ev".equals(str3)) {
                                if (zzcq(str3)) {
                                    i2 = i + 1;
                                    if (i2 > 25) {
                                        zzgo().zzjd().zze("Event can't contain more than 25 params", zzgl().zzbs(str2), zzgl().zzd(bundle));
                                        zza(bundle2, 5);
                                        bundle2.remove(str3);
                                        i = i2;
                                    }
                                } else {
                                    i2 = i;
                                }
                                i = i2;
                            } else {
                                if (zza(bundle2, i2)) {
                                    bundle2.putString("_ev", zza(str3, 40, true));
                                    zza(bundle2, bundle.get(str3));
                                }
                                bundle2.remove(str3);
                            }
                        }
                    }
                    if (i2 > 1000) {
                        zzgo().zzjg().zzd("Parameter array is too long; discarded. Value kind, name, array length", str4, str3, Integer.valueOf(i2));
                        obj2 = null;
                    } else {
                        obj2 = 1;
                    }
                    if (obj2 == null) {
                        i2 = 17;
                        if (i2 != 0) {
                        }
                        if (zzcq(str3)) {
                            i2 = i;
                        } else {
                            i2 = i + 1;
                            if (i2 > 25) {
                                zzgo().zzjd().zze("Event can't contain more than 25 params", zzgl().zzbs(str2), zzgl().zzd(bundle));
                                zza(bundle2, 5);
                                bundle2.remove(str3);
                                i = i2;
                            }
                        }
                        i = i2;
                    }
                }
                if ((zzgq().zzay(str) && zzcv(str2)) || zzcv(str3)) {
                    zza = zza("param", str3, 256, obj, z2);
                } else {
                    zza = zza("param", str3, 100, obj, z2);
                }
                i2 = zza ? 0 : 4;
                if (i2 != 0) {
                }
                if (zzcq(str3)) {
                    i2 = i + 1;
                    if (i2 > 25) {
                        zzgo().zzjd().zze("Event can't contain more than 25 params", zzgl().zzbs(str2), zzgl().zzd(bundle));
                        zza(bundle2, 5);
                        bundle2.remove(str3);
                        i = i2;
                    }
                } else {
                    i2 = i;
                }
                i = i2;
            }
        }
        return bundle2;
    }

    private static boolean zza(Bundle bundle, int i) {
        if (bundle.getLong("_err") != 0) {
            return false;
        }
        bundle.putLong("_err", (long) i);
        return true;
    }

    private static void zza(Bundle bundle, Object obj) {
        Preconditions.checkNotNull(bundle);
        if (obj == null) {
            return;
        }
        if ((obj instanceof String) || (obj instanceof CharSequence)) {
            bundle.putLong("_el", (long) String.valueOf(obj).length());
        }
    }

    private static int zzcu(String str) {
        if ("_ldl".equals(str)) {
            return 2048;
        }
        if ("_id".equals(str)) {
            return 256;
        }
        return 36;
    }

    final int zzi(String str, Object obj) {
        boolean zza;
        if ("_ldl".equals(str)) {
            zza = zza("user property referrer", str, zzcu(str), obj, false);
        } else {
            zza = zza("user property", str, zzcu(str), obj, false);
        }
        if (zza) {
            return 0;
        }
        return 7;
    }

    final Object zzj(String str, Object obj) {
        if ("_ldl".equals(str)) {
            return zza(zzcu(str), obj, true);
        }
        return zza(zzcu(str), obj, false);
    }

    final void zza(Bundle bundle, String str, Object obj) {
        if (bundle != null) {
            if (obj instanceof Long) {
                bundle.putLong(str, ((Long) obj).longValue());
            } else if (obj instanceof String) {
                bundle.putString(str, String.valueOf(obj));
            } else if (obj instanceof Double) {
                bundle.putDouble(str, ((Double) obj).doubleValue());
            } else if (str != null) {
                zzgo().zzji().zze("Not putting event parameter. Invalid value type. name, type", zzgl().zzbt(str), obj != null ? obj.getClass().getSimpleName() : null);
            }
        }
    }

    public final void zza(int i, String str, String str2, int i2) {
        zza(null, i, str, str2, i2);
    }

    final void zza(String str, int i, String str2, String str3, int i2) {
        Bundle bundle = new Bundle();
        zza(bundle, i);
        if (!TextUtils.isEmpty(str2)) {
            bundle.putString(str2, str3);
        }
        if (i == 6 || i == 7 || i == 2) {
            bundle.putLong("_el", (long) i2);
        }
        this.zzadj.zzgr();
        this.zzadj.zzge().logEvent("auto", "_err", bundle);
    }

    static MessageDigest getMessageDigest() {
        int i = 0;
        while (i < 2) {
            try {
                MessageDigest instance = MessageDigest.getInstance("MD5");
                if (instance != null) {
                    return instance;
                }
                i++;
            } catch (NoSuchAlgorithmException e) {
            }
        }
        return null;
    }

    static long zzc(byte[] bArr) {
        long j = null;
        Preconditions.checkNotNull(bArr);
        Preconditions.checkState(bArr.length > 0);
        long j2 = 0;
        int length = bArr.length - 1;
        while (length >= 0 && length >= bArr.length - 8) {
            j2 += (((long) bArr[length]) & 255) << j;
            j += 8;
            length--;
        }
        return j2;
    }

    static boolean zza(Context context, boolean z) {
        Preconditions.checkNotNull(context);
        if (VERSION.SDK_INT >= 24) {
            return zzc(context, "com.google.android.gms.measurement.AppMeasurementJobService");
        }
        return zzc(context, "com.google.android.gms.measurement.AppMeasurementService");
    }

    private static boolean zzc(Context context, String str) {
        try {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager == null) {
                return false;
            }
            ServiceInfo serviceInfo = packageManager.getServiceInfo(new ComponentName(context, str), 0);
            if (serviceInfo == null || !serviceInfo.enabled) {
                return false;
            }
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    final boolean zzx(String str) {
        zzaf();
        if (Wrappers.packageManager(getContext()).checkCallingOrSelfPermission(str) == 0) {
            return true;
        }
        zzgo().zzjk().zzg("Permission not granted", str);
        return false;
    }

    static boolean zzcv(String str) {
        return !TextUtils.isEmpty(str) && str.startsWith("_");
    }

    static boolean zzu(String str, String str2) {
        if (str == null && str2 == null) {
            return true;
        }
        if (str == null) {
            return false;
        }
        return str.equals(str2);
    }

    final boolean zzcw(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        String zzhy = zzgq().zzhy();
        zzgr();
        return zzhy.equals(str);
    }

    final Bundle zze(Bundle bundle) {
        Bundle bundle2 = new Bundle();
        if (bundle != null) {
            for (String str : bundle.keySet()) {
                Object zzh = zzh(str, bundle.get(str));
                if (zzh == null) {
                    zzgo().zzjg().zzg("Param value can't be null", zzgl().zzbt(str));
                } else {
                    zza(bundle2, str, zzh);
                }
            }
        }
        return bundle2;
    }

    final zzad zza(String str, String str2, Bundle bundle, String str3, long j, boolean z, boolean z2) {
        if (TextUtils.isEmpty(str2)) {
            return null;
        }
        if (zzcr(str2) != 0) {
            zzgo().zzjd().zzg("Invalid conditional property event name", zzgl().zzbu(str2));
            throw new IllegalArgumentException();
        }
        Bundle bundle2 = bundle != null ? new Bundle(bundle) : new Bundle();
        bundle2.putString("_o", str3);
        return new zzad(str2, new zzaa(zze(zza(str, str2, bundle2, CollectionUtils.listOf((Object) "_o"), false, false))), str3, j);
    }

    final long zzd(Context context, String str) {
        zzaf();
        Preconditions.checkNotNull(context);
        Preconditions.checkNotEmpty(str);
        PackageManager packageManager = context.getPackageManager();
        MessageDigest messageDigest = getMessageDigest();
        if (messageDigest == null) {
            zzgo().zzjd().zzbx("Could not get MD5 instance");
            return -1;
        }
        if (packageManager != null) {
            try {
                if (!zze(context, str)) {
                    PackageInfo packageInfo = Wrappers.packageManager(context).getPackageInfo(getContext().getPackageName(), 64);
                    if (packageInfo.signatures != null && packageInfo.signatures.length > 0) {
                        return zzc(messageDigest.digest(packageInfo.signatures[0].toByteArray()));
                    }
                    zzgo().zzjg().zzbx("Could not get signatures");
                    return -1;
                }
            } catch (NameNotFoundException e) {
                zzgo().zzjd().zzg("Package name not found", e);
            }
        }
        return 0;
    }

    private final boolean zze(Context context, String str) {
        X500Principal x500Principal = new X500Principal("CN=Android Debug,O=Android,C=US");
        try {
            PackageInfo packageInfo = Wrappers.packageManager(context).getPackageInfo(str, 64);
            if (!(packageInfo == null || packageInfo.signatures == null || packageInfo.signatures.length <= 0)) {
                return ((X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(packageInfo.signatures[0].toByteArray()))).getSubjectX500Principal().equals(x500Principal);
            }
        } catch (CertificateException e) {
            zzgo().zzjd().zzg("Error obtaining certificate", e);
        } catch (NameNotFoundException e2) {
            zzgo().zzjd().zzg("Package name not found", e2);
        }
        return true;
    }

    static byte[] zza(Parcelable parcelable) {
        if (parcelable == null) {
            return null;
        }
        Parcel obtain = Parcel.obtain();
        try {
            parcelable.writeToParcel(obtain, 0);
            byte[] marshall = obtain.marshall();
            return marshall;
        } finally {
            obtain.recycle();
        }
    }

    public static Bundle zzf(Bundle bundle) {
        if (bundle == null) {
            return new Bundle();
        }
        Bundle bundle2 = new Bundle(bundle);
        for (String str : bundle2.keySet()) {
            Object obj = bundle2.get(str);
            if (obj instanceof Bundle) {
                bundle2.putBundle(str, new Bundle((Bundle) obj));
            } else if (obj instanceof Parcelable[]) {
                Parcelable[] parcelableArr = (Parcelable[]) obj;
                for (r2 = 0; r2 < parcelableArr.length; r2++) {
                    if (parcelableArr[r2] instanceof Bundle) {
                        parcelableArr[r2] = new Bundle((Bundle) parcelableArr[r2]);
                    }
                }
            } else if (obj instanceof List) {
                List list = (List) obj;
                for (r2 = 0; r2 < list.size(); r2++) {
                    Object obj2 = list.get(r2);
                    if (obj2 instanceof Bundle) {
                        list.set(r2, new Bundle((Bundle) obj2));
                    }
                }
            }
        }
        return bundle2;
    }

    public static Object zzf(Object obj) {
        ObjectOutputStream objectOutputStream;
        Throwable th;
        if (obj == null) {
            return null;
        }
        ObjectInputStream objectInputStream;
        try {
            OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            try {
                objectOutputStream.writeObject(obj);
                objectOutputStream.flush();
                objectInputStream = new ObjectInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
            } catch (Throwable th2) {
                th = th2;
                objectInputStream = null;
                if (objectOutputStream != null) {
                    objectOutputStream.close();
                }
                if (objectInputStream != null) {
                    objectInputStream.close();
                }
                throw th;
            }
            try {
                Object readObject = objectInputStream.readObject();
                try {
                    objectOutputStream.close();
                    objectInputStream.close();
                    return readObject;
                } catch (IOException e) {
                    return null;
                } catch (ClassNotFoundException e2) {
                    return null;
                }
            } catch (Throwable th3) {
                th = th3;
                if (objectOutputStream != null) {
                    objectOutputStream.close();
                }
                if (objectInputStream != null) {
                    objectInputStream.close();
                }
                throw th;
            }
        } catch (Throwable th4) {
            th = th4;
            objectInputStream = null;
            objectOutputStream = null;
            if (objectOutputStream != null) {
                objectOutputStream.close();
            }
            if (objectInputStream != null) {
                objectInputStream.close();
            }
            throw th;
        }
    }

    public static String zza(String str, String[] strArr, String[] strArr2) {
        Preconditions.checkNotNull(strArr);
        Preconditions.checkNotNull(strArr2);
        int min = Math.min(strArr.length, strArr2.length);
        for (int i = 0; i < min; i++) {
            if (zzu(str, strArr[i])) {
                return strArr2[i];
            }
        }
        return null;
    }

    public final int zzme() {
        if (this.zzaul == null) {
            this.zzaul = Integer.valueOf(GoogleApiAvailabilityLight.getInstance().getApkVersion(getContext()) / 1000);
        }
        return this.zzaul.intValue();
    }

    public static long zzc(long j, long j2) {
        return ((ChunkedTrackBlacklistUtil.DEFAULT_TRACK_BLACKLIST_MS * j2) + j) / 86400000;
    }

    final String zzmf() {
        zzmd().nextBytes(new byte[16]);
        return String.format(Locale.US, "%032x", new Object[]{new BigInteger(1, r0)});
    }

    public final /* bridge */ /* synthetic */ void zzga() {
        super.zzga();
    }

    public final /* bridge */ /* synthetic */ void zzgb() {
        super.zzgb();
    }

    public final /* bridge */ /* synthetic */ void zzgc() {
        super.zzgc();
    }

    public final /* bridge */ /* synthetic */ void zzaf() {
        super.zzaf();
    }

    public final /* bridge */ /* synthetic */ zzx zzgk() {
        return super.zzgk();
    }

    public final /* bridge */ /* synthetic */ Clock zzbx() {
        return super.zzbx();
    }

    public final /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    public final /* bridge */ /* synthetic */ zzan zzgl() {
        return super.zzgl();
    }

    public final /* bridge */ /* synthetic */ zzfk zzgm() {
        return super.zzgm();
    }

    public final /* bridge */ /* synthetic */ zzbo zzgn() {
        return super.zzgn();
    }

    public final /* bridge */ /* synthetic */ zzap zzgo() {
        return super.zzgo();
    }

    public final /* bridge */ /* synthetic */ zzba zzgp() {
        return super.zzgp();
    }

    public final /* bridge */ /* synthetic */ zzn zzgq() {
        return super.zzgq();
    }

    public final /* bridge */ /* synthetic */ zzk zzgr() {
        return super.zzgr();
    }
}
