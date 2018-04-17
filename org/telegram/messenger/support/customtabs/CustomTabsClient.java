package org.telegram.messenger.support.customtabs;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.List;
import org.telegram.messenger.support.customtabs.ICustomTabsCallback.Stub;

public class CustomTabsClient {
    private final ICustomTabsService mService;
    private final ComponentName mServiceComponentName;

    class AnonymousClass2 extends Stub {
        private Handler mHandler = new Handler(Looper.getMainLooper());
        final /* synthetic */ CustomTabsCallback val$callback;

        AnonymousClass2(CustomTabsCallback customTabsCallback) {
            this.val$callback = customTabsCallback;
        }

        public void onNavigationEvent(final int navigationEvent, final Bundle extras) {
            if (this.val$callback != null) {
                this.mHandler.post(new Runnable() {
                    public void run() {
                        AnonymousClass2.this.val$callback.onNavigationEvent(navigationEvent, extras);
                    }
                });
            }
        }

        public void extraCallback(final String callbackName, final Bundle args) throws RemoteException {
            if (this.val$callback != null) {
                this.mHandler.post(new Runnable() {
                    public void run() {
                        AnonymousClass2.this.val$callback.extraCallback(callbackName, args);
                    }
                });
            }
        }

        public void onMessageChannelReady(final Bundle extras) throws RemoteException {
            if (this.val$callback != null) {
                this.mHandler.post(new Runnable() {
                    public void run() {
                        AnonymousClass2.this.val$callback.onMessageChannelReady(extras);
                    }
                });
            }
        }

        public void onPostMessage(final String message, final Bundle extras) throws RemoteException {
            if (this.val$callback != null) {
                this.mHandler.post(new Runnable() {
                    public void run() {
                        AnonymousClass2.this.val$callback.onPostMessage(message, extras);
                    }
                });
            }
        }
    }

    public org.telegram.messenger.support.customtabs.CustomTabsSession newSession(org.telegram.messenger.support.customtabs.CustomTabsCallback r1) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.support.customtabs.CustomTabsClient.newSession(org.telegram.messenger.support.customtabs.CustomTabsCallback):org.telegram.messenger.support.customtabs.CustomTabsSession
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
        r0 = this;
        r0 = new org.telegram.messenger.support.customtabs.CustomTabsClient$2;
        r0.<init>(r5);
        r1 = 0;
        r2 = r4.mService;	 Catch:{ RemoteException -> 0x001a }
        r2 = r2.newSession(r0);	 Catch:{ RemoteException -> 0x001a }
        if (r2 != 0) goto L_0x000f;
    L_0x000e:
        return r1;
        r1 = new org.telegram.messenger.support.customtabs.CustomTabsSession;
        r2 = r4.mService;
        r3 = r4.mServiceComponentName;
        r1.<init>(r2, r0, r3);
        return r1;
    L_0x001a:
        r2 = move-exception;
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.support.customtabs.CustomTabsClient.newSession(org.telegram.messenger.support.customtabs.CustomTabsCallback):org.telegram.messenger.support.customtabs.CustomTabsSession");
    }

    CustomTabsClient(ICustomTabsService service, ComponentName componentName) {
        this.mService = service;
        this.mServiceComponentName = componentName;
    }

    public static boolean bindCustomTabsService(Context context, String packageName, CustomTabsServiceConnection connection) {
        Intent intent = new Intent(CustomTabsService.ACTION_CUSTOM_TABS_CONNECTION);
        if (!TextUtils.isEmpty(packageName)) {
            intent.setPackage(packageName);
        }
        return context.bindService(intent, connection, 33);
    }

    public static String getPackageName(Context context, List<String> packages) {
        return getPackageName(context, packages, false);
    }

    public static String getPackageName(Context context, List<String> packages, boolean ignoreDefault) {
        PackageManager pm = context.getPackageManager();
        List<String> packageNames = packages == null ? new ArrayList() : packages;
        Intent activityIntent = new Intent("android.intent.action.VIEW", Uri.parse("http://"));
        if (!ignoreDefault) {
            ResolveInfo defaultViewHandlerInfo = pm.resolveActivity(activityIntent, 0);
            if (defaultViewHandlerInfo != null) {
                String packageName = defaultViewHandlerInfo.activityInfo.packageName;
                packageNames = new ArrayList(packageNames.size() + 1);
                packageNames.add(packageName);
                if (packages != null) {
                    packageNames.addAll(packages);
                }
            }
        }
        Intent serviceIntent = new Intent(CustomTabsService.ACTION_CUSTOM_TABS_CONNECTION);
        for (String packageName2 : packageNames) {
            serviceIntent.setPackage(packageName2);
            if (pm.resolveService(serviceIntent, 0) != null) {
                return packageName2;
            }
        }
        return null;
    }

    public static boolean connectAndInitialize(Context context, String packageName) {
        if (packageName == null) {
            return false;
        }
        final Context applicationContext = context.getApplicationContext();
        try {
            return bindCustomTabsService(applicationContext, packageName, new CustomTabsServiceConnection() {
                public final void onCustomTabsServiceConnected(ComponentName name, CustomTabsClient client) {
                    client.warmup(0);
                    applicationContext.unbindService(this);
                }

                public final void onServiceDisconnected(ComponentName componentName) {
                }
            });
        } catch (SecurityException e) {
            return false;
        }
    }

    public boolean warmup(long flags) {
        try {
            return this.mService.warmup(flags);
        } catch (RemoteException e) {
            return false;
        }
    }

    public Bundle extraCommand(String commandName, Bundle args) {
        try {
            return this.mService.extraCommand(commandName, args);
        } catch (RemoteException e) {
            return null;
        }
    }
}
