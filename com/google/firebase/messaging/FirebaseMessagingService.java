package com.google.firebase.messaging;

import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.iid.zzb;
import com.google.firebase.iid.zzk;
import com.google.firebase.iid.zzz;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class FirebaseMessagingService extends zzb {
    private static final Queue<String> zzbss = new ArrayDeque(10);

    static void zzp(Bundle bundle) {
        Iterator it = bundle.keySet().iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            if (str != null && str.startsWith("google.c.")) {
                it.remove();
            }
        }
    }

    static boolean zzq(Bundle bundle) {
        return bundle == null ? false : "1".equals(bundle.getString("google.c.a.e"));
    }

    public void onDeletedMessages() {
    }

    public void onMessageReceived(RemoteMessage remoteMessage) {
    }

    public void onMessageSent(String str) {
    }

    public void onSendError(String str, Exception exception) {
    }

    protected final Intent zzf(Intent intent) {
        return zzz.zzta().zztb();
    }

    public final boolean zzg(Intent intent) {
        if (!"com.google.firebase.messaging.NOTIFICATION_OPEN".equals(intent.getAction())) {
            return false;
        }
        PendingIntent pendingIntent = (PendingIntent) intent.getParcelableExtra("pending_intent");
        if (pendingIntent != null) {
            try {
                pendingIntent.send();
            } catch (CanceledException e) {
                Log.e("FirebaseMessaging", "Notification pending intent canceled");
            }
        }
        if (zzq(intent.getExtras())) {
            zzd.zzd(this, intent);
        }
        return true;
    }

    public final void zzh(Intent intent) {
        Object obj;
        CharSequence stringExtra;
        int i;
        Task forResult;
        Bundle bundle;
        String str;
        String str2;
        int hashCode;
        Bundle extras;
        String str3;
        String str4;
        String str5;
        String action = intent.getAction();
        if (action == null) {
            action = TtmlNode.ANONYMOUS_REGION_ID;
        }
        int hashCode2 = action.hashCode();
        if (hashCode2 != 75300319) {
            if (hashCode2 == 366519424) {
                if (action.equals("com.google.android.c2dm.intent.RECEIVE")) {
                    obj = null;
                    switch (obj) {
                        case null:
                            stringExtra = intent.getStringExtra("google.message_id");
                            i = 2;
                            if (TextUtils.isEmpty(stringExtra)) {
                                forResult = Tasks.forResult(null);
                            } else {
                                bundle = new Bundle();
                                bundle.putString("google.message_id", stringExtra);
                                forResult = zzk.zzv(this).zza(2, bundle);
                            }
                            if (!TextUtils.isEmpty(stringExtra)) {
                                if (zzbss.contains(stringExtra)) {
                                    if (Log.isLoggable("FirebaseMessaging", 3)) {
                                        str = "FirebaseMessaging";
                                        str2 = "Received duplicate message: ";
                                        action = String.valueOf(stringExtra);
                                        Log.d(str, action.length() != 0 ? str2.concat(action) : new String(str2));
                                    }
                                    obj = 1;
                                    if (obj == null) {
                                        action = intent.getStringExtra("message_type");
                                        if (action == null) {
                                            action = "gcm";
                                        }
                                        hashCode = action.hashCode();
                                        if (hashCode != -2062414158) {
                                            if (action.equals("deleted_messages")) {
                                                i = 1;
                                                switch (i) {
                                                    case 0:
                                                        if (zzq(intent.getExtras())) {
                                                            zzd.zzc(this, intent);
                                                        }
                                                        extras = intent.getExtras();
                                                        if (extras == null) {
                                                            extras = new Bundle();
                                                        }
                                                        extras.remove("android.support.content.wakelockid");
                                                        if (zza.zzl(extras)) {
                                                            if (!zza.zzw(this).zzn(extras)) {
                                                                if (zzq(extras)) {
                                                                    zzd.zzf(this, intent);
                                                                }
                                                            }
                                                        }
                                                        onMessageReceived(new RemoteMessage(extras));
                                                        break;
                                                    case 1:
                                                        onDeletedMessages();
                                                        break;
                                                    case 2:
                                                        onMessageSent(intent.getStringExtra("google.message_id"));
                                                        break;
                                                    case 3:
                                                        action = intent.getStringExtra("google.message_id");
                                                        if (action == null) {
                                                            action = intent.getStringExtra("message_id");
                                                        }
                                                        onSendError(action, new SendException(intent.getStringExtra("error")));
                                                        break;
                                                    default:
                                                        str3 = "FirebaseMessaging";
                                                        str4 = "Received message with unknown type: ";
                                                        action = String.valueOf(action);
                                                        if (action.length() == 0) {
                                                        }
                                                        Log.w(str3, action.length() == 0 ? str4.concat(action) : new String(str4));
                                                        break;
                                                }
                                            }
                                        } else if (hashCode != 102161) {
                                            if (action.equals("gcm")) {
                                                i = 0;
                                                switch (i) {
                                                    case 0:
                                                        if (zzq(intent.getExtras())) {
                                                            zzd.zzc(this, intent);
                                                        }
                                                        extras = intent.getExtras();
                                                        if (extras == null) {
                                                            extras = new Bundle();
                                                        }
                                                        extras.remove("android.support.content.wakelockid");
                                                        if (zza.zzl(extras)) {
                                                            if (zza.zzw(this).zzn(extras)) {
                                                                if (zzq(extras)) {
                                                                    zzd.zzf(this, intent);
                                                                }
                                                            }
                                                        }
                                                        onMessageReceived(new RemoteMessage(extras));
                                                        break;
                                                    case 1:
                                                        onDeletedMessages();
                                                        break;
                                                    case 2:
                                                        onMessageSent(intent.getStringExtra("google.message_id"));
                                                        break;
                                                    case 3:
                                                        action = intent.getStringExtra("google.message_id");
                                                        if (action == null) {
                                                            action = intent.getStringExtra("message_id");
                                                        }
                                                        onSendError(action, new SendException(intent.getStringExtra("error")));
                                                        break;
                                                    default:
                                                        str3 = "FirebaseMessaging";
                                                        str4 = "Received message with unknown type: ";
                                                        action = String.valueOf(action);
                                                        if (action.length() == 0) {
                                                        }
                                                        Log.w(str3, action.length() == 0 ? str4.concat(action) : new String(str4));
                                                        break;
                                                }
                                            }
                                        } else if (hashCode != 814694033) {
                                            if (action.equals("send_error")) {
                                                i = 3;
                                                switch (i) {
                                                    case 0:
                                                        if (zzq(intent.getExtras())) {
                                                            zzd.zzc(this, intent);
                                                        }
                                                        extras = intent.getExtras();
                                                        if (extras == null) {
                                                            extras = new Bundle();
                                                        }
                                                        extras.remove("android.support.content.wakelockid");
                                                        if (zza.zzl(extras)) {
                                                            if (zza.zzw(this).zzn(extras)) {
                                                                if (zzq(extras)) {
                                                                    zzd.zzf(this, intent);
                                                                }
                                                            }
                                                        }
                                                        onMessageReceived(new RemoteMessage(extras));
                                                        break;
                                                    case 1:
                                                        onDeletedMessages();
                                                        break;
                                                    case 2:
                                                        onMessageSent(intent.getStringExtra("google.message_id"));
                                                        break;
                                                    case 3:
                                                        action = intent.getStringExtra("google.message_id");
                                                        if (action == null) {
                                                            action = intent.getStringExtra("message_id");
                                                        }
                                                        onSendError(action, new SendException(intent.getStringExtra("error")));
                                                        break;
                                                    default:
                                                        str3 = "FirebaseMessaging";
                                                        str4 = "Received message with unknown type: ";
                                                        action = String.valueOf(action);
                                                        if (action.length() == 0) {
                                                        }
                                                        Log.w(str3, action.length() == 0 ? str4.concat(action) : new String(str4));
                                                        break;
                                                }
                                            }
                                        } else if (hashCode != 814800675) {
                                            if (action.equals("send_event")) {
                                                switch (i) {
                                                    case 0:
                                                        if (zzq(intent.getExtras())) {
                                                            zzd.zzc(this, intent);
                                                        }
                                                        extras = intent.getExtras();
                                                        if (extras == null) {
                                                            extras = new Bundle();
                                                        }
                                                        extras.remove("android.support.content.wakelockid");
                                                        if (zza.zzl(extras)) {
                                                            if (zza.zzw(this).zzn(extras)) {
                                                                if (zzq(extras)) {
                                                                    zzd.zzf(this, intent);
                                                                }
                                                            }
                                                        }
                                                        onMessageReceived(new RemoteMessage(extras));
                                                        break;
                                                    case 1:
                                                        onDeletedMessages();
                                                        break;
                                                    case 2:
                                                        onMessageSent(intent.getStringExtra("google.message_id"));
                                                        break;
                                                    case 3:
                                                        action = intent.getStringExtra("google.message_id");
                                                        if (action == null) {
                                                            action = intent.getStringExtra("message_id");
                                                        }
                                                        onSendError(action, new SendException(intent.getStringExtra("error")));
                                                        break;
                                                    default:
                                                        str3 = "FirebaseMessaging";
                                                        str4 = "Received message with unknown type: ";
                                                        action = String.valueOf(action);
                                                        Log.w(str3, action.length() == 0 ? str4.concat(action) : new String(str4));
                                                        break;
                                                }
                                            }
                                        }
                                        i = -1;
                                        switch (i) {
                                            case 0:
                                                if (zzq(intent.getExtras())) {
                                                    zzd.zzc(this, intent);
                                                }
                                                extras = intent.getExtras();
                                                if (extras == null) {
                                                    extras = new Bundle();
                                                }
                                                extras.remove("android.support.content.wakelockid");
                                                if (zza.zzl(extras)) {
                                                    if (zza.zzw(this).zzn(extras)) {
                                                        if (zzq(extras)) {
                                                            zzd.zzf(this, intent);
                                                        }
                                                    }
                                                }
                                                onMessageReceived(new RemoteMessage(extras));
                                                break;
                                            case 1:
                                                onDeletedMessages();
                                                break;
                                            case 2:
                                                onMessageSent(intent.getStringExtra("google.message_id"));
                                                break;
                                            case 3:
                                                action = intent.getStringExtra("google.message_id");
                                                if (action == null) {
                                                    action = intent.getStringExtra("message_id");
                                                }
                                                onSendError(action, new SendException(intent.getStringExtra("error")));
                                                break;
                                            default:
                                                str3 = "FirebaseMessaging";
                                                str4 = "Received message with unknown type: ";
                                                action = String.valueOf(action);
                                                if (action.length() == 0) {
                                                }
                                                Log.w(str3, action.length() == 0 ? str4.concat(action) : new String(str4));
                                                break;
                                        }
                                    }
                                    Tasks.await(forResult, 1, TimeUnit.SECONDS);
                                    return;
                                }
                                if (zzbss.size() >= 10) {
                                    zzbss.remove();
                                }
                                zzbss.add(stringExtra);
                            }
                            obj = null;
                            if (obj == null) {
                                action = intent.getStringExtra("message_type");
                                if (action == null) {
                                    action = "gcm";
                                }
                                hashCode = action.hashCode();
                                if (hashCode != -2062414158) {
                                    if (action.equals("deleted_messages")) {
                                        i = 1;
                                        switch (i) {
                                            case 0:
                                                if (zzq(intent.getExtras())) {
                                                    zzd.zzc(this, intent);
                                                }
                                                extras = intent.getExtras();
                                                if (extras == null) {
                                                    extras = new Bundle();
                                                }
                                                extras.remove("android.support.content.wakelockid");
                                                if (zza.zzl(extras)) {
                                                    if (zza.zzw(this).zzn(extras)) {
                                                        if (zzq(extras)) {
                                                            zzd.zzf(this, intent);
                                                        }
                                                    }
                                                }
                                                onMessageReceived(new RemoteMessage(extras));
                                                break;
                                            case 1:
                                                onDeletedMessages();
                                                break;
                                            case 2:
                                                onMessageSent(intent.getStringExtra("google.message_id"));
                                                break;
                                            case 3:
                                                action = intent.getStringExtra("google.message_id");
                                                if (action == null) {
                                                    action = intent.getStringExtra("message_id");
                                                }
                                                onSendError(action, new SendException(intent.getStringExtra("error")));
                                                break;
                                            default:
                                                str3 = "FirebaseMessaging";
                                                str4 = "Received message with unknown type: ";
                                                action = String.valueOf(action);
                                                if (action.length() == 0) {
                                                }
                                                Log.w(str3, action.length() == 0 ? str4.concat(action) : new String(str4));
                                                break;
                                        }
                                    }
                                } else if (hashCode != 102161) {
                                    if (action.equals("gcm")) {
                                        i = 0;
                                        switch (i) {
                                            case 0:
                                                if (zzq(intent.getExtras())) {
                                                    zzd.zzc(this, intent);
                                                }
                                                extras = intent.getExtras();
                                                if (extras == null) {
                                                    extras = new Bundle();
                                                }
                                                extras.remove("android.support.content.wakelockid");
                                                if (zza.zzl(extras)) {
                                                    if (zza.zzw(this).zzn(extras)) {
                                                        if (zzq(extras)) {
                                                            zzd.zzf(this, intent);
                                                        }
                                                    }
                                                }
                                                onMessageReceived(new RemoteMessage(extras));
                                                break;
                                            case 1:
                                                onDeletedMessages();
                                                break;
                                            case 2:
                                                onMessageSent(intent.getStringExtra("google.message_id"));
                                                break;
                                            case 3:
                                                action = intent.getStringExtra("google.message_id");
                                                if (action == null) {
                                                    action = intent.getStringExtra("message_id");
                                                }
                                                onSendError(action, new SendException(intent.getStringExtra("error")));
                                                break;
                                            default:
                                                str3 = "FirebaseMessaging";
                                                str4 = "Received message with unknown type: ";
                                                action = String.valueOf(action);
                                                if (action.length() == 0) {
                                                }
                                                Log.w(str3, action.length() == 0 ? str4.concat(action) : new String(str4));
                                                break;
                                        }
                                    }
                                } else if (hashCode != 814694033) {
                                    if (action.equals("send_error")) {
                                        i = 3;
                                        switch (i) {
                                            case 0:
                                                if (zzq(intent.getExtras())) {
                                                    zzd.zzc(this, intent);
                                                }
                                                extras = intent.getExtras();
                                                if (extras == null) {
                                                    extras = new Bundle();
                                                }
                                                extras.remove("android.support.content.wakelockid");
                                                if (zza.zzl(extras)) {
                                                    if (zza.zzw(this).zzn(extras)) {
                                                        if (zzq(extras)) {
                                                            zzd.zzf(this, intent);
                                                        }
                                                    }
                                                }
                                                onMessageReceived(new RemoteMessage(extras));
                                                break;
                                            case 1:
                                                onDeletedMessages();
                                                break;
                                            case 2:
                                                onMessageSent(intent.getStringExtra("google.message_id"));
                                                break;
                                            case 3:
                                                action = intent.getStringExtra("google.message_id");
                                                if (action == null) {
                                                    action = intent.getStringExtra("message_id");
                                                }
                                                onSendError(action, new SendException(intent.getStringExtra("error")));
                                                break;
                                            default:
                                                str3 = "FirebaseMessaging";
                                                str4 = "Received message with unknown type: ";
                                                action = String.valueOf(action);
                                                if (action.length() == 0) {
                                                }
                                                Log.w(str3, action.length() == 0 ? str4.concat(action) : new String(str4));
                                                break;
                                        }
                                    }
                                } else if (hashCode != 814800675) {
                                    if (action.equals("send_event")) {
                                        switch (i) {
                                            case 0:
                                                if (zzq(intent.getExtras())) {
                                                    zzd.zzc(this, intent);
                                                }
                                                extras = intent.getExtras();
                                                if (extras == null) {
                                                    extras = new Bundle();
                                                }
                                                extras.remove("android.support.content.wakelockid");
                                                if (zza.zzl(extras)) {
                                                    if (zza.zzw(this).zzn(extras)) {
                                                        if (zzq(extras)) {
                                                            zzd.zzf(this, intent);
                                                        }
                                                    }
                                                }
                                                onMessageReceived(new RemoteMessage(extras));
                                                break;
                                            case 1:
                                                onDeletedMessages();
                                                break;
                                            case 2:
                                                onMessageSent(intent.getStringExtra("google.message_id"));
                                                break;
                                            case 3:
                                                action = intent.getStringExtra("google.message_id");
                                                if (action == null) {
                                                    action = intent.getStringExtra("message_id");
                                                }
                                                onSendError(action, new SendException(intent.getStringExtra("error")));
                                                break;
                                            default:
                                                str3 = "FirebaseMessaging";
                                                str4 = "Received message with unknown type: ";
                                                action = String.valueOf(action);
                                                if (action.length() == 0) {
                                                }
                                                Log.w(str3, action.length() == 0 ? str4.concat(action) : new String(str4));
                                                break;
                                        }
                                    }
                                }
                                i = -1;
                                switch (i) {
                                    case 0:
                                        if (zzq(intent.getExtras())) {
                                            zzd.zzc(this, intent);
                                        }
                                        extras = intent.getExtras();
                                        if (extras == null) {
                                            extras = new Bundle();
                                        }
                                        extras.remove("android.support.content.wakelockid");
                                        if (zza.zzl(extras)) {
                                            if (zza.zzw(this).zzn(extras)) {
                                                if (zzq(extras)) {
                                                    zzd.zzf(this, intent);
                                                }
                                            }
                                        }
                                        onMessageReceived(new RemoteMessage(extras));
                                        break;
                                    case 1:
                                        onDeletedMessages();
                                        break;
                                    case 2:
                                        onMessageSent(intent.getStringExtra("google.message_id"));
                                        break;
                                    case 3:
                                        action = intent.getStringExtra("google.message_id");
                                        if (action == null) {
                                            action = intent.getStringExtra("message_id");
                                        }
                                        onSendError(action, new SendException(intent.getStringExtra("error")));
                                        break;
                                    default:
                                        str3 = "FirebaseMessaging";
                                        str4 = "Received message with unknown type: ";
                                        action = String.valueOf(action);
                                        if (action.length() == 0) {
                                        }
                                        Log.w(str3, action.length() == 0 ? str4.concat(action) : new String(str4));
                                        break;
                                }
                            }
                            try {
                                Tasks.await(forResult, 1, TimeUnit.SECONDS);
                                return;
                            } catch (ExecutionException e) {
                                str3 = String.valueOf(e);
                                StringBuilder stringBuilder = new StringBuilder(20 + String.valueOf(str3).length());
                                stringBuilder.append("Message ack failed: ");
                                stringBuilder.append(str3);
                                Log.w("FirebaseMessaging", stringBuilder.toString());
                                return;
                            }
                        case 1:
                            if (zzq(intent.getExtras())) {
                                zzd.zze(this, intent);
                                return;
                            }
                            break;
                        default:
                            action = "FirebaseMessaging";
                            str5 = "Unknown intent action: ";
                            str3 = String.valueOf(intent.getAction());
                            Log.d(action, str3.length() != 0 ? str5.concat(str3) : new String(str5));
                            break;
                    }
                }
            }
        } else if (action.equals("com.google.firebase.messaging.NOTIFICATION_DISMISS")) {
            obj = 1;
            switch (obj) {
                case null:
                    stringExtra = intent.getStringExtra("google.message_id");
                    i = 2;
                    if (TextUtils.isEmpty(stringExtra)) {
                        bundle = new Bundle();
                        bundle.putString("google.message_id", stringExtra);
                        forResult = zzk.zzv(this).zza(2, bundle);
                    } else {
                        forResult = Tasks.forResult(null);
                    }
                    if (TextUtils.isEmpty(stringExtra)) {
                        if (zzbss.contains(stringExtra)) {
                            if (zzbss.size() >= 10) {
                                zzbss.remove();
                            }
                            zzbss.add(stringExtra);
                        } else {
                            if (Log.isLoggable("FirebaseMessaging", 3)) {
                                str = "FirebaseMessaging";
                                str2 = "Received duplicate message: ";
                                action = String.valueOf(stringExtra);
                                if (action.length() != 0) {
                                }
                                Log.d(str, action.length() != 0 ? str2.concat(action) : new String(str2));
                            }
                            obj = 1;
                            if (obj == null) {
                                action = intent.getStringExtra("message_type");
                                if (action == null) {
                                    action = "gcm";
                                }
                                hashCode = action.hashCode();
                                if (hashCode != -2062414158) {
                                    if (action.equals("deleted_messages")) {
                                        i = 1;
                                        switch (i) {
                                            case 0:
                                                if (zzq(intent.getExtras())) {
                                                    zzd.zzc(this, intent);
                                                }
                                                extras = intent.getExtras();
                                                if (extras == null) {
                                                    extras = new Bundle();
                                                }
                                                extras.remove("android.support.content.wakelockid");
                                                if (zza.zzl(extras)) {
                                                    if (zza.zzw(this).zzn(extras)) {
                                                        if (zzq(extras)) {
                                                            zzd.zzf(this, intent);
                                                        }
                                                    }
                                                }
                                                onMessageReceived(new RemoteMessage(extras));
                                                break;
                                            case 1:
                                                onDeletedMessages();
                                                break;
                                            case 2:
                                                onMessageSent(intent.getStringExtra("google.message_id"));
                                                break;
                                            case 3:
                                                action = intent.getStringExtra("google.message_id");
                                                if (action == null) {
                                                    action = intent.getStringExtra("message_id");
                                                }
                                                onSendError(action, new SendException(intent.getStringExtra("error")));
                                                break;
                                            default:
                                                str3 = "FirebaseMessaging";
                                                str4 = "Received message with unknown type: ";
                                                action = String.valueOf(action);
                                                if (action.length() == 0) {
                                                }
                                                Log.w(str3, action.length() == 0 ? str4.concat(action) : new String(str4));
                                                break;
                                        }
                                    }
                                } else if (hashCode != 102161) {
                                    if (action.equals("gcm")) {
                                        i = 0;
                                        switch (i) {
                                            case 0:
                                                if (zzq(intent.getExtras())) {
                                                    zzd.zzc(this, intent);
                                                }
                                                extras = intent.getExtras();
                                                if (extras == null) {
                                                    extras = new Bundle();
                                                }
                                                extras.remove("android.support.content.wakelockid");
                                                if (zza.zzl(extras)) {
                                                    if (zza.zzw(this).zzn(extras)) {
                                                        if (zzq(extras)) {
                                                            zzd.zzf(this, intent);
                                                        }
                                                    }
                                                }
                                                onMessageReceived(new RemoteMessage(extras));
                                                break;
                                            case 1:
                                                onDeletedMessages();
                                                break;
                                            case 2:
                                                onMessageSent(intent.getStringExtra("google.message_id"));
                                                break;
                                            case 3:
                                                action = intent.getStringExtra("google.message_id");
                                                if (action == null) {
                                                    action = intent.getStringExtra("message_id");
                                                }
                                                onSendError(action, new SendException(intent.getStringExtra("error")));
                                                break;
                                            default:
                                                str3 = "FirebaseMessaging";
                                                str4 = "Received message with unknown type: ";
                                                action = String.valueOf(action);
                                                if (action.length() == 0) {
                                                }
                                                Log.w(str3, action.length() == 0 ? str4.concat(action) : new String(str4));
                                                break;
                                        }
                                    }
                                } else if (hashCode != 814694033) {
                                    if (action.equals("send_error")) {
                                        i = 3;
                                        switch (i) {
                                            case 0:
                                                if (zzq(intent.getExtras())) {
                                                    zzd.zzc(this, intent);
                                                }
                                                extras = intent.getExtras();
                                                if (extras == null) {
                                                    extras = new Bundle();
                                                }
                                                extras.remove("android.support.content.wakelockid");
                                                if (zza.zzl(extras)) {
                                                    if (zza.zzw(this).zzn(extras)) {
                                                        if (zzq(extras)) {
                                                            zzd.zzf(this, intent);
                                                        }
                                                    }
                                                }
                                                onMessageReceived(new RemoteMessage(extras));
                                                break;
                                            case 1:
                                                onDeletedMessages();
                                                break;
                                            case 2:
                                                onMessageSent(intent.getStringExtra("google.message_id"));
                                                break;
                                            case 3:
                                                action = intent.getStringExtra("google.message_id");
                                                if (action == null) {
                                                    action = intent.getStringExtra("message_id");
                                                }
                                                onSendError(action, new SendException(intent.getStringExtra("error")));
                                                break;
                                            default:
                                                str3 = "FirebaseMessaging";
                                                str4 = "Received message with unknown type: ";
                                                action = String.valueOf(action);
                                                if (action.length() == 0) {
                                                }
                                                Log.w(str3, action.length() == 0 ? str4.concat(action) : new String(str4));
                                                break;
                                        }
                                    }
                                } else if (hashCode != 814800675) {
                                    if (action.equals("send_event")) {
                                        switch (i) {
                                            case 0:
                                                if (zzq(intent.getExtras())) {
                                                    zzd.zzc(this, intent);
                                                }
                                                extras = intent.getExtras();
                                                if (extras == null) {
                                                    extras = new Bundle();
                                                }
                                                extras.remove("android.support.content.wakelockid");
                                                if (zza.zzl(extras)) {
                                                    if (zza.zzw(this).zzn(extras)) {
                                                        if (zzq(extras)) {
                                                            zzd.zzf(this, intent);
                                                        }
                                                    }
                                                }
                                                onMessageReceived(new RemoteMessage(extras));
                                                break;
                                            case 1:
                                                onDeletedMessages();
                                                break;
                                            case 2:
                                                onMessageSent(intent.getStringExtra("google.message_id"));
                                                break;
                                            case 3:
                                                action = intent.getStringExtra("google.message_id");
                                                if (action == null) {
                                                    action = intent.getStringExtra("message_id");
                                                }
                                                onSendError(action, new SendException(intent.getStringExtra("error")));
                                                break;
                                            default:
                                                str3 = "FirebaseMessaging";
                                                str4 = "Received message with unknown type: ";
                                                action = String.valueOf(action);
                                                if (action.length() == 0) {
                                                }
                                                Log.w(str3, action.length() == 0 ? str4.concat(action) : new String(str4));
                                                break;
                                        }
                                    }
                                }
                                i = -1;
                                switch (i) {
                                    case 0:
                                        if (zzq(intent.getExtras())) {
                                            zzd.zzc(this, intent);
                                        }
                                        extras = intent.getExtras();
                                        if (extras == null) {
                                            extras = new Bundle();
                                        }
                                        extras.remove("android.support.content.wakelockid");
                                        if (zza.zzl(extras)) {
                                            if (zza.zzw(this).zzn(extras)) {
                                                if (zzq(extras)) {
                                                    zzd.zzf(this, intent);
                                                }
                                            }
                                        }
                                        onMessageReceived(new RemoteMessage(extras));
                                        break;
                                    case 1:
                                        onDeletedMessages();
                                        break;
                                    case 2:
                                        onMessageSent(intent.getStringExtra("google.message_id"));
                                        break;
                                    case 3:
                                        action = intent.getStringExtra("google.message_id");
                                        if (action == null) {
                                            action = intent.getStringExtra("message_id");
                                        }
                                        onSendError(action, new SendException(intent.getStringExtra("error")));
                                        break;
                                    default:
                                        str3 = "FirebaseMessaging";
                                        str4 = "Received message with unknown type: ";
                                        action = String.valueOf(action);
                                        if (action.length() == 0) {
                                        }
                                        Log.w(str3, action.length() == 0 ? str4.concat(action) : new String(str4));
                                        break;
                                }
                            }
                            Tasks.await(forResult, 1, TimeUnit.SECONDS);
                            return;
                        }
                    }
                    obj = null;
                    if (obj == null) {
                        action = intent.getStringExtra("message_type");
                        if (action == null) {
                            action = "gcm";
                        }
                        hashCode = action.hashCode();
                        if (hashCode != -2062414158) {
                            if (action.equals("deleted_messages")) {
                                i = 1;
                                switch (i) {
                                    case 0:
                                        if (zzq(intent.getExtras())) {
                                            zzd.zzc(this, intent);
                                        }
                                        extras = intent.getExtras();
                                        if (extras == null) {
                                            extras = new Bundle();
                                        }
                                        extras.remove("android.support.content.wakelockid");
                                        if (zza.zzl(extras)) {
                                            if (zza.zzw(this).zzn(extras)) {
                                                if (zzq(extras)) {
                                                    zzd.zzf(this, intent);
                                                }
                                            }
                                        }
                                        onMessageReceived(new RemoteMessage(extras));
                                        break;
                                    case 1:
                                        onDeletedMessages();
                                        break;
                                    case 2:
                                        onMessageSent(intent.getStringExtra("google.message_id"));
                                        break;
                                    case 3:
                                        action = intent.getStringExtra("google.message_id");
                                        if (action == null) {
                                            action = intent.getStringExtra("message_id");
                                        }
                                        onSendError(action, new SendException(intent.getStringExtra("error")));
                                        break;
                                    default:
                                        str3 = "FirebaseMessaging";
                                        str4 = "Received message with unknown type: ";
                                        action = String.valueOf(action);
                                        if (action.length() == 0) {
                                        }
                                        Log.w(str3, action.length() == 0 ? str4.concat(action) : new String(str4));
                                        break;
                                }
                            }
                        } else if (hashCode != 102161) {
                            if (action.equals("gcm")) {
                                i = 0;
                                switch (i) {
                                    case 0:
                                        if (zzq(intent.getExtras())) {
                                            zzd.zzc(this, intent);
                                        }
                                        extras = intent.getExtras();
                                        if (extras == null) {
                                            extras = new Bundle();
                                        }
                                        extras.remove("android.support.content.wakelockid");
                                        if (zza.zzl(extras)) {
                                            if (zza.zzw(this).zzn(extras)) {
                                                if (zzq(extras)) {
                                                    zzd.zzf(this, intent);
                                                }
                                            }
                                        }
                                        onMessageReceived(new RemoteMessage(extras));
                                        break;
                                    case 1:
                                        onDeletedMessages();
                                        break;
                                    case 2:
                                        onMessageSent(intent.getStringExtra("google.message_id"));
                                        break;
                                    case 3:
                                        action = intent.getStringExtra("google.message_id");
                                        if (action == null) {
                                            action = intent.getStringExtra("message_id");
                                        }
                                        onSendError(action, new SendException(intent.getStringExtra("error")));
                                        break;
                                    default:
                                        str3 = "FirebaseMessaging";
                                        str4 = "Received message with unknown type: ";
                                        action = String.valueOf(action);
                                        if (action.length() == 0) {
                                        }
                                        Log.w(str3, action.length() == 0 ? str4.concat(action) : new String(str4));
                                        break;
                                }
                            }
                        } else if (hashCode != 814694033) {
                            if (action.equals("send_error")) {
                                i = 3;
                                switch (i) {
                                    case 0:
                                        if (zzq(intent.getExtras())) {
                                            zzd.zzc(this, intent);
                                        }
                                        extras = intent.getExtras();
                                        if (extras == null) {
                                            extras = new Bundle();
                                        }
                                        extras.remove("android.support.content.wakelockid");
                                        if (zza.zzl(extras)) {
                                            if (zza.zzw(this).zzn(extras)) {
                                                if (zzq(extras)) {
                                                    zzd.zzf(this, intent);
                                                }
                                            }
                                        }
                                        onMessageReceived(new RemoteMessage(extras));
                                        break;
                                    case 1:
                                        onDeletedMessages();
                                        break;
                                    case 2:
                                        onMessageSent(intent.getStringExtra("google.message_id"));
                                        break;
                                    case 3:
                                        action = intent.getStringExtra("google.message_id");
                                        if (action == null) {
                                            action = intent.getStringExtra("message_id");
                                        }
                                        onSendError(action, new SendException(intent.getStringExtra("error")));
                                        break;
                                    default:
                                        str3 = "FirebaseMessaging";
                                        str4 = "Received message with unknown type: ";
                                        action = String.valueOf(action);
                                        if (action.length() == 0) {
                                        }
                                        Log.w(str3, action.length() == 0 ? str4.concat(action) : new String(str4));
                                        break;
                                }
                            }
                        } else if (hashCode != 814800675) {
                            if (action.equals("send_event")) {
                                switch (i) {
                                    case 0:
                                        if (zzq(intent.getExtras())) {
                                            zzd.zzc(this, intent);
                                        }
                                        extras = intent.getExtras();
                                        if (extras == null) {
                                            extras = new Bundle();
                                        }
                                        extras.remove("android.support.content.wakelockid");
                                        if (zza.zzl(extras)) {
                                            if (zza.zzw(this).zzn(extras)) {
                                                if (zzq(extras)) {
                                                    zzd.zzf(this, intent);
                                                }
                                            }
                                        }
                                        onMessageReceived(new RemoteMessage(extras));
                                        break;
                                    case 1:
                                        onDeletedMessages();
                                        break;
                                    case 2:
                                        onMessageSent(intent.getStringExtra("google.message_id"));
                                        break;
                                    case 3:
                                        action = intent.getStringExtra("google.message_id");
                                        if (action == null) {
                                            action = intent.getStringExtra("message_id");
                                        }
                                        onSendError(action, new SendException(intent.getStringExtra("error")));
                                        break;
                                    default:
                                        str3 = "FirebaseMessaging";
                                        str4 = "Received message with unknown type: ";
                                        action = String.valueOf(action);
                                        if (action.length() == 0) {
                                        }
                                        Log.w(str3, action.length() == 0 ? str4.concat(action) : new String(str4));
                                        break;
                                }
                            }
                        }
                        i = -1;
                        switch (i) {
                            case 0:
                                if (zzq(intent.getExtras())) {
                                    zzd.zzc(this, intent);
                                }
                                extras = intent.getExtras();
                                if (extras == null) {
                                    extras = new Bundle();
                                }
                                extras.remove("android.support.content.wakelockid");
                                if (zza.zzl(extras)) {
                                    if (zza.zzw(this).zzn(extras)) {
                                        if (zzq(extras)) {
                                            zzd.zzf(this, intent);
                                        }
                                    }
                                }
                                onMessageReceived(new RemoteMessage(extras));
                                break;
                            case 1:
                                onDeletedMessages();
                                break;
                            case 2:
                                onMessageSent(intent.getStringExtra("google.message_id"));
                                break;
                            case 3:
                                action = intent.getStringExtra("google.message_id");
                                if (action == null) {
                                    action = intent.getStringExtra("message_id");
                                }
                                onSendError(action, new SendException(intent.getStringExtra("error")));
                                break;
                            default:
                                str3 = "FirebaseMessaging";
                                str4 = "Received message with unknown type: ";
                                action = String.valueOf(action);
                                if (action.length() == 0) {
                                }
                                Log.w(str3, action.length() == 0 ? str4.concat(action) : new String(str4));
                                break;
                        }
                    }
                    Tasks.await(forResult, 1, TimeUnit.SECONDS);
                    return;
                case 1:
                    if (zzq(intent.getExtras())) {
                        zzd.zze(this, intent);
                        return;
                    }
                    break;
                default:
                    action = "FirebaseMessaging";
                    str5 = "Unknown intent action: ";
                    str3 = String.valueOf(intent.getAction());
                    if (str3.length() != 0) {
                    }
                    Log.d(action, str3.length() != 0 ? str5.concat(str3) : new String(str5));
                    break;
            }
        }
        obj = -1;
        switch (obj) {
            case null:
                stringExtra = intent.getStringExtra("google.message_id");
                i = 2;
                if (TextUtils.isEmpty(stringExtra)) {
                    forResult = Tasks.forResult(null);
                } else {
                    bundle = new Bundle();
                    bundle.putString("google.message_id", stringExtra);
                    forResult = zzk.zzv(this).zza(2, bundle);
                }
                if (TextUtils.isEmpty(stringExtra)) {
                    if (zzbss.contains(stringExtra)) {
                        if (Log.isLoggable("FirebaseMessaging", 3)) {
                            str = "FirebaseMessaging";
                            str2 = "Received duplicate message: ";
                            action = String.valueOf(stringExtra);
                            if (action.length() != 0) {
                            }
                            Log.d(str, action.length() != 0 ? str2.concat(action) : new String(str2));
                        }
                        obj = 1;
                        if (obj == null) {
                            action = intent.getStringExtra("message_type");
                            if (action == null) {
                                action = "gcm";
                            }
                            hashCode = action.hashCode();
                            if (hashCode != -2062414158) {
                                if (action.equals("deleted_messages")) {
                                    i = 1;
                                    switch (i) {
                                        case 0:
                                            if (zzq(intent.getExtras())) {
                                                zzd.zzc(this, intent);
                                            }
                                            extras = intent.getExtras();
                                            if (extras == null) {
                                                extras = new Bundle();
                                            }
                                            extras.remove("android.support.content.wakelockid");
                                            if (zza.zzl(extras)) {
                                                if (zza.zzw(this).zzn(extras)) {
                                                    if (zzq(extras)) {
                                                        zzd.zzf(this, intent);
                                                    }
                                                }
                                            }
                                            onMessageReceived(new RemoteMessage(extras));
                                            break;
                                        case 1:
                                            onDeletedMessages();
                                            break;
                                        case 2:
                                            onMessageSent(intent.getStringExtra("google.message_id"));
                                            break;
                                        case 3:
                                            action = intent.getStringExtra("google.message_id");
                                            if (action == null) {
                                                action = intent.getStringExtra("message_id");
                                            }
                                            onSendError(action, new SendException(intent.getStringExtra("error")));
                                            break;
                                        default:
                                            str3 = "FirebaseMessaging";
                                            str4 = "Received message with unknown type: ";
                                            action = String.valueOf(action);
                                            if (action.length() == 0) {
                                            }
                                            Log.w(str3, action.length() == 0 ? str4.concat(action) : new String(str4));
                                            break;
                                    }
                                }
                            } else if (hashCode != 102161) {
                                if (action.equals("gcm")) {
                                    i = 0;
                                    switch (i) {
                                        case 0:
                                            if (zzq(intent.getExtras())) {
                                                zzd.zzc(this, intent);
                                            }
                                            extras = intent.getExtras();
                                            if (extras == null) {
                                                extras = new Bundle();
                                            }
                                            extras.remove("android.support.content.wakelockid");
                                            if (zza.zzl(extras)) {
                                                if (zza.zzw(this).zzn(extras)) {
                                                    if (zzq(extras)) {
                                                        zzd.zzf(this, intent);
                                                    }
                                                }
                                            }
                                            onMessageReceived(new RemoteMessage(extras));
                                            break;
                                        case 1:
                                            onDeletedMessages();
                                            break;
                                        case 2:
                                            onMessageSent(intent.getStringExtra("google.message_id"));
                                            break;
                                        case 3:
                                            action = intent.getStringExtra("google.message_id");
                                            if (action == null) {
                                                action = intent.getStringExtra("message_id");
                                            }
                                            onSendError(action, new SendException(intent.getStringExtra("error")));
                                            break;
                                        default:
                                            str3 = "FirebaseMessaging";
                                            str4 = "Received message with unknown type: ";
                                            action = String.valueOf(action);
                                            if (action.length() == 0) {
                                            }
                                            Log.w(str3, action.length() == 0 ? str4.concat(action) : new String(str4));
                                            break;
                                    }
                                }
                            } else if (hashCode != 814694033) {
                                if (action.equals("send_error")) {
                                    i = 3;
                                    switch (i) {
                                        case 0:
                                            if (zzq(intent.getExtras())) {
                                                zzd.zzc(this, intent);
                                            }
                                            extras = intent.getExtras();
                                            if (extras == null) {
                                                extras = new Bundle();
                                            }
                                            extras.remove("android.support.content.wakelockid");
                                            if (zza.zzl(extras)) {
                                                if (zza.zzw(this).zzn(extras)) {
                                                    if (zzq(extras)) {
                                                        zzd.zzf(this, intent);
                                                    }
                                                }
                                            }
                                            onMessageReceived(new RemoteMessage(extras));
                                            break;
                                        case 1:
                                            onDeletedMessages();
                                            break;
                                        case 2:
                                            onMessageSent(intent.getStringExtra("google.message_id"));
                                            break;
                                        case 3:
                                            action = intent.getStringExtra("google.message_id");
                                            if (action == null) {
                                                action = intent.getStringExtra("message_id");
                                            }
                                            onSendError(action, new SendException(intent.getStringExtra("error")));
                                            break;
                                        default:
                                            str3 = "FirebaseMessaging";
                                            str4 = "Received message with unknown type: ";
                                            action = String.valueOf(action);
                                            if (action.length() == 0) {
                                            }
                                            Log.w(str3, action.length() == 0 ? str4.concat(action) : new String(str4));
                                            break;
                                    }
                                }
                            } else if (hashCode != 814800675) {
                                if (action.equals("send_event")) {
                                    switch (i) {
                                        case 0:
                                            if (zzq(intent.getExtras())) {
                                                zzd.zzc(this, intent);
                                            }
                                            extras = intent.getExtras();
                                            if (extras == null) {
                                                extras = new Bundle();
                                            }
                                            extras.remove("android.support.content.wakelockid");
                                            if (zza.zzl(extras)) {
                                                if (zza.zzw(this).zzn(extras)) {
                                                    if (zzq(extras)) {
                                                        zzd.zzf(this, intent);
                                                    }
                                                }
                                            }
                                            onMessageReceived(new RemoteMessage(extras));
                                            break;
                                        case 1:
                                            onDeletedMessages();
                                            break;
                                        case 2:
                                            onMessageSent(intent.getStringExtra("google.message_id"));
                                            break;
                                        case 3:
                                            action = intent.getStringExtra("google.message_id");
                                            if (action == null) {
                                                action = intent.getStringExtra("message_id");
                                            }
                                            onSendError(action, new SendException(intent.getStringExtra("error")));
                                            break;
                                        default:
                                            str3 = "FirebaseMessaging";
                                            str4 = "Received message with unknown type: ";
                                            action = String.valueOf(action);
                                            if (action.length() == 0) {
                                            }
                                            Log.w(str3, action.length() == 0 ? str4.concat(action) : new String(str4));
                                            break;
                                    }
                                }
                            }
                            i = -1;
                            switch (i) {
                                case 0:
                                    if (zzq(intent.getExtras())) {
                                        zzd.zzc(this, intent);
                                    }
                                    extras = intent.getExtras();
                                    if (extras == null) {
                                        extras = new Bundle();
                                    }
                                    extras.remove("android.support.content.wakelockid");
                                    if (zza.zzl(extras)) {
                                        if (zza.zzw(this).zzn(extras)) {
                                            if (zzq(extras)) {
                                                zzd.zzf(this, intent);
                                            }
                                        }
                                    }
                                    onMessageReceived(new RemoteMessage(extras));
                                    break;
                                case 1:
                                    onDeletedMessages();
                                    break;
                                case 2:
                                    onMessageSent(intent.getStringExtra("google.message_id"));
                                    break;
                                case 3:
                                    action = intent.getStringExtra("google.message_id");
                                    if (action == null) {
                                        action = intent.getStringExtra("message_id");
                                    }
                                    onSendError(action, new SendException(intent.getStringExtra("error")));
                                    break;
                                default:
                                    str3 = "FirebaseMessaging";
                                    str4 = "Received message with unknown type: ";
                                    action = String.valueOf(action);
                                    if (action.length() == 0) {
                                    }
                                    Log.w(str3, action.length() == 0 ? str4.concat(action) : new String(str4));
                                    break;
                            }
                        }
                        Tasks.await(forResult, 1, TimeUnit.SECONDS);
                        return;
                    }
                    if (zzbss.size() >= 10) {
                        zzbss.remove();
                    }
                    zzbss.add(stringExtra);
                }
                obj = null;
                if (obj == null) {
                    action = intent.getStringExtra("message_type");
                    if (action == null) {
                        action = "gcm";
                    }
                    hashCode = action.hashCode();
                    if (hashCode != -2062414158) {
                        if (action.equals("deleted_messages")) {
                            i = 1;
                            switch (i) {
                                case 0:
                                    if (zzq(intent.getExtras())) {
                                        zzd.zzc(this, intent);
                                    }
                                    extras = intent.getExtras();
                                    if (extras == null) {
                                        extras = new Bundle();
                                    }
                                    extras.remove("android.support.content.wakelockid");
                                    if (zza.zzl(extras)) {
                                        if (zza.zzw(this).zzn(extras)) {
                                            if (zzq(extras)) {
                                                zzd.zzf(this, intent);
                                            }
                                        }
                                    }
                                    onMessageReceived(new RemoteMessage(extras));
                                    break;
                                case 1:
                                    onDeletedMessages();
                                    break;
                                case 2:
                                    onMessageSent(intent.getStringExtra("google.message_id"));
                                    break;
                                case 3:
                                    action = intent.getStringExtra("google.message_id");
                                    if (action == null) {
                                        action = intent.getStringExtra("message_id");
                                    }
                                    onSendError(action, new SendException(intent.getStringExtra("error")));
                                    break;
                                default:
                                    str3 = "FirebaseMessaging";
                                    str4 = "Received message with unknown type: ";
                                    action = String.valueOf(action);
                                    if (action.length() == 0) {
                                    }
                                    Log.w(str3, action.length() == 0 ? str4.concat(action) : new String(str4));
                                    break;
                            }
                        }
                    } else if (hashCode != 102161) {
                        if (action.equals("gcm")) {
                            i = 0;
                            switch (i) {
                                case 0:
                                    if (zzq(intent.getExtras())) {
                                        zzd.zzc(this, intent);
                                    }
                                    extras = intent.getExtras();
                                    if (extras == null) {
                                        extras = new Bundle();
                                    }
                                    extras.remove("android.support.content.wakelockid");
                                    if (zza.zzl(extras)) {
                                        if (zza.zzw(this).zzn(extras)) {
                                            if (zzq(extras)) {
                                                zzd.zzf(this, intent);
                                            }
                                        }
                                    }
                                    onMessageReceived(new RemoteMessage(extras));
                                    break;
                                case 1:
                                    onDeletedMessages();
                                    break;
                                case 2:
                                    onMessageSent(intent.getStringExtra("google.message_id"));
                                    break;
                                case 3:
                                    action = intent.getStringExtra("google.message_id");
                                    if (action == null) {
                                        action = intent.getStringExtra("message_id");
                                    }
                                    onSendError(action, new SendException(intent.getStringExtra("error")));
                                    break;
                                default:
                                    str3 = "FirebaseMessaging";
                                    str4 = "Received message with unknown type: ";
                                    action = String.valueOf(action);
                                    if (action.length() == 0) {
                                    }
                                    Log.w(str3, action.length() == 0 ? str4.concat(action) : new String(str4));
                                    break;
                            }
                        }
                    } else if (hashCode != 814694033) {
                        if (action.equals("send_error")) {
                            i = 3;
                            switch (i) {
                                case 0:
                                    if (zzq(intent.getExtras())) {
                                        zzd.zzc(this, intent);
                                    }
                                    extras = intent.getExtras();
                                    if (extras == null) {
                                        extras = new Bundle();
                                    }
                                    extras.remove("android.support.content.wakelockid");
                                    if (zza.zzl(extras)) {
                                        if (zza.zzw(this).zzn(extras)) {
                                            if (zzq(extras)) {
                                                zzd.zzf(this, intent);
                                            }
                                        }
                                    }
                                    onMessageReceived(new RemoteMessage(extras));
                                    break;
                                case 1:
                                    onDeletedMessages();
                                    break;
                                case 2:
                                    onMessageSent(intent.getStringExtra("google.message_id"));
                                    break;
                                case 3:
                                    action = intent.getStringExtra("google.message_id");
                                    if (action == null) {
                                        action = intent.getStringExtra("message_id");
                                    }
                                    onSendError(action, new SendException(intent.getStringExtra("error")));
                                    break;
                                default:
                                    str3 = "FirebaseMessaging";
                                    str4 = "Received message with unknown type: ";
                                    action = String.valueOf(action);
                                    if (action.length() == 0) {
                                    }
                                    Log.w(str3, action.length() == 0 ? str4.concat(action) : new String(str4));
                                    break;
                            }
                        }
                    } else if (hashCode != 814800675) {
                        if (action.equals("send_event")) {
                            switch (i) {
                                case 0:
                                    if (zzq(intent.getExtras())) {
                                        zzd.zzc(this, intent);
                                    }
                                    extras = intent.getExtras();
                                    if (extras == null) {
                                        extras = new Bundle();
                                    }
                                    extras.remove("android.support.content.wakelockid");
                                    if (zza.zzl(extras)) {
                                        if (zza.zzw(this).zzn(extras)) {
                                            if (zzq(extras)) {
                                                zzd.zzf(this, intent);
                                            }
                                        }
                                    }
                                    onMessageReceived(new RemoteMessage(extras));
                                    break;
                                case 1:
                                    onDeletedMessages();
                                    break;
                                case 2:
                                    onMessageSent(intent.getStringExtra("google.message_id"));
                                    break;
                                case 3:
                                    action = intent.getStringExtra("google.message_id");
                                    if (action == null) {
                                        action = intent.getStringExtra("message_id");
                                    }
                                    onSendError(action, new SendException(intent.getStringExtra("error")));
                                    break;
                                default:
                                    str3 = "FirebaseMessaging";
                                    str4 = "Received message with unknown type: ";
                                    action = String.valueOf(action);
                                    if (action.length() == 0) {
                                    }
                                    Log.w(str3, action.length() == 0 ? str4.concat(action) : new String(str4));
                                    break;
                            }
                        }
                    }
                    i = -1;
                    switch (i) {
                        case 0:
                            if (zzq(intent.getExtras())) {
                                zzd.zzc(this, intent);
                            }
                            extras = intent.getExtras();
                            if (extras == null) {
                                extras = new Bundle();
                            }
                            extras.remove("android.support.content.wakelockid");
                            if (zza.zzl(extras)) {
                                if (zza.zzw(this).zzn(extras)) {
                                    if (zzq(extras)) {
                                        zzd.zzf(this, intent);
                                    }
                                }
                            }
                            onMessageReceived(new RemoteMessage(extras));
                            break;
                        case 1:
                            onDeletedMessages();
                            break;
                        case 2:
                            onMessageSent(intent.getStringExtra("google.message_id"));
                            break;
                        case 3:
                            action = intent.getStringExtra("google.message_id");
                            if (action == null) {
                                action = intent.getStringExtra("message_id");
                            }
                            onSendError(action, new SendException(intent.getStringExtra("error")));
                            break;
                        default:
                            str3 = "FirebaseMessaging";
                            str4 = "Received message with unknown type: ";
                            action = String.valueOf(action);
                            if (action.length() == 0) {
                            }
                            Log.w(str3, action.length() == 0 ? str4.concat(action) : new String(str4));
                            break;
                    }
                }
                Tasks.await(forResult, 1, TimeUnit.SECONDS);
                return;
            case 1:
                if (zzq(intent.getExtras())) {
                    zzd.zze(this, intent);
                    return;
                }
                break;
            default:
                action = "FirebaseMessaging";
                str5 = "Unknown intent action: ";
                str3 = String.valueOf(intent.getAction());
                if (str3.length() != 0) {
                }
                Log.d(action, str3.length() != 0 ? str5.concat(str3) : new String(str5));
                break;
        }
    }
}
