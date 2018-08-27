package org.telegram.messenger;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v13.view.inputmethod.InputContentInfoCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.util.LongSparseArray;
import android.util.SparseArray;
import android.widget.Toast;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.util.MimeTypes;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import net.hockeyapp.android.UpdateFragment;
import org.telegram.messenger.MediaController.SearchImage;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.messenger.beta.R;
import org.telegram.messenger.support.SparseLongArray;
import org.telegram.tgnet.AbstractSerializedData;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.NativeByteBuffer;
import org.telegram.tgnet.QuickAckDelegate;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.SerializedData;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.BotInlineResult;
import org.telegram.tgnet.TLRPC.Chat;
import org.telegram.tgnet.TLRPC.ChatFull;
import org.telegram.tgnet.TLRPC.ChatParticipant;
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
import org.telegram.tgnet.TLRPC.InputUser;
import org.telegram.tgnet.TLRPC.KeyboardButton;
import org.telegram.tgnet.TLRPC.Message;
import org.telegram.tgnet.TLRPC.MessageEntity;
import org.telegram.tgnet.TLRPC.MessageFwdHeader;
import org.telegram.tgnet.TLRPC.MessageMedia;
import org.telegram.tgnet.TLRPC.Peer;
import org.telegram.tgnet.TLRPC.PhotoSize;
import org.telegram.tgnet.TLRPC.ReplyMarkup;
import org.telegram.tgnet.TLRPC.TL_botInlineMediaResult;
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
import org.telegram.tgnet.TLRPC.TL_decryptedMessageMediaContact;
import org.telegram.tgnet.TLRPC.TL_decryptedMessageMediaDocument;
import org.telegram.tgnet.TLRPC.TL_decryptedMessageMediaEmpty;
import org.telegram.tgnet.TLRPC.TL_decryptedMessageMediaExternalDocument;
import org.telegram.tgnet.TLRPC.TL_decryptedMessageMediaGeoPoint;
import org.telegram.tgnet.TLRPC.TL_decryptedMessageMediaPhoto;
import org.telegram.tgnet.TLRPC.TL_decryptedMessageMediaVenue;
import org.telegram.tgnet.TLRPC.TL_decryptedMessageMediaVideo;
import org.telegram.tgnet.TLRPC.TL_decryptedMessageMediaWebPage;
import org.telegram.tgnet.TLRPC.TL_decryptedMessage_layer45;
import org.telegram.tgnet.TLRPC.TL_document;
import org.telegram.tgnet.TLRPC.TL_documentAttributeAnimated;
import org.telegram.tgnet.TLRPC.TL_documentAttributeAudio;
import org.telegram.tgnet.TLRPC.TL_documentAttributeFilename;
import org.telegram.tgnet.TLRPC.TL_documentAttributeImageSize;
import org.telegram.tgnet.TLRPC.TL_documentAttributeSticker;
import org.telegram.tgnet.TLRPC.TL_documentAttributeSticker_layer55;
import org.telegram.tgnet.TLRPC.TL_documentAttributeVideo;
import org.telegram.tgnet.TLRPC.TL_documentAttributeVideo_layer65;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.tgnet.TLRPC.TL_fileLocationUnavailable;
import org.telegram.tgnet.TLRPC.TL_game;
import org.telegram.tgnet.TLRPC.TL_geoPoint;
import org.telegram.tgnet.TLRPC.TL_inputDocument;
import org.telegram.tgnet.TLRPC.TL_inputEncryptedFile;
import org.telegram.tgnet.TLRPC.TL_inputGeoPoint;
import org.telegram.tgnet.TLRPC.TL_inputMediaContact;
import org.telegram.tgnet.TLRPC.TL_inputMediaDocument;
import org.telegram.tgnet.TLRPC.TL_inputMediaEmpty;
import org.telegram.tgnet.TLRPC.TL_inputMediaGame;
import org.telegram.tgnet.TLRPC.TL_inputMediaGeoLive;
import org.telegram.tgnet.TLRPC.TL_inputMediaGeoPoint;
import org.telegram.tgnet.TLRPC.TL_inputMediaGifExternal;
import org.telegram.tgnet.TLRPC.TL_inputMediaPhoto;
import org.telegram.tgnet.TLRPC.TL_inputMediaUploadedDocument;
import org.telegram.tgnet.TLRPC.TL_inputMediaUploadedPhoto;
import org.telegram.tgnet.TLRPC.TL_inputMediaVenue;
import org.telegram.tgnet.TLRPC.TL_inputPeerChannel;
import org.telegram.tgnet.TLRPC.TL_inputPeerEmpty;
import org.telegram.tgnet.TLRPC.TL_inputPeerUser;
import org.telegram.tgnet.TLRPC.TL_inputPhoto;
import org.telegram.tgnet.TLRPC.TL_inputSingleMedia;
import org.telegram.tgnet.TLRPC.TL_inputStickerSetEmpty;
import org.telegram.tgnet.TLRPC.TL_inputStickerSetShortName;
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
import org.telegram.tgnet.TLRPC.TL_message_secret;
import org.telegram.tgnet.TLRPC.TL_messages_botCallbackAnswer;
import org.telegram.tgnet.TLRPC.TL_messages_editMessage;
import org.telegram.tgnet.TLRPC.TL_messages_forwardMessages;
import org.telegram.tgnet.TLRPC.TL_messages_getBotCallbackAnswer;
import org.telegram.tgnet.TLRPC.TL_messages_messages;
import org.telegram.tgnet.TLRPC.TL_messages_sendBroadcast;
import org.telegram.tgnet.TLRPC.TL_messages_sendEncryptedMultiMedia;
import org.telegram.tgnet.TLRPC.TL_messages_sendInlineBotResult;
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
import org.telegram.tgnet.TLRPC.TL_peerChat;
import org.telegram.tgnet.TLRPC.TL_peerUser;
import org.telegram.tgnet.TLRPC.TL_photo;
import org.telegram.tgnet.TLRPC.TL_photoCachedSize;
import org.telegram.tgnet.TLRPC.TL_photoSize;
import org.telegram.tgnet.TLRPC.TL_photoSizeEmpty;
import org.telegram.tgnet.TLRPC.TL_updateEditChannelMessage;
import org.telegram.tgnet.TLRPC.TL_updateEditMessage;
import org.telegram.tgnet.TLRPC.TL_updateMessageID;
import org.telegram.tgnet.TLRPC.TL_updateNewChannelMessage;
import org.telegram.tgnet.TLRPC.TL_updateNewMessage;
import org.telegram.tgnet.TLRPC.TL_updateShortSentMessage;
import org.telegram.tgnet.TLRPC.TL_user;
import org.telegram.tgnet.TLRPC.TL_userContact_old2;
import org.telegram.tgnet.TLRPC.TL_userRequest_old2;
import org.telegram.tgnet.TLRPC.TL_webPagePending;
import org.telegram.tgnet.TLRPC.TL_webPageUrlPending;
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
            if (this.requests == null) {
                return;
            }
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

        public void markAsError() {
            if (this.type == 4) {
                for (int a = 0; a < this.messageObjects.size(); a++) {
                    MessageObject obj = (MessageObject) this.messageObjects.get(a);
                    MessagesStorage.getInstance(SendMessagesHelper.this.currentAccount).markMessageAsSendError(obj.messageOwner);
                    obj.messageOwner.send_state = 2;
                    NotificationCenter.getInstance(SendMessagesHelper.this.currentAccount).postNotificationName(NotificationCenter.messageSendError, Integer.valueOf(obj.getId()));
                    SendMessagesHelper.this.processSentMessage(obj.getId());
                }
                SendMessagesHelper.this.delayedMessages.remove("group_" + this.groupId);
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
                if (location != null && LocationProvider.this.locationQueryCancelRunnable != null) {
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("found location " + location);
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

    static {
        int cores;
        if (VERSION.SDK_INT >= 17) {
            cores = Runtime.getRuntime().availableProcessors();
        } else {
            cores = 2;
        }
        mediaSendThreadPool = new ThreadPoolExecutor(cores, cores, 60, TimeUnit.SECONDS, new LinkedBlockingQueue());
    }

    public static SendMessagesHelper getInstance(int num) {
        SendMessagesHelper localInstance = Instance[num];
        if (localInstance == null) {
            synchronized (SendMessagesHelper.class) {
                try {
                    localInstance = Instance[num];
                    if (localInstance == null) {
                        SendMessagesHelper[] sendMessagesHelperArr = Instance;
                        SendMessagesHelper localInstance2 = new SendMessagesHelper(num);
                        try {
                            sendMessagesHelperArr[num] = localInstance2;
                            localInstance = localInstance2;
                        } catch (Throwable th) {
                            Throwable th2 = th;
                            localInstance = localInstance2;
                            throw th2;
                        }
                    }
                } catch (Throwable th3) {
                    th2 = th3;
                    throw th2;
                }
            }
        }
        return localInstance;
    }

    public SendMessagesHelper(int instance) {
        this.currentAccount = instance;
        AndroidUtilities.runOnUIThread(new SendMessagesHelper$$Lambda$0(this));
    }

    final /* synthetic */ void lambda$new$0$SendMessagesHelper() {
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.FileDidUpload);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.FileDidFailUpload);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.FilePreparingStarted);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.FileNewChunkAvailable);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.FilePreparingFailed);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.httpFileDidFailedLoad);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.httpFileDidLoaded);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.FileDidLoaded);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.FileDidFailedLoad);
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
        String location;
        ArrayList<DelayedMessage> arr;
        int a;
        DelayedMessage message;
        int index;
        MessageObject messageObject;
        if (id == NotificationCenter.FileDidUpload) {
            location = args[0];
            InputFile file = args[1];
            InputEncryptedFile encryptedFile = args[2];
            arr = (ArrayList) this.delayedMessages.get(location);
            if (arr != null) {
                a = 0;
                while (a < arr.size()) {
                    message = (DelayedMessage) arr.get(a);
                    InputMedia media = null;
                    if (message.sendRequest instanceof TL_messages_sendMedia) {
                        media = ((TL_messages_sendMedia) message.sendRequest).media;
                    } else if (message.sendRequest instanceof TL_messages_editMessage) {
                        media = ((TL_messages_editMessage) message.sendRequest).media;
                    } else if (message.sendRequest instanceof TL_messages_sendBroadcast) {
                        media = ((TL_messages_sendBroadcast) message.sendRequest).media;
                    } else if (message.sendRequest instanceof TL_messages_sendMultiMedia) {
                        media = (InputMedia) message.extraHashMap.get(location);
                    }
                    if (file != null && media != null) {
                        if (message.type == 0) {
                            media.file = file;
                            performSendMessageRequest(message.sendRequest, message.obj, message.originalPath, message, true);
                        } else if (message.type == 1) {
                            if (media.file == null) {
                                media.file = file;
                                if (media.thumb != null || message.location == null) {
                                    performSendMessageRequest(message.sendRequest, message.obj, message.originalPath);
                                } else {
                                    performSendDelayedMessage(message);
                                }
                            } else {
                                media.thumb = file;
                                media.flags |= 4;
                                performSendMessageRequest(message.sendRequest, message.obj, message.originalPath);
                            }
                        } else if (message.type == 2) {
                            if (media.file == null) {
                                media.file = file;
                                if (media.thumb != null || message.location == null) {
                                    performSendMessageRequest(message.sendRequest, message.obj, message.originalPath);
                                } else {
                                    performSendDelayedMessage(message);
                                }
                            } else {
                                media.thumb = file;
                                media.flags |= 4;
                                performSendMessageRequest(message.sendRequest, message.obj, message.originalPath);
                            }
                        } else if (message.type == 3) {
                            media.file = file;
                            performSendMessageRequest(message.sendRequest, message.obj, message.originalPath);
                        } else if (message.type == 4) {
                            if (!(media instanceof TL_inputMediaUploadedDocument)) {
                                media.file = file;
                                uploadMultiMedia(message, media, null, location);
                            } else if (media.file == null) {
                                media.file = file;
                                index = message.messageObjects.indexOf((MessageObject) message.extraHashMap.get(location + "_i"));
                                message.location = (FileLocation) message.extraHashMap.get(location + "_t");
                                stopVideoService(((MessageObject) message.messageObjects.get(index)).messageOwner.attachPath);
                                if (media.thumb != null || message.location == null) {
                                    uploadMultiMedia(message, media, null, location);
                                } else {
                                    performSendDelayedMessage(message, index);
                                }
                            } else {
                                media.thumb = file;
                                media.flags |= 4;
                                uploadMultiMedia(message, media, null, (String) message.extraHashMap.get(location + "_o"));
                            }
                        }
                        arr.remove(a);
                        a--;
                    } else if (!(encryptedFile == null || message.sendEncryptedRequest == null)) {
                        TL_decryptedMessage decryptedMessage = null;
                        if (message.type == 4) {
                            TL_messages_sendEncryptedMultiMedia req = (TL_messages_sendEncryptedMultiMedia) message.sendEncryptedRequest;
                            InputEncryptedFile inputEncryptedFile = (InputEncryptedFile) message.extraHashMap.get(location);
                            index = req.files.indexOf(inputEncryptedFile);
                            if (index >= 0) {
                                req.files.set(index, encryptedFile);
                                if (inputEncryptedFile.id == 1) {
                                    messageObject = (MessageObject) message.extraHashMap.get(location + "_i");
                                    message.location = (FileLocation) message.extraHashMap.get(location + "_t");
                                    stopVideoService(((MessageObject) message.messageObjects.get(index)).messageOwner.attachPath);
                                }
                                decryptedMessage = (TL_decryptedMessage) req.messages.get(index);
                            }
                        } else {
                            decryptedMessage = message.sendEncryptedRequest;
                        }
                        if (decryptedMessage != null) {
                            if ((decryptedMessage.media instanceof TL_decryptedMessageMediaVideo) || (decryptedMessage.media instanceof TL_decryptedMessageMediaPhoto) || (decryptedMessage.media instanceof TL_decryptedMessageMediaDocument)) {
                                decryptedMessage.media.size = (int) ((Long) args[5]).longValue();
                            }
                            decryptedMessage.media.key = (byte[]) args[3];
                            decryptedMessage.media.iv = (byte[]) args[4];
                            if (message.type == 4) {
                                uploadMultiMedia(message, null, encryptedFile, location);
                            } else {
                                SecretChatHelper.getInstance(this.currentAccount).performSendEncryptedRequest(decryptedMessage, message.obj.messageOwner, message.encryptedChat, encryptedFile, message.originalPath, message.obj);
                            }
                        }
                        arr.remove(a);
                        a--;
                    }
                    a++;
                }
                if (arr.isEmpty()) {
                    this.delayedMessages.remove(location);
                }
            }
        } else if (id == NotificationCenter.FileDidFailUpload) {
            location = (String) args[0];
            boolean enc = ((Boolean) args[1]).booleanValue();
            arr = (ArrayList) this.delayedMessages.get(location);
            if (arr != null) {
                a = 0;
                while (a < arr.size()) {
                    DelayedMessage obj = (DelayedMessage) arr.get(a);
                    if ((enc && obj.sendEncryptedRequest != null) || !(enc || obj.sendRequest == null)) {
                        obj.markAsError();
                        arr.remove(a);
                        a--;
                    }
                    a++;
                }
                if (arr.isEmpty()) {
                    this.delayedMessages.remove(location);
                }
            }
        } else if (id == NotificationCenter.FilePreparingStarted) {
            messageObject = (MessageObject) args[0];
            if (messageObject.getId() != 0) {
                finalPath = args[1];
                arr = (ArrayList) this.delayedMessages.get(messageObject.messageOwner.attachPath);
                if (arr != null) {
                    a = 0;
                    while (a < arr.size()) {
                        message = (DelayedMessage) arr.get(a);
                        if (message.type == 4) {
                            index = message.messageObjects.indexOf(messageObject);
                            message.location = (FileLocation) message.extraHashMap.get(messageObject.messageOwner.attachPath + "_t");
                            performSendDelayedMessage(message, index);
                            arr.remove(a);
                            break;
                        } else if (message.obj == messageObject) {
                            message.videoEditedInfo = null;
                            performSendDelayedMessage(message);
                            arr.remove(a);
                            break;
                        } else {
                            a++;
                        }
                    }
                    if (arr.isEmpty()) {
                        this.delayedMessages.remove(messageObject.messageOwner.attachPath);
                    }
                }
            }
        } else if (id == NotificationCenter.FileNewChunkAvailable) {
            messageObject = (MessageObject) args[0];
            if (messageObject.getId() != 0) {
                finalPath = (String) args[1];
                long availableSize = ((Long) args[2]).longValue();
                long finalSize = ((Long) args[3]).longValue();
                FileLoader.getInstance(this.currentAccount).checkUploadNewDataAvailable(finalPath, ((int) messageObject.getDialogId()) == 0, availableSize, finalSize);
                if (finalSize != 0) {
                    arr = (ArrayList) this.delayedMessages.get(messageObject.messageOwner.attachPath);
                    if (arr != null) {
                        for (a = 0; a < arr.size(); a++) {
                            message = (DelayedMessage) arr.get(a);
                            ArrayList messages;
                            if (message.type == 4) {
                                for (b = 0; b < message.messageObjects.size(); b++) {
                                    MessageObject obj2 = (MessageObject) message.messageObjects.get(b);
                                    if (obj2 == messageObject) {
                                        obj2.videoEditedInfo = null;
                                        obj2.messageOwner.params.remove("ve");
                                        obj2.messageOwner.media.document.size = (int) finalSize;
                                        messages = new ArrayList();
                                        messages.add(obj2.messageOwner);
                                        MessagesStorage.getInstance(this.currentAccount).putMessages(messages, false, true, false, 0);
                                        break;
                                    }
                                }
                            } else if (message.obj == messageObject) {
                                message.obj.videoEditedInfo = null;
                                message.obj.messageOwner.params.remove("ve");
                                message.obj.messageOwner.media.document.size = (int) finalSize;
                                messages = new ArrayList();
                                messages.add(message.obj.messageOwner);
                                MessagesStorage.getInstance(this.currentAccount).putMessages(messages, false, true, false, 0);
                                return;
                            }
                        }
                    }
                }
            }
        } else if (id == NotificationCenter.FilePreparingFailed) {
            messageObject = (MessageObject) args[0];
            if (messageObject.getId() != 0) {
                finalPath = (String) args[1];
                stopVideoService(messageObject.messageOwner.attachPath);
                arr = (ArrayList) this.delayedMessages.get(finalPath);
                if (arr != null) {
                    a = 0;
                    while (a < arr.size()) {
                        message = (DelayedMessage) arr.get(a);
                        if (message.type == 4) {
                            for (b = 0; b < message.messages.size(); b++) {
                                if (message.messageObjects.get(b) == messageObject) {
                                    message.markAsError();
                                    arr.remove(a);
                                    a--;
                                    break;
                                }
                            }
                        } else if (message.obj == messageObject) {
                            message.markAsError();
                            arr.remove(a);
                            a--;
                        }
                        a++;
                    }
                    if (arr.isEmpty()) {
                        this.delayedMessages.remove(finalPath);
                    }
                }
            }
        } else if (id == NotificationCenter.httpFileDidLoaded) {
            path = args[0];
            arr = (ArrayList) this.delayedMessages.get(path);
            if (arr != null) {
                for (a = 0; a < arr.size(); a++) {
                    message = (DelayedMessage) arr.get(a);
                    int fileType = -1;
                    if (message.type == 0) {
                        fileType = 0;
                        messageObject = message.obj;
                    } else if (message.type == 2) {
                        fileType = 1;
                        messageObject = message.obj;
                    } else if (message.type == 4) {
                        messageObject = (MessageObject) message.extraHashMap.get(path);
                        if (messageObject.getDocument() != null) {
                            fileType = 1;
                        } else {
                            fileType = 0;
                        }
                    } else {
                        messageObject = null;
                    }
                    if (fileType == 0) {
                        Utilities.globalQueue.postRunnable(new SendMessagesHelper$$Lambda$1(this, new File(FileLoader.getDirectory(4), Utilities.MD5(path) + "." + ImageLoader.getHttpUrlExtension(path, "file")), messageObject, message, path));
                    } else if (fileType == 1) {
                        Utilities.globalQueue.postRunnable(new SendMessagesHelper$$Lambda$2(this, message, new File(FileLoader.getDirectory(4), Utilities.MD5(path) + ".gif"), messageObject));
                    }
                }
                this.delayedMessages.remove(path);
            }
        } else if (id == NotificationCenter.FileDidLoaded) {
            path = (String) args[0];
            arr = (ArrayList) this.delayedMessages.get(path);
            if (arr != null) {
                for (a = 0; a < arr.size(); a++) {
                    performSendDelayedMessage((DelayedMessage) arr.get(a));
                }
                this.delayedMessages.remove(path);
            }
        } else if (id == NotificationCenter.httpFileDidFailedLoad || id == NotificationCenter.FileDidFailedLoad) {
            path = (String) args[0];
            arr = (ArrayList) this.delayedMessages.get(path);
            if (arr != null) {
                for (a = 0; a < arr.size(); a++) {
                    ((DelayedMessage) arr.get(a)).markAsError();
                }
                this.delayedMessages.remove(path);
            }
        }
    }

    final /* synthetic */ void lambda$didReceivedNotification$2$SendMessagesHelper(File cacheFile, MessageObject messageObject, DelayedMessage message, String path) {
        AndroidUtilities.runOnUIThread(new SendMessagesHelper$$Lambda$61(this, generatePhotoSizes(cacheFile.toString(), null), messageObject, cacheFile, message, path));
    }

    final /* synthetic */ void lambda$null$1$SendMessagesHelper(TL_photo photo, MessageObject messageObject, File cacheFile, DelayedMessage message, String path) {
        if (photo != null) {
            messageObject.messageOwner.media.photo = photo;
            messageObject.messageOwner.attachPath = cacheFile.toString();
            ArrayList messages = new ArrayList();
            messages.add(messageObject.messageOwner);
            MessagesStorage.getInstance(this.currentAccount).putMessages(messages, false, true, false, 0);
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.updateMessageMedia, messageObject.messageOwner);
            message.location = ((PhotoSize) photo.sizes.get(photo.sizes.size() - 1)).location;
            message.httpLocation = null;
            if (message.type == 4) {
                performSendDelayedMessage(message, message.messageObjects.indexOf(messageObject));
                return;
            } else {
                performSendDelayedMessage(message);
                return;
            }
        }
        if (BuildVars.LOGS_ENABLED) {
            FileLog.e("can't load image " + path + " to file " + cacheFile.toString());
        }
        message.markAsError();
    }

    final /* synthetic */ void lambda$didReceivedNotification$4$SendMessagesHelper(DelayedMessage message, File cacheFile, MessageObject messageObject) {
        boolean z = true;
        Document document = message.obj.getDocument();
        if (document.thumb.location instanceof TL_fileLocationUnavailable) {
            try {
                Bitmap bitmap = ImageLoader.loadBitmap(cacheFile.getAbsolutePath(), null, 90.0f, 90.0f, true);
                if (bitmap != null) {
                    if (message.sendEncryptedRequest == null) {
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
        AndroidUtilities.runOnUIThread(new SendMessagesHelper$$Lambda$60(this, message, cacheFile, document, messageObject));
    }

    final /* synthetic */ void lambda$null$3$SendMessagesHelper(DelayedMessage message, File cacheFile, Document document, MessageObject messageObject) {
        message.httpLocation = null;
        message.obj.messageOwner.attachPath = cacheFile.toString();
        message.location = document.thumb.location;
        ArrayList messages = new ArrayList();
        messages.add(messageObject.messageOwner);
        MessagesStorage.getInstance(this.currentAccount).putMessages(messages, false, true, false, 0);
        performSendDelayedMessage(message);
        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.updateMessageMedia, message.obj.messageOwner);
    }

    private void revertEditingMessageObject(MessageObject object) {
        object.cancelEditing = true;
        object.messageOwner.media = object.previousMedia;
        object.messageOwner.message = object.previousCaption;
        object.messageOwner.entities = object.previousCaptionEntities;
        object.messageOwner.attachPath = object.previousAttachPath;
        object.messageOwner.send_state = 0;
        object.previousMedia = null;
        object.previousCaption = null;
        object.previousCaptionEntities = null;
        object.previousAttachPath = null;
        object.videoEditedInfo = null;
        object.type = -1;
        object.setType();
        object.caption = null;
        object.generateCaption();
        ArrayList arr = new ArrayList();
        arr.add(object.messageOwner);
        MessagesStorage.getInstance(this.currentAccount).putMessages(arr, false, true, false, 0);
        new ArrayList().add(object);
        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.replaceMessagesObjects, Long.valueOf(object.getDialogId()), arrayList);
    }

    public void cancelSendingMessage(MessageObject object) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(object);
        cancelSendingMessage(arrayList);
    }

    public void cancelSendingMessage(ArrayList<MessageObject> objects) {
        ArrayList<String> keysToRemove = new ArrayList();
        ArrayList<Integer> messageIds = new ArrayList();
        boolean enc = false;
        int channelId = 0;
        int c = 0;
        while (c < objects.size()) {
            MessageObject object = (MessageObject) objects.get(c);
            messageIds.add(Integer.valueOf(object.getId()));
            int channelId2 = object.messageOwner.to_id.channel_id;
            Message sendingMessage = removeFromSendingMessages(object.getId());
            if (sendingMessage != null) {
                ConnectionsManager.getInstance(this.currentAccount).cancelRequest(sendingMessage.reqId, true);
            }
            for (Entry<String, ArrayList<DelayedMessage>> entry : this.delayedMessages.entrySet()) {
                ArrayList<DelayedMessage> messages = (ArrayList) entry.getValue();
                int a = 0;
                while (a < messages.size()) {
                    DelayedMessage message = (DelayedMessage) messages.get(a);
                    if (message.type == 4) {
                        int index = -1;
                        MessageObject messageObject = null;
                        for (int b = 0; b < message.messageObjects.size(); b++) {
                            messageObject = (MessageObject) message.messageObjects.get(b);
                            if (messageObject.getId() == object.getId()) {
                                index = b;
                                break;
                            }
                        }
                        if (index >= 0) {
                            message.messageObjects.remove(index);
                            message.messages.remove(index);
                            message.originalPaths.remove(index);
                            if (message.sendRequest != null) {
                                ((TL_messages_sendMultiMedia) message.sendRequest).multi_media.remove(index);
                            } else {
                                TL_messages_sendEncryptedMultiMedia request = (TL_messages_sendEncryptedMultiMedia) message.sendEncryptedRequest;
                                request.messages.remove(index);
                                request.files.remove(index);
                            }
                            MediaController.getInstance().cancelVideoConvert(object);
                            String keyToRemove = (String) message.extraHashMap.get(messageObject);
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
                                    MessagesStorage.getInstance(this.currentAccount).putMessages(messagesRes, message.peer, -2, 0, false);
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
            c++;
            channelId = channelId2;
        }
        for (a = 0; a < keysToRemove.size(); a++) {
            String key = (String) keysToRemove.get(a);
            if (key.startsWith("http")) {
                ImageLoader.getInstance().cancelLoadHttpFile(key);
            } else {
                FileLoader.getInstance(this.currentAccount).cancelUploadFile(key, enc);
            }
            stopVideoService(key);
            this.delayedMessages.remove(key);
        }
        if (objects.size() == 1 && ((MessageObject) objects.get(0)).isEditing() && ((MessageObject) objects.get(0)).previousMedia != null) {
            revertEditingMessageObject((MessageObject) objects.get(0));
        } else {
            MessagesController.getInstance(this.currentAccount).deleteMessages(messageIds, null, null, channelId, false);
        }
    }

    public boolean retrySendMessage(MessageObject messageObject, boolean unsent) {
        if (messageObject.getId() >= 0) {
            if (messageObject.isEditing()) {
                editMessageMedia(messageObject, null, null, null, null, null, true);
            }
            return false;
        } else if (messageObject.messageOwner.action instanceof TL_messageEncryptedAction) {
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
            } else if (!((messageObject.messageOwner.action.encryptedAction instanceof TL_decryptedMessageActionTyping) || (messageObject.messageOwner.action.encryptedAction instanceof TL_decryptedMessageActionResend))) {
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
            return true;
        } else {
            if (messageObject.messageOwner.action instanceof TL_messageActionScreenshotTaken) {
                sendScreenshotMessage(MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf((int) messageObject.getDialogId())), messageObject.messageOwner.reply_to_msg_id, messageObject.messageOwner);
            }
            if (unsent) {
                this.unsentMessages.put(messageObject.getId(), messageObject);
            }
            sendMessage(messageObject);
            return true;
        }
    }

    protected void processSentMessage(int id) {
        int prevSize = this.unsentMessages.size();
        this.unsentMessages.remove(id);
        if (prevSize != 0 && this.unsentMessages.size() == 0) {
            checkUnsentMessages();
        }
    }

    public void processForwardFromMyName(MessageObject messageObject, long did) {
        if (messageObject != null) {
            ArrayList<MessageObject> arrayList;
            if (messageObject.messageOwner.media == null || (messageObject.messageOwner.media instanceof TL_messageMediaEmpty) || (messageObject.messageOwner.media instanceof TL_messageMediaWebPage) || (messageObject.messageOwner.media instanceof TL_messageMediaGame) || (messageObject.messageOwner.media instanceof TL_messageMediaInvoice)) {
                if (messageObject.messageOwner.message != null) {
                    ArrayList<MessageEntity> entities;
                    WebPage webPage = null;
                    if (messageObject.messageOwner.media instanceof TL_messageMediaWebPage) {
                        webPage = messageObject.messageOwner.media.webpage;
                    }
                    if (messageObject.messageOwner.entities == null || messageObject.messageOwner.entities.isEmpty()) {
                        entities = null;
                    } else {
                        entities = new ArrayList();
                        for (int a = 0; a < messageObject.messageOwner.entities.size(); a++) {
                            MessageEntity entity = (MessageEntity) messageObject.messageOwner.entities.get(a);
                            if ((entity instanceof TL_messageEntityBold) || (entity instanceof TL_messageEntityItalic) || (entity instanceof TL_messageEntityPre) || (entity instanceof TL_messageEntityCode) || (entity instanceof TL_messageEntityTextUrl)) {
                                entities.add(entity);
                            }
                        }
                    }
                    sendMessage(messageObject.messageOwner.message, did, messageObject.replyMessageObject, webPage, true, entities, null, null);
                } else if (((int) did) != 0) {
                    arrayList = new ArrayList();
                    arrayList.add(messageObject);
                    sendMessage(arrayList, did);
                }
            } else if (messageObject.messageOwner.media.photo instanceof TL_photo) {
                sendMessage((TL_photo) messageObject.messageOwner.media.photo, null, did, messageObject.replyMessageObject, messageObject.messageOwner.message, messageObject.messageOwner.entities, null, null, messageObject.messageOwner.media.ttl_seconds);
            } else if (messageObject.messageOwner.media.document instanceof TL_document) {
                sendMessage((TL_document) messageObject.messageOwner.media.document, null, messageObject.messageOwner.attachPath, did, messageObject.replyMessageObject, messageObject.messageOwner.message, messageObject.messageOwner.entities, null, null, messageObject.messageOwner.media.ttl_seconds);
            } else if ((messageObject.messageOwner.media instanceof TL_messageMediaVenue) || (messageObject.messageOwner.media instanceof TL_messageMediaGeo)) {
                sendMessage(messageObject.messageOwner.media, did, messageObject.replyMessageObject, null, null);
            } else if (messageObject.messageOwner.media.phone_number != null) {
                User user = new TL_userContact_old2();
                user.phone = messageObject.messageOwner.media.phone_number;
                user.first_name = messageObject.messageOwner.media.first_name;
                user.last_name = messageObject.messageOwner.media.last_name;
                user.id = messageObject.messageOwner.media.user_id;
                sendMessage(user, did, messageObject.replyMessageObject, null, null);
            } else if (((int) did) != 0) {
                arrayList = new ArrayList();
                arrayList.add(messageObject);
                sendMessage(arrayList, did);
            }
        }
    }

    public void sendScreenshotMessage(User user, int messageId, Message resendMessage) {
        if (user != null && messageId != 0 && user.id != UserConfig.getInstance(this.currentAccount).getClientUserId()) {
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
            ArrayList arr = new ArrayList();
            arr.add(message);
            MessagesStorage.getInstance(this.currentAccount).putMessages(arr, false, true, false, 0);
            performSendMessageRequest(req, newMsgObj, null);
        }
    }

    public void sendSticker(Document document, long peer, MessageObject replyingMessageObject) {
        if (document != null) {
            if (((int) peer) == 0) {
                if (MessagesController.getInstance(this.currentAccount).getEncryptedChat(Integer.valueOf((int) (peer >> 32))) != null) {
                    Document newDocument = new TL_document();
                    newDocument.id = document.id;
                    newDocument.access_hash = document.access_hash;
                    newDocument.date = document.date;
                    newDocument.mime_type = document.mime_type;
                    newDocument.size = document.size;
                    newDocument.dc_id = document.dc_id;
                    newDocument.attributes = new ArrayList(document.attributes);
                    if (newDocument.mime_type == null) {
                        newDocument.mime_type = TtmlNode.ANONYMOUS_REGION_ID;
                    }
                    if (document.thumb instanceof TL_photoSize) {
                        File file = FileLoader.getPathToAttach(document.thumb, true);
                        if (file.exists()) {
                            try {
                                int len = (int) file.length();
                                byte[] arr = new byte[((int) file.length())];
                                new RandomAccessFile(file, "r").readFully(arr);
                                newDocument.thumb = new TL_photoCachedSize();
                                newDocument.thumb.location = document.thumb.location;
                                newDocument.thumb.size = document.thumb.size;
                                newDocument.thumb.w = document.thumb.w;
                                newDocument.thumb.h = document.thumb.h;
                                newDocument.thumb.type = document.thumb.type;
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
                    document = newDocument;
                } else {
                    return;
                }
            }
            if (document instanceof TL_document) {
                sendMessage((TL_document) document, null, null, peer, replyingMessageObject, null, null, null, null, 0);
            }
        }
    }

    public int sendMessage(ArrayList<MessageObject> messages, long peer) {
        if (messages == null || messages.isEmpty()) {
            return 0;
        }
        int lower_id = (int) peer;
        int sendResult = 0;
        int a;
        if (lower_id != 0) {
            Chat chat;
            Peer to_id = MessagesController.getInstance(this.currentAccount).getPeer((int) peer);
            boolean isMegagroup = false;
            boolean isSignature = false;
            boolean canSendStickers = true;
            boolean canSendMedia = true;
            boolean canSendPreview = true;
            if (lower_id <= 0) {
                chat = MessagesController.getInstance(this.currentAccount).getChat(Integer.valueOf(-lower_id));
                if (ChatObject.isChannel(chat)) {
                    isMegagroup = chat.megagroup;
                    isSignature = chat.signatures;
                    if (chat.banned_rights != null) {
                        canSendStickers = !chat.banned_rights.send_stickers;
                        canSendMedia = !chat.banned_rights.send_media;
                        canSendPreview = !chat.banned_rights.embed_links;
                    }
                }
            } else if (MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(lower_id)) == null) {
                return 0;
            }
            LongSparseArray<Long> groupsMap = new LongSparseArray();
            ArrayList<MessageObject> objArr = new ArrayList();
            ArrayList<Message> arr = new ArrayList();
            ArrayList<Long> randomIds = new ArrayList();
            ArrayList<Integer> ids = new ArrayList();
            LongSparseArray<Message> messagesByRandomIds = new LongSparseArray();
            InputPeer inputPeer = MessagesController.getInstance(this.currentAccount).getInputPeer(lower_id);
            int myId = UserConfig.getInstance(this.currentAccount).getClientUserId();
            boolean toMyself = peer == ((long) myId);
            a = 0;
            while (a < messages.size()) {
                MessageObject msgObj = (MessageObject) messages.get(a);
                if (msgObj.getId() > 0 && !msgObj.needDrawBluredPreview()) {
                    if (canSendStickers || !(msgObj.isSticker() || msgObj.isGif() || msgObj.isGame())) {
                        if (canSendMedia || !((msgObj.messageOwner.media instanceof TL_messageMediaPhoto) || (msgObj.messageOwner.media instanceof TL_messageMediaDocument))) {
                            MessageFwdHeader messageFwdHeader;
                            boolean groupedIdChanged = false;
                            Message newMsg = new TL_message();
                            boolean forwardFromSaved = msgObj.getDialogId() == ((long) myId) && msgObj.messageOwner.from_id == UserConfig.getInstance(this.currentAccount).getClientUserId();
                            if (msgObj.isForwarded()) {
                                newMsg.fwd_from = new TL_messageFwdHeader();
                                newMsg.fwd_from.flags = msgObj.messageOwner.fwd_from.flags;
                                newMsg.fwd_from.from_id = msgObj.messageOwner.fwd_from.from_id;
                                newMsg.fwd_from.date = msgObj.messageOwner.fwd_from.date;
                                newMsg.fwd_from.channel_id = msgObj.messageOwner.fwd_from.channel_id;
                                newMsg.fwd_from.channel_post = msgObj.messageOwner.fwd_from.channel_post;
                                newMsg.fwd_from.post_author = msgObj.messageOwner.fwd_from.post_author;
                                newMsg.flags = 4;
                            } else if (!forwardFromSaved) {
                                newMsg.fwd_from = new TL_messageFwdHeader();
                                newMsg.fwd_from.channel_post = msgObj.getId();
                                messageFwdHeader = newMsg.fwd_from;
                                messageFwdHeader.flags |= 4;
                                if (msgObj.isFromUser()) {
                                    newMsg.fwd_from.from_id = msgObj.messageOwner.from_id;
                                    messageFwdHeader = newMsg.fwd_from;
                                    messageFwdHeader.flags |= 1;
                                } else {
                                    newMsg.fwd_from.channel_id = msgObj.messageOwner.to_id.channel_id;
                                    messageFwdHeader = newMsg.fwd_from;
                                    messageFwdHeader.flags |= 2;
                                    if (msgObj.messageOwner.post && msgObj.messageOwner.from_id > 0) {
                                        newMsg.fwd_from.from_id = msgObj.messageOwner.from_id;
                                        messageFwdHeader = newMsg.fwd_from;
                                        messageFwdHeader.flags |= 1;
                                    }
                                }
                                if (msgObj.messageOwner.post_author != null) {
                                    newMsg.fwd_from.post_author = msgObj.messageOwner.post_author;
                                    messageFwdHeader = newMsg.fwd_from;
                                    messageFwdHeader.flags |= 8;
                                } else if (!msgObj.isOutOwner() && msgObj.messageOwner.from_id > 0 && msgObj.messageOwner.post) {
                                    User signUser = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(msgObj.messageOwner.from_id));
                                    if (signUser != null) {
                                        newMsg.fwd_from.post_author = ContactsController.formatName(signUser.first_name, signUser.last_name);
                                        messageFwdHeader = newMsg.fwd_from;
                                        messageFwdHeader.flags |= 8;
                                    }
                                }
                                newMsg.date = msgObj.messageOwner.date;
                                newMsg.flags = 4;
                            }
                            if (peer == ((long) myId) && newMsg.fwd_from != null) {
                                messageFwdHeader = newMsg.fwd_from;
                                messageFwdHeader.flags |= 16;
                                newMsg.fwd_from.saved_from_msg_id = msgObj.getId();
                                newMsg.fwd_from.saved_from_peer = msgObj.messageOwner.to_id;
                            }
                            if (canSendPreview || !(msgObj.messageOwner.media instanceof TL_messageMediaWebPage)) {
                                newMsg.media = msgObj.messageOwner.media;
                            } else {
                                newMsg.media = new TL_messageMediaEmpty();
                            }
                            if (newMsg.media != null) {
                                newMsg.flags |= 512;
                            }
                            if (isMegagroup) {
                                newMsg.flags |= Integer.MIN_VALUE;
                            }
                            if (msgObj.messageOwner.via_bot_id != 0) {
                                newMsg.via_bot_id = msgObj.messageOwner.via_bot_id;
                                newMsg.flags |= 2048;
                            }
                            newMsg.message = msgObj.messageOwner.message;
                            newMsg.fwd_msg_id = msgObj.getId();
                            newMsg.attachPath = msgObj.messageOwner.attachPath;
                            newMsg.entities = msgObj.messageOwner.entities;
                            if (!newMsg.entities.isEmpty()) {
                                newMsg.flags |= 128;
                            }
                            if (newMsg.attachPath == null) {
                                newMsg.attachPath = TtmlNode.ANONYMOUS_REGION_ID;
                            }
                            int newMessageId = UserConfig.getInstance(this.currentAccount).getNewMessageId();
                            newMsg.id = newMessageId;
                            newMsg.local_id = newMessageId;
                            newMsg.out = true;
                            long lastGroupedId = msgObj.messageOwner.grouped_id;
                            if (lastGroupedId != 0) {
                                Long gId = (Long) groupsMap.get(msgObj.messageOwner.grouped_id);
                                if (gId == null) {
                                    gId = Long.valueOf(Utilities.random.nextLong());
                                    groupsMap.put(msgObj.messageOwner.grouped_id, gId);
                                }
                                newMsg.grouped_id = gId.longValue();
                                newMsg.flags |= 131072;
                            }
                            if (a != messages.size() - 1) {
                                if (((MessageObject) messages.get(a + 1)).messageOwner.grouped_id != msgObj.messageOwner.grouped_id) {
                                    groupedIdChanged = true;
                                }
                            }
                            if (to_id.channel_id == 0 || isMegagroup) {
                                newMsg.from_id = UserConfig.getInstance(this.currentAccount).getClientUserId();
                                newMsg.flags |= 256;
                            } else {
                                newMsg.from_id = isSignature ? UserConfig.getInstance(this.currentAccount).getClientUserId() : -to_id.channel_id;
                                newMsg.post = true;
                            }
                            if (newMsg.random_id == 0) {
                                newMsg.random_id = getNextRandomId();
                            }
                            randomIds.add(Long.valueOf(newMsg.random_id));
                            messagesByRandomIds.put(newMsg.random_id, newMsg);
                            ids.add(Integer.valueOf(newMsg.fwd_msg_id));
                            newMsg.date = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
                            if (!(inputPeer instanceof TL_inputPeerChannel) || isMegagroup) {
                                if ((msgObj.messageOwner.flags & 1024) != 0) {
                                    newMsg.views = msgObj.messageOwner.views;
                                    newMsg.flags |= 1024;
                                }
                                newMsg.unread = true;
                            } else {
                                newMsg.views = 1;
                                newMsg.flags |= 1024;
                            }
                            newMsg.dialog_id = peer;
                            newMsg.to_id = to_id;
                            if (MessageObject.isVoiceMessage(newMsg) || MessageObject.isRoundVideoMessage(newMsg)) {
                                if (!(inputPeer instanceof TL_inputPeerChannel) || msgObj.getChannelId() == 0) {
                                    newMsg.media_unread = true;
                                } else {
                                    newMsg.media_unread = msgObj.isContentUnread();
                                }
                            }
                            if (msgObj.messageOwner.to_id instanceof TL_peerChannel) {
                                newMsg.ttl = -msgObj.messageOwner.to_id.channel_id;
                            }
                            MessageObject messageObject = new MessageObject(this.currentAccount, newMsg, true);
                            messageObject.messageOwner.send_state = 1;
                            objArr.add(messageObject);
                            arr.add(newMsg);
                            putToSendingMessages(newMsg);
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.d("forward message user_id = " + inputPeer.user_id + " chat_id = " + inputPeer.chat_id + " channel_id = " + inputPeer.channel_id + " access_hash = " + inputPeer.access_hash);
                            }
                            if (!((groupedIdChanged && arr.size() > 0) || arr.size() == 100 || a == messages.size() - 1)) {
                                if (a != messages.size() - 1) {
                                    if (((MessageObject) messages.get(a + 1)).getDialogId() == msgObj.getDialogId()) {
                                    }
                                }
                            }
                            MessagesStorage.getInstance(this.currentAccount).putMessages(new ArrayList(arr), false, true, false, 0);
                            MessagesController.getInstance(this.currentAccount).updateInterfaceWithMessages(peer, objArr);
                            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
                            UserConfig.getInstance(this.currentAccount).saveConfig(false);
                            TL_messages_forwardMessages req = new TL_messages_forwardMessages();
                            req.to_peer = inputPeer;
                            req.grouped = lastGroupedId != 0;
                            if (req.to_peer instanceof TL_inputPeerChannel) {
                                req.silent = MessagesController.getNotificationsSettings(this.currentAccount).getBoolean("silent_" + peer, false);
                            }
                            if (msgObj.messageOwner.to_id instanceof TL_peerChannel) {
                                chat = MessagesController.getInstance(this.currentAccount).getChat(Integer.valueOf(msgObj.messageOwner.to_id.channel_id));
                                req.from_peer = new TL_inputPeerChannel();
                                req.from_peer.channel_id = msgObj.messageOwner.to_id.channel_id;
                                if (chat != null) {
                                    req.from_peer.access_hash = chat.access_hash;
                                }
                            } else {
                                req.from_peer = new TL_inputPeerEmpty();
                            }
                            req.random_id = randomIds;
                            req.id = ids;
                            boolean z = messages.size() == 1 && ((MessageObject) messages.get(0)).messageOwner.with_my_score;
                            req.with_my_score = z;
                            ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new SendMessagesHelper$$Lambda$3(this, peer, isMegagroup, toMyself, messagesByRandomIds, arr, objArr, to_id, req), 68);
                            if (a != messages.size() - 1) {
                                objArr = new ArrayList();
                                arr = new ArrayList();
                                randomIds = new ArrayList();
                                ids = new ArrayList();
                                messagesByRandomIds = new LongSparseArray();
                            }
                        } else if (sendResult == 0) {
                            sendResult = 2;
                        }
                    } else if (sendResult == 0) {
                        sendResult = 1;
                    }
                }
                a++;
            }
            return sendResult;
        }
        for (a = 0; a < messages.size(); a++) {
            processForwardFromMyName((MessageObject) messages.get(a), peer);
        }
        return 0;
    }

    final /* synthetic */ void lambda$sendMessage$9$SendMessagesHelper(long peer, boolean isMegagroupFinal, boolean toMyself, LongSparseArray messagesByRandomIdsFinal, ArrayList newMsgObjArr, ArrayList newMsgArr, Peer to_id, TL_messages_forwardMessages req, TLObject response, TL_error error) {
        int a1;
        Message newMsgObj1;
        if (error == null) {
            Update update;
            SparseLongArray newMessagesByIds = new SparseLongArray();
            Updates updates = (Updates) response;
            a1 = 0;
            while (a1 < updates.updates.size()) {
                update = (Update) updates.updates.get(a1);
                if (update instanceof TL_updateMessageID) {
                    TL_updateMessageID updateMessageID = (TL_updateMessageID) update;
                    newMessagesByIds.put(updateMessageID.id, updateMessageID.random_id);
                    updates.updates.remove(a1);
                    a1--;
                }
                a1++;
            }
            Integer value = (Integer) MessagesController.getInstance(this.currentAccount).dialogs_read_outbox_max.get(Long.valueOf(peer));
            if (value == null) {
                value = Integer.valueOf(MessagesStorage.getInstance(this.currentAccount).getDialogReadMax(true, peer));
                MessagesController.getInstance(this.currentAccount).dialogs_read_outbox_max.put(Long.valueOf(peer), value);
            }
            int sentCount = 0;
            a1 = 0;
            while (a1 < updates.updates.size()) {
                update = (Update) updates.updates.get(a1);
                if ((update instanceof TL_updateNewMessage) || (update instanceof TL_updateNewChannelMessage)) {
                    Message message;
                    updates.updates.remove(a1);
                    a1--;
                    if (update instanceof TL_updateNewMessage) {
                        TL_updateNewMessage updateNewMessage = (TL_updateNewMessage) update;
                        message = updateNewMessage.message;
                        MessagesController.getInstance(this.currentAccount).processNewDifferenceParams(-1, updateNewMessage.pts, -1, updateNewMessage.pts_count);
                    } else {
                        TL_updateNewChannelMessage updateNewChannelMessage = (TL_updateNewChannelMessage) update;
                        message = updateNewChannelMessage.message;
                        MessagesController.getInstance(this.currentAccount).processNewChannelDifferenceParams(updateNewChannelMessage.pts, updateNewChannelMessage.pts_count, message.to_id.channel_id);
                        if (isMegagroupFinal) {
                            message.flags |= Integer.MIN_VALUE;
                        }
                    }
                    ImageLoader.saveMessageThumbs(message);
                    message.unread = value.intValue() < message.id;
                    if (toMyself) {
                        message.out = true;
                        message.unread = false;
                        message.media_unread = false;
                    }
                    long random_id = newMessagesByIds.get(message.id);
                    if (random_id != 0) {
                        newMsgObj1 = (Message) messagesByRandomIdsFinal.get(random_id);
                        if (newMsgObj1 != null) {
                            int index = newMsgObjArr.indexOf(newMsgObj1);
                            if (index != -1) {
                                MessageObject msgObj1 = (MessageObject) newMsgArr.get(index);
                                newMsgObjArr.remove(index);
                                newMsgArr.remove(index);
                                int oldId = newMsgObj1.id;
                                ArrayList<Message> sentMessages = new ArrayList();
                                sentMessages.add(message);
                                newMsgObj1.id = message.id;
                                sentCount++;
                                updateMediaPaths(msgObj1, message, null, true);
                                MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new SendMessagesHelper$$Lambda$56(this, newMsgObj1, oldId, to_id, sentMessages, peer, message));
                            }
                        }
                    }
                }
                a1++;
            }
            if (!updates.updates.isEmpty()) {
                MessagesController.getInstance(this.currentAccount).processUpdates(updates, false);
            }
            StatsController.getInstance(this.currentAccount).incrementSentItemsCount(ConnectionsManager.getCurrentNetworkType(), 1, sentCount);
        } else {
            AndroidUtilities.runOnUIThread(new SendMessagesHelper$$Lambda$57(this, error, req));
        }
        for (a1 = 0; a1 < newMsgObjArr.size(); a1++) {
            newMsgObj1 = (Message) newMsgObjArr.get(a1);
            MessagesStorage.getInstance(this.currentAccount).markMessageAsSendError(newMsgObj1);
            AndroidUtilities.runOnUIThread(new SendMessagesHelper$$Lambda$58(this, newMsgObj1));
        }
    }

    final /* synthetic */ void lambda$null$6$SendMessagesHelper(Message newMsgObj1, int oldId, Peer to_id, ArrayList sentMessages, long peer, Message message) {
        MessagesStorage.getInstance(this.currentAccount).updateMessageStateAndId(newMsgObj1.random_id, Integer.valueOf(oldId), newMsgObj1.id, 0, false, to_id.channel_id);
        MessagesStorage.getInstance(this.currentAccount).putMessages(sentMessages, true, false, false, 0);
        AndroidUtilities.runOnUIThread(new SendMessagesHelper$$Lambda$59(this, newMsgObj1, peer, oldId, message));
    }

    final /* synthetic */ void lambda$null$5$SendMessagesHelper(Message newMsgObj1, long peer, int oldId, Message message) {
        newMsgObj1.send_state = 0;
        DataQuery.getInstance(this.currentAccount).increasePeerRaiting(peer);
        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.messageReceivedByServer, Integer.valueOf(oldId), Integer.valueOf(message.id), message, Long.valueOf(peer), Long.valueOf(0));
        processSentMessage(oldId);
        removeFromSendingMessages(oldId);
    }

    final /* synthetic */ void lambda$null$7$SendMessagesHelper(TL_error error, TL_messages_forwardMessages req) {
        AlertsCreator.processError(this.currentAccount, error, null, req, new Object[0]);
    }

    final /* synthetic */ void lambda$null$8$SendMessagesHelper(Message newMsgObj1) {
        newMsgObj1.send_state = 2;
        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.messageSendError, Integer.valueOf(newMsgObj1.id));
        processSentMessage(newMsgObj1.id);
        removeFromSendingMessages(newMsgObj1.id);
    }

    private void writePreviousMessageData(Message message, SerializedData data) {
        message.media.serializeToStream(data);
        data.writeString(message.message != null ? message.message : TtmlNode.ANONYMOUS_REGION_ID);
        data.writeString(message.attachPath != null ? message.attachPath : TtmlNode.ANONYMOUS_REGION_ID);
        int count = message.entities.size();
        data.writeInt32(count);
        for (int a = 0; a < count; a++) {
            ((MessageEntity) message.entities.get(a)).serializeToStream(data);
        }
    }

    private void editMessageMedia(MessageObject messageObject, TL_photo photo, VideoEditedInfo videoEditedInfo, TL_document document, String path, HashMap<String, String> params, boolean retry) {
        Throwable e;
        if (messageObject != null) {
            ArrayList<MessageEntity> entities;
            Message newMsg = messageObject.messageOwner;
            messageObject.cancelEditing = false;
            int type = -1;
            DelayedMessage delayedMessage = null;
            long peer = messageObject.getDialogId();
            if (retry) {
                if (messageObject.messageOwner.media instanceof TL_messageMediaPhoto) {
                    photo = (TL_photo) messageObject.messageOwner.media.photo;
                    type = 2;
                } else {
                    document = (TL_document) messageObject.messageOwner.media.document;
                    if (MessageObject.isVideoDocument(document) || videoEditedInfo != null) {
                        type = 3;
                    } else {
                        type = 7;
                    }
                    videoEditedInfo = messageObject.videoEditedInfo;
                }
                params = newMsg.params;
                messageObject.editingMessage = newMsg.message;
                messageObject.editingMessageEntities = newMsg.entities;
                path = newMsg.attachPath;
            } else {
                messageObject.previousMedia = newMsg.media;
                messageObject.previousCaption = newMsg.message;
                messageObject.previousCaptionEntities = newMsg.entities;
                messageObject.previousAttachPath = newMsg.attachPath;
                SerializedData serializedData = new SerializedData(true);
                writePreviousMessageData(newMsg, serializedData);
                serializedData = new SerializedData(serializedData.length());
                writePreviousMessageData(newMsg, serializedData);
                if (params == null) {
                    params = new HashMap();
                }
                params.put("prevMedia", Base64.encodeToString(serializedData.toByteArray(), 0));
                serializedData.cleanup();
                MessageMedia messageMedia;
                if (photo != null) {
                    newMsg.media = new TL_messageMediaPhoto();
                    messageMedia = newMsg.media;
                    messageMedia.flags |= 3;
                    newMsg.media.photo = photo;
                    type = 2;
                    if (path != null && path.length() > 0) {
                        if (path.startsWith("http")) {
                            newMsg.attachPath = path;
                        }
                    }
                    try {
                        newMsg.attachPath = FileLoader.getPathToAttach(((PhotoSize) photo.sizes.get(photo.sizes.size() - 1)).location, true).toString();
                    } catch (Exception e2) {
                        e = e2;
                        FileLog.e(e);
                        revertEditingMessageObject(messageObject);
                        return;
                    }
                } else if (document != null) {
                    newMsg.media = new TL_messageMediaDocument();
                    messageMedia = newMsg.media;
                    messageMedia.flags |= 3;
                    newMsg.media.document = document;
                    if (MessageObject.isVideoDocument(document) || videoEditedInfo != null) {
                        type = 3;
                    } else {
                        type = 7;
                    }
                    if (videoEditedInfo != null) {
                        params.put("ve", videoEditedInfo.getString());
                    }
                    newMsg.attachPath = path;
                }
                newMsg.params = params;
                newMsg.send_state = 3;
            }
            if (newMsg.attachPath == null) {
                newMsg.attachPath = TtmlNode.ANONYMOUS_REGION_ID;
            }
            newMsg.local_id = 0;
            if ((messageObject.type == 3 || videoEditedInfo != null || messageObject.type == 2) && !TextUtils.isEmpty(newMsg.attachPath)) {
                messageObject.attachPathExists = true;
            }
            if (messageObject.videoEditedInfo != null && videoEditedInfo == null) {
                videoEditedInfo = messageObject.videoEditedInfo;
            }
            if (!retry) {
                if (messageObject.editingMessage != null) {
                    newMsg.message = messageObject.editingMessage.toString();
                    if (messageObject.editingMessageEntities != null) {
                        newMsg.entities = messageObject.editingMessageEntities;
                    } else {
                        entities = DataQuery.getInstance(this.currentAccount).getEntities(new CharSequence[]{messageObject.editingMessage});
                        if (!(entities == null || entities.isEmpty())) {
                            newMsg.entities = entities;
                        }
                    }
                    messageObject.caption = null;
                    messageObject.generateCaption();
                }
                ArrayList arr = new ArrayList();
                arr.add(newMsg);
                MessagesStorage.getInstance(this.currentAccount).putMessages(arr, false, true, false, 0);
                messageObject.type = -1;
                messageObject.setType();
                messageObject.createMessageSendInfo();
                new ArrayList().add(messageObject);
                NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.replaceMessagesObjects, Long.valueOf(peer), arrayList);
            }
            String originalPath = null;
            if (params != null) {
                if (params.containsKey("originalPath")) {
                    originalPath = (String) params.get("originalPath");
                }
            }
            if ((type >= 1 && type <= 3) || (type >= 5 && type <= 8)) {
                InputMedia inputMedia = null;
                DelayedMessage delayedMessage2;
                InputMedia media;
                if (type == 2) {
                    if (photo.access_hash == 0) {
                        inputMedia = new TL_inputMediaUploadedPhoto();
                        if (params != null) {
                            String masks = (String) params.get("masks");
                            if (masks != null) {
                                AbstractSerializedData serializedData2 = new SerializedData(Utilities.hexToBytes(masks));
                                int count = serializedData2.readInt32(false);
                                for (int a = 0; a < count; a++) {
                                    inputMedia.stickers.add(InputDocument.TLdeserialize(serializedData2, serializedData2.readInt32(false), false));
                                }
                                inputMedia.flags |= 1;
                                serializedData2.cleanup();
                            }
                        }
                        if (null == null) {
                            delayedMessage2 = new DelayedMessage(peer);
                            try {
                                delayedMessage2.type = 0;
                                delayedMessage2.obj = messageObject;
                                delayedMessage2.originalPath = originalPath;
                                delayedMessage = delayedMessage2;
                            } catch (Exception e3) {
                                e = e3;
                                delayedMessage = delayedMessage;
                                FileLog.e(e);
                                revertEditingMessageObject(messageObject);
                                return;
                            }
                        }
                        if (path != null) {
                            if (path.length() > 0) {
                                if (path.startsWith("http")) {
                                    delayedMessage.httpLocation = path;
                                }
                            }
                        }
                        delayedMessage.location = ((PhotoSize) photo.sizes.get(photo.sizes.size() - 1)).location;
                    } else {
                        media = new TL_inputMediaPhoto();
                        media.id = new TL_inputPhoto();
                        media.id.id = photo.id;
                        media.id.access_hash = photo.access_hash;
                        inputMedia = media;
                    }
                } else if (type == 3) {
                    if (document.access_hash == 0) {
                        inputMedia = new TL_inputMediaUploadedDocument();
                        inputMedia.mime_type = document.mime_type;
                        inputMedia.attributes = document.attributes;
                        if (!messageObject.isGif() && (videoEditedInfo == null || !videoEditedInfo.muted)) {
                            inputMedia.nosound_video = true;
                        }
                        if (null == null) {
                            delayedMessage2 = new DelayedMessage(peer);
                            delayedMessage2.type = 1;
                            delayedMessage2.obj = messageObject;
                            delayedMessage2.originalPath = originalPath;
                            delayedMessage = delayedMessage2;
                        }
                        delayedMessage.location = document.thumb.location;
                        delayedMessage.videoEditedInfo = videoEditedInfo;
                    } else {
                        media = new TL_inputMediaDocument();
                        media.id = new TL_inputDocument();
                        media.id.id = document.id;
                        media.id.access_hash = document.access_hash;
                        inputMedia = media;
                    }
                } else if (type == 7) {
                    if (document.access_hash == 0) {
                        if (originalPath != null && originalPath.length() > 0) {
                            if (originalPath.startsWith("http") && params != null) {
                                inputMedia = new TL_inputMediaGifExternal();
                                String[] args = ((String) params.get(UpdateFragment.FRAGMENT_URL)).split("\\|");
                                if (args.length == 2) {
                                    ((TL_inputMediaGifExternal) inputMedia).url = args[0];
                                    inputMedia.q = args[1];
                                }
                                inputMedia.mime_type = document.mime_type;
                                inputMedia.attributes = document.attributes;
                            }
                        }
                        inputMedia = new TL_inputMediaUploadedDocument();
                        delayedMessage2 = new DelayedMessage(peer);
                        delayedMessage2.originalPath = originalPath;
                        delayedMessage2.type = 2;
                        delayedMessage2.obj = messageObject;
                        delayedMessage2.location = document.thumb.location;
                        delayedMessage = delayedMessage2;
                        inputMedia.mime_type = document.mime_type;
                        inputMedia.attributes = document.attributes;
                    } else {
                        media = new TL_inputMediaDocument();
                        media.id = new TL_inputDocument();
                        media.id.id = document.id;
                        media.id.access_hash = document.access_hash;
                        inputMedia = media;
                    }
                }
                TLObject request = new TL_messages_editMessage();
                request.id = messageObject.getId();
                request.peer = MessagesController.getInstance(this.currentAccount).getInputPeer((int) peer);
                request.flags |= MessagesController.UPDATE_MASK_CHAT_ADMINS;
                request.media = inputMedia;
                if (messageObject.editingMessage != null) {
                    request.message = messageObject.editingMessage.toString();
                    request.flags |= 2048;
                    if (messageObject.editingMessageEntities != null) {
                        request.entities = messageObject.editingMessageEntities;
                        request.flags |= 8;
                    } else {
                        entities = DataQuery.getInstance(this.currentAccount).getEntities(new CharSequence[]{messageObject.editingMessage});
                        if (!(entities == null || entities.isEmpty())) {
                            request.entities = entities;
                            request.flags |= 8;
                        }
                    }
                    messageObject.editingMessage = null;
                    messageObject.editingMessageEntities = null;
                }
                if (delayedMessage != null) {
                    delayedMessage.sendRequest = request;
                }
                TLObject reqSend = request;
                if (type == 1) {
                    performSendMessageRequest(reqSend, messageObject, null);
                } else if (type == 2) {
                    if (photo.access_hash == 0) {
                        performSendDelayedMessage(delayedMessage);
                    } else {
                        performSendMessageRequest(reqSend, messageObject, null, null, true);
                    }
                } else if (type == 3) {
                    if (document.access_hash == 0) {
                        performSendDelayedMessage(delayedMessage);
                    } else {
                        performSendMessageRequest(reqSend, messageObject, null);
                    }
                } else if (type == 6) {
                    performSendMessageRequest(reqSend, messageObject, null);
                } else if (type == 7) {
                    if (document.access_hash != 0 || delayedMessage == null) {
                        performSendMessageRequest(reqSend, messageObject, originalPath);
                    } else {
                        performSendDelayedMessage(delayedMessage);
                    }
                } else if (type != 8) {
                } else {
                    if (document.access_hash == 0) {
                        performSendDelayedMessage(delayedMessage);
                    } else {
                        performSendMessageRequest(reqSend, messageObject, null);
                    }
                }
            }
        }
    }

    public int editMessage(MessageObject messageObject, String message, boolean searchLinks, BaseFragment fragment, ArrayList<MessageEntity> entities, Runnable callback) {
        boolean z = false;
        if (fragment == null || fragment.getParentActivity() == null || callback == null) {
            return 0;
        }
        TL_messages_editMessage req = new TL_messages_editMessage();
        req.peer = MessagesController.getInstance(this.currentAccount).getInputPeer((int) messageObject.getDialogId());
        req.message = message;
        req.flags |= 2048;
        req.id = messageObject.getId();
        if (!searchLinks) {
            z = true;
        }
        req.no_webpage = z;
        if (entities != null) {
            req.entities = entities;
            req.flags |= 8;
        }
        return ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new SendMessagesHelper$$Lambda$4(this, fragment, req, callback));
    }

    final /* synthetic */ void lambda$editMessage$11$SendMessagesHelper(BaseFragment fragment, TL_messages_editMessage req, Runnable callback, TLObject response, TL_error error) {
        if (error == null) {
            MessagesController.getInstance(this.currentAccount).processUpdates((Updates) response, false);
        } else {
            AndroidUtilities.runOnUIThread(new SendMessagesHelper$$Lambda$55(this, error, fragment, req));
        }
        AndroidUtilities.runOnUIThread(callback);
    }

    final /* synthetic */ void lambda$null$10$SendMessagesHelper(TL_error error, BaseFragment fragment, TL_messages_editMessage req) {
        AlertsCreator.processError(this.currentAccount, error, fragment, req, new Object[0]);
    }

    private void sendLocation(Location location) {
        MessageMedia mediaGeo = new TL_messageMediaGeo();
        mediaGeo.geo = new TL_geoPoint();
        mediaGeo.geo.lat = AndroidUtilities.fixLocationCoord(location.getLatitude());
        mediaGeo.geo._long = AndroidUtilities.fixLocationCoord(location.getLongitude());
        for (Entry<String, MessageObject> entry : this.waitingForLocation.entrySet()) {
            MessageObject messageObject = (MessageObject) entry.getValue();
            sendMessage(mediaGeo, messageObject.getDialogId(), messageObject, null, null);
        }
    }

    public void sendCurrentLocation(MessageObject messageObject, KeyboardButton button) {
        if (messageObject != null && button != null) {
            this.waitingForLocation.put(messageObject.getDialogId() + "_" + messageObject.getId() + "_" + Utilities.bytesToHex(button.data) + "_" + (button instanceof TL_keyboardButtonGame ? "1" : "0"), messageObject);
            this.locationProvider.start();
        }
    }

    public boolean isSendingCurrentLocation(MessageObject messageObject, KeyboardButton button) {
        if (messageObject == null || button == null) {
            return false;
        }
        return this.waitingForLocation.containsKey(messageObject.getDialogId() + "_" + messageObject.getId() + "_" + Utilities.bytesToHex(button.data) + "_" + (button instanceof TL_keyboardButtonGame ? "1" : "0"));
    }

    public void sendNotificationCallback(long dialogId, int msgId, byte[] data) {
        AndroidUtilities.runOnUIThread(new SendMessagesHelper$$Lambda$5(this, dialogId, msgId, data));
    }

    final /* synthetic */ void lambda$sendNotificationCallback$14$SendMessagesHelper(long dialogId, int msgId, byte[] data) {
        int lowerId = (int) dialogId;
        String key = dialogId + "_" + msgId + "_" + Utilities.bytesToHex(data) + "_" + 0;
        this.waitingForCallback.put(key, Boolean.valueOf(true));
        if (lowerId > 0) {
            if (MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(lowerId)) == null) {
                User user = MessagesStorage.getInstance(this.currentAccount).getUserSync(lowerId);
                if (user != null) {
                    MessagesController.getInstance(this.currentAccount).putUser(user, true);
                }
            }
        } else if (MessagesController.getInstance(this.currentAccount).getChat(Integer.valueOf(-lowerId)) == null) {
            Chat chat = MessagesStorage.getInstance(this.currentAccount).getChatSync(-lowerId);
            if (chat != null) {
                MessagesController.getInstance(this.currentAccount).putChat(chat, true);
            }
        }
        TL_messages_getBotCallbackAnswer req = new TL_messages_getBotCallbackAnswer();
        req.peer = MessagesController.getInstance(this.currentAccount).getInputPeer(lowerId);
        req.msg_id = msgId;
        req.game = false;
        if (data != null) {
            req.flags |= 1;
            req.data = data;
        }
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new SendMessagesHelper$$Lambda$53(this, key), 2);
        MessagesController.getInstance(this.currentAccount).markDialogAsRead(dialogId, msgId, msgId, 0, false, 0, true);
    }

    final /* synthetic */ void lambda$null$12$SendMessagesHelper(String key) {
        Boolean bool = (Boolean) this.waitingForCallback.remove(key);
    }

    final /* synthetic */ void lambda$null$13$SendMessagesHelper(String key, TLObject response, TL_error error) {
        AndroidUtilities.runOnUIThread(new SendMessagesHelper$$Lambda$54(this, key));
    }

    public void sendCallback(boolean cache, MessageObject messageObject, KeyboardButton button, ChatActivity parentFragment) {
        if (messageObject != null && button != null && parentFragment != null) {
            boolean cacheFinal;
            int type;
            if (button instanceof TL_keyboardButtonGame) {
                cacheFinal = false;
                type = 1;
            } else {
                cacheFinal = cache;
                if (button instanceof TL_keyboardButtonBuy) {
                    type = 2;
                } else {
                    type = 0;
                }
            }
            String key = messageObject.getDialogId() + "_" + messageObject.getId() + "_" + Utilities.bytesToHex(button.data) + "_" + type;
            this.waitingForCallback.put(key, Boolean.valueOf(true));
            RequestDelegate requestDelegate = new SendMessagesHelper$$Lambda$6(this, key, cacheFinal, messageObject, button, parentFragment);
            if (cacheFinal) {
                MessagesStorage.getInstance(this.currentAccount).getBotCache(key, requestDelegate);
            } else if (!(button instanceof TL_keyboardButtonBuy)) {
                TL_messages_getBotCallbackAnswer req = new TL_messages_getBotCallbackAnswer();
                req.peer = MessagesController.getInstance(this.currentAccount).getInputPeer((int) messageObject.getDialogId());
                req.msg_id = messageObject.getId();
                req.game = button instanceof TL_keyboardButtonGame;
                if (button.data != null) {
                    req.flags |= 1;
                    req.data = button.data;
                }
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, requestDelegate, 2);
            } else if ((messageObject.messageOwner.media.flags & 4) == 0) {
                TL_payments_getPaymentForm req2 = new TL_payments_getPaymentForm();
                req2.msg_id = messageObject.getId();
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(req2, requestDelegate, 2);
            } else {
                TL_payments_getPaymentReceipt req3 = new TL_payments_getPaymentReceipt();
                req3.msg_id = messageObject.messageOwner.media.receipt_msg_id;
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(req3, requestDelegate, 2);
            }
        }
    }

    final /* synthetic */ void lambda$sendCallback$16$SendMessagesHelper(String key, boolean cacheFinal, MessageObject messageObject, KeyboardButton button, ChatActivity parentFragment, TLObject response, TL_error error) {
        AndroidUtilities.runOnUIThread(new SendMessagesHelper$$Lambda$52(this, key, cacheFinal, response, messageObject, button, parentFragment));
    }

    final /* synthetic */ void lambda$null$15$SendMessagesHelper(String key, boolean cacheFinal, TLObject response, MessageObject messageObject, KeyboardButton button, ChatActivity parentFragment) {
        this.waitingForCallback.remove(key);
        if (cacheFinal && response == null) {
            sendCallback(false, messageObject, button, parentFragment);
        } else if (response == null) {
        } else {
            if (!(button instanceof TL_keyboardButtonBuy)) {
                TL_messages_botCallbackAnswer res = (TL_messages_botCallbackAnswer) response;
                if (!(cacheFinal || res.cache_time == 0)) {
                    MessagesStorage.getInstance(this.currentAccount).saveBotCache(key, res);
                }
                int uid;
                User user;
                if (res.message != null) {
                    if (!res.alert) {
                        uid = messageObject.messageOwner.from_id;
                        if (messageObject.messageOwner.via_bot_id != 0) {
                            uid = messageObject.messageOwner.via_bot_id;
                        }
                        String name = null;
                        if (uid > 0) {
                            user = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(uid));
                            if (user != null) {
                                name = ContactsController.formatName(user.first_name, user.last_name);
                            }
                        } else {
                            Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Integer.valueOf(-uid));
                            if (chat != null) {
                                name = chat.title;
                            }
                        }
                        if (name == null) {
                            name = "bot";
                        }
                        parentFragment.showAlert(name, res.message);
                    } else if (parentFragment.getParentActivity() != null) {
                        Builder builder = new Builder(parentFragment.getParentActivity());
                        builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
                        builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
                        builder.setMessage(res.message);
                        parentFragment.showDialog(builder.create());
                    }
                } else if (res.url != null && parentFragment.getParentActivity() != null) {
                    uid = messageObject.messageOwner.from_id;
                    if (messageObject.messageOwner.via_bot_id != 0) {
                        uid = messageObject.messageOwner.via_bot_id;
                    }
                    user = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(uid));
                    boolean verified = user != null && user.verified;
                    if (button instanceof TL_keyboardButtonGame) {
                        TL_game game = messageObject.messageOwner.media instanceof TL_messageMediaGame ? messageObject.messageOwner.media.game : null;
                        if (game != null) {
                            boolean z;
                            String str = res.url;
                            if (verified || !MessagesController.getNotificationsSettings(this.currentAccount).getBoolean("askgame_" + uid, true)) {
                                z = false;
                            } else {
                                z = true;
                            }
                            parentFragment.showOpenGameAlert(game, messageObject, str, z, uid);
                            return;
                        }
                        return;
                    }
                    parentFragment.showOpenUrlAlert(res.url, false);
                }
            } else if (response instanceof TL_payments_paymentForm) {
                TL_payments_paymentForm form = (TL_payments_paymentForm) response;
                MessagesController.getInstance(this.currentAccount).putUsers(form.users, false);
                parentFragment.presentFragment(new PaymentFormActivity(form, messageObject));
            } else if (response instanceof TL_payments_paymentReceipt) {
                parentFragment.presentFragment(new PaymentFormActivity(messageObject, (TL_payments_paymentReceipt) response));
            }
        }
    }

    public boolean isSendingCallback(MessageObject messageObject, KeyboardButton button) {
        if (messageObject == null || button == null) {
            return false;
        }
        int type;
        if (button instanceof TL_keyboardButtonGame) {
            type = 1;
        } else if (button instanceof TL_keyboardButtonBuy) {
            type = 2;
        } else {
            type = 0;
        }
        return this.waitingForCallback.containsKey(messageObject.getDialogId() + "_" + messageObject.getId() + "_" + Utilities.bytesToHex(button.data) + "_" + type);
    }

    public void sendGame(InputPeer peer, TL_inputMediaGame game, long random_id, long taskId) {
        Throwable e;
        long newTaskId;
        if (peer != null && game != null) {
            TL_messages_sendMedia request = new TL_messages_sendMedia();
            request.peer = peer;
            if (request.peer instanceof TL_inputPeerChannel) {
                request.silent = MessagesController.getNotificationsSettings(this.currentAccount).getBoolean("silent_" + peer.channel_id, false);
            }
            request.random_id = random_id != 0 ? random_id : getNextRandomId();
            request.message = TtmlNode.ANONYMOUS_REGION_ID;
            request.media = game;
            if (taskId == 0) {
                NativeByteBuffer data = null;
                try {
                    NativeByteBuffer data2 = new NativeByteBuffer(((peer.getObjectSize() + game.getObjectSize()) + 4) + 8);
                    try {
                        data2.writeInt32(3);
                        data2.writeInt64(random_id);
                        peer.serializeToStream(data2);
                        game.serializeToStream(data2);
                        data = data2;
                    } catch (Exception e2) {
                        e = e2;
                        data = data2;
                        FileLog.e(e);
                        newTaskId = MessagesStorage.getInstance(this.currentAccount).createPendingTask(data);
                        ConnectionsManager.getInstance(this.currentAccount).sendRequest(request, new SendMessagesHelper$$Lambda$7(this, newTaskId));
                    }
                } catch (Exception e3) {
                    e = e3;
                    FileLog.e(e);
                    newTaskId = MessagesStorage.getInstance(this.currentAccount).createPendingTask(data);
                    ConnectionsManager.getInstance(this.currentAccount).sendRequest(request, new SendMessagesHelper$$Lambda$7(this, newTaskId));
                }
                newTaskId = MessagesStorage.getInstance(this.currentAccount).createPendingTask(data);
            } else {
                newTaskId = taskId;
            }
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(request, new SendMessagesHelper$$Lambda$7(this, newTaskId));
        }
    }

    final /* synthetic */ void lambda$sendGame$17$SendMessagesHelper(long newTaskId, TLObject response, TL_error error) {
        if (error == null) {
            MessagesController.getInstance(this.currentAccount).processUpdates((Updates) response, false);
        }
        if (newTaskId != 0) {
            MessagesStorage.getInstance(this.currentAccount).removePendingTask(newTaskId);
        }
    }

    public void sendMessage(MessageObject retryMessageObject) {
        sendMessage(null, null, null, null, null, null, null, null, retryMessageObject.getDialogId(), retryMessageObject.messageOwner.attachPath, null, null, true, retryMessageObject, null, retryMessageObject.messageOwner.reply_markup, retryMessageObject.messageOwner.params, 0);
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

    private void sendMessage(String message, String caption, MessageMedia location, TL_photo photo, VideoEditedInfo videoEditedInfo, User user, TL_document document, TL_game game, long peer, String path, MessageObject reply_to_msg, WebPage webPage, boolean searchLinks, MessageObject retryMessageObject, ArrayList<MessageEntity> entities, ReplyMarkup replyMarkup, HashMap<String, String> params, int ttl) {
        Throwable e;
        MessageObject newMsgObj;
        if ((user == null || user.phone != null) && peer != 0) {
            Chat chat;
            Document document2;
            MessageMedia messageMedia;
            int a;
            DocumentAttribute attribute;
            if (message == null && caption == null) {
                caption = TtmlNode.ANONYMOUS_REGION_ID;
            }
            String originalPath = null;
            if (params != null) {
                if (params.containsKey("originalPath")) {
                    originalPath = (String) params.get("originalPath");
                }
            }
            Message newMsg = null;
            int type = -1;
            int lower_id = (int) peer;
            int high_id = (int) (peer >> 32);
            boolean isChannel = false;
            EncryptedChat encryptedChat = null;
            InputPeer sendToPeer = lower_id != 0 ? MessagesController.getInstance(this.currentAccount).getInputPeer(lower_id) : null;
            ArrayList<InputUser> sendToPeers = null;
            if (lower_id == 0) {
                encryptedChat = MessagesController.getInstance(this.currentAccount).getEncryptedChat(Integer.valueOf(high_id));
                if (encryptedChat == null) {
                    if (retryMessageObject != null) {
                        MessagesStorage.getInstance(this.currentAccount).markMessageAsSendError(retryMessageObject.messageOwner);
                        retryMessageObject.messageOwner.send_state = 2;
                        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.messageSendError, Integer.valueOf(retryMessageObject.getId()));
                        processSentMessage(retryMessageObject.getId());
                        return;
                    }
                    return;
                }
            } else if (sendToPeer instanceof TL_inputPeerChannel) {
                chat = MessagesController.getInstance(this.currentAccount).getChat(Integer.valueOf(sendToPeer.channel_id));
                isChannel = (chat == null || chat.megagroup) ? false : true;
            }
            if (retryMessageObject != null) {
                try {
                    newMsg = retryMessageObject.messageOwner;
                    if (retryMessageObject.isForwarded()) {
                        type = 4;
                    } else {
                        if (retryMessageObject.type == 0) {
                            if (!(retryMessageObject.messageOwner.media instanceof TL_messageMediaGame)) {
                                message = newMsg.message;
                            }
                            type = 0;
                        } else if (retryMessageObject.type == 4) {
                            location = newMsg.media;
                            type = 1;
                        } else if (retryMessageObject.type == 1) {
                            photo = (TL_photo) newMsg.media.photo;
                            type = 2;
                        } else if (retryMessageObject.type == 3 || retryMessageObject.type == 5 || videoEditedInfo != null) {
                            type = 3;
                            document2 = (TL_document) newMsg.media.document;
                        } else if (retryMessageObject.type == 12) {
                            User user2 = new TL_userRequest_old2();
                            try {
                                user2.phone = newMsg.media.phone_number;
                                user2.first_name = newMsg.media.first_name;
                                user2.last_name = newMsg.media.last_name;
                                user2.restriction_reason = newMsg.media.vcard;
                                user2.id = newMsg.media.user_id;
                                type = 6;
                                user = user2;
                            } catch (Exception e2) {
                                e = e2;
                                newMsgObj = null;
                                user = user2;
                                FileLog.e(e);
                                MessagesStorage.getInstance(this.currentAccount).markMessageAsSendError(newMsg);
                                if (newMsgObj != null) {
                                    newMsgObj.messageOwner.send_state = 2;
                                }
                                NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.messageSendError, Integer.valueOf(newMsg.id));
                                processSentMessage(newMsg.id);
                            }
                        } else if (retryMessageObject.type == 8 || retryMessageObject.type == 9 || retryMessageObject.type == 13 || retryMessageObject.type == 14) {
                            document2 = (TL_document) newMsg.media.document;
                            type = 7;
                        } else if (retryMessageObject.type == 2) {
                            document2 = (TL_document) newMsg.media.document;
                            type = 8;
                        }
                        if (params != null) {
                            if (params.containsKey("query_id")) {
                                type = 9;
                            }
                        }
                    }
                } catch (Exception e3) {
                    e = e3;
                    newMsgObj = null;
                    FileLog.e(e);
                    MessagesStorage.getInstance(this.currentAccount).markMessageAsSendError(newMsg);
                    if (newMsgObj != null) {
                        newMsgObj.messageOwner.send_state = 2;
                    }
                    NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.messageSendError, Integer.valueOf(newMsg.id));
                    processSentMessage(newMsg.id);
                }
            }
            if (message != null) {
                if (encryptedChat != null) {
                    newMsg = new TL_message_secret();
                } else {
                    newMsg = new TL_message();
                }
                if (encryptedChat != null && (webPage instanceof TL_webPagePending)) {
                    if (webPage.url != null) {
                        WebPage newWebPage = new TL_webPageUrlPending();
                        newWebPage.url = webPage.url;
                        webPage = newWebPage;
                    } else {
                        webPage = null;
                    }
                }
                if (webPage == null) {
                    newMsg.media = new TL_messageMediaEmpty();
                } else {
                    newMsg.media = new TL_messageMediaWebPage();
                    newMsg.media.webpage = webPage;
                }
                if (params != null) {
                    if (params.containsKey("query_id")) {
                        type = 9;
                        newMsg.message = message;
                    }
                }
                type = 0;
                newMsg.message = message;
            } else if (location != null) {
                if (encryptedChat != null) {
                    newMsg = new TL_message_secret();
                } else {
                    newMsg = new TL_message();
                }
                newMsg.media = location;
                if (params != null) {
                    if (params.containsKey("query_id")) {
                        type = 9;
                    }
                }
                type = 1;
            } else if (photo != null) {
                if (encryptedChat != null) {
                    newMsg = new TL_message_secret();
                } else {
                    newMsg = new TL_message();
                }
                newMsg.media = new TL_messageMediaPhoto();
                messageMedia = newMsg.media;
                messageMedia.flags |= 3;
                if (entities != null) {
                    newMsg.entities = entities;
                }
                if (ttl != 0) {
                    newMsg.media.ttl_seconds = ttl;
                    newMsg.ttl = ttl;
                    messageMedia = newMsg.media;
                    messageMedia.flags |= 4;
                }
                newMsg.media.photo = photo;
                if (params != null) {
                    if (params.containsKey("query_id")) {
                        type = 9;
                        if (path != null && path.length() > 0) {
                            if (path.startsWith("http")) {
                                newMsg.attachPath = path;
                            }
                        }
                        newMsg.attachPath = FileLoader.getPathToAttach(((PhotoSize) photo.sizes.get(photo.sizes.size() - 1)).location, true).toString();
                    }
                }
                type = 2;
                if (path.startsWith("http")) {
                    newMsg.attachPath = path;
                }
                newMsg.attachPath = FileLoader.getPathToAttach(((PhotoSize) photo.sizes.get(photo.sizes.size() - 1)).location, true).toString();
            } else if (game != null) {
                Message newMsg2 = new TL_message();
                try {
                    newMsg2.media = new TL_messageMediaGame();
                    newMsg2.media.game = game;
                    if (params != null) {
                        if (params.containsKey("query_id")) {
                            type = 9;
                            newMsg = newMsg2;
                        }
                    }
                    newMsg = newMsg2;
                } catch (Exception e4) {
                    e = e4;
                    newMsgObj = null;
                    newMsg = newMsg2;
                    FileLog.e(e);
                    MessagesStorage.getInstance(this.currentAccount).markMessageAsSendError(newMsg);
                    if (newMsgObj != null) {
                        newMsgObj.messageOwner.send_state = 2;
                    }
                    NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.messageSendError, Integer.valueOf(newMsg.id));
                    processSentMessage(newMsg.id);
                }
            } else if (user != null) {
                String str;
                if (encryptedChat != null) {
                    newMsg = new TL_message_secret();
                } else {
                    newMsg = new TL_message();
                }
                newMsg.media = new TL_messageMediaContact();
                newMsg.media.phone_number = user.phone;
                newMsg.media.first_name = user.first_name;
                newMsg.media.last_name = user.last_name;
                newMsg.media.user_id = user.id;
                if (user.restriction_reason == null || !user.restriction_reason.startsWith("BEGIN:VCARD")) {
                    newMsg.media.vcard = TtmlNode.ANONYMOUS_REGION_ID;
                } else {
                    newMsg.media.vcard = user.restriction_reason;
                }
                if (newMsg.media.first_name == null) {
                    messageMedia = newMsg.media;
                    str = TtmlNode.ANONYMOUS_REGION_ID;
                    messageMedia.first_name = str;
                    user.first_name = str;
                }
                if (newMsg.media.last_name == null) {
                    messageMedia = newMsg.media;
                    str = TtmlNode.ANONYMOUS_REGION_ID;
                    messageMedia.last_name = str;
                    user.last_name = str;
                }
                if (params != null) {
                    if (params.containsKey("query_id")) {
                        type = 9;
                    }
                }
                type = 6;
            } else if (document != null) {
                String ve;
                TL_documentAttributeSticker_layer55 attributeSticker;
                String name;
                if (encryptedChat != null) {
                    newMsg = new TL_message_secret();
                } else {
                    newMsg = new TL_message();
                }
                newMsg.media = new TL_messageMediaDocument();
                messageMedia = newMsg.media;
                messageMedia.flags |= 3;
                if (ttl != 0) {
                    newMsg.media.ttl_seconds = ttl;
                    newMsg.ttl = ttl;
                    messageMedia = newMsg.media;
                    messageMedia.flags |= 4;
                }
                newMsg.media.document = document;
                if (params != null) {
                    if (params.containsKey("query_id")) {
                        type = 9;
                        if (videoEditedInfo != null) {
                            ve = videoEditedInfo.getString();
                            if (params == null) {
                                params = new HashMap();
                            }
                            params.put("ve", ve);
                        }
                        if (encryptedChat != null || document.dc_id <= 0 || MessageObject.isStickerDocument(document)) {
                            newMsg.attachPath = path;
                        } else {
                            newMsg.attachPath = FileLoader.getPathToAttach(document).toString();
                        }
                        if (encryptedChat != null && MessageObject.isStickerDocument(document)) {
                            a = 0;
                            while (a < document.attributes.size()) {
                                attribute = (DocumentAttribute) document.attributes.get(a);
                                if (attribute instanceof TL_documentAttributeSticker) {
                                    document.attributes.remove(a);
                                    attributeSticker = new TL_documentAttributeSticker_layer55();
                                    document.attributes.add(attributeSticker);
                                    attributeSticker.alt = attribute.alt;
                                    if (attribute.stickerset != null) {
                                        if (attribute.stickerset instanceof TL_inputStickerSetShortName) {
                                            name = attribute.stickerset.short_name;
                                        } else {
                                            name = DataQuery.getInstance(this.currentAccount).getStickerSetName(attribute.stickerset.id);
                                        }
                                        if (TextUtils.isEmpty(name)) {
                                            attributeSticker.stickerset = new TL_inputStickerSetEmpty();
                                        } else {
                                            attributeSticker.stickerset = new TL_inputStickerSetShortName();
                                            attributeSticker.stickerset.short_name = name;
                                        }
                                    } else {
                                        attributeSticker.stickerset = new TL_inputStickerSetEmpty();
                                    }
                                } else {
                                    a++;
                                }
                            }
                        }
                    }
                }
                if (MessageObject.isVideoDocument(document) || MessageObject.isRoundVideoDocument(document) || videoEditedInfo != null) {
                    type = 3;
                    if (videoEditedInfo != null) {
                        ve = videoEditedInfo.getString();
                        if (params == null) {
                            params = new HashMap();
                        }
                        params.put("ve", ve);
                    }
                    if (encryptedChat != null) {
                    }
                    newMsg.attachPath = path;
                    a = 0;
                    while (a < document.attributes.size()) {
                        attribute = (DocumentAttribute) document.attributes.get(a);
                        if (attribute instanceof TL_documentAttributeSticker) {
                            a++;
                        } else {
                            document.attributes.remove(a);
                            attributeSticker = new TL_documentAttributeSticker_layer55();
                            document.attributes.add(attributeSticker);
                            attributeSticker.alt = attribute.alt;
                            if (attribute.stickerset != null) {
                                attributeSticker.stickerset = new TL_inputStickerSetEmpty();
                            } else {
                                if (attribute.stickerset instanceof TL_inputStickerSetShortName) {
                                    name = DataQuery.getInstance(this.currentAccount).getStickerSetName(attribute.stickerset.id);
                                } else {
                                    name = attribute.stickerset.short_name;
                                }
                                if (TextUtils.isEmpty(name)) {
                                    attributeSticker.stickerset = new TL_inputStickerSetEmpty();
                                } else {
                                    attributeSticker.stickerset = new TL_inputStickerSetShortName();
                                    attributeSticker.stickerset.short_name = name;
                                }
                            }
                        }
                    }
                } else {
                    if (MessageObject.isVoiceDocument(document)) {
                        type = 8;
                    } else {
                        type = 7;
                    }
                    if (videoEditedInfo != null) {
                        ve = videoEditedInfo.getString();
                        if (params == null) {
                            params = new HashMap();
                        }
                        params.put("ve", ve);
                    }
                    if (encryptedChat != null) {
                    }
                    newMsg.attachPath = path;
                    a = 0;
                    while (a < document.attributes.size()) {
                        attribute = (DocumentAttribute) document.attributes.get(a);
                        if (attribute instanceof TL_documentAttributeSticker) {
                            document.attributes.remove(a);
                            attributeSticker = new TL_documentAttributeSticker_layer55();
                            document.attributes.add(attributeSticker);
                            attributeSticker.alt = attribute.alt;
                            if (attribute.stickerset != null) {
                                if (attribute.stickerset instanceof TL_inputStickerSetShortName) {
                                    name = attribute.stickerset.short_name;
                                } else {
                                    name = DataQuery.getInstance(this.currentAccount).getStickerSetName(attribute.stickerset.id);
                                }
                                if (TextUtils.isEmpty(name)) {
                                    attributeSticker.stickerset = new TL_inputStickerSetShortName();
                                    attributeSticker.stickerset.short_name = name;
                                } else {
                                    attributeSticker.stickerset = new TL_inputStickerSetEmpty();
                                }
                            } else {
                                attributeSticker.stickerset = new TL_inputStickerSetEmpty();
                            }
                        } else {
                            a++;
                        }
                    }
                }
            }
            if (!(entities == null || entities.isEmpty())) {
                newMsg.entities = entities;
                newMsg.flags |= 128;
            }
            if (caption != null) {
                newMsg.message = caption;
            } else if (newMsg.message == null) {
                newMsg.message = TtmlNode.ANONYMOUS_REGION_ID;
            }
            if (newMsg.attachPath == null) {
                newMsg.attachPath = TtmlNode.ANONYMOUS_REGION_ID;
            }
            int newMessageId = UserConfig.getInstance(this.currentAccount).getNewMessageId();
            newMsg.id = newMessageId;
            newMsg.local_id = newMessageId;
            newMsg.out = true;
            if (!isChannel || sendToPeer == null) {
                newMsg.from_id = UserConfig.getInstance(this.currentAccount).getClientUserId();
                newMsg.flags |= 256;
            } else {
                newMsg.from_id = -sendToPeer.channel_id;
            }
            UserConfig.getInstance(this.currentAccount).saveConfig(false);
            if (newMsg.random_id == 0) {
                newMsg.random_id = getNextRandomId();
            }
            if (params != null) {
                if (params.containsKey("bot")) {
                    if (encryptedChat != null) {
                        newMsg.via_bot_name = (String) params.get("bot_name");
                        if (newMsg.via_bot_name == null) {
                            newMsg.via_bot_name = TtmlNode.ANONYMOUS_REGION_ID;
                        }
                    } else {
                        newMsg.via_bot_id = Utilities.parseInt((String) params.get("bot")).intValue();
                    }
                    newMsg.flags |= 2048;
                }
            }
            newMsg.params = params;
            if (retryMessageObject == null || !retryMessageObject.resendAsIs) {
                newMsg.date = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
                if (sendToPeer instanceof TL_inputPeerChannel) {
                    if (isChannel) {
                        newMsg.views = 1;
                        newMsg.flags |= 1024;
                    }
                    chat = MessagesController.getInstance(this.currentAccount).getChat(Integer.valueOf(sendToPeer.channel_id));
                    if (chat != null) {
                        if (chat.megagroup) {
                            newMsg.flags |= Integer.MIN_VALUE;
                            newMsg.unread = true;
                        } else {
                            newMsg.post = true;
                            if (chat.signatures) {
                                newMsg.from_id = UserConfig.getInstance(this.currentAccount).getClientUserId();
                            }
                        }
                    }
                } else {
                    newMsg.unread = true;
                }
            }
            newMsg.flags |= 512;
            newMsg.dialog_id = peer;
            if (reply_to_msg != null) {
                if (encryptedChat == null || reply_to_msg.messageOwner.random_id == 0) {
                    newMsg.flags |= 8;
                } else {
                    newMsg.reply_to_random_id = reply_to_msg.messageOwner.random_id;
                    newMsg.flags |= 8;
                }
                newMsg.reply_to_msg_id = reply_to_msg.getId();
            }
            if (replyMarkup != null && encryptedChat == null) {
                newMsg.flags |= 64;
                newMsg.reply_markup = replyMarkup;
            }
            if (lower_id == 0) {
                newMsg.to_id = new TL_peerUser();
                if (encryptedChat.participant_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                    newMsg.to_id.user_id = encryptedChat.admin_id;
                } else {
                    newMsg.to_id.user_id = encryptedChat.participant_id;
                }
                if (ttl != 0) {
                    newMsg.ttl = ttl;
                } else {
                    newMsg.ttl = encryptedChat.ttl;
                    if (!(newMsg.ttl == 0 || newMsg.media == null)) {
                        newMsg.media.ttl_seconds = newMsg.ttl;
                        messageMedia = newMsg.media;
                        messageMedia.flags |= 4;
                    }
                }
                if (!(newMsg.ttl == 0 || newMsg.media.document == null)) {
                    int duration;
                    if (MessageObject.isVoiceMessage(newMsg)) {
                        duration = 0;
                        for (a = 0; a < newMsg.media.document.attributes.size(); a++) {
                            attribute = (DocumentAttribute) newMsg.media.document.attributes.get(a);
                            if (attribute instanceof TL_documentAttributeAudio) {
                                duration = attribute.duration;
                                break;
                            }
                        }
                        newMsg.ttl = Math.max(newMsg.ttl, duration + 1);
                    } else if (MessageObject.isVideoMessage(newMsg) || MessageObject.isRoundVideoMessage(newMsg)) {
                        duration = 0;
                        for (a = 0; a < newMsg.media.document.attributes.size(); a++) {
                            attribute = (DocumentAttribute) newMsg.media.document.attributes.get(a);
                            if (attribute instanceof TL_documentAttributeVideo) {
                                duration = attribute.duration;
                                break;
                            }
                        }
                        newMsg.ttl = Math.max(newMsg.ttl, duration + 1);
                    }
                }
            } else if (high_id != 1) {
                newMsg.to_id = MessagesController.getInstance(this.currentAccount).getPeer(lower_id);
                if (lower_id > 0) {
                    User sendToUser = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(lower_id));
                    if (sendToUser == null) {
                        processSentMessage(newMsg.id);
                        return;
                    } else if (sendToUser.bot) {
                        newMsg.unread = false;
                    }
                }
            } else if (this.currentChatInfo == null) {
                MessagesStorage.getInstance(this.currentAccount).markMessageAsSendError(newMsg);
                NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.messageSendError, Integer.valueOf(newMsg.id));
                processSentMessage(newMsg.id);
                return;
            } else {
                ArrayList<InputUser> sendToPeers2 = new ArrayList();
                try {
                    Iterator it = this.currentChatInfo.participants.participants.iterator();
                    while (it.hasNext()) {
                        InputUser peerUser = MessagesController.getInstance(this.currentAccount).getInputUser(MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(((ChatParticipant) it.next()).user_id)));
                        if (peerUser != null) {
                            sendToPeers2.add(peerUser);
                        }
                    }
                    newMsg.to_id = new TL_peerChat();
                    newMsg.to_id.chat_id = lower_id;
                    sendToPeers = sendToPeers2;
                } catch (Exception e5) {
                    e = e5;
                    sendToPeers = sendToPeers2;
                    newMsgObj = null;
                    FileLog.e(e);
                    MessagesStorage.getInstance(this.currentAccount).markMessageAsSendError(newMsg);
                    if (newMsgObj != null) {
                        newMsgObj.messageOwner.send_state = 2;
                    }
                    NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.messageSendError, Integer.valueOf(newMsg.id));
                    processSentMessage(newMsg.id);
                }
            }
            if (high_id != 1) {
                if (MessageObject.isVoiceMessage(newMsg) || MessageObject.isRoundVideoMessage(newMsg)) {
                    newMsg.media_unread = true;
                }
            }
            newMsg.send_state = 1;
            newMsgObj = new MessageObject(this.currentAccount, newMsg, true);
            try {
                DelayedMessage delayedMessage;
                DelayedMessage delayedMessage2;
                DelayedMessage delayedMessage3;
                newMsgObj.replyMessageObject = reply_to_msg;
                if (!newMsgObj.isForwarded() && ((newMsgObj.type == 3 || videoEditedInfo != null || newMsgObj.type == 2) && !TextUtils.isEmpty(newMsg.attachPath))) {
                    newMsgObj.attachPathExists = true;
                }
                if (newMsgObj.videoEditedInfo != null && videoEditedInfo == null) {
                    videoEditedInfo = newMsgObj.videoEditedInfo;
                }
                long groupId = 0;
                boolean isFinalGroupMedia = false;
                if (params != null) {
                    String groupIdStr = (String) params.get("groupId");
                    if (groupIdStr != null) {
                        groupId = Utilities.parseLong(groupIdStr).longValue();
                        newMsg.grouped_id = groupId;
                        newMsg.flags |= 131072;
                    }
                    isFinalGroupMedia = params.get("final") != null;
                }
                if (groupId == 0) {
                    ArrayList<MessageObject> objArr = new ArrayList();
                    objArr.add(newMsgObj);
                    ArrayList arr = new ArrayList();
                    arr.add(newMsg);
                    MessagesStorage.getInstance(this.currentAccount).putMessages(arr, false, true, false, 0);
                    MessagesController.getInstance(this.currentAccount).updateInterfaceWithMessages(peer, objArr);
                    NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
                    delayedMessage = null;
                } else {
                    ArrayList<DelayedMessage> arrayList = (ArrayList) this.delayedMessages.get("group_" + groupId);
                    if (arrayList != null) {
                        delayedMessage = (DelayedMessage) arrayList.get(0);
                    } else {
                        delayedMessage = null;
                    }
                    if (delayedMessage == null) {
                        delayedMessage2 = new DelayedMessage(peer);
                        delayedMessage2.type = 4;
                        delayedMessage2.groupId = groupId;
                        delayedMessage2.messageObjects = new ArrayList();
                        delayedMessage2.messages = new ArrayList();
                        delayedMessage2.originalPaths = new ArrayList();
                        delayedMessage2.extraHashMap = new HashMap();
                        delayedMessage2.encryptedChat = encryptedChat;
                    } else {
                        delayedMessage3 = delayedMessage;
                    }
                    if (isFinalGroupMedia) {
                        delayedMessage3.finalGroupMessage = newMsg.id;
                    }
                    delayedMessage = delayedMessage3;
                }
                try {
                    if (BuildVars.LOGS_ENABLED && sendToPeer != null) {
                        FileLog.d("send message user_id = " + sendToPeer.user_id + " chat_id = " + sendToPeer.chat_id + " channel_id = " + sendToPeer.channel_id + " access_hash = " + sendToPeer.access_hash);
                    }
                    TL_decryptedMessage reqSend;
                    ArrayList<Long> random_ids;
                    if (type == 0 || !(type != 9 || message == null || encryptedChat == null)) {
                        if (encryptedChat != null) {
                            if (AndroidUtilities.getPeerLayerVersion(encryptedChat.layer) >= 73) {
                                reqSend = new TL_decryptedMessage();
                            } else {
                                reqSend = new TL_decryptedMessage_layer45();
                            }
                            reqSend.ttl = newMsg.ttl;
                            if (!(entities == null || entities.isEmpty())) {
                                reqSend.entities = entities;
                                reqSend.flags |= 128;
                            }
                            if (newMsg.reply_to_random_id != 0) {
                                reqSend.reply_to_random_id = newMsg.reply_to_random_id;
                                reqSend.flags |= 8;
                            }
                            if (params != null) {
                                if (params.get("bot_name") != null) {
                                    reqSend.via_bot_name = (String) params.get("bot_name");
                                    reqSend.flags |= 2048;
                                }
                            }
                            reqSend.random_id = newMsg.random_id;
                            reqSend.message = message;
                            if (webPage == null || webPage.url == null) {
                                reqSend.media = new TL_decryptedMessageMediaEmpty();
                            } else {
                                reqSend.media = new TL_decryptedMessageMediaWebPage();
                                reqSend.media.url = webPage.url;
                                reqSend.flags |= 512;
                            }
                            SecretChatHelper.getInstance(this.currentAccount).performSendEncryptedRequest(reqSend, newMsgObj.messageOwner, encryptedChat, null, null, newMsgObj);
                            if (retryMessageObject == null) {
                                DataQuery.getInstance(this.currentAccount).cleanDraft(peer, false);
                            }
                            delayedMessage3 = delayedMessage;
                        } else if (sendToPeers != null) {
                            TL_messages_sendBroadcast reqSend2 = new TL_messages_sendBroadcast();
                            random_ids = new ArrayList();
                            for (a = 0; a < sendToPeers.size(); a++) {
                                random_ids.add(Long.valueOf(Utilities.random.nextLong()));
                            }
                            reqSend2.message = message;
                            reqSend2.contacts = sendToPeers;
                            reqSend2.media = new TL_inputMediaEmpty();
                            reqSend2.random_id = random_ids;
                            performSendMessageRequest(reqSend2, newMsgObj, null);
                            delayedMessage3 = delayedMessage;
                        } else {
                            TL_messages_sendMessage reqSend3 = new TL_messages_sendMessage();
                            reqSend3.message = message;
                            reqSend3.clear_draft = retryMessageObject == null;
                            if (newMsg.to_id instanceof TL_peerChannel) {
                                reqSend3.silent = MessagesController.getNotificationsSettings(this.currentAccount).getBoolean("silent_" + peer, false);
                            }
                            reqSend3.peer = sendToPeer;
                            reqSend3.random_id = newMsg.random_id;
                            if (newMsg.reply_to_msg_id != 0) {
                                reqSend3.flags |= 1;
                                reqSend3.reply_to_msg_id = newMsg.reply_to_msg_id;
                            }
                            if (!searchLinks) {
                                reqSend3.no_webpage = true;
                            }
                            if (!(entities == null || entities.isEmpty())) {
                                reqSend3.entities = entities;
                                reqSend3.flags |= 8;
                            }
                            performSendMessageRequest(reqSend3, newMsgObj, null);
                            if (retryMessageObject == null) {
                                DataQuery.getInstance(this.currentAccount).cleanDraft(peer, false);
                            }
                            delayedMessage3 = delayedMessage;
                        }
                    } else if ((type < 1 || type > 3) && ((type < 5 || type > 8) && (type != 9 || encryptedChat == null))) {
                        if (type == 4) {
                            TL_messages_forwardMessages reqSend4 = new TL_messages_forwardMessages();
                            reqSend4.to_peer = sendToPeer;
                            reqSend4.with_my_score = retryMessageObject.messageOwner.with_my_score;
                            if (retryMessageObject.messageOwner.ttl != 0) {
                                chat = MessagesController.getInstance(this.currentAccount).getChat(Integer.valueOf(-retryMessageObject.messageOwner.ttl));
                                reqSend4.from_peer = new TL_inputPeerChannel();
                                reqSend4.from_peer.channel_id = -retryMessageObject.messageOwner.ttl;
                                if (chat != null) {
                                    reqSend4.from_peer.access_hash = chat.access_hash;
                                }
                            } else {
                                reqSend4.from_peer = new TL_inputPeerEmpty();
                            }
                            if (retryMessageObject.messageOwner.to_id instanceof TL_peerChannel) {
                                reqSend4.silent = MessagesController.getNotificationsSettings(this.currentAccount).getBoolean("silent_" + peer, false);
                            }
                            reqSend4.random_id.add(Long.valueOf(newMsg.random_id));
                            if (retryMessageObject.getId() >= 0) {
                                reqSend4.id.add(Integer.valueOf(retryMessageObject.getId()));
                            } else if (retryMessageObject.messageOwner.fwd_msg_id != 0) {
                                reqSend4.id.add(Integer.valueOf(retryMessageObject.messageOwner.fwd_msg_id));
                            } else if (retryMessageObject.messageOwner.fwd_from != null) {
                                reqSend4.id.add(Integer.valueOf(retryMessageObject.messageOwner.fwd_from.channel_post));
                            }
                            performSendMessageRequest(reqSend4, newMsgObj, null);
                            delayedMessage3 = delayedMessage;
                            return;
                        }
                        if (type == 9) {
                            reqSend = new TL_messages_sendInlineBotResult();
                            reqSend.peer = sendToPeer;
                            reqSend.random_id = newMsg.random_id;
                            if (newMsg.reply_to_msg_id != 0) {
                                reqSend.flags |= 1;
                                reqSend.reply_to_msg_id = newMsg.reply_to_msg_id;
                            }
                            if (newMsg.to_id instanceof TL_peerChannel) {
                                reqSend.silent = MessagesController.getNotificationsSettings(this.currentAccount).getBoolean("silent_" + peer, false);
                            }
                            reqSend.query_id = Utilities.parseLong((String) params.get("query_id")).longValue();
                            reqSend.id = (String) params.get(TtmlNode.ATTR_ID);
                            if (retryMessageObject == null) {
                                reqSend.clear_draft = true;
                                DataQuery.getInstance(this.currentAccount).cleanDraft(peer, false);
                            }
                            performSendMessageRequest(reqSend, newMsgObj, null);
                        }
                        delayedMessage3 = delayedMessage;
                    } else if (encryptedChat == null) {
                        TLObject reqSend5;
                        InputMedia inputMedia = null;
                        if (type == 1) {
                            if (location instanceof TL_messageMediaVenue) {
                                inputMedia = new TL_inputMediaVenue();
                                inputMedia.address = location.address;
                                inputMedia.title = location.title;
                                inputMedia.provider = location.provider;
                                inputMedia.venue_id = location.venue_id;
                                inputMedia.venue_type = TtmlNode.ANONYMOUS_REGION_ID;
                            } else if (location instanceof TL_messageMediaGeoLive) {
                                inputMedia = new TL_inputMediaGeoLive();
                                inputMedia.period = location.period;
                            } else {
                                inputMedia = new TL_inputMediaGeoPoint();
                            }
                            inputMedia.geo_point = new TL_inputGeoPoint();
                            inputMedia.geo_point.lat = location.geo.lat;
                            inputMedia.geo_point._long = location.geo._long;
                            delayedMessage3 = delayedMessage;
                        } else if (type == 2 || (type == 9 && photo != null)) {
                            if (photo.access_hash == 0) {
                                inputMedia = new TL_inputMediaUploadedPhoto();
                                if (ttl != 0) {
                                    inputMedia.ttl_seconds = ttl;
                                    newMsg.ttl = ttl;
                                    inputMedia.flags |= 2;
                                }
                                if (params != null) {
                                    String masks = (String) params.get("masks");
                                    if (masks != null) {
                                        AbstractSerializedData serializedData = new SerializedData(Utilities.hexToBytes(masks));
                                        int count = serializedData.readInt32(false);
                                        for (a = 0; a < count; a++) {
                                            inputMedia.stickers.add(InputDocument.TLdeserialize(serializedData, serializedData.readInt32(false), false));
                                        }
                                        inputMedia.flags |= 1;
                                        serializedData.cleanup();
                                    }
                                }
                                if (delayedMessage == null) {
                                    delayedMessage2 = new DelayedMessage(peer);
                                    delayedMessage2.type = 0;
                                    delayedMessage2.obj = newMsgObj;
                                    delayedMessage2.originalPath = originalPath;
                                } else {
                                    delayedMessage3 = delayedMessage;
                                }
                                if (path != null && path.length() > 0) {
                                    if (path.startsWith("http")) {
                                        delayedMessage3.httpLocation = path;
                                    }
                                }
                                delayedMessage3.location = ((PhotoSize) photo.sizes.get(photo.sizes.size() - 1)).location;
                            } else {
                                media = new TL_inputMediaPhoto();
                                media.id = new TL_inputPhoto();
                                media.id.id = photo.id;
                                media.id.access_hash = photo.access_hash;
                                inputMedia = media;
                                delayedMessage3 = delayedMessage;
                            }
                        } else if (type == 3) {
                            if (document2.access_hash == 0) {
                                inputMedia = new TL_inputMediaUploadedDocument();
                                inputMedia.mime_type = document2.mime_type;
                                inputMedia.attributes = document2.attributes;
                                if (!MessageObject.isRoundVideoDocument(document2) && (videoEditedInfo == null || !(videoEditedInfo.muted || videoEditedInfo.roundVideo))) {
                                    inputMedia.nosound_video = true;
                                }
                                if (ttl != 0) {
                                    inputMedia.ttl_seconds = ttl;
                                    newMsg.ttl = ttl;
                                    inputMedia.flags |= 2;
                                }
                                if (delayedMessage == null) {
                                    delayedMessage2 = new DelayedMessage(peer);
                                    delayedMessage2.type = 1;
                                    delayedMessage2.obj = newMsgObj;
                                    delayedMessage2.originalPath = originalPath;
                                } else {
                                    delayedMessage3 = delayedMessage;
                                }
                                delayedMessage3.location = document2.thumb.location;
                                delayedMessage3.videoEditedInfo = videoEditedInfo;
                            } else {
                                media = new TL_inputMediaDocument();
                                media.id = new TL_inputDocument();
                                media.id.id = document2.id;
                                media.id.access_hash = document2.access_hash;
                                inputMedia = media;
                                delayedMessage3 = delayedMessage;
                            }
                        } else if (type == 6) {
                            inputMedia = new TL_inputMediaContact();
                            inputMedia.phone_number = user.phone;
                            inputMedia.first_name = user.first_name;
                            inputMedia.last_name = user.last_name;
                            if (user.restriction_reason == null || !user.restriction_reason.startsWith("BEGIN:VCARD")) {
                                inputMedia.vcard = TtmlNode.ANONYMOUS_REGION_ID;
                                delayedMessage3 = delayedMessage;
                            } else {
                                inputMedia.vcard = user.restriction_reason;
                                delayedMessage3 = delayedMessage;
                            }
                        } else if (type == 7 || type == 9) {
                            if (document2.access_hash == 0) {
                                if (encryptedChat == null && originalPath != null && originalPath.length() > 0) {
                                    if (originalPath.startsWith("http") && params != null) {
                                        inputMedia = new TL_inputMediaGifExternal();
                                        String[] args = ((String) params.get(UpdateFragment.FRAGMENT_URL)).split("\\|");
                                        if (args.length == 2) {
                                            ((TL_inputMediaGifExternal) inputMedia).url = args[0];
                                            inputMedia.q = args[1];
                                        }
                                        delayedMessage3 = delayedMessage;
                                        inputMedia.mime_type = document2.mime_type;
                                        inputMedia.attributes = document2.attributes;
                                    }
                                }
                                inputMedia = new TL_inputMediaUploadedDocument();
                                if (ttl != 0) {
                                    inputMedia.ttl_seconds = ttl;
                                    newMsg.ttl = ttl;
                                    inputMedia.flags |= 2;
                                }
                                delayedMessage2 = new DelayedMessage(peer);
                                delayedMessage2.originalPath = originalPath;
                                delayedMessage2.type = 2;
                                delayedMessage2.obj = newMsgObj;
                                delayedMessage2.location = document2.thumb.location;
                                inputMedia.mime_type = document2.mime_type;
                                inputMedia.attributes = document2.attributes;
                            } else {
                                media = new TL_inputMediaDocument();
                                media.id = new TL_inputDocument();
                                media.id.id = document2.id;
                                media.id.access_hash = document2.access_hash;
                                inputMedia = media;
                                delayedMessage3 = delayedMessage;
                            }
                        } else if (type != 8) {
                            delayedMessage3 = delayedMessage;
                        } else if (document2.access_hash == 0) {
                            inputMedia = new TL_inputMediaUploadedDocument();
                            inputMedia.mime_type = document2.mime_type;
                            inputMedia.attributes = document2.attributes;
                            if (ttl != 0) {
                                inputMedia.ttl_seconds = ttl;
                                newMsg.ttl = ttl;
                                inputMedia.flags |= 2;
                            }
                            delayedMessage2 = new DelayedMessage(peer);
                            delayedMessage2.type = 3;
                            delayedMessage2.obj = newMsgObj;
                        } else {
                            media = new TL_inputMediaDocument();
                            media.id = new TL_inputDocument();
                            media.id.id = document2.id;
                            media.id.access_hash = document2.access_hash;
                            inputMedia = media;
                            delayedMessage3 = delayedMessage;
                        }
                        if (sendToPeers != null) {
                            request = new TL_messages_sendBroadcast();
                            random_ids = new ArrayList();
                            for (a = 0; a < sendToPeers.size(); a++) {
                                random_ids.add(Long.valueOf(Utilities.random.nextLong()));
                            }
                            request.contacts = sendToPeers;
                            request.media = inputMedia;
                            request.random_id = random_ids;
                            request.message = TtmlNode.ANONYMOUS_REGION_ID;
                            if (delayedMessage3 != null) {
                                delayedMessage3.sendRequest = request;
                            }
                            reqSend5 = request;
                            if (retryMessageObject == null) {
                                DataQuery.getInstance(this.currentAccount).cleanDraft(peer, false);
                            }
                        } else if (groupId != 0) {
                            if (delayedMessage3.sendRequest != null) {
                                request = (TL_messages_sendMultiMedia) delayedMessage3.sendRequest;
                            } else {
                                request = new TL_messages_sendMultiMedia();
                                request.peer = sendToPeer;
                                if (newMsg.to_id instanceof TL_peerChannel) {
                                    request.silent = MessagesController.getNotificationsSettings(this.currentAccount).getBoolean("silent_" + peer, false);
                                }
                                if (newMsg.reply_to_msg_id != 0) {
                                    request.flags |= 1;
                                    request.reply_to_msg_id = newMsg.reply_to_msg_id;
                                }
                                delayedMessage3.sendRequest = request;
                            }
                            delayedMessage3.messageObjects.add(newMsgObj);
                            delayedMessage3.messages.add(newMsg);
                            delayedMessage3.originalPaths.add(originalPath);
                            TL_inputSingleMedia inputSingleMedia = new TL_inputSingleMedia();
                            inputSingleMedia.random_id = newMsg.random_id;
                            inputSingleMedia.media = inputMedia;
                            inputSingleMedia.message = caption;
                            if (!(entities == null || entities.isEmpty())) {
                                inputSingleMedia.entities = entities;
                                inputSingleMedia.flags |= 1;
                            }
                            request.multi_media.add(inputSingleMedia);
                            reqSend5 = request;
                        } else {
                            request = new TL_messages_sendMedia();
                            request.peer = sendToPeer;
                            if (newMsg.to_id instanceof TL_peerChannel) {
                                request.silent = MessagesController.getNotificationsSettings(this.currentAccount).getBoolean("silent_" + peer, false);
                            }
                            if (newMsg.reply_to_msg_id != 0) {
                                request.flags |= 1;
                                request.reply_to_msg_id = newMsg.reply_to_msg_id;
                            }
                            request.random_id = newMsg.random_id;
                            request.media = inputMedia;
                            request.message = caption;
                            if (!(entities == null || entities.isEmpty())) {
                                request.entities = entities;
                                request.flags |= 8;
                            }
                            if (delayedMessage3 != null) {
                                delayedMessage3.sendRequest = request;
                            }
                            reqSend5 = request;
                        }
                        if (groupId != 0) {
                            performSendDelayedMessage(delayedMessage3);
                        } else if (type == 1) {
                            performSendMessageRequest(reqSend5, newMsgObj, null);
                        } else if (type == 2) {
                            if (photo.access_hash == 0) {
                                performSendDelayedMessage(delayedMessage3);
                            } else {
                                performSendMessageRequest(reqSend5, newMsgObj, null, null, true);
                            }
                        } else if (type == 3) {
                            if (document2.access_hash == 0) {
                                performSendDelayedMessage(delayedMessage3);
                            } else {
                                performSendMessageRequest(reqSend5, newMsgObj, null);
                            }
                        } else if (type == 6) {
                            performSendMessageRequest(reqSend5, newMsgObj, null);
                        } else if (type == 7) {
                            if (document2.access_hash != 0 || delayedMessage3 == null) {
                                performSendMessageRequest(reqSend5, newMsgObj, originalPath);
                            } else {
                                performSendDelayedMessage(delayedMessage3);
                            }
                        } else if (type != 8) {
                        } else {
                            if (document2.access_hash == 0) {
                                performSendDelayedMessage(delayedMessage3);
                            } else {
                                performSendMessageRequest(reqSend5, newMsgObj, null);
                            }
                        }
                    } else {
                        DecryptedMessage reqSend6;
                        TL_inputEncryptedFile encryptedFile;
                        if (AndroidUtilities.getPeerLayerVersion(encryptedChat.layer) >= 73) {
                            reqSend6 = new TL_decryptedMessage();
                            if (groupId != 0) {
                                reqSend6.grouped_id = groupId;
                                reqSend6.flags |= 131072;
                            }
                        } else {
                            reqSend6 = new TL_decryptedMessage_layer45();
                        }
                        reqSend6.ttl = newMsg.ttl;
                        if (!(entities == null || entities.isEmpty())) {
                            reqSend6.entities = entities;
                            reqSend6.flags |= 128;
                        }
                        if (newMsg.reply_to_random_id != 0) {
                            reqSend6.reply_to_random_id = newMsg.reply_to_random_id;
                            reqSend6.flags |= 8;
                        }
                        reqSend6.flags |= 512;
                        if (params != null) {
                            if (params.get("bot_name") != null) {
                                reqSend6.via_bot_name = (String) params.get("bot_name");
                                reqSend6.flags |= 2048;
                            }
                        }
                        reqSend6.random_id = newMsg.random_id;
                        reqSend6.message = TtmlNode.ANONYMOUS_REGION_ID;
                        if (type == 1) {
                            if (location instanceof TL_messageMediaVenue) {
                                reqSend6.media = new TL_decryptedMessageMediaVenue();
                                reqSend6.media.address = location.address;
                                reqSend6.media.title = location.title;
                                reqSend6.media.provider = location.provider;
                                reqSend6.media.venue_id = location.venue_id;
                            } else {
                                reqSend6.media = new TL_decryptedMessageMediaGeoPoint();
                            }
                            reqSend6.media.lat = location.geo.lat;
                            reqSend6.media._long = location.geo._long;
                            SecretChatHelper.getInstance(this.currentAccount).performSendEncryptedRequest(reqSend6, newMsgObj.messageOwner, encryptedChat, null, null, newMsgObj);
                            delayedMessage3 = delayedMessage;
                        } else {
                            if (type == 2 || (type == 9 && photo != null)) {
                                PhotoSize small = (PhotoSize) photo.sizes.get(0);
                                PhotoSize big = (PhotoSize) photo.sizes.get(photo.sizes.size() - 1);
                                ImageLoader.fillPhotoSizeWithBytes(small);
                                reqSend6.media = new TL_decryptedMessageMediaPhoto();
                                reqSend6.media.caption = caption;
                                if (small.bytes != null) {
                                    ((TL_decryptedMessageMediaPhoto) reqSend6.media).thumb = small.bytes;
                                } else {
                                    ((TL_decryptedMessageMediaPhoto) reqSend6.media).thumb = new byte[0];
                                }
                                reqSend6.media.thumb_h = small.h;
                                reqSend6.media.thumb_w = small.w;
                                reqSend6.media.w = big.w;
                                reqSend6.media.h = big.h;
                                reqSend6.media.size = big.size;
                                if (big.location.key == null || groupId != 0) {
                                    if (delayedMessage == null) {
                                        delayedMessage2 = new DelayedMessage(peer);
                                        delayedMessage2.encryptedChat = encryptedChat;
                                        delayedMessage2.type = 0;
                                        delayedMessage2.originalPath = originalPath;
                                        delayedMessage2.sendEncryptedRequest = reqSend6;
                                        delayedMessage2.obj = newMsgObj;
                                    } else {
                                        delayedMessage3 = delayedMessage;
                                    }
                                    if (!TextUtils.isEmpty(path)) {
                                        if (path.startsWith("http")) {
                                            delayedMessage3.httpLocation = path;
                                            if (groupId == 0) {
                                                performSendDelayedMessage(delayedMessage3);
                                            }
                                        }
                                    }
                                    delayedMessage3.location = ((PhotoSize) photo.sizes.get(photo.sizes.size() - 1)).location;
                                    if (groupId == 0) {
                                        performSendDelayedMessage(delayedMessage3);
                                    }
                                } else {
                                    encryptedFile = new TL_inputEncryptedFile();
                                    encryptedFile.id = big.location.volume_id;
                                    encryptedFile.access_hash = big.location.secret;
                                    reqSend6.media.key = big.location.key;
                                    reqSend6.media.iv = big.location.iv;
                                    SecretChatHelper.getInstance(this.currentAccount).performSendEncryptedRequest(reqSend6, newMsgObj.messageOwner, encryptedChat, encryptedFile, null, newMsgObj);
                                }
                            } else if (type == 3) {
                                ImageLoader.fillPhotoSizeWithBytes(document2.thumb);
                                if (MessageObject.isNewGifDocument(document2) || MessageObject.isRoundVideoDocument(document2)) {
                                    reqSend6.media = new TL_decryptedMessageMediaDocument();
                                    reqSend6.media.attributes = document2.attributes;
                                    if (document2.thumb == null || document2.thumb.bytes == null) {
                                        ((TL_decryptedMessageMediaDocument) reqSend6.media).thumb = new byte[0];
                                    } else {
                                        ((TL_decryptedMessageMediaDocument) reqSend6.media).thumb = document2.thumb.bytes;
                                    }
                                } else {
                                    reqSend6.media = new TL_decryptedMessageMediaVideo();
                                    if (document2.thumb == null || document2.thumb.bytes == null) {
                                        ((TL_decryptedMessageMediaVideo) reqSend6.media).thumb = new byte[0];
                                    } else {
                                        ((TL_decryptedMessageMediaVideo) reqSend6.media).thumb = document2.thumb.bytes;
                                    }
                                }
                                reqSend6.media.caption = caption;
                                reqSend6.media.mime_type = MimeTypes.VIDEO_MP4;
                                reqSend6.media.size = document2.size;
                                for (a = 0; a < document2.attributes.size(); a++) {
                                    attribute = (DocumentAttribute) document2.attributes.get(a);
                                    if (attribute instanceof TL_documentAttributeVideo) {
                                        reqSend6.media.w = attribute.w;
                                        reqSend6.media.h = attribute.h;
                                        reqSend6.media.duration = attribute.duration;
                                        break;
                                    }
                                }
                                reqSend6.media.thumb_h = document2.thumb.h;
                                reqSend6.media.thumb_w = document2.thumb.w;
                                if (document2.key == null || groupId != 0) {
                                    if (delayedMessage == null) {
                                        delayedMessage2 = new DelayedMessage(peer);
                                        delayedMessage2.encryptedChat = encryptedChat;
                                        delayedMessage2.type = 1;
                                        delayedMessage2.sendEncryptedRequest = reqSend6;
                                        delayedMessage2.originalPath = originalPath;
                                        delayedMessage2.obj = newMsgObj;
                                    } else {
                                        delayedMessage3 = delayedMessage;
                                    }
                                    delayedMessage3.videoEditedInfo = videoEditedInfo;
                                    if (groupId == 0) {
                                        performSendDelayedMessage(delayedMessage3);
                                    }
                                } else {
                                    encryptedFile = new TL_inputEncryptedFile();
                                    encryptedFile.id = document2.id;
                                    encryptedFile.access_hash = document2.access_hash;
                                    reqSend6.media.key = document2.key;
                                    reqSend6.media.iv = document2.iv;
                                    SecretChatHelper.getInstance(this.currentAccount).performSendEncryptedRequest(reqSend6, newMsgObj.messageOwner, encryptedChat, encryptedFile, null, newMsgObj);
                                    delayedMessage3 = delayedMessage;
                                }
                            } else if (type == 6) {
                                reqSend6.media = new TL_decryptedMessageMediaContact();
                                reqSend6.media.phone_number = user.phone;
                                reqSend6.media.first_name = user.first_name;
                                reqSend6.media.last_name = user.last_name;
                                reqSend6.media.user_id = user.id;
                                SecretChatHelper.getInstance(this.currentAccount).performSendEncryptedRequest(reqSend6, newMsgObj.messageOwner, encryptedChat, null, null, newMsgObj);
                                delayedMessage3 = delayedMessage;
                            } else if (type == 7 || (type == 9 && document2 != null)) {
                                if (MessageObject.isStickerDocument(document2)) {
                                    reqSend6.media = new TL_decryptedMessageMediaExternalDocument();
                                    reqSend6.media.id = document2.id;
                                    reqSend6.media.date = document2.date;
                                    reqSend6.media.access_hash = document2.access_hash;
                                    reqSend6.media.mime_type = document2.mime_type;
                                    reqSend6.media.size = document2.size;
                                    reqSend6.media.dc_id = document2.dc_id;
                                    reqSend6.media.attributes = document2.attributes;
                                    if (document2.thumb == null) {
                                        ((TL_decryptedMessageMediaExternalDocument) reqSend6.media).thumb = new TL_photoSizeEmpty();
                                        ((TL_decryptedMessageMediaExternalDocument) reqSend6.media).thumb.type = "s";
                                    } else {
                                        ((TL_decryptedMessageMediaExternalDocument) reqSend6.media).thumb = document2.thumb;
                                    }
                                    SecretChatHelper.getInstance(this.currentAccount).performSendEncryptedRequest(reqSend6, newMsgObj.messageOwner, encryptedChat, null, null, newMsgObj);
                                    delayedMessage3 = delayedMessage;
                                } else {
                                    ImageLoader.fillPhotoSizeWithBytes(document2.thumb);
                                    reqSend6.media = new TL_decryptedMessageMediaDocument();
                                    reqSend6.media.attributes = document2.attributes;
                                    reqSend6.media.caption = caption;
                                    if (document2.thumb == null || document2.thumb.bytes == null) {
                                        ((TL_decryptedMessageMediaDocument) reqSend6.media).thumb = new byte[0];
                                        reqSend6.media.thumb_h = 0;
                                        reqSend6.media.thumb_w = 0;
                                    } else {
                                        ((TL_decryptedMessageMediaDocument) reqSend6.media).thumb = document2.thumb.bytes;
                                        reqSend6.media.thumb_h = document2.thumb.h;
                                        reqSend6.media.thumb_w = document2.thumb.w;
                                    }
                                    reqSend6.media.size = document2.size;
                                    reqSend6.media.mime_type = document2.mime_type;
                                    if (document2.key == null) {
                                        delayedMessage2 = new DelayedMessage(peer);
                                        delayedMessage2.originalPath = originalPath;
                                        delayedMessage2.sendEncryptedRequest = reqSend6;
                                        delayedMessage2.type = 2;
                                        delayedMessage2.obj = newMsgObj;
                                        delayedMessage2.encryptedChat = encryptedChat;
                                        if (path != null && path.length() > 0) {
                                            if (path.startsWith("http")) {
                                                delayedMessage2.httpLocation = path;
                                            }
                                        }
                                        performSendDelayedMessage(delayedMessage2);
                                    } else {
                                        encryptedFile = new TL_inputEncryptedFile();
                                        encryptedFile.id = document2.id;
                                        encryptedFile.access_hash = document2.access_hash;
                                        reqSend6.media.key = document2.key;
                                        reqSend6.media.iv = document2.iv;
                                        SecretChatHelper.getInstance(this.currentAccount).performSendEncryptedRequest(reqSend6, newMsgObj.messageOwner, encryptedChat, encryptedFile, null, newMsgObj);
                                        delayedMessage3 = delayedMessage;
                                    }
                                }
                            } else if (type == 8) {
                                delayedMessage2 = new DelayedMessage(peer);
                                delayedMessage2.encryptedChat = encryptedChat;
                                delayedMessage2.sendEncryptedRequest = reqSend6;
                                delayedMessage2.obj = newMsgObj;
                                delayedMessage2.type = 3;
                                reqSend6.media = new TL_decryptedMessageMediaDocument();
                                reqSend6.media.attributes = document2.attributes;
                                reqSend6.media.caption = caption;
                                if (document2.thumb == null || document2.thumb.bytes == null) {
                                    ((TL_decryptedMessageMediaDocument) reqSend6.media).thumb = new byte[0];
                                    reqSend6.media.thumb_h = 0;
                                    reqSend6.media.thumb_w = 0;
                                } else {
                                    ((TL_decryptedMessageMediaDocument) reqSend6.media).thumb = document2.thumb.bytes;
                                    reqSend6.media.thumb_h = document2.thumb.h;
                                    reqSend6.media.thumb_w = document2.thumb.w;
                                }
                                reqSend6.media.mime_type = document2.mime_type;
                                reqSend6.media.size = document2.size;
                                delayedMessage2.originalPath = originalPath;
                                performSendDelayedMessage(delayedMessage2);
                            }
                            delayedMessage3 = delayedMessage;
                        }
                        if (groupId != 0) {
                            TL_messages_sendEncryptedMultiMedia request;
                            if (delayedMessage3.sendEncryptedRequest != null) {
                                request = (TL_messages_sendEncryptedMultiMedia) delayedMessage3.sendEncryptedRequest;
                            } else {
                                request = new TL_messages_sendEncryptedMultiMedia();
                                delayedMessage3.sendEncryptedRequest = request;
                            }
                            delayedMessage3.messageObjects.add(newMsgObj);
                            delayedMessage3.messages.add(newMsg);
                            delayedMessage3.originalPaths.add(originalPath);
                            delayedMessage3.upload = true;
                            request.messages.add(reqSend6);
                            encryptedFile = new TL_inputEncryptedFile();
                            encryptedFile.id = type == 3 ? 1 : 0;
                            request.files.add(encryptedFile);
                            performSendDelayedMessage(delayedMessage3);
                        }
                        if (retryMessageObject == null) {
                            DataQuery.getInstance(this.currentAccount).cleanDraft(peer, false);
                        }
                    }
                } catch (Exception e6) {
                    e = e6;
                    delayedMessage3 = delayedMessage;
                    FileLog.e(e);
                    MessagesStorage.getInstance(this.currentAccount).markMessageAsSendError(newMsg);
                    if (newMsgObj != null) {
                        newMsgObj.messageOwner.send_state = 2;
                    }
                    NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.messageSendError, Integer.valueOf(newMsg.id));
                    processSentMessage(newMsg.id);
                }
            } catch (Exception e7) {
                e = e7;
                FileLog.e(e);
                MessagesStorage.getInstance(this.currentAccount).markMessageAsSendError(newMsg);
                if (newMsgObj != null) {
                    newMsgObj.messageOwner.send_state = 2;
                }
                NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.messageSendError, Integer.valueOf(newMsg.id));
                processSentMessage(newMsg.id);
            }
        }
    }

    private void performSendDelayedMessage(DelayedMessage message) {
        performSendDelayedMessage(message, -1);
    }

    private void performSendDelayedMessage(DelayedMessage message, int index) {
        String location;
        if (message.type == 0) {
            if (message.httpLocation != null) {
                putToDelayedMessages(message.httpLocation, message);
                ImageLoader.getInstance().loadHttpFile(message.httpLocation, "file", this.currentAccount);
            } else if (message.sendRequest != null) {
                location = FileLoader.getPathToAttach(message.location).toString();
                putToDelayedMessages(location, message);
                FileLoader.getInstance(this.currentAccount).uploadFile(location, false, true, 16777216);
            } else {
                location = FileLoader.getPathToAttach(message.location).toString();
                if (!(message.sendEncryptedRequest == null || message.location.dc_id == 0)) {
                    File file = new File(location);
                    if (!file.exists()) {
                        location = FileLoader.getPathToAttach(message.location, true).toString();
                        file = new File(location);
                    }
                    if (!file.exists()) {
                        putToDelayedMessages(FileLoader.getAttachFileName(message.location), message);
                        FileLoader.getInstance(this.currentAccount).loadFile(message.location, "jpg", 0, 0);
                        return;
                    }
                }
                putToDelayedMessages(location, message);
                FileLoader.getInstance(this.currentAccount).uploadFile(location, true, true, 16777216);
            }
        } else if (message.type == 1) {
            if (message.videoEditedInfo == null || !message.videoEditedInfo.needConvert()) {
                if (message.videoEditedInfo != null) {
                    if (message.videoEditedInfo.file != null) {
                        if (message.sendRequest instanceof TL_messages_sendMedia) {
                            media = ((TL_messages_sendMedia) message.sendRequest).media;
                        } else if (message.sendRequest instanceof TL_messages_editMessage) {
                            media = ((TL_messages_editMessage) message.sendRequest).media;
                        } else {
                            media = ((TL_messages_sendBroadcast) message.sendRequest).media;
                        }
                        media.file = message.videoEditedInfo.file;
                        message.videoEditedInfo.file = null;
                    } else if (message.videoEditedInfo.encryptedFile != null) {
                        TL_decryptedMessage decryptedMessage = message.sendEncryptedRequest;
                        decryptedMessage.media.size = (int) message.videoEditedInfo.estimatedSize;
                        decryptedMessage.media.key = message.videoEditedInfo.key;
                        decryptedMessage.media.iv = message.videoEditedInfo.iv;
                        SecretChatHelper.getInstance(this.currentAccount).performSendEncryptedRequest(decryptedMessage, message.obj.messageOwner, message.encryptedChat, message.videoEditedInfo.encryptedFile, message.originalPath, message.obj);
                        message.videoEditedInfo.encryptedFile = null;
                        return;
                    }
                }
                if (message.sendRequest != null) {
                    if (message.sendRequest instanceof TL_messages_sendMedia) {
                        media = ((TL_messages_sendMedia) message.sendRequest).media;
                    } else if (message.sendRequest instanceof TL_messages_editMessage) {
                        media = ((TL_messages_editMessage) message.sendRequest).media;
                    } else {
                        media = ((TL_messages_sendBroadcast) message.sendRequest).media;
                    }
                    if (media.file == null) {
                        location = message.obj.messageOwner.attachPath;
                        document = message.obj.getDocument();
                        if (location == null) {
                            location = FileLoader.getDirectory(4) + "/" + document.id + ".mp4";
                        }
                        putToDelayedMessages(location, message);
                        if (message.obj.videoEditedInfo == null || !message.obj.videoEditedInfo.needConvert()) {
                            FileLoader.getInstance(this.currentAccount).uploadFile(location, false, false, ConnectionsManager.FileTypeVideo);
                            return;
                        } else {
                            FileLoader.getInstance(this.currentAccount).uploadFile(location, false, false, document.size, ConnectionsManager.FileTypeVideo);
                            return;
                        }
                    }
                    location = FileLoader.getDirectory(4) + "/" + message.location.volume_id + "_" + message.location.local_id + ".jpg";
                    putToDelayedMessages(location, message);
                    FileLoader.getInstance(this.currentAccount).uploadFile(location, false, true, 16777216);
                    return;
                }
                location = message.obj.messageOwner.attachPath;
                document = message.obj.getDocument();
                if (location == null) {
                    location = FileLoader.getDirectory(4) + "/" + document.id + ".mp4";
                }
                if (message.sendEncryptedRequest == null || document.dc_id == 0 || new File(location).exists()) {
                    putToDelayedMessages(location, message);
                    if (message.obj.videoEditedInfo == null || !message.obj.videoEditedInfo.needConvert()) {
                        FileLoader.getInstance(this.currentAccount).uploadFile(location, true, false, ConnectionsManager.FileTypeVideo);
                        return;
                    } else {
                        FileLoader.getInstance(this.currentAccount).uploadFile(location, true, false, document.size, ConnectionsManager.FileTypeVideo);
                        return;
                    }
                }
                putToDelayedMessages(FileLoader.getAttachFileName(document), message);
                FileLoader.getInstance(this.currentAccount).loadFile(document, true, 0);
                return;
            }
            location = message.obj.messageOwner.attachPath;
            document = message.obj.getDocument();
            if (location == null) {
                location = FileLoader.getDirectory(4) + "/" + document.id + ".mp4";
            }
            putToDelayedMessages(location, message);
            MediaController.getInstance().scheduleVideoConvert(message.obj);
        } else if (message.type == 2) {
            if (message.httpLocation != null) {
                putToDelayedMessages(message.httpLocation, message);
                ImageLoader.getInstance().loadHttpFile(message.httpLocation, "gif", this.currentAccount);
            } else if (message.sendRequest != null) {
                if (message.sendRequest instanceof TL_messages_sendMedia) {
                    media = ((TL_messages_sendMedia) message.sendRequest).media;
                } else if (message.sendRequest instanceof TL_messages_editMessage) {
                    media = ((TL_messages_editMessage) message.sendRequest).media;
                } else {
                    media = ((TL_messages_sendBroadcast) message.sendRequest).media;
                }
                if (media.file == null) {
                    boolean z;
                    location = message.obj.messageOwner.attachPath;
                    putToDelayedMessages(location, message);
                    FileLoader instance = FileLoader.getInstance(this.currentAccount);
                    if (message.sendRequest == null) {
                        z = true;
                    } else {
                        z = false;
                    }
                    instance.uploadFile(location, z, false, ConnectionsManager.FileTypeFile);
                } else if (media.thumb == null && message.location != null) {
                    location = FileLoader.getDirectory(4) + "/" + message.location.volume_id + "_" + message.location.local_id + ".jpg";
                    putToDelayedMessages(location, message);
                    FileLoader.getInstance(this.currentAccount).uploadFile(location, false, true, 16777216);
                }
            } else {
                location = message.obj.messageOwner.attachPath;
                document = message.obj.getDocument();
                if (message.sendEncryptedRequest == null || document.dc_id == 0 || new File(location).exists()) {
                    putToDelayedMessages(location, message);
                    FileLoader.getInstance(this.currentAccount).uploadFile(location, true, false, ConnectionsManager.FileTypeFile);
                    return;
                }
                putToDelayedMessages(FileLoader.getAttachFileName(document), message);
                FileLoader.getInstance(this.currentAccount).loadFile(document, true, 0);
            }
        } else if (message.type == 3) {
            location = message.obj.messageOwner.attachPath;
            putToDelayedMessages(location, message);
            FileLoader.getInstance(this.currentAccount).uploadFile(location, message.sendRequest == null, true, ConnectionsManager.FileTypeAudio);
        } else if (message.type == 4) {
            boolean add = index < 0;
            if (message.location != null || message.httpLocation != null || message.upload || index >= 0) {
                if (index < 0) {
                    index = message.messageObjects.size() - 1;
                }
                MessageObject messageObject = (MessageObject) message.messageObjects.get(index);
                if (messageObject.getDocument() != null) {
                    if (message.videoEditedInfo != null) {
                        location = messageObject.messageOwner.attachPath;
                        document = messageObject.getDocument();
                        if (location == null) {
                            location = FileLoader.getDirectory(4) + "/" + document.id + ".mp4";
                        }
                        putToDelayedMessages(location, message);
                        message.extraHashMap.put(messageObject, location);
                        message.extraHashMap.put(location + "_i", messageObject);
                        if (message.location != null) {
                            message.extraHashMap.put(location + "_t", message.location);
                        }
                        MediaController.getInstance().scheduleVideoConvert(messageObject);
                    } else {
                        document = messageObject.getDocument();
                        String documentLocation = messageObject.messageOwner.attachPath;
                        if (documentLocation == null) {
                            documentLocation = FileLoader.getDirectory(4) + "/" + document.id + ".mp4";
                        }
                        if (message.sendRequest != null) {
                            media = ((TL_inputSingleMedia) ((TL_messages_sendMultiMedia) message.sendRequest).multi_media.get(index)).media;
                            if (media.file == null) {
                                putToDelayedMessages(documentLocation, message);
                                message.extraHashMap.put(messageObject, documentLocation);
                                message.extraHashMap.put(documentLocation, media);
                                message.extraHashMap.put(documentLocation + "_i", messageObject);
                                if (message.location != null) {
                                    message.extraHashMap.put(documentLocation + "_t", message.location);
                                }
                                if (messageObject.videoEditedInfo == null || !messageObject.videoEditedInfo.needConvert()) {
                                    FileLoader.getInstance(this.currentAccount).uploadFile(documentLocation, false, false, ConnectionsManager.FileTypeVideo);
                                } else {
                                    FileLoader.getInstance(this.currentAccount).uploadFile(documentLocation, false, false, document.size, ConnectionsManager.FileTypeVideo);
                                }
                            } else {
                                location = FileLoader.getDirectory(4) + "/" + message.location.volume_id + "_" + message.location.local_id + ".jpg";
                                putToDelayedMessages(location, message);
                                message.extraHashMap.put(location + "_o", documentLocation);
                                message.extraHashMap.put(messageObject, location);
                                message.extraHashMap.put(location, media);
                                FileLoader.getInstance(this.currentAccount).uploadFile(location, false, true, 16777216);
                            }
                        } else {
                            TL_messages_sendEncryptedMultiMedia request = (TL_messages_sendEncryptedMultiMedia) message.sendEncryptedRequest;
                            putToDelayedMessages(documentLocation, message);
                            message.extraHashMap.put(messageObject, documentLocation);
                            message.extraHashMap.put(documentLocation, request.files.get(index));
                            message.extraHashMap.put(documentLocation + "_i", messageObject);
                            if (message.location != null) {
                                message.extraHashMap.put(documentLocation + "_t", message.location);
                            }
                            if (messageObject.videoEditedInfo == null || !messageObject.videoEditedInfo.needConvert()) {
                                FileLoader.getInstance(this.currentAccount).uploadFile(documentLocation, true, false, ConnectionsManager.FileTypeVideo);
                            } else {
                                FileLoader.getInstance(this.currentAccount).uploadFile(documentLocation, true, false, document.size, ConnectionsManager.FileTypeVideo);
                            }
                        }
                    }
                    message.videoEditedInfo = null;
                    message.location = null;
                } else if (message.httpLocation != null) {
                    putToDelayedMessages(message.httpLocation, message);
                    message.extraHashMap.put(messageObject, message.httpLocation);
                    message.extraHashMap.put(message.httpLocation, messageObject);
                    ImageLoader.getInstance().loadHttpFile(message.httpLocation, "file", this.currentAccount);
                    message.httpLocation = null;
                } else {
                    TLObject inputMedia;
                    if (message.sendRequest != null) {
                        inputMedia = ((TL_inputSingleMedia) ((TL_messages_sendMultiMedia) message.sendRequest).multi_media.get(index)).media;
                    } else {
                        inputMedia = (TLObject) ((TL_messages_sendEncryptedMultiMedia) message.sendEncryptedRequest).files.get(index);
                    }
                    location = FileLoader.getDirectory(4) + "/" + message.location.volume_id + "_" + message.location.local_id + ".jpg";
                    putToDelayedMessages(location, message);
                    message.extraHashMap.put(location, inputMedia);
                    message.extraHashMap.put(messageObject, location);
                    FileLoader.getInstance(this.currentAccount).uploadFile(location, message.sendEncryptedRequest != null, true, 16777216);
                    message.location = null;
                }
                message.upload = false;
            } else if (!message.messageObjects.isEmpty()) {
                putToSendingMessages(((MessageObject) message.messageObjects.get(message.messageObjects.size() - 1)).messageOwner);
            }
            sendReadyToSendGroup(message, add, true);
        }
    }

    private void uploadMultiMedia(DelayedMessage message, InputMedia inputMedia, InputEncryptedFile inputEncryptedFile, String key) {
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
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new SendMessagesHelper$$Lambda$8(this, inputMedia, message));
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

    final /* synthetic */ void lambda$uploadMultiMedia$19$SendMessagesHelper(InputMedia inputMedia, DelayedMessage message, TLObject response, TL_error error) {
        AndroidUtilities.runOnUIThread(new SendMessagesHelper$$Lambda$51(this, response, inputMedia, message));
    }

    final /* synthetic */ void lambda$null$18$SendMessagesHelper(TLObject response, InputMedia inputMedia, DelayedMessage message) {
        InputMedia newInputMedia = null;
        if (response != null) {
            MessageMedia messageMedia = (MessageMedia) response;
            if ((inputMedia instanceof TL_inputMediaUploadedPhoto) && (messageMedia instanceof TL_messageMediaPhoto)) {
                InputMedia inputMediaPhoto = new TL_inputMediaPhoto();
                inputMediaPhoto.id = new TL_inputPhoto();
                inputMediaPhoto.id.id = messageMedia.photo.id;
                inputMediaPhoto.id.access_hash = messageMedia.photo.access_hash;
                newInputMedia = inputMediaPhoto;
            } else if ((inputMedia instanceof TL_inputMediaUploadedDocument) && (messageMedia instanceof TL_messageMediaDocument)) {
                InputMedia inputMediaDocument = new TL_inputMediaDocument();
                inputMediaDocument.id = new TL_inputDocument();
                inputMediaDocument.id.id = messageMedia.document.id;
                inputMediaDocument.id.access_hash = messageMedia.document.access_hash;
                newInputMedia = inputMediaDocument;
            }
        }
        if (newInputMedia != null) {
            if (inputMedia.ttl_seconds != 0) {
                newInputMedia.ttl_seconds = inputMedia.ttl_seconds;
                newInputMedia.flags |= 1;
            }
            TL_messages_sendMultiMedia req1 = message.sendRequest;
            for (int a = 0; a < req1.multi_media.size(); a++) {
                if (((TL_inputSingleMedia) req1.multi_media.get(a)).media == inputMedia) {
                    ((TL_inputSingleMedia) req1.multi_media.get(a)).media = newInputMedia;
                    break;
                }
            }
            sendReadyToSendGroup(message, false, true);
            return;
        }
        message.markAsError();
    }

    private void sendReadyToSendGroup(DelayedMessage message, boolean add, boolean check) {
        if (message.messageObjects.isEmpty()) {
            message.markAsError();
            return;
        }
        String key = "group_" + message.groupId;
        if (message.finalGroupMessage == ((MessageObject) message.messageObjects.get(message.messageObjects.size() - 1)).getId()) {
            if (add) {
                this.delayedMessages.remove(key);
                MessagesStorage.getInstance(this.currentAccount).putMessages(message.messages, false, true, false, 0);
                MessagesController.getInstance(this.currentAccount).updateInterfaceWithMessages(message.peer, message.messageObjects);
                NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
            }
            int a;
            if (message.sendRequest instanceof TL_messages_sendMultiMedia) {
                TL_messages_sendMultiMedia request = message.sendRequest;
                a = 0;
                while (a < request.multi_media.size()) {
                    InputMedia inputMedia = ((TL_inputSingleMedia) request.multi_media.get(a)).media;
                    if (!(inputMedia instanceof TL_inputMediaUploadedPhoto) && !(inputMedia instanceof TL_inputMediaUploadedDocument)) {
                        a++;
                    } else {
                        return;
                    }
                }
                if (check) {
                    DelayedMessage maxDelayedMessage = findMaxDelayedMessageForMessageId(message.finalGroupMessage, message.peer);
                    if (maxDelayedMessage != null) {
                        maxDelayedMessage.addDelayedRequest(message.sendRequest, message.messageObjects, message.originalPaths);
                        if (message.requests != null) {
                            maxDelayedMessage.requests.addAll(message.requests);
                            return;
                        }
                        return;
                    }
                }
            }
            TL_messages_sendEncryptedMultiMedia request2 = message.sendEncryptedRequest;
            a = 0;
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
        } else if (add) {
            putToDelayedMessages(key, message);
        }
    }

    final /* synthetic */ void lambda$null$20$SendMessagesHelper(String path) {
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.stopEncodingService, path, Integer.valueOf(this.currentAccount));
    }

    final /* synthetic */ void lambda$stopVideoService$21$SendMessagesHelper(String path) {
        AndroidUtilities.runOnUIThread(new SendMessagesHelper$$Lambda$50(this, path));
    }

    protected void stopVideoService(String path) {
        MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new SendMessagesHelper$$Lambda$9(this, path));
    }

    protected void putToSendingMessages(Message message) {
        this.sendingMessages.put(message.id, message);
    }

    protected Message removeFromSendingMessages(int mid) {
        Message message = (Message) this.sendingMessages.get(mid);
        if (message != null) {
            this.sendingMessages.remove(mid);
        }
        return message;
    }

    public boolean isSendingMessage(int mid) {
        return this.sendingMessages.indexOfKey(mid) >= 0;
    }

    private void performSendMessageRequestMulti(TL_messages_sendMultiMedia req, ArrayList<MessageObject> msgObjs, ArrayList<String> originalPaths) {
        for (int a = 0; a < msgObjs.size(); a++) {
            putToSendingMessages(((MessageObject) msgObjs.get(a)).messageOwner);
        }
        ConnectionsManager.getInstance(this.currentAccount).sendRequest((TLObject) req, new SendMessagesHelper$$Lambda$10(this, msgObjs, originalPaths, req), null, 68);
    }

    final /* synthetic */ void lambda$performSendMessageRequestMulti$28$SendMessagesHelper(ArrayList msgObjs, ArrayList originalPaths, TL_messages_sendMultiMedia req, TLObject response, TL_error error) {
        AndroidUtilities.runOnUIThread(new SendMessagesHelper$$Lambda$44(this, error, response, msgObjs, originalPaths, req));
    }

    final /* synthetic */ void lambda$null$27$SendMessagesHelper(TL_error error, TLObject response, ArrayList msgObjs, ArrayList originalPaths, TL_messages_sendMultiMedia req) {
        int i;
        Message newMsgObj;
        boolean isSentError = false;
        if (error == null) {
            SparseArray<Message> newMessages = new SparseArray();
            LongSparseArray<Integer> newIds = new LongSparseArray();
            Updates updates = (Updates) response;
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
                    TL_updateNewMessage newMessage = (TL_updateNewMessage) update;
                    newMessages.put(newMessage.message.id, newMessage.message);
                    Utilities.stageQueue.postRunnable(new SendMessagesHelper$$Lambda$45(this, newMessage));
                    updatesArr.remove(a);
                    a--;
                } else if (update instanceof TL_updateNewChannelMessage) {
                    TL_updateNewChannelMessage newMessage2 = (TL_updateNewChannelMessage) update;
                    newMessages.put(newMessage2.message.id, newMessage2.message);
                    Utilities.stageQueue.postRunnable(new SendMessagesHelper$$Lambda$46(this, newMessage2));
                    updatesArr.remove(a);
                    a--;
                }
                a++;
            }
            for (i = 0; i < msgObjs.size(); i++) {
                MessageObject msgObj = (MessageObject) msgObjs.get(i);
                String originalPath = (String) originalPaths.get(i);
                newMsgObj = msgObj.messageOwner;
                int oldId = newMsgObj.id;
                ArrayList<Message> sentMessages = new ArrayList();
                String attachPath = newMsgObj.attachPath;
                Integer id = (Integer) newIds.get(newMsgObj.random_id);
                if (id == null) {
                    isSentError = true;
                    break;
                }
                Message message = (Message) newMessages.get(id.intValue());
                if (message == null) {
                    isSentError = true;
                    break;
                }
                sentMessages.add(message);
                newMsgObj.id = message.id;
                if ((newMsgObj.flags & Integer.MIN_VALUE) != 0) {
                    message.flags |= Integer.MIN_VALUE;
                }
                long grouped_id = message.grouped_id;
                Integer value = (Integer) MessagesController.getInstance(this.currentAccount).dialogs_read_outbox_max.get(Long.valueOf(message.dialog_id));
                if (value == null) {
                    value = Integer.valueOf(MessagesStorage.getInstance(this.currentAccount).getDialogReadMax(message.out, message.dialog_id));
                    MessagesController.getInstance(this.currentAccount).dialogs_read_outbox_max.put(Long.valueOf(message.dialog_id), value);
                }
                message.unread = value.intValue() < message.id;
                updateMediaPaths(msgObj, message, originalPath, false);
                if (null == null) {
                    StatsController.getInstance(this.currentAccount).incrementSentItemsCount(ConnectionsManager.getCurrentNetworkType(), 1, 1);
                    newMsgObj.send_state = 0;
                    NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.messageReceivedByServer, Integer.valueOf(oldId), Integer.valueOf(newMsgObj.id), newMsgObj, Long.valueOf(newMsgObj.dialog_id), Long.valueOf(grouped_id));
                    MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new SendMessagesHelper$$Lambda$47(this, newMsgObj, oldId, sentMessages, grouped_id));
                }
            }
            Utilities.stageQueue.postRunnable(new SendMessagesHelper$$Lambda$48(this, updates));
        } else {
            AlertsCreator.processError(this.currentAccount, error, null, req, new Object[0]);
            isSentError = true;
        }
        if (isSentError) {
            for (i = 0; i < msgObjs.size(); i++) {
                newMsgObj = ((MessageObject) msgObjs.get(i)).messageOwner;
                MessagesStorage.getInstance(this.currentAccount).markMessageAsSendError(newMsgObj);
                newMsgObj.send_state = 2;
                NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.messageSendError, Integer.valueOf(newMsgObj.id));
                processSentMessage(newMsgObj.id);
                removeFromSendingMessages(newMsgObj.id);
            }
        }
    }

    final /* synthetic */ void lambda$null$22$SendMessagesHelper(TL_updateNewMessage newMessage) {
        MessagesController.getInstance(this.currentAccount).processNewDifferenceParams(-1, newMessage.pts, -1, newMessage.pts_count);
    }

    final /* synthetic */ void lambda$null$23$SendMessagesHelper(TL_updateNewChannelMessage newMessage) {
        MessagesController.getInstance(this.currentAccount).processNewChannelDifferenceParams(newMessage.pts, newMessage.pts_count, newMessage.message.to_id.channel_id);
    }

    final /* synthetic */ void lambda$null$25$SendMessagesHelper(Message newMsgObj, int oldId, ArrayList sentMessages, long grouped_id) {
        MessagesStorage.getInstance(this.currentAccount).updateMessageStateAndId(newMsgObj.random_id, Integer.valueOf(oldId), newMsgObj.id, 0, false, newMsgObj.to_id.channel_id);
        MessagesStorage.getInstance(this.currentAccount).putMessages(sentMessages, true, false, false, 0);
        AndroidUtilities.runOnUIThread(new SendMessagesHelper$$Lambda$49(this, newMsgObj, oldId, grouped_id));
    }

    final /* synthetic */ void lambda$null$24$SendMessagesHelper(Message newMsgObj, int oldId, long grouped_id) {
        DataQuery.getInstance(this.currentAccount).increasePeerRaiting(newMsgObj.dialog_id);
        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.messageReceivedByServer, Integer.valueOf(oldId), Integer.valueOf(newMsgObj.id), newMsgObj, Long.valueOf(newMsgObj.dialog_id), Long.valueOf(grouped_id));
        processSentMessage(oldId);
        removeFromSendingMessages(oldId);
    }

    final /* synthetic */ void lambda$null$26$SendMessagesHelper(Updates updates) {
        MessagesController.getInstance(this.currentAccount).processUpdates(updates, false);
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
        int i;
        if (!(req instanceof TL_messages_editMessage) && check) {
            DelayedMessage maxDelayedMessage = findMaxDelayedMessageForMessageId(msgObj.getId(), msgObj.getDialogId());
            if (maxDelayedMessage != null) {
                maxDelayedMessage.addDelayedRequest(req, msgObj, originalPath);
                if (parentMessage != null && parentMessage.requests != null) {
                    maxDelayedMessage.requests.addAll(parentMessage.requests);
                    return;
                }
                return;
            }
        }
        Message newMsgObj = msgObj.messageOwner;
        putToSendingMessages(newMsgObj);
        ConnectionsManager instance = ConnectionsManager.getInstance(this.currentAccount);
        RequestDelegate sendMessagesHelper$$Lambda$11 = new SendMessagesHelper$$Lambda$11(this, req, newMsgObj, msgObj, originalPath);
        QuickAckDelegate sendMessagesHelper$$Lambda$12 = new SendMessagesHelper$$Lambda$12(this, newMsgObj);
        if (req instanceof TL_messages_sendMessage) {
            i = 128;
        } else {
            i = 0;
        }
        newMsgObj.reqId = instance.sendRequest(req, sendMessagesHelper$$Lambda$11, sendMessagesHelper$$Lambda$12, i | 68);
        if (parentMessage != null) {
            parentMessage.sendDelayedRequests();
        }
    }

    final /* synthetic */ void lambda$performSendMessageRequest$39$SendMessagesHelper(TLObject req, Message newMsgObj, MessageObject msgObj, String originalPath, TLObject response, TL_error error) {
        if (req instanceof TL_messages_editMessage) {
            AndroidUtilities.runOnUIThread(new SendMessagesHelper$$Lambda$34(this, error, newMsgObj, response, msgObj, originalPath, req));
        } else {
            AndroidUtilities.runOnUIThread(new SendMessagesHelper$$Lambda$35(this, error, newMsgObj, req, response, msgObj, originalPath));
        }
    }

    final /* synthetic */ void lambda$null$31$SendMessagesHelper(TL_error error, Message newMsgObj, TLObject response, MessageObject msgObj, String originalPath, TLObject req) {
        if (error == null) {
            String attachPath = newMsgObj.attachPath;
            Updates updates = (Updates) response;
            ArrayList<Update> updatesArr = ((Updates) response).updates;
            Message message = null;
            int a = 0;
            while (a < updatesArr.size()) {
                Update update = (Update) updatesArr.get(a);
                if (update instanceof TL_updateEditMessage) {
                    message = ((TL_updateEditMessage) update).message;
                    break;
                } else if (update instanceof TL_updateEditChannelMessage) {
                    message = ((TL_updateEditChannelMessage) update).message;
                    break;
                } else {
                    a++;
                }
            }
            if (message != null) {
                ImageLoader.saveMessageThumbs(message);
                updateMediaPaths(msgObj, message, originalPath, false);
            }
            Utilities.stageQueue.postRunnable(new SendMessagesHelper$$Lambda$42(this, updates, newMsgObj));
            if (MessageObject.isVideoMessage(newMsgObj) || MessageObject.isRoundVideoMessage(newMsgObj) || MessageObject.isNewGifMessage(newMsgObj)) {
                stopVideoService(attachPath);
                return;
            }
            return;
        }
        AlertsCreator.processError(this.currentAccount, error, null, req, new Object[0]);
        if (MessageObject.isVideoMessage(newMsgObj) || MessageObject.isRoundVideoMessage(newMsgObj) || MessageObject.isNewGifMessage(newMsgObj)) {
            stopVideoService(newMsgObj.attachPath);
        }
        removeFromSendingMessages(newMsgObj.id);
        revertEditingMessageObject(msgObj);
    }

    final /* synthetic */ void lambda$null$30$SendMessagesHelper(Updates updates, Message newMsgObj) {
        MessagesController.getInstance(this.currentAccount).processUpdates(updates, false);
        AndroidUtilities.runOnUIThread(new SendMessagesHelper$$Lambda$43(this, newMsgObj));
    }

    final /* synthetic */ void lambda$null$29$SendMessagesHelper(Message newMsgObj) {
        processSentMessage(newMsgObj.id);
        removeFromSendingMessages(newMsgObj.id);
    }

    final /* synthetic */ void lambda$null$38$SendMessagesHelper(TL_error error, Message newMsgObj, TLObject req, TLObject response, MessageObject msgObj, String originalPath) {
        boolean isSentError = false;
        if (error == null) {
            int i;
            int oldId = newMsgObj.id;
            boolean isBroadcast = req instanceof TL_messages_sendBroadcast;
            ArrayList<Message> sentMessages = new ArrayList();
            String attachPath = newMsgObj.attachPath;
            if (response instanceof TL_updateShortSentMessage) {
                TL_updateShortSentMessage res = (TL_updateShortSentMessage) response;
                i = res.id;
                newMsgObj.id = i;
                newMsgObj.local_id = i;
                newMsgObj.date = res.date;
                newMsgObj.entities = res.entities;
                newMsgObj.out = res.out;
                if (res.media != null) {
                    newMsgObj.media = res.media;
                    newMsgObj.flags |= 512;
                    ImageLoader.saveMessageThumbs(newMsgObj);
                }
                if ((res.media instanceof TL_messageMediaGame) && !TextUtils.isEmpty(res.message)) {
                    newMsgObj.message = res.message;
                }
                if (!newMsgObj.entities.isEmpty()) {
                    newMsgObj.flags |= 128;
                }
                Utilities.stageQueue.postRunnable(new SendMessagesHelper$$Lambda$36(this, res));
                sentMessages.add(newMsgObj);
            } else if (response instanceof Updates) {
                Updates updates = (Updates) response;
                ArrayList<Update> updatesArr = ((Updates) response).updates;
                Message message = null;
                int a = 0;
                while (a < updatesArr.size()) {
                    Update update = (Update) updatesArr.get(a);
                    if (update instanceof TL_updateNewMessage) {
                        TL_updateNewMessage newMessage = (TL_updateNewMessage) update;
                        message = newMessage.message;
                        sentMessages.add(message);
                        Utilities.stageQueue.postRunnable(new SendMessagesHelper$$Lambda$37(this, newMessage));
                        updatesArr.remove(a);
                        break;
                    } else if (update instanceof TL_updateNewChannelMessage) {
                        TL_updateNewChannelMessage newMessage2 = (TL_updateNewChannelMessage) update;
                        message = newMessage2.message;
                        sentMessages.add(message);
                        if ((newMsgObj.flags & Integer.MIN_VALUE) != 0) {
                            Message message2 = newMessage2.message;
                            message2.flags |= Integer.MIN_VALUE;
                        }
                        Utilities.stageQueue.postRunnable(new SendMessagesHelper$$Lambda$38(this, newMessage2));
                        updatesArr.remove(a);
                    } else {
                        a++;
                    }
                }
                if (message != null) {
                    ImageLoader.saveMessageThumbs(message);
                    Integer value = (Integer) MessagesController.getInstance(this.currentAccount).dialogs_read_outbox_max.get(Long.valueOf(message.dialog_id));
                    if (value == null) {
                        value = Integer.valueOf(MessagesStorage.getInstance(this.currentAccount).getDialogReadMax(message.out, message.dialog_id));
                        MessagesController.getInstance(this.currentAccount).dialogs_read_outbox_max.put(Long.valueOf(message.dialog_id), value);
                    }
                    message.unread = value.intValue() < message.id;
                    newMsgObj.id = message.id;
                    updateMediaPaths(msgObj, message, originalPath, false);
                } else {
                    isSentError = true;
                }
                Utilities.stageQueue.postRunnable(new SendMessagesHelper$$Lambda$39(this, updates));
            }
            if (MessageObject.isLiveLocationMessage(newMsgObj)) {
                LocationController.getInstance(this.currentAccount).addSharingLocation(newMsgObj.dialog_id, newMsgObj.id, newMsgObj.media.period, newMsgObj);
            }
            if (!isSentError) {
                StatsController.getInstance(this.currentAccount).incrementSentItemsCount(ConnectionsManager.getCurrentNetworkType(), 1, 1);
                newMsgObj.send_state = 0;
                NotificationCenter instance = NotificationCenter.getInstance(this.currentAccount);
                int i2 = NotificationCenter.messageReceivedByServer;
                Object[] objArr = new Object[5];
                objArr[0] = Integer.valueOf(oldId);
                if (isBroadcast) {
                    i = oldId;
                } else {
                    i = newMsgObj.id;
                }
                objArr[1] = Integer.valueOf(i);
                objArr[2] = newMsgObj;
                objArr[3] = Long.valueOf(newMsgObj.dialog_id);
                objArr[4] = Long.valueOf(0);
                instance.postNotificationName(i2, objArr);
                MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new SendMessagesHelper$$Lambda$40(this, newMsgObj, oldId, isBroadcast, sentMessages, attachPath));
            }
        } else {
            AlertsCreator.processError(this.currentAccount, error, null, req, new Object[0]);
            isSentError = true;
        }
        if (isSentError) {
            MessagesStorage.getInstance(this.currentAccount).markMessageAsSendError(newMsgObj);
            newMsgObj.send_state = 2;
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.messageSendError, Integer.valueOf(newMsgObj.id));
            processSentMessage(newMsgObj.id);
            if (MessageObject.isVideoMessage(newMsgObj) || MessageObject.isRoundVideoMessage(newMsgObj) || MessageObject.isNewGifMessage(newMsgObj)) {
                stopVideoService(newMsgObj.attachPath);
            }
            removeFromSendingMessages(newMsgObj.id);
        }
    }

    final /* synthetic */ void lambda$null$32$SendMessagesHelper(TL_updateShortSentMessage res) {
        MessagesController.getInstance(this.currentAccount).processNewDifferenceParams(-1, res.pts, res.date, res.pts_count);
    }

    final /* synthetic */ void lambda$null$33$SendMessagesHelper(TL_updateNewMessage newMessage) {
        MessagesController.getInstance(this.currentAccount).processNewDifferenceParams(-1, newMessage.pts, -1, newMessage.pts_count);
    }

    final /* synthetic */ void lambda$null$34$SendMessagesHelper(TL_updateNewChannelMessage newMessage) {
        MessagesController.getInstance(this.currentAccount).processNewChannelDifferenceParams(newMessage.pts, newMessage.pts_count, newMessage.message.to_id.channel_id);
    }

    final /* synthetic */ void lambda$null$35$SendMessagesHelper(Updates updates) {
        MessagesController.getInstance(this.currentAccount).processUpdates(updates, false);
    }

    final /* synthetic */ void lambda$null$37$SendMessagesHelper(Message newMsgObj, int oldId, boolean isBroadcast, ArrayList sentMessages, String attachPath) {
        MessagesStorage.getInstance(this.currentAccount).updateMessageStateAndId(newMsgObj.random_id, Integer.valueOf(oldId), isBroadcast ? oldId : newMsgObj.id, 0, false, newMsgObj.to_id.channel_id);
        MessagesStorage.getInstance(this.currentAccount).putMessages(sentMessages, true, false, isBroadcast, 0);
        if (isBroadcast) {
            ArrayList currentMessage = new ArrayList();
            currentMessage.add(newMsgObj);
            MessagesStorage.getInstance(this.currentAccount).putMessages(currentMessage, true, false, false, 0);
        }
        AndroidUtilities.runOnUIThread(new SendMessagesHelper$$Lambda$41(this, isBroadcast, sentMessages, newMsgObj, oldId));
        if (MessageObject.isVideoMessage(newMsgObj) || MessageObject.isRoundVideoMessage(newMsgObj) || MessageObject.isNewGifMessage(newMsgObj)) {
            stopVideoService(attachPath);
        }
    }

    final /* synthetic */ void lambda$null$36$SendMessagesHelper(boolean isBroadcast, ArrayList sentMessages, Message newMsgObj, int oldId) {
        if (isBroadcast) {
            for (int a = 0; a < sentMessages.size(); a++) {
                Message message = (Message) sentMessages.get(a);
                ArrayList<MessageObject> arr = new ArrayList();
                MessageObject messageObject = new MessageObject(this.currentAccount, message, false);
                arr.add(messageObject);
                MessagesController.getInstance(this.currentAccount).updateInterfaceWithMessages(messageObject.getDialogId(), arr, true);
            }
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
        }
        DataQuery.getInstance(this.currentAccount).increasePeerRaiting(newMsgObj.dialog_id);
        NotificationCenter instance = NotificationCenter.getInstance(this.currentAccount);
        int i = NotificationCenter.messageReceivedByServer;
        Object[] objArr = new Object[5];
        objArr[0] = Integer.valueOf(oldId);
        objArr[1] = Integer.valueOf(isBroadcast ? oldId : newMsgObj.id);
        objArr[2] = newMsgObj;
        objArr[3] = Long.valueOf(newMsgObj.dialog_id);
        objArr[4] = Long.valueOf(0);
        instance.postNotificationName(i, objArr);
        processSentMessage(oldId);
        removeFromSendingMessages(oldId);
    }

    final /* synthetic */ void lambda$performSendMessageRequest$41$SendMessagesHelper(Message newMsgObj) {
        AndroidUtilities.runOnUIThread(new SendMessagesHelper$$Lambda$33(this, newMsgObj, newMsgObj.id));
    }

    final /* synthetic */ void lambda$null$40$SendMessagesHelper(Message newMsgObj, int msg_id) {
        newMsgObj.send_state = 0;
        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.messageReceivedByAck, Integer.valueOf(msg_id));
    }

    private void updateMediaPaths(MessageObject newMsgObj, Message sentMessage, String originalPath, boolean post) {
        Message newMsg = newMsgObj.messageOwner;
        if (sentMessage != null) {
            int a;
            PhotoSize size;
            PhotoSize size2;
            String fileName;
            String fileName2;
            File cacheFile;
            File cacheFile2;
            if ((sentMessage.media instanceof TL_messageMediaPhoto) && sentMessage.media.photo != null && (newMsg.media instanceof TL_messageMediaPhoto) && newMsg.media.photo != null) {
                if (sentMessage.media.ttl_seconds == 0) {
                    MessagesStorage.getInstance(this.currentAccount).putSentFile(originalPath, sentMessage.media.photo, 0);
                }
                if (newMsg.media.photo.sizes.size() == 1 && (((PhotoSize) newMsg.media.photo.sizes.get(0)).location instanceof TL_fileLocationUnavailable)) {
                    newMsg.media.photo.sizes = sentMessage.media.photo.sizes;
                } else {
                    for (a = 0; a < sentMessage.media.photo.sizes.size(); a++) {
                        size = (PhotoSize) sentMessage.media.photo.sizes.get(a);
                        if (!(size == null || size.location == null || (size instanceof TL_photoSizeEmpty) || size.type == null)) {
                            int b = 0;
                            while (b < newMsg.media.photo.sizes.size()) {
                                size2 = (PhotoSize) newMsg.media.photo.sizes.get(b);
                                if (size2 == null || size2.location == null || size2.type == null || !((size2.location.volume_id == -2147483648L && size.type.equals(size2.type)) || (size.w == size2.w && size.h == size2.h))) {
                                    b++;
                                } else {
                                    fileName = size2.location.volume_id + "_" + size2.location.local_id;
                                    fileName2 = size.location.volume_id + "_" + size.location.local_id;
                                    if (!fileName.equals(fileName2)) {
                                        cacheFile = new File(FileLoader.getDirectory(4), fileName + ".jpg");
                                        if (sentMessage.media.ttl_seconds != 0 || (sentMessage.media.photo.sizes.size() != 1 && size.w <= 90 && size.h <= 90)) {
                                            cacheFile2 = new File(FileLoader.getDirectory(4), fileName2 + ".jpg");
                                        } else {
                                            cacheFile2 = FileLoader.getPathToAttach(size);
                                        }
                                        cacheFile.renameTo(cacheFile2);
                                        ImageLoader.getInstance().replaceImageInCache(fileName, fileName2, size.location, post);
                                        size2.location = size.location;
                                        size2.size = size.size;
                                    }
                                }
                            }
                        }
                    }
                }
                sentMessage.message = newMsg.message;
                sentMessage.attachPath = newMsg.attachPath;
                newMsg.media.photo.id = sentMessage.media.photo.id;
                newMsg.media.photo.access_hash = sentMessage.media.photo.access_hash;
            } else if ((sentMessage.media instanceof TL_messageMediaDocument) && sentMessage.media.document != null && (newMsg.media instanceof TL_messageMediaDocument) && newMsg.media.document != null) {
                DocumentAttribute attribute;
                if (MessageObject.isVideoMessage(sentMessage)) {
                    if (sentMessage.media.ttl_seconds == 0) {
                        MessagesStorage.getInstance(this.currentAccount).putSentFile(originalPath, sentMessage.media.document, 2);
                    }
                    sentMessage.attachPath = newMsg.attachPath;
                } else if (!(MessageObject.isVoiceMessage(sentMessage) || MessageObject.isRoundVideoMessage(sentMessage) || sentMessage.media.ttl_seconds != 0)) {
                    MessagesStorage.getInstance(this.currentAccount).putSentFile(originalPath, sentMessage.media.document, 1);
                }
                size2 = newMsg.media.document.thumb;
                size = sentMessage.media.document.thumb;
                if (size2 != null && size2.location != null && size2.location.volume_id == -2147483648L && size != null && size.location != null && !(size instanceof TL_photoSizeEmpty) && !(size2 instanceof TL_photoSizeEmpty)) {
                    fileName = size2.location.volume_id + "_" + size2.location.local_id;
                    fileName2 = size.location.volume_id + "_" + size.location.local_id;
                    if (!fileName.equals(fileName2)) {
                        new File(FileLoader.getDirectory(4), fileName + ".jpg").renameTo(new File(FileLoader.getDirectory(4), fileName2 + ".jpg"));
                        ImageLoader.getInstance().replaceImageInCache(fileName, fileName2, size.location, post);
                        size2.location = size.location;
                        size2.size = size.size;
                    }
                } else if (size2 != null && MessageObject.isStickerMessage(sentMessage) && size2.location != null) {
                    size.location = size2.location;
                } else if ((size2 != null && (size2.location instanceof TL_fileLocationUnavailable)) || (size2 instanceof TL_photoSizeEmpty)) {
                    newMsg.media.document.thumb = sentMessage.media.document.thumb;
                }
                newMsg.media.document.dc_id = sentMessage.media.document.dc_id;
                newMsg.media.document.id = sentMessage.media.document.id;
                newMsg.media.document.access_hash = sentMessage.media.document.access_hash;
                byte[] oldWaveform = null;
                for (a = 0; a < newMsg.media.document.attributes.size(); a++) {
                    attribute = (DocumentAttribute) newMsg.media.document.attributes.get(a);
                    if (attribute instanceof TL_documentAttributeAudio) {
                        oldWaveform = attribute.waveform;
                        break;
                    }
                }
                newMsg.media.document.attributes = sentMessage.media.document.attributes;
                if (oldWaveform != null) {
                    for (a = 0; a < newMsg.media.document.attributes.size(); a++) {
                        attribute = (DocumentAttribute) newMsg.media.document.attributes.get(a);
                        if (attribute instanceof TL_documentAttributeAudio) {
                            attribute.waveform = oldWaveform;
                            attribute.flags |= 4;
                        }
                    }
                }
                newMsg.media.document.size = sentMessage.media.document.size;
                newMsg.media.document.mime_type = sentMessage.media.document.mime_type;
                if ((sentMessage.flags & 4) == 0 && MessageObject.isOut(sentMessage)) {
                    if (MessageObject.isNewGifDocument(sentMessage.media.document)) {
                        DataQuery.getInstance(this.currentAccount).addRecentGif(sentMessage.media.document, sentMessage.date);
                    } else if (MessageObject.isStickerDocument(sentMessage.media.document)) {
                        DataQuery.getInstance(this.currentAccount).addRecentSticker(0, sentMessage.media.document, sentMessage.date, false);
                    }
                }
                if (newMsg.attachPath == null || !newMsg.attachPath.startsWith(FileLoader.getDirectory(4).getAbsolutePath())) {
                    sentMessage.attachPath = newMsg.attachPath;
                    sentMessage.message = newMsg.message;
                    return;
                }
                cacheFile = new File(newMsg.attachPath);
                cacheFile2 = FileLoader.getPathToAttach(sentMessage.media.document, sentMessage.media.ttl_seconds != 0);
                if (!cacheFile.renameTo(cacheFile2)) {
                    sentMessage.attachPath = newMsg.attachPath;
                    sentMessage.message = newMsg.message;
                } else if (MessageObject.isVideoMessage(sentMessage)) {
                    newMsgObj.attachPathExists = true;
                } else {
                    newMsgObj.mediaExists = newMsgObj.attachPathExists;
                    newMsgObj.attachPathExists = false;
                    newMsg.attachPath = TtmlNode.ANONYMOUS_REGION_ID;
                    if (originalPath != null) {
                        if (originalPath.startsWith("http")) {
                            MessagesStorage.getInstance(this.currentAccount).addRecentLocalFile(originalPath, cacheFile2.toString(), newMsg.media.document);
                        }
                    }
                }
            } else if ((sentMessage.media instanceof TL_messageMediaContact) && (newMsg.media instanceof TL_messageMediaContact)) {
                newMsg.media = sentMessage.media;
            } else if (sentMessage.media instanceof TL_messageMediaWebPage) {
                newMsg.media = sentMessage.media;
            } else if (sentMessage.media instanceof TL_messageMediaGeo) {
                sentMessage.media.geo.lat = newMsg.media.geo.lat;
                sentMessage.media.geo._long = newMsg.media.geo._long;
            } else if (sentMessage.media instanceof TL_messageMediaGame) {
                newMsg.media = sentMessage.media;
                if ((newMsg.media instanceof TL_messageMediaGame) && !TextUtils.isEmpty(sentMessage.message)) {
                    newMsg.entities = sentMessage.entities;
                    newMsg.message = sentMessage.message;
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
        AndroidUtilities.runOnUIThread(new SendMessagesHelper$$Lambda$13(this, users, chats, encryptedChats, messages));
    }

    final /* synthetic */ void lambda$processUnsentMessages$42$SendMessagesHelper(ArrayList users, ArrayList chats, ArrayList encryptedChats, ArrayList messages) {
        MessagesController.getInstance(this.currentAccount).putUsers(users, true);
        MessagesController.getInstance(this.currentAccount).putChats(chats, true);
        MessagesController.getInstance(this.currentAccount).putEncryptedChats(encryptedChats, true);
        for (int a = 0; a < messages.size(); a++) {
            retrySendMessage(new MessageObject(this.currentAccount, (Message) messages.get(a), false), true);
        }
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

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static boolean prepareSendingDocumentInternal(int r45, java.lang.String r46, java.lang.String r47, android.net.Uri r48, java.lang.String r49, long r50, org.telegram.messenger.MessageObject r52, java.lang.CharSequence r53, java.util.ArrayList<org.telegram.tgnet.TLRPC.MessageEntity> r54, org.telegram.messenger.MessageObject r55) {
        /*
        if (r46 == 0) goto L_0x0008;
    L_0x0002:
        r2 = r46.length();
        if (r2 != 0) goto L_0x000c;
    L_0x0008:
        if (r48 != 0) goto L_0x000c;
    L_0x000a:
        r2 = 0;
    L_0x000b:
        return r2;
    L_0x000c:
        if (r48 == 0) goto L_0x0016;
    L_0x000e:
        r2 = org.telegram.messenger.AndroidUtilities.isInternalUri(r48);
        if (r2 == 0) goto L_0x0016;
    L_0x0014:
        r2 = 0;
        goto L_0x000b;
    L_0x0016:
        if (r46 == 0) goto L_0x002b;
    L_0x0018:
        r2 = new java.io.File;
        r0 = r46;
        r2.<init>(r0);
        r2 = android.net.Uri.fromFile(r2);
        r2 = org.telegram.messenger.AndroidUtilities.isInternalUri(r2);
        if (r2 == 0) goto L_0x002b;
    L_0x0029:
        r2 = 0;
        goto L_0x000b;
    L_0x002b:
        r39 = android.webkit.MimeTypeMap.getSingleton();
        r14 = 0;
        r28 = 0;
        if (r48 == 0) goto L_0x0058;
    L_0x0034:
        r32 = 0;
        if (r49 == 0) goto L_0x0040;
    L_0x0038:
        r0 = r39;
        r1 = r49;
        r28 = r0.getExtensionFromMimeType(r1);
    L_0x0040:
        if (r28 != 0) goto L_0x0051;
    L_0x0042:
        r28 = "txt";
    L_0x0045:
        r0 = r48;
        r1 = r28;
        r46 = org.telegram.messenger.MediaController.copyFileToCache(r0, r1);
        if (r46 != 0) goto L_0x0054;
    L_0x004f:
        r2 = 0;
        goto L_0x000b;
    L_0x0051:
        r32 = 1;
        goto L_0x0045;
    L_0x0054:
        if (r32 != 0) goto L_0x0058;
    L_0x0056:
        r28 = 0;
    L_0x0058:
        r29 = new java.io.File;
        r0 = r29;
        r1 = r46;
        r0.<init>(r1);
        r2 = r29.exists();
        if (r2 == 0) goto L_0x0071;
    L_0x0067:
        r2 = r29.length();
        r8 = 0;
        r2 = (r2 > r8 ? 1 : (r2 == r8 ? 0 : -1));
        if (r2 != 0) goto L_0x0073;
    L_0x0071:
        r2 = 0;
        goto L_0x000b;
    L_0x0073:
        r0 = r50;
        r2 = (int) r0;
        if (r2 != 0) goto L_0x02e2;
    L_0x0078:
        r34 = 1;
    L_0x007a:
        if (r34 != 0) goto L_0x02e6;
    L_0x007c:
        r13 = 1;
    L_0x007d:
        r40 = r29.getName();
        r26 = "";
        if (r28 == 0) goto L_0x02e9;
    L_0x0086:
        r26 = r28;
    L_0x0088:
        r27 = r26.toLowerCase();
        r41 = 0;
        r43 = 0;
        r35 = 0;
        r24 = 0;
        r2 = "mp3";
        r0 = r27;
        r2 = r0.equals(r2);
        if (r2 != 0) goto L_0x00aa;
    L_0x009f:
        r2 = "m4a";
        r0 = r27;
        r2 = r0.equals(r2);
        if (r2 == 0) goto L_0x0300;
    L_0x00aa:
        r17 = org.telegram.messenger.audioinfo.AudioInfo.getAudioInfo(r29);
        if (r17 == 0) goto L_0x00c2;
    L_0x00b0:
        r22 = r17.getDuration();
        r2 = 0;
        r2 = (r22 > r2 ? 1 : (r22 == r2 ? 0 : -1));
        if (r2 == 0) goto L_0x00c2;
    L_0x00ba:
        r41 = r17.getArtist();
        r43 = r17.getTitle();
    L_0x00c2:
        if (r24 == 0) goto L_0x00f8;
    L_0x00c4:
        r14 = new org.telegram.tgnet.TLRPC$TL_documentAttributeAudio;
        r14.<init>();
        r0 = r24;
        r14.duration = r0;
        r0 = r43;
        r14.title = r0;
        r0 = r41;
        r14.performer = r0;
        r2 = r14.title;
        if (r2 != 0) goto L_0x00de;
    L_0x00d9:
        r2 = "";
        r14.title = r2;
    L_0x00de:
        r2 = r14.flags;
        r2 = r2 | 1;
        r14.flags = r2;
        r2 = r14.performer;
        if (r2 != 0) goto L_0x00ed;
    L_0x00e8:
        r2 = "";
        r14.performer = r2;
    L_0x00ed:
        r2 = r14.flags;
        r2 = r2 | 2;
        r14.flags = r2;
        if (r35 == 0) goto L_0x00f8;
    L_0x00f5:
        r2 = 1;
        r14.voice = r2;
    L_0x00f8:
        r42 = 0;
        if (r47 == 0) goto L_0x0109;
    L_0x00fc:
        r2 = "attheme";
        r0 = r47;
        r2 = r0.endsWith(r2);
        if (r2 == 0) goto L_0x039d;
    L_0x0107:
        r42 = 1;
    L_0x0109:
        r21 = 0;
        if (r42 != 0) goto L_0x014c;
    L_0x010d:
        if (r34 != 0) goto L_0x014c;
    L_0x010f:
        r3 = org.telegram.messenger.MessagesStorage.getInstance(r45);
        if (r34 != 0) goto L_0x03df;
    L_0x0115:
        r2 = 1;
    L_0x0116:
        r0 = r47;
        r21 = r3.getSentFile(r0, r2);
        r21 = (org.telegram.tgnet.TLRPC.TL_document) r21;
        if (r21 != 0) goto L_0x014c;
    L_0x0120:
        r2 = r46.equals(r47);
        if (r2 != 0) goto L_0x014c;
    L_0x0126:
        if (r34 != 0) goto L_0x014c;
    L_0x0128:
        r3 = org.telegram.messenger.MessagesStorage.getInstance(r45);
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r0 = r46;
        r2 = r2.append(r0);
        r8 = r29.length();
        r2 = r2.append(r8);
        r4 = r2.toString();
        if (r34 != 0) goto L_0x03e2;
    L_0x0145:
        r2 = 1;
    L_0x0146:
        r21 = r3.getSentFile(r4, r2);
        r21 = (org.telegram.tgnet.TLRPC.TL_document) r21;
    L_0x014c:
        if (r21 != 0) goto L_0x02b4;
    L_0x014e:
        r21 = new org.telegram.tgnet.TLRPC$TL_document;
        r21.<init>();
        r2 = 0;
        r0 = r21;
        r0.id = r2;
        r2 = org.telegram.tgnet.ConnectionsManager.getInstance(r45);
        r2 = r2.getCurrentTime();
        r0 = r21;
        r0.date = r2;
        r31 = new org.telegram.tgnet.TLRPC$TL_documentAttributeFilename;
        r31.<init>();
        r0 = r40;
        r1 = r31;
        r1.file_name = r0;
        r0 = r21;
        r2 = r0.attributes;
        r0 = r31;
        r2.add(r0);
        r2 = r29.length();
        r2 = (int) r2;
        r0 = r21;
        r0.size = r2;
        r2 = 0;
        r0 = r21;
        r0.dc_id = r2;
        if (r14 == 0) goto L_0x0190;
    L_0x0189:
        r0 = r21;
        r2 = r0.attributes;
        r2.add(r14);
    L_0x0190:
        r2 = r26.length();
        if (r2 == 0) goto L_0x044a;
    L_0x0196:
        r2 = -1;
        r3 = r27.hashCode();
        switch(r3) {
            case 109967: goto L_0x0401;
            case 3145576: goto L_0x040f;
            case 3418175: goto L_0x03f3;
            case 3645340: goto L_0x03e5;
            default: goto L_0x019e;
        };
    L_0x019e:
        switch(r2) {
            case 0: goto L_0x041d;
            case 1: goto L_0x0426;
            case 2: goto L_0x042f;
            case 3: goto L_0x0438;
            default: goto L_0x01a1;
        };
    L_0x01a1:
        r0 = r39;
        r1 = r27;
        r38 = r0.getMimeTypeFromExtension(r1);
        if (r38 == 0) goto L_0x0441;
    L_0x01ab:
        r0 = r38;
        r1 = r21;
        r1.mime_type = r0;
    L_0x01b1:
        r0 = r21;
        r2 = r0.mime_type;
        r3 = "image/gif";
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x0202;
    L_0x01be:
        if (r55 == 0) goto L_0x01ca;
    L_0x01c0:
        r2 = r55.getGroupIdForUse();
        r8 = 0;
        r2 = (r2 > r8 ? 1 : (r2 == r8 ? 0 : -1));
        if (r2 != 0) goto L_0x0202;
    L_0x01ca:
        r2 = r29.getAbsolutePath();	 Catch:{ Exception -> 0x0453 }
        r3 = 0;
        r4 = 1119092736; // 0x42b40000 float:90.0 double:5.529052754E-315;
        r8 = 1119092736; // 0x42b40000 float:90.0 double:5.529052754E-315;
        r9 = 1;
        r18 = org.telegram.messenger.ImageLoader.loadBitmap(r2, r3, r4, r8, r9);	 Catch:{ Exception -> 0x0453 }
        if (r18 == 0) goto L_0x0202;
    L_0x01da:
        r2 = "animation.gif";
        r0 = r31;
        r0.file_name = r2;	 Catch:{ Exception -> 0x0453 }
        r0 = r21;
        r2 = r0.attributes;	 Catch:{ Exception -> 0x0453 }
        r3 = new org.telegram.tgnet.TLRPC$TL_documentAttributeAnimated;	 Catch:{ Exception -> 0x0453 }
        r3.<init>();	 Catch:{ Exception -> 0x0453 }
        r2.add(r3);	 Catch:{ Exception -> 0x0453 }
        r2 = 1119092736; // 0x42b40000 float:90.0 double:5.529052754E-315;
        r3 = 1119092736; // 0x42b40000 float:90.0 double:5.529052754E-315;
        r4 = 55;
        r0 = r18;
        r1 = r34;
        r2 = org.telegram.messenger.ImageLoader.scaleAndSaveImage(r0, r2, r3, r4, r1);	 Catch:{ Exception -> 0x0453 }
        r0 = r21;
        r0.thumb = r2;	 Catch:{ Exception -> 0x0453 }
        r18.recycle();	 Catch:{ Exception -> 0x0453 }
    L_0x0202:
        r0 = r21;
        r2 = r0.mime_type;
        r3 = "image/webp";
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x029c;
    L_0x020f:
        if (r13 == 0) goto L_0x029c;
    L_0x0211:
        if (r55 != 0) goto L_0x029c;
    L_0x0213:
        r19 = new android.graphics.BitmapFactory$Options;
        r19.<init>();
        r2 = 1;
        r0 = r19;
        r0.inJustDecodeBounds = r2;	 Catch:{ Exception -> 0x0459 }
        r30 = new java.io.RandomAccessFile;	 Catch:{ Exception -> 0x0459 }
        r2 = "r";
        r0 = r30;
        r1 = r46;
        r0.<init>(r1, r2);	 Catch:{ Exception -> 0x0459 }
        r2 = r30.getChannel();	 Catch:{ Exception -> 0x0459 }
        r3 = java.nio.channels.FileChannel.MapMode.READ_ONLY;	 Catch:{ Exception -> 0x0459 }
        r4 = 0;
        r8 = r46.length();	 Catch:{ Exception -> 0x0459 }
        r6 = (long) r8;	 Catch:{ Exception -> 0x0459 }
        r20 = r2.map(r3, r4, r6);	 Catch:{ Exception -> 0x0459 }
        r2 = 0;
        r3 = r20.limit();	 Catch:{ Exception -> 0x0459 }
        r4 = 1;
        r0 = r20;
        r1 = r19;
        org.telegram.messenger.Utilities.loadWebpImage(r2, r0, r3, r1, r4);	 Catch:{ Exception -> 0x0459 }
        r30.close();	 Catch:{ Exception -> 0x0459 }
    L_0x024a:
        r0 = r19;
        r2 = r0.outWidth;
        if (r2 == 0) goto L_0x029c;
    L_0x0250:
        r0 = r19;
        r2 = r0.outHeight;
        if (r2 == 0) goto L_0x029c;
    L_0x0256:
        r0 = r19;
        r2 = r0.outWidth;
        r3 = 800; // 0x320 float:1.121E-42 double:3.953E-321;
        if (r2 > r3) goto L_0x029c;
    L_0x025e:
        r0 = r19;
        r2 = r0.outHeight;
        r3 = 800; // 0x320 float:1.121E-42 double:3.953E-321;
        if (r2 > r3) goto L_0x029c;
    L_0x0266:
        r16 = new org.telegram.tgnet.TLRPC$TL_documentAttributeSticker;
        r16.<init>();
        r2 = "";
        r0 = r16;
        r0.alt = r2;
        r2 = new org.telegram.tgnet.TLRPC$TL_inputStickerSetEmpty;
        r2.<init>();
        r0 = r16;
        r0.stickerset = r2;
        r0 = r21;
        r2 = r0.attributes;
        r0 = r16;
        r2.add(r0);
        r15 = new org.telegram.tgnet.TLRPC$TL_documentAttributeImageSize;
        r15.<init>();
        r0 = r19;
        r2 = r0.outWidth;
        r15.w = r2;
        r0 = r19;
        r2 = r0.outHeight;
        r15.h = r2;
        r0 = r21;
        r2 = r0.attributes;
        r2.add(r15);
    L_0x029c:
        r0 = r21;
        r2 = r0.thumb;
        if (r2 != 0) goto L_0x02b4;
    L_0x02a2:
        r2 = new org.telegram.tgnet.TLRPC$TL_photoSizeEmpty;
        r2.<init>();
        r0 = r21;
        r0.thumb = r2;
        r0 = r21;
        r2 = r0.thumb;
        r3 = "s";
        r2.type = r3;
    L_0x02b4:
        if (r53 == 0) goto L_0x045f;
    L_0x02b6:
        r11 = r53.toString();
    L_0x02ba:
        r7 = new java.util.HashMap;
        r7.<init>();
        if (r47 == 0) goto L_0x02c9;
    L_0x02c1:
        r2 = "originalPath";
        r0 = r47;
        r7.put(r2, r0);
    L_0x02c9:
        r5 = r21;
        r6 = r46;
        r2 = new org.telegram.messenger.SendMessagesHelper$$Lambda$14;
        r3 = r55;
        r4 = r45;
        r8 = r50;
        r10 = r52;
        r12 = r54;
        r2.<init>(r3, r4, r5, r6, r7, r8, r10, r11, r12);
        org.telegram.messenger.AndroidUtilities.runOnUIThread(r2);
        r2 = 1;
        goto L_0x000b;
    L_0x02e2:
        r34 = 0;
        goto L_0x007a;
    L_0x02e6:
        r13 = 0;
        goto L_0x007d;
    L_0x02e9:
        r2 = 46;
        r0 = r46;
        r33 = r0.lastIndexOf(r2);
        r2 = -1;
        r0 = r33;
        if (r0 == r2) goto L_0x0088;
    L_0x02f6:
        r2 = r33 + 1;
        r0 = r46;
        r26 = r0.substring(r2);
        goto L_0x0088;
    L_0x0300:
        r2 = "opus";
        r0 = r27;
        r2 = r0.equals(r2);
        if (r2 != 0) goto L_0x0321;
    L_0x030b:
        r2 = "ogg";
        r0 = r27;
        r2 = r0.equals(r2);
        if (r2 != 0) goto L_0x0321;
    L_0x0316:
        r2 = "flac";
        r0 = r27;
        r2 = r0.equals(r2);
        if (r2 == 0) goto L_0x00c2;
    L_0x0321:
        r36 = 0;
        r37 = new android.media.MediaMetadataRetriever;	 Catch:{ Exception -> 0x0380 }
        r37.<init>();	 Catch:{ Exception -> 0x0380 }
        r2 = r29.getAbsolutePath();	 Catch:{ Exception -> 0x0469, all -> 0x0464 }
        r0 = r37;
        r0.setDataSource(r2);	 Catch:{ Exception -> 0x0469, all -> 0x0464 }
        r2 = 9;
        r0 = r37;
        r22 = r0.extractMetadata(r2);	 Catch:{ Exception -> 0x0469, all -> 0x0464 }
        if (r22 == 0) goto L_0x0359;
    L_0x033b:
        r2 = java.lang.Long.parseLong(r22);	 Catch:{ Exception -> 0x0469, all -> 0x0464 }
        r2 = (float) r2;	 Catch:{ Exception -> 0x0469, all -> 0x0464 }
        r3 = 1148846080; // 0x447a0000 float:1000.0 double:5.676053805E-315;
        r2 = r2 / r3;
        r2 = (double) r2;	 Catch:{ Exception -> 0x0469, all -> 0x0464 }
        r2 = java.lang.Math.ceil(r2);	 Catch:{ Exception -> 0x0469, all -> 0x0464 }
        r0 = (int) r2;	 Catch:{ Exception -> 0x0469, all -> 0x0464 }
        r24 = r0;
        r2 = 7;
        r0 = r37;
        r43 = r0.extractMetadata(r2);	 Catch:{ Exception -> 0x0469, all -> 0x0464 }
        r2 = 2;
        r0 = r37;
        r41 = r0.extractMetadata(r2);	 Catch:{ Exception -> 0x0469, all -> 0x0464 }
    L_0x0359:
        if (r55 != 0) goto L_0x0373;
    L_0x035b:
        r2 = "ogg";
        r0 = r27;
        r2 = r0.equals(r2);	 Catch:{ Exception -> 0x0469, all -> 0x0464 }
        if (r2 == 0) goto L_0x0373;
    L_0x0366:
        r2 = r29.getAbsolutePath();	 Catch:{ Exception -> 0x0469, all -> 0x0464 }
        r2 = org.telegram.messenger.MediaController.isOpusFile(r2);	 Catch:{ Exception -> 0x0469, all -> 0x0464 }
        r3 = 1;
        if (r2 != r3) goto L_0x0373;
    L_0x0371:
        r35 = 1;
    L_0x0373:
        if (r37 == 0) goto L_0x00c2;
    L_0x0375:
        r37.release();	 Catch:{ Exception -> 0x037a }
        goto L_0x00c2;
    L_0x037a:
        r25 = move-exception;
        org.telegram.messenger.FileLog.e(r25);
        goto L_0x00c2;
    L_0x0380:
        r25 = move-exception;
    L_0x0381:
        org.telegram.messenger.FileLog.e(r25);	 Catch:{ all -> 0x0391 }
        if (r36 == 0) goto L_0x00c2;
    L_0x0386:
        r36.release();	 Catch:{ Exception -> 0x038b }
        goto L_0x00c2;
    L_0x038b:
        r25 = move-exception;
        org.telegram.messenger.FileLog.e(r25);
        goto L_0x00c2;
    L_0x0391:
        r2 = move-exception;
    L_0x0392:
        if (r36 == 0) goto L_0x0397;
    L_0x0394:
        r36.release();	 Catch:{ Exception -> 0x0398 }
    L_0x0397:
        throw r2;
    L_0x0398:
        r25 = move-exception;
        org.telegram.messenger.FileLog.e(r25);
        goto L_0x0397;
    L_0x039d:
        if (r14 == 0) goto L_0x03bf;
    L_0x039f:
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r0 = r47;
        r2 = r2.append(r0);
        r3 = "audio";
        r2 = r2.append(r3);
        r8 = r29.length();
        r2 = r2.append(r8);
        r47 = r2.toString();
        goto L_0x0109;
    L_0x03bf:
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r0 = r47;
        r2 = r2.append(r0);
        r3 = "";
        r2 = r2.append(r3);
        r8 = r29.length();
        r2 = r2.append(r8);
        r47 = r2.toString();
        goto L_0x0109;
    L_0x03df:
        r2 = 4;
        goto L_0x0116;
    L_0x03e2:
        r2 = 4;
        goto L_0x0146;
    L_0x03e5:
        r3 = "webp";
        r0 = r27;
        r3 = r0.equals(r3);
        if (r3 == 0) goto L_0x019e;
    L_0x03f0:
        r2 = 0;
        goto L_0x019e;
    L_0x03f3:
        r3 = "opus";
        r0 = r27;
        r3 = r0.equals(r3);
        if (r3 == 0) goto L_0x019e;
    L_0x03fe:
        r2 = 1;
        goto L_0x019e;
    L_0x0401:
        r3 = "ogg";
        r0 = r27;
        r3 = r0.equals(r3);
        if (r3 == 0) goto L_0x019e;
    L_0x040c:
        r2 = 2;
        goto L_0x019e;
    L_0x040f:
        r3 = "flac";
        r0 = r27;
        r3 = r0.equals(r3);
        if (r3 == 0) goto L_0x019e;
    L_0x041a:
        r2 = 3;
        goto L_0x019e;
    L_0x041d:
        r2 = "image/webp";
        r0 = r21;
        r0.mime_type = r2;
        goto L_0x01b1;
    L_0x0426:
        r2 = "audio/opus";
        r0 = r21;
        r0.mime_type = r2;
        goto L_0x01b1;
    L_0x042f:
        r2 = "audio/ogg";
        r0 = r21;
        r0.mime_type = r2;
        goto L_0x01b1;
    L_0x0438:
        r2 = "audio/flac";
        r0 = r21;
        r0.mime_type = r2;
        goto L_0x01b1;
    L_0x0441:
        r2 = "application/octet-stream";
        r0 = r21;
        r0.mime_type = r2;
        goto L_0x01b1;
    L_0x044a:
        r2 = "application/octet-stream";
        r0 = r21;
        r0.mime_type = r2;
        goto L_0x01b1;
    L_0x0453:
        r25 = move-exception;
        org.telegram.messenger.FileLog.e(r25);
        goto L_0x0202;
    L_0x0459:
        r25 = move-exception;
        org.telegram.messenger.FileLog.e(r25);
        goto L_0x024a;
    L_0x045f:
        r11 = "";
        goto L_0x02ba;
    L_0x0464:
        r2 = move-exception;
        r36 = r37;
        goto L_0x0392;
    L_0x0469:
        r25 = move-exception;
        r36 = r37;
        goto L_0x0381;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.SendMessagesHelper.prepareSendingDocumentInternal(int, java.lang.String, java.lang.String, android.net.Uri, java.lang.String, long, org.telegram.messenger.MessageObject, java.lang.CharSequence, java.util.ArrayList, org.telegram.messenger.MessageObject):boolean");
    }

    static final /* synthetic */ void lambda$prepareSendingDocumentInternal$43$SendMessagesHelper(MessageObject editingMessageObject, int currentAccount, TL_document documentFinal, String pathFinal, HashMap params, long dialog_id, MessageObject reply_to_msg, String captionFinal, ArrayList entities) {
        if (editingMessageObject != null) {
            getInstance(currentAccount).editMessageMedia(editingMessageObject, null, null, documentFinal, pathFinal, params, false);
        } else {
            getInstance(currentAccount).sendMessage(documentFinal, null, pathFinal, dialog_id, reply_to_msg, captionFinal, entities, null, params, 0);
        }
    }

    public static void prepareSendingDocument(String path, String originalPath, Uri uri, String mine, long dialog_id, MessageObject reply_to_msg, InputContentInfoCompat inputContent, MessageObject editingMessageObject) {
        if ((path != null && originalPath != null) || uri != null) {
            ArrayList<String> paths = new ArrayList();
            ArrayList<String> originalPaths = new ArrayList();
            ArrayList<Uri> uris = null;
            if (uri != null) {
                uris = new ArrayList();
                uris.add(uri);
            }
            if (path != null) {
                paths.add(path);
                originalPaths.add(originalPath);
            }
            prepareSendingDocuments(paths, originalPaths, uris, mine, dialog_id, reply_to_msg, inputContent, editingMessageObject);
        }
    }

    public static void prepareSendingAudioDocuments(ArrayList<MessageObject> messageObjects, long dialog_id, MessageObject reply_to_msg, MessageObject editingMessageObject) {
        new Thread(new SendMessagesHelper$$Lambda$15(messageObjects, dialog_id, UserConfig.selectedAccount, editingMessageObject, reply_to_msg)).start();
    }

    static final /* synthetic */ void lambda$prepareSendingAudioDocuments$45$SendMessagesHelper(ArrayList messageObjects, long dialog_id, int currentAccount, MessageObject editingMessageObject, MessageObject reply_to_msg) {
        int size = messageObjects.size();
        for (int a = 0; a < size; a++) {
            MessageObject messageObject = (MessageObject) messageObjects.get(a);
            String originalPath = messageObject.messageOwner.attachPath;
            File f = new File(originalPath);
            boolean isEncrypted = ((int) dialog_id) == 0;
            if (originalPath != null) {
                originalPath = originalPath + MimeTypes.BASE_TYPE_AUDIO + f.length();
            }
            TL_document tL_document = null;
            if (!isEncrypted) {
                tL_document = (TL_document) MessagesStorage.getInstance(currentAccount).getSentFile(originalPath, !isEncrypted ? 1 : 4);
            }
            if (tL_document == null) {
                tL_document = messageObject.messageOwner.media.document;
            }
            if (isEncrypted) {
                if (MessagesController.getInstance(currentAccount).getEncryptedChat(Integer.valueOf((int) (dialog_id >> 32))) == null) {
                    return;
                }
            }
            HashMap<String, String> params = new HashMap();
            if (originalPath != null) {
                params.put("originalPath", originalPath);
            }
            AndroidUtilities.runOnUIThread(new SendMessagesHelper$$Lambda$32(editingMessageObject, currentAccount, tL_document, messageObject, params, dialog_id, reply_to_msg));
        }
    }

    static final /* synthetic */ void lambda$null$44$SendMessagesHelper(MessageObject editingMessageObject, int currentAccount, TL_document documentFinal, MessageObject messageObject, HashMap params, long dialog_id, MessageObject reply_to_msg) {
        if (editingMessageObject != null) {
            getInstance(currentAccount).editMessageMedia(editingMessageObject, null, null, documentFinal, messageObject.messageOwner.attachPath, params, false);
            return;
        }
        getInstance(currentAccount).sendMessage(documentFinal, null, messageObject.messageOwner.attachPath, dialog_id, reply_to_msg, null, null, null, params, 0);
    }

    public static void prepareSendingDocuments(ArrayList<String> paths, ArrayList<String> originalPaths, ArrayList<Uri> uris, String mime, long dialog_id, MessageObject reply_to_msg, InputContentInfoCompat inputContent, MessageObject editingMessageObject) {
        if (paths != null || originalPaths != null || uris != null) {
            if (paths == null || originalPaths == null || paths.size() == originalPaths.size()) {
                new Thread(new SendMessagesHelper$$Lambda$16(paths, UserConfig.selectedAccount, originalPaths, mime, dialog_id, reply_to_msg, editingMessageObject, uris, inputContent)).start();
            }
        }
    }

    static final /* synthetic */ void lambda$prepareSendingDocuments$47$SendMessagesHelper(ArrayList paths, int currentAccount, ArrayList originalPaths, String mime, long dialog_id, MessageObject reply_to_msg, MessageObject editingMessageObject, ArrayList uris, InputContentInfoCompat inputContent) {
        int a;
        boolean error = false;
        if (paths != null) {
            for (a = 0; a < paths.size(); a++) {
                if (!prepareSendingDocumentInternal(currentAccount, (String) paths.get(a), (String) originalPaths.get(a), null, mime, dialog_id, reply_to_msg, null, null, editingMessageObject)) {
                    error = true;
                }
            }
        }
        if (uris != null) {
            for (a = 0; a < uris.size(); a++) {
                if (!prepareSendingDocumentInternal(currentAccount, null, null, (Uri) uris.get(a), mime, dialog_id, reply_to_msg, null, null, editingMessageObject)) {
                    error = true;
                }
            }
        }
        if (inputContent != null) {
            inputContent.releasePermission();
        }
        if (error) {
            AndroidUtilities.runOnUIThread(SendMessagesHelper$$Lambda$31.$instance);
        }
    }

    static final /* synthetic */ void lambda$null$46$SendMessagesHelper() {
        try {
            Toast.makeText(ApplicationLoader.applicationContext, LocaleController.getString("UnsupportedAttachment", R.string.UnsupportedAttachment), 0).show();
        } catch (Throwable e) {
            FileLog.e(e);
        }
    }

    public static void prepareSendingPhoto(String imageFilePath, Uri imageUri, long dialog_id, MessageObject reply_to_msg, CharSequence caption, ArrayList<MessageEntity> entities, ArrayList<InputDocument> stickers, InputContentInfoCompat inputContent, int ttl, MessageObject editingMessageObject) {
        SendingMediaInfo info = new SendingMediaInfo();
        info.path = imageFilePath;
        info.uri = imageUri;
        if (caption != null) {
            info.caption = caption.toString();
        }
        info.entities = entities;
        info.ttl = ttl;
        if (!(stickers == null || stickers.isEmpty())) {
            info.masks = new ArrayList(stickers);
        }
        ArrayList<SendingMediaInfo> infos = new ArrayList();
        infos.add(info);
        prepareSendingMedia(infos, dialog_id, reply_to_msg, inputContent, false, false, editingMessageObject);
    }

    public static void prepareSendingBotContextResult(BotInlineResult result, HashMap<String, String> params, long dialog_id, MessageObject reply_to_msg) {
        if (result != null) {
            int currentAccount = UserConfig.selectedAccount;
            if (result.send_message instanceof TL_botInlineMessageMediaAuto) {
                new Thread(new SendMessagesHelper$$Lambda$17(result, dialog_id, currentAccount, params, reply_to_msg)).run();
            } else if (result.send_message instanceof TL_botInlineMessageText) {
                boolean z;
                WebPage webPage = null;
                if (((int) dialog_id) == 0) {
                    for (int a = 0; a < result.send_message.entities.size(); a++) {
                        MessageEntity entity = (MessageEntity) result.send_message.entities.get(a);
                        if (entity instanceof TL_messageEntityUrl) {
                            webPage = new TL_webPagePending();
                            webPage.url = result.send_message.message.substring(entity.offset, entity.offset + entity.length);
                            break;
                        }
                    }
                }
                SendMessagesHelper instance = getInstance(currentAccount);
                String str = result.send_message.message;
                if (result.send_message.no_webpage) {
                    z = false;
                } else {
                    z = true;
                }
                instance.sendMessage(str, dialog_id, reply_to_msg, webPage, z, result.send_message.entities, result.send_message.reply_markup, params);
            } else if (result.send_message instanceof TL_botInlineMessageMediaVenue) {
                MessageMedia venue = new TL_messageMediaVenue();
                venue.geo = result.send_message.geo;
                venue.address = result.send_message.address;
                venue.title = result.send_message.title;
                venue.provider = result.send_message.provider;
                venue.venue_id = result.send_message.venue_id;
                String str2 = result.send_message.venue_type;
                venue.venue_id = str2;
                venue.venue_type = str2;
                if (venue.venue_type == null) {
                    venue.venue_type = TtmlNode.ANONYMOUS_REGION_ID;
                }
                getInstance(currentAccount).sendMessage(venue, dialog_id, reply_to_msg, result.send_message.reply_markup, (HashMap) params);
            } else if (result.send_message instanceof TL_botInlineMessageMediaGeo) {
                MessageMedia location;
                if (result.send_message.period != 0) {
                    location = new TL_messageMediaGeoLive();
                    location.period = result.send_message.period;
                    location.geo = result.send_message.geo;
                    getInstance(currentAccount).sendMessage(location, dialog_id, reply_to_msg, result.send_message.reply_markup, (HashMap) params);
                    return;
                }
                location = new TL_messageMediaGeo();
                location.geo = result.send_message.geo;
                getInstance(currentAccount).sendMessage(location, dialog_id, reply_to_msg, result.send_message.reply_markup, (HashMap) params);
            } else if (result.send_message instanceof TL_botInlineMessageMediaContact) {
                User user = new TL_user();
                user.phone = result.send_message.phone_number;
                user.first_name = result.send_message.first_name;
                user.last_name = result.send_message.last_name;
                getInstance(currentAccount).sendMessage(user, dialog_id, reply_to_msg, result.send_message.reply_markup, (HashMap) params);
            }
        }
    }

    static final /* synthetic */ void lambda$prepareSendingBotContextResult$49$SendMessagesHelper(BotInlineResult result, long dialog_id, int currentAccount, HashMap params, MessageObject reply_to_msg) {
        String finalPath = null;
        TL_document document = null;
        TL_photo photo = null;
        TL_game game = null;
        if (!(result instanceof TL_botInlineMediaResult)) {
            if (result.content != null) {
                File file = new File(FileLoader.getDirectory(4), Utilities.MD5(result.content.url) + "." + ImageLoader.getHttpUrlExtension(result.content.url, "file"));
                if (file.exists()) {
                    finalPath = file.getAbsolutePath();
                } else {
                    finalPath = result.content.url;
                }
                String str = result.type;
                Object obj = -1;
                switch (str.hashCode()) {
                    case -1890252483:
                        if (str.equals("sticker")) {
                            obj = 4;
                            break;
                        }
                        break;
                    case 102340:
                        if (str.equals("gif")) {
                            obj = 5;
                            break;
                        }
                        break;
                    case 3143036:
                        if (str.equals("file")) {
                            obj = 2;
                            break;
                        }
                        break;
                    case 93166550:
                        if (str.equals(MimeTypes.BASE_TYPE_AUDIO)) {
                            obj = null;
                            break;
                        }
                        break;
                    case 106642994:
                        if (str.equals("photo")) {
                            obj = 6;
                            break;
                        }
                        break;
                    case 112202875:
                        if (str.equals(MimeTypes.BASE_TYPE_VIDEO)) {
                            obj = 3;
                            break;
                        }
                        break;
                    case 112386354:
                        if (str.equals("voice")) {
                            obj = 1;
                            break;
                        }
                        break;
                }
                int[] wh;
                switch (obj) {
                    case null:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                        document = new TL_document();
                        document.id = 0;
                        document.size = 0;
                        document.dc_id = 0;
                        document.mime_type = result.content.mime_type;
                        document.date = ConnectionsManager.getInstance(currentAccount).getCurrentTime();
                        TL_documentAttributeFilename fileName = new TL_documentAttributeFilename();
                        document.attributes.add(fileName);
                        str = result.type;
                        obj = -1;
                        switch (str.hashCode()) {
                            case -1890252483:
                                if (str.equals("sticker")) {
                                    obj = 5;
                                    break;
                                }
                                break;
                            case 102340:
                                if (str.equals("gif")) {
                                    obj = null;
                                    break;
                                }
                                break;
                            case 3143036:
                                if (str.equals("file")) {
                                    obj = 3;
                                    break;
                                }
                                break;
                            case 93166550:
                                if (str.equals(MimeTypes.BASE_TYPE_AUDIO)) {
                                    obj = 2;
                                    break;
                                }
                                break;
                            case 112202875:
                                if (str.equals(MimeTypes.BASE_TYPE_VIDEO)) {
                                    obj = 4;
                                    break;
                                }
                                break;
                            case 112386354:
                                if (str.equals("voice")) {
                                    obj = 1;
                                    break;
                                }
                                break;
                        }
                        Bitmap bitmap;
                        TL_documentAttributeAudio audio;
                        switch (obj) {
                            case null:
                                fileName.file_name = "animation.gif";
                                if (finalPath.endsWith("mp4")) {
                                    document.mime_type = MimeTypes.VIDEO_MP4;
                                    document.attributes.add(new TL_documentAttributeAnimated());
                                } else {
                                    document.mime_type = "image/gif";
                                }
                                try {
                                    if (finalPath.endsWith("mp4")) {
                                        bitmap = ThumbnailUtils.createVideoThumbnail(finalPath, 1);
                                    } else {
                                        bitmap = ImageLoader.loadBitmap(finalPath, null, 90.0f, 90.0f, true);
                                    }
                                    if (bitmap != null) {
                                        document.thumb = ImageLoader.scaleAndSaveImage(bitmap, 90.0f, 90.0f, 55, false);
                                        bitmap.recycle();
                                        break;
                                    }
                                } catch (Throwable e) {
                                    FileLog.e(e);
                                    break;
                                }
                                break;
                            case 1:
                                audio = new TL_documentAttributeAudio();
                                audio.duration = MessageObject.getInlineResultDuration(result);
                                audio.voice = true;
                                fileName.file_name = "audio.ogg";
                                document.attributes.add(audio);
                                document.thumb = new TL_photoSizeEmpty();
                                document.thumb.type = "s";
                                break;
                            case 2:
                                audio = new TL_documentAttributeAudio();
                                audio.duration = MessageObject.getInlineResultDuration(result);
                                audio.title = result.title;
                                audio.flags |= 1;
                                if (result.description != null) {
                                    audio.performer = result.description;
                                    audio.flags |= 2;
                                }
                                fileName.file_name = "audio.mp3";
                                document.attributes.add(audio);
                                document.thumb = new TL_photoSizeEmpty();
                                document.thumb.type = "s";
                                break;
                            case 3:
                                int idx = result.content.mime_type.lastIndexOf(47);
                                if (idx == -1) {
                                    fileName.file_name = "file";
                                    break;
                                } else {
                                    fileName.file_name = "file." + result.content.mime_type.substring(idx + 1);
                                    break;
                                }
                            case 4:
                                fileName.file_name = "video.mp4";
                                TL_documentAttributeVideo attributeVideo = new TL_documentAttributeVideo();
                                wh = MessageObject.getInlineResultWidthAndHeight(result);
                                attributeVideo.w = wh[0];
                                attributeVideo.h = wh[1];
                                attributeVideo.duration = MessageObject.getInlineResultDuration(result);
                                attributeVideo.supports_streaming = true;
                                document.attributes.add(attributeVideo);
                                try {
                                    if (result.thumb != null) {
                                        bitmap = ImageLoader.loadBitmap(new File(FileLoader.getDirectory(4), Utilities.MD5(result.thumb.url) + "." + ImageLoader.getHttpUrlExtension(result.thumb.url, "jpg")).getAbsolutePath(), null, 90.0f, 90.0f, true);
                                        if (bitmap != null) {
                                            document.thumb = ImageLoader.scaleAndSaveImage(bitmap, 90.0f, 90.0f, 55, false);
                                            bitmap.recycle();
                                            break;
                                        }
                                    }
                                } catch (Throwable e2) {
                                    FileLog.e(e2);
                                    break;
                                }
                                break;
                            case 5:
                                TL_documentAttributeSticker attributeSticker = new TL_documentAttributeSticker();
                                attributeSticker.alt = TtmlNode.ANONYMOUS_REGION_ID;
                                attributeSticker.stickerset = new TL_inputStickerSetEmpty();
                                document.attributes.add(attributeSticker);
                                TL_documentAttributeImageSize attributeImageSize = new TL_documentAttributeImageSize();
                                wh = MessageObject.getInlineResultWidthAndHeight(result);
                                attributeImageSize.w = wh[0];
                                attributeImageSize.h = wh[1];
                                document.attributes.add(attributeImageSize);
                                fileName.file_name = "sticker.webp";
                                try {
                                    if (result.thumb != null) {
                                        bitmap = ImageLoader.loadBitmap(new File(FileLoader.getDirectory(4), Utilities.MD5(result.thumb.url) + "." + ImageLoader.getHttpUrlExtension(result.thumb.url, "webp")).getAbsolutePath(), null, 90.0f, 90.0f, true);
                                        if (bitmap != null) {
                                            document.thumb = ImageLoader.scaleAndSaveImage(bitmap, 90.0f, 90.0f, 55, false);
                                            bitmap.recycle();
                                            break;
                                        }
                                    }
                                } catch (Throwable e22) {
                                    FileLog.e(e22);
                                    break;
                                }
                                break;
                        }
                        if (fileName.file_name == null) {
                            fileName.file_name = "file";
                        }
                        if (document.mime_type == null) {
                            document.mime_type = "application/octet-stream";
                        }
                        if (document.thumb == null) {
                            document.thumb = new TL_photoSize();
                            wh = MessageObject.getInlineResultWidthAndHeight(result);
                            document.thumb.w = wh[0];
                            document.thumb.h = wh[1];
                            document.thumb.size = 0;
                            document.thumb.location = new TL_fileLocationUnavailable();
                            document.thumb.type = "x";
                            break;
                        }
                        break;
                    case 6:
                        if (file.exists()) {
                            photo = getInstance(currentAccount).generatePhotoSizes(finalPath, null);
                        }
                        if (photo == null) {
                            photo = new TL_photo();
                            photo.date = ConnectionsManager.getInstance(currentAccount).getCurrentTime();
                            TL_photoSize photoSize = new TL_photoSize();
                            wh = MessageObject.getInlineResultWidthAndHeight(result);
                            photoSize.w = wh[0];
                            photoSize.h = wh[1];
                            photoSize.size = 1;
                            photoSize.location = new TL_fileLocationUnavailable();
                            photoSize.type = "x";
                            photo.sizes.add(photoSize);
                            break;
                        }
                        break;
                    default:
                        break;
                }
            }
        } else if (result.type.equals("game")) {
            if (((int) dialog_id) != 0) {
                game = new TL_game();
                game.title = result.title;
                game.description = result.description;
                game.short_name = result.id;
                game.photo = result.photo;
                if (result.document instanceof TL_document) {
                    game.document = result.document;
                    game.flags |= 1;
                }
            } else {
                return;
            }
        } else if (result.document != null) {
            if (result.document instanceof TL_document) {
                document = (TL_document) result.document;
            }
        } else if (result.photo != null && (result.photo instanceof TL_photo)) {
            photo = (TL_photo) result.photo;
        }
        String finalPathFinal = finalPath;
        TL_document finalDocument = document;
        TL_photo finalPhoto = photo;
        TL_game finalGame = game;
        if (!(params == null || result.content == null)) {
            params.put("originalPath", result.content.url);
        }
        AndroidUtilities.runOnUIThread(new SendMessagesHelper$$Lambda$30(finalDocument, currentAccount, finalPathFinal, dialog_id, reply_to_msg, result, params, finalPhoto, finalGame));
    }

    static final /* synthetic */ void lambda$null$48$SendMessagesHelper(TL_document finalDocument, int currentAccount, String finalPathFinal, long dialog_id, MessageObject reply_to_msg, BotInlineResult result, HashMap params, TL_photo finalPhoto, TL_game finalGame) {
        if (finalDocument != null) {
            getInstance(currentAccount).sendMessage(finalDocument, null, finalPathFinal, dialog_id, reply_to_msg, result.send_message.message, result.send_message.entities, result.send_message.reply_markup, params, 0);
        } else if (finalPhoto != null) {
            getInstance(currentAccount).sendMessage(finalPhoto, result.content != null ? result.content.url : null, dialog_id, reply_to_msg, result.send_message.message, result.send_message.entities, result.send_message.reply_markup, params, 0);
        } else if (finalGame != null) {
            getInstance(currentAccount).sendMessage(finalGame, dialog_id, result.send_message.reply_markup, params);
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

    public static void prepareSendingText(String text, long dialog_id) {
        int currentAccount = UserConfig.selectedAccount;
        MessagesStorage.getInstance(currentAccount).getStorageQueue().postRunnable(new SendMessagesHelper$$Lambda$18(text, currentAccount, dialog_id));
    }

    static final /* synthetic */ void lambda$null$50$SendMessagesHelper(String text, int currentAccount, long dialog_id) {
        String textFinal = getTrimmedString(text);
        if (textFinal.length() != 0) {
            int count = (int) Math.ceil((double) (((float) textFinal.length()) / 4096.0f));
            for (int a = 0; a < count; a++) {
                getInstance(currentAccount).sendMessage(textFinal.substring(a * 4096, Math.min((a + 1) * 4096, textFinal.length())), dialog_id, null, null, true, null, null, null);
            }
        }
    }

    public static void prepareSendingMedia(ArrayList<SendingMediaInfo> media, long dialog_id, MessageObject reply_to_msg, InputContentInfoCompat inputContent, boolean forceDocument, boolean groupPhotos, MessageObject editingMessageObject) {
        if (!media.isEmpty()) {
            mediaSendQueue.postRunnable(new SendMessagesHelper$$Lambda$19(media, dialog_id, UserConfig.selectedAccount, forceDocument, groupPhotos, editingMessageObject, reply_to_msg, inputContent));
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static final /* synthetic */ void lambda$prepareSendingMedia$59$SendMessagesHelper(java.util.ArrayList r87, long r88, int r90, boolean r91, boolean r92, org.telegram.messenger.MessageObject r93, org.telegram.messenger.MessageObject r94, android.support.v13.view.inputmethod.InputContentInfoCompat r95) {
        /*
        r42 = java.lang.System.currentTimeMillis();
        r46 = r87.size();
        r0 = r88;
        r4 = (int) r0;
        if (r4 != 0) goto L_0x008b;
    L_0x000d:
        r58 = 1;
    L_0x000f:
        r51 = 0;
        if (r58 == 0) goto L_0x0030;
    L_0x0013:
        r4 = 32;
        r4 = r88 >> r4;
        r0 = (int) r4;
        r56 = r0;
        r4 = org.telegram.messenger.MessagesController.getInstance(r90);
        r5 = java.lang.Integer.valueOf(r56);
        r50 = r4.getEncryptedChat(r5);
        if (r50 == 0) goto L_0x0030;
    L_0x0028:
        r0 = r50;
        r4 = r0.layer;
        r51 = org.telegram.messenger.AndroidUtilities.getPeerLayerVersion(r4);
    L_0x0030:
        if (r58 == 0) goto L_0x0038;
    L_0x0032:
        r4 = 73;
        r0 = r51;
        if (r0 < r4) goto L_0x013b;
    L_0x0038:
        if (r91 != 0) goto L_0x013b;
    L_0x003a:
        if (r92 == 0) goto L_0x013b;
    L_0x003c:
        r86 = new java.util.HashMap;
        r86.<init>();
        r14 = 0;
    L_0x0042:
        r0 = r46;
        if (r14 >= r0) goto L_0x013d;
    L_0x0046:
        r0 = r87;
        r13 = r0.get(r14);
        r13 = (org.telegram.messenger.SendMessagesHelper.SendingMediaInfo) r13;
        r4 = r13.searchImage;
        if (r4 != 0) goto L_0x0088;
    L_0x0052:
        r4 = r13.isVideo;
        if (r4 != 0) goto L_0x0088;
    L_0x0056:
        r0 = r13.path;
        r66 = r0;
        r0 = r13.path;
        r81 = r0;
        if (r81 != 0) goto L_0x0070;
    L_0x0060:
        r4 = r13.uri;
        if (r4 == 0) goto L_0x0070;
    L_0x0064:
        r4 = r13.uri;
        r81 = org.telegram.messenger.AndroidUtilities.getPath(r4);
        r4 = r13.uri;
        r66 = r4.toString();
    L_0x0070:
        if (r81 == 0) goto L_0x008e;
    L_0x0072:
        r4 = ".gif";
        r0 = r81;
        r4 = r0.endsWith(r4);
        if (r4 != 0) goto L_0x0088;
    L_0x007d:
        r4 = ".webp";
        r0 = r81;
        r4 = r0.endsWith(r4);
        if (r4 == 0) goto L_0x008e;
    L_0x0088:
        r14 = r14 + 1;
        goto L_0x0042;
    L_0x008b:
        r58 = 0;
        goto L_0x000f;
    L_0x008e:
        if (r81 != 0) goto L_0x00a4;
    L_0x0090:
        r4 = r13.uri;
        if (r4 == 0) goto L_0x00a4;
    L_0x0094:
        r4 = r13.uri;
        r4 = org.telegram.messenger.MediaController.isGif(r4);
        if (r4 != 0) goto L_0x0088;
    L_0x009c:
        r4 = r13.uri;
        r4 = org.telegram.messenger.MediaController.isWebp(r4);
        if (r4 != 0) goto L_0x0088;
    L_0x00a4:
        if (r81 == 0) goto L_0x011a;
    L_0x00a6:
        r80 = new java.io.File;
        r80.<init>(r81);
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r0 = r66;
        r4 = r4.append(r0);
        r10 = r80.length();
        r4 = r4.append(r10);
        r5 = "_";
        r4 = r4.append(r5);
        r10 = r80.lastModified();
        r4 = r4.append(r10);
        r66 = r4.toString();
    L_0x00d1:
        r69 = 0;
        if (r58 != 0) goto L_0x0101;
    L_0x00d5:
        r4 = r13.ttl;
        if (r4 != 0) goto L_0x0101;
    L_0x00d9:
        r5 = org.telegram.messenger.MessagesStorage.getInstance(r90);
        if (r58 != 0) goto L_0x011d;
    L_0x00df:
        r4 = 0;
    L_0x00e0:
        r0 = r66;
        r69 = r5.getSentFile(r0, r4);
        r69 = (org.telegram.tgnet.TLRPC.TL_photo) r69;
        if (r69 != 0) goto L_0x0101;
    L_0x00ea:
        r4 = r13.uri;
        if (r4 == 0) goto L_0x0101;
    L_0x00ee:
        r5 = org.telegram.messenger.MessagesStorage.getInstance(r90);
        r4 = r13.uri;
        r6 = org.telegram.messenger.AndroidUtilities.getPath(r4);
        if (r58 != 0) goto L_0x011f;
    L_0x00fa:
        r4 = 0;
    L_0x00fb:
        r69 = r5.getSentFile(r6, r4);
        r69 = (org.telegram.tgnet.TLRPC.TL_photo) r69;
    L_0x0101:
        r85 = new org.telegram.messenger.SendMessagesHelper$MediaSendPrepareWorker;
        r4 = 0;
        r0 = r85;
        r0.<init>();
        r0 = r86;
        r1 = r85;
        r0.put(r13, r1);
        if (r69 == 0) goto L_0x0121;
    L_0x0112:
        r0 = r69;
        r1 = r85;
        r1.photo = r0;
        goto L_0x0088;
    L_0x011a:
        r66 = 0;
        goto L_0x00d1;
    L_0x011d:
        r4 = 3;
        goto L_0x00e0;
    L_0x011f:
        r4 = 3;
        goto L_0x00fb;
    L_0x0121:
        r4 = new java.util.concurrent.CountDownLatch;
        r5 = 1;
        r4.<init>(r5);
        r0 = r85;
        r0.sync = r4;
        r4 = mediaSendThreadPool;
        r5 = new org.telegram.messenger.SendMessagesHelper$$Lambda$22;
        r0 = r85;
        r1 = r90;
        r5.<init>(r0, r1, r13);
        r4.execute(r5);
        goto L_0x0088;
    L_0x013b:
        r86 = 0;
    L_0x013d:
        r54 = 0;
        r60 = 0;
        r72 = 0;
        r75 = 0;
        r73 = 0;
        r74 = 0;
        r52 = 0;
        r71 = 0;
        r14 = 0;
    L_0x014e:
        r0 = r46;
        if (r14 >= r0) goto L_0x0a57;
    L_0x0152:
        r0 = r87;
        r13 = r0.get(r14);
        r13 = (org.telegram.messenger.SendMessagesHelper.SendingMediaInfo) r13;
        if (r92 == 0) goto L_0x0177;
    L_0x015c:
        if (r58 == 0) goto L_0x0164;
    L_0x015e:
        r4 = 73;
        r0 = r51;
        if (r0 < r4) goto L_0x0177;
    L_0x0164:
        r4 = 1;
        r0 = r46;
        if (r0 <= r4) goto L_0x0177;
    L_0x0169:
        r4 = r71 % 10;
        if (r4 != 0) goto L_0x0177;
    L_0x016d:
        r4 = org.telegram.messenger.Utilities.random;
        r54 = r4.nextLong();
        r60 = r54;
        r71 = 0;
    L_0x0177:
        r4 = r13.searchImage;
        if (r4 == 0) goto L_0x0501;
    L_0x017b:
        r4 = r13.searchImage;
        r4 = r4.type;
        r5 = 1;
        if (r4 != r5) goto L_0x037c;
    L_0x0182:
        r9 = new java.util.HashMap;
        r9.<init>();
        r48 = 0;
        r4 = r13.searchImage;
        r4 = r4.document;
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_document;
        if (r4 == 0) goto L_0x02f9;
    L_0x0191:
        r4 = r13.searchImage;
        r0 = r4.document;
        r48 = r0;
        r48 = (org.telegram.tgnet.TLRPC.TL_document) r48;
        r4 = 1;
        r0 = r48;
        r45 = org.telegram.messenger.FileLoader.getPathToAttach(r0, r4);
    L_0x01a0:
        if (r48 != 0) goto L_0x02c5;
    L_0x01a2:
        r4 = r13.searchImage;
        r4 = r4.localUrl;
        if (r4 == 0) goto L_0x01b2;
    L_0x01a8:
        r4 = "url";
        r5 = r13.searchImage;
        r5 = r5.localUrl;
        r9.put(r4, r5);
    L_0x01b2:
        r83 = 0;
        r48 = new org.telegram.tgnet.TLRPC$TL_document;
        r48.<init>();
        r4 = 0;
        r0 = r48;
        r0.id = r4;
        r4 = org.telegram.tgnet.ConnectionsManager.getInstance(r90);
        r4 = r4.getCurrentTime();
        r0 = r48;
        r0.date = r4;
        r53 = new org.telegram.tgnet.TLRPC$TL_documentAttributeFilename;
        r53.<init>();
        r4 = "animation.gif";
        r0 = r53;
        r0.file_name = r4;
        r0 = r48;
        r4 = r0.attributes;
        r0 = r53;
        r4.add(r0);
        r4 = r13.searchImage;
        r4 = r4.size;
        r0 = r48;
        r0.size = r4;
        r4 = 0;
        r0 = r48;
        r0.dc_id = r4;
        r4 = r45.toString();
        r5 = "mp4";
        r4 = r4.endsWith(r5);
        if (r4 == 0) goto L_0x0353;
    L_0x01fa:
        r4 = "video/mp4";
        r0 = r48;
        r0.mime_type = r4;
        r0 = r48;
        r4 = r0.attributes;
        r5 = new org.telegram.tgnet.TLRPC$TL_documentAttributeAnimated;
        r5.<init>();
        r4.add(r5);
    L_0x020d:
        r4 = r45.exists();
        if (r4 == 0) goto L_0x035c;
    L_0x0213:
        r83 = r45;
    L_0x0215:
        if (r83 != 0) goto L_0x0258;
    L_0x0217:
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = r13.searchImage;
        r5 = r5.thumbUrl;
        r5 = org.telegram.messenger.Utilities.MD5(r5);
        r4 = r4.append(r5);
        r5 = ".";
        r4 = r4.append(r5);
        r5 = r13.searchImage;
        r5 = r5.thumbUrl;
        r6 = "jpg";
        r5 = org.telegram.messenger.ImageLoader.getHttpUrlExtension(r5, r6);
        r4 = r4.append(r5);
        r82 = r4.toString();
        r83 = new java.io.File;
        r4 = 4;
        r4 = org.telegram.messenger.FileLoader.getDirectory(r4);
        r0 = r83;
        r1 = r82;
        r0.<init>(r4, r1);
        r4 = r83.exists();
        if (r4 != 0) goto L_0x0258;
    L_0x0256:
        r83 = 0;
    L_0x0258:
        if (r83 == 0) goto L_0x0287;
    L_0x025a:
        r4 = r83.getAbsolutePath();	 Catch:{ Exception -> 0x0370 }
        r5 = "mp4";
        r4 = r4.endsWith(r5);	 Catch:{ Exception -> 0x0370 }
        if (r4 == 0) goto L_0x0360;
    L_0x0267:
        r4 = r83.getAbsolutePath();	 Catch:{ Exception -> 0x0370 }
        r5 = 1;
        r44 = android.media.ThumbnailUtils.createVideoThumbnail(r4, r5);	 Catch:{ Exception -> 0x0370 }
    L_0x0270:
        if (r44 == 0) goto L_0x0287;
    L_0x0272:
        r4 = 1119092736; // 0x42b40000 float:90.0 double:5.529052754E-315;
        r5 = 1119092736; // 0x42b40000 float:90.0 double:5.529052754E-315;
        r6 = 55;
        r0 = r44;
        r1 = r58;
        r4 = org.telegram.messenger.ImageLoader.scaleAndSaveImage(r0, r4, r5, r6, r1);	 Catch:{ Exception -> 0x0370 }
        r0 = r48;
        r0.thumb = r4;	 Catch:{ Exception -> 0x0370 }
        r44.recycle();	 Catch:{ Exception -> 0x0370 }
    L_0x0287:
        r0 = r48;
        r4 = r0.thumb;
        if (r4 != 0) goto L_0x02c5;
    L_0x028d:
        r4 = new org.telegram.tgnet.TLRPC$TL_photoSize;
        r4.<init>();
        r0 = r48;
        r0.thumb = r4;
        r0 = r48;
        r4 = r0.thumb;
        r5 = r13.searchImage;
        r5 = r5.width;
        r4.w = r5;
        r0 = r48;
        r4 = r0.thumb;
        r5 = r13.searchImage;
        r5 = r5.height;
        r4.h = r5;
        r0 = r48;
        r4 = r0.thumb;
        r5 = 0;
        r4.size = r5;
        r0 = r48;
        r4 = r0.thumb;
        r5 = new org.telegram.tgnet.TLRPC$TL_fileLocationUnavailable;
        r5.<init>();
        r4.location = r5;
        r0 = r48;
        r4 = r0.thumb;
        r5 = "x";
        r4.type = r5;
    L_0x02c5:
        r7 = r48;
        r4 = r13.searchImage;
        r0 = r4.imageUrl;
        r67 = r0;
        if (r45 != 0) goto L_0x0376;
    L_0x02cf:
        r4 = r13.searchImage;
        r8 = r4.imageUrl;
    L_0x02d3:
        if (r9 == 0) goto L_0x02e5;
    L_0x02d5:
        r4 = r13.searchImage;
        r4 = r4.imageUrl;
        if (r4 == 0) goto L_0x02e5;
    L_0x02db:
        r4 = "originalPath";
        r5 = r13.searchImage;
        r5 = r5.imageUrl;
        r9.put(r4, r5);
    L_0x02e5:
        r4 = new org.telegram.messenger.SendMessagesHelper$$Lambda$23;
        r5 = r93;
        r6 = r90;
        r10 = r88;
        r12 = r94;
        r4.<init>(r5, r6, r7, r8, r9, r10, r12, r13);
        org.telegram.messenger.AndroidUtilities.runOnUIThread(r4);
    L_0x02f5:
        r14 = r14 + 1;
        goto L_0x014e;
    L_0x02f9:
        if (r58 != 0) goto L_0x0316;
    L_0x02fb:
        r5 = org.telegram.messenger.MessagesStorage.getInstance(r90);
        r4 = r13.searchImage;
        r6 = r4.imageUrl;
        if (r58 != 0) goto L_0x0351;
    L_0x0305:
        r4 = 1;
    L_0x0306:
        r47 = r5.getSentFile(r6, r4);
        r47 = (org.telegram.tgnet.TLRPC.Document) r47;
        r0 = r47;
        r4 = r0 instanceof org.telegram.tgnet.TLRPC.TL_document;
        if (r4 == 0) goto L_0x0316;
    L_0x0312:
        r48 = r47;
        r48 = (org.telegram.tgnet.TLRPC.TL_document) r48;
    L_0x0316:
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = r13.searchImage;
        r5 = r5.imageUrl;
        r5 = org.telegram.messenger.Utilities.MD5(r5);
        r4 = r4.append(r5);
        r5 = ".";
        r4 = r4.append(r5);
        r5 = r13.searchImage;
        r5 = r5.imageUrl;
        r6 = "jpg";
        r5 = org.telegram.messenger.ImageLoader.getHttpUrlExtension(r5, r6);
        r4 = r4.append(r5);
        r59 = r4.toString();
        r45 = new java.io.File;
        r4 = 4;
        r4 = org.telegram.messenger.FileLoader.getDirectory(r4);
        r0 = r45;
        r1 = r59;
        r0.<init>(r4, r1);
        goto L_0x01a0;
    L_0x0351:
        r4 = 4;
        goto L_0x0306;
    L_0x0353:
        r4 = "image/gif";
        r0 = r48;
        r0.mime_type = r4;
        goto L_0x020d;
    L_0x035c:
        r45 = 0;
        goto L_0x0215;
    L_0x0360:
        r4 = r83.getAbsolutePath();	 Catch:{ Exception -> 0x0370 }
        r5 = 0;
        r6 = 1119092736; // 0x42b40000 float:90.0 double:5.529052754E-315;
        r10 = 1119092736; // 0x42b40000 float:90.0 double:5.529052754E-315;
        r11 = 1;
        r44 = org.telegram.messenger.ImageLoader.loadBitmap(r4, r5, r6, r10, r11);	 Catch:{ Exception -> 0x0370 }
        goto L_0x0270;
    L_0x0370:
        r49 = move-exception;
        org.telegram.messenger.FileLog.e(r49);
        goto L_0x0287;
    L_0x0376:
        r8 = r45.toString();
        goto L_0x02d3;
    L_0x037c:
        r65 = 1;
        r69 = 0;
        r4 = r13.searchImage;
        r4 = r4.photo;
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_photo;
        if (r4 == 0) goto L_0x04e6;
    L_0x0388:
        r4 = r13.searchImage;
        r0 = r4.photo;
        r69 = r0;
        r69 = (org.telegram.tgnet.TLRPC.TL_photo) r69;
    L_0x0390:
        if (r69 != 0) goto L_0x0480;
    L_0x0392:
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = r13.searchImage;
        r5 = r5.imageUrl;
        r5 = org.telegram.messenger.Utilities.MD5(r5);
        r4 = r4.append(r5);
        r5 = ".";
        r4 = r4.append(r5);
        r5 = r13.searchImage;
        r5 = r5.imageUrl;
        r6 = "jpg";
        r5 = org.telegram.messenger.ImageLoader.getHttpUrlExtension(r5, r6);
        r4 = r4.append(r5);
        r59 = r4.toString();
        r45 = new java.io.File;
        r4 = 4;
        r4 = org.telegram.messenger.FileLoader.getDirectory(r4);
        r0 = r45;
        r1 = r59;
        r0.<init>(r4, r1);
        r4 = r45.exists();
        if (r4 == 0) goto L_0x03ec;
    L_0x03d1:
        r4 = r45.length();
        r10 = 0;
        r4 = (r4 > r10 ? 1 : (r4 == r10 ? 0 : -1));
        if (r4 == 0) goto L_0x03ec;
    L_0x03db:
        r4 = getInstance(r90);
        r5 = r45.toString();
        r6 = 0;
        r69 = r4.generatePhotoSizes(r5, r6);
        if (r69 == 0) goto L_0x03ec;
    L_0x03ea:
        r65 = 0;
    L_0x03ec:
        if (r69 != 0) goto L_0x0480;
    L_0x03ee:
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = r13.searchImage;
        r5 = r5.thumbUrl;
        r5 = org.telegram.messenger.Utilities.MD5(r5);
        r4 = r4.append(r5);
        r5 = ".";
        r4 = r4.append(r5);
        r5 = r13.searchImage;
        r5 = r5.thumbUrl;
        r6 = "jpg";
        r5 = org.telegram.messenger.ImageLoader.getHttpUrlExtension(r5, r6);
        r4 = r4.append(r5);
        r59 = r4.toString();
        r45 = new java.io.File;
        r4 = 4;
        r4 = org.telegram.messenger.FileLoader.getDirectory(r4);
        r0 = r45;
        r1 = r59;
        r0.<init>(r4, r1);
        r4 = r45.exists();
        if (r4 == 0) goto L_0x043a;
    L_0x042d:
        r4 = getInstance(r90);
        r5 = r45.toString();
        r6 = 0;
        r69 = r4.generatePhotoSizes(r5, r6);
    L_0x043a:
        if (r69 != 0) goto L_0x0480;
    L_0x043c:
        r69 = new org.telegram.tgnet.TLRPC$TL_photo;
        r69.<init>();
        r4 = org.telegram.tgnet.ConnectionsManager.getInstance(r90);
        r4 = r4.getCurrentTime();
        r0 = r69;
        r0.date = r4;
        r70 = new org.telegram.tgnet.TLRPC$TL_photoSize;
        r70.<init>();
        r4 = r13.searchImage;
        r4 = r4.width;
        r0 = r70;
        r0.w = r4;
        r4 = r13.searchImage;
        r4 = r4.height;
        r0 = r70;
        r0.h = r4;
        r4 = 0;
        r0 = r70;
        r0.size = r4;
        r4 = new org.telegram.tgnet.TLRPC$TL_fileLocationUnavailable;
        r4.<init>();
        r0 = r70;
        r0.location = r4;
        r4 = "x";
        r0 = r70;
        r0.type = r4;
        r0 = r69;
        r4 = r0.sizes;
        r0 = r70;
        r4.add(r0);
    L_0x0480:
        if (r69 == 0) goto L_0x02f5;
    L_0x0482:
        r18 = r69;
        r19 = r65;
        r9 = new java.util.HashMap;
        r9.<init>();
        r4 = r13.searchImage;
        r4 = r4.imageUrl;
        if (r4 == 0) goto L_0x049b;
    L_0x0491:
        r4 = "originalPath";
        r5 = r13.searchImage;
        r5 = r5.imageUrl;
        r9.put(r4, r5);
    L_0x049b:
        if (r92 == 0) goto L_0x04d0;
    L_0x049d:
        r71 = r71 + 1;
        r4 = "groupId";
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "";
        r5 = r5.append(r6);
        r0 = r54;
        r5 = r5.append(r0);
        r5 = r5.toString();
        r9.put(r4, r5);
        r4 = 10;
        r0 = r71;
        if (r0 == r4) goto L_0x04c5;
    L_0x04c1:
        r4 = r46 + -1;
        if (r14 != r4) goto L_0x04d0;
    L_0x04c5:
        r4 = "final";
        r5 = "1";
        r9.put(r4, r5);
        r60 = 0;
    L_0x04d0:
        r15 = new org.telegram.messenger.SendMessagesHelper$$Lambda$24;
        r16 = r93;
        r17 = r90;
        r20 = r13;
        r21 = r9;
        r22 = r88;
        r24 = r94;
        r15.<init>(r16, r17, r18, r19, r20, r21, r22, r24);
        org.telegram.messenger.AndroidUtilities.runOnUIThread(r15);
        goto L_0x02f5;
    L_0x04e6:
        if (r58 != 0) goto L_0x0390;
    L_0x04e8:
        r4 = r13.ttl;
        if (r4 != 0) goto L_0x0390;
    L_0x04ec:
        r5 = org.telegram.messenger.MessagesStorage.getInstance(r90);
        r4 = r13.searchImage;
        r6 = r4.imageUrl;
        if (r58 != 0) goto L_0x04ff;
    L_0x04f6:
        r4 = 0;
    L_0x04f7:
        r69 = r5.getSentFile(r6, r4);
        r69 = (org.telegram.tgnet.TLRPC.TL_photo) r69;
        goto L_0x0390;
    L_0x04ff:
        r4 = 3;
        goto L_0x04f7;
    L_0x0501:
        r4 = r13.isVideo;
        if (r4 == 0) goto L_0x07fa;
    L_0x0505:
        r82 = 0;
        r84 = 0;
        if (r91 == 0) goto L_0x0745;
    L_0x050b:
        r26 = 0;
    L_0x050d:
        if (r91 != 0) goto L_0x07d9;
    L_0x050f:
        if (r26 != 0) goto L_0x051c;
    L_0x0511:
        r4 = r13.path;
        r5 = "mp4";
        r4 = r4.endsWith(r5);
        if (r4 == 0) goto L_0x07d9;
    L_0x051c:
        r0 = r13.path;
        r68 = r0;
        r0 = r13.path;
        r66 = r0;
        r80 = new java.io.File;
        r0 = r80;
        r1 = r66;
        r0.<init>(r1);
        r78 = 0;
        r64 = 0;
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r0 = r66;
        r4 = r4.append(r0);
        r10 = r80.length();
        r4 = r4.append(r10);
        r5 = "_";
        r4 = r4.append(r5);
        r10 = r80.lastModified();
        r4 = r4.append(r10);
        r66 = r4.toString();
        if (r26 == 0) goto L_0x05d9;
    L_0x0559:
        r0 = r26;
        r0 = r0.muted;
        r64 = r0;
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r0 = r66;
        r4 = r4.append(r0);
        r0 = r26;
        r10 = r0.estimatedDuration;
        r4 = r4.append(r10);
        r5 = "_";
        r4 = r4.append(r5);
        r0 = r26;
        r10 = r0.startTime;
        r4 = r4.append(r10);
        r5 = "_";
        r4 = r4.append(r5);
        r0 = r26;
        r10 = r0.endTime;
        r5 = r4.append(r10);
        r0 = r26;
        r4 = r0.muted;
        if (r4 == 0) goto L_0x0756;
    L_0x0596:
        r4 = "_m";
    L_0x0599:
        r4 = r5.append(r4);
        r66 = r4.toString();
        r0 = r26;
        r4 = r0.resultWidth;
        r0 = r26;
        r5 = r0.originalWidth;
        if (r4 == r5) goto L_0x05c9;
    L_0x05ab:
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r0 = r66;
        r4 = r4.append(r0);
        r5 = "_";
        r4 = r4.append(r5);
        r0 = r26;
        r5 = r0.resultWidth;
        r4 = r4.append(r5);
        r66 = r4.toString();
    L_0x05c9:
        r0 = r26;
        r4 = r0.startTime;
        r10 = 0;
        r4 = (r4 > r10 ? 1 : (r4 == r10 ? 0 : -1));
        if (r4 < 0) goto L_0x075b;
    L_0x05d3:
        r0 = r26;
        r0 = r0.startTime;
        r78 = r0;
    L_0x05d9:
        r48 = 0;
        if (r58 != 0) goto L_0x05f0;
    L_0x05dd:
        r4 = r13.ttl;
        if (r4 != 0) goto L_0x05f0;
    L_0x05e1:
        r5 = org.telegram.messenger.MessagesStorage.getInstance(r90);
        if (r58 != 0) goto L_0x075f;
    L_0x05e7:
        r4 = 2;
    L_0x05e8:
        r0 = r66;
        r48 = r5.getSentFile(r0, r4);
        r48 = (org.telegram.tgnet.TLRPC.TL_document) r48;
    L_0x05f0:
        if (r48 != 0) goto L_0x06df;
    L_0x05f2:
        r4 = r13.path;
        r0 = r78;
        r82 = createVideoThumbnail(r4, r0);
        if (r82 != 0) goto L_0x0603;
    L_0x05fc:
        r4 = r13.path;
        r5 = 1;
        r82 = android.media.ThumbnailUtils.createVideoThumbnail(r4, r5);
    L_0x0603:
        r4 = 1119092736; // 0x42b40000 float:90.0 double:5.529052754E-315;
        r5 = 1119092736; // 0x42b40000 float:90.0 double:5.529052754E-315;
        r6 = 55;
        r0 = r82;
        r1 = r58;
        r77 = org.telegram.messenger.ImageLoader.scaleAndSaveImage(r0, r4, r5, r6, r1);
        if (r82 == 0) goto L_0x0617;
    L_0x0613:
        if (r77 == 0) goto L_0x0617;
    L_0x0615:
        r82 = 0;
    L_0x0617:
        r48 = new org.telegram.tgnet.TLRPC$TL_document;
        r48.<init>();
        r0 = r77;
        r1 = r48;
        r1.thumb = r0;
        r0 = r48;
        r4 = r0.thumb;
        if (r4 != 0) goto L_0x0762;
    L_0x0628:
        r4 = new org.telegram.tgnet.TLRPC$TL_photoSizeEmpty;
        r4.<init>();
        r0 = r48;
        r0.thumb = r4;
        r0 = r48;
        r4 = r0.thumb;
        r5 = "s";
        r4.type = r5;
    L_0x063a:
        r4 = "video/mp4";
        r0 = r48;
        r0.mime_type = r4;
        r4 = org.telegram.messenger.UserConfig.getInstance(r90);
        r5 = 0;
        r4.saveConfig(r5);
        if (r58 == 0) goto L_0x0774;
    L_0x064b:
        r4 = 66;
        r0 = r51;
        if (r0 < r4) goto L_0x076d;
    L_0x0651:
        r40 = new org.telegram.tgnet.TLRPC$TL_documentAttributeVideo;
        r40.<init>();
    L_0x0656:
        r0 = r48;
        r4 = r0.attributes;
        r0 = r40;
        r4.add(r0);
        if (r26 == 0) goto L_0x07c0;
    L_0x0661:
        r4 = r26.needConvert();
        if (r4 == 0) goto L_0x07c0;
    L_0x0667:
        r0 = r26;
        r4 = r0.muted;
        if (r4 == 0) goto L_0x0780;
    L_0x066d:
        r0 = r48;
        r4 = r0.attributes;
        r5 = new org.telegram.tgnet.TLRPC$TL_documentAttributeAnimated;
        r5.<init>();
        r4.add(r5);
        r4 = r13.path;
        r0 = r40;
        r1 = r26;
        fillVideoAttribute(r4, r0, r1);
        r0 = r40;
        r4 = r0.w;
        r0 = r26;
        r0.originalWidth = r4;
        r0 = r40;
        r4 = r0.h;
        r0 = r26;
        r0.originalHeight = r4;
        r0 = r26;
        r4 = r0.resultWidth;
        r0 = r40;
        r0.w = r4;
        r0 = r26;
        r4 = r0.resultHeight;
        r0 = r40;
        r0.h = r4;
    L_0x06a2:
        r0 = r26;
        r4 = r0.estimatedSize;
        r4 = (int) r4;
        r0 = r48;
        r0.size = r4;
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "-2147483648_";
        r4 = r4.append(r5);
        r5 = org.telegram.messenger.SharedConfig.getLastLocalId();
        r4 = r4.append(r5);
        r5 = ".mp4";
        r4 = r4.append(r5);
        r53 = r4.toString();
        r45 = new java.io.File;
        r4 = 4;
        r4 = org.telegram.messenger.FileLoader.getDirectory(r4);
        r0 = r45;
        r1 = r53;
        r0.<init>(r4, r1);
        org.telegram.messenger.SharedConfig.saveConfig();
        r68 = r45.getAbsolutePath();
    L_0x06df:
        r27 = r48;
        r67 = r66;
        r28 = r68;
        r9 = new java.util.HashMap;
        r9.<init>();
        r22 = r82;
        r23 = r84;
        if (r66 == 0) goto L_0x06f8;
    L_0x06f0:
        r4 = "originalPath";
        r0 = r66;
        r9.put(r4, r0);
    L_0x06f8:
        if (r64 != 0) goto L_0x072f;
    L_0x06fa:
        if (r92 == 0) goto L_0x072f;
    L_0x06fc:
        r71 = r71 + 1;
        r4 = "groupId";
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "";
        r5 = r5.append(r6);
        r0 = r54;
        r5 = r5.append(r0);
        r5 = r5.toString();
        r9.put(r4, r5);
        r4 = 10;
        r0 = r71;
        if (r0 == r4) goto L_0x0724;
    L_0x0720:
        r4 = r46 + -1;
        if (r14 != r4) goto L_0x072f;
    L_0x0724:
        r4 = "final";
        r5 = "1";
        r9.put(r4, r5);
        r60 = 0;
    L_0x072f:
        r21 = new org.telegram.messenger.SendMessagesHelper$$Lambda$25;
        r24 = r93;
        r25 = r90;
        r29 = r9;
        r30 = r88;
        r32 = r94;
        r33 = r13;
        r21.<init>(r22, r23, r24, r25, r26, r27, r28, r29, r30, r32, r33);
        org.telegram.messenger.AndroidUtilities.runOnUIThread(r21);
        goto L_0x02f5;
    L_0x0745:
        r4 = r13.videoEditedInfo;
        if (r4 == 0) goto L_0x074f;
    L_0x0749:
        r0 = r13.videoEditedInfo;
        r26 = r0;
    L_0x074d:
        goto L_0x050d;
    L_0x074f:
        r4 = r13.path;
        r26 = createCompressionSettings(r4);
        goto L_0x074d;
    L_0x0756:
        r4 = "";
        goto L_0x0599;
    L_0x075b:
        r78 = 0;
        goto L_0x05d9;
    L_0x075f:
        r4 = 5;
        goto L_0x05e8;
    L_0x0762:
        r0 = r48;
        r4 = r0.thumb;
        r5 = "s";
        r4.type = r5;
        goto L_0x063a;
    L_0x076d:
        r40 = new org.telegram.tgnet.TLRPC$TL_documentAttributeVideo_layer65;
        r40.<init>();
        goto L_0x0656;
    L_0x0774:
        r40 = new org.telegram.tgnet.TLRPC$TL_documentAttributeVideo;
        r40.<init>();
        r4 = 1;
        r0 = r40;
        r0.supports_streaming = r4;
        goto L_0x0656;
    L_0x0780:
        r0 = r26;
        r4 = r0.estimatedDuration;
        r10 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r4 = r4 / r10;
        r4 = (int) r4;
        r0 = r40;
        r0.duration = r4;
        r0 = r26;
        r4 = r0.rotationValue;
        r5 = 90;
        if (r4 == r5) goto L_0x079c;
    L_0x0794:
        r0 = r26;
        r4 = r0.rotationValue;
        r5 = 270; // 0x10e float:3.78E-43 double:1.334E-321;
        if (r4 != r5) goto L_0x07ae;
    L_0x079c:
        r0 = r26;
        r4 = r0.resultHeight;
        r0 = r40;
        r0.w = r4;
        r0 = r26;
        r4 = r0.resultWidth;
        r0 = r40;
        r0.h = r4;
        goto L_0x06a2;
    L_0x07ae:
        r0 = r26;
        r4 = r0.resultWidth;
        r0 = r40;
        r0.w = r4;
        r0 = r26;
        r4 = r0.resultHeight;
        r0 = r40;
        r0.h = r4;
        goto L_0x06a2;
    L_0x07c0:
        r4 = r80.exists();
        if (r4 == 0) goto L_0x07cf;
    L_0x07c6:
        r4 = r80.length();
        r4 = (int) r4;
        r0 = r48;
        r0.size = r4;
    L_0x07cf:
        r4 = r13.path;
        r5 = 0;
        r0 = r40;
        fillVideoAttribute(r4, r0, r5);
        goto L_0x06df;
    L_0x07d9:
        r0 = r13.path;
        r30 = r0;
        r0 = r13.path;
        r31 = r0;
        r32 = 0;
        r33 = 0;
        r0 = r13.caption;
        r37 = r0;
        r0 = r13.entities;
        r38 = r0;
        r29 = r90;
        r34 = r88;
        r36 = r94;
        r39 = r93;
        prepareSendingDocumentInternal(r29, r30, r31, r32, r33, r34, r36, r37, r38, r39);
        goto L_0x02f5;
    L_0x07fa:
        r0 = r13.path;
        r66 = r0;
        r0 = r13.path;
        r81 = r0;
        if (r81 != 0) goto L_0x0814;
    L_0x0804:
        r4 = r13.uri;
        if (r4 == 0) goto L_0x0814;
    L_0x0808:
        r4 = r13.uri;
        r81 = org.telegram.messenger.AndroidUtilities.getPath(r4);
        r4 = r13.uri;
        r66 = r4.toString();
    L_0x0814:
        r57 = 0;
        if (r91 == 0) goto L_0x085b;
    L_0x0818:
        r57 = 1;
        r4 = new java.io.File;
        r0 = r81;
        r4.<init>(r0);
        r52 = org.telegram.messenger.FileLoader.getFileExtension(r4);
    L_0x0825:
        if (r57 == 0) goto L_0x08ca;
    L_0x0827:
        if (r72 != 0) goto L_0x083d;
    L_0x0829:
        r72 = new java.util.ArrayList;
        r72.<init>();
        r75 = new java.util.ArrayList;
        r75.<init>();
        r73 = new java.util.ArrayList;
        r73.<init>();
        r74 = new java.util.ArrayList;
        r74.<init>();
    L_0x083d:
        r0 = r72;
        r1 = r81;
        r0.add(r1);
        r0 = r75;
        r1 = r66;
        r0.add(r1);
        r4 = r13.caption;
        r0 = r73;
        r0.add(r4);
        r4 = r13.entities;
        r0 = r74;
        r0.add(r4);
        goto L_0x02f5;
    L_0x085b:
        if (r81 == 0) goto L_0x0888;
    L_0x085d:
        r4 = ".gif";
        r0 = r81;
        r4 = r0.endsWith(r4);
        if (r4 != 0) goto L_0x0873;
    L_0x0868:
        r4 = ".webp";
        r0 = r81;
        r4 = r0.endsWith(r4);
        if (r4 == 0) goto L_0x0888;
    L_0x0873:
        r4 = ".gif";
        r0 = r81;
        r4 = r0.endsWith(r4);
        if (r4 == 0) goto L_0x0884;
    L_0x087e:
        r52 = "gif";
    L_0x0881:
        r57 = 1;
        goto L_0x0825;
    L_0x0884:
        r52 = "webp";
        goto L_0x0881;
    L_0x0888:
        if (r81 != 0) goto L_0x0825;
    L_0x088a:
        r4 = r13.uri;
        if (r4 == 0) goto L_0x0825;
    L_0x088e:
        r4 = r13.uri;
        r4 = org.telegram.messenger.MediaController.isGif(r4);
        if (r4 == 0) goto L_0x08ac;
    L_0x0896:
        r57 = 1;
        r4 = r13.uri;
        r66 = r4.toString();
        r4 = r13.uri;
        r5 = "gif";
        r81 = org.telegram.messenger.MediaController.copyFileToCache(r4, r5);
        r52 = "gif";
        goto L_0x0825;
    L_0x08ac:
        r4 = r13.uri;
        r4 = org.telegram.messenger.MediaController.isWebp(r4);
        if (r4 == 0) goto L_0x0825;
    L_0x08b4:
        r57 = 1;
        r4 = r13.uri;
        r66 = r4.toString();
        r4 = r13.uri;
        r5 = "webp";
        r81 = org.telegram.messenger.MediaController.copyFileToCache(r4, r5);
        r52 = "webp";
        goto L_0x0825;
    L_0x08ca:
        if (r81 == 0) goto L_0x096e;
    L_0x08cc:
        r80 = new java.io.File;
        r80.<init>(r81);
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r0 = r66;
        r4 = r4.append(r0);
        r10 = r80.length();
        r4 = r4.append(r10);
        r5 = "_";
        r4 = r4.append(r5);
        r10 = r80.lastModified();
        r4 = r4.append(r10);
        r66 = r4.toString();
    L_0x08f7:
        r69 = 0;
        if (r86 == 0) goto L_0x0976;
    L_0x08fb:
        r0 = r86;
        r85 = r0.get(r13);
        r85 = (org.telegram.messenger.SendMessagesHelper.MediaSendPrepareWorker) r85;
        r0 = r85;
        r0 = r0.photo;
        r69 = r0;
        if (r69 != 0) goto L_0x0918;
    L_0x090b:
        r0 = r85;
        r4 = r0.sync;	 Catch:{ Exception -> 0x0971 }
        r4.await();	 Catch:{ Exception -> 0x0971 }
    L_0x0912:
        r0 = r85;
        r0 = r0.photo;
        r69 = r0;
    L_0x0918:
        if (r69 == 0) goto L_0x0a23;
    L_0x091a:
        r18 = r69;
        r9 = new java.util.HashMap;
        r9.<init>();
        r4 = r13.masks;
        if (r4 == 0) goto L_0x09b8;
    L_0x0925:
        r4 = r13.masks;
        r4 = r4.isEmpty();
        if (r4 != 0) goto L_0x09b8;
    L_0x092d:
        r4 = 1;
    L_0x092e:
        r0 = r69;
        r0.has_stickers = r4;
        if (r4 == 0) goto L_0x09cc;
    L_0x0934:
        r76 = new org.telegram.tgnet.SerializedData;
        r4 = r13.masks;
        r4 = r4.size();
        r4 = r4 * 20;
        r4 = r4 + 4;
        r0 = r76;
        r0.<init>(r4);
        r4 = r13.masks;
        r4 = r4.size();
        r0 = r76;
        r0.writeInt32(r4);
        r41 = 0;
    L_0x0952:
        r4 = r13.masks;
        r4 = r4.size();
        r0 = r41;
        if (r0 >= r4) goto L_0x09bb;
    L_0x095c:
        r4 = r13.masks;
        r0 = r41;
        r4 = r4.get(r0);
        r4 = (org.telegram.tgnet.TLRPC.InputDocument) r4;
        r0 = r76;
        r4.serializeToStream(r0);
        r41 = r41 + 1;
        goto L_0x0952;
    L_0x096e:
        r66 = 0;
        goto L_0x08f7;
    L_0x0971:
        r49 = move-exception;
        org.telegram.messenger.FileLog.e(r49);
        goto L_0x0912;
    L_0x0976:
        if (r58 != 0) goto L_0x09a4;
    L_0x0978:
        r4 = r13.ttl;
        if (r4 != 0) goto L_0x09a4;
    L_0x097c:
        r5 = org.telegram.messenger.MessagesStorage.getInstance(r90);
        if (r58 != 0) goto L_0x09b4;
    L_0x0982:
        r4 = 0;
    L_0x0983:
        r0 = r66;
        r69 = r5.getSentFile(r0, r4);
        r69 = (org.telegram.tgnet.TLRPC.TL_photo) r69;
        if (r69 != 0) goto L_0x09a4;
    L_0x098d:
        r4 = r13.uri;
        if (r4 == 0) goto L_0x09a4;
    L_0x0991:
        r5 = org.telegram.messenger.MessagesStorage.getInstance(r90);
        r4 = r13.uri;
        r6 = org.telegram.messenger.AndroidUtilities.getPath(r4);
        if (r58 != 0) goto L_0x09b6;
    L_0x099d:
        r4 = 0;
    L_0x099e:
        r69 = r5.getSentFile(r6, r4);
        r69 = (org.telegram.tgnet.TLRPC.TL_photo) r69;
    L_0x09a4:
        if (r69 != 0) goto L_0x0918;
    L_0x09a6:
        r4 = getInstance(r90);
        r5 = r13.path;
        r6 = r13.uri;
        r69 = r4.generatePhotoSizes(r5, r6);
        goto L_0x0918;
    L_0x09b4:
        r4 = 3;
        goto L_0x0983;
    L_0x09b6:
        r4 = 3;
        goto L_0x099e;
    L_0x09b8:
        r4 = 0;
        goto L_0x092e;
    L_0x09bb:
        r4 = "masks";
        r5 = r76.toByteArray();
        r5 = org.telegram.messenger.Utilities.bytesToHex(r5);
        r9.put(r4, r5);
        r76.cleanup();
    L_0x09cc:
        if (r66 == 0) goto L_0x09d6;
    L_0x09ce:
        r4 = "originalPath";
        r0 = r66;
        r9.put(r4, r0);
    L_0x09d6:
        if (r92 == 0) goto L_0x0a0b;
    L_0x09d8:
        r71 = r71 + 1;
        r4 = "groupId";
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "";
        r5 = r5.append(r6);
        r0 = r54;
        r5 = r5.append(r0);
        r5 = r5.toString();
        r9.put(r4, r5);
        r4 = 10;
        r0 = r71;
        if (r0 == r4) goto L_0x0a00;
    L_0x09fc:
        r4 = r46 + -1;
        if (r14 != r4) goto L_0x0a0b;
    L_0x0a00:
        r4 = "final";
        r5 = "1";
        r9.put(r4, r5);
        r60 = 0;
    L_0x0a0b:
        r29 = new org.telegram.messenger.SendMessagesHelper$$Lambda$26;
        r30 = r93;
        r31 = r90;
        r32 = r18;
        r33 = r9;
        r34 = r88;
        r36 = r94;
        r37 = r13;
        r29.<init>(r30, r31, r32, r33, r34, r36, r37);
        org.telegram.messenger.AndroidUtilities.runOnUIThread(r29);
        goto L_0x02f5;
    L_0x0a23:
        if (r72 != 0) goto L_0x0a39;
    L_0x0a25:
        r72 = new java.util.ArrayList;
        r72.<init>();
        r75 = new java.util.ArrayList;
        r75.<init>();
        r73 = new java.util.ArrayList;
        r73.<init>();
        r74 = new java.util.ArrayList;
        r74.<init>();
    L_0x0a39:
        r0 = r72;
        r1 = r81;
        r0.add(r1);
        r0 = r75;
        r1 = r66;
        r0.add(r1);
        r4 = r13.caption;
        r0 = r73;
        r0.add(r4);
        r4 = r13.entities;
        r0 = r74;
        r0.add(r4);
        goto L_0x02f5;
    L_0x0a57:
        r4 = 0;
        r4 = (r60 > r4 ? 1 : (r60 == r4 ? 0 : -1));
        if (r4 == 0) goto L_0x0a6b;
    L_0x0a5d:
        r62 = r60;
        r4 = new org.telegram.messenger.SendMessagesHelper$$Lambda$27;
        r0 = r90;
        r1 = r62;
        r4.<init>(r0, r1);
        org.telegram.messenger.AndroidUtilities.runOnUIThread(r4);
    L_0x0a6b:
        if (r95 == 0) goto L_0x0a70;
    L_0x0a6d:
        r95.releasePermission();
    L_0x0a70:
        if (r72 == 0) goto L_0x0ab1;
    L_0x0a72:
        r4 = r72.isEmpty();
        if (r4 != 0) goto L_0x0ab1;
    L_0x0a78:
        r14 = 0;
    L_0x0a79:
        r4 = r72.size();
        if (r14 >= r4) goto L_0x0ab1;
    L_0x0a7f:
        r0 = r72;
        r30 = r0.get(r14);
        r30 = (java.lang.String) r30;
        r0 = r75;
        r31 = r0.get(r14);
        r31 = (java.lang.String) r31;
        r32 = 0;
        r0 = r73;
        r37 = r0.get(r14);
        r37 = (java.lang.CharSequence) r37;
        r0 = r74;
        r38 = r0.get(r14);
        r38 = (java.util.ArrayList) r38;
        r29 = r90;
        r33 = r52;
        r34 = r88;
        r36 = r94;
        r39 = r93;
        prepareSendingDocumentInternal(r29, r30, r31, r32, r33, r34, r36, r37, r38, r39);
        r14 = r14 + 1;
        goto L_0x0a79;
    L_0x0ab1:
        r4 = org.telegram.messenger.BuildVars.LOGS_ENABLED;
        if (r4 == 0) goto L_0x0ad2;
    L_0x0ab5:
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "total send time = ";
        r4 = r4.append(r5);
        r10 = java.lang.System.currentTimeMillis();
        r10 = r10 - r42;
        r4 = r4.append(r10);
        r4 = r4.toString();
        org.telegram.messenger.FileLog.d(r4);
    L_0x0ad2:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.SendMessagesHelper.lambda$prepareSendingMedia$59$SendMessagesHelper(java.util.ArrayList, long, int, boolean, boolean, org.telegram.messenger.MessageObject, org.telegram.messenger.MessageObject, android.support.v13.view.inputmethod.InputContentInfoCompat):void");
    }

    static final /* synthetic */ void lambda$null$53$SendMessagesHelper(MediaSendPrepareWorker worker, int currentAccount, SendingMediaInfo info) {
        worker.photo = getInstance(currentAccount).generatePhotoSizes(info.path, info.uri);
        worker.sync.countDown();
    }

    static final /* synthetic */ void lambda$null$54$SendMessagesHelper(MessageObject editingMessageObject, int currentAccount, TL_document documentFinal, String pathFinal, HashMap params, long dialog_id, MessageObject reply_to_msg, SendingMediaInfo info) {
        if (editingMessageObject != null) {
            getInstance(currentAccount).editMessageMedia(editingMessageObject, null, null, documentFinal, pathFinal, params, false);
            return;
        }
        getInstance(currentAccount).sendMessage(documentFinal, null, pathFinal, dialog_id, reply_to_msg, info.caption, info.entities, null, params, 0);
    }

    static final /* synthetic */ void lambda$null$55$SendMessagesHelper(MessageObject editingMessageObject, int currentAccount, TL_photo photoFinal, boolean needDownloadHttpFinal, SendingMediaInfo info, HashMap params, long dialog_id, MessageObject reply_to_msg) {
        if (editingMessageObject != null) {
            getInstance(currentAccount).editMessageMedia(editingMessageObject, photoFinal, null, null, needDownloadHttpFinal ? info.searchImage.imageUrl : null, params, false);
        } else {
            getInstance(currentAccount).sendMessage(photoFinal, needDownloadHttpFinal ? info.searchImage.imageUrl : null, dialog_id, reply_to_msg, info.caption, info.entities, null, params, info.ttl);
        }
    }

    static final /* synthetic */ void lambda$null$56$SendMessagesHelper(Bitmap thumbFinal, String thumbKeyFinal, MessageObject editingMessageObject, int currentAccount, VideoEditedInfo videoEditedInfo, TL_document videoFinal, String finalPath, HashMap params, long dialog_id, MessageObject reply_to_msg, SendingMediaInfo info) {
        if (!(thumbFinal == null || thumbKeyFinal == null)) {
            ImageLoader.getInstance().putImageToCache(new BitmapDrawable(thumbFinal), thumbKeyFinal);
        }
        if (editingMessageObject != null) {
            getInstance(currentAccount).editMessageMedia(editingMessageObject, null, videoEditedInfo, videoFinal, finalPath, params, false);
            return;
        }
        getInstance(currentAccount).sendMessage(videoFinal, videoEditedInfo, finalPath, dialog_id, reply_to_msg, info.caption, info.entities, null, params, info.ttl);
    }

    static final /* synthetic */ void lambda$null$57$SendMessagesHelper(MessageObject editingMessageObject, int currentAccount, TL_photo photoFinal, HashMap params, long dialog_id, MessageObject reply_to_msg, SendingMediaInfo info) {
        if (editingMessageObject != null) {
            getInstance(currentAccount).editMessageMedia(editingMessageObject, photoFinal, null, null, null, params, false);
            return;
        }
        getInstance(currentAccount).sendMessage(photoFinal, null, dialog_id, reply_to_msg, info.caption, info.entities, null, params, info.ttl);
    }

    static final /* synthetic */ void lambda$null$58$SendMessagesHelper(int currentAccount, long lastGroupIdFinal) {
        SendMessagesHelper instance = getInstance(currentAccount);
        ArrayList<DelayedMessage> arrayList = (ArrayList) instance.delayedMessages.get("group_" + lastGroupIdFinal);
        if (arrayList != null && !arrayList.isEmpty()) {
            DelayedMessage message = (DelayedMessage) arrayList.get(0);
            MessageObject prevMessage = (MessageObject) message.messageObjects.get(message.messageObjects.size() - 1);
            message.finalGroupMessage = prevMessage.getId();
            prevMessage.messageOwner.params.put("final", "1");
            messages_Messages messagesRes = new TL_messages_messages();
            messagesRes.messages.add(prevMessage.messageOwner);
            MessagesStorage.getInstance(currentAccount).putMessages(messagesRes, message.peer, -2, 0, false);
            instance.sendReadyToSendGroup(message, true, true);
        }
    }

    private static void fillVideoAttribute(String videoPath, TL_documentAttributeVideo attributeVideo, VideoEditedInfo videoEditedInfo) {
        Throwable e;
        MediaPlayer mp;
        Throwable th;
        boolean infoObtained = false;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            MediaMetadataRetriever mediaMetadataRetriever2 = new MediaMetadataRetriever();
            try {
                mediaMetadataRetriever2.setDataSource(videoPath);
                String width = mediaMetadataRetriever2.extractMetadata(18);
                if (width != null) {
                    attributeVideo.w = Integer.parseInt(width);
                }
                String height = mediaMetadataRetriever2.extractMetadata(19);
                if (height != null) {
                    attributeVideo.h = Integer.parseInt(height);
                }
                String duration = mediaMetadataRetriever2.extractMetadata(9);
                if (duration != null) {
                    attributeVideo.duration = (int) Math.ceil((double) (((float) Long.parseLong(duration)) / 1000.0f));
                }
                if (VERSION.SDK_INT >= 17) {
                    String rotation = mediaMetadataRetriever2.extractMetadata(24);
                    if (rotation != null) {
                        int val = Utilities.parseInt(rotation).intValue();
                        if (videoEditedInfo != null) {
                            videoEditedInfo.rotationValue = val;
                        } else if (val == 90 || val == 270) {
                            int temp = attributeVideo.w;
                            attributeVideo.w = attributeVideo.h;
                            attributeVideo.h = temp;
                        }
                    }
                }
                infoObtained = true;
                if (mediaMetadataRetriever2 != null) {
                    try {
                        mediaMetadataRetriever2.release();
                    } catch (Throwable e2) {
                        FileLog.e(e2);
                        mediaMetadataRetriever = mediaMetadataRetriever2;
                    }
                }
                mediaMetadataRetriever = mediaMetadataRetriever2;
            } catch (Exception e3) {
                e2 = e3;
                mediaMetadataRetriever = mediaMetadataRetriever2;
                try {
                    FileLog.e(e2);
                    if (mediaMetadataRetriever != null) {
                        try {
                            mediaMetadataRetriever.release();
                        } catch (Throwable e22) {
                            FileLog.e(e22);
                        }
                    }
                    if (infoObtained) {
                        try {
                            mp = MediaPlayer.create(ApplicationLoader.applicationContext, Uri.fromFile(new File(videoPath)));
                            if (mp == null) {
                                attributeVideo.duration = (int) Math.ceil((double) (((float) mp.getDuration()) / 1000.0f));
                                attributeVideo.w = mp.getVideoWidth();
                                attributeVideo.h = mp.getVideoHeight();
                                mp.release();
                            }
                        } catch (Throwable e222) {
                            FileLog.e(e222);
                            return;
                        }
                    }
                } catch (Throwable th2) {
                    th = th2;
                    if (mediaMetadataRetriever != null) {
                        try {
                            mediaMetadataRetriever.release();
                        } catch (Throwable e2222) {
                            FileLog.e(e2222);
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                mediaMetadataRetriever = mediaMetadataRetriever2;
                if (mediaMetadataRetriever != null) {
                    mediaMetadataRetriever.release();
                }
                throw th;
            }
        } catch (Exception e4) {
            e2222 = e4;
            FileLog.e(e2222);
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
            if (infoObtained) {
                mp = MediaPlayer.create(ApplicationLoader.applicationContext, Uri.fromFile(new File(videoPath)));
                if (mp == null) {
                    attributeVideo.duration = (int) Math.ceil((double) (((float) mp.getDuration()) / 1000.0f));
                    attributeVideo.w = mp.getVideoWidth();
                    attributeVideo.h = mp.getVideoHeight();
                    mp.release();
                }
            }
        }
        if (infoObtained) {
            mp = MediaPlayer.create(ApplicationLoader.applicationContext, Uri.fromFile(new File(videoPath)));
            if (mp == null) {
                attributeVideo.duration = (int) Math.ceil((double) (((float) mp.getDuration()) / 1000.0f));
                attributeVideo.w = mp.getVideoWidth();
                attributeVideo.h = mp.getVideoHeight();
                mp.release();
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static android.graphics.Bitmap createVideoThumbnail(java.lang.String r13, long r14) {
        /*
        r12 = 1;
        r0 = 0;
        r5 = new android.media.MediaMetadataRetriever;
        r5.<init>();
        r5.setDataSource(r13);	 Catch:{ Exception -> 0x0017, all -> 0x001e }
        r10 = 1;
        r0 = r5.getFrameAtTime(r14, r10);	 Catch:{ Exception -> 0x0017, all -> 0x001e }
        r5.release();	 Catch:{ RuntimeException -> 0x0051 }
    L_0x0012:
        if (r0 != 0) goto L_0x0023;
    L_0x0014:
        r10 = 0;
        r1 = r0;
    L_0x0016:
        return r10;
    L_0x0017:
        r10 = move-exception;
        r5.release();	 Catch:{ RuntimeException -> 0x001c }
        goto L_0x0012;
    L_0x001c:
        r10 = move-exception;
        goto L_0x0012;
    L_0x001e:
        r10 = move-exception;
        r5.release();	 Catch:{ RuntimeException -> 0x0053 }
    L_0x0022:
        throw r10;
    L_0x0023:
        r9 = r0.getWidth();
        r3 = r0.getHeight();
        r4 = java.lang.Math.max(r9, r3);
        r10 = 90;
        if (r4 <= r10) goto L_0x004e;
    L_0x0033:
        r10 = 1119092736; // 0x42b40000 float:90.0 double:5.529052754E-315;
        r11 = (float) r4;
        r6 = r10 / r11;
        r10 = (float) r9;
        r10 = r10 * r6;
        r8 = java.lang.Math.round(r10);
        r10 = (float) r3;
        r10 = r10 * r6;
        r2 = java.lang.Math.round(r10);
        r7 = org.telegram.messenger.Bitmaps.createScaledBitmap(r0, r8, r2, r12);
        if (r7 == r0) goto L_0x004e;
    L_0x004a:
        r0.recycle();
        r0 = r7;
    L_0x004e:
        r1 = r0;
        r10 = r0;
        goto L_0x0016;
    L_0x0051:
        r10 = move-exception;
        goto L_0x0012;
    L_0x0053:
        r11 = move-exception;
        goto L_0x0022;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.SendMessagesHelper.createVideoThumbnail(java.lang.String, long):android.graphics.Bitmap");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static org.telegram.messenger.VideoEditedInfo createCompressionSettings(java.lang.String r52) {
        /*
        r39 = 0;
        r25 = 0;
        r8 = 0;
        r40 = 0;
        r44 = 0;
        r6 = 0;
        r42 = 25;
        r19 = new com.coremedia.iso.IsoFile;	 Catch:{ Exception -> 0x0151 }
        r0 = r19;
        r1 = r52;
        r0.<init>(r1);	 Catch:{ Exception -> 0x0151 }
        r43 = "/moov/trak/";
        r0 = r19;
        r1 = r43;
        r11 = com.googlecode.mp4parser.util.Path.getPaths(r0, r1);	 Catch:{ Exception -> 0x0151 }
        r43 = "/moov/trak/mdia/minf/stbl/stsd/mp4a/";
        r0 = r19;
        r1 = r43;
        r10 = com.googlecode.mp4parser.util.Path.getPath(r0, r1);	 Catch:{ Exception -> 0x0151 }
        if (r10 != 0) goto L_0x0038;
    L_0x002e:
        r43 = org.telegram.messenger.BuildVars.LOGS_ENABLED;	 Catch:{ Exception -> 0x0151 }
        if (r43 == 0) goto L_0x0038;
    L_0x0032:
        r43 = "video hasn't mp4a atom";
        org.telegram.messenger.FileLog.d(r43);	 Catch:{ Exception -> 0x0151 }
    L_0x0038:
        r43 = "/moov/trak/mdia/minf/stbl/stsd/avc1/";
        r0 = r19;
        r1 = r43;
        r10 = com.googlecode.mp4parser.util.Path.getPath(r0, r1);	 Catch:{ Exception -> 0x0151 }
        if (r10 != 0) goto L_0x0052;
    L_0x0045:
        r43 = org.telegram.messenger.BuildVars.LOGS_ENABLED;	 Catch:{ Exception -> 0x0151 }
        if (r43 == 0) goto L_0x004f;
    L_0x0049:
        r43 = "video hasn't avc1 atom";
        org.telegram.messenger.FileLog.d(r43);	 Catch:{ Exception -> 0x0151 }
    L_0x004f:
        r41 = 0;
    L_0x0051:
        return r41;
    L_0x0052:
        r5 = 0;
    L_0x0053:
        r43 = r11.size();	 Catch:{ Exception -> 0x0151 }
        r0 = r43;
        if (r5 >= r0) goto L_0x0184;
    L_0x005b:
        r9 = r11.get(r5);	 Catch:{ Exception -> 0x0151 }
        r9 = (com.coremedia.iso.boxes.Box) r9;	 Catch:{ Exception -> 0x0151 }
        r0 = r9;
        r0 = (com.coremedia.iso.boxes.TrackBox) r0;	 Catch:{ Exception -> 0x0151 }
        r38 = r0;
        r28 = 0;
        r36 = 0;
        r22 = 0;
        r23 = 0;
        r22 = r38.getMediaBox();	 Catch:{ Exception -> 0x014b }
        r23 = r22.getMediaHeaderBox();	 Catch:{ Exception -> 0x014b }
        r43 = r22.getMediaInformationBox();	 Catch:{ Exception -> 0x014b }
        r43 = r43.getSampleTableBox();	 Catch:{ Exception -> 0x014b }
        r27 = r43.getSampleSizeBox();	 Catch:{ Exception -> 0x014b }
        r33 = r27.getSampleSizes();	 Catch:{ Exception -> 0x014b }
        r4 = 0;
    L_0x0087:
        r0 = r33;
        r0 = r0.length;	 Catch:{ Exception -> 0x014b }
        r43 = r0;
        r0 = r43;
        if (r4 >= r0) goto L_0x0097;
    L_0x0090:
        r46 = r33[r4];	 Catch:{ Exception -> 0x014b }
        r28 = r28 + r46;
        r4 = r4 + 1;
        goto L_0x0087;
    L_0x0097:
        r46 = r23.getDuration();	 Catch:{ Exception -> 0x014b }
        r0 = r46;
        r0 = (float) r0;	 Catch:{ Exception -> 0x014b }
        r43 = r0;
        r46 = r23.getTimescale();	 Catch:{ Exception -> 0x014b }
        r0 = r46;
        r0 = (float) r0;
        r46 = r0;
        r40 = r43 / r46;
        r46 = 8;
        r46 = r46 * r28;
        r0 = r46;
        r0 = (float) r0;
        r43 = r0;
        r43 = r43 / r40;
        r0 = r43;
        r0 = (int) r0;
        r43 = r0;
        r0 = r43;
        r0 = (long) r0;
        r36 = r0;
    L_0x00c0:
        r18 = r38.getTrackHeaderBox();	 Catch:{ Exception -> 0x0151 }
        r46 = r18.getWidth();	 Catch:{ Exception -> 0x0151 }
        r48 = 0;
        r43 = (r46 > r48 ? 1 : (r46 == r48 ? 0 : -1));
        if (r43 == 0) goto L_0x0181;
    L_0x00ce:
        r46 = r18.getHeight();	 Catch:{ Exception -> 0x0151 }
        r48 = 0;
        r43 = (r46 > r48 ? 1 : (r46 == r48 ? 0 : -1));
        if (r43 == 0) goto L_0x0181;
    L_0x00d8:
        if (r39 == 0) goto L_0x00f2;
    L_0x00da:
        r46 = r39.getWidth();	 Catch:{ Exception -> 0x0151 }
        r48 = r18.getWidth();	 Catch:{ Exception -> 0x0151 }
        r43 = (r46 > r48 ? 1 : (r46 == r48 ? 0 : -1));
        if (r43 < 0) goto L_0x00f2;
    L_0x00e6:
        r46 = r39.getHeight();	 Catch:{ Exception -> 0x0151 }
        r48 = r18.getHeight();	 Catch:{ Exception -> 0x0151 }
        r43 = (r46 > r48 ? 1 : (r46 == r48 ? 0 : -1));
        if (r43 >= 0) goto L_0x017d;
    L_0x00f2:
        r39 = r18;
        r46 = 100000; // 0x186a0 float:1.4013E-40 double:4.94066E-319;
        r46 = r36 / r46;
        r48 = 100000; // 0x186a0 float:1.4013E-40 double:4.94066E-319;
        r46 = r46 * r48;
        r0 = r46;
        r8 = (int) r0;	 Catch:{ Exception -> 0x0151 }
        r25 = r8;
        r43 = 900000; // 0xdbba0 float:1.261169E-39 double:4.44659E-318;
        r0 = r43;
        if (r8 <= r0) goto L_0x010d;
    L_0x010a:
        r8 = 900000; // 0xdbba0 float:1.261169E-39 double:4.44659E-318;
    L_0x010d:
        r44 = r44 + r28;
        if (r22 == 0) goto L_0x017d;
    L_0x0111:
        if (r23 == 0) goto L_0x017d;
    L_0x0113:
        r43 = r22.getMediaInformationBox();	 Catch:{ Exception -> 0x0151 }
        r43 = r43.getSampleTableBox();	 Catch:{ Exception -> 0x0151 }
        r35 = r43.getTimeToSampleBox();	 Catch:{ Exception -> 0x0151 }
        if (r35 == 0) goto L_0x017d;
    L_0x0121:
        r17 = r35.getEntries();	 Catch:{ Exception -> 0x0151 }
        r14 = 0;
        r43 = r17.size();	 Catch:{ Exception -> 0x0151 }
        r46 = 11;
        r0 = r43;
        r1 = r46;
        r32 = java.lang.Math.min(r0, r1);	 Catch:{ Exception -> 0x0151 }
        r4 = 1;
    L_0x0136:
        r0 = r32;
        if (r4 >= r0) goto L_0x0159;
    L_0x013a:
        r0 = r17;
        r43 = r0.get(r4);	 Catch:{ Exception -> 0x0151 }
        r43 = (com.coremedia.iso.boxes.TimeToSampleBox.Entry) r43;	 Catch:{ Exception -> 0x0151 }
        r46 = r43.getDelta();	 Catch:{ Exception -> 0x0151 }
        r14 = r14 + r46;
        r4 = r4 + 1;
        goto L_0x0136;
    L_0x014b:
        r16 = move-exception;
        org.telegram.messenger.FileLog.e(r16);	 Catch:{ Exception -> 0x0151 }
        goto L_0x00c0;
    L_0x0151:
        r16 = move-exception;
        org.telegram.messenger.FileLog.e(r16);
        r41 = 0;
        goto L_0x0051;
    L_0x0159:
        r46 = 0;
        r43 = (r14 > r46 ? 1 : (r14 == r46 ? 0 : -1));
        if (r43 == 0) goto L_0x017d;
    L_0x015f:
        r46 = r23.getTimescale();	 Catch:{ Exception -> 0x0151 }
        r0 = r46;
        r0 = (double) r0;	 Catch:{ Exception -> 0x0151 }
        r46 = r0;
        r43 = r32 + -1;
        r0 = r43;
        r0 = (long) r0;	 Catch:{ Exception -> 0x0151 }
        r48 = r0;
        r48 = r14 / r48;
        r0 = r48;
        r0 = (double) r0;
        r48 = r0;
        r46 = r46 / r48;
        r0 = r46;
        r0 = (int) r0;
        r42 = r0;
    L_0x017d:
        r5 = r5 + 1;
        goto L_0x0053;
    L_0x0181:
        r6 = r6 + r28;
        goto L_0x017d;
    L_0x0184:
        if (r39 != 0) goto L_0x0194;
    L_0x0186:
        r43 = org.telegram.messenger.BuildVars.LOGS_ENABLED;
        if (r43 == 0) goto L_0x0190;
    L_0x018a:
        r43 = "video hasn't trackHeaderBox atom";
        org.telegram.messenger.FileLog.d(r43);
    L_0x0190:
        r41 = 0;
        goto L_0x0051;
    L_0x0194:
        r43 = android.os.Build.VERSION.SDK_INT;
        r46 = 18;
        r0 = r43;
        r1 = r46;
        if (r0 >= r1) goto L_0x0259;
    L_0x019e:
        r43 = "video/avc";
        r12 = org.telegram.messenger.MediaController.selectCodec(r43);	 Catch:{ Exception -> 0x0254 }
        if (r12 != 0) goto L_0x01b5;
    L_0x01a7:
        r43 = org.telegram.messenger.BuildVars.LOGS_ENABLED;	 Catch:{ Exception -> 0x0254 }
        if (r43 == 0) goto L_0x01b1;
    L_0x01ab:
        r43 = "no codec info for video/avc";
        org.telegram.messenger.FileLog.d(r43);	 Catch:{ Exception -> 0x0254 }
    L_0x01b1:
        r41 = 0;
        goto L_0x0051;
    L_0x01b5:
        r24 = r12.getName();	 Catch:{ Exception -> 0x0254 }
        r43 = "OMX.google.h264.encoder";
        r0 = r24;
        r1 = r43;
        r43 = r0.equals(r1);	 Catch:{ Exception -> 0x0254 }
        if (r43 != 0) goto L_0x0214;
    L_0x01c6:
        r43 = "OMX.ST.VFM.H264Enc";
        r0 = r24;
        r1 = r43;
        r43 = r0.equals(r1);	 Catch:{ Exception -> 0x0254 }
        if (r43 != 0) goto L_0x0214;
    L_0x01d3:
        r43 = "OMX.Exynos.avc.enc";
        r0 = r24;
        r1 = r43;
        r43 = r0.equals(r1);	 Catch:{ Exception -> 0x0254 }
        if (r43 != 0) goto L_0x0214;
    L_0x01e0:
        r43 = "OMX.MARVELL.VIDEO.HW.CODA7542ENCODER";
        r0 = r24;
        r1 = r43;
        r43 = r0.equals(r1);	 Catch:{ Exception -> 0x0254 }
        if (r43 != 0) goto L_0x0214;
    L_0x01ed:
        r43 = "OMX.MARVELL.VIDEO.H264ENCODER";
        r0 = r24;
        r1 = r43;
        r43 = r0.equals(r1);	 Catch:{ Exception -> 0x0254 }
        if (r43 != 0) goto L_0x0214;
    L_0x01fa:
        r43 = "OMX.k3.video.encoder.avc";
        r0 = r24;
        r1 = r43;
        r43 = r0.equals(r1);	 Catch:{ Exception -> 0x0254 }
        if (r43 != 0) goto L_0x0214;
    L_0x0207:
        r43 = "OMX.TI.DUCATI1.VIDEO.H264E";
        r0 = r24;
        r1 = r43;
        r43 = r0.equals(r1);	 Catch:{ Exception -> 0x0254 }
        if (r43 == 0) goto L_0x023b;
    L_0x0214:
        r43 = org.telegram.messenger.BuildVars.LOGS_ENABLED;	 Catch:{ Exception -> 0x0254 }
        if (r43 == 0) goto L_0x0237;
    L_0x0218:
        r43 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0254 }
        r43.<init>();	 Catch:{ Exception -> 0x0254 }
        r46 = "unsupported encoder = ";
        r0 = r43;
        r1 = r46;
        r43 = r0.append(r1);	 Catch:{ Exception -> 0x0254 }
        r0 = r43;
        r1 = r24;
        r43 = r0.append(r1);	 Catch:{ Exception -> 0x0254 }
        r43 = r43.toString();	 Catch:{ Exception -> 0x0254 }
        org.telegram.messenger.FileLog.d(r43);	 Catch:{ Exception -> 0x0254 }
    L_0x0237:
        r41 = 0;
        goto L_0x0051;
    L_0x023b:
        r43 = "video/avc";
        r0 = r43;
        r43 = org.telegram.messenger.MediaController.selectColorFormat(r12, r0);	 Catch:{ Exception -> 0x0254 }
        if (r43 != 0) goto L_0x0259;
    L_0x0246:
        r43 = org.telegram.messenger.BuildVars.LOGS_ENABLED;	 Catch:{ Exception -> 0x0254 }
        if (r43 == 0) goto L_0x0250;
    L_0x024a:
        r43 = "no color format for video/avc";
        org.telegram.messenger.FileLog.d(r43);	 Catch:{ Exception -> 0x0254 }
    L_0x0250:
        r41 = 0;
        goto L_0x0051;
    L_0x0254:
        r16 = move-exception;
        r41 = 0;
        goto L_0x0051;
    L_0x0259:
        r43 = 1148846080; // 0x447a0000 float:1000.0 double:5.676053805E-315;
        r40 = r40 * r43;
        r41 = new org.telegram.messenger.VideoEditedInfo;
        r41.<init>();
        r46 = -1;
        r0 = r46;
        r2 = r41;
        r2.startTime = r0;
        r46 = -1;
        r0 = r46;
        r2 = r41;
        r2.endTime = r0;
        r0 = r41;
        r0.bitrate = r8;
        r0 = r52;
        r1 = r41;
        r1.originalPath = r0;
        r0 = r42;
        r1 = r41;
        r1.framerate = r0;
        r0 = r40;
        r0 = (double) r0;
        r46 = r0;
        r46 = java.lang.Math.ceil(r46);
        r0 = r46;
        r0 = (long) r0;
        r46 = r0;
        r0 = r46;
        r2 = r41;
        r2.estimatedDuration = r0;
        r46 = r39.getWidth();
        r0 = r46;
        r0 = (int) r0;
        r43 = r0;
        r0 = r43;
        r1 = r41;
        r1.originalWidth = r0;
        r0 = r43;
        r1 = r41;
        r1.resultWidth = r0;
        r46 = r39.getHeight();
        r0 = r46;
        r0 = (int) r0;
        r43 = r0;
        r0 = r43;
        r1 = r41;
        r1.originalHeight = r0;
        r0 = r43;
        r1 = r41;
        r1.resultHeight = r0;
        r20 = r39.getMatrix();
        r43 = com.googlecode.mp4parser.util.Matrix.ROTATE_90;
        r0 = r20;
        r1 = r43;
        r43 = r0.equals(r1);
        if (r43 == 0) goto L_0x03e4;
    L_0x02d0:
        r43 = 90;
        r0 = r43;
        r1 = r41;
        r1.rotationValue = r0;
    L_0x02d8:
        r26 = org.telegram.messenger.MessagesController.getGlobalMainSettings();
        r43 = "compress_video2";
        r46 = 1;
        r0 = r26;
        r1 = r43;
        r2 = r46;
        r31 = r0.getInt(r1, r2);
        r0 = r41;
        r0 = r0.originalWidth;
        r43 = r0;
        r46 = 1280; // 0x500 float:1.794E-42 double:6.324E-321;
        r0 = r43;
        r1 = r46;
        if (r0 > r1) goto L_0x0307;
    L_0x02f9:
        r0 = r41;
        r0 = r0.originalHeight;
        r43 = r0;
        r46 = 1280; // 0x500 float:1.794E-42 double:6.324E-321;
        r0 = r43;
        r1 = r46;
        if (r0 <= r1) goto L_0x041a;
    L_0x0307:
        r13 = 5;
    L_0x0308:
        r0 = r31;
        if (r0 < r13) goto L_0x030e;
    L_0x030c:
        r31 = r13 + -1;
    L_0x030e:
        r43 = r13 + -1;
        r0 = r31;
        r1 = r43;
        if (r0 == r1) goto L_0x039f;
    L_0x0316:
        switch(r31) {
            case 0: goto L_0x047a;
            case 1: goto L_0x0481;
            case 2: goto L_0x0488;
            default: goto L_0x0319;
        };
    L_0x0319:
        r34 = 2500000; // 0x2625a0 float:3.503246E-39 double:1.235164E-317;
        r21 = 1151336448; // 0x44a00000 float:1280.0 double:5.68835786E-315;
    L_0x031e:
        r0 = r41;
        r0 = r0.originalWidth;
        r43 = r0;
        r0 = r41;
        r0 = r0.originalHeight;
        r46 = r0;
        r0 = r43;
        r1 = r46;
        if (r0 <= r1) goto L_0x048f;
    L_0x0330:
        r0 = r41;
        r0 = r0.originalWidth;
        r43 = r0;
        r0 = r43;
        r0 = (float) r0;
        r43 = r0;
        r30 = r21 / r43;
    L_0x033d:
        r0 = r41;
        r0 = r0.originalWidth;
        r43 = r0;
        r0 = r43;
        r0 = (float) r0;
        r43 = r0;
        r43 = r43 * r30;
        r46 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r43 = r43 / r46;
        r43 = java.lang.Math.round(r43);
        r43 = r43 * 2;
        r0 = r43;
        r1 = r41;
        r1.resultWidth = r0;
        r0 = r41;
        r0 = r0.originalHeight;
        r43 = r0;
        r0 = r43;
        r0 = (float) r0;
        r43 = r0;
        r43 = r43 * r30;
        r46 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r43 = r43 / r46;
        r43 = java.lang.Math.round(r43);
        r43 = r43 * 2;
        r0 = r43;
        r1 = r41;
        r1.resultHeight = r0;
        if (r8 == 0) goto L_0x039f;
    L_0x0379:
        r0 = r25;
        r0 = (float) r0;
        r43 = r0;
        r43 = r43 / r30;
        r0 = r43;
        r0 = (int) r0;
        r43 = r0;
        r0 = r34;
        r1 = r43;
        r8 = java.lang.Math.min(r0, r1);
        r43 = r8 / 8;
        r0 = r43;
        r0 = (float) r0;
        r43 = r0;
        r43 = r43 * r40;
        r46 = 1148846080; // 0x447a0000 float:1000.0 double:5.676053805E-315;
        r43 = r43 / r46;
        r0 = r43;
        r0 = (long) r0;
        r44 = r0;
    L_0x039f:
        r43 = r13 + -1;
        r0 = r31;
        r1 = r43;
        if (r0 != r1) goto L_0x049e;
    L_0x03a7:
        r0 = r41;
        r0 = r0.originalWidth;
        r43 = r0;
        r0 = r43;
        r1 = r41;
        r1.resultWidth = r0;
        r0 = r41;
        r0 = r0.originalHeight;
        r43 = r0;
        r0 = r43;
        r1 = r41;
        r1.resultHeight = r0;
        r0 = r25;
        r1 = r41;
        r1.bitrate = r0;
        r43 = new java.io.File;
        r0 = r43;
        r1 = r52;
        r0.<init>(r1);
        r46 = r43.length();
        r0 = r46;
        r0 = (int) r0;
        r43 = r0;
        r0 = r43;
        r0 = (long) r0;
        r46 = r0;
        r0 = r46;
        r2 = r41;
        r2.estimatedSize = r0;
        goto L_0x0051;
    L_0x03e4:
        r43 = com.googlecode.mp4parser.util.Matrix.ROTATE_180;
        r0 = r20;
        r1 = r43;
        r43 = r0.equals(r1);
        if (r43 == 0) goto L_0x03fa;
    L_0x03f0:
        r43 = 180; // 0xb4 float:2.52E-43 double:8.9E-322;
        r0 = r43;
        r1 = r41;
        r1.rotationValue = r0;
        goto L_0x02d8;
    L_0x03fa:
        r43 = com.googlecode.mp4parser.util.Matrix.ROTATE_270;
        r0 = r20;
        r1 = r43;
        r43 = r0.equals(r1);
        if (r43 == 0) goto L_0x0410;
    L_0x0406:
        r43 = 270; // 0x10e float:3.78E-43 double:1.334E-321;
        r0 = r43;
        r1 = r41;
        r1.rotationValue = r0;
        goto L_0x02d8;
    L_0x0410:
        r43 = 0;
        r0 = r43;
        r1 = r41;
        r1.rotationValue = r0;
        goto L_0x02d8;
    L_0x041a:
        r0 = r41;
        r0 = r0.originalWidth;
        r43 = r0;
        r46 = 848; // 0x350 float:1.188E-42 double:4.19E-321;
        r0 = r43;
        r1 = r46;
        if (r0 > r1) goto L_0x0436;
    L_0x0428:
        r0 = r41;
        r0 = r0.originalHeight;
        r43 = r0;
        r46 = 848; // 0x350 float:1.188E-42 double:4.19E-321;
        r0 = r43;
        r1 = r46;
        if (r0 <= r1) goto L_0x0439;
    L_0x0436:
        r13 = 4;
        goto L_0x0308;
    L_0x0439:
        r0 = r41;
        r0 = r0.originalWidth;
        r43 = r0;
        r46 = 640; // 0x280 float:8.97E-43 double:3.16E-321;
        r0 = r43;
        r1 = r46;
        if (r0 > r1) goto L_0x0455;
    L_0x0447:
        r0 = r41;
        r0 = r0.originalHeight;
        r43 = r0;
        r46 = 640; // 0x280 float:8.97E-43 double:3.16E-321;
        r0 = r43;
        r1 = r46;
        if (r0 <= r1) goto L_0x0458;
    L_0x0455:
        r13 = 3;
        goto L_0x0308;
    L_0x0458:
        r0 = r41;
        r0 = r0.originalWidth;
        r43 = r0;
        r46 = 480; // 0x1e0 float:6.73E-43 double:2.37E-321;
        r0 = r43;
        r1 = r46;
        if (r0 > r1) goto L_0x0474;
    L_0x0466:
        r0 = r41;
        r0 = r0.originalHeight;
        r43 = r0;
        r46 = 480; // 0x1e0 float:6.73E-43 double:2.37E-321;
        r0 = r43;
        r1 = r46;
        if (r0 <= r1) goto L_0x0477;
    L_0x0474:
        r13 = 2;
        goto L_0x0308;
    L_0x0477:
        r13 = 1;
        goto L_0x0308;
    L_0x047a:
        r21 = 1138229248; // 0x43d80000 float:432.0 double:5.623599685E-315;
        r34 = 400000; // 0x61a80 float:5.6052E-40 double:1.976263E-318;
        goto L_0x031e;
    L_0x0481:
        r21 = 1142947840; // 0x44200000 float:640.0 double:5.646912627E-315;
        r34 = 900000; // 0xdbba0 float:1.261169E-39 double:4.44659E-318;
        goto L_0x031e;
    L_0x0488:
        r21 = 1146355712; // 0x44540000 float:848.0 double:5.66374975E-315;
        r34 = 1100000; // 0x10c8e0 float:1.541428E-39 double:5.43472E-318;
        goto L_0x031e;
    L_0x048f:
        r0 = r41;
        r0 = r0.originalHeight;
        r43 = r0;
        r0 = r43;
        r0 = (float) r0;
        r43 = r0;
        r30 = r21 / r43;
        goto L_0x033d;
    L_0x049e:
        r0 = r41;
        r0.bitrate = r8;
        r46 = r6 + r44;
        r0 = r46;
        r0 = (int) r0;
        r43 = r0;
        r0 = r43;
        r0 = (long) r0;
        r46 = r0;
        r0 = r46;
        r2 = r41;
        r2.estimatedSize = r0;
        r0 = r41;
        r0 = r0.estimatedSize;
        r46 = r0;
        r0 = r41;
        r0 = r0.estimatedSize;
        r48 = r0;
        r50 = 32768; // 0x8000 float:4.5918E-41 double:1.61895E-319;
        r48 = r48 / r50;
        r50 = 16;
        r48 = r48 * r50;
        r46 = r46 + r48;
        r0 = r46;
        r2 = r41;
        r2.estimatedSize = r0;
        goto L_0x0051;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.SendMessagesHelper.createCompressionSettings(java.lang.String):org.telegram.messenger.VideoEditedInfo");
    }

    public static void prepareSendingVideo(String videoPath, long estimatedSize, long duration, int width, int height, VideoEditedInfo info, long dialog_id, MessageObject reply_to_msg, CharSequence caption, ArrayList<MessageEntity> entities, int ttl, MessageObject editingMessageObject) {
        if (videoPath != null && videoPath.length() != 0) {
            new Thread(new SendMessagesHelper$$Lambda$20(info, videoPath, dialog_id, duration, ttl, UserConfig.selectedAccount, height, width, estimatedSize, caption, editingMessageObject, reply_to_msg, entities)).start();
        }
    }

    static final /* synthetic */ void lambda$prepareSendingVideo$61$SendMessagesHelper(VideoEditedInfo info, String videoPath, long dialog_id, long duration, int ttl, int currentAccount, int height, int width, long estimatedSize, CharSequence caption, MessageObject editingMessageObject, MessageObject reply_to_msg, ArrayList entities) {
        VideoEditedInfo videoEditedInfo = info != null ? info : createCompressionSettings(videoPath);
        boolean isEncrypted = ((int) dialog_id) == 0;
        boolean isRound = videoEditedInfo != null && videoEditedInfo.roundVideo;
        Bitmap thumb = null;
        String thumbKey = null;
        if (videoEditedInfo == null) {
            if (!(videoPath.endsWith("mp4") || isRound)) {
                prepareSendingDocumentInternal(currentAccount, videoPath, videoPath, null, null, dialog_id, reply_to_msg, caption, entities, editingMessageObject);
                return;
            }
        }
        String path = videoPath;
        String originalPath = videoPath;
        File file = new File(originalPath);
        long startTime = 0;
        originalPath = originalPath + file.length() + "_" + file.lastModified();
        if (videoEditedInfo != null) {
            if (!isRound) {
                originalPath = originalPath + duration + "_" + videoEditedInfo.startTime + "_" + videoEditedInfo.endTime + (videoEditedInfo.muted ? "_m" : TtmlNode.ANONYMOUS_REGION_ID);
                if (videoEditedInfo.resultWidth != videoEditedInfo.originalWidth) {
                    originalPath = originalPath + "_" + videoEditedInfo.resultWidth;
                }
            }
            startTime = videoEditedInfo.startTime >= 0 ? videoEditedInfo.startTime : 0;
        }
        TL_document tL_document = null;
        if (!isEncrypted && ttl == 0) {
            tL_document = (TL_document) MessagesStorage.getInstance(currentAccount).getSentFile(originalPath, !isEncrypted ? 2 : 5);
        }
        if (tL_document == null) {
            TL_documentAttributeVideo attributeVideo;
            thumb = createVideoThumbnail(videoPath, startTime);
            if (thumb == null) {
                thumb = ThumbnailUtils.createVideoThumbnail(videoPath, 1);
            }
            PhotoSize size = ImageLoader.scaleAndSaveImage(thumb, 90.0f, 90.0f, 55, isEncrypted);
            if (!(thumb == null || size == null)) {
                if (!isRound) {
                    thumb = null;
                } else if (isEncrypted) {
                    Utilities.blurBitmap(thumb, 7, VERSION.SDK_INT < 21 ? 0 : 1, thumb.getWidth(), thumb.getHeight(), thumb.getRowBytes());
                    thumbKey = String.format(size.location.volume_id + "_" + size.location.local_id + "@%d_%d_b2", new Object[]{Integer.valueOf((int) (((float) AndroidUtilities.roundMessageSize) / AndroidUtilities.density)), Integer.valueOf((int) (((float) AndroidUtilities.roundMessageSize) / AndroidUtilities.density))});
                } else {
                    Utilities.blurBitmap(thumb, 3, VERSION.SDK_INT < 21 ? 0 : 1, thumb.getWidth(), thumb.getHeight(), thumb.getRowBytes());
                    thumbKey = String.format(size.location.volume_id + "_" + size.location.local_id + "@%d_%d_b", new Object[]{Integer.valueOf((int) (((float) AndroidUtilities.roundMessageSize) / AndroidUtilities.density)), Integer.valueOf((int) (((float) AndroidUtilities.roundMessageSize) / AndroidUtilities.density))});
                }
            }
            tL_document = new TL_document();
            tL_document.thumb = size;
            if (tL_document.thumb == null) {
                tL_document.thumb = new TL_photoSizeEmpty();
                tL_document.thumb.type = "s";
            } else {
                tL_document.thumb.type = "s";
            }
            tL_document.mime_type = MimeTypes.VIDEO_MP4;
            UserConfig.getInstance(currentAccount).saveConfig(false);
            if (isEncrypted) {
                EncryptedChat encryptedChat = MessagesController.getInstance(currentAccount).getEncryptedChat(Integer.valueOf((int) (dialog_id >> 32)));
                if (encryptedChat != null) {
                    if (AndroidUtilities.getPeerLayerVersion(encryptedChat.layer) >= 66) {
                        attributeVideo = new TL_documentAttributeVideo();
                    } else {
                        attributeVideo = new TL_documentAttributeVideo_layer65();
                    }
                } else {
                    return;
                }
            }
            attributeVideo = new TL_documentAttributeVideo();
            attributeVideo.supports_streaming = true;
            attributeVideo.round_message = isRound;
            tL_document.attributes.add(attributeVideo);
            if (videoEditedInfo == null || !videoEditedInfo.needConvert()) {
                if (file.exists()) {
                    tL_document.size = (int) file.length();
                }
                fillVideoAttribute(videoPath, attributeVideo, null);
            } else {
                if (videoEditedInfo.muted) {
                    tL_document.attributes.add(new TL_documentAttributeAnimated());
                    fillVideoAttribute(videoPath, attributeVideo, videoEditedInfo);
                    videoEditedInfo.originalWidth = attributeVideo.w;
                    videoEditedInfo.originalHeight = attributeVideo.h;
                    attributeVideo.w = videoEditedInfo.resultWidth;
                    attributeVideo.h = videoEditedInfo.resultHeight;
                } else {
                    attributeVideo.duration = (int) (duration / 1000);
                    if (videoEditedInfo.rotationValue == 90 || videoEditedInfo.rotationValue == 270) {
                        attributeVideo.w = height;
                        attributeVideo.h = width;
                    } else {
                        attributeVideo.w = width;
                        attributeVideo.h = height;
                    }
                }
                tL_document.size = (int) estimatedSize;
                file = new File(FileLoader.getDirectory(4), "-2147483648_" + SharedConfig.getLastLocalId() + ".mp4");
                SharedConfig.saveConfig();
                path = file.getAbsolutePath();
            }
        }
        TL_document videoFinal = tL_document;
        String originalPathFinal = originalPath;
        String finalPath = path;
        HashMap<String, String> params = new HashMap();
        Bitmap thumbFinal = thumb;
        String thumbKeyFinal = thumbKey;
        String captionFinal = caption != null ? caption.toString() : TtmlNode.ANONYMOUS_REGION_ID;
        if (originalPath != null) {
            params.put("originalPath", originalPath);
        }
        AndroidUtilities.runOnUIThread(new SendMessagesHelper$$Lambda$21(thumbFinal, thumbKeyFinal, editingMessageObject, currentAccount, videoEditedInfo, videoFinal, finalPath, params, dialog_id, reply_to_msg, captionFinal, entities, ttl));
    }

    static final /* synthetic */ void lambda$null$60$SendMessagesHelper(Bitmap thumbFinal, String thumbKeyFinal, MessageObject editingMessageObject, int currentAccount, VideoEditedInfo videoEditedInfo, TL_document videoFinal, String finalPath, HashMap params, long dialog_id, MessageObject reply_to_msg, String captionFinal, ArrayList entities, int ttl) {
        if (!(thumbFinal == null || thumbKeyFinal == null)) {
            ImageLoader.getInstance().putImageToCache(new BitmapDrawable(thumbFinal), thumbKeyFinal);
        }
        if (editingMessageObject != null) {
            getInstance(currentAccount).editMessageMedia(editingMessageObject, null, videoEditedInfo, videoFinal, finalPath, params, false);
        } else {
            getInstance(currentAccount).sendMessage(videoFinal, videoEditedInfo, finalPath, dialog_id, reply_to_msg, captionFinal, entities, null, params, ttl);
        }
    }
}
