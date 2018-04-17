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
import java.util.concurrent.TimeUnit;
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
                str = String.valueOf(str);
                Log.w(str2, str.length() != 0 ? str3.concat(str) : new String(str3));
                return;
            }
            taskCompletionSource.setResult(bundle);
        }
    }

    private final void zzb(Message message) {
        String str;
        String str2;
        if (message == null || !(message.obj instanceof Intent)) {
            str = "FirebaseInstanceId";
            str2 = "Dropping invalid message";
        } else {
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
            Intent intent2 = (Intent) message.obj;
            str2 = intent2.getAction();
            String str3;
            if ("com.google.android.c2dm.intent.REGISTRATION".equals(str2)) {
                CharSequence stringExtra = intent2.getStringExtra("registration_id");
                if (stringExtra == null) {
                    stringExtra = intent2.getStringExtra("unregistered");
                }
                if (stringExtra == null) {
                    str2 = intent2.getStringExtra("error");
                    if (str2 == null) {
                        str = String.valueOf(intent2.getExtras());
                        StringBuilder stringBuilder = new StringBuilder(49 + String.valueOf(str).length());
                        stringBuilder.append("Unexpected response, no error or registration id ");
                        stringBuilder.append(str);
                        Log.w("FirebaseInstanceId", stringBuilder.toString());
                        return;
                    }
                    if (Log.isLoggable("FirebaseInstanceId", 3)) {
                        String str4 = "FirebaseInstanceId";
                        String str5 = "Received InstanceID error ";
                        String valueOf = String.valueOf(str2);
                        Log.d(str4, valueOf.length() != 0 ? str5.concat(valueOf) : new String(str5));
                    }
                    if (str2.startsWith("|")) {
                        String[] split = str2.split("\\|");
                        if (split.length > 2) {
                            if ("ID".equals(split[1])) {
                                str2 = split[2];
                                str3 = split[3];
                                if (str3.startsWith(":")) {
                                    str3 = str3.substring(1);
                                }
                                zza(str2, intent2.putExtra("error", str3).getExtras());
                                return;
                            }
                        }
                        str = "FirebaseInstanceId";
                        str3 = "Unexpected structured response ";
                        str2 = String.valueOf(str2);
                        str2 = str2.length() != 0 ? str3.concat(str2) : new String(str3);
                    } else {
                        synchronized (this.zzbrp) {
                            for (int i = 0; i < this.zzbrp.size(); i++) {
                                zza((String) this.zzbrp.keyAt(i), intent2.getExtras());
                            }
                        }
                        return;
                    }
                }
                Matcher matcher = Pattern.compile("\\|ID\\|([^|]+)\\|:?+(.*)").matcher(stringExtra);
                if (matcher.matches()) {
                    str2 = matcher.group(1);
                    str3 = matcher.group(2);
                    Bundle extras = intent2.getExtras();
                    extras.putString("registration_id", str3);
                    zza(str2, extras);
                    return;
                }
                if (Log.isLoggable("FirebaseInstanceId", 3)) {
                    str = "FirebaseInstanceId";
                    str3 = "Unexpected response string: ";
                    str2 = String.valueOf(stringExtra);
                    Log.d(str, str2.length() != 0 ? str3.concat(str2) : new String(str3));
                }
                return;
            }
            if (Log.isLoggable("FirebaseInstanceId", 3)) {
                str = "FirebaseInstanceId";
                str3 = "Unexpected response action: ";
                str2 = String.valueOf(str2);
                Log.d(str, str2.length() != 0 ? str3.concat(str2) : new String(str3));
            }
            return;
        }
        Log.w(str, str2);
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
        intent.setAction(this.zzbqn.zzsu() == 2 ? "com.google.iid.TOKEN_REQUEST" : "com.google.android.c2dm.intent.REGISTER");
        intent.putExtras(bundle);
        zza(this.zzqs, intent);
        StringBuilder stringBuilder = new StringBuilder(5 + String.valueOf(zzsz).length());
        stringBuilder.append("|ID|");
        stringBuilder.append(zzsz);
        stringBuilder.append("|");
        intent.putExtra("kid", stringBuilder.toString());
        if (Log.isLoggable("FirebaseInstanceId", 3)) {
            String valueOf = String.valueOf(intent.getExtras());
            StringBuilder stringBuilder2 = new StringBuilder(8 + String.valueOf(valueOf).length());
            stringBuilder2.append("Sending ");
            stringBuilder2.append(valueOf);
            Log.d("FirebaseInstanceId", stringBuilder2.toString());
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
            bundle = (Bundle) Tasks.await(taskCompletionSource.getTask(), 30000, TimeUnit.MILLISECONDS);
            synchronized (this.zzbrp) {
                this.zzbrp.remove(zzsz);
            }
            return bundle;
        }
        if (this.zzbqn.zzsu() == 2) {
            this.zzqs.sendBroadcast(intent);
        } else {
            this.zzqs.startService(intent);
        }
        try {
            bundle = (Bundle) Tasks.await(taskCompletionSource.getTask(), 30000, TimeUnit.MILLISECONDS);
            synchronized (this.zzbrp) {
                this.zzbrp.remove(zzsz);
            }
            return bundle;
        } catch (InterruptedException e2) {
            Log.w("FirebaseInstanceId", "No response");
            throw new IOException("TIMEOUT");
        } catch (Throwable e3) {
            throw new IOException(e3);
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

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    final android.os.Bundle zzi(android.os.Bundle r6) throws java.io.IOException {
        /*
        r5 = this;
        r0 = r5.zzbqn;
        r0 = r0.zzsx();
        r1 = 12000000; // 0xb71b00 float:1.6815582E-38 double:5.9287878E-317;
        if (r0 < r1) goto L_0x0068;
    L_0x000b:
        r0 = r5.zzqs;
        r0 = com.google.firebase.iid.zzk.zzv(r0);
        r1 = 1;
        r0 = r0.zzb(r1, r6);
        r0 = com.google.android.gms.tasks.Tasks.await(r0);	 Catch:{ InterruptedException -> 0x001d, InterruptedException -> 0x001d }
        r0 = (android.os.Bundle) r0;	 Catch:{ InterruptedException -> 0x001d, InterruptedException -> 0x001d }
        return r0;
    L_0x001d:
        r0 = move-exception;
        r1 = "FirebaseInstanceId";
        r2 = 3;
        r1 = android.util.Log.isLoggable(r1, r2);
        if (r1 == 0) goto L_0x004c;
    L_0x0027:
        r1 = "FirebaseInstanceId";
        r2 = java.lang.String.valueOf(r0);
        r3 = 22;
        r4 = java.lang.String.valueOf(r2);
        r4 = r4.length();
        r3 = r3 + r4;
        r4 = new java.lang.StringBuilder;
        r4.<init>(r3);
        r3 = "Error making request: ";
        r4.append(r3);
        r4.append(r2);
        r2 = r4.toString();
        android.util.Log.d(r1, r2);
    L_0x004c:
        r1 = r0.getCause();
        r1 = r1 instanceof com.google.firebase.iid.zzu;
        if (r1 == 0) goto L_0x0066;
    L_0x0054:
        r0 = r0.getCause();
        r0 = (com.google.firebase.iid.zzu) r0;
        r0 = r0.getErrorCode();
        r1 = 4;
        if (r0 != r1) goto L_0x0066;
    L_0x0061:
        r6 = r5.zzj(r6);
        return r6;
    L_0x0066:
        r6 = 0;
        return r6;
    L_0x0068:
        r6 = r5.zzj(r6);
        return r6;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.firebase.iid.zzx.zzi(android.os.Bundle):android.os.Bundle");
    }
}
