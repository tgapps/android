package com.google.firebase.iid;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcelable;
import android.os.RemoteException;
import android.support.v4.util.SimpleArrayMap;
import android.util.Log;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.iid.zzi.zza;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.concurrent.GuardedBy;

final class zzx {
    private static int zzbrh = 0;
    private static PendingIntent zzbro;
    private final zzw zzbqn;
    @GuardedBy("responseCallbacks")
    private final SimpleArrayMap<String, TaskCompletionSource<Bundle>> zzbrp = new SimpleArrayMap();
    private Messenger zzbrq;
    private Messenger zzbrr;
    private zzi zzbrs;
    private final Context zzqs;

    public zzx(Context context, zzw com_google_firebase_iid_zzw) {
        this.zzqs = context;
        this.zzbqn = com_google_firebase_iid_zzw;
        this.zzbrq = new Messenger(new zzy(this, Looper.getMainLooper()));
    }

    private static synchronized void zza(Context context, Intent intent) {
        synchronized (zzx.class) {
            if (zzbro == null) {
                Intent intent2 = new Intent();
                intent2.setPackage("com.google.example.invalidpackage");
                zzbro = PendingIntent.getBroadcast(context, 0, intent2, 0);
            }
            intent.putExtra("app", zzbro);
        }
    }

    private final void zza(String str, Bundle bundle) {
        synchronized (this.zzbrp) {
            TaskCompletionSource taskCompletionSource = (TaskCompletionSource) this.zzbrp.remove(str);
            if (taskCompletionSource == null) {
                String str2 = "FirebaseInstanceId";
                String str3 = "Missing callback for ";
                String valueOf = String.valueOf(str);
                Log.w(str2, valueOf.length() != 0 ? str3.concat(valueOf) : new String(str3));
                return;
            }
            taskCompletionSource.setResult(bundle);
        }
    }

    private final void zzb(Message message) {
        if (message == null || !(message.obj instanceof Intent)) {
            Log.w("FirebaseInstanceId", "Dropping invalid message");
            return;
        }
        Intent intent = (Intent) message.obj;
        intent.setExtrasClassLoader(new zza());
        if (intent.hasExtra("google.messenger")) {
            Parcelable parcelableExtra = intent.getParcelableExtra("google.messenger");
            if (parcelableExtra instanceof zzi) {
                this.zzbrs = (zzi) parcelableExtra;
            }
            if (parcelableExtra instanceof Messenger) {
                this.zzbrr = (Messenger) parcelableExtra;
            }
        }
        intent = (Intent) message.obj;
        String action = intent.getAction();
        String stringExtra;
        String valueOf;
        String str;
        if ("com.google.android.c2dm.intent.REGISTRATION".equals(action)) {
            CharSequence stringExtra2 = intent.getStringExtra("registration_id");
            if (stringExtra2 == null) {
                stringExtra2 = intent.getStringExtra("unregistered");
            }
            if (stringExtra2 == null) {
                stringExtra = intent.getStringExtra("error");
                if (stringExtra == null) {
                    valueOf = String.valueOf(intent.getExtras());
                    Log.w("FirebaseInstanceId", new StringBuilder(String.valueOf(valueOf).length() + 49).append("Unexpected response, no error or registration id ").append(valueOf).toString());
                    return;
                }
                if (Log.isLoggable("FirebaseInstanceId", 3)) {
                    str = "FirebaseInstanceId";
                    String str2 = "Received InstanceID error ";
                    action = String.valueOf(stringExtra);
                    Log.d(str, action.length() != 0 ? str2.concat(action) : new String(str2));
                }
                if (stringExtra.startsWith("|")) {
                    String[] split = stringExtra.split("\\|");
                    if (split.length <= 2 || !"ID".equals(split[1])) {
                        action = "FirebaseInstanceId";
                        str = "Unexpected structured response ";
                        valueOf = String.valueOf(stringExtra);
                        Log.w(action, valueOf.length() != 0 ? str.concat(valueOf) : new String(str));
                        return;
                    }
                    stringExtra = split[2];
                    action = split[3];
                    if (action.startsWith(":")) {
                        action = action.substring(1);
                    }
                    zza(stringExtra, intent.putExtra("error", action).getExtras());
                    return;
                }
                synchronized (this.zzbrp) {
                    for (int i = 0; i < this.zzbrp.size(); i++) {
                        zza((String) this.zzbrp.keyAt(i), intent.getExtras());
                    }
                }
                return;
            }
            Matcher matcher = Pattern.compile("\\|ID\\|([^|]+)\\|:?+(.*)").matcher(stringExtra2);
            if (matcher.matches()) {
                action = matcher.group(1);
                stringExtra = matcher.group(2);
                Bundle extras = intent.getExtras();
                extras.putString("registration_id", stringExtra);
                zza(action, extras);
            } else if (Log.isLoggable("FirebaseInstanceId", 3)) {
                stringExtra = "FirebaseInstanceId";
                str = "Unexpected response string: ";
                valueOf = String.valueOf(stringExtra2);
                Log.d(stringExtra, valueOf.length() != 0 ? str.concat(valueOf) : new String(str));
            }
        } else if (Log.isLoggable("FirebaseInstanceId", 3)) {
            stringExtra = "FirebaseInstanceId";
            str = "Unexpected response action: ";
            valueOf = String.valueOf(action);
            Log.d(stringExtra, valueOf.length() != 0 ? str.concat(valueOf) : new String(str));
        }
    }

    private final Bundle zzj(Bundle bundle) throws IOException {
        Bundle zzk = zzk(bundle);
        if (zzk == null || !zzk.containsKey("google.messenger")) {
            return zzk;
        }
        zzk = zzk(bundle);
        return (zzk == null || !zzk.containsKey("google.messenger")) ? zzk : null;
    }

    private final Bundle zzk(Bundle bundle) throws IOException {
        String zzsz = zzsz();
        TaskCompletionSource taskCompletionSource = new TaskCompletionSource();
        synchronized (this.zzbrp) {
            this.zzbrp.put(zzsz, taskCompletionSource);
        }
        if (this.zzbqn.zzsu() == 0) {
            throw new IOException("MISSING_INSTANCEID_SERVICE");
        }
        Intent intent = new Intent();
        intent.setPackage("com.google.android.gms");
        if (this.zzbqn.zzsu() == 2) {
            intent.setAction("com.google.iid.TOKEN_REQUEST");
        } else {
            intent.setAction("com.google.android.c2dm.intent.REGISTER");
        }
        intent.putExtras(bundle);
        zza(this.zzqs, intent);
        intent.putExtra("kid", new StringBuilder(String.valueOf(zzsz).length() + 5).append("|ID|").append(zzsz).append("|").toString());
        if (Log.isLoggable("FirebaseInstanceId", 3)) {
            String valueOf = String.valueOf(intent.getExtras());
            Log.d("FirebaseInstanceId", new StringBuilder(String.valueOf(valueOf).length() + 8).append("Sending ").append(valueOf).toString());
        }
        intent.putExtra("google.messenger", this.zzbrq);
        if (!(this.zzbrr == null && this.zzbrs == null)) {
            Message obtain = Message.obtain();
            obtain.obj = intent;
            try {
                if (this.zzbrr != null) {
                    this.zzbrr.send(obtain);
                } else {
                    this.zzbrs.send(obtain);
                }
            } catch (RemoteException e) {
                if (Log.isLoggable("FirebaseInstanceId", 3)) {
                    Log.d("FirebaseInstanceId", "Messenger failed, fallback to startService");
                }
            }
            Bundle bundle2 = (Bundle) Tasks.await(taskCompletionSource.getTask(), 30000, TimeUnit.MILLISECONDS);
            synchronized (this.zzbrp) {
                this.zzbrp.remove(zzsz);
            }
            return bundle2;
        }
        if (this.zzbqn.zzsu() == 2) {
            this.zzqs.sendBroadcast(intent);
        } else {
            this.zzqs.startService(intent);
        }
        try {
            Bundle bundle22 = (Bundle) Tasks.await(taskCompletionSource.getTask(), 30000, TimeUnit.MILLISECONDS);
            synchronized (this.zzbrp) {
                this.zzbrp.remove(zzsz);
            }
            return bundle22;
        } catch (InterruptedException e2) {
            Log.w("FirebaseInstanceId", "No response");
            throw new IOException("TIMEOUT");
        } catch (TimeoutException e3) {
            Log.w("FirebaseInstanceId", "No response");
            throw new IOException("TIMEOUT");
        } catch (Throwable e4) {
            throw new IOException(e4);
        } catch (Throwable th) {
            synchronized (this.zzbrp) {
                this.zzbrp.remove(zzsz);
            }
        }
    }

    private static synchronized String zzsz() {
        String num;
        synchronized (zzx.class) {
            int i = zzbrh;
            zzbrh = i + 1;
            num = Integer.toString(i);
        }
        return num;
    }

    final Bundle zzi(Bundle bundle) throws IOException {
        Exception e;
        if (this.zzbqn.zzsx() < 12000000) {
            return zzj(bundle);
        }
        try {
            return (Bundle) Tasks.await(zzk.zzv(this.zzqs).zzb(1, bundle));
        } catch (InterruptedException e2) {
            e = e2;
        } catch (ExecutionException e3) {
            e = e3;
        }
        if (Log.isLoggable("FirebaseInstanceId", 3)) {
            String valueOf = String.valueOf(e);
            Log.d("FirebaseInstanceId", new StringBuilder(String.valueOf(valueOf).length() + 22).append("Error making request: ").append(valueOf).toString());
        }
        return ((e.getCause() instanceof zzu) && ((zzu) e.getCause()).getErrorCode() == 4) ? zzj(bundle) : null;
    }
}
