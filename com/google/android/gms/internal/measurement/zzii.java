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

public final class zzii extends zzhh {
    private final zziw zzaox;
    private zzey zzaoy;
    private volatile Boolean zzaoz;
    private final zzem zzapa;
    private final zzjm zzapb;
    private final List<Runnable> zzapc = new ArrayList();
    private final zzem zzapd;

    protected zzii(zzgl com_google_android_gms_internal_measurement_zzgl) {
        super(com_google_android_gms_internal_measurement_zzgl);
        this.zzapb = new zzjm(com_google_android_gms_internal_measurement_zzgl.zzbt());
        this.zzaox = new zziw(this);
        this.zzapa = new zzij(this, com_google_android_gms_internal_measurement_zzgl);
        this.zzapd = new zzio(this, com_google_android_gms_internal_measurement_zzgl);
    }

    private final void onServiceDisconnected(ComponentName componentName) {
        zzab();
        if (this.zzaoy != null) {
            this.zzaoy = null;
            zzge().zzit().zzg("Disconnected from device MeasurementService", componentName);
            zzab();
            zzdf();
        }
    }

    private final void zzcu() {
        zzab();
        this.zzapb.start();
        this.zzapa.zzh(((Long) zzew.zzaho.get()).longValue());
    }

    private final void zzcv() {
        zzab();
        if (isConnected()) {
            zzge().zzit().log("Inactivity, disconnecting from the service");
            disconnect();
        }
    }

    private final void zzf(Runnable runnable) throws IllegalStateException {
        zzab();
        if (isConnected()) {
            runnable.run();
        } else if (((long) this.zzapc.size()) >= 1000) {
            zzge().zzim().log("Discarding data. Max runnable queue size reached");
        } else {
            this.zzapc.add(runnable);
            this.zzapd.zzh(ChunkedTrackBlacklistUtil.DEFAULT_TRACK_BLACKLIST_MS);
            zzdf();
        }
    }

    private final zzdz zzk(boolean z) {
        return zzfv().zzbi(z ? zzge().zziv() : null);
    }

    private final void zzkg() {
        zzab();
        zzge().zzit().zzg("Processing queued up service tasks", Integer.valueOf(this.zzapc.size()));
        for (Runnable run : this.zzapc) {
            try {
                run.run();
            } catch (Exception e) {
                zzge().zzim().zzg("Task exception while flushing queue", e);
            }
        }
        this.zzapc.clear();
        this.zzapd.cancel();
    }

    public final void disconnect() {
        zzab();
        zzch();
        try {
            ConnectionTracker.getInstance().unbindService(getContext(), this.zzaox);
        } catch (IllegalStateException e) {
        } catch (IllegalArgumentException e2) {
        }
        this.zzaoy = null;
    }

    public final /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    public final boolean isConnected() {
        zzab();
        zzch();
        return this.zzaoy != null;
    }

    protected final void resetAnalyticsData() {
        zzab();
        zzch();
        zzdz zzk = zzk(false);
        zzfz().resetAnalyticsData();
        zzf(new zzik(this, zzk));
    }

    protected final void zza(zzey com_google_android_gms_internal_measurement_zzey) {
        zzab();
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzey);
        this.zzaoy = com_google_android_gms_internal_measurement_zzey;
        zzcu();
        zzkg();
    }

    final void zza(zzey com_google_android_gms_internal_measurement_zzey, AbstractSafeParcelable abstractSafeParcelable, zzdz com_google_android_gms_internal_measurement_zzdz) {
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
                        com_google_android_gms_internal_measurement_zzey.zza((zzeu) abstractSafeParcelable2, com_google_android_gms_internal_measurement_zzdz);
                    } catch (RemoteException e) {
                        zzge().zzim().zzg("Failed to send event to the service", e);
                    }
                } else if (abstractSafeParcelable2 instanceof zzjx) {
                    try {
                        com_google_android_gms_internal_measurement_zzey.zza((zzjx) abstractSafeParcelable2, com_google_android_gms_internal_measurement_zzdz);
                    } catch (RemoteException e2) {
                        zzge().zzim().zzg("Failed to send attribute to the service", e2);
                    }
                } else if (abstractSafeParcelable2 instanceof zzed) {
                    try {
                        com_google_android_gms_internal_measurement_zzey.zza((zzed) abstractSafeParcelable2, com_google_android_gms_internal_measurement_zzdz);
                    } catch (RemoteException e22) {
                        zzge().zzim().zzg("Failed to send conditional property to the service", e22);
                    }
                } else {
                    zzge().zzim().log("Discarding data. Unrecognized parcel type.");
                }
            }
        }
    }

    public final void zza(AtomicReference<String> atomicReference) {
        zzab();
        zzch();
        zzf(new zzil(this, atomicReference, zzk(false)));
    }

    protected final void zza(AtomicReference<List<zzed>> atomicReference, String str, String str2, String str3) {
        zzab();
        zzch();
        zzf(new zzis(this, atomicReference, str, str2, str3, zzk(false)));
    }

    protected final void zza(AtomicReference<List<zzjx>> atomicReference, String str, String str2, String str3, boolean z) {
        zzab();
        zzch();
        zzf(new zzit(this, atomicReference, str, str2, str3, z, zzk(false)));
    }

    protected final void zza(AtomicReference<List<zzjx>> atomicReference, boolean z) {
        zzab();
        zzch();
        zzf(new zziv(this, atomicReference, zzk(false), z));
    }

    public final /* bridge */ /* synthetic */ void zzab() {
        super.zzab();
    }

    protected final void zzb(zzeu com_google_android_gms_internal_measurement_zzeu, String str) {
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzeu);
        zzab();
        zzch();
        zzf(new zziq(this, true, zzfz().zza(com_google_android_gms_internal_measurement_zzeu), com_google_android_gms_internal_measurement_zzeu, zzk(true), str));
    }

    protected final void zzb(zzie com_google_android_gms_internal_measurement_zzie) {
        zzab();
        zzch();
        zzf(new zzin(this, com_google_android_gms_internal_measurement_zzie));
    }

    protected final void zzb(zzjx com_google_android_gms_internal_measurement_zzjx) {
        zzab();
        zzch();
        zzf(new zziu(this, zzfz().zza(com_google_android_gms_internal_measurement_zzjx), com_google_android_gms_internal_measurement_zzjx, zzk(true)));
    }

    public final /* bridge */ /* synthetic */ Clock zzbt() {
        return super.zzbt();
    }

    protected final void zzd(zzed com_google_android_gms_internal_measurement_zzed) {
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzed);
        zzab();
        zzch();
        zzf(new zzir(this, true, zzfz().zzc(com_google_android_gms_internal_measurement_zzed), new zzed(com_google_android_gms_internal_measurement_zzed), zzk(true), com_google_android_gms_internal_measurement_zzed));
    }

    final void zzdf() {
        Object obj = 1;
        zzab();
        zzch();
        if (!isConnected()) {
            if (this.zzaoz == null) {
                boolean z;
                zzab();
                zzch();
                Boolean zzjb = zzgf().zzjb();
                if (zzjb == null || !zzjb.booleanValue()) {
                    Object obj2;
                    if (zzfv().zzik() != 1) {
                        zzge().zzit().log("Checking service availability");
                        int isGooglePlayServicesAvailable = GoogleApiAvailabilityLight.getInstance().isGooglePlayServicesAvailable(zzgb().getContext(), 12451);
                        int i;
                        switch (isGooglePlayServicesAvailable) {
                            case 0:
                                zzge().zzit().log("Service available");
                                i = 1;
                                z = true;
                                break;
                            case 1:
                                zzge().zzit().log("Service missing");
                                i = 1;
                                z = false;
                                break;
                            case 2:
                                zzge().zzis().log("Service container out of date");
                                if (zzgb().zzld() >= 12600) {
                                    zzjb = zzgf().zzjb();
                                    z = zzjb == null || zzjb.booleanValue();
                                    obj2 = null;
                                    break;
                                }
                                i = 1;
                                z = false;
                                break;
                            case 3:
                                zzge().zzip().log("Service disabled");
                                obj2 = null;
                                z = false;
                                break;
                            case 9:
                                zzge().zzip().log("Service invalid");
                                obj2 = null;
                                z = false;
                                break;
                            case 18:
                                zzge().zzip().log("Service updating");
                                i = 1;
                                z = true;
                                break;
                            default:
                                zzge().zzip().zzg("Unexpected service status", Integer.valueOf(isGooglePlayServicesAvailable));
                                obj2 = null;
                                z = false;
                                break;
                        }
                    }
                    obj2 = 1;
                    z = true;
                    if (obj2 != null) {
                        zzgf().zzf(z);
                    }
                } else {
                    z = true;
                }
                this.zzaoz = Boolean.valueOf(z);
            }
            if (this.zzaoz.booleanValue()) {
                this.zzaox.zzkh();
                return;
            }
            List queryIntentServices = getContext().getPackageManager().queryIntentServices(new Intent().setClassName(getContext(), "com.google.android.gms.measurement.AppMeasurementService"), C.DEFAULT_BUFFER_SEGMENT_SIZE);
            if (queryIntentServices == null || queryIntentServices.size() <= 0) {
                obj = null;
            }
            if (obj != null) {
                Intent intent = new Intent("com.google.android.gms.measurement.START");
                intent.setComponent(new ComponentName(getContext(), "com.google.android.gms.measurement.AppMeasurementService"));
                this.zzaox.zzc(intent);
                return;
            }
            zzge().zzim().log("Unable to use remote or local measurement implementation. Please register the AppMeasurementService service in the app manifest");
        }
    }

    public final /* bridge */ /* synthetic */ void zzfr() {
        super.zzfr();
    }

    public final /* bridge */ /* synthetic */ void zzfs() {
        super.zzfs();
    }

    public final /* bridge */ /* synthetic */ zzdu zzft() {
        return super.zzft();
    }

    public final /* bridge */ /* synthetic */ zzhk zzfu() {
        return super.zzfu();
    }

    public final /* bridge */ /* synthetic */ zzfb zzfv() {
        return super.zzfv();
    }

    public final /* bridge */ /* synthetic */ zzeo zzfw() {
        return super.zzfw();
    }

    public final /* bridge */ /* synthetic */ zzii zzfx() {
        return super.zzfx();
    }

    public final /* bridge */ /* synthetic */ zzif zzfy() {
        return super.zzfy();
    }

    public final /* bridge */ /* synthetic */ zzfc zzfz() {
        return super.zzfz();
    }

    public final /* bridge */ /* synthetic */ zzfe zzga() {
        return super.zzga();
    }

    public final /* bridge */ /* synthetic */ zzka zzgb() {
        return super.zzgb();
    }

    public final /* bridge */ /* synthetic */ zzjh zzgc() {
        return super.zzgc();
    }

    public final /* bridge */ /* synthetic */ zzgg zzgd() {
        return super.zzgd();
    }

    public final /* bridge */ /* synthetic */ zzfg zzge() {
        return super.zzge();
    }

    public final /* bridge */ /* synthetic */ zzfr zzgf() {
        return super.zzgf();
    }

    public final /* bridge */ /* synthetic */ zzef zzgg() {
        return super.zzgg();
    }

    protected final boolean zzhf() {
        return false;
    }

    protected final void zzkb() {
        zzab();
        zzch();
        zzf(new zzim(this, zzk(true)));
    }

    protected final void zzke() {
        zzab();
        zzch();
        zzf(new zzip(this, zzk(true)));
    }

    final Boolean zzkf() {
        return this.zzaoz;
    }
}
