package org.telegram.messenger.exoplayer2.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Display.Mode;
import android.view.WindowManager;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Formatter;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.telegram.messenger.exoplayer2.C;
import org.telegram.messenger.exoplayer2.ExoPlayerLibraryInfo;
import org.telegram.messenger.exoplayer2.ParserException;
import org.telegram.messenger.exoplayer2.SeekParameters;

public final class Util {
    private static final int[] CRC32_BYTES_MSBF = new int[]{0, 79764919, 159529838, 222504665, 319059676, 398814059, 445009330, 507990021, 638119352, 583659535, 797628118, 726387553, 890018660, 835552979, 1015980042, 944750013, 1276238704, 1221641927, 1167319070, 1095957929, 1595256236, 1540665371, 1452775106, 1381403509, 1780037320, 1859660671, 1671105958, 1733955601, 2031960084, 2111593891, 1889500026, 1952343757, -1742489888, -1662866601, -1851683442, -1788833735, -1960329156, -1880695413, -2103051438, -2040207643, -1104454824, -1159051537, -1213636554, -1284997759, -1389417084, -1444007885, -1532160278, -1603531939, -734892656, -789352409, -575645954, -646886583, -952755380, -1007220997, -827056094, -898286187, -231047128, -151282273, -71779514, -8804623, -515967244, -436212925, -390279782, -327299027, 881225847, 809987520, 1023691545, 969234094, 662832811, 591600412, 771767749, 717299826, 311336399, 374308984, 453813921, 533576470, 25881363, 88864420, 134795389, 214552010, 2023205639, 2086057648, 1897238633, 1976864222, 1804852699, 1867694188, 1645340341, 1724971778, 1587496639, 1516133128, 1461550545, 1406951526, 1302016099, 1230646740, 1142491917, 1087903418, -1398421865, -1469785312, -1524105735, -1578704818, -1079922613, -1151291908, -1239184603, -1293773166, -1968362705, -1905510760, -2094067647, -2014441994, -1716953613, -1654112188, -1876203875, -1796572374, -525066777, -462094256, -382327159, -302564546, -206542021, -143559028, -97365931, -17609246, -960696225, -1031934488, -817968335, -872425850, -709327229, -780559564, -600130067, -654598054, 1762451694, 1842216281, 1619975040, 1682949687, 2047383090, 2127137669, 1938468188, 2001449195, 1325665622, 1271206113, 1183200824, 1111960463, 1543535498, 1489069629, 1434599652, 1363369299, 622672798, 568075817, 748617968, 677256519, 907627842, 853037301, 1067152940, 995781531, 51762726, 131386257, 177728840, 240578815, 269590778, 349224269, 429104020, 491947555, -248556018, -168932423, -122852000, -60002089, -500490030, -420856475, -341238852, -278395381, -685261898, -739858943, -559578920, -630940305, -1004286614, -1058877219, -845023740, -916395085, -1119974018, -1174433591, -1262701040, -1333941337, -1371866206, -1426332139, -1481064244, -1552294533, -1690935098, -1611170447, -1833673816, -1770699233, -2009983462, -1930228819, -2119160460, -2056179517, 1569362073, 1498123566, 1409854455, 1355396672, 1317987909, 1246755826, 1192025387, 1137557660, 2072149281, 2135122070, 1912620623, 1992383480, 1753615357, 1816598090, 1627664531, 1707420964, 295390185, 358241886, 404320391, 483945776, 43990325, 106832002, 186451547, 266083308, 932423249, 861060070, 1041341759, 986742920, 613929101, 542559546, 756411363, 701822548, -978770311, -1050133554, -869589737, -924188512, -693284699, -764654318, -550540341, -605129092, -475935807, -413084042, -366743377, -287118056, -257573603, -194731862, -114850189, -35218492, -1984365303, -1921392450, -2143631769, -2063868976, -1698919467, -1635936670, -1824608069, -1744851700, -1347415887, -1418654458, -1506661409, -1561119128, -1129027987, -1200260134, -1254728445, -1309196108};
    public static final String DEVICE = Build.DEVICE;
    public static final String DEVICE_DEBUG_INFO;
    private static final Pattern ESCAPED_CHARACTER_PATTERN = Pattern.compile("%([A-Fa-f0-9]{2})");
    public static final String MANUFACTURER = Build.MANUFACTURER;
    public static final String MODEL = Build.MODEL;
    public static final int SDK_INT;
    private static final String TAG = "Util";
    private static final Pattern XS_DATE_TIME_PATTERN = Pattern.compile("(\\d\\d\\d\\d)\\-(\\d\\d)\\-(\\d\\d)[Tt](\\d\\d):(\\d\\d):(\\d\\d)([\\.,](\\d+))?([Zz]|((\\+|\\-)(\\d?\\d):?(\\d\\d)))?");
    private static final Pattern XS_DURATION_PATTERN = Pattern.compile("^(-)?P(([0-9]*)Y)?(([0-9]*)M)?(([0-9]*)D)?(T(([0-9]*)H)?(([0-9]*)M)?(([0-9.]*)S)?)?$");

    public static void closeQuietly(java.io.Closeable r1) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.util.Util.closeQuietly(java.io.Closeable):void
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
        if (r1 == 0) goto L_0x0008;
    L_0x0002:
        r1.close();	 Catch:{ IOException -> 0x0006 }
        goto L_0x0008;
    L_0x0006:
        r0 = move-exception;
        goto L_0x0009;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.util.Util.closeQuietly(java.io.Closeable):void");
    }

    public static void closeQuietly(org.telegram.messenger.exoplayer2.upstream.DataSource r1) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.util.Util.closeQuietly(org.telegram.messenger.exoplayer2.upstream.DataSource):void
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
        if (r1 == 0) goto L_0x0008;
    L_0x0002:
        r1.close();	 Catch:{ IOException -> 0x0006 }
        goto L_0x0008;
    L_0x0006:
        r0 = move-exception;
        goto L_0x0009;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.util.Util.closeQuietly(org.telegram.messenger.exoplayer2.upstream.DataSource):void");
    }

    public static java.lang.String escapeFileName(java.lang.String r1) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.util.Util.escapeFileName(java.lang.String):java.lang.String
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
        r0 = r6.length();
        r1 = 0;
        r2 = 0;
    L_0x0006:
        if (r2 >= r0) goto L_0x0017;
    L_0x0008:
        r3 = r6.charAt(r2);
        r3 = shouldEscapeCharacter(r3);
        if (r3 == 0) goto L_0x0014;
    L_0x0012:
        r1 = r1 + 1;
    L_0x0014:
        r2 = r2 + 1;
        goto L_0x0006;
    L_0x0017:
        if (r1 != 0) goto L_0x001a;
    L_0x0019:
        return r6;
    L_0x001a:
        r2 = 0;
        r3 = new java.lang.StringBuilder;
        r4 = r1 * 2;
        r4 = r4 + r0;
        r3.<init>(r4);
        if (r1 <= 0) goto L_0x0046;
    L_0x0025:
        r4 = r2 + 1;
        r2 = r6.charAt(r2);
        r5 = shouldEscapeCharacter(r2);
        if (r5 == 0) goto L_0x0040;
    L_0x0031:
        r5 = 37;
        r3.append(r5);
        r5 = java.lang.Integer.toHexString(r2);
        r3.append(r5);
        r1 = r1 + -1;
        goto L_0x0043;
    L_0x0040:
        r3.append(r2);
        r2 = r4;
        goto L_0x0023;
    L_0x0046:
        if (r2 >= r0) goto L_0x004b;
        r3.append(r6, r2, r0);
        r4 = r3.toString();
        return r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.util.Util.escapeFileName(java.lang.String):java.lang.String");
    }

    static {
        int i = (VERSION.SDK_INT == 25 && VERSION.CODENAME.charAt(0) == 'O') ? 26 : VERSION.SDK_INT;
        SDK_INT = i;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(DEVICE);
        stringBuilder.append(", ");
        stringBuilder.append(MODEL);
        stringBuilder.append(", ");
        stringBuilder.append(MANUFACTURER);
        stringBuilder.append(", ");
        stringBuilder.append(SDK_INT);
        DEVICE_DEBUG_INFO = stringBuilder.toString();
    }

    private Util() {
    }

    public static byte[] toByteArray(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[4096];
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        while (true) {
            int read = inputStream.read(buffer);
            int bytesRead = read;
            if (read == -1) {
                return outputStream.toByteArray();
            }
            outputStream.write(buffer, 0, bytesRead);
        }
    }

    @TargetApi(23)
    public static boolean maybeRequestReadExternalStoragePermission(Activity activity, Uri... uris) {
        if (SDK_INT < 23) {
            return false;
        }
        for (Uri uri : uris) {
            if (isLocalFileUri(uri)) {
                if (activity.checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") != 0) {
                    activity.requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, 0);
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    public static boolean isLocalFileUri(Uri uri) {
        String scheme = uri.getScheme();
        if (!TextUtils.isEmpty(scheme)) {
            if (!scheme.equals("file")) {
                return false;
            }
        }
        return true;
    }

    public static boolean areEqual(Object o1, Object o2) {
        if (o1 == null) {
            return o2 == null;
        } else {
            return o1.equals(o2);
        }
    }

    public static boolean contains(Object[] items, Object item) {
        for (Object arrayItem : items) {
            if (areEqual(arrayItem, item)) {
                return true;
            }
        }
        return false;
    }

    public static <T> void removeRange(List<T> list, int fromIndex, int toIndex) {
        list.subList(fromIndex, toIndex).clear();
    }

    public static ExecutorService newSingleThreadExecutor(final String threadName) {
        return Executors.newSingleThreadExecutor(new ThreadFactory() {
            public Thread newThread(Runnable r) {
                return new Thread(r, threadName);
            }
        });
    }

    public static String normalizeLanguageCode(String language) {
        String str;
        if (language == null) {
            str = null;
        } else {
            try {
                str = new Locale(language).getISO3Language();
            } catch (MissingResourceException e) {
                return language.toLowerCase();
            }
        }
        return str;
    }

    public static String fromUtf8Bytes(byte[] bytes) {
        return new String(bytes, Charset.forName(C.UTF8_NAME));
    }

    public static byte[] getUtf8Bytes(String value) {
        return value.getBytes(Charset.forName(C.UTF8_NAME));
    }

    public static boolean isLinebreak(int c) {
        if (c != 10) {
            if (c != 13) {
                return false;
            }
        }
        return true;
    }

    public static String toLowerInvariant(String text) {
        return text == null ? null : text.toLowerCase(Locale.US);
    }

    public static int ceilDivide(int numerator, int denominator) {
        return ((numerator + denominator) - 1) / denominator;
    }

    public static long ceilDivide(long numerator, long denominator) {
        return ((numerator + denominator) - 1) / denominator;
    }

    public static int constrainValue(int value, int min, int max) {
        return Math.max(min, Math.min(value, max));
    }

    public static long constrainValue(long value, long min, long max) {
        return Math.max(min, Math.min(value, max));
    }

    public static float constrainValue(float value, float min, float max) {
        return Math.max(min, Math.min(value, max));
    }

    public static long addWithOverflowDefault(long x, long y, long overflowResult) {
        long result = x + y;
        if (((x ^ result) & (y ^ result)) < 0) {
            return overflowResult;
        }
        return result;
    }

    public static long subtractWithOverflowDefault(long x, long y, long overflowResult) {
        long result = x - y;
        if (((x ^ y) & (x ^ result)) < 0) {
            return overflowResult;
        }
        return result;
    }

    public static int binarySearchFloor(int[] array, int value, boolean inclusive, boolean stayInBounds) {
        int index = Arrays.binarySearch(array, value);
        if (index < 0) {
            index = -(index + 2);
        } else {
            while (true) {
                index--;
                if (index < 0 || array[index] != value) {
                    if (inclusive) {
                        index++;
                    }
                }
            }
            if (inclusive) {
                index++;
            }
        }
        return stayInBounds ? Math.max(0, index) : index;
    }

    public static int binarySearchFloor(long[] array, long value, boolean inclusive, boolean stayInBounds) {
        int index = Arrays.binarySearch(array, value);
        if (index < 0) {
            index = -(index + 2);
        } else {
            while (true) {
                index--;
                if (index < 0 || array[index] != value) {
                    if (inclusive) {
                        index++;
                    }
                }
            }
            if (inclusive) {
                index++;
            }
        }
        return stayInBounds ? Math.max(0, index) : index;
    }

    public static <T> int binarySearchFloor(List<? extends Comparable<? super T>> list, T value, boolean inclusive, boolean stayInBounds) {
        int index = Collections.binarySearch(list, value);
        if (index < 0) {
            index = -(index + 2);
        } else {
            while (true) {
                index--;
                if (index < 0 || ((Comparable) list.get(index)).compareTo(value) != 0) {
                    if (inclusive) {
                        index++;
                    }
                }
            }
            if (inclusive) {
                index++;
            }
        }
        return stayInBounds ? Math.max(0, index) : index;
    }

    public static int binarySearchCeil(long[] array, long value, boolean inclusive, boolean stayInBounds) {
        int index = Arrays.binarySearch(array, value);
        if (index < 0) {
            index ^= -1;
        } else {
            while (true) {
                index++;
                if (index >= array.length || array[index] != value) {
                    if (inclusive) {
                        index--;
                    }
                }
            }
            if (inclusive) {
                index--;
            }
        }
        return stayInBounds ? Math.min(array.length - 1, index) : index;
    }

    public static <T> int binarySearchCeil(List<? extends Comparable<? super T>> list, T value, boolean inclusive, boolean stayInBounds) {
        int index = Collections.binarySearch(list, value);
        if (index < 0) {
            index ^= -1;
        } else {
            int listSize = list.size();
            while (true) {
                index++;
                if (index >= listSize || ((Comparable) list.get(index)).compareTo(value) != 0) {
                    if (inclusive) {
                        index--;
                    }
                }
            }
            if (inclusive) {
                index--;
            }
        }
        return stayInBounds ? Math.min(list.size() - 1, index) : index;
    }

    public static int compareLong(long left, long right) {
        if (left < right) {
            return -1;
        }
        return left == right ? 0 : 1;
    }

    public static long parseXsDuration(String value) {
        Matcher matcher = XS_DURATION_PATTERN.matcher(value);
        if (!matcher.matches()) {
            return (long) ((Double.parseDouble(value) * 3600.0d) * 1000.0d);
        }
        boolean negated = true ^ TextUtils.isEmpty(matcher.group(1));
        String years = matcher.group(3);
        double d = 0.0d;
        double durationSeconds = years != null ? Double.parseDouble(years) * 3.1556908E7d : 0.0d;
        String months = matcher.group(5);
        durationSeconds += months != null ? Double.parseDouble(months) * 2629739.0d : 0.0d;
        String days = matcher.group(7);
        durationSeconds += days != null ? Double.parseDouble(days) * 86400.0d : 0.0d;
        String hours = matcher.group(10);
        durationSeconds += hours != null ? 3600.0d * Double.parseDouble(hours) : 0.0d;
        String minutes = matcher.group(12);
        durationSeconds += minutes != null ? Double.parseDouble(minutes) * 60.0d : 0.0d;
        String seconds = matcher.group(14);
        if (seconds != null) {
            d = Double.parseDouble(seconds);
        }
        long durationMillis = (long) (4652007308841189376L * (durationSeconds + d));
        return negated ? -durationMillis : durationMillis;
    }

    public static long parseXsDateTime(String value) throws ParserException {
        Matcher matcher = XS_DATE_TIME_PATTERN.matcher(value);
        if (matcher.matches()) {
            int timezoneShift;
            Calendar dateTime;
            StringBuilder stringBuilder;
            long time;
            if (matcher.group(9) == null) {
                timezoneShift = 0;
            } else if (matcher.group(9).equalsIgnoreCase("Z")) {
                timezoneShift = 0;
            } else {
                timezoneShift = (Integer.parseInt(matcher.group(12)) * 60) + Integer.parseInt(matcher.group(13));
                if (matcher.group(11).equals("-")) {
                    timezoneShift *= -1;
                }
                dateTime = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
                dateTime.clear();
                dateTime.set(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)) - 1, Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(4)), Integer.parseInt(matcher.group(5)), Integer.parseInt(matcher.group(6)));
                if (!TextUtils.isEmpty(matcher.group(8))) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("0.");
                    stringBuilder.append(matcher.group(8));
                    dateTime.set(14, new BigDecimal(stringBuilder.toString()).movePointRight(3).intValue());
                }
                time = dateTime.getTimeInMillis();
                if (timezoneShift == 0) {
                    return time - ((long) (60000 * timezoneShift));
                }
                return time;
            }
            dateTime = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
            dateTime.clear();
            dateTime.set(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)) - 1, Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(4)), Integer.parseInt(matcher.group(5)), Integer.parseInt(matcher.group(6)));
            if (TextUtils.isEmpty(matcher.group(8))) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("0.");
                stringBuilder.append(matcher.group(8));
                dateTime.set(14, new BigDecimal(stringBuilder.toString()).movePointRight(3).intValue());
            }
            time = dateTime.getTimeInMillis();
            if (timezoneShift == 0) {
                return time;
            }
            return time - ((long) (60000 * timezoneShift));
        }
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("Invalid date/time format: ");
        stringBuilder2.append(value);
        throw new ParserException(stringBuilder2.toString());
    }

    public static long scaleLargeTimestamp(long timestamp, long multiplier, long divisor) {
        if (divisor >= multiplier && divisor % multiplier == 0) {
            return timestamp / (divisor / multiplier);
        }
        if (divisor < multiplier && multiplier % divisor == 0) {
            return timestamp * (multiplier / divisor);
        }
        return (long) (((double) timestamp) * (((double) multiplier) / ((double) divisor)));
    }

    public static long[] scaleLargeTimestamps(List<Long> timestamps, long multiplier, long divisor) {
        long[] scaledTimestamps = new long[timestamps.size()];
        int i = 0;
        long multiplicationFactor;
        int i2;
        if (divisor < multiplier || divisor % multiplier != 0) {
            if (divisor < multiplier && multiplier % divisor == 0) {
                multiplicationFactor = multiplier / divisor;
                while (true) {
                    i2 = i;
                    if (i2 >= scaledTimestamps.length) {
                        break;
                    }
                    scaledTimestamps[i2] = ((Long) timestamps.get(i2)).longValue() * multiplicationFactor;
                    i = i2 + 1;
                }
            } else {
                double multiplicationFactor2 = ((double) multiplier) / ((double) divisor);
                while (true) {
                    i2 = i;
                    if (i2 >= scaledTimestamps.length) {
                        break;
                    }
                    scaledTimestamps[i2] = (long) (((double) ((Long) timestamps.get(i2)).longValue()) * multiplicationFactor2);
                    i = i2 + 1;
                }
            }
        } else {
            multiplicationFactor = divisor / multiplier;
            while (true) {
                i2 = i;
                if (i2 >= scaledTimestamps.length) {
                    break;
                }
                scaledTimestamps[i2] = ((Long) timestamps.get(i2)).longValue() / multiplicationFactor;
                i = i2 + 1;
            }
        }
        return scaledTimestamps;
    }

    public static void scaleLargeTimestampsInPlace(long[] timestamps, long multiplier, long divisor) {
        int i = 0;
        long divisionFactor;
        int i2;
        if (divisor >= multiplier && divisor % multiplier == 0) {
            divisionFactor = divisor / multiplier;
            while (true) {
                i2 = i;
                if (i2 < timestamps.length) {
                    timestamps[i2] = timestamps[i2] / divisionFactor;
                    i = i2 + 1;
                } else {
                    return;
                }
            }
        } else if (divisor >= multiplier || multiplier % divisor != 0) {
            double multiplicationFactor = ((double) multiplier) / ((double) divisor);
            while (true) {
                i2 = i;
                if (i2 < timestamps.length) {
                    timestamps[i2] = (long) (((double) timestamps[i2]) * multiplicationFactor);
                    i = i2 + 1;
                } else {
                    return;
                }
            }
        } else {
            divisionFactor = multiplier / divisor;
            while (true) {
                i2 = i;
                if (i2 < timestamps.length) {
                    timestamps[i2] = timestamps[i2] * divisionFactor;
                    i = i2 + 1;
                } else {
                    return;
                }
            }
        }
    }

    public static long getMediaDurationForPlayoutDuration(long playoutDuration, float speed) {
        if (speed == 1.0f) {
            return playoutDuration;
        }
        return Math.round(((double) playoutDuration) * ((double) speed));
    }

    public static long getPlayoutDurationForMediaDuration(long mediaDuration, float speed) {
        if (speed == 1.0f) {
            return mediaDuration;
        }
        return Math.round(((double) mediaDuration) / ((double) speed));
    }

    public static long resolveSeekPositionUs(long positionUs, SeekParameters seekParameters, long firstSyncUs, long secondSyncUs) {
        SeekParameters seekParameters2 = seekParameters;
        if (SeekParameters.EXACT.equals(seekParameters2)) {
            return positionUs;
        }
        long maxPositionUs = positionUs;
        long minPositionUs = subtractWithOverflowDefault(maxPositionUs, seekParameters2.toleranceBeforeUs, Long.MIN_VALUE);
        maxPositionUs = addWithOverflowDefault(maxPositionUs, seekParameters2.toleranceAfterUs, Long.MAX_VALUE);
        boolean secondSyncPositionValid = false;
        boolean firstSyncPositionValid = minPositionUs <= firstSyncUs && firstSyncUs <= maxPositionUs;
        if (minPositionUs <= secondSyncUs && secondSyncUs <= maxPositionUs) {
            secondSyncPositionValid = true;
        }
        if (firstSyncPositionValid && secondSyncPositionValid) {
            if (Math.abs(firstSyncUs - positionUs) <= Math.abs(secondSyncUs - positionUs)) {
                return firstSyncUs;
            }
            return secondSyncUs;
        }
        if (firstSyncPositionValid) {
            return firstSyncUs;
        }
        if (secondSyncPositionValid) {
            return secondSyncUs;
        }
        return minPositionUs;
    }

    public static int[] toArray(List<Integer> list) {
        if (list == null) {
            return null;
        }
        int length = list.size();
        int[] intArray = new int[length];
        for (int i = 0; i < length; i++) {
            intArray[i] = ((Integer) list.get(i)).intValue();
        }
        return intArray;
    }

    public static int getIntegerCodeForString(String string) {
        int length = string.length();
        int i = 0;
        Assertions.checkArgument(length <= 4);
        int result = 0;
        while (i < length) {
            result = (result << 8) | string.charAt(i);
            i++;
        }
        return result;
    }

    public static byte[] getBytesFromHexString(String hexString) {
        byte[] data = new byte[(hexString.length() / 2)];
        for (int i = 0; i < data.length; i++) {
            int stringOffset = i * 2;
            data[i] = (byte) ((Character.digit(hexString.charAt(stringOffset), 16) << 4) + Character.digit(hexString.charAt(stringOffset + 1), 16));
        }
        return data;
    }

    public static String getCommaDelimitedSimpleClassNames(Object[] objects) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < objects.length; i++) {
            stringBuilder.append(objects[i].getClass().getSimpleName());
            if (i < objects.length - 1) {
                stringBuilder.append(", ");
            }
        }
        return stringBuilder.toString();
    }

    public static String getUserAgent(Context context, String applicationName) {
        String versionName;
        try {
            versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            versionName = "?";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(applicationName);
        stringBuilder.append("/");
        stringBuilder.append(versionName);
        stringBuilder.append(" (Linux;Android ");
        stringBuilder.append(VERSION.RELEASE);
        stringBuilder.append(") ");
        stringBuilder.append(ExoPlayerLibraryInfo.VERSION_SLASHY);
        return stringBuilder.toString();
    }

    public static String getCodecsOfType(String codecs, int trackType) {
        String str = null;
        if (TextUtils.isEmpty(codecs)) {
            return null;
        }
        String[] codecArray = codecs.trim().split("(\\s*,\\s*)");
        StringBuilder builder = new StringBuilder();
        for (String codec : codecArray) {
            if (trackType == MimeTypes.getTrackTypeOfCodec(codec)) {
                if (builder.length() > 0) {
                    builder.append(",");
                }
                builder.append(codec);
            }
        }
        if (builder.length() > 0) {
            str = builder.toString();
        }
        return str;
    }

    public static int getPcmEncoding(int bitDepth) {
        if (bitDepth == 8) {
            return 3;
        }
        if (bitDepth == 16) {
            return 2;
        }
        if (bitDepth == 24) {
            return Integer.MIN_VALUE;
        }
        if (bitDepth != 32) {
            return 0;
        }
        return 1073741824;
    }

    public static boolean isEncodingHighResolutionIntegerPcm(int encoding) {
        if (encoding != Integer.MIN_VALUE) {
            if (encoding != 1073741824) {
                return false;
            }
        }
        return true;
    }

    public static int getPcmFrameSize(int pcmEncoding, int channelCount) {
        if (pcmEncoding == Integer.MIN_VALUE) {
            return channelCount * 3;
        }
        if (pcmEncoding != 1073741824) {
            switch (pcmEncoding) {
                case 2:
                    return channelCount * 2;
                case 3:
                    return channelCount;
                case 4:
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
        return channelCount * 4;
    }

    public static int getAudioUsageForStreamType(int streamType) {
        switch (streamType) {
            case 0:
                return 2;
            case 1:
                return 13;
            case 2:
                return 6;
            case 4:
                return 4;
            case 5:
                return 5;
            case 8:
                return 3;
            default:
                return 1;
        }
    }

    public static int getAudioContentTypeForStreamType(int streamType) {
        switch (streamType) {
            case 0:
                return 1;
            case 1:
            case 2:
            case 4:
            case 5:
            case 8:
                return 4;
            default:
                return 2;
        }
    }

    public static int getStreamTypeForAudioUsage(int usage) {
        switch (usage) {
            case 1:
            case 12:
            case 14:
                return 3;
            case 2:
                return 0;
            case 3:
                return 8;
            case 4:
                return 4;
            case 5:
            case 7:
            case 8:
            case 9:
            case 10:
                return 5;
            case 6:
                return 2;
            case 13:
                return 1;
            default:
                return 3;
        }
    }

    public static UUID getDrmUuid(String drmScheme) {
        Object obj;
        String toLowerInvariant = toLowerInvariant(drmScheme);
        int hashCode = toLowerInvariant.hashCode();
        if (hashCode != -1860423953) {
            if (hashCode != -1400551171) {
                if (hashCode == 790309106) {
                    if (toLowerInvariant.equals("clearkey")) {
                        obj = 2;
                        switch (obj) {
                            case null:
                                return C.WIDEVINE_UUID;
                            case 1:
                                return C.PLAYREADY_UUID;
                            case 2:
                                return C.CLEARKEY_UUID;
                            default:
                                try {
                                    return UUID.fromString(drmScheme);
                                } catch (RuntimeException e) {
                                    return null;
                                }
                        }
                    }
                }
            } else if (toLowerInvariant.equals("widevine")) {
                obj = null;
                switch (obj) {
                    case null:
                        return C.WIDEVINE_UUID;
                    case 1:
                        return C.PLAYREADY_UUID;
                    case 2:
                        return C.CLEARKEY_UUID;
                    default:
                        return UUID.fromString(drmScheme);
                }
            }
        } else if (toLowerInvariant.equals("playready")) {
            obj = 1;
            switch (obj) {
                case null:
                    return C.WIDEVINE_UUID;
                case 1:
                    return C.PLAYREADY_UUID;
                case 2:
                    return C.CLEARKEY_UUID;
                default:
                    return UUID.fromString(drmScheme);
            }
        }
        obj = -1;
        switch (obj) {
            case null:
                return C.WIDEVINE_UUID;
            case 1:
                return C.PLAYREADY_UUID;
            case 2:
                return C.CLEARKEY_UUID;
            default:
                return UUID.fromString(drmScheme);
        }
    }

    public static int inferContentType(Uri uri) {
        String path = uri.getPath();
        return path == null ? 3 : inferContentType(path);
    }

    public static int inferContentType(String fileName) {
        fileName = toLowerInvariant(fileName);
        if (fileName.endsWith(".mpd")) {
            return 0;
        }
        if (fileName.endsWith(".m3u8")) {
            return 2;
        }
        if (fileName.matches(".*\\.ism(l)?(/manifest(\\(.+\\))?)?")) {
            return 1;
        }
        return 3;
    }

    public static String getStringForTime(StringBuilder builder, Formatter formatter, long timeMs) {
        long timeMs2;
        Formatter formatter2 = formatter;
        if (timeMs == C.TIME_UNSET) {
            timeMs2 = 0;
        } else {
            timeMs2 = timeMs;
        }
        long totalSeconds = (timeMs2 + 500) / 1000;
        long seconds = totalSeconds % 60;
        long minutes = (totalSeconds / 60) % 60;
        long hours = totalSeconds / 3600;
        builder.setLength(0);
        if (hours > 0) {
            return formatter2.format("%d:%02d:%02d", new Object[]{Long.valueOf(hours), Long.valueOf(minutes), Long.valueOf(seconds)}).toString();
        }
        return formatter2.format("%02d:%02d", new Object[]{Long.valueOf(minutes), Long.valueOf(seconds)}).toString();
    }

    public static int getDefaultBufferSize(int trackType) {
        switch (trackType) {
            case 0:
                return 16777216;
            case 1:
                return C.DEFAULT_AUDIO_BUFFER_SIZE;
            case 2:
                return C.DEFAULT_VIDEO_BUFFER_SIZE;
            case 3:
                return 131072;
            case 4:
                return 131072;
            default:
                throw new IllegalStateException();
        }
    }

    private static boolean shouldEscapeCharacter(char c) {
        if (!(c == '\"' || c == '%' || c == '*' || c == '/' || c == ':' || c == '<' || c == '\\' || c == '|')) {
            switch (c) {
                case '>':
                case '?':
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

    public static String unescapeFileName(String fileName) {
        int i;
        int length = fileName.length();
        int startOfNotEscaped = 0;
        int percentCharacterCount = 0;
        for (i = 0; i < length; i++) {
            if (fileName.charAt(i) == '%') {
                percentCharacterCount++;
            }
        }
        if (percentCharacterCount == 0) {
            return fileName;
        }
        i = length - (percentCharacterCount * 2);
        StringBuilder builder = new StringBuilder(i);
        Matcher matcher = ESCAPED_CHARACTER_PATTERN.matcher(fileName);
        while (percentCharacterCount > 0 && matcher.find()) {
            char unescapedCharacter = (char) Integer.parseInt(matcher.group(1), 16);
            builder.append(fileName, startOfNotEscaped, matcher.start());
            builder.append(unescapedCharacter);
            startOfNotEscaped = matcher.end();
            percentCharacterCount--;
        }
        if (startOfNotEscaped < length) {
            builder.append(fileName, startOfNotEscaped, length);
        }
        if (builder.length() != i) {
            return null;
        }
        return builder.toString();
    }

    public static void sneakyThrow(Throwable t) {
        sneakyThrowInternal(t);
    }

    private static <T extends Throwable> void sneakyThrowInternal(Throwable t) throws Throwable {
        throw t;
    }

    public static void recursiveDelete(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                recursiveDelete(child);
            }
        }
        fileOrDirectory.delete();
    }

    public static File createTempDirectory(Context context, String prefix) throws IOException {
        File tempFile = createTempFile(context, prefix);
        tempFile.delete();
        tempFile.mkdir();
        return tempFile;
    }

    public static File createTempFile(Context context, String prefix) throws IOException {
        return File.createTempFile(prefix, null, context.getCacheDir());
    }

    public static int crc(byte[] bytes, int start, int end, int initialValue) {
        int initialValue2 = initialValue;
        for (initialValue = start; initialValue < end; initialValue++) {
            initialValue2 = (initialValue2 << 8) ^ CRC32_BYTES_MSBF[((initialValue2 >>> 24) ^ (bytes[initialValue] & 255)) & 255];
        }
        return initialValue2;
    }

    public static Point getPhysicalDisplaySize(Context context) {
        return getPhysicalDisplaySize(context, ((WindowManager) context.getSystemService("window")).getDefaultDisplay());
    }

    public static Point getPhysicalDisplaySize(Context context, Display display) {
        if (SDK_INT < 25 && display.getDisplayId() == 0) {
            if ("Sony".equals(MANUFACTURER) && MODEL.startsWith("BRAVIA") && context.getPackageManager().hasSystemFeature("com.sony.dtv.hardware.panel.qfhd")) {
                return new Point(3840, 2160);
            }
            if ("NVIDIA".equals(MANUFACTURER) && MODEL.contains("SHIELD")) {
                String sysDisplaySize = null;
                try {
                    Class<?> systemProperties = Class.forName("android.os.SystemProperties");
                    sysDisplaySize = (String) systemProperties.getMethod("get", new Class[]{String.class}).invoke(systemProperties, new Object[]{"sys.display-size"});
                } catch (Exception e) {
                    Log.e(TAG, "Failed to read sys.display-size", e);
                }
                if (!TextUtils.isEmpty(sysDisplaySize)) {
                    try {
                        String[] sysDisplaySizeParts = sysDisplaySize.trim().split("x");
                        if (sysDisplaySizeParts.length == 2) {
                            int width = Integer.parseInt(sysDisplaySizeParts[0]);
                            int height = Integer.parseInt(sysDisplaySizeParts[1]);
                            if (width > 0 && height > 0) {
                                return new Point(width, height);
                            }
                        }
                    } catch (NumberFormatException e2) {
                    }
                    String str = TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Invalid sys.display-size: ");
                    stringBuilder.append(sysDisplaySize);
                    Log.e(str, stringBuilder.toString());
                }
            }
        }
        Point displaySize = new Point();
        if (SDK_INT >= 23) {
            getDisplaySizeV23(display, displaySize);
        } else if (SDK_INT >= 17) {
            getDisplaySizeV17(display, displaySize);
        } else if (SDK_INT >= 16) {
            getDisplaySizeV16(display, displaySize);
        } else {
            getDisplaySizeV9(display, displaySize);
        }
        return displaySize;
    }

    @TargetApi(23)
    private static void getDisplaySizeV23(Display display, Point outSize) {
        Mode mode = display.getMode();
        outSize.x = mode.getPhysicalWidth();
        outSize.y = mode.getPhysicalHeight();
    }

    @TargetApi(17)
    private static void getDisplaySizeV17(Display display, Point outSize) {
        display.getRealSize(outSize);
    }

    @TargetApi(16)
    private static void getDisplaySizeV16(Display display, Point outSize) {
        display.getSize(outSize);
    }

    private static void getDisplaySizeV9(Display display, Point outSize) {
        outSize.x = display.getWidth();
        outSize.y = display.getHeight();
    }
}
