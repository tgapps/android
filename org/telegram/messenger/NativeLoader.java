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
    private static final String LIB_NAME = "tmessages.27";
    private static final String LIB_SO_NAME = "libtmessages.27.so";
    private static final int LIB_VERSION = 27;
    private static final String LOCALE_LIB_SO_NAME = "libtmessages.27loc.so";
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
    public static synchronized void initNativeLibs(android.content.Context r10) {
        /*
        r8 = org.telegram.messenger.NativeLoader.class;
        monitor-enter(r8);
        r7 = nativeLoaded;	 Catch:{ all -> 0x00c8 }
        if (r7 == 0) goto L_0x0009;
    L_0x0007:
        monitor-exit(r8);
        return;
    L_0x0009:
        net.hockeyapp.android.Constants.loadFromContext(r10);	 Catch:{ all -> 0x00c8 }
        r7 = android.os.Build.CPU_ABI;	 Catch:{ Exception -> 0x011d }
        r9 = "armeabi-v7a";
        r7 = r7.equalsIgnoreCase(r9);	 Catch:{ Exception -> 0x011d }
        if (r7 == 0) goto L_0x00cb;
    L_0x0017:
        r5 = "armeabi-v7a";
    L_0x001a:
        r7 = "os.arch";
        r6 = java.lang.System.getProperty(r7);	 Catch:{ Throwable -> 0x0126 }
        if (r6 == 0) goto L_0x002f;
    L_0x0023:
        r7 = "686";
        r7 = r6.contains(r7);	 Catch:{ Throwable -> 0x0126 }
        if (r7 == 0) goto L_0x002f;
    L_0x002c:
        r5 = "x86";
    L_0x002f:
        r1 = getNativeLibraryDir(r10);	 Catch:{ Throwable -> 0x0126 }
        if (r1 == 0) goto L_0x005c;
    L_0x0035:
        r2 = new java.io.File;	 Catch:{ Throwable -> 0x0126 }
        r7 = "libtmessages.27.so";
        r2.<init>(r1, r7);	 Catch:{ Throwable -> 0x0126 }
        r7 = r2.exists();	 Catch:{ Throwable -> 0x0126 }
        if (r7 == 0) goto L_0x005b;
    L_0x0043:
        r7 = org.telegram.messenger.BuildVars.LOGS_ENABLED;	 Catch:{ Throwable -> 0x0126 }
        if (r7 == 0) goto L_0x004d;
    L_0x0047:
        r7 = "load normal lib";
        org.telegram.messenger.FileLog.d(r7);	 Catch:{ Throwable -> 0x0126 }
    L_0x004d:
        r7 = "tmessages.27";
        java.lang.System.loadLibrary(r7);	 Catch:{ Error -> 0x0057 }
        r7 = 1;
        nativeLoaded = r7;	 Catch:{ Error -> 0x0057 }
        goto L_0x0007;
    L_0x0057:
        r4 = move-exception;
        org.telegram.messenger.FileLog.e(r4);	 Catch:{ Throwable -> 0x0126 }
    L_0x005b:
        r1 = r2;
    L_0x005c:
        r0 = new java.io.File;	 Catch:{ Throwable -> 0x0126 }
        r7 = r10.getFilesDir();	 Catch:{ Throwable -> 0x0126 }
        r9 = "lib";
        r0.<init>(r7, r9);	 Catch:{ Throwable -> 0x0126 }
        r0.mkdirs();	 Catch:{ Throwable -> 0x0126 }
        r3 = new java.io.File;	 Catch:{ Throwable -> 0x0126 }
        r7 = "libtmessages.27loc.so";
        r3.<init>(r0, r7);	 Catch:{ Throwable -> 0x0126 }
        r7 = r3.exists();	 Catch:{ Throwable -> 0x0126 }
        if (r7 == 0) goto L_0x0096;
    L_0x0079:
        r7 = org.telegram.messenger.BuildVars.LOGS_ENABLED;	 Catch:{ Error -> 0x008f }
        if (r7 == 0) goto L_0x0083;
    L_0x007d:
        r7 = "Load local lib";
        org.telegram.messenger.FileLog.d(r7);	 Catch:{ Error -> 0x008f }
    L_0x0083:
        r7 = r3.getAbsolutePath();	 Catch:{ Error -> 0x008f }
        java.lang.System.load(r7);	 Catch:{ Error -> 0x008f }
        r7 = 1;
        nativeLoaded = r7;	 Catch:{ Error -> 0x008f }
        goto L_0x0007;
    L_0x008f:
        r4 = move-exception;
        org.telegram.messenger.FileLog.e(r4);	 Catch:{ Throwable -> 0x0126 }
        r3.delete();	 Catch:{ Throwable -> 0x0126 }
    L_0x0096:
        r7 = org.telegram.messenger.BuildVars.LOGS_ENABLED;	 Catch:{ Throwable -> 0x0126 }
        if (r7 == 0) goto L_0x00b1;
    L_0x009a:
        r7 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x0126 }
        r7.<init>();	 Catch:{ Throwable -> 0x0126 }
        r9 = "Library not found, arch = ";
        r7 = r7.append(r9);	 Catch:{ Throwable -> 0x0126 }
        r7 = r7.append(r5);	 Catch:{ Throwable -> 0x0126 }
        r7 = r7.toString();	 Catch:{ Throwable -> 0x0126 }
        org.telegram.messenger.FileLog.e(r7);	 Catch:{ Throwable -> 0x0126 }
    L_0x00b1:
        r7 = loadFromZip(r10, r0, r3, r5);	 Catch:{ Throwable -> 0x0126 }
        if (r7 != 0) goto L_0x0007;
    L_0x00b7:
        r7 = "tmessages.27";
        java.lang.System.loadLibrary(r7);	 Catch:{ Error -> 0x00c2 }
        r7 = 1;
        nativeLoaded = r7;	 Catch:{ Error -> 0x00c2 }
        goto L_0x0007;
    L_0x00c2:
        r4 = move-exception;
        org.telegram.messenger.FileLog.e(r4);	 Catch:{ all -> 0x00c8 }
        goto L_0x0007;
    L_0x00c8:
        r7 = move-exception;
        monitor-exit(r8);
        throw r7;
    L_0x00cb:
        r7 = android.os.Build.CPU_ABI;	 Catch:{ Exception -> 0x011d }
        r9 = "armeabi";
        r7 = r7.equalsIgnoreCase(r9);	 Catch:{ Exception -> 0x011d }
        if (r7 == 0) goto L_0x00db;
    L_0x00d6:
        r5 = "armeabi";
        goto L_0x001a;
    L_0x00db:
        r7 = android.os.Build.CPU_ABI;	 Catch:{ Exception -> 0x011d }
        r9 = "x86";
        r7 = r7.equalsIgnoreCase(r9);	 Catch:{ Exception -> 0x011d }
        if (r7 == 0) goto L_0x00eb;
    L_0x00e6:
        r5 = "x86";
        goto L_0x001a;
    L_0x00eb:
        r7 = android.os.Build.CPU_ABI;	 Catch:{ Exception -> 0x011d }
        r9 = "mips";
        r7 = r7.equalsIgnoreCase(r9);	 Catch:{ Exception -> 0x011d }
        if (r7 == 0) goto L_0x00fb;
    L_0x00f6:
        r5 = "mips";
        goto L_0x001a;
    L_0x00fb:
        r5 = "armeabi";
        r7 = org.telegram.messenger.BuildVars.LOGS_ENABLED;	 Catch:{ Exception -> 0x011d }
        if (r7 == 0) goto L_0x001a;
    L_0x0102:
        r7 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x011d }
        r7.<init>();	 Catch:{ Exception -> 0x011d }
        r9 = "Unsupported arch: ";
        r7 = r7.append(r9);	 Catch:{ Exception -> 0x011d }
        r9 = android.os.Build.CPU_ABI;	 Catch:{ Exception -> 0x011d }
        r7 = r7.append(r9);	 Catch:{ Exception -> 0x011d }
        r7 = r7.toString();	 Catch:{ Exception -> 0x011d }
        org.telegram.messenger.FileLog.e(r7);	 Catch:{ Exception -> 0x011d }
        goto L_0x001a;
    L_0x011d:
        r4 = move-exception;
        org.telegram.messenger.FileLog.e(r4);	 Catch:{ Throwable -> 0x0126 }
        r5 = "armeabi";
        goto L_0x001a;
    L_0x0126:
        r4 = move-exception;
        r4.printStackTrace();	 Catch:{ all -> 0x00c8 }
        goto L_0x00b7;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.NativeLoader.initNativeLibs(android.content.Context):void");
    }
}
