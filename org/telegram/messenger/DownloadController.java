package org.telegram.messenger;

import android.util.LongSparseArray;
import android.util.SparseArray;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC.Document;
import org.telegram.tgnet.TLRPC.Message;
import org.telegram.tgnet.TLRPC.Peer;
import org.telegram.tgnet.TLRPC.PhotoSize;

public class DownloadController implements NotificationCenterDelegate {
    public static final int AUTODOWNLOAD_MASK_AUDIO = 2;
    public static final int AUTODOWNLOAD_MASK_DOCUMENT = 8;
    public static final int AUTODOWNLOAD_MASK_GIF = 32;
    public static final int AUTODOWNLOAD_MASK_MUSIC = 16;
    public static final int AUTODOWNLOAD_MASK_PHOTO = 1;
    public static final int AUTODOWNLOAD_MASK_VIDEO = 4;
    public static final int AUTODOWNLOAD_MASK_VIDEOMESSAGE = 64;
    private static volatile DownloadController[] Instance = new DownloadController[3];
    private HashMap<String, FileDownloadProgressListener> addLaterArray;
    private ArrayList<DownloadObject> audioDownloadQueue;
    private int currentAccount;
    private ArrayList<FileDownloadProgressListener> deleteLaterArray;
    private ArrayList<DownloadObject> documentDownloadQueue;
    private HashMap<String, DownloadObject> downloadQueueKeys;
    private ArrayList<DownloadObject> gifDownloadQueue;
    public boolean globalAutodownloadEnabled;
    private int lastCheckMask;
    private int lastTag;
    private boolean listenerInProgress;
    private HashMap<String, ArrayList<MessageObject>> loadingFileMessagesObservers;
    private HashMap<String, ArrayList<WeakReference<FileDownloadProgressListener>>> loadingFileObservers;
    public int[] mobileDataDownloadMask;
    public int[] mobileMaxFileSize;
    private ArrayList<DownloadObject> musicDownloadQueue;
    private SparseArray<String> observersByTag;
    private ArrayList<DownloadObject> photoDownloadQueue;
    public int[] roamingDownloadMask;
    public int[] roamingMaxFileSize;
    private LongSparseArray<Long> typingTimes;
    private ArrayList<DownloadObject> videoDownloadQueue;
    private ArrayList<DownloadObject> videoMessageDownloadQueue;
    public int[] wifiDownloadMask;
    public int[] wifiMaxFileSize;

    public interface FileDownloadProgressListener {
        int getObserverTag();

        void onFailedDownload(String str);

        void onProgressDownload(String str, float f);

        void onProgressUpload(String str, float f, boolean z);

        void onSuccessDownload(String str);
    }

    public DownloadController(int r1) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.DownloadController.<init>(int):void
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
        r10.<init>();
        r0 = 4;
        r1 = new int[r0];
        r10.mobileDataDownloadMask = r1;
        r1 = new int[r0];
        r10.wifiDownloadMask = r1;
        r1 = new int[r0];
        r10.roamingDownloadMask = r1;
        r1 = 7;
        r2 = new int[r1];
        r10.mobileMaxFileSize = r2;
        r2 = new int[r1];
        r10.wifiMaxFileSize = r2;
        r2 = new int[r1];
        r10.roamingMaxFileSize = r2;
        r2 = 0;
        r10.lastCheckMask = r2;
        r3 = new java.util.ArrayList;
        r3.<init>();
        r10.photoDownloadQueue = r3;
        r3 = new java.util.ArrayList;
        r3.<init>();
        r10.audioDownloadQueue = r3;
        r3 = new java.util.ArrayList;
        r3.<init>();
        r10.videoMessageDownloadQueue = r3;
        r3 = new java.util.ArrayList;
        r3.<init>();
        r10.documentDownloadQueue = r3;
        r3 = new java.util.ArrayList;
        r3.<init>();
        r10.musicDownloadQueue = r3;
        r3 = new java.util.ArrayList;
        r3.<init>();
        r10.gifDownloadQueue = r3;
        r3 = new java.util.ArrayList;
        r3.<init>();
        r10.videoDownloadQueue = r3;
        r3 = new java.util.HashMap;
        r3.<init>();
        r10.downloadQueueKeys = r3;
        r3 = new java.util.HashMap;
        r3.<init>();
        r10.loadingFileObservers = r3;
        r3 = new java.util.HashMap;
        r3.<init>();
        r10.loadingFileMessagesObservers = r3;
        r3 = new android.util.SparseArray;
        r3.<init>();
        r10.observersByTag = r3;
        r10.listenerInProgress = r2;
        r3 = new java.util.HashMap;
        r3.<init>();
        r10.addLaterArray = r3;
        r3 = new java.util.ArrayList;
        r3.<init>();
        r10.deleteLaterArray = r3;
        r10.lastTag = r2;
        r3 = new android.util.LongSparseArray;
        r3.<init>();
        r10.typingTimes = r3;
        r10.currentAccount = r11;
        r3 = r10.currentAccount;
        r3 = org.telegram.messenger.MessagesController.getMainSettings(r3);
        r4 = r2;
        if (r4 >= r0) goto L_0x011f;
    L_0x0091:
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "mobileDataDownloadMask";
        r5.append(r6);
        if (r4 != 0) goto L_0x00a0;
        r6 = "";
        goto L_0x00a4;
        r6 = java.lang.Integer.valueOf(r4);
        r5.append(r6);
        r5 = r5.toString();
        if (r4 == 0) goto L_0x00cd;
        r6 = r3.contains(r5);
        if (r6 == 0) goto L_0x00b4;
        goto L_0x00cd;
        r6 = r10.mobileDataDownloadMask;
        r7 = r10.mobileDataDownloadMask;
        r7 = r7[r2];
        r6[r4] = r7;
        r6 = r10.wifiDownloadMask;
        r7 = r10.wifiDownloadMask;
        r7 = r7[r2];
        r6[r4] = r7;
        r6 = r10.roamingDownloadMask;
        r7 = r10.roamingDownloadMask;
        r7 = r7[r2];
        r6[r4] = r7;
        goto L_0x011b;
        r6 = r10.mobileDataDownloadMask;
        r7 = 115; // 0x73 float:1.61E-43 double:5.7E-322;
        r8 = r3.getInt(r5, r7);
        r6[r4] = r8;
        r6 = r10.wifiDownloadMask;
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r9 = "wifiDownloadMask";
        r8.append(r9);
        if (r4 != 0) goto L_0x00e8;
        r9 = "";
        goto L_0x00ec;
        r9 = java.lang.Integer.valueOf(r4);
        r8.append(r9);
        r8 = r8.toString();
        r7 = r3.getInt(r8, r7);
        r6[r4] = r7;
        r6 = r10.roamingDownloadMask;
        r7 = new java.lang.StringBuilder;
        r7.<init>();
        r8 = "roamingDownloadMask";
        r7.append(r8);
        if (r4 != 0) goto L_0x010a;
        r8 = "";
        goto L_0x010e;
        r8 = java.lang.Integer.valueOf(r4);
        r7.append(r8);
        r7 = r7.toString();
        r7 = r3.getInt(r7, r2);
        r6[r4] = r7;
        r4 = r4 + 1;
        goto L_0x008f;
        r0 = r2;
        r2 = 1;
        if (r0 >= r1) goto L_0x017f;
        if (r0 != r2) goto L_0x0129;
        r2 = 2097152; // 0x200000 float:2.938736E-39 double:1.0361308E-317;
        goto L_0x0131;
        r2 = 6;
        if (r0 != r2) goto L_0x012f;
        r2 = 5242880; // 0x500000 float:7.34684E-39 double:2.590327E-317;
        goto L_0x0128;
        r2 = 10485760; // 0xa00000 float:1.469368E-38 double:5.180654E-317;
        r4 = r10.mobileMaxFileSize;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "mobileMaxDownloadSize";
        r5.append(r6);
        r5.append(r0);
        r5 = r5.toString();
        r5 = r3.getInt(r5, r2);
        r4[r0] = r5;
        r4 = r10.wifiMaxFileSize;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "wifiMaxDownloadSize";
        r5.append(r6);
        r5.append(r0);
        r5 = r5.toString();
        r5 = r3.getInt(r5, r2);
        r4[r0] = r5;
        r4 = r10.roamingMaxFileSize;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "roamingMaxDownloadSize";
        r5.append(r6);
        r5.append(r0);
        r5 = r5.toString();
        r5 = r3.getInt(r5, r2);
        r4[r0] = r5;
        r2 = r0 + 1;
        goto L_0x0120;
        r0 = "globalAutodownloadEnabled";
        r0 = r3.getBoolean(r0, r2);
        r10.globalAutodownloadEnabled = r0;
        r0 = new org.telegram.messenger.DownloadController$1;
        r0.<init>();
        org.telegram.messenger.AndroidUtilities.runOnUIThread(r0);
        r0 = new org.telegram.messenger.DownloadController$2;
        r0.<init>();
        r1 = new android.content.IntentFilter;
        r2 = "android.net.conn.CONNECTIVITY_CHANGE";
        r1.<init>(r2);
        r2 = org.telegram.messenger.ApplicationLoader.applicationContext;
        r2.registerReceiver(r0, r1);
        r2 = r10.currentAccount;
        r2 = org.telegram.messenger.UserConfig.getInstance(r2);
        r2 = r2.isClientActivated();
        if (r2 == 0) goto L_0x01af;
        r10.checkAutodownloadSettings();
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.DownloadController.<init>(int):void");
    }

    protected int getAutodownloadMask() {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.DownloadController.getAutodownloadMask():int
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
        r0 = r6.globalAutodownloadEnabled;
        r1 = 0;
        if (r0 != 0) goto L_0x0006;
    L_0x0005:
        return r1;
    L_0x0006:
        r0 = 0;
        r2 = org.telegram.tgnet.ConnectionsManager.isConnectedToWiFi();
        if (r2 == 0) goto L_0x0010;
    L_0x000d:
        r2 = r6.wifiDownloadMask;
        goto L_0x001b;
    L_0x0010:
        r2 = org.telegram.tgnet.ConnectionsManager.isRoaming();
        if (r2 == 0) goto L_0x0019;
        r2 = r6.roamingDownloadMask;
        goto L_0x000f;
        r2 = r6.mobileDataDownloadMask;
        r3 = 4;
        if (r1 >= r3) goto L_0x005f;
        r4 = 0;
        r5 = r2[r1];
        r5 = r5 & 1;
        if (r5 == 0) goto L_0x0028;
        r4 = r4 | 1;
        r5 = r2[r1];
        r5 = r5 & 2;
        if (r5 == 0) goto L_0x0030;
        r4 = r4 | 2;
        r5 = r2[r1];
        r5 = r5 & 64;
        if (r5 == 0) goto L_0x0038;
        r4 = r4 | 64;
        r5 = r2[r1];
        r3 = r3 & r5;
        if (r3 == 0) goto L_0x003f;
        r4 = r4 | 4;
        r3 = r2[r1];
        r3 = r3 & 8;
        if (r3 == 0) goto L_0x0047;
        r4 = r4 | 8;
        r3 = r2[r1];
        r3 = r3 & 16;
        if (r3 == 0) goto L_0x004f;
        r4 = r4 | 16;
        r3 = r2[r1];
        r3 = r3 & 32;
        if (r3 == 0) goto L_0x0057;
        r4 = r4 | 32;
        r3 = r1 * 8;
        r3 = r4 << r3;
        r0 = r0 | r3;
        r1 = r1 + 1;
        goto L_0x001c;
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.DownloadController.getAutodownloadMask():int");
    }

    public static DownloadController getInstance(int num) {
        DownloadController localInstance = Instance[num];
        if (localInstance == null) {
            synchronized (DownloadController.class) {
                localInstance = Instance[num];
                if (localInstance == null) {
                    DownloadController[] downloadControllerArr = Instance;
                    DownloadController downloadController = new DownloadController(num);
                    localInstance = downloadController;
                    downloadControllerArr[num] = downloadController;
                }
            }
        }
        return localInstance;
    }

    public static int maskToIndex(int mask) {
        if (mask == 1) {
            return 0;
        }
        if (mask == 2) {
            return 1;
        }
        if (mask == 4) {
            return 2;
        }
        if (mask == 8) {
            return 3;
        }
        if (mask == 16) {
            return 4;
        }
        if (mask == 32) {
            return 5;
        }
        if (mask == 64) {
            return 6;
        }
        return 0;
    }

    public void cleanup() {
        this.photoDownloadQueue.clear();
        this.audioDownloadQueue.clear();
        this.videoMessageDownloadQueue.clear();
        this.documentDownloadQueue.clear();
        this.videoDownloadQueue.clear();
        this.musicDownloadQueue.clear();
        this.gifDownloadQueue.clear();
        this.downloadQueueKeys.clear();
        this.typingTimes.clear();
    }

    protected int getAutodownloadMaskAll() {
        int a = 0;
        if (!this.globalAutodownloadEnabled) {
            return 0;
        }
        int mask = 0;
        while (a < 4) {
            if (!((this.mobileDataDownloadMask[a] & 1) == 0 && (this.wifiDownloadMask[a] & 1) == 0 && (this.roamingDownloadMask[a] & 1) == 0)) {
                mask |= 1;
            }
            if (!((this.mobileDataDownloadMask[a] & 2) == 0 && (this.wifiDownloadMask[a] & 2) == 0 && (this.roamingDownloadMask[a] & 2) == 0)) {
                mask |= 2;
            }
            if (!((this.mobileDataDownloadMask[a] & 64) == 0 && (this.wifiDownloadMask[a] & 64) == 0 && (this.roamingDownloadMask[a] & 64) == 0)) {
                mask |= 64;
            }
            if (!((this.mobileDataDownloadMask[a] & 4) == 0 && (this.wifiDownloadMask[a] & 4) == 0 && (4 & this.roamingDownloadMask[a]) == 0)) {
                mask |= 4;
            }
            if (!((this.mobileDataDownloadMask[a] & 8) == 0 && (this.wifiDownloadMask[a] & 8) == 0 && (this.roamingDownloadMask[a] & 8) == 0)) {
                mask |= 8;
            }
            if (!((this.mobileDataDownloadMask[a] & 16) == 0 && (this.wifiDownloadMask[a] & 16) == 0 && (this.roamingDownloadMask[a] & 16) == 0)) {
                mask |= 16;
            }
            if ((this.mobileDataDownloadMask[a] & 32) != 0 || (this.wifiDownloadMask[a] & 32) != 0 || (this.roamingDownloadMask[a] & 32) != 0) {
                mask |= 32;
            }
            a++;
        }
        return mask;
    }

    public void checkAutodownloadSettings() {
        int currentMask = getCurrentDownloadMask();
        if (currentMask != this.lastCheckMask) {
            int a;
            this.lastCheckMask = currentMask;
            if ((currentMask & 1) == 0) {
                for (a = 0; a < this.photoDownloadQueue.size(); a++) {
                    FileLoader.getInstance(this.currentAccount).cancelLoadFile((PhotoSize) ((DownloadObject) this.photoDownloadQueue.get(a)).object);
                }
                this.photoDownloadQueue.clear();
            } else if (this.photoDownloadQueue.isEmpty()) {
                newDownloadObjectsAvailable(1);
            }
            if ((currentMask & 2) == 0) {
                for (a = 0; a < this.audioDownloadQueue.size(); a++) {
                    FileLoader.getInstance(this.currentAccount).cancelLoadFile((Document) ((DownloadObject) this.audioDownloadQueue.get(a)).object);
                }
                this.audioDownloadQueue.clear();
            } else if (this.audioDownloadQueue.isEmpty()) {
                newDownloadObjectsAvailable(2);
            }
            if ((currentMask & 64) == 0) {
                for (a = 0; a < this.videoMessageDownloadQueue.size(); a++) {
                    FileLoader.getInstance(this.currentAccount).cancelLoadFile((Document) ((DownloadObject) this.videoMessageDownloadQueue.get(a)).object);
                }
                this.videoMessageDownloadQueue.clear();
            } else if (this.videoMessageDownloadQueue.isEmpty()) {
                newDownloadObjectsAvailable(64);
            }
            if ((currentMask & 8) == 0) {
                for (a = 0; a < this.documentDownloadQueue.size(); a++) {
                    FileLoader.getInstance(this.currentAccount).cancelLoadFile(((DownloadObject) this.documentDownloadQueue.get(a)).object);
                }
                this.documentDownloadQueue.clear();
            } else if (this.documentDownloadQueue.isEmpty()) {
                newDownloadObjectsAvailable(8);
            }
            if ((currentMask & 4) == 0) {
                for (a = 0; a < this.videoDownloadQueue.size(); a++) {
                    FileLoader.getInstance(this.currentAccount).cancelLoadFile((Document) ((DownloadObject) this.videoDownloadQueue.get(a)).object);
                }
                this.videoDownloadQueue.clear();
            } else if (this.videoDownloadQueue.isEmpty()) {
                newDownloadObjectsAvailable(4);
            }
            if ((currentMask & 16) == 0) {
                for (a = 0; a < this.musicDownloadQueue.size(); a++) {
                    FileLoader.getInstance(this.currentAccount).cancelLoadFile(((DownloadObject) this.musicDownloadQueue.get(a)).object);
                }
                this.musicDownloadQueue.clear();
            } else if (this.musicDownloadQueue.isEmpty()) {
                newDownloadObjectsAvailable(16);
            }
            if ((currentMask & 32) == 0) {
                for (a = 0; a < this.gifDownloadQueue.size(); a++) {
                    FileLoader.getInstance(this.currentAccount).cancelLoadFile(((DownloadObject) this.gifDownloadQueue.get(a)).object);
                }
                this.gifDownloadQueue.clear();
            } else if (this.gifDownloadQueue.isEmpty()) {
                newDownloadObjectsAvailable(32);
            }
            a = getAutodownloadMaskAll();
            if (a == 0) {
                MessagesStorage.getInstance(this.currentAccount).clearDownloadQueue(0);
            } else {
                if ((a & 1) == 0) {
                    MessagesStorage.getInstance(this.currentAccount).clearDownloadQueue(1);
                }
                if ((a & 2) == 0) {
                    MessagesStorage.getInstance(this.currentAccount).clearDownloadQueue(2);
                }
                if ((a & 64) == 0) {
                    MessagesStorage.getInstance(this.currentAccount).clearDownloadQueue(64);
                }
                if ((a & 4) == 0) {
                    MessagesStorage.getInstance(this.currentAccount).clearDownloadQueue(4);
                }
                if ((a & 8) == 0) {
                    MessagesStorage.getInstance(this.currentAccount).clearDownloadQueue(8);
                }
                if ((a & 16) == 0) {
                    MessagesStorage.getInstance(this.currentAccount).clearDownloadQueue(16);
                }
                if ((a & 32) == 0) {
                    MessagesStorage.getInstance(this.currentAccount).clearDownloadQueue(32);
                }
            }
        }
    }

    public boolean canDownloadMedia(MessageObject messageObject) {
        return canDownloadMedia(messageObject.messageOwner);
    }

    public boolean canDownloadMedia(Message message) {
        boolean z = false;
        if (!this.globalAutodownloadEnabled) {
            return false;
        }
        int type;
        Peer peer;
        int index;
        int mask;
        int maxSize;
        if (MessageObject.isPhoto(message)) {
            type = 1;
        } else if (MessageObject.isVoiceMessage(message)) {
            type = 2;
        } else if (MessageObject.isRoundVideoMessage(message)) {
            type = 64;
        } else if (MessageObject.isVideoMessage(message)) {
            type = 4;
        } else if (MessageObject.isMusicMessage(message)) {
            type = 16;
        } else if (MessageObject.isGifMessage(message)) {
            type = 32;
        } else {
            type = 8;
            peer = message.to_id;
            if (peer != null) {
                index = 1;
            } else if (peer.user_id == 0) {
                if (ContactsController.getInstance(this.currentAccount).contactsDict.containsKey(Integer.valueOf(peer.user_id))) {
                    index = 1;
                } else {
                    index = 0;
                }
            } else if (peer.chat_id != 0) {
                index = 2;
            } else if (MessageObject.isMegagroup(message)) {
                index = 3;
            } else {
                index = 2;
            }
            if (ConnectionsManager.isConnectedToWiFi()) {
                mask = this.wifiDownloadMask[index];
                maxSize = this.wifiMaxFileSize[maskToIndex(type)];
            } else if (ConnectionsManager.isRoaming()) {
                mask = this.mobileDataDownloadMask[index];
                maxSize = this.mobileMaxFileSize[maskToIndex(type)];
                if ((type == 1 || MessageObject.getMessageSize(message) <= maxSize) && (mask & type) != 0) {
                    z = true;
                }
                return z;
            } else {
                mask = this.roamingDownloadMask[index];
                maxSize = this.roamingMaxFileSize[maskToIndex(type)];
            }
            z = true;
            return z;
        }
        peer = message.to_id;
        if (peer != null) {
            index = 1;
        } else if (peer.user_id == 0) {
            if (peer.chat_id != 0) {
                index = 2;
            } else if (MessageObject.isMegagroup(message)) {
                index = 3;
            } else {
                index = 2;
            }
        } else if (ContactsController.getInstance(this.currentAccount).contactsDict.containsKey(Integer.valueOf(peer.user_id))) {
            index = 1;
        } else {
            index = 0;
        }
        if (ConnectionsManager.isConnectedToWiFi()) {
            mask = this.wifiDownloadMask[index];
            maxSize = this.wifiMaxFileSize[maskToIndex(type)];
        } else if (ConnectionsManager.isRoaming()) {
            mask = this.mobileDataDownloadMask[index];
            maxSize = this.mobileMaxFileSize[maskToIndex(type)];
            z = true;
            return z;
        } else {
            mask = this.roamingDownloadMask[index];
            maxSize = this.roamingMaxFileSize[maskToIndex(type)];
        }
        z = true;
        return z;
    }

    protected int getCurrentDownloadMask() {
        int a = 0;
        if (!this.globalAutodownloadEnabled) {
            return 0;
        }
        int mask;
        if (ConnectionsManager.isConnectedToWiFi()) {
            mask = 0;
            while (a < 4) {
                mask |= this.wifiDownloadMask[a];
                a++;
            }
            return mask;
        } else if (ConnectionsManager.isRoaming()) {
            mask = 0;
            while (a < 4) {
                mask |= this.roamingDownloadMask[a];
                a++;
            }
            return mask;
        } else {
            mask = 0;
            while (a < 4) {
                mask |= this.mobileDataDownloadMask[a];
                a++;
            }
            return mask;
        }
    }

    protected void processDownloadObjects(int type, ArrayList<DownloadObject> objects) {
        if (!objects.isEmpty()) {
            ArrayList<DownloadObject> queue = null;
            if (type == 1) {
                queue = this.photoDownloadQueue;
            } else if (type == 2) {
                queue = this.audioDownloadQueue;
            } else if (type == 64) {
                queue = this.videoMessageDownloadQueue;
            } else if (type == 4) {
                queue = this.videoDownloadQueue;
            } else if (type == 8) {
                queue = this.documentDownloadQueue;
            } else if (type == 16) {
                queue = this.musicDownloadQueue;
            } else if (type == 32) {
                queue = this.gifDownloadQueue;
            }
            for (int a = 0; a < objects.size(); a++) {
                String path;
                DownloadObject downloadObject = (DownloadObject) objects.get(a);
                if (downloadObject.object instanceof Document) {
                    path = FileLoader.getAttachFileName((Document) downloadObject.object);
                } else {
                    path = FileLoader.getAttachFileName(downloadObject.object);
                }
                if (!this.downloadQueueKeys.containsKey(path)) {
                    boolean added = true;
                    if (downloadObject.object instanceof PhotoSize) {
                        FileLoader.getInstance(this.currentAccount).loadFile((PhotoSize) downloadObject.object, null, downloadObject.secret ? 2 : 0);
                    } else if (downloadObject.object instanceof Document) {
                        FileLoader.getInstance(this.currentAccount).loadFile(downloadObject.object, false, downloadObject.secret ? 2 : 0);
                    } else {
                        added = false;
                    }
                    if (added) {
                        queue.add(downloadObject);
                        this.downloadQueueKeys.put(path, downloadObject);
                    }
                }
            }
        }
    }

    protected void newDownloadObjectsAvailable(int downloadMask) {
        int mask = getCurrentDownloadMask();
        if (!((mask & 1) == 0 || (downloadMask & 1) == 0 || !this.photoDownloadQueue.isEmpty())) {
            MessagesStorage.getInstance(this.currentAccount).getDownloadQueue(1);
        }
        if (!((mask & 2) == 0 || (downloadMask & 2) == 0 || !this.audioDownloadQueue.isEmpty())) {
            MessagesStorage.getInstance(this.currentAccount).getDownloadQueue(2);
        }
        if (!((mask & 64) == 0 || (downloadMask & 64) == 0 || !this.videoMessageDownloadQueue.isEmpty())) {
            MessagesStorage.getInstance(this.currentAccount).getDownloadQueue(64);
        }
        if (!((mask & 4) == 0 || (downloadMask & 4) == 0 || !this.videoDownloadQueue.isEmpty())) {
            MessagesStorage.getInstance(this.currentAccount).getDownloadQueue(4);
        }
        if (!((mask & 8) == 0 || (downloadMask & 8) == 0 || !this.documentDownloadQueue.isEmpty())) {
            MessagesStorage.getInstance(this.currentAccount).getDownloadQueue(8);
        }
        if (!((mask & 16) == 0 || (downloadMask & 16) == 0 || !this.musicDownloadQueue.isEmpty())) {
            MessagesStorage.getInstance(this.currentAccount).getDownloadQueue(16);
        }
        if ((mask & 32) != 0 && (downloadMask & 32) != 0 && this.gifDownloadQueue.isEmpty()) {
            MessagesStorage.getInstance(this.currentAccount).getDownloadQueue(32);
        }
    }

    private void checkDownloadFinished(String fileName, int state) {
        DownloadObject downloadObject = (DownloadObject) this.downloadQueueKeys.get(fileName);
        if (downloadObject != null) {
            this.downloadQueueKeys.remove(fileName);
            if (state == 0 || state == 2) {
                MessagesStorage.getInstance(this.currentAccount).removeFromDownloadQueue(downloadObject.id, downloadObject.type, false);
            }
            if (downloadObject.type == 1) {
                this.photoDownloadQueue.remove(downloadObject);
                if (this.photoDownloadQueue.isEmpty()) {
                    newDownloadObjectsAvailable(1);
                }
            } else if (downloadObject.type == 2) {
                this.audioDownloadQueue.remove(downloadObject);
                if (this.audioDownloadQueue.isEmpty()) {
                    newDownloadObjectsAvailable(2);
                }
            } else if (downloadObject.type == 64) {
                this.videoMessageDownloadQueue.remove(downloadObject);
                if (this.videoMessageDownloadQueue.isEmpty()) {
                    newDownloadObjectsAvailable(64);
                }
            } else if (downloadObject.type == 4) {
                this.videoDownloadQueue.remove(downloadObject);
                if (this.videoDownloadQueue.isEmpty()) {
                    newDownloadObjectsAvailable(4);
                }
            } else if (downloadObject.type == 8) {
                this.documentDownloadQueue.remove(downloadObject);
                if (this.documentDownloadQueue.isEmpty()) {
                    newDownloadObjectsAvailable(8);
                }
            } else if (downloadObject.type == 16) {
                this.musicDownloadQueue.remove(downloadObject);
                if (this.musicDownloadQueue.isEmpty()) {
                    newDownloadObjectsAvailable(16);
                }
            } else if (downloadObject.type == 32) {
                this.gifDownloadQueue.remove(downloadObject);
                if (this.gifDownloadQueue.isEmpty()) {
                    newDownloadObjectsAvailable(32);
                }
            }
        }
    }

    public int generateObserverTag() {
        int i = this.lastTag;
        this.lastTag = i + 1;
        return i;
    }

    public void addLoadingFileObserver(String fileName, FileDownloadProgressListener observer) {
        addLoadingFileObserver(fileName, null, observer);
    }

    public void addLoadingFileObserver(String fileName, MessageObject messageObject, FileDownloadProgressListener observer) {
        if (this.listenerInProgress) {
            this.addLaterArray.put(fileName, observer);
            return;
        }
        removeLoadingFileObserver(observer);
        ArrayList<WeakReference<FileDownloadProgressListener>> arrayList = (ArrayList) this.loadingFileObservers.get(fileName);
        if (arrayList == null) {
            arrayList = new ArrayList();
            this.loadingFileObservers.put(fileName, arrayList);
        }
        arrayList.add(new WeakReference(observer));
        if (messageObject != null) {
            ArrayList<MessageObject> messageObjects = (ArrayList) this.loadingFileMessagesObservers.get(fileName);
            if (messageObjects == null) {
                messageObjects = new ArrayList();
                this.loadingFileMessagesObservers.put(fileName, messageObjects);
            }
            messageObjects.add(messageObject);
        }
        this.observersByTag.put(observer.getObserverTag(), fileName);
    }

    public void removeLoadingFileObserver(FileDownloadProgressListener observer) {
        if (this.listenerInProgress) {
            this.deleteLaterArray.add(observer);
            return;
        }
        String fileName = (String) this.observersByTag.get(observer.getObserverTag());
        if (fileName != null) {
            ArrayList<WeakReference<FileDownloadProgressListener>> arrayList = (ArrayList) this.loadingFileObservers.get(fileName);
            if (arrayList != null) {
                int a = 0;
                while (a < arrayList.size()) {
                    WeakReference<FileDownloadProgressListener> reference = (WeakReference) arrayList.get(a);
                    if (reference.get() == null || reference.get() == observer) {
                        arrayList.remove(a);
                        a--;
                    }
                    a++;
                }
                if (arrayList.isEmpty()) {
                    this.loadingFileObservers.remove(fileName);
                }
            }
            this.observersByTag.remove(observer.getObserverTag());
        }
    }

    private void processLaterArrays() {
        for (Entry<String, FileDownloadProgressListener> listener : this.addLaterArray.entrySet()) {
            addLoadingFileObserver((String) listener.getKey(), (FileDownloadProgressListener) listener.getValue());
        }
        this.addLaterArray.clear();
        Iterator it = this.deleteLaterArray.iterator();
        while (it.hasNext()) {
            removeLoadingFileObserver((FileDownloadProgressListener) it.next());
        }
        this.deleteLaterArray.clear();
    }

    public void didReceivedNotification(int id, int account, Object... args) {
        String fileName;
        int a;
        int a2;
        WeakReference<FileDownloadProgressListener> reference;
        DownloadController downloadController = this;
        int i = id;
        if (i != NotificationCenter.FileDidFailedLoad) {
            if (i != NotificationCenter.httpFileDidFailedLoad) {
                if (i != NotificationCenter.FileDidLoaded) {
                    if (i != NotificationCenter.httpFileDidLoaded) {
                        ArrayList<WeakReference<FileDownloadProgressListener>> arrayList;
                        Float progress;
                        int size;
                        if (i == NotificationCenter.FileLoadProgressChanged) {
                            downloadController.listenerInProgress = true;
                            fileName = args[0];
                            arrayList = (ArrayList) downloadController.loadingFileObservers.get(fileName);
                            if (arrayList != null) {
                                progress = args[1];
                                size = arrayList.size();
                                for (a = 0; a < size; a++) {
                                    WeakReference<FileDownloadProgressListener> reference2 = (WeakReference) arrayList.get(a);
                                    if (reference2.get() != null) {
                                        ((FileDownloadProgressListener) reference2.get()).onProgressDownload(fileName, progress.floatValue());
                                    }
                                }
                            }
                            downloadController.listenerInProgress = false;
                            processLaterArrays();
                            return;
                        } else if (i == NotificationCenter.FileUploadProgressChanged) {
                            downloadController.listenerInProgress = true;
                            fileName = (String) args[0];
                            arrayList = (ArrayList) downloadController.loadingFileObservers.get(fileName);
                            if (arrayList != null) {
                                progress = (Float) args[1];
                                Boolean enc = args[2];
                                int size2 = arrayList.size();
                                for (size = 0; size < size2; size++) {
                                    WeakReference<FileDownloadProgressListener> reference3 = (WeakReference) arrayList.get(size);
                                    if (reference3.get() != null) {
                                        ((FileDownloadProgressListener) reference3.get()).onProgressUpload(fileName, progress.floatValue(), enc.booleanValue());
                                    }
                                }
                            }
                            downloadController.listenerInProgress = false;
                            processLaterArrays();
                            try {
                                ArrayList<DelayedMessage> delayedMessages = SendMessagesHelper.getInstance(downloadController.currentAccount).getDelayedMessages(fileName);
                                if (delayedMessages != null) {
                                    for (a = 0; a < delayedMessages.size(); a++) {
                                        DelayedMessage delayedMessage = (DelayedMessage) delayedMessages.get(a);
                                        if (delayedMessage.encryptedChat == null) {
                                            long dialog_id = delayedMessage.peer;
                                            if (delayedMessage.type == 4) {
                                                Long lastTime = (Long) downloadController.typingTimes.get(dialog_id);
                                                if (lastTime == null || lastTime.longValue() + 4000 < System.currentTimeMillis()) {
                                                    HashMap hashMap = delayedMessage.extraHashMap;
                                                    StringBuilder stringBuilder = new StringBuilder();
                                                    stringBuilder.append(fileName);
                                                    stringBuilder.append("_i");
                                                    MessageObject messageObject = (MessageObject) hashMap.get(stringBuilder.toString());
                                                    if (messageObject == null || !messageObject.isVideo()) {
                                                        MessagesController.getInstance(downloadController.currentAccount).sendTyping(dialog_id, 4, 0);
                                                    } else {
                                                        MessagesController.getInstance(downloadController.currentAccount).sendTyping(dialog_id, 5, 0);
                                                    }
                                                    downloadController.typingTimes.put(dialog_id, Long.valueOf(System.currentTimeMillis()));
                                                }
                                            } else {
                                                Long lastTime2 = (Long) downloadController.typingTimes.get(dialog_id);
                                                Document document = delayedMessage.obj.getDocument();
                                                if (lastTime2 == null || lastTime2.longValue() + 4000 < System.currentTimeMillis()) {
                                                    if (delayedMessage.obj.isRoundVideo()) {
                                                        MessagesController.getInstance(downloadController.currentAccount).sendTyping(dialog_id, 8, 0);
                                                    } else if (delayedMessage.obj.isVideo()) {
                                                        MessagesController.getInstance(downloadController.currentAccount).sendTyping(dialog_id, 5, 0);
                                                    } else if (delayedMessage.obj.isVoice()) {
                                                        MessagesController.getInstance(downloadController.currentAccount).sendTyping(dialog_id, 9, 0);
                                                    } else if (delayedMessage.obj.getDocument() != null) {
                                                        MessagesController.getInstance(downloadController.currentAccount).sendTyping(dialog_id, 3, 0);
                                                    } else if (delayedMessage.location != null) {
                                                        MessagesController.getInstance(downloadController.currentAccount).sendTyping(dialog_id, 4, 0);
                                                    }
                                                    downloadController.typingTimes.put(dialog_id, Long.valueOf(System.currentTimeMillis()));
                                                }
                                            }
                                        }
                                    }
                                }
                                return;
                            } catch (Throwable e) {
                                FileLog.e(e);
                                return;
                            }
                        } else {
                            return;
                        }
                    }
                }
                downloadController.listenerInProgress = true;
                fileName = (String) args[0];
                ArrayList<MessageObject> messageObjects = (ArrayList) downloadController.loadingFileMessagesObservers.get(fileName);
                if (messageObjects != null) {
                    a = messageObjects.size();
                    for (a2 = 0; a2 < a; a2++) {
                        ((MessageObject) messageObjects.get(a2)).mediaExists = true;
                    }
                    downloadController.loadingFileMessagesObservers.remove(fileName);
                }
                ArrayList<WeakReference<FileDownloadProgressListener>> arrayList2 = (ArrayList) downloadController.loadingFileObservers.get(fileName);
                if (arrayList2 != null) {
                    a = arrayList2.size();
                    for (a2 = 0; a2 < a; a2++) {
                        reference = (WeakReference) arrayList2.get(a2);
                        if (reference.get() != null) {
                            ((FileDownloadProgressListener) reference.get()).onSuccessDownload(fileName);
                            downloadController.observersByTag.remove(((FileDownloadProgressListener) reference.get()).getObserverTag());
                        }
                    }
                    downloadController.loadingFileObservers.remove(fileName);
                }
                downloadController.listenerInProgress = false;
                processLaterArrays();
                checkDownloadFinished(fileName, 0);
                return;
            }
        }
        a2 = 0;
        downloadController.listenerInProgress = true;
        fileName = (String) args[a2];
        ArrayList<WeakReference<FileDownloadProgressListener>> arrayList3 = (ArrayList) downloadController.loadingFileObservers.get(fileName);
        if (arrayList3 != null) {
            a = arrayList3.size();
            for (a2 = 0; a2 < a; a2++) {
                reference = (WeakReference) arrayList3.get(a2);
                if (reference.get() != null) {
                    ((FileDownloadProgressListener) reference.get()).onFailedDownload(fileName);
                    downloadController.observersByTag.remove(((FileDownloadProgressListener) reference.get()).getObserverTag());
                }
            }
            downloadController.loadingFileObservers.remove(fileName);
        }
        downloadController.listenerInProgress = false;
        processLaterArrays();
        checkDownloadFinished(fileName, ((Integer) args[1]).intValue());
    }
}
