package org.telegram.messenger.browser;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import java.lang.ref.WeakReference;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.beta.R;
import org.telegram.messenger.support.customtabs.CustomTabsCallback;
import org.telegram.messenger.support.customtabs.CustomTabsClient;
import org.telegram.messenger.support.customtabs.CustomTabsServiceConnection;
import org.telegram.messenger.support.customtabs.CustomTabsSession;
import org.telegram.messenger.support.customtabsclient.shared.CustomTabsHelper;
import org.telegram.messenger.support.customtabsclient.shared.ServiceConnection;
import org.telegram.messenger.support.customtabsclient.shared.ServiceConnectionCallback;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.tgnet.TLRPC.TL_messageMediaWebPage;
import org.telegram.tgnet.TLRPC.TL_webPage;
import org.telegram.ui.ActionBar.AlertDialog;

public class Browser {
    private static WeakReference<Activity> currentCustomTabsActivity;
    private static CustomTabsClient customTabsClient;
    private static WeakReference<CustomTabsSession> customTabsCurrentSession;
    private static String customTabsPackageToBind;
    private static CustomTabsServiceConnection customTabsServiceConnection;
    private static CustomTabsSession customTabsSession;

    static class AnonymousClass3 implements Runnable {
        final /* synthetic */ AlertDialog[] val$progressDialog;
        final /* synthetic */ int val$reqId;

        AnonymousClass3(AlertDialog[] alertDialogArr, int i) {
            this.val$progressDialog = alertDialogArr;
            this.val$reqId = i;
        }

        public void run() {
            if (this.val$progressDialog[0] != null) {
                try {
                    this.val$progressDialog[0].setMessage(LocaleController.getString("Loading", R.string.Loading));
                    this.val$progressDialog[0].setCanceledOnTouchOutside(false);
                    this.val$progressDialog[0].setCancelable(false);
                    this.val$progressDialog[0].setButton(-2, LocaleController.getString("Cancel", R.string.Cancel), new OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ConnectionsManager.getInstance(UserConfig.selectedAccount).cancelRequest(AnonymousClass3.this.val$reqId, true);
                            try {
                                dialog.dismiss();
                            } catch (Throwable e) {
                                FileLog.e(e);
                            }
                        }
                    });
                    this.val$progressDialog[0].show();
                } catch (Exception e) {
                }
            }
        }
    }

    static class AnonymousClass2 implements RequestDelegate {
        final /* synthetic */ boolean val$allowCustom;
        final /* synthetic */ Context val$context;
        final /* synthetic */ int val$currentAccount;
        final /* synthetic */ AlertDialog[] val$progressDialog;
        final /* synthetic */ Uri val$uri;

        AnonymousClass2(AlertDialog[] alertDialogArr, int i, Uri uri, Context context, boolean z) {
            this.val$progressDialog = alertDialogArr;
            this.val$currentAccount = i;
            this.val$uri = uri;
            this.val$context = context;
            this.val$allowCustom = z;
        }

        public void run(final TLObject response, TL_error error) {
            AndroidUtilities.runOnUIThread(new Runnable() {
                public void run() {
                    try {
                        AnonymousClass2.this.val$progressDialog[0].dismiss();
                    } catch (Throwable th) {
                    }
                    AnonymousClass2.this.val$progressDialog[0] = null;
                    boolean ok = false;
                    if (response instanceof TL_messageMediaWebPage) {
                        TL_messageMediaWebPage webPage = response;
                        if ((webPage.webpage instanceof TL_webPage) && webPage.webpage.cached_page != null) {
                            NotificationCenter.getInstance(AnonymousClass2.this.val$currentAccount).postNotificationName(NotificationCenter.openArticle, webPage.webpage, AnonymousClass2.this.val$uri.toString());
                            ok = true;
                        }
                    }
                    if (!ok) {
                        Browser.openUrl(AnonymousClass2.this.val$context, AnonymousClass2.this.val$uri, AnonymousClass2.this.val$allowCustom, false);
                    }
                }
            });
        }
    }

    private static class NavigationCallback extends CustomTabsCallback {
        private NavigationCallback() {
        }

        public void onNavigationEvent(int navigationEvent, Bundle extras) {
        }
    }

    public static void openUrl(android.content.Context r1, android.net.Uri r2, boolean r3, boolean r4) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.browser.Browser.openUrl(android.content.Context, android.net.Uri, boolean, boolean):void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
        /*
        r7 = r18;
        r8 = r19;
        if (r7 == 0) goto L_0x0266;
    L_0x0006:
        if (r8 != 0) goto L_0x000a;
    L_0x0008:
        goto L_0x0266;
    L_0x000a:
        r9 = org.telegram.messenger.UserConfig.selectedAccount;
        r10 = 1;
        r1 = new boolean[r10];
        r11 = 0;
        r1[r11] = r11;
        r12 = r1;
        r13 = isInternalUri(r8, r12);
        if (r21 == 0) goto L_0x0086;
    L_0x0019:
        r1 = r19.getHost();	 Catch:{ Exception -> 0x0082 }
        r1 = r1.toLowerCase();	 Catch:{ Exception -> 0x0082 }
        r15 = r1;	 Catch:{ Exception -> 0x0082 }
        r1 = "telegra.ph";	 Catch:{ Exception -> 0x0082 }
        r1 = r15.equals(r1);	 Catch:{ Exception -> 0x0082 }
        if (r1 != 0) goto L_0x0043;
    L_0x002a:
        r1 = r19.toString();	 Catch:{ Exception -> 0x003f }
        r1 = r1.toLowerCase();	 Catch:{ Exception -> 0x003f }
        r2 = "telegram.org/faq";	 Catch:{ Exception -> 0x003f }
        r1 = r1.contains(r2);	 Catch:{ Exception -> 0x003f }
        if (r1 == 0) goto L_0x003b;
    L_0x003a:
        goto L_0x0043;
        r16 = r9;
        goto L_0x0088;
    L_0x003f:
        r0 = move-exception;
        r16 = r9;
        goto L_0x0085;
    L_0x0043:
        r1 = new org.telegram.ui.ActionBar.AlertDialog[r10];	 Catch:{ Exception -> 0x0082 }
        r2 = new org.telegram.ui.ActionBar.AlertDialog;	 Catch:{ Exception -> 0x0082 }
        r2.<init>(r7, r10);	 Catch:{ Exception -> 0x0082 }
        r1[r11] = r2;	 Catch:{ Exception -> 0x0082 }
        r6 = r1;	 Catch:{ Exception -> 0x0082 }
        r1 = new org.telegram.tgnet.TLRPC$TL_messages_getWebPagePreview;	 Catch:{ Exception -> 0x0082 }
        r1.<init>();	 Catch:{ Exception -> 0x0082 }
        r5 = r1;	 Catch:{ Exception -> 0x0082 }
        r1 = r19.toString();	 Catch:{ Exception -> 0x0082 }
        r5.message = r1;	 Catch:{ Exception -> 0x0082 }
        r1 = org.telegram.messenger.UserConfig.selectedAccount;	 Catch:{ Exception -> 0x0082 }
        r4 = org.telegram.tgnet.ConnectionsManager.getInstance(r1);	 Catch:{ Exception -> 0x0082 }
        r3 = new org.telegram.messenger.browser.Browser$2;	 Catch:{ Exception -> 0x0082 }
        r1 = r3;
        r2 = r6;
        r10 = r3;
        r3 = r9;
        r11 = r4;
        r4 = r8;
        r16 = r9;
        r9 = r5;
        r5 = r7;
        r14 = r6;
        r6 = r20;
        r1.<init>(r2, r3, r4, r5, r6);	 Catch:{ Exception -> 0x0080 }
        r1 = r11.sendRequest(r9, r10);	 Catch:{ Exception -> 0x0080 }
        r2 = new org.telegram.messenger.browser.Browser$3;	 Catch:{ Exception -> 0x0080 }
        r2.<init>(r14, r1);	 Catch:{ Exception -> 0x0080 }
        r3 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;	 Catch:{ Exception -> 0x0080 }
        org.telegram.messenger.AndroidUtilities.runOnUIThread(r2, r3);	 Catch:{ Exception -> 0x0080 }
        return;
    L_0x0080:
        r0 = move-exception;
        goto L_0x0085;
    L_0x0082:
        r0 = move-exception;
        r16 = r9;
        goto L_0x0088;
    L_0x0086:
        r16 = r9;
        r1 = r19.getScheme();	 Catch:{ Exception -> 0x022d }
        if (r1 == 0) goto L_0x0097;	 Catch:{ Exception -> 0x022d }
        r1 = r19.getScheme();	 Catch:{ Exception -> 0x022d }
        r1 = r1.toLowerCase();	 Catch:{ Exception -> 0x022d }
        goto L_0x0099;	 Catch:{ Exception -> 0x022d }
        r1 = "";	 Catch:{ Exception -> 0x022d }
        if (r20 == 0) goto L_0x022c;	 Catch:{ Exception -> 0x022d }
        r3 = org.telegram.messenger.SharedConfig.customTabs;	 Catch:{ Exception -> 0x022d }
        if (r3 == 0) goto L_0x022c;	 Catch:{ Exception -> 0x022d }
        if (r13 != 0) goto L_0x022c;	 Catch:{ Exception -> 0x022d }
        r3 = "tel";	 Catch:{ Exception -> 0x022d }
        r3 = r1.equals(r3);	 Catch:{ Exception -> 0x022d }
        if (r3 != 0) goto L_0x022c;
        r3 = 0;
        r4 = r3;
        r5 = new android.content.Intent;	 Catch:{ Exception -> 0x0101 }
        r6 = "android.intent.action.VIEW";	 Catch:{ Exception -> 0x0101 }
        r9 = "http://www.google.com";	 Catch:{ Exception -> 0x0101 }
        r9 = android.net.Uri.parse(r9);	 Catch:{ Exception -> 0x0101 }
        r5.<init>(r6, r9);	 Catch:{ Exception -> 0x0101 }
        r6 = r18.getPackageManager();	 Catch:{ Exception -> 0x0101 }
        r9 = 0;	 Catch:{ Exception -> 0x0101 }
        r6 = r6.queryIntentActivities(r5, r9);	 Catch:{ Exception -> 0x0101 }
        if (r6 == 0) goto L_0x0100;	 Catch:{ Exception -> 0x0101 }
        r9 = r6.isEmpty();	 Catch:{ Exception -> 0x0101 }
        if (r9 != 0) goto L_0x0100;	 Catch:{ Exception -> 0x0101 }
        r9 = r6.size();	 Catch:{ Exception -> 0x0101 }
        r9 = new java.lang.String[r9];	 Catch:{ Exception -> 0x0101 }
        r4 = r9;	 Catch:{ Exception -> 0x0101 }
        r9 = 0;	 Catch:{ Exception -> 0x0101 }
        r10 = r6.size();	 Catch:{ Exception -> 0x0101 }
        if (r9 >= r10) goto L_0x0100;	 Catch:{ Exception -> 0x0101 }
        r10 = r6.get(r9);	 Catch:{ Exception -> 0x0101 }
        r10 = (android.content.pm.ResolveInfo) r10;	 Catch:{ Exception -> 0x0101 }
        r10 = r10.activityInfo;	 Catch:{ Exception -> 0x0101 }
        r10 = r10.packageName;	 Catch:{ Exception -> 0x0101 }
        r4[r9] = r10;	 Catch:{ Exception -> 0x0101 }
        r10 = org.telegram.messenger.BuildVars.LOGS_ENABLED;	 Catch:{ Exception -> 0x0101 }
        if (r10 == 0) goto L_0x00fd;	 Catch:{ Exception -> 0x0101 }
        r10 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0101 }
        r10.<init>();	 Catch:{ Exception -> 0x0101 }
        r11 = "default browser name = ";	 Catch:{ Exception -> 0x0101 }
        r10.append(r11);	 Catch:{ Exception -> 0x0101 }
        r11 = r4[r9];	 Catch:{ Exception -> 0x0101 }
        r10.append(r11);	 Catch:{ Exception -> 0x0101 }
        r10 = r10.toString();	 Catch:{ Exception -> 0x0101 }
        org.telegram.messenger.FileLog.d(r10);	 Catch:{ Exception -> 0x0101 }
        r9 = r9 + 1;
        goto L_0x00d1;
        goto L_0x0102;
    L_0x0101:
        r0 = move-exception;
        r5 = new android.content.Intent;	 Catch:{ Exception -> 0x01b3 }
        r6 = "android.intent.action.VIEW";	 Catch:{ Exception -> 0x01b3 }
        r5.<init>(r6, r8);	 Catch:{ Exception -> 0x01b3 }
        r6 = r18.getPackageManager();	 Catch:{ Exception -> 0x01b3 }
        r9 = 0;	 Catch:{ Exception -> 0x01b3 }
        r6 = r6.queryIntentActivities(r5, r9);	 Catch:{ Exception -> 0x01b3 }
        r3 = r6;	 Catch:{ Exception -> 0x01b3 }
        if (r4 == 0) goto L_0x013f;	 Catch:{ Exception -> 0x01b3 }
        r6 = 0;	 Catch:{ Exception -> 0x01b3 }
        r9 = r3.size();	 Catch:{ Exception -> 0x01b3 }
        if (r6 >= r9) goto L_0x017a;	 Catch:{ Exception -> 0x01b3 }
        r9 = 0;	 Catch:{ Exception -> 0x01b3 }
        r10 = r4.length;	 Catch:{ Exception -> 0x01b3 }
        if (r9 >= r10) goto L_0x013c;	 Catch:{ Exception -> 0x01b3 }
        r10 = r4[r9];	 Catch:{ Exception -> 0x01b3 }
        r11 = r3.get(r6);	 Catch:{ Exception -> 0x01b3 }
        r11 = (android.content.pm.ResolveInfo) r11;	 Catch:{ Exception -> 0x01b3 }
        r11 = r11.activityInfo;	 Catch:{ Exception -> 0x01b3 }
        r11 = r11.packageName;	 Catch:{ Exception -> 0x01b3 }
        r10 = r10.equals(r11);	 Catch:{ Exception -> 0x01b3 }
        if (r10 == 0) goto L_0x0139;	 Catch:{ Exception -> 0x01b3 }
        r3.remove(r6);	 Catch:{ Exception -> 0x01b3 }
        r6 = r6 + -1;	 Catch:{ Exception -> 0x01b3 }
        goto L_0x013c;	 Catch:{ Exception -> 0x01b3 }
        r9 = r9 + 1;	 Catch:{ Exception -> 0x01b3 }
        goto L_0x011e;	 Catch:{ Exception -> 0x01b3 }
        r9 = 1;	 Catch:{ Exception -> 0x01b3 }
        r6 = r6 + r9;	 Catch:{ Exception -> 0x01b3 }
        goto L_0x0117;	 Catch:{ Exception -> 0x01b3 }
        r6 = 0;	 Catch:{ Exception -> 0x01b3 }
        r9 = r3.size();	 Catch:{ Exception -> 0x01b3 }
        if (r6 >= r9) goto L_0x017a;	 Catch:{ Exception -> 0x01b3 }
        r9 = r3.get(r6);	 Catch:{ Exception -> 0x01b3 }
        r9 = (android.content.pm.ResolveInfo) r9;	 Catch:{ Exception -> 0x01b3 }
        r9 = r9.activityInfo;	 Catch:{ Exception -> 0x01b3 }
        r9 = r9.packageName;	 Catch:{ Exception -> 0x01b3 }
        r9 = r9.toLowerCase();	 Catch:{ Exception -> 0x01b3 }
        r10 = "browser";	 Catch:{ Exception -> 0x01b3 }
        r9 = r9.contains(r10);	 Catch:{ Exception -> 0x01b3 }
        if (r9 != 0) goto L_0x0172;	 Catch:{ Exception -> 0x01b3 }
        r9 = r3.get(r6);	 Catch:{ Exception -> 0x01b3 }
        r9 = (android.content.pm.ResolveInfo) r9;	 Catch:{ Exception -> 0x01b3 }
        r9 = r9.activityInfo;	 Catch:{ Exception -> 0x01b3 }
        r9 = r9.packageName;	 Catch:{ Exception -> 0x01b3 }
        r9 = r9.toLowerCase();	 Catch:{ Exception -> 0x01b3 }
        r10 = "chrome";	 Catch:{ Exception -> 0x01b3 }
        r9 = r9.contains(r10);	 Catch:{ Exception -> 0x01b3 }
        if (r9 == 0) goto L_0x0177;	 Catch:{ Exception -> 0x01b3 }
        r3.remove(r6);	 Catch:{ Exception -> 0x01b3 }
        r6 = r6 + -1;	 Catch:{ Exception -> 0x01b3 }
        r9 = 1;	 Catch:{ Exception -> 0x01b3 }
        r6 = r6 + r9;	 Catch:{ Exception -> 0x01b3 }
        goto L_0x0140;	 Catch:{ Exception -> 0x01b3 }
        r6 = org.telegram.messenger.BuildVars.LOGS_ENABLED;	 Catch:{ Exception -> 0x01b3 }
        if (r6 == 0) goto L_0x01b2;	 Catch:{ Exception -> 0x01b3 }
        r6 = 0;	 Catch:{ Exception -> 0x01b3 }
        r9 = r3.size();	 Catch:{ Exception -> 0x01b3 }
        if (r6 >= r9) goto L_0x01b2;	 Catch:{ Exception -> 0x01b3 }
        r9 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01b3 }
        r9.<init>();	 Catch:{ Exception -> 0x01b3 }
        r10 = "device has ";	 Catch:{ Exception -> 0x01b3 }
        r9.append(r10);	 Catch:{ Exception -> 0x01b3 }
        r10 = r3.get(r6);	 Catch:{ Exception -> 0x01b3 }
        r10 = (android.content.pm.ResolveInfo) r10;	 Catch:{ Exception -> 0x01b3 }
        r10 = r10.activityInfo;	 Catch:{ Exception -> 0x01b3 }
        r10 = r10.packageName;	 Catch:{ Exception -> 0x01b3 }
        r9.append(r10);	 Catch:{ Exception -> 0x01b3 }
        r10 = " to open ";	 Catch:{ Exception -> 0x01b3 }
        r9.append(r10);	 Catch:{ Exception -> 0x01b3 }
        r10 = r19.toString();	 Catch:{ Exception -> 0x01b3 }
        r9.append(r10);	 Catch:{ Exception -> 0x01b3 }
        r9 = r9.toString();	 Catch:{ Exception -> 0x01b3 }
        org.telegram.messenger.FileLog.d(r9);	 Catch:{ Exception -> 0x01b3 }
        r6 = r6 + 1;
        goto L_0x017f;
        goto L_0x01b4;
    L_0x01b3:
        r0 = move-exception;
        r5 = 0;
        r6 = r12[r5];	 Catch:{ Exception -> 0x022d }
        if (r6 != 0) goto L_0x01c1;	 Catch:{ Exception -> 0x022d }
        if (r3 == 0) goto L_0x01c1;	 Catch:{ Exception -> 0x022d }
        r5 = r3.isEmpty();	 Catch:{ Exception -> 0x022d }
        if (r5 == 0) goto L_0x022c;	 Catch:{ Exception -> 0x022d }
        r5 = new android.content.Intent;	 Catch:{ Exception -> 0x022d }
        r6 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Exception -> 0x022d }
        r9 = org.telegram.messenger.ShareBroadcastReceiver.class;	 Catch:{ Exception -> 0x022d }
        r5.<init>(r6, r9);	 Catch:{ Exception -> 0x022d }
        r6 = "android.intent.action.SEND";	 Catch:{ Exception -> 0x022d }
        r5.setAction(r6);	 Catch:{ Exception -> 0x022d }
        r6 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Exception -> 0x022d }
        r9 = new android.content.Intent;	 Catch:{ Exception -> 0x022d }
        r10 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Exception -> 0x022d }
        r11 = org.telegram.messenger.CustomTabsCopyReceiver.class;	 Catch:{ Exception -> 0x022d }
        r9.<init>(r10, r11);	 Catch:{ Exception -> 0x022d }
        r10 = 134217728; // 0x8000000 float:3.85186E-34 double:6.63123685E-316;	 Catch:{ Exception -> 0x022d }
        r11 = 0;	 Catch:{ Exception -> 0x022d }
        r6 = android.app.PendingIntent.getBroadcast(r6, r11, r9, r10);	 Catch:{ Exception -> 0x022d }
        r9 = new org.telegram.messenger.support.customtabs.CustomTabsIntent$Builder;	 Catch:{ Exception -> 0x022d }
        r10 = getSession();	 Catch:{ Exception -> 0x022d }
        r9.<init>(r10);	 Catch:{ Exception -> 0x022d }
        r10 = "CopyLink";	 Catch:{ Exception -> 0x022d }
        r11 = 2131493304; // 0x7f0c01b8 float:1.8610084E38 double:1.053097616E-314;	 Catch:{ Exception -> 0x022d }
        r10 = org.telegram.messenger.LocaleController.getString(r10, r11);	 Catch:{ Exception -> 0x022d }
        r9.addMenuItem(r10, r6);	 Catch:{ Exception -> 0x022d }
        r10 = "actionBarDefault";	 Catch:{ Exception -> 0x022d }
        r10 = org.telegram.ui.ActionBar.Theme.getColor(r10);	 Catch:{ Exception -> 0x022d }
        r9.setToolbarColor(r10);	 Catch:{ Exception -> 0x022d }
        r10 = 1;	 Catch:{ Exception -> 0x022d }
        r9.setShowTitle(r10);	 Catch:{ Exception -> 0x022d }
        r10 = r18.getResources();	 Catch:{ Exception -> 0x022d }
        r11 = 2131165185; // 0x7f070001 float:1.794458E38 double:1.0529355035E-314;	 Catch:{ Exception -> 0x022d }
        r10 = android.graphics.BitmapFactory.decodeResource(r10, r11);	 Catch:{ Exception -> 0x022d }
        r11 = "ShareFile";	 Catch:{ Exception -> 0x022d }
        r14 = 2131494383; // 0x7f0c05ef float:1.8612273E38 double:1.053098149E-314;	 Catch:{ Exception -> 0x022d }
        r11 = org.telegram.messenger.LocaleController.getString(r11, r14);	 Catch:{ Exception -> 0x022d }
        r14 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Exception -> 0x022d }
        r15 = 0;	 Catch:{ Exception -> 0x022d }
        r14 = android.app.PendingIntent.getBroadcast(r14, r15, r5, r15);	 Catch:{ Exception -> 0x022d }
        r9.setActionButton(r10, r11, r14, r15);	 Catch:{ Exception -> 0x022d }
        r10 = r9.build();	 Catch:{ Exception -> 0x022d }
        r10.setUseNewTask();	 Catch:{ Exception -> 0x022d }
        r10.launchUrl(r7, r8);	 Catch:{ Exception -> 0x022d }
        return;
        goto L_0x0232;
    L_0x022d:
        r0 = move-exception;
        r1 = r0;
        org.telegram.messenger.FileLog.e(r1);
        r1 = new android.content.Intent;	 Catch:{ Exception -> 0x0260 }
        r3 = "android.intent.action.VIEW";	 Catch:{ Exception -> 0x0260 }
        r1.<init>(r3, r8);	 Catch:{ Exception -> 0x0260 }
        if (r13 == 0) goto L_0x024d;	 Catch:{ Exception -> 0x0260 }
        r3 = new android.content.ComponentName;	 Catch:{ Exception -> 0x0260 }
        r4 = r18.getPackageName();	 Catch:{ Exception -> 0x0260 }
        r5 = org.telegram.ui.LaunchActivity.class;	 Catch:{ Exception -> 0x0260 }
        r5 = r5.getName();	 Catch:{ Exception -> 0x0260 }
        r3.<init>(r4, r5);	 Catch:{ Exception -> 0x0260 }
        r1.setComponent(r3);	 Catch:{ Exception -> 0x0260 }
        r3 = "create_new_tab";	 Catch:{ Exception -> 0x0260 }
        r4 = 1;	 Catch:{ Exception -> 0x0260 }
        r1.putExtra(r3, r4);	 Catch:{ Exception -> 0x0260 }
        r3 = "com.android.browser.application_id";	 Catch:{ Exception -> 0x0260 }
        r4 = r18.getPackageName();	 Catch:{ Exception -> 0x0260 }
        r1.putExtra(r3, r4);	 Catch:{ Exception -> 0x0260 }
        r7.startActivity(r1);	 Catch:{ Exception -> 0x0260 }
        goto L_0x0265;
    L_0x0260:
        r0 = move-exception;
        r1 = r0;
        org.telegram.messenger.FileLog.e(r1);
        return;
    L_0x0266:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.browser.Browser.openUrl(android.content.Context, android.net.Uri, boolean, boolean):void");
    }

    private static CustomTabsSession getCurrentSession() {
        return customTabsCurrentSession == null ? null : (CustomTabsSession) customTabsCurrentSession.get();
    }

    private static void setCurrentSession(CustomTabsSession session) {
        customTabsCurrentSession = new WeakReference(session);
    }

    private static CustomTabsSession getSession() {
        if (customTabsClient == null) {
            customTabsSession = null;
        } else if (customTabsSession == null) {
            customTabsSession = customTabsClient.newSession(new NavigationCallback());
            setCurrentSession(customTabsSession);
        }
        return customTabsSession;
    }

    public static void bindCustomTabsService(Activity activity) {
        Activity currentActivity = currentCustomTabsActivity == null ? null : (Activity) currentCustomTabsActivity.get();
        if (!(currentActivity == null || currentActivity == activity)) {
            unbindCustomTabsService(currentActivity);
        }
        if (customTabsClient == null) {
            currentCustomTabsActivity = new WeakReference(activity);
            try {
                if (TextUtils.isEmpty(customTabsPackageToBind)) {
                    customTabsPackageToBind = CustomTabsHelper.getPackageNameToUse(activity);
                    if (customTabsPackageToBind == null) {
                        return;
                    }
                }
                customTabsServiceConnection = new ServiceConnection(new ServiceConnectionCallback() {
                    public void onServiceConnected(CustomTabsClient client) {
                        Browser.customTabsClient = client;
                        if (SharedConfig.customTabs && Browser.customTabsClient != null) {
                            try {
                                Browser.customTabsClient.warmup(0);
                            } catch (Throwable e) {
                                FileLog.e(e);
                            }
                        }
                    }

                    public void onServiceDisconnected() {
                        Browser.customTabsClient = null;
                    }
                });
                if (!CustomTabsClient.bindCustomTabsService(activity, customTabsPackageToBind, customTabsServiceConnection)) {
                    customTabsServiceConnection = null;
                }
            } catch (Throwable e) {
                FileLog.e(e);
            }
        }
    }

    public static void unbindCustomTabsService(Activity activity) {
        if (customTabsServiceConnection != null) {
            if ((currentCustomTabsActivity == null ? null : (Activity) currentCustomTabsActivity.get()) == activity) {
                currentCustomTabsActivity.clear();
            }
            try {
                activity.unbindService(customTabsServiceConnection);
            } catch (Exception e) {
            }
            customTabsClient = null;
            customTabsSession = null;
        }
    }

    public static void openUrl(Context context, String url) {
        if (url != null) {
            openUrl(context, Uri.parse(url), true);
        }
    }

    public static void openUrl(Context context, Uri uri) {
        openUrl(context, uri, true);
    }

    public static void openUrl(Context context, String url, boolean allowCustom) {
        if (context != null) {
            if (url != null) {
                openUrl(context, Uri.parse(url), allowCustom);
            }
        }
    }

    public static void openUrl(Context context, Uri uri, boolean allowCustom) {
        openUrl(context, uri, allowCustom, true);
    }

    public static boolean isInternalUrl(String url, boolean[] forceBrowser) {
        return isInternalUri(Uri.parse(url), forceBrowser);
    }

    public static boolean isInternalUri(Uri uri, boolean[] forceBrowser) {
        String host = uri.getHost();
        host = host != null ? host.toLowerCase() : TtmlNode.ANONYMOUS_REGION_ID;
        if ("tg".equals(uri.getScheme())) {
            return true;
        }
        String path;
        if ("telegram.dog".equals(host)) {
            path = uri.getPath();
            if (path != null && path.length() > 1) {
                path = path.substring(1).toLowerCase();
                if (!(path.startsWith("blog") || path.equals("iv") || path.startsWith("faq"))) {
                    if (!path.equals("apps")) {
                        return true;
                    }
                }
                if (forceBrowser != null) {
                    forceBrowser[0] = true;
                }
                return false;
            }
        } else if ("telegram.me".equals(host) || "t.me".equals(host) || "telesco.pe".equals(host)) {
            path = uri.getPath();
            if (path != null && path.length() > 1) {
                if (!path.substring(1).toLowerCase().equals("iv")) {
                    return true;
                }
                if (forceBrowser != null) {
                    forceBrowser[0] = true;
                }
                return false;
            }
        }
        return false;
    }
}
