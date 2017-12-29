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
import com.google.android.gms.iid.MessengerCompat;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class zzv {
    private static PendingIntent zzicn;
    private static int zzift = 0;
    private final Context zzair;
    private Messenger zzicr;
    private Messenger zzifw;
    private MessengerCompat zzifx;
    private final zzu zznys;
    private final SimpleArrayMap<String, TaskCompletionSource<Bundle>> zznzn = new SimpleArrayMap();

    public zzv(Context context, zzu com_google_firebase_iid_zzu) {
        this.zzair = context;
        this.zznys = com_google_firebase_iid_zzu;
        this.zzicr = new Messenger(new zzw(this, Looper.getMainLooper()));
    }

    private final Bundle zzae(Bundle bundle) throws IOException {
        Bundle zzaf = zzaf(bundle);
        if (zzaf == null || !zzaf.containsKey("google.messenger")) {
            return zzaf;
        }
        zzaf = zzaf(bundle);
        return (zzaf == null || !zzaf.containsKey("google.messenger")) ? zzaf : null;
    }

    private final Bundle zzaf(Bundle bundle) throws IOException {
        String zzavi = zzavi();
        TaskCompletionSource taskCompletionSource = new TaskCompletionSource();
        synchronized (this.zznzn) {
            this.zznzn.put(zzavi, taskCompletionSource);
        }
        if (this.zznys.zzcjf() == 0) {
            throw new IOException("MISSING_INSTANCEID_SERVICE");
        }
        Intent intent = new Intent();
        intent.setPackage("com.google.android.gms");
        if (this.zznys.zzcjf() == 2) {
            intent.setAction("com.google.iid.TOKEN_REQUEST");
        } else {
            intent.setAction("com.google.android.c2dm.intent.REGISTER");
        }
        intent.putExtras(bundle);
        zzd(this.zzair, intent);
        intent.putExtra("kid", new StringBuilder(String.valueOf(zzavi).length() + 5).append("|ID|").append(zzavi).append("|").toString());
        if (Log.isLoggable("FirebaseInstanceId", 3)) {
            String valueOf = String.valueOf(intent.getExtras());
            Log.d("FirebaseInstanceId", new StringBuilder(String.valueOf(valueOf).length() + 8).append("Sending ").append(valueOf).toString());
        }
        intent.putExtra("google.messenger", this.zzicr);
        if (!(this.zzifw == null && this.zzifx == null)) {
            Message obtain = Message.obtain();
            obtain.obj = intent;
            try {
                if (this.zzifw != null) {
                    this.zzifw.send(obtain);
                } else {
                    this.zzifx.send(obtain);
                }
            } catch (RemoteException e) {
                if (Log.isLoggable("FirebaseInstanceId", 3)) {
                    Log.d("FirebaseInstanceId", "Messenger failed, fallback to startService");
                }
            }
            Bundle bundle2 = (Bundle) Tasks.await(taskCompletionSource.getTask(), 30000, TimeUnit.MILLISECONDS);
            synchronized (this.zznzn) {
                this.zznzn.remove(zzavi);
            }
            return bundle2;
        }
        if (this.zznys.zzcjf() == 2) {
            this.zzair.sendBroadcast(intent);
        } else {
            this.zzair.startService(intent);
        }
        try {
            Bundle bundle22 = (Bundle) Tasks.await(taskCompletionSource.getTask(), 30000, TimeUnit.MILLISECONDS);
            synchronized (this.zznzn) {
                this.zznzn.remove(zzavi);
            }
            return bundle22;
        } catch (InterruptedException e2) {
            Log.w("FirebaseInstanceId", "No response");
            throw new IOException("TIMEOUT");
        } catch (TimeoutException e3) {
            Log.w("FirebaseInstanceId", "No response");
            throw new IOException("TIMEOUT");
        } catch (ExecutionException e4) {
            r0 = e4.getCause();
            Throwable cause;
            if (cause instanceof IOException) {
                throw ((IOException) cause);
            }
            throw new IOException(cause);
        } catch (Throwable th) {
            synchronized (this.zznzn) {
                this.zznzn.remove(zzavi);
            }
        }
    }

    private static synchronized String zzavi() {
        String num;
        synchronized (zzv.class) {
            int i = zzift;
            zzift = i + 1;
            num = Integer.toString(i);
        }
        return num;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final void zzbl(java.lang.String r6, java.lang.String r7) {
        /*
        r5 = this;
        r2 = r5.zznzn;
        monitor-enter(r2);
        if (r6 != 0) goto L_0x002a;
    L_0x0005:
        r0 = 0;
        r1 = r0;
    L_0x0007:
        r0 = r5.zznzn;	 Catch:{ all -> 0x004d }
        r0 = r0.size();	 Catch:{ all -> 0x004d }
        if (r1 >= r0) goto L_0x0023;
    L_0x000f:
        r0 = r5.zznzn;	 Catch:{ all -> 0x004d }
        r0 = r0.valueAt(r1);	 Catch:{ all -> 0x004d }
        r0 = (com.google.android.gms.tasks.TaskCompletionSource) r0;	 Catch:{ all -> 0x004d }
        r3 = new java.io.IOException;	 Catch:{ all -> 0x004d }
        r3.<init>(r7);	 Catch:{ all -> 0x004d }
        r0.setException(r3);	 Catch:{ all -> 0x004d }
        r0 = r1 + 1;
        r1 = r0;
        goto L_0x0007;
    L_0x0023:
        r0 = r5.zznzn;	 Catch:{ all -> 0x004d }
        r0.clear();	 Catch:{ all -> 0x004d }
    L_0x0028:
        monitor-exit(r2);	 Catch:{ all -> 0x004d }
    L_0x0029:
        return;
    L_0x002a:
        r0 = r5.zznzn;	 Catch:{ all -> 0x004d }
        r0 = r0.remove(r6);	 Catch:{ all -> 0x004d }
        r0 = (com.google.android.gms.tasks.TaskCompletionSource) r0;	 Catch:{ all -> 0x004d }
        if (r0 != 0) goto L_0x0056;
    L_0x0034:
        r1 = "FirebaseInstanceId";
        r3 = "Missing callback for ";
        r0 = java.lang.String.valueOf(r6);	 Catch:{ all -> 0x004d }
        r4 = r0.length();	 Catch:{ all -> 0x004d }
        if (r4 == 0) goto L_0x0050;
    L_0x0044:
        r0 = r3.concat(r0);	 Catch:{ all -> 0x004d }
    L_0x0048:
        android.util.Log.w(r1, r0);	 Catch:{ all -> 0x004d }
        monitor-exit(r2);	 Catch:{ all -> 0x004d }
        goto L_0x0029;
    L_0x004d:
        r0 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x004d }
        throw r0;
    L_0x0050:
        r0 = new java.lang.String;	 Catch:{ all -> 0x004d }
        r0.<init>(r3);	 Catch:{ all -> 0x004d }
        goto L_0x0048;
    L_0x0056:
        r1 = new java.io.IOException;	 Catch:{ all -> 0x004d }
        r1.<init>(r7);	 Catch:{ all -> 0x004d }
        r0.setException(r1);	 Catch:{ all -> 0x004d }
        goto L_0x0028;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.firebase.iid.zzv.zzbl(java.lang.String, java.lang.String):void");
    }

    private static synchronized void zzd(Context context, Intent intent) {
        synchronized (zzv.class) {
            if (zzicn == null) {
                Intent intent2 = new Intent();
                intent2.setPackage("com.google.example.invalidpackage");
                zzicn = PendingIntent.getBroadcast(context, 0, intent2, 0);
            }
            intent.putExtra("app", zzicn);
        }
    }

    private final void zze(Message message) {
        if (message == null || !(message.obj instanceof Intent)) {
            Log.w("FirebaseInstanceId", "Dropping invalid message");
            return;
        }
        Intent intent = (Intent) message.obj;
        intent.setExtrasClassLoader(MessengerCompat.class.getClassLoader());
        if (intent.hasExtra("google.messenger")) {
            Parcelable parcelableExtra = intent.getParcelableExtra("google.messenger");
            if (parcelableExtra instanceof MessengerCompat) {
                this.zzifx = (MessengerCompat) parcelableExtra;
            }
            if (parcelableExtra instanceof Messenger) {
                this.zzifw = (Messenger) parcelableExtra;
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
            String str2;
            if (stringExtra2 == null) {
                stringExtra = intent.getStringExtra("error");
                if (stringExtra == null) {
                    valueOf = String.valueOf(intent.getExtras());
                    Log.w("FirebaseInstanceId", new StringBuilder(String.valueOf(valueOf).length() + 49).append("Unexpected response, no error or registration id ").append(valueOf).toString());
                    return;
                }
                if (Log.isLoggable("FirebaseInstanceId", 3)) {
                    str = "FirebaseInstanceId";
                    str2 = "Received InstanceID error ";
                    action = String.valueOf(stringExtra);
                    Log.d(str, action.length() != 0 ? str2.concat(action) : new String(str2));
                }
                if (stringExtra.startsWith("|")) {
                    String[] split = stringExtra.split("\\|");
                    if (!"ID".equals(split[1])) {
                        String str3 = "FirebaseInstanceId";
                        String str4 = "Unexpected structured response ";
                        action = String.valueOf(stringExtra);
                        Log.w(str3, action.length() != 0 ? str4.concat(action) : new String(str4));
                    }
                    if (split.length > 2) {
                        action = split[2];
                        str = split[3];
                        if (str.startsWith(":")) {
                            str = str.substring(1);
                        }
                    } else {
                        str = "UNKNOWN";
                        action = null;
                    }
                    intent.putExtra("error", str);
                } else {
                    action = null;
                    str = stringExtra;
                }
                zzbl(action, str);
                return;
            }
            Matcher matcher = Pattern.compile("\\|ID\\|([^|]+)\\|:?+(.*)").matcher(stringExtra2);
            if (matcher.matches()) {
                action = matcher.group(1);
                str = matcher.group(2);
                Bundle extras = intent.getExtras();
                extras.putString("registration_id", str);
                synchronized (this.zznzn) {
                    TaskCompletionSource taskCompletionSource = (TaskCompletionSource) this.zznzn.remove(action);
                    if (taskCompletionSource == null) {
                        stringExtra = "FirebaseInstanceId";
                        str2 = "Missing callback for ";
                        valueOf = String.valueOf(action);
                        Log.w(stringExtra, valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2));
                        return;
                    }
                    taskCompletionSource.setResult(extras);
                }
            } else if (Log.isLoggable("FirebaseInstanceId", 3)) {
                str = "FirebaseInstanceId";
                stringExtra = "Unexpected response string: ";
                valueOf = String.valueOf(stringExtra2);
                Log.d(str, valueOf.length() != 0 ? stringExtra.concat(valueOf) : new String(stringExtra));
            }
        } else if (Log.isLoggable("FirebaseInstanceId", 3)) {
            str = "FirebaseInstanceId";
            stringExtra = "Unexpected response action: ";
            valueOf = String.valueOf(action);
            Log.d(str, valueOf.length() != 0 ? stringExtra.concat(valueOf) : new String(stringExtra));
        }
    }

    final Bundle zzad(Bundle bundle) throws IOException {
        Exception e;
        if (this.zznys.zzcji() < 12000000) {
            return zzae(bundle);
        }
        try {
            return (Bundle) Tasks.await(zzi.zzev(this.zzair).zzi(1, bundle));
        } catch (InterruptedException e2) {
            e = e2;
        } catch (ExecutionException e3) {
            e = e3;
        }
        if (Log.isLoggable("FirebaseInstanceId", 3)) {
            String valueOf = String.valueOf(e);
            Log.d("FirebaseInstanceId", new StringBuilder(String.valueOf(valueOf).length() + 22).append("Error making request: ").append(valueOf).toString());
        }
        return ((e.getCause() instanceof zzs) && ((zzs) e.getCause()).getErrorCode() == 4) ? zzae(bundle) : null;
    }
}
