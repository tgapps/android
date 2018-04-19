package com.google.android.gms.internal.measurement;

import android.content.Context;
import android.os.Bundle;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.Clock;
import com.google.android.gms.measurement.AppMeasurement.Event;
import com.google.android.gms.measurement.AppMeasurement.Param;
import com.google.android.gms.measurement.AppMeasurement.UserProperty;
import java.util.concurrent.atomic.AtomicReference;
import org.telegram.messenger.NotificationBadge.NewHtcHomeBadger;

public final class zzfe extends zzhk {
    private static final AtomicReference<String[]> zzaij = new AtomicReference();
    private static final AtomicReference<String[]> zzaik = new AtomicReference();
    private static final AtomicReference<String[]> zzail = new AtomicReference();

    zzfe(zzgl com_google_android_gms_internal_measurement_zzgl) {
        super(com_google_android_gms_internal_measurement_zzgl);
    }

    private static String zza(String str, String[] strArr, String[] strArr2, AtomicReference<String[]> atomicReference) {
        int i = 0;
        Preconditions.checkNotNull(strArr);
        Preconditions.checkNotNull(strArr2);
        Preconditions.checkNotNull(atomicReference);
        Preconditions.checkArgument(strArr.length == strArr2.length);
        while (i < strArr.length) {
            if (zzjv.zzs(str, strArr[i])) {
                synchronized (atomicReference) {
                    String[] strArr3 = (String[]) atomicReference.get();
                    if (strArr3 == null) {
                        strArr3 = new String[strArr2.length];
                        atomicReference.set(strArr3);
                    }
                    if (strArr3[i] == null) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(strArr2[i]);
                        stringBuilder.append("(");
                        stringBuilder.append(strArr[i]);
                        stringBuilder.append(")");
                        strArr3[i] = stringBuilder.toString();
                    }
                    str = strArr3[i];
                }
                return str;
            }
            i++;
        }
        return str;
    }

    private static void zza(StringBuilder stringBuilder, int i) {
        for (int i2 = 0; i2 < i; i2++) {
            stringBuilder.append("  ");
        }
    }

    private final void zza(StringBuilder stringBuilder, int i, zzka com_google_android_gms_internal_measurement_zzka) {
        if (com_google_android_gms_internal_measurement_zzka != null) {
            zza(stringBuilder, i);
            stringBuilder.append("filter {\n");
            zza(stringBuilder, i, "complement", com_google_android_gms_internal_measurement_zzka.zzars);
            zza(stringBuilder, i, "param_name", zzbf(com_google_android_gms_internal_measurement_zzka.zzart));
            int i2 = i + 1;
            String str = "string_filter";
            zzkd com_google_android_gms_internal_measurement_zzkd = com_google_android_gms_internal_measurement_zzka.zzarq;
            if (com_google_android_gms_internal_measurement_zzkd != null) {
                zza(stringBuilder, i2);
                stringBuilder.append(str);
                stringBuilder.append(" {\n");
                if (com_google_android_gms_internal_measurement_zzkd.zzasc != null) {
                    Object obj = "UNKNOWN_MATCH_TYPE";
                    switch (com_google_android_gms_internal_measurement_zzkd.zzasc.intValue()) {
                        case 1:
                            obj = "REGEXP";
                            break;
                        case 2:
                            obj = "BEGINS_WITH";
                            break;
                        case 3:
                            obj = "ENDS_WITH";
                            break;
                        case 4:
                            obj = "PARTIAL";
                            break;
                        case 5:
                            obj = "EXACT";
                            break;
                        case 6:
                            obj = "IN_LIST";
                            break;
                    }
                    zza(stringBuilder, i2, "match_type", obj);
                }
                zza(stringBuilder, i2, "expression", com_google_android_gms_internal_measurement_zzkd.zzasd);
                zza(stringBuilder, i2, "case_sensitive", com_google_android_gms_internal_measurement_zzkd.zzase);
                if (com_google_android_gms_internal_measurement_zzkd.zzasf.length > 0) {
                    zza(stringBuilder, i2 + 1);
                    stringBuilder.append("expression_list {\n");
                    for (String str2 : com_google_android_gms_internal_measurement_zzkd.zzasf) {
                        zza(stringBuilder, i2 + 2);
                        stringBuilder.append(str2);
                        stringBuilder.append("\n");
                    }
                    stringBuilder.append("}\n");
                }
                zza(stringBuilder, i2);
                stringBuilder.append("}\n");
            }
            zza(stringBuilder, i + 1, "number_filter", com_google_android_gms_internal_measurement_zzka.zzarr);
            zza(stringBuilder, i);
            stringBuilder.append("}\n");
        }
    }

    private final void zza(StringBuilder stringBuilder, int i, String str, zzkb com_google_android_gms_internal_measurement_zzkb) {
        if (com_google_android_gms_internal_measurement_zzkb != null) {
            zza(stringBuilder, i);
            stringBuilder.append(str);
            stringBuilder.append(" {\n");
            if (com_google_android_gms_internal_measurement_zzkb.zzaru != null) {
                Object obj = "UNKNOWN_COMPARISON_TYPE";
                switch (com_google_android_gms_internal_measurement_zzkb.zzaru.intValue()) {
                    case 1:
                        obj = "LESS_THAN";
                        break;
                    case 2:
                        obj = "GREATER_THAN";
                        break;
                    case 3:
                        obj = "EQUAL";
                        break;
                    case 4:
                        obj = "BETWEEN";
                        break;
                }
                zza(stringBuilder, i, "comparison_type", obj);
            }
            zza(stringBuilder, i, "match_as_float", com_google_android_gms_internal_measurement_zzkb.zzarv);
            zza(stringBuilder, i, "comparison_value", com_google_android_gms_internal_measurement_zzkb.zzarw);
            zza(stringBuilder, i, "min_comparison_value", com_google_android_gms_internal_measurement_zzkb.zzarx);
            zza(stringBuilder, i, "max_comparison_value", com_google_android_gms_internal_measurement_zzkb.zzary);
            zza(stringBuilder, i);
            stringBuilder.append("}\n");
        }
    }

    private static void zza(StringBuilder stringBuilder, int i, String str, zzkm com_google_android_gms_internal_measurement_zzkm) {
        if (com_google_android_gms_internal_measurement_zzkm != null) {
            int i2;
            int i3;
            zza(stringBuilder, 3);
            stringBuilder.append(str);
            stringBuilder.append(" {\n");
            if (com_google_android_gms_internal_measurement_zzkm.zzaug != null) {
                zza(stringBuilder, 4);
                stringBuilder.append("results: ");
                long[] jArr = com_google_android_gms_internal_measurement_zzkm.zzaug;
                int length = jArr.length;
                i2 = 0;
                i3 = 0;
                while (i2 < length) {
                    Long valueOf = Long.valueOf(jArr[i2]);
                    int i4 = i3 + 1;
                    if (i3 != 0) {
                        stringBuilder.append(", ");
                    }
                    stringBuilder.append(valueOf);
                    i2++;
                    i3 = i4;
                }
                stringBuilder.append('\n');
            }
            if (com_google_android_gms_internal_measurement_zzkm.zzauf != null) {
                zza(stringBuilder, 4);
                stringBuilder.append("status: ");
                long[] jArr2 = com_google_android_gms_internal_measurement_zzkm.zzauf;
                int length2 = jArr2.length;
                i2 = 0;
                i3 = 0;
                while (i2 < length2) {
                    Long valueOf2 = Long.valueOf(jArr2[i2]);
                    int i5 = i3 + 1;
                    if (i3 != 0) {
                        stringBuilder.append(", ");
                    }
                    stringBuilder.append(valueOf2);
                    i2++;
                    i3 = i5;
                }
                stringBuilder.append('\n');
            }
            zza(stringBuilder, 3);
            stringBuilder.append("}\n");
        }
    }

    private static void zza(StringBuilder stringBuilder, int i, String str, Object obj) {
        if (obj != null) {
            zza(stringBuilder, i + 1);
            stringBuilder.append(str);
            stringBuilder.append(": ");
            stringBuilder.append(obj);
            stringBuilder.append('\n');
        }
    }

    private final boolean zzik() {
        return this.zzacr.zzgg().isLoggable(3);
    }

    public final /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    protected final String zza(zzjz com_google_android_gms_internal_measurement_zzjz) {
        int i = 0;
        if (com_google_android_gms_internal_measurement_zzjz == null) {
            return "null";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\nevent_filter {\n");
        zza(stringBuilder, 0, "filter_id", com_google_android_gms_internal_measurement_zzjz.zzark);
        zza(stringBuilder, 0, "event_name", zzbe(com_google_android_gms_internal_measurement_zzjz.zzarl));
        zza(stringBuilder, 1, "event_count_filter", com_google_android_gms_internal_measurement_zzjz.zzaro);
        stringBuilder.append("  filters {\n");
        zzka[] com_google_android_gms_internal_measurement_zzkaArr = com_google_android_gms_internal_measurement_zzjz.zzarm;
        int length = com_google_android_gms_internal_measurement_zzkaArr.length;
        while (i < length) {
            zza(stringBuilder, 2, com_google_android_gms_internal_measurement_zzkaArr[i]);
            i++;
        }
        zza(stringBuilder, 1);
        stringBuilder.append("}\n}\n");
        return stringBuilder.toString();
    }

    protected final String zza(zzkc com_google_android_gms_internal_measurement_zzkc) {
        if (com_google_android_gms_internal_measurement_zzkc == null) {
            return "null";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\nproperty_filter {\n");
        zza(stringBuilder, 0, "filter_id", com_google_android_gms_internal_measurement_zzkc.zzark);
        zza(stringBuilder, 0, "property_name", zzbg(com_google_android_gms_internal_measurement_zzkc.zzasa));
        zza(stringBuilder, 1, com_google_android_gms_internal_measurement_zzkc.zzasb);
        stringBuilder.append("}\n");
        return stringBuilder.toString();
    }

    protected final String zza(zzkk com_google_android_gms_internal_measurement_zzkk) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\nbatch {\n");
        if (com_google_android_gms_internal_measurement_zzkk.zzata != null) {
            for (zzkl com_google_android_gms_internal_measurement_zzkl : com_google_android_gms_internal_measurement_zzkk.zzata) {
                if (!(com_google_android_gms_internal_measurement_zzkl == null || com_google_android_gms_internal_measurement_zzkl == null)) {
                    zza(stringBuilder, 1);
                    stringBuilder.append("bundle {\n");
                    zza(stringBuilder, 1, "protocol_version", com_google_android_gms_internal_measurement_zzkl.zzatc);
                    zza(stringBuilder, 1, "platform", com_google_android_gms_internal_measurement_zzkl.zzatk);
                    zza(stringBuilder, 1, "gmp_version", com_google_android_gms_internal_measurement_zzkl.zzato);
                    zza(stringBuilder, 1, "uploading_gmp_version", com_google_android_gms_internal_measurement_zzkl.zzatp);
                    zza(stringBuilder, 1, "config_version", com_google_android_gms_internal_measurement_zzkl.zzaua);
                    zza(stringBuilder, 1, "gmp_app_id", com_google_android_gms_internal_measurement_zzkl.zzadh);
                    zza(stringBuilder, 1, "app_id", com_google_android_gms_internal_measurement_zzkl.zztd);
                    zza(stringBuilder, 1, "app_version", com_google_android_gms_internal_measurement_zzkl.zztc);
                    zza(stringBuilder, 1, "app_version_major", com_google_android_gms_internal_measurement_zzkl.zzatw);
                    zza(stringBuilder, 1, "firebase_instance_id", com_google_android_gms_internal_measurement_zzkl.zzadj);
                    zza(stringBuilder, 1, "dev_cert_hash", com_google_android_gms_internal_measurement_zzkl.zzats);
                    zza(stringBuilder, 1, "app_store", com_google_android_gms_internal_measurement_zzkl.zzado);
                    zza(stringBuilder, 1, "upload_timestamp_millis", com_google_android_gms_internal_measurement_zzkl.zzatf);
                    zza(stringBuilder, 1, "start_timestamp_millis", com_google_android_gms_internal_measurement_zzkl.zzatg);
                    zza(stringBuilder, 1, "end_timestamp_millis", com_google_android_gms_internal_measurement_zzkl.zzath);
                    zza(stringBuilder, 1, "previous_bundle_start_timestamp_millis", com_google_android_gms_internal_measurement_zzkl.zzati);
                    zza(stringBuilder, 1, "previous_bundle_end_timestamp_millis", com_google_android_gms_internal_measurement_zzkl.zzatj);
                    zza(stringBuilder, 1, "app_instance_id", com_google_android_gms_internal_measurement_zzkl.zzadg);
                    zza(stringBuilder, 1, "resettable_device_id", com_google_android_gms_internal_measurement_zzkl.zzatq);
                    zza(stringBuilder, 1, "device_id", com_google_android_gms_internal_measurement_zzkl.zzatz);
                    zza(stringBuilder, 1, "limited_ad_tracking", com_google_android_gms_internal_measurement_zzkl.zzatr);
                    zza(stringBuilder, 1, "os_version", com_google_android_gms_internal_measurement_zzkl.zzatl);
                    zza(stringBuilder, 1, "device_model", com_google_android_gms_internal_measurement_zzkl.zzatm);
                    zza(stringBuilder, 1, "user_default_language", com_google_android_gms_internal_measurement_zzkl.zzafl);
                    zza(stringBuilder, 1, "time_zone_offset_minutes", com_google_android_gms_internal_measurement_zzkl.zzatn);
                    zza(stringBuilder, 1, "bundle_sequential_index", com_google_android_gms_internal_measurement_zzkl.zzatt);
                    zza(stringBuilder, 1, "service_upload", com_google_android_gms_internal_measurement_zzkl.zzatu);
                    zza(stringBuilder, 1, "health_monitor", com_google_android_gms_internal_measurement_zzkl.zzaef);
                    if (!(com_google_android_gms_internal_measurement_zzkl.zzaub == null || com_google_android_gms_internal_measurement_zzkl.zzaub.longValue() == 0)) {
                        zza(stringBuilder, 1, "android_id", com_google_android_gms_internal_measurement_zzkl.zzaub);
                    }
                    if (com_google_android_gms_internal_measurement_zzkl.zzaue != null) {
                        zza(stringBuilder, 1, "retry_counter", com_google_android_gms_internal_measurement_zzkl.zzaue);
                    }
                    zzkn[] com_google_android_gms_internal_measurement_zzknArr = com_google_android_gms_internal_measurement_zzkl.zzate;
                    if (com_google_android_gms_internal_measurement_zzknArr != null) {
                        for (zzkn com_google_android_gms_internal_measurement_zzkn : com_google_android_gms_internal_measurement_zzknArr) {
                            if (com_google_android_gms_internal_measurement_zzkn != null) {
                                zza(stringBuilder, 2);
                                stringBuilder.append("user_property {\n");
                                zza(stringBuilder, 2, "set_timestamp_millis", com_google_android_gms_internal_measurement_zzkn.zzaui);
                                zza(stringBuilder, 2, "name", zzbg(com_google_android_gms_internal_measurement_zzkn.name));
                                zza(stringBuilder, 2, "string_value", com_google_android_gms_internal_measurement_zzkn.zzajf);
                                zza(stringBuilder, 2, "int_value", com_google_android_gms_internal_measurement_zzkn.zzasz);
                                zza(stringBuilder, 2, "double_value", com_google_android_gms_internal_measurement_zzkn.zzaqx);
                                zza(stringBuilder, 2);
                                stringBuilder.append("}\n");
                            }
                        }
                    }
                    zzkh[] com_google_android_gms_internal_measurement_zzkhArr = com_google_android_gms_internal_measurement_zzkl.zzatv;
                    if (com_google_android_gms_internal_measurement_zzkhArr != null) {
                        for (zzkh com_google_android_gms_internal_measurement_zzkh : com_google_android_gms_internal_measurement_zzkhArr) {
                            if (com_google_android_gms_internal_measurement_zzkh != null) {
                                zza(stringBuilder, 2);
                                stringBuilder.append("audience_membership {\n");
                                zza(stringBuilder, 2, "audience_id", com_google_android_gms_internal_measurement_zzkh.zzarg);
                                zza(stringBuilder, 2, "new_audience", com_google_android_gms_internal_measurement_zzkh.zzast);
                                zza(stringBuilder, 2, "current_data", com_google_android_gms_internal_measurement_zzkh.zzasr);
                                zza(stringBuilder, 2, "previous_data", com_google_android_gms_internal_measurement_zzkh.zzass);
                                zza(stringBuilder, 2);
                                stringBuilder.append("}\n");
                            }
                        }
                    }
                    zzki[] com_google_android_gms_internal_measurement_zzkiArr = com_google_android_gms_internal_measurement_zzkl.zzatd;
                    if (com_google_android_gms_internal_measurement_zzkiArr != null) {
                        for (zzki com_google_android_gms_internal_measurement_zzki : com_google_android_gms_internal_measurement_zzkiArr) {
                            if (com_google_android_gms_internal_measurement_zzki != null) {
                                zza(stringBuilder, 2);
                                stringBuilder.append("event {\n");
                                zza(stringBuilder, 2, "name", zzbe(com_google_android_gms_internal_measurement_zzki.name));
                                zza(stringBuilder, 2, "timestamp_millis", com_google_android_gms_internal_measurement_zzki.zzasw);
                                zza(stringBuilder, 2, "previous_timestamp_millis", com_google_android_gms_internal_measurement_zzki.zzasx);
                                zza(stringBuilder, 2, NewHtcHomeBadger.COUNT, com_google_android_gms_internal_measurement_zzki.count);
                                zzkj[] com_google_android_gms_internal_measurement_zzkjArr = com_google_android_gms_internal_measurement_zzki.zzasv;
                                if (com_google_android_gms_internal_measurement_zzkjArr != null) {
                                    for (zzkj com_google_android_gms_internal_measurement_zzkj : com_google_android_gms_internal_measurement_zzkjArr) {
                                        if (com_google_android_gms_internal_measurement_zzkj != null) {
                                            zza(stringBuilder, 3);
                                            stringBuilder.append("param {\n");
                                            zza(stringBuilder, 3, "name", zzbf(com_google_android_gms_internal_measurement_zzkj.name));
                                            zza(stringBuilder, 3, "string_value", com_google_android_gms_internal_measurement_zzkj.zzajf);
                                            zza(stringBuilder, 3, "int_value", com_google_android_gms_internal_measurement_zzkj.zzasz);
                                            zza(stringBuilder, 3, "double_value", com_google_android_gms_internal_measurement_zzkj.zzaqx);
                                            zza(stringBuilder, 3);
                                            stringBuilder.append("}\n");
                                        }
                                    }
                                }
                                zza(stringBuilder, 2);
                                stringBuilder.append("}\n");
                            }
                        }
                    }
                    zza(stringBuilder, 1);
                    stringBuilder.append("}\n");
                }
            }
        }
        stringBuilder.append("}\n");
        return stringBuilder.toString();
    }

    public final /* bridge */ /* synthetic */ void zzab() {
        super.zzab();
    }

    protected final String zzb(Bundle bundle) {
        if (bundle == null) {
            return null;
        }
        if (!zzik()) {
            return bundle.toString();
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String str : bundle.keySet()) {
            if (stringBuilder.length() != 0) {
                stringBuilder.append(", ");
            } else {
                stringBuilder.append("Bundle[{");
            }
            stringBuilder.append(zzbf(str));
            stringBuilder.append("=");
            stringBuilder.append(bundle.get(str));
        }
        stringBuilder.append("}]");
        return stringBuilder.toString();
    }

    protected final String zzbe(String str) {
        return str == null ? null : zzik() ? zza(str, Event.zzact, Event.zzacs, zzaij) : str;
    }

    protected final String zzbf(String str) {
        return str == null ? null : zzik() ? zza(str, Param.zzacv, Param.zzacu, zzaik) : str;
    }

    protected final String zzbg(String str) {
        if (str == null) {
            return null;
        }
        if (!zzik()) {
            return str;
        }
        if (!str.startsWith("_exp_")) {
            return zza(str, UserProperty.zzacx, UserProperty.zzacw, zzail);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("experiment_id");
        stringBuilder.append("(");
        stringBuilder.append(str);
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    public final /* bridge */ /* synthetic */ Clock zzbt() {
        return super.zzbt();
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

    protected final boolean zzhh() {
        return false;
    }
}
