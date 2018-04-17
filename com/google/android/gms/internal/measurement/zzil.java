package com.google.android.gms.internal.measurement;

import android.content.ComponentName;
import android.content.Context;
import android.os.RemoteException;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.stats.ConnectionTracker;
import com.google.android.gms.common.util.Clock;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.telegram.messenger.exoplayer2.source.chunk.ChunkedTrackBlacklistUtil;

public final class zzil extends zzhk {
    private final zziz zzapr;
    private zzey zzaps;
    private volatile Boolean zzapt;
    private final zzem zzapu;
    private final zzjp zzapv;
    private final List<Runnable> zzapw = new ArrayList();
    private final zzem zzapx;

    protected zzil(zzgl com_google_android_gms_internal_measurement_zzgl) {
        super(com_google_android_gms_internal_measurement_zzgl);
        this.zzapv = new zzjp(com_google_android_gms_internal_measurement_zzgl.zzbt());
        this.zzapr = new zziz(this);
        this.zzapu = new zzim(this, com_google_android_gms_internal_measurement_zzgl);
        this.zzapx = new zzir(this, com_google_android_gms_internal_measurement_zzgl);
    }

    private final void onServiceDisconnected(ComponentName componentName) {
        zzab();
        if (this.zzaps != null) {
            this.zzaps = null;
            zzgg().zzir().zzg("Disconnected from device MeasurementService", componentName);
            zzab();
            zzdf();
        }
    }

    private final void zzcu() {
        zzab();
        this.zzapv.start();
        this.zzapu.zzh(((Long) zzew.zzahr.get()).longValue());
    }

    private final void zzcv() {
        zzab();
        if (isConnected()) {
            zzgg().zzir().log("Inactivity, disconnecting from the service");
            disconnect();
        }
    }

    private final void zzf(Runnable runnable) throws IllegalStateException {
        zzab();
        if (isConnected()) {
            runnable.run();
        } else if (((long) this.zzapw.size()) >= 1000) {
            zzgg().zzil().log("Discarding data. Max runnable queue size reached");
        } else {
            this.zzapw.add(runnable);
            this.zzapx.zzh(ChunkedTrackBlacklistUtil.DEFAULT_TRACK_BLACKLIST_MS);
            zzdf();
        }
    }

    private final void zzko() {
        zzab();
        zzgg().zzir().zzg("Processing queued up service tasks", Integer.valueOf(this.zzapw.size()));
        for (Runnable run : this.zzapw) {
            try {
                run.run();
            } catch (Throwable th) {
                zzgg().zzil().zzg("Task exception while flushing queue", th);
            }
        }
        this.zzapw.clear();
        this.zzapx.cancel();
    }

    private final zzec zzl(boolean z) {
        return zzfv().zzbd(z ? zzgg().zzit() : null);
    }

    public final void disconnect() {
        zzab();
        zzch();
        try {
            ConnectionTracker.getInstance().unbindService(getContext(), this.zzapr);
        } catch (IllegalStateException e) {
        }
        this.zzaps = null;
    }

    public final /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    public final boolean isConnected() {
        zzab();
        zzch();
        return this.zzaps != null;
    }

    protected final void resetAnalyticsData() {
        zzab();
        zzch();
        zzec zzl = zzl(false);
        zzfz().resetAnalyticsData();
        zzf(new zzin(this, zzl));
    }

    protected final void zza(zzey com_google_android_gms_internal_measurement_zzey) {
        zzab();
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzey);
        this.zzaps = com_google_android_gms_internal_measurement_zzey;
        zzcu();
        zzko();
    }

    final void zza(zzey com_google_android_gms_internal_measurement_zzey, AbstractSafeParcelable abstractSafeParcelable, zzec com_google_android_gms_internal_measurement_zzec) {
        zzfi zzil;
        String str;
        zzab();
        zzch();
        int i = 0;
        int i2 = 100;
        while (i < 1001 && r3 == 100) {
            int size;
            List arrayList = new ArrayList();
            Object zzp = zzfz().zzp(100);
            if (zzp != null) {
                arrayList.addAll(zzp);
                size = zzp.size();
            } else {
                size = 0;
            }
            if (abstractSafeParcelable != null && size < 100) {
                arrayList.add(abstractSafeParcelable);
            }
            ArrayList arrayList2 = (ArrayList) arrayList;
            int size2 = arrayList2.size();
            int i3 = 0;
            while (i3 < size2) {
                Object obj = arrayList2.get(i3);
                i3++;
                AbstractSafeParcelable abstractSafeParcelable2 = (AbstractSafeParcelable) obj;
                if (abstractSafeParcelable2 instanceof zzeu) {
                    try {
                        com_google_android_gms_internal_measurement_zzey.zza((zzeu) abstractSafeParcelable2, com_google_android_gms_internal_measurement_zzec);
                    } catch (RemoteException e) {
                        obj = e;
                        zzil = zzgg().zzil();
                        str = "Failed to send event to the service";
                        zzil.zzg(str, obj);
                    }
                } else if (abstractSafeParcelable2 instanceof zzjs) {
                    try {
                        com_google_android_gms_internal_measurement_zzey.zza((zzjs) abstractSafeParcelable2, com_google_android_gms_internal_measurement_zzec);
                    } catch (RemoteException e2) {
                        obj = e2;
                        zzil = zzgg().zzil();
                        str = "Failed to send attribute to the service";
                        zzil.zzg(str, obj);
                    }
                } else if (abstractSafeParcelable2 instanceof zzef) {
                    try {
                        com_google_android_gms_internal_measurement_zzey.zza((zzef) abstractSafeParcelable2, com_google_android_gms_internal_measurement_zzec);
                    } catch (RemoteException e3) {
                        obj = e3;
                        zzil = zzgg().zzil();
                        str = "Failed to send conditional property to the service";
                        zzil.zzg(str, obj);
                    }
                } else {
                    zzgg().zzil().log("Discarding data. Unrecognized parcel type.");
                }
            }
            i++;
            i2 = size;
        }
    }

    protected final void zza(zzig com_google_android_gms_internal_measurement_zzig) {
        zzab();
        zzch();
        zzf(new zziq(this, com_google_android_gms_internal_measurement_zzig));
    }

    public final void zza(AtomicReference<String> atomicReference) {
        zzab();
        zzch();
        zzf(new zzio(this, atomicReference, zzl(false)));
    }

    protected final void zza(AtomicReference<List<zzef>> atomicReference, String str, String str2, String str3) {
        zzab();
        zzch();
        zzf(new zziv(this, atomicReference, str, str2, str3, zzl(false)));
    }

    protected final void zza(AtomicReference<List<zzjs>> atomicReference, String str, String str2, String str3, boolean z) {
        zzab();
        zzch();
        zzf(new zziw(this, atomicReference, str, str2, str3, z, zzl(false)));
    }

    protected final void zza(AtomicReference<List<zzjs>> atomicReference, boolean z) {
        zzab();
        zzch();
        zzf(new zziy(this, atomicReference, zzl(false), z));
    }

    public final /* bridge */ /* synthetic */ void zzab() {
        super.zzab();
    }

    protected final void zzb(zzjs com_google_android_gms_internal_measurement_zzjs) {
        zzab();
        zzch();
        zzf(new zzix(this, zzfz().zza(com_google_android_gms_internal_measurement_zzjs), com_google_android_gms_internal_measurement_zzjs, zzl(true)));
    }

    public final /* bridge */ /* synthetic */ Clock zzbt() {
        return super.zzbt();
    }

    protected final void zzc(zzeu com_google_android_gms_internal_measurement_zzeu, String str) {
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzeu);
        zzab();
        zzch();
        zzf(new zzit(this, true, zzfz().zza(com_google_android_gms_internal_measurement_zzeu), com_google_android_gms_internal_measurement_zzeu, zzl(true), str));
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    final void zzdf() {
        /*
        r6 = this;
        r6.zzab();
        r6.zzch();
        r0 = r6.isConnected();
        if (r0 == 0) goto L_0x000d;
    L_0x000c:
        return;
    L_0x000d:
        r0 = r6.zzapt;
        r1 = 0;
        r2 = 1;
        if (r0 != 0) goto L_0x00fa;
    L_0x0013:
        r6.zzab();
        r6.zzch();
        r0 = r6.zzgh();
        r0 = r0.zzix();
        if (r0 == 0) goto L_0x002c;
    L_0x0023:
        r0 = r0.booleanValue();
        if (r0 == 0) goto L_0x002c;
    L_0x0029:
        r3 = r2;
        goto L_0x00f4;
    L_0x002c:
        r0 = r6.zzfv();
        r0 = r0.zzij();
        if (r0 != r2) goto L_0x003a;
    L_0x0036:
        r0 = r2;
    L_0x0037:
        r3 = r0;
        goto L_0x00eb;
    L_0x003a:
        r0 = r6.zzgg();
        r0 = r0.zzir();
        r3 = "Checking service availability";
        r0.log(r3);
        r0 = r6.zzgc();
        r3 = com.google.android.gms.common.GoogleApiAvailabilityLight.getInstance();
        r0 = r0.getContext();
        r4 = 12451; // 0x30a3 float:1.7448E-41 double:6.1516E-320;
        r0 = r3.isGooglePlayServicesAvailable(r0, r4);
        r3 = 9;
        if (r0 == r3) goto L_0x00e0;
    L_0x005d:
        r3 = 18;
        if (r0 == r3) goto L_0x00d5;
    L_0x0061:
        switch(r0) {
            case 0: goto L_0x00c6;
            case 1: goto L_0x00b6;
            case 2: goto L_0x0085;
            case 3: goto L_0x0077;
            default: goto L_0x0064;
        };
    L_0x0064:
        r3 = r6.zzgg();
        r3 = r3.zzin();
        r4 = "Unexpected service status";
        r0 = java.lang.Integer.valueOf(r0);
        r3.zzg(r4, r0);
    L_0x0075:
        r0 = r1;
        goto L_0x0037;
    L_0x0077:
        r0 = r6.zzgg();
        r0 = r0.zzin();
        r3 = "Service disabled";
    L_0x0081:
        r0.log(r3);
        goto L_0x0075;
    L_0x0085:
        r0 = r6.zzgg();
        r0 = r0.zziq();
        r3 = "Service container out of date";
        r0.log(r3);
        r0 = r6.zzgc();
        r0 = r0.zzkv();
        r3 = 12400; // 0x3070 float:1.7376E-41 double:6.1264E-320;
        if (r0 >= r3) goto L_0x009f;
    L_0x009e:
        goto L_0x00c3;
    L_0x009f:
        r0 = r6.zzgh();
        r0 = r0.zzix();
        if (r0 == 0) goto L_0x00b2;
    L_0x00a9:
        r0 = r0.booleanValue();
        if (r0 == 0) goto L_0x00b0;
    L_0x00af:
        goto L_0x00b2;
    L_0x00b0:
        r0 = r1;
        goto L_0x00b3;
    L_0x00b2:
        r0 = r2;
    L_0x00b3:
        r3 = r0;
        r0 = r1;
        goto L_0x00eb;
    L_0x00b6:
        r0 = r6.zzgg();
        r0 = r0.zzir();
        r3 = "Service missing";
        r0.log(r3);
    L_0x00c3:
        r3 = r1;
        r0 = r2;
        goto L_0x00eb;
    L_0x00c6:
        r0 = r6.zzgg();
        r0 = r0.zzir();
        r3 = "Service available";
    L_0x00d0:
        r0.log(r3);
        goto L_0x0036;
    L_0x00d5:
        r0 = r6.zzgg();
        r0 = r0.zzin();
        r3 = "Service updating";
        goto L_0x00d0;
    L_0x00e0:
        r0 = r6.zzgg();
        r0 = r0.zzin();
        r3 = "Service invalid";
        goto L_0x0081;
    L_0x00eb:
        if (r0 == 0) goto L_0x00f4;
    L_0x00ed:
        r0 = r6.zzgh();
        r0.zzf(r3);
    L_0x00f4:
        r0 = java.lang.Boolean.valueOf(r3);
        r6.zzapt = r0;
    L_0x00fa:
        r0 = r6.zzapt;
        r0 = r0.booleanValue();
        if (r0 == 0) goto L_0x0108;
    L_0x0102:
        r0 = r6.zzapr;
        r0.zzkp();
        return;
    L_0x0108:
        r0 = r6.getContext();
        r0 = r0.getPackageManager();
        r3 = new android.content.Intent;
        r3.<init>();
        r4 = r6.getContext();
        r5 = "com.google.android.gms.measurement.AppMeasurementService";
        r3 = r3.setClassName(r4, r5);
        r4 = 65536; // 0x10000 float:9.18355E-41 double:3.2379E-319;
        r0 = r0.queryIntentServices(r3, r4);
        if (r0 == 0) goto L_0x012e;
    L_0x0127:
        r0 = r0.size();
        if (r0 <= 0) goto L_0x012e;
    L_0x012d:
        r1 = r2;
    L_0x012e:
        if (r1 == 0) goto L_0x014b;
    L_0x0130:
        r0 = new android.content.Intent;
        r1 = "com.google.android.gms.measurement.START";
        r0.<init>(r1);
        r1 = new android.content.ComponentName;
        r2 = r6.getContext();
        r3 = "com.google.android.gms.measurement.AppMeasurementService";
        r1.<init>(r2, r3);
        r0.setComponent(r1);
        r1 = r6.zzapr;
        r1.zzc(r0);
        return;
    L_0x014b:
        r0 = r6.zzgg();
        r0 = r0.zzil();
        r1 = "Unable to use remote or local measurement implementation. Please register the AppMeasurementService service in the app manifest";
        r0.log(r1);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzil.zzdf():void");
    }

    protected final void zzf(zzef com_google_android_gms_internal_measurement_zzef) {
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzef);
        zzab();
        zzch();
        zzf(new zziu(this, true, zzfz().zzc(com_google_android_gms_internal_measurement_zzef), new zzef(com_google_android_gms_internal_measurement_zzef), zzl(true), com_google_android_gms_internal_measurement_zzef));
    }

    public final /* bridge */ /* synthetic */ void zzfq() {
        super.zzfq();
    }

    public final /* bridge */ /* synthetic */ void zzfr() {
        super.zzfr();
    }

    public final /* bridge */ /* synthetic */ zzdx zzfs() {
        return super.zzfs();
    }

    public final /* bridge */ /* synthetic */ zzee zzft() {
        return super.zzft();
    }

    public final /* bridge */ /* synthetic */ zzhm zzfu() {
        return super.zzfu();
    }

    public final /* bridge */ /* synthetic */ zzfb zzfv() {
        return super.zzfv();
    }

    public final /* bridge */ /* synthetic */ zzeo zzfw() {
        return super.zzfw();
    }

    public final /* bridge */ /* synthetic */ zzil zzfx() {
        return super.zzfx();
    }

    public final /* bridge */ /* synthetic */ zzih zzfy() {
        return super.zzfy();
    }

    public final /* bridge */ /* synthetic */ zzfc zzfz() {
        return super.zzfz();
    }

    public final /* bridge */ /* synthetic */ zzei zzga() {
        return super.zzga();
    }

    public final /* bridge */ /* synthetic */ zzfe zzgb() {
        return super.zzgb();
    }

    public final /* bridge */ /* synthetic */ zzjv zzgc() {
        return super.zzgc();
    }

    public final /* bridge */ /* synthetic */ zzgf zzgd() {
        return super.zzgd();
    }

    public final /* bridge */ /* synthetic */ zzjk zzge() {
        return super.zzge();
    }

    public final /* bridge */ /* synthetic */ zzgg zzgf() {
        return super.zzgf();
    }

    public final /* bridge */ /* synthetic */ zzfg zzgg() {
        return super.zzgg();
    }

    public final /* bridge */ /* synthetic */ zzfr zzgh() {
        return super.zzgh();
    }

    public final /* bridge */ /* synthetic */ zzeh zzgi() {
        return super.zzgi();
    }

    protected final boolean zzhh() {
        return false;
    }

    protected final void zzkj() {
        zzab();
        zzch();
        zzf(new zzip(this, zzl(true)));
    }

    protected final void zzkm() {
        zzab();
        zzch();
        zzf(new zzis(this, zzl(true)));
    }

    final Boolean zzkn() {
        return this.zzapt;
    }
}
