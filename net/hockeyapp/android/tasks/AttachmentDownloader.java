package net.hockeyapp.android.tasks;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.Queue;
import net.hockeyapp.android.Constants;
import net.hockeyapp.android.objects.FeedbackAttachment;
import net.hockeyapp.android.utils.AsyncTaskUtils;
import net.hockeyapp.android.utils.HockeyLog;
import net.hockeyapp.android.utils.ImageUtils;
import net.hockeyapp.android.views.AttachmentView;

public class AttachmentDownloader {
    private final Handler downloadHandler;
    private boolean downloadRunning;
    private Queue<DownloadJob> queue;

    private static class AttachmentDownloaderHolder {
        static final AttachmentDownloader INSTANCE = new AttachmentDownloader();
    }

    private static class DownloadHandler extends Handler {
        private final AttachmentDownloader downloader;

        DownloadHandler(AttachmentDownloader downloader) {
            this.downloader = downloader;
        }

        public void handleMessage(Message msg) {
            final DownloadJob retryCandidate = (DownloadJob) this.downloader.queue.poll();
            if (!retryCandidate.isSuccess() && retryCandidate.consumeRetry()) {
                postDelayed(new Runnable() {
                    public void run() {
                        DownloadHandler.this.downloader.queue.add(retryCandidate);
                        DownloadHandler.this.downloader.downloadNext();
                    }
                }, 3000);
            }
            this.downloader.downloadRunning = false;
            this.downloader.downloadNext();
        }
    }

    private static class DownloadJob {
        private final AttachmentView attachmentView;
        private final FeedbackAttachment feedbackAttachment;
        private int remainingRetries;
        private boolean success;

        private DownloadJob(FeedbackAttachment feedbackAttachment, AttachmentView attachmentView) {
            this.feedbackAttachment = feedbackAttachment;
            this.attachmentView = attachmentView;
            this.success = false;
            this.remainingRetries = 2;
        }

        FeedbackAttachment getFeedbackAttachment() {
            return this.feedbackAttachment;
        }

        AttachmentView getAttachmentView() {
            return this.attachmentView;
        }

        boolean isSuccess() {
            return this.success;
        }

        void setSuccess(boolean success) {
            this.success = success;
        }

        boolean hasRetry() {
            return this.remainingRetries > 0;
        }

        boolean consumeRetry() {
            int i = this.remainingRetries - 1;
            this.remainingRetries = i;
            return i >= 0;
        }
    }

    @SuppressLint({"StaticFieldLeak"})
    private static class DownloadTask extends AsyncTask<Void, Integer, Boolean> {
        private Bitmap bitmap = null;
        private int bitmapOrientation = 1;
        private final Context context;
        private final DownloadJob downloadJob;
        private final Handler handler;

        private boolean downloadAttachment(java.lang.String r1, java.io.File r2) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: net.hockeyapp.android.tasks.AttachmentDownloader.DownloadTask.downloadAttachment(java.lang.String, java.io.File):boolean
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
            r1 = r19;
            r2 = r21;
            r3 = 0;
            r4 = 0;
            r5 = 0;
            r6 = 0;
            r7 = new java.net.URL;	 Catch:{ IOException -> 0x00b5, all -> 0x00b0 }
            r8 = r20;	 Catch:{ IOException -> 0x00b5, all -> 0x00b0 }
            r7.<init>(r8);	 Catch:{ IOException -> 0x00b5, all -> 0x00b0 }
            r7 = r1.createConnection(r7);	 Catch:{ IOException -> 0x00b5, all -> 0x00b0 }
            r7 = (java.net.HttpURLConnection) r7;	 Catch:{ IOException -> 0x00b5, all -> 0x00b0 }
            r5 = r7;	 Catch:{ IOException -> 0x00b5, all -> 0x00b0 }
            r5.connect();	 Catch:{ IOException -> 0x00b5, all -> 0x00b0 }
            r7 = r5.getContentLength();	 Catch:{ IOException -> 0x00b5, all -> 0x00b0 }
            r9 = "Status";	 Catch:{ IOException -> 0x00b5, all -> 0x00b0 }
            r9 = r5.getHeaderField(r9);	 Catch:{ IOException -> 0x00b5, all -> 0x00b0 }
            if (r9 == 0) goto L_0x0044;	 Catch:{ IOException -> 0x00b5, all -> 0x00b0 }
        L_0x0025:
            r10 = "200";	 Catch:{ IOException -> 0x00b5, all -> 0x00b0 }
            r10 = r9.startsWith(r10);	 Catch:{ IOException -> 0x00b5, all -> 0x00b0 }
            if (r10 != 0) goto L_0x0044;
        L_0x002e:
            if (r4 == 0) goto L_0x0036;
        L_0x0030:
            r4.close();	 Catch:{ IOException -> 0x0034 }
            goto L_0x0036;	 Catch:{ IOException -> 0x0034 }
        L_0x0034:
            r0 = move-exception;	 Catch:{ IOException -> 0x0034 }
            goto L_0x003c;	 Catch:{ IOException -> 0x0034 }
        L_0x0036:
            if (r3 == 0) goto L_0x003d;	 Catch:{ IOException -> 0x0034 }
        L_0x0038:
            r3.close();	 Catch:{ IOException -> 0x0034 }
            goto L_0x003d;
        L_0x003c:
            goto L_0x003e;
            if (r5 == 0) goto L_0x0043;
            r5.disconnect();
            return r6;
        L_0x0044:
            r10 = new java.io.BufferedInputStream;	 Catch:{ IOException -> 0x00b5, all -> 0x00b0 }
            r11 = r5.getInputStream();	 Catch:{ IOException -> 0x00b5, all -> 0x00b0 }
            r10.<init>(r11);	 Catch:{ IOException -> 0x00b5, all -> 0x00b0 }
            r3 = r10;	 Catch:{ IOException -> 0x00b5, all -> 0x00b0 }
            r10 = new java.io.FileOutputStream;	 Catch:{ IOException -> 0x00b5, all -> 0x00b0 }
            r10.<init>(r2);	 Catch:{ IOException -> 0x00b5, all -> 0x00b0 }
            r4 = r10;	 Catch:{ IOException -> 0x00b5, all -> 0x00b0 }
            r10 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;	 Catch:{ IOException -> 0x00b5, all -> 0x00b0 }
            r10 = new byte[r10];	 Catch:{ IOException -> 0x00b5, all -> 0x00b0 }
            r13 = 0;	 Catch:{ IOException -> 0x00b5, all -> 0x00b0 }
            r15 = r3.read(r10);	 Catch:{ IOException -> 0x00b5, all -> 0x00b0 }
            r16 = r15;	 Catch:{ IOException -> 0x00b5, all -> 0x00b0 }
            r11 = -1;	 Catch:{ IOException -> 0x00b5, all -> 0x00b0 }
            r12 = 1;	 Catch:{ IOException -> 0x00b5, all -> 0x00b0 }
            if (r15 == r11) goto L_0x008b;	 Catch:{ IOException -> 0x00b5, all -> 0x00b0 }
            r17 = r7;	 Catch:{ IOException -> 0x00b5, all -> 0x00b0 }
            r11 = r16;	 Catch:{ IOException -> 0x00b5, all -> 0x00b0 }
            r6 = (long) r11;	 Catch:{ IOException -> 0x00b5, all -> 0x00b0 }
            r15 = r13 + r6;	 Catch:{ IOException -> 0x00b5, all -> 0x00b0 }
            r6 = new java.lang.Integer[r12];	 Catch:{ IOException -> 0x00b5, all -> 0x00b0 }
            r12 = 100;	 Catch:{ IOException -> 0x00b5, all -> 0x00b0 }
            r12 = r12 * r15;	 Catch:{ IOException -> 0x00b5, all -> 0x00b0 }
            r18 = r9;	 Catch:{ IOException -> 0x00b5, all -> 0x00b0 }
            r7 = r17;	 Catch:{ IOException -> 0x00b5, all -> 0x00b0 }
            r8 = (long) r7;	 Catch:{ IOException -> 0x00b5, all -> 0x00b0 }
            r12 = r12 / r8;	 Catch:{ IOException -> 0x00b5, all -> 0x00b0 }
            r8 = (int) r12;	 Catch:{ IOException -> 0x00b5, all -> 0x00b0 }
            r8 = java.lang.Integer.valueOf(r8);	 Catch:{ IOException -> 0x00b5, all -> 0x00b0 }
            r9 = 0;	 Catch:{ IOException -> 0x00b5, all -> 0x00b0 }
            r6[r9] = r8;	 Catch:{ IOException -> 0x00b5, all -> 0x00b0 }
            r1.publishProgress(r6);	 Catch:{ IOException -> 0x00b5, all -> 0x00b0 }
            r4.write(r10, r9, r11);	 Catch:{ IOException -> 0x00b5, all -> 0x00b0 }
            r13 = r15;	 Catch:{ IOException -> 0x00b5, all -> 0x00b0 }
            r9 = r18;	 Catch:{ IOException -> 0x00b5, all -> 0x00b0 }
            r6 = 0;	 Catch:{ IOException -> 0x00b5, all -> 0x00b0 }
            r8 = r20;	 Catch:{ IOException -> 0x00b5, all -> 0x00b0 }
            goto L_0x005a;	 Catch:{ IOException -> 0x00b5, all -> 0x00b0 }
            r18 = r9;	 Catch:{ IOException -> 0x00b5, all -> 0x00b0 }
            r11 = r16;	 Catch:{ IOException -> 0x00b5, all -> 0x00b0 }
            r4.flush();	 Catch:{ IOException -> 0x00b5, all -> 0x00b0 }
            r8 = 0;
            r6 = (r13 > r8 ? 1 : (r13 == r8 ? 0 : -1));
            if (r6 <= 0) goto L_0x0099;
            goto L_0x009a;
            r12 = 0;
            if (r4 == 0) goto L_0x00a2;
            r4.close();	 Catch:{ IOException -> 0x00a0 }
            goto L_0x00a2;	 Catch:{ IOException -> 0x00a0 }
        L_0x00a0:
            r0 = move-exception;	 Catch:{ IOException -> 0x00a0 }
            goto L_0x00a8;	 Catch:{ IOException -> 0x00a0 }
            if (r3 == 0) goto L_0x00a9;	 Catch:{ IOException -> 0x00a0 }
            r3.close();	 Catch:{ IOException -> 0x00a0 }
            goto L_0x00a9;
            goto L_0x00aa;
            if (r5 == 0) goto L_0x00af;
            r5.disconnect();
            return r12;
        L_0x00b0:
            r0 = move-exception;
            r6 = r5;
            r5 = r3;
            r3 = r0;
            goto L_0x00e7;
        L_0x00b5:
            r0 = move-exception;
            r6 = r5;
            r5 = r3;
            r3 = r0;
            r7 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00e5 }
            r7.<init>();	 Catch:{ all -> 0x00e5 }
            r8 = "Failed to download attachment to ";	 Catch:{ all -> 0x00e5 }
            r7.append(r8);	 Catch:{ all -> 0x00e5 }
            r7.append(r2);	 Catch:{ all -> 0x00e5 }
            r7 = r7.toString();	 Catch:{ all -> 0x00e5 }
            net.hockeyapp.android.utils.HockeyLog.error(r7, r3);	 Catch:{ all -> 0x00e5 }
            if (r4 == 0) goto L_0x00d6;
            r4.close();	 Catch:{ IOException -> 0x00d4 }
            goto L_0x00d6;	 Catch:{ IOException -> 0x00d4 }
        L_0x00d4:
            r0 = move-exception;	 Catch:{ IOException -> 0x00d4 }
            goto L_0x00dc;	 Catch:{ IOException -> 0x00d4 }
            if (r5 == 0) goto L_0x00dd;	 Catch:{ IOException -> 0x00d4 }
            r5.close();	 Catch:{ IOException -> 0x00d4 }
            goto L_0x00dd;
            goto L_0x00de;
            if (r6 == 0) goto L_0x00e3;
            r6.disconnect();
            r7 = 0;
            return r7;
        L_0x00e5:
            r0 = move-exception;
            goto L_0x00b3;
            if (r4 == 0) goto L_0x00ef;
            r4.close();	 Catch:{ IOException -> 0x00ed }
            goto L_0x00ef;	 Catch:{ IOException -> 0x00ed }
        L_0x00ed:
            r0 = move-exception;	 Catch:{ IOException -> 0x00ed }
            goto L_0x00f5;	 Catch:{ IOException -> 0x00ed }
            if (r5 == 0) goto L_0x00f6;	 Catch:{ IOException -> 0x00ed }
            r5.close();	 Catch:{ IOException -> 0x00ed }
            goto L_0x00f6;
            goto L_0x00f7;
            if (r6 == 0) goto L_0x00fc;
            r6.disconnect();
            throw r3;
            */
            throw new UnsupportedOperationException("Method not decompiled: net.hockeyapp.android.tasks.AttachmentDownloader.DownloadTask.downloadAttachment(java.lang.String, java.io.File):boolean");
        }

        DownloadTask(DownloadJob downloadJob, Handler handler) {
            this.downloadJob = downloadJob;
            this.handler = handler;
            this.context = downloadJob.getAttachmentView().getContext();
        }

        protected void onPreExecute() {
        }

        protected Boolean doInBackground(Void... args) {
            FeedbackAttachment attachment = this.downloadJob.getFeedbackAttachment();
            File file = new File(Constants.getHockeyAppStorageDir(this.context), attachment.getCacheId());
            if (file.exists()) {
                HockeyLog.error("Cached...");
                loadImageThumbnail(file);
                return Boolean.valueOf(true);
            }
            HockeyLog.error("Downloading...");
            boolean success = downloadAttachment(attachment.getUrl(), file);
            if (success) {
                loadImageThumbnail(file);
            }
            return Boolean.valueOf(success);
        }

        protected void onProgressUpdate(Integer... values) {
        }

        protected void onPostExecute(Boolean success) {
            AttachmentView attachmentView = this.downloadJob.getAttachmentView();
            this.downloadJob.setSuccess(success.booleanValue());
            if (success.booleanValue()) {
                attachmentView.setImage(this.bitmap, this.bitmapOrientation);
            } else if (!this.downloadJob.hasRetry()) {
                attachmentView.signalImageLoadingError();
            }
            this.handler.sendEmptyMessage(0);
        }

        private void loadImageThumbnail(File file) {
            try {
                AttachmentView attachmentView = this.downloadJob.getAttachmentView();
                this.bitmapOrientation = ImageUtils.determineOrientation(file);
                this.bitmap = ImageUtils.decodeSampledBitmap(file, this.bitmapOrientation == 0 ? attachmentView.getWidthLandscape() : attachmentView.getWidthPortrait(), this.bitmapOrientation == 0 ? attachmentView.getMaxHeightLandscape() : attachmentView.getMaxHeightPortrait());
            } catch (Throwable e) {
                HockeyLog.error("Failed to load image thumbnail", e);
                this.bitmap = null;
            }
        }

        private URLConnection createConnection(URL url) throws IOException {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("User-Agent", "HockeySDK/Android 5.0.4");
            connection.setInstanceFollowRedirects(true);
            return connection;
        }
    }

    public static AttachmentDownloader getInstance() {
        return AttachmentDownloaderHolder.INSTANCE;
    }

    private AttachmentDownloader() {
        this.downloadHandler = new DownloadHandler(this);
        this.queue = new LinkedList();
        this.downloadRunning = false;
    }

    public void download(FeedbackAttachment feedbackAttachment, AttachmentView attachmentView) {
        this.queue.add(new DownloadJob(feedbackAttachment, attachmentView));
        downloadNext();
    }

    private void downloadNext() {
        if (!this.downloadRunning) {
            DownloadJob downloadJob = (DownloadJob) this.queue.peek();
            if (downloadJob != null) {
                this.downloadRunning = true;
                AsyncTaskUtils.execute(new DownloadTask(downloadJob, this.downloadHandler));
            }
        }
    }
}
