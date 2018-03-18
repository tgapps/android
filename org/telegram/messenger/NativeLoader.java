package org.telegram.messenger;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class NativeLoader {
    private static final String LIB_NAME = "tmessages.28";
    private static final String LIB_SO_NAME = "libtmessages.28.so";
    private static final int LIB_VERSION = 28;
    private static final String LOCALE_LIB_SO_NAME = "libtmessages.28loc.so";
    private static volatile boolean nativeLoaded = false;
    private String crashPath = TtmlNode.ANONYMOUS_REGION_ID;

    private static native void init(String str, boolean z);

    private static File getNativeLibraryDir(Context context) {
        File file = null;
        if (context != null) {
            try {
                file = new File((String) ApplicationInfo.class.getField("nativeLibraryDir").get(context.getApplicationInfo()));
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
        if (file == null) {
            file = new File(context.getApplicationInfo().dataDir, "lib");
        }
        return file.isDirectory() ? file : null;
    }

    @SuppressLint({"UnsafeDynamicallyLoadedCode", "SetWorldReadable"})
    private static boolean loadFromZip(Context context, File destDir, File destLocalFile, String folder) {
        Throwable e;
        Throwable th;
        try {
            for (File file : destDir.listFiles()) {
                file.delete();
            }
        } catch (Throwable e2) {
            FileLog.e(e2);
        }
        ZipFile zipFile = null;
        InputStream stream = null;
        try {
            ZipFile zipFile2 = new ZipFile(context.getApplicationInfo().sourceDir);
            try {
                ZipEntry entry = zipFile2.getEntry("lib/" + folder + "/" + LIB_SO_NAME);
                if (entry == null) {
                    throw new Exception("Unable to find file in apk:lib/" + folder + "/" + LIB_NAME);
                }
                stream = zipFile2.getInputStream(entry);
                OutputStream out = new FileOutputStream(destLocalFile);
                byte[] buf = new byte[4096];
                while (true) {
                    int len = stream.read(buf);
                    if (len <= 0) {
                        break;
                    }
                    Thread.yield();
                    out.write(buf, 0, len);
                }
                out.close();
                destLocalFile.setReadable(true, false);
                destLocalFile.setExecutable(true, false);
                destLocalFile.setWritable(true);
                try {
                    System.load(destLocalFile.getAbsolutePath());
                    nativeLoaded = true;
                } catch (Throwable e22) {
                    FileLog.e(e22);
                }
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (Throwable e222) {
                        FileLog.e(e222);
                    }
                }
                if (zipFile2 != null) {
                    try {
                        zipFile2.close();
                    } catch (Throwable e2222) {
                        FileLog.e(e2222);
                    }
                }
                zipFile = zipFile2;
                return true;
            } catch (Exception e3) {
                e2222 = e3;
                zipFile = zipFile2;
                try {
                    FileLog.e(e2222);
                    if (stream != null) {
                        try {
                            stream.close();
                        } catch (Throwable e22222) {
                            FileLog.e(e22222);
                        }
                    }
                    if (zipFile != null) {
                        try {
                            zipFile.close();
                        } catch (Throwable e222222) {
                            FileLog.e(e222222);
                        }
                    }
                    return false;
                } catch (Throwable th2) {
                    th = th2;
                    if (stream != null) {
                        try {
                            stream.close();
                        } catch (Throwable e2222222) {
                            FileLog.e(e2222222);
                        }
                    }
                    if (zipFile != null) {
                        try {
                            zipFile.close();
                        } catch (Throwable e22222222) {
                            FileLog.e(e22222222);
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                zipFile = zipFile2;
                if (stream != null) {
                    stream.close();
                }
                if (zipFile != null) {
                    zipFile.close();
                }
                throw th;
            }
        } catch (Exception e4) {
            e22222222 = e4;
            FileLog.e(e22222222);
            if (stream != null) {
                stream.close();
            }
            if (zipFile != null) {
                zipFile.close();
            }
            return false;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @android.annotation.SuppressLint({"UnsafeDynamicallyLoadedCode"})
    public static synchronized void initNativeLibs(android.content.Context r9) {
        /*
        r7 = org.telegram.messenger.NativeLoader.class;
        monitor-enter(r7);
        r6 = nativeLoaded;	 Catch:{ all -> 0x00b4 }
        if (r6 == 0) goto L_0x0009;
    L_0x0007:
        monitor-exit(r7);
        return;
    L_0x0009:
        net.hockeyapp.android.Constants.loadFromContext(r9);	 Catch:{ all -> 0x00b4 }
        r6 = "tmessages.28";
        java.lang.System.loadLibrary(r6);	 Catch:{ Error -> 0x0020 }
        r6 = 1;
        nativeLoaded = r6;	 Catch:{ Error -> 0x0020 }
        r6 = org.telegram.messenger.BuildVars.LOGS_ENABLED;	 Catch:{ Error -> 0x0020 }
        if (r6 == 0) goto L_0x0007;
    L_0x0019:
        r6 = "loaded normal lib";
        org.telegram.messenger.FileLog.d(r6);	 Catch:{ Error -> 0x0020 }
        goto L_0x0007;
    L_0x0020:
        r2 = move-exception;
        org.telegram.messenger.FileLog.e(r2);	 Catch:{ Throwable -> 0x0132 }
        r5 = android.os.Build.CPU_ABI;	 Catch:{ Exception -> 0x0129 }
        r6 = android.os.Build.CPU_ABI;	 Catch:{ Exception -> 0x0129 }
        r8 = "x86_64";
        r6 = r6.equalsIgnoreCase(r8);	 Catch:{ Exception -> 0x0129 }
        if (r6 == 0) goto L_0x00b7;
    L_0x0031:
        r3 = "x86_64";
    L_0x0034:
        r6 = "os.arch";
        r4 = java.lang.System.getProperty(r6);	 Catch:{ Throwable -> 0x0132 }
        if (r4 == 0) goto L_0x0049;
    L_0x003d:
        r6 = "686";
        r6 = r4.contains(r6);	 Catch:{ Throwable -> 0x0132 }
        if (r6 == 0) goto L_0x0049;
    L_0x0046:
        r3 = "x86";
    L_0x0049:
        r0 = new java.io.File;	 Catch:{ Throwable -> 0x0132 }
        r6 = r9.getFilesDir();	 Catch:{ Throwable -> 0x0132 }
        r8 = "lib";
        r0.<init>(r6, r8);	 Catch:{ Throwable -> 0x0132 }
        r0.mkdirs();	 Catch:{ Throwable -> 0x0132 }
        r1 = new java.io.File;	 Catch:{ Throwable -> 0x0132 }
        r6 = "libtmessages.28loc.so";
        r1.<init>(r0, r6);	 Catch:{ Throwable -> 0x0132 }
        r6 = r1.exists();	 Catch:{ Throwable -> 0x0132 }
        if (r6 == 0) goto L_0x0082;
    L_0x0066:
        r6 = org.telegram.messenger.BuildVars.LOGS_ENABLED;	 Catch:{ Error -> 0x007b }
        if (r6 == 0) goto L_0x0070;
    L_0x006a:
        r6 = "Load local lib";
        org.telegram.messenger.FileLog.d(r6);	 Catch:{ Error -> 0x007b }
    L_0x0070:
        r6 = r1.getAbsolutePath();	 Catch:{ Error -> 0x007b }
        java.lang.System.load(r6);	 Catch:{ Error -> 0x007b }
        r6 = 1;
        nativeLoaded = r6;	 Catch:{ Error -> 0x007b }
        goto L_0x0007;
    L_0x007b:
        r2 = move-exception;
        org.telegram.messenger.FileLog.e(r2);	 Catch:{ Throwable -> 0x0132 }
        r1.delete();	 Catch:{ Throwable -> 0x0132 }
    L_0x0082:
        r6 = org.telegram.messenger.BuildVars.LOGS_ENABLED;	 Catch:{ Throwable -> 0x0132 }
        if (r6 == 0) goto L_0x009d;
    L_0x0086:
        r6 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x0132 }
        r6.<init>();	 Catch:{ Throwable -> 0x0132 }
        r8 = "Library not found, arch = ";
        r6 = r6.append(r8);	 Catch:{ Throwable -> 0x0132 }
        r6 = r6.append(r3);	 Catch:{ Throwable -> 0x0132 }
        r6 = r6.toString();	 Catch:{ Throwable -> 0x0132 }
        org.telegram.messenger.FileLog.e(r6);	 Catch:{ Throwable -> 0x0132 }
    L_0x009d:
        r6 = loadFromZip(r9, r0, r1, r3);	 Catch:{ Throwable -> 0x0132 }
        if (r6 != 0) goto L_0x0007;
    L_0x00a3:
        r6 = "tmessages.28";
        java.lang.System.loadLibrary(r6);	 Catch:{ Error -> 0x00ae }
        r6 = 1;
        nativeLoaded = r6;	 Catch:{ Error -> 0x00ae }
        goto L_0x0007;
    L_0x00ae:
        r2 = move-exception;
        org.telegram.messenger.FileLog.e(r2);	 Catch:{ all -> 0x00b4 }
        goto L_0x0007;
    L_0x00b4:
        r6 = move-exception;
        monitor-exit(r7);
        throw r6;
    L_0x00b7:
        r6 = android.os.Build.CPU_ABI;	 Catch:{ Exception -> 0x0129 }
        r8 = "arm64-v8a";
        r6 = r6.equalsIgnoreCase(r8);	 Catch:{ Exception -> 0x0129 }
        if (r6 == 0) goto L_0x00c7;
    L_0x00c2:
        r3 = "arm64-v8a";
        goto L_0x0034;
    L_0x00c7:
        r6 = android.os.Build.CPU_ABI;	 Catch:{ Exception -> 0x0129 }
        r8 = "armeabi-v7a";
        r6 = r6.equalsIgnoreCase(r8);	 Catch:{ Exception -> 0x0129 }
        if (r6 == 0) goto L_0x00d7;
    L_0x00d2:
        r3 = "armeabi-v7a";
        goto L_0x0034;
    L_0x00d7:
        r6 = android.os.Build.CPU_ABI;	 Catch:{ Exception -> 0x0129 }
        r8 = "armeabi";
        r6 = r6.equalsIgnoreCase(r8);	 Catch:{ Exception -> 0x0129 }
        if (r6 == 0) goto L_0x00e7;
    L_0x00e2:
        r3 = "armeabi";
        goto L_0x0034;
    L_0x00e7:
        r6 = android.os.Build.CPU_ABI;	 Catch:{ Exception -> 0x0129 }
        r8 = "x86";
        r6 = r6.equalsIgnoreCase(r8);	 Catch:{ Exception -> 0x0129 }
        if (r6 == 0) goto L_0x00f7;
    L_0x00f2:
        r3 = "x86";
        goto L_0x0034;
    L_0x00f7:
        r6 = android.os.Build.CPU_ABI;	 Catch:{ Exception -> 0x0129 }
        r8 = "mips";
        r6 = r6.equalsIgnoreCase(r8);	 Catch:{ Exception -> 0x0129 }
        if (r6 == 0) goto L_0x0107;
    L_0x0102:
        r3 = "mips";
        goto L_0x0034;
    L_0x0107:
        r3 = "armeabi";
        r6 = org.telegram.messenger.BuildVars.LOGS_ENABLED;	 Catch:{ Exception -> 0x0129 }
        if (r6 == 0) goto L_0x0034;
    L_0x010e:
        r6 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0129 }
        r6.<init>();	 Catch:{ Exception -> 0x0129 }
        r8 = "Unsupported arch: ";
        r6 = r6.append(r8);	 Catch:{ Exception -> 0x0129 }
        r8 = android.os.Build.CPU_ABI;	 Catch:{ Exception -> 0x0129 }
        r6 = r6.append(r8);	 Catch:{ Exception -> 0x0129 }
        r6 = r6.toString();	 Catch:{ Exception -> 0x0129 }
        org.telegram.messenger.FileLog.e(r6);	 Catch:{ Exception -> 0x0129 }
        goto L_0x0034;
    L_0x0129:
        r2 = move-exception;
        org.telegram.messenger.FileLog.e(r2);	 Catch:{ Throwable -> 0x0132 }
        r3 = "armeabi";
        goto L_0x0034;
    L_0x0132:
        r2 = move-exception;
        r2.printStackTrace();	 Catch:{ all -> 0x00b4 }
        goto L_0x00a3;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.NativeLoader.initNativeLibs(android.content.Context):void");
    }
}
