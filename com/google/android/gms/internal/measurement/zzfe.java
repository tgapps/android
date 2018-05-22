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

public final class zzfe extends zzhh {
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
            if (zzka.zzs(str, strArr[i])) {
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

    private final void zza(StringBuilder stringBuilder, int i, zzkf com_google_android_gms_internal_measurement_zzkf) {
        if (com_google_android_gms_internal_measurement_zzkf != null) {
            zza(stringBuilder, i);
            stringBuilder.append("filter {\n");
            zza(stringBuilder, i, "complement", com_google_android_gms_internal_measurement_zzkf.zzarx);
            zza(stringBuilder, i, "param_name", zzbk(com_google_android_gms_internal_measurement_zzkf.zzary));
            int i2 = i + 1;
            String str = "string_filter";
            zzki com_google_android_gms_internal_measurement_zzki = com_google_android_gms_internal_measurement_zzkf.zzarv;
            if (com_google_android_gms_internal_measurement_zzki != null) {
                zza(stringBuilder, i2);
                stringBuilder.append(str);
                stringBuilder.append(" {\n");
                if (com_google_android_gms_internal_measurement_zzki.zzash != null) {
                    Object obj = "UNKNOWN_MATCH_TYPE";
                    switch (com_google_android_gms_internal_measurement_zzki.zzash.intValue()) {
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
                zza(stringBuilder, i2, "expression", com_google_android_gms_internal_measurement_zzki.zzasi);
                zza(stringBuilder, i2, "case_sensitive", com_google_android_gms_internal_measurement_zzki.zzasj);
                if (com_google_android_gms_internal_measurement_zzki.zzask.length > 0) {
                    zza(stringBuilder, i2 + 1);
                    stringBuilder.append("expression_list {\n");
                    for (String str2 : com_google_android_gms_internal_measurement_zzki.zzask) {
                        zza(stringBuilder, i2 + 2);
                        stringBuilder.append(str2);
                        stringBuilder.append("\n");
                    }
                    stringBuilder.append("}\n");
                }
                zza(stringBuilder, i2);
                stringBuilder.append("}\n");
            }
            zza(stringBuilder, i + 1, "number_filter", com_google_android_gms_internal_measurement_zzkf.zzarw);
            zza(stringBuilder, i);
            stringBuilder.append("}\n");
        }
    }

    private final void zza(StringBuilder stringBuilder, int i, String str, zzkg com_google_android_gms_internal_measurement_zzkg) {
        if (com_google_android_gms_internal_measurement_zzkg != null) {
            zza(stringBuilder, i);
            stringBuilder.append(str);
            stringBuilder.append(" {\n");
            if (com_google_android_gms_internal_measurement_zzkg.zzarz != null) {
                Object obj = "UNKNOWN_COMPARISON_TYPE";
                switch (com_google_android_gms_internal_measurement_zzkg.zzarz.intValue()) {
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
            zza(stringBuilder, i, "match_as_float", com_google_android_gms_internal_measurement_zzkg.zzasa);
            zza(stringBuilder, i, "comparison_value", com_google_android_gms_internal_measurement_zzkg.zzasb);
            zza(stringBuilder, i, "min_comparison_value", com_google_android_gms_internal_measurement_zzkg.zzasc);
            zza(stringBuilder, i, "max_comparison_value", com_google_android_gms_internal_measurement_zzkg.zzasd);
            zza(stringBuilder, i);
            stringBuilder.append("}\n");
        }
    }

    private static void zza(StringBuilder stringBuilder, int i, String str, zzkr com_google_android_gms_internal_measurement_zzkr) {
        if (com_google_android_gms_internal_measurement_zzkr != null) {
            int i2;
            int i3;
            zza(stringBuilder, 3);
            stringBuilder.append(str);
            stringBuilder.append(" {\n");
            if (com_google_android_gms_internal_measurement_zzkr.zzaul != null) {
                zza(stringBuilder, 4);
                stringBuilder.append("results: ");
                long[] jArr = com_google_android_gms_internal_measurement_zzkr.zzaul;
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
            if (com_google_android_gms_internal_measurement_zzkr.zzauk != null) {
                zza(stringBuilder, 4);
                stringBuilder.append("status: ");
                long[] jArr2 = com_google_android_gms_internal_measurement_zzkr.zzauk;
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

    private final String zzb(zzer com_google_android_gms_internal_measurement_zzer) {
        return com_google_android_gms_internal_measurement_zzer == null ? null : !zzil() ? com_google_android_gms_internal_measurement_zzer.toString() : zzb(com_google_android_gms_internal_measurement_zzer.zzif());
    }

    private final boolean zzil() {
        return this.zzacw.zzge().isLoggable(3);
    }

    public final /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    protected final String zza(zzep com_google_android_gms_internal_measurement_zzep) {
        if (com_google_android_gms_internal_measurement_zzep == null) {
            return null;
        }
        if (!zzil()) {
            return com_google_android_gms_internal_measurement_zzep.toString();
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Event{appId='");
        stringBuilder.append(com_google_android_gms_internal_measurement_zzep.zzti);
        stringBuilder.append("', name='");
        stringBuilder.append(zzbj(com_google_android_gms_internal_measurement_zzep.name));
        stringBuilder.append("', params=");
        stringBuilder.append(zzb(com_google_android_gms_internal_measurement_zzep.zzafq));
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    protected final String zza(zzke com_google_android_gms_internal_measurement_zzke) {
        int i = 0;
        if (com_google_android_gms_internal_measurement_zzke == null) {
            return "null";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\nevent_filter {\n");
        zza(stringBuilder, 0, "filter_id", com_google_android_gms_internal_measurement_zzke.zzarp);
        zza(stringBuilder, 0, "event_name", zzbj(com_google_android_gms_internal_measurement_zzke.zzarq));
        zza(stringBuilder, 1, "event_count_filter", com_google_android_gms_internal_measurement_zzke.zzart);
        stringBuilder.append("  filters {\n");
        zzkf[] com_google_android_gms_internal_measurement_zzkfArr = com_google_android_gms_internal_measurement_zzke.zzarr;
        int length = com_google_android_gms_internal_measurement_zzkfArr.length;
        while (i < length) {
            zza(stringBuilder, 2, com_google_android_gms_internal_measurement_zzkfArr[i]);
            i++;
        }
        zza(stringBuilder, 1);
        stringBuilder.append("}\n}\n");
        return stringBuilder.toString();
    }

    protected final String zza(zzkh com_google_android_gms_internal_measurement_zzkh) {
        if (com_google_android_gms_internal_measurement_zzkh == null) {
            return "null";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\nproperty_filter {\n");
        zza(stringBuilder, 0, "filter_id", com_google_android_gms_internal_measurement_zzkh.zzarp);
        zza(stringBuilder, 0, "property_name", zzbl(com_google_android_gms_internal_measurement_zzkh.zzasf));
        zza(stringBuilder, 1, com_google_android_gms_internal_measurement_zzkh.zzasg);
        stringBuilder.append("}\n");
        return stringBuilder.toString();
    }

    protected final String zza(zzkp com_google_android_gms_internal_measurement_zzkp) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\nbatch {\n");
        if (com_google_android_gms_internal_measurement_zzkp.zzatf != null) {
            for (zzkq com_google_android_gms_internal_measurement_zzkq : com_google_android_gms_internal_measurement_zzkp.zzatf) {
                if (!(com_google_android_gms_internal_measurement_zzkq == null || com_google_android_gms_internal_measurement_zzkq == null)) {
                    zza(stringBuilder, 1);
                    stringBuilder.append("bundle {\n");
                    zza(stringBuilder, 1, "protocol_version", com_google_android_gms_internal_measurement_zzkq.zzath);
                    zza(stringBuilder, 1, "platform", com_google_android_gms_internal_measurement_zzkq.zzatp);
                    zza(stringBuilder, 1, "gmp_version", com_google_android_gms_internal_measurement_zzkq.zzatt);
                    zza(stringBuilder, 1, "uploading_gmp_version", com_google_android_gms_internal_measurement_zzkq.zzatu);
                    zza(stringBuilder, 1, "config_version", com_google_android_gms_internal_measurement_zzkq.zzauf);
                    zza(stringBuilder, 1, "gmp_app_id", com_google_android_gms_internal_measurement_zzkq.zzadm);
                    zza(stringBuilder, 1, "app_id", com_google_android_gms_internal_measurement_zzkq.zzti);
                    zza(stringBuilder, 1, "app_version", com_google_android_gms_internal_measurement_zzkq.zzth);
                    zza(stringBuilder, 1, "app_version_major", com_google_android_gms_internal_measurement_zzkq.zzaub);
                    zza(stringBuilder, 1, "firebase_instance_id", com_google_android_gms_internal_measurement_zzkq.zzado);
                    zza(stringBuilder, 1, "dev_cert_hash", com_google_android_gms_internal_measurement_zzkq.zzatx);
                    zza(stringBuilder, 1, "app_store", com_google_android_gms_internal_measurement_zzkq.zzadt);
                    zza(stringBuilder, 1, "upload_timestamp_millis", com_google_android_gms_internal_measurement_zzkq.zzatk);
                    zza(stringBuilder, 1, "start_timestamp_millis", com_google_android_gms_internal_measurement_zzkq.zzatl);
                    zza(stringBuilder, 1, "end_timestamp_millis", com_google_android_gms_internal_measurement_zzkq.zzatm);
                    zza(stringBuilder, 1, "previous_bundle_start_timestamp_millis", com_google_android_gms_internal_measurement_zzkq.zzatn);
                    zza(stringBuilder, 1, "previous_bundle_end_timestamp_millis", com_google_android_gms_internal_measurement_zzkq.zzato);
                    zza(stringBuilder, 1, "app_instance_id", com_google_android_gms_internal_measurement_zzkq.zzadl);
                    zza(stringBuilder, 1, "resettable_device_id", com_google_android_gms_internal_measurement_zzkq.zzatv);
                    zza(stringBuilder, 1, "device_id", com_google_android_gms_internal_measurement_zzkq.zzaue);
                    zza(stringBuilder, 1, "limited_ad_tracking", com_google_android_gms_internal_measurement_zzkq.zzatw);
                    zza(stringBuilder, 1, "os_version", com_google_android_gms_internal_measurement_zzkq.zzatq);
                    zza(stringBuilder, 1, "device_model", com_google_android_gms_internal_measurement_zzkq.zzatr);
                    zza(stringBuilder, 1, "user_default_language", com_google_android_gms_internal_measurement_zzkq.zzafn);
                    zza(stringBuilder, 1, "time_zone_offset_minutes", com_google_android_gms_internal_measurement_zzkq.zzats);
                    zza(stringBuilder, 1, "bundle_sequential_index", com_google_android_gms_internal_measurement_zzkq.zzaty);
                    zza(stringBuilder, 1, "service_upload", com_google_android_gms_internal_measurement_zzkq.zzatz);
                    zza(stringBuilder, 1, "health_monitor", com_google_android_gms_internal_measurement_zzkq.zzaek);
                    if (!(com_google_android_gms_internal_measurement_zzkq.zzaug == null || com_google_android_gms_internal_measurement_zzkq.zzaug.longValue() == 0)) {
                        zza(stringBuilder, 1, "android_id", com_google_android_gms_internal_measurement_zzkq.zzaug);
                    }
                    if (com_google_android_gms_internal_measurement_zzkq.zzauj != null) {
                        zza(stringBuilder, 1, "retry_counter", com_google_android_gms_internal_measurement_zzkq.zzauj);
                    }
                    zzks[] com_google_android_gms_internal_measurement_zzksArr = com_google_android_gms_internal_measurement_zzkq.zzatj;
                    if (com_google_android_gms_internal_measurement_zzksArr != null) {
                        for (zzks com_google_android_gms_internal_measurement_zzks : com_google_android_gms_internal_measurement_zzksArr) {
                            if (com_google_android_gms_internal_measurement_zzks != null) {
                                zza(stringBuilder, 2);
                                stringBuilder.append("user_property {\n");
                                zza(stringBuilder, 2, "set_timestamp_millis", com_google_android_gms_internal_measurement_zzks.zzaun);
                                zza(stringBuilder, 2, "name", zzbl(com_google_android_gms_internal_measurement_zzks.name));
                                zza(stringBuilder, 2, "string_value", com_google_android_gms_internal_measurement_zzks.zzajf);
                                zza(stringBuilder, 2, "int_value", com_google_android_gms_internal_measurement_zzks.zzate);
                                zza(stringBuilder, 2, "double_value", com_google_android_gms_internal_measurement_zzks.zzarc);
                                zza(stringBuilder, 2);
                                stringBuilder.append("}\n");
                            }
                        }
                    }
                    zzkm[] com_google_android_gms_internal_measurement_zzkmArr = com_google_android_gms_internal_measurement_zzkq.zzaua;
                    if (com_google_android_gms_internal_measurement_zzkmArr != null) {
                        for (zzkm com_google_android_gms_internal_measurement_zzkm : com_google_android_gms_internal_measurement_zzkmArr) {
                            if (com_google_android_gms_internal_measurement_zzkm != null) {
                                zza(stringBuilder, 2);
                                stringBuilder.append("audience_membership {\n");
                                zza(stringBuilder, 2, "audience_id", com_google_android_gms_internal_measurement_zzkm.zzarl);
                                zza(stringBuilder, 2, "new_audience", com_google_android_gms_internal_measurement_zzkm.zzasy);
                                zza(stringBuilder, 2, "current_data", com_google_android_gms_internal_measurement_zzkm.zzasw);
                                zza(stringBuilder, 2, "previous_data", com_google_android_gms_internal_measurement_zzkm.zzasx);
                                zza(stringBuilder, 2);
                                stringBuilder.append("}\n");
                            }
                        }
                    }
                    zzkn[] com_google_android_gms_internal_measurement_zzknArr = com_google_android_gms_internal_measurement_zzkq.zzati;
                    if (com_google_android_gms_internal_measurement_zzknArr != null) {
                        for (zzkn com_google_android_gms_internal_measurement_zzkn : com_google_android_gms_internal_measurement_zzknArr) {
                            if (com_google_android_gms_internal_measurement_zzkn != null) {
                                zza(stringBuilder, 2);
                                stringBuilder.append("event {\n");
                                zza(stringBuilder, 2, "name", zzbj(com_google_android_gms_internal_measurement_zzkn.name));
                                zza(stringBuilder, 2, "timestamp_millis", com_google_android_gms_internal_measurement_zzkn.zzatb);
                                zza(stringBuilder, 2, "previous_timestamp_millis", com_google_android_gms_internal_measurement_zzkn.zzatc);
                                zza(stringBuilder, 2, NewHtcHomeBadger.COUNT, com_google_android_gms_internal_measurement_zzkn.count);
                                zzko[] com_google_android_gms_internal_measurement_zzkoArr = com_google_android_gms_internal_measurement_zzkn.zzata;
                                if (com_google_android_gms_internal_measurement_zzkoArr != null) {
                                    for (zzko com_google_android_gms_internal_measurement_zzko : com_google_android_gms_internal_measurement_zzkoArr) {
                                        if (com_google_android_gms_internal_measurement_zzko != null) {
                                            zza(stringBuilder, 3);
                                            stringBuilder.append("param {\n");
                                            zza(stringBuilder, 3, "name", zzbk(com_google_android_gms_internal_measurement_zzko.name));
                                            zza(stringBuilder, 3, "string_value", com_google_android_gms_internal_measurement_zzko.zzajf);
                                            zza(stringBuilder, 3, "int_value", com_google_android_gms_internal_measurement_zzko.zzate);
                                            zza(stringBuilder, 3, "double_value", com_google_android_gms_internal_measurement_zzko.zzarc);
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
        if (!zzil()) {
            return bundle.toString();
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String str : bundle.keySet()) {
            if (stringBuilder.length() != 0) {
                stringBuilder.append(", ");
            } else {
                stringBuilder.append("Bundle[{");
            }
            stringBuilder.append(zzbk(str));
            stringBuilder.append("=");
            stringBuilder.append(bundle.get(str));
        }
        stringBuilder.append("}]");
        return stringBuilder.toString();
    }

    protected final String zzb(zzeu com_google_android_gms_internal_measurement_zzeu) {
        if (com_google_android_gms_internal_measurement_zzeu == null) {
            return null;
        }
        if (!zzil()) {
            return com_google_android_gms_internal_measurement_zzeu.toString();
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("origin=");
        stringBuilder.append(com_google_android_gms_internal_measurement_zzeu.origin);
        stringBuilder.append(",name=");
        stringBuilder.append(zzbj(com_google_android_gms_internal_measurement_zzeu.name));
        stringBuilder.append(",params=");
        stringBuilder.append(zzb(com_google_android_gms_internal_measurement_zzeu.zzafq));
        return stringBuilder.toString();
    }

    protected final String zzbj(String str) {
        return str == null ? null : zzil() ? zza(str, Event.zzacy, Event.zzacx, zzaij) : str;
    }

    protected final String zzbk(String str) {
        return str == null ? null : zzil() ? zza(str, Param.zzada, Param.zzacz, zzaik) : str;
    }

    protected final String zzbl(String str) {
        if (str == null) {
            return null;
        }
        if (!zzil()) {
            return str;
        }
        if (!str.startsWith("_exp_")) {
            return zza(str, UserProperty.zzadc, UserProperty.zzadb, zzail);
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

    protected final boolean zzhf() {
        return false;
    }
}
