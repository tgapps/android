package net.hockeyapp.android.tasks;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Map;
import net.hockeyapp.android.Constants;
import net.hockeyapp.android.utils.HockeyLog;
import net.hockeyapp.android.utils.HttpURLConnectionBuilder;
import org.json.JSONObject;

@SuppressLint({"StaticFieldLeak"})
public class LoginTask extends ConnectionTask<Void, Void, Boolean> {
    private Context mContext;
    private Handler mHandler;
    private final int mMode;
    private final Map<String, String> mParams;
    private ProgressDialog mProgressDialog;
    private boolean mShowProgressDialog = true;
    private final String mUrlString;

    public LoginTask(Context context, Handler handler, String urlString, int mode, Map<String, String> params) {
        this.mContext = context;
        this.mHandler = handler;
        this.mUrlString = urlString;
        this.mMode = mode;
        this.mParams = params;
        if (context != null) {
            Constants.loadFromContext(context);
        }
    }

    public void attach(Context context, Handler handler) {
        this.mContext = context;
        this.mHandler = handler;
    }

    public void detach() {
        this.mContext = null;
        this.mHandler = null;
        this.mProgressDialog = null;
    }

    protected void onPreExecute() {
        if ((this.mProgressDialog == null || !this.mProgressDialog.isShowing()) && this.mShowProgressDialog) {
            this.mProgressDialog = ProgressDialog.show(this.mContext, TtmlNode.ANONYMOUS_REGION_ID, "Please wait...", true, false);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected java.lang.Boolean doInBackground(java.lang.Void... r4) {
        /*
        r3 = this;
        r0 = 0;
        r1 = r3.mMode;	 Catch:{ IOException -> 0x0035 }
        r2 = r3.mParams;	 Catch:{ IOException -> 0x0035 }
        r1 = r3.makeRequest(r1, r2);	 Catch:{ IOException -> 0x0035 }
        r0 = r1;
        r0.connect();	 Catch:{ IOException -> 0x0035 }
        r1 = r0.getResponseCode();	 Catch:{ IOException -> 0x0035 }
        r2 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        if (r1 != r2) goto L_0x002d;
    L_0x0015:
        r1 = net.hockeyapp.android.tasks.ConnectionTask.getStringFromConnection(r0);	 Catch:{ IOException -> 0x0035 }
        r2 = android.text.TextUtils.isEmpty(r1);	 Catch:{ IOException -> 0x0035 }
        if (r2 != 0) goto L_0x002d;
    L_0x001f:
        r2 = r3.handleResponse(r1);	 Catch:{ IOException -> 0x0035 }
        r2 = java.lang.Boolean.valueOf(r2);	 Catch:{ IOException -> 0x0035 }
        if (r0 == 0) goto L_0x002c;
    L_0x0029:
        r0.disconnect();
    L_0x002c:
        return r2;
    L_0x002d:
        if (r0 == 0) goto L_0x003e;
    L_0x002f:
        r0.disconnect();
        goto L_0x003e;
    L_0x0033:
        r1 = move-exception;
        goto L_0x0044;
    L_0x0035:
        r1 = move-exception;
        r2 = "Failed to login";
        net.hockeyapp.android.utils.HockeyLog.error(r2, r1);	 Catch:{ all -> 0x0033 }
        if (r0 == 0) goto L_0x003e;
    L_0x003d:
        goto L_0x002f;
    L_0x003e:
        r1 = 0;
        r1 = java.lang.Boolean.valueOf(r1);
        return r1;
    L_0x0044:
        if (r0 == 0) goto L_0x0049;
    L_0x0046:
        r0.disconnect();
    L_0x0049:
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: net.hockeyapp.android.tasks.LoginTask.doInBackground(java.lang.Void[]):java.lang.Boolean");
    }

    protected void onPostExecute(Boolean success) {
        if (this.mProgressDialog != null) {
            try {
                this.mProgressDialog.dismiss();
            } catch (Exception e) {
            }
        }
        if (this.mHandler != null) {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putBoolean("success", success.booleanValue());
            msg.setData(bundle);
            this.mHandler.sendMessage(msg);
        }
    }

    private HttpURLConnection makeRequest(int mode, Map<String, String> params) throws IOException {
        if (mode == 1) {
            return new HttpURLConnectionBuilder(this.mUrlString).setRequestMethod("POST").writeFormFields(params).build();
        }
        if (mode == 2) {
            return new HttpURLConnectionBuilder(this.mUrlString).setRequestMethod("POST").setBasicAuthorization((String) params.get("email"), (String) params.get("password")).build();
        }
        if (mode == 3) {
            String type = (String) params.get("type");
            String id = (String) params.get(TtmlNode.ATTR_ID);
            String paramUrl = new StringBuilder();
            paramUrl.append(this.mUrlString);
            paramUrl.append("?");
            paramUrl.append(type);
            paramUrl.append("=");
            paramUrl.append(id);
            return new HttpURLConnectionBuilder(paramUrl.toString()).build();
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Login mode ");
        stringBuilder.append(mode);
        stringBuilder.append(" not supported.");
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    private boolean handleResponse(String responseStr) {
        SharedPreferences prefs = this.mContext.getSharedPreferences("net.hockeyapp.android.login", 0);
        try {
            JSONObject response = new JSONObject(responseStr);
            String status = response.getString("status");
            if (TextUtils.isEmpty(status)) {
                return false;
            }
            String iuid;
            if (this.mMode == 1) {
                if (status.equals("identified")) {
                    iuid = response.getString("iuid");
                    if (!TextUtils.isEmpty(iuid)) {
                        prefs.edit().putString("iuid", iuid).putString("email", (String) this.mParams.get("email")).apply();
                        return true;
                    }
                }
            } else if (this.mMode == 2) {
                if (status.equals("authorized")) {
                    iuid = response.getString("auid");
                    if (!TextUtils.isEmpty(iuid)) {
                        prefs.edit().putString("auid", iuid).putString("email", (String) this.mParams.get("email")).apply();
                        return true;
                    }
                }
            } else if (this.mMode != 3) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Login mode ");
                stringBuilder.append(this.mMode);
                stringBuilder.append(" not supported.");
                throw new IllegalArgumentException(stringBuilder.toString());
            } else if (status.equals("validated")) {
                return true;
            } else {
                prefs.edit().remove("iuid").remove("auid").remove("email").apply();
            }
            return false;
        } catch (Throwable e) {
            HockeyLog.error("Failed to parse login response", e);
            return false;
        }
    }
}
