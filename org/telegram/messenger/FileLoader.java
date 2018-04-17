package org.telegram.messenger;

import android.text.TextUtils;
import android.util.SparseArray;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import org.telegram.messenger.FileLoadOperation.FileLoadOperationDelegate;
import org.telegram.messenger.FileUploadOperation.FileUploadOperationDelegate;
import org.telegram.messenger.exoplayer2.upstream.DataSource;
import org.telegram.messenger.exoplayer2.upstream.TransferListener;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.Document;
import org.telegram.tgnet.TLRPC.DocumentAttribute;
import org.telegram.tgnet.TLRPC.FileLocation;
import org.telegram.tgnet.TLRPC.InputEncryptedFile;
import org.telegram.tgnet.TLRPC.InputFile;
import org.telegram.tgnet.TLRPC.Message;
import org.telegram.tgnet.TLRPC.PhotoSize;
import org.telegram.tgnet.TLRPC.TL_documentAttributeFilename;
import org.telegram.tgnet.TLRPC.TL_messageMediaDocument;
import org.telegram.tgnet.TLRPC.TL_messageMediaInvoice;
import org.telegram.tgnet.TLRPC.TL_messageMediaPhoto;
import org.telegram.tgnet.TLRPC.TL_messageMediaWebPage;
import org.telegram.tgnet.TLRPC.TL_messageService;
import org.telegram.tgnet.TLRPC.TL_photoCachedSize;
import org.telegram.tgnet.TLRPC.TL_webDocument;
import org.telegram.tgnet.TLRPC.WebDocument;

public class FileLoader {
    private static volatile FileLoader[] Instance = new FileLoader[3];
    public static final int MEDIA_DIR_AUDIO = 1;
    public static final int MEDIA_DIR_CACHE = 4;
    public static final int MEDIA_DIR_DOCUMENT = 3;
    public static final int MEDIA_DIR_IMAGE = 0;
    public static final int MEDIA_DIR_VIDEO = 2;
    private static volatile DispatchQueue fileLoaderQueue = new DispatchQueue("fileUploadQueue");
    private static SparseArray<File> mediaDirs = null;
    private ArrayList<FileLoadOperation> activeFileLoadOperation = new ArrayList();
    private LinkedList<FileLoadOperation> audioLoadOperationQueue = new LinkedList();
    private int currentAccount;
    private int currentAudioLoadOperationsCount = 0;
    private int currentLoadOperationsCount = 0;
    private int currentPhotoLoadOperationsCount = 0;
    private int currentUploadOperationsCount = 0;
    private int currentUploadSmallOperationsCount = 0;
    private FileLoaderDelegate delegate = null;
    private ConcurrentHashMap<String, FileLoadOperation> loadOperationPaths = new ConcurrentHashMap();
    private ConcurrentHashMap<String, Boolean> loadOperationPathsUI = new ConcurrentHashMap(10, 1.0f, 2);
    private LinkedList<FileLoadOperation> loadOperationQueue = new LinkedList();
    private LinkedList<FileLoadOperation> photoLoadOperationQueue = new LinkedList();
    private ConcurrentHashMap<String, FileUploadOperation> uploadOperationPaths = new ConcurrentHashMap();
    private ConcurrentHashMap<String, FileUploadOperation> uploadOperationPathsEnc = new ConcurrentHashMap();
    private LinkedList<FileUploadOperation> uploadOperationQueue = new LinkedList();
    private HashMap<String, Long> uploadSizes = new HashMap();
    private LinkedList<FileUploadOperation> uploadSmallOperationQueue = new LinkedList();

    public interface FileLoaderDelegate {
        void fileDidFailedLoad(String str, int i);

        void fileDidFailedUpload(String str, boolean z);

        void fileDidLoaded(String str, File file, int i);

        void fileDidUploaded(String str, InputFile inputFile, InputEncryptedFile inputEncryptedFile, byte[] bArr, byte[] bArr2, long j);

        void fileLoadProgressChanged(String str, float f);

        void fileUploadProgressChanged(String str, float f, boolean z);
    }

    class AnonymousClass5 implements FileLoadOperationDelegate {
        final /* synthetic */ Document val$document;
        final /* synthetic */ String val$finalFileName;
        final /* synthetic */ int val$finalType;
        final /* synthetic */ FileLocation val$location;
        final /* synthetic */ TL_webDocument val$webDocument;

        AnonymousClass5(String str, int i, Document document, TL_webDocument tL_webDocument, FileLocation fileLocation) {
            this.val$finalFileName = str;
            this.val$finalType = i;
            this.val$document = document;
            this.val$webDocument = tL_webDocument;
            this.val$location = fileLocation;
        }

        public void didFinishLoadingFile(FileLoadOperation operation, File finalFile) {
            FileLoader.this.loadOperationPathsUI.remove(this.val$finalFileName);
            if (FileLoader.this.delegate != null) {
                FileLoader.this.delegate.fileDidLoaded(this.val$finalFileName, finalFile, this.val$finalType);
            }
            FileLoader.this.checkDownloadQueue(this.val$document, this.val$webDocument, this.val$location, this.val$finalFileName);
        }

        public void didFailedLoadingFile(FileLoadOperation operation, int reason) {
            FileLoader.this.loadOperationPathsUI.remove(this.val$finalFileName);
            FileLoader.this.checkDownloadQueue(this.val$document, this.val$webDocument, this.val$location, this.val$finalFileName);
            if (FileLoader.this.delegate != null) {
                FileLoader.this.delegate.fileDidFailedLoad(this.val$finalFileName, reason);
            }
        }

        public void didChangedLoadProgress(FileLoadOperation operation, float progress) {
            if (FileLoader.this.delegate != null) {
                FileLoader.this.delegate.fileLoadProgressChanged(this.val$finalFileName, progress);
            }
        }
    }

    public static java.lang.String getAttachFileName(org.telegram.tgnet.TLObject r1, java.lang.String r2) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.FileLoader.getAttachFileName(org.telegram.tgnet.TLObject, java.lang.String):java.lang.String
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
        r0 = r7 instanceof org.telegram.tgnet.TLRPC.Document;
        if (r0 == 0) goto L_0x00ef;
    L_0x0004:
        r0 = r7;
        r0 = (org.telegram.tgnet.TLRPC.Document) r0;
        r1 = 0;
        r2 = -1;
        if (r1 != 0) goto L_0x0023;
    L_0x000b:
        r1 = getDocumentFileName(r0);
        if (r1 == 0) goto L_0x0021;
    L_0x0011:
        r3 = 46;
        r3 = r1.lastIndexOf(r3);
        r4 = r3;
        if (r3 != r2) goto L_0x001b;
    L_0x001a:
        goto L_0x0021;
        r1 = r1.substring(r4);
        goto L_0x0023;
    L_0x0021:
        r1 = "";
    L_0x0023:
        r3 = r1.length();
        r4 = 1;
        if (r3 > r4) goto L_0x0061;
        r3 = r0.mime_type;
        if (r3 == 0) goto L_0x005f;
        r3 = r0.mime_type;
        r5 = r3.hashCode();
        r6 = 187091926; // 0xb26cbd6 float:3.2123786E-32 double:9.24356933E-316;
        if (r5 == r6) goto L_0x0049;
        r6 = 1331848029; // 0x4f62635d float:3.79816269E9 double:6.580203566E-315;
        if (r5 == r6) goto L_0x003f;
        goto L_0x0052;
        r5 = "video/mp4";
        r3 = r3.equals(r5);
        if (r3 == 0) goto L_0x0052;
        r2 = 0;
        goto L_0x0052;
        r5 = "audio/ogg";
        r3 = r3.equals(r5);
        if (r3 == 0) goto L_0x0052;
        r2 = r4;
        switch(r2) {
            case 0: goto L_0x005b;
            case 1: goto L_0x0058;
            default: goto L_0x0055;
        };
        r1 = "";
        goto L_0x005e;
        r1 = ".ogg";
        goto L_0x005e;
        r1 = ".mp4";
        goto L_0x0061;
        r1 = "";
        r2 = r0.version;
        if (r2 != 0) goto L_0x00a0;
        r2 = r1.length();
        if (r2 <= r4) goto L_0x0087;
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = r0.dc_id;
        r2.append(r3);
        r3 = "_";
        r2.append(r3);
        r3 = r0.id;
        r2.append(r3);
        r2.append(r1);
        r2 = r2.toString();
        return r2;
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = r0.dc_id;
        r2.append(r3);
        r3 = "_";
        r2.append(r3);
        r3 = r0.id;
        r2.append(r3);
        r2 = r2.toString();
        return r2;
        r2 = r1.length();
        if (r2 <= r4) goto L_0x00cc;
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = r0.dc_id;
        r2.append(r3);
        r3 = "_";
        r2.append(r3);
        r3 = r0.id;
        r2.append(r3);
        r3 = "_";
        r2.append(r3);
        r3 = r0.version;
        r2.append(r3);
        r2.append(r1);
        r2 = r2.toString();
        return r2;
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = r0.dc_id;
        r2.append(r3);
        r3 = "_";
        r2.append(r3);
        r3 = r0.id;
        r2.append(r3);
        r3 = "_";
        r2.append(r3);
        r3 = r0.version;
        r2.append(r3);
        r2 = r2.toString();
        return r2;
    L_0x00ef:
        r0 = r7 instanceof org.telegram.tgnet.TLRPC.TL_webDocument;
        if (r0 == 0) goto L_0x011d;
        r0 = r7;
        r0 = (org.telegram.tgnet.TLRPC.TL_webDocument) r0;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = r0.url;
        r2 = org.telegram.messenger.Utilities.MD5(r2);
        r1.append(r2);
        r2 = ".";
        r1.append(r2);
        r2 = r0.url;
        r3 = r0.mime_type;
        r3 = getExtensionByMime(r3);
        r2 = org.telegram.messenger.ImageLoader.getHttpUrlExtension(r2, r3);
        r1.append(r2);
        r1 = r1.toString();
        return r1;
        r0 = r7 instanceof org.telegram.tgnet.TLRPC.PhotoSize;
        if (r0 == 0) goto L_0x015d;
        r0 = r7;
        r0 = (org.telegram.tgnet.TLRPC.PhotoSize) r0;
        r1 = r0.location;
        if (r1 == 0) goto L_0x015a;
        r1 = r0.location;
        r1 = r1 instanceof org.telegram.tgnet.TLRPC.TL_fileLocationUnavailable;
        if (r1 == 0) goto L_0x012f;
        goto L_0x015a;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = r0.location;
        r2 = r2.volume_id;
        r1.append(r2);
        r2 = "_";
        r1.append(r2);
        r2 = r0.location;
        r2 = r2.local_id;
        r1.append(r2);
        r2 = ".";
        r1.append(r2);
        if (r8 == 0) goto L_0x0150;
        r2 = r8;
        goto L_0x0152;
        r2 = "jpg";
        r1.append(r2);
        r1 = r1.toString();
        return r1;
        r1 = "";
        return r1;
        r0 = r7 instanceof org.telegram.tgnet.TLRPC.FileLocation;
        if (r0 == 0) goto L_0x0192;
        r0 = r7 instanceof org.telegram.tgnet.TLRPC.TL_fileLocationUnavailable;
        if (r0 == 0) goto L_0x0168;
        r0 = "";
        return r0;
        r0 = r7;
        r0 = (org.telegram.tgnet.TLRPC.FileLocation) r0;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = r0.volume_id;
        r1.append(r2);
        r2 = "_";
        r1.append(r2);
        r2 = r0.local_id;
        r1.append(r2);
        r2 = ".";
        r1.append(r2);
        if (r8 == 0) goto L_0x0188;
        r2 = r8;
        goto L_0x018a;
        r2 = "jpg";
        r1.append(r2);
        r1 = r1.toString();
        return r1;
        r0 = "";
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.FileLoader.getAttachFileName(org.telegram.tgnet.TLObject, java.lang.String):java.lang.String");
    }

    private org.telegram.messenger.FileLoadOperation loadFileInternal(org.telegram.tgnet.TLRPC.Document r1, org.telegram.tgnet.TLRPC.TL_webDocument r2, org.telegram.tgnet.TLRPC.FileLocation r3, java.lang.String r4, int r5, boolean r6, org.telegram.messenger.FileStreamLoadOperation r7, int r8, int r9) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.FileLoader.loadFileInternal(org.telegram.tgnet.TLRPC$Document, org.telegram.tgnet.TLRPC$TL_webDocument, org.telegram.tgnet.TLRPC$FileLocation, java.lang.String, int, boolean, org.telegram.messenger.FileStreamLoadOperation, int, int):org.telegram.messenger.FileLoadOperation
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
        r7 = r20;
        r8 = r21;
        r9 = r22;
        r10 = r23;
        r11 = r27;
        r12 = r28;
        r13 = r29;
        r0 = 0;
        if (r10 == 0) goto L_0x0017;
    L_0x0011:
        r0 = getAttachFileName(r23, r24);
    L_0x0015:
        r14 = r0;
        goto L_0x0025;
    L_0x0017:
        if (r8 == 0) goto L_0x001e;
    L_0x0019:
        r0 = getAttachFileName(r21);
        goto L_0x0015;
    L_0x001e:
        if (r9 == 0) goto L_0x0015;
    L_0x0020:
        r0 = getAttachFileName(r22);
        goto L_0x0015;
    L_0x0025:
        if (r14 == 0) goto L_0x0200;
    L_0x0027:
        r0 = "-2147483648";
        r0 = r14.contains(r0);
        if (r0 == 0) goto L_0x0031;
    L_0x002f:
        goto L_0x0200;
    L_0x0031:
        r0 = android.text.TextUtils.isEmpty(r14);
        r6 = 1;
        if (r0 != 0) goto L_0x0049;
    L_0x0038:
        r0 = "-2147483648";
        r0 = r14.contains(r0);
        if (r0 != 0) goto L_0x0049;
    L_0x0040:
        r0 = r7.loadOperationPathsUI;
        r1 = java.lang.Boolean.valueOf(r6);
        r0.put(r14, r1);
    L_0x0049:
        r0 = r7.loadOperationPaths;
        r0 = r0.get(r14);
        r0 = (org.telegram.messenger.FileLoadOperation) r0;
        r5 = 0;
        if (r0 == 0) goto L_0x00e9;
    L_0x0054:
        if (r12 != 0) goto L_0x0058;
    L_0x0056:
        if (r26 == 0) goto L_0x00e8;
    L_0x0058:
        r0.setForceRequest(r6);
        r1 = org.telegram.messenger.MessageObject.isVoiceDocument(r21);
        if (r1 != 0) goto L_0x0077;
    L_0x0061:
        r1 = org.telegram.messenger.MessageObject.isVoiceWebDocument(r22);
        if (r1 == 0) goto L_0x0068;
    L_0x0067:
        goto L_0x0077;
    L_0x0068:
        if (r10 != 0) goto L_0x0074;
    L_0x006a:
        r1 = org.telegram.messenger.MessageObject.isImageWebDocument(r22);
        if (r1 == 0) goto L_0x0071;
    L_0x0070:
        goto L_0x0074;
    L_0x0071:
        r1 = r7.loadOperationQueue;
        goto L_0x0079;
    L_0x0074:
        r1 = r7.photoLoadOperationQueue;
        goto L_0x0079;
    L_0x0077:
        r1 = r7.audioLoadOperationQueue;
        if (r1 == 0) goto L_0x00e8;
        r2 = r1.indexOf(r0);
        if (r2 <= 0) goto L_0x00cf;
        r1.remove(r2);
        if (r12 == 0) goto L_0x00cb;
        r3 = r7.audioLoadOperationQueue;
        if (r1 != r3) goto L_0x0097;
        r3 = r0.start(r11, r12);
        if (r3 == 0) goto L_0x00e8;
        r3 = r7.currentAudioLoadOperationsCount;
        r3 = r3 + r6;
        r7.currentAudioLoadOperationsCount = r3;
        goto L_0x00e8;
        r3 = r7.photoLoadOperationQueue;
        if (r1 != r3) goto L_0x00a7;
        r3 = r0.start(r11, r12);
        if (r3 == 0) goto L_0x00e8;
        r3 = r7.currentPhotoLoadOperationsCount;
        r3 = r3 + r6;
        r7.currentPhotoLoadOperationsCount = r3;
        goto L_0x00e8;
        r3 = r0.start(r11, r12);
        if (r3 == 0) goto L_0x00b2;
        r3 = r7.currentLoadOperationsCount;
        r3 = r3 + r6;
        r7.currentLoadOperationsCount = r3;
        r3 = r0.wasStarted();
        if (r3 == 0) goto L_0x00e8;
        r3 = r7.activeFileLoadOperation;
        r3 = r3.contains(r0);
        if (r3 != 0) goto L_0x00e8;
        if (r11 == 0) goto L_0x00c5;
        r7.pauseCurrentFileLoadOperations(r0);
        r3 = r7.activeFileLoadOperation;
        r3.add(r0);
        goto L_0x00e8;
        r1.add(r5, r0);
        goto L_0x00e8;
        if (r11 == 0) goto L_0x00d4;
        r7.pauseCurrentFileLoadOperations(r0);
        r0.start(r11, r12);
        r3 = r7.loadOperationQueue;
        if (r1 != r3) goto L_0x00e8;
        r3 = r7.activeFileLoadOperation;
        r3 = r3.contains(r0);
        if (r3 != 0) goto L_0x00e8;
        r3 = r7.activeFileLoadOperation;
        r3.add(r0);
    L_0x00e8:
        return r0;
    L_0x00e9:
        r1 = 4;
        r4 = getDirectory(r1);
        r1 = r4;
        r2 = 4;
        if (r10 == 0) goto L_0x00ff;
        r3 = new org.telegram.messenger.FileLoadOperation;
        r6 = r24;
        r11 = r25;
        r3.<init>(r10, r6, r11);
        r0 = r3;
        r2 = 0;
        r0 = r2;
        goto L_0x0140;
        r6 = r24;
        r11 = r25;
        if (r8 == 0) goto L_0x011d;
        r3 = new org.telegram.messenger.FileLoadOperation;
        r3.<init>(r8);
        r0 = r3;
        r3 = org.telegram.messenger.MessageObject.isVoiceDocument(r21);
        if (r3 == 0) goto L_0x0113;
        r2 = 1;
        goto L_0x013e;
        r3 = org.telegram.messenger.MessageObject.isVideoDocument(r21);
        if (r3 == 0) goto L_0x011b;
        r2 = 2;
        goto L_0x013e;
        r2 = 3;
        goto L_0x013e;
        if (r9 == 0) goto L_0x013e;
        r3 = new org.telegram.messenger.FileLoadOperation;
        r3.<init>(r9);
        r0 = r3;
        r3 = org.telegram.messenger.MessageObject.isVoiceWebDocument(r22);
        if (r3 == 0) goto L_0x012d;
        r2 = 1;
        goto L_0x013e;
        r3 = org.telegram.messenger.MessageObject.isVideoWebDocument(r22);
        if (r3 == 0) goto L_0x0135;
        r2 = 2;
        goto L_0x013e;
        r3 = org.telegram.messenger.MessageObject.isImageWebDocument(r22);
        if (r3 == 0) goto L_0x013d;
        r2 = 0;
        goto L_0x013e;
        r2 = 3;
        r3 = r0;
        goto L_0x00fd;
        if (r13 != 0) goto L_0x0148;
        r1 = getDirectory(r0);
        r2 = 1;
        goto L_0x014f;
        r2 = 2;
        if (r13 != r2) goto L_0x0146;
        r2 = 1;
        r3.setEncryptFile(r2);
        r2 = r7.currentAccount;
        r3.setPaths(r2, r1, r4);
        r15 = 1;
        r2 = r14;
        r11 = r3;
        r3 = r0;
        r16 = new org.telegram.messenger.FileLoader$5;
        r13 = r0;
        r0 = r16;
        r17 = r1;
        r1 = r7;
        r18 = r4;
        r4 = r8;
        r8 = r5;
        r5 = r9;
        r8 = r15;
        r6 = r10;
        r0.<init>(r2, r3, r4, r5, r6);
        r11.setDelegate(r0);
        r1 = r7.loadOperationPaths;
        r1.put(r14, r11);
        if (r26 == 0) goto L_0x0176;
        r6 = 3;
        goto L_0x0177;
        r6 = r8;
        r1 = r6;
        if (r13 != r8) goto L_0x01a1;
        if (r12 != 0) goto L_0x0193;
        r4 = r7.currentAudioLoadOperationsCount;
        if (r4 >= r1) goto L_0x0181;
        goto L_0x0193;
        if (r26 == 0) goto L_0x018d;
        r4 = r7.audioLoadOperationQueue;
        r5 = 0;
        r4.add(r5, r11);
        r4 = r27;
        goto L_0x01ff;
        r4 = r7.audioLoadOperationQueue;
        r4.add(r11);
        goto L_0x0189;
        r4 = r27;
        r5 = r11.start(r4, r12);
        if (r5 == 0) goto L_0x01ff;
        r5 = r7.currentAudioLoadOperationsCount;
        r5 = r5 + r8;
        r7.currentAudioLoadOperationsCount = r5;
        goto L_0x01ff;
        r4 = r27;
        if (r10 != 0) goto L_0x01de;
        r5 = org.telegram.messenger.MessageObject.isImageWebDocument(r22);
        if (r5 == 0) goto L_0x01ac;
        goto L_0x01de;
        if (r12 != 0) goto L_0x01c2;
        r5 = r7.currentLoadOperationsCount;
        if (r5 >= r1) goto L_0x01b3;
        goto L_0x01c2;
        if (r26 == 0) goto L_0x01bc;
        r5 = r7.loadOperationQueue;
        r6 = 0;
        r5.add(r6, r11);
        goto L_0x01ff;
        r5 = r7.loadOperationQueue;
        r5.add(r11);
        goto L_0x01ff;
        r5 = r11.start(r4, r12);
        if (r5 == 0) goto L_0x01d2;
        r5 = r7.currentLoadOperationsCount;
        r5 = r5 + r8;
        r7.currentLoadOperationsCount = r5;
        r5 = r7.activeFileLoadOperation;
        r5.add(r11);
        r5 = r11.wasStarted();
        if (r5 == 0) goto L_0x01ff;
        if (r4 == 0) goto L_0x01ff;
        r7.pauseCurrentFileLoadOperations(r11);
        goto L_0x01ff;
        if (r12 != 0) goto L_0x01f4;
        r5 = r7.currentPhotoLoadOperationsCount;
        if (r5 >= r1) goto L_0x01e5;
        goto L_0x01f4;
        if (r26 == 0) goto L_0x01ee;
        r5 = r7.photoLoadOperationQueue;
        r6 = 0;
        r5.add(r6, r11);
        goto L_0x01ff;
        r5 = r7.photoLoadOperationQueue;
        r5.add(r11);
        goto L_0x01ff;
        r5 = r11.start(r4, r12);
        if (r5 == 0) goto L_0x01ff;
        r5 = r7.currentPhotoLoadOperationsCount;
        r5 = r5 + r8;
        r7.currentPhotoLoadOperationsCount = r5;
        return r11;
    L_0x0200:
        r4 = r11;
        r0 = 0;
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.FileLoader.loadFileInternal(org.telegram.tgnet.TLRPC$Document, org.telegram.tgnet.TLRPC$TL_webDocument, org.telegram.tgnet.TLRPC$FileLocation, java.lang.String, int, boolean, org.telegram.messenger.FileStreamLoadOperation, int, int):org.telegram.messenger.FileLoadOperation");
    }

    public static FileLoader getInstance(int num) {
        FileLoader localInstance = Instance[num];
        if (localInstance == null) {
            synchronized (FileLoader.class) {
                localInstance = Instance[num];
                if (localInstance == null) {
                    FileLoader[] fileLoaderArr = Instance;
                    FileLoader fileLoader = new FileLoader(num);
                    localInstance = fileLoader;
                    fileLoaderArr[num] = fileLoader;
                }
            }
        }
        return localInstance;
    }

    public FileLoader(int instance) {
        this.currentAccount = instance;
    }

    public static void setMediaDirs(SparseArray<File> dirs) {
        mediaDirs = dirs;
    }

    public static File checkDirectory(int type) {
        return (File) mediaDirs.get(type);
    }

    public static File getDirectory(int type) {
        File dir = (File) mediaDirs.get(type);
        if (dir == null && type != 4) {
            dir = (File) mediaDirs.get(4);
        }
        try {
            if (!dir.isDirectory()) {
                dir.mkdirs();
            }
        } catch (Exception e) {
        }
        return dir;
    }

    public void cancelUploadFile(final String location, final boolean enc) {
        fileLoaderQueue.postRunnable(new Runnable() {
            public void run() {
                FileUploadOperation operation;
                if (enc) {
                    operation = (FileUploadOperation) FileLoader.this.uploadOperationPathsEnc.get(location);
                } else {
                    operation = (FileUploadOperation) FileLoader.this.uploadOperationPaths.get(location);
                }
                FileLoader.this.uploadSizes.remove(location);
                if (operation != null) {
                    FileLoader.this.uploadOperationPathsEnc.remove(location);
                    FileLoader.this.uploadOperationQueue.remove(operation);
                    FileLoader.this.uploadSmallOperationQueue.remove(operation);
                    operation.cancel();
                }
            }
        });
    }

    public void checkUploadNewDataAvailable(String location, boolean encrypted, long newAvailableSize, long finalSize) {
        final boolean z = encrypted;
        final String str = location;
        final long j = newAvailableSize;
        final long j2 = finalSize;
        fileLoaderQueue.postRunnable(new Runnable() {
            public void run() {
                FileUploadOperation operation;
                if (z) {
                    operation = (FileUploadOperation) FileLoader.this.uploadOperationPathsEnc.get(str);
                } else {
                    operation = (FileUploadOperation) FileLoader.this.uploadOperationPaths.get(str);
                }
                if (operation != null) {
                    operation.checkNewDataAvailable(j, j2);
                } else if (j2 != 0) {
                    FileLoader.this.uploadSizes.put(str, Long.valueOf(j2));
                }
            }
        });
    }

    public void uploadFile(String location, boolean encrypted, boolean small, int type) {
        uploadFile(location, encrypted, small, 0, type);
    }

    public void uploadFile(String location, boolean encrypted, boolean small, int estimatedSize, int type) {
        if (location != null) {
            final boolean z = encrypted;
            final String str = location;
            final int i = estimatedSize;
            final int i2 = type;
            final boolean z2 = small;
            fileLoaderQueue.postRunnable(new Runnable() {
                public void run() {
                    if (z) {
                        if (FileLoader.this.uploadOperationPathsEnc.containsKey(str)) {
                            return;
                        }
                    } else if (FileLoader.this.uploadOperationPaths.containsKey(str)) {
                        return;
                    }
                    int esimated = i;
                    if (!(esimated == 0 || ((Long) FileLoader.this.uploadSizes.get(str)) == null)) {
                        esimated = 0;
                        FileLoader.this.uploadSizes.remove(str);
                    }
                    FileUploadOperation fileUploadOperation = new FileUploadOperation(FileLoader.this.currentAccount, str, z, esimated, i2);
                    if (z) {
                        FileLoader.this.uploadOperationPathsEnc.put(str, fileUploadOperation);
                    } else {
                        FileLoader.this.uploadOperationPaths.put(str, fileUploadOperation);
                    }
                    fileUploadOperation.setDelegate(new FileUploadOperationDelegate() {
                        public void didFinishUploadingFile(FileUploadOperation operation, InputFile inputFile, InputEncryptedFile inputEncryptedFile, byte[] key, byte[] iv) {
                            final InputFile inputFile2 = inputFile;
                            final InputEncryptedFile inputEncryptedFile2 = inputEncryptedFile;
                            final byte[] bArr = key;
                            final byte[] bArr2 = iv;
                            final FileUploadOperation fileUploadOperation = operation;
                            FileLoader.fileLoaderQueue.postRunnable(new Runnable() {
                                public void run() {
                                    if (z) {
                                        FileLoader.this.uploadOperationPathsEnc.remove(str);
                                    } else {
                                        FileLoader.this.uploadOperationPaths.remove(str);
                                    }
                                    FileUploadOperation operation;
                                    if (z2) {
                                        FileLoader.this.currentUploadSmallOperationsCount = FileLoader.this.currentUploadSmallOperationsCount - 1;
                                        if (FileLoader.this.currentUploadSmallOperationsCount < 1) {
                                            operation = (FileUploadOperation) FileLoader.this.uploadSmallOperationQueue.poll();
                                            if (operation != null) {
                                                FileLoader.this.currentUploadSmallOperationsCount = FileLoader.this.currentUploadSmallOperationsCount + 1;
                                                operation.start();
                                            }
                                        }
                                    } else {
                                        FileLoader.this.currentUploadOperationsCount = FileLoader.this.currentUploadOperationsCount - 1;
                                        if (FileLoader.this.currentUploadOperationsCount < 1) {
                                            operation = (FileUploadOperation) FileLoader.this.uploadOperationQueue.poll();
                                            if (operation != null) {
                                                FileLoader.this.currentUploadOperationsCount = FileLoader.this.currentUploadOperationsCount + 1;
                                                operation.start();
                                            }
                                        }
                                    }
                                    if (FileLoader.this.delegate != null) {
                                        FileLoader.this.delegate.fileDidUploaded(str, inputFile2, inputEncryptedFile2, bArr, bArr2, fileUploadOperation.getTotalFileSize());
                                    }
                                }
                            });
                        }

                        public void didFailedUploadingFile(FileUploadOperation operation) {
                            FileLoader.fileLoaderQueue.postRunnable(new Runnable() {
                                public void run() {
                                    if (z) {
                                        FileLoader.this.uploadOperationPathsEnc.remove(str);
                                    } else {
                                        FileLoader.this.uploadOperationPaths.remove(str);
                                    }
                                    if (FileLoader.this.delegate != null) {
                                        FileLoader.this.delegate.fileDidFailedUpload(str, z);
                                    }
                                    FileUploadOperation operation;
                                    if (z2) {
                                        FileLoader.this.currentUploadSmallOperationsCount = FileLoader.this.currentUploadSmallOperationsCount - 1;
                                        if (FileLoader.this.currentUploadSmallOperationsCount < 1) {
                                            operation = (FileUploadOperation) FileLoader.this.uploadSmallOperationQueue.poll();
                                            if (operation != null) {
                                                FileLoader.this.currentUploadSmallOperationsCount = FileLoader.this.currentUploadSmallOperationsCount + 1;
                                                operation.start();
                                            }
                                            return;
                                        }
                                        return;
                                    }
                                    FileLoader.this.currentUploadOperationsCount = FileLoader.this.currentUploadOperationsCount - 1;
                                    if (FileLoader.this.currentUploadOperationsCount < 1) {
                                        operation = (FileUploadOperation) FileLoader.this.uploadOperationQueue.poll();
                                        if (operation != null) {
                                            FileLoader.this.currentUploadOperationsCount = FileLoader.this.currentUploadOperationsCount + 1;
                                            operation.start();
                                        }
                                    }
                                }
                            });
                        }

                        public void didChangedUploadProgress(FileUploadOperation operation, float progress) {
                            if (FileLoader.this.delegate != null) {
                                FileLoader.this.delegate.fileUploadProgressChanged(str, progress, z);
                            }
                        }
                    });
                    if (z2) {
                        if (FileLoader.this.currentUploadSmallOperationsCount < 1) {
                            FileLoader.this.currentUploadSmallOperationsCount = FileLoader.this.currentUploadSmallOperationsCount + 1;
                            fileUploadOperation.start();
                        } else {
                            FileLoader.this.uploadSmallOperationQueue.add(fileUploadOperation);
                        }
                    } else if (FileLoader.this.currentUploadOperationsCount < 1) {
                        FileLoader.this.currentUploadOperationsCount = FileLoader.this.currentUploadOperationsCount + 1;
                        fileUploadOperation.start();
                    } else {
                        FileLoader.this.uploadOperationQueue.add(fileUploadOperation);
                    }
                }
            });
        }
    }

    public void cancelLoadFile(Document document) {
        cancelLoadFile(document, null, null, null);
    }

    public void cancelLoadFile(TL_webDocument document) {
        cancelLoadFile(null, document, null, null);
    }

    public void cancelLoadFile(PhotoSize photo) {
        cancelLoadFile(null, null, photo.location, null);
    }

    public void cancelLoadFile(FileLocation location, String ext) {
        cancelLoadFile(null, null, location, ext);
    }

    private void cancelLoadFile(Document document, TL_webDocument webDocument, FileLocation location, String locationExt) {
        if (location != null || document != null || webDocument != null) {
            String fileName;
            final String str;
            final Document document2;
            final TL_webDocument tL_webDocument;
            final FileLocation fileLocation;
            if (location != null) {
                fileName = getAttachFileName(location, locationExt);
            } else if (document != null) {
                fileName = getAttachFileName(document);
            } else if (webDocument != null) {
                fileName = getAttachFileName(webDocument);
            } else {
                fileName = null;
                if (fileName == null) {
                    this.loadOperationPathsUI.remove(fileName);
                    str = fileName;
                    document2 = document;
                    tL_webDocument = webDocument;
                    fileLocation = location;
                    fileLoaderQueue.postRunnable(new Runnable() {
                        public void run() {
                            FileLoadOperation operation = (FileLoadOperation) FileLoader.this.loadOperationPaths.remove(str);
                            if (operation != null) {
                                if (!MessageObject.isVoiceDocument(document2)) {
                                    if (!MessageObject.isVoiceWebDocument(tL_webDocument)) {
                                        if (fileLocation == null) {
                                            if (!MessageObject.isImageWebDocument(tL_webDocument)) {
                                                if (!FileLoader.this.loadOperationQueue.remove(operation)) {
                                                    FileLoader.this.currentLoadOperationsCount = FileLoader.this.currentLoadOperationsCount - 1;
                                                }
                                                FileLoader.this.activeFileLoadOperation.remove(operation);
                                                operation.cancel();
                                            }
                                        }
                                        if (!FileLoader.this.photoLoadOperationQueue.remove(operation)) {
                                            FileLoader.this.currentPhotoLoadOperationsCount = FileLoader.this.currentPhotoLoadOperationsCount - 1;
                                        }
                                        operation.cancel();
                                    }
                                }
                                if (!FileLoader.this.audioLoadOperationQueue.remove(operation)) {
                                    FileLoader.this.currentAudioLoadOperationsCount = FileLoader.this.currentAudioLoadOperationsCount - 1;
                                }
                                operation.cancel();
                            }
                        }
                    });
                }
            }
            if (fileName == null) {
                this.loadOperationPathsUI.remove(fileName);
                str = fileName;
                document2 = document;
                tL_webDocument = webDocument;
                fileLocation = location;
                fileLoaderQueue.postRunnable(/* anonymous class already generated */);
            }
        }
    }

    public boolean isLoadingFile(String fileName) {
        return this.loadOperationPathsUI.containsKey(fileName);
    }

    public float getBufferedProgressFromPosition(float position, String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return 0.0f;
        }
        FileLoadOperation loadOperation = (FileLoadOperation) this.loadOperationPaths.get(fileName);
        if (loadOperation != null) {
            return loadOperation.getDownloadedLengthFromOffset(position);
        }
        return 0.0f;
    }

    public void loadFile(PhotoSize photo, String ext, int cacheType) {
        if (photo != null) {
            if (cacheType == 0 && photo != null && (photo.size == 0 || photo.location.key != null)) {
                cacheType = 1;
            }
            loadFile(null, null, photo.location, ext, photo.size, false, cacheType);
        }
    }

    public void loadFile(Document document, boolean force, int cacheType) {
        if (document != null) {
            if (!(cacheType != 0 || document == null || document.key == null)) {
                cacheType = 1;
            }
            loadFile(document, null, null, null, 0, force, cacheType);
        }
    }

    public void loadFile(TL_webDocument document, boolean force, int cacheType) {
        loadFile(null, document, null, null, 0, force, cacheType);
    }

    public void loadFile(FileLocation location, String ext, int size, int cacheType) {
        if (location != null) {
            if (cacheType == 0 && (size == 0 || !(location == null || location.key == null))) {
                cacheType = 1;
            }
            loadFile(null, null, location, ext, size, true, cacheType);
        }
    }

    private void pauseCurrentFileLoadOperations(FileLoadOperation newOperation) {
        int a = 0;
        while (a < this.activeFileLoadOperation.size()) {
            FileLoadOperation operation = (FileLoadOperation) this.activeFileLoadOperation.get(a);
            if (operation != newOperation) {
                this.activeFileLoadOperation.remove(operation);
                a--;
                operation.pause();
                this.loadOperationQueue.add(0, operation);
                if (operation.wasStarted()) {
                    this.currentLoadOperationsCount--;
                }
            }
            a++;
        }
    }

    private void loadFile(Document document, TL_webDocument webDocument, FileLocation location, String locationExt, int locationSize, boolean force, int cacheType) {
        String fileName;
        String fileName2;
        FileLoader fileLoader;
        final Document document2;
        final TL_webDocument tL_webDocument;
        final FileLocation fileLocation;
        final String str;
        final int i;
        final boolean z;
        final int i2;
        if (location != null) {
            fileName = getAttachFileName(location, locationExt);
        } else if (document != null) {
            fileName = getAttachFileName(document);
        } else if (webDocument != null) {
            fileName = getAttachFileName(webDocument);
        } else {
            fileName = null;
            fileName2 = fileName;
            if (!TextUtils.isEmpty(fileName2) || fileName2.contains("-2147483648")) {
                fileLoader = this;
            } else {
                this.loadOperationPathsUI.put(fileName2, Boolean.valueOf(true));
            }
            document2 = document;
            tL_webDocument = webDocument;
            fileLocation = location;
            str = locationExt;
            i = locationSize;
            z = force;
            i2 = cacheType;
            fileLoaderQueue.postRunnable(new Runnable() {
                public void run() {
                    FileLoader.this.loadFileInternal(document2, tL_webDocument, fileLocation, str, i, z, null, 0, i2);
                }
            });
        }
        fileName2 = fileName;
        if (TextUtils.isEmpty(fileName2)) {
        }
        fileLoader = this;
        document2 = document;
        tL_webDocument = webDocument;
        fileLocation = location;
        str = locationExt;
        i = locationSize;
        z = force;
        i2 = cacheType;
        fileLoaderQueue.postRunnable(/* anonymous class already generated */);
    }

    protected FileLoadOperation loadStreamFile(FileStreamLoadOperation stream, Document document, int offset) {
        CountDownLatch semaphore = new CountDownLatch(1);
        FileLoadOperation[] result = new FileLoadOperation[1];
        final FileLoadOperation[] fileLoadOperationArr = result;
        final Document document2 = document;
        final FileStreamLoadOperation fileStreamLoadOperation = stream;
        final int i = offset;
        final CountDownLatch countDownLatch = semaphore;
        fileLoaderQueue.postRunnable(new Runnable() {
            public void run() {
                fileLoadOperationArr[0] = FileLoader.this.loadFileInternal(document2, null, null, null, 0, true, fileStreamLoadOperation, i, 0);
                countDownLatch.countDown();
            }
        });
        try {
            semaphore.await();
        } catch (Throwable e) {
            FileLog.e(e);
        }
        return result[0];
    }

    private void checkDownloadQueue(Document document, TL_webDocument webDocument, FileLocation location, String arg1) {
        final String str = arg1;
        final Document document2 = document;
        final TL_webDocument tL_webDocument = webDocument;
        final FileLocation fileLocation = location;
        fileLoaderQueue.postRunnable(new Runnable() {
            public void run() {
                FileLoadOperation operation = (FileLoadOperation) FileLoader.this.loadOperationPaths.remove(str);
                if (!MessageObject.isVoiceDocument(document2)) {
                    if (!MessageObject.isVoiceWebDocument(tL_webDocument)) {
                        if (fileLocation == null) {
                            if (!MessageObject.isImageWebDocument(tL_webDocument)) {
                                if (operation != null) {
                                    if (operation.wasStarted()) {
                                        FileLoader.this.currentLoadOperationsCount = FileLoader.this.currentLoadOperationsCount - 1;
                                    } else {
                                        FileLoader.this.loadOperationQueue.remove(operation);
                                    }
                                    FileLoader.this.activeFileLoadOperation.remove(operation);
                                }
                                while (!FileLoader.this.loadOperationQueue.isEmpty()) {
                                    if (FileLoader.this.currentLoadOperationsCount < (((FileLoadOperation) FileLoader.this.loadOperationQueue.get(0)).isForceRequest() ? 3 : 1)) {
                                        operation = (FileLoadOperation) FileLoader.this.loadOperationQueue.poll();
                                        if (operation != null && operation.start()) {
                                            FileLoader.this.currentLoadOperationsCount = FileLoader.this.currentLoadOperationsCount + 1;
                                            if (!FileLoader.this.activeFileLoadOperation.contains(operation)) {
                                                FileLoader.this.activeFileLoadOperation.add(operation);
                                            }
                                        }
                                    } else {
                                        return;
                                    }
                                }
                                return;
                            }
                        }
                        if (operation != null) {
                            if (operation.wasStarted()) {
                                FileLoader.this.currentPhotoLoadOperationsCount = FileLoader.this.currentPhotoLoadOperationsCount - 1;
                            } else {
                                FileLoader.this.photoLoadOperationQueue.remove(operation);
                            }
                        }
                        while (!FileLoader.this.photoLoadOperationQueue.isEmpty()) {
                            if (FileLoader.this.currentPhotoLoadOperationsCount < (((FileLoadOperation) FileLoader.this.photoLoadOperationQueue.get(0)).isForceRequest() ? 3 : 1)) {
                                operation = (FileLoadOperation) FileLoader.this.photoLoadOperationQueue.poll();
                                if (operation != null && operation.start()) {
                                    FileLoader.this.currentPhotoLoadOperationsCount = FileLoader.this.currentPhotoLoadOperationsCount + 1;
                                }
                            } else {
                                return;
                            }
                        }
                        return;
                    }
                }
                if (operation != null) {
                    if (operation.wasStarted()) {
                        FileLoader.this.currentAudioLoadOperationsCount = FileLoader.this.currentAudioLoadOperationsCount - 1;
                    } else {
                        FileLoader.this.audioLoadOperationQueue.remove(operation);
                    }
                }
                while (!FileLoader.this.audioLoadOperationQueue.isEmpty()) {
                    if (FileLoader.this.currentAudioLoadOperationsCount < (((FileLoadOperation) FileLoader.this.audioLoadOperationQueue.get(0)).isForceRequest() ? 3 : 1)) {
                        operation = (FileLoadOperation) FileLoader.this.audioLoadOperationQueue.poll();
                        if (operation != null && operation.start()) {
                            FileLoader.this.currentAudioLoadOperationsCount = FileLoader.this.currentAudioLoadOperationsCount + 1;
                        }
                    } else {
                        return;
                    }
                }
            }
        });
    }

    public void setDelegate(FileLoaderDelegate delegate) {
        this.delegate = delegate;
    }

    public static String getMessageFileName(Message message) {
        if (message == null) {
            return TtmlNode.ANONYMOUS_REGION_ID;
        }
        ArrayList<PhotoSize> sizes;
        PhotoSize sizeFull;
        if (message instanceof TL_messageService) {
            if (message.action.photo != null) {
                sizes = message.action.photo.sizes;
                if (sizes.size() > 0) {
                    sizeFull = getClosestPhotoSizeWithSize(sizes, AndroidUtilities.getPhotoSize());
                    if (sizeFull != null) {
                        return getAttachFileName(sizeFull);
                    }
                }
            }
        } else if (message.media instanceof TL_messageMediaDocument) {
            return getAttachFileName(message.media.document);
        } else {
            if (message.media instanceof TL_messageMediaPhoto) {
                sizes = message.media.photo.sizes;
                if (sizes.size() > 0) {
                    sizeFull = getClosestPhotoSizeWithSize(sizes, AndroidUtilities.getPhotoSize());
                    if (sizeFull != null) {
                        return getAttachFileName(sizeFull);
                    }
                }
            } else if (message.media instanceof TL_messageMediaWebPage) {
                if (message.media.webpage.document != null) {
                    return getAttachFileName(message.media.webpage.document);
                }
                if (message.media.webpage.photo != null) {
                    sizes = message.media.webpage.photo.sizes;
                    if (sizes.size() > 0) {
                        sizeFull = getClosestPhotoSizeWithSize(sizes, AndroidUtilities.getPhotoSize());
                        if (sizeFull != null) {
                            return getAttachFileName(sizeFull);
                        }
                    }
                } else if (message.media instanceof TL_messageMediaInvoice) {
                    return getAttachFileName(((TL_messageMediaInvoice) message.media).photo);
                }
            } else if (message.media instanceof TL_messageMediaInvoice) {
                WebDocument document = ((TL_messageMediaInvoice) message.media).photo;
                if (document != null) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(Utilities.MD5(document.url));
                    stringBuilder.append(".");
                    stringBuilder.append(ImageLoader.getHttpUrlExtension(document.url, getExtensionByMime(document.mime_type)));
                    return stringBuilder.toString();
                }
            }
        }
        return TtmlNode.ANONYMOUS_REGION_ID;
    }

    public static File getPathToMessage(Message message) {
        if (message == null) {
            return new File(TtmlNode.ANONYMOUS_REGION_ID);
        }
        ArrayList<PhotoSize> sizes;
        PhotoSize sizeFull;
        if (!(message instanceof TL_messageService)) {
            boolean z = false;
            if (message.media instanceof TL_messageMediaDocument) {
                TLObject tLObject = message.media.document;
                if (message.media.ttl_seconds != 0) {
                    z = true;
                }
                return getPathToAttach(tLObject, z);
            } else if (message.media instanceof TL_messageMediaPhoto) {
                sizes = message.media.photo.sizes;
                if (sizes.size() > 0) {
                    PhotoSize sizeFull2 = getClosestPhotoSizeWithSize(sizes, AndroidUtilities.getPhotoSize());
                    if (sizeFull2 != null) {
                        if (message.media.ttl_seconds != 0) {
                            z = true;
                        }
                        return getPathToAttach(sizeFull2, z);
                    }
                }
            } else if (message.media instanceof TL_messageMediaWebPage) {
                if (message.media.webpage.document != null) {
                    return getPathToAttach(message.media.webpage.document);
                }
                if (message.media.webpage.photo != null) {
                    sizes = message.media.webpage.photo.sizes;
                    if (sizes.size() > 0) {
                        sizeFull = getClosestPhotoSizeWithSize(sizes, AndroidUtilities.getPhotoSize());
                        if (sizeFull != null) {
                            return getPathToAttach(sizeFull);
                        }
                    }
                }
            } else if (message.media instanceof TL_messageMediaInvoice) {
                return getPathToAttach(((TL_messageMediaInvoice) message.media).photo, true);
            }
        } else if (message.action.photo != null) {
            sizes = message.action.photo.sizes;
            if (sizes.size() > 0) {
                sizeFull = getClosestPhotoSizeWithSize(sizes, AndroidUtilities.getPhotoSize());
                if (sizeFull != null) {
                    return getPathToAttach(sizeFull);
                }
            }
        }
        return new File(TtmlNode.ANONYMOUS_REGION_ID);
    }

    public static File getPathToAttach(TLObject attach) {
        return getPathToAttach(attach, null, false);
    }

    public static File getPathToAttach(TLObject attach, boolean forceCache) {
        return getPathToAttach(attach, null, forceCache);
    }

    public static File getPathToAttach(TLObject attach, String ext, boolean forceCache) {
        File dir = null;
        if (forceCache) {
            dir = getDirectory(4);
        } else if (attach instanceof Document) {
            Document document = (Document) attach;
            if (document.key != null) {
                dir = getDirectory(4);
            } else if (MessageObject.isVoiceDocument(document)) {
                dir = getDirectory(1);
            } else if (MessageObject.isVideoDocument(document)) {
                dir = getDirectory(2);
            } else {
                dir = getDirectory(3);
            }
        } else if (attach instanceof PhotoSize) {
            PhotoSize photoSize = (PhotoSize) attach;
            if (photoSize.location != null && photoSize.location.key == null && (photoSize.location.volume_id != -2147483648L || photoSize.location.local_id >= 0)) {
                if (photoSize.size >= 0) {
                    dir = getDirectory(0);
                }
            }
            dir = getDirectory(4);
        } else if (attach instanceof FileLocation) {
            FileLocation fileLocation = (FileLocation) attach;
            if (fileLocation.key == null) {
                if (fileLocation.volume_id != -2147483648L || fileLocation.local_id >= 0) {
                    dir = getDirectory(0);
                }
            }
            dir = getDirectory(4);
        } else if (attach instanceof TL_webDocument) {
            TL_webDocument document2 = (TL_webDocument) attach;
            if (document2.mime_type.startsWith("image/")) {
                dir = getDirectory(0);
            } else if (document2.mime_type.startsWith("audio/")) {
                dir = getDirectory(1);
            } else if (document2.mime_type.startsWith("video/")) {
                dir = getDirectory(2);
            } else {
                dir = getDirectory(3);
            }
        }
        if (dir == null) {
            return new File(TtmlNode.ANONYMOUS_REGION_ID);
        }
        return new File(dir, getAttachFileName(attach, ext));
    }

    public static FileStreamLoadOperation getStreamLoadOperation(TransferListener<? super DataSource> listener) {
        return new FileStreamLoadOperation(listener);
    }

    public static PhotoSize getClosestPhotoSizeWithSize(ArrayList<PhotoSize> sizes, int side) {
        return getClosestPhotoSizeWithSize(sizes, side, false);
    }

    public static PhotoSize getClosestPhotoSizeWithSize(ArrayList<PhotoSize> sizes, int side, boolean byMinSide) {
        if (sizes != null) {
            if (!sizes.isEmpty()) {
                int lastSide = 0;
                PhotoSize closestObject = null;
                for (int a = 0; a < sizes.size(); a++) {
                    PhotoSize obj = (PhotoSize) sizes.get(a);
                    if (obj != null) {
                        int currentSide;
                        if (byMinSide) {
                            currentSide = obj.h >= obj.w ? obj.w : obj.h;
                            if (closestObject == null || ((side > 100 && closestObject.location != null && closestObject.location.dc_id == Integer.MIN_VALUE) || (obj instanceof TL_photoCachedSize) || (side > lastSide && lastSide < currentSide))) {
                                closestObject = obj;
                                lastSide = currentSide;
                            }
                        } else {
                            currentSide = obj.w >= obj.h ? obj.w : obj.h;
                            if (closestObject == null || ((side > 100 && closestObject.location != null && closestObject.location.dc_id == Integer.MIN_VALUE) || (obj instanceof TL_photoCachedSize) || (currentSide <= side && lastSide < currentSide))) {
                                closestObject = obj;
                                lastSide = currentSide;
                            }
                        }
                    }
                }
                return closestObject;
            }
        }
        return null;
    }

    public static String getFileExtension(File file) {
        String name = file.getName();
        try {
            return name.substring(name.lastIndexOf(46) + 1);
        } catch (Exception e) {
            return TtmlNode.ANONYMOUS_REGION_ID;
        }
    }

    public static String fixFileName(String fileName) {
        if (fileName != null) {
            return fileName.replaceAll("[\u0001-\u001f<>:\"/\\\\|?*]+", TtmlNode.ANONYMOUS_REGION_ID).trim();
        }
        return fileName;
    }

    public static String getDocumentFileName(Document document) {
        String fileName = null;
        if (document != null) {
            if (document.file_name != null) {
                fileName = document.file_name;
            } else {
                for (int a = 0; a < document.attributes.size(); a++) {
                    DocumentAttribute documentAttribute = (DocumentAttribute) document.attributes.get(a);
                    if (documentAttribute instanceof TL_documentAttributeFilename) {
                        fileName = documentAttribute.file_name;
                    }
                }
            }
        }
        fileName = fixFileName(fileName);
        return fileName != null ? fileName : TtmlNode.ANONYMOUS_REGION_ID;
    }

    public static String getExtensionByMime(String mime) {
        int lastIndexOf = mime.lastIndexOf(47);
        int index = lastIndexOf;
        if (lastIndexOf != -1) {
            return mime.substring(index + 1);
        }
        return TtmlNode.ANONYMOUS_REGION_ID;
    }

    public static File getInternalCacheDir() {
        return ApplicationLoader.applicationContext.getCacheDir();
    }

    public static String getDocumentExtension(Document document) {
        String fileName = getDocumentFileName(document);
        int idx = fileName.lastIndexOf(46);
        String ext = null;
        if (idx != -1) {
            ext = fileName.substring(idx + 1);
        }
        if (ext == null || ext.length() == 0) {
            ext = document.mime_type;
        }
        if (ext == null) {
            ext = TtmlNode.ANONYMOUS_REGION_ID;
        }
        return ext.toUpperCase();
    }

    public static String getAttachFileName(TLObject attach) {
        return getAttachFileName(attach, null);
    }

    public void deleteFiles(final ArrayList<File> files, final int type) {
        if (files != null) {
            if (!files.isEmpty()) {
                fileLoaderQueue.postRunnable(new Runnable() {
                    public void run() {
                        for (int a = 0; a < files.size(); a++) {
                            File key;
                            File file = (File) files.get(a);
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append(file.getAbsolutePath());
                            stringBuilder.append(".enc");
                            File encrypted = new File(stringBuilder.toString());
                            if (encrypted.exists()) {
                                try {
                                    if (!encrypted.delete()) {
                                        encrypted.deleteOnExit();
                                    }
                                } catch (Throwable e) {
                                    FileLog.e(e);
                                }
                                try {
                                    File internalCacheDir = FileLoader.getInternalCacheDir();
                                    StringBuilder stringBuilder2 = new StringBuilder();
                                    stringBuilder2.append(file.getName());
                                    stringBuilder2.append(".enc.key");
                                    key = new File(internalCacheDir, stringBuilder2.toString());
                                    if (!key.delete()) {
                                        key.deleteOnExit();
                                    }
                                } catch (Throwable e2) {
                                    FileLog.e(e2);
                                }
                            } else if (file.exists()) {
                                try {
                                    if (!file.delete()) {
                                        file.deleteOnExit();
                                    }
                                } catch (Throwable e22) {
                                    FileLog.e(e22);
                                }
                            }
                            try {
                                internalCacheDir = file.getParentFile();
                                stringBuilder2 = new StringBuilder();
                                stringBuilder2.append("q_");
                                stringBuilder2.append(file.getName());
                                key = new File(internalCacheDir, stringBuilder2.toString());
                                if (key.exists() && !key.delete()) {
                                    key.deleteOnExit();
                                }
                            } catch (Throwable e222) {
                                FileLog.e(e222);
                            }
                        }
                        if (type == 2) {
                            ImageLoader.getInstance().clearMemory();
                        }
                    }
                });
            }
        }
    }
}
