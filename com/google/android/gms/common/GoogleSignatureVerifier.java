package com.google.android.gms.common;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.wrappers.Wrappers;
import javax.annotation.CheckReturnValue;

@CheckReturnValue
public class GoogleSignatureVerifier {
    private static GoogleSignatureVerifier zzbv;
    private final Context mContext;

    private GoogleSignatureVerifier(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public static GoogleSignatureVerifier getInstance(Context context) {
        Preconditions.checkNotNull(context);
        synchronized (GoogleSignatureVerifier.class) {
            if (zzbv == null) {
                GoogleCertificates.init(context);
                zzbv = new GoogleSignatureVerifier(context);
            }
        }
        return zzbv;
    }

    private static CertData zza(PackageInfo packageInfo, CertData... certDataArr) {
        int i = 0;
        if (packageInfo.signatures == null) {
            return null;
        }
        if (packageInfo.signatures.length != 1) {
            Log.w("GoogleSignatureVerifier", "Package has more than one signature.");
            return null;
        }
        zzb com_google_android_gms_common_zzb = new zzb(packageInfo.signatures[0].toByteArray());
        while (i < certDataArr.length) {
            if (certDataArr[i].equals(com_google_android_gms_common_zzb)) {
                return certDataArr[i];
            }
            i++;
        }
        return null;
    }

    private final zzg zza(PackageInfo packageInfo) {
        boolean honorsDebugCertificates = GooglePlayServicesUtilLight.honorsDebugCertificates(this.mContext);
        if (packageInfo == null) {
            return zzg.zze("null pkg");
        }
        if (packageInfo.signatures.length != 1) {
            return zzg.zze("single cert required");
        }
        CertData com_google_android_gms_common_zzb = new zzb(packageInfo.signatures[0].toByteArray());
        String str = packageInfo.packageName;
        zzg zza = GoogleCertificates.zza(str, com_google_android_gms_common_zzb, honorsDebugCertificates);
        return (!zza.zzbl || packageInfo.applicationInfo == null || (packageInfo.applicationInfo.flags & 2) == 0) ? zza : (!honorsDebugCertificates || GoogleCertificates.zza(str, com_google_android_gms_common_zzb, false).zzbl) ? zzg.zze("debuggable release cert app rejected") : zza;
    }

    private final zzg zzb(int i) {
        String[] packagesForUid = Wrappers.packageManager(this.mContext).getPackagesForUid(i);
        if (packagesForUid == null || packagesForUid.length == 0) {
            return zzg.zze("no pkgs");
        }
        zzg com_google_android_gms_common_zzg = null;
        for (String zzf : packagesForUid) {
            com_google_android_gms_common_zzg = zzf(zzf);
            if (com_google_android_gms_common_zzg.zzbl) {
                return com_google_android_gms_common_zzg;
            }
        }
        return com_google_android_gms_common_zzg;
    }

    private final zzg zzf(String str) {
        try {
            return zza(Wrappers.packageManager(this.mContext).getPackageInfo(str, 64));
        } catch (NameNotFoundException e) {
            String str2 = "no pkg ";
            String valueOf = String.valueOf(str);
            return zzg.zze(valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2));
        }
    }

    public boolean isGooglePublicSignedPackage(PackageInfo packageInfo) {
        if (packageInfo == null) {
            return false;
        }
        if (isGooglePublicSignedPackage(packageInfo, false)) {
            return true;
        }
        if (!isGooglePublicSignedPackage(packageInfo, true)) {
            return false;
        }
        if (GooglePlayServicesUtilLight.honorsDebugCertificates(this.mContext)) {
            return true;
        }
        Log.w("GoogleSignatureVerifier", "Test-keys aren't accepted on this build.");
        return false;
    }

    public boolean isGooglePublicSignedPackage(PackageInfo packageInfo, boolean z) {
        if (!(packageInfo == null || packageInfo.signatures == null)) {
            CertData zza;
            if (z) {
                zza = zza(packageInfo, zzd.zzbg);
            } else {
                zza = zza(packageInfo, zzd.zzbg[0]);
            }
            if (zza != null) {
                return true;
            }
        }
        return false;
    }

    public boolean isUidGoogleSigned(int i) {
        zzg zzb = zzb(i);
        zzb.zzi();
        return zzb.zzbl;
    }
}
