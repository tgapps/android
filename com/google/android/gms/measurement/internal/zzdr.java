package com.google.android.gms.measurement.internal;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.source.chunk.ChunkedTrackBlacklistUtil;
import com.google.android.gms.common.GoogleApiAvailabilityLight;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.stats.ConnectionTracker;
import com.google.android.gms.common.util.Clock;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public final class zzdr extends zzf {
    private final zzef zzarz;
    private zzag zzasa;
    private volatile Boolean zzasb;
    private final zzv zzasc;
    private final zzev zzasd;
    private final List<Runnable> zzase = new ArrayList();
    private final zzv zzasf;

    protected zzdr(zzbt com_google_android_gms_measurement_internal_zzbt) {
        super(com_google_android_gms_measurement_internal_zzbt);
        this.zzasd = new zzev(com_google_android_gms_measurement_internal_zzbt.zzbx());
        this.zzarz = new zzef(this);
        this.zzasc = new zzds(this, com_google_android_gms_measurement_internal_zzbt);
        this.zzasf = new zzdx(this, com_google_android_gms_measurement_internal_zzbt);
    }

    protected final boolean zzgt() {
        return false;
    }

    public final boolean isConnected() {
        zzaf();
        zzcl();
        return this.zzasa != null;
    }

    protected final void zzlc() {
        zzaf();
        zzcl();
        zzf(new zzdy(this, zzm(true)));
    }

    final void zza(zzag com_google_android_gms_measurement_internal_zzag, AbstractSafeParcelable abstractSafeParcelable, zzh com_google_android_gms_measurement_internal_zzh) {
        zzaf();
        zzgb();
        zzcl();
        boolean zzld = zzld();
        int i = 100;
        for (int i2 = 0; i2 < 1001 && r4 == 100; i2++) {
            Object zzr;
            ArrayList arrayList;
            int size;
            int i3;
            AbstractSafeParcelable abstractSafeParcelable2;
            List arrayList2 = new ArrayList();
            if (zzld) {
                zzr = zzgi().zzr(100);
                if (zzr != null) {
                    arrayList2.addAll(zzr);
                    i = zzr.size();
                    if (abstractSafeParcelable != null && r4 < 100) {
                        arrayList2.add(abstractSafeParcelable);
                    }
                    arrayList = (ArrayList) arrayList2;
                    size = arrayList.size();
                    i3 = 0;
                    while (i3 < size) {
                        zzr = arrayList.get(i3);
                        i3++;
                        abstractSafeParcelable2 = (AbstractSafeParcelable) zzr;
                        if (abstractSafeParcelable2 instanceof zzad) {
                            try {
                                com_google_android_gms_measurement_internal_zzag.zza((zzad) abstractSafeParcelable2, com_google_android_gms_measurement_internal_zzh);
                            } catch (RemoteException e) {
                                zzgo().zzjd().zzg("Failed to send event to the service", e);
                            }
                        } else if (abstractSafeParcelable2 instanceof zzfh) {
                            try {
                                com_google_android_gms_measurement_internal_zzag.zza((zzfh) abstractSafeParcelable2, com_google_android_gms_measurement_internal_zzh);
                            } catch (RemoteException e2) {
                                zzgo().zzjd().zzg("Failed to send attribute to the service", e2);
                            }
                        } else if (abstractSafeParcelable2 instanceof zzl) {
                            zzgo().zzjd().zzbx("Discarding data. Unrecognized parcel type.");
                        } else {
                            try {
                                com_google_android_gms_measurement_internal_zzag.zza((zzl) abstractSafeParcelable2, com_google_android_gms_measurement_internal_zzh);
                            } catch (RemoteException e22) {
                                zzgo().zzjd().zzg("Failed to send conditional property to the service", e22);
                            }
                        }
                    }
                }
            }
            i = 0;
            arrayList2.add(abstractSafeParcelable);
            arrayList = (ArrayList) arrayList2;
            size = arrayList.size();
            i3 = 0;
            while (i3 < size) {
                zzr = arrayList.get(i3);
                i3++;
                abstractSafeParcelable2 = (AbstractSafeParcelable) zzr;
                if (abstractSafeParcelable2 instanceof zzad) {
                    com_google_android_gms_measurement_internal_zzag.zza((zzad) abstractSafeParcelable2, com_google_android_gms_measurement_internal_zzh);
                } else if (abstractSafeParcelable2 instanceof zzfh) {
                    com_google_android_gms_measurement_internal_zzag.zza((zzfh) abstractSafeParcelable2, com_google_android_gms_measurement_internal_zzh);
                } else if (abstractSafeParcelable2 instanceof zzl) {
                    zzgo().zzjd().zzbx("Discarding data. Unrecognized parcel type.");
                } else {
                    com_google_android_gms_measurement_internal_zzag.zza((zzl) abstractSafeParcelable2, com_google_android_gms_measurement_internal_zzh);
                }
            }
        }
    }

    protected final void zzb(zzad com_google_android_gms_measurement_internal_zzad, String str) {
        Preconditions.checkNotNull(com_google_android_gms_measurement_internal_zzad);
        zzaf();
        zzcl();
        boolean zzld = zzld();
        boolean z = zzld && zzgi().zza(com_google_android_gms_measurement_internal_zzad);
        zzf(new zzdz(this, zzld, z, com_google_android_gms_measurement_internal_zzad, zzm(true), str));
    }

    protected final void zzd(zzl com_google_android_gms_measurement_internal_zzl) {
        Preconditions.checkNotNull(com_google_android_gms_measurement_internal_zzl);
        zzaf();
        zzcl();
        zzgr();
        zzf(new zzea(this, true, zzgi().zzc(com_google_android_gms_measurement_internal_zzl), new zzl(com_google_android_gms_measurement_internal_zzl), zzm(true), com_google_android_gms_measurement_internal_zzl));
    }

    protected final void zza(AtomicReference<List<zzl>> atomicReference, String str, String str2, String str3) {
        zzaf();
        zzcl();
        zzf(new zzeb(this, atomicReference, str, str2, str3, zzm(false)));
    }

    protected final void zza(AtomicReference<List<zzfh>> atomicReference, String str, String str2, String str3, boolean z) {
        zzaf();
        zzcl();
        zzf(new zzec(this, atomicReference, str, str2, str3, z, zzm(false)));
    }

    protected final void zzb(zzfh com_google_android_gms_measurement_internal_zzfh) {
        zzaf();
        zzcl();
        boolean z = zzld() && zzgi().zza(com_google_android_gms_measurement_internal_zzfh);
        zzf(new zzed(this, z, com_google_android_gms_measurement_internal_zzfh, zzm(true)));
    }

    protected final void zza(AtomicReference<List<zzfh>> atomicReference, boolean z) {
        zzaf();
        zzcl();
        zzf(new zzee(this, atomicReference, zzm(false), z));
    }

    private final boolean zzld() {
        zzgr();
        return true;
    }

    public final void zza(AtomicReference<String> atomicReference) {
        zzaf();
        zzcl();
        zzf(new zzdu(this, atomicReference, zzm(false)));
    }

    protected final void zzkz() {
        zzaf();
        zzcl();
        zzf(new zzdv(this, zzm(true)));
    }

    protected final void zzb(zzdn com_google_android_gms_measurement_internal_zzdn) {
        zzaf();
        zzcl();
        zzf(new zzdw(this, com_google_android_gms_measurement_internal_zzdn));
    }

    private final void zzcy() {
        zzaf();
        this.zzasd.start();
        this.zzasc.zzh(((Long) zzaf.zzakj.get()).longValue());
    }

    final void zzdj() {
        Object obj = 1;
        zzaf();
        zzcl();
        if (!isConnected()) {
            if (this.zzasb == null) {
                boolean z;
                zzaf();
                zzcl();
                Boolean zzju = zzgp().zzju();
                if (zzju == null || !zzju.booleanValue()) {
                    Object obj2;
                    zzgr();
                    if (zzgf().zzjb() != 1) {
                        zzgo().zzjl().zzbx("Checking service availability");
                        int isGooglePlayServicesAvailable = GoogleApiAvailabilityLight.getInstance().isGooglePlayServicesAvailable(zzgm().getContext(), 12451000);
                        int i;
                        switch (isGooglePlayServicesAvailable) {
                            case 0:
                                zzgo().zzjl().zzbx("Service available");
                                i = 1;
                                z = true;
                                break;
                            case 1:
                                zzgo().zzjl().zzbx("Service missing");
                                i = 1;
                                z = false;
                                break;
                            case 2:
                                zzgo().zzjk().zzbx("Service container out of date");
                                if (zzgm().zzme() >= 13000) {
                                    zzju = zzgp().zzju();
                                    if (zzju == null || zzju.booleanValue()) {
                                        z = true;
                                    } else {
                                        z = false;
                                    }
                                    obj2 = null;
                                    break;
                                }
                                i = 1;
                                z = false;
                                break;
                                break;
                            case 3:
                                zzgo().zzjg().zzbx("Service disabled");
                                obj2 = null;
                                z = false;
                                break;
                            case 9:
                                zzgo().zzjg().zzbx("Service invalid");
                                obj2 = null;
                                z = false;
                                break;
                            case 18:
                                zzgo().zzjg().zzbx("Service updating");
                                i = 1;
                                z = true;
                                break;
                            default:
                                zzgo().zzjg().zzg("Unexpected service status", Integer.valueOf(isGooglePlayServicesAvailable));
                                obj2 = null;
                                z = false;
                                break;
                        }
                    }
                    obj2 = 1;
                    z = true;
                    if (!z && zzgq().zzib()) {
                        zzgo().zzjd().zzbx("No way to upload. Consider using the full version of Analytics");
                        obj2 = null;
                    }
                    if (obj2 != null) {
                        zzgp().zzg(z);
                    }
                } else {
                    z = true;
                }
                this.zzasb = Boolean.valueOf(z);
            }
            if (this.zzasb.booleanValue()) {
                this.zzarz.zzlh();
            } else if (!zzgq().zzib()) {
                zzgr();
                List queryIntentServices = getContext().getPackageManager().queryIntentServices(new Intent().setClassName(getContext(), "com.google.android.gms.measurement.AppMeasurementService"), C.DEFAULT_BUFFER_SEGMENT_SIZE);
                if (queryIntentServices == null || queryIntentServices.size() <= 0) {
                    obj = null;
                }
                if (obj != null) {
                    Intent intent = new Intent("com.google.android.gms.measurement.START");
                    Context context = getContext();
                    zzgr();
                    intent.setComponent(new ComponentName(context, "com.google.android.gms.measurement.AppMeasurementService"));
                    this.zzarz.zzc(intent);
                    return;
                }
                zzgo().zzjd().zzbx("Unable to use remote or local measurement implementation. Please register the AppMeasurementService service in the app manifest");
            }
        }
    }

    final Boolean zzle() {
        return this.zzasb;
    }

    protected final void zza(zzag com_google_android_gms_measurement_internal_zzag) {
        zzaf();
        Preconditions.checkNotNull(com_google_android_gms_measurement_internal_zzag);
        this.zzasa = com_google_android_gms_measurement_internal_zzag;
        zzcy();
        zzlf();
    }

    public final void disconnect() {
        zzaf();
        zzcl();
        if (zzn.zzia()) {
            this.zzarz.zzlg();
        }
        try {
            ConnectionTracker.getInstance().unbindService(getContext(), this.zzarz);
        } catch (IllegalStateException e) {
        } catch (IllegalArgumentException e2) {
        }
        this.zzasa = null;
    }

    private final void onServiceDisconnected(ComponentName componentName) {
        zzaf();
        if (this.zzasa != null) {
            this.zzasa = null;
            zzgo().zzjl().zzg("Disconnected from device MeasurementService", componentName);
            zzaf();
            zzdj();
        }
    }

    private final void zzcz() {
        zzaf();
        if (isConnected()) {
            zzgo().zzjl().zzbx("Inactivity, disconnecting from the service");
            disconnect();
        }
    }

    private final void zzf(Runnable runnable) throws IllegalStateException {
        zzaf();
        if (isConnected()) {
            runnable.run();
        } else if (((long) this.zzase.size()) >= 1000) {
            zzgo().zzjd().zzbx("Discarding data. Max runnable queue size reached");
        } else {
            this.zzase.add(runnable);
            this.zzasf.zzh(ChunkedTrackBlacklistUtil.DEFAULT_TRACK_BLACKLIST_MS);
            zzdj();
        }
    }

    private final void zzlf() {
        zzaf();
        zzgo().zzjl().zzg("Processing queued up service tasks", Integer.valueOf(this.zzase.size()));
        for (Runnable run : this.zzase) {
            try {
                run.run();
            } catch (Exception e) {
                zzgo().zzjd().zzg("Task exception while flushing queue", e);
            }
        }
        this.zzase.clear();
        this.zzasf.cancel();
    }

    private final zzh zzm(boolean z) {
        zzgr();
        return zzgf().zzbr(z ? zzgo().zzjn() : null);
    }

    public final /* bridge */ /* synthetic */ void zzga() {
        super.zzga();
    }

    public final /* bridge */ /* synthetic */ void zzgb() {
        super.zzgb();
    }

    public final /* bridge */ /* synthetic */ void zzgc() {
        super.zzgc();
    }

    public final /* bridge */ /* synthetic */ void zzaf() {
        super.zzaf();
    }

    public final /* bridge */ /* synthetic */ zza zzgd() {
        return super.zzgd();
    }

    public final /* bridge */ /* synthetic */ zzcs zzge() {
        return super.zzge();
    }

    public final /* bridge */ /* synthetic */ zzaj zzgf() {
        return super.zzgf();
    }

    public final /* bridge */ /* synthetic */ zzdr zzgg() {
        return super.zzgg();
    }

    public final /* bridge */ /* synthetic */ zzdo zzgh() {
        return super.zzgh();
    }

    public final /* bridge */ /* synthetic */ zzal zzgi() {
        return super.zzgi();
    }

    public final /* bridge */ /* synthetic */ zzeq zzgj() {
        return super.zzgj();
    }

    public final /* bridge */ /* synthetic */ zzx zzgk() {
        return super.zzgk();
    }

    public final /* bridge */ /* synthetic */ Clock zzbx() {
        return super.zzbx();
    }

    public final /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    public final /* bridge */ /* synthetic */ zzan zzgl() {
        return super.zzgl();
    }

    public final /* bridge */ /* synthetic */ zzfk zzgm() {
        return super.zzgm();
    }

    public final /* bridge */ /* synthetic */ zzbo zzgn() {
        return super.zzgn();
    }

    public final /* bridge */ /* synthetic */ zzap zzgo() {
        return super.zzgo();
    }

    public final /* bridge */ /* synthetic */ zzba zzgp() {
        return super.zzgp();
    }

    public final /* bridge */ /* synthetic */ zzn zzgq() {
        return super.zzgq();
    }

    public final /* bridge */ /* synthetic */ zzk zzgr() {
        return super.zzgr();
    }
}
