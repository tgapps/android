package com.stripe.android;

import android.os.AsyncTask;
import android.os.Build.VERSION;
import com.stripe.android.exception.AuthenticationException;
import com.stripe.android.exception.StripeException;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.net.RequestOptions;
import com.stripe.android.net.StripeApiHandler;
import com.stripe.android.util.StripeNetworkUtils;
import java.util.concurrent.Executor;

public class Stripe {
    private String defaultPublishableKey;
    TokenCreator tokenCreator = new TokenCreator() {
        public void create(final Card card, final String publishableKey, Executor executor, final TokenCallback callback) {
            Stripe.this.executeTokenTask(executor, new AsyncTask<Void, Void, ResponseWrapper>() {
                protected ResponseWrapper doInBackground(Void... params) {
                    try {
                        return new ResponseWrapper(StripeApiHandler.createToken(StripeNetworkUtils.hashMapFromCard(card), RequestOptions.builder(publishableKey).build()), null);
                    } catch (StripeException e) {
                        return new ResponseWrapper(null, e);
                    }
                }

                protected void onPostExecute(ResponseWrapper result) {
                    Stripe.this.tokenTaskPostExecution(result, callback);
                }
            });
        }
    };

    private class ResponseWrapper {
        final Exception error;
        final Token token;

        private ResponseWrapper(Token token, Exception error) {
            this.error = error;
            this.token = token;
        }
    }

    interface TokenCreator {
        void create(Card card, String str, Executor executor, TokenCallback tokenCallback);
    }

    public void createToken(com.stripe.android.model.Card r1, java.lang.String r2, java.util.concurrent.Executor r3, com.stripe.android.TokenCallback r4) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: com.stripe.android.Stripe.createToken(com.stripe.android.model.Card, java.lang.String, java.util.concurrent.Executor, com.stripe.android.TokenCallback):void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
        /*
        r0 = this;
        if (r3 != 0) goto L_0x000c;
    L_0x0002:
        r0 = new java.lang.RuntimeException;	 Catch:{ AuthenticationException -> 0x000a }
        r1 = "Required Parameter: 'card' is required to create a token";	 Catch:{ AuthenticationException -> 0x000a }
        r0.<init>(r1);	 Catch:{ AuthenticationException -> 0x000a }
        throw r0;	 Catch:{ AuthenticationException -> 0x000a }
    L_0x000a:
        r0 = move-exception;	 Catch:{ AuthenticationException -> 0x000a }
        goto L_0x001f;	 Catch:{ AuthenticationException -> 0x000a }
    L_0x000c:
        if (r6 != 0) goto L_0x0016;	 Catch:{ AuthenticationException -> 0x000a }
        r0 = new java.lang.RuntimeException;	 Catch:{ AuthenticationException -> 0x000a }
        r1 = "Required Parameter: 'callback' is required to use the created token and handle errors";	 Catch:{ AuthenticationException -> 0x000a }
        r0.<init>(r1);	 Catch:{ AuthenticationException -> 0x000a }
        throw r0;	 Catch:{ AuthenticationException -> 0x000a }
        r2.validateKey(r4);	 Catch:{ AuthenticationException -> 0x000a }
        r0 = r2.tokenCreator;	 Catch:{ AuthenticationException -> 0x000a }
        r0.create(r3, r4, r5, r6);	 Catch:{ AuthenticationException -> 0x000a }
        goto L_0x0023;
        r6.onError(r0);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.stripe.android.Stripe.createToken(com.stripe.android.model.Card, java.lang.String, java.util.concurrent.Executor, com.stripe.android.TokenCallback):void");
    }

    public Stripe(String publishableKey) throws AuthenticationException {
        setDefaultPublishableKey(publishableKey);
    }

    public void createToken(Card card, TokenCallback callback) {
        createToken(card, this.defaultPublishableKey, callback);
    }

    public void createToken(Card card, String publishableKey, TokenCallback callback) {
        createToken(card, publishableKey, null, callback);
    }

    public void setDefaultPublishableKey(String publishableKey) throws AuthenticationException {
        validateKey(publishableKey);
        this.defaultPublishableKey = publishableKey;
    }

    private void validateKey(String publishableKey) throws AuthenticationException {
        if (publishableKey != null) {
            if (publishableKey.length() != 0) {
                if (publishableKey.startsWith("sk_")) {
                    throw new AuthenticationException("Invalid Publishable Key: You are using a secret key to create a token, instead of the publishable one. For more info, see https://stripe.com/docs/stripe.js", null, Integer.valueOf(0));
                }
                return;
            }
        }
        throw new AuthenticationException("Invalid Publishable Key: You must use a valid publishable key to create a token.  For more info, see https://stripe.com/docs/stripe.js.", null, Integer.valueOf(0));
    }

    private void tokenTaskPostExecution(ResponseWrapper result, TokenCallback callback) {
        if (result.token != null) {
            callback.onSuccess(result.token);
        } else if (result.error != null) {
            callback.onError(result.error);
        } else {
            callback.onError(new RuntimeException("Somehow got neither a token response or an error response"));
        }
    }

    private void executeTokenTask(Executor executor, AsyncTask<Void, Void, ResponseWrapper> task) {
        if (executor == null || VERSION.SDK_INT <= 11) {
            task.execute(new Void[0]);
        } else {
            task.executeOnExecutor(executor, new Void[0]);
        }
    }
}
