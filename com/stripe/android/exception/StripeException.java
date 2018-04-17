package com.stripe.android.exception;

public abstract class StripeException extends Exception {
    private String requestId;
    private Integer statusCode;

    public StripeException(String message, String requestId, Integer statusCode) {
        super(message, null);
        this.requestId = requestId;
        this.statusCode = statusCode;
    }

    public StripeException(String message, String requestId, Integer statusCode, Throwable e) {
        super(message, e);
        this.statusCode = statusCode;
        this.requestId = requestId;
    }

    public String toString() {
        StringBuilder stringBuilder;
        String reqIdStr = TtmlNode.ANONYMOUS_REGION_ID;
        if (this.requestId != null) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("; request-id: ");
            stringBuilder.append(this.requestId);
            reqIdStr = stringBuilder.toString();
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append(super.toString());
        stringBuilder.append(reqIdStr);
        return stringBuilder.toString();
    }
}
