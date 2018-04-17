package org.telegram.messenger.time;

import java.text.Format;
import java.util.Arrays;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

abstract class FormatCache<F extends Format> {
    static final int NONE = -1;
    private static final ConcurrentMap<MultipartKey, String> cDateTimeInstanceCache = new ConcurrentHashMap(7);
    private final ConcurrentMap<MultipartKey, F> cInstanceCache = new ConcurrentHashMap(7);

    private static class MultipartKey {
        private int hashCode;
        private final Object[] keys;

        public MultipartKey(Object... keys) {
            this.keys = keys;
        }

        public boolean equals(Object obj) {
            return Arrays.equals(this.keys, ((MultipartKey) obj).keys);
        }

        public int hashCode() {
            if (this.hashCode == 0) {
                int rc = 0;
                for (Object key : this.keys) {
                    if (key != null) {
                        rc = (rc * 7) + key.hashCode();
                    }
                }
                this.hashCode = rc;
            }
            return this.hashCode;
        }
    }

    static java.lang.String getPatternForStyle(java.lang.Integer r1, java.lang.Integer r2, java.util.Locale r3) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.time.FormatCache.getPatternForStyle(java.lang.Integer, java.lang.Integer, java.util.Locale):java.lang.String
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
        /*
        r0 = new org.telegram.messenger.time.FormatCache$MultipartKey;
        r1 = 3;
        r1 = new java.lang.Object[r1];
        r2 = 0;
        r1[r2] = r6;
        r2 = 1;
        r1[r2] = r7;
        r2 = 2;
        r1[r2] = r8;
        r0.<init>(r1);
        r1 = cDateTimeInstanceCache;
        r1 = r1.get(r0);
        r1 = (java.lang.String) r1;
        if (r1 != 0) goto L_0x006b;
    L_0x001b:
        if (r6 != 0) goto L_0x0028;
    L_0x001d:
        r2 = r7.intValue();	 Catch:{ ClassCastException -> 0x0026 }
        r2 = java.text.DateFormat.getTimeInstance(r2, r8);	 Catch:{ ClassCastException -> 0x0026 }
        goto L_0x003f;	 Catch:{ ClassCastException -> 0x0026 }
    L_0x0026:
        r2 = move-exception;	 Catch:{ ClassCastException -> 0x0026 }
        goto L_0x0053;	 Catch:{ ClassCastException -> 0x0026 }
    L_0x0028:
        if (r7 != 0) goto L_0x0033;	 Catch:{ ClassCastException -> 0x0026 }
        r2 = r6.intValue();	 Catch:{ ClassCastException -> 0x0026 }
        r2 = java.text.DateFormat.getDateInstance(r2, r8);	 Catch:{ ClassCastException -> 0x0026 }
        goto L_0x0025;	 Catch:{ ClassCastException -> 0x0026 }
        r2 = r6.intValue();	 Catch:{ ClassCastException -> 0x0026 }
        r3 = r7.intValue();	 Catch:{ ClassCastException -> 0x0026 }
        r2 = java.text.DateFormat.getDateTimeInstance(r2, r3, r8);	 Catch:{ ClassCastException -> 0x0026 }
    L_0x003f:
        r3 = r2;	 Catch:{ ClassCastException -> 0x0026 }
        r3 = (java.text.SimpleDateFormat) r3;	 Catch:{ ClassCastException -> 0x0026 }
        r3 = r3.toPattern();	 Catch:{ ClassCastException -> 0x0026 }
        r1 = r3;	 Catch:{ ClassCastException -> 0x0026 }
        r3 = cDateTimeInstanceCache;	 Catch:{ ClassCastException -> 0x0026 }
        r3 = r3.putIfAbsent(r0, r1);	 Catch:{ ClassCastException -> 0x0026 }
        r3 = (java.lang.String) r3;	 Catch:{ ClassCastException -> 0x0026 }
        if (r3 == 0) goto L_0x0052;
        r1 = r3;
        goto L_0x006b;
        r3 = new java.lang.IllegalArgumentException;
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "No date time pattern for locale: ";
        r4.append(r5);
        r4.append(r8);
        r4 = r4.toString();
        r3.<init>(r4);
        throw r3;
    L_0x006b:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.time.FormatCache.getPatternForStyle(java.lang.Integer, java.lang.Integer, java.util.Locale):java.lang.String");
    }

    protected abstract F createInstance(String str, TimeZone timeZone, Locale locale);

    FormatCache() {
    }

    public F getInstance() {
        return getDateTimeInstance(3, 3, TimeZone.getDefault(), Locale.getDefault());
    }

    public F getInstance(String pattern, TimeZone timeZone, Locale locale) {
        if (pattern == null) {
            throw new NullPointerException("pattern must not be null");
        }
        if (timeZone == null) {
            timeZone = TimeZone.getDefault();
        }
        if (locale == null) {
            locale = Locale.getDefault();
        }
        MultipartKey key = new MultipartKey(pattern, timeZone, locale);
        Format format = (Format) this.cInstanceCache.get(key);
        if (format != null) {
            return format;
        }
        F format2 = createInstance(pattern, timeZone, locale);
        F previousValue = (Format) this.cInstanceCache.putIfAbsent(key, format2);
        if (previousValue != null) {
            return previousValue;
        }
        return format2;
    }

    private F getDateTimeInstance(Integer dateStyle, Integer timeStyle, TimeZone timeZone, Locale locale) {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        return getInstance(getPatternForStyle(dateStyle, timeStyle, locale), timeZone, locale);
    }

    F getDateTimeInstance(int dateStyle, int timeStyle, TimeZone timeZone, Locale locale) {
        return getDateTimeInstance(Integer.valueOf(dateStyle), Integer.valueOf(timeStyle), timeZone, locale);
    }

    F getDateInstance(int dateStyle, TimeZone timeZone, Locale locale) {
        return getDateTimeInstance(Integer.valueOf(dateStyle), null, timeZone, locale);
    }

    F getTimeInstance(int timeStyle, TimeZone timeZone, Locale locale) {
        return getDateTimeInstance(null, Integer.valueOf(timeStyle), timeZone, locale);
    }
}
