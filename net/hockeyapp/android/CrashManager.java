package net.hockeyapp.android;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import net.hockeyapp.android.objects.CrashManagerUserInput;
import net.hockeyapp.android.objects.CrashMetaData;
import net.hockeyapp.android.utils.AsyncTaskUtils;
import net.hockeyapp.android.utils.HockeyLog;
import net.hockeyapp.android.utils.HttpURLConnectionBuilder;
import net.hockeyapp.android.utils.Util;

public class CrashManager {
    private static final FilenameFilter STACK_TRACES_FILTER = new FilenameFilter() {
        public boolean accept(File dir, String filename) {
            return filename.endsWith(".stacktrace");
        }
    };
    private static boolean didCrashInLastSession = false;
    private static String identifier = null;
    private static long initializeTimestamp;
    static CountDownLatch latch = new CountDownLatch(1);
    static int stackTracesCount = 0;
    private static String urlString = null;
    static WeakReference<Context> weakContext;

    public static void deleteStackTraces(java.lang.ref.WeakReference<android.content.Context> r1) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: net.hockeyapp.android.CrashManager.deleteStackTraces(java.lang.ref.WeakReference):void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
        /*
        r0 = searchForStackTraces(r6);
        if (r0 == 0) goto L_0x0053;
    L_0x0006:
        r1 = r0.length;
        if (r1 <= 0) goto L_0x0053;
    L_0x0009:
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Found ";
        r1.append(r2);
        r2 = r0.length;
        r1.append(r2);
        r2 = " stacktrace(s).";
        r1.append(r2);
        r1 = r1.toString();
        net.hockeyapp.android.utils.HockeyLog.debug(r1);
        r1 = r0.length;
        r2 = 0;
        if (r2 >= r1) goto L_0x0053;
    L_0x0027:
        r3 = r0[r2];
        if (r6 == 0) goto L_0x004f;
    L_0x002b:
        r4 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0048 }
        r4.<init>();	 Catch:{ Exception -> 0x0048 }
        r5 = "Delete stacktrace ";	 Catch:{ Exception -> 0x0048 }
        r4.append(r5);	 Catch:{ Exception -> 0x0048 }
        r4.append(r3);	 Catch:{ Exception -> 0x0048 }
        r5 = ".";	 Catch:{ Exception -> 0x0048 }
        r4.append(r5);	 Catch:{ Exception -> 0x0048 }
        r4 = r4.toString();	 Catch:{ Exception -> 0x0048 }
        net.hockeyapp.android.utils.HockeyLog.debug(r4);	 Catch:{ Exception -> 0x0048 }
        deleteStackTrace(r6, r3);	 Catch:{ Exception -> 0x0048 }
        goto L_0x004f;
    L_0x0048:
        r4 = move-exception;
        r5 = "Failed to delete stacktrace";
        net.hockeyapp.android.utils.HockeyLog.error(r5, r4);
        goto L_0x0050;
        r2 = r2 + 1;
        goto L_0x0025;
    L_0x0053:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: net.hockeyapp.android.CrashManager.deleteStackTraces(java.lang.ref.WeakReference):void");
    }

    public static int hasStackTraces(java.lang.ref.WeakReference<android.content.Context> r1) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: net.hockeyapp.android.CrashManager.hasStackTraces(java.lang.ref.WeakReference):int
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
        /*
        r0 = searchForStackTraces(r8);
        r1 = 0;
        r2 = 0;
        if (r0 == 0) goto L_0x004a;
    L_0x0008:
        r3 = r0.length;
        if (r3 <= 0) goto L_0x004a;
    L_0x000b:
        r3 = 0;
        if (r8 == 0) goto L_0x0017;
    L_0x000e:
        r4 = r8.get();	 Catch:{ Exception -> 0x0015 }
        r4 = (android.content.Context) r4;	 Catch:{ Exception -> 0x0015 }
        goto L_0x0018;	 Catch:{ Exception -> 0x0015 }
    L_0x0015:
        r4 = move-exception;	 Catch:{ Exception -> 0x0015 }
        goto L_0x0034;	 Catch:{ Exception -> 0x0015 }
    L_0x0017:
        r4 = 0;	 Catch:{ Exception -> 0x0015 }
    L_0x0018:
        if (r4 == 0) goto L_0x0035;	 Catch:{ Exception -> 0x0015 }
    L_0x001a:
        r5 = "HockeySDK";	 Catch:{ Exception -> 0x0015 }
        r5 = r4.getSharedPreferences(r5, r3);	 Catch:{ Exception -> 0x0015 }
        r6 = "ConfirmedFilenames";	 Catch:{ Exception -> 0x0015 }
        r7 = "";	 Catch:{ Exception -> 0x0015 }
        r6 = r5.getString(r6, r7);	 Catch:{ Exception -> 0x0015 }
        r7 = "\\|";	 Catch:{ Exception -> 0x0015 }
        r6 = r6.split(r7);	 Catch:{ Exception -> 0x0015 }
        r6 = java.util.Arrays.asList(r6);	 Catch:{ Exception -> 0x0015 }
        r1 = r6;
        goto L_0x0035;
    L_0x0034:
        goto L_0x0036;
        if (r1 == 0) goto L_0x0049;
        r2 = 2;
        r4 = r0.length;
        if (r3 >= r4) goto L_0x004a;
        r5 = r0[r3];
        r6 = r1.contains(r5);
        if (r6 != 0) goto L_0x0046;
        r2 = 1;
        goto L_0x004a;
        r3 = r3 + 1;
        goto L_0x003a;
        r2 = 1;
    L_0x004a:
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: net.hockeyapp.android.CrashManager.hasStackTraces(java.lang.ref.WeakReference):int");
    }

    public static void register(Context context, String appIdentifier, CrashManagerListener listener) {
        register(context, "https://sdk.hockeyapp.net/", appIdentifier, listener);
    }

    public static void register(Context context, String urlString, String appIdentifier, CrashManagerListener listener) {
        initialize(context, urlString, appIdentifier, listener, false);
        execute(context, listener);
    }

    @SuppressLint({"StaticFieldLeak"})
    public static void execute(Context context, final CrashManagerListener listener) {
        final WeakReference<Context> weakContext = new WeakReference(context);
        AsyncTaskUtils.execute(new AsyncTask<Void, Object, Integer>() {
            private boolean autoSend = null;

            protected Integer doInBackground(Void... voids) {
                Context context = (Context) weakContext.get();
                boolean z = false;
                if (context != null) {
                    this.autoSend |= PreferenceManager.getDefaultSharedPreferences(context).getBoolean("always_send_crash_reports", false);
                }
                int foundOrSend = CrashManager.hasStackTraces(weakContext);
                if (foundOrSend == 1) {
                    z = true;
                }
                CrashManager.didCrashInLastSession = z;
                CrashManager.latch.countDown();
                return Integer.valueOf(foundOrSend);
            }

            protected void onPostExecute(Integer foundOrSend) {
                boolean autoSend = this.autoSend;
                boolean ignoreDefaultHandler = listener != null && listener.ignoreDefaultHandler();
                if (foundOrSend.intValue() == 1) {
                    if (listener != null) {
                        autoSend |= listener.shouldAutoUploadCrashes();
                        listener.onNewCrashesFound();
                    }
                    if (autoSend || !CrashManager.showDialog(weakContext, listener, ignoreDefaultHandler)) {
                        CrashManager.sendCrashes(weakContext, listener, ignoreDefaultHandler, null);
                    }
                } else if (foundOrSend.intValue() == 2) {
                    if (listener != null) {
                        listener.onConfirmedCrashesFound();
                    }
                    CrashManager.sendCrashes(weakContext, listener, ignoreDefaultHandler, null);
                } else if (foundOrSend.intValue() == 0) {
                    if (listener != null) {
                        listener.onNoCrashesFound();
                    }
                    CrashManager.registerHandler(listener, ignoreDefaultHandler);
                }
            }
        });
    }

    private static void submitStackTrace(WeakReference<Context> weakContext, String filename, CrashManagerListener listener, CrashMetaData crashMetaData) {
        Boolean successful = null;
        Boolean successful2 = Boolean.valueOf(false);
        HttpURLConnection urlConnection = null;
        try {
            String stacktrace = contentsOfFile(weakContext, filename);
            if (stacktrace.length() > 0) {
                String crashMetaDataUserID;
                String crashMetaDataContact;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Transmitting crash data: \n");
                stringBuilder.append(stacktrace);
                HockeyLog.debug(stringBuilder.toString());
                String userID = contentsOfFile(weakContext, filename.replace(".stacktrace", ".user"));
                String contact = contentsOfFile(weakContext, filename.replace(".stacktrace", ".contact"));
                if (crashMetaData != null) {
                    crashMetaDataUserID = crashMetaData.getUserID();
                    if (!TextUtils.isEmpty(crashMetaDataUserID)) {
                        userID = crashMetaDataUserID;
                    }
                    crashMetaDataContact = crashMetaData.getUserEmail();
                    if (!TextUtils.isEmpty(crashMetaDataContact)) {
                        contact = crashMetaDataContact;
                    }
                }
                crashMetaDataUserID = contentsOfFile(weakContext, filename.replace(".stacktrace", ".description"));
                crashMetaDataContact = crashMetaData != null ? crashMetaData.getUserDescription() : TtmlNode.ANONYMOUS_REGION_ID;
                if (!TextUtils.isEmpty(crashMetaDataUserID)) {
                    if (TextUtils.isEmpty(crashMetaDataContact)) {
                        crashMetaDataContact = String.format("Log:\n%s", new Object[]{crashMetaDataUserID});
                    } else {
                        crashMetaDataContact = String.format("%s\n\nLog:\n%s", new Object[]{crashMetaDataContact, crashMetaDataUserID});
                    }
                }
                Map<String, String> parameters = new HashMap();
                parameters.put("raw", stacktrace);
                parameters.put("userID", userID);
                parameters.put("contact", contact);
                parameters.put("description", crashMetaDataContact);
                parameters.put("sdk", "HockeySDK");
                parameters.put("sdk_version", "5.0.4");
                urlConnection = new HttpURLConnectionBuilder(getURLString()).setRequestMethod("POST").writeFormFields(parameters).build();
                int responseCode = urlConnection.getResponseCode();
                if (responseCode != 202) {
                    if (responseCode != 201) {
                        successful2 = Boolean.valueOf(successful);
                    }
                }
                successful = 1;
                successful2 = Boolean.valueOf(successful);
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (successful2.booleanValue()) {
                HockeyLog.debug("Transmission succeeded");
                deleteStackTrace(weakContext, filename);
                if (listener == null) {
                    return;
                }
                listener.onCrashesSent();
                deleteRetryCounter(weakContext, filename);
                return;
            }
            HockeyLog.debug("Transmission failed, will retry on next register() call");
            if (listener == null) {
                return;
            }
            listener.onCrashesNotSent();
            updateRetryCounter(weakContext, filename, listener.getMaxRetryAttempts());
        } catch (Throwable e) {
            HockeyLog.error("Failed to transmit crash data", e);
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (successful2.booleanValue()) {
                HockeyLog.debug("Transmission succeeded");
                deleteStackTrace(weakContext, filename);
                if (listener == null) {
                }
            } else {
                HockeyLog.debug("Transmission failed, will retry on next register() call");
                if (listener == null) {
                }
            }
        } catch (Throwable th) {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (successful2.booleanValue()) {
                HockeyLog.debug("Transmission succeeded");
                deleteStackTrace(weakContext, filename);
                if (listener != null) {
                    listener.onCrashesSent();
                    deleteRetryCounter(weakContext, filename);
                }
            } else {
                HockeyLog.debug("Transmission failed, will retry on next register() call");
                if (listener != null) {
                    listener.onCrashesNotSent();
                    updateRetryCounter(weakContext, filename, listener.getMaxRetryAttempts());
                }
            }
        }
    }

    @SuppressLint({"StaticFieldLeak"})
    public static boolean handleUserInput(CrashManagerUserInput userInput, CrashMetaData userProvidedMetaData, CrashManagerListener listener, final WeakReference<Context> weakContext, boolean ignoreDefaultHandler) {
        switch (userInput) {
            case CrashManagerUserInputDontSend:
                if (listener != null) {
                    listener.onUserDeniedCrashes();
                }
                registerHandler(listener, ignoreDefaultHandler);
                AsyncTaskUtils.execute(new AsyncTask<Void, Object, Object>() {
                    protected Object doInBackground(Void... voids) {
                        CrashManager.deleteStackTraces(weakContext);
                        return null;
                    }
                });
                return true;
            case CrashManagerUserInputAlwaysSend:
                Context context = weakContext != null ? (Context) weakContext.get() : null;
                if (context == null) {
                    return false;
                }
                PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean("always_send_crash_reports", true).apply();
                sendCrashes(weakContext, listener, ignoreDefaultHandler, userProvidedMetaData);
                return true;
            case CrashManagerUserInputSend:
                sendCrashes(weakContext, listener, ignoreDefaultHandler, userProvidedMetaData);
                return true;
            default:
                return false;
        }
    }

    private static void initialize(Context context, String urlString, String appIdentifier, CrashManagerListener listener, boolean registerHandler) {
        if (context != null) {
            if (initializeTimestamp == 0) {
                initializeTimestamp = System.currentTimeMillis();
            }
            urlString = urlString;
            identifier = Util.sanitizeAppIdentifier(appIdentifier);
            boolean ignoreDefaultHandler = false;
            didCrashInLastSession = false;
            weakContext = new WeakReference(context);
            Constants.loadFromContext(context);
            if (identifier == null) {
                identifier = Constants.APP_PACKAGE;
            }
            if (registerHandler) {
                if (listener != null && listener.ignoreDefaultHandler()) {
                    ignoreDefaultHandler = true;
                }
                registerHandler(listener, ignoreDefaultHandler);
            }
        }
    }

    private static boolean showDialog(final WeakReference<Context> weakContext, final CrashManagerListener listener, final boolean ignoreDefaultHandler) {
        if (listener != null && listener.onHandleAlertView()) {
            return true;
        }
        Context context = weakContext != null ? (Context) weakContext.get() : null;
        if (context != null) {
            if (context instanceof Activity) {
                Builder builder = new Builder(context);
                builder.setTitle(getAlertTitle(context));
                builder.setMessage(R.string.hockeyapp_crash_dialog_message);
                builder.setNegativeButton(R.string.hockeyapp_crash_dialog_negative_button, new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        CrashManager.handleUserInput(CrashManagerUserInput.CrashManagerUserInputDontSend, null, listener, weakContext, ignoreDefaultHandler);
                    }
                });
                builder.setNeutralButton(R.string.hockeyapp_crash_dialog_neutral_button, new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        CrashManager.handleUserInput(CrashManagerUserInput.CrashManagerUserInputAlwaysSend, null, listener, weakContext, ignoreDefaultHandler);
                    }
                });
                builder.setPositiveButton(R.string.hockeyapp_crash_dialog_positive_button, new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        CrashManager.handleUserInput(CrashManagerUserInput.CrashManagerUserInputSend, null, listener, weakContext, ignoreDefaultHandler);
                    }
                });
                builder.create().show();
                return true;
            }
        }
        return false;
    }

    private static String getAlertTitle(Context context) {
        return context.getString(R.string.hockeyapp_crash_dialog_title, new Object[]{Util.getAppName(context)});
    }

    @SuppressLint({"StaticFieldLeak"})
    private static void sendCrashes(final WeakReference<Context> weakContext, final CrashManagerListener listener, boolean ignoreDefaultHandler, final CrashMetaData crashMetaData) {
        registerHandler(listener, ignoreDefaultHandler);
        Context context = weakContext != null ? (Context) weakContext.get() : null;
        final boolean isConnectedToNetwork = context != null && Util.isConnectedToNetwork(context);
        if (!(isConnectedToNetwork || listener == null)) {
            listener.onCrashesNotSent();
        }
        AsyncTaskUtils.execute(new AsyncTask<Void, Object, Object>() {
            protected Object doInBackground(Void... voids) {
                String[] list = CrashManager.searchForStackTraces(weakContext);
                if (list != null && list.length > 0) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Found ");
                    stringBuilder.append(list.length);
                    stringBuilder.append(" stacktrace(s).");
                    HockeyLog.debug(stringBuilder.toString());
                    if (list.length > 100) {
                        CrashManager.deleteRedundantStackTraces(weakContext);
                        list = CrashManager.searchForStackTraces(weakContext);
                        if (list == null) {
                            return null;
                        }
                    }
                    CrashManager.saveConfirmedStackTraces(weakContext, list);
                    if (isConnectedToNetwork) {
                        for (String file : list) {
                            CrashManager.submitStackTrace(weakContext, file, listener, crashMetaData);
                        }
                    }
                }
                return null;
            }
        });
    }

    private static void registerHandler(CrashManagerListener listener, boolean ignoreDefaultHandler) {
        if (TextUtils.isEmpty(Constants.APP_VERSION) || TextUtils.isEmpty(Constants.APP_PACKAGE)) {
            HockeyLog.debug("Exception handler not set because version or package is null.");
            return;
        }
        UncaughtExceptionHandler currentHandler = Thread.getDefaultUncaughtExceptionHandler();
        if (currentHandler != null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Current handler class = ");
            stringBuilder.append(currentHandler.getClass().getName());
            HockeyLog.debug(stringBuilder.toString());
        }
        if (currentHandler instanceof ExceptionHandler) {
            ((ExceptionHandler) currentHandler).setListener(listener);
        } else {
            Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(currentHandler, listener, ignoreDefaultHandler));
        }
    }

    private static String getURLString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(urlString);
        stringBuilder.append("api/2/apps/");
        stringBuilder.append(identifier);
        stringBuilder.append("/crashes/");
        return stringBuilder.toString();
    }

    private static void updateRetryCounter(WeakReference<Context> weakContext, String filename, int maxRetryAttempts) {
        if (maxRetryAttempts != -1) {
            Context context = weakContext != null ? (Context) weakContext.get() : null;
            if (context != null) {
                SharedPreferences preferences = context.getSharedPreferences("HockeySDK", 0);
                Editor editor = preferences.edit();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("RETRY_COUNT: ");
                stringBuilder.append(filename);
                int retryCounter = preferences.getInt(stringBuilder.toString(), 0);
                if (retryCounter >= maxRetryAttempts) {
                    deleteStackTrace(weakContext, filename);
                    deleteRetryCounter(weakContext, filename);
                } else {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("RETRY_COUNT: ");
                    stringBuilder.append(filename);
                    editor.putInt(stringBuilder.toString(), retryCounter + 1);
                    editor.apply();
                }
            }
        }
    }

    private static void deleteRetryCounter(WeakReference<Context> weakContext, String filename) {
        Context context = weakContext != null ? (Context) weakContext.get() : null;
        if (context != null) {
            Editor editor = context.getSharedPreferences("HockeySDK", 0).edit();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("RETRY_COUNT: ");
            stringBuilder.append(filename);
            editor.remove(stringBuilder.toString());
            editor.apply();
        }
    }

    private static void deleteStackTrace(WeakReference<Context> weakContext, String filename) {
        Context context = weakContext != null ? (Context) weakContext.get() : null;
        if (context != null) {
            context.deleteFile(filename);
            context.deleteFile(filename.replace(".stacktrace", ".user"));
            context.deleteFile(filename.replace(".stacktrace", ".contact"));
            context.deleteFile(filename.replace(".stacktrace", ".description"));
            stackTracesCount--;
        }
    }

    private static String contentsOfFile(WeakReference<Context> weakContext, String filename) {
        BufferedReader reader = null;
        Context context = weakContext != null ? (Context) weakContext.get() : reader;
        if (context == null) {
            return TtmlNode.ANONYMOUS_REGION_ID;
        }
        File file = context.getFileStreamPath(filename);
        if (file != null) {
            if (file.exists()) {
                StringBuilder contents = new StringBuilder();
                try {
                    reader = new BufferedReader(new InputStreamReader(context.openFileInput(filename)));
                    while (true) {
                        String readLine = reader.readLine();
                        String line = readLine;
                        if (readLine == null) {
                            break;
                        }
                        contents.append(line);
                        contents.append(System.getProperty("line.separator"));
                    }
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                        }
                    }
                } catch (Throwable e2) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Failed to read content of ");
                    stringBuilder.append(filename);
                    HockeyLog.error(stringBuilder.toString(), e2);
                    if (reader != null) {
                        reader.close();
                    }
                } catch (Throwable th) {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e3) {
                        }
                    }
                }
                return contents.toString();
            }
        }
        return TtmlNode.ANONYMOUS_REGION_ID;
    }

    private static void saveConfirmedStackTraces(WeakReference<Context> weakContext, String[] stackTraces) {
        Context context = weakContext != null ? (Context) weakContext.get() : null;
        if (context != null) {
            try {
                Editor editor = context.getSharedPreferences("HockeySDK", 0).edit();
                editor.putString("ConfirmedFilenames", TextUtils.join(",", stackTraces));
                editor.apply();
            } catch (Exception e) {
            }
        }
    }

    static String[] searchForStackTraces(WeakReference<Context> weakContext) {
        Context context = weakContext != null ? (Context) weakContext.get() : null;
        if (context != null) {
            File dir = context.getFilesDir();
            if (dir != null) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Looking for exceptions in: ");
                stringBuilder.append(dir.getAbsolutePath());
                HockeyLog.debug(stringBuilder.toString());
                int i = 0;
                if (!dir.exists() && !dir.mkdir()) {
                    return new String[0];
                }
                String[] list = dir.list(STACK_TRACES_FILTER);
                if (list != null) {
                    i = list.length;
                }
                stackTracesCount = i;
                return list;
            }
            HockeyLog.debug("Can't search for exception as file path is null.");
        }
        return null;
    }

    private static void deleteRedundantStackTraces(WeakReference<Context> weakContext) {
        Context context = weakContext != null ? (Context) weakContext.get() : null;
        if (context != null) {
            File dir = context.getFilesDir();
            if (dir != null) {
                if (dir.exists()) {
                    File[] files = dir.listFiles(STACK_TRACES_FILTER);
                    if (files.length > 100) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Delete ");
                        stringBuilder.append(files.length - 100);
                        stringBuilder.append(" redundant stacktrace(s).");
                        HockeyLog.debug(stringBuilder.toString());
                        Arrays.sort(files, new Comparator<File>() {
                            public int compare(File file1, File file2) {
                                return Long.valueOf(file1.lastModified()).compareTo(Long.valueOf(file2.lastModified()));
                            }
                        });
                        for (int i = 0; i < files.length - 100; i++) {
                            deleteStackTrace(weakContext, files[i].getName());
                        }
                    }
                }
            }
        }
    }

    public static long getInitializeTimestamp() {
        return initializeTimestamp;
    }
}
