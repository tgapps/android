package com.google.firebase.messaging;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.Notification.BigTextStyle;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.Color;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Process;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.common.util.PlatformVersion;
import com.google.firebase.iid.zzz;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.json.JSONArray;
import org.json.JSONException;
import org.telegram.tgnet.ConnectionsManager;

final class zza {
    private static zza zzbsg;
    private Bundle zzbsh;
    private Method zzbsi;
    private Method zzbsj;
    private final AtomicInteger zzbsk = new AtomicInteger((int) SystemClock.elapsedRealtime());
    private final Context zzqs;

    private zza(Context context) {
        this.zzqs = context.getApplicationContext();
    }

    @TargetApi(26)
    private final Notification zza(CharSequence charSequence, String str, int i, Integer num, Uri uri, PendingIntent pendingIntent, PendingIntent pendingIntent2, String str2) {
        Builder smallIcon = new Builder(this.zzqs).setAutoCancel(true).setSmallIcon(i);
        if (!TextUtils.isEmpty(charSequence)) {
            smallIcon.setContentTitle(charSequence);
        }
        if (!TextUtils.isEmpty(str)) {
            smallIcon.setContentText(str);
            smallIcon.setStyle(new BigTextStyle().bigText(str));
        }
        if (num != null) {
            smallIcon.setColor(num.intValue());
        }
        if (uri != null) {
            smallIcon.setSound(uri);
        }
        if (pendingIntent != null) {
            smallIcon.setContentIntent(pendingIntent);
        }
        if (pendingIntent2 != null) {
            smallIcon.setDeleteIntent(pendingIntent2);
        }
        if (str2 != null) {
            if (this.zzbsi == null) {
                this.zzbsi = zzfh("setChannelId");
            }
            if (this.zzbsi == null) {
                this.zzbsi = zzfh("setChannel");
            }
            if (this.zzbsi == null) {
                Log.e("FirebaseMessaging", "Error while setting the notification channel");
            } else {
                try {
                    this.zzbsi.invoke(smallIcon, new Object[]{str2});
                } catch (Throwable e) {
                    Log.e("FirebaseMessaging", "Error while setting the notification channel", e);
                }
            }
        }
        return smallIcon.build();
    }

    static String zza(Bundle bundle, String str) {
        String string = bundle.getString(str);
        return string == null ? bundle.getString(str.replace("gcm.n.", "gcm.notification.")) : string;
    }

    private static void zza(Intent intent, Bundle bundle) {
        for (String str : bundle.keySet()) {
            if (str.startsWith("google.c.a.") || str.equals("from")) {
                intent.putExtra(str, bundle.getString(str));
            }
        }
    }

    @TargetApi(26)
    private final boolean zzaf(int i) {
        if (VERSION.SDK_INT != 26) {
            return true;
        }
        try {
            if (!(this.zzqs.getResources().getDrawable(i, null) instanceof AdaptiveIconDrawable)) {
                return true;
            }
            StringBuilder stringBuilder = new StringBuilder(77);
            stringBuilder.append("Adaptive icons cannot be used in notifications. Ignoring icon id: ");
            stringBuilder.append(i);
            Log.e("FirebaseMessaging", stringBuilder.toString());
            return false;
        } catch (NotFoundException e) {
            return false;
        }
    }

    static String zzb(Bundle bundle, String str) {
        str = String.valueOf(str);
        String valueOf = String.valueOf("_loc_key");
        return zza(bundle, valueOf.length() != 0 ? str.concat(valueOf) : new String(str));
    }

    static Object[] zzc(Bundle bundle, String str) {
        String valueOf = String.valueOf(str);
        String valueOf2 = String.valueOf("_loc_args");
        String zza = zza(bundle, valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf));
        if (TextUtils.isEmpty(zza)) {
            return null;
        }
        try {
            JSONArray jSONArray = new JSONArray(zza);
            String[] strArr = new String[jSONArray.length()];
            for (int i = 0; i < strArr.length; i++) {
                strArr[i] = jSONArray.opt(i);
            }
            return strArr;
        } catch (JSONException e) {
            valueOf = "FirebaseMessaging";
            str = String.valueOf(str);
            String valueOf3 = String.valueOf("_loc_args");
            str = (valueOf3.length() != 0 ? str.concat(valueOf3) : new String(str)).substring(6);
            StringBuilder stringBuilder = new StringBuilder((41 + String.valueOf(str).length()) + String.valueOf(zza).length());
            stringBuilder.append("Malformed ");
            stringBuilder.append(str);
            stringBuilder.append(": ");
            stringBuilder.append(zza);
            stringBuilder.append("  Default value will be used.");
            Log.w(valueOf, stringBuilder.toString());
            return null;
        }
    }

    private final String zzd(Bundle bundle, String str) {
        Object zza = zza(bundle, str);
        if (!TextUtils.isEmpty(zza)) {
            return zza;
        }
        String zzb = zzb(bundle, str);
        if (TextUtils.isEmpty(zzb)) {
            return null;
        }
        Resources resources = this.zzqs.getResources();
        int identifier = resources.getIdentifier(zzb, "string", this.zzqs.getPackageName());
        if (identifier == 0) {
            String str2 = "FirebaseMessaging";
            str = String.valueOf(str);
            String valueOf = String.valueOf("_loc_key");
            str = (valueOf.length() != 0 ? str.concat(valueOf) : new String(str)).substring(6);
            StringBuilder stringBuilder = new StringBuilder((49 + String.valueOf(str).length()) + String.valueOf(zzb).length());
            stringBuilder.append(str);
            stringBuilder.append(" resource not found: ");
            stringBuilder.append(zzb);
            stringBuilder.append(" Default value will be used.");
            Log.w(str2, stringBuilder.toString());
            return null;
        }
        Object[] zzc = zzc(bundle, str);
        if (zzc == null) {
            return resources.getString(identifier);
        }
        try {
            return resources.getString(identifier, zzc);
        } catch (Throwable e) {
            str2 = Arrays.toString(zzc);
            StringBuilder stringBuilder2 = new StringBuilder((58 + String.valueOf(zzb).length()) + String.valueOf(str2).length());
            stringBuilder2.append("Missing format argument for ");
            stringBuilder2.append(zzb);
            stringBuilder2.append(": ");
            stringBuilder2.append(str2);
            stringBuilder2.append(" Default value will be used.");
            Log.w("FirebaseMessaging", stringBuilder2.toString(), e);
            return null;
        }
    }

    @TargetApi(26)
    private static Method zzfh(String str) {
        try {
            return Builder.class.getMethod(str, new Class[]{String.class});
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    private final Integer zzfi(String str) {
        if (VERSION.SDK_INT < 21) {
            return null;
        }
        if (!TextUtils.isEmpty(str)) {
            try {
                return Integer.valueOf(Color.parseColor(str));
            } catch (IllegalArgumentException e) {
                StringBuilder stringBuilder = new StringBuilder(54 + String.valueOf(str).length());
                stringBuilder.append("Color ");
                stringBuilder.append(str);
                stringBuilder.append(" not valid. Notification will use default color.");
                Log.w("FirebaseMessaging", stringBuilder.toString());
            }
        }
        int i = zzti().getInt("com.google.firebase.messaging.default_notification_color", 0);
        if (i != 0) {
            try {
                return Integer.valueOf(ContextCompat.getColor(this.zzqs, i));
            } catch (NotFoundException e2) {
                Log.w("FirebaseMessaging", "Cannot find the color resource referenced in AndroidManifest.");
            }
        }
        return null;
    }

    @TargetApi(26)
    private final String zzfj(String str) {
        if (!PlatformVersion.isAtLeastO()) {
            return null;
        }
        NotificationManager notificationManager = (NotificationManager) this.zzqs.getSystemService(NotificationManager.class);
        try {
            String str2;
            if (this.zzbsj == null) {
                this.zzbsj = notificationManager.getClass().getMethod("getNotificationChannel", new Class[]{String.class});
            }
            if (!TextUtils.isEmpty(str)) {
                if (this.zzbsj.invoke(notificationManager, new Object[]{str}) != null) {
                    return str;
                }
                StringBuilder stringBuilder = new StringBuilder(122 + String.valueOf(str).length());
                stringBuilder.append("Notification Channel requested (");
                stringBuilder.append(str);
                stringBuilder.append(") has not been created by the app. Manifest configuration, or default, value will be used.");
                Log.w("FirebaseMessaging", stringBuilder.toString());
            }
            Object string = zzti().getString("com.google.firebase.messaging.default_notification_channel_id");
            if (TextUtils.isEmpty(string)) {
                str = "FirebaseMessaging";
                str2 = "Missing Default Notification Channel metadata in AndroidManifest. Default value will be used.";
            } else {
                if (this.zzbsj.invoke(notificationManager, new Object[]{string}) != null) {
                    return string;
                }
                str = "FirebaseMessaging";
                str2 = "Notification Channel set in AndroidManifest.xml has not been created by the app. Default value will be used.";
            }
            Log.w(str, str2);
            if (this.zzbsj.invoke(notificationManager, new Object[]{"fcm_fallback_notification_channel"}) == null) {
                Object newInstance = Class.forName("android.app.NotificationChannel").getConstructor(new Class[]{String.class, CharSequence.class, Integer.TYPE}).newInstance(new Object[]{"fcm_fallback_notification_channel", this.zzqs.getString(R.string.fcm_fallback_notification_channel_label), Integer.valueOf(3)});
                notificationManager.getClass().getMethod("createNotificationChannel", new Class[]{r11}).invoke(notificationManager, new Object[]{newInstance});
            }
            return "fcm_fallback_notification_channel";
        } catch (Throwable e) {
            Log.e("FirebaseMessaging", "Error while setting the notification channel", e);
            return null;
        }
    }

    static boolean zzl(Bundle bundle) {
        if (!"1".equals(zza(bundle, "gcm.n.e"))) {
            if (zza(bundle, "gcm.n.icon") == null) {
                return false;
            }
        }
        return true;
    }

    static Uri zzm(Bundle bundle) {
        Object zza = zza(bundle, "gcm.n.link_android");
        if (TextUtils.isEmpty(zza)) {
            zza = zza(bundle, "gcm.n.link");
        }
        return !TextUtils.isEmpty(zza) ? Uri.parse(zza) : null;
    }

    static String zzo(Bundle bundle) {
        Object zza = zza(bundle, "gcm.n.sound2");
        return TextUtils.isEmpty(zza) ? zza(bundle, "gcm.n.sound") : zza;
    }

    private final Bundle zzti() {
        if (this.zzbsh != null) {
            return this.zzbsh;
        }
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = this.zzqs.getPackageManager().getApplicationInfo(this.zzqs.getPackageName(), 128);
        } catch (NameNotFoundException e) {
        }
        if (applicationInfo == null || applicationInfo.metaData == null) {
            return Bundle.EMPTY;
        }
        this.zzbsh = applicationInfo.metaData;
        return this.zzbsh;
    }

    static synchronized zza zzw(Context context) {
        zza com_google_firebase_messaging_zza;
        synchronized (zza.class) {
            if (zzbsg == null) {
                zzbsg = new zza(context);
            }
            com_google_firebase_messaging_zza = zzbsg;
        }
        return com_google_firebase_messaging_zza;
    }

    final boolean zzn(Bundle bundle) {
        if ("1".equals(zza(bundle, "gcm.n.noui"))) {
            return true;
        }
        int myPid;
        boolean z;
        CharSequence zzd;
        CharSequence charSequence;
        CharSequence zzd2;
        String zza;
        Resources resources;
        int identifier;
        Integer zzfi;
        Uri uri;
        Object zza2;
        Uri zzm;
        Intent launchIntentForPackage;
        Bundle bundle2;
        PendingIntent activity;
        Intent intent;
        PendingIntent zza3;
        PendingIntent zza4;
        NotificationCompat.Builder smallIcon;
        Notification build;
        String zza5;
        NotificationManager notificationManager;
        long uptimeMillis;
        StringBuilder stringBuilder;
        int identifier2;
        if (!((KeyguardManager) this.zzqs.getSystemService("keyguard")).inKeyguardRestrictedInputMode()) {
            if (!PlatformVersion.isAtLeastLollipop()) {
                SystemClock.sleep(10);
            }
            myPid = Process.myPid();
            List<RunningAppProcessInfo> runningAppProcesses = ((ActivityManager) this.zzqs.getSystemService("activity")).getRunningAppProcesses();
            if (runningAppProcesses != null) {
                for (RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
                    if (runningAppProcessInfo.pid == myPid) {
                        if (runningAppProcessInfo.importance == 100) {
                            z = true;
                            if (z) {
                                return false;
                            }
                            zzd = zzd(bundle, "gcm.n.title");
                            if (TextUtils.isEmpty(zzd)) {
                                zzd = this.zzqs.getApplicationInfo().loadLabel(this.zzqs.getPackageManager());
                            }
                            charSequence = zzd;
                            zzd2 = zzd(bundle, "gcm.n.body");
                            zza = zza(bundle, "gcm.n.icon");
                            if (!TextUtils.isEmpty(zza)) {
                                resources = this.zzqs.getResources();
                                identifier = resources.getIdentifier(zza, "drawable", this.zzqs.getPackageName());
                                if (identifier == 0 && zzaf(identifier)) {
                                    zzfi = zzfi(zza(bundle, "gcm.n.color"));
                                    zza = zzo(bundle);
                                    if (TextUtils.isEmpty(zza)) {
                                        uri = null;
                                    } else {
                                        if (!"default".equals(zza)) {
                                        }
                                        uri = RingtoneManager.getDefaultUri(2);
                                    }
                                    zza2 = zza(bundle, "gcm.n.click_action");
                                    if (TextUtils.isEmpty(zza2)) {
                                        zzm = zzm(bundle);
                                        if (zzm == null) {
                                            launchIntentForPackage = this.zzqs.getPackageManager().getLaunchIntentForPackage(this.zzqs.getPackageName());
                                            if (launchIntentForPackage == null) {
                                                Log.w("FirebaseMessaging", "No activity found to launch app");
                                            }
                                        } else {
                                            launchIntentForPackage = new Intent("android.intent.action.VIEW");
                                            launchIntentForPackage.setPackage(this.zzqs.getPackageName());
                                            launchIntentForPackage.setData(zzm);
                                        }
                                    } else {
                                        launchIntentForPackage = new Intent(zza2);
                                        launchIntentForPackage.setPackage(this.zzqs.getPackageName());
                                        launchIntentForPackage.setFlags(268435456);
                                    }
                                    if (launchIntentForPackage != null) {
                                        launchIntentForPackage.addFlags(ConnectionsManager.FileTypeFile);
                                        bundle2 = new Bundle(bundle);
                                        FirebaseMessagingService.zzp(bundle2);
                                        launchIntentForPackage.putExtras(bundle2);
                                        for (String str : bundle2.keySet()) {
                                            if (!str.startsWith("gcm.n.")) {
                                            }
                                            launchIntentForPackage.removeExtra(str);
                                        }
                                        activity = PendingIntent.getActivity(this.zzqs, this.zzbsk.incrementAndGet(), launchIntentForPackage, 1073741824);
                                    } else {
                                        activity = null;
                                    }
                                    if (FirebaseMessagingService.zzq(bundle)) {
                                        intent = new Intent("com.google.firebase.messaging.NOTIFICATION_OPEN");
                                        zza(intent, bundle);
                                        intent.putExtra("pending_intent", activity);
                                        zza3 = zzz.zza(this.zzqs, this.zzbsk.incrementAndGet(), intent, 1073741824);
                                        launchIntentForPackage = new Intent("com.google.firebase.messaging.NOTIFICATION_DISMISS");
                                        zza(launchIntentForPackage, bundle);
                                        zza4 = zzz.zza(this.zzqs, this.zzbsk.incrementAndGet(), launchIntentForPackage, 1073741824);
                                        activity = zza3;
                                    } else {
                                        zza4 = null;
                                    }
                                    if (PlatformVersion.isAtLeastO()) {
                                    }
                                    smallIcon = new NotificationCompat.Builder(this.zzqs).setAutoCancel(true).setSmallIcon(identifier);
                                    if (!TextUtils.isEmpty(charSequence)) {
                                        smallIcon.setContentTitle(charSequence);
                                    }
                                    if (!TextUtils.isEmpty(zzd2)) {
                                        smallIcon.setContentText(zzd2);
                                        smallIcon.setStyle(new NotificationCompat.BigTextStyle().bigText(zzd2));
                                    }
                                    if (zzfi != null) {
                                        smallIcon.setColor(zzfi.intValue());
                                    }
                                    if (uri != null) {
                                        smallIcon.setSound(uri);
                                    }
                                    if (activity != null) {
                                        smallIcon.setContentIntent(activity);
                                    }
                                    if (zza4 != null) {
                                        smallIcon.setDeleteIntent(zza4);
                                    }
                                    build = smallIcon.build();
                                    zza5 = zza(bundle, "gcm.n.tag");
                                    if (Log.isLoggable("FirebaseMessaging", 3)) {
                                        Log.d("FirebaseMessaging", "Showing notification");
                                    }
                                    notificationManager = (NotificationManager) this.zzqs.getSystemService("notification");
                                    if (TextUtils.isEmpty(zza5)) {
                                        uptimeMillis = SystemClock.uptimeMillis();
                                        stringBuilder = new StringBuilder(37);
                                        stringBuilder.append("FCM-Notification:");
                                        stringBuilder.append(uptimeMillis);
                                        zza5 = stringBuilder.toString();
                                    }
                                    notificationManager.notify(zza5, 0, build);
                                    return true;
                                }
                                identifier2 = resources.getIdentifier(zza, "mipmap", this.zzqs.getPackageName());
                                if (identifier2 == 0 && zzaf(identifier2)) {
                                    identifier = identifier2;
                                    zzfi = zzfi(zza(bundle, "gcm.n.color"));
                                    zza = zzo(bundle);
                                    if (TextUtils.isEmpty(zza)) {
                                        uri = null;
                                    } else if ("default".equals(zza) || this.zzqs.getResources().getIdentifier(zza, "raw", this.zzqs.getPackageName()) == 0) {
                                        uri = RingtoneManager.getDefaultUri(2);
                                    } else {
                                        String packageName = this.zzqs.getPackageName();
                                        StringBuilder stringBuilder2 = new StringBuilder((24 + String.valueOf(packageName).length()) + String.valueOf(zza).length());
                                        stringBuilder2.append("android.resource://");
                                        stringBuilder2.append(packageName);
                                        stringBuilder2.append("/raw/");
                                        stringBuilder2.append(zza);
                                        uri = Uri.parse(stringBuilder2.toString());
                                    }
                                    zza2 = zza(bundle, "gcm.n.click_action");
                                    if (TextUtils.isEmpty(zza2)) {
                                        launchIntentForPackage = new Intent(zza2);
                                        launchIntentForPackage.setPackage(this.zzqs.getPackageName());
                                        launchIntentForPackage.setFlags(268435456);
                                    } else {
                                        zzm = zzm(bundle);
                                        if (zzm == null) {
                                            launchIntentForPackage = new Intent("android.intent.action.VIEW");
                                            launchIntentForPackage.setPackage(this.zzqs.getPackageName());
                                            launchIntentForPackage.setData(zzm);
                                        } else {
                                            launchIntentForPackage = this.zzqs.getPackageManager().getLaunchIntentForPackage(this.zzqs.getPackageName());
                                            if (launchIntentForPackage == null) {
                                                Log.w("FirebaseMessaging", "No activity found to launch app");
                                            }
                                        }
                                    }
                                    if (launchIntentForPackage != null) {
                                        activity = null;
                                    } else {
                                        launchIntentForPackage.addFlags(ConnectionsManager.FileTypeFile);
                                        bundle2 = new Bundle(bundle);
                                        FirebaseMessagingService.zzp(bundle2);
                                        launchIntentForPackage.putExtras(bundle2);
                                        for (String str2 : bundle2.keySet()) {
                                            if (str2.startsWith("gcm.n.") || str2.startsWith("gcm.notification.")) {
                                                launchIntentForPackage.removeExtra(str2);
                                            }
                                        }
                                        activity = PendingIntent.getActivity(this.zzqs, this.zzbsk.incrementAndGet(), launchIntentForPackage, 1073741824);
                                    }
                                    if (FirebaseMessagingService.zzq(bundle)) {
                                        intent = new Intent("com.google.firebase.messaging.NOTIFICATION_OPEN");
                                        zza(intent, bundle);
                                        intent.putExtra("pending_intent", activity);
                                        zza3 = zzz.zza(this.zzqs, this.zzbsk.incrementAndGet(), intent, 1073741824);
                                        launchIntentForPackage = new Intent("com.google.firebase.messaging.NOTIFICATION_DISMISS");
                                        zza(launchIntentForPackage, bundle);
                                        zza4 = zzz.zza(this.zzqs, this.zzbsk.incrementAndGet(), launchIntentForPackage, 1073741824);
                                        activity = zza3;
                                    } else {
                                        zza4 = null;
                                    }
                                    if (PlatformVersion.isAtLeastO() || this.zzqs.getApplicationInfo().targetSdkVersion <= 25) {
                                        smallIcon = new NotificationCompat.Builder(this.zzqs).setAutoCancel(true).setSmallIcon(identifier);
                                        if (TextUtils.isEmpty(charSequence)) {
                                            smallIcon.setContentTitle(charSequence);
                                        }
                                        if (TextUtils.isEmpty(zzd2)) {
                                            smallIcon.setContentText(zzd2);
                                            smallIcon.setStyle(new NotificationCompat.BigTextStyle().bigText(zzd2));
                                        }
                                        if (zzfi != null) {
                                            smallIcon.setColor(zzfi.intValue());
                                        }
                                        if (uri != null) {
                                            smallIcon.setSound(uri);
                                        }
                                        if (activity != null) {
                                            smallIcon.setContentIntent(activity);
                                        }
                                        if (zza4 != null) {
                                            smallIcon.setDeleteIntent(zza4);
                                        }
                                        build = smallIcon.build();
                                    } else {
                                        build = zza(charSequence, zzd2, identifier, zzfi, uri, activity, zza4, zzfj(zza(bundle, "gcm.n.android_channel_id")));
                                    }
                                    zza5 = zza(bundle, "gcm.n.tag");
                                    if (Log.isLoggable("FirebaseMessaging", 3)) {
                                        Log.d("FirebaseMessaging", "Showing notification");
                                    }
                                    notificationManager = (NotificationManager) this.zzqs.getSystemService("notification");
                                    if (TextUtils.isEmpty(zza5)) {
                                        uptimeMillis = SystemClock.uptimeMillis();
                                        stringBuilder = new StringBuilder(37);
                                        stringBuilder.append("FCM-Notification:");
                                        stringBuilder.append(uptimeMillis);
                                        zza5 = stringBuilder.toString();
                                    }
                                    notificationManager.notify(zza5, 0, build);
                                    return true;
                                }
                                StringBuilder stringBuilder3 = new StringBuilder(61 + String.valueOf(zza).length());
                                stringBuilder3.append("Icon resource ");
                                stringBuilder3.append(zza);
                                stringBuilder3.append(" not found. Notification will use default icon.");
                                Log.w("FirebaseMessaging", stringBuilder3.toString());
                            }
                            myPid = zzti().getInt("com.google.firebase.messaging.default_notification_icon", 0);
                            if (myPid == 0 || !zzaf(myPid)) {
                                myPid = this.zzqs.getApplicationInfo().icon;
                            }
                            if (myPid == 0 || !zzaf(myPid)) {
                                myPid = 17301651;
                            }
                            identifier = myPid;
                            zzfi = zzfi(zza(bundle, "gcm.n.color"));
                            zza = zzo(bundle);
                            if (TextUtils.isEmpty(zza)) {
                                uri = null;
                            } else {
                                if ("default".equals(zza)) {
                                }
                                uri = RingtoneManager.getDefaultUri(2);
                            }
                            zza2 = zza(bundle, "gcm.n.click_action");
                            if (TextUtils.isEmpty(zza2)) {
                                launchIntentForPackage = new Intent(zza2);
                                launchIntentForPackage.setPackage(this.zzqs.getPackageName());
                                launchIntentForPackage.setFlags(268435456);
                            } else {
                                zzm = zzm(bundle);
                                if (zzm == null) {
                                    launchIntentForPackage = new Intent("android.intent.action.VIEW");
                                    launchIntentForPackage.setPackage(this.zzqs.getPackageName());
                                    launchIntentForPackage.setData(zzm);
                                } else {
                                    launchIntentForPackage = this.zzqs.getPackageManager().getLaunchIntentForPackage(this.zzqs.getPackageName());
                                    if (launchIntentForPackage == null) {
                                        Log.w("FirebaseMessaging", "No activity found to launch app");
                                    }
                                }
                            }
                            if (launchIntentForPackage != null) {
                                activity = null;
                            } else {
                                launchIntentForPackage.addFlags(ConnectionsManager.FileTypeFile);
                                bundle2 = new Bundle(bundle);
                                FirebaseMessagingService.zzp(bundle2);
                                launchIntentForPackage.putExtras(bundle2);
                                for (String str22 : bundle2.keySet()) {
                                    if (str22.startsWith("gcm.n.")) {
                                    }
                                    launchIntentForPackage.removeExtra(str22);
                                }
                                activity = PendingIntent.getActivity(this.zzqs, this.zzbsk.incrementAndGet(), launchIntentForPackage, 1073741824);
                            }
                            if (FirebaseMessagingService.zzq(bundle)) {
                                intent = new Intent("com.google.firebase.messaging.NOTIFICATION_OPEN");
                                zza(intent, bundle);
                                intent.putExtra("pending_intent", activity);
                                zza3 = zzz.zza(this.zzqs, this.zzbsk.incrementAndGet(), intent, 1073741824);
                                launchIntentForPackage = new Intent("com.google.firebase.messaging.NOTIFICATION_DISMISS");
                                zza(launchIntentForPackage, bundle);
                                zza4 = zzz.zza(this.zzqs, this.zzbsk.incrementAndGet(), launchIntentForPackage, 1073741824);
                                activity = zza3;
                            } else {
                                zza4 = null;
                            }
                            if (PlatformVersion.isAtLeastO()) {
                            }
                            smallIcon = new NotificationCompat.Builder(this.zzqs).setAutoCancel(true).setSmallIcon(identifier);
                            if (TextUtils.isEmpty(charSequence)) {
                                smallIcon.setContentTitle(charSequence);
                            }
                            if (TextUtils.isEmpty(zzd2)) {
                                smallIcon.setContentText(zzd2);
                                smallIcon.setStyle(new NotificationCompat.BigTextStyle().bigText(zzd2));
                            }
                            if (zzfi != null) {
                                smallIcon.setColor(zzfi.intValue());
                            }
                            if (uri != null) {
                                smallIcon.setSound(uri);
                            }
                            if (activity != null) {
                                smallIcon.setContentIntent(activity);
                            }
                            if (zza4 != null) {
                                smallIcon.setDeleteIntent(zza4);
                            }
                            build = smallIcon.build();
                            zza5 = zza(bundle, "gcm.n.tag");
                            if (Log.isLoggable("FirebaseMessaging", 3)) {
                                Log.d("FirebaseMessaging", "Showing notification");
                            }
                            notificationManager = (NotificationManager) this.zzqs.getSystemService("notification");
                            if (TextUtils.isEmpty(zza5)) {
                                uptimeMillis = SystemClock.uptimeMillis();
                                stringBuilder = new StringBuilder(37);
                                stringBuilder.append("FCM-Notification:");
                                stringBuilder.append(uptimeMillis);
                                zza5 = stringBuilder.toString();
                            }
                            notificationManager.notify(zza5, 0, build);
                            return true;
                        }
                    }
                }
            }
        }
        z = false;
        if (z) {
            return false;
        }
        zzd = zzd(bundle, "gcm.n.title");
        if (TextUtils.isEmpty(zzd)) {
            zzd = this.zzqs.getApplicationInfo().loadLabel(this.zzqs.getPackageManager());
        }
        charSequence = zzd;
        zzd2 = zzd(bundle, "gcm.n.body");
        zza = zza(bundle, "gcm.n.icon");
        if (TextUtils.isEmpty(zza)) {
            resources = this.zzqs.getResources();
            identifier = resources.getIdentifier(zza, "drawable", this.zzqs.getPackageName());
            if (identifier == 0) {
            }
            identifier2 = resources.getIdentifier(zza, "mipmap", this.zzqs.getPackageName());
            if (identifier2 == 0) {
            }
            StringBuilder stringBuilder32 = new StringBuilder(61 + String.valueOf(zza).length());
            stringBuilder32.append("Icon resource ");
            stringBuilder32.append(zza);
            stringBuilder32.append(" not found. Notification will use default icon.");
            Log.w("FirebaseMessaging", stringBuilder32.toString());
        }
        myPid = zzti().getInt("com.google.firebase.messaging.default_notification_icon", 0);
        myPid = this.zzqs.getApplicationInfo().icon;
        myPid = 17301651;
        identifier = myPid;
        zzfi = zzfi(zza(bundle, "gcm.n.color"));
        zza = zzo(bundle);
        if (TextUtils.isEmpty(zza)) {
            if ("default".equals(zza)) {
            }
            uri = RingtoneManager.getDefaultUri(2);
        } else {
            uri = null;
        }
        zza2 = zza(bundle, "gcm.n.click_action");
        if (TextUtils.isEmpty(zza2)) {
            zzm = zzm(bundle);
            if (zzm == null) {
                launchIntentForPackage = this.zzqs.getPackageManager().getLaunchIntentForPackage(this.zzqs.getPackageName());
                if (launchIntentForPackage == null) {
                    Log.w("FirebaseMessaging", "No activity found to launch app");
                }
            } else {
                launchIntentForPackage = new Intent("android.intent.action.VIEW");
                launchIntentForPackage.setPackage(this.zzqs.getPackageName());
                launchIntentForPackage.setData(zzm);
            }
        } else {
            launchIntentForPackage = new Intent(zza2);
            launchIntentForPackage.setPackage(this.zzqs.getPackageName());
            launchIntentForPackage.setFlags(268435456);
        }
        if (launchIntentForPackage != null) {
            launchIntentForPackage.addFlags(ConnectionsManager.FileTypeFile);
            bundle2 = new Bundle(bundle);
            FirebaseMessagingService.zzp(bundle2);
            launchIntentForPackage.putExtras(bundle2);
            for (String str222 : bundle2.keySet()) {
                if (str222.startsWith("gcm.n.")) {
                }
                launchIntentForPackage.removeExtra(str222);
            }
            activity = PendingIntent.getActivity(this.zzqs, this.zzbsk.incrementAndGet(), launchIntentForPackage, 1073741824);
        } else {
            activity = null;
        }
        if (FirebaseMessagingService.zzq(bundle)) {
            zza4 = null;
        } else {
            intent = new Intent("com.google.firebase.messaging.NOTIFICATION_OPEN");
            zza(intent, bundle);
            intent.putExtra("pending_intent", activity);
            zza3 = zzz.zza(this.zzqs, this.zzbsk.incrementAndGet(), intent, 1073741824);
            launchIntentForPackage = new Intent("com.google.firebase.messaging.NOTIFICATION_DISMISS");
            zza(launchIntentForPackage, bundle);
            zza4 = zzz.zza(this.zzqs, this.zzbsk.incrementAndGet(), launchIntentForPackage, 1073741824);
            activity = zza3;
        }
        if (PlatformVersion.isAtLeastO()) {
        }
        smallIcon = new NotificationCompat.Builder(this.zzqs).setAutoCancel(true).setSmallIcon(identifier);
        if (TextUtils.isEmpty(charSequence)) {
            smallIcon.setContentTitle(charSequence);
        }
        if (TextUtils.isEmpty(zzd2)) {
            smallIcon.setContentText(zzd2);
            smallIcon.setStyle(new NotificationCompat.BigTextStyle().bigText(zzd2));
        }
        if (zzfi != null) {
            smallIcon.setColor(zzfi.intValue());
        }
        if (uri != null) {
            smallIcon.setSound(uri);
        }
        if (activity != null) {
            smallIcon.setContentIntent(activity);
        }
        if (zza4 != null) {
            smallIcon.setDeleteIntent(zza4);
        }
        build = smallIcon.build();
        zza5 = zza(bundle, "gcm.n.tag");
        if (Log.isLoggable("FirebaseMessaging", 3)) {
            Log.d("FirebaseMessaging", "Showing notification");
        }
        notificationManager = (NotificationManager) this.zzqs.getSystemService("notification");
        if (TextUtils.isEmpty(zza5)) {
            uptimeMillis = SystemClock.uptimeMillis();
            stringBuilder = new StringBuilder(37);
            stringBuilder.append("FCM-Notification:");
            stringBuilder.append(uptimeMillis);
            zza5 = stringBuilder.toString();
        }
        notificationManager.notify(zza5, 0, build);
        return true;
    }
}
