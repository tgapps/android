package com.google.android.gms.internal.measurement;

import java.io.IOException;
import org.telegram.messenger.exoplayer2.RendererCapabilities;
import org.telegram.messenger.voip.VoIPService;

public final class zzabo extends zzabd<zzabo> {
    private String zzbsl;
    public String zzbsm;
    public String zzbsn;
    public String zzbso;
    public String zzbsp;
    public String zzcag;
    public String zzcah;
    public long zzcai;
    public String zzcaj;
    public long zzcak;
    public int zzcal;
    public zzabn[] zzcam;
    public long zzrp;

    public zzabo() {
        this.zzcag = TtmlNode.ANONYMOUS_REGION_ID;
        this.zzcah = TtmlNode.ANONYMOUS_REGION_ID;
        this.zzcai = 0;
        this.zzcaj = TtmlNode.ANONYMOUS_REGION_ID;
        this.zzcak = 0;
        this.zzrp = 0;
        this.zzbsl = TtmlNode.ANONYMOUS_REGION_ID;
        this.zzbsm = TtmlNode.ANONYMOUS_REGION_ID;
        this.zzbsp = TtmlNode.ANONYMOUS_REGION_ID;
        this.zzbsn = TtmlNode.ANONYMOUS_REGION_ID;
        this.zzbso = TtmlNode.ANONYMOUS_REGION_ID;
        this.zzcal = 0;
        this.zzcam = zzabn.zzwh();
        this.zzbzh = null;
        this.zzbzs = -1;
    }

    protected final int zza() {
        int zza = super.zza();
        if (!(this.zzcag == null || this.zzcag.equals(TtmlNode.ANONYMOUS_REGION_ID))) {
            zza += zzabb.zzd(1, this.zzcag);
        }
        if (!(this.zzcah == null || this.zzcah.equals(TtmlNode.ANONYMOUS_REGION_ID))) {
            zza += zzabb.zzd(2, this.zzcah);
        }
        if (this.zzcai != 0) {
            zza += zzabb.zzc(3, this.zzcai);
        }
        if (!(this.zzcaj == null || this.zzcaj.equals(TtmlNode.ANONYMOUS_REGION_ID))) {
            zza += zzabb.zzd(4, this.zzcaj);
        }
        if (this.zzcak != 0) {
            zza += zzabb.zzc(5, this.zzcak);
        }
        if (this.zzrp != 0) {
            zza += zzabb.zzc(6, this.zzrp);
        }
        if (!(this.zzbsl == null || this.zzbsl.equals(TtmlNode.ANONYMOUS_REGION_ID))) {
            zza += zzabb.zzd(7, this.zzbsl);
        }
        if (!(this.zzbsm == null || this.zzbsm.equals(TtmlNode.ANONYMOUS_REGION_ID))) {
            zza += zzabb.zzd(8, this.zzbsm);
        }
        if (!(this.zzbsp == null || this.zzbsp.equals(TtmlNode.ANONYMOUS_REGION_ID))) {
            zza += zzabb.zzd(9, this.zzbsp);
        }
        if (!(this.zzbsn == null || this.zzbsn.equals(TtmlNode.ANONYMOUS_REGION_ID))) {
            zza += zzabb.zzd(10, this.zzbsn);
        }
        if (!(this.zzbso == null || this.zzbso.equals(TtmlNode.ANONYMOUS_REGION_ID))) {
            zza += zzabb.zzd(11, this.zzbso);
        }
        if (this.zzcal != 0) {
            zza += zzabb.zzf(12, this.zzcal);
        }
        if (this.zzcam != null && this.zzcam.length > 0) {
            for (zzabj com_google_android_gms_internal_measurement_zzabj : this.zzcam) {
                if (com_google_android_gms_internal_measurement_zzabj != null) {
                    zza += zzabb.zzb(13, com_google_android_gms_internal_measurement_zzabj);
                }
            }
        }
        return zza;
    }

    public final void zza(zzabb com_google_android_gms_internal_measurement_zzabb) throws IOException {
        if (!(this.zzcag == null || this.zzcag.equals(TtmlNode.ANONYMOUS_REGION_ID))) {
            com_google_android_gms_internal_measurement_zzabb.zzc(1, this.zzcag);
        }
        if (!(this.zzcah == null || this.zzcah.equals(TtmlNode.ANONYMOUS_REGION_ID))) {
            com_google_android_gms_internal_measurement_zzabb.zzc(2, this.zzcah);
        }
        if (this.zzcai != 0) {
            com_google_android_gms_internal_measurement_zzabb.zzb(3, this.zzcai);
        }
        if (!(this.zzcaj == null || this.zzcaj.equals(TtmlNode.ANONYMOUS_REGION_ID))) {
            com_google_android_gms_internal_measurement_zzabb.zzc(4, this.zzcaj);
        }
        if (this.zzcak != 0) {
            com_google_android_gms_internal_measurement_zzabb.zzb(5, this.zzcak);
        }
        if (this.zzrp != 0) {
            com_google_android_gms_internal_measurement_zzabb.zzb(6, this.zzrp);
        }
        if (!(this.zzbsl == null || this.zzbsl.equals(TtmlNode.ANONYMOUS_REGION_ID))) {
            com_google_android_gms_internal_measurement_zzabb.zzc(7, this.zzbsl);
        }
        if (!(this.zzbsm == null || this.zzbsm.equals(TtmlNode.ANONYMOUS_REGION_ID))) {
            com_google_android_gms_internal_measurement_zzabb.zzc(8, this.zzbsm);
        }
        if (!(this.zzbsp == null || this.zzbsp.equals(TtmlNode.ANONYMOUS_REGION_ID))) {
            com_google_android_gms_internal_measurement_zzabb.zzc(9, this.zzbsp);
        }
        if (!(this.zzbsn == null || this.zzbsn.equals(TtmlNode.ANONYMOUS_REGION_ID))) {
            com_google_android_gms_internal_measurement_zzabb.zzc(10, this.zzbsn);
        }
        if (!(this.zzbso == null || this.zzbso.equals(TtmlNode.ANONYMOUS_REGION_ID))) {
            com_google_android_gms_internal_measurement_zzabb.zzc(11, this.zzbso);
        }
        if (this.zzcal != 0) {
            com_google_android_gms_internal_measurement_zzabb.zze(12, this.zzcal);
        }
        if (this.zzcam != null && this.zzcam.length > 0) {
            for (zzabj com_google_android_gms_internal_measurement_zzabj : this.zzcam) {
                if (com_google_android_gms_internal_measurement_zzabj != null) {
                    com_google_android_gms_internal_measurement_zzabb.zza(13, com_google_android_gms_internal_measurement_zzabj);
                }
            }
        }
        super.zza(com_google_android_gms_internal_measurement_zzabb);
    }

    public final /* synthetic */ zzabj zzb(zzaba com_google_android_gms_internal_measurement_zzaba) throws IOException {
        while (true) {
            int zzvo = com_google_android_gms_internal_measurement_zzaba.zzvo();
            switch (zzvo) {
                case 0:
                    return this;
                case 10:
                    this.zzcag = com_google_android_gms_internal_measurement_zzaba.readString();
                    break;
                case 18:
                    this.zzcah = com_google_android_gms_internal_measurement_zzaba.readString();
                    break;
                case RendererCapabilities.ADAPTIVE_SUPPORT_MASK /*24*/:
                    this.zzcai = com_google_android_gms_internal_measurement_zzaba.zzvp();
                    break;
                case 34:
                    this.zzcaj = com_google_android_gms_internal_measurement_zzaba.readString();
                    break;
                case 40:
                    this.zzcak = com_google_android_gms_internal_measurement_zzaba.zzvp();
                    break;
                case 48:
                    this.zzrp = com_google_android_gms_internal_measurement_zzaba.zzvp();
                    break;
                case 58:
                    this.zzbsl = com_google_android_gms_internal_measurement_zzaba.readString();
                    break;
                case 66:
                    this.zzbsm = com_google_android_gms_internal_measurement_zzaba.readString();
                    break;
                case VoIPService.CALL_MAX_LAYER /*74*/:
                    this.zzbsp = com_google_android_gms_internal_measurement_zzaba.readString();
                    break;
                case 82:
                    this.zzbsn = com_google_android_gms_internal_measurement_zzaba.readString();
                    break;
                case 90:
                    this.zzbso = com_google_android_gms_internal_measurement_zzaba.readString();
                    break;
                case 96:
                    this.zzcal = com_google_android_gms_internal_measurement_zzaba.zzvq();
                    break;
                case 106:
                    zzvo = zzabm.zzb(com_google_android_gms_internal_measurement_zzaba, 106);
                    int length = this.zzcam == null ? 0 : this.zzcam.length;
                    Object obj = new zzabn[(zzvo + length)];
                    if (length != 0) {
                        System.arraycopy(this.zzcam, 0, obj, 0, length);
                    }
                    while (length < obj.length - 1) {
                        obj[length] = new zzabn();
                        com_google_android_gms_internal_measurement_zzaba.zza(obj[length]);
                        com_google_android_gms_internal_measurement_zzaba.zzvo();
                        length++;
                    }
                    obj[length] = new zzabn();
                    com_google_android_gms_internal_measurement_zzaba.zza(obj[length]);
                    this.zzcam = obj;
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
