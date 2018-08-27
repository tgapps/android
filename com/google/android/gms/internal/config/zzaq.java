package com.google.android.gms.internal.config;

import com.google.android.exoplayer2.C;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

public final class zzaq {
    public static final Charset UTF_8 = Charset.forName(C.UTF8_NAME);
    public static final Pattern zzl = Pattern.compile("^(1|true|t|yes|y|on)$", 2);
    public static final Pattern zzm = Pattern.compile("^(0|false|f|no|n|off|)$", 2);
}
