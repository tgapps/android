package com.google.android.gms.common.api;

public class ApiException extends Exception {
    protected final Status mStatus;

    public ApiException(Status status) {
        int statusCode = status.getStatusCode();
        String statusMessage = status.getStatusMessage() != null ? status.getStatusMessage() : TtmlNode.ANONYMOUS_REGION_ID;
        StringBuilder stringBuilder = new StringBuilder(13 + String.valueOf(statusMessage).length());
        stringBuilder.append(statusCode);
        stringBuilder.append(": ");
        stringBuilder.append(statusMessage);
        super(stringBuilder.toString());
        this.mStatus = status;
    }
}
