package com.google.android.gms.measurement.internal;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader.ParseException;
import com.google.android.gms.common.util.Clock;
import com.google.android.gms.internal.measurement.zzfv;
import com.google.android.gms.internal.measurement.zzfw;
import com.google.android.gms.internal.measurement.zzfx;
import com.google.android.gms.internal.measurement.zzfy;
import com.google.android.gms.internal.measurement.zzfz;
import com.google.android.gms.internal.measurement.zzgd;
import com.google.android.gms.internal.measurement.zzgf;
import com.google.android.gms.internal.measurement.zzgg;
import com.google.android.gms.internal.measurement.zzgh;
import com.google.android.gms.internal.measurement.zzgi;
import com.google.android.gms.internal.measurement.zzgj;
import com.google.android.gms.internal.measurement.zzgl;
import com.google.android.gms.internal.measurement.zzyy;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.BitSet;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.telegram.messenger.NotificationBadge.NewHtcHomeBadger;

public final class zzfg extends zzez {
    zzfg(zzfa com_google_android_gms_measurement_internal_zzfa) {
        super(com_google_android_gms_measurement_internal_zzfa);
    }

    protected final boolean zzgt() {
        return false;
    }

    final void zza(zzgl com_google_android_gms_internal_measurement_zzgl, Object obj) {
        Preconditions.checkNotNull(obj);
        com_google_android_gms_internal_measurement_zzgl.zzamp = null;
        com_google_android_gms_internal_measurement_zzgl.zzawx = null;
        com_google_android_gms_internal_measurement_zzgl.zzauh = null;
        if (obj instanceof String) {
            com_google_android_gms_internal_measurement_zzgl.zzamp = (String) obj;
        } else if (obj instanceof Long) {
            com_google_android_gms_internal_measurement_zzgl.zzawx = (Long) obj;
        } else if (obj instanceof Double) {
            com_google_android_gms_internal_measurement_zzgl.zzauh = (Double) obj;
        } else {
            zzgo().zzjd().zzg("Ignoring invalid (type) user attribute value", obj);
        }
    }

    final void zza(zzgg com_google_android_gms_internal_measurement_zzgg, Object obj) {
        Preconditions.checkNotNull(obj);
        com_google_android_gms_internal_measurement_zzgg.zzamp = null;
        com_google_android_gms_internal_measurement_zzgg.zzawx = null;
        com_google_android_gms_internal_measurement_zzgg.zzauh = null;
        if (obj instanceof String) {
            com_google_android_gms_internal_measurement_zzgg.zzamp = (String) obj;
        } else if (obj instanceof Long) {
            com_google_android_gms_internal_measurement_zzgg.zzawx = (Long) obj;
        } else if (obj instanceof Double) {
            com_google_android_gms_internal_measurement_zzgg.zzauh = (Double) obj;
        } else {
            zzgo().zzjd().zzg("Ignoring invalid (type) event param value", obj);
        }
    }

    final byte[] zza(zzgh com_google_android_gms_internal_measurement_zzgh) {
        try {
            byte[] bArr = new byte[com_google_android_gms_internal_measurement_zzgh.zzvu()];
            zzyy zzk = zzyy.zzk(bArr, 0, bArr.length);
            com_google_android_gms_internal_measurement_zzgh.zza(zzk);
            zzk.zzyt();
            return bArr;
        } catch (IOException e) {
            zzgo().zzjd().zzg("Data loss. Failed to serialize batch", e);
            return null;
        }
    }

    static zzgg zza(zzgf com_google_android_gms_internal_measurement_zzgf, String str) {
        for (zzgg com_google_android_gms_internal_measurement_zzgg : com_google_android_gms_internal_measurement_zzgf.zzawt) {
            if (com_google_android_gms_internal_measurement_zzgg.name.equals(str)) {
                return com_google_android_gms_internal_measurement_zzgg;
            }
        }
        return null;
    }

    static Object zzb(zzgf com_google_android_gms_internal_measurement_zzgf, String str) {
        zzgg zza = zza(com_google_android_gms_internal_measurement_zzgf, str);
        if (zza != null) {
            if (zza.zzamp != null) {
                return zza.zzamp;
            }
            if (zza.zzawx != null) {
                return zza.zzawx;
            }
            if (zza.zzauh != null) {
                return zza.zzauh;
            }
        }
        return null;
    }

    static zzgg[] zza(zzgg[] com_google_android_gms_internal_measurement_zzggArr, String str, Object obj) {
        for (zzgg com_google_android_gms_internal_measurement_zzgg : com_google_android_gms_internal_measurement_zzggArr) {
            if (str.equals(com_google_android_gms_internal_measurement_zzgg.name)) {
                com_google_android_gms_internal_measurement_zzgg.zzawx = null;
                com_google_android_gms_internal_measurement_zzgg.zzamp = null;
                com_google_android_gms_internal_measurement_zzgg.zzauh = null;
                if (obj instanceof Long) {
                    com_google_android_gms_internal_measurement_zzgg.zzawx = (Long) obj;
                    return com_google_android_gms_internal_measurement_zzggArr;
                } else if (obj instanceof String) {
                    com_google_android_gms_internal_measurement_zzgg.zzamp = (String) obj;
                    return com_google_android_gms_internal_measurement_zzggArr;
                } else if (!(obj instanceof Double)) {
                    return com_google_android_gms_internal_measurement_zzggArr;
                } else {
                    com_google_android_gms_internal_measurement_zzgg.zzauh = (Double) obj;
                    return com_google_android_gms_internal_measurement_zzggArr;
                }
            }
        }
        Object obj2 = new zzgg[(com_google_android_gms_internal_measurement_zzggArr.length + 1)];
        System.arraycopy(com_google_android_gms_internal_measurement_zzggArr, 0, obj2, 0, com_google_android_gms_internal_measurement_zzggArr.length);
        zzgg com_google_android_gms_internal_measurement_zzgg2 = new zzgg();
        com_google_android_gms_internal_measurement_zzgg2.name = str;
        if (obj instanceof Long) {
            com_google_android_gms_internal_measurement_zzgg2.zzawx = (Long) obj;
        } else if (obj instanceof String) {
            com_google_android_gms_internal_measurement_zzgg2.zzamp = (String) obj;
        } else if (obj instanceof Double) {
            com_google_android_gms_internal_measurement_zzgg2.zzauh = (Double) obj;
        }
        obj2[com_google_android_gms_internal_measurement_zzggArr.length] = com_google_android_gms_internal_measurement_zzgg2;
        return obj2;
    }

    final String zzb(zzgh com_google_android_gms_internal_measurement_zzgh) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\nbatch {\n");
        if (com_google_android_gms_internal_measurement_zzgh.zzawy != null) {
            for (zzgi com_google_android_gms_internal_measurement_zzgi : com_google_android_gms_internal_measurement_zzgh.zzawy) {
                if (!(com_google_android_gms_internal_measurement_zzgi == null || com_google_android_gms_internal_measurement_zzgi == null)) {
                    zza(stringBuilder, 1);
                    stringBuilder.append("bundle {\n");
                    zza(stringBuilder, 1, "protocol_version", com_google_android_gms_internal_measurement_zzgi.zzaxa);
                    zza(stringBuilder, 1, "platform", com_google_android_gms_internal_measurement_zzgi.zzaxi);
                    zza(stringBuilder, 1, "gmp_version", com_google_android_gms_internal_measurement_zzgi.zzaxm);
                    zza(stringBuilder, 1, "uploading_gmp_version", com_google_android_gms_internal_measurement_zzgi.zzaxn);
                    zza(stringBuilder, 1, "config_version", com_google_android_gms_internal_measurement_zzgi.zzaxy);
                    zza(stringBuilder, 1, "gmp_app_id", com_google_android_gms_internal_measurement_zzgi.zzafx);
                    zza(stringBuilder, 1, "admob_app_id", com_google_android_gms_internal_measurement_zzgi.zzawj);
                    zza(stringBuilder, 1, "app_id", com_google_android_gms_internal_measurement_zzgi.zztt);
                    zza(stringBuilder, 1, "app_version", com_google_android_gms_internal_measurement_zzgi.zzts);
                    zza(stringBuilder, 1, "app_version_major", com_google_android_gms_internal_measurement_zzgi.zzaxu);
                    zza(stringBuilder, 1, "firebase_instance_id", com_google_android_gms_internal_measurement_zzgi.zzafz);
                    zza(stringBuilder, 1, "dev_cert_hash", com_google_android_gms_internal_measurement_zzgi.zzaxq);
                    zza(stringBuilder, 1, "app_store", com_google_android_gms_internal_measurement_zzgi.zzage);
                    zza(stringBuilder, 1, "upload_timestamp_millis", com_google_android_gms_internal_measurement_zzgi.zzaxd);
                    zza(stringBuilder, 1, "start_timestamp_millis", com_google_android_gms_internal_measurement_zzgi.zzaxe);
                    zza(stringBuilder, 1, "end_timestamp_millis", com_google_android_gms_internal_measurement_zzgi.zzaxf);
                    zza(stringBuilder, 1, "previous_bundle_start_timestamp_millis", com_google_android_gms_internal_measurement_zzgi.zzaxg);
                    zza(stringBuilder, 1, "previous_bundle_end_timestamp_millis", com_google_android_gms_internal_measurement_zzgi.zzaxh);
                    zza(stringBuilder, 1, "app_instance_id", com_google_android_gms_internal_measurement_zzgi.zzafw);
                    zza(stringBuilder, 1, "resettable_device_id", com_google_android_gms_internal_measurement_zzgi.zzaxo);
                    zza(stringBuilder, 1, "device_id", com_google_android_gms_internal_measurement_zzgi.zzaxx);
                    zza(stringBuilder, 1, "ds_id", com_google_android_gms_internal_measurement_zzgi.zzaya);
                    zza(stringBuilder, 1, "limited_ad_tracking", com_google_android_gms_internal_measurement_zzgi.zzaxp);
                    zza(stringBuilder, 1, "os_version", com_google_android_gms_internal_measurement_zzgi.zzaxj);
                    zza(stringBuilder, 1, "device_model", com_google_android_gms_internal_measurement_zzgi.zzaxk);
                    zza(stringBuilder, 1, "user_default_language", com_google_android_gms_internal_measurement_zzgi.zzaia);
                    zza(stringBuilder, 1, "time_zone_offset_minutes", com_google_android_gms_internal_measurement_zzgi.zzaxl);
                    zza(stringBuilder, 1, "bundle_sequential_index", com_google_android_gms_internal_measurement_zzgi.zzaxr);
                    zza(stringBuilder, 1, "service_upload", com_google_android_gms_internal_measurement_zzgi.zzaxs);
                    zza(stringBuilder, 1, "health_monitor", com_google_android_gms_internal_measurement_zzgi.zzagv);
                    if (!(com_google_android_gms_internal_measurement_zzgi.zzaxz == null || com_google_android_gms_internal_measurement_zzgi.zzaxz.longValue() == 0)) {
                        zza(stringBuilder, 1, "android_id", com_google_android_gms_internal_measurement_zzgi.zzaxz);
                    }
                    if (com_google_android_gms_internal_measurement_zzgi.zzayc != null) {
                        zza(stringBuilder, 1, "retry_counter", com_google_android_gms_internal_measurement_zzgi.zzayc);
                    }
                    zzgl[] com_google_android_gms_internal_measurement_zzglArr = com_google_android_gms_internal_measurement_zzgi.zzaxc;
                    if (com_google_android_gms_internal_measurement_zzglArr != null) {
                        for (zzgl com_google_android_gms_internal_measurement_zzgl : com_google_android_gms_internal_measurement_zzglArr) {
                            if (com_google_android_gms_internal_measurement_zzgl != null) {
                                zza(stringBuilder, 2);
                                stringBuilder.append("user_property {\n");
                                zza(stringBuilder, 2, "set_timestamp_millis", com_google_android_gms_internal_measurement_zzgl.zzayl);
                                zza(stringBuilder, 2, "name", zzgl().zzbu(com_google_android_gms_internal_measurement_zzgl.name));
                                zza(stringBuilder, 2, "string_value", com_google_android_gms_internal_measurement_zzgl.zzamp);
                                zza(stringBuilder, 2, "int_value", com_google_android_gms_internal_measurement_zzgl.zzawx);
                                zza(stringBuilder, 2, "double_value", com_google_android_gms_internal_measurement_zzgl.zzauh);
                                zza(stringBuilder, 2);
                                stringBuilder.append("}\n");
                            }
                        }
                    }
                    zzgd[] com_google_android_gms_internal_measurement_zzgdArr = com_google_android_gms_internal_measurement_zzgi.zzaxt;
                    if (com_google_android_gms_internal_measurement_zzgdArr != null) {
                        for (zzgd com_google_android_gms_internal_measurement_zzgd : com_google_android_gms_internal_measurement_zzgdArr) {
                            if (com_google_android_gms_internal_measurement_zzgd != null) {
                                zza(stringBuilder, 2);
                                stringBuilder.append("audience_membership {\n");
                                zza(stringBuilder, 2, "audience_id", com_google_android_gms_internal_measurement_zzgd.zzauy);
                                zza(stringBuilder, 2, "new_audience", com_google_android_gms_internal_measurement_zzgd.zzawo);
                                zza(stringBuilder, 2, "current_data", com_google_android_gms_internal_measurement_zzgd.zzawm);
                                zza(stringBuilder, 2, "previous_data", com_google_android_gms_internal_measurement_zzgd.zzawn);
                                zza(stringBuilder, 2);
                                stringBuilder.append("}\n");
                            }
                        }
                    }
                    zzgf[] com_google_android_gms_internal_measurement_zzgfArr = com_google_android_gms_internal_measurement_zzgi.zzaxb;
                    if (com_google_android_gms_internal_measurement_zzgfArr != null) {
                        for (zzgf com_google_android_gms_internal_measurement_zzgf : com_google_android_gms_internal_measurement_zzgfArr) {
                            if (com_google_android_gms_internal_measurement_zzgf != null) {
                                zza(stringBuilder, 2);
                                stringBuilder.append("event {\n");
                                zza(stringBuilder, 2, "name", zzgl().zzbs(com_google_android_gms_internal_measurement_zzgf.name));
                                zza(stringBuilder, 2, "timestamp_millis", com_google_android_gms_internal_measurement_zzgf.zzawu);
                                zza(stringBuilder, 2, "previous_timestamp_millis", com_google_android_gms_internal_measurement_zzgf.zzawv);
                                zza(stringBuilder, 2, NewHtcHomeBadger.COUNT, com_google_android_gms_internal_measurement_zzgf.count);
                                zzgg[] com_google_android_gms_internal_measurement_zzggArr = com_google_android_gms_internal_measurement_zzgf.zzawt;
                                if (com_google_android_gms_internal_measurement_zzggArr != null) {
                                    for (zzgg com_google_android_gms_internal_measurement_zzgg : com_google_android_gms_internal_measurement_zzggArr) {
                                        if (com_google_android_gms_internal_measurement_zzgg != null) {
                                            zza(stringBuilder, 3);
                                            stringBuilder.append("param {\n");
                                            zza(stringBuilder, 3, "name", zzgl().zzbt(com_google_android_gms_internal_measurement_zzgg.name));
                                            zza(stringBuilder, 3, "string_value", com_google_android_gms_internal_measurement_zzgg.zzamp);
                                            zza(stringBuilder, 3, "int_value", com_google_android_gms_internal_measurement_zzgg.zzawx);
                                            zza(stringBuilder, 3, "double_value", com_google_android_gms_internal_measurement_zzgg.zzauh);
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

    final String zza(zzfv com_google_android_gms_internal_measurement_zzfv) {
        int i = 0;
        if (com_google_android_gms_internal_measurement_zzfv == null) {
            return "null";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\nevent_filter {\n");
        zza(stringBuilder, 0, "filter_id", com_google_android_gms_internal_measurement_zzfv.zzave);
        zza(stringBuilder, 0, "event_name", zzgl().zzbs(com_google_android_gms_internal_measurement_zzfv.zzavf));
        zza(stringBuilder, 1, "event_count_filter", com_google_android_gms_internal_measurement_zzfv.zzavi);
        stringBuilder.append("  filters {\n");
        zzfw[] com_google_android_gms_internal_measurement_zzfwArr = com_google_android_gms_internal_measurement_zzfv.zzavg;
        int length = com_google_android_gms_internal_measurement_zzfwArr.length;
        while (i < length) {
            zza(stringBuilder, 2, com_google_android_gms_internal_measurement_zzfwArr[i]);
            i++;
        }
        zza(stringBuilder, 1);
        stringBuilder.append("}\n}\n");
        return stringBuilder.toString();
    }

    final String zza(zzfy com_google_android_gms_internal_measurement_zzfy) {
        if (com_google_android_gms_internal_measurement_zzfy == null) {
            return "null";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\nproperty_filter {\n");
        zza(stringBuilder, 0, "filter_id", com_google_android_gms_internal_measurement_zzfy.zzave);
        zza(stringBuilder, 0, "property_name", zzgl().zzbu(com_google_android_gms_internal_measurement_zzfy.zzavu));
        zza(stringBuilder, 1, com_google_android_gms_internal_measurement_zzfy.zzavv);
        stringBuilder.append("}\n");
        return stringBuilder.toString();
    }

    private static void zza(StringBuilder stringBuilder, int i, String str, zzgj com_google_android_gms_internal_measurement_zzgj) {
        if (com_google_android_gms_internal_measurement_zzgj != null) {
            int i2;
            int i3;
            zza(stringBuilder, 3);
            stringBuilder.append(str);
            stringBuilder.append(" {\n");
            if (com_google_android_gms_internal_measurement_zzgj.zzayf != null) {
                zza(stringBuilder, 4);
                stringBuilder.append("results: ");
                long[] jArr = com_google_android_gms_internal_measurement_zzgj.zzayf;
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
            if (com_google_android_gms_internal_measurement_zzgj.zzaye != null) {
                zza(stringBuilder, 4);
                stringBuilder.append("status: ");
                long[] jArr2 = com_google_android_gms_internal_measurement_zzgj.zzaye;
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

    private final void zza(StringBuilder stringBuilder, int i, String str, zzfx com_google_android_gms_internal_measurement_zzfx) {
        if (com_google_android_gms_internal_measurement_zzfx != null) {
            zza(stringBuilder, i);
            stringBuilder.append(str);
            stringBuilder.append(" {\n");
            if (com_google_android_gms_internal_measurement_zzfx.zzavo != null) {
                Object obj = "UNKNOWN_COMPARISON_TYPE";
                switch (com_google_android_gms_internal_measurement_zzfx.zzavo.intValue()) {
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
            zza(stringBuilder, i, "match_as_float", com_google_android_gms_internal_measurement_zzfx.zzavp);
            zza(stringBuilder, i, "comparison_value", com_google_android_gms_internal_measurement_zzfx.zzavq);
            zza(stringBuilder, i, "min_comparison_value", com_google_android_gms_internal_measurement_zzfx.zzavr);
            zza(stringBuilder, i, "max_comparison_value", com_google_android_gms_internal_measurement_zzfx.zzavs);
            zza(stringBuilder, i);
            stringBuilder.append("}\n");
        }
    }

    private final void zza(StringBuilder stringBuilder, int i, zzfw com_google_android_gms_internal_measurement_zzfw) {
        if (com_google_android_gms_internal_measurement_zzfw != null) {
            zza(stringBuilder, i);
            stringBuilder.append("filter {\n");
            zza(stringBuilder, i, "complement", com_google_android_gms_internal_measurement_zzfw.zzavm);
            zza(stringBuilder, i, "param_name", zzgl().zzbt(com_google_android_gms_internal_measurement_zzfw.zzavn));
            int i2 = i + 1;
            String str = "string_filter";
            zzfz com_google_android_gms_internal_measurement_zzfz = com_google_android_gms_internal_measurement_zzfw.zzavk;
            if (com_google_android_gms_internal_measurement_zzfz != null) {
                zza(stringBuilder, i2);
                stringBuilder.append(str);
                stringBuilder.append(" {\n");
                if (com_google_android_gms_internal_measurement_zzfz.zzavw != null) {
                    Object obj = "UNKNOWN_MATCH_TYPE";
                    switch (com_google_android_gms_internal_measurement_zzfz.zzavw.intValue()) {
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
                zza(stringBuilder, i2, "expression", com_google_android_gms_internal_measurement_zzfz.zzavx);
                zza(stringBuilder, i2, "case_sensitive", com_google_android_gms_internal_measurement_zzfz.zzavy);
                if (com_google_android_gms_internal_measurement_zzfz.zzavz.length > 0) {
                    zza(stringBuilder, i2 + 1);
                    stringBuilder.append("expression_list {\n");
                    for (String str2 : com_google_android_gms_internal_measurement_zzfz.zzavz) {
                        zza(stringBuilder, i2 + 2);
                        stringBuilder.append(str2);
                        stringBuilder.append("\n");
                    }
                    stringBuilder.append("}\n");
                }
                zza(stringBuilder, i2);
                stringBuilder.append("}\n");
            }
            zza(stringBuilder, i + 1, "number_filter", com_google_android_gms_internal_measurement_zzfw.zzavl);
            zza(stringBuilder, i);
            stringBuilder.append("}\n");
        }
    }

    private static void zza(StringBuilder stringBuilder, int i) {
        for (int i2 = 0; i2 < i; i2++) {
            stringBuilder.append("  ");
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

    final <T extends Parcelable> T zza(byte[] bArr, Creator<T> creator) {
        if (bArr == null) {
            return null;
        }
        Parcel obtain = Parcel.obtain();
        T t;
        try {
            obtain.unmarshall(bArr, 0, bArr.length);
            obtain.setDataPosition(0);
            t = (Parcelable) creator.createFromParcel(obtain);
            return t;
        } catch (ParseException e) {
            t = zzgo().zzjd();
            t.zzbx("Failed to load parcelable from buffer");
            return null;
        } finally {
            obtain.recycle();
        }
    }

    final boolean zze(zzad com_google_android_gms_measurement_internal_zzad, zzh com_google_android_gms_measurement_internal_zzh) {
        Preconditions.checkNotNull(com_google_android_gms_measurement_internal_zzad);
        Preconditions.checkNotNull(com_google_android_gms_measurement_internal_zzh);
        if (!TextUtils.isEmpty(com_google_android_gms_measurement_internal_zzh.zzafx) || !TextUtils.isEmpty(com_google_android_gms_measurement_internal_zzh.zzagk)) {
            return true;
        }
        zzgr();
        return false;
    }

    static boolean zzcp(String str) {
        return str != null && str.matches("([+-])?([0-9]+\\.?[0-9]*|[0-9]*\\.?[0-9]+)") && str.length() <= 310;
    }

    static boolean zza(long[] jArr, int i) {
        if (i < (jArr.length << 6) && (jArr[i / 64] & (1 << (i % 64))) != 0) {
            return true;
        }
        return false;
    }

    static long[] zza(BitSet bitSet) {
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

    final boolean zzb(long j, long j2) {
        if (j == 0 || j2 <= 0 || Math.abs(zzbx().currentTimeMillis() - j) > j2) {
            return true;
        }
        return false;
    }

    final byte[] zza(byte[] bArr) throws IOException {
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
            zzgo().zzjd().zzg("Failed to ungzip content", e);
            throw e;
        }
    }

    final byte[] zzb(byte[] bArr) throws IOException {
        try {
            OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            GZIPOutputStream gZIPOutputStream = new GZIPOutputStream(byteArrayOutputStream);
            gZIPOutputStream.write(bArr);
            gZIPOutputStream.close();
            byteArrayOutputStream.close();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            zzgo().zzjd().zzg("Failed to gzip content", e);
            throw e;
        }
    }

    public final /* bridge */ /* synthetic */ zzfg zzjo() {
        return super.zzjo();
    }

    public final /* bridge */ /* synthetic */ zzj zzjp() {
        return super.zzjp();
    }

    public final /* bridge */ /* synthetic */ zzq zzjq() {
        return super.zzjq();
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
