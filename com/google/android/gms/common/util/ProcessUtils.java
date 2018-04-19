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
        Closeable closeable;
        String str = null;
        if (i > 0) {
            try {
                zzm = zzm("/proc/" + i + "/cmdline");
                try {
                    str = zzm.readLine().trim();
                    IOUtils.closeQuietly(zzm);
                } catch (IOException e) {
                    IOUtils.closeQuietly(zzm);
                    return str;
                } catch (Throwable th2) {
                    th = th2;
                    closeable = zzm;
                    IOUtils.closeQuietly(closeable);
                    throw th;
                }
            } catch (IOException e2) {
                zzm = str;
                IOUtils.closeQuietly(zzm);
                return str;
            } catch (Throwable th3) {
                th = th3;
                closeable = str;
                IOUtils.closeQuietly(closeable);
                throw th;
            }
        }
        return str;
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
