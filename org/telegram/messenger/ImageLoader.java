package org.telegram.messenger;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;
import android.util.SparseArray;
import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import org.telegram.messenger.FileLoader.FileLoaderDelegate;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.Document;
import org.telegram.tgnet.TLRPC.FileLocation;
import org.telegram.tgnet.TLRPC.InputEncryptedFile;
import org.telegram.tgnet.TLRPC.InputFile;
import org.telegram.tgnet.TLRPC.Message;
import org.telegram.tgnet.TLRPC.PhotoSize;
import org.telegram.tgnet.TLRPC.TL_documentEncrypted;
import org.telegram.tgnet.TLRPC.TL_fileEncryptedLocation;
import org.telegram.tgnet.TLRPC.TL_webDocument;
import org.telegram.ui.Components.AnimatedFileDrawable;

public class ImageLoader {
    private static volatile ImageLoader Instance = null;
    private static byte[] bytes;
    private static byte[] bytesThumb;
    private static byte[] header = new byte[12];
    private static byte[] headerThumb = new byte[12];
    private HashMap<String, Integer> bitmapUseCounts = new HashMap();
    private DispatchQueue cacheOutQueue = new DispatchQueue("cacheOutQueue");
    private DispatchQueue cacheThumbOutQueue = new DispatchQueue("cacheThumbOutQueue");
    private int currentHttpFileLoadTasksCount;
    private int currentHttpTasksCount;
    private ConcurrentHashMap<String, Float> fileProgresses = new ConcurrentHashMap();
    private HashMap<String, Integer> forceLoadingImages = new HashMap();
    private LinkedList<HttpFileTask> httpFileLoadTasks;
    private HashMap<String, HttpFileTask> httpFileLoadTasksByKeys;
    private LinkedList<HttpImageTask> httpTasks = new LinkedList();
    private String ignoreRemoval;
    private DispatchQueue imageLoadQueue = new DispatchQueue("imageLoadQueue");
    private HashMap<String, CacheImage> imageLoadingByKeys = new HashMap();
    private SparseArray<CacheImage> imageLoadingByTag = new SparseArray();
    private HashMap<String, CacheImage> imageLoadingByUrl = new HashMap();
    private volatile long lastCacheOutTime;
    private int lastImageNum;
    private long lastProgressUpdateTime;
    private LruCache memCache;
    private HashMap<String, Runnable> retryHttpsTasks;
    private File telegramPath;
    private HashMap<String, ThumbGenerateTask> thumbGenerateTasks = new HashMap();
    private DispatchQueue thumbGeneratingQueue = new DispatchQueue("thumbGeneratingQueue");
    private HashMap<String, ThumbGenerateInfo> waitingForQualityThumb = new HashMap();
    private SparseArray<String> waitingForQualityThumbByTag = new SparseArray();

    private class CacheImage {
        protected boolean animatedFile;
        protected CacheOutTask cacheTask;
        protected int currentAccount;
        protected File encryptionKeyPath;
        protected String ext;
        protected String filter;
        protected ArrayList<String> filters;
        protected File finalFilePath;
        protected HttpImageTask httpTask;
        protected String httpUrl;
        protected ArrayList<ImageReceiver> imageReceiverArray;
        protected String key;
        protected ArrayList<String> keys;
        protected TLObject location;
        protected boolean selfThumb;
        protected File tempFilePath;
        protected ArrayList<Boolean> thumbs;
        protected String url;

        private CacheImage() {
            this.imageReceiverArray = new ArrayList();
            this.keys = new ArrayList();
            this.filters = new ArrayList();
            this.thumbs = new ArrayList();
        }

        public void addImageReceiver(ImageReceiver imageReceiver, String key, String filter, boolean thumb) {
            if (!this.imageReceiverArray.contains(imageReceiver)) {
                this.imageReceiverArray.add(imageReceiver);
                this.keys.add(key);
                this.filters.add(filter);
                this.thumbs.add(Boolean.valueOf(thumb));
                ImageLoader.this.imageLoadingByTag.put(imageReceiver.getTag(thumb), this);
            }
        }

        public void replaceImageReceiver(ImageReceiver imageReceiver, String key, String filter, boolean thumb) {
            int index = this.imageReceiverArray.indexOf(imageReceiver);
            if (index != -1) {
                if (((Boolean) this.thumbs.get(index)).booleanValue() != thumb) {
                    index = this.imageReceiverArray.subList(index + 1, this.imageReceiverArray.size()).indexOf(imageReceiver);
                    if (index == -1) {
                        return;
                    }
                }
                this.keys.set(index, key);
                this.filters.set(index, filter);
            }
        }

        public void removeImageReceiver(ImageReceiver imageReceiver) {
            int a = 0;
            Boolean thumb = Boolean.valueOf(this.selfThumb);
            int a2 = 0;
            while (a2 < this.imageReceiverArray.size()) {
                ImageReceiver obj = (ImageReceiver) this.imageReceiverArray.get(a2);
                if (obj == null || obj == imageReceiver) {
                    this.imageReceiverArray.remove(a2);
                    this.keys.remove(a2);
                    this.filters.remove(a2);
                    thumb = (Boolean) this.thumbs.remove(a2);
                    if (obj != null) {
                        ImageLoader.this.imageLoadingByTag.remove(obj.getTag(thumb.booleanValue()));
                    }
                    a2--;
                }
                a2++;
            }
            if (this.imageReceiverArray.size() == 0) {
                while (true) {
                    a2 = a;
                    if (a2 >= this.imageReceiverArray.size()) {
                        break;
                    }
                    ImageLoader.this.imageLoadingByTag.remove(((ImageReceiver) this.imageReceiverArray.get(a2)).getTag(thumb.booleanValue()));
                    a = a2 + 1;
                }
                this.imageReceiverArray.clear();
                if (!(this.location == null || ImageLoader.this.forceLoadingImages.containsKey(this.key))) {
                    if (this.location instanceof FileLocation) {
                        FileLoader.getInstance(this.currentAccount).cancelLoadFile((FileLocation) this.location, this.ext);
                    } else if (this.location instanceof Document) {
                        FileLoader.getInstance(this.currentAccount).cancelLoadFile((Document) this.location);
                    } else if (this.location instanceof TL_webDocument) {
                        FileLoader.getInstance(this.currentAccount).cancelLoadFile((TL_webDocument) this.location);
                    }
                }
                if (this.cacheTask != null) {
                    if (this.selfThumb) {
                        ImageLoader.this.cacheThumbOutQueue.cancelRunnable(this.cacheTask);
                    } else {
                        ImageLoader.this.cacheOutQueue.cancelRunnable(this.cacheTask);
                    }
                    this.cacheTask.cancel();
                    this.cacheTask = null;
                }
                if (this.httpTask != null) {
                    ImageLoader.this.httpTasks.remove(this.httpTask);
                    this.httpTask.cancel(true);
                    this.httpTask = null;
                }
                if (this.url != null) {
                    ImageLoader.this.imageLoadingByUrl.remove(this.url);
                }
                if (this.key != null) {
                    ImageLoader.this.imageLoadingByKeys.remove(this.key);
                }
            }
        }

        public void setImageAndClear(final BitmapDrawable image) {
            if (image != null) {
                final ArrayList<ImageReceiver> finalImageReceiverArray = new ArrayList(this.imageReceiverArray);
                AndroidUtilities.runOnUIThread(new Runnable() {
                    public void run() {
                        int a;
                        if (image instanceof AnimatedFileDrawable) {
                            BitmapDrawable fileDrawable = image;
                            boolean imageSet = false;
                            a = 0;
                            while (a < finalImageReceiverArray.size()) {
                                if (((ImageReceiver) finalImageReceiverArray.get(a)).setImageBitmapByKey(a == 0 ? fileDrawable : fileDrawable.makeCopy(), CacheImage.this.key, CacheImage.this.selfThumb, false)) {
                                    imageSet = true;
                                }
                                a++;
                            }
                            if (!imageSet) {
                                ((AnimatedFileDrawable) image).recycle();
                            }
                            return;
                        }
                        for (a = 0; a < finalImageReceiverArray.size(); a++) {
                            ((ImageReceiver) finalImageReceiverArray.get(a)).setImageBitmapByKey(image, CacheImage.this.key, CacheImage.this.selfThumb, false);
                        }
                    }
                });
            }
            for (int a = 0; a < this.imageReceiverArray.size(); a++) {
                ImageLoader.this.imageLoadingByTag.remove(((ImageReceiver) this.imageReceiverArray.get(a)).getTag(this.selfThumb));
            }
            this.imageReceiverArray.clear();
            if (this.url != null) {
                ImageLoader.this.imageLoadingByUrl.remove(this.url);
            }
            if (this.key != null) {
                ImageLoader.this.imageLoadingByKeys.remove(this.key);
            }
        }
    }

    private class CacheOutTask implements Runnable {
        private CacheImage cacheImage;
        private boolean isCancelled;
        private Thread runningThread;
        private final Object sync = new Object();

        public void run() {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.ImageLoader.CacheOutTask.run():void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
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
            r1 = r43;
            r2 = r1.sync;
            monitor-enter(r2);
            r3 = java.lang.Thread.currentThread();	 Catch:{ all -> 0x06bf }
            r1.runningThread = r3;	 Catch:{ all -> 0x06bf }
            java.lang.Thread.interrupted();	 Catch:{ all -> 0x06bf }
            r3 = r1.isCancelled;	 Catch:{ all -> 0x06bf }
            if (r3 == 0) goto L_0x0014;	 Catch:{ all -> 0x06bf }
        L_0x0012:
            monitor-exit(r2);	 Catch:{ all -> 0x06bf }
            return;	 Catch:{ all -> 0x06bf }
        L_0x0014:
            monitor-exit(r2);	 Catch:{ all -> 0x06bf }
            r2 = r1.cacheImage;
            r2 = r2.animatedFile;
            r3 = 0;
            r4 = 1;
            if (r2 == 0) goto L_0x0050;
        L_0x001d:
            r2 = r1.sync;
            monitor-enter(r2);
            r5 = r1.isCancelled;	 Catch:{ all -> 0x004c }
            if (r5 == 0) goto L_0x0026;	 Catch:{ all -> 0x004c }
        L_0x0024:
            monitor-exit(r2);	 Catch:{ all -> 0x004c }
            return;	 Catch:{ all -> 0x004c }
        L_0x0026:
            monitor-exit(r2);	 Catch:{ all -> 0x004c }
            r2 = new org.telegram.ui.Components.AnimatedFileDrawable;
            r5 = r1.cacheImage;
            r5 = r5.finalFilePath;
            r6 = r1.cacheImage;
            r6 = r6.filter;
            if (r6 == 0) goto L_0x0041;
        L_0x0033:
            r6 = r1.cacheImage;
            r6 = r6.filter;
            r7 = "d";
            r6 = r6.equals(r7);
            if (r6 == 0) goto L_0x0041;
        L_0x003f:
            r3 = r4;
        L_0x0041:
            r2.<init>(r5, r3);
            java.lang.Thread.interrupted();
            r1.onPostExecute(r2);
            goto L_0x06be;
        L_0x004c:
            r0 = move-exception;
            r3 = r0;
            monitor-exit(r2);	 Catch:{ all -> 0x004c }
            throw r3;
        L_0x0050:
            r2 = 0;
            r5 = 0;
            r6 = 0;
            r7 = r1.cacheImage;
            r7 = r7.finalFilePath;
            r8 = r1.cacheImage;
            r8 = r8.encryptionKeyPath;
            if (r8 == 0) goto L_0x006d;
        L_0x005d:
            if (r7 == 0) goto L_0x006d;
        L_0x005f:
            r8 = r7.getAbsolutePath();
            r9 = ".enc";
            r8 = r8.endsWith(r9);
            if (r8 == 0) goto L_0x006d;
        L_0x006b:
            r8 = r4;
            goto L_0x006e;
        L_0x006d:
            r8 = r3;
        L_0x006e:
            r9 = 1;
            r10 = 0;
            r11 = android.os.Build.VERSION.SDK_INT;
            r12 = 19;
            r13 = 0;
            if (r11 >= r12) goto L_0x00e7;
        L_0x0077:
            r11 = r13;
            r12 = new java.io.RandomAccessFile;	 Catch:{ Exception -> 0x00c4, all -> 0x00c1 }
            r14 = "r";	 Catch:{ Exception -> 0x00c4, all -> 0x00c1 }
            r12.<init>(r7, r14);	 Catch:{ Exception -> 0x00c4, all -> 0x00c1 }
            r11 = r12;	 Catch:{ Exception -> 0x00c4, all -> 0x00c1 }
            r12 = r1.cacheImage;	 Catch:{ Exception -> 0x00c4, all -> 0x00c1 }
            r12 = r12.selfThumb;	 Catch:{ Exception -> 0x00c4, all -> 0x00c1 }
            if (r12 == 0) goto L_0x008b;	 Catch:{ Exception -> 0x00c4, all -> 0x00c1 }
        L_0x0086:
            r12 = org.telegram.messenger.ImageLoader.headerThumb;	 Catch:{ Exception -> 0x00c4, all -> 0x00c1 }
            goto L_0x008f;	 Catch:{ Exception -> 0x00c4, all -> 0x00c1 }
        L_0x008b:
            r12 = org.telegram.messenger.ImageLoader.header;	 Catch:{ Exception -> 0x00c4, all -> 0x00c1 }
        L_0x008f:
            r14 = r12.length;	 Catch:{ Exception -> 0x00c4, all -> 0x00c1 }
            r11.readFully(r12, r3, r14);	 Catch:{ Exception -> 0x00c4, all -> 0x00c1 }
            r14 = new java.lang.String;	 Catch:{ Exception -> 0x00c4, all -> 0x00c1 }
            r14.<init>(r12);	 Catch:{ Exception -> 0x00c4, all -> 0x00c1 }
            r14 = r14.toLowerCase();	 Catch:{ Exception -> 0x00c4, all -> 0x00c1 }
            r15 = r14.toLowerCase();	 Catch:{ Exception -> 0x00c4, all -> 0x00c1 }
            r14 = r15;	 Catch:{ Exception -> 0x00c4, all -> 0x00c1 }
            r15 = "riff";	 Catch:{ Exception -> 0x00c4, all -> 0x00c1 }
            r15 = r14.startsWith(r15);	 Catch:{ Exception -> 0x00c4, all -> 0x00c1 }
            if (r15 == 0) goto L_0x00b2;	 Catch:{ Exception -> 0x00c4, all -> 0x00c1 }
        L_0x00a9:
            r15 = "webp";	 Catch:{ Exception -> 0x00c4, all -> 0x00c1 }
            r15 = r14.endsWith(r15);	 Catch:{ Exception -> 0x00c4, all -> 0x00c1 }
            if (r15 == 0) goto L_0x00b2;	 Catch:{ Exception -> 0x00c4, all -> 0x00c1 }
        L_0x00b1:
            r10 = 1;	 Catch:{ Exception -> 0x00c4, all -> 0x00c1 }
        L_0x00b2:
            r11.close();	 Catch:{ Exception -> 0x00c4, all -> 0x00c1 }
            if (r11 == 0) goto L_0x00e7;
        L_0x00b7:
            r11.close();	 Catch:{ Exception -> 0x00bb }
        L_0x00ba:
            goto L_0x00e7;
        L_0x00bb:
            r0 = move-exception;
            r12 = r0;
            org.telegram.messenger.FileLog.e(r12);
            goto L_0x00ba;
        L_0x00c1:
            r0 = move-exception;
            r3 = r0;
            goto L_0x00db;
        L_0x00c4:
            r0 = move-exception;
            r12 = r10;
            r10 = r0;
            org.telegram.messenger.FileLog.e(r10);	 Catch:{ all -> 0x00d8 }
            if (r11 == 0) goto L_0x00d6;
        L_0x00cc:
            r11.close();	 Catch:{ Exception -> 0x00d0 }
        L_0x00cf:
            goto L_0x00d6;
        L_0x00d0:
            r0 = move-exception;
            r10 = r0;
            org.telegram.messenger.FileLog.e(r10);
            goto L_0x00cf;
        L_0x00d6:
            r10 = r12;
            goto L_0x00e7;
        L_0x00d8:
            r0 = move-exception;
            r3 = r0;
            r10 = r12;
        L_0x00db:
            if (r11 == 0) goto L_0x00e6;
        L_0x00dd:
            r11.close();	 Catch:{ Exception -> 0x00e1 }
            goto L_0x00e6;
        L_0x00e1:
            r0 = move-exception;
            r4 = r0;
            org.telegram.messenger.FileLog.e(r4);
        L_0x00e6:
            throw r3;
        L_0x00e7:
            r11 = r1.cacheImage;
            r11 = r11.selfThumb;
            if (r11 == 0) goto L_0x02c5;
        L_0x00ed:
            r11 = 0;
            r15 = r1.cacheImage;
            r15 = r15.filter;
            if (r15 == 0) goto L_0x011d;
        L_0x00f4:
            r15 = r1.cacheImage;
            r15 = r15.filter;
            r14 = "b2";
            r14 = r15.contains(r14);
            if (r14 == 0) goto L_0x0102;
        L_0x0100:
            r11 = 3;
            goto L_0x011d;
        L_0x0102:
            r14 = r1.cacheImage;
            r14 = r14.filter;
            r15 = "b1";
            r14 = r14.contains(r15);
            if (r14 == 0) goto L_0x0110;
        L_0x010e:
            r11 = 2;
            goto L_0x011d;
        L_0x0110:
            r14 = r1.cacheImage;
            r14 = r14.filter;
            r15 = "b";
            r14 = r14.contains(r15);
            if (r14 == 0) goto L_0x011d;
        L_0x011c:
            r11 = 1;
        L_0x011d:
            r14 = org.telegram.messenger.ImageLoader.this;	 Catch:{ Throwable -> 0x02b7 }
            r12 = java.lang.System.currentTimeMillis();	 Catch:{ Throwable -> 0x02b7 }
            r14.lastCacheOutTime = r12;	 Catch:{ Throwable -> 0x02b7 }
            r12 = r1.sync;	 Catch:{ Throwable -> 0x02b7 }
            monitor-enter(r12);	 Catch:{ Throwable -> 0x02b7 }
            r13 = r1.isCancelled;	 Catch:{ all -> 0x02ab }
            if (r13 == 0) goto L_0x0134;
        L_0x012d:
            monitor-exit(r12);	 Catch:{ all -> 0x012f }
            return;
        L_0x012f:
            r0 = move-exception;
            r26 = r2;
            goto L_0x02b5;
        L_0x0134:
            monitor-exit(r12);	 Catch:{ all -> 0x02ab }
            r12 = new android.graphics.BitmapFactory$Options;	 Catch:{ Throwable -> 0x02b7 }
            r12.<init>();	 Catch:{ Throwable -> 0x02b7 }
            r12.inSampleSize = r4;	 Catch:{ Throwable -> 0x02b7 }
            r13 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Throwable -> 0x02b7 }
            r14 = 21;
            if (r13 >= r14) goto L_0x014a;
        L_0x0142:
            r12.inPurgeable = r4;	 Catch:{ Throwable -> 0x0145 }
            goto L_0x014a;
        L_0x0145:
            r0 = move-exception;
            r26 = r2;
            goto L_0x02b2;
        L_0x014a:
            if (r10 == 0) goto L_0x018f;
        L_0x014c:
            r3 = new java.io.RandomAccessFile;	 Catch:{ Throwable -> 0x02b7 }
            r13 = "r";	 Catch:{ Throwable -> 0x02b7 }
            r3.<init>(r7, r13);	 Catch:{ Throwable -> 0x02b7 }
            r20 = r3.getChannel();	 Catch:{ Throwable -> 0x02b7 }
            r21 = java.nio.channels.FileChannel.MapMode.READ_ONLY;	 Catch:{ Throwable -> 0x02b7 }
            r22 = 0;	 Catch:{ Throwable -> 0x02b7 }
            r24 = r7.length();	 Catch:{ Throwable -> 0x02b7 }
            r13 = r20.map(r21, r22, r24);	 Catch:{ Throwable -> 0x02b7 }
            r14 = new android.graphics.BitmapFactory$Options;	 Catch:{ Throwable -> 0x02b7 }
            r14.<init>();	 Catch:{ Throwable -> 0x02b7 }
            r14.inJustDecodeBounds = r4;	 Catch:{ Throwable -> 0x02b7 }
            r15 = r13.limit();	 Catch:{ Throwable -> 0x02b7 }
            r26 = r2;
            r2 = 0;
            org.telegram.messenger.Utilities.loadWebpImage(r2, r13, r15, r14, r4);	 Catch:{ Throwable -> 0x02b1 }
            r2 = r14.outWidth;	 Catch:{ Throwable -> 0x02b1 }
            r15 = r14.outHeight;	 Catch:{ Throwable -> 0x02b1 }
            r4 = android.graphics.Bitmap.Config.ARGB_8888;	 Catch:{ Throwable -> 0x02b1 }
            r2 = org.telegram.messenger.Bitmaps.createBitmap(r2, r15, r4);	 Catch:{ Throwable -> 0x02b1 }
            r6 = r2;	 Catch:{ Throwable -> 0x02b1 }
            r2 = r13.limit();	 Catch:{ Throwable -> 0x02b1 }
            r4 = r12.inPurgeable;	 Catch:{ Throwable -> 0x02b1 }
            r15 = 1;	 Catch:{ Throwable -> 0x02b1 }
            r4 = r4 ^ r15;	 Catch:{ Throwable -> 0x02b1 }
            r15 = 0;	 Catch:{ Throwable -> 0x02b1 }
            org.telegram.messenger.Utilities.loadWebpImage(r6, r13, r2, r15, r4);	 Catch:{ Throwable -> 0x02b1 }
            r3.close();	 Catch:{ Throwable -> 0x02b1 }
            goto L_0x01ed;	 Catch:{ Throwable -> 0x02b1 }
        L_0x018f:
            r26 = r2;	 Catch:{ Throwable -> 0x02b1 }
            r2 = r12.inPurgeable;	 Catch:{ Throwable -> 0x02b1 }
            if (r2 == 0) goto L_0x01d3;	 Catch:{ Throwable -> 0x02b1 }
        L_0x0195:
            r2 = new java.io.RandomAccessFile;	 Catch:{ Throwable -> 0x02b1 }
            r4 = "r";	 Catch:{ Throwable -> 0x02b1 }
            r2.<init>(r7, r4);	 Catch:{ Throwable -> 0x02b1 }
            r13 = r2.length();	 Catch:{ Throwable -> 0x02b1 }
            r4 = (int) r13;	 Catch:{ Throwable -> 0x02b1 }
            r13 = org.telegram.messenger.ImageLoader.bytesThumb;	 Catch:{ Throwable -> 0x02b1 }
            if (r13 == 0) goto L_0x01b3;	 Catch:{ Throwable -> 0x02b1 }
        L_0x01a7:
            r13 = org.telegram.messenger.ImageLoader.bytesThumb;	 Catch:{ Throwable -> 0x02b1 }
            r13 = r13.length;	 Catch:{ Throwable -> 0x02b1 }
            if (r13 < r4) goto L_0x01b3;	 Catch:{ Throwable -> 0x02b1 }
        L_0x01ae:
            r13 = org.telegram.messenger.ImageLoader.bytesThumb;	 Catch:{ Throwable -> 0x02b1 }
            goto L_0x01b4;	 Catch:{ Throwable -> 0x02b1 }
        L_0x01b3:
            r13 = 0;	 Catch:{ Throwable -> 0x02b1 }
        L_0x01b4:
            if (r13 != 0) goto L_0x01bc;	 Catch:{ Throwable -> 0x02b1 }
        L_0x01b6:
            r14 = new byte[r4];	 Catch:{ Throwable -> 0x02b1 }
            r13 = r14;	 Catch:{ Throwable -> 0x02b1 }
            org.telegram.messenger.ImageLoader.bytesThumb = r14;	 Catch:{ Throwable -> 0x02b1 }
        L_0x01bc:
            r2.readFully(r13, r3, r4);	 Catch:{ Throwable -> 0x02b1 }
            r2.close();	 Catch:{ Throwable -> 0x02b1 }
            if (r8 == 0) goto L_0x01cb;	 Catch:{ Throwable -> 0x02b1 }
        L_0x01c4:
            r14 = r1.cacheImage;	 Catch:{ Throwable -> 0x02b1 }
            r14 = r14.encryptionKeyPath;	 Catch:{ Throwable -> 0x02b1 }
            org.telegram.messenger.secretmedia.EncryptedFileInputStream.decryptBytesWithKeyFile(r13, r3, r4, r14);	 Catch:{ Throwable -> 0x02b1 }
        L_0x01cb:
            r3 = android.graphics.BitmapFactory.decodeByteArray(r13, r3, r4, r12);	 Catch:{ Throwable -> 0x02b1 }
            r2 = r3;	 Catch:{ Throwable -> 0x02b1 }
            r6 = r2;	 Catch:{ Throwable -> 0x02b1 }
            goto L_0x01ed;	 Catch:{ Throwable -> 0x02b1 }
        L_0x01d3:
            if (r8 == 0) goto L_0x01df;	 Catch:{ Throwable -> 0x02b1 }
        L_0x01d5:
            r2 = new org.telegram.messenger.secretmedia.EncryptedFileInputStream;	 Catch:{ Throwable -> 0x02b1 }
            r3 = r1.cacheImage;	 Catch:{ Throwable -> 0x02b1 }
            r3 = r3.encryptionKeyPath;	 Catch:{ Throwable -> 0x02b1 }
            r2.<init>(r7, r3);	 Catch:{ Throwable -> 0x02b1 }
            goto L_0x01e4;	 Catch:{ Throwable -> 0x02b1 }
        L_0x01df:
            r2 = new java.io.FileInputStream;	 Catch:{ Throwable -> 0x02b1 }
            r2.<init>(r7);	 Catch:{ Throwable -> 0x02b1 }
        L_0x01e4:
            r3 = 0;	 Catch:{ Throwable -> 0x02b1 }
            r4 = android.graphics.BitmapFactory.decodeStream(r2, r3, r12);	 Catch:{ Throwable -> 0x02b1 }
            r6 = r4;	 Catch:{ Throwable -> 0x02b1 }
            r2.close();	 Catch:{ Throwable -> 0x02b1 }
        L_0x01ed:
            if (r6 != 0) goto L_0x0204;	 Catch:{ Throwable -> 0x02b1 }
        L_0x01ef:
            r2 = r7.length();	 Catch:{ Throwable -> 0x02b1 }
            r13 = 0;	 Catch:{ Throwable -> 0x02b1 }
            r4 = (r2 > r13 ? 1 : (r2 == r13 ? 0 : -1));	 Catch:{ Throwable -> 0x02b1 }
            if (r4 == 0) goto L_0x01ff;	 Catch:{ Throwable -> 0x02b1 }
        L_0x01f9:
            r2 = r1.cacheImage;	 Catch:{ Throwable -> 0x02b1 }
            r2 = r2.filter;	 Catch:{ Throwable -> 0x02b1 }
            if (r2 != 0) goto L_0x02aa;	 Catch:{ Throwable -> 0x02b1 }
        L_0x01ff:
            r7.delete();	 Catch:{ Throwable -> 0x02b1 }
            goto L_0x02aa;	 Catch:{ Throwable -> 0x02b1 }
        L_0x0204:
            r2 = 1;	 Catch:{ Throwable -> 0x02b1 }
            if (r11 != r2) goto L_0x0229;	 Catch:{ Throwable -> 0x02b1 }
        L_0x0207:
            r2 = r6.getConfig();	 Catch:{ Throwable -> 0x02b1 }
            r3 = android.graphics.Bitmap.Config.ARGB_8888;	 Catch:{ Throwable -> 0x02b1 }
            if (r2 != r3) goto L_0x02aa;	 Catch:{ Throwable -> 0x02b1 }
        L_0x020f:
            r21 = 3;	 Catch:{ Throwable -> 0x02b1 }
            r2 = r12.inPurgeable;	 Catch:{ Throwable -> 0x02b1 }
            r3 = 1;	 Catch:{ Throwable -> 0x02b1 }
            r22 = r2 ^ 1;	 Catch:{ Throwable -> 0x02b1 }
            r23 = r6.getWidth();	 Catch:{ Throwable -> 0x02b1 }
            r24 = r6.getHeight();	 Catch:{ Throwable -> 0x02b1 }
            r25 = r6.getRowBytes();	 Catch:{ Throwable -> 0x02b1 }
            r20 = r6;	 Catch:{ Throwable -> 0x02b1 }
            org.telegram.messenger.Utilities.blurBitmap(r20, r21, r22, r23, r24, r25);	 Catch:{ Throwable -> 0x02b1 }
            goto L_0x02aa;	 Catch:{ Throwable -> 0x02b1 }
        L_0x0229:
            r2 = 2;	 Catch:{ Throwable -> 0x02b1 }
            if (r11 != r2) goto L_0x024d;	 Catch:{ Throwable -> 0x02b1 }
        L_0x022c:
            r2 = r6.getConfig();	 Catch:{ Throwable -> 0x02b1 }
            r3 = android.graphics.Bitmap.Config.ARGB_8888;	 Catch:{ Throwable -> 0x02b1 }
            if (r2 != r3) goto L_0x02aa;	 Catch:{ Throwable -> 0x02b1 }
        L_0x0234:
            r21 = 1;	 Catch:{ Throwable -> 0x02b1 }
            r2 = r12.inPurgeable;	 Catch:{ Throwable -> 0x02b1 }
            r3 = 1;	 Catch:{ Throwable -> 0x02b1 }
            r22 = r2 ^ 1;	 Catch:{ Throwable -> 0x02b1 }
            r23 = r6.getWidth();	 Catch:{ Throwable -> 0x02b1 }
            r24 = r6.getHeight();	 Catch:{ Throwable -> 0x02b1 }
            r25 = r6.getRowBytes();	 Catch:{ Throwable -> 0x02b1 }
            r20 = r6;	 Catch:{ Throwable -> 0x02b1 }
            org.telegram.messenger.Utilities.blurBitmap(r20, r21, r22, r23, r24, r25);	 Catch:{ Throwable -> 0x02b1 }
            goto L_0x02aa;	 Catch:{ Throwable -> 0x02b1 }
        L_0x024d:
            r2 = 3;	 Catch:{ Throwable -> 0x02b1 }
            if (r11 != r2) goto L_0x02a1;	 Catch:{ Throwable -> 0x02b1 }
        L_0x0250:
            r2 = r6.getConfig();	 Catch:{ Throwable -> 0x02b1 }
            r3 = android.graphics.Bitmap.Config.ARGB_8888;	 Catch:{ Throwable -> 0x02b1 }
            if (r2 != r3) goto L_0x02aa;	 Catch:{ Throwable -> 0x02b1 }
        L_0x0258:
            r21 = 7;	 Catch:{ Throwable -> 0x02b1 }
            r2 = r12.inPurgeable;	 Catch:{ Throwable -> 0x02b1 }
            r3 = 1;	 Catch:{ Throwable -> 0x02b1 }
            r22 = r2 ^ 1;	 Catch:{ Throwable -> 0x02b1 }
            r23 = r6.getWidth();	 Catch:{ Throwable -> 0x02b1 }
            r24 = r6.getHeight();	 Catch:{ Throwable -> 0x02b1 }
            r25 = r6.getRowBytes();	 Catch:{ Throwable -> 0x02b1 }
            r20 = r6;	 Catch:{ Throwable -> 0x02b1 }
            org.telegram.messenger.Utilities.blurBitmap(r20, r21, r22, r23, r24, r25);	 Catch:{ Throwable -> 0x02b1 }
            r21 = 7;	 Catch:{ Throwable -> 0x02b1 }
            r2 = r12.inPurgeable;	 Catch:{ Throwable -> 0x02b1 }
            r3 = 1;	 Catch:{ Throwable -> 0x02b1 }
            r22 = r2 ^ 1;	 Catch:{ Throwable -> 0x02b1 }
            r23 = r6.getWidth();	 Catch:{ Throwable -> 0x02b1 }
            r24 = r6.getHeight();	 Catch:{ Throwable -> 0x02b1 }
            r25 = r6.getRowBytes();	 Catch:{ Throwable -> 0x02b1 }
            r20 = r6;	 Catch:{ Throwable -> 0x02b1 }
            org.telegram.messenger.Utilities.blurBitmap(r20, r21, r22, r23, r24, r25);	 Catch:{ Throwable -> 0x02b1 }
            r21 = 7;	 Catch:{ Throwable -> 0x02b1 }
            r2 = r12.inPurgeable;	 Catch:{ Throwable -> 0x02b1 }
            r3 = 1;	 Catch:{ Throwable -> 0x02b1 }
            r22 = r2 ^ 1;	 Catch:{ Throwable -> 0x02b1 }
            r23 = r6.getWidth();	 Catch:{ Throwable -> 0x02b1 }
            r24 = r6.getHeight();	 Catch:{ Throwable -> 0x02b1 }
            r25 = r6.getRowBytes();	 Catch:{ Throwable -> 0x02b1 }
            r20 = r6;	 Catch:{ Throwable -> 0x02b1 }
            org.telegram.messenger.Utilities.blurBitmap(r20, r21, r22, r23, r24, r25);	 Catch:{ Throwable -> 0x02b1 }
            goto L_0x02aa;	 Catch:{ Throwable -> 0x02b1 }
        L_0x02a1:
            if (r11 != 0) goto L_0x02aa;	 Catch:{ Throwable -> 0x02b1 }
        L_0x02a3:
            r2 = r12.inPurgeable;	 Catch:{ Throwable -> 0x02b1 }
            if (r2 == 0) goto L_0x02aa;	 Catch:{ Throwable -> 0x02b1 }
        L_0x02a7:
            org.telegram.messenger.Utilities.pinBitmap(r6);	 Catch:{ Throwable -> 0x02b1 }
        L_0x02aa:
            goto L_0x02be;
        L_0x02ab:
            r0 = move-exception;
            r26 = r2;
            r2 = r0;
            monitor-exit(r12);	 Catch:{ all -> 0x02b4 }
            throw r2;	 Catch:{ Throwable -> 0x02b1 }
        L_0x02b1:
            r0 = move-exception;
        L_0x02b2:
            r2 = r0;
            goto L_0x02bb;
        L_0x02b4:
            r0 = move-exception;
        L_0x02b5:
            r2 = r0;
            goto L_0x02af;
        L_0x02b7:
            r0 = move-exception;
            r26 = r2;
            r2 = r0;
            org.telegram.messenger.FileLog.e(r2);
            r27 = r10;
            r2 = r26;
            goto L_0x06af;
        L_0x02c5:
            r26 = r2;
            r2 = 0;
            r4 = r1.cacheImage;	 Catch:{ Throwable -> 0x06aa }
            r4 = r4.httpUrl;	 Catch:{ Throwable -> 0x06aa }
            if (r4 == 0) goto L_0x0359;
            r4 = r1.cacheImage;	 Catch:{ Throwable -> 0x0352 }
            r4 = r4.httpUrl;	 Catch:{ Throwable -> 0x0352 }
            r11 = "thumb://";	 Catch:{ Throwable -> 0x0352 }
            r4 = r4.startsWith(r11);	 Catch:{ Throwable -> 0x0352 }
            if (r4 == 0) goto L_0x0315;	 Catch:{ Throwable -> 0x0352 }
            r4 = r1.cacheImage;	 Catch:{ Throwable -> 0x0352 }
            r4 = r4.httpUrl;	 Catch:{ Throwable -> 0x0352 }
            r11 = ":";	 Catch:{ Throwable -> 0x0352 }
            r12 = 8;	 Catch:{ Throwable -> 0x0352 }
            r4 = r4.indexOf(r11, r12);	 Catch:{ Throwable -> 0x0352 }
            if (r4 < 0) goto L_0x0310;	 Catch:{ Throwable -> 0x0352 }
            r11 = r1.cacheImage;	 Catch:{ Throwable -> 0x0352 }
            r11 = r11.httpUrl;	 Catch:{ Throwable -> 0x0352 }
            r11 = r11.substring(r12, r4);	 Catch:{ Throwable -> 0x0352 }
            r11 = java.lang.Long.parseLong(r11);	 Catch:{ Throwable -> 0x0352 }
            r11 = java.lang.Long.valueOf(r11);	 Catch:{ Throwable -> 0x0352 }
            r5 = 0;
            r12 = r1.cacheImage;	 Catch:{ Throwable -> 0x030a }
            r12 = r12.httpUrl;	 Catch:{ Throwable -> 0x030a }
            r13 = r4 + 1;	 Catch:{ Throwable -> 0x030a }
            r12 = r12.substring(r13);	 Catch:{ Throwable -> 0x030a }
            r2 = r12;
            r42 = r11;
            r11 = r2;
            r2 = r42;
            goto L_0x0313;
        L_0x030a:
            r0 = move-exception;
            r27 = r10;
            r2 = r11;
            goto L_0x06af;
            r11 = r2;
            r2 = r26;
            r9 = 0;
            goto L_0x035c;
            r4 = r1.cacheImage;	 Catch:{ Throwable -> 0x0352 }
            r4 = r4.httpUrl;	 Catch:{ Throwable -> 0x0352 }
            r11 = "vthumb://";	 Catch:{ Throwable -> 0x0352 }
            r4 = r4.startsWith(r11);	 Catch:{ Throwable -> 0x0352 }
            if (r4 == 0) goto L_0x0344;	 Catch:{ Throwable -> 0x0352 }
            r4 = r1.cacheImage;	 Catch:{ Throwable -> 0x0352 }
            r4 = r4.httpUrl;	 Catch:{ Throwable -> 0x0352 }
            r11 = ":";	 Catch:{ Throwable -> 0x0352 }
            r12 = 9;	 Catch:{ Throwable -> 0x0352 }
            r4 = r4.indexOf(r11, r12);	 Catch:{ Throwable -> 0x0352 }
            if (r4 < 0) goto L_0x0342;	 Catch:{ Throwable -> 0x0352 }
            r11 = r1.cacheImage;	 Catch:{ Throwable -> 0x0352 }
            r11 = r11.httpUrl;	 Catch:{ Throwable -> 0x0352 }
            r11 = r11.substring(r12, r4);	 Catch:{ Throwable -> 0x0352 }
            r11 = java.lang.Long.parseLong(r11);	 Catch:{ Throwable -> 0x0352 }
            r11 = java.lang.Long.valueOf(r11);	 Catch:{ Throwable -> 0x0352 }
            r5 = 1;	 Catch:{ Throwable -> 0x0352 }
            r26 = r11;	 Catch:{ Throwable -> 0x0352 }
            r9 = 0;	 Catch:{ Throwable -> 0x0352 }
            goto L_0x0359;	 Catch:{ Throwable -> 0x0352 }
            r4 = r1.cacheImage;	 Catch:{ Throwable -> 0x0352 }
            r4 = r4.httpUrl;	 Catch:{ Throwable -> 0x0352 }
            r11 = "http";	 Catch:{ Throwable -> 0x0352 }
            r4 = r4.startsWith(r11);	 Catch:{ Throwable -> 0x0352 }
            if (r4 != 0) goto L_0x0359;
            r9 = 0;
            goto L_0x0359;
        L_0x0352:
            r0 = move-exception;
            r27 = r10;
            r2 = r26;
            goto L_0x06af;
            r11 = r2;
            r2 = r26;
            r4 = 20;
            if (r2 == 0) goto L_0x0361;
            r4 = 0;
            if (r4 == 0) goto L_0x039b;
            r12 = org.telegram.messenger.ImageLoader.this;	 Catch:{ Throwable -> 0x0394 }
            r12 = r12.lastCacheOutTime;	 Catch:{ Throwable -> 0x0394 }
            r14 = 0;	 Catch:{ Throwable -> 0x0394 }
            r21 = (r12 > r14 ? 1 : (r12 == r14 ? 0 : -1));	 Catch:{ Throwable -> 0x0394 }
            if (r21 == 0) goto L_0x039b;	 Catch:{ Throwable -> 0x0394 }
            r12 = org.telegram.messenger.ImageLoader.this;	 Catch:{ Throwable -> 0x0394 }
            r12 = r12.lastCacheOutTime;	 Catch:{ Throwable -> 0x0394 }
            r14 = java.lang.System.currentTimeMillis();	 Catch:{ Throwable -> 0x0394 }
            r28 = r9;
            r27 = r10;
            r9 = (long) r4;
            r21 = r14 - r9;
            r9 = (r12 > r21 ? 1 : (r12 == r21 ? 0 : -1));
            if (r9 <= 0) goto L_0x039f;
            r9 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Throwable -> 0x038f }
            r10 = 21;	 Catch:{ Throwable -> 0x038f }
            if (r9 >= r10) goto L_0x039f;	 Catch:{ Throwable -> 0x038f }
            r9 = (long) r4;	 Catch:{ Throwable -> 0x038f }
            java.lang.Thread.sleep(r9);	 Catch:{ Throwable -> 0x038f }
            goto L_0x039f;
        L_0x038f:
            r0 = move-exception;
            r9 = r28;
            goto L_0x06af;
        L_0x0394:
            r0 = move-exception;
            r28 = r9;
            r27 = r10;
            goto L_0x06af;
            r28 = r9;
            r27 = r10;
            r9 = org.telegram.messenger.ImageLoader.this;	 Catch:{ Throwable -> 0x06a2 }
            r12 = java.lang.System.currentTimeMillis();	 Catch:{ Throwable -> 0x06a2 }
            r9.lastCacheOutTime = r12;	 Catch:{ Throwable -> 0x06a2 }
            r9 = r1.sync;	 Catch:{ Throwable -> 0x06a2 }
            monitor-enter(r9);	 Catch:{ Throwable -> 0x06a2 }
            r10 = r1.isCancelled;	 Catch:{ all -> 0x068d }
            if (r10 == 0) goto L_0x03ba;
            monitor-exit(r9);	 Catch:{ all -> 0x03b1 }
            return;
        L_0x03b1:
            r0 = move-exception;
            r39 = r2;
            r32 = r4;
            r40 = r5;
            goto L_0x06a0;
            monitor-exit(r9);	 Catch:{ all -> 0x068d }
            r9 = new android.graphics.BitmapFactory$Options;	 Catch:{ Throwable -> 0x06a2 }
            r9.<init>();	 Catch:{ Throwable -> 0x06a2 }
            r10 = 1;	 Catch:{ Throwable -> 0x06a2 }
            r9.inSampleSize = r10;	 Catch:{ Throwable -> 0x06a2 }
            r10 = 0;	 Catch:{ Throwable -> 0x06a2 }
            r12 = 0;	 Catch:{ Throwable -> 0x06a2 }
            r13 = 0;	 Catch:{ Throwable -> 0x06a2 }
            r14 = r1.cacheImage;	 Catch:{ Throwable -> 0x06a2 }
            r14 = r14.filter;	 Catch:{ Throwable -> 0x06a2 }
            r21 = 0;
            if (r14 == 0) goto L_0x0479;
            r14 = r1.cacheImage;	 Catch:{ Throwable -> 0x038f }
            r14 = r14.filter;	 Catch:{ Throwable -> 0x038f }
            r15 = "_";	 Catch:{ Throwable -> 0x038f }
            r14 = r14.split(r15);	 Catch:{ Throwable -> 0x038f }
            r15 = r14.length;	 Catch:{ Throwable -> 0x038f }
            r3 = 2;	 Catch:{ Throwable -> 0x038f }
            if (r15 < r3) goto L_0x03f2;	 Catch:{ Throwable -> 0x038f }
            r3 = 0;	 Catch:{ Throwable -> 0x038f }
            r15 = r14[r3];	 Catch:{ Throwable -> 0x038f }
            r3 = java.lang.Float.parseFloat(r15);	 Catch:{ Throwable -> 0x038f }
            r15 = org.telegram.messenger.AndroidUtilities.density;	 Catch:{ Throwable -> 0x038f }
            r10 = r3 * r15;	 Catch:{ Throwable -> 0x038f }
            r3 = 1;	 Catch:{ Throwable -> 0x038f }
            r15 = r14[r3];	 Catch:{ Throwable -> 0x038f }
            r3 = java.lang.Float.parseFloat(r15);	 Catch:{ Throwable -> 0x038f }
            r15 = org.telegram.messenger.AndroidUtilities.density;	 Catch:{ Throwable -> 0x038f }
            r12 = r3 * r15;	 Catch:{ Throwable -> 0x038f }
            r3 = r1.cacheImage;	 Catch:{ Throwable -> 0x038f }
            r3 = r3.filter;	 Catch:{ Throwable -> 0x038f }
            r15 = "b";	 Catch:{ Throwable -> 0x038f }
            r3 = r3.contains(r15);	 Catch:{ Throwable -> 0x038f }
            if (r3 == 0) goto L_0x0400;	 Catch:{ Throwable -> 0x038f }
            r3 = 1;	 Catch:{ Throwable -> 0x038f }
            r13 = r3;	 Catch:{ Throwable -> 0x038f }
            r3 = (r10 > r21 ? 1 : (r10 == r21 ? 0 : -1));	 Catch:{ Throwable -> 0x038f }
            if (r3 == 0) goto L_0x0472;	 Catch:{ Throwable -> 0x038f }
            r3 = (r12 > r21 ? 1 : (r12 == r21 ? 0 : -1));	 Catch:{ Throwable -> 0x038f }
            if (r3 == 0) goto L_0x0472;	 Catch:{ Throwable -> 0x038f }
            r3 = 1;	 Catch:{ Throwable -> 0x038f }
            r9.inJustDecodeBounds = r3;	 Catch:{ Throwable -> 0x038f }
            if (r2 == 0) goto L_0x0437;	 Catch:{ Throwable -> 0x038f }
            if (r11 != 0) goto L_0x0437;	 Catch:{ Throwable -> 0x038f }
            if (r5 == 0) goto L_0x0424;	 Catch:{ Throwable -> 0x038f }
            r3 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Throwable -> 0x038f }
            r3 = r3.getContentResolver();	 Catch:{ Throwable -> 0x038f }
            r30 = r13;	 Catch:{ Throwable -> 0x038f }
            r29 = r14;	 Catch:{ Throwable -> 0x038f }
            r13 = r2.longValue();	 Catch:{ Throwable -> 0x038f }
            r15 = 1;	 Catch:{ Throwable -> 0x038f }
            android.provider.MediaStore.Video.Thumbnails.getThumbnail(r3, r13, r15, r9);	 Catch:{ Throwable -> 0x038f }
            goto L_0x0455;	 Catch:{ Throwable -> 0x038f }
            r30 = r13;	 Catch:{ Throwable -> 0x038f }
            r29 = r14;	 Catch:{ Throwable -> 0x038f }
            r3 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Throwable -> 0x038f }
            r3 = r3.getContentResolver();	 Catch:{ Throwable -> 0x038f }
            r13 = r2.longValue();	 Catch:{ Throwable -> 0x038f }
            r15 = 1;	 Catch:{ Throwable -> 0x038f }
            android.provider.MediaStore.Images.Thumbnails.getThumbnail(r3, r13, r15, r9);	 Catch:{ Throwable -> 0x038f }
            goto L_0x0455;	 Catch:{ Throwable -> 0x038f }
            r30 = r13;	 Catch:{ Throwable -> 0x038f }
            r29 = r14;	 Catch:{ Throwable -> 0x038f }
            if (r8 == 0) goto L_0x0447;	 Catch:{ Throwable -> 0x038f }
            r3 = new org.telegram.messenger.secretmedia.EncryptedFileInputStream;	 Catch:{ Throwable -> 0x038f }
            r13 = r1.cacheImage;	 Catch:{ Throwable -> 0x038f }
            r13 = r13.encryptionKeyPath;	 Catch:{ Throwable -> 0x038f }
            r3.<init>(r7, r13);	 Catch:{ Throwable -> 0x038f }
            goto L_0x044c;	 Catch:{ Throwable -> 0x038f }
            r3 = new java.io.FileInputStream;	 Catch:{ Throwable -> 0x038f }
            r3.<init>(r7);	 Catch:{ Throwable -> 0x038f }
            r13 = 0;	 Catch:{ Throwable -> 0x038f }
            r14 = android.graphics.BitmapFactory.decodeStream(r3, r13, r9);	 Catch:{ Throwable -> 0x038f }
            r6 = r14;	 Catch:{ Throwable -> 0x038f }
            r3.close();	 Catch:{ Throwable -> 0x038f }
            r3 = r9.outWidth;	 Catch:{ Throwable -> 0x038f }
            r3 = (float) r3;	 Catch:{ Throwable -> 0x038f }
            r13 = r9.outHeight;	 Catch:{ Throwable -> 0x038f }
            r13 = (float) r13;	 Catch:{ Throwable -> 0x038f }
            r14 = r3 / r10;	 Catch:{ Throwable -> 0x038f }
            r15 = r13 / r12;	 Catch:{ Throwable -> 0x038f }
            r14 = java.lang.Math.max(r14, r15);	 Catch:{ Throwable -> 0x038f }
            r15 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;	 Catch:{ Throwable -> 0x038f }
            r15 = (r14 > r15 ? 1 : (r14 == r15 ? 0 : -1));	 Catch:{ Throwable -> 0x038f }
            if (r15 >= 0) goto L_0x046b;	 Catch:{ Throwable -> 0x038f }
            r14 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;	 Catch:{ Throwable -> 0x038f }
            r15 = 0;	 Catch:{ Throwable -> 0x038f }
            r9.inJustDecodeBounds = r15;	 Catch:{ Throwable -> 0x038f }
            r15 = (int) r14;	 Catch:{ Throwable -> 0x038f }
            r9.inSampleSize = r15;	 Catch:{ Throwable -> 0x038f }
            goto L_0x0474;	 Catch:{ Throwable -> 0x038f }
            r30 = r13;	 Catch:{ Throwable -> 0x038f }
            r32 = r4;	 Catch:{ Throwable -> 0x038f }
            r13 = r30;	 Catch:{ Throwable -> 0x038f }
            goto L_0x04d2;	 Catch:{ Throwable -> 0x038f }
            if (r11 == 0) goto L_0x04d0;	 Catch:{ Throwable -> 0x038f }
            r3 = 1;	 Catch:{ Throwable -> 0x038f }
            r9.inJustDecodeBounds = r3;	 Catch:{ Throwable -> 0x038f }
            r3 = android.graphics.Bitmap.Config.RGB_565;	 Catch:{ Throwable -> 0x038f }
            r9.inPreferredConfig = r3;	 Catch:{ Throwable -> 0x038f }
            r3 = new java.io.FileInputStream;	 Catch:{ Throwable -> 0x038f }
            r3.<init>(r7);	 Catch:{ Throwable -> 0x038f }
            r14 = 0;	 Catch:{ Throwable -> 0x038f }
            r15 = android.graphics.BitmapFactory.decodeStream(r3, r14, r9);	 Catch:{ Throwable -> 0x038f }
            r6 = r15;
            r3.close();	 Catch:{ Throwable -> 0x04c9 }
            r14 = r9.outWidth;	 Catch:{ Throwable -> 0x04c9 }
            r15 = r9.outHeight;	 Catch:{ Throwable -> 0x04c9 }
            r31 = r3;	 Catch:{ Throwable -> 0x04c9 }
            r3 = 0;	 Catch:{ Throwable -> 0x04c9 }
            r9.inJustDecodeBounds = r3;	 Catch:{ Throwable -> 0x04c9 }
            r3 = r14 / 200;	 Catch:{ Throwable -> 0x04c9 }
            r32 = r4;	 Catch:{ Throwable -> 0x04c9 }
            r4 = r15 / 200;	 Catch:{ Throwable -> 0x04c9 }
            r3 = java.lang.Math.max(r3, r4);	 Catch:{ Throwable -> 0x04c9 }
            r3 = (float) r3;
            r4 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
            r4 = (r3 > r4 ? 1 : (r3 == r4 ? 0 : -1));
            if (r4 >= 0) goto L_0x04ac;
            r3 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
            r4 = 1;
            r18 = 2;
            r4 = r4 * 2;
            r33 = r6;
            r6 = r4 * 2;
            r6 = (float) r6;
            r6 = (r6 > r3 ? 1 : (r6 == r3 ? 0 : -1));
            if (r6 < 0) goto L_0x04c6;
            r9.inSampleSize = r4;	 Catch:{ Throwable -> 0x04bf }
            r6 = r33;
            goto L_0x04d2;
        L_0x04bf:
            r0 = move-exception;
            r9 = r28;
            r6 = r33;
            goto L_0x06af;
            r6 = r33;
            goto L_0x04ad;
        L_0x04c9:
            r0 = move-exception;
            r33 = r6;
            r9 = r28;
            goto L_0x06af;
            r32 = r4;
            r3 = r1.sync;	 Catch:{ Throwable -> 0x06a2 }
            monitor-enter(r3);	 Catch:{ Throwable -> 0x06a2 }
            r4 = r1.isCancelled;	 Catch:{ all -> 0x0682 }
            if (r4 == 0) goto L_0x04e2;
            monitor-exit(r3);	 Catch:{ all -> 0x04db }
            return;
        L_0x04db:
            r0 = move-exception;
            r39 = r2;
            r40 = r5;
            goto L_0x068b;
            monitor-exit(r3);	 Catch:{ all -> 0x0682 }
            r3 = r1.cacheImage;	 Catch:{ Throwable -> 0x06a2 }
            r3 = r3.filter;	 Catch:{ Throwable -> 0x06a2 }
            if (r3 == 0) goto L_0x04f7;
            if (r13 != 0) goto L_0x04f7;
            r3 = r1.cacheImage;	 Catch:{ Throwable -> 0x038f }
            r3 = r3.httpUrl;	 Catch:{ Throwable -> 0x038f }
            if (r3 == 0) goto L_0x04f2;	 Catch:{ Throwable -> 0x038f }
            goto L_0x04f7;	 Catch:{ Throwable -> 0x038f }
            r3 = android.graphics.Bitmap.Config.RGB_565;	 Catch:{ Throwable -> 0x038f }
            r9.inPreferredConfig = r3;	 Catch:{ Throwable -> 0x038f }
            goto L_0x04fb;
            r3 = android.graphics.Bitmap.Config.ARGB_8888;	 Catch:{ Throwable -> 0x06a2 }
            r9.inPreferredConfig = r3;	 Catch:{ Throwable -> 0x06a2 }
            r3 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Throwable -> 0x06a2 }
            r4 = 21;
            if (r3 >= r4) goto L_0x0504;
            r3 = 1;
            r9.inPurgeable = r3;	 Catch:{ Throwable -> 0x038f }
            r3 = 0;
            r9.inDither = r3;	 Catch:{ Throwable -> 0x06a2 }
            if (r2 == 0) goto L_0x052e;
            if (r11 != 0) goto L_0x052e;
            if (r5 == 0) goto L_0x051e;
            r3 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Throwable -> 0x038f }
            r3 = r3.getContentResolver();	 Catch:{ Throwable -> 0x038f }
            r14 = r2.longValue();	 Catch:{ Throwable -> 0x038f }
            r4 = 1;	 Catch:{ Throwable -> 0x038f }
            r3 = android.provider.MediaStore.Video.Thumbnails.getThumbnail(r3, r14, r4, r9);	 Catch:{ Throwable -> 0x038f }
            r6 = r3;	 Catch:{ Throwable -> 0x038f }
            goto L_0x052e;	 Catch:{ Throwable -> 0x038f }
            r3 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Throwable -> 0x038f }
            r3 = r3.getContentResolver();	 Catch:{ Throwable -> 0x038f }
            r14 = r2.longValue();	 Catch:{ Throwable -> 0x038f }
            r4 = 1;	 Catch:{ Throwable -> 0x038f }
            r3 = android.provider.MediaStore.Images.Thumbnails.getThumbnail(r3, r14, r4, r9);	 Catch:{ Throwable -> 0x038f }
            goto L_0x051c;
            if (r6 != 0) goto L_0x05df;
            if (r27 == 0) goto L_0x0579;
            r3 = new java.io.RandomAccessFile;	 Catch:{ Throwable -> 0x06a2 }
            r4 = "r";	 Catch:{ Throwable -> 0x06a2 }
            r3.<init>(r7, r4);	 Catch:{ Throwable -> 0x06a2 }
            r33 = r3.getChannel();	 Catch:{ Throwable -> 0x06a2 }
            r34 = java.nio.channels.FileChannel.MapMode.READ_ONLY;	 Catch:{ Throwable -> 0x06a2 }
            r35 = 0;	 Catch:{ Throwable -> 0x06a2 }
            r37 = r7.length();	 Catch:{ Throwable -> 0x06a2 }
            r4 = r33.map(r34, r35, r37);	 Catch:{ Throwable -> 0x06a2 }
            r14 = new android.graphics.BitmapFactory$Options;	 Catch:{ Throwable -> 0x06a2 }
            r14.<init>();	 Catch:{ Throwable -> 0x06a2 }
            r15 = 1;	 Catch:{ Throwable -> 0x06a2 }
            r14.inJustDecodeBounds = r15;	 Catch:{ Throwable -> 0x06a2 }
            r15 = r4.limit();	 Catch:{ Throwable -> 0x06a2 }
            r39 = r2;
            r40 = r5;
            r2 = 1;
            r5 = 0;
            org.telegram.messenger.Utilities.loadWebpImage(r5, r4, r15, r14, r2);	 Catch:{ Throwable -> 0x0697 }
            r2 = r14.outWidth;	 Catch:{ Throwable -> 0x0697 }
            r5 = r14.outHeight;	 Catch:{ Throwable -> 0x0697 }
            r15 = android.graphics.Bitmap.Config.ARGB_8888;	 Catch:{ Throwable -> 0x0697 }
            r2 = org.telegram.messenger.Bitmaps.createBitmap(r2, r5, r15);	 Catch:{ Throwable -> 0x0697 }
            r6 = r2;	 Catch:{ Throwable -> 0x0697 }
            r2 = r4.limit();	 Catch:{ Throwable -> 0x0697 }
            r5 = r9.inPurgeable;	 Catch:{ Throwable -> 0x0697 }
            r15 = 1;	 Catch:{ Throwable -> 0x0697 }
            r5 = r5 ^ r15;	 Catch:{ Throwable -> 0x0697 }
            r15 = 0;	 Catch:{ Throwable -> 0x0697 }
            org.telegram.messenger.Utilities.loadWebpImage(r6, r4, r2, r15, r5);	 Catch:{ Throwable -> 0x0697 }
            r3.close();	 Catch:{ Throwable -> 0x0697 }
            goto L_0x05c2;	 Catch:{ Throwable -> 0x0697 }
            r39 = r2;	 Catch:{ Throwable -> 0x0697 }
            r40 = r5;	 Catch:{ Throwable -> 0x0697 }
            r2 = r9.inPurgeable;	 Catch:{ Throwable -> 0x0697 }
            if (r2 == 0) goto L_0x05c4;	 Catch:{ Throwable -> 0x0697 }
            r2 = new java.io.RandomAccessFile;	 Catch:{ Throwable -> 0x0697 }
            r3 = "r";	 Catch:{ Throwable -> 0x0697 }
            r2.<init>(r7, r3);	 Catch:{ Throwable -> 0x0697 }
            r3 = r2.length();	 Catch:{ Throwable -> 0x0697 }
            r3 = (int) r3;	 Catch:{ Throwable -> 0x0697 }
            r4 = org.telegram.messenger.ImageLoader.bytes;	 Catch:{ Throwable -> 0x0697 }
            if (r4 == 0) goto L_0x059f;	 Catch:{ Throwable -> 0x0697 }
            r4 = org.telegram.messenger.ImageLoader.bytes;	 Catch:{ Throwable -> 0x0697 }
            r4 = r4.length;	 Catch:{ Throwable -> 0x0697 }
            if (r4 < r3) goto L_0x059f;	 Catch:{ Throwable -> 0x0697 }
            r4 = org.telegram.messenger.ImageLoader.bytes;	 Catch:{ Throwable -> 0x0697 }
            goto L_0x05a0;	 Catch:{ Throwable -> 0x0697 }
            r4 = 0;	 Catch:{ Throwable -> 0x0697 }
            if (r4 != 0) goto L_0x05a8;	 Catch:{ Throwable -> 0x0697 }
            r5 = new byte[r3];	 Catch:{ Throwable -> 0x0697 }
            r4 = r5;	 Catch:{ Throwable -> 0x0697 }
            org.telegram.messenger.ImageLoader.bytes = r5;	 Catch:{ Throwable -> 0x0697 }
            r5 = 0;	 Catch:{ Throwable -> 0x0697 }
            r2.readFully(r4, r5, r3);	 Catch:{ Throwable -> 0x0697 }
            r2.close();	 Catch:{ Throwable -> 0x0697 }
            if (r8 == 0) goto L_0x05ba;	 Catch:{ Throwable -> 0x0697 }
            r5 = r1.cacheImage;	 Catch:{ Throwable -> 0x0697 }
            r5 = r5.encryptionKeyPath;	 Catch:{ Throwable -> 0x0697 }
            r14 = 0;	 Catch:{ Throwable -> 0x0697 }
            org.telegram.messenger.secretmedia.EncryptedFileInputStream.decryptBytesWithKeyFile(r4, r14, r3, r5);	 Catch:{ Throwable -> 0x0697 }
            goto L_0x05bb;	 Catch:{ Throwable -> 0x0697 }
            r14 = 0;	 Catch:{ Throwable -> 0x0697 }
            r5 = android.graphics.BitmapFactory.decodeByteArray(r4, r14, r3, r9);	 Catch:{ Throwable -> 0x0697 }
            r2 = r5;	 Catch:{ Throwable -> 0x0697 }
            r6 = r2;	 Catch:{ Throwable -> 0x0697 }
            r4 = 0;	 Catch:{ Throwable -> 0x0697 }
            goto L_0x05e4;	 Catch:{ Throwable -> 0x0697 }
            if (r8 == 0) goto L_0x05d0;	 Catch:{ Throwable -> 0x0697 }
            r2 = new org.telegram.messenger.secretmedia.EncryptedFileInputStream;	 Catch:{ Throwable -> 0x0697 }
            r3 = r1.cacheImage;	 Catch:{ Throwable -> 0x0697 }
            r3 = r3.encryptionKeyPath;	 Catch:{ Throwable -> 0x0697 }
            r2.<init>(r7, r3);	 Catch:{ Throwable -> 0x0697 }
            goto L_0x05d5;	 Catch:{ Throwable -> 0x0697 }
            r2 = new java.io.FileInputStream;	 Catch:{ Throwable -> 0x0697 }
            r2.<init>(r7);	 Catch:{ Throwable -> 0x0697 }
            r4 = 0;	 Catch:{ Throwable -> 0x0697 }
            r3 = android.graphics.BitmapFactory.decodeStream(r2, r4, r9);	 Catch:{ Throwable -> 0x0697 }
            r6 = r3;	 Catch:{ Throwable -> 0x0697 }
            r2.close();	 Catch:{ Throwable -> 0x0697 }
            goto L_0x05e4;	 Catch:{ Throwable -> 0x0697 }
            r39 = r2;	 Catch:{ Throwable -> 0x0697 }
            r40 = r5;	 Catch:{ Throwable -> 0x0697 }
            r4 = 0;	 Catch:{ Throwable -> 0x0697 }
            if (r6 != 0) goto L_0x05fd;	 Catch:{ Throwable -> 0x0697 }
            if (r28 == 0) goto L_0x067a;	 Catch:{ Throwable -> 0x0697 }
            r2 = r7.length();	 Catch:{ Throwable -> 0x0697 }
            r14 = 0;	 Catch:{ Throwable -> 0x0697 }
            r5 = (r2 > r14 ? 1 : (r2 == r14 ? 0 : -1));	 Catch:{ Throwable -> 0x0697 }
            if (r5 == 0) goto L_0x05f8;	 Catch:{ Throwable -> 0x0697 }
            r2 = r1.cacheImage;	 Catch:{ Throwable -> 0x0697 }
            r2 = r2.filter;	 Catch:{ Throwable -> 0x0697 }
            if (r2 != 0) goto L_0x067a;	 Catch:{ Throwable -> 0x0697 }
            r7.delete();	 Catch:{ Throwable -> 0x0697 }
            goto L_0x067a;	 Catch:{ Throwable -> 0x0697 }
            r2 = 0;	 Catch:{ Throwable -> 0x0697 }
            r3 = r1.cacheImage;	 Catch:{ Throwable -> 0x0697 }
            r3 = r3.filter;	 Catch:{ Throwable -> 0x0697 }
            if (r3 == 0) goto L_0x066f;	 Catch:{ Throwable -> 0x0697 }
            r3 = r6.getWidth();	 Catch:{ Throwable -> 0x0697 }
            r3 = (float) r3;	 Catch:{ Throwable -> 0x0697 }
            r5 = r6.getHeight();	 Catch:{ Throwable -> 0x0697 }
            r5 = (float) r5;	 Catch:{ Throwable -> 0x0697 }
            r14 = r9.inPurgeable;	 Catch:{ Throwable -> 0x0697 }
            if (r14 != 0) goto L_0x0636;	 Catch:{ Throwable -> 0x0697 }
            r14 = (r10 > r21 ? 1 : (r10 == r21 ? 0 : -1));	 Catch:{ Throwable -> 0x0697 }
            if (r14 == 0) goto L_0x0636;	 Catch:{ Throwable -> 0x0697 }
            r14 = (r3 > r10 ? 1 : (r3 == r10 ? 0 : -1));	 Catch:{ Throwable -> 0x0697 }
            if (r14 == 0) goto L_0x0636;	 Catch:{ Throwable -> 0x0697 }
            r14 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;	 Catch:{ Throwable -> 0x0697 }
            r14 = r14 + r10;	 Catch:{ Throwable -> 0x0697 }
            r14 = (r3 > r14 ? 1 : (r3 == r14 ? 0 : -1));	 Catch:{ Throwable -> 0x0697 }
            if (r14 <= 0) goto L_0x0636;	 Catch:{ Throwable -> 0x0697 }
            r14 = r3 / r10;	 Catch:{ Throwable -> 0x0697 }
            r15 = (int) r10;	 Catch:{ Throwable -> 0x0697 }
            r4 = r5 / r14;	 Catch:{ Throwable -> 0x0697 }
            r4 = (int) r4;	 Catch:{ Throwable -> 0x0697 }
            r41 = r2;	 Catch:{ Throwable -> 0x0697 }
            r2 = 1;	 Catch:{ Throwable -> 0x0697 }
            r4 = org.telegram.messenger.Bitmaps.createScaledBitmap(r6, r15, r4, r2);	 Catch:{ Throwable -> 0x0697 }
            r2 = r4;	 Catch:{ Throwable -> 0x0697 }
            if (r6 == r2) goto L_0x0638;	 Catch:{ Throwable -> 0x0697 }
            r6.recycle();	 Catch:{ Throwable -> 0x0697 }
            goto L_0x0639;
            r41 = r2;
            r2 = r6;
            if (r2 == 0) goto L_0x066d;
            if (r13 == 0) goto L_0x066d;
            r4 = 1120403456; // 0x42c80000 float:100.0 double:5.53552857E-315;
            r6 = (r5 > r4 ? 1 : (r5 == r4 ? 0 : -1));
            if (r6 >= 0) goto L_0x066d;
            r4 = (r3 > r4 ? 1 : (r3 == r4 ? 0 : -1));
            if (r4 >= 0) goto L_0x066d;
            r4 = r2.getConfig();	 Catch:{ Throwable -> 0x066a }
            r6 = android.graphics.Bitmap.Config.ARGB_8888;	 Catch:{ Throwable -> 0x066a }
            if (r4 != r6) goto L_0x0665;	 Catch:{ Throwable -> 0x066a }
            r15 = 3;	 Catch:{ Throwable -> 0x066a }
            r4 = r9.inPurgeable;	 Catch:{ Throwable -> 0x066a }
            r6 = 1;	 Catch:{ Throwable -> 0x066a }
            r16 = r4 ^ 1;	 Catch:{ Throwable -> 0x066a }
            r17 = r2.getWidth();	 Catch:{ Throwable -> 0x066a }
            r18 = r2.getHeight();	 Catch:{ Throwable -> 0x066a }
            r19 = r2.getRowBytes();	 Catch:{ Throwable -> 0x066a }
            r14 = r2;	 Catch:{ Throwable -> 0x066a }
            org.telegram.messenger.Utilities.blurBitmap(r14, r15, r16, r17, r18, r19);	 Catch:{ Throwable -> 0x066a }
            r3 = 1;
            r6 = r2;
            r41 = r3;
            goto L_0x0671;
        L_0x066a:
            r0 = move-exception;
            r6 = r2;
            goto L_0x0698;
            r6 = r2;
            goto L_0x0671;
            r41 = r2;
            if (r41 != 0) goto L_0x067a;
            r2 = r9.inPurgeable;	 Catch:{ Throwable -> 0x0697 }
            if (r2 == 0) goto L_0x067a;	 Catch:{ Throwable -> 0x0697 }
            org.telegram.messenger.Utilities.pinBitmap(r6);	 Catch:{ Throwable -> 0x0697 }
            r9 = r28;
            r2 = r39;
            r5 = r40;
            goto L_0x06af;
        L_0x0682:
            r0 = move-exception;
            r39 = r2;
            r40 = r5;
            r2 = r0;
            monitor-exit(r3);	 Catch:{ all -> 0x068a }
            throw r2;	 Catch:{ Throwable -> 0x0697 }
        L_0x068a:
            r0 = move-exception;
            r2 = r0;
            goto L_0x0688;
        L_0x068d:
            r0 = move-exception;
            r39 = r2;
            r32 = r4;
            r40 = r5;
            r2 = r0;
            monitor-exit(r9);	 Catch:{ all -> 0x069f }
            throw r2;	 Catch:{ Throwable -> 0x0697 }
        L_0x0697:
            r0 = move-exception;
            r9 = r28;
            r2 = r39;
            r5 = r40;
            goto L_0x06af;
        L_0x069f:
            r0 = move-exception;
            r2 = r0;
            goto L_0x0695;
        L_0x06a2:
            r0 = move-exception;
            r39 = r2;
            r40 = r5;
            r9 = r28;
            goto L_0x06af;
        L_0x06aa:
            r0 = move-exception;
            r27 = r10;
            r2 = r26;
            java.lang.Thread.interrupted();
            if (r6 == 0) goto L_0x06ba;
            r13 = new android.graphics.drawable.BitmapDrawable;
            r13.<init>(r6);
            goto L_0x06bb;
            r13 = 0;
            r1.onPostExecute(r13);
        L_0x06be:
            return;
        L_0x06bf:
            r0 = move-exception;
            r3 = r0;
            monitor-exit(r2);	 Catch:{ all -> 0x06bf }
            throw r3;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.ImageLoader.CacheOutTask.run():void");
        }

        public CacheOutTask(CacheImage image) {
            this.cacheImage = image;
        }

        private void onPostExecute(final BitmapDrawable bitmapDrawable) {
            AndroidUtilities.runOnUIThread(new Runnable() {
                public void run() {
                    BitmapDrawable toSet = null;
                    if (bitmapDrawable instanceof AnimatedFileDrawable) {
                        toSet = bitmapDrawable;
                    } else if (bitmapDrawable != null) {
                        toSet = ImageLoader.this.memCache.get(CacheOutTask.this.cacheImage.key);
                        if (toSet == null) {
                            ImageLoader.this.memCache.put(CacheOutTask.this.cacheImage.key, bitmapDrawable);
                            toSet = bitmapDrawable;
                        } else {
                            bitmapDrawable.getBitmap().recycle();
                        }
                    }
                    final BitmapDrawable toSetFinal = toSet;
                    ImageLoader.this.imageLoadQueue.postRunnable(new Runnable() {
                        public void run() {
                            CacheOutTask.this.cacheImage.setImageAndClear(toSetFinal);
                        }
                    });
                }
            });
        }

        public void cancel() {
            synchronized (this.sync) {
                try {
                    this.isCancelled = true;
                    if (this.runningThread != null) {
                        this.runningThread.interrupt();
                    }
                } catch (Exception e) {
                }
            }
        }
    }

    private class HttpFileTask extends AsyncTask<Void, Void, Boolean> {
        private boolean canRetry = true;
        private int currentAccount;
        private String ext;
        private RandomAccessFile fileOutputStream = null;
        private int fileSize;
        private long lastProgressTime;
        private File tempFile;
        private String url;

        protected java.lang.Boolean doInBackground(java.lang.Void... r1) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.ImageLoader.HttpFileTask.doInBackground(java.lang.Void[]):java.lang.Boolean
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
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
            r0 = 0;
            r1 = 0;
            r2 = 0;
            r3 = r2;
            r4 = 0;
            r5 = new java.net.URL;	 Catch:{ Throwable -> 0x0072 }
            r6 = r12.url;	 Catch:{ Throwable -> 0x0072 }
            r5.<init>(r6);	 Catch:{ Throwable -> 0x0072 }
            r6 = r5.openConnection();	 Catch:{ Throwable -> 0x0072 }
            r3 = r6;	 Catch:{ Throwable -> 0x0072 }
            r6 = "User-Agent";	 Catch:{ Throwable -> 0x0072 }
            r7 = "Mozilla/5.0 (iPhone; CPU iPhone OS 10_0 like Mac OS X) AppleWebKit/602.1.38 (KHTML, like Gecko) Version/10.0 Mobile/14A5297c Safari/602.1";	 Catch:{ Throwable -> 0x0072 }
            r3.addRequestProperty(r6, r7);	 Catch:{ Throwable -> 0x0072 }
            r6 = 5000; // 0x1388 float:7.006E-42 double:2.4703E-320;	 Catch:{ Throwable -> 0x0072 }
            r3.setConnectTimeout(r6);	 Catch:{ Throwable -> 0x0072 }
            r3.setReadTimeout(r6);	 Catch:{ Throwable -> 0x0072 }
            r6 = r3 instanceof java.net.HttpURLConnection;	 Catch:{ Throwable -> 0x0072 }
            if (r6 == 0) goto L_0x005e;	 Catch:{ Throwable -> 0x0072 }
        L_0x0024:
            r6 = r3;	 Catch:{ Throwable -> 0x0072 }
            r6 = (java.net.HttpURLConnection) r6;	 Catch:{ Throwable -> 0x0072 }
            r7 = 1;	 Catch:{ Throwable -> 0x0072 }
            r6.setInstanceFollowRedirects(r7);	 Catch:{ Throwable -> 0x0072 }
            r7 = r6.getResponseCode();	 Catch:{ Throwable -> 0x0072 }
            r8 = 302; // 0x12e float:4.23E-43 double:1.49E-321;	 Catch:{ Throwable -> 0x0072 }
            if (r7 == r8) goto L_0x003b;	 Catch:{ Throwable -> 0x0072 }
        L_0x0033:
            r8 = 301; // 0x12d float:4.22E-43 double:1.487E-321;	 Catch:{ Throwable -> 0x0072 }
            if (r7 == r8) goto L_0x003b;	 Catch:{ Throwable -> 0x0072 }
        L_0x0037:
            r8 = 303; // 0x12f float:4.25E-43 double:1.497E-321;	 Catch:{ Throwable -> 0x0072 }
            if (r7 != r8) goto L_0x005e;	 Catch:{ Throwable -> 0x0072 }
        L_0x003b:
            r8 = "Location";	 Catch:{ Throwable -> 0x0072 }
            r8 = r6.getHeaderField(r8);	 Catch:{ Throwable -> 0x0072 }
            r9 = "Set-Cookie";	 Catch:{ Throwable -> 0x0072 }
            r9 = r6.getHeaderField(r9);	 Catch:{ Throwable -> 0x0072 }
            r10 = new java.net.URL;	 Catch:{ Throwable -> 0x0072 }
            r10.<init>(r8);	 Catch:{ Throwable -> 0x0072 }
            r5 = r10;	 Catch:{ Throwable -> 0x0072 }
            r10 = r5.openConnection();	 Catch:{ Throwable -> 0x0072 }
            r3 = r10;	 Catch:{ Throwable -> 0x0072 }
            r10 = "Cookie";	 Catch:{ Throwable -> 0x0072 }
            r3.setRequestProperty(r10, r9);	 Catch:{ Throwable -> 0x0072 }
            r10 = "User-Agent";	 Catch:{ Throwable -> 0x0072 }
            r11 = "Mozilla/5.0 (iPhone; CPU iPhone OS 10_0 like Mac OS X) AppleWebKit/602.1.38 (KHTML, like Gecko) Version/10.0 Mobile/14A5297c Safari/602.1";	 Catch:{ Throwable -> 0x0072 }
            r3.addRequestProperty(r10, r11);	 Catch:{ Throwable -> 0x0072 }
        L_0x005e:
            r3.connect();	 Catch:{ Throwable -> 0x0072 }
            r6 = r3.getInputStream();	 Catch:{ Throwable -> 0x0072 }
            r0 = r6;	 Catch:{ Throwable -> 0x0072 }
            r6 = new java.io.RandomAccessFile;	 Catch:{ Throwable -> 0x0072 }
            r7 = r12.tempFile;	 Catch:{ Throwable -> 0x0072 }
            r8 = "rws";	 Catch:{ Throwable -> 0x0072 }
            r6.<init>(r7, r8);	 Catch:{ Throwable -> 0x0072 }
            r12.fileOutputStream = r6;	 Catch:{ Throwable -> 0x0072 }
            goto L_0x00a9;
        L_0x0072:
            r5 = move-exception;
            r6 = r5 instanceof java.net.SocketTimeoutException;
            if (r6 == 0) goto L_0x0080;
        L_0x0077:
            r6 = org.telegram.tgnet.ConnectionsManager.isNetworkOnline();
            if (r6 == 0) goto L_0x00a6;
        L_0x007d:
            r12.canRetry = r4;
            goto L_0x00a6;
        L_0x0080:
            r6 = r5 instanceof java.net.UnknownHostException;
            if (r6 == 0) goto L_0x0087;
        L_0x0084:
            r12.canRetry = r4;
            goto L_0x00a6;
        L_0x0087:
            r6 = r5 instanceof java.net.SocketException;
            if (r6 == 0) goto L_0x00a0;
        L_0x008b:
            r6 = r5.getMessage();
            if (r6 == 0) goto L_0x00a6;
        L_0x0091:
            r6 = r5.getMessage();
            r7 = "ECONNRESET";
            r6 = r6.contains(r7);
            if (r6 == 0) goto L_0x00a6;
        L_0x009d:
            r12.canRetry = r4;
            goto L_0x00a6;
        L_0x00a0:
            r6 = r5 instanceof java.io.FileNotFoundException;
            if (r6 == 0) goto L_0x00a6;
        L_0x00a4:
            r12.canRetry = r4;
        L_0x00a6:
            org.telegram.messenger.FileLog.e(r5);
        L_0x00a9:
            r5 = r12.canRetry;
            if (r5 == 0) goto L_0x015b;
        L_0x00ad:
            if (r3 == 0) goto L_0x00ce;
        L_0x00af:
            r5 = r3 instanceof java.net.HttpURLConnection;	 Catch:{ Exception -> 0x00c9 }
            if (r5 == 0) goto L_0x00ce;	 Catch:{ Exception -> 0x00c9 }
            r5 = r3;	 Catch:{ Exception -> 0x00c9 }
            r5 = (java.net.HttpURLConnection) r5;	 Catch:{ Exception -> 0x00c9 }
            r5 = r5.getResponseCode();	 Catch:{ Exception -> 0x00c9 }
            r6 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;	 Catch:{ Exception -> 0x00c9 }
            if (r5 == r6) goto L_0x00ce;	 Catch:{ Exception -> 0x00c9 }
            r6 = 202; // 0xca float:2.83E-43 double:1.0E-321;	 Catch:{ Exception -> 0x00c9 }
            if (r5 == r6) goto L_0x00ce;	 Catch:{ Exception -> 0x00c9 }
            r6 = 304; // 0x130 float:4.26E-43 double:1.5E-321;	 Catch:{ Exception -> 0x00c9 }
            if (r5 == r6) goto L_0x00ce;	 Catch:{ Exception -> 0x00c9 }
            r12.canRetry = r4;	 Catch:{ Exception -> 0x00c9 }
            goto L_0x00ce;
        L_0x00c9:
            r5 = move-exception;
            org.telegram.messenger.FileLog.e(r5);
            goto L_0x00cf;
            if (r3 == 0) goto L_0x00fe;
            r5 = r3.getHeaderFields();	 Catch:{ Exception -> 0x00fa }
            if (r5 == 0) goto L_0x00f9;	 Catch:{ Exception -> 0x00fa }
            r6 = "content-Length";	 Catch:{ Exception -> 0x00fa }
            r6 = r5.get(r6);	 Catch:{ Exception -> 0x00fa }
            r6 = (java.util.List) r6;	 Catch:{ Exception -> 0x00fa }
            if (r6 == 0) goto L_0x00f9;	 Catch:{ Exception -> 0x00fa }
            r7 = r6.isEmpty();	 Catch:{ Exception -> 0x00fa }
            if (r7 != 0) goto L_0x00f9;	 Catch:{ Exception -> 0x00fa }
            r7 = r6.get(r4);	 Catch:{ Exception -> 0x00fa }
            r7 = (java.lang.String) r7;	 Catch:{ Exception -> 0x00fa }
            if (r7 == 0) goto L_0x00f9;	 Catch:{ Exception -> 0x00fa }
            r8 = org.telegram.messenger.Utilities.parseInt(r7);	 Catch:{ Exception -> 0x00fa }
            r8 = r8.intValue();	 Catch:{ Exception -> 0x00fa }
            r12.fileSize = r8;	 Catch:{ Exception -> 0x00fa }
            goto L_0x00fe;
        L_0x00fa:
            r5 = move-exception;
            org.telegram.messenger.FileLog.e(r5);
            if (r0 == 0) goto L_0x013f;
            r5 = 32768; // 0x8000 float:4.5918E-41 double:1.61895E-319;
            r5 = new byte[r5];	 Catch:{ Throwable -> 0x013b }
            r6 = r4;	 Catch:{ Throwable -> 0x013b }
            r7 = r12.isCancelled();	 Catch:{ Throwable -> 0x013b }
            if (r7 == 0) goto L_0x010d;
            goto L_0x013a;
            r7 = r0.read(r5);	 Catch:{ Exception -> 0x0135 }
            if (r7 <= 0) goto L_0x0126;	 Catch:{ Exception -> 0x0135 }
            r8 = r12.fileOutputStream;	 Catch:{ Exception -> 0x0135 }
            r8.write(r5, r4, r7);	 Catch:{ Exception -> 0x0135 }
            r6 = r6 + r7;	 Catch:{ Exception -> 0x0135 }
            r8 = r12.fileSize;	 Catch:{ Exception -> 0x0135 }
            if (r8 <= 0) goto L_0x0125;	 Catch:{ Exception -> 0x0135 }
            r8 = (float) r6;	 Catch:{ Exception -> 0x0135 }
            r9 = r12.fileSize;	 Catch:{ Exception -> 0x0135 }
            r9 = (float) r9;	 Catch:{ Exception -> 0x0135 }
            r8 = r8 / r9;	 Catch:{ Exception -> 0x0135 }
            r12.reportProgress(r8);	 Catch:{ Exception -> 0x0135 }
            goto L_0x0106;	 Catch:{ Exception -> 0x0135 }
            r4 = -1;	 Catch:{ Exception -> 0x0135 }
            if (r7 != r4) goto L_0x0134;	 Catch:{ Exception -> 0x0135 }
            r1 = 1;	 Catch:{ Exception -> 0x0135 }
            r4 = r12.fileSize;	 Catch:{ Exception -> 0x0135 }
            if (r4 == 0) goto L_0x0133;	 Catch:{ Exception -> 0x0135 }
            r4 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;	 Catch:{ Exception -> 0x0135 }
            r12.reportProgress(r4);	 Catch:{ Exception -> 0x0135 }
            goto L_0x013a;
            goto L_0x013a;
        L_0x0135:
            r4 = move-exception;
            org.telegram.messenger.FileLog.e(r4);	 Catch:{ Throwable -> 0x013b }
            goto L_0x013f;
        L_0x013b:
            r4 = move-exception;
            org.telegram.messenger.FileLog.e(r4);
            r4 = r12.fileOutputStream;	 Catch:{ Throwable -> 0x014b }
            if (r4 == 0) goto L_0x014a;	 Catch:{ Throwable -> 0x014b }
            r4 = r12.fileOutputStream;	 Catch:{ Throwable -> 0x014b }
            r4.close();	 Catch:{ Throwable -> 0x014b }
            r12.fileOutputStream = r2;	 Catch:{ Throwable -> 0x014b }
            goto L_0x014f;
        L_0x014b:
            r2 = move-exception;
            org.telegram.messenger.FileLog.e(r2);
            if (r0 == 0) goto L_0x015a;
            r0.close();	 Catch:{ Throwable -> 0x0155 }
            goto L_0x015a;
        L_0x0155:
            r2 = move-exception;
            org.telegram.messenger.FileLog.e(r2);
            goto L_0x015b;
        L_0x015b:
            r2 = java.lang.Boolean.valueOf(r1);
            return r2;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.ImageLoader.HttpFileTask.doInBackground(java.lang.Void[]):java.lang.Boolean");
        }

        public HttpFileTask(String url, File tempFile, String ext, int currentAccount) {
            this.url = url;
            this.tempFile = tempFile;
            this.ext = ext;
            this.currentAccount = currentAccount;
        }

        private void reportProgress(final float progress) {
            long currentTime = System.currentTimeMillis();
            if (progress == 1.0f || this.lastProgressTime == 0 || this.lastProgressTime < currentTime - 500) {
                this.lastProgressTime = currentTime;
                Utilities.stageQueue.postRunnable(new Runnable() {
                    public void run() {
                        ImageLoader.this.fileProgresses.put(HttpFileTask.this.url, Float.valueOf(progress));
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            public void run() {
                                NotificationCenter.getInstance(HttpFileTask.this.currentAccount).postNotificationName(NotificationCenter.FileLoadProgressChanged, HttpFileTask.this.url, Float.valueOf(progress));
                            }
                        });
                    }
                });
            }
        }

        protected void onPostExecute(Boolean result) {
            ImageLoader.this.runHttpFileLoadTasks(this, result.booleanValue() ? 2 : 1);
        }

        protected void onCancelled() {
            ImageLoader.this.runHttpFileLoadTasks(this, 2);
        }
    }

    private class HttpImageTask extends AsyncTask<Void, Void, Boolean> {
        private CacheImage cacheImage = null;
        private boolean canRetry = true;
        private RandomAccessFile fileOutputStream = null;
        private HttpURLConnection httpConnection = null;
        private int imageSize;
        private long lastProgressTime;

        protected java.lang.Boolean doInBackground(java.lang.Void... r1) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.ImageLoader.HttpImageTask.doInBackground(java.lang.Void[]):java.lang.Boolean
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
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
            r0 = 0;
            r1 = 0;
            r2 = r8.isCancelled();
            r3 = 0;
            if (r2 != 0) goto L_0x008c;
        L_0x0009:
            r2 = new java.net.URL;	 Catch:{ Throwable -> 0x0055 }
            r4 = r8.cacheImage;	 Catch:{ Throwable -> 0x0055 }
            r4 = r4.httpUrl;	 Catch:{ Throwable -> 0x0055 }
            r2.<init>(r4);	 Catch:{ Throwable -> 0x0055 }
            r4 = r2.openConnection();	 Catch:{ Throwable -> 0x0055 }
            r4 = (java.net.HttpURLConnection) r4;	 Catch:{ Throwable -> 0x0055 }
            r8.httpConnection = r4;	 Catch:{ Throwable -> 0x0055 }
            r4 = r8.httpConnection;	 Catch:{ Throwable -> 0x0055 }
            r5 = "User-Agent";	 Catch:{ Throwable -> 0x0055 }
            r6 = "Mozilla/5.0 (iPhone; CPU iPhone OS 10_0 like Mac OS X) AppleWebKit/602.1.38 (KHTML, like Gecko) Version/10.0 Mobile/14A5297c Safari/602.1";	 Catch:{ Throwable -> 0x0055 }
            r4.addRequestProperty(r5, r6);	 Catch:{ Throwable -> 0x0055 }
            r4 = r8.httpConnection;	 Catch:{ Throwable -> 0x0055 }
            r5 = 5000; // 0x1388 float:7.006E-42 double:2.4703E-320;	 Catch:{ Throwable -> 0x0055 }
            r4.setConnectTimeout(r5);	 Catch:{ Throwable -> 0x0055 }
            r4 = r8.httpConnection;	 Catch:{ Throwable -> 0x0055 }
            r4.setReadTimeout(r5);	 Catch:{ Throwable -> 0x0055 }
            r4 = r8.httpConnection;	 Catch:{ Throwable -> 0x0055 }
            r5 = 1;	 Catch:{ Throwable -> 0x0055 }
            r4.setInstanceFollowRedirects(r5);	 Catch:{ Throwable -> 0x0055 }
            r4 = r8.isCancelled();	 Catch:{ Throwable -> 0x0055 }
            if (r4 != 0) goto L_0x0054;	 Catch:{ Throwable -> 0x0055 }
        L_0x003b:
            r4 = r8.httpConnection;	 Catch:{ Throwable -> 0x0055 }
            r4.connect();	 Catch:{ Throwable -> 0x0055 }
            r4 = r8.httpConnection;	 Catch:{ Throwable -> 0x0055 }
            r4 = r4.getInputStream();	 Catch:{ Throwable -> 0x0055 }
            r0 = r4;	 Catch:{ Throwable -> 0x0055 }
            r4 = new java.io.RandomAccessFile;	 Catch:{ Throwable -> 0x0055 }
            r5 = r8.cacheImage;	 Catch:{ Throwable -> 0x0055 }
            r5 = r5.tempFilePath;	 Catch:{ Throwable -> 0x0055 }
            r6 = "rws";	 Catch:{ Throwable -> 0x0055 }
            r4.<init>(r5, r6);	 Catch:{ Throwable -> 0x0055 }
            r8.fileOutputStream = r4;	 Catch:{ Throwable -> 0x0055 }
        L_0x0054:
            goto L_0x008c;
        L_0x0055:
            r2 = move-exception;
            r4 = r2 instanceof java.net.SocketTimeoutException;
            if (r4 == 0) goto L_0x0063;
        L_0x005a:
            r4 = org.telegram.tgnet.ConnectionsManager.isNetworkOnline();
            if (r4 == 0) goto L_0x0089;
        L_0x0060:
            r8.canRetry = r3;
            goto L_0x0089;
        L_0x0063:
            r4 = r2 instanceof java.net.UnknownHostException;
            if (r4 == 0) goto L_0x006a;
        L_0x0067:
            r8.canRetry = r3;
            goto L_0x0089;
        L_0x006a:
            r4 = r2 instanceof java.net.SocketException;
            if (r4 == 0) goto L_0x0083;
        L_0x006e:
            r4 = r2.getMessage();
            if (r4 == 0) goto L_0x0089;
        L_0x0074:
            r4 = r2.getMessage();
            r5 = "ECONNRESET";
            r4 = r4.contains(r5);
            if (r4 == 0) goto L_0x0089;
        L_0x0080:
            r8.canRetry = r3;
            goto L_0x0089;
        L_0x0083:
            r4 = r2 instanceof java.io.FileNotFoundException;
            if (r4 == 0) goto L_0x0089;
        L_0x0087:
            r8.canRetry = r3;
        L_0x0089:
            org.telegram.messenger.FileLog.e(r2);
        L_0x008c:
            r2 = r8.isCancelled();
            if (r2 != 0) goto L_0x012c;
        L_0x0092:
            r2 = r8.httpConnection;	 Catch:{ Exception -> 0x00b1 }
            if (r2 == 0) goto L_0x00b0;	 Catch:{ Exception -> 0x00b1 }
        L_0x0096:
            r2 = r8.httpConnection;	 Catch:{ Exception -> 0x00b1 }
            r2 = r2 instanceof java.net.HttpURLConnection;	 Catch:{ Exception -> 0x00b1 }
            if (r2 == 0) goto L_0x00b0;	 Catch:{ Exception -> 0x00b1 }
        L_0x009c:
            r2 = r8.httpConnection;	 Catch:{ Exception -> 0x00b1 }
            r2 = r2.getResponseCode();	 Catch:{ Exception -> 0x00b1 }
            r4 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;	 Catch:{ Exception -> 0x00b1 }
            if (r2 == r4) goto L_0x00b0;	 Catch:{ Exception -> 0x00b1 }
        L_0x00a6:
            r4 = 202; // 0xca float:2.83E-43 double:1.0E-321;	 Catch:{ Exception -> 0x00b1 }
            if (r2 == r4) goto L_0x00b0;	 Catch:{ Exception -> 0x00b1 }
        L_0x00aa:
            r4 = 304; // 0x130 float:4.26E-43 double:1.5E-321;	 Catch:{ Exception -> 0x00b1 }
            if (r2 == r4) goto L_0x00b0;	 Catch:{ Exception -> 0x00b1 }
        L_0x00ae:
            r8.canRetry = r3;	 Catch:{ Exception -> 0x00b1 }
        L_0x00b0:
            goto L_0x00b5;
        L_0x00b1:
            r2 = move-exception;
            org.telegram.messenger.FileLog.e(r2);
        L_0x00b5:
            r2 = r8.imageSize;
            if (r2 != 0) goto L_0x00ec;
        L_0x00b9:
            r2 = r8.httpConnection;
            if (r2 == 0) goto L_0x00ec;
        L_0x00bd:
            r2 = r8.httpConnection;	 Catch:{ Exception -> 0x00e8 }
            r2 = r2.getHeaderFields();	 Catch:{ Exception -> 0x00e8 }
            if (r2 == 0) goto L_0x00e7;	 Catch:{ Exception -> 0x00e8 }
        L_0x00c5:
            r4 = "content-Length";	 Catch:{ Exception -> 0x00e8 }
            r4 = r2.get(r4);	 Catch:{ Exception -> 0x00e8 }
            r4 = (java.util.List) r4;	 Catch:{ Exception -> 0x00e8 }
            if (r4 == 0) goto L_0x00e7;	 Catch:{ Exception -> 0x00e8 }
        L_0x00cf:
            r5 = r4.isEmpty();	 Catch:{ Exception -> 0x00e8 }
            if (r5 != 0) goto L_0x00e7;	 Catch:{ Exception -> 0x00e8 }
        L_0x00d5:
            r5 = r4.get(r3);	 Catch:{ Exception -> 0x00e8 }
            r5 = (java.lang.String) r5;	 Catch:{ Exception -> 0x00e8 }
            if (r5 == 0) goto L_0x00e7;	 Catch:{ Exception -> 0x00e8 }
        L_0x00dd:
            r6 = org.telegram.messenger.Utilities.parseInt(r5);	 Catch:{ Exception -> 0x00e8 }
            r6 = r6.intValue();	 Catch:{ Exception -> 0x00e8 }
            r8.imageSize = r6;	 Catch:{ Exception -> 0x00e8 }
        L_0x00e7:
            goto L_0x00ec;
        L_0x00e8:
            r2 = move-exception;
            org.telegram.messenger.FileLog.e(r2);
        L_0x00ec:
            if (r0 == 0) goto L_0x012c;
        L_0x00ee:
            r2 = 8192; // 0x2000 float:1.14794E-41 double:4.0474E-320;
            r2 = new byte[r2];	 Catch:{ Throwable -> 0x0128 }
            r4 = r3;	 Catch:{ Throwable -> 0x0128 }
        L_0x00f3:
            r5 = r8.isCancelled();	 Catch:{ Throwable -> 0x0128 }
            if (r5 == 0) goto L_0x00fa;
        L_0x00f9:
            goto L_0x0127;
        L_0x00fa:
            r5 = r0.read(r2);	 Catch:{ Exception -> 0x0122 }
            if (r5 <= 0) goto L_0x0113;	 Catch:{ Exception -> 0x0122 }
        L_0x0100:
            r4 = r4 + r5;	 Catch:{ Exception -> 0x0122 }
            r6 = r8.fileOutputStream;	 Catch:{ Exception -> 0x0122 }
            r6.write(r2, r3, r5);	 Catch:{ Exception -> 0x0122 }
            r6 = r8.imageSize;	 Catch:{ Exception -> 0x0122 }
            if (r6 == 0) goto L_0x0112;	 Catch:{ Exception -> 0x0122 }
        L_0x010a:
            r6 = (float) r4;	 Catch:{ Exception -> 0x0122 }
            r7 = r8.imageSize;	 Catch:{ Exception -> 0x0122 }
            r7 = (float) r7;	 Catch:{ Exception -> 0x0122 }
            r6 = r6 / r7;	 Catch:{ Exception -> 0x0122 }
            r8.reportProgress(r6);	 Catch:{ Exception -> 0x0122 }
        L_0x0112:
            goto L_0x00f3;	 Catch:{ Exception -> 0x0122 }
        L_0x0113:
            r3 = -1;	 Catch:{ Exception -> 0x0122 }
            if (r5 != r3) goto L_0x0121;	 Catch:{ Exception -> 0x0122 }
        L_0x0116:
            r1 = 1;	 Catch:{ Exception -> 0x0122 }
            r3 = r8.imageSize;	 Catch:{ Exception -> 0x0122 }
            if (r3 == 0) goto L_0x0120;	 Catch:{ Exception -> 0x0122 }
        L_0x011b:
            r3 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;	 Catch:{ Exception -> 0x0122 }
            r8.reportProgress(r3);	 Catch:{ Exception -> 0x0122 }
        L_0x0120:
            goto L_0x0127;
        L_0x0121:
            goto L_0x0127;
        L_0x0122:
            r3 = move-exception;
            org.telegram.messenger.FileLog.e(r3);	 Catch:{ Throwable -> 0x0128 }
        L_0x0127:
            goto L_0x012c;
        L_0x0128:
            r2 = move-exception;
            org.telegram.messenger.FileLog.e(r2);
        L_0x012c:
            r2 = r8.fileOutputStream;	 Catch:{ Throwable -> 0x0139 }
            if (r2 == 0) goto L_0x0138;	 Catch:{ Throwable -> 0x0139 }
        L_0x0130:
            r2 = r8.fileOutputStream;	 Catch:{ Throwable -> 0x0139 }
            r2.close();	 Catch:{ Throwable -> 0x0139 }
            r2 = 0;	 Catch:{ Throwable -> 0x0139 }
            r8.fileOutputStream = r2;	 Catch:{ Throwable -> 0x0139 }
        L_0x0138:
            goto L_0x013d;
        L_0x0139:
            r2 = move-exception;
            org.telegram.messenger.FileLog.e(r2);
        L_0x013d:
            r2 = r8.httpConnection;	 Catch:{ Throwable -> 0x0147 }
            if (r2 == 0) goto L_0x0146;	 Catch:{ Throwable -> 0x0147 }
        L_0x0141:
            r2 = r8.httpConnection;	 Catch:{ Throwable -> 0x0147 }
            r2.disconnect();	 Catch:{ Throwable -> 0x0147 }
        L_0x0146:
            goto L_0x0148;
        L_0x0147:
            r2 = move-exception;
        L_0x0148:
            if (r0 == 0) goto L_0x0153;
        L_0x014a:
            r0.close();	 Catch:{ Throwable -> 0x014e }
            goto L_0x0153;
        L_0x014e:
            r2 = move-exception;
            org.telegram.messenger.FileLog.e(r2);
            goto L_0x0154;
            if (r1 == 0) goto L_0x0172;
            r2 = r8.cacheImage;
            r2 = r2.tempFilePath;
            if (r2 == 0) goto L_0x0172;
            r2 = r8.cacheImage;
            r2 = r2.tempFilePath;
            r3 = r8.cacheImage;
            r3 = r3.finalFilePath;
            r2 = r2.renameTo(r3);
            if (r2 != 0) goto L_0x0172;
            r2 = r8.cacheImage;
            r3 = r8.cacheImage;
            r3 = r3.tempFilePath;
            r2.finalFilePath = r3;
            r2 = java.lang.Boolean.valueOf(r1);
            return r2;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.ImageLoader.HttpImageTask.doInBackground(java.lang.Void[]):java.lang.Boolean");
        }

        public HttpImageTask(CacheImage cacheImage, int size) {
            this.cacheImage = cacheImage;
            this.imageSize = size;
        }

        private void reportProgress(final float progress) {
            long currentTime = System.currentTimeMillis();
            if (progress == 1.0f || this.lastProgressTime == 0 || this.lastProgressTime < currentTime - 500) {
                this.lastProgressTime = currentTime;
                Utilities.stageQueue.postRunnable(new Runnable() {
                    public void run() {
                        ImageLoader.this.fileProgresses.put(HttpImageTask.this.cacheImage.url, Float.valueOf(progress));
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            public void run() {
                                NotificationCenter.getInstance(HttpImageTask.this.cacheImage.currentAccount).postNotificationName(NotificationCenter.FileLoadProgressChanged, HttpImageTask.this.cacheImage.url, Float.valueOf(progress));
                            }
                        });
                    }
                });
            }
        }

        protected void onPostExecute(final Boolean result) {
            if (!result.booleanValue()) {
                if (this.canRetry) {
                    ImageLoader.this.httpFileLoadError(this.cacheImage.url);
                    Utilities.stageQueue.postRunnable(new Runnable() {
                        public void run() {
                            ImageLoader.this.fileProgresses.remove(HttpImageTask.this.cacheImage.url);
                            AndroidUtilities.runOnUIThread(new Runnable() {
                                public void run() {
                                    if (result.booleanValue()) {
                                        NotificationCenter.getInstance(HttpImageTask.this.cacheImage.currentAccount).postNotificationName(NotificationCenter.FileDidLoaded, HttpImageTask.this.cacheImage.url);
                                        return;
                                    }
                                    NotificationCenter.getInstance(HttpImageTask.this.cacheImage.currentAccount).postNotificationName(NotificationCenter.FileDidFailedLoad, HttpImageTask.this.cacheImage.url, Integer.valueOf(2));
                                }
                            });
                        }
                    });
                    ImageLoader.this.imageLoadQueue.postRunnable(new Runnable() {
                        public void run() {
                            ImageLoader.this.runHttpTasks(true);
                        }
                    });
                }
            }
            ImageLoader.this.fileDidLoaded(this.cacheImage.url, this.cacheImage.finalFilePath, 0);
            Utilities.stageQueue.postRunnable(/* anonymous class already generated */);
            ImageLoader.this.imageLoadQueue.postRunnable(/* anonymous class already generated */);
        }

        protected void onCancelled() {
            ImageLoader.this.imageLoadQueue.postRunnable(new Runnable() {
                public void run() {
                    ImageLoader.this.runHttpTasks(true);
                }
            });
            Utilities.stageQueue.postRunnable(new Runnable() {
                public void run() {
                    ImageLoader.this.fileProgresses.remove(HttpImageTask.this.cacheImage.url);
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        public void run() {
                            NotificationCenter.getInstance(HttpImageTask.this.cacheImage.currentAccount).postNotificationName(NotificationCenter.FileDidFailedLoad, HttpImageTask.this.cacheImage.url, Integer.valueOf(1));
                        }
                    });
                }
            });
        }
    }

    private class ThumbGenerateInfo {
        private int count;
        private FileLocation fileLocation;
        private String filter;

        private ThumbGenerateInfo() {
        }
    }

    private class ThumbGenerateTask implements Runnable {
        private String filter;
        private int mediaType;
        private File originalPath;
        private FileLocation thumbLocation;

        public ThumbGenerateTask(int type, File path, FileLocation location, String f) {
            this.mediaType = type;
            this.originalPath = path;
            this.thumbLocation = location;
            this.filter = f;
        }

        private void removeTask() {
            if (this.thumbLocation != null) {
                final String name = FileLoader.getAttachFileName(this.thumbLocation);
                ImageLoader.this.imageLoadQueue.postRunnable(new Runnable() {
                    public void run() {
                        ImageLoader.this.thumbGenerateTasks.remove(name);
                    }
                });
            }
        }

        public void run() {
            try {
                if (this.thumbLocation == null) {
                    removeTask();
                    return;
                }
                String key = new StringBuilder();
                key.append(this.thumbLocation.volume_id);
                key.append("_");
                key.append(this.thumbLocation.local_id);
                key = key.toString();
                File directory = FileLoader.getDirectory(4);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("q_");
                stringBuilder.append(key);
                stringBuilder.append(".jpg");
                File thumbFile = new File(directory, stringBuilder.toString());
                if (!thumbFile.exists()) {
                    if (this.originalPath.exists()) {
                        int size = Math.min(180, Math.min(AndroidUtilities.displaySize.x, AndroidUtilities.displaySize.y) / 4);
                        Bitmap originalBitmap = null;
                        if (this.mediaType == 0) {
                            originalBitmap = ImageLoader.loadBitmap(this.originalPath.toString(), null, (float) size, (float) size, false);
                        } else if (this.mediaType == 2) {
                            originalBitmap = ThumbnailUtils.createVideoThumbnail(this.originalPath.toString(), 1);
                        } else if (this.mediaType == 3) {
                            String path = this.originalPath.toString().toLowerCase();
                            if (path.endsWith(".jpg") || path.endsWith(".jpeg") || path.endsWith(".png") || path.endsWith(".gif")) {
                                originalBitmap = ImageLoader.loadBitmap(path, null, (float) size, (float) size, false);
                            } else {
                                removeTask();
                                return;
                            }
                        }
                        if (originalBitmap == null) {
                            removeTask();
                            return;
                        }
                        int w = originalBitmap.getWidth();
                        int h = originalBitmap.getHeight();
                        if (w != 0) {
                            if (h != 0) {
                                float scaleFactor = Math.min(((float) w) / ((float) size), ((float) h) / ((float) size));
                                Bitmap scaledBitmap = Bitmaps.createScaledBitmap(originalBitmap, (int) (((float) w) / scaleFactor), (int) (((float) h) / scaleFactor), true);
                                if (scaledBitmap != originalBitmap) {
                                    originalBitmap.recycle();
                                    originalBitmap = scaledBitmap;
                                }
                                FileOutputStream stream = new FileOutputStream(thumbFile);
                                originalBitmap.compress(CompressFormat.JPEG, 60, stream);
                                stream.close();
                                final BitmapDrawable bitmapDrawable = new BitmapDrawable(originalBitmap);
                                AndroidUtilities.runOnUIThread(new Runnable() {
                                    public void run() {
                                        ThumbGenerateTask.this.removeTask();
                                        String kf = key;
                                        if (ThumbGenerateTask.this.filter != null) {
                                            StringBuilder stringBuilder = new StringBuilder();
                                            stringBuilder.append(kf);
                                            stringBuilder.append("@");
                                            stringBuilder.append(ThumbGenerateTask.this.filter);
                                            kf = stringBuilder.toString();
                                        }
                                        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.messageThumbGenerated, bitmapDrawable, kf);
                                        ImageLoader.this.memCache.put(kf, bitmapDrawable);
                                    }
                                });
                                return;
                            }
                        }
                        removeTask();
                        return;
                    }
                }
                removeTask();
            } catch (Throwable e) {
                FileLog.e(e);
            } catch (Throwable e2) {
                FileLog.e(e2);
                removeTask();
            }
        }
    }

    private boolean canMoveFiles(java.io.File r1, java.io.File r2, int r3) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.ImageLoader.canMoveFiles(java.io.File, java.io.File, int):boolean
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
        r0 = 0;
        r1 = 0;
        r2 = 0;
        r3 = 1;
        if (r10 != 0) goto L_0x001d;
    L_0x0006:
        r4 = new java.io.File;	 Catch:{ Exception -> 0x001a }
        r5 = "000000000_999999_temp.jpg";	 Catch:{ Exception -> 0x001a }
        r4.<init>(r8, r5);	 Catch:{ Exception -> 0x001a }
        r1 = r4;	 Catch:{ Exception -> 0x001a }
        r4 = new java.io.File;	 Catch:{ Exception -> 0x001a }
        r5 = "000000000_999999.jpg";	 Catch:{ Exception -> 0x001a }
        r4.<init>(r9, r5);	 Catch:{ Exception -> 0x001a }
        r2 = r4;	 Catch:{ Exception -> 0x001a }
        goto L_0x0057;	 Catch:{ Exception -> 0x001a }
    L_0x0017:
        r1 = move-exception;	 Catch:{ Exception -> 0x001a }
        goto L_0x009f;	 Catch:{ Exception -> 0x001a }
    L_0x001a:
        r1 = move-exception;	 Catch:{ Exception -> 0x001a }
        goto L_0x0093;	 Catch:{ Exception -> 0x001a }
    L_0x001d:
        r4 = 3;	 Catch:{ Exception -> 0x001a }
        if (r10 != r4) goto L_0x0031;	 Catch:{ Exception -> 0x001a }
        r4 = new java.io.File;	 Catch:{ Exception -> 0x001a }
        r5 = "000000000_999999_temp.doc";	 Catch:{ Exception -> 0x001a }
        r4.<init>(r8, r5);	 Catch:{ Exception -> 0x001a }
        r1 = r4;	 Catch:{ Exception -> 0x001a }
        r4 = new java.io.File;	 Catch:{ Exception -> 0x001a }
        r5 = "000000000_999999.doc";	 Catch:{ Exception -> 0x001a }
        r4.<init>(r9, r5);	 Catch:{ Exception -> 0x001a }
        r2 = r4;	 Catch:{ Exception -> 0x001a }
        goto L_0x0057;	 Catch:{ Exception -> 0x001a }
        if (r10 != r3) goto L_0x0044;	 Catch:{ Exception -> 0x001a }
        r4 = new java.io.File;	 Catch:{ Exception -> 0x001a }
        r5 = "000000000_999999_temp.ogg";	 Catch:{ Exception -> 0x001a }
        r4.<init>(r8, r5);	 Catch:{ Exception -> 0x001a }
        r1 = r4;	 Catch:{ Exception -> 0x001a }
        r4 = new java.io.File;	 Catch:{ Exception -> 0x001a }
        r5 = "000000000_999999.ogg";	 Catch:{ Exception -> 0x001a }
        r4.<init>(r9, r5);	 Catch:{ Exception -> 0x001a }
        r2 = r4;	 Catch:{ Exception -> 0x001a }
        goto L_0x0057;	 Catch:{ Exception -> 0x001a }
        r4 = 2;	 Catch:{ Exception -> 0x001a }
        if (r10 != r4) goto L_0x0057;	 Catch:{ Exception -> 0x001a }
        r4 = new java.io.File;	 Catch:{ Exception -> 0x001a }
        r5 = "000000000_999999_temp.mp4";	 Catch:{ Exception -> 0x001a }
        r4.<init>(r8, r5);	 Catch:{ Exception -> 0x001a }
        r1 = r4;	 Catch:{ Exception -> 0x001a }
        r4 = new java.io.File;	 Catch:{ Exception -> 0x001a }
        r5 = "000000000_999999.mp4";	 Catch:{ Exception -> 0x001a }
        r4.<init>(r9, r5);	 Catch:{ Exception -> 0x001a }
        r2 = r4;	 Catch:{ Exception -> 0x001a }
    L_0x0057:
        r4 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;	 Catch:{ Exception -> 0x001a }
        r4 = new byte[r4];	 Catch:{ Exception -> 0x001a }
        r1.createNewFile();	 Catch:{ Exception -> 0x001a }
        r5 = new java.io.RandomAccessFile;	 Catch:{ Exception -> 0x001a }
        r6 = "rws";	 Catch:{ Exception -> 0x001a }
        r5.<init>(r1, r6);	 Catch:{ Exception -> 0x001a }
        r0 = r5;	 Catch:{ Exception -> 0x001a }
        r0.write(r4);	 Catch:{ Exception -> 0x001a }
        r0.close();	 Catch:{ Exception -> 0x001a }
        r0 = 0;	 Catch:{ Exception -> 0x001a }
        r5 = r1.renameTo(r2);	 Catch:{ Exception -> 0x001a }
        r1.delete();	 Catch:{ Exception -> 0x001a }
        r2.delete();	 Catch:{ Exception -> 0x001a }
        if (r5 == 0) goto L_0x0087;
        if (r0 == 0) goto L_0x0085;
        r0.close();	 Catch:{ Exception -> 0x0080 }
        goto L_0x0085;
    L_0x0080:
        r6 = move-exception;
        org.telegram.messenger.FileLog.e(r6);
        goto L_0x0086;
        return r3;
        if (r0 == 0) goto L_0x0092;
        r0.close();	 Catch:{ Exception -> 0x008d }
        goto L_0x0092;
    L_0x008d:
        r1 = move-exception;
        org.telegram.messenger.FileLog.e(r1);
        goto L_0x009d;
        goto L_0x009d;
        org.telegram.messenger.FileLog.e(r1);	 Catch:{ all -> 0x0017 }
        if (r0 == 0) goto L_0x0092;
        r0.close();	 Catch:{ Exception -> 0x008d }
        goto L_0x0092;
        r1 = 0;
        return r1;
        if (r0 == 0) goto L_0x00ab;
        r0.close();	 Catch:{ Exception -> 0x00a6 }
        goto L_0x00ab;
    L_0x00a6:
        r2 = move-exception;
        org.telegram.messenger.FileLog.e(r2);
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.ImageLoader.canMoveFiles(java.io.File, java.io.File, int):boolean");
    }

    public static android.graphics.Bitmap loadBitmap(java.lang.String r1, android.net.Uri r2, float r3, float r4, boolean r5) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.ImageLoader.loadBitmap(java.lang.String, android.net.Uri, float, float, boolean):android.graphics.Bitmap
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
        r1 = r21;
        r2 = new android.graphics.BitmapFactory$Options;
        r2.<init>();
        r3 = 1;
        r2.inJustDecodeBounds = r3;
        r4 = 0;
        if (r20 != 0) goto L_0x0032;
    L_0x000d:
        if (r1 == 0) goto L_0x0032;
    L_0x000f:
        r6 = r21.getScheme();
        if (r6 == 0) goto L_0x0032;
    L_0x0015:
        r6 = 0;
        r7 = r21.getScheme();
        r8 = "file";
        r7 = r7.contains(r8);
        if (r7 == 0) goto L_0x0027;
    L_0x0022:
        r5 = r21.getPath();
        goto L_0x0034;
    L_0x0027:
        r7 = org.telegram.messenger.AndroidUtilities.getPath(r21);	 Catch:{ Throwable -> 0x002d }
        r5 = r7;
        goto L_0x0034;
    L_0x002d:
        r0 = move-exception;
        r7 = r0;
        org.telegram.messenger.FileLog.e(r7);
    L_0x0032:
        r5 = r20;
    L_0x0034:
        r6 = 0;
        r7 = 0;
        if (r5 == 0) goto L_0x003c;
    L_0x0038:
        android.graphics.BitmapFactory.decodeFile(r5, r2);
        goto L_0x0062;
    L_0x003c:
        if (r1 == 0) goto L_0x0062;
    L_0x003e:
        r8 = r7;
        r9 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Throwable -> 0x005c }
        r9 = r9.getContentResolver();	 Catch:{ Throwable -> 0x005c }
        r9 = r9.openInputStream(r1);	 Catch:{ Throwable -> 0x005c }
        r4 = r9;	 Catch:{ Throwable -> 0x005c }
        android.graphics.BitmapFactory.decodeStream(r4, r6, r2);	 Catch:{ Throwable -> 0x005c }
        r4.close();	 Catch:{ Throwable -> 0x005c }
        r9 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Throwable -> 0x005c }
        r9 = r9.getContentResolver();	 Catch:{ Throwable -> 0x005c }
        r9 = r9.openInputStream(r1);	 Catch:{ Throwable -> 0x005c }
        r4 = r9;
        goto L_0x0062;
    L_0x005c:
        r0 = move-exception;
        r3 = r0;
        org.telegram.messenger.FileLog.e(r3);
        return r6;
    L_0x0062:
        r8 = r2.outWidth;
        r8 = (float) r8;
        r9 = r2.outHeight;
        r9 = (float) r9;
        if (r24 == 0) goto L_0x0073;
    L_0x006a:
        r10 = r8 / r22;
        r11 = r9 / r23;
        r10 = java.lang.Math.max(r10, r11);
        goto L_0x007b;
    L_0x0073:
        r10 = r8 / r22;
        r11 = r9 / r23;
        r10 = java.lang.Math.min(r10, r11);
    L_0x007b:
        r11 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r11 = (r10 > r11 ? 1 : (r10 == r11 ? 0 : -1));
        if (r11 >= 0) goto L_0x0083;
    L_0x0081:
        r10 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
    L_0x0083:
        r2.inJustDecodeBounds = r7;
        r11 = (int) r10;
        r2.inSampleSize = r11;
        r11 = r2.inSampleSize;
        r11 = r11 % 2;
        if (r11 == 0) goto L_0x009b;
    L_0x008e:
        r11 = r3;
    L_0x008f:
        r12 = r11 * 2;
        r7 = r2.inSampleSize;
        if (r12 >= r7) goto L_0x0099;
    L_0x0095:
        r11 = r11 * 2;
        r7 = 0;
        goto L_0x008f;
    L_0x0099:
        r2.inSampleSize = r11;
    L_0x009b:
        r7 = android.os.Build.VERSION.SDK_INT;
        r11 = 21;
        if (r7 >= r11) goto L_0x00a3;
    L_0x00a1:
        r7 = r3;
        goto L_0x00a4;
    L_0x00a3:
        r7 = 0;
    L_0x00a4:
        r2.inPurgeable = r7;
        r7 = 0;
        if (r5 == 0) goto L_0x00ab;
    L_0x00a9:
        r7 = r5;
        goto L_0x00b1;
    L_0x00ab:
        if (r1 == 0) goto L_0x00b1;
    L_0x00ad:
        r7 = org.telegram.messenger.AndroidUtilities.getPath(r21);
    L_0x00b1:
        r11 = 0;
        if (r7 == 0) goto L_0x00e4;
    L_0x00b4:
        r12 = new android.support.media.ExifInterface;	 Catch:{ Throwable -> 0x00e3 }
        r12.<init>(r7);	 Catch:{ Throwable -> 0x00e3 }
        r6 = "Orientation";	 Catch:{ Throwable -> 0x00e3 }
        r3 = r12.getAttributeInt(r6, r3);	 Catch:{ Throwable -> 0x00e3 }
        r6 = new android.graphics.Matrix;	 Catch:{ Throwable -> 0x00e3 }
        r6.<init>();	 Catch:{ Throwable -> 0x00e3 }
        r11 = r6;	 Catch:{ Throwable -> 0x00e3 }
        r6 = 3;	 Catch:{ Throwable -> 0x00e3 }
        if (r3 == r6) goto L_0x00dc;	 Catch:{ Throwable -> 0x00e3 }
    L_0x00c8:
        r6 = 6;	 Catch:{ Throwable -> 0x00e3 }
        if (r3 == r6) goto L_0x00d6;	 Catch:{ Throwable -> 0x00e3 }
    L_0x00cb:
        r6 = 8;	 Catch:{ Throwable -> 0x00e3 }
        if (r3 == r6) goto L_0x00d0;	 Catch:{ Throwable -> 0x00e3 }
    L_0x00cf:
        goto L_0x00e2;	 Catch:{ Throwable -> 0x00e3 }
    L_0x00d0:
        r6 = 1132920832; // 0x43870000 float:270.0 double:5.597372625E-315;	 Catch:{ Throwable -> 0x00e3 }
        r11.postRotate(r6);	 Catch:{ Throwable -> 0x00e3 }
        goto L_0x00e2;	 Catch:{ Throwable -> 0x00e3 }
    L_0x00d6:
        r6 = 1119092736; // 0x42b40000 float:90.0 double:5.529052754E-315;	 Catch:{ Throwable -> 0x00e3 }
        r11.postRotate(r6);	 Catch:{ Throwable -> 0x00e3 }
        goto L_0x00e2;	 Catch:{ Throwable -> 0x00e3 }
    L_0x00dc:
        r6 = 1127481344; // 0x43340000 float:180.0 double:5.570497984E-315;	 Catch:{ Throwable -> 0x00e3 }
        r11.postRotate(r6);	 Catch:{ Throwable -> 0x00e3 }
    L_0x00e2:
        goto L_0x00e4;
    L_0x00e3:
        r0 = move-exception;
    L_0x00e4:
        r3 = 0;
        if (r5 == 0) goto L_0x0157;
    L_0x00e7:
        r6 = android.graphics.BitmapFactory.decodeFile(r5, r2);	 Catch:{ Throwable -> 0x0110 }
        r3 = r6;	 Catch:{ Throwable -> 0x0110 }
        if (r3 == 0) goto L_0x010e;	 Catch:{ Throwable -> 0x0110 }
    L_0x00ee:
        r6 = r2.inPurgeable;	 Catch:{ Throwable -> 0x0110 }
        if (r6 == 0) goto L_0x00f5;	 Catch:{ Throwable -> 0x0110 }
    L_0x00f2:
        org.telegram.messenger.Utilities.pinBitmap(r3);	 Catch:{ Throwable -> 0x0110 }
    L_0x00f5:
        r14 = 0;	 Catch:{ Throwable -> 0x0110 }
        r15 = 0;	 Catch:{ Throwable -> 0x0110 }
        r16 = r3.getWidth();	 Catch:{ Throwable -> 0x0110 }
        r17 = r3.getHeight();	 Catch:{ Throwable -> 0x0110 }
        r19 = 1;	 Catch:{ Throwable -> 0x0110 }
        r13 = r3;	 Catch:{ Throwable -> 0x0110 }
        r18 = r11;	 Catch:{ Throwable -> 0x0110 }
        r6 = org.telegram.messenger.Bitmaps.createBitmap(r13, r14, r15, r16, r17, r18, r19);	 Catch:{ Throwable -> 0x0110 }
        if (r6 == r3) goto L_0x010e;	 Catch:{ Throwable -> 0x0110 }
    L_0x010a:
        r3.recycle();	 Catch:{ Throwable -> 0x0110 }
        r3 = r6;
    L_0x010e:
        goto L_0x01ad;
    L_0x0110:
        r0 = move-exception;
        r6 = r3;
        r3 = r0;
        org.telegram.messenger.FileLog.e(r3);
        r12 = getInstance();
        r12.clearMemory();
        if (r6 != 0) goto L_0x0132;
    L_0x011f:
        r12 = android.graphics.BitmapFactory.decodeFile(r5, r2);	 Catch:{ Throwable -> 0x012e }
        r6 = r12;	 Catch:{ Throwable -> 0x012e }
        if (r6 == 0) goto L_0x0132;	 Catch:{ Throwable -> 0x012e }
    L_0x0126:
        r12 = r2.inPurgeable;	 Catch:{ Throwable -> 0x012e }
        if (r12 == 0) goto L_0x0132;	 Catch:{ Throwable -> 0x012e }
    L_0x012a:
        org.telegram.messenger.Utilities.pinBitmap(r6);	 Catch:{ Throwable -> 0x012e }
        goto L_0x0132;	 Catch:{ Throwable -> 0x012e }
    L_0x012e:
        r0 = move-exception;	 Catch:{ Throwable -> 0x012e }
        r12 = r6;	 Catch:{ Throwable -> 0x012e }
        r6 = r0;	 Catch:{ Throwable -> 0x012e }
        goto L_0x014e;	 Catch:{ Throwable -> 0x012e }
    L_0x0132:
        if (r6 == 0) goto L_0x0154;	 Catch:{ Throwable -> 0x012e }
        r14 = 0;	 Catch:{ Throwable -> 0x012e }
        r15 = 0;	 Catch:{ Throwable -> 0x012e }
        r16 = r6.getWidth();	 Catch:{ Throwable -> 0x012e }
        r17 = r6.getHeight();	 Catch:{ Throwable -> 0x012e }
        r19 = 1;	 Catch:{ Throwable -> 0x012e }
        r13 = r6;	 Catch:{ Throwable -> 0x012e }
        r18 = r11;	 Catch:{ Throwable -> 0x012e }
        r12 = org.telegram.messenger.Bitmaps.createBitmap(r13, r14, r15, r16, r17, r18, r19);	 Catch:{ Throwable -> 0x012e }
        if (r12 == r6) goto L_0x0154;	 Catch:{ Throwable -> 0x012e }
        r6.recycle();	 Catch:{ Throwable -> 0x012e }
        r6 = r12;
        goto L_0x0154;
        org.telegram.messenger.FileLog.e(r6);
        r3 = r12;
        goto L_0x010e;
        r3 = r6;
        goto L_0x010e;
    L_0x0157:
        if (r1 == 0) goto L_0x01ad;
        r6 = 0;
        r6 = android.graphics.BitmapFactory.decodeStream(r4, r6, r2);	 Catch:{ Throwable -> 0x018f, all -> 0x018b }
        r3 = r6;	 Catch:{ Throwable -> 0x018f, all -> 0x018b }
        if (r3 == 0) goto L_0x0181;	 Catch:{ Throwable -> 0x018f, all -> 0x018b }
        r6 = r2.inPurgeable;	 Catch:{ Throwable -> 0x018f, all -> 0x018b }
        if (r6 == 0) goto L_0x0168;	 Catch:{ Throwable -> 0x018f, all -> 0x018b }
        org.telegram.messenger.Utilities.pinBitmap(r3);	 Catch:{ Throwable -> 0x018f, all -> 0x018b }
        r14 = 0;	 Catch:{ Throwable -> 0x018f, all -> 0x018b }
        r15 = 0;	 Catch:{ Throwable -> 0x018f, all -> 0x018b }
        r16 = r3.getWidth();	 Catch:{ Throwable -> 0x018f, all -> 0x018b }
        r17 = r3.getHeight();	 Catch:{ Throwable -> 0x018f, all -> 0x018b }
        r19 = 1;	 Catch:{ Throwable -> 0x018f, all -> 0x018b }
        r13 = r3;	 Catch:{ Throwable -> 0x018f, all -> 0x018b }
        r18 = r11;	 Catch:{ Throwable -> 0x018f, all -> 0x018b }
        r6 = org.telegram.messenger.Bitmaps.createBitmap(r13, r14, r15, r16, r17, r18, r19);	 Catch:{ Throwable -> 0x018f, all -> 0x018b }
        if (r6 == r3) goto L_0x0181;	 Catch:{ Throwable -> 0x018f, all -> 0x018b }
        r3.recycle();	 Catch:{ Throwable -> 0x018f, all -> 0x018b }
        r3 = r6;
        r4.close();	 Catch:{ Throwable -> 0x0185 }
        goto L_0x01ad;
    L_0x0185:
        r0 = move-exception;
        r6 = r0;
        org.telegram.messenger.FileLog.e(r6);
        goto L_0x01ad;
    L_0x018b:
        r0 = move-exception;
        r6 = r3;
        r3 = r0;
        goto L_0x01a3;
    L_0x018f:
        r0 = move-exception;
        r6 = r3;
        r3 = r0;
        org.telegram.messenger.FileLog.e(r3);	 Catch:{ all -> 0x01a1 }
        r4.close();	 Catch:{ Throwable -> 0x0199 }
        goto L_0x019f;
    L_0x0199:
        r0 = move-exception;
        r3 = r0;
        org.telegram.messenger.FileLog.e(r3);
        r3 = r6;
        goto L_0x01ad;
    L_0x01a1:
        r0 = move-exception;
        goto L_0x018d;
        r4.close();	 Catch:{ Throwable -> 0x01a7 }
        goto L_0x01ac;
    L_0x01a7:
        r0 = move-exception;
        r12 = r0;
        org.telegram.messenger.FileLog.e(r12);
        throw r3;
    L_0x01ad:
        return r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.ImageLoader.loadBitmap(java.lang.String, android.net.Uri, float, float, boolean):android.graphics.Bitmap");
    }

    public static void saveMessageThumbs(org.telegram.tgnet.TLRPC.Message r1) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.ImageLoader.saveMessageThumbs(org.telegram.tgnet.TLRPC$Message):void
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
        r1 = r18;
        r2 = 0;
        r3 = r1.media;
        r3 = r3 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaPhoto;
        if (r3 == 0) goto L_0x002b;
    L_0x0009:
        r3 = 0;
        r4 = r1.media;
        r4 = r4.photo;
        r4 = r4.sizes;
        r4 = r4.size();
    L_0x0014:
        if (r3 >= r4) goto L_0x0076;
    L_0x0016:
        r5 = r1.media;
        r5 = r5.photo;
        r5 = r5.sizes;
        r5 = r5.get(r3);
        r5 = (org.telegram.tgnet.TLRPC.PhotoSize) r5;
        r6 = r5 instanceof org.telegram.tgnet.TLRPC.TL_photoCachedSize;
        if (r6 == 0) goto L_0x0028;
    L_0x0026:
        r2 = r5;
        goto L_0x0076;
    L_0x0028:
        r3 = r3 + 1;
        goto L_0x0014;
    L_0x002b:
        r3 = r1.media;
        r3 = r3 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaDocument;
        if (r3 == 0) goto L_0x0042;
    L_0x0031:
        r3 = r1.media;
        r3 = r3.document;
        r3 = r3.thumb;
        r3 = r3 instanceof org.telegram.tgnet.TLRPC.TL_photoCachedSize;
        if (r3 == 0) goto L_0x0076;
    L_0x003b:
        r3 = r1.media;
        r3 = r3.document;
        r2 = r3.thumb;
        goto L_0x0076;
    L_0x0042:
        r3 = r1.media;
        r3 = r3 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaWebPage;
        if (r3 == 0) goto L_0x0076;
    L_0x0048:
        r3 = r1.media;
        r3 = r3.webpage;
        r3 = r3.photo;
        if (r3 == 0) goto L_0x0076;
    L_0x0050:
        r3 = 0;
        r4 = r1.media;
        r4 = r4.webpage;
        r4 = r4.photo;
        r4 = r4.sizes;
        r4 = r4.size();
    L_0x005d:
        if (r3 >= r4) goto L_0x0076;
    L_0x005f:
        r5 = r1.media;
        r5 = r5.webpage;
        r5 = r5.photo;
        r5 = r5.sizes;
        r5 = r5.get(r3);
        r5 = (org.telegram.tgnet.TLRPC.PhotoSize) r5;
        r6 = r5 instanceof org.telegram.tgnet.TLRPC.TL_photoCachedSize;
        if (r6 == 0) goto L_0x0073;
    L_0x0071:
        r2 = r5;
        goto L_0x0076;
    L_0x0073:
        r3 = r3 + 1;
        goto L_0x005d;
    L_0x0076:
        if (r2 == 0) goto L_0x01de;
    L_0x0078:
        r3 = r2.bytes;
        if (r3 == 0) goto L_0x01de;
    L_0x007c:
        r3 = r2.bytes;
        r3 = r3.length;
        if (r3 == 0) goto L_0x01de;
    L_0x0081:
        r3 = r2.location;
        r3 = r3 instanceof org.telegram.tgnet.TLRPC.TL_fileLocationUnavailable;
        if (r3 == 0) goto L_0x00a3;
    L_0x0087:
        r3 = new org.telegram.tgnet.TLRPC$TL_fileLocation;
        r3.<init>();
        r2.location = r3;
        r3 = r2.location;
        r4 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r3.volume_id = r4;
        r3 = r2.location;
        r4 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r3.dc_id = r4;
        r3 = r2.location;
        r4 = org.telegram.messenger.SharedConfig.getLastLocalId();
        r3.local_id = r4;
    L_0x00a3:
        r3 = 1;
        r3 = org.telegram.messenger.FileLoader.getPathToAttach(r2, r3);
        r4 = 0;
        r5 = org.telegram.messenger.MessageObject.shouldEncryptPhotoOrVideo(r18);
        if (r5 == 0) goto L_0x00cb;
    L_0x00af:
        r5 = new java.io.File;
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r7 = r3.getAbsolutePath();
        r6.append(r7);
        r7 = ".enc";
        r6.append(r7);
        r6 = r6.toString();
        r5.<init>(r6);
        r3 = r5;
        r4 = 1;
    L_0x00cb:
        r5 = r3.exists();
        if (r5 != 0) goto L_0x0156;
    L_0x00d1:
        if (r4 == 0) goto L_0x0142;
    L_0x00d3:
        r5 = new java.io.File;	 Catch:{ Exception -> 0x013f }
        r6 = org.telegram.messenger.FileLoader.getInternalCacheDir();	 Catch:{ Exception -> 0x013f }
        r7 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x013f }
        r7.<init>();	 Catch:{ Exception -> 0x013f }
        r8 = r3.getName();	 Catch:{ Exception -> 0x013f }
        r7.append(r8);	 Catch:{ Exception -> 0x013f }
        r8 = ".key";	 Catch:{ Exception -> 0x013f }
        r7.append(r8);	 Catch:{ Exception -> 0x013f }
        r7 = r7.toString();	 Catch:{ Exception -> 0x013f }
        r5.<init>(r6, r7);	 Catch:{ Exception -> 0x013f }
        r6 = new java.io.RandomAccessFile;	 Catch:{ Exception -> 0x013f }
        r7 = "rws";	 Catch:{ Exception -> 0x013f }
        r6.<init>(r5, r7);	 Catch:{ Exception -> 0x013f }
        r7 = r6.length();	 Catch:{ Exception -> 0x013f }
        r9 = 32;	 Catch:{ Exception -> 0x013f }
        r10 = new byte[r9];	 Catch:{ Exception -> 0x013f }
        r11 = 16;	 Catch:{ Exception -> 0x013f }
        r12 = new byte[r11];	 Catch:{ Exception -> 0x013f }
        r15 = r12;	 Catch:{ Exception -> 0x013f }
        r12 = 0;	 Catch:{ Exception -> 0x013f }
        r14 = (r7 > r12 ? 1 : (r7 == r12 ? 0 : -1));	 Catch:{ Exception -> 0x013f }
        if (r14 <= 0) goto L_0x011b;	 Catch:{ Exception -> 0x013f }
    L_0x010b:
        r16 = 48;	 Catch:{ Exception -> 0x013f }
        r16 = r7 % r16;	 Catch:{ Exception -> 0x013f }
        r14 = (r16 > r12 ? 1 : (r16 == r12 ? 0 : -1));	 Catch:{ Exception -> 0x013f }
        if (r14 != 0) goto L_0x011b;	 Catch:{ Exception -> 0x013f }
    L_0x0113:
        r12 = 0;	 Catch:{ Exception -> 0x013f }
        r6.read(r10, r12, r9);	 Catch:{ Exception -> 0x013f }
        r6.read(r15, r12, r11);	 Catch:{ Exception -> 0x013f }
        goto L_0x012b;	 Catch:{ Exception -> 0x013f }
    L_0x011b:
        r9 = org.telegram.messenger.Utilities.random;	 Catch:{ Exception -> 0x013f }
        r9.nextBytes(r10);	 Catch:{ Exception -> 0x013f }
        r9 = org.telegram.messenger.Utilities.random;	 Catch:{ Exception -> 0x013f }
        r9.nextBytes(r15);	 Catch:{ Exception -> 0x013f }
        r6.write(r10);	 Catch:{ Exception -> 0x013f }
        r6.write(r15);	 Catch:{ Exception -> 0x013f }
    L_0x012b:
        r6.close();	 Catch:{ Exception -> 0x013f }
        r11 = r2.bytes;	 Catch:{ Exception -> 0x013f }
        r14 = 0;	 Catch:{ Exception -> 0x013f }
        r9 = r2.bytes;	 Catch:{ Exception -> 0x013f }
        r9 = r9.length;	 Catch:{ Exception -> 0x013f }
        r16 = 0;	 Catch:{ Exception -> 0x013f }
        r12 = r10;	 Catch:{ Exception -> 0x013f }
        r13 = r15;	 Catch:{ Exception -> 0x013f }
        r17 = r15;	 Catch:{ Exception -> 0x013f }
        r15 = r9;	 Catch:{ Exception -> 0x013f }
        org.telegram.messenger.Utilities.aesCtrDecryptionByteArray(r11, r12, r13, r14, r15, r16);	 Catch:{ Exception -> 0x013f }
        goto L_0x0142;	 Catch:{ Exception -> 0x013f }
    L_0x013f:
        r0 = move-exception;	 Catch:{ Exception -> 0x013f }
        r5 = r0;	 Catch:{ Exception -> 0x013f }
        goto L_0x0152;	 Catch:{ Exception -> 0x013f }
    L_0x0142:
        r5 = new java.io.RandomAccessFile;	 Catch:{ Exception -> 0x013f }
        r6 = "rws";	 Catch:{ Exception -> 0x013f }
        r5.<init>(r3, r6);	 Catch:{ Exception -> 0x013f }
        r6 = r2.bytes;	 Catch:{ Exception -> 0x013f }
        r5.write(r6);	 Catch:{ Exception -> 0x013f }
        r5.close();	 Catch:{ Exception -> 0x013f }
        goto L_0x0156;
        org.telegram.messenger.FileLog.e(r5);
    L_0x0156:
        r5 = new org.telegram.tgnet.TLRPC$TL_photoSize;
        r5.<init>();
        r6 = r2.w;
        r5.w = r6;
        r6 = r2.h;
        r5.h = r6;
        r6 = r2.location;
        r5.location = r6;
        r6 = r2.size;
        r5.size = r6;
        r6 = r2.type;
        r5.type = r6;
        r6 = r1.media;
        r6 = r6 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaPhoto;
        if (r6 == 0) goto L_0x019d;
        r6 = 0;
        r7 = r1.media;
        r7 = r7.photo;
        r7 = r7.sizes;
        r7 = r7.size();
        if (r6 >= r7) goto L_0x01de;
        r8 = r1.media;
        r8 = r8.photo;
        r8 = r8.sizes;
        r8 = r8.get(r6);
        r8 = r8 instanceof org.telegram.tgnet.TLRPC.TL_photoCachedSize;
        if (r8 == 0) goto L_0x019a;
        r8 = r1.media;
        r8 = r8.photo;
        r8 = r8.sizes;
        r8.set(r6, r5);
        goto L_0x01de;
        r6 = r6 + 1;
        goto L_0x0180;
        r6 = r1.media;
        r6 = r6 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaDocument;
        if (r6 == 0) goto L_0x01aa;
        r6 = r1.media;
        r6 = r6.document;
        r6.thumb = r5;
        goto L_0x01de;
        r6 = r1.media;
        r6 = r6 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaWebPage;
        if (r6 == 0) goto L_0x01de;
        r6 = 0;
        r7 = r1.media;
        r7 = r7.webpage;
        r7 = r7.photo;
        r7 = r7.sizes;
        r7 = r7.size();
        if (r6 >= r7) goto L_0x01de;
        r8 = r1.media;
        r8 = r8.webpage;
        r8 = r8.photo;
        r8 = r8.sizes;
        r8 = r8.get(r6);
        r8 = r8 instanceof org.telegram.tgnet.TLRPC.TL_photoCachedSize;
        if (r8 == 0) goto L_0x01db;
        r8 = r1.media;
        r8 = r8.webpage;
        r8 = r8.photo;
        r8 = r8.sizes;
        r8.set(r6, r5);
        goto L_0x01de;
        r6 = r6 + 1;
        goto L_0x01bd;
    L_0x01de:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.ImageLoader.saveMessageThumbs(org.telegram.tgnet.TLRPC$Message):void");
    }

    private static org.telegram.tgnet.TLRPC.PhotoSize scaleAndSaveImageInternal(android.graphics.Bitmap r1, int r2, int r3, float r4, float r5, float r6, int r7, boolean r8, boolean r9) throws java.lang.Exception {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.ImageLoader.scaleAndSaveImageInternal(android.graphics.Bitmap, int, int, float, float, float, int, boolean, boolean):org.telegram.tgnet.TLRPC$PhotoSize
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
        r0 = r14;
        r1 = r20;
        r2 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r2 = (r19 > r2 ? 1 : (r19 == r2 ? 0 : -1));
        if (r2 > 0) goto L_0x0011;
    L_0x0009:
        if (r22 == 0) goto L_0x000c;
    L_0x000b:
        goto L_0x0011;
    L_0x000c:
        r4 = r15;
        r5 = r16;
        r3 = r0;
        goto L_0x0019;
    L_0x0011:
        r3 = 1;
        r4 = r15;
        r5 = r16;
        r3 = org.telegram.messenger.Bitmaps.createScaledBitmap(r0, r4, r5, r3);
        r6 = new org.telegram.tgnet.TLRPC$TL_fileLocation;
        r6.<init>();
        r7 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r6.volume_id = r7;
        r7 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r6.dc_id = r7;
        r7 = org.telegram.messenger.SharedConfig.getLastLocalId();
        r6.local_id = r7;
        r7 = new org.telegram.tgnet.TLRPC$TL_photoSize;
        r7.<init>();
        r7.location = r6;
        r8 = r3.getWidth();
        r7.w = r8;
        r8 = r3.getHeight();
        r7.h = r8;
        r8 = r7.w;
        r9 = 100;
        if (r8 > r9) goto L_0x0050;
        r8 = r7.h;
        if (r8 > r9) goto L_0x0050;
        r8 = "s";
        r7.type = r8;
        goto L_0x0081;
        r8 = r7.w;
        r9 = 320; // 0x140 float:4.48E-43 double:1.58E-321;
        if (r8 > r9) goto L_0x005f;
        r8 = r7.h;
        if (r8 > r9) goto L_0x005f;
        r8 = "m";
        r7.type = r8;
        goto L_0x0081;
        r8 = r7.w;
        r9 = 800; // 0x320 float:1.121E-42 double:3.953E-321;
        if (r8 > r9) goto L_0x006e;
        r8 = r7.h;
        if (r8 > r9) goto L_0x006e;
        r8 = "x";
        r7.type = r8;
        goto L_0x0081;
        r8 = r7.w;
        r9 = 1280; // 0x500 float:1.794E-42 double:6.324E-321;
        if (r8 > r9) goto L_0x007d;
        r8 = r7.h;
        if (r8 > r9) goto L_0x007d;
        r8 = "y";
        r7.type = r8;
        goto L_0x0081;
        r8 = "w";
        r7.type = r8;
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r9 = r6.volume_id;
        r8.append(r9);
        r9 = "_";
        r8.append(r9);
        r9 = r6.local_id;
        r8.append(r9);
        r9 = ".jpg";
        r8.append(r9);
        r8 = r8.toString();
        r9 = new java.io.File;
        r10 = 4;
        r10 = org.telegram.messenger.FileLoader.getDirectory(r10);
        r9.<init>(r10, r8);
        r10 = new java.io.FileOutputStream;
        r10.<init>(r9);
        r11 = android.graphics.Bitmap.CompressFormat.JPEG;
        r3.compress(r11, r1, r10);
        if (r21 == 0) goto L_0x00cd;
        r12 = new java.io.ByteArrayOutputStream;
        r12.<init>();
        r13 = android.graphics.Bitmap.CompressFormat.JPEG;
        r3.compress(r13, r1, r12);
        r13 = r12.toByteArray();
        r7.bytes = r13;
        r13 = r7.bytes;
        r13 = r13.length;
        r7.size = r13;
        r12.close();
        goto L_0x00d8;
        r12 = r10.getChannel();
        r12 = r12.size();
        r12 = (int) r12;
        r7.size = r12;
        r10.close();
        if (r3 == r0) goto L_0x00e0;
        r3.recycle();
        return r7;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.ImageLoader.scaleAndSaveImageInternal(android.graphics.Bitmap, int, int, float, float, float, int, boolean, boolean):org.telegram.tgnet.TLRPC$PhotoSize");
    }

    public void loadImageForImageReceiver(org.telegram.messenger.ImageReceiver r1) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.ImageLoader.loadImageForImageReceiver(org.telegram.messenger.ImageReceiver):void
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
        r11 = r28;
        r12 = r29;
        if (r12 != 0) goto L_0x0007;
    L_0x0006:
        return;
    L_0x0007:
        r0 = 0;
        r1 = r29.getKey();
        r2 = 0;
        r3 = 1;
        if (r1 == 0) goto L_0x0026;
    L_0x0010:
        r4 = r11.memCache;
        r4 = r4.get(r1);
        if (r4 == 0) goto L_0x0026;
    L_0x0018:
        r11.cancelLoadingForImageReceiver(r12, r2);
        r12.setImageBitmapByKey(r4, r1, r2, r3);
        r0 = 1;
        r5 = r29.isForcePreview();
        if (r5 != 0) goto L_0x0026;
    L_0x0025:
        return;
    L_0x0026:
        r13 = r0;
        r0 = 0;
        r4 = r29.getThumbKey();
        if (r4 == 0) goto L_0x0046;
    L_0x002e:
        r5 = r11.memCache;
        r5 = r5.get(r4);
        if (r5 == 0) goto L_0x0046;
    L_0x0036:
        r12.setImageBitmapByKey(r5, r4, r3, r3);
        r11.cancelLoadingForImageReceiver(r12, r3);
        if (r13 == 0) goto L_0x0045;
    L_0x003e:
        r6 = r29.isForcePreview();
        if (r6 == 0) goto L_0x0045;
    L_0x0044:
        return;
    L_0x0045:
        r0 = 1;
    L_0x0046:
        r14 = r0;
        r15 = r29.getThumbLocation();
        r0 = r29.getImageLocation();
        r10 = r29.getHttpImageLocation();
        r5 = 0;
        r6 = 0;
        r7 = 0;
        r1 = 0;
        r4 = 0;
        r8 = r29.getExt();
        if (r8 != 0) goto L_0x0060;
    L_0x005e:
        r8 = "jpg";
    L_0x0060:
        r9 = r8;
        if (r10 == 0) goto L_0x008a;
    L_0x0063:
        r1 = org.telegram.messenger.Utilities.MD5(r10);
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r2.append(r1);
        r8 = ".";
        r2.append(r8);
        r8 = "jpg";
        r8 = getHttpUrlExtension(r10, r8);
        r2.append(r8);
        r2 = r2.toString();
        r17 = r0;
        r16 = r2;
        r8 = r4;
        r22 = r5;
        goto L_0x0208;
    L_0x008a:
        if (r0 == 0) goto L_0x01ff;
    L_0x008c:
        r8 = r0 instanceof org.telegram.tgnet.TLRPC.FileLocation;
        if (r8 == 0) goto L_0x00da;
    L_0x0090:
        r2 = r0;
        r2 = (org.telegram.tgnet.TLRPC.FileLocation) r2;
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r17 = r4;
        r3 = r2.volume_id;
        r8.append(r3);
        r3 = "_";
        r8.append(r3);
        r3 = r2.local_id;
        r8.append(r3);
        r1 = r8.toString();
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r3.append(r1);
        r4 = ".";
        r3.append(r4);
        r3.append(r9);
        r6 = r3.toString();
        r3 = r29.getExt();
        if (r3 != 0) goto L_0x00d8;
    L_0x00c7:
        r3 = r2.key;
        if (r3 != 0) goto L_0x00d8;
    L_0x00cb:
        r3 = r2.volume_id;
        r18 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r8 = (r3 > r18 ? 1 : (r3 == r18 ? 0 : -1));
        if (r8 != 0) goto L_0x00d9;
    L_0x00d4:
        r3 = r2.local_id;
        if (r3 >= 0) goto L_0x00d9;
    L_0x00d8:
        r5 = 1;
    L_0x00d9:
        goto L_0x010a;
    L_0x00da:
        r17 = r4;
        r3 = r0 instanceof org.telegram.tgnet.TLRPC.TL_webDocument;
        if (r3 == 0) goto L_0x010e;
    L_0x00e0:
        r2 = r0;
        r2 = (org.telegram.tgnet.TLRPC.TL_webDocument) r2;
        r3 = r2.mime_type;
        r3 = org.telegram.messenger.FileLoader.getExtensionByMime(r3);
        r4 = r2.url;
        r1 = org.telegram.messenger.Utilities.MD5(r4);
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r4.append(r1);
        r8 = ".";
        r4.append(r8);
        r8 = r2.url;
        r8 = getHttpUrlExtension(r8, r3);
        r4.append(r8);
        r6 = r4.toString();
    L_0x010a:
        r8 = r17;
        goto L_0x01ed;
    L_0x010e:
        r3 = r0 instanceof org.telegram.tgnet.TLRPC.Document;
        if (r3 == 0) goto L_0x01e7;
    L_0x0112:
        r3 = r0;
        r3 = (org.telegram.tgnet.TLRPC.Document) r3;
        r21 = r1;
        r1 = r3.id;
        r18 = 0;
        r4 = (r1 > r18 ? 1 : (r1 == r18 ? 0 : -1));
        if (r4 == 0) goto L_0x01e2;
    L_0x011f:
        r1 = r3.dc_id;
        if (r1 != 0) goto L_0x0129;
    L_0x0123:
        r22 = r5;
        r8 = r17;
        goto L_0x01e6;
    L_0x0129:
        r1 = r3.version;
        if (r1 != 0) goto L_0x0148;
    L_0x012d:
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = r3.dc_id;
        r1.append(r2);
        r2 = "_";
        r1.append(r2);
        r22 = r5;
        r4 = r3.id;
        r1.append(r4);
        r1 = r1.toString();
    L_0x0147:
        goto L_0x016d;
    L_0x0148:
        r22 = r5;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = r3.dc_id;
        r1.append(r2);
        r2 = "_";
        r1.append(r2);
        r4 = r3.id;
        r1.append(r4);
        r2 = "_";
        r1.append(r2);
        r2 = r3.version;
        r1.append(r2);
        r1 = r1.toString();
        goto L_0x0147;
    L_0x016d:
        r2 = org.telegram.messenger.FileLoader.getDocumentFileName(r3);
        if (r2 == 0) goto L_0x0184;
    L_0x0173:
        r4 = 46;
        r4 = r2.lastIndexOf(r4);
        r5 = r4;
        r8 = -1;
        if (r4 != r8) goto L_0x017e;
    L_0x017d:
        goto L_0x0184;
        r2 = r2.substring(r5);
        goto L_0x0186;
    L_0x0184:
        r2 = "";
        r4 = r2.length();
        r5 = 1;
        if (r4 > r5) goto L_0x01a0;
        r4 = r3.mime_type;
        if (r4 == 0) goto L_0x019e;
        r4 = r3.mime_type;
        r8 = "video/mp4";
        r4 = r4.equals(r8);
        if (r4 == 0) goto L_0x019e;
        r2 = ".mp4";
        goto L_0x01a0;
        r2 = "";
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r4.append(r1);
        r4.append(r2);
        r6 = r4.toString();
        if (r17 == 0) goto L_0x01c8;
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r8 = r17;
        r4.append(r8);
        r5 = ".";
        r4.append(r5);
        r4.append(r9);
        r7 = r4.toString();
        goto L_0x01ca;
        r8 = r17;
        r4 = org.telegram.messenger.MessageObject.isGifDocument(r3);
        if (r4 != 0) goto L_0x01dc;
        r4 = r0;
        r4 = (org.telegram.tgnet.TLRPC.Document) r4;
        r4 = org.telegram.messenger.MessageObject.isRoundVideoDocument(r4);
        if (r4 != 0) goto L_0x01dc;
        r20 = 1;
        goto L_0x01de;
        r20 = 0;
        r2 = r20;
        r5 = r2;
        goto L_0x01ed;
    L_0x01e2:
        r22 = r5;
        r8 = r17;
    L_0x01e6:
        return;
    L_0x01e7:
        r21 = r1;
        r22 = r5;
        r8 = r17;
    L_0x01ed:
        if (r0 != r15) goto L_0x01f8;
        r0 = 0;
        r1 = 0;
        r2 = 0;
        r17 = r0;
        r16 = r2;
        goto L_0x0086;
        r17 = r0;
        r22 = r5;
        r16 = r6;
        goto L_0x0208;
    L_0x01ff:
        r21 = r1;
        r8 = r4;
        r22 = r5;
        r17 = r0;
        r16 = r6;
    L_0x0208:
        if (r15 == 0) goto L_0x0239;
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r2 = r15.volume_id;
        r0.append(r2);
        r2 = "_";
        r0.append(r2);
        r2 = r15.local_id;
        r0.append(r2);
        r4 = r0.toString();
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r0.append(r4);
        r2 = ".";
        r0.append(r2);
        r0.append(r9);
        r0 = r0.toString();
        r18 = r0;
        goto L_0x023c;
        r18 = r7;
        r4 = r8;
        r8 = r29.getFilter();
        r7 = r29.getThumbFilter();
        if (r1 == 0) goto L_0x025f;
        if (r8 == 0) goto L_0x025f;
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r0.append(r1);
        r2 = "@";
        r0.append(r2);
        r0.append(r8);
        r0 = r0.toString();
        r19 = r0;
        goto L_0x0261;
        r19 = r1;
        if (r4 == 0) goto L_0x027c;
        if (r7 == 0) goto L_0x027c;
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r0.append(r4);
        r1 = "@";
        r0.append(r1);
        r0.append(r7);
        r0 = r0.toString();
        r20 = r0;
        goto L_0x027e;
        r20 = r4;
        r0 = 2;
        if (r10 == 0) goto L_0x02b8;
        r6 = 0;
        r21 = 0;
        r24 = 1;
        if (r14 == 0) goto L_0x028b;
        r23 = r0;
        goto L_0x028d;
        r23 = 1;
        r0 = r11;
        r1 = r12;
        r2 = r20;
        r3 = r18;
        r4 = r9;
        r5 = r15;
        r25 = r7;
        r26 = r8;
        r8 = r21;
        r21 = r9;
        r9 = r24;
        r24 = r10;
        r10 = r23;
        r0.createLoadOperationForImageReceiver(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10);
        r5 = 0;
        r8 = 0;
        r9 = 1;
        r10 = 0;
        r2 = r19;
        r3 = r16;
        r4 = r21;
        r6 = r24;
        r7 = r26;
        r0.createLoadOperationForImageReceiver(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10);
        goto L_0x02f9;
        r25 = r7;
        r26 = r8;
        r21 = r9;
        r24 = r10;
        r1 = r29.getCacheType();
        if (r1 != 0) goto L_0x02c9;
        if (r22 == 0) goto L_0x02c9;
        r1 = 1;
        r27 = r1;
        r6 = 0;
        r8 = 0;
        if (r27 != 0) goto L_0x02d1;
        r9 = 1;
        goto L_0x02d3;
        r9 = r27;
        if (r14 == 0) goto L_0x02d7;
        r10 = r0;
        goto L_0x02d8;
        r10 = 1;
        r0 = r11;
        r1 = r12;
        r2 = r20;
        r3 = r18;
        r4 = r21;
        r5 = r15;
        r7 = r25;
        r0.createLoadOperationForImageReceiver(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10);
        r6 = 0;
        r8 = r29.getSize();
        r10 = 0;
        r2 = r19;
        r3 = r16;
        r5 = r17;
        r7 = r26;
        r9 = r27;
        r0.createLoadOperationForImageReceiver(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.ImageLoader.loadImageForImageReceiver(org.telegram.messenger.ImageReceiver):void");
    }

    public static ImageLoader getInstance() {
        ImageLoader localInstance = Instance;
        if (localInstance == null) {
            synchronized (ImageLoader.class) {
                localInstance = Instance;
                if (localInstance == null) {
                    ImageLoader imageLoader = new ImageLoader();
                    localInstance = imageLoader;
                    Instance = imageLoader;
                }
            }
        }
        return localInstance;
    }

    public ImageLoader() {
        int a = 0;
        this.currentHttpTasksCount = 0;
        this.httpFileLoadTasks = new LinkedList();
        this.httpFileLoadTasksByKeys = new HashMap();
        this.retryHttpsTasks = new HashMap();
        this.currentHttpFileLoadTasksCount = 0;
        this.ignoreRemoval = null;
        this.lastCacheOutTime = 0;
        this.lastImageNum = 0;
        this.lastProgressUpdateTime = 0;
        this.telegramPath = null;
        this.thumbGeneratingQueue.setPriority(1);
        this.memCache = new LruCache((Math.min(15, ((ActivityManager) ApplicationLoader.applicationContext.getSystemService("activity")).getMemoryClass() / 7) * 1024) * 1024) {
            protected int sizeOf(String key, BitmapDrawable value) {
                return value.getBitmap().getByteCount();
            }

            protected void entryRemoved(boolean evicted, String key, BitmapDrawable oldValue, BitmapDrawable newValue) {
                if (ImageLoader.this.ignoreRemoval == null || key == null || !ImageLoader.this.ignoreRemoval.equals(key)) {
                    Integer count = (Integer) ImageLoader.this.bitmapUseCounts.get(key);
                    if (count == null || count.intValue() == 0) {
                        Bitmap b = oldValue.getBitmap();
                        if (!b.isRecycled()) {
                            b.recycle();
                        }
                    }
                }
            }
        };
        SparseArray<File> mediaDirs = new SparseArray();
        File cachePath = AndroidUtilities.getCacheDir();
        if (!cachePath.isDirectory()) {
            try {
                cachePath.mkdirs();
            } catch (Throwable e) {
                FileLog.e(e);
            }
        }
        try {
            new File(cachePath, ".nomedia").createNewFile();
        } catch (Throwable e2) {
            FileLog.e(e2);
        }
        mediaDirs.put(4, cachePath);
        while (a < 3) {
            final int currentAccount = a;
            FileLoader.getInstance(a).setDelegate(new FileLoaderDelegate() {
                public void fileUploadProgressChanged(final String location, final float progress, final boolean isEncrypted) {
                    ImageLoader.this.fileProgresses.put(location, Float.valueOf(progress));
                    long currentTime = System.currentTimeMillis();
                    if (ImageLoader.this.lastProgressUpdateTime == 0 || ImageLoader.this.lastProgressUpdateTime < currentTime - 500) {
                        ImageLoader.this.lastProgressUpdateTime = currentTime;
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            public void run() {
                                NotificationCenter.getInstance(currentAccount).postNotificationName(NotificationCenter.FileUploadProgressChanged, location, Float.valueOf(progress), Boolean.valueOf(isEncrypted));
                            }
                        });
                    }
                }

                public void fileDidUploaded(String location, InputFile inputFile, InputEncryptedFile inputEncryptedFile, byte[] key, byte[] iv, long totalFileSize) {
                    final String str = location;
                    final InputFile inputFile2 = inputFile;
                    final InputEncryptedFile inputEncryptedFile2 = inputEncryptedFile;
                    final byte[] bArr = key;
                    final byte[] bArr2 = iv;
                    final long j = totalFileSize;
                    Utilities.stageQueue.postRunnable(new Runnable() {
                        public void run() {
                            AndroidUtilities.runOnUIThread(new Runnable() {
                                public void run() {
                                    NotificationCenter.getInstance(currentAccount).postNotificationName(NotificationCenter.FileDidUpload, str, inputFile2, inputEncryptedFile2, bArr, bArr2, Long.valueOf(j));
                                }
                            });
                            ImageLoader.this.fileProgresses.remove(str);
                        }
                    });
                }

                public void fileDidFailedUpload(final String location, final boolean isEncrypted) {
                    Utilities.stageQueue.postRunnable(new Runnable() {
                        public void run() {
                            AndroidUtilities.runOnUIThread(new Runnable() {
                                public void run() {
                                    NotificationCenter.getInstance(currentAccount).postNotificationName(NotificationCenter.FileDidFailUpload, location, Boolean.valueOf(isEncrypted));
                                }
                            });
                            ImageLoader.this.fileProgresses.remove(location);
                        }
                    });
                }

                public void fileDidLoaded(final String location, final File finalFile, final int type) {
                    ImageLoader.this.fileProgresses.remove(location);
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        public void run() {
                            if (SharedConfig.saveToGallery && ImageLoader.this.telegramPath != null && finalFile != null && ((location.endsWith(".mp4") || location.endsWith(".jpg")) && finalFile.toString().startsWith(ImageLoader.this.telegramPath.toString()))) {
                                AndroidUtilities.addMediaToGallery(finalFile.toString());
                            }
                            NotificationCenter.getInstance(currentAccount).postNotificationName(NotificationCenter.FileDidLoaded, location);
                            ImageLoader.this.fileDidLoaded(location, finalFile, type);
                        }
                    });
                }

                public void fileDidFailedLoad(final String location, final int canceled) {
                    ImageLoader.this.fileProgresses.remove(location);
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        public void run() {
                            ImageLoader.this.fileDidFailedLoad(location, canceled);
                            NotificationCenter.getInstance(currentAccount).postNotificationName(NotificationCenter.FileDidFailedLoad, location, Integer.valueOf(canceled));
                        }
                    });
                }

                public void fileLoadProgressChanged(final String location, final float progress) {
                    ImageLoader.this.fileProgresses.put(location, Float.valueOf(progress));
                    long currentTime = System.currentTimeMillis();
                    if (ImageLoader.this.lastProgressUpdateTime == 0 || ImageLoader.this.lastProgressUpdateTime < currentTime - 500) {
                        ImageLoader.this.lastProgressUpdateTime = currentTime;
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            public void run() {
                                NotificationCenter.getInstance(currentAccount).postNotificationName(NotificationCenter.FileLoadProgressChanged, location, Float.valueOf(progress));
                            }
                        });
                    }
                }
            });
            a++;
        }
        FileLoader.setMediaDirs(mediaDirs);
        BroadcastReceiver receiver = new BroadcastReceiver() {
            public void onReceive(Context arg0, Intent intent) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("file system changed");
                }
                Runnable r = new Runnable() {
                    public void run() {
                        ImageLoader.this.checkMediaPaths();
                    }
                };
                if ("android.intent.action.MEDIA_UNMOUNTED".equals(intent.getAction())) {
                    AndroidUtilities.runOnUIThread(r, 1000);
                } else {
                    r.run();
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.MEDIA_BAD_REMOVAL");
        filter.addAction("android.intent.action.MEDIA_CHECKING");
        filter.addAction("android.intent.action.MEDIA_EJECT");
        filter.addAction("android.intent.action.MEDIA_MOUNTED");
        filter.addAction("android.intent.action.MEDIA_NOFS");
        filter.addAction("android.intent.action.MEDIA_REMOVED");
        filter.addAction("android.intent.action.MEDIA_SHARED");
        filter.addAction("android.intent.action.MEDIA_UNMOUNTABLE");
        filter.addAction("android.intent.action.MEDIA_UNMOUNTED");
        filter.addDataScheme("file");
        try {
            ApplicationLoader.applicationContext.registerReceiver(receiver, filter);
        } catch (Throwable th) {
        }
        checkMediaPaths();
    }

    public void checkMediaPaths() {
        this.cacheOutQueue.postRunnable(new Runnable() {
            public void run() {
                final SparseArray<File> paths = ImageLoader.this.createMediaPaths();
                AndroidUtilities.runOnUIThread(new Runnable() {
                    public void run() {
                        FileLoader.setMediaDirs(paths);
                    }
                });
            }
        });
    }

    public SparseArray<File> createMediaPaths() {
        SparseArray<File> mediaDirs = new SparseArray();
        File cachePath = AndroidUtilities.getCacheDir();
        if (!cachePath.isDirectory()) {
            try {
                cachePath.mkdirs();
            } catch (Throwable e) {
                FileLog.e(e);
            }
        }
        try {
            new File(cachePath, ".nomedia").createNewFile();
        } catch (Throwable e2) {
            FileLog.e(e2);
        }
        mediaDirs.put(4, cachePath);
        if (BuildVars.LOGS_ENABLED) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("cache path = ");
            stringBuilder.append(cachePath);
            FileLog.d(stringBuilder.toString());
        }
        try {
            if ("mounted".equals(Environment.getExternalStorageState())) {
                this.telegramPath = new File(Environment.getExternalStorageDirectory(), "Telegram");
                this.telegramPath.mkdirs();
                if (this.telegramPath.isDirectory()) {
                    File imagePath;
                    StringBuilder stringBuilder2;
                    try {
                        imagePath = new File(this.telegramPath, "Telegram Images");
                        imagePath.mkdir();
                        if (imagePath.isDirectory() && canMoveFiles(cachePath, imagePath, 0)) {
                            mediaDirs.put(0, imagePath);
                            if (BuildVars.LOGS_ENABLED) {
                                stringBuilder2 = new StringBuilder();
                                stringBuilder2.append("image path = ");
                                stringBuilder2.append(imagePath);
                                FileLog.d(stringBuilder2.toString());
                            }
                        }
                    } catch (Throwable e22) {
                        FileLog.e(e22);
                    }
                    try {
                        imagePath = new File(this.telegramPath, "Telegram Video");
                        imagePath.mkdir();
                        if (imagePath.isDirectory() && canMoveFiles(cachePath, imagePath, 2)) {
                            mediaDirs.put(2, imagePath);
                            if (BuildVars.LOGS_ENABLED) {
                                stringBuilder2 = new StringBuilder();
                                stringBuilder2.append("video path = ");
                                stringBuilder2.append(imagePath);
                                FileLog.d(stringBuilder2.toString());
                            }
                        }
                    } catch (Throwable e222) {
                        FileLog.e(e222);
                    }
                    try {
                        imagePath = new File(this.telegramPath, "Telegram Audio");
                        imagePath.mkdir();
                        if (imagePath.isDirectory() && canMoveFiles(cachePath, imagePath, 1)) {
                            new File(imagePath, ".nomedia").createNewFile();
                            mediaDirs.put(1, imagePath);
                            if (BuildVars.LOGS_ENABLED) {
                                stringBuilder2 = new StringBuilder();
                                stringBuilder2.append("audio path = ");
                                stringBuilder2.append(imagePath);
                                FileLog.d(stringBuilder2.toString());
                            }
                        }
                    } catch (Throwable e2222) {
                        FileLog.e(e2222);
                    }
                    try {
                        imagePath = new File(this.telegramPath, "Telegram Documents");
                        imagePath.mkdir();
                        if (imagePath.isDirectory() && canMoveFiles(cachePath, imagePath, 3)) {
                            new File(imagePath, ".nomedia").createNewFile();
                            mediaDirs.put(3, imagePath);
                            if (BuildVars.LOGS_ENABLED) {
                                stringBuilder2 = new StringBuilder();
                                stringBuilder2.append("documents path = ");
                                stringBuilder2.append(imagePath);
                                FileLog.d(stringBuilder2.toString());
                            }
                        }
                    } catch (Throwable e22222) {
                        FileLog.e(e22222);
                    }
                }
            } else if (BuildVars.LOGS_ENABLED) {
                FileLog.d("this Android can't rename files");
            }
            SharedConfig.checkSaveToGalleryFiles();
        } catch (Throwable e222222) {
            FileLog.e(e222222);
        }
        return mediaDirs;
    }

    public Float getFileProgress(String location) {
        if (location == null) {
            return null;
        }
        return (Float) this.fileProgresses.get(location);
    }

    private void performReplace(String oldKey, String newKey) {
        BitmapDrawable b = this.memCache.get(oldKey);
        if (b != null) {
            BitmapDrawable oldBitmap = this.memCache.get(newKey);
            boolean dontChange = false;
            if (!(oldBitmap == null || oldBitmap.getBitmap() == null || b.getBitmap() == null)) {
                Bitmap oldBitmapObject = oldBitmap.getBitmap();
                Bitmap newBitmapObject = b.getBitmap();
                if (oldBitmapObject.getWidth() > newBitmapObject.getWidth() || oldBitmapObject.getHeight() > newBitmapObject.getHeight()) {
                    dontChange = true;
                }
            }
            if (dontChange) {
                this.memCache.remove(oldKey);
            } else {
                this.ignoreRemoval = oldKey;
                this.memCache.remove(oldKey);
                this.memCache.put(newKey, b);
                this.ignoreRemoval = null;
            }
        }
        Integer val = (Integer) this.bitmapUseCounts.get(oldKey);
        if (val != null) {
            this.bitmapUseCounts.put(newKey, val);
            this.bitmapUseCounts.remove(oldKey);
        }
    }

    public void incrementUseCount(String key) {
        Integer count = (Integer) this.bitmapUseCounts.get(key);
        if (count == null) {
            this.bitmapUseCounts.put(key, Integer.valueOf(1));
        } else {
            this.bitmapUseCounts.put(key, Integer.valueOf(count.intValue() + 1));
        }
    }

    public boolean decrementUseCount(String key) {
        Integer count = (Integer) this.bitmapUseCounts.get(key);
        if (count == null) {
            return true;
        }
        if (count.intValue() == 1) {
            this.bitmapUseCounts.remove(key);
            return true;
        }
        this.bitmapUseCounts.put(key, Integer.valueOf(count.intValue() - 1));
        return false;
    }

    public void removeImage(String key) {
        this.bitmapUseCounts.remove(key);
        this.memCache.remove(key);
    }

    public boolean isInCache(String key) {
        return this.memCache.get(key) != null;
    }

    public void clearMemory() {
        this.memCache.evictAll();
    }

    private void removeFromWaitingForThumb(int TAG) {
        String location = (String) this.waitingForQualityThumbByTag.get(TAG);
        if (location != null) {
            ThumbGenerateInfo info = (ThumbGenerateInfo) this.waitingForQualityThumb.get(location);
            if (info != null) {
                info.count = info.count - 1;
                if (info.count == 0) {
                    this.waitingForQualityThumb.remove(location);
                }
            }
            this.waitingForQualityThumbByTag.remove(TAG);
        }
    }

    public void cancelLoadingForImageReceiver(final ImageReceiver imageReceiver, final int type) {
        if (imageReceiver != null) {
            this.imageLoadQueue.postRunnable(new Runnable() {
                public void run() {
                    int start = 0;
                    int count = 2;
                    if (type == 1) {
                        count = 1;
                    } else if (type == 2) {
                        start = 1;
                    }
                    int a = start;
                    while (a < count) {
                        int TAG = imageReceiver.getTag(a == 0);
                        if (a == 0) {
                            ImageLoader.this.removeFromWaitingForThumb(TAG);
                        }
                        if (TAG != 0) {
                            CacheImage ei = (CacheImage) ImageLoader.this.imageLoadingByTag.get(TAG);
                            if (ei != null) {
                                ei.removeImageReceiver(imageReceiver);
                            }
                        }
                        a++;
                    }
                }
            });
        }
    }

    public BitmapDrawable getImageFromMemory(String key) {
        return this.memCache.get(key);
    }

    public BitmapDrawable getImageFromMemory(TLObject fileLocation, String httpUrl, String filter) {
        if (fileLocation == null && httpUrl == null) {
            return null;
        }
        String key = null;
        if (httpUrl != null) {
            key = Utilities.MD5(httpUrl);
        } else if (fileLocation instanceof FileLocation) {
            FileLocation location = (FileLocation) fileLocation;
            r2 = new StringBuilder();
            r2.append(location.volume_id);
            r2.append("_");
            r2.append(location.local_id);
            key = r2.toString();
        } else if (fileLocation instanceof Document) {
            Document location2 = (Document) fileLocation;
            if (location2.version == 0) {
                r2 = new StringBuilder();
                r2.append(location2.dc_id);
                r2.append("_");
                r2.append(location2.id);
                key = r2.toString();
            } else {
                r2 = new StringBuilder();
                r2.append(location2.dc_id);
                r2.append("_");
                r2.append(location2.id);
                r2.append("_");
                r2.append(location2.version);
                key = r2.toString();
            }
        } else if (fileLocation instanceof TL_webDocument) {
            key = Utilities.MD5(((TL_webDocument) fileLocation).url);
        }
        if (filter != null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(key);
            stringBuilder.append("@");
            stringBuilder.append(filter);
            key = stringBuilder.toString();
        }
        return this.memCache.get(key);
    }

    private void replaceImageInCacheInternal(String oldKey, String newKey, FileLocation newLocation) {
        ArrayList<String> arr = this.memCache.getFilterKeys(oldKey);
        if (arr != null) {
            for (int a = 0; a < arr.size(); a++) {
                String filter = (String) arr.get(a);
                String oldK = new StringBuilder();
                oldK.append(oldKey);
                oldK.append("@");
                oldK.append(filter);
                oldK = oldK.toString();
                String newK = new StringBuilder();
                newK.append(newKey);
                newK.append("@");
                newK.append(filter);
                performReplace(oldK, newK.toString());
                NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.didReplacedPhotoInMemCache, oldK, newK, newLocation);
            }
            return;
        }
        performReplace(oldKey, newKey);
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.didReplacedPhotoInMemCache, oldKey, newKey, newLocation);
    }

    public void replaceImageInCache(final String oldKey, final String newKey, final FileLocation newLocation, boolean post) {
        if (post) {
            AndroidUtilities.runOnUIThread(new Runnable() {
                public void run() {
                    ImageLoader.this.replaceImageInCacheInternal(oldKey, newKey, newLocation);
                }
            });
        } else {
            replaceImageInCacheInternal(oldKey, newKey, newLocation);
        }
    }

    public void putImageToCache(BitmapDrawable bitmap, String key) {
        this.memCache.put(key, bitmap);
    }

    private void generateThumb(int mediaType, File originalPath, FileLocation thumbLocation, String filter) {
        if ((mediaType == 0 || mediaType == 2 || mediaType == 3) && originalPath != null) {
            if (thumbLocation != null) {
                if (((ThumbGenerateTask) this.thumbGenerateTasks.get(FileLoader.getAttachFileName(thumbLocation))) == null) {
                    this.thumbGeneratingQueue.postRunnable(new ThumbGenerateTask(mediaType, originalPath, thumbLocation, filter));
                }
            }
        }
    }

    public void cancelForceLoadingForImageReceiver(ImageReceiver imageReceiver) {
        if (imageReceiver != null) {
            final String key = imageReceiver.getKey();
            if (key != null) {
                this.imageLoadQueue.postRunnable(new Runnable() {
                    public void run() {
                        ImageLoader.this.forceLoadingImages.remove(key);
                    }
                });
            }
        }
    }

    private void createLoadOperationForImageReceiver(ImageReceiver imageReceiver, String key, String url, String ext, TLObject imageLocation, String httpLocation, String filter, int size, int cacheType, int thumb) {
        ImageLoader imageLoader = this;
        ImageReceiver imageReceiver2 = imageReceiver;
        if (!(imageReceiver2 == null || url == null)) {
            if (key != null) {
                int TAG = imageReceiver2.getTag(thumb != 0 ? 1 : 0);
                if (TAG == 0) {
                    int i = imageLoader.lastImageNum;
                    TAG = i;
                    imageReceiver2.setTag(i, thumb != 0);
                    imageLoader.lastImageNum++;
                    if (imageLoader.lastImageNum == ConnectionsManager.DEFAULT_DATACENTER_ID) {
                        imageLoader.lastImageNum = 0;
                    }
                }
                final int finalTag = TAG;
                boolean finalIsNeedsQualityThumb = imageReceiver.isNeedsQualityThumb();
                MessageObject parentMessageObject = imageReceiver.getParentMessageObject();
                boolean shouldGenerateQualityThumb = imageReceiver.isShouldGenerateQualityThumb();
                int currentAccount = imageReceiver.getcurrentAccount();
                TAG = thumb;
                final String str = url;
                final String str2 = key;
                final ImageReceiver imageReceiver3 = imageReceiver2;
                final String str3 = filter;
                final String str4 = httpLocation;
                final boolean z = finalIsNeedsQualityThumb;
                final MessageObject messageObject = parentMessageObject;
                final TLObject tLObject = imageLocation;
                AnonymousClass8 anonymousClass8 = r0;
                final boolean z2 = shouldGenerateQualityThumb;
                DispatchQueue dispatchQueue = imageLoader.imageLoadQueue;
                final int i2 = cacheType;
                final int i3 = size;
                final String str5 = ext;
                final int i4 = currentAccount;
                AnonymousClass8 anonymousClass82 = new Runnable() {
                    public void run() {
                        boolean added = false;
                        if (TAG != 2) {
                            CacheImage alreadyLoadingUrl = (CacheImage) ImageLoader.this.imageLoadingByUrl.get(str);
                            CacheImage alreadyLoadingCache = (CacheImage) ImageLoader.this.imageLoadingByKeys.get(str2);
                            CacheImage alreadyLoadingImage = (CacheImage) ImageLoader.this.imageLoadingByTag.get(finalTag);
                            if (alreadyLoadingImage != null) {
                                if (alreadyLoadingImage == alreadyLoadingCache) {
                                    added = true;
                                } else if (alreadyLoadingImage == alreadyLoadingUrl) {
                                    if (alreadyLoadingCache == null) {
                                        alreadyLoadingImage.replaceImageReceiver(imageReceiver3, str2, str3, TAG != 0);
                                    }
                                    added = true;
                                } else {
                                    alreadyLoadingImage.removeImageReceiver(imageReceiver3);
                                }
                            }
                            if (!(added || alreadyLoadingCache == null)) {
                                alreadyLoadingCache.addImageReceiver(imageReceiver3, str2, str3, TAG != 0);
                                added = true;
                            }
                            if (!(added || alreadyLoadingUrl == null)) {
                                alreadyLoadingUrl.addImageReceiver(imageReceiver3, str2, str3, TAG != 0);
                                added = true;
                            }
                        }
                        if (!added) {
                            File directory;
                            StringBuilder stringBuilder;
                            String location;
                            boolean onlyCache = false;
                            File cacheFile = null;
                            boolean cacheFileExists = false;
                            if (str4 != null) {
                                if (!str4.startsWith("http")) {
                                    onlyCache = true;
                                    int idx;
                                    if (str4.startsWith("thumb://")) {
                                        idx = str4.indexOf(":", 8);
                                        if (idx >= 0) {
                                            cacheFile = new File(str4.substring(idx + 1));
                                        }
                                    } else if (str4.startsWith("vthumb://")) {
                                        idx = str4.indexOf(":", 9);
                                        if (idx >= 0) {
                                            cacheFile = new File(str4.substring(idx + 1));
                                        }
                                    } else {
                                        cacheFile = new File(str4);
                                    }
                                }
                            } else if (TAG != 0) {
                                if (z) {
                                    directory = FileLoader.getDirectory(4);
                                    stringBuilder = new StringBuilder();
                                    stringBuilder.append("q_");
                                    stringBuilder.append(str);
                                    cacheFile = new File(directory, stringBuilder.toString());
                                    if (cacheFile.exists()) {
                                        cacheFileExists = true;
                                    } else {
                                        cacheFile = null;
                                    }
                                }
                                if (messageObject != null) {
                                    File attachPath = null;
                                    if (messageObject.messageOwner.attachPath != null && messageObject.messageOwner.attachPath.length() > 0) {
                                        attachPath = new File(messageObject.messageOwner.attachPath);
                                        if (!attachPath.exists()) {
                                            attachPath = null;
                                        }
                                    }
                                    if (attachPath == null) {
                                        attachPath = FileLoader.getPathToMessage(messageObject.messageOwner);
                                    }
                                    if (z && cacheFile == null) {
                                        location = messageObject.getFileName();
                                        ThumbGenerateInfo info = (ThumbGenerateInfo) ImageLoader.this.waitingForQualityThumb.get(location);
                                        if (info == null) {
                                            info = new ThumbGenerateInfo();
                                            info.fileLocation = (FileLocation) tLObject;
                                            info.filter = str3;
                                            ImageLoader.this.waitingForQualityThumb.put(location, info);
                                        }
                                        info.count = info.count + 1;
                                        ImageLoader.this.waitingForQualityThumbByTag.put(finalTag, location);
                                    }
                                    if (attachPath.exists() && z2) {
                                        ImageLoader.this.generateThumb(messageObject.getFileType(), attachPath, (FileLocation) tLObject, str3);
                                    }
                                }
                            }
                            if (TAG != 2) {
                                boolean isEncrypted;
                                CacheImage img;
                                File directory2;
                                StringBuilder stringBuilder2;
                                FileLocation location2;
                                int localCacheType;
                                String file;
                                File cacheDir;
                                StringBuilder stringBuilder3;
                                if (!(tLObject instanceof TL_documentEncrypted)) {
                                    if (!(tLObject instanceof TL_fileEncryptedLocation)) {
                                        isEncrypted = false;
                                        img = new CacheImage();
                                        if (str4 == null && !str4.startsWith("vthumb") && !str4.startsWith("thumb")) {
                                            location = ImageLoader.getHttpUrlExtension(str4, "jpg");
                                            if (location.equals("mp4") || location.equals("gif")) {
                                                img.animatedFile = true;
                                            }
                                        } else if (((tLObject instanceof TL_webDocument) && MessageObject.isGifDocument((TL_webDocument) tLObject)) || ((tLObject instanceof Document) && (MessageObject.isGifDocument((Document) tLObject) || MessageObject.isRoundVideoDocument((Document) tLObject)))) {
                                            img.animatedFile = true;
                                        }
                                        if (cacheFile == null) {
                                            if (i2 == 0 && i3 > 0 && str4 == null) {
                                                if (!isEncrypted) {
                                                    cacheFile = tLObject instanceof Document ? MessageObject.isVideoDocument((Document) tLObject) ? new File(FileLoader.getDirectory(2), str) : new File(FileLoader.getDirectory(3), str) : tLObject instanceof TL_webDocument ? new File(FileLoader.getDirectory(3), str) : new File(FileLoader.getDirectory(0), str);
                                                }
                                            }
                                            cacheFile = new File(FileLoader.getDirectory(4), str);
                                            if (cacheFile.exists()) {
                                                cacheFileExists = true;
                                            } else if (i2 == 2) {
                                                directory2 = FileLoader.getDirectory(4);
                                                stringBuilder2 = new StringBuilder();
                                                stringBuilder2.append(str);
                                                stringBuilder2.append(".enc");
                                                cacheFile = new File(directory2, stringBuilder2.toString());
                                            }
                                        }
                                        img.selfThumb = TAG == 0;
                                        img.key = str2;
                                        img.filter = str3;
                                        img.httpUrl = str4;
                                        img.ext = str5;
                                        img.currentAccount = i4;
                                        if (i2 == 2) {
                                            directory = FileLoader.getInternalCacheDir();
                                            stringBuilder = new StringBuilder();
                                            stringBuilder.append(str);
                                            stringBuilder.append(".enc.key");
                                            img.encryptionKeyPath = new File(directory, stringBuilder.toString());
                                        }
                                        img.addImageReceiver(imageReceiver3, str2, str3, TAG == 0);
                                        if (!(onlyCache || cacheFileExists)) {
                                            if (cacheFile.exists()) {
                                                img.url = str;
                                                img.location = tLObject;
                                                ImageLoader.this.imageLoadingByUrl.put(str, img);
                                                if (str4 != null) {
                                                    if (tLObject instanceof FileLocation) {
                                                        location2 = tLObject;
                                                        localCacheType = i2;
                                                        if (localCacheType == 0 && (i3 <= 0 || location2.key != null)) {
                                                            localCacheType = 1;
                                                        }
                                                        FileLoader.getInstance(i4).loadFile(location2, str5, i3, localCacheType);
                                                    } else if (tLObject instanceof Document) {
                                                        FileLoader.getInstance(i4).loadFile((Document) tLObject, true, i2);
                                                    } else if (tLObject instanceof TL_webDocument) {
                                                        FileLoader.getInstance(i4).loadFile((TL_webDocument) tLObject, true, i2);
                                                    }
                                                    if (imageReceiver3.isForceLoding()) {
                                                        ImageLoader.this.forceLoadingImages.put(img.key, Integer.valueOf(0));
                                                        return;
                                                    }
                                                    return;
                                                }
                                                file = Utilities.MD5(str4);
                                                cacheDir = FileLoader.getDirectory(4);
                                                stringBuilder3 = new StringBuilder();
                                                stringBuilder3.append(file);
                                                stringBuilder3.append("_temp.jpg");
                                                img.tempFilePath = new File(cacheDir, stringBuilder3.toString());
                                                img.finalFilePath = cacheFile;
                                                img.httpTask = new HttpImageTask(img, i3);
                                                ImageLoader.this.httpTasks.add(img.httpTask);
                                                ImageLoader.this.runHttpTasks(false);
                                                return;
                                            }
                                        }
                                        img.finalFilePath = cacheFile;
                                        img.cacheTask = new CacheOutTask(img);
                                        ImageLoader.this.imageLoadingByKeys.put(str2, img);
                                        if (TAG == 0) {
                                            ImageLoader.this.cacheThumbOutQueue.postRunnable(img.cacheTask);
                                        } else {
                                            ImageLoader.this.cacheOutQueue.postRunnable(img.cacheTask);
                                        }
                                    }
                                }
                                isEncrypted = true;
                                img = new CacheImage();
                                if (str4 == null) {
                                }
                                img.animatedFile = true;
                                if (cacheFile == null) {
                                    if (!isEncrypted) {
                                        cacheFile = new File(FileLoader.getDirectory(4), str);
                                        if (cacheFile.exists()) {
                                            cacheFileExists = true;
                                        } else if (i2 == 2) {
                                            directory2 = FileLoader.getDirectory(4);
                                            stringBuilder2 = new StringBuilder();
                                            stringBuilder2.append(str);
                                            stringBuilder2.append(".enc");
                                            cacheFile = new File(directory2, stringBuilder2.toString());
                                        }
                                    } else if (tLObject instanceof Document) {
                                        if (MessageObject.isVideoDocument((Document) tLObject)) {
                                        }
                                    }
                                }
                                if (TAG == 0) {
                                }
                                img.selfThumb = TAG == 0;
                                img.key = str2;
                                img.filter = str3;
                                img.httpUrl = str4;
                                img.ext = str5;
                                img.currentAccount = i4;
                                if (i2 == 2) {
                                    directory = FileLoader.getInternalCacheDir();
                                    stringBuilder = new StringBuilder();
                                    stringBuilder.append(str);
                                    stringBuilder.append(".enc.key");
                                    img.encryptionKeyPath = new File(directory, stringBuilder.toString());
                                }
                                if (TAG == 0) {
                                }
                                img.addImageReceiver(imageReceiver3, str2, str3, TAG == 0);
                                if (cacheFile.exists()) {
                                    img.url = str;
                                    img.location = tLObject;
                                    ImageLoader.this.imageLoadingByUrl.put(str, img);
                                    if (str4 != null) {
                                        file = Utilities.MD5(str4);
                                        cacheDir = FileLoader.getDirectory(4);
                                        stringBuilder3 = new StringBuilder();
                                        stringBuilder3.append(file);
                                        stringBuilder3.append("_temp.jpg");
                                        img.tempFilePath = new File(cacheDir, stringBuilder3.toString());
                                        img.finalFilePath = cacheFile;
                                        img.httpTask = new HttpImageTask(img, i3);
                                        ImageLoader.this.httpTasks.add(img.httpTask);
                                        ImageLoader.this.runHttpTasks(false);
                                        return;
                                    }
                                    if (tLObject instanceof FileLocation) {
                                        location2 = tLObject;
                                        localCacheType = i2;
                                        localCacheType = 1;
                                        FileLoader.getInstance(i4).loadFile(location2, str5, i3, localCacheType);
                                    } else if (tLObject instanceof Document) {
                                        FileLoader.getInstance(i4).loadFile((Document) tLObject, true, i2);
                                    } else if (tLObject instanceof TL_webDocument) {
                                        FileLoader.getInstance(i4).loadFile((TL_webDocument) tLObject, true, i2);
                                    }
                                    if (imageReceiver3.isForceLoding()) {
                                        ImageLoader.this.forceLoadingImages.put(img.key, Integer.valueOf(0));
                                        return;
                                    }
                                    return;
                                }
                                img.finalFilePath = cacheFile;
                                img.cacheTask = new CacheOutTask(img);
                                ImageLoader.this.imageLoadingByKeys.put(str2, img);
                                if (TAG == 0) {
                                    ImageLoader.this.cacheOutQueue.postRunnable(img.cacheTask);
                                } else {
                                    ImageLoader.this.cacheThumbOutQueue.postRunnable(img.cacheTask);
                                }
                            }
                        }
                    }
                };
                dispatchQueue.postRunnable(anonymousClass8);
            }
        }
    }

    private void httpFileLoadError(final String location) {
        this.imageLoadQueue.postRunnable(new Runnable() {
            public void run() {
                CacheImage img = (CacheImage) ImageLoader.this.imageLoadingByUrl.get(location);
                if (img != null) {
                    HttpImageTask oldTask = img.httpTask;
                    img.httpTask = new HttpImageTask(oldTask.cacheImage, oldTask.imageSize);
                    ImageLoader.this.httpTasks.add(img.httpTask);
                    ImageLoader.this.runHttpTasks(false);
                }
            }
        });
    }

    private void fileDidLoaded(final String location, final File finalFile, final int type) {
        this.imageLoadQueue.postRunnable(new Runnable() {
            public void run() {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.ImageLoader.10.run():void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
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
                r0 = org.telegram.messenger.ImageLoader.this;
                r0 = r0.waitingForQualityThumb;
                r1 = r3;
                r0 = r0.get(r1);
                r0 = (org.telegram.messenger.ImageLoader.ThumbGenerateInfo) r0;
                if (r0 == 0) goto L_0x002c;
            L_0x0010:
                r1 = org.telegram.messenger.ImageLoader.this;
                r2 = r5;
                r3 = r4;
                r4 = r0.fileLocation;
                r5 = r0.filter;
                r1.generateThumb(r2, r3, r4, r5);
                r1 = org.telegram.messenger.ImageLoader.this;
                r1 = r1.waitingForQualityThumb;
                r2 = r3;
                r1.remove(r2);
            L_0x002c:
                r1 = org.telegram.messenger.ImageLoader.this;
                r1 = r1.imageLoadingByUrl;
                r2 = r3;
                r1 = r1.get(r2);
                r1 = (org.telegram.messenger.ImageLoader.CacheImage) r1;
                if (r1 != 0) goto L_0x003d;
            L_0x003c:
                return;
            L_0x003d:
                r2 = org.telegram.messenger.ImageLoader.this;
                r2 = r2.imageLoadingByUrl;
                r3 = r3;
                r2.remove(r3);
                r2 = new java.util.ArrayList;
                r2.<init>();
                r3 = 0;
                r4 = r3;
                r5 = r1.imageReceiverArray;
                r5 = r5.size();
                if (r4 >= r5) goto L_0x00d2;
            L_0x0057:
                r5 = r1.keys;
                r5 = r5.get(r4);
                r5 = (java.lang.String) r5;
                r6 = r1.filters;
                r6 = r6.get(r4);
                r6 = (java.lang.String) r6;
                r7 = r1.thumbs;
                r7 = r7.get(r4);
                r7 = (java.lang.Boolean) r7;
                r8 = r1.imageReceiverArray;
                r8 = r8.get(r4);
                r8 = (org.telegram.messenger.ImageReceiver) r8;
                r9 = org.telegram.messenger.ImageLoader.this;
                r9 = r9.imageLoadingByKeys;
                r9 = r9.get(r5);
                r9 = (org.telegram.messenger.ImageLoader.CacheImage) r9;
                if (r9 != 0) goto L_0x00c7;
                r10 = new org.telegram.messenger.ImageLoader$CacheImage;
                r11 = org.telegram.messenger.ImageLoader.this;
                r12 = 0;
                r10.<init>();
                r9 = r10;
                r10 = r1.currentAccount;
                r9.currentAccount = r10;
                r10 = r4;
                r9.finalFilePath = r10;
                r9.key = r5;
                r10 = r1.httpUrl;
                r9.httpUrl = r10;
                r10 = r7.booleanValue();
                r9.selfThumb = r10;
                r10 = r1.ext;
                r9.ext = r10;
                r10 = r1.encryptionKeyPath;
                r9.encryptionKeyPath = r10;
                r10 = new org.telegram.messenger.ImageLoader$CacheOutTask;
                r11 = org.telegram.messenger.ImageLoader.this;
                r10.<init>(r9);
                r9.cacheTask = r10;
                r9.filter = r6;
                r10 = r1.animatedFile;
                r9.animatedFile = r10;
                r10 = org.telegram.messenger.ImageLoader.this;
                r10 = r10.imageLoadingByKeys;
                r10.put(r5, r9);
                r10 = r9.cacheTask;
                r2.add(r10);
                r10 = r7.booleanValue();
                r9.addImageReceiver(r8, r5, r6, r10);
                r4 = r4 + 1;
                goto L_0x004f;
                r4 = r2.size();
                if (r3 >= r4) goto L_0x00fd;
                r4 = r2.get(r3);
                r4 = (org.telegram.messenger.ImageLoader.CacheOutTask) r4;
                r5 = r4.cacheImage;
                r5 = r5.selfThumb;
                if (r5 == 0) goto L_0x00f1;
                r5 = org.telegram.messenger.ImageLoader.this;
                r5 = r5.cacheThumbOutQueue;
                r5.postRunnable(r4);
                goto L_0x00fa;
                r5 = org.telegram.messenger.ImageLoader.this;
                r5 = r5.cacheOutQueue;
                r5.postRunnable(r4);
                r3 = r3 + 1;
                goto L_0x00d3;
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.ImageLoader.10.run():void");
            }
        });
    }

    private void fileDidFailedLoad(final String location, int canceled) {
        if (canceled != 1) {
            this.imageLoadQueue.postRunnable(new Runnable() {
                public void run() {
                    CacheImage img = (CacheImage) ImageLoader.this.imageLoadingByUrl.get(location);
                    if (img != null) {
                        img.setImageAndClear(null);
                    }
                }
            });
        }
    }

    private void runHttpTasks(boolean complete) {
        if (complete) {
            this.currentHttpTasksCount--;
        }
        while (this.currentHttpTasksCount < 4 && !this.httpTasks.isEmpty()) {
            ((HttpImageTask) this.httpTasks.poll()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[]{null, null, null});
            this.currentHttpTasksCount++;
        }
    }

    public boolean isLoadingHttpFile(String url) {
        return this.httpFileLoadTasksByKeys.containsKey(url);
    }

    public void loadHttpFile(String url, String defaultExt, int currentAccount) {
        if (!(url == null || url.length() == 0)) {
            if (!this.httpFileLoadTasksByKeys.containsKey(url)) {
                String ext = getHttpUrlExtension(url, defaultExt);
                File directory = FileLoader.getDirectory(4);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(Utilities.MD5(url));
                stringBuilder.append("_temp.");
                stringBuilder.append(ext);
                File file = new File(directory, stringBuilder.toString());
                file.delete();
                HttpFileTask task = new HttpFileTask(url, file, ext, currentAccount);
                this.httpFileLoadTasks.add(task);
                this.httpFileLoadTasksByKeys.put(url, task);
                runHttpFileLoadTasks(null, 0);
            }
        }
    }

    public void cancelLoadHttpFile(String url) {
        HttpFileTask task = (HttpFileTask) this.httpFileLoadTasksByKeys.get(url);
        if (task != null) {
            task.cancel(true);
            this.httpFileLoadTasksByKeys.remove(url);
            this.httpFileLoadTasks.remove(task);
        }
        Runnable runnable = (Runnable) this.retryHttpsTasks.get(url);
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
        }
        runHttpFileLoadTasks(null, 0);
    }

    private void runHttpFileLoadTasks(final HttpFileTask oldTask, final int reason) {
        AndroidUtilities.runOnUIThread(new Runnable() {
            public void run() {
                if (oldTask != null) {
                    ImageLoader.this.currentHttpFileLoadTasksCount = ImageLoader.this.currentHttpFileLoadTasksCount - 1;
                }
                if (oldTask != null) {
                    if (reason == 1) {
                        if (oldTask.canRetry) {
                            final HttpFileTask httpFileTask = new HttpFileTask(oldTask.url, oldTask.tempFile, oldTask.ext, oldTask.currentAccount);
                            Runnable runnable = new Runnable() {
                                public void run() {
                                    ImageLoader.this.httpFileLoadTasks.add(httpFileTask);
                                    ImageLoader.this.runHttpFileLoadTasks(null, 0);
                                }
                            };
                            ImageLoader.this.retryHttpsTasks.put(oldTask.url, runnable);
                            AndroidUtilities.runOnUIThread(runnable, 1000);
                        } else {
                            ImageLoader.this.httpFileLoadTasksByKeys.remove(oldTask.url);
                            NotificationCenter.getInstance(oldTask.currentAccount).postNotificationName(NotificationCenter.httpFileDidFailedLoad, oldTask.url, Integer.valueOf(0));
                        }
                    } else if (reason == 2) {
                        ImageLoader.this.httpFileLoadTasksByKeys.remove(oldTask.url);
                        File directory = FileLoader.getDirectory(4);
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(Utilities.MD5(oldTask.url));
                        stringBuilder.append(".");
                        stringBuilder.append(oldTask.ext);
                        File file = new File(directory, stringBuilder.toString());
                        String result = oldTask.tempFile.renameTo(file) ? file.toString() : oldTask.tempFile.toString();
                        NotificationCenter.getInstance(oldTask.currentAccount).postNotificationName(NotificationCenter.httpFileDidLoaded, oldTask.url, result);
                    }
                }
                while (ImageLoader.this.currentHttpFileLoadTasksCount < 2 && !ImageLoader.this.httpFileLoadTasks.isEmpty()) {
                    ((HttpFileTask) ImageLoader.this.httpFileLoadTasks.poll()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[]{null, null, null});
                    ImageLoader.this.currentHttpFileLoadTasksCount = ImageLoader.this.currentHttpFileLoadTasksCount + 1;
                }
            }
        });
    }

    public static void fillPhotoSizeWithBytes(PhotoSize photoSize) {
        if (photoSize != null) {
            if (photoSize.bytes == null) {
                try {
                    RandomAccessFile f = new RandomAccessFile(FileLoader.getPathToAttach(photoSize, true), "r");
                    if (((int) f.length()) < 20000) {
                        photoSize.bytes = new byte[((int) f.length())];
                        f.readFully(photoSize.bytes, 0, photoSize.bytes.length);
                    }
                } catch (Throwable e) {
                    FileLog.e(e);
                }
            }
        }
    }

    public static PhotoSize scaleAndSaveImage(Bitmap bitmap, float maxWidth, float maxHeight, int quality, boolean cache) {
        return scaleAndSaveImage(bitmap, maxWidth, maxHeight, quality, cache, 0, 0);
    }

    public static PhotoSize scaleAndSaveImage(Bitmap bitmap, float maxWidth, float maxHeight, int quality, boolean cache, int minWidth, int minHeight) {
        int i;
        int i2;
        int i3 = minWidth;
        int i4 = minHeight;
        if (bitmap == null) {
            return null;
        }
        float photoW = (float) bitmap.getWidth();
        float photoH = (float) bitmap.getHeight();
        if (photoW != 0.0f) {
            if (photoH != 0.0f) {
                boolean scaleAnyway = false;
                float scaleFactor = Math.max(photoW / maxWidth, photoH / maxHeight);
                if (!(i3 == 0 || i4 == 0 || (photoW >= ((float) i3) && photoH >= ((float) i4)))) {
                    if (photoW < ((float) i3) && photoH > ((float) i4)) {
                        scaleFactor = photoW / ((float) i3);
                    } else if (photoW <= ((float) i3) || photoH >= ((float) i4)) {
                        scaleFactor = Math.max(photoW / ((float) i3), photoH / ((float) i4));
                    } else {
                        scaleFactor = photoH / ((float) i4);
                    }
                    scaleAnyway = true;
                }
                boolean scaleAnyway2 = scaleAnyway;
                float scaleFactor2 = scaleFactor;
                int w = (int) (photoW / scaleFactor2);
                int h = (int) (photoH / scaleFactor2);
                if (h == 0) {
                    i = w;
                } else if (w == 0) {
                    i2 = h;
                    i = w;
                } else {
                    i2 = h;
                    i = w;
                    try {
                        return scaleAndSaveImageInternal(bitmap, w, h, photoW, photoH, scaleFactor2, quality, cache, scaleAnyway2);
                    } catch (Throwable th) {
                        FileLog.e(th);
                        return null;
                    }
                }
                return null;
            }
        }
        return null;
    }

    public static String getHttpUrlExtension(String url, String defaultExt) {
        String ext = null;
        String last = Uri.parse(url).getLastPathSegment();
        if (!TextUtils.isEmpty(last) && last.length() > 1) {
            url = last;
        }
        int idx = url.lastIndexOf(46);
        if (idx != -1) {
            ext = url.substring(idx + 1);
        }
        if (ext == null || ext.length() == 0 || ext.length() > 4) {
            return defaultExt;
        }
        return ext;
    }

    public static void saveMessagesThumbs(ArrayList<Message> messages) {
        if (messages != null) {
            if (!messages.isEmpty()) {
                for (int a = 0; a < messages.size(); a++) {
                    saveMessageThumbs((Message) messages.get(a));
                }
            }
        }
    }
}
