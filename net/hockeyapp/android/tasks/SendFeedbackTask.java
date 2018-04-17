package net.hockeyapp.android.tasks;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.List;
import net.hockeyapp.android.Constants;
import net.hockeyapp.android.R;
import net.hockeyapp.android.utils.HockeyLog;

@SuppressLint({"StaticFieldLeak"})
public class SendFeedbackTask extends ConnectionTask<Void, Void, HashMap<String, String>> {
    private List<Uri> mAttachmentUris;
    private Context mContext;
    private String mEmail;
    private Handler mHandler;
    private boolean mIsFetchMessages;
    private int mLastMessageId = -1;
    private String mName;
    private ProgressDialog mProgressDialog;
    private boolean mShowProgressDialog = true;
    private String mSubject;
    private String mText;
    private String mToken;
    private String mUrlString;

    public SendFeedbackTask(Context context, String urlString, String name, String email, String subject, String text, List<Uri> attachmentUris, String token, Handler handler, boolean isFetchMessages) {
        this.mContext = context;
        this.mUrlString = urlString;
        this.mName = name;
        this.mEmail = email;
        this.mSubject = subject;
        this.mText = text;
        this.mAttachmentUris = attachmentUris;
        this.mToken = token;
        this.mHandler = handler;
        this.mIsFetchMessages = isFetchMessages;
        if (context != null) {
            Constants.loadFromContext(context);
        }
    }

    public void setHandler(Handler handler) {
        this.mHandler = handler;
    }

    public void attach(Context context) {
        this.mContext = context;
        if (getStatus() != Status.RUNNING) {
            return;
        }
        if ((this.mProgressDialog == null || !this.mProgressDialog.isShowing()) && this.mShowProgressDialog) {
            this.mProgressDialog = ProgressDialog.show(this.mContext, TtmlNode.ANONYMOUS_REGION_ID, getLoadingMessage(), true, false);
        }
    }

    public void detach() {
        this.mContext = null;
        if (this.mProgressDialog != null) {
            this.mProgressDialog.dismiss();
            this.mProgressDialog = null;
        }
    }

    protected void onPreExecute() {
        if ((this.mProgressDialog == null || !this.mProgressDialog.isShowing()) && this.mShowProgressDialog) {
            this.mProgressDialog = ProgressDialog.show(this.mContext, TtmlNode.ANONYMOUS_REGION_ID, getLoadingMessage(), true, false);
        }
    }

    protected HashMap<String, String> doInBackground(Void... args) {
        if (this.mIsFetchMessages && this.mToken != null) {
            return doGet();
        }
        if (this.mIsFetchMessages) {
            return null;
        }
        if (this.mAttachmentUris.isEmpty()) {
            return doPostPut();
        }
        HashMap<String, String> result = doPostPutWithAttachments();
        if (result != null) {
            clearTemporaryFolder(result);
        }
        return result;
    }

    private void clearTemporaryFolder(HashMap<String, String> result) {
        String status = (String) result.get("status");
        if (status != null && status.startsWith("2") && this.mContext != null) {
            File file;
            File folder = new File(this.mContext.getCacheDir(), "HockeyApp");
            int i = 0;
            if (folder.exists()) {
                for (File file2 : folder.listFiles()) {
                    if (!(file2 == null || Boolean.valueOf(file2.delete()).booleanValue())) {
                        HockeyLog.debug("SendFeedbackTask", "Error deleting file from temporary folder");
                    }
                }
            }
            File[] screenshots = Constants.getHockeyAppStorageDir(this.mContext).listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.endsWith(".jpg");
                }
            });
            int length = screenshots.length;
            while (i < length) {
                file2 = screenshots[i];
                if (this.mAttachmentUris.contains(Uri.fromFile(file2))) {
                    if (file2.delete()) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Screenshot '");
                        stringBuilder.append(file2.getName());
                        stringBuilder.append("' has been deleted");
                        HockeyLog.debug("SendFeedbackTask", stringBuilder.toString());
                    } else {
                        HockeyLog.error("SendFeedbackTask", "Error deleting screenshot");
                    }
                }
                i++;
            }
        }
    }

    protected void onPostExecute(HashMap<String, String> result) {
        if (this.mProgressDialog != null) {
            try {
                this.mProgressDialog.dismiss();
            } catch (Exception e) {
            }
        }
        if (this.mHandler != null) {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            if (result != null) {
                bundle.putString("request_type", (String) result.get("type"));
                bundle.putString("feedback_response", (String) result.get("response"));
                bundle.putString("feedback_status", (String) result.get("status"));
            } else {
                bundle.putString("request_type", "unknown");
            }
            msg.setData(bundle);
            this.mHandler.sendMessage(msg);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.util.HashMap<java.lang.String, java.lang.String> doPostPut() {
        /*
        r5 = this;
        r0 = new java.util.HashMap;
        r0.<init>();
        r1 = "type";
        r2 = "send";
        r0.put(r1, r2);
        r1 = 0;
        r2 = new java.util.HashMap;	 Catch:{ IOException -> 0x00bb }
        r2.<init>();	 Catch:{ IOException -> 0x00bb }
        r3 = "name";
        r4 = r5.mName;	 Catch:{ IOException -> 0x00bb }
        r2.put(r3, r4);	 Catch:{ IOException -> 0x00bb }
        r3 = "email";
        r4 = r5.mEmail;	 Catch:{ IOException -> 0x00bb }
        r2.put(r3, r4);	 Catch:{ IOException -> 0x00bb }
        r3 = "subject";
        r4 = r5.mSubject;	 Catch:{ IOException -> 0x00bb }
        r2.put(r3, r4);	 Catch:{ IOException -> 0x00bb }
        r3 = "text";
        r4 = r5.mText;	 Catch:{ IOException -> 0x00bb }
        r2.put(r3, r4);	 Catch:{ IOException -> 0x00bb }
        r3 = "bundle_identifier";
        r4 = net.hockeyapp.android.Constants.APP_PACKAGE;	 Catch:{ IOException -> 0x00bb }
        r2.put(r3, r4);	 Catch:{ IOException -> 0x00bb }
        r3 = "bundle_short_version";
        r4 = net.hockeyapp.android.Constants.APP_VERSION_NAME;	 Catch:{ IOException -> 0x00bb }
        r2.put(r3, r4);	 Catch:{ IOException -> 0x00bb }
        r3 = "bundle_version";
        r4 = net.hockeyapp.android.Constants.APP_VERSION;	 Catch:{ IOException -> 0x00bb }
        r2.put(r3, r4);	 Catch:{ IOException -> 0x00bb }
        r3 = "os_version";
        r4 = net.hockeyapp.android.Constants.ANDROID_VERSION;	 Catch:{ IOException -> 0x00bb }
        r2.put(r3, r4);	 Catch:{ IOException -> 0x00bb }
        r3 = "oem";
        r4 = net.hockeyapp.android.Constants.PHONE_MANUFACTURER;	 Catch:{ IOException -> 0x00bb }
        r2.put(r3, r4);	 Catch:{ IOException -> 0x00bb }
        r3 = "model";
        r4 = net.hockeyapp.android.Constants.PHONE_MODEL;	 Catch:{ IOException -> 0x00bb }
        r2.put(r3, r4);	 Catch:{ IOException -> 0x00bb }
        r3 = "sdk_version";
        r4 = "5.0.4";
        r2.put(r3, r4);	 Catch:{ IOException -> 0x00bb }
        r3 = r5.mToken;	 Catch:{ IOException -> 0x00bb }
        if (r3 == 0) goto L_0x007d;
    L_0x0063:
        r3 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x00bb }
        r3.<init>();	 Catch:{ IOException -> 0x00bb }
        r4 = r5.mUrlString;	 Catch:{ IOException -> 0x00bb }
        r3.append(r4);	 Catch:{ IOException -> 0x00bb }
        r4 = r5.mToken;	 Catch:{ IOException -> 0x00bb }
        r3.append(r4);	 Catch:{ IOException -> 0x00bb }
        r4 = "/";
        r3.append(r4);	 Catch:{ IOException -> 0x00bb }
        r3 = r3.toString();	 Catch:{ IOException -> 0x00bb }
        r5.mUrlString = r3;	 Catch:{ IOException -> 0x00bb }
    L_0x007d:
        r3 = new net.hockeyapp.android.utils.HttpURLConnectionBuilder;	 Catch:{ IOException -> 0x00bb }
        r4 = r5.mUrlString;	 Catch:{ IOException -> 0x00bb }
        r3.<init>(r4);	 Catch:{ IOException -> 0x00bb }
        r4 = r5.mToken;	 Catch:{ IOException -> 0x00bb }
        if (r4 == 0) goto L_0x008b;
    L_0x0088:
        r4 = "PUT";
        goto L_0x008d;
    L_0x008b:
        r4 = "POST";
    L_0x008d:
        r3 = r3.setRequestMethod(r4);	 Catch:{ IOException -> 0x00bb }
        r3 = r3.writeFormFields(r2);	 Catch:{ IOException -> 0x00bb }
        r3 = r3.build();	 Catch:{ IOException -> 0x00bb }
        r1 = r3;
        r1.connect();	 Catch:{ IOException -> 0x00bb }
        r3 = "status";
        r4 = r1.getResponseCode();	 Catch:{ IOException -> 0x00bb }
        r4 = java.lang.String.valueOf(r4);	 Catch:{ IOException -> 0x00bb }
        r0.put(r3, r4);	 Catch:{ IOException -> 0x00bb }
        r3 = "response";
        r4 = net.hockeyapp.android.tasks.ConnectionTask.getStringFromConnection(r1);	 Catch:{ IOException -> 0x00bb }
        r0.put(r3, r4);	 Catch:{ IOException -> 0x00bb }
        if (r1 == 0) goto L_0x00c4;
    L_0x00b5:
        r1.disconnect();
        goto L_0x00c4;
    L_0x00b9:
        r2 = move-exception;
        goto L_0x00c5;
    L_0x00bb:
        r2 = move-exception;
        r3 = "Failed to send feedback message";
        net.hockeyapp.android.utils.HockeyLog.error(r3, r2);	 Catch:{ all -> 0x00b9 }
        if (r1 == 0) goto L_0x00c4;
    L_0x00c3:
        goto L_0x00b5;
    L_0x00c4:
        return r0;
    L_0x00c5:
        if (r1 == 0) goto L_0x00ca;
    L_0x00c7:
        r1.disconnect();
    L_0x00ca:
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: net.hockeyapp.android.tasks.SendFeedbackTask.doPostPut():java.util.HashMap<java.lang.String, java.lang.String>");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.util.HashMap<java.lang.String, java.lang.String> doPostPutWithAttachments() {
        /*
        r6 = this;
        r0 = new java.util.HashMap;
        r0.<init>();
        r1 = "type";
        r2 = "send";
        r0.put(r1, r2);
        r1 = 0;
        r2 = new java.util.HashMap;	 Catch:{ IOException -> 0x00bf }
        r2.<init>();	 Catch:{ IOException -> 0x00bf }
        r3 = "name";
        r4 = r6.mName;	 Catch:{ IOException -> 0x00bf }
        r2.put(r3, r4);	 Catch:{ IOException -> 0x00bf }
        r3 = "email";
        r4 = r6.mEmail;	 Catch:{ IOException -> 0x00bf }
        r2.put(r3, r4);	 Catch:{ IOException -> 0x00bf }
        r3 = "subject";
        r4 = r6.mSubject;	 Catch:{ IOException -> 0x00bf }
        r2.put(r3, r4);	 Catch:{ IOException -> 0x00bf }
        r3 = "text";
        r4 = r6.mText;	 Catch:{ IOException -> 0x00bf }
        r2.put(r3, r4);	 Catch:{ IOException -> 0x00bf }
        r3 = "bundle_identifier";
        r4 = net.hockeyapp.android.Constants.APP_PACKAGE;	 Catch:{ IOException -> 0x00bf }
        r2.put(r3, r4);	 Catch:{ IOException -> 0x00bf }
        r3 = "bundle_short_version";
        r4 = net.hockeyapp.android.Constants.APP_VERSION_NAME;	 Catch:{ IOException -> 0x00bf }
        r2.put(r3, r4);	 Catch:{ IOException -> 0x00bf }
        r3 = "bundle_version";
        r4 = net.hockeyapp.android.Constants.APP_VERSION;	 Catch:{ IOException -> 0x00bf }
        r2.put(r3, r4);	 Catch:{ IOException -> 0x00bf }
        r3 = "os_version";
        r4 = net.hockeyapp.android.Constants.ANDROID_VERSION;	 Catch:{ IOException -> 0x00bf }
        r2.put(r3, r4);	 Catch:{ IOException -> 0x00bf }
        r3 = "oem";
        r4 = net.hockeyapp.android.Constants.PHONE_MANUFACTURER;	 Catch:{ IOException -> 0x00bf }
        r2.put(r3, r4);	 Catch:{ IOException -> 0x00bf }
        r3 = "model";
        r4 = net.hockeyapp.android.Constants.PHONE_MODEL;	 Catch:{ IOException -> 0x00bf }
        r2.put(r3, r4);	 Catch:{ IOException -> 0x00bf }
        r3 = "sdk_version";
        r4 = "5.0.4";
        r2.put(r3, r4);	 Catch:{ IOException -> 0x00bf }
        r3 = r6.mToken;	 Catch:{ IOException -> 0x00bf }
        if (r3 == 0) goto L_0x007d;
    L_0x0063:
        r3 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x00bf }
        r3.<init>();	 Catch:{ IOException -> 0x00bf }
        r4 = r6.mUrlString;	 Catch:{ IOException -> 0x00bf }
        r3.append(r4);	 Catch:{ IOException -> 0x00bf }
        r4 = r6.mToken;	 Catch:{ IOException -> 0x00bf }
        r3.append(r4);	 Catch:{ IOException -> 0x00bf }
        r4 = "/";
        r3.append(r4);	 Catch:{ IOException -> 0x00bf }
        r3 = r3.toString();	 Catch:{ IOException -> 0x00bf }
        r6.mUrlString = r3;	 Catch:{ IOException -> 0x00bf }
    L_0x007d:
        r3 = new net.hockeyapp.android.utils.HttpURLConnectionBuilder;	 Catch:{ IOException -> 0x00bf }
        r4 = r6.mUrlString;	 Catch:{ IOException -> 0x00bf }
        r3.<init>(r4);	 Catch:{ IOException -> 0x00bf }
        r4 = r6.mToken;	 Catch:{ IOException -> 0x00bf }
        if (r4 == 0) goto L_0x008b;
    L_0x0088:
        r4 = "PUT";
        goto L_0x008d;
    L_0x008b:
        r4 = "POST";
    L_0x008d:
        r3 = r3.setRequestMethod(r4);	 Catch:{ IOException -> 0x00bf }
        r4 = r6.mContext;	 Catch:{ IOException -> 0x00bf }
        r5 = r6.mAttachmentUris;	 Catch:{ IOException -> 0x00bf }
        r3 = r3.writeMultipartData(r2, r4, r5);	 Catch:{ IOException -> 0x00bf }
        r3 = r3.build();	 Catch:{ IOException -> 0x00bf }
        r1 = r3;
        r1.connect();	 Catch:{ IOException -> 0x00bf }
        r3 = "status";
        r4 = r1.getResponseCode();	 Catch:{ IOException -> 0x00bf }
        r4 = java.lang.String.valueOf(r4);	 Catch:{ IOException -> 0x00bf }
        r0.put(r3, r4);	 Catch:{ IOException -> 0x00bf }
        r3 = "response";
        r4 = net.hockeyapp.android.tasks.ConnectionTask.getStringFromConnection(r1);	 Catch:{ IOException -> 0x00bf }
        r0.put(r3, r4);	 Catch:{ IOException -> 0x00bf }
        if (r1 == 0) goto L_0x00c8;
    L_0x00b9:
        r1.disconnect();
        goto L_0x00c8;
    L_0x00bd:
        r2 = move-exception;
        goto L_0x00c9;
    L_0x00bf:
        r2 = move-exception;
        r3 = "Failed to send feedback message";
        net.hockeyapp.android.utils.HockeyLog.error(r3, r2);	 Catch:{ all -> 0x00bd }
        if (r1 == 0) goto L_0x00c8;
    L_0x00c7:
        goto L_0x00b9;
    L_0x00c8:
        return r0;
    L_0x00c9:
        if (r1 == 0) goto L_0x00ce;
    L_0x00cb:
        r1.disconnect();
    L_0x00ce:
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: net.hockeyapp.android.tasks.SendFeedbackTask.doPostPutWithAttachments():java.util.HashMap<java.lang.String, java.lang.String>");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.util.HashMap<java.lang.String, java.lang.String> doGet() {
        /*
        r5 = this;
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = r5.mUrlString;
        r0.append(r1);
        r1 = r5.mToken;
        r1 = net.hockeyapp.android.utils.Util.encodeParam(r1);
        r0.append(r1);
        r1 = r5.mLastMessageId;
        r2 = -1;
        if (r1 == r2) goto L_0x0022;
    L_0x0018:
        r1 = "?last_message_id=";
        r0.append(r1);
        r1 = r5.mLastMessageId;
        r0.append(r1);
    L_0x0022:
        r1 = new java.util.HashMap;
        r1.<init>();
        r2 = 0;
        r3 = new net.hockeyapp.android.utils.HttpURLConnectionBuilder;	 Catch:{ IOException -> 0x005e }
        r4 = r0.toString();	 Catch:{ IOException -> 0x005e }
        r3.<init>(r4);	 Catch:{ IOException -> 0x005e }
        r3 = r3.build();	 Catch:{ IOException -> 0x005e }
        r2 = r3;
        r3 = "type";
        r4 = "fetch";
        r1.put(r3, r4);	 Catch:{ IOException -> 0x005e }
        r2.connect();	 Catch:{ IOException -> 0x005e }
        r3 = "status";
        r4 = r2.getResponseCode();	 Catch:{ IOException -> 0x005e }
        r4 = java.lang.String.valueOf(r4);	 Catch:{ IOException -> 0x005e }
        r1.put(r3, r4);	 Catch:{ IOException -> 0x005e }
        r3 = "response";
        r4 = net.hockeyapp.android.tasks.ConnectionTask.getStringFromConnection(r2);	 Catch:{ IOException -> 0x005e }
        r1.put(r3, r4);	 Catch:{ IOException -> 0x005e }
        if (r2 == 0) goto L_0x0067;
    L_0x0058:
        r2.disconnect();
        goto L_0x0067;
    L_0x005c:
        r3 = move-exception;
        goto L_0x0068;
    L_0x005e:
        r3 = move-exception;
        r4 = "Failed to fetching feedback messages";
        net.hockeyapp.android.utils.HockeyLog.error(r4, r3);	 Catch:{ all -> 0x005c }
        if (r2 == 0) goto L_0x0067;
    L_0x0066:
        goto L_0x0058;
    L_0x0067:
        return r1;
    L_0x0068:
        if (r2 == 0) goto L_0x006d;
    L_0x006a:
        r2.disconnect();
    L_0x006d:
        throw r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: net.hockeyapp.android.tasks.SendFeedbackTask.doGet():java.util.HashMap<java.lang.String, java.lang.String>");
    }

    private String getLoadingMessage() {
        String loadingMessage = this.mContext.getString(R.string.hockeyapp_feedback_sending_feedback_text);
        if (this.mIsFetchMessages) {
            return this.mContext.getString(R.string.hockeyapp_feedback_fetching_feedback_text);
        }
        return loadingMessage;
    }
}
