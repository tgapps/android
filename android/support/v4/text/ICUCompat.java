package android.support.v4.text;

import android.os.Build.VERSION;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;

public final class ICUCompat {
    private static Method sAddLikelySubtagsMethod;
    private static Method sGetScriptMethod;

    private static java.lang.String addLikelySubtags(java.util.Locale r1) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: android.support.v4.text.ICUCompat.addLikelySubtags(java.util.Locale):java.lang.String
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
        /*
        r0 = r4.toString();
        r1 = sAddLikelySubtagsMethod;	 Catch:{ IllegalAccessException -> 0x001f, InvocationTargetException -> 0x0018 }
        if (r1 == 0) goto L_0x0025;	 Catch:{ IllegalAccessException -> 0x001f, InvocationTargetException -> 0x0018 }
    L_0x0008:
        r1 = 1;	 Catch:{ IllegalAccessException -> 0x001f, InvocationTargetException -> 0x0018 }
        r1 = new java.lang.Object[r1];	 Catch:{ IllegalAccessException -> 0x001f, InvocationTargetException -> 0x0018 }
        r2 = 0;	 Catch:{ IllegalAccessException -> 0x001f, InvocationTargetException -> 0x0018 }
        r1[r2] = r0;	 Catch:{ IllegalAccessException -> 0x001f, InvocationTargetException -> 0x0018 }
        r2 = sAddLikelySubtagsMethod;	 Catch:{ IllegalAccessException -> 0x001f, InvocationTargetException -> 0x0018 }
        r3 = 0;	 Catch:{ IllegalAccessException -> 0x001f, InvocationTargetException -> 0x0018 }
        r2 = r2.invoke(r3, r1);	 Catch:{ IllegalAccessException -> 0x001f, InvocationTargetException -> 0x0018 }
        r2 = (java.lang.String) r2;	 Catch:{ IllegalAccessException -> 0x001f, InvocationTargetException -> 0x0018 }
        return r2;
    L_0x0018:
        r1 = move-exception;
        r2 = "ICUCompat";
        android.util.Log.w(r2, r1);
        goto L_0x0026;
    L_0x001f:
        r1 = move-exception;
        r2 = "ICUCompat";
        android.util.Log.w(r2, r1);
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.text.ICUCompat.addLikelySubtags(java.util.Locale):java.lang.String");
    }

    private static java.lang.String getScript(java.lang.String r1) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: android.support.v4.text.ICUCompat.getScript(java.lang.String):java.lang.String
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
        /*
        r0 = 0;
        r1 = sGetScriptMethod;	 Catch:{ IllegalAccessException -> 0x001b, InvocationTargetException -> 0x0014 }
        if (r1 == 0) goto L_0x0021;	 Catch:{ IllegalAccessException -> 0x001b, InvocationTargetException -> 0x0014 }
    L_0x0005:
        r1 = 1;	 Catch:{ IllegalAccessException -> 0x001b, InvocationTargetException -> 0x0014 }
        r1 = new java.lang.Object[r1];	 Catch:{ IllegalAccessException -> 0x001b, InvocationTargetException -> 0x0014 }
        r2 = 0;	 Catch:{ IllegalAccessException -> 0x001b, InvocationTargetException -> 0x0014 }
        r1[r2] = r3;	 Catch:{ IllegalAccessException -> 0x001b, InvocationTargetException -> 0x0014 }
        r2 = sGetScriptMethod;	 Catch:{ IllegalAccessException -> 0x001b, InvocationTargetException -> 0x0014 }
        r2 = r2.invoke(r0, r1);	 Catch:{ IllegalAccessException -> 0x001b, InvocationTargetException -> 0x0014 }
        r2 = (java.lang.String) r2;	 Catch:{ IllegalAccessException -> 0x001b, InvocationTargetException -> 0x0014 }
        return r2;
    L_0x0014:
        r1 = move-exception;
        r2 = "ICUCompat";
        android.util.Log.w(r2, r1);
        goto L_0x0022;
    L_0x001b:
        r1 = move-exception;
        r2 = "ICUCompat";
        android.util.Log.w(r2, r1);
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.text.ICUCompat.getScript(java.lang.String):java.lang.String");
    }

    static {
        if (VERSION.SDK_INT >= 21) {
            try {
                sAddLikelySubtagsMethod = Class.forName("libcore.icu.ICU").getMethod("addLikelySubtags", new Class[]{Locale.class});
                return;
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
        try {
            Class<?> clazz = Class.forName("libcore.icu.ICU");
            if (clazz != null) {
                sGetScriptMethod = clazz.getMethod("getScript", new Class[]{String.class});
                sAddLikelySubtagsMethod = clazz.getMethod("addLikelySubtags", new Class[]{String.class});
            }
        } catch (Exception e2) {
            sGetScriptMethod = null;
            sAddLikelySubtagsMethod = null;
            Log.w("ICUCompat", e2);
        }
    }

    public static String maximizeAndGetScript(Locale locale) {
        if (VERSION.SDK_INT >= 21) {
            try {
                return ((Locale) sAddLikelySubtagsMethod.invoke(null, new Object[]{locale})).getScript();
            } catch (InvocationTargetException e) {
                Log.w("ICUCompat", e);
                return locale.getScript();
            } catch (IllegalAccessException e2) {
                Log.w("ICUCompat", e2);
                return locale.getScript();
            }
        }
        String localeWithSubtags = addLikelySubtags(locale);
        if (localeWithSubtags != null) {
            return getScript(localeWithSubtags);
        }
        return null;
    }
}
