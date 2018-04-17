package org.telegram.messenger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.text.TextUtils;
import android.text.format.DateFormat;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.TimeZone;
import org.telegram.messenger.beta.R;
import org.telegram.messenger.time.FastDateFormat;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.LangPackString;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.tgnet.TLRPC.TL_langPackDifference;
import org.telegram.tgnet.TLRPC.TL_langPackLanguage;
import org.telegram.tgnet.TLRPC.TL_langPackString;
import org.telegram.tgnet.TLRPC.TL_langPackStringDeleted;
import org.telegram.tgnet.TLRPC.TL_langPackStringPluralized;
import org.telegram.tgnet.TLRPC.TL_langpack_getDifference;
import org.telegram.tgnet.TLRPC.TL_langpack_getLangPack;
import org.telegram.tgnet.TLRPC.TL_langpack_getLanguages;
import org.telegram.tgnet.TLRPC.TL_userEmpty;
import org.telegram.tgnet.TLRPC.TL_userStatusLastMonth;
import org.telegram.tgnet.TLRPC.TL_userStatusLastWeek;
import org.telegram.tgnet.TLRPC.TL_userStatusRecently;
import org.telegram.tgnet.TLRPC.User;
import org.telegram.tgnet.TLRPC.Vector;

public class LocaleController {
    private static volatile LocaleController Instance = null;
    static final int QUANTITY_FEW = 8;
    static final int QUANTITY_MANY = 16;
    static final int QUANTITY_ONE = 2;
    static final int QUANTITY_OTHER = 0;
    static final int QUANTITY_TWO = 4;
    static final int QUANTITY_ZERO = 1;
    public static boolean is24HourFormat = false;
    public static boolean isRTL = false;
    public static int nameDisplayOrder = 1;
    private HashMap<String, PluralRules> allRules = new HashMap();
    private boolean changingConfiguration;
    public FastDateFormat chatDate;
    public FastDateFormat chatFullDate;
    private HashMap<String, String> currencyValues;
    private Locale currentLocale;
    private LocaleInfo currentLocaleInfo;
    private PluralRules currentPluralRules;
    public FastDateFormat formatterBannedUntil;
    public FastDateFormat formatterBannedUntilThisYear;
    public FastDateFormat formatterDay;
    public FastDateFormat formatterMonth;
    public FastDateFormat formatterMonthYear;
    public FastDateFormat formatterStats;
    public FastDateFormat formatterWeek;
    public FastDateFormat formatterYear;
    public FastDateFormat formatterYearMax;
    private String languageOverride;
    public ArrayList<LocaleInfo> languages;
    public HashMap<String, LocaleInfo> languagesDict;
    private boolean loadingRemoteLanguages;
    private HashMap<String, String> localeValues = new HashMap();
    private ArrayList<LocaleInfo> otherLanguages;
    private boolean reloadLastFile;
    public ArrayList<LocaleInfo> remoteLanguages;
    private Locale systemDefaultLocale;
    private HashMap<String, String> translitChars;

    public static class LocaleInfo {
        public boolean builtIn;
        public String name;
        public String nameEnglish;
        public String pathToFile;
        public String shortName;
        public int version;

        public String getSaveString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.name);
            stringBuilder.append("|");
            stringBuilder.append(this.nameEnglish);
            stringBuilder.append("|");
            stringBuilder.append(this.shortName);
            stringBuilder.append("|");
            stringBuilder.append(this.pathToFile);
            stringBuilder.append("|");
            stringBuilder.append(this.version);
            return stringBuilder.toString();
        }

        public static LocaleInfo createWithString(String string) {
            if (string != null) {
                if (string.length() != 0) {
                    String[] args = string.split("\\|");
                    LocaleInfo localeInfo = null;
                    if (args.length >= 4) {
                        localeInfo = new LocaleInfo();
                        localeInfo.name = args[0];
                        localeInfo.nameEnglish = args[1];
                        localeInfo.shortName = args[2].toLowerCase();
                        localeInfo.pathToFile = args[3];
                        if (args.length >= 5) {
                            localeInfo.version = Utilities.parseInt(args[4]).intValue();
                        }
                    }
                    return localeInfo;
                }
            }
            return null;
        }

        public File getPathToFile() {
            if (isRemote()) {
                File filesDirFixed = ApplicationLoader.getFilesDirFixed();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("remote_");
                stringBuilder.append(this.shortName);
                stringBuilder.append(".xml");
                return new File(filesDirFixed, stringBuilder.toString());
            }
            return !TextUtils.isEmpty(this.pathToFile) ? new File(this.pathToFile) : null;
        }

        public String getKey() {
            if (this.pathToFile == null || "remote".equals(this.pathToFile)) {
                return this.shortName;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("local_");
            stringBuilder.append(this.shortName);
            return stringBuilder.toString();
        }

        public boolean isRemote() {
            return "remote".equals(this.pathToFile);
        }

        public boolean isLocal() {
            return (TextUtils.isEmpty(this.pathToFile) || isRemote()) ? false : true;
        }

        public boolean isBuiltIn() {
            return this.builtIn;
        }
    }

    public static abstract class PluralRules {
        abstract int quantityForNumber(int i);
    }

    private class TimeZoneChangedReceiver extends BroadcastReceiver {
        private TimeZoneChangedReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            ApplicationLoader.applicationHandler.post(new Runnable() {
                public void run() {
                    if (!LocaleController.this.formatterMonth.getTimeZone().equals(TimeZone.getDefault())) {
                        LocaleController.getInstance().recreateFormatters();
                    }
                }
            });
        }
    }

    public static class PluralRules_Arabic extends PluralRules {
        public int quantityForNumber(int count) {
            int rem100 = count % 100;
            if (count == 0) {
                return 1;
            }
            if (count == 1) {
                return 2;
            }
            if (count == 2) {
                return 4;
            }
            if (rem100 >= 3 && rem100 <= 10) {
                return 8;
            }
            if (rem100 < 11 || rem100 > 99) {
                return 0;
            }
            return 16;
        }
    }

    public static class PluralRules_Balkan extends PluralRules {
        public int quantityForNumber(int count) {
            int rem100 = count % 100;
            int rem10 = count % 10;
            if (rem10 == 1 && rem100 != 11) {
                return 2;
            }
            if (rem10 >= 2 && rem10 <= 4 && (rem100 < 12 || rem100 > 14)) {
                return 8;
            }
            if (rem10 != 0 && (rem10 < 5 || rem10 > 9)) {
                if (rem100 < 11 || rem100 > 14) {
                    return 0;
                }
            }
            return 16;
        }
    }

    public static class PluralRules_Breton extends PluralRules {
        public int quantityForNumber(int count) {
            if (count == 0) {
                return 1;
            }
            if (count == 1) {
                return 2;
            }
            if (count == 2) {
                return 4;
            }
            if (count == 3) {
                return 8;
            }
            if (count == 6) {
                return 16;
            }
            return 0;
        }
    }

    public static class PluralRules_Czech extends PluralRules {
        public int quantityForNumber(int count) {
            if (count == 1) {
                return 2;
            }
            if (count < 2 || count > 4) {
                return 0;
            }
            return 8;
        }
    }

    public static class PluralRules_French extends PluralRules {
        public int quantityForNumber(int count) {
            if (count < 0 || count >= 2) {
                return 0;
            }
            return 2;
        }
    }

    public static class PluralRules_Langi extends PluralRules {
        public int quantityForNumber(int count) {
            if (count == 0) {
                return 1;
            }
            if (count <= 0 || count >= 2) {
                return 0;
            }
            return 2;
        }
    }

    public static class PluralRules_Latvian extends PluralRules {
        public int quantityForNumber(int count) {
            if (count == 0) {
                return 1;
            }
            if (count % 10 != 1 || count % 100 == 11) {
                return 0;
            }
            return 2;
        }
    }

    public static class PluralRules_Lithuanian extends PluralRules {
        public int quantityForNumber(int count) {
            int rem100 = count % 100;
            int rem10 = count % 10;
            if (rem10 == 1 && (rem100 < 11 || rem100 > 19)) {
                return 2;
            }
            if (rem10 < 2 || rem10 > 9 || (rem100 >= 11 && rem100 <= 19)) {
                return 0;
            }
            return 8;
        }
    }

    public static class PluralRules_Macedonian extends PluralRules {
        public int quantityForNumber(int count) {
            if (count % 10 != 1 || count == 11) {
                return 0;
            }
            return 2;
        }
    }

    public static class PluralRules_Maltese extends PluralRules {
        public int quantityForNumber(int count) {
            int rem100 = count % 100;
            if (count == 1) {
                return 2;
            }
            if (count != 0) {
                if (rem100 < 2 || rem100 > 10) {
                    if (rem100 < 11 || rem100 > 19) {
                        return 0;
                    }
                    return 16;
                }
            }
            return 8;
        }
    }

    public static class PluralRules_None extends PluralRules {
        public int quantityForNumber(int count) {
            return 0;
        }
    }

    public static class PluralRules_One extends PluralRules {
        public int quantityForNumber(int count) {
            return count == 1 ? 2 : 0;
        }
    }

    public static class PluralRules_Polish extends PluralRules {
        public int quantityForNumber(int count) {
            int rem100 = count % 100;
            int rem10 = count % 10;
            if (count == 1) {
                return 2;
            }
            if (rem10 < 2 || rem10 > 4 || ((rem100 >= 12 && rem100 <= 14) || (rem100 >= 22 && rem100 <= 24))) {
                return 0;
            }
            return 8;
        }
    }

    public static class PluralRules_Romanian extends PluralRules {
        public int quantityForNumber(int count) {
            int rem100 = count % 100;
            if (count == 1) {
                return 2;
            }
            if (count != 0) {
                if (rem100 < 1 || rem100 > 19) {
                    return 0;
                }
            }
            return 8;
        }
    }

    public static class PluralRules_Slovenian extends PluralRules {
        public int quantityForNumber(int count) {
            int rem100 = count % 100;
            if (rem100 == 1) {
                return 2;
            }
            if (rem100 == 2) {
                return 4;
            }
            if (rem100 < 3 || rem100 > 4) {
                return 0;
            }
            return 8;
        }
    }

    public static class PluralRules_Tachelhit extends PluralRules {
        public int quantityForNumber(int count) {
            if (count >= 0 && count <= 1) {
                return 2;
            }
            if (count < 2 || count > 10) {
                return 0;
            }
            return 8;
        }
    }

    public static class PluralRules_Two extends PluralRules {
        public int quantityForNumber(int count) {
            if (count == 1) {
                return 2;
            }
            if (count == 2) {
                return 4;
            }
            return 0;
        }
    }

    public static class PluralRules_Welsh extends PluralRules {
        public int quantityForNumber(int count) {
            if (count == 0) {
                return 1;
            }
            if (count == 1) {
                return 2;
            }
            if (count == 2) {
                return 4;
            }
            if (count == 3) {
                return 8;
            }
            if (count == 6) {
                return 16;
            }
            return 0;
        }
    }

    public static class PluralRules_Zero extends PluralRules {
        public int quantityForNumber(int count) {
            if (count != 0) {
                if (count != 1) {
                    return 0;
                }
            }
            return 2;
        }
    }

    private java.util.HashMap<java.lang.String, java.lang.String> getLocaleFileStrings(java.io.File r1, boolean r2) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.LocaleController.getLocaleFileStrings(java.io.File, boolean):java.util.HashMap<java.lang.String, java.lang.String>
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
        r0 = this;
        r0 = 0;
        r1 = 0;
        r12.reloadLastFile = r1;
        r2 = 1;
        r3 = r13.exists();	 Catch:{ Exception -> 0x00e9 }
        if (r3 != 0) goto L_0x001d;	 Catch:{ Exception -> 0x00e9 }
    L_0x000b:
        r1 = new java.util.HashMap;	 Catch:{ Exception -> 0x00e9 }
        r1.<init>();	 Catch:{ Exception -> 0x00e9 }
        if (r0 == 0) goto L_0x001b;
    L_0x0012:
        r0.close();	 Catch:{ Exception -> 0x0016 }
        goto L_0x001b;
    L_0x0016:
        r2 = move-exception;
        org.telegram.messenger.FileLog.e(r2);
        goto L_0x001c;
        return r1;
    L_0x001d:
        r3 = new java.util.HashMap;	 Catch:{ Exception -> 0x00e9 }
        r3.<init>();	 Catch:{ Exception -> 0x00e9 }
        r4 = android.util.Xml.newPullParser();	 Catch:{ Exception -> 0x00e9 }
        r5 = new java.io.FileInputStream;	 Catch:{ Exception -> 0x00e9 }
        r5.<init>(r13);	 Catch:{ Exception -> 0x00e9 }
        r0 = r5;	 Catch:{ Exception -> 0x00e9 }
        r5 = "UTF-8";	 Catch:{ Exception -> 0x00e9 }
        r4.setInput(r0, r5);	 Catch:{ Exception -> 0x00e9 }
        r5 = r4.getEventType();	 Catch:{ Exception -> 0x00e9 }
        r6 = 0;	 Catch:{ Exception -> 0x00e9 }
        r7 = 0;	 Catch:{ Exception -> 0x00e9 }
        r8 = 0;	 Catch:{ Exception -> 0x00e9 }
        if (r5 == r2) goto L_0x00d9;	 Catch:{ Exception -> 0x00e9 }
        r9 = 2;	 Catch:{ Exception -> 0x00e9 }
        if (r5 != r9) goto L_0x004e;	 Catch:{ Exception -> 0x00e9 }
        r9 = r4.getName();	 Catch:{ Exception -> 0x00e9 }
        r6 = r9;	 Catch:{ Exception -> 0x00e9 }
        r9 = r4.getAttributeCount();	 Catch:{ Exception -> 0x00e9 }
        if (r9 <= 0) goto L_0x004d;	 Catch:{ Exception -> 0x00e9 }
        r10 = r4.getAttributeValue(r1);	 Catch:{ Exception -> 0x00e9 }
        r8 = r10;	 Catch:{ Exception -> 0x00e9 }
        goto L_0x00b2;	 Catch:{ Exception -> 0x00e9 }
        r9 = 4;	 Catch:{ Exception -> 0x00e9 }
        if (r5 != r9) goto L_0x00ac;	 Catch:{ Exception -> 0x00e9 }
        if (r8 == 0) goto L_0x00b2;	 Catch:{ Exception -> 0x00e9 }
        r9 = r4.getText();	 Catch:{ Exception -> 0x00e9 }
        r7 = r9;	 Catch:{ Exception -> 0x00e9 }
        if (r7 == 0) goto L_0x00b2;	 Catch:{ Exception -> 0x00e9 }
        r9 = r7.trim();	 Catch:{ Exception -> 0x00e9 }
        r7 = r9;	 Catch:{ Exception -> 0x00e9 }
        if (r14 == 0) goto L_0x0083;	 Catch:{ Exception -> 0x00e9 }
        r9 = "<";	 Catch:{ Exception -> 0x00e9 }
        r10 = "&lt;";	 Catch:{ Exception -> 0x00e9 }
        r9 = r7.replace(r9, r10);	 Catch:{ Exception -> 0x00e9 }
        r10 = ">";	 Catch:{ Exception -> 0x00e9 }
        r11 = "&gt;";	 Catch:{ Exception -> 0x00e9 }
        r9 = r9.replace(r10, r11);	 Catch:{ Exception -> 0x00e9 }
        r10 = "'";	 Catch:{ Exception -> 0x00e9 }
        r11 = "\\'";	 Catch:{ Exception -> 0x00e9 }
        r9 = r9.replace(r10, r11);	 Catch:{ Exception -> 0x00e9 }
        r10 = "& ";	 Catch:{ Exception -> 0x00e9 }
        r11 = "&amp; ";	 Catch:{ Exception -> 0x00e9 }
        r9 = r9.replace(r10, r11);	 Catch:{ Exception -> 0x00e9 }
        r7 = r9;	 Catch:{ Exception -> 0x00e9 }
        goto L_0x00b2;	 Catch:{ Exception -> 0x00e9 }
        r9 = "\\n";	 Catch:{ Exception -> 0x00e9 }
        r10 = "\n";	 Catch:{ Exception -> 0x00e9 }
        r9 = r7.replace(r9, r10);	 Catch:{ Exception -> 0x00e9 }
        r7 = r9;	 Catch:{ Exception -> 0x00e9 }
        r9 = "\\";	 Catch:{ Exception -> 0x00e9 }
        r10 = "";	 Catch:{ Exception -> 0x00e9 }
        r9 = r7.replace(r9, r10);	 Catch:{ Exception -> 0x00e9 }
        r7 = r9;	 Catch:{ Exception -> 0x00e9 }
        r10 = "&lt;";	 Catch:{ Exception -> 0x00e9 }
        r11 = "<";	 Catch:{ Exception -> 0x00e9 }
        r10 = r7.replace(r10, r11);	 Catch:{ Exception -> 0x00e9 }
        r7 = r10;	 Catch:{ Exception -> 0x00e9 }
        r10 = r12.reloadLastFile;	 Catch:{ Exception -> 0x00e9 }
        if (r10 != 0) goto L_0x00ab;	 Catch:{ Exception -> 0x00e9 }
        r10 = r7.equals(r9);	 Catch:{ Exception -> 0x00e9 }
        if (r10 != 0) goto L_0x00ab;	 Catch:{ Exception -> 0x00e9 }
        r12.reloadLastFile = r2;	 Catch:{ Exception -> 0x00e9 }
        goto L_0x00b2;	 Catch:{ Exception -> 0x00e9 }
        r9 = 3;	 Catch:{ Exception -> 0x00e9 }
        if (r5 != r9) goto L_0x00b2;	 Catch:{ Exception -> 0x00e9 }
        r7 = 0;	 Catch:{ Exception -> 0x00e9 }
        r8 = 0;	 Catch:{ Exception -> 0x00e9 }
        r6 = 0;	 Catch:{ Exception -> 0x00e9 }
        if (r6 == 0) goto L_0x00d2;	 Catch:{ Exception -> 0x00e9 }
        r9 = "string";	 Catch:{ Exception -> 0x00e9 }
        r9 = r6.equals(r9);	 Catch:{ Exception -> 0x00e9 }
        if (r9 == 0) goto L_0x00d2;	 Catch:{ Exception -> 0x00e9 }
        if (r7 == 0) goto L_0x00d2;	 Catch:{ Exception -> 0x00e9 }
        if (r8 == 0) goto L_0x00d2;	 Catch:{ Exception -> 0x00e9 }
        r9 = r7.length();	 Catch:{ Exception -> 0x00e9 }
        if (r9 == 0) goto L_0x00d2;	 Catch:{ Exception -> 0x00e9 }
        r9 = r8.length();	 Catch:{ Exception -> 0x00e9 }
        if (r9 == 0) goto L_0x00d2;	 Catch:{ Exception -> 0x00e9 }
        r3.put(r8, r7);	 Catch:{ Exception -> 0x00e9 }
        r6 = 0;	 Catch:{ Exception -> 0x00e9 }
        r7 = 0;	 Catch:{ Exception -> 0x00e9 }
        r8 = 0;	 Catch:{ Exception -> 0x00e9 }
        r9 = r4.next();	 Catch:{ Exception -> 0x00e9 }
        r5 = r9;
        goto L_0x0038;
        if (r0 == 0) goto L_0x00e5;
        r0.close();	 Catch:{ Exception -> 0x00e0 }
        goto L_0x00e5;
    L_0x00e0:
        r1 = move-exception;
        org.telegram.messenger.FileLog.e(r1);
        goto L_0x00e6;
        return r3;
    L_0x00e7:
        r1 = move-exception;
        goto L_0x0101;
    L_0x00e9:
        r1 = move-exception;
        org.telegram.messenger.FileLog.e(r1);	 Catch:{ all -> 0x00e7 }
        r12.reloadLastFile = r2;	 Catch:{ all -> 0x00e7 }
        if (r0 == 0) goto L_0x00fa;
        r0.close();	 Catch:{ Exception -> 0x00f5 }
        goto L_0x00fa;
    L_0x00f5:
        r1 = move-exception;
        org.telegram.messenger.FileLog.e(r1);
        goto L_0x00fb;
        r1 = new java.util.HashMap;
        r1.<init>();
        return r1;
        if (r0 == 0) goto L_0x010d;
        r0.close();	 Catch:{ Exception -> 0x0108 }
        goto L_0x010d;
    L_0x0108:
        r2 = move-exception;
        org.telegram.messenger.FileLog.e(r2);
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.LocaleController.getLocaleFileStrings(java.io.File, boolean):java.util.HashMap<java.lang.String, java.lang.String>");
    }

    public java.lang.String formatCurrencyDecimalString(long r1, java.lang.String r3, boolean r4) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.LocaleController.formatCurrencyDecimalString(long, java.lang.String, boolean):java.lang.String
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
        r0 = this;
        r12 = r12.toUpperCase();
        r10 = java.lang.Math.abs(r10);
        r0 = r12.hashCode();
        r1 = 0;
        r2 = 1;
        switch(r0) {
            case 65726: goto L_0x0160;
            case 65759: goto L_0x0155;
            case 66267: goto L_0x014a;
            case 66813: goto L_0x0140;
            case 66823: goto L_0x0135;
            case 67122: goto L_0x012a;
            case 67712: goto L_0x011f;
            case 70719: goto L_0x0114;
            case 72732: goto L_0x010a;
            case 72777: goto L_0x00ff;
            case 72801: goto L_0x00f3;
            case 73631: goto L_0x00e8;
            case 73683: goto L_0x00dc;
            case 74532: goto L_0x00d0;
            case 74704: goto L_0x00c4;
            case 74840: goto L_0x00b9;
            case 75863: goto L_0x00ae;
            case 76263: goto L_0x00a2;
            case 76618: goto L_0x0096;
            case 78388: goto L_0x008b;
            case 79710: goto L_0x007f;
            case 81569: goto L_0x0073;
            case 83210: goto L_0x0067;
            case 83974: goto L_0x005b;
            case 84517: goto L_0x004f;
            case 85132: goto L_0x0043;
            case 85367: goto L_0x0037;
            case 86653: goto L_0x002b;
            case 87087: goto L_0x001f;
            case 87118: goto L_0x0013;
            default: goto L_0x0011;
        };
    L_0x0011:
        goto L_0x016a;
    L_0x0013:
        r0 = "XPF";
        r0 = r12.equals(r0);
        if (r0 == 0) goto L_0x016a;
    L_0x001b:
        r0 = 28;
        goto L_0x016b;
    L_0x001f:
        r0 = "XOF";
        r0 = r12.equals(r0);
        if (r0 == 0) goto L_0x016a;
    L_0x0027:
        r0 = 27;
        goto L_0x016b;
    L_0x002b:
        r0 = "XAF";
        r0 = r12.equals(r0);
        if (r0 == 0) goto L_0x016a;
    L_0x0033:
        r0 = 26;
        goto L_0x016b;
    L_0x0037:
        r0 = "VUV";
        r0 = r12.equals(r0);
        if (r0 == 0) goto L_0x016a;
    L_0x003f:
        r0 = 25;
        goto L_0x016b;
    L_0x0043:
        r0 = "VND";
        r0 = r12.equals(r0);
        if (r0 == 0) goto L_0x016a;
    L_0x004b:
        r0 = 24;
        goto L_0x016b;
    L_0x004f:
        r0 = "UYI";
        r0 = r12.equals(r0);
        if (r0 == 0) goto L_0x016a;
    L_0x0057:
        r0 = 23;
        goto L_0x016b;
    L_0x005b:
        r0 = "UGX";
        r0 = r12.equals(r0);
        if (r0 == 0) goto L_0x016a;
    L_0x0063:
        r0 = 22;
        goto L_0x016b;
    L_0x0067:
        r0 = "TND";
        r0 = r12.equals(r0);
        if (r0 == 0) goto L_0x016a;
    L_0x006f:
        r0 = 8;
        goto L_0x016b;
    L_0x0073:
        r0 = "RWF";
        r0 = r12.equals(r0);
        if (r0 == 0) goto L_0x016a;
    L_0x007b:
        r0 = 21;
        goto L_0x016b;
    L_0x007f:
        r0 = "PYG";
        r0 = r12.equals(r0);
        if (r0 == 0) goto L_0x016a;
    L_0x0087:
        r0 = 20;
        goto L_0x016b;
    L_0x008b:
        r0 = "OMR";
        r0 = r12.equals(r0);
        if (r0 == 0) goto L_0x016a;
    L_0x0093:
        r0 = 7;
        goto L_0x016b;
    L_0x0096:
        r0 = "MRO";
        r0 = r12.equals(r0);
        if (r0 == 0) goto L_0x016a;
    L_0x009e:
        r0 = 29;
        goto L_0x016b;
    L_0x00a2:
        r0 = "MGA";
        r0 = r12.equals(r0);
        if (r0 == 0) goto L_0x016a;
    L_0x00aa:
        r0 = 19;
        goto L_0x016b;
    L_0x00ae:
        r0 = "LYD";
        r0 = r12.equals(r0);
        if (r0 == 0) goto L_0x016a;
    L_0x00b6:
        r0 = 6;
        goto L_0x016b;
    L_0x00b9:
        r0 = "KWD";
        r0 = r12.equals(r0);
        if (r0 == 0) goto L_0x016a;
    L_0x00c1:
        r0 = 5;
        goto L_0x016b;
    L_0x00c4:
        r0 = "KRW";
        r0 = r12.equals(r0);
        if (r0 == 0) goto L_0x016a;
    L_0x00cc:
        r0 = 18;
        goto L_0x016b;
    L_0x00d0:
        r0 = "KMF";
        r0 = r12.equals(r0);
        if (r0 == 0) goto L_0x016a;
    L_0x00d8:
        r0 = 17;
        goto L_0x016b;
    L_0x00dc:
        r0 = "JPY";
        r0 = r12.equals(r0);
        if (r0 == 0) goto L_0x016a;
    L_0x00e4:
        r0 = 16;
        goto L_0x016b;
    L_0x00e8:
        r0 = "JOD";
        r0 = r12.equals(r0);
        if (r0 == 0) goto L_0x016a;
    L_0x00f0:
        r0 = 4;
        goto L_0x016b;
    L_0x00f3:
        r0 = "ISK";
        r0 = r12.equals(r0);
        if (r0 == 0) goto L_0x016a;
    L_0x00fb:
        r0 = 15;
        goto L_0x016b;
    L_0x00ff:
        r0 = "IRR";
        r0 = r12.equals(r0);
        if (r0 == 0) goto L_0x016a;
    L_0x0107:
        r0 = r2;
        goto L_0x016b;
    L_0x010a:
        r0 = "IQD";
        r0 = r12.equals(r0);
        if (r0 == 0) goto L_0x016a;
    L_0x0112:
        r0 = 3;
        goto L_0x016b;
    L_0x0114:
        r0 = "GNF";
        r0 = r12.equals(r0);
        if (r0 == 0) goto L_0x016a;
    L_0x011c:
        r0 = 14;
        goto L_0x016b;
    L_0x011f:
        r0 = "DJF";
        r0 = r12.equals(r0);
        if (r0 == 0) goto L_0x016a;
    L_0x0127:
        r0 = 13;
        goto L_0x016b;
    L_0x012a:
        r0 = "CVE";
        r0 = r12.equals(r0);
        if (r0 == 0) goto L_0x016a;
    L_0x0132:
        r0 = 12;
        goto L_0x016b;
    L_0x0135:
        r0 = "CLP";
        r0 = r12.equals(r0);
        if (r0 == 0) goto L_0x016a;
    L_0x013d:
        r0 = 11;
        goto L_0x016b;
    L_0x0140:
        r0 = "CLF";
        r0 = r12.equals(r0);
        if (r0 == 0) goto L_0x016a;
    L_0x0148:
        r0 = r1;
        goto L_0x016b;
    L_0x014a:
        r0 = "BYR";
        r0 = r12.equals(r0);
        if (r0 == 0) goto L_0x016a;
    L_0x0152:
        r0 = 10;
        goto L_0x016b;
    L_0x0155:
        r0 = "BIF";
        r0 = r12.equals(r0);
        if (r0 == 0) goto L_0x016a;
    L_0x015d:
        r0 = 9;
        goto L_0x016b;
    L_0x0160:
        r0 = "BHD";
        r0 = r12.equals(r0);
        if (r0 == 0) goto L_0x016a;
    L_0x0168:
        r0 = 2;
        goto L_0x016b;
    L_0x016a:
        r0 = -1;
    L_0x016b:
        switch(r0) {
            case 0: goto L_0x019f;
            case 1: goto L_0x018a;
            case 2: goto L_0x0180;
            case 3: goto L_0x0180;
            case 4: goto L_0x0180;
            case 5: goto L_0x0180;
            case 6: goto L_0x0180;
            case 7: goto L_0x0180;
            case 8: goto L_0x0180;
            case 9: goto L_0x017c;
            case 10: goto L_0x017c;
            case 11: goto L_0x017c;
            case 12: goto L_0x017c;
            case 13: goto L_0x017c;
            case 14: goto L_0x017c;
            case 15: goto L_0x017c;
            case 16: goto L_0x017c;
            case 17: goto L_0x017c;
            case 18: goto L_0x017c;
            case 19: goto L_0x017c;
            case 20: goto L_0x017c;
            case 21: goto L_0x017c;
            case 22: goto L_0x017c;
            case 23: goto L_0x017c;
            case 24: goto L_0x017c;
            case 25: goto L_0x017c;
            case 26: goto L_0x017c;
            case 27: goto L_0x017c;
            case 28: goto L_0x017c;
            case 29: goto L_0x0175;
            default: goto L_0x016e;
        };
    L_0x016e:
        r0 = " %.2f";
        r3 = (double) r10;
        r5 = 4636737291354636288; // 0x4059000000000000 float:0.0 double:100.0;
        r3 = r3 / r5;
        goto L_0x01a9;
    L_0x0175:
        r0 = " %.1f";
        r3 = (double) r10;
        r5 = 4621819117588971520; // 0x4024000000000000 float:0.0 double:10.0;
        r3 = r3 / r5;
        goto L_0x01a9;
    L_0x017c:
        r0 = " %.0f";
        r3 = (double) r10;
        goto L_0x01a9;
    L_0x0180:
        r0 = " %.3f";
        r3 = (double) r10;
        r5 = 4652007308841189376; // 0x408f400000000000 float:0.0 double:1000.0;
        r3 = r3 / r5;
        goto L_0x01a9;
    L_0x018a:
        r0 = (float) r10;
        r3 = 1120403456; // 0x42c80000 float:100.0 double:5.53552857E-315;
        r0 = r0 / r3;
        r3 = (double) r0;
        r5 = 100;
        r5 = r10 % r5;
        r7 = 0;
        r0 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1));
        if (r0 != 0) goto L_0x019c;
        r0 = " %.0f";
        goto L_0x01a9;
        r0 = " %.2f";
        goto L_0x01a9;
    L_0x019f:
        r0 = " %.4f";
        r3 = (double) r10;
        r5 = 4666723172467343360; // 0x40c3880000000000 float:0.0 double:10000.0;
        r3 = r3 / r5;
        r5 = java.util.Locale.US;
        if (r13 == 0) goto L_0x01b0;
        r6 = r12;
        goto L_0x01c1;
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r7 = "";
        r6.append(r7);
        r6.append(r0);
        r6 = r6.toString();
        r2 = new java.lang.Object[r2];
        r7 = java.lang.Double.valueOf(r3);
        r2[r1] = r7;
        r1 = java.lang.String.format(r5, r6, r2);
        r1 = r1.trim();
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.LocaleController.formatCurrencyDecimalString(long, java.lang.String, boolean):java.lang.String");
    }

    public java.lang.String formatCurrencyString(long r1, java.lang.String r3) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.LocaleController.formatCurrencyString(long, java.lang.String):java.lang.String
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
        r0 = this;
        r14 = r14.toUpperCase();
        r0 = 0;
        r2 = (r12 > r0 ? 1 : (r12 == r0 ? 0 : -1));
        r3 = 1;
        r4 = 0;
        if (r2 >= 0) goto L_0x000e;
    L_0x000c:
        r2 = r3;
        goto L_0x000f;
    L_0x000e:
        r2 = r4;
    L_0x000f:
        r12 = java.lang.Math.abs(r12);
        r5 = java.util.Currency.getInstance(r14);
        r6 = -1;
        r7 = r14.hashCode();
        switch(r7) {
            case 65726: goto L_0x016d;
            case 65759: goto L_0x0162;
            case 66267: goto L_0x0157;
            case 66813: goto L_0x014d;
            case 66823: goto L_0x0142;
            case 67122: goto L_0x0137;
            case 67712: goto L_0x012c;
            case 70719: goto L_0x0121;
            case 72732: goto L_0x0117;
            case 72777: goto L_0x010d;
            case 72801: goto L_0x0101;
            case 73631: goto L_0x00f6;
            case 73683: goto L_0x00ea;
            case 74532: goto L_0x00de;
            case 74704: goto L_0x00d2;
            case 74840: goto L_0x00c7;
            case 75863: goto L_0x00bc;
            case 76263: goto L_0x00b0;
            case 76618: goto L_0x00a4;
            case 78388: goto L_0x0099;
            case 79710: goto L_0x008d;
            case 81569: goto L_0x0081;
            case 83210: goto L_0x0075;
            case 83974: goto L_0x0069;
            case 84517: goto L_0x005d;
            case 85132: goto L_0x0051;
            case 85367: goto L_0x0045;
            case 86653: goto L_0x0039;
            case 87087: goto L_0x002d;
            case 87118: goto L_0x0021;
            default: goto L_0x001f;
        };
    L_0x001f:
        goto L_0x0176;
    L_0x0021:
        r7 = "XPF";
        r7 = r14.equals(r7);
        if (r7 == 0) goto L_0x0176;
    L_0x0029:
        r6 = 28;
        goto L_0x0176;
    L_0x002d:
        r7 = "XOF";
        r7 = r14.equals(r7);
        if (r7 == 0) goto L_0x0176;
    L_0x0035:
        r6 = 27;
        goto L_0x0176;
    L_0x0039:
        r7 = "XAF";
        r7 = r14.equals(r7);
        if (r7 == 0) goto L_0x0176;
    L_0x0041:
        r6 = 26;
        goto L_0x0176;
    L_0x0045:
        r7 = "VUV";
        r7 = r14.equals(r7);
        if (r7 == 0) goto L_0x0176;
    L_0x004d:
        r6 = 25;
        goto L_0x0176;
    L_0x0051:
        r7 = "VND";
        r7 = r14.equals(r7);
        if (r7 == 0) goto L_0x0176;
    L_0x0059:
        r6 = 24;
        goto L_0x0176;
    L_0x005d:
        r7 = "UYI";
        r7 = r14.equals(r7);
        if (r7 == 0) goto L_0x0176;
    L_0x0065:
        r6 = 23;
        goto L_0x0176;
    L_0x0069:
        r7 = "UGX";
        r7 = r14.equals(r7);
        if (r7 == 0) goto L_0x0176;
    L_0x0071:
        r6 = 22;
        goto L_0x0176;
    L_0x0075:
        r7 = "TND";
        r7 = r14.equals(r7);
        if (r7 == 0) goto L_0x0176;
    L_0x007d:
        r6 = 8;
        goto L_0x0176;
    L_0x0081:
        r7 = "RWF";
        r7 = r14.equals(r7);
        if (r7 == 0) goto L_0x0176;
    L_0x0089:
        r6 = 21;
        goto L_0x0176;
    L_0x008d:
        r7 = "PYG";
        r7 = r14.equals(r7);
        if (r7 == 0) goto L_0x0176;
    L_0x0095:
        r6 = 20;
        goto L_0x0176;
    L_0x0099:
        r7 = "OMR";
        r7 = r14.equals(r7);
        if (r7 == 0) goto L_0x0176;
    L_0x00a1:
        r6 = 7;
        goto L_0x0176;
    L_0x00a4:
        r7 = "MRO";
        r7 = r14.equals(r7);
        if (r7 == 0) goto L_0x0176;
    L_0x00ac:
        r6 = 29;
        goto L_0x0176;
    L_0x00b0:
        r7 = "MGA";
        r7 = r14.equals(r7);
        if (r7 == 0) goto L_0x0176;
    L_0x00b8:
        r6 = 19;
        goto L_0x0176;
    L_0x00bc:
        r7 = "LYD";
        r7 = r14.equals(r7);
        if (r7 == 0) goto L_0x0176;
    L_0x00c4:
        r6 = 6;
        goto L_0x0176;
    L_0x00c7:
        r7 = "KWD";
        r7 = r14.equals(r7);
        if (r7 == 0) goto L_0x0176;
    L_0x00cf:
        r6 = 5;
        goto L_0x0176;
    L_0x00d2:
        r7 = "KRW";
        r7 = r14.equals(r7);
        if (r7 == 0) goto L_0x0176;
    L_0x00da:
        r6 = 18;
        goto L_0x0176;
    L_0x00de:
        r7 = "KMF";
        r7 = r14.equals(r7);
        if (r7 == 0) goto L_0x0176;
    L_0x00e6:
        r6 = 17;
        goto L_0x0176;
    L_0x00ea:
        r7 = "JPY";
        r7 = r14.equals(r7);
        if (r7 == 0) goto L_0x0176;
    L_0x00f2:
        r6 = 16;
        goto L_0x0176;
    L_0x00f6:
        r7 = "JOD";
        r7 = r14.equals(r7);
        if (r7 == 0) goto L_0x0176;
    L_0x00fe:
        r6 = 4;
        goto L_0x0176;
    L_0x0101:
        r7 = "ISK";
        r7 = r14.equals(r7);
        if (r7 == 0) goto L_0x0176;
    L_0x0109:
        r6 = 15;
        goto L_0x0176;
    L_0x010d:
        r7 = "IRR";
        r7 = r14.equals(r7);
        if (r7 == 0) goto L_0x0176;
    L_0x0115:
        r6 = r3;
        goto L_0x0176;
    L_0x0117:
        r7 = "IQD";
        r7 = r14.equals(r7);
        if (r7 == 0) goto L_0x0176;
    L_0x011f:
        r6 = 3;
        goto L_0x0176;
    L_0x0121:
        r7 = "GNF";
        r7 = r14.equals(r7);
        if (r7 == 0) goto L_0x0176;
    L_0x0129:
        r6 = 14;
        goto L_0x0176;
    L_0x012c:
        r7 = "DJF";
        r7 = r14.equals(r7);
        if (r7 == 0) goto L_0x0176;
    L_0x0134:
        r6 = 13;
        goto L_0x0176;
    L_0x0137:
        r7 = "CVE";
        r7 = r14.equals(r7);
        if (r7 == 0) goto L_0x0176;
    L_0x013f:
        r6 = 12;
        goto L_0x0176;
    L_0x0142:
        r7 = "CLP";
        r7 = r14.equals(r7);
        if (r7 == 0) goto L_0x0176;
    L_0x014a:
        r6 = 11;
        goto L_0x0176;
    L_0x014d:
        r7 = "CLF";
        r7 = r14.equals(r7);
        if (r7 == 0) goto L_0x0176;
    L_0x0155:
        r6 = r4;
        goto L_0x0176;
    L_0x0157:
        r7 = "BYR";
        r7 = r14.equals(r7);
        if (r7 == 0) goto L_0x0176;
    L_0x015f:
        r6 = 10;
        goto L_0x0176;
    L_0x0162:
        r7 = "BIF";
        r7 = r14.equals(r7);
        if (r7 == 0) goto L_0x0176;
    L_0x016a:
        r6 = 9;
        goto L_0x0176;
    L_0x016d:
        r7 = "BHD";
        r7 = r14.equals(r7);
        if (r7 == 0) goto L_0x0176;
    L_0x0175:
        r6 = 2;
    L_0x0176:
        switch(r6) {
            case 0: goto L_0x01a8;
            case 1: goto L_0x0195;
            case 2: goto L_0x018b;
            case 3: goto L_0x018b;
            case 4: goto L_0x018b;
            case 5: goto L_0x018b;
            case 6: goto L_0x018b;
            case 7: goto L_0x018b;
            case 8: goto L_0x018b;
            case 9: goto L_0x0187;
            case 10: goto L_0x0187;
            case 11: goto L_0x0187;
            case 12: goto L_0x0187;
            case 13: goto L_0x0187;
            case 14: goto L_0x0187;
            case 15: goto L_0x0187;
            case 16: goto L_0x0187;
            case 17: goto L_0x0187;
            case 18: goto L_0x0187;
            case 19: goto L_0x0187;
            case 20: goto L_0x0187;
            case 21: goto L_0x0187;
            case 22: goto L_0x0187;
            case 23: goto L_0x0187;
            case 24: goto L_0x0187;
            case 25: goto L_0x0187;
            case 26: goto L_0x0187;
            case 27: goto L_0x0187;
            case 28: goto L_0x0187;
            case 29: goto L_0x0180;
            default: goto L_0x0179;
        };
    L_0x0179:
        r0 = " %.2f";
        r6 = (double) r12;
        r8 = 4636737291354636288; // 0x4059000000000000 float:0.0 double:100.0;
        r6 = r6 / r8;
        goto L_0x01b2;
    L_0x0180:
        r0 = " %.1f";
        r6 = (double) r12;
        r8 = 4621819117588971520; // 0x4024000000000000 float:0.0 double:10.0;
        r6 = r6 / r8;
        goto L_0x01b2;
    L_0x0187:
        r0 = " %.0f";
        r6 = (double) r12;
        goto L_0x01b2;
    L_0x018b:
        r0 = " %.3f";
        r6 = (double) r12;
        r8 = 4652007308841189376; // 0x408f400000000000 float:0.0 double:1000.0;
        r6 = r6 / r8;
        goto L_0x01b2;
    L_0x0195:
        r6 = (float) r12;
        r7 = 1120403456; // 0x42c80000 float:100.0 double:5.53552857E-315;
        r6 = r6 / r7;
        r6 = (double) r6;
        r8 = 100;
        r8 = r12 % r8;
        r10 = (r8 > r0 ? 1 : (r8 == r0 ? 0 : -1));
        if (r10 != 0) goto L_0x01a5;
        r0 = " %.0f";
        goto L_0x01b2;
        r0 = " %.2f";
        goto L_0x01b2;
    L_0x01a8:
        r0 = " %.4f";
        r6 = (double) r12;
        r8 = 4666723172467343360; // 0x40c3880000000000 float:0.0 double:10000.0;
        r6 = r6 / r8;
        if (r5 == 0) goto L_0x01eb;
        r1 = r11.currentLocale;
        if (r1 == 0) goto L_0x01bc;
        r1 = r11.currentLocale;
        goto L_0x01be;
        r1 = r11.systemDefaultLocale;
        r1 = java.text.NumberFormat.getCurrencyInstance(r1);
        r1.setCurrency(r5);
        r3 = "IRR";
        r3 = r14.equals(r3);
        if (r3 == 0) goto L_0x01d0;
        r1.setMaximumFractionDigits(r4);
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        if (r2 == 0) goto L_0x01da;
        r4 = "-";
        goto L_0x01dc;
        r4 = "";
        r3.append(r4);
        r4 = r1.format(r6);
        r3.append(r4);
        r3 = r3.toString();
        return r3;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        if (r2 == 0) goto L_0x01f5;
        r8 = "-";
        goto L_0x01f7;
        r8 = "";
        r1.append(r8);
        r8 = java.util.Locale.US;
        r9 = new java.lang.StringBuilder;
        r9.<init>();
        r9.append(r14);
        r9.append(r0);
        r9 = r9.toString();
        r3 = new java.lang.Object[r3];
        r10 = java.lang.Double.valueOf(r6);
        r3[r4] = r10;
        r3 = java.lang.String.format(r8, r9, r3);
        r1.append(r3);
        r1 = r1.toString();
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.LocaleController.formatCurrencyString(long, java.lang.String):java.lang.String");
    }

    public static LocaleController getInstance() {
        LocaleController localInstance = Instance;
        if (localInstance == null) {
            synchronized (LocaleController.class) {
                localInstance = Instance;
                if (localInstance == null) {
                    LocaleController localeController = new LocaleController();
                    localInstance = localeController;
                    Instance = localeController;
                }
            }
        }
        return localInstance;
    }

    public LocaleController() {
        int a;
        boolean override = false;
        this.changingConfiguration = false;
        this.languages = new ArrayList();
        this.remoteLanguages = new ArrayList();
        this.languagesDict = new HashMap();
        this.otherLanguages = new ArrayList();
        addRules(new String[]{"bem", "brx", "da", "de", "el", "en", "eo", "es", "et", "fi", "fo", "gl", "he", "iw", "it", "nb", "nl", "nn", "no", "sv", "af", "bg", "bn", "ca", "eu", "fur", "fy", "gu", "ha", "is", "ku", "lb", "ml", "mr", "nah", "ne", "om", "or", "pa", "pap", "ps", "so", "sq", "sw", "ta", "te", "tk", "ur", "zu", "mn", "gsw", "chr", "rm", "pt", "an", "ast"}, new PluralRules_One());
        addRules(new String[]{"cs", "sk"}, new PluralRules_Czech());
        addRules(new String[]{"ff", "fr", "kab"}, new PluralRules_French());
        addRules(new String[]{"hr", "ru", "sr", "uk", "be", "bs", "sh"}, new PluralRules_Balkan());
        addRules(new String[]{"lv"}, new PluralRules_Latvian());
        addRules(new String[]{"lt"}, new PluralRules_Lithuanian());
        addRules(new String[]{"pl"}, new PluralRules_Polish());
        addRules(new String[]{"ro", "mo"}, new PluralRules_Romanian());
        addRules(new String[]{"sl"}, new PluralRules_Slovenian());
        addRules(new String[]{"ar"}, new PluralRules_Arabic());
        addRules(new String[]{"mk"}, new PluralRules_Macedonian());
        addRules(new String[]{"cy"}, new PluralRules_Welsh());
        addRules(new String[]{TtmlNode.TAG_BR}, new PluralRules_Breton());
        addRules(new String[]{"lag"}, new PluralRules_Langi());
        addRules(new String[]{"shi"}, new PluralRules_Tachelhit());
        addRules(new String[]{"mt"}, new PluralRules_Maltese());
        addRules(new String[]{"ga", "se", "sma", "smi", "smj", "smn", "sms"}, new PluralRules_Two());
        addRules(new String[]{"ak", "am", "bh", "fil", "tl", "guw", "hi", "ln", "mg", "nso", "ti", "wa"}, new PluralRules_Zero());
        addRules(new String[]{"az", "bm", "fa", "ig", "hu", "ja", "kde", "kea", "ko", "my", "ses", "sg", "to", "tr", "vi", "wo", "yo", "zh", "bo", "dz", TtmlNode.ATTR_ID, "jv", "jw", "ka", "km", "kn", "ms", "th", "in"}, new PluralRules_None());
        LocaleInfo localeInfo = new LocaleInfo();
        localeInfo.name = "English";
        localeInfo.nameEnglish = "English";
        localeInfo.shortName = "en";
        localeInfo.pathToFile = null;
        localeInfo.builtIn = true;
        this.languages.add(localeInfo);
        this.languagesDict.put(localeInfo.shortName, localeInfo);
        localeInfo = new LocaleInfo();
        localeInfo.name = "Italiano";
        localeInfo.nameEnglish = "Italian";
        localeInfo.shortName = "it";
        localeInfo.pathToFile = null;
        localeInfo.builtIn = true;
        this.languages.add(localeInfo);
        this.languagesDict.put(localeInfo.shortName, localeInfo);
        localeInfo = new LocaleInfo();
        localeInfo.name = "Español";
        localeInfo.nameEnglish = "Spanish";
        localeInfo.shortName = "es";
        localeInfo.builtIn = true;
        this.languages.add(localeInfo);
        this.languagesDict.put(localeInfo.shortName, localeInfo);
        localeInfo = new LocaleInfo();
        localeInfo.name = "Deutsch";
        localeInfo.nameEnglish = "German";
        localeInfo.shortName = "de";
        localeInfo.pathToFile = null;
        localeInfo.builtIn = true;
        this.languages.add(localeInfo);
        this.languagesDict.put(localeInfo.shortName, localeInfo);
        localeInfo = new LocaleInfo();
        localeInfo.name = "Nederlands";
        localeInfo.nameEnglish = "Dutch";
        localeInfo.shortName = "nl";
        localeInfo.pathToFile = null;
        localeInfo.builtIn = true;
        this.languages.add(localeInfo);
        this.languagesDict.put(localeInfo.shortName, localeInfo);
        localeInfo = new LocaleInfo();
        localeInfo.name = "العربية";
        localeInfo.nameEnglish = "Arabic";
        localeInfo.shortName = "ar";
        localeInfo.pathToFile = null;
        localeInfo.builtIn = true;
        this.languages.add(localeInfo);
        this.languagesDict.put(localeInfo.shortName, localeInfo);
        localeInfo = new LocaleInfo();
        localeInfo.name = "Português (Brasil)";
        localeInfo.nameEnglish = "Portuguese (Brazil)";
        localeInfo.shortName = "pt_br";
        localeInfo.pathToFile = null;
        localeInfo.builtIn = true;
        this.languages.add(localeInfo);
        this.languagesDict.put(localeInfo.shortName, localeInfo);
        localeInfo = new LocaleInfo();
        localeInfo.name = "한국어";
        localeInfo.nameEnglish = "Korean";
        localeInfo.shortName = "ko";
        localeInfo.pathToFile = null;
        localeInfo.builtIn = true;
        this.languages.add(localeInfo);
        this.languagesDict.put(localeInfo.shortName, localeInfo);
        loadOtherLanguages();
        if (this.remoteLanguages.isEmpty()) {
            AndroidUtilities.runOnUIThread(new Runnable() {
                public void run() {
                    LocaleController.this.loadRemoteLanguages(UserConfig.selectedAccount);
                }
            });
        }
        for (a = 0; a < r1.otherLanguages.size(); a++) {
            LocaleInfo locale = (LocaleInfo) r1.otherLanguages.get(a);
            r1.languages.add(locale);
            r1.languagesDict.put(locale.getKey(), locale);
        }
        for (a = 0; a < r1.remoteLanguages.size(); a++) {
            locale = (LocaleInfo) r1.remoteLanguages.get(a);
            LocaleInfo existingLocale = getLanguageFromDict(locale.getKey());
            if (existingLocale != null) {
                existingLocale.pathToFile = locale.pathToFile;
                existingLocale.version = locale.version;
                r1.remoteLanguages.set(a, existingLocale);
            } else {
                r1.languages.add(locale);
                r1.languagesDict.put(locale.getKey(), locale);
            }
        }
        r1.systemDefaultLocale = Locale.getDefault();
        is24HourFormat = DateFormat.is24HourFormat(ApplicationLoader.applicationContext);
        LocaleInfo currentInfo = null;
        try {
            String lang = MessagesController.getGlobalMainSettings().getString("language", null);
            if (lang != null) {
                currentInfo = getLanguageFromDict(lang);
                if (currentInfo != null) {
                    override = true;
                }
            }
            if (currentInfo == null && r1.systemDefaultLocale.getLanguage() != null) {
                currentInfo = getLanguageFromDict(r1.systemDefaultLocale.getLanguage());
            }
            if (currentInfo == null) {
                currentInfo = getLanguageFromDict(getLocaleString(r1.systemDefaultLocale));
                if (currentInfo == null) {
                    currentInfo = getLanguageFromDict("en");
                }
            }
            applyLanguage(currentInfo, override, true, UserConfig.selectedAccount);
        } catch (Throwable e) {
            boolean override2 = false;
            FileLog.e(e);
        }
        try {
            ApplicationLoader.applicationContext.registerReceiver(new TimeZoneChangedReceiver(), new IntentFilter("android.intent.action.TIMEZONE_CHANGED"));
        } catch (Throwable e2) {
            FileLog.e(e2);
        }
    }

    private LocaleInfo getLanguageFromDict(String key) {
        if (key == null) {
            return null;
        }
        return (LocaleInfo) this.languagesDict.get(key.toLowerCase().replace("-", "_"));
    }

    private void addRules(String[] languages, PluralRules rules) {
        for (String language : languages) {
            this.allRules.put(language, rules);
        }
    }

    private String stringForQuantity(int quantity) {
        if (quantity == 4) {
            return "two";
        }
        if (quantity == 8) {
            return "few";
        }
        if (quantity == 16) {
            return "many";
        }
        switch (quantity) {
            case 1:
                return "zero";
            case 2:
                return "one";
            default:
                return "other";
        }
    }

    public Locale getSystemDefaultLocale() {
        return this.systemDefaultLocale;
    }

    public boolean isCurrentLocalLocale() {
        return this.currentLocaleInfo.isLocal();
    }

    public void reloadCurrentRemoteLocale(int currentAccount) {
        applyRemoteLanguage(this.currentLocaleInfo, true, currentAccount);
    }

    public void checkUpdateForCurrentRemoteLocale(int currentAccount, int version) {
        if (this.currentLocaleInfo != null) {
            if (this.currentLocaleInfo == null || this.currentLocaleInfo.isRemote()) {
                if (this.currentLocaleInfo.version < version) {
                    applyRemoteLanguage(this.currentLocaleInfo, false, currentAccount);
                }
            }
        }
    }

    private String getLocaleString(Locale locale) {
        if (locale == null) {
            return "en";
        }
        String languageCode = locale.getLanguage();
        String countryCode = locale.getCountry();
        String variantCode = locale.getVariant();
        if (languageCode.length() == 0 && countryCode.length() == 0) {
            return "en";
        }
        StringBuilder result = new StringBuilder(11);
        result.append(languageCode);
        if (countryCode.length() > 0 || variantCode.length() > 0) {
            result.append('_');
        }
        result.append(countryCode);
        if (variantCode.length() > 0) {
            result.append('_');
        }
        result.append(variantCode);
        return result.toString();
    }

    public static String getSystemLocaleStringIso639() {
        Locale locale = getInstance().getSystemDefaultLocale();
        if (locale == null) {
            return "en";
        }
        String languageCode = locale.getLanguage();
        String countryCode = locale.getCountry();
        String variantCode = locale.getVariant();
        if (languageCode.length() == 0 && countryCode.length() == 0) {
            return "en";
        }
        StringBuilder result = new StringBuilder(11);
        result.append(languageCode);
        if (countryCode.length() > 0 || variantCode.length() > 0) {
            result.append('-');
        }
        result.append(countryCode);
        if (variantCode.length() > 0) {
            result.append('_');
        }
        result.append(variantCode);
        return result.toString();
    }

    public static String getLocaleStringIso639() {
        Locale locale = getInstance().currentLocale;
        if (locale == null) {
            return "en";
        }
        String languageCode = locale.getLanguage();
        String countryCode = locale.getCountry();
        String variantCode = locale.getVariant();
        if (languageCode.length() == 0 && countryCode.length() == 0) {
            return "en";
        }
        StringBuilder result = new StringBuilder(11);
        result.append(languageCode);
        if (countryCode.length() > 0 || variantCode.length() > 0) {
            result.append('-');
        }
        result.append(countryCode);
        if (variantCode.length() > 0) {
            result.append('_');
        }
        result.append(variantCode);
        return result.toString();
    }

    public static String getLocaleAlias(String code) {
        if (code == null) {
            return null;
        }
        Object obj = -1;
        switch (code.hashCode()) {
            case 3325:
                if (code.equals("he")) {
                    obj = 7;
                    break;
                }
                break;
            case 3355:
                if (code.equals(TtmlNode.ATTR_ID)) {
                    obj = 6;
                    break;
                }
                break;
            case 3365:
                if (code.equals("in")) {
                    obj = null;
                    break;
                }
                break;
            case 3374:
                if (code.equals("iw")) {
                    obj = 1;
                    break;
                }
                break;
            case 3391:
                if (code.equals("ji")) {
                    obj = 5;
                    break;
                }
                break;
            case 3404:
                if (code.equals("jv")) {
                    obj = 8;
                    break;
                }
                break;
            case 3405:
                if (code.equals("jw")) {
                    obj = 2;
                    break;
                }
                break;
            case 3508:
                if (code.equals("nb")) {
                    obj = 9;
                    break;
                }
                break;
            case 3521:
                if (code.equals("no")) {
                    obj = 3;
                    break;
                }
                break;
            case 3704:
                if (code.equals("tl")) {
                    obj = 4;
                    break;
                }
                break;
            case 3856:
                if (code.equals("yi")) {
                    obj = 11;
                    break;
                }
                break;
            case 101385:
                if (code.equals("fil")) {
                    obj = 10;
                    break;
                }
                break;
            default:
                break;
        }
        switch (obj) {
            case null:
                return TtmlNode.ATTR_ID;
            case 1:
                return "he";
            case 2:
                return "jv";
            case 3:
                return "nb";
            case 4:
                return "fil";
            case 5:
                return "yi";
            case 6:
                return "in";
            case 7:
                return "iw";
            case 8:
                return "jw";
            case 9:
                return "no";
            case 10:
                return "tl";
            case 11:
                return "ji";
            default:
                return null;
        }
    }

    public boolean applyLanguageFile(File file, int currentAccount) {
        Throwable e;
        LocaleController localeController = this;
        File file2;
        try {
            HashMap<String, String> stringMap = getLocaleFileStrings(file);
            String languageName = (String) stringMap.get("LanguageName");
            String languageNameInEnglish = (String) stringMap.get("LanguageNameInEnglish");
            String languageCode = (String) stringMap.get("LanguageCode");
            if (languageName == null || languageName.length() <= 0 || languageNameInEnglish == null || languageNameInEnglish.length() <= 0 || languageCode == null || languageCode.length() <= 0) {
                file2 = file;
                return false;
            }
            if (!languageName.contains("&")) {
                if (!languageName.contains("|")) {
                    if (!languageNameInEnglish.contains("&")) {
                        if (!languageNameInEnglish.contains("|")) {
                            if (!(languageCode.contains("&") || languageCode.contains("|") || languageCode.contains("/"))) {
                                if (!languageCode.contains("\\")) {
                                    File filesDirFixed = ApplicationLoader.getFilesDirFixed();
                                    StringBuilder stringBuilder = new StringBuilder();
                                    stringBuilder.append(languageCode);
                                    stringBuilder.append(".xml");
                                    File finalFile = new File(filesDirFixed, stringBuilder.toString());
                                    try {
                                        if (!AndroidUtilities.copyFile(file, finalFile)) {
                                            return false;
                                        }
                                        StringBuilder stringBuilder2 = new StringBuilder();
                                        stringBuilder2.append("local_");
                                        stringBuilder2.append(languageCode.toLowerCase());
                                        String key = stringBuilder2.toString();
                                        LocaleInfo localeInfo = getLanguageFromDict(key);
                                        if (localeInfo == null) {
                                            localeInfo = new LocaleInfo();
                                            localeInfo.name = languageName;
                                            localeInfo.nameEnglish = languageNameInEnglish;
                                            localeInfo.shortName = languageCode.toLowerCase();
                                            localeInfo.pathToFile = finalFile.getAbsolutePath();
                                            localeController.languages.add(localeInfo);
                                            localeController.languagesDict.put(localeInfo.getKey(), localeInfo);
                                            localeController.otherLanguages.add(localeInfo);
                                            saveOtherLanguages();
                                        }
                                        LocaleInfo localeInfo2 = localeInfo;
                                        localeController.localeValues = stringMap;
                                        applyLanguage(localeInfo2, true, false, true, false, currentAccount);
                                        return true;
                                    } catch (Exception e2) {
                                        e = e2;
                                        FileLog.e(e);
                                        return false;
                                    }
                                }
                            }
                            file2 = file;
                            return false;
                        }
                    }
                    file2 = file;
                    return false;
                }
            }
            file2 = file;
            return false;
        } catch (Exception e3) {
            e = e3;
            file2 = file;
            FileLog.e(e);
            return false;
        }
    }

    private void saveOtherLanguages() {
        int a = 0;
        Editor editor = ApplicationLoader.applicationContext.getSharedPreferences("langconfig", 0).edit();
        StringBuilder stringBuilder = new StringBuilder();
        for (int a2 = 0; a2 < this.otherLanguages.size(); a2++) {
            String loc = ((LocaleInfo) this.otherLanguages.get(a2)).getSaveString();
            if (loc != null) {
                if (stringBuilder.length() != 0) {
                    stringBuilder.append("&");
                }
                stringBuilder.append(loc);
            }
        }
        editor.putString("locales", stringBuilder.toString());
        stringBuilder.setLength(0);
        while (a < this.remoteLanguages.size()) {
            String loc2 = ((LocaleInfo) this.remoteLanguages.get(a)).getSaveString();
            if (loc2 != null) {
                if (stringBuilder.length() != 0) {
                    stringBuilder.append("&");
                }
                stringBuilder.append(loc2);
            }
            a++;
        }
        editor.putString("remote", stringBuilder.toString());
        editor.commit();
    }

    public boolean deleteLanguage(LocaleInfo localeInfo, int currentAccount) {
        if (localeInfo.pathToFile != null) {
            if (!localeInfo.isRemote()) {
                if (this.currentLocaleInfo == localeInfo) {
                    LocaleInfo info = null;
                    if (this.systemDefaultLocale.getLanguage() != null) {
                        info = getLanguageFromDict(this.systemDefaultLocale.getLanguage());
                    }
                    if (info == null) {
                        info = getLanguageFromDict(getLocaleString(this.systemDefaultLocale));
                    }
                    if (info == null) {
                        info = getLanguageFromDict("en");
                    }
                    applyLanguage(info, true, false, currentAccount);
                }
                this.otherLanguages.remove(localeInfo);
                this.languages.remove(localeInfo);
                this.languagesDict.remove(localeInfo.shortName);
                new File(localeInfo.pathToFile).delete();
                saveOtherLanguages();
                return true;
            }
        }
        return false;
    }

    private void loadOtherLanguages() {
        int i = 0;
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("langconfig", 0);
        String locales = preferences.getString("locales", null);
        if (!TextUtils.isEmpty(locales)) {
            for (String locale : locales.split("&")) {
                LocaleInfo localeInfo = LocaleInfo.createWithString(locale);
                if (localeInfo != null) {
                    this.otherLanguages.add(localeInfo);
                }
            }
        }
        locales = preferences.getString("remote", null);
        if (!TextUtils.isEmpty(locales)) {
            String[] localesArr = locales.split("&");
            int length = localesArr.length;
            while (i < length) {
                LocaleInfo localeInfo2 = LocaleInfo.createWithString(localesArr[i]);
                localeInfo2.shortName = localeInfo2.shortName.replace("-", "_");
                if (localeInfo2 != null) {
                    this.remoteLanguages.add(localeInfo2);
                }
                i++;
            }
        }
    }

    private HashMap<String, String> getLocaleFileStrings(File file) {
        return getLocaleFileStrings(file, false);
    }

    public void applyLanguage(LocaleInfo localeInfo, boolean override, boolean init, int currentAccount) {
        applyLanguage(localeInfo, override, init, false, false, currentAccount);
    }

    public void applyLanguage(LocaleInfo localeInfo, boolean override, boolean init, boolean fromFile, boolean force, int currentAccount) {
        LocaleController localeController = this;
        final LocaleInfo localeInfo2 = localeInfo;
        final int i = currentAccount;
        if (localeInfo2 != null) {
            File pathToFile = localeInfo.getPathToFile();
            String shortName = localeInfo2.shortName;
            if (!init) {
                ConnectionsManager.setLangCode(shortName.replace("_", "-"));
            }
            if (localeInfo.isRemote() && (force || !pathToFile.exists())) {
                if (BuildVars.LOGS_ENABLED) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("reload locale because file doesn't exist ");
                    stringBuilder.append(pathToFile);
                    FileLog.d(stringBuilder.toString());
                }
                if (init) {
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        public void run() {
                            LocaleController.this.applyRemoteLanguage(localeInfo2, true, i);
                        }
                    });
                } else {
                    applyRemoteLanguage(localeInfo2, true, i);
                }
            }
            try {
                Locale newLocale;
                String[] args = localeInfo2.shortName.split("_");
                if (args.length == 1) {
                    newLocale = new Locale(localeInfo2.shortName);
                } else {
                    newLocale = new Locale(args[0], args[1]);
                }
                if (override) {
                    localeController.languageOverride = localeInfo2.shortName;
                    Editor editor = MessagesController.getGlobalMainSettings().edit();
                    editor.putString("language", localeInfo.getKey());
                    editor.commit();
                }
                if (pathToFile == null) {
                    localeController.localeValues.clear();
                } else if (!fromFile) {
                    localeController.localeValues = getLocaleFileStrings(pathToFile);
                }
                localeController.currentLocale = newLocale;
                localeController.currentLocaleInfo = localeInfo2;
                localeController.currentPluralRules = (PluralRules) localeController.allRules.get(args[0]);
                if (localeController.currentPluralRules == null) {
                    localeController.currentPluralRules = (PluralRules) localeController.allRules.get(localeController.currentLocale.getLanguage());
                }
                if (localeController.currentPluralRules == null) {
                    localeController.currentPluralRules = new PluralRules_None();
                }
                localeController.changingConfiguration = true;
                Locale.setDefault(localeController.currentLocale);
                Configuration config = new Configuration();
                config.locale = localeController.currentLocale;
                ApplicationLoader.applicationContext.getResources().updateConfiguration(config, ApplicationLoader.applicationContext.getResources().getDisplayMetrics());
                localeController.changingConfiguration = false;
                if (localeController.reloadLastFile) {
                    if (init) {
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            public void run() {
                                LocaleController.this.reloadCurrentRemoteLocale(i);
                            }
                        });
                    } else {
                        reloadCurrentRemoteLocale(i);
                    }
                    localeController.reloadLastFile = false;
                }
            } catch (Throwable e) {
                FileLog.e(e);
                localeController.changingConfiguration = false;
            }
            recreateFormatters();
        }
    }

    public LocaleInfo getCurrentLocaleInfo() {
        return this.currentLocaleInfo;
    }

    public static String getCurrentLanguageName() {
        return getString("LanguageName", R.string.LanguageName);
    }

    private String getStringInternal(String key, int res) {
        String value = (String) this.localeValues.get(key);
        if (value == null) {
            try {
                value = ApplicationLoader.applicationContext.getString(res);
            } catch (Throwable e) {
                FileLog.e(e);
            }
        }
        if (value != null) {
            return value;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("LOC_ERR:");
        stringBuilder.append(key);
        return stringBuilder.toString();
    }

    public static String getString(String key, int res) {
        return getInstance().getStringInternal(key, res);
    }

    public static String getPluralString(String key, int plural) {
        if (!(key == null || key.length() == 0)) {
            if (getInstance().currentPluralRules != null) {
                String param = getInstance().stringForQuantity(getInstance().currentPluralRules.quantityForNumber(plural));
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(key);
                stringBuilder.append("_");
                stringBuilder.append(param);
                param = stringBuilder.toString();
                return getString(param, ApplicationLoader.applicationContext.getResources().getIdentifier(param, "string", ApplicationLoader.applicationContext.getPackageName()));
            }
        }
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("LOC_ERR:");
        stringBuilder2.append(key);
        return stringBuilder2.toString();
    }

    public static String formatPluralString(String key, int plural) {
        if (!(key == null || key.length() == 0)) {
            if (getInstance().currentPluralRules != null) {
                String param = getInstance().stringForQuantity(getInstance().currentPluralRules.quantityForNumber(plural));
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(key);
                stringBuilder.append("_");
                stringBuilder.append(param);
                param = stringBuilder.toString();
                return formatString(param, ApplicationLoader.applicationContext.getResources().getIdentifier(param, "string", ApplicationLoader.applicationContext.getPackageName()), Integer.valueOf(plural));
            }
        }
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("LOC_ERR:");
        stringBuilder2.append(key);
        return stringBuilder2.toString();
    }

    public static String formatString(String key, int res, Object... args) {
        try {
            String value = (String) getInstance().localeValues.get(key);
            if (value == null) {
                value = ApplicationLoader.applicationContext.getString(res);
            }
            if (getInstance().currentLocale != null) {
                return String.format(getInstance().currentLocale, value, args);
            }
            return String.format(value, args);
        } catch (Throwable e) {
            FileLog.e(e);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("LOC_ERR: ");
            stringBuilder.append(key);
            return stringBuilder.toString();
        }
    }

    public static String formatTTLString(int ttl) {
        if (ttl < 60) {
            return formatPluralString("Seconds", ttl);
        }
        if (ttl < 3600) {
            return formatPluralString("Minutes", ttl / 60);
        }
        if (ttl < 86400) {
            return formatPluralString("Hours", (ttl / 60) / 60);
        }
        if (ttl < 604800) {
            return formatPluralString("Days", ((ttl / 60) / 60) / 24);
        }
        int days = ((ttl / 60) / 60) / 24;
        if (ttl % 7 == 0) {
            return formatPluralString("Weeks", days / 7);
        }
        return String.format("%s %s", new Object[]{formatPluralString("Weeks", days / 7), formatPluralString("Days", days % 7)});
    }

    public static String formatStringSimple(String string, Object... args) {
        try {
            if (getInstance().currentLocale != null) {
                return String.format(getInstance().currentLocale, string, args);
            }
            return String.format(string, args);
        } catch (Throwable e) {
            FileLog.e(e);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("LOC_ERR: ");
            stringBuilder.append(string);
            return stringBuilder.toString();
        }
    }

    public static String formatCallDuration(int duration) {
        if (duration > 3600) {
            String result = formatPluralString("Hours", duration / 3600);
            int minutes = (duration % 3600) / 60;
            if (minutes > 0) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(result);
                stringBuilder.append(", ");
                stringBuilder.append(formatPluralString("Minutes", minutes));
                result = stringBuilder.toString();
            }
            return result;
        } else if (duration > 60) {
            return formatPluralString("Minutes", duration / 60);
        } else {
            return formatPluralString("Seconds", duration);
        }
    }

    public void onDeviceConfigurationChange(Configuration newConfig) {
        if (!this.changingConfiguration) {
            is24HourFormat = DateFormat.is24HourFormat(ApplicationLoader.applicationContext);
            this.systemDefaultLocale = newConfig.locale;
            if (this.languageOverride != null) {
                LocaleInfo toSet = this.currentLocaleInfo;
                this.currentLocaleInfo = null;
                applyLanguage(toSet, false, false, UserConfig.selectedAccount);
            } else {
                Locale newLocale = newConfig.locale;
                if (newLocale != null) {
                    String d1 = newLocale.getDisplayName();
                    String d2 = this.currentLocale.getDisplayName();
                    if (!(d1 == null || d2 == null || d1.equals(d2))) {
                        recreateFormatters();
                    }
                    this.currentLocale = newLocale;
                    this.currentPluralRules = (PluralRules) this.allRules.get(this.currentLocale.getLanguage());
                    if (this.currentPluralRules == null) {
                        this.currentPluralRules = (PluralRules) this.allRules.get("en");
                    }
                }
            }
        }
    }

    public static String formatDateChat(long date) {
        try {
            date *= 1000;
            Calendar.getInstance().setTimeInMillis(date);
            if (Math.abs(System.currentTimeMillis() - date) < 31536000000L) {
                return getInstance().chatDate.format(date);
            }
            return getInstance().chatFullDate.format(date);
        } catch (Throwable e) {
            FileLog.e(e);
            return "LOC_ERR: formatDateChat";
        }
    }

    public static String formatDate(long date) {
        date *= 1000;
        try {
            Calendar rightNow = Calendar.getInstance();
            int day = rightNow.get(6);
            int year = rightNow.get(1);
            rightNow.setTimeInMillis(date);
            int dateDay = rightNow.get(6);
            int dateYear = rightNow.get(1);
            if (dateDay == day && year == dateYear) {
                return getInstance().formatterDay.format(new Date(date));
            }
            if (dateDay + 1 == day && year == dateYear) {
                return getString("Yesterday", R.string.Yesterday);
            }
            if (Math.abs(System.currentTimeMillis() - date) < 31536000000L) {
                return getInstance().formatterMonth.format(new Date(date));
            }
            return getInstance().formatterYear.format(new Date(date));
        } catch (Throwable e) {
            FileLog.e(e);
            return "LOC_ERR: formatDate";
        }
    }

    public static String formatDateAudio(long date) {
        date *= 1000;
        try {
            Calendar rightNow = Calendar.getInstance();
            int day = rightNow.get(6);
            int year = rightNow.get(1);
            rightNow.setTimeInMillis(date);
            int dateDay = rightNow.get(6);
            int dateYear = rightNow.get(1);
            if (dateDay == day && year == dateYear) {
                return formatString("TodayAtFormatted", R.string.TodayAtFormatted, getInstance().formatterDay.format(new Date(date)));
            } else if (dateDay + 1 == day && year == dateYear) {
                return formatString("YesterdayAtFormatted", R.string.YesterdayAtFormatted, getInstance().formatterDay.format(new Date(date)));
            } else if (Math.abs(System.currentTimeMillis() - date) < 31536000000L) {
                return formatString("formatDateAtTime", R.string.formatDateAtTime, getInstance().formatterMonth.format(new Date(date)), getInstance().formatterDay.format(new Date(date)));
            } else {
                return formatString("formatDateAtTime", R.string.formatDateAtTime, getInstance().formatterYear.format(new Date(date)), getInstance().formatterDay.format(new Date(date)));
            }
        } catch (Throwable e) {
            FileLog.e(e);
            return "LOC_ERR";
        }
    }

    public static String formatDateCallLog(long date) {
        date *= 1000;
        try {
            Calendar rightNow = Calendar.getInstance();
            int day = rightNow.get(6);
            int year = rightNow.get(1);
            rightNow.setTimeInMillis(date);
            int dateDay = rightNow.get(6);
            int dateYear = rightNow.get(1);
            if (dateDay == day && year == dateYear) {
                return getInstance().formatterDay.format(new Date(date));
            }
            if (dateDay + 1 == day && year == dateYear) {
                return formatString("YesterdayAtFormatted", R.string.YesterdayAtFormatted, getInstance().formatterDay.format(new Date(date)));
            } else if (Math.abs(System.currentTimeMillis() - date) < 31536000000L) {
                return formatString("formatDateAtTime", R.string.formatDateAtTime, getInstance().chatDate.format(new Date(date)), getInstance().formatterDay.format(new Date(date)));
            } else {
                return formatString("formatDateAtTime", R.string.formatDateAtTime, getInstance().chatFullDate.format(new Date(date)), getInstance().formatterDay.format(new Date(date)));
            }
        } catch (Throwable e) {
            FileLog.e(e);
            return "LOC_ERR";
        }
    }

    public static String formatLocationUpdateDate(long date) {
        long date2 = date * 1000;
        try {
            Calendar rightNow = Calendar.getInstance();
            int day = rightNow.get(6);
            int year = rightNow.get(1);
            rightNow.setTimeInMillis(date2);
            int dateDay = rightNow.get(6);
            int dateYear = rightNow.get(1);
            if (dateDay == day && year == dateYear) {
                int diff = ((int) (((long) ConnectionsManager.getInstance(UserConfig.selectedAccount).getCurrentTime()) - (date2 / 1000))) / 60;
                if (diff < 1) {
                    return getString("LocationUpdatedJustNow", R.string.LocationUpdatedJustNow);
                }
                if (diff < 60) {
                    return formatPluralString("UpdatedMinutes", diff);
                }
                Object[] objArr = new Object[1];
                objArr[0] = formatString("TodayAtFormatted", R.string.TodayAtFormatted, getInstance().formatterDay.format(new Date(date2)));
                return formatString("LocationUpdatedFormatted", R.string.LocationUpdatedFormatted, objArr);
            } else if (dateDay + 1 == day && year == dateYear) {
                Object[] objArr2 = new Object[1];
                objArr2[0] = formatString("YesterdayAtFormatted", R.string.YesterdayAtFormatted, getInstance().formatterDay.format(new Date(date2)));
                return formatString("LocationUpdatedFormatted", R.string.LocationUpdatedFormatted, objArr2);
            } else if (Math.abs(System.currentTimeMillis() - date2) < 31536000000L) {
                String format = new Object[]{getInstance().formatterMonth.format(new Date(date2)), getInstance().formatterDay.format(new Date(date2))};
                return formatString("LocationUpdatedFormatted", R.string.LocationUpdatedFormatted, formatString("formatDateAtTime", R.string.formatDateAtTime, format));
            } else {
                Object[] objArr3 = new Object[]{getInstance().formatterYear.format(new Date(date2)), getInstance().formatterDay.format(new Date(date2))};
                return formatString("LocationUpdatedFormatted", R.string.LocationUpdatedFormatted, formatString("formatDateAtTime", R.string.formatDateAtTime, objArr3));
            }
        } catch (Throwable e) {
            FileLog.e(e);
            return "LOC_ERR";
        }
    }

    public static String formatLocationLeftTime(int time) {
        String text;
        int hours = (time / 60) / 60;
        time -= (hours * 60) * 60;
        int minutes = time / 60;
        time -= minutes * 60;
        int i = 1;
        String str;
        Object[] objArr;
        if (hours != 0) {
            str = "%dh";
            objArr = new Object[1];
            if (minutes <= 30) {
                i = 0;
            }
            objArr[0] = Integer.valueOf(i + hours);
            text = String.format(str, objArr);
        } else if (minutes != 0) {
            str = "%d";
            objArr = new Object[1];
            if (time <= 30) {
                i = 0;
            }
            objArr[0] = Integer.valueOf(i + minutes);
            text = String.format(str, objArr);
        } else {
            return String.format("%d", new Object[]{Integer.valueOf(time)});
        }
        return text;
    }

    public static String formatDateOnline(long date) {
        date *= 1000;
        try {
            Calendar rightNow = Calendar.getInstance();
            int day = rightNow.get(6);
            int year = rightNow.get(1);
            rightNow.setTimeInMillis(date);
            int dateDay = rightNow.get(6);
            int dateYear = rightNow.get(1);
            Object[] objArr;
            if (dateDay == day && year == dateYear) {
                objArr = new Object[1];
                objArr[0] = formatString("TodayAtFormatted", R.string.TodayAtFormatted, getInstance().formatterDay.format(new Date(date)));
                return formatString("LastSeenFormatted", R.string.LastSeenFormatted, objArr);
            } else if (dateDay + 1 == day && year == dateYear) {
                objArr = new Object[1];
                objArr[0] = formatString("YesterdayAtFormatted", R.string.YesterdayAtFormatted, getInstance().formatterDay.format(new Date(date)));
                return formatString("LastSeenFormatted", R.string.LastSeenFormatted, objArr);
            } else if (Math.abs(System.currentTimeMillis() - date) < 31536000000L) {
                format = formatString("formatDateAtTime", R.string.formatDateAtTime, getInstance().formatterMonth.format(new Date(date)), getInstance().formatterDay.format(new Date(date)));
                return formatString("LastSeenDateFormatted", R.string.LastSeenDateFormatted, format);
            } else {
                format = formatString("formatDateAtTime", R.string.formatDateAtTime, getInstance().formatterYear.format(new Date(date)), getInstance().formatterDay.format(new Date(date)));
                return formatString("LastSeenDateFormatted", R.string.LastSeenDateFormatted, format);
            }
        } catch (Throwable e) {
            FileLog.e(e);
            return "LOC_ERR";
        }
    }

    private FastDateFormat createFormatter(Locale locale, String format, String defaultFormat) {
        if (format == null || format.length() == 0) {
            format = defaultFormat;
        }
        try {
            return FastDateFormat.getInstance(format, locale);
        } catch (Exception e) {
            return FastDateFormat.getInstance(defaultFormat, locale);
        }
    }

    public void recreateFormatters() {
        boolean z;
        Locale locale;
        String str;
        int i;
        String str2;
        Locale locale2 = this.currentLocale;
        if (locale2 == null) {
            locale2 = Locale.getDefault();
        }
        String lang = locale2.getLanguage();
        if (lang == null) {
            lang = "en";
        }
        lang = lang.toLowerCase();
        int i2 = 1;
        if (!(lang.startsWith("ar") || lang.startsWith("fa"))) {
            if (BuildVars.DEBUG_VERSION) {
                if (!lang.startsWith("he")) {
                    if (lang.startsWith("iw")) {
                    }
                }
            }
            z = false;
            isRTL = z;
            if (lang.equals("ko")) {
                i2 = 2;
            }
            nameDisplayOrder = i2;
            this.formatterMonth = createFormatter(locale2, getStringInternal("formatterMonth", R.string.formatterMonth), "dd MMM");
            this.formatterYear = createFormatter(locale2, getStringInternal("formatterYear", R.string.formatterYear), "dd.MM.yy");
            this.formatterYearMax = createFormatter(locale2, getStringInternal("formatterYearMax", R.string.formatterYearMax), "dd.MM.yyyy");
            this.chatDate = createFormatter(locale2, getStringInternal("chatDate", R.string.chatDate), "d MMMM");
            this.chatFullDate = createFormatter(locale2, getStringInternal("chatFullDate", R.string.chatFullDate), "d MMMM yyyy");
            this.formatterWeek = createFormatter(locale2, getStringInternal("formatterWeek", R.string.formatterWeek), "EEE");
            this.formatterMonthYear = createFormatter(locale2, getStringInternal("formatterMonthYear", R.string.formatterMonthYear), "MMMM yyyy");
            if (!lang.toLowerCase().equals("ar")) {
                if (lang.toLowerCase().equals("ko")) {
                    locale = Locale.US;
                    if (is24HourFormat) {
                        str = "formatterDay12H";
                        i = R.string.formatterDay12H;
                    } else {
                        str = "formatterDay24H";
                        i = R.string.formatterDay24H;
                    }
                    this.formatterDay = createFormatter(locale, getStringInternal(str, i), is24HourFormat ? "HH:mm" : "h:mm a");
                    if (is24HourFormat) {
                        str2 = "formatterStats12H";
                        i2 = R.string.formatterStats12H;
                    } else {
                        str2 = "formatterStats24H";
                        i2 = R.string.formatterStats24H;
                    }
                    this.formatterStats = createFormatter(locale2, getStringInternal(str2, i2), is24HourFormat ? "MMM dd yyyy, HH:mm" : "MMM dd yyyy, h:mm a");
                    if (is24HourFormat) {
                        str2 = "formatterBannedUntil12H";
                        i2 = R.string.formatterBannedUntil12H;
                    } else {
                        str2 = "formatterBannedUntil24H";
                        i2 = R.string.formatterBannedUntil24H;
                    }
                    this.formatterBannedUntil = createFormatter(locale2, getStringInternal(str2, i2), is24HourFormat ? "MMM dd yyyy, HH:mm" : "MMM dd yyyy, h:mm a");
                    if (is24HourFormat) {
                        str2 = "formatterBannedUntilThisYear12H";
                        i2 = R.string.formatterBannedUntilThisYear12H;
                    } else {
                        str2 = "formatterBannedUntilThisYear24H";
                        i2 = R.string.formatterBannedUntilThisYear24H;
                    }
                    this.formatterBannedUntilThisYear = createFormatter(locale2, getStringInternal(str2, i2), is24HourFormat ? "MMM dd, HH:mm" : "MMM dd, h:mm a");
                }
            }
            locale = locale2;
            if (is24HourFormat) {
                str = "formatterDay12H";
                i = R.string.formatterDay12H;
            } else {
                str = "formatterDay24H";
                i = R.string.formatterDay24H;
            }
            if (is24HourFormat) {
            }
            this.formatterDay = createFormatter(locale, getStringInternal(str, i), is24HourFormat ? "HH:mm" : "h:mm a");
            if (is24HourFormat) {
                str2 = "formatterStats12H";
                i2 = R.string.formatterStats12H;
            } else {
                str2 = "formatterStats24H";
                i2 = R.string.formatterStats24H;
            }
            if (is24HourFormat) {
            }
            this.formatterStats = createFormatter(locale2, getStringInternal(str2, i2), is24HourFormat ? "MMM dd yyyy, HH:mm" : "MMM dd yyyy, h:mm a");
            if (is24HourFormat) {
                str2 = "formatterBannedUntil12H";
                i2 = R.string.formatterBannedUntil12H;
            } else {
                str2 = "formatterBannedUntil24H";
                i2 = R.string.formatterBannedUntil24H;
            }
            if (is24HourFormat) {
            }
            this.formatterBannedUntil = createFormatter(locale2, getStringInternal(str2, i2), is24HourFormat ? "MMM dd yyyy, HH:mm" : "MMM dd yyyy, h:mm a");
            if (is24HourFormat) {
                str2 = "formatterBannedUntilThisYear12H";
                i2 = R.string.formatterBannedUntilThisYear12H;
            } else {
                str2 = "formatterBannedUntilThisYear24H";
                i2 = R.string.formatterBannedUntilThisYear24H;
            }
            if (is24HourFormat) {
            }
            this.formatterBannedUntilThisYear = createFormatter(locale2, getStringInternal(str2, i2), is24HourFormat ? "MMM dd, HH:mm" : "MMM dd, h:mm a");
        }
        z = true;
        isRTL = z;
        if (lang.equals("ko")) {
            i2 = 2;
        }
        nameDisplayOrder = i2;
        this.formatterMonth = createFormatter(locale2, getStringInternal("formatterMonth", R.string.formatterMonth), "dd MMM");
        this.formatterYear = createFormatter(locale2, getStringInternal("formatterYear", R.string.formatterYear), "dd.MM.yy");
        this.formatterYearMax = createFormatter(locale2, getStringInternal("formatterYearMax", R.string.formatterYearMax), "dd.MM.yyyy");
        this.chatDate = createFormatter(locale2, getStringInternal("chatDate", R.string.chatDate), "d MMMM");
        this.chatFullDate = createFormatter(locale2, getStringInternal("chatFullDate", R.string.chatFullDate), "d MMMM yyyy");
        this.formatterWeek = createFormatter(locale2, getStringInternal("formatterWeek", R.string.formatterWeek), "EEE");
        this.formatterMonthYear = createFormatter(locale2, getStringInternal("formatterMonthYear", R.string.formatterMonthYear), "MMMM yyyy");
        if (lang.toLowerCase().equals("ar")) {
            if (lang.toLowerCase().equals("ko")) {
                locale = Locale.US;
                if (is24HourFormat) {
                    str = "formatterDay24H";
                    i = R.string.formatterDay24H;
                } else {
                    str = "formatterDay12H";
                    i = R.string.formatterDay12H;
                }
                if (is24HourFormat) {
                }
                this.formatterDay = createFormatter(locale, getStringInternal(str, i), is24HourFormat ? "HH:mm" : "h:mm a");
                if (is24HourFormat) {
                    str2 = "formatterStats24H";
                    i2 = R.string.formatterStats24H;
                } else {
                    str2 = "formatterStats12H";
                    i2 = R.string.formatterStats12H;
                }
                if (is24HourFormat) {
                }
                this.formatterStats = createFormatter(locale2, getStringInternal(str2, i2), is24HourFormat ? "MMM dd yyyy, HH:mm" : "MMM dd yyyy, h:mm a");
                if (is24HourFormat) {
                    str2 = "formatterBannedUntil24H";
                    i2 = R.string.formatterBannedUntil24H;
                } else {
                    str2 = "formatterBannedUntil12H";
                    i2 = R.string.formatterBannedUntil12H;
                }
                if (is24HourFormat) {
                }
                this.formatterBannedUntil = createFormatter(locale2, getStringInternal(str2, i2), is24HourFormat ? "MMM dd yyyy, HH:mm" : "MMM dd yyyy, h:mm a");
                if (is24HourFormat) {
                    str2 = "formatterBannedUntilThisYear24H";
                    i2 = R.string.formatterBannedUntilThisYear24H;
                } else {
                    str2 = "formatterBannedUntilThisYear12H";
                    i2 = R.string.formatterBannedUntilThisYear12H;
                }
                if (is24HourFormat) {
                }
                this.formatterBannedUntilThisYear = createFormatter(locale2, getStringInternal(str2, i2), is24HourFormat ? "MMM dd, HH:mm" : "MMM dd, h:mm a");
            }
        }
        locale = locale2;
        if (is24HourFormat) {
            str = "formatterDay12H";
            i = R.string.formatterDay12H;
        } else {
            str = "formatterDay24H";
            i = R.string.formatterDay24H;
        }
        if (is24HourFormat) {
        }
        this.formatterDay = createFormatter(locale, getStringInternal(str, i), is24HourFormat ? "HH:mm" : "h:mm a");
        if (is24HourFormat) {
            str2 = "formatterStats12H";
            i2 = R.string.formatterStats12H;
        } else {
            str2 = "formatterStats24H";
            i2 = R.string.formatterStats24H;
        }
        if (is24HourFormat) {
        }
        this.formatterStats = createFormatter(locale2, getStringInternal(str2, i2), is24HourFormat ? "MMM dd yyyy, HH:mm" : "MMM dd yyyy, h:mm a");
        if (is24HourFormat) {
            str2 = "formatterBannedUntil12H";
            i2 = R.string.formatterBannedUntil12H;
        } else {
            str2 = "formatterBannedUntil24H";
            i2 = R.string.formatterBannedUntil24H;
        }
        if (is24HourFormat) {
        }
        this.formatterBannedUntil = createFormatter(locale2, getStringInternal(str2, i2), is24HourFormat ? "MMM dd yyyy, HH:mm" : "MMM dd yyyy, h:mm a");
        if (is24HourFormat) {
            str2 = "formatterBannedUntilThisYear12H";
            i2 = R.string.formatterBannedUntilThisYear12H;
        } else {
            str2 = "formatterBannedUntilThisYear24H";
            i2 = R.string.formatterBannedUntilThisYear24H;
        }
        if (is24HourFormat) {
        }
        this.formatterBannedUntilThisYear = createFormatter(locale2, getStringInternal(str2, i2), is24HourFormat ? "MMM dd, HH:mm" : "MMM dd, h:mm a");
    }

    public static boolean isRTLCharacter(char ch) {
        if (Character.getDirectionality(ch) == (byte) 1 || Character.getDirectionality(ch) == (byte) 2 || Character.getDirectionality(ch) == (byte) 16) {
            return true;
        }
        return Character.getDirectionality(ch) == (byte) 17;
    }

    public static String formatDateForBan(long date) {
        date *= 1000;
        try {
            Calendar rightNow = Calendar.getInstance();
            int year = rightNow.get(1);
            rightNow.setTimeInMillis(date);
            if (year == rightNow.get(1)) {
                return getInstance().formatterBannedUntilThisYear.format(new Date(date));
            }
            return getInstance().formatterBannedUntil.format(new Date(date));
        } catch (Throwable e) {
            FileLog.e(e);
            return "LOC_ERR";
        }
    }

    public static String stringForMessageListDate(long date) {
        date *= 1000;
        try {
            Calendar rightNow = Calendar.getInstance();
            int day = rightNow.get(6);
            rightNow.setTimeInMillis(date);
            int dateDay = rightNow.get(6);
            if (Math.abs(System.currentTimeMillis() - date) >= 31536000000L) {
                return getInstance().formatterYear.format(new Date(date));
            }
            int dayDiff = dateDay - day;
            if (dayDiff != 0) {
                if (dayDiff != -1 || System.currentTimeMillis() - date >= 28800000) {
                    if (dayDiff <= -7 || dayDiff > -1) {
                        return getInstance().formatterMonth.format(new Date(date));
                    }
                    return getInstance().formatterWeek.format(new Date(date));
                }
            }
            return getInstance().formatterDay.format(new Date(date));
        } catch (Throwable e) {
            FileLog.e(e);
            return "LOC_ERR";
        }
    }

    public static String formatShortNumber(int number, int[] rounded) {
        StringBuilder K = new StringBuilder();
        int lastDec = 0;
        int number2 = number;
        number = 0;
        while (number2 / 1000 > 0) {
            K.append("K");
            lastDec = (number2 % 1000) / 100;
            number2 /= 1000;
        }
        if (rounded != null) {
            double value = ((double) number2) + (((double) lastDec) / 10.0d);
            for (int a = 0; a < K.length(); a++) {
                value *= 1000.0d;
            }
            rounded[0] = (int) value;
        }
        if (lastDec == 0 || K.length() <= 0) {
            if (K.length() == 2) {
                return String.format(Locale.US, "%dM", new Object[]{Integer.valueOf(number2)});
            }
            return String.format(Locale.US, "%d%s", new Object[]{Integer.valueOf(number2), K.toString()});
        } else if (K.length() == 2) {
            return String.format(Locale.US, "%d.%dM", new Object[]{Integer.valueOf(number2), Integer.valueOf(lastDec)});
        } else {
            return String.format(Locale.US, "%d.%d%s", new Object[]{Integer.valueOf(number2), Integer.valueOf(lastDec), K.toString()});
        }
    }

    public static String formatUserStatus(int currentAccount, User user) {
        if (!(user == null || user.status == null || user.status.expires != 0)) {
            if (user.status instanceof TL_userStatusRecently) {
                user.status.expires = -100;
            } else if (user.status instanceof TL_userStatusLastWeek) {
                user.status.expires = -101;
            } else if (user.status instanceof TL_userStatusLastMonth) {
                user.status.expires = -102;
            }
        }
        if (user != null && user.status != null && user.status.expires <= 0 && MessagesController.getInstance(currentAccount).onlinePrivacy.containsKey(Integer.valueOf(user.id))) {
            return getString("Online", R.string.Online);
        }
        if (!(user == null || user.status == null || user.status.expires == 0 || UserObject.isDeleted(user))) {
            if (!(user instanceof TL_userEmpty)) {
                if (user.status.expires > ConnectionsManager.getInstance(currentAccount).getCurrentTime()) {
                    return getString("Online", R.string.Online);
                }
                if (user.status.expires == -1) {
                    return getString("Invisible", R.string.Invisible);
                }
                if (user.status.expires == -100) {
                    return getString("Lately", R.string.Lately);
                }
                if (user.status.expires == -101) {
                    return getString("WithinAWeek", R.string.WithinAWeek);
                }
                if (user.status.expires == -102) {
                    return getString("WithinAMonth", R.string.WithinAMonth);
                }
                return formatDateOnline((long) user.status.expires);
            }
        }
        return getString("ALongTimeAgo", R.string.ALongTimeAgo);
    }

    private String escapeString(String str) {
        if (str.contains("[CDATA")) {
            return str;
        }
        return str.replace("<", "&lt;").replace(">", "&gt;").replace("& ", "&amp; ");
    }

    public void saveRemoteLocaleStrings(final TL_langPackDifference difference, int currentAccount) {
        if (difference != null) {
            if (!difference.strings.isEmpty()) {
                final String langCode = difference.lang_code.replace('-', '_').toLowerCase();
                if (langCode.equals(this.currentLocaleInfo.shortName)) {
                    File filesDirFixed = ApplicationLoader.getFilesDirFixed();
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("remote_");
                    stringBuilder.append(langCode);
                    stringBuilder.append(".xml");
                    File finalFile = new File(filesDirFixed, stringBuilder.toString());
                    try {
                        HashMap<String, String> values;
                        if (difference.from_version == 0) {
                            values = new HashMap();
                        } else {
                            values = getLocaleFileStrings(finalFile, true);
                        }
                        for (int a = 0; a < difference.strings.size(); a++) {
                            LangPackString string = (LangPackString) difference.strings.get(a);
                            if (string instanceof TL_langPackString) {
                                values.put(string.key, escapeString(string.value));
                            } else if (string instanceof TL_langPackStringPluralized) {
                                StringBuilder stringBuilder2 = new StringBuilder();
                                stringBuilder2.append(string.key);
                                stringBuilder2.append("_zero");
                                values.put(stringBuilder2.toString(), string.zero_value != null ? escapeString(string.zero_value) : TtmlNode.ANONYMOUS_REGION_ID);
                                stringBuilder2 = new StringBuilder();
                                stringBuilder2.append(string.key);
                                stringBuilder2.append("_one");
                                values.put(stringBuilder2.toString(), string.one_value != null ? escapeString(string.one_value) : TtmlNode.ANONYMOUS_REGION_ID);
                                stringBuilder2 = new StringBuilder();
                                stringBuilder2.append(string.key);
                                stringBuilder2.append("_two");
                                values.put(stringBuilder2.toString(), string.two_value != null ? escapeString(string.two_value) : TtmlNode.ANONYMOUS_REGION_ID);
                                stringBuilder2 = new StringBuilder();
                                stringBuilder2.append(string.key);
                                stringBuilder2.append("_few");
                                values.put(stringBuilder2.toString(), string.few_value != null ? escapeString(string.few_value) : TtmlNode.ANONYMOUS_REGION_ID);
                                stringBuilder2 = new StringBuilder();
                                stringBuilder2.append(string.key);
                                stringBuilder2.append("_many");
                                values.put(stringBuilder2.toString(), string.many_value != null ? escapeString(string.many_value) : TtmlNode.ANONYMOUS_REGION_ID);
                                stringBuilder2 = new StringBuilder();
                                stringBuilder2.append(string.key);
                                stringBuilder2.append("_other");
                                values.put(stringBuilder2.toString(), string.other_value != null ? escapeString(string.other_value) : TtmlNode.ANONYMOUS_REGION_ID);
                            } else if (string instanceof TL_langPackStringDeleted) {
                                values.remove(string.key);
                            }
                        }
                        if (BuildVars.LOGS_ENABLED) {
                            StringBuilder stringBuilder3 = new StringBuilder();
                            stringBuilder3.append("save locale file to ");
                            stringBuilder3.append(finalFile);
                            FileLog.d(stringBuilder3.toString());
                        }
                        BufferedWriter writer = new BufferedWriter(new FileWriter(finalFile));
                        writer.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
                        writer.write("<resources>\n");
                        for (Entry<String, String> entry : values.entrySet()) {
                            writer.write(String.format("<string name=\"%1$s\">%2$s</string>\n", new Object[]{entry.getKey(), entry.getValue()}));
                        }
                        writer.write("</resources>");
                        writer.close();
                        final HashMap<String, String> valuesToSet = getLocaleFileStrings(finalFile);
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            public void run() {
                                LocaleInfo localeInfo = LocaleController.this.getLanguageFromDict(langCode);
                                if (localeInfo != null) {
                                    localeInfo.version = difference.version;
                                }
                                LocaleController.this.saveOtherLanguages();
                                if (LocaleController.this.currentLocaleInfo == null || !LocaleController.this.currentLocaleInfo.isLocal()) {
                                    try {
                                        Locale newLocale;
                                        String[] args = localeInfo.shortName.split("_");
                                        if (args.length == 1) {
                                            newLocale = new Locale(localeInfo.shortName);
                                        } else {
                                            newLocale = new Locale(args[0], args[1]);
                                        }
                                        if (newLocale != null) {
                                            LocaleController.this.languageOverride = localeInfo.shortName;
                                            Editor editor = MessagesController.getGlobalMainSettings().edit();
                                            editor.putString("language", localeInfo.getKey());
                                            editor.commit();
                                        }
                                        if (newLocale != null) {
                                            LocaleController.this.localeValues = valuesToSet;
                                            LocaleController.this.currentLocale = newLocale;
                                            LocaleController.this.currentLocaleInfo = localeInfo;
                                            LocaleController.this.currentPluralRules = (PluralRules) LocaleController.this.allRules.get(LocaleController.this.currentLocale.getLanguage());
                                            if (LocaleController.this.currentPluralRules == null) {
                                                LocaleController.this.currentPluralRules = (PluralRules) LocaleController.this.allRules.get("en");
                                            }
                                            LocaleController.this.changingConfiguration = true;
                                            Locale.setDefault(LocaleController.this.currentLocale);
                                            Configuration config = new Configuration();
                                            config.locale = LocaleController.this.currentLocale;
                                            ApplicationLoader.applicationContext.getResources().updateConfiguration(config, ApplicationLoader.applicationContext.getResources().getDisplayMetrics());
                                            LocaleController.this.changingConfiguration = false;
                                        }
                                    } catch (Throwable e) {
                                        FileLog.e(e);
                                        LocaleController.this.changingConfiguration = false;
                                    }
                                    LocaleController.this.recreateFormatters();
                                    NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.reloadInterface, new Object[0]);
                                }
                            }
                        });
                    } catch (Exception e) {
                    }
                }
            }
        }
    }

    public void loadRemoteLanguages(final int currentAccount) {
        if (!this.loadingRemoteLanguages) {
            this.loadingRemoteLanguages = true;
            ConnectionsManager.getInstance(currentAccount).sendRequest(new TL_langpack_getLanguages(), new RequestDelegate() {
                public void run(final TLObject response, TL_error error) {
                    if (response != null) {
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            public void run() {
                                int a;
                                LocaleController.this.loadingRemoteLanguages = false;
                                Vector res = response;
                                HashMap<String, LocaleInfo> remoteLoaded = new HashMap();
                                LocaleController.this.remoteLanguages.clear();
                                for (a = 0; a < res.objects.size(); a++) {
                                    TL_langPackLanguage language = (TL_langPackLanguage) res.objects.get(a);
                                    if (BuildVars.LOGS_ENABLED) {
                                        StringBuilder stringBuilder = new StringBuilder();
                                        stringBuilder.append("loaded lang ");
                                        stringBuilder.append(language.name);
                                        FileLog.d(stringBuilder.toString());
                                    }
                                    LocaleInfo localeInfo = new LocaleInfo();
                                    localeInfo.nameEnglish = language.name;
                                    localeInfo.name = language.native_name;
                                    localeInfo.shortName = language.lang_code.replace('-', '_').toLowerCase();
                                    localeInfo.pathToFile = "remote";
                                    LocaleInfo existing = LocaleController.this.getLanguageFromDict(localeInfo.getKey());
                                    if (existing == null) {
                                        LocaleController.this.languages.add(localeInfo);
                                        LocaleController.this.languagesDict.put(localeInfo.getKey(), localeInfo);
                                        existing = localeInfo;
                                    } else {
                                        existing.nameEnglish = localeInfo.nameEnglish;
                                        existing.name = localeInfo.name;
                                        existing.pathToFile = localeInfo.pathToFile;
                                        localeInfo = existing;
                                    }
                                    LocaleController.this.remoteLanguages.add(localeInfo);
                                    remoteLoaded.put(localeInfo.getKey(), existing);
                                }
                                a = 0;
                                while (a < LocaleController.this.languages.size()) {
                                    LocaleInfo info = (LocaleInfo) LocaleController.this.languages.get(a);
                                    if (!info.isBuiltIn()) {
                                        if (info.isRemote()) {
                                            if (((LocaleInfo) remoteLoaded.get(info.getKey())) == null) {
                                                if (BuildVars.LOGS_ENABLED) {
                                                    StringBuilder stringBuilder2 = new StringBuilder();
                                                    stringBuilder2.append("remove lang ");
                                                    stringBuilder2.append(info.getKey());
                                                    FileLog.d(stringBuilder2.toString());
                                                }
                                                LocaleController.this.languages.remove(a);
                                                LocaleController.this.languagesDict.remove(info.getKey());
                                                a--;
                                                if (info == LocaleController.this.currentLocaleInfo) {
                                                    if (LocaleController.this.systemDefaultLocale.getLanguage() != null) {
                                                        info = LocaleController.this.getLanguageFromDict(LocaleController.this.systemDefaultLocale.getLanguage());
                                                    }
                                                    if (info == null) {
                                                        info = LocaleController.this.getLanguageFromDict(LocaleController.this.getLocaleString(LocaleController.this.systemDefaultLocale));
                                                    }
                                                    if (info == null) {
                                                        info = LocaleController.this.getLanguageFromDict("en");
                                                    }
                                                    LocaleController.this.applyLanguage(info, true, false, currentAccount);
                                                    NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.reloadInterface, new Object[0]);
                                                }
                                            }
                                        }
                                    }
                                    a++;
                                }
                                LocaleController.this.saveOtherLanguages();
                                NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.suggestedLangpack, new Object[0]);
                                LocaleController.this.applyLanguage(LocaleController.this.currentLocaleInfo, true, false, currentAccount);
                            }
                        });
                    }
                }
            }, 8);
        }
    }

    private void applyRemoteLanguage(LocaleInfo localeInfo, boolean force, final int currentAccount) {
        if (localeInfo != null) {
            if (localeInfo == null || localeInfo.isRemote()) {
                if (localeInfo.version == 0 || force) {
                    for (int a = 0; a < 3; a++) {
                        ConnectionsManager.setLangCode(localeInfo.shortName);
                    }
                    TL_langpack_getLangPack req = new TL_langpack_getLangPack();
                    req.lang_code = localeInfo.shortName.replace("_", "-");
                    ConnectionsManager.getInstance(currentAccount).sendRequest(req, new RequestDelegate() {
                        public void run(final TLObject response, TL_error error) {
                            if (response != null) {
                                AndroidUtilities.runOnUIThread(new Runnable() {
                                    public void run() {
                                        LocaleController.this.saveRemoteLocaleStrings((TL_langPackDifference) response, currentAccount);
                                    }
                                });
                            }
                        }
                    }, 8);
                } else {
                    TL_langpack_getDifference req2 = new TL_langpack_getDifference();
                    req2.from_version = localeInfo.version;
                    ConnectionsManager.getInstance(currentAccount).sendRequest(req2, new RequestDelegate() {
                        public void run(final TLObject response, TL_error error) {
                            if (response != null) {
                                AndroidUtilities.runOnUIThread(new Runnable() {
                                    public void run() {
                                        LocaleController.this.saveRemoteLocaleStrings((TL_langPackDifference) response, currentAccount);
                                    }
                                });
                            }
                        }
                    }, 8);
                }
            }
        }
    }

    public String getTranslitString(String src) {
        if (this.translitChars == null) {
            this.translitChars = new HashMap(520);
            this.translitChars.put("ȼ", "c");
            this.translitChars.put("ᶇ", "n");
            this.translitChars.put("ɖ", "d");
            this.translitChars.put("ỿ", "y");
            this.translitChars.put("ᴓ", "o");
            this.translitChars.put("ø", "o");
            this.translitChars.put("ḁ", "a");
            this.translitChars.put("ʯ", "h");
            this.translitChars.put("ŷ", "y");
            this.translitChars.put("ʞ", "k");
            this.translitChars.put("ừ", "u");
            this.translitChars.put("ꜳ", "aa");
            this.translitChars.put("ĳ", "ij");
            this.translitChars.put("ḽ", "l");
            this.translitChars.put("ɪ", "i");
            this.translitChars.put("ḇ", "b");
            this.translitChars.put("ʀ", "r");
            this.translitChars.put("ě", "e");
            this.translitChars.put("ﬃ", "ffi");
            this.translitChars.put("ơ", "o");
            this.translitChars.put("ⱹ", "r");
            this.translitChars.put("ồ", "o");
            this.translitChars.put("ǐ", "i");
            this.translitChars.put("ꝕ", TtmlNode.TAG_P);
            this.translitChars.put("ý", "y");
            this.translitChars.put("ḝ", "e");
            this.translitChars.put("ₒ", "o");
            this.translitChars.put("ⱥ", "a");
            this.translitChars.put("ʙ", "b");
            this.translitChars.put("ḛ", "e");
            this.translitChars.put("ƈ", "c");
            this.translitChars.put("ɦ", "h");
            this.translitChars.put("ᵬ", "b");
            this.translitChars.put("ṣ", "s");
            this.translitChars.put("đ", "d");
            this.translitChars.put("ỗ", "o");
            this.translitChars.put("ɟ", "j");
            this.translitChars.put("ẚ", "a");
            this.translitChars.put("ɏ", "y");
            this.translitChars.put("л", "l");
            this.translitChars.put("ʌ", "v");
            this.translitChars.put("ꝓ", TtmlNode.TAG_P);
            this.translitChars.put("ﬁ", "fi");
            this.translitChars.put("ᶄ", "k");
            this.translitChars.put("ḏ", "d");
            this.translitChars.put("ᴌ", "l");
            this.translitChars.put("ė", "e");
            this.translitChars.put("ё", "yo");
            this.translitChars.put("ᴋ", "k");
            this.translitChars.put("ċ", "c");
            this.translitChars.put("ʁ", "r");
            this.translitChars.put("ƕ", "hv");
            this.translitChars.put("ƀ", "b");
            this.translitChars.put("ṍ", "o");
            this.translitChars.put("ȣ", "ou");
            this.translitChars.put("ǰ", "j");
            this.translitChars.put("ᶃ", "g");
            this.translitChars.put("ṋ", "n");
            this.translitChars.put("ɉ", "j");
            this.translitChars.put("ǧ", "g");
            this.translitChars.put("ǳ", "dz");
            this.translitChars.put("ź", "z");
            this.translitChars.put("ꜷ", "au");
            this.translitChars.put("ǖ", "u");
            this.translitChars.put("ᵹ", "g");
            this.translitChars.put("ȯ", "o");
            this.translitChars.put("ɐ", "a");
            this.translitChars.put("ą", "a");
            this.translitChars.put("õ", "o");
            this.translitChars.put("ɻ", "r");
            this.translitChars.put("ꝍ", "o");
            this.translitChars.put("ǟ", "a");
            this.translitChars.put("ȴ", "l");
            this.translitChars.put("ʂ", "s");
            this.translitChars.put("ﬂ", "fl");
            this.translitChars.put("ȉ", "i");
            this.translitChars.put("ⱻ", "e");
            this.translitChars.put("ṉ", "n");
            this.translitChars.put("ï", "i");
            this.translitChars.put("ñ", "n");
            this.translitChars.put("ᴉ", "i");
            this.translitChars.put("ʇ", "t");
            this.translitChars.put("ẓ", "z");
            this.translitChars.put("ỷ", "y");
            this.translitChars.put("ȳ", "y");
            this.translitChars.put("ṩ", "s");
            this.translitChars.put("ɽ", "r");
            this.translitChars.put("ĝ", "g");
            this.translitChars.put("в", "v");
            this.translitChars.put("ᴝ", "u");
            this.translitChars.put("ḳ", "k");
            this.translitChars.put("ꝫ", "et");
            this.translitChars.put("ī", "i");
            this.translitChars.put("ť", "t");
            this.translitChars.put("ꜿ", "c");
            this.translitChars.put("ʟ", "l");
            this.translitChars.put("ꜹ", "av");
            this.translitChars.put("û", "u");
            this.translitChars.put("æ", "ae");
            this.translitChars.put("и", "i");
            this.translitChars.put("ă", "a");
            this.translitChars.put("ǘ", "u");
            this.translitChars.put("ꞅ", "s");
            this.translitChars.put("ᵣ", "r");
            this.translitChars.put("ᴀ", "a");
            this.translitChars.put("ƃ", "b");
            this.translitChars.put("ḩ", "h");
            this.translitChars.put("ṧ", "s");
            this.translitChars.put("ₑ", "e");
            this.translitChars.put("ʜ", "h");
            this.translitChars.put("ẋ", "x");
            this.translitChars.put("ꝅ", "k");
            this.translitChars.put("ḋ", "d");
            this.translitChars.put("ƣ", "oi");
            this.translitChars.put("ꝑ", TtmlNode.TAG_P);
            this.translitChars.put("ħ", "h");
            this.translitChars.put("ⱴ", "v");
            this.translitChars.put("ẇ", "w");
            this.translitChars.put("ǹ", "n");
            this.translitChars.put("ɯ", "m");
            this.translitChars.put("ɡ", "g");
            this.translitChars.put("ɴ", "n");
            this.translitChars.put("ᴘ", TtmlNode.TAG_P);
            this.translitChars.put("ᵥ", "v");
            this.translitChars.put("ū", "u");
            this.translitChars.put("ḃ", "b");
            this.translitChars.put("ṗ", TtmlNode.TAG_P);
            this.translitChars.put("ь", TtmlNode.ANONYMOUS_REGION_ID);
            this.translitChars.put("å", "a");
            this.translitChars.put("ɕ", "c");
            this.translitChars.put("ọ", "o");
            this.translitChars.put("ắ", "a");
            this.translitChars.put("ƒ", "f");
            this.translitChars.put("ǣ", "ae");
            this.translitChars.put("ꝡ", "vy");
            this.translitChars.put("ﬀ", "ff");
            this.translitChars.put("ᶉ", "r");
            this.translitChars.put("ô", "o");
            this.translitChars.put("ǿ", "o");
            this.translitChars.put("ṳ", "u");
            this.translitChars.put("ȥ", "z");
            this.translitChars.put("ḟ", "f");
            this.translitChars.put("ḓ", "d");
            this.translitChars.put("ȇ", "e");
            this.translitChars.put("ȕ", "u");
            this.translitChars.put("п", TtmlNode.TAG_P);
            this.translitChars.put("ȵ", "n");
            this.translitChars.put("ʠ", "q");
            this.translitChars.put("ấ", "a");
            this.translitChars.put("ǩ", "k");
            this.translitChars.put("ĩ", "i");
            this.translitChars.put("ṵ", "u");
            this.translitChars.put("ŧ", "t");
            this.translitChars.put("ɾ", "r");
            this.translitChars.put("ƙ", "k");
            this.translitChars.put("ṫ", "t");
            this.translitChars.put("ꝗ", "q");
            this.translitChars.put("ậ", "a");
            this.translitChars.put("н", "n");
            this.translitChars.put("ʄ", "j");
            this.translitChars.put("ƚ", "l");
            this.translitChars.put("ᶂ", "f");
            this.translitChars.put("д", "d");
            this.translitChars.put("ᵴ", "s");
            this.translitChars.put("ꞃ", "r");
            this.translitChars.put("ᶌ", "v");
            this.translitChars.put("ɵ", "o");
            this.translitChars.put("ḉ", "c");
            this.translitChars.put("ᵤ", "u");
            this.translitChars.put("ẑ", "z");
            this.translitChars.put("ṹ", "u");
            this.translitChars.put("ň", "n");
            this.translitChars.put("ʍ", "w");
            this.translitChars.put("ầ", "a");
            this.translitChars.put("ǉ", "lj");
            this.translitChars.put("ɓ", "b");
            this.translitChars.put("ɼ", "r");
            this.translitChars.put("ò", "o");
            this.translitChars.put("ẘ", "w");
            this.translitChars.put("ɗ", "d");
            this.translitChars.put("ꜽ", "ay");
            this.translitChars.put("ư", "u");
            this.translitChars.put("ᶀ", "b");
            this.translitChars.put("ǜ", "u");
            this.translitChars.put("ẹ", "e");
            this.translitChars.put("ǡ", "a");
            this.translitChars.put("ɥ", "h");
            this.translitChars.put("ṏ", "o");
            this.translitChars.put("ǔ", "u");
            this.translitChars.put("ʎ", "y");
            this.translitChars.put("ȱ", "o");
            this.translitChars.put("ệ", "e");
            this.translitChars.put("ế", "e");
            this.translitChars.put("ĭ", "i");
            this.translitChars.put("ⱸ", "e");
            this.translitChars.put("ṯ", "t");
            this.translitChars.put("ᶑ", "d");
            this.translitChars.put("ḧ", "h");
            this.translitChars.put("ṥ", "s");
            this.translitChars.put("ë", "e");
            this.translitChars.put("ᴍ", "m");
            this.translitChars.put("ö", "o");
            this.translitChars.put("é", "e");
            this.translitChars.put("ı", "i");
            this.translitChars.put("ď", "d");
            this.translitChars.put("ᵯ", "m");
            this.translitChars.put("ỵ", "y");
            this.translitChars.put("я", "ya");
            this.translitChars.put("ŵ", "w");
            this.translitChars.put("ề", "e");
            this.translitChars.put("ứ", "u");
            this.translitChars.put("ƶ", "z");
            this.translitChars.put("ĵ", "j");
            this.translitChars.put("ḍ", "d");
            this.translitChars.put("ŭ", "u");
            this.translitChars.put("ʝ", "j");
            this.translitChars.put("ж", "zh");
            this.translitChars.put("ê", "e");
            this.translitChars.put("ǚ", "u");
            this.translitChars.put("ġ", "g");
            this.translitChars.put("ṙ", "r");
            this.translitChars.put("ƞ", "n");
            this.translitChars.put("ъ", TtmlNode.ANONYMOUS_REGION_ID);
            this.translitChars.put("ḗ", "e");
            this.translitChars.put("ẝ", "s");
            this.translitChars.put("ᶁ", "d");
            this.translitChars.put("ķ", "k");
            this.translitChars.put("ᴂ", "ae");
            this.translitChars.put("ɘ", "e");
            this.translitChars.put("ợ", "o");
            this.translitChars.put("ḿ", "m");
            this.translitChars.put("ꜰ", "f");
            this.translitChars.put("а", "a");
            this.translitChars.put("ẵ", "a");
            this.translitChars.put("ꝏ", "oo");
            this.translitChars.put("ᶆ", "m");
            this.translitChars.put("ᵽ", TtmlNode.TAG_P);
            this.translitChars.put("ц", "ts");
            this.translitChars.put("ữ", "u");
            this.translitChars.put("ⱪ", "k");
            this.translitChars.put("ḥ", "h");
            this.translitChars.put("ţ", "t");
            this.translitChars.put("ᵱ", TtmlNode.TAG_P);
            this.translitChars.put("ṁ", "m");
            this.translitChars.put("á", "a");
            this.translitChars.put("ᴎ", "n");
            this.translitChars.put("ꝟ", "v");
            this.translitChars.put("è", "e");
            this.translitChars.put("ᶎ", "z");
            this.translitChars.put("ꝺ", "d");
            this.translitChars.put("ᶈ", TtmlNode.TAG_P);
            this.translitChars.put("м", "m");
            this.translitChars.put("ɫ", "l");
            this.translitChars.put("ᴢ", "z");
            this.translitChars.put("ɱ", "m");
            this.translitChars.put("ṝ", "r");
            this.translitChars.put("ṽ", "v");
            this.translitChars.put("ũ", "u");
            this.translitChars.put("ß", "ss");
            this.translitChars.put("т", "t");
            this.translitChars.put("ĥ", "h");
            this.translitChars.put("ᵵ", "t");
            this.translitChars.put("ʐ", "z");
            this.translitChars.put("ṟ", "r");
            this.translitChars.put("ɲ", "n");
            this.translitChars.put("à", "a");
            this.translitChars.put("ẙ", "y");
            this.translitChars.put("ỳ", "y");
            this.translitChars.put("ᴔ", "oe");
            this.translitChars.put("ы", "i");
            this.translitChars.put("ₓ", "x");
            this.translitChars.put("ȗ", "u");
            this.translitChars.put("ⱼ", "j");
            this.translitChars.put("ẫ", "a");
            this.translitChars.put("ʑ", "z");
            this.translitChars.put("ẛ", "s");
            this.translitChars.put("ḭ", "i");
            this.translitChars.put("ꜵ", "ao");
            this.translitChars.put("ɀ", "z");
            this.translitChars.put("ÿ", "y");
            this.translitChars.put("ǝ", "e");
            this.translitChars.put("ǭ", "o");
            this.translitChars.put("ᴅ", "d");
            this.translitChars.put("ᶅ", "l");
            this.translitChars.put("ù", "u");
            this.translitChars.put("ạ", "a");
            this.translitChars.put("ḅ", "b");
            this.translitChars.put("ụ", "u");
            this.translitChars.put("к", "k");
            this.translitChars.put("ằ", "a");
            this.translitChars.put("ᴛ", "t");
            this.translitChars.put("ƴ", "y");
            this.translitChars.put("ⱦ", "t");
            this.translitChars.put("з", "z");
            this.translitChars.put("ⱡ", "l");
            this.translitChars.put("ȷ", "j");
            this.translitChars.put("ᵶ", "z");
            this.translitChars.put("ḫ", "h");
            this.translitChars.put("ⱳ", "w");
            this.translitChars.put("ḵ", "k");
            this.translitChars.put("ờ", "o");
            this.translitChars.put("î", "i");
            this.translitChars.put("ģ", "g");
            this.translitChars.put("ȅ", "e");
            this.translitChars.put("ȧ", "a");
            this.translitChars.put("ẳ", "a");
            this.translitChars.put("щ", "sch");
            this.translitChars.put("ɋ", "q");
            this.translitChars.put("ṭ", "t");
            this.translitChars.put("ꝸ", "um");
            this.translitChars.put("ᴄ", "c");
            this.translitChars.put("ẍ", "x");
            this.translitChars.put("ủ", "u");
            this.translitChars.put("ỉ", "i");
            this.translitChars.put("ᴚ", "r");
            this.translitChars.put("ś", "s");
            this.translitChars.put("ꝋ", "o");
            this.translitChars.put("ỹ", "y");
            this.translitChars.put("ṡ", "s");
            this.translitChars.put("ǌ", "nj");
            this.translitChars.put("ȁ", "a");
            this.translitChars.put("ẗ", "t");
            this.translitChars.put("ĺ", "l");
            this.translitChars.put("ž", "z");
            this.translitChars.put("ᵺ", "th");
            this.translitChars.put("ƌ", "d");
            this.translitChars.put("ș", "s");
            this.translitChars.put("š", "s");
            this.translitChars.put("ᶙ", "u");
            this.translitChars.put("ẽ", "e");
            this.translitChars.put("ẜ", "s");
            this.translitChars.put("ɇ", "e");
            this.translitChars.put("ṷ", "u");
            this.translitChars.put("ố", "o");
            this.translitChars.put("ȿ", "s");
            this.translitChars.put("ᴠ", "v");
            this.translitChars.put("ꝭ", "is");
            this.translitChars.put("ᴏ", "o");
            this.translitChars.put("ɛ", "e");
            this.translitChars.put("ǻ", "a");
            this.translitChars.put("ﬄ", "ffl");
            this.translitChars.put("ⱺ", "o");
            this.translitChars.put("ȋ", "i");
            this.translitChars.put("ᵫ", "ue");
            this.translitChars.put("ȡ", "d");
            this.translitChars.put("ⱬ", "z");
            this.translitChars.put("ẁ", "w");
            this.translitChars.put("ᶏ", "a");
            this.translitChars.put("ꞇ", "t");
            this.translitChars.put("ğ", "g");
            this.translitChars.put("ɳ", "n");
            this.translitChars.put("ʛ", "g");
            this.translitChars.put("ᴜ", "u");
            this.translitChars.put("ф", "f");
            this.translitChars.put("ẩ", "a");
            this.translitChars.put("ṅ", "n");
            this.translitChars.put("ɨ", "i");
            this.translitChars.put("ᴙ", "r");
            this.translitChars.put("ǎ", "a");
            this.translitChars.put("ſ", "s");
            this.translitChars.put("у", "u");
            this.translitChars.put("ȫ", "o");
            this.translitChars.put("ɿ", "r");
            this.translitChars.put("ƭ", "t");
            this.translitChars.put("ḯ", "i");
            this.translitChars.put("ǽ", "ae");
            this.translitChars.put("ⱱ", "v");
            this.translitChars.put("ɶ", "oe");
            this.translitChars.put("ṃ", "m");
            this.translitChars.put("ż", "z");
            this.translitChars.put("ĕ", "e");
            this.translitChars.put("ꜻ", "av");
            this.translitChars.put("ở", "o");
            this.translitChars.put("ễ", "e");
            this.translitChars.put("ɬ", "l");
            this.translitChars.put("ị", "i");
            this.translitChars.put("ᵭ", "d");
            this.translitChars.put("ﬆ", "st");
            this.translitChars.put("ḷ", "l");
            this.translitChars.put("ŕ", "r");
            this.translitChars.put("ᴕ", "ou");
            this.translitChars.put("ʈ", "t");
            this.translitChars.put("ā", "a");
            this.translitChars.put("э", "e");
            this.translitChars.put("ḙ", "e");
            this.translitChars.put("ᴑ", "o");
            this.translitChars.put("ç", "c");
            this.translitChars.put("ᶊ", "s");
            this.translitChars.put("ặ", "a");
            this.translitChars.put("ų", "u");
            this.translitChars.put("ả", "a");
            this.translitChars.put("ǥ", "g");
            this.translitChars.put("р", "r");
            this.translitChars.put("ꝁ", "k");
            this.translitChars.put("ẕ", "z");
            this.translitChars.put("ŝ", "s");
            this.translitChars.put("ḕ", "e");
            this.translitChars.put("ɠ", "g");
            this.translitChars.put("ꝉ", "l");
            this.translitChars.put("ꝼ", "f");
            this.translitChars.put("ᶍ", "x");
            this.translitChars.put("х", "h");
            this.translitChars.put("ǒ", "o");
            this.translitChars.put("ę", "e");
            this.translitChars.put("ổ", "o");
            this.translitChars.put("ƫ", "t");
            this.translitChars.put("ǫ", "o");
            this.translitChars.put("i̇", "i");
            this.translitChars.put("ṇ", "n");
            this.translitChars.put("ć", "c");
            this.translitChars.put("ᵷ", "g");
            this.translitChars.put("ẅ", "w");
            this.translitChars.put("ḑ", "d");
            this.translitChars.put("ḹ", "l");
            this.translitChars.put("ч", "ch");
            this.translitChars.put("œ", "oe");
            this.translitChars.put("ᵳ", "r");
            this.translitChars.put("ļ", "l");
            this.translitChars.put("ȑ", "r");
            this.translitChars.put("ȭ", "o");
            this.translitChars.put("ᵰ", "n");
            this.translitChars.put("ᴁ", "ae");
            this.translitChars.put("ŀ", "l");
            this.translitChars.put("ä", "a");
            this.translitChars.put("ƥ", TtmlNode.TAG_P);
            this.translitChars.put("ỏ", "o");
            this.translitChars.put("į", "i");
            this.translitChars.put("ȓ", "r");
            this.translitChars.put("ǆ", "dz");
            this.translitChars.put("ḡ", "g");
            this.translitChars.put("ṻ", "u");
            this.translitChars.put("ō", "o");
            this.translitChars.put("ľ", "l");
            this.translitChars.put("ẃ", "w");
            this.translitChars.put("ț", "t");
            this.translitChars.put("ń", "n");
            this.translitChars.put("ɍ", "r");
            this.translitChars.put("ȃ", "a");
            this.translitChars.put("ü", "u");
            this.translitChars.put("ꞁ", "l");
            this.translitChars.put("ᴐ", "o");
            this.translitChars.put("ớ", "o");
            this.translitChars.put("ᴃ", "b");
            this.translitChars.put("ɹ", "r");
            this.translitChars.put("ᵲ", "r");
            this.translitChars.put("ʏ", "y");
            this.translitChars.put("ᵮ", "f");
            this.translitChars.put("ⱨ", "h");
            this.translitChars.put("ŏ", "o");
            this.translitChars.put("ú", "u");
            this.translitChars.put("ṛ", "r");
            this.translitChars.put("ʮ", "h");
            this.translitChars.put("ó", "o");
            this.translitChars.put("ů", "u");
            this.translitChars.put("ỡ", "o");
            this.translitChars.put("ṕ", TtmlNode.TAG_P);
            this.translitChars.put("ᶖ", "i");
            this.translitChars.put("ự", "u");
            this.translitChars.put("ã", "a");
            this.translitChars.put("ᵢ", "i");
            this.translitChars.put("ṱ", "t");
            this.translitChars.put("ể", "e");
            this.translitChars.put("ử", "u");
            this.translitChars.put("í", "i");
            this.translitChars.put("ɔ", "o");
            this.translitChars.put("с", "s");
            this.translitChars.put("й", "i");
            this.translitChars.put("ɺ", "r");
            this.translitChars.put("ɢ", "g");
            this.translitChars.put("ř", "r");
            this.translitChars.put("ẖ", "h");
            this.translitChars.put("ű", "u");
            this.translitChars.put("ȍ", "o");
            this.translitChars.put("ш", "sh");
            this.translitChars.put("ḻ", "l");
            this.translitChars.put("ḣ", "h");
            this.translitChars.put("ȶ", "t");
            this.translitChars.put("ņ", "n");
            this.translitChars.put("ᶒ", "e");
            this.translitChars.put("ì", "i");
            this.translitChars.put("ẉ", "w");
            this.translitChars.put("б", "b");
            this.translitChars.put("ē", "e");
            this.translitChars.put("ᴇ", "e");
            this.translitChars.put("ł", "l");
            this.translitChars.put("ộ", "o");
            this.translitChars.put("ɭ", "l");
            this.translitChars.put("ẏ", "y");
            this.translitChars.put("ᴊ", "j");
            this.translitChars.put("ḱ", "k");
            this.translitChars.put("ṿ", "v");
            this.translitChars.put("ȩ", "e");
            this.translitChars.put("â", "a");
            this.translitChars.put("ş", "s");
            this.translitChars.put("ŗ", "r");
            this.translitChars.put("ʋ", "v");
            this.translitChars.put("ₐ", "a");
            this.translitChars.put("ↄ", "c");
            this.translitChars.put("ᶓ", "e");
            this.translitChars.put("ɰ", "m");
            this.translitChars.put("е", "e");
            this.translitChars.put("ᴡ", "w");
            this.translitChars.put("ȏ", "o");
            this.translitChars.put("č", "c");
            this.translitChars.put("ǵ", "g");
            this.translitChars.put("ĉ", "c");
            this.translitChars.put("ю", "yu");
            this.translitChars.put("ᶗ", "o");
            this.translitChars.put("ꝃ", "k");
            this.translitChars.put("ꝙ", "q");
            this.translitChars.put("г", "g");
            this.translitChars.put("ṑ", "o");
            this.translitChars.put("ꜱ", "s");
            this.translitChars.put("ṓ", "o");
            this.translitChars.put("ȟ", "h");
            this.translitChars.put("ő", "o");
            this.translitChars.put("ꜩ", "tz");
            this.translitChars.put("ẻ", "e");
            this.translitChars.put("о", "o");
        }
        StringBuilder dst = new StringBuilder(src.length());
        int len = src.length();
        for (int a = 0; a < len; a++) {
            String ch = src.substring(a, a + 1);
            String tch = (String) this.translitChars.get(ch);
            if (tch != null) {
                dst.append(tch);
            } else {
                dst.append(ch);
            }
        }
        return dst.toString();
    }

    public static String addNbsp(String src) {
        return src.replace(' ', ' ');
    }
}
