package com.google.android.gms.internal.config;

import android.os.Bundle;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.data.DataBufferSafeParcelable;
import com.google.android.gms.common.data.DataHolder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.regex.Pattern;
import org.telegram.messenger.exoplayer2.C;

public final class zzo implements zzg {
    private static final Charset UTF_8 = Charset.forName(C.UTF8_NAME);
    private static final Pattern zzl = Pattern.compile("^(1|true|t|yes|y|on)$", 2);
    private static final Pattern zzm = Pattern.compile("^(0|false|f|no|n|off|)$", 2);

    private static HashMap<String, TreeMap<String, byte[]>> zza(zzad com_google_android_gms_internal_config_zzad) {
        if (com_google_android_gms_internal_config_zzad == null) {
            return null;
        }
        DataHolder zzi = com_google_android_gms_internal_config_zzad.zzi();
        if (zzi == null) {
            return null;
        }
        zzaj com_google_android_gms_internal_config_zzaj = (zzaj) new DataBufferSafeParcelable(zzi, zzaj.CREATOR).get(0);
        com_google_android_gms_internal_config_zzad.zzk();
        HashMap<String, TreeMap<String, byte[]>> hashMap = new HashMap();
        for (String str : com_google_android_gms_internal_config_zzaj.zzm().keySet()) {
            TreeMap treeMap = new TreeMap();
            hashMap.put(str, treeMap);
            Bundle bundle = com_google_android_gms_internal_config_zzaj.zzm().getBundle(str);
            for (String str2 : bundle.keySet()) {
                treeMap.put(str2, bundle.getByteArray(str2));
            }
        }
        return hashMap;
    }

    static List<byte[]> zzb(zzad com_google_android_gms_internal_config_zzad) {
        if (com_google_android_gms_internal_config_zzad == null) {
            return null;
        }
        DataHolder zzj = com_google_android_gms_internal_config_zzad.zzj();
        if (zzj == null) {
            return null;
        }
        List<byte[]> arrayList = new ArrayList();
        for (zzx payload : new DataBufferSafeParcelable(zzj, zzx.CREATOR)) {
            arrayList.add(payload.getPayload());
        }
        com_google_android_gms_internal_config_zzad.zzl();
        return arrayList;
    }

    private static Status zzd(int i) {
        String str;
        if (i == -6508) {
            str = "SUCCESS_CACHE_STALE";
        } else if (i != 6507) {
            switch (i) {
                case -6506:
                    str = "SUCCESS_CACHE";
                    break;
                case -6505:
                    str = "SUCCESS_FRESH";
                    break;
                default:
                    switch (i) {
                        case 6500:
                            str = "NOT_AUTHORIZED_TO_FETCH";
                            break;
                        case 6501:
                            str = "ANOTHER_FETCH_INFLIGHT";
                            break;
                        case 6502:
                            str = "FETCH_THROTTLED";
                            break;
                        case 6503:
                            str = "NOT_AVAILABLE";
                            break;
                        case 6504:
                            str = "FAILURE_CACHE";
                            break;
                        default:
                            str = CommonStatusCodes.getStatusCodeString(i);
                            break;
                    }
            }
        } else {
            str = "FETCH_THROTTLED_STALE";
        }
        return new Status(i, str);
    }

    public final PendingResult<zzk> zza(GoogleApiClient googleApiClient, zzi com_google_android_gms_internal_config_zzi) {
        if (googleApiClient != null) {
            if (com_google_android_gms_internal_config_zzi != null) {
                return googleApiClient.enqueue(new zzp(this, googleApiClient, com_google_android_gms_internal_config_zzi));
            }
        }
        return null;
    }
}
