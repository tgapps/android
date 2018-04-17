package com.google.android.gms.dynamite;

import com.google.android.gms.dynamite.DynamiteModule.VersionPolicy;

final class zzf implements VersionPolicy {
    zzf() {
    }

    public final com.google.android.gms.dynamite.DynamiteModule.VersionPolicy.SelectionResult selectModule(android.content.Context r3, java.lang.String r4, com.google.android.gms.dynamite.DynamiteModule.VersionPolicy.IVersions r5) throws com.google.android.gms.dynamite.DynamiteModule.LoadingException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:12:0x0029 in {4, 6, 10, 11} preds:[]
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:129)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:38)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
        /*
        r2 = this;
        r0 = new com.google.android.gms.dynamite.DynamiteModule$VersionPolicy$SelectionResult;
        r0.<init>();
        r1 = r5.getLocalVersion(r3, r4);
        r0.localVersion = r1;
        r1 = 1;
        r3 = r5.getRemoteVersion(r3, r4, r1);
        r0.remoteVersion = r3;
        r3 = r0.localVersion;
        if (r3 != 0) goto L_0x001e;
    L_0x0016:
        r3 = r0.remoteVersion;
        if (r3 != 0) goto L_0x001e;
    L_0x001a:
        r3 = 0;
    L_0x001b:
        r0.selection = r3;
        return r0;
    L_0x001e:
        r3 = r0.remoteVersion;
        r4 = r0.localVersion;
        if (r3 < r4) goto L_0x0027;
    L_0x0024:
        r0.selection = r1;
        return r0;
    L_0x0027:
        r3 = -1;
        goto L_0x001b;
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.dynamite.zzf.selectModule(android.content.Context, java.lang.String, com.google.android.gms.dynamite.DynamiteModule$VersionPolicy$IVersions):com.google.android.gms.dynamite.DynamiteModule$VersionPolicy$SelectionResult");
    }
}
