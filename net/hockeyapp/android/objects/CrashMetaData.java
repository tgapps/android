package net.hockeyapp.android.objects;

public class CrashMetaData {
    private String mUserDescription;
    private String mUserEmail;
    private String mUserID;

    public String getUserDescription() {
        return this.mUserDescription;
    }

    public String getUserEmail() {
        return this.mUserEmail;
    }

    public String getUserID() {
        return this.mUserID;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n");
        stringBuilder.append(CrashMetaData.class.getSimpleName());
        stringBuilder.append("\nuserDescription ");
        stringBuilder.append(this.mUserDescription);
        stringBuilder.append("\nuserEmail       ");
        stringBuilder.append(this.mUserEmail);
        stringBuilder.append("\nuserID          ");
        stringBuilder.append(this.mUserID);
        return stringBuilder.toString();
    }
}
