package org.telegram.messenger;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import java.io.File;

public class NativeLoader {
    private static final String LIB_NAME = "tmessages.28";
    private static final String LIB_SO_NAME = "libtmessages.28.so";
    private static final int LIB_VERSION = 28;
    private static final String LOCALE_LIB_SO_NAME = "libtmessages.28loc.so";
    private static volatile boolean nativeLoaded = false;
    private String crashPath = TtmlNode.ANONYMOUS_REGION_ID;

    private static native void init(String str, boolean z);

    @android.annotation.SuppressLint({"UnsafeDynamicallyLoadedCode"})
    public static synchronized void initNativeLibs(android.content.Context r1) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.NativeLoader.initNativeLibs(android.content.Context):void
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
        r0 = org.telegram.messenger.NativeLoader.class;
        monitor-enter(r0);
        r1 = nativeLoaded;	 Catch:{ all -> 0x0116 }
        if (r1 == 0) goto L_0x0009;
    L_0x0007:
        monitor-exit(r0);
        return;
    L_0x0009:
        net.hockeyapp.android.Constants.loadFromContext(r8);	 Catch:{ all -> 0x0116 }
        r1 = 1;
        r2 = "tmessages.28";	 Catch:{ Error -> 0x0022 }
        java.lang.System.loadLibrary(r2);	 Catch:{ Error -> 0x0022 }
        nativeLoaded = r1;	 Catch:{ Error -> 0x0022 }
        r2 = org.telegram.messenger.BuildVars.LOGS_ENABLED;	 Catch:{ Error -> 0x0022 }
        if (r2 == 0) goto L_0x001d;	 Catch:{ Error -> 0x0022 }
    L_0x0018:
        r2 = "loaded normal lib";	 Catch:{ Error -> 0x0022 }
        org.telegram.messenger.FileLog.d(r2);	 Catch:{ Error -> 0x0022 }
    L_0x001d:
        monitor-exit(r0);
        return;
    L_0x001f:
        r2 = move-exception;
        goto L_0x0104;
    L_0x0022:
        r2 = move-exception;
        org.telegram.messenger.FileLog.e(r2);	 Catch:{ Throwable -> 0x001f }
        r2 = android.os.Build.CPU_ABI;	 Catch:{ Exception -> 0x0093 }
        r3 = android.os.Build.CPU_ABI;	 Catch:{ Exception -> 0x0093 }
        r4 = "x86_64";	 Catch:{ Exception -> 0x0093 }
        r3 = r3.equalsIgnoreCase(r4);	 Catch:{ Exception -> 0x0093 }
        if (r3 == 0) goto L_0x0035;	 Catch:{ Exception -> 0x0093 }
        r3 = "x86_64";	 Catch:{ Exception -> 0x0093 }
        goto L_0x0092;	 Catch:{ Exception -> 0x0093 }
        r3 = android.os.Build.CPU_ABI;	 Catch:{ Exception -> 0x0093 }
        r4 = "arm64-v8a";	 Catch:{ Exception -> 0x0093 }
        r3 = r3.equalsIgnoreCase(r4);	 Catch:{ Exception -> 0x0093 }
        if (r3 == 0) goto L_0x0042;	 Catch:{ Exception -> 0x0093 }
        r3 = "arm64-v8a";	 Catch:{ Exception -> 0x0093 }
        goto L_0x0034;	 Catch:{ Exception -> 0x0093 }
        r3 = android.os.Build.CPU_ABI;	 Catch:{ Exception -> 0x0093 }
        r4 = "armeabi-v7a";	 Catch:{ Exception -> 0x0093 }
        r3 = r3.equalsIgnoreCase(r4);	 Catch:{ Exception -> 0x0093 }
        if (r3 == 0) goto L_0x004f;	 Catch:{ Exception -> 0x0093 }
        r3 = "armeabi-v7a";	 Catch:{ Exception -> 0x0093 }
        goto L_0x0034;	 Catch:{ Exception -> 0x0093 }
        r3 = android.os.Build.CPU_ABI;	 Catch:{ Exception -> 0x0093 }
        r4 = "armeabi";	 Catch:{ Exception -> 0x0093 }
        r3 = r3.equalsIgnoreCase(r4);	 Catch:{ Exception -> 0x0093 }
        if (r3 == 0) goto L_0x005c;	 Catch:{ Exception -> 0x0093 }
        r3 = "armeabi";	 Catch:{ Exception -> 0x0093 }
        goto L_0x0034;	 Catch:{ Exception -> 0x0093 }
        r3 = android.os.Build.CPU_ABI;	 Catch:{ Exception -> 0x0093 }
        r4 = "x86";	 Catch:{ Exception -> 0x0093 }
        r3 = r3.equalsIgnoreCase(r4);	 Catch:{ Exception -> 0x0093 }
        if (r3 == 0) goto L_0x0069;	 Catch:{ Exception -> 0x0093 }
        r3 = "x86";	 Catch:{ Exception -> 0x0093 }
        goto L_0x0034;	 Catch:{ Exception -> 0x0093 }
        r3 = android.os.Build.CPU_ABI;	 Catch:{ Exception -> 0x0093 }
        r4 = "mips";	 Catch:{ Exception -> 0x0093 }
        r3 = r3.equalsIgnoreCase(r4);	 Catch:{ Exception -> 0x0093 }
        if (r3 == 0) goto L_0x0076;	 Catch:{ Exception -> 0x0093 }
        r3 = "mips";	 Catch:{ Exception -> 0x0093 }
        goto L_0x0034;	 Catch:{ Exception -> 0x0093 }
        r3 = "armeabi";	 Catch:{ Exception -> 0x0093 }
        r4 = org.telegram.messenger.BuildVars.LOGS_ENABLED;	 Catch:{ Exception -> 0x0093 }
        if (r4 == 0) goto L_0x0092;	 Catch:{ Exception -> 0x0093 }
        r4 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0093 }
        r4.<init>();	 Catch:{ Exception -> 0x0093 }
        r5 = "Unsupported arch: ";	 Catch:{ Exception -> 0x0093 }
        r4.append(r5);	 Catch:{ Exception -> 0x0093 }
        r5 = android.os.Build.CPU_ABI;	 Catch:{ Exception -> 0x0093 }
        r4.append(r5);	 Catch:{ Exception -> 0x0093 }
        r4 = r4.toString();	 Catch:{ Exception -> 0x0093 }
        org.telegram.messenger.FileLog.e(r4);	 Catch:{ Exception -> 0x0093 }
        goto L_0x0099;
    L_0x0093:
        r2 = move-exception;
        org.telegram.messenger.FileLog.e(r2);	 Catch:{ Throwable -> 0x001f }
        r3 = "armeabi";	 Catch:{ Throwable -> 0x001f }
        r2 = r3;	 Catch:{ Throwable -> 0x001f }
        r3 = "os.arch";	 Catch:{ Throwable -> 0x001f }
        r3 = java.lang.System.getProperty(r3);	 Catch:{ Throwable -> 0x001f }
        if (r3 == 0) goto L_0x00ad;	 Catch:{ Throwable -> 0x001f }
        r4 = "686";	 Catch:{ Throwable -> 0x001f }
        r4 = r3.contains(r4);	 Catch:{ Throwable -> 0x001f }
        if (r4 == 0) goto L_0x00ad;	 Catch:{ Throwable -> 0x001f }
        r4 = "x86";	 Catch:{ Throwable -> 0x001f }
        r2 = r4;	 Catch:{ Throwable -> 0x001f }
        r4 = new java.io.File;	 Catch:{ Throwable -> 0x001f }
        r5 = r8.getFilesDir();	 Catch:{ Throwable -> 0x001f }
        r6 = "lib";	 Catch:{ Throwable -> 0x001f }
        r4.<init>(r5, r6);	 Catch:{ Throwable -> 0x001f }
        r4.mkdirs();	 Catch:{ Throwable -> 0x001f }
        r5 = new java.io.File;	 Catch:{ Throwable -> 0x001f }
        r6 = "libtmessages.28loc.so";	 Catch:{ Throwable -> 0x001f }
        r5.<init>(r4, r6);	 Catch:{ Throwable -> 0x001f }
        r6 = r5.exists();	 Catch:{ Throwable -> 0x001f }
        if (r6 == 0) goto L_0x00e3;
        r6 = org.telegram.messenger.BuildVars.LOGS_ENABLED;	 Catch:{ Error -> 0x00dc }
        if (r6 == 0) goto L_0x00d1;	 Catch:{ Error -> 0x00dc }
        r6 = "Load local lib";	 Catch:{ Error -> 0x00dc }
        org.telegram.messenger.FileLog.d(r6);	 Catch:{ Error -> 0x00dc }
        r6 = r5.getAbsolutePath();	 Catch:{ Error -> 0x00dc }
        java.lang.System.load(r6);	 Catch:{ Error -> 0x00dc }
        nativeLoaded = r1;	 Catch:{ Error -> 0x00dc }
        monitor-exit(r0);
        return;
    L_0x00dc:
        r6 = move-exception;
        org.telegram.messenger.FileLog.e(r6);	 Catch:{ Throwable -> 0x001f }
        r5.delete();	 Catch:{ Throwable -> 0x001f }
        r6 = org.telegram.messenger.BuildVars.LOGS_ENABLED;	 Catch:{ Throwable -> 0x001f }
        if (r6 == 0) goto L_0x00fb;	 Catch:{ Throwable -> 0x001f }
        r6 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x001f }
        r6.<init>();	 Catch:{ Throwable -> 0x001f }
        r7 = "Library not found, arch = ";	 Catch:{ Throwable -> 0x001f }
        r6.append(r7);	 Catch:{ Throwable -> 0x001f }
        r6.append(r2);	 Catch:{ Throwable -> 0x001f }
        r6 = r6.toString();	 Catch:{ Throwable -> 0x001f }
        org.telegram.messenger.FileLog.e(r6);	 Catch:{ Throwable -> 0x001f }
        r6 = loadFromZip(r8, r4, r5, r2);	 Catch:{ Throwable -> 0x001f }
        if (r6 == 0) goto L_0x0103;
        monitor-exit(r0);
        return;
        goto L_0x0108;
        r2.printStackTrace();	 Catch:{ all -> 0x0116 }
        r2 = "tmessages.28";	 Catch:{ Error -> 0x0110 }
        java.lang.System.loadLibrary(r2);	 Catch:{ Error -> 0x0110 }
        nativeLoaded = r1;	 Catch:{ Error -> 0x0110 }
        goto L_0x0114;
    L_0x0110:
        r1 = move-exception;
        org.telegram.messenger.FileLog.e(r1);	 Catch:{ all -> 0x0116 }
        monitor-exit(r0);
        return;
    L_0x0116:
        r8 = move-exception;
        monitor-exit(r0);
        throw r8;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.NativeLoader.initNativeLibs(android.content.Context):void");
    }

    @android.annotation.SuppressLint({"UnsafeDynamicallyLoadedCode", "SetWorldReadable"})
    private static boolean loadFromZip(android.content.Context r1, java.io.File r2, java.io.File r3, java.lang.String r4) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.NativeLoader.loadFromZip(android.content.Context, java.io.File, java.io.File, java.lang.String):boolean
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
        r0 = 0;
        r1 = r10.listFiles();	 Catch:{ Exception -> 0x0012 }
        r2 = r1.length;	 Catch:{ Exception -> 0x0012 }
        r3 = r0;	 Catch:{ Exception -> 0x0012 }
    L_0x0007:
        if (r3 >= r2) goto L_0x0011;	 Catch:{ Exception -> 0x0012 }
    L_0x0009:
        r4 = r1[r3];	 Catch:{ Exception -> 0x0012 }
        r4.delete();	 Catch:{ Exception -> 0x0012 }
        r3 = r3 + 1;
        goto L_0x0007;
    L_0x0011:
        goto L_0x0016;
    L_0x0012:
        r1 = move-exception;
        org.telegram.messenger.FileLog.e(r1);
    L_0x0016:
        r1 = 0;
        r2 = 0;
        r3 = new java.util.zip.ZipFile;	 Catch:{ Exception -> 0x00b5 }
        r4 = r9.getApplicationInfo();	 Catch:{ Exception -> 0x00b5 }
        r4 = r4.sourceDir;	 Catch:{ Exception -> 0x00b5 }
        r3.<init>(r4);	 Catch:{ Exception -> 0x00b5 }
        r1 = r3;	 Catch:{ Exception -> 0x00b5 }
        r3 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00b5 }
        r3.<init>();	 Catch:{ Exception -> 0x00b5 }
        r4 = "lib/";	 Catch:{ Exception -> 0x00b5 }
        r3.append(r4);	 Catch:{ Exception -> 0x00b5 }
        r3.append(r12);	 Catch:{ Exception -> 0x00b5 }
        r4 = "/";	 Catch:{ Exception -> 0x00b5 }
        r3.append(r4);	 Catch:{ Exception -> 0x00b5 }
        r4 = "libtmessages.28.so";	 Catch:{ Exception -> 0x00b5 }
        r3.append(r4);	 Catch:{ Exception -> 0x00b5 }
        r3 = r3.toString();	 Catch:{ Exception -> 0x00b5 }
        r3 = r1.getEntry(r3);	 Catch:{ Exception -> 0x00b5 }
        if (r3 != 0) goto L_0x0066;	 Catch:{ Exception -> 0x00b5 }
    L_0x0045:
        r4 = new java.lang.Exception;	 Catch:{ Exception -> 0x00b5 }
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00b5 }
        r5.<init>();	 Catch:{ Exception -> 0x00b5 }
        r6 = "Unable to find file in apk:lib/";	 Catch:{ Exception -> 0x00b5 }
        r5.append(r6);	 Catch:{ Exception -> 0x00b5 }
        r5.append(r12);	 Catch:{ Exception -> 0x00b5 }
        r6 = "/";	 Catch:{ Exception -> 0x00b5 }
        r5.append(r6);	 Catch:{ Exception -> 0x00b5 }
        r6 = "tmessages.28";	 Catch:{ Exception -> 0x00b5 }
        r5.append(r6);	 Catch:{ Exception -> 0x00b5 }
        r5 = r5.toString();	 Catch:{ Exception -> 0x00b5 }
        r4.<init>(r5);	 Catch:{ Exception -> 0x00b5 }
        throw r4;	 Catch:{ Exception -> 0x00b5 }
    L_0x0066:
        r4 = r1.getInputStream(r3);	 Catch:{ Exception -> 0x00b5 }
        r2 = r4;	 Catch:{ Exception -> 0x00b5 }
        r4 = new java.io.FileOutputStream;	 Catch:{ Exception -> 0x00b5 }
        r4.<init>(r11);	 Catch:{ Exception -> 0x00b5 }
        r5 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;	 Catch:{ Exception -> 0x00b5 }
        r5 = new byte[r5];	 Catch:{ Exception -> 0x00b5 }
    L_0x0074:
        r6 = r2.read(r5);	 Catch:{ Exception -> 0x00b5 }
        r7 = r6;	 Catch:{ Exception -> 0x00b5 }
        if (r6 <= 0) goto L_0x0082;	 Catch:{ Exception -> 0x00b5 }
    L_0x007b:
        java.lang.Thread.yield();	 Catch:{ Exception -> 0x00b5 }
        r4.write(r5, r0, r7);	 Catch:{ Exception -> 0x00b5 }
        goto L_0x0074;	 Catch:{ Exception -> 0x00b5 }
    L_0x0082:
        r4.close();	 Catch:{ Exception -> 0x00b5 }
        r6 = 1;	 Catch:{ Exception -> 0x00b5 }
        r11.setReadable(r6, r0);	 Catch:{ Exception -> 0x00b5 }
        r11.setExecutable(r6, r0);	 Catch:{ Exception -> 0x00b5 }
        r11.setWritable(r6);	 Catch:{ Exception -> 0x00b5 }
        r8 = r11.getAbsolutePath();	 Catch:{ Error -> 0x0099 }
        java.lang.System.load(r8);	 Catch:{ Error -> 0x0099 }
        nativeLoaded = r6;	 Catch:{ Error -> 0x0099 }
        goto L_0x009d;
    L_0x0099:
        r8 = move-exception;
        org.telegram.messenger.FileLog.e(r8);	 Catch:{ Exception -> 0x00b5 }
        if (r2 == 0) goto L_0x00a8;
        r2.close();	 Catch:{ Exception -> 0x00a4 }
        goto L_0x00a8;
    L_0x00a4:
        r0 = move-exception;
        org.telegram.messenger.FileLog.e(r0);
        if (r1 == 0) goto L_0x00b2;
        r1.close();	 Catch:{ Exception -> 0x00ae }
        goto L_0x00b2;
    L_0x00ae:
        r0 = move-exception;
        org.telegram.messenger.FileLog.e(r0);
        return r6;
    L_0x00b3:
        r0 = move-exception;
        goto L_0x00cf;
    L_0x00b5:
        r3 = move-exception;
        org.telegram.messenger.FileLog.e(r3);	 Catch:{ all -> 0x00b3 }
        if (r2 == 0) goto L_0x00c3;
        r2.close();	 Catch:{ Exception -> 0x00bf }
        goto L_0x00c3;
    L_0x00bf:
        r3 = move-exception;
        org.telegram.messenger.FileLog.e(r3);
        if (r1 == 0) goto L_0x00ce;
        r1.close();	 Catch:{ Exception -> 0x00c9 }
        goto L_0x00ce;
    L_0x00c9:
        r3 = move-exception;
        org.telegram.messenger.FileLog.e(r3);
        goto L_0x00c8;
        return r0;
        if (r2 == 0) goto L_0x00d9;
        r2.close();	 Catch:{ Exception -> 0x00d5 }
        goto L_0x00d9;
    L_0x00d5:
        r3 = move-exception;
        org.telegram.messenger.FileLog.e(r3);
        if (r1 == 0) goto L_0x00e3;
        r1.close();	 Catch:{ Exception -> 0x00df }
        goto L_0x00e3;
    L_0x00df:
        r3 = move-exception;
        org.telegram.messenger.FileLog.e(r3);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.NativeLoader.loadFromZip(android.content.Context, java.io.File, java.io.File, java.lang.String):boolean");
    }

    private static File getNativeLibraryDir(Context context) {
        File f = null;
        if (context != null) {
            try {
                f = new File((String) ApplicationInfo.class.getField("nativeLibraryDir").get(context.getApplicationInfo()));
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
        if (f == null) {
            f = new File(context.getApplicationInfo().dataDir, "lib");
        }
        if (f.isDirectory()) {
            return f;
        }
        return null;
    }
}
