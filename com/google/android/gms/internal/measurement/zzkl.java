package com.google.android.gms.internal.measurement;

import java.io.IOException;
import org.telegram.messenger.exoplayer2.extractor.ts.TsExtractor;
import org.telegram.messenger.voip.VoIPService;

public final class zzkl extends zzabd<zzkl> {
    private static volatile zzkl[] zzatb;
    public String zzadg;
    public String zzadh;
    public String zzadj;
    public String zzado;
    public String zzaef;
    public String zzafl;
    public Integer zzatc;
    public zzki[] zzatd;
    public zzkn[] zzate;
    public Long zzatf;
    public Long zzatg;
    public Long zzath;
    public Long zzati;
    public Long zzatj;
    public String zzatk;
    public String zzatl;
    public String zzatm;
    public Integer zzatn;
    public Long zzato;
    public Long zzatp;
    public String zzatq;
    public Boolean zzatr;
    public Long zzats;
    public Integer zzatt;
    public Boolean zzatu;
    public zzkh[] zzatv;
    public Integer zzatw;
    private Integer zzatx;
    private Integer zzaty;
    public String zzatz;
    public Long zzaua;
    public Long zzaub;
    public String zzauc;
    private String zzaud;
    public Integer zzaue;
    public String zztc;
    public String zztd;

    public zzkl() {
        this.zzatc = null;
        this.zzatd = zzki.zzld();
        this.zzate = zzkn.zzlg();
        this.zzatf = null;
        this.zzatg = null;
        this.zzath = null;
        this.zzati = null;
        this.zzatj = null;
        this.zzatk = null;
        this.zzatl = null;
        this.zzatm = null;
        this.zzafl = null;
        this.zzatn = null;
        this.zzado = null;
        this.zztd = null;
        this.zztc = null;
        this.zzato = null;
        this.zzatp = null;
        this.zzatq = null;
        this.zzatr = null;
        this.zzadg = null;
        this.zzats = null;
        this.zzatt = null;
        this.zzaef = null;
        this.zzadh = null;
        this.zzatu = null;
        this.zzatv = zzkh.zzlc();
        this.zzadj = null;
        this.zzatw = null;
        this.zzatx = null;
        this.zzaty = null;
        this.zzatz = null;
        this.zzaua = null;
        this.zzaub = null;
        this.zzauc = null;
        this.zzaud = null;
        this.zzaue = null;
        this.zzbzh = null;
        this.zzbzs = -1;
    }

    public static zzkl[] zzlf() {
        if (zzatb == null) {
            synchronized (zzabh.zzbzr) {
                if (zzatb == null) {
                    zzatb = new zzkl[0];
                }
            }
        }
        return zzatb;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzkl)) {
            return false;
        }
        zzkl com_google_android_gms_internal_measurement_zzkl = (zzkl) obj;
        if (this.zzatc == null) {
            if (com_google_android_gms_internal_measurement_zzkl.zzatc != null) {
                return false;
            }
        } else if (!this.zzatc.equals(com_google_android_gms_internal_measurement_zzkl.zzatc)) {
            return false;
        }
        if (!zzabh.equals(this.zzatd, com_google_android_gms_internal_measurement_zzkl.zzatd) || !zzabh.equals(this.zzate, com_google_android_gms_internal_measurement_zzkl.zzate)) {
            return false;
        }
        if (this.zzatf == null) {
            if (com_google_android_gms_internal_measurement_zzkl.zzatf != null) {
                return false;
            }
        } else if (!this.zzatf.equals(com_google_android_gms_internal_measurement_zzkl.zzatf)) {
            return false;
        }
        if (this.zzatg == null) {
            if (com_google_android_gms_internal_measurement_zzkl.zzatg != null) {
                return false;
            }
        } else if (!this.zzatg.equals(com_google_android_gms_internal_measurement_zzkl.zzatg)) {
            return false;
        }
        if (this.zzath == null) {
            if (com_google_android_gms_internal_measurement_zzkl.zzath != null) {
                return false;
            }
        } else if (!this.zzath.equals(com_google_android_gms_internal_measurement_zzkl.zzath)) {
            return false;
        }
        if (this.zzati == null) {
            if (com_google_android_gms_internal_measurement_zzkl.zzati != null) {
                return false;
            }
        } else if (!this.zzati.equals(com_google_android_gms_internal_measurement_zzkl.zzati)) {
            return false;
        }
        if (this.zzatj == null) {
            if (com_google_android_gms_internal_measurement_zzkl.zzatj != null) {
                return false;
            }
        } else if (!this.zzatj.equals(com_google_android_gms_internal_measurement_zzkl.zzatj)) {
            return false;
        }
        if (this.zzatk == null) {
            if (com_google_android_gms_internal_measurement_zzkl.zzatk != null) {
                return false;
            }
        } else if (!this.zzatk.equals(com_google_android_gms_internal_measurement_zzkl.zzatk)) {
            return false;
        }
        if (this.zzatl == null) {
            if (com_google_android_gms_internal_measurement_zzkl.zzatl != null) {
                return false;
            }
        } else if (!this.zzatl.equals(com_google_android_gms_internal_measurement_zzkl.zzatl)) {
            return false;
        }
        if (this.zzatm == null) {
            if (com_google_android_gms_internal_measurement_zzkl.zzatm != null) {
                return false;
            }
        } else if (!this.zzatm.equals(com_google_android_gms_internal_measurement_zzkl.zzatm)) {
            return false;
        }
        if (this.zzafl == null) {
            if (com_google_android_gms_internal_measurement_zzkl.zzafl != null) {
                return false;
            }
        } else if (!this.zzafl.equals(com_google_android_gms_internal_measurement_zzkl.zzafl)) {
            return false;
        }
        if (this.zzatn == null) {
            if (com_google_android_gms_internal_measurement_zzkl.zzatn != null) {
                return false;
            }
        } else if (!this.zzatn.equals(com_google_android_gms_internal_measurement_zzkl.zzatn)) {
            return false;
        }
        if (this.zzado == null) {
            if (com_google_android_gms_internal_measurement_zzkl.zzado != null) {
                return false;
            }
        } else if (!this.zzado.equals(com_google_android_gms_internal_measurement_zzkl.zzado)) {
            return false;
        }
        if (this.zztd == null) {
            if (com_google_android_gms_internal_measurement_zzkl.zztd != null) {
                return false;
            }
        } else if (!this.zztd.equals(com_google_android_gms_internal_measurement_zzkl.zztd)) {
            return false;
        }
        if (this.zztc == null) {
            if (com_google_android_gms_internal_measurement_zzkl.zztc != null) {
                return false;
            }
        } else if (!this.zztc.equals(com_google_android_gms_internal_measurement_zzkl.zztc)) {
            return false;
        }
        if (this.zzato == null) {
            if (com_google_android_gms_internal_measurement_zzkl.zzato != null) {
                return false;
            }
        } else if (!this.zzato.equals(com_google_android_gms_internal_measurement_zzkl.zzato)) {
            return false;
        }
        if (this.zzatp == null) {
            if (com_google_android_gms_internal_measurement_zzkl.zzatp != null) {
                return false;
            }
        } else if (!this.zzatp.equals(com_google_android_gms_internal_measurement_zzkl.zzatp)) {
            return false;
        }
        if (this.zzatq == null) {
            if (com_google_android_gms_internal_measurement_zzkl.zzatq != null) {
                return false;
            }
        } else if (!this.zzatq.equals(com_google_android_gms_internal_measurement_zzkl.zzatq)) {
            return false;
        }
        if (this.zzatr == null) {
            if (com_google_android_gms_internal_measurement_zzkl.zzatr != null) {
                return false;
            }
        } else if (!this.zzatr.equals(com_google_android_gms_internal_measurement_zzkl.zzatr)) {
            return false;
        }
        if (this.zzadg == null) {
            if (com_google_android_gms_internal_measurement_zzkl.zzadg != null) {
                return false;
            }
        } else if (!this.zzadg.equals(com_google_android_gms_internal_measurement_zzkl.zzadg)) {
            return false;
        }
        if (this.zzats == null) {
            if (com_google_android_gms_internal_measurement_zzkl.zzats != null) {
                return false;
            }
        } else if (!this.zzats.equals(com_google_android_gms_internal_measurement_zzkl.zzats)) {
            return false;
        }
        if (this.zzatt == null) {
            if (com_google_android_gms_internal_measurement_zzkl.zzatt != null) {
                return false;
            }
        } else if (!this.zzatt.equals(com_google_android_gms_internal_measurement_zzkl.zzatt)) {
            return false;
        }
        if (this.zzaef == null) {
            if (com_google_android_gms_internal_measurement_zzkl.zzaef != null) {
                return false;
            }
        } else if (!this.zzaef.equals(com_google_android_gms_internal_measurement_zzkl.zzaef)) {
            return false;
        }
        if (this.zzadh == null) {
            if (com_google_android_gms_internal_measurement_zzkl.zzadh != null) {
                return false;
            }
        } else if (!this.zzadh.equals(com_google_android_gms_internal_measurement_zzkl.zzadh)) {
            return false;
        }
        if (this.zzatu == null) {
            if (com_google_android_gms_internal_measurement_zzkl.zzatu != null) {
                return false;
            }
        } else if (!this.zzatu.equals(com_google_android_gms_internal_measurement_zzkl.zzatu)) {
            return false;
        }
        if (!zzabh.equals(this.zzatv, com_google_android_gms_internal_measurement_zzkl.zzatv)) {
            return false;
        }
        if (this.zzadj == null) {
            if (com_google_android_gms_internal_measurement_zzkl.zzadj != null) {
                return false;
            }
        } else if (!this.zzadj.equals(com_google_android_gms_internal_measurement_zzkl.zzadj)) {
            return false;
        }
        if (this.zzatw == null) {
            if (com_google_android_gms_internal_measurement_zzkl.zzatw != null) {
                return false;
            }
        } else if (!this.zzatw.equals(com_google_android_gms_internal_measurement_zzkl.zzatw)) {
            return false;
        }
        if (this.zzatx == null) {
            if (com_google_android_gms_internal_measurement_zzkl.zzatx != null) {
                return false;
            }
        } else if (!this.zzatx.equals(com_google_android_gms_internal_measurement_zzkl.zzatx)) {
            return false;
        }
        if (this.zzaty == null) {
            if (com_google_android_gms_internal_measurement_zzkl.zzaty != null) {
                return false;
            }
        } else if (!this.zzaty.equals(com_google_android_gms_internal_measurement_zzkl.zzaty)) {
            return false;
        }
        if (this.zzatz == null) {
            if (com_google_android_gms_internal_measurement_zzkl.zzatz != null) {
                return false;
            }
        } else if (!this.zzatz.equals(com_google_android_gms_internal_measurement_zzkl.zzatz)) {
            return false;
        }
        if (this.zzaua == null) {
            if (com_google_android_gms_internal_measurement_zzkl.zzaua != null) {
                return false;
            }
        } else if (!this.zzaua.equals(com_google_android_gms_internal_measurement_zzkl.zzaua)) {
            return false;
        }
        if (this.zzaub == null) {
            if (com_google_android_gms_internal_measurement_zzkl.zzaub != null) {
                return false;
            }
        } else if (!this.zzaub.equals(com_google_android_gms_internal_measurement_zzkl.zzaub)) {
            return false;
        }
        if (this.zzauc == null) {
            if (com_google_android_gms_internal_measurement_zzkl.zzauc != null) {
                return false;
            }
        } else if (!this.zzauc.equals(com_google_android_gms_internal_measurement_zzkl.zzauc)) {
            return false;
        }
        if (this.zzaud == null) {
            if (com_google_android_gms_internal_measurement_zzkl.zzaud != null) {
                return false;
            }
        } else if (!this.zzaud.equals(com_google_android_gms_internal_measurement_zzkl.zzaud)) {
            return false;
        }
        if (this.zzaue == null) {
            if (com_google_android_gms_internal_measurement_zzkl.zzaue != null) {
                return false;
            }
        } else if (!this.zzaue.equals(com_google_android_gms_internal_measurement_zzkl.zzaue)) {
            return false;
        }
        if (this.zzbzh != null) {
            if (!this.zzbzh.isEmpty()) {
                return this.zzbzh.equals(com_google_android_gms_internal_measurement_zzkl.zzbzh);
            }
        }
        return com_google_android_gms_internal_measurement_zzkl.zzbzh == null || com_google_android_gms_internal_measurement_zzkl.zzbzh.isEmpty();
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = (((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((527 + getClass().getName().hashCode()) * 31) + (this.zzatc == null ? 0 : this.zzatc.hashCode())) * 31) + zzabh.hashCode(this.zzatd)) * 31) + zzabh.hashCode(this.zzate)) * 31) + (this.zzatf == null ? 0 : this.zzatf.hashCode())) * 31) + (this.zzatg == null ? 0 : this.zzatg.hashCode())) * 31) + (this.zzath == null ? 0 : this.zzath.hashCode())) * 31) + (this.zzati == null ? 0 : this.zzati.hashCode())) * 31) + (this.zzatj == null ? 0 : this.zzatj.hashCode())) * 31) + (this.zzatk == null ? 0 : this.zzatk.hashCode())) * 31) + (this.zzatl == null ? 0 : this.zzatl.hashCode())) * 31) + (this.zzatm == null ? 0 : this.zzatm.hashCode())) * 31) + (this.zzafl == null ? 0 : this.zzafl.hashCode())) * 31) + (this.zzatn == null ? 0 : this.zzatn.hashCode())) * 31) + (this.zzado == null ? 0 : this.zzado.hashCode())) * 31) + (this.zztd == null ? 0 : this.zztd.hashCode())) * 31) + (this.zztc == null ? 0 : this.zztc.hashCode())) * 31) + (this.zzato == null ? 0 : this.zzato.hashCode())) * 31) + (this.zzatp == null ? 0 : this.zzatp.hashCode())) * 31) + (this.zzatq == null ? 0 : this.zzatq.hashCode())) * 31) + (this.zzatr == null ? 0 : this.zzatr.hashCode())) * 31) + (this.zzadg == null ? 0 : this.zzadg.hashCode())) * 31) + (this.zzats == null ? 0 : this.zzats.hashCode())) * 31) + (this.zzatt == null ? 0 : this.zzatt.hashCode())) * 31) + (this.zzaef == null ? 0 : this.zzaef.hashCode())) * 31) + (this.zzadh == null ? 0 : this.zzadh.hashCode())) * 31) + (this.zzatu == null ? 0 : this.zzatu.hashCode())) * 31) + zzabh.hashCode(this.zzatv)) * 31) + (this.zzadj == null ? 0 : this.zzadj.hashCode())) * 31) + (this.zzatw == null ? 0 : this.zzatw.hashCode())) * 31) + (this.zzatx == null ? 0 : this.zzatx.hashCode())) * 31) + (this.zzaty == null ? 0 : this.zzaty.hashCode())) * 31) + (this.zzatz == null ? 0 : this.zzatz.hashCode())) * 31) + (this.zzaua == null ? 0 : this.zzaua.hashCode())) * 31) + (this.zzaub == null ? 0 : this.zzaub.hashCode())) * 31) + (this.zzauc == null ? 0 : this.zzauc.hashCode())) * 31) + (this.zzaud == null ? 0 : this.zzaud.hashCode())) * 31) + (this.zzaue == null ? 0 : this.zzaue.hashCode())) * 31;
        if (this.zzbzh != null) {
            if (!this.zzbzh.isEmpty()) {
                i = this.zzbzh.hashCode();
            }
        }
        return hashCode + i;
    }

    protected final int zza() {
        int i;
        int zza = super.zza();
        if (this.zzatc != null) {
            zza += zzabb.zzf(1, this.zzatc.intValue());
        }
        int i2 = 0;
        if (this.zzatd != null && this.zzatd.length > 0) {
            i = zza;
            for (zzabj com_google_android_gms_internal_measurement_zzabj : this.zzatd) {
                if (com_google_android_gms_internal_measurement_zzabj != null) {
                    i += zzabb.zzb(2, com_google_android_gms_internal_measurement_zzabj);
                }
            }
            zza = i;
        }
        if (this.zzate != null && this.zzate.length > 0) {
            i = zza;
            for (zzabj com_google_android_gms_internal_measurement_zzabj2 : this.zzate) {
                if (com_google_android_gms_internal_measurement_zzabj2 != null) {
                    i += zzabb.zzb(3, com_google_android_gms_internal_measurement_zzabj2);
                }
            }
            zza = i;
        }
        if (this.zzatf != null) {
            zza += zzabb.zzc(4, this.zzatf.longValue());
        }
        if (this.zzatg != null) {
            zza += zzabb.zzc(5, this.zzatg.longValue());
        }
        if (this.zzath != null) {
            zza += zzabb.zzc(6, this.zzath.longValue());
        }
        if (this.zzatj != null) {
            zza += zzabb.zzc(7, this.zzatj.longValue());
        }
        if (this.zzatk != null) {
            zza += zzabb.zzd(8, this.zzatk);
        }
        if (this.zzatl != null) {
            zza += zzabb.zzd(9, this.zzatl);
        }
        if (this.zzatm != null) {
            zza += zzabb.zzd(10, this.zzatm);
        }
        if (this.zzafl != null) {
            zza += zzabb.zzd(11, this.zzafl);
        }
        if (this.zzatn != null) {
            zza += zzabb.zzf(12, this.zzatn.intValue());
        }
        if (this.zzado != null) {
            zza += zzabb.zzd(13, this.zzado);
        }
        if (this.zztd != null) {
            zza += zzabb.zzd(14, this.zztd);
        }
        if (this.zztc != null) {
            zza += zzabb.zzd(16, this.zztc);
        }
        if (this.zzato != null) {
            zza += zzabb.zzc(17, this.zzato.longValue());
        }
        if (this.zzatp != null) {
            zza += zzabb.zzc(18, this.zzatp.longValue());
        }
        if (this.zzatq != null) {
            zza += zzabb.zzd(19, this.zzatq);
        }
        if (this.zzatr != null) {
            this.zzatr.booleanValue();
            zza += zzabb.zzas(20) + 1;
        }
        if (this.zzadg != null) {
            zza += zzabb.zzd(21, this.zzadg);
        }
        if (this.zzats != null) {
            zza += zzabb.zzc(22, this.zzats.longValue());
        }
        if (this.zzatt != null) {
            zza += zzabb.zzf(23, this.zzatt.intValue());
        }
        if (this.zzaef != null) {
            zza += zzabb.zzd(24, this.zzaef);
        }
        if (this.zzadh != null) {
            zza += zzabb.zzd(25, this.zzadh);
        }
        if (this.zzati != null) {
            zza += zzabb.zzc(26, this.zzati.longValue());
        }
        if (this.zzatu != null) {
            this.zzatu.booleanValue();
            zza += zzabb.zzas(28) + 1;
        }
        if (this.zzatv != null && this.zzatv.length > 0) {
            while (i2 < this.zzatv.length) {
                zzabj com_google_android_gms_internal_measurement_zzabj3 = this.zzatv[i2];
                if (com_google_android_gms_internal_measurement_zzabj3 != null) {
                    zza += zzabb.zzb(29, com_google_android_gms_internal_measurement_zzabj3);
                }
                i2++;
            }
        }
        if (this.zzadj != null) {
            zza += zzabb.zzd(30, this.zzadj);
        }
        if (this.zzatw != null) {
            zza += zzabb.zzf(31, this.zzatw.intValue());
        }
        if (this.zzatx != null) {
            zza += zzabb.zzf(32, this.zzatx.intValue());
        }
        if (this.zzaty != null) {
            zza += zzabb.zzf(33, this.zzaty.intValue());
        }
        if (this.zzatz != null) {
            zza += zzabb.zzd(34, this.zzatz);
        }
        if (this.zzaua != null) {
            zza += zzabb.zzc(35, this.zzaua.longValue());
        }
        if (this.zzaub != null) {
            zza += zzabb.zzc(36, this.zzaub.longValue());
        }
        if (this.zzauc != null) {
            zza += zzabb.zzd(37, this.zzauc);
        }
        if (this.zzaud != null) {
            zza += zzabb.zzd(38, this.zzaud);
        }
        return this.zzaue != null ? zza + zzabb.zzf(39, this.zzaue.intValue()) : zza;
    }

    public final void zza(zzabb com_google_android_gms_internal_measurement_zzabb) throws IOException {
        if (this.zzatc != null) {
            com_google_android_gms_internal_measurement_zzabb.zze(1, this.zzatc.intValue());
        }
        int i = 0;
        if (this.zzatd != null && this.zzatd.length > 0) {
            for (zzabj com_google_android_gms_internal_measurement_zzabj : this.zzatd) {
                if (com_google_android_gms_internal_measurement_zzabj != null) {
                    com_google_android_gms_internal_measurement_zzabb.zza(2, com_google_android_gms_internal_measurement_zzabj);
                }
            }
        }
        if (this.zzate != null && this.zzate.length > 0) {
            for (zzabj com_google_android_gms_internal_measurement_zzabj2 : this.zzate) {
                if (com_google_android_gms_internal_measurement_zzabj2 != null) {
                    com_google_android_gms_internal_measurement_zzabb.zza(3, com_google_android_gms_internal_measurement_zzabj2);
                }
            }
        }
        if (this.zzatf != null) {
            com_google_android_gms_internal_measurement_zzabb.zzb(4, this.zzatf.longValue());
        }
        if (this.zzatg != null) {
            com_google_android_gms_internal_measurement_zzabb.zzb(5, this.zzatg.longValue());
        }
        if (this.zzath != null) {
            com_google_android_gms_internal_measurement_zzabb.zzb(6, this.zzath.longValue());
        }
        if (this.zzatj != null) {
            com_google_android_gms_internal_measurement_zzabb.zzb(7, this.zzatj.longValue());
        }
        if (this.zzatk != null) {
            com_google_android_gms_internal_measurement_zzabb.zzc(8, this.zzatk);
        }
        if (this.zzatl != null) {
            com_google_android_gms_internal_measurement_zzabb.zzc(9, this.zzatl);
        }
        if (this.zzatm != null) {
            com_google_android_gms_internal_measurement_zzabb.zzc(10, this.zzatm);
        }
        if (this.zzafl != null) {
            com_google_android_gms_internal_measurement_zzabb.zzc(11, this.zzafl);
        }
        if (this.zzatn != null) {
            com_google_android_gms_internal_measurement_zzabb.zze(12, this.zzatn.intValue());
        }
        if (this.zzado != null) {
            com_google_android_gms_internal_measurement_zzabb.zzc(13, this.zzado);
        }
        if (this.zztd != null) {
            com_google_android_gms_internal_measurement_zzabb.zzc(14, this.zztd);
        }
        if (this.zztc != null) {
            com_google_android_gms_internal_measurement_zzabb.zzc(16, this.zztc);
        }
        if (this.zzato != null) {
            com_google_android_gms_internal_measurement_zzabb.zzb(17, this.zzato.longValue());
        }
        if (this.zzatp != null) {
            com_google_android_gms_internal_measurement_zzabb.zzb(18, this.zzatp.longValue());
        }
        if (this.zzatq != null) {
            com_google_android_gms_internal_measurement_zzabb.zzc(19, this.zzatq);
        }
        if (this.zzatr != null) {
            com_google_android_gms_internal_measurement_zzabb.zza(20, this.zzatr.booleanValue());
        }
        if (this.zzadg != null) {
            com_google_android_gms_internal_measurement_zzabb.zzc(21, this.zzadg);
        }
        if (this.zzats != null) {
            com_google_android_gms_internal_measurement_zzabb.zzb(22, this.zzats.longValue());
        }
        if (this.zzatt != null) {
            com_google_android_gms_internal_measurement_zzabb.zze(23, this.zzatt.intValue());
        }
        if (this.zzaef != null) {
            com_google_android_gms_internal_measurement_zzabb.zzc(24, this.zzaef);
        }
        if (this.zzadh != null) {
            com_google_android_gms_internal_measurement_zzabb.zzc(25, this.zzadh);
        }
        if (this.zzati != null) {
            com_google_android_gms_internal_measurement_zzabb.zzb(26, this.zzati.longValue());
        }
        if (this.zzatu != null) {
            com_google_android_gms_internal_measurement_zzabb.zza(28, this.zzatu.booleanValue());
        }
        if (this.zzatv != null && this.zzatv.length > 0) {
            while (i < this.zzatv.length) {
                zzabj com_google_android_gms_internal_measurement_zzabj3 = this.zzatv[i];
                if (com_google_android_gms_internal_measurement_zzabj3 != null) {
                    com_google_android_gms_internal_measurement_zzabb.zza(29, com_google_android_gms_internal_measurement_zzabj3);
                }
                i++;
            }
        }
        if (this.zzadj != null) {
            com_google_android_gms_internal_measurement_zzabb.zzc(30, this.zzadj);
        }
        if (this.zzatw != null) {
            com_google_android_gms_internal_measurement_zzabb.zze(31, this.zzatw.intValue());
        }
        if (this.zzatx != null) {
            com_google_android_gms_internal_measurement_zzabb.zze(32, this.zzatx.intValue());
        }
        if (this.zzaty != null) {
            com_google_android_gms_internal_measurement_zzabb.zze(33, this.zzaty.intValue());
        }
        if (this.zzatz != null) {
            com_google_android_gms_internal_measurement_zzabb.zzc(34, this.zzatz);
        }
        if (this.zzaua != null) {
            com_google_android_gms_internal_measurement_zzabb.zzb(35, this.zzaua.longValue());
        }
        if (this.zzaub != null) {
            com_google_android_gms_internal_measurement_zzabb.zzb(36, this.zzaub.longValue());
        }
        if (this.zzauc != null) {
            com_google_android_gms_internal_measurement_zzabb.zzc(37, this.zzauc);
        }
        if (this.zzaud != null) {
            com_google_android_gms_internal_measurement_zzabb.zzc(38, this.zzaud);
        }
        if (this.zzaue != null) {
            com_google_android_gms_internal_measurement_zzabb.zze(39, this.zzaue.intValue());
        }
        super.zza(com_google_android_gms_internal_measurement_zzabb);
    }

    public final /* synthetic */ zzabj zzb(zzaba com_google_android_gms_internal_measurement_zzaba) throws IOException {
        while (true) {
            int zzvo = com_google_android_gms_internal_measurement_zzaba.zzvo();
            int length;
            Object obj;
            switch (zzvo) {
                case 0:
                    return this;
                case 8:
                    this.zzatc = Integer.valueOf(com_google_android_gms_internal_measurement_zzaba.zzvs());
                    break;
                case 18:
                    zzvo = zzabm.zzb(com_google_android_gms_internal_measurement_zzaba, 18);
                    length = this.zzatd == null ? 0 : this.zzatd.length;
                    obj = new zzki[(zzvo + length)];
                    if (length != 0) {
                        System.arraycopy(this.zzatd, 0, obj, 0, length);
                    }
                    while (length < obj.length - 1) {
                        obj[length] = new zzki();
                        com_google_android_gms_internal_measurement_zzaba.zza(obj[length]);
                        com_google_android_gms_internal_measurement_zzaba.zzvo();
                        length++;
                    }
                    obj[length] = new zzki();
                    com_google_android_gms_internal_measurement_zzaba.zza(obj[length]);
                    this.zzatd = obj;
                    break;
                case 26:
                    zzvo = zzabm.zzb(com_google_android_gms_internal_measurement_zzaba, 26);
                    length = this.zzate == null ? 0 : this.zzate.length;
                    obj = new zzkn[(zzvo + length)];
                    if (length != 0) {
                        System.arraycopy(this.zzate, 0, obj, 0, length);
                    }
                    while (length < obj.length - 1) {
                        obj[length] = new zzkn();
                        com_google_android_gms_internal_measurement_zzaba.zza(obj[length]);
                        com_google_android_gms_internal_measurement_zzaba.zzvo();
                        length++;
                    }
                    obj[length] = new zzkn();
                    com_google_android_gms_internal_measurement_zzaba.zza(obj[length]);
                    this.zzate = obj;
                    break;
                case 32:
                    this.zzatf = Long.valueOf(com_google_android_gms_internal_measurement_zzaba.zzvt());
                    break;
                case 40:
                    this.zzatg = Long.valueOf(com_google_android_gms_internal_measurement_zzaba.zzvt());
                    break;
                case 48:
                    this.zzath = Long.valueOf(com_google_android_gms_internal_measurement_zzaba.zzvt());
                    break;
                case 56:
                    this.zzatj = Long.valueOf(com_google_android_gms_internal_measurement_zzaba.zzvt());
                    break;
                case 66:
                    this.zzatk = com_google_android_gms_internal_measurement_zzaba.readString();
                    break;
                case VoIPService.CALL_MAX_LAYER /*74*/:
                    this.zzatl = com_google_android_gms_internal_measurement_zzaba.readString();
                    break;
                case 82:
                    this.zzatm = com_google_android_gms_internal_measurement_zzaba.readString();
                    break;
                case 90:
                    this.zzafl = com_google_android_gms_internal_measurement_zzaba.readString();
                    break;
                case 96:
                    this.zzatn = Integer.valueOf(com_google_android_gms_internal_measurement_zzaba.zzvs());
                    break;
                case 106:
                    this.zzado = com_google_android_gms_internal_measurement_zzaba.readString();
                    break;
                case 114:
                    this.zztd = com_google_android_gms_internal_measurement_zzaba.readString();
                    break;
                case TsExtractor.TS_STREAM_TYPE_HDMV_DTS /*130*/:
                    this.zztc = com_google_android_gms_internal_measurement_zzaba.readString();
                    break;
                case 136:
                    this.zzato = Long.valueOf(com_google_android_gms_internal_measurement_zzaba.zzvt());
                    break;
                case 144:
                    this.zzatp = Long.valueOf(com_google_android_gms_internal_measurement_zzaba.zzvt());
                    break;
                case 154:
                    this.zzatq = com_google_android_gms_internal_measurement_zzaba.readString();
                    break;
                case 160:
                    this.zzatr = Boolean.valueOf(com_google_android_gms_internal_measurement_zzaba.zzvr());
                    break;
                case 170:
                    this.zzadg = com_google_android_gms_internal_measurement_zzaba.readString();
                    break;
                case 176:
                    this.zzats = Long.valueOf(com_google_android_gms_internal_measurement_zzaba.zzvt());
                    break;
                case 184:
                    this.zzatt = Integer.valueOf(com_google_android_gms_internal_measurement_zzaba.zzvs());
                    break;
                case 194:
                    this.zzaef = com_google_android_gms_internal_measurement_zzaba.readString();
                    break;
                case 202:
                    this.zzadh = com_google_android_gms_internal_measurement_zzaba.readString();
                    break;
                case 208:
                    this.zzati = Long.valueOf(com_google_android_gms_internal_measurement_zzaba.zzvt());
                    break;
                case 224:
                    this.zzatu = Boolean.valueOf(com_google_android_gms_internal_measurement_zzaba.zzvr());
                    break;
                case 234:
                    zzvo = zzabm.zzb(com_google_android_gms_internal_measurement_zzaba, 234);
                    length = this.zzatv == null ? 0 : this.zzatv.length;
                    obj = new zzkh[(zzvo + length)];
                    if (length != 0) {
                        System.arraycopy(this.zzatv, 0, obj, 0, length);
                    }
                    while (length < obj.length - 1) {
                        obj[length] = new zzkh();
                        com_google_android_gms_internal_measurement_zzaba.zza(obj[length]);
                        com_google_android_gms_internal_measurement_zzaba.zzvo();
                        length++;
                    }
                    obj[length] = new zzkh();
                    com_google_android_gms_internal_measurement_zzaba.zza(obj[length]);
                    this.zzatv = obj;
                    break;
                case 242:
                    this.zzadj = com_google_android_gms_internal_measurement_zzaba.readString();
                    break;
                case 248:
                    this.zzatw = Integer.valueOf(com_google_android_gms_internal_measurement_zzaba.zzvs());
                    break;
                case 256:
                    this.zzatx = Integer.valueOf(com_google_android_gms_internal_measurement_zzaba.zzvs());
                    break;
                case 264:
                    this.zzaty = Integer.valueOf(com_google_android_gms_internal_measurement_zzaba.zzvs());
                    break;
                case 274:
                    this.zzatz = com_google_android_gms_internal_measurement_zzaba.readString();
                    break;
                case 280:
                    this.zzaua = Long.valueOf(com_google_android_gms_internal_measurement_zzaba.zzvt());
                    break;
                case 288:
                    this.zzaub = Long.valueOf(com_google_android_gms_internal_measurement_zzaba.zzvt());
                    break;
                case 298:
                    this.zzauc = com_google_android_gms_internal_measurement_zzaba.readString();
                    break;
                case 306:
                    this.zzaud = com_google_android_gms_internal_measurement_zzaba.readString();
                    break;
                case 312:
                    this.zzaue = Integer.valueOf(com_google_android_gms_internal_measurement_zzaba.zzvs());
                    break;
                default:
                    if (super.zza(com_google_android_gms_internal_measurement_zzaba, zzvo)) {
                        break;
                    }
                    return this;
            }
        }
    }
}
