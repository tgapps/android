package com.stripe.android.net;

import com.stripe.android.exception.APIConnectionException;
import com.stripe.android.exception.APIException;
import com.stripe.android.exception.AuthenticationException;
import com.stripe.android.exception.CardException;
import com.stripe.android.exception.InvalidRequestException;
import com.stripe.android.exception.PermissionException;
import com.stripe.android.exception.RateLimitException;
import com.stripe.android.model.Token;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import org.json.JSONObject;
import org.telegram.messenger.exoplayer2.C;
import org.telegram.messenger.exoplayer2.DefaultLoadControl;
import org.telegram.messenger.support.widget.helper.ItemTouchHelper.Callback;

public class StripeApiHandler {
    private static final SSLSocketFactory SSL_SOCKET_FACTORY = new StripeSSLSocketFactory();

    private static final class Parameter {
        public final String key;
        public final String value;

        public Parameter(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private static com.stripe.android.model.Token requestToken(java.lang.String r12, java.lang.String r13, java.util.Map<java.lang.String, java.lang.Object> r14, com.stripe.android.net.RequestOptions r15) throws com.stripe.android.exception.AuthenticationException, com.stripe.android.exception.InvalidRequestException, com.stripe.android.exception.APIConnectionException, com.stripe.android.exception.CardException, com.stripe.android.exception.APIException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find block by offset: 0x00b8 in list []
	at jadx.core.utils.BlockUtils.getBlockByOffset(BlockUtils.java:42)
	at jadx.core.dex.instructions.IfNode.initBlocks(IfNode.java:60)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.initBlocksInIfNodes(BlockFinish.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.visit(BlockFinish.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
        /*
        r0 = 0;
        if (r15 != 0) goto L_0x0004;
    L_0x0003:
        return r0;
    L_0x0004:
        r1 = 0;
        r2 = 1;
        r2 = java.lang.Boolean.valueOf(r2);
        r3 = 0;
        r4 = "networkaddress.cache.ttl";	 Catch:{ SecurityException -> 0x001a }
        r4 = java.security.Security.getProperty(r4);	 Catch:{ SecurityException -> 0x001a }
        r1 = r4;	 Catch:{ SecurityException -> 0x001a }
        r4 = "networkaddress.cache.ttl";	 Catch:{ SecurityException -> 0x001a }
        r5 = "0";	 Catch:{ SecurityException -> 0x001a }
        java.security.Security.setProperty(r4, r5);	 Catch:{ SecurityException -> 0x001a }
        goto L_0x001f;
    L_0x001a:
        r4 = move-exception;
        r2 = java.lang.Boolean.valueOf(r3);
    L_0x001f:
        r4 = r15.getPublishableApiKey();
        r5 = r4.trim();
        r5 = r5.isEmpty();
        if (r5 == 0) goto L_0x0039;
    L_0x002d:
        r5 = new com.stripe.android.exception.AuthenticationException;
        r6 = "No API key provided. (HINT: set your API key using 'Stripe.apiKey = <API-KEY>'. You can generate API keys from the Stripe web interface. See https://stripe.com/api for details or email support@stripe.com if you have questions.";
        r3 = java.lang.Integer.valueOf(r3);
        r5.<init>(r6, r0, r3);
        throw r5;
    L_0x0039:
        r5 = getStripeResponse(r12, r13, r14, r15);	 Catch:{ JSONException -> 0x00a1, all -> 0x008a }
        r6 = r5.getResponseCode();	 Catch:{ JSONException -> 0x00a1, all -> 0x008a }
        r7 = r5.getResponseBody();	 Catch:{ JSONException -> 0x00a1, all -> 0x008a }
        r8 = 0;	 Catch:{ JSONException -> 0x00a1, all -> 0x008a }
        r9 = r5.getResponseHeaders();	 Catch:{ JSONException -> 0x00a1, all -> 0x008a }
        if (r9 != 0) goto L_0x004e;	 Catch:{ JSONException -> 0x00a1, all -> 0x008a }
    L_0x004c:
        r10 = r0;	 Catch:{ JSONException -> 0x00a1, all -> 0x008a }
        goto L_0x0056;	 Catch:{ JSONException -> 0x00a1, all -> 0x008a }
    L_0x004e:
        r10 = "Request-Id";	 Catch:{ JSONException -> 0x00a1, all -> 0x008a }
        r10 = r9.get(r10);	 Catch:{ JSONException -> 0x00a1, all -> 0x008a }
        r10 = (java.util.List) r10;	 Catch:{ JSONException -> 0x00a1, all -> 0x008a }
    L_0x0056:
        if (r10 == 0) goto L_0x0065;	 Catch:{ JSONException -> 0x00a1, all -> 0x008a }
    L_0x0058:
        r11 = r10.size();	 Catch:{ JSONException -> 0x00a1, all -> 0x008a }
        if (r11 <= 0) goto L_0x0065;	 Catch:{ JSONException -> 0x00a1, all -> 0x008a }
    L_0x005e:
        r3 = r10.get(r3);	 Catch:{ JSONException -> 0x00a1, all -> 0x008a }
        r3 = (java.lang.String) r3;	 Catch:{ JSONException -> 0x00a1, all -> 0x008a }
        r8 = r3;	 Catch:{ JSONException -> 0x00a1, all -> 0x008a }
    L_0x0065:
        r3 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;	 Catch:{ JSONException -> 0x00a1, all -> 0x008a }
        if (r6 < r3) goto L_0x006d;	 Catch:{ JSONException -> 0x00a1, all -> 0x008a }
    L_0x0069:
        r3 = 300; // 0x12c float:4.2E-43 double:1.48E-321;	 Catch:{ JSONException -> 0x00a1, all -> 0x008a }
        if (r6 < r3) goto L_0x0070;	 Catch:{ JSONException -> 0x00a1, all -> 0x008a }
    L_0x006d:
        handleAPIError(r7, r6, r8);	 Catch:{ JSONException -> 0x00a1, all -> 0x008a }
    L_0x0070:
        r3 = com.stripe.android.net.TokenParser.parseToken(r7);	 Catch:{ JSONException -> 0x00a1, all -> 0x008a }
        r0 = r2.booleanValue();
        if (r0 == 0) goto L_0x0089;
    L_0x007a:
        if (r1 != 0) goto L_0x0084;
    L_0x007c:
        r0 = "networkaddress.cache.ttl";
        r11 = "-1";
        java.security.Security.setProperty(r0, r11);
        goto L_0x0089;
    L_0x0084:
        r0 = "networkaddress.cache.ttl";
        java.security.Security.setProperty(r0, r1);
    L_0x0089:
        return r3;
    L_0x008a:
        r0 = move-exception;
        r3 = r2.booleanValue();
        if (r3 == 0) goto L_0x00a0;
    L_0x0091:
        if (r1 != 0) goto L_0x009b;
    L_0x0093:
        r3 = "networkaddress.cache.ttl";
        r5 = "-1";
        java.security.Security.setProperty(r3, r5);
        goto L_0x00a0;
    L_0x009b:
        r3 = "networkaddress.cache.ttl";
        java.security.Security.setProperty(r3, r1);
    L_0x00a0:
        throw r0;
    L_0x00a1:
        r3 = move-exception;
        r5 = r2.booleanValue();
        if (r5 == 0) goto L_0x00b8;
    L_0x00a9:
        if (r1 != 0) goto L_0x00b3;
    L_0x00ab:
        r5 = "networkaddress.cache.ttl";
        r6 = "-1";
        java.security.Security.setProperty(r5, r6);
        goto L_0x00b8;
    L_0x00b3:
        r5 = "networkaddress.cache.ttl";
        java.security.Security.setProperty(r5, r1);
    L_0x00b8:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.stripe.android.net.StripeApiHandler.requestToken(java.lang.String, java.lang.String, java.util.Map, com.stripe.android.net.RequestOptions):com.stripe.android.model.Token");
    }

    public static Token createToken(Map<String, Object> cardParams, RequestOptions options) throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException {
        return requestToken("POST", getApiUrl(), cardParams, options);
    }

    static String createQuery(Map<String, Object> params) throws UnsupportedEncodingException, InvalidRequestException {
        StringBuilder queryStringBuffer = new StringBuilder();
        for (Parameter param : flattenParams(params)) {
            if (queryStringBuffer.length() > 0) {
                queryStringBuffer.append("&");
            }
            queryStringBuffer.append(urlEncodePair(param.key, param.value));
        }
        return queryStringBuffer.toString();
    }

    static Map<String, String> getHeaders(RequestOptions options) {
        Map<String, String> headers = new HashMap();
        String apiVersion = options.getApiVersion();
        headers.put("Accept-Charset", C.UTF8_NAME);
        headers.put("Accept", "application/json");
        Object[] objArr = new Object[1];
        int i = 0;
        objArr[0] = "3.5.0";
        headers.put("User-Agent", String.format("Stripe/v1 JavaBindings/%s", objArr));
        headers.put("Authorization", String.format("Bearer %s", new Object[]{options.getPublishableApiKey()}));
        String[] propertyNames = new String[]{"os.name", "os.version", "os.arch", "java.version", "java.vendor", "java.vm.version", "java.vm.vendor"};
        Map<String, String> propertyMap = new HashMap();
        int length = propertyNames.length;
        while (i < length) {
            String propertyName = propertyNames[i];
            propertyMap.put(propertyName, System.getProperty(propertyName));
            i++;
        }
        propertyMap.put("bindings.version", "3.5.0");
        propertyMap.put("lang", "Java");
        propertyMap.put("publisher", "Stripe");
        headers.put("X-Stripe-Client-User-Agent", new JSONObject(propertyMap).toString());
        if (apiVersion != null) {
            headers.put("Stripe-Version", apiVersion);
        }
        if (options.getIdempotencyKey() != null) {
            headers.put("Idempotency-Key", options.getIdempotencyKey());
        }
        return headers;
    }

    static String getApiUrl() {
        return String.format("%s/v1/%s", new Object[]{"https://api.stripe.com", "tokens"});
    }

    private static String formatURL(String url, String query) {
        if (query != null) {
            if (!query.isEmpty()) {
                String separator = url.contains("?") ? "&" : "?";
                return String.format("%s%s%s", new Object[]{url, separator, query});
            }
        }
        return url;
    }

    private static HttpURLConnection createGetConnection(String url, String query, RequestOptions options) throws IOException {
        HttpURLConnection conn = createStripeConnection(formatURL(url, query), options);
        conn.setRequestMethod("GET");
        return conn;
    }

    private static HttpURLConnection createPostConnection(String url, String query, RequestOptions options) throws IOException {
        HttpURLConnection conn = createStripeConnection(url, options);
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", String.format("application/x-www-form-urlencoded;charset=%s", new Object[]{C.UTF8_NAME}));
        OutputStream output = null;
        try {
            output = conn.getOutputStream();
            output.write(query.getBytes(C.UTF8_NAME));
            return conn;
        } finally {
            if (output != null) {
                output.close();
            }
        }
    }

    private static HttpURLConnection createStripeConnection(String url, RequestOptions options) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setConnectTimeout(DefaultLoadControl.DEFAULT_MAX_BUFFER_MS);
        conn.setReadTimeout(80000);
        conn.setUseCaches(false);
        for (Entry<String, String> header : getHeaders(options).entrySet()) {
            conn.setRequestProperty((String) header.getKey(), (String) header.getValue());
        }
        if (conn instanceof HttpsURLConnection) {
            ((HttpsURLConnection) conn).setSSLSocketFactory(SSL_SOCKET_FACTORY);
        }
        return conn;
    }

    private static StripeResponse getStripeResponse(String method, String url, Map<String, Object> params, RequestOptions options) throws InvalidRequestException, APIConnectionException, APIException {
        try {
            return makeURLConnectionRequest(method, url, createQuery(params), options);
        } catch (UnsupportedEncodingException e) {
            throw new InvalidRequestException("Unable to encode parameters to UTF-8. Please contact support@stripe.com for assistance.", null, null, Integer.valueOf(0), e);
        }
    }

    private static List<Parameter> flattenParams(Map<String, Object> params) throws InvalidRequestException {
        return flattenParamsMap(params, null);
    }

    private static List<Parameter> flattenParamsList(List<Object> params, String keyPrefix) throws InvalidRequestException {
        List<Parameter> flatParams = new LinkedList();
        String newPrefix = String.format("%s[]", new Object[]{keyPrefix});
        if (params.isEmpty()) {
            flatParams.add(new Parameter(keyPrefix, TtmlNode.ANONYMOUS_REGION_ID));
        } else {
            for (Object flattenParamsValue : params) {
                flatParams.addAll(flattenParamsValue(flattenParamsValue, newPrefix));
            }
        }
        return flatParams;
    }

    private static List<Parameter> flattenParamsMap(Map<String, Object> params, String keyPrefix) throws InvalidRequestException {
        List<Parameter> flatParams = new LinkedList();
        if (params == null) {
            return flatParams;
        }
        for (Entry<String, Object> entry : params.entrySet()) {
            String key = (String) entry.getKey();
            Object value = entry.getValue();
            String newPrefix = key;
            if (keyPrefix != null) {
                newPrefix = String.format("%s[%s]", new Object[]{keyPrefix, key});
            }
            flatParams.addAll(flattenParamsValue(value, newPrefix));
        }
        return flatParams;
    }

    private static List<Parameter> flattenParamsValue(Object value, String keyPrefix) throws InvalidRequestException {
        List<Parameter> flatParams;
        if (value instanceof Map) {
            flatParams = flattenParamsMap((Map) value, keyPrefix);
        } else if (value instanceof List) {
            flatParams = flattenParamsList((List) value, keyPrefix);
        } else if (TtmlNode.ANONYMOUS_REGION_ID.equals(value)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("You cannot set '");
            stringBuilder.append(keyPrefix);
            stringBuilder.append("' to an empty string. We interpret empty strings as null in requests. You may set '");
            stringBuilder.append(keyPrefix);
            stringBuilder.append("' to null to delete the property.");
            throw new InvalidRequestException(stringBuilder.toString(), keyPrefix, null, Integer.valueOf(0), null);
        } else if (value == null) {
            flatParams = new LinkedList();
            flatParams.add(new Parameter(keyPrefix, TtmlNode.ANONYMOUS_REGION_ID));
            return flatParams;
        } else {
            flatParams = new LinkedList();
            flatParams.add(new Parameter(keyPrefix, value.toString()));
            return flatParams;
        }
        return flatParams;
    }

    private static void handleAPIError(String rBody, int rCode, String requestId) throws InvalidRequestException, AuthenticationException, CardException, APIException {
        StripeError stripeError = ErrorParser.parseError(rBody);
        if (rCode != 429) {
            switch (rCode) {
                case 400:
                    throw new InvalidRequestException(stripeError.message, stripeError.param, requestId, Integer.valueOf(rCode), null);
                case 401:
                    throw new AuthenticationException(stripeError.message, requestId, Integer.valueOf(rCode));
                case 402:
                    throw new CardException(stripeError.message, requestId, stripeError.code, stripeError.param, stripeError.decline_code, stripeError.charge, Integer.valueOf(rCode), null);
                case 403:
                    throw new PermissionException(stripeError.message, requestId, Integer.valueOf(rCode));
                case 404:
                    throw new InvalidRequestException(stripeError.message, stripeError.param, requestId, Integer.valueOf(rCode), null);
                default:
                    throw new APIException(stripeError.message, requestId, Integer.valueOf(rCode), null);
            }
        }
        throw new RateLimitException(stripeError.message, stripeError.param, requestId, Integer.valueOf(rCode), null);
    }

    private static String urlEncodePair(String k, String v) throws UnsupportedEncodingException {
        return String.format("%s=%s", new Object[]{urlEncode(k), urlEncode(v)});
    }

    private static String urlEncode(String str) throws UnsupportedEncodingException {
        if (str == null) {
            return null;
        }
        return URLEncoder.encode(str, C.UTF8_NAME);
    }

    private static StripeResponse makeURLConnectionRequest(String method, String url, String query, RequestOptions options) throws APIConnectionException {
        HttpURLConnection conn = null;
        int i = -1;
        try {
            String rBody;
            int hashCode = method.hashCode();
            if (hashCode != 70454) {
                if (hashCode == 2461856) {
                    if (method.equals("POST")) {
                        i = 1;
                    }
                }
            } else if (method.equals("GET")) {
                i = 0;
            }
            switch (i) {
                case 0:
                    conn = createGetConnection(url, query, options);
                    break;
                case 1:
                    conn = createPostConnection(url, query, options);
                    break;
                default:
                    throw new APIConnectionException(String.format("Unrecognized HTTP method %s. This indicates a bug in the Stripe bindings. Please contact support@stripe.com for assistance.", new Object[]{method}));
            }
            i = conn.getResponseCode();
            if (i < Callback.DEFAULT_DRAG_ANIMATION_DURATION || i >= 300) {
                rBody = getResponseBody(conn.getErrorStream());
            } else {
                rBody = getResponseBody(conn.getInputStream());
            }
            StripeResponse stripeResponse = new StripeResponse(i, rBody, conn.getHeaderFields());
            if (conn != null) {
                conn.disconnect();
            }
            return stripeResponse;
        } catch (IOException e) {
            throw new APIConnectionException(String.format("IOException during API request to Stripe (%s): %s Please check your internet connection and try again. If this problem persists, you should check Stripe's service status at https://twitter.com/stripestatus, or let us know at support@stripe.com.", new Object[]{getApiUrl(), e.getMessage()}), e);
        } catch (Throwable th) {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    private static String getResponseBody(InputStream responseStream) throws IOException {
        String rBody = new Scanner(responseStream, C.UTF8_NAME).useDelimiter("\\A").next();
        responseStream.close();
        return rBody;
    }
}
