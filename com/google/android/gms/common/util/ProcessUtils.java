package com.google.android.gms.common.util;

import android.os.Process;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileReader;
import java.io.IOException;
import javax.annotation.Nullable;

public class ProcessUtils {
    private static String zzaai = null;
    private static int zzaaj = 0;

    @Nullable
    public static String getMyProcessName() {
        if (zzaai == null) {
            zzaai = zzl(zzde());
        }
        return zzaai;
    }

    private static int zzde() {
        if (zzaaj == 0) {
            zzaaj = Process.myPid();
        }
        return zzaaj;
    }

    @Nullable
    private static String zzl(int i) {
        Closeable zzm;
        Throwable th;
        Closeable closeable = null;
        if (i <= 0) {
            return null;
        }
        try {
            StringBuilder stringBuilder = new StringBuilder(25);
            stringBuilder.append("/proc/");
            stringBuilder.append(i);
            stringBuilder.append("/cmdline");
            zzm = zzm(stringBuilder.toString());
            try {
                String trim = zzm.readLine().trim();
                IOUtils.closeQuietly(zzm);
                return trim;
            } catch (IOException e) {
                IOUtils.closeQuietly(zzm);
                return null;
            } catch (Throwable th2) {
                Throwable th3 = th2;
                closeable = zzm;
                th = th3;
                IOUtils.closeQuietly(closeable);
                throw th;
            }
        } catch (IOException e2) {
            zzm = null;
            IOUtils.closeQuietly(zzm);
            return null;
        } catch (Throwable th4) {
            th = th4;
            IOUtils.closeQuietly(closeable);
            throw th;
        }
    }

    private static BufferedReader zzm(String str) throws IOException {
        ThreadPolicy allowThreadDiskReads = StrictMode.allowThreadDiskReads();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(str));
            return bufferedReader;
        } finally {
            StrictMode.setThreadPolicy(allowThreadDiskReads);
        }
    }
}
