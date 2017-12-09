package com.google.android.gms.internal;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.TreeMap;
import org.apache.http.Header;
import org.apache.http.HttpEntity;

public final class zzad implements zzk {
    private static boolean DEBUG = zzab.DEBUG;
    private static int zzam = 3000;
    private static int zzan = 4096;
    private zzan zzao;
    private zzae zzap;

    public zzad(zzan com_google_android_gms_internal_zzan) {
        this(com_google_android_gms_internal_zzan, new zzae(zzan));
    }

    private zzad(zzan com_google_android_gms_internal_zzan, zzae com_google_android_gms_internal_zzae) {
        this.zzao = com_google_android_gms_internal_zzan;
        this.zzap = com_google_android_gms_internal_zzae;
    }

    private static Map<String, String> zza(Header[] headerArr) {
        Map<String, String> treeMap = new TreeMap(String.CASE_INSENSITIVE_ORDER);
        for (int i = 0; i < headerArr.length; i++) {
            treeMap.put(headerArr[i].getName(), headerArr[i].getValue());
        }
        return treeMap;
    }

    private static void zza(String str, zzp<?> com_google_android_gms_internal_zzp_, zzaa com_google_android_gms_internal_zzaa) throws zzaa {
        zzx zzj = com_google_android_gms_internal_zzp_.zzj();
        int zzi = com_google_android_gms_internal_zzp_.zzi();
        try {
            zzj.zza(com_google_android_gms_internal_zzaa);
            com_google_android_gms_internal_zzp_.zzb(String.format("%s-retry [timeout=%s]", new Object[]{str, Integer.valueOf(zzi)}));
        } catch (zzaa e) {
            com_google_android_gms_internal_zzp_.zzb(String.format("%s-timeout-giveup [timeout=%s]", new Object[]{str, Integer.valueOf(zzi)}));
            throw e;
        }
    }

    private final byte[] zza(HttpEntity httpEntity) throws IOException, zzy {
        zzaq com_google_android_gms_internal_zzaq = new zzaq(this.zzap, (int) httpEntity.getContentLength());
        byte[] bArr = null;
        try {
            InputStream content = httpEntity.getContent();
            if (content == null) {
                throw new zzy();
            }
            bArr = this.zzap.zzb(1024);
            while (true) {
                int read = content.read(bArr);
                if (read == -1) {
                    break;
                }
                com_google_android_gms_internal_zzaq.write(bArr, 0, read);
            }
            byte[] toByteArray = com_google_android_gms_internal_zzaq.toByteArray();
            return toByteArray;
        } finally {
            try {
                httpEntity.consumeContent();
            } catch (IOException e) {
                zzab.zza("Error occured when calling consumingContent", new Object[0]);
            }
            this.zzap.zza(bArr);
            com_google_android_gms_internal_zzaq.close();
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final com.google.android.gms.internal.zzn zza(com.google.android.gms.internal.zzp<?> r19) throws com.google.android.gms.internal.zzaa {
        /*
        r18 = this;
        r16 = android.os.SystemClock.elapsedRealtime();
    L_0x0004:
        r3 = 0;
        r14 = 0;
        r6 = java.util.Collections.emptyMap();
        r2 = new java.util.HashMap;	 Catch:{ SocketTimeoutException -> 0x00ed, ConnectTimeoutException -> 0x0113, MalformedURLException -> 0x0123, IOException -> 0x0146 }
        r2.<init>();	 Catch:{ SocketTimeoutException -> 0x00ed, ConnectTimeoutException -> 0x0113, MalformedURLException -> 0x0123, IOException -> 0x0146 }
        r4 = r19.zze();	 Catch:{ SocketTimeoutException -> 0x00ed, ConnectTimeoutException -> 0x0113, MalformedURLException -> 0x0123, IOException -> 0x0146 }
        if (r4 == 0) goto L_0x003a;
    L_0x0015:
        r5 = r4.zza;	 Catch:{ SocketTimeoutException -> 0x00ed, ConnectTimeoutException -> 0x0113, MalformedURLException -> 0x0123, IOException -> 0x0146 }
        if (r5 == 0) goto L_0x0021;
    L_0x0019:
        r5 = "If-None-Match";
        r7 = r4.zza;	 Catch:{ SocketTimeoutException -> 0x00ed, ConnectTimeoutException -> 0x0113, MalformedURLException -> 0x0123, IOException -> 0x0146 }
        r2.put(r5, r7);	 Catch:{ SocketTimeoutException -> 0x00ed, ConnectTimeoutException -> 0x0113, MalformedURLException -> 0x0123, IOException -> 0x0146 }
    L_0x0021:
        r8 = r4.zzc;	 Catch:{ SocketTimeoutException -> 0x00ed, ConnectTimeoutException -> 0x0113, MalformedURLException -> 0x0123, IOException -> 0x0146 }
        r10 = 0;
        r5 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1));
        if (r5 <= 0) goto L_0x003a;
    L_0x0029:
        r5 = new java.util.Date;	 Catch:{ SocketTimeoutException -> 0x00ed, ConnectTimeoutException -> 0x0113, MalformedURLException -> 0x0123, IOException -> 0x0146 }
        r8 = r4.zzc;	 Catch:{ SocketTimeoutException -> 0x00ed, ConnectTimeoutException -> 0x0113, MalformedURLException -> 0x0123, IOException -> 0x0146 }
        r5.<init>(r8);	 Catch:{ SocketTimeoutException -> 0x00ed, ConnectTimeoutException -> 0x0113, MalformedURLException -> 0x0123, IOException -> 0x0146 }
        r4 = "If-Modified-Since";
        r5 = org.apache.http.impl.cookie.DateUtils.formatDate(r5);	 Catch:{ SocketTimeoutException -> 0x00ed, ConnectTimeoutException -> 0x0113, MalformedURLException -> 0x0123, IOException -> 0x0146 }
        r2.put(r4, r5);	 Catch:{ SocketTimeoutException -> 0x00ed, ConnectTimeoutException -> 0x0113, MalformedURLException -> 0x0123, IOException -> 0x0146 }
    L_0x003a:
        r0 = r18;
        r4 = r0.zzao;	 Catch:{ SocketTimeoutException -> 0x00ed, ConnectTimeoutException -> 0x0113, MalformedURLException -> 0x0123, IOException -> 0x0146 }
        r0 = r19;
        r15 = r4.zza(r0, r2);	 Catch:{ SocketTimeoutException -> 0x00ed, ConnectTimeoutException -> 0x0113, MalformedURLException -> 0x0123, IOException -> 0x0146 }
        r3 = r15.getStatusLine();	 Catch:{ SocketTimeoutException -> 0x00ed, ConnectTimeoutException -> 0x0113, MalformedURLException -> 0x0123, IOException -> 0x01c5 }
        r4 = r3.getStatusCode();	 Catch:{ SocketTimeoutException -> 0x00ed, ConnectTimeoutException -> 0x0113, MalformedURLException -> 0x0123, IOException -> 0x01c5 }
        r2 = r15.getAllHeaders();	 Catch:{ SocketTimeoutException -> 0x00ed, ConnectTimeoutException -> 0x0113, MalformedURLException -> 0x0123, IOException -> 0x01c5 }
        r6 = zza(r2);	 Catch:{ SocketTimeoutException -> 0x00ed, ConnectTimeoutException -> 0x0113, MalformedURLException -> 0x0123, IOException -> 0x01c5 }
        r2 = 304; // 0x130 float:4.26E-43 double:1.5E-321;
        if (r4 != r2) goto L_0x0087;
    L_0x0058:
        r2 = r19.zze();	 Catch:{ SocketTimeoutException -> 0x00ed, ConnectTimeoutException -> 0x0113, MalformedURLException -> 0x0123, IOException -> 0x01c5 }
        if (r2 != 0) goto L_0x006e;
    L_0x005e:
        r3 = new com.google.android.gms.internal.zzn;	 Catch:{ SocketTimeoutException -> 0x00ed, ConnectTimeoutException -> 0x0113, MalformedURLException -> 0x0123, IOException -> 0x01c5 }
        r4 = 304; // 0x130 float:4.26E-43 double:1.5E-321;
        r5 = 0;
        r7 = 1;
        r8 = android.os.SystemClock.elapsedRealtime();	 Catch:{ SocketTimeoutException -> 0x00ed, ConnectTimeoutException -> 0x0113, MalformedURLException -> 0x0123, IOException -> 0x01c5 }
        r8 = r8 - r16;
        r3.<init>(r4, r5, r6, r7, r8);	 Catch:{ SocketTimeoutException -> 0x00ed, ConnectTimeoutException -> 0x0113, MalformedURLException -> 0x0123, IOException -> 0x01c5 }
    L_0x006d:
        return r3;
    L_0x006e:
        r3 = r2.zzf;	 Catch:{ SocketTimeoutException -> 0x00ed, ConnectTimeoutException -> 0x0113, MalformedURLException -> 0x0123, IOException -> 0x01c5 }
        r3.putAll(r6);	 Catch:{ SocketTimeoutException -> 0x00ed, ConnectTimeoutException -> 0x0113, MalformedURLException -> 0x0123, IOException -> 0x01c5 }
        r7 = new com.google.android.gms.internal.zzn;	 Catch:{ SocketTimeoutException -> 0x00ed, ConnectTimeoutException -> 0x0113, MalformedURLException -> 0x0123, IOException -> 0x01c5 }
        r8 = 304; // 0x130 float:4.26E-43 double:1.5E-321;
        r9 = r2.data;	 Catch:{ SocketTimeoutException -> 0x00ed, ConnectTimeoutException -> 0x0113, MalformedURLException -> 0x0123, IOException -> 0x01c5 }
        r10 = r2.zzf;	 Catch:{ SocketTimeoutException -> 0x00ed, ConnectTimeoutException -> 0x0113, MalformedURLException -> 0x0123, IOException -> 0x01c5 }
        r11 = 1;
        r2 = android.os.SystemClock.elapsedRealtime();	 Catch:{ SocketTimeoutException -> 0x00ed, ConnectTimeoutException -> 0x0113, MalformedURLException -> 0x0123, IOException -> 0x01c5 }
        r12 = r2 - r16;
        r7.<init>(r8, r9, r10, r11, r12);	 Catch:{ SocketTimeoutException -> 0x00ed, ConnectTimeoutException -> 0x0113, MalformedURLException -> 0x0123, IOException -> 0x01c5 }
        r3 = r7;
        goto L_0x006d;
    L_0x0087:
        r2 = r15.getEntity();	 Catch:{ SocketTimeoutException -> 0x00ed, ConnectTimeoutException -> 0x0113, MalformedURLException -> 0x0123, IOException -> 0x01c5 }
        if (r2 == 0) goto L_0x00fd;
    L_0x008d:
        r2 = r15.getEntity();	 Catch:{ SocketTimeoutException -> 0x00ed, ConnectTimeoutException -> 0x0113, MalformedURLException -> 0x0123, IOException -> 0x01c5 }
        r0 = r18;
        r5 = r0.zza(r2);	 Catch:{ SocketTimeoutException -> 0x00ed, ConnectTimeoutException -> 0x0113, MalformedURLException -> 0x0123, IOException -> 0x01c5 }
    L_0x0097:
        r8 = android.os.SystemClock.elapsedRealtime();	 Catch:{ SocketTimeoutException -> 0x00ed, ConnectTimeoutException -> 0x0113, MalformedURLException -> 0x0123, IOException -> 0x01c9 }
        r8 = r8 - r16;
        r2 = DEBUG;	 Catch:{ SocketTimeoutException -> 0x00ed, ConnectTimeoutException -> 0x0113, MalformedURLException -> 0x0123, IOException -> 0x01c9 }
        if (r2 != 0) goto L_0x00a8;
    L_0x00a1:
        r2 = zzam;	 Catch:{ SocketTimeoutException -> 0x00ed, ConnectTimeoutException -> 0x0113, MalformedURLException -> 0x0123, IOException -> 0x01c9 }
        r10 = (long) r2;	 Catch:{ SocketTimeoutException -> 0x00ed, ConnectTimeoutException -> 0x0113, MalformedURLException -> 0x0123, IOException -> 0x01c9 }
        r2 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1));
        if (r2 <= 0) goto L_0x00df;
    L_0x00a8:
        r7 = "HTTP response for request=<%s> [lifetime=%d], [size=%s], [rc=%d], [retryCount=%s]";
        r2 = 5;
        r10 = new java.lang.Object[r2];	 Catch:{ SocketTimeoutException -> 0x00ed, ConnectTimeoutException -> 0x0113, MalformedURLException -> 0x0123, IOException -> 0x01c9 }
        r2 = 0;
        r10[r2] = r19;	 Catch:{ SocketTimeoutException -> 0x00ed, ConnectTimeoutException -> 0x0113, MalformedURLException -> 0x0123, IOException -> 0x01c9 }
        r2 = 1;
        r8 = java.lang.Long.valueOf(r8);	 Catch:{ SocketTimeoutException -> 0x00ed, ConnectTimeoutException -> 0x0113, MalformedURLException -> 0x0123, IOException -> 0x01c9 }
        r10[r2] = r8;	 Catch:{ SocketTimeoutException -> 0x00ed, ConnectTimeoutException -> 0x0113, MalformedURLException -> 0x0123, IOException -> 0x01c9 }
        r8 = 2;
        if (r5 == 0) goto L_0x0101;
    L_0x00bb:
        r2 = r5.length;	 Catch:{ SocketTimeoutException -> 0x00ed, ConnectTimeoutException -> 0x0113, MalformedURLException -> 0x0123, IOException -> 0x01c9 }
        r2 = java.lang.Integer.valueOf(r2);	 Catch:{ SocketTimeoutException -> 0x00ed, ConnectTimeoutException -> 0x0113, MalformedURLException -> 0x0123, IOException -> 0x01c9 }
    L_0x00c0:
        r10[r8] = r2;	 Catch:{ SocketTimeoutException -> 0x00ed, ConnectTimeoutException -> 0x0113, MalformedURLException -> 0x0123, IOException -> 0x01c9 }
        r2 = 3;
        r3 = r3.getStatusCode();	 Catch:{ SocketTimeoutException -> 0x00ed, ConnectTimeoutException -> 0x0113, MalformedURLException -> 0x0123, IOException -> 0x01c9 }
        r3 = java.lang.Integer.valueOf(r3);	 Catch:{ SocketTimeoutException -> 0x00ed, ConnectTimeoutException -> 0x0113, MalformedURLException -> 0x0123, IOException -> 0x01c9 }
        r10[r2] = r3;	 Catch:{ SocketTimeoutException -> 0x00ed, ConnectTimeoutException -> 0x0113, MalformedURLException -> 0x0123, IOException -> 0x01c9 }
        r2 = 4;
        r3 = r19.zzj();	 Catch:{ SocketTimeoutException -> 0x00ed, ConnectTimeoutException -> 0x0113, MalformedURLException -> 0x0123, IOException -> 0x01c9 }
        r3 = r3.zzb();	 Catch:{ SocketTimeoutException -> 0x00ed, ConnectTimeoutException -> 0x0113, MalformedURLException -> 0x0123, IOException -> 0x01c9 }
        r3 = java.lang.Integer.valueOf(r3);	 Catch:{ SocketTimeoutException -> 0x00ed, ConnectTimeoutException -> 0x0113, MalformedURLException -> 0x0123, IOException -> 0x01c9 }
        r10[r2] = r3;	 Catch:{ SocketTimeoutException -> 0x00ed, ConnectTimeoutException -> 0x0113, MalformedURLException -> 0x0123, IOException -> 0x01c9 }
        com.google.android.gms.internal.zzab.zzb(r7, r10);	 Catch:{ SocketTimeoutException -> 0x00ed, ConnectTimeoutException -> 0x0113, MalformedURLException -> 0x0123, IOException -> 0x01c9 }
    L_0x00df:
        r2 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        if (r4 < r2) goto L_0x00e7;
    L_0x00e3:
        r2 = 299; // 0x12b float:4.19E-43 double:1.477E-321;
        if (r4 <= r2) goto L_0x0105;
    L_0x00e7:
        r2 = new java.io.IOException;	 Catch:{ SocketTimeoutException -> 0x00ed, ConnectTimeoutException -> 0x0113, MalformedURLException -> 0x0123, IOException -> 0x01c9 }
        r2.<init>();	 Catch:{ SocketTimeoutException -> 0x00ed, ConnectTimeoutException -> 0x0113, MalformedURLException -> 0x0123, IOException -> 0x01c9 }
        throw r2;	 Catch:{ SocketTimeoutException -> 0x00ed, ConnectTimeoutException -> 0x0113, MalformedURLException -> 0x0123, IOException -> 0x01c9 }
    L_0x00ed:
        r2 = move-exception;
        r2 = "socket";
        r3 = new com.google.android.gms.internal.zzz;
        r3.<init>();
        r0 = r19;
        zza(r2, r0, r3);
        goto L_0x0004;
    L_0x00fd:
        r2 = 0;
        r5 = new byte[r2];	 Catch:{ SocketTimeoutException -> 0x00ed, ConnectTimeoutException -> 0x0113, MalformedURLException -> 0x0123, IOException -> 0x01c5 }
        goto L_0x0097;
    L_0x0101:
        r2 = "null";
        goto L_0x00c0;
    L_0x0105:
        r3 = new com.google.android.gms.internal.zzn;	 Catch:{ SocketTimeoutException -> 0x00ed, ConnectTimeoutException -> 0x0113, MalformedURLException -> 0x0123, IOException -> 0x01c9 }
        r7 = 0;
        r8 = android.os.SystemClock.elapsedRealtime();	 Catch:{ SocketTimeoutException -> 0x00ed, ConnectTimeoutException -> 0x0113, MalformedURLException -> 0x0123, IOException -> 0x01c9 }
        r8 = r8 - r16;
        r3.<init>(r4, r5, r6, r7, r8);	 Catch:{ SocketTimeoutException -> 0x00ed, ConnectTimeoutException -> 0x0113, MalformedURLException -> 0x0123, IOException -> 0x01c9 }
        goto L_0x006d;
    L_0x0113:
        r2 = move-exception;
        r2 = "connection";
        r3 = new com.google.android.gms.internal.zzz;
        r3.<init>();
        r0 = r19;
        zza(r2, r0, r3);
        goto L_0x0004;
    L_0x0123:
        r2 = move-exception;
        r3 = r2;
        r4 = new java.lang.RuntimeException;
        r5 = "Bad URL ";
        r2 = r19.getUrl();
        r2 = java.lang.String.valueOf(r2);
        r6 = r2.length();
        if (r6 == 0) goto L_0x0140;
    L_0x0138:
        r2 = r5.concat(r2);
    L_0x013c:
        r4.<init>(r2, r3);
        throw r4;
    L_0x0140:
        r2 = new java.lang.String;
        r2.<init>(r5);
        goto L_0x013c;
    L_0x0146:
        r2 = move-exception;
        r5 = r14;
    L_0x0148:
        if (r3 == 0) goto L_0x018e;
    L_0x014a:
        r2 = r3.getStatusLine();
        r4 = r2.getStatusCode();
        r2 = "Unexpected response code %d for %s";
        r3 = 2;
        r3 = new java.lang.Object[r3];
        r7 = 0;
        r8 = java.lang.Integer.valueOf(r4);
        r3[r7] = r8;
        r7 = 1;
        r8 = r19.getUrl();
        r3[r7] = r8;
        com.google.android.gms.internal.zzab.zzc(r2, r3);
        if (r5 == 0) goto L_0x01b6;
    L_0x016b:
        r3 = new com.google.android.gms.internal.zzn;
        r7 = 0;
        r8 = android.os.SystemClock.elapsedRealtime();
        r8 = r8 - r16;
        r3.<init>(r4, r5, r6, r7, r8);
        r2 = 401; // 0x191 float:5.62E-43 double:1.98E-321;
        if (r4 == r2) goto L_0x017f;
    L_0x017b:
        r2 = 403; // 0x193 float:5.65E-43 double:1.99E-321;
        if (r4 != r2) goto L_0x0194;
    L_0x017f:
        r2 = "auth";
        r4 = new com.google.android.gms.internal.zza;
        r4.<init>(r3);
        r0 = r19;
        zza(r2, r0, r4);
        goto L_0x0004;
    L_0x018e:
        r3 = new com.google.android.gms.internal.zzo;
        r3.<init>(r2);
        throw r3;
    L_0x0194:
        r2 = 400; // 0x190 float:5.6E-43 double:1.976E-321;
        if (r4 < r2) goto L_0x01a2;
    L_0x0198:
        r2 = 499; // 0x1f3 float:6.99E-43 double:2.465E-321;
        if (r4 > r2) goto L_0x01a2;
    L_0x019c:
        r2 = new com.google.android.gms.internal.zzf;
        r2.<init>(r3);
        throw r2;
    L_0x01a2:
        r2 = 500; // 0x1f4 float:7.0E-43 double:2.47E-321;
        if (r4 < r2) goto L_0x01b0;
    L_0x01a6:
        r2 = 599; // 0x257 float:8.4E-43 double:2.96E-321;
        if (r4 > r2) goto L_0x01b0;
    L_0x01aa:
        r2 = new com.google.android.gms.internal.zzy;
        r2.<init>(r3);
        throw r2;
    L_0x01b0:
        r2 = new com.google.android.gms.internal.zzy;
        r2.<init>(r3);
        throw r2;
    L_0x01b6:
        r2 = "network";
        r3 = new com.google.android.gms.internal.zzm;
        r3.<init>();
        r0 = r19;
        zza(r2, r0, r3);
        goto L_0x0004;
    L_0x01c5:
        r2 = move-exception;
        r5 = r14;
        r3 = r15;
        goto L_0x0148;
    L_0x01c9:
        r2 = move-exception;
        r3 = r15;
        goto L_0x0148;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzad.zza(com.google.android.gms.internal.zzp):com.google.android.gms.internal.zzn");
    }
}
