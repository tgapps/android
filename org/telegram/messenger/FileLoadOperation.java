package org.telegram.messenger;

import android.util.SparseArray;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.FileLocation;
import org.telegram.tgnet.TLRPC.InputFileLocation;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.tgnet.TLRPC.TL_fileEncryptedLocation;
import org.telegram.tgnet.TLRPC.TL_fileHash;
import org.telegram.tgnet.TLRPC.TL_fileLocation;
import org.telegram.tgnet.TLRPC.TL_inputEncryptedFileLocation;
import org.telegram.tgnet.TLRPC.TL_inputFileLocation;
import org.telegram.tgnet.TLRPC.TL_inputWebFileLocation;
import org.telegram.tgnet.TLRPC.TL_upload_cdnFile;
import org.telegram.tgnet.TLRPC.TL_upload_cdnFileReuploadNeeded;
import org.telegram.tgnet.TLRPC.TL_upload_file;
import org.telegram.tgnet.TLRPC.TL_upload_fileCdnRedirect;
import org.telegram.tgnet.TLRPC.TL_upload_getCdnFile;
import org.telegram.tgnet.TLRPC.TL_upload_getCdnFileHashes;
import org.telegram.tgnet.TLRPC.TL_upload_getFile;
import org.telegram.tgnet.TLRPC.TL_upload_getWebFile;
import org.telegram.tgnet.TLRPC.TL_upload_reuploadCdnFile;
import org.telegram.tgnet.TLRPC.TL_upload_webFile;
import org.telegram.tgnet.TLRPC.TL_webDocument;
import org.telegram.tgnet.TLRPC.Vector;

public class FileLoadOperation {
    private static final int bigFileSizeFrom = 1048576;
    private static final int cdnChunkCheckSize = 131072;
    private static final int downloadChunkSize = 32768;
    private static final int downloadChunkSizeBig = 131072;
    private static final int maxCdnParts = 12288;
    private static final int maxDownloadRequests = 4;
    private static final int maxDownloadRequestsBig = 4;
    private static final int stateDownloading = 1;
    private static final int stateFailed = 2;
    private static final int stateFinished = 3;
    private static final int stateIdle = 0;
    private boolean allowDisordererFileSave;
    private int bytesCountPadding;
    private File cacheFileFinal;
    private File cacheFileParts;
    private File cacheFileTemp;
    private File cacheIvTemp;
    private byte[] cdnCheckBytes;
    private int cdnDatacenterId;
    private SparseArray<TL_fileHash> cdnHashes;
    private byte[] cdnIv;
    private byte[] cdnKey;
    private byte[] cdnToken;
    private int currentAccount;
    private int currentDownloadChunkSize;
    private int currentMaxDownloadRequests;
    private int currentType;
    private int datacenter_id;
    private ArrayList<RequestInfo> delayedRequestInfos;
    private FileLoadOperationDelegate delegate;
    private volatile int downloadedBytes;
    private boolean encryptFile;
    private byte[] encryptIv;
    private byte[] encryptKey;
    private String ext;
    private RandomAccessFile fileOutputStream;
    private RandomAccessFile filePartsStream;
    private RandomAccessFile fileReadStream;
    private RandomAccessFile fiv;
    private boolean isCdn;
    private boolean isForceRequest;
    private byte[] iv;
    private byte[] key;
    private InputFileLocation location;
    private ArrayList<Range> notCheckedCdnRanges;
    private ArrayList<Range> notLoadedBytesRanges;
    private volatile ArrayList<Range> notLoadedBytesRangesCopy;
    private ArrayList<Range> notRequestedBytesRanges;
    private volatile boolean paused;
    private int renameRetryCount;
    private ArrayList<RequestInfo> requestInfos;
    private int requestedBytesCount;
    private boolean requestingCdnOffsets;
    private int requestsCount;
    private boolean reuploadingCdn;
    private boolean started;
    private volatile int state;
    private File storePath;
    private ArrayList<FileStreamLoadOperation> streamListeners;
    private int streamStartOffset;
    private File tempPath;
    private int totalBytesCount;
    private TL_inputWebFileLocation webLocation;

    public interface FileLoadOperationDelegate {
        void didChangedLoadProgress(FileLoadOperation fileLoadOperation, float f);

        void didFailedLoadingFile(FileLoadOperation fileLoadOperation, int i);

        void didFinishLoadingFile(FileLoadOperation fileLoadOperation, File file);
    }

    public static class Range {
        private int end;
        private int start;

        private Range(int s, int e) {
            this.start = s;
            this.end = e;
        }
    }

    private static class RequestInfo {
        private int offset;
        private int requestToken;
        private TL_upload_file response;
        private TL_upload_cdnFile responseCdn;
        private TL_upload_webFile responseWeb;

        private RequestInfo() {
        }
    }

    public FileLoadOperation(org.telegram.tgnet.TLRPC.Document r1) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.FileLoadOperation.<init>(org.telegram.tgnet.TLRPC$Document):void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
        /*
        r0 = this;
        r6.<init>();
        r0 = 0;
        r6.state = r0;
        r1 = 1;
        r2 = r7 instanceof org.telegram.tgnet.TLRPC.TL_documentEncrypted;	 Catch:{ Exception -> 0x0101 }
        if (r2 == 0) goto L_0x0037;	 Catch:{ Exception -> 0x0101 }
    L_0x000b:
        r2 = new org.telegram.tgnet.TLRPC$TL_inputEncryptedFileLocation;	 Catch:{ Exception -> 0x0101 }
        r2.<init>();	 Catch:{ Exception -> 0x0101 }
        r6.location = r2;	 Catch:{ Exception -> 0x0101 }
        r2 = r6.location;	 Catch:{ Exception -> 0x0101 }
        r3 = r7.id;	 Catch:{ Exception -> 0x0101 }
        r2.id = r3;	 Catch:{ Exception -> 0x0101 }
        r2 = r6.location;	 Catch:{ Exception -> 0x0101 }
        r3 = r7.access_hash;	 Catch:{ Exception -> 0x0101 }
        r2.access_hash = r3;	 Catch:{ Exception -> 0x0101 }
        r2 = r7.dc_id;	 Catch:{ Exception -> 0x0101 }
        r6.datacenter_id = r2;	 Catch:{ Exception -> 0x0101 }
        r2 = 32;	 Catch:{ Exception -> 0x0101 }
        r2 = new byte[r2];	 Catch:{ Exception -> 0x0101 }
        r6.iv = r2;	 Catch:{ Exception -> 0x0101 }
        r2 = r7.iv;	 Catch:{ Exception -> 0x0101 }
        r3 = r6.iv;	 Catch:{ Exception -> 0x0101 }
        r4 = r6.iv;	 Catch:{ Exception -> 0x0101 }
        r4 = r4.length;	 Catch:{ Exception -> 0x0101 }
        java.lang.System.arraycopy(r2, r0, r3, r0, r4);	 Catch:{ Exception -> 0x0101 }
        r2 = r7.key;	 Catch:{ Exception -> 0x0101 }
        r6.key = r2;	 Catch:{ Exception -> 0x0101 }
        goto L_0x0054;	 Catch:{ Exception -> 0x0101 }
    L_0x0037:
        r2 = r7 instanceof org.telegram.tgnet.TLRPC.TL_document;	 Catch:{ Exception -> 0x0101 }
        if (r2 == 0) goto L_0x0054;	 Catch:{ Exception -> 0x0101 }
    L_0x003b:
        r2 = new org.telegram.tgnet.TLRPC$TL_inputDocumentFileLocation;	 Catch:{ Exception -> 0x0101 }
        r2.<init>();	 Catch:{ Exception -> 0x0101 }
        r6.location = r2;	 Catch:{ Exception -> 0x0101 }
        r2 = r6.location;	 Catch:{ Exception -> 0x0101 }
        r3 = r7.id;	 Catch:{ Exception -> 0x0101 }
        r2.id = r3;	 Catch:{ Exception -> 0x0101 }
        r2 = r6.location;	 Catch:{ Exception -> 0x0101 }
        r3 = r7.access_hash;	 Catch:{ Exception -> 0x0101 }
        r2.access_hash = r3;	 Catch:{ Exception -> 0x0101 }
        r2 = r7.dc_id;	 Catch:{ Exception -> 0x0101 }
        r6.datacenter_id = r2;	 Catch:{ Exception -> 0x0101 }
        r6.allowDisordererFileSave = r1;	 Catch:{ Exception -> 0x0101 }
    L_0x0054:
        r2 = r7.size;	 Catch:{ Exception -> 0x0101 }
        r6.totalBytesCount = r2;	 Catch:{ Exception -> 0x0101 }
        r2 = r6.key;	 Catch:{ Exception -> 0x0101 }
        if (r2 == 0) goto L_0x0072;	 Catch:{ Exception -> 0x0101 }
    L_0x005c:
        r2 = 0;	 Catch:{ Exception -> 0x0101 }
        r3 = r6.totalBytesCount;	 Catch:{ Exception -> 0x0101 }
        r3 = r3 % 16;	 Catch:{ Exception -> 0x0101 }
        if (r3 == 0) goto L_0x0072;	 Catch:{ Exception -> 0x0101 }
    L_0x0063:
        r3 = r6.totalBytesCount;	 Catch:{ Exception -> 0x0101 }
        r3 = r3 % 16;	 Catch:{ Exception -> 0x0101 }
        r3 = 16 - r3;	 Catch:{ Exception -> 0x0101 }
        r6.bytesCountPadding = r3;	 Catch:{ Exception -> 0x0101 }
        r3 = r6.totalBytesCount;	 Catch:{ Exception -> 0x0101 }
        r4 = r6.bytesCountPadding;	 Catch:{ Exception -> 0x0101 }
        r3 = r3 + r4;	 Catch:{ Exception -> 0x0101 }
        r6.totalBytesCount = r3;	 Catch:{ Exception -> 0x0101 }
    L_0x0072:
        r2 = org.telegram.messenger.FileLoader.getDocumentFileName(r7);	 Catch:{ Exception -> 0x0101 }
        r6.ext = r2;	 Catch:{ Exception -> 0x0101 }
        r2 = r6.ext;	 Catch:{ Exception -> 0x0101 }
        r3 = -1;	 Catch:{ Exception -> 0x0101 }
        if (r2 == 0) goto L_0x0093;	 Catch:{ Exception -> 0x0101 }
    L_0x007d:
        r2 = r6.ext;	 Catch:{ Exception -> 0x0101 }
        r4 = 46;	 Catch:{ Exception -> 0x0101 }
        r2 = r2.lastIndexOf(r4);	 Catch:{ Exception -> 0x0101 }
        r4 = r2;	 Catch:{ Exception -> 0x0101 }
        if (r2 != r3) goto L_0x0089;	 Catch:{ Exception -> 0x0101 }
    L_0x0088:
        goto L_0x0093;	 Catch:{ Exception -> 0x0101 }
        r2 = r6.ext;	 Catch:{ Exception -> 0x0101 }
        r2 = r2.substring(r4);	 Catch:{ Exception -> 0x0101 }
        r6.ext = r2;	 Catch:{ Exception -> 0x0101 }
        goto L_0x0097;	 Catch:{ Exception -> 0x0101 }
    L_0x0093:
        r2 = "";	 Catch:{ Exception -> 0x0101 }
        r6.ext = r2;	 Catch:{ Exception -> 0x0101 }
        r2 = "audio/ogg";	 Catch:{ Exception -> 0x0101 }
        r4 = r7.mime_type;	 Catch:{ Exception -> 0x0101 }
        r2 = r2.equals(r4);	 Catch:{ Exception -> 0x0101 }
        if (r2 == 0) goto L_0x00a6;	 Catch:{ Exception -> 0x0101 }
        r2 = 50331648; // 0x3000000 float:3.761582E-37 double:2.4867138E-316;	 Catch:{ Exception -> 0x0101 }
        r6.currentType = r2;	 Catch:{ Exception -> 0x0101 }
        goto L_0x00b9;	 Catch:{ Exception -> 0x0101 }
        r2 = "video/mp4";	 Catch:{ Exception -> 0x0101 }
        r4 = r7.mime_type;	 Catch:{ Exception -> 0x0101 }
        r2 = r2.equals(r4);	 Catch:{ Exception -> 0x0101 }
        if (r2 == 0) goto L_0x00b5;	 Catch:{ Exception -> 0x0101 }
        r2 = 33554432; // 0x2000000 float:9.403955E-38 double:1.6578092E-316;	 Catch:{ Exception -> 0x0101 }
        r6.currentType = r2;	 Catch:{ Exception -> 0x0101 }
        goto L_0x00b9;	 Catch:{ Exception -> 0x0101 }
        r2 = 67108864; // 0x4000000 float:1.5046328E-36 double:3.31561842E-316;	 Catch:{ Exception -> 0x0101 }
        r6.currentType = r2;	 Catch:{ Exception -> 0x0101 }
        r2 = r6.ext;	 Catch:{ Exception -> 0x0101 }
        r2 = r2.length();	 Catch:{ Exception -> 0x0101 }
        if (r2 > r1) goto L_0x0100;	 Catch:{ Exception -> 0x0101 }
        r2 = r7.mime_type;	 Catch:{ Exception -> 0x0101 }
        if (r2 == 0) goto L_0x00fc;	 Catch:{ Exception -> 0x0101 }
        r2 = r7.mime_type;	 Catch:{ Exception -> 0x0101 }
        r4 = r2.hashCode();	 Catch:{ Exception -> 0x0101 }
        r5 = 187091926; // 0xb26cbd6 float:3.2123786E-32 double:9.24356933E-316;	 Catch:{ Exception -> 0x0101 }
        if (r4 == r5) goto L_0x00e0;	 Catch:{ Exception -> 0x0101 }
        r5 = 1331848029; // 0x4f62635d float:3.79816269E9 double:6.580203566E-315;	 Catch:{ Exception -> 0x0101 }
        if (r4 == r5) goto L_0x00d6;	 Catch:{ Exception -> 0x0101 }
        goto L_0x00e9;	 Catch:{ Exception -> 0x0101 }
        r4 = "video/mp4";	 Catch:{ Exception -> 0x0101 }
        r2 = r2.equals(r4);	 Catch:{ Exception -> 0x0101 }
        if (r2 == 0) goto L_0x00e9;	 Catch:{ Exception -> 0x0101 }
        r3 = r0;	 Catch:{ Exception -> 0x0101 }
        goto L_0x00e9;	 Catch:{ Exception -> 0x0101 }
        r4 = "audio/ogg";	 Catch:{ Exception -> 0x0101 }
        r2 = r2.equals(r4);	 Catch:{ Exception -> 0x0101 }
        if (r2 == 0) goto L_0x00e9;	 Catch:{ Exception -> 0x0101 }
        r3 = r1;	 Catch:{ Exception -> 0x0101 }
        switch(r3) {
            case 0: goto L_0x00f4;
            case 1: goto L_0x00ef;
            default: goto L_0x00ec;
        };	 Catch:{ Exception -> 0x0101 }
        r2 = "";	 Catch:{ Exception -> 0x0101 }
        goto L_0x00f9;	 Catch:{ Exception -> 0x0101 }
        r2 = ".ogg";	 Catch:{ Exception -> 0x0101 }
        r6.ext = r2;	 Catch:{ Exception -> 0x0101 }
        goto L_0x00fb;	 Catch:{ Exception -> 0x0101 }
        r2 = ".mp4";	 Catch:{ Exception -> 0x0101 }
        r6.ext = r2;	 Catch:{ Exception -> 0x0101 }
        goto L_0x00fb;	 Catch:{ Exception -> 0x0101 }
        r6.ext = r2;	 Catch:{ Exception -> 0x0101 }
        goto L_0x0100;	 Catch:{ Exception -> 0x0101 }
        r2 = "";	 Catch:{ Exception -> 0x0101 }
        r6.ext = r2;	 Catch:{ Exception -> 0x0101 }
        goto L_0x0108;
    L_0x0101:
        r2 = move-exception;
        org.telegram.messenger.FileLog.e(r2);
        r6.onFail(r1, r0);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.FileLoadOperation.<init>(org.telegram.tgnet.TLRPC$Document):void");
    }

    private boolean processRequestResult(org.telegram.messenger.FileLoadOperation.RequestInfo r1, org.telegram.tgnet.TLRPC.TL_error r2) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.FileLoadOperation.processRequestResult(org.telegram.messenger.FileLoadOperation$RequestInfo, org.telegram.tgnet.TLRPC$TL_error):boolean
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
        /*
        r0 = this;
        r1 = r25;
        r2 = r27;
        r3 = r1.state;
        r4 = 1;
        r5 = 0;
        if (r3 == r4) goto L_0x000b;
    L_0x000a:
        return r5;
    L_0x000b:
        r3 = r1.requestInfos;
        r6 = r26;
        r3.remove(r6);
        if (r2 != 0) goto L_0x0382;
    L_0x0014:
        r7 = r1.notLoadedBytesRanges;	 Catch:{ Exception -> 0x0375 }
        if (r7 != 0) goto L_0x0024;	 Catch:{ Exception -> 0x0375 }
    L_0x0018:
        r7 = r1.downloadedBytes;	 Catch:{ Exception -> 0x0375 }
        r8 = r26.offset;	 Catch:{ Exception -> 0x0375 }
        if (r7 == r8) goto L_0x0024;	 Catch:{ Exception -> 0x0375 }
    L_0x0020:
        r25.delayRequestInfo(r26);	 Catch:{ Exception -> 0x0375 }
        return r5;	 Catch:{ Exception -> 0x0375 }
    L_0x0024:
        r7 = r26.response;	 Catch:{ Exception -> 0x0375 }
        if (r7 == 0) goto L_0x0031;	 Catch:{ Exception -> 0x0375 }
    L_0x002a:
        r7 = r26.response;	 Catch:{ Exception -> 0x0375 }
        r7 = r7.bytes;	 Catch:{ Exception -> 0x0375 }
    L_0x0030:
        goto L_0x004c;	 Catch:{ Exception -> 0x0375 }
    L_0x0031:
        r7 = r26.responseWeb;	 Catch:{ Exception -> 0x0375 }
        if (r7 == 0) goto L_0x003e;	 Catch:{ Exception -> 0x0375 }
    L_0x0037:
        r7 = r26.responseWeb;	 Catch:{ Exception -> 0x0375 }
        r7 = r7.bytes;	 Catch:{ Exception -> 0x0375 }
        goto L_0x0030;	 Catch:{ Exception -> 0x0375 }
    L_0x003e:
        r7 = r26.responseCdn;	 Catch:{ Exception -> 0x0375 }
        if (r7 == 0) goto L_0x004b;	 Catch:{ Exception -> 0x0375 }
    L_0x0044:
        r7 = r26.responseCdn;	 Catch:{ Exception -> 0x0375 }
        r7 = r7.bytes;	 Catch:{ Exception -> 0x0375 }
        goto L_0x0030;	 Catch:{ Exception -> 0x0375 }
    L_0x004b:
        r7 = 0;	 Catch:{ Exception -> 0x0375 }
    L_0x004c:
        if (r7 == 0) goto L_0x036d;	 Catch:{ Exception -> 0x0375 }
    L_0x004e:
        r8 = r7.limit();	 Catch:{ Exception -> 0x0375 }
        if (r8 != 0) goto L_0x0058;	 Catch:{ Exception -> 0x0375 }
    L_0x0054:
        r24 = r7;	 Catch:{ Exception -> 0x0375 }
        goto L_0x036f;	 Catch:{ Exception -> 0x0375 }
    L_0x0058:
        r8 = r7.limit();	 Catch:{ Exception -> 0x0375 }
        r9 = r1.isCdn;	 Catch:{ Exception -> 0x0375 }
        r10 = 131072; // 0x20000 float:1.83671E-40 double:6.47582E-319;	 Catch:{ Exception -> 0x0375 }
        if (r9 == 0) goto L_0x0080;	 Catch:{ Exception -> 0x0375 }
    L_0x0062:
        r9 = r26.offset;	 Catch:{ Exception -> 0x0375 }
        r9 = r9 / r10;	 Catch:{ Exception -> 0x0375 }
        r11 = r9 * r10;	 Catch:{ Exception -> 0x0375 }
        r12 = r1.cdnHashes;	 Catch:{ Exception -> 0x0375 }
        if (r12 == 0) goto L_0x0076;	 Catch:{ Exception -> 0x0375 }
    L_0x006d:
        r12 = r1.cdnHashes;	 Catch:{ Exception -> 0x0375 }
        r12 = r12.get(r11);	 Catch:{ Exception -> 0x0375 }
        r12 = (org.telegram.tgnet.TLRPC.TL_fileHash) r12;	 Catch:{ Exception -> 0x0375 }
        goto L_0x0077;	 Catch:{ Exception -> 0x0375 }
    L_0x0076:
        r12 = 0;	 Catch:{ Exception -> 0x0375 }
    L_0x0077:
        if (r12 != 0) goto L_0x0080;	 Catch:{ Exception -> 0x0375 }
    L_0x0079:
        r25.delayRequestInfo(r26);	 Catch:{ Exception -> 0x0375 }
        r1.requestFileOffsets(r11);	 Catch:{ Exception -> 0x0375 }
        return r4;	 Catch:{ Exception -> 0x0375 }
    L_0x0080:
        r9 = r26.responseCdn;	 Catch:{ Exception -> 0x0375 }
        r11 = 12;	 Catch:{ Exception -> 0x0375 }
        r12 = 13;	 Catch:{ Exception -> 0x0375 }
        r13 = 14;	 Catch:{ Exception -> 0x0375 }
        r14 = 15;	 Catch:{ Exception -> 0x0375 }
        if (r9 == 0) goto L_0x00c3;	 Catch:{ Exception -> 0x0375 }
    L_0x008e:
        r9 = r26.offset;	 Catch:{ Exception -> 0x0375 }
        r9 = r9 / 16;	 Catch:{ Exception -> 0x0375 }
        r15 = r1.cdnIv;	 Catch:{ Exception -> 0x0375 }
        r3 = r9 & 255;	 Catch:{ Exception -> 0x0375 }
        r3 = (byte) r3;	 Catch:{ Exception -> 0x0375 }
        r15[r14] = r3;	 Catch:{ Exception -> 0x0375 }
        r3 = r1.cdnIv;	 Catch:{ Exception -> 0x0375 }
        r15 = r9 >> 8;	 Catch:{ Exception -> 0x0375 }
        r15 = r15 & 255;	 Catch:{ Exception -> 0x0375 }
        r15 = (byte) r15;	 Catch:{ Exception -> 0x0375 }
        r3[r13] = r15;	 Catch:{ Exception -> 0x0375 }
        r3 = r1.cdnIv;	 Catch:{ Exception -> 0x0375 }
        r15 = r9 >> 16;	 Catch:{ Exception -> 0x0375 }
        r15 = r15 & 255;	 Catch:{ Exception -> 0x0375 }
        r15 = (byte) r15;	 Catch:{ Exception -> 0x0375 }
        r3[r12] = r15;	 Catch:{ Exception -> 0x0375 }
        r3 = r1.cdnIv;	 Catch:{ Exception -> 0x0375 }
        r15 = r9 >> 24;	 Catch:{ Exception -> 0x0375 }
        r15 = r15 & 255;	 Catch:{ Exception -> 0x0375 }
        r15 = (byte) r15;	 Catch:{ Exception -> 0x0375 }
        r3[r11] = r15;	 Catch:{ Exception -> 0x0375 }
        r3 = r7.buffer;	 Catch:{ Exception -> 0x0375 }
        r15 = r1.cdnKey;	 Catch:{ Exception -> 0x0375 }
        r10 = r1.cdnIv;	 Catch:{ Exception -> 0x0375 }
        r4 = r7.limit();	 Catch:{ Exception -> 0x0375 }
        org.telegram.messenger.Utilities.aesCtrDecryption(r3, r15, r10, r5, r4);	 Catch:{ Exception -> 0x0375 }
    L_0x00c3:
        r3 = r1.downloadedBytes;	 Catch:{ Exception -> 0x0375 }
        r3 = r3 + r8;	 Catch:{ Exception -> 0x0375 }
        r1.downloadedBytes = r3;	 Catch:{ Exception -> 0x0375 }
        r3 = r1.totalBytesCount;	 Catch:{ Exception -> 0x0375 }
        if (r3 <= 0) goto L_0x00d6;	 Catch:{ Exception -> 0x0375 }
    L_0x00cc:
        r3 = r1.downloadedBytes;	 Catch:{ Exception -> 0x0375 }
        r4 = r1.totalBytesCount;	 Catch:{ Exception -> 0x0375 }
        if (r3 < r4) goto L_0x00d4;	 Catch:{ Exception -> 0x0375 }
    L_0x00d2:
        r3 = 1;	 Catch:{ Exception -> 0x0375 }
        goto L_0x00d5;	 Catch:{ Exception -> 0x0375 }
    L_0x00d4:
        r3 = r5;	 Catch:{ Exception -> 0x0375 }
    L_0x00d5:
        goto L_0x00f5;	 Catch:{ Exception -> 0x0375 }
    L_0x00d6:
        r3 = r1.currentDownloadChunkSize;	 Catch:{ Exception -> 0x0375 }
        if (r8 != r3) goto L_0x00f4;	 Catch:{ Exception -> 0x0375 }
    L_0x00da:
        r3 = r1.totalBytesCount;	 Catch:{ Exception -> 0x0375 }
        r4 = r1.downloadedBytes;	 Catch:{ Exception -> 0x0375 }
        if (r3 == r4) goto L_0x00e7;	 Catch:{ Exception -> 0x0375 }
    L_0x00e0:
        r3 = r1.downloadedBytes;	 Catch:{ Exception -> 0x0375 }
        r4 = r1.currentDownloadChunkSize;	 Catch:{ Exception -> 0x0375 }
        r3 = r3 % r4;	 Catch:{ Exception -> 0x0375 }
        if (r3 == 0) goto L_0x00f2;	 Catch:{ Exception -> 0x0375 }
    L_0x00e7:
        r3 = r1.totalBytesCount;	 Catch:{ Exception -> 0x0375 }
        if (r3 <= 0) goto L_0x00f4;	 Catch:{ Exception -> 0x0375 }
    L_0x00eb:
        r3 = r1.totalBytesCount;	 Catch:{ Exception -> 0x0375 }
        r4 = r1.downloadedBytes;	 Catch:{ Exception -> 0x0375 }
        if (r3 > r4) goto L_0x00f2;	 Catch:{ Exception -> 0x0375 }
    L_0x00f1:
        goto L_0x00f4;	 Catch:{ Exception -> 0x0375 }
    L_0x00f2:
        r3 = r5;	 Catch:{ Exception -> 0x0375 }
        goto L_0x00f5;	 Catch:{ Exception -> 0x0375 }
    L_0x00f4:
        r3 = 1;	 Catch:{ Exception -> 0x0375 }
    L_0x00f5:
        r4 = r1.key;	 Catch:{ Exception -> 0x0375 }
        if (r4 == 0) goto L_0x0122;	 Catch:{ Exception -> 0x0375 }
    L_0x00f9:
        r4 = r7.buffer;	 Catch:{ Exception -> 0x0375 }
        r9 = r1.key;	 Catch:{ Exception -> 0x0375 }
        r10 = r1.iv;	 Catch:{ Exception -> 0x0375 }
        r19 = 0;	 Catch:{ Exception -> 0x0375 }
        r20 = 1;	 Catch:{ Exception -> 0x0375 }
        r21 = 0;	 Catch:{ Exception -> 0x0375 }
        r22 = r7.limit();	 Catch:{ Exception -> 0x0375 }
        r16 = r4;	 Catch:{ Exception -> 0x0375 }
        r17 = r9;	 Catch:{ Exception -> 0x0375 }
        r18 = r10;	 Catch:{ Exception -> 0x0375 }
        org.telegram.messenger.Utilities.aesIgeEncryption(r16, r17, r18, r19, r20, r21, r22);	 Catch:{ Exception -> 0x0375 }
        if (r3 == 0) goto L_0x0122;	 Catch:{ Exception -> 0x0375 }
    L_0x0114:
        r4 = r1.bytesCountPadding;	 Catch:{ Exception -> 0x0375 }
        if (r4 == 0) goto L_0x0122;	 Catch:{ Exception -> 0x0375 }
    L_0x0118:
        r4 = r7.limit();	 Catch:{ Exception -> 0x0375 }
        r9 = r1.bytesCountPadding;	 Catch:{ Exception -> 0x0375 }
        r4 = r4 - r9;	 Catch:{ Exception -> 0x0375 }
        r7.limit(r4);	 Catch:{ Exception -> 0x0375 }
    L_0x0122:
        r4 = r1.encryptFile;	 Catch:{ Exception -> 0x0375 }
        if (r4 == 0) goto L_0x015b;	 Catch:{ Exception -> 0x0375 }
    L_0x0126:
        r4 = r26.offset;	 Catch:{ Exception -> 0x0375 }
        r4 = r4 / 16;	 Catch:{ Exception -> 0x0375 }
        r9 = r1.encryptIv;	 Catch:{ Exception -> 0x0375 }
        r10 = r4 & 255;	 Catch:{ Exception -> 0x0375 }
        r10 = (byte) r10;	 Catch:{ Exception -> 0x0375 }
        r9[r14] = r10;	 Catch:{ Exception -> 0x0375 }
        r9 = r1.encryptIv;	 Catch:{ Exception -> 0x0375 }
        r10 = r4 >> 8;	 Catch:{ Exception -> 0x0375 }
        r10 = r10 & 255;	 Catch:{ Exception -> 0x0375 }
        r10 = (byte) r10;	 Catch:{ Exception -> 0x0375 }
        r9[r13] = r10;	 Catch:{ Exception -> 0x0375 }
        r9 = r1.encryptIv;	 Catch:{ Exception -> 0x0375 }
        r10 = r4 >> 16;	 Catch:{ Exception -> 0x0375 }
        r10 = r10 & 255;	 Catch:{ Exception -> 0x0375 }
        r10 = (byte) r10;	 Catch:{ Exception -> 0x0375 }
        r9[r12] = r10;	 Catch:{ Exception -> 0x0375 }
        r9 = r1.encryptIv;	 Catch:{ Exception -> 0x0375 }
        r10 = r4 >> 24;	 Catch:{ Exception -> 0x0375 }
        r10 = r10 & 255;	 Catch:{ Exception -> 0x0375 }
        r10 = (byte) r10;	 Catch:{ Exception -> 0x0375 }
        r9[r11] = r10;	 Catch:{ Exception -> 0x0375 }
        r9 = r7.buffer;	 Catch:{ Exception -> 0x0375 }
        r10 = r1.encryptKey;	 Catch:{ Exception -> 0x0375 }
        r11 = r1.encryptIv;	 Catch:{ Exception -> 0x0375 }
        r12 = r7.limit();	 Catch:{ Exception -> 0x0375 }
        org.telegram.messenger.Utilities.aesCtrDecryption(r9, r10, r11, r5, r12);	 Catch:{ Exception -> 0x0375 }
    L_0x015b:
        r4 = r1.notLoadedBytesRanges;	 Catch:{ Exception -> 0x0375 }
        if (r4 == 0) goto L_0x0169;	 Catch:{ Exception -> 0x0375 }
    L_0x015f:
        r4 = r1.fileOutputStream;	 Catch:{ Exception -> 0x0375 }
        r9 = r26.offset;	 Catch:{ Exception -> 0x0375 }
        r9 = (long) r9;	 Catch:{ Exception -> 0x0375 }
        r4.seek(r9);	 Catch:{ Exception -> 0x0375 }
    L_0x0169:
        r4 = r1.fileOutputStream;	 Catch:{ Exception -> 0x0375 }
        r4 = r4.getChannel();	 Catch:{ Exception -> 0x0375 }
        r9 = r7.buffer;	 Catch:{ Exception -> 0x0375 }
        r4.write(r9);	 Catch:{ Exception -> 0x0375 }
        r9 = r1.notLoadedBytesRanges;	 Catch:{ Exception -> 0x0375 }
        r10 = r26.offset;	 Catch:{ Exception -> 0x0375 }
        r11 = r26.offset;	 Catch:{ Exception -> 0x0375 }
        r11 = r11 + r8;	 Catch:{ Exception -> 0x0375 }
        r12 = 1;	 Catch:{ Exception -> 0x0375 }
        r1.addPart(r9, r10, r11, r12);	 Catch:{ Exception -> 0x0375 }
        r9 = r1.isCdn;	 Catch:{ Exception -> 0x0375 }
        if (r9 == 0) goto L_0x02c3;	 Catch:{ Exception -> 0x0375 }
    L_0x0187:
        r9 = r26.offset;	 Catch:{ Exception -> 0x0375 }
        r10 = 131072; // 0x20000 float:1.83671E-40 double:6.47582E-319;	 Catch:{ Exception -> 0x0375 }
        r9 = r9 / r10;	 Catch:{ Exception -> 0x0375 }
        r10 = r1.notCheckedCdnRanges;	 Catch:{ Exception -> 0x0375 }
        r10 = r10.size();	 Catch:{ Exception -> 0x0375 }
        r11 = 1;	 Catch:{ Exception -> 0x0375 }
        r12 = r5;	 Catch:{ Exception -> 0x0375 }
    L_0x0196:
        if (r12 >= r10) goto L_0x01b1;	 Catch:{ Exception -> 0x0375 }
    L_0x0198:
        r13 = r1.notCheckedCdnRanges;	 Catch:{ Exception -> 0x0375 }
        r13 = r13.get(r12);	 Catch:{ Exception -> 0x0375 }
        r13 = (org.telegram.messenger.FileLoadOperation.Range) r13;	 Catch:{ Exception -> 0x0375 }
        r14 = r13.start;	 Catch:{ Exception -> 0x0375 }
        if (r14 > r9) goto L_0x01ae;	 Catch:{ Exception -> 0x0375 }
    L_0x01a6:
        r14 = r13.end;	 Catch:{ Exception -> 0x0375 }
        if (r9 > r14) goto L_0x01ae;	 Catch:{ Exception -> 0x0375 }
    L_0x01ac:
        r11 = 0;	 Catch:{ Exception -> 0x0375 }
        goto L_0x01b1;	 Catch:{ Exception -> 0x0375 }
    L_0x01ae:
        r12 = r12 + 1;	 Catch:{ Exception -> 0x0375 }
        goto L_0x0196;	 Catch:{ Exception -> 0x0375 }
    L_0x01b1:
        if (r11 != 0) goto L_0x02c3;	 Catch:{ Exception -> 0x0375 }
    L_0x01b3:
        r12 = 131072; // 0x20000 float:1.83671E-40 double:6.47582E-319;	 Catch:{ Exception -> 0x0375 }
        r13 = r9 * r12;	 Catch:{ Exception -> 0x0375 }
        r14 = r1.notLoadedBytesRanges;	 Catch:{ Exception -> 0x0375 }
        r14 = r1.getDownloadedLengthFromOffsetInternal(r14, r13, r12);	 Catch:{ Exception -> 0x0375 }
        if (r14 == 0) goto L_0x02c3;	 Catch:{ Exception -> 0x0375 }
    L_0x01bf:
        if (r14 == r12) goto L_0x01d7;	 Catch:{ Exception -> 0x0375 }
    L_0x01c1:
        r12 = r1.totalBytesCount;	 Catch:{ Exception -> 0x0375 }
        if (r12 <= 0) goto L_0x01ca;	 Catch:{ Exception -> 0x0375 }
    L_0x01c5:
        r12 = r1.totalBytesCount;	 Catch:{ Exception -> 0x0375 }
        r12 = r12 - r13;	 Catch:{ Exception -> 0x0375 }
        if (r14 == r12) goto L_0x01d7;	 Catch:{ Exception -> 0x0375 }
    L_0x01ca:
        r12 = r1.totalBytesCount;	 Catch:{ Exception -> 0x0375 }
        if (r12 > 0) goto L_0x01d1;	 Catch:{ Exception -> 0x0375 }
    L_0x01ce:
        if (r3 == 0) goto L_0x01d1;	 Catch:{ Exception -> 0x0375 }
    L_0x01d0:
        goto L_0x01d7;	 Catch:{ Exception -> 0x0375 }
    L_0x01d1:
        r23 = r4;	 Catch:{ Exception -> 0x0375 }
        r24 = r7;	 Catch:{ Exception -> 0x0375 }
        goto L_0x02c7;	 Catch:{ Exception -> 0x0375 }
    L_0x01d7:
        r12 = r1.cdnHashes;	 Catch:{ Exception -> 0x0375 }
        r12 = r12.get(r13);	 Catch:{ Exception -> 0x0375 }
        r12 = (org.telegram.tgnet.TLRPC.TL_fileHash) r12;	 Catch:{ Exception -> 0x0375 }
        r15 = r1.fileReadStream;	 Catch:{ Exception -> 0x0375 }
        if (r15 != 0) goto L_0x01f7;	 Catch:{ Exception -> 0x0375 }
    L_0x01e3:
        r15 = 131072; // 0x20000 float:1.83671E-40 double:6.47582E-319;	 Catch:{ Exception -> 0x0375 }
        r15 = new byte[r15];	 Catch:{ Exception -> 0x0375 }
        r1.cdnCheckBytes = r15;	 Catch:{ Exception -> 0x0375 }
        r15 = new java.io.RandomAccessFile;	 Catch:{ Exception -> 0x0375 }
        r5 = r1.cacheFileTemp;	 Catch:{ Exception -> 0x0375 }
        r23 = r4;	 Catch:{ Exception -> 0x0375 }
        r4 = "r";	 Catch:{ Exception -> 0x0375 }
        r15.<init>(r5, r4);	 Catch:{ Exception -> 0x0375 }
        r1.fileReadStream = r15;	 Catch:{ Exception -> 0x0375 }
        goto L_0x01f9;	 Catch:{ Exception -> 0x0375 }
    L_0x01f7:
        r23 = r4;	 Catch:{ Exception -> 0x0375 }
    L_0x01f9:
        r4 = r1.fileReadStream;	 Catch:{ Exception -> 0x0375 }
        r5 = (long) r13;	 Catch:{ Exception -> 0x0375 }
        r4.seek(r5);	 Catch:{ Exception -> 0x0375 }
        r4 = r1.fileReadStream;	 Catch:{ Exception -> 0x0375 }
        r5 = r1.cdnCheckBytes;	 Catch:{ Exception -> 0x0375 }
        r6 = 0;	 Catch:{ Exception -> 0x0375 }
        r4.readFully(r5, r6, r14);	 Catch:{ Exception -> 0x0375 }
        r4 = r1.cdnCheckBytes;	 Catch:{ Exception -> 0x0375 }
        r4 = org.telegram.messenger.Utilities.computeSHA256(r4, r6, r14);	 Catch:{ Exception -> 0x0375 }
        r5 = r12.hash;	 Catch:{ Exception -> 0x0375 }
        r5 = java.util.Arrays.equals(r4, r5);	 Catch:{ Exception -> 0x0375 }
        if (r5 != 0) goto L_0x02b3;	 Catch:{ Exception -> 0x0375 }
    L_0x0215:
        r5 = org.telegram.messenger.BuildVars.LOGS_ENABLED;	 Catch:{ Exception -> 0x0375 }
        if (r5 == 0) goto L_0x02a7;	 Catch:{ Exception -> 0x0375 }
    L_0x0219:
        r5 = r1.location;	 Catch:{ Exception -> 0x0375 }
        if (r5 == 0) goto L_0x0272;	 Catch:{ Exception -> 0x0375 }
    L_0x021d:
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0375 }
        r5.<init>();	 Catch:{ Exception -> 0x0375 }
        r6 = "invalid cdn hash ";	 Catch:{ Exception -> 0x0375 }
        r5.append(r6);	 Catch:{ Exception -> 0x0375 }
        r6 = r1.location;	 Catch:{ Exception -> 0x0375 }
        r5.append(r6);	 Catch:{ Exception -> 0x0375 }
        r6 = " id = ";	 Catch:{ Exception -> 0x0375 }
        r5.append(r6);	 Catch:{ Exception -> 0x0375 }
        r6 = r1.location;	 Catch:{ Exception -> 0x0375 }
        r24 = r7;	 Catch:{ Exception -> 0x0375 }
        r6 = r6.id;	 Catch:{ Exception -> 0x0375 }
        r5.append(r6);	 Catch:{ Exception -> 0x0375 }
        r6 = " local_id = ";	 Catch:{ Exception -> 0x0375 }
        r5.append(r6);	 Catch:{ Exception -> 0x0375 }
        r6 = r1.location;	 Catch:{ Exception -> 0x0375 }
        r6 = r6.local_id;	 Catch:{ Exception -> 0x0375 }
        r5.append(r6);	 Catch:{ Exception -> 0x0375 }
        r6 = " access_hash = ";	 Catch:{ Exception -> 0x0375 }
        r5.append(r6);	 Catch:{ Exception -> 0x0375 }
        r6 = r1.location;	 Catch:{ Exception -> 0x0375 }
        r6 = r6.access_hash;	 Catch:{ Exception -> 0x0375 }
        r5.append(r6);	 Catch:{ Exception -> 0x0375 }
        r6 = " volume_id = ";	 Catch:{ Exception -> 0x0375 }
        r5.append(r6);	 Catch:{ Exception -> 0x0375 }
        r6 = r1.location;	 Catch:{ Exception -> 0x0375 }
        r6 = r6.volume_id;	 Catch:{ Exception -> 0x0375 }
        r5.append(r6);	 Catch:{ Exception -> 0x0375 }
        r6 = " secret = ";	 Catch:{ Exception -> 0x0375 }
        r5.append(r6);	 Catch:{ Exception -> 0x0375 }
        r6 = r1.location;	 Catch:{ Exception -> 0x0375 }
        r6 = r6.secret;	 Catch:{ Exception -> 0x0375 }
        r5.append(r6);	 Catch:{ Exception -> 0x0375 }
        r5 = r5.toString();	 Catch:{ Exception -> 0x0375 }
        org.telegram.messenger.FileLog.e(r5);	 Catch:{ Exception -> 0x0375 }
        goto L_0x02a9;	 Catch:{ Exception -> 0x0375 }
    L_0x0272:
        r24 = r7;	 Catch:{ Exception -> 0x0375 }
        r5 = r1.webLocation;	 Catch:{ Exception -> 0x0375 }
        if (r5 == 0) goto L_0x02a9;	 Catch:{ Exception -> 0x0375 }
    L_0x0278:
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0375 }
        r5.<init>();	 Catch:{ Exception -> 0x0375 }
        r6 = "invalid cdn hash  ";	 Catch:{ Exception -> 0x0375 }
        r5.append(r6);	 Catch:{ Exception -> 0x0375 }
        r6 = r1.webLocation;	 Catch:{ Exception -> 0x0375 }
        r5.append(r6);	 Catch:{ Exception -> 0x0375 }
        r6 = " id = ";	 Catch:{ Exception -> 0x0375 }
        r5.append(r6);	 Catch:{ Exception -> 0x0375 }
        r6 = r1.webLocation;	 Catch:{ Exception -> 0x0375 }
        r6 = r6.url;	 Catch:{ Exception -> 0x0375 }
        r5.append(r6);	 Catch:{ Exception -> 0x0375 }
        r6 = " access_hash = ";	 Catch:{ Exception -> 0x0375 }
        r5.append(r6);	 Catch:{ Exception -> 0x0375 }
        r6 = r1.webLocation;	 Catch:{ Exception -> 0x0375 }
        r6 = r6.access_hash;	 Catch:{ Exception -> 0x0375 }
        r5.append(r6);	 Catch:{ Exception -> 0x0375 }
        r5 = r5.toString();	 Catch:{ Exception -> 0x0375 }
        org.telegram.messenger.FileLog.e(r5);	 Catch:{ Exception -> 0x0375 }
        goto L_0x02a9;	 Catch:{ Exception -> 0x0375 }
    L_0x02a7:
        r24 = r7;	 Catch:{ Exception -> 0x0375 }
    L_0x02a9:
        r5 = 0;	 Catch:{ Exception -> 0x0375 }
        r1.onFail(r5, r5);	 Catch:{ Exception -> 0x0375 }
        r6 = r1.cacheFileTemp;	 Catch:{ Exception -> 0x0375 }
        r6.delete();	 Catch:{ Exception -> 0x0375 }
        return r5;	 Catch:{ Exception -> 0x0375 }
    L_0x02b3:
        r24 = r7;	 Catch:{ Exception -> 0x0375 }
        r5 = r1.cdnHashes;	 Catch:{ Exception -> 0x0375 }
        r5.remove(r13);	 Catch:{ Exception -> 0x0375 }
        r5 = r1.notCheckedCdnRanges;	 Catch:{ Exception -> 0x0375 }
        r6 = r9 + 1;	 Catch:{ Exception -> 0x0375 }
        r7 = 0;	 Catch:{ Exception -> 0x0375 }
        r1.addPart(r5, r9, r6, r7);	 Catch:{ Exception -> 0x0375 }
        goto L_0x02c7;	 Catch:{ Exception -> 0x0375 }
    L_0x02c3:
        r23 = r4;	 Catch:{ Exception -> 0x0375 }
        r24 = r7;	 Catch:{ Exception -> 0x0375 }
    L_0x02c7:
        r4 = r1.fiv;	 Catch:{ Exception -> 0x0375 }
        if (r4 == 0) goto L_0x02d9;	 Catch:{ Exception -> 0x0375 }
    L_0x02cb:
        r4 = r1.fiv;	 Catch:{ Exception -> 0x0375 }
        r5 = 0;	 Catch:{ Exception -> 0x0375 }
        r4.seek(r5);	 Catch:{ Exception -> 0x0375 }
        r4 = r1.fiv;	 Catch:{ Exception -> 0x0375 }
        r5 = r1.iv;	 Catch:{ Exception -> 0x0375 }
        r4.write(r5);	 Catch:{ Exception -> 0x0375 }
    L_0x02d9:
        r4 = r1.totalBytesCount;	 Catch:{ Exception -> 0x0375 }
        if (r4 <= 0) goto L_0x02f7;	 Catch:{ Exception -> 0x0375 }
    L_0x02dd:
        r4 = r1.state;	 Catch:{ Exception -> 0x0375 }
        r5 = 1;	 Catch:{ Exception -> 0x0375 }
        if (r4 != r5) goto L_0x02f7;	 Catch:{ Exception -> 0x0375 }
    L_0x02e2:
        r25.copytNotLoadedRanges();	 Catch:{ Exception -> 0x0375 }
        r4 = r1.delegate;	 Catch:{ Exception -> 0x0375 }
        r5 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;	 Catch:{ Exception -> 0x0375 }
        r6 = r1.downloadedBytes;	 Catch:{ Exception -> 0x0375 }
        r6 = (float) r6;	 Catch:{ Exception -> 0x0375 }
        r7 = r1.totalBytesCount;	 Catch:{ Exception -> 0x0375 }
        r7 = (float) r7;	 Catch:{ Exception -> 0x0375 }
        r6 = r6 / r7;	 Catch:{ Exception -> 0x0375 }
        r5 = java.lang.Math.min(r5, r6);	 Catch:{ Exception -> 0x0375 }
        r4.didChangedLoadProgress(r1, r5);	 Catch:{ Exception -> 0x0375 }
    L_0x02f7:
        r4 = 0;	 Catch:{ Exception -> 0x0375 }
    L_0x02f8:
        r5 = r1.delayedRequestInfos;	 Catch:{ Exception -> 0x0375 }
        r5 = r5.size();	 Catch:{ Exception -> 0x0375 }
        if (r4 >= r5) goto L_0x0362;	 Catch:{ Exception -> 0x0375 }
    L_0x0300:
        r5 = r1.delayedRequestInfos;	 Catch:{ Exception -> 0x0375 }
        r5 = r5.get(r4);	 Catch:{ Exception -> 0x0375 }
        r5 = (org.telegram.messenger.FileLoadOperation.RequestInfo) r5;	 Catch:{ Exception -> 0x0375 }
        r6 = r1.notLoadedBytesRanges;	 Catch:{ Exception -> 0x0375 }
        if (r6 != 0) goto L_0x0318;	 Catch:{ Exception -> 0x0375 }
    L_0x030c:
        r6 = r1.downloadedBytes;	 Catch:{ Exception -> 0x0375 }
        r7 = r5.offset;	 Catch:{ Exception -> 0x0375 }
        if (r6 != r7) goto L_0x0315;	 Catch:{ Exception -> 0x0375 }
    L_0x0314:
        goto L_0x0318;	 Catch:{ Exception -> 0x0375 }
    L_0x0315:
        r4 = r4 + 1;	 Catch:{ Exception -> 0x0375 }
        goto L_0x02f8;	 Catch:{ Exception -> 0x0375 }
    L_0x0318:
        r6 = r1.delayedRequestInfos;	 Catch:{ Exception -> 0x0375 }
        r6.remove(r4);	 Catch:{ Exception -> 0x0375 }
        r6 = 0;	 Catch:{ Exception -> 0x0375 }
        r6 = r1.processRequestResult(r5, r6);	 Catch:{ Exception -> 0x0375 }
        if (r6 != 0) goto L_0x0362;	 Catch:{ Exception -> 0x0375 }
    L_0x0324:
        r6 = r5.response;	 Catch:{ Exception -> 0x0375 }
        if (r6 == 0) goto L_0x0339;	 Catch:{ Exception -> 0x0375 }
    L_0x032a:
        r6 = r5.response;	 Catch:{ Exception -> 0x0375 }
        r7 = 0;	 Catch:{ Exception -> 0x0375 }
        r6.disableFree = r7;	 Catch:{ Exception -> 0x0375 }
        r6 = r5.response;	 Catch:{ Exception -> 0x0375 }
        r6.freeResources();	 Catch:{ Exception -> 0x0375 }
        goto L_0x0362;	 Catch:{ Exception -> 0x0375 }
    L_0x0339:
        r6 = r5.responseWeb;	 Catch:{ Exception -> 0x0375 }
        if (r6 == 0) goto L_0x034e;	 Catch:{ Exception -> 0x0375 }
    L_0x033f:
        r6 = r5.responseWeb;	 Catch:{ Exception -> 0x0375 }
        r7 = 0;	 Catch:{ Exception -> 0x0375 }
        r6.disableFree = r7;	 Catch:{ Exception -> 0x0375 }
        r6 = r5.responseWeb;	 Catch:{ Exception -> 0x0375 }
        r6.freeResources();	 Catch:{ Exception -> 0x0375 }
        goto L_0x0362;	 Catch:{ Exception -> 0x0375 }
    L_0x034e:
        r6 = r5.responseCdn;	 Catch:{ Exception -> 0x0375 }
        if (r6 == 0) goto L_0x0362;	 Catch:{ Exception -> 0x0375 }
    L_0x0354:
        r6 = r5.responseCdn;	 Catch:{ Exception -> 0x0375 }
        r7 = 0;	 Catch:{ Exception -> 0x0375 }
        r6.disableFree = r7;	 Catch:{ Exception -> 0x0375 }
        r6 = r5.responseCdn;	 Catch:{ Exception -> 0x0375 }
        r6.freeResources();	 Catch:{ Exception -> 0x0375 }
    L_0x0362:
        if (r3 == 0) goto L_0x0369;	 Catch:{ Exception -> 0x0375 }
    L_0x0364:
        r4 = 1;	 Catch:{ Exception -> 0x0375 }
        r1.onFinishLoadingFile(r4);	 Catch:{ Exception -> 0x0375 }
        goto L_0x037e;	 Catch:{ Exception -> 0x0375 }
    L_0x0369:
        r25.startDownloadRequest();	 Catch:{ Exception -> 0x0375 }
        goto L_0x037e;	 Catch:{ Exception -> 0x0375 }
    L_0x036d:
        r24 = r7;	 Catch:{ Exception -> 0x0375 }
    L_0x036f:
        r3 = 1;	 Catch:{ Exception -> 0x0375 }
        r1.onFinishLoadingFile(r3);	 Catch:{ Exception -> 0x0375 }
        r3 = 0;
        return r3;
    L_0x0375:
        r0 = move-exception;
        r3 = r0;
        r4 = 0;
        r1.onFail(r4, r4);
        org.telegram.messenger.FileLog.e(r3);
        r3 = 0;
        goto L_0x048c;
    L_0x0382:
        r6 = 0;
        r3 = r2.text;
        r4 = "FILE_MIGRATE_";
        r3 = r3.contains(r4);
        if (r3 == 0) goto L_0x03c4;
        r3 = r2.text;
        r4 = "FILE_MIGRATE_";
        r5 = "";
        r3 = r3.replace(r4, r5);
        r4 = new java.util.Scanner;
        r4.<init>(r3);
        r5 = "";
        r4.useDelimiter(r5);
        r5 = r4.nextInt();	 Catch:{ Exception -> 0x03aa }
        r5 = java.lang.Integer.valueOf(r5);	 Catch:{ Exception -> 0x03aa }
        goto L_0x03ae;
    L_0x03aa:
        r0 = move-exception;
        r5 = r0;
        r5 = r6;
        if (r5 != 0) goto L_0x03b5;
        r6 = 0;
        r1.onFail(r6, r6);
        goto L_0x03c3;
        r6 = 0;
        r7 = r5.intValue();
        r1.datacenter_id = r7;
        r1.downloadedBytes = r6;
        r1.requestedBytesCount = r6;
        r25.startDownloadRequest();
        goto L_0x037f;
        r3 = r2.text;
        r4 = "OFFSET_INVALID";
        r3 = r3.contains(r4);
        if (r3 == 0) goto L_0x03eb;
        r3 = r1.downloadedBytes;
        r4 = r1.currentDownloadChunkSize;
        r3 = r3 % r4;
        if (r3 != 0) goto L_0x03e4;
        r3 = 1;
        r1.onFinishLoadingFile(r3);	 Catch:{ Exception -> 0x03da }
        goto L_0x037f;
    L_0x03da:
        r0 = move-exception;
        r3 = r0;
        org.telegram.messenger.FileLog.e(r3);
        r4 = 0;
        r1.onFail(r4, r4);
        goto L_0x03e8;
        r4 = 0;
        r1.onFail(r4, r4);
        r3 = r4;
        goto L_0x048c;
        r4 = 0;
        r3 = r2.text;
        r5 = "RETRY_LIMIT";
        r3 = r3.contains(r5);
        if (r3 == 0) goto L_0x03fb;
        r3 = 2;
        r1.onFail(r4, r3);
        goto L_0x03e8;
        r3 = org.telegram.messenger.BuildVars.LOGS_ENABLED;
        if (r3 == 0) goto L_0x0488;
        r3 = r1.location;
        if (r3 == 0) goto L_0x0456;
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "";
        r3.append(r4);
        r4 = r1.location;
        r3.append(r4);
        r4 = " id = ";
        r3.append(r4);
        r4 = r1.location;
        r4 = r4.id;
        r3.append(r4);
        r4 = " local_id = ";
        r3.append(r4);
        r4 = r1.location;
        r4 = r4.local_id;
        r3.append(r4);
        r4 = " access_hash = ";
        r3.append(r4);
        r4 = r1.location;
        r4 = r4.access_hash;
        r3.append(r4);
        r4 = " volume_id = ";
        r3.append(r4);
        r4 = r1.location;
        r4 = r4.volume_id;
        r3.append(r4);
        r4 = " secret = ";
        r3.append(r4);
        r4 = r1.location;
        r4 = r4.secret;
        r3.append(r4);
        r3 = r3.toString();
        org.telegram.messenger.FileLog.e(r3);
        goto L_0x0488;
        r3 = r1.webLocation;
        if (r3 == 0) goto L_0x0488;
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "";
        r3.append(r4);
        r4 = r1.webLocation;
        r3.append(r4);
        r4 = " id = ";
        r3.append(r4);
        r4 = r1.webLocation;
        r4 = r4.url;
        r3.append(r4);
        r4 = " access_hash = ";
        r3.append(r4);
        r4 = r1.webLocation;
        r4 = r4.access_hash;
        r3.append(r4);
        r3 = r3.toString();
        org.telegram.messenger.FileLog.e(r3);
        r3 = 0;
        r1.onFail(r3, r3);
        return r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.FileLoadOperation.processRequestResult(org.telegram.messenger.FileLoadOperation$RequestInfo, org.telegram.tgnet.TLRPC$TL_error):boolean");
    }

    public FileLoadOperation(FileLocation photoLocation, String extension, int size) {
        this.state = 0;
        if (photoLocation instanceof TL_fileEncryptedLocation) {
            this.location = new TL_inputEncryptedFileLocation();
            this.location.id = photoLocation.volume_id;
            this.location.volume_id = photoLocation.volume_id;
            this.location.access_hash = photoLocation.secret;
            this.location.local_id = photoLocation.local_id;
            this.iv = new byte[32];
            System.arraycopy(photoLocation.iv, 0, this.iv, 0, this.iv.length);
            this.key = photoLocation.key;
            this.datacenter_id = photoLocation.dc_id;
        } else if (photoLocation instanceof TL_fileLocation) {
            this.location = new TL_inputFileLocation();
            this.location.volume_id = photoLocation.volume_id;
            this.location.secret = photoLocation.secret;
            this.location.local_id = photoLocation.local_id;
            this.datacenter_id = photoLocation.dc_id;
            this.allowDisordererFileSave = true;
        }
        this.currentType = 16777216;
        this.totalBytesCount = size;
        this.ext = extension != null ? extension : "jpg";
    }

    public FileLoadOperation(TL_webDocument webDocument) {
        this.state = 0;
        this.webLocation = new TL_inputWebFileLocation();
        this.webLocation.url = webDocument.url;
        this.webLocation.access_hash = webDocument.access_hash;
        this.totalBytesCount = webDocument.size;
        this.datacenter_id = webDocument.dc_id;
        String defaultExt = FileLoader.getExtensionByMime(webDocument.mime_type);
        if (webDocument.mime_type.startsWith("image/")) {
            this.currentType = 16777216;
        } else if (webDocument.mime_type.equals("audio/ogg")) {
            this.currentType = ConnectionsManager.FileTypeAudio;
        } else if (webDocument.mime_type.startsWith("video/")) {
            this.currentType = ConnectionsManager.FileTypeVideo;
        } else {
            this.currentType = ConnectionsManager.FileTypeFile;
        }
        this.allowDisordererFileSave = true;
        this.ext = ImageLoader.getHttpUrlExtension(webDocument.url, defaultExt);
    }

    public void setEncryptFile(boolean value) {
        this.encryptFile = value;
        if (this.encryptFile) {
            this.allowDisordererFileSave = false;
        }
    }

    public void setForceRequest(boolean forceRequest) {
        this.isForceRequest = forceRequest;
    }

    public boolean isForceRequest() {
        return this.isForceRequest;
    }

    public void setPaths(int instance, File store, File temp) {
        this.storePath = store;
        this.tempPath = temp;
        this.currentAccount = instance;
    }

    public boolean wasStarted() {
        return this.started;
    }

    public int getCurrentType() {
        return this.currentType;
    }

    private void removePart(ArrayList<Range> ranges, int start, int end) {
        if (ranges != null) {
            if (end >= start) {
                int count = ranges.size();
                boolean modified = false;
                int a = 0;
                while (a < count) {
                    Range range = (Range) ranges.get(a);
                    if (start == range.end) {
                        range.end = end;
                        modified = true;
                        break;
                    } else if (end == range.start) {
                        range.start = start;
                        modified = true;
                        break;
                    } else {
                        a++;
                    }
                }
                if (!modified) {
                    ranges.add(new Range(start, end));
                }
            }
        }
    }

    private void addPart(ArrayList<Range> ranges, int start, int end, boolean save) {
        if (ranges != null) {
            if (end >= start) {
                int a;
                Range range;
                boolean modified = false;
                int count = ranges.size();
                int a2 = 0;
                for (a = 0; a < count; a++) {
                    range = (Range) ranges.get(a);
                    if (start <= range.start) {
                        if (end >= range.end) {
                            ranges.remove(a);
                            modified = true;
                            break;
                        } else if (end > range.start) {
                            range.start = end;
                            modified = true;
                            break;
                        }
                    } else if (end < range.end) {
                        ranges.add(0, new Range(range.start, start));
                        modified = true;
                        range.start = end;
                        break;
                    } else if (start < range.end) {
                        range.end = start;
                        modified = true;
                        break;
                    }
                }
                if (save) {
                    if (modified) {
                        try {
                            this.filePartsStream.seek(0);
                            count = ranges.size();
                            this.filePartsStream.writeInt(count);
                            for (a = 0; a < count; a++) {
                                range = (Range) ranges.get(a);
                                this.filePartsStream.writeInt(range.start);
                                this.filePartsStream.writeInt(range.end);
                            }
                        } catch (Throwable e) {
                            FileLog.e(e);
                        }
                        if (this.streamListeners != null) {
                            count = this.streamListeners.size();
                            while (a2 < count) {
                                ((FileStreamLoadOperation) this.streamListeners.get(a2)).newDataAvailable();
                                a2++;
                            }
                        }
                    } else if (BuildVars.LOGS_ENABLED) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(this.cacheFileFinal);
                        stringBuilder.append(" downloaded duplicate file part ");
                        stringBuilder.append(start);
                        stringBuilder.append(" - ");
                        stringBuilder.append(end);
                        FileLog.e(stringBuilder.toString());
                    }
                }
            }
        }
    }

    protected File getCurrentFile() {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final File[] result = new File[1];
        Utilities.stageQueue.postRunnable(new Runnable() {
            public void run() {
                if (FileLoadOperation.this.state == 3) {
                    result[0] = FileLoadOperation.this.cacheFileFinal;
                } else {
                    result[0] = FileLoadOperation.this.cacheFileTemp;
                }
                countDownLatch.countDown();
            }
        });
        try {
            countDownLatch.await();
        } catch (Throwable e) {
            FileLog.e(e);
        }
        return result[0];
    }

    private int getDownloadedLengthFromOffsetInternal(ArrayList<Range> ranges, int offset, int length) {
        if (!(ranges == null || this.state == 3)) {
            if (!ranges.isEmpty()) {
                int count = ranges.size();
                int availableLength = length;
                Range minRange = null;
                for (int a = 0; a < count; a++) {
                    Range range = (Range) ranges.get(a);
                    if (offset <= range.start && (minRange == null || range.start < minRange.start)) {
                        minRange = range;
                    }
                    if (range.start <= offset && range.end > offset) {
                        availableLength = 0;
                    }
                }
                if (availableLength == 0) {
                    return 0;
                }
                if (minRange != null) {
                    return Math.min(length, minRange.start - offset);
                }
                return Math.min(length, Math.max(this.totalBytesCount - offset, 0));
            }
        }
        if (this.downloadedBytes == 0) {
            return length;
        }
        return Math.min(length, Math.max(this.downloadedBytes - offset, 0));
    }

    protected float getDownloadedLengthFromOffset(float progress) {
        ArrayList<Range> ranges = this.notLoadedBytesRangesCopy;
        if (this.totalBytesCount != 0) {
            if (ranges != null) {
                return (((float) getDownloadedLengthFromOffsetInternal(ranges, (int) (((float) this.totalBytesCount) * progress), this.totalBytesCount)) / ((float) this.totalBytesCount)) + progress;
            }
        }
        return 0.0f;
    }

    protected int getDownloadedLengthFromOffset(int offset, int length) {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        int[] result = new int[1];
        final int[] iArr = result;
        final int i = offset;
        final int i2 = length;
        final CountDownLatch countDownLatch2 = countDownLatch;
        Utilities.stageQueue.postRunnable(new Runnable() {
            public void run() {
                iArr[0] = FileLoadOperation.this.getDownloadedLengthFromOffsetInternal(FileLoadOperation.this.notLoadedBytesRanges, i, i2);
                countDownLatch2.countDown();
            }
        });
        try {
            countDownLatch.await();
        } catch (Throwable e) {
            FileLog.e(e);
        }
        return result[0];
    }

    public String getFileName() {
        if (this.location != null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.location.volume_id);
            stringBuilder.append("_");
            stringBuilder.append(this.location.local_id);
            stringBuilder.append(".");
            stringBuilder.append(this.ext);
            return stringBuilder.toString();
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append(Utilities.MD5(this.webLocation.url));
        stringBuilder.append(".");
        stringBuilder.append(this.ext);
        return stringBuilder.toString();
    }

    protected void removeStreamListener(final FileStreamLoadOperation operation) {
        Utilities.stageQueue.postRunnable(new Runnable() {
            public void run() {
                if (FileLoadOperation.this.streamListeners != null) {
                    FileLoadOperation.this.streamListeners.remove(operation);
                }
            }
        });
    }

    private void copytNotLoadedRanges() {
        if (this.notLoadedBytesRanges != null) {
            this.notLoadedBytesRangesCopy = new ArrayList(this.notLoadedBytesRanges);
        }
    }

    public void pause() {
        if (this.state == 1) {
            Utilities.stageQueue.postRunnable(new Runnable() {
                public void run() {
                    FileLoadOperation.this.paused = true;
                }
            });
        }
    }

    public boolean start() {
        return start(null, 0);
    }

    public boolean start(FileStreamLoadOperation stream, int streamOffset) {
        Throwable e;
        final FileStreamLoadOperation fileStreamLoadOperation = stream;
        final int i = streamOffset;
        if (this.currentDownloadChunkSize == 0) {
            r1.currentDownloadChunkSize = r1.totalBytesCount >= 1048576 ? 131072 : 32768;
            int i2 = r1.totalBytesCount;
            r1.currentMaxDownloadRequests = 4;
        }
        final boolean alreadyStarted = r1.state != 0;
        boolean wasPaused = r1.paused;
        r1.paused = false;
        if (fileStreamLoadOperation != null) {
            Utilities.stageQueue.postRunnable(new Runnable() {
                public void run() {
                    if (FileLoadOperation.this.streamListeners == null) {
                        FileLoadOperation.this.streamListeners = new ArrayList();
                    }
                    FileLoadOperation.this.streamStartOffset = (i / FileLoadOperation.this.currentDownloadChunkSize) * FileLoadOperation.this.currentDownloadChunkSize;
                    FileLoadOperation.this.streamListeners.add(fileStreamLoadOperation);
                    if (alreadyStarted) {
                        FileLoadOperation.this.startDownloadRequest();
                    }
                }
            });
        } else if (wasPaused && alreadyStarted) {
            Utilities.stageQueue.postRunnable(new Runnable() {
                public void run() {
                    FileLoadOperation.this.startDownloadRequest();
                }
            });
        }
        if (alreadyStarted) {
            return wasPaused;
        }
        if (r1.location == null && r1.webLocation == null) {
            onFail(true, 0);
            return false;
        }
        String fileNameTemp;
        String fileNameFinal;
        r1.streamStartOffset = (i / r1.currentDownloadChunkSize) * r1.currentDownloadChunkSize;
        if (r1.allowDisordererFileSave && r1.totalBytesCount > 0 && r1.totalBytesCount > r1.currentDownloadChunkSize) {
            r1.notLoadedBytesRanges = new ArrayList();
            r1.notRequestedBytesRanges = new ArrayList();
        }
        String fileNameParts = null;
        String fileNameIv = null;
        StringBuilder stringBuilder;
        if (r1.webLocation != null) {
            String md5 = Utilities.MD5(r1.webLocation.url);
            if (r1.encryptFile) {
                fileNameTemp = new StringBuilder();
                fileNameTemp.append(md5);
                fileNameTemp.append(".temp.enc");
                fileNameTemp = fileNameTemp.toString();
                fileNameFinal = new StringBuilder();
                fileNameFinal.append(md5);
                fileNameFinal.append(".");
                fileNameFinal.append(r1.ext);
                fileNameFinal.append(".enc");
                fileNameFinal = fileNameFinal.toString();
                if (r1.key != null) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append(md5);
                    stringBuilder2.append(".iv.enc");
                    fileNameIv = stringBuilder2.toString();
                }
            } else {
                stringBuilder = new StringBuilder();
                stringBuilder.append(md5);
                stringBuilder.append(".temp");
                fileNameTemp = stringBuilder.toString();
                stringBuilder = new StringBuilder();
                stringBuilder.append(md5);
                stringBuilder.append(".");
                stringBuilder.append(r1.ext);
                fileNameFinal = stringBuilder.toString();
                if (r1.key != null) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(md5);
                    stringBuilder.append(".iv");
                    fileNameIv = stringBuilder.toString();
                }
            }
        } else if (r1.location.volume_id == 0 || r1.location.local_id == 0) {
            boolean z;
            if (r1.datacenter_id == 0) {
                z = false;
                alreadyStarted = true;
            } else if (r1.location.id == 0) {
                boolean z2 = alreadyStarted;
                z = false;
                alreadyStarted = true;
            } else if (r1.encryptFile) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(r1.datacenter_id);
                stringBuilder.append("_");
                stringBuilder.append(r1.location.id);
                stringBuilder.append(".temp.enc");
                fileNameTemp = stringBuilder.toString();
                stringBuilder = new StringBuilder();
                stringBuilder.append(r1.datacenter_id);
                stringBuilder.append("_");
                stringBuilder.append(r1.location.id);
                stringBuilder.append(r1.ext);
                stringBuilder.append(".enc");
                fileNameFinal = stringBuilder.toString();
                if (r1.key != null) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(r1.datacenter_id);
                    stringBuilder.append("_");
                    stringBuilder.append(r1.location.id);
                    stringBuilder.append(".iv.enc");
                    fileNameIv = stringBuilder.toString();
                }
            } else {
                stringBuilder = new StringBuilder();
                stringBuilder.append(r1.datacenter_id);
                stringBuilder.append("_");
                stringBuilder.append(r1.location.id);
                stringBuilder.append(".temp");
                fileNameTemp = stringBuilder.toString();
                stringBuilder = new StringBuilder();
                stringBuilder.append(r1.datacenter_id);
                stringBuilder.append("_");
                stringBuilder.append(r1.location.id);
                stringBuilder.append(r1.ext);
                fileNameFinal = stringBuilder.toString();
                if (r1.key != null) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(r1.datacenter_id);
                    stringBuilder.append("_");
                    stringBuilder.append(r1.location.id);
                    stringBuilder.append(".iv");
                    fileNameIv = stringBuilder.toString();
                }
                if (r1.notLoadedBytesRanges != null) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(r1.datacenter_id);
                    stringBuilder.append("_");
                    stringBuilder.append(r1.location.id);
                    stringBuilder.append(".pt");
                    fileNameParts = stringBuilder.toString();
                }
            }
            onFail(alreadyStarted, z);
            return z;
        } else {
            if (!(r1.datacenter_id == Integer.MIN_VALUE || r1.location.volume_id == -2147483648L)) {
                if (r1.datacenter_id != 0) {
                    if (r1.encryptFile) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append(r1.location.volume_id);
                        stringBuilder.append("_");
                        stringBuilder.append(r1.location.local_id);
                        stringBuilder.append(".temp.enc");
                        fileNameTemp = stringBuilder.toString();
                        stringBuilder = new StringBuilder();
                        stringBuilder.append(r1.location.volume_id);
                        stringBuilder.append("_");
                        stringBuilder.append(r1.location.local_id);
                        stringBuilder.append(".");
                        stringBuilder.append(r1.ext);
                        stringBuilder.append(".enc");
                        fileNameFinal = stringBuilder.toString();
                        if (r1.key != null) {
                            stringBuilder = new StringBuilder();
                            stringBuilder.append(r1.location.volume_id);
                            stringBuilder.append("_");
                            stringBuilder.append(r1.location.local_id);
                            stringBuilder.append(".iv.enc");
                            fileNameIv = stringBuilder.toString();
                        }
                    } else {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append(r1.location.volume_id);
                        stringBuilder.append("_");
                        stringBuilder.append(r1.location.local_id);
                        stringBuilder.append(".temp");
                        fileNameTemp = stringBuilder.toString();
                        stringBuilder = new StringBuilder();
                        stringBuilder.append(r1.location.volume_id);
                        stringBuilder.append("_");
                        stringBuilder.append(r1.location.local_id);
                        stringBuilder.append(".");
                        stringBuilder.append(r1.ext);
                        fileNameFinal = stringBuilder.toString();
                        if (r1.key != null) {
                            String fileNameIv2 = new StringBuilder();
                            fileNameIv2.append(r1.location.volume_id);
                            fileNameIv2.append("_");
                            fileNameIv2.append(r1.location.local_id);
                            fileNameIv2.append(".iv");
                            fileNameIv = fileNameIv2.toString();
                        }
                        if (r1.notLoadedBytesRanges != null) {
                            stringBuilder = new StringBuilder();
                            stringBuilder.append(r1.location.volume_id);
                            stringBuilder.append("_");
                            stringBuilder.append(r1.location.local_id);
                            stringBuilder.append(".pt");
                            fileNameParts = stringBuilder.toString();
                        }
                    }
                }
            }
            onFail(true, 0);
            return false;
        }
        r1.requestInfos = new ArrayList(r1.currentMaxDownloadRequests);
        r1.delayedRequestInfos = new ArrayList(r1.currentMaxDownloadRequests - 1);
        r1.state = 1;
        r1.cacheFileFinal = new File(r1.storePath, fileNameFinal);
        boolean finalFileExist = r1.cacheFileFinal.exists();
        if (!(!finalFileExist || r1.totalBytesCount == 0 || ((long) r1.totalBytesCount) == r1.cacheFileFinal.length())) {
            r1.cacheFileFinal.delete();
            finalFileExist = false;
        }
        if (finalFileExist) {
            r1.started = true;
            try {
                onFinishLoadingFile(false);
                alreadyStarted = true;
            } catch (Exception e2) {
                alreadyStarted = true;
                onFail(true, 0);
            }
        } else {
            long len;
            long totalDownloadedLen;
            int size;
            Range alreadyStarted2;
            StringBuilder stringBuilder3;
            r1.cacheFileTemp = new File(r1.tempPath, fileNameTemp);
            boolean newKeyGenerated = false;
            if (r1.encryptFile) {
                File internalCacheDir = FileLoader.getInternalCacheDir();
                StringBuilder stringBuilder4 = new StringBuilder();
                stringBuilder4.append(fileNameFinal);
                stringBuilder4.append(".key");
                File keyFile = new File(internalCacheDir, stringBuilder4.toString());
                try {
                    RandomAccessFile file = new RandomAccessFile(keyFile, "rws");
                    len = keyFile.length();
                    r1.encryptKey = new byte[32];
                    r1.encryptIv = new byte[16];
                    if (len <= 0 || len % 48 != 0) {
                        Utilities.random.nextBytes(r1.encryptKey);
                        Utilities.random.nextBytes(r1.encryptIv);
                        file.write(r1.encryptKey);
                        file.write(r1.encryptIv);
                        newKeyGenerated = true;
                    } else {
                        file.read(r1.encryptKey, 0, 32);
                        file.read(r1.encryptIv, 0, 16);
                    }
                    try {
                        file.getChannel().close();
                    } catch (Throwable e3) {
                        FileLog.e(e3);
                    }
                    file.close();
                } catch (Throwable e32) {
                    FileLog.e(e32);
                }
            }
            if (fileNameParts != null) {
                r1.cacheFileParts = new File(r1.tempPath, fileNameParts);
                try {
                    r1.filePartsStream = new RandomAccessFile(r1.cacheFileParts, "rws");
                    long len2 = r1.filePartsStream.length();
                    if (len2 % 8 == 4) {
                        len = len2 - 4;
                        int count = r1.filePartsStream.readInt();
                        if (((long) count) <= len / 2) {
                            int a = 0;
                            while (a < count) {
                                int start = r1.filePartsStream.readInt();
                                int end = r1.filePartsStream.readInt();
                                z2 = alreadyStarted;
                                try {
                                    r1.notLoadedBytesRanges.add(new Range(start, end));
                                    r1.notRequestedBytesRanges.add(new Range(start, end));
                                    a++;
                                    alreadyStarted = z2;
                                    i = streamOffset;
                                } catch (Throwable e322) {
                                    e = e322;
                                }
                            }
                        }
                    }
                } catch (Throwable e3222) {
                    z2 = alreadyStarted;
                    e = e3222;
                    FileLog.e(e);
                    if (r1.cacheFileTemp.exists()) {
                        r1.notLoadedBytesRanges.add(new Range(0, r1.totalBytesCount));
                        r1.notRequestedBytesRanges.add(new Range(0, r1.totalBytesCount));
                    } else if (newKeyGenerated) {
                        r1.cacheFileTemp.delete();
                    } else {
                        totalDownloadedLen = r1.cacheFileTemp.length();
                        if (fileNameIv != null) {
                        }
                        alreadyStarted = (((int) r1.cacheFileTemp.length()) / r1.currentDownloadChunkSize) * r1.currentDownloadChunkSize;
                        r1.downloadedBytes = alreadyStarted;
                        r1.requestedBytesCount = alreadyStarted;
                        r1.notLoadedBytesRanges.add(new Range(r1.downloadedBytes, r1.totalBytesCount));
                        r1.notRequestedBytesRanges.add(new Range(r1.downloadedBytes, r1.totalBytesCount));
                    }
                    if (r1.notLoadedBytesRanges != null) {
                        r1.downloadedBytes = r1.totalBytesCount;
                        size = r1.notLoadedBytesRanges.size();
                        for (i = 0; i < size; i++) {
                            alreadyStarted2 = (Range) r1.notLoadedBytesRanges.get(i);
                            r1.downloadedBytes -= alreadyStarted2.end - alreadyStarted2.start;
                        }
                    }
                    if (BuildVars.LOGS_ENABLED) {
                        stringBuilder3 = new StringBuilder();
                        stringBuilder3.append("start loading file to temp = ");
                        stringBuilder3.append(r1.cacheFileTemp);
                        stringBuilder3.append(" final = ");
                        stringBuilder3.append(r1.cacheFileFinal);
                        FileLog.d(stringBuilder3.toString());
                    }
                    if (fileNameIv != null) {
                        r1.cacheIvTemp = new File(r1.tempPath, fileNameIv);
                        try {
                            r1.fiv = new RandomAccessFile(r1.cacheIvTemp, "rws");
                            totalDownloadedLen = r1.cacheIvTemp.length();
                            if (totalDownloadedLen > 0) {
                            }
                            r1.downloadedBytes = 0;
                            r1.requestedBytesCount = 0;
                        } catch (Throwable e32222) {
                            FileLog.e(e32222);
                            r1.downloadedBytes = 0;
                            r1.requestedBytesCount = 0;
                        }
                    }
                    copytNotLoadedRanges();
                    r1.delegate.didChangedLoadProgress(r1, Math.min(1.0f, ((float) r1.downloadedBytes) / ((float) r1.totalBytesCount)));
                    r1.fileOutputStream = new RandomAccessFile(r1.cacheFileTemp, "rws");
                    if (r1.downloadedBytes != 0) {
                        r1.fileOutputStream.seek((long) r1.downloadedBytes);
                    }
                    if (r1.fileOutputStream != null) {
                        r1.started = true;
                        Utilities.stageQueue.postRunnable(new Runnable() {
                            public void run() {
                                if (FileLoadOperation.this.totalBytesCount == 0 || FileLoadOperation.this.downloadedBytes != FileLoadOperation.this.totalBytesCount) {
                                    FileLoadOperation.this.startDownloadRequest();
                                    return;
                                }
                                try {
                                    FileLoadOperation.this.onFinishLoadingFile(false);
                                } catch (Exception e) {
                                    FileLoadOperation.this.onFail(true, 0);
                                }
                            }
                        });
                        alreadyStarted = true;
                        return alreadyStarted;
                    }
                    onFail(true, 0);
                    return false;
                }
            }
            if (r1.cacheFileTemp.exists()) {
                if (r1.notLoadedBytesRanges != null && r1.notLoadedBytesRanges.isEmpty()) {
                    r1.notLoadedBytesRanges.add(new Range(0, r1.totalBytesCount));
                    r1.notRequestedBytesRanges.add(new Range(0, r1.totalBytesCount));
                }
            } else if (newKeyGenerated) {
                r1.cacheFileTemp.delete();
            } else {
                totalDownloadedLen = r1.cacheFileTemp.length();
                if (fileNameIv != null || totalDownloadedLen % ((long) r1.currentDownloadChunkSize) == 0) {
                    alreadyStarted = (((int) r1.cacheFileTemp.length()) / r1.currentDownloadChunkSize) * r1.currentDownloadChunkSize;
                    r1.downloadedBytes = alreadyStarted;
                    r1.requestedBytesCount = alreadyStarted;
                } else {
                    r1.downloadedBytes = 0;
                    r1.requestedBytesCount = 0;
                }
                if (r1.notLoadedBytesRanges && r1.notLoadedBytesRanges.isEmpty()) {
                    r1.notLoadedBytesRanges.add(new Range(r1.downloadedBytes, r1.totalBytesCount));
                    r1.notRequestedBytesRanges.add(new Range(r1.downloadedBytes, r1.totalBytesCount));
                }
            }
            if (r1.notLoadedBytesRanges != null) {
                r1.downloadedBytes = r1.totalBytesCount;
                size = r1.notLoadedBytesRanges.size();
                for (i = 0; i < size; i++) {
                    alreadyStarted2 = (Range) r1.notLoadedBytesRanges.get(i);
                    r1.downloadedBytes -= alreadyStarted2.end - alreadyStarted2.start;
                }
            }
            if (BuildVars.LOGS_ENABLED) {
                stringBuilder3 = new StringBuilder();
                stringBuilder3.append("start loading file to temp = ");
                stringBuilder3.append(r1.cacheFileTemp);
                stringBuilder3.append(" final = ");
                stringBuilder3.append(r1.cacheFileFinal);
                FileLog.d(stringBuilder3.toString());
            }
            if (fileNameIv != null) {
                r1.cacheIvTemp = new File(r1.tempPath, fileNameIv);
                r1.fiv = new RandomAccessFile(r1.cacheIvTemp, "rws");
                if (!(r1.downloadedBytes == 0 || newKeyGenerated)) {
                    totalDownloadedLen = r1.cacheIvTemp.length();
                    if (totalDownloadedLen > 0 || totalDownloadedLen % 32 != 0) {
                        r1.downloadedBytes = 0;
                        r1.requestedBytesCount = 0;
                    } else {
                        r1.fiv.read(r1.iv, 0, 32);
                    }
                }
            }
            if (r1.downloadedBytes != 0 && r1.totalBytesCount > 0) {
                copytNotLoadedRanges();
                r1.delegate.didChangedLoadProgress(r1, Math.min(1.0f, ((float) r1.downloadedBytes) / ((float) r1.totalBytesCount)));
            }
            try {
                r1.fileOutputStream = new RandomAccessFile(r1.cacheFileTemp, "rws");
                if (r1.downloadedBytes != 0) {
                    r1.fileOutputStream.seek((long) r1.downloadedBytes);
                }
            } catch (Throwable e322222) {
                FileLog.e(e322222);
            }
            if (r1.fileOutputStream != null) {
                onFail(true, 0);
                return false;
            }
            r1.started = true;
            Utilities.stageQueue.postRunnable(/* anonymous class already generated */);
            alreadyStarted = true;
        }
        return alreadyStarted;
    }

    public boolean isPaused() {
        return this.paused;
    }

    public void cancel() {
        Utilities.stageQueue.postRunnable(new Runnable() {
            public void run() {
                if (FileLoadOperation.this.state != 3) {
                    if (FileLoadOperation.this.state != 2) {
                        if (FileLoadOperation.this.requestInfos != null) {
                            for (int a = 0; a < FileLoadOperation.this.requestInfos.size(); a++) {
                                RequestInfo requestInfo = (RequestInfo) FileLoadOperation.this.requestInfos.get(a);
                                if (requestInfo.requestToken != 0) {
                                    ConnectionsManager.getInstance(FileLoadOperation.this.currentAccount).cancelRequest(requestInfo.requestToken, true);
                                }
                            }
                        }
                        FileLoadOperation.this.onFail(false, 1);
                    }
                }
            }
        });
    }

    private void cleanup() {
        try {
            if (this.fileOutputStream != null) {
                try {
                    this.fileOutputStream.getChannel().close();
                } catch (Throwable e) {
                    FileLog.e(e);
                }
                this.fileOutputStream.close();
                this.fileOutputStream = null;
            }
        } catch (Throwable e2) {
            FileLog.e(e2);
        }
        try {
            if (this.fileReadStream != null) {
                try {
                    this.fileReadStream.getChannel().close();
                } catch (Throwable e22) {
                    FileLog.e(e22);
                }
                this.fileReadStream.close();
                this.fileReadStream = null;
            }
        } catch (Throwable e222) {
            FileLog.e(e222);
        }
        try {
            if (this.filePartsStream != null) {
                try {
                    this.filePartsStream.getChannel().close();
                } catch (Throwable e2222) {
                    FileLog.e(e2222);
                }
                this.filePartsStream.close();
                this.filePartsStream = null;
            }
        } catch (Throwable e22222) {
            FileLog.e(e22222);
        }
        try {
            if (this.fiv != null) {
                this.fiv.close();
                this.fiv = null;
            }
        } catch (Throwable e3) {
            FileLog.e(e3);
        }
        if (this.delayedRequestInfos != null) {
            for (int a = 0; a < this.delayedRequestInfos.size(); a++) {
                RequestInfo requestInfo = (RequestInfo) this.delayedRequestInfos.get(a);
                if (requestInfo.response != null) {
                    requestInfo.response.disableFree = false;
                    requestInfo.response.freeResources();
                } else if (requestInfo.responseWeb != null) {
                    requestInfo.responseWeb.disableFree = false;
                    requestInfo.responseWeb.freeResources();
                } else if (requestInfo.responseCdn != null) {
                    requestInfo.responseCdn.disableFree = false;
                    requestInfo.responseCdn.freeResources();
                }
            }
            this.delayedRequestInfos.clear();
        }
    }

    private void onFinishLoadingFile(final boolean increment) throws Exception {
        if (this.state == 1) {
            this.state = 3;
            cleanup();
            if (this.cacheIvTemp != null) {
                this.cacheIvTemp.delete();
                this.cacheIvTemp = null;
            }
            if (this.cacheFileParts != null) {
                this.cacheFileParts.delete();
                this.cacheFileParts = null;
            }
            if (!(this.cacheFileTemp == null || this.cacheFileTemp.renameTo(this.cacheFileFinal))) {
                if (BuildVars.LOGS_ENABLED) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("unable to rename temp = ");
                    stringBuilder.append(this.cacheFileTemp);
                    stringBuilder.append(" to final = ");
                    stringBuilder.append(this.cacheFileFinal);
                    stringBuilder.append(" retry = ");
                    stringBuilder.append(this.renameRetryCount);
                    FileLog.e(stringBuilder.toString());
                }
                this.renameRetryCount++;
                if (this.renameRetryCount < 3) {
                    this.state = 1;
                    Utilities.stageQueue.postRunnable(new Runnable() {
                        public void run() {
                            try {
                                FileLoadOperation.this.onFinishLoadingFile(increment);
                            } catch (Exception e) {
                                FileLoadOperation.this.onFail(false, 0);
                            }
                        }
                    }, 200);
                    return;
                }
                this.cacheFileFinal = this.cacheFileTemp;
            }
            if (BuildVars.LOGS_ENABLED) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("finished downloading file to ");
                stringBuilder2.append(this.cacheFileFinal);
                FileLog.d(stringBuilder2.toString());
            }
            this.delegate.didFinishLoadingFile(this, this.cacheFileFinal);
            if (increment) {
                if (this.currentType == ConnectionsManager.FileTypeAudio) {
                    StatsController.getInstance(this.currentAccount).incrementReceivedItemsCount(ConnectionsManager.getCurrentNetworkType(), 3, 1);
                } else if (this.currentType == ConnectionsManager.FileTypeVideo) {
                    StatsController.getInstance(this.currentAccount).incrementReceivedItemsCount(ConnectionsManager.getCurrentNetworkType(), 2, 1);
                } else if (this.currentType == 16777216) {
                    StatsController.getInstance(this.currentAccount).incrementReceivedItemsCount(ConnectionsManager.getCurrentNetworkType(), 4, 1);
                } else if (this.currentType == ConnectionsManager.FileTypeFile) {
                    StatsController.getInstance(this.currentAccount).incrementReceivedItemsCount(ConnectionsManager.getCurrentNetworkType(), 5, 1);
                }
            }
        }
    }

    private void delayRequestInfo(RequestInfo requestInfo) {
        this.delayedRequestInfos.add(requestInfo);
        if (requestInfo.response != null) {
            requestInfo.response.disableFree = true;
        } else if (requestInfo.responseWeb != null) {
            requestInfo.responseWeb.disableFree = true;
        } else if (requestInfo.responseCdn != null) {
            requestInfo.responseCdn.disableFree = true;
        }
    }

    private void requestFileOffsets(int offset) {
        if (!this.requestingCdnOffsets) {
            this.requestingCdnOffsets = true;
            TLObject req = new TL_upload_getCdnFileHashes();
            req.file_token = this.cdnToken;
            req.offset = offset;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                public void run(TLObject response, TL_error error) {
                    if (error != null) {
                        FileLoadOperation.this.onFail(false, 0);
                        return;
                    }
                    int a;
                    FileLoadOperation.this.requestingCdnOffsets = false;
                    Vector vector = (Vector) response;
                    if (!vector.objects.isEmpty()) {
                        if (FileLoadOperation.this.cdnHashes == null) {
                            FileLoadOperation.this.cdnHashes = new SparseArray();
                        }
                        for (a = 0; a < vector.objects.size(); a++) {
                            TL_fileHash hash = (TL_fileHash) vector.objects.get(a);
                            FileLoadOperation.this.cdnHashes.put(hash.offset, hash);
                        }
                    }
                    a = 0;
                    while (a < FileLoadOperation.this.delayedRequestInfos.size()) {
                        RequestInfo delayedRequestInfo = (RequestInfo) FileLoadOperation.this.delayedRequestInfos.get(a);
                        if (FileLoadOperation.this.notLoadedBytesRanges == null) {
                            if (FileLoadOperation.this.downloadedBytes != delayedRequestInfo.offset) {
                                a++;
                            }
                        }
                        FileLoadOperation.this.delayedRequestInfos.remove(a);
                        if (!FileLoadOperation.this.processRequestResult(delayedRequestInfo, null)) {
                            if (delayedRequestInfo.response != null) {
                                delayedRequestInfo.response.disableFree = false;
                                delayedRequestInfo.response.freeResources();
                                return;
                            } else if (delayedRequestInfo.responseWeb != null) {
                                delayedRequestInfo.responseWeb.disableFree = false;
                                delayedRequestInfo.responseWeb.freeResources();
                                return;
                            } else if (delayedRequestInfo.responseCdn != null) {
                                delayedRequestInfo.responseCdn.disableFree = false;
                                delayedRequestInfo.responseCdn.freeResources();
                                return;
                            } else {
                                return;
                            }
                        }
                        return;
                    }
                }
            }, null, null, 0, this.datacenter_id, 1, true);
        }
    }

    private void onFail(boolean thread, final int reason) {
        cleanup();
        this.state = 2;
        if (thread) {
            Utilities.stageQueue.postRunnable(new Runnable() {
                public void run() {
                    FileLoadOperation.this.delegate.didFailedLoadingFile(FileLoadOperation.this, reason);
                }
            });
        } else {
            this.delegate.didFailedLoadingFile(this, reason);
        }
    }

    private void clearOperaion(RequestInfo currentInfo) {
        int a;
        int minOffset = ConnectionsManager.DEFAULT_DATACENTER_ID;
        for (a = 0; a < this.requestInfos.size(); a++) {
            RequestInfo info = (RequestInfo) this.requestInfos.get(a);
            minOffset = Math.min(info.offset, minOffset);
            removePart(this.notRequestedBytesRanges, info.offset, info.offset + this.currentDownloadChunkSize);
            if (currentInfo != info) {
                if (info.requestToken != 0) {
                    ConnectionsManager.getInstance(this.currentAccount).cancelRequest(info.requestToken, true);
                }
            }
        }
        this.requestInfos.clear();
        for (a = 0; a < this.delayedRequestInfos.size(); a++) {
            info = (RequestInfo) this.delayedRequestInfos.get(a);
            removePart(this.notRequestedBytesRanges, info.offset, info.offset + this.currentDownloadChunkSize);
            if (info.response != null) {
                info.response.disableFree = false;
                info.response.freeResources();
            } else if (info.responseWeb != null) {
                info.responseWeb.disableFree = false;
                info.responseWeb.freeResources();
            } else if (info.responseCdn != null) {
                info.responseCdn.disableFree = false;
                info.responseCdn.freeResources();
            }
            minOffset = Math.min(info.offset, minOffset);
        }
        this.delayedRequestInfos.clear();
        this.requestsCount = 0;
        if (this.notLoadedBytesRanges == null) {
            this.downloadedBytes = minOffset;
            this.requestedBytesCount = minOffset;
        }
    }

    private void startDownloadRequest() {
        if (!this.paused && r0.state == 1) {
            if (r0.requestInfos.size() + r0.delayedRequestInfos.size() < r0.currentMaxDownloadRequests) {
                int count = 1;
                boolean z = false;
                if (r0.totalBytesCount > 0) {
                    count = Math.max(0, r0.currentMaxDownloadRequests - r0.requestInfos.size());
                }
                int a = 0;
                while (a < count) {
                    int size;
                    int b;
                    if (r0.notRequestedBytesRanges != null) {
                        size = r0.notRequestedBytesRanges.size();
                        int minStreamStart = ConnectionsManager.DEFAULT_DATACENTER_ID;
                        int minStart = ConnectionsManager.DEFAULT_DATACENTER_ID;
                        for (b = z; b < size; b++) {
                            Range range = (Range) r0.notRequestedBytesRanges.get(b);
                            if (r0.streamStartOffset != 0) {
                                if (range.start <= r0.streamStartOffset && range.end > r0.streamStartOffset) {
                                    minStreamStart = r0.streamStartOffset;
                                    minStart = ConnectionsManager.DEFAULT_DATACENTER_ID;
                                    break;
                                } else if (r0.streamStartOffset < range.start && range.start < minStreamStart) {
                                    minStreamStart = range.start;
                                }
                            }
                            minStart = Math.min(minStart, range.start);
                        }
                        if (minStreamStart == ConnectionsManager.DEFAULT_DATACENTER_ID) {
                            if (minStart == ConnectionsManager.DEFAULT_DATACENTER_ID) {
                                break;
                            }
                            b = minStart;
                        } else {
                            b = minStreamStart;
                        }
                        size = b;
                    } else {
                        size = r0.requestedBytesCount;
                    }
                    if (r0.notRequestedBytesRanges != null) {
                        addPart(r0.notRequestedBytesRanges, size, r0.currentDownloadChunkSize + size, z);
                    }
                    if (r0.totalBytesCount > 0 && size >= r0.totalBytesCount) {
                        break;
                    }
                    boolean isLast;
                    int connectionType;
                    TLObject req;
                    TLObject request;
                    final RequestInfo requestInfo;
                    if (r0.totalBytesCount > 0 && a != count - 1) {
                        if (r0.totalBytesCount <= 0 || r0.currentDownloadChunkSize + size < r0.totalBytesCount) {
                            isLast = z;
                            connectionType = r0.requestsCount % 2 != 0 ? 2 : ConnectionsManager.ConnectionTypeDownload2;
                            b = (r0.isForceRequest ? 32 : z) | 2;
                            if (r0.isCdn) {
                                req = new TL_upload_getCdnFile();
                                req.file_token = r0.cdnToken;
                                req.offset = size;
                                req.limit = r0.currentDownloadChunkSize;
                                request = req;
                                b |= 1;
                            } else if (r0.webLocation == null) {
                                req = new TL_upload_getWebFile();
                                req.location = r0.webLocation;
                                req.offset = size;
                                req.limit = r0.currentDownloadChunkSize;
                                request = req;
                            } else {
                                request = new TL_upload_getFile();
                                request.location = r0.location;
                                request.offset = size;
                                request.limit = r0.currentDownloadChunkSize;
                            }
                            req = request;
                            r0.requestedBytesCount += r0.currentDownloadChunkSize;
                            requestInfo = new RequestInfo();
                            r0.requestInfos.add(requestInfo);
                            requestInfo.offset = size;
                            requestInfo.requestToken = ConnectionsManager.getInstance(r0.currentAccount).sendRequest(req, new RequestDelegate() {
                                public void run(TLObject response, TL_error error) {
                                    if (!FileLoadOperation.this.requestInfos.contains(requestInfo)) {
                                        return;
                                    }
                                    if (error != null && (req instanceof TL_upload_getCdnFile) && error.text.equals("FILE_TOKEN_INVALID")) {
                                        FileLoadOperation.this.isCdn = false;
                                        FileLoadOperation.this.clearOperaion(requestInfo);
                                        FileLoadOperation.this.startDownloadRequest();
                                        return;
                                    }
                                    if (response instanceof TL_upload_fileCdnRedirect) {
                                        TL_upload_fileCdnRedirect res = (TL_upload_fileCdnRedirect) response;
                                        if (!res.file_hashes.isEmpty()) {
                                            if (FileLoadOperation.this.cdnHashes == null) {
                                                FileLoadOperation.this.cdnHashes = new SparseArray();
                                            }
                                            for (int a = 0; a < res.file_hashes.size(); a++) {
                                                TL_fileHash hash = (TL_fileHash) res.file_hashes.get(a);
                                                FileLoadOperation.this.cdnHashes.put(hash.offset, hash);
                                            }
                                        }
                                        if (!(res.encryption_iv == null || res.encryption_key == null || res.encryption_iv.length != 16)) {
                                            if (res.encryption_key.length == 32) {
                                                FileLoadOperation.this.isCdn = true;
                                                if (FileLoadOperation.this.notCheckedCdnRanges == null) {
                                                    FileLoadOperation.this.notCheckedCdnRanges = new ArrayList();
                                                    FileLoadOperation.this.notCheckedCdnRanges.add(new Range(0, FileLoadOperation.maxCdnParts));
                                                }
                                                FileLoadOperation.this.cdnDatacenterId = res.dc_id;
                                                FileLoadOperation.this.cdnIv = res.encryption_iv;
                                                FileLoadOperation.this.cdnKey = res.encryption_key;
                                                FileLoadOperation.this.cdnToken = res.file_token;
                                                FileLoadOperation.this.clearOperaion(requestInfo);
                                                FileLoadOperation.this.startDownloadRequest();
                                            }
                                        }
                                        error = new TL_error();
                                        error.text = "bad redirect response";
                                        error.code = 400;
                                        FileLoadOperation.this.processRequestResult(requestInfo, error);
                                    } else if (!(response instanceof TL_upload_cdnFileReuploadNeeded)) {
                                        if (response instanceof TL_upload_file) {
                                            requestInfo.response = (TL_upload_file) response;
                                        } else if (response instanceof TL_upload_webFile) {
                                            requestInfo.responseWeb = (TL_upload_webFile) response;
                                            if (FileLoadOperation.this.totalBytesCount == 0 && requestInfo.responseWeb.size != 0) {
                                                FileLoadOperation.this.totalBytesCount = requestInfo.responseWeb.size;
                                            }
                                        } else {
                                            requestInfo.responseCdn = (TL_upload_cdnFile) response;
                                        }
                                        if (response != null) {
                                            if (FileLoadOperation.this.currentType == ConnectionsManager.FileTypeAudio) {
                                                StatsController.getInstance(FileLoadOperation.this.currentAccount).incrementReceivedBytesCount(response.networkType, 3, (long) (response.getObjectSize() + 4));
                                            } else if (FileLoadOperation.this.currentType == ConnectionsManager.FileTypeVideo) {
                                                StatsController.getInstance(FileLoadOperation.this.currentAccount).incrementReceivedBytesCount(response.networkType, 2, (long) (response.getObjectSize() + 4));
                                            } else if (FileLoadOperation.this.currentType == 16777216) {
                                                StatsController.getInstance(FileLoadOperation.this.currentAccount).incrementReceivedBytesCount(response.networkType, 4, (long) (response.getObjectSize() + 4));
                                            } else if (FileLoadOperation.this.currentType == ConnectionsManager.FileTypeFile) {
                                                StatsController.getInstance(FileLoadOperation.this.currentAccount).incrementReceivedBytesCount(response.networkType, 5, (long) (response.getObjectSize() + 4));
                                            }
                                        }
                                        FileLoadOperation.this.processRequestResult(requestInfo, error);
                                    } else if (!FileLoadOperation.this.reuploadingCdn) {
                                        FileLoadOperation.this.clearOperaion(requestInfo);
                                        FileLoadOperation.this.reuploadingCdn = true;
                                        TL_upload_cdnFileReuploadNeeded res2 = (TL_upload_cdnFileReuploadNeeded) response;
                                        TLObject req = new TL_upload_reuploadCdnFile();
                                        req.file_token = FileLoadOperation.this.cdnToken;
                                        req.request_token = res2.request_token;
                                        ConnectionsManager.getInstance(FileLoadOperation.this.currentAccount).sendRequest(req, new RequestDelegate() {
                                            public void run(org.telegram.tgnet.TLObject r1, org.telegram.tgnet.TLRPC.TL_error r2) {
                                                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.FileLoadOperation.12.1.run(org.telegram.tgnet.TLObject, org.telegram.tgnet.TLRPC$TL_error):void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
                                                /*
                                                r0 = this;
                                                r0 = org.telegram.messenger.FileLoadOperation.AnonymousClass12.this;
                                                r0 = org.telegram.messenger.FileLoadOperation.this;
                                                r1 = 0;
                                                r0.reuploadingCdn = r1;
                                                if (r7 != 0) goto L_0x0054;
                                            L_0x000a:
                                                r0 = r6;
                                                r0 = (org.telegram.tgnet.TLRPC.Vector) r0;
                                                r2 = r0.objects;
                                                r2 = r2.isEmpty();
                                                if (r2 != 0) goto L_0x004c;
                                            L_0x0015:
                                                r2 = org.telegram.messenger.FileLoadOperation.AnonymousClass12.this;
                                                r2 = org.telegram.messenger.FileLoadOperation.this;
                                                r2 = r2.cdnHashes;
                                                if (r2 != 0) goto L_0x002b;
                                            L_0x001f:
                                                r2 = org.telegram.messenger.FileLoadOperation.AnonymousClass12.this;
                                                r2 = org.telegram.messenger.FileLoadOperation.this;
                                                r3 = new android.util.SparseArray;
                                                r3.<init>();
                                                r2.cdnHashes = r3;
                                                r2 = r0.objects;
                                                r2 = r2.size();
                                                if (r1 >= r2) goto L_0x004c;
                                                r2 = r0.objects;
                                                r2 = r2.get(r1);
                                                r2 = (org.telegram.tgnet.TLRPC.TL_fileHash) r2;
                                                r3 = org.telegram.messenger.FileLoadOperation.AnonymousClass12.this;
                                                r3 = org.telegram.messenger.FileLoadOperation.this;
                                                r3 = r3.cdnHashes;
                                                r4 = r2.offset;
                                                r3.put(r4, r2);
                                                r1 = r1 + 1;
                                                goto L_0x002c;
                                            L_0x004c:
                                                r1 = org.telegram.messenger.FileLoadOperation.AnonymousClass12.this;
                                                r1 = org.telegram.messenger.FileLoadOperation.this;
                                                r1.startDownloadRequest();
                                                goto L_0x008a;
                                            L_0x0054:
                                                r0 = r7.text;
                                                r2 = "FILE_TOKEN_INVALID";
                                                r0 = r0.equals(r2);
                                                if (r0 != 0) goto L_0x0071;
                                                r0 = r7.text;
                                                r2 = "REQUEST_TOKEN_INVALID";
                                                r0 = r0.equals(r2);
                                                if (r0 == 0) goto L_0x0069;
                                                goto L_0x0071;
                                                r0 = org.telegram.messenger.FileLoadOperation.AnonymousClass12.this;
                                                r0 = org.telegram.messenger.FileLoadOperation.this;
                                                r0.onFail(r1, r1);
                                                goto L_0x008a;
                                                r0 = org.telegram.messenger.FileLoadOperation.AnonymousClass12.this;
                                                r0 = org.telegram.messenger.FileLoadOperation.this;
                                                r0.isCdn = r1;
                                                r0 = org.telegram.messenger.FileLoadOperation.AnonymousClass12.this;
                                                r0 = org.telegram.messenger.FileLoadOperation.this;
                                                r1 = org.telegram.messenger.FileLoadOperation.AnonymousClass12.this;
                                                r1 = r14;
                                                r0.clearOperaion(r1);
                                                r0 = org.telegram.messenger.FileLoadOperation.AnonymousClass12.this;
                                                r0 = org.telegram.messenger.FileLoadOperation.this;
                                                r0.startDownloadRequest();
                                                return;
                                                */
                                                throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.FileLoadOperation.12.1.run(org.telegram.tgnet.TLObject, org.telegram.tgnet.TLRPC$TL_error):void");
                                            }
                                        }, null, null, 0, FileLoadOperation.this.datacenter_id, 1, true);
                                    }
                                }
                            }, null, null, b, r0.isCdn ? r0.cdnDatacenterId : r0.datacenter_id, connectionType, isLast);
                            r0.requestsCount++;
                            a++;
                            z = false;
                        }
                    }
                    isLast = true;
                    if (r0.requestsCount % 2 != 0) {
                    }
                    if (r0.isForceRequest) {
                    }
                    b = (r0.isForceRequest ? 32 : z) | 2;
                    if (r0.isCdn) {
                        req = new TL_upload_getCdnFile();
                        req.file_token = r0.cdnToken;
                        req.offset = size;
                        req.limit = r0.currentDownloadChunkSize;
                        request = req;
                        b |= 1;
                    } else if (r0.webLocation == null) {
                        request = new TL_upload_getFile();
                        request.location = r0.location;
                        request.offset = size;
                        request.limit = r0.currentDownloadChunkSize;
                    } else {
                        req = new TL_upload_getWebFile();
                        req.location = r0.webLocation;
                        req.offset = size;
                        req.limit = r0.currentDownloadChunkSize;
                        request = req;
                    }
                    req = request;
                    r0.requestedBytesCount += r0.currentDownloadChunkSize;
                    requestInfo = new RequestInfo();
                    r0.requestInfos.add(requestInfo);
                    requestInfo.offset = size;
                    if (r0.isCdn) {
                    }
                    requestInfo.requestToken = ConnectionsManager.getInstance(r0.currentAccount).sendRequest(req, /* anonymous class already generated */, null, null, b, r0.isCdn ? r0.cdnDatacenterId : r0.datacenter_id, connectionType, isLast);
                    r0.requestsCount++;
                    a++;
                    z = false;
                }
            }
        }
    }

    public void setDelegate(FileLoadOperationDelegate delegate) {
        this.delegate = delegate;
    }
}
