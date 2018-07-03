package org.telegram.messenger.exoplayer2.offline;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import java.util.HashMap;
import org.telegram.messenger.exoplayer2.offline.DownloadManager.Listener;
import org.telegram.messenger.exoplayer2.offline.DownloadManager.TaskState;
import org.telegram.messenger.exoplayer2.scheduler.Requirements;
import org.telegram.messenger.exoplayer2.scheduler.RequirementsWatcher;
import org.telegram.messenger.exoplayer2.scheduler.Scheduler;
import org.telegram.messenger.exoplayer2.util.NotificationUtil;
import org.telegram.messenger.exoplayer2.util.Util;

public abstract class DownloadService extends Service {
    public static final String ACTION_ADD = "com.google.android.exoplayer.downloadService.action.ADD";
    public static final String ACTION_INIT = "com.google.android.exoplayer.downloadService.action.INIT";
    private static final String ACTION_RESTART = "com.google.android.exoplayer.downloadService.action.RESTART";
    private static final String ACTION_START_DOWNLOADS = "com.google.android.exoplayer.downloadService.action.START_DOWNLOADS";
    private static final String ACTION_STOP_DOWNLOADS = "com.google.android.exoplayer.downloadService.action.STOP_DOWNLOADS";
    private static final boolean DEBUG = false;
    public static final long DEFAULT_FOREGROUND_NOTIFICATION_UPDATE_INTERVAL = 1000;
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
        }

        public void onTaskStateChanged(DownloadManager downloadManager, TaskState taskState) {
            DownloadService.this.onTaskStateChanged(taskState);
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
            startServiceWithAction(DownloadService.ACTION_START_DOWNLOADS);
            if (this.scheduler != null) {
                this.scheduler.cancel();
            }
        }

        public void requirementsNotMet(RequirementsWatcher requirementsWatcher) {
            startServiceWithAction(DownloadService.ACTION_STOP_DOWNLOADS);
            if (this.scheduler != null) {
                if (!this.scheduler.schedule(this.requirements, this.context.getPackageName(), DownloadService.ACTION_RESTART)) {
                    Log.e(DownloadService.TAG, "Scheduling downloads failed.");
                }
            }
        }

        private void startServiceWithAction(String action) {
            Util.startForegroundService(this.context, new Intent(this.context, this.serviceClass).setAction(action).putExtra(DownloadService.KEY_FOREGROUND, true));
        }
    }

    protected abstract DownloadManager getDownloadManager();

    protected abstract Notification getForegroundNotification(TaskState[] taskStateArr);

    protected abstract Scheduler getScheduler();

    protected DownloadService(int foregroundNotificationId) {
        this(foregroundNotificationId, 1000);
    }

    protected DownloadService(int foregroundNotificationId, long foregroundNotificationUpdateInterval) {
        this(foregroundNotificationId, foregroundNotificationUpdateInterval, null, 0);
    }

    protected DownloadService(int foregroundNotificationId, long foregroundNotificationUpdateInterval, String channelId, int channelName) {
        this.foregroundNotificationUpdater = new ForegroundNotificationUpdater(foregroundNotificationId, foregroundNotificationUpdateInterval);
        this.channelId = channelId;
        this.channelName = channelName;
    }

    public static Intent buildAddActionIntent(Context context, Class<? extends DownloadService> clazz, DownloadAction downloadAction, boolean foreground) {
        return new Intent(context, clazz).setAction(ACTION_ADD).putExtra(KEY_DOWNLOAD_ACTION, downloadAction.toByteArray()).putExtra(KEY_FOREGROUND, foreground);
    }

    public static void startWithAction(Context context, Class<? extends DownloadService> clazz, DownloadAction downloadAction, boolean foreground) {
        Intent intent = buildAddActionIntent(context, clazz, downloadAction, foreground);
        if (foreground) {
            Util.startForegroundService(context, intent);
        } else {
            context.startService(intent);
        }
    }

    public void onCreate() {
        RequirementsHelper requirementsHelper;
        logd("onCreate");
        if (this.channelId != null) {
            NotificationUtil.createNotificationChannel(this, this.channelId, this.channelName, 2);
        }
        this.downloadManager = getDownloadManager();
        this.downloadManagerListener = new DownloadManagerListener();
        this.downloadManager.addListener(this.downloadManagerListener);
        synchronized (requirementsHelpers) {
            Class<? extends DownloadService> clazz = getClass();
            requirementsHelper = (RequirementsHelper) requirementsHelpers.get(clazz);
            if (requirementsHelper == null) {
                requirementsHelper = new RequirementsHelper(this, getRequirements(), getScheduler(), clazz);
                requirementsHelpers.put(clazz, requirementsHelper);
            }
        }
        requirementsHelper.start();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int onStartCommand(android.content.Intent r8, int r9, int r10) {
        /*
        r7 = this;
        r5 = 1;
        r4 = 0;
        r7.lastStartId = r10;
        r2 = 0;
        if (r8 == 0) goto L_0x0023;
    L_0x0007:
        r2 = r8.getAction();
        r6 = r7.startedInForeground;
        r3 = "foreground";
        r3 = r8.getBooleanExtra(r3, r4);
        if (r3 != 0) goto L_0x001f;
    L_0x0016:
        r3 = "com.google.android.exoplayer.downloadService.action.RESTART";
        r3 = r3.equals(r2);
        if (r3 == 0) goto L_0x0077;
    L_0x001f:
        r3 = r5;
    L_0x0020:
        r3 = r3 | r6;
        r7.startedInForeground = r3;
    L_0x0023:
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r6 = "onStartCommand action: ";
        r3 = r3.append(r6);
        r3 = r3.append(r2);
        r6 = " startId: ";
        r3 = r3.append(r6);
        r3 = r3.append(r10);
        r3 = r3.toString();
        r7.logd(r3);
        r3 = -1;
        r6 = r2.hashCode();
        switch(r6) {
            case -871181424: goto L_0x0083;
            case -382886238: goto L_0x008e;
            case -337334865: goto L_0x00a4;
            case 1015676687: goto L_0x0079;
            case 1286088717: goto L_0x0099;
            default: goto L_0x004d;
        };
    L_0x004d:
        r4 = r3;
    L_0x004e:
        switch(r4) {
            case 0: goto L_0x006b;
            case 1: goto L_0x006b;
            case 2: goto L_0x00af;
            case 3: goto L_0x00d3;
            case 4: goto L_0x00d9;
            default: goto L_0x0051;
        };
    L_0x0051:
        r3 = "DownloadService";
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r6 = "Ignoring unrecognized action: ";
        r4 = r4.append(r6);
        r4 = r4.append(r2);
        r4 = r4.toString();
        android.util.Log.e(r3, r4);
    L_0x006b:
        r3 = r7.downloadManager;
        r3 = r3.isIdle();
        if (r3 == 0) goto L_0x0076;
    L_0x0073:
        r7.stop();
    L_0x0076:
        return r5;
    L_0x0077:
        r3 = r4;
        goto L_0x0020;
    L_0x0079:
        r6 = "com.google.android.exoplayer.downloadService.action.INIT";
        r6 = r2.equals(r6);
        if (r6 == 0) goto L_0x004d;
    L_0x0082:
        goto L_0x004e;
    L_0x0083:
        r4 = "com.google.android.exoplayer.downloadService.action.RESTART";
        r4 = r2.equals(r4);
        if (r4 == 0) goto L_0x004d;
    L_0x008c:
        r4 = r5;
        goto L_0x004e;
    L_0x008e:
        r4 = "com.google.android.exoplayer.downloadService.action.ADD";
        r4 = r2.equals(r4);
        if (r4 == 0) goto L_0x004d;
    L_0x0097:
        r4 = 2;
        goto L_0x004e;
    L_0x0099:
        r4 = "com.google.android.exoplayer.downloadService.action.STOP_DOWNLOADS";
        r4 = r2.equals(r4);
        if (r4 == 0) goto L_0x004d;
    L_0x00a2:
        r4 = 3;
        goto L_0x004e;
    L_0x00a4:
        r4 = "com.google.android.exoplayer.downloadService.action.START_DOWNLOADS";
        r4 = r2.equals(r4);
        if (r4 == 0) goto L_0x004d;
    L_0x00ad:
        r4 = 4;
        goto L_0x004e;
    L_0x00af:
        r3 = "download_action";
        r0 = r8.getByteArrayExtra(r3);
        if (r0 != 0) goto L_0x00c2;
    L_0x00b8:
        r3 = "DownloadService";
        r4 = "Ignoring ADD action with no action data";
        android.util.Log.e(r3, r4);
        goto L_0x006b;
    L_0x00c2:
        r3 = r7.downloadManager;	 Catch:{ IOException -> 0x00c8 }
        r3.handleAction(r0);	 Catch:{ IOException -> 0x00c8 }
        goto L_0x006b;
    L_0x00c8:
        r1 = move-exception;
        r3 = "DownloadService";
        r4 = "Failed to handle ADD action";
        android.util.Log.e(r3, r4, r1);
        goto L_0x006b;
    L_0x00d3:
        r3 = r7.downloadManager;
        r3.stopDownloads();
        goto L_0x006b;
    L_0x00d9:
        r3 = r7.downloadManager;
        r3.startDownloads();
        goto L_0x006b;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.offline.DownloadService.onStartCommand(android.content.Intent, int, int):int");
    }

    public void onDestroy() {
        logd("onDestroy");
        this.foregroundNotificationUpdater.stopPeriodicUpdates();
        this.downloadManager.removeListener(this.downloadManagerListener);
        if (this.downloadManager.getTaskCount() == 0) {
            synchronized (requirementsHelpers) {
                RequirementsHelper requirementsHelper = (RequirementsHelper) requirementsHelpers.remove(getClass());
                if (requirementsHelper != null) {
                    requirementsHelper.stop();
                }
            }
        }
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    protected Requirements getRequirements() {
        return new Requirements(1, false, false);
    }

    protected void onTaskStateChanged(TaskState taskState) {
    }

    private void stop() {
        this.foregroundNotificationUpdater.stopPeriodicUpdates();
        if (this.startedInForeground && Util.SDK_INT >= 26) {
            this.foregroundNotificationUpdater.showNotificationIfNotAlready();
        }
        logd("stopSelf(" + this.lastStartId + ") result: " + stopSelfResult(this.lastStartId));
    }

    private void logd(String message) {
    }
}
