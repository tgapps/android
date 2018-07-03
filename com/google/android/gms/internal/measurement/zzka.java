package com.google.android.gms.internal.measurement;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ServiceInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import com.google.android.gms.common.GoogleApiAvailabilityLight;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader.ParseException;
import com.google.android.gms.common.util.Clock;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.gms.common.wrappers.Wrappers;
import com.google.android.gms.measurement.AppMeasurement.Event;
import com.google.android.gms.measurement.AppMeasurement.UserProperty;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import javax.security.auth.x500.X500Principal;

public final class zzka extends zzhh {
    private static final String[] zzard = new String[]{"firebase_", "google_", "ga_"};
    private SecureRandom zzare;
    private final AtomicLong zzarf = new AtomicLong(0);
    private int zzarg;
    private Integer zzarh = null;

    zzka(zzgl com_google_android_gms_internal_measurement_zzgl) {
        super(com_google_android_gms_internal_measurement_zzgl);
    }

    static MessageDigest getMessageDigest(String str) {
        int i = 0;
        while (i < 2) {
            try {
                MessageDigest instance = MessageDigest.getInstance(str);
                if (instance != null) {
                    return instance;
                }
                i++;
            } catch (NoSuchAlgorithmException e) {
            }
        }
        return null;
    }

    public static zzko zza(zzkn com_google_android_gms_internal_measurement_zzkn, String str) {
        for (zzko com_google_android_gms_internal_measurement_zzko : com_google_android_gms_internal_measurement_zzkn.zzata) {
            if (com_google_android_gms_internal_measurement_zzko.name.equals(str)) {
                return com_google_android_gms_internal_measurement_zzko;
            }
        }
        return null;
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
        if (!(obj instanceof Boolean)) {
            return obj instanceof Float ? Double.valueOf(((Float) obj).doubleValue()) : ((obj instanceof String) || (obj instanceof Character) || (obj instanceof CharSequence)) ? zza(String.valueOf(obj), i, z) : null;
        } else {
            return Long.valueOf(((Boolean) obj).booleanValue() ? 1 : 0);
        }
    }

    public static String zza(String str, int i, boolean z) {
        return str.codePointCount(0, str.length()) > i ? z ? String.valueOf(str.substring(0, str.offsetByCodePoints(0, i))).concat("...") : null : str;
    }

    public static String zza(String str, String[] strArr, String[] strArr2) {
        Preconditions.checkNotNull(strArr);
        Preconditions.checkNotNull(strArr2);
        int min = Math.min(strArr.length, strArr2.length);
        for (int i = 0; i < min; i++) {
            if (zzs(str, strArr[i])) {
                return strArr2[i];
            }
        }
        return null;
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

    private static boolean zza(Bundle bundle, int i) {
        if (bundle.getLong("_err") != 0) {
            return false;
        }
        bundle.putLong("_err", (long) i);
        return true;
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
            zzge().zzip().zzd("Value is too long; discarded. Value kind, name, value length", str, str2, Integer.valueOf(valueOf.length()));
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
                        zzge().zzip().zze("All Parcelable[] elements must be of type Bundle. Value type, name", obj2.getClass(), str2);
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
                        zzge().zzip().zze("All ArrayList elements must be of type Bundle. Value type, name", obj2.getClass(), str2);
                        return false;
                    }
                }
                return true;
            }
        }
    }

    public static boolean zza(long[] jArr, int i) {
        return i < (jArr.length << 6) && (jArr[i / 64] & (1 << (i % 64))) != 0;
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

    public static long[] zza(BitSet bitSet) {
        int length = (bitSet.length() + 63) / 64;
        long[] jArr = new long[length];
        int i = 0;
        while (i < length) {
            jArr[i] = 0;
            int i2 = 0;
            while (i2 < 64 && (i << 6) + i2 < bitSet.length()) {
                if (bitSet.get((i << 6) + i2)) {
                    jArr[i] = jArr[i] | (1 << i2);
                }
                i2++;
            }
            i++;
        }
        return jArr;
    }

    static zzko[] zza(zzko[] com_google_android_gms_internal_measurement_zzkoArr, String str, Object obj) {
        for (zzko com_google_android_gms_internal_measurement_zzko : com_google_android_gms_internal_measurement_zzkoArr) {
            if (str.equals(com_google_android_gms_internal_measurement_zzko.name)) {
                com_google_android_gms_internal_measurement_zzko.zzate = null;
                com_google_android_gms_internal_measurement_zzko.zzajf = null;
                com_google_android_gms_internal_measurement_zzko.zzarc = null;
                if (obj instanceof Long) {
                    com_google_android_gms_internal_measurement_zzko.zzate = (Long) obj;
                    return com_google_android_gms_internal_measurement_zzkoArr;
                } else if (obj instanceof String) {
                    com_google_android_gms_internal_measurement_zzko.zzajf = (String) obj;
                    return com_google_android_gms_internal_measurement_zzkoArr;
                } else if (!(obj instanceof Double)) {
                    return com_google_android_gms_internal_measurement_zzkoArr;
                } else {
                    com_google_android_gms_internal_measurement_zzko.zzarc = (Double) obj;
                    return com_google_android_gms_internal_measurement_zzkoArr;
                }
            }
        }
        Object obj2 = new zzko[(com_google_android_gms_internal_measurement_zzkoArr.length + 1)];
        System.arraycopy(com_google_android_gms_internal_measurement_zzkoArr, 0, obj2, 0, com_google_android_gms_internal_measurement_zzkoArr.length);
        zzko com_google_android_gms_internal_measurement_zzko2 = new zzko();
        com_google_android_gms_internal_measurement_zzko2.name = str;
        if (obj instanceof Long) {
            com_google_android_gms_internal_measurement_zzko2.zzate = (Long) obj;
        } else if (obj instanceof String) {
            com_google_android_gms_internal_measurement_zzko2.zzajf = (String) obj;
        } else if (obj instanceof Double) {
            com_google_android_gms_internal_measurement_zzko2.zzarc = (Double) obj;
        }
        obj2[com_google_android_gms_internal_measurement_zzkoArr.length] = com_google_android_gms_internal_measurement_zzko2;
        return obj2;
    }

    public static Object zzb(zzkn com_google_android_gms_internal_measurement_zzkn, String str) {
        zzko zza = zza(com_google_android_gms_internal_measurement_zzkn, str);
        if (zza != null) {
            if (zza.zzajf != null) {
                return zza.zzajf;
            }
            if (zza.zzate != null) {
                return zza.zzate;
            }
            if (zza.zzarc != null) {
                return zza.zzarc;
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

    public static boolean zzc(Context context, String str) {
        try {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager == null) {
                return false;
            }
            ServiceInfo serviceInfo = packageManager.getServiceInfo(new ComponentName(context, str), 0);
            return serviceInfo != null && serviceInfo.enabled;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    static boolean zzcc(String str) {
        Preconditions.checkNotEmpty(str);
        return str.charAt(0) != '_' || str.equals("_ep");
    }

    private static int zzch(String str) {
        return "_ldl".equals(str) ? 2048 : "_id".equals(str) ? 256 : 36;
    }

    public static boolean zzci(String str) {
        return !TextUtils.isEmpty(str) && str.startsWith("_");
    }

    static boolean zzck(String str) {
        return str != null && str.matches("(\\+|-)?([0-9]+\\.?[0-9]*|[0-9]*\\.?[0-9]+)") && str.length() <= 310;
    }

    static boolean zzcl(String str) {
        Preconditions.checkNotEmpty(str);
        boolean z = true;
        switch (str.hashCode()) {
            case 94660:
                if (str.equals("_in")) {
                    z = false;
                    break;
                }
                break;
            case 95025:
                if (str.equals("_ug")) {
                    z = true;
                    break;
                }
                break;
            case 95027:
                if (str.equals("_ui")) {
                    z = true;
                    break;
                }
                break;
        }
        switch (z) {
            case false:
            case true:
            case true:
                return true;
            default:
                return false;
        }
    }

    public static boolean zzd(Intent intent) {
        String stringExtra = intent.getStringExtra("android.intent.extra.REFERRER_NAME");
        return "android-app://com.google.android.googlequicksearchbox/https/www.google.com".equals(stringExtra) || "https://www.google.com".equals(stringExtra) || "android-app://com.google.appcrawler".equals(stringExtra);
    }

    static boolean zzd(zzeu com_google_android_gms_internal_measurement_zzeu, zzdz com_google_android_gms_internal_measurement_zzdz) {
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzeu);
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzdz);
        return !TextUtils.isEmpty(com_google_android_gms_internal_measurement_zzdz.zzadm);
    }

    private final boolean zze(Context context, String str) {
        X500Principal x500Principal = new X500Principal("CN=Android Debug,O=Android,C=US");
        try {
            PackageInfo packageInfo = Wrappers.packageManager(context).getPackageInfo(str, 64);
            if (!(packageInfo == null || packageInfo.signatures == null || packageInfo.signatures.length <= 0)) {
                return ((X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(packageInfo.signatures[0].toByteArray()))).getSubjectX500Principal().equals(x500Principal);
            }
        } catch (CertificateException e) {
            zzge().zzim().zzg("Error obtaining certificate", e);
        } catch (NameNotFoundException e2) {
            zzge().zzim().zzg("Package name not found", e2);
        }
        return true;
    }

    public static Bundle[] zze(Object obj) {
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

    public static Object zzf(Object obj) {
        Throwable th;
        if (obj == null) {
            return null;
        }
        ObjectOutputStream objectOutputStream;
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

    private final boolean zzr(String str, String str2) {
        if (str2 == null) {
            zzge().zzim().zzg("Name is required and can't be null. Type", str);
            return false;
        } else if (str2.length() == 0) {
            zzge().zzim().zzg("Name is required and can't be empty. Type", str);
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
                        zzge().zzim().zze("Name must consist of letters, digits or _ (underscores). Type, name", str, str2);
                        return false;
                    }
                }
                return true;
            }
            zzge().zzim().zze("Name must start with a letter or _ (underscore). Type, name", str, str2);
            return false;
        }
    }

    public static boolean zzs(String str, String str2) {
        return (str == null && str2 == null) ? true : str == null ? false : str.equals(str2);
    }

    public final /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    public final Bundle zza(Uri uri) {
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
                zzge().zzip().zzg("Install referrer url isn't a hierarchical URI", e);
            }
        }
        return bundle;
    }

    public final Bundle zza(String str, Bundle bundle, List<String> list, boolean z, boolean z2) {
        if (bundle == null) {
            return null;
        }
        Bundle bundle2 = new Bundle(bundle);
        int i = 0;
        for (String str2 : bundle.keySet()) {
            int i2 = 0;
            if (list == null || !list.contains(str2)) {
                if (z) {
                    i2 = !zzq("event param", str2) ? 3 : !zza("event param", null, str2) ? 14 : !zza("event param", 40, str2) ? 3 : 0;
                }
                if (i2 == 0) {
                    i2 = !zzr("event param", str2) ? 3 : !zza("event param", null, str2) ? 14 : !zza("event param", 40, str2) ? 3 : 0;
                }
            }
            if (i2 != 0) {
                if (zza(bundle2, i2)) {
                    bundle2.putString("_ev", zza(str2, 40, true));
                    if (i2 == 3) {
                        zza(bundle2, (Object) str2);
                    }
                }
                bundle2.remove(str2);
            } else {
                Object obj = bundle.get(str2);
                zzab();
                if (z2) {
                    Object obj2;
                    String str3 = "param";
                    if (obj instanceof Parcelable[]) {
                        i2 = ((Parcelable[]) obj).length;
                    } else if (obj instanceof ArrayList) {
                        i2 = ((ArrayList) obj).size();
                    } else {
                        obj2 = 1;
                        if (obj2 == null) {
                            i2 = 17;
                            if (i2 != 0 || "_ev".equals(str2)) {
                                if (zzcc(str2)) {
                                    i2 = i + 1;
                                    if (i2 > 25) {
                                        zzge().zzim().zze("Event can't contain more than 25 params", zzga().zzbj(str), zzga().zzb(bundle));
                                        zza(bundle2, 5);
                                        bundle2.remove(str2);
                                        i = i2;
                                    }
                                } else {
                                    i2 = i;
                                }
                                i = i2;
                            } else {
                                if (zza(bundle2, i2)) {
                                    bundle2.putString("_ev", zza(str2, 40, true));
                                    zza(bundle2, bundle.get(str2));
                                }
                                bundle2.remove(str2);
                            }
                        }
                    }
                    if (i2 > 1000) {
                        zzge().zzip().zzd("Parameter array is too long; discarded. Value kind, name, array length", str3, str2, Integer.valueOf(i2));
                        obj2 = null;
                    } else {
                        obj2 = 1;
                    }
                    if (obj2 == null) {
                        i2 = 17;
                        if (i2 != 0) {
                        }
                        if (zzcc(str2)) {
                            i2 = i;
                        } else {
                            i2 = i + 1;
                            if (i2 > 25) {
                                zzge().zzim().zze("Event can't contain more than 25 params", zzga().zzbj(str), zzga().zzb(bundle));
                                zza(bundle2, 5);
                                bundle2.remove(str2);
                                i = i2;
                            }
                        }
                        i = i2;
                    }
                }
                boolean zza = ((zzgg().zzaw(zzfv().zzah()) && zzci(str)) || zzci(str2)) ? zza("param", str2, 256, obj, z2) : zza("param", str2, 100, obj, z2);
                i2 = zza ? 0 : 4;
                if (i2 != 0) {
                }
                if (zzcc(str2)) {
                    i2 = i + 1;
                    if (i2 > 25) {
                        zzge().zzim().zze("Event can't contain more than 25 params", zzga().zzbj(str), zzga().zzb(bundle));
                        zza(bundle2, 5);
                        bundle2.remove(str2);
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

    final <T extends Parcelable> T zza(byte[] bArr, Creator<T> creator) {
        T t;
        if (bArr == null) {
            return null;
        }
        Parcel obtain = Parcel.obtain();
        try {
            obtain.unmarshall(bArr, 0, bArr.length);
            obtain.setDataPosition(0);
            t = (Parcelable) creator.createFromParcel(obtain);
            return t;
        } catch (ParseException e) {
            t = zzge().zzim();
            t.log("Failed to load parcelable from buffer");
            return null;
        } finally {
            obtain.recycle();
        }
    }

    final zzeu zza(String str, Bundle bundle, String str2, long j, boolean z, boolean z2) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        if (zzcd(str) != 0) {
            zzge().zzim().zzg("Invalid conditional property event name", zzga().zzbl(str));
            throw new IllegalArgumentException();
        }
        Bundle bundle2 = bundle != null ? new Bundle(bundle) : new Bundle();
        bundle2.putString("_o", str2);
        return new zzeu(str, new zzer(zzd(zza(str, bundle2, CollectionUtils.listOf((Object) "_o"), false, false))), str2, j);
    }

    public final void zza(int i, String str, String str2, int i2) {
        zza(null, i, str, str2, i2);
    }

    public final void zza(Bundle bundle, String str, Object obj) {
        if (bundle != null) {
            if (obj instanceof Long) {
                bundle.putLong(str, ((Long) obj).longValue());
            } else if (obj instanceof String) {
                bundle.putString(str, String.valueOf(obj));
            } else if (obj instanceof Double) {
                bundle.putDouble(str, ((Double) obj).doubleValue());
            } else if (str != null) {
                zzge().zziq().zze("Not putting event parameter. Invalid value type. name, type", zzga().zzbk(str), obj != null ? obj.getClass().getSimpleName() : null);
            }
        }
    }

    public final void zza(zzko com_google_android_gms_internal_measurement_zzko, Object obj) {
        Preconditions.checkNotNull(obj);
        com_google_android_gms_internal_measurement_zzko.zzajf = null;
        com_google_android_gms_internal_measurement_zzko.zzate = null;
        com_google_android_gms_internal_measurement_zzko.zzarc = null;
        if (obj instanceof String) {
            com_google_android_gms_internal_measurement_zzko.zzajf = (String) obj;
        } else if (obj instanceof Long) {
            com_google_android_gms_internal_measurement_zzko.zzate = (Long) obj;
        } else if (obj instanceof Double) {
            com_google_android_gms_internal_measurement_zzko.zzarc = (Double) obj;
        } else {
            zzge().zzim().zzg("Ignoring invalid (type) event param value", obj);
        }
    }

    public final void zza(zzks com_google_android_gms_internal_measurement_zzks, Object obj) {
        Preconditions.checkNotNull(obj);
        com_google_android_gms_internal_measurement_zzks.zzajf = null;
        com_google_android_gms_internal_measurement_zzks.zzate = null;
        com_google_android_gms_internal_measurement_zzks.zzarc = null;
        if (obj instanceof String) {
            com_google_android_gms_internal_measurement_zzks.zzajf = (String) obj;
        } else if (obj instanceof Long) {
            com_google_android_gms_internal_measurement_zzks.zzate = (Long) obj;
        } else if (obj instanceof Double) {
            com_google_android_gms_internal_measurement_zzks.zzarc = (Double) obj;
        } else {
            zzge().zzim().zzg("Ignoring invalid (type) user attribute value", obj);
        }
    }

    public final void zza(String str, int i, String str2, String str3, int i2) {
        Bundle bundle = new Bundle();
        zza(bundle, i);
        if (!TextUtils.isEmpty(str2)) {
            bundle.putString(str2, str3);
        }
        if (i == 6 || i == 7 || i == 2) {
            bundle.putLong("_el", (long) i2);
        }
        this.zzacw.zzfu().logEvent("auto", "_err", bundle);
    }

    public final boolean zza(long j, long j2) {
        return j == 0 || j2 <= 0 || Math.abs(zzbt().currentTimeMillis() - j) > j2;
    }

    final boolean zza(String str, int i, String str2) {
        if (str2 == null) {
            zzge().zzim().zzg("Name is required and can't be null. Type", str);
            return false;
        } else if (str2.codePointCount(0, str2.length()) <= i) {
            return true;
        } else {
            zzge().zzim().zzd("Name is too long. Type, maximum supported length, name", str, Integer.valueOf(i), str2);
            return false;
        }
    }

    final boolean zza(String str, String[] strArr, String str2) {
        if (str2 == null) {
            zzge().zzim().zzg("Name is required and can't be null. Type", str);
            return false;
        }
        boolean z;
        Preconditions.checkNotNull(str2);
        for (String startsWith : zzard) {
            if (str2.startsWith(startsWith)) {
                z = true;
                break;
            }
        }
        z = false;
        if (z) {
            zzge().zzim().zze("Name starts with reserved prefix. Type, name", str, str2);
            return false;
        }
        if (strArr != null) {
            Preconditions.checkNotNull(strArr);
            for (String startsWith2 : strArr) {
                if (zzs(str2, startsWith2)) {
                    z = true;
                    break;
                }
            }
            z = false;
            if (z) {
                zzge().zzim().zze("Name is reserved. Type, name", str, str2);
                return false;
            }
        }
        return true;
    }

    public final byte[] zza(byte[] bArr) throws IOException {
        try {
            OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            GZIPOutputStream gZIPOutputStream = new GZIPOutputStream(byteArrayOutputStream);
            gZIPOutputStream.write(bArr);
            gZIPOutputStream.close();
            byteArrayOutputStream.close();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            zzge().zzim().zzg("Failed to gzip content", e);
            throw e;
        }
    }

    public final /* bridge */ /* synthetic */ void zzab() {
        super.zzab();
    }

    public final byte[] zzb(zzkp com_google_android_gms_internal_measurement_zzkp) {
        try {
            byte[] bArr = new byte[com_google_android_gms_internal_measurement_zzkp.zzvm()];
            zzabw zzb = zzabw.zzb(bArr, 0, bArr.length);
            com_google_android_gms_internal_measurement_zzkp.zza(zzb);
            zzb.zzve();
            return bArr;
        } catch (IOException e) {
            zzge().zzim().zzg("Data loss. Failed to serialize batch", e);
            return null;
        }
    }

    public final byte[] zzb(byte[] bArr) throws IOException {
        try {
            InputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
            GZIPInputStream gZIPInputStream = new GZIPInputStream(byteArrayInputStream);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] bArr2 = new byte[1024];
            while (true) {
                int read = gZIPInputStream.read(bArr2);
                if (read > 0) {
                    byteArrayOutputStream.write(bArr2, 0, read);
                } else {
                    gZIPInputStream.close();
                    byteArrayInputStream.close();
                    return byteArrayOutputStream.toByteArray();
                }
            }
        } catch (IOException e) {
            zzge().zzim().zzg("Failed to ungzip content", e);
            throw e;
        }
    }

    public final /* bridge */ /* synthetic */ Clock zzbt() {
        return super.zzbt();
    }

    public final int zzcd(String str) {
        return !zzr("event", str) ? 2 : !zza("event", Event.zzacx, str) ? 13 : zza("event", 40, str) ? 0 : 2;
    }

    public final int zzce(String str) {
        return !zzq("user property", str) ? 6 : !zza("user property", UserProperty.zzadb, str) ? 15 : zza("user property", 24, str) ? 0 : 6;
    }

    public final int zzcf(String str) {
        return !zzr("user property", str) ? 6 : !zza("user property", UserProperty.zzadb, str) ? 15 : zza("user property", 24, str) ? 0 : 6;
    }

    public final boolean zzcg(String str) {
        if (TextUtils.isEmpty(str)) {
            zzge().zzim().log("Missing google_app_id. Firebase Analytics disabled. See https://goo.gl/NAOOOI");
            return false;
        }
        Preconditions.checkNotNull(str);
        if (str.matches("^1:\\d+:android:[a-f0-9]+$")) {
            return true;
        }
        zzge().zzim().zzg("Invalid google_app_id. Firebase Analytics disabled. See https://goo.gl/NAOOOI. provided id", str);
        return false;
    }

    public final boolean zzcj(String str) {
        return TextUtils.isEmpty(str) ? false : zzgg().zzhj().equals(str);
    }

    final long zzd(Context context, String str) {
        zzab();
        Preconditions.checkNotNull(context);
        Preconditions.checkNotEmpty(str);
        PackageManager packageManager = context.getPackageManager();
        MessageDigest messageDigest = getMessageDigest("MD5");
        if (messageDigest == null) {
            zzge().zzim().log("Could not get MD5 instance");
            return -1;
        }
        if (packageManager != null) {
            try {
                if (!zze(context, str)) {
                    PackageInfo packageInfo = Wrappers.packageManager(context).getPackageInfo(getContext().getPackageName(), 64);
                    if (packageInfo.signatures != null && packageInfo.signatures.length > 0) {
                        return zzc(messageDigest.digest(packageInfo.signatures[0].toByteArray()));
                    }
                    zzge().zzip().log("Could not get signatures");
                    return -1;
                }
            } catch (NameNotFoundException e) {
                zzge().zzim().zzg("Package name not found", e);
            }
        }
        return 0;
    }

    final Bundle zzd(Bundle bundle) {
        Bundle bundle2 = new Bundle();
        if (bundle != null) {
            for (String str : bundle.keySet()) {
                Object zzh = zzh(str, bundle.get(str));
                if (zzh == null) {
                    zzge().zzip().zzg("Param value can't be null", zzga().zzbk(str));
                } else {
                    zza(bundle2, str, zzh);
                }
            }
        }
        return bundle2;
    }

    public final /* bridge */ /* synthetic */ void zzfr() {
        super.zzfr();
    }

    public final /* bridge */ /* synthetic */ void zzfs() {
        super.zzfs();
    }

    public final /* bridge */ /* synthetic */ zzdu zzft() {
        return super.zzft();
    }

    public final /* bridge */ /* synthetic */ zzhk zzfu() {
        return super.zzfu();
    }

    public final /* bridge */ /* synthetic */ zzfb zzfv() {
        return super.zzfv();
    }

    public final /* bridge */ /* synthetic */ zzeo zzfw() {
        return super.zzfw();
    }

    public final /* bridge */ /* synthetic */ zzii zzfx() {
        return super.zzfx();
    }

    public final /* bridge */ /* synthetic */ zzif zzfy() {
        return super.zzfy();
    }

    public final /* bridge */ /* synthetic */ zzfc zzfz() {
        return super.zzfz();
    }

    public final /* bridge */ /* synthetic */ zzfe zzga() {
        return super.zzga();
    }

    public final /* bridge */ /* synthetic */ zzka zzgb() {
        return super.zzgb();
    }

    public final /* bridge */ /* synthetic */ zzjh zzgc() {
        return super.zzgc();
    }

    public final /* bridge */ /* synthetic */ zzgg zzgd() {
        return super.zzgd();
    }

    public final /* bridge */ /* synthetic */ zzfg zzge() {
        return super.zzge();
    }

    public final /* bridge */ /* synthetic */ zzfr zzgf() {
        return super.zzgf();
    }

    public final /* bridge */ /* synthetic */ zzef zzgg() {
        return super.zzgg();
    }

    public final Object zzh(String str, Object obj) {
        int i = 256;
        if ("_ev".equals(str)) {
            return zza(256, obj, true);
        }
        if (!zzci(str)) {
            i = 100;
        }
        return zza(i, obj, false);
    }

    protected final boolean zzhf() {
        return true;
    }

    public final int zzi(String str, Object obj) {
        return "_ldl".equals(str) ? zza("user property referrer", str, zzch(str), obj, false) : zza("user property", str, zzch(str), obj, false) ? 0 : 7;
    }

    protected final void zzih() {
        zzab();
        SecureRandom secureRandom = new SecureRandom();
        long nextLong = secureRandom.nextLong();
        if (nextLong == 0) {
            nextLong = secureRandom.nextLong();
            if (nextLong == 0) {
                zzge().zzip().log("Utils falling back to Random for random id");
            }
        }
        this.zzarf.set(nextLong);
    }

    public final Object zzj(String str, Object obj) {
        return "_ldl".equals(str) ? zza(zzch(str), obj, true) : zza(zzch(str), obj, false);
    }

    public final long zzlb() {
        long nextLong;
        if (this.zzarf.get() == 0) {
            synchronized (this.zzarf) {
                nextLong = new Random(System.nanoTime() ^ zzbt().currentTimeMillis()).nextLong();
                int i = this.zzarg + 1;
                this.zzarg = i;
                nextLong += (long) i;
            }
        } else {
            synchronized (this.zzarf) {
                this.zzarf.compareAndSet(-1, 1);
                nextLong = this.zzarf.getAndIncrement();
            }
        }
        return nextLong;
    }

    final SecureRandom zzlc() {
        zzab();
        if (this.zzare == null) {
            this.zzare = new SecureRandom();
        }
        return this.zzare;
    }

    public final int zzld() {
        if (this.zzarh == null) {
            this.zzarh = Integer.valueOf(GoogleApiAvailabilityLight.getInstance().getApkVersion(getContext()) / 1000);
        }
        return this.zzarh.intValue();
    }

    final boolean zzq(String str, String str2) {
        if (str2 == null) {
            zzge().zzim().zzg("Name is required and can't be null. Type", str);
            return false;
        } else if (str2.length() == 0) {
            zzge().zzim().zzg("Name is required and can't be empty. Type", str);
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
                        zzge().zzim().zze("Name must consist of letters, digits or _ (underscores). Type, name", str, str2);
                        return false;
                    }
                }
                return true;
            }
            zzge().zzim().zze("Name must start with a letter. Type, name", str, str2);
            return false;
        }
    }

    public final boolean zzx(String str) {
        zzab();
        if (Wrappers.packageManager(getContext()).checkCallingOrSelfPermission(str) == 0) {
            return true;
        }
        zzge().zzis().zzg("Permission not granted", str);
        return false;
    }
}
