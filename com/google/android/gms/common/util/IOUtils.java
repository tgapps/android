package com.google.android.gms.common.util;

import java.io.Closeable;
import java.io.IOException;
import javax.annotation.Nullable;

public final class IOUtils {
    public static void closeQuietly(@Nullable Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
            }
        }
    }
}
