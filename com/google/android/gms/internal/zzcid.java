package com.google.android.gms.internal;

import android.content.ComponentName;
import android.content.Context;
import android.os.RemoteException;
import android.support.annotation.WorkerThread;
import android.support.v4.view.PointerIconCompat;
import com.google.android.gms.common.internal.zzbo;
import com.google.android.gms.common.stats.zza;
import com.google.android.gms.common.util.zze;
import com.google.android.gms.measurement.AppMeasurement.zzb;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.telegram.messenger.exoplayer2.source.chunk.ChunkedTrackBlacklistUtil;

public final class zzcid extends zzchj {
    private final zzciq zzbtT;
    private zzcfd zzbtU;
    private Boolean zzbtV;
    private final zzcer zzbtW;
    private final zzcjf zzbtX;
    private final List<Runnable> zzbtY = new ArrayList();
    private final zzcer zzbtZ;

    protected zzcid(zzcgl com_google_android_gms_internal_zzcgl) {
        super(com_google_android_gms_internal_zzcgl);
        this.zzbtX = new zzcjf(com_google_android_gms_internal_zzcgl.zzkq());
        this.zzbtT = new zzciq(this);
        this.zzbtW = new zzcie(this, com_google_android_gms_internal_zzcgl);
        this.zzbtZ = new zzcii(this, com_google_android_gms_internal_zzcgl);
    }

    @WorkerThread
    private final void onServiceDisconnected(ComponentName componentName) {
        super.zzjC();
        if (this.zzbtU != null) {
            this.zzbtU = null;
            super.zzwF().zzyD().zzj("Disconnected from device MeasurementService", componentName);
            super.zzjC();
            zzla();
        }
    }

    @WorkerThread
    private final void zzkP() {
        super.zzjC();
        this.zzbtX.start();
        this.zzbtW.zzs(zzcem.zzxB());
    }

    @WorkerThread
    private final void zzkQ() {
        super.zzjC();
        if (isConnected()) {
            super.zzwF().zzyD().log("Inactivity, disconnecting from the service");
            disconnect();
        }
    }

    @WorkerThread
    private final void zzm(Runnable runnable) throws IllegalStateException {
        super.zzjC();
        if (isConnected()) {
            runnable.run();
        } else if (((long) this.zzbtY.size()) >= zzcem.zzxJ()) {
            super.zzwF().zzyx().log("Discarding data. Max runnable queue size reached");
        } else {
            this.zzbtY.add(runnable);
            this.zzbtZ.zzs(ChunkedTrackBlacklistUtil.DEFAULT_TRACK_BLACKLIST_MS);
            zzla();
        }
    }

    @WorkerThread
    private final void zzzl() {
        super.zzjC();
        super.zzwF().zzyD().zzj("Processing queued up service tasks", Integer.valueOf(this.zzbtY.size()));
        for (Runnable run : this.zzbtY) {
            try {
                run.run();
            } catch (Throwable th) {
                super.zzwF().zzyx().zzj("Task exception while flushing queue", th);
            }
        }
        this.zzbtY.clear();
        this.zzbtZ.cancel();
    }

    @WorkerThread
    public final void disconnect() {
        super.zzjC();
        zzkD();
        try {
            zza.zzrU();
            super.getContext().unbindService(this.zzbtT);
        } catch (IllegalStateException e) {
        } catch (IllegalArgumentException e2) {
        }
        this.zzbtU = null;
    }

    public final /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    @WorkerThread
    public final boolean isConnected() {
        super.zzjC();
        zzkD();
        return this.zzbtU != null;
    }

    @WorkerThread
    protected final void zza(zzcfd com_google_android_gms_internal_zzcfd) {
        super.zzjC();
        zzbo.zzu(com_google_android_gms_internal_zzcfd);
        this.zzbtU = com_google_android_gms_internal_zzcfd;
        zzkP();
        zzzl();
    }

    @WorkerThread
    final void zza(zzcfd com_google_android_gms_internal_zzcfd, com.google.android.gms.common.internal.safeparcel.zza com_google_android_gms_common_internal_safeparcel_zza) {
        super.zzjC();
        super.zzwp();
        zzkD();
        zzcem.zzxE();
        List arrayList = new ArrayList();
        zzcem.zzxN();
        int i = 100;
        for (int i2 = 0; i2 < PointerIconCompat.TYPE_CONTEXT_MENU && r5 == 100; i2++) {
            Object zzbp = super.zzwy().zzbp(100);
            if (zzbp != null) {
                arrayList.addAll(zzbp);
                i = zzbp.size();
            } else {
                i = 0;
            }
            if (com_google_android_gms_common_internal_safeparcel_zza != null && r5 < 100) {
                arrayList.add(com_google_android_gms_common_internal_safeparcel_zza);
            }
            ArrayList arrayList2 = (ArrayList) arrayList;
            int size = arrayList2.size();
            int i3 = 0;
            while (i3 < size) {
                Object obj = arrayList2.get(i3);
                i3++;
                com.google.android.gms.common.internal.safeparcel.zza com_google_android_gms_common_internal_safeparcel_zza2 = (com.google.android.gms.common.internal.safeparcel.zza) obj;
                if (com_google_android_gms_common_internal_safeparcel_zza2 instanceof zzcez) {
                    try {
                        com_google_android_gms_internal_zzcfd.zza((zzcez) com_google_android_gms_common_internal_safeparcel_zza2, super.zzwu().zzdV(super.zzwF().zzyE()));
                    } catch (RemoteException e) {
                        super.zzwF().zzyx().zzj("Failed to send event to the service", e);
                    }
                } else if (com_google_android_gms_common_internal_safeparcel_zza2 instanceof zzcji) {
                    try {
                        com_google_android_gms_internal_zzcfd.zza((zzcji) com_google_android_gms_common_internal_safeparcel_zza2, super.zzwu().zzdV(super.zzwF().zzyE()));
                    } catch (RemoteException e2) {
                        super.zzwF().zzyx().zzj("Failed to send attribute to the service", e2);
                    }
                } else if (com_google_android_gms_common_internal_safeparcel_zza2 instanceof zzcek) {
                    try {
                        com_google_android_gms_internal_zzcfd.zza((zzcek) com_google_android_gms_common_internal_safeparcel_zza2, super.zzwu().zzdV(super.zzwF().zzyE()));
                    } catch (RemoteException e22) {
                        super.zzwF().zzyx().zzj("Failed to send conditional property to the service", e22);
                    }
                } else {
                    super.zzwF().zzyx().log("Discarding data. Unrecognized parcel type.");
                }
            }
        }
    }

    @WorkerThread
    protected final void zza(zzb com_google_android_gms_measurement_AppMeasurement_zzb) {
        super.zzjC();
        zzkD();
        zzm(new zzcih(this, com_google_android_gms_measurement_AppMeasurement_zzb));
    }

    @WorkerThread
    public final void zza(AtomicReference<String> atomicReference) {
        super.zzjC();
        zzkD();
        zzm(new zzcif(this, atomicReference));
    }

    @WorkerThread
    protected final void zza(AtomicReference<List<zzcek>> atomicReference, String str, String str2, String str3) {
        super.zzjC();
        zzkD();
        zzm(new zzcim(this, atomicReference, str, str2, str3));
    }

    @WorkerThread
    protected final void zza(AtomicReference<List<zzcji>> atomicReference, String str, String str2, String str3, boolean z) {
        super.zzjC();
        zzkD();
        zzm(new zzcin(this, atomicReference, str, str2, str3, z));
    }

    @WorkerThread
    protected final void zza(AtomicReference<List<zzcji>> atomicReference, boolean z) {
        super.zzjC();
        zzkD();
        zzm(new zzcip(this, atomicReference, z));
    }

    @WorkerThread
    protected final void zzb(zzcji com_google_android_gms_internal_zzcji) {
        super.zzjC();
        zzkD();
        zzcem.zzxE();
        zzm(new zzcio(this, super.zzwy().zza(com_google_android_gms_internal_zzcji), com_google_android_gms_internal_zzcji));
    }

    @WorkerThread
    protected final void zzc(zzcez com_google_android_gms_internal_zzcez, String str) {
        zzbo.zzu(com_google_android_gms_internal_zzcez);
        super.zzjC();
        zzkD();
        zzcem.zzxE();
        zzm(new zzcik(this, true, super.zzwy().zza(com_google_android_gms_internal_zzcez), com_google_android_gms_internal_zzcez, str));
    }

    @WorkerThread
    protected final void zzf(zzcek com_google_android_gms_internal_zzcek) {
        zzbo.zzu(com_google_android_gms_internal_zzcek);
        super.zzjC();
        zzkD();
        zzcem.zzxE();
        zzm(new zzcil(this, true, super.zzwy().zzc(com_google_android_gms_internal_zzcek), new zzcek(com_google_android_gms_internal_zzcek), com_google_android_gms_internal_zzcek));
    }

    public final /* bridge */ /* synthetic */ void zzjC() {
        super.zzjC();
    }

    protected final void zzjD() {
    }

    public final /* bridge */ /* synthetic */ zze zzkq() {
        return super.zzkq();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @android.support.annotation.WorkerThread
    final void zzla() {
        /*
        r6 = this;
        r2 = 0;
        r1 = 1;
        super.zzjC();
        r6.zzkD();
        r0 = r6.isConnected();
        if (r0 == 0) goto L_0x000f;
    L_0x000e:
        return;
    L_0x000f:
        r0 = r6.zzbtV;
        if (r0 != 0) goto L_0x0069;
    L_0x0013:
        r0 = super.zzwG();
        r0 = r0.zzyI();
        r6.zzbtV = r0;
        r0 = r6.zzbtV;
        if (r0 != 0) goto L_0x0069;
    L_0x0021:
        r0 = super.zzwF();
        r0 = r0.zzyD();
        r3 = "State of service unknown";
        r0.log(r3);
        super.zzjC();
        r6.zzkD();
        com.google.android.gms.internal.zzcem.zzxE();
        r0 = super.zzwF();
        r0 = r0.zzyD();
        r3 = "Checking service availability";
        r0.log(r3);
        r0 = com.google.android.gms.common.zze.zzoW();
        r3 = super.getContext();
        r0 = r0.isGooglePlayServicesAvailable(r3);
        switch(r0) {
            case 0: goto L_0x0085;
            case 1: goto L_0x0095;
            case 2: goto L_0x00b4;
            case 3: goto L_0x00c4;
            case 9: goto L_0x00d3;
            case 18: goto L_0x00a4;
            default: goto L_0x0055;
        };
    L_0x0055:
        r0 = r2;
    L_0x0056:
        r0 = java.lang.Boolean.valueOf(r0);
        r6.zzbtV = r0;
        r0 = super.zzwG();
        r3 = r6.zzbtV;
        r3 = r3.booleanValue();
        r0.zzak(r3);
    L_0x0069:
        r0 = r6.zzbtV;
        r0 = r0.booleanValue();
        if (r0 == 0) goto L_0x00e3;
    L_0x0071:
        r0 = super.zzwF();
        r0 = r0.zzyD();
        r1 = "Using measurement service";
        r0.log(r1);
        r0 = r6.zzbtT;
        r0.zzzm();
        goto L_0x000e;
    L_0x0085:
        r0 = super.zzwF();
        r0 = r0.zzyD();
        r3 = "Service available";
        r0.log(r3);
        r0 = r1;
        goto L_0x0056;
    L_0x0095:
        r0 = super.zzwF();
        r0 = r0.zzyD();
        r3 = "Service missing";
        r0.log(r3);
        goto L_0x0055;
    L_0x00a4:
        r0 = super.zzwF();
        r0 = r0.zzyz();
        r3 = "Service updating";
        r0.log(r3);
        r0 = r1;
        goto L_0x0056;
    L_0x00b4:
        r0 = super.zzwF();
        r0 = r0.zzyC();
        r3 = "Service container out of date";
        r0.log(r3);
        r0 = r1;
        goto L_0x0056;
    L_0x00c4:
        r0 = super.zzwF();
        r0 = r0.zzyz();
        r3 = "Service disabled";
        r0.log(r3);
        goto L_0x0055;
    L_0x00d3:
        r0 = super.zzwF();
        r0 = r0.zzyz();
        r3 = "Service invalid";
        r0.log(r3);
        goto L_0x0055;
    L_0x00e3:
        com.google.android.gms.internal.zzcem.zzxE();
        r0 = super.getContext();
        r0 = r0.getPackageManager();
        r3 = new android.content.Intent;
        r3.<init>();
        r4 = super.getContext();
        r5 = "com.google.android.gms.measurement.AppMeasurementService";
        r3 = r3.setClassName(r4, r5);
        r4 = 65536; // 0x10000 float:9.18355E-41 double:3.2379E-319;
        r0 = r0.queryIntentServices(r3, r4);
        if (r0 == 0) goto L_0x013d;
    L_0x0106:
        r0 = r0.size();
        if (r0 <= 0) goto L_0x013d;
    L_0x010c:
        if (r1 == 0) goto L_0x013f;
    L_0x010e:
        r0 = super.zzwF();
        r0 = r0.zzyD();
        r1 = "Using local app measurement service";
        r0.log(r1);
        r0 = new android.content.Intent;
        r1 = "com.google.android.gms.measurement.START";
        r0.<init>(r1);
        r1 = new android.content.ComponentName;
        r2 = super.getContext();
        com.google.android.gms.internal.zzcem.zzxE();
        r3 = "com.google.android.gms.measurement.AppMeasurementService";
        r1.<init>(r2, r3);
        r0.setComponent(r1);
        r1 = r6.zzbtT;
        r1.zzk(r0);
        goto L_0x000e;
    L_0x013d:
        r1 = r2;
        goto L_0x010c;
    L_0x013f:
        r0 = super.zzwF();
        r0 = r0.zzyx();
        r1 = "Unable to use remote or local measurement implementation. Please register the AppMeasurementService service in the app manifest";
        r0.log(r1);
        goto L_0x000e;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzcid.zzla():void");
    }

    public final /* bridge */ /* synthetic */ zzcfj zzwA() {
        return super.zzwA();
    }

    public final /* bridge */ /* synthetic */ zzcjl zzwB() {
        return super.zzwB();
    }

    public final /* bridge */ /* synthetic */ zzcgf zzwC() {
        return super.zzwC();
    }

    public final /* bridge */ /* synthetic */ zzcja zzwD() {
        return super.zzwD();
    }

    public final /* bridge */ /* synthetic */ zzcgg zzwE() {
        return super.zzwE();
    }

    public final /* bridge */ /* synthetic */ zzcfl zzwF() {
        return super.zzwF();
    }

    public final /* bridge */ /* synthetic */ zzcfw zzwG() {
        return super.zzwG();
    }

    public final /* bridge */ /* synthetic */ zzcem zzwH() {
        return super.zzwH();
    }

    public final /* bridge */ /* synthetic */ void zzwo() {
        super.zzwo();
    }

    public final /* bridge */ /* synthetic */ void zzwp() {
        super.zzwp();
    }

    public final /* bridge */ /* synthetic */ void zzwq() {
        super.zzwq();
    }

    public final /* bridge */ /* synthetic */ zzcec zzwr() {
        return super.zzwr();
    }

    public final /* bridge */ /* synthetic */ zzcej zzws() {
        return super.zzws();
    }

    public final /* bridge */ /* synthetic */ zzchl zzwt() {
        return super.zzwt();
    }

    public final /* bridge */ /* synthetic */ zzcfg zzwu() {
        return super.zzwu();
    }

    public final /* bridge */ /* synthetic */ zzcet zzwv() {
        return super.zzwv();
    }

    public final /* bridge */ /* synthetic */ zzcid zzww() {
        return super.zzww();
    }

    public final /* bridge */ /* synthetic */ zzchz zzwx() {
        return super.zzwx();
    }

    public final /* bridge */ /* synthetic */ zzcfh zzwy() {
        return super.zzwy();
    }

    public final /* bridge */ /* synthetic */ zzcen zzwz() {
        return super.zzwz();
    }

    @WorkerThread
    protected final void zzzj() {
        super.zzjC();
        zzkD();
        zzm(new zzcij(this));
    }

    @WorkerThread
    protected final void zzzk() {
        super.zzjC();
        zzkD();
        zzm(new zzcig(this));
    }
}
