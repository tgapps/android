package org.telegram.tgnet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.util.Base64;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings.Builder;
import java.io.File;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildConfig;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.KeepAliveJob;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.StatsController;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.exoplayer2.DefaultRenderersFactory;
import org.telegram.tgnet.TLRPC.TL_config;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.tgnet.TLRPC.Updates;

public class ConnectionsManager {
    public static final int ConnectionStateConnected = 3;
    public static final int ConnectionStateConnecting = 1;
    public static final int ConnectionStateConnectingToProxy = 4;
    public static final int ConnectionStateUpdating = 5;
    public static final int ConnectionStateWaitingForNetwork = 2;
    public static final int ConnectionTypeDownload = 2;
    public static final int ConnectionTypeDownload2 = 65538;
    public static final int ConnectionTypeGeneric = 1;
    public static final int ConnectionTypePush = 8;
    public static final int ConnectionTypeUpload = 4;
    public static final int DEFAULT_DATACENTER_ID = Integer.MAX_VALUE;
    public static final int FileTypeAudio = 50331648;
    public static final int FileTypeFile = 67108864;
    public static final int FileTypePhoto = 16777216;
    public static final int FileTypeVideo = 33554432;
    private static volatile ConnectionsManager[] Instance = new ConnectionsManager[3];
    public static final int RequestFlagCanCompress = 4;
    public static final int RequestFlagEnableUnauthorized = 1;
    public static final int RequestFlagFailOnServerErrors = 2;
    public static final int RequestFlagForceDownload = 32;
    public static final int RequestFlagInvokeAfter = 64;
    public static final int RequestFlagNeedQuickAck = 128;
    public static final int RequestFlagTryDifferentDc = 16;
    public static final int RequestFlagWithoutLogin = 8;
    private static AsyncTask currentTask;
    private static ThreadLocal<HashMap<String, ResolvedDomain>> dnsCache = new ThreadLocal<HashMap<String, ResolvedDomain>>() {
        protected HashMap<String, ResolvedDomain> initialValue() {
            return new HashMap();
        }
    };
    private static final int dnsConfigVersion = 0;
    private static int lastClassGuid = 1;
    private static long lastDnsRequestTime;
    private boolean appPaused = true;
    private int appResumeCount;
    private int connectionState;
    private int currentAccount;
    private boolean isUpdating;
    private long lastPauseTime = System.currentTimeMillis();
    private AtomicInteger lastRequestToken = new AtomicInteger(1);

    private static class AzureLoadTask extends AsyncTask<Void, Void, NativeByteBuffer> {
        private int currentAccount;

        protected org.telegram.tgnet.NativeByteBuffer doInBackground(java.lang.Void... r1) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.tgnet.ConnectionsManager.AzureLoadTask.doInBackground(java.lang.Void[]):org.telegram.tgnet.NativeByteBuffer
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
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
            r0 = 0;
            r1 = 0;
            r2 = r1;
            r3 = r9.currentAccount;	 Catch:{ Throwable -> 0x0087 }
            r3 = org.telegram.tgnet.ConnectionsManager.native_isTestBackend(r3);	 Catch:{ Throwable -> 0x0087 }
            if (r3 == 0) goto L_0x0013;	 Catch:{ Throwable -> 0x0087 }
        L_0x000b:
            r3 = new java.net.URL;	 Catch:{ Throwable -> 0x0087 }
            r4 = "https://software-download.microsoft.com/test/config.txt";	 Catch:{ Throwable -> 0x0087 }
            r3.<init>(r4);	 Catch:{ Throwable -> 0x0087 }
            goto L_0x001a;	 Catch:{ Throwable -> 0x0087 }
        L_0x0013:
            r3 = new java.net.URL;	 Catch:{ Throwable -> 0x0087 }
            r4 = "https://software-download.microsoft.com/prod/config.txt";	 Catch:{ Throwable -> 0x0087 }
            r3.<init>(r4);	 Catch:{ Throwable -> 0x0087 }
        L_0x001a:
            r4 = r3.openConnection();	 Catch:{ Throwable -> 0x0087 }
            r5 = "User-Agent";	 Catch:{ Throwable -> 0x0087 }
            r6 = "Mozilla/5.0 (iPhone; CPU iPhone OS 10_0 like Mac OS X) AppleWebKit/602.1.38 (KHTML, like Gecko) Version/10.0 Mobile/14A5297c Safari/602.1";	 Catch:{ Throwable -> 0x0087 }
            r4.addRequestProperty(r5, r6);	 Catch:{ Throwable -> 0x0087 }
            r5 = "Host";	 Catch:{ Throwable -> 0x0087 }
            r6 = "tcdnb.azureedge.net";	 Catch:{ Throwable -> 0x0087 }
            r4.addRequestProperty(r5, r6);	 Catch:{ Throwable -> 0x0087 }
            r5 = 5000; // 0x1388 float:7.006E-42 double:2.4703E-320;	 Catch:{ Throwable -> 0x0087 }
            r4.setConnectTimeout(r5);	 Catch:{ Throwable -> 0x0087 }
            r4.setReadTimeout(r5);	 Catch:{ Throwable -> 0x0087 }
            r4.connect();	 Catch:{ Throwable -> 0x0087 }
            r5 = r4.getInputStream();	 Catch:{ Throwable -> 0x0087 }
            r2 = r5;	 Catch:{ Throwable -> 0x0087 }
            r5 = new java.io.ByteArrayOutputStream;	 Catch:{ Throwable -> 0x0087 }
            r5.<init>();	 Catch:{ Throwable -> 0x0087 }
            r0 = r5;	 Catch:{ Throwable -> 0x0087 }
            r5 = 32768; // 0x8000 float:4.5918E-41 double:1.61895E-319;	 Catch:{ Throwable -> 0x0087 }
            r5 = new byte[r5];	 Catch:{ Throwable -> 0x0087 }
        L_0x0047:
            r6 = r9.isCancelled();	 Catch:{ Throwable -> 0x0087 }
            r7 = 0;	 Catch:{ Throwable -> 0x0087 }
            if (r6 == 0) goto L_0x004f;	 Catch:{ Throwable -> 0x0087 }
        L_0x004e:
            goto L_0x005d;	 Catch:{ Throwable -> 0x0087 }
        L_0x004f:
            r6 = r2.read(r5);	 Catch:{ Throwable -> 0x0087 }
            if (r6 <= 0) goto L_0x0059;	 Catch:{ Throwable -> 0x0087 }
        L_0x0055:
            r0.write(r5, r7, r6);	 Catch:{ Throwable -> 0x0087 }
            goto L_0x0047;	 Catch:{ Throwable -> 0x0087 }
        L_0x0059:
            r8 = -1;	 Catch:{ Throwable -> 0x0087 }
            if (r6 != r8) goto L_0x005d;	 Catch:{ Throwable -> 0x0087 }
        L_0x005d:
            r6 = r0.toByteArray();	 Catch:{ Throwable -> 0x0087 }
            r6 = android.util.Base64.decode(r6, r7);	 Catch:{ Throwable -> 0x0087 }
            r7 = new org.telegram.tgnet.NativeByteBuffer;	 Catch:{ Throwable -> 0x0087 }
            r8 = r6.length;	 Catch:{ Throwable -> 0x0087 }
            r7.<init>(r8);	 Catch:{ Throwable -> 0x0087 }
            r7.writeBytes(r6);	 Catch:{ Throwable -> 0x0087 }
            if (r2 == 0) goto L_0x007a;
        L_0x0071:
            r2.close();	 Catch:{ Throwable -> 0x0075 }
            goto L_0x007a;
        L_0x0075:
            r1 = move-exception;
            org.telegram.messenger.FileLog.e(r1);
            goto L_0x007b;
            if (r0 == 0) goto L_0x0083;
            r0.close();	 Catch:{ Exception -> 0x0081 }
            goto L_0x0083;
        L_0x0081:
            r1 = move-exception;
            goto L_0x0084;
            return r7;
        L_0x0085:
            r1 = move-exception;
            goto L_0x00a1;
        L_0x0087:
            r3 = move-exception;
            org.telegram.messenger.FileLog.e(r3);	 Catch:{ all -> 0x0085 }
            if (r2 == 0) goto L_0x0096;
            r2.close();	 Catch:{ Throwable -> 0x0091 }
            goto L_0x0096;
        L_0x0091:
            r3 = move-exception;
            org.telegram.messenger.FileLog.e(r3);
            goto L_0x0097;
            if (r0 == 0) goto L_0x009f;
            r0.close();	 Catch:{ Exception -> 0x009d }
            goto L_0x009f;
        L_0x009d:
            r3 = move-exception;
            goto L_0x00a0;
            return r1;
            if (r2 == 0) goto L_0x00ad;
            r2.close();	 Catch:{ Throwable -> 0x00a8 }
            goto L_0x00ad;
        L_0x00a8:
            r3 = move-exception;
            org.telegram.messenger.FileLog.e(r3);
            goto L_0x00ae;
            if (r0 == 0) goto L_0x00b6;
            r0.close();	 Catch:{ Exception -> 0x00b4 }
            goto L_0x00b6;
        L_0x00b4:
            r3 = move-exception;
            throw r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.telegram.tgnet.ConnectionsManager.AzureLoadTask.doInBackground(java.lang.Void[]):org.telegram.tgnet.NativeByteBuffer");
        }

        public AzureLoadTask(int instance) {
            this.currentAccount = instance;
        }

        protected void onPostExecute(final NativeByteBuffer result) {
            Utilities.stageQueue.postRunnable(new Runnable() {
                public void run() {
                    if (result != null) {
                        ConnectionsManager.currentTask = null;
                        ConnectionsManager.native_applyDnsConfig(AzureLoadTask.this.currentAccount, result.address);
                        return;
                    }
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("failed to get azure result");
                        FileLog.d("start dns txt task");
                    }
                    DnsTxtLoadTask task = new DnsTxtLoadTask(AzureLoadTask.this.currentAccount);
                    task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[]{null, null, null});
                    ConnectionsManager.currentTask = task;
                }
            });
        }
    }

    private static class DnsTxtLoadTask extends AsyncTask<Void, Void, NativeByteBuffer> {
        private int currentAccount;

        protected org.telegram.tgnet.NativeByteBuffer doInBackground(java.lang.Void... r1) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.tgnet.ConnectionsManager.DnsTxtLoadTask.doInBackground(java.lang.Void[]):org.telegram.tgnet.NativeByteBuffer
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
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
            r1 = r17;
            r2 = 0;
            r3 = 0;
            r4 = r3;
            r5 = java.util.Locale.US;	 Catch:{ Throwable -> 0x0108 }
            r6 = r1.currentAccount;	 Catch:{ Throwable -> 0x0108 }
            r6 = org.telegram.tgnet.ConnectionsManager.native_isTestBackend(r6);	 Catch:{ Throwable -> 0x0108 }
            if (r6 == 0) goto L_0x0012;	 Catch:{ Throwable -> 0x0108 }
        L_0x000f:
            r6 = "tap%1$s.stel.com";	 Catch:{ Throwable -> 0x0108 }
            goto L_0x0014;	 Catch:{ Throwable -> 0x0108 }
        L_0x0012:
            r6 = "ap%1$s.stel.com";	 Catch:{ Throwable -> 0x0108 }
        L_0x0014:
            r7 = 1;	 Catch:{ Throwable -> 0x0108 }
            r7 = new java.lang.Object[r7];	 Catch:{ Throwable -> 0x0108 }
            r8 = "";	 Catch:{ Throwable -> 0x0108 }
            r9 = 0;	 Catch:{ Throwable -> 0x0108 }
            r7[r9] = r8;	 Catch:{ Throwable -> 0x0108 }
            r5 = java.lang.String.format(r5, r6, r7);	 Catch:{ Throwable -> 0x0108 }
            r6 = new java.net.URL;	 Catch:{ Throwable -> 0x0108 }
            r7 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x0108 }
            r7.<init>();	 Catch:{ Throwable -> 0x0108 }
            r8 = "https://google.com/resolve?name=";	 Catch:{ Throwable -> 0x0108 }
            r7.append(r8);	 Catch:{ Throwable -> 0x0108 }
            r7.append(r5);	 Catch:{ Throwable -> 0x0108 }
            r8 = "&type=16";	 Catch:{ Throwable -> 0x0108 }
            r7.append(r8);	 Catch:{ Throwable -> 0x0108 }
            r7 = r7.toString();	 Catch:{ Throwable -> 0x0108 }
            r6.<init>(r7);	 Catch:{ Throwable -> 0x0108 }
            r7 = r6.openConnection();	 Catch:{ Throwable -> 0x0108 }
            r8 = "User-Agent";	 Catch:{ Throwable -> 0x0108 }
            r10 = "Mozilla/5.0 (iPhone; CPU iPhone OS 10_0 like Mac OS X) AppleWebKit/602.1.38 (KHTML, like Gecko) Version/10.0 Mobile/14A5297c Safari/602.1";	 Catch:{ Throwable -> 0x0108 }
            r7.addRequestProperty(r8, r10);	 Catch:{ Throwable -> 0x0108 }
            r8 = "Host";	 Catch:{ Throwable -> 0x0108 }
            r10 = "dns.google.com";	 Catch:{ Throwable -> 0x0108 }
            r7.addRequestProperty(r8, r10);	 Catch:{ Throwable -> 0x0108 }
            r8 = 5000; // 0x1388 float:7.006E-42 double:2.4703E-320;	 Catch:{ Throwable -> 0x0108 }
            r7.setConnectTimeout(r8);	 Catch:{ Throwable -> 0x0108 }
            r7.setReadTimeout(r8);	 Catch:{ Throwable -> 0x0108 }
            r7.connect();	 Catch:{ Throwable -> 0x0108 }
            r8 = r7.getInputStream();	 Catch:{ Throwable -> 0x0108 }
            r4 = r8;	 Catch:{ Throwable -> 0x0108 }
            r8 = new java.io.ByteArrayOutputStream;	 Catch:{ Throwable -> 0x0108 }
            r8.<init>();	 Catch:{ Throwable -> 0x0108 }
            r2 = r8;	 Catch:{ Throwable -> 0x0108 }
            r8 = 32768; // 0x8000 float:4.5918E-41 double:1.61895E-319;	 Catch:{ Throwable -> 0x0108 }
            r8 = new byte[r8];	 Catch:{ Throwable -> 0x0108 }
        L_0x0068:
            r10 = r17.isCancelled();	 Catch:{ Throwable -> 0x0108 }
            if (r10 == 0) goto L_0x006f;	 Catch:{ Throwable -> 0x0108 }
        L_0x006e:
            goto L_0x007d;	 Catch:{ Throwable -> 0x0108 }
        L_0x006f:
            r10 = r4.read(r8);	 Catch:{ Throwable -> 0x0108 }
            if (r10 <= 0) goto L_0x0079;	 Catch:{ Throwable -> 0x0108 }
        L_0x0075:
            r2.write(r8, r9, r10);	 Catch:{ Throwable -> 0x0108 }
            goto L_0x0068;	 Catch:{ Throwable -> 0x0108 }
        L_0x0079:
            r11 = -1;	 Catch:{ Throwable -> 0x0108 }
            if (r10 != r11) goto L_0x007d;	 Catch:{ Throwable -> 0x0108 }
        L_0x007d:
            r10 = new org.json.JSONObject;	 Catch:{ Throwable -> 0x0108 }
            r11 = new java.lang.String;	 Catch:{ Throwable -> 0x0108 }
            r12 = r2.toByteArray();	 Catch:{ Throwable -> 0x0108 }
            r13 = "UTF-8";	 Catch:{ Throwable -> 0x0108 }
            r11.<init>(r12, r13);	 Catch:{ Throwable -> 0x0108 }
            r10.<init>(r11);	 Catch:{ Throwable -> 0x0108 }
            r11 = "Answer";	 Catch:{ Throwable -> 0x0108 }
            r11 = r10.getJSONArray(r11);	 Catch:{ Throwable -> 0x0108 }
            r12 = r11.length();	 Catch:{ Throwable -> 0x0108 }
            r13 = new java.util.ArrayList;	 Catch:{ Throwable -> 0x0108 }
            r13.<init>(r12);	 Catch:{ Throwable -> 0x0108 }
            r14 = r9;	 Catch:{ Throwable -> 0x0108 }
        L_0x009d:
            if (r14 >= r12) goto L_0x00b0;	 Catch:{ Throwable -> 0x0108 }
        L_0x009f:
            r15 = r11.getJSONObject(r14);	 Catch:{ Throwable -> 0x0108 }
            r3 = "data";	 Catch:{ Throwable -> 0x0108 }
            r3 = r15.getString(r3);	 Catch:{ Throwable -> 0x0108 }
            r13.add(r3);	 Catch:{ Throwable -> 0x0108 }
            r14 = r14 + 1;	 Catch:{ Throwable -> 0x0108 }
            r3 = 0;	 Catch:{ Throwable -> 0x0108 }
            goto L_0x009d;	 Catch:{ Throwable -> 0x0108 }
        L_0x00b0:
            r3 = new org.telegram.tgnet.ConnectionsManager$DnsTxtLoadTask$1;	 Catch:{ Throwable -> 0x0108 }
            r3.<init>();	 Catch:{ Throwable -> 0x0108 }
            java.util.Collections.sort(r13, r3);	 Catch:{ Throwable -> 0x0108 }
            r3 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x0108 }
            r3.<init>();	 Catch:{ Throwable -> 0x0108 }
            r14 = r9;	 Catch:{ Throwable -> 0x0108 }
        L_0x00be:
            r15 = r13.size();	 Catch:{ Throwable -> 0x0108 }
            if (r14 >= r15) goto L_0x00db;	 Catch:{ Throwable -> 0x0108 }
        L_0x00c4:
            r15 = r13.get(r14);	 Catch:{ Throwable -> 0x0108 }
            r15 = (java.lang.String) r15;	 Catch:{ Throwable -> 0x0108 }
            r9 = "\"";	 Catch:{ Throwable -> 0x0108 }
            r1 = "";	 Catch:{ Throwable -> 0x0108 }
            r1 = r15.replace(r9, r1);	 Catch:{ Throwable -> 0x0108 }
            r3.append(r1);	 Catch:{ Throwable -> 0x0108 }
            r14 = r14 + 1;	 Catch:{ Throwable -> 0x0108 }
            r1 = r17;	 Catch:{ Throwable -> 0x0108 }
            r9 = 0;	 Catch:{ Throwable -> 0x0108 }
            goto L_0x00be;	 Catch:{ Throwable -> 0x0108 }
        L_0x00db:
            r1 = r3.toString();	 Catch:{ Throwable -> 0x0108 }
            r9 = 0;	 Catch:{ Throwable -> 0x0108 }
            r1 = android.util.Base64.decode(r1, r9);	 Catch:{ Throwable -> 0x0108 }
            r9 = new org.telegram.tgnet.NativeByteBuffer;	 Catch:{ Throwable -> 0x0108 }
            r14 = r1.length;	 Catch:{ Throwable -> 0x0108 }
            r9.<init>(r14);	 Catch:{ Throwable -> 0x0108 }
            r9.writeBytes(r1);	 Catch:{ Throwable -> 0x0108 }
            if (r4 == 0) goto L_0x00fa;
        L_0x00f0:
            r4.close();	 Catch:{ Throwable -> 0x00f4 }
            goto L_0x00fa;
        L_0x00f4:
            r0 = move-exception;
            r14 = r0;
            org.telegram.messenger.FileLog.e(r14);
            goto L_0x00fb;
            if (r2 == 0) goto L_0x0103;
            r2.close();	 Catch:{ Exception -> 0x0101 }
            goto L_0x0103;
        L_0x0101:
            r0 = move-exception;
            goto L_0x0104;
            return r9;
        L_0x0105:
            r0 = move-exception;
            r1 = r0;
            goto L_0x0125;
        L_0x0108:
            r0 = move-exception;
            r1 = r0;
            org.telegram.messenger.FileLog.e(r1);	 Catch:{ all -> 0x0105 }
            if (r4 == 0) goto L_0x0119;
            r4.close();	 Catch:{ Throwable -> 0x0113 }
            goto L_0x0119;
        L_0x0113:
            r0 = move-exception;
            r1 = r0;
            org.telegram.messenger.FileLog.e(r1);
            goto L_0x011a;
            if (r2 == 0) goto L_0x0122;
            r2.close();	 Catch:{ Exception -> 0x0120 }
            goto L_0x0122;
        L_0x0120:
            r0 = move-exception;
            goto L_0x0123;
            r1 = 0;
            return r1;
            if (r4 == 0) goto L_0x0132;
            r4.close();	 Catch:{ Throwable -> 0x012c }
            goto L_0x0132;
        L_0x012c:
            r0 = move-exception;
            r3 = r0;
            org.telegram.messenger.FileLog.e(r3);
            goto L_0x0133;
            if (r2 == 0) goto L_0x013b;
            r2.close();	 Catch:{ Exception -> 0x0139 }
            goto L_0x013b;
        L_0x0139:
            r0 = move-exception;
            throw r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.telegram.tgnet.ConnectionsManager.DnsTxtLoadTask.doInBackground(java.lang.Void[]):org.telegram.tgnet.NativeByteBuffer");
        }

        public DnsTxtLoadTask(int instance) {
            this.currentAccount = instance;
        }

        protected void onPostExecute(final NativeByteBuffer result) {
            Utilities.stageQueue.postRunnable(new Runnable() {
                public void run() {
                    if (result != null) {
                        ConnectionsManager.native_applyDnsConfig(DnsTxtLoadTask.this.currentAccount, result.address);
                    } else if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("failed to get dns txt result");
                    }
                    ConnectionsManager.currentTask = null;
                }
            });
        }
    }

    private static class FirebaseTask extends AsyncTask<Void, Void, NativeByteBuffer> {
        private int currentAccount;
        private FirebaseRemoteConfig firebaseRemoteConfig;

        public FirebaseTask(int instance) {
            this.currentAccount = instance;
        }

        protected NativeByteBuffer doInBackground(Void... voids) {
            try {
                this.firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
                this.firebaseRemoteConfig.setConfigSettings(new Builder().setDeveloperModeEnabled(BuildConfig.DEBUG).build());
                String currentValue = this.firebaseRemoteConfig.getString("ipconfig");
                if (BuildVars.LOGS_ENABLED) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("current firebase value = ");
                    stringBuilder.append(currentValue);
                    FileLog.d(stringBuilder.toString());
                }
                this.firebaseRemoteConfig.fetch(0).addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(Task<Void> finishedTask) {
                        final boolean success = finishedTask.isSuccessful();
                        Utilities.stageQueue.postRunnable(new Runnable() {
                            public void run() {
                                ConnectionsManager.currentTask = null;
                                String config = null;
                                if (success) {
                                    FirebaseTask.this.firebaseRemoteConfig.activateFetched();
                                    config = FirebaseTask.this.firebaseRemoteConfig.getString("ipconfig");
                                }
                                if (TextUtils.isEmpty(config)) {
                                    if (BuildVars.LOGS_ENABLED) {
                                        FileLog.d("failed to get firebase result");
                                        FileLog.d("start azure task");
                                    }
                                    AzureLoadTask task = new AzureLoadTask(FirebaseTask.this.currentAccount);
                                    task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[]{null, null, null});
                                    ConnectionsManager.currentTask = task;
                                    return;
                                }
                                byte[] bytes = Base64.decode(config, 0);
                                try {
                                    NativeByteBuffer buffer = new NativeByteBuffer(bytes.length);
                                    buffer.writeBytes(bytes);
                                    ConnectionsManager.native_applyDnsConfig(FirebaseTask.this.currentAccount, buffer.address);
                                } catch (Throwable e) {
                                    ConnectionsManager.currentTask = null;
                                    FileLog.e(e);
                                }
                            }
                        });
                    }
                });
            } catch (Throwable e) {
                FileLog.e(e);
            }
            return null;
        }

        protected void onPostExecute(NativeByteBuffer result) {
        }
    }

    private static class ResolvedDomain {
        public String address;
        long ttl;

        public ResolvedDomain(String a, long t) {
            this.address = a;
            this.ttl = t;
        }
    }

    public static java.lang.String getHostByName(java.lang.String r1, int r2) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.tgnet.ConnectionsManager.getHostByName(java.lang.String, int):java.lang.String
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
        r0 = dnsCache;
        r0 = r0.get();
        r0 = (java.util.HashMap) r0;
        r1 = r0.get(r14);
        r1 = (org.telegram.tgnet.ConnectionsManager.ResolvedDomain) r1;
        if (r1 == 0) goto L_0x0022;
    L_0x0010:
        r2 = android.os.SystemClock.uptimeMillis();
        r4 = r1.ttl;
        r6 = r2 - r4;
        r2 = 300000; // 0x493e0 float:4.2039E-40 double:1.482197E-318;
        r4 = (r6 > r2 ? 1 : (r6 == r2 ? 0 : -1));
        if (r4 >= 0) goto L_0x0022;
    L_0x001f:
        r2 = r1.address;
        return r2;
    L_0x0022:
        r2 = 0;
        r3 = 0;
        r4 = new java.net.URL;	 Catch:{ Throwable -> 0x00e5 }
        r5 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x00e5 }
        r5.<init>();	 Catch:{ Throwable -> 0x00e5 }
        r6 = "https://google.com/resolve?name=";	 Catch:{ Throwable -> 0x00e5 }
        r5.append(r6);	 Catch:{ Throwable -> 0x00e5 }
        r5.append(r14);	 Catch:{ Throwable -> 0x00e5 }
        r6 = "&type=A";	 Catch:{ Throwable -> 0x00e5 }
        r5.append(r6);	 Catch:{ Throwable -> 0x00e5 }
        r5 = r5.toString();	 Catch:{ Throwable -> 0x00e5 }
        r4.<init>(r5);	 Catch:{ Throwable -> 0x00e5 }
        r5 = r4.openConnection();	 Catch:{ Throwable -> 0x00e5 }
        r6 = "User-Agent";	 Catch:{ Throwable -> 0x00e5 }
        r7 = "Mozilla/5.0 (iPhone; CPU iPhone OS 10_0 like Mac OS X) AppleWebKit/602.1.38 (KHTML, like Gecko) Version/10.0 Mobile/14A5297c Safari/602.1";	 Catch:{ Throwable -> 0x00e5 }
        r5.addRequestProperty(r6, r7);	 Catch:{ Throwable -> 0x00e5 }
        r6 = "Host";	 Catch:{ Throwable -> 0x00e5 }
        r7 = "dns.google.com";	 Catch:{ Throwable -> 0x00e5 }
        r5.addRequestProperty(r6, r7);	 Catch:{ Throwable -> 0x00e5 }
        r6 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;	 Catch:{ Throwable -> 0x00e5 }
        r5.setConnectTimeout(r6);	 Catch:{ Throwable -> 0x00e5 }
        r6 = 2000; // 0x7d0 float:2.803E-42 double:9.88E-321;	 Catch:{ Throwable -> 0x00e5 }
        r5.setReadTimeout(r6);	 Catch:{ Throwable -> 0x00e5 }
        r5.connect();	 Catch:{ Throwable -> 0x00e5 }
        r6 = r5.getInputStream();	 Catch:{ Throwable -> 0x00e5 }
        r3 = r6;	 Catch:{ Throwable -> 0x00e5 }
        r6 = new java.io.ByteArrayOutputStream;	 Catch:{ Throwable -> 0x00e5 }
        r6.<init>();	 Catch:{ Throwable -> 0x00e5 }
        r2 = r6;	 Catch:{ Throwable -> 0x00e5 }
        r6 = 32768; // 0x8000 float:4.5918E-41 double:1.61895E-319;	 Catch:{ Throwable -> 0x00e5 }
        r6 = new byte[r6];	 Catch:{ Throwable -> 0x00e5 }
    L_0x006e:
        r7 = r3.read(r6);	 Catch:{ Throwable -> 0x00e5 }
        if (r7 <= 0) goto L_0x0079;	 Catch:{ Throwable -> 0x00e5 }
    L_0x0074:
        r8 = 0;	 Catch:{ Throwable -> 0x00e5 }
        r2.write(r6, r8, r7);	 Catch:{ Throwable -> 0x00e5 }
        goto L_0x006e;	 Catch:{ Throwable -> 0x00e5 }
    L_0x0079:
        r8 = -1;	 Catch:{ Throwable -> 0x00e5 }
        if (r7 != r8) goto L_0x007d;	 Catch:{ Throwable -> 0x00e5 }
    L_0x007d:
        r7 = new org.json.JSONObject;	 Catch:{ Throwable -> 0x00e5 }
        r8 = new java.lang.String;	 Catch:{ Throwable -> 0x00e5 }
        r9 = r2.toByteArray();	 Catch:{ Throwable -> 0x00e5 }
        r8.<init>(r9);	 Catch:{ Throwable -> 0x00e5 }
        r7.<init>(r8);	 Catch:{ Throwable -> 0x00e5 }
        r8 = "Answer";	 Catch:{ Throwable -> 0x00e5 }
        r8 = r7.getJSONArray(r8);	 Catch:{ Throwable -> 0x00e5 }
        r9 = r8.length();	 Catch:{ Throwable -> 0x00e5 }
        if (r9 <= 0) goto L_0x00ce;	 Catch:{ Throwable -> 0x00e5 }
    L_0x0097:
        r10 = org.telegram.messenger.Utilities.random;	 Catch:{ Throwable -> 0x00e5 }
        r11 = r8.length();	 Catch:{ Throwable -> 0x00e5 }
        r10 = r10.nextInt(r11);	 Catch:{ Throwable -> 0x00e5 }
        r10 = r8.getJSONObject(r10);	 Catch:{ Throwable -> 0x00e5 }
        r11 = "data";	 Catch:{ Throwable -> 0x00e5 }
        r10 = r10.getString(r11);	 Catch:{ Throwable -> 0x00e5 }
        r11 = new org.telegram.tgnet.ConnectionsManager$ResolvedDomain;	 Catch:{ Throwable -> 0x00e5 }
        r12 = android.os.SystemClock.uptimeMillis();	 Catch:{ Throwable -> 0x00e5 }
        r11.<init>(r10, r12);	 Catch:{ Throwable -> 0x00e5 }
        r0.put(r14, r11);	 Catch:{ Throwable -> 0x00e5 }
        if (r3 == 0) goto L_0x00c3;
    L_0x00ba:
        r3.close();	 Catch:{ Throwable -> 0x00be }
        goto L_0x00c3;
    L_0x00be:
        r12 = move-exception;
        org.telegram.messenger.FileLog.e(r12);
        goto L_0x00c4;
        if (r2 == 0) goto L_0x00cc;
        r2.close();	 Catch:{ Exception -> 0x00ca }
        goto L_0x00cc;
    L_0x00ca:
        r12 = move-exception;
        goto L_0x00cd;
        return r10;
    L_0x00ce:
        if (r3 == 0) goto L_0x00d9;
        r3.close();	 Catch:{ Throwable -> 0x00d4 }
        goto L_0x00d9;
    L_0x00d4:
        r4 = move-exception;
        org.telegram.messenger.FileLog.e(r4);
        goto L_0x00da;
        if (r2 == 0) goto L_0x00e2;
        r2.close();	 Catch:{ Exception -> 0x00e0 }
        goto L_0x00e2;
    L_0x00e0:
        r4 = move-exception;
        goto L_0x00fb;
        goto L_0x00fb;
    L_0x00e3:
        r4 = move-exception;
        goto L_0x00fe;
    L_0x00e5:
        r4 = move-exception;
        org.telegram.messenger.FileLog.e(r4);	 Catch:{ all -> 0x00e3 }
        if (r3 == 0) goto L_0x00f4;
        r3.close();	 Catch:{ Throwable -> 0x00ef }
        goto L_0x00f4;
    L_0x00ef:
        r4 = move-exception;
        org.telegram.messenger.FileLog.e(r4);
        goto L_0x00f5;
        if (r2 == 0) goto L_0x00e2;
        r2.close();	 Catch:{ Exception -> 0x00e0 }
        goto L_0x00e2;
        r4 = "";
        return r4;
        if (r3 == 0) goto L_0x010a;
        r3.close();	 Catch:{ Throwable -> 0x0105 }
        goto L_0x010a;
    L_0x0105:
        r5 = move-exception;
        org.telegram.messenger.FileLog.e(r5);
        goto L_0x010b;
        if (r2 == 0) goto L_0x0113;
        r2.close();	 Catch:{ Exception -> 0x0111 }
        goto L_0x0113;
    L_0x0111:
        r5 = move-exception;
        throw r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.tgnet.ConnectionsManager.getHostByName(java.lang.String, int):java.lang.String");
    }

    public static boolean isNetworkOnline() {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.tgnet.ConnectionsManager.isNetworkOnline():boolean
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
        r0 = 1;
        r1 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Exception -> 0x003d }
        r2 = "connectivity";	 Catch:{ Exception -> 0x003d }
        r1 = r1.getSystemService(r2);	 Catch:{ Exception -> 0x003d }
        r1 = (android.net.ConnectivityManager) r1;	 Catch:{ Exception -> 0x003d }
        r2 = r1.getActiveNetworkInfo();	 Catch:{ Exception -> 0x003d }
        if (r2 == 0) goto L_0x001e;	 Catch:{ Exception -> 0x003d }
    L_0x0011:
        r3 = r2.isConnectedOrConnecting();	 Catch:{ Exception -> 0x003d }
        if (r3 != 0) goto L_0x001d;	 Catch:{ Exception -> 0x003d }
    L_0x0017:
        r3 = r2.isAvailable();	 Catch:{ Exception -> 0x003d }
        if (r3 == 0) goto L_0x001e;	 Catch:{ Exception -> 0x003d }
    L_0x001d:
        return r0;	 Catch:{ Exception -> 0x003d }
    L_0x001e:
        r3 = 0;	 Catch:{ Exception -> 0x003d }
        r4 = r1.getNetworkInfo(r3);	 Catch:{ Exception -> 0x003d }
        r2 = r4;	 Catch:{ Exception -> 0x003d }
        if (r2 == 0) goto L_0x002d;	 Catch:{ Exception -> 0x003d }
    L_0x0026:
        r4 = r2.isConnectedOrConnecting();	 Catch:{ Exception -> 0x003d }
        if (r4 == 0) goto L_0x002d;	 Catch:{ Exception -> 0x003d }
    L_0x002c:
        return r0;	 Catch:{ Exception -> 0x003d }
    L_0x002d:
        r4 = r1.getNetworkInfo(r0);	 Catch:{ Exception -> 0x003d }
        r2 = r4;	 Catch:{ Exception -> 0x003d }
        if (r2 == 0) goto L_0x003b;	 Catch:{ Exception -> 0x003d }
    L_0x0034:
        r4 = r2.isConnectedOrConnecting();	 Catch:{ Exception -> 0x003d }
        if (r4 == 0) goto L_0x003b;
        return r0;
        return r3;
    L_0x003d:
        r1 = move-exception;
        org.telegram.messenger.FileLog.e(r1);
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.tgnet.ConnectionsManager.isNetworkOnline():boolean");
    }

    public static native void native_applyDatacenterAddress(int i, int i2, String str, int i3);

    public static native void native_applyDnsConfig(int i, long j);

    public static native void native_bindRequestToGuid(int i, int i2, int i3);

    public static native void native_cancelRequest(int i, int i2, boolean z);

    public static native void native_cancelRequestsForGuid(int i, int i2);

    public static native void native_cleanUp(int i);

    public static native int native_getConnectionState(int i);

    public static native int native_getCurrentTime(int i);

    public static native long native_getCurrentTimeMillis(int i);

    public static native int native_getTimeDifference(int i);

    public static native void native_init(int i, int i2, int i3, int i4, String str, String str2, String str3, String str4, String str5, String str6, String str7, int i5, boolean z, boolean z2, int i6);

    public static native int native_isTestBackend(int i);

    public static native void native_pauseNetwork(int i);

    public static native void native_resumeNetwork(int i, boolean z);

    public static native void native_sendRequest(int i, long j, RequestDelegateInternal requestDelegateInternal, QuickAckDelegate quickAckDelegate, WriteToSocketDelegate writeToSocketDelegate, int i2, int i3, int i4, boolean z, int i5);

    public static native void native_setJava(boolean z);

    public static native void native_setLangCode(int i, String str);

    public static native void native_setNetworkAvailable(int i, boolean z, int i2);

    public static native void native_setProxySettings(int i, String str, int i2, String str2, String str3);

    public static native void native_setPushConnectionEnabled(int i, boolean z);

    public static native void native_setUseIpv6(int i, boolean z);

    public static native void native_setUserId(int i, int i2);

    public static native void native_switchBackend(int i);

    public static native void native_updateDcSettings(int i);

    @android.annotation.SuppressLint({"NewApi"})
    protected static boolean useIpv6Address() {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.tgnet.ConnectionsManager.useIpv6Address():boolean
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
        r0 = android.os.Build.VERSION.SDK_INT;
        r1 = 0;
        r2 = 19;
        if (r0 >= r2) goto L_0x0008;
    L_0x0007:
        return r1;
    L_0x0008:
        r0 = org.telegram.messenger.BuildVars.LOGS_ENABLED;
        if (r0 == 0) goto L_0x00a2;
    L_0x000c:
        r0 = java.net.NetworkInterface.getNetworkInterfaces();	 Catch:{ Throwable -> 0x009e }
    L_0x0010:
        r2 = r0.hasMoreElements();	 Catch:{ Throwable -> 0x009e }
        if (r2 == 0) goto L_0x009d;	 Catch:{ Throwable -> 0x009e }
    L_0x0016:
        r2 = r0.nextElement();	 Catch:{ Throwable -> 0x009e }
        r2 = (java.net.NetworkInterface) r2;	 Catch:{ Throwable -> 0x009e }
        r3 = r2.isUp();	 Catch:{ Throwable -> 0x009e }
        if (r3 == 0) goto L_0x0010;	 Catch:{ Throwable -> 0x009e }
    L_0x0022:
        r3 = r2.isLoopback();	 Catch:{ Throwable -> 0x009e }
        if (r3 != 0) goto L_0x0010;	 Catch:{ Throwable -> 0x009e }
    L_0x0028:
        r3 = r2.getInterfaceAddresses();	 Catch:{ Throwable -> 0x009e }
        r3 = r3.isEmpty();	 Catch:{ Throwable -> 0x009e }
        if (r3 == 0) goto L_0x0033;	 Catch:{ Throwable -> 0x009e }
    L_0x0032:
        goto L_0x0010;	 Catch:{ Throwable -> 0x009e }
    L_0x0033:
        r3 = org.telegram.messenger.BuildVars.LOGS_ENABLED;	 Catch:{ Throwable -> 0x009e }
        if (r3 == 0) goto L_0x004b;	 Catch:{ Throwable -> 0x009e }
    L_0x0037:
        r3 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x009e }
        r3.<init>();	 Catch:{ Throwable -> 0x009e }
        r4 = "valid interface: ";	 Catch:{ Throwable -> 0x009e }
        r3.append(r4);	 Catch:{ Throwable -> 0x009e }
        r3.append(r2);	 Catch:{ Throwable -> 0x009e }
        r3 = r3.toString();	 Catch:{ Throwable -> 0x009e }
        org.telegram.messenger.FileLog.d(r3);	 Catch:{ Throwable -> 0x009e }
    L_0x004b:
        r3 = r2.getInterfaceAddresses();	 Catch:{ Throwable -> 0x009e }
        r4 = r1;	 Catch:{ Throwable -> 0x009e }
    L_0x0050:
        r5 = r3.size();	 Catch:{ Throwable -> 0x009e }
        if (r4 >= r5) goto L_0x009b;	 Catch:{ Throwable -> 0x009e }
    L_0x0056:
        r5 = r3.get(r4);	 Catch:{ Throwable -> 0x009e }
        r5 = (java.net.InterfaceAddress) r5;	 Catch:{ Throwable -> 0x009e }
        r6 = r5.getAddress();	 Catch:{ Throwable -> 0x009e }
        r7 = org.telegram.messenger.BuildVars.LOGS_ENABLED;	 Catch:{ Throwable -> 0x009e }
        if (r7 == 0) goto L_0x007c;	 Catch:{ Throwable -> 0x009e }
    L_0x0064:
        r7 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x009e }
        r7.<init>();	 Catch:{ Throwable -> 0x009e }
        r8 = "address: ";	 Catch:{ Throwable -> 0x009e }
        r7.append(r8);	 Catch:{ Throwable -> 0x009e }
        r8 = r6.getHostAddress();	 Catch:{ Throwable -> 0x009e }
        r7.append(r8);	 Catch:{ Throwable -> 0x009e }
        r7 = r7.toString();	 Catch:{ Throwable -> 0x009e }
        org.telegram.messenger.FileLog.d(r7);	 Catch:{ Throwable -> 0x009e }
    L_0x007c:
        r7 = r6.isLinkLocalAddress();	 Catch:{ Throwable -> 0x009e }
        if (r7 != 0) goto L_0x0098;	 Catch:{ Throwable -> 0x009e }
    L_0x0082:
        r7 = r6.isLoopbackAddress();	 Catch:{ Throwable -> 0x009e }
        if (r7 != 0) goto L_0x0098;	 Catch:{ Throwable -> 0x009e }
    L_0x0088:
        r7 = r6.isMulticastAddress();	 Catch:{ Throwable -> 0x009e }
        if (r7 == 0) goto L_0x008f;	 Catch:{ Throwable -> 0x009e }
    L_0x008e:
        goto L_0x0098;	 Catch:{ Throwable -> 0x009e }
    L_0x008f:
        r7 = org.telegram.messenger.BuildVars.LOGS_ENABLED;	 Catch:{ Throwable -> 0x009e }
        if (r7 == 0) goto L_0x0098;	 Catch:{ Throwable -> 0x009e }
    L_0x0093:
        r7 = "address is good";	 Catch:{ Throwable -> 0x009e }
        org.telegram.messenger.FileLog.d(r7);	 Catch:{ Throwable -> 0x009e }
    L_0x0098:
        r4 = r4 + 1;
        goto L_0x0050;
    L_0x009b:
        goto L_0x0010;
    L_0x009d:
        goto L_0x00a2;
    L_0x009e:
        r0 = move-exception;
        org.telegram.messenger.FileLog.e(r0);
    L_0x00a2:
        r0 = java.net.NetworkInterface.getNetworkInterfaces();	 Catch:{ Throwable -> 0x010f }
        r2 = 0;	 Catch:{ Throwable -> 0x010f }
        r3 = r2;	 Catch:{ Throwable -> 0x010f }
        r2 = r1;	 Catch:{ Throwable -> 0x010f }
    L_0x00a9:
        r4 = r0.hasMoreElements();	 Catch:{ Throwable -> 0x010f }
        if (r4 == 0) goto L_0x0108;	 Catch:{ Throwable -> 0x010f }
    L_0x00af:
        r4 = r0.nextElement();	 Catch:{ Throwable -> 0x010f }
        r4 = (java.net.NetworkInterface) r4;	 Catch:{ Throwable -> 0x010f }
        r5 = r4.isUp();	 Catch:{ Throwable -> 0x010f }
        if (r5 == 0) goto L_0x00a9;	 Catch:{ Throwable -> 0x010f }
    L_0x00bb:
        r5 = r4.isLoopback();	 Catch:{ Throwable -> 0x010f }
        if (r5 == 0) goto L_0x00c2;	 Catch:{ Throwable -> 0x010f }
    L_0x00c1:
        goto L_0x00a9;	 Catch:{ Throwable -> 0x010f }
    L_0x00c2:
        r5 = r4.getInterfaceAddresses();	 Catch:{ Throwable -> 0x010f }
        r6 = r2;	 Catch:{ Throwable -> 0x010f }
        r2 = r1;	 Catch:{ Throwable -> 0x010f }
        r7 = r5.size();	 Catch:{ Throwable -> 0x010f }
        if (r2 >= r7) goto L_0x0105;	 Catch:{ Throwable -> 0x010f }
    L_0x00ce:
        r7 = r5.get(r2);	 Catch:{ Throwable -> 0x010f }
        r7 = (java.net.InterfaceAddress) r7;	 Catch:{ Throwable -> 0x010f }
        r8 = r7.getAddress();	 Catch:{ Throwable -> 0x010f }
        r9 = r8.isLinkLocalAddress();	 Catch:{ Throwable -> 0x010f }
        if (r9 != 0) goto L_0x0102;	 Catch:{ Throwable -> 0x010f }
        r9 = r8.isLoopbackAddress();	 Catch:{ Throwable -> 0x010f }
        if (r9 != 0) goto L_0x0102;	 Catch:{ Throwable -> 0x010f }
        r9 = r8.isMulticastAddress();	 Catch:{ Throwable -> 0x010f }
        if (r9 == 0) goto L_0x00eb;	 Catch:{ Throwable -> 0x010f }
        goto L_0x0102;	 Catch:{ Throwable -> 0x010f }
        r9 = r8 instanceof java.net.Inet6Address;	 Catch:{ Throwable -> 0x010f }
        if (r9 == 0) goto L_0x00f1;	 Catch:{ Throwable -> 0x010f }
        r6 = 1;	 Catch:{ Throwable -> 0x010f }
        goto L_0x0102;	 Catch:{ Throwable -> 0x010f }
        r9 = r8 instanceof java.net.Inet4Address;	 Catch:{ Throwable -> 0x010f }
        if (r9 == 0) goto L_0x0102;	 Catch:{ Throwable -> 0x010f }
        r9 = r8.getHostAddress();	 Catch:{ Throwable -> 0x010f }
        r10 = "192.0.0.";	 Catch:{ Throwable -> 0x010f }
        r10 = r9.startsWith(r10);	 Catch:{ Throwable -> 0x010f }
        if (r10 != 0) goto L_0x0102;
        r3 = 1;
        r2 = r2 + 1;
        goto L_0x00c8;
        r2 = r6;
        goto L_0x00a9;
    L_0x0108:
        if (r3 != 0) goto L_0x010e;
        if (r2 == 0) goto L_0x010e;
        r1 = 1;
        return r1;
        goto L_0x0113;
    L_0x010f:
        r0 = move-exception;
        org.telegram.messenger.FileLog.e(r0);
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.tgnet.ConnectionsManager.useIpv6Address():boolean");
    }

    public static ConnectionsManager getInstance(int num) {
        ConnectionsManager localInstance = Instance[num];
        if (localInstance == null) {
            synchronized (ConnectionsManager.class) {
                localInstance = Instance[num];
                if (localInstance == null) {
                    ConnectionsManager[] connectionsManagerArr = Instance;
                    ConnectionsManager connectionsManager = new ConnectionsManager(num);
                    localInstance = connectionsManager;
                    connectionsManagerArr[num] = connectionsManager;
                }
            }
        }
        return localInstance;
    }

    public ConnectionsManager(int instance) {
        String systemLangCode;
        String langCode;
        String deviceModel;
        String appVersion;
        StringBuilder stringBuilder;
        String langCode2;
        String deviceModel2;
        String appVersion2;
        int i = instance;
        this.currentAccount = i;
        this.connectionState = native_getConnectionState(this.currentAccount);
        File config = ApplicationLoader.getFilesDirFixed();
        if (i != 0) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("account");
            stringBuilder2.append(i);
            config = new File(config, stringBuilder2.toString());
            config.mkdirs();
        }
        File config2 = config;
        String configPath = config2.toString();
        SharedPreferences preferences = MessagesController.getGlobalNotificationsSettings();
        boolean enablePushConnection = preferences.getBoolean("pushConnection", true);
        try {
            systemLangCode = LocaleController.getSystemLocaleStringIso639().toLowerCase();
            langCode = LocaleController.getLocaleStringIso639().toLowerCase();
            deviceModel = new StringBuilder();
            deviceModel.append(Build.MANUFACTURER);
            deviceModel.append(Build.MODEL);
            deviceModel = deviceModel.toString();
            PackageInfo pInfo = ApplicationLoader.applicationContext.getPackageManager().getPackageInfo(ApplicationLoader.applicationContext.getPackageName(), 0);
            appVersion = new StringBuilder();
            appVersion.append(pInfo.versionName);
            appVersion.append(" (");
            appVersion.append(pInfo.versionCode);
            appVersion.append(")");
            appVersion = appVersion.toString();
            stringBuilder = new StringBuilder();
            stringBuilder.append("SDK ");
            stringBuilder.append(VERSION.SDK_INT);
            langCode2 = langCode;
            langCode = systemLangCode;
            systemLangCode = stringBuilder.toString();
        } catch (Exception e) {
            langCode = "en";
            deviceModel = TtmlNode.ANONYMOUS_REGION_ID;
            appVersion = "App version unknown";
            stringBuilder = new StringBuilder();
            stringBuilder.append("SDK ");
            stringBuilder.append(VERSION.SDK_INT);
            systemLangCode = stringBuilder.toString();
            langCode2 = deviceModel;
            deviceModel = "Android unknown";
        }
        if (langCode.trim().length() == 0) {
            langCode = "en";
        }
        String systemLangCode2 = langCode;
        if (deviceModel.trim().length() == 0) {
            deviceModel2 = "Android unknown";
        } else {
            deviceModel2 = deviceModel;
        }
        if (appVersion.trim().length() == 0) {
            appVersion2 = "App version unknown";
        } else {
            appVersion2 = appVersion;
        }
        if (systemLangCode.trim().length() == 0) {
            systemLangCode = "SDK Unknown";
        }
        String systemVersion = systemLangCode;
        UserConfig.getInstance(r14.currentAccount).loadConfig();
        int i2 = BuildVars.BUILD_VERSION;
        int i3 = BuildVars.APP_ID;
        String networkLogPath = FileLog.getNetworkLogPath();
        String str = networkLogPath;
        init(i2, 76, i3, deviceModel2, systemVersion, appVersion2, langCode2, systemLangCode2, configPath, str, UserConfig.getInstance(r14.currentAccount).getClientUserId(), enablePushConnection);
    }

    public long getCurrentTimeMillis() {
        return native_getCurrentTimeMillis(this.currentAccount);
    }

    public int getCurrentTime() {
        return native_getCurrentTime(this.currentAccount);
    }

    public int getTimeDifference() {
        return native_getTimeDifference(this.currentAccount);
    }

    public int sendRequest(TLObject object, RequestDelegate completionBlock) {
        return sendRequest(object, completionBlock, null, 0);
    }

    public int sendRequest(TLObject object, RequestDelegate completionBlock, int flags) {
        return sendRequest(object, completionBlock, null, null, flags, DEFAULT_DATACENTER_ID, 1, true);
    }

    public int sendRequest(TLObject object, RequestDelegate completionBlock, int flags, int connetionType) {
        return sendRequest(object, completionBlock, null, null, flags, DEFAULT_DATACENTER_ID, connetionType, true);
    }

    public int sendRequest(TLObject object, RequestDelegate completionBlock, QuickAckDelegate quickAckBlock, int flags) {
        return sendRequest(object, completionBlock, quickAckBlock, null, flags, DEFAULT_DATACENTER_ID, 1, true);
    }

    public int sendRequest(TLObject object, RequestDelegate onComplete, QuickAckDelegate onQuickAck, WriteToSocketDelegate onWriteToSocket, int flags, int datacenterId, int connetionType, boolean immediate) {
        int requestToken = this.lastRequestToken.getAndIncrement();
        final TLObject tLObject = object;
        final int i = requestToken;
        final RequestDelegate requestDelegate = onComplete;
        final QuickAckDelegate quickAckDelegate = onQuickAck;
        final WriteToSocketDelegate writeToSocketDelegate = onWriteToSocket;
        final int i2 = flags;
        final int i3 = datacenterId;
        final int i4 = connetionType;
        final boolean z = immediate;
        Utilities.stageQueue.postRunnable(new Runnable() {
            public void run() {
                if (BuildVars.LOGS_ENABLED) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("send request ");
                    stringBuilder.append(tLObject);
                    stringBuilder.append(" with token = ");
                    stringBuilder.append(i);
                    FileLog.d(stringBuilder.toString());
                }
                try {
                    NativeByteBuffer buffer = new NativeByteBuffer(tLObject.getObjectSize());
                    tLObject.serializeToStream(buffer);
                    tLObject.freeResources();
                    ConnectionsManager.native_sendRequest(ConnectionsManager.this.currentAccount, buffer.address, new RequestDelegateInternal() {

                        class AnonymousClass1 implements Runnable {
                            final /* synthetic */ TL_error val$finalError;
                            final /* synthetic */ TLObject val$finalResponse;

                            AnonymousClass1(TLObject tLObject, TL_error tL_error) {
                                this.val$finalResponse = tLObject;
                                this.val$finalError = tL_error;
                            }

                            public void run() {
                                requestDelegate.run(this.val$finalResponse, this.val$finalError);
                                if (this.val$finalResponse != null) {
                                    this.val$finalResponse.freeResources();
                                }
                            }
                        }

                        public void run(long r1, int r3, java.lang.String r4, int r5) {
                            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.tgnet.ConnectionsManager.2.1.run(long, int, java.lang.String, int):void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
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
                            r0 = 0;
                            r1 = 0;
                            r2 = 0;
                            r4 = (r7 > r2 ? 1 : (r7 == r2 ? 0 : -1));
                            if (r4 == 0) goto L_0x001f;
                        L_0x0008:
                            r2 = org.telegram.tgnet.NativeByteBuffer.wrap(r7);	 Catch:{ Exception -> 0x001d }
                            r3 = 1;	 Catch:{ Exception -> 0x001d }
                            r2.reused = r3;	 Catch:{ Exception -> 0x001d }
                            r4 = org.telegram.tgnet.ConnectionsManager.AnonymousClass2.this;	 Catch:{ Exception -> 0x001d }
                            r4 = r2;	 Catch:{ Exception -> 0x001d }
                            r5 = r2.readInt32(r3);	 Catch:{ Exception -> 0x001d }
                            r3 = r4.deserializeResponse(r2, r5, r3);	 Catch:{ Exception -> 0x001d }
                            r0 = r3;	 Catch:{ Exception -> 0x001d }
                            goto L_0x0056;	 Catch:{ Exception -> 0x001d }
                        L_0x001d:
                            r0 = move-exception;	 Catch:{ Exception -> 0x001d }
                            goto L_0x0087;	 Catch:{ Exception -> 0x001d }
                        L_0x001f:
                            if (r10 == 0) goto L_0x0056;	 Catch:{ Exception -> 0x001d }
                            r2 = new org.telegram.tgnet.TLRPC$TL_error;	 Catch:{ Exception -> 0x001d }
                            r2.<init>();	 Catch:{ Exception -> 0x001d }
                            r1 = r2;	 Catch:{ Exception -> 0x001d }
                            r1.code = r9;	 Catch:{ Exception -> 0x001d }
                            r1.text = r10;	 Catch:{ Exception -> 0x001d }
                            r2 = org.telegram.messenger.BuildVars.LOGS_ENABLED;	 Catch:{ Exception -> 0x001d }
                            if (r2 == 0) goto L_0x0056;	 Catch:{ Exception -> 0x001d }
                            r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x001d }
                            r2.<init>();	 Catch:{ Exception -> 0x001d }
                            r3 = org.telegram.tgnet.ConnectionsManager.AnonymousClass2.this;	 Catch:{ Exception -> 0x001d }
                            r3 = r2;	 Catch:{ Exception -> 0x001d }
                            r2.append(r3);	 Catch:{ Exception -> 0x001d }
                            r3 = " got error ";	 Catch:{ Exception -> 0x001d }
                            r2.append(r3);	 Catch:{ Exception -> 0x001d }
                            r3 = r1.code;	 Catch:{ Exception -> 0x001d }
                            r2.append(r3);	 Catch:{ Exception -> 0x001d }
                            r3 = " ";	 Catch:{ Exception -> 0x001d }
                            r2.append(r3);	 Catch:{ Exception -> 0x001d }
                            r3 = r1.text;	 Catch:{ Exception -> 0x001d }
                            r2.append(r3);	 Catch:{ Exception -> 0x001d }
                            r2 = r2.toString();	 Catch:{ Exception -> 0x001d }
                            org.telegram.messenger.FileLog.e(r2);	 Catch:{ Exception -> 0x001d }
                        L_0x0056:
                            if (r0 == 0) goto L_0x005a;	 Catch:{ Exception -> 0x001d }
                            r0.networkType = r11;	 Catch:{ Exception -> 0x001d }
                            r2 = org.telegram.messenger.BuildVars.LOGS_ENABLED;	 Catch:{ Exception -> 0x001d }
                            if (r2 == 0) goto L_0x007a;	 Catch:{ Exception -> 0x001d }
                            r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x001d }
                            r2.<init>();	 Catch:{ Exception -> 0x001d }
                            r3 = "java received ";	 Catch:{ Exception -> 0x001d }
                            r2.append(r3);	 Catch:{ Exception -> 0x001d }
                            r2.append(r0);	 Catch:{ Exception -> 0x001d }
                            r3 = " error = ";	 Catch:{ Exception -> 0x001d }
                            r2.append(r3);	 Catch:{ Exception -> 0x001d }
                            r2.append(r1);	 Catch:{ Exception -> 0x001d }
                            r2 = r2.toString();	 Catch:{ Exception -> 0x001d }
                            org.telegram.messenger.FileLog.d(r2);	 Catch:{ Exception -> 0x001d }
                            r2 = r0;	 Catch:{ Exception -> 0x001d }
                            r3 = r1;	 Catch:{ Exception -> 0x001d }
                            r4 = org.telegram.messenger.Utilities.stageQueue;	 Catch:{ Exception -> 0x001d }
                            r5 = new org.telegram.tgnet.ConnectionsManager$2$1$1;	 Catch:{ Exception -> 0x001d }
                            r5.<init>(r2, r3);	 Catch:{ Exception -> 0x001d }
                            r4.postRunnable(r5);	 Catch:{ Exception -> 0x001d }
                            goto L_0x008b;
                            org.telegram.messenger.FileLog.e(r0);
                            return;
                            */
                            throw new UnsupportedOperationException("Method not decompiled: org.telegram.tgnet.ConnectionsManager.2.1.run(long, int, java.lang.String, int):void");
                        }
                    }, quickAckDelegate, writeToSocketDelegate, i2, i3, i4, z, i);
                } catch (Throwable e) {
                    FileLog.e(e);
                }
            }
        });
        return requestToken;
    }

    public void cancelRequest(int token, boolean notifyServer) {
        native_cancelRequest(this.currentAccount, token, notifyServer);
    }

    public void cleanup() {
        native_cleanUp(this.currentAccount);
    }

    public void cancelRequestsForGuid(int guid) {
        native_cancelRequestsForGuid(this.currentAccount, guid);
    }

    public void bindRequestToGuid(int requestToken, int guid) {
        native_bindRequestToGuid(this.currentAccount, requestToken, guid);
    }

    public void applyDatacenterAddress(int datacenterId, String ipAddress, int port) {
        native_applyDatacenterAddress(this.currentAccount, datacenterId, ipAddress, port);
    }

    public int getConnectionState() {
        if (this.connectionState == 3 && this.isUpdating) {
            return 5;
        }
        return this.connectionState;
    }

    public void setUserId(int id) {
        native_setUserId(this.currentAccount, id);
    }

    private void checkConnection() {
        native_setUseIpv6(this.currentAccount, useIpv6Address());
        native_setNetworkAvailable(this.currentAccount, isNetworkOnline(), getCurrentNetworkType());
    }

    public void setPushConnectionEnabled(boolean value) {
        native_setPushConnectionEnabled(this.currentAccount, value);
    }

    public void init(int version, int layer, int apiId, String deviceModel, String systemVersion, String appVersion, String langCode, String systemLangCode, String configPath, String logPath, int userId, boolean enablePushConnection) {
        ConnectionsManager connectionsManager = this;
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0);
        String proxyAddress = preferences.getString("proxy_ip", TtmlNode.ANONYMOUS_REGION_ID);
        String proxyUsername = preferences.getString("proxy_user", TtmlNode.ANONYMOUS_REGION_ID);
        String proxyPassword = preferences.getString("proxy_pass", TtmlNode.ANONYMOUS_REGION_ID);
        int proxyPort = preferences.getInt("proxy_port", 1080);
        if (preferences.getBoolean("proxy_enabled", false) && !TextUtils.isEmpty(proxyAddress)) {
            native_setProxySettings(connectionsManager.currentAccount, proxyAddress, proxyPort, proxyUsername, proxyPassword);
        }
        native_init(connectionsManager.currentAccount, version, layer, apiId, deviceModel, systemVersion, appVersion, langCode, systemLangCode, configPath, logPath, userId, enablePushConnection, isNetworkOnline(), getCurrentNetworkType());
        checkConnection();
        ApplicationLoader.applicationContext.registerReceiver(new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                ConnectionsManager.this.checkConnection();
            }
        }, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }

    public static void setLangCode(String langCode) {
        langCode = langCode.replace('_', '-').toLowerCase();
        for (int a = 0; a < 3; a++) {
            native_setLangCode(a, langCode);
        }
    }

    public void switchBackend() {
        MessagesController.getGlobalMainSettings().edit().remove("language_showed2").commit();
        native_switchBackend(this.currentAccount);
    }

    public void resumeNetworkMaybe() {
        native_resumeNetwork(this.currentAccount, true);
    }

    public void updateDcSettings() {
        native_updateDcSettings(this.currentAccount);
    }

    public long getPauseTime() {
        return this.lastPauseTime;
    }

    public void setAppPaused(boolean value, boolean byScreenState) {
        if (!byScreenState) {
            StringBuilder stringBuilder;
            this.appPaused = value;
            if (BuildVars.LOGS_ENABLED) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("app paused = ");
                stringBuilder.append(value);
                FileLog.d(stringBuilder.toString());
            }
            if (value) {
                this.appResumeCount--;
            } else {
                this.appResumeCount++;
            }
            if (BuildVars.LOGS_ENABLED) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("app resume count ");
                stringBuilder.append(this.appResumeCount);
                FileLog.d(stringBuilder.toString());
            }
            if (this.appResumeCount < 0) {
                this.appResumeCount = 0;
            }
        }
        if (this.appResumeCount == 0) {
            if (this.lastPauseTime == 0) {
                this.lastPauseTime = System.currentTimeMillis();
            }
            native_pauseNetwork(this.currentAccount);
        } else if (!this.appPaused) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("reset app pause time");
            }
            if (this.lastPauseTime != 0 && System.currentTimeMillis() - this.lastPauseTime > DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS) {
                ContactsController.getInstance(this.currentAccount).checkContacts();
            }
            this.lastPauseTime = 0;
            native_resumeNetwork(this.currentAccount, false);
        }
    }

    public static void onUnparsedMessageReceived(long address, final int currentAccount) {
        try {
            NativeByteBuffer buff = NativeByteBuffer.wrap(address);
            buff.reused = true;
            final TLObject message = TLClassStore.Instance().TLdeserialize(buff, buff.readInt32(true), true);
            if (message instanceof Updates) {
                if (BuildVars.LOGS_ENABLED) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("java received ");
                    stringBuilder.append(message);
                    FileLog.d(stringBuilder.toString());
                }
                KeepAliveJob.finishJob();
                Utilities.stageQueue.postRunnable(new Runnable() {
                    public void run() {
                        MessagesController.getInstance(currentAccount).processUpdates((Updates) message, false);
                    }
                });
            }
        } catch (Throwable e) {
            FileLog.e(e);
        }
    }

    public static void onUpdate(final int currentAccount) {
        Utilities.stageQueue.postRunnable(new Runnable() {
            public void run() {
                MessagesController.getInstance(currentAccount).updateTimerProc();
            }
        });
    }

    public static void onSessionCreated(final int currentAccount) {
        Utilities.stageQueue.postRunnable(new Runnable() {
            public void run() {
                MessagesController.getInstance(currentAccount).getDifference();
            }
        });
    }

    public static void onConnectionStateChanged(final int state, final int currentAccount) {
        AndroidUtilities.runOnUIThread(new Runnable() {
            public void run() {
                ConnectionsManager.getInstance(currentAccount).connectionState = state;
                NotificationCenter.getInstance(currentAccount).postNotificationName(NotificationCenter.didUpdatedConnectionState, new Object[0]);
            }
        });
    }

    public static void onLogout(final int currentAccount) {
        AndroidUtilities.runOnUIThread(new Runnable() {
            public void run() {
                if (UserConfig.getInstance(currentAccount).getClientUserId() != 0) {
                    UserConfig.getInstance(currentAccount).clearConfig();
                    MessagesController.getInstance(currentAccount).performLogout(false);
                }
            }
        });
    }

    public static int getCurrentNetworkType() {
        if (isConnectedOrConnectingToWiFi()) {
            return 1;
        }
        if (isRoaming()) {
            return 2;
        }
        return 0;
    }

    public static void onBytesSent(int amount, int networkType, int currentAccount) {
        try {
            StatsController.getInstance(currentAccount).incrementSentBytesCount(networkType, 6, (long) amount);
        } catch (Throwable e) {
            FileLog.e(e);
        }
    }

    public static void onRequestNewServerIpAndPort(final int second, final int currentAccount) {
        Utilities.stageQueue.postRunnable(new Runnable() {
            public void run() {
                if (ConnectionsManager.currentTask == null && (second != 0 || Math.abs(ConnectionsManager.lastDnsRequestTime - System.currentTimeMillis()) >= 10000)) {
                    if (ConnectionsManager.isNetworkOnline()) {
                        ConnectionsManager.lastDnsRequestTime = System.currentTimeMillis();
                        if (second == 2) {
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.d("start dns txt task");
                            }
                            DnsTxtLoadTask task = new DnsTxtLoadTask(currentAccount);
                            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[]{null, null, null});
                            ConnectionsManager.currentTask = task;
                        } else if (second == 1) {
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.d("start azure dns task");
                            }
                            AzureLoadTask task2 = new AzureLoadTask(currentAccount);
                            task2.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[]{null, null, null});
                            ConnectionsManager.currentTask = task2;
                        } else {
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.d("start firebase task");
                            }
                            FirebaseTask task3 = new FirebaseTask(currentAccount);
                            task3.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[]{null, null, null});
                            ConnectionsManager.currentTask = task3;
                        }
                        return;
                    }
                }
                if (BuildVars.LOGS_ENABLED) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("don't start task, current task = ");
                    stringBuilder.append(ConnectionsManager.currentTask);
                    stringBuilder.append(" next task = ");
                    stringBuilder.append(second);
                    stringBuilder.append(" time diff = ");
                    stringBuilder.append(Math.abs(ConnectionsManager.lastDnsRequestTime - System.currentTimeMillis()));
                    stringBuilder.append(" network = ");
                    stringBuilder.append(ConnectionsManager.isNetworkOnline());
                    FileLog.d(stringBuilder.toString());
                }
            }
        });
    }

    public static void onBytesReceived(int amount, int networkType, int currentAccount) {
        try {
            StatsController.getInstance(currentAccount).incrementReceivedBytesCount(networkType, 6, (long) amount);
        } catch (Throwable e) {
            FileLog.e(e);
        }
    }

    public static void onUpdateConfig(long address, final int currentAccount) {
        try {
            NativeByteBuffer buff = NativeByteBuffer.wrap(address);
            buff.reused = true;
            final TL_config message = TL_config.TLdeserialize(buff, buff.readInt32(true), true);
            if (message != null) {
                Utilities.stageQueue.postRunnable(new Runnable() {
                    public void run() {
                        MessagesController.getInstance(currentAccount).updateConfig(message);
                    }
                });
            }
        } catch (Throwable e) {
            FileLog.e(e);
        }
    }

    public static void onInternalPushReceived(int currentAccount) {
        KeepAliveJob.startJob();
    }

    public static int generateClassGuid() {
        int i = lastClassGuid;
        lastClassGuid = i + 1;
        return i;
    }

    public static boolean isRoaming() {
        try {
            NetworkInfo netInfo = ((ConnectivityManager) ApplicationLoader.applicationContext.getSystemService("connectivity")).getActiveNetworkInfo();
            if (netInfo != null) {
                return netInfo.isRoaming();
            }
            return false;
        } catch (Throwable e) {
            FileLog.e(e);
        }
    }

    public static boolean isConnectedOrConnectingToWiFi() {
        try {
            NetworkInfo netInfo = ((ConnectivityManager) ApplicationLoader.applicationContext.getSystemService("connectivity")).getNetworkInfo(1);
            State state = netInfo.getState();
            if (netInfo == null || (state != State.CONNECTED && state != State.CONNECTING && state != State.SUSPENDED)) {
                return false;
            }
            return true;
        } catch (Throwable e) {
            FileLog.e(e);
        }
    }

    public static boolean isConnectedToWiFi() {
        try {
            NetworkInfo netInfo = ((ConnectivityManager) ApplicationLoader.applicationContext.getSystemService("connectivity")).getNetworkInfo(1);
            if (netInfo == null || netInfo.getState() != State.CONNECTED) {
                return false;
            }
            return true;
        } catch (Throwable e) {
            FileLog.e(e);
        }
    }

    public void setIsUpdating(final boolean value) {
        AndroidUtilities.runOnUIThread(new Runnable() {
            public void run() {
                if (ConnectionsManager.this.isUpdating != value) {
                    ConnectionsManager.this.isUpdating = value;
                    if (ConnectionsManager.this.connectionState == 3) {
                        NotificationCenter.getInstance(ConnectionsManager.this.currentAccount).postNotificationName(NotificationCenter.didUpdatedConnectionState, new Object[0]);
                    }
                }
            }
        });
    }
}
