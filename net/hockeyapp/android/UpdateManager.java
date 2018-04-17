package net.hockeyapp.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask.Status;
import android.os.Build.VERSION;
import android.text.TextUtils;
import java.lang.ref.WeakReference;
import java.util.Date;
import net.hockeyapp.android.tasks.CheckUpdateTask;
import net.hockeyapp.android.tasks.CheckUpdateTaskWithUI;
import net.hockeyapp.android.utils.AsyncTaskUtils;
import net.hockeyapp.android.utils.Util;

public class UpdateManager {
    private static CheckUpdateTask updateTask = null;

    public static void register(Activity activity, String appIdentifier) {
        register(activity, appIdentifier, true);
    }

    public static void register(Activity activity, String appIdentifier, boolean isDialogRequired) {
        register(activity, appIdentifier, null, isDialogRequired);
    }

    public static void register(Activity activity, String appIdentifier, UpdateManagerListener listener, boolean isDialogRequired) {
        register(activity, "https://sdk.hockeyapp.net/", appIdentifier, listener, isDialogRequired);
    }

    public static void register(Activity activity, String urlString, String appIdentifier, UpdateManagerListener listener, boolean isDialogRequired) {
        appIdentifier = Util.sanitizeAppIdentifier(appIdentifier);
        Constants.loadFromContext(activity);
        WeakReference<Activity> weakActivity = new WeakReference(activity);
        if (!(dialogShown(weakActivity) || checkExpiryDate(weakActivity, listener) || ((listener == null || !listener.canUpdateInMarket()) && installedFromMarket(weakActivity)))) {
            startUpdateTask(weakActivity, urlString, appIdentifier, listener, isDialogRequired);
        }
    }

    public static void unregister() {
        if (updateTask != null) {
            updateTask.cancel(true);
            updateTask.detach();
            updateTask = null;
        }
    }

    private static boolean checkExpiryDate(WeakReference<Activity> weakActivity, UpdateManagerListener listener) {
        boolean handle = false;
        boolean hasExpired = checkExpiryDateForBackground(listener);
        if (hasExpired) {
            handle = listener.onBuildExpired();
        }
        if (hasExpired && handle) {
            startExpiryInfoIntent(weakActivity);
        }
        return hasExpired;
    }

    private static boolean checkExpiryDateForBackground(UpdateManagerListener listener) {
        if (listener == null) {
            return false;
        }
        Date expiryDate = listener.getExpiryDate();
        boolean z = expiryDate != null && new Date().compareTo(expiryDate) > 0;
        return z;
    }

    protected static boolean installedFromMarket(WeakReference<? extends Context> weakContext) {
        boolean result = false;
        Context context = (Context) weakContext.get();
        if (context != null) {
            try {
                String installer = context.getPackageManager().getInstallerPackageName(context.getPackageName());
                if (!TextUtils.isEmpty(installer)) {
                    result = true;
                    if (VERSION.SDK_INT >= 24 && (TextUtils.equals(installer, "com.google.android.packageinstaller") || TextUtils.equals(installer, "com.android.packageinstaller"))) {
                        result = false;
                    }
                    if (TextUtils.equals(installer, "adb")) {
                        result = false;
                    }
                }
            } catch (Throwable th) {
            }
        }
        return result;
    }

    private static void startExpiryInfoIntent(WeakReference<Activity> weakActivity) {
        if (weakActivity != null) {
            Activity activity = (Activity) weakActivity.get();
            if (activity != null) {
                activity.finish();
                Intent intent = new Intent(activity, ExpiryInfoActivity.class);
                intent.addFlags(335544320);
                activity.startActivity(intent);
            }
        }
    }

    private static void startUpdateTask(WeakReference<Activity> weakActivity, String urlString, String appIdentifier, UpdateManagerListener listener, boolean isDialogRequired) {
        if (updateTask != null) {
            if (updateTask.getStatus() != Status.FINISHED) {
                updateTask.attach(weakActivity);
                return;
            }
        }
        updateTask = new CheckUpdateTaskWithUI(weakActivity, urlString, appIdentifier, listener, isDialogRequired);
        AsyncTaskUtils.execute(updateTask);
    }

    private static boolean dialogShown(WeakReference<Activity> weakActivity) {
        boolean z = false;
        if (weakActivity != null) {
            Activity activity = (Activity) weakActivity.get();
            if (activity != null) {
                if (activity.getFragmentManager().findFragmentByTag(UpdateFragment.FRAGMENT_TAG) != null) {
                    z = true;
                }
                return z;
            }
        }
        return false;
    }
}
