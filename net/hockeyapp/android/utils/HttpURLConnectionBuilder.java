package net.hockeyapp.android.utils;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.telegram.messenger.exoplayer2.C;

public class HttpURLConnectionBuilder {
    private final Map<String, String> mHeaders;
    private SimpleMultipartEntity mMultipartEntity;
    private String mRequestBody;
    private String mRequestMethod;
    private int mTimeout = 120000;
    private final String mUrlString;

    public HttpURLConnectionBuilder(String urlString) {
        this.mUrlString = urlString;
        this.mHeaders = new HashMap();
        this.mHeaders.put("User-Agent", "HockeySDK/Android 5.0.4");
    }

    public HttpURLConnectionBuilder setRequestMethod(String requestMethod) {
        this.mRequestMethod = requestMethod;
        return this;
    }

    public HttpURLConnectionBuilder setRequestBody(String requestBody) {
        this.mRequestBody = requestBody;
        return this;
    }

    public HttpURLConnectionBuilder writeFormFields(Map<String, String> fields) {
        if (fields.size() > 25) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Fields size too large: ");
            stringBuilder.append(fields.size());
            stringBuilder.append(" - max allowed: ");
            stringBuilder.append(25);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        for (String key : fields.keySet()) {
            String value = (String) fields.get(key);
            if (value != null && ((long) value.length()) > 4194304) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Form field ");
                stringBuilder2.append(key);
                stringBuilder2.append(" size too large: ");
                stringBuilder2.append(value.length());
                stringBuilder2.append(" - max allowed: ");
                stringBuilder2.append(4194304);
                throw new IllegalArgumentException(stringBuilder2.toString());
            }
        }
        try {
            String formString = getFormString(fields, C.UTF8_NAME);
            setHeader("Content-Type", "application/x-www-form-urlencoded");
            setRequestBody(formString);
            return this;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public HttpURLConnectionBuilder writeMultipartData(Map<String, String> fields, Context context, List<Uri> attachmentUris) {
        try {
            this.mMultipartEntity = new SimpleMultipartEntity(File.createTempFile("multipart", null, context.getCacheDir()));
            this.mMultipartEntity.writeFirstBoundaryIfNeeds();
            for (String key : fields.keySet()) {
                this.mMultipartEntity.addPart(key, (String) fields.get(key));
            }
            for (int i = 0; i < attachmentUris.size(); i++) {
                Uri attachmentUri = (Uri) attachmentUris.get(i);
                boolean z = true;
                if (i != attachmentUris.size() - 1) {
                    z = false;
                }
                boolean lastFile = z;
                InputStream input = context.getContentResolver().openInputStream(attachmentUri);
                String filename = attachmentUri.getLastPathSegment();
                SimpleMultipartEntity simpleMultipartEntity = this.mMultipartEntity;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("attachment");
                stringBuilder.append(i);
                simpleMultipartEntity.addPart(stringBuilder.toString(), filename, input, lastFile);
            }
            this.mMultipartEntity.writeLastBoundaryIfNeeds();
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("multipart/form-data; boundary=");
            stringBuilder2.append(this.mMultipartEntity.getBoundary());
            setHeader("Content-Type", stringBuilder2.toString());
            return this;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public HttpURLConnectionBuilder setHeader(String name, String value) {
        this.mHeaders.put(name, value);
        return this;
    }

    public HttpURLConnectionBuilder setBasicAuthorization(String username, String password) {
        String authString = new StringBuilder();
        authString.append("Basic ");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(username);
        stringBuilder.append(":");
        stringBuilder.append(password);
        authString.append(Base64.encodeToString(stringBuilder.toString().getBytes(), 2));
        setHeader("Authorization", authString.toString());
        return this;
    }

    public HttpURLConnection build() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(this.mUrlString).openConnection();
        connection.setConnectTimeout(this.mTimeout);
        connection.setReadTimeout(this.mTimeout);
        if (!TextUtils.isEmpty(this.mRequestMethod)) {
            connection.setRequestMethod(this.mRequestMethod);
            if (!TextUtils.isEmpty(this.mRequestBody) || this.mRequestMethod.equalsIgnoreCase("POST") || this.mRequestMethod.equalsIgnoreCase("PUT")) {
                connection.setDoOutput(true);
            }
        }
        for (String name : this.mHeaders.keySet()) {
            connection.setRequestProperty(name, (String) this.mHeaders.get(name));
        }
        if (!TextUtils.isEmpty(this.mRequestBody)) {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), C.UTF8_NAME));
            writer.write(this.mRequestBody);
            writer.flush();
            writer.close();
        }
        if (this.mMultipartEntity != null) {
            connection.setRequestProperty("Content-Length", String.valueOf(this.mMultipartEntity.getContentLength()));
            this.mMultipartEntity.writeTo(connection.getOutputStream());
        }
        return connection;
    }

    private static String getFormString(Map<String, String> params, String charset) throws UnsupportedEncodingException {
        List<String> protoList = new ArrayList();
        for (String key : params.keySet()) {
            String value = (String) params.get(key);
            String key2 = URLEncoder.encode(key2, charset);
            value = URLEncoder.encode(value, charset);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(key2);
            stringBuilder.append("=");
            stringBuilder.append(value);
            protoList.add(stringBuilder.toString());
        }
        return TextUtils.join("&", protoList);
    }
}
