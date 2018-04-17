package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.api.Api.Client;
import java.util.ArrayList;

final class zzap extends zzat {
    private final /* synthetic */ zzaj zzhv;
    private final ArrayList<Client> zzib;

    public zzap(zzaj com_google_android_gms_common_api_internal_zzaj, ArrayList<Client> arrayList) {
        this.zzhv = com_google_android_gms_common_api_internal_zzaj;
        super(com_google_android_gms_common_api_internal_zzaj);
        this.zzib = arrayList;
    }

    public final void zzaq() {
        this.zzhv.zzhf.zzfq.zzim = this.zzhv.zzaw();
        ArrayList arrayList = this.zzib;
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            ((Client) obj).getRemoteService(this.zzhv.zzhr, this.zzhv.zzhf.zzfq.zzim);
        }
    }
}
