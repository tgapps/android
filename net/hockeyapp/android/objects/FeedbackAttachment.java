package net.hockeyapp.android.objects;

import java.io.Serializable;

public class FeedbackAttachment implements Serializable {
    private String mCreatedAt;
    private String mFilename;
    private int mId;
    private int mMessageId;
    private String mUpdatedAt;
    private String mUrl;

    public void setId(int id) {
        this.mId = id;
    }

    public void setMessageId(int messageId) {
        this.mMessageId = messageId;
    }

    public String getFilename() {
        return this.mFilename;
    }

    public void setFilename(String filename) {
        this.mFilename = filename;
    }

    public String getUrl() {
        return this.mUrl;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    public void setCreatedAt(String createdAt) {
        this.mCreatedAt = createdAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.mUpdatedAt = updatedAt;
    }

    public String getCacheId() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(TtmlNode.ANONYMOUS_REGION_ID);
        stringBuilder.append(this.mMessageId);
        stringBuilder.append(this.mId);
        return stringBuilder.toString();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n");
        stringBuilder.append(FeedbackAttachment.class.getSimpleName());
        stringBuilder.append("\nid         ");
        stringBuilder.append(this.mId);
        stringBuilder.append("\nmessage id ");
        stringBuilder.append(this.mMessageId);
        stringBuilder.append("\nfilename   ");
        stringBuilder.append(this.mFilename);
        stringBuilder.append("\nurl        ");
        stringBuilder.append(this.mUrl);
        stringBuilder.append("\ncreatedAt  ");
        stringBuilder.append(this.mCreatedAt);
        stringBuilder.append("\nupdatedAt  ");
        stringBuilder.append(this.mUpdatedAt);
        return stringBuilder.toString();
    }
}
