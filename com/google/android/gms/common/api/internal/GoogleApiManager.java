package com.google.android.gms.common.api.internal;

import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.support.v4.util.ArrayMap;
import android.support.v4.util.ArraySet;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Feature;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.AnyClient;
import com.google.android.gms.common.api.Api.ApiOptions;
import com.google.android.gms.common.api.Api.Client;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.UnsupportedApiCallException;
import com.google.android.gms.common.api.internal.BaseImplementation.ApiMethodImpl;
import com.google.android.gms.common.api.internal.ListenerHolder.ListenerKey;
import com.google.android.gms.common.internal.BaseGmsClient.ConnectionProgressReportCallbacks;
import com.google.android.gms.common.internal.GoogleApiAvailabilityCache;
import com.google.android.gms.common.internal.IAccountAccessor;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.SimpleClientAdapter;
import com.google.android.gms.common.util.ArrayUtils;
import com.google.android.gms.common.util.PlatformVersion;
import com.google.android.gms.signin.SignInClient;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.concurrent.GuardedBy;
import org.telegram.messenger.exoplayer2.DefaultRenderersFactory;

public class GoogleApiManager implements Callback {
    private static final Object lock = new Object();
    public static final Status zzjj = new Status(4, "Sign-out occurred while this API call was in progress.");
    private static final Status zzjk = new Status(4, "The user must be signed in to make this API call.");
    @GuardedBy("lock")
    private static GoogleApiManager zzjo;
    private final Handler handler;
    private long zzjl = DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS;
    private long zzjm = 120000;
    private long zzjn = 10000;
    private final Context zzjp;
    private final GoogleApiAvailability zzjq;
    private final GoogleApiAvailabilityCache zzjr;
    private final AtomicInteger zzjs = new AtomicInteger(1);
    private final AtomicInteger zzjt = new AtomicInteger(0);
    private final Map<zzh<?>, zza<?>> zzju = new ConcurrentHashMap(5, 0.75f, 1);
    @GuardedBy("lock")
    private zzad zzjv = null;
    @GuardedBy("lock")
    private final Set<zzh<?>> zzjw = new ArraySet();
    private final Set<zzh<?>> zzjx = new ArraySet();

    private static class zzb {
        private final Feature zzdr;
        private final zzh<?> zzkn;

        private zzb(zzh<?> com_google_android_gms_common_api_internal_zzh_, Feature feature) {
            this.zzkn = com_google_android_gms_common_api_internal_zzh_;
            this.zzdr = feature;
        }

        public final boolean equals(Object obj) {
            if (obj != null && (obj instanceof zzb)) {
                zzb com_google_android_gms_common_api_internal_GoogleApiManager_zzb = (zzb) obj;
                if (Objects.equal(this.zzkn, com_google_android_gms_common_api_internal_GoogleApiManager_zzb.zzkn) && Objects.equal(this.zzdr, com_google_android_gms_common_api_internal_GoogleApiManager_zzb.zzdr)) {
                    return true;
                }
            }
            return false;
        }

        public final int hashCode() {
            return Objects.hashCode(this.zzkn, this.zzdr);
        }

        public final String toString() {
            return Objects.toStringHelper(this).add("key", this.zzkn).add("feature", this.zzdr).toString();
        }
    }

    private class zzc implements zzcb, ConnectionProgressReportCallbacks {
        private final zzh<?> zzhc;
        final /* synthetic */ GoogleApiManager zzjy;
        private final Client zzka;
        private IAccountAccessor zzko = null;
        private Set<Scope> zzkp = null;
        private boolean zzkq = false;

        public zzc(GoogleApiManager googleApiManager, Client client, zzh<?> com_google_android_gms_common_api_internal_zzh_) {
            this.zzjy = googleApiManager;
            this.zzka = client;
            this.zzhc = com_google_android_gms_common_api_internal_zzh_;
        }

        private final void zzbu() {
            if (this.zzkq && this.zzko != null) {
                this.zzka.getRemoteService(this.zzko, this.zzkp);
            }
        }

        public final void onReportServiceBinding(ConnectionResult connectionResult) {
            this.zzjy.handler.post(new zzbn(this, connectionResult));
        }

        public final void zza(IAccountAccessor iAccountAccessor, Set<Scope> set) {
            if (iAccountAccessor != null) {
                if (set != null) {
                    this.zzko = iAccountAccessor;
                    this.zzkp = set;
                    zzbu();
                    return;
                }
            }
            Log.wtf("GoogleApiManager", "Received null response from onSignInSuccess", new Exception());
            zzg(new ConnectionResult(4));
        }

        public final void zzg(ConnectionResult connectionResult) {
            ((zza) this.zzjy.zzju.get(this.zzhc)).zzg(connectionResult);
        }
    }

    public class zza<O extends ApiOptions> implements ConnectionCallbacks, OnConnectionFailedListener, zzq {
        private final zzh<O> zzhc;
        final /* synthetic */ GoogleApiManager zzjy;
        private final Queue<zzb> zzjz = new LinkedList();
        private final Client zzka;
        private final AnyClient zzkb;
        private final zzaa zzkc;
        private final Set<zzj> zzkd = new HashSet();
        private final Map<ListenerKey<?>, zzbv> zzke = new HashMap();
        private final int zzkf;
        private final zzby zzkg;
        private boolean zzkh;
        private final List<zzb> zzki = new ArrayList();
        private ConnectionResult zzkj = null;

        public zza(GoogleApiManager googleApiManager, GoogleApi<O> googleApi) {
            this.zzjy = googleApiManager;
            this.zzka = googleApi.zza(googleApiManager.handler.getLooper(), this);
            this.zzkb = this.zzka instanceof SimpleClientAdapter ? ((SimpleClientAdapter) this.zzka).getClient() : this.zzka;
            this.zzhc = googleApi.zzm();
            this.zzkc = new zzaa();
            this.zzkf = googleApi.getInstanceId();
            if (this.zzka.requiresSignIn()) {
                this.zzkg = googleApi.zza(googleApiManager.zzjp, googleApiManager.handler);
            } else {
                this.zzkg = null;
            }
        }

        private final void zza(zzb com_google_android_gms_common_api_internal_GoogleApiManager_zzb) {
            if (this.zzki.contains(com_google_android_gms_common_api_internal_GoogleApiManager_zzb) && !this.zzkh) {
                if (this.zzka.isConnected()) {
                    zzbl();
                } else {
                    connect();
                }
            }
        }

        private final void zzb(zzb com_google_android_gms_common_api_internal_GoogleApiManager_zzb) {
            if (this.zzki.remove(com_google_android_gms_common_api_internal_GoogleApiManager_zzb)) {
                this.zzjy.handler.removeMessages(15, com_google_android_gms_common_api_internal_GoogleApiManager_zzb);
                this.zzjy.handler.removeMessages(16, com_google_android_gms_common_api_internal_GoogleApiManager_zzb);
                Feature zzd = com_google_android_gms_common_api_internal_GoogleApiManager_zzb.zzdr;
                ArrayList arrayList = new ArrayList(this.zzjz.size());
                for (zzb com_google_android_gms_common_api_internal_zzb : this.zzjz) {
                    if (com_google_android_gms_common_api_internal_zzb instanceof zzf) {
                        Feature[] requiredFeatures = ((zzf) com_google_android_gms_common_api_internal_zzb).getRequiredFeatures();
                        if (requiredFeatures != null && ArrayUtils.contains(requiredFeatures, zzd)) {
                            arrayList.add(com_google_android_gms_common_api_internal_zzb);
                        }
                    }
                }
                arrayList = arrayList;
                int size = arrayList.size();
                int i = 0;
                while (i < size) {
                    Object obj = arrayList.get(i);
                    i++;
                    zzb com_google_android_gms_common_api_internal_zzb2 = (zzb) obj;
                    this.zzjz.remove(com_google_android_gms_common_api_internal_zzb2);
                    com_google_android_gms_common_api_internal_zzb2.zza(new UnsupportedApiCallException(zzd));
                }
            }
        }

        private final boolean zzb(zzb com_google_android_gms_common_api_internal_zzb) {
            if (com_google_android_gms_common_api_internal_zzb instanceof zzf) {
                zzf com_google_android_gms_common_api_internal_zzf = (zzf) com_google_android_gms_common_api_internal_zzb;
                Feature[] requiredFeatures = com_google_android_gms_common_api_internal_zzf.getRequiredFeatures();
                if (requiredFeatures != null) {
                    if (requiredFeatures.length != 0) {
                        Feature[] availableFeatures = this.zzka.getAvailableFeatures();
                        if (availableFeatures == null) {
                            availableFeatures = new Feature[0];
                        }
                        Map arrayMap = new ArrayMap(availableFeatures.length);
                        for (Feature feature : availableFeatures) {
                            arrayMap.put(feature.getName(), Long.valueOf(feature.getVersion()));
                        }
                        int length = requiredFeatures.length;
                        int i = 0;
                        while (i < length) {
                            Feature feature2 = requiredFeatures[i];
                            if (arrayMap.containsKey(feature2.getName())) {
                                if (((Long) arrayMap.get(feature2.getName())).longValue() >= feature2.getVersion()) {
                                    this.zzki.remove(new zzb(this.zzhc, feature2));
                                    i++;
                                }
                            }
                            if (com_google_android_gms_common_api_internal_zzf.shouldAutoResolveMissingFeatures()) {
                                zzb com_google_android_gms_common_api_internal_GoogleApiManager_zzb = new zzb(this.zzhc, feature2);
                                int indexOf = this.zzki.indexOf(com_google_android_gms_common_api_internal_GoogleApiManager_zzb);
                                if (indexOf >= 0) {
                                    com_google_android_gms_common_api_internal_GoogleApiManager_zzb = (zzb) this.zzki.get(indexOf);
                                    this.zzjy.handler.removeMessages(15, com_google_android_gms_common_api_internal_GoogleApiManager_zzb);
                                    this.zzjy.handler.sendMessageDelayed(Message.obtain(this.zzjy.handler, 15, com_google_android_gms_common_api_internal_GoogleApiManager_zzb), this.zzjy.zzjl);
                                    return false;
                                }
                                this.zzki.add(com_google_android_gms_common_api_internal_GoogleApiManager_zzb);
                                this.zzjy.handler.sendMessageDelayed(Message.obtain(this.zzjy.handler, 15, com_google_android_gms_common_api_internal_GoogleApiManager_zzb), this.zzjy.zzjl);
                                this.zzjy.handler.sendMessageDelayed(Message.obtain(this.zzjy.handler, 16, com_google_android_gms_common_api_internal_GoogleApiManager_zzb), this.zzjy.zzjm);
                                ConnectionResult connectionResult = new ConnectionResult(2, null);
                                if (!zzh(connectionResult)) {
                                    this.zzjy.zzc(connectionResult, this.zzkf);
                                    return false;
                                }
                            }
                            com_google_android_gms_common_api_internal_zzf.zza(new UnsupportedApiCallException(feature2));
                            return false;
                        }
                        zzc(com_google_android_gms_common_api_internal_zzb);
                        return true;
                    }
                }
                zzc(com_google_android_gms_common_api_internal_zzb);
                return true;
            }
            zzc(com_google_android_gms_common_api_internal_zzb);
            return true;
        }

        private final boolean zzb(boolean z) {
            Preconditions.checkHandlerThread(this.zzjy.handler);
            if (!this.zzka.isConnected() || this.zzke.size() != 0) {
                return false;
            }
            if (this.zzkc.zzaj()) {
                if (z) {
                    zzbr();
                }
                return false;
            }
            this.zzka.disconnect();
            return true;
        }

        private final void zzbj() {
            zzbo();
            zzi(ConnectionResult.RESULT_SUCCESS);
            zzbq();
            for (zzbv com_google_android_gms_common_api_internal_zzbv : this.zzke.values()) {
                try {
                    com_google_android_gms_common_api_internal_zzbv.zzlt.registerListener(this.zzkb, new TaskCompletionSource());
                } catch (DeadObjectException e) {
                    onConnectionSuspended(1);
                    this.zzka.disconnect();
                } catch (RemoteException e2) {
                }
            }
            zzbl();
            zzbr();
        }

        private final void zzbk() {
            zzbo();
            this.zzkh = true;
            this.zzkc.zzal();
            this.zzjy.handler.sendMessageDelayed(Message.obtain(this.zzjy.handler, 9, this.zzhc), this.zzjy.zzjl);
            this.zzjy.handler.sendMessageDelayed(Message.obtain(this.zzjy.handler, 11, this.zzhc), this.zzjy.zzjm);
            this.zzjy.zzjr.flush();
        }

        private final void zzbl() {
            ArrayList arrayList = new ArrayList(this.zzjz);
            int size = arrayList.size();
            int i = 0;
            while (i < size) {
                Object obj = arrayList.get(i);
                i++;
                zzb com_google_android_gms_common_api_internal_zzb = (zzb) obj;
                if (!this.zzka.isConnected()) {
                    return;
                }
                if (zzb(com_google_android_gms_common_api_internal_zzb)) {
                    this.zzjz.remove(com_google_android_gms_common_api_internal_zzb);
                }
            }
        }

        private final void zzbq() {
            if (this.zzkh) {
                this.zzjy.handler.removeMessages(11, this.zzhc);
                this.zzjy.handler.removeMessages(9, this.zzhc);
                this.zzkh = false;
            }
        }

        private final void zzbr() {
            this.zzjy.handler.removeMessages(12, this.zzhc);
            this.zzjy.handler.sendMessageDelayed(this.zzjy.handler.obtainMessage(12, this.zzhc), this.zzjy.zzjn);
        }

        private final void zzc(zzb com_google_android_gms_common_api_internal_zzb) {
            com_google_android_gms_common_api_internal_zzb.zza(this.zzkc, requiresSignIn());
            try {
                com_google_android_gms_common_api_internal_zzb.zza(this);
            } catch (DeadObjectException e) {
                onConnectionSuspended(1);
                this.zzka.disconnect();
            }
        }

        private final boolean zzh(ConnectionResult connectionResult) {
            synchronized (GoogleApiManager.lock) {
                if (this.zzjy.zzjv == null || !this.zzjy.zzjw.contains(this.zzhc)) {
                    return false;
                }
                this.zzjy.zzjv.zzb(connectionResult, this.zzkf);
                return true;
            }
        }

        private final void zzi(ConnectionResult connectionResult) {
            for (zzj com_google_android_gms_common_api_internal_zzj : this.zzkd) {
                String str = null;
                if (Objects.equal(connectionResult, ConnectionResult.RESULT_SUCCESS)) {
                    str = this.zzka.getEndpointPackageName();
                }
                com_google_android_gms_common_api_internal_zzj.zza(this.zzhc, connectionResult, str);
            }
            this.zzkd.clear();
        }

        public final void connect() {
            Preconditions.checkHandlerThread(this.zzjy.handler);
            if (!this.zzka.isConnected() && !this.zzka.isConnecting()) {
                int clientAvailability = this.zzjy.zzjr.getClientAvailability(this.zzjy.zzjp, this.zzka);
                if (clientAvailability != 0) {
                    onConnectionFailed(new ConnectionResult(clientAvailability, null));
                    return;
                }
                zzcb com_google_android_gms_common_api_internal_GoogleApiManager_zzc = new zzc(this.zzjy, this.zzka, this.zzhc);
                if (this.zzka.requiresSignIn()) {
                    this.zzkg.zza(com_google_android_gms_common_api_internal_GoogleApiManager_zzc);
                }
                this.zzka.connect(com_google_android_gms_common_api_internal_GoogleApiManager_zzc);
            }
        }

        public final int getInstanceId() {
            return this.zzkf;
        }

        final boolean isConnected() {
            return this.zzka.isConnected();
        }

        public final void onConnected(Bundle bundle) {
            if (Looper.myLooper() == this.zzjy.handler.getLooper()) {
                zzbj();
            } else {
                this.zzjy.handler.post(new zzbi(this));
            }
        }

        public final void onConnectionFailed(ConnectionResult connectionResult) {
            Preconditions.checkHandlerThread(this.zzjy.handler);
            if (this.zzkg != null) {
                this.zzkg.zzbz();
            }
            zzbo();
            this.zzjy.zzjr.flush();
            zzi(connectionResult);
            if (connectionResult.getErrorCode() == 4) {
                zzc(GoogleApiManager.zzjk);
            } else if (this.zzjz.isEmpty()) {
                this.zzkj = connectionResult;
            } else {
                if (!(zzh(connectionResult) || this.zzjy.zzc(connectionResult, this.zzkf))) {
                    if (connectionResult.getErrorCode() == 18) {
                        this.zzkh = true;
                    }
                    if (this.zzkh) {
                        this.zzjy.handler.sendMessageDelayed(Message.obtain(this.zzjy.handler, 9, this.zzhc), this.zzjy.zzjl);
                        return;
                    }
                    String zzq = this.zzhc.zzq();
                    StringBuilder stringBuilder = new StringBuilder(38 + String.valueOf(zzq).length());
                    stringBuilder.append("API: ");
                    stringBuilder.append(zzq);
                    stringBuilder.append(" is not available on this device.");
                    zzc(new Status(17, stringBuilder.toString()));
                }
            }
        }

        public final void onConnectionSuspended(int i) {
            if (Looper.myLooper() == this.zzjy.handler.getLooper()) {
                zzbk();
            } else {
                this.zzjy.handler.post(new zzbj(this));
            }
        }

        public final boolean requiresSignIn() {
            return this.zzka.requiresSignIn();
        }

        public final void resume() {
            Preconditions.checkHandlerThread(this.zzjy.handler);
            if (this.zzkh) {
                connect();
            }
        }

        public final void zza(ConnectionResult connectionResult, Api<?> api, boolean z) {
            if (Looper.myLooper() == this.zzjy.handler.getLooper()) {
                onConnectionFailed(connectionResult);
            } else {
                this.zzjy.handler.post(new zzbk(this, connectionResult));
            }
        }

        public final void zza(zzb com_google_android_gms_common_api_internal_zzb) {
            Preconditions.checkHandlerThread(this.zzjy.handler);
            if (!this.zzka.isConnected()) {
                this.zzjz.add(com_google_android_gms_common_api_internal_zzb);
                if (this.zzkj == null || !this.zzkj.hasResolution()) {
                    connect();
                } else {
                    onConnectionFailed(this.zzkj);
                }
            } else if (zzb(com_google_android_gms_common_api_internal_zzb)) {
                zzbr();
            } else {
                this.zzjz.add(com_google_android_gms_common_api_internal_zzb);
            }
        }

        public final void zza(zzj com_google_android_gms_common_api_internal_zzj) {
            Preconditions.checkHandlerThread(this.zzjy.handler);
            this.zzkd.add(com_google_android_gms_common_api_internal_zzj);
        }

        public final Client zzae() {
            return this.zzka;
        }

        public final void zzay() {
            Preconditions.checkHandlerThread(this.zzjy.handler);
            if (this.zzkh) {
                zzbq();
                zzc(this.zzjy.zzjq.isGooglePlayServicesAvailable(this.zzjy.zzjp) == 18 ? new Status(8, "Connection timed out while waiting for Google Play services update to complete.") : new Status(8, "API failed to connect while resuming due to an unknown error."));
                this.zzka.disconnect();
            }
        }

        public final void zzbm() {
            Preconditions.checkHandlerThread(this.zzjy.handler);
            zzc(GoogleApiManager.zzjj);
            this.zzkc.zzak();
            for (ListenerKey com_google_android_gms_common_api_internal_zzg : (ListenerKey[]) this.zzke.keySet().toArray(new ListenerKey[this.zzke.size()])) {
                zza(new zzg(com_google_android_gms_common_api_internal_zzg, new TaskCompletionSource()));
            }
            zzi(new ConnectionResult(4));
            if (this.zzka.isConnected()) {
                this.zzka.onUserSignOut(new zzbl(this));
            }
        }

        public final Map<ListenerKey<?>, zzbv> zzbn() {
            return this.zzke;
        }

        public final void zzbo() {
            Preconditions.checkHandlerThread(this.zzjy.handler);
            this.zzkj = null;
        }

        public final ConnectionResult zzbp() {
            Preconditions.checkHandlerThread(this.zzjy.handler);
            return this.zzkj;
        }

        public final boolean zzbs() {
            return zzb(true);
        }

        final SignInClient zzbt() {
            return this.zzkg == null ? null : this.zzkg.zzbt();
        }

        public final void zzc(Status status) {
            Preconditions.checkHandlerThread(this.zzjy.handler);
            for (zzb zza : this.zzjz) {
                zza.zza(status);
            }
            this.zzjz.clear();
        }

        public final void zzg(ConnectionResult connectionResult) {
            Preconditions.checkHandlerThread(this.zzjy.handler);
            this.zzka.disconnect();
            onConnectionFailed(connectionResult);
        }
    }

    private GoogleApiManager(Context context, Looper looper, GoogleApiAvailability googleApiAvailability) {
        this.zzjp = context;
        this.handler = new Handler(looper, this);
        this.zzjq = googleApiAvailability;
        this.zzjr = new GoogleApiAvailabilityCache(googleApiAvailability);
        this.handler.sendMessage(this.handler.obtainMessage(6));
    }

    public static GoogleApiManager zzb(Context context) {
        GoogleApiManager googleApiManager;
        synchronized (lock) {
            if (zzjo == null) {
                HandlerThread handlerThread = new HandlerThread("GoogleApiHandler", 9);
                handlerThread.start();
                zzjo = new GoogleApiManager(context.getApplicationContext(), handlerThread.getLooper(), GoogleApiAvailability.getInstance());
            }
            googleApiManager = zzjo;
        }
        return googleApiManager;
    }

    private final void zzb(GoogleApi<?> googleApi) {
        zzh zzm = googleApi.zzm();
        zza com_google_android_gms_common_api_internal_GoogleApiManager_zza = (zza) this.zzju.get(zzm);
        if (com_google_android_gms_common_api_internal_GoogleApiManager_zza == null) {
            com_google_android_gms_common_api_internal_GoogleApiManager_zza = new zza(this, googleApi);
            this.zzju.put(zzm, com_google_android_gms_common_api_internal_GoogleApiManager_zza);
        }
        if (com_google_android_gms_common_api_internal_GoogleApiManager_zza.requiresSignIn()) {
            this.zzjx.add(zzm);
        }
        com_google_android_gms_common_api_internal_GoogleApiManager_zza.connect();
    }

    public static GoogleApiManager zzbf() {
        GoogleApiManager googleApiManager;
        synchronized (lock) {
            Preconditions.checkNotNull(zzjo, "Must guarantee manager is non-null before using getInstance");
            googleApiManager = zzjo;
        }
        return googleApiManager;
    }

    public boolean handleMessage(Message message) {
        long j = 300000;
        zza com_google_android_gms_common_api_internal_GoogleApiManager_zza;
        zza com_google_android_gms_common_api_internal_GoogleApiManager_zza2;
        StringBuilder stringBuilder;
        zzh zzm;
        zzb com_google_android_gms_common_api_internal_GoogleApiManager_zzb;
        switch (message.what) {
            case 1:
                if (((Boolean) message.obj).booleanValue()) {
                    j = 10000;
                }
                this.zzjn = j;
                this.handler.removeMessages(12);
                for (zzh obtainMessage : this.zzju.keySet()) {
                    this.handler.sendMessageDelayed(this.handler.obtainMessage(12, obtainMessage), this.zzjn);
                }
                break;
            case 2:
                zzj com_google_android_gms_common_api_internal_zzj = (zzj) message.obj;
                for (zzh obtainMessage2 : com_google_android_gms_common_api_internal_zzj.zzs()) {
                    com_google_android_gms_common_api_internal_GoogleApiManager_zza = (zza) this.zzju.get(obtainMessage2);
                    if (com_google_android_gms_common_api_internal_GoogleApiManager_zza == null) {
                        com_google_android_gms_common_api_internal_zzj.zza(obtainMessage2, new ConnectionResult(13), null);
                        return true;
                    } else if (com_google_android_gms_common_api_internal_GoogleApiManager_zza.isConnected()) {
                        com_google_android_gms_common_api_internal_zzj.zza(obtainMessage2, ConnectionResult.RESULT_SUCCESS, com_google_android_gms_common_api_internal_GoogleApiManager_zza.zzae().getEndpointPackageName());
                    } else if (com_google_android_gms_common_api_internal_GoogleApiManager_zza.zzbp() != null) {
                        com_google_android_gms_common_api_internal_zzj.zza(obtainMessage2, com_google_android_gms_common_api_internal_GoogleApiManager_zza.zzbp(), null);
                    } else {
                        com_google_android_gms_common_api_internal_GoogleApiManager_zza.zza(com_google_android_gms_common_api_internal_zzj);
                    }
                }
                break;
            case 3:
                for (zza com_google_android_gms_common_api_internal_GoogleApiManager_zza22 : this.zzju.values()) {
                    com_google_android_gms_common_api_internal_GoogleApiManager_zza22.zzbo();
                    com_google_android_gms_common_api_internal_GoogleApiManager_zza22.connect();
                }
                break;
            case 4:
            case 8:
            case 13:
                zzbu com_google_android_gms_common_api_internal_zzbu = (zzbu) message.obj;
                com_google_android_gms_common_api_internal_GoogleApiManager_zza22 = (zza) this.zzju.get(com_google_android_gms_common_api_internal_zzbu.zzlr.zzm());
                if (com_google_android_gms_common_api_internal_GoogleApiManager_zza22 == null) {
                    zzb(com_google_android_gms_common_api_internal_zzbu.zzlr);
                    com_google_android_gms_common_api_internal_GoogleApiManager_zza22 = (zza) this.zzju.get(com_google_android_gms_common_api_internal_zzbu.zzlr.zzm());
                }
                if (!com_google_android_gms_common_api_internal_GoogleApiManager_zza22.requiresSignIn() || this.zzjt.get() == com_google_android_gms_common_api_internal_zzbu.zzlq) {
                    com_google_android_gms_common_api_internal_GoogleApiManager_zza22.zza(com_google_android_gms_common_api_internal_zzbu.zzlp);
                    return true;
                }
                com_google_android_gms_common_api_internal_zzbu.zzlp.zza(zzjj);
                com_google_android_gms_common_api_internal_GoogleApiManager_zza22.zzbm();
                return true;
            case 5:
                String errorString;
                String errorMessage;
                StringBuilder stringBuilder2;
                int i = message.arg1;
                ConnectionResult connectionResult = (ConnectionResult) message.obj;
                for (zza com_google_android_gms_common_api_internal_GoogleApiManager_zza3 : this.zzju.values()) {
                    if (com_google_android_gms_common_api_internal_GoogleApiManager_zza3.getInstanceId() == i) {
                        if (com_google_android_gms_common_api_internal_GoogleApiManager_zza3 == null) {
                            errorString = this.zzjq.getErrorString(connectionResult.getErrorCode());
                            errorMessage = connectionResult.getErrorMessage();
                            stringBuilder2 = new StringBuilder((69 + String.valueOf(errorString).length()) + String.valueOf(errorMessage).length());
                            stringBuilder2.append("Error resolution was canceled by the user, original error message: ");
                            stringBuilder2.append(errorString);
                            stringBuilder2.append(": ");
                            stringBuilder2.append(errorMessage);
                            com_google_android_gms_common_api_internal_GoogleApiManager_zza3.zzc(new Status(17, stringBuilder2.toString()));
                            return true;
                        }
                        stringBuilder = new StringBuilder(76);
                        stringBuilder.append("Could not find API instance ");
                        stringBuilder.append(i);
                        stringBuilder.append(" while trying to fail enqueued calls.");
                        Log.wtf("GoogleApiManager", stringBuilder.toString(), new Exception());
                        return true;
                    }
                }
                com_google_android_gms_common_api_internal_GoogleApiManager_zza3 = null;
                if (com_google_android_gms_common_api_internal_GoogleApiManager_zza3 == null) {
                    stringBuilder = new StringBuilder(76);
                    stringBuilder.append("Could not find API instance ");
                    stringBuilder.append(i);
                    stringBuilder.append(" while trying to fail enqueued calls.");
                    Log.wtf("GoogleApiManager", stringBuilder.toString(), new Exception());
                    return true;
                }
                errorString = this.zzjq.getErrorString(connectionResult.getErrorCode());
                errorMessage = connectionResult.getErrorMessage();
                stringBuilder2 = new StringBuilder((69 + String.valueOf(errorString).length()) + String.valueOf(errorMessage).length());
                stringBuilder2.append("Error resolution was canceled by the user, original error message: ");
                stringBuilder2.append(errorString);
                stringBuilder2.append(": ");
                stringBuilder2.append(errorMessage);
                com_google_android_gms_common_api_internal_GoogleApiManager_zza3.zzc(new Status(17, stringBuilder2.toString()));
                return true;
            case 6:
                if (PlatformVersion.isAtLeastIceCreamSandwich() && (this.zzjp.getApplicationContext() instanceof Application)) {
                    BackgroundDetector.initialize((Application) this.zzjp.getApplicationContext());
                    BackgroundDetector.getInstance().addListener(new zzbh(this));
                    if (!BackgroundDetector.getInstance().readCurrentStateIfPossible(true)) {
                        this.zzjn = 300000;
                        return true;
                    }
                }
                break;
            case 7:
                zzb((GoogleApi) message.obj);
                return true;
            case 9:
                if (this.zzju.containsKey(message.obj)) {
                    ((zza) this.zzju.get(message.obj)).resume();
                    return true;
                }
                break;
            case 10:
                for (zzh zzm2 : this.zzjx) {
                    ((zza) this.zzju.remove(zzm2)).zzbm();
                }
                this.zzjx.clear();
                return true;
            case 11:
                if (this.zzju.containsKey(message.obj)) {
                    ((zza) this.zzju.get(message.obj)).zzay();
                    return true;
                }
                break;
            case 12:
                if (this.zzju.containsKey(message.obj)) {
                    ((zza) this.zzju.get(message.obj)).zzbs();
                    return true;
                }
                break;
            case 14:
                TaskCompletionSource zzao;
                Object valueOf;
                zzae com_google_android_gms_common_api_internal_zzae = (zzae) message.obj;
                zzm2 = com_google_android_gms_common_api_internal_zzae.zzm();
                if (this.zzju.containsKey(zzm2)) {
                    boolean zza = ((zza) this.zzju.get(zzm2)).zzb(false);
                    zzao = com_google_android_gms_common_api_internal_zzae.zzao();
                    valueOf = Boolean.valueOf(zza);
                } else {
                    zzao = com_google_android_gms_common_api_internal_zzae.zzao();
                    valueOf = Boolean.valueOf(false);
                }
                zzao.setResult(valueOf);
                return true;
            case 15:
                com_google_android_gms_common_api_internal_GoogleApiManager_zzb = (zzb) message.obj;
                if (this.zzju.containsKey(com_google_android_gms_common_api_internal_GoogleApiManager_zzb.zzkn)) {
                    ((zza) this.zzju.get(com_google_android_gms_common_api_internal_GoogleApiManager_zzb.zzkn)).zza(com_google_android_gms_common_api_internal_GoogleApiManager_zzb);
                    return true;
                }
                break;
            case 16:
                com_google_android_gms_common_api_internal_GoogleApiManager_zzb = (zzb) message.obj;
                if (this.zzju.containsKey(com_google_android_gms_common_api_internal_GoogleApiManager_zzb.zzkn)) {
                    ((zza) this.zzju.get(com_google_android_gms_common_api_internal_GoogleApiManager_zzb.zzkn)).zzb(com_google_android_gms_common_api_internal_GoogleApiManager_zzb);
                    return true;
                }
                break;
            default:
                int i2 = message.what;
                stringBuilder = new StringBuilder(31);
                stringBuilder.append("Unknown message id: ");
                stringBuilder.append(i2);
                Log.w("GoogleApiManager", stringBuilder.toString());
                return false;
        }
        return true;
    }

    final PendingIntent zza(zzh<?> com_google_android_gms_common_api_internal_zzh_, int i) {
        zza com_google_android_gms_common_api_internal_GoogleApiManager_zza = (zza) this.zzju.get(com_google_android_gms_common_api_internal_zzh_);
        if (com_google_android_gms_common_api_internal_GoogleApiManager_zza == null) {
            return null;
        }
        SignInClient zzbt = com_google_android_gms_common_api_internal_GoogleApiManager_zza.zzbt();
        return zzbt == null ? null : PendingIntent.getActivity(this.zzjp, i, zzbt.getSignInIntent(), 134217728);
    }

    public final Task<Map<zzh<?>, String>> zza(Iterable<? extends GoogleApi<?>> iterable) {
        zzj com_google_android_gms_common_api_internal_zzj = new zzj(iterable);
        this.handler.sendMessage(this.handler.obtainMessage(2, com_google_android_gms_common_api_internal_zzj));
        return com_google_android_gms_common_api_internal_zzj.getTask();
    }

    public final void zza(ConnectionResult connectionResult, int i) {
        if (!zzc(connectionResult, i)) {
            this.handler.sendMessage(this.handler.obtainMessage(5, i, 0, connectionResult));
        }
    }

    public final void zza(GoogleApi<?> googleApi) {
        this.handler.sendMessage(this.handler.obtainMessage(7, googleApi));
    }

    public final <O extends ApiOptions> void zza(GoogleApi<O> googleApi, int i, ApiMethodImpl<? extends Result, AnyClient> apiMethodImpl) {
        this.handler.sendMessage(this.handler.obtainMessage(4, new zzbu(new zzd(i, apiMethodImpl), this.zzjt.get(), googleApi)));
    }

    public final void zza(zzad com_google_android_gms_common_api_internal_zzad) {
        synchronized (lock) {
            if (this.zzjv != com_google_android_gms_common_api_internal_zzad) {
                this.zzjv = com_google_android_gms_common_api_internal_zzad;
                this.zzjw.clear();
            }
            this.zzjw.addAll(com_google_android_gms_common_api_internal_zzad.zzam());
        }
    }

    final void zzb(zzad com_google_android_gms_common_api_internal_zzad) {
        synchronized (lock) {
            if (this.zzjv == com_google_android_gms_common_api_internal_zzad) {
                this.zzjv = null;
                this.zzjw.clear();
            }
        }
    }

    public final int zzbg() {
        return this.zzjs.getAndIncrement();
    }

    final boolean zzc(ConnectionResult connectionResult, int i) {
        return this.zzjq.showWrappedErrorNotification(this.zzjp, connectionResult, i);
    }

    public final void zzr() {
        this.handler.sendMessage(this.handler.obtainMessage(3));
    }
}
