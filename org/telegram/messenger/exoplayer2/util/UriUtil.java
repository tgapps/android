package org.telegram.messenger.exoplayer2.util;

import android.net.Uri;
import android.text.TextUtils;

public final class UriUtil {
    private static final int FRAGMENT = 3;
    private static final int INDEX_COUNT = 4;
    private static final int PATH = 1;
    private static final int QUERY = 2;
    private static final int SCHEME_COLON = 0;

    private static java.lang.String removeDotSegments(java.lang.StringBuilder r1, int r2, int r3) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.util.UriUtil.removeDotSegments(java.lang.StringBuilder, int, int):java.lang.String
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
        if (r8 < r9) goto L_0x0007;
    L_0x0002:
        r0 = r7.toString();
        return r0;
    L_0x0007:
        r0 = r7.charAt(r8);
        r1 = 47;
        if (r0 != r1) goto L_0x0011;
    L_0x000f:
        r8 = r8 + 1;
    L_0x0011:
        r0 = r8;
        r2 = r0;
        r0 = r9;
        r9 = r8;
        if (r9 > r0) goto L_0x006a;
    L_0x0017:
        if (r9 != r0) goto L_0x001b;
    L_0x0019:
        r3 = r9;
        goto L_0x0024;
    L_0x001b:
        r3 = r7.charAt(r9);
        if (r3 != r1) goto L_0x0067;
        r3 = r9 + 1;
        goto L_0x001a;
        r4 = r2 + 1;
        r5 = 46;
        if (r9 != r4) goto L_0x0039;
        r4 = r7.charAt(r2);
        if (r4 != r5) goto L_0x0039;
        r7.delete(r2, r3);
        r4 = r3 - r2;
        r0 = r0 - r4;
        r9 = r2;
        goto L_0x0066;
        r4 = r2 + 2;
        if (r9 != r4) goto L_0x0063;
        r4 = r7.charAt(r2);
        if (r4 != r5) goto L_0x0063;
        r4 = r2 + 1;
        r4 = r7.charAt(r4);
        if (r4 != r5) goto L_0x0063;
        r4 = "/";
        r5 = r2 + -2;
        r4 = r7.lastIndexOf(r4, r5);
        r4 = r4 + 1;
        if (r4 <= r8) goto L_0x0059;
        r5 = r4;
        goto L_0x005a;
        r5 = r8;
        r7.delete(r5, r3);
        r6 = r3 - r5;
        r0 = r0 - r6;
        r2 = r4;
        r9 = r4;
        goto L_0x0066;
        r9 = r9 + 1;
        r2 = r9;
        goto L_0x0015;
        r9 = r9 + 1;
        goto L_0x0015;
    L_0x006a:
        r1 = r7.toString();
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.util.UriUtil.removeDotSegments(java.lang.StringBuilder, int, int):java.lang.String");
    }

    private UriUtil() {
    }

    public static Uri resolveToUri(String baseUri, String referenceUri) {
        return Uri.parse(resolve(baseUri, referenceUri));
    }

    public static String resolve(String baseUri, String referenceUri) {
        StringBuilder uri = new StringBuilder();
        baseUri = baseUri == null ? TtmlNode.ANONYMOUS_REGION_ID : baseUri;
        referenceUri = referenceUri == null ? TtmlNode.ANONYMOUS_REGION_ID : referenceUri;
        int[] refIndices = getUriIndices(referenceUri);
        if (refIndices[0] != -1) {
            uri.append(referenceUri);
            removeDotSegments(uri, refIndices[1], refIndices[2]);
            return uri.toString();
        }
        int[] baseIndices = getUriIndices(baseUri);
        if (refIndices[3] == 0) {
            uri.append(baseUri, 0, baseIndices[3]);
            uri.append(referenceUri);
            return uri.toString();
        } else if (refIndices[2] == 0) {
            uri.append(baseUri, 0, baseIndices[2]);
            uri.append(referenceUri);
            return uri.toString();
        } else if (refIndices[1] != 0) {
            baseLimit = baseIndices[0] + 1;
            uri.append(baseUri, 0, baseLimit);
            uri.append(referenceUri);
            return removeDotSegments(uri, refIndices[1] + baseLimit, refIndices[2] + baseLimit);
        } else if (referenceUri.charAt(refIndices[1]) == '/') {
            uri.append(baseUri, 0, baseIndices[1]);
            uri.append(referenceUri);
            return removeDotSegments(uri, baseIndices[1], baseIndices[1] + refIndices[2]);
        } else if (baseIndices[0] + 2 >= baseIndices[1] || baseIndices[1] != baseIndices[2]) {
            int lastSlashIndex = baseUri.lastIndexOf(47, baseIndices[2] - 1);
            baseLimit = lastSlashIndex == -1 ? baseIndices[1] : lastSlashIndex + 1;
            uri.append(baseUri, 0, baseLimit);
            uri.append(referenceUri);
            return removeDotSegments(uri, baseIndices[1], refIndices[2] + baseLimit);
        } else {
            uri.append(baseUri, 0, baseIndices[1]);
            uri.append('/');
            uri.append(referenceUri);
            return removeDotSegments(uri, baseIndices[1], (baseIndices[1] + refIndices[2]) + 1);
        }
    }

    private static int[] getUriIndices(String uriString) {
        int[] indices = new int[4];
        if (TextUtils.isEmpty(uriString)) {
            indices[0] = -1;
            return indices;
        }
        int pathIndex;
        int length = uriString.length();
        int fragmentIndex = uriString.indexOf(35);
        if (fragmentIndex == -1) {
            fragmentIndex = length;
        }
        int queryIndex = uriString.indexOf(63);
        if (queryIndex == -1 || queryIndex > fragmentIndex) {
            queryIndex = fragmentIndex;
        }
        int schemeIndexLimit = uriString.indexOf(47);
        if (schemeIndexLimit == -1 || schemeIndexLimit > queryIndex) {
            schemeIndexLimit = queryIndex;
        }
        int schemeIndex = uriString.indexOf(58);
        if (schemeIndex > schemeIndexLimit) {
            schemeIndex = -1;
        }
        boolean hasAuthority = schemeIndex + 2 < queryIndex && uriString.charAt(schemeIndex + 1) == '/' && uriString.charAt(schemeIndex + 2) == '/';
        if (hasAuthority) {
            pathIndex = uriString.indexOf(47, schemeIndex + 3);
            if (pathIndex == -1 || pathIndex > queryIndex) {
                pathIndex = queryIndex;
            }
        } else {
            pathIndex = schemeIndex + 1;
        }
        int pathIndex2 = pathIndex;
        indices[0] = schemeIndex;
        indices[1] = pathIndex2;
        indices[2] = queryIndex;
        indices[3] = fragmentIndex;
        return indices;
    }
}
