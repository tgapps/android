package org.telegram.messenger;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v13.view.inputmethod.InputContentInfoCompat;
import android.text.TextUtils;
import android.util.LongSparseArray;
import android.util.SparseArray;
import android.widget.Toast;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.telegram.messenger.MediaController.SearchImage;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.messenger.beta.R;
import org.telegram.messenger.exoplayer2.DefaultRenderersFactory;
import org.telegram.messenger.exoplayer2.util.MimeTypes;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.NativeByteBuffer;
import org.telegram.tgnet.QuickAckDelegate;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.BotInlineResult;
import org.telegram.tgnet.TLRPC.Chat;
import org.telegram.tgnet.TLRPC.ChatFull;
import org.telegram.tgnet.TLRPC.DecryptedMessage;
import org.telegram.tgnet.TLRPC.Document;
import org.telegram.tgnet.TLRPC.DocumentAttribute;
import org.telegram.tgnet.TLRPC.EncryptedChat;
import org.telegram.tgnet.TLRPC.FileLocation;
import org.telegram.tgnet.TLRPC.InputDocument;
import org.telegram.tgnet.TLRPC.InputEncryptedFile;
import org.telegram.tgnet.TLRPC.InputFile;
import org.telegram.tgnet.TLRPC.InputMedia;
import org.telegram.tgnet.TLRPC.InputPeer;
import org.telegram.tgnet.TLRPC.KeyboardButton;
import org.telegram.tgnet.TLRPC.Message;
import org.telegram.tgnet.TLRPC.MessageEntity;
import org.telegram.tgnet.TLRPC.MessageFwdHeader;
import org.telegram.tgnet.TLRPC.MessageMedia;
import org.telegram.tgnet.TLRPC.Peer;
import org.telegram.tgnet.TLRPC.PhotoSize;
import org.telegram.tgnet.TLRPC.ReplyMarkup;
import org.telegram.tgnet.TLRPC.TL_botInlineMessageMediaAuto;
import org.telegram.tgnet.TLRPC.TL_botInlineMessageMediaContact;
import org.telegram.tgnet.TLRPC.TL_botInlineMessageMediaGeo;
import org.telegram.tgnet.TLRPC.TL_botInlineMessageMediaVenue;
import org.telegram.tgnet.TLRPC.TL_botInlineMessageText;
import org.telegram.tgnet.TLRPC.TL_decryptedMessage;
import org.telegram.tgnet.TLRPC.TL_decryptedMessageActionAbortKey;
import org.telegram.tgnet.TLRPC.TL_decryptedMessageActionAcceptKey;
import org.telegram.tgnet.TLRPC.TL_decryptedMessageActionCommitKey;
import org.telegram.tgnet.TLRPC.TL_decryptedMessageActionDeleteMessages;
import org.telegram.tgnet.TLRPC.TL_decryptedMessageActionFlushHistory;
import org.telegram.tgnet.TLRPC.TL_decryptedMessageActionNoop;
import org.telegram.tgnet.TLRPC.TL_decryptedMessageActionNotifyLayer;
import org.telegram.tgnet.TLRPC.TL_decryptedMessageActionReadMessages;
import org.telegram.tgnet.TLRPC.TL_decryptedMessageActionRequestKey;
import org.telegram.tgnet.TLRPC.TL_decryptedMessageActionResend;
import org.telegram.tgnet.TLRPC.TL_decryptedMessageActionScreenshotMessages;
import org.telegram.tgnet.TLRPC.TL_decryptedMessageActionSetMessageTTL;
import org.telegram.tgnet.TLRPC.TL_decryptedMessageActionTyping;
import org.telegram.tgnet.TLRPC.TL_decryptedMessageMediaDocument;
import org.telegram.tgnet.TLRPC.TL_decryptedMessageMediaPhoto;
import org.telegram.tgnet.TLRPC.TL_decryptedMessageMediaVideo;
import org.telegram.tgnet.TLRPC.TL_document;
import org.telegram.tgnet.TLRPC.TL_documentAttributeAnimated;
import org.telegram.tgnet.TLRPC.TL_documentAttributeAudio;
import org.telegram.tgnet.TLRPC.TL_documentAttributeVideo;
import org.telegram.tgnet.TLRPC.TL_documentAttributeVideo_layer65;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.tgnet.TLRPC.TL_fileLocationUnavailable;
import org.telegram.tgnet.TLRPC.TL_game;
import org.telegram.tgnet.TLRPC.TL_geoPoint;
import org.telegram.tgnet.TLRPC.TL_inputDocument;
import org.telegram.tgnet.TLRPC.TL_inputEncryptedFile;
import org.telegram.tgnet.TLRPC.TL_inputMediaDocument;
import org.telegram.tgnet.TLRPC.TL_inputMediaGame;
import org.telegram.tgnet.TLRPC.TL_inputMediaPhoto;
import org.telegram.tgnet.TLRPC.TL_inputMediaUploadedDocument;
import org.telegram.tgnet.TLRPC.TL_inputMediaUploadedPhoto;
import org.telegram.tgnet.TLRPC.TL_inputPeerChannel;
import org.telegram.tgnet.TLRPC.TL_inputPeerEmpty;
import org.telegram.tgnet.TLRPC.TL_inputPeerUser;
import org.telegram.tgnet.TLRPC.TL_inputPhoto;
import org.telegram.tgnet.TLRPC.TL_inputSingleMedia;
import org.telegram.tgnet.TLRPC.TL_keyboardButtonBuy;
import org.telegram.tgnet.TLRPC.TL_keyboardButtonGame;
import org.telegram.tgnet.TLRPC.TL_message;
import org.telegram.tgnet.TLRPC.TL_messageActionScreenshotTaken;
import org.telegram.tgnet.TLRPC.TL_messageEncryptedAction;
import org.telegram.tgnet.TLRPC.TL_messageEntityBold;
import org.telegram.tgnet.TLRPC.TL_messageEntityCode;
import org.telegram.tgnet.TLRPC.TL_messageEntityItalic;
import org.telegram.tgnet.TLRPC.TL_messageEntityPre;
import org.telegram.tgnet.TLRPC.TL_messageEntityTextUrl;
import org.telegram.tgnet.TLRPC.TL_messageEntityUrl;
import org.telegram.tgnet.TLRPC.TL_messageFwdHeader;
import org.telegram.tgnet.TLRPC.TL_messageMediaContact;
import org.telegram.tgnet.TLRPC.TL_messageMediaDocument;
import org.telegram.tgnet.TLRPC.TL_messageMediaEmpty;
import org.telegram.tgnet.TLRPC.TL_messageMediaGame;
import org.telegram.tgnet.TLRPC.TL_messageMediaGeo;
import org.telegram.tgnet.TLRPC.TL_messageMediaGeoLive;
import org.telegram.tgnet.TLRPC.TL_messageMediaInvoice;
import org.telegram.tgnet.TLRPC.TL_messageMediaPhoto;
import org.telegram.tgnet.TLRPC.TL_messageMediaVenue;
import org.telegram.tgnet.TLRPC.TL_messageMediaWebPage;
import org.telegram.tgnet.TLRPC.TL_messageService;
import org.telegram.tgnet.TLRPC.TL_messages_botCallbackAnswer;
import org.telegram.tgnet.TLRPC.TL_messages_editMessage;
import org.telegram.tgnet.TLRPC.TL_messages_forwardMessages;
import org.telegram.tgnet.TLRPC.TL_messages_getBotCallbackAnswer;
import org.telegram.tgnet.TLRPC.TL_messages_messages;
import org.telegram.tgnet.TLRPC.TL_messages_sendBroadcast;
import org.telegram.tgnet.TLRPC.TL_messages_sendEncryptedMultiMedia;
import org.telegram.tgnet.TLRPC.TL_messages_sendMedia;
import org.telegram.tgnet.TLRPC.TL_messages_sendMessage;
import org.telegram.tgnet.TLRPC.TL_messages_sendMultiMedia;
import org.telegram.tgnet.TLRPC.TL_messages_sendScreenshotNotification;
import org.telegram.tgnet.TLRPC.TL_messages_uploadMedia;
import org.telegram.tgnet.TLRPC.TL_payments_getPaymentForm;
import org.telegram.tgnet.TLRPC.TL_payments_getPaymentReceipt;
import org.telegram.tgnet.TLRPC.TL_payments_paymentForm;
import org.telegram.tgnet.TLRPC.TL_payments_paymentReceipt;
import org.telegram.tgnet.TLRPC.TL_peerChannel;
import org.telegram.tgnet.TLRPC.TL_peerUser;
import org.telegram.tgnet.TLRPC.TL_photo;
import org.telegram.tgnet.TLRPC.TL_photoCachedSize;
import org.telegram.tgnet.TLRPC.TL_photoSize;
import org.telegram.tgnet.TLRPC.TL_photoSizeEmpty;
import org.telegram.tgnet.TLRPC.TL_updateMessageID;
import org.telegram.tgnet.TLRPC.TL_updateNewChannelMessage;
import org.telegram.tgnet.TLRPC.TL_updateNewMessage;
import org.telegram.tgnet.TLRPC.TL_updateShortSentMessage;
import org.telegram.tgnet.TLRPC.TL_user;
import org.telegram.tgnet.TLRPC.TL_userContact_old2;
import org.telegram.tgnet.TLRPC.TL_webPagePending;
import org.telegram.tgnet.TLRPC.Update;
import org.telegram.tgnet.TLRPC.Updates;
import org.telegram.tgnet.TLRPC.User;
import org.telegram.tgnet.TLRPC.WebPage;
import org.telegram.tgnet.TLRPC.messages_Messages;
import org.telegram.ui.ActionBar.AlertDialog.Builder;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.PaymentFormActivity;

public class SendMessagesHelper implements NotificationCenterDelegate {
    private static volatile SendMessagesHelper[] Instance = new SendMessagesHelper[3];
    private static DispatchQueue mediaSendQueue = new DispatchQueue("mediaSendQueue");
    private static ThreadPoolExecutor mediaSendThreadPool;
    private int currentAccount;
    private ChatFull currentChatInfo = null;
    private HashMap<String, ArrayList<DelayedMessage>> delayedMessages = new HashMap();
    private LocationProvider locationProvider = new LocationProvider(new LocationProviderDelegate() {
        public void onLocationAcquired(Location location) {
            SendMessagesHelper.this.sendLocation(location);
            SendMessagesHelper.this.waitingForLocation.clear();
        }

        public void onUnableLocationAcquire() {
            HashMap<String, MessageObject> waitingForLocationCopy = new HashMap(SendMessagesHelper.this.waitingForLocation);
            NotificationCenter.getInstance(SendMessagesHelper.this.currentAccount).postNotificationName(NotificationCenter.wasUnableToFindCurrentLocation, waitingForLocationCopy);
            SendMessagesHelper.this.waitingForLocation.clear();
        }
    });
    private SparseArray<Message> sendingMessages = new SparseArray();
    private SparseArray<MessageObject> unsentMessages = new SparseArray();
    private HashMap<String, Boolean> waitingForCallback = new HashMap();
    private HashMap<String, MessageObject> waitingForLocation = new HashMap();

    static class AnonymousClass16 implements Runnable {
        final /* synthetic */ String val$captionFinal;
        final /* synthetic */ int val$currentAccount;
        final /* synthetic */ long val$dialog_id;
        final /* synthetic */ TL_document val$documentFinal;
        final /* synthetic */ ArrayList val$entities;
        final /* synthetic */ HashMap val$params;
        final /* synthetic */ String val$pathFinal;
        final /* synthetic */ MessageObject val$reply_to_msg;

        AnonymousClass16(int i, TL_document tL_document, String str, long j, MessageObject messageObject, String str2, ArrayList arrayList, HashMap hashMap) {
            this.val$currentAccount = i;
            this.val$documentFinal = tL_document;
            this.val$pathFinal = str;
            this.val$dialog_id = j;
            this.val$reply_to_msg = messageObject;
            this.val$captionFinal = str2;
            this.val$entities = arrayList;
            this.val$params = hashMap;
        }

        public void run() {
            SendMessagesHelper.getInstance(this.val$currentAccount).sendMessage(this.val$documentFinal, null, this.val$pathFinal, this.val$dialog_id, this.val$reply_to_msg, this.val$captionFinal, this.val$entities, null, this.val$params, 0);
        }
    }

    protected class DelayedMessage {
        public EncryptedChat encryptedChat;
        public HashMap<Object, Object> extraHashMap;
        public int finalGroupMessage;
        public long groupId;
        public String httpLocation;
        public FileLocation location;
        public ArrayList<MessageObject> messageObjects;
        public ArrayList<Message> messages;
        public MessageObject obj;
        public String originalPath;
        public ArrayList<String> originalPaths;
        public long peer;
        ArrayList<DelayedMessageSendAfterRequest> requests;
        public TLObject sendEncryptedRequest;
        public TLObject sendRequest;
        public int type;
        public boolean upload;
        public VideoEditedInfo videoEditedInfo;

        public DelayedMessage(long peer) {
            this.peer = peer;
        }

        public void addDelayedRequest(TLObject req, MessageObject msgObj, String originalPath) {
            DelayedMessageSendAfterRequest request = new DelayedMessageSendAfterRequest();
            request.request = req;
            request.msgObj = msgObj;
            request.originalPath = originalPath;
            if (this.requests == null) {
                this.requests = new ArrayList();
            }
            this.requests.add(request);
        }

        public void addDelayedRequest(TLObject req, ArrayList<MessageObject> msgObjs, ArrayList<String> originalPaths) {
            DelayedMessageSendAfterRequest request = new DelayedMessageSendAfterRequest();
            request.request = req;
            request.msgObjs = msgObjs;
            request.originalPaths = originalPaths;
            if (this.requests == null) {
                this.requests = new ArrayList();
            }
            this.requests.add(request);
        }

        public void sendDelayedRequests() {
            if (this.requests != null) {
                if (this.type == 4 || this.type == 0) {
                    int size = this.requests.size();
                    for (int a = 0; a < size; a++) {
                        DelayedMessageSendAfterRequest request = (DelayedMessageSendAfterRequest) this.requests.get(a);
                        if (request.request instanceof TL_messages_sendEncryptedMultiMedia) {
                            SecretChatHelper.getInstance(SendMessagesHelper.this.currentAccount).performSendEncryptedRequest((TL_messages_sendEncryptedMultiMedia) request.request, this);
                        } else if (request.request instanceof TL_messages_sendMultiMedia) {
                            SendMessagesHelper.this.performSendMessageRequestMulti((TL_messages_sendMultiMedia) request.request, request.msgObjs, request.originalPaths);
                        } else {
                            SendMessagesHelper.this.performSendMessageRequest(request.request, request.msgObj, request.originalPath);
                        }
                    }
                    this.requests = null;
                }
            }
        }

        public void markAsError() {
            if (this.type == 4) {
                for (int a = 0; a < this.messageObjects.size(); a++) {
                    MessageObject obj = (MessageObject) this.messageObjects.get(a);
                    MessagesStorage.getInstance(SendMessagesHelper.this.currentAccount).markMessageAsSendError(obj.messageOwner);
                    obj.messageOwner.send_state = 2;
                    NotificationCenter.getInstance(SendMessagesHelper.this.currentAccount).postNotificationName(NotificationCenter.messageSendError, Integer.valueOf(obj.getId()));
                    SendMessagesHelper.this.processSentMessage(obj.getId());
                }
                HashMap access$1000 = SendMessagesHelper.this.delayedMessages;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("group_");
                stringBuilder.append(this.groupId);
                access$1000.remove(stringBuilder.toString());
            } else {
                MessagesStorage.getInstance(SendMessagesHelper.this.currentAccount).markMessageAsSendError(this.obj.messageOwner);
                this.obj.messageOwner.send_state = 2;
                NotificationCenter.getInstance(SendMessagesHelper.this.currentAccount).postNotificationName(NotificationCenter.messageSendError, Integer.valueOf(this.obj.getId()));
                SendMessagesHelper.this.processSentMessage(this.obj.getId());
            }
            sendDelayedRequests();
        }
    }

    protected class DelayedMessageSendAfterRequest {
        public MessageObject msgObj;
        public ArrayList<MessageObject> msgObjs;
        public String originalPath;
        public ArrayList<String> originalPaths;
        public TLObject request;

        protected DelayedMessageSendAfterRequest() {
        }
    }

    public static class LocationProvider {
        private LocationProviderDelegate delegate;
        private GpsLocationListener gpsLocationListener = new GpsLocationListener();
        private Location lastKnownLocation;
        private LocationManager locationManager;
        private Runnable locationQueryCancelRunnable;
        private GpsLocationListener networkLocationListener = new GpsLocationListener();

        private class GpsLocationListener implements LocationListener {
            private GpsLocationListener() {
            }

            public void onLocationChanged(Location location) {
                if (location != null) {
                    if (LocationProvider.this.locationQueryCancelRunnable != null) {
                        if (BuildVars.LOGS_ENABLED) {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("found location ");
                            stringBuilder.append(location);
                            FileLog.d(stringBuilder.toString());
                        }
                        LocationProvider.this.lastKnownLocation = location;
                        if (location.getAccuracy() < 100.0f) {
                            if (LocationProvider.this.delegate != null) {
                                LocationProvider.this.delegate.onLocationAcquired(location);
                            }
                            if (LocationProvider.this.locationQueryCancelRunnable != null) {
                                AndroidUtilities.cancelRunOnUIThread(LocationProvider.this.locationQueryCancelRunnable);
                            }
                            LocationProvider.this.cleanup();
                        }
                    }
                }
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        }

        public interface LocationProviderDelegate {
            void onLocationAcquired(Location location);

            void onUnableLocationAcquire();
        }

        public LocationProvider(LocationProviderDelegate locationProviderDelegate) {
            this.delegate = locationProviderDelegate;
        }

        public void setDelegate(LocationProviderDelegate locationProviderDelegate) {
            this.delegate = locationProviderDelegate;
        }

        private void cleanup() {
            this.locationManager.removeUpdates(this.gpsLocationListener);
            this.locationManager.removeUpdates(this.networkLocationListener);
            this.lastKnownLocation = null;
            this.locationQueryCancelRunnable = null;
        }

        public void start() {
            if (this.locationManager == null) {
                this.locationManager = (LocationManager) ApplicationLoader.applicationContext.getSystemService("location");
            }
            try {
                this.locationManager.requestLocationUpdates("gps", 1, 0.0f, this.gpsLocationListener);
            } catch (Throwable e) {
                FileLog.e(e);
            }
            try {
                this.locationManager.requestLocationUpdates("network", 1, 0.0f, this.networkLocationListener);
            } catch (Throwable e2) {
                FileLog.e(e2);
            }
            try {
                this.lastKnownLocation = this.locationManager.getLastKnownLocation("gps");
                if (this.lastKnownLocation == null) {
                    this.lastKnownLocation = this.locationManager.getLastKnownLocation("network");
                }
            } catch (Throwable e22) {
                FileLog.e(e22);
            }
            if (this.locationQueryCancelRunnable != null) {
                AndroidUtilities.cancelRunOnUIThread(this.locationQueryCancelRunnable);
            }
            this.locationQueryCancelRunnable = new Runnable() {
                public void run() {
                    if (LocationProvider.this.locationQueryCancelRunnable == this) {
                        if (LocationProvider.this.delegate != null) {
                            if (LocationProvider.this.lastKnownLocation != null) {
                                LocationProvider.this.delegate.onLocationAcquired(LocationProvider.this.lastKnownLocation);
                            } else {
                                LocationProvider.this.delegate.onUnableLocationAcquire();
                            }
                        }
                        LocationProvider.this.cleanup();
                    }
                }
            };
            AndroidUtilities.runOnUIThread(this.locationQueryCancelRunnable, DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS);
        }

        public void stop() {
            if (this.locationManager != null) {
                if (this.locationQueryCancelRunnable != null) {
                    AndroidUtilities.cancelRunOnUIThread(this.locationQueryCancelRunnable);
                }
                cleanup();
            }
        }
    }

    private static class MediaSendPrepareWorker {
        public volatile TL_photo photo;
        public CountDownLatch sync;

        private MediaSendPrepareWorker() {
        }
    }

    public static class SendingMediaInfo {
        public String caption;
        public ArrayList<MessageEntity> entities;
        public boolean isVideo;
        public ArrayList<InputDocument> masks;
        public String path;
        public SearchImage searchImage;
        public int ttl;
        public Uri uri;
        public VideoEditedInfo videoEditedInfo;
    }

    private static org.telegram.messenger.VideoEditedInfo createCompressionSettings(java.lang.String r1) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.SendMessagesHelper.createCompressionSettings(java.lang.String):org.telegram.messenger.VideoEditedInfo
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
        r1 = r40;
        r2 = 0;
        r3 = 0;
        r4 = 0;
        r5 = 0;
        r6 = 0;
        r8 = 0;
        r10 = r8;
        r12 = 0;
        r13 = new com.coremedia.iso.IsoFile;	 Catch:{ Exception -> 0x0333 }
        r13.<init>(r1);	 Catch:{ Exception -> 0x0333 }
        r14 = "/moov/trak/";	 Catch:{ Exception -> 0x0333 }
        r14 = com.googlecode.mp4parser.util.Path.getPaths(r13, r14);	 Catch:{ Exception -> 0x0333 }
        r15 = "/moov/trak/mdia/minf/stbl/stsd/mp4a/";	 Catch:{ Exception -> 0x0333 }
        r15 = com.googlecode.mp4parser.util.Path.getPath(r13, r15);	 Catch:{ Exception -> 0x0333 }
        if (r15 != 0) goto L_0x0028;	 Catch:{ Exception -> 0x0333 }
    L_0x001f:
        r16 = org.telegram.messenger.BuildVars.LOGS_ENABLED;	 Catch:{ Exception -> 0x0333 }
        if (r16 == 0) goto L_0x0028;	 Catch:{ Exception -> 0x0333 }
    L_0x0023:
        r8 = "video hasn't mp4a atom";	 Catch:{ Exception -> 0x0333 }
        org.telegram.messenger.FileLog.d(r8);	 Catch:{ Exception -> 0x0333 }
    L_0x0028:
        r8 = "/moov/trak/mdia/minf/stbl/stsd/avc1/";	 Catch:{ Exception -> 0x0333 }
        r8 = com.googlecode.mp4parser.util.Path.getPath(r13, r8);	 Catch:{ Exception -> 0x0333 }
        if (r8 != 0) goto L_0x003a;	 Catch:{ Exception -> 0x0333 }
    L_0x0030:
        r9 = org.telegram.messenger.BuildVars.LOGS_ENABLED;	 Catch:{ Exception -> 0x0333 }
        if (r9 == 0) goto L_0x0039;	 Catch:{ Exception -> 0x0333 }
    L_0x0034:
        r9 = "video hasn't avc1 atom";	 Catch:{ Exception -> 0x0333 }
        org.telegram.messenger.FileLog.d(r9);	 Catch:{ Exception -> 0x0333 }
    L_0x0039:
        return r12;
    L_0x003a:
        r15 = r10;
        r10 = r6;
        r6 = r5;
        r5 = r4;
        r4 = r3;
        r3 = r2;
        r2 = 0;
        r7 = r14.size();	 Catch:{ Exception -> 0x0329 }
        if (r2 >= r7) goto L_0x0148;
    L_0x0047:
        r7 = r14.get(r2);	 Catch:{ Exception -> 0x0141 }
        r7 = (com.coremedia.iso.boxes.Box) r7;	 Catch:{ Exception -> 0x0141 }
        r19 = r7;	 Catch:{ Exception -> 0x0141 }
        r19 = (com.coremedia.iso.boxes.TrackBox) r19;	 Catch:{ Exception -> 0x0141 }
        r20 = r19;
        r21 = 0;
        r17 = 0;
        r23 = r17;
        r9 = r20;
        r19 = r9.getMediaBox();	 Catch:{ Exception -> 0x00d8 }
        r25 = r19;	 Catch:{ Exception -> 0x00d8 }
        r12 = r25;	 Catch:{ Exception -> 0x00d8 }
        r19 = r12.getMediaHeaderBox();	 Catch:{ Exception -> 0x00d8 }
        r26 = r19;
        r27 = r7;
        r7 = r12.getMediaInformationBox();	 Catch:{ Exception -> 0x00cf }
        r7 = r7.getSampleTableBox();	 Catch:{ Exception -> 0x00cf }
        r7 = r7.getSampleSizeBox();	 Catch:{ Exception -> 0x00cf }
        r19 = r7.getSampleSizes();	 Catch:{ Exception -> 0x00cf }
        r28 = r19;
        r19 = 0;
        r29 = r19;
        r30 = r7;
        r31 = r8;
        r7 = r28;
        r8 = r7.length;	 Catch:{ Exception -> 0x00c8 }
        r32 = r12;
        r12 = r29;
        if (r12 >= r8) goto L_0x00a6;
        r19 = r7[r12];	 Catch:{ Exception -> 0x009f }
        r28 = r21 + r19;
        r19 = r12 + 1;
        r21 = r28;
        r8 = r31;
        r12 = r32;
        r28 = r7;
        r7 = r30;
        goto L_0x007f;
    L_0x009f:
        r0 = move-exception;
        r7 = r0;
        r33 = r13;
        r34 = r14;
        goto L_0x00e2;
        r33 = r13;
        r8 = r26;
        r12 = r8.getDuration();	 Catch:{ Exception -> 0x00c3 }
        r12 = (float) r12;
        r34 = r14;
        r13 = r8.getTimescale();	 Catch:{ Exception -> 0x00c0 }
        r13 = (float) r13;
        r12 = r12 / r13;
        r13 = 8;
        r13 = r13 * r21;
        r6 = (float) r13;
        r6 = r6 / r12;
        r6 = (int) r6;
        r6 = (long) r6;
        goto L_0x00e8;
    L_0x00c0:
        r0 = move-exception;
        r7 = r0;
        goto L_0x00e2;
    L_0x00c3:
        r0 = move-exception;
        r34 = r14;
        r7 = r0;
        goto L_0x00e2;
    L_0x00c8:
        r0 = move-exception;
        r33 = r13;
        r34 = r14;
        r7 = r0;
        goto L_0x00e2;
    L_0x00cf:
        r0 = move-exception;
        r31 = r8;
        r33 = r13;
        r34 = r14;
        r7 = r0;
        goto L_0x00e2;
    L_0x00d8:
        r0 = move-exception;
        r27 = r7;
        r31 = r8;
        r33 = r13;
        r34 = r14;
        r7 = r0;
        org.telegram.messenger.FileLog.e(r7);	 Catch:{ Exception -> 0x0141 }
        r12 = r6;
        r6 = r23;
        r8 = r9.getTrackHeaderBox();	 Catch:{ Exception -> 0x013e }
        r13 = r8.getWidth();	 Catch:{ Exception -> 0x013e }
        r19 = 0;	 Catch:{ Exception -> 0x013e }
        r23 = (r13 > r19 ? 1 : (r13 == r19 ? 0 : -1));	 Catch:{ Exception -> 0x013e }
        if (r23 == 0) goto L_0x012f;	 Catch:{ Exception -> 0x013e }
        r13 = r8.getHeight();	 Catch:{ Exception -> 0x013e }
        r23 = (r13 > r19 ? 1 : (r13 == r19 ? 0 : -1));	 Catch:{ Exception -> 0x013e }
        if (r23 == 0) goto L_0x012f;	 Catch:{ Exception -> 0x013e }
        if (r3 == 0) goto L_0x0118;	 Catch:{ Exception -> 0x013e }
        r13 = r3.getWidth();	 Catch:{ Exception -> 0x013e }
        r19 = r8.getWidth();	 Catch:{ Exception -> 0x013e }
        r23 = (r13 > r19 ? 1 : (r13 == r19 ? 0 : -1));	 Catch:{ Exception -> 0x013e }
        if (r23 < 0) goto L_0x0118;	 Catch:{ Exception -> 0x013e }
        r13 = r3.getHeight();	 Catch:{ Exception -> 0x013e }
        r19 = r8.getHeight();	 Catch:{ Exception -> 0x013e }
        r23 = (r13 > r19 ? 1 : (r13 == r19 ? 0 : -1));	 Catch:{ Exception -> 0x013e }
        if (r23 >= 0) goto L_0x0132;	 Catch:{ Exception -> 0x013e }
        r3 = r8;	 Catch:{ Exception -> 0x013e }
        r13 = 100000; // 0x186a0 float:1.4013E-40 double:4.94066E-319;	 Catch:{ Exception -> 0x013e }
        r19 = r6 / r13;	 Catch:{ Exception -> 0x013e }
        r13 = r13 * r19;
        r13 = (int) r13;
        r5 = r13;
        r4 = r13;
        r13 = 900000; // 0xdbba0 float:1.261169E-39 double:4.44659E-318;
        if (r5 <= r13) goto L_0x012b;
        r5 = 900000; // 0xdbba0 float:1.261169E-39 double:4.44659E-318;
        r13 = r10 + r21;
        r10 = r13;
        goto L_0x0132;
        r6 = r15 + r21;
        r15 = r6;
        r2 = r2 + 1;
        r6 = r12;
        r8 = r31;
        r13 = r33;
        r14 = r34;
        r12 = 0;
        goto L_0x0041;
    L_0x013e:
        r0 = move-exception;
        r2 = r0;
        goto L_0x0144;
    L_0x0141:
        r0 = move-exception;
        r2 = r0;
        r12 = r6;
        r6 = r10;
        r10 = r15;
        goto L_0x0339;
        if (r3 != 0) goto L_0x0156;
        r2 = org.telegram.messenger.BuildVars.LOGS_ENABLED;
        if (r2 == 0) goto L_0x0154;
        r2 = "video hasn't trackHeaderBox atom";
        org.telegram.messenger.FileLog.d(r2);
        r2 = 0;
        return r2;
        r2 = android.os.Build.VERSION.SDK_INT;
        r7 = 18;
        if (r2 >= r7) goto L_0x01de;
        r2 = "video/avc";	 Catch:{ Exception -> 0x01da }
        r2 = org.telegram.messenger.MediaController.selectCodec(r2);	 Catch:{ Exception -> 0x01da }
        if (r2 != 0) goto L_0x016f;	 Catch:{ Exception -> 0x01da }
        r7 = org.telegram.messenger.BuildVars.LOGS_ENABLED;	 Catch:{ Exception -> 0x01da }
        if (r7 == 0) goto L_0x016d;	 Catch:{ Exception -> 0x01da }
        r7 = "no codec info for video/avc";	 Catch:{ Exception -> 0x01da }
        org.telegram.messenger.FileLog.d(r7);	 Catch:{ Exception -> 0x01da }
        r7 = 0;	 Catch:{ Exception -> 0x01da }
        return r7;	 Catch:{ Exception -> 0x01da }
        r7 = r2.getName();	 Catch:{ Exception -> 0x01da }
        r8 = "OMX.google.h264.encoder";	 Catch:{ Exception -> 0x01da }
        r8 = r7.equals(r8);	 Catch:{ Exception -> 0x01da }
        if (r8 != 0) goto L_0x01c0;	 Catch:{ Exception -> 0x01da }
        r8 = "OMX.ST.VFM.H264Enc";	 Catch:{ Exception -> 0x01da }
        r8 = r7.equals(r8);	 Catch:{ Exception -> 0x01da }
        if (r8 != 0) goto L_0x01c0;	 Catch:{ Exception -> 0x01da }
        r8 = "OMX.Exynos.avc.enc";	 Catch:{ Exception -> 0x01da }
        r8 = r7.equals(r8);	 Catch:{ Exception -> 0x01da }
        if (r8 != 0) goto L_0x01c0;	 Catch:{ Exception -> 0x01da }
        r8 = "OMX.MARVELL.VIDEO.HW.CODA7542ENCODER";	 Catch:{ Exception -> 0x01da }
        r8 = r7.equals(r8);	 Catch:{ Exception -> 0x01da }
        if (r8 != 0) goto L_0x01c0;	 Catch:{ Exception -> 0x01da }
        r8 = "OMX.MARVELL.VIDEO.H264ENCODER";	 Catch:{ Exception -> 0x01da }
        r8 = r7.equals(r8);	 Catch:{ Exception -> 0x01da }
        if (r8 != 0) goto L_0x01c0;	 Catch:{ Exception -> 0x01da }
        r8 = "OMX.k3.video.encoder.avc";	 Catch:{ Exception -> 0x01da }
        r8 = r7.equals(r8);	 Catch:{ Exception -> 0x01da }
        if (r8 != 0) goto L_0x01c0;	 Catch:{ Exception -> 0x01da }
        r8 = "OMX.TI.DUCATI1.VIDEO.H264E";	 Catch:{ Exception -> 0x01da }
        r8 = r7.equals(r8);	 Catch:{ Exception -> 0x01da }
        if (r8 == 0) goto L_0x01ac;	 Catch:{ Exception -> 0x01da }
        goto L_0x01c0;	 Catch:{ Exception -> 0x01da }
        r8 = "video/avc";	 Catch:{ Exception -> 0x01da }
        r8 = org.telegram.messenger.MediaController.selectColorFormat(r2, r8);	 Catch:{ Exception -> 0x01da }
        if (r8 != 0) goto L_0x01bf;	 Catch:{ Exception -> 0x01da }
        r8 = org.telegram.messenger.BuildVars.LOGS_ENABLED;	 Catch:{ Exception -> 0x01da }
        if (r8 == 0) goto L_0x01bd;	 Catch:{ Exception -> 0x01da }
        r8 = "no color format for video/avc";	 Catch:{ Exception -> 0x01da }
        org.telegram.messenger.FileLog.d(r8);	 Catch:{ Exception -> 0x01da }
        r8 = 0;	 Catch:{ Exception -> 0x01da }
        return r8;	 Catch:{ Exception -> 0x01da }
        goto L_0x01de;	 Catch:{ Exception -> 0x01da }
        r8 = org.telegram.messenger.BuildVars.LOGS_ENABLED;	 Catch:{ Exception -> 0x01da }
        if (r8 == 0) goto L_0x01d8;	 Catch:{ Exception -> 0x01da }
        r8 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01da }
        r8.<init>();	 Catch:{ Exception -> 0x01da }
        r9 = "unsupported encoder = ";	 Catch:{ Exception -> 0x01da }
        r8.append(r9);	 Catch:{ Exception -> 0x01da }
        r8.append(r7);	 Catch:{ Exception -> 0x01da }
        r8 = r8.toString();	 Catch:{ Exception -> 0x01da }
        org.telegram.messenger.FileLog.d(r8);	 Catch:{ Exception -> 0x01da }
        r8 = 0;
        return r8;
    L_0x01da:
        r0 = move-exception;
        r2 = r0;
        r7 = 0;
        return r7;
        r2 = 1148846080; // 0x447a0000 float:1000.0 double:5.676053805E-315;
        r6 = r6 * r2;
        r7 = new org.telegram.messenger.VideoEditedInfo;
        r7.<init>();
        r8 = -1;
        r7.startTime = r8;
        r7.endTime = r8;
        r7.bitrate = r5;
        r7.originalPath = r1;
        r8 = (double) r6;
        r8 = java.lang.Math.ceil(r8);
        r8 = (long) r8;
        r7.estimatedDuration = r8;
        r8 = r3.getWidth();
        r8 = (int) r8;
        r7.originalWidth = r8;
        r7.resultWidth = r8;
        r8 = r3.getHeight();
        r8 = (int) r8;
        r7.originalHeight = r8;
        r7.resultHeight = r8;
        r8 = r3.getMatrix();
        r9 = com.googlecode.mp4parser.util.Matrix.ROTATE_90;
        r9 = r8.equals(r9);
        if (r9 == 0) goto L_0x021b;
        r9 = 90;
        r7.rotationValue = r9;
        goto L_0x0238;
        r9 = com.googlecode.mp4parser.util.Matrix.ROTATE_180;
        r9 = r8.equals(r9);
        if (r9 == 0) goto L_0x0228;
        r9 = 180; // 0xb4 float:2.52E-43 double:8.9E-322;
        r7.rotationValue = r9;
        goto L_0x0238;
        r9 = com.googlecode.mp4parser.util.Matrix.ROTATE_270;
        r9 = r8.equals(r9);
        if (r9 == 0) goto L_0x0235;
        r9 = 270; // 0x10e float:3.78E-43 double:1.334E-321;
        r7.rotationValue = r9;
        goto L_0x0238;
        r9 = 0;
        r7.rotationValue = r9;
        r9 = org.telegram.messenger.MessagesController.getGlobalMainSettings();
        r12 = "compress_video2";
        r13 = 1;
        r12 = r9.getInt(r12, r13);
        r14 = r7.originalWidth;
        r13 = 1280; // 0x500 float:1.794E-42 double:6.324E-321;
        if (r14 > r13) goto L_0x0277;
        r14 = r7.originalHeight;
        if (r14 <= r13) goto L_0x024e;
        goto L_0x0277;
        r13 = r7.originalWidth;
        r14 = 848; // 0x350 float:1.188E-42 double:4.19E-321;
        if (r13 > r14) goto L_0x0275;
        r13 = r7.originalHeight;
        if (r13 <= r14) goto L_0x0259;
        goto L_0x0275;
        r13 = r7.originalWidth;
        r14 = 640; // 0x280 float:8.97E-43 double:3.16E-321;
        if (r13 > r14) goto L_0x0273;
        r13 = r7.originalHeight;
        if (r13 <= r14) goto L_0x0264;
        goto L_0x0273;
        r13 = r7.originalWidth;
        r14 = 480; // 0x1e0 float:6.73E-43 double:2.37E-321;
        if (r13 > r14) goto L_0x0271;
        r13 = r7.originalHeight;
        if (r13 <= r14) goto L_0x026f;
        goto L_0x0271;
        r13 = 1;
        goto L_0x0278;
        r13 = 2;
        goto L_0x0278;
        r13 = 3;
        goto L_0x0278;
        r13 = 4;
        goto L_0x0278;
        r13 = 5;
        if (r12 < r13) goto L_0x027d;
        r12 = r13 + -1;
        r14 = r13 + -1;
        if (r12 == r14) goto L_0x02e3;
        switch(r12) {
            case 0: goto L_0x0298;
            case 1: goto L_0x0292;
            case 2: goto L_0x028c;
            default: goto L_0x0284;
        };
        r17 = 2500000; // 0x2625a0 float:3.503246E-39 double:1.235164E-317;
        r14 = 1151336448; // 0x44a00000 float:1280.0 double:5.68835786E-315;
        r2 = r17;
        goto L_0x029e;
        r14 = 1146355712; // 0x44540000 float:848.0 double:5.66374975E-315;
        r17 = 1100000; // 0x10c8e0 float:1.541428E-39 double:5.43472E-318;
        goto L_0x0289;
        r14 = 1142947840; // 0x44200000 float:640.0 double:5.646912627E-315;
        r17 = 900000; // 0xdbba0 float:1.261169E-39 double:4.44659E-318;
        goto L_0x0289;
        r14 = 1138229248; // 0x43d80000 float:432.0 double:5.623599685E-315;
        r17 = 400000; // 0x61a80 float:5.6052E-40 double:1.976263E-318;
        goto L_0x0289;
        r35 = r3;
        r3 = r7.originalWidth;
        r36 = r8;
        r8 = r7.originalHeight;
        if (r3 <= r8) goto L_0x02af;
        r3 = r7.originalWidth;
        r3 = (float) r3;
        r3 = r14 / r3;
        goto L_0x02b2;
        r3 = r7.originalHeight;
        goto L_0x02ab;
        r8 = r7.originalWidth;
        r8 = (float) r8;
        r8 = r8 * r3;
        r17 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r8 = r8 / r17;
        r8 = java.lang.Math.round(r8);
        r8 = r8 * 2;
        r7.resultWidth = r8;
        r8 = r7.originalHeight;
        r8 = (float) r8;
        r8 = r8 * r3;
        r8 = r8 / r17;
        r8 = java.lang.Math.round(r8);
        r8 = r8 * 2;
        r7.resultHeight = r8;
        if (r5 == 0) goto L_0x02e7;
        r8 = (float) r4;
        r8 = r8 / r3;
        r8 = (int) r8;
        r5 = java.lang.Math.min(r2, r8);
        r8 = r5 / 8;
        r8 = (float) r8;
        r8 = r8 * r6;
        r17 = 1148846080; // 0x447a0000 float:1000.0 double:5.676053805E-315;
        r8 = r8 / r17;
        r10 = (long) r8;
        goto L_0x02e7;
        r35 = r3;
        r36 = r8;
        r2 = r13 + -1;
        if (r12 != r2) goto L_0x0309;
        r2 = r7.originalWidth;
        r7.resultWidth = r2;
        r2 = r7.originalHeight;
        r7.resultHeight = r2;
        r7.bitrate = r4;
        r2 = new java.io.File;
        r2.<init>(r1);
        r2 = r2.length();
        r2 = (int) r2;
        r2 = (long) r2;
        r7.estimatedSize = r2;
        r37 = r4;
        r38 = r5;
        r39 = r9;
        goto L_0x0328;
        r7.bitrate = r5;
        r2 = r15 + r10;
        r2 = (int) r2;
        r2 = (long) r2;
        r7.estimatedSize = r2;
        r2 = r7.estimatedSize;
        r37 = r4;
        r38 = r5;
        r4 = r7.estimatedSize;
        r17 = 32768; // 0x8000 float:4.5918E-41 double:1.61895E-319;
        r4 = r4 / r17;
        r17 = 16;
        r4 = r4 * r17;
        r39 = r9;
        r8 = r2 + r4;
        r7.estimatedSize = r8;
        return r7;
    L_0x0329:
        r0 = move-exception;
        r35 = r3;
        r37 = r4;
        r2 = r0;
        r12 = r6;
        r6 = r10;
        r10 = r15;
        goto L_0x0339;
    L_0x0333:
        r0 = move-exception;
        r12 = r5;
        r5 = r4;
        r4 = r3;
        r3 = r2;
        r2 = r0;
        org.telegram.messenger.FileLog.e(r2);
        r8 = 0;
        return r8;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.SendMessagesHelper.createCompressionSettings(java.lang.String):org.telegram.messenger.VideoEditedInfo");
    }

    private static void fillVideoAttribute(java.lang.String r1, org.telegram.tgnet.TLRPC.TL_documentAttributeVideo r2, org.telegram.messenger.VideoEditedInfo r3) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.SendMessagesHelper.fillVideoAttribute(java.lang.String, org.telegram.tgnet.TLRPC$TL_documentAttributeVideo, org.telegram.messenger.VideoEditedInfo):void
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
        r0 = 0;
        r1 = 0;
        r2 = 1148846080; // 0x447a0000 float:1000.0 double:5.676053805E-315;
        r3 = new android.media.MediaMetadataRetriever;	 Catch:{ Exception -> 0x0079 }
        r3.<init>();	 Catch:{ Exception -> 0x0079 }
        r1 = r3;	 Catch:{ Exception -> 0x0079 }
        r1.setDataSource(r10);	 Catch:{ Exception -> 0x0079 }
        r3 = 18;	 Catch:{ Exception -> 0x0079 }
        r3 = r1.extractMetadata(r3);	 Catch:{ Exception -> 0x0079 }
        if (r3 == 0) goto L_0x001b;	 Catch:{ Exception -> 0x0079 }
    L_0x0015:
        r4 = java.lang.Integer.parseInt(r3);	 Catch:{ Exception -> 0x0079 }
        r11.w = r4;	 Catch:{ Exception -> 0x0079 }
    L_0x001b:
        r4 = 19;	 Catch:{ Exception -> 0x0079 }
        r4 = r1.extractMetadata(r4);	 Catch:{ Exception -> 0x0079 }
        if (r4 == 0) goto L_0x0029;	 Catch:{ Exception -> 0x0079 }
    L_0x0023:
        r5 = java.lang.Integer.parseInt(r4);	 Catch:{ Exception -> 0x0079 }
        r11.h = r5;	 Catch:{ Exception -> 0x0079 }
    L_0x0029:
        r5 = 9;	 Catch:{ Exception -> 0x0079 }
        r5 = r1.extractMetadata(r5);	 Catch:{ Exception -> 0x0079 }
        if (r5 == 0) goto L_0x003f;	 Catch:{ Exception -> 0x0079 }
    L_0x0031:
        r6 = java.lang.Long.parseLong(r5);	 Catch:{ Exception -> 0x0079 }
        r6 = (float) r6;	 Catch:{ Exception -> 0x0079 }
        r6 = r6 / r2;	 Catch:{ Exception -> 0x0079 }
        r6 = (double) r6;	 Catch:{ Exception -> 0x0079 }
        r6 = java.lang.Math.ceil(r6);	 Catch:{ Exception -> 0x0079 }
        r6 = (int) r6;	 Catch:{ Exception -> 0x0079 }
        r11.duration = r6;	 Catch:{ Exception -> 0x0079 }
    L_0x003f:
        r6 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x0079 }
        r7 = 17;	 Catch:{ Exception -> 0x0079 }
        if (r6 < r7) goto L_0x006a;	 Catch:{ Exception -> 0x0079 }
    L_0x0045:
        r6 = 24;	 Catch:{ Exception -> 0x0079 }
        r6 = r1.extractMetadata(r6);	 Catch:{ Exception -> 0x0079 }
        if (r6 == 0) goto L_0x006a;	 Catch:{ Exception -> 0x0079 }
    L_0x004d:
        r7 = org.telegram.messenger.Utilities.parseInt(r6);	 Catch:{ Exception -> 0x0079 }
        r7 = r7.intValue();	 Catch:{ Exception -> 0x0079 }
        if (r12 == 0) goto L_0x005a;	 Catch:{ Exception -> 0x0079 }
    L_0x0057:
        r12.rotationValue = r7;	 Catch:{ Exception -> 0x0079 }
        goto L_0x006a;	 Catch:{ Exception -> 0x0079 }
    L_0x005a:
        r8 = 90;	 Catch:{ Exception -> 0x0079 }
        if (r7 == r8) goto L_0x0062;	 Catch:{ Exception -> 0x0079 }
    L_0x005e:
        r8 = 270; // 0x10e float:3.78E-43 double:1.334E-321;	 Catch:{ Exception -> 0x0079 }
        if (r7 != r8) goto L_0x006a;	 Catch:{ Exception -> 0x0079 }
    L_0x0062:
        r8 = r11.w;	 Catch:{ Exception -> 0x0079 }
        r9 = r11.h;	 Catch:{ Exception -> 0x0079 }
        r11.w = r9;	 Catch:{ Exception -> 0x0079 }
        r11.h = r8;	 Catch:{ Exception -> 0x0079 }
    L_0x006a:
        r0 = 1;
        if (r1 == 0) goto L_0x0076;
    L_0x006d:
        r1.release();	 Catch:{ Exception -> 0x0071 }
        goto L_0x0076;
    L_0x0071:
        r3 = move-exception;
        org.telegram.messenger.FileLog.e(r3);
        goto L_0x0083;
    L_0x0076:
        goto L_0x0083;
    L_0x0077:
        r2 = move-exception;
        goto L_0x00b9;
    L_0x0079:
        r3 = move-exception;
        org.telegram.messenger.FileLog.e(r3);	 Catch:{ all -> 0x0077 }
        if (r1 == 0) goto L_0x0076;
        r1.release();	 Catch:{ Exception -> 0x0071 }
        goto L_0x0076;
    L_0x0083:
        if (r0 != 0) goto L_0x00b8;
        r3 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Exception -> 0x00b4 }
        r4 = new java.io.File;	 Catch:{ Exception -> 0x00b4 }
        r4.<init>(r10);	 Catch:{ Exception -> 0x00b4 }
        r4 = android.net.Uri.fromFile(r4);	 Catch:{ Exception -> 0x00b4 }
        r3 = android.media.MediaPlayer.create(r3, r4);	 Catch:{ Exception -> 0x00b4 }
        if (r3 == 0) goto L_0x00b3;	 Catch:{ Exception -> 0x00b4 }
        r4 = r3.getDuration();	 Catch:{ Exception -> 0x00b4 }
        r4 = (float) r4;	 Catch:{ Exception -> 0x00b4 }
        r4 = r4 / r2;	 Catch:{ Exception -> 0x00b4 }
        r4 = (double) r4;	 Catch:{ Exception -> 0x00b4 }
        r4 = java.lang.Math.ceil(r4);	 Catch:{ Exception -> 0x00b4 }
        r2 = (int) r4;	 Catch:{ Exception -> 0x00b4 }
        r11.duration = r2;	 Catch:{ Exception -> 0x00b4 }
        r2 = r3.getVideoWidth();	 Catch:{ Exception -> 0x00b4 }
        r11.w = r2;	 Catch:{ Exception -> 0x00b4 }
        r2 = r3.getVideoHeight();	 Catch:{ Exception -> 0x00b4 }
        r11.h = r2;	 Catch:{ Exception -> 0x00b4 }
        r3.release();	 Catch:{ Exception -> 0x00b4 }
        goto L_0x00b8;
    L_0x00b4:
        r2 = move-exception;
        org.telegram.messenger.FileLog.e(r2);
        return;
        if (r1 == 0) goto L_0x00c5;
        r1.release();	 Catch:{ Exception -> 0x00c0 }
        goto L_0x00c5;
    L_0x00c0:
        r3 = move-exception;
        org.telegram.messenger.FileLog.e(r3);
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.SendMessagesHelper.fillVideoAttribute(java.lang.String, org.telegram.tgnet.TLRPC$TL_documentAttributeVideo, org.telegram.messenger.VideoEditedInfo):void");
    }

    private static boolean prepareSendingDocumentInternal(int r1, java.lang.String r2, java.lang.String r3, android.net.Uri r4, java.lang.String r5, long r6, org.telegram.messenger.MessageObject r8, java.lang.CharSequence r9, java.util.ArrayList<org.telegram.tgnet.TLRPC.MessageEntity> r10) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.SendMessagesHelper.prepareSendingDocumentInternal(int, java.lang.String, java.lang.String, android.net.Uri, java.lang.String, long, org.telegram.messenger.MessageObject, java.lang.CharSequence, java.util.ArrayList):boolean
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
        r1 = r40;
        r2 = r41;
        r3 = r42;
        r4 = r43;
        r5 = 0;
        if (r1 == 0) goto L_0x0011;
    L_0x000b:
        r6 = r40.length();
        if (r6 != 0) goto L_0x0014;
    L_0x0011:
        if (r3 != 0) goto L_0x0014;
    L_0x0013:
        return r5;
    L_0x0014:
        if (r3 == 0) goto L_0x001d;
    L_0x0016:
        r6 = org.telegram.messenger.AndroidUtilities.isInternalUri(r42);
        if (r6 == 0) goto L_0x001d;
    L_0x001c:
        return r5;
    L_0x001d:
        if (r1 == 0) goto L_0x002f;
    L_0x001f:
        r6 = new java.io.File;
        r6.<init>(r1);
        r6 = android.net.Uri.fromFile(r6);
        r6 = org.telegram.messenger.AndroidUtilities.isInternalUri(r6);
        if (r6 == 0) goto L_0x002f;
    L_0x002e:
        return r5;
    L_0x002f:
        r6 = android.webkit.MimeTypeMap.getSingleton();
        r7 = 0;
        r8 = 0;
        if (r3 == 0) goto L_0x004e;
    L_0x0037:
        r9 = 0;
        if (r4 == 0) goto L_0x003e;
    L_0x003a:
        r8 = r6.getExtensionFromMimeType(r4);
    L_0x003e:
        if (r8 != 0) goto L_0x0043;
    L_0x0040:
        r8 = "txt";
        goto L_0x0044;
    L_0x0043:
        r9 = 1;
    L_0x0044:
        r1 = org.telegram.messenger.MediaController.copyFileToCache(r3, r8);
        if (r1 != 0) goto L_0x004b;
    L_0x004a:
        return r5;
    L_0x004b:
        if (r9 != 0) goto L_0x004e;
    L_0x004d:
        r8 = 0;
    L_0x004e:
        r9 = new java.io.File;
        r9.<init>(r1);
        r15 = r9;
        r9 = r15.exists();
        if (r9 == 0) goto L_0x042f;
    L_0x005a:
        r9 = r15.length();
        r11 = 0;
        r13 = (r9 > r11 ? 1 : (r9 == r11 ? 0 : -1));
        if (r13 != 0) goto L_0x006c;
    L_0x0064:
        r24 = r7;
        r25 = r8;
        r22 = r15;
        goto L_0x0435;
    L_0x006c:
        r13 = r44;
        r9 = (int) r13;
        if (r9 != 0) goto L_0x0073;
    L_0x0071:
        r9 = 1;
        goto L_0x0074;
    L_0x0073:
        r9 = r5;
    L_0x0074:
        if (r9 != 0) goto L_0x0079;
    L_0x0076:
        r16 = 1;
        goto L_0x007b;
    L_0x0079:
        r16 = r5;
    L_0x007b:
        r19 = r16;
        r5 = r15.getName();
        r16 = "";
        r11 = -1;
        if (r8 == 0) goto L_0x008b;
    L_0x0086:
        r16 = r8;
    L_0x0088:
        r12 = r16;
        goto L_0x009a;
    L_0x008b:
        r12 = 46;
        r12 = r1.lastIndexOf(r12);
        if (r12 == r11) goto L_0x0088;
    L_0x0093:
        r11 = r12 + 1;
        r16 = r1.substring(r11);
        goto L_0x0088;
    L_0x009a:
        r11 = r12.toLowerCase();
        r16 = 0;
        r17 = 0;
        r18 = 0;
        r23 = 0;
        r10 = "mp3";
        r10 = r11.equals(r10);
        r3 = 0;
        if (r10 != 0) goto L_0x0172;
    L_0x00af:
        r10 = "m4a";
        r10 = r11.equals(r10);
        if (r10 == 0) goto L_0x00bd;
    L_0x00b7:
        r24 = r7;
        r25 = r8;
        goto L_0x0176;
    L_0x00bd:
        r10 = "opus";
        r10 = r11.equals(r10);
        if (r10 != 0) goto L_0x00dc;
    L_0x00c5:
        r10 = "ogg";
        r10 = r11.equals(r10);
        if (r10 != 0) goto L_0x00dc;
    L_0x00cd:
        r10 = "flac";
        r10 = r11.equals(r10);
        if (r10 == 0) goto L_0x00d6;
    L_0x00d5:
        goto L_0x00dc;
    L_0x00d6:
        r24 = r7;
        r25 = r8;
        goto L_0x018f;
    L_0x00dc:
        r10 = r3;
        r3 = new android.media.MediaMetadataRetriever;	 Catch:{ Exception -> 0x0152, all -> 0x014b }
        r3.<init>();	 Catch:{ Exception -> 0x0152, all -> 0x014b }
        r10 = r3;	 Catch:{ Exception -> 0x0152, all -> 0x014b }
        r3 = r15.getAbsolutePath();	 Catch:{ Exception -> 0x0152, all -> 0x014b }
        r10.setDataSource(r3);	 Catch:{ Exception -> 0x0152, all -> 0x014b }
        r3 = 9;	 Catch:{ Exception -> 0x0152, all -> 0x014b }
        r3 = r10.extractMetadata(r3);	 Catch:{ Exception -> 0x0152, all -> 0x014b }
        if (r3 == 0) goto L_0x0124;
    L_0x00f2:
        r24 = r7;
        r25 = r8;
        r7 = java.lang.Long.parseLong(r3);	 Catch:{ Exception -> 0x0121 }
        r7 = (float) r7;	 Catch:{ Exception -> 0x0121 }
        r8 = 1148846080; // 0x447a0000 float:1000.0 double:5.676053805E-315;	 Catch:{ Exception -> 0x0121 }
        r7 = r7 / r8;	 Catch:{ Exception -> 0x0121 }
        r7 = (double) r7;	 Catch:{ Exception -> 0x0121 }
        r7 = java.lang.Math.ceil(r7);	 Catch:{ Exception -> 0x0121 }
        r7 = (int) r7;
        r8 = 7;
        r8 = r10.extractMetadata(r8);	 Catch:{ Exception -> 0x011c, all -> 0x0117 }
        r17 = r8;	 Catch:{ Exception -> 0x011c, all -> 0x0117 }
        r8 = 2;	 Catch:{ Exception -> 0x011c, all -> 0x0117 }
        r23 = r10.extractMetadata(r8);	 Catch:{ Exception -> 0x011c, all -> 0x0117 }
        r8 = r23;
        r23 = r7;
        r16 = r8;
        goto L_0x0128;
    L_0x0117:
        r0 = move-exception;
        r3 = r0;
        r23 = r7;
        goto L_0x0165;
    L_0x011c:
        r0 = move-exception;
        r3 = r0;
        r23 = r7;
        goto L_0x0158;
    L_0x0121:
        r0 = move-exception;
        r3 = r0;
        goto L_0x0158;
    L_0x0124:
        r24 = r7;
        r25 = r8;
    L_0x0128:
        r7 = "ogg";	 Catch:{ Exception -> 0x0121 }
        r7 = r11.equals(r7);	 Catch:{ Exception -> 0x0121 }
        if (r7 == 0) goto L_0x013e;	 Catch:{ Exception -> 0x0121 }
    L_0x0130:
        r7 = r15.getAbsolutePath();	 Catch:{ Exception -> 0x0121 }
        r7 = org.telegram.messenger.MediaController.isOpusFile(r7);	 Catch:{ Exception -> 0x0121 }
        r8 = 1;
        if (r7 != r8) goto L_0x013e;
    L_0x013b:
        r3 = 1;
        r18 = r3;
    L_0x013e:
        if (r10 == 0) goto L_0x014a;
    L_0x0140:
        r10.release();	 Catch:{ Exception -> 0x0144 }
        goto L_0x014a;
    L_0x0144:
        r0 = move-exception;
    L_0x0145:
        r3 = r0;
        org.telegram.messenger.FileLog.e(r3);
        goto L_0x018f;
    L_0x014a:
        goto L_0x018f;
    L_0x014b:
        r0 = move-exception;
        r24 = r7;
        r25 = r8;
        r3 = r0;
        goto L_0x0165;
    L_0x0152:
        r0 = move-exception;
        r24 = r7;
        r25 = r8;
        r3 = r0;
    L_0x0158:
        org.telegram.messenger.FileLog.e(r3);	 Catch:{ all -> 0x0163 }
        if (r10 == 0) goto L_0x014a;
    L_0x015d:
        r10.release();	 Catch:{ Exception -> 0x0161 }
        goto L_0x014a;
    L_0x0161:
        r0 = move-exception;
        goto L_0x0145;
    L_0x0163:
        r0 = move-exception;
        r3 = r0;
    L_0x0165:
        if (r10 == 0) goto L_0x0171;
    L_0x0167:
        r10.release();	 Catch:{ Exception -> 0x016b }
        goto L_0x0171;
    L_0x016b:
        r0 = move-exception;
        r7 = r0;
        org.telegram.messenger.FileLog.e(r7);
    L_0x0171:
        throw r3;
    L_0x0172:
        r24 = r7;
        r25 = r8;
    L_0x0176:
        r3 = org.telegram.messenger.audioinfo.AudioInfo.getAudioInfo(r15);
        if (r3 == 0) goto L_0x018e;
    L_0x017c:
        r7 = r3.getDuration();
        r21 = 0;
        r10 = (r7 > r21 ? 1 : (r7 == r21 ? 0 : -1));
        if (r10 == 0) goto L_0x018e;
        r16 = r3.getArtist();
        r17 = r3.getTitle();
    L_0x018f:
        r8 = r16;
        r7 = r17;
        r3 = r23;
        r23 = r18;
        if (r3 == 0) goto L_0x01cd;
        r10 = new org.telegram.tgnet.TLRPC$TL_documentAttributeAudio;
        r10.<init>();
        r10.duration = r3;
        r10.title = r7;
        r10.performer = r8;
        r26 = r3;
        r3 = r10.title;
        if (r3 != 0) goto L_0x01ae;
        r3 = "";
        r10.title = r3;
        r3 = r10.flags;
        r16 = 1;
        r3 = r3 | 1;
        r10.flags = r3;
        r3 = r10.performer;
        if (r3 != 0) goto L_0x01be;
        r3 = "";
        r10.performer = r3;
        r3 = r10.flags;
        r16 = 2;
        r3 = r3 | 2;
        r10.flags = r3;
        if (r23 == 0) goto L_0x01cb;
        r3 = 1;
        r10.voice = r3;
        r3 = r10;
        goto L_0x01d3;
        r26 = r3;
        r16 = 2;
        r3 = r24;
        r10 = 0;
        if (r2 == 0) goto L_0x0223;
        r4 = "attheme";
        r4 = r2.endsWith(r4);
        if (r4 == 0) goto L_0x01e6;
        r10 = 1;
        r4 = r2;
        r27 = r7;
        r28 = r8;
        r2 = r10;
        goto L_0x0229;
        if (r3 == 0) goto L_0x0206;
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r4.append(r2);
        r27 = r7;
        r7 = "audio";
        r4.append(r7);
        r28 = r8;
        r7 = r15.length();
        r4.append(r7);
        r2 = r4.toString();
        r4 = r2;
        goto L_0x01e4;
        r27 = r7;
        r28 = r8;
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r4.append(r2);
        r7 = "";
        r4.append(r7);
        r7 = r15.length();
        r4.append(r7);
        r2 = r4.toString();
        goto L_0x0204;
        r27 = r7;
        r28 = r8;
        r4 = r2;
        r2 = r10;
        r7 = 0;
        if (r2 != 0) goto L_0x026b;
        if (r9 != 0) goto L_0x026b;
        r8 = org.telegram.messenger.MessagesStorage.getInstance(r39);
        if (r9 != 0) goto L_0x0236;
        r10 = 1;
        goto L_0x0237;
        r10 = 4;
        r8 = r8.getSentFile(r4, r10);
        r7 = r8;
        r7 = (org.telegram.tgnet.TLRPC.TL_document) r7;
        if (r7 != 0) goto L_0x026b;
        r8 = r1.equals(r4);
        if (r8 != 0) goto L_0x026b;
        if (r9 != 0) goto L_0x026b;
        r8 = org.telegram.messenger.MessagesStorage.getInstance(r39);
        r10 = new java.lang.StringBuilder;
        r10.<init>();
        r10.append(r1);
        r13 = r15.length();
        r10.append(r13);
        r10 = r10.toString();
        if (r9 != 0) goto L_0x0263;
        r13 = 1;
        goto L_0x0264;
        r13 = 4;
        r8 = r8.getSentFile(r10, r13);
        r7 = r8;
        r7 = (org.telegram.tgnet.TLRPC.TL_document) r7;
        if (r7 != 0) goto L_0x03f1;
        r8 = new org.telegram.tgnet.TLRPC$TL_document;
        r8.<init>();
        r7 = r8;
        r13 = 0;
        r7.id = r13;
        r8 = org.telegram.tgnet.ConnectionsManager.getInstance(r39);
        r8 = r8.getCurrentTime();
        r7.date = r8;
        r8 = new org.telegram.tgnet.TLRPC$TL_documentAttributeFilename;
        r8.<init>();
        r8.file_name = r5;
        r10 = r7.attributes;
        r10.add(r8);
        r13 = r15.length();
        r10 = (int) r13;
        r7.size = r10;
        r10 = 0;
        r7.dc_id = r10;
        if (r3 == 0) goto L_0x029e;
        r10 = r7.attributes;
        r10.add(r3);
        r10 = r12.length();
        if (r10 == 0) goto L_0x0311;
        r10 = r11.hashCode();
        r13 = 109967; // 0x1ad8f float:1.54097E-40 double:5.4331E-319;
        if (r10 == r13) goto L_0x02df;
        r13 = 3145576; // 0x2fff68 float:4.407891E-39 double:1.554121E-317;
        if (r10 == r13) goto L_0x02d3;
        r13 = 3418175; // 0x34283f float:4.789883E-39 double:1.688803E-317;
        if (r10 == r13) goto L_0x02c8;
        r13 = 3645340; // 0x379f9c float:5.10821E-39 double:1.8010373E-317;
        if (r10 == r13) goto L_0x02bd;
        goto L_0x02ea;
        r10 = "webp";
        r10 = r11.equals(r10);
        if (r10 == 0) goto L_0x02ea;
        r20 = 0;
        goto L_0x02ec;
        r10 = "opus";
        r10 = r11.equals(r10);
        if (r10 == 0) goto L_0x02ea;
        r20 = 1;
        goto L_0x02ec;
        r10 = "flac";
        r10 = r11.equals(r10);
        if (r10 == 0) goto L_0x02ea;
        r10 = 3;
        r20 = r10;
        goto L_0x02ec;
        r10 = "ogg";
        r10 = r11.equals(r10);
        if (r10 == 0) goto L_0x02ea;
        r20 = r16;
        goto L_0x02ec;
        r20 = -1;
        switch(r20) {
            case 0: goto L_0x0307;
            case 1: goto L_0x0302;
            case 2: goto L_0x02fd;
            case 3: goto L_0x02f8;
            default: goto L_0x02ef;
        };
        r10 = r6.getMimeTypeFromExtension(r11);
        if (r10 == 0) goto L_0x030c;
        r7.mime_type = r10;
        goto L_0x0310;
        r10 = "audio/flac";
        r7.mime_type = r10;
        goto L_0x0310;
        r10 = "audio/ogg";
        r7.mime_type = r10;
        goto L_0x0310;
        r10 = "audio/opus";
        r7.mime_type = r10;
        goto L_0x0310;
        r10 = "image/webp";
        r7.mime_type = r10;
        goto L_0x0310;
        r13 = "application/octet-stream";
        r7.mime_type = r13;
        goto L_0x0315;
        r10 = "application/octet-stream";
        r7.mime_type = r10;
        r10 = r7.mime_type;
        r13 = "image/gif";
        r10 = r10.equals(r13);
        if (r10 == 0) goto L_0x034b;
        r10 = r15.getAbsolutePath();	 Catch:{ Exception -> 0x0343 }
        r13 = 1119092736; // 0x42b40000 float:90.0 double:5.529052754E-315;
        r29 = r2;
        r2 = 0;
        r14 = 1;
        r10 = org.telegram.messenger.ImageLoader.loadBitmap(r10, r2, r13, r13, r14);	 Catch:{ Exception -> 0x0340 }
        r2 = r10;	 Catch:{ Exception -> 0x0340 }
        if (r2 == 0) goto L_0x033f;	 Catch:{ Exception -> 0x0340 }
        r10 = "animation.gif";	 Catch:{ Exception -> 0x0340 }
        r8.file_name = r10;	 Catch:{ Exception -> 0x0340 }
        r10 = 55;	 Catch:{ Exception -> 0x0340 }
        r10 = org.telegram.messenger.ImageLoader.scaleAndSaveImage(r2, r13, r13, r10, r9);	 Catch:{ Exception -> 0x0340 }
        r7.thumb = r10;	 Catch:{ Exception -> 0x0340 }
        r2.recycle();	 Catch:{ Exception -> 0x0340 }
        goto L_0x034d;
    L_0x0340:
        r0 = move-exception;
        r2 = r0;
        goto L_0x0347;
    L_0x0343:
        r0 = move-exception;
        r29 = r2;
        r2 = r0;
        org.telegram.messenger.FileLog.e(r2);
        goto L_0x034d;
        r29 = r2;
        r2 = r7.mime_type;
        r10 = "image/webp";
        r2 = r2.equals(r10);
        if (r2 == 0) goto L_0x03da;
        if (r19 == 0) goto L_0x03da;
        r2 = new android.graphics.BitmapFactory$Options;
        r2.<init>();
        r10 = 1;
        r2.inJustDecodeBounds = r10;	 Catch:{ Exception -> 0x0396 }
        r10 = new java.io.RandomAccessFile;	 Catch:{ Exception -> 0x038f }
        r13 = "r";	 Catch:{ Exception -> 0x038f }
        r10.<init>(r1, r13);	 Catch:{ Exception -> 0x038f }
        r30 = r10.getChannel();	 Catch:{ Exception -> 0x038f }
        r31 = java.nio.channels.FileChannel.MapMode.READ_ONLY;	 Catch:{ Exception -> 0x038f }
        r32 = 0;	 Catch:{ Exception -> 0x038f }
        r13 = r1.length();	 Catch:{ Exception -> 0x038f }
        r13 = (long) r13;	 Catch:{ Exception -> 0x038f }
        r34 = r13;	 Catch:{ Exception -> 0x038f }
        r13 = r30.map(r31, r32, r34);	 Catch:{ Exception -> 0x038f }
        r14 = r13.limit();	 Catch:{ Exception -> 0x038f }
        r36 = r3;
        r37 = r5;
        r3 = 1;
        r5 = 0;
        org.telegram.messenger.Utilities.loadWebpImage(r5, r13, r14, r2, r3);	 Catch:{ Exception -> 0x038c }
        r10.close();	 Catch:{ Exception -> 0x038c }
        goto L_0x03a0;
    L_0x038c:
        r0 = move-exception;
        r5 = r0;
        goto L_0x039d;
    L_0x038f:
        r0 = move-exception;
        r36 = r3;
        r37 = r5;
        r3 = 1;
        goto L_0x039c;
    L_0x0396:
        r0 = move-exception;
        r36 = r3;
        r37 = r5;
        r3 = r10;
        r5 = r0;
        org.telegram.messenger.FileLog.e(r5);
        r5 = r2.outWidth;
        if (r5 == 0) goto L_0x03df;
        r5 = r2.outHeight;
        if (r5 == 0) goto L_0x03df;
        r5 = r2.outWidth;
        r10 = 800; // 0x320 float:1.121E-42 double:3.953E-321;
        if (r5 > r10) goto L_0x03df;
        r5 = r2.outHeight;
        if (r5 > r10) goto L_0x03df;
        r5 = new org.telegram.tgnet.TLRPC$TL_documentAttributeSticker;
        r5.<init>();
        r10 = "";
        r5.alt = r10;
        r10 = new org.telegram.tgnet.TLRPC$TL_inputStickerSetEmpty;
        r10.<init>();
        r5.stickerset = r10;
        r10 = r7.attributes;
        r10.add(r5);
        r10 = new org.telegram.tgnet.TLRPC$TL_documentAttributeImageSize;
        r10.<init>();
        r13 = r2.outWidth;
        r10.w = r13;
        r13 = r2.outHeight;
        r10.h = r13;
        r13 = r7.attributes;
        r13.add(r10);
        goto L_0x03df;
        r36 = r3;
        r37 = r5;
        r3 = 1;
        r2 = r7.thumb;
        if (r2 != 0) goto L_0x03f8;
        r2 = new org.telegram.tgnet.TLRPC$TL_photoSizeEmpty;
        r2.<init>();
        r7.thumb = r2;
        r2 = r7.thumb;
        r5 = "s";
        r2.type = r5;
        goto L_0x03f8;
        r29 = r2;
        r36 = r3;
        r37 = r5;
        r3 = 1;
        if (r47 == 0) goto L_0x0401;
        r2 = r47.toString();
        r16 = r2;
        goto L_0x0404;
        r2 = "";
        goto L_0x03fe;
        r2 = new java.util.HashMap;
        r2.<init>();
        if (r4 == 0) goto L_0x0410;
        r8 = "originalPath";
        r2.put(r8, r4);
        r8 = r11;
        r11 = r7;
        r20 = r12;
        r12 = r1;
        r13 = new org.telegram.messenger.SendMessagesHelper$16;
        r21 = r9;
        r9 = r13;
        r10 = r39;
        r3 = r13;
        r13 = r44;
        r22 = r15;
        r15 = r46;
        r17 = r48;
        r18 = r2;
        r9.<init>(r10, r11, r12, r13, r15, r16, r17, r18);
        org.telegram.messenger.AndroidUtilities.runOnUIThread(r3);
        r3 = 1;
        return r3;
    L_0x042f:
        r24 = r7;
        r25 = r8;
        r22 = r15;
    L_0x0435:
        r3 = 0;
        return r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.SendMessagesHelper.prepareSendingDocumentInternal(int, java.lang.String, java.lang.String, android.net.Uri, java.lang.String, long, org.telegram.messenger.MessageObject, java.lang.CharSequence, java.util.ArrayList):boolean");
    }

    private void sendMessage(java.lang.String r1, java.lang.String r2, org.telegram.tgnet.TLRPC.MessageMedia r3, org.telegram.tgnet.TLRPC.TL_photo r4, org.telegram.messenger.VideoEditedInfo r5, org.telegram.tgnet.TLRPC.User r6, org.telegram.tgnet.TLRPC.TL_document r7, org.telegram.tgnet.TLRPC.TL_game r8, long r9, java.lang.String r11, org.telegram.messenger.MessageObject r12, org.telegram.tgnet.TLRPC.WebPage r13, boolean r14, org.telegram.messenger.MessageObject r15, java.util.ArrayList<org.telegram.tgnet.TLRPC.MessageEntity> r16, org.telegram.tgnet.TLRPC.ReplyMarkup r17, java.util.HashMap<java.lang.String, java.lang.String> r18, int r19) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.SendMessagesHelper.sendMessage(java.lang.String, java.lang.String, org.telegram.tgnet.TLRPC$MessageMedia, org.telegram.tgnet.TLRPC$TL_photo, org.telegram.messenger.VideoEditedInfo, org.telegram.tgnet.TLRPC$User, org.telegram.tgnet.TLRPC$TL_document, org.telegram.tgnet.TLRPC$TL_game, long, java.lang.String, org.telegram.messenger.MessageObject, org.telegram.tgnet.TLRPC$WebPage, boolean, org.telegram.messenger.MessageObject, java.util.ArrayList, org.telegram.tgnet.TLRPC$ReplyMarkup, java.util.HashMap, int):void
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
        r7 = r122;
        r1 = r123;
        r2 = r125;
        r3 = r126;
        r5 = r128;
        r6 = r129;
        r8 = r130;
        r9 = r131;
        r11 = r133;
        r12 = r134;
        r13 = r135;
        r14 = r137;
        r15 = r138;
        r12 = r140;
        r6 = r141;
        r19 = 0;
        r21 = (r9 > r19 ? 1 : (r9 == r19 ? 0 : -1));
        if (r21 != 0) goto L_0x0025;
    L_0x0024:
        return;
    L_0x0025:
        if (r1 != 0) goto L_0x002e;
    L_0x0027:
        if (r124 != 0) goto L_0x002e;
    L_0x0029:
        r21 = "";
        r22 = r21;
        goto L_0x0030;
    L_0x002e:
        r22 = r124;
    L_0x0030:
        r21 = 0;
        if (r12 == 0) goto L_0x0046;
    L_0x0034:
        r5 = "originalPath";
        r5 = r12.containsKey(r5);
        if (r5 == 0) goto L_0x0046;
    L_0x003c:
        r5 = "originalPath";
        r5 = r12.get(r5);
        r21 = r5;
        r21 = (java.lang.String) r21;
    L_0x0046:
        r5 = r21;
        r21 = 0;
        r24 = 0;
        r25 = 0;
        r26 = -1;
        r27 = r5;
        r5 = (int) r9;
        r28 = 32;
        r2 = r9 >> r28;
        r3 = (int) r2;
        r2 = 0;
        r28 = 0;
        r31 = r2;
        if (r5 == 0) goto L_0x006a;
    L_0x005f:
        r2 = r7.currentAccount;
        r2 = org.telegram.messenger.MessagesController.getInstance(r2);
        r2 = r2.getInputPeer(r5);
        goto L_0x006b;
    L_0x006a:
        r2 = 0;
    L_0x006b:
        r32 = 0;
        if (r5 != 0) goto L_0x00c3;
    L_0x006f:
        r10 = r7.currentAccount;
        r10 = org.telegram.messenger.MessagesController.getInstance(r10);
        r9 = java.lang.Integer.valueOf(r3);
        r28 = r10.getEncryptedChat(r9);
        if (r28 != 0) goto L_0x00bc;
    L_0x007f:
        if (r14 == 0) goto L_0x00b7;
    L_0x0081:
        r9 = r7.currentAccount;
        r9 = org.telegram.messenger.MessagesStorage.getInstance(r9);
        r10 = r14.messageOwner;
        r9.markMessageAsSendError(r10);
        r9 = r14.messageOwner;
        r10 = 2;
        r9.send_state = r10;
        r9 = r7.currentAccount;
        r9 = org.telegram.messenger.NotificationCenter.getInstance(r9);
        r10 = org.telegram.messenger.NotificationCenter.messageSendError;
        r34 = r3;
        r3 = 1;
        r3 = new java.lang.Object[r3];
        r35 = r5;
        r5 = r137.getId();
        r5 = java.lang.Integer.valueOf(r5);
        r19 = 0;
        r3[r19] = r5;
        r9.postNotificationName(r10, r3);
        r3 = r137.getId();
        r7.processSentMessage(r3);
        goto L_0x00bb;
    L_0x00b7:
        r34 = r3;
        r35 = r5;
    L_0x00bb:
        return;
    L_0x00bc:
        r34 = r3;
        r35 = r5;
        r9 = r28;
        goto L_0x00e9;
    L_0x00c3:
        r34 = r3;
        r35 = r5;
        r3 = r2 instanceof org.telegram.tgnet.TLRPC.TL_inputPeerChannel;
        if (r3 == 0) goto L_0x00e7;
    L_0x00cb:
        r3 = r7.currentAccount;
        r3 = org.telegram.messenger.MessagesController.getInstance(r3);
        r5 = r2.channel_id;
        r5 = java.lang.Integer.valueOf(r5);
        r3 = r3.getChat(r5);
        if (r3 == 0) goto L_0x00e3;
    L_0x00dd:
        r5 = r3.megagroup;
        if (r5 != 0) goto L_0x00e3;
    L_0x00e1:
        r5 = 1;
        goto L_0x00e4;
    L_0x00e3:
        r5 = 0;
    L_0x00e4:
        r3 = r5;
        r31 = r3;
    L_0x00e7:
        r9 = r28;
    L_0x00e9:
        r5 = 4;
        if (r14 == 0) goto L_0x0279;
    L_0x00ec:
        r3 = r14.messageOwner;	 Catch:{ Exception -> 0x0253 }
        r21 = r137.isForwarded();	 Catch:{ Exception -> 0x022e }
        if (r21 == 0) goto L_0x010a;	 Catch:{ Exception -> 0x022e }
    L_0x00f4:
        r21 = 4;	 Catch:{ Exception -> 0x022e }
        r10 = r6;	 Catch:{ Exception -> 0x022e }
        r45 = r13;	 Catch:{ Exception -> 0x022e }
        r8 = r21;	 Catch:{ Exception -> 0x022e }
        r46 = r22;	 Catch:{ Exception -> 0x022e }
        r5 = r125;	 Catch:{ Exception -> 0x022e }
        r15 = r129;	 Catch:{ Exception -> 0x022e }
        r6 = r1;	 Catch:{ Exception -> 0x022e }
        r13 = r12;	 Catch:{ Exception -> 0x022e }
        r1 = r128;	 Catch:{ Exception -> 0x022e }
        r12 = r3;	 Catch:{ Exception -> 0x022e }
        r3 = r126;	 Catch:{ Exception -> 0x022e }
        goto L_0x0817;	 Catch:{ Exception -> 0x022e }
    L_0x010a:
        r10 = r14.type;	 Catch:{ Exception -> 0x022e }
        if (r10 != 0) goto L_0x0124;	 Catch:{ Exception -> 0x022e }
    L_0x010e:
        r10 = r14.messageOwner;	 Catch:{ Exception -> 0x022e }
        r10 = r10.media;	 Catch:{ Exception -> 0x022e }
        r10 = r10 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGame;	 Catch:{ Exception -> 0x022e }
        if (r10 == 0) goto L_0x0117;	 Catch:{ Exception -> 0x022e }
    L_0x0116:
        goto L_0x011a;	 Catch:{ Exception -> 0x022e }
    L_0x0117:
        r10 = r3.message;	 Catch:{ Exception -> 0x022e }
        r1 = r10;	 Catch:{ Exception -> 0x022e }
    L_0x011a:
        r26 = 0;	 Catch:{ Exception -> 0x022e }
        r10 = 2;	 Catch:{ Exception -> 0x022e }
    L_0x011d:
        r18 = r129;	 Catch:{ Exception -> 0x022e }
    L_0x011f:
        r23 = r128;	 Catch:{ Exception -> 0x022e }
    L_0x0121:
        r29 = r125;	 Catch:{ Exception -> 0x022e }
        goto L_0x0133;	 Catch:{ Exception -> 0x022e }
    L_0x0124:
        r10 = r14.type;	 Catch:{ Exception -> 0x022e }
        if (r10 != r5) goto L_0x0137;	 Catch:{ Exception -> 0x022e }
    L_0x0128:
        r10 = r3.media;	 Catch:{ Exception -> 0x022e }
        r26 = 1;	 Catch:{ Exception -> 0x022e }
        r29 = r10;	 Catch:{ Exception -> 0x022e }
        r10 = 2;	 Catch:{ Exception -> 0x022e }
        r18 = r129;	 Catch:{ Exception -> 0x022e }
        r23 = r128;	 Catch:{ Exception -> 0x022e }
    L_0x0133:
        r30 = r126;	 Catch:{ Exception -> 0x022e }
        goto L_0x01e5;	 Catch:{ Exception -> 0x022e }
    L_0x0137:
        r10 = r14.type;	 Catch:{ Exception -> 0x022e }
        r5 = 1;	 Catch:{ Exception -> 0x022e }
        if (r10 != r5) goto L_0x014f;	 Catch:{ Exception -> 0x022e }
    L_0x013c:
        r5 = r3.media;	 Catch:{ Exception -> 0x022e }
        r5 = r5.photo;	 Catch:{ Exception -> 0x022e }
        r5 = (org.telegram.tgnet.TLRPC.TL_photo) r5;	 Catch:{ Exception -> 0x022e }
        r26 = 2;	 Catch:{ Exception -> 0x022e }
        r30 = r5;	 Catch:{ Exception -> 0x022e }
        r10 = 2;	 Catch:{ Exception -> 0x022e }
        r18 = r129;	 Catch:{ Exception -> 0x022e }
        r23 = r128;	 Catch:{ Exception -> 0x022e }
        r29 = r125;	 Catch:{ Exception -> 0x022e }
        goto L_0x01e5;	 Catch:{ Exception -> 0x022e }
    L_0x014f:
        r5 = r14.type;	 Catch:{ Exception -> 0x022e }
        r10 = 3;	 Catch:{ Exception -> 0x022e }
        if (r5 == r10) goto L_0x01d8;	 Catch:{ Exception -> 0x022e }
    L_0x0154:
        r5 = r14.type;	 Catch:{ Exception -> 0x022e }
        r10 = 5;	 Catch:{ Exception -> 0x022e }
        if (r5 == r10) goto L_0x01d8;	 Catch:{ Exception -> 0x022e }
    L_0x0159:
        if (r127 == 0) goto L_0x015d;	 Catch:{ Exception -> 0x022e }
    L_0x015b:
        goto L_0x01d8;	 Catch:{ Exception -> 0x022e }
    L_0x015d:
        r5 = r14.type;	 Catch:{ Exception -> 0x022e }
        r10 = 12;	 Catch:{ Exception -> 0x022e }
        if (r5 != r10) goto L_0x01a7;	 Catch:{ Exception -> 0x022e }
    L_0x0163:
        r5 = new org.telegram.tgnet.TLRPC$TL_userRequest_old2;	 Catch:{ Exception -> 0x022e }
        r5.<init>();	 Catch:{ Exception -> 0x022e }
        r10 = r3.media;	 Catch:{ Exception -> 0x0188 }
        r10 = r10.phone_number;	 Catch:{ Exception -> 0x0188 }
        r5.phone = r10;	 Catch:{ Exception -> 0x0188 }
        r10 = r3.media;	 Catch:{ Exception -> 0x0188 }
        r10 = r10.first_name;	 Catch:{ Exception -> 0x0188 }
        r5.first_name = r10;	 Catch:{ Exception -> 0x0188 }
        r10 = r3.media;	 Catch:{ Exception -> 0x0188 }
        r10 = r10.last_name;	 Catch:{ Exception -> 0x0188 }
        r5.last_name = r10;	 Catch:{ Exception -> 0x0188 }
        r10 = r3.media;	 Catch:{ Exception -> 0x0188 }
        r10 = r10.user_id;	 Catch:{ Exception -> 0x0188 }
        r5.id = r10;	 Catch:{ Exception -> 0x0188 }
        r26 = 6;
        r23 = r5;
        r10 = 2;
        r18 = r129;
        goto L_0x0121;
    L_0x0188:
        r0 = move-exception;
        r4 = r127;
        r6 = r1;
        r118 = r2;
        r108 = r5;
        r2 = r9;
        r45 = r13;
        r111 = r22;
        r15 = r24;
        r8 = r26;
        r114 = r27;
        r89 = r32;
        r101 = r34;
        r17 = r35;
        r5 = r125;
        r11 = r129;
        goto L_0x024b;
    L_0x01a7:
        r5 = r14.type;	 Catch:{ Exception -> 0x022e }
        r10 = 8;	 Catch:{ Exception -> 0x022e }
        if (r5 == r10) goto L_0x01ce;	 Catch:{ Exception -> 0x022e }
    L_0x01ad:
        r5 = r14.type;	 Catch:{ Exception -> 0x022e }
        r10 = 9;	 Catch:{ Exception -> 0x022e }
        if (r5 == r10) goto L_0x01ce;	 Catch:{ Exception -> 0x022e }
    L_0x01b3:
        r5 = r14.type;	 Catch:{ Exception -> 0x022e }
        r10 = 13;	 Catch:{ Exception -> 0x022e }
        if (r5 == r10) goto L_0x01ce;	 Catch:{ Exception -> 0x022e }
    L_0x01b9:
        r5 = r14.type;	 Catch:{ Exception -> 0x022e }
        r10 = 14;	 Catch:{ Exception -> 0x022e }
        if (r5 != r10) goto L_0x01c0;	 Catch:{ Exception -> 0x022e }
    L_0x01bf:
        goto L_0x01ce;	 Catch:{ Exception -> 0x022e }
    L_0x01c0:
        r5 = r14.type;	 Catch:{ Exception -> 0x022e }
        r10 = 2;	 Catch:{ Exception -> 0x022e }
        if (r5 != r10) goto L_0x011d;	 Catch:{ Exception -> 0x022e }
    L_0x01c5:
        r5 = r3.media;	 Catch:{ Exception -> 0x022e }
        r5 = r5.document;	 Catch:{ Exception -> 0x022e }
        r5 = (org.telegram.tgnet.TLRPC.TL_document) r5;	 Catch:{ Exception -> 0x022e }
        r26 = 8;	 Catch:{ Exception -> 0x022e }
        goto L_0x01e1;	 Catch:{ Exception -> 0x022e }
    L_0x01ce:
        r10 = 2;	 Catch:{ Exception -> 0x022e }
        r5 = r3.media;	 Catch:{ Exception -> 0x022e }
        r5 = r5.document;	 Catch:{ Exception -> 0x022e }
        r5 = (org.telegram.tgnet.TLRPC.TL_document) r5;	 Catch:{ Exception -> 0x022e }
        r26 = 7;	 Catch:{ Exception -> 0x022e }
        goto L_0x01e1;	 Catch:{ Exception -> 0x022e }
    L_0x01d8:
        r10 = 2;	 Catch:{ Exception -> 0x022e }
        r26 = 3;	 Catch:{ Exception -> 0x022e }
        r5 = r3.media;	 Catch:{ Exception -> 0x022e }
        r5 = r5.document;	 Catch:{ Exception -> 0x022e }
        r5 = (org.telegram.tgnet.TLRPC.TL_document) r5;	 Catch:{ Exception -> 0x022e }
    L_0x01e1:
        r18 = r5;
        goto L_0x011f;
    L_0x01e5:
        if (r12 == 0) goto L_0x021a;
    L_0x01e7:
        r5 = "query_id";	 Catch:{ Exception -> 0x01fa }
        r5 = r12.containsKey(r5);	 Catch:{ Exception -> 0x01fa }
        if (r5 == 0) goto L_0x021a;
    L_0x01ef:
        r5 = 9;
        r8 = r5;
        r10 = r6;
        r45 = r13;
        r15 = r18;
        r46 = r22;
        goto L_0x0223;
    L_0x01fa:
        r0 = move-exception;
        r4 = r127;
        r6 = r1;
        r118 = r2;
        r2 = r9;
        r45 = r13;
        r11 = r18;
        r111 = r22;
        r108 = r23;
        r15 = r24;
        r8 = r26;
        r114 = r27;
        r5 = r29;
        r113 = r30;
        r89 = r32;
        r101 = r34;
        r17 = r35;
        goto L_0x024d;
    L_0x021a:
        r10 = r6;
        r45 = r13;
        r15 = r18;
        r46 = r22;
        r8 = r26;
    L_0x0223:
        r5 = r29;
        r6 = r1;
        r13 = r12;
        r1 = r23;
        r12 = r3;
        r3 = r30;
        goto L_0x0817;
    L_0x022e:
        r0 = move-exception;
        r4 = r127;
        r6 = r1;
        r118 = r2;
        r2 = r9;
        r45 = r13;
        r111 = r22;
        r15 = r24;
        r8 = r26;
        r114 = r27;
        r89 = r32;
        r101 = r34;
        r17 = r35;
        r5 = r125;
        r11 = r129;
    L_0x0249:
        r108 = r128;
    L_0x024b:
        r113 = r126;
    L_0x024d:
        r1 = r0;
        r13 = r12;
    L_0x024f:
        r12 = r3;
    L_0x0250:
        r3 = r14;
        goto L_0x29e0;
    L_0x0253:
        r0 = move-exception;
        r4 = r127;
        r6 = r1;
        r118 = r2;
        r2 = r9;
        r45 = r13;
        r3 = r14;
        r111 = r22;
        r15 = r24;
        r8 = r26;
        r114 = r27;
        r89 = r32;
        r101 = r34;
        r17 = r35;
        r5 = r125;
    L_0x026d:
        r11 = r129;
        r108 = r128;
    L_0x0271:
        r113 = r126;
        r1 = r0;
    L_0x0274:
        r13 = r12;
    L_0x0275:
        r12 = r21;
        goto L_0x29e0;
    L_0x0279:
        r10 = 2;
        if (r1 == 0) goto L_0x02ee;
    L_0x027c:
        if (r9 == 0) goto L_0x0284;
    L_0x027e:
        r3 = new org.telegram.tgnet.TLRPC$TL_message_secret;	 Catch:{ Exception -> 0x0253 }
        r3.<init>();	 Catch:{ Exception -> 0x0253 }
    L_0x0283:
        goto L_0x028a;	 Catch:{ Exception -> 0x0253 }
    L_0x0284:
        r3 = new org.telegram.tgnet.TLRPC$TL_message;	 Catch:{ Exception -> 0x0253 }
        r3.<init>();	 Catch:{ Exception -> 0x0253 }
        goto L_0x0283;
    L_0x028a:
        if (r9 == 0) goto L_0x02a1;
    L_0x028c:
        r5 = r13 instanceof org.telegram.tgnet.TLRPC.TL_webPagePending;	 Catch:{ Exception -> 0x022e }
        if (r5 == 0) goto L_0x02a1;	 Catch:{ Exception -> 0x022e }
    L_0x0290:
        r5 = r13.url;	 Catch:{ Exception -> 0x022e }
        if (r5 == 0) goto L_0x029f;	 Catch:{ Exception -> 0x022e }
    L_0x0294:
        r5 = new org.telegram.tgnet.TLRPC$TL_webPageUrlPending;	 Catch:{ Exception -> 0x022e }
        r5.<init>();	 Catch:{ Exception -> 0x022e }
        r10 = r13.url;	 Catch:{ Exception -> 0x022e }
        r5.url = r10;	 Catch:{ Exception -> 0x022e }
        goto L_0x02a2;
    L_0x029f:
        r5 = 0;
        goto L_0x02a2;
    L_0x02a1:
        r5 = r13;
    L_0x02a2:
        if (r5 != 0) goto L_0x02ce;
    L_0x02a4:
        r10 = new org.telegram.tgnet.TLRPC$TL_messageMediaEmpty;	 Catch:{ Exception -> 0x02ac }
        r10.<init>();	 Catch:{ Exception -> 0x02ac }
        r3.media = r10;	 Catch:{ Exception -> 0x02ac }
        goto L_0x02d9;	 Catch:{ Exception -> 0x02ac }
    L_0x02ac:
        r0 = move-exception;	 Catch:{ Exception -> 0x02ac }
        r4 = r127;	 Catch:{ Exception -> 0x02ac }
        r6 = r1;	 Catch:{ Exception -> 0x02ac }
        r118 = r2;	 Catch:{ Exception -> 0x02ac }
        r45 = r5;	 Catch:{ Exception -> 0x02ac }
        r2 = r9;	 Catch:{ Exception -> 0x02ac }
        r13 = r12;	 Catch:{ Exception -> 0x02ac }
        r111 = r22;	 Catch:{ Exception -> 0x02ac }
        r15 = r24;	 Catch:{ Exception -> 0x02ac }
        r8 = r26;	 Catch:{ Exception -> 0x02ac }
        r114 = r27;	 Catch:{ Exception -> 0x02ac }
        r89 = r32;	 Catch:{ Exception -> 0x02ac }
        r101 = r34;	 Catch:{ Exception -> 0x02ac }
        r17 = r35;	 Catch:{ Exception -> 0x02ac }
        r5 = r125;	 Catch:{ Exception -> 0x02ac }
        r11 = r129;	 Catch:{ Exception -> 0x02ac }
        r108 = r128;	 Catch:{ Exception -> 0x02ac }
        r113 = r126;	 Catch:{ Exception -> 0x02ac }
        r1 = r0;	 Catch:{ Exception -> 0x02ac }
        goto L_0x024f;	 Catch:{ Exception -> 0x02ac }
    L_0x02ce:
        r10 = new org.telegram.tgnet.TLRPC$TL_messageMediaWebPage;	 Catch:{ Exception -> 0x02ac }
        r10.<init>();	 Catch:{ Exception -> 0x02ac }
        r3.media = r10;	 Catch:{ Exception -> 0x02ac }
        r10 = r3.media;	 Catch:{ Exception -> 0x02ac }
        r10.webpage = r5;	 Catch:{ Exception -> 0x02ac }
    L_0x02d9:
        if (r12 == 0) goto L_0x02e6;	 Catch:{ Exception -> 0x02ac }
    L_0x02db:
        r10 = "query_id";	 Catch:{ Exception -> 0x02ac }
        r10 = r12.containsKey(r10);	 Catch:{ Exception -> 0x02ac }
        if (r10 == 0) goto L_0x02e6;	 Catch:{ Exception -> 0x02ac }
    L_0x02e3:
        r10 = 9;	 Catch:{ Exception -> 0x02ac }
        goto L_0x02e7;	 Catch:{ Exception -> 0x02ac }
    L_0x02e6:
        r10 = 0;	 Catch:{ Exception -> 0x02ac }
    L_0x02e7:
        r26 = r10;	 Catch:{ Exception -> 0x02ac }
        r3.message = r1;	 Catch:{ Exception -> 0x02ac }
        r1 = r3;
        r13 = r5;
        goto L_0x032d;
    L_0x02ee:
        r3 = r125;
        if (r3 == 0) goto L_0x0353;
    L_0x02f2:
        if (r9 == 0) goto L_0x0315;
    L_0x02f4:
        r5 = new org.telegram.tgnet.TLRPC$TL_message_secret;	 Catch:{ Exception -> 0x02fa }
        r5.<init>();	 Catch:{ Exception -> 0x02fa }
    L_0x02f9:
        goto L_0x031b;	 Catch:{ Exception -> 0x02fa }
    L_0x02fa:
        r0 = move-exception;	 Catch:{ Exception -> 0x02fa }
        r4 = r127;	 Catch:{ Exception -> 0x02fa }
        r6 = r1;	 Catch:{ Exception -> 0x02fa }
        r118 = r2;	 Catch:{ Exception -> 0x02fa }
        r5 = r3;	 Catch:{ Exception -> 0x02fa }
        r2 = r9;	 Catch:{ Exception -> 0x02fa }
        r45 = r13;	 Catch:{ Exception -> 0x02fa }
        r3 = r14;	 Catch:{ Exception -> 0x02fa }
        r111 = r22;	 Catch:{ Exception -> 0x02fa }
        r15 = r24;	 Catch:{ Exception -> 0x02fa }
        r8 = r26;	 Catch:{ Exception -> 0x02fa }
        r114 = r27;	 Catch:{ Exception -> 0x02fa }
        r89 = r32;	 Catch:{ Exception -> 0x02fa }
        r101 = r34;	 Catch:{ Exception -> 0x02fa }
        r17 = r35;	 Catch:{ Exception -> 0x02fa }
        goto L_0x026d;	 Catch:{ Exception -> 0x02fa }
    L_0x0315:
        r5 = new org.telegram.tgnet.TLRPC$TL_message;	 Catch:{ Exception -> 0x02fa }
        r5.<init>();	 Catch:{ Exception -> 0x02fa }
        goto L_0x02f9;
    L_0x031b:
        r5.media = r3;	 Catch:{ Exception -> 0x0330 }
        if (r12 == 0) goto L_0x032a;	 Catch:{ Exception -> 0x0330 }
    L_0x031f:
        r10 = "query_id";	 Catch:{ Exception -> 0x0330 }
        r10 = r12.containsKey(r10);	 Catch:{ Exception -> 0x0330 }
        if (r10 == 0) goto L_0x032a;
    L_0x0327:
        r26 = 9;
        goto L_0x032c;
    L_0x032a:
        r26 = 1;
    L_0x032c:
        r1 = r5;
    L_0x032d:
        r10 = r6;
        goto L_0x079a;
    L_0x0330:
        r0 = move-exception;
        r4 = r127;
        r6 = r1;
        r118 = r2;
        r2 = r9;
        r45 = r13;
        r111 = r22;
        r15 = r24;
        r8 = r26;
        r114 = r27;
        r89 = r32;
        r101 = r34;
        r17 = r35;
        r11 = r129;
        r108 = r128;
        r113 = r126;
        r1 = r0;
        r13 = r12;
        r12 = r5;
        r5 = r3;
        goto L_0x0250;
    L_0x0353:
        r5 = r126;
        if (r5 == 0) goto L_0x0468;
    L_0x0357:
        if (r9 == 0) goto L_0x0382;
    L_0x0359:
        r10 = new org.telegram.tgnet.TLRPC$TL_message_secret;	 Catch:{ Exception -> 0x035f }
        r10.<init>();	 Catch:{ Exception -> 0x035f }
    L_0x035e:
        goto L_0x0388;
    L_0x035f:
        r0 = move-exception;
        r4 = r127;
        r6 = r1;
        r118 = r2;
        r113 = r5;
        r2 = r9;
        r45 = r13;
        r111 = r22;
        r15 = r24;
        r8 = r26;
        r114 = r27;
        r89 = r32;
        r101 = r34;
        r17 = r35;
        r11 = r129;
        r108 = r128;
        r1 = r0;
        r5 = r3;
        r13 = r12;
        r3 = r14;
        goto L_0x0275;
    L_0x0382:
        r10 = new org.telegram.tgnet.TLRPC$TL_message;	 Catch:{ Exception -> 0x0444 }
        r10.<init>();	 Catch:{ Exception -> 0x0444 }
        goto L_0x035e;
    L_0x0388:
        r1 = new org.telegram.tgnet.TLRPC$TL_messageMediaPhoto;	 Catch:{ Exception -> 0x041e }
        r1.<init>();	 Catch:{ Exception -> 0x041e }
        r10.media = r1;	 Catch:{ Exception -> 0x041e }
        r1 = r10.media;	 Catch:{ Exception -> 0x041e }
        r3 = r1.flags;	 Catch:{ Exception -> 0x041e }
        r21 = 3;	 Catch:{ Exception -> 0x041e }
        r3 = r3 | 3;	 Catch:{ Exception -> 0x041e }
        r1.flags = r3;	 Catch:{ Exception -> 0x041e }
        if (r15 == 0) goto L_0x03c2;
    L_0x039b:
        r10.entities = r15;	 Catch:{ Exception -> 0x039e }
        goto L_0x03c2;	 Catch:{ Exception -> 0x039e }
    L_0x039e:
        r0 = move-exception;	 Catch:{ Exception -> 0x039e }
        r4 = r127;	 Catch:{ Exception -> 0x039e }
        r1 = r0;	 Catch:{ Exception -> 0x039e }
        r118 = r2;	 Catch:{ Exception -> 0x039e }
        r113 = r5;	 Catch:{ Exception -> 0x039e }
        r2 = r9;	 Catch:{ Exception -> 0x039e }
        r45 = r13;	 Catch:{ Exception -> 0x039e }
        r3 = r14;	 Catch:{ Exception -> 0x039e }
        r111 = r22;	 Catch:{ Exception -> 0x039e }
        r15 = r24;	 Catch:{ Exception -> 0x039e }
        r8 = r26;	 Catch:{ Exception -> 0x039e }
        r114 = r27;	 Catch:{ Exception -> 0x039e }
        r89 = r32;	 Catch:{ Exception -> 0x039e }
        r101 = r34;	 Catch:{ Exception -> 0x039e }
        r17 = r35;	 Catch:{ Exception -> 0x039e }
        r5 = r125;	 Catch:{ Exception -> 0x039e }
        r6 = r123;	 Catch:{ Exception -> 0x039e }
        r11 = r129;	 Catch:{ Exception -> 0x039e }
        r108 = r128;	 Catch:{ Exception -> 0x039e }
        goto L_0x0440;	 Catch:{ Exception -> 0x039e }
    L_0x03c2:
        if (r6 == 0) goto L_0x03d4;	 Catch:{ Exception -> 0x039e }
    L_0x03c4:
        r1 = r10.media;	 Catch:{ Exception -> 0x039e }
        r1.ttl_seconds = r6;	 Catch:{ Exception -> 0x039e }
        r10.ttl = r6;	 Catch:{ Exception -> 0x039e }
        r1 = r10.media;	 Catch:{ Exception -> 0x039e }
        r3 = r1.flags;	 Catch:{ Exception -> 0x039e }
        r21 = 4;	 Catch:{ Exception -> 0x039e }
        r3 = r3 | 4;	 Catch:{ Exception -> 0x039e }
        r1.flags = r3;	 Catch:{ Exception -> 0x039e }
    L_0x03d4:
        r1 = r10.media;	 Catch:{ Exception -> 0x041e }
        r1.photo = r5;	 Catch:{ Exception -> 0x041e }
        if (r12 == 0) goto L_0x03e5;
    L_0x03da:
        r1 = "query_id";	 Catch:{ Exception -> 0x039e }
        r1 = r12.containsKey(r1);	 Catch:{ Exception -> 0x039e }
        if (r1 == 0) goto L_0x03e5;	 Catch:{ Exception -> 0x039e }
    L_0x03e2:
        r1 = 9;	 Catch:{ Exception -> 0x039e }
        goto L_0x03e6;	 Catch:{ Exception -> 0x039e }
    L_0x03e5:
        r1 = 2;	 Catch:{ Exception -> 0x039e }
    L_0x03e6:
        r26 = r1;	 Catch:{ Exception -> 0x039e }
        if (r11 == 0) goto L_0x03fb;	 Catch:{ Exception -> 0x039e }
    L_0x03ea:
        r1 = r133.length();	 Catch:{ Exception -> 0x039e }
        if (r1 <= 0) goto L_0x03fb;	 Catch:{ Exception -> 0x039e }
    L_0x03f0:
        r1 = "http";	 Catch:{ Exception -> 0x039e }
        r1 = r11.startsWith(r1);	 Catch:{ Exception -> 0x039e }
        if (r1 == 0) goto L_0x03fb;	 Catch:{ Exception -> 0x039e }
    L_0x03f8:
        r10.attachPath = r11;	 Catch:{ Exception -> 0x039e }
        goto L_0x041b;
    L_0x03fb:
        r1 = r5.sizes;	 Catch:{ Exception -> 0x041e }
        r3 = r5.sizes;	 Catch:{ Exception -> 0x041e }
        r3 = r3.size();	 Catch:{ Exception -> 0x041e }
        r21 = 1;	 Catch:{ Exception -> 0x041e }
        r3 = r3 + -1;	 Catch:{ Exception -> 0x041e }
        r1 = r1.get(r3);	 Catch:{ Exception -> 0x041e }
        r1 = (org.telegram.tgnet.TLRPC.PhotoSize) r1;	 Catch:{ Exception -> 0x041e }
        r1 = r1.location;	 Catch:{ Exception -> 0x041e }
        r3 = 1;	 Catch:{ Exception -> 0x041e }
        r5 = org.telegram.messenger.FileLoader.getPathToAttach(r1, r3);	 Catch:{ Exception -> 0x041e }
        r3 = r5.toString();	 Catch:{ Exception -> 0x041e }
        r10.attachPath = r3;	 Catch:{ Exception -> 0x041e }
    L_0x041b:
        r1 = r10;
        goto L_0x032d;
    L_0x041e:
        r0 = move-exception;
        r4 = r127;
        r1 = r0;
        r118 = r2;
        r2 = r9;
        r45 = r13;
        r3 = r14;
        r111 = r22;
        r15 = r24;
        r8 = r26;
        r114 = r27;
        r89 = r32;
        r101 = r34;
        r17 = r35;
        r5 = r125;
        r6 = r123;
        r11 = r129;
        r108 = r128;
        r113 = r126;
    L_0x0440:
        r13 = r12;
        r12 = r10;
        goto L_0x29e0;
    L_0x0444:
        r0 = move-exception;
        r4 = r127;
        r1 = r0;
        r118 = r2;
        r2 = r9;
        r45 = r13;
        r3 = r14;
        r111 = r22;
        r15 = r24;
        r8 = r26;
        r114 = r27;
        r89 = r32;
        r101 = r34;
        r17 = r35;
        r5 = r125;
        r6 = r123;
        r11 = r129;
        r108 = r128;
    L_0x0464:
        r113 = r126;
        goto L_0x0274;
    L_0x0468:
        if (r8 == 0) goto L_0x04ad;
    L_0x046a:
        r1 = new org.telegram.tgnet.TLRPC$TL_message;	 Catch:{ Exception -> 0x0444 }
        r1.<init>();	 Catch:{ Exception -> 0x0444 }
        r3 = new org.telegram.tgnet.TLRPC$TL_messageMediaGame;	 Catch:{ Exception -> 0x0488 }
        r3.<init>();	 Catch:{ Exception -> 0x0488 }
        r1.media = r3;	 Catch:{ Exception -> 0x0488 }
        r3 = r1.media;	 Catch:{ Exception -> 0x0488 }
        r3.game = r8;	 Catch:{ Exception -> 0x0488 }
        if (r12 == 0) goto L_0x032d;	 Catch:{ Exception -> 0x0488 }
    L_0x047c:
        r3 = "query_id";	 Catch:{ Exception -> 0x0488 }
        r3 = r12.containsKey(r3);	 Catch:{ Exception -> 0x0488 }
        if (r3 == 0) goto L_0x032d;
    L_0x0484:
        r26 = 9;
        goto L_0x032d;
    L_0x0488:
        r0 = move-exception;
        r4 = r127;
        r118 = r2;
        r2 = r9;
        r45 = r13;
        r3 = r14;
        r111 = r22;
    L_0x0493:
        r15 = r24;
        r8 = r26;
        r114 = r27;
        r89 = r32;
        r101 = r34;
        r17 = r35;
        r5 = r125;
        r6 = r123;
        r11 = r129;
        r108 = r128;
    L_0x04a7:
        r113 = r126;
        r13 = r12;
        r12 = r1;
        goto L_0x0844;
    L_0x04ad:
        r5 = r128;
        if (r5 == 0) goto L_0x054e;
    L_0x04b1:
        if (r9 == 0) goto L_0x04da;
    L_0x04b3:
        r1 = new org.telegram.tgnet.TLRPC$TL_message_secret;	 Catch:{ Exception -> 0x04b9 }
        r1.<init>();	 Catch:{ Exception -> 0x04b9 }
    L_0x04b8:
        goto L_0x04e0;	 Catch:{ Exception -> 0x04b9 }
    L_0x04b9:
        r0 = move-exception;	 Catch:{ Exception -> 0x04b9 }
        r4 = r127;	 Catch:{ Exception -> 0x04b9 }
        r1 = r0;	 Catch:{ Exception -> 0x04b9 }
        r118 = r2;	 Catch:{ Exception -> 0x04b9 }
        r108 = r5;	 Catch:{ Exception -> 0x04b9 }
        r2 = r9;	 Catch:{ Exception -> 0x04b9 }
        r45 = r13;	 Catch:{ Exception -> 0x04b9 }
        r3 = r14;	 Catch:{ Exception -> 0x04b9 }
        r111 = r22;	 Catch:{ Exception -> 0x04b9 }
        r15 = r24;	 Catch:{ Exception -> 0x04b9 }
        r8 = r26;	 Catch:{ Exception -> 0x04b9 }
        r114 = r27;	 Catch:{ Exception -> 0x04b9 }
        r89 = r32;	 Catch:{ Exception -> 0x04b9 }
        r101 = r34;	 Catch:{ Exception -> 0x04b9 }
        r17 = r35;	 Catch:{ Exception -> 0x04b9 }
        r5 = r125;	 Catch:{ Exception -> 0x04b9 }
        r6 = r123;	 Catch:{ Exception -> 0x04b9 }
        r11 = r129;	 Catch:{ Exception -> 0x04b9 }
        goto L_0x0464;	 Catch:{ Exception -> 0x04b9 }
    L_0x04da:
        r1 = new org.telegram.tgnet.TLRPC$TL_message;	 Catch:{ Exception -> 0x04b9 }
        r1.<init>();	 Catch:{ Exception -> 0x04b9 }
        goto L_0x04b8;
    L_0x04e0:
        r3 = new org.telegram.tgnet.TLRPC$TL_messageMediaContact;	 Catch:{ Exception -> 0x052d }
        r3.<init>();	 Catch:{ Exception -> 0x052d }
        r1.media = r3;	 Catch:{ Exception -> 0x052d }
        r3 = r1.media;	 Catch:{ Exception -> 0x052d }
        r10 = r5.phone;	 Catch:{ Exception -> 0x052d }
        r3.phone_number = r10;	 Catch:{ Exception -> 0x052d }
        r3 = r1.media;	 Catch:{ Exception -> 0x052d }
        r10 = r5.first_name;	 Catch:{ Exception -> 0x052d }
        r3.first_name = r10;	 Catch:{ Exception -> 0x052d }
        r3 = r1.media;	 Catch:{ Exception -> 0x052d }
        r10 = r5.last_name;	 Catch:{ Exception -> 0x052d }
        r3.last_name = r10;	 Catch:{ Exception -> 0x052d }
        r3 = r1.media;	 Catch:{ Exception -> 0x052d }
        r10 = r5.id;	 Catch:{ Exception -> 0x052d }
        r3.user_id = r10;	 Catch:{ Exception -> 0x052d }
        r3 = r1.media;	 Catch:{ Exception -> 0x052d }
        r3 = r3.first_name;	 Catch:{ Exception -> 0x052d }
        if (r3 != 0) goto L_0x050d;	 Catch:{ Exception -> 0x052d }
    L_0x0505:
        r3 = r1.media;	 Catch:{ Exception -> 0x052d }
        r10 = "";	 Catch:{ Exception -> 0x052d }
        r3.first_name = r10;	 Catch:{ Exception -> 0x052d }
        r5.first_name = r10;	 Catch:{ Exception -> 0x052d }
    L_0x050d:
        r3 = r1.media;	 Catch:{ Exception -> 0x052d }
        r3 = r3.last_name;	 Catch:{ Exception -> 0x052d }
        if (r3 != 0) goto L_0x051b;	 Catch:{ Exception -> 0x052d }
    L_0x0513:
        r3 = r1.media;	 Catch:{ Exception -> 0x052d }
        r10 = "";	 Catch:{ Exception -> 0x052d }
        r3.last_name = r10;	 Catch:{ Exception -> 0x052d }
        r5.last_name = r10;	 Catch:{ Exception -> 0x052d }
    L_0x051b:
        if (r12 == 0) goto L_0x0529;	 Catch:{ Exception -> 0x052d }
    L_0x051d:
        r3 = "query_id";	 Catch:{ Exception -> 0x052d }
        r3 = r12.containsKey(r3);	 Catch:{ Exception -> 0x052d }
        if (r3 == 0) goto L_0x0529;
    L_0x0525:
        r26 = 9;
        goto L_0x032d;
    L_0x0529:
        r26 = 6;
        goto L_0x032d;
    L_0x052d:
        r0 = move-exception;
        r4 = r127;
        r118 = r2;
        r108 = r5;
        r2 = r9;
        r45 = r13;
        r3 = r14;
        r111 = r22;
        r15 = r24;
        r8 = r26;
        r114 = r27;
        r89 = r32;
        r101 = r34;
        r17 = r35;
        r5 = r125;
        r6 = r123;
        r11 = r129;
        goto L_0x04a7;
    L_0x054e:
        r10 = r6;
        r1 = r129;
        if (r1 == 0) goto L_0x0796;
    L_0x0553:
        if (r9 == 0) goto L_0x057b;
    L_0x0555:
        r3 = new org.telegram.tgnet.TLRPC$TL_message_secret;	 Catch:{ Exception -> 0x055b }
        r3.<init>();	 Catch:{ Exception -> 0x055b }
    L_0x055a:
        goto L_0x0581;
    L_0x055b:
        r0 = move-exception;
        r4 = r127;
        r11 = r1;
        r118 = r2;
        r108 = r5;
        r2 = r9;
        r45 = r13;
        r3 = r14;
        r111 = r22;
        r15 = r24;
        r8 = r26;
        r114 = r27;
        r89 = r32;
        r101 = r34;
        r17 = r35;
        r5 = r125;
        r6 = r123;
        goto L_0x0271;
    L_0x057b:
        r3 = new org.telegram.tgnet.TLRPC$TL_message;	 Catch:{ Exception -> 0x076f }
        r3.<init>();	 Catch:{ Exception -> 0x076f }
        goto L_0x055a;
    L_0x0581:
        r6 = new org.telegram.tgnet.TLRPC$TL_messageMediaDocument;	 Catch:{ Exception -> 0x0746 }
        r6.<init>();	 Catch:{ Exception -> 0x0746 }
        r3.media = r6;	 Catch:{ Exception -> 0x0746 }
        r6 = r3.media;	 Catch:{ Exception -> 0x0746 }
        r5 = r6.flags;	 Catch:{ Exception -> 0x0746 }
        r18 = 3;	 Catch:{ Exception -> 0x0746 }
        r5 = r5 | 3;	 Catch:{ Exception -> 0x0746 }
        r6.flags = r5;	 Catch:{ Exception -> 0x0746 }
        if (r10 == 0) goto L_0x05c2;
    L_0x0594:
        r5 = r3.media;	 Catch:{ Exception -> 0x05a5 }
        r5.ttl_seconds = r10;	 Catch:{ Exception -> 0x05a5 }
        r3.ttl = r10;	 Catch:{ Exception -> 0x05a5 }
        r5 = r3.media;	 Catch:{ Exception -> 0x05a5 }
        r6 = r5.flags;	 Catch:{ Exception -> 0x05a5 }
        r18 = 4;	 Catch:{ Exception -> 0x05a5 }
        r6 = r6 | 4;	 Catch:{ Exception -> 0x05a5 }
        r5.flags = r6;	 Catch:{ Exception -> 0x05a5 }
        goto L_0x05c2;
    L_0x05a5:
        r0 = move-exception;
        r4 = r127;
        r11 = r1;
        r118 = r2;
        r2 = r9;
        r45 = r13;
        r111 = r22;
        r15 = r24;
        r8 = r26;
        r114 = r27;
        r89 = r32;
        r101 = r34;
        r17 = r35;
        r5 = r125;
        r6 = r123;
        goto L_0x0249;
    L_0x05c2:
        r5 = r3.media;	 Catch:{ Exception -> 0x0746 }
        r5.document = r1;	 Catch:{ Exception -> 0x0746 }
        if (r12 == 0) goto L_0x05d3;
    L_0x05c8:
        r5 = "query_id";	 Catch:{ Exception -> 0x05a5 }
        r5 = r12.containsKey(r5);	 Catch:{ Exception -> 0x05a5 }
        if (r5 == 0) goto L_0x05d3;
    L_0x05d0:
        r5 = 9;
        goto L_0x05ee;
    L_0x05d3:
        r5 = org.telegram.messenger.MessageObject.isVideoDocument(r129);	 Catch:{ Exception -> 0x0746 }
        if (r5 != 0) goto L_0x05ed;
    L_0x05d9:
        r5 = org.telegram.messenger.MessageObject.isRoundVideoDocument(r129);	 Catch:{ Exception -> 0x05a5 }
        if (r5 != 0) goto L_0x05ed;	 Catch:{ Exception -> 0x05a5 }
    L_0x05df:
        if (r127 == 0) goto L_0x05e2;	 Catch:{ Exception -> 0x05a5 }
    L_0x05e1:
        goto L_0x05ed;	 Catch:{ Exception -> 0x05a5 }
    L_0x05e2:
        r5 = org.telegram.messenger.MessageObject.isVoiceDocument(r129);	 Catch:{ Exception -> 0x05a5 }
        if (r5 == 0) goto L_0x05eb;	 Catch:{ Exception -> 0x05a5 }
    L_0x05e8:
        r5 = 8;	 Catch:{ Exception -> 0x05a5 }
        goto L_0x05ee;	 Catch:{ Exception -> 0x05a5 }
    L_0x05eb:
        r5 = 7;	 Catch:{ Exception -> 0x05a5 }
        goto L_0x05ee;	 Catch:{ Exception -> 0x05a5 }
    L_0x05ed:
        r5 = 3;	 Catch:{ Exception -> 0x05a5 }
    L_0x05ee:
        r26 = r5;	 Catch:{ Exception -> 0x05a5 }
        if (r127 == 0) goto L_0x0603;	 Catch:{ Exception -> 0x05a5 }
    L_0x05f2:
        r5 = r127.getString();	 Catch:{ Exception -> 0x05a5 }
        if (r12 != 0) goto L_0x05fe;	 Catch:{ Exception -> 0x05a5 }
    L_0x05f8:
        r6 = new java.util.HashMap;	 Catch:{ Exception -> 0x05a5 }
        r6.<init>();	 Catch:{ Exception -> 0x05a5 }
        r12 = r6;	 Catch:{ Exception -> 0x05a5 }
    L_0x05fe:
        r6 = "ve";	 Catch:{ Exception -> 0x05a5 }
        r12.put(r6, r5);	 Catch:{ Exception -> 0x05a5 }
    L_0x0603:
        if (r9 == 0) goto L_0x061a;	 Catch:{ Exception -> 0x05a5 }
    L_0x0605:
        r5 = r1.dc_id;	 Catch:{ Exception -> 0x05a5 }
        if (r5 <= 0) goto L_0x061a;	 Catch:{ Exception -> 0x05a5 }
    L_0x0609:
        r5 = org.telegram.messenger.MessageObject.isStickerDocument(r129);	 Catch:{ Exception -> 0x05a5 }
        if (r5 != 0) goto L_0x061a;	 Catch:{ Exception -> 0x05a5 }
    L_0x060f:
        r5 = org.telegram.messenger.FileLoader.getPathToAttach(r129);	 Catch:{ Exception -> 0x05a5 }
        r5 = r5.toString();	 Catch:{ Exception -> 0x05a5 }
        r3.attachPath = r5;	 Catch:{ Exception -> 0x05a5 }
        goto L_0x061c;
    L_0x061a:
        r3.attachPath = r11;	 Catch:{ Exception -> 0x071a }
    L_0x061c:
        if (r9 == 0) goto L_0x0710;	 Catch:{ Exception -> 0x071a }
    L_0x061e:
        r5 = org.telegram.messenger.MessageObject.isStickerDocument(r129);	 Catch:{ Exception -> 0x071a }
        if (r5 == 0) goto L_0x0710;	 Catch:{ Exception -> 0x071a }
    L_0x0624:
        r5 = 0;	 Catch:{ Exception -> 0x071a }
    L_0x0625:
        r6 = r1.attributes;	 Catch:{ Exception -> 0x071a }
        r6 = r6.size();	 Catch:{ Exception -> 0x071a }
        if (r5 >= r6) goto L_0x0710;	 Catch:{ Exception -> 0x071a }
    L_0x062d:
        r6 = r1.attributes;	 Catch:{ Exception -> 0x071a }
        r6 = r6.get(r5);	 Catch:{ Exception -> 0x071a }
        r6 = (org.telegram.tgnet.TLRPC.DocumentAttribute) r6;	 Catch:{ Exception -> 0x071a }
        r43 = r3;
        r3 = r6 instanceof org.telegram.tgnet.TLRPC.TL_documentAttributeSticker;	 Catch:{ Exception -> 0x06e6 }
        if (r3 == 0) goto L_0x06d8;	 Catch:{ Exception -> 0x06e6 }
    L_0x063b:
        r3 = r1.attributes;	 Catch:{ Exception -> 0x06e6 }
        r3.remove(r5);	 Catch:{ Exception -> 0x06e6 }
        r3 = new org.telegram.tgnet.TLRPC$TL_documentAttributeSticker_layer55;	 Catch:{ Exception -> 0x06e6 }
        r3.<init>();	 Catch:{ Exception -> 0x06e6 }
        r8 = r1.attributes;	 Catch:{ Exception -> 0x06e6 }
        r8.add(r3);	 Catch:{ Exception -> 0x06e6 }
        r8 = r6.alt;	 Catch:{ Exception -> 0x06e6 }
        r3.alt = r8;	 Catch:{ Exception -> 0x06e6 }
        r8 = r6.stickerset;	 Catch:{ Exception -> 0x06e6 }
        if (r8 == 0) goto L_0x06b1;	 Catch:{ Exception -> 0x06e6 }
    L_0x0652:
        r8 = r6.stickerset;	 Catch:{ Exception -> 0x06e6 }
        r8 = r8 instanceof org.telegram.tgnet.TLRPC.TL_inputStickerSetShortName;	 Catch:{ Exception -> 0x06e6 }
        if (r8 == 0) goto L_0x0685;
    L_0x0658:
        r8 = r6.stickerset;	 Catch:{ Exception -> 0x065f }
        r8 = r8.short_name;	 Catch:{ Exception -> 0x065f }
        r44 = r12;
        goto L_0x0695;
    L_0x065f:
        r0 = move-exception;
        r4 = r127;
        r11 = r1;
        r118 = r2;
        r2 = r9;
        r45 = r13;
        r3 = r14;
        r111 = r22;
        r15 = r24;
        r8 = r26;
        r114 = r27;
        r89 = r32;
        r101 = r34;
        r17 = r35;
        r5 = r125;
        r6 = r123;
        r108 = r128;
        r113 = r126;
        r1 = r0;
        r13 = r12;
        r12 = r43;
        goto L_0x29e0;
    L_0x0685:
        r8 = r7.currentAccount;	 Catch:{ Exception -> 0x06e6 }
        r8 = org.telegram.messenger.DataQuery.getInstance(r8);	 Catch:{ Exception -> 0x06e6 }
        r1 = r6.stickerset;	 Catch:{ Exception -> 0x06e6 }
        r44 = r12;
        r12 = r1.id;	 Catch:{ Exception -> 0x06bc }
        r8 = r8.getStickerSetName(r12);	 Catch:{ Exception -> 0x06bc }
    L_0x0695:
        r1 = r8;	 Catch:{ Exception -> 0x06bc }
        r8 = android.text.TextUtils.isEmpty(r1);	 Catch:{ Exception -> 0x06bc }
        if (r8 != 0) goto L_0x06a8;	 Catch:{ Exception -> 0x06bc }
    L_0x069c:
        r8 = new org.telegram.tgnet.TLRPC$TL_inputStickerSetShortName;	 Catch:{ Exception -> 0x06bc }
        r8.<init>();	 Catch:{ Exception -> 0x06bc }
        r3.stickerset = r8;	 Catch:{ Exception -> 0x06bc }
        r8 = r3.stickerset;	 Catch:{ Exception -> 0x06bc }
        r8.short_name = r1;	 Catch:{ Exception -> 0x06bc }
        goto L_0x06af;	 Catch:{ Exception -> 0x06bc }
    L_0x06a8:
        r8 = new org.telegram.tgnet.TLRPC$TL_inputStickerSetEmpty;	 Catch:{ Exception -> 0x06bc }
        r8.<init>();	 Catch:{ Exception -> 0x06bc }
        r3.stickerset = r8;	 Catch:{ Exception -> 0x06bc }
    L_0x06af:
        goto L_0x0714;	 Catch:{ Exception -> 0x06bc }
    L_0x06b1:
        r44 = r12;	 Catch:{ Exception -> 0x06bc }
        r1 = new org.telegram.tgnet.TLRPC$TL_inputStickerSetEmpty;	 Catch:{ Exception -> 0x06bc }
        r1.<init>();	 Catch:{ Exception -> 0x06bc }
        r3.stickerset = r1;	 Catch:{ Exception -> 0x06bc }
        goto L_0x0714;
    L_0x06bc:
        r0 = move-exception;
        r4 = r127;
        r1 = r0;
        r118 = r2;
        r2 = r9;
        r3 = r14;
        r111 = r22;
        r15 = r24;
        r8 = r26;
        r114 = r27;
        r89 = r32;
        r101 = r34;
        r17 = r35;
        r12 = r43;
        r13 = r44;
        goto L_0x0788;
    L_0x06d8:
        r44 = r12;
        r5 = r5 + 1;
        r3 = r43;
        r1 = r129;
        r8 = r130;
        r13 = r135;
        goto L_0x0625;
    L_0x06e6:
        r0 = move-exception;
        r44 = r12;
        r4 = r127;
        r1 = r0;
        r118 = r2;
        r2 = r9;
        r3 = r14;
        r111 = r22;
        r15 = r24;
        r8 = r26;
        r114 = r27;
        r89 = r32;
        r101 = r34;
        r17 = r35;
        r12 = r43;
        r13 = r44;
        r5 = r125;
        r6 = r123;
        r11 = r129;
        r45 = r135;
        r108 = r128;
        r113 = r126;
        goto L_0x29e0;
    L_0x0710:
        r43 = r3;
        r44 = r12;
    L_0x0714:
        r1 = r43;
        r12 = r44;
        goto L_0x0798;
    L_0x071a:
        r0 = move-exception;
        r43 = r3;
        r44 = r12;
        r4 = r127;
        r1 = r0;
        r118 = r2;
        r2 = r9;
        r3 = r14;
        r111 = r22;
        r15 = r24;
        r8 = r26;
        r114 = r27;
        r89 = r32;
        r101 = r34;
        r17 = r35;
        r12 = r43;
        r13 = r44;
        r5 = r125;
        r6 = r123;
        r11 = r129;
        r45 = r135;
        r108 = r128;
        r113 = r126;
        goto L_0x29e0;
    L_0x0746:
        r0 = move-exception;
        r43 = r3;
        r4 = r127;
        r1 = r0;
        r118 = r2;
        r2 = r9;
        r13 = r12;
        r3 = r14;
        r111 = r22;
        r15 = r24;
        r8 = r26;
        r114 = r27;
        r89 = r32;
        r101 = r34;
        r17 = r35;
        r12 = r43;
        r5 = r125;
        r6 = r123;
        r11 = r129;
        r45 = r135;
        r108 = r128;
        r113 = r126;
        goto L_0x29e0;
    L_0x076f:
        r0 = move-exception;
        r4 = r127;
        r1 = r0;
        r118 = r2;
        r2 = r9;
        r13 = r12;
        r3 = r14;
        r12 = r21;
        r111 = r22;
        r15 = r24;
        r8 = r26;
        r114 = r27;
        r89 = r32;
        r101 = r34;
        r17 = r35;
    L_0x0788:
        r5 = r125;
        r6 = r123;
        r11 = r129;
        r45 = r135;
        r108 = r128;
        r113 = r126;
        goto L_0x29e0;
    L_0x0796:
        r1 = r21;
    L_0x0798:
        r13 = r135;
    L_0x079a:
        if (r15 == 0) goto L_0x07aa;
    L_0x079c:
        r3 = r138.isEmpty();	 Catch:{ Exception -> 0x0488 }
        if (r3 != 0) goto L_0x07aa;	 Catch:{ Exception -> 0x0488 }
    L_0x07a2:
        r1.entities = r15;	 Catch:{ Exception -> 0x0488 }
        r3 = r1.flags;	 Catch:{ Exception -> 0x0488 }
        r3 = r3 | 128;	 Catch:{ Exception -> 0x0488 }
        r1.flags = r3;	 Catch:{ Exception -> 0x0488 }
    L_0x07aa:
        r8 = r22;
        if (r8 == 0) goto L_0x07be;
    L_0x07ae:
        r1.message = r8;	 Catch:{ Exception -> 0x07b1 }
        goto L_0x07c6;
    L_0x07b1:
        r0 = move-exception;
        r4 = r127;
        r118 = r2;
        r111 = r8;
        r2 = r9;
        r45 = r13;
        r3 = r14;
        goto L_0x0493;
    L_0x07be:
        r3 = r1.message;	 Catch:{ Exception -> 0x29bc }
        if (r3 != 0) goto L_0x07c6;
    L_0x07c2:
        r3 = "";	 Catch:{ Exception -> 0x07b1 }
        r1.message = r3;	 Catch:{ Exception -> 0x07b1 }
    L_0x07c6:
        r3 = r1.attachPath;	 Catch:{ Exception -> 0x29bc }
        if (r3 != 0) goto L_0x07ce;
    L_0x07ca:
        r3 = "";	 Catch:{ Exception -> 0x07b1 }
        r1.attachPath = r3;	 Catch:{ Exception -> 0x07b1 }
    L_0x07ce:
        r3 = r7.currentAccount;	 Catch:{ Exception -> 0x29bc }
        r3 = org.telegram.messenger.UserConfig.getInstance(r3);	 Catch:{ Exception -> 0x29bc }
        r3 = r3.getNewMessageId();	 Catch:{ Exception -> 0x29bc }
        r1.id = r3;	 Catch:{ Exception -> 0x29bc }
        r1.local_id = r3;	 Catch:{ Exception -> 0x29bc }
        r3 = 1;	 Catch:{ Exception -> 0x29bc }
        r1.out = r3;	 Catch:{ Exception -> 0x29bc }
        if (r31 == 0) goto L_0x07e9;
    L_0x07e1:
        if (r2 == 0) goto L_0x07e9;
    L_0x07e3:
        r3 = r2.channel_id;	 Catch:{ Exception -> 0x07b1 }
        r3 = -r3;	 Catch:{ Exception -> 0x07b1 }
        r1.from_id = r3;	 Catch:{ Exception -> 0x07b1 }
        goto L_0x07fb;
    L_0x07e9:
        r3 = r7.currentAccount;	 Catch:{ Exception -> 0x29bc }
        r3 = org.telegram.messenger.UserConfig.getInstance(r3);	 Catch:{ Exception -> 0x29bc }
        r3 = r3.getClientUserId();	 Catch:{ Exception -> 0x29bc }
        r1.from_id = r3;	 Catch:{ Exception -> 0x29bc }
        r3 = r1.flags;	 Catch:{ Exception -> 0x29bc }
        r3 = r3 | 256;	 Catch:{ Exception -> 0x29bc }
        r1.flags = r3;	 Catch:{ Exception -> 0x29bc }
    L_0x07fb:
        r3 = r7.currentAccount;	 Catch:{ Exception -> 0x29bc }
        r3 = org.telegram.messenger.UserConfig.getInstance(r3);	 Catch:{ Exception -> 0x29bc }
        r5 = 0;	 Catch:{ Exception -> 0x29bc }
        r3.saveConfig(r5);	 Catch:{ Exception -> 0x29bc }
        r46 = r8;
        r45 = r13;
        r8 = r26;
        r3 = r126;
        r5 = r125;
        r6 = r123;
        r15 = r129;
        r13 = r12;
        r12 = r1;
        r1 = r128;
    L_0x0817:
        r48 = r5;
        r47 = r6;
        r5 = r12.random_id;	 Catch:{ Exception -> 0x298c }
        r18 = (r5 > r19 ? 1 : (r5 == r19 ? 0 : -1));
        if (r18 != 0) goto L_0x0847;
    L_0x0821:
        r5 = r122.getNextRandomId();	 Catch:{ Exception -> 0x0828 }
        r12.random_id = r5;	 Catch:{ Exception -> 0x0828 }
        goto L_0x0847;	 Catch:{ Exception -> 0x0828 }
    L_0x0828:
        r0 = move-exception;	 Catch:{ Exception -> 0x0828 }
        r4 = r127;	 Catch:{ Exception -> 0x0828 }
        r108 = r1;	 Catch:{ Exception -> 0x0828 }
        r118 = r2;	 Catch:{ Exception -> 0x0828 }
        r113 = r3;	 Catch:{ Exception -> 0x0828 }
        r2 = r9;	 Catch:{ Exception -> 0x0828 }
        r3 = r14;	 Catch:{ Exception -> 0x0828 }
        r11 = r15;	 Catch:{ Exception -> 0x0828 }
        r15 = r24;	 Catch:{ Exception -> 0x0828 }
        r114 = r27;	 Catch:{ Exception -> 0x0828 }
        r89 = r32;	 Catch:{ Exception -> 0x0828 }
        r101 = r34;	 Catch:{ Exception -> 0x0828 }
        r17 = r35;	 Catch:{ Exception -> 0x0828 }
        r111 = r46;	 Catch:{ Exception -> 0x0828 }
        r6 = r47;	 Catch:{ Exception -> 0x0828 }
        r5 = r48;	 Catch:{ Exception -> 0x0828 }
    L_0x0844:
        r1 = r0;	 Catch:{ Exception -> 0x0828 }
        goto L_0x29e0;	 Catch:{ Exception -> 0x0828 }
    L_0x0847:
        if (r13 == 0) goto L_0x087e;	 Catch:{ Exception -> 0x0828 }
    L_0x0849:
        r5 = "bot";	 Catch:{ Exception -> 0x0828 }
        r5 = r13.containsKey(r5);	 Catch:{ Exception -> 0x0828 }
        if (r5 == 0) goto L_0x087e;	 Catch:{ Exception -> 0x0828 }
    L_0x0851:
        if (r9 == 0) goto L_0x0866;	 Catch:{ Exception -> 0x0828 }
    L_0x0853:
        r5 = "bot_name";	 Catch:{ Exception -> 0x0828 }
        r5 = r13.get(r5);	 Catch:{ Exception -> 0x0828 }
        r5 = (java.lang.String) r5;	 Catch:{ Exception -> 0x0828 }
        r12.via_bot_name = r5;	 Catch:{ Exception -> 0x0828 }
        r5 = r12.via_bot_name;	 Catch:{ Exception -> 0x0828 }
        if (r5 != 0) goto L_0x0878;	 Catch:{ Exception -> 0x0828 }
    L_0x0861:
        r5 = "";	 Catch:{ Exception -> 0x0828 }
        r12.via_bot_name = r5;	 Catch:{ Exception -> 0x0828 }
        goto L_0x0878;	 Catch:{ Exception -> 0x0828 }
    L_0x0866:
        r5 = "bot";	 Catch:{ Exception -> 0x0828 }
        r5 = r13.get(r5);	 Catch:{ Exception -> 0x0828 }
        r5 = (java.lang.String) r5;	 Catch:{ Exception -> 0x0828 }
        r5 = org.telegram.messenger.Utilities.parseInt(r5);	 Catch:{ Exception -> 0x0828 }
        r5 = r5.intValue();	 Catch:{ Exception -> 0x0828 }
        r12.via_bot_id = r5;	 Catch:{ Exception -> 0x0828 }
    L_0x0878:
        r5 = r12.flags;	 Catch:{ Exception -> 0x0828 }
        r5 = r5 | 2048;	 Catch:{ Exception -> 0x0828 }
        r12.flags = r5;	 Catch:{ Exception -> 0x0828 }
    L_0x087e:
        r12.params = r13;	 Catch:{ Exception -> 0x298c }
        if (r14 == 0) goto L_0x0886;
    L_0x0882:
        r5 = r14.resendAsIs;	 Catch:{ Exception -> 0x0828 }
        if (r5 != 0) goto L_0x08da;
    L_0x0886:
        r5 = r7.currentAccount;	 Catch:{ Exception -> 0x298c }
        r5 = org.telegram.tgnet.ConnectionsManager.getInstance(r5);	 Catch:{ Exception -> 0x298c }
        r5 = r5.getCurrentTime();	 Catch:{ Exception -> 0x298c }
        r12.date = r5;	 Catch:{ Exception -> 0x298c }
        r5 = r2 instanceof org.telegram.tgnet.TLRPC.TL_inputPeerChannel;	 Catch:{ Exception -> 0x298c }
        if (r5 == 0) goto L_0x08d7;
    L_0x0896:
        if (r31 == 0) goto L_0x08a1;
    L_0x0898:
        r5 = 1;
        r12.views = r5;	 Catch:{ Exception -> 0x0828 }
        r5 = r12.flags;	 Catch:{ Exception -> 0x0828 }
        r5 = r5 | 1024;	 Catch:{ Exception -> 0x0828 }
        r12.flags = r5;	 Catch:{ Exception -> 0x0828 }
    L_0x08a1:
        r5 = r7.currentAccount;	 Catch:{ Exception -> 0x0828 }
        r5 = org.telegram.messenger.MessagesController.getInstance(r5);	 Catch:{ Exception -> 0x0828 }
        r6 = r2.channel_id;	 Catch:{ Exception -> 0x0828 }
        r6 = java.lang.Integer.valueOf(r6);	 Catch:{ Exception -> 0x0828 }
        r5 = r5.getChat(r6);	 Catch:{ Exception -> 0x0828 }
        if (r5 == 0) goto L_0x08d6;	 Catch:{ Exception -> 0x0828 }
    L_0x08b3:
        r6 = r5.megagroup;	 Catch:{ Exception -> 0x0828 }
        if (r6 == 0) goto L_0x08c3;	 Catch:{ Exception -> 0x0828 }
    L_0x08b7:
        r6 = r12.flags;	 Catch:{ Exception -> 0x0828 }
        r18 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;	 Catch:{ Exception -> 0x0828 }
        r6 = r6 | r18;	 Catch:{ Exception -> 0x0828 }
        r12.flags = r6;	 Catch:{ Exception -> 0x0828 }
        r6 = 1;	 Catch:{ Exception -> 0x0828 }
        r12.unread = r6;	 Catch:{ Exception -> 0x0828 }
        goto L_0x08d6;	 Catch:{ Exception -> 0x0828 }
    L_0x08c3:
        r6 = 1;	 Catch:{ Exception -> 0x0828 }
        r12.post = r6;	 Catch:{ Exception -> 0x0828 }
        r6 = r5.signatures;	 Catch:{ Exception -> 0x0828 }
        if (r6 == 0) goto L_0x08d6;	 Catch:{ Exception -> 0x0828 }
    L_0x08ca:
        r6 = r7.currentAccount;	 Catch:{ Exception -> 0x0828 }
        r6 = org.telegram.messenger.UserConfig.getInstance(r6);	 Catch:{ Exception -> 0x0828 }
        r6 = r6.getClientUserId();	 Catch:{ Exception -> 0x0828 }
        r12.from_id = r6;	 Catch:{ Exception -> 0x0828 }
    L_0x08d6:
        goto L_0x08da;
    L_0x08d7:
        r5 = 1;
        r12.unread = r5;	 Catch:{ Exception -> 0x298c }
    L_0x08da:
        r5 = r12.flags;	 Catch:{ Exception -> 0x298c }
        r5 = r5 | 512;	 Catch:{ Exception -> 0x298c }
        r12.flags = r5;	 Catch:{ Exception -> 0x298c }
        r5 = r131;
        r14 = 2;
        r12.dialog_id = r5;	 Catch:{ Exception -> 0x296b }
        r14 = r134;
        if (r14 == 0) goto L_0x0959;
    L_0x08e9:
        if (r9 == 0) goto L_0x0927;
    L_0x08eb:
        r11 = r14.messageOwner;	 Catch:{ Exception -> 0x0905 }
        r50 = r1;
        r49 = r2;
        r1 = r11.random_id;	 Catch:{ Exception -> 0x0939 }
        r11 = (r1 > r19 ? 1 : (r1 == r19 ? 0 : -1));	 Catch:{ Exception -> 0x0939 }
        if (r11 == 0) goto L_0x092b;	 Catch:{ Exception -> 0x0939 }
    L_0x08f7:
        r1 = r14.messageOwner;	 Catch:{ Exception -> 0x0939 }
        r1 = r1.random_id;	 Catch:{ Exception -> 0x0939 }
        r12.reply_to_random_id = r1;	 Catch:{ Exception -> 0x0939 }
        r1 = r12.flags;	 Catch:{ Exception -> 0x0939 }
        r2 = 8;	 Catch:{ Exception -> 0x0939 }
        r1 = r1 | r2;	 Catch:{ Exception -> 0x0939 }
        r12.flags = r1;	 Catch:{ Exception -> 0x0939 }
        goto L_0x0932;	 Catch:{ Exception -> 0x0939 }
    L_0x0905:
        r0 = move-exception;	 Catch:{ Exception -> 0x0939 }
        r50 = r1;	 Catch:{ Exception -> 0x0939 }
        r4 = r127;	 Catch:{ Exception -> 0x0939 }
        r1 = r0;	 Catch:{ Exception -> 0x0939 }
        r118 = r2;	 Catch:{ Exception -> 0x0939 }
        r113 = r3;	 Catch:{ Exception -> 0x0939 }
        r2 = r9;	 Catch:{ Exception -> 0x0939 }
        r11 = r15;	 Catch:{ Exception -> 0x0939 }
        r15 = r24;	 Catch:{ Exception -> 0x0939 }
        r114 = r27;	 Catch:{ Exception -> 0x0939 }
        r89 = r32;	 Catch:{ Exception -> 0x0939 }
        r101 = r34;	 Catch:{ Exception -> 0x0939 }
        r17 = r35;	 Catch:{ Exception -> 0x0939 }
        r111 = r46;	 Catch:{ Exception -> 0x0939 }
        r6 = r47;	 Catch:{ Exception -> 0x0939 }
        r5 = r48;	 Catch:{ Exception -> 0x0939 }
        r108 = r50;	 Catch:{ Exception -> 0x0939 }
        r3 = r137;	 Catch:{ Exception -> 0x0939 }
        goto L_0x29e0;	 Catch:{ Exception -> 0x0939 }
    L_0x0927:
        r50 = r1;	 Catch:{ Exception -> 0x0939 }
        r49 = r2;	 Catch:{ Exception -> 0x0939 }
    L_0x092b:
        r1 = r12.flags;	 Catch:{ Exception -> 0x0939 }
        r2 = 8;	 Catch:{ Exception -> 0x0939 }
        r1 = r1 | r2;	 Catch:{ Exception -> 0x0939 }
        r12.flags = r1;	 Catch:{ Exception -> 0x0939 }
    L_0x0932:
        r1 = r134.getId();	 Catch:{ Exception -> 0x0939 }
        r12.reply_to_msg_id = r1;	 Catch:{ Exception -> 0x0939 }
        goto L_0x095d;	 Catch:{ Exception -> 0x0939 }
    L_0x0939:
        r0 = move-exception;	 Catch:{ Exception -> 0x0939 }
        r4 = r127;	 Catch:{ Exception -> 0x0939 }
        r1 = r0;	 Catch:{ Exception -> 0x0939 }
        r113 = r3;	 Catch:{ Exception -> 0x0939 }
        r2 = r9;	 Catch:{ Exception -> 0x0939 }
        r11 = r15;	 Catch:{ Exception -> 0x0939 }
        r15 = r24;	 Catch:{ Exception -> 0x0939 }
        r114 = r27;	 Catch:{ Exception -> 0x0939 }
        r89 = r32;	 Catch:{ Exception -> 0x0939 }
        r101 = r34;	 Catch:{ Exception -> 0x0939 }
        r17 = r35;	 Catch:{ Exception -> 0x0939 }
        r111 = r46;	 Catch:{ Exception -> 0x0939 }
        r6 = r47;	 Catch:{ Exception -> 0x0939 }
        r5 = r48;	 Catch:{ Exception -> 0x0939 }
        r118 = r49;	 Catch:{ Exception -> 0x0939 }
        r108 = r50;	 Catch:{ Exception -> 0x0939 }
        r3 = r137;	 Catch:{ Exception -> 0x0939 }
        goto L_0x29e0;	 Catch:{ Exception -> 0x0939 }
    L_0x0959:
        r50 = r1;	 Catch:{ Exception -> 0x0939 }
        r49 = r2;	 Catch:{ Exception -> 0x0939 }
    L_0x095d:
        r11 = r139;	 Catch:{ Exception -> 0x0939 }
        if (r11 == 0) goto L_0x096b;	 Catch:{ Exception -> 0x0939 }
    L_0x0961:
        if (r9 != 0) goto L_0x096b;	 Catch:{ Exception -> 0x0939 }
    L_0x0963:
        r1 = r12.flags;	 Catch:{ Exception -> 0x0939 }
        r1 = r1 | 64;	 Catch:{ Exception -> 0x0939 }
        r12.flags = r1;	 Catch:{ Exception -> 0x0939 }
        r12.reply_markup = r11;	 Catch:{ Exception -> 0x0939 }
    L_0x096b:
        if (r35 == 0) goto L_0x0b12;
    L_0x096d:
        r2 = r34;
        r1 = 1;
        if (r2 != r1) goto L_0x0ac0;
    L_0x0972:
        r1 = r7.currentChatInfo;	 Catch:{ Exception -> 0x0a9b }
        if (r1 != 0) goto L_0x09d7;
    L_0x0976:
        r1 = r7.currentAccount;	 Catch:{ Exception -> 0x09b2 }
        r1 = org.telegram.messenger.MessagesStorage.getInstance(r1);	 Catch:{ Exception -> 0x09b2 }
        r1.markMessageAsSendError(r12);	 Catch:{ Exception -> 0x09b2 }
        r1 = r7.currentAccount;	 Catch:{ Exception -> 0x09b2 }
        r1 = org.telegram.messenger.NotificationCenter.getInstance(r1);	 Catch:{ Exception -> 0x09b2 }
        r11 = org.telegram.messenger.NotificationCenter.messageSendError;	 Catch:{ Exception -> 0x09b2 }
        r52 = r3;
        r51 = r15;
        r15 = 1;
        r3 = new java.lang.Object[r15];	 Catch:{ Exception -> 0x09a1 }
        r15 = r12.id;	 Catch:{ Exception -> 0x09a1 }
        r15 = java.lang.Integer.valueOf(r15);	 Catch:{ Exception -> 0x09a1 }
        r16 = 0;	 Catch:{ Exception -> 0x09a1 }
        r3[r16] = r15;	 Catch:{ Exception -> 0x09a1 }
        r1.postNotificationName(r11, r3);	 Catch:{ Exception -> 0x09a1 }
        r1 = r12.id;	 Catch:{ Exception -> 0x09a1 }
        r7.processSentMessage(r1);	 Catch:{ Exception -> 0x09a1 }
        return;
    L_0x09a1:
        r0 = move-exception;
        r4 = r127;
        r1 = r0;
        r101 = r2;
        r2 = r9;
        r15 = r24;
        r114 = r27;
        r89 = r32;
        r17 = r35;
        goto L_0x0b02;
    L_0x09b2:
        r0 = move-exception;
        r52 = r3;
        r51 = r15;
        r4 = r127;
        r1 = r0;
        r101 = r2;
        r2 = r9;
        r15 = r24;
        r114 = r27;
        r89 = r32;
        r17 = r35;
        r111 = r46;
        r6 = r47;
        r5 = r48;
        r118 = r49;
        r108 = r50;
        r11 = r51;
        r113 = r52;
        r3 = r137;
        goto L_0x29e0;
    L_0x09d7:
        r52 = r3;
        r51 = r15;
        r1 = new java.util.ArrayList;	 Catch:{ Exception -> 0x0a7a }
        r1.<init>();	 Catch:{ Exception -> 0x0a7a }
        r3 = r7.currentChatInfo;	 Catch:{ Exception -> 0x0a5a }
        r3 = r3.participants;	 Catch:{ Exception -> 0x0a5a }
        r3 = r3.participants;	 Catch:{ Exception -> 0x0a5a }
        r3 = r3.iterator();	 Catch:{ Exception -> 0x0a5a }
        r11 = r3.hasNext();	 Catch:{ Exception -> 0x0a5a }
        if (r11 == 0) goto L_0x0a3b;
    L_0x09f0:
        r11 = r3.next();	 Catch:{ Exception -> 0x0a1b }
        r11 = (org.telegram.tgnet.TLRPC.ChatParticipant) r11;	 Catch:{ Exception -> 0x0a1b }
        r15 = r7.currentAccount;	 Catch:{ Exception -> 0x0a1b }
        r15 = org.telegram.messenger.MessagesController.getInstance(r15);	 Catch:{ Exception -> 0x0a1b }
        r53 = r3;	 Catch:{ Exception -> 0x0a1b }
        r3 = r11.user_id;	 Catch:{ Exception -> 0x0a1b }
        r3 = java.lang.Integer.valueOf(r3);	 Catch:{ Exception -> 0x0a1b }
        r3 = r15.getUser(r3);	 Catch:{ Exception -> 0x0a1b }
        r15 = r7.currentAccount;	 Catch:{ Exception -> 0x0a1b }
        r15 = org.telegram.messenger.MessagesController.getInstance(r15);	 Catch:{ Exception -> 0x0a1b }
        r15 = r15.getInputUser(r3);	 Catch:{ Exception -> 0x0a1b }
        if (r15 == 0) goto L_0x0a17;	 Catch:{ Exception -> 0x0a1b }
    L_0x0a14:
        r1.add(r15);	 Catch:{ Exception -> 0x0a1b }
        r3 = r53;
        goto L_0x09ea;
    L_0x0a1b:
        r0 = move-exception;
        r4 = r127;
        r89 = r1;
        r101 = r2;
        r2 = r9;
        r15 = r24;
        r114 = r27;
        r17 = r35;
        r111 = r46;
        r6 = r47;
        r5 = r48;
        r118 = r49;
        r108 = r50;
        r11 = r51;
        r113 = r52;
        r3 = r137;
        goto L_0x0844;
    L_0x0a3b:
        r3 = new org.telegram.tgnet.TLRPC$TL_peerChat;	 Catch:{ Exception -> 0x0a5a }
        r3.<init>();	 Catch:{ Exception -> 0x0a5a }
        r12.to_id = r3;	 Catch:{ Exception -> 0x0a5a }
        r3 = r12.to_id;	 Catch:{ Exception -> 0x0a5a }
        r11 = r35;
        r3.chat_id = r11;	 Catch:{ Exception -> 0x0a4b }
        r15 = r1;
        goto L_0x0be8;
    L_0x0a4b:
        r0 = move-exception;
        r4 = r127;
        r89 = r1;
        r101 = r2;
        r2 = r9;
        r17 = r11;
        r15 = r24;
        r114 = r27;
        goto L_0x0a29;
    L_0x0a5a:
        r0 = move-exception;
        r4 = r127;
        r89 = r1;
        r101 = r2;
        r2 = r9;
        r15 = r24;
        r114 = r27;
        r17 = r35;
        r111 = r46;
        r6 = r47;
        r5 = r48;
        r118 = r49;
        r108 = r50;
        r11 = r51;
        r113 = r52;
        r3 = r137;
        r1 = r0;
        goto L_0x0a99;
    L_0x0a7a:
        r0 = move-exception;
        r4 = r127;
        r1 = r0;
        r101 = r2;
        r2 = r9;
        r15 = r24;
        r114 = r27;
        r89 = r32;
        r17 = r35;
        r111 = r46;
        r6 = r47;
        r5 = r48;
        r118 = r49;
        r108 = r50;
        r11 = r51;
        r113 = r52;
        r3 = r137;
        goto L_0x29e0;
    L_0x0a9b:
        r0 = move-exception;
        r52 = r3;
        r51 = r15;
        r4 = r127;
        r1 = r0;
        r101 = r2;
        r2 = r9;
        r15 = r24;
        r114 = r27;
        r89 = r32;
        r17 = r35;
        r111 = r46;
        r6 = r47;
        r5 = r48;
        r118 = r49;
        r108 = r50;
        r11 = r51;
        r113 = r52;
        r3 = r137;
        goto L_0x29e0;
    L_0x0ac0:
        r52 = r3;
        r51 = r15;
        r11 = r35;
        r1 = r7.currentAccount;	 Catch:{ Exception -> 0x0af3 }
        r1 = org.telegram.messenger.MessagesController.getInstance(r1);	 Catch:{ Exception -> 0x0af3 }
        r1 = r1.getPeer(r11);	 Catch:{ Exception -> 0x0af3 }
        r12.to_id = r1;	 Catch:{ Exception -> 0x0af3 }
        if (r11 <= 0) goto L_0x0be6;	 Catch:{ Exception -> 0x0af3 }
        r1 = r7.currentAccount;	 Catch:{ Exception -> 0x0af3 }
        r1 = org.telegram.messenger.MessagesController.getInstance(r1);	 Catch:{ Exception -> 0x0af3 }
        r3 = java.lang.Integer.valueOf(r11);	 Catch:{ Exception -> 0x0af3 }
        r1 = r1.getUser(r3);	 Catch:{ Exception -> 0x0af3 }
        if (r1 != 0) goto L_0x0aea;	 Catch:{ Exception -> 0x0af3 }
        r3 = r12.id;	 Catch:{ Exception -> 0x0af3 }
        r7.processSentMessage(r3);	 Catch:{ Exception -> 0x0af3 }
        return;	 Catch:{ Exception -> 0x0af3 }
        r3 = r1.bot;	 Catch:{ Exception -> 0x0af3 }
        if (r3 == 0) goto L_0x0af1;	 Catch:{ Exception -> 0x0af3 }
        r3 = 0;	 Catch:{ Exception -> 0x0af3 }
        r12.unread = r3;	 Catch:{ Exception -> 0x0af3 }
        goto L_0x0be6;
    L_0x0af3:
        r0 = move-exception;
        r4 = r127;
        r1 = r0;
        r101 = r2;
        r2 = r9;
        r17 = r11;
        r15 = r24;
        r114 = r27;
        r89 = r32;
    L_0x0b02:
        r111 = r46;
        r6 = r47;
        r5 = r48;
        r118 = r49;
        r108 = r50;
        r11 = r51;
        r113 = r52;
        goto L_0x0955;
    L_0x0b12:
        r52 = r3;
        r51 = r15;
        r2 = r34;
        r11 = r35;
        r1 = new org.telegram.tgnet.TLRPC$TL_peerUser;	 Catch:{ Exception -> 0x293a }
        r1.<init>();	 Catch:{ Exception -> 0x293a }
        r12.to_id = r1;	 Catch:{ Exception -> 0x293a }
        r1 = r9.participant_id;	 Catch:{ Exception -> 0x293a }
        r3 = r7.currentAccount;	 Catch:{ Exception -> 0x293a }
        r3 = org.telegram.messenger.UserConfig.getInstance(r3);	 Catch:{ Exception -> 0x293a }
        r3 = r3.getClientUserId();	 Catch:{ Exception -> 0x293a }
        if (r1 != r3) goto L_0x0b36;
        r1 = r12.to_id;	 Catch:{ Exception -> 0x0af3 }
        r3 = r9.admin_id;	 Catch:{ Exception -> 0x0af3 }
        r1.user_id = r3;	 Catch:{ Exception -> 0x0af3 }
        goto L_0x0b3c;
        r1 = r12.to_id;	 Catch:{ Exception -> 0x293a }
        r3 = r9.participant_id;	 Catch:{ Exception -> 0x293a }
        r1.user_id = r3;	 Catch:{ Exception -> 0x293a }
        if (r10 == 0) goto L_0x0b41;
        r12.ttl = r10;	 Catch:{ Exception -> 0x0af3 }
        goto L_0x0b5b;
        r1 = r9.ttl;	 Catch:{ Exception -> 0x293a }
        r12.ttl = r1;	 Catch:{ Exception -> 0x293a }
        r1 = r12.ttl;	 Catch:{ Exception -> 0x293a }
        if (r1 == 0) goto L_0x0b5b;
        r1 = r12.media;	 Catch:{ Exception -> 0x0af3 }
        if (r1 == 0) goto L_0x0b5b;	 Catch:{ Exception -> 0x0af3 }
        r1 = r12.media;	 Catch:{ Exception -> 0x0af3 }
        r3 = r12.ttl;	 Catch:{ Exception -> 0x0af3 }
        r1.ttl_seconds = r3;	 Catch:{ Exception -> 0x0af3 }
        r1 = r12.media;	 Catch:{ Exception -> 0x0af3 }
        r3 = r1.flags;	 Catch:{ Exception -> 0x0af3 }
        r15 = 4;	 Catch:{ Exception -> 0x0af3 }
        r3 = r3 | r15;	 Catch:{ Exception -> 0x0af3 }
        r1.flags = r3;	 Catch:{ Exception -> 0x0af3 }
        r1 = r12.ttl;	 Catch:{ Exception -> 0x293a }
        if (r1 == 0) goto L_0x0be6;
        r1 = r12.media;	 Catch:{ Exception -> 0x0af3 }
        r1 = r1.document;	 Catch:{ Exception -> 0x0af3 }
        if (r1 == 0) goto L_0x0be6;	 Catch:{ Exception -> 0x0af3 }
        r1 = org.telegram.messenger.MessageObject.isVoiceMessage(r12);	 Catch:{ Exception -> 0x0af3 }
        if (r1 == 0) goto L_0x0ba3;	 Catch:{ Exception -> 0x0af3 }
        r1 = 0;	 Catch:{ Exception -> 0x0af3 }
        r3 = 0;	 Catch:{ Exception -> 0x0af3 }
        r15 = r12.media;	 Catch:{ Exception -> 0x0af3 }
        r15 = r15.document;	 Catch:{ Exception -> 0x0af3 }
        r15 = r15.attributes;	 Catch:{ Exception -> 0x0af3 }
        r15 = r15.size();	 Catch:{ Exception -> 0x0af3 }
        if (r3 >= r15) goto L_0x0b96;	 Catch:{ Exception -> 0x0af3 }
        r15 = r12.media;	 Catch:{ Exception -> 0x0af3 }
        r15 = r15.document;	 Catch:{ Exception -> 0x0af3 }
        r15 = r15.attributes;	 Catch:{ Exception -> 0x0af3 }
        r15 = r15.get(r3);	 Catch:{ Exception -> 0x0af3 }
        r15 = (org.telegram.tgnet.TLRPC.DocumentAttribute) r15;	 Catch:{ Exception -> 0x0af3 }
        r54 = r1;	 Catch:{ Exception -> 0x0af3 }
        r1 = r15 instanceof org.telegram.tgnet.TLRPC.TL_documentAttributeAudio;	 Catch:{ Exception -> 0x0af3 }
        if (r1 == 0) goto L_0x0b91;	 Catch:{ Exception -> 0x0af3 }
        r1 = r15.duration;	 Catch:{ Exception -> 0x0af3 }
        r54 = r1;	 Catch:{ Exception -> 0x0af3 }
        goto L_0x0b98;	 Catch:{ Exception -> 0x0af3 }
        r3 = r3 + 1;	 Catch:{ Exception -> 0x0af3 }
        r1 = r54;	 Catch:{ Exception -> 0x0af3 }
        goto L_0x0b6d;	 Catch:{ Exception -> 0x0af3 }
        r54 = r1;	 Catch:{ Exception -> 0x0af3 }
        r1 = r12.ttl;	 Catch:{ Exception -> 0x0af3 }
        r3 = r54 + 1;	 Catch:{ Exception -> 0x0af3 }
        r1 = java.lang.Math.max(r1, r3);	 Catch:{ Exception -> 0x0af3 }
        r12.ttl = r1;	 Catch:{ Exception -> 0x0af3 }
        goto L_0x0be6;	 Catch:{ Exception -> 0x0af3 }
        r1 = org.telegram.messenger.MessageObject.isVideoMessage(r12);	 Catch:{ Exception -> 0x0af3 }
        if (r1 != 0) goto L_0x0baf;	 Catch:{ Exception -> 0x0af3 }
        r1 = org.telegram.messenger.MessageObject.isRoundVideoMessage(r12);	 Catch:{ Exception -> 0x0af3 }
        if (r1 == 0) goto L_0x0be6;	 Catch:{ Exception -> 0x0af3 }
        r1 = 0;	 Catch:{ Exception -> 0x0af3 }
        r3 = 0;	 Catch:{ Exception -> 0x0af3 }
        r15 = r12.media;	 Catch:{ Exception -> 0x0af3 }
        r15 = r15.document;	 Catch:{ Exception -> 0x0af3 }
        r15 = r15.attributes;	 Catch:{ Exception -> 0x0af3 }
        r15 = r15.size();	 Catch:{ Exception -> 0x0af3 }
        if (r3 >= r15) goto L_0x0bda;	 Catch:{ Exception -> 0x0af3 }
        r15 = r12.media;	 Catch:{ Exception -> 0x0af3 }
        r15 = r15.document;	 Catch:{ Exception -> 0x0af3 }
        r15 = r15.attributes;	 Catch:{ Exception -> 0x0af3 }
        r15 = r15.get(r3);	 Catch:{ Exception -> 0x0af3 }
        r15 = (org.telegram.tgnet.TLRPC.DocumentAttribute) r15;	 Catch:{ Exception -> 0x0af3 }
        r55 = r1;	 Catch:{ Exception -> 0x0af3 }
        r1 = r15 instanceof org.telegram.tgnet.TLRPC.TL_documentAttributeVideo;	 Catch:{ Exception -> 0x0af3 }
        if (r1 == 0) goto L_0x0bd5;	 Catch:{ Exception -> 0x0af3 }
        r1 = r15.duration;	 Catch:{ Exception -> 0x0af3 }
        r55 = r1;	 Catch:{ Exception -> 0x0af3 }
        goto L_0x0bdc;	 Catch:{ Exception -> 0x0af3 }
        r3 = r3 + 1;	 Catch:{ Exception -> 0x0af3 }
        r1 = r55;	 Catch:{ Exception -> 0x0af3 }
        goto L_0x0bb1;	 Catch:{ Exception -> 0x0af3 }
        r55 = r1;	 Catch:{ Exception -> 0x0af3 }
        r1 = r12.ttl;	 Catch:{ Exception -> 0x0af3 }
        r3 = r55 + 1;	 Catch:{ Exception -> 0x0af3 }
        r1 = java.lang.Math.max(r1, r3);	 Catch:{ Exception -> 0x0af3 }
        r12.ttl = r1;	 Catch:{ Exception -> 0x0af3 }
        r15 = r32;
        r1 = 1;
        if (r2 == r1) goto L_0x0c0f;
        r1 = org.telegram.messenger.MessageObject.isVoiceMessage(r12);	 Catch:{ Exception -> 0x0bfe }
        if (r1 != 0) goto L_0x0bfa;	 Catch:{ Exception -> 0x0bfe }
        r1 = org.telegram.messenger.MessageObject.isRoundVideoMessage(r12);	 Catch:{ Exception -> 0x0bfe }
        if (r1 == 0) goto L_0x0bf8;	 Catch:{ Exception -> 0x0bfe }
        goto L_0x0bfa;	 Catch:{ Exception -> 0x0bfe }
        r1 = 1;	 Catch:{ Exception -> 0x0bfe }
        goto L_0x0c0f;	 Catch:{ Exception -> 0x0bfe }
        r1 = 1;	 Catch:{ Exception -> 0x0bfe }
        r12.media_unread = r1;	 Catch:{ Exception -> 0x0bfe }
        goto L_0x0c0f;
    L_0x0bfe:
        r0 = move-exception;
        r4 = r127;
        r1 = r0;
        r101 = r2;
        r2 = r9;
        r17 = r11;
        r89 = r15;
        r15 = r24;
        r114 = r27;
        goto L_0x0b02;
        r12.send_state = r1;	 Catch:{ Exception -> 0x2907 }
        r3 = new org.telegram.messenger.MessageObject;	 Catch:{ Exception -> 0x2907 }
        r1 = r7.currentAccount;	 Catch:{ Exception -> 0x2907 }
        r56 = r2;
        r2 = 1;
        r3.<init>(r1, r12, r2);	 Catch:{ Exception -> 0x28d4 }
        r3.replyMessageObject = r14;	 Catch:{ Exception -> 0x28a1 }
        r1 = r3.isForwarded();	 Catch:{ Exception -> 0x28a1 }
        if (r1 != 0) goto L_0x0c59;
        r1 = r3.type;	 Catch:{ Exception -> 0x0c3b }
        r2 = 3;	 Catch:{ Exception -> 0x0c3b }
        if (r1 == r2) goto L_0x0c2f;	 Catch:{ Exception -> 0x0c3b }
        if (r127 != 0) goto L_0x0c2f;	 Catch:{ Exception -> 0x0c3b }
        r1 = r3.type;	 Catch:{ Exception -> 0x0c3b }
        r2 = 2;	 Catch:{ Exception -> 0x0c3b }
        if (r1 != r2) goto L_0x0c59;	 Catch:{ Exception -> 0x0c3b }
        r1 = r12.attachPath;	 Catch:{ Exception -> 0x0c3b }
        r1 = android.text.TextUtils.isEmpty(r1);	 Catch:{ Exception -> 0x0c3b }
        if (r1 != 0) goto L_0x0c59;	 Catch:{ Exception -> 0x0c3b }
        r1 = 1;	 Catch:{ Exception -> 0x0c3b }
        r3.attachPathExists = r1;	 Catch:{ Exception -> 0x0c3b }
        goto L_0x0c59;
    L_0x0c3b:
        r0 = move-exception;
        r4 = r127;
        r1 = r0;
        r2 = r9;
        r17 = r11;
        r89 = r15;
        r114 = r27;
        r111 = r46;
        r6 = r47;
        r5 = r48;
        r118 = r49;
        r108 = r50;
        r11 = r51;
        r113 = r52;
        r101 = r56;
        r15 = r3;
        goto L_0x0955;
        r1 = r3.videoEditedInfo;	 Catch:{ Exception -> 0x28a1 }
        if (r1 == 0) goto L_0x0c63;
        if (r127 != 0) goto L_0x0c63;
        r1 = r3.videoEditedInfo;	 Catch:{ Exception -> 0x0c3b }
        r4 = r1;
        goto L_0x0c65;
        r4 = r127;
        r1 = 0;
        r16 = 0;
        if (r13 == 0) goto L_0x0cab;
        r57 = r1;
        r1 = "groupId";	 Catch:{ Exception -> 0x0ca9 }
        r1 = r13.get(r1);	 Catch:{ Exception -> 0x0ca9 }
        r1 = (java.lang.String) r1;	 Catch:{ Exception -> 0x0ca9 }
        if (r1 == 0) goto L_0x0c93;	 Catch:{ Exception -> 0x0ca9 }
        r2 = org.telegram.messenger.Utilities.parseLong(r1);	 Catch:{ Exception -> 0x0ca9 }
        r17 = r2.longValue();	 Catch:{ Exception -> 0x0ca9 }
        r59 = r17;	 Catch:{ Exception -> 0x0ca9 }
        r61 = r1;	 Catch:{ Exception -> 0x0ca9 }
        r1 = r59;	 Catch:{ Exception -> 0x0ca9 }
        r12.grouped_id = r1;	 Catch:{ Exception -> 0x0ca9 }
        r62 = r1;	 Catch:{ Exception -> 0x0ca9 }
        r1 = r12.flags;	 Catch:{ Exception -> 0x0ca9 }
        r2 = 131072; // 0x20000 float:1.83671E-40 double:6.47582E-319;	 Catch:{ Exception -> 0x0ca9 }
        r1 = r1 | r2;	 Catch:{ Exception -> 0x0ca9 }
        r12.flags = r1;	 Catch:{ Exception -> 0x0ca9 }
        r1 = r62;	 Catch:{ Exception -> 0x0ca9 }
        goto L_0x0c97;	 Catch:{ Exception -> 0x0ca9 }
        r61 = r1;	 Catch:{ Exception -> 0x0ca9 }
        r1 = r57;	 Catch:{ Exception -> 0x0ca9 }
        r64 = r1;	 Catch:{ Exception -> 0x0ca9 }
        r1 = "final";	 Catch:{ Exception -> 0x0ca9 }
        r1 = r13.get(r1);	 Catch:{ Exception -> 0x0ca9 }
        if (r1 == 0) goto L_0x0ca3;
        r1 = 1;
        goto L_0x0ca4;
        r1 = 0;
        r16 = r1;
        r1 = r64;
        goto L_0x0cad;
    L_0x0ca9:
        r0 = move-exception;
        goto L_0x0c3e;
        r57 = r1;
        r17 = (r1 > r19 ? 1 : (r1 == r19 ? 0 : -1));
        if (r17 != 0) goto L_0x0d39;
        r66 = r11;
        r11 = new java.util.ArrayList;	 Catch:{ Exception -> 0x0d17 }
        r11.<init>();	 Catch:{ Exception -> 0x0d17 }
        r11.add(r3);	 Catch:{ Exception -> 0x0d17 }
        r14 = new java.util.ArrayList;	 Catch:{ Exception -> 0x0d17 }
        r14.<init>();	 Catch:{ Exception -> 0x0d17 }
        r14.add(r12);	 Catch:{ Exception -> 0x0d17 }
        r67 = r15;
        r15 = r7.currentAccount;	 Catch:{ Exception -> 0x0cf7 }
        r33 = org.telegram.messenger.MessagesStorage.getInstance(r15);	 Catch:{ Exception -> 0x0cf7 }
        r35 = 0;	 Catch:{ Exception -> 0x0cf7 }
        r36 = 1;	 Catch:{ Exception -> 0x0cf7 }
        r37 = 0;	 Catch:{ Exception -> 0x0cf7 }
        r38 = 0;	 Catch:{ Exception -> 0x0cf7 }
        r34 = r14;	 Catch:{ Exception -> 0x0cf7 }
        r33.putMessages(r34, r35, r36, r37, r38);	 Catch:{ Exception -> 0x0cf7 }
        r15 = r7.currentAccount;	 Catch:{ Exception -> 0x0cf7 }
        r15 = org.telegram.messenger.MessagesController.getInstance(r15);	 Catch:{ Exception -> 0x0cf7 }
        r15.updateInterfaceWithMessages(r5, r11);	 Catch:{ Exception -> 0x0cf7 }
        r15 = r7.currentAccount;	 Catch:{ Exception -> 0x0cf7 }
        r15 = org.telegram.messenger.NotificationCenter.getInstance(r15);	 Catch:{ Exception -> 0x0cf7 }
        r68 = r11;	 Catch:{ Exception -> 0x0cf7 }
        r11 = org.telegram.messenger.NotificationCenter.dialogsNeedReload;	 Catch:{ Exception -> 0x0cf7 }
        r70 = r13;
        r69 = r14;
        r14 = 0;
        r13 = new java.lang.Object[r14];	 Catch:{ Exception -> 0x0d64 }
        r15.postNotificationName(r11, r13);	 Catch:{ Exception -> 0x0d64 }
        goto L_0x0dbf;
    L_0x0cf7:
        r0 = move-exception;
        r70 = r13;
        r1 = r0;
        r15 = r3;
        r2 = r9;
        r114 = r27;
        r111 = r46;
        r6 = r47;
        r5 = r48;
        r118 = r49;
        r108 = r50;
        r11 = r51;
        r113 = r52;
        r101 = r56;
        r17 = r66;
        r89 = r67;
        r3 = r137;
        goto L_0x29e0;
    L_0x0d17:
        r0 = move-exception;
        r70 = r13;
        r67 = r15;
        r1 = r0;
        r15 = r3;
        r2 = r9;
        r114 = r27;
        r111 = r46;
        r6 = r47;
        r5 = r48;
        r118 = r49;
        r108 = r50;
        r11 = r51;
        r113 = r52;
        r101 = r56;
        r17 = r66;
        r89 = r67;
        r3 = r137;
        goto L_0x29e0;
        r66 = r11;
        r70 = r13;
        r67 = r15;
        r11 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x286d }
        r11.<init>();	 Catch:{ Exception -> 0x286d }
        r13 = "group_";	 Catch:{ Exception -> 0x286d }
        r11.append(r13);	 Catch:{ Exception -> 0x286d }
        r11.append(r1);	 Catch:{ Exception -> 0x286d }
        r11 = r11.toString();	 Catch:{ Exception -> 0x286d }
        r13 = r7.delayedMessages;	 Catch:{ Exception -> 0x286d }
        r13 = r13.get(r11);	 Catch:{ Exception -> 0x286d }
        r13 = (java.util.ArrayList) r13;	 Catch:{ Exception -> 0x286d }
        if (r13 == 0) goto L_0x0d82;
        r14 = 0;
        r15 = r13.get(r14);	 Catch:{ Exception -> 0x0d64 }
        r15 = (org.telegram.messenger.SendMessagesHelper.DelayedMessage) r15;	 Catch:{ Exception -> 0x0d64 }
        r25 = r15;	 Catch:{ Exception -> 0x0d64 }
        goto L_0x0d82;	 Catch:{ Exception -> 0x0d64 }
    L_0x0d64:
        r0 = move-exception;	 Catch:{ Exception -> 0x0d64 }
        r1 = r0;	 Catch:{ Exception -> 0x0d64 }
        r15 = r3;	 Catch:{ Exception -> 0x0d64 }
        r2 = r9;	 Catch:{ Exception -> 0x0d64 }
        r114 = r27;	 Catch:{ Exception -> 0x0d64 }
        r111 = r46;	 Catch:{ Exception -> 0x0d64 }
        r6 = r47;	 Catch:{ Exception -> 0x0d64 }
        r5 = r48;	 Catch:{ Exception -> 0x0d64 }
        r118 = r49;	 Catch:{ Exception -> 0x0d64 }
        r108 = r50;	 Catch:{ Exception -> 0x0d64 }
        r11 = r51;	 Catch:{ Exception -> 0x0d64 }
        r113 = r52;	 Catch:{ Exception -> 0x0d64 }
        r101 = r56;	 Catch:{ Exception -> 0x0d64 }
        r17 = r66;	 Catch:{ Exception -> 0x0d64 }
        r89 = r67;	 Catch:{ Exception -> 0x0d64 }
        r13 = r70;	 Catch:{ Exception -> 0x0d64 }
        goto L_0x0955;	 Catch:{ Exception -> 0x0d64 }
        if (r25 != 0) goto L_0x0dad;	 Catch:{ Exception -> 0x0d64 }
        r14 = new org.telegram.messenger.SendMessagesHelper$DelayedMessage;	 Catch:{ Exception -> 0x0d64 }
        r14.<init>(r5);	 Catch:{ Exception -> 0x0d64 }
        r15 = 4;
        r14.type = r15;	 Catch:{ Exception -> 0x0db6 }
        r14.groupId = r1;	 Catch:{ Exception -> 0x0db6 }
        r15 = new java.util.ArrayList;	 Catch:{ Exception -> 0x0db6 }
        r15.<init>();	 Catch:{ Exception -> 0x0db6 }
        r14.messageObjects = r15;	 Catch:{ Exception -> 0x0db6 }
        r15 = new java.util.ArrayList;	 Catch:{ Exception -> 0x0db6 }
        r15.<init>();	 Catch:{ Exception -> 0x0db6 }
        r14.messages = r15;	 Catch:{ Exception -> 0x0db6 }
        r15 = new java.util.ArrayList;	 Catch:{ Exception -> 0x0db6 }
        r15.<init>();	 Catch:{ Exception -> 0x0db6 }
        r14.originalPaths = r15;	 Catch:{ Exception -> 0x0db6 }
        r15 = new java.util.HashMap;	 Catch:{ Exception -> 0x0db6 }
        r15.<init>();	 Catch:{ Exception -> 0x0db6 }
        r14.extraHashMap = r15;	 Catch:{ Exception -> 0x0db6 }
        r14.encryptedChat = r9;	 Catch:{ Exception -> 0x0db6 }
        goto L_0x0daf;	 Catch:{ Exception -> 0x0db6 }
        r14 = r25;	 Catch:{ Exception -> 0x0db6 }
        if (r16 == 0) goto L_0x0dbd;	 Catch:{ Exception -> 0x0db6 }
        r15 = r12.id;	 Catch:{ Exception -> 0x0db6 }
        r14.finalGroupMessage = r15;	 Catch:{ Exception -> 0x0db6 }
        goto L_0x0dbd;
    L_0x0db6:
        r0 = move-exception;
        r1 = r0;
        r15 = r3;
        r2 = r9;
        r25 = r14;
        goto L_0x0d68;
        r25 = r14;
        r11 = org.telegram.messenger.BuildVars.LOGS_ENABLED;	 Catch:{ Exception -> 0x286d }
        if (r11 == 0) goto L_0x0e2c;
        if (r49 == 0) goto L_0x0e2c;
        r11 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0e0c }
        r11.<init>();	 Catch:{ Exception -> 0x0e0c }
        r13 = "send message user_id = ";	 Catch:{ Exception -> 0x0e0c }
        r11.append(r13);	 Catch:{ Exception -> 0x0e0c }
        r13 = r49;
        r14 = r13.user_id;	 Catch:{ Exception -> 0x0dfc }
        r11.append(r14);	 Catch:{ Exception -> 0x0dfc }
        r14 = " chat_id = ";	 Catch:{ Exception -> 0x0dfc }
        r11.append(r14);	 Catch:{ Exception -> 0x0dfc }
        r14 = r13.chat_id;	 Catch:{ Exception -> 0x0dfc }
        r11.append(r14);	 Catch:{ Exception -> 0x0dfc }
        r14 = " channel_id = ";	 Catch:{ Exception -> 0x0dfc }
        r11.append(r14);	 Catch:{ Exception -> 0x0dfc }
        r14 = r13.channel_id;	 Catch:{ Exception -> 0x0dfc }
        r11.append(r14);	 Catch:{ Exception -> 0x0dfc }
        r14 = " access_hash = ";	 Catch:{ Exception -> 0x0dfc }
        r11.append(r14);	 Catch:{ Exception -> 0x0dfc }
        r14 = r13.access_hash;	 Catch:{ Exception -> 0x0dfc }
        r11.append(r14);	 Catch:{ Exception -> 0x0dfc }
        r11 = r11.toString();	 Catch:{ Exception -> 0x0dfc }
        org.telegram.messenger.FileLog.d(r11);	 Catch:{ Exception -> 0x0dfc }
        goto L_0x0e2e;
    L_0x0dfc:
        r0 = move-exception;
        r1 = r0;
        r15 = r3;
        r2 = r9;
        r118 = r13;
        r114 = r27;
        r111 = r46;
        r6 = r47;
        r5 = r48;
        goto L_0x0d72;
    L_0x0e0c:
        r0 = move-exception;
        r1 = r0;
        r15 = r3;
        r2 = r9;
        r114 = r27;
        r111 = r46;
        r6 = r47;
        r5 = r48;
        r118 = r49;
        r108 = r50;
        r11 = r51;
        r113 = r52;
        r101 = r56;
        r17 = r66;
        r89 = r67;
        r13 = r70;
        r3 = r137;
        goto L_0x29e0;
        r13 = r49;
        if (r8 == 0) goto L_0x260a;
        r11 = 9;
        if (r8 != r11) goto L_0x0e5f;
        if (r47 == 0) goto L_0x0e5f;
        if (r9 == 0) goto L_0x0e5f;
        r102 = r1;
        r1 = r3;
        r106 = r4;
        r15 = r8;
        r2 = r9;
        r14 = r12;
        r114 = r27;
        r111 = r46;
        r107 = r47;
        r105 = r48;
        r108 = r50;
        r11 = r51;
        r113 = r52;
        r101 = r56;
        r17 = r66;
        r89 = r67;
        r8 = r70;
        r3 = r137;
        r120 = r5;
        r5 = r13;
        r12 = r120;
        goto L_0x262f;
        r11 = 1;
        if (r8 < r11) goto L_0x0e65;
        r11 = 3;
        if (r8 <= r11) goto L_0x0e72;
        r11 = 5;
        if (r8 < r11) goto L_0x0e6c;
        r11 = 8;
        if (r8 <= r11) goto L_0x0e72;
        r11 = 9;
        if (r8 != r11) goto L_0x242a;
        if (r9 == 0) goto L_0x242a;
        if (r9 != 0) goto L_0x1b25;
        r15 = 0;
        r11 = 1;
        if (r8 != r11) goto L_0x0f2a;
        r11 = r48;
        r14 = r11 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaVenue;	 Catch:{ Exception -> 0x0f0b }
        if (r14 == 0) goto L_0x0ea7;
        r14 = new org.telegram.tgnet.TLRPC$TL_inputMediaVenue;	 Catch:{ Exception -> 0x0e98 }
        r14.<init>();	 Catch:{ Exception -> 0x0e98 }
        r15 = r11.address;	 Catch:{ Exception -> 0x0e98 }
        r14.address = r15;	 Catch:{ Exception -> 0x0e98 }
        r15 = r11.title;	 Catch:{ Exception -> 0x0e98 }
        r14.title = r15;	 Catch:{ Exception -> 0x0e98 }
        r15 = r11.provider;	 Catch:{ Exception -> 0x0e98 }
        r14.provider = r15;	 Catch:{ Exception -> 0x0e98 }
        r15 = r11.venue_id;	 Catch:{ Exception -> 0x0e98 }
        r14.venue_id = r15;	 Catch:{ Exception -> 0x0e98 }
        r15 = "";	 Catch:{ Exception -> 0x0e98 }
        r14.venue_type = r15;	 Catch:{ Exception -> 0x0e98 }
        goto L_0x0eba;
    L_0x0e98:
        r0 = move-exception;
        r1 = r0;
        r15 = r3;
        r2 = r9;
        r5 = r11;
        r118 = r13;
        r114 = r27;
        r111 = r46;
        r6 = r47;
        goto L_0x0d72;
        r14 = r11 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGeoLive;	 Catch:{ Exception -> 0x0f0b }
        if (r14 == 0) goto L_0x0eb5;
        r14 = new org.telegram.tgnet.TLRPC$TL_inputMediaGeoLive;	 Catch:{ Exception -> 0x0e98 }
        r14.<init>();	 Catch:{ Exception -> 0x0e98 }
        r15 = r11.period;	 Catch:{ Exception -> 0x0e98 }
        r14.period = r15;	 Catch:{ Exception -> 0x0e98 }
        goto L_0x0eba;
        r14 = new org.telegram.tgnet.TLRPC$TL_inputMediaGeoPoint;	 Catch:{ Exception -> 0x0f0b }
        r14.<init>();	 Catch:{ Exception -> 0x0f0b }
        r15 = r14;	 Catch:{ Exception -> 0x0f0b }
        r14 = new org.telegram.tgnet.TLRPC$TL_inputGeoPoint;	 Catch:{ Exception -> 0x0f0b }
        r14.<init>();	 Catch:{ Exception -> 0x0f0b }
        r15.geo_point = r14;	 Catch:{ Exception -> 0x0f0b }
        r14 = r15.geo_point;	 Catch:{ Exception -> 0x0f0b }
        r71 = r13;
        r13 = r11.geo;	 Catch:{ Exception -> 0x0eee }
        r72 = r1;	 Catch:{ Exception -> 0x0eee }
        r1 = r13.lat;	 Catch:{ Exception -> 0x0eee }
        r14.lat = r1;	 Catch:{ Exception -> 0x0eee }
        r1 = r15.geo_point;	 Catch:{ Exception -> 0x0eee }
        r2 = r11.geo;	 Catch:{ Exception -> 0x0eee }
        r13 = r2._long;	 Catch:{ Exception -> 0x0eee }
        r1._long = r13;	 Catch:{ Exception -> 0x0eee }
        r75 = r4;
        r80 = r8;
        r79 = r9;
        r10 = r15;
        r9 = r25;
        r13 = r27;
        r78 = r50;
        r82 = r51;
        r14 = r70;
        r8 = r133;
        r15 = r3;
        r3 = r52;
        goto L_0x1548;
    L_0x0eee:
        r0 = move-exception;
        r1 = r0;
        r15 = r3;
        r2 = r9;
        r5 = r11;
        r114 = r27;
        r111 = r46;
        r6 = r47;
        r108 = r50;
        r11 = r51;
        r113 = r52;
        r101 = r56;
        r17 = r66;
        r89 = r67;
        r13 = r70;
        r118 = r71;
        goto L_0x0955;
    L_0x0f0b:
        r0 = move-exception;
        r1 = r0;
        r15 = r3;
        r2 = r9;
        r5 = r11;
        r118 = r13;
        r114 = r27;
        r111 = r46;
        r6 = r47;
        r108 = r50;
        r11 = r51;
        r113 = r52;
        r101 = r56;
        r17 = r66;
        r89 = r67;
        r13 = r70;
        r3 = r137;
        goto L_0x29e0;
        r72 = r1;
        r71 = r13;
        r11 = r48;
        r1 = 2;
        if (r8 == r1) goto L_0x13eb;
        r1 = 9;
        if (r8 != r1) goto L_0x0f4c;
        if (r52 == 0) goto L_0x0f4c;
        r75 = r4;
        r80 = r8;
        r79 = r9;
        r76 = r15;
        r13 = r27;
        r78 = r50;
        r1 = r51;
        r14 = r70;
        r15 = r3;
        goto L_0x13fc;
        r1 = 3;
        if (r8 != r1) goto L_0x107b;
        r1 = r51;
        r13 = r1.access_hash;	 Catch:{ Exception -> 0x1058 }
        r2 = (r13 > r19 ? 1 : (r13 == r19 ? 0 : -1));
        if (r2 != 0) goto L_0x0ff9;
        r2 = new org.telegram.tgnet.TLRPC$TL_inputMediaUploadedDocument;	 Catch:{ Exception -> 0x0fdb }
        r2.<init>();	 Catch:{ Exception -> 0x0fdb }
        r15 = r2;	 Catch:{ Exception -> 0x0fdb }
        r2 = r1.mime_type;	 Catch:{ Exception -> 0x0fdb }
        r15.mime_type = r2;	 Catch:{ Exception -> 0x0fdb }
        r2 = r1.attributes;	 Catch:{ Exception -> 0x0fdb }
        r15.attributes = r2;	 Catch:{ Exception -> 0x0fdb }
        r2 = org.telegram.messenger.MessageObject.isRoundVideoDocument(r1);	 Catch:{ Exception -> 0x0fdb }
        if (r2 != 0) goto L_0x0f93;
        if (r4 == 0) goto L_0x0f90;
        r2 = r4.muted;	 Catch:{ Exception -> 0x0f76 }
        if (r2 != 0) goto L_0x0f93;	 Catch:{ Exception -> 0x0f76 }
        r2 = r4.roundVideo;	 Catch:{ Exception -> 0x0f76 }
        if (r2 != 0) goto L_0x0f93;	 Catch:{ Exception -> 0x0f76 }
        goto L_0x0f90;	 Catch:{ Exception -> 0x0f76 }
    L_0x0f76:
        r0 = move-exception;	 Catch:{ Exception -> 0x0f76 }
        r15 = r3;	 Catch:{ Exception -> 0x0f76 }
        r2 = r9;	 Catch:{ Exception -> 0x0f76 }
        r5 = r11;	 Catch:{ Exception -> 0x0f76 }
        r114 = r27;	 Catch:{ Exception -> 0x0f76 }
        r111 = r46;	 Catch:{ Exception -> 0x0f76 }
        r6 = r47;	 Catch:{ Exception -> 0x0f76 }
        r108 = r50;	 Catch:{ Exception -> 0x0f76 }
        r113 = r52;	 Catch:{ Exception -> 0x0f76 }
        r101 = r56;	 Catch:{ Exception -> 0x0f76 }
        r17 = r66;	 Catch:{ Exception -> 0x0f76 }
        r89 = r67;	 Catch:{ Exception -> 0x0f76 }
        r13 = r70;	 Catch:{ Exception -> 0x0f76 }
        r118 = r71;	 Catch:{ Exception -> 0x0f76 }
        goto L_0x10cc;	 Catch:{ Exception -> 0x0f76 }
        r2 = 1;	 Catch:{ Exception -> 0x0f76 }
        r15.nosound_video = r2;	 Catch:{ Exception -> 0x0f76 }
        if (r10 == 0) goto L_0x0f9f;	 Catch:{ Exception -> 0x0f76 }
        r15.ttl_seconds = r10;	 Catch:{ Exception -> 0x0f76 }
        r12.ttl = r10;	 Catch:{ Exception -> 0x0f76 }
        r2 = r15.flags;	 Catch:{ Exception -> 0x0f76 }
        r13 = 2;	 Catch:{ Exception -> 0x0f76 }
        r2 = r2 | r13;	 Catch:{ Exception -> 0x0f76 }
        r15.flags = r2;	 Catch:{ Exception -> 0x0f76 }
        if (r25 != 0) goto L_0x0fb4;
        r2 = new org.telegram.messenger.SendMessagesHelper$DelayedMessage;	 Catch:{ Exception -> 0x0fdb }
        r2.<init>(r5);	 Catch:{ Exception -> 0x0fdb }
        r13 = 1;
        r2.type = r13;	 Catch:{ Exception -> 0x0fb0 }
        r2.obj = r3;	 Catch:{ Exception -> 0x0fb0 }
        r13 = r27;
        r2.originalPath = r13;	 Catch:{ Exception -> 0x0fd2 }
        goto L_0x0fb8;	 Catch:{ Exception -> 0x0fd2 }
    L_0x0fb0:
        r0 = move-exception;	 Catch:{ Exception -> 0x0fd2 }
        r25 = r2;	 Catch:{ Exception -> 0x0fd2 }
        goto L_0x0fdc;	 Catch:{ Exception -> 0x0fd2 }
        r13 = r27;	 Catch:{ Exception -> 0x0fd2 }
        r2 = r25;	 Catch:{ Exception -> 0x0fd2 }
        r14 = r1.thumb;	 Catch:{ Exception -> 0x0fd2 }
        r14 = r14.location;	 Catch:{ Exception -> 0x0fd2 }
        r2.location = r14;	 Catch:{ Exception -> 0x0fd2 }
        r2.videoEditedInfo = r4;	 Catch:{ Exception -> 0x0fd2 }
        r82 = r1;
        r75 = r4;
        r80 = r8;
        r79 = r9;
        r10 = r15;
        r78 = r50;
        r14 = r70;
        r8 = r133;
        r9 = r2;
        goto L_0x0ee9;
    L_0x0fd2:
        r0 = move-exception;
        r25 = r2;
        r15 = r3;
        r2 = r9;
        r5 = r11;
        r114 = r13;
        goto L_0x0f7c;
    L_0x0fdb:
        r0 = move-exception;
        r15 = r3;
        r2 = r9;
        r5 = r11;
        r114 = r27;
        r111 = r46;
        r6 = r47;
        r108 = r50;
        r113 = r52;
        r101 = r56;
        r17 = r66;
        r89 = r67;
        r13 = r70;
        r118 = r71;
        r3 = r137;
        r11 = r1;
        r1 = r0;
        goto L_0x29e0;
        r13 = r27;
        r2 = new org.telegram.tgnet.TLRPC$TL_inputMediaDocument;	 Catch:{ Exception -> 0x1035 }
        r2.<init>();	 Catch:{ Exception -> 0x1035 }
        r14 = new org.telegram.tgnet.TLRPC$TL_inputDocument;	 Catch:{ Exception -> 0x1035 }
        r14.<init>();	 Catch:{ Exception -> 0x1035 }
        r2.id = r14;	 Catch:{ Exception -> 0x1035 }
        r14 = r2.id;	 Catch:{ Exception -> 0x1035 }
        r74 = r3;
        r75 = r4;
        r3 = r1.id;	 Catch:{ Exception -> 0x1028 }
        r14.id = r3;	 Catch:{ Exception -> 0x1028 }
        r3 = r2.id;	 Catch:{ Exception -> 0x1028 }
        r76 = r15;	 Catch:{ Exception -> 0x1028 }
        r14 = r1.access_hash;	 Catch:{ Exception -> 0x1028 }
        r3.access_hash = r14;	 Catch:{ Exception -> 0x1028 }
        r15 = r2;
        r82 = r1;
        r80 = r8;
        r79 = r9;
        r10 = r15;
        r9 = r25;
        r78 = r50;
        goto L_0x10a7;
    L_0x1028:
        r0 = move-exception;
        r2 = r9;
        r5 = r11;
        r114 = r13;
        r111 = r46;
        r6 = r47;
        r108 = r50;
        goto L_0x10bc;
    L_0x1035:
        r0 = move-exception;
        r74 = r3;
        r75 = r4;
        r2 = r9;
        r5 = r11;
        r114 = r13;
        r111 = r46;
        r6 = r47;
        r108 = r50;
        r113 = r52;
        r101 = r56;
        r17 = r66;
        r89 = r67;
        r13 = r70;
        r118 = r71;
        r15 = r74;
        r3 = r137;
        r11 = r1;
        r1 = r0;
        goto L_0x29e0;
    L_0x1058:
        r0 = move-exception;
        r74 = r3;
        r75 = r4;
        r2 = r9;
        r5 = r11;
        r114 = r27;
        r111 = r46;
        r6 = r47;
        r108 = r50;
        r113 = r52;
        r101 = r56;
        r17 = r66;
        r89 = r67;
        r13 = r70;
        r118 = r71;
        r15 = r74;
        r3 = r137;
        r11 = r1;
        r1 = r0;
        goto L_0x29e0;
        r74 = r3;
        r75 = r4;
        r76 = r15;
        r13 = r27;
        r1 = r51;
        r2 = 6;
        if (r8 != r2) goto L_0x10f4;
        r2 = new org.telegram.tgnet.TLRPC$TL_inputMediaContact;	 Catch:{ Exception -> 0x10d1 }
        r2.<init>();	 Catch:{ Exception -> 0x10d1 }
        r15 = r2;
        r2 = r50;
        r3 = r2.phone;	 Catch:{ Exception -> 0x10b1 }
        r15.phone_number = r3;	 Catch:{ Exception -> 0x10b1 }
        r3 = r2.first_name;	 Catch:{ Exception -> 0x10b1 }
        r15.first_name = r3;	 Catch:{ Exception -> 0x10b1 }
        r3 = r2.last_name;	 Catch:{ Exception -> 0x10b1 }
        r15.last_name = r3;	 Catch:{ Exception -> 0x10b1 }
        r82 = r1;
        r78 = r2;
        r80 = r8;
        r79 = r9;
        r10 = r15;
        r9 = r25;
        r3 = r52;
        r14 = r70;
        r15 = r74;
        r8 = r133;
        goto L_0x1548;
    L_0x10b1:
        r0 = move-exception;
        r108 = r2;
        r2 = r9;
        r5 = r11;
        r114 = r13;
        r111 = r46;
        r6 = r47;
        r113 = r52;
        r101 = r56;
        r17 = r66;
        r89 = r67;
        r13 = r70;
        r118 = r71;
        r15 = r74;
        r4 = r75;
        r3 = r137;
        r11 = r1;
        goto L_0x0844;
    L_0x10d1:
        r0 = move-exception;
        r2 = r50;
        r108 = r2;
        r2 = r9;
        r5 = r11;
        r114 = r13;
        r111 = r46;
        r6 = r47;
        r113 = r52;
        r101 = r56;
        r17 = r66;
        r89 = r67;
        r13 = r70;
        r118 = r71;
        r15 = r74;
        r4 = r75;
        r3 = r137;
        r11 = r1;
        r1 = r0;
        goto L_0x29e0;
        r2 = r50;
        r3 = 7;
        if (r8 == r3) goto L_0x122d;
        r3 = 9;
        if (r8 != r3) goto L_0x1101;
        r77 = r74;
        goto L_0x122f;
        r3 = 8;
        if (r8 != r3) goto L_0x1217;
        r3 = r1.access_hash;	 Catch:{ Exception -> 0x11f4 }
        r14 = (r3 > r19 ? 1 : (r3 == r19 ? 0 : -1));
        if (r14 != 0) goto L_0x1188;
        r3 = new org.telegram.tgnet.TLRPC$TL_inputMediaUploadedDocument;	 Catch:{ Exception -> 0x1166 }
        r3.<init>();	 Catch:{ Exception -> 0x1166 }
        r15 = r3;	 Catch:{ Exception -> 0x1166 }
        r3 = r1.mime_type;	 Catch:{ Exception -> 0x1166 }
        r15.mime_type = r3;	 Catch:{ Exception -> 0x1166 }
        r3 = r1.attributes;	 Catch:{ Exception -> 0x1166 }
        r15.attributes = r3;	 Catch:{ Exception -> 0x1166 }
        if (r10 == 0) goto L_0x1125;
        r15.ttl_seconds = r10;	 Catch:{ Exception -> 0x10b1 }
        r12.ttl = r10;	 Catch:{ Exception -> 0x10b1 }
        r3 = r15.flags;	 Catch:{ Exception -> 0x10b1 }
        r4 = 2;	 Catch:{ Exception -> 0x10b1 }
        r3 = r3 | r4;	 Catch:{ Exception -> 0x10b1 }
        r15.flags = r3;	 Catch:{ Exception -> 0x10b1 }
        r3 = new org.telegram.messenger.SendMessagesHelper$DelayedMessage;	 Catch:{ Exception -> 0x1166 }
        r3.<init>(r5);	 Catch:{ Exception -> 0x1166 }
        r4 = 3;
        r3.type = r4;	 Catch:{ Exception -> 0x115e }
        r4 = r74;
        r3.obj = r4;	 Catch:{ Exception -> 0x1142 }
        r82 = r1;
        r78 = r2;
        r80 = r8;
        r79 = r9;
        r10 = r15;
        r14 = r70;
        r8 = r133;
        r9 = r3;
        r15 = r4;
        goto L_0x0eea;
    L_0x1142:
        r0 = move-exception;
        r108 = r2;
        r25 = r3;
        r15 = r4;
        r2 = r9;
        r5 = r11;
        r114 = r13;
        r111 = r46;
        r6 = r47;
        r113 = r52;
        r101 = r56;
        r17 = r66;
        r89 = r67;
        r13 = r70;
        r118 = r71;
        goto L_0x10ca;
    L_0x115e:
        r0 = move-exception;
        r4 = r74;
        r108 = r2;
        r25 = r3;
        goto L_0x116b;
    L_0x1166:
        r0 = move-exception;
        r4 = r74;
        r108 = r2;
        r15 = r4;
        r2 = r9;
        r5 = r11;
        r114 = r13;
        r111 = r46;
        r6 = r47;
        r113 = r52;
        r101 = r56;
        r17 = r66;
        r89 = r67;
        r13 = r70;
        r118 = r71;
        r4 = r75;
        r3 = r137;
        r11 = r1;
        r1 = r0;
        goto L_0x29e0;
        r4 = r74;
        r3 = new org.telegram.tgnet.TLRPC$TL_inputMediaDocument;	 Catch:{ Exception -> 0x11d1 }
        r3.<init>();	 Catch:{ Exception -> 0x11d1 }
        r14 = new org.telegram.tgnet.TLRPC$TL_inputDocument;	 Catch:{ Exception -> 0x11d1 }
        r14.<init>();	 Catch:{ Exception -> 0x11d1 }
        r3.id = r14;	 Catch:{ Exception -> 0x11d1 }
        r14 = r3.id;	 Catch:{ Exception -> 0x11d1 }
        r77 = r4;
        r4 = r1.id;	 Catch:{ Exception -> 0x11ba }
        r14.id = r4;	 Catch:{ Exception -> 0x11ba }
        r4 = r3.id;	 Catch:{ Exception -> 0x11ba }
        r5 = r1.access_hash;	 Catch:{ Exception -> 0x11ba }
        r4.access_hash = r5;	 Catch:{ Exception -> 0x11ba }
        r15 = r3;
        r82 = r1;
        r78 = r2;
        r80 = r8;
        r79 = r9;
        r10 = r15;
        r9 = r25;
        r3 = r52;
        r14 = r70;
        r15 = r77;
        r5 = r131;
        goto L_0x10ad;
    L_0x11ba:
        r0 = move-exception;
        r108 = r2;
        r2 = r9;
        r5 = r11;
        r114 = r13;
        r111 = r46;
        r6 = r47;
        r113 = r52;
        r101 = r56;
        r17 = r66;
        r89 = r67;
        r13 = r70;
        goto L_0x12c2;
    L_0x11d1:
        r0 = move-exception;
        r77 = r4;
        r108 = r2;
        r2 = r9;
        r5 = r11;
        r114 = r13;
        r111 = r46;
        r6 = r47;
        r113 = r52;
        r101 = r56;
        r17 = r66;
        r89 = r67;
        r13 = r70;
        r118 = r71;
        r4 = r75;
        r15 = r77;
        r3 = r137;
        r11 = r1;
        r1 = r0;
        goto L_0x29e0;
    L_0x11f4:
        r0 = move-exception;
        r77 = r74;
        r108 = r2;
        r2 = r9;
        r5 = r11;
        r114 = r13;
        r111 = r46;
        r6 = r47;
        r113 = r52;
        r101 = r56;
        r17 = r66;
        r89 = r67;
        r13 = r70;
        r118 = r71;
        r4 = r75;
        r15 = r77;
        r3 = r137;
        r11 = r1;
        r1 = r0;
        goto L_0x29e0;
        r82 = r1;
        r78 = r2;
        r80 = r8;
        r79 = r9;
        r9 = r25;
        r3 = r52;
        r14 = r70;
        r15 = r74;
        r10 = r76;
        r8 = r133;
        goto L_0x1548;
        r77 = r74;
        r3 = r1.access_hash;	 Catch:{ Exception -> 0x13c3 }
        r5 = (r3 > r19 ? 1 : (r3 == r19 ? 0 : -1));
        if (r5 != 0) goto L_0x1367;
        if (r9 != 0) goto L_0x129a;
        if (r13 == 0) goto L_0x129a;
        r3 = r13.length();	 Catch:{ Exception -> 0x1278 }
        if (r3 <= 0) goto L_0x129a;	 Catch:{ Exception -> 0x1278 }
        r3 = "http";	 Catch:{ Exception -> 0x1278 }
        r3 = r13.startsWith(r3);	 Catch:{ Exception -> 0x1278 }
        if (r3 == 0) goto L_0x129a;	 Catch:{ Exception -> 0x1278 }
        if (r70 == 0) goto L_0x129a;	 Catch:{ Exception -> 0x1278 }
        r3 = new org.telegram.tgnet.TLRPC$TL_inputMediaGifExternal;	 Catch:{ Exception -> 0x1278 }
        r3.<init>();	 Catch:{ Exception -> 0x1278 }
        r4 = "url";	 Catch:{ Exception -> 0x1278 }
        r14 = r70;
        r4 = r14.get(r4);	 Catch:{ Exception -> 0x12ae }
        r4 = (java.lang.String) r4;	 Catch:{ Exception -> 0x12ae }
        r5 = "\\|";	 Catch:{ Exception -> 0x12ae }
        r4 = r4.split(r5);	 Catch:{ Exception -> 0x12ae }
        r5 = r4.length;	 Catch:{ Exception -> 0x12ae }
        r6 = 2;	 Catch:{ Exception -> 0x12ae }
        if (r5 != r6) goto L_0x126f;	 Catch:{ Exception -> 0x12ae }
        r5 = r3;	 Catch:{ Exception -> 0x12ae }
        r5 = (org.telegram.tgnet.TLRPC.TL_inputMediaGifExternal) r5;	 Catch:{ Exception -> 0x12ae }
        r6 = 0;	 Catch:{ Exception -> 0x12ae }
        r15 = r4[r6];	 Catch:{ Exception -> 0x12ae }
        r5.url = r15;	 Catch:{ Exception -> 0x12ae }
        r5 = 1;	 Catch:{ Exception -> 0x12ae }
        r6 = r4[r5];	 Catch:{ Exception -> 0x12ae }
        r3.q = r6;	 Catch:{ Exception -> 0x12ae }
        r78 = r2;
        r15 = r77;
        r5 = r131;
        goto L_0x12e4;
    L_0x1278:
        r0 = move-exception;
        r14 = r70;
        r108 = r2;
        r2 = r9;
        r5 = r11;
        r114 = r13;
        r13 = r14;
        r111 = r46;
        r6 = r47;
        r113 = r52;
        r101 = r56;
        r17 = r66;
        r89 = r67;
        r118 = r71;
        r4 = r75;
        r15 = r77;
        r3 = r137;
        r11 = r1;
        r1 = r0;
        goto L_0x29e0;
        r14 = r70;
        r3 = new org.telegram.tgnet.TLRPC$TL_inputMediaUploadedDocument;	 Catch:{ Exception -> 0x1343 }
        r3.<init>();	 Catch:{ Exception -> 0x1343 }
        if (r10 == 0) goto L_0x12ca;
        r3.ttl_seconds = r10;	 Catch:{ Exception -> 0x12ae }
        r12.ttl = r10;	 Catch:{ Exception -> 0x12ae }
        r4 = r3.flags;	 Catch:{ Exception -> 0x12ae }
        r5 = 2;	 Catch:{ Exception -> 0x12ae }
        r4 = r4 | r5;	 Catch:{ Exception -> 0x12ae }
        r3.flags = r4;	 Catch:{ Exception -> 0x12ae }
        goto L_0x12ca;
    L_0x12ae:
        r0 = move-exception;
        r108 = r2;
        r2 = r9;
        r5 = r11;
        r114 = r13;
        r13 = r14;
        r111 = r46;
        r6 = r47;
        r113 = r52;
        r101 = r56;
        r17 = r66;
        r89 = r67;
        r118 = r71;
        r4 = r75;
        r15 = r77;
        goto L_0x10cc;
        r4 = new org.telegram.messenger.SendMessagesHelper$DelayedMessage;	 Catch:{ Exception -> 0x1343 }
        r5 = r131;
        r4.<init>(r5);	 Catch:{ Exception -> 0x133d }
        r4.originalPath = r13;	 Catch:{ Exception -> 0x1335 }
        r15 = 2;	 Catch:{ Exception -> 0x1335 }
        r4.type = r15;	 Catch:{ Exception -> 0x1335 }
        r15 = r77;
        r4.obj = r15;	 Catch:{ Exception -> 0x1313 }
        r78 = r2;
        r2 = r1.thumb;	 Catch:{ Exception -> 0x12f7 }
        r2 = r2.location;	 Catch:{ Exception -> 0x12f7 }
        r4.location = r2;	 Catch:{ Exception -> 0x12f7 }
        r25 = r4;
        r2 = r1.mime_type;	 Catch:{ Exception -> 0x12f5 }
        r3.mime_type = r2;	 Catch:{ Exception -> 0x12f5 }
        r2 = r1.attributes;	 Catch:{ Exception -> 0x12f5 }
        r3.attributes = r2;	 Catch:{ Exception -> 0x12f5 }
        r82 = r1;
        r10 = r3;
        r80 = r8;
        r79 = r9;
        goto L_0x1390;
    L_0x12f5:
        r0 = move-exception;
        goto L_0x12fa;
    L_0x12f7:
        r0 = move-exception;
        r25 = r4;
        r2 = r9;
        r5 = r11;
        r114 = r13;
        r13 = r14;
        r111 = r46;
        r6 = r47;
        r113 = r52;
        r101 = r56;
        r17 = r66;
        r89 = r67;
        r118 = r71;
        r4 = r75;
        r108 = r78;
        goto L_0x10cc;
    L_0x1313:
        r0 = move-exception;
        r78 = r2;
        r25 = r4;
        r2 = r9;
        r5 = r11;
        r114 = r13;
        r13 = r14;
        r111 = r46;
        r6 = r47;
        r113 = r52;
        r101 = r56;
        r17 = r66;
        r89 = r67;
        r118 = r71;
        r4 = r75;
        r108 = r78;
        r3 = r137;
        r11 = r1;
        r1 = r0;
        goto L_0x29e0;
    L_0x1335:
        r0 = move-exception;
        r78 = r2;
        r15 = r77;
        r25 = r4;
        goto L_0x134a;
    L_0x133d:
        r0 = move-exception;
        r78 = r2;
        r15 = r77;
        goto L_0x134a;
    L_0x1343:
        r0 = move-exception;
        r78 = r2;
        r15 = r77;
        r5 = r131;
        r2 = r9;
        r5 = r11;
        r114 = r13;
        r13 = r14;
        r111 = r46;
        r6 = r47;
        r113 = r52;
        r101 = r56;
        r17 = r66;
        r89 = r67;
        r118 = r71;
        r4 = r75;
        r108 = r78;
        r3 = r137;
        r11 = r1;
        r1 = r0;
        goto L_0x29e0;
        r78 = r2;
        r14 = r70;
        r15 = r77;
        r5 = r131;
        r2 = new org.telegram.tgnet.TLRPC$TL_inputMediaDocument;	 Catch:{ Exception -> 0x13a3 }
        r2.<init>();	 Catch:{ Exception -> 0x13a3 }
        r3 = new org.telegram.tgnet.TLRPC$TL_inputDocument;	 Catch:{ Exception -> 0x13a3 }
        r3.<init>();	 Catch:{ Exception -> 0x13a3 }
        r2.id = r3;	 Catch:{ Exception -> 0x13a3 }
        r3 = r2.id;	 Catch:{ Exception -> 0x13a3 }
        r80 = r8;
        r79 = r9;
        r8 = r1.id;	 Catch:{ Exception -> 0x1396 }
        r3.id = r8;	 Catch:{ Exception -> 0x1396 }
        r3 = r2.id;	 Catch:{ Exception -> 0x1396 }
        r8 = r1.access_hash;	 Catch:{ Exception -> 0x1396 }
        r3.access_hash = r8;	 Catch:{ Exception -> 0x1396 }
        r82 = r1;
        r10 = r2;
        r9 = r25;
        r3 = r52;
        goto L_0x10ad;
    L_0x1396:
        r0 = move-exception;
        r5 = r11;
        r114 = r13;
        r13 = r14;
        r111 = r46;
        r6 = r47;
        r113 = r52;
        goto L_0x1421;
    L_0x13a3:
        r0 = move-exception;
        r80 = r8;
        r2 = r9;
        r5 = r11;
        r114 = r13;
        r13 = r14;
        r111 = r46;
        r6 = r47;
        r113 = r52;
        r101 = r56;
        r17 = r66;
        r89 = r67;
        r118 = r71;
        r4 = r75;
        r108 = r78;
        r3 = r137;
        r11 = r1;
        r1 = r0;
        goto L_0x29e0;
    L_0x13c3:
        r0 = move-exception;
        r78 = r2;
        r80 = r8;
        r14 = r70;
        r15 = r77;
        r5 = r131;
        r2 = r9;
        r5 = r11;
        r114 = r13;
        r13 = r14;
        r111 = r46;
        r6 = r47;
        r113 = r52;
        r101 = r56;
        r17 = r66;
        r89 = r67;
        r118 = r71;
        r4 = r75;
        r108 = r78;
        r3 = r137;
        r11 = r1;
        r1 = r0;
        goto L_0x29e0;
        r75 = r4;
        r80 = r8;
        r79 = r9;
        r76 = r15;
        r13 = r27;
        r78 = r50;
        r1 = r51;
        r14 = r70;
        r15 = r3;
        r3 = r52;
        r8 = r3.access_hash;	 Catch:{ Exception -> 0x1af1 }
        r2 = (r8 > r19 ? 1 : (r8 == r19 ? 0 : -1));
        if (r2 != 0) goto L_0x1527;
        r2 = new org.telegram.tgnet.TLRPC$TL_inputMediaUploadedPhoto;	 Catch:{ Exception -> 0x1503 }
        r2.<init>();	 Catch:{ Exception -> 0x1503 }
        if (r10 == 0) goto L_0x1433;
        r2.ttl_seconds = r10;	 Catch:{ Exception -> 0x1416 }
        r12.ttl = r10;	 Catch:{ Exception -> 0x1416 }
        r4 = r2.flags;	 Catch:{ Exception -> 0x1416 }
        r8 = 2;	 Catch:{ Exception -> 0x1416 }
        r4 = r4 | r8;	 Catch:{ Exception -> 0x1416 }
        r2.flags = r4;	 Catch:{ Exception -> 0x1416 }
        goto L_0x1434;
    L_0x1416:
        r0 = move-exception;
        r113 = r3;
        r5 = r11;
        r114 = r13;
        r13 = r14;
        r111 = r46;
        r6 = r47;
        r101 = r56;
        r17 = r66;
        r89 = r67;
        r118 = r71;
        r4 = r75;
        r108 = r78;
        r2 = r79;
        r8 = r80;
        goto L_0x10cc;
        r8 = 2;
        if (r14 == 0) goto L_0x1483;
        r4 = "masks";	 Catch:{ Exception -> 0x1503 }
        r4 = r14.get(r4);	 Catch:{ Exception -> 0x1503 }
        r4 = (java.lang.String) r4;	 Catch:{ Exception -> 0x1503 }
        if (r4 == 0) goto L_0x1483;	 Catch:{ Exception -> 0x1503 }
        r9 = new org.telegram.tgnet.SerializedData;	 Catch:{ Exception -> 0x1503 }
        r8 = org.telegram.messenger.Utilities.hexToBytes(r4);	 Catch:{ Exception -> 0x1503 }
        r9.<init>(r8);	 Catch:{ Exception -> 0x1503 }
        r8 = r9;	 Catch:{ Exception -> 0x1503 }
        r9 = 0;	 Catch:{ Exception -> 0x1503 }
        r17 = r8.readInt32(r9);	 Catch:{ Exception -> 0x1503 }
        r9 = r17;
        r17 = 0;
        r81 = r17;
        r82 = r1;
        r1 = r81;
        if (r1 >= r9) goto L_0x1478;
        r83 = r4;
        r4 = r2.stickers;	 Catch:{ Exception -> 0x1494 }
        r84 = r9;	 Catch:{ Exception -> 0x1494 }
        r9 = 0;	 Catch:{ Exception -> 0x1494 }
        r10 = r8.readInt32(r9);	 Catch:{ Exception -> 0x1494 }
        r10 = org.telegram.tgnet.TLRPC.InputDocument.TLdeserialize(r8, r10, r9);	 Catch:{ Exception -> 0x1494 }
        r4.add(r10);	 Catch:{ Exception -> 0x1494 }
        r17 = r1 + 1;	 Catch:{ Exception -> 0x1494 }
        r1 = r82;	 Catch:{ Exception -> 0x1494 }
        r4 = r83;	 Catch:{ Exception -> 0x1494 }
        r9 = r84;	 Catch:{ Exception -> 0x1494 }
        r10 = r141;	 Catch:{ Exception -> 0x1494 }
        goto L_0x1453;	 Catch:{ Exception -> 0x1494 }
        r83 = r4;	 Catch:{ Exception -> 0x1494 }
        r84 = r9;	 Catch:{ Exception -> 0x1494 }
        r1 = r2.flags;	 Catch:{ Exception -> 0x1494 }
        r4 = 1;	 Catch:{ Exception -> 0x1494 }
        r1 = r1 | r4;	 Catch:{ Exception -> 0x1494 }
        r2.flags = r1;	 Catch:{ Exception -> 0x1494 }
        goto L_0x1485;	 Catch:{ Exception -> 0x1494 }
        r82 = r1;	 Catch:{ Exception -> 0x1494 }
        if (r25 != 0) goto L_0x14b4;	 Catch:{ Exception -> 0x1494 }
        r1 = new org.telegram.messenger.SendMessagesHelper$DelayedMessage;	 Catch:{ Exception -> 0x1494 }
        r1.<init>(r5);	 Catch:{ Exception -> 0x1494 }
        r4 = 0;
        r1.type = r4;	 Catch:{ Exception -> 0x14cb }
        r1.obj = r15;	 Catch:{ Exception -> 0x14cb }
        r1.originalPath = r13;	 Catch:{ Exception -> 0x14cb }
        goto L_0x14b6;	 Catch:{ Exception -> 0x14cb }
    L_0x1494:
        r0 = move-exception;	 Catch:{ Exception -> 0x14cb }
        r1 = r0;	 Catch:{ Exception -> 0x14cb }
        r113 = r3;	 Catch:{ Exception -> 0x14cb }
        r5 = r11;	 Catch:{ Exception -> 0x14cb }
        r114 = r13;	 Catch:{ Exception -> 0x14cb }
        r13 = r14;	 Catch:{ Exception -> 0x14cb }
        r111 = r46;	 Catch:{ Exception -> 0x14cb }
        r6 = r47;	 Catch:{ Exception -> 0x14cb }
        r101 = r56;	 Catch:{ Exception -> 0x14cb }
        r17 = r66;	 Catch:{ Exception -> 0x14cb }
        r89 = r67;	 Catch:{ Exception -> 0x14cb }
        r118 = r71;	 Catch:{ Exception -> 0x14cb }
        r4 = r75;	 Catch:{ Exception -> 0x14cb }
        r108 = r78;	 Catch:{ Exception -> 0x14cb }
        r2 = r79;	 Catch:{ Exception -> 0x14cb }
        r8 = r80;	 Catch:{ Exception -> 0x14cb }
        r11 = r82;	 Catch:{ Exception -> 0x14cb }
        goto L_0x0955;	 Catch:{ Exception -> 0x14cb }
        r1 = r25;	 Catch:{ Exception -> 0x14cb }
        r8 = r133;	 Catch:{ Exception -> 0x14cb }
        if (r8 == 0) goto L_0x14ec;	 Catch:{ Exception -> 0x14cb }
        r4 = r133.length();	 Catch:{ Exception -> 0x14cb }
        if (r4 <= 0) goto L_0x14ec;	 Catch:{ Exception -> 0x14cb }
        r4 = "http";	 Catch:{ Exception -> 0x14cb }
        r4 = r8.startsWith(r4);	 Catch:{ Exception -> 0x14cb }
        if (r4 == 0) goto L_0x14ec;	 Catch:{ Exception -> 0x14cb }
        r1.httpLocation = r8;	 Catch:{ Exception -> 0x14cb }
        goto L_0x1500;	 Catch:{ Exception -> 0x14cb }
    L_0x14cb:
        r0 = move-exception;	 Catch:{ Exception -> 0x14cb }
        r25 = r1;	 Catch:{ Exception -> 0x14cb }
        r113 = r3;	 Catch:{ Exception -> 0x14cb }
        r5 = r11;	 Catch:{ Exception -> 0x14cb }
        r114 = r13;	 Catch:{ Exception -> 0x14cb }
        r13 = r14;	 Catch:{ Exception -> 0x14cb }
        r111 = r46;	 Catch:{ Exception -> 0x14cb }
        r6 = r47;	 Catch:{ Exception -> 0x14cb }
        r101 = r56;	 Catch:{ Exception -> 0x14cb }
        r17 = r66;	 Catch:{ Exception -> 0x14cb }
        r89 = r67;	 Catch:{ Exception -> 0x14cb }
        r118 = r71;	 Catch:{ Exception -> 0x14cb }
        r4 = r75;	 Catch:{ Exception -> 0x14cb }
        r108 = r78;	 Catch:{ Exception -> 0x14cb }
        r2 = r79;	 Catch:{ Exception -> 0x14cb }
        r8 = r80;	 Catch:{ Exception -> 0x14cb }
        r11 = r82;	 Catch:{ Exception -> 0x14cb }
        goto L_0x0a37;	 Catch:{ Exception -> 0x14cb }
        r4 = r3.sizes;	 Catch:{ Exception -> 0x14cb }
        r9 = r3.sizes;	 Catch:{ Exception -> 0x14cb }
        r9 = r9.size();	 Catch:{ Exception -> 0x14cb }
        r10 = 1;	 Catch:{ Exception -> 0x14cb }
        r9 = r9 - r10;	 Catch:{ Exception -> 0x14cb }
        r4 = r4.get(r9);	 Catch:{ Exception -> 0x14cb }
        r4 = (org.telegram.tgnet.TLRPC.PhotoSize) r4;	 Catch:{ Exception -> 0x14cb }
        r4 = r4.location;	 Catch:{ Exception -> 0x14cb }
        r1.location = r4;	 Catch:{ Exception -> 0x14cb }
        r9 = r1;
        r10 = r2;
        goto L_0x1548;
    L_0x1503:
        r0 = move-exception;
        r82 = r1;
        r1 = r0;
        r113 = r3;
        r5 = r11;
        r114 = r13;
        r13 = r14;
        r111 = r46;
        r6 = r47;
        r101 = r56;
        r17 = r66;
        r89 = r67;
        r118 = r71;
        r4 = r75;
        r108 = r78;
        r2 = r79;
        r8 = r80;
        r11 = r82;
        r3 = r137;
        goto L_0x29e0;
        r82 = r1;
        r8 = r133;
        r1 = new org.telegram.tgnet.TLRPC$TL_inputMediaPhoto;	 Catch:{ Exception -> 0x1abc }
        r1.<init>();	 Catch:{ Exception -> 0x1abc }
        r2 = new org.telegram.tgnet.TLRPC$TL_inputPhoto;	 Catch:{ Exception -> 0x1abc }
        r2.<init>();	 Catch:{ Exception -> 0x1abc }
        r1.id = r2;	 Catch:{ Exception -> 0x1abc }
        r2 = r1.id;	 Catch:{ Exception -> 0x1abc }
        r9 = r3.id;	 Catch:{ Exception -> 0x1abc }
        r2.id = r9;	 Catch:{ Exception -> 0x1abc }
        r2 = r1.id;	 Catch:{ Exception -> 0x1abc }
        r9 = r3.access_hash;	 Catch:{ Exception -> 0x1abc }
        r2.access_hash = r9;	 Catch:{ Exception -> 0x1abc }
        r10 = r1;
        r9 = r25;
        if (r67 == 0) goto L_0x1626;
        r1 = new org.telegram.tgnet.TLRPC$TL_messages_sendBroadcast;	 Catch:{ Exception -> 0x15fa }
        r1.<init>();	 Catch:{ Exception -> 0x15fa }
        r2 = new java.util.ArrayList;	 Catch:{ Exception -> 0x15fa }
        r2.<init>();	 Catch:{ Exception -> 0x15fa }
        r4 = 0;
        r85 = r11;
        r11 = r67;
        r8 = r11.size();	 Catch:{ Exception -> 0x15d2 }
        if (r4 >= r8) goto L_0x157d;	 Catch:{ Exception -> 0x15d2 }
        r8 = org.telegram.messenger.Utilities.random;	 Catch:{ Exception -> 0x15d2 }
        r86 = r13;
        r87 = r14;
        r13 = r8.nextLong();	 Catch:{ Exception -> 0x15ae }
        r8 = java.lang.Long.valueOf(r13);	 Catch:{ Exception -> 0x15ae }
        r2.add(r8);	 Catch:{ Exception -> 0x15ae }
        r4 = r4 + 1;	 Catch:{ Exception -> 0x15ae }
        r67 = r11;	 Catch:{ Exception -> 0x15ae }
        r11 = r85;	 Catch:{ Exception -> 0x15ae }
        r13 = r86;	 Catch:{ Exception -> 0x15ae }
        r14 = r87;	 Catch:{ Exception -> 0x15ae }
        r8 = r133;	 Catch:{ Exception -> 0x15ae }
        goto L_0x1555;	 Catch:{ Exception -> 0x15ae }
        r86 = r13;	 Catch:{ Exception -> 0x15ae }
        r87 = r14;	 Catch:{ Exception -> 0x15ae }
        r1.contacts = r11;	 Catch:{ Exception -> 0x15ae }
        r1.media = r10;	 Catch:{ Exception -> 0x15ae }
        r1.random_id = r2;	 Catch:{ Exception -> 0x15ae }
        r4 = "";	 Catch:{ Exception -> 0x15ae }
        r1.message = r4;	 Catch:{ Exception -> 0x15ae }
        if (r9 == 0) goto L_0x158f;	 Catch:{ Exception -> 0x15ae }
        r9.sendRequest = r1;	 Catch:{ Exception -> 0x15ae }
        r4 = r1;
        r8 = r137;
        r14 = 2;
        if (r8 != 0) goto L_0x159f;
        r13 = r7.currentAccount;	 Catch:{ Exception -> 0x1640 }
        r13 = org.telegram.messenger.DataQuery.getInstance(r13);	 Catch:{ Exception -> 0x1640 }
        r14 = 0;	 Catch:{ Exception -> 0x1640 }
        r13.cleanDraft(r5, r14);	 Catch:{ Exception -> 0x1640 }
        r89 = r11;
        r14 = r46;
        r90 = r71;
        r11 = r82;
        r88 = r86;
        r13 = r138;
        goto L_0x18b0;
    L_0x15ae:
        r0 = move-exception;
        r1 = r0;
        r113 = r3;
        r25 = r9;
        r89 = r11;
        r111 = r46;
        r6 = r47;
        r101 = r56;
        r17 = r66;
        r118 = r71;
        r4 = r75;
        r108 = r78;
        r2 = r79;
        r8 = r80;
        r11 = r82;
        r5 = r85;
        r114 = r86;
        r13 = r87;
        goto L_0x0955;
    L_0x15d2:
        r0 = move-exception;
        r87 = r14;
        r1 = r0;
        r113 = r3;
        r25 = r9;
        r89 = r11;
        r114 = r13;
        r111 = r46;
        r6 = r47;
        r101 = r56;
        r17 = r66;
        r118 = r71;
        r4 = r75;
        r108 = r78;
        r2 = r79;
        r8 = r80;
        r11 = r82;
        r5 = r85;
        r13 = r87;
        r3 = r137;
        goto L_0x29e0;
    L_0x15fa:
        r0 = move-exception;
        r85 = r11;
        r87 = r14;
        r11 = r67;
        r1 = r0;
        r113 = r3;
        r25 = r9;
        r89 = r11;
        r114 = r13;
        r111 = r46;
        r6 = r47;
        r101 = r56;
        r17 = r66;
        r118 = r71;
        r4 = r75;
        r108 = r78;
        r2 = r79;
        r8 = r80;
        r11 = r82;
        r5 = r85;
        r13 = r87;
        r3 = r137;
        goto L_0x29e0;
        r85 = r11;
        r86 = r13;
        r87 = r14;
        r11 = r67;
        r8 = r137;
        r1 = (r72 > r19 ? 1 : (r72 == r19 ? 0 : -1));
        if (r1 == 0) goto L_0x1804;
        r1 = r9.sendRequest;	 Catch:{ Exception -> 0x17dd }
        if (r1 == 0) goto L_0x1665;
        r1 = r9.sendRequest;	 Catch:{ Exception -> 0x1640 }
        r1 = (org.telegram.tgnet.TLRPC.TL_messages_sendMultiMedia) r1;	 Catch:{ Exception -> 0x1640 }
        r2 = r71;
        goto L_0x16b7;
    L_0x1640:
        r0 = move-exception;
        r1 = r0;
        r113 = r3;
        r3 = r8;
        r25 = r9;
        r89 = r11;
        r111 = r46;
        r6 = r47;
        r101 = r56;
        r17 = r66;
        r118 = r71;
        r4 = r75;
        r108 = r78;
        r2 = r79;
        r8 = r80;
        r11 = r82;
        r5 = r85;
        r114 = r86;
        r13 = r87;
        goto L_0x29e0;
        r1 = new org.telegram.tgnet.TLRPC$TL_messages_sendMultiMedia;	 Catch:{ Exception -> 0x17dd }
        r1.<init>();	 Catch:{ Exception -> 0x17dd }
        r2 = r71;
        r1.peer = r2;	 Catch:{ Exception -> 0x17b6 }
        r4 = r12.to_id;	 Catch:{ Exception -> 0x17b6 }
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_peerChannel;	 Catch:{ Exception -> 0x17b6 }
        if (r4 == 0) goto L_0x16a7;
        r4 = r7.currentAccount;	 Catch:{ Exception -> 0x1693 }
        r4 = org.telegram.messenger.MessagesController.getNotificationsSettings(r4);	 Catch:{ Exception -> 0x1693 }
        r13 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x1693 }
        r13.<init>();	 Catch:{ Exception -> 0x1693 }
        r14 = "silent_";	 Catch:{ Exception -> 0x1693 }
        r13.append(r14);	 Catch:{ Exception -> 0x1693 }
        r13.append(r5);	 Catch:{ Exception -> 0x1693 }
        r13 = r13.toString();	 Catch:{ Exception -> 0x1693 }
        r14 = 0;	 Catch:{ Exception -> 0x1693 }
        r4 = r4.getBoolean(r13, r14);	 Catch:{ Exception -> 0x1693 }
        r1.silent = r4;	 Catch:{ Exception -> 0x1693 }
        goto L_0x16a7;
    L_0x1693:
        r0 = move-exception;
        r1 = r0;
        r118 = r2;
        r113 = r3;
        r3 = r8;
        r25 = r9;
        r89 = r11;
        r111 = r46;
        r6 = r47;
        r101 = r56;
        r17 = r66;
        goto L_0x1653;
        r4 = r12.reply_to_msg_id;	 Catch:{ Exception -> 0x17b6 }
        if (r4 == 0) goto L_0x16b5;
        r4 = r1.flags;	 Catch:{ Exception -> 0x1693 }
        r13 = 1;	 Catch:{ Exception -> 0x1693 }
        r4 = r4 | r13;	 Catch:{ Exception -> 0x1693 }
        r1.flags = r4;	 Catch:{ Exception -> 0x1693 }
        r4 = r12.reply_to_msg_id;	 Catch:{ Exception -> 0x1693 }
        r1.reply_to_msg_id = r4;	 Catch:{ Exception -> 0x1693 }
        r9.sendRequest = r1;	 Catch:{ Exception -> 0x17b6 }
        r4 = r9.messageObjects;	 Catch:{ Exception -> 0x17b6 }
        r4.add(r15);	 Catch:{ Exception -> 0x17b6 }
        r4 = r9.messages;	 Catch:{ Exception -> 0x17b6 }
        r4.add(r12);	 Catch:{ Exception -> 0x17b6 }
        r4 = r9.originalPaths;	 Catch:{ Exception -> 0x17b6 }
        r13 = r86;
        r4.add(r13);	 Catch:{ Exception -> 0x178d }
        r4 = new org.telegram.tgnet.TLRPC$TL_inputSingleMedia;	 Catch:{ Exception -> 0x178d }
        r4.<init>();	 Catch:{ Exception -> 0x178d }
        r88 = r13;
        r13 = r12.random_id;	 Catch:{ Exception -> 0x1766 }
        r4.random_id = r13;	 Catch:{ Exception -> 0x1766 }
        r4.media = r10;	 Catch:{ Exception -> 0x1766 }
        r14 = r46;
        r4.message = r14;	 Catch:{ Exception -> 0x173f }
        r89 = r11;
        r11 = r82;
        r13 = r138;
        if (r13 == 0) goto L_0x1713;
        r17 = r138.isEmpty();	 Catch:{ Exception -> 0x16f2 }
        if (r17 != 0) goto L_0x1713;	 Catch:{ Exception -> 0x16f2 }
        r4.entities = r13;	 Catch:{ Exception -> 0x16f2 }
        r8 = r4.flags;	 Catch:{ Exception -> 0x171f }
        r17 = 1;	 Catch:{ Exception -> 0x171f }
        r8 = r8 | 1;	 Catch:{ Exception -> 0x171f }
        r4.flags = r8;	 Catch:{ Exception -> 0x171f }
        goto L_0x1713;	 Catch:{ Exception -> 0x171f }
    L_0x16f2:
        r0 = move-exception;	 Catch:{ Exception -> 0x171f }
        r1 = r0;	 Catch:{ Exception -> 0x171f }
        r118 = r2;	 Catch:{ Exception -> 0x171f }
        r113 = r3;	 Catch:{ Exception -> 0x171f }
        r3 = r8;	 Catch:{ Exception -> 0x171f }
        r25 = r9;	 Catch:{ Exception -> 0x171f }
        r111 = r14;	 Catch:{ Exception -> 0x171f }
        r6 = r47;	 Catch:{ Exception -> 0x171f }
        r101 = r56;	 Catch:{ Exception -> 0x171f }
        r17 = r66;	 Catch:{ Exception -> 0x171f }
        r4 = r75;	 Catch:{ Exception -> 0x171f }
        r108 = r78;	 Catch:{ Exception -> 0x171f }
        r2 = r79;	 Catch:{ Exception -> 0x171f }
        r8 = r80;	 Catch:{ Exception -> 0x171f }
        r5 = r85;	 Catch:{ Exception -> 0x171f }
        r13 = r87;	 Catch:{ Exception -> 0x171f }
        r114 = r88;	 Catch:{ Exception -> 0x171f }
        goto L_0x29e0;	 Catch:{ Exception -> 0x171f }
        r8 = r1.multi_media;	 Catch:{ Exception -> 0x171f }
        r8.add(r4);	 Catch:{ Exception -> 0x171f }
        r4 = r1;
        r90 = r2;
        goto L_0x18b0;
    L_0x171f:
        r0 = move-exception;
        r1 = r0;
        r118 = r2;
        r113 = r3;
        r25 = r9;
        r111 = r14;
        r6 = r47;
        r101 = r56;
        r17 = r66;
        r4 = r75;
        r108 = r78;
        r2 = r79;
        r8 = r80;
        r5 = r85;
        r13 = r87;
        r114 = r88;
        goto L_0x0955;
    L_0x173f:
        r0 = move-exception;
        r89 = r11;
        r11 = r82;
        r13 = r138;
        r1 = r0;
        r118 = r2;
        r113 = r3;
        r3 = r8;
        r25 = r9;
        r111 = r14;
        r6 = r47;
        r101 = r56;
        r17 = r66;
        r4 = r75;
        r108 = r78;
        r2 = r79;
        r8 = r80;
        r5 = r85;
        r13 = r87;
        r114 = r88;
        goto L_0x29e0;
    L_0x1766:
        r0 = move-exception;
        r89 = r11;
        r11 = r82;
        r13 = r138;
        r1 = r0;
        r118 = r2;
        r113 = r3;
        r3 = r8;
        r25 = r9;
        r111 = r46;
        r6 = r47;
        r101 = r56;
        r17 = r66;
        r4 = r75;
        r108 = r78;
        r2 = r79;
        r8 = r80;
        r5 = r85;
        r13 = r87;
        r114 = r88;
        goto L_0x29e0;
    L_0x178d:
        r0 = move-exception;
        r89 = r11;
        r88 = r13;
        r11 = r82;
        r13 = r138;
        r1 = r0;
        r118 = r2;
        r113 = r3;
        r3 = r8;
        r25 = r9;
        r111 = r46;
        r6 = r47;
        r101 = r56;
        r17 = r66;
        r4 = r75;
        r108 = r78;
        r2 = r79;
        r8 = r80;
        r5 = r85;
        r13 = r87;
        r114 = r88;
        goto L_0x29e0;
    L_0x17b6:
        r0 = move-exception;
        r89 = r11;
        r11 = r82;
        r13 = r138;
        r1 = r0;
        r118 = r2;
        r113 = r3;
        r3 = r8;
        r25 = r9;
        r111 = r46;
        r6 = r47;
        r101 = r56;
        r17 = r66;
        r4 = r75;
        r108 = r78;
        r2 = r79;
        r8 = r80;
        r5 = r85;
        r114 = r86;
        r13 = r87;
        goto L_0x29e0;
    L_0x17dd:
        r0 = move-exception;
        r89 = r11;
        r11 = r82;
        r13 = r138;
        r1 = r0;
        r113 = r3;
        r3 = r8;
        r25 = r9;
        r111 = r46;
        r6 = r47;
        r101 = r56;
        r17 = r66;
        r118 = r71;
        r4 = r75;
        r108 = r78;
        r2 = r79;
        r8 = r80;
        r5 = r85;
        r114 = r86;
        r13 = r87;
        goto L_0x29e0;
        r89 = r11;
        r14 = r46;
        r2 = r71;
        r11 = r82;
        r88 = r86;
        r13 = r138;
        r1 = new org.telegram.tgnet.TLRPC$TL_messages_sendMedia;	 Catch:{ Exception -> 0x1a8e }
        r1.<init>();	 Catch:{ Exception -> 0x1a8e }
        r4 = r1;	 Catch:{ Exception -> 0x1a8e }
        r4.peer = r2;	 Catch:{ Exception -> 0x1a8e }
        r1 = r12.to_id;	 Catch:{ Exception -> 0x1a8e }
        r1 = r1 instanceof org.telegram.tgnet.TLRPC.TL_peerChannel;	 Catch:{ Exception -> 0x1a8e }
        if (r1 == 0) goto L_0x1861;
        r1 = r7.currentAccount;	 Catch:{ Exception -> 0x183f }
        r1 = org.telegram.messenger.MessagesController.getNotificationsSettings(r1);	 Catch:{ Exception -> 0x183f }
        r8 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x183f }
        r8.<init>();	 Catch:{ Exception -> 0x183f }
        r90 = r2;
        r2 = "silent_";	 Catch:{ Exception -> 0x1872 }
        r8.append(r2);	 Catch:{ Exception -> 0x1872 }
        r8.append(r5);	 Catch:{ Exception -> 0x1872 }
        r2 = r8.toString();	 Catch:{ Exception -> 0x1872 }
        r8 = 0;	 Catch:{ Exception -> 0x1872 }
        r1 = r1.getBoolean(r2, r8);	 Catch:{ Exception -> 0x1872 }
        r4.silent = r1;	 Catch:{ Exception -> 0x1872 }
        goto L_0x1863;
    L_0x183f:
        r0 = move-exception;
        r1 = r0;
        r118 = r2;
        r113 = r3;
        r25 = r9;
        r111 = r14;
        r6 = r47;
        r101 = r56;
        r17 = r66;
        r4 = r75;
        r108 = r78;
        r2 = r79;
        r8 = r80;
        r5 = r85;
        r13 = r87;
        r114 = r88;
        r3 = r137;
        goto L_0x29e0;
        r90 = r2;
        r1 = r12.reply_to_msg_id;	 Catch:{ Exception -> 0x1a60 }
        if (r1 == 0) goto L_0x1892;
        r1 = r4.flags;	 Catch:{ Exception -> 0x1872 }
        r2 = 1;	 Catch:{ Exception -> 0x1872 }
        r1 = r1 | r2;	 Catch:{ Exception -> 0x1872 }
        r4.flags = r1;	 Catch:{ Exception -> 0x1872 }
        r1 = r12.reply_to_msg_id;	 Catch:{ Exception -> 0x1872 }
        r4.reply_to_msg_id = r1;	 Catch:{ Exception -> 0x1872 }
        goto L_0x1892;
    L_0x1872:
        r0 = move-exception;
        r1 = r0;
        r113 = r3;
        r25 = r9;
        r111 = r14;
        r6 = r47;
        r101 = r56;
        r17 = r66;
        r4 = r75;
        r108 = r78;
        r2 = r79;
        r8 = r80;
        r5 = r85;
        r13 = r87;
        r114 = r88;
        r118 = r90;
        goto L_0x0955;
        r1 = r12.random_id;	 Catch:{ Exception -> 0x1a60 }
        r4.random_id = r1;	 Catch:{ Exception -> 0x1a60 }
        r4.media = r10;	 Catch:{ Exception -> 0x1a60 }
        r4.message = r14;	 Catch:{ Exception -> 0x1a60 }
        if (r13 == 0) goto L_0x18ab;
        r1 = r138.isEmpty();	 Catch:{ Exception -> 0x1872 }
        if (r1 != 0) goto L_0x18ab;	 Catch:{ Exception -> 0x1872 }
        r4.entities = r13;	 Catch:{ Exception -> 0x1872 }
        r1 = r4.flags;	 Catch:{ Exception -> 0x1872 }
        r2 = 8;	 Catch:{ Exception -> 0x1872 }
        r1 = r1 | r2;	 Catch:{ Exception -> 0x1872 }
        r4.flags = r1;	 Catch:{ Exception -> 0x1872 }
        if (r9 == 0) goto L_0x18af;	 Catch:{ Exception -> 0x1872 }
        r9.sendRequest = r4;	 Catch:{ Exception -> 0x1872 }
        r8 = r4;	 Catch:{ Exception -> 0x1872 }
        r1 = (r72 > r19 ? 1 : (r72 == r19 ? 0 : -1));	 Catch:{ Exception -> 0x1872 }
        if (r1 == 0) goto L_0x18d1;	 Catch:{ Exception -> 0x1872 }
        r7.performSendDelayedMessage(r9);	 Catch:{ Exception -> 0x1872 }
        r96 = r3;
        r1 = r15;
        r15 = r47;
        r101 = r56;
        r17 = r66;
        r91 = r72;
        r98 = r75;
        r94 = r78;
        r5 = r80;
        r99 = r85;
        r10 = r88;
        r93 = r90;
        goto L_0x1a3c;
        r4 = r80;
        r1 = 1;
        if (r4 != r1) goto L_0x1901;
        r1 = 0;
        r7.performSendMessageRequest(r8, r15, r1);	 Catch:{ Exception -> 0x18eb }
        r96 = r3;
        r5 = r4;
        r1 = r15;
        r15 = r47;
        r101 = r56;
        r17 = r66;
        r91 = r72;
        r98 = r75;
        r94 = r78;
        goto L_0x18c9;
    L_0x18eb:
        r0 = move-exception;
        r1 = r0;
        r113 = r3;
        r8 = r4;
        r25 = r9;
        r111 = r14;
        r6 = r47;
        r101 = r56;
        r17 = r66;
        r4 = r75;
        r108 = r78;
        r2 = r79;
        goto L_0x1888;
        r1 = 2;
        if (r4 != r1) goto L_0x1996;
        r1 = r3.access_hash;	 Catch:{ Exception -> 0x1965 }
        r17 = (r1 > r19 ? 1 : (r1 == r19 ? 0 : -1));
        if (r17 != 0) goto L_0x190e;
        r7.performSendDelayedMessage(r9);	 Catch:{ Exception -> 0x18eb }
        goto L_0x18da;
        r17 = 0;
        r18 = 0;
        r19 = 1;
        r91 = r72;
        r2 = r78;
        r1 = r7;
        r94 = r2;
        r20 = r56;
        r93 = r90;
        r2 = r8;
        r96 = r3;
        r95 = r15;
        r15 = r20;
        r3 = r95;
        r97 = r4;
        r98 = r75;
        r4 = r17;
        r17 = r66;
        r99 = r85;
        r6 = r88;
        r5 = r18;
        r100 = r10;
        r101 = r15;
        r15 = r47;
        r10 = r6;
        r6 = r19;
        r1.performSendMessageRequest(r2, r3, r4, r5, r6);	 Catch:{ Exception -> 0x1948 }
        r1 = r95;
        r5 = r97;
        goto L_0x1a3c;
    L_0x1948:
        r0 = move-exception;
        r1 = r0;
        r25 = r9;
        r114 = r10;
        r111 = r14;
        r6 = r15;
        r2 = r79;
        r13 = r87;
        r118 = r93;
        r108 = r94;
        r15 = r95;
        r113 = r96;
        r8 = r97;
        r4 = r98;
        r5 = r99;
        goto L_0x0955;
    L_0x1965:
        r0 = move-exception;
        r96 = r3;
        r97 = r4;
        r95 = r15;
        r15 = r47;
        r101 = r56;
        r17 = r66;
        r98 = r75;
        r94 = r78;
        r99 = r85;
        r1 = r0;
        r25 = r9;
        r111 = r14;
        r6 = r15;
        r2 = r79;
        r13 = r87;
        r114 = r88;
        r118 = r90;
        r108 = r94;
        r15 = r95;
        r113 = r96;
        r8 = r97;
        r4 = r98;
        r5 = r99;
        r3 = r137;
        goto L_0x29e0;
        r96 = r3;
        r97 = r4;
        r100 = r10;
        r95 = r15;
        r15 = r47;
        r101 = r56;
        r17 = r66;
        r91 = r72;
        r98 = r75;
        r94 = r78;
        r99 = r85;
        r10 = r88;
        r93 = r90;
        r5 = r97;
        r1 = 3;
        if (r5 != r1) goto L_0x1a00;
        r1 = r11.access_hash;	 Catch:{ Exception -> 0x19e1 }
        r3 = (r1 > r19 ? 1 : (r1 == r19 ? 0 : -1));
        if (r3 != 0) goto L_0x19d9;
        r7.performSendDelayedMessage(r9);	 Catch:{ Exception -> 0x19c2 }
        r1 = r95;
        goto L_0x1a3c;
    L_0x19c2:
        r0 = move-exception;
        r1 = r0;
        r8 = r5;
        r25 = r9;
        r114 = r10;
        r111 = r14;
        r6 = r15;
        r2 = r79;
        r13 = r87;
        r118 = r93;
        r108 = r94;
        r15 = r95;
        r113 = r96;
        goto L_0x195f;
        r1 = r95;
        r2 = 0;
        r7.performSendMessageRequest(r8, r1, r2);	 Catch:{ Exception -> 0x1a0a }
        goto L_0x1a3c;	 Catch:{ Exception -> 0x1a0a }
    L_0x19e1:
        r0 = move-exception;	 Catch:{ Exception -> 0x1a0a }
        r1 = r95;	 Catch:{ Exception -> 0x1a0a }
        r8 = r5;	 Catch:{ Exception -> 0x1a0a }
        r25 = r9;	 Catch:{ Exception -> 0x1a0a }
        r114 = r10;	 Catch:{ Exception -> 0x1a0a }
        r111 = r14;	 Catch:{ Exception -> 0x1a0a }
        r6 = r15;	 Catch:{ Exception -> 0x1a0a }
        r2 = r79;	 Catch:{ Exception -> 0x1a0a }
        r13 = r87;	 Catch:{ Exception -> 0x1a0a }
        r118 = r93;	 Catch:{ Exception -> 0x1a0a }
        r108 = r94;	 Catch:{ Exception -> 0x1a0a }
        r113 = r96;	 Catch:{ Exception -> 0x1a0a }
        r4 = r98;	 Catch:{ Exception -> 0x1a0a }
        r5 = r99;	 Catch:{ Exception -> 0x1a0a }
        r3 = r137;	 Catch:{ Exception -> 0x1a0a }
        r15 = r1;	 Catch:{ Exception -> 0x1a0a }
        r1 = r0;	 Catch:{ Exception -> 0x1a0a }
        goto L_0x29e0;	 Catch:{ Exception -> 0x1a0a }
        r1 = r95;	 Catch:{ Exception -> 0x1a0a }
        r2 = 0;	 Catch:{ Exception -> 0x1a0a }
        r3 = 6;	 Catch:{ Exception -> 0x1a0a }
        if (r5 != r3) goto L_0x1a17;	 Catch:{ Exception -> 0x1a0a }
        r7.performSendMessageRequest(r8, r1, r2);	 Catch:{ Exception -> 0x1a0a }
        goto L_0x1a3c;	 Catch:{ Exception -> 0x1a0a }
    L_0x1a0a:
        r0 = move-exception;	 Catch:{ Exception -> 0x1a0a }
        r8 = r5;	 Catch:{ Exception -> 0x1a0a }
        r25 = r9;	 Catch:{ Exception -> 0x1a0a }
        r114 = r10;	 Catch:{ Exception -> 0x1a0a }
        r111 = r14;	 Catch:{ Exception -> 0x1a0a }
        r6 = r15;	 Catch:{ Exception -> 0x1a0a }
        r2 = r79;	 Catch:{ Exception -> 0x1a0a }
        goto L_0x1b6f;	 Catch:{ Exception -> 0x1a0a }
        r2 = 7;	 Catch:{ Exception -> 0x1a0a }
        if (r5 != r2) goto L_0x1a2a;	 Catch:{ Exception -> 0x1a0a }
        r2 = r11.access_hash;	 Catch:{ Exception -> 0x1a0a }
        r4 = (r2 > r19 ? 1 : (r2 == r19 ? 0 : -1));	 Catch:{ Exception -> 0x1a0a }
        if (r4 != 0) goto L_0x1a26;	 Catch:{ Exception -> 0x1a0a }
        if (r9 == 0) goto L_0x1a26;	 Catch:{ Exception -> 0x1a0a }
        r7.performSendDelayedMessage(r9);	 Catch:{ Exception -> 0x1a0a }
        goto L_0x1a3c;	 Catch:{ Exception -> 0x1a0a }
        r7.performSendMessageRequest(r8, r1, r10);	 Catch:{ Exception -> 0x1a0a }
        goto L_0x1a3c;	 Catch:{ Exception -> 0x1a0a }
        r2 = 8;	 Catch:{ Exception -> 0x1a0a }
        if (r5 != r2) goto L_0x1a3c;	 Catch:{ Exception -> 0x1a0a }
        r2 = r11.access_hash;	 Catch:{ Exception -> 0x1a0a }
        r4 = (r2 > r19 ? 1 : (r2 == r19 ? 0 : -1));	 Catch:{ Exception -> 0x1a0a }
        if (r4 != 0) goto L_0x1a38;	 Catch:{ Exception -> 0x1a0a }
        r7.performSendDelayedMessage(r9);	 Catch:{ Exception -> 0x1a0a }
        goto L_0x1a3c;	 Catch:{ Exception -> 0x1a0a }
        r2 = 0;	 Catch:{ Exception -> 0x1a0a }
        r7.performSendMessageRequest(r8, r1, r2);	 Catch:{ Exception -> 0x1a0a }
        r4 = r9;
        r114 = r10;
        r115 = r11;
        r10 = r13;
        r111 = r14;
        r9 = r15;
        r2 = r79;
        r8 = r87;
        r116 = r89;
        r118 = r93;
        r108 = r94;
        r113 = r96;
        r106 = r98;
        r105 = r99;
        r3 = r137;
        r15 = r5;
        r14 = r12;
        r5 = r45;
        r12 = r131;
        goto L_0x283a;
    L_0x1a60:
        r0 = move-exception;
        r96 = r3;
        r1 = r15;
        r15 = r47;
        r101 = r56;
        r17 = r66;
        r98 = r75;
        r94 = r78;
        r5 = r80;
        r99 = r85;
        r8 = r5;
        r25 = r9;
        r111 = r14;
        r6 = r15;
        r2 = r79;
        r13 = r87;
        r114 = r88;
        r118 = r90;
        r108 = r94;
        r113 = r96;
        r4 = r98;
        r5 = r99;
        r3 = r137;
        r15 = r1;
        r1 = r0;
        goto L_0x29e0;
    L_0x1a8e:
        r0 = move-exception;
        r96 = r3;
        r1 = r15;
        r15 = r47;
        r101 = r56;
        r17 = r66;
        r98 = r75;
        r94 = r78;
        r5 = r80;
        r99 = r85;
        r118 = r2;
        r8 = r5;
        r25 = r9;
        r111 = r14;
        r6 = r15;
        r2 = r79;
        r13 = r87;
        r114 = r88;
        r108 = r94;
        r113 = r96;
        r4 = r98;
        r5 = r99;
        r3 = r137;
        r15 = r1;
        r1 = r0;
        goto L_0x29e0;
    L_0x1abc:
        r0 = move-exception;
        r96 = r3;
        r99 = r11;
        r10 = r13;
        r87 = r14;
        r1 = r15;
        r15 = r47;
        r101 = r56;
        r17 = r66;
        r89 = r67;
        r98 = r75;
        r94 = r78;
        r5 = r80;
        r11 = r82;
        r13 = r138;
        r8 = r5;
        r114 = r10;
        r6 = r15;
        r111 = r46;
        r118 = r71;
        r2 = r79;
        r13 = r87;
        r108 = r94;
        r113 = r96;
        r4 = r98;
        r5 = r99;
        r3 = r137;
        r15 = r1;
        r1 = r0;
        goto L_0x29e0;
    L_0x1af1:
        r0 = move-exception;
        r96 = r3;
        r99 = r11;
        r10 = r13;
        r87 = r14;
        r101 = r56;
        r17 = r66;
        r89 = r67;
        r98 = r75;
        r94 = r78;
        r5 = r80;
        r13 = r138;
        r11 = r1;
        r1 = r15;
        r15 = r47;
        r8 = r5;
        r114 = r10;
        r6 = r15;
        r111 = r46;
        r118 = r71;
        r2 = r79;
        r13 = r87;
        r108 = r94;
        r113 = r96;
        r4 = r98;
        r5 = r99;
        r3 = r137;
        r15 = r1;
        r1 = r0;
        goto L_0x29e0;
        r91 = r1;
        r1 = r3;
        r98 = r4;
        r5 = r8;
        r79 = r9;
        r93 = r13;
        r10 = r27;
        r14 = r46;
        r15 = r47;
        r99 = r48;
        r94 = r50;
        r11 = r51;
        r96 = r52;
        r101 = r56;
        r17 = r66;
        r89 = r67;
        r87 = r70;
        r13 = r138;
        r2 = r79;
        r3 = r2.layer;	 Catch:{ Exception -> 0x2403 }
        r3 = org.telegram.messenger.AndroidUtilities.getPeerLayerVersion(r3);	 Catch:{ Exception -> 0x2403 }
        r4 = 73;
        if (r3 < r4) goto L_0x1b7f;
        r3 = new org.telegram.tgnet.TLRPC$TL_decryptedMessage;	 Catch:{ Exception -> 0x1b68 }
        r3.<init>();	 Catch:{ Exception -> 0x1b68 }
        r8 = r91;	 Catch:{ Exception -> 0x1b68 }
        r4 = (r8 > r19 ? 1 : (r8 == r19 ? 0 : -1));	 Catch:{ Exception -> 0x1b68 }
        if (r4 == 0) goto L_0x1b86;	 Catch:{ Exception -> 0x1b68 }
        r3.grouped_id = r8;	 Catch:{ Exception -> 0x1b68 }
        r4 = r3.flags;	 Catch:{ Exception -> 0x1b68 }
        r6 = 131072; // 0x20000 float:1.83671E-40 double:6.47582E-319;	 Catch:{ Exception -> 0x1b68 }
        r4 = r4 | r6;	 Catch:{ Exception -> 0x1b68 }
        r3.flags = r4;	 Catch:{ Exception -> 0x1b68 }
        goto L_0x1b86;
    L_0x1b68:
        r0 = move-exception;
        r8 = r5;
        r114 = r10;
        r111 = r14;
        r6 = r15;
        r13 = r87;
        r118 = r93;
        r108 = r94;
        r113 = r96;
        r4 = r98;
        r5 = r99;
        r3 = r137;
        goto L_0x279b;
        r8 = r91;
        r3 = new org.telegram.tgnet.TLRPC$TL_decryptedMessage_layer45;	 Catch:{ Exception -> 0x2403 }
        r3.<init>();	 Catch:{ Exception -> 0x2403 }
        r4 = r12.ttl;	 Catch:{ Exception -> 0x2403 }
        r3.ttl = r4;	 Catch:{ Exception -> 0x2403 }
        if (r13 == 0) goto L_0x1b9a;
        r4 = r138.isEmpty();	 Catch:{ Exception -> 0x1b68 }
        if (r4 != 0) goto L_0x1b9a;	 Catch:{ Exception -> 0x1b68 }
        r3.entities = r13;	 Catch:{ Exception -> 0x1b68 }
        r4 = r3.flags;	 Catch:{ Exception -> 0x1b68 }
        r4 = r4 | 128;	 Catch:{ Exception -> 0x1b68 }
        r3.flags = r4;	 Catch:{ Exception -> 0x1b68 }
        r102 = r8;
        r8 = r12.reply_to_random_id;	 Catch:{ Exception -> 0x2403 }
        r4 = (r8 > r19 ? 1 : (r8 == r19 ? 0 : -1));
        if (r4 == 0) goto L_0x1bad;
        r8 = r12.reply_to_random_id;	 Catch:{ Exception -> 0x1b68 }
        r3.reply_to_random_id = r8;	 Catch:{ Exception -> 0x1b68 }
        r4 = r3.flags;	 Catch:{ Exception -> 0x1b68 }
        r6 = 8;	 Catch:{ Exception -> 0x1b68 }
        r4 = r4 | r6;	 Catch:{ Exception -> 0x1b68 }
        r3.flags = r4;	 Catch:{ Exception -> 0x1b68 }
        r4 = r3.flags;	 Catch:{ Exception -> 0x2403 }
        r4 = r4 | 512;	 Catch:{ Exception -> 0x2403 }
        r3.flags = r4;	 Catch:{ Exception -> 0x2403 }
        if (r87 == 0) goto L_0x1bf3;
        r4 = "bot_name";	 Catch:{ Exception -> 0x1bd9 }
        r6 = r87;
        r4 = r6.get(r4);	 Catch:{ Exception -> 0x1bd0 }
        if (r4 == 0) goto L_0x1bf5;	 Catch:{ Exception -> 0x1bd0 }
        r4 = "bot_name";	 Catch:{ Exception -> 0x1bd0 }
        r4 = r6.get(r4);	 Catch:{ Exception -> 0x1bd0 }
        r4 = (java.lang.String) r4;	 Catch:{ Exception -> 0x1bd0 }
        r3.via_bot_name = r4;	 Catch:{ Exception -> 0x1bd0 }
        r4 = r3.flags;	 Catch:{ Exception -> 0x1bd0 }
        r4 = r4 | 2048;	 Catch:{ Exception -> 0x1bd0 }
        r3.flags = r4;	 Catch:{ Exception -> 0x1bd0 }
        goto L_0x1bf5;
    L_0x1bd0:
        r0 = move-exception;
        r8 = r5;
        r13 = r6;
        r114 = r10;
        r111 = r14;
        r6 = r15;
        goto L_0x1b71;
    L_0x1bd9:
        r0 = move-exception;
        r6 = r87;
        r8 = r5;
        r13 = r6;
        r114 = r10;
        r111 = r14;
        r6 = r15;
        r118 = r93;
        r108 = r94;
        r113 = r96;
        r4 = r98;
        r5 = r99;
        r3 = r137;
        r15 = r1;
        r1 = r0;
        goto L_0x29e0;
        r6 = r87;
        r8 = r12.random_id;	 Catch:{ Exception -> 0x23dc }
        r3.random_id = r8;	 Catch:{ Exception -> 0x23dc }
        r4 = "";	 Catch:{ Exception -> 0x23dc }
        r3.message = r4;	 Catch:{ Exception -> 0x23dc }
        r4 = 1;
        if (r5 != r4) goto L_0x1cb0;
        r8 = r99;
        r4 = r8 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaVenue;	 Catch:{ Exception -> 0x1c94 }
        if (r4 == 0) goto L_0x1c40;
        r4 = new org.telegram.tgnet.TLRPC$TL_decryptedMessageMediaVenue;	 Catch:{ Exception -> 0x1c26 }
        r4.<init>();	 Catch:{ Exception -> 0x1c26 }
        r3.media = r4;	 Catch:{ Exception -> 0x1c26 }
        r4 = r3.media;	 Catch:{ Exception -> 0x1c26 }
        r9 = r8.address;	 Catch:{ Exception -> 0x1c26 }
        r4.address = r9;	 Catch:{ Exception -> 0x1c26 }
        r4 = r3.media;	 Catch:{ Exception -> 0x1c26 }
        r9 = r8.title;	 Catch:{ Exception -> 0x1c26 }
        r4.title = r9;	 Catch:{ Exception -> 0x1c26 }
        r4 = r3.media;	 Catch:{ Exception -> 0x1c26 }
        r9 = r8.provider;	 Catch:{ Exception -> 0x1c26 }
        r4.provider = r9;	 Catch:{ Exception -> 0x1c26 }
        r4 = r3.media;	 Catch:{ Exception -> 0x1c26 }
        r9 = r8.venue_id;	 Catch:{ Exception -> 0x1c26 }
        r4.venue_id = r9;	 Catch:{ Exception -> 0x1c26 }
        goto L_0x1c47;
    L_0x1c26:
        r0 = move-exception;
        r13 = r6;
        r114 = r10;
        r111 = r14;
        r6 = r15;
        r118 = r93;
        r108 = r94;
        r113 = r96;
        r4 = r98;
        r3 = r137;
        r15 = r1;
        r1 = r0;
        r120 = r8;
        r8 = r5;
        r5 = r120;
        goto L_0x29e0;
        r4 = new org.telegram.tgnet.TLRPC$TL_decryptedMessageMediaGeoPoint;	 Catch:{ Exception -> 0x1c94 }
        r4.<init>();	 Catch:{ Exception -> 0x1c94 }
        r3.media = r4;	 Catch:{ Exception -> 0x1c94 }
        r4 = r3.media;	 Catch:{ Exception -> 0x1c94 }
        r9 = r8.geo;	 Catch:{ Exception -> 0x1c94 }
        r104 = r12;
        r12 = r9.lat;	 Catch:{ Exception -> 0x1c82 }
        r4.lat = r12;	 Catch:{ Exception -> 0x1c82 }
        r4 = r3.media;	 Catch:{ Exception -> 0x1c82 }
        r9 = r8.geo;	 Catch:{ Exception -> 0x1c82 }
        r12 = r9._long;	 Catch:{ Exception -> 0x1c82 }
        r4._long = r12;	 Catch:{ Exception -> 0x1c82 }
        r4 = r7.currentAccount;	 Catch:{ Exception -> 0x1c82 }
        r36 = org.telegram.messenger.SecretChatHelper.getInstance(r4);	 Catch:{ Exception -> 0x1c82 }
        r4 = r1.messageOwner;	 Catch:{ Exception -> 0x1c82 }
        r40 = 0;	 Catch:{ Exception -> 0x1c82 }
        r41 = 0;	 Catch:{ Exception -> 0x1c82 }
        r37 = r3;	 Catch:{ Exception -> 0x1c82 }
        r38 = r4;	 Catch:{ Exception -> 0x1c82 }
        r39 = r2;	 Catch:{ Exception -> 0x1c82 }
        r42 = r1;	 Catch:{ Exception -> 0x1c82 }
        r36.performSendEncryptedRequest(r37, r38, r39, r40, r41, r42);	 Catch:{ Exception -> 0x1c82 }
        r110 = r5;
        r109 = r6;
        r105 = r8;
        r107 = r15;
        r108 = r94;
        r9 = r96;
        r106 = r98;
        r12 = r131;
        goto L_0x216d;
    L_0x1c82:
        r0 = move-exception;
        r13 = r6;
        r114 = r10;
        r111 = r14;
        r6 = r15;
        r118 = r93;
        r108 = r94;
        r113 = r96;
        r4 = r98;
        r12 = r104;
        goto L_0x1c35;
    L_0x1c94:
        r0 = move-exception;
        r104 = r12;
        r13 = r6;
        r114 = r10;
        r111 = r14;
        r6 = r15;
        r118 = r93;
        r108 = r94;
        r113 = r96;
        r4 = r98;
        r3 = r137;
        r15 = r1;
        r1 = r0;
        r120 = r8;
        r8 = r5;
        r5 = r120;
        goto L_0x29e0;
        r104 = r12;
        r8 = r99;
        r4 = 2;
        if (r5 == r4) goto L_0x21aa;
        r4 = 9;
        if (r5 != r4) goto L_0x1ccf;
        r9 = r96;
        if (r9 == 0) goto L_0x1cd1;
        r110 = r5;
        r109 = r6;
        r105 = r8;
        r107 = r15;
        r108 = r94;
        r106 = r98;
        r12 = r131;
        goto L_0x21ba;
        r9 = r96;
        r4 = 3;
        if (r5 != r4) goto L_0x1e9f;
        r4 = r11.thumb;	 Catch:{ Exception -> 0x1e80 }
        org.telegram.messenger.ImageLoader.fillPhotoSizeWithBytes(r4);	 Catch:{ Exception -> 0x1e80 }
        r4 = org.telegram.messenger.MessageObject.isNewGifDocument(r11);	 Catch:{ Exception -> 0x1e80 }
        if (r4 != 0) goto L_0x1d1b;
        r4 = org.telegram.messenger.MessageObject.isRoundVideoDocument(r11);	 Catch:{ Exception -> 0x1d0c }
        if (r4 == 0) goto L_0x1ce6;	 Catch:{ Exception -> 0x1d0c }
        goto L_0x1d1b;	 Catch:{ Exception -> 0x1d0c }
        r4 = new org.telegram.tgnet.TLRPC$TL_decryptedMessageMediaVideo;	 Catch:{ Exception -> 0x1d0c }
        r4.<init>();	 Catch:{ Exception -> 0x1d0c }
        r3.media = r4;	 Catch:{ Exception -> 0x1d0c }
        r4 = r11.thumb;	 Catch:{ Exception -> 0x1d0c }
        if (r4 == 0) goto L_0x1d02;	 Catch:{ Exception -> 0x1d0c }
        r4 = r11.thumb;	 Catch:{ Exception -> 0x1d0c }
        r4 = r4.bytes;	 Catch:{ Exception -> 0x1d0c }
        if (r4 == 0) goto L_0x1d02;	 Catch:{ Exception -> 0x1d0c }
        r4 = r3.media;	 Catch:{ Exception -> 0x1d0c }
        r4 = (org.telegram.tgnet.TLRPC.TL_decryptedMessageMediaVideo) r4;	 Catch:{ Exception -> 0x1d0c }
        r12 = r11.thumb;	 Catch:{ Exception -> 0x1d0c }
        r12 = r12.bytes;	 Catch:{ Exception -> 0x1d0c }
        r4.thumb = r12;	 Catch:{ Exception -> 0x1d0c }
        goto L_0x1d46;	 Catch:{ Exception -> 0x1d0c }
        r4 = r3.media;	 Catch:{ Exception -> 0x1d0c }
        r4 = (org.telegram.tgnet.TLRPC.TL_decryptedMessageMediaVideo) r4;	 Catch:{ Exception -> 0x1d0c }
        r12 = 0;	 Catch:{ Exception -> 0x1d0c }
        r13 = new byte[r12];	 Catch:{ Exception -> 0x1d0c }
        r4.thumb = r13;	 Catch:{ Exception -> 0x1d0c }
        goto L_0x1d46;
    L_0x1d0c:
        r0 = move-exception;
        r13 = r6;
        r113 = r9;
        r114 = r10;
        r111 = r14;
        r6 = r15;
        r118 = r93;
        r108 = r94;
        goto L_0x1c8f;
        r4 = new org.telegram.tgnet.TLRPC$TL_decryptedMessageMediaDocument;	 Catch:{ Exception -> 0x1e80 }
        r4.<init>();	 Catch:{ Exception -> 0x1e80 }
        r3.media = r4;	 Catch:{ Exception -> 0x1e80 }
        r4 = r3.media;	 Catch:{ Exception -> 0x1e80 }
        r12 = r11.attributes;	 Catch:{ Exception -> 0x1e80 }
        r4.attributes = r12;	 Catch:{ Exception -> 0x1e80 }
        r4 = r11.thumb;	 Catch:{ Exception -> 0x1e80 }
        if (r4 == 0) goto L_0x1d3d;
        r4 = r11.thumb;	 Catch:{ Exception -> 0x1d0c }
        r4 = r4.bytes;	 Catch:{ Exception -> 0x1d0c }
        if (r4 == 0) goto L_0x1d3d;	 Catch:{ Exception -> 0x1d0c }
        r4 = r3.media;	 Catch:{ Exception -> 0x1d0c }
        r4 = (org.telegram.tgnet.TLRPC.TL_decryptedMessageMediaDocument) r4;	 Catch:{ Exception -> 0x1d0c }
        r12 = r11.thumb;	 Catch:{ Exception -> 0x1d0c }
        r12 = r12.bytes;	 Catch:{ Exception -> 0x1d0c }
        r4.thumb = r12;	 Catch:{ Exception -> 0x1d0c }
        goto L_0x1d46;
        r4 = r3.media;	 Catch:{ Exception -> 0x1e80 }
        r4 = (org.telegram.tgnet.TLRPC.TL_decryptedMessageMediaDocument) r4;	 Catch:{ Exception -> 0x1e80 }
        r12 = 0;	 Catch:{ Exception -> 0x1e80 }
        r13 = new byte[r12];	 Catch:{ Exception -> 0x1e80 }
        r4.thumb = r13;	 Catch:{ Exception -> 0x1e80 }
        r4 = r3.media;	 Catch:{ Exception -> 0x1e80 }
        r4.caption = r14;	 Catch:{ Exception -> 0x1e80 }
        r4 = r3.media;	 Catch:{ Exception -> 0x1e80 }
        r12 = "video/mp4";	 Catch:{ Exception -> 0x1e80 }
        r4.mime_type = r12;	 Catch:{ Exception -> 0x1e80 }
        r4 = r3.media;	 Catch:{ Exception -> 0x1e80 }
        r12 = r11.size;	 Catch:{ Exception -> 0x1e80 }
        r4.size = r12;	 Catch:{ Exception -> 0x1e80 }
        r4 = 0;	 Catch:{ Exception -> 0x1e80 }
        r12 = r11.attributes;	 Catch:{ Exception -> 0x1e80 }
        r12 = r12.size();	 Catch:{ Exception -> 0x1e80 }
        if (r4 >= r12) goto L_0x1db7;
        r12 = r11.attributes;	 Catch:{ Exception -> 0x1d9b }
        r12 = r12.get(r4);	 Catch:{ Exception -> 0x1d9b }
        r12 = (org.telegram.tgnet.TLRPC.DocumentAttribute) r12;	 Catch:{ Exception -> 0x1d9b }
        r13 = r12 instanceof org.telegram.tgnet.TLRPC.TL_documentAttributeVideo;	 Catch:{ Exception -> 0x1d9b }
        if (r13 == 0) goto L_0x1d96;	 Catch:{ Exception -> 0x1d9b }
        r13 = r3.media;	 Catch:{ Exception -> 0x1d9b }
        r105 = r8;
        r8 = r12.w;	 Catch:{ Exception -> 0x1d80 }
        r13.w = r8;	 Catch:{ Exception -> 0x1d80 }
        r8 = r3.media;	 Catch:{ Exception -> 0x1d80 }
        r13 = r12.h;	 Catch:{ Exception -> 0x1d80 }
        r8.h = r13;	 Catch:{ Exception -> 0x1d80 }
        r8 = r3.media;	 Catch:{ Exception -> 0x1d80 }
        r13 = r12.duration;	 Catch:{ Exception -> 0x1d80 }
        r8.duration = r13;	 Catch:{ Exception -> 0x1d80 }
        goto L_0x1db9;
    L_0x1d80:
        r0 = move-exception;
        r8 = r5;
        r13 = r6;
        r113 = r9;
        r114 = r10;
        r111 = r14;
        r6 = r15;
        r118 = r93;
        r108 = r94;
        r4 = r98;
        r12 = r104;
        r5 = r105;
        goto L_0x1b7b;
        r105 = r8;
        r4 = r4 + 1;
        goto L_0x1d57;
    L_0x1d9b:
        r0 = move-exception;
        r105 = r8;
        r8 = r5;
        r13 = r6;
        r113 = r9;
        r114 = r10;
        r111 = r14;
        r6 = r15;
        r118 = r93;
        r108 = r94;
        r4 = r98;
        r12 = r104;
        r5 = r105;
        r3 = r137;
        r15 = r1;
        r1 = r0;
        goto L_0x29e0;
        r105 = r8;
        r4 = r3.media;	 Catch:{ Exception -> 0x1e63 }
        r8 = r11.thumb;	 Catch:{ Exception -> 0x1e63 }
        r8 = r8.h;	 Catch:{ Exception -> 0x1e63 }
        r4.thumb_h = r8;	 Catch:{ Exception -> 0x1e63 }
        r4 = r3.media;	 Catch:{ Exception -> 0x1e63 }
        r8 = r11.thumb;	 Catch:{ Exception -> 0x1e63 }
        r8 = r8.w;	 Catch:{ Exception -> 0x1e63 }
        r4.thumb_w = r8;	 Catch:{ Exception -> 0x1e63 }
        r4 = r11.key;	 Catch:{ Exception -> 0x1e63 }
        if (r4 == 0) goto L_0x1e0d;
        r4 = (r102 > r19 ? 1 : (r102 == r19 ? 0 : -1));
        if (r4 == 0) goto L_0x1dd2;
        goto L_0x1e0d;
        r4 = new org.telegram.tgnet.TLRPC$TL_inputEncryptedFile;	 Catch:{ Exception -> 0x1d80 }
        r4.<init>();	 Catch:{ Exception -> 0x1d80 }
        r12 = r11.id;	 Catch:{ Exception -> 0x1d80 }
        r4.id = r12;	 Catch:{ Exception -> 0x1d80 }
        r12 = r11.access_hash;	 Catch:{ Exception -> 0x1d80 }
        r4.access_hash = r12;	 Catch:{ Exception -> 0x1d80 }
        r8 = r3.media;	 Catch:{ Exception -> 0x1d80 }
        r12 = r11.key;	 Catch:{ Exception -> 0x1d80 }
        r8.key = r12;	 Catch:{ Exception -> 0x1d80 }
        r8 = r3.media;	 Catch:{ Exception -> 0x1d80 }
        r12 = r11.iv;	 Catch:{ Exception -> 0x1d80 }
        r8.iv = r12;	 Catch:{ Exception -> 0x1d80 }
        r8 = r7.currentAccount;	 Catch:{ Exception -> 0x1d80 }
        r36 = org.telegram.messenger.SecretChatHelper.getInstance(r8);	 Catch:{ Exception -> 0x1d80 }
        r8 = r1.messageOwner;	 Catch:{ Exception -> 0x1d80 }
        r41 = 0;	 Catch:{ Exception -> 0x1d80 }
        r37 = r3;	 Catch:{ Exception -> 0x1d80 }
        r38 = r8;	 Catch:{ Exception -> 0x1d80 }
        r39 = r2;	 Catch:{ Exception -> 0x1d80 }
        r40 = r4;	 Catch:{ Exception -> 0x1d80 }
        r42 = r1;	 Catch:{ Exception -> 0x1d80 }
        r36.performSendEncryptedRequest(r37, r38, r39, r40, r41, r42);	 Catch:{ Exception -> 0x1d80 }
        r110 = r5;
        r109 = r6;
        r107 = r15;
        r108 = r94;
        goto L_0x1c7c;
        if (r25 != 0) goto L_0x1e2c;
        r4 = new org.telegram.messenger.SendMessagesHelper$DelayedMessage;	 Catch:{ Exception -> 0x1e27 }
        r12 = r131;
        r4.<init>(r12);	 Catch:{ Exception -> 0x1d80 }
        r4.encryptedChat = r2;	 Catch:{ Exception -> 0x1e22 }
        r8 = 1;	 Catch:{ Exception -> 0x1e22 }
        r4.type = r8;	 Catch:{ Exception -> 0x1e22 }
        r4.sendEncryptedRequest = r3;	 Catch:{ Exception -> 0x1e22 }
        r4.originalPath = r10;	 Catch:{ Exception -> 0x1e22 }
        r4.obj = r1;	 Catch:{ Exception -> 0x1e22 }
        goto L_0x1e30;
    L_0x1e22:
        r0 = move-exception;
        r25 = r4;
        goto L_0x1d81;
    L_0x1e27:
        r0 = move-exception;
        r12 = r131;
        goto L_0x1d81;
        r12 = r131;
        r4 = r25;
        r8 = r98;
        r4.videoEditedInfo = r8;	 Catch:{ Exception -> 0x1e49 }
        r18 = (r102 > r19 ? 1 : (r102 == r19 ? 0 : -1));	 Catch:{ Exception -> 0x1e49 }
        if (r18 != 0) goto L_0x1e3b;	 Catch:{ Exception -> 0x1e49 }
        r7.performSendDelayedMessage(r4);	 Catch:{ Exception -> 0x1e49 }
        r110 = r5;
        r109 = r6;
        r106 = r8;
        r111 = r14;
        r107 = r15;
        r108 = r94;
        goto L_0x1fc9;
    L_0x1e49:
        r0 = move-exception;
        r25 = r4;
        r13 = r6;
        r4 = r8;
        r113 = r9;
        r114 = r10;
        r111 = r14;
        r6 = r15;
        r118 = r93;
        r108 = r94;
        r12 = r104;
        r3 = r137;
        r15 = r1;
        r8 = r5;
        r5 = r105;
        goto L_0x0844;
    L_0x1e63:
        r0 = move-exception;
        r8 = r98;
        r12 = r131;
        r13 = r6;
        r4 = r8;
        r113 = r9;
        r114 = r10;
        r111 = r14;
        r6 = r15;
        r118 = r93;
        r108 = r94;
        r12 = r104;
        r3 = r137;
        r15 = r1;
        r8 = r5;
        r5 = r105;
        r1 = r0;
        goto L_0x29e0;
    L_0x1e80:
        r0 = move-exception;
        r105 = r8;
        r8 = r98;
        r12 = r131;
        r13 = r6;
        r4 = r8;
        r113 = r9;
        r114 = r10;
        r111 = r14;
        r6 = r15;
        r118 = r93;
        r108 = r94;
        r12 = r104;
        r3 = r137;
        r15 = r1;
        r8 = r5;
        r5 = r105;
        r1 = r0;
        goto L_0x29e0;
        r105 = r8;
        r8 = r98;
        r12 = r131;
        r4 = 6;
        if (r5 != r4) goto L_0x1f28;
        r4 = new org.telegram.tgnet.TLRPC$TL_decryptedMessageMediaContact;	 Catch:{ Exception -> 0x1f07 }
        r4.<init>();	 Catch:{ Exception -> 0x1f07 }
        r3.media = r4;	 Catch:{ Exception -> 0x1f07 }
        r4 = r3.media;	 Catch:{ Exception -> 0x1f07 }
        r106 = r8;
        r107 = r15;
        r8 = r94;
        r15 = r8.phone;	 Catch:{ Exception -> 0x1eec }
        r4.phone_number = r15;	 Catch:{ Exception -> 0x1eec }
        r4 = r3.media;	 Catch:{ Exception -> 0x1eec }
        r15 = r8.first_name;	 Catch:{ Exception -> 0x1eec }
        r4.first_name = r15;	 Catch:{ Exception -> 0x1eec }
        r4 = r3.media;	 Catch:{ Exception -> 0x1eec }
        r15 = r8.last_name;	 Catch:{ Exception -> 0x1eec }
        r4.last_name = r15;	 Catch:{ Exception -> 0x1eec }
        r4 = r3.media;	 Catch:{ Exception -> 0x1eec }
        r15 = r8.id;	 Catch:{ Exception -> 0x1eec }
        r4.user_id = r15;	 Catch:{ Exception -> 0x1eec }
        r4 = r7.currentAccount;	 Catch:{ Exception -> 0x1eec }
        r36 = org.telegram.messenger.SecretChatHelper.getInstance(r4);	 Catch:{ Exception -> 0x1eec }
        r4 = r1.messageOwner;	 Catch:{ Exception -> 0x1eec }
        r40 = 0;	 Catch:{ Exception -> 0x1eec }
        r41 = 0;	 Catch:{ Exception -> 0x1eec }
        r37 = r3;	 Catch:{ Exception -> 0x1eec }
        r38 = r4;	 Catch:{ Exception -> 0x1eec }
        r39 = r2;	 Catch:{ Exception -> 0x1eec }
        r42 = r1;	 Catch:{ Exception -> 0x1eec }
        r36.performSendEncryptedRequest(r37, r38, r39, r40, r41, r42);	 Catch:{ Exception -> 0x1eec }
        r110 = r5;
        r109 = r6;
        r108 = r8;
        goto L_0x216d;
    L_0x1eec:
        r0 = move-exception;
        r15 = r1;
        r13 = r6;
        r108 = r8;
        r113 = r9;
        r114 = r10;
        r111 = r14;
        r118 = r93;
        r12 = r104;
        r4 = r106;
        r6 = r107;
        r3 = r137;
        r1 = r0;
        r8 = r5;
        r5 = r105;
        goto L_0x29e0;
    L_0x1f07:
        r0 = move-exception;
        r106 = r8;
        r107 = r15;
        r8 = r94;
        r15 = r1;
        r13 = r6;
        r108 = r8;
        r113 = r9;
        r114 = r10;
        r111 = r14;
        r118 = r93;
        r12 = r104;
        r4 = r106;
        r6 = r107;
        r3 = r137;
        r1 = r0;
        r8 = r5;
        r5 = r105;
        goto L_0x29e0;
        r106 = r8;
        r107 = r15;
        r8 = r94;
        r4 = 7;
        if (r5 == r4) goto L_0x2033;
        r4 = 9;
        if (r5 != r4) goto L_0x1f3d;
        if (r11 == 0) goto L_0x1f3d;
        r109 = r6;
        r108 = r8;
        goto L_0x2037;
        r4 = 8;
        if (r5 != r4) goto L_0x202b;
        r4 = new org.telegram.messenger.SendMessagesHelper$DelayedMessage;	 Catch:{ Exception -> 0x200d }
        r4.<init>(r12);	 Catch:{ Exception -> 0x200d }
        r4.encryptedChat = r2;	 Catch:{ Exception -> 0x2004 }
        r4.sendEncryptedRequest = r3;	 Catch:{ Exception -> 0x2004 }
        r4.obj = r1;	 Catch:{ Exception -> 0x2004 }
        r15 = 3;	 Catch:{ Exception -> 0x2004 }
        r4.type = r15;	 Catch:{ Exception -> 0x2004 }
        r15 = new org.telegram.tgnet.TLRPC$TL_decryptedMessageMediaDocument;	 Catch:{ Exception -> 0x2004 }
        r15.<init>();	 Catch:{ Exception -> 0x2004 }
        r3.media = r15;	 Catch:{ Exception -> 0x2004 }
        r15 = r3.media;	 Catch:{ Exception -> 0x2004 }
        r108 = r8;
        r8 = r11.attributes;	 Catch:{ Exception -> 0x1fe6 }
        r15.attributes = r8;	 Catch:{ Exception -> 0x1fe6 }
        r8 = r3.media;	 Catch:{ Exception -> 0x1fe6 }
        r8.caption = r14;	 Catch:{ Exception -> 0x1fe6 }
        r8 = r11.thumb;	 Catch:{ Exception -> 0x1fe6 }
        if (r8 == 0) goto L_0x1fa1;
        r8 = r11.thumb;	 Catch:{ Exception -> 0x1f89 }
        r8 = r8.bytes;	 Catch:{ Exception -> 0x1f89 }
        if (r8 == 0) goto L_0x1fa1;	 Catch:{ Exception -> 0x1f89 }
        r8 = r3.media;	 Catch:{ Exception -> 0x1f89 }
        r8 = (org.telegram.tgnet.TLRPC.TL_decryptedMessageMediaDocument) r8;	 Catch:{ Exception -> 0x1f89 }
        r15 = r11.thumb;	 Catch:{ Exception -> 0x1f89 }
        r15 = r15.bytes;	 Catch:{ Exception -> 0x1f89 }
        r8.thumb = r15;	 Catch:{ Exception -> 0x1f89 }
        r8 = r3.media;	 Catch:{ Exception -> 0x1f89 }
        r15 = r11.thumb;	 Catch:{ Exception -> 0x1f89 }
        r15 = r15.h;	 Catch:{ Exception -> 0x1f89 }
        r8.thumb_h = r15;	 Catch:{ Exception -> 0x1f89 }
        r8 = r3.media;	 Catch:{ Exception -> 0x1f89 }
        r15 = r11.thumb;	 Catch:{ Exception -> 0x1f89 }
        r15 = r15.w;	 Catch:{ Exception -> 0x1f89 }
        r8.thumb_w = r15;	 Catch:{ Exception -> 0x1f89 }
        r109 = r6;
        goto L_0x1fb4;
    L_0x1f89:
        r0 = move-exception;
        r15 = r1;
        r25 = r4;
        r8 = r5;
        r13 = r6;
        r113 = r9;
        r114 = r10;
        r111 = r14;
        r118 = r93;
        r12 = r104;
        r5 = r105;
        r4 = r106;
        r6 = r107;
        goto L_0x0a37;
        r8 = r3.media;	 Catch:{ Exception -> 0x1fe6 }
        r8 = (org.telegram.tgnet.TLRPC.TL_decryptedMessageMediaDocument) r8;	 Catch:{ Exception -> 0x1fe6 }
        r109 = r6;
        r15 = 0;
        r6 = new byte[r15];	 Catch:{ Exception -> 0x1fcd }
        r8.thumb = r6;	 Catch:{ Exception -> 0x1fcd }
        r6 = r3.media;	 Catch:{ Exception -> 0x1fcd }
        r6.thumb_h = r15;	 Catch:{ Exception -> 0x1fcd }
        r6 = r3.media;	 Catch:{ Exception -> 0x1fcd }
        r6.thumb_w = r15;	 Catch:{ Exception -> 0x1fcd }
        r6 = r3.media;	 Catch:{ Exception -> 0x1fcd }
        r8 = r11.mime_type;	 Catch:{ Exception -> 0x1fcd }
        r6.mime_type = r8;	 Catch:{ Exception -> 0x1fcd }
        r6 = r3.media;	 Catch:{ Exception -> 0x1fcd }
        r8 = r11.size;	 Catch:{ Exception -> 0x1fcd }
        r6.size = r8;	 Catch:{ Exception -> 0x1fcd }
        r4.originalPath = r10;	 Catch:{ Exception -> 0x1fcd }
        r7.performSendDelayedMessage(r4);	 Catch:{ Exception -> 0x1fcd }
        r110 = r5;
        r111 = r14;
        r8 = r133;
        goto L_0x22ce;
    L_0x1fcd:
        r0 = move-exception;
        r15 = r1;
        r25 = r4;
        r8 = r5;
        r113 = r9;
        r114 = r10;
        r111 = r14;
        r118 = r93;
        r12 = r104;
        r5 = r105;
        r4 = r106;
        r6 = r107;
        r13 = r109;
        goto L_0x0a37;
    L_0x1fe6:
        r0 = move-exception;
        r109 = r6;
        r15 = r1;
        r25 = r4;
        r8 = r5;
        r113 = r9;
        r114 = r10;
        r111 = r14;
        r118 = r93;
        r12 = r104;
        r5 = r105;
        r4 = r106;
        r6 = r107;
        r13 = r109;
        r3 = r137;
        r1 = r0;
        goto L_0x29e0;
    L_0x2004:
        r0 = move-exception;
        r109 = r6;
        r108 = r8;
        r15 = r1;
        r25 = r4;
        goto L_0x2013;
    L_0x200d:
        r0 = move-exception;
        r109 = r6;
        r108 = r8;
        r15 = r1;
        r8 = r5;
        r113 = r9;
        r114 = r10;
        r111 = r14;
        r118 = r93;
        r12 = r104;
        r5 = r105;
        r4 = r106;
        r6 = r107;
        r13 = r109;
        r3 = r137;
        r1 = r0;
        goto L_0x29e0;
        r109 = r6;
        r108 = r8;
        r110 = r5;
        goto L_0x216d;
        r109 = r6;
        r108 = r8;
        r4 = org.telegram.messenger.MessageObject.isStickerDocument(r11);	 Catch:{ Exception -> 0x218d }
        if (r4 == 0) goto L_0x20ab;	 Catch:{ Exception -> 0x218d }
        r4 = new org.telegram.tgnet.TLRPC$TL_decryptedMessageMediaExternalDocument;	 Catch:{ Exception -> 0x218d }
        r4.<init>();	 Catch:{ Exception -> 0x218d }
        r3.media = r4;	 Catch:{ Exception -> 0x218d }
        r4 = r3.media;	 Catch:{ Exception -> 0x218d }
        r110 = r5;
        r5 = r11.id;	 Catch:{ Exception -> 0x2175 }
        r4.id = r5;	 Catch:{ Exception -> 0x2175 }
        r4 = r3.media;	 Catch:{ Exception -> 0x2175 }
        r5 = r11.date;	 Catch:{ Exception -> 0x2175 }
        r4.date = r5;	 Catch:{ Exception -> 0x2175 }
        r4 = r3.media;	 Catch:{ Exception -> 0x2175 }
        r5 = r11.access_hash;	 Catch:{ Exception -> 0x2175 }
        r4.access_hash = r5;	 Catch:{ Exception -> 0x2175 }
        r4 = r3.media;	 Catch:{ Exception -> 0x2175 }
        r5 = r11.mime_type;	 Catch:{ Exception -> 0x2175 }
        r4.mime_type = r5;	 Catch:{ Exception -> 0x2175 }
        r4 = r3.media;	 Catch:{ Exception -> 0x2175 }
        r5 = r11.size;	 Catch:{ Exception -> 0x2175 }
        r4.size = r5;	 Catch:{ Exception -> 0x2175 }
        r4 = r3.media;	 Catch:{ Exception -> 0x2175 }
        r5 = r11.dc_id;	 Catch:{ Exception -> 0x2175 }
        r4.dc_id = r5;	 Catch:{ Exception -> 0x2175 }
        r4 = r3.media;	 Catch:{ Exception -> 0x2175 }
        r5 = r11.attributes;	 Catch:{ Exception -> 0x2175 }
        r4.attributes = r5;	 Catch:{ Exception -> 0x2175 }
        r4 = r11.thumb;	 Catch:{ Exception -> 0x2175 }
        if (r4 != 0) goto L_0x208a;	 Catch:{ Exception -> 0x2175 }
        r4 = r3.media;	 Catch:{ Exception -> 0x2175 }
        r4 = (org.telegram.tgnet.TLRPC.TL_decryptedMessageMediaExternalDocument) r4;	 Catch:{ Exception -> 0x2175 }
        r5 = new org.telegram.tgnet.TLRPC$TL_photoSizeEmpty;	 Catch:{ Exception -> 0x2175 }
        r5.<init>();	 Catch:{ Exception -> 0x2175 }
        r4.thumb = r5;	 Catch:{ Exception -> 0x2175 }
        r4 = r3.media;	 Catch:{ Exception -> 0x2175 }
        r4 = (org.telegram.tgnet.TLRPC.TL_decryptedMessageMediaExternalDocument) r4;	 Catch:{ Exception -> 0x2175 }
        r4 = r4.thumb;	 Catch:{ Exception -> 0x2175 }
        r5 = "s";	 Catch:{ Exception -> 0x2175 }
        r4.type = r5;	 Catch:{ Exception -> 0x2175 }
        goto L_0x2092;	 Catch:{ Exception -> 0x2175 }
        r4 = r3.media;	 Catch:{ Exception -> 0x2175 }
        r4 = (org.telegram.tgnet.TLRPC.TL_decryptedMessageMediaExternalDocument) r4;	 Catch:{ Exception -> 0x2175 }
        r5 = r11.thumb;	 Catch:{ Exception -> 0x2175 }
        r4.thumb = r5;	 Catch:{ Exception -> 0x2175 }
        r4 = r7.currentAccount;	 Catch:{ Exception -> 0x2175 }
        r36 = org.telegram.messenger.SecretChatHelper.getInstance(r4);	 Catch:{ Exception -> 0x2175 }
        r4 = r1.messageOwner;	 Catch:{ Exception -> 0x2175 }
        r40 = 0;	 Catch:{ Exception -> 0x2175 }
        r41 = 0;	 Catch:{ Exception -> 0x2175 }
        r37 = r3;	 Catch:{ Exception -> 0x2175 }
        r38 = r4;	 Catch:{ Exception -> 0x2175 }
        r39 = r2;	 Catch:{ Exception -> 0x2175 }
        r42 = r1;	 Catch:{ Exception -> 0x2175 }
        r36.performSendEncryptedRequest(r37, r38, r39, r40, r41, r42);	 Catch:{ Exception -> 0x2175 }
        goto L_0x216d;	 Catch:{ Exception -> 0x2175 }
        r110 = r5;	 Catch:{ Exception -> 0x2175 }
        r4 = r11.thumb;	 Catch:{ Exception -> 0x2175 }
        org.telegram.messenger.ImageLoader.fillPhotoSizeWithBytes(r4);	 Catch:{ Exception -> 0x2175 }
        r4 = new org.telegram.tgnet.TLRPC$TL_decryptedMessageMediaDocument;	 Catch:{ Exception -> 0x2175 }
        r4.<init>();	 Catch:{ Exception -> 0x2175 }
        r3.media = r4;	 Catch:{ Exception -> 0x2175 }
        r4 = r3.media;	 Catch:{ Exception -> 0x2175 }
        r5 = r11.attributes;	 Catch:{ Exception -> 0x2175 }
        r4.attributes = r5;	 Catch:{ Exception -> 0x2175 }
        r4 = r3.media;	 Catch:{ Exception -> 0x2175 }
        r4.caption = r14;	 Catch:{ Exception -> 0x2175 }
        r4 = r11.thumb;	 Catch:{ Exception -> 0x2175 }
        if (r4 == 0) goto L_0x20e8;	 Catch:{ Exception -> 0x2175 }
        r4 = r11.thumb;	 Catch:{ Exception -> 0x2175 }
        r4 = r4.bytes;	 Catch:{ Exception -> 0x2175 }
        if (r4 == 0) goto L_0x20e8;	 Catch:{ Exception -> 0x2175 }
        r4 = r3.media;	 Catch:{ Exception -> 0x2175 }
        r4 = (org.telegram.tgnet.TLRPC.TL_decryptedMessageMediaDocument) r4;	 Catch:{ Exception -> 0x2175 }
        r5 = r11.thumb;	 Catch:{ Exception -> 0x2175 }
        r5 = r5.bytes;	 Catch:{ Exception -> 0x2175 }
        r4.thumb = r5;	 Catch:{ Exception -> 0x2175 }
        r4 = r3.media;	 Catch:{ Exception -> 0x2175 }
        r5 = r11.thumb;	 Catch:{ Exception -> 0x2175 }
        r5 = r5.h;	 Catch:{ Exception -> 0x2175 }
        r4.thumb_h = r5;	 Catch:{ Exception -> 0x2175 }
        r4 = r3.media;	 Catch:{ Exception -> 0x2175 }
        r5 = r11.thumb;	 Catch:{ Exception -> 0x2175 }
        r5 = r5.w;	 Catch:{ Exception -> 0x2175 }
        r4.thumb_w = r5;	 Catch:{ Exception -> 0x2175 }
        goto L_0x20f9;	 Catch:{ Exception -> 0x2175 }
        r4 = r3.media;	 Catch:{ Exception -> 0x2175 }
        r4 = (org.telegram.tgnet.TLRPC.TL_decryptedMessageMediaDocument) r4;	 Catch:{ Exception -> 0x2175 }
        r5 = 0;	 Catch:{ Exception -> 0x2175 }
        r6 = new byte[r5];	 Catch:{ Exception -> 0x2175 }
        r4.thumb = r6;	 Catch:{ Exception -> 0x2175 }
        r4 = r3.media;	 Catch:{ Exception -> 0x2175 }
        r4.thumb_h = r5;	 Catch:{ Exception -> 0x2175 }
        r4 = r3.media;	 Catch:{ Exception -> 0x2175 }
        r4.thumb_w = r5;	 Catch:{ Exception -> 0x2175 }
        r4 = r3.media;	 Catch:{ Exception -> 0x2175 }
        r5 = r11.size;	 Catch:{ Exception -> 0x2175 }
        r4.size = r5;	 Catch:{ Exception -> 0x2175 }
        r4 = r3.media;	 Catch:{ Exception -> 0x2175 }
        r5 = r11.mime_type;	 Catch:{ Exception -> 0x2175 }
        r4.mime_type = r5;	 Catch:{ Exception -> 0x2175 }
        r4 = r11.key;	 Catch:{ Exception -> 0x2175 }
        if (r4 != 0) goto L_0x213a;	 Catch:{ Exception -> 0x2175 }
        r4 = new org.telegram.messenger.SendMessagesHelper$DelayedMessage;	 Catch:{ Exception -> 0x2175 }
        r4.<init>(r12);	 Catch:{ Exception -> 0x2175 }
        r4.originalPath = r10;	 Catch:{ Exception -> 0x2135 }
        r4.sendEncryptedRequest = r3;	 Catch:{ Exception -> 0x2135 }
        r5 = 2;	 Catch:{ Exception -> 0x2135 }
        r4.type = r5;	 Catch:{ Exception -> 0x2135 }
        r4.obj = r1;	 Catch:{ Exception -> 0x2135 }
        r4.encryptedChat = r2;	 Catch:{ Exception -> 0x2135 }
        r5 = r133;	 Catch:{ Exception -> 0x2135 }
        if (r5 == 0) goto L_0x212d;	 Catch:{ Exception -> 0x2135 }
        r6 = r133.length();	 Catch:{ Exception -> 0x2135 }
        if (r6 <= 0) goto L_0x212d;	 Catch:{ Exception -> 0x2135 }
        r6 = "http";	 Catch:{ Exception -> 0x2135 }
        r6 = r5.startsWith(r6);	 Catch:{ Exception -> 0x2135 }
        if (r6 == 0) goto L_0x212d;	 Catch:{ Exception -> 0x2135 }
        r4.httpLocation = r5;	 Catch:{ Exception -> 0x2135 }
        r7.performSendDelayedMessage(r4);	 Catch:{ Exception -> 0x2135 }
        r8 = r5;
        r111 = r14;
        goto L_0x22ce;
    L_0x2135:
        r0 = move-exception;
        r15 = r1;
        r25 = r4;
        goto L_0x2177;
        r5 = r133;
        r4 = new org.telegram.tgnet.TLRPC$TL_inputEncryptedFile;	 Catch:{ Exception -> 0x2175 }
        r4.<init>();	 Catch:{ Exception -> 0x2175 }
        r5 = r11.id;	 Catch:{ Exception -> 0x2175 }
        r4.id = r5;	 Catch:{ Exception -> 0x2175 }
        r5 = r11.access_hash;	 Catch:{ Exception -> 0x2175 }
        r4.access_hash = r5;	 Catch:{ Exception -> 0x2175 }
        r5 = r3.media;	 Catch:{ Exception -> 0x2175 }
        r6 = r11.key;	 Catch:{ Exception -> 0x2175 }
        r5.key = r6;	 Catch:{ Exception -> 0x2175 }
        r5 = r3.media;	 Catch:{ Exception -> 0x2175 }
        r6 = r11.iv;	 Catch:{ Exception -> 0x2175 }
        r5.iv = r6;	 Catch:{ Exception -> 0x2175 }
        r5 = r7.currentAccount;	 Catch:{ Exception -> 0x2175 }
        r36 = org.telegram.messenger.SecretChatHelper.getInstance(r5);	 Catch:{ Exception -> 0x2175 }
        r5 = r1.messageOwner;	 Catch:{ Exception -> 0x2175 }
        r41 = 0;	 Catch:{ Exception -> 0x2175 }
        r37 = r3;	 Catch:{ Exception -> 0x2175 }
        r38 = r5;	 Catch:{ Exception -> 0x2175 }
        r39 = r2;	 Catch:{ Exception -> 0x2175 }
        r40 = r4;	 Catch:{ Exception -> 0x2175 }
        r42 = r1;	 Catch:{ Exception -> 0x2175 }
        r36.performSendEncryptedRequest(r37, r38, r39, r40, r41, r42);	 Catch:{ Exception -> 0x2175 }
        r111 = r14;
        r4 = r25;
        r8 = r133;
        goto L_0x22ce;
    L_0x2175:
        r0 = move-exception;
        r15 = r1;
        r113 = r9;
        r114 = r10;
        r111 = r14;
        r118 = r93;
        r12 = r104;
        r5 = r105;
        r4 = r106;
        r6 = r107;
        r13 = r109;
        r8 = r110;
        goto L_0x0a37;
    L_0x218d:
        r0 = move-exception;
        r110 = r5;
        r15 = r1;
        r113 = r9;
        r114 = r10;
        r111 = r14;
        r118 = r93;
        r12 = r104;
        r5 = r105;
        r4 = r106;
        r6 = r107;
        r13 = r109;
        r8 = r110;
        r3 = r137;
        r1 = r0;
        goto L_0x29e0;
        r110 = r5;
        r109 = r6;
        r105 = r8;
        r107 = r15;
        r108 = r94;
        r9 = r96;
        r106 = r98;
        r12 = r131;
        r4 = r9.sizes;	 Catch:{ Exception -> 0x23bf }
        r5 = 0;	 Catch:{ Exception -> 0x23bf }
        r4 = r4.get(r5);	 Catch:{ Exception -> 0x23bf }
        r4 = (org.telegram.tgnet.TLRPC.PhotoSize) r4;	 Catch:{ Exception -> 0x23bf }
        r5 = r9.sizes;	 Catch:{ Exception -> 0x23bf }
        r6 = r9.sizes;	 Catch:{ Exception -> 0x23bf }
        r6 = r6.size();	 Catch:{ Exception -> 0x23bf }
        r8 = 1;	 Catch:{ Exception -> 0x23bf }
        r6 = r6 - r8;	 Catch:{ Exception -> 0x23bf }
        r5 = r5.get(r6);	 Catch:{ Exception -> 0x23bf }
        r5 = (org.telegram.tgnet.TLRPC.PhotoSize) r5;	 Catch:{ Exception -> 0x23bf }
        org.telegram.messenger.ImageLoader.fillPhotoSizeWithBytes(r4);	 Catch:{ Exception -> 0x23bf }
        r6 = new org.telegram.tgnet.TLRPC$TL_decryptedMessageMediaPhoto;	 Catch:{ Exception -> 0x23bf }
        r6.<init>();	 Catch:{ Exception -> 0x23bf }
        r3.media = r6;	 Catch:{ Exception -> 0x23bf }
        r6 = r3.media;	 Catch:{ Exception -> 0x23bf }
        r6.caption = r14;	 Catch:{ Exception -> 0x23bf }
        r6 = r4.bytes;	 Catch:{ Exception -> 0x23bf }
        if (r6 == 0) goto L_0x21ee;
        r6 = r3.media;	 Catch:{ Exception -> 0x2175 }
        r6 = (org.telegram.tgnet.TLRPC.TL_decryptedMessageMediaPhoto) r6;	 Catch:{ Exception -> 0x2175 }
        r8 = r4.bytes;	 Catch:{ Exception -> 0x2175 }
        r6.thumb = r8;	 Catch:{ Exception -> 0x2175 }
        goto L_0x21f7;
        r6 = r3.media;	 Catch:{ Exception -> 0x23bf }
        r6 = (org.telegram.tgnet.TLRPC.TL_decryptedMessageMediaPhoto) r6;	 Catch:{ Exception -> 0x23bf }
        r8 = 0;	 Catch:{ Exception -> 0x23bf }
        r15 = new byte[r8];	 Catch:{ Exception -> 0x23bf }
        r6.thumb = r15;	 Catch:{ Exception -> 0x23bf }
        r6 = r3.media;	 Catch:{ Exception -> 0x23bf }
        r8 = r4.h;	 Catch:{ Exception -> 0x23bf }
        r6.thumb_h = r8;	 Catch:{ Exception -> 0x23bf }
        r6 = r3.media;	 Catch:{ Exception -> 0x23bf }
        r8 = r4.w;	 Catch:{ Exception -> 0x23bf }
        r6.thumb_w = r8;	 Catch:{ Exception -> 0x23bf }
        r6 = r3.media;	 Catch:{ Exception -> 0x23bf }
        r8 = r5.w;	 Catch:{ Exception -> 0x23bf }
        r6.w = r8;	 Catch:{ Exception -> 0x23bf }
        r6 = r3.media;	 Catch:{ Exception -> 0x23bf }
        r8 = r5.h;	 Catch:{ Exception -> 0x23bf }
        r6.h = r8;	 Catch:{ Exception -> 0x23bf }
        r6 = r3.media;	 Catch:{ Exception -> 0x23bf }
        r8 = r5.size;	 Catch:{ Exception -> 0x23bf }
        r6.size = r8;	 Catch:{ Exception -> 0x23bf }
        r6 = r5.location;	 Catch:{ Exception -> 0x23bf }
        r6 = r6.key;	 Catch:{ Exception -> 0x23bf }
        if (r6 == 0) goto L_0x227d;
        r6 = (r102 > r19 ? 1 : (r102 == r19 ? 0 : -1));
        if (r6 == 0) goto L_0x2222;
        r111 = r14;
        goto L_0x227f;
        r6 = new org.telegram.tgnet.TLRPC$TL_inputEncryptedFile;	 Catch:{ Exception -> 0x2262 }
        r6.<init>();	 Catch:{ Exception -> 0x2262 }
        r8 = r5.location;	 Catch:{ Exception -> 0x2262 }
        r111 = r14;
        r14 = r8.volume_id;	 Catch:{ Exception -> 0x2297 }
        r6.id = r14;	 Catch:{ Exception -> 0x2297 }
        r8 = r5.location;	 Catch:{ Exception -> 0x2297 }
        r14 = r8.secret;	 Catch:{ Exception -> 0x2297 }
        r6.access_hash = r14;	 Catch:{ Exception -> 0x2297 }
        r8 = r3.media;	 Catch:{ Exception -> 0x2297 }
        r14 = r5.location;	 Catch:{ Exception -> 0x2297 }
        r14 = r14.key;	 Catch:{ Exception -> 0x2297 }
        r8.key = r14;	 Catch:{ Exception -> 0x2297 }
        r8 = r3.media;	 Catch:{ Exception -> 0x2297 }
        r14 = r5.location;	 Catch:{ Exception -> 0x2297 }
        r14 = r14.iv;	 Catch:{ Exception -> 0x2297 }
        r8.iv = r14;	 Catch:{ Exception -> 0x2297 }
        r8 = r7.currentAccount;	 Catch:{ Exception -> 0x2297 }
        r36 = org.telegram.messenger.SecretChatHelper.getInstance(r8);	 Catch:{ Exception -> 0x2297 }
        r8 = r1.messageOwner;	 Catch:{ Exception -> 0x2297 }
        r41 = 0;	 Catch:{ Exception -> 0x2297 }
        r37 = r3;	 Catch:{ Exception -> 0x2297 }
        r38 = r8;	 Catch:{ Exception -> 0x2297 }
        r39 = r2;	 Catch:{ Exception -> 0x2297 }
        r40 = r6;	 Catch:{ Exception -> 0x2297 }
        r42 = r1;	 Catch:{ Exception -> 0x2297 }
        r36.performSendEncryptedRequest(r37, r38, r39, r40, r41, r42);	 Catch:{ Exception -> 0x2297 }
        r6 = r25;	 Catch:{ Exception -> 0x2297 }
        r8 = r133;	 Catch:{ Exception -> 0x2297 }
        goto L_0x22cc;	 Catch:{ Exception -> 0x2297 }
    L_0x2262:
        r0 = move-exception;	 Catch:{ Exception -> 0x2297 }
        r111 = r14;	 Catch:{ Exception -> 0x2297 }
        r15 = r1;	 Catch:{ Exception -> 0x2297 }
        r113 = r9;	 Catch:{ Exception -> 0x2297 }
        r114 = r10;	 Catch:{ Exception -> 0x2297 }
        r118 = r93;	 Catch:{ Exception -> 0x2297 }
        r12 = r104;	 Catch:{ Exception -> 0x2297 }
        r5 = r105;	 Catch:{ Exception -> 0x2297 }
        r4 = r106;	 Catch:{ Exception -> 0x2297 }
        r6 = r107;	 Catch:{ Exception -> 0x2297 }
        r13 = r109;	 Catch:{ Exception -> 0x2297 }
        r8 = r110;	 Catch:{ Exception -> 0x2297 }
        r3 = r137;	 Catch:{ Exception -> 0x2297 }
        r1 = r0;	 Catch:{ Exception -> 0x2297 }
        goto L_0x29e0;	 Catch:{ Exception -> 0x2297 }
        r111 = r14;	 Catch:{ Exception -> 0x2297 }
        if (r25 != 0) goto L_0x229a;	 Catch:{ Exception -> 0x2297 }
        r6 = new org.telegram.messenger.SendMessagesHelper$DelayedMessage;	 Catch:{ Exception -> 0x2297 }
        r6.<init>(r12);	 Catch:{ Exception -> 0x2297 }
        r6.encryptedChat = r2;	 Catch:{ Exception -> 0x2292 }
        r8 = 0;	 Catch:{ Exception -> 0x2292 }
        r6.type = r8;	 Catch:{ Exception -> 0x2292 }
        r6.originalPath = r10;	 Catch:{ Exception -> 0x2292 }
        r6.sendEncryptedRequest = r3;	 Catch:{ Exception -> 0x2292 }
        r6.obj = r1;	 Catch:{ Exception -> 0x2292 }
        goto L_0x229c;
    L_0x2292:
        r0 = move-exception;
        r15 = r1;
        r25 = r6;
        goto L_0x22df;
    L_0x2297:
        r0 = move-exception;
        r15 = r1;
        goto L_0x22df;
        r6 = r25;
        r8 = r133;
        r14 = android.text.TextUtils.isEmpty(r133);	 Catch:{ Exception -> 0x23a5 }
        if (r14 != 0) goto L_0x22af;
        r14 = "http";	 Catch:{ Exception -> 0x2292 }
        r14 = r8.startsWith(r14);	 Catch:{ Exception -> 0x2292 }
        if (r14 == 0) goto L_0x22af;	 Catch:{ Exception -> 0x2292 }
        r6.httpLocation = r8;	 Catch:{ Exception -> 0x2292 }
        goto L_0x22c5;
        r14 = r9.sizes;	 Catch:{ Exception -> 0x23a5 }
        r15 = r9.sizes;	 Catch:{ Exception -> 0x23a5 }
        r15 = r15.size();	 Catch:{ Exception -> 0x23a5 }
        r18 = 1;	 Catch:{ Exception -> 0x23a5 }
        r15 = r15 + -1;	 Catch:{ Exception -> 0x23a5 }
        r14 = r14.get(r15);	 Catch:{ Exception -> 0x23a5 }
        r14 = (org.telegram.tgnet.TLRPC.PhotoSize) r14;	 Catch:{ Exception -> 0x23a5 }
        r14 = r14.location;	 Catch:{ Exception -> 0x23a5 }
        r6.location = r14;	 Catch:{ Exception -> 0x23a5 }
        r14 = (r102 > r19 ? 1 : (r102 == r19 ? 0 : -1));
        if (r14 != 0) goto L_0x22cc;
        r7.performSendDelayedMessage(r6);	 Catch:{ Exception -> 0x2292 }
        r4 = r6;
        r5 = (r102 > r19 ? 1 : (r102 == r19 ? 0 : -1));
        if (r5 == 0) goto L_0x2370;
        r5 = r4.sendEncryptedRequest;	 Catch:{ Exception -> 0x2353 }
        if (r5 == 0) goto L_0x22e5;
        r5 = r4.sendEncryptedRequest;	 Catch:{ Exception -> 0x22db }
        r5 = (org.telegram.tgnet.TLRPC.TL_messages_sendEncryptedMultiMedia) r5;	 Catch:{ Exception -> 0x22db }
        goto L_0x22ec;
    L_0x22db:
        r0 = move-exception;
        r15 = r1;
        r25 = r4;
        r113 = r9;
        r114 = r10;
        goto L_0x217d;
        r5 = new org.telegram.tgnet.TLRPC$TL_messages_sendEncryptedMultiMedia;	 Catch:{ Exception -> 0x2353 }
        r5.<init>();	 Catch:{ Exception -> 0x2353 }
        r4.sendEncryptedRequest = r5;	 Catch:{ Exception -> 0x2353 }
        r6 = r4.messageObjects;	 Catch:{ Exception -> 0x2353 }
        r6.add(r1);	 Catch:{ Exception -> 0x2353 }
        r6 = r4.messages;	 Catch:{ Exception -> 0x2353 }
        r14 = r104;
        r6.add(r14);	 Catch:{ Exception -> 0x2338 }
        r6 = r4.originalPaths;	 Catch:{ Exception -> 0x2338 }
        r6.add(r10);	 Catch:{ Exception -> 0x2338 }
        r6 = 1;	 Catch:{ Exception -> 0x2338 }
        r4.upload = r6;	 Catch:{ Exception -> 0x2338 }
        r6 = r5.messages;	 Catch:{ Exception -> 0x2338 }
        r6.add(r3);	 Catch:{ Exception -> 0x2338 }
        r6 = new org.telegram.tgnet.TLRPC$TL_inputEncryptedFile;	 Catch:{ Exception -> 0x2338 }
        r6.<init>();	 Catch:{ Exception -> 0x2338 }
        r112 = r3;
        r15 = r110;
        r3 = 3;
        if (r15 != r3) goto L_0x2318;
        r19 = 1;
        r113 = r9;
        r8 = r19;
        goto L_0x2319;
        goto L_0x2313;
        r6.id = r8;	 Catch:{ Exception -> 0x2325 }
        r3 = r5.files;	 Catch:{ Exception -> 0x2325 }
        r3.add(r6);	 Catch:{ Exception -> 0x2325 }
        r7.performSendDelayedMessage(r4);	 Catch:{ Exception -> 0x2325 }
        goto L_0x2378;
    L_0x2325:
        r0 = move-exception;
        r25 = r4;
        r114 = r10;
        r12 = r14;
        r8 = r15;
        r118 = r93;
        r5 = r105;
        r4 = r106;
        r6 = r107;
        r13 = r109;
        goto L_0x1b7b;
    L_0x2338:
        r0 = move-exception;
        r113 = r9;
        r15 = r110;
        r25 = r4;
        r114 = r10;
        r12 = r14;
        r8 = r15;
        r118 = r93;
        r5 = r105;
        r4 = r106;
        r6 = r107;
        r13 = r109;
        r3 = r137;
        r15 = r1;
        r1 = r0;
        goto L_0x29e0;
    L_0x2353:
        r0 = move-exception;
        r113 = r9;
        r14 = r104;
        r15 = r110;
        r25 = r4;
        r114 = r10;
        r12 = r14;
        r8 = r15;
        r118 = r93;
        r5 = r105;
        r4 = r106;
        r6 = r107;
        r13 = r109;
        r3 = r137;
        r15 = r1;
        r1 = r0;
        goto L_0x29e0;
        r112 = r3;
        r113 = r9;
        r14 = r104;
        r15 = r110;
        r3 = r137;
        if (r3 != 0) goto L_0x2392;
        r5 = r7.currentAccount;	 Catch:{ Exception -> 0x2387 }
        r5 = org.telegram.messenger.DataQuery.getInstance(r5);	 Catch:{ Exception -> 0x2387 }
        r6 = 0;	 Catch:{ Exception -> 0x2387 }
        r5.cleanDraft(r12, r6);	 Catch:{ Exception -> 0x2387 }
        goto L_0x2392;
    L_0x2387:
        r0 = move-exception;
        r25 = r4;
        r114 = r10;
        r12 = r14;
        r8 = r15;
        r118 = r93;
        goto L_0x256d;
        r114 = r10;
        r115 = r11;
        r5 = r45;
        r116 = r89;
        r118 = r93;
        r9 = r107;
        r8 = r109;
        r10 = r138;
        goto L_0x283a;
    L_0x23a5:
        r0 = move-exception;
        r113 = r9;
        r14 = r104;
        r15 = r110;
        r3 = r137;
        r25 = r6;
        r114 = r10;
        r12 = r14;
        r8 = r15;
        r118 = r93;
        r5 = r105;
        r4 = r106;
        r6 = r107;
        r13 = r109;
        goto L_0x236c;
    L_0x23bf:
        r0 = move-exception;
        r113 = r9;
        r111 = r14;
        r14 = r104;
        r15 = r110;
        r3 = r137;
        r114 = r10;
        r12 = r14;
        r8 = r15;
        r118 = r93;
        r5 = r105;
        r4 = r106;
        r6 = r107;
        r13 = r109;
        r15 = r1;
        r1 = r0;
        goto L_0x29e0;
    L_0x23dc:
        r0 = move-exception;
        r109 = r6;
        r111 = r14;
        r107 = r15;
        r108 = r94;
        r113 = r96;
        r106 = r98;
        r105 = r99;
        r3 = r137;
        r15 = r5;
        r14 = r12;
        r12 = r131;
        r114 = r10;
        r12 = r14;
        r8 = r15;
        r118 = r93;
        r5 = r105;
        r4 = r106;
        r6 = r107;
        r13 = r109;
        r15 = r1;
        r1 = r0;
        goto L_0x29e0;
    L_0x2403:
        r0 = move-exception;
        r111 = r14;
        r107 = r15;
        r109 = r87;
        r108 = r94;
        r113 = r96;
        r106 = r98;
        r105 = r99;
        r3 = r137;
        r15 = r5;
        r14 = r12;
        r12 = r131;
        r114 = r10;
        r12 = r14;
        r8 = r15;
        r118 = r93;
        r5 = r105;
        r4 = r106;
        r6 = r107;
        r13 = r109;
        r15 = r1;
        r1 = r0;
        goto L_0x29e0;
        r102 = r1;
        r1 = r3;
        r106 = r4;
        r15 = r8;
        r2 = r9;
        r14 = r12;
        r93 = r13;
        r10 = r27;
        r111 = r46;
        r107 = r47;
        r105 = r48;
        r108 = r50;
        r11 = r51;
        r113 = r52;
        r101 = r56;
        r17 = r66;
        r89 = r67;
        r109 = r70;
        r3 = r137;
        r12 = r5;
        r4 = 4;
        if (r15 != r4) goto L_0x2546;
        r4 = new org.telegram.tgnet.TLRPC$TL_messages_forwardMessages;	 Catch:{ Exception -> 0x2533 }
        r4.<init>();	 Catch:{ Exception -> 0x2533 }
        r5 = r93;
        r4.to_peer = r5;	 Catch:{ Exception -> 0x2520 }
        r6 = r3.messageOwner;	 Catch:{ Exception -> 0x2520 }
        r6 = r6.with_my_score;	 Catch:{ Exception -> 0x2520 }
        r4.with_my_score = r6;	 Catch:{ Exception -> 0x2520 }
        r6 = r3.messageOwner;	 Catch:{ Exception -> 0x2520 }
        r6 = r6.ttl;	 Catch:{ Exception -> 0x2520 }
        if (r6 == 0) goto L_0x2496;	 Catch:{ Exception -> 0x2520 }
        r6 = r7.currentAccount;	 Catch:{ Exception -> 0x2520 }
        r6 = org.telegram.messenger.MessagesController.getInstance(r6);	 Catch:{ Exception -> 0x2520 }
        r8 = r3.messageOwner;	 Catch:{ Exception -> 0x2520 }
        r8 = r8.ttl;	 Catch:{ Exception -> 0x2520 }
        r8 = -r8;	 Catch:{ Exception -> 0x2520 }
        r8 = java.lang.Integer.valueOf(r8);	 Catch:{ Exception -> 0x2520 }
        r6 = r6.getChat(r8);	 Catch:{ Exception -> 0x2520 }
        r8 = new org.telegram.tgnet.TLRPC$TL_inputPeerChannel;	 Catch:{ Exception -> 0x2520 }
        r8.<init>();	 Catch:{ Exception -> 0x2520 }
        r4.from_peer = r8;	 Catch:{ Exception -> 0x2520 }
        r8 = r4.from_peer;	 Catch:{ Exception -> 0x2520 }
        r9 = r3.messageOwner;	 Catch:{ Exception -> 0x2520 }
        r9 = r9.ttl;	 Catch:{ Exception -> 0x2520 }
        r9 = -r9;	 Catch:{ Exception -> 0x2520 }
        r8.channel_id = r9;	 Catch:{ Exception -> 0x2520 }
        if (r6 == 0) goto L_0x2493;	 Catch:{ Exception -> 0x2520 }
        r8 = r4.from_peer;	 Catch:{ Exception -> 0x2520 }
        r114 = r10;
        r9 = r6.access_hash;	 Catch:{ Exception -> 0x2568 }
        r8.access_hash = r9;	 Catch:{ Exception -> 0x2568 }
        goto L_0x2495;	 Catch:{ Exception -> 0x2568 }
        r114 = r10;	 Catch:{ Exception -> 0x2568 }
        goto L_0x249f;	 Catch:{ Exception -> 0x2568 }
        r114 = r10;	 Catch:{ Exception -> 0x2568 }
        r6 = new org.telegram.tgnet.TLRPC$TL_inputPeerEmpty;	 Catch:{ Exception -> 0x2568 }
        r6.<init>();	 Catch:{ Exception -> 0x2568 }
        r4.from_peer = r6;	 Catch:{ Exception -> 0x2568 }
        r6 = r3.messageOwner;	 Catch:{ Exception -> 0x2568 }
        r6 = r6.to_id;	 Catch:{ Exception -> 0x2568 }
        r6 = r6 instanceof org.telegram.tgnet.TLRPC.TL_peerChannel;	 Catch:{ Exception -> 0x2568 }
        if (r6 == 0) goto L_0x24c5;	 Catch:{ Exception -> 0x2568 }
        r6 = r7.currentAccount;	 Catch:{ Exception -> 0x2568 }
        r6 = org.telegram.messenger.MessagesController.getNotificationsSettings(r6);	 Catch:{ Exception -> 0x2568 }
        r8 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x2568 }
        r8.<init>();	 Catch:{ Exception -> 0x2568 }
        r9 = "silent_";	 Catch:{ Exception -> 0x2568 }
        r8.append(r9);	 Catch:{ Exception -> 0x2568 }
        r8.append(r12);	 Catch:{ Exception -> 0x2568 }
        r8 = r8.toString();	 Catch:{ Exception -> 0x2568 }
        r9 = 0;	 Catch:{ Exception -> 0x2568 }
        r6 = r6.getBoolean(r8, r9);	 Catch:{ Exception -> 0x2568 }
        r4.silent = r6;	 Catch:{ Exception -> 0x2568 }
        r6 = r4.random_id;	 Catch:{ Exception -> 0x2568 }
        r8 = r14.random_id;	 Catch:{ Exception -> 0x2568 }
        r8 = java.lang.Long.valueOf(r8);	 Catch:{ Exception -> 0x2568 }
        r6.add(r8);	 Catch:{ Exception -> 0x2568 }
        r6 = r137.getId();	 Catch:{ Exception -> 0x2568 }
        if (r6 < 0) goto L_0x24e4;	 Catch:{ Exception -> 0x2568 }
        r6 = r4.id;	 Catch:{ Exception -> 0x2568 }
        r8 = r137.getId();	 Catch:{ Exception -> 0x2568 }
        r8 = java.lang.Integer.valueOf(r8);	 Catch:{ Exception -> 0x2568 }
        r6.add(r8);	 Catch:{ Exception -> 0x2568 }
        goto L_0x250d;	 Catch:{ Exception -> 0x2568 }
        r6 = r3.messageOwner;	 Catch:{ Exception -> 0x2568 }
        r6 = r6.fwd_msg_id;	 Catch:{ Exception -> 0x2568 }
        if (r6 == 0) goto L_0x24f8;	 Catch:{ Exception -> 0x2568 }
        r6 = r4.id;	 Catch:{ Exception -> 0x2568 }
        r8 = r3.messageOwner;	 Catch:{ Exception -> 0x2568 }
        r8 = r8.fwd_msg_id;	 Catch:{ Exception -> 0x2568 }
        r8 = java.lang.Integer.valueOf(r8);	 Catch:{ Exception -> 0x2568 }
        r6.add(r8);	 Catch:{ Exception -> 0x2568 }
        goto L_0x250d;	 Catch:{ Exception -> 0x2568 }
        r6 = r3.messageOwner;	 Catch:{ Exception -> 0x2568 }
        r6 = r6.fwd_from;	 Catch:{ Exception -> 0x2568 }
        if (r6 == 0) goto L_0x250d;	 Catch:{ Exception -> 0x2568 }
        r6 = r4.id;	 Catch:{ Exception -> 0x2568 }
        r8 = r3.messageOwner;	 Catch:{ Exception -> 0x2568 }
        r8 = r8.fwd_from;	 Catch:{ Exception -> 0x2568 }
        r8 = r8.channel_post;	 Catch:{ Exception -> 0x2568 }
        r8 = java.lang.Integer.valueOf(r8);	 Catch:{ Exception -> 0x2568 }
        r6.add(r8);	 Catch:{ Exception -> 0x2568 }
        r6 = 0;	 Catch:{ Exception -> 0x2568 }
        r7.performSendMessageRequest(r4, r1, r6);	 Catch:{ Exception -> 0x2568 }
        r118 = r5;
        r115 = r11;
        r5 = r45;
        r116 = r89;
        r9 = r107;
        r8 = r109;
        goto L_0x25d6;
    L_0x2520:
        r0 = move-exception;
        r114 = r10;
        r118 = r5;
        r12 = r14;
        r8 = r15;
        r5 = r105;
        r4 = r106;
        r6 = r107;
        r13 = r109;
        r15 = r1;
        r1 = r0;
        goto L_0x29e0;
    L_0x2533:
        r0 = move-exception;
        r114 = r10;
        r12 = r14;
        r8 = r15;
        r118 = r93;
        r5 = r105;
        r4 = r106;
        r6 = r107;
        r13 = r109;
        r15 = r1;
        r1 = r0;
        goto L_0x29e0;
        r114 = r10;
        r5 = r93;
        r4 = 9;
        if (r15 != r4) goto L_0x25fa;
        r4 = new org.telegram.tgnet.TLRPC$TL_messages_sendInlineBotResult;	 Catch:{ Exception -> 0x25e8 }
        r4.<init>();	 Catch:{ Exception -> 0x25e8 }
        r4.peer = r5;	 Catch:{ Exception -> 0x25e8 }
        r8 = r14.random_id;	 Catch:{ Exception -> 0x25e8 }
        r4.random_id = r8;	 Catch:{ Exception -> 0x25e8 }
        r6 = r14.reply_to_msg_id;	 Catch:{ Exception -> 0x25e8 }
        if (r6 == 0) goto L_0x2577;
        r6 = r4.flags;	 Catch:{ Exception -> 0x2568 }
        r8 = 1;	 Catch:{ Exception -> 0x2568 }
        r6 = r6 | r8;	 Catch:{ Exception -> 0x2568 }
        r4.flags = r6;	 Catch:{ Exception -> 0x2568 }
        r6 = r14.reply_to_msg_id;	 Catch:{ Exception -> 0x2568 }
        r4.reply_to_msg_id = r6;	 Catch:{ Exception -> 0x2568 }
        goto L_0x2577;
    L_0x2568:
        r0 = move-exception;
        r118 = r5;
        r12 = r14;
        r8 = r15;
        r5 = r105;
        r4 = r106;
        r6 = r107;
        r13 = r109;
        goto L_0x279b;
        r6 = r14.to_id;	 Catch:{ Exception -> 0x25e8 }
        r6 = r6 instanceof org.telegram.tgnet.TLRPC.TL_peerChannel;	 Catch:{ Exception -> 0x25e8 }
        if (r6 == 0) goto L_0x259b;
        r6 = r7.currentAccount;	 Catch:{ Exception -> 0x2568 }
        r6 = org.telegram.messenger.MessagesController.getNotificationsSettings(r6);	 Catch:{ Exception -> 0x2568 }
        r8 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x2568 }
        r8.<init>();	 Catch:{ Exception -> 0x2568 }
        r9 = "silent_";	 Catch:{ Exception -> 0x2568 }
        r8.append(r9);	 Catch:{ Exception -> 0x2568 }
        r8.append(r12);	 Catch:{ Exception -> 0x2568 }
        r8 = r8.toString();	 Catch:{ Exception -> 0x2568 }
        r9 = 0;	 Catch:{ Exception -> 0x2568 }
        r6 = r6.getBoolean(r8, r9);	 Catch:{ Exception -> 0x2568 }
        r4.silent = r6;	 Catch:{ Exception -> 0x2568 }
        r6 = "query_id";	 Catch:{ Exception -> 0x25e8 }
        r8 = r109;
        r6 = r8.get(r6);	 Catch:{ Exception -> 0x25da }
        r6 = (java.lang.String) r6;	 Catch:{ Exception -> 0x25da }
        r6 = org.telegram.messenger.Utilities.parseLong(r6);	 Catch:{ Exception -> 0x25da }
        r9 = r6.longValue();	 Catch:{ Exception -> 0x25da }
        r4.query_id = r9;	 Catch:{ Exception -> 0x25da }
        r6 = "id";	 Catch:{ Exception -> 0x25da }
        r6 = r8.get(r6);	 Catch:{ Exception -> 0x25da }
        r6 = (java.lang.String) r6;	 Catch:{ Exception -> 0x25da }
        r4.id = r6;	 Catch:{ Exception -> 0x25da }
        if (r3 != 0) goto L_0x25c8;	 Catch:{ Exception -> 0x25da }
        r6 = 1;	 Catch:{ Exception -> 0x25da }
        r4.clear_draft = r6;	 Catch:{ Exception -> 0x25da }
        r6 = r7.currentAccount;	 Catch:{ Exception -> 0x25da }
        r6 = org.telegram.messenger.DataQuery.getInstance(r6);	 Catch:{ Exception -> 0x25da }
        r9 = 0;	 Catch:{ Exception -> 0x25da }
        r6.cleanDraft(r12, r9);	 Catch:{ Exception -> 0x25da }
        r6 = 0;	 Catch:{ Exception -> 0x25da }
        r7.performSendMessageRequest(r4, r1, r6);	 Catch:{ Exception -> 0x25da }
        r118 = r5;
        r115 = r11;
        r5 = r45;
        r116 = r89;
        r9 = r107;
        r10 = r138;
        goto L_0x2838;
    L_0x25da:
        r0 = move-exception;
        r118 = r5;
        r13 = r8;
        r12 = r14;
        r8 = r15;
        r5 = r105;
        r4 = r106;
        r6 = r107;
        goto L_0x279b;
    L_0x25e8:
        r0 = move-exception;
        r8 = r109;
        r118 = r5;
        r13 = r8;
        r12 = r14;
        r8 = r15;
        r5 = r105;
        r4 = r106;
        r6 = r107;
        r15 = r1;
        r1 = r0;
        goto L_0x29e0;
        r8 = r109;
        r118 = r5;
        r115 = r11;
        r5 = r45;
        r116 = r89;
        r9 = r107;
        r10 = r138;
        goto L_0x2838;
        r102 = r1;
        r1 = r3;
        r106 = r4;
        r15 = r8;
        r2 = r9;
        r14 = r12;
        r114 = r27;
        r111 = r46;
        r107 = r47;
        r105 = r48;
        r108 = r50;
        r11 = r51;
        r113 = r52;
        r101 = r56;
        r17 = r66;
        r89 = r67;
        r8 = r70;
        r3 = r137;
        r120 = r5;
        r5 = r13;
        r12 = r120;
        if (r2 != 0) goto L_0x2774;
        if (r89 == 0) goto L_0x26c8;
        r4 = new org.telegram.tgnet.TLRPC$TL_messages_sendBroadcast;	 Catch:{ Exception -> 0x26b3 }
        r4.<init>();	 Catch:{ Exception -> 0x26b3 }
        r6 = new java.util.ArrayList;	 Catch:{ Exception -> 0x26b3 }
        r6.<init>();	 Catch:{ Exception -> 0x26b3 }
        r9 = 0;
        r115 = r11;
        r10 = r89;
        r11 = r10.size();	 Catch:{ Exception -> 0x269e }
        if (r9 >= r11) goto L_0x2670;
        r11 = org.telegram.messenger.Utilities.random;	 Catch:{ Exception -> 0x265e }
        r11 = r11.nextLong();	 Catch:{ Exception -> 0x265e }
        r11 = java.lang.Long.valueOf(r11);	 Catch:{ Exception -> 0x265e }
        r6.add(r11);	 Catch:{ Exception -> 0x265e }
        r9 = r9 + 1;
        r89 = r10;
        r11 = r115;
        r12 = r131;
        goto L_0x263e;
    L_0x265e:
        r0 = move-exception;
        r118 = r5;
        r13 = r8;
        r89 = r10;
        r12 = r14;
        r8 = r15;
        r5 = r105;
        r4 = r106;
        r6 = r107;
        r11 = r115;
        goto L_0x279b;
        r9 = r107;
        r4.message = r9;	 Catch:{ Exception -> 0x2690 }
        r4.contacts = r10;	 Catch:{ Exception -> 0x2690 }
        r11 = new org.telegram.tgnet.TLRPC$TL_inputMediaEmpty;	 Catch:{ Exception -> 0x2690 }
        r11.<init>();	 Catch:{ Exception -> 0x2690 }
        r4.media = r11;	 Catch:{ Exception -> 0x2690 }
        r4.random_id = r6;	 Catch:{ Exception -> 0x2690 }
        r11 = 0;	 Catch:{ Exception -> 0x2690 }
        r7.performSendMessageRequest(r4, r1, r11);	 Catch:{ Exception -> 0x2690 }
        r118 = r5;
        r116 = r10;
        r5 = r45;
        r10 = r138;
        r12 = r131;
        goto L_0x2838;
    L_0x2690:
        r0 = move-exception;
        r118 = r5;
        r13 = r8;
        r6 = r9;
        r89 = r10;
        r12 = r14;
        r8 = r15;
        r5 = r105;
        r4 = r106;
        goto L_0x266c;
    L_0x269e:
        r0 = move-exception;
        r9 = r107;
        r118 = r5;
        r13 = r8;
        r6 = r9;
        r89 = r10;
        r12 = r14;
        r8 = r15;
        r5 = r105;
        r4 = r106;
        r11 = r115;
        r15 = r1;
        r1 = r0;
        goto L_0x29e0;
    L_0x26b3:
        r0 = move-exception;
        r115 = r11;
        r10 = r89;
        r9 = r107;
        r118 = r5;
        r13 = r8;
        r6 = r9;
        r12 = r14;
        r8 = r15;
        r5 = r105;
        r4 = r106;
        r15 = r1;
        r1 = r0;
        goto L_0x29e0;
        r115 = r11;
        r10 = r89;
        r9 = r107;
        r4 = new org.telegram.tgnet.TLRPC$TL_messages_sendMessage;	 Catch:{ Exception -> 0x275b }
        r4.<init>();	 Catch:{ Exception -> 0x275b }
        r4.message = r9;	 Catch:{ Exception -> 0x275b }
        if (r3 != 0) goto L_0x26d9;	 Catch:{ Exception -> 0x275b }
        r6 = 1;	 Catch:{ Exception -> 0x275b }
        goto L_0x26da;	 Catch:{ Exception -> 0x275b }
        r6 = 0;	 Catch:{ Exception -> 0x275b }
        r4.clear_draft = r6;	 Catch:{ Exception -> 0x275b }
        r6 = r14.to_id;	 Catch:{ Exception -> 0x275b }
        r6 = r6 instanceof org.telegram.tgnet.TLRPC.TL_peerChannel;	 Catch:{ Exception -> 0x275b }
        if (r6 == 0) goto L_0x270d;
        r6 = r7.currentAccount;	 Catch:{ Exception -> 0x2709 }
        r6 = org.telegram.messenger.MessagesController.getNotificationsSettings(r6);	 Catch:{ Exception -> 0x2709 }
        r11 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x2709 }
        r11.<init>();	 Catch:{ Exception -> 0x2709 }
        r12 = "silent_";	 Catch:{ Exception -> 0x2709 }
        r11.append(r12);	 Catch:{ Exception -> 0x2709 }
        r12 = r131;
        r11.append(r12);	 Catch:{ Exception -> 0x2705 }
        r11 = r11.toString();	 Catch:{ Exception -> 0x2705 }
        r116 = r10;
        r10 = 0;
        r6 = r6.getBoolean(r11, r10);	 Catch:{ Exception -> 0x278c }
        r4.silent = r6;	 Catch:{ Exception -> 0x278c }
        goto L_0x2711;
    L_0x2705:
        r0 = move-exception;
        r116 = r10;
        goto L_0x2762;
    L_0x2709:
        r0 = move-exception;
        r116 = r10;
        goto L_0x2760;
        r116 = r10;
        r12 = r131;
        r4.peer = r5;	 Catch:{ Exception -> 0x2757 }
        r10 = r14.random_id;	 Catch:{ Exception -> 0x2757 }
        r4.random_id = r10;	 Catch:{ Exception -> 0x2757 }
        r6 = r14.reply_to_msg_id;	 Catch:{ Exception -> 0x2757 }
        if (r6 == 0) goto L_0x2725;
        r6 = r4.flags;	 Catch:{ Exception -> 0x278c }
        r10 = 1;	 Catch:{ Exception -> 0x278c }
        r6 = r6 | r10;	 Catch:{ Exception -> 0x278c }
        r4.flags = r6;	 Catch:{ Exception -> 0x278c }
        r6 = r14.reply_to_msg_id;	 Catch:{ Exception -> 0x278c }
        r4.reply_to_msg_id = r6;	 Catch:{ Exception -> 0x278c }
        if (r136 != 0) goto L_0x272a;	 Catch:{ Exception -> 0x278c }
        r10 = 1;	 Catch:{ Exception -> 0x278c }
        r4.no_webpage = r10;	 Catch:{ Exception -> 0x278c }
        r10 = r138;	 Catch:{ Exception -> 0x278c }
        if (r10 == 0) goto L_0x273e;	 Catch:{ Exception -> 0x278c }
        r11 = r138.isEmpty();	 Catch:{ Exception -> 0x278c }
        if (r11 != 0) goto L_0x273e;	 Catch:{ Exception -> 0x278c }
        r4.entities = r10;	 Catch:{ Exception -> 0x278c }
        r11 = r4.flags;	 Catch:{ Exception -> 0x278c }
        r18 = 8;	 Catch:{ Exception -> 0x278c }
        r11 = r11 | 8;	 Catch:{ Exception -> 0x278c }
        r4.flags = r11;	 Catch:{ Exception -> 0x278c }
        r11 = 0;	 Catch:{ Exception -> 0x278c }
        r7.performSendMessageRequest(r4, r1, r11);	 Catch:{ Exception -> 0x278c }
        if (r3 != 0) goto L_0x2750;	 Catch:{ Exception -> 0x278c }
        r11 = r7.currentAccount;	 Catch:{ Exception -> 0x278c }
        r11 = org.telegram.messenger.DataQuery.getInstance(r11);	 Catch:{ Exception -> 0x278c }
        r117 = r4;	 Catch:{ Exception -> 0x278c }
        r4 = 0;	 Catch:{ Exception -> 0x278c }
        r11.cleanDraft(r12, r4);	 Catch:{ Exception -> 0x278c }
        r118 = r5;
        r5 = r45;
        goto L_0x2838;
    L_0x2757:
        r0 = move-exception;
        r10 = r138;
        goto L_0x278d;
    L_0x275b:
        r0 = move-exception;
        r116 = r10;
        r10 = r138;
        r12 = r131;
        r118 = r5;
        r13 = r8;
        r6 = r9;
        r12 = r14;
        r8 = r15;
        r5 = r105;
        r4 = r106;
        r11 = r115;
        r89 = r116;
        r15 = r1;
        r1 = r0;
        goto L_0x29e0;
        r115 = r11;
        r116 = r89;
        r9 = r107;
        r10 = r138;
        r4 = r2.layer;	 Catch:{ Exception -> 0x2858 }
        r4 = org.telegram.messenger.AndroidUtilities.getPeerLayerVersion(r4);	 Catch:{ Exception -> 0x2858 }
        r11 = 73;
        if (r4 < r11) goto L_0x279e;
        r4 = new org.telegram.tgnet.TLRPC$TL_decryptedMessage;	 Catch:{ Exception -> 0x278c }
        r4.<init>();	 Catch:{ Exception -> 0x278c }
        goto L_0x27a3;
    L_0x278c:
        r0 = move-exception;
        r118 = r5;
        r13 = r8;
        r6 = r9;
        r12 = r14;
        r8 = r15;
        r5 = r105;
        r4 = r106;
        r11 = r115;
        r89 = r116;
        r15 = r1;
        goto L_0x0844;
        r4 = new org.telegram.tgnet.TLRPC$TL_decryptedMessage_layer45;	 Catch:{ Exception -> 0x2858 }
        r4.<init>();	 Catch:{ Exception -> 0x2858 }
        r11 = r14.ttl;	 Catch:{ Exception -> 0x2858 }
        r4.ttl = r11;	 Catch:{ Exception -> 0x2858 }
        if (r10 == 0) goto L_0x27b7;
        r11 = r138.isEmpty();	 Catch:{ Exception -> 0x278c }
        if (r11 != 0) goto L_0x27b7;	 Catch:{ Exception -> 0x278c }
        r4.entities = r10;	 Catch:{ Exception -> 0x278c }
        r11 = r4.flags;	 Catch:{ Exception -> 0x278c }
        r11 = r11 | 128;	 Catch:{ Exception -> 0x278c }
        r4.flags = r11;	 Catch:{ Exception -> 0x278c }
        r118 = r5;
        r5 = r14.reply_to_random_id;	 Catch:{ Exception -> 0x2845 }
        r11 = (r5 > r19 ? 1 : (r5 == r19 ? 0 : -1));
        if (r11 == 0) goto L_0x27cd;
        r5 = r14.reply_to_random_id;	 Catch:{ Exception -> 0x27cb }
        r4.reply_to_random_id = r5;	 Catch:{ Exception -> 0x27cb }
        r5 = r4.flags;	 Catch:{ Exception -> 0x27cb }
        r6 = 8;	 Catch:{ Exception -> 0x27cb }
        r5 = r5 | r6;	 Catch:{ Exception -> 0x27cb }
        r4.flags = r5;	 Catch:{ Exception -> 0x27cb }
        goto L_0x27cd;	 Catch:{ Exception -> 0x27cb }
    L_0x27cb:
        r0 = move-exception;	 Catch:{ Exception -> 0x27cb }
        goto L_0x278f;	 Catch:{ Exception -> 0x27cb }
        if (r8 == 0) goto L_0x27e7;	 Catch:{ Exception -> 0x27cb }
        r5 = "bot_name";	 Catch:{ Exception -> 0x27cb }
        r5 = r8.get(r5);	 Catch:{ Exception -> 0x27cb }
        if (r5 == 0) goto L_0x27e7;	 Catch:{ Exception -> 0x27cb }
        r5 = "bot_name";	 Catch:{ Exception -> 0x27cb }
        r5 = r8.get(r5);	 Catch:{ Exception -> 0x27cb }
        r5 = (java.lang.String) r5;	 Catch:{ Exception -> 0x27cb }
        r4.via_bot_name = r5;	 Catch:{ Exception -> 0x27cb }
        r5 = r4.flags;	 Catch:{ Exception -> 0x27cb }
        r5 = r5 | 2048;	 Catch:{ Exception -> 0x27cb }
        r4.flags = r5;	 Catch:{ Exception -> 0x27cb }
        r5 = r14.random_id;	 Catch:{ Exception -> 0x2845 }
        r4.random_id = r5;	 Catch:{ Exception -> 0x2845 }
        r4.message = r9;	 Catch:{ Exception -> 0x2845 }
        r5 = r45;
        if (r5 == 0) goto L_0x280d;
        r6 = r5.url;	 Catch:{ Exception -> 0x2809 }
        if (r6 == 0) goto L_0x280d;	 Catch:{ Exception -> 0x2809 }
        r6 = new org.telegram.tgnet.TLRPC$TL_decryptedMessageMediaWebPage;	 Catch:{ Exception -> 0x2809 }
        r6.<init>();	 Catch:{ Exception -> 0x2809 }
        r4.media = r6;	 Catch:{ Exception -> 0x2809 }
        r6 = r4.media;	 Catch:{ Exception -> 0x2809 }
        r11 = r5.url;	 Catch:{ Exception -> 0x2809 }
        r6.url = r11;	 Catch:{ Exception -> 0x2809 }
        r6 = r4.flags;	 Catch:{ Exception -> 0x2809 }
        r6 = r6 | 512;	 Catch:{ Exception -> 0x2809 }
        r4.flags = r6;	 Catch:{ Exception -> 0x2809 }
        goto L_0x2814;	 Catch:{ Exception -> 0x2809 }
    L_0x2809:
        r0 = move-exception;	 Catch:{ Exception -> 0x2809 }
        r45 = r5;	 Catch:{ Exception -> 0x2809 }
        goto L_0x278f;	 Catch:{ Exception -> 0x2809 }
        r6 = new org.telegram.tgnet.TLRPC$TL_decryptedMessageMediaEmpty;	 Catch:{ Exception -> 0x2809 }
        r6.<init>();	 Catch:{ Exception -> 0x2809 }
        r4.media = r6;	 Catch:{ Exception -> 0x2809 }
        r6 = r7.currentAccount;	 Catch:{ Exception -> 0x2809 }
        r36 = org.telegram.messenger.SecretChatHelper.getInstance(r6);	 Catch:{ Exception -> 0x2809 }
        r6 = r1.messageOwner;	 Catch:{ Exception -> 0x2809 }
        r40 = 0;	 Catch:{ Exception -> 0x2809 }
        r41 = 0;	 Catch:{ Exception -> 0x2809 }
        r37 = r4;	 Catch:{ Exception -> 0x2809 }
        r38 = r6;	 Catch:{ Exception -> 0x2809 }
        r39 = r2;	 Catch:{ Exception -> 0x2809 }
        r42 = r1;	 Catch:{ Exception -> 0x2809 }
        r36.performSendEncryptedRequest(r37, r38, r39, r40, r41, r42);	 Catch:{ Exception -> 0x2809 }
        if (r3 != 0) goto L_0x2837;	 Catch:{ Exception -> 0x2809 }
        r6 = r7.currentAccount;	 Catch:{ Exception -> 0x2809 }
        r6 = org.telegram.messenger.DataQuery.getInstance(r6);	 Catch:{ Exception -> 0x2809 }
        r11 = 0;	 Catch:{ Exception -> 0x2809 }
        r6.cleanDraft(r12, r11);	 Catch:{ Exception -> 0x2809 }
        r4 = r25;
        r45 = r5;
        r13 = r8;
        r6 = r9;
        r12 = r14;
        r8 = r15;
        r89 = r116;
        r15 = r1;
        goto L_0x2a1a;
    L_0x2845:
        r0 = move-exception;
        r5 = r45;
        r13 = r8;
        r6 = r9;
        r12 = r14;
        r8 = r15;
        r5 = r105;
        r4 = r106;
        r11 = r115;
        r89 = r116;
        r15 = r1;
        r1 = r0;
        goto L_0x29e0;
    L_0x2858:
        r0 = move-exception;
        r118 = r5;
        r5 = r45;
        r13 = r8;
        r6 = r9;
        r12 = r14;
        r8 = r15;
        r5 = r105;
        r4 = r106;
        r11 = r115;
        r89 = r116;
        r15 = r1;
        r1 = r0;
        goto L_0x29e0;
    L_0x286d:
        r0 = move-exception;
        r1 = r3;
        r106 = r4;
        r15 = r8;
        r2 = r9;
        r14 = r12;
        r114 = r27;
        r111 = r46;
        r9 = r47;
        r105 = r48;
        r118 = r49;
        r108 = r50;
        r115 = r51;
        r113 = r52;
        r101 = r56;
        r17 = r66;
        r116 = r67;
        r8 = r70;
        r3 = r137;
        r10 = r138;
        r12 = r5;
        r5 = r45;
        r13 = r8;
        r6 = r9;
        r12 = r14;
        r8 = r15;
        r5 = r105;
        r11 = r115;
        r89 = r116;
        r15 = r1;
        r1 = r0;
        goto L_0x29e0;
    L_0x28a1:
        r0 = move-exception;
        r1 = r3;
        r2 = r9;
        r17 = r11;
        r14 = r12;
        r116 = r15;
        r114 = r27;
        r111 = r46;
        r9 = r47;
        r105 = r48;
        r118 = r49;
        r108 = r50;
        r115 = r51;
        r113 = r52;
        r101 = r56;
        r3 = r137;
        r10 = r138;
        r15 = r8;
        r8 = r13;
        r12 = r5;
        r5 = r45;
        r4 = r127;
        r13 = r8;
        r6 = r9;
        r12 = r14;
        r8 = r15;
        r5 = r105;
        r11 = r115;
        r89 = r116;
        r15 = r1;
        r1 = r0;
        goto L_0x29e0;
    L_0x28d4:
        r0 = move-exception;
        r2 = r9;
        r17 = r11;
        r14 = r12;
        r116 = r15;
        r114 = r27;
        r111 = r46;
        r9 = r47;
        r105 = r48;
        r118 = r49;
        r108 = r50;
        r115 = r51;
        r113 = r52;
        r101 = r56;
        r3 = r137;
        r10 = r138;
        r15 = r8;
        r8 = r13;
        r12 = r5;
        r5 = r45;
        r4 = r127;
        r1 = r0;
        r13 = r8;
        r6 = r9;
        r12 = r14;
        r8 = r15;
        r15 = r24;
        r5 = r105;
        r11 = r115;
        r89 = r116;
        goto L_0x29e0;
    L_0x2907:
        r0 = move-exception;
        r101 = r2;
        r2 = r9;
        r17 = r11;
        r14 = r12;
        r116 = r15;
        r114 = r27;
        r111 = r46;
        r9 = r47;
        r105 = r48;
        r118 = r49;
        r108 = r50;
        r115 = r51;
        r113 = r52;
        r3 = r137;
        r10 = r138;
        r15 = r8;
        r8 = r13;
        r12 = r5;
        r5 = r45;
        r4 = r127;
        r1 = r0;
        r13 = r8;
        r6 = r9;
        r12 = r14;
        r8 = r15;
        r15 = r24;
        r5 = r105;
        r11 = r115;
        r89 = r116;
        goto L_0x29e0;
    L_0x293a:
        r0 = move-exception;
        r101 = r2;
        r15 = r8;
        r2 = r9;
        r17 = r11;
        r14 = r12;
        r8 = r13;
        r114 = r27;
        r111 = r46;
        r9 = r47;
        r105 = r48;
        r118 = r49;
        r108 = r50;
        r115 = r51;
        r113 = r52;
        r3 = r137;
        r10 = r138;
        r12 = r5;
        r5 = r45;
        r4 = r127;
        r1 = r0;
        r13 = r8;
        r6 = r9;
        r12 = r14;
        r8 = r15;
        r15 = r24;
        r89 = r32;
        r5 = r105;
        r11 = r115;
        goto L_0x29e0;
    L_0x296b:
        r0 = move-exception;
        r108 = r1;
        r118 = r2;
        r113 = r3;
        r2 = r9;
        r14 = r12;
        r115 = r15;
        r114 = r27;
        r101 = r34;
        r17 = r35;
        r111 = r46;
        r9 = r47;
        r105 = r48;
        r3 = r137;
        r10 = r138;
        r15 = r8;
        r8 = r13;
        r12 = r5;
        r5 = r45;
        goto L_0x29ac;
    L_0x298c:
        r0 = move-exception;
        r108 = r1;
        r118 = r2;
        r113 = r3;
        r2 = r9;
        r3 = r14;
        r115 = r15;
        r114 = r27;
        r101 = r34;
        r17 = r35;
        r5 = r45;
        r111 = r46;
        r9 = r47;
        r105 = r48;
        r10 = r138;
        r15 = r8;
        r14 = r12;
        r8 = r13;
        r12 = r131;
        r4 = r127;
        r1 = r0;
        r13 = r8;
        r6 = r9;
        r12 = r14;
        r8 = r15;
        r15 = r24;
        r89 = r32;
        r5 = r105;
        r11 = r115;
        goto L_0x29e0;
    L_0x29bc:
        r0 = move-exception;
        r118 = r2;
        r111 = r8;
        r2 = r9;
        r3 = r14;
        r114 = r27;
        r101 = r34;
        r17 = r35;
        r4 = r127;
        r45 = r13;
        r15 = r24;
        r8 = r26;
        r89 = r32;
        r5 = r125;
        r6 = r123;
        r11 = r129;
        r108 = r128;
        r113 = r126;
        r13 = r12;
        r12 = r1;
        r1 = r0;
    L_0x29e0:
        org.telegram.messenger.FileLog.e(r1);
        r9 = r7.currentAccount;
        r9 = org.telegram.messenger.MessagesStorage.getInstance(r9);
        r9.markMessageAsSendError(r12);
        if (r15 == 0) goto L_0x29f3;
        r9 = r15.messageOwner;
        r10 = 2;
        r9.send_state = r10;
        r9 = r7.currentAccount;
        r9 = org.telegram.messenger.NotificationCenter.getInstance(r9);
        r10 = org.telegram.messenger.NotificationCenter.messageSendError;
        r14 = 1;
        r14 = new java.lang.Object[r14];
        r119 = r1;
        r1 = r12.id;
        r1 = java.lang.Integer.valueOf(r1);
        r16 = 0;
        r14[r16] = r1;
        r9.postNotificationName(r10, r14);
        r1 = r12.id;
        r7.processSentMessage(r1);
        r106 = r4;
        r105 = r5;
        r115 = r11;
        r4 = r25;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.SendMessagesHelper.sendMessage(java.lang.String, java.lang.String, org.telegram.tgnet.TLRPC$MessageMedia, org.telegram.tgnet.TLRPC$TL_photo, org.telegram.messenger.VideoEditedInfo, org.telegram.tgnet.TLRPC$User, org.telegram.tgnet.TLRPC$TL_document, org.telegram.tgnet.TLRPC$TL_game, long, java.lang.String, org.telegram.messenger.MessageObject, org.telegram.tgnet.TLRPC$WebPage, boolean, org.telegram.messenger.MessageObject, java.util.ArrayList, org.telegram.tgnet.TLRPC$ReplyMarkup, java.util.HashMap, int):void");
    }

    public boolean isSendingCallback(org.telegram.messenger.MessageObject r1, org.telegram.tgnet.TLRPC.KeyboardButton r2) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.SendMessagesHelper.isSendingCallback(org.telegram.messenger.MessageObject, org.telegram.tgnet.TLRPC$KeyboardButton):boolean
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
        if (r5 == 0) goto L_0x004c;
    L_0x0003:
        if (r6 != 0) goto L_0x0006;
    L_0x0005:
        goto L_0x004c;
    L_0x0006:
        r1 = r6 instanceof org.telegram.tgnet.TLRPC.TL_keyboardButtonGame;
        if (r1 == 0) goto L_0x000c;
    L_0x000a:
        r0 = 1;
        goto L_0x0013;
    L_0x000c:
        r1 = r6 instanceof org.telegram.tgnet.TLRPC.TL_keyboardButtonBuy;
        if (r1 == 0) goto L_0x0012;
    L_0x0010:
        r0 = 2;
        goto L_0x000b;
    L_0x0013:
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = r5.getDialogId();
        r1.append(r2);
        r2 = "_";
        r1.append(r2);
        r2 = r5.getId();
        r1.append(r2);
        r2 = "_";
        r1.append(r2);
        r2 = r6.data;
        r2 = org.telegram.messenger.Utilities.bytesToHex(r2);
        r1.append(r2);
        r2 = "_";
        r1.append(r2);
        r1.append(r0);
        r1 = r1.toString();
        r2 = r4.waitingForCallback;
        r2 = r2.containsKey(r1);
        return r2;
    L_0x004c:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.SendMessagesHelper.isSendingCallback(org.telegram.messenger.MessageObject, org.telegram.tgnet.TLRPC$KeyboardButton):boolean");
    }

    static {
        int availableProcessors;
        if (VERSION.SDK_INT >= 17) {
            availableProcessors = Runtime.getRuntime().availableProcessors();
        } else {
            availableProcessors = 2;
        }
        int cores = availableProcessors;
        mediaSendThreadPool = new ThreadPoolExecutor(cores, cores, 60, TimeUnit.SECONDS, new LinkedBlockingQueue());
    }

    public static SendMessagesHelper getInstance(int num) {
        SendMessagesHelper localInstance = Instance[num];
        if (localInstance == null) {
            synchronized (SendMessagesHelper.class) {
                localInstance = Instance[num];
                if (localInstance == null) {
                    SendMessagesHelper[] sendMessagesHelperArr = Instance;
                    SendMessagesHelper sendMessagesHelper = new SendMessagesHelper(num);
                    localInstance = sendMessagesHelper;
                    sendMessagesHelperArr[num] = sendMessagesHelper;
                }
            }
        }
        return localInstance;
    }

    public SendMessagesHelper(int instance) {
        this.currentAccount = instance;
        AndroidUtilities.runOnUIThread(new Runnable() {
            public void run() {
                NotificationCenter.getInstance(SendMessagesHelper.this.currentAccount).addObserver(SendMessagesHelper.this, NotificationCenter.FileDidUpload);
                NotificationCenter.getInstance(SendMessagesHelper.this.currentAccount).addObserver(SendMessagesHelper.this, NotificationCenter.FileDidFailUpload);
                NotificationCenter.getInstance(SendMessagesHelper.this.currentAccount).addObserver(SendMessagesHelper.this, NotificationCenter.FilePreparingStarted);
                NotificationCenter.getInstance(SendMessagesHelper.this.currentAccount).addObserver(SendMessagesHelper.this, NotificationCenter.FileNewChunkAvailable);
                NotificationCenter.getInstance(SendMessagesHelper.this.currentAccount).addObserver(SendMessagesHelper.this, NotificationCenter.FilePreparingFailed);
                NotificationCenter.getInstance(SendMessagesHelper.this.currentAccount).addObserver(SendMessagesHelper.this, NotificationCenter.httpFileDidFailedLoad);
                NotificationCenter.getInstance(SendMessagesHelper.this.currentAccount).addObserver(SendMessagesHelper.this, NotificationCenter.httpFileDidLoaded);
                NotificationCenter.getInstance(SendMessagesHelper.this.currentAccount).addObserver(SendMessagesHelper.this, NotificationCenter.FileDidLoaded);
                NotificationCenter.getInstance(SendMessagesHelper.this.currentAccount).addObserver(SendMessagesHelper.this, NotificationCenter.FileDidFailedLoad);
            }
        });
    }

    public void cleanup() {
        this.delayedMessages.clear();
        this.unsentMessages.clear();
        this.sendingMessages.clear();
        this.waitingForLocation.clear();
        this.waitingForCallback.clear();
        this.currentChatInfo = null;
        this.locationProvider.stop();
    }

    public void setCurrentChatInfo(ChatFull info) {
        this.currentChatInfo = info;
    }

    public void didReceivedNotification(int id, int account, Object... args) {
        SendMessagesHelper sendMessagesHelper = this;
        int i = id;
        int i2 = 4;
        int i3 = 1;
        int a = 0;
        int a2;
        int a3;
        int index;
        String str;
        int index2;
        if (i == NotificationCenter.FileDidUpload) {
            String location = args[0];
            InputFile file = args[1];
            InputEncryptedFile encryptedFile = args[2];
            ArrayList<DelayedMessage> arr = (ArrayList) sendMessagesHelper.delayedMessages.get(location);
            if (arr != null) {
                while (true) {
                    a2 = a;
                    if (a2 >= arr.size()) {
                        break;
                    }
                    ArrayList<DelayedMessage> arr2;
                    DelayedMessage message = (DelayedMessage) arr.get(a2);
                    InputMedia media = null;
                    if ((message.sendRequest instanceof TL_messages_sendMedia) != 0) {
                        media = ((TL_messages_sendMedia) message.sendRequest).media;
                    } else if ((message.sendRequest instanceof TL_messages_sendBroadcast) != 0) {
                        media = ((TL_messages_sendBroadcast) message.sendRequest).media;
                    } else if ((message.sendRequest instanceof TL_messages_sendMultiMedia) != 0) {
                        media = (InputMedia) message.extraHashMap.get(location);
                    }
                    a = media;
                    InputMedia media2;
                    DelayedMessage message2;
                    MessageObject decryptedMessage;
                    if (file == null || a == 0) {
                        media2 = a;
                        message2 = message;
                        a3 = a2;
                        arr2 = arr;
                        a = encryptedFile;
                        if (a == 0 || message2.sendEncryptedRequest == null) {
                            a2 = a3;
                        } else {
                            DecryptedMessage decryptedMessage2;
                            TL_decryptedMessage decryptedMessage3;
                            if (message2.type == i2) {
                                DecryptedMessage decryptedMessage4;
                                TL_messages_sendEncryptedMultiMedia req = message2.sendEncryptedRequest;
                                InputEncryptedFile arr3 = (InputEncryptedFile) message2.extraHashMap.get(location);
                                index = req.files.indexOf(arr3);
                                if (index >= 0) {
                                    req.files.set(index, a);
                                    if (arr3.id == 1) {
                                        HashMap hashMap = message2.extraHashMap;
                                        StringBuilder stringBuilder = new StringBuilder();
                                        stringBuilder.append(location);
                                        decryptedMessage3 = null;
                                        stringBuilder.append("_i");
                                        decryptedMessage = (MessageObject) hashMap.get(stringBuilder.toString());
                                        hashMap = message2.extraHashMap;
                                        stringBuilder = new StringBuilder();
                                        stringBuilder.append(location);
                                        MessageObject messageObject = decryptedMessage;
                                        stringBuilder.append("_t");
                                        message2.location = (FileLocation) hashMap.get(stringBuilder.toString());
                                        stopVideoService(((MessageObject) message2.messageObjects.get(index)).messageOwner.attachPath);
                                    } else {
                                        decryptedMessage3 = null;
                                    }
                                    decryptedMessage4 = (TL_decryptedMessage) req.messages.get(index);
                                } else {
                                    decryptedMessage4 = null;
                                }
                                decryptedMessage2 = decryptedMessage4;
                            } else {
                                decryptedMessage3 = null;
                                decryptedMessage2 = (TL_decryptedMessage) message2.sendEncryptedRequest;
                            }
                            if (decryptedMessage2 != null) {
                                if ((decryptedMessage2.media instanceof TL_decryptedMessageMediaVideo) || (decryptedMessage2.media instanceof TL_decryptedMessageMediaPhoto) || (decryptedMessage2.media instanceof TL_decryptedMessageMediaDocument)) {
                                    decryptedMessage2.media.size = (int) ((Long) args[5]).longValue();
                                }
                                decryptedMessage2.media.key = (byte[]) args[3];
                                decryptedMessage2.media.iv = (byte[]) args[4];
                                if (message2.type == 4) {
                                    uploadMultiMedia(message2, null, a, location);
                                } else {
                                    SecretChatHelper.getInstance(sendMessagesHelper.currentAccount).performSendEncryptedRequest(decryptedMessage2, message2.obj.messageOwner, message2.encryptedChat, a, message2.originalPath, message2.obj);
                                }
                            }
                            arr2.remove(a3);
                            a2 = a3 - 1;
                        }
                    } else {
                        InputEncryptedFile encryptedFile2;
                        if (message.type == 0) {
                            a.file = file;
                            TLObject tLObject = message.sendRequest;
                            MessageObject messageObject2 = message.obj;
                            str = message.originalPath;
                            media2 = a;
                            DelayedMessage message3 = message;
                            decryptedMessage = messageObject2;
                            a3 = a2;
                            String str2 = str;
                            arr2 = arr;
                            encryptedFile2 = encryptedFile;
                            performSendMessageRequest(tLObject, decryptedMessage, str2, message3, 1);
                            message2 = message3;
                        } else {
                            media2 = a;
                            a3 = a2;
                            arr2 = arr;
                            encryptedFile2 = encryptedFile;
                            message2 = message;
                            if (message2.type == i3) {
                                if (media2.file == 0) {
                                    media2.file = file;
                                    if (media2.thumb != 0 || message2.location == 0) {
                                        performSendMessageRequest(message2.sendRequest, message2.obj, message2.originalPath);
                                    } else {
                                        performSendDelayedMessage(message2);
                                    }
                                } else {
                                    media2.thumb = file;
                                    media2.flags |= i2;
                                    performSendMessageRequest(message2.sendRequest, message2.obj, message2.originalPath);
                                }
                            } else if (message2.type == 2) {
                                if (media2.file == 0) {
                                    media2.file = file;
                                    if (media2.thumb != 0 || message2.location == 0) {
                                        performSendMessageRequest(message2.sendRequest, message2.obj, message2.originalPath);
                                    } else {
                                        performSendDelayedMessage(message2);
                                    }
                                } else {
                                    media2.thumb = file;
                                    media2.flags |= i2;
                                    performSendMessageRequest(message2.sendRequest, message2.obj, message2.originalPath);
                                }
                            } else if (message2.type == 3) {
                                media2.file = file;
                                performSendMessageRequest(message2.sendRequest, message2.obj, message2.originalPath);
                            } else if (message2.type == i2) {
                                if ((media2 instanceof TL_inputMediaUploadedDocument) == 0) {
                                    media2.file = file;
                                    uploadMultiMedia(message2, media2, null, location);
                                } else if (media2.file == 0) {
                                    media2.file = file;
                                    a = message2.extraHashMap;
                                    r2 = new StringBuilder();
                                    r2.append(location);
                                    r2.append("_i");
                                    index2 = message2.messageObjects.indexOf((MessageObject) a.get(r2.toString()));
                                    HashMap hashMap2 = message2.extraHashMap;
                                    arr = new StringBuilder();
                                    arr.append(location);
                                    arr.append("_t");
                                    message2.location = (FileLocation) hashMap2.get(arr.toString());
                                    stopVideoService(((MessageObject) message2.messageObjects.get(index2)).messageOwner.attachPath);
                                    if (media2.thumb != null || message2.location == null) {
                                        uploadMultiMedia(message2, media2, null, location);
                                    } else {
                                        performSendDelayedMessage(message2, index2);
                                    }
                                } else {
                                    media2.thumb = file;
                                    media2.flags |= i2;
                                    a = message2.extraHashMap;
                                    r2 = new StringBuilder();
                                    r2.append(location);
                                    r2.append("_o");
                                    uploadMultiMedia(message2, media2, null, (String) a.get(r2.toString()));
                                }
                            }
                        }
                        arr2.remove(a3);
                        a2 = a3 - 1;
                        a = encryptedFile2;
                    }
                    i3 = 1;
                    encryptedFile = a;
                    a = a2 + 1;
                    arr = arr2;
                    i2 = 4;
                }
                a = encryptedFile;
                if (arr.isEmpty()) {
                    sendMessagesHelper.delayedMessages.remove(location);
                }
            }
        } else if (i == NotificationCenter.FileDidFailUpload) {
            location = args[0];
            boolean enc = ((Boolean) args[1]).booleanValue();
            arr = (ArrayList) sendMessagesHelper.delayedMessages.get(location);
            if (arr != null) {
                while (a < arr.size()) {
                    obj = (DelayedMessage) arr.get(a);
                    if ((enc && obj.sendEncryptedRequest != null) || !(enc || obj.sendRequest == null)) {
                        obj.markAsError();
                        arr.remove(a);
                        a--;
                    }
                    a++;
                }
                if (arr.isEmpty()) {
                    sendMessagesHelper.delayedMessages.remove(location);
                }
            }
        } else if (i == NotificationCenter.FilePreparingStarted) {
            messageObject = args[0];
            if (messageObject.getId() != 0) {
                finalPath = args[1];
                arr = (ArrayList) sendMessagesHelper.delayedMessages.get(messageObject.messageOwner.attachPath);
                if (arr != null) {
                    while (a < arr.size()) {
                        obj = (DelayedMessage) arr.get(a);
                        if (obj.type == 4) {
                            index = obj.messageObjects.indexOf(messageObject);
                            HashMap hashMap3 = obj.extraHashMap;
                            StringBuilder stringBuilder2 = new StringBuilder();
                            stringBuilder2.append(messageObject.messageOwner.attachPath);
                            stringBuilder2.append("_t");
                            obj.location = (FileLocation) hashMap3.get(stringBuilder2.toString());
                            performSendDelayedMessage(obj, index);
                            arr.remove(a);
                            break;
                        } else if (obj.obj == messageObject) {
                            obj.videoEditedInfo = null;
                            performSendDelayedMessage(obj);
                            arr.remove(a);
                            break;
                        } else {
                            a++;
                        }
                    }
                    if (arr.isEmpty()) {
                        sendMessagesHelper.delayedMessages.remove(messageObject.messageOwner.attachPath);
                    }
                }
            }
        } else if (i == NotificationCenter.FileNewChunkAvailable) {
            messageObject = (MessageObject) args[0];
            if (messageObject.getId() != 0) {
                finalPath = (String) args[1];
                long availableSize = ((Long) args[2]).longValue();
                long finalSize = ((Long) args[3]).longValue();
                long finalSize2 = finalSize;
                FileLoader.getInstance(sendMessagesHelper.currentAccount).checkUploadNewDataAvailable(finalPath, ((int) messageObject.getDialogId()) == 0, availableSize, finalSize);
                long finalSize3 = finalSize2;
                if (finalSize3 != 0) {
                    ArrayList<DelayedMessage> arr4 = (ArrayList) sendMessagesHelper.delayedMessages.get(messageObject.messageOwner.attachPath);
                    if (arr4 != null) {
                        int a4 = 0;
                        while (a4 < arr4.size()) {
                            String finalPath;
                            DelayedMessage message4 = (DelayedMessage) arr4.get(a4);
                            ArrayList<Message> messages;
                            if (message4.type == 4) {
                                for (int b = a; b < message4.messageObjects.size(); b++) {
                                    MessageObject obj = (MessageObject) message4.messageObjects.get(b);
                                    if (obj == messageObject) {
                                        obj.videoEditedInfo = null;
                                        finalPath = finalPath;
                                        obj.messageOwner.params.remove("ve");
                                        obj.messageOwner.media.document.size = (int) finalSize3;
                                        messages = new ArrayList();
                                        messages.add(obj.messageOwner);
                                        MessagesStorage.getInstance(sendMessagesHelper.currentAccount).putMessages((ArrayList) messages, false, true, false, 0);
                                        break;
                                    }
                                }
                                finalPath = finalPath;
                            } else {
                                finalPath = finalPath;
                                if (message4.obj == messageObject) {
                                    message4.obj.videoEditedInfo = null;
                                    message4.obj.messageOwner.params.remove("ve");
                                    message4.obj.messageOwner.media.document.size = (int) finalSize3;
                                    messages = new ArrayList();
                                    messages.add(message4.obj.messageOwner);
                                    MessagesStorage.getInstance(sendMessagesHelper.currentAccount).putMessages((ArrayList) messages, false, true, false, 0);
                                    break;
                                }
                            }
                            a4++;
                            finalPath = finalPath;
                            a = 0;
                        }
                    }
                }
            }
        } else {
            MessageObject messageObject3 = null;
            String finalPath2;
            ArrayList<DelayedMessage> arr5;
            if (i == NotificationCenter.FilePreparingFailed) {
                messageObject = (MessageObject) args[0];
                if (messageObject.getId() != 0) {
                    finalPath2 = args[1];
                    stopVideoService(messageObject.messageOwner.attachPath);
                    arr5 = (ArrayList) sendMessagesHelper.delayedMessages.get(finalPath2);
                    if (arr5 != null) {
                        a2 = 0;
                        while (a2 < arr5.size()) {
                            obj = (DelayedMessage) arr5.get(a2);
                            if (obj.type == 4) {
                                for (index = 0; index < obj.messages.size(); index++) {
                                    if (obj.messageObjects.get(index) == messageObject) {
                                        obj.markAsError();
                                        arr5.remove(a2);
                                        a2--;
                                        break;
                                    }
                                }
                            } else if (obj.obj == messageObject) {
                                obj.markAsError();
                                arr5.remove(a2);
                                a2--;
                            }
                            a2++;
                        }
                        if (arr5.isEmpty()) {
                            sendMessagesHelper.delayedMessages.remove(finalPath2);
                        }
                    }
                }
            } else if (i == NotificationCenter.httpFileDidLoaded) {
                str = args[0];
                ArrayList<DelayedMessage> arr6 = (ArrayList) sendMessagesHelper.delayedMessages.get(str);
                if (arr6 != null) {
                    a = 0;
                    while (true) {
                        a3 = a;
                        if (a3 >= arr6.size()) {
                            break;
                        }
                        MessageObject messageObject4;
                        int fileType;
                        ArrayList<DelayedMessage> arr7;
                        final DelayedMessage message5 = (DelayedMessage) arr6.get(a3);
                        if (message5.type == 0) {
                            messageObject4 = message5.obj;
                            fileType = 0;
                        } else {
                            if (message5.type == 2) {
                                fileType = 1;
                                messageObject4 = message5.obj;
                            } else if (message5.type == 4) {
                                messageObject4 = (MessageObject) message5.extraHashMap.get(str);
                                if (messageObject4.getDocument() != null) {
                                    fileType = 1;
                                } else {
                                    fileType = 0;
                                }
                            } else {
                                fileType = -1;
                                messageObject4 = messageObject3;
                            }
                            fileType = fileType;
                        }
                        MessageObject messageObject5 = messageObject4;
                        final File cacheFile;
                        MessageObject arr8;
                        if (fileType == 0) {
                            StringBuilder stringBuilder3 = new StringBuilder();
                            stringBuilder3.append(Utilities.MD5(str));
                            stringBuilder3.append(".");
                            stringBuilder3.append(ImageLoader.getHttpUrlExtension(str, "file"));
                            String md5 = stringBuilder3.toString();
                            cacheFile = new File(FileLoader.getDirectory(4), md5);
                            AnonymousClass3 anonymousClass3 = r0;
                            DispatchQueue dispatchQueue = Utilities.globalQueue;
                            final MessageObject messageObject6 = messageObject5;
                            obj = message5;
                            arr7 = arr6;
                            arr8 = messageObject5;
                            final String messageObject7 = str;
                            AnonymousClass3 anonymousClass32 = new Runnable() {
                                public void run() {
                                    final TL_photo photo = SendMessagesHelper.this.generatePhotoSizes(cacheFile.toString(), null);
                                    AndroidUtilities.runOnUIThread(new Runnable() {
                                        public void run() {
                                            if (photo != null) {
                                                messageObject6.messageOwner.media.photo = photo;
                                                messageObject6.messageOwner.attachPath = cacheFile.toString();
                                                ArrayList<Message> messages = new ArrayList();
                                                messages.add(messageObject6.messageOwner);
                                                MessagesStorage.getInstance(SendMessagesHelper.this.currentAccount).putMessages((ArrayList) messages, false, true, false, 0);
                                                NotificationCenter.getInstance(SendMessagesHelper.this.currentAccount).postNotificationName(NotificationCenter.updateMessageMedia, messageObject6.messageOwner);
                                                obj.location = ((PhotoSize) photo.sizes.get(photo.sizes.size() - 1)).location;
                                                obj.httpLocation = null;
                                                if (obj.type == 4) {
                                                    SendMessagesHelper.this.performSendDelayedMessage(obj, obj.messageObjects.indexOf(messageObject6));
                                                } else {
                                                    SendMessagesHelper.this.performSendDelayedMessage(obj);
                                                }
                                                return;
                                            }
                                            if (BuildVars.LOGS_ENABLED) {
                                                StringBuilder stringBuilder = new StringBuilder();
                                                stringBuilder.append("can't load image ");
                                                stringBuilder.append(messageObject7);
                                                stringBuilder.append(" to file ");
                                                stringBuilder.append(cacheFile.toString());
                                                FileLog.e(stringBuilder.toString());
                                            }
                                            obj.markAsError();
                                        }
                                    });
                                }
                            };
                            dispatchQueue.postRunnable(anonymousClass3);
                        } else {
                            arr7 = arr6;
                            arr8 = messageObject5;
                            if (fileType == 1) {
                                finalPath2 = new StringBuilder();
                                finalPath2.append(Utilities.MD5(str));
                                finalPath2.append(".gif");
                                cacheFile = new File(FileLoader.getDirectory(4), finalPath2.toString());
                                Utilities.globalQueue.postRunnable(new Runnable() {
                                    public void run() {
                                        final Document document = message5.obj.getDocument();
                                        if (document.thumb.location instanceof TL_fileLocationUnavailable) {
                                            try {
                                                boolean z = true;
                                                Bitmap bitmap = ImageLoader.loadBitmap(cacheFile.getAbsolutePath(), null, 90.0f, 90.0f, true);
                                                if (bitmap != null) {
                                                    if (message5.sendEncryptedRequest == null) {
                                                        z = false;
                                                    }
                                                    document.thumb = ImageLoader.scaleAndSaveImage(bitmap, 90.0f, 90.0f, 55, z);
                                                    bitmap.recycle();
                                                }
                                            } catch (Throwable e) {
                                                document.thumb = null;
                                                FileLog.e(e);
                                            }
                                            if (document.thumb == null) {
                                                document.thumb = new TL_photoSizeEmpty();
                                                document.thumb.type = "s";
                                            }
                                        }
                                        AndroidUtilities.runOnUIThread(new Runnable() {
                                            public void run() {
                                                message5.httpLocation = null;
                                                message5.obj.messageOwner.attachPath = cacheFile.toString();
                                                message5.location = document.thumb.location;
                                                ArrayList<Message> messages = new ArrayList();
                                                messages.add(arr8.messageOwner);
                                                MessagesStorage.getInstance(SendMessagesHelper.this.currentAccount).putMessages((ArrayList) messages, false, true, false, 0);
                                                SendMessagesHelper.this.performSendDelayedMessage(message5);
                                                NotificationCenter.getInstance(SendMessagesHelper.this.currentAccount).postNotificationName(NotificationCenter.updateMessageMedia, message5.obj.messageOwner);
                                            }
                                        });
                                    }
                                });
                                a = a3 + 1;
                                arr6 = arr7;
                                messageObject3 = null;
                            }
                        }
                        a = a3 + 1;
                        arr6 = arr7;
                        messageObject3 = null;
                    }
                    sendMessagesHelper.delayedMessages.remove(str);
                }
            } else if (i == NotificationCenter.FileDidLoaded) {
                location = args[0];
                ArrayList<DelayedMessage> arr9 = (ArrayList) sendMessagesHelper.delayedMessages.get(location);
                if (arr9 != null) {
                    a = 0;
                    while (true) {
                        index2 = a;
                        if (index2 >= arr9.size()) {
                            break;
                        }
                        performSendDelayedMessage((DelayedMessage) arr9.get(index2));
                        a = index2 + 1;
                    }
                    sendMessagesHelper.delayedMessages.remove(location);
                }
            } else if (i == NotificationCenter.httpFileDidFailedLoad || i == NotificationCenter.FileDidFailedLoad) {
                fileType = 0;
                finalPath2 = args[0];
                arr5 = (ArrayList) sendMessagesHelper.delayedMessages.get(finalPath2);
                if (arr5 != null) {
                    while (fileType < arr5.size()) {
                        ((DelayedMessage) arr5.get(fileType)).markAsError();
                        fileType++;
                    }
                    sendMessagesHelper.delayedMessages.remove(finalPath2);
                }
            }
        }
    }

    public void cancelSendingMessage(MessageObject object) {
        MessageObject messageObject = object;
        ArrayList<String> keysToRemove = new ArrayList();
        boolean enc = false;
        for (Entry<String, ArrayList<DelayedMessage>> entry : this.delayedMessages.entrySet()) {
            ArrayList<DelayedMessage> messages = (ArrayList) entry.getValue();
            int a = 0;
            while (a < messages.size()) {
                DelayedMessage message = (DelayedMessage) messages.get(a);
                if (message.type == 4) {
                    int index = -1;
                    MessageObject messageObject2 = null;
                    for (int b = 0; b < message.messageObjects.size(); b++) {
                        messageObject2 = (MessageObject) message.messageObjects.get(b);
                        if (messageObject2.getId() == object.getId()) {
                            index = b;
                            break;
                        }
                    }
                    if (index >= 0) {
                        message.messageObjects.remove(index);
                        message.messages.remove(index);
                        message.originalPaths.remove(index);
                        if (message.sendRequest != null) {
                            message.sendRequest.multi_media.remove(index);
                        } else {
                            TL_messages_sendEncryptedMultiMedia request = message.sendEncryptedRequest;
                            request.messages.remove(index);
                            request.files.remove(index);
                        }
                        MediaController.getInstance().cancelVideoConvert(messageObject);
                        String keyToRemove = (String) message.extraHashMap.get(messageObject2);
                        if (keyToRemove != null) {
                            keysToRemove.add(keyToRemove);
                        }
                        if (message.messageObjects.isEmpty()) {
                            message.sendDelayedRequests();
                        } else {
                            if (message.finalGroupMessage == object.getId()) {
                                MessageObject prevMessage = (MessageObject) message.messageObjects.get(message.messageObjects.size() - 1);
                                message.finalGroupMessage = prevMessage.getId();
                                prevMessage.messageOwner.params.put("final", "1");
                                messages_Messages messagesRes = new TL_messages_messages();
                                messagesRes.messages.add(prevMessage.messageOwner);
                                MessagesStorage.getInstance(r0.currentAccount).putMessages(messagesRes, message.peer, -2, 0, false);
                            }
                            sendReadyToSendGroup(message, false, true);
                        }
                    }
                } else if (message.obj.getId() == object.getId()) {
                    messages.remove(a);
                    message.sendDelayedRequests();
                    MediaController.getInstance().cancelVideoConvert(message.obj);
                    if (messages.size() == 0) {
                        keysToRemove.add(entry.getKey());
                        if (message.sendEncryptedRequest != null) {
                            enc = true;
                        }
                    }
                } else {
                    a++;
                }
            }
        }
        int a2 = 0;
        while (true) {
            int a3 = a2;
            if (a3 < keysToRemove.size()) {
                String key = (String) keysToRemove.get(a3);
                if (key.startsWith("http")) {
                    ImageLoader.getInstance().cancelLoadHttpFile(key);
                } else {
                    FileLoader.getInstance(r0.currentAccount).cancelUploadFile(key, enc);
                }
                stopVideoService(key);
                r0.delayedMessages.remove(key);
                a2 = a3 + 1;
            } else {
                ArrayList<Integer> messages2 = new ArrayList();
                messages2.add(Integer.valueOf(object.getId()));
                MessagesController.getInstance(r0.currentAccount).deleteMessages(messages2, null, null, messageObject.messageOwner.to_id.channel_id, false);
                return;
            }
        }
    }

    public boolean retrySendMessage(MessageObject messageObject, boolean unsent) {
        if (messageObject.getId() >= 0) {
            return false;
        }
        if (messageObject.messageOwner.action instanceof TL_messageEncryptedAction) {
            EncryptedChat encryptedChat = MessagesController.getInstance(this.currentAccount).getEncryptedChat(Integer.valueOf((int) (messageObject.getDialogId() >> 32)));
            if (encryptedChat == null) {
                MessagesStorage.getInstance(this.currentAccount).markMessageAsSendError(messageObject.messageOwner);
                messageObject.messageOwner.send_state = 2;
                NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.messageSendError, Integer.valueOf(messageObject.getId()));
                processSentMessage(messageObject.getId());
                return false;
            }
            if (messageObject.messageOwner.random_id == 0) {
                messageObject.messageOwner.random_id = getNextRandomId();
            }
            if (messageObject.messageOwner.action.encryptedAction instanceof TL_decryptedMessageActionSetMessageTTL) {
                SecretChatHelper.getInstance(this.currentAccount).sendTTLMessage(encryptedChat, messageObject.messageOwner);
            } else if (messageObject.messageOwner.action.encryptedAction instanceof TL_decryptedMessageActionDeleteMessages) {
                SecretChatHelper.getInstance(this.currentAccount).sendMessagesDeleteMessage(encryptedChat, null, messageObject.messageOwner);
            } else if (messageObject.messageOwner.action.encryptedAction instanceof TL_decryptedMessageActionFlushHistory) {
                SecretChatHelper.getInstance(this.currentAccount).sendClearHistoryMessage(encryptedChat, messageObject.messageOwner);
            } else if (messageObject.messageOwner.action.encryptedAction instanceof TL_decryptedMessageActionNotifyLayer) {
                SecretChatHelper.getInstance(this.currentAccount).sendNotifyLayerMessage(encryptedChat, messageObject.messageOwner);
            } else if (messageObject.messageOwner.action.encryptedAction instanceof TL_decryptedMessageActionReadMessages) {
                SecretChatHelper.getInstance(this.currentAccount).sendMessagesReadMessage(encryptedChat, null, messageObject.messageOwner);
            } else if (messageObject.messageOwner.action.encryptedAction instanceof TL_decryptedMessageActionScreenshotMessages) {
                SecretChatHelper.getInstance(this.currentAccount).sendScreenshotMessage(encryptedChat, null, messageObject.messageOwner);
            } else if (!(messageObject.messageOwner.action.encryptedAction instanceof TL_decryptedMessageActionTyping)) {
                if (!(messageObject.messageOwner.action.encryptedAction instanceof TL_decryptedMessageActionResend)) {
                    if (messageObject.messageOwner.action.encryptedAction instanceof TL_decryptedMessageActionCommitKey) {
                        SecretChatHelper.getInstance(this.currentAccount).sendCommitKeyMessage(encryptedChat, messageObject.messageOwner);
                    } else if (messageObject.messageOwner.action.encryptedAction instanceof TL_decryptedMessageActionAbortKey) {
                        SecretChatHelper.getInstance(this.currentAccount).sendAbortKeyMessage(encryptedChat, messageObject.messageOwner, 0);
                    } else if (messageObject.messageOwner.action.encryptedAction instanceof TL_decryptedMessageActionRequestKey) {
                        SecretChatHelper.getInstance(this.currentAccount).sendRequestKeyMessage(encryptedChat, messageObject.messageOwner);
                    } else if (messageObject.messageOwner.action.encryptedAction instanceof TL_decryptedMessageActionAcceptKey) {
                        SecretChatHelper.getInstance(this.currentAccount).sendAcceptKeyMessage(encryptedChat, messageObject.messageOwner);
                    } else if (messageObject.messageOwner.action.encryptedAction instanceof TL_decryptedMessageActionNoop) {
                        SecretChatHelper.getInstance(this.currentAccount).sendNoopMessage(encryptedChat, messageObject.messageOwner);
                    }
                }
            }
            return true;
        }
        if (messageObject.messageOwner.action instanceof TL_messageActionScreenshotTaken) {
            sendScreenshotMessage(MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf((int) messageObject.getDialogId())), messageObject.messageOwner.reply_to_msg_id, messageObject.messageOwner);
        }
        if (unsent) {
            this.unsentMessages.put(messageObject.getId(), messageObject);
        }
        sendMessage(messageObject);
        return true;
    }

    protected void processSentMessage(int id) {
        int prevSize = this.unsentMessages.size();
        this.unsentMessages.remove(id);
        if (prevSize != 0 && this.unsentMessages.size() == 0) {
            checkUnsentMessages();
        }
    }

    public void processForwardFromMyName(MessageObject messageObject, long did) {
        SendMessagesHelper sendMessagesHelper = this;
        MessageObject messageObject2 = messageObject;
        long j = did;
        if (messageObject2 != null) {
            ArrayList<MessageObject> arrayList;
            if (messageObject2.messageOwner.media == null || (messageObject2.messageOwner.media instanceof TL_messageMediaEmpty) || (messageObject2.messageOwner.media instanceof TL_messageMediaWebPage) || (messageObject2.messageOwner.media instanceof TL_messageMediaGame) || (messageObject2.messageOwner.media instanceof TL_messageMediaInvoice)) {
                if (messageObject2.messageOwner.message != null) {
                    ArrayList<MessageEntity> arrayList2;
                    WebPage webPage = null;
                    if (messageObject2.messageOwner.media instanceof TL_messageMediaWebPage) {
                        webPage = messageObject2.messageOwner.media.webpage;
                    }
                    WebPage webPage2 = webPage;
                    if (messageObject2.messageOwner.entities == null || messageObject2.messageOwner.entities.isEmpty()) {
                        arrayList2 = null;
                    } else {
                        arrayList2 = new ArrayList();
                        for (int a = 0; a < messageObject2.messageOwner.entities.size(); a++) {
                            MessageEntity entity = (MessageEntity) messageObject2.messageOwner.entities.get(a);
                            if ((entity instanceof TL_messageEntityBold) || (entity instanceof TL_messageEntityItalic) || (entity instanceof TL_messageEntityPre) || (entity instanceof TL_messageEntityCode) || (entity instanceof TL_messageEntityTextUrl)) {
                                arrayList2.add(entity);
                            }
                        }
                    }
                    long j2 = j;
                    sendMessage(messageObject2.messageOwner.message, j2, messageObject2.replyMessageObject, webPage2, true, arrayList2, null, null);
                } else if (((int) j) != 0) {
                    arrayList = new ArrayList();
                    arrayList.add(messageObject2);
                    sendMessage(arrayList, j);
                }
            } else if (messageObject2.messageOwner.media.photo instanceof TL_photo) {
                sendMessage((TL_photo) messageObject2.messageOwner.media.photo, null, j, messageObject2.replyMessageObject, messageObject2.messageOwner.message, messageObject2.messageOwner.entities, null, null, messageObject2.messageOwner.media.ttl_seconds);
            } else if (messageObject2.messageOwner.media.document instanceof TL_document) {
                sendMessage((TL_document) messageObject2.messageOwner.media.document, null, messageObject2.messageOwner.attachPath, j, messageObject2.replyMessageObject, messageObject2.messageOwner.message, messageObject2.messageOwner.entities, null, null, messageObject2.messageOwner.media.ttl_seconds);
            } else {
                if (!(messageObject2.messageOwner.media instanceof TL_messageMediaVenue)) {
                    if (!(messageObject2.messageOwner.media instanceof TL_messageMediaGeo)) {
                        if (messageObject2.messageOwner.media.phone_number != null) {
                            User user = new TL_userContact_old2();
                            user.phone = messageObject2.messageOwner.media.phone_number;
                            user.first_name = messageObject2.messageOwner.media.first_name;
                            user.last_name = messageObject2.messageOwner.media.last_name;
                            user.id = messageObject2.messageOwner.media.user_id;
                            sendMessage(user, j, messageObject2.replyMessageObject, null, null);
                        } else if (((int) j) != 0) {
                            arrayList = new ArrayList();
                            arrayList.add(messageObject2);
                            sendMessage(arrayList, j);
                        }
                    }
                }
                sendMessage(messageObject2.messageOwner.media, j, messageObject2.replyMessageObject, null, null);
            }
        }
    }

    public void sendScreenshotMessage(User user, int messageId, Message resendMessage) {
        if (!(user == null || messageId == 0)) {
            if (user.id != UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                Message message;
                TL_messages_sendScreenshotNotification req = new TL_messages_sendScreenshotNotification();
                req.peer = new TL_inputPeerUser();
                req.peer.access_hash = user.access_hash;
                req.peer.user_id = user.id;
                if (resendMessage != null) {
                    message = resendMessage;
                    req.reply_to_msg_id = messageId;
                    req.random_id = resendMessage.random_id;
                } else {
                    message = new TL_messageService();
                    message.random_id = getNextRandomId();
                    message.dialog_id = (long) user.id;
                    message.unread = true;
                    message.out = true;
                    int newMessageId = UserConfig.getInstance(this.currentAccount).getNewMessageId();
                    message.id = newMessageId;
                    message.local_id = newMessageId;
                    message.from_id = UserConfig.getInstance(this.currentAccount).getClientUserId();
                    message.flags |= 256;
                    message.flags |= 8;
                    message.reply_to_msg_id = messageId;
                    message.to_id = new TL_peerUser();
                    message.to_id.user_id = user.id;
                    message.date = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
                    message.action = new TL_messageActionScreenshotTaken();
                    UserConfig.getInstance(this.currentAccount).saveConfig(false);
                }
                req.random_id = message.random_id;
                MessageObject newMsgObj = new MessageObject(this.currentAccount, message, false);
                newMsgObj.messageOwner.send_state = 1;
                ArrayList<MessageObject> objArr = new ArrayList();
                objArr.add(newMsgObj);
                MessagesController.getInstance(this.currentAccount).updateInterfaceWithMessages(message.dialog_id, objArr);
                NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
                ArrayList<Message> arr = new ArrayList();
                arr.add(message);
                MessagesStorage.getInstance(this.currentAccount).putMessages((ArrayList) arr, false, true, false, 0);
                performSendMessageRequest(req, newMsgObj, null);
            }
        }
    }

    public void sendSticker(Document document, long peer, MessageObject replyingMessageObject) {
        Document document2 = document;
        long j = peer;
        if (document2 != null) {
            SendMessagesHelper sendMessagesHelper;
            if (((int) j) == 0) {
                if (MessagesController.getInstance(this.currentAccount).getEncryptedChat(Integer.valueOf((int) (j >> 32))) != null) {
                    Document newDocument = new TL_document();
                    newDocument.id = document2.id;
                    newDocument.access_hash = document2.access_hash;
                    newDocument.date = document2.date;
                    newDocument.mime_type = document2.mime_type;
                    newDocument.size = document2.size;
                    newDocument.dc_id = document2.dc_id;
                    newDocument.attributes = new ArrayList(document2.attributes);
                    if (newDocument.mime_type == null) {
                        newDocument.mime_type = TtmlNode.ANONYMOUS_REGION_ID;
                    }
                    if (document2.thumb instanceof TL_photoSize) {
                        File file = FileLoader.getPathToAttach(document2.thumb, true);
                        if (file.exists()) {
                            try {
                                int len = (int) file.length();
                                byte[] arr = new byte[((int) file.length())];
                                new RandomAccessFile(file, "r").readFully(arr);
                                newDocument.thumb = new TL_photoCachedSize();
                                newDocument.thumb.location = document2.thumb.location;
                                newDocument.thumb.size = document2.thumb.size;
                                newDocument.thumb.w = document2.thumb.w;
                                newDocument.thumb.h = document2.thumb.h;
                                newDocument.thumb.type = document2.thumb.type;
                                newDocument.thumb.bytes = arr;
                            } catch (Throwable e) {
                                FileLog.e(e);
                            }
                        }
                    }
                    if (newDocument.thumb == null) {
                        newDocument.thumb = new TL_photoSizeEmpty();
                        newDocument.thumb.type = "s";
                    }
                    document2 = newDocument;
                } else {
                    return;
                }
            }
            sendMessagesHelper = this;
            Document document3 = document2;
            if (document3 instanceof TL_document) {
                sendMessagesHelper.sendMessage((TL_document) document3, null, null, j, replyingMessageObject, null, null, null, null, null);
            }
        }
    }

    public int sendMessage(ArrayList<MessageObject> messages, long peer) {
        SendMessagesHelper sendMessagesHelper;
        long j;
        SendMessagesHelper sendMessagesHelper2 = this;
        ArrayList<MessageObject> arrayList = messages;
        long j2 = peer;
        if (arrayList != null) {
            if (!messages.isEmpty()) {
                int lower_id = (int) j2;
                int sendResult = 0;
                int a;
                if (lower_id == 0) {
                    int a2 = 0;
                    while (true) {
                        a = a2;
                        if (a >= messages.size()) {
                            break;
                        }
                        processForwardFromMyName((MessageObject) arrayList.get(a), peer);
                        a2 = a + 1;
                    }
                } else {
                    Peer to_id = MessagesController.getInstance(sendMessagesHelper2.currentAccount).getPeer((int) j2);
                    boolean isMegagroup = false;
                    boolean isSignature = false;
                    boolean canSendStickers = true;
                    boolean canSendMedia = true;
                    boolean canSendPreview = true;
                    if (lower_id <= 0) {
                        Chat chat = MessagesController.getInstance(sendMessagesHelper2.currentAccount).getChat(Integer.valueOf(-lower_id));
                        if (ChatObject.isChannel(chat)) {
                            isMegagroup = chat.megagroup;
                            isSignature = chat.signatures;
                            if (chat.banned_rights != null) {
                                canSendStickers = chat.banned_rights.send_stickers ^ 1;
                                canSendMedia = chat.banned_rights.send_media ^ 1;
                                canSendPreview = chat.banned_rights.embed_links ^ 1;
                            }
                        }
                    } else if (MessagesController.getInstance(sendMessagesHelper2.currentAccount).getUser(Integer.valueOf(lower_id)) == null) {
                        return 0;
                    }
                    boolean isMegagroup2 = isMegagroup;
                    boolean isSignature2 = isSignature;
                    boolean canSendStickers2 = canSendStickers;
                    boolean canSendMedia2 = canSendMedia;
                    boolean canSendPreview2 = canSendPreview;
                    LongSparseArray<Long> groupsMap = new LongSparseArray();
                    ArrayList<MessageObject> objArr = new ArrayList();
                    ArrayList<Message> arr = new ArrayList();
                    ArrayList randomIds = new ArrayList();
                    ArrayList<Integer> ids = new ArrayList();
                    LongSparseArray messagesByRandomIds = new LongSparseArray();
                    InputPeer inputPeer = MessagesController.getInstance(sendMessagesHelper2.currentAccount).getInputPeer(lower_id);
                    int myId = UserConfig.getInstance(sendMessagesHelper2.currentAccount).getClientUserId();
                    ArrayList<Integer> ids2 = ids;
                    InputPeer inputPeer2 = inputPeer;
                    canSendPreview = j2 == ((long) myId);
                    ArrayList<Integer> ids3 = ids2;
                    int sendResult2 = 0;
                    sendResult = 0;
                    ArrayList<Message> arrayList2 = arr;
                    ArrayList<MessageObject> objArr2 = objArr;
                    ArrayList<Message> arr2 = arrayList2;
                    while (true) {
                        int lower_id2 = lower_id;
                        if (sendResult >= messages.size()) {
                            break;
                        }
                        LongSparseArray<Long> groupsMap2;
                        InputPeer inputPeer3;
                        Peer to_id2;
                        int myId2;
                        int lower_id3;
                        ArrayList<Message> arr3;
                        ArrayList<MessageObject> objArr3;
                        ArrayList<Long> randomIds2;
                        ArrayList<Integer> ids4;
                        LongSparseArray<Message> messagesByRandomIds2;
                        MessageObject lower_id4 = (MessageObject) arrayList.get(sendResult);
                        if (lower_id4.getId() > 0) {
                            if (!lower_id4.needDrawBluredPreview()) {
                                if (canSendStickers2 || !(lower_id4.isSticker() || lower_id4.isGif() || lower_id4.isGame())) {
                                    boolean toMyself;
                                    ArrayList<Message> arr4;
                                    ArrayList<MessageObject> objArr4;
                                    MessageFwdHeader messageFwdHeader;
                                    LongSparseArray<Long> groupsMap3;
                                    LongSparseArray<Message> messagesByRandomIds3;
                                    Peer to_id3;
                                    int i;
                                    InputPeer inputPeer4;
                                    if (canSendMedia2) {
                                        toMyself = canSendPreview;
                                    } else {
                                        toMyself = canSendPreview;
                                        if ((lower_id4.messageOwner.media instanceof TL_messageMediaPhoto) || (lower_id4.messageOwner.media instanceof TL_messageMediaDocument)) {
                                            if (sendResult2 == 0) {
                                                sendResult2 = 2;
                                                groupsMap2 = groupsMap;
                                                inputPeer3 = inputPeer2;
                                                to_id2 = to_id;
                                                myId2 = myId;
                                                lower_id3 = lower_id2;
                                                canSendPreview = toMyself;
                                            } else {
                                                arr3 = arr2;
                                                groupsMap2 = groupsMap;
                                                objArr3 = objArr2;
                                                randomIds2 = randomIds;
                                                ids4 = ids3;
                                                inputPeer3 = inputPeer2;
                                                messagesByRandomIds2 = messagesByRandomIds;
                                                to_id2 = to_id;
                                                myId2 = myId;
                                                lower_id3 = lower_id2;
                                                canSendPreview = toMyself;
                                                myId = sendResult;
                                            }
                                        }
                                    }
                                    boolean groupedIdChanged = false;
                                    Message newMsg = new TL_message();
                                    ArrayList<Integer> ids5 = ids3;
                                    InputPeer inputPeer5 = inputPeer2;
                                    boolean z = lower_id4.getDialogId() == ((long) myId) && lower_id4.messageOwner.from_id == UserConfig.getInstance(sendMessagesHelper2.currentAccount).getClientUserId();
                                    boolean forwardFromSaved = z;
                                    if (lower_id4.isForwarded()) {
                                        newMsg.fwd_from = new TL_messageFwdHeader();
                                        newMsg.fwd_from.flags = lower_id4.messageOwner.fwd_from.flags;
                                        newMsg.fwd_from.from_id = lower_id4.messageOwner.fwd_from.from_id;
                                        newMsg.fwd_from.date = lower_id4.messageOwner.fwd_from.date;
                                        newMsg.fwd_from.channel_id = lower_id4.messageOwner.fwd_from.channel_id;
                                        newMsg.fwd_from.channel_post = lower_id4.messageOwner.fwd_from.channel_post;
                                        newMsg.fwd_from.post_author = lower_id4.messageOwner.fwd_from.post_author;
                                        newMsg.flags = 4;
                                        arr4 = arr2;
                                        objArr4 = objArr2;
                                    } else if (forwardFromSaved) {
                                        arr4 = arr2;
                                        objArr4 = objArr2;
                                    } else {
                                        newMsg.fwd_from = new TL_messageFwdHeader();
                                        newMsg.fwd_from.channel_post = lower_id4.getId();
                                        MessageFwdHeader messageFwdHeader2 = newMsg.fwd_from;
                                        messageFwdHeader2.flags |= 4;
                                        if (lower_id4.isFromUser()) {
                                            newMsg.fwd_from.from_id = lower_id4.messageOwner.from_id;
                                            messageFwdHeader2 = newMsg.fwd_from;
                                            messageFwdHeader2.flags |= 1;
                                        } else {
                                            newMsg.fwd_from.channel_id = lower_id4.messageOwner.to_id.channel_id;
                                            messageFwdHeader2 = newMsg.fwd_from;
                                            messageFwdHeader2.flags |= 2;
                                            if (lower_id4.messageOwner.post && lower_id4.messageOwner.from_id > 0) {
                                                newMsg.fwd_from.from_id = lower_id4.messageOwner.from_id;
                                                messageFwdHeader2 = newMsg.fwd_from;
                                                messageFwdHeader2.flags |= 1;
                                            }
                                        }
                                        if (lower_id4.messageOwner.post_author != null) {
                                            newMsg.fwd_from.post_author = lower_id4.messageOwner.post_author;
                                            messageFwdHeader2 = newMsg.fwd_from;
                                            messageFwdHeader2.flags |= 8;
                                            arr4 = arr2;
                                            objArr4 = objArr2;
                                        } else {
                                            if (!lower_id4.isOutOwner() && lower_id4.messageOwner.from_id > 0 && lower_id4.messageOwner.post) {
                                                User signUser = MessagesController.getInstance(sendMessagesHelper2.currentAccount).getUser(Integer.valueOf(lower_id4.messageOwner.from_id));
                                                if (signUser != null) {
                                                    arr4 = arr2;
                                                    objArr4 = objArr2;
                                                    newMsg.fwd_from.post_author = ContactsController.formatName(signUser.first_name, signUser.last_name);
                                                    messageFwdHeader = newMsg.fwd_from;
                                                    messageFwdHeader.flags |= 8;
                                                }
                                            }
                                            arr4 = arr2;
                                            objArr4 = objArr2;
                                        }
                                        newMsg.date = lower_id4.messageOwner.date;
                                        newMsg.flags = 4;
                                    }
                                    if (j2 == ((long) myId) && newMsg.fwd_from != null) {
                                        messageFwdHeader = newMsg.fwd_from;
                                        messageFwdHeader.flags |= 16;
                                        newMsg.fwd_from.saved_from_msg_id = lower_id4.getId();
                                        newMsg.fwd_from.saved_from_peer = lower_id4.messageOwner.to_id;
                                    }
                                    if (canSendPreview2 || !(lower_id4.messageOwner.media instanceof TL_messageMediaWebPage)) {
                                        newMsg.media = lower_id4.messageOwner.media;
                                    } else {
                                        newMsg.media = new TL_messageMediaEmpty();
                                    }
                                    if (newMsg.media != null) {
                                        newMsg.flags |= 512;
                                    }
                                    if (isMegagroup2) {
                                        newMsg.flags |= Integer.MIN_VALUE;
                                    }
                                    if (lower_id4.messageOwner.via_bot_id != 0) {
                                        newMsg.via_bot_id = lower_id4.messageOwner.via_bot_id;
                                        newMsg.flags |= 2048;
                                    }
                                    newMsg.message = lower_id4.messageOwner.message;
                                    newMsg.fwd_msg_id = lower_id4.getId();
                                    newMsg.attachPath = lower_id4.messageOwner.attachPath;
                                    newMsg.entities = lower_id4.messageOwner.entities;
                                    if (!newMsg.entities.isEmpty()) {
                                        newMsg.flags |= 128;
                                    }
                                    if (newMsg.attachPath == null) {
                                        newMsg.attachPath = TtmlNode.ANONYMOUS_REGION_ID;
                                    }
                                    a = UserConfig.getInstance(sendMessagesHelper2.currentAccount).getNewMessageId();
                                    newMsg.id = a;
                                    newMsg.local_id = a;
                                    newMsg.out = true;
                                    long j3 = lower_id4.messageOwner.grouped_id;
                                    long lastGroupedId = j3;
                                    if (j3 != 0) {
                                        Long gId = (Long) groupsMap.get(lower_id4.messageOwner.grouped_id);
                                        if (gId == null) {
                                            gId = Long.valueOf(Utilities.random.nextLong());
                                            groupsMap.put(lower_id4.messageOwner.grouped_id, gId);
                                        }
                                        newMsg.grouped_id = gId.longValue();
                                        newMsg.flags |= 131072;
                                    }
                                    if (sendResult != messages.size() - 1) {
                                        MessageObject next = (MessageObject) arrayList.get(sendResult + 1);
                                        groupsMap3 = groupsMap;
                                        if (next.messageOwner.grouped_id != lower_id4.messageOwner.grouped_id) {
                                            groupedIdChanged = true;
                                        }
                                    } else {
                                        groupsMap3 = groupsMap;
                                    }
                                    if (to_id.channel_id == 0 || isMegagroup2) {
                                        newMsg.from_id = UserConfig.getInstance(sendMessagesHelper2.currentAccount).getClientUserId();
                                        newMsg.flags |= 256;
                                    } else {
                                        newMsg.from_id = isSignature2 ? UserConfig.getInstance(sendMessagesHelper2.currentAccount).getClientUserId() : -to_id.channel_id;
                                        newMsg.post = true;
                                    }
                                    if (newMsg.random_id == 0) {
                                        newMsg.random_id = getNextRandomId();
                                    }
                                    randomIds.add(Long.valueOf(newMsg.random_id));
                                    messagesByRandomIds.put(newMsg.random_id, newMsg);
                                    ArrayList<Integer> ids6 = ids5;
                                    ids6.add(Integer.valueOf(newMsg.fwd_msg_id));
                                    newMsg.date = ConnectionsManager.getInstance(sendMessagesHelper2.currentAccount).getCurrentTime();
                                    InputPeer inputPeer6 = inputPeer5;
                                    if (!(inputPeer6 instanceof TL_inputPeerChannel)) {
                                        if ((lower_id4.messageOwner.flags & 1024) != 0) {
                                            newMsg.views = lower_id4.messageOwner.views;
                                            newMsg.flags |= 1024;
                                        }
                                        newMsg.unread = true;
                                    } else if (isMegagroup2) {
                                        newMsg.unread = true;
                                    } else {
                                        newMsg.views = 1;
                                        newMsg.flags |= 1024;
                                    }
                                    newMsg.dialog_id = j2;
                                    newMsg.to_id = to_id;
                                    if (MessageObject.isVoiceMessage(newMsg) || MessageObject.isRoundVideoMessage(newMsg)) {
                                        newMsg.media_unread = true;
                                    }
                                    if (lower_id4.messageOwner.to_id instanceof TL_peerChannel) {
                                        newMsg.ttl = -lower_id4.messageOwner.to_id.channel_id;
                                    }
                                    MessageObject newMsgObj = new MessageObject(sendMessagesHelper2.currentAccount, newMsg, true);
                                    newMsgObj.messageOwner.send_state = 1;
                                    ArrayList<MessageObject> objArr5 = objArr4;
                                    objArr5.add(newMsgObj);
                                    final ArrayList<Message> inputPeer7 = arr4;
                                    inputPeer7.add(newMsg);
                                    sendMessagesHelper2.putToSendingMessages(newMsg);
                                    MessageObject newMsgObj2;
                                    Message newMsg2;
                                    if (BuildVars.LOGS_ENABLED) {
                                        newMsgObj2 = newMsgObj;
                                        StringBuilder stringBuilder = new StringBuilder();
                                        newMsg2 = newMsg;
                                        stringBuilder.append("forward message user_id = ");
                                        stringBuilder.append(inputPeer6.user_id);
                                        stringBuilder.append(" chat_id = ");
                                        stringBuilder.append(inputPeer6.chat_id);
                                        stringBuilder.append(" channel_id = ");
                                        stringBuilder.append(inputPeer6.channel_id);
                                        stringBuilder.append(" access_hash = ");
                                        messagesByRandomIds3 = messagesByRandomIds;
                                        to_id3 = to_id;
                                        stringBuilder.append(inputPeer6.access_hash);
                                        FileLog.d(stringBuilder.toString());
                                    } else {
                                        newMsgObj2 = newMsgObj;
                                        newMsg2 = newMsg;
                                        messagesByRandomIds3 = messagesByRandomIds;
                                        to_id3 = to_id;
                                    }
                                    if (!((groupedIdChanged && inputPeer7.size() > 0) || inputPeer7.size() == 100 || sendResult == messages.size() - 1)) {
                                        if (sendResult == messages.size() - 1 || ((MessageObject) arrayList.get(sendResult + 1)).getDialogId() == lower_id4.getDialogId()) {
                                            inputPeer3 = inputPeer6;
                                            ids4 = ids6;
                                            randomIds2 = randomIds;
                                            arr3 = inputPeer7;
                                            myId2 = myId;
                                            lower_id3 = lower_id2;
                                            canSendPreview = toMyself;
                                            groupsMap2 = groupsMap3;
                                            to_id2 = to_id3;
                                            messagesByRandomIds2 = messagesByRandomIds3;
                                            myId = sendResult;
                                            objArr3 = objArr5;
                                        }
                                    }
                                    MessagesStorage.getInstance(sendMessagesHelper2.currentAccount).putMessages(new ArrayList(inputPeer7), false, true, false, 0);
                                    MessagesController.getInstance(sendMessagesHelper2.currentAccount).updateInterfaceWithMessages(j2, objArr5);
                                    NotificationCenter.getInstance(sendMessagesHelper2.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
                                    UserConfig.getInstance(sendMessagesHelper2.currentAccount).saveConfig(false);
                                    TL_messages_forwardMessages req = new TL_messages_forwardMessages();
                                    req.to_peer = inputPeer6;
                                    req.grouped = lastGroupedId != 0;
                                    if (req.to_peer instanceof TL_inputPeerChannel) {
                                        SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(sendMessagesHelper2.currentAccount);
                                        StringBuilder stringBuilder2 = new StringBuilder();
                                        stringBuilder2.append("silent_");
                                        stringBuilder2.append(j2);
                                        req.silent = notificationsSettings.getBoolean(stringBuilder2.toString(), false);
                                    }
                                    if (lower_id4.messageOwner.to_id instanceof TL_peerChannel) {
                                        Chat chat2 = MessagesController.getInstance(sendMessagesHelper2.currentAccount).getChat(Integer.valueOf(lower_id4.messageOwner.to_id.channel_id));
                                        req.from_peer = new TL_inputPeerChannel();
                                        req.from_peer.channel_id = lower_id4.messageOwner.to_id.channel_id;
                                        if (chat2 != null) {
                                            i = sendResult;
                                            inputPeer4 = inputPeer6;
                                            req.from_peer.access_hash = chat2.access_hash;
                                        } else {
                                            i = sendResult;
                                            inputPeer4 = inputPeer6;
                                        }
                                    } else {
                                        i = sendResult;
                                        inputPeer4 = inputPeer6;
                                        req.from_peer = new TL_inputPeerEmpty();
                                    }
                                    req.random_id = randomIds;
                                    req.id = ids6;
                                    boolean z2 = messages.size() == 1 && ((MessageObject) arrayList.get(0)).messageOwner.with_my_score;
                                    req.with_my_score = z2;
                                    inputPeer3 = inputPeer4;
                                    messagesByRandomIds2 = messagesByRandomIds3;
                                    final ArrayList<MessageObject> newMsgArr = objArr5;
                                    int i2 = 1;
                                    ids4 = ids6;
                                    ArrayList<MessageObject> objArr6 = objArr5;
                                    final LongSparseArray<Message> messagesByRandomIdsFinal = messagesByRandomIds2;
                                    randomIds2 = randomIds;
                                    canSendMedia = isMegagroup2;
                                    myId2 = myId;
                                    myId = i;
                                    arr3 = inputPeer7;
                                    SendMessagesHelper sendMessagesHelper3 = sendMessagesHelper2;
                                    RequestDelegate requestDelegate = r0;
                                    groupsMap2 = groupsMap3;
                                    objArr3 = objArr6;
                                    final long j4 = j2;
                                    ConnectionsManager instance = ConnectionsManager.getInstance(sendMessagesHelper2.currentAccount);
                                    to_id2 = to_id3;
                                    to_id = to_id2;
                                    int lower_id5 = lower_id2;
                                    lower_id = req;
                                    lower_id3 = lower_id5;
                                    TL_messages_forwardMessages req2 = req;
                                    canSendPreview = toMyself;
                                    AnonymousClass5 anonymousClass5 = new RequestDelegate() {

                                        class AnonymousClass1 implements Runnable {
                                            final /* synthetic */ Message val$message;
                                            final /* synthetic */ Message val$newMsgObj;
                                            final /* synthetic */ int val$oldId;
                                            final /* synthetic */ ArrayList val$sentMessages;

                                            AnonymousClass1(Message message, int i, ArrayList arrayList, Message message2) {
                                                this.val$newMsgObj = message;
                                                this.val$oldId = i;
                                                this.val$sentMessages = arrayList;
                                                this.val$message = message2;
                                            }

                                            public void run() {
                                                MessagesStorage.getInstance(SendMessagesHelper.this.currentAccount).updateMessageStateAndId(this.val$newMsgObj.random_id, Integer.valueOf(this.val$oldId), this.val$newMsgObj.id, 0, false, to_id.channel_id);
                                                MessagesStorage.getInstance(SendMessagesHelper.this.currentAccount).putMessages(this.val$sentMessages, true, false, false, 0);
                                                AndroidUtilities.runOnUIThread(new Runnable() {
                                                    public void run() {
                                                        AnonymousClass1.this.val$newMsgObj.send_state = 0;
                                                        DataQuery.getInstance(SendMessagesHelper.this.currentAccount).increasePeerRaiting(j4);
                                                        NotificationCenter.getInstance(SendMessagesHelper.this.currentAccount).postNotificationName(NotificationCenter.messageReceivedByServer, Integer.valueOf(AnonymousClass1.this.val$oldId), Integer.valueOf(AnonymousClass1.this.val$message.id), AnonymousClass1.this.val$message, Long.valueOf(j4));
                                                        SendMessagesHelper.this.processSentMessage(AnonymousClass1.this.val$oldId);
                                                        SendMessagesHelper.this.removeFromSendingMessages(AnonymousClass1.this.val$oldId);
                                                    }
                                                });
                                            }
                                        }

                                        class AnonymousClass2 implements Runnable {
                                            final /* synthetic */ TL_error val$error;

                                            AnonymousClass2(TL_error tL_error) {
                                                this.val$error = tL_error;
                                            }

                                            public void run() {
                                                AlertsCreator.processError(SendMessagesHelper.this.currentAccount, this.val$error, null, lower_id, new Object[0]);
                                            }
                                        }

                                        class AnonymousClass3 implements Runnable {
                                            final /* synthetic */ Message val$newMsgObj;

                                            AnonymousClass3(Message message) {
                                                this.val$newMsgObj = message;
                                            }

                                            public void run() {
                                                this.val$newMsgObj.send_state = 2;
                                                NotificationCenter.getInstance(SendMessagesHelper.this.currentAccount).postNotificationName(NotificationCenter.messageSendError, Integer.valueOf(this.val$newMsgObj.id));
                                                SendMessagesHelper.this.processSentMessage(this.val$newMsgObj.id);
                                                SendMessagesHelper.this.removeFromSendingMessages(this.val$newMsgObj.id);
                                            }
                                        }

                                        public void run(org.telegram.tgnet.TLObject r1, org.telegram.tgnet.TLRPC.TL_error r2) {
                                            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.SendMessagesHelper.5.run(org.telegram.tgnet.TLObject, org.telegram.tgnet.TLRPC$TL_error):void
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
                                            r6 = r25;
                                            r7 = r27;
                                            if (r7 != 0) goto L_0x01b8;
                                        L_0x0006:
                                            r0 = new org.telegram.messenger.support.SparseLongArray;
                                            r0.<init>();
                                            r9 = r0;
                                            r11 = r26;
                                            r11 = (org.telegram.tgnet.TLRPC.Updates) r11;
                                            r0 = 0;
                                        L_0x0011:
                                            r1 = r11.updates;
                                            r1 = r1.size();
                                            r12 = 1;
                                            if (r0 >= r1) goto L_0x0039;
                                        L_0x001a:
                                            r1 = r11.updates;
                                            r1 = r1.get(r0);
                                            r1 = (org.telegram.tgnet.TLRPC.Update) r1;
                                            r2 = r1 instanceof org.telegram.tgnet.TLRPC.TL_updateMessageID;
                                            if (r2 == 0) goto L_0x0037;
                                        L_0x0026:
                                            r2 = r1;
                                            r2 = (org.telegram.tgnet.TLRPC.TL_updateMessageID) r2;
                                            r3 = r2.id;
                                            r4 = r2.random_id;
                                            r9.put(r3, r4);
                                            r3 = r11.updates;
                                            r3.remove(r0);
                                            r0 = r0 + -1;
                                        L_0x0037:
                                            r0 = r0 + r12;
                                            goto L_0x0011;
                                        L_0x0039:
                                            r0 = org.telegram.messenger.SendMessagesHelper.this;
                                            r0 = r0.currentAccount;
                                            r0 = org.telegram.messenger.MessagesController.getInstance(r0);
                                            r0 = r0.dialogs_read_outbox_max;
                                            r1 = r2;
                                            r1 = java.lang.Long.valueOf(r1);
                                            r0 = r0.get(r1);
                                            r0 = (java.lang.Integer) r0;
                                            if (r0 != 0) goto L_0x007c;
                                        L_0x0053:
                                            r1 = org.telegram.messenger.SendMessagesHelper.this;
                                            r1 = r1.currentAccount;
                                            r1 = org.telegram.messenger.MessagesStorage.getInstance(r1);
                                            r2 = r2;
                                            r1 = r1.getDialogReadMax(r12, r2);
                                            r0 = java.lang.Integer.valueOf(r1);
                                            r1 = org.telegram.messenger.SendMessagesHelper.this;
                                            r1 = r1.currentAccount;
                                            r1 = org.telegram.messenger.MessagesController.getInstance(r1);
                                            r1 = r1.dialogs_read_outbox_max;
                                            r2 = r2;
                                            r2 = java.lang.Long.valueOf(r2);
                                            r1.put(r2, r0);
                                        L_0x007c:
                                            r13 = r0;
                                            r0 = 0;
                                            r1 = r0;
                                            r0 = 0;
                                        L_0x0080:
                                            r2 = r11.updates;
                                            r2 = r2.size();
                                            if (r0 >= r2) goto L_0x018b;
                                        L_0x0088:
                                            r2 = r11.updates;
                                            r2 = r2.get(r0);
                                            r14 = r2;
                                            r14 = (org.telegram.tgnet.TLRPC.Update) r14;
                                            r2 = r14 instanceof org.telegram.tgnet.TLRPC.TL_updateNewMessage;
                                            if (r2 != 0) goto L_0x009f;
                                        L_0x0095:
                                            r2 = r14 instanceof org.telegram.tgnet.TLRPC.TL_updateNewChannelMessage;
                                            if (r2 == 0) goto L_0x009a;
                                        L_0x0099:
                                            goto L_0x009f;
                                        L_0x009a:
                                            r15 = r0;
                                        L_0x009b:
                                            r23 = r9;
                                            goto L_0x0182;
                                        L_0x009f:
                                            r2 = r11.updates;
                                            r2.remove(r0);
                                            r15 = r0 + -1;
                                            r0 = r14 instanceof org.telegram.tgnet.TLRPC.TL_updateNewMessage;
                                            r2 = -1;
                                            if (r0 == 0) goto L_0x00c4;
                                        L_0x00ab:
                                            r0 = r14;
                                            r0 = (org.telegram.tgnet.TLRPC.TL_updateNewMessage) r0;
                                            r3 = r0.message;
                                            r4 = org.telegram.messenger.SendMessagesHelper.this;
                                            r4 = r4.currentAccount;
                                            r4 = org.telegram.messenger.MessagesController.getInstance(r4);
                                            r5 = r0.pts;
                                            r8 = r0.pts_count;
                                            r4.processNewDifferenceParams(r2, r5, r2, r8);
                                        L_0x00c2:
                                            r8 = r3;
                                            goto L_0x00ea;
                                        L_0x00c4:
                                            r0 = r14;
                                            r0 = (org.telegram.tgnet.TLRPC.TL_updateNewChannelMessage) r0;
                                            r3 = r0.message;
                                            r4 = org.telegram.messenger.SendMessagesHelper.this;
                                            r4 = r4.currentAccount;
                                            r4 = org.telegram.messenger.MessagesController.getInstance(r4);
                                            r5 = r0.pts;
                                            r8 = r0.pts_count;
                                            r2 = r3.to_id;
                                            r2 = r2.channel_id;
                                            r4.processNewChannelDifferenceParams(r5, r8, r2);
                                            r2 = r4;
                                            if (r2 == 0) goto L_0x00c2;
                                        L_0x00e2:
                                            r2 = r3.flags;
                                            r4 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
                                            r2 = r2 | r4;
                                            r3.flags = r2;
                                            goto L_0x00c2;
                                        L_0x00ea:
                                            org.telegram.messenger.ImageLoader.saveMessageThumbs(r8);
                                            r0 = r13.intValue();
                                            r2 = r8.id;
                                            if (r0 >= r2) goto L_0x00f7;
                                        L_0x00f5:
                                            r0 = r12;
                                            goto L_0x00f8;
                                        L_0x00f7:
                                            r0 = 0;
                                        L_0x00f8:
                                            r8.unread = r0;
                                            r0 = r5;
                                            if (r0 == 0) goto L_0x0105;
                                        L_0x00fe:
                                            r8.out = r12;
                                            r0 = 0;
                                            r8.unread = r0;
                                            r8.media_unread = r0;
                                        L_0x0105:
                                            r0 = r8.id;
                                            r4 = r9.get(r0);
                                            r2 = 0;
                                            r0 = (r4 > r2 ? 1 : (r4 == r2 ? 0 : -1));
                                            if (r0 == 0) goto L_0x0180;
                                        L_0x0111:
                                            r0 = r6;
                                            r0 = r0.get(r4);
                                            r3 = r0;
                                            r3 = (org.telegram.tgnet.TLRPC.Message) r3;
                                            if (r3 != 0) goto L_0x011e;
                                        L_0x011c:
                                            goto L_0x009b;
                                        L_0x011e:
                                            r0 = r7;
                                            r2 = r0.indexOf(r3);
                                            r0 = -1;
                                            if (r2 != r0) goto L_0x0129;
                                        L_0x0127:
                                            goto L_0x009b;
                                        L_0x0129:
                                            r0 = r8;
                                            r0 = r0.get(r2);
                                            r0 = (org.telegram.messenger.MessageObject) r0;
                                            r12 = r7;
                                            r12.remove(r2);
                                            r12 = r8;
                                            r12.remove(r2);
                                            r12 = r3.id;
                                            r17 = r2;
                                            r2 = new java.util.ArrayList;
                                            r2.<init>();
                                            r2.add(r8);
                                            r18 = r2;
                                            r2 = r8.id;
                                            r3.id = r2;
                                            r16 = r1 + 1;
                                            r1 = org.telegram.messenger.SendMessagesHelper.this;
                                            r2 = 0;
                                            r19 = r4;
                                            r4 = 1;
                                            r1.updateMediaPaths(r0, r8, r2, r4);
                                            r1 = org.telegram.messenger.SendMessagesHelper.this;
                                            r1 = r1.currentAccount;
                                            r1 = org.telegram.messenger.MessagesStorage.getInstance(r1);
                                            r5 = r1.getStorageQueue();
                                            r4 = new org.telegram.messenger.SendMessagesHelper$5$1;
                                            r21 = r0;
                                            r0 = r4;
                                            r1 = r6;
                                            r2 = r3;
                                            r22 = r3;
                                            r3 = r12;
                                            r23 = r9;
                                            r9 = r4;
                                            r4 = r18;
                                            r10 = r5;
                                            r5 = r8;
                                            r0.<init>(r2, r3, r4, r5);
                                            r10.postRunnable(r9);
                                            r1 = r16;
                                            goto L_0x0182;
                                        L_0x0180:
                                            r23 = r9;
                                        L_0x0182:
                                            r0 = 1;
                                            r2 = r15 + 1;
                                            r12 = r0;
                                            r0 = r2;
                                            r9 = r23;
                                            goto L_0x0080;
                                        L_0x018b:
                                            r23 = r9;
                                            r0 = r11.updates;
                                            r0 = r0.isEmpty();
                                            if (r0 != 0) goto L_0x01a4;
                                        L_0x0195:
                                            r0 = org.telegram.messenger.SendMessagesHelper.this;
                                            r0 = r0.currentAccount;
                                            r0 = org.telegram.messenger.MessagesController.getInstance(r0);
                                            r2 = 0;
                                            r0.processUpdates(r11, r2);
                                            goto L_0x01a5;
                                        L_0x01a4:
                                            r2 = 0;
                                        L_0x01a5:
                                            r0 = org.telegram.messenger.SendMessagesHelper.this;
                                            r0 = r0.currentAccount;
                                            r0 = org.telegram.messenger.StatsController.getInstance(r0);
                                            r3 = org.telegram.tgnet.ConnectionsManager.getCurrentNetworkType();
                                            r4 = 1;
                                            r0.incrementSentItemsCount(r3, r4, r1);
                                            goto L_0x01c1;
                                        L_0x01b8:
                                            r2 = 0;
                                            r0 = new org.telegram.messenger.SendMessagesHelper$5$2;
                                            r0.<init>(r7);
                                            org.telegram.messenger.AndroidUtilities.runOnUIThread(r0);
                                            r0 = r2;
                                            r1 = r7;
                                            r1 = r1.size();
                                            if (r0 >= r1) goto L_0x01eb;
                                            r1 = r7;
                                            r1 = r1.get(r0);
                                            r1 = (org.telegram.tgnet.TLRPC.Message) r1;
                                            r2 = org.telegram.messenger.SendMessagesHelper.this;
                                            r2 = r2.currentAccount;
                                            r2 = org.telegram.messenger.MessagesStorage.getInstance(r2);
                                            r2.markMessageAsSendError(r1);
                                            r2 = new org.telegram.messenger.SendMessagesHelper$5$3;
                                            r2.<init>(r1);
                                            org.telegram.messenger.AndroidUtilities.runOnUIThread(r2);
                                            r2 = r0 + 1;
                                            goto L_0x01c2;
                                            return;
                                            */
                                            throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.SendMessagesHelper.5.run(org.telegram.tgnet.TLObject, org.telegram.tgnet.TLRPC$TL_error):void");
                                        }
                                    };
                                    instance.sendRequest(req2, requestDelegate, 68);
                                    if (myId != messages.size() - 1) {
                                        objArr2 = new ArrayList();
                                        arr2 = new ArrayList();
                                        sendResult = new ArrayList();
                                        groupsMap = new ArrayList();
                                        messagesByRandomIds = new LongSparseArray();
                                        randomIds = sendResult;
                                        ids3 = groupsMap;
                                        sendResult = myId + 1;
                                        inputPeer2 = inputPeer3;
                                        groupsMap = groupsMap2;
                                        to_id = to_id2;
                                        myId = myId2;
                                        lower_id = lower_id3;
                                        sendMessagesHelper2 = this;
                                        j2 = peer;
                                    }
                                } else if (sendResult2 == 0) {
                                    sendResult2 = 1;
                                    groupsMap2 = groupsMap;
                                    inputPeer3 = inputPeer2;
                                    to_id2 = to_id;
                                    myId2 = myId;
                                    lower_id3 = lower_id2;
                                }
                                myId = sendResult;
                                sendResult = myId + 1;
                                inputPeer2 = inputPeer3;
                                groupsMap = groupsMap2;
                                to_id = to_id2;
                                myId = myId2;
                                lower_id = lower_id3;
                                sendMessagesHelper2 = this;
                                j2 = peer;
                            }
                            arr3 = arr2;
                            groupsMap2 = groupsMap;
                            objArr3 = objArr2;
                            randomIds2 = randomIds;
                            ids4 = ids3;
                            inputPeer3 = inputPeer2;
                            messagesByRandomIds2 = messagesByRandomIds;
                            to_id2 = to_id;
                            myId2 = myId;
                            lower_id3 = lower_id2;
                            myId = sendResult;
                        } else {
                            arr3 = arr2;
                            groupsMap2 = groupsMap;
                            objArr3 = objArr2;
                            randomIds2 = randomIds;
                            ids4 = ids3;
                            inputPeer3 = inputPeer2;
                            messagesByRandomIds2 = messagesByRandomIds;
                            to_id2 = to_id;
                            myId2 = myId;
                            lower_id3 = lower_id2;
                            myId = sendResult;
                        }
                        messagesByRandomIds = messagesByRandomIds2;
                        ids3 = ids4;
                        randomIds = randomIds2;
                        arr2 = arr3;
                        objArr2 = objArr3;
                        sendResult = myId + 1;
                        inputPeer2 = inputPeer3;
                        groupsMap = groupsMap2;
                        to_id = to_id2;
                        myId = myId2;
                        lower_id = lower_id3;
                        sendMessagesHelper2 = this;
                        j2 = peer;
                    }
                    sendResult = sendResult2;
                }
                sendMessagesHelper = this;
                j = peer;
                return sendResult;
            }
        }
        sendMessagesHelper = sendMessagesHelper2;
        j = j2;
        return 0;
    }

    public int editMessage(MessageObject messageObject, String message, boolean searchLinks, final BaseFragment fragment, ArrayList<MessageEntity> entities, final Runnable callback) {
        if (!(fragment == null || fragment.getParentActivity() == null)) {
            if (callback != null) {
                final TL_messages_editMessage req = new TL_messages_editMessage();
                req.peer = MessagesController.getInstance(this.currentAccount).getInputPeer((int) messageObject.getDialogId());
                req.message = message;
                req.flags |= 2048;
                req.id = messageObject.getId();
                req.no_webpage = searchLinks ^ 1;
                if (entities != null) {
                    req.entities = entities;
                    req.flags |= 8;
                }
                return ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                    public void run(TLObject response, final TL_error error) {
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            public void run() {
                                callback.run();
                            }
                        });
                        if (error == null) {
                            MessagesController.getInstance(SendMessagesHelper.this.currentAccount).processUpdates((Updates) response, false);
                        } else {
                            AndroidUtilities.runOnUIThread(new Runnable() {
                                public void run() {
                                    AlertsCreator.processError(SendMessagesHelper.this.currentAccount, error, fragment, req, new Object[0]);
                                }
                            });
                        }
                    }
                });
            }
        }
        return 0;
    }

    private void sendLocation(Location location) {
        MessageMedia mediaGeo = new TL_messageMediaGeo();
        mediaGeo.geo = new TL_geoPoint();
        mediaGeo.geo.lat = location.getLatitude();
        mediaGeo.geo._long = location.getLongitude();
        for (Entry<String, MessageObject> entry : this.waitingForLocation.entrySet()) {
            MessageObject messageObject = (MessageObject) entry.getValue();
            sendMessage(mediaGeo, messageObject.getDialogId(), messageObject, null, null);
        }
    }

    public void sendCurrentLocation(MessageObject messageObject, KeyboardButton button) {
        if (messageObject != null) {
            if (button != null) {
                String key = new StringBuilder();
                key.append(messageObject.getDialogId());
                key.append("_");
                key.append(messageObject.getId());
                key.append("_");
                key.append(Utilities.bytesToHex(button.data));
                key.append("_");
                key.append(button instanceof TL_keyboardButtonGame ? "1" : "0");
                this.waitingForLocation.put(key.toString(), messageObject);
                this.locationProvider.start();
            }
        }
    }

    public boolean isSendingCurrentLocation(MessageObject messageObject, KeyboardButton button) {
        if (messageObject != null) {
            if (button != null) {
                String key = new StringBuilder();
                key.append(messageObject.getDialogId());
                key.append("_");
                key.append(messageObject.getId());
                key.append("_");
                key.append(Utilities.bytesToHex(button.data));
                key.append("_");
                key.append(button instanceof TL_keyboardButtonGame ? "1" : "0");
                return this.waitingForLocation.containsKey(key.toString());
            }
        }
        return false;
    }

    public void sendNotificationCallback(long dialogId, int msgId, byte[] data) {
        final long j = dialogId;
        final int i = msgId;
        final byte[] bArr = data;
        AndroidUtilities.runOnUIThread(new Runnable() {
            public void run() {
                int lowerId = (int) j;
                String key = new StringBuilder();
                key.append(j);
                key.append("_");
                key.append(i);
                key.append("_");
                key.append(Utilities.bytesToHex(bArr));
                key.append("_");
                key.append(0);
                key = key.toString();
                SendMessagesHelper.this.waitingForCallback.put(key, Boolean.valueOf(true));
                if (lowerId > 0) {
                    if (MessagesController.getInstance(SendMessagesHelper.this.currentAccount).getUser(Integer.valueOf(lowerId)) == null) {
                        User user = MessagesStorage.getInstance(SendMessagesHelper.this.currentAccount).getUserSync(lowerId);
                        if (user != null) {
                            MessagesController.getInstance(SendMessagesHelper.this.currentAccount).putUser(user, true);
                        }
                    }
                } else if (MessagesController.getInstance(SendMessagesHelper.this.currentAccount).getChat(Integer.valueOf(-lowerId)) == null) {
                    Chat chat = MessagesStorage.getInstance(SendMessagesHelper.this.currentAccount).getChatSync(-lowerId);
                    if (chat != null) {
                        MessagesController.getInstance(SendMessagesHelper.this.currentAccount).putChat(chat, true);
                    }
                }
                TL_messages_getBotCallbackAnswer req = new TL_messages_getBotCallbackAnswer();
                req.peer = MessagesController.getInstance(SendMessagesHelper.this.currentAccount).getInputPeer(lowerId);
                req.msg_id = i;
                req.game = false;
                if (bArr != null) {
                    req.flags |= 1;
                    req.data = bArr;
                }
                ConnectionsManager.getInstance(SendMessagesHelper.this.currentAccount).sendRequest(req, new RequestDelegate() {
                    public void run(TLObject response, TL_error error) {
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            public void run() {
                                SendMessagesHelper.this.waitingForCallback.remove(key);
                            }
                        });
                    }
                }, 2);
                MessagesController.getInstance(SendMessagesHelper.this.currentAccount).markDialogAsRead(j, i, i, 0, false, 0, true);
            }
        });
    }

    public void sendCallback(boolean cache, MessageObject messageObject, KeyboardButton button, ChatActivity parentFragment) {
        SendMessagesHelper sendMessagesHelper = this;
        MessageObject messageObject2 = messageObject;
        KeyboardButton keyboardButton = button;
        if (!(messageObject2 == null || keyboardButton == null)) {
            if (parentFragment != null) {
                boolean cacheFinal;
                int type;
                if (keyboardButton instanceof TL_keyboardButtonGame) {
                    cacheFinal = false;
                    type = 1;
                } else {
                    cacheFinal = cache;
                    if (keyboardButton instanceof TL_keyboardButtonBuy) {
                        type = 2;
                    } else {
                        type = 0;
                    }
                }
                boolean cacheFinal2 = cacheFinal;
                int type2 = type;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(messageObject.getDialogId());
                stringBuilder.append("_");
                stringBuilder.append(messageObject.getId());
                stringBuilder.append("_");
                stringBuilder.append(Utilities.bytesToHex(keyboardButton.data));
                stringBuilder.append("_");
                stringBuilder.append(type2);
                String key = stringBuilder.toString();
                sendMessagesHelper.waitingForCallback.put(key, Boolean.valueOf(true));
                final String str = key;
                final boolean z = cacheFinal2;
                final MessageObject messageObject3 = messageObject2;
                final KeyboardButton keyboardButton2 = keyboardButton;
                final ChatActivity chatActivity = parentFragment;
                AnonymousClass8 requestDelegate = new RequestDelegate() {
                    public void run(final TLObject response, TL_error error) {
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            public void run() {
                                SendMessagesHelper.this.waitingForCallback.remove(str);
                                if (z && response == null) {
                                    SendMessagesHelper.this.sendCallback(false, messageObject3, keyboardButton2, chatActivity);
                                } else if (response != null) {
                                    if (!(keyboardButton2 instanceof TL_keyboardButtonBuy)) {
                                        TL_messages_botCallbackAnswer res = response;
                                        if (!(z || res.cache_time == 0)) {
                                            MessagesStorage.getInstance(SendMessagesHelper.this.currentAccount).saveBotCache(str, res);
                                        }
                                        TL_game game = null;
                                        if (res.message != null) {
                                            if (!res.alert) {
                                                int uid = messageObject3.messageOwner.from_id;
                                                if (messageObject3.messageOwner.via_bot_id != 0) {
                                                    uid = messageObject3.messageOwner.via_bot_id;
                                                }
                                                String name = null;
                                                if (uid > 0) {
                                                    User user = MessagesController.getInstance(SendMessagesHelper.this.currentAccount).getUser(Integer.valueOf(uid));
                                                    if (user != null) {
                                                        name = ContactsController.formatName(user.first_name, user.last_name);
                                                    }
                                                } else {
                                                    Chat chat = MessagesController.getInstance(SendMessagesHelper.this.currentAccount).getChat(Integer.valueOf(-uid));
                                                    if (chat != null) {
                                                        name = chat.title;
                                                    }
                                                }
                                                if (name == null) {
                                                    name = "bot";
                                                }
                                                chatActivity.showAlert(name, res.message);
                                            } else if (chatActivity.getParentActivity() != null) {
                                                Builder builder = new Builder(chatActivity.getParentActivity());
                                                builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
                                                builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
                                                builder.setMessage(res.message);
                                                chatActivity.showDialog(builder.create());
                                            }
                                        } else if (res.url != null && chatActivity.getParentActivity() != null) {
                                            int uid2 = messageObject3.messageOwner.from_id;
                                            if (messageObject3.messageOwner.via_bot_id != 0) {
                                                uid2 = messageObject3.messageOwner.via_bot_id;
                                            }
                                            User user2 = MessagesController.getInstance(SendMessagesHelper.this.currentAccount).getUser(Integer.valueOf(uid2));
                                            boolean z = user2 != null && user2.verified;
                                            boolean verified = z;
                                            if (keyboardButton2 instanceof TL_keyboardButtonGame) {
                                                if (messageObject3.messageOwner.media instanceof TL_messageMediaGame) {
                                                    game = messageObject3.messageOwner.media.game;
                                                }
                                                if (game != null) {
                                                    boolean z2;
                                                    ChatActivity chatActivity = chatActivity;
                                                    MessageObject messageObject = messageObject3;
                                                    String str = res.url;
                                                    if (!verified) {
                                                        SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(SendMessagesHelper.this.currentAccount);
                                                        StringBuilder stringBuilder = new StringBuilder();
                                                        stringBuilder.append("askgame_");
                                                        stringBuilder.append(uid2);
                                                        if (notificationsSettings.getBoolean(stringBuilder.toString(), true)) {
                                                            z2 = true;
                                                            chatActivity.showOpenGameAlert(game, messageObject, str, z2, uid2);
                                                        }
                                                    }
                                                    z2 = false;
                                                    chatActivity.showOpenGameAlert(game, messageObject, str, z2, uid2);
                                                } else {
                                                    return;
                                                }
                                            }
                                            chatActivity.showOpenUrlAlert(res.url, false);
                                        }
                                    } else if (response instanceof TL_payments_paymentForm) {
                                        TL_payments_paymentForm form = response;
                                        MessagesController.getInstance(SendMessagesHelper.this.currentAccount).putUsers(form.users, false);
                                        chatActivity.presentFragment(new PaymentFormActivity(form, messageObject3));
                                    } else if (response instanceof TL_payments_paymentReceipt) {
                                        chatActivity.presentFragment(new PaymentFormActivity(messageObject3, (TL_payments_paymentReceipt) response));
                                    }
                                }
                            }
                        });
                    }
                };
                if (cacheFinal2) {
                    MessagesStorage.getInstance(sendMessagesHelper.currentAccount).getBotCache(key, requestDelegate);
                } else if (!(keyboardButton instanceof TL_keyboardButtonBuy)) {
                    TL_messages_getBotCallbackAnswer req = new TL_messages_getBotCallbackAnswer();
                    req.peer = MessagesController.getInstance(sendMessagesHelper.currentAccount).getInputPeer((int) messageObject.getDialogId());
                    req.msg_id = messageObject.getId();
                    req.game = keyboardButton instanceof TL_keyboardButtonGame;
                    if (keyboardButton.data != null) {
                        req.flags |= 1;
                        req.data = keyboardButton.data;
                    }
                    ConnectionsManager.getInstance(sendMessagesHelper.currentAccount).sendRequest(req, requestDelegate, 2);
                } else if ((messageObject2.messageOwner.media.flags & 4) == 0) {
                    TL_payments_getPaymentForm req2 = new TL_payments_getPaymentForm();
                    req2.msg_id = messageObject.getId();
                    ConnectionsManager.getInstance(sendMessagesHelper.currentAccount).sendRequest(req2, requestDelegate, 2);
                } else {
                    TL_payments_getPaymentReceipt req3 = new TL_payments_getPaymentReceipt();
                    req3.msg_id = messageObject2.messageOwner.media.receipt_msg_id;
                    ConnectionsManager.getInstance(sendMessagesHelper.currentAccount).sendRequest(req3, requestDelegate, 2);
                }
            }
        }
    }

    public void sendGame(InputPeer peer, TL_inputMediaGame game, long random_id, long taskId) {
        if (peer != null) {
            if (game != null) {
                long data;
                TL_messages_sendMedia request = new TL_messages_sendMedia();
                request.peer = peer;
                if (request.peer instanceof TL_inputPeerChannel) {
                    SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(this.currentAccount);
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("silent_");
                    stringBuilder.append(peer.channel_id);
                    request.silent = notificationsSettings.getBoolean(stringBuilder.toString(), false);
                }
                request.random_id = random_id != 0 ? random_id : getNextRandomId();
                request.message = TtmlNode.ANONYMOUS_REGION_ID;
                request.media = game;
                if (taskId == 0) {
                    data = null;
                    try {
                        data = new NativeByteBuffer(((peer.getObjectSize() + game.getObjectSize()) + 4) + 8);
                        data.writeInt32(3);
                        data.writeInt64(random_id);
                        peer.serializeToStream(data);
                        game.serializeToStream(data);
                    } catch (Throwable e) {
                        FileLog.e(e);
                    }
                    data = MessagesStorage.getInstance(this.currentAccount).createPendingTask(data);
                } else {
                    data = taskId;
                }
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(request, new RequestDelegate() {
                    public void run(TLObject response, TL_error error) {
                        if (error == null) {
                            MessagesController.getInstance(SendMessagesHelper.this.currentAccount).processUpdates((Updates) response, false);
                        }
                        if (data != 0) {
                            MessagesStorage.getInstance(SendMessagesHelper.this.currentAccount).removePendingTask(data);
                        }
                    }
                });
            }
        }
    }

    public void sendMessage(MessageObject retryMessageObject) {
        MessageObject messageObject = retryMessageObject;
        long dialogId = retryMessageObject.getDialogId();
        String str = messageObject.messageOwner.attachPath;
        ReplyMarkup replyMarkup = messageObject.messageOwner.reply_markup;
        ReplyMarkup replyMarkup2 = replyMarkup;
        sendMessage(null, null, null, null, null, null, null, null, dialogId, str, null, null, true, messageObject, null, replyMarkup2, messageObject.messageOwner.params, 0);
    }

    public void sendMessage(User user, long peer, MessageObject reply_to_msg, ReplyMarkup replyMarkup, HashMap<String, String> params) {
        sendMessage(null, null, null, null, null, user, null, null, peer, null, reply_to_msg, null, true, null, null, replyMarkup, params, 0);
    }

    public void sendMessage(TL_document document, VideoEditedInfo videoEditedInfo, String path, long peer, MessageObject reply_to_msg, String caption, ArrayList<MessageEntity> entities, ReplyMarkup replyMarkup, HashMap<String, String> params, int ttl) {
        sendMessage(null, caption, null, null, videoEditedInfo, null, document, null, peer, path, reply_to_msg, null, true, null, entities, replyMarkup, params, ttl);
    }

    public void sendMessage(String message, long peer, MessageObject reply_to_msg, WebPage webPage, boolean searchLinks, ArrayList<MessageEntity> entities, ReplyMarkup replyMarkup, HashMap<String, String> params) {
        sendMessage(message, null, null, null, null, null, null, null, peer, null, reply_to_msg, webPage, searchLinks, null, entities, replyMarkup, params, 0);
    }

    public void sendMessage(MessageMedia location, long peer, MessageObject reply_to_msg, ReplyMarkup replyMarkup, HashMap<String, String> params) {
        sendMessage(null, null, location, null, null, null, null, null, peer, null, reply_to_msg, null, true, null, null, replyMarkup, params, 0);
    }

    public void sendMessage(TL_game game, long peer, ReplyMarkup replyMarkup, HashMap<String, String> params) {
        sendMessage(null, null, null, null, null, null, null, game, peer, null, null, null, true, null, null, replyMarkup, params, 0);
    }

    public void sendMessage(TL_photo photo, String path, long peer, MessageObject reply_to_msg, String caption, ArrayList<MessageEntity> entities, ReplyMarkup replyMarkup, HashMap<String, String> params, int ttl) {
        sendMessage(null, caption, null, photo, null, null, null, null, peer, path, reply_to_msg, null, true, null, entities, replyMarkup, params, ttl);
    }

    private void performSendDelayedMessage(DelayedMessage message) {
        performSendDelayedMessage(message, -1);
    }

    private void performSendDelayedMessage(DelayedMessage message, int index) {
        SendMessagesHelper sendMessagesHelper = this;
        DelayedMessage delayedMessage = message;
        boolean z = false;
        boolean z2 = true;
        String location;
        if (delayedMessage.type == 0) {
            if (delayedMessage.httpLocation != null) {
                putToDelayedMessages(delayedMessage.httpLocation, delayedMessage);
                ImageLoader.getInstance().loadHttpFile(delayedMessage.httpLocation, "file", sendMessagesHelper.currentAccount);
            } else if (delayedMessage.sendRequest != null) {
                location = FileLoader.getPathToAttach(delayedMessage.location).toString();
                putToDelayedMessages(location, delayedMessage);
                FileLoader.getInstance(sendMessagesHelper.currentAccount).uploadFile(location, false, true, 16777216);
            } else {
                location = FileLoader.getPathToAttach(delayedMessage.location).toString();
                if (!(delayedMessage.sendEncryptedRequest == null || delayedMessage.location.dc_id == 0)) {
                    File file = new File(location);
                    if (!file.exists()) {
                        location = FileLoader.getPathToAttach(delayedMessage.location, true).toString();
                        file = new File(location);
                    }
                    if (!file.exists()) {
                        putToDelayedMessages(FileLoader.getAttachFileName(delayedMessage.location), delayedMessage);
                        FileLoader.getInstance(sendMessagesHelper.currentAccount).loadFile(delayedMessage.location, "jpg", 0, 0);
                        return;
                    }
                }
                putToDelayedMessages(location, delayedMessage);
                FileLoader.getInstance(sendMessagesHelper.currentAccount).uploadFile(location, true, true, 16777216);
            }
        } else if (delayedMessage.type == 1) {
            if (delayedMessage.videoEditedInfo == null || !delayedMessage.videoEditedInfo.needConvert()) {
                if (delayedMessage.videoEditedInfo != null) {
                    if (delayedMessage.videoEditedInfo.file != null) {
                        if (delayedMessage.sendRequest instanceof TL_messages_sendMedia) {
                            media = ((TL_messages_sendMedia) delayedMessage.sendRequest).media;
                        } else {
                            media = ((TL_messages_sendBroadcast) delayedMessage.sendRequest).media;
                        }
                        media.file = delayedMessage.videoEditedInfo.file;
                        delayedMessage.videoEditedInfo.file = null;
                    } else if (delayedMessage.videoEditedInfo.encryptedFile != null) {
                        DecryptedMessage decryptedMessage = delayedMessage.sendEncryptedRequest;
                        decryptedMessage.media.size = (int) delayedMessage.videoEditedInfo.estimatedSize;
                        decryptedMessage.media.key = delayedMessage.videoEditedInfo.key;
                        decryptedMessage.media.iv = delayedMessage.videoEditedInfo.iv;
                        SecretChatHelper.getInstance(sendMessagesHelper.currentAccount).performSendEncryptedRequest(decryptedMessage, delayedMessage.obj.messageOwner, delayedMessage.encryptedChat, delayedMessage.videoEditedInfo.encryptedFile, delayedMessage.originalPath, delayedMessage.obj);
                        delayedMessage.videoEditedInfo.encryptedFile = null;
                        return;
                    }
                }
                StringBuilder stringBuilder;
                if (delayedMessage.sendRequest != null) {
                    if (delayedMessage.sendRequest instanceof TL_messages_sendMedia) {
                        media = ((TL_messages_sendMedia) delayedMessage.sendRequest).media;
                    } else {
                        media = ((TL_messages_sendBroadcast) delayedMessage.sendRequest).media;
                    }
                    if (media.file == null) {
                        location = delayedMessage.obj.messageOwner.attachPath;
                        Document document = delayedMessage.obj.getDocument();
                        if (location == null) {
                            stringBuilder = new StringBuilder();
                            stringBuilder.append(FileLoader.getDirectory(4));
                            stringBuilder.append("/");
                            stringBuilder.append(document.id);
                            stringBuilder.append(".mp4");
                            location = stringBuilder.toString();
                        }
                        putToDelayedMessages(location, delayedMessage);
                        if (delayedMessage.obj.videoEditedInfo == null || !delayedMessage.obj.videoEditedInfo.needConvert()) {
                            FileLoader.getInstance(sendMessagesHelper.currentAccount).uploadFile(location, false, false, ConnectionsManager.FileTypeVideo);
                        } else {
                            FileLoader.getInstance(sendMessagesHelper.currentAccount).uploadFile(location, false, false, document.size, ConnectionsManager.FileTypeVideo);
                        }
                    } else {
                        location = new StringBuilder();
                        location.append(FileLoader.getDirectory(4));
                        location.append("/");
                        location.append(delayedMessage.location.volume_id);
                        location.append("_");
                        location.append(delayedMessage.location.local_id);
                        location.append(".jpg");
                        location = location.toString();
                        putToDelayedMessages(location, delayedMessage);
                        FileLoader.getInstance(sendMessagesHelper.currentAccount).uploadFile(location, false, true, 16777216);
                    }
                } else {
                    location = delayedMessage.obj.messageOwner.attachPath;
                    document = delayedMessage.obj.getDocument();
                    if (location == null) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append(FileLoader.getDirectory(4));
                        stringBuilder.append("/");
                        stringBuilder.append(document.id);
                        stringBuilder.append(".mp4");
                        location = stringBuilder.toString();
                    }
                    if (delayedMessage.sendEncryptedRequest == null || document.dc_id == 0 || new File(location).exists()) {
                        putToDelayedMessages(location, delayedMessage);
                        if (delayedMessage.obj.videoEditedInfo == null || !delayedMessage.obj.videoEditedInfo.needConvert()) {
                            FileLoader.getInstance(sendMessagesHelper.currentAccount).uploadFile(location, true, false, ConnectionsManager.FileTypeVideo);
                        } else {
                            FileLoader.getInstance(sendMessagesHelper.currentAccount).uploadFile(location, true, false, document.size, ConnectionsManager.FileTypeVideo);
                        }
                    } else {
                        putToDelayedMessages(FileLoader.getAttachFileName(document), delayedMessage);
                        FileLoader.getInstance(sendMessagesHelper.currentAccount).loadFile(document, true, 0);
                        return;
                    }
                }
            }
            location = delayedMessage.obj.messageOwner.attachPath;
            document = delayedMessage.obj.getDocument();
            if (location == null) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append(FileLoader.getDirectory(4));
                stringBuilder2.append("/");
                stringBuilder2.append(document.id);
                stringBuilder2.append(".mp4");
                location = stringBuilder2.toString();
            }
            putToDelayedMessages(location, delayedMessage);
            MediaController.getInstance().scheduleVideoConvert(delayedMessage.obj);
        } else if (delayedMessage.type == 2) {
            if (delayedMessage.httpLocation != null) {
                putToDelayedMessages(delayedMessage.httpLocation, delayedMessage);
                ImageLoader.getInstance().loadHttpFile(delayedMessage.httpLocation, "gif", sendMessagesHelper.currentAccount);
            } else if (delayedMessage.sendRequest != null) {
                if (delayedMessage.sendRequest instanceof TL_messages_sendMedia) {
                    media = ((TL_messages_sendMedia) delayedMessage.sendRequest).media;
                } else {
                    media = ((TL_messages_sendBroadcast) delayedMessage.sendRequest).media;
                }
                if (media.file == null) {
                    location = delayedMessage.obj.messageOwner.attachPath;
                    putToDelayedMessages(location, delayedMessage);
                    FileLoader instance = FileLoader.getInstance(sendMessagesHelper.currentAccount);
                    if (delayedMessage.sendRequest != null) {
                        z2 = false;
                    }
                    instance.uploadFile(location, z2, false, ConnectionsManager.FileTypeFile);
                } else if (media.thumb == null && delayedMessage.location != null) {
                    StringBuilder stringBuilder3 = new StringBuilder();
                    stringBuilder3.append(FileLoader.getDirectory(4));
                    stringBuilder3.append("/");
                    stringBuilder3.append(delayedMessage.location.volume_id);
                    stringBuilder3.append("_");
                    stringBuilder3.append(delayedMessage.location.local_id);
                    stringBuilder3.append(".jpg");
                    location = stringBuilder3.toString();
                    putToDelayedMessages(location, delayedMessage);
                    FileLoader.getInstance(sendMessagesHelper.currentAccount).uploadFile(location, false, true, 16777216);
                }
            } else {
                location = delayedMessage.obj.messageOwner.attachPath;
                document = delayedMessage.obj.getDocument();
                if (delayedMessage.sendEncryptedRequest == null || document.dc_id == 0 || new File(location).exists()) {
                    putToDelayedMessages(location, delayedMessage);
                    FileLoader.getInstance(sendMessagesHelper.currentAccount).uploadFile(location, true, false, ConnectionsManager.FileTypeFile);
                } else {
                    putToDelayedMessages(FileLoader.getAttachFileName(document), delayedMessage);
                    FileLoader.getInstance(sendMessagesHelper.currentAccount).loadFile(document, true, 0);
                    return;
                }
            }
        } else if (delayedMessage.type == 3) {
            location = delayedMessage.obj.messageOwner.attachPath;
            putToDelayedMessages(location, delayedMessage);
            FileLoader instance2 = FileLoader.getInstance(sendMessagesHelper.currentAccount);
            if (delayedMessage.sendRequest == null) {
                z = true;
            }
            instance2.uploadFile(location, z, true, ConnectionsManager.FileTypeAudio);
        } else if (delayedMessage.type == 4) {
            int i;
            boolean add = index < 0;
            if (delayedMessage.location == null && delayedMessage.httpLocation == null && !delayedMessage.upload) {
                if (index < 0) {
                    if (!delayedMessage.messageObjects.isEmpty()) {
                        putToSendingMessages(((MessageObject) delayedMessage.messageObjects.get(delayedMessage.messageObjects.size() - 1)).messageOwner);
                    }
                    i = index;
                    sendReadyToSendGroup(delayedMessage, add, true);
                }
            }
            if (index < 0) {
                i = delayedMessage.messageObjects.size() - 1;
            } else {
                i = index;
            }
            MessageObject messageObject = (MessageObject) delayedMessage.messageObjects.get(i);
            StringBuilder stringBuilder4;
            String location2;
            if (messageObject.getDocument() != null) {
                HashMap hashMap;
                if (delayedMessage.videoEditedInfo != null) {
                    location = messageObject.messageOwner.attachPath;
                    Document document2 = messageObject.getDocument();
                    if (location == null) {
                        stringBuilder4 = new StringBuilder();
                        stringBuilder4.append(FileLoader.getDirectory(4));
                        stringBuilder4.append("/");
                        stringBuilder4.append(document2.id);
                        stringBuilder4.append(".mp4");
                        location = stringBuilder4.toString();
                    }
                    putToDelayedMessages(location, delayedMessage);
                    delayedMessage.extraHashMap.put(messageObject, location);
                    hashMap = delayedMessage.extraHashMap;
                    stringBuilder4 = new StringBuilder();
                    stringBuilder4.append(location);
                    stringBuilder4.append("_i");
                    hashMap.put(stringBuilder4.toString(), messageObject);
                    if (delayedMessage.location != null) {
                        hashMap = delayedMessage.extraHashMap;
                        stringBuilder4 = new StringBuilder();
                        stringBuilder4.append(location);
                        stringBuilder4.append("_t");
                        hashMap.put(stringBuilder4.toString(), delayedMessage.location);
                    }
                    MediaController.getInstance().scheduleVideoConvert(messageObject);
                } else {
                    StringBuilder stringBuilder5;
                    Document document3 = messageObject.getDocument();
                    String documentLocation = messageObject.messageOwner.attachPath;
                    if (documentLocation == null) {
                        stringBuilder5 = new StringBuilder();
                        stringBuilder5.append(FileLoader.getDirectory(4));
                        stringBuilder5.append("/");
                        stringBuilder5.append(document3.id);
                        stringBuilder5.append(".mp4");
                        documentLocation = stringBuilder5.toString();
                    }
                    if (delayedMessage.sendRequest != null) {
                        TL_messages_sendMultiMedia request = (TL_messages_sendMultiMedia) delayedMessage.sendRequest;
                        InputMedia media = ((TL_inputSingleMedia) request.multi_media.get(i)).media;
                        TL_messages_sendMultiMedia tL_messages_sendMultiMedia;
                        if (media.file == null) {
                            putToDelayedMessages(documentLocation, delayedMessage);
                            delayedMessage.extraHashMap.put(messageObject, documentLocation);
                            delayedMessage.extraHashMap.put(documentLocation, media);
                            HashMap hashMap2 = delayedMessage.extraHashMap;
                            StringBuilder stringBuilder6 = new StringBuilder();
                            stringBuilder6.append(documentLocation);
                            stringBuilder6.append("_i");
                            hashMap2.put(stringBuilder6.toString(), messageObject);
                            if (delayedMessage.location != null) {
                                hashMap2 = delayedMessage.extraHashMap;
                                stringBuilder6 = new StringBuilder();
                                stringBuilder6.append(documentLocation);
                                stringBuilder6.append("_t");
                                hashMap2.put(stringBuilder6.toString(), delayedMessage.location);
                            }
                            if (messageObject.videoEditedInfo == null || !messageObject.videoEditedInfo.needConvert()) {
                                tL_messages_sendMultiMedia = request;
                                FileLoader.getInstance(sendMessagesHelper.currentAccount).uploadFile(documentLocation, false, false, ConnectionsManager.FileTypeVideo);
                            } else {
                                FileLoader.getInstance(sendMessagesHelper.currentAccount).uploadFile(documentLocation, false, false, document3.size, ConnectionsManager.FileTypeVideo);
                            }
                        } else {
                            InputMedia media2 = media;
                            tL_messages_sendMultiMedia = request;
                            stringBuilder5 = new StringBuilder();
                            stringBuilder5.append(FileLoader.getDirectory(4));
                            stringBuilder5.append("/");
                            stringBuilder5.append(delayedMessage.location.volume_id);
                            stringBuilder5.append("_");
                            stringBuilder5.append(delayedMessage.location.local_id);
                            stringBuilder5.append(".jpg");
                            location2 = stringBuilder5.toString();
                            putToDelayedMessages(location2, delayedMessage);
                            HashMap hashMap3 = delayedMessage.extraHashMap;
                            StringBuilder stringBuilder7 = new StringBuilder();
                            stringBuilder7.append(location2);
                            stringBuilder7.append("_o");
                            hashMap3.put(stringBuilder7.toString(), documentLocation);
                            delayedMessage.extraHashMap.put(messageObject, location2);
                            delayedMessage.extraHashMap.put(location2, media2);
                            FileLoader.getInstance(sendMessagesHelper.currentAccount).uploadFile(location2, false, true, 16777216);
                        }
                    } else {
                        TL_messages_sendEncryptedMultiMedia request2 = delayedMessage.sendEncryptedRequest;
                        putToDelayedMessages(documentLocation, delayedMessage);
                        delayedMessage.extraHashMap.put(messageObject, documentLocation);
                        delayedMessage.extraHashMap.put(documentLocation, request2.files.get(i));
                        hashMap = delayedMessage.extraHashMap;
                        stringBuilder5 = new StringBuilder();
                        stringBuilder5.append(documentLocation);
                        stringBuilder5.append("_i");
                        hashMap.put(stringBuilder5.toString(), messageObject);
                        if (delayedMessage.location != null) {
                            hashMap = delayedMessage.extraHashMap;
                            stringBuilder5 = new StringBuilder();
                            stringBuilder5.append(documentLocation);
                            stringBuilder5.append("_t");
                            hashMap.put(stringBuilder5.toString(), delayedMessage.location);
                        }
                        if (messageObject.videoEditedInfo == null || !messageObject.videoEditedInfo.needConvert()) {
                            FileLoader.getInstance(sendMessagesHelper.currentAccount).uploadFile(documentLocation, true, false, ConnectionsManager.FileTypeVideo);
                        } else {
                            FileLoader.getInstance(sendMessagesHelper.currentAccount).uploadFile(documentLocation, true, false, document3.size, ConnectionsManager.FileTypeVideo);
                        }
                    }
                }
                delayedMessage.videoEditedInfo = null;
                delayedMessage.location = null;
            } else if (delayedMessage.httpLocation != null) {
                putToDelayedMessages(delayedMessage.httpLocation, delayedMessage);
                delayedMessage.extraHashMap.put(messageObject, delayedMessage.httpLocation);
                delayedMessage.extraHashMap.put(delayedMessage.httpLocation, messageObject);
                ImageLoader.getInstance().loadHttpFile(delayedMessage.httpLocation, "file", sendMessagesHelper.currentAccount);
                delayedMessage.httpLocation = null;
            } else {
                TLObject inputMedia;
                if (delayedMessage.sendRequest != null) {
                    inputMedia = ((TL_inputSingleMedia) delayedMessage.sendRequest.multi_media.get(i)).media;
                } else {
                    inputMedia = (TLObject) delayedMessage.sendEncryptedRequest.files.get(i);
                }
                stringBuilder4 = new StringBuilder();
                stringBuilder4.append(FileLoader.getDirectory(4));
                stringBuilder4.append("/");
                stringBuilder4.append(delayedMessage.location.volume_id);
                stringBuilder4.append("_");
                stringBuilder4.append(delayedMessage.location.local_id);
                stringBuilder4.append(".jpg");
                location2 = stringBuilder4.toString();
                putToDelayedMessages(location2, delayedMessage);
                delayedMessage.extraHashMap.put(location2, inputMedia);
                delayedMessage.extraHashMap.put(messageObject, location2);
                FileLoader.getInstance(sendMessagesHelper.currentAccount).uploadFile(location2, delayedMessage.sendEncryptedRequest != null, true, 16777216);
                delayedMessage.location = null;
            }
            delayedMessage.upload = false;
            sendReadyToSendGroup(delayedMessage, add, true);
        }
    }

    private void uploadMultiMedia(final DelayedMessage message, final InputMedia inputMedia, InputEncryptedFile inputEncryptedFile, String key) {
        int a;
        if (inputMedia != null) {
            TL_messages_sendMultiMedia multiMedia = message.sendRequest;
            for (a = 0; a < multiMedia.multi_media.size(); a++) {
                if (((TL_inputSingleMedia) multiMedia.multi_media.get(a)).media == inputMedia) {
                    putToSendingMessages((Message) message.messages.get(a));
                    NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.FileUploadProgressChanged, key, Float.valueOf(1.0f), Boolean.valueOf(false));
                    break;
                }
            }
            TL_messages_uploadMedia req = new TL_messages_uploadMedia();
            req.media = inputMedia;
            req.peer = ((TL_messages_sendMultiMedia) message.sendRequest).peer;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                public void run(final TLObject response, TL_error error) {
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        public void run() {
                            InputMedia newInputMedia = null;
                            if (response != null) {
                                MessageMedia messageMedia = response;
                                InputMedia inputMediaPhoto;
                                if ((inputMedia instanceof TL_inputMediaUploadedPhoto) && (messageMedia instanceof TL_messageMediaPhoto)) {
                                    inputMediaPhoto = new TL_inputMediaPhoto();
                                    inputMediaPhoto.id = new TL_inputPhoto();
                                    inputMediaPhoto.id.id = messageMedia.photo.id;
                                    inputMediaPhoto.id.access_hash = messageMedia.photo.access_hash;
                                    newInputMedia = inputMediaPhoto;
                                } else if ((inputMedia instanceof TL_inputMediaUploadedDocument) && (messageMedia instanceof TL_messageMediaDocument)) {
                                    inputMediaPhoto = new TL_inputMediaDocument();
                                    inputMediaPhoto.id = new TL_inputDocument();
                                    inputMediaPhoto.id.id = messageMedia.document.id;
                                    inputMediaPhoto.id.access_hash = messageMedia.document.access_hash;
                                    newInputMedia = inputMediaPhoto;
                                }
                            }
                            if (newInputMedia != null) {
                                if (inputMedia.ttl_seconds != 0) {
                                    newInputMedia.ttl_seconds = inputMedia.ttl_seconds;
                                    newInputMedia.flags |= 1;
                                }
                                TL_messages_sendMultiMedia req = message.sendRequest;
                                for (int a = 0; a < req.multi_media.size(); a++) {
                                    if (((TL_inputSingleMedia) req.multi_media.get(a)).media == inputMedia) {
                                        ((TL_inputSingleMedia) req.multi_media.get(a)).media = newInputMedia;
                                        break;
                                    }
                                }
                                SendMessagesHelper.this.sendReadyToSendGroup(message, false, true);
                                return;
                            }
                            message.markAsError();
                        }
                    });
                }
            });
        } else if (inputEncryptedFile != null) {
            TL_messages_sendEncryptedMultiMedia multiMedia2 = message.sendEncryptedRequest;
            for (a = 0; a < multiMedia2.files.size(); a++) {
                if (multiMedia2.files.get(a) == inputEncryptedFile) {
                    putToSendingMessages((Message) message.messages.get(a));
                    NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.FileUploadProgressChanged, key, Float.valueOf(1.0f), Boolean.valueOf(false));
                    break;
                }
            }
            sendReadyToSendGroup(message, false, true);
        }
    }

    private void sendReadyToSendGroup(DelayedMessage message, boolean add, boolean check) {
        if (message.messageObjects.isEmpty()) {
            message.markAsError();
            return;
        }
        String key = new StringBuilder();
        key.append("group_");
        key.append(message.groupId);
        key = key.toString();
        if (message.finalGroupMessage != ((MessageObject) message.messageObjects.get(message.messageObjects.size() - 1)).getId()) {
            if (add) {
                putToDelayedMessages(key, message);
            }
            return;
        }
        int a = 0;
        if (add) {
            this.delayedMessages.remove(key);
            MessagesStorage.getInstance(this.currentAccount).putMessages(message.messages, false, true, false, 0);
            MessagesController.getInstance(this.currentAccount).updateInterfaceWithMessages(message.peer, message.messageObjects);
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
        }
        if (message.sendRequest instanceof TL_messages_sendMultiMedia) {
            TL_messages_sendMultiMedia request = message.sendRequest;
            while (a < request.multi_media.size()) {
                InputMedia inputMedia = ((TL_inputSingleMedia) request.multi_media.get(a)).media;
                if (!(inputMedia instanceof TL_inputMediaUploadedPhoto)) {
                    if (!(inputMedia instanceof TL_inputMediaUploadedDocument)) {
                        a++;
                    }
                }
                return;
            }
            if (check) {
                a = findMaxDelayedMessageForMessageId(message.finalGroupMessage, message.peer);
                if (a != 0) {
                    a.addDelayedRequest(message.sendRequest, message.messageObjects, message.originalPaths);
                    if (message.requests != null) {
                        a.requests.addAll(message.requests);
                    }
                    return;
                }
            }
        }
        TL_messages_sendEncryptedMultiMedia request2 = message.sendEncryptedRequest;
        while (a < request2.files.size()) {
            if (!(((InputEncryptedFile) request2.files.get(a)) instanceof TL_inputEncryptedFile)) {
                a++;
            } else {
                return;
            }
        }
        if (message.sendRequest instanceof TL_messages_sendMultiMedia) {
            performSendMessageRequestMulti((TL_messages_sendMultiMedia) message.sendRequest, message.messageObjects, message.originalPaths);
        } else {
            SecretChatHelper.getInstance(this.currentAccount).performSendEncryptedRequest((TL_messages_sendEncryptedMultiMedia) message.sendEncryptedRequest, message);
        }
        message.sendDelayedRequests();
    }

    protected void stopVideoService(final String path) {
        MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new Runnable() {
            public void run() {
                AndroidUtilities.runOnUIThread(new Runnable() {
                    public void run() {
                        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.stopEncodingService, path, Integer.valueOf(SendMessagesHelper.this.currentAccount));
                    }
                });
            }
        });
    }

    protected void putToSendingMessages(Message message) {
        this.sendingMessages.put(message.id, message);
    }

    protected void removeFromSendingMessages(int mid) {
        this.sendingMessages.remove(mid);
    }

    public boolean isSendingMessage(int mid) {
        return this.sendingMessages.indexOfKey(mid) >= 0;
    }

    private void performSendMessageRequestMulti(final TL_messages_sendMultiMedia req, final ArrayList<MessageObject> msgObjs, final ArrayList<String> originalPaths) {
        for (int a = 0; a < msgObjs.size(); a++) {
            putToSendingMessages(((MessageObject) msgObjs.get(a)).messageOwner);
        }
        ConnectionsManager.getInstance(this.currentAccount).sendRequest((TLObject) req, new RequestDelegate() {
            public void run(final TLObject response, final TL_error error) {
                AndroidUtilities.runOnUIThread(new Runnable() {
                    public void run() {
                        boolean isSentError = false;
                        if (error == null) {
                            LongSparseArray<Integer> longSparseArray;
                            Updates updates;
                            SparseArray<Message> newMessages = new SparseArray();
                            LongSparseArray<Integer> newIds = new LongSparseArray();
                            Updates updates2 = response;
                            ArrayList<Update> updatesArr = ((Updates) response).updates;
                            int a = 0;
                            while (a < updatesArr.size()) {
                                Update update = (Update) updatesArr.get(a);
                                if (update instanceof TL_updateMessageID) {
                                    TL_updateMessageID updateMessageID = (TL_updateMessageID) update;
                                    newIds.put(updateMessageID.random_id, Integer.valueOf(updateMessageID.id));
                                    updatesArr.remove(a);
                                    a--;
                                } else if (update instanceof TL_updateNewMessage) {
                                    final TL_updateNewMessage newMessage = (TL_updateNewMessage) update;
                                    newMessages.put(newMessage.message.id, newMessage.message);
                                    Utilities.stageQueue.postRunnable(new Runnable() {
                                        public void run() {
                                            MessagesController.getInstance(SendMessagesHelper.this.currentAccount).processNewDifferenceParams(-1, newMessage.pts, -1, newMessage.pts_count);
                                        }
                                    });
                                    updatesArr.remove(a);
                                    a--;
                                } else if (update instanceof TL_updateNewChannelMessage) {
                                    final TL_updateNewChannelMessage newMessage2 = (TL_updateNewChannelMessage) update;
                                    newMessages.put(newMessage2.message.id, newMessage2.message);
                                    Utilities.stageQueue.postRunnable(new Runnable() {
                                        public void run() {
                                            MessagesController.getInstance(SendMessagesHelper.this.currentAccount).processNewChannelDifferenceParams(newMessage2.pts, newMessage2.pts_count, newMessage2.message.to_id.channel_id);
                                        }
                                    });
                                    updatesArr.remove(a);
                                    a--;
                                }
                                a++;
                            }
                            a = 0;
                            while (a < msgObjs.size()) {
                                MessageObject msgObj = (MessageObject) msgObjs.get(a);
                                String originalPath = (String) originalPaths.get(a);
                                final Message newMsgObj = msgObj.messageOwner;
                                final int oldId = newMsgObj.id;
                                final ArrayList<Message> sentMessages = new ArrayList();
                                String attachPath = newMsgObj.attachPath;
                                Integer id = (Integer) newIds.get(newMsgObj.random_id);
                                Integer num;
                                if (id == null) {
                                    num = id;
                                    longSparseArray = newIds;
                                    updates = updates2;
                                    isSentError = true;
                                    break;
                                }
                                Message message = (Message) newMessages.get(id.intValue());
                                if (message == null) {
                                    num = id;
                                    longSparseArray = newIds;
                                    updates = updates2;
                                    isSentError = true;
                                    break;
                                }
                                sentMessages.add(message);
                                newMsgObj.id = message.id;
                                if ((newMsgObj.flags & Integer.MIN_VALUE) != 0) {
                                    message.flags |= Integer.MIN_VALUE;
                                }
                                SparseArray<Message> newMessages2 = newMessages;
                                newMessages = (Integer) MessagesController.getInstance(SendMessagesHelper.this.currentAccount).dialogs_read_outbox_max.get(Long.valueOf(message.dialog_id));
                                if (newMessages == null) {
                                    longSparseArray = newIds;
                                    updates = updates2;
                                    newMessages = Integer.valueOf(MessagesStorage.getInstance(SendMessagesHelper.this.currentAccount).getDialogReadMax(message.out, message.dialog_id));
                                    MessagesController.getInstance(SendMessagesHelper.this.currentAccount).dialogs_read_outbox_max.put(Long.valueOf(message.dialog_id), newMessages);
                                } else {
                                    longSparseArray = newIds;
                                    updates = updates2;
                                }
                                message.unread = newMessages.intValue() < message.id;
                                SendMessagesHelper.this.updateMediaPaths(msgObj, message, originalPath, false);
                                if (null == null) {
                                    StatsController.getInstance(SendMessagesHelper.this.currentAccount).incrementSentItemsCount(ConnectionsManager.getCurrentNetworkType(), 1, 1);
                                    newMsgObj.send_state = 0;
                                    NotificationCenter.getInstance(SendMessagesHelper.this.currentAccount).postNotificationName(NotificationCenter.messageReceivedByServer, Integer.valueOf(oldId), Integer.valueOf(newMsgObj.id), newMsgObj, Long.valueOf(newMsgObj.dialog_id));
                                    MessagesStorage.getInstance(SendMessagesHelper.this.currentAccount).getStorageQueue().postRunnable(new Runnable() {
                                        public void run() {
                                            MessagesStorage.getInstance(SendMessagesHelper.this.currentAccount).updateMessageStateAndId(newMsgObj.random_id, Integer.valueOf(oldId), newMsgObj.id, 0, false, newMsgObj.to_id.channel_id);
                                            MessagesStorage.getInstance(SendMessagesHelper.this.currentAccount).putMessages(sentMessages, true, false, false, 0);
                                            AndroidUtilities.runOnUIThread(new Runnable() {
                                                public void run() {
                                                    DataQuery.getInstance(SendMessagesHelper.this.currentAccount).increasePeerRaiting(newMsgObj.dialog_id);
                                                    NotificationCenter.getInstance(SendMessagesHelper.this.currentAccount).postNotificationName(NotificationCenter.messageReceivedByServer, Integer.valueOf(oldId), Integer.valueOf(newMsgObj.id), newMsgObj, Long.valueOf(newMsgObj.dialog_id));
                                                    SendMessagesHelper.this.processSentMessage(oldId);
                                                    SendMessagesHelper.this.removeFromSendingMessages(oldId);
                                                }
                                            });
                                        }
                                    });
                                }
                                a++;
                                newMessages = newMessages2;
                                newIds = longSparseArray;
                                updates2 = updates;
                            }
                            longSparseArray = newIds;
                            updates = updates2;
                            final Updates updates3 = updates;
                            Utilities.stageQueue.postRunnable(new Runnable() {
                                public void run() {
                                    MessagesController.getInstance(SendMessagesHelper.this.currentAccount).processUpdates(updates3, false);
                                }
                            });
                        } else {
                            AlertsCreator.processError(SendMessagesHelper.this.currentAccount, error, null, req, new Object[0]);
                            isSentError = true;
                        }
                        if (isSentError) {
                            for (int i = 0; i < msgObjs.size(); i++) {
                                Message newMsgObj2 = ((MessageObject) msgObjs.get(i)).messageOwner;
                                MessagesStorage.getInstance(SendMessagesHelper.this.currentAccount).markMessageAsSendError(newMsgObj2);
                                newMsgObj2.send_state = 2;
                                NotificationCenter.getInstance(SendMessagesHelper.this.currentAccount).postNotificationName(NotificationCenter.messageSendError, Integer.valueOf(newMsgObj2.id));
                                SendMessagesHelper.this.processSentMessage(newMsgObj2.id);
                                SendMessagesHelper.this.removeFromSendingMessages(newMsgObj2.id);
                            }
                        }
                    }
                });
            }
        }, null, 68);
    }

    private void performSendMessageRequest(TLObject req, MessageObject msgObj, String originalPath) {
        performSendMessageRequest(req, msgObj, originalPath, null, false);
    }

    private DelayedMessage findMaxDelayedMessageForMessageId(int messageId, long dialogId) {
        DelayedMessage maxDelayedMessage = null;
        int maxDalyedMessageId = Integer.MIN_VALUE;
        for (Entry<String, ArrayList<DelayedMessage>> entry : this.delayedMessages.entrySet()) {
            ArrayList<DelayedMessage> messages = (ArrayList) entry.getValue();
            int size = messages.size();
            for (int a = 0; a < size; a++) {
                DelayedMessage delayedMessage = (DelayedMessage) messages.get(a);
                if ((delayedMessage.type == 4 || delayedMessage.type == 0) && delayedMessage.peer == dialogId) {
                    int mid = 0;
                    if (delayedMessage.obj != null) {
                        mid = delayedMessage.obj.getId();
                    } else if (!(delayedMessage.messageObjects == null || delayedMessage.messageObjects.isEmpty())) {
                        mid = ((MessageObject) delayedMessage.messageObjects.get(delayedMessage.messageObjects.size() - 1)).getId();
                    }
                    if (mid != 0 && mid > messageId && maxDelayedMessage == null && maxDalyedMessageId < mid) {
                        maxDelayedMessage = delayedMessage;
                        maxDalyedMessageId = mid;
                    }
                }
            }
        }
        return maxDelayedMessage;
    }

    private void performSendMessageRequest(TLObject req, MessageObject msgObj, String originalPath, DelayedMessage parentMessage, boolean check) {
        if (check) {
            DelayedMessage maxDelayedMessage = findMaxDelayedMessageForMessageId(msgObj.getId(), msgObj.getDialogId());
            if (maxDelayedMessage != null) {
                maxDelayedMessage.addDelayedRequest(req, msgObj, originalPath);
                if (!(parentMessage == null || parentMessage.requests == null)) {
                    maxDelayedMessage.requests.addAll(parentMessage.requests);
                }
                return;
            }
        }
        final Message newMsgObj = msgObj.messageOwner;
        putToSendingMessages(newMsgObj);
        final Message message = newMsgObj;
        final TLObject tLObject = req;
        final MessageObject messageObject = msgObj;
        final String str = originalPath;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
            public void run(final TLObject response, final TL_error error) {
                AndroidUtilities.runOnUIThread(new Runnable() {
                    public void run() {
                        boolean isSentError;
                        boolean isSentError2 = false;
                        if (error == null) {
                            int oldId = message.id;
                            boolean isBroadcast = tLObject instanceof TL_messages_sendBroadcast;
                            ArrayList<Message> sentMessages = new ArrayList();
                            String attachPath = message.attachPath;
                            Message message;
                            int i;
                            if (response instanceof TL_updateShortSentMessage) {
                                final TL_updateShortSentMessage res = response;
                                Message message2 = message;
                                message = message;
                                i = res.id;
                                message.id = i;
                                message2.local_id = i;
                                message.date = res.date;
                                message.entities = res.entities;
                                message.out = res.out;
                                if (res.media != null) {
                                    message.media = res.media;
                                    message2 = message;
                                    message2.flags |= 512;
                                    ImageLoader.saveMessageThumbs(message);
                                }
                                if ((res.media instanceof TL_messageMediaGame) && !TextUtils.isEmpty(res.message)) {
                                    message.message = res.message;
                                }
                                if (!message.entities.isEmpty()) {
                                    message2 = message;
                                    message2.flags |= 128;
                                }
                                Utilities.stageQueue.postRunnable(new Runnable() {
                                    public void run() {
                                        MessagesController.getInstance(SendMessagesHelper.this.currentAccount).processNewDifferenceParams(-1, res.pts, res.date, res.pts_count);
                                    }
                                });
                                sentMessages.add(message);
                            } else if (response instanceof Updates) {
                                final Updates updates = response;
                                ArrayList<Update> updatesArr = ((Updates) response).updates;
                                message = null;
                                i = 0;
                                while (i < updatesArr.size()) {
                                    Update update = (Update) updatesArr.get(i);
                                    if (update instanceof TL_updateNewMessage) {
                                        final TL_updateNewMessage newMessage = (TL_updateNewMessage) update;
                                        Message message3 = newMessage.message;
                                        message = message3;
                                        sentMessages.add(message3);
                                        Utilities.stageQueue.postRunnable(new Runnable() {
                                            public void run() {
                                                MessagesController.getInstance(SendMessagesHelper.this.currentAccount).processNewDifferenceParams(-1, newMessage.pts, -1, newMessage.pts_count);
                                            }
                                        });
                                        updatesArr.remove(i);
                                        break;
                                    } else if (update instanceof TL_updateNewChannelMessage) {
                                        final TL_updateNewChannelMessage newMessage2 = (TL_updateNewChannelMessage) update;
                                        Message message4 = newMessage2.message;
                                        message = message4;
                                        sentMessages.add(message4);
                                        if ((message.flags & Integer.MIN_VALUE) != 0) {
                                            message4 = newMessage2.message;
                                            message4.flags |= Integer.MIN_VALUE;
                                        }
                                        Utilities.stageQueue.postRunnable(new Runnable() {
                                            public void run() {
                                                MessagesController.getInstance(SendMessagesHelper.this.currentAccount).processNewChannelDifferenceParams(newMessage2.pts, newMessage2.pts_count, newMessage2.message.to_id.channel_id);
                                            }
                                        });
                                        updatesArr.remove(i);
                                    } else {
                                        i++;
                                    }
                                }
                                if (message != null) {
                                    ImageLoader.saveMessageThumbs(message);
                                    Integer value = (Integer) MessagesController.getInstance(SendMessagesHelper.this.currentAccount).dialogs_read_outbox_max.get(Long.valueOf(message.dialog_id));
                                    if (value == null) {
                                        value = Integer.valueOf(MessagesStorage.getInstance(SendMessagesHelper.this.currentAccount).getDialogReadMax(message.out, message.dialog_id));
                                        MessagesController.getInstance(SendMessagesHelper.this.currentAccount).dialogs_read_outbox_max.put(Long.valueOf(message.dialog_id), value);
                                    }
                                    message.unread = value.intValue() < message.id;
                                    message.id = message.id;
                                    SendMessagesHelper.this.updateMediaPaths(messageObject, message, str, false);
                                } else {
                                    isSentError2 = true;
                                }
                                Utilities.stageQueue.postRunnable(new Runnable() {
                                    public void run() {
                                        MessagesController.getInstance(SendMessagesHelper.this.currentAccount).processUpdates(updates, false);
                                    }
                                });
                            }
                            isSentError = isSentError2;
                            if (MessageObject.isLiveLocationMessage(message)) {
                                LocationController.getInstance(SendMessagesHelper.this.currentAccount).addSharingLocation(message.dialog_id, message.id, message.media.period, message);
                            }
                            if (!isSentError) {
                                StatsController.getInstance(SendMessagesHelper.this.currentAccount).incrementSentItemsCount(ConnectionsManager.getCurrentNetworkType(), 1, 1);
                                message.send_state = 0;
                                NotificationCenter instance = NotificationCenter.getInstance(SendMessagesHelper.this.currentAccount);
                                int i2 = NotificationCenter.messageReceivedByServer;
                                Object[] objArr = new Object[4];
                                objArr[0] = Integer.valueOf(oldId);
                                objArr[1] = Integer.valueOf(isBroadcast ? oldId : message.id);
                                objArr[2] = message;
                                objArr[3] = Long.valueOf(message.dialog_id);
                                instance.postNotificationName(i2, objArr);
                                final int i3 = oldId;
                                final boolean z = isBroadcast;
                                final ArrayList<Message> arrayList = sentMessages;
                                final String str = attachPath;
                                MessagesStorage.getInstance(SendMessagesHelper.this.currentAccount).getStorageQueue().postRunnable(new Runnable() {
                                    public void run() {
                                        MessagesStorage.getInstance(SendMessagesHelper.this.currentAccount).updateMessageStateAndId(message.random_id, Integer.valueOf(i3), z ? i3 : message.id, 0, false, message.to_id.channel_id);
                                        MessagesStorage.getInstance(SendMessagesHelper.this.currentAccount).putMessages(arrayList, true, false, z, 0);
                                        if (z) {
                                            ArrayList<Message> currentMessage = new ArrayList();
                                            currentMessage.add(message);
                                            MessagesStorage.getInstance(SendMessagesHelper.this.currentAccount).putMessages((ArrayList) currentMessage, true, false, false, 0);
                                        }
                                        AndroidUtilities.runOnUIThread(new Runnable() {
                                            public void run() {
                                                if (z) {
                                                    for (int a = 0; a < arrayList.size(); a++) {
                                                        Message message = (Message) arrayList.get(a);
                                                        ArrayList<MessageObject> arr = new ArrayList();
                                                        MessageObject messageObject = new MessageObject(SendMessagesHelper.this.currentAccount, message, false);
                                                        arr.add(messageObject);
                                                        MessagesController.getInstance(SendMessagesHelper.this.currentAccount).updateInterfaceWithMessages(messageObject.getDialogId(), arr, true);
                                                    }
                                                    NotificationCenter.getInstance(SendMessagesHelper.this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
                                                }
                                                DataQuery.getInstance(SendMessagesHelper.this.currentAccount).increasePeerRaiting(message.dialog_id);
                                                NotificationCenter instance = NotificationCenter.getInstance(SendMessagesHelper.this.currentAccount);
                                                int i = NotificationCenter.messageReceivedByServer;
                                                Object[] objArr = new Object[4];
                                                objArr[0] = Integer.valueOf(i3);
                                                objArr[1] = Integer.valueOf(z ? i3 : message.id);
                                                objArr[2] = message;
                                                objArr[3] = Long.valueOf(message.dialog_id);
                                                instance.postNotificationName(i, objArr);
                                                SendMessagesHelper.this.processSentMessage(i3);
                                                SendMessagesHelper.this.removeFromSendingMessages(i3);
                                            }
                                        });
                                        if (MessageObject.isVideoMessage(message) || MessageObject.isRoundVideoMessage(message) || MessageObject.isNewGifMessage(message)) {
                                            SendMessagesHelper.this.stopVideoService(str);
                                        }
                                    }
                                });
                            }
                        } else {
                            AlertsCreator.processError(SendMessagesHelper.this.currentAccount, error, null, tLObject, new Object[0]);
                            isSentError = true;
                        }
                        if (isSentError) {
                            MessagesStorage.getInstance(SendMessagesHelper.this.currentAccount).markMessageAsSendError(message);
                            message.send_state = 2;
                            NotificationCenter.getInstance(SendMessagesHelper.this.currentAccount).postNotificationName(NotificationCenter.messageSendError, Integer.valueOf(message.id));
                            SendMessagesHelper.this.processSentMessage(message.id);
                            if (MessageObject.isVideoMessage(message) || MessageObject.isRoundVideoMessage(message) || MessageObject.isNewGifMessage(message)) {
                                SendMessagesHelper.this.stopVideoService(message.attachPath);
                            }
                            SendMessagesHelper.this.removeFromSendingMessages(message.id);
                        }
                    }
                });
            }
        }, new QuickAckDelegate() {
            public void run() {
                final int msg_id = newMsgObj.id;
                AndroidUtilities.runOnUIThread(new Runnable() {
                    public void run() {
                        newMsgObj.send_state = 0;
                        NotificationCenter.getInstance(SendMessagesHelper.this.currentAccount).postNotificationName(NotificationCenter.messageReceivedByAck, Integer.valueOf(msg_id));
                    }
                });
            }
        }, 68 | (req instanceof TL_messages_sendMessage ? 128 : 0));
        if (parentMessage != null) {
            parentMessage.sendDelayedRequests();
        }
    }

    private void updateMediaPaths(MessageObject newMsgObj, Message sentMessage, String originalPath, boolean post) {
        SendMessagesHelper sendMessagesHelper = this;
        Message message = sentMessage;
        String str = originalPath;
        boolean z = post;
        Message newMsg = newMsgObj.messageOwner;
        if (message != null) {
            long j = -2147483648L;
            int i = 4;
            int i2 = 0;
            StringBuilder stringBuilder;
            String fileName;
            String fileName2;
            File directory;
            File cacheFile;
            MessageObject messageObject;
            if ((message.media instanceof TL_messageMediaPhoto) && message.media.photo != null && (newMsg.media instanceof TL_messageMediaPhoto) && newMsg.media.photo != null) {
                if (message.media.ttl_seconds == 0) {
                    MessagesStorage.getInstance(sendMessagesHelper.currentAccount).putSentFile(str, message.media.photo, 0);
                }
                if (newMsg.media.photo.sizes.size() == 1 && (((PhotoSize) newMsg.media.photo.sizes.get(0)).location instanceof TL_fileLocationUnavailable)) {
                    newMsg.media.photo.sizes = message.media.photo.sizes;
                } else {
                    int a = 0;
                    while (a < message.media.photo.sizes.size()) {
                        MessageObject messageObject2;
                        PhotoSize size = (PhotoSize) message.media.photo.sizes.get(a);
                        if (!(size == null || size.location == null || (size instanceof TL_photoSizeEmpty))) {
                            if (size.type != null) {
                                int b = i2;
                                while (b < newMsg.media.photo.sizes.size()) {
                                    PhotoSize size2 = (PhotoSize) newMsg.media.photo.sizes.get(b);
                                    if (!(size2 == null || size2.location == null)) {
                                        if (size2.type != null) {
                                            if ((size2.location.volume_id == j && size.type.equals(size2.type)) || (size.w == size2.w && size.h == size2.h)) {
                                                stringBuilder = new StringBuilder();
                                                stringBuilder.append(size2.location.volume_id);
                                                stringBuilder.append("_");
                                                stringBuilder.append(size2.location.local_id);
                                                fileName = stringBuilder.toString();
                                                fileName2 = new StringBuilder();
                                                fileName2.append(size.location.volume_id);
                                                fileName2.append("_");
                                                fileName2.append(size.location.local_id);
                                                fileName2 = fileName2.toString();
                                                if (!fileName.equals(fileName2)) {
                                                    File cacheFile2;
                                                    directory = FileLoader.getDirectory(i);
                                                    StringBuilder stringBuilder2 = new StringBuilder();
                                                    stringBuilder2.append(fileName);
                                                    stringBuilder2.append(".jpg");
                                                    cacheFile = new File(directory, stringBuilder2.toString());
                                                    if (message.media.ttl_seconds != 0 || (message.media.photo.sizes.size() != 1 && size.w <= 90 && size.h <= 90)) {
                                                        File directory2 = FileLoader.getDirectory(4);
                                                        StringBuilder stringBuilder3 = new StringBuilder();
                                                        stringBuilder3.append(fileName2);
                                                        stringBuilder3.append(".jpg");
                                                        cacheFile2 = new File(directory2, stringBuilder3.toString());
                                                    } else {
                                                        cacheFile2 = FileLoader.getPathToAttach(size);
                                                    }
                                                    cacheFile.renameTo(cacheFile2);
                                                    ImageLoader.getInstance().replaceImageInCache(fileName, fileName2, size.location, z);
                                                    size2.location = size.location;
                                                    size2.size = size.size;
                                                }
                                            }
                                        }
                                    }
                                    b++;
                                    messageObject2 = newMsgObj;
                                    j = -2147483648L;
                                    i = 4;
                                }
                            }
                        }
                        a++;
                        messageObject2 = newMsgObj;
                        j = -2147483648L;
                        i = 4;
                        i2 = 0;
                    }
                }
                message.message = newMsg.message;
                message.attachPath = newMsg.attachPath;
                newMsg.media.photo.id = message.media.photo.id;
                newMsg.media.photo.access_hash = message.media.photo.access_hash;
                messageObject = newMsgObj;
            } else if (!(message.media instanceof TL_messageMediaDocument) || message.media.document == null || !(newMsg.media instanceof TL_messageMediaDocument) || newMsg.media.document == null) {
                messageObject = newMsgObj;
                if ((message.media instanceof TL_messageMediaContact) && (newMsg.media instanceof TL_messageMediaContact)) {
                    newMsg.media = message.media;
                } else if (message.media instanceof TL_messageMediaWebPage) {
                    newMsg.media = message.media;
                } else if (message.media instanceof TL_messageMediaGame) {
                    newMsg.media = message.media;
                    if ((newMsg.media instanceof TL_messageMediaGame) && !TextUtils.isEmpty(message.message)) {
                        newMsg.entities = message.entities;
                        newMsg.message = message.message;
                    }
                }
            } else {
                int a2;
                DocumentAttribute attribute;
                if (MessageObject.isVideoMessage(sentMessage)) {
                    if (message.media.ttl_seconds == 0) {
                        MessagesStorage.getInstance(sendMessagesHelper.currentAccount).putSentFile(str, message.media.document, 2);
                    }
                    message.attachPath = newMsg.attachPath;
                } else if (!(MessageObject.isVoiceMessage(sentMessage) || MessageObject.isRoundVideoMessage(sentMessage) || message.media.ttl_seconds != 0)) {
                    MessagesStorage.getInstance(sendMessagesHelper.currentAccount).putSentFile(str, message.media.document, 1);
                }
                PhotoSize size22 = newMsg.media.document.thumb;
                PhotoSize size3 = message.media.document.thumb;
                if (size22 != null && size22.location != null && size22.location.volume_id == -2147483648L && size3 != null && size3.location != null && !(size3 instanceof TL_photoSizeEmpty) && !(size22 instanceof TL_photoSizeEmpty)) {
                    StringBuilder stringBuilder4 = new StringBuilder();
                    stringBuilder4.append(size22.location.volume_id);
                    stringBuilder4.append("_");
                    stringBuilder4.append(size22.location.local_id);
                    fileName = stringBuilder4.toString();
                    StringBuilder stringBuilder5 = new StringBuilder();
                    stringBuilder5.append(size3.location.volume_id);
                    stringBuilder5.append("_");
                    stringBuilder5.append(size3.location.local_id);
                    fileName2 = stringBuilder5.toString();
                    if (!fileName.equals(fileName2)) {
                        directory = FileLoader.getDirectory(4);
                        StringBuilder stringBuilder6 = new StringBuilder();
                        stringBuilder6.append(fileName);
                        stringBuilder6.append(".jpg");
                        cacheFile = new File(directory, stringBuilder6.toString());
                        File directory3 = FileLoader.getDirectory(4);
                        stringBuilder = new StringBuilder();
                        stringBuilder.append(fileName2);
                        stringBuilder.append(".jpg");
                        cacheFile.renameTo(new File(directory3, stringBuilder.toString()));
                        ImageLoader.getInstance().replaceImageInCache(fileName, fileName2, size3.location, z);
                        size22.location = size3.location;
                        size22.size = size3.size;
                    }
                } else if (size22 != null && MessageObject.isStickerMessage(sentMessage) && size22.location != null) {
                    size3.location = size22.location;
                } else if ((size22 != null && (size22.location instanceof TL_fileLocationUnavailable)) || (size22 instanceof TL_photoSizeEmpty)) {
                    newMsg.media.document.thumb = message.media.document.thumb;
                }
                newMsg.media.document.dc_id = message.media.document.dc_id;
                newMsg.media.document.id = message.media.document.id;
                newMsg.media.document.access_hash = message.media.document.access_hash;
                byte[] oldWaveform = null;
                for (a2 = 0; a2 < newMsg.media.document.attributes.size(); a2++) {
                    attribute = (DocumentAttribute) newMsg.media.document.attributes.get(a2);
                    if (attribute instanceof TL_documentAttributeAudio) {
                        oldWaveform = attribute.waveform;
                        break;
                    }
                }
                newMsg.media.document.attributes = message.media.document.attributes;
                if (oldWaveform != null) {
                    for (a2 = 0; a2 < newMsg.media.document.attributes.size(); a2++) {
                        attribute = (DocumentAttribute) newMsg.media.document.attributes.get(a2);
                        if (attribute instanceof TL_documentAttributeAudio) {
                            attribute.waveform = oldWaveform;
                            attribute.flags |= 4;
                        }
                    }
                }
                newMsg.media.document.size = message.media.document.size;
                newMsg.media.document.mime_type = message.media.document.mime_type;
                if ((message.flags & 4) == 0 && MessageObject.isOut(sentMessage)) {
                    if (MessageObject.isNewGifDocument(message.media.document)) {
                        DataQuery.getInstance(sendMessagesHelper.currentAccount).addRecentGif(message.media.document, message.date);
                    } else if (MessageObject.isStickerDocument(message.media.document)) {
                        DataQuery.getInstance(sendMessagesHelper.currentAccount).addRecentSticker(0, message.media.document, message.date, false);
                    }
                }
                if (newMsg.attachPath == null || !newMsg.attachPath.startsWith(FileLoader.getDirectory(4).getAbsolutePath())) {
                    messageObject = newMsgObj;
                    message.attachPath = newMsg.attachPath;
                    message.message = newMsg.message;
                } else {
                    File cacheFile3 = new File(newMsg.attachPath);
                    cacheFile = FileLoader.getPathToAttach(message.media.document, message.media.ttl_seconds != 0);
                    if (!cacheFile3.renameTo(cacheFile)) {
                        message.attachPath = newMsg.attachPath;
                        message.message = newMsg.message;
                        messageObject = newMsgObj;
                    } else if (MessageObject.isVideoMessage(sentMessage)) {
                        newMsgObj.attachPathExists = true;
                    } else {
                        messageObject = newMsgObj;
                        messageObject.mediaExists = messageObject.attachPathExists;
                        messageObject.attachPathExists = false;
                        newMsg.attachPath = TtmlNode.ANONYMOUS_REGION_ID;
                        if (str != null && str.startsWith("http")) {
                            MessagesStorage.getInstance(sendMessagesHelper.currentAccount).addRecentLocalFile(str, cacheFile.toString(), newMsg.media.document);
                        }
                    }
                }
            }
        }
    }

    private void putToDelayedMessages(String location, DelayedMessage message) {
        ArrayList<DelayedMessage> arrayList = (ArrayList) this.delayedMessages.get(location);
        if (arrayList == null) {
            arrayList = new ArrayList();
            this.delayedMessages.put(location, arrayList);
        }
        arrayList.add(message);
    }

    protected ArrayList<DelayedMessage> getDelayedMessages(String location) {
        return (ArrayList) this.delayedMessages.get(location);
    }

    protected long getNextRandomId() {
        long val = 0;
        while (val == 0) {
            val = Utilities.random.nextLong();
        }
        return val;
    }

    public void checkUnsentMessages() {
        MessagesStorage.getInstance(this.currentAccount).getUnsentMessages(1000);
    }

    protected void processUnsentMessages(ArrayList<Message> messages, ArrayList<User> users, ArrayList<Chat> chats, ArrayList<EncryptedChat> encryptedChats) {
        final ArrayList<User> arrayList = users;
        final ArrayList<Chat> arrayList2 = chats;
        final ArrayList<EncryptedChat> arrayList3 = encryptedChats;
        final ArrayList<Message> arrayList4 = messages;
        AndroidUtilities.runOnUIThread(new Runnable() {
            public void run() {
                MessagesController.getInstance(SendMessagesHelper.this.currentAccount).putUsers(arrayList, true);
                MessagesController.getInstance(SendMessagesHelper.this.currentAccount).putChats(arrayList2, true);
                MessagesController.getInstance(SendMessagesHelper.this.currentAccount).putEncryptedChats(arrayList3, true);
                for (int a = 0; a < arrayList4.size(); a++) {
                    SendMessagesHelper.this.retrySendMessage(new MessageObject(SendMessagesHelper.this.currentAccount, (Message) arrayList4.get(a), false), true);
                }
            }
        });
    }

    public TL_photo generatePhotoSizes(String path, Uri imageUri) {
        Bitmap bitmap = ImageLoader.loadBitmap(path, imageUri, (float) AndroidUtilities.getPhotoSize(), (float) AndroidUtilities.getPhotoSize(), true);
        if (bitmap == null && AndroidUtilities.getPhotoSize() != 800) {
            bitmap = ImageLoader.loadBitmap(path, imageUri, 800.0f, 800.0f, true);
        }
        ArrayList<PhotoSize> sizes = new ArrayList();
        PhotoSize size = ImageLoader.scaleAndSaveImage(bitmap, 90.0f, 90.0f, 55, true);
        if (size != null) {
            sizes.add(size);
        }
        size = ImageLoader.scaleAndSaveImage(bitmap, (float) AndroidUtilities.getPhotoSize(), (float) AndroidUtilities.getPhotoSize(), 80, false, 101, 101);
        if (size != null) {
            sizes.add(size);
        }
        if (bitmap != null) {
            bitmap.recycle();
        }
        if (sizes.isEmpty()) {
            return null;
        }
        UserConfig.getInstance(this.currentAccount).saveConfig(false);
        TL_photo photo = new TL_photo();
        photo.date = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
        photo.sizes = sizes;
        return photo;
    }

    public static void prepareSendingDocument(String path, String originalPath, Uri uri, String mine, long dialog_id, MessageObject reply_to_msg, InputContentInfoCompat inputContent) {
        String str = path;
        String str2 = originalPath;
        Uri uri2 = uri;
        if ((str != null && str2 != null) || uri2 != null) {
            ArrayList<String> paths = new ArrayList();
            ArrayList<String> originalPaths = new ArrayList();
            ArrayList<Uri> uris = null;
            if (uri2 != null) {
                uris = new ArrayList();
                uris.add(uri2);
            }
            ArrayList<Uri> uris2 = uris;
            if (str != null) {
                paths.add(str);
                originalPaths.add(str2);
            }
            prepareSendingDocuments(paths, originalPaths, uris2, mine, dialog_id, reply_to_msg, inputContent);
        }
    }

    public static void prepareSendingAudioDocuments(ArrayList<MessageObject> messageObjects, long dialog_id, MessageObject reply_to_msg) {
        final ArrayList<MessageObject> arrayList = messageObjects;
        final long j = dialog_id;
        final int i = UserConfig.selectedAccount;
        final MessageObject messageObject = reply_to_msg;
        new Thread(new Runnable() {
            public void run() {
                int size = arrayList.size();
                for (int a = 0; a < size; a++) {
                    final MessageObject messageObject = (MessageObject) arrayList.get(a);
                    String originalPath = messageObject.messageOwner.attachPath;
                    File f = new File(originalPath);
                    int i = 1;
                    boolean isEncrypted = ((int) j) == 0;
                    if (originalPath != null) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(originalPath);
                        stringBuilder.append(MimeTypes.BASE_TYPE_AUDIO);
                        stringBuilder.append(f.length());
                        originalPath = stringBuilder.toString();
                    }
                    TL_document document = null;
                    if (!isEncrypted) {
                        MessagesStorage instance = MessagesStorage.getInstance(i);
                        if (isEncrypted) {
                            i = 4;
                        }
                        document = (TL_document) instance.getSentFile(originalPath, i);
                    }
                    if (document == null) {
                        document = (TL_document) messageObject.messageOwner.media.document;
                    }
                    if (isEncrypted) {
                        if (MessagesController.getInstance(i).getEncryptedChat(Integer.valueOf((int) (j >> 32))) == null) {
                            return;
                        }
                    }
                    final HashMap<String, String> params = new HashMap();
                    if (originalPath != null) {
                        params.put("originalPath", originalPath);
                    }
                    final TL_document documentFinal = document;
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        public void run() {
                            SendMessagesHelper.getInstance(i).sendMessage(documentFinal, null, messageObject.messageOwner.attachPath, j, messageObject, null, null, null, params, 0);
                        }
                    });
                }
            }
        }).start();
    }

    public static void prepareSendingDocuments(ArrayList<String> paths, ArrayList<String> originalPaths, ArrayList<Uri> uris, String mime, long dialog_id, MessageObject reply_to_msg, InputContentInfoCompat inputContent) {
        if (!(paths == null && originalPaths == null && uris == null) && (paths == null || originalPaths == null || paths.size() == originalPaths.size())) {
            final ArrayList<String> arrayList = paths;
            final int i = UserConfig.selectedAccount;
            final ArrayList<String> arrayList2 = originalPaths;
            final String str = mime;
            final long j = dialog_id;
            final MessageObject messageObject = reply_to_msg;
            final ArrayList<Uri> arrayList3 = uris;
            final InputContentInfoCompat inputContentInfoCompat = inputContent;
            new Thread(new Runnable() {
                public void run() {
                    boolean z = false;
                    int a = 0;
                    if (arrayList != null) {
                        boolean error = false;
                        for (int a2 = 0; a2 < arrayList.size(); a2++) {
                            if (!SendMessagesHelper.prepareSendingDocumentInternal(i, (String) arrayList.get(a2), (String) arrayList2.get(a2), null, str, j, messageObject, null, null)) {
                                error = true;
                            }
                        }
                        z = error;
                    }
                    if (arrayList3 != null) {
                        while (true) {
                            int a3 = a;
                            if (a3 >= arrayList3.size()) {
                                break;
                            }
                            if (!SendMessagesHelper.prepareSendingDocumentInternal(i, null, null, (Uri) arrayList3.get(a3), str, j, messageObject, null, null)) {
                                z = true;
                            }
                            a = a3 + 1;
                        }
                    }
                    if (inputContentInfoCompat != null) {
                        inputContentInfoCompat.releasePermission();
                    }
                    if (z) {
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            public void run() {
                                try {
                                    Toast.makeText(ApplicationLoader.applicationContext, LocaleController.getString("UnsupportedAttachment", R.string.UnsupportedAttachment), 0).show();
                                } catch (Throwable e) {
                                    FileLog.e(e);
                                }
                            }
                        });
                    }
                }
            }).start();
        }
    }

    public static void prepareSendingPhoto(String imageFilePath, Uri imageUri, long dialog_id, MessageObject reply_to_msg, CharSequence caption, ArrayList<MessageEntity> entities, ArrayList<InputDocument> stickers, InputContentInfoCompat inputContent, int ttl) {
        ArrayList<InputDocument> arrayList = stickers;
        SendingMediaInfo info = new SendingMediaInfo();
        info.path = imageFilePath;
        info.uri = imageUri;
        if (caption != null) {
            info.caption = caption.toString();
        }
        info.entities = entities;
        info.ttl = ttl;
        if (!(arrayList == null || stickers.isEmpty())) {
            info.masks = new ArrayList(arrayList);
        }
        ArrayList<SendingMediaInfo> infos = new ArrayList();
        infos.add(info);
        prepareSendingMedia(infos, dialog_id, reply_to_msg, inputContent, false, false);
    }

    public static void prepareSendingBotContextResult(BotInlineResult result, HashMap<String, String> params, long dialog_id, MessageObject reply_to_msg) {
        BotInlineResult botInlineResult = result;
        if (botInlineResult != null) {
            int currentAccount = UserConfig.selectedAccount;
            long j;
            if (botInlineResult.send_message instanceof TL_botInlineMessageMediaAuto) {
                final BotInlineResult botInlineResult2 = botInlineResult;
                final long j2 = dialog_id;
                final int i = currentAccount;
                final HashMap<String, String> hashMap = params;
                final MessageObject messageObject = reply_to_msg;
                new Thread(new Runnable() {
                    /* JADX WARNING: inconsistent code. */
                    /* Code decompiled incorrectly, please refer to instructions dump. */
                    public void run() {
                        /*
                        r18 = this;
                        r7 = r18;
                        r1 = 0;
                        r2 = 0;
                        r3 = 0;
                        r4 = 0;
                        r5 = r1;
                        r5 = r5 instanceof org.telegram.tgnet.TLRPC.TL_botInlineMediaResult;
                        r6 = 1;
                        if (r5 == 0) goto L_0x0080;
                    L_0x000d:
                        r5 = r1;
                        r5 = r5.type;
                        r8 = "game";
                        r5 = r5.equals(r8);
                        if (r5 == 0) goto L_0x0052;
                    L_0x0019:
                        r8 = r2;
                        r5 = (int) r8;
                        if (r5 != 0) goto L_0x001f;
                    L_0x001e:
                        return;
                    L_0x001f:
                        r5 = new org.telegram.tgnet.TLRPC$TL_game;
                        r5.<init>();
                        r4 = r5;
                        r5 = r1;
                        r5 = r5.title;
                        r4.title = r5;
                        r5 = r1;
                        r5 = r5.description;
                        r4.description = r5;
                        r5 = r1;
                        r5 = r5.id;
                        r4.short_name = r5;
                        r5 = r1;
                        r5 = r5.photo;
                        r4.photo = r5;
                        r5 = r1;
                        r5 = r5.document;
                        r5 = r5 instanceof org.telegram.tgnet.TLRPC.TL_document;
                        if (r5 == 0) goto L_0x0435;
                    L_0x0045:
                        r5 = r1;
                        r5 = r5.document;
                        r4.document = r5;
                        r5 = r4.flags;
                        r5 = r5 | r6;
                        r4.flags = r5;
                        goto L_0x0435;
                    L_0x0052:
                        r5 = r1;
                        r5 = r5.document;
                        if (r5 == 0) goto L_0x0069;
                    L_0x0058:
                        r5 = r1;
                        r5 = r5.document;
                        r5 = r5 instanceof org.telegram.tgnet.TLRPC.TL_document;
                        if (r5 == 0) goto L_0x0435;
                    L_0x0060:
                        r5 = r1;
                        r5 = r5.document;
                        r2 = r5;
                        r2 = (org.telegram.tgnet.TLRPC.TL_document) r2;
                        goto L_0x0435;
                    L_0x0069:
                        r5 = r1;
                        r5 = r5.photo;
                        if (r5 == 0) goto L_0x0435;
                    L_0x006f:
                        r5 = r1;
                        r5 = r5.photo;
                        r5 = r5 instanceof org.telegram.tgnet.TLRPC.TL_photo;
                        if (r5 == 0) goto L_0x0435;
                    L_0x0077:
                        r5 = r1;
                        r5 = r5.photo;
                        r3 = r5;
                        r3 = (org.telegram.tgnet.TLRPC.TL_photo) r3;
                        goto L_0x0435;
                    L_0x0080:
                        r5 = r1;
                        r5 = r5.content;
                        if (r5 == 0) goto L_0x0435;
                    L_0x0086:
                        r5 = new java.io.File;
                        r8 = 4;
                        r9 = org.telegram.messenger.FileLoader.getDirectory(r8);
                        r10 = new java.lang.StringBuilder;
                        r10.<init>();
                        r11 = r1;
                        r11 = r11.content;
                        r11 = r11.url;
                        r11 = org.telegram.messenger.Utilities.MD5(r11);
                        r10.append(r11);
                        r11 = ".";
                        r10.append(r11);
                        r11 = r1;
                        r11 = r11.content;
                        r11 = r11.url;
                        r12 = "file";
                        r11 = org.telegram.messenger.ImageLoader.getHttpUrlExtension(r11, r12);
                        r10.append(r11);
                        r10 = r10.toString();
                        r5.<init>(r9, r10);
                        r9 = r5.exists();
                        if (r9 == 0) goto L_0x00c5;
                    L_0x00c0:
                        r1 = r5.getAbsolutePath();
                        goto L_0x00cb;
                    L_0x00c5:
                        r9 = r1;
                        r9 = r9.content;
                        r1 = r9.url;
                    L_0x00cb:
                        r9 = r1;
                        r9 = r9.type;
                        r10 = r9.hashCode();
                        r13 = 2;
                        r14 = -1;
                        r15 = 0;
                        switch(r10) {
                            case -1890252483: goto L_0x0116;
                            case 102340: goto L_0x010c;
                            case 3143036: goto L_0x0102;
                            case 93166550: goto L_0x00f8;
                            case 106642994: goto L_0x00ee;
                            case 112202875: goto L_0x00e4;
                            case 112386354: goto L_0x00da;
                            default: goto L_0x00d9;
                        };
                    L_0x00d9:
                        goto L_0x0120;
                    L_0x00da:
                        r10 = "voice";
                        r9 = r9.equals(r10);
                        if (r9 == 0) goto L_0x0120;
                    L_0x00e2:
                        r9 = r6;
                        goto L_0x0121;
                    L_0x00e4:
                        r10 = "video";
                        r9 = r9.equals(r10);
                        if (r9 == 0) goto L_0x0120;
                    L_0x00ec:
                        r9 = 3;
                        goto L_0x0121;
                    L_0x00ee:
                        r10 = "photo";
                        r9 = r9.equals(r10);
                        if (r9 == 0) goto L_0x0120;
                    L_0x00f6:
                        r9 = 6;
                        goto L_0x0121;
                    L_0x00f8:
                        r10 = "audio";
                        r9 = r9.equals(r10);
                        if (r9 == 0) goto L_0x0120;
                    L_0x0100:
                        r9 = r15;
                        goto L_0x0121;
                    L_0x0102:
                        r10 = "file";
                        r9 = r9.equals(r10);
                        if (r9 == 0) goto L_0x0120;
                    L_0x010a:
                        r9 = r13;
                        goto L_0x0121;
                    L_0x010c:
                        r10 = "gif";
                        r9 = r9.equals(r10);
                        if (r9 == 0) goto L_0x0120;
                    L_0x0114:
                        r9 = 5;
                        goto L_0x0121;
                    L_0x0116:
                        r10 = "sticker";
                        r9 = r9.equals(r10);
                        if (r9 == 0) goto L_0x0120;
                    L_0x011e:
                        r9 = r8;
                        goto L_0x0121;
                    L_0x0120:
                        r9 = r14;
                    L_0x0121:
                        r10 = 0;
                        switch(r9) {
                            case 0: goto L_0x0172;
                            case 1: goto L_0x0172;
                            case 2: goto L_0x0172;
                            case 3: goto L_0x0172;
                            case 4: goto L_0x0172;
                            case 5: goto L_0x0172;
                            case 6: goto L_0x0127;
                            default: goto L_0x0125;
                        };
                    L_0x0125:
                        goto L_0x0435;
                    L_0x0127:
                        r8 = r5.exists();
                        if (r8 == 0) goto L_0x0137;
                    L_0x012d:
                        r8 = r4;
                        r8 = org.telegram.messenger.SendMessagesHelper.getInstance(r8);
                        r3 = r8.generatePhotoSizes(r1, r10);
                    L_0x0137:
                        if (r3 != 0) goto L_0x0435;
                    L_0x0139:
                        r8 = new org.telegram.tgnet.TLRPC$TL_photo;
                        r8.<init>();
                        r3 = r8;
                        r8 = r4;
                        r8 = org.telegram.tgnet.ConnectionsManager.getInstance(r8);
                        r8 = r8.getCurrentTime();
                        r3.date = r8;
                        r8 = new org.telegram.tgnet.TLRPC$TL_photoSize;
                        r8.<init>();
                        r9 = r1;
                        r9 = org.telegram.messenger.MessageObject.getInlineResultWidthAndHeight(r9);
                        r10 = r9[r15];
                        r8.w = r10;
                        r10 = r9[r6];
                        r8.h = r10;
                        r8.size = r6;
                        r6 = new org.telegram.tgnet.TLRPC$TL_fileLocationUnavailable;
                        r6.<init>();
                        r8.location = r6;
                        r6 = "x";
                        r8.type = r6;
                        r6 = r3.sizes;
                        r6.add(r8);
                        goto L_0x0435;
                    L_0x0172:
                        r9 = new org.telegram.tgnet.TLRPC$TL_document;
                        r9.<init>();
                        r2 = r9;
                        r11 = 0;
                        r2.id = r11;
                        r2.size = r15;
                        r2.dc_id = r15;
                        r9 = r1;
                        r9 = r9.content;
                        r9 = r9.mime_type;
                        r2.mime_type = r9;
                        r9 = r4;
                        r9 = org.telegram.tgnet.ConnectionsManager.getInstance(r9);
                        r9 = r9.getCurrentTime();
                        r2.date = r9;
                        r9 = new org.telegram.tgnet.TLRPC$TL_documentAttributeFilename;
                        r9.<init>();
                        r11 = r2.attributes;
                        r11.add(r9);
                        r11 = r1;
                        r11 = r11.type;
                        r12 = r11.hashCode();
                        switch(r12) {
                            case -1890252483: goto L_0x01e1;
                            case 102340: goto L_0x01d6;
                            case 3143036: goto L_0x01cb;
                            case 93166550: goto L_0x01c0;
                            case 112202875: goto L_0x01b5;
                            case 112386354: goto L_0x01aa;
                            default: goto L_0x01a9;
                        };
                    L_0x01a9:
                        goto L_0x01ec;
                    L_0x01aa:
                        r12 = "voice";
                        r11 = r11.equals(r12);
                        if (r11 == 0) goto L_0x01ec;
                    L_0x01b2:
                        r16 = r6;
                        goto L_0x01ee;
                    L_0x01b5:
                        r12 = "video";
                        r11 = r11.equals(r12);
                        if (r11 == 0) goto L_0x01ec;
                    L_0x01bd:
                        r16 = r8;
                        goto L_0x01ee;
                    L_0x01c0:
                        r12 = "audio";
                        r11 = r11.equals(r12);
                        if (r11 == 0) goto L_0x01ec;
                    L_0x01c8:
                        r16 = r13;
                        goto L_0x01ee;
                    L_0x01cb:
                        r12 = "file";
                        r11 = r11.equals(r12);
                        if (r11 == 0) goto L_0x01ec;
                    L_0x01d3:
                        r16 = 3;
                        goto L_0x01ee;
                    L_0x01d6:
                        r12 = "gif";
                        r11 = r11.equals(r12);
                        if (r11 == 0) goto L_0x01ec;
                    L_0x01de:
                        r16 = r15;
                        goto L_0x01ee;
                    L_0x01e1:
                        r12 = "sticker";
                        r11 = r11.equals(r12);
                        if (r11 == 0) goto L_0x01ec;
                    L_0x01e9:
                        r16 = 5;
                        goto L_0x01ee;
                    L_0x01ec:
                        r16 = r14;
                    L_0x01ee:
                        switch(r16) {
                            case 0: goto L_0x03a4;
                            case 1: goto L_0x037d;
                            case 2: goto L_0x033b;
                            case 3: goto L_0x0306;
                            case 4: goto L_0x0281;
                            case 5: goto L_0x01f3;
                            default: goto L_0x01f1;
                        };
                    L_0x01f1:
                        goto L_0x03f2;
                    L_0x01f3:
                        r13 = new org.telegram.tgnet.TLRPC$TL_documentAttributeSticker;
                        r13.<init>();
                        r14 = "";
                        r13.alt = r14;
                        r14 = new org.telegram.tgnet.TLRPC$TL_inputStickerSetEmpty;
                        r14.<init>();
                        r13.stickerset = r14;
                        r14 = r2.attributes;
                        r14.add(r13);
                        r14 = new org.telegram.tgnet.TLRPC$TL_documentAttributeImageSize;
                        r14.<init>();
                        r11 = r1;
                        r11 = org.telegram.messenger.MessageObject.getInlineResultWidthAndHeight(r11);
                        r10 = r11[r15];
                        r14.w = r10;
                        r10 = r11[r6];
                        r14.h = r10;
                        r10 = r2.attributes;
                        r10.add(r14);
                        r10 = "sticker.webp";
                        r9.file_name = r10;
                        r10 = r1;	 Catch:{ Throwable -> 0x027a }
                        r10 = r10.thumb;	 Catch:{ Throwable -> 0x027a }
                        if (r10 == 0) goto L_0x0278;
                    L_0x022a:
                        r10 = new java.io.File;	 Catch:{ Throwable -> 0x027a }
                        r8 = org.telegram.messenger.FileLoader.getDirectory(r8);	 Catch:{ Throwable -> 0x027a }
                        r15 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x027a }
                        r15.<init>();	 Catch:{ Throwable -> 0x027a }
                        r6 = r1;	 Catch:{ Throwable -> 0x027a }
                        r6 = r6.thumb;	 Catch:{ Throwable -> 0x027a }
                        r6 = r6.url;	 Catch:{ Throwable -> 0x027a }
                        r6 = org.telegram.messenger.Utilities.MD5(r6);	 Catch:{ Throwable -> 0x027a }
                        r15.append(r6);	 Catch:{ Throwable -> 0x027a }
                        r6 = ".";
                        r15.append(r6);	 Catch:{ Throwable -> 0x027a }
                        r6 = r1;	 Catch:{ Throwable -> 0x027a }
                        r6 = r6.thumb;	 Catch:{ Throwable -> 0x027a }
                        r6 = r6.url;	 Catch:{ Throwable -> 0x027a }
                        r12 = "webp";
                        r6 = org.telegram.messenger.ImageLoader.getHttpUrlExtension(r6, r12);	 Catch:{ Throwable -> 0x027a }
                        r15.append(r6);	 Catch:{ Throwable -> 0x027a }
                        r6 = r15.toString();	 Catch:{ Throwable -> 0x027a }
                        r10.<init>(r8, r6);	 Catch:{ Throwable -> 0x027a }
                        r6 = r10.getAbsolutePath();	 Catch:{ Throwable -> 0x027a }
                        r8 = 1;
                        r10 = 1119092736; // 0x42b40000 float:90.0 double:5.529052754E-315;
                        r12 = 0;
                        r12 = org.telegram.messenger.ImageLoader.loadBitmap(r6, r12, r10, r10, r8);	 Catch:{ Throwable -> 0x027a }
                        r8 = r12;
                        if (r8 == 0) goto L_0x0278;
                    L_0x026c:
                        r12 = 0;
                        r15 = 55;
                        r10 = org.telegram.messenger.ImageLoader.scaleAndSaveImage(r8, r10, r10, r15, r12);	 Catch:{ Throwable -> 0x027a }
                        r2.thumb = r10;	 Catch:{ Throwable -> 0x027a }
                        r8.recycle();	 Catch:{ Throwable -> 0x027a }
                    L_0x0278:
                        goto L_0x03f2;
                    L_0x027a:
                        r0 = move-exception;
                        r6 = r0;
                        org.telegram.messenger.FileLog.e(r6);
                        goto L_0x03f2;
                    L_0x0281:
                        r6 = "video.mp4";
                        r9.file_name = r6;
                        r6 = new org.telegram.tgnet.TLRPC$TL_documentAttributeVideo;
                        r6.<init>();
                        r10 = r1;
                        r10 = org.telegram.messenger.MessageObject.getInlineResultWidthAndHeight(r10);
                        r11 = 0;
                        r12 = r10[r11];
                        r6.w = r12;
                        r11 = 1;
                        r12 = r10[r11];
                        r6.h = r12;
                        r12 = r1;
                        r12 = org.telegram.messenger.MessageObject.getInlineResultDuration(r12);
                        r6.duration = r12;
                        r6.supports_streaming = r11;
                        r11 = r2.attributes;
                        r11.add(r6);
                        r11 = r1;	 Catch:{ Throwable -> 0x02ff }
                        r11 = r11.thumb;	 Catch:{ Throwable -> 0x02ff }
                        if (r11 == 0) goto L_0x02fd;
                    L_0x02af:
                        r11 = new java.io.File;	 Catch:{ Throwable -> 0x02ff }
                        r8 = org.telegram.messenger.FileLoader.getDirectory(r8);	 Catch:{ Throwable -> 0x02ff }
                        r12 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x02ff }
                        r12.<init>();	 Catch:{ Throwable -> 0x02ff }
                        r13 = r1;	 Catch:{ Throwable -> 0x02ff }
                        r13 = r13.thumb;	 Catch:{ Throwable -> 0x02ff }
                        r13 = r13.url;	 Catch:{ Throwable -> 0x02ff }
                        r13 = org.telegram.messenger.Utilities.MD5(r13);	 Catch:{ Throwable -> 0x02ff }
                        r12.append(r13);	 Catch:{ Throwable -> 0x02ff }
                        r13 = ".";
                        r12.append(r13);	 Catch:{ Throwable -> 0x02ff }
                        r13 = r1;	 Catch:{ Throwable -> 0x02ff }
                        r13 = r13.thumb;	 Catch:{ Throwable -> 0x02ff }
                        r13 = r13.url;	 Catch:{ Throwable -> 0x02ff }
                        r14 = "jpg";
                        r13 = org.telegram.messenger.ImageLoader.getHttpUrlExtension(r13, r14);	 Catch:{ Throwable -> 0x02ff }
                        r12.append(r13);	 Catch:{ Throwable -> 0x02ff }
                        r12 = r12.toString();	 Catch:{ Throwable -> 0x02ff }
                        r11.<init>(r8, r12);	 Catch:{ Throwable -> 0x02ff }
                        r8 = r11.getAbsolutePath();	 Catch:{ Throwable -> 0x02ff }
                        r11 = 1;
                        r12 = 1119092736; // 0x42b40000 float:90.0 double:5.529052754E-315;
                        r13 = 0;
                        r13 = org.telegram.messenger.ImageLoader.loadBitmap(r8, r13, r12, r12, r11);	 Catch:{ Throwable -> 0x02ff }
                        r11 = r13;
                        if (r11 == 0) goto L_0x02fd;
                    L_0x02f1:
                        r13 = 0;
                        r14 = 55;
                        r12 = org.telegram.messenger.ImageLoader.scaleAndSaveImage(r11, r12, r12, r14, r13);	 Catch:{ Throwable -> 0x02ff }
                        r2.thumb = r12;	 Catch:{ Throwable -> 0x02ff }
                        r11.recycle();	 Catch:{ Throwable -> 0x02ff }
                    L_0x02fd:
                        goto L_0x03f2;
                    L_0x02ff:
                        r0 = move-exception;
                        r8 = r0;
                        org.telegram.messenger.FileLog.e(r8);
                        goto L_0x03f2;
                    L_0x0306:
                        r6 = r1;
                        r6 = r6.content;
                        r6 = r6.mime_type;
                        r8 = 47;
                        r6 = r6.lastIndexOf(r8);
                        if (r6 == r14) goto L_0x0335;
                    L_0x0314:
                        r8 = new java.lang.StringBuilder;
                        r8.<init>();
                        r10 = "file.";
                        r8.append(r10);
                        r10 = r1;
                        r10 = r10.content;
                        r10 = r10.mime_type;
                        r11 = r6 + 1;
                        r10 = r10.substring(r11);
                        r8.append(r10);
                        r8 = r8.toString();
                        r9.file_name = r8;
                        goto L_0x03f2;
                    L_0x0335:
                        r8 = "file";
                        r9.file_name = r8;
                        goto L_0x03f2;
                    L_0x033b:
                        r6 = new org.telegram.tgnet.TLRPC$TL_documentAttributeAudio;
                        r6.<init>();
                        r8 = r1;
                        r8 = org.telegram.messenger.MessageObject.getInlineResultDuration(r8);
                        r6.duration = r8;
                        r8 = r1;
                        r8 = r8.title;
                        r6.title = r8;
                        r8 = r6.flags;
                        r10 = 1;
                        r8 = r8 | r10;
                        r6.flags = r8;
                        r8 = r1;
                        r8 = r8.description;
                        if (r8 == 0) goto L_0x0365;
                    L_0x035a:
                        r8 = r1;
                        r8 = r8.description;
                        r6.performer = r8;
                        r8 = r6.flags;
                        r8 = r8 | r13;
                        r6.flags = r8;
                    L_0x0365:
                        r8 = "audio.mp3";
                        r9.file_name = r8;
                        r8 = r2.attributes;
                        r8.add(r6);
                        r8 = new org.telegram.tgnet.TLRPC$TL_photoSizeEmpty;
                        r8.<init>();
                        r2.thumb = r8;
                        r8 = r2.thumb;
                        r10 = "s";
                        r8.type = r10;
                        goto L_0x03f2;
                    L_0x037d:
                        r6 = new org.telegram.tgnet.TLRPC$TL_documentAttributeAudio;
                        r6.<init>();
                        r8 = r1;
                        r8 = org.telegram.messenger.MessageObject.getInlineResultDuration(r8);
                        r6.duration = r8;
                        r8 = 1;
                        r6.voice = r8;
                        r8 = "audio.ogg";
                        r9.file_name = r8;
                        r8 = r2.attributes;
                        r8.add(r6);
                        r8 = new org.telegram.tgnet.TLRPC$TL_photoSizeEmpty;
                        r8.<init>();
                        r2.thumb = r8;
                        r8 = r2.thumb;
                        r10 = "s";
                        r8.type = r10;
                        goto L_0x03f2;
                    L_0x03a4:
                        r6 = "animation.gif";
                        r9.file_name = r6;
                        r6 = "mp4";
                        r6 = r1.endsWith(r6);
                        if (r6 == 0) goto L_0x03bf;
                    L_0x03b0:
                        r6 = "video/mp4";
                        r2.mime_type = r6;
                        r6 = r2.attributes;
                        r8 = new org.telegram.tgnet.TLRPC$TL_documentAttributeAnimated;
                        r8.<init>();
                        r6.add(r8);
                        goto L_0x03c3;
                    L_0x03bf:
                        r6 = "image/gif";
                        r2.mime_type = r6;
                    L_0x03c3:
                        r6 = "mp4";
                        r6 = r1.endsWith(r6);	 Catch:{ Throwable -> 0x03ec }
                        if (r6 == 0) goto L_0x03d4;
                    L_0x03cb:
                        r6 = 1;
                        r8 = android.media.ThumbnailUtils.createVideoThumbnail(r1, r6);	 Catch:{ Throwable -> 0x03ec }
                        r10 = r8;
                        r8 = 1119092736; // 0x42b40000 float:90.0 double:5.529052754E-315;
                        goto L_0x03dc;
                    L_0x03d4:
                        r6 = 1;
                        r8 = 1119092736; // 0x42b40000 float:90.0 double:5.529052754E-315;
                        r10 = 0;
                        r10 = org.telegram.messenger.ImageLoader.loadBitmap(r1, r10, r8, r8, r6);	 Catch:{ Throwable -> 0x03ec }
                    L_0x03dc:
                        r6 = r10;
                        if (r6 == 0) goto L_0x03eb;
                    L_0x03df:
                        r10 = 0;
                        r11 = 55;
                        r8 = org.telegram.messenger.ImageLoader.scaleAndSaveImage(r6, r8, r8, r11, r10);	 Catch:{ Throwable -> 0x03ec }
                        r2.thumb = r8;	 Catch:{ Throwable -> 0x03ec }
                        r6.recycle();	 Catch:{ Throwable -> 0x03ec }
                    L_0x03eb:
                        goto L_0x03f2;
                    L_0x03ec:
                        r0 = move-exception;
                        r6 = r0;
                        org.telegram.messenger.FileLog.e(r6);
                    L_0x03f2:
                        r6 = r9.file_name;
                        if (r6 != 0) goto L_0x03fa;
                    L_0x03f6:
                        r6 = "file";
                        r9.file_name = r6;
                    L_0x03fa:
                        r6 = r2.mime_type;
                        if (r6 != 0) goto L_0x0402;
                    L_0x03fe:
                        r6 = "application/octet-stream";
                        r2.mime_type = r6;
                    L_0x0402:
                        r6 = r2.thumb;
                        if (r6 != 0) goto L_0x0435;
                    L_0x0406:
                        r6 = new org.telegram.tgnet.TLRPC$TL_photoSize;
                        r6.<init>();
                        r2.thumb = r6;
                        r6 = r1;
                        r6 = org.telegram.messenger.MessageObject.getInlineResultWidthAndHeight(r6);
                        r8 = r2.thumb;
                        r10 = 0;
                        r11 = r6[r10];
                        r8.w = r11;
                        r8 = r2.thumb;
                        r11 = 1;
                        r11 = r6[r11];
                        r8.h = r11;
                        r8 = r2.thumb;
                        r8.size = r10;
                        r8 = r2.thumb;
                        r10 = new org.telegram.tgnet.TLRPC$TL_fileLocationUnavailable;
                        r10.<init>();
                        r8.location = r10;
                        r8 = r2.thumb;
                        r10 = "x";
                        r8.type = r10;
                    L_0x0435:
                        r8 = r1;
                        r9 = r2;
                        r10 = r3;
                        r11 = r4;
                        r4 = r8;
                        r3 = r9;
                        r5 = r10;
                        r6 = r11;
                        r1 = r5;
                        if (r1 == 0) goto L_0x0454;
                    L_0x0441:
                        r1 = r1;
                        r1 = r1.content;
                        if (r1 == 0) goto L_0x0454;
                    L_0x0447:
                        r1 = r5;
                        r2 = "originalPath";
                        r12 = r1;
                        r12 = r12.content;
                        r12 = r12.url;
                        r1.put(r2, r12);
                    L_0x0454:
                        r12 = new org.telegram.messenger.SendMessagesHelper$19$1;
                        r1 = r12;
                        r2 = r7;
                        r1.<init>(r3, r4, r5, r6);
                        org.telegram.messenger.AndroidUtilities.runOnUIThread(r12);
                        return;
                        */
                        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.SendMessagesHelper.19.run():void");
                    }
                }).run();
                j = dialog_id;
            } else if (botInlineResult.send_message instanceof TL_botInlineMessageText) {
                WebPage webPage = null;
                j = dialog_id;
                if (((int) j) == 0) {
                    for (int a = 0; a < botInlineResult.send_message.entities.size(); a++) {
                        MessageEntity entity = (MessageEntity) botInlineResult.send_message.entities.get(a);
                        if (entity instanceof TL_messageEntityUrl) {
                            webPage = new TL_webPagePending();
                            webPage.url = botInlineResult.send_message.message.substring(entity.offset, entity.offset + entity.length);
                            break;
                        }
                    }
                }
                getInstance(currentAccount).sendMessage(botInlineResult.send_message.message, j, reply_to_msg, webPage, botInlineResult.send_message.no_webpage ^ 1, botInlineResult.send_message.entities, botInlineResult.send_message.reply_markup, params);
            } else {
                j = dialog_id;
                MessageMedia venue;
                if (botInlineResult.send_message instanceof TL_botInlineMessageMediaVenue) {
                    venue = new TL_messageMediaVenue();
                    venue.geo = botInlineResult.send_message.geo;
                    venue.address = botInlineResult.send_message.address;
                    venue.title = botInlineResult.send_message.title;
                    venue.provider = botInlineResult.send_message.provider;
                    venue.venue_id = botInlineResult.send_message.venue_id;
                    venue.venue_type = TtmlNode.ANONYMOUS_REGION_ID;
                    getInstance(currentAccount).sendMessage(venue, j, reply_to_msg, botInlineResult.send_message.reply_markup, (HashMap) params);
                } else if (botInlineResult.send_message instanceof TL_botInlineMessageMediaGeo) {
                    if (botInlineResult.send_message.period != 0) {
                        venue = new TL_messageMediaGeoLive();
                        venue.period = botInlineResult.send_message.period;
                        venue.geo = botInlineResult.send_message.geo;
                        getInstance(currentAccount).sendMessage(venue, j, reply_to_msg, botInlineResult.send_message.reply_markup, (HashMap) params);
                    } else {
                        venue = new TL_messageMediaGeo();
                        venue.geo = botInlineResult.send_message.geo;
                        getInstance(currentAccount).sendMessage(venue, j, reply_to_msg, botInlineResult.send_message.reply_markup, (HashMap) params);
                    }
                } else if (botInlineResult.send_message instanceof TL_botInlineMessageMediaContact) {
                    User user = new TL_user();
                    user.phone = botInlineResult.send_message.phone_number;
                    user.first_name = botInlineResult.send_message.first_name;
                    user.last_name = botInlineResult.send_message.last_name;
                    getInstance(currentAccount).sendMessage(user, j, reply_to_msg, botInlineResult.send_message.reply_markup, (HashMap) params);
                }
            }
        }
    }

    private static String getTrimmedString(String src) {
        String result = src.trim();
        if (result.length() == 0) {
            return result;
        }
        while (src.startsWith("\n")) {
            src = src.substring(1);
        }
        while (src.endsWith("\n")) {
            src = src.substring(0, src.length() - 1);
        }
        return src;
    }

    public static void prepareSendingText(final String text, final long dialog_id) {
        final int currentAccount = UserConfig.selectedAccount;
        MessagesStorage.getInstance(currentAccount).getStorageQueue().postRunnable(new Runnable() {
            public void run() {
                Utilities.stageQueue.postRunnable(new Runnable() {
                    public void run() {
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            public void run() {
                                String textFinal = SendMessagesHelper.getTrimmedString(text);
                                if (textFinal.length() != 0) {
                                    int count = (int) Math.ceil((double) (((float) textFinal.length()) / 4096.0f));
                                    for (int a = 0; a < count; a++) {
                                        SendMessagesHelper.getInstance(currentAccount).sendMessage(textFinal.substring(a * 4096, Math.min((a + 1) * 4096, textFinal.length())), dialog_id, null, null, true, null, null, null);
                                    }
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    public static void prepareSendingMedia(ArrayList<SendingMediaInfo> media, long dialog_id, MessageObject reply_to_msg, InputContentInfoCompat inputContent, boolean forceDocument, boolean groupPhotos) {
        if (!media.isEmpty()) {
            final ArrayList<SendingMediaInfo> arrayList = media;
            final long j = dialog_id;
            final int i = UserConfig.selectedAccount;
            final boolean z = forceDocument;
            final boolean z2 = groupPhotos;
            final MessageObject messageObject = reply_to_msg;
            final InputContentInfoCompat inputContentInfoCompat = inputContent;
            mediaSendQueue.postRunnable(new Runnable() {

                class AnonymousClass1 implements Runnable {
                    final /* synthetic */ SendingMediaInfo val$info;
                    final /* synthetic */ MediaSendPrepareWorker val$worker;

                    AnonymousClass1(MediaSendPrepareWorker mediaSendPrepareWorker, SendingMediaInfo sendingMediaInfo) {
                        this.val$worker = mediaSendPrepareWorker;
                        this.val$info = sendingMediaInfo;
                    }

                    public void run() {
                        this.val$worker.photo = SendMessagesHelper.getInstance(i).generatePhotoSizes(this.val$info.path, this.val$info.uri);
                        this.val$worker.sync.countDown();
                    }
                }

                class AnonymousClass2 implements Runnable {
                    final /* synthetic */ TL_document val$documentFinal;
                    final /* synthetic */ SendingMediaInfo val$info;
                    final /* synthetic */ HashMap val$params;
                    final /* synthetic */ String val$pathFinal;

                    AnonymousClass2(TL_document tL_document, String str, SendingMediaInfo sendingMediaInfo, HashMap hashMap) {
                        this.val$documentFinal = tL_document;
                        this.val$pathFinal = str;
                        this.val$info = sendingMediaInfo;
                        this.val$params = hashMap;
                    }

                    public void run() {
                        SendMessagesHelper.getInstance(i).sendMessage(this.val$documentFinal, null, this.val$pathFinal, j, messageObject, this.val$info.caption, this.val$info.entities, null, this.val$params, 0);
                    }
                }

                class AnonymousClass3 implements Runnable {
                    final /* synthetic */ SendingMediaInfo val$info;
                    final /* synthetic */ boolean val$needDownloadHttpFinal;
                    final /* synthetic */ HashMap val$params;
                    final /* synthetic */ TL_photo val$photoFinal;

                    AnonymousClass3(TL_photo tL_photo, boolean z, SendingMediaInfo sendingMediaInfo, HashMap hashMap) {
                        this.val$photoFinal = tL_photo;
                        this.val$needDownloadHttpFinal = z;
                        this.val$info = sendingMediaInfo;
                        this.val$params = hashMap;
                    }

                    public void run() {
                        SendMessagesHelper.getInstance(i).sendMessage(this.val$photoFinal, this.val$needDownloadHttpFinal ? this.val$info.searchImage.imageUrl : null, j, messageObject, this.val$info.caption, this.val$info.entities, null, this.val$params, this.val$info.ttl);
                    }
                }

                class AnonymousClass4 implements Runnable {
                    final /* synthetic */ String val$finalPath;
                    final /* synthetic */ SendingMediaInfo val$info;
                    final /* synthetic */ HashMap val$params;
                    final /* synthetic */ Bitmap val$thumbFinal;
                    final /* synthetic */ String val$thumbKeyFinal;
                    final /* synthetic */ VideoEditedInfo val$videoEditedInfo;
                    final /* synthetic */ TL_document val$videoFinal;

                    AnonymousClass4(Bitmap bitmap, String str, TL_document tL_document, VideoEditedInfo videoEditedInfo, String str2, SendingMediaInfo sendingMediaInfo, HashMap hashMap) {
                        this.val$thumbFinal = bitmap;
                        this.val$thumbKeyFinal = str;
                        this.val$videoFinal = tL_document;
                        this.val$videoEditedInfo = videoEditedInfo;
                        this.val$finalPath = str2;
                        this.val$info = sendingMediaInfo;
                        this.val$params = hashMap;
                    }

                    public void run() {
                        if (!(this.val$thumbFinal == null || this.val$thumbKeyFinal == null)) {
                            ImageLoader.getInstance().putImageToCache(new BitmapDrawable(this.val$thumbFinal), this.val$thumbKeyFinal);
                        }
                        SendMessagesHelper.getInstance(i).sendMessage(this.val$videoFinal, this.val$videoEditedInfo, this.val$finalPath, j, messageObject, this.val$info.caption, this.val$info.entities, null, this.val$params, this.val$info.ttl);
                    }
                }

                class AnonymousClass5 implements Runnable {
                    final /* synthetic */ SendingMediaInfo val$info;
                    final /* synthetic */ HashMap val$params;
                    final /* synthetic */ TL_photo val$photoFinal;

                    AnonymousClass5(TL_photo tL_photo, SendingMediaInfo sendingMediaInfo, HashMap hashMap) {
                        this.val$photoFinal = tL_photo;
                        this.val$info = sendingMediaInfo;
                        this.val$params = hashMap;
                    }

                    public void run() {
                        SendMessagesHelper.getInstance(i).sendMessage(this.val$photoFinal, null, j, messageObject, this.val$info.caption, this.val$info.entities, null, this.val$params, this.val$info.ttl);
                    }
                }

                class AnonymousClass6 implements Runnable {
                    final /* synthetic */ long val$lastGroupIdFinal;

                    AnonymousClass6(long j) {
                        this.val$lastGroupIdFinal = j;
                    }

                    public void run() {
                        SendMessagesHelper instance = SendMessagesHelper.getInstance(i);
                        HashMap access$1000 = instance.delayedMessages;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("group_");
                        stringBuilder.append(this.val$lastGroupIdFinal);
                        ArrayList<DelayedMessage> arrayList = (ArrayList) access$1000.get(stringBuilder.toString());
                        if (arrayList != null && !arrayList.isEmpty()) {
                            DelayedMessage message = (DelayedMessage) arrayList.get(0);
                            MessageObject prevMessage = (MessageObject) message.messageObjects.get(message.messageObjects.size() - 1);
                            message.finalGroupMessage = prevMessage.getId();
                            prevMessage.messageOwner.params.put("final", "1");
                            messages_Messages messagesRes = new TL_messages_messages();
                            messagesRes.messages.add(prevMessage.messageOwner);
                            MessagesStorage.getInstance(i).putMessages(messagesRes, message.peer, -2, 0, false);
                            instance.sendReadyToSendGroup(message, true, true);
                        }
                    }
                }

                public void run() {
                    /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.SendMessagesHelper.21.run():void
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
                    r10 = r90;
                    r11 = java.lang.System.currentTimeMillis();
                    r1 = r2;
                    r13 = r1.size();
                    r1 = r3;
                    r1 = (int) r1;
                    if (r1 != 0) goto L_0x0013;
                L_0x0011:
                    r1 = 1;
                    goto L_0x0014;
                L_0x0013:
                    r1 = 0;
                L_0x0014:
                    r9 = r1;
                    r1 = 0;
                    if (r9 == 0) goto L_0x0034;
                L_0x0018:
                    r2 = r3;
                    r4 = 32;
                    r2 = r2 >> r4;
                    r2 = (int) r2;
                    r3 = r5;
                    r3 = org.telegram.messenger.MessagesController.getInstance(r3);
                    r4 = java.lang.Integer.valueOf(r2);
                    r3 = r3.getEncryptedChat(r4);
                    if (r3 == 0) goto L_0x0034;
                L_0x002e:
                    r4 = r3.layer;
                    r1 = org.telegram.messenger.AndroidUtilities.getPeerLayerVersion(r4);
                L_0x0034:
                    r8 = r1;
                    r7 = 73;
                    r16 = 3;
                    r6 = 0;
                    if (r9 == 0) goto L_0x003e;
                L_0x003c:
                    if (r8 < r7) goto L_0x0130;
                L_0x003e:
                    r1 = r6;
                    if (r1 != 0) goto L_0x0130;
                L_0x0042:
                    r1 = r7;
                    if (r1 == 0) goto L_0x0130;
                L_0x0046:
                    r1 = new java.util.HashMap;
                    r1.<init>();
                    r2 = 0;
                L_0x004c:
                    if (r2 >= r13) goto L_0x0131;
                L_0x004e:
                    r3 = r2;
                    r3 = r3.get(r2);
                    r3 = (org.telegram.messenger.SendMessagesHelper.SendingMediaInfo) r3;
                    r4 = r3.searchImage;
                    if (r4 != 0) goto L_0x012a;
                L_0x005a:
                    r4 = r3.isVideo;
                    if (r4 != 0) goto L_0x012a;
                L_0x005e:
                    r4 = r3.path;
                    r5 = r3.path;
                    if (r5 != 0) goto L_0x0074;
                L_0x0064:
                    r14 = r3.uri;
                    if (r14 == 0) goto L_0x0074;
                L_0x0068:
                    r14 = r3.uri;
                    r5 = org.telegram.messenger.AndroidUtilities.getPath(r14);
                    r14 = r3.uri;
                    r4 = r14.toString();
                L_0x0074:
                    if (r5 == 0) goto L_0x0088;
                L_0x0076:
                    r14 = ".gif";
                    r14 = r5.endsWith(r14);
                    if (r14 != 0) goto L_0x012a;
                L_0x007e:
                    r14 = ".webp";
                    r14 = r5.endsWith(r14);
                    if (r14 == 0) goto L_0x0088;
                L_0x0086:
                    goto L_0x012a;
                L_0x0088:
                    if (r5 != 0) goto L_0x00a0;
                L_0x008a:
                    r14 = r3.uri;
                    if (r14 == 0) goto L_0x00a0;
                L_0x008e:
                    r14 = r3.uri;
                    r14 = org.telegram.messenger.MediaController.isGif(r14);
                    if (r14 != 0) goto L_0x012a;
                L_0x0096:
                    r14 = r3.uri;
                    r14 = org.telegram.messenger.MediaController.isWebp(r14);
                    if (r14 == 0) goto L_0x00a0;
                L_0x009e:
                    goto L_0x012a;
                L_0x00a0:
                    if (r5 == 0) goto L_0x00cb;
                L_0x00a2:
                    r14 = new java.io.File;
                    r14.<init>(r5);
                    r7 = new java.lang.StringBuilder;
                    r7.<init>();
                    r7.append(r4);
                    r20 = r4;
                    r19 = r5;
                    r4 = r14.length();
                    r7.append(r4);
                    r4 = "_";
                    r7.append(r4);
                    r4 = r14.lastModified();
                    r7.append(r4);
                    r4 = r7.toString();
                    goto L_0x00d0;
                L_0x00cb:
                    r20 = r4;
                    r19 = r5;
                    r4 = 0;
                L_0x00d0:
                    r5 = 0;
                    if (r9 != 0) goto L_0x0109;
                L_0x00d3:
                    r7 = r3.ttl;
                    if (r7 != 0) goto L_0x0109;
                L_0x00d7:
                    r7 = r5;
                    r7 = org.telegram.messenger.MessagesStorage.getInstance(r7);
                    if (r9 != 0) goto L_0x00e1;
                L_0x00df:
                    r14 = 0;
                    goto L_0x00e3;
                L_0x00e1:
                    r14 = r16;
                L_0x00e3:
                    r7 = r7.getSentFile(r4, r14);
                    r5 = r7;
                    r5 = (org.telegram.tgnet.TLRPC.TL_photo) r5;
                    if (r5 != 0) goto L_0x0109;
                L_0x00ec:
                    r7 = r3.uri;
                    if (r7 == 0) goto L_0x0109;
                L_0x00f0:
                    r7 = r5;
                    r7 = org.telegram.messenger.MessagesStorage.getInstance(r7);
                    r14 = r3.uri;
                    r14 = org.telegram.messenger.AndroidUtilities.getPath(r14);
                    if (r9 != 0) goto L_0x0100;
                L_0x00fe:
                    r15 = 0;
                    goto L_0x0102;
                L_0x0100:
                    r15 = r16;
                L_0x0102:
                    r7 = r7.getSentFile(r14, r15);
                    r5 = r7;
                    r5 = (org.telegram.tgnet.TLRPC.TL_photo) r5;
                L_0x0109:
                    r7 = new org.telegram.messenger.SendMessagesHelper$MediaSendPrepareWorker;
                    r7.<init>();
                    r1.put(r3, r7);
                    if (r5 == 0) goto L_0x0116;
                L_0x0113:
                    r7.photo = r5;
                    goto L_0x012a;
                L_0x0116:
                    r14 = new java.util.concurrent.CountDownLatch;
                    r15 = 1;
                    r14.<init>(r15);
                    r7.sync = r14;
                    r14 = org.telegram.messenger.SendMessagesHelper.mediaSendThreadPool;
                    r15 = new org.telegram.messenger.SendMessagesHelper$21$1;
                    r15.<init>(r7, r3);
                    r14.execute(r15);
                L_0x012a:
                    r2 = r2 + 1;
                    r7 = 73;
                    goto L_0x004c;
                L_0x0130:
                    r1 = r6;
                L_0x0131:
                    r14 = r1;
                    r1 = 0;
                    r3 = 0;
                    r5 = 0;
                    r7 = 0;
                    r15 = 0;
                    r19 = 0;
                    r20 = 0;
                    r22 = 0;
                    r33 = r20;
                    r87 = r15;
                    r15 = r5;
                    r5 = r87;
                    r88 = r1;
                    r2 = r19;
                    r19 = r88;
                    r1 = 0;
                    r35 = r7;
                    if (r1 >= r13) goto L_0x0a9c;
                L_0x0151:
                    r6 = r2;
                    r6 = r6.get(r1);
                    r7 = r6;
                    r7 = (org.telegram.messenger.SendMessagesHelper.SendingMediaInfo) r7;
                    r6 = r7;
                    if (r6 == 0) goto L_0x017f;
                L_0x015e:
                    if (r9 == 0) goto L_0x0165;
                L_0x0160:
                    r6 = 73;
                    if (r8 < r6) goto L_0x017f;
                L_0x0164:
                    goto L_0x0167;
                L_0x0165:
                    r6 = 73;
                L_0x0167:
                    r6 = 1;
                    if (r13 <= r6) goto L_0x017f;
                L_0x016a:
                    r6 = r22 % 10;
                    if (r6 != 0) goto L_0x017f;
                L_0x016e:
                    r6 = org.telegram.messenger.Utilities.random;
                    r23 = r6.nextLong();
                    r18 = r23;
                    r3 = r23;
                    r22 = 0;
                    r87 = r3;
                    r3 = r18;
                    goto L_0x0183;
                L_0x017f:
                    r87 = r3;
                    r3 = r19;
                L_0x0183:
                    r18 = r87;
                    r6 = r7.searchImage;
                    r39 = r1;
                    if (r6 == 0) goto L_0x0521;
                L_0x018b:
                    r6 = r7.searchImage;
                    r6 = r6.type;
                    r1 = 1;
                    if (r6 != r1) goto L_0x03a9;
                L_0x0192:
                    r1 = new java.util.HashMap;
                    r1.<init>();
                    r6 = r1;
                    r1 = 0;
                    r44 = r1;
                    r1 = r7.searchImage;
                    r1 = r1.document;
                    r1 = r1 instanceof org.telegram.tgnet.TLRPC.TL_document;
                    if (r1 == 0) goto L_0x01b7;
                L_0x01a3:
                    r1 = r7.searchImage;
                    r1 = r1.document;
                    r1 = (org.telegram.tgnet.TLRPC.TL_document) r1;
                    r45 = r2;
                    r2 = 1;
                    r20 = org.telegram.messenger.FileLoader.getPathToAttach(r1, r2);
                    r48 = r1;
                    r46 = r3;
                    r3 = r20;
                    goto L_0x0211;
                L_0x01b7:
                    r45 = r2;
                    if (r9 != 0) goto L_0x01db;
                L_0x01bb:
                    r1 = r5;
                    r1 = org.telegram.messenger.MessagesStorage.getInstance(r1);
                    r2 = r7.searchImage;
                    r2 = r2.imageUrl;
                    if (r9 != 0) goto L_0x01cb;
                L_0x01c7:
                    r46 = r3;
                    r3 = 1;
                    goto L_0x01ce;
                L_0x01cb:
                    r46 = r3;
                    r3 = 4;
                L_0x01ce:
                    r1 = r1.getSentFile(r2, r3);
                    r1 = (org.telegram.tgnet.TLRPC.Document) r1;
                    r2 = r1 instanceof org.telegram.tgnet.TLRPC.TL_document;
                    if (r2 == 0) goto L_0x01dd;
                L_0x01d8:
                    r1 = (org.telegram.tgnet.TLRPC.TL_document) r1;
                    goto L_0x01df;
                L_0x01db:
                    r46 = r3;
                L_0x01dd:
                    r1 = r44;
                L_0x01df:
                    r2 = new java.lang.StringBuilder;
                    r2.<init>();
                    r3 = r7.searchImage;
                    r3 = r3.imageUrl;
                    r3 = org.telegram.messenger.Utilities.MD5(r3);
                    r2.append(r3);
                    r3 = ".";
                    r2.append(r3);
                    r3 = r7.searchImage;
                    r3 = r3.imageUrl;
                    r4 = "jpg";
                    r3 = org.telegram.messenger.ImageLoader.getHttpUrlExtension(r3, r4);
                    r2.append(r3);
                    r2 = r2.toString();
                    r3 = new java.io.File;
                    r48 = r1;
                    r4 = 4;
                    r1 = org.telegram.messenger.FileLoader.getDirectory(r4);
                    r3.<init>(r1, r2);
                L_0x0211:
                    r1 = r3;
                    if (r48 != 0) goto L_0x0342;
                L_0x0214:
                    r2 = r7.searchImage;
                    r2 = r2.localUrl;
                    if (r2 == 0) goto L_0x0223;
                L_0x021a:
                    r2 = "url";
                    r3 = r7.searchImage;
                    r3 = r3.localUrl;
                    r6.put(r2, r3);
                L_0x0223:
                    r2 = 0;
                    r3 = new org.telegram.tgnet.TLRPC$TL_document;
                    r3.<init>();
                    r49 = r5;
                    r4 = 0;
                    r3.id = r4;
                    r4 = r5;
                    r4 = org.telegram.tgnet.ConnectionsManager.getInstance(r4);
                    r4 = r4.getCurrentTime();
                    r3.date = r4;
                    r4 = new org.telegram.tgnet.TLRPC$TL_documentAttributeFilename;
                    r4.<init>();
                    r5 = "animation.gif";
                    r4.file_name = r5;
                    r5 = r3.attributes;
                    r5.add(r4);
                    r5 = r7.searchImage;
                    r5 = r5.size;
                    r3.size = r5;
                    r5 = 0;
                    r3.dc_id = r5;
                    r5 = r1.toString();
                    r50 = r2;
                    r2 = "mp4";
                    r2 = r5.endsWith(r2);
                    if (r2 == 0) goto L_0x026f;
                L_0x0260:
                    r2 = "video/mp4";
                    r3.mime_type = r2;
                    r2 = r3.attributes;
                    r5 = new org.telegram.tgnet.TLRPC$TL_documentAttributeAnimated;
                    r5.<init>();
                    r2.add(r5);
                    goto L_0x0273;
                L_0x026f:
                    r2 = "image/gif";
                    r3.mime_type = r2;
                L_0x0273:
                    r2 = r1.exists();
                    if (r2 == 0) goto L_0x027b;
                L_0x0279:
                    r2 = r1;
                    goto L_0x027e;
                L_0x027b:
                    r1 = 0;
                    r2 = r50;
                L_0x027e:
                    if (r2 != 0) goto L_0x02bc;
                L_0x0280:
                    r5 = new java.lang.StringBuilder;
                    r5.<init>();
                    r51 = r1;
                    r1 = r7.searchImage;
                    r1 = r1.thumbUrl;
                    r1 = org.telegram.messenger.Utilities.MD5(r1);
                    r5.append(r1);
                    r1 = ".";
                    r5.append(r1);
                    r1 = r7.searchImage;
                    r1 = r1.thumbUrl;
                    r52 = r2;
                    r2 = "jpg";
                    r1 = org.telegram.messenger.ImageLoader.getHttpUrlExtension(r1, r2);
                    r5.append(r1);
                    r1 = r5.toString();
                    r2 = new java.io.File;
                    r5 = 4;
                    r5 = org.telegram.messenger.FileLoader.getDirectory(r5);
                    r2.<init>(r5, r1);
                    r5 = r2.exists();
                    if (r5 != 0) goto L_0x02c0;
                L_0x02ba:
                    r2 = 0;
                    goto L_0x02c0;
                L_0x02bc:
                    r51 = r1;
                    r52 = r2;
                L_0x02c0:
                    if (r2 == 0) goto L_0x030b;
                L_0x02c2:
                    r1 = r2.getAbsolutePath();	 Catch:{ Exception -> 0x0302 }
                    r5 = "mp4";	 Catch:{ Exception -> 0x0302 }
                    r1 = r1.endsWith(r5);	 Catch:{ Exception -> 0x0302 }
                    if (r1 == 0) goto L_0x02e3;
                L_0x02ce:
                    r1 = r2.getAbsolutePath();	 Catch:{ Exception -> 0x02dd }
                    r5 = 1;	 Catch:{ Exception -> 0x02dd }
                    r1 = android.media.ThumbnailUtils.createVideoThumbnail(r1, r5);	 Catch:{ Exception -> 0x02dd }
                    r53 = r11;
                    r11 = 0;
                    r12 = 1119092736; // 0x42b40000 float:90.0 double:5.529052754E-315;
                    goto L_0x02f1;
                L_0x02dd:
                    r0 = move-exception;
                    r1 = r0;
                    r53 = r11;
                    r11 = 0;
                    goto L_0x0307;
                L_0x02e3:
                    r1 = r2.getAbsolutePath();	 Catch:{ Exception -> 0x0302 }
                    r53 = r11;
                    r5 = 1;
                    r11 = 0;
                    r12 = 1119092736; // 0x42b40000 float:90.0 double:5.529052754E-315;
                    r1 = org.telegram.messenger.ImageLoader.loadBitmap(r1, r11, r12, r12, r5);	 Catch:{ Exception -> 0x02ff }
                L_0x02f1:
                    if (r1 == 0) goto L_0x02fe;	 Catch:{ Exception -> 0x02ff }
                L_0x02f3:
                    r5 = 55;	 Catch:{ Exception -> 0x02ff }
                    r5 = org.telegram.messenger.ImageLoader.scaleAndSaveImage(r1, r12, r12, r5, r9);	 Catch:{ Exception -> 0x02ff }
                    r3.thumb = r5;	 Catch:{ Exception -> 0x02ff }
                    r1.recycle();	 Catch:{ Exception -> 0x02ff }
                L_0x02fe:
                    goto L_0x030e;
                L_0x02ff:
                    r0 = move-exception;
                    r1 = r0;
                    goto L_0x0307;
                L_0x0302:
                    r0 = move-exception;
                    r53 = r11;
                    r11 = 0;
                    r1 = r0;
                L_0x0307:
                    org.telegram.messenger.FileLog.e(r1);
                    goto L_0x030e;
                L_0x030b:
                    r53 = r11;
                    r11 = 0;
                L_0x030e:
                    r1 = r3.thumb;
                    if (r1 != 0) goto L_0x033d;
                L_0x0312:
                    r1 = new org.telegram.tgnet.TLRPC$TL_photoSize;
                    r1.<init>();
                    r3.thumb = r1;
                    r1 = r3.thumb;
                    r5 = r7.searchImage;
                    r5 = r5.width;
                    r1.w = r5;
                    r1 = r3.thumb;
                    r5 = r7.searchImage;
                    r5 = r5.height;
                    r1.h = r5;
                    r1 = r3.thumb;
                    r5 = 0;
                    r1.size = r5;
                    r1 = r3.thumb;
                    r5 = new org.telegram.tgnet.TLRPC$TL_fileLocationUnavailable;
                    r5.<init>();
                    r1.location = r5;
                    r1 = r3.thumb;
                    r5 = "x";
                    r1.type = r5;
                L_0x033d:
                    r48 = r3;
                    r12 = r51;
                    goto L_0x0348;
                L_0x0342:
                    r49 = r5;
                    r53 = r11;
                    r11 = 0;
                    r12 = r1;
                L_0x0348:
                    r1 = r46;
                    r3 = r48;
                    r4 = r7.searchImage;
                    r5 = r4.imageUrl;
                    if (r12 != 0) goto L_0x0357;
                L_0x0352:
                    r4 = r7.searchImage;
                    r4 = r4.imageUrl;
                    goto L_0x035b;
                L_0x0357:
                    r4 = r12.toString();
                L_0x035b:
                    if (r6 == 0) goto L_0x036f;
                L_0x035d:
                    r11 = r7.searchImage;
                    r11 = r11.imageUrl;
                    if (r11 == 0) goto L_0x036f;
                L_0x0363:
                    r11 = "originalPath";
                    r55 = r1;
                    r1 = r7.searchImage;
                    r1 = r1.imageUrl;
                    r6.put(r11, r1);
                    goto L_0x0371;
                L_0x036f:
                    r55 = r1;
                L_0x0371:
                    r11 = new org.telegram.messenger.SendMessagesHelper$21$2;
                    r57 = r14;
                    r58 = r15;
                    r2 = r39;
                    r14 = r55;
                    r1 = r11;
                    r59 = r12;
                    r60 = r45;
                    r12 = r2;
                    r2 = r10;
                    r20 = r5;
                    r61 = r49;
                    r5 = r7;
                    r23 = r6;
                    r62 = r8;
                    r8 = 0;
                    r24 = 73;
                    r1.<init>(r3, r4, r5, r6);
                    org.telegram.messenger.AndroidUtilities.runOnUIThread(r11);
                    r28 = r8;
                    r3 = r18;
                    r21 = r24;
                    r7 = r35;
                    r6 = r57;
                    r2 = r60;
                    r5 = r61;
                    r34 = r62;
                    r25 = 0;
                    goto L_0x051b;
                L_0x03a9:
                    r60 = r2;
                    r61 = r5;
                    r62 = r8;
                    r53 = r11;
                    r57 = r14;
                    r58 = r15;
                    r12 = r39;
                    r8 = 0;
                    r24 = 73;
                    r14 = r3;
                    r1 = 1;
                    r2 = 0;
                    if (r9 != 0) goto L_0x03da;
                L_0x03bf:
                    r3 = r7.ttl;
                    if (r3 != 0) goto L_0x03da;
                L_0x03c3:
                    r3 = r5;
                    r3 = org.telegram.messenger.MessagesStorage.getInstance(r3);
                    r4 = r7.searchImage;
                    r4 = r4.imageUrl;
                    if (r9 != 0) goto L_0x03d1;
                L_0x03cf:
                    r5 = 0;
                    goto L_0x03d3;
                L_0x03d1:
                    r5 = r16;
                L_0x03d3:
                    r3 = r3.getSentFile(r4, r5);
                    r2 = r3;
                    r2 = (org.telegram.tgnet.TLRPC.TL_photo) r2;
                L_0x03da:
                    if (r2 != 0) goto L_0x04ac;
                L_0x03dc:
                    r3 = new java.lang.StringBuilder;
                    r3.<init>();
                    r4 = r7.searchImage;
                    r4 = r4.imageUrl;
                    r4 = org.telegram.messenger.Utilities.MD5(r4);
                    r3.append(r4);
                    r4 = ".";
                    r3.append(r4);
                    r4 = r7.searchImage;
                    r4 = r4.imageUrl;
                    r5 = "jpg";
                    r4 = org.telegram.messenger.ImageLoader.getHttpUrlExtension(r4, r5);
                    r3.append(r4);
                    r3 = r3.toString();
                    r4 = new java.io.File;
                    r5 = 4;
                    r6 = org.telegram.messenger.FileLoader.getDirectory(r5);
                    r4.<init>(r6, r3);
                    r5 = r4.exists();
                    if (r5 == 0) goto L_0x042d;
                L_0x0412:
                    r5 = r4.length();
                    r25 = 0;
                    r11 = (r5 > r25 ? 1 : (r5 == r25 ? 0 : -1));
                    if (r11 == 0) goto L_0x042d;
                L_0x041c:
                    r5 = r5;
                    r5 = org.telegram.messenger.SendMessagesHelper.getInstance(r5);
                    r6 = r4.toString();
                    r2 = r5.generatePhotoSizes(r6, r8);
                    if (r2 == 0) goto L_0x042d;
                L_0x042c:
                    r1 = 0;
                L_0x042d:
                    if (r2 != 0) goto L_0x04ac;
                L_0x042f:
                    r5 = new java.lang.StringBuilder;
                    r5.<init>();
                    r6 = r7.searchImage;
                    r6 = r6.thumbUrl;
                    r6 = org.telegram.messenger.Utilities.MD5(r6);
                    r5.append(r6);
                    r6 = ".";
                    r5.append(r6);
                    r6 = r7.searchImage;
                    r6 = r6.thumbUrl;
                    r11 = "jpg";
                    r6 = org.telegram.messenger.ImageLoader.getHttpUrlExtension(r6, r11);
                    r5.append(r6);
                    r3 = r5.toString();
                    r5 = new java.io.File;
                    r6 = 4;
                    r6 = org.telegram.messenger.FileLoader.getDirectory(r6);
                    r5.<init>(r6, r3);
                    r4 = r5;
                    r5 = r4.exists();
                    if (r5 == 0) goto L_0x0474;
                L_0x0466:
                    r5 = r5;
                    r5 = org.telegram.messenger.SendMessagesHelper.getInstance(r5);
                    r6 = r4.toString();
                    r2 = r5.generatePhotoSizes(r6, r8);
                L_0x0474:
                    if (r2 != 0) goto L_0x04ac;
                L_0x0476:
                    r5 = new org.telegram.tgnet.TLRPC$TL_photo;
                    r5.<init>();
                    r2 = r5;
                    r5 = r5;
                    r5 = org.telegram.tgnet.ConnectionsManager.getInstance(r5);
                    r5 = r5.getCurrentTime();
                    r2.date = r5;
                    r5 = new org.telegram.tgnet.TLRPC$TL_photoSize;
                    r5.<init>();
                    r6 = r7.searchImage;
                    r6 = r6.width;
                    r5.w = r6;
                    r6 = r7.searchImage;
                    r6 = r6.height;
                    r5.h = r6;
                    r6 = 0;
                    r5.size = r6;
                    r6 = new org.telegram.tgnet.TLRPC$TL_fileLocationUnavailable;
                    r6.<init>();
                    r5.location = r6;
                    r6 = "x";
                    r5.type = r6;
                    r6 = r2.sizes;
                    r6.add(r5);
                L_0x04ac:
                    r20 = r1;
                    r11 = r2;
                    if (r11 == 0) goto L_0x0508;
                L_0x04b1:
                    r3 = r11;
                    r4 = r20;
                    r1 = new java.util.HashMap;
                    r1.<init>();
                    r6 = r1;
                    r1 = r7.searchImage;
                    r1 = r1.imageUrl;
                    if (r1 == 0) goto L_0x04c9;
                    r1 = "originalPath";
                    r2 = r7.searchImage;
                    r2 = r2.imageUrl;
                    r6.put(r1, r2);
                    r1 = r7;
                    if (r1 == 0) goto L_0x04fb;
                    r1 = r22 + 1;
                    r2 = "groupId";
                    r5 = new java.lang.StringBuilder;
                    r5.<init>();
                    r8 = "";
                    r5.append(r8);
                    r5.append(r14);
                    r5 = r5.toString();
                    r6.put(r2, r5);
                    r2 = 10;
                    if (r1 == r2) goto L_0x04f1;
                    r2 = r13 + -1;
                    if (r12 != r2) goto L_0x04ee;
                    goto L_0x04f1;
                    r22 = r1;
                    goto L_0x04fb;
                    r2 = "final";
                    r5 = "1";
                    r6.put(r2, r5);
                    r18 = 0;
                    goto L_0x04ee;
                    r8 = new org.telegram.messenger.SendMessagesHelper$21$3;
                    r1 = r8;
                    r2 = r10;
                    r5 = r7;
                    r23 = r6;
                    r1.<init>(r3, r4, r5, r6);
                    org.telegram.messenger.AndroidUtilities.runOnUIThread(r8);
                    r3 = r18;
                    r21 = r24;
                    r7 = r35;
                    r6 = r57;
                    r2 = r60;
                    r5 = r61;
                    r34 = r62;
                    r25 = 0;
                    r28 = 0;
                L_0x051b:
                    r29 = 1;
                    r35 = r9;
                    goto L_0x0a8b;
                L_0x0521:
                    r60 = r2;
                    r61 = r5;
                    r62 = r8;
                    r53 = r11;
                    r57 = r14;
                    r58 = r15;
                    r12 = r39;
                    r24 = 73;
                    r14 = r3;
                    r1 = r7.isVideo;
                    if (r1 == 0) goto L_0x0846;
                    r1 = 0;
                    r11 = 0;
                    r2 = r6;
                    if (r2 == 0) goto L_0x053e;
                    r2 = 0;
                    goto L_0x054b;
                    r2 = r7.videoEditedInfo;
                    if (r2 == 0) goto L_0x0545;
                    r2 = r7.videoEditedInfo;
                    goto L_0x054b;
                    r2 = r7.path;
                    r2 = org.telegram.messenger.SendMessagesHelper.createCompressionSettings(r2);
                    r8 = r2;
                    r2 = r6;
                    if (r2 != 0) goto L_0x07fe;
                    if (r8 != 0) goto L_0x0575;
                    r2 = r7.path;
                    r3 = "mp4";
                    r2 = r2.endsWith(r3);
                    if (r2 == 0) goto L_0x055d;
                    goto L_0x0575;
                    r63 = r1;
                    r81 = r7;
                    r27 = r8;
                    r82 = r11;
                    r21 = r24;
                    r77 = r35;
                    r34 = r62;
                    r25 = 0;
                    r28 = 0;
                    r29 = 1;
                    r35 = r9;
                    goto L_0x0814;
                    r2 = r7.path;
                    r3 = r7.path;
                    r4 = new java.io.File;
                    r4.<init>(r3);
                    r6 = r4;
                    r4 = 0;
                    r20 = 0;
                    r63 = r1;
                    r1 = new java.lang.StringBuilder;
                    r1.<init>();
                    r1.append(r3);
                    r64 = r2;
                    r65 = r3;
                    r2 = r6.length();
                    r1.append(r2);
                    r2 = "_";
                    r1.append(r2);
                    r2 = r6.lastModified();
                    r1.append(r2);
                    r1 = r1.toString();
                    if (r8 == 0) goto L_0x0610;
                    r2 = r8.muted;
                    r3 = new java.lang.StringBuilder;
                    r3.<init>();
                    r3.append(r1);
                    r66 = r1;
                    r67 = r2;
                    r1 = r8.estimatedDuration;
                    r3.append(r1);
                    r1 = "_";
                    r3.append(r1);
                    r1 = r8.startTime;
                    r3.append(r1);
                    r1 = "_";
                    r3.append(r1);
                    r1 = r8.endTime;
                    r3.append(r1);
                    r1 = r8.muted;
                    if (r1 == 0) goto L_0x05d8;
                    r1 = "_m";
                    goto L_0x05da;
                    r1 = "";
                    r3.append(r1);
                    r1 = r3.toString();
                    r2 = r8.resultWidth;
                    r3 = r8.originalWidth;
                    if (r2 == r3) goto L_0x05fd;
                    r2 = new java.lang.StringBuilder;
                    r2.<init>();
                    r2.append(r1);
                    r3 = "_";
                    r2.append(r3);
                    r3 = r8.resultWidth;
                    r2.append(r3);
                    r1 = r2.toString();
                    r2 = r8.startTime;
                    r25 = 0;
                    r20 = (r2 > r25 ? 1 : (r2 == r25 ? 0 : -1));
                    if (r20 < 0) goto L_0x0608;
                    r2 = r8.startTime;
                    goto L_0x060a;
                    r2 = 0;
                    r4 = r2;
                    r2 = r1;
                    r3 = r4;
                    r20 = r67;
                    goto L_0x0615;
                    r66 = r1;
                    r3 = r4;
                    r2 = r66;
                    r1 = 0;
                    if (r9 != 0) goto L_0x0635;
                    r5 = r7.ttl;
                    if (r5 != 0) goto L_0x0635;
                    r5 = r5;
                    r5 = org.telegram.messenger.MessagesStorage.getInstance(r5);
                    if (r9 != 0) goto L_0x062b;
                    r23 = 2;
                    r68 = r1;
                    r1 = r23;
                    goto L_0x062e;
                    r23 = 5;
                    goto L_0x0626;
                    r1 = r5.getSentFile(r2, r1);
                    r1 = (org.telegram.tgnet.TLRPC.TL_document) r1;
                    goto L_0x0639;
                    r68 = r1;
                    r1 = r68;
                    if (r1 != 0) goto L_0x0763;
                    r5 = r7.path;
                    r5 = org.telegram.messenger.SendMessagesHelper.createVideoThumbnail(r5, r3);
                    if (r5 != 0) goto L_0x064f;
                    r69 = r1;
                    r1 = r7.path;
                    r70 = r3;
                    r3 = 1;
                    r5 = android.media.ThumbnailUtils.createVideoThumbnail(r1, r3);
                    goto L_0x0653;
                    r69 = r1;
                    r70 = r3;
                    r1 = 1119092736; // 0x42b40000 float:90.0 double:5.529052754E-315;
                    r3 = 55;
                    r1 = org.telegram.messenger.ImageLoader.scaleAndSaveImage(r5, r1, r1, r3, r9);
                    if (r5 == 0) goto L_0x0660;
                    if (r1 == 0) goto L_0x0660;
                    r5 = 0;
                    r3 = new org.telegram.tgnet.TLRPC$TL_document;
                    r3.<init>();
                    r3.thumb = r1;
                    r4 = r3.thumb;
                    if (r4 != 0) goto L_0x067b;
                    r4 = new org.telegram.tgnet.TLRPC$TL_photoSizeEmpty;
                    r4.<init>();
                    r3.thumb = r4;
                    r4 = r3.thumb;
                    r72 = r1;
                    r1 = "s";
                    r4.type = r1;
                    goto L_0x0683;
                    r72 = r1;
                    r1 = r3.thumb;
                    r4 = "s";
                    r1.type = r4;
                    r1 = "video/mp4";
                    r3.mime_type = r1;
                    r1 = r5;
                    r1 = org.telegram.messenger.UserConfig.getInstance(r1);
                    r4 = 0;
                    r1.saveConfig(r4);
                    if (r9 == 0) goto L_0x06aa;
                    r1 = 66;
                    r73 = r9;
                    r9 = r62;
                    if (r9 < r1) goto L_0x06a4;
                    r1 = new org.telegram.tgnet.TLRPC$TL_documentAttributeVideo;
                    r1.<init>();
                    r74 = r9;
                    r9 = 1;
                    goto L_0x06b8;
                    r1 = new org.telegram.tgnet.TLRPC$TL_documentAttributeVideo_layer65;
                    r1.<init>();
                    goto L_0x06a0;
                    r73 = r9;
                    r9 = r62;
                    r1 = new org.telegram.tgnet.TLRPC$TL_documentAttributeVideo;
                    r1.<init>();
                    r74 = r9;
                    r9 = 1;
                    r1.supports_streaming = r9;
                    r4 = r3.attributes;
                    r4.add(r1);
                    if (r8 == 0) goto L_0x074b;
                    r4 = r8.needConvert();
                    if (r4 == 0) goto L_0x074b;
                    r4 = r8.muted;
                    if (r4 == 0) goto L_0x06eb;
                    r4 = r3.attributes;
                    r9 = new org.telegram.tgnet.TLRPC$TL_documentAttributeAnimated;
                    r9.<init>();
                    r4.add(r9);
                    r4 = r7.path;
                    org.telegram.messenger.SendMessagesHelper.fillVideoAttribute(r4, r1, r8);
                    r4 = r1.w;
                    r8.originalWidth = r4;
                    r4 = r1.h;
                    r8.originalHeight = r4;
                    r4 = r8.resultWidth;
                    r1.w = r4;
                    r4 = r8.resultHeight;
                    r1.h = r4;
                    r75 = r5;
                    goto L_0x0714;
                    r75 = r5;
                    r4 = r8.estimatedDuration;
                    r25 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
                    r4 = r4 / r25;
                    r4 = (int) r4;
                    r1.duration = r4;
                    r4 = r8.rotationValue;
                    r5 = 90;
                    if (r4 == r5) goto L_0x070c;
                    r4 = r8.rotationValue;
                    r5 = 270; // 0x10e float:3.78E-43 double:1.334E-321;
                    if (r4 != r5) goto L_0x0703;
                    goto L_0x070c;
                    r4 = r8.resultWidth;
                    r1.w = r4;
                    r4 = r8.resultHeight;
                    r1.h = r4;
                    goto L_0x0714;
                    r4 = r8.resultHeight;
                    r1.w = r4;
                    r4 = r8.resultWidth;
                    r1.h = r4;
                    r4 = r8.estimatedSize;
                    r4 = (int) r4;
                    r3.size = r4;
                    r4 = new java.lang.StringBuilder;
                    r4.<init>();
                    r5 = "-2147483648_";
                    r4.append(r5);
                    r5 = org.telegram.messenger.SharedConfig.getLastLocalId();
                    r4.append(r5);
                    r5 = ".mp4";
                    r4.append(r5);
                    r4 = r4.toString();
                    r5 = new java.io.File;
                    r9 = 4;
                    r9 = org.telegram.messenger.FileLoader.getDirectory(r9);
                    r5.<init>(r9, r4);
                    org.telegram.messenger.SharedConfig.saveConfig();
                    r4 = r5.getAbsolutePath();
                    r69 = r3;
                    r64 = r4;
                    r9 = 0;
                    goto L_0x076e;
                    r75 = r5;
                    r4 = r6.exists();
                    if (r4 == 0) goto L_0x075a;
                    r4 = r6.length();
                    r4 = (int) r4;
                    r3.size = r4;
                    r4 = r7.path;
                    r9 = 0;
                    org.telegram.messenger.SendMessagesHelper.fillVideoAttribute(r4, r1, r9);
                    r69 = r3;
                    goto L_0x076e;
                    r69 = r1;
                    r70 = r3;
                    r73 = r9;
                    r74 = r62;
                    r9 = 0;
                    r75 = r63;
                    r5 = r69;
                    r17 = r2;
                    r1 = r7;
                    r21 = r24;
                    r4 = r35;
                    r7 = r64;
                    r3 = new java.util.HashMap;
                    r3.<init>();
                    r76 = r5;
                    r23 = r70;
                    r5 = r3;
                    r3 = r75;
                    r77 = r4;
                    r25 = 0;
                    r4 = r11;
                    if (r2 == 0) goto L_0x0791;
                    r9 = "originalPath";
                    r5.put(r9, r2);
                    if (r20 != 0) goto L_0x07cd;
                    r9 = r7;
                    if (r9 == 0) goto L_0x07cd;
                    r9 = r22 + 1;
                    r78 = r1;
                    r1 = "groupId";
                    r79 = r2;
                    r2 = new java.lang.StringBuilder;
                    r2.<init>();
                    r80 = r6;
                    r6 = "";
                    r2.append(r6);
                    r2.append(r14);
                    r2 = r2.toString();
                    r5.put(r1, r2);
                    r1 = 10;
                    if (r9 == r1) goto L_0x07c1;
                    r1 = r13 + -1;
                    if (r12 != r1) goto L_0x07be;
                    goto L_0x07c1;
                    r22 = r9;
                    goto L_0x07d3;
                    r1 = "final";
                    r2 = "1";
                    r5.put(r1, r2);
                    r1 = 0;
                    r18 = r1;
                    goto L_0x07be;
                    r78 = r1;
                    r79 = r2;
                    r80 = r6;
                    r9 = new org.telegram.messenger.SendMessagesHelper$21$4;
                    r6 = r78;
                    r1 = r9;
                    r66 = r79;
                    r2 = r10;
                    r81 = r6;
                    r26 = r80;
                    r6 = r8;
                    r27 = r8;
                    r34 = r74;
                    r28 = 0;
                    r8 = r81;
                    r82 = r11;
                    r35 = r73;
                    r29 = 1;
                    r11 = r9;
                    r9 = r5;
                    r30 = r5;
                    r5 = r76;
                    r1.<init>(r3, r4, r5, r6, r7, r8, r9);
                    org.telegram.messenger.AndroidUtilities.runOnUIThread(r11);
                    r2 = r81;
                    goto L_0x0839;
                    r63 = r1;
                    r81 = r7;
                    r27 = r8;
                    r82 = r11;
                    r21 = r24;
                    r77 = r35;
                    r34 = r62;
                    r25 = 0;
                    r28 = 0;
                    r29 = 1;
                    r35 = r9;
                    r1 = r5;
                    r2 = r81;
                    r3 = r2.path;
                    r4 = r2.path;
                    r39 = 0;
                    r40 = 0;
                    r5 = r3;
                    r7 = r8;
                    r8 = r2.caption;
                    r9 = r2.entities;
                    r36 = r1;
                    r37 = r3;
                    r38 = r4;
                    r41 = r5;
                    r43 = r7;
                    r44 = r8;
                    r45 = r9;
                    org.telegram.messenger.SendMessagesHelper.prepareSendingDocumentInternal(r36, r37, r38, r39, r40, r41, r43, r44, r45);
                    r3 = r18;
                    r6 = r57;
                    r2 = r60;
                    r5 = r61;
                    r7 = r77;
                    goto L_0x0a8b;
                    r2 = r7;
                    r21 = r24;
                    r77 = r35;
                    r34 = r62;
                    r25 = 0;
                    r28 = 0;
                    r29 = 1;
                    r35 = r9;
                    r1 = r2.path;
                    r3 = r2.path;
                    if (r3 != 0) goto L_0x086b;
                    r4 = r2.uri;
                    if (r4 == 0) goto L_0x086b;
                    r4 = r2.uri;
                    r3 = org.telegram.messenger.AndroidUtilities.getPath(r4);
                    r4 = r2.uri;
                    r1 = r4.toString();
                    r4 = 0;
                    r5 = r6;
                    if (r5 == 0) goto L_0x087b;
                    r4 = 1;
                    r5 = new java.io.File;
                    r5.<init>(r3);
                    r33 = org.telegram.messenger.FileLoader.getFileExtension(r5);
                    goto L_0x08d8;
                    if (r3 == 0) goto L_0x089f;
                    r5 = ".gif";
                    r5 = r3.endsWith(r5);
                    if (r5 != 0) goto L_0x088d;
                    r5 = ".webp";
                    r5 = r3.endsWith(r5);
                    if (r5 == 0) goto L_0x089f;
                    r5 = ".gif";
                    r5 = r3.endsWith(r5);
                    if (r5 == 0) goto L_0x089a;
                    r5 = "gif";
                    r33 = r5;
                    goto L_0x089d;
                    r5 = "webp";
                    goto L_0x0897;
                    r4 = 1;
                    goto L_0x08d8;
                    if (r3 != 0) goto L_0x08d8;
                    r5 = r2.uri;
                    if (r5 == 0) goto L_0x08d8;
                    r5 = r2.uri;
                    r5 = org.telegram.messenger.MediaController.isGif(r5);
                    if (r5 == 0) goto L_0x08bf;
                    r4 = 1;
                    r5 = r2.uri;
                    r1 = r5.toString();
                    r5 = r2.uri;
                    r6 = "gif";
                    r3 = org.telegram.messenger.MediaController.copyFileToCache(r5, r6);
                    r33 = "gif";
                    goto L_0x08d8;
                    r5 = r2.uri;
                    r5 = org.telegram.messenger.MediaController.isWebp(r5);
                    if (r5 == 0) goto L_0x08d8;
                    r4 = 1;
                    r5 = r2.uri;
                    r1 = r5.toString();
                    r5 = r2.uri;
                    r6 = "webp";
                    r3 = org.telegram.messenger.MediaController.copyFileToCache(r5, r6);
                    r33 = "webp";
                    if (r4 == 0) goto L_0x0914;
                    if (r58 != 0) goto L_0x08f2;
                    r5 = new java.util.ArrayList;
                    r5.<init>();
                    r6 = new java.util.ArrayList;
                    r6.<init>();
                    r7 = r6;
                    r6 = new java.util.ArrayList;
                    r6.<init>();
                    r8 = new java.util.ArrayList;
                    r8.<init>();
                    goto L_0x08fa;
                    r5 = r58;
                    r8 = r60;
                    r6 = r61;
                    r7 = r77;
                    r5.add(r3);
                    r7.add(r1);
                    r9 = r2.caption;
                    r6.add(r9);
                    r9 = r2.entities;
                    r8.add(r9);
                    r58 = r5;
                    r5 = r6;
                    r2 = r8;
                    r3 = r18;
                    r6 = r57;
                    goto L_0x0a8b;
                    if (r3 == 0) goto L_0x093b;
                    r5 = new java.io.File;
                    r5.<init>(r3);
                    r6 = new java.lang.StringBuilder;
                    r6.<init>();
                    r6.append(r1);
                    r7 = r5.length();
                    r6.append(r7);
                    r7 = "_";
                    r6.append(r7);
                    r7 = r5.lastModified();
                    r6.append(r7);
                    r1 = r6.toString();
                    goto L_0x093c;
                    r1 = 0;
                    r5 = 0;
                    if (r57 == 0) goto L_0x0959;
                    r6 = r57;
                    r7 = r6.get(r2);
                    r7 = (org.telegram.messenger.SendMessagesHelper.MediaSendPrepareWorker) r7;
                    r5 = r7.photo;
                    if (r5 != 0) goto L_0x0958;
                    r8 = r7.sync;	 Catch:{ Exception -> 0x0951 }
                    r8.await();	 Catch:{ Exception -> 0x0951 }
                    goto L_0x0956;
                L_0x0951:
                    r0 = move-exception;
                    r8 = r0;
                    org.telegram.messenger.FileLog.e(r8);
                    r5 = r7.photo;
                    goto L_0x09a5;
                    r6 = r57;
                    if (r35 != 0) goto L_0x0995;
                    r7 = r2.ttl;
                    if (r7 != 0) goto L_0x0995;
                    r7 = r5;
                    r7 = org.telegram.messenger.MessagesStorage.getInstance(r7);
                    if (r35 != 0) goto L_0x096c;
                    r8 = r25;
                    goto L_0x096e;
                    r8 = r16;
                    r7 = r7.getSentFile(r1, r8);
                    r5 = r7;
                    r5 = (org.telegram.tgnet.TLRPC.TL_photo) r5;
                    if (r5 != 0) goto L_0x0995;
                    r7 = r2.uri;
                    if (r7 == 0) goto L_0x0995;
                    r7 = r5;
                    r7 = org.telegram.messenger.MessagesStorage.getInstance(r7);
                    r8 = r2.uri;
                    r8 = org.telegram.messenger.AndroidUtilities.getPath(r8);
                    if (r35 != 0) goto L_0x098c;
                    r9 = r25;
                    goto L_0x098e;
                    r9 = r16;
                    r7 = r7.getSentFile(r8, r9);
                    r5 = r7;
                    r5 = (org.telegram.tgnet.TLRPC.TL_photo) r5;
                    if (r5 != 0) goto L_0x09a5;
                    r7 = r5;
                    r7 = org.telegram.messenger.SendMessagesHelper.getInstance(r7);
                    r8 = r2.path;
                    r9 = r2.uri;
                    r5 = r7.generatePhotoSizes(r8, r9);
                    if (r5 == 0) goto L_0x0a52;
                    r7 = r5;
                    r8 = new java.util.HashMap;
                    r8.<init>();
                    r9 = r2.masks;
                    if (r9 == 0) goto L_0x09bc;
                    r9 = r2.masks;
                    r9 = r9.isEmpty();
                    if (r9 != 0) goto L_0x09bc;
                    r9 = r29;
                    goto L_0x09be;
                    r9 = r25;
                    r5.has_stickers = r9;
                    if (r9 == 0) goto L_0x0a06;
                    r9 = new org.telegram.tgnet.SerializedData;
                    r11 = r2.masks;
                    r11 = r11.size();
                    r11 = r11 * 20;
                    r17 = 4;
                    r11 = r17 + r11;
                    r9.<init>(r11);
                    r11 = r2.masks;
                    r11 = r11.size();
                    r9.writeInt32(r11);
                    r11 = r25;
                    r83 = r4;
                    r4 = r2.masks;
                    r4 = r4.size();
                    if (r11 >= r4) goto L_0x09f8;
                    r4 = r2.masks;
                    r4 = r4.get(r11);
                    r4 = (org.telegram.tgnet.TLRPC.InputDocument) r4;
                    r4.serializeToStream(r9);
                    r11 = r11 + 1;
                    r4 = r83;
                    goto L_0x09de;
                    r4 = "masks";
                    r11 = r9.toByteArray();
                    r11 = org.telegram.messenger.Utilities.bytesToHex(r11);
                    r8.put(r4, r11);
                    goto L_0x0a08;
                    r83 = r4;
                    if (r1 == 0) goto L_0x0a0f;
                    r4 = "originalPath";
                    r8.put(r4, r1);
                    r4 = r7;
                    if (r4 == 0) goto L_0x0a3f;
                    r4 = r22 + 1;
                    r9 = "groupId";
                    r11 = new java.lang.StringBuilder;
                    r11.<init>();
                    r84 = r5;
                    r5 = "";
                    r11.append(r5);
                    r11.append(r14);
                    r5 = r11.toString();
                    r8.put(r9, r5);
                    r5 = 10;
                    if (r4 == r5) goto L_0x0a35;
                    r5 = r13 + -1;
                    if (r12 != r5) goto L_0x0a43;
                    r5 = "final";
                    r9 = "1";
                    r8.put(r5, r9);
                    r18 = 0;
                    goto L_0x0a43;
                    r84 = r5;
                    r4 = r22;
                    r5 = new org.telegram.messenger.SendMessagesHelper$21$5;
                    r5.<init>(r7, r2, r8);
                    org.telegram.messenger.AndroidUtilities.runOnUIThread(r5);
                    r22 = r4;
                    r3 = r18;
                    goto L_0x083e;
                    r83 = r4;
                    r84 = r5;
                    if (r58 != 0) goto L_0x0a6e;
                    r4 = new java.util.ArrayList;
                    r4.<init>();
                    r5 = new java.util.ArrayList;
                    r5.<init>();
                    r7 = r5;
                    r5 = new java.util.ArrayList;
                    r5.<init>();
                    r8 = new java.util.ArrayList;
                    r8.<init>();
                    goto L_0x0a76;
                    r4 = r58;
                    r8 = r60;
                    r5 = r61;
                    r7 = r77;
                    r4.add(r3);
                    r7.add(r1);
                    r9 = r2.caption;
                    r5.add(r9);
                    r9 = r2.entities;
                    r8.add(r9);
                    r58 = r4;
                    r2 = r8;
                    r3 = r18;
                    r1 = r12 + 1;
                    r19 = r14;
                    r8 = r34;
                    r9 = r35;
                    r11 = r53;
                    r15 = r58;
                    r14 = r6;
                    r6 = r28;
                    goto L_0x014d;
                L_0x0a9c:
                    r60 = r2;
                    r61 = r5;
                    r34 = r8;
                    r53 = r11;
                    r6 = r14;
                    r58 = r15;
                    r77 = r35;
                    r25 = 0;
                    r35 = r9;
                    r1 = 0;
                    r5 = (r3 > r1 ? 1 : (r3 == r1 ? 0 : -1));
                    if (r5 == 0) goto L_0x0abc;
                    r1 = r3;
                    r5 = new org.telegram.messenger.SendMessagesHelper$21$6;
                    r5.<init>(r1);
                    org.telegram.messenger.AndroidUtilities.runOnUIThread(r5);
                    r1 = r9;
                    if (r1 == 0) goto L_0x0ac5;
                    r1 = r9;
                    r1.releasePermission();
                    if (r58 == 0) goto L_0x0b1b;
                    r5 = r58;
                    r1 = r5.isEmpty();
                    if (r1 != 0) goto L_0x0b14;
                    r1 = r25;
                    r2 = r5.size();
                    if (r1 >= r2) goto L_0x0b14;
                    r2 = r5;
                    r7 = r5.get(r1);
                    r24 = r7;
                    r24 = (java.lang.String) r24;
                    r7 = r77;
                    r8 = r7.get(r1);
                    r25 = r8;
                    r25 = (java.lang.String) r25;
                    r26 = 0;
                    r8 = r3;
                    r11 = r8;
                    r15 = r61;
                    r12 = r15.get(r1);
                    r31 = r12;
                    r31 = (java.lang.CharSequence) r31;
                    r12 = r60;
                    r14 = r12.get(r1);
                    r32 = r14;
                    r32 = (java.util.ArrayList) r32;
                    r23 = r2;
                    r27 = r33;
                    r28 = r8;
                    r30 = r11;
                    org.telegram.messenger.SendMessagesHelper.prepareSendingDocumentInternal(r23, r24, r25, r26, r27, r28, r30, r31, r32);
                    r25 = r1 + 1;
                    goto L_0x0ad0;
                    r12 = r60;
                    r15 = r61;
                    r7 = r77;
                    goto L_0x0b23;
                    r5 = r58;
                    r12 = r60;
                    r15 = r61;
                    r7 = r77;
                    r1 = org.telegram.messenger.BuildVars.LOGS_ENABLED;
                    if (r1 == 0) goto L_0x0b44;
                    r1 = new java.lang.StringBuilder;
                    r1.<init>();
                    r2 = "total send time = ";
                    r1.append(r2);
                    r8 = java.lang.System.currentTimeMillis();
                    r85 = r3;
                    r2 = r8 - r53;
                    r1.append(r2);
                    r1 = r1.toString();
                    org.telegram.messenger.FileLog.d(r1);
                    goto L_0x0b46;
                    r85 = r3;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.SendMessagesHelper.21.run():void");
                }
            });
        }
    }

    private static Bitmap createVideoThumbnail(String filePath, long time) {
        int width;
        int height;
        int max;
        Bitmap scaled;
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            bitmap = retriever.getFrameAtTime(time, 1);
        } catch (Exception e) {
            if (bitmap == null) {
                return null;
            }
            width = bitmap.getWidth();
            height = bitmap.getHeight();
            max = Math.max(width, height);
            if (max > 90) {
                float scale = 90.0f / ((float) max);
                scaled = Bitmaps.createScaledBitmap(bitmap, Math.round(((float) width) * scale), Math.round(((float) height) * scale), true);
                if (scaled != bitmap) {
                    bitmap.recycle();
                    bitmap = scaled;
                }
            }
            return bitmap;
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException e2) {
            }
        }
        if (bitmap == null) {
            return null;
        }
        width = bitmap.getWidth();
        height = bitmap.getHeight();
        max = Math.max(width, height);
        if (max > 90) {
            float scale2 = 90.0f / ((float) max);
            scaled = Bitmaps.createScaledBitmap(bitmap, Math.round(((float) width) * scale2), Math.round(((float) height) * scale2), true);
            if (scaled != bitmap) {
                bitmap.recycle();
                bitmap = scaled;
            }
        }
        return bitmap;
    }

    public static void prepareSendingVideo(String videoPath, long estimatedSize, long duration, int width, int height, VideoEditedInfo info, long dialog_id, MessageObject reply_to_msg, CharSequence caption, ArrayList<MessageEntity> entities, int ttl) {
        if (videoPath != null) {
            if (videoPath.length() != 0) {
                final VideoEditedInfo videoEditedInfo = info;
                final String str = videoPath;
                final long j = dialog_id;
                final long j2 = duration;
                final int i = ttl;
                final int i2 = UserConfig.selectedAccount;
                final int i3 = height;
                final int i4 = width;
                final long j3 = estimatedSize;
                final CharSequence charSequence = caption;
                AnonymousClass22 anonymousClass22 = r0;
                final MessageObject messageObject = reply_to_msg;
                final ArrayList<MessageEntity> arrayList = entities;
                AnonymousClass22 anonymousClass222 = new Runnable() {
                    public void run() {
                        String str;
                        Bitmap thumb;
                        String fileName;
                        Bitmap thumb2;
                        TL_document document;
                        String path;
                        VideoEditedInfo videoEditedInfo = videoEditedInfo != null ? videoEditedInfo : SendMessagesHelper.createCompressionSettings(str);
                        boolean isEncrypted = ((int) j) == 0;
                        boolean z = videoEditedInfo != null && videoEditedInfo.roundVideo;
                        boolean isRound = z;
                        String thumbKey = null;
                        if (videoEditedInfo == null && !str.endsWith("mp4")) {
                            if (!isRound) {
                                SendMessagesHelper.prepareSendingDocumentInternal(i2, str, str, null, null, j, messageObject, charSequence, arrayList);
                                str = null;
                            }
                        }
                        String path2 = str;
                        String originalPath = str;
                        File temp = new File(originalPath);
                        long startTime = 0;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(originalPath);
                        stringBuilder.append(temp.length());
                        stringBuilder.append("_");
                        stringBuilder.append(temp.lastModified());
                        originalPath = stringBuilder.toString();
                        if (videoEditedInfo != null) {
                            if (!isRound) {
                                stringBuilder = new StringBuilder();
                                stringBuilder.append(originalPath);
                                stringBuilder.append(j2);
                                stringBuilder.append("_");
                                stringBuilder.append(videoEditedInfo.startTime);
                                stringBuilder.append("_");
                                stringBuilder.append(videoEditedInfo.endTime);
                                stringBuilder.append(videoEditedInfo.muted ? "_m" : TtmlNode.ANONYMOUS_REGION_ID);
                                originalPath = stringBuilder.toString();
                                if (videoEditedInfo.resultWidth != videoEditedInfo.originalWidth) {
                                    stringBuilder = new StringBuilder();
                                    stringBuilder.append(originalPath);
                                    stringBuilder.append("_");
                                    stringBuilder.append(videoEditedInfo.resultWidth);
                                    originalPath = stringBuilder.toString();
                                }
                            }
                            startTime = videoEditedInfo.startTime >= 0 ? videoEditedInfo.startTime : 0;
                        }
                        String originalPath2 = originalPath;
                        long startTime2 = startTime;
                        TL_document document2 = null;
                        if (!isEncrypted && i == 0) {
                            document2 = (TL_document) MessagesStorage.getInstance(i2).getSentFile(originalPath2, !isEncrypted ? 2 : 5);
                        }
                        long j;
                        if (document2 == null) {
                            thumb = SendMessagesHelper.createVideoThumbnail(str, startTime2);
                            if (thumb == null) {
                                thumb = ThumbnailUtils.createVideoThumbnail(str, 1);
                            }
                            PhotoSize size = ImageLoader.scaleAndSaveImage(thumb, 90.0f, 90.0f, 55, isEncrypted);
                            if (thumb == null || size == null) {
                                j = startTime2;
                            } else if (!isRound) {
                                j = startTime2;
                                thumb = null;
                            } else if (isEncrypted) {
                                Utilities.blurBitmap(thumb, 7, VERSION.SDK_INT < 21 ? 0 : 1, thumb.getWidth(), thumb.getHeight(), thumb.getRowBytes());
                                r2 = new StringBuilder();
                                j = startTime2;
                                r2.append(size.location.volume_id);
                                r2.append("_");
                                r2.append(size.location.local_id);
                                r2.append("@%d_%d_b2");
                                thumbKey = String.format(r2.toString(), new Object[]{Integer.valueOf((int) (((float) AndroidUtilities.roundMessageSize) / AndroidUtilities.density)), Integer.valueOf((int) (((float) AndroidUtilities.roundMessageSize) / AndroidUtilities.density))});
                            } else {
                                j = startTime2;
                                Utilities.blurBitmap(thumb, 3, VERSION.SDK_INT < 21 ? 0 : 1, thumb.getWidth(), thumb.getHeight(), thumb.getRowBytes());
                                r2 = new StringBuilder();
                                r2.append(size.location.volume_id);
                                r2.append("_");
                                r2.append(size.location.local_id);
                                r2.append("@%d_%d_b");
                                thumbKey = String.format(r2.toString(), new Object[]{Integer.valueOf((int) (((float) AndroidUtilities.roundMessageSize) / AndroidUtilities.density)), Integer.valueOf((int) (((float) AndroidUtilities.roundMessageSize) / AndroidUtilities.density))});
                            }
                            TL_document tL_document = new TL_document();
                            tL_document.thumb = size;
                            if (tL_document.thumb == null) {
                                tL_document.thumb = new TL_photoSizeEmpty();
                                tL_document.thumb.type = "s";
                            } else {
                                tL_document.thumb.type = "s";
                            }
                            tL_document.mime_type = MimeTypes.VIDEO_MP4;
                            UserConfig.getInstance(i2).saveConfig(false);
                            if (isEncrypted) {
                                EncryptedChat encryptedChat = MessagesController.getInstance(i2).getEncryptedChat(Integer.valueOf((int) (j >> 32)));
                                if (encryptedChat != null) {
                                    if (AndroidUtilities.getPeerLayerVersion(encryptedChat.layer) >= 66) {
                                        startTime2 = new TL_documentAttributeVideo();
                                    } else {
                                        startTime2 = new TL_documentAttributeVideo_layer65();
                                    }
                                    document2 = startTime2;
                                } else {
                                    return;
                                }
                            }
                            document2 = new TL_documentAttributeVideo();
                            document2.supports_streaming = true;
                            document2.round_message = isRound;
                            tL_document.attributes.add(document2);
                            if (videoEditedInfo == null || !videoEditedInfo.needConvert()) {
                                if (temp.exists()) {
                                    tL_document.size = (int) temp.length();
                                }
                                SendMessagesHelper.fillVideoAttribute(str, document2, 0);
                            } else {
                                if (videoEditedInfo.muted) {
                                    tL_document.attributes.add(new TL_documentAttributeAnimated());
                                    SendMessagesHelper.fillVideoAttribute(str, document2, videoEditedInfo);
                                    videoEditedInfo.originalWidth = document2.w;
                                    videoEditedInfo.originalHeight = document2.h;
                                    document2.w = videoEditedInfo.resultWidth;
                                    document2.h = videoEditedInfo.resultHeight;
                                } else {
                                    document2.duration = (int) (j2 / 1000);
                                    if (videoEditedInfo.rotationValue != 90) {
                                        if (videoEditedInfo.rotationValue != 270) {
                                            document2.w = i4;
                                            document2.h = i3;
                                        }
                                    }
                                    document2.w = i3;
                                    document2.h = i4;
                                }
                                tL_document.size = (int) j3;
                                fileName = new StringBuilder();
                                fileName.append("-2147483648_");
                                fileName.append(SharedConfig.getLastLocalId());
                                fileName.append(".mp4");
                                startTime2 = new File(FileLoader.getDirectory(4), fileName.toString());
                                SharedConfig.saveConfig();
                                path2 = startTime2.getAbsolutePath();
                            }
                            thumb2 = thumb;
                            document = tL_document;
                            str = thumbKey;
                            path = path2;
                        } else {
                            j = startTime2;
                            thumb2 = null;
                            str = null;
                            path = path2;
                            document = document2;
                        }
                        final TL_document videoFinal = document;
                        String originalPathFinal = originalPath2;
                        fileName = path;
                        final HashMap<String, String> params = new HashMap();
                        final Bitmap thumbFinal = thumb2;
                        thumbKey = str;
                        final String captionFinal = charSequence != null ? charSequence.toString() : TtmlNode.ANONYMOUS_REGION_ID;
                        if (originalPath2 != null) {
                            params.put("originalPath", originalPath2);
                        }
                        AnonymousClass1 anonymousClass1 = r0;
                        final VideoEditedInfo videoEditedInfo2 = videoEditedInfo;
                        AnonymousClass1 anonymousClass12 = new Runnable() {
                            public void run() {
                                if (!(thumbFinal == null || thumbKey == null)) {
                                    ImageLoader.getInstance().putImageToCache(new BitmapDrawable(thumbFinal), thumbKey);
                                }
                                SendMessagesHelper.getInstance(i2).sendMessage(videoFinal, videoEditedInfo2, fileName, j, messageObject, captionFinal, arrayList, null, params, i);
                            }
                        };
                        AndroidUtilities.runOnUIThread(anonymousClass1);
                        thumb = thumb2;
                    }
                };
                new Thread(anonymousClass22).start();
            }
        }
    }
}
