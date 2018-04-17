package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api.Client;
import com.google.android.gms.common.internal.BaseGmsClient.ConnectionProgressReportCallbacks;
import com.google.android.gms.common.internal.GoogleApiAvailabilityCache;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

final class zzam extends zzat {
    final /* synthetic */ zzaj zzhv;
    private final Map<Client, zzal> zzhx;

    public zzam(zzaj com_google_android_gms_common_api_internal_zzaj, Map<Client, zzal> map) {
        this.zzhv = com_google_android_gms_common_api_internal_zzaj;
        super(com_google_android_gms_common_api_internal_zzaj);
        this.zzhx = map;
    }

    public final void zzaq() {
        GoogleApiAvailabilityCache googleApiAvailabilityCache = new GoogleApiAvailabilityCache(this.zzhv.zzgk);
        List arrayList = new ArrayList();
        List arrayList2 = new ArrayList();
        for (Client client : this.zzhx.keySet()) {
            if (!client.requiresGooglePlayServices() || ((zzal) this.zzhx.get(client)).zzfo) {
                arrayList2.add(client);
            } else {
                arrayList.add(client);
            }
        }
        int i = -1;
        int i2 = 0;
        if (!arrayList.isEmpty()) {
            ArrayList arrayList3 = (ArrayList) arrayList;
            int size = arrayList3.size();
            while (i2 < size) {
                Object obj = arrayList3.get(i2);
                i2++;
                i = googleApiAvailabilityCache.getClientAvailability(this.zzhv.mContext, (Client) obj);
                if (i != 0) {
                    break;
                }
            }
        }
        ArrayList arrayList4 = (ArrayList) arrayList2;
        int size2 = arrayList4.size();
        while (i2 < size2) {
            obj = arrayList4.get(i2);
            i2++;
            i = googleApiAvailabilityCache.getClientAvailability(this.zzhv.mContext, (Client) obj);
            if (i == 0) {
                break;
            }
        }
        if (i != 0) {
            this.zzhv.zzhf.zza(new zzan(this, this.zzhv, new ConnectionResult(i, null)));
            return;
        }
        if (this.zzhv.zzhp) {
            this.zzhv.zzhn.connect();
        }
        for (Client client2 : this.zzhx.keySet()) {
            ConnectionProgressReportCallbacks connectionProgressReportCallbacks = (ConnectionProgressReportCallbacks) this.zzhx.get(client2);
            if (!client2.requiresGooglePlayServices() || googleApiAvailabilityCache.getClientAvailability(this.zzhv.mContext, client2) == 0) {
                client2.connect(connectionProgressReportCallbacks);
            } else {
                this.zzhv.zzhf.zza(new zzao(this, this.zzhv, connectionProgressReportCallbacks));
            }
        }
    }
}
