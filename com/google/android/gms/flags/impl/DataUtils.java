package com.google.android.gms.flags.impl;

import android.content.SharedPreferences;
import android.util.Log;
import com.google.android.gms.flags.impl.util.StrictModeUtil;

public abstract class DataUtils<T> {

    public static class BooleanUtils extends DataUtils<Boolean> {
        public static Boolean getFromSharedPreferencesNoStrict(SharedPreferences sharedPreferences, String str, Boolean bool) {
            try {
                return (Boolean) StrictModeUtil.runWithLaxStrictMode(new zza(sharedPreferences, str, bool));
            } catch (Exception e) {
                str = "FlagDataUtils";
                String str2 = "Flag value not available, returning default: ";
                String valueOf = String.valueOf(e.getMessage());
                Log.w(str, valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2));
                return bool;
            }
        }
    }

    public static class IntegerUtils extends DataUtils<Integer> {
        public static Integer getFromSharedPreferencesNoStrict(SharedPreferences sharedPreferences, String str, Integer num) {
            try {
                return (Integer) StrictModeUtil.runWithLaxStrictMode(new zzb(sharedPreferences, str, num));
            } catch (Exception e) {
                str = "FlagDataUtils";
                String str2 = "Flag value not available, returning default: ";
                String valueOf = String.valueOf(e.getMessage());
                Log.w(str, valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2));
                return num;
            }
        }
    }

    public static class LongUtils extends DataUtils<Long> {
        public static Long getFromSharedPreferencesNoStrict(SharedPreferences sharedPreferences, String str, Long l) {
            try {
                return (Long) StrictModeUtil.runWithLaxStrictMode(new zzc(sharedPreferences, str, l));
            } catch (Exception e) {
                str = "FlagDataUtils";
                String str2 = "Flag value not available, returning default: ";
                String valueOf = String.valueOf(e.getMessage());
                Log.w(str, valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2));
                return l;
            }
        }
    }

    public static class StringUtils extends DataUtils<String> {
        public static String getFromSharedPreferencesNoStrict(SharedPreferences sharedPreferences, String str, String str2) {
            try {
                return (String) StrictModeUtil.runWithLaxStrictMode(new zzd(sharedPreferences, str, str2));
            } catch (Exception e) {
                str = "FlagDataUtils";
                String str3 = "Flag value not available, returning default: ";
                String valueOf = String.valueOf(e.getMessage());
                Log.w(str, valueOf.length() != 0 ? str3.concat(valueOf) : new String(str3));
                return str2;
            }
        }
    }
}
