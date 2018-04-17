package android.support.v4.app;

import android.app.AppOpsManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.provider.Settings.Secure;
import android.support.v4.app.INotificationSideChannel.Stub;
import android.util.Log;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public final class NotificationManagerCompat {
    private static Set<String> sEnabledNotificationListenerPackages = new HashSet();
    private static String sEnabledNotificationListeners;
    private static final Object sEnabledNotificationListenersLock = new Object();
    private static final Object sLock = new Object();
    private static SideChannelManager sSideChannelManager;
    private final Context mContext;
    private final NotificationManager mNotificationManager = ((NotificationManager) this.mContext.getSystemService("notification"));

    private static class ServiceConnectedEvent {
        final ComponentName componentName;
        final IBinder iBinder;

        ServiceConnectedEvent(ComponentName componentName, IBinder iBinder) {
            this.componentName = componentName;
            this.iBinder = iBinder;
        }
    }

    private static class SideChannelManager implements ServiceConnection, Callback {
        private Set<String> mCachedEnabledPackages = new HashSet();
        private final Context mContext;
        private final Handler mHandler;
        private final HandlerThread mHandlerThread;
        private final Map<ComponentName, ListenerRecord> mRecordMap = new HashMap();

        private static class ListenerRecord {
            boolean bound = false;
            final ComponentName componentName;
            int retryCount = 0;
            INotificationSideChannel service;
            ArrayDeque<Task> taskQueue = new ArrayDeque();

            ListenerRecord(ComponentName componentName) {
                this.componentName = componentName;
            }
        }

        private void processListenerQueue(android.support.v4.app.NotificationManagerCompat.SideChannelManager.ListenerRecord r1) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: android.support.v4.app.NotificationManagerCompat.SideChannelManager.processListenerQueue(android.support.v4.app.NotificationManagerCompat$SideChannelManager$ListenerRecord):void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
            /*
            r0 = this;
            r0 = "NotifManCompat";
            r1 = 3;
            r0 = android.util.Log.isLoggable(r0, r1);
            if (r0 == 0) goto L_0x0034;
        L_0x0009:
            r0 = "NotifManCompat";
            r2 = new java.lang.StringBuilder;
            r2.<init>();
            r3 = "Processing component ";
            r2.append(r3);
            r3 = r6.componentName;
            r2.append(r3);
            r3 = ", ";
            r2.append(r3);
            r3 = r6.taskQueue;
            r3 = r3.size();
            r2.append(r3);
            r3 = " queued tasks";
            r2.append(r3);
            r2 = r2.toString();
            android.util.Log.d(r0, r2);
        L_0x0034:
            r0 = r6.taskQueue;
            r0 = r0.isEmpty();
            if (r0 == 0) goto L_0x003d;
        L_0x003c:
            return;
        L_0x003d:
            r0 = r5.ensureServiceBound(r6);
            if (r0 == 0) goto L_0x00c6;
        L_0x0043:
            r0 = r6.service;
            if (r0 != 0) goto L_0x0049;
        L_0x0047:
            goto L_0x00c6;
        L_0x0049:
            r0 = r6.taskQueue;
            r0 = r0.peek();
            r0 = (android.support.v4.app.NotificationManagerCompat.Task) r0;
            if (r0 != 0) goto L_0x0054;
        L_0x0053:
            goto L_0x00ba;
        L_0x0054:
            r2 = "NotifManCompat";	 Catch:{ DeadObjectException -> 0x0098, RemoteException -> 0x007e }
            r2 = android.util.Log.isLoggable(r2, r1);	 Catch:{ DeadObjectException -> 0x0098, RemoteException -> 0x007e }
            if (r2 == 0) goto L_0x0072;	 Catch:{ DeadObjectException -> 0x0098, RemoteException -> 0x007e }
        L_0x005c:
            r2 = "NotifManCompat";	 Catch:{ DeadObjectException -> 0x0098, RemoteException -> 0x007e }
            r3 = new java.lang.StringBuilder;	 Catch:{ DeadObjectException -> 0x0098, RemoteException -> 0x007e }
            r3.<init>();	 Catch:{ DeadObjectException -> 0x0098, RemoteException -> 0x007e }
            r4 = "Sending task ";	 Catch:{ DeadObjectException -> 0x0098, RemoteException -> 0x007e }
            r3.append(r4);	 Catch:{ DeadObjectException -> 0x0098, RemoteException -> 0x007e }
            r3.append(r0);	 Catch:{ DeadObjectException -> 0x0098, RemoteException -> 0x007e }
            r3 = r3.toString();	 Catch:{ DeadObjectException -> 0x0098, RemoteException -> 0x007e }
            android.util.Log.d(r2, r3);	 Catch:{ DeadObjectException -> 0x0098, RemoteException -> 0x007e }
        L_0x0072:
            r2 = r6.service;	 Catch:{ DeadObjectException -> 0x0098, RemoteException -> 0x007e }
            r0.send(r2);	 Catch:{ DeadObjectException -> 0x0098, RemoteException -> 0x007e }
            r2 = r6.taskQueue;	 Catch:{ DeadObjectException -> 0x0098, RemoteException -> 0x007e }
            r2.remove();	 Catch:{ DeadObjectException -> 0x0098, RemoteException -> 0x007e }
            goto L_0x0049;
        L_0x007e:
            r1 = move-exception;
            r2 = "NotifManCompat";
            r3 = new java.lang.StringBuilder;
            r3.<init>();
            r4 = "RemoteException communicating with ";
            r3.append(r4);
            r4 = r6.componentName;
            r3.append(r4);
            r3 = r3.toString();
            android.util.Log.w(r2, r3, r1);
            goto L_0x00ba;
        L_0x0098:
            r2 = move-exception;
            r3 = "NotifManCompat";
            r1 = android.util.Log.isLoggable(r3, r1);
            if (r1 == 0) goto L_0x00b9;
        L_0x00a1:
            r1 = "NotifManCompat";
            r3 = new java.lang.StringBuilder;
            r3.<init>();
            r4 = "Remote service has died: ";
            r3.append(r4);
            r4 = r6.componentName;
            r3.append(r4);
            r3 = r3.toString();
            android.util.Log.d(r1, r3);
        L_0x00ba:
            r0 = r6.taskQueue;
            r0 = r0.isEmpty();
            if (r0 != 0) goto L_0x00c5;
            r5.scheduleListenerRetry(r6);
            return;
        L_0x00c6:
            r5.scheduleListenerRetry(r6);
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.support.v4.app.NotificationManagerCompat.SideChannelManager.processListenerQueue(android.support.v4.app.NotificationManagerCompat$SideChannelManager$ListenerRecord):void");
        }

        SideChannelManager(Context context) {
            this.mContext = context;
            this.mHandlerThread = new HandlerThread("NotificationManagerCompat");
            this.mHandlerThread.start();
            this.mHandler = new Handler(this.mHandlerThread.getLooper(), this);
        }

        public void queueTask(Task task) {
            this.mHandler.obtainMessage(0, task).sendToTarget();
        }

        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    handleQueueTask((Task) msg.obj);
                    return true;
                case 1:
                    ServiceConnectedEvent event = msg.obj;
                    handleServiceConnected(event.componentName, event.iBinder);
                    return true;
                case 2:
                    handleServiceDisconnected((ComponentName) msg.obj);
                    return true;
                case 3:
                    handleRetryListenerQueue((ComponentName) msg.obj);
                    return true;
                default:
                    return false;
            }
        }

        private void handleQueueTask(Task task) {
            updateListenerMap();
            for (ListenerRecord record : this.mRecordMap.values()) {
                record.taskQueue.add(task);
                processListenerQueue(record);
            }
        }

        private void handleServiceConnected(ComponentName componentName, IBinder iBinder) {
            ListenerRecord record = (ListenerRecord) this.mRecordMap.get(componentName);
            if (record != null) {
                record.service = Stub.asInterface(iBinder);
                record.retryCount = 0;
                processListenerQueue(record);
            }
        }

        private void handleServiceDisconnected(ComponentName componentName) {
            ListenerRecord record = (ListenerRecord) this.mRecordMap.get(componentName);
            if (record != null) {
                ensureServiceUnbound(record);
            }
        }

        private void handleRetryListenerQueue(ComponentName componentName) {
            ListenerRecord record = (ListenerRecord) this.mRecordMap.get(componentName);
            if (record != null) {
                processListenerQueue(record);
            }
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            if (Log.isLoggable("NotifManCompat", 3)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Connected to service ");
                stringBuilder.append(componentName);
                Log.d("NotifManCompat", stringBuilder.toString());
            }
            this.mHandler.obtainMessage(1, new ServiceConnectedEvent(componentName, iBinder)).sendToTarget();
        }

        public void onServiceDisconnected(ComponentName componentName) {
            if (Log.isLoggable("NotifManCompat", 3)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Disconnected from service ");
                stringBuilder.append(componentName);
                Log.d("NotifManCompat", stringBuilder.toString());
            }
            this.mHandler.obtainMessage(2, componentName).sendToTarget();
        }

        private void updateListenerMap() {
            Set<String> enabledPackages = NotificationManagerCompat.getEnabledListenerPackages(this.mContext);
            if (!enabledPackages.equals(this.mCachedEnabledPackages)) {
                StringBuilder stringBuilder;
                this.mCachedEnabledPackages = enabledPackages;
                List<ResolveInfo> resolveInfos = this.mContext.getPackageManager().queryIntentServices(new Intent().setAction("android.support.BIND_NOTIFICATION_SIDE_CHANNEL"), 0);
                Set<ComponentName> enabledComponents = new HashSet();
                for (ResolveInfo resolveInfo : resolveInfos) {
                    if (enabledPackages.contains(resolveInfo.serviceInfo.packageName)) {
                        ComponentName componentName = new ComponentName(resolveInfo.serviceInfo.packageName, resolveInfo.serviceInfo.name);
                        if (resolveInfo.serviceInfo.permission != null) {
                            stringBuilder = new StringBuilder();
                            stringBuilder.append("Permission present on component ");
                            stringBuilder.append(componentName);
                            stringBuilder.append(", not adding listener record.");
                            Log.w("NotifManCompat", stringBuilder.toString());
                        } else {
                            enabledComponents.add(componentName);
                        }
                    }
                }
                for (ComponentName componentName2 : enabledComponents) {
                    if (!this.mRecordMap.containsKey(componentName2)) {
                        if (Log.isLoggable("NotifManCompat", 3)) {
                            StringBuilder stringBuilder2 = new StringBuilder();
                            stringBuilder2.append("Adding listener record for ");
                            stringBuilder2.append(componentName2);
                            Log.d("NotifManCompat", stringBuilder2.toString());
                        }
                        this.mRecordMap.put(componentName2, new ListenerRecord(componentName2));
                    }
                }
                Iterator<Entry<ComponentName, ListenerRecord>> it = this.mRecordMap.entrySet().iterator();
                while (it.hasNext()) {
                    Entry<ComponentName, ListenerRecord> entry = (Entry) it.next();
                    if (!enabledComponents.contains(entry.getKey())) {
                        if (Log.isLoggable("NotifManCompat", 3)) {
                            stringBuilder = new StringBuilder();
                            stringBuilder.append("Removing listener record for ");
                            stringBuilder.append(entry.getKey());
                            Log.d("NotifManCompat", stringBuilder.toString());
                        }
                        ensureServiceUnbound((ListenerRecord) entry.getValue());
                        it.remove();
                    }
                }
            }
        }

        private boolean ensureServiceBound(ListenerRecord record) {
            if (record.bound) {
                return true;
            }
            record.bound = this.mContext.bindService(new Intent("android.support.BIND_NOTIFICATION_SIDE_CHANNEL").setComponent(record.componentName), this, 33);
            if (record.bound) {
                record.retryCount = 0;
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unable to bind to listener ");
                stringBuilder.append(record.componentName);
                Log.w("NotifManCompat", stringBuilder.toString());
                this.mContext.unbindService(this);
            }
            return record.bound;
        }

        private void ensureServiceUnbound(ListenerRecord record) {
            if (record.bound) {
                this.mContext.unbindService(this);
                record.bound = false;
            }
            record.service = null;
        }

        private void scheduleListenerRetry(ListenerRecord record) {
            if (!this.mHandler.hasMessages(3, record.componentName)) {
                record.retryCount++;
                if (record.retryCount > 6) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Giving up on delivering ");
                    stringBuilder.append(record.taskQueue.size());
                    stringBuilder.append(" tasks to ");
                    stringBuilder.append(record.componentName);
                    stringBuilder.append(" after ");
                    stringBuilder.append(record.retryCount);
                    stringBuilder.append(" retries");
                    Log.w("NotifManCompat", stringBuilder.toString());
                    record.taskQueue.clear();
                    return;
                }
                int delayMs = 1000 * (1 << (record.retryCount - 1));
                if (Log.isLoggable("NotifManCompat", 3)) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("Scheduling retry for ");
                    stringBuilder2.append(delayMs);
                    stringBuilder2.append(" ms");
                    Log.d("NotifManCompat", stringBuilder2.toString());
                }
                this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(3, record.componentName), (long) delayMs);
            }
        }
    }

    private interface Task {
        void send(INotificationSideChannel iNotificationSideChannel) throws RemoteException;
    }

    private static class CancelTask implements Task {
        final boolean all = false;
        final int id;
        final String packageName;
        final String tag;

        CancelTask(String packageName, int id, String tag) {
            this.packageName = packageName;
            this.id = id;
            this.tag = tag;
        }

        public void send(INotificationSideChannel service) throws RemoteException {
            if (this.all) {
                service.cancelAll(this.packageName);
            } else {
                service.cancel(this.packageName, this.id, this.tag);
            }
        }

        public String toString() {
            StringBuilder sb = new StringBuilder("CancelTask[");
            sb.append("packageName:");
            sb.append(this.packageName);
            sb.append(", id:");
            sb.append(this.id);
            sb.append(", tag:");
            sb.append(this.tag);
            sb.append(", all:");
            sb.append(this.all);
            sb.append("]");
            return sb.toString();
        }
    }

    private static class NotifyTask implements Task {
        final int id;
        final Notification notif;
        final String packageName;
        final String tag;

        NotifyTask(String packageName, int id, String tag, Notification notif) {
            this.packageName = packageName;
            this.id = id;
            this.tag = tag;
            this.notif = notif;
        }

        public void send(INotificationSideChannel service) throws RemoteException {
            service.notify(this.packageName, this.id, this.tag, this.notif);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder("NotifyTask[");
            sb.append("packageName:");
            sb.append(this.packageName);
            sb.append(", id:");
            sb.append(this.id);
            sb.append(", tag:");
            sb.append(this.tag);
            sb.append("]");
            return sb.toString();
        }
    }

    public static NotificationManagerCompat from(Context context) {
        return new NotificationManagerCompat(context);
    }

    private NotificationManagerCompat(Context context) {
        this.mContext = context;
    }

    public void cancel(int id) {
        cancel(null, id);
    }

    public void cancel(String tag, int id) {
        this.mNotificationManager.cancel(tag, id);
        if (VERSION.SDK_INT <= 19) {
            pushSideChannelQueue(new CancelTask(this.mContext.getPackageName(), id, tag));
        }
    }

    public void notify(int id, Notification notification) {
        notify(null, id, notification);
    }

    public void notify(String tag, int id, Notification notification) {
        if (useSideChannelForNotification(notification)) {
            pushSideChannelQueue(new NotifyTask(this.mContext.getPackageName(), id, tag, notification));
            this.mNotificationManager.cancel(tag, id);
            return;
        }
        this.mNotificationManager.notify(tag, id, notification);
    }

    public boolean areNotificationsEnabled() {
        if (VERSION.SDK_INT >= 24) {
            return this.mNotificationManager.areNotificationsEnabled();
        }
        boolean z = true;
        if (VERSION.SDK_INT < 19) {
            return true;
        }
        AppOpsManager appOps = (AppOpsManager) this.mContext.getSystemService("appops");
        ApplicationInfo appInfo = this.mContext.getApplicationInfo();
        String pkg = this.mContext.getApplicationContext().getPackageName();
        int uid = appInfo.uid;
        try {
            if (((Integer) Class.forName(AppOpsManager.class.getName()).getMethod("checkOpNoThrow", new Class[]{Integer.TYPE, Integer.TYPE, String.class}).invoke(appOps, new Object[]{Integer.valueOf(((Integer) appOpsClass.getDeclaredField("OP_POST_NOTIFICATION").get(Integer.class)).intValue()), Integer.valueOf(uid), pkg})).intValue() != 0) {
                z = false;
            }
            return z;
        } catch (ClassNotFoundException e) {
            return true;
        }
    }

    public static Set<String> getEnabledListenerPackages(Context context) {
        String enabledNotificationListeners = Secure.getString(context.getContentResolver(), "enabled_notification_listeners");
        synchronized (sEnabledNotificationListenersLock) {
            if (enabledNotificationListeners != null) {
                if (!enabledNotificationListeners.equals(sEnabledNotificationListeners)) {
                    String[] components = enabledNotificationListeners.split(":");
                    Set<String> packageNames = new HashSet(components.length);
                    for (String component : components) {
                        ComponentName componentName = ComponentName.unflattenFromString(component);
                        if (componentName != null) {
                            packageNames.add(componentName.getPackageName());
                        }
                    }
                    sEnabledNotificationListenerPackages = packageNames;
                    sEnabledNotificationListeners = enabledNotificationListeners;
                }
            }
        }
        return sEnabledNotificationListenerPackages;
    }

    private static boolean useSideChannelForNotification(Notification notification) {
        Bundle extras = NotificationCompat.getExtras(notification);
        return extras != null && extras.getBoolean("android.support.useSideChannel");
    }

    private void pushSideChannelQueue(Task task) {
        synchronized (sLock) {
            if (sSideChannelManager == null) {
                sSideChannelManager = new SideChannelManager(this.mContext.getApplicationContext());
            }
            sSideChannelManager.queueTask(task);
        }
    }
}
