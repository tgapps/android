package com.google.android.gms.internal.measurement;

final class zzxx {
    static String zzd(zzud com_google_android_gms_internal_measurement_zzud) {
        zzxz com_google_android_gms_internal_measurement_zzxy = new zzxy(com_google_android_gms_internal_measurement_zzud);
        StringBuilder stringBuilder = new StringBuilder(com_google_android_gms_internal_measurement_zzxy.size());
        for (int i = 0; i < com_google_android_gms_internal_measurement_zzxy.size(); i++) {
            byte zzal = com_google_android_gms_internal_measurement_zzxy.zzal(i);
            switch (zzal) {
                case (byte) 7:
                    stringBuilder.append("\\a");
                    break;
                case (byte) 8:
                    stringBuilder.append("\\b");
                    break;
                case (byte) 9:
                    stringBuilder.append("\\t");
                    break;
                case (byte) 10:
                    stringBuilder.append("\\n");
                    break;
                case (byte) 11:
                    stringBuilder.append("\\v");
                    break;
                case (byte) 12:
                    stringBuilder.append("\\f");
                    break;
                case (byte) 13:
                    stringBuilder.append("\\r");
                    break;
                case (byte) 34:
                    stringBuilder.append("\\\"");
                    break;
                case (byte) 39:
                    stringBuilder.append("\\'");
                    break;
                case (byte) 92:
                    stringBuilder.append("\\\\");
                    break;
                default:
                    if (zzal >= (byte) 32 && zzal <= (byte) 126) {
                        stringBuilder.append((char) zzal);
                        break;
                    }
                    stringBuilder.append('\\');
                    stringBuilder.append((char) (((zzal >>> 6) & 3) + 48));
                    stringBuilder.append((char) (((zzal >>> 3) & 7) + 48));
                    stringBuilder.append((char) ((zzal & 7) + 48));
                    break;
            }
        }
        return stringBuilder.toString();
    }
}
