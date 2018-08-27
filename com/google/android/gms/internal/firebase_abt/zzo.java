package com.google.android.gms.internal.firebase_abt;

import java.io.IOException;
import org.telegram.messenger.voip.VoIPService;

public final class zzo extends zzd<zzo> {
    public String zzaq;
    public String zzar;
    public long zzas;
    public String zzat;
    public long zzau;
    public long zzav;
    private String zzaw;
    private String zzax;
    private String zzay;
    private String zzaz;
    private String zzba;
    public zzn[] zzbb;
    public int zzc;

    public zzo() {
        this.zzaq = TtmlNode.ANONYMOUS_REGION_ID;
        this.zzar = TtmlNode.ANONYMOUS_REGION_ID;
        this.zzas = 0;
        this.zzat = TtmlNode.ANONYMOUS_REGION_ID;
        this.zzau = 0;
        this.zzav = 0;
        this.zzaw = TtmlNode.ANONYMOUS_REGION_ID;
        this.zzax = TtmlNode.ANONYMOUS_REGION_ID;
        this.zzay = TtmlNode.ANONYMOUS_REGION_ID;
        this.zzaz = TtmlNode.ANONYMOUS_REGION_ID;
        this.zzba = TtmlNode.ANONYMOUS_REGION_ID;
        this.zzc = 0;
        this.zzbb = zzn.zzo();
        this.zzs = null;
        this.zzab = -1;
    }

    public final /* synthetic */ zzj zza(zza com_google_android_gms_internal_firebase_abt_zza) throws IOException {
        while (true) {
            int zzd = com_google_android_gms_internal_firebase_abt_zza.zzd();
            switch (zzd) {
                case 0:
                    break;
                case 10:
                    this.zzaq = com_google_android_gms_internal_firebase_abt_zza.readString();
                    continue;
                case 18:
                    this.zzar = com_google_android_gms_internal_firebase_abt_zza.readString();
                    continue;
                case 24:
                    this.zzas = com_google_android_gms_internal_firebase_abt_zza.zze();
                    continue;
                case 34:
                    this.zzat = com_google_android_gms_internal_firebase_abt_zza.readString();
                    continue;
                case 40:
                    this.zzau = com_google_android_gms_internal_firebase_abt_zza.zze();
                    continue;
                case 48:
                    this.zzav = com_google_android_gms_internal_firebase_abt_zza.zze();
                    continue;
                case 58:
                    this.zzaw = com_google_android_gms_internal_firebase_abt_zza.readString();
                    continue;
                case 66:
                    this.zzax = com_google_android_gms_internal_firebase_abt_zza.readString();
                    continue;
                case VoIPService.CALL_MAX_LAYER /*74*/:
                    this.zzay = com_google_android_gms_internal_firebase_abt_zza.readString();
                    continue;
                case 82:
                    this.zzaz = com_google_android_gms_internal_firebase_abt_zza.readString();
                    continue;
                case 90:
                    this.zzba = com_google_android_gms_internal_firebase_abt_zza.readString();
                    continue;
                case 96:
                    this.zzc = com_google_android_gms_internal_firebase_abt_zza.zzf();
                    continue;
                case 106:
                    int zzb = zzm.zzb(com_google_android_gms_internal_firebase_abt_zza, 106);
                    zzd = this.zzbb == null ? 0 : this.zzbb.length;
                    Object obj = new zzn[(zzb + zzd)];
                    if (zzd != 0) {
                        System.arraycopy(this.zzbb, 0, obj, 0, zzd);
                    }
                    while (zzd < obj.length - 1) {
                        obj[zzd] = new zzn();
                        com_google_android_gms_internal_firebase_abt_zza.zza(obj[zzd]);
                        com_google_android_gms_internal_firebase_abt_zza.zzd();
                        zzd++;
                    }
                    obj[zzd] = new zzn();
                    com_google_android_gms_internal_firebase_abt_zza.zza(obj[zzd]);
                    this.zzbb = obj;
                    continue;
                default:
                    if (!super.zza(com_google_android_gms_internal_firebase_abt_zza, zzd)) {
                        break;
                    }
                    continue;
            }
            return this;
        }
    }
}
