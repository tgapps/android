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
import android.text.TextUtils;
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
import java.io.InputStream;
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

public final class zzjv extends zzhk {
    private static final String[] zzaqy = new String[]{"firebase_", "google_", "ga_"};
    private SecureRandom zzaqz;
    private final AtomicLong zzara = new AtomicLong(0);
    private int zzarb;
    private Integer zzarc = null;

    zzjv(zzgl com_google_android_gms_internal_measurement_zzgl) {
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

    public static zzkj zza(zzki com_google_android_gms_internal_measurement_zzki, String str) {
        for (zzkj com_google_android_gms_internal_measurement_zzkj : com_google_android_gms_internal_measurement_zzki.zzasv) {
            if (com_google_android_gms_internal_measurement_zzkj.name.equals(str)) {
                return com_google_android_gms_internal_measurement_zzkj;
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
        if (obj instanceof Boolean) {
            return Long.valueOf(((Boolean) obj).booleanValue() ? 1 : 0);
        } else if (obj instanceof Float) {
            return Double.valueOf(((Float) obj).doubleValue());
        } else {
            if (!((obj instanceof String) || (obj instanceof Character))) {
                if (!(obj instanceof CharSequence)) {
                    return null;
                }
            }
            return zza(String.valueOf(obj), i, z);
        }
    }

    public static String zza(String str, int i, boolean z) {
        if (str.codePointCount(0, str.length()) > i) {
            if (z) {
                return String.valueOf(str.substring(0, str.offsetByCodePoints(0, i))).concat("...");
            }
            str = null;
        }
        return str;
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
        if (!((obj instanceof String) || (obj instanceof Character))) {
            if (!(obj instanceof CharSequence)) {
                if ((obj instanceof Bundle) && z) {
                    return true;
                }
                int length;
                Object obj2;
                if ((obj instanceof Parcelable[]) && z) {
                    Parcelable[] parcelableArr = (Parcelable[]) obj;
                    length = parcelableArr.length;
                    i = 0;
                    while (i < length) {
                        obj2 = parcelableArr[i];
                        if (obj2 instanceof Bundle) {
                            i++;
                        } else {
                            zzgg().zzin().zze("All Parcelable[] elements must be of type Bundle. Value type, name", obj2.getClass(), str2);
                            return false;
                        }
                    }
                    return true;
                } else if (!(obj instanceof ArrayList) || !z) {
                    return false;
                } else {
                    ArrayList arrayList = (ArrayList) obj;
                    length = arrayList.size();
                    i = 0;
                    while (i < length) {
                        obj2 = arrayList.get(i);
                        i++;
                        if (!(obj2 instanceof Bundle)) {
                            zzgg().zzin().zze("All ArrayList elements must be of type Bundle. Value type, name", obj2.getClass(), str2);
                            return false;
                        }
                    }
                    return true;
                }
            }
        }
        String valueOf = String.valueOf(obj);
        if (valueOf.codePointCount(0, valueOf.length()) > i) {
            zzgg().zzin().zzd("Value is too long; discarded. Value kind, name, value length", str, str2, Integer.valueOf(valueOf.length()));
            return false;
        }
        return true;
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
        for (int i = 0; i < length; i++) {
            jArr[i] = 0;
            for (int i2 = 0; i2 < 64; i2++) {
                int i3 = (i << 6) + i2;
                if (i3 >= bitSet.length()) {
                    break;
                }
                if (bitSet.get(i3)) {
                    jArr[i] = jArr[i] | (1 << i2);
                }
            }
        }
        return jArr;
    }

    static zzkj[] zza(zzkj[] com_google_android_gms_internal_measurement_zzkjArr, String str, Object obj) {
        for (zzkj com_google_android_gms_internal_measurement_zzkj : com_google_android_gms_internal_measurement_zzkjArr) {
            if (str.equals(com_google_android_gms_internal_measurement_zzkj.name)) {
                com_google_android_gms_internal_measurement_zzkj.zzasz = null;
                com_google_android_gms_internal_measurement_zzkj.zzajf = null;
                com_google_android_gms_internal_measurement_zzkj.zzaqx = null;
                if (obj instanceof Long) {
                    com_google_android_gms_internal_measurement_zzkj.zzasz = (Long) obj;
                    return com_google_android_gms_internal_measurement_zzkjArr;
                } else if (obj instanceof String) {
                    com_google_android_gms_internal_measurement_zzkj.zzajf = (String) obj;
                    return com_google_android_gms_internal_measurement_zzkjArr;
                } else {
                    if (obj instanceof Double) {
                        com_google_android_gms_internal_measurement_zzkj.zzaqx = (Double) obj;
                    }
                    return com_google_android_gms_internal_measurement_zzkjArr;
                }
            }
        }
        Object obj2 = new zzkj[(com_google_android_gms_internal_measurement_zzkjArr.length + 1)];
        System.arraycopy(com_google_android_gms_internal_measurement_zzkjArr, 0, obj2, 0, com_google_android_gms_internal_measurement_zzkjArr.length);
        zzkj com_google_android_gms_internal_measurement_zzkj2 = new zzkj();
        com_google_android_gms_internal_measurement_zzkj2.name = str;
        if (obj instanceof Long) {
            com_google_android_gms_internal_measurement_zzkj2.zzasz = (Long) obj;
        } else if (obj instanceof String) {
            com_google_android_gms_internal_measurement_zzkj2.zzajf = (String) obj;
        } else if (obj instanceof Double) {
            com_google_android_gms_internal_measurement_zzkj2.zzaqx = (Double) obj;
        }
        obj2[com_google_android_gms_internal_measurement_zzkjArr.length] = com_google_android_gms_internal_measurement_zzkj2;
        return obj2;
    }

    public static Object zzb(zzki com_google_android_gms_internal_measurement_zzki, String str) {
        zzkj zza = zza(com_google_android_gms_internal_measurement_zzki, str);
        if (zza != null) {
            if (zza.zzajf != null) {
                return zza.zzajf;
            }
            if (zza.zzasz != null) {
                return zza.zzasz;
            }
            if (zza.zzaqx != null) {
                return zza.zzaqx;
            }
        }
        return null;
    }

    static boolean zzbv(String str) {
        Preconditions.checkNotEmpty(str);
        if (str.charAt(0) == '_') {
            if (!str.equals("_ep")) {
                return false;
            }
        }
        return true;
    }

    static long zzc(byte[] bArr) {
        Preconditions.checkNotNull(bArr);
        long j = null;
        Preconditions.checkState(bArr.length > 0);
        long j2 = 0;
        int length = bArr.length - 1;
        while (length >= 0 && length >= bArr.length - 8) {
            j += 8;
            length--;
            j2 += (((long) bArr[length]) & 255) << j;
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
            if (serviceInfo != null && serviceInfo.enabled) {
                return true;
            }
            return false;
        } catch (NameNotFoundException e) {
        }
    }

    private static int zzca(String str) {
        return "_ldl".equals(str) ? 2048 : "_id".equals(str) ? 256 : 36;
    }

    public static boolean zzcb(String str) {
        return !TextUtils.isEmpty(str) && str.startsWith("_");
    }

    static boolean zzcd(String str) {
        return str != null && str.matches("(\\+|-)?([0-9]+\\.?[0-9]*|[0-9]*\\.?[0-9]+)") && str.length() <= 310;
    }

    static boolean zzcg(String str) {
        boolean z;
        Preconditions.checkNotEmpty(str);
        int hashCode = str.hashCode();
        if (hashCode != 94660) {
            if (hashCode != 95025) {
                if (hashCode == 95027) {
                    if (str.equals("_ui")) {
                        z = true;
                        switch (z) {
                            case false:
                            case true:
                            case true:
                                return true;
                            default:
                                return false;
                        }
                    }
                }
            } else if (str.equals("_ug")) {
                z = true;
                switch (z) {
                    case false:
                    case true:
                    case true:
                        return true;
                    default:
                        return false;
                }
            }
        } else if (str.equals("_in")) {
            z = false;
            switch (z) {
                case false:
                case true:
                case true:
                    return true;
                default:
                    return false;
            }
        }
        z = true;
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
        if (!("android-app://com.google.android.googlequicksearchbox/https/www.google.com".equals(stringExtra) || "https://www.google.com".equals(stringExtra))) {
            if (!"android-app://com.google.appcrawler".equals(stringExtra)) {
                return false;
            }
        }
        return true;
    }

    private final boolean zze(Context context, String str) {
        Object e;
        zzfi zzil;
        String str2;
        X500Principal x500Principal = new X500Principal("CN=Android Debug,O=Android,C=US");
        try {
            PackageInfo packageInfo = Wrappers.packageManager(context).getPackageInfo(str, 64);
            if (!(packageInfo == null || packageInfo.signatures == null || packageInfo.signatures.length <= 0)) {
                return ((X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(packageInfo.signatures[0].toByteArray()))).getSubjectX500Principal().equals(x500Principal);
            }
        } catch (CertificateException e2) {
            e = e2;
            zzil = zzgg().zzil();
            str2 = "Error obtaining certificate";
            zzil.zzg(str2, e);
            return true;
        } catch (NameNotFoundException e3) {
            e = e3;
            zzil = zzgg().zzil();
            str2 = "Package name not found";
            zzil.zzg(str2, e);
            return true;
        }
        return true;
    }

    public static Bundle[] zze(Object obj) {
        if (obj instanceof Bundle) {
            return new Bundle[]{(Bundle) obj};
        }
        Object[] copyOf;
        if (obj instanceof Parcelable[]) {
            Parcelable[] parcelableArr = (Parcelable[]) obj;
            copyOf = Arrays.copyOf(parcelableArr, parcelableArr.length, Bundle[].class);
        } else if (!(obj instanceof ArrayList)) {
            return null;
        } else {
            ArrayList arrayList = (ArrayList) obj;
            copyOf = arrayList.toArray(new Bundle[arrayList.size()]);
        }
        return (Bundle[]) copyOf;
    }

    public static java.lang.Object zzf(java.lang.Object r4) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:25:0x0043 in {2, 11, 13, 15, 17, 19, 21, 23, 24} preds:[]
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:129)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:38)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
        /*
        r0 = 0;
        if (r4 != 0) goto L_0x0004;
    L_0x0003:
        return r0;
    L_0x0004:
        r1 = new java.io.ByteArrayOutputStream;	 Catch:{ all -> 0x0032 }
        r1.<init>();	 Catch:{ all -> 0x0032 }
        r2 = new java.io.ObjectOutputStream;	 Catch:{ all -> 0x0032 }
        r2.<init>(r1);	 Catch:{ all -> 0x0032 }
        r2.writeObject(r4);	 Catch:{ all -> 0x002f }
        r2.flush();	 Catch:{ all -> 0x002f }
        r4 = new java.io.ObjectInputStream;	 Catch:{ all -> 0x002f }
        r3 = new java.io.ByteArrayInputStream;	 Catch:{ all -> 0x002f }
        r1 = r1.toByteArray();	 Catch:{ all -> 0x002f }
        r3.<init>(r1);	 Catch:{ all -> 0x002f }
        r4.<init>(r3);	 Catch:{ all -> 0x002f }
        r1 = r4.readObject();	 Catch:{ all -> 0x002d }
        r2.close();	 Catch:{ IOException -> 0x003b, IOException -> 0x003b }
        r4.close();	 Catch:{ IOException -> 0x003b, IOException -> 0x003b }
        return r1;	 Catch:{ IOException -> 0x003b, IOException -> 0x003b }
    L_0x002d:
        r1 = move-exception;	 Catch:{ IOException -> 0x003b, IOException -> 0x003b }
        goto L_0x0035;	 Catch:{ IOException -> 0x003b, IOException -> 0x003b }
    L_0x002f:
        r1 = move-exception;	 Catch:{ IOException -> 0x003b, IOException -> 0x003b }
        r4 = r0;	 Catch:{ IOException -> 0x003b, IOException -> 0x003b }
        goto L_0x0035;	 Catch:{ IOException -> 0x003b, IOException -> 0x003b }
    L_0x0032:
        r1 = move-exception;	 Catch:{ IOException -> 0x003b, IOException -> 0x003b }
        r4 = r0;	 Catch:{ IOException -> 0x003b, IOException -> 0x003b }
        r2 = r4;	 Catch:{ IOException -> 0x003b, IOException -> 0x003b }
    L_0x0035:
        if (r2 == 0) goto L_0x003d;	 Catch:{ IOException -> 0x003b, IOException -> 0x003b }
    L_0x0037:
        r2.close();	 Catch:{ IOException -> 0x003b, IOException -> 0x003b }
        goto L_0x003d;	 Catch:{ IOException -> 0x003b, IOException -> 0x003b }
    L_0x003b:
        r4 = move-exception;	 Catch:{ IOException -> 0x003b, IOException -> 0x003b }
        return r0;	 Catch:{ IOException -> 0x003b, IOException -> 0x003b }
    L_0x003d:
        if (r4 == 0) goto L_0x0042;	 Catch:{ IOException -> 0x003b, IOException -> 0x003b }
    L_0x003f:
        r4.close();	 Catch:{ IOException -> 0x003b, IOException -> 0x003b }
    L_0x0042:
        throw r1;	 Catch:{ IOException -> 0x003b, IOException -> 0x003b }
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzjv.zzf(java.lang.Object):java.lang.Object");
    }

    private final boolean zzr(String str, String str2) {
        if (str2 == null) {
            zzgg().zzil().zzg("Name is required and can't be null. Type", str);
            return false;
        } else if (str2.length() == 0) {
            zzgg().zzil().zzg("Name is required and can't be empty. Type", str);
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
                        zzgg().zzil().zze("Name must consist of letters, digits or _ (underscores). Type, name", str, str2);
                        return false;
                    }
                }
                return true;
            }
            zzgg().zzil().zze("Name must start with a letter or _ (underscore). Type, name", str, str2);
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
        if (uri == null) {
            return null;
        }
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
                queryParameter = null;
                queryParameter2 = queryParameter;
                queryParameter3 = queryParameter2;
                queryParameter4 = queryParameter3;
            }
            if (TextUtils.isEmpty(queryParameter) && TextUtils.isEmpty(queryParameter2) && TextUtils.isEmpty(queryParameter3)) {
                if (TextUtils.isEmpty(queryParameter4)) {
                    return null;
                }
            }
            Bundle bundle = new Bundle();
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
            queryParameter = uri.getQueryParameter("utm_term");
            if (!TextUtils.isEmpty(queryParameter)) {
                bundle.putString("term", queryParameter);
            }
            queryParameter = uri.getQueryParameter("utm_content");
            if (!TextUtils.isEmpty(queryParameter)) {
                bundle.putString("content", queryParameter);
            }
            queryParameter = uri.getQueryParameter("aclid");
            if (!TextUtils.isEmpty(queryParameter)) {
                bundle.putString("aclid", queryParameter);
            }
            queryParameter = uri.getQueryParameter("cp1");
            if (!TextUtils.isEmpty(queryParameter)) {
                bundle.putString("cp1", queryParameter);
            }
            Object queryParameter5 = uri.getQueryParameter("anid");
            if (!TextUtils.isEmpty(queryParameter5)) {
                bundle.putString("anid", queryParameter5);
            }
            return bundle;
        } catch (UnsupportedOperationException e) {
            zzgg().zzin().zzg("Install referrer url isn't a hierarchical URI", e);
            return null;
        }
    }

    public final Bundle zza(String str, Bundle bundle, List<String> list, boolean z, boolean z2) {
        zzjv com_google_android_gms_internal_measurement_zzjv = this;
        Bundle bundle2 = bundle;
        List<String> list2 = list;
        String[] strArr = null;
        if (bundle2 == null) {
            return null;
        }
        Bundle bundle3 = new Bundle(bundle2);
        int i = 0;
        for (String str2 : bundle.keySet()) {
            int i2;
            Object obj;
            String str3;
            boolean z3;
            int i3;
            boolean z4;
            StringBuilder stringBuilder;
            String str4;
            String str5;
            int i4;
            zzjv com_google_android_gms_internal_measurement_zzjv2;
            String str6;
            int i5;
            if (list2 != null) {
                if (!list2.contains(str2)) {
                }
                i2 = 0;
                if (i2 == 0) {
                    if (zza(bundle3, i2)) {
                        bundle3.putString("_ev", zza(str2, 40, true));
                        if (i2 == 3) {
                            zza(bundle3, (Object) str2);
                        }
                    }
                    bundle3.remove(str2);
                } else {
                    obj = bundle2.get(str2);
                    zzab();
                    if (z2) {
                        str3 = "param";
                        if (obj instanceof Parcelable[]) {
                            if (obj instanceof ArrayList) {
                                i2 = ((ArrayList) obj).size();
                            }
                            z3 = true;
                            if (!z3) {
                                i3 = 17;
                                z4 = true;
                                if (i3 != 0 || "_ev".equals(str2)) {
                                    if (zzbv(str2)) {
                                        i++;
                                        if (i > 25) {
                                            stringBuilder = new StringBuilder(48);
                                            stringBuilder.append("Event can't contain more than 25 params");
                                            zzgg().zzil().zze(stringBuilder.toString(), zzgb().zzbe(str), zzgb().zzb(bundle2));
                                            zza(bundle3, 5);
                                            bundle3.remove(str2);
                                        }
                                    }
                                    str4 = str;
                                } else {
                                    if (zza(bundle3, i3)) {
                                        bundle3.putString("_ev", zza(str2, 40, z4));
                                        zza(bundle3, bundle2.get(str2));
                                    }
                                    bundle3.remove(str2);
                                }
                            }
                        } else {
                            i2 = ((Parcelable[]) obj).length;
                        }
                        if (i2 > 1000) {
                            zzgg().zzin().zzd("Parameter array is too long; discarded. Value kind, name, array length", str3, str2, Integer.valueOf(i2));
                            z3 = false;
                            if (z3) {
                                i3 = 17;
                                z4 = true;
                                if (i3 != 0) {
                                }
                                if (zzbv(str2)) {
                                    i++;
                                    if (i > 25) {
                                        stringBuilder = new StringBuilder(48);
                                        stringBuilder.append("Event can't contain more than 25 params");
                                        zzgg().zzil().zze(stringBuilder.toString(), zzgb().zzbe(str), zzgb().zzb(bundle2));
                                        zza(bundle3, 5);
                                        bundle3.remove(str2);
                                    }
                                }
                                str4 = str;
                            }
                        }
                        z3 = true;
                        if (z3) {
                            i3 = 17;
                            z4 = true;
                            if (i3 != 0) {
                            }
                            if (zzbv(str2)) {
                                i++;
                                if (i > 25) {
                                    stringBuilder = new StringBuilder(48);
                                    stringBuilder.append("Event can't contain more than 25 params");
                                    zzgg().zzil().zze(stringBuilder.toString(), zzgb().zzbe(str), zzgb().zzb(bundle2));
                                    zza(bundle3, 5);
                                    bundle3.remove(str2);
                                }
                            }
                            str4 = str;
                        }
                    }
                    if ((zzgi().zzd(zzfv().zzah(), zzew.zzahz) || !zzcb(str)) && !zzcb(str2)) {
                        z4 = true;
                        str5 = "param";
                        i4 = 100;
                        com_google_android_gms_internal_measurement_zzjv2 = com_google_android_gms_internal_measurement_zzjv;
                        str6 = str2;
                    } else {
                        str5 = "param";
                        i4 = 256;
                        com_google_android_gms_internal_measurement_zzjv2 = com_google_android_gms_internal_measurement_zzjv;
                        str6 = str2;
                        z4 = true;
                    }
                    i3 = com_google_android_gms_internal_measurement_zzjv2.zza(str5, str6, i4, obj, z2) ? 0 : 4;
                    if (i3 != 0) {
                    }
                    if (zzbv(str2)) {
                        i++;
                        if (i > 25) {
                            stringBuilder = new StringBuilder(48);
                            stringBuilder.append("Event can't contain more than 25 params");
                            zzgg().zzil().zze(stringBuilder.toString(), zzgb().zzbe(str), zzgb().zzb(bundle2));
                            zza(bundle3, 5);
                            bundle3.remove(str2);
                        }
                    }
                    str4 = str;
                }
                strArr = null;
            }
            i2 = 14;
            if (z) {
                if (zzq("event param", str2)) {
                    if (!zza("event param", strArr, str2)) {
                        i5 = 14;
                        if (i5 == 0) {
                            if (zzr("event param", str2)) {
                                if (!zza("event param", strArr, str2)) {
                                    if (!zza("event param", 40, str2)) {
                                    }
                                    i2 = 0;
                                }
                            }
                            i2 = 3;
                        } else {
                            i2 = i5;
                        }
                        if (i2 == 0) {
                            obj = bundle2.get(str2);
                            zzab();
                            if (z2) {
                                str3 = "param";
                                if (obj instanceof Parcelable[]) {
                                    if (obj instanceof ArrayList) {
                                        i2 = ((ArrayList) obj).size();
                                    }
                                    z3 = true;
                                    if (z3) {
                                        i3 = 17;
                                        z4 = true;
                                        if (i3 != 0) {
                                        }
                                        if (zzbv(str2)) {
                                            i++;
                                            if (i > 25) {
                                                stringBuilder = new StringBuilder(48);
                                                stringBuilder.append("Event can't contain more than 25 params");
                                                zzgg().zzil().zze(stringBuilder.toString(), zzgb().zzbe(str), zzgb().zzb(bundle2));
                                                zza(bundle3, 5);
                                                bundle3.remove(str2);
                                            }
                                        }
                                        str4 = str;
                                    }
                                } else {
                                    i2 = ((Parcelable[]) obj).length;
                                }
                                if (i2 > 1000) {
                                    zzgg().zzin().zzd("Parameter array is too long; discarded. Value kind, name, array length", str3, str2, Integer.valueOf(i2));
                                    z3 = false;
                                    if (z3) {
                                        i3 = 17;
                                        z4 = true;
                                        if (i3 != 0) {
                                        }
                                        if (zzbv(str2)) {
                                            i++;
                                            if (i > 25) {
                                                stringBuilder = new StringBuilder(48);
                                                stringBuilder.append("Event can't contain more than 25 params");
                                                zzgg().zzil().zze(stringBuilder.toString(), zzgb().zzbe(str), zzgb().zzb(bundle2));
                                                zza(bundle3, 5);
                                                bundle3.remove(str2);
                                            }
                                        }
                                        str4 = str;
                                    }
                                }
                                z3 = true;
                                if (z3) {
                                    i3 = 17;
                                    z4 = true;
                                    if (i3 != 0) {
                                    }
                                    if (zzbv(str2)) {
                                        i++;
                                        if (i > 25) {
                                            stringBuilder = new StringBuilder(48);
                                            stringBuilder.append("Event can't contain more than 25 params");
                                            zzgg().zzil().zze(stringBuilder.toString(), zzgb().zzbe(str), zzgb().zzb(bundle2));
                                            zza(bundle3, 5);
                                            bundle3.remove(str2);
                                        }
                                    }
                                    str4 = str;
                                }
                            }
                            if (zzgi().zzd(zzfv().zzah(), zzew.zzahz)) {
                            }
                            z4 = true;
                            str5 = "param";
                            i4 = 100;
                            com_google_android_gms_internal_measurement_zzjv2 = com_google_android_gms_internal_measurement_zzjv;
                            str6 = str2;
                            if (com_google_android_gms_internal_measurement_zzjv2.zza(str5, str6, i4, obj, z2)) {
                            }
                            if (i3 != 0) {
                            }
                            if (zzbv(str2)) {
                                i++;
                                if (i > 25) {
                                    stringBuilder = new StringBuilder(48);
                                    stringBuilder.append("Event can't contain more than 25 params");
                                    zzgg().zzil().zze(stringBuilder.toString(), zzgb().zzbe(str), zzgb().zzb(bundle2));
                                    zza(bundle3, 5);
                                    bundle3.remove(str2);
                                }
                            }
                            str4 = str;
                        } else {
                            if (zza(bundle3, i2)) {
                                bundle3.putString("_ev", zza(str2, 40, true));
                                if (i2 == 3) {
                                    zza(bundle3, (Object) str2);
                                }
                            }
                            bundle3.remove(str2);
                        }
                        strArr = null;
                    } else if (!zza("event param", 40, str2)) {
                    }
                }
                i5 = 3;
                if (i5 == 0) {
                    i2 = i5;
                } else {
                    if (zzr("event param", str2)) {
                        if (!zza("event param", strArr, str2)) {
                            if (zza("event param", 40, str2)) {
                            }
                            i2 = 0;
                        }
                    }
                    i2 = 3;
                }
                if (i2 == 0) {
                    if (zza(bundle3, i2)) {
                        bundle3.putString("_ev", zza(str2, 40, true));
                        if (i2 == 3) {
                            zza(bundle3, (Object) str2);
                        }
                    }
                    bundle3.remove(str2);
                } else {
                    obj = bundle2.get(str2);
                    zzab();
                    if (z2) {
                        str3 = "param";
                        if (obj instanceof Parcelable[]) {
                            i2 = ((Parcelable[]) obj).length;
                        } else {
                            if (obj instanceof ArrayList) {
                                i2 = ((ArrayList) obj).size();
                            }
                            z3 = true;
                            if (z3) {
                                i3 = 17;
                                z4 = true;
                                if (i3 != 0) {
                                }
                                if (zzbv(str2)) {
                                    i++;
                                    if (i > 25) {
                                        stringBuilder = new StringBuilder(48);
                                        stringBuilder.append("Event can't contain more than 25 params");
                                        zzgg().zzil().zze(stringBuilder.toString(), zzgb().zzbe(str), zzgb().zzb(bundle2));
                                        zza(bundle3, 5);
                                        bundle3.remove(str2);
                                    }
                                }
                                str4 = str;
                            }
                        }
                        if (i2 > 1000) {
                            zzgg().zzin().zzd("Parameter array is too long; discarded. Value kind, name, array length", str3, str2, Integer.valueOf(i2));
                            z3 = false;
                            if (z3) {
                                i3 = 17;
                                z4 = true;
                                if (i3 != 0) {
                                }
                                if (zzbv(str2)) {
                                    i++;
                                    if (i > 25) {
                                        stringBuilder = new StringBuilder(48);
                                        stringBuilder.append("Event can't contain more than 25 params");
                                        zzgg().zzil().zze(stringBuilder.toString(), zzgb().zzbe(str), zzgb().zzb(bundle2));
                                        zza(bundle3, 5);
                                        bundle3.remove(str2);
                                    }
                                }
                                str4 = str;
                            }
                        }
                        z3 = true;
                        if (z3) {
                            i3 = 17;
                            z4 = true;
                            if (i3 != 0) {
                            }
                            if (zzbv(str2)) {
                                i++;
                                if (i > 25) {
                                    stringBuilder = new StringBuilder(48);
                                    stringBuilder.append("Event can't contain more than 25 params");
                                    zzgg().zzil().zze(stringBuilder.toString(), zzgb().zzbe(str), zzgb().zzb(bundle2));
                                    zza(bundle3, 5);
                                    bundle3.remove(str2);
                                }
                            }
                            str4 = str;
                        }
                    }
                    if (zzgi().zzd(zzfv().zzah(), zzew.zzahz)) {
                    }
                    z4 = true;
                    str5 = "param";
                    i4 = 100;
                    com_google_android_gms_internal_measurement_zzjv2 = com_google_android_gms_internal_measurement_zzjv;
                    str6 = str2;
                    if (com_google_android_gms_internal_measurement_zzjv2.zza(str5, str6, i4, obj, z2)) {
                    }
                    if (i3 != 0) {
                    }
                    if (zzbv(str2)) {
                        i++;
                        if (i > 25) {
                            stringBuilder = new StringBuilder(48);
                            stringBuilder.append("Event can't contain more than 25 params");
                            zzgg().zzil().zze(stringBuilder.toString(), zzgb().zzbe(str), zzgb().zzb(bundle2));
                            zza(bundle3, 5);
                            bundle3.remove(str2);
                        }
                    }
                    str4 = str;
                }
                strArr = null;
            }
            i5 = 0;
            if (i5 == 0) {
                if (zzr("event param", str2)) {
                    if (!zza("event param", strArr, str2)) {
                        if (zza("event param", 40, str2)) {
                        }
                        i2 = 0;
                    }
                }
                i2 = 3;
            } else {
                i2 = i5;
            }
            if (i2 == 0) {
                obj = bundle2.get(str2);
                zzab();
                if (z2) {
                    str3 = "param";
                    if (obj instanceof Parcelable[]) {
                        if (obj instanceof ArrayList) {
                            i2 = ((ArrayList) obj).size();
                        }
                        z3 = true;
                        if (z3) {
                            i3 = 17;
                            z4 = true;
                            if (i3 != 0) {
                            }
                            if (zzbv(str2)) {
                                i++;
                                if (i > 25) {
                                    stringBuilder = new StringBuilder(48);
                                    stringBuilder.append("Event can't contain more than 25 params");
                                    zzgg().zzil().zze(stringBuilder.toString(), zzgb().zzbe(str), zzgb().zzb(bundle2));
                                    zza(bundle3, 5);
                                    bundle3.remove(str2);
                                }
                            }
                            str4 = str;
                        }
                    } else {
                        i2 = ((Parcelable[]) obj).length;
                    }
                    if (i2 > 1000) {
                        zzgg().zzin().zzd("Parameter array is too long; discarded. Value kind, name, array length", str3, str2, Integer.valueOf(i2));
                        z3 = false;
                        if (z3) {
                            i3 = 17;
                            z4 = true;
                            if (i3 != 0) {
                            }
                            if (zzbv(str2)) {
                                i++;
                                if (i > 25) {
                                    stringBuilder = new StringBuilder(48);
                                    stringBuilder.append("Event can't contain more than 25 params");
                                    zzgg().zzil().zze(stringBuilder.toString(), zzgb().zzbe(str), zzgb().zzb(bundle2));
                                    zza(bundle3, 5);
                                    bundle3.remove(str2);
                                }
                            }
                            str4 = str;
                        }
                    }
                    z3 = true;
                    if (z3) {
                        i3 = 17;
                        z4 = true;
                        if (i3 != 0) {
                        }
                        if (zzbv(str2)) {
                            i++;
                            if (i > 25) {
                                stringBuilder = new StringBuilder(48);
                                stringBuilder.append("Event can't contain more than 25 params");
                                zzgg().zzil().zze(stringBuilder.toString(), zzgb().zzbe(str), zzgb().zzb(bundle2));
                                zza(bundle3, 5);
                                bundle3.remove(str2);
                            }
                        }
                        str4 = str;
                    }
                }
                if (zzgi().zzd(zzfv().zzah(), zzew.zzahz)) {
                }
                z4 = true;
                str5 = "param";
                i4 = 100;
                com_google_android_gms_internal_measurement_zzjv2 = com_google_android_gms_internal_measurement_zzjv;
                str6 = str2;
                if (com_google_android_gms_internal_measurement_zzjv2.zza(str5, str6, i4, obj, z2)) {
                }
                if (i3 != 0) {
                }
                if (zzbv(str2)) {
                    i++;
                    if (i > 25) {
                        stringBuilder = new StringBuilder(48);
                        stringBuilder.append("Event can't contain more than 25 params");
                        zzgg().zzil().zze(stringBuilder.toString(), zzgb().zzbe(str), zzgb().zzb(bundle2));
                        zza(bundle3, 5);
                        bundle3.remove(str2);
                    }
                }
                str4 = str;
            } else {
                if (zza(bundle3, i2)) {
                    bundle3.putString("_ev", zza(str2, 40, true));
                    if (i2 == 3) {
                        zza(bundle3, (Object) str2);
                    }
                }
                bundle3.remove(str2);
            }
            strArr = null;
        }
        return bundle3;
    }

    final zzeu zza(String str, Bundle bundle, String str2, long j, boolean z, boolean z2) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        if (zzbw(str) != 0) {
            zzgg().zzil().zzg("Invalid conditional property event name", zzgb().zzbg(str));
            throw new IllegalArgumentException();
        }
        Bundle bundle2 = bundle != null ? new Bundle(bundle) : new Bundle();
        bundle2.putString("_o", str2);
        String str3 = str;
        return new zzeu(str3, new zzer(zzd(zza(str3, bundle2, CollectionUtils.listOf((Object) "_o"), false, false))), str2, j);
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
            } else {
                if (str != null) {
                    zzgg().zzio().zze("Not putting event parameter. Invalid value type. name, type", zzgb().zzbf(str), obj != null ? obj.getClass().getSimpleName() : null);
                }
            }
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
        this.zzacr.zzfu().zza("auto", "_err", bundle);
    }

    public final boolean zza(long j, long j2) {
        return j == 0 || j2 <= 0 || Math.abs(zzbt().currentTimeMillis() - j) > j2;
    }

    final boolean zza(String str, int i, String str2) {
        if (str2 == null) {
            zzgg().zzil().zzg("Name is required and can't be null. Type", str);
            return false;
        } else if (str2.codePointCount(0, str2.length()) <= i) {
            return true;
        } else {
            zzgg().zzil().zzd("Name is too long. Type, maximum supported length, name", str, Integer.valueOf(i), str2);
            return false;
        }
    }

    final boolean zza(String str, String[] strArr, String str2) {
        if (str2 == null) {
            zzgg().zzil().zzg("Name is required and can't be null. Type", str);
            return false;
        }
        boolean z;
        Preconditions.checkNotNull(str2);
        for (String startsWith : zzaqy) {
            if (str2.startsWith(startsWith)) {
                z = true;
                break;
            }
        }
        z = false;
        if (z) {
            zzgg().zzil().zze("Name starts with reserved prefix. Type, name", str, str2);
            return false;
        }
        if (strArr != null) {
            boolean z2;
            Preconditions.checkNotNull(strArr);
            for (String startsWith2 : strArr) {
                if (zzs(str2, startsWith2)) {
                    z2 = true;
                    break;
                }
            }
            z2 = false;
            if (z2) {
                zzgg().zzil().zze("Name is reserved. Type, name", str, str2);
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
            zzgg().zzil().zzg("Failed to gzip content", e);
            throw e;
        }
    }

    public final /* bridge */ /* synthetic */ void zzab() {
        super.zzab();
    }

    public final byte[] zzb(zzkk com_google_android_gms_internal_measurement_zzkk) {
        try {
            byte[] bArr = new byte[com_google_android_gms_internal_measurement_zzkk.zzwg()];
            zzabb zzb = zzabb.zzb(bArr, 0, bArr.length);
            com_google_android_gms_internal_measurement_zzkk.zza(zzb);
            zzb.zzvy();
            return bArr;
        } catch (IOException e) {
            zzgg().zzil().zzg("Data loss. Failed to serialize batch", e);
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
            zzgg().zzil().zzg("Failed to ungzip content", e);
            throw e;
        }
    }

    public final /* bridge */ /* synthetic */ Clock zzbt() {
        return super.zzbt();
    }

    public final int zzbw(String str) {
        return !zzr("event", str) ? 2 : !zza("event", Event.zzacs, str) ? 13 : !zza("event", 40, str) ? 2 : 0;
    }

    public final int zzbx(String str) {
        return !zzq("user property", str) ? 6 : !zza("user property", UserProperty.zzacw, str) ? 15 : !zza("user property", 24, str) ? 6 : 0;
    }

    public final int zzby(String str) {
        return !zzr("user property", str) ? 6 : !zza("user property", UserProperty.zzacw, str) ? 15 : !zza("user property", 24, str) ? 6 : 0;
    }

    public final boolean zzbz(String str) {
        if (TextUtils.isEmpty(str)) {
            zzgg().zzil().log("Missing google_app_id. Firebase Analytics disabled. See https://goo.gl/NAOOOI");
            return false;
        }
        Preconditions.checkNotNull(str);
        if (str.matches("^1:\\d+:android:[a-f0-9]+$")) {
            return true;
        }
        zzgg().zzil().zzg("Invalid google_app_id. Firebase Analytics disabled. See https://goo.gl/NAOOOI. provided id", str);
        return false;
    }

    public final boolean zzcc(String str) {
        return TextUtils.isEmpty(str) ? false : zzgi().zzhl().equals(str);
    }

    final boolean zzce(String str) {
        return "1".equals(zzgd().zzm(str, "measurement.upload.blacklist_internal"));
    }

    final boolean zzcf(String str) {
        return "1".equals(zzgd().zzm(str, "measurement.upload.blacklist_public"));
    }

    final long zzd(Context context, String str) {
        zzab();
        Preconditions.checkNotNull(context);
        Preconditions.checkNotEmpty(str);
        PackageManager packageManager = context.getPackageManager();
        MessageDigest messageDigest = getMessageDigest("MD5");
        if (messageDigest == null) {
            zzgg().zzil().log("Could not get MD5 instance");
            return -1;
        }
        if (packageManager != null) {
            try {
                if (!zze(context, str)) {
                    PackageInfo packageInfo = Wrappers.packageManager(context).getPackageInfo(getContext().getPackageName(), 64);
                    if (packageInfo.signatures != null && packageInfo.signatures.length > 0) {
                        return zzc(messageDigest.digest(packageInfo.signatures[0].toByteArray()));
                    }
                    zzgg().zzin().log("Could not get signatures");
                    return -1;
                }
            } catch (NameNotFoundException e) {
                zzgg().zzil().zzg("Package name not found", e);
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
                    zzgg().zzin().zzg("Param value can't be null", zzgb().zzbf(str));
                } else {
                    zza(bundle2, str, zzh);
                }
            }
        }
        return bundle2;
    }

    public final /* bridge */ /* synthetic */ void zzfq() {
        super.zzfq();
    }

    public final /* bridge */ /* synthetic */ void zzfr() {
        super.zzfr();
    }

    public final /* bridge */ /* synthetic */ zzdx zzfs() {
        return super.zzfs();
    }

    public final /* bridge */ /* synthetic */ zzee zzft() {
        return super.zzft();
    }

    public final /* bridge */ /* synthetic */ zzhm zzfu() {
        return super.zzfu();
    }

    public final /* bridge */ /* synthetic */ zzfb zzfv() {
        return super.zzfv();
    }

    public final /* bridge */ /* synthetic */ zzeo zzfw() {
        return super.zzfw();
    }

    public final /* bridge */ /* synthetic */ zzil zzfx() {
        return super.zzfx();
    }

    public final /* bridge */ /* synthetic */ zzih zzfy() {
        return super.zzfy();
    }

    public final /* bridge */ /* synthetic */ zzfc zzfz() {
        return super.zzfz();
    }

    public final /* bridge */ /* synthetic */ zzei zzga() {
        return super.zzga();
    }

    public final /* bridge */ /* synthetic */ zzfe zzgb() {
        return super.zzgb();
    }

    public final /* bridge */ /* synthetic */ zzjv zzgc() {
        return super.zzgc();
    }

    public final /* bridge */ /* synthetic */ zzgf zzgd() {
        return super.zzgd();
    }

    public final /* bridge */ /* synthetic */ zzjk zzge() {
        return super.zzge();
    }

    public final /* bridge */ /* synthetic */ zzgg zzgf() {
        return super.zzgf();
    }

    public final /* bridge */ /* synthetic */ zzfg zzgg() {
        return super.zzgg();
    }

    public final /* bridge */ /* synthetic */ zzfr zzgh() {
        return super.zzgh();
    }

    public final /* bridge */ /* synthetic */ zzeh zzgi() {
        return super.zzgi();
    }

    public final Object zzh(String str, Object obj) {
        boolean z;
        int i = 256;
        if ("_ev".equals(str)) {
            z = true;
        } else {
            if (!zzcb(str)) {
                i = 100;
            }
            z = false;
        }
        return zza(i, obj, z);
    }

    protected final boolean zzhh() {
        return true;
    }

    public final int zzi(String str, Object obj) {
        return "_ldl".equals(str) ? zza("user property referrer", str, zzca(str), obj, false) : zza("user property", str, zzca(str), obj, false) ? 0 : 7;
    }

    protected final void zzig() {
        zzab();
        SecureRandom secureRandom = new SecureRandom();
        long nextLong = secureRandom.nextLong();
        if (nextLong == 0) {
            nextLong = secureRandom.nextLong();
            if (nextLong == 0) {
                zzgg().zzin().log("Utils falling back to Random for random id");
            }
        }
        this.zzara.set(nextLong);
    }

    public final Object zzj(String str, Object obj) {
        int zzca;
        boolean z;
        if ("_ldl".equals(str)) {
            zzca = zzca(str);
            z = true;
        } else {
            zzca = zzca(str);
            z = false;
        }
        return zza(zzca, obj, z);
    }

    public final long zzkt() {
        if (this.zzara.get() == 0) {
            long j;
            synchronized (this.zzara) {
                long nextLong = new Random(System.nanoTime() ^ zzbt().currentTimeMillis()).nextLong();
                int i = this.zzarb + 1;
                this.zzarb = i;
                j = nextLong + ((long) i);
            }
            return j;
        }
        synchronized (this.zzara) {
            this.zzara.compareAndSet(-1, 1);
            nextLong = this.zzara.getAndIncrement();
        }
        return nextLong;
    }

    final SecureRandom zzku() {
        zzab();
        if (this.zzaqz == null) {
            this.zzaqz = new SecureRandom();
        }
        return this.zzaqz;
    }

    public final int zzkv() {
        if (this.zzarc == null) {
            this.zzarc = Integer.valueOf(GoogleApiAvailabilityLight.getInstance().getApkVersion(getContext()) / 1000);
        }
        return this.zzarc.intValue();
    }

    final boolean zzq(String str, String str2) {
        if (str2 == null) {
            zzgg().zzil().zzg("Name is required and can't be null. Type", str);
            return false;
        } else if (str2.length() == 0) {
            zzgg().zzil().zzg("Name is required and can't be empty. Type", str);
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
                        zzgg().zzil().zze("Name must consist of letters, digits or _ (underscores). Type, name", str, str2);
                        return false;
                    }
                }
                return true;
            }
            zzgg().zzil().zze("Name must start with a letter. Type, name", str, str2);
            return false;
        }
    }

    public final boolean zzx(String str) {
        zzab();
        if (Wrappers.packageManager(getContext()).checkCallingOrSelfPermission(str) == 0) {
            return true;
        }
        zzgg().zziq().zzg("Permission not granted", str);
        return false;
    }
}
