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
        Throwable th;
        try {
            for (File file : destDir.listFiles()) {
                file.delete();
            }
        } catch (Throwable e) {
            Throwable e2;
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
    public static synchronized void initNativeLibs(android.content.Context r11) {
        /*
        r9 = org.telegram.messenger.NativeLoader.class;
        monitor-enter(r9);
        r8 = nativeLoaded;	 Catch:{ all -> 0x00ca }
        if (r8 == 0) goto L_0x0009;
    L_0x0007:
        monitor-exit(r9);
        return;
    L_0x0009:
        net.hockeyapp.android.Constants.loadFromContext(r11);	 Catch:{ all -> 0x00ca }
        r7 = android.os.Build.CPU_ABI;	 Catch:{ Exception -> 0x013f }
        r8 = android.os.Build.CPU_ABI;	 Catch:{ Exception -> 0x013f }
        r10 = "x86_64";
        r8 = r8.equalsIgnoreCase(r10);	 Catch:{ Exception -> 0x013f }
        if (r8 == 0) goto L_0x00cd;
    L_0x0019:
        r5 = "x86_64";
    L_0x001c:
        r8 = "os.arch";
        r6 = java.lang.System.getProperty(r8);	 Catch:{ Throwable -> 0x0148 }
        if (r6 == 0) goto L_0x0031;
    L_0x0025:
        r8 = "686";
        r8 = r6.contains(r8);	 Catch:{ Throwable -> 0x0148 }
        if (r8 == 0) goto L_0x0031;
    L_0x002e:
        r5 = "x86";
    L_0x0031:
        r1 = getNativeLibraryDir(r11);	 Catch:{ Throwable -> 0x0148 }
        if (r1 == 0) goto L_0x005e;
    L_0x0037:
        r2 = new java.io.File;	 Catch:{ Throwable -> 0x0148 }
        r8 = "libtmessages.28.so";
        r2.<init>(r1, r8);	 Catch:{ Throwable -> 0x0148 }
        r8 = r2.exists();	 Catch:{ Throwable -> 0x0148 }
        if (r8 == 0) goto L_0x005d;
    L_0x0045:
        r8 = org.telegram.messenger.BuildVars.LOGS_ENABLED;	 Catch:{ Throwable -> 0x0148 }
        if (r8 == 0) goto L_0x004f;
    L_0x0049:
        r8 = "load normal lib";
        org.telegram.messenger.FileLog.d(r8);	 Catch:{ Throwable -> 0x0148 }
    L_0x004f:
        r8 = "tmessages.28";
        java.lang.System.loadLibrary(r8);	 Catch:{ Error -> 0x0059 }
        r8 = 1;
        nativeLoaded = r8;	 Catch:{ Error -> 0x0059 }
        goto L_0x0007;
    L_0x0059:
        r4 = move-exception;
        org.telegram.messenger.FileLog.e(r4);	 Catch:{ Throwable -> 0x0148 }
    L_0x005d:
        r1 = r2;
    L_0x005e:
        r0 = new java.io.File;	 Catch:{ Throwable -> 0x0148 }
        r8 = r11.getFilesDir();	 Catch:{ Throwable -> 0x0148 }
        r10 = "lib";
        r0.<init>(r8, r10);	 Catch:{ Throwable -> 0x0148 }
        r0.mkdirs();	 Catch:{ Throwable -> 0x0148 }
        r3 = new java.io.File;	 Catch:{ Throwable -> 0x0148 }
        r8 = "libtmessages.28loc.so";
        r3.<init>(r0, r8);	 Catch:{ Throwable -> 0x0148 }
        r8 = r3.exists();	 Catch:{ Throwable -> 0x0148 }
        if (r8 == 0) goto L_0x0098;
    L_0x007b:
        r8 = org.telegram.messenger.BuildVars.LOGS_ENABLED;	 Catch:{ Error -> 0x0091 }
        if (r8 == 0) goto L_0x0085;
    L_0x007f:
        r8 = "Load local lib";
        org.telegram.messenger.FileLog.d(r8);	 Catch:{ Error -> 0x0091 }
    L_0x0085:
        r8 = r3.getAbsolutePath();	 Catch:{ Error -> 0x0091 }
        java.lang.System.load(r8);	 Catch:{ Error -> 0x0091 }
        r8 = 1;
        nativeLoaded = r8;	 Catch:{ Error -> 0x0091 }
        goto L_0x0007;
    L_0x0091:
        r4 = move-exception;
        org.telegram.messenger.FileLog.e(r4);	 Catch:{ Throwable -> 0x0148 }
        r3.delete();	 Catch:{ Throwable -> 0x0148 }
    L_0x0098:
        r8 = org.telegram.messenger.BuildVars.LOGS_ENABLED;	 Catch:{ Throwable -> 0x0148 }
        if (r8 == 0) goto L_0x00b3;
    L_0x009c:
        r8 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x0148 }
        r8.<init>();	 Catch:{ Throwable -> 0x0148 }
        r10 = "Library not found, arch = ";
        r8 = r8.append(r10);	 Catch:{ Throwable -> 0x0148 }
        r8 = r8.append(r5);	 Catch:{ Throwable -> 0x0148 }
        r8 = r8.toString();	 Catch:{ Throwable -> 0x0148 }
        org.telegram.messenger.FileLog.e(r8);	 Catch:{ Throwable -> 0x0148 }
    L_0x00b3:
        r8 = loadFromZip(r11, r0, r3, r5);	 Catch:{ Throwable -> 0x0148 }
        if (r8 != 0) goto L_0x0007;
    L_0x00b9:
        r8 = "tmessages.28";
        java.lang.System.loadLibrary(r8);	 Catch:{ Error -> 0x00c4 }
        r8 = 1;
        nativeLoaded = r8;	 Catch:{ Error -> 0x00c4 }
        goto L_0x0007;
    L_0x00c4:
        r4 = move-exception;
        org.telegram.messenger.FileLog.e(r4);	 Catch:{ all -> 0x00ca }
        goto L_0x0007;
    L_0x00ca:
        r8 = move-exception;
        monitor-exit(r9);
        throw r8;
    L_0x00cd:
        r8 = android.os.Build.CPU_ABI;	 Catch:{ Exception -> 0x013f }
        r10 = "arm64-v8a";
        r8 = r8.equalsIgnoreCase(r10);	 Catch:{ Exception -> 0x013f }
        if (r8 == 0) goto L_0x00dd;
    L_0x00d8:
        r5 = "arm64-v8a";
        goto L_0x001c;
    L_0x00dd:
        r8 = android.os.Build.CPU_ABI;	 Catch:{ Exception -> 0x013f }
        r10 = "armeabi-v7a";
        r8 = r8.equalsIgnoreCase(r10);	 Catch:{ Exception -> 0x013f }
        if (r8 == 0) goto L_0x00ed;
    L_0x00e8:
        r5 = "armeabi-v7a";
        goto L_0x001c;
    L_0x00ed:
        r8 = android.os.Build.CPU_ABI;	 Catch:{ Exception -> 0x013f }
        r10 = "armeabi";
        r8 = r8.equalsIgnoreCase(r10);	 Catch:{ Exception -> 0x013f }
        if (r8 == 0) goto L_0x00fd;
    L_0x00f8:
        r5 = "armeabi";
        goto L_0x001c;
    L_0x00fd:
        r8 = android.os.Build.CPU_ABI;	 Catch:{ Exception -> 0x013f }
        r10 = "x86";
        r8 = r8.equalsIgnoreCase(r10);	 Catch:{ Exception -> 0x013f }
        if (r8 == 0) goto L_0x010d;
    L_0x0108:
        r5 = "x86";
        goto L_0x001c;
    L_0x010d:
        r8 = android.os.Build.CPU_ABI;	 Catch:{ Exception -> 0x013f }
        r10 = "mips";
        r8 = r8.equalsIgnoreCase(r10);	 Catch:{ Exception -> 0x013f }
        if (r8 == 0) goto L_0x011d;
    L_0x0118:
        r5 = "mips";
        goto L_0x001c;
    L_0x011d:
        r5 = "armeabi";
        r8 = org.telegram.messenger.BuildVars.LOGS_ENABLED;	 Catch:{ Exception -> 0x013f }
        if (r8 == 0) goto L_0x001c;
    L_0x0124:
        r8 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x013f }
        r8.<init>();	 Catch:{ Exception -> 0x013f }
        r10 = "Unsupported arch: ";
        r8 = r8.append(r10);	 Catch:{ Exception -> 0x013f }
        r10 = android.os.Build.CPU_ABI;	 Catch:{ Exception -> 0x013f }
        r8 = r8.append(r10);	 Catch:{ Exception -> 0x013f }
        r8 = r8.toString();	 Catch:{ Exception -> 0x013f }
        org.telegram.messenger.FileLog.e(r8);	 Catch:{ Exception -> 0x013f }
        goto L_0x001c;
    L_0x013f:
        r4 = move-exception;
        org.telegram.messenger.FileLog.e(r4);	 Catch:{ Throwable -> 0x0148 }
        r5 = "armeabi";
        goto L_0x001c;
    L_0x0148:
        r4 = move-exception;
        r4.printStackTrace();	 Catch:{ all -> 0x00ca }
        goto L_0x00b9;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.NativeLoader.initNativeLibs(android.content.Context):void");
    }
}
