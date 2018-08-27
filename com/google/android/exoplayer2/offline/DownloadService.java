package com.google.android.exoplayer2.offline;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import com.google.android.exoplayer2.offline.DownloadManager.Listener;
import com.google.android.exoplayer2.offline.DownloadManager.TaskState;
import com.google.android.exoplayer2.scheduler.Requirements;
import com.google.android.exoplayer2.scheduler.RequirementsWatcher;
import com.google.android.exoplayer2.scheduler.Scheduler;
import com.google.android.exoplayer2.util.NotificationUtil;
import com.google.android.exoplayer2.util.Util;
import java.util.HashMap;

public abstract class DownloadService extends Service {
    public static final String ACTION_ADD = "com.google.android.exoplayer.downloadService.action.ADD";
    public static final String ACTION_INIT = "com.google.android.exoplayer.downloadService.action.INIT";
    public static final String ACTION_RELOAD_REQUIREMENTS = "com.google.android.exoplayer.downloadService.action.RELOAD_REQUIREMENTS";
    private static final String ACTION_RESTART = "com.google.android.exoplayer.downloadService.action.RESTART";
    private static final boolean DEBUG = false;
    public static final long DEFAULT_FOREGROUND_NOTIFICATION_UPDATE_INTERVAL = 1000;
    private static final Requirements DEFAULT_REQUIREMENTS = new Requirements(1, false, false);
    public static final int FOREGROUND_NOTIFICATION_ID_NONE = 0;
    public static final String KEY_DOWNLOAD_ACTION = "download_action";
    public static final String KEY_FOREGROUND = "foreground";
    private static final String TAG = "DownloadService";
    private static final HashMap<Class<? extends DownloadService>, RequirementsHelper> requirementsHelpers = new HashMap();
    private final String channelId;
    private final int channelName;
    private DownloadManager downloadManager;
    private DownloadManagerListener downloadManagerListener;
    private final ForegroundNotificationUpdater foregroundNotificationUpdater;
    private int lastStartId;
    private boolean startedInForeground;
    private boolean taskRemoved;

    private final class ForegroundNotificationUpdater implements Runnable {
        private final Handler handler = new Handler(Looper.getMainLooper());
        private boolean notificationDisplayed;
        private final int notificationId;
        private boolean periodicUpdatesStarted;
        private final long updateInterval;

        public ForegroundNotificationUpdater(int notificationId, long updateInterval) {
            this.notificationId = notificationId;
            this.updateInterval = updateInterval;
        }

        public void startPeriodicUpdates() {
            this.periodicUpdatesStarted = true;
            update();
        }

        public void stopPeriodicUpdates() {
            this.periodicUpdatesStarted = false;
            this.handler.removeCallbacks(this);
        }

        public void update() {
            DownloadService.this.startForeground(this.notificationId, DownloadService.this.getForegroundNotification(DownloadService.this.downloadManager.getAllTaskStates()));
            this.notificationDisplayed = true;
            if (this.periodicUpdatesStarted) {
                this.handler.removeCallbacks(this);
                this.handler.postDelayed(this, this.updateInterval);
            }
        }

        public void showNotificationIfNotAlready() {
            if (!this.notificationDisplayed) {
                update();
            }
        }

        public void run() {
            update();
        }
    }

    private final class DownloadManagerListener implements Listener {
        private DownloadManagerListener() {
        }

        public void onInitialized(DownloadManager downloadManager) {
            DownloadService.this.maybeStartWatchingRequirements(DownloadService.this.getRequirements());
        }

        public void onTaskStateChanged(DownloadManager downloadManager, TaskState taskState) {
            DownloadService.this.onTaskStateChanged(taskState);
            if (DownloadService.this.foregroundNotificationUpdater == null) {
                return;
            }
            if (taskState.state == 1) {
                DownloadService.this.foregroundNotificationUpdater.startPeriodicUpdates();
            } else {
                DownloadService.this.foregroundNotificationUpdater.update();
            }
        }

        public final void onIdle(DownloadManager downloadManager) {
            DownloadService.this.stop();
        }
    }

    private static final class RequirementsHelper implements RequirementsWatcher.Listener {
        private final Context context;
        private final Requirements requirements;
        private final RequirementsWatcher requirementsWatcher;
        private final Scheduler scheduler;
        private final Class<? extends DownloadService> serviceClass;

        private RequirementsHelper(Context context, Requirements requirements, Scheduler scheduler, Class<? extends DownloadService> serviceClass) {
            this.context = context;
            this.requirements = requirements;
            this.scheduler = scheduler;
            this.serviceClass = serviceClass;
            this.requirementsWatcher = new RequirementsWatcher(context, this, requirements);
        }

        public void start() {
            this.requirementsWatcher.start();
        }

        public void stop() {
            this.requirementsWatcher.stop();
            if (this.scheduler != null) {
                this.scheduler.cancel();
            }
        }

        public void requirementsMet(RequirementsWatcher requirementsWatcher) {
            try {
                notifyService();
                if (this.scheduler != null) {
                    this.scheduler.cancel();
                }
            } catch (Exception e) {
            }
        }

        public void requirementsNotMet(RequirementsWatcher requirementsWatcher) {
            try {
                notifyService();
            } catch (Exception e) {
            }
            if (this.scheduler != null) {
                if (!this.scheduler.schedule(this.requirements, this.context.getPackageName(), DownloadService.ACTION_RESTART)) {
                    Log.e(DownloadService.TAG, "Scheduling downloads failed.");
                }
            }
        }

        private void notifyService() throws Exception {
            try {
                this.context.startService(DownloadService.getIntent(this.context, this.serviceClass, DownloadService.ACTION_INIT));
            } catch (IllegalStateException e) {
                throw new Exception(e);
            }
        }
    }

    protected abstract DownloadManager getDownloadManager();

    protected abstract Scheduler getScheduler();

    protected DownloadService(int foregroundNotificationId) {
        this(foregroundNotificationId, 1000);
    }

    protected DownloadService(int foregroundNotificationId, long foregroundNotificationUpdateInterval) {
        this(foregroundNotificationId, foregroundNotificationUpdateInterval, null, 0);
    }

    protected DownloadService(int foregroundNotificationId, long foregroundNotificationUpdateInterval, String channelId, int channelName) {
        this.foregroundNotificationUpdater = foregroundNotificationId == 0 ? null : new ForegroundNotificationUpdater(foregroundNotificationId, foregroundNotificationUpdateInterval);
        this.channelId = channelId;
        this.channelName = channelName;
    }

    public static Intent buildAddActionIntent(Context context, Class<? extends DownloadService> clazz, DownloadAction downloadAction, boolean foreground) {
        return getIntent(context, clazz, ACTION_ADD).putExtra(KEY_DOWNLOAD_ACTION, downloadAction.toByteArray()).putExtra(KEY_FOREGROUND, foreground);
    }

    public static void startWithAction(Context context, Class<? extends DownloadService> clazz, DownloadAction downloadAction, boolean foreground) {
        Intent intent = buildAddActionIntent(context, clazz, downloadAction, foreground);
        if (foreground) {
            Util.startForegroundService(context, intent);
        } else {
            context.startService(intent);
        }
    }

    public static void start(Context context, Class<? extends DownloadService> clazz) {
        context.startService(getIntent(context, clazz, ACTION_INIT));
    }

    public static void startForeground(Context context, Class<? extends DownloadService> clazz) {
        Util.startForegroundService(context, getIntent(context, clazz, ACTION_INIT).putExtra(KEY_FOREGROUND, true));
    }

    public void onCreate() {
        logd("onCreate");
        if (this.channelId != null) {
            NotificationUtil.createNotificationChannel(this, this.channelId, this.channelName, 2);
        }
        this.downloadManager = getDownloadManager();
        this.downloadManagerListener = new DownloadManagerListener();
        this.downloadManager.addListener(this.downloadManagerListener);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int onStartCommand(android.content.Intent r9, int r10, int r11) {
        /*
        r8 = this;
        r6 = 1;
        r5 = 0;
        r8.lastStartId = r11;
        r8.taskRemoved = r5;
        r2 = 0;
        if (r9 == 0) goto L_0x0025;
    L_0x0009:
        r2 = r9.getAction();
        r7 = r8.startedInForeground;
        r4 = "foreground";
        r4 = r9.getBooleanExtra(r4, r5);
        if (r4 != 0) goto L_0x0021;
    L_0x0018:
        r4 = "com.google.android.exoplayer.downloadService.action.RESTART";
        r4 = r4.equals(r2);
        if (r4 == 0) goto L_0x0090;
    L_0x0021:
        r4 = r6;
    L_0x0022:
        r4 = r4 | r7;
        r8.startedInForeground = r4;
    L_0x0025:
        if (r2 != 0) goto L_0x002a;
    L_0x0027:
        r2 = "com.google.android.exoplayer.downloadService.action.INIT";
    L_0x002a:
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r7 = "onStartCommand action: ";
        r4 = r4.append(r7);
        r4 = r4.append(r2);
        r7 = " startId: ";
        r4 = r4.append(r7);
        r4 = r4.append(r11);
        r4 = r4.toString();
        r8.logd(r4);
        r4 = -1;
        r7 = r2.hashCode();
        switch(r7) {
            case -871181424: goto L_0x009c;
            case -608867945: goto L_0x00b2;
            case -382886238: goto L_0x00a7;
            case 1015676687: goto L_0x0092;
            default: goto L_0x0054;
        };
    L_0x0054:
        r5 = r4;
    L_0x0055:
        switch(r5) {
            case 0: goto L_0x0072;
            case 1: goto L_0x0072;
            case 2: goto L_0x00bd;
            case 3: goto L_0x00e1;
            default: goto L_0x0058;
        };
    L_0x0058:
        r4 = "DownloadService";
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r7 = "Ignoring unrecognized action: ";
        r5 = r5.append(r7);
        r5 = r5.append(r2);
        r5 = r5.toString();
        android.util.Log.e(r4, r5);
    L_0x0072:
        r3 = r8.getRequirements();
        r4 = r3.checkRequirements(r8);
        if (r4 == 0) goto L_0x00e5;
    L_0x007c:
        r4 = r8.downloadManager;
        r4.startDownloads();
    L_0x0081:
        r8.maybeStartWatchingRequirements(r3);
        r4 = r8.downloadManager;
        r4 = r4.isIdle();
        if (r4 == 0) goto L_0x008f;
    L_0x008c:
        r8.stop();
    L_0x008f:
        return r6;
    L_0x0090:
        r4 = r5;
        goto L_0x0022;
    L_0x0092:
        r7 = "com.google.android.exoplayer.downloadService.action.INIT";
        r7 = r2.equals(r7);
        if (r7 == 0) goto L_0x0054;
    L_0x009b:
        goto L_0x0055;
    L_0x009c:
        r5 = "com.google.android.exoplayer.downloadService.action.RESTART";
        r5 = r2.equals(r5);
        if (r5 == 0) goto L_0x0054;
    L_0x00a5:
        r5 = r6;
        goto L_0x0055;
    L_0x00a7:
        r5 = "com.google.android.exoplayer.downloadService.action.ADD";
        r5 = r2.equals(r5);
        if (r5 == 0) goto L_0x0054;
    L_0x00b0:
        r5 = 2;
        goto L_0x0055;
    L_0x00b2:
        r5 = "com.google.android.exoplayer.downloadService.action.RELOAD_REQUIREMENTS";
        r5 = r2.equals(r5);
        if (r5 == 0) goto L_0x0054;
    L_0x00bb:
        r5 = 3;
        goto L_0x0055;
    L_0x00bd:
        r4 = "download_action";
        r0 = r9.getByteArrayExtra(r4);
        if (r0 != 0) goto L_0x00d0;
    L_0x00c6:
        r4 = "DownloadService";
        r5 = "Ignoring ADD action with no action data";
        android.util.Log.e(r4, r5);
        goto L_0x0072;
    L_0x00d0:
        r4 = r8.downloadManager;	 Catch:{ IOException -> 0x00d6 }
        r4.handleAction(r0);	 Catch:{ IOException -> 0x00d6 }
        goto L_0x0072;
    L_0x00d6:
        r1 = move-exception;
        r4 = "DownloadService";
        r5 = "Failed to handle ADD action";
        android.util.Log.e(r4, r5, r1);
        goto L_0x0072;
    L_0x00e1:
        r8.stopWatchingRequirements();
        goto L_0x0072;
    L_0x00e5:
        r4 = r8.downloadManager;
        r4.stopDownloads();
        goto L_0x0081;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.offline.DownloadService.onStartCommand(android.content.Intent, int, int):int");
    }

    public void onTaskRemoved(Intent rootIntent) {
        logd("onTaskRemoved rootIntent: " + rootIntent);
        this.taskRemoved = true;
    }

    public void onDestroy() {
        logd("onDestroy");
        if (this.foregroundNotificationUpdater != null) {
            this.foregroundNotificationUpdater.stopPeriodicUpdates();
        }
        this.downloadManager.removeListener(this.downloadManagerListener);
        maybeStopWatchingRequirements();
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    protected Requirements getRequirements() {
        return DEFAULT_REQUIREMENTS;
    }

    protected Notification getForegroundNotification(TaskState[] taskStates) {
        throw new IllegalStateException(getClass().getName() + " is started in the foreground but getForegroundNotification() is not implemented.");
    }

    protected void onTaskStateChanged(TaskState taskState) {
    }

    private void maybeStartWatchingRequirements(Requirements requirements) {
        if (this.downloadManager.getDownloadCount() != 0) {
            Class<? extends DownloadService> clazz = getClass();
            if (((RequirementsHelper) requirementsHelpers.get(clazz)) == null) {
                RequirementsHelper requirementsHelper = new RequirementsHelper(this, requirements, getScheduler(), clazz);
                requirementsHelpers.put(clazz, requirementsHelper);
                requirementsHelper.start();
                logd("started watching requirements");
            }
        }
    }

    private void maybeStopWatchingRequirements() {
        if (this.downloadManager.getDownloadCount() <= 0) {
            stopWatchingRequirements();
        }
    }

    private void stopWatchingRequirements() {
        RequirementsHelper requirementsHelper = (RequirementsHelper) requirementsHelpers.remove(getClass());
        if (requirementsHelper != null) {
            requirementsHelper.stop();
            logd("stopped watching requirements");
        }
    }

    private void stop() {
        if (this.foregroundNotificationUpdater != null) {
            this.foregroundNotificationUpdater.stopPeriodicUpdates();
            if (this.startedInForeground && Util.SDK_INT >= 26) {
                this.foregroundNotificationUpdater.showNotificationIfNotAlready();
            }
        }
        if (Util.SDK_INT >= 28 || !this.taskRemoved) {
            logd("stopSelf(" + this.lastStartId + ") result: " + stopSelfResult(this.lastStartId));
            return;
        }
        stopSelf();
        logd("stopSelf()");
    }

    private void logd(String message) {
    }

    private static Intent getIntent(Context context, Class<? extends DownloadService> clazz, String action) {
        return new Intent(context, clazz).setAction(action);
    }
}
