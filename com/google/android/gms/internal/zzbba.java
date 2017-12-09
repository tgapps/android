package com.google.android.gms.internal;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import java.util.concurrent.atomic.AtomicReference;

public abstract class zzbba extends zzbds implements OnCancelListener {
    protected volatile boolean mStarted;
    protected final AtomicReference<zzbbb> zzaBN;
    private final Handler zzaBO;
    protected final GoogleApiAvailability zzaBd;

    protected zzbba(zzbdt com_google_android_gms_internal_zzbdt) {
        this(com_google_android_gms_internal_zzbdt, GoogleApiAvailability.getInstance());
    }

    private zzbba(zzbdt com_google_android_gms_internal_zzbdt, GoogleApiAvailability googleApiAvailability) {
        super(com_google_android_gms_internal_zzbdt);
        this.zzaBN = new AtomicReference(null);
        this.zzaBO = new Handler(Looper.getMainLooper());
        this.zzaBd = googleApiAvailability;
    }

    private static int zza(@Nullable zzbbb com_google_android_gms_internal_zzbbb) {
        return com_google_android_gms_internal_zzbbb == null ? -1 : com_google_android_gms_internal_zzbbb.zzpy();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void onActivityResult(int r7, int r8, android.content.Intent r9) {
        /*
        r6 = this;
        r5 = 18;
        r1 = 13;
        r2 = 1;
        r3 = 0;
        r0 = r6.zzaBN;
        r0 = r0.get();
        r0 = (com.google.android.gms.internal.zzbbb) r0;
        switch(r7) {
            case 1: goto L_0x0034;
            case 2: goto L_0x0018;
            default: goto L_0x0011;
        };
    L_0x0011:
        r1 = r3;
    L_0x0012:
        if (r1 == 0) goto L_0x005b;
    L_0x0014:
        r6.zzpx();
    L_0x0017:
        return;
    L_0x0018:
        r1 = r6.zzaBd;
        r4 = r6.getActivity();
        r4 = r1.isGooglePlayServicesAvailable(r4);
        if (r4 != 0) goto L_0x0069;
    L_0x0024:
        r1 = r2;
    L_0x0025:
        if (r0 == 0) goto L_0x0017;
    L_0x0027:
        r2 = r0.zzpz();
        r2 = r2.getErrorCode();
        if (r2 != r5) goto L_0x0012;
    L_0x0031:
        if (r4 != r5) goto L_0x0012;
    L_0x0033:
        goto L_0x0017;
    L_0x0034:
        r4 = -1;
        if (r8 != r4) goto L_0x0039;
    L_0x0037:
        r1 = r2;
        goto L_0x0012;
    L_0x0039:
        if (r8 != 0) goto L_0x0011;
    L_0x003b:
        if (r9 == 0) goto L_0x0044;
    L_0x003d:
        r2 = "<<ResolutionFailureErrorDetail>>";
        r1 = r9.getIntExtra(r2, r1);
    L_0x0044:
        r2 = new com.google.android.gms.internal.zzbbb;
        r4 = new com.google.android.gms.common.ConnectionResult;
        r5 = 0;
        r4.<init>(r1, r5);
        r0 = zza(r0);
        r2.<init>(r4, r0);
        r0 = r6.zzaBN;
        r0.set(r2);
        r0 = r2;
        r1 = r3;
        goto L_0x0012;
    L_0x005b:
        if (r0 == 0) goto L_0x0017;
    L_0x005d:
        r1 = r0.zzpz();
        r0 = r0.zzpy();
        r6.zza(r1, r0);
        goto L_0x0017;
    L_0x0069:
        r1 = r3;
        goto L_0x0025;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzbba.onActivityResult(int, int, android.content.Intent):void");
    }

    public void onCancel(DialogInterface dialogInterface) {
        zza(new ConnectionResult(13, null), zza((zzbbb) this.zzaBN.get()));
        zzpx();
    }

    public final void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle != null) {
            this.zzaBN.set(bundle.getBoolean("resolving_error", false) ? new zzbbb(new ConnectionResult(bundle.getInt("failed_status"), (PendingIntent) bundle.getParcelable("failed_resolution")), bundle.getInt("failed_client_id", -1)) : null);
        }
    }

    public final void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        zzbbb com_google_android_gms_internal_zzbbb = (zzbbb) this.zzaBN.get();
        if (com_google_android_gms_internal_zzbbb != null) {
            bundle.putBoolean("resolving_error", true);
            bundle.putInt("failed_client_id", com_google_android_gms_internal_zzbbb.zzpy());
            bundle.putInt("failed_status", com_google_android_gms_internal_zzbbb.zzpz().getErrorCode());
            bundle.putParcelable("failed_resolution", com_google_android_gms_internal_zzbbb.zzpz().getResolution());
        }
    }

    public void onStart() {
        super.onStart();
        this.mStarted = true;
    }

    public void onStop() {
        super.onStop();
        this.mStarted = false;
    }

    protected abstract void zza(ConnectionResult connectionResult, int i);

    public final void zzb(ConnectionResult connectionResult, int i) {
        zzbbb com_google_android_gms_internal_zzbbb = new zzbbb(connectionResult, i);
        if (this.zzaBN.compareAndSet(null, com_google_android_gms_internal_zzbbb)) {
            this.zzaBO.post(new zzbbc(this, com_google_android_gms_internal_zzbbb));
        }
    }

    protected abstract void zzps();

    protected final void zzpx() {
        this.zzaBN.set(null);
        zzps();
    }
}
