package net.hockeyapp.android.tasks;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.StrictMode;
import android.os.StrictMode.VmPolicy;
import android.os.StrictMode.VmPolicy.Builder;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;
import net.hockeyapp.android.R;
import net.hockeyapp.android.listeners.DownloadFileListener;

@SuppressLint({"StaticFieldLeak"})
public class DownloadFileTask extends AsyncTask<Void, Integer, Long> {
    protected Context mContext;
    protected File mDirectory;
    private String mDownloadErrorMessage = null;
    protected String mFilename;
    protected DownloadFileListener mNotifier;
    protected ProgressDialog mProgressDialog;
    protected String mUrlString;

    protected java.lang.Long doInBackground(java.lang.Void... r1) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: net.hockeyapp.android.tasks.DownloadFileTask.doInBackground(java.lang.Void[]):java.lang.Long
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
        r1 = r20;
        r2 = 0;
        r3 = 0;
        r4 = 0;
        r6 = new java.net.URL;	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r7 = r20.getURLString();	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r6.<init>(r7);	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r7 = 6;	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r7 = r1.createConnection(r6, r7);	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r7.connect();	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r8 = r7.getContentLength();	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r9 = r7.getContentType();	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        if (r9 == 0) goto L_0x0042;	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
    L_0x0021:
        r10 = "text";	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r10 = r9.contains(r10);	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        if (r10 == 0) goto L_0x0042;	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
    L_0x0029:
        r10 = "The requested download does not appear to be a file.";	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r1.mDownloadErrorMessage = r10;	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r10 = java.lang.Long.valueOf(r4);	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        if (r3 == 0) goto L_0x0039;
    L_0x0033:
        r3.close();	 Catch:{ IOException -> 0x0037 }
        goto L_0x0039;	 Catch:{ IOException -> 0x0037 }
    L_0x0037:
        r0 = move-exception;	 Catch:{ IOException -> 0x0037 }
        goto L_0x003f;	 Catch:{ IOException -> 0x0037 }
    L_0x0039:
        if (r2 == 0) goto L_0x0040;	 Catch:{ IOException -> 0x0037 }
    L_0x003b:
        r2.close();	 Catch:{ IOException -> 0x0037 }
        goto L_0x0040;
    L_0x003f:
        goto L_0x0041;
        return r10;
    L_0x0042:
        r10 = r1.mDirectory;	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r10 = r10.mkdirs();	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        if (r10 != 0) goto L_0x006f;	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r11 = r1.mDirectory;	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r11 = r11.exists();	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        if (r11 != 0) goto L_0x006f;	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r11 = new java.io.IOException;	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r12 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r12.<init>();	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r13 = "Could not create the dir(s):";	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r12.append(r13);	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r13 = r1.mDirectory;	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r13 = r13.getAbsolutePath();	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r12.append(r13);	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r12 = r12.toString();	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r11.<init>(r12);	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        throw r11;	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r11 = new java.io.File;	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r12 = r1.mDirectory;	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r13 = r1.mFilename;	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r11.<init>(r12, r13);	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r12 = new java.io.BufferedInputStream;	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r13 = r7.getInputStream();	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r12.<init>(r13);	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r2 = r12;	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r12 = new java.io.FileOutputStream;	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r12.<init>(r11);	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r3 = r12;	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r12 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r12 = new byte[r12];	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r13 = r4;	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r15 = r2.read(r12);	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r16 = r15;	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r4 = -1;	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        if (r15 == r4) goto L_0x00c5;	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r17 = r6;	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r4 = r16;	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r5 = (long) r4;	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r18 = r9;	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r19 = r10;	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r9 = r13 + r5;	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r5 = 1;	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r5 = new java.lang.Integer[r5];	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r6 = (float) r9;	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r13 = 1120403456; // 0x42c80000 float:100.0 double:5.53552857E-315;	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r6 = r6 * r13;	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r13 = (float) r8;	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r6 = r6 / r13;	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r6 = java.lang.Math.round(r6);	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r6 = java.lang.Integer.valueOf(r6);	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r13 = 0;	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r5[r13] = r6;	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r1.publishProgress(r5);	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r3.write(r12, r13, r4);	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r13 = r9;	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r6 = r17;	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r9 = r18;	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r10 = r19;	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r4 = 0;	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        goto L_0x008d;	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r17 = r6;	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r18 = r9;	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r19 = r10;	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r4 = r16;	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r3.flush();	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        r5 = java.lang.Long.valueOf(r13);	 Catch:{ IOException -> 0x00e9, all -> 0x00e5 }
        if (r3 == 0) goto L_0x00dc;
        r3.close();	 Catch:{ IOException -> 0x00da }
        goto L_0x00dc;	 Catch:{ IOException -> 0x00da }
    L_0x00da:
        r0 = move-exception;	 Catch:{ IOException -> 0x00da }
        goto L_0x00e2;	 Catch:{ IOException -> 0x00da }
        if (r2 == 0) goto L_0x00e3;	 Catch:{ IOException -> 0x00da }
        r2.close();	 Catch:{ IOException -> 0x00da }
        goto L_0x00e3;
        goto L_0x00e4;
        return r5;
    L_0x00e5:
        r0 = move-exception;
        r4 = r2;
        r2 = r0;
        goto L_0x011b;
    L_0x00e9:
        r0 = move-exception;
        r4 = r2;
        r2 = r0;
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0119 }
        r5.<init>();	 Catch:{ all -> 0x0119 }
        r6 = "Failed to download ";	 Catch:{ all -> 0x0119 }
        r5.append(r6);	 Catch:{ all -> 0x0119 }
        r6 = r1.mUrlString;	 Catch:{ all -> 0x0119 }
        r5.append(r6);	 Catch:{ all -> 0x0119 }
        r5 = r5.toString();	 Catch:{ all -> 0x0119 }
        net.hockeyapp.android.utils.HockeyLog.error(r5, r2);	 Catch:{ all -> 0x0119 }
        r5 = 0;	 Catch:{ all -> 0x0119 }
        r5 = java.lang.Long.valueOf(r5);	 Catch:{ all -> 0x0119 }
        if (r3 == 0) goto L_0x0110;
        r3.close();	 Catch:{ IOException -> 0x010e }
        goto L_0x0110;	 Catch:{ IOException -> 0x010e }
    L_0x010e:
        r0 = move-exception;	 Catch:{ IOException -> 0x010e }
        goto L_0x0116;	 Catch:{ IOException -> 0x010e }
        if (r4 == 0) goto L_0x0117;	 Catch:{ IOException -> 0x010e }
        r4.close();	 Catch:{ IOException -> 0x010e }
        goto L_0x0117;
        goto L_0x0118;
        return r5;
    L_0x0119:
        r0 = move-exception;
        goto L_0x00e7;
        if (r3 == 0) goto L_0x0123;
        r3.close();	 Catch:{ IOException -> 0x0121 }
        goto L_0x0123;	 Catch:{ IOException -> 0x0121 }
    L_0x0121:
        r0 = move-exception;	 Catch:{ IOException -> 0x0121 }
        goto L_0x0129;	 Catch:{ IOException -> 0x0121 }
        if (r4 == 0) goto L_0x012a;	 Catch:{ IOException -> 0x0121 }
        r4.close();	 Catch:{ IOException -> 0x0121 }
        goto L_0x012a;
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: net.hockeyapp.android.tasks.DownloadFileTask.doInBackground(java.lang.Void[]):java.lang.Long");
    }

    public DownloadFileTask(Context context, String urlString, DownloadFileListener notifier) {
        this.mContext = context;
        this.mUrlString = urlString;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(UUID.randomUUID());
        stringBuilder.append(".apk");
        this.mFilename = stringBuilder.toString();
        this.mDirectory = new File(context.getExternalFilesDir(null), "Download");
        this.mNotifier = notifier;
    }

    protected void setConnectionProperties(HttpURLConnection connection) {
        connection.addRequestProperty("User-Agent", "HockeySDK/Android 5.0.4");
        connection.setInstanceFollowRedirects(true);
    }

    protected URLConnection createConnection(URL url, int remainingRedirects) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        setConnectionProperties(connection);
        int code = connection.getResponseCode();
        if ((code != 301 && code != 302 && code != 303) || remainingRedirects == 0) {
            return connection;
        }
        URL movedUrl = new URL(connection.getHeaderField("Location"));
        if (!url.getProtocol().equals(movedUrl.getProtocol())) {
            connection.disconnect();
            return createConnection(movedUrl, remainingRedirects - 1);
        }
        return connection;
    }

    protected void onProgressUpdate(Integer... args) {
        try {
            if (this.mProgressDialog == null) {
                this.mProgressDialog = new ProgressDialog(this.mContext);
                this.mProgressDialog.setProgressStyle(1);
                this.mProgressDialog.setMessage(this.mContext.getString(R.string.hockeyapp_update_loading));
                this.mProgressDialog.setCancelable(false);
                this.mProgressDialog.show();
            }
            this.mProgressDialog.setProgress(args[0].intValue());
        } catch (Exception e) {
        }
    }

    protected void onPostExecute(Long result) {
        if (this.mProgressDialog != null) {
            try {
                this.mProgressDialog.dismiss();
            } catch (Exception e) {
            }
        }
        if (result.longValue() > 0) {
            this.mNotifier.downloadSuccessful(this);
            Intent intent = new Intent("android.intent.action.INSTALL_PACKAGE");
            intent.setDataAndType(Uri.fromFile(new File(this.mDirectory, this.mFilename)), "application/vnd.android.package-archive");
            intent.setFlags(268435456);
            VmPolicy oldVmPolicy = null;
            if (VERSION.SDK_INT >= 24) {
                oldVmPolicy = StrictMode.getVmPolicy();
                StrictMode.setVmPolicy(new Builder().penaltyLog().build());
            }
            this.mContext.startActivity(intent);
            if (oldVmPolicy != null) {
                StrictMode.setVmPolicy(oldVmPolicy);
            }
            return;
        }
        try {
            String message;
            AlertDialog.Builder builder = new AlertDialog.Builder(this.mContext);
            builder.setTitle(R.string.hockeyapp_download_failed_dialog_title);
            if (this.mDownloadErrorMessage == null) {
                message = this.mContext.getString(R.string.hockeyapp_download_failed_dialog_message);
            } else {
                message = this.mDownloadErrorMessage;
            }
            builder.setMessage(message);
            builder.setNegativeButton(R.string.hockeyapp_download_failed_dialog_negative_button, new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    DownloadFileTask.this.mNotifier.downloadFailed(DownloadFileTask.this, Boolean.valueOf(false));
                }
            });
            builder.setPositiveButton(R.string.hockeyapp_download_failed_dialog_positive_button, new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    DownloadFileTask.this.mNotifier.downloadFailed(DownloadFileTask.this, Boolean.valueOf(true));
                }
            });
            builder.create().show();
        } catch (Exception e2) {
        }
    }

    protected String getURLString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.mUrlString);
        stringBuilder.append("&type=apk");
        return stringBuilder.toString();
    }
}
