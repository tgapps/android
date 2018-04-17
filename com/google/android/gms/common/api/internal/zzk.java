package com.google.android.gms.common.api.internal;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import java.util.concurrent.atomic.AtomicReference;

public abstract class zzk extends LifecycleCallback implements OnCancelListener {
    protected volatile boolean mStarted;
    protected final GoogleApiAvailability zzdg;
    protected final AtomicReference<zzl> zzer;
    private final Handler zzes;

    protected zzk(LifecycleFragment lifecycleFragment) {
        this(lifecycleFragment, GoogleApiAvailability.getInstance());
    }

    private zzk(LifecycleFragment lifecycleFragment, GoogleApiAvailability googleApiAvailability) {
        super(lifecycleFragment);
        this.zzer = new AtomicReference(null);
        this.zzes = new Handler(Looper.getMainLooper());
        this.zzdg = googleApiAvailability;
    }

    private static int zza(zzl com_google_android_gms_common_api_internal_zzl) {
        return com_google_android_gms_common_api_internal_zzl == null ? -1 : com_google_android_gms_common_api_internal_zzl.zzu();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void onActivityResult(int r4, int r5, android.content.Intent r6) {
        /*
        r3 = this;
        r0 = r3.zzer;
        r0 = r0.get();
        r0 = (com.google.android.gms.common.api.internal.zzl) r0;
        r1 = 1;
        r2 = 0;
        switch(r4) {
            case 1: goto L_0x002e;
            case 2: goto L_0x000e;
            default: goto L_0x000d;
        };
    L_0x000d:
        goto L_0x0053;
    L_0x000e:
        r4 = r3.zzdg;
        r5 = r3.getActivity();
        r4 = r4.isGooglePlayServicesAvailable(r5);
        if (r4 != 0) goto L_0x001b;
    L_0x001a:
        goto L_0x001c;
    L_0x001b:
        r1 = r2;
    L_0x001c:
        if (r0 != 0) goto L_0x001f;
    L_0x001e:
        return;
    L_0x001f:
        r5 = r0.getConnectionResult();
        r5 = r5.getErrorCode();
        r6 = 18;
        if (r5 != r6) goto L_0x0054;
    L_0x002b:
        if (r4 != r6) goto L_0x0054;
    L_0x002d:
        return;
    L_0x002e:
        r4 = -1;
        if (r5 != r4) goto L_0x0032;
    L_0x0031:
        goto L_0x0054;
    L_0x0032:
        if (r5 != 0) goto L_0x0053;
    L_0x0034:
        r4 = 13;
        if (r6 == 0) goto L_0x003e;
    L_0x0038:
        r5 = "<<ResolutionFailureErrorDetail>>";
        r4 = r6.getIntExtra(r5, r4);
    L_0x003e:
        r5 = new com.google.android.gms.common.api.internal.zzl;
        r6 = new com.google.android.gms.common.ConnectionResult;
        r1 = 0;
        r6.<init>(r4, r1);
        r4 = zza(r0);
        r5.<init>(r6, r4);
        r4 = r3.zzer;
        r4.set(r5);
        r0 = r5;
    L_0x0053:
        r1 = r2;
    L_0x0054:
        if (r1 == 0) goto L_0x005a;
    L_0x0056:
        r3.zzt();
        return;
    L_0x005a:
        if (r0 == 0) goto L_0x0067;
    L_0x005c:
        r4 = r0.getConnectionResult();
        r5 = r0.zzu();
        r3.zza(r4, r5);
    L_0x0067:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.api.internal.zzk.onActivityResult(int, int, android.content.Intent):void");
    }

    public void onCancel(DialogInterface dialogInterface) {
        zza(new ConnectionResult(13, null), zza((zzl) this.zzer.get()));
        zzt();
    }

    public final void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle != null) {
            this.zzer.set(bundle.getBoolean("resolving_error", false) ? new zzl(new ConnectionResult(bundle.getInt("failed_status"), (PendingIntent) bundle.getParcelable("failed_resolution")), bundle.getInt("failed_client_id", -1)) : null);
        }
    }

    public final void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        zzl com_google_android_gms_common_api_internal_zzl = (zzl) this.zzer.get();
        if (com_google_android_gms_common_api_internal_zzl != null) {
            bundle.putBoolean("resolving_error", true);
            bundle.putInt("failed_client_id", com_google_android_gms_common_api_internal_zzl.zzu());
            bundle.putInt("failed_status", com_google_android_gms_common_api_internal_zzl.getConnectionResult().getErrorCode());
            bundle.putParcelable("failed_resolution", com_google_android_gms_common_api_internal_zzl.getConnectionResult().getResolution());
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
        zzl com_google_android_gms_common_api_internal_zzl = new zzl(connectionResult, i);
        if (this.zzer.compareAndSet(null, com_google_android_gms_common_api_internal_zzl)) {
            this.zzes.post(new zzm(this, com_google_android_gms_common_api_internal_zzl));
        }
    }

    protected abstract void zzr();

    protected final void zzt() {
        this.zzer.set(null);
        zzr();
    }
}
