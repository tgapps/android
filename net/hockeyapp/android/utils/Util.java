package net.hockeyapp.android.utils;

import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.hockeyapp.android.R;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.exoplayer2.C;

public class Util {
    private static final ThreadLocal<DateFormat> DATE_FORMAT_THREAD_LOCAL = new ThreadLocal<DateFormat>() {
        protected DateFormat initialValue() {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            return dateFormat;
        }
    };
    private static final Pattern appIdentifierPattern = Pattern.compile("[0-9a-f]+", 2);

    public static java.lang.String convertStreamToString(java.io.InputStream r1) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: net.hockeyapp.android.utils.Util.convertStreamToString(java.io.InputStream):java.lang.String
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
        /*
        r0 = new java.io.BufferedReader;
        r1 = new java.io.InputStreamReader;
        r1.<init>(r4);
        r2 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        r0.<init>(r1, r2);
        r1 = new java.lang.StringBuilder;
        r1.<init>();
    L_0x0011:
        r2 = r0.readLine();	 Catch:{ IOException -> 0x0029 }
        r3 = r2;	 Catch:{ IOException -> 0x0029 }
        if (r2 == 0) goto L_0x0021;	 Catch:{ IOException -> 0x0029 }
    L_0x0018:
        r1.append(r3);	 Catch:{ IOException -> 0x0029 }
        r2 = 10;	 Catch:{ IOException -> 0x0029 }
        r1.append(r2);	 Catch:{ IOException -> 0x0029 }
        goto L_0x0011;
    L_0x0021:
        r4.close();	 Catch:{ IOException -> 0x0025 }
        goto L_0x0033;
    L_0x0025:
        r2 = move-exception;
        goto L_0x0033;
    L_0x0027:
        r2 = move-exception;
        goto L_0x0038;
    L_0x0029:
        r2 = move-exception;
        r3 = "Failed to convert stream to string";	 Catch:{ all -> 0x0027 }
        net.hockeyapp.android.utils.HockeyLog.error(r3, r2);	 Catch:{ all -> 0x0027 }
        r4.close();	 Catch:{ IOException -> 0x0025 }
        goto L_0x0024;
    L_0x0033:
        r2 = r1.toString();
        return r2;
        r4.close();	 Catch:{ IOException -> 0x003d }
        goto L_0x003e;
    L_0x003d:
        r3 = move-exception;
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: net.hockeyapp.android.utils.Util.convertStreamToString(java.io.InputStream):java.lang.String");
    }

    public static String encodeParam(String param) {
        try {
            return URLEncoder.encode(param, C.UTF8_NAME);
        } catch (Throwable e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Failed to encode param ");
            stringBuilder.append(param);
            HockeyLog.error(stringBuilder.toString(), e);
            return TtmlNode.ANONYMOUS_REGION_ID;
        }
    }

    public static boolean isValidEmail(String value) {
        return !TextUtils.isEmpty(value) && Patterns.EMAIL_ADDRESS.matcher(value).matches();
    }

    public static Boolean runsOnTablet(Context context) {
        boolean z = false;
        if (context == null) {
            return Boolean.valueOf(false);
        }
        Configuration configuration = context.getResources().getConfiguration();
        if ((configuration.screenLayout & 15) != 3) {
            if ((configuration.screenLayout & 15) != 4) {
                return Boolean.valueOf(z);
            }
        }
        z = true;
        return Boolean.valueOf(z);
    }

    public static String sanitizeAppIdentifier(String appIdentifier) throws IllegalArgumentException {
        if (appIdentifier == null) {
            throw new IllegalArgumentException("App ID must not be null.");
        }
        String sAppIdentifier = appIdentifier.trim();
        Matcher matcher = appIdentifierPattern.matcher(sAppIdentifier);
        if (sAppIdentifier.length() != 32) {
            throw new IllegalArgumentException("App ID length must be 32 characters.");
        } else if (matcher.matches()) {
            return sAppIdentifier;
        } else {
            throw new IllegalArgumentException("App ID must match regex pattern /[0-9a-f]+/i");
        }
    }

    public static Notification createNotification(Context context, PendingIntent pendingIntent, String title, String text, int iconId, String channelId) {
        Builder builder;
        if (VERSION.SDK_INT >= 26) {
            builder = new Builder(context, channelId);
        } else {
            builder = new Builder(context);
        }
        builder.setContentTitle(title).setContentText(text).setContentIntent(pendingIntent).setSmallIcon(iconId);
        if (VERSION.SDK_INT >= 16) {
            return builder.build();
        }
        return builder.getNotification();
    }

    public static void sendNotification(Context context, int id, Notification notification, String channelId, CharSequence channelName) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
        if (VERSION.SDK_INT >= 26) {
            notificationManager.createNotificationChannel(new NotificationChannel(channelId, channelName, 3));
        }
        notificationManager.notify(id, notification);
    }

    public static void cancelNotification(Context context, int id) {
        ((NotificationManager) context.getSystemService("notification")).cancel(id);
    }

    public static void announceForAccessibility(View view, CharSequence text) {
        AccessibilityManager manager = (AccessibilityManager) view.getContext().getSystemService("accessibility");
        if (manager.isEnabled()) {
            int eventType;
            if (VERSION.SDK_INT < 16) {
                eventType = 8;
            } else {
                eventType = MessagesController.UPDATE_MASK_CHAT_ADMINS;
            }
            AccessibilityEvent event = AccessibilityEvent.obtain(eventType);
            event.getText().add(text);
            event.setSource(view);
            event.setEnabled(view.isEnabled());
            event.setClassName(view.getClass().getName());
            event.setPackageName(view.getContext().getPackageName());
            manager.sendAccessibilityEvent(event);
        }
    }

    public static boolean isConnectedToNetwork(Context context) {
        boolean z = false;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService("connectivity");
            if (connectivityManager == null) {
                return false;
            }
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            if (activeNetwork != null && activeNetwork.isConnected()) {
                z = true;
            }
            return z;
        } catch (Throwable e) {
            HockeyLog.error("Exception thrown when check network is connected", e);
        }
    }

    public static String getAppName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = packageManager.getApplicationInfo(context.getApplicationInfo().packageName, 0);
        } catch (NameNotFoundException e) {
        }
        if (applicationInfo != null) {
            return (String) packageManager.getApplicationLabel(applicationInfo);
        }
        return context.getString(R.string.hockeyapp_crash_dialog_app_name_fallback);
    }

    public static byte[] hash(byte[] bytes, String algorithm) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        digest.update(bytes);
        return digest.digest();
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte aMessageDigest : bytes) {
            String h = Integer.toHexString(255 & aMessageDigest);
            while (h.length() < 2) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("0");
                stringBuilder.append(h);
                h = stringBuilder.toString();
            }
            hexString.append(h);
        }
        return hexString.toString();
    }
}
