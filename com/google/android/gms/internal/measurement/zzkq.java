package com.google.android.gms.internal.measurement;

import java.io.IOException;
import org.telegram.messenger.exoplayer2.extractor.ts.TsExtractor;
import org.telegram.messenger.voip.VoIPService;

public final class zzkq extends zzaby<zzkq> {
    private static volatile zzkq[] zzatg;
    public String zzadl;
    public String zzadm;
    public String zzado;
    public String zzadt;
    public String zzaek;
    public String zzafn;
    public Integer zzath;
    public zzkn[] zzati;
    public zzks[] zzatj;
    public Long zzatk;
    public Long zzatl;
    public Long zzatm;
    public Long zzatn;
    public Long zzato;
    public String zzatp;
    public String zzatq;
    public String zzatr;
    public Integer zzats;
    public Long zzatt;
    public Long zzatu;
    public String zzatv;
    public Boolean zzatw;
    public Long zzatx;
    public Integer zzaty;
    public Boolean zzatz;
    public zzkm[] zzaua;
    public Integer zzaub;
    private Integer zzauc;
    private Integer zzaud;
    public String zzaue;
    public Long zzauf;
    public Long zzaug;
    public String zzauh;
    private String zzaui;
    public Integer zzauj;
    public String zzth;
    public String zzti;

    public zzkq() {
        this.zzath = null;
        this.zzati = zzkn.zzll();
        this.zzatj = zzks.zzlo();
        this.zzatk = null;
        this.zzatl = null;
        this.zzatm = null;
        this.zzatn = null;
        this.zzato = null;
        this.zzatp = null;
        this.zzatq = null;
        this.zzatr = null;
        this.zzafn = null;
        this.zzats = null;
        this.zzadt = null;
        this.zzti = null;
        this.zzth = null;
        this.zzatt = null;
        this.zzatu = null;
        this.zzatv = null;
        this.zzatw = null;
        this.zzadl = null;
        this.zzatx = null;
        this.zzaty = null;
        this.zzaek = null;
        this.zzadm = null;
        this.zzatz = null;
        this.zzaua = zzkm.zzlk();
        this.zzado = null;
        this.zzaub = null;
        this.zzauc = null;
        this.zzaud = null;
        this.zzaue = null;
        this.zzauf = null;
        this.zzaug = null;
        this.zzauh = null;
        this.zzaui = null;
        this.zzauj = null;
        this.zzbww = null;
        this.zzbxh = -1;
    }

    public static zzkq[] zzln() {
        if (zzatg == null) {
            synchronized (zzacc.zzbxg) {
                if (zzatg == null) {
                    zzatg = new zzkq[0];
                }
            }
        }
        return zzatg;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzkq)) {
            return false;
        }
        zzkq com_google_android_gms_internal_measurement_zzkq = (zzkq) obj;
        if (this.zzath == null) {
            if (com_google_android_gms_internal_measurement_zzkq.zzath != null) {
                return false;
            }
        } else if (!this.zzath.equals(com_google_android_gms_internal_measurement_zzkq.zzath)) {
            return false;
        }
        if (!zzacc.equals(this.zzati, com_google_android_gms_internal_measurement_zzkq.zzati)) {
            return false;
        }
        if (!zzacc.equals(this.zzatj, com_google_android_gms_internal_measurement_zzkq.zzatj)) {
            return false;
        }
        if (this.zzatk == null) {
            if (com_google_android_gms_internal_measurement_zzkq.zzatk != null) {
                return false;
            }
        } else if (!this.zzatk.equals(com_google_android_gms_internal_measurement_zzkq.zzatk)) {
            return false;
        }
        if (this.zzatl == null) {
            if (com_google_android_gms_internal_measurement_zzkq.zzatl != null) {
                return false;
            }
        } else if (!this.zzatl.equals(com_google_android_gms_internal_measurement_zzkq.zzatl)) {
            return false;
        }
        if (this.zzatm == null) {
            if (com_google_android_gms_internal_measurement_zzkq.zzatm != null) {
                return false;
            }
        } else if (!this.zzatm.equals(com_google_android_gms_internal_measurement_zzkq.zzatm)) {
            return false;
        }
        if (this.zzatn == null) {
            if (com_google_android_gms_internal_measurement_zzkq.zzatn != null) {
                return false;
            }
        } else if (!this.zzatn.equals(com_google_android_gms_internal_measurement_zzkq.zzatn)) {
            return false;
        }
        if (this.zzato == null) {
            if (com_google_android_gms_internal_measurement_zzkq.zzato != null) {
                return false;
            }
        } else if (!this.zzato.equals(com_google_android_gms_internal_measurement_zzkq.zzato)) {
            return false;
        }
        if (this.zzatp == null) {
            if (com_google_android_gms_internal_measurement_zzkq.zzatp != null) {
                return false;
            }
        } else if (!this.zzatp.equals(com_google_android_gms_internal_measurement_zzkq.zzatp)) {
            return false;
        }
        if (this.zzatq == null) {
            if (com_google_android_gms_internal_measurement_zzkq.zzatq != null) {
                return false;
            }
        } else if (!this.zzatq.equals(com_google_android_gms_internal_measurement_zzkq.zzatq)) {
            return false;
        }
        if (this.zzatr == null) {
            if (com_google_android_gms_internal_measurement_zzkq.zzatr != null) {
                return false;
            }
        } else if (!this.zzatr.equals(com_google_android_gms_internal_measurement_zzkq.zzatr)) {
            return false;
        }
        if (this.zzafn == null) {
            if (com_google_android_gms_internal_measurement_zzkq.zzafn != null) {
                return false;
            }
        } else if (!this.zzafn.equals(com_google_android_gms_internal_measurement_zzkq.zzafn)) {
            return false;
        }
        if (this.zzats == null) {
            if (com_google_android_gms_internal_measurement_zzkq.zzats != null) {
                return false;
            }
        } else if (!this.zzats.equals(com_google_android_gms_internal_measurement_zzkq.zzats)) {
            return false;
        }
        if (this.zzadt == null) {
            if (com_google_android_gms_internal_measurement_zzkq.zzadt != null) {
                return false;
            }
        } else if (!this.zzadt.equals(com_google_android_gms_internal_measurement_zzkq.zzadt)) {
            return false;
        }
        if (this.zzti == null) {
            if (com_google_android_gms_internal_measurement_zzkq.zzti != null) {
                return false;
            }
        } else if (!this.zzti.equals(com_google_android_gms_internal_measurement_zzkq.zzti)) {
            return false;
        }
        if (this.zzth == null) {
            if (com_google_android_gms_internal_measurement_zzkq.zzth != null) {
                return false;
            }
        } else if (!this.zzth.equals(com_google_android_gms_internal_measurement_zzkq.zzth)) {
            return false;
        }
        if (this.zzatt == null) {
            if (com_google_android_gms_internal_measurement_zzkq.zzatt != null) {
                return false;
            }
        } else if (!this.zzatt.equals(com_google_android_gms_internal_measurement_zzkq.zzatt)) {
            return false;
        }
        if (this.zzatu == null) {
            if (com_google_android_gms_internal_measurement_zzkq.zzatu != null) {
                return false;
            }
        } else if (!this.zzatu.equals(com_google_android_gms_internal_measurement_zzkq.zzatu)) {
            return false;
        }
        if (this.zzatv == null) {
            if (com_google_android_gms_internal_measurement_zzkq.zzatv != null) {
                return false;
            }
        } else if (!this.zzatv.equals(com_google_android_gms_internal_measurement_zzkq.zzatv)) {
            return false;
        }
        if (this.zzatw == null) {
            if (com_google_android_gms_internal_measurement_zzkq.zzatw != null) {
                return false;
            }
        } else if (!this.zzatw.equals(com_google_android_gms_internal_measurement_zzkq.zzatw)) {
            return false;
        }
        if (this.zzadl == null) {
            if (com_google_android_gms_internal_measurement_zzkq.zzadl != null) {
                return false;
            }
        } else if (!this.zzadl.equals(com_google_android_gms_internal_measurement_zzkq.zzadl)) {
            return false;
        }
        if (this.zzatx == null) {
            if (com_google_android_gms_internal_measurement_zzkq.zzatx != null) {
                return false;
            }
        } else if (!this.zzatx.equals(com_google_android_gms_internal_measurement_zzkq.zzatx)) {
            return false;
        }
        if (this.zzaty == null) {
            if (com_google_android_gms_internal_measurement_zzkq.zzaty != null) {
                return false;
            }
        } else if (!this.zzaty.equals(com_google_android_gms_internal_measurement_zzkq.zzaty)) {
            return false;
        }
        if (this.zzaek == null) {
            if (com_google_android_gms_internal_measurement_zzkq.zzaek != null) {
                return false;
            }
        } else if (!this.zzaek.equals(com_google_android_gms_internal_measurement_zzkq.zzaek)) {
            return false;
        }
        if (this.zzadm == null) {
            if (com_google_android_gms_internal_measurement_zzkq.zzadm != null) {
                return false;
            }
        } else if (!this.zzadm.equals(com_google_android_gms_internal_measurement_zzkq.zzadm)) {
            return false;
        }
        if (this.zzatz == null) {
            if (com_google_android_gms_internal_measurement_zzkq.zzatz != null) {
                return false;
            }
        } else if (!this.zzatz.equals(com_google_android_gms_internal_measurement_zzkq.zzatz)) {
            return false;
        }
        if (!zzacc.equals(this.zzaua, com_google_android_gms_internal_measurement_zzkq.zzaua)) {
            return false;
        }
        if (this.zzado == null) {
            if (com_google_android_gms_internal_measurement_zzkq.zzado != null) {
                return false;
            }
        } else if (!this.zzado.equals(com_google_android_gms_internal_measurement_zzkq.zzado)) {
            return false;
        }
        if (this.zzaub == null) {
            if (com_google_android_gms_internal_measurement_zzkq.zzaub != null) {
                return false;
            }
        } else if (!this.zzaub.equals(com_google_android_gms_internal_measurement_zzkq.zzaub)) {
            return false;
        }
        if (this.zzauc == null) {
            if (com_google_android_gms_internal_measurement_zzkq.zzauc != null) {
                return false;
            }
        } else if (!this.zzauc.equals(com_google_android_gms_internal_measurement_zzkq.zzauc)) {
            return false;
        }
        if (this.zzaud == null) {
            if (com_google_android_gms_internal_measurement_zzkq.zzaud != null) {
                return false;
            }
        } else if (!this.zzaud.equals(com_google_android_gms_internal_measurement_zzkq.zzaud)) {
            return false;
        }
        if (this.zzaue == null) {
            if (com_google_android_gms_internal_measurement_zzkq.zzaue != null) {
                return false;
            }
        } else if (!this.zzaue.equals(com_google_android_gms_internal_measurement_zzkq.zzaue)) {
            return false;
        }
        if (this.zzauf == null) {
            if (com_google_android_gms_internal_measurement_zzkq.zzauf != null) {
                return false;
            }
        } else if (!this.zzauf.equals(com_google_android_gms_internal_measurement_zzkq.zzauf)) {
            return false;
        }
        if (this.zzaug == null) {
            if (com_google_android_gms_internal_measurement_zzkq.zzaug != null) {
                return false;
            }
        } else if (!this.zzaug.equals(com_google_android_gms_internal_measurement_zzkq.zzaug)) {
            return false;
        }
        if (this.zzauh == null) {
            if (com_google_android_gms_internal_measurement_zzkq.zzauh != null) {
                return false;
            }
        } else if (!this.zzauh.equals(com_google_android_gms_internal_measurement_zzkq.zzauh)) {
            return false;
        }
        if (this.zzaui == null) {
            if (com_google_android_gms_internal_measurement_zzkq.zzaui != null) {
                return false;
            }
        } else if (!this.zzaui.equals(com_google_android_gms_internal_measurement_zzkq.zzaui)) {
            return false;
        }
        if (this.zzauj == null) {
            if (com_google_android_gms_internal_measurement_zzkq.zzauj != null) {
                return false;
            }
        } else if (!this.zzauj.equals(com_google_android_gms_internal_measurement_zzkq.zzauj)) {
            return false;
        }
        return (this.zzbww == null || this.zzbww.isEmpty()) ? com_google_android_gms_internal_measurement_zzkq.zzbww == null || com_google_android_gms_internal_measurement_zzkq.zzbww.isEmpty() : this.zzbww.equals(com_google_android_gms_internal_measurement_zzkq.zzbww);
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = ((this.zzauj == null ? 0 : this.zzauj.hashCode()) + (((this.zzaui == null ? 0 : this.zzaui.hashCode()) + (((this.zzauh == null ? 0 : this.zzauh.hashCode()) + (((this.zzaug == null ? 0 : this.zzaug.hashCode()) + (((this.zzauf == null ? 0 : this.zzauf.hashCode()) + (((this.zzaue == null ? 0 : this.zzaue.hashCode()) + (((this.zzaud == null ? 0 : this.zzaud.hashCode()) + (((this.zzauc == null ? 0 : this.zzauc.hashCode()) + (((this.zzaub == null ? 0 : this.zzaub.hashCode()) + (((this.zzado == null ? 0 : this.zzado.hashCode()) + (((((this.zzatz == null ? 0 : this.zzatz.hashCode()) + (((this.zzadm == null ? 0 : this.zzadm.hashCode()) + (((this.zzaek == null ? 0 : this.zzaek.hashCode()) + (((this.zzaty == null ? 0 : this.zzaty.hashCode()) + (((this.zzatx == null ? 0 : this.zzatx.hashCode()) + (((this.zzadl == null ? 0 : this.zzadl.hashCode()) + (((this.zzatw == null ? 0 : this.zzatw.hashCode()) + (((this.zzatv == null ? 0 : this.zzatv.hashCode()) + (((this.zzatu == null ? 0 : this.zzatu.hashCode()) + (((this.zzatt == null ? 0 : this.zzatt.hashCode()) + (((this.zzth == null ? 0 : this.zzth.hashCode()) + (((this.zzti == null ? 0 : this.zzti.hashCode()) + (((this.zzadt == null ? 0 : this.zzadt.hashCode()) + (((this.zzats == null ? 0 : this.zzats.hashCode()) + (((this.zzafn == null ? 0 : this.zzafn.hashCode()) + (((this.zzatr == null ? 0 : this.zzatr.hashCode()) + (((this.zzatq == null ? 0 : this.zzatq.hashCode()) + (((this.zzatp == null ? 0 : this.zzatp.hashCode()) + (((this.zzato == null ? 0 : this.zzato.hashCode()) + (((this.zzatn == null ? 0 : this.zzatn.hashCode()) + (((this.zzatm == null ? 0 : this.zzatm.hashCode()) + (((this.zzatl == null ? 0 : this.zzatl.hashCode()) + (((this.zzatk == null ? 0 : this.zzatk.hashCode()) + (((((((this.zzath == null ? 0 : this.zzath.hashCode()) + ((getClass().getName().hashCode() + 527) * 31)) * 31) + zzacc.hashCode(this.zzati)) * 31) + zzacc.hashCode(this.zzatj)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31) + zzacc.hashCode(this.zzaua)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31;
        if (!(this.zzbww == null || this.zzbww.isEmpty())) {
            i = this.zzbww.hashCode();
        }
        return hashCode + i;
    }

    protected final int zza() {
        int i;
        int i2 = 0;
        int zza = super.zza();
        if (this.zzath != null) {
            zza += zzabw.zzf(1, this.zzath.intValue());
        }
        if (this.zzati != null && this.zzati.length > 0) {
            i = zza;
            for (zzace com_google_android_gms_internal_measurement_zzace : this.zzati) {
                if (com_google_android_gms_internal_measurement_zzace != null) {
                    i += zzabw.zzb(2, com_google_android_gms_internal_measurement_zzace);
                }
            }
            zza = i;
        }
        if (this.zzatj != null && this.zzatj.length > 0) {
            i = zza;
            for (zzace com_google_android_gms_internal_measurement_zzace2 : this.zzatj) {
                if (com_google_android_gms_internal_measurement_zzace2 != null) {
                    i += zzabw.zzb(3, com_google_android_gms_internal_measurement_zzace2);
                }
            }
            zza = i;
        }
        if (this.zzatk != null) {
            zza += zzabw.zzc(4, this.zzatk.longValue());
        }
        if (this.zzatl != null) {
            zza += zzabw.zzc(5, this.zzatl.longValue());
        }
        if (this.zzatm != null) {
            zza += zzabw.zzc(6, this.zzatm.longValue());
        }
        if (this.zzato != null) {
            zza += zzabw.zzc(7, this.zzato.longValue());
        }
        if (this.zzatp != null) {
            zza += zzabw.zzc(8, this.zzatp);
        }
        if (this.zzatq != null) {
            zza += zzabw.zzc(9, this.zzatq);
        }
        if (this.zzatr != null) {
            zza += zzabw.zzc(10, this.zzatr);
        }
        if (this.zzafn != null) {
            zza += zzabw.zzc(11, this.zzafn);
        }
        if (this.zzats != null) {
            zza += zzabw.zzf(12, this.zzats.intValue());
        }
        if (this.zzadt != null) {
            zza += zzabw.zzc(13, this.zzadt);
        }
        if (this.zzti != null) {
            zza += zzabw.zzc(14, this.zzti);
        }
        if (this.zzth != null) {
            zza += zzabw.zzc(16, this.zzth);
        }
        if (this.zzatt != null) {
            zza += zzabw.zzc(17, this.zzatt.longValue());
        }
        if (this.zzatu != null) {
            zza += zzabw.zzc(18, this.zzatu.longValue());
        }
        if (this.zzatv != null) {
            zza += zzabw.zzc(19, this.zzatv);
        }
        if (this.zzatw != null) {
            this.zzatw.booleanValue();
            zza += zzabw.zzaq(20) + 1;
        }
        if (this.zzadl != null) {
            zza += zzabw.zzc(21, this.zzadl);
        }
        if (this.zzatx != null) {
            zza += zzabw.zzc(22, this.zzatx.longValue());
        }
        if (this.zzaty != null) {
            zza += zzabw.zzf(23, this.zzaty.intValue());
        }
        if (this.zzaek != null) {
            zza += zzabw.zzc(24, this.zzaek);
        }
        if (this.zzadm != null) {
            zza += zzabw.zzc(25, this.zzadm);
        }
        if (this.zzatn != null) {
            zza += zzabw.zzc(26, this.zzatn.longValue());
        }
        if (this.zzatz != null) {
            this.zzatz.booleanValue();
            zza += zzabw.zzaq(28) + 1;
        }
        if (this.zzaua != null && this.zzaua.length > 0) {
            while (i2 < this.zzaua.length) {
                zzace com_google_android_gms_internal_measurement_zzace3 = this.zzaua[i2];
                if (com_google_android_gms_internal_measurement_zzace3 != null) {
                    zza += zzabw.zzb(29, com_google_android_gms_internal_measurement_zzace3);
                }
                i2++;
            }
        }
        if (this.zzado != null) {
            zza += zzabw.zzc(30, this.zzado);
        }
        if (this.zzaub != null) {
            zza += zzabw.zzf(31, this.zzaub.intValue());
        }
        if (this.zzauc != null) {
            zza += zzabw.zzf(32, this.zzauc.intValue());
        }
        if (this.zzaud != null) {
            zza += zzabw.zzf(33, this.zzaud.intValue());
        }
        if (this.zzaue != null) {
            zza += zzabw.zzc(34, this.zzaue);
        }
        if (this.zzauf != null) {
            zza += zzabw.zzc(35, this.zzauf.longValue());
        }
        if (this.zzaug != null) {
            zza += zzabw.zzc(36, this.zzaug.longValue());
        }
        if (this.zzauh != null) {
            zza += zzabw.zzc(37, this.zzauh);
        }
        if (this.zzaui != null) {
            zza += zzabw.zzc(38, this.zzaui);
        }
        return this.zzauj != null ? zza + zzabw.zzf(39, this.zzauj.intValue()) : zza;
    }

    public final void zza(zzabw com_google_android_gms_internal_measurement_zzabw) throws IOException {
        int i = 0;
        if (this.zzath != null) {
            com_google_android_gms_internal_measurement_zzabw.zze(1, this.zzath.intValue());
        }
        if (this.zzati != null && this.zzati.length > 0) {
            for (zzace com_google_android_gms_internal_measurement_zzace : this.zzati) {
                if (com_google_android_gms_internal_measurement_zzace != null) {
                    com_google_android_gms_internal_measurement_zzabw.zza(2, com_google_android_gms_internal_measurement_zzace);
                }
            }
        }
        if (this.zzatj != null && this.zzatj.length > 0) {
            for (zzace com_google_android_gms_internal_measurement_zzace2 : this.zzatj) {
                if (com_google_android_gms_internal_measurement_zzace2 != null) {
                    com_google_android_gms_internal_measurement_zzabw.zza(3, com_google_android_gms_internal_measurement_zzace2);
                }
            }
        }
        if (this.zzatk != null) {
            com_google_android_gms_internal_measurement_zzabw.zzb(4, this.zzatk.longValue());
        }
        if (this.zzatl != null) {
            com_google_android_gms_internal_measurement_zzabw.zzb(5, this.zzatl.longValue());
        }
        if (this.zzatm != null) {
            com_google_android_gms_internal_measurement_zzabw.zzb(6, this.zzatm.longValue());
        }
        if (this.zzato != null) {
            com_google_android_gms_internal_measurement_zzabw.zzb(7, this.zzato.longValue());
        }
        if (this.zzatp != null) {
            com_google_android_gms_internal_measurement_zzabw.zzb(8, this.zzatp);
        }
        if (this.zzatq != null) {
            com_google_android_gms_internal_measurement_zzabw.zzb(9, this.zzatq);
        }
        if (this.zzatr != null) {
            com_google_android_gms_internal_measurement_zzabw.zzb(10, this.zzatr);
        }
        if (this.zzafn != null) {
            com_google_android_gms_internal_measurement_zzabw.zzb(11, this.zzafn);
        }
        if (this.zzats != null) {
            com_google_android_gms_internal_measurement_zzabw.zze(12, this.zzats.intValue());
        }
        if (this.zzadt != null) {
            com_google_android_gms_internal_measurement_zzabw.zzb(13, this.zzadt);
        }
        if (this.zzti != null) {
            com_google_android_gms_internal_measurement_zzabw.zzb(14, this.zzti);
        }
        if (this.zzth != null) {
            com_google_android_gms_internal_measurement_zzabw.zzb(16, this.zzth);
        }
        if (this.zzatt != null) {
            com_google_android_gms_internal_measurement_zzabw.zzb(17, this.zzatt.longValue());
        }
        if (this.zzatu != null) {
            com_google_android_gms_internal_measurement_zzabw.zzb(18, this.zzatu.longValue());
        }
        if (this.zzatv != null) {
            com_google_android_gms_internal_measurement_zzabw.zzb(19, this.zzatv);
        }
        if (this.zzatw != null) {
            com_google_android_gms_internal_measurement_zzabw.zza(20, this.zzatw.booleanValue());
        }
        if (this.zzadl != null) {
            com_google_android_gms_internal_measurement_zzabw.zzb(21, this.zzadl);
        }
        if (this.zzatx != null) {
            com_google_android_gms_internal_measurement_zzabw.zzb(22, this.zzatx.longValue());
        }
        if (this.zzaty != null) {
            com_google_android_gms_internal_measurement_zzabw.zze(23, this.zzaty.intValue());
        }
        if (this.zzaek != null) {
            com_google_android_gms_internal_measurement_zzabw.zzb(24, this.zzaek);
        }
        if (this.zzadm != null) {
            com_google_android_gms_internal_measurement_zzabw.zzb(25, this.zzadm);
        }
        if (this.zzatn != null) {
            com_google_android_gms_internal_measurement_zzabw.zzb(26, this.zzatn.longValue());
        }
        if (this.zzatz != null) {
            com_google_android_gms_internal_measurement_zzabw.zza(28, this.zzatz.booleanValue());
        }
        if (this.zzaua != null && this.zzaua.length > 0) {
            while (i < this.zzaua.length) {
                zzace com_google_android_gms_internal_measurement_zzace3 = this.zzaua[i];
                if (com_google_android_gms_internal_measurement_zzace3 != null) {
                    com_google_android_gms_internal_measurement_zzabw.zza(29, com_google_android_gms_internal_measurement_zzace3);
                }
                i++;
            }
        }
        if (this.zzado != null) {
            com_google_android_gms_internal_measurement_zzabw.zzb(30, this.zzado);
        }
        if (this.zzaub != null) {
            com_google_android_gms_internal_measurement_zzabw.zze(31, this.zzaub.intValue());
        }
        if (this.zzauc != null) {
            com_google_android_gms_internal_measurement_zzabw.zze(32, this.zzauc.intValue());
        }
        if (this.zzaud != null) {
            com_google_android_gms_internal_measurement_zzabw.zze(33, this.zzaud.intValue());
        }
        if (this.zzaue != null) {
            com_google_android_gms_internal_measurement_zzabw.zzb(34, this.zzaue);
        }
        if (this.zzauf != null) {
            com_google_android_gms_internal_measurement_zzabw.zzb(35, this.zzauf.longValue());
        }
        if (this.zzaug != null) {
            com_google_android_gms_internal_measurement_zzabw.zzb(36, this.zzaug.longValue());
        }
        if (this.zzauh != null) {
            com_google_android_gms_internal_measurement_zzabw.zzb(37, this.zzauh);
        }
        if (this.zzaui != null) {
            com_google_android_gms_internal_measurement_zzabw.zzb(38, this.zzaui);
        }
        if (this.zzauj != null) {
            com_google_android_gms_internal_measurement_zzabw.zze(39, this.zzauj.intValue());
        }
        super.zza(com_google_android_gms_internal_measurement_zzabw);
    }

    public final /* synthetic */ zzace zzb(zzabv com_google_android_gms_internal_measurement_zzabv) throws IOException {
        while (true) {
            int zzuw = com_google_android_gms_internal_measurement_zzabv.zzuw();
            int zzb;
            Object obj;
            switch (zzuw) {
                case 0:
                    break;
                case 8:
                    this.zzath = Integer.valueOf(com_google_android_gms_internal_measurement_zzabv.zzuy());
                    continue;
                case 18:
                    zzb = zzach.zzb(com_google_android_gms_internal_measurement_zzabv, 18);
                    zzuw = this.zzati == null ? 0 : this.zzati.length;
                    obj = new zzkn[(zzb + zzuw)];
                    if (zzuw != 0) {
                        System.arraycopy(this.zzati, 0, obj, 0, zzuw);
                    }
                    while (zzuw < obj.length - 1) {
                        obj[zzuw] = new zzkn();
                        com_google_android_gms_internal_measurement_zzabv.zza(obj[zzuw]);
                        com_google_android_gms_internal_measurement_zzabv.zzuw();
                        zzuw++;
                    }
                    obj[zzuw] = new zzkn();
                    com_google_android_gms_internal_measurement_zzabv.zza(obj[zzuw]);
                    this.zzati = obj;
                    continue;
                case 26:
                    zzb = zzach.zzb(com_google_android_gms_internal_measurement_zzabv, 26);
                    zzuw = this.zzatj == null ? 0 : this.zzatj.length;
                    obj = new zzks[(zzb + zzuw)];
                    if (zzuw != 0) {
                        System.arraycopy(this.zzatj, 0, obj, 0, zzuw);
                    }
                    while (zzuw < obj.length - 1) {
                        obj[zzuw] = new zzks();
                        com_google_android_gms_internal_measurement_zzabv.zza(obj[zzuw]);
                        com_google_android_gms_internal_measurement_zzabv.zzuw();
                        zzuw++;
                    }
                    obj[zzuw] = new zzks();
                    com_google_android_gms_internal_measurement_zzabv.zza(obj[zzuw]);
                    this.zzatj = obj;
                    continue;
                case 32:
                    this.zzatk = Long.valueOf(com_google_android_gms_internal_measurement_zzabv.zzuz());
                    continue;
                case 40:
                    this.zzatl = Long.valueOf(com_google_android_gms_internal_measurement_zzabv.zzuz());
                    continue;
                case 48:
                    this.zzatm = Long.valueOf(com_google_android_gms_internal_measurement_zzabv.zzuz());
                    continue;
                case 56:
                    this.zzato = Long.valueOf(com_google_android_gms_internal_measurement_zzabv.zzuz());
                    continue;
                case 66:
                    this.zzatp = com_google_android_gms_internal_measurement_zzabv.readString();
                    continue;
                case VoIPService.CALL_MAX_LAYER /*74*/:
                    this.zzatq = com_google_android_gms_internal_measurement_zzabv.readString();
                    continue;
                case 82:
                    this.zzatr = com_google_android_gms_internal_measurement_zzabv.readString();
                    continue;
                case 90:
                    this.zzafn = com_google_android_gms_internal_measurement_zzabv.readString();
                    continue;
                case 96:
                    this.zzats = Integer.valueOf(com_google_android_gms_internal_measurement_zzabv.zzuy());
                    continue;
                case 106:
                    this.zzadt = com_google_android_gms_internal_measurement_zzabv.readString();
                    continue;
                case 114:
                    this.zzti = com_google_android_gms_internal_measurement_zzabv.readString();
                    continue;
                case TsExtractor.TS_STREAM_TYPE_HDMV_DTS /*130*/:
                    this.zzth = com_google_android_gms_internal_measurement_zzabv.readString();
                    continue;
                case 136:
                    this.zzatt = Long.valueOf(com_google_android_gms_internal_measurement_zzabv.zzuz());
                    continue;
                case 144:
                    this.zzatu = Long.valueOf(com_google_android_gms_internal_measurement_zzabv.zzuz());
                    continue;
                case 154:
                    this.zzatv = com_google_android_gms_internal_measurement_zzabv.readString();
                    continue;
                case 160:
                    this.zzatw = Boolean.valueOf(com_google_android_gms_internal_measurement_zzabv.zzux());
                    continue;
                case 170:
                    this.zzadl = com_google_android_gms_internal_measurement_zzabv.readString();
                    continue;
                case 176:
                    this.zzatx = Long.valueOf(com_google_android_gms_internal_measurement_zzabv.zzuz());
                    continue;
                case 184:
                    this.zzaty = Integer.valueOf(com_google_android_gms_internal_measurement_zzabv.zzuy());
                    continue;
                case 194:
                    this.zzaek = com_google_android_gms_internal_measurement_zzabv.readString();
                    continue;
                case 202:
                    this.zzadm = com_google_android_gms_internal_measurement_zzabv.readString();
                    continue;
                case 208:
                    this.zzatn = Long.valueOf(com_google_android_gms_internal_measurement_zzabv.zzuz());
                    continue;
                case 224:
                    this.zzatz = Boolean.valueOf(com_google_android_gms_internal_measurement_zzabv.zzux());
                    continue;
                case 234:
                    zzb = zzach.zzb(com_google_android_gms_internal_measurement_zzabv, 234);
                    zzuw = this.zzaua == null ? 0 : this.zzaua.length;
                    obj = new zzkm[(zzb + zzuw)];
                    if (zzuw != 0) {
                        System.arraycopy(this.zzaua, 0, obj, 0, zzuw);
                    }
                    while (zzuw < obj.length - 1) {
                        obj[zzuw] = new zzkm();
                        com_google_android_gms_internal_measurement_zzabv.zza(obj[zzuw]);
                        com_google_android_gms_internal_measurement_zzabv.zzuw();
                        zzuw++;
                    }
                    obj[zzuw] = new zzkm();
                    com_google_android_gms_internal_measurement_zzabv.zza(obj[zzuw]);
                    this.zzaua = obj;
                    continue;
                case 242:
                    this.zzado = com_google_android_gms_internal_measurement_zzabv.readString();
                    continue;
                case 248:
                    this.zzaub = Integer.valueOf(com_google_android_gms_internal_measurement_zzabv.zzuy());
                    continue;
                case 256:
                    this.zzauc = Integer.valueOf(com_google_android_gms_internal_measurement_zzabv.zzuy());
                    continue;
                case 264:
                    this.zzaud = Integer.valueOf(com_google_android_gms_internal_measurement_zzabv.zzuy());
                    continue;
                case 274:
                    this.zzaue = com_google_android_gms_internal_measurement_zzabv.readString();
                    continue;
                case 280:
                    this.zzauf = Long.valueOf(com_google_android_gms_internal_measurement_zzabv.zzuz());
                    continue;
                case 288:
                    this.zzaug = Long.valueOf(com_google_android_gms_internal_measurement_zzabv.zzuz());
                    continue;
                case 298:
                    this.zzauh = com_google_android_gms_internal_measurement_zzabv.readString();
                    continue;
                case 306:
                    this.zzaui = com_google_android_gms_internal_measurement_zzabv.readString();
                    continue;
                case 312:
                    this.zzauj = Integer.valueOf(com_google_android_gms_internal_measurement_zzabv.zzuy());
                    continue;
                default:
                    if (!super.zza(com_google_android_gms_internal_measurement_zzabv, zzuw)) {
                        break;
                    }
                    continue;
            }
            return this;
        }
    }
}
