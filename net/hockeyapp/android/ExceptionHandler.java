package net.hockeyapp.android;

import android.content.Context;
import android.os.Process;
import android.text.TextUtils;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Date;
import java.util.UUID;
import net.hockeyapp.android.objects.CrashDetails;
import net.hockeyapp.android.utils.HockeyLog;

public class ExceptionHandler implements UncaughtExceptionHandler {
    private CrashManagerListener mCrashManagerListener;
    private UncaughtExceptionHandler mDefaultExceptionHandler;
    private boolean mIgnoreDefaultHandler = false;

    public ExceptionHandler(UncaughtExceptionHandler defaultExceptionHandler, CrashManagerListener listener, boolean ignoreDefaultHandler) {
        this.mDefaultExceptionHandler = defaultExceptionHandler;
        this.mIgnoreDefaultHandler = ignoreDefaultHandler;
        this.mCrashManagerListener = listener;
    }

    public void setListener(CrashManagerListener listener) {
        this.mCrashManagerListener = listener;
    }

    public static void saveException(Throwable exception, Thread thread, CrashManagerListener listener) {
        Date now = new Date();
        Date startDate = new Date(CrashManager.getInitializeTimestamp());
        exception.printStackTrace(new PrintWriter(new StringWriter()));
        Context context = CrashManager.weakContext != null ? (Context) CrashManager.weakContext.get() : null;
        if (context == null) {
            HockeyLog.error("Failed to save exception: context in CrashManager is null");
        } else if (CrashManager.stackTracesCount >= 100) {
            HockeyLog.warn("ExceptionHandler: HockeyApp will not save this exception as there are already 100 or more unsent exceptions on disk");
        } else {
            String filename = UUID.randomUUID().toString();
            CrashDetails crashDetails = new CrashDetails(filename, exception);
            crashDetails.setAppPackage(Constants.APP_PACKAGE);
            crashDetails.setAppVersionCode(Constants.APP_VERSION);
            crashDetails.setAppVersionName(Constants.APP_VERSION_NAME);
            crashDetails.setAppStartDate(startDate);
            crashDetails.setAppCrashDate(now);
            if (listener == null || listener.includeDeviceData()) {
                crashDetails.setOsVersion(Constants.ANDROID_VERSION);
                crashDetails.setOsBuild(Constants.ANDROID_BUILD);
                crashDetails.setDeviceManufacturer(Constants.PHONE_MANUFACTURER);
                crashDetails.setDeviceModel(Constants.PHONE_MODEL);
            }
            if (thread != null && (listener == null || listener.includeThreadDetails())) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(thread.getName());
                stringBuilder.append("-");
                stringBuilder.append(thread.getId());
                crashDetails.setThreadName(stringBuilder.toString());
            }
            String deviceIdentifier = Constants.DEVICE_IDENTIFIER;
            if (deviceIdentifier != null && (listener == null || listener.includeDeviceIdentifier())) {
                crashDetails.setReporterKey(deviceIdentifier);
            }
            crashDetails.writeCrashReport(context);
            if (listener != null) {
                try {
                    String limitedString = limitedString(listener.getUserID());
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append(filename);
                    stringBuilder2.append(".user");
                    writeValueToFile(context, limitedString, stringBuilder2.toString());
                    limitedString = limitedString(listener.getContact());
                    stringBuilder2 = new StringBuilder();
                    stringBuilder2.append(filename);
                    stringBuilder2.append(".contact");
                    writeValueToFile(context, limitedString, stringBuilder2.toString());
                    limitedString = listener.getDescription();
                    stringBuilder2 = new StringBuilder();
                    stringBuilder2.append(filename);
                    stringBuilder2.append(".description");
                    writeValueToFile(context, limitedString, stringBuilder2.toString());
                } catch (Throwable e) {
                    HockeyLog.error("Error saving crash meta data!", e);
                }
            }
        }
    }

    public void uncaughtException(Thread thread, Throwable exception) {
        Context context = CrashManager.weakContext != null ? (Context) CrashManager.weakContext.get() : null;
        if (context != null) {
            if (context.getFilesDir() != null) {
                saveException(exception, thread, this.mCrashManagerListener);
                if (this.mIgnoreDefaultHandler) {
                    Process.killProcess(Process.myPid());
                    System.exit(10);
                    return;
                }
                this.mDefaultExceptionHandler.uncaughtException(thread, exception);
                return;
            }
        }
        this.mDefaultExceptionHandler.uncaughtException(thread, exception);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void writeValueToFile(android.content.Context r4, java.lang.String r5, java.lang.String r6) throws java.io.IOException {
        /*
        r0 = android.text.TextUtils.isEmpty(r5);
        if (r0 == 0) goto L_0x0007;
    L_0x0006:
        return;
    L_0x0007:
        r0 = 0;
        r1 = new java.io.File;	 Catch:{ IOException -> 0x0036 }
        r2 = r4.getFilesDir();	 Catch:{ IOException -> 0x0036 }
        r1.<init>(r2, r6);	 Catch:{ IOException -> 0x0036 }
        r2 = android.text.TextUtils.isEmpty(r5);	 Catch:{ IOException -> 0x0036 }
        if (r2 != 0) goto L_0x002e;
    L_0x0017:
        r2 = android.text.TextUtils.getTrimmedLength(r5);	 Catch:{ IOException -> 0x0036 }
        if (r2 <= 0) goto L_0x002e;
    L_0x001d:
        r2 = new java.io.BufferedWriter;	 Catch:{ IOException -> 0x0036 }
        r3 = new java.io.FileWriter;	 Catch:{ IOException -> 0x0036 }
        r3.<init>(r1);	 Catch:{ IOException -> 0x0036 }
        r2.<init>(r3);	 Catch:{ IOException -> 0x0036 }
        r0 = r2;
        r0.write(r5);	 Catch:{ IOException -> 0x0036 }
        r0.flush();	 Catch:{ IOException -> 0x0036 }
    L_0x002e:
        if (r0 == 0) goto L_0x004e;
    L_0x0030:
        r0.close();
        goto L_0x004e;
    L_0x0034:
        r1 = move-exception;
        goto L_0x004f;
    L_0x0036:
        r1 = move-exception;
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0034 }
        r2.<init>();	 Catch:{ all -> 0x0034 }
        r3 = "Failed to write value to ";
        r2.append(r3);	 Catch:{ all -> 0x0034 }
        r2.append(r6);	 Catch:{ all -> 0x0034 }
        r2 = r2.toString();	 Catch:{ all -> 0x0034 }
        net.hockeyapp.android.utils.HockeyLog.error(r2, r1);	 Catch:{ all -> 0x0034 }
        if (r0 == 0) goto L_0x004e;
    L_0x004d:
        goto L_0x0030;
    L_0x004e:
        return;
    L_0x004f:
        if (r0 == 0) goto L_0x0054;
    L_0x0051:
        r0.close();
    L_0x0054:
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: net.hockeyapp.android.ExceptionHandler.writeValueToFile(android.content.Context, java.lang.String, java.lang.String):void");
    }

    private static String limitedString(String string) {
        if (TextUtils.isEmpty(string) || string.length() <= 255) {
            return string;
        }
        return string.substring(0, 255);
    }
}
