package net.hockeyapp.android.objects;

import android.content.Context;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;
import net.hockeyapp.android.utils.HockeyLog;
import org.json.JSONException;

public class CrashDetails {
    private Date appCrashDate;
    private String appPackage;
    private Date appStartDate;
    private String appVersionCode;
    private String appVersionName;
    private final String crashIdentifier;
    private String deviceManufacturer;
    private String deviceModel;
    private Boolean isXamarinException;
    private String osBuild;
    private String osVersion;
    private String reporterKey;
    private String threadName;
    private String throwableStackTrace;

    public void writeCrashReport(java.io.File r1) throws org.json.JSONException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: net.hockeyapp.android.objects.CrashDetails.writeCrashReport(java.io.File):void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
        /*
        r0 = this;
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = "Writing unhandled exception to: ";
        r0.append(r1);
        r1 = r5.getAbsolutePath();
        r0.append(r1);
        r0 = r0.toString();
        net.hockeyapp.android.utils.HockeyLog.debug(r0);
        r0 = 0;
        r1 = new java.io.BufferedWriter;	 Catch:{ IOException -> 0x00a5 }
        r2 = new java.io.FileWriter;	 Catch:{ IOException -> 0x00a5 }
        r2.<init>(r5);	 Catch:{ IOException -> 0x00a5 }
        r1.<init>(r2);	 Catch:{ IOException -> 0x00a5 }
        r0 = r1;	 Catch:{ IOException -> 0x00a5 }
        r1 = "Package";	 Catch:{ IOException -> 0x00a5 }
        r2 = r4.appPackage;	 Catch:{ IOException -> 0x00a5 }
        r4.writeHeader(r0, r1, r2);	 Catch:{ IOException -> 0x00a5 }
        r1 = "Version Code";	 Catch:{ IOException -> 0x00a5 }
        r2 = r4.appVersionCode;	 Catch:{ IOException -> 0x00a5 }
        r4.writeHeader(r0, r1, r2);	 Catch:{ IOException -> 0x00a5 }
        r1 = "Version Name";	 Catch:{ IOException -> 0x00a5 }
        r2 = r4.appVersionName;	 Catch:{ IOException -> 0x00a5 }
        r4.writeHeader(r0, r1, r2);	 Catch:{ IOException -> 0x00a5 }
        r1 = "Android";	 Catch:{ IOException -> 0x00a5 }
        r2 = r4.osVersion;	 Catch:{ IOException -> 0x00a5 }
        r4.writeHeader(r0, r1, r2);	 Catch:{ IOException -> 0x00a5 }
        r1 = "Android Build";	 Catch:{ IOException -> 0x00a5 }
        r2 = r4.osBuild;	 Catch:{ IOException -> 0x00a5 }
        r4.writeHeader(r0, r1, r2);	 Catch:{ IOException -> 0x00a5 }
        r1 = "Manufacturer";	 Catch:{ IOException -> 0x00a5 }
        r2 = r4.deviceManufacturer;	 Catch:{ IOException -> 0x00a5 }
        r4.writeHeader(r0, r1, r2);	 Catch:{ IOException -> 0x00a5 }
        r1 = "Model";	 Catch:{ IOException -> 0x00a5 }
        r2 = r4.deviceModel;	 Catch:{ IOException -> 0x00a5 }
        r4.writeHeader(r0, r1, r2);	 Catch:{ IOException -> 0x00a5 }
        r1 = "Thread";	 Catch:{ IOException -> 0x00a5 }
        r2 = r4.threadName;	 Catch:{ IOException -> 0x00a5 }
        r4.writeHeader(r0, r1, r2);	 Catch:{ IOException -> 0x00a5 }
        r1 = "CrashReporter Key";	 Catch:{ IOException -> 0x00a5 }
        r2 = r4.reporterKey;	 Catch:{ IOException -> 0x00a5 }
        r4.writeHeader(r0, r1, r2);	 Catch:{ IOException -> 0x00a5 }
        r1 = "Start Date";	 Catch:{ IOException -> 0x00a5 }
        r2 = r4.appStartDate;	 Catch:{ IOException -> 0x00a5 }
        r2 = net.hockeyapp.android.utils.JSONDateUtils.toString(r2);	 Catch:{ IOException -> 0x00a5 }
        r4.writeHeader(r0, r1, r2);	 Catch:{ IOException -> 0x00a5 }
        r1 = "Date";	 Catch:{ IOException -> 0x00a5 }
        r2 = r4.appCrashDate;	 Catch:{ IOException -> 0x00a5 }
        r2 = net.hockeyapp.android.utils.JSONDateUtils.toString(r2);	 Catch:{ IOException -> 0x00a5 }
        r4.writeHeader(r0, r1, r2);	 Catch:{ IOException -> 0x00a5 }
        r1 = r4.isXamarinException;	 Catch:{ IOException -> 0x00a5 }
        r1 = r1.booleanValue();	 Catch:{ IOException -> 0x00a5 }
        if (r1 == 0) goto L_0x0088;	 Catch:{ IOException -> 0x00a5 }
    L_0x0081:
        r1 = "Format";	 Catch:{ IOException -> 0x00a5 }
        r2 = "Xamarin";	 Catch:{ IOException -> 0x00a5 }
        r4.writeHeader(r0, r1, r2);	 Catch:{ IOException -> 0x00a5 }
    L_0x0088:
        r1 = "\n";	 Catch:{ IOException -> 0x00a5 }
        r0.write(r1);	 Catch:{ IOException -> 0x00a5 }
        r1 = r4.throwableStackTrace;	 Catch:{ IOException -> 0x00a5 }
        r0.write(r1);	 Catch:{ IOException -> 0x00a5 }
        r0.flush();	 Catch:{ IOException -> 0x00a5 }
        if (r0 == 0) goto L_0x00a2;
    L_0x0097:
        r0.close();	 Catch:{ IOException -> 0x009b }
        goto L_0x00a2;
    L_0x009b:
        r1 = move-exception;
        r2 = "Error saving crash report!";
        net.hockeyapp.android.utils.HockeyLog.error(r2, r1);
        goto L_0x00b1;
    L_0x00a2:
        goto L_0x00b1;
    L_0x00a3:
        r1 = move-exception;
        goto L_0x00b2;
    L_0x00a5:
        r1 = move-exception;
        r2 = "Error saving crash report!";	 Catch:{ all -> 0x00a3 }
        net.hockeyapp.android.utils.HockeyLog.error(r2, r1);	 Catch:{ all -> 0x00a3 }
        if (r0 == 0) goto L_0x00a2;
        r0.close();	 Catch:{ IOException -> 0x009b }
        goto L_0x00a2;
    L_0x00b1:
        return;
        if (r0 == 0) goto L_0x00c0;
        r0.close();	 Catch:{ IOException -> 0x00b9 }
        goto L_0x00c0;
    L_0x00b9:
        r2 = move-exception;
        r3 = "Error saving crash report!";
        net.hockeyapp.android.utils.HockeyLog.error(r3, r2);
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: net.hockeyapp.android.objects.CrashDetails.writeCrashReport(java.io.File):void");
    }

    public CrashDetails(String crashIdentifier) {
        this.crashIdentifier = crashIdentifier;
        this.isXamarinException = Boolean.valueOf(false);
        this.throwableStackTrace = TtmlNode.ANONYMOUS_REGION_ID;
    }

    public CrashDetails(String crashIdentifier, Throwable throwable) {
        this(crashIdentifier);
        this.isXamarinException = Boolean.valueOf(false);
        Writer stackTraceResult = new StringWriter();
        throwable.printStackTrace(new PrintWriter(stackTraceResult));
        this.throwableStackTrace = stackTraceResult.toString();
    }

    public void writeCrashReport(Context context) {
        File filesDir = context.getFilesDir();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.crashIdentifier);
        stringBuilder.append(".stacktrace");
        try {
            writeCrashReport(new File(filesDir, stringBuilder.toString()));
        } catch (JSONException e) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Could not write crash report with error ");
            stringBuilder.append(e.toString());
            HockeyLog.error(stringBuilder.toString());
        }
    }

    private void writeHeader(Writer writer, String name, String value) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(name);
        stringBuilder.append(": ");
        stringBuilder.append(value);
        stringBuilder.append("\n");
        writer.write(stringBuilder.toString());
    }

    public void setReporterKey(String reporterKey) {
        this.reporterKey = reporterKey;
    }

    public void setAppStartDate(Date appStartDate) {
        this.appStartDate = appStartDate;
    }

    public void setAppCrashDate(Date appCrashDate) {
        this.appCrashDate = appCrashDate;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public void setOsBuild(String osBuild) {
        this.osBuild = osBuild;
    }

    public void setDeviceManufacturer(String deviceManufacturer) {
        this.deviceManufacturer = deviceManufacturer;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public void setAppPackage(String appPackage) {
        this.appPackage = appPackage;
    }

    public void setAppVersionName(String appVersionName) {
        this.appVersionName = appVersionName;
    }

    public void setAppVersionCode(String appVersionCode) {
        this.appVersionCode = appVersionCode;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }
}
