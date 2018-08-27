package com.google.firebase.iid;

import com.google.firebase.events.Event;
import com.google.firebase.events.EventHandler;

final /* synthetic */ class zzp implements EventHandler {
    private final zza zzbb;

    zzp(zza com_google_firebase_iid_FirebaseInstanceId_zza) {
        this.zzbb = com_google_firebase_iid_FirebaseInstanceId_zza;
    }

    public final void handle(Event event) {
        zza com_google_firebase_iid_FirebaseInstanceId_zza = this.zzbb;
        synchronized (com_google_firebase_iid_FirebaseInstanceId_zza) {
            if (com_google_firebase_iid_FirebaseInstanceId_zza.isEnabled()) {
                com_google_firebase_iid_FirebaseInstanceId_zza.zzba.zzf();
            }
        }
    }
}
