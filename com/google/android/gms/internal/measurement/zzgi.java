package com.google.android.gms.internal.measurement;

import com.google.android.exoplayer2.extractor.ts.TsExtractor;
import com.google.android.gms.internal.measurement.zzfq.zzb;
import com.googlecode.mp4parser.authoring.tracks.h265.NalUnitTypes;
import java.io.IOException;
import org.telegram.messenger.voip.VoIPService;

public final class zzgi extends zzza<zzgi> {
    private static volatile zzgi[] zzawz;
    public String zzafw;
    public String zzafx;
    public String zzafz;
    public String zzage;
    public String zzagv;
    public String zzaia;
    public String zzawj;
    public Integer zzaxa;
    public zzgf[] zzaxb;
    public zzgl[] zzaxc;
    public Long zzaxd;
    public Long zzaxe;
    public Long zzaxf;
    public Long zzaxg;
    public Long zzaxh;
    public String zzaxi;
    public String zzaxj;
    public String zzaxk;
    public Integer zzaxl;
    public Long zzaxm;
    public Long zzaxn;
    public String zzaxo;
    public Boolean zzaxp;
    public Long zzaxq;
    public Integer zzaxr;
    public Boolean zzaxs;
    public zzgd[] zzaxt;
    public Integer zzaxu;
    private Integer zzaxv;
    private Integer zzaxw;
    public String zzaxx;
    public Long zzaxy;
    public Long zzaxz;
    public String zzaya;
    private String zzayb;
    public Integer zzayc;
    private zzb zzayd;
    public String zzts;
    public String zztt;

    public static zzgi[] zzms() {
        if (zzawz == null) {
            synchronized (zzze.zzcfl) {
                if (zzawz == null) {
                    zzawz = new zzgi[0];
                }
            }
        }
        return zzawz;
    }

    public zzgi() {
        this.zzaxa = null;
        this.zzaxb = zzgf.zzmq();
        this.zzaxc = zzgl.zzmu();
        this.zzaxd = null;
        this.zzaxe = null;
        this.zzaxf = null;
        this.zzaxg = null;
        this.zzaxh = null;
        this.zzaxi = null;
        this.zzaxj = null;
        this.zzaxk = null;
        this.zzaia = null;
        this.zzaxl = null;
        this.zzage = null;
        this.zztt = null;
        this.zzts = null;
        this.zzaxm = null;
        this.zzaxn = null;
        this.zzaxo = null;
        this.zzaxp = null;
        this.zzafw = null;
        this.zzaxq = null;
        this.zzaxr = null;
        this.zzagv = null;
        this.zzafx = null;
        this.zzaxs = null;
        this.zzaxt = zzgd.zzmo();
        this.zzafz = null;
        this.zzaxu = null;
        this.zzaxv = null;
        this.zzaxw = null;
        this.zzaxx = null;
        this.zzaxy = null;
        this.zzaxz = null;
        this.zzaya = null;
        this.zzayb = null;
        this.zzayc = null;
        this.zzawj = null;
        this.zzayd = null;
        this.zzcfc = null;
        this.zzcfm = -1;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzgi)) {
            return false;
        }
        zzgi com_google_android_gms_internal_measurement_zzgi = (zzgi) obj;
        if (this.zzaxa == null) {
            if (com_google_android_gms_internal_measurement_zzgi.zzaxa != null) {
                return false;
            }
        } else if (!this.zzaxa.equals(com_google_android_gms_internal_measurement_zzgi.zzaxa)) {
            return false;
        }
        if (!zzze.equals(this.zzaxb, com_google_android_gms_internal_measurement_zzgi.zzaxb)) {
            return false;
        }
        if (!zzze.equals(this.zzaxc, com_google_android_gms_internal_measurement_zzgi.zzaxc)) {
            return false;
        }
        if (this.zzaxd == null) {
            if (com_google_android_gms_internal_measurement_zzgi.zzaxd != null) {
                return false;
            }
        } else if (!this.zzaxd.equals(com_google_android_gms_internal_measurement_zzgi.zzaxd)) {
            return false;
        }
        if (this.zzaxe == null) {
            if (com_google_android_gms_internal_measurement_zzgi.zzaxe != null) {
                return false;
            }
        } else if (!this.zzaxe.equals(com_google_android_gms_internal_measurement_zzgi.zzaxe)) {
            return false;
        }
        if (this.zzaxf == null) {
            if (com_google_android_gms_internal_measurement_zzgi.zzaxf != null) {
                return false;
            }
        } else if (!this.zzaxf.equals(com_google_android_gms_internal_measurement_zzgi.zzaxf)) {
            return false;
        }
        if (this.zzaxg == null) {
            if (com_google_android_gms_internal_measurement_zzgi.zzaxg != null) {
                return false;
            }
        } else if (!this.zzaxg.equals(com_google_android_gms_internal_measurement_zzgi.zzaxg)) {
            return false;
        }
        if (this.zzaxh == null) {
            if (com_google_android_gms_internal_measurement_zzgi.zzaxh != null) {
                return false;
            }
        } else if (!this.zzaxh.equals(com_google_android_gms_internal_measurement_zzgi.zzaxh)) {
            return false;
        }
        if (this.zzaxi == null) {
            if (com_google_android_gms_internal_measurement_zzgi.zzaxi != null) {
                return false;
            }
        } else if (!this.zzaxi.equals(com_google_android_gms_internal_measurement_zzgi.zzaxi)) {
            return false;
        }
        if (this.zzaxj == null) {
            if (com_google_android_gms_internal_measurement_zzgi.zzaxj != null) {
                return false;
            }
        } else if (!this.zzaxj.equals(com_google_android_gms_internal_measurement_zzgi.zzaxj)) {
            return false;
        }
        if (this.zzaxk == null) {
            if (com_google_android_gms_internal_measurement_zzgi.zzaxk != null) {
                return false;
            }
        } else if (!this.zzaxk.equals(com_google_android_gms_internal_measurement_zzgi.zzaxk)) {
            return false;
        }
        if (this.zzaia == null) {
            if (com_google_android_gms_internal_measurement_zzgi.zzaia != null) {
                return false;
            }
        } else if (!this.zzaia.equals(com_google_android_gms_internal_measurement_zzgi.zzaia)) {
            return false;
        }
        if (this.zzaxl == null) {
            if (com_google_android_gms_internal_measurement_zzgi.zzaxl != null) {
                return false;
            }
        } else if (!this.zzaxl.equals(com_google_android_gms_internal_measurement_zzgi.zzaxl)) {
            return false;
        }
        if (this.zzage == null) {
            if (com_google_android_gms_internal_measurement_zzgi.zzage != null) {
                return false;
            }
        } else if (!this.zzage.equals(com_google_android_gms_internal_measurement_zzgi.zzage)) {
            return false;
        }
        if (this.zztt == null) {
            if (com_google_android_gms_internal_measurement_zzgi.zztt != null) {
                return false;
            }
        } else if (!this.zztt.equals(com_google_android_gms_internal_measurement_zzgi.zztt)) {
            return false;
        }
        if (this.zzts == null) {
            if (com_google_android_gms_internal_measurement_zzgi.zzts != null) {
                return false;
            }
        } else if (!this.zzts.equals(com_google_android_gms_internal_measurement_zzgi.zzts)) {
            return false;
        }
        if (this.zzaxm == null) {
            if (com_google_android_gms_internal_measurement_zzgi.zzaxm != null) {
                return false;
            }
        } else if (!this.zzaxm.equals(com_google_android_gms_internal_measurement_zzgi.zzaxm)) {
            return false;
        }
        if (this.zzaxn == null) {
            if (com_google_android_gms_internal_measurement_zzgi.zzaxn != null) {
                return false;
            }
        } else if (!this.zzaxn.equals(com_google_android_gms_internal_measurement_zzgi.zzaxn)) {
            return false;
        }
        if (this.zzaxo == null) {
            if (com_google_android_gms_internal_measurement_zzgi.zzaxo != null) {
                return false;
            }
        } else if (!this.zzaxo.equals(com_google_android_gms_internal_measurement_zzgi.zzaxo)) {
            return false;
        }
        if (this.zzaxp == null) {
            if (com_google_android_gms_internal_measurement_zzgi.zzaxp != null) {
                return false;
            }
        } else if (!this.zzaxp.equals(com_google_android_gms_internal_measurement_zzgi.zzaxp)) {
            return false;
        }
        if (this.zzafw == null) {
            if (com_google_android_gms_internal_measurement_zzgi.zzafw != null) {
                return false;
            }
        } else if (!this.zzafw.equals(com_google_android_gms_internal_measurement_zzgi.zzafw)) {
            return false;
        }
        if (this.zzaxq == null) {
            if (com_google_android_gms_internal_measurement_zzgi.zzaxq != null) {
                return false;
            }
        } else if (!this.zzaxq.equals(com_google_android_gms_internal_measurement_zzgi.zzaxq)) {
            return false;
        }
        if (this.zzaxr == null) {
            if (com_google_android_gms_internal_measurement_zzgi.zzaxr != null) {
                return false;
            }
        } else if (!this.zzaxr.equals(com_google_android_gms_internal_measurement_zzgi.zzaxr)) {
            return false;
        }
        if (this.zzagv == null) {
            if (com_google_android_gms_internal_measurement_zzgi.zzagv != null) {
                return false;
            }
        } else if (!this.zzagv.equals(com_google_android_gms_internal_measurement_zzgi.zzagv)) {
            return false;
        }
        if (this.zzafx == null) {
            if (com_google_android_gms_internal_measurement_zzgi.zzafx != null) {
                return false;
            }
        } else if (!this.zzafx.equals(com_google_android_gms_internal_measurement_zzgi.zzafx)) {
            return false;
        }
        if (this.zzaxs == null) {
            if (com_google_android_gms_internal_measurement_zzgi.zzaxs != null) {
                return false;
            }
        } else if (!this.zzaxs.equals(com_google_android_gms_internal_measurement_zzgi.zzaxs)) {
            return false;
        }
        if (!zzze.equals(this.zzaxt, com_google_android_gms_internal_measurement_zzgi.zzaxt)) {
            return false;
        }
        if (this.zzafz == null) {
            if (com_google_android_gms_internal_measurement_zzgi.zzafz != null) {
                return false;
            }
        } else if (!this.zzafz.equals(com_google_android_gms_internal_measurement_zzgi.zzafz)) {
            return false;
        }
        if (this.zzaxu == null) {
            if (com_google_android_gms_internal_measurement_zzgi.zzaxu != null) {
                return false;
            }
        } else if (!this.zzaxu.equals(com_google_android_gms_internal_measurement_zzgi.zzaxu)) {
            return false;
        }
        if (this.zzaxv == null) {
            if (com_google_android_gms_internal_measurement_zzgi.zzaxv != null) {
                return false;
            }
        } else if (!this.zzaxv.equals(com_google_android_gms_internal_measurement_zzgi.zzaxv)) {
            return false;
        }
        if (this.zzaxw == null) {
            if (com_google_android_gms_internal_measurement_zzgi.zzaxw != null) {
                return false;
            }
        } else if (!this.zzaxw.equals(com_google_android_gms_internal_measurement_zzgi.zzaxw)) {
            return false;
        }
        if (this.zzaxx == null) {
            if (com_google_android_gms_internal_measurement_zzgi.zzaxx != null) {
                return false;
            }
        } else if (!this.zzaxx.equals(com_google_android_gms_internal_measurement_zzgi.zzaxx)) {
            return false;
        }
        if (this.zzaxy == null) {
            if (com_google_android_gms_internal_measurement_zzgi.zzaxy != null) {
                return false;
            }
        } else if (!this.zzaxy.equals(com_google_android_gms_internal_measurement_zzgi.zzaxy)) {
            return false;
        }
        if (this.zzaxz == null) {
            if (com_google_android_gms_internal_measurement_zzgi.zzaxz != null) {
                return false;
            }
        } else if (!this.zzaxz.equals(com_google_android_gms_internal_measurement_zzgi.zzaxz)) {
            return false;
        }
        if (this.zzaya == null) {
            if (com_google_android_gms_internal_measurement_zzgi.zzaya != null) {
                return false;
            }
        } else if (!this.zzaya.equals(com_google_android_gms_internal_measurement_zzgi.zzaya)) {
            return false;
        }
        if (this.zzayb == null) {
            if (com_google_android_gms_internal_measurement_zzgi.zzayb != null) {
                return false;
            }
        } else if (!this.zzayb.equals(com_google_android_gms_internal_measurement_zzgi.zzayb)) {
            return false;
        }
        if (this.zzayc == null) {
            if (com_google_android_gms_internal_measurement_zzgi.zzayc != null) {
                return false;
            }
        } else if (!this.zzayc.equals(com_google_android_gms_internal_measurement_zzgi.zzayc)) {
            return false;
        }
        if (this.zzawj == null) {
            if (com_google_android_gms_internal_measurement_zzgi.zzawj != null) {
                return false;
            }
        } else if (!this.zzawj.equals(com_google_android_gms_internal_measurement_zzgi.zzawj)) {
            return false;
        }
        if (this.zzayd == null) {
            if (com_google_android_gms_internal_measurement_zzgi.zzayd != null) {
                return false;
            }
        } else if (!this.zzayd.equals(com_google_android_gms_internal_measurement_zzgi.zzayd)) {
            return false;
        }
        if (this.zzcfc != null && !this.zzcfc.isEmpty()) {
            return this.zzcfc.equals(com_google_android_gms_internal_measurement_zzgi.zzcfc);
        }
        if (com_google_android_gms_internal_measurement_zzgi.zzcfc == null || com_google_android_gms_internal_measurement_zzgi.zzcfc.isEmpty()) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = (this.zzawj == null ? 0 : this.zzawj.hashCode()) + (((this.zzayc == null ? 0 : this.zzayc.hashCode()) + (((this.zzayb == null ? 0 : this.zzayb.hashCode()) + (((this.zzaya == null ? 0 : this.zzaya.hashCode()) + (((this.zzaxz == null ? 0 : this.zzaxz.hashCode()) + (((this.zzaxy == null ? 0 : this.zzaxy.hashCode()) + (((this.zzaxx == null ? 0 : this.zzaxx.hashCode()) + (((this.zzaxw == null ? 0 : this.zzaxw.hashCode()) + (((this.zzaxv == null ? 0 : this.zzaxv.hashCode()) + (((this.zzaxu == null ? 0 : this.zzaxu.hashCode()) + (((this.zzafz == null ? 0 : this.zzafz.hashCode()) + (((((this.zzaxs == null ? 0 : this.zzaxs.hashCode()) + (((this.zzafx == null ? 0 : this.zzafx.hashCode()) + (((this.zzagv == null ? 0 : this.zzagv.hashCode()) + (((this.zzaxr == null ? 0 : this.zzaxr.hashCode()) + (((this.zzaxq == null ? 0 : this.zzaxq.hashCode()) + (((this.zzafw == null ? 0 : this.zzafw.hashCode()) + (((this.zzaxp == null ? 0 : this.zzaxp.hashCode()) + (((this.zzaxo == null ? 0 : this.zzaxo.hashCode()) + (((this.zzaxn == null ? 0 : this.zzaxn.hashCode()) + (((this.zzaxm == null ? 0 : this.zzaxm.hashCode()) + (((this.zzts == null ? 0 : this.zzts.hashCode()) + (((this.zztt == null ? 0 : this.zztt.hashCode()) + (((this.zzage == null ? 0 : this.zzage.hashCode()) + (((this.zzaxl == null ? 0 : this.zzaxl.hashCode()) + (((this.zzaia == null ? 0 : this.zzaia.hashCode()) + (((this.zzaxk == null ? 0 : this.zzaxk.hashCode()) + (((this.zzaxj == null ? 0 : this.zzaxj.hashCode()) + (((this.zzaxi == null ? 0 : this.zzaxi.hashCode()) + (((this.zzaxh == null ? 0 : this.zzaxh.hashCode()) + (((this.zzaxg == null ? 0 : this.zzaxg.hashCode()) + (((this.zzaxf == null ? 0 : this.zzaxf.hashCode()) + (((this.zzaxe == null ? 0 : this.zzaxe.hashCode()) + (((this.zzaxd == null ? 0 : this.zzaxd.hashCode()) + (((((((this.zzaxa == null ? 0 : this.zzaxa.hashCode()) + ((getClass().getName().hashCode() + 527) * 31)) * 31) + zzze.hashCode(this.zzaxb)) * 31) + zzze.hashCode(this.zzaxc)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31) + zzze.hashCode(this.zzaxt)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31);
        zzvm com_google_android_gms_internal_measurement_zzvm = this.zzayd;
        hashCode = ((com_google_android_gms_internal_measurement_zzvm == null ? 0 : com_google_android_gms_internal_measurement_zzvm.hashCode()) + (hashCode * 31)) * 31;
        if (!(this.zzcfc == null || this.zzcfc.isEmpty())) {
            i = this.zzcfc.hashCode();
        }
        return hashCode + i;
    }

    public final void zza(zzyy com_google_android_gms_internal_measurement_zzyy) throws IOException {
        int i = 0;
        if (this.zzaxa != null) {
            com_google_android_gms_internal_measurement_zzyy.zzd(1, this.zzaxa.intValue());
        }
        if (this.zzaxb != null && this.zzaxb.length > 0) {
            for (zzzg com_google_android_gms_internal_measurement_zzzg : this.zzaxb) {
                if (com_google_android_gms_internal_measurement_zzzg != null) {
                    com_google_android_gms_internal_measurement_zzyy.zza(2, com_google_android_gms_internal_measurement_zzzg);
                }
            }
        }
        if (this.zzaxc != null && this.zzaxc.length > 0) {
            for (zzzg com_google_android_gms_internal_measurement_zzzg2 : this.zzaxc) {
                if (com_google_android_gms_internal_measurement_zzzg2 != null) {
                    com_google_android_gms_internal_measurement_zzyy.zza(3, com_google_android_gms_internal_measurement_zzzg2);
                }
            }
        }
        if (this.zzaxd != null) {
            com_google_android_gms_internal_measurement_zzyy.zzi(4, this.zzaxd.longValue());
        }
        if (this.zzaxe != null) {
            com_google_android_gms_internal_measurement_zzyy.zzi(5, this.zzaxe.longValue());
        }
        if (this.zzaxf != null) {
            com_google_android_gms_internal_measurement_zzyy.zzi(6, this.zzaxf.longValue());
        }
        if (this.zzaxh != null) {
            com_google_android_gms_internal_measurement_zzyy.zzi(7, this.zzaxh.longValue());
        }
        if (this.zzaxi != null) {
            com_google_android_gms_internal_measurement_zzyy.zzb(8, this.zzaxi);
        }
        if (this.zzaxj != null) {
            com_google_android_gms_internal_measurement_zzyy.zzb(9, this.zzaxj);
        }
        if (this.zzaxk != null) {
            com_google_android_gms_internal_measurement_zzyy.zzb(10, this.zzaxk);
        }
        if (this.zzaia != null) {
            com_google_android_gms_internal_measurement_zzyy.zzb(11, this.zzaia);
        }
        if (this.zzaxl != null) {
            com_google_android_gms_internal_measurement_zzyy.zzd(12, this.zzaxl.intValue());
        }
        if (this.zzage != null) {
            com_google_android_gms_internal_measurement_zzyy.zzb(13, this.zzage);
        }
        if (this.zztt != null) {
            com_google_android_gms_internal_measurement_zzyy.zzb(14, this.zztt);
        }
        if (this.zzts != null) {
            com_google_android_gms_internal_measurement_zzyy.zzb(16, this.zzts);
        }
        if (this.zzaxm != null) {
            com_google_android_gms_internal_measurement_zzyy.zzi(17, this.zzaxm.longValue());
        }
        if (this.zzaxn != null) {
            com_google_android_gms_internal_measurement_zzyy.zzi(18, this.zzaxn.longValue());
        }
        if (this.zzaxo != null) {
            com_google_android_gms_internal_measurement_zzyy.zzb(19, this.zzaxo);
        }
        if (this.zzaxp != null) {
            com_google_android_gms_internal_measurement_zzyy.zzb(20, this.zzaxp.booleanValue());
        }
        if (this.zzafw != null) {
            com_google_android_gms_internal_measurement_zzyy.zzb(21, this.zzafw);
        }
        if (this.zzaxq != null) {
            com_google_android_gms_internal_measurement_zzyy.zzi(22, this.zzaxq.longValue());
        }
        if (this.zzaxr != null) {
            com_google_android_gms_internal_measurement_zzyy.zzd(23, this.zzaxr.intValue());
        }
        if (this.zzagv != null) {
            com_google_android_gms_internal_measurement_zzyy.zzb(24, this.zzagv);
        }
        if (this.zzafx != null) {
            com_google_android_gms_internal_measurement_zzyy.zzb(25, this.zzafx);
        }
        if (this.zzaxg != null) {
            com_google_android_gms_internal_measurement_zzyy.zzi(26, this.zzaxg.longValue());
        }
        if (this.zzaxs != null) {
            com_google_android_gms_internal_measurement_zzyy.zzb(28, this.zzaxs.booleanValue());
        }
        if (this.zzaxt != null && this.zzaxt.length > 0) {
            while (i < this.zzaxt.length) {
                zzzg com_google_android_gms_internal_measurement_zzzg3 = this.zzaxt[i];
                if (com_google_android_gms_internal_measurement_zzzg3 != null) {
                    com_google_android_gms_internal_measurement_zzyy.zza(29, com_google_android_gms_internal_measurement_zzzg3);
                }
                i++;
            }
        }
        if (this.zzafz != null) {
            com_google_android_gms_internal_measurement_zzyy.zzb(30, this.zzafz);
        }
        if (this.zzaxu != null) {
            com_google_android_gms_internal_measurement_zzyy.zzd(31, this.zzaxu.intValue());
        }
        if (this.zzaxv != null) {
            com_google_android_gms_internal_measurement_zzyy.zzd(32, this.zzaxv.intValue());
        }
        if (this.zzaxw != null) {
            com_google_android_gms_internal_measurement_zzyy.zzd(33, this.zzaxw.intValue());
        }
        if (this.zzaxx != null) {
            com_google_android_gms_internal_measurement_zzyy.zzb(34, this.zzaxx);
        }
        if (this.zzaxy != null) {
            com_google_android_gms_internal_measurement_zzyy.zzi(35, this.zzaxy.longValue());
        }
        if (this.zzaxz != null) {
            com_google_android_gms_internal_measurement_zzyy.zzi(36, this.zzaxz.longValue());
        }
        if (this.zzaya != null) {
            com_google_android_gms_internal_measurement_zzyy.zzb(37, this.zzaya);
        }
        if (this.zzayb != null) {
            com_google_android_gms_internal_measurement_zzyy.zzb(38, this.zzayb);
        }
        if (this.zzayc != null) {
            com_google_android_gms_internal_measurement_zzyy.zzd(39, this.zzayc.intValue());
        }
        if (this.zzawj != null) {
            com_google_android_gms_internal_measurement_zzyy.zzb(41, this.zzawj);
        }
        if (this.zzayd != null) {
            com_google_android_gms_internal_measurement_zzyy.zze(44, this.zzayd);
        }
        super.zza(com_google_android_gms_internal_measurement_zzyy);
    }

    protected final int zzf() {
        int i;
        int i2 = 0;
        int zzf = super.zzf();
        if (this.zzaxa != null) {
            zzf += zzyy.zzh(1, this.zzaxa.intValue());
        }
        if (this.zzaxb != null && this.zzaxb.length > 0) {
            i = zzf;
            for (zzzg com_google_android_gms_internal_measurement_zzzg : this.zzaxb) {
                if (com_google_android_gms_internal_measurement_zzzg != null) {
                    i += zzyy.zzb(2, com_google_android_gms_internal_measurement_zzzg);
                }
            }
            zzf = i;
        }
        if (this.zzaxc != null && this.zzaxc.length > 0) {
            i = zzf;
            for (zzzg com_google_android_gms_internal_measurement_zzzg2 : this.zzaxc) {
                if (com_google_android_gms_internal_measurement_zzzg2 != null) {
                    i += zzyy.zzb(3, com_google_android_gms_internal_measurement_zzzg2);
                }
            }
            zzf = i;
        }
        if (this.zzaxd != null) {
            zzf += zzyy.zzd(4, this.zzaxd.longValue());
        }
        if (this.zzaxe != null) {
            zzf += zzyy.zzd(5, this.zzaxe.longValue());
        }
        if (this.zzaxf != null) {
            zzf += zzyy.zzd(6, this.zzaxf.longValue());
        }
        if (this.zzaxh != null) {
            zzf += zzyy.zzd(7, this.zzaxh.longValue());
        }
        if (this.zzaxi != null) {
            zzf += zzyy.zzc(8, this.zzaxi);
        }
        if (this.zzaxj != null) {
            zzf += zzyy.zzc(9, this.zzaxj);
        }
        if (this.zzaxk != null) {
            zzf += zzyy.zzc(10, this.zzaxk);
        }
        if (this.zzaia != null) {
            zzf += zzyy.zzc(11, this.zzaia);
        }
        if (this.zzaxl != null) {
            zzf += zzyy.zzh(12, this.zzaxl.intValue());
        }
        if (this.zzage != null) {
            zzf += zzyy.zzc(13, this.zzage);
        }
        if (this.zztt != null) {
            zzf += zzyy.zzc(14, this.zztt);
        }
        if (this.zzts != null) {
            zzf += zzyy.zzc(16, this.zzts);
        }
        if (this.zzaxm != null) {
            zzf += zzyy.zzd(17, this.zzaxm.longValue());
        }
        if (this.zzaxn != null) {
            zzf += zzyy.zzd(18, this.zzaxn.longValue());
        }
        if (this.zzaxo != null) {
            zzf += zzyy.zzc(19, this.zzaxo);
        }
        if (this.zzaxp != null) {
            this.zzaxp.booleanValue();
            zzf += zzyy.zzbb(20) + 1;
        }
        if (this.zzafw != null) {
            zzf += zzyy.zzc(21, this.zzafw);
        }
        if (this.zzaxq != null) {
            zzf += zzyy.zzd(22, this.zzaxq.longValue());
        }
        if (this.zzaxr != null) {
            zzf += zzyy.zzh(23, this.zzaxr.intValue());
        }
        if (this.zzagv != null) {
            zzf += zzyy.zzc(24, this.zzagv);
        }
        if (this.zzafx != null) {
            zzf += zzyy.zzc(25, this.zzafx);
        }
        if (this.zzaxg != null) {
            zzf += zzyy.zzd(26, this.zzaxg.longValue());
        }
        if (this.zzaxs != null) {
            this.zzaxs.booleanValue();
            zzf += zzyy.zzbb(28) + 1;
        }
        if (this.zzaxt != null && this.zzaxt.length > 0) {
            while (i2 < this.zzaxt.length) {
                zzzg com_google_android_gms_internal_measurement_zzzg3 = this.zzaxt[i2];
                if (com_google_android_gms_internal_measurement_zzzg3 != null) {
                    zzf += zzyy.zzb(29, com_google_android_gms_internal_measurement_zzzg3);
                }
                i2++;
            }
        }
        if (this.zzafz != null) {
            zzf += zzyy.zzc(30, this.zzafz);
        }
        if (this.zzaxu != null) {
            zzf += zzyy.zzh(31, this.zzaxu.intValue());
        }
        if (this.zzaxv != null) {
            zzf += zzyy.zzh(32, this.zzaxv.intValue());
        }
        if (this.zzaxw != null) {
            zzf += zzyy.zzh(33, this.zzaxw.intValue());
        }
        if (this.zzaxx != null) {
            zzf += zzyy.zzc(34, this.zzaxx);
        }
        if (this.zzaxy != null) {
            zzf += zzyy.zzd(35, this.zzaxy.longValue());
        }
        if (this.zzaxz != null) {
            zzf += zzyy.zzd(36, this.zzaxz.longValue());
        }
        if (this.zzaya != null) {
            zzf += zzyy.zzc(37, this.zzaya);
        }
        if (this.zzayb != null) {
            zzf += zzyy.zzc(38, this.zzayb);
        }
        if (this.zzayc != null) {
            zzf += zzyy.zzh(39, this.zzayc.intValue());
        }
        if (this.zzawj != null) {
            zzf += zzyy.zzc(41, this.zzawj);
        }
        if (this.zzayd != null) {
            return zzf + zzut.zzc(44, this.zzayd);
        }
        return zzf;
    }

    public final /* synthetic */ zzzg zza(zzyx com_google_android_gms_internal_measurement_zzyx) throws IOException {
        while (true) {
            int zzug = com_google_android_gms_internal_measurement_zzyx.zzug();
            int zzb;
            Object obj;
            switch (zzug) {
                case 0:
                    break;
                case 8:
                    this.zzaxa = Integer.valueOf(com_google_android_gms_internal_measurement_zzyx.zzuy());
                    continue;
                case 18:
                    zzb = zzzj.zzb(com_google_android_gms_internal_measurement_zzyx, 18);
                    zzug = this.zzaxb == null ? 0 : this.zzaxb.length;
                    obj = new zzgf[(zzb + zzug)];
                    if (zzug != 0) {
                        System.arraycopy(this.zzaxb, 0, obj, 0, zzug);
                    }
                    while (zzug < obj.length - 1) {
                        obj[zzug] = new zzgf();
                        com_google_android_gms_internal_measurement_zzyx.zza(obj[zzug]);
                        com_google_android_gms_internal_measurement_zzyx.zzug();
                        zzug++;
                    }
                    obj[zzug] = new zzgf();
                    com_google_android_gms_internal_measurement_zzyx.zza(obj[zzug]);
                    this.zzaxb = obj;
                    continue;
                case NalUnitTypes.NAL_TYPE_RSV_VCL26 /*26*/:
                    zzb = zzzj.zzb(com_google_android_gms_internal_measurement_zzyx, 26);
                    zzug = this.zzaxc == null ? 0 : this.zzaxc.length;
                    obj = new zzgl[(zzb + zzug)];
                    if (zzug != 0) {
                        System.arraycopy(this.zzaxc, 0, obj, 0, zzug);
                    }
                    while (zzug < obj.length - 1) {
                        obj[zzug] = new zzgl();
                        com_google_android_gms_internal_measurement_zzyx.zza(obj[zzug]);
                        com_google_android_gms_internal_measurement_zzyx.zzug();
                        zzug++;
                    }
                    obj[zzug] = new zzgl();
                    com_google_android_gms_internal_measurement_zzyx.zza(obj[zzug]);
                    this.zzaxc = obj;
                    continue;
                case 32:
                    this.zzaxd = Long.valueOf(com_google_android_gms_internal_measurement_zzyx.zzuz());
                    continue;
                case 40:
                    this.zzaxe = Long.valueOf(com_google_android_gms_internal_measurement_zzyx.zzuz());
                    continue;
                case 48:
                    this.zzaxf = Long.valueOf(com_google_android_gms_internal_measurement_zzyx.zzuz());
                    continue;
                case 56:
                    this.zzaxh = Long.valueOf(com_google_android_gms_internal_measurement_zzyx.zzuz());
                    continue;
                case 66:
                    this.zzaxi = com_google_android_gms_internal_measurement_zzyx.readString();
                    continue;
                case VoIPService.CALL_MAX_LAYER /*74*/:
                    this.zzaxj = com_google_android_gms_internal_measurement_zzyx.readString();
                    continue;
                case 82:
                    this.zzaxk = com_google_android_gms_internal_measurement_zzyx.readString();
                    continue;
                case 90:
                    this.zzaia = com_google_android_gms_internal_measurement_zzyx.readString();
                    continue;
                case 96:
                    this.zzaxl = Integer.valueOf(com_google_android_gms_internal_measurement_zzyx.zzuy());
                    continue;
                case 106:
                    this.zzage = com_google_android_gms_internal_measurement_zzyx.readString();
                    continue;
                case 114:
                    this.zztt = com_google_android_gms_internal_measurement_zzyx.readString();
                    continue;
                case TsExtractor.TS_STREAM_TYPE_HDMV_DTS /*130*/:
                    this.zzts = com_google_android_gms_internal_measurement_zzyx.readString();
                    continue;
                case 136:
                    this.zzaxm = Long.valueOf(com_google_android_gms_internal_measurement_zzyx.zzuz());
                    continue;
                case 144:
                    this.zzaxn = Long.valueOf(com_google_android_gms_internal_measurement_zzyx.zzuz());
                    continue;
                case 154:
                    this.zzaxo = com_google_android_gms_internal_measurement_zzyx.readString();
                    continue;
                case 160:
                    this.zzaxp = Boolean.valueOf(com_google_android_gms_internal_measurement_zzyx.zzum());
                    continue;
                case 170:
                    this.zzafw = com_google_android_gms_internal_measurement_zzyx.readString();
                    continue;
                case 176:
                    this.zzaxq = Long.valueOf(com_google_android_gms_internal_measurement_zzyx.zzuz());
                    continue;
                case 184:
                    this.zzaxr = Integer.valueOf(com_google_android_gms_internal_measurement_zzyx.zzuy());
                    continue;
                case 194:
                    this.zzagv = com_google_android_gms_internal_measurement_zzyx.readString();
                    continue;
                case 202:
                    this.zzafx = com_google_android_gms_internal_measurement_zzyx.readString();
                    continue;
                case 208:
                    this.zzaxg = Long.valueOf(com_google_android_gms_internal_measurement_zzyx.zzuz());
                    continue;
                case 224:
                    this.zzaxs = Boolean.valueOf(com_google_android_gms_internal_measurement_zzyx.zzum());
                    continue;
                case 234:
                    zzb = zzzj.zzb(com_google_android_gms_internal_measurement_zzyx, 234);
                    zzug = this.zzaxt == null ? 0 : this.zzaxt.length;
                    obj = new zzgd[(zzb + zzug)];
                    if (zzug != 0) {
                        System.arraycopy(this.zzaxt, 0, obj, 0, zzug);
                    }
                    while (zzug < obj.length - 1) {
                        obj[zzug] = new zzgd();
                        com_google_android_gms_internal_measurement_zzyx.zza(obj[zzug]);
                        com_google_android_gms_internal_measurement_zzyx.zzug();
                        zzug++;
                    }
                    obj[zzug] = new zzgd();
                    com_google_android_gms_internal_measurement_zzyx.zza(obj[zzug]);
                    this.zzaxt = obj;
                    continue;
                case 242:
                    this.zzafz = com_google_android_gms_internal_measurement_zzyx.readString();
                    continue;
                case 248:
                    this.zzaxu = Integer.valueOf(com_google_android_gms_internal_measurement_zzyx.zzuy());
                    continue;
                case 256:
                    this.zzaxv = Integer.valueOf(com_google_android_gms_internal_measurement_zzyx.zzuy());
                    continue;
                case 264:
                    this.zzaxw = Integer.valueOf(com_google_android_gms_internal_measurement_zzyx.zzuy());
                    continue;
                case 274:
                    this.zzaxx = com_google_android_gms_internal_measurement_zzyx.readString();
                    continue;
                case 280:
                    this.zzaxy = Long.valueOf(com_google_android_gms_internal_measurement_zzyx.zzuz());
                    continue;
                case 288:
                    this.zzaxz = Long.valueOf(com_google_android_gms_internal_measurement_zzyx.zzuz());
                    continue;
                case 298:
                    this.zzaya = com_google_android_gms_internal_measurement_zzyx.readString();
                    continue;
                case 306:
                    this.zzayb = com_google_android_gms_internal_measurement_zzyx.readString();
                    continue;
                case 312:
                    this.zzayc = Integer.valueOf(com_google_android_gms_internal_measurement_zzyx.zzuy());
                    continue;
                case 330:
                    this.zzawj = com_google_android_gms_internal_measurement_zzyx.readString();
                    continue;
                case 354:
                    this.zzayd = (zzb) com_google_android_gms_internal_measurement_zzyx.zza(zzb.zza());
                    continue;
                default:
                    if (!super.zza(com_google_android_gms_internal_measurement_zzyx, zzug)) {
                        break;
                    }
                    continue;
            }
            return this;
        }
    }
}
