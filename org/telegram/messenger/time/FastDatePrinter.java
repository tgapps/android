package org.telegram.messenger.time;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.text.FieldPosition;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class FastDatePrinter implements Serializable, DatePrinter {
    public static final int FULL = 0;
    public static final int LONG = 1;
    public static final int MEDIUM = 2;
    public static final int SHORT = 3;
    private static final ConcurrentMap<TimeZoneDisplayKey, String> cTimeZoneDisplayCache = new ConcurrentHashMap(7);
    private static final long serialVersionUID = 1;
    private final Locale mLocale;
    private transient int mMaxLengthEstimate;
    private final String mPattern;
    private transient Rule[] mRules;
    private final TimeZone mTimeZone;

    private interface Rule {
        void appendTo(StringBuffer stringBuffer, Calendar calendar);

        int estimateLength();
    }

    private static class TimeZoneDisplayKey {
        private final Locale mLocale;
        private final int mStyle;
        private final TimeZone mTimeZone;

        TimeZoneDisplayKey(TimeZone timeZone, boolean daylight, int style, Locale locale) {
            this.mTimeZone = timeZone;
            if (daylight) {
                this.mStyle = Integer.MIN_VALUE | style;
            } else {
                this.mStyle = style;
            }
            this.mLocale = locale;
        }

        public int hashCode() {
            return (((this.mStyle * 31) + this.mLocale.hashCode()) * 31) + this.mTimeZone.hashCode();
        }

        public boolean equals(Object obj) {
            boolean z = true;
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof TimeZoneDisplayKey)) {
                return false;
            }
            TimeZoneDisplayKey other = (TimeZoneDisplayKey) obj;
            if (!this.mTimeZone.equals(other.mTimeZone) || this.mStyle != other.mStyle || !this.mLocale.equals(other.mLocale)) {
                z = false;
            }
            return z;
        }
    }

    private static class CharacterLiteral implements Rule {
        private final char mValue;

        CharacterLiteral(char value) {
            this.mValue = value;
        }

        public int estimateLength() {
            return 1;
        }

        public void appendTo(StringBuffer buffer, Calendar calendar) {
            buffer.append(this.mValue);
        }
    }

    private interface NumberRule extends Rule {
        void appendTo(StringBuffer stringBuffer, int i);
    }

    private static class StringLiteral implements Rule {
        private final String mValue;

        StringLiteral(String value) {
            this.mValue = value;
        }

        public int estimateLength() {
            return this.mValue.length();
        }

        public void appendTo(StringBuffer buffer, Calendar calendar) {
            buffer.append(this.mValue);
        }
    }

    private static class TextField implements Rule {
        private final int mField;
        private final String[] mValues;

        TextField(int field, String[] values) {
            this.mField = field;
            this.mValues = values;
        }

        public int estimateLength() {
            int max = 0;
            int i = this.mValues.length;
            while (true) {
                i--;
                if (i < 0) {
                    return max;
                }
                int len = this.mValues[i].length();
                if (len > max) {
                    max = len;
                }
            }
        }

        public void appendTo(StringBuffer buffer, Calendar calendar) {
            buffer.append(this.mValues[calendar.get(this.mField)]);
        }
    }

    private static class TimeZoneNameRule implements Rule {
        private final String mDaylight;
        private final Locale mLocale;
        private final String mStandard;
        private final int mStyle;

        TimeZoneNameRule(TimeZone timeZone, Locale locale, int style) {
            this.mLocale = locale;
            this.mStyle = style;
            this.mStandard = FastDatePrinter.getTimeZoneDisplay(timeZone, false, style, locale);
            this.mDaylight = FastDatePrinter.getTimeZoneDisplay(timeZone, true, style, locale);
        }

        public int estimateLength() {
            return Math.max(this.mStandard.length(), this.mDaylight.length());
        }

        public void appendTo(StringBuffer buffer, Calendar calendar) {
            TimeZone zone = calendar.getTimeZone();
            if (!zone.useDaylightTime() || calendar.get(16) == 0) {
                buffer.append(FastDatePrinter.getTimeZoneDisplay(zone, false, this.mStyle, this.mLocale));
            } else {
                buffer.append(FastDatePrinter.getTimeZoneDisplay(zone, true, this.mStyle, this.mLocale));
            }
        }
    }

    private static class TimeZoneNumberRule implements Rule {
        static final TimeZoneNumberRule INSTANCE_COLON = new TimeZoneNumberRule(true);
        static final TimeZoneNumberRule INSTANCE_NO_COLON = new TimeZoneNumberRule(false);
        final boolean mColon;

        TimeZoneNumberRule(boolean colon) {
            this.mColon = colon;
        }

        public int estimateLength() {
            return 5;
        }

        public void appendTo(StringBuffer buffer, Calendar calendar) {
            int offset = calendar.get(15) + calendar.get(16);
            if (offset < 0) {
                buffer.append('-');
                offset = -offset;
            } else {
                buffer.append('+');
            }
            int hours = offset / 3600000;
            buffer.append((char) ((hours / 10) + 48));
            buffer.append((char) ((hours % 10) + 48));
            if (this.mColon) {
                buffer.append(':');
            }
            int minutes = (offset / 60000) - (60 * hours);
            buffer.append((char) ((minutes / 10) + 48));
            buffer.append((char) ((minutes % 10) + 48));
        }
    }

    private static class PaddedNumberField implements NumberRule {
        private final int mField;
        private final int mSize;

        PaddedNumberField(int field, int size) {
            if (size < 3) {
                throw new IllegalArgumentException();
            }
            this.mField = field;
            this.mSize = size;
        }

        public int estimateLength() {
            return 4;
        }

        public void appendTo(StringBuffer buffer, Calendar calendar) {
            appendTo(buffer, calendar.get(this.mField));
        }

        public final void appendTo(StringBuffer buffer, int value) {
            int i;
            if (value < 100) {
                i = this.mSize;
                while (true) {
                    i--;
                    if (i >= 2) {
                        buffer.append('0');
                    } else {
                        buffer.append((char) ((value / 10) + 48));
                        buffer.append((char) ((value % 10) + 48));
                        return;
                    }
                }
            }
            if (value < 1000) {
                i = 3;
            } else {
                i = Integer.toString(value).length();
            }
            int i2 = this.mSize;
            while (true) {
                i2--;
                if (i2 >= i) {
                    buffer.append('0');
                } else {
                    buffer.append(Integer.toString(value));
                    return;
                }
            }
        }
    }

    private static class TwelveHourField implements NumberRule {
        private final NumberRule mRule;

        TwelveHourField(NumberRule rule) {
            this.mRule = rule;
        }

        public int estimateLength() {
            return this.mRule.estimateLength();
        }

        public void appendTo(StringBuffer buffer, Calendar calendar) {
            int value = calendar.get(10);
            if (value == 0) {
                value = calendar.getLeastMaximum(10) + 1;
            }
            this.mRule.appendTo(buffer, value);
        }

        public void appendTo(StringBuffer buffer, int value) {
            this.mRule.appendTo(buffer, value);
        }
    }

    private static class TwentyFourHourField implements NumberRule {
        private final NumberRule mRule;

        TwentyFourHourField(NumberRule rule) {
            this.mRule = rule;
        }

        public int estimateLength() {
            return this.mRule.estimateLength();
        }

        public void appendTo(StringBuffer buffer, Calendar calendar) {
            int value = calendar.get(11);
            if (value == 0) {
                value = calendar.getMaximum(11) + 1;
            }
            this.mRule.appendTo(buffer, value);
        }

        public void appendTo(StringBuffer buffer, int value) {
            this.mRule.appendTo(buffer, value);
        }
    }

    private static class TwoDigitMonthField implements NumberRule {
        static final TwoDigitMonthField INSTANCE = new TwoDigitMonthField();

        TwoDigitMonthField() {
        }

        public int estimateLength() {
            return 2;
        }

        public void appendTo(StringBuffer buffer, Calendar calendar) {
            appendTo(buffer, calendar.get(2) + 1);
        }

        public final void appendTo(StringBuffer buffer, int value) {
            buffer.append((char) ((value / 10) + 48));
            buffer.append((char) ((value % 10) + 48));
        }
    }

    private static class TwoDigitNumberField implements NumberRule {
        private final int mField;

        TwoDigitNumberField(int field) {
            this.mField = field;
        }

        public int estimateLength() {
            return 2;
        }

        public void appendTo(StringBuffer buffer, Calendar calendar) {
            appendTo(buffer, calendar.get(this.mField));
        }

        public final void appendTo(StringBuffer buffer, int value) {
            if (value < 100) {
                buffer.append((char) ((value / 10) + 48));
                buffer.append((char) ((value % 10) + 48));
                return;
            }
            buffer.append(Integer.toString(value));
        }
    }

    private static class TwoDigitYearField implements NumberRule {
        static final TwoDigitYearField INSTANCE = new TwoDigitYearField();

        TwoDigitYearField() {
        }

        public int estimateLength() {
            return 2;
        }

        public void appendTo(StringBuffer buffer, Calendar calendar) {
            appendTo(buffer, calendar.get(1) % 100);
        }

        public final void appendTo(StringBuffer buffer, int value) {
            buffer.append((char) ((value / 10) + 48));
            buffer.append((char) ((value % 10) + 48));
        }
    }

    private static class UnpaddedMonthField implements NumberRule {
        static final UnpaddedMonthField INSTANCE = new UnpaddedMonthField();

        UnpaddedMonthField() {
        }

        public int estimateLength() {
            return 2;
        }

        public void appendTo(StringBuffer buffer, Calendar calendar) {
            appendTo(buffer, calendar.get(2) + 1);
        }

        public final void appendTo(StringBuffer buffer, int value) {
            if (value < 10) {
                buffer.append((char) (value + 48));
                return;
            }
            buffer.append((char) ((value / 10) + 48));
            buffer.append((char) ((value % 10) + 48));
        }
    }

    private static class UnpaddedNumberField implements NumberRule {
        private final int mField;

        UnpaddedNumberField(int field) {
            this.mField = field;
        }

        public int estimateLength() {
            return 4;
        }

        public void appendTo(StringBuffer buffer, Calendar calendar) {
            appendTo(buffer, calendar.get(this.mField));
        }

        public final void appendTo(StringBuffer buffer, int value) {
            if (value < 10) {
                buffer.append((char) (value + 48));
            } else if (value < 100) {
                buffer.append((char) ((value / 10) + 48));
                buffer.append((char) ((value % 10) + 48));
            } else {
                buffer.append(Integer.toString(value));
            }
        }
    }

    protected java.util.List<org.telegram.messenger.time.FastDatePrinter.Rule> parsePattern() {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.time.FastDatePrinter.parsePattern():java.util.List<org.telegram.messenger.time.FastDatePrinter$Rule>
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
        r0 = r20;
        r1 = new java.text.DateFormatSymbols;
        r2 = r0.mLocale;
        r1.<init>(r2);
        r2 = new java.util.ArrayList;
        r2.<init>();
        r3 = r1.getEras();
        r4 = r1.getMonths();
        r5 = r1.getShortMonths();
        r6 = r1.getWeekdays();
        r7 = r1.getShortWeekdays();
        r8 = r1.getAmPmStrings();
        r9 = r0.mPattern;
        r9 = r9.length();
        r10 = 1;
        r11 = new int[r10];
        r12 = 0;
        r13 = r12;
        if (r13 >= r9) goto L_0x01c0;
    L_0x0033:
        r11[r12] = r13;
        r14 = r0.mPattern;
        r14 = r0.parseToken(r14, r11);
        r13 = r11[r12];
        r15 = r14.length();
        if (r15 != 0) goto L_0x004a;
    L_0x0044:
        r17 = r1;
        r18 = r6;
        goto L_0x01c4;
    L_0x004a:
        r16 = r14.charAt(r12);
        r12 = 4;
        switch(r16) {
            case 39: goto L_0x0191;
            case 68: goto L_0x0185;
            case 69: goto L_0x0174;
            case 70: goto L_0x0169;
            case 71: goto L_0x015e;
            case 72: goto L_0x0153;
            case 75: goto L_0x0148;
            case 77: goto L_0x0128;
            case 83: goto L_0x011c;
            case 87: goto L_0x0112;
            case 90: goto L_0x0104;
            case 97: goto L_0x00f7;
            case 100: goto L_0x00ec;
            case 104: goto L_0x00db;
            case 107: goto L_0x00ca;
            case 109: goto L_0x00be;
            case 115: goto L_0x00b2;
            case 119: goto L_0x00a7;
            case 121: goto L_0x008f;
            case 122: goto L_0x006d;
            default: goto L_0x0052;
        };
    L_0x0052:
        r17 = r1;
        r18 = r6;
        r1 = new java.lang.IllegalArgumentException;
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r10 = "Illegal pattern component: ";
        r6.append(r10);
        r6.append(r14);
        r6 = r6.toString();
        r1.<init>(r6);
        throw r1;
    L_0x006d:
        if (r15 < r12) goto L_0x007f;
    L_0x006f:
        r12 = new org.telegram.messenger.time.FastDatePrinter$TimeZoneNameRule;
        r17 = r1;
        r1 = r0.mTimeZone;
        r18 = r6;
        r6 = r0.mLocale;
        r12.<init>(r1, r6, r10);
        r1 = r12;
        goto L_0x0134;
    L_0x007f:
        r17 = r1;
        r18 = r6;
        r1 = new org.telegram.messenger.time.FastDatePrinter$TimeZoneNameRule;
        r6 = r0.mTimeZone;
        r12 = r0.mLocale;
        r10 = 0;
        r1.<init>(r6, r12, r10);
        goto L_0x018f;
    L_0x008f:
        r17 = r1;
        r18 = r6;
        r1 = 2;
        if (r15 != r1) goto L_0x009a;
    L_0x0096:
        r1 = org.telegram.messenger.time.FastDatePrinter.TwoDigitYearField.INSTANCE;
        goto L_0x0134;
    L_0x009a:
        if (r15 >= r12) goto L_0x009d;
    L_0x009c:
        goto L_0x009e;
    L_0x009d:
        r12 = r15;
    L_0x009e:
        r1 = 1;
        r6 = r0.selectNumberRule(r1, r12);
        r1 = r6;
        goto L_0x018f;
    L_0x00a7:
        r17 = r1;
        r18 = r6;
        r1 = 3;
        r1 = r0.selectNumberRule(r1, r15);
        goto L_0x018f;
    L_0x00b2:
        r17 = r1;
        r18 = r6;
        r1 = 13;
        r1 = r0.selectNumberRule(r1, r15);
        goto L_0x018f;
    L_0x00be:
        r17 = r1;
        r18 = r6;
        r1 = 12;
        r1 = r0.selectNumberRule(r1, r15);
        goto L_0x018f;
    L_0x00ca:
        r17 = r1;
        r18 = r6;
        r1 = new org.telegram.messenger.time.FastDatePrinter$TwentyFourHourField;
        r6 = 11;
        r6 = r0.selectNumberRule(r6, r15);
        r1.<init>(r6);
        goto L_0x018f;
    L_0x00db:
        r17 = r1;
        r18 = r6;
        r1 = new org.telegram.messenger.time.FastDatePrinter$TwelveHourField;
        r6 = 10;
        r6 = r0.selectNumberRule(r6, r15);
        r1.<init>(r6);
        goto L_0x018f;
    L_0x00ec:
        r17 = r1;
        r18 = r6;
        r1 = 5;
        r1 = r0.selectNumberRule(r1, r15);
        goto L_0x018f;
    L_0x00f7:
        r17 = r1;
        r18 = r6;
        r1 = new org.telegram.messenger.time.FastDatePrinter$TextField;
        r6 = 9;
        r1.<init>(r6, r8);
        goto L_0x018f;
    L_0x0104:
        r17 = r1;
        r18 = r6;
        r1 = 1;
        if (r15 != r1) goto L_0x010e;
    L_0x010b:
        r1 = org.telegram.messenger.time.FastDatePrinter.TimeZoneNumberRule.INSTANCE_NO_COLON;
        goto L_0x0134;
    L_0x010e:
        r1 = org.telegram.messenger.time.FastDatePrinter.TimeZoneNumberRule.INSTANCE_COLON;
        goto L_0x018f;
    L_0x0112:
        r17 = r1;
        r18 = r6;
        r1 = r0.selectNumberRule(r12, r15);
        goto L_0x018f;
    L_0x011c:
        r17 = r1;
        r18 = r6;
        r1 = 14;
        r1 = r0.selectNumberRule(r1, r15);
        goto L_0x018f;
    L_0x0128:
        r17 = r1;
        r18 = r6;
        if (r15 < r12) goto L_0x0136;
    L_0x012e:
        r1 = new org.telegram.messenger.time.FastDatePrinter$TextField;
        r6 = 2;
        r1.<init>(r6, r4);
    L_0x0134:
        goto L_0x018f;
    L_0x0136:
        r6 = 2;
        r1 = 3;
        if (r15 != r1) goto L_0x0140;
    L_0x013a:
        r1 = new org.telegram.messenger.time.FastDatePrinter$TextField;
        r1.<init>(r6, r5);
        goto L_0x0134;
    L_0x0140:
        if (r15 != r6) goto L_0x0145;
    L_0x0142:
        r1 = org.telegram.messenger.time.FastDatePrinter.TwoDigitMonthField.INSTANCE;
        goto L_0x0134;
    L_0x0145:
        r1 = org.telegram.messenger.time.FastDatePrinter.UnpaddedMonthField.INSTANCE;
        goto L_0x018f;
    L_0x0148:
        r17 = r1;
        r18 = r6;
        r1 = 10;
        r1 = r0.selectNumberRule(r1, r15);
        goto L_0x018f;
    L_0x0153:
        r17 = r1;
        r18 = r6;
        r1 = 11;
        r1 = r0.selectNumberRule(r1, r15);
        goto L_0x018f;
    L_0x015e:
        r17 = r1;
        r18 = r6;
        r1 = new org.telegram.messenger.time.FastDatePrinter$TextField;
        r6 = 0;
        r1.<init>(r6, r3);
        goto L_0x018f;
    L_0x0169:
        r17 = r1;
        r18 = r6;
        r1 = 8;
        r1 = r0.selectNumberRule(r1, r15);
        goto L_0x018f;
    L_0x0174:
        r17 = r1;
        r18 = r6;
        r1 = new org.telegram.messenger.time.FastDatePrinter$TextField;
        r6 = 7;
        if (r15 >= r12) goto L_0x017f;
    L_0x017d:
        r10 = r7;
        goto L_0x0181;
    L_0x017f:
        r10 = r18;
    L_0x0181:
        r1.<init>(r6, r10);
        goto L_0x018f;
    L_0x0185:
        r17 = r1;
        r18 = r6;
        r1 = 6;
        r1 = r0.selectNumberRule(r1, r15);
    L_0x018f:
        r12 = 0;
        goto L_0x01b3;
    L_0x0191:
        r17 = r1;
        r18 = r6;
        r1 = 1;
        r6 = r14.substring(r1);
        r10 = r6.length();
        if (r10 != r1) goto L_0x01ac;
        r10 = new org.telegram.messenger.time.FastDatePrinter$CharacterLiteral;
        r12 = 0;
        r1 = r6.charAt(r12);
        r10.<init>(r1);
        r1 = r10;
        goto L_0x01b3;
        r12 = 0;
        r1 = new org.telegram.messenger.time.FastDatePrinter$StringLiteral;
        r1.<init>(r6);
        r2.add(r1);
        r13 = r13 + 1;
        r1 = r17;
        r6 = r18;
        r10 = 1;
        goto L_0x0031;
    L_0x01c0:
        r17 = r1;
        r18 = r6;
    L_0x01c4:
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.time.FastDatePrinter.parsePattern():java.util.List<org.telegram.messenger.time.FastDatePrinter$Rule>");
    }

    protected FastDatePrinter(String pattern, TimeZone timeZone, Locale locale) {
        this.mPattern = pattern;
        this.mTimeZone = timeZone;
        this.mLocale = locale;
        init();
    }

    private void init() {
        List<Rule> rulesList = parsePattern();
        this.mRules = (Rule[]) rulesList.toArray(new Rule[rulesList.size()]);
        int len = 0;
        int i = this.mRules.length;
        while (true) {
            i--;
            if (i >= 0) {
                len += this.mRules[i].estimateLength();
            } else {
                this.mMaxLengthEstimate = len;
                return;
            }
        }
    }

    protected String parseToken(String pattern, int[] indexRef) {
        int i;
        StringBuilder buf = new StringBuilder();
        int i2 = indexRef[0];
        int length = pattern.length();
        char c = pattern.charAt(i2);
        char c2;
        if ((c < 'A' || c > 'Z') && (c < 'a' || c > 'z')) {
            buf.append('\'');
            i = i2;
            boolean inLiteral = false;
            while (i < length) {
                c2 = pattern.charAt(i);
                if (c2 != '\'') {
                    if (!inLiteral && ((c2 >= 'A' && c2 <= 'Z') || (c2 >= 'a' && c2 <= 'z'))) {
                        i--;
                        break;
                    }
                    buf.append(c2);
                } else if (i + 1 >= length || pattern.charAt(i + 1) != '\'') {
                    inLiteral = !inLiteral;
                } else {
                    i++;
                    buf.append(c2);
                }
                i++;
            }
        } else {
            buf.append(c);
            while (i2 + 1 < length && pattern.charAt(i2 + 1) == c) {
                buf.append(c);
                i2++;
            }
            c2 = c;
            i = i2;
        }
        indexRef[0] = i;
        return buf.toString();
    }

    protected NumberRule selectNumberRule(int field, int padding) {
        switch (padding) {
            case 1:
                return new UnpaddedNumberField(field);
            case 2:
                return new TwoDigitNumberField(field);
            default:
                return new PaddedNumberField(field, padding);
        }
    }

    public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
        if (obj instanceof Date) {
            return format((Date) obj, toAppendTo);
        }
        if (obj instanceof Calendar) {
            return format((Calendar) obj, toAppendTo);
        }
        if (obj instanceof Long) {
            return format(((Long) obj).longValue(), toAppendTo);
        }
        String str;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unknown class: ");
        if (obj == null) {
            str = "<null>";
        } else {
            str = obj.getClass().getName();
        }
        stringBuilder.append(str);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public String format(long millis) {
        Calendar c = newCalendar();
        c.setTimeInMillis(millis);
        return applyRulesToString(c);
    }

    private String applyRulesToString(Calendar c) {
        return applyRules(c, new StringBuffer(this.mMaxLengthEstimate)).toString();
    }

    private GregorianCalendar newCalendar() {
        return new GregorianCalendar(this.mTimeZone, this.mLocale);
    }

    public String format(Date date) {
        Calendar c = newCalendar();
        c.setTime(date);
        return applyRulesToString(c);
    }

    public String format(Calendar calendar) {
        return format(calendar, new StringBuffer(this.mMaxLengthEstimate)).toString();
    }

    public StringBuffer format(long millis, StringBuffer buf) {
        return format(new Date(millis), buf);
    }

    public StringBuffer format(Date date, StringBuffer buf) {
        Calendar c = newCalendar();
        c.setTime(date);
        return applyRules(c, buf);
    }

    public StringBuffer format(Calendar calendar, StringBuffer buf) {
        return applyRules(calendar, buf);
    }

    protected StringBuffer applyRules(Calendar calendar, StringBuffer buf) {
        for (Rule rule : this.mRules) {
            rule.appendTo(buf, calendar);
        }
        return buf;
    }

    public String getPattern() {
        return this.mPattern;
    }

    public TimeZone getTimeZone() {
        return this.mTimeZone;
    }

    public Locale getLocale() {
        return this.mLocale;
    }

    public int getMaxLengthEstimate() {
        return this.mMaxLengthEstimate;
    }

    public boolean equals(Object obj) {
        boolean z = false;
        if (!(obj instanceof FastDatePrinter)) {
            return false;
        }
        FastDatePrinter other = (FastDatePrinter) obj;
        if (this.mPattern.equals(other.mPattern) && this.mTimeZone.equals(other.mTimeZone) && this.mLocale.equals(other.mLocale)) {
            z = true;
        }
        return z;
    }

    public int hashCode() {
        return this.mPattern.hashCode() + (13 * (this.mTimeZone.hashCode() + (this.mLocale.hashCode() * 13)));
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("FastDatePrinter[");
        stringBuilder.append(this.mPattern);
        stringBuilder.append(",");
        stringBuilder.append(this.mLocale);
        stringBuilder.append(",");
        stringBuilder.append(this.mTimeZone.getID());
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        init();
    }

    static String getTimeZoneDisplay(TimeZone tz, boolean daylight, int style, Locale locale) {
        TimeZoneDisplayKey key = new TimeZoneDisplayKey(tz, daylight, style, locale);
        String value = (String) cTimeZoneDisplayCache.get(key);
        if (value != null) {
            return value;
        }
        value = tz.getDisplayName(daylight, style, locale);
        String prior = (String) cTimeZoneDisplayCache.putIfAbsent(key, value);
        if (prior != null) {
            return prior;
        }
        return value;
    }
}
