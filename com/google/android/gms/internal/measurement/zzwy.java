package com.google.android.gms.internal.measurement;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;
import org.telegram.tgnet.ConnectionsManager;

final class zzwy<T> implements zzxj<T> {
    private final zzwt zzcbd;
    private final boolean zzcbe;
    private final zzyb<?, ?> zzcbn;
    private final zzva<?> zzcbo;

    private zzwy(zzyb<?, ?> com_google_android_gms_internal_measurement_zzyb___, zzva<?> com_google_android_gms_internal_measurement_zzva_, zzwt com_google_android_gms_internal_measurement_zzwt) {
        this.zzcbn = com_google_android_gms_internal_measurement_zzyb___;
        this.zzcbe = com_google_android_gms_internal_measurement_zzva_.zze(com_google_android_gms_internal_measurement_zzwt);
        this.zzcbo = com_google_android_gms_internal_measurement_zzva_;
        this.zzcbd = com_google_android_gms_internal_measurement_zzwt;
    }

    static <T> zzwy<T> zza(zzyb<?, ?> com_google_android_gms_internal_measurement_zzyb___, zzva<?> com_google_android_gms_internal_measurement_zzva_, zzwt com_google_android_gms_internal_measurement_zzwt) {
        return new zzwy(com_google_android_gms_internal_measurement_zzyb___, com_google_android_gms_internal_measurement_zzva_, com_google_android_gms_internal_measurement_zzwt);
    }

    public final T newInstance() {
        return this.zzcbd.zzwe().zzwi();
    }

    public final boolean equals(T t, T t2) {
        if (!this.zzcbn.zzah(t).equals(this.zzcbn.zzah(t2))) {
            return false;
        }
        if (this.zzcbe) {
            return this.zzcbo.zzs(t).equals(this.zzcbo.zzs(t2));
        }
        return true;
    }

    public final int hashCode(T t) {
        int hashCode = this.zzcbn.zzah(t).hashCode();
        if (this.zzcbe) {
            return (hashCode * 53) + this.zzcbo.zzs(t).hashCode();
        }
        return hashCode;
    }

    public final void zzd(T t, T t2) {
        zzxl.zza(this.zzcbn, (Object) t, (Object) t2);
        if (this.zzcbe) {
            zzxl.zza(this.zzcbo, (Object) t, (Object) t2);
        }
    }

    public final void zza(T t, zzyw com_google_android_gms_internal_measurement_zzyw) throws IOException {
        Iterator it = this.zzcbo.zzs(t).iterator();
        while (it.hasNext()) {
            Entry entry = (Entry) it.next();
            zzvf com_google_android_gms_internal_measurement_zzvf = (zzvf) entry.getKey();
            if (com_google_android_gms_internal_measurement_zzvf.zzvx() != zzyv.MESSAGE || com_google_android_gms_internal_measurement_zzvf.zzvy() || com_google_android_gms_internal_measurement_zzvf.zzvz()) {
                throw new IllegalStateException("Found invalid MessageSet item.");
            } else if (entry instanceof zzvy) {
                com_google_android_gms_internal_measurement_zzyw.zza(com_google_android_gms_internal_measurement_zzvf.zzc(), ((zzvy) entry).zzwu().zztt());
            } else {
                com_google_android_gms_internal_measurement_zzyw.zza(com_google_android_gms_internal_measurement_zzvf.zzc(), entry.getValue());
            }
        }
        zzyb com_google_android_gms_internal_measurement_zzyb = this.zzcbn;
        com_google_android_gms_internal_measurement_zzyb.zzc(com_google_android_gms_internal_measurement_zzyb.zzah(t), com_google_android_gms_internal_measurement_zzyw);
    }

    public final void zza(T t, zzxi com_google_android_gms_internal_measurement_zzxi, zzuz com_google_android_gms_internal_measurement_zzuz) throws IOException {
        zzyb com_google_android_gms_internal_measurement_zzyb = this.zzcbn;
        zzva com_google_android_gms_internal_measurement_zzva = this.zzcbo;
        Object zzai = com_google_android_gms_internal_measurement_zzyb.zzai(t);
        zzvd zzt = com_google_android_gms_internal_measurement_zzva.zzt(t);
        while (com_google_android_gms_internal_measurement_zzxi.zzve() != ConnectionsManager.DEFAULT_DATACENTER_ID) {
            try {
                boolean zza;
                int tag = com_google_android_gms_internal_measurement_zzxi.getTag();
                Object zza2;
                if (tag != 11) {
                    if ((tag & 7) == 2) {
                        zza2 = com_google_android_gms_internal_measurement_zzva.zza(com_google_android_gms_internal_measurement_zzuz, this.zzcbd, tag >>> 3);
                        if (zza2 != null) {
                            com_google_android_gms_internal_measurement_zzva.zza(com_google_android_gms_internal_measurement_zzxi, zza2, com_google_android_gms_internal_measurement_zzuz, zzt);
                        } else {
                            zza = com_google_android_gms_internal_measurement_zzyb.zza(zzai, com_google_android_gms_internal_measurement_zzxi);
                            continue;
                        }
                    } else {
                        zza = com_google_android_gms_internal_measurement_zzxi.zzvf();
                        continue;
                    }
                    if (!zza) {
                        return;
                    }
                }
                int i = 0;
                zza2 = null;
                zzud com_google_android_gms_internal_measurement_zzud = null;
                while (com_google_android_gms_internal_measurement_zzxi.zzve() != ConnectionsManager.DEFAULT_DATACENTER_ID) {
                    int tag2 = com_google_android_gms_internal_measurement_zzxi.getTag();
                    if (tag2 == 16) {
                        i = com_google_android_gms_internal_measurement_zzxi.zzup();
                        zza2 = com_google_android_gms_internal_measurement_zzva.zza(com_google_android_gms_internal_measurement_zzuz, this.zzcbd, i);
                    } else if (tag2 == 26) {
                        if (zza2 != null) {
                            com_google_android_gms_internal_measurement_zzva.zza(com_google_android_gms_internal_measurement_zzxi, zza2, com_google_android_gms_internal_measurement_zzuz, zzt);
                        } else {
                            com_google_android_gms_internal_measurement_zzud = com_google_android_gms_internal_measurement_zzxi.zzuo();
                        }
                    } else if (!com_google_android_gms_internal_measurement_zzxi.zzvf()) {
                        break;
                    }
                }
                if (com_google_android_gms_internal_measurement_zzxi.getTag() != 12) {
                    throw zzvt.zzwn();
                } else if (com_google_android_gms_internal_measurement_zzud != null) {
                    if (zza2 != null) {
                        com_google_android_gms_internal_measurement_zzva.zza(com_google_android_gms_internal_measurement_zzud, zza2, com_google_android_gms_internal_measurement_zzuz, zzt);
                    } else {
                        com_google_android_gms_internal_measurement_zzyb.zza(zzai, i, com_google_android_gms_internal_measurement_zzud);
                    }
                }
                zza = true;
                continue;
                if (zza) {
                    return;
                }
            } finally {
                com_google_android_gms_internal_measurement_zzyb.zzg(t, zzai);
            }
        }
        com_google_android_gms_internal_measurement_zzyb.zzg(t, zzai);
    }

    public final void zzu(T t) {
        this.zzcbn.zzu(t);
        this.zzcbo.zzu(t);
    }

    public final boolean zzaf(T t) {
        return this.zzcbo.zzs(t).isInitialized();
    }

    public final int zzae(T t) {
        zzyb com_google_android_gms_internal_measurement_zzyb = this.zzcbn;
        int zzaj = com_google_android_gms_internal_measurement_zzyb.zzaj(com_google_android_gms_internal_measurement_zzyb.zzah(t)) + 0;
        if (this.zzcbe) {
            return zzaj + this.zzcbo.zzs(t).zzvv();
        }
        return zzaj;
    }
}
