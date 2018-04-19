package com.google.android.gms.internal.measurement;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import com.google.android.gms.common.GoogleApiAvailabilityLight;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.stats.ConnectionTracker;
import com.google.android.gms.common.util.Clock;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.telegram.messenger.exoplayer2.C;
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
        } catch (IllegalArgumentException e2) {
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
        zzab();
        zzch();
        int i = 100;
        for (int i2 = 0; i2 < 1001 && r4 == 100; i2++) {
            List arrayList = new ArrayList();
            Object zzp = zzfz().zzp(100);
            if (zzp != null) {
                arrayList.addAll(zzp);
                i = zzp.size();
            } else {
                i = 0;
            }
            if (abstractSafeParcelable != null && r4 < 100) {
                arrayList.add(abstractSafeParcelable);
            }
            ArrayList arrayList2 = (ArrayList) arrayList;
            int size = arrayList2.size();
            int i3 = 0;
            while (i3 < size) {
                zzp = arrayList2.get(i3);
                i3++;
                AbstractSafeParcelable abstractSafeParcelable2 = (AbstractSafeParcelable) zzp;
                if (abstractSafeParcelable2 instanceof zzeu) {
                    try {
                        com_google_android_gms_internal_measurement_zzey.zza((zzeu) abstractSafeParcelable2, com_google_android_gms_internal_measurement_zzec);
                    } catch (RemoteException e) {
                        zzgg().zzil().zzg("Failed to send event to the service", e);
                    }
                } else if (abstractSafeParcelable2 instanceof zzjs) {
                    try {
                        com_google_android_gms_internal_measurement_zzey.zza((zzjs) abstractSafeParcelable2, com_google_android_gms_internal_measurement_zzec);
                    } catch (RemoteException e2) {
                        zzgg().zzil().zzg("Failed to send attribute to the service", e2);
                    }
                } else if (abstractSafeParcelable2 instanceof zzef) {
                    try {
                        com_google_android_gms_internal_measurement_zzey.zza((zzef) abstractSafeParcelable2, com_google_android_gms_internal_measurement_zzec);
                    } catch (RemoteException e22) {
                        zzgg().zzil().zzg("Failed to send conditional property to the service", e22);
                    }
                } else {
                    zzgg().zzil().log("Discarding data. Unrecognized parcel type.");
                }
            }
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

    final void zzdf() {
        Object obj = 1;
        zzab();
        zzch();
        if (!isConnected()) {
            if (this.zzapt == null) {
                boolean z;
                zzab();
                zzch();
                Boolean zzix = zzgh().zzix();
                if (zzix == null || !zzix.booleanValue()) {
                    Object obj2;
                    if (zzfv().zzij() != 1) {
                        zzgg().zzir().log("Checking service availability");
                        int isGooglePlayServicesAvailable = GoogleApiAvailabilityLight.getInstance().isGooglePlayServicesAvailable(zzgc().getContext(), 12451);
                        int i;
                        switch (isGooglePlayServicesAvailable) {
                            case 0:
                                zzgg().zzir().log("Service available");
                                i = 1;
                                z = true;
                                break;
                            case 1:
                                zzgg().zzir().log("Service missing");
                                i = 1;
                                z = false;
                                break;
                            case 2:
                                zzgg().zziq().log("Service container out of date");
                                if (zzgc().zzkv() >= 12400) {
                                    zzix = zzgh().zzix();
                                    z = zzix == null || zzix.booleanValue();
                                    obj2 = null;
                                    break;
                                }
                                i = 1;
                                z = false;
                                break;
                            case 3:
                                zzgg().zzin().log("Service disabled");
                                obj2 = null;
                                z = false;
                                break;
                            case 9:
                                zzgg().zzin().log("Service invalid");
                                obj2 = null;
                                z = false;
                                break;
                            case 18:
                                zzgg().zzin().log("Service updating");
                                i = 1;
                                z = true;
                                break;
                            default:
                                zzgg().zzin().zzg("Unexpected service status", Integer.valueOf(isGooglePlayServicesAvailable));
                                obj2 = null;
                                z = false;
                                break;
                        }
                    }
                    obj2 = 1;
                    z = true;
                    if (obj2 != null) {
                        zzgh().zzf(z);
                    }
                } else {
                    z = true;
                }
                this.zzapt = Boolean.valueOf(z);
            }
            if (this.zzapt.booleanValue()) {
                this.zzapr.zzkp();
                return;
            }
            List queryIntentServices = getContext().getPackageManager().queryIntentServices(new Intent().setClassName(getContext(), "com.google.android.gms.measurement.AppMeasurementService"), C.DEFAULT_BUFFER_SEGMENT_SIZE);
            if (queryIntentServices == null || queryIntentServices.size() <= 0) {
                obj = null;
            }
            if (obj != null) {
                Intent intent = new Intent("com.google.android.gms.measurement.START");
                intent.setComponent(new ComponentName(getContext(), "com.google.android.gms.measurement.AppMeasurementService"));
                this.zzapr.zzc(intent);
                return;
            }
            zzgg().zzil().log("Unable to use remote or local measurement implementation. Please register the AppMeasurementService service in the app manifest");
        }
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
